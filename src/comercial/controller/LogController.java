/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comercial.controller;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import modelo.Log;
import util.BDConexao;

/**
 *
 * @author Domingos Dala Vunge
 */
public class LogController implements EntidadeFactory
{

    private BDConexao conexao;

    public LogController( BDConexao conexao )
    {
        this.conexao = conexao;
    }

    @Override
    public boolean salvar( Object object )
    {
        Log log = ( Log ) object;
        String INSERT = "INSERT INTO log( ip_maquina, nome_maquina, estado "
                + ")"
                + " VALUES("
                + "'" + log.getIpMaquina() + "' , "
                + "'" + log.getNomeMaquina() + "',  "
                + "'" + log.getEstado() + "' "
                + " ) ";

        return conexao.executeUpdate( INSERT );

    }

    @Override
    public boolean actualizar( Object object )
    {
        Log log = ( Log ) object;
        String update = "UPDATE log SET estado = '" + log.getEstado() + "' WHERE pk_log = " + log.getPkLog();
        return conexao.executeUpdate( update );
    }

    @Override
    public boolean eliminar( int pk_log )
    {
        String DELETE = "DELETE FROM log WHERE pk_log = " + pk_log;
        return conexao.executeUpdate( DELETE );
    }

    public boolean eliminarByIpMaquina( String ipMaquina )
    {
        String DELETE = "DELETE FROM log WHERE ip_maquina = '" + ipMaquina + "'";
        return conexao.executeUpdate( DELETE );
    }

    @Override
    public List<Log> listarTodos()
    {
        return null;
    }

    @Override
    public Vector<String> getVector()
    {
        return null;

    }

    @Override
    public Object findById( int pk_log )
    {

        String sql = "SELECT * FROM log WHERE pk_log = " + pk_log;
        ResultSet result = conexao.executeQuery( sql );
        Log log = null;
        try
        {
            if ( result.next() )
            {
                log = new Log();
                setResultSetGrupo( result, log );
            }
        }
        catch ( SQLException e )
        {
        }

        return log;

    }

    public Log getLogByNomeMaquina( String nomeMaquina )
    {
        String FIND__BY_CODIGO = "SELECT *  FROM log a WHERE a.nome_maquina = '" + nomeMaquina + "'";
        ResultSet result = conexao.executeQuery( FIND__BY_CODIGO );
        Log log = null;
        try
        {
            if ( result.next() )
            {
                log = new Log();
                setResultSetGrupo( result, log );
            }
        }
        catch ( SQLException e )
        {
        }
        return log;

    }

    public boolean existe( String nomeMaquina )
    {
        String FIND__BY_CODIGO = "SELECT *  FROM log a WHERE a.nome_maquina = '" + nomeMaquina + "'";
        ResultSet result = conexao.executeQuery( FIND__BY_CODIGO );
        try
        {
            if ( result.next() )
            {
                return true;
            }
        }
        catch ( SQLException e )
        {
        }
        return false;

    }

    public Log getLogById( int pk_log )
    {

        String FIND_BY_COD_BARRA = "SELECT * FROM log WHERE pk_log = " + pk_log;
        ResultSet result = conexao.executeQuery( FIND_BY_COD_BARRA );
        Log log = null;

        try
        {
            if ( result.next() )
            {
                log = new Log();
                setResultSetGrupo( result, log );
            }
        }
        catch ( SQLException e )
        {
        }
        return log;

    }

    private void setResultSetGrupo( ResultSet rs, Log log )
    {
        try
        {
            log.setPkLog( rs.getInt( "pk_log" ) );
            log.setIpMaquina( rs.getString( "ip_maquina" ) );
            log.setNomeMaquina( rs.getString( "nome_maquina" ) );
            log.setEstado( rs.getString( "estado" ) );
        }
        catch ( SQLException e )
        {
        }

    }

}
