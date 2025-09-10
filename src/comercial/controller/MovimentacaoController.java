package comercial.controller;

import controller.StockController;
import entity.Movimentacao;
import entity.TbPreco;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import util.BDConexao;

/**
 * Controller Movimentacao
 *
 * @author Eng. Domingos Dala Vunge
 * @created 10/set/2025
 */
public class MovimentacaoController
{

    private final Connection conn;

    public MovimentacaoController( Connection conn )
    {
        this.conn = conn;
    }

    /**
     * Inserir nova movimentação e retornar o ID gerado
     */
    public int salvar( Movimentacao mov ) throws SQLException
    {
        String sql = "INSERT INTO movimentacao ("
                + "produto_id, data_mov, tipo, documento, "
                + "quantidade_anterior, quantidade, quantidade_actual, "
                + "valor_unitario, valor_total, custo_medio, usuario_id, armazem_id"
                + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try ( PreparedStatement ps = conn.prepareStatement( sql, PreparedStatement.RETURN_GENERATED_KEYS ) )
        {
            ps.setInt( 1, mov.getProdutoId() );
            ps.setTimestamp( 2, new java.sql.Timestamp( mov.getDataMov().getTime() ) );
            ps.setString( 3, mov.getTipo() );
            ps.setString( 4, mov.getDocumento() );
            ps.setBigDecimal( 5, mov.getQuantidadeAnterior() );
            ps.setBigDecimal( 6, mov.getQuantidade() );
            ps.setBigDecimal( 7, mov.getQuantidadeActual() );
            ps.setBigDecimal( 8, mov.getValorUnitario() );
            ps.setBigDecimal( 9, mov.getValorTotal() );
            ps.setBigDecimal( 10, mov.getCustoMedio() );
            ps.setInt( 11, mov.getUsuarioId() );
            ps.setInt( 12, mov.getArmazemId() );
            ps.executeUpdate();

            try ( ResultSet rs = ps.getGeneratedKeys() )
            {
                if ( rs.next() )
                {
                    int idGerado = rs.getInt( 1 );
                    mov.setId( idGerado );
                    return idGerado;
                }
            }
        }
        return -1;
    }

    /**
     * Registrar entrada no estoque
     */
    public int registrarEntrada( Movimentacao mov ) throws SQLException
    {
        // quantidade actual = anterior + entrada
        mov.setQuantidadeActual(
                mov.getQuantidadeAnterior().add( mov.getQuantidade() )
        );

        // valor total da movimentação
        mov.setValorTotal(
                mov.getQuantidade().multiply( mov.getValorUnitario() )
        );

        // cálculo do custo médio (precisa da última movimentação para ter histórico)
        Movimentacao ultima = buscarUltimaMovimentacao( mov.getProdutoId() );
        if ( ultima != null )
        {
            BigDecimal estoqueAnterior = ultima.getQuantidadeActual();
            BigDecimal valorAnterior = ultima.getCustoMedio().multiply( estoqueAnterior );

            BigDecimal novoValorTotal = valorAnterior.add( mov.getValorTotal() );
            BigDecimal novaQuantidade = estoqueAnterior.add( mov.getQuantidade() );

            if ( novaQuantidade.compareTo( BigDecimal.ZERO ) > 0 )
            {
                mov.setCustoMedio(
                        novoValorTotal.divide( novaQuantidade, 2, RoundingMode.HALF_UP )
                );
            }
            else
            {
                mov.setCustoMedio( mov.getValorUnitario() );
            }
        }
        else
        {
            mov.setCustoMedio( mov.getValorUnitario() );
        }

        mov.setTipo( "ENTRADA" );
        return salvar( mov );
    }

    /**
     * Registrar saída do estoque
     */
    /**
     * Registrar saída do estoque — mesma lógica da entrada, mas subtrai valores
     */
    public int registrarSaida( Movimentacao mov ) throws SQLException
    {
        // validações básicas
        if ( mov.getQuantidade() == null || mov.getValorUnitario() == null )
        {
            throw new SQLException( "Quantidade e valor_unitario devem ser informados." );
        }

        Movimentacao ultima = buscarUltimaMovimentacao( mov.getProdutoId() );

        // quantidade_anterior vem do parâmetro; se não informado, tentar usar ultima
        java.math.BigDecimal qtdAnt = mov.getQuantidadeAnterior();
        if ( qtdAnt == null )
        {
            if ( ultima != null && ultima.getQuantidadeActual() != null )
            {
                qtdAnt = ultima.getQuantidadeActual();
                mov.setQuantidadeAnterior( qtdAnt );
            }
            else
            {
                throw new SQLException( "Quantidade anterior não informada e não existe saldo anterior." );
            }
        }

        // verificar saldo suficiente
        if ( qtdAnt.subtract( mov.getQuantidade() ).compareTo( java.math.BigDecimal.ZERO ) < 0 )
        {
            throw new SQLException( "Saldo insuficiente para efetuar a saída." );
        }

        // quantidade actual = anterior - saída
        java.math.BigDecimal novaQtd = qtdAnt.subtract( mov.getQuantidade() );
        mov.setQuantidadeActual( novaQtd );

        // valor total da movimentação (usa o valor_unitario informado)
        mov.setValorTotal( mov.getQuantidade().multiply( mov.getValorUnitario() ) );

        // recalcular custo médio: (valorAnterior - valorDaSaida) / novaQuantidade
        java.math.BigDecimal custoMedioAnterior = ( ultima != null && ultima.getCustoMedio() != null )
                ? ultima.getCustoMedio()
                : java.math.BigDecimal.ZERO;
        java.math.BigDecimal valorAnterior = custoMedioAnterior.multiply( qtdAnt );
        java.math.BigDecimal valorSaida = mov.getValorTotal();
        java.math.BigDecimal valorRestante = valorAnterior.subtract( valorSaida );

        if ( novaQtd.compareTo( java.math.BigDecimal.ZERO ) > 0 )
        {
            mov.setCustoMedio( valorRestante.divide( novaQtd, 2, java.math.RoundingMode.HALF_UP ) );
        }
        else
        {
            // estoque zerado — custo médio fica zerado (ou ajuste conforme tua regra)
            mov.setCustoMedio( java.math.BigDecimal.ZERO );
        }

        mov.setTipo( "SAIDA" );
        return salvar( mov );
    }

    /**
     * Buscar última movimentação de um produto
     */
    public Movimentacao buscarUltimaMovimentacao( int produtoId ) throws SQLException
    {
        String sql = "SELECT * FROM movimentacao WHERE produto_id=? ORDER BY data_mov DESC, id DESC LIMIT 1";
        try ( PreparedStatement ps = conn.prepareStatement( sql ) )
        {
            ps.setInt( 1, produtoId );
            try ( ResultSet rs = ps.executeQuery() )
            {
                if ( rs.next() )
                {
                    return mapear( rs );
                }
            }
        }
        return null;
    }

    /**
     * Buscar movimentações por produto em um intervalo de datas
     */
    public List<Object[]> buscarPorIntervaloDatas(
            int produtoId,
            int armazemId,
            Date dataInicio,
            Date dataFim
    ) throws SQLException
    {
        List<Object[]> lista = new ArrayList<>();

        String sql = "SELECT m.id, "
                + "p.designacao, "
                + "m.documento, "
                + "m.data_mov, "
                + "m.quantidade_anterior, "
                + "m.quantidade, "
                + "m.quantidade_actual, "
                + "m.tipo "
                + "FROM movimentacao m "
                + "INNER JOIN tb_produto p ON p.codigo = m.produto_id "
                + "WHERE m.produto_id=? "
                + "AND m.armazem_id=? "
                + "AND DATE(m.data_mov) BETWEEN ? AND ? "
                + "ORDER BY m.data_mov ASC";

        System.out.println( "SQL => " + sql );
        System.out.println( "Parametros => produtoId=" + produtoId
                + ", armazemId=" + armazemId
                + ", dataInicio=" + new java.sql.Date( dataInicio.getTime() )
                + ", dataFim=" + new java.sql.Date( dataFim.getTime() ) );

        try ( PreparedStatement ps = conn.prepareStatement( sql ) )
        {
            ps.setInt( 1, produtoId );
            ps.setInt( 2, armazemId );
            ps.setDate( 3, new java.sql.Date( dataInicio.getTime() ) );
            ps.setDate( 4, new java.sql.Date( dataFim.getTime() ) );

            try ( ResultSet rs = ps.executeQuery() )
            {
                while ( rs.next() )
                {
                    Object[] linha = new Object[]
                    {
                        rs.getInt( "id" ),
                        rs.getString( "designacao" ),
                        rs.getString( "documento" ),
                        rs.getTimestamp( "data_mov" ),
                        rs.getBigDecimal( "quantidade_anterior" ),
                        rs.getBigDecimal( "quantidade" ),
                        rs.getBigDecimal( "quantidade_actual" ),
                        rs.getString( "tipo" ),
                    };
                    lista.add( linha );
                }
            }
        }
        return lista;
    }

    /**
     * Atualizar movimentação existente
     */
    public void atualizar( Movimentacao mov ) throws SQLException
    {
        String sql = "UPDATE movimentacao SET "
                + "produto_id=?, data_mov=?, tipo=?, documento=?, "
                + "quantidade_anterior=?, quantidade=?, quantidade_actual=?, "
                + "valor_unitario=?, valor_total=?, custo_medio=?, usuario_id=? "
                + "WHERE id=?";

        try ( PreparedStatement ps = conn.prepareStatement( sql ) )
        {
            ps.setInt( 1, mov.getProdutoId() );
            ps.setTimestamp( 2, new java.sql.Timestamp( mov.getDataMov().getTime() ) );
            ps.setString( 3, mov.getTipo() );
            ps.setString( 4, mov.getDocumento() );
            ps.setBigDecimal( 5, mov.getQuantidadeAnterior() );
            ps.setBigDecimal( 6, mov.getQuantidade() );
            ps.setBigDecimal( 7, mov.getQuantidadeActual() );
            ps.setBigDecimal( 8, mov.getValorUnitario() );
            ps.setBigDecimal( 9, mov.getValorTotal() );
            ps.setBigDecimal( 10, mov.getCustoMedio() );
            ps.setInt( 11, mov.getUsuarioId() );
            ps.setInt( 12, mov.getId() );
            ps.executeUpdate();
        }
    }

    /**
     * Remover movimentação por ID
     */
    public void remover( int id ) throws SQLException
    {
        String sql = "DELETE FROM movimentacao WHERE id=?";
        try ( PreparedStatement ps = conn.prepareStatement( sql ) )
        {
            ps.setInt( 1, id );
            ps.executeUpdate();
        }
    }

    /**
     * Buscar movimentação por ID
     */
    public Movimentacao buscarPorId( int id ) throws SQLException
    {
        String sql = "SELECT * FROM movimentacao WHERE id=?";
        try ( PreparedStatement ps = conn.prepareStatement( sql ) )
        {
            ps.setInt( 1, id );
            try ( ResultSet rs = ps.executeQuery() )
            {
                if ( rs.next() )
                {
                    return mapear( rs );
                }
            }
        }
        return null;
    }

    /**
     * Listar todas as movimentações
     */
    public List<Movimentacao> listarTodos() throws SQLException
    {
        List<Movimentacao> lista = new ArrayList<>();
        String sql = "SELECT * FROM movimentacao ORDER BY data_mov DESC";
        try ( PreparedStatement ps = conn.prepareStatement( sql ); ResultSet rs = ps.executeQuery() )
        {
            while ( rs.next() )
            {
                lista.add( mapear( rs ) );
            }
        }
        return lista;
    }

    /**
     * Buscar movimentações por produto
     */
    public List<Movimentacao> buscarPorProduto( int produtoId ) throws SQLException
    {
        List<Movimentacao> lista = new ArrayList<>();
        String sql = "SELECT * FROM movimentacao WHERE produto_id=? ORDER BY data_mov DESC";
        try ( PreparedStatement ps = conn.prepareStatement( sql ) )
        {
            ps.setInt( 1, produtoId );
            try ( ResultSet rs = ps.executeQuery() )
            {
                while ( rs.next() )
                {
                    lista.add( mapear( rs ) );
                }
            }
        }
        return lista;
    }

    /**
     * Mapear ResultSet -> Movimentacao
     */
    private Movimentacao mapear( ResultSet rs ) throws SQLException
    {
        Movimentacao m = new Movimentacao();
        m.setId( rs.getInt( "id" ) );
        m.setProdutoId( rs.getInt( "produto_id" ) );
        m.setDataMov( new Date( rs.getTimestamp( "data_mov" ).getTime() ) );
        m.setTipo( rs.getString( "tipo" ) );
        m.setDocumento( rs.getString( "documento" ) );
        m.setQuantidadeAnterior( rs.getBigDecimal( "quantidade_anterior" ) );
        m.setQuantidade( rs.getBigDecimal( "quantidade" ) );
        m.setQuantidadeActual( rs.getBigDecimal( "quantidade_actual" ) );
        m.setValorUnitario( rs.getBigDecimal( "valor_unitario" ) );
        m.setValorTotal( rs.getBigDecimal( "valor_total" ) );
        m.setCustoMedio( rs.getBigDecimal( "custo_medio" ) );
        m.setUsuarioId( rs.getInt( "usuario_id" ) );
        m.setArmazemId( rs.getInt( "armazem_id" ) );
        return m;
    }

    public static boolean registrarMovimento(
            int idProduto,
            int idArmazem,
            int idUsuario,
            BigDecimal qtd,
            String doc,
            String tipo,
            BDConexao conexao
    )
    {
        MovimentacaoController mv = new MovimentacaoController( conexao.getConnection1() );
        PrecosController pc = new PrecosController( conexao );
        StoksController sc = new StoksController( conexao );

        double qtdAnterior = sc.getQuantidadeProduto( idProduto, idArmazem );

        TbPreco preco = (TbPreco) pc.getLastIdPrecoByIdProdutos( idProduto, qtd.doubleValue() );
        Movimentacao mov = new Movimentacao();
        mov.setProdutoId( idProduto );
        mov.setDataMov( new java.util.Date() );
        mov.setQuantidadeAnterior( new BigDecimal( qtdAnterior ) );
        mov.setDocumento( doc );
        mov.setQuantidade( qtd );
        mov.setValorUnitario( Objects.nonNull( preco )
                ? preco.getPrecoVenda()
                : new BigDecimal( 0 )
        );
        mov.setUsuarioId( idUsuario );
        mov.setArmazemId( idArmazem );

        try
        {
            if ( tipo.equals( "ENTRADA" ) )
            {
                mv.registrarEntrada( mov );
                return true;
            }
            else
            {
                mv.registrarSaida( mov );
                return true;
            }
        }
        catch ( SQLException e )
        {
        }

        return false;
    }

}
