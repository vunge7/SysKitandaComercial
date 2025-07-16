/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comercial.controller;

import entity.AnoEconomico;
import entity.Cambio;
import entity.Documento;
import entity.TbArmazem;
//import entity.TbBanco;
import entity.TbCliente;
import entity.TbItemVenda;
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
public class LugaresController implements EntidadeFactory
{

    private BDConexao conexao;

    public LugaresController( BDConexao conexao )
    {
        this.conexao = conexao;
    }

    @Override
    public boolean salvar( Object object )
    {
        TbLugares lugares = ( TbLugares ) object;
        String INSERT = "INSERT INTO tb_lugares( designacao , "
                + ")"
                + " VALUES("
                + "'" + lugares.getDesignacao() + "' , "
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
        String DELETE = "DELETE FROM tb_lugares WHERE pk_lugares = " + codigo;
        return conexao.executeUpdate( DELETE );
    }

    @Override
    public List<TbLugares> listarTodos()
    {

        String FIND_ALL = "SELECT * FROM tb_lugares ORDER BY pk_lugares ASC";
        ResultSet result = conexao.executeQuery( FIND_ALL );
        List<TbLugares> lista_lugares = new ArrayList<>();
        TbLugares lugares;
        try
        {

            while ( result.next() )
            {
                lugares = new TbLugares();
                lugares.setDesignacao( result.getString( "designacao" ) );
                lista_lugares.add( lugares );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return lista_lugares;
    }

    @Override
    public Vector<String> getVector()
    {
        throw new UnsupportedOperationException( "Not supported yet." ); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object findById( int codigo )
    {

        String FIND__BY_CODIGO = "SELECT * FROM tb_lugares WHERE pk_lugares = " + codigo;
        ResultSet result = conexao.executeQuery( FIND__BY_CODIGO );
        TbLugares lugares = null;
        try
        {

            if ( result.next() )
            {
                lugares = new TbLugares();
                lugares.setPkLugares( result.getInt( "pk_lugares" ) );
                lugares.setDesignacao( result.getString( "designacao" ) );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return lugares;

    }

    public Object findByLugar( String designacao )
    {

        String FIND__BY_CODIGO = "SELECT * FROM tb_lugares WHERE designacao = '" + designacao + "'";
        ResultSet result = conexao.executeQuery( FIND__BY_CODIGO );
        TbLugares lugares = null;
        try
        {

            if ( result.next() )
            {
                lugares = new TbLugares();
                lugares.setPkLugares( result.getInt( "pk_lugares" ) );
                lugares.setDesignacao( result.getString( "designacao" ) );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return lugares;

    }

    public TbLugares getLastLugar()
    {

        String FIND__BY_CODIGO = "SELECT MAX(pk_lugares) as maximo_id, l.*  FROM tb_lugares l";
        ResultSet result = conexao.executeQuery( FIND__BY_CODIGO );
        TbLugares lugares = null;
        try
        {

            if ( result.next() )
            {
                lugares = new TbLugares();
                lugares.setPkLugares( result.getInt( "maximo_id" ) );
                lugares.setDesignacao( result.getString( "designacao" ) );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return lugares;

    }

    public int getIdByDescricao( String designacao )
    {
        String FIND_BY_CODIGO = "SELECT pk_lugares AS id FROM tb_lugares WHERE designacao = '" + designacao + "'";
        ResultSet result = conexao.executeQuery( FIND_BY_CODIGO );
        try
        {
            if ( result.next() )
            {
                return result.getInt( "id" );

            }
        }
        catch ( SQLException e )
        {
//            e.printStackTrace();
        }
        return 0;

    }

}
