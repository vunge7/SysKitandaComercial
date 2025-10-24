/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comercial.controller;


import java.sql.Connection;
import entity.Marca;
import entity.Modelo;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import util.BDConexao;

/**
 *
 * @author Domingos Dala Vunge
 */
public class ModelosController implements EntidadeFactory
{

    private BDConexao conexao;

    public ModelosController( BDConexao conexao )
    {
        this.conexao = conexao;
    }

    @Override
    public boolean salvar( Object object )
    {
        Modelo modelo = ( Modelo ) object;
        String INSERT = "INSERT INTO modelo( designacao "
                + ")"
                + " VALUES("
                + "'" + modelo.getDesignacao() + "', "
                + modelo.getFkMarca().getPkMarca()
                + " ) ";

        return conexao.executeUpdate( INSERT );

    }

    @Override
    public boolean actualizar( Object object )
    {
        return true;
    }

    @Override
    public boolean eliminar( int pk_modelo )
    {
        String DELETE = "DELETE FROM modelo WHERE pk_modelo = " + pk_modelo;
        return conexao.executeUpdate( DELETE );
    }

    @Override
    public List<Modelo> listarTodos()
    {

        String FIND_ALL = "SELECT * FROM modelo ORDER BY pk_modelo ASC";
        ResultSet result = conexao.executeQuery( FIND_ALL );
        List<Modelo> lista_modelos = new ArrayList<>();
        Modelo modelo;
        try
        {
            while ( result.next() )
            {
                modelo = new Modelo();
                setResultSetModelo( result, modelo );
                lista_modelos.add( modelo );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return lista_modelos;
    }

    @Override
    public Vector<String> getVector()
    {
        String FIND_ALL = "SELECT * FROM modelo ORDER BY pk_modelo ASC";
        ResultSet result = conexao.executeQuery( FIND_ALL );
        Vector<String> lista_modelo = new Vector<>();
        try
        {
            while ( result.next() )
            {
                lista_modelo.add( result.getString( "designacao" ) );
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return lista_modelo;

    }

    public Vector<String> getVectorByIdMarca( int idMarca )
    {
        String FIND_ALL = "SELECT designacao FROM modelo  WHERE fk_marca = " + idMarca + " ORDER BY designacao ASC";
        ResultSet result = conexao.executeQuery( FIND_ALL );
        Vector<String> lista_modelo = new Vector<>();
        try
        {
            while ( result.next() )
            {
                lista_modelo.add( result.getString( "designacao" ) );
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return lista_modelo;

    }

    @Override
    public Object findById( int pk_modelo )
    {

        String sql = "SELECT * FROM modelo WHERE pk_modelo = " + pk_modelo;
        ResultSet result = conexao.executeQuery( sql );
        Modelo modelo = null;
        try
        {
            if ( result.next() )
            {
                modelo = new Modelo();
                setResultSetModelo( result, modelo );
            }
        }
        catch ( SQLException e )
        {
        }

        return modelo;

    }

    public Modelo getModeloByDesignacao( String designacao )
    {

        String sql = "SELECT *  FROM modelo a WHERE a.designacao = '" + designacao + "'";
        ResultSet result = conexao.executeQuery( sql );
        Modelo modelo = null;
        try
        {
            if ( result.next() )
            {
                modelo = new Modelo();
                setResultSetModelo( result, modelo );
            }
        }
        catch ( SQLException e )
        {
        }

        return modelo;

    }

    private void setResultSetModelo( ResultSet rs, Modelo modelo )
    {

        try
        {

            modelo.setPkModelo( rs.getInt( "pk_modelo" ) );
            modelo.setDesignacao( rs.getString( "designacao" ) );
            modelo.setFkMarca( new Marca( rs.getInt( "fk_marca" ) ) );
        }
        catch ( SQLException e )
        {
        }

    }
}
