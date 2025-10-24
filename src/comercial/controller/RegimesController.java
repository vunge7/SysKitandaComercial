/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comercial.controller;


import java.sql.Connection;
import entity.Regime;
import java.sql.PreparedStatement;
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

public class RegimesController implements EntidadeFactory
{

    private BDConexao conexao;

    public RegimesController( BDConexao conexao )
    {
        this.conexao = conexao;
    }

    @Override
    public boolean salvar( Object object )
    {
        Regime regime = ( Regime ) object;
        PreparedStatement stmt;
        String INSERT = "INSERT INTO regime( "
                + " designacao, valor "
                + ")"
                + " VALUES("
                + " ? , ?"
                + " ) ";

        try
        {

            stmt = BDConexao.getConnection().prepareStatement( INSERT );

            int cod = 1;
            stmt.setString( cod++, regime.getDesignacao() );
            stmt.setDouble( cod++, regime.getValor() );
            stmt.executeUpdate();
            stmt.close();
            return true;

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return false;

    }

    @Override
    public boolean actualizar( Object object )
    {
        Regime regime = ( Regime ) object;
        PreparedStatement stmt;
        String UPDATE = "UPDATE regime SET "
                + " designacao = ?, "
                + " valor = ?"
                + " WHERE pk_regime = ?";
        try
        {
            System.out.println( UPDATE );

            stmt = conexao.getConnection().prepareStatement( UPDATE );

            int cod = 1;
            stmt.setString( cod++, regime.getDesignacao() );
            stmt.setDouble( cod++, regime.getValor() );
            stmt.setInt( cod++, regime.getPkRegime() );
            stmt.executeUpdate();
            stmt.close();
            return true;

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return false;

    }

    @Override
    public boolean eliminar( int pk_regime )
    {
        String DELETE = "DELETE FROM regime WHERE pk_regime = " + pk_regime;
        return conexao.executeUpdate( DELETE );
    }

    @Override
    public List<Regime> listarTodos()
    {

        String FIND_ALL = "SELECT * FROM regime ORDER BY pk_regime ASC";
        ResultSet result = conexao.executeQuery( FIND_ALL );
        List<Regime> lista_regime = new ArrayList<>();
        Regime regime;
        try
        {
            while ( result.next() )
            {
                regime = new Regime();
                regime.setDesignacao( result.getString( "designacao" ) );
                regime.setValor(    result.getDouble("valor" ) );
                lista_regime.add( regime );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return lista_regime;
    }

    @Override
    public Vector<String> getVector()
    {
//        String sql = "SELECT designacao FROM moeda ORDER BY pk_moeda ASC";
        String sql = "SELECT designacao FROM regime";
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
        vector.add(0,"--Seleccione--");
        return vector;

    }

    @Override
    public Object findById( int pk_regime )
    {

        String FIND__BY_CODIGO = "SELECT * FROM regime WHERE pk_regime = " + pk_regime;
        ResultSet result = conexao.executeQuery( FIND__BY_CODIGO );
        Regime regime = null;
        try
        {

            if ( result.next() )
            {
                regime = new Regime();
                regime.setPkRegime(result.getInt( "pk_regime" ) );
                regime.setDesignacao( result.getString( "designacao" ) );
                regime.setValor(        result.getDouble("valor" ) );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return regime;

    }

    public Regime getRegimeByDesignacao( String designacao )
    {
        String FIND__BY_CODIGO = "SELECT *  FROM regime a WHERE a.designacao = '" + designacao + "'";
        ResultSet result = conexao.executeQuery( FIND__BY_CODIGO );
        Regime regime = null;
        try
        {

            if ( result.next() )
            {
                regime = new Regime();
                regime.setPkRegime(result.getInt( "pk_regime" ) );
                regime.setDesignacao( result.getString( "designacao" ) );
                regime.setValor(        result.getDouble("valor" ) );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return regime;

    }

}
