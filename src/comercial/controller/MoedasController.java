/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comercial.controller;

import entity.Moeda;
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

public class MoedasController implements EntidadeFactory
{

    private BDConexao conexao;

    public MoedasController( BDConexao conexao )
    {
        this.conexao = conexao;
    }

    @Override
    public boolean salvar( Object object )
    {
        Moeda mesas = ( Moeda ) object;
        String INSERT = "INSERT INTO moeda( designacao "
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
    public boolean eliminar( int pk_moeda )
    {
        String DELETE = "DELETE FROM moeda WHERE pk_moeda = " + pk_moeda;
        return conexao.executeUpdate( DELETE );
    }

    @Override
    public List<Moeda> listarTodos()
    {

        String FIND_ALL = "SELECT * FROM moeda ORDER BY pk_moeda ASC";
        ResultSet result = conexao.executeQuery( FIND_ALL );
        List<Moeda> lista_moedas = new ArrayList<>();
        Moeda moeda;
        try
        {
            while ( result.next() )
            {
                moeda = new Moeda();
                moeda.setDesignacao( result.getString( "designacao" ) );
                moeda.setAbreviacao( result.getString( "abreviacao" ) );
                lista_moedas.add( moeda );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return lista_moedas;
    }

    @Override
    public Vector<String> getVector()
    {
        String sql = "SELECT designacao FROM moeda ORDER BY pk_moeda ASC";
        ResultSet result = conexao.executeQuery( sql );
        Vector<String> vector = new Vector<>();
        try
        {
            while ( result.next() )
            {
                vector.add( result.getString( "designacao" ) );
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        System.out.println( "Vector::::: " +vector.toString() );
        return vector;

    }

    @Override
    public Object findById( int pk_moeda )
    {

        String FIND__BY_CODIGO = "SELECT * FROM moeda WHERE pk_moeda = " + pk_moeda;
        ResultSet result = conexao.executeQuery( FIND__BY_CODIGO );
        Moeda moeda = null;
        try
        {

            if ( result.next() )
            {
                moeda = new Moeda();
                moeda.setPkMoeda( result.getInt( "pk_moeda" ) );
                moeda.setDesignacao( result.getString( "designacao" ) );
                moeda.setAbreviacao( result.getString( "abreviacao" ) );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return moeda;

    }

    public Moeda getMoedaByDesignacao( String designacao )
    {
        System.out.println( "MOEDA CONTROLLER: " +designacao );

        String FIND__BY_CODIGO = "SELECT *  FROM moeda a WHERE a.designacao = '" + designacao + "'";
        ResultSet result = conexao.executeQuery( FIND__BY_CODIGO );
        Moeda moeda = null;
        try
        {

            if ( result.next() )
            {
                moeda = new Moeda();
                moeda.setPkMoeda( result.getInt( "pk_moeda" ) );
                moeda.setDesignacao( result.getString( "designacao" ) );
                moeda.setAbreviacao( result.getString( "abreviacao" ) );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return moeda;

    }

}
