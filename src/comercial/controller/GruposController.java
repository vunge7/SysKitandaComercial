/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comercial.controller;


import java.sql.Connection;
import entity.Grupo;
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
public class GruposController implements EntidadeFactory
{

    private BDConexao conexao;

    public GruposController( BDConexao conexao )
    {
        this.conexao = conexao;
    }

    @Override
    public boolean salvar( Object object )
    {
        Grupo mesas = (Grupo) object;
        String INSERT = "INSERT INTO grupo( designacao "
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
    public boolean eliminar( int pk_grupo )
    {
        String DELETE = "DELETE FROM grupo WHERE pk_grupo = " + pk_grupo;
        return conexao.executeUpdate( DELETE );
    }

    @Override
    public List<Grupo> listarTodos()
    {

        String FIND_ALL = "SELECT * FROM grupo ORDER BY pk_grupo ASC";
        ResultSet result = conexao.executeQuery( FIND_ALL );
        List<Grupo> lista_grupos = new ArrayList<>();
        Grupo grupo;
        try
        {
            while ( result.next() )
            {
                grupo = new Grupo();
                setResultSetGrupo( result, grupo );
                lista_grupos.add( grupo );
            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return lista_grupos;
    }

    @Override
    public Vector<String> getVector()
    {
        String FIND_ALL = "SELECT * FROM grupo ORDER BY pk_grupo ASC";
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
    public Object findById( int pk_grupo )
    {

        String FIND__BY_CODIGO = "SELECT * FROM grupo WHERE pk_grupo = " + pk_grupo;
        ResultSet result = conexao.executeQuery( FIND__BY_CODIGO );
        Grupo grupo = null;
        try
        {

            if ( result.next() )
            {
                grupo = new Grupo();
                setResultSetGrupo( result, grupo );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return grupo;

    }

    public Grupo getGrupoByDesignacao( String designacao )
    {

        String FIND__BY_CODIGO = "SELECT *  FROM grupo a WHERE a.designacao = '" + designacao + "'";
        ResultSet result = conexao.executeQuery( FIND__BY_CODIGO );
        Grupo grupo = null;
        try
        {

            if ( result.next() )
            {
                grupo = new Grupo();
                setResultSetGrupo( result, grupo );
            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        System.out.println( "CONTROLLER GRUPO: " + grupo.getPkGrupo() );
        return grupo;

    }

    public Vector<String> getVectorRefeicoes()
    {
        String FIND_ALL = "SELECT * FROM grupo WHERE pk_grupo = 3 ORDER BY pk_grupo ASC";
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

    public Vector<String> getVectorIngredientes()
    {
        String FIND_ALL = "SELECT * FROM grupo WHERE pk_grupo = 2 ORDER BY pk_grupo ASC";
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

    private void setResultSetGrupo( ResultSet rs, Grupo grupo )
    {
        try
        {
            grupo.setPkGrupo( rs.getInt( "pk_grupo" ) );
            grupo.setDesignacao( rs.getString( "designacao" ) );
        }
        catch ( SQLException e )
        {
        }
    }

}
