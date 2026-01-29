package comercial.controller;

import entity.MultaServico;
import entity.TbUsuario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MultaServicoController
{

    private Connection conexao;

    public MultaServicoController( Connection conexao )
    {
        this.conexao = conexao;
    }

    // 🔹 Criar multa
    public void create( MultaServico multa ) throws SQLException
    {
        String sql = "INSERT INTO multa_servico (day_start, day_end, valor, data_registro, usuario_id) "
                + "VALUES (?, ?, ?, ?, ?)";

        try ( PreparedStatement ps = conexao.prepareStatement( sql, Statement.RETURN_GENERATED_KEYS ) )
        {

            ps.setInt( 1, multa.getDayStart() );
            ps.setInt( 2, multa.getDayEnd() );
            ps.setBigDecimal( 3, multa.getValor() );
            ps.setTimestamp( 4, new Timestamp( multa.getDataRegistro().getTime() ) );
            ps.setInt( 5, multa.getUsuario().getCodigo() );

            ps.executeUpdate();

            try ( ResultSet rs = ps.getGeneratedKeys() )
            {
                if ( rs.next() )
                {
                    multa.setId( rs.getInt( 1 ) );
                }
            }
        }
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
            ps.setInt( 5, multa.getUsuario().getCodigo() );
            ps.setInt( 6, multa.getId() );

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

    // 🔹 Buscar todas
    public List<MultaServico> findAll() throws SQLException
    {
        List<MultaServico> lista = new ArrayList<>();
        String sql = "SELECT * FROM multa_servico";

        try ( Statement st = conexao.createStatement(); ResultSet rs = st.executeQuery( sql ) )
        {

            while ( rs.next() )
            {
                lista.add( mapResultSetToMulta( rs ) );
            }
        }
        return lista;
    }

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

        TbUsuario usuario = new TbUsuario();
        usuario.setCodigo( rs.getInt( "usuario_id" ) );
        multa.setUsuario( usuario );

        return multa;
    }
}
