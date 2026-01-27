/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package comercial.controller;

/**
 *
 * @author Engº Domingos Dala Vunge
 * @created 21/nov/2025
 * @lastModified 21/nov/2025
 */
import enties.util.CozinhaPedido;
import entity.TbItemPedidos;
import entity.TbLugares;
import entity.TbPedido;
import entity.TbProduto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ItemPedidosController
{

    private Connection conexao;

    public ItemPedidosController( Connection conexao )
    {
        this.conexao = conexao;
    }

    // Salvar um novo TbItemPedidos
    public void create( TbItemPedidos item ) throws SQLException
    {
        String sql = "INSERT INTO tb_item_pedidos (fk_lugares, fk_produtos, qtd, fk_pedidos, status_convertido, total_item, status_enviado, status_efectuado, data_entrega, obs, preco) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try ( PreparedStatement ps = conexao.prepareStatement( sql, Statement.RETURN_GENERATED_KEYS ) )
        {
            ps.setInt( 1, item.getFkLugares().getPkLugares() );
            ps.setInt( 2, item.getFkProdutos().getCodigo() );
            ps.setDouble( 3, item.getQtd() );
            ps.setInt( 4, item.getFkPedidos().getPkPedido() );
            ps.setBoolean( 5, item.getStatusConvertido() );
            ps.setDouble( 6, item.getTotalItem() );
            ps.setBoolean( 7, item.getStatusEnviado() );
            ps.setBoolean( 8, item.getStatusEfectuado() );
            ps.setTimestamp( 9, item.getDataEntrega() != null ? new Timestamp( item.getDataEntrega().getTime() ) : null );
            ps.setString( 10, item.getObs() );
            ps.setDouble( 11, item.getPreco() );

            ps.executeUpdate();

            try ( ResultSet rs = ps.getGeneratedKeys() )
            {
                if ( rs.next() )
                {
                    item.setPkItemPedidos( rs.getInt( 1 ) );
                }
            }
        }
    }

    // Atualizar TbItemPedidos
    public void update( TbItemPedidos item ) throws SQLException
    {
        String sql = "UPDATE tb_item_pedidos SET fk_lugares=?, fk_produtos=?, qtd=?, fk_pedidos=?, status_convertido=?, total_item=?, status_enviado=?, status_efectuado=?, data_entrega=?, obs=?, preco=? "
                + "WHERE pk_item_pedidos=?";
        try ( PreparedStatement ps = conexao.prepareStatement( sql ) )
        {
            ps.setInt( 1, item.getFkLugares().getPkLugares() );
            ps.setInt( 2, item.getFkProdutos().getCodigo() );
            ps.setDouble( 3, item.getQtd() );
            ps.setInt( 4, item.getFkPedidos().getPkPedido() );
            ps.setBoolean( 5, item.getStatusConvertido() );
            ps.setDouble( 6, item.getTotalItem() );
            ps.setBoolean( 7, item.getStatusEnviado() );
            ps.setBoolean( 8, item.getStatusEfectuado() );
            ps.setTimestamp( 9, item.getDataEntrega() != null ? new Timestamp( item.getDataEntrega().getTime() ) : null );
            ps.setString( 10, item.getObs() );
            ps.setDouble( 11, item.getPreco() );
            ps.setInt( 12, item.getPkItemPedidos() );

            ps.executeUpdate();
        }
    }

    // Deletar TbItemPedidos
    public void delete( int id ) throws SQLException
    {
        String sql = "DELETE FROM tb_item_pedidos WHERE pk_item_pedidos=?";
        try ( PreparedStatement ps = conexao.prepareStatement( sql ) )
        {
            ps.setInt( 1, id );
            ps.executeUpdate();
        }
    }
    // Deletar TbItemPedidos
    public void deleteByIdPedido( int idPedido ) throws SQLException
    {
        String sql = "DELETE FROM tb_item_pedidos WHERE fk_pedidos=?";
        try ( PreparedStatement ps = conexao.prepareStatement( sql ) )
        {
            ps.setInt( 1, idPedido );
            ps.executeUpdate();
        }
    }

    // Buscar por ID
    public TbItemPedidos findById( int id ) throws SQLException
    {
        String sql = "SELECT * FROM tb_item_pedidos WHERE pk_item_pedidos=?";
        try ( PreparedStatement ps = conexao.prepareStatement( sql ) )
        {
            ps.setInt( 1, id );
            try ( ResultSet rs = ps.executeQuery() )
            {
                if ( rs.next() )
                {
                    return mapResultSetToTbItemPedidos( rs );
                }
            }
        }
        return null;
    }

    // Buscar todos
    public List<TbItemPedidos> findAll() throws SQLException
    {
        List<TbItemPedidos> lista = new ArrayList<>();
        String sql = "SELECT * FROM tb_item_pedidos";
        try ( Statement st = conexao.createStatement(); ResultSet rs = st.executeQuery( sql ) )
        {
            while ( rs.next() )
            {
                lista.add( mapResultSetToTbItemPedidos( rs ) );
            }
        }
        return lista;
    }

    // Buscar por fk_pedidos
    public List<TbItemPedidos> findByPedido( int fkPedidos ) throws SQLException
    {
        List<TbItemPedidos> lista = new ArrayList<>();
        String sql = "SELECT * FROM tb_item_pedidos WHERE fk_pedidos=?";
        try ( PreparedStatement ps = conexao.prepareStatement( sql ) )
        {
            ps.setInt( 1, fkPedidos );
            try ( ResultSet rs = ps.executeQuery() )
            {
                while ( rs.next() )
                {
                    lista.add( mapResultSetToTbItemPedidos( rs ) );
                }
            }
        }
        return lista;
    }

    // Método auxiliar para mapear ResultSet para TbItemPedidos
    private TbItemPedidos mapResultSetToTbItemPedidos( ResultSet rs ) throws SQLException
    {
        TbItemPedidos item = new TbItemPedidos();
        item.setPkItemPedidos( rs.getInt( "pk_item_pedidos" ) );
        item.setFkLugares( new TbLugares( rs.getInt( "fk_lugares" ) ) );
        item.setFkProdutos( new TbProduto( rs.getInt( "fk_produtos" ) ) );
        item.setQtd( rs.getDouble( "qtd" ) );
        item.setFkPedidos( new TbPedido( rs.getInt( "fk_pedidos" ) ) );
        item.setStatusConvertido( rs.getBoolean( "status_convertido" ) );
        item.setTotalItem( rs.getDouble( "total_item" ) );
        item.setStatusEnviado( rs.getBoolean( "status_enviado" ) );
        item.setStatusEfectuado( rs.getBoolean( "status_efectuado" ) );
        Timestamp ts = rs.getTimestamp( "data_entrega" );
        if ( ts != null )
        {
            item.setDataEntrega( new java.util.Date( ts.getTime() ) );
        }

        item.setObs( rs.getString( "obs" ) );
        item.setPreco( rs.getDouble( "preco" ) );
        return item;
    }

    // Retorna a quantidade de itens de um pedido que não foram convertidos
    public long getQtdItensByIdPedido( int pkPedido ) throws SQLException
    {
        String sql = "SELECT COUNT(*) AS qtd "
                + "FROM tb_item_pedidos "
                + "WHERE fk_pedidos = ? AND status_convertido = false";
        try ( PreparedStatement ps = conexao.prepareStatement( sql ) )
        {
            ps.setInt( 1, pkPedido );

            try ( ResultSet rs = ps.executeQuery() )
            {
                if ( rs.next() )
                {
                    return rs.getLong( "qtd" );
                }
            }
        }
        return 0;
    }

    public List<CozinhaPedido> listarPedidosCozinha()
    {

        List<CozinhaPedido> lista = new ArrayList<>();

        String sql
                = "SELECT "
                + "   p.pk_pedido, "
                + "   ip.pk_item_pedidos, "
                + "   m.designacao AS mesa, "
                + "   l.designacao AS lugar, "
                + "   pr.designacao AS produto, "
                + "   ip.qtd "
                + "FROM tb_pedido p "
                + "INNER JOIN tb_item_pedidos ip ON ip.fk_pedidos = p.pk_pedido "
                + "INNER JOIN tb_produto pr ON pr.codigo = ip.fk_produtos "
                + "INNER JOIN tb_mesas m ON m.pk_mesas = p.fk_mesas "
                + "INNER JOIN tb_lugares l ON l.pk_lugares = ip.fk_lugares "
                + "WHERE pr.cozinha = 'Enviar Ticket' "
                + "ORDER BY ip.pk_item_pedidos";

        try ( PreparedStatement ps = conexao.prepareStatement( sql ); ResultSet rs = ps.executeQuery() )
        {

            while ( rs.next() )
            {

                CozinhaPedido c = new CozinhaPedido();
                c.setPedido( rs.getInt( "pk_pedido" ) );
                c.setPkItemPedidos(rs.getInt( "pk_item_pedidos" ) );
                c.setMesa( rs.getString( "mesa" ) );
                c.setLugar( rs.getString( "lugar" ) );
                c.setProduto( rs.getString( "produto" ) );
                c.setQuantidade( rs.getDouble( "qtd" ) );

                lista.add( c );
            }
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }

        return lista;
    }

}
