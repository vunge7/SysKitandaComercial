/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package comercial.controller;


import java.sql.Connection;
import entity.TbItemSaidas;
import entity.TbProduto;
import entity.TbSaidasProdutos;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Engº Domingos Dala Vunge
 * @created 17/set/2025
 * @lastModified 17/set/2025
 */
public class ItemSaidasController
{

    private Connection conn;

    public ItemSaidasController( Connection conn )
    {
        this.conn = conn;
    }

    // CREATE
    public void salvar(TbItemSaidas item) throws SQLException {
    String sql = "INSERT INTO tb_item_saidas (quantidade, preco_compra, fk_produtos, fk_saidas_produtos) VALUES (?, ?, ?, ?)";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setDouble(1, item.getQuantidade());
        stmt.setBigDecimal(2, item.getPrecoCompra());
        stmt.setInt(3, item.getFkProdutos().getCodigo());
        stmt.setInt(4, item.getFkSaidasProdutos().getPkSaidasProdutos());
        stmt.executeUpdate();
    }
}

//    public void salvar( TbItemSaidas item ) throws SQLException
//    {
//        String sql = "INSERT INTO tb_item_saidas (quantidade, preco_compra, fk_produtos, fk_saidas_produtos) VALUES (?, ?, ?, ?)";
//        try ( PreparedStatement stmt = conn.prepareStatement( sql ) )
//        {
//            stmt.setDouble( 1, item.getQuantidade() );
//            stmt.setBigDecimal( 2, item.getPrecoCompra() );
//            stmt.setInt( 3, item.getFkProdutos().getCodigo() );
//            stmt.setInt( 4, item.getFkSaidasProdutos().getPkSaidasProdutos() );
//            stmt.executeUpdate();
//        }
//    }

    // READ by ID
    public TbItemSaidas buscarPorId( int id ) throws SQLException
    {
        String sql = "SELECT * FROM tb_item_saidas WHERE codigo = ?";
        try ( PreparedStatement stmt = conn.prepareStatement( sql ) )
        {
            stmt.setInt( 1, id );
            try ( ResultSet rs = stmt.executeQuery() )
            {
                if ( rs.next() )
                {
                    TbItemSaidas item = new TbItemSaidas();
                    item.setCodigo( rs.getInt( "codigo" ) );
                    item.setQuantidade( rs.getDouble( "quantidade" ) );
                    item.setPrecoCompra( rs.getBigDecimal( "preco_compra" ) );

                    // Relacionamentos
                    TbProduto produto = new TbProduto( rs.getInt( "fk_produtos" ) );
                    TbSaidasProdutos saida = new TbSaidasProdutos( rs.getInt( "fk_saidas_produtos" ) );

                    item.setFkProdutos( produto );
                    item.setFkSaidasProdutos( saida );

                    return item;
                }
            }
        }
        return null;
    }

    // READ all
    public List<TbItemSaidas> listarTodos() throws SQLException
    {
        List<TbItemSaidas> lista = new ArrayList<>();
        String sql = "SELECT * FROM tb_item_saidas";
        try ( PreparedStatement stmt = conn.prepareStatement( sql ); ResultSet rs = stmt.executeQuery() )
        {
            while ( rs.next() )
            {
                TbItemSaidas item = new TbItemSaidas();
                item.setCodigo( rs.getInt( "codigo" ) );
                item.setQuantidade( rs.getDouble( "quantidade" ) );
                item.setPrecoCompra( rs.getBigDecimal( "preco_compra" ) );

                // Relacionamentos
                TbProduto produto = new TbProduto( rs.getInt( "fk_produtos" ) );
                TbSaidasProdutos saida = new TbSaidasProdutos( rs.getInt( "fk_saidas_produtos" ) );

                item.setFkProdutos( produto );
                item.setFkSaidasProdutos( saida );

                lista.add( item );
            }
        }
        return lista;
    }

    // UPDATE
    public void atualizar( TbItemSaidas item ) throws SQLException
    {
        String sql = "UPDATE tb_item_saidas SET quantidade=?, preco_compra=?, fk_produtos=?, fk_saidas_produtos=? WHERE codigo=?";
        try ( PreparedStatement stmt = conn.prepareStatement( sql ) )
        {
            stmt.setDouble( 1, item.getQuantidade() );
            stmt.setBigDecimal( 2, item.getPrecoCompra() );
            stmt.setInt( 3, item.getFkProdutos().getCodigo() );
            stmt.setInt( 4, item.getFkSaidasProdutos().getPkSaidasProdutos() );
            stmt.setInt( 5, item.getCodigo() );
            stmt.executeUpdate();
        }
    }

    // DELETE
    public void deletar( int id ) throws SQLException
    {
        String sql = "DELETE FROM tb_item_saidas WHERE codigo=?";
        try ( PreparedStatement stmt = conn.prepareStatement( sql ) )
        {
            stmt.setInt( 1, id );
            stmt.executeUpdate();
        }
    }

    public void deleteAllItemSaidasByIdSaidaReciclagem( int pkSaidasProdutos ) throws SQLException
    {
        String sql = "DELETE FROM tb_item_saidas WHERE fk_saidas_produtos = ?";
        try ( PreparedStatement stmt = conn.prepareStatement( sql ) )
        {
            stmt.setInt( 1, pkSaidasProdutos );
            stmt.executeUpdate();
        }
    }

    public List<TbItemSaidas> getAllItemSaidasByIdSaida( int pkSaidasProdutos ) throws SQLException
    {
        List<TbItemSaidas> lista = new ArrayList<>();

        String sql = "SELECT * FROM tb_item_saidas v "
                + " INNER JOIN tb_saidas_produtos s on s.pk_saidas_produtos = v.fk_saidas_produtos "
                + " WHERE v.fk_saidas_produtos = ? "
                + " AND s.status_eliminado = 'false' ";

        try ( PreparedStatement stmt = conn.prepareStatement( sql ) )
        {
            stmt.setInt( 1, pkSaidasProdutos );
            try ( ResultSet rs = stmt.executeQuery() )
            {
                while ( rs.next() )
                {
                    lista.add( mapearItemSaidas( rs ) );
                }
            }
        }
        return lista;
    }

    private TbItemSaidas mapearItemSaidas( ResultSet rs ) throws SQLException
    {
        TbItemSaidas item = new TbItemSaidas();
        item.setCodigo( rs.getInt( "codigo" ) );
        item.setQuantidade( rs.getDouble( "quantidade" ) );
        item.setPrecoCompra( rs.getBigDecimal( "preco_compra" ) );
        // Relacionamentos
        TbProduto produto = new TbProduto( rs.getInt( "fk_produtos" ) );
        TbSaidasProdutos saida = new TbSaidasProdutos( rs.getInt( "fk_saidas_produtos" ) );

        item.setFkProdutos( produto );
        item.setFkSaidasProdutos( saida );

        return item;
    }

}
