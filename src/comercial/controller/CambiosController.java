/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comercial.controller;


import java.sql.Connection;
import entity.Cambio;
import entity.Moeda;
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
public class CambiosController implements EntidadeFactory
{

    private BDConexao conexao;

    public CambiosController( BDConexao conexao )
    {
        this.conexao = conexao;
    }

    @Override
    public boolean salvar( Object object )
    {
        Cambio mesas = ( Cambio ) object;
        String INSERT = "INSERT INTO cambio( valor , data_hora, fk_moeda"
                + ")"
                + " VALUES("
                + mesas.getValor() + ", "
                + "'" + MetodosUtil.getDataBancoFull( mesas.getDataHora() ) + "', "
                + mesas.getFkMoeda().getPkMoeda()
                + " ) ";

        return conexao.executeUpdate( INSERT );

    }

    @Override
    public boolean actualizar( Object object )
    {
        return true;
    }

    @Override
    public boolean eliminar( int pk_cambio )
    {
        String DELETE = "DELETE FROM cambio WHERE pk_cambio = " + pk_cambio;
        return conexao.executeUpdate( DELETE );
    }

    @Override
    public List<Cambio> listarTodos()
    {

        String FIND_ALL = "SELECT * FROM cambio ORDER BY pk_cambio ASC";
        ResultSet result = conexao.executeQuery( FIND_ALL );
        List<Cambio> list = new ArrayList<>();
        Cambio cambio;
        try
        {
            while ( result.next() )
            {
                cambio = new Cambio();
                cambio.setValor( result.getDouble( "valor" ) );
                cambio.setDataHora( result.getDate( "data_hora" ) );
                cambio.setFkMoeda( new Moeda( result.getInt( "fk_moeda" ) ) );
                list.add( cambio );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public Vector<String> getVector()
    {

        Vector<String> vector = new Vector<>();

        return vector;

    }

    @Override
    public Object findById( int pk_cambio )
    {

        String FIND_BY_CODIGO = "SELECT * FROM cambio WHERE pk_cambio = " + pk_cambio;
        ResultSet result = conexao.executeQuery( FIND_BY_CODIGO );
        Cambio cambio = null;
        try
        {
            if ( result.next() )
            {
                cambio = new Cambio();
                cambio.setValor( result.getDouble( "valor" ) );
                cambio.setDataHora( result.getDate( "data_hora" ) );
                cambio.setFkMoeda( new Moeda( result.getInt( "fk_moeda" ) ) );
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return cambio;

    }

    public Cambio findByCodigo( int codigo )
    {

        String FIND_BY_NOME = "SELECT * FROM cambio WHERE pk_cambio = " + codigo;
        ResultSet result = conexao.executeQuery( FIND_BY_NOME );
        Cambio cambio = null;
        try
        {

            if ( result.next() )
            {
                cambio = new Cambio();
                cambio.setPkCambio( result.getInt( "pk_cambio" ) );
                cambio.setValor( result.getDouble( "valor" ) );
//                cambio.setDataHora( result.getDate( "data_hora" ) );
                cambio.setFkMoeda( new Moeda( result.getInt( "fk_moeda" ) ) );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return cambio;

    }

    public Cambio getLastObject( int fk_moeda )
    {
        Cambio cambio = ( Cambio ) findById( getLastId( fk_moeda ) );
        return cambio;

    }

    public int getLastId( int fk_moeda )
    {
        String FIND_BY_CODIGO = "SELECT MAX(pk_cambio) AS maximo_id FROM cambio WHERE fk_moeda = " + fk_moeda;
        ResultSet result = conexao.executeQuery( FIND_BY_CODIGO );
        try
        {
            if ( result.next() )
            {
                return result.getInt( "maximo_id" );

            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return 0;

    }

}
