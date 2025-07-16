/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tesouraria.novo.controller;

import entity.TipoConta;
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
public class TipoContaController implements EntidadeFactory
{

    private BDConexao conexao;
    public TipoContaController( BDConexao conexao )
    {
        this.conexao = conexao;
    }

    @Override
    public boolean salvar( Object object )
    {
        TipoConta mesas = ( TipoConta ) object;
        String INSERT = "INSERT INTO tipo_conta( designacao "
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
    public boolean eliminar( int pk_tipo_conta )
    {
        String DELETE = "DELETE FROM tipo_conta WHERE pk_tipo_conta = " + pk_tipo_conta;
        return conexao.executeUpdate( DELETE );
    }

    @Override
    public List<TipoConta> listarTodos()
    {
        String FIND_ALL = "SELECT * FROM tipo_conta ORDER BY pk_tipo_conta ASC";
        ResultSet result = conexao.executeQuery( FIND_ALL );
        List<TipoConta> lista = new ArrayList<>();
        TipoConta tipoProduto;
        try
        {
            while ( result.next() )
            {
                tipoProduto = new TipoConta();
                setResultSetTipoConta( result, tipoProduto );
                lista.add( tipoProduto );
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return lista;
    }

    @Override
    public Vector<String> getVector()
    {
        String FIND_ALL = "SELECT designacao FROM tipo_conta ORDER BY designacao ASC";
        ResultSet result = conexao.executeQuery( FIND_ALL );
        Vector<String> lista = new Vector<>();
        try
        {
            while ( result.next() )
            {
                lista.add( result.getString( "designacao" ) );
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return lista;

    }

    @Override
    public Object findById( int pk_tipo_conta )
    {

        String FIND__BY_CODIGO = "SELECT * FROM tipo_conta WHERE pk_tipo_conta = " + pk_tipo_conta;
        ResultSet result = conexao.executeQuery( FIND__BY_CODIGO );
        TipoConta tipoProduto = null;
        try
        {
            if ( result.next() )
            {
                tipoProduto = new TipoConta();
                setResultSetTipoConta( result, tipoProduto );
            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return tipoProduto;

    }

    public TipoConta getTipoContaByDesignacao( String designacao )
    {
        String FIND_BY_CODIGO = "SELECT *  FROM tipo_conta  WHERE designacao = '" + designacao + "'";
        ResultSet result = conexao.executeQuery( FIND_BY_CODIGO );
        TipoConta tipoProduto = null;
        try
        {
            if ( result.next() )
            {
                tipoProduto = new TipoConta();
                setResultSetTipoConta( result, tipoProduto );
            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return tipoProduto;

    }

    private void setResultSetTipoConta( ResultSet rs, TipoConta tipoConta )
    {
        try
        {
            tipoConta.setPkTipoConta( rs.getInt( "pk_tipo_conta" ) );
            tipoConta.setDesignacao( rs.getString( "designacao" ) );
        }
        catch ( SQLException e )
        {
        }
    }

}
