package comercial.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeParseException;
import java.util.Date;

/**
 * Entrada de lotes na RECEPCAO DE COMPRAS.
 *
 * Classe independente do LoteController (nao precisa de o alterar). Escreve em
 * lote e lote_movimento (tipo COMPRA, fk_operacao = pk_compra). O tb_stock
 * continua a ser actualizado pelo codigo de compras que ja existe.
 *
 * NAO faz commit nem rollback: usa a Connection (e a transaccao) de quem
 * chama, a mesma que grava a compra e actualiza o tb_stock.
 */
public final class EntradaLoteCompra
{

    private EntradaLoteCompra()
    {
    }

    // ------------------------------------------------------------------
    // Validade digitada no formulario
    // ------------------------------------------------------------------
    /**
     * Converte o texto da validade numa data.
     *
     * Aceita "MM/AAAA" (como vem impresso na caixa; assume o ULTIMO dia do
     * mes) e "DD/MM/AAAA".
     *
     * @throws IllegalArgumentException se vazio, invalido ou ja caducado
     */
    public static java.sql.Date parseValidade( String texto )
    {
        if ( texto == null || texto.trim().isEmpty() )
        {
            throw new IllegalArgumentException( "Indique a validade do lote (MM/AAAA)." );
        }
        String t = texto.trim().replace( '-', '/' ).replace( '.', '/' );

        LocalDate data;
        try
        {
            if ( t.matches( "\\d{1,2}/\\d{4}" ) )
            {
                String[] p = t.split( "/" );
                data = YearMonth.of( Integer.parseInt( p[1] ), Integer.parseInt( p[0] ) ).atEndOfMonth();
            }
            else if ( t.matches( "\\d{1,2}/\\d{1,2}/\\d{4}" ) )
            {
                String[] p = t.split( "/" );
                data = LocalDate.of( Integer.parseInt( p[2] ), Integer.parseInt( p[1] ), Integer.parseInt( p[0] ) );
            }
            else
            {
                throw new IllegalArgumentException( "Validade invalida. Use MM/AAAA, por exemplo 08/2028." );
            }
        }
        catch ( java.time.DateTimeException | NumberFormatException e )
        {
            throw new IllegalArgumentException( "Validade invalida. Use MM/AAAA, por exemplo 08/2028." );
        }

        if ( data.isBefore( LocalDate.now() ) )
        {
            throw new IllegalArgumentException( "Produto caducado: validade " + data + " ja passou." );
        }
        return java.sql.Date.valueOf( data );
    }

    // ------------------------------------------------------------------
    // Datas do lote (calendarios do formulario)
    // ------------------------------------------------------------------
    // STRICT: recusa datas impossiveis (31/02) em vez de as "corrigir"
    private static final java.time.format.DateTimeFormatter FORMATO
            = java.time.format.DateTimeFormatter.ofPattern("dd/MM/uuuu")
                    .withResolverStyle(java.time.format.ResolverStyle.STRICT);

    private static LocalDate paraLocalDate(Date d)
    {
        return new java.sql.Date( d.getTime() ).toLocalDate();
    }

    /**
     * Formata uma data como dd/MM/yyyy ("" se null). E o formato guardado na
     * tabela do formulario.
     */
    public static String formatar( Date data )
    {
        return data == null ? "" : paraLocalDate( data ).format( FORMATO );
    }

    /**
     * Le uma data dd/MM/yyyy (formato da tabela). Devolve null se vazio. NAO
     * valida se e passada (serve tambem para a data de fabrico).
     *
     * @throws IllegalArgumentException se o texto nao for uma data valida
     */
    public static java.sql.Date parseData( String texto )
    {
        if ( texto == null || texto.trim().isEmpty() )
        {
            return null;
        }
        try
        {
            return java.sql.Date.valueOf( LocalDate.parse( texto.trim(), FORMATO ) );
        }
        catch ( DateTimeParseException e )
        {
            throw new IllegalArgumentException( "Data invalida: " + texto + " (use dd/MM/aaaa)." );
        }
    }

    /**
     * Dias que faltam para a validade (negativo se ja caducou).
     */
    public static long diasAteValidade( Date dataValidade )
    {
        return java.time.temporal.ChronoUnit.DAYS.between( LocalDate.now(), paraLocalDate( dataValidade ) );
    }

    /**
     * Regras das datas de um lote: validade obrigatoria e nao caducada;
     * fabrico (opcional) nao pode ser futuro nem posterior a validade.
     *
     * @throws IllegalArgumentException com a mensagem para mostrar ao utilizador
     */
    public static void validarDatas( Date dataFabrico, Date dataValidade )
    {
        if ( dataValidade == null )
        {
            throw new IllegalArgumentException( "Indique a data de validade do lote." );
        }
        LocalDate validade = paraLocalDate( dataValidade );
        if ( validade.isBefore( LocalDate.now() ) )
        {
            throw new IllegalArgumentException( "Produto caducado: a validade " + validade.format( FORMATO ) + " ja passou." );
        }
        if ( dataFabrico != null )
        {
            LocalDate fabrico = paraLocalDate( dataFabrico );
            if ( fabrico.isAfter( LocalDate.now() ) )
            {
                throw new IllegalArgumentException( "A data de fabrico nao pode ser futura." );
            }
            if ( !fabrico.isBefore( validade ) )
            {
                throw new IllegalArgumentException( "A data de fabrico deve ser anterior a validade." );
            }
        }
    }

    // ------------------------------------------------------------------
    // Gravacao
    // ------------------------------------------------------------------
    /**
     * Regista a entrada de um lote no armazem de descarga da compra. Se o lote
     * (produto + numero + armazem) ja existe, soma a quantidade; senao cria-o.
     *
     * @param qtd quantidade em UNIDADES BASE (qtd a entrar x factor de
     * conversao), igual a que vai para o tb_stock
     * @param fkCompra compras.pk_compra
     * @return id do lote que recebeu a quantidade
     * @throws IllegalArgumentException dados invalidos
     * @throws IllegalStateException o numero de lote ja existe com outra
     * validade, ou o lote esta desactivado
     */
    public static int registar( Connection conn, int fkProduto, int fkArmazem, String numeroLote,
            Date dataFabrica, Date dataValidade, double qtd, int fkCompra ) throws SQLException
    {
        if ( numeroLote == null || numeroLote.trim().isEmpty() )
        {
            throw new IllegalArgumentException( "Indique o numero do lote." );
        }
        if ( qtd <= 0 )
        {
            throw new IllegalArgumentException( "A quantidade a entrar deve ser maior que zero." );
        }
        validarDatas( dataFabrica, dataValidade );
        String lote = numeroLote.trim();
        java.sql.Date validade = new java.sql.Date( dataValidade.getTime() );

        int idLote = creditarLote( conn, fkProduto, fkArmazem, lote, dataFabrica, validade, qtd );
        inserirMovimento( conn, idLote, fkCompra, qtd );
        return idLote;
    }

    private static int creditarLote( Connection conn, int fkProduto, int fkArmazem, String numeroLote,
            Date dataFabrica, java.sql.Date validade, double qtd ) throws SQLException
    {
        int idNoArmazem = 0;

        // Bloqueia as linhas deste lote (em todos os armazens) e valida a validade
        String sqlBusca = "SELECT id, fk_armazem, data_validade, estado FROM lote "
                + "WHERE fk_produto = ? AND numero_lote = ? FOR UPDATE";
        try ( PreparedStatement ps = conn.prepareStatement( sqlBusca ) )
        {
            ps.setInt( 1, fkProduto );
            ps.setString( 2, numeroLote );
            try ( ResultSet rs = ps.executeQuery() )
            {
                while ( rs.next() )
                {
                    if ( !rs.getDate( "data_validade" ).toString().equals( validade.toString() ) )
                    {
                        throw new IllegalStateException( "O lote " + numeroLote + " ja existe com a validade "
                                + rs.getDate( "data_validade" ) + ". Confirme o numero do lote e a validade." );
                    }
                    if ( rs.getInt( "fk_armazem" ) == fkArmazem )
                    {
                        if ( "DESACTIVO".equals( rs.getString( "estado" ) ) )
                        {
                            throw new IllegalStateException( "O lote " + numeroLote + " esta desactivado." );
                        }
                        idNoArmazem = rs.getInt( "id" );
                    }
                }
            }
        }

        if ( idNoArmazem > 0 )
        {
            String sqlSoma = "UPDATE lote SET qtd = qtd + ?, estado = 'ACTIVO' WHERE id = ?";
            try ( PreparedStatement ps = conn.prepareStatement( sqlSoma ) )
            {
                ps.setDouble( 1, qtd );
                ps.setInt( 2, idNoArmazem );
                ps.executeUpdate();
            }
            return idNoArmazem;
        }

        String sqlNovo = "INSERT INTO lote( fk_produto, fk_armazem, numero_lote, data_entrada, data_fabrica, data_validade, qtd, estado ) "
                + "VALUES( ?, ?, ?, CURDATE(), ?, ?, ?, 'ACTIVO' )";
        try ( PreparedStatement ps = conn.prepareStatement( sqlNovo, Statement.RETURN_GENERATED_KEYS ) )
        {
            ps.setInt( 1, fkProduto );
            ps.setInt( 2, fkArmazem );
            ps.setString( 3, numeroLote );
            if ( dataFabrica != null )
            {
                ps.setDate( 4, new java.sql.Date( dataFabrica.getTime() ) );
            }
            else
            {
                ps.setNull( 4, Types.DATE );
            }
            ps.setDate( 5, validade );
            ps.setDouble( 6, qtd );
            ps.executeUpdate();

            try ( ResultSet keys = ps.getGeneratedKeys() )
            {
                if ( keys.next() )
                {
                    return keys.getInt( 1 );
                }
            }
        }
        throw new SQLException( "Nao foi possivel criar o lote " + numeroLote );
    }

    private static void inserirMovimento( Connection conn, int fkLote, int fkCompra, double qtd ) throws SQLException
    {
        String sql = "INSERT INTO lote_movimento( fk_lote, fk_operacao, tipo, qtd, data_mov ) "
                + "VALUES( ?, ?, 'COMPRA', ?, NOW() )";
        try ( PreparedStatement ps = conn.prepareStatement( sql ) )
        {
            ps.setInt( 1, fkLote );
            ps.setInt( 2, fkCompra );
            ps.setDouble( 3, qtd );
            ps.executeUpdate();
        }
    }
}
