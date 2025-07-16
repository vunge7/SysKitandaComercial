/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comercial.controller;

import entity.FichaTecnica;
import entity.TbArmazem;
import entity.TbEntrada;
import entity.TbItemEntradas;
import entity.TbProduto;
import entity.TbUsuario;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import util.BDConexao;
import util.MetodosUtil;

/**
 *
 * @author Martinho Luis
 */
public class ItemEntradasController implements EntidadeFactory
{

    private BDConexao conexao;

    public ItemEntradasController( BDConexao conexao )
    {
        this.conexao = conexao;
    }

    @Override
    public boolean salvar( Object object )
    {
        TbItemEntradas item_entradas = (TbItemEntradas) object;
        String INSERT = "INSERT INTO tb_item_entradas("
                + " id_produto , "
                + " quantidade , "
                + " fk_entradas "
                + ")"
                + " VALUES("
                + item_entradas.getIdProduto().getCodigo() + " , "
                + item_entradas.getQuantidade() + " , "
                + item_entradas.getFkEntradas().getIdEntrada()+ "  "
                + " ) ";

        System.out.println( "Modo INSERT" + INSERT );
        return conexao.executeUpdate( INSERT );

    }

    @Override
    public boolean actualizar( Object object )
    {
        TbItemEntradas item_entradas = (TbItemEntradas) object;

        String sql = "UPDATE tb_item_entradas SET "
                + " , id_produto = " + item_entradas.getIdProduto().getCodigo()
                + ", quantidade = " + item_entradas.getQuantidade()
                + ", fk_entradas = '" + item_entradas.getFkEntradas().getIdEntrada()
                + " WHERE id_item_entradas = ?";
        System.out.println( sql );
        return conexao.executeUpdate( sql );
    }

    @Override
    public boolean eliminar( int codigo )
    {
        String DELETE = "DELETE FROM tb_item_entradas WHERE id_item_entradas = " + codigo;
        return conexao.executeUpdate( DELETE );
    }

    @Override
    public List<TbItemEntradas> listarTodos()
    {

        String FIND_ALL = "SELECT * FROM tb_item_entradas ORDER BY id_item_entradas ASC";
        ResultSet result = conexao.executeQuery( FIND_ALL );
        List<TbItemEntradas> lista_item_entradas = new ArrayList<>();
        TbItemEntradas item_entradas;
        try
        {
            while ( result.next() )
            {
                item_entradas = new TbItemEntradas();
                item_entradas.setIdItemEntradas(result.getInt( "id_item_entradas" ) );
                item_entradas.setIdProduto(new TbProduto (result.getInt( "id_produto" ) ));
                item_entradas.setQuantidade(result.getInt( "quantidade" ) );
                item_entradas.setFkEntradas( new TbEntrada( result.getInt("fk_entradas" ) ));

                lista_item_entradas.add( item_entradas );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return lista_item_entradas;
    }

    @Override
    public Vector<String> getVector()
    {
        throw new UnsupportedOperationException( "Not supported yet." ); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object findById( int codigo )
    {

        String FIND__BY_CODIGO = "SELECT * FROM tb_item_entradas WHERE id_item_entradas = " + codigo;
        ResultSet result = conexao.executeQuery( FIND__BY_CODIGO );
        TbItemEntradas item_entradas = null;
        try
        {

            if ( result.next() )
            {
                item_entradas = new TbItemEntradas();
                item_entradas.setIdItemEntradas(result.getInt( "id_item_entradas" ) );
                item_entradas.setIdProduto(new TbProduto (result.getInt( "id_produto" ) ));
                item_entradas.setQuantidade(result.getInt( "quantidade" ) );
                item_entradas.setFkEntradas( new TbEntrada( result.getInt("fk_entradas" ) ));
            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return item_entradas;

    }

    public TbItemEntradas getLastItemEntrada()
    {

        String FIND__BY_CODIGO = "   SELECT * FROM tb_item_entradas WHERE id_item_entradas = ( SELECT MAX(id_item_entradas) as maximo_id   FROM tb_item_entradas f ) ";
        ResultSet result = conexao.executeQuery( FIND__BY_CODIGO );
        TbItemEntradas item_entradas = null;
        try
        {

            if ( result.next() )
            {
                item_entradas = new TbItemEntradas();
                item_entradas.setIdItemEntradas(result.getInt( "id_item_entradas" ) );
                item_entradas.setIdProduto(new TbProduto (result.getInt( "id_produto" ) ));
                item_entradas.setQuantidade(result.getInt( "quantidade" ) );
                item_entradas.setFkEntradas( new TbEntrada( result.getInt("fk_entradas" ) ));

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return item_entradas;

    }

    private TbItemEntradas getEntradaResultSet( TbItemEntradas item_entradas, ResultSet result )
    {
        try
        {
                item_entradas.setIdItemEntradas(result.getInt( "id_item_entradas" ) );
                item_entradas.setIdProduto(new TbProduto (result.getInt( "id_produto" ) ));
                item_entradas.setQuantidade(result.getInt( "quantidade" ) );
                item_entradas.setFkEntradas( new TbEntrada( result.getInt("fk_entradas" ) ));

        }
        catch ( Exception e )
        {
        }

        return item_entradas;
    }

//    public List<TbItemEntradas> listarTodasEntradasByData1AndData2( Date data_1, Date data_2 )
//    {
//        String FIND_ALL = "SELECT * FROM tb_item_entradas WHERE DATE(data_entrada) BETWEEN '" + MetodosUtil.getDataBanco( data_1 ) + "' AND '" + MetodosUtil.getDataBanco( data_2 ) + "'";
//        ResultSet result = conexao.executeQuery( FIND_ALL );
//        List<TbItemEntradas> lista_item_entradas = new ArrayList<>();
//        TbItemEntradas item_entradas;
//        try
//        {
//            while ( result.next() )
//            {
//                item_entradas = new TbItemEntradas();
//                item_entradas = getEntradaResultSet( item_entradas, result );
//                lista_item_entradas.add( item_entradas );
//            }
//        }
//        catch ( SQLException e )
//        {
//            e.printStackTrace();
//        }
//
//        return lista_item_entradas;
//    }

    /**
     *
     * @param cod_fact
     * @decricao busca todos os recibos de uma determnada factura.
     * @return
     */

    public TbItemEntradas getEntradas( int codigo )
    {
        String FIND_BY_CODIGO = "SELECT f.id_item_entradas FROM tb_item_entradas f WHERE id_item_entradas = " + codigo;

        ResultSet result = conexao.executeQuery( FIND_BY_CODIGO );
        TbItemEntradas item_entradas = null;
        try
        {

            if ( result.next() )
            {

                item_entradas = new TbItemEntradas();
                item_entradas = getEntradaResultSet( item_entradas, result );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return item_entradas;

    }

    public boolean existeEntradas( int codigo )
    {
        String sql = "SELECT *  FROM tb_item_entradas WHERE id_item_entradas = " + codigo;
        ResultSet rs = conexao.executeQuery( sql );
        try
        {
            if ( rs.next() )
            {
                return true;
            }
        }
        catch ( Exception e )
        {
        }
        return false;

    }

    public static void main( String[] args )
    {
        BDConexao conexao = new BDConexao();
        ItemEntradasController f = new ItemEntradasController( conexao );

    }

}
