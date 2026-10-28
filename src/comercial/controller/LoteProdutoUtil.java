package comercial.controller;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import util.BDConexao;

/**
 * Regras de lote ao nivel do PRODUTO (formulario de produtos).
 *
 * Nao depende da entidade TbProduto nem do ProdutosController: a configuracao
 * de lote (tb_produto.controla_lote e dias_alerta_validade) le-se e grava-se
 * por SQL proprio, depois de o produto ser gravado como ja e hoje.
 *
 * Usa apenas  conexao.executeQuery(sql)  e  conexao.executeUpdate(sql),
 * como o resto do teu codigo. Todo o SQL e montado so com inteiros e
 * constantes (sem texto do utilizador).
 */
public final class LoteProdutoUtil
{

    public static final String LOTE_INICIAL = "INICIAL";
    public static final String VALIDADE_PROVISORIA = "2099-12-31";
    public static final int DIAS_ALERTA_DEFAULT = 90;
    public static final int DIAS_ALERTA_MIN = 1;
    public static final int DIAS_ALERTA_MAX = 1095;
    public static final int DIAS_CRITICO = 30;

    private LoteProdutoUtil()
    {
    }

    /**
     * O negocio usa lotes? Hoje so a Farmacia. Ignora maiusculas, acentos e
     * espacos ("Farmacia", "Farmácia", " FARMACIA " sao todos farmacia).
     * Usar nos formularios (compras, vendas, produtos) para decidir o que se
     * MOSTRA. Null devolve false.
     */
    public static boolean negocioUsaLotes( String negocio )
    {
        if ( negocio == null )
        {
            return false;
        }
        String n = java.text.Normalizer.normalize( negocio, java.text.Normalizer.Form.NFD )
                .replaceAll( "\\p{M}", "" ).trim();
        return n.equalsIgnoreCase( "Farmacia" );
    }

    /**
     * Configuracao de lote de um produto.
     */
    public static final class Config
    {

        public final boolean controlaLote;
        public final int diasAlerta;

        public Config( boolean controlaLote, int diasAlerta )
        {
            this.controlaLote = controlaLote;
            this.diasAlerta = diasAlerta;
        }
    }

    // ------------------------------------------------------------------
    // Configuracao
    // ------------------------------------------------------------------
    /**
     * Le a configuracao. Se o produto nao existir devolve o valor por defeito
     * (controla lote, 90 dias).
     */
    public static Config lerConfiguracao( BDConexao conexao, int codProduto )
    {
        String sql = "SELECT controla_lote, dias_alerta_validade FROM tb_produto WHERE codigo = " + codProduto;
        try
        {
            ResultSet rs = conexao.executeQuery( sql );
            if ( rs != null && rs.next() )
            {
                return new Config( rs.getInt( "controla_lote" ) == 1, limitarDias( rs.getInt( "dias_alerta_validade" ) ) );
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return new Config( true, DIAS_ALERTA_DEFAULT );
    }

    /**
     * Grava a configuracao e CONFIRMA lendo de volta (o executeUpdate do
     * BDConexao nao diz se falhou).
     *
     * @return true se o valor lido de volta e o pretendido
     */
    public static boolean gravarConfiguracao( BDConexao conexao, int codProduto, boolean controlaLote, int diasAlerta )
    {
        int dias = limitarDias( diasAlerta );
        conexao.executeUpdate( "UPDATE tb_produto SET controla_lote = " + ( controlaLote ? 1 : 0 )
                + ", dias_alerta_validade = " + dias + " WHERE codigo = " + codProduto );
        Config lida = lerConfiguracao( conexao, codProduto );
        return lida.controlaLote == controlaLote && lida.diasAlerta == dias;
    }

    static int limitarDias( int dias )
    {
        if ( dias < DIAS_ALERTA_MIN )
        {
            return DIAS_ALERTA_DEFAULT;
        }
        return Math.min( dias, DIAS_ALERTA_MAX );
    }

    // ------------------------------------------------------------------
    // Desactivar o controlo de lote
    // ------------------------------------------------------------------
    /**
     * Quantidade ainda em lotes (todos os armazens, lotes nao desactivados).
     */
    public static double stockEmLotes( BDConexao conexao, int codProduto )
    {
        String sql = "SELECT COALESCE( SUM( qtd ), 0 ) AS total FROM lote "
                + "WHERE fk_produto = " + codProduto + " AND estado <> 'DESACTIVO'";
        try
        {
            ResultSet rs = conexao.executeQuery( sql );
            if ( rs != null && rs.next() )
            {
                return rs.getDouble( "total" );
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Pode-se deixar de controlar lote neste produto? Devolve null se sim,
     * ou a mensagem a mostrar ao utilizador. Se ainda houver stock em lotes,
     * a venda deixaria de o descontar dos lotes e os totais divergiriam.
     */
    public static String validarDesactivacao( BDConexao conexao, int codProduto )
    {
        double emLotes = stockEmLotes( conexao, codProduto );
        if ( emLotes > 0 )
        {
            return "Este produto ainda tem " + BigDecimal.valueOf( emLotes ).stripTrailingZeros().toPlainString()
                    + " unidade(s) em lotes.\nPara deixar de controlar lote, o stock em lotes tem de estar a zero "
                    + "(venda, transferência ou baixa).";
        }
        return null;
    }

    // ------------------------------------------------------------------
    // Activar o controlo de lote num produto que ja tem stock
    // ------------------------------------------------------------------
    /**
     * Cria o lote INICIAL (validade provisoria 31/12/2099) para o stock que o
     * produto ja tem em cada armazem e que ainda nao esta em lote. E
     * repetivel: nao duplica um INICIAL que ja exista.
     *
     * @return numero de armazens para os quais foi criado lote
     */
    public static int criarLotesIniciais( BDConexao conexao, int codProduto )
    {
        // 1) le TUDO primeiro (o ResultSet do BDConexao pode ser invalidado pela consulta seguinte)
        List<double[]> armazens = new ArrayList<>(); // {codArmazem, quantidade}
        try
        {
            ResultSet rs = conexao.executeQuery( "SELECT cod_armazem, SUM( quantidade_existente ) AS qtd FROM tb_stock "
                    + "WHERE cod_produto_codigo = " + codProduto + " AND quantidade_existente > 0 GROUP BY cod_armazem" );
            while ( rs != null && rs.next() )
            {
                armazens.add( new double[]
                {
                    rs.getInt( "cod_armazem" ), rs.getDouble( "qtd" )
                } );
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
            return 0;
        }

        // 2) cria o INICIAL onde ainda nao existe
        int criados = 0;
        for ( double[] a : armazens )
        {
            int armazem = ( int ) a[0];
            if ( existeLoteInicial( conexao, codProduto, armazem ) )
            {
                continue;
            }
            conexao.executeUpdate( "INSERT INTO lote( fk_produto, fk_armazem, numero_lote, data_entrada, data_validade, qtd, estado ) "
                    + "VALUES( " + codProduto + ", " + armazem + ", '" + LOTE_INICIAL + "', CURDATE(), '" + VALIDADE_PROVISORIA + "', "
                    + BigDecimal.valueOf( a[1] ).toPlainString() + ", 'ACTIVO' )" );
            criados++;
        }
        return criados;
    }

    private static boolean existeLoteInicial( BDConexao conexao, int codProduto, int armazem )
    {
        try
        {
            ResultSet rs = conexao.executeQuery( "SELECT COUNT( * ) AS n FROM lote WHERE fk_produto = " + codProduto
                    + " AND fk_armazem = " + armazem + " AND numero_lote = '" + LOTE_INICIAL + "'" );
            return rs != null && rs.next() && rs.getInt( "n" ) > 0;
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
            return true; // na duvida nao cria (evita duplicar)
        }
    }

    // ------------------------------------------------------------------
    // Listagem dos lotes do produto
    // ------------------------------------------------------------------
    /**
     * Linhas para a tabela "Lotes do produto":
     * {lote, armazem, fabrico, validade, dias (Long), qtd (Double), estado, situacao}
     */
    public static List<Object[]> listarLotes( BDConexao conexao, int codProduto, boolean incluirSemStock )
    {
        int diasAlerta = lerConfiguracao( conexao, codProduto ).diasAlerta;
        String sql = "SELECT l.numero_lote, a.designacao AS armazem, l.data_fabrica, l.data_validade, "
                + "DATEDIFF( l.data_validade, CURDATE() ) AS dias, l.qtd, l.estado "
                + "FROM lote l INNER JOIN tb_armazem a ON a.codigo = l.fk_armazem "
                + "WHERE l.fk_produto = " + codProduto + ( incluirSemStock ? "" : " AND l.qtd > 0" )
                + " ORDER BY l.data_validade ASC, l.id ASC";

        List<Object[]> linhas = new ArrayList<>();
        try
        {
            ResultSet rs = conexao.executeQuery( sql );
            while ( rs != null && rs.next() )
            {
                String numero = rs.getString( "numero_lote" );
                Date validade = rs.getDate( "data_validade" );
                long dias = rs.getLong( "dias" );
                double qtd = rs.getDouble( "qtd" );
                String estado = rs.getString( "estado" );
                linhas.add( new Object[]
                {
                    numero,
                    rs.getString( "armazem" ),
                    EntradaLoteCompra.formatar( rs.getDate( "data_fabrica" ) ),
                    EntradaLoteCompra.formatar( validade ),
                    dias,
                    qtd,
                    estado,
                    classificar( dias, diasAlerta, estado, qtd, numero )
                } );
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return linhas;
    }

    /**
     * Situacao do lote, para cores e filtros.
     */
    public static String classificar( long diasParaValidade, int diasAlerta, String estado, double qtd, String numeroLote )
    {
        if ( "DESACTIVO".equals( estado ) )
        {
            return "DESACTIVADO";
        }
        if ( qtd <= 0 )
        {
            return "SEM STOCK";
        }
        if ( LOTE_INICIAL.equals( numeroLote ) )
        {
            return "REGULARIZAR";
        }
        if ( diasParaValidade < 0 )
        {
            return "CADUCADO";
        }
        if ( diasParaValidade <= Math.min( DIAS_CRITICO, diasAlerta ) )
        {
            return "CRÍTICO";
        }
        if ( diasParaValidade <= diasAlerta )
        {
            return "ATENÇÃO";
        }
        return "NORMAL";
    }
}
