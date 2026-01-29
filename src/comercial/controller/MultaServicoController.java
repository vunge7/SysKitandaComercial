package comercial.controller;

import entity.MultaServico;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.List;
import util.BDConexao;

public class MultaServicoController
{

    private BDConexao conexao;

    public MultaServicoController( BDConexao conexao )
    {
        this.conexao = conexao;
    }

    public boolean create( MultaServico multa )
    {
        String sql = "INSERT INTO multa_servico (day_start, day_end, valor, data_registro, produto_id, usuario_id) "
                + "VALUES (?, ?, ?, ?, ?)";

        try ( PreparedStatement ps
                = conexao.getConnectionAtiva().prepareStatement( sql, Statement.RETURN_GENERATED_KEYS ) )
        {
            ps.setInt( 1, multa.getDayStart() );
            ps.setInt( 2, multa.getDayEnd() );
            ps.setBigDecimal( 3, multa.getValor().setScale( 2, RoundingMode.HALF_UP ) );
            ps.setTimestamp( 4, new Timestamp( multa.getDataRegistro().getTime() ) );
            ps.setInt( 5, multa.getProdutoId() );
            ps.setInt( 5, multa.getUsuarioId() );

            ps.executeUpdate();

            try ( ResultSet rs = ps.getGeneratedKeys() )
            {
                if ( rs.next() )
                {
                    multa.setId( rs.getInt( 1 ) );
                }
            }

            return true;
        }
        catch ( SQLException e )
        {
            System.err.println( "[ERRO] Falha ao salvar multa: " + e.getMessage() );
            e.printStackTrace();
            return false;
        }
    }

//    public boolean create( MultaServico multa )
//    {
//        String sql = "INSERT INTO multa_servico (day_start, day_end, valor, data_registro, usuario_id) "
//                + "VALUES (?, ?, ?, ?, ?)";
//
//        try ( PreparedStatement ps
//                = conexao.getConnectionAtiva().prepareStatement( sql, Statement.RETURN_GENERATED_KEYS ) )
//        {
//            ps.setInt( 1, multa.getDayStart() );
//            ps.setInt( 2, multa.getDayEnd() );
//            ps.setBigDecimal( 3, multa.getValor() );
//            ps.setTimestamp( 4, new Timestamp( multa.getDataRegistro().getTime() ) );
//            ps.setInt( 5, multa.getUsuario().getCodigo() );
//
//            ps.executeUpdate();
//
//            try ( ResultSet rs = ps.getGeneratedKeys() )
//            {
//                if ( rs.next() )
//                {
//                    multa.setId( rs.getInt( 1 ) );
//                }
//            }
//
//            return true;
//
//        }
//        catch ( SQLException e )
//        {
//            System.err.println( "[ERRO] Falha ao salvar multa: " + e.getMessage() );
//            e.printStackTrace();
//            return false;
//        }
//    }
    public boolean alterar( MultaServico multa )
    {
        String sql = "UPDATE multa_servico "
                + "SET day_start = ?, day_end = ?, valor = ? "
                + "WHERE id = ?";

        try ( PreparedStatement ps
                = conexao.getConnectionAtiva().prepareStatement( sql ) )
        {
            ps.setInt( 1, multa.getDayStart() );
            ps.setInt( 2, multa.getDayEnd() );
            ps.setBigDecimal( 3, multa.getValor() );
            ps.setInt( 4, multa.getId() );

            return ps.executeUpdate() > 0;

        }
        catch ( SQLException e )
        {
            System.err.println( "[ERRO] Falha ao alterar multa: " + e.getMessage() );
            e.printStackTrace();
            return false;
        }
    }

    public boolean existMulta() throws SQLException
    {
        String sql = "SELECT 1 FROM multa_servico LIMIT 1";

        try ( PreparedStatement ps = conexao.prepareStatement( sql ); ResultSet rs = ps.executeQuery() )
        {
            return rs.next(); // se existir pelo menos 1 registo
        }
    }

    public List<MultaServico> buscaTodos() throws SQLException
    {
        List<MultaServico> lista = new ArrayList<>();

        String sql = "SELECT id, day_start, day_end, valor, data_registro, usuario_id "
                + "FROM multa_servico "
                + "ORDER BY day_start";

        try ( PreparedStatement ps = conexao.prepareStatement( sql ); ResultSet rs = ps.executeQuery() )
        {
            while ( rs.next() )
            {
                MultaServico multa = new MultaServico();

                multa.setId( rs.getInt( "id" ) );
                multa.setDayStart( rs.getInt( "day_start" ) );
                multa.setDayEnd( rs.getInt( "day_end" ) );
                multa.setValor( rs.getBigDecimal( "valor" ) );
                multa.setDataRegistro( rs.getTimestamp( "data_registro" ) );
                multa.setProdutoId( rs.getInt( "produto_id" ) );
                multa.setUsuarioId( rs.getInt( "usuario_id" ) );
                lista.add( multa );
            }
        }

        return lista;
    }

    // 🔹 Atualizar multa
    public void update( MultaServico multa ) throws SQLException
    {
        String sql = "UPDATE multa_servico SET day_start=?, day_end=?, valor=?, data_registro=?, usuario_id=? "
                + "WHERE id=?";

        try ( PreparedStatement ps = conexao.prepareStatement( sql ) )
        {

            ps.setInt( 1, multa.getDayStart() );
            ps.setInt( 2, multa.getDayEnd() );
            ps.setBigDecimal( 3, multa.getValor() );
            ps.setTimestamp( 4, new Timestamp( multa.getDataRegistro().getTime() ) );
            ps.setInt( 5, multa.getProdutoId() );
            ps.setInt( 6, multa.getUsuarioId() );
            ps.setInt( 7, multa.getId() );

            ps.executeUpdate();
        }
    }

    // 🔹 Deletar multa
    public void delete( int id ) throws SQLException
    {
        String sql = "DELETE FROM multa_servico WHERE id=?";
        try ( PreparedStatement ps = conexao.prepareStatement( sql ) )
        {
            ps.setInt( 1, id );
            ps.executeUpdate();
        }
    }

    // 🔹 Buscar por ID
    public MultaServico findById( int id ) throws SQLException
    {
        String sql = "SELECT * FROM multa_servico WHERE id=?";
        try ( PreparedStatement ps = conexao.prepareStatement( sql ) )
        {
            ps.setInt( 1, id );
            try ( ResultSet rs = ps.executeQuery() )
            {
                if ( rs.next() )
                {
                    return mapResultSetToMulta( rs );
                }
            }
        }
        return null;
    }

    public MultaServico findMulta( int id )
    {
        String sql = "SELECT id, day_start, day_end, valor, data_registro, usuario_id "
                + "FROM multa_servico "
                + "WHERE id = ?";

        try ( PreparedStatement ps
                = conexao.getConnectionAtiva().prepareStatement( sql ) )
        {
            ps.setInt( 1, id );

            try ( ResultSet rs = ps.executeQuery() )
            {
                if ( rs.next() )
                {
                    MultaServico multa = new MultaServico();
                    multa.setId( rs.getInt( "id" ) );
                    multa.setDayStart( rs.getInt( "day_start" ) );
                    multa.setDayEnd( rs.getInt( "day_end" ) );
                    multa.setValor( rs.getBigDecimal( "valor" ) );
                    multa.setDataRegistro( rs.getTimestamp( "data_registro" ) );
                    multa.setProdutoId( rs.getInt( "produto_id" ) );
                    multa.setUsuarioId( rs.getInt( "usuario_id" ) );

                    return multa;
                }
            }
        }
        catch ( SQLException e )
        {
            System.err.println( "[ERRO] Falha ao buscar multa: " + e.getMessage() );
            e.printStackTrace();
        }

        return null;
    }

    // 🔹 Buscar todas
//    public List<MultaServico> findAll() throws SQLException
//    {
//        List<MultaServico> lista = new ArrayList<>();
//        String sql = "SELECT * FROM multa_servico";
//
//        try ( Statement st = conexao.createStatement(); ResultSet rs = st.executeQuery( sql ) )
//        {
//
//            while ( rs.next() )
//            {
//                lista.add( mapResultSetToMulta( rs ) );
//            }
//        }
//        return lista;
//    }
    // 🔹 Buscar multa pelo intervalo de dias (REGRA DE NEGÓCIO 🔥)
    public MultaServico findByIntervaloDias( int diasAtraso ) throws SQLException
    {
        String sql = "SELECT * FROM multa_servico WHERE ? BETWEEN day_start AND day_end LIMIT 1";

        try ( PreparedStatement ps = conexao.prepareStatement( sql ) )
        {
            ps.setInt( 1, diasAtraso );

            try ( ResultSet rs = ps.executeQuery() )
            {
                if ( rs.next() )
                {
                    return mapResultSetToMulta( rs );
                }
            }
        }
        return null;
    }

    // 🔹 Mapper
    private MultaServico mapResultSetToMulta( ResultSet rs ) throws SQLException
    {
        MultaServico multa = new MultaServico();
        multa.setId( rs.getInt( "id" ) );
        multa.setDayStart( rs.getInt( "day_start" ) );
        multa.setDayEnd( rs.getInt( "day_end" ) );
        multa.setValor( rs.getBigDecimal( "valor" ) );
        multa.setDataRegistro( rs.getTimestamp( "data_registro" ) );
        multa.setProdutoId( rs.getInt( "produto_id" ) );
        multa.setUsuarioId( rs.getInt( "usuario_id" ) );

        return multa;
    }

    public BigDecimal getValorMultaByDay( int day ) throws SQLException
    {
        BigDecimal decimal = new BigDecimal( 0 );
        String sql = "SELECT valor FROM multa_servico WHERE ? BETWEEN  day_start and day_end LIMIT 1";

        try ( PreparedStatement ps = conexao.prepareStatement( sql ) )
        {
            ps.setInt( 1, day );
            try ( ResultSet rs = ps.executeQuery() )
            {
                if ( rs.next() )
                {
                    decimal = rs.getBigDecimal( "valor" );
                }
            }
        }
        return decimal;
    }

}
