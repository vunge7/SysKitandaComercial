/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comercial.controller;

import java.sql.Connection;
import entity.AnoEconomico;
import entity.Cambio;
import entity.Documento;
import entity.TbArmazem;
import entity.TbBanco;
import entity.TbCliente;
import entity.TbItemVenda;
import entity.TbLugares;
import entity.TbMesas;
import entity.TbPreco;
import entity.TbProduto;
import entity.TbVenda;
import entity.TbUsuario;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import util.BDConexao;
import util.MetodosUtil;

/**
 *
 * @author Martinho Luis
 */
public class MesasController implements EntidadeFactory
{

    private BDConexao conexao;

    public MesasController( BDConexao conexao )
    {
        this.conexao = conexao;
    }

    @Override
    public boolean salvar( Object object )
    {
        TbMesas mesas = (TbMesas) object;
        String INSERT = "INSERT INTO tb_mesas( designacao , "
                + ")"
                + " VALUES("
                + "'" + mesas.getDesignacao() + "' , "
                + " ) ";

        return conexao.executeUpdate( INSERT );

    }

    @Override
    public boolean actualizar( Object object )
    {
        return true;
    }

    @Override
    public boolean eliminar( int codigo )
    {
        String DELETE = "DELETE FROM tb_mesas WHERE pk_mesas = " + codigo;
        return conexao.executeUpdate( DELETE );
    }

    @Override
    public List<TbMesas> listarTodos()
    {

        String FIND_ALL = "SELECT * FROM tb_mesas ORDER BY pk_mesas ASC";
        ResultSet result = conexao.executeQuery( FIND_ALL );
        List<TbMesas> lista_mesas = new ArrayList<>();
        TbMesas mesas;
        try
        {

            while ( result.next() )
            {
                mesas = new TbMesas();
                mesas.setDesignacao( result.getString( "designacao" ) );
                lista_mesas.add( mesas );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return lista_mesas;
    }

    @Override
    public Vector<String> getVector()
    {
        throw new UnsupportedOperationException( "Not supported yet." ); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object findById( int codigo )
    {

        String FIND__BY_CODIGO = "SELECT * FROM tb_mesas WHERE pk_mesas = " + codigo;
        ResultSet result = conexao.executeQuery( FIND__BY_CODIGO );
        TbMesas mesas = null;
        try
        {

            if ( result.next() )
            {
                mesas = new TbMesas();
                mesas.setPkMesas( result.getInt( "pk_mesas" ) );
                mesas.setDesignacao( result.getString( "designacao" ) );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return mesas;

    }

    public Object findByDesignacao( String designacao )
    {

        String FIND__BY_CODIGO = "SELECT * FROM tb_mesas WHERE designacao = '" + designacao + "'";
        ResultSet result = conexao.executeQuery( FIND__BY_CODIGO );
        TbMesas mesas = null;
        try
        {

            if ( result.next() )
            {
                mesas = new TbMesas();
                mesas.setPkMesas( result.getInt( "pk_mesas" ) );
                mesas.setDesignacao( result.getString( "designacao" ) );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return mesas;

    }

    public TbMesas getLastMesa()
    {

        String FIND__BY_CODIGO = "SELECT MAX(pk_mesas) as maximo_id, m.*  FROM tb_mesas m";
        ResultSet result = conexao.executeQuery( FIND__BY_CODIGO );
        TbMesas mesas = null;
        try
        {

            if ( result.next() )
            {
                mesas = new TbMesas();
                mesas.setPkMesas( result.getInt( "maximo_id" ) );
                mesas.setDesignacao( result.getString( "designacao" ) );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return mesas;

    }

    // Retorna o ID da mesa a partir da designação
    public int getIdByDescricao( String designacao ) throws SQLException
    {
        String sql = "SELECT pk_mesas FROM tb_mesas WHERE designacao = ?";

        try ( PreparedStatement ps = conexao.prepareStatement( sql ) )
        {
            ps.setString( 1, designacao );

            try ( ResultSet rs = ps.executeQuery() )
            {
                if ( rs.next() )
                {
                    return rs.getInt( "pk_mesas" );
                }
            }
        }
        return 0; // retorna 0 se não encontrar
    }


    // =====================================================================
    // MESAS DINÂMICAS (plano de sala e cadastro de mesas, como no SysKitandaHotel)
    // =====================================================================
    /** Ordem natural: "MESA 2" antes de "MESA 10"; designações livres no fim. */
    private static final String ORDEM_NATURAL
            = " ORDER BY (m.designacao REGEXP '^MESA [0-9]+$') DESC,"
            + " CAST(TRIM(SUBSTRING(m.designacao, 5)) AS UNSIGNED), m.designacao";

    /** Estado de uma mesa no plano de sala (lido de uma só vez para todas). */
    public static class EstadoMesa
    {

        public int codigo;
        public String designacao;
        public long itens;
        public double total;
        /** Hora do primeiro item ainda por facturar (null com a mesa livre). */
        public java.util.Date desde;
        /** Facturas de hoje da mesa (mesa livre já vendida fica amarela no plano). */
        public int vendasHoje;
        public double valorVendasHoje;

        /** O que vem depois de "MESA " (é o que o formulário de pedidos recebe). */
        public String getNumero()
        {
            return numeroDe( designacao );
        }
    }

    public static String numeroDe( String designacao )
    {
        String d = designacao == null ? "" : designacao.trim();
        return d.toUpperCase().startsWith( "MESA " ) ? d.substring( 5 ).trim() : d;
    }

    /** Todas as mesas, com o código, por ordem natural. */
    public List<TbMesas> listarOrdenadas()
    {
        List<TbMesas> lista = new ArrayList<>();
        try ( PreparedStatement ps = conexao.getPreparedStatement(
                "SELECT m.pk_mesas, m.designacao FROM tb_mesas m" + ORDEM_NATURAL );
                ResultSet rs = ps.executeQuery() )
        {
            while ( rs.next() )
            {
                TbMesas m = new TbMesas();
                m.setPkMesas( rs.getInt( "pk_mesas" ) );
                m.setDesignacao( rs.getString( "designacao" ) );
                lista.add( m );
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return lista;
    }

    /**
     * Todas as mesas e o pedido em aberto de cada uma, numa só consulta. A
     * regra é a do plano antigo do comercial (último pedido da mesa e itens
     * com status_convertido = 0), que fazia duas consultas por mesa.
     */
    public List<EstadoMesa> estadoMesas() throws SQLException
    {
        String sql = "SELECT m.pk_mesas, m.designacao,"
                + " (SELECT COUNT(*) FROM tb_item_pedidos i WHERE i.fk_pedidos = p.ultimo AND i.status_convertido = 0) AS itens,"
                + " (SELECT COALESCE(SUM(i.total_item), 0) FROM tb_item_pedidos i WHERE i.fk_pedidos = p.ultimo AND i.status_convertido = 0) AS total,"
                + " (SELECT MIN(i.data_entrega) FROM tb_item_pedidos i WHERE i.fk_pedidos = p.ultimo AND i.status_convertido = 0) AS desde"
                + " , COALESCE(vd.vendas, 0) AS vendas, COALESCE(vd.valor, 0) AS valor_vendas"
                + " FROM tb_mesas m"
                + " LEFT JOIN (SELECT fk_mesas, MAX(pk_pedido) AS ultimo FROM tb_pedido GROUP BY fk_mesas) p"
                + " ON p.fk_mesas = m.pk_mesas"
                + " LEFT JOIN (" + SQL_VENDAS_POR_MESA + ") vd ON vd.fk_mesas = m.pk_mesas"
                + ORDEM_NATURAL;
        List<EstadoMesa> lista = new ArrayList<>();
        java.util.Date[] dia = limitesDia( new java.util.Date() );
        try ( PreparedStatement ps = conexao.getPreparedStatement( sql ) )
        {
            ps.setTimestamp( 1, new java.sql.Timestamp( dia[ 0 ].getTime() ) );
            ps.setTimestamp( 2, new java.sql.Timestamp( dia[ 1 ].getTime() ) );
            ps.setInt( 3, util.DVML.MESA_BALCAO );
            try ( ResultSet rs = ps.executeQuery() )
            {
            while ( rs.next() )
            {
                EstadoMesa e = new EstadoMesa();
                e.codigo = rs.getInt( "pk_mesas" );
                e.designacao = rs.getString( "designacao" );
                e.itens = rs.getLong( "itens" );
                // a 2 casas: há itens gravados com resíduos (ex.: 1500.0009)
                e.total = Math.round( rs.getDouble( "total" ) * 100 ) / 100.0;
                java.sql.Timestamp t = null;
                try
                {
                    t = rs.getTimestamp( "desde" );
                }
                catch ( SQLException semData )
                {
                    // data_entrega inválida numa linha: a mesa continua, sem a hora
                }
                e.desde = t == null ? null : new java.util.Date( t.getTime() );
                e.vendasHoje = rs.getInt( "vendas" );
                e.valorVendasHoje = Math.round( rs.getDouble( "valor_vendas" ) * 100 ) / 100.0;
                lista.add( e );
            }
            }
        }
        return lista;
    }

    // =====================================================================
    // VENDAS POR MESA (cor amarela e histórico do dia), como no SysKitandaHotel
    // =====================================================================
    /**
     * Facturas (FR e FT) válidas, por mesa, entre dois instantes (parâmetros
     * 1 e 2). Uma venda conta uma vez por mesa, mesmo com vários itens. Ficam
     * de fora consultas, proformas, notas, recibos, documentos eliminados e
     * as vendas de balcão: o comercial não tem area_venda
     * e grava todas as vendas de balcão na DVML.MESA_BALCAO (parâmetro 3).
     */
    private static final String FILTRO_VENDAS_MESA
            = " v.dataVenda >= ? AND v.dataVenda < ?"
            + " AND iv.fk_mesas <> ?"
            + " AND v.status_eliminado = 'false'"
            + " AND v.fk_documento IN (1, 2)";

    private static final String SQL_VENDAS_POR_MESA
            = "SELECT x.fk_mesas, COUNT(*) AS vendas, SUM(x.total_geral) AS valor FROM ("
            + " SELECT DISTINCT v.codigo, iv.fk_mesas, v.total_geral FROM tb_venda v"
            + " JOIN tb_item_venda iv ON iv.codigo_venda = v.codigo WHERE" + FILTRO_VENDAS_MESA
            + ") x GROUP BY x.fk_mesas";

    /** [00:00 do dia, 00:00 do dia seguinte]. */
    public static java.util.Date[] limitesDia( java.util.Date dia )
    {
        java.util.Calendar c = java.util.Calendar.getInstance();
        c.setTime( dia );
        c.set( java.util.Calendar.HOUR_OF_DAY, 0 );
        c.set( java.util.Calendar.MINUTE, 0 );
        c.set( java.util.Calendar.SECOND, 0 );
        c.set( java.util.Calendar.MILLISECOND, 0 );
        java.util.Date inicio = c.getTime();
        c.add( java.util.Calendar.DAY_OF_MONTH, 1 );
        return new java.util.Date[]
        {
            inicio, c.getTime()
        };
    }

    /** Uma factura emitida para uma mesa (histórico do dia). */
    public static class VendaMesa
    {

        public int codigoMesa;
        public String mesa;
        public int codigoVenda;
        public String documento;
        public java.util.Date dataHora;
        public String cliente;
        public double total;
        public String utilizador;
    }

    /** Facturas do dia, por mesa e hora (a mais antiga primeiro). */
    public List<VendaMesa> historicoVendas( java.util.Date dia ) throws SQLException
    {
        String sql = "SELECT DISTINCT m.pk_mesas, m.designacao, v.codigo, v.cod_fact, v.dataVenda,"
                + " COALESCE(NULLIF(v.nome_consumidor_final, ''), v.nome_cliente) AS cliente, v.total_geral, u.nome AS utilizador"
                + " FROM tb_venda v JOIN tb_item_venda iv ON iv.codigo_venda = v.codigo"
                + " JOIN tb_mesas m ON m.pk_mesas = iv.fk_mesas"
                + " LEFT JOIN tb_usuario u ON u.codigo = v.codigo_usuario"
                + " WHERE" + FILTRO_VENDAS_MESA
                + " ORDER BY v.dataVenda, v.codigo";
        List<VendaMesa> lista = new ArrayList<>();
        java.util.Date[] limites = limitesDia( dia );
        try ( PreparedStatement ps = conexao.getPreparedStatement( sql ) )
        {
            ps.setTimestamp( 1, new java.sql.Timestamp( limites[ 0 ].getTime() ) );
            ps.setTimestamp( 2, new java.sql.Timestamp( limites[ 1 ].getTime() ) );
            ps.setInt( 3, util.DVML.MESA_BALCAO );
            try ( ResultSet rs = ps.executeQuery() )
            {
                while ( rs.next() )
                {
                    VendaMesa v = new VendaMesa();
                    v.codigoMesa = rs.getInt( "pk_mesas" );
                    v.mesa = rs.getString( "designacao" );
                    v.codigoVenda = rs.getInt( "codigo" );
                    v.documento = rs.getString( "cod_fact" );
                    java.sql.Timestamp t = rs.getTimestamp( "dataVenda" );
                    v.dataHora = t == null ? null : new java.util.Date( t.getTime() );
                    v.cliente = rs.getString( "cliente" );
                    v.total = rs.getDouble( "total_geral" );
                    v.utilizador = rs.getString( "utilizador" );
                    lista.add( v );
                }
            }
        }
        return lista;
    }

    // =====================================================================
    // TRANSFERÊNCIAS ENTRE MESAS (e entre lugares da mesma mesa)
    // =====================================================================
    /** Item ainda por facturar de um pedido. */
    public static class ItemAberto
    {

        public int codigo;
        public String produto;
        public int codigoLugar;
        public String lugar;
        public int quantidade;
        public double total;
    }

    /** Pedido aberto da mesa (status_pedido = 0, como o ecrã de pedidos); 0 se não houver. */
    public int pedidoAberto( String designacaoMesa ) throws SQLException
    {
        try ( PreparedStatement ps = conexao.getPreparedStatement(
                "SELECT MAX(p.pk_pedido) FROM tb_pedido p JOIN tb_mesas m ON m.pk_mesas = p.fk_mesas"
                + " WHERE m.designacao = ? AND p.status_pedido = 0" ) )
        {
            ps.setString( 1, designacaoMesa );
            try ( ResultSet rs = ps.executeQuery() )
            {
                return rs.next() ? rs.getInt( 1 ) : 0;
            }
        }
    }

    public List<ItemAberto> itensAbertos( int pedido ) throws SQLException
    {
        List<ItemAberto> lista = new ArrayList<>();
        try ( PreparedStatement ps = conexao.getPreparedStatement(
                "SELECT i.pk_item_pedidos, p.designacao AS produto, i.fk_lugares, l.designacao AS lugar, i.qtd, i.total_item"
                + " FROM tb_item_pedidos i JOIN tb_produto p ON p.codigo = i.fk_produtos"
                + " LEFT JOIN tb_lugares l ON l.pk_lugares = i.fk_lugares"
                + " WHERE i.fk_pedidos = ? AND i.status_convertido = 0 ORDER BY i.pk_item_pedidos" ) )
        {
            ps.setInt( 1, pedido );
            try ( ResultSet rs = ps.executeQuery() )
            {
                while ( rs.next() )
                {
                    ItemAberto it = new ItemAberto();
                    it.codigo = rs.getInt( 1 );
                    it.produto = rs.getString( 2 );
                    it.codigoLugar = rs.getInt( 3 );
                    it.lugar = rs.getString( 4 );
                    it.quantidade = (int) Math.round( rs.getDouble( 5 ) );
                    it.total = rs.getDouble( 6 );
                    lista.add( it );
                }
            }
        }
        return lista;
    }

    /** Lugares (código e designação), por ordem. */
    public List<Object[]> lugares()
    {
        List<Object[]> lista = new ArrayList<>();
        try ( PreparedStatement ps = conexao.getPreparedStatement(
                "SELECT pk_lugares, designacao FROM tb_lugares ORDER BY pk_lugares" );
                ResultSet rs = ps.executeQuery() )
        {
            while ( rs.next() )
            {
                lista.add( new Object[]
                {
                    rs.getInt( 1 ), rs.getString( 2 )
                } );
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return lista;
    }

    /**
     * Passa itens de um pedido para o pedido aberto de outra mesa (criado se
     * não existir) ou, com a mesma mesa, só muda o lugar. Tudo numa
     * transacção: ou passa tudo ou nada.
     *
     * @param quantidades item -> quantidade a passar (igual à do item = item
     * inteiro; menor = o item é dividido em dois)
     * @param novoLugar código do lugar no destino, ou 0 para manter
     * @return o pedido de destino
     */
    public int transferir( int pedidoOrigem, String mesaDestino,
            java.util.Map<Integer, Integer> quantidades, int novoLugar ) throws SQLException
    {
        Connection conn = conexao.getConnectionAtiva();
        boolean autoCommit = conn.getAutoCommit();
        conn.setAutoCommit( false );
        try
        {
            int destino = pedidoAberto( mesaDestino );
            if ( destino == 0 )
            {
                // as mesmas colunas que a função PEDIDO_CRIAR preenche
                try ( PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO tb_pedido (data_pedido, hora_pedido, fk_mesas, status_pedido)"
                        + " VALUES (CURDATE(), CURTIME(), ?, 0)", java.sql.Statement.RETURN_GENERATED_KEYS ) )
                {
                    int pkMesa = getIdByDescricao( mesaDestino );
                    if ( pkMesa == 0 )
                    {
                        throw new SQLException( "A " + mesaDestino + " não existe." );
                    }
                    ps.setInt( 1, pkMesa );
                    ps.executeUpdate();
                    try ( ResultSet k = ps.getGeneratedKeys() )
                    {
                        if ( !k.next() )
                        {
                            throw new SQLException( "Não foi possível criar o pedido da " + mesaDestino + "." );
                        }
                        destino = k.getInt( 1 );
                    }
                }
            }
            for ( java.util.Map.Entry<Integer, Integer> e : quantidades.entrySet() )
            {
                int item = e.getKey(), passar = e.getValue();
                double qtd;
                try ( PreparedStatement ps = conn.prepareStatement(
                        "SELECT qtd FROM tb_item_pedidos WHERE pk_item_pedidos = ? AND fk_pedidos = ? AND status_convertido = 0 FOR UPDATE" ) )
                {
                    ps.setInt( 1, item );
                    ps.setInt( 2, pedidoOrigem );
                    try ( ResultSet rs = ps.executeQuery() )
                    {
                        if ( !rs.next() )
                        {
                            throw new SQLException( "Um dos itens já não está no pedido (foi facturado ou anulado noutro posto)." );
                        }
                        qtd = rs.getDouble( 1 );
                    }
                }
                if ( passar <= 0 )
                {
                    continue;
                }
                if ( passar >= qtd )
                {
                    // item inteiro: muda de pedido (e de lugar, se pedido)
                    try ( PreparedStatement ps = conn.prepareStatement(
                            "UPDATE tb_item_pedidos SET fk_pedidos = ?, fk_lugares = IF(? > 0, ?, fk_lugares) WHERE pk_item_pedidos = ?" ) )
                    {
                        ps.setInt( 1, destino );
                        ps.setInt( 2, novoLugar );
                        ps.setInt( 3, novoLugar );
                        ps.setInt( 4, item );
                        ps.executeUpdate();
                    }
                }
                else
                {
                    // parte do item: cópia com a quantidade passada (valor proporcional) e o original fica com o resto
                    try ( PreparedStatement ps = conn.prepareStatement(
                            "INSERT INTO tb_item_pedidos (fk_lugares, fk_produtos, qtd, fk_pedidos, status_convertido, total_item,"
                            + " status_enviado, status_efectuado, data_entrega, obs, preco)"
                            + " SELECT IF(? > 0, ?, fk_lugares), fk_produtos, ?, ?, status_convertido, total_item * ? / qtd,"
                            + " status_enviado, status_efectuado, data_entrega, obs, preco"
                            + " FROM tb_item_pedidos WHERE pk_item_pedidos = ?" ) )
                    {
                        ps.setInt( 1, novoLugar );
                        ps.setInt( 2, novoLugar );
                        ps.setInt( 3, passar );
                        ps.setInt( 4, destino );
                        ps.setInt( 5, passar );
                        ps.setInt( 6, item );
                        ps.executeUpdate();
                    }
                    // MySQL aplica o SET da esquerda para a direita: o total usa ainda a qtd antiga
                    try ( PreparedStatement ps = conn.prepareStatement(
                            "UPDATE tb_item_pedidos SET total_item = total_item * (qtd - ?) / qtd, qtd = qtd - ? WHERE pk_item_pedidos = ?" ) )
                    {
                        ps.setInt( 1, passar );
                        ps.setInt( 2, passar );
                        ps.setInt( 3, item );
                        ps.executeUpdate();
                    }
                }
            }
            conn.commit();
            return destino;
        }
        catch ( SQLException ex )
        {
            conn.rollback();
            throw ex;
        }
        finally
        {
            conn.setAutoCommit( autoCommit );
        }
    }

    public boolean existeDesignacao( String designacao )
    {
        try ( PreparedStatement ps = conexao.getPreparedStatement(
                "SELECT 1 FROM tb_mesas WHERE UPPER(TRIM(designacao)) = UPPER(TRIM(?)) LIMIT 1" ) )
        {
            ps.setString( 1, designacao );
            try ( ResultSet rs = ps.executeQuery() )
            {
                return rs.next();
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
            return true; // na dúvida não se cria um duplicado
        }
    }

    /** Cria a mesa ("MESA N"); false se já existir ou der erro. */
    public boolean criarMesa( String designacao )
    {
        if ( designacao == null || designacao.trim().isEmpty() || existeDesignacao( designacao ) )
        {
            return false;
        }
        try ( PreparedStatement ps = conexao.getPreparedStatement(
                "INSERT INTO tb_mesas (designacao) VALUES (?)" ) )
        {
            ps.setString( 1, designacao.trim() );
            return ps.executeUpdate() == 1;
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
            return false;
        }
    }

    /** Pedidos e vendas que apontam para a mesa (com algum, não se elimina). */
    public int contarUtilizacoes( int codigo )
    {
        int total = 0;
        for ( String tabela : new String[]
        {
            "tb_pedido", "tb_item_venda"
        } )
        {
            try ( PreparedStatement ps = conexao.getPreparedStatement(
                    "SELECT COUNT(*) FROM " + tabela + " WHERE fk_mesas = ?" ) )
            {
                ps.setInt( 1, codigo );
                try ( ResultSet rs = ps.executeQuery() )
                {
                    if ( rs.next() )
                    {
                        total += rs.getInt( 1 );
                    }
                }
            }
            catch ( SQLException e )
            {
                e.printStackTrace();
                return Integer.MAX_VALUE;
            }
        }
        return total;
    }

    /** Elimina só se a mesa nunca foi usada: o histórico de pedidos e vendas fica intacto. */
    public boolean eliminarSeLivre( int codigo )
    {
        if ( contarUtilizacoes( codigo ) > 0 )
        {
            return false;
        }
        try ( PreparedStatement ps = conexao.getPreparedStatement(
                "DELETE FROM tb_mesas WHERE pk_mesas = ?" ) )
        {
            ps.setInt( 1, codigo );
            return ps.executeUpdate() == 1;
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
            return false;
        }
    }

}
