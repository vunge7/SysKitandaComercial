/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comercial.controller;

import entity.Imposto;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import util.BDConexao;
import util.MetodosUtil;

/**
 *
 * @author Domingos Dala Vunge
 */
public class ImpostosController implements EntidadeFactory
{

    private BDConexao conexao;

    public ImpostosController( BDConexao conexao )
    {
        this.conexao = conexao;
    }

    @Override
    public boolean salvar( Object object )
    {
        Imposto imposto = ( Imposto ) object;
        String INSERT = "INSERT INTO imposto( descricao , taxa, data_hora"
                + ")"
                + " VALUES("
                + "'" + imposto.getDescricao() + "', "
                + imposto.getTaxa() + ", "
                + "'" + MetodosUtil.getDataBancoFull( imposto.getDataHora() ) + "'"
                + " ) ";

        return conexao.executeUpdate( INSERT );

    }

    @Override
    public boolean actualizar( Object object )
    {
        return true;
    }

    @Override
    public boolean eliminar( int pk_imposto )
    {
        String DELETE = "DELETE FROM imposto WHERE pk_imposto = " + pk_imposto;
        return conexao.executeUpdate( DELETE );
    }

    @Override
    public List<Imposto> listarTodos()
    {

        String FIND_ALL = "SELECT * FROM imposto ORDER BY pk_imposto ASC";
        ResultSet result = conexao.executeQuery( FIND_ALL );
        List<Imposto> lista_impostos = new ArrayList<>();
        Imposto imposto;
        try
        {
            while ( result.next() )
            {
                imposto = new Imposto();
                setResultSetImposto( result, imposto );
                lista_impostos.add( imposto );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return lista_impostos;
    }

    @Override
    public Vector<String> getVector()
    {
        String sql = "SELECT taxa FROM imposto ORDER BY taxa ASC";
        ResultSet result = conexao.executeQuery( sql );
        Vector<String> lista_imposto = new Vector<>();
        try
        {
            while ( result.next() )
            {
                lista_imposto.add( String.valueOf( result.getDouble( "taxa" ) ) );
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        lista_imposto.add(0, "--SELECIONE--" );
        return lista_imposto;

    }
    
        public Vector<String> getVector1()
    {
        String FIND_ALL = "SELECT * FROM familia ORDER BY pk_familia ASC";
        ResultSet result = conexao.executeQuery( FIND_ALL );
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
        vector.add( 0, "-- Seleccione --" );
        return vector;

    }


    @Override
    public Object findById( int pk_imposto )
    {

        String FIND__BY_CODIGO = "SELECT * FROM imposto WHERE pk_imposto = " + pk_imposto;
        ResultSet result = conexao.executeQuery( FIND__BY_CODIGO );
        Imposto imposto = null;
        try
        {
            if ( result.next() )
            {
                imposto = new Imposto();
                setResultSetImposto( result, imposto );
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return imposto;

    }
    
    public Imposto getImpostoByTaxa(double taxa) {
    System.out.println("Procurando imposto pela taxa: " + taxa);
    Imposto imposto = null;

    String sql = "SELECT * FROM imposto WHERE ABS(taxa - ?) < 0.000001"; // tolerância numérica

    try (PreparedStatement ps = conexao.getConnection().prepareStatement(sql)) {
        ps.setDouble(1, taxa);

        try (ResultSet result = ps.executeQuery()) {
            if (result.next()) {
                imposto = new Imposto();
                setResultSetImposto(result, imposto); // mesmo método que já usas
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return imposto;
}


    public Imposto getImpostoByDesignacao( String taxa )
    {

        System.out.println( "TAXA IMPOSTO : " + taxa );
        String sql = "SELECT * FROM imposto  WHERE taxa = " + taxa;
        ResultSet result = conexao.executeQuery( sql );
        Imposto imposto = null;
        try
        {
            if ( result.next() )
            {
                imposto = new Imposto();
                setResultSetImposto( result, imposto );
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return imposto;

    }

    private void setResultSetImposto( ResultSet rs, Imposto imposto )
    {
        try
        {

            imposto.setPkImposto( rs.getInt( "pk_imposto" ) );
            imposto.setDescricao( rs.getString( "descricao" ) );
            imposto.setTaxa( rs.getDouble( "taxa" ) );
            imposto.setDataHora( rs.getDate( "data_hora" ) );

        }
        catch ( SQLException e )
        {
        }
    }

}
