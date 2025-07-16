/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comercial.controller;

import entity.AnoEconomico;
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
public class AnoEconomicoController implements EntidadeFactory
{

    private BDConexao conexao;

    public AnoEconomicoController( BDConexao conexao )
    {
        this.conexao = conexao;
    }

    @Override
    public boolean salvar( Object object )
    {
        AnoEconomico ano_economico = ( AnoEconomico ) object;
        String INSERT = "INSERT INTO ano_economico( designacao , serie , data_inicio , data_fim "
                + ")"
                + " VALUES("
                + "'" + ano_economico.getDesignacao() + "' , "
                + "'" + ano_economico.getSerie() + "' , "
                + "'" + MetodosUtil.getDataBanco( ano_economico.getDataInicio() ) + "' , "
                + "'" + MetodosUtil.getDataBanco( ano_economico.getDataFim() ) + "' , "
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
        String DELETE = "DELETE FROM ano_economico WHERE pk_ano_economico = " + codigo;
        return conexao.executeUpdate( DELETE );
    }

    @Override
    public List<AnoEconomico> listarTodos()
    {

        String FIND_ALL = "SELECT * FROM ano_economico ORDER BY pk_ano_economico ASC";
        ResultSet result = conexao.executeQuery( FIND_ALL );
        List<AnoEconomico> lista_ano_economico = new ArrayList<>();
        AnoEconomico ano_economico;
        try
        {

            while ( result.next() )
            {
                ano_economico = new AnoEconomico();
                ano_economico.setDesignacao( result.getString( "designacao" ) );
                ano_economico.setSerie( result.getString( "serie" ) );
                ano_economico.setDataInicio( result.getDate( "data_inicio" ) );
                ano_economico.setDataFim( result.getDate( "data_fim" ) );

                lista_ano_economico.add( ano_economico );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return lista_ano_economico;
    }

    @Override
    public Vector<String> getVector()
    {

        String FIND_ALL = "SELECT designacao FROM ano_economico ORDER BY pk_ano_economico DESC";
        ResultSet result = conexao.executeQuery( FIND_ALL );
        Vector<String> list = new Vector<>();
        try
        {
            while ( result.next() )
            {
                list.add( result.getString( "designacao" ) );
            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return list;

    }

    @Override
    public Object findById( int codigo )
    {

        String FIND__BY_CODIGO = "SELECT * FROM ano_economico WHERE pk_ano_economico = " + codigo;
        ResultSet result = conexao.executeQuery( FIND__BY_CODIGO );
        AnoEconomico ano_economico = null;
        try
        {

            if ( result.next() )
            {
                ano_economico = new AnoEconomico();
                ano_economico.setPkAnoEconomico( result.getInt( "pk_ano_economico" ) );
                ano_economico.setDesignacao( result.getString( "designacao" ) );
                ano_economico.setSerie( result.getString( "serie" ) );
                ano_economico.setDataInicio( result.getDate( "data_inicio" ) );
                ano_economico.setDataFim( result.getDate( "data_fim" ) );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return ano_economico;

    }

    public AnoEconomico getLastObject()
    {

        String FIND__BY_CODIGO = "SELECT MAX(pk_ano_economico) as maximo_id, a.*  FROM ano_economico a";
        ResultSet result = conexao.executeQuery( FIND__BY_CODIGO );
        AnoEconomico ano_economico = null;
        try
        {

            if ( result.next() )
            {
                ano_economico = new AnoEconomico();
                ano_economico.setPkAnoEconomico( result.getInt( "maximo_id" ) );
                ano_economico.setDesignacao( result.getString( "designacao" ) );
                ano_economico.setSerie( result.getString( "serie" ) );
                ano_economico.setDataInicio( result.getDate( "data_inicio" ) );
                ano_economico.setDataFim( result.getDate( "data_fim" ) );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return ano_economico;

    }

    public AnoEconomico getAnoEconomicoByDesignacao( String designacao )
    {
        String FIND__BY_CODIGO = "SELECT *  FROM ano_economico a WHERE a.designacao = '" + designacao + "'";
        ResultSet result = conexao.executeQuery( FIND__BY_CODIGO );
        AnoEconomico anoEconomico = null;
        try
        {

            if ( result.next() )
            {
                anoEconomico = new AnoEconomico();
                anoEconomico.setPkAnoEconomico( result.getInt( "pk_ano_economico" ) );
                anoEconomico.setDesignacao( result.getString( "designacao" ) );
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return anoEconomico;

    }

    public Object findByName( String designacao )
    {

        String FIND_BY_CODIGO = "SELECT * FROM ano_economico WHERE designacao = '" + designacao + "' ";
        ResultSet result = conexao.executeQuery( FIND_BY_CODIGO );
        AnoEconomico ano_economico = null;
        try
        {

            if ( result.next() )
            {
                ano_economico = new AnoEconomico();
                ano_economico.setPkAnoEconomico( result.getInt( "pk_ano_economico" ) );
                ano_economico.setDesignacao( result.getString( "designacao" ) );
                ano_economico.setSerie( result.getString( "serie" ) );
                ano_economico.setDataInicio( result.getDate( "data_inicio" ) );
                ano_economico.setDataFim( result.getDate( "data_fim" ) );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return ano_economico;

    }

}
