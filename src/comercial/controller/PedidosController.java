/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package comercial.controller;

import entity.TbMesas;
import entity.TbPedido;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PedidosController
{

    private Connection conexao;

    public PedidosController( Connection conexao )
    {
        this.conexao = conexao;
    }

    // Criar novo pedido
    public void create( TbPedido pedido ) throws SQLException
    {
        String sql = "INSERT INTO tb_pedido (data_pedido, hora_pedido, fk_mesas, status_pedido, facturado, deposito, valor_em_falta) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try ( PreparedStatement ps = conexao.prepareStatement( sql, Statement.RETURN_GENERATED_KEYS ) )
        {
            ps.setDate( 1, pedido.getDataPedido() != null ? new java.sql.Date( pedido.getDataPedido().getTime() ) : null );
            ps.setTime( 2, pedido.getHoraPedido() != null ? new java.sql.Time( pedido.getHoraPedido().getTime() ) : null );
            ps.setInt( 3, pedido.getFkMesas().getPkMesas() );
            ps.setBoolean( 4, pedido.getStatusPedido() );
            ps.setString( 5, pedido.getFacturado() );
            ps.setDouble( 6, pedido.getDeposito() );
            ps.setDouble( 7, pedido.getValorEmFalta() );

            ps.executeUpdate();

            try ( ResultSet rs = ps.getGeneratedKeys() )
            {
                if ( rs.next() )
                {
                    pedido.setPkPedido( rs.getInt( 1 ) );
                }
            }
        }
    }

    // Atualizar pedido
    public void update( TbPedido pedido ) throws SQLException
    {
        String sql = "UPDATE tb_pedido SET data_pedido=?, hora_pedido=?, fk_mesas=?, status_pedido=?, facturado=?, deposito=?, valor_em_falta=? "
                + "WHERE pk_pedido=?";
        try ( PreparedStatement ps = conexao.prepareStatement( sql ) )
        {
            ps.setDate( 1, pedido.getDataPedido() != null ? new java.sql.Date( pedido.getDataPedido().getTime() ) : null );
            ps.setTime( 2, pedido.getHoraPedido() != null ? new java.sql.Time( pedido.getHoraPedido().getTime() ) : null );
            ps.setInt( 3, pedido.getFkMesas().getPkMesas() );
            ps.setBoolean( 4, pedido.getStatusPedido() );
            ps.setString( 5, pedido.getFacturado() );
            ps.setDouble( 6, pedido.getDeposito() );
            ps.setDouble( 7, pedido.getValorEmFalta() );
            ps.setInt( 8, pedido.getPkPedido() );

            ps.executeUpdate();
        }
    }

    // Deletar pedido
    public void delete( int id ) throws SQLException
    {
        String sql = "DELETE FROM tb_pedido WHERE pk_pedido=?";
        try ( PreparedStatement ps = conexao.prepareStatement( sql ) )
        {
            ps.setInt( 1, id );
            ps.executeUpdate();
        }
    }

    // Buscar por ID
    public TbPedido findById( int id ) throws SQLException
    {
        String sql = "SELECT * FROM tb_pedido WHERE pk_pedido=?";
        try ( PreparedStatement ps = conexao.prepareStatement( sql ) )
        {
            ps.setInt( 1, id );
            try ( ResultSet rs = ps.executeQuery() )
            {
                if ( rs.next() )
                {
                    return mapResultSetToTbPedido( rs );
                }
            }
        }
        return null;
    }

    // Buscar todos os pedidos
    public List<TbPedido> findAll() throws SQLException
    {
        List<TbPedido> lista = new ArrayList<>();
        String sql = "SELECT * FROM tb_pedido";
        try ( Statement st = conexao.createStatement(); ResultSet rs = st.executeQuery( sql ) )
        {
            while ( rs.next() )
            {
                lista.add( mapResultSetToTbPedido( rs ) );
            }
        }
        return lista;
    }

    // Método auxiliar para mapear ResultSet para TbPedido
    private TbPedido mapResultSetToTbPedido( ResultSet rs ) throws SQLException
    {
        TbPedido pedido = new TbPedido();
        pedido.setPkPedido( rs.getInt( "pk_pedido" ) );
        pedido.setDataPedido( rs.getDate( "data_pedido" ) ); // java.util.Date ou java.sql.Date
        pedido.setHoraPedido( rs.getTime( "hora_pedido" ) ); // java.sql.Time
        pedido.setFkMesas( new TbMesas( rs.getInt( "fk_mesas" ) ) );
        pedido.setStatusPedido( rs.getBoolean( "status_pedido" ) );
        pedido.setFacturado( rs.getString( "facturado" ) );
        pedido.setDeposito( rs.getDouble( "deposito" ) );
        pedido.setValorEmFalta( rs.getDouble( "valor_em_falta" ) );
        return pedido;
    }

    // Buscar o último pedido por designação da mesa
    public int getLastPedidoByMesa( String mesa ) throws SQLException
    {
        String sql = "SELECT MAX(p.pk_pedido) AS last_pk "
                + "FROM tb_pedido p "
                + "INNER JOIN tb_mesas m ON p.fk_mesas = m.pk_mesas "
                + "WHERE m.designacao = ?";
        try ( PreparedStatement ps = conexao.prepareStatement( sql ) )
        {
            ps.setString( 1, mesa );

            try ( ResultSet rs = ps.executeQuery() )
            {
                if ( rs.next() )
                {
                    int lastPk = rs.getInt( "last_pk" );
                    // Se não houver pedidos, rs.getInt retorna 0
                    return lastPk;
                }
            }
        }
        return 0;
    }

    // Retorna o último pedido pela designação da mesa (sem filtro de status)
    public int getLastPedidoByDesignacaoMesaSemStatus( String mesa ) throws SQLException
    {
        System.out.println( "DESIGNACAO " + mesa );

        String sql = "SELECT MAX(p.pk_pedido) AS last_pk "
                + "FROM tb_pedido p "
                + "INNER JOIN tb_mesas m ON p.fk_mesas = m.pk_mesas "
                + "WHERE m.designacao = ?";

        try ( PreparedStatement ps = conexao.prepareStatement( sql ) )
        {
            ps.setString( 1, mesa );

            try ( ResultSet rs = ps.executeQuery() )
            {
                if ( rs.next() )
                {
                    return rs.getInt( "last_pk" ); // retorna 0 se não houver registros
                }
            }
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            return 0;
        }

        return 0;
    }

}
