/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comercial.controller;


import java.sql.Connection;
import entity.Marca;
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
public class MarcasController implements EntidadeFactory
{

    private BDConexao conexao;

    public MarcasController( BDConexao conexao )
    {
        this.conexao = conexao;
    }

    @Override
    public boolean salvar( Object object )
    {
        Marca mesas = ( Marca ) object;
        String INSERT = "INSERT INTO marca( designacao "
                + ")"
                + " VALUES("
                + "'" + mesas.getDesignacao() + "'"
                + " ) ";

        return conexao.executeUpdate( INSERT );

    }

    @Override
    public boolean actualizar( Object object )
    {
        return true;
    }

    @Override
    public boolean eliminar( int pk_marca )
    {
        String DELETE = "DELETE FROM marca WHERE pk_marca = " + pk_marca;
        return conexao.executeUpdate( DELETE );
    }

    @Override
    public List<Marca> listarTodos()
    {

        String FIND_ALL = "SELECT * FROM marca ORDER BY pk_marca ASC";
        ResultSet result = conexao.executeQuery( FIND_ALL );
        List<Marca> lista_marcas = new ArrayList<>();
        Marca marca;
        try
        {
            while ( result.next() )
            {
                marca = new Marca();
                setResultSetMarca( result, marca );
                lista_marcas.add( marca );
            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return lista_marcas;
    }

    @Override
    public Vector<String> getVector()
    {
        String FIND_ALL = "SELECT designacao FROM marca ORDER BY designacao ASC";
        ResultSet result = conexao.executeQuery( FIND_ALL );
        Vector<String> lista_mesas = new Vector<>();
        try
        {
            while ( result.next() )
            {
                lista_mesas.add( result.getString( "designacao" ) );
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return lista_mesas;

    }

    @Override
    public Object findById( int pk_marca )
    {

        String FIND__BY_CODIGO = "SELECT * FROM marca WHERE pk_marca = " + pk_marca;
        ResultSet result = conexao.executeQuery( FIND__BY_CODIGO );
        Marca marca = null;
        try
        {

            if ( result.next() )
            {
                marca = new Marca();
                setResultSetMarca( result, marca );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return marca;

    }

    public Marca getMarcaByDesignacao( String designacao )
    {

        String sql = "SELECT * FROM marca  WHERE designacao = '" + designacao + "'";
        ResultSet result = conexao.executeQuery( sql );
        Marca marca = null;
        try
        {
            if ( result.next() )
            {
                marca = new Marca();
                setResultSetMarca( result, marca );
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return marca;

    }

    private void setResultSetMarca( ResultSet rs, Marca marca )
    {
        try
        {

            marca.setPkMarca( rs.getInt( "pk_marca" ) );
            marca.setDesignacao( rs.getString( "designacao" ) );

        }
        catch ( SQLException e )
        {
        }

    }

}
