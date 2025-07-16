/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comercial.controller;

import entity.AnoEconomico;
import entity.Cambio;
import entity.Compras;
import entity.Documento;
import entity.TbArmazem;
import entity.TbBanco;
import entity.TbCliente;
import entity.ItemCompras;
import entity.TbLugares;
import entity.TbMesas;
import entity.TbPreco;
import entity.TbProduto;
import entity.TbVenda;
import entity.TbUsuario;
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
public class ItemComprasController implements EntidadeFactory
{

    private BDConexao conexao;

    public ItemComprasController( BDConexao conexao )
    {
        this.conexao = conexao;
    }

    @Override
    public boolean salvar( Object object )
    {
        ItemCompras itemCompras = ( ItemCompras ) object;
        String INSERT = "INSERT INTO item_compras( preco_compra , quantidade , valor_iva , motivo_isensao , desconto , total , "
                + " codigo_isensao , fk_produto  , fk_compra "
                + ")"
                + " VALUES("
                + itemCompras.getPrecoCompra() + " , "
                + itemCompras.getQuantidade() + " , "
                + itemCompras.getValorIva() + " , "
                + "'" + itemCompras.getMotivoIsensao() + "' , "
                + itemCompras.getDesconto() + " , "
                + itemCompras.getTotal() + " , "
                + "'" + itemCompras.getCodigoIsensao() + "' , "
                + itemCompras.getFkProduto().getCodigo() + ", "
                + itemCompras.getFkCompra().getPkCompra()
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
        String DELETE = "DELETE FROM item_compras WHERE pk_itm_compras = " + codigo;
        return conexao.executeUpdate( DELETE );
    }

    @Override
    public List<ItemCompras> listarTodos()
    {

        String FIND_ALL = "SELECT * FROM item_compras ORDER BY pk_itm_compras ASC";
        ResultSet result = conexao.executeQuery( FIND_ALL );
        List<ItemCompras> lista_item_compra = new ArrayList<>();
        ItemCompras item_compras;
        try
        {

            while ( result.next() )
            {
                item_compras = new ItemCompras();
                item_compras.setPkItmCompras( result.getInt( "pk_itm_compras" ) );
                item_compras.setPrecoCompra( result.getDouble( "preco_compra" ) );
                item_compras.setQuantidade( result.getDouble( "quantidade" ) );
                item_compras.setValorIva( result.getDouble( "total_venda" ) );
                item_compras.setMotivoIsensao( result.getString( "motivo_isensao" ) );
                item_compras.setDesconto( result.getDouble( "desconto" ) );
                item_compras.setTotal( result.getDouble( "total" ) );
                item_compras.setCodigoIsensao( result.getString( "codigo_isensao" ) );
                item_compras.setFkProduto( new TbProduto( result.getInt( "fk_produto" ) ) );
                item_compras.setFkCompra( new Compras( result.getInt( "fk_compra" ) ) );

                lista_item_compra.add( item_compras );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return lista_item_compra;
    }

    @Override
    public Vector<String> getVector()
    {
        throw new UnsupportedOperationException( "Not supported yet." ); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object findById( int codigo )
    {

        String FIND__BY_CODIGO = "SELECT * FROM item_compras WHERE pk_itm_compras = " + codigo;
        ResultSet result = conexao.executeQuery( FIND__BY_CODIGO );
        ItemCompras item_compras = null;
        try
        {

            if ( result.next() )
            {
                item_compras = new ItemCompras();
                item_compras.setPkItmCompras( result.getInt( "pk_itm_compras" ) );
                item_compras.setPrecoCompra( result.getDouble( "preco_compra" ) );
                item_compras.setQuantidade( result.getDouble( "quantidade" ) );
                item_compras.setValorIva( result.getDouble( "total_venda" ) );
                item_compras.setMotivoIsensao( result.getString( "motivo_isensao" ) );
                item_compras.setDesconto( result.getDouble( "desconto" ) );
                item_compras.setTotal( result.getDouble( "total" ) );
                item_compras.setCodigoIsensao( result.getString( "codigo_isensao" ) );
                item_compras.setFkProduto( new TbProduto( result.getInt( "fk_produto" ) ) );
                item_compras.setFkCompra( new Compras( result.getInt( "fk_compra" ) ) );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return item_compras;

    }

    public List<ItemCompras> listarTodosByIdCompra( int idCompra )
    {

        String FIND_ALL = "SELECT * FROM item_compras WHERE fk_compra = " + idCompra;
        ResultSet result = conexao.executeQuery( FIND_ALL );
        List<ItemCompras> lista_item_compra = new ArrayList<>();
        ItemCompras item_compras;
        try
        {

            while ( result.next() )
            {
                item_compras = new ItemCompras();
                item_compras.setPkItmCompras( result.getInt( "pk_itm_compras" ) );
                item_compras.setPrecoCompra( result.getDouble( "preco_compra" ) );
                item_compras.setQuantidade( result.getDouble( "quantidade" ) );
                item_compras.setValorIva( result.getDouble( "valor_iva" ) );
                item_compras.setMotivoIsensao( result.getString( "motivo_isensao" ) );
                item_compras.setDesconto( result.getDouble( "desconto" ) );
                item_compras.setTotal( result.getDouble( "total" ) );
                item_compras.setCodigoIsensao( result.getString( "codigo_isensao" ) );
                item_compras.setFkProduto( new TbProduto( result.getInt( "fk_produto" ) ) );
                item_compras.setFkCompra( new Compras( result.getInt( "fk_compra" ) ) );

                lista_item_compra.add( item_compras );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return lista_item_compra;
    }

    public ItemCompras getLastVenda()
    {

        String FIND__BY_CODIGO = "SELECT MAX(pk_itm_compras) as maximo_id, i.*  FROM item_compras i";
        ResultSet result = conexao.executeQuery( FIND__BY_CODIGO );
        ItemCompras item_compras = null;
        try
        {

            if ( result.next() )
            {
                item_compras = new ItemCompras();
                item_compras.setPkItmCompras( result.getInt( "maximo_id" ) );
                item_compras.setPrecoCompra( result.getDouble( "preco_compra" ) );
                item_compras.setQuantidade( result.getDouble( "quantidade" ) );
                item_compras.setValorIva( result.getDouble( "total_venda" ) );
                item_compras.setMotivoIsensao( result.getString( "motivo_isensao" ) );
                item_compras.setDesconto( result.getDouble( "desconto" ) );
                item_compras.setTotal( result.getDouble( "total" ) );
                item_compras.setCodigoIsensao( result.getString( "codigo_isensao" ) );
                item_compras.setFkProduto( new TbProduto( result.getInt( "fk_produto" ) ) );
                item_compras.setFkCompra( new Compras( result.getInt( "fk_compra" ) ) );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return item_compras;

    }

    public List<ItemCompras> getAllItensByCodFact( String codFact )
    {

        String FIND_ALL = "SELECT i.* FROM item_compras i INNER JOIN compras c ON c.pk_compra = i.fk_compra"
                + "  WHERE c.cod_fact = '" + codFact + "' AND despacho_encomenda = 'false'";
        ResultSet result = conexao.executeQuery( FIND_ALL );
        List<ItemCompras> lista_item_compra = new ArrayList<>();
        ItemCompras item_compras;
        try
        {
            while ( result.next() )
            {
                item_compras = new ItemCompras();
                item_compras.setPkItmCompras( result.getInt( "pk_itm_compras" ) );
                item_compras.setPrecoCompra( result.getDouble( "preco_compra" ) );
                item_compras.setQuantidade( result.getDouble( "quantidade" ) );
                item_compras.setValorIva( result.getDouble( "valor_iva" ) );
                item_compras.setMotivoIsensao( result.getString( "motivo_isensao" ) );
                item_compras.setDesconto( result.getDouble( "desconto" ) );
                item_compras.setTotal( result.getDouble( "total" ) );
                item_compras.setCodigoIsensao( result.getString( "codigo_isensao" ) );
                item_compras.setFkProduto( new TbProduto( result.getInt( "fk_produto" ) ) );
                item_compras.setFkCompra( new Compras( result.getInt( "fk_compra" ) ) );

                lista_item_compra.add( item_compras );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return lista_item_compra;
    }

}
