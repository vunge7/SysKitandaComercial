/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comercial.controller;


import java.sql.Connection;
import entity.Retencao;
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
public class RetencaoController implements EntidadeFactory
{

    private BDConexao conexao;

    public RetencaoController( BDConexao conexao )
    {
        this.conexao = conexao;
    }

    @Override
    public boolean salvar( Object object )
    {
        Retencao retencao = ( Retencao ) object;
        String INSERT = "INSERT INTO retencao( descricao , taxa, data_hora"
                + ")"
                + " VALUES("
                + "'" + retencao.getDescricao() + "', "
                + retencao.getTaxa() + ", "
                + "'" + MetodosUtil.getDataBancoFull( retencao.getDataHora() ) + "'"
                + " ) ";

        return conexao.executeUpdate( INSERT );

    }

    @Override
    public boolean actualizar( Object object )
    {
        return true;
    }

    @Override
    public boolean eliminar( int pk_retencao )
    {
        String DELETE = "DELETE FROM retencao WHERE pk_retencao = " + pk_retencao;
        return conexao.executeUpdate( DELETE );
    }
  
    @Override
    public List<Retencao> listarTodos()
    {

        String FIND_ALL = "SELECT * FROM retencao ORDER BY pk_retencao ASC";
        ResultSet result = conexao.executeQuery( FIND_ALL );
        List<Retencao> lista_retencaos = new ArrayList<>();
        Retencao retencao;
        try
        {
            while ( result.next() )
            {
                retencao = new Retencao();
                setResultSetRetencao( result, retencao );
                lista_retencaos.add( retencao );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return lista_retencaos;
    }

    @Override
    public Vector<String> getVector()
    {
        String FIND_ALL = "SELECT * FROM retencao ORDER BY pk_retencao ASC";
        ResultSet result = conexao.executeQuery( FIND_ALL );
        Vector<String> lista_retencao = new Vector<>();
        try
        {
            while ( result.next() )
            {
                lista_retencao.add( result.getString( "descricao" ) );
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return lista_retencao;

    }

    @Override
    public Object findById( int pk_retencao )
    {

        String FIND__BY_CODIGO = "SELECT * FROM retencao WHERE pk_retencao = " + pk_retencao;
        ResultSet result = conexao.executeQuery( FIND__BY_CODIGO );
        Retencao retencao = null;
        try
        {
            if ( result.next() )
            {
                retencao = new Retencao();
                setResultSetRetencao( result, retencao );
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return retencao;

    }

    public Retencao getRetencaoByDesignacao( String taxa )
    {

        System.out.println( "TAXA IMPOSTO : " + taxa );
        String sql = "SELECT *  FROM retencao  WHERE taxa = " + taxa;
        ResultSet result = conexao.executeQuery( sql );
        Retencao retencao = null;
        try
        {
            if ( result.next() )
            {
                retencao = new Retencao();
                setResultSetRetencao( result, retencao );
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return retencao;

    }

    private void setResultSetRetencao( ResultSet rs, Retencao retencao )
    {
        try
        {

            retencao.setPkRetencao( rs.getInt( "pk_retencao" ) );
            retencao.setDescricao( rs.getString( "descricao" ) );
            retencao.setTaxa( rs.getDouble( "taxa" ) );
            retencao.setDataHora( rs.getDate( "data_hora" ) );

        }
        catch ( SQLException e )
        {
        }
    }
    
   

}
