/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comercial.controller;


import java.sql.Connection;
import entity.Unidade;
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
public class UnidadesController implements EntidadeFactory
{

    private BDConexao conexao;

    public UnidadesController( BDConexao conexao )
    {
        this.conexao = conexao;
    }

    @Override
    public boolean salvar( Object object )
    {
        Unidade mesas = ( Unidade ) object;
        String INSERT = "INSERT INTO unidade( descricao "
                + ")"
                + " VALUES("
                + "'" + mesas.getDescricao() + "'"
                + " ) ";

        return conexao.executeUpdate( INSERT );

    }

    @Override
    public boolean actualizar( Object object )
    {
        return true;
    }

    @Override
    public boolean eliminar( int pk_unidade )
    {
        String DELETE = "DELETE FROM unidade WHERE pk_unidade = " + pk_unidade;
        return conexao.executeUpdate( DELETE );
    }

    @Override
    public List<Unidade> listarTodos()
    {

        String FIND_ALL = "SELECT * FROM unidade ORDER BY pk_unidade ASC";
        ResultSet result = conexao.executeQuery( FIND_ALL );
        List<Unidade> lista_unidades = new ArrayList<>();
        Unidade unidade;
        try
        {
            while ( result.next() )
            {
                unidade = new Unidade();
                unidade.setDescricao( result.getString( "descricao" ) );
                unidade.setAbreviacao( result.getString( "abreviacao" ) );
                lista_unidades.add( unidade );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return lista_unidades;
    }

    @Override
    public Vector<String> getVector()
    {
        String FIND_ALL = "SELECT * FROM unidade ORDER BY pk_unidade ASC";
        ResultSet result = conexao.executeQuery( FIND_ALL );
        Vector<String> lista_mesas = new Vector<>();
        try
        {
            while ( result.next() )
            {
                lista_mesas.add( result.getString( "descricao" ) );
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return lista_mesas;

    }

    @Override
    public Object findById( int pk_unidade )
    {

        String FIND__BY_CODIGO = "SELECT * FROM unidade WHERE pk_unidade = " + pk_unidade;
        ResultSet result = conexao.executeQuery( FIND__BY_CODIGO );
        Unidade unidade = null;
        try
        {

            if ( result.next() )
            {
                unidade = new Unidade();
                unidade.setPkUnidade( result.getInt( "pk_unidade" ) );
                unidade.setDescricao( result.getString( "descricao" ) );
                unidade.setAbreviacao( result.getString( "abreviacao" ) );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return unidade;

    }

    public Unidade getUnidadeByDesignacao( String descricao )
    {

        String sql = "SELECT *  FROM unidade a WHERE a.descricao = '" + descricao + "'";
        ResultSet result = conexao.executeQuery( sql );
        Unidade unidade = null;
        try
        {

            if ( result.next() )
            {
                unidade = new Unidade();
                unidade.setPkUnidade( result.getInt( "pk_unidade" ) );
                unidade.setDescricao( result.getString( "descricao" ) );
                unidade.setAbreviacao( result.getString( "abreviacao" ) );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        System.out.println( "COD_UNIDADE: " +unidade.getPkUnidade() );
        return unidade;

    }

}
