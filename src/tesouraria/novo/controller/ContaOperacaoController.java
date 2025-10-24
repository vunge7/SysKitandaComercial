/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tesouraria.novo.controller;


import java.sql.Connection;
import entity.ContaOperacoes;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import util.BDConexao;
import util.MetodosUtil;

/**
 *
 * @author Domingos Dala Vunge
 */
public class ContaOperacaoController implements EntidadeFactory
{

    private BDConexao conexao;

    public ContaOperacaoController( BDConexao conexao )
    {
        this.conexao = conexao;
    }

    @Override
    public boolean salvar( Object object )
    {

        ContaOperacoes contaOperacao = ( ContaOperacoes ) object;
        String INSERT = "INSERT INTO conta_operacoes( data_hora, tipo,  user_id, user_name,beneficiario,  valor "
                + ")"
                + " VALUES("
                + "'" + MetodosUtil.getDataBancoFull( contaOperacao.getDataHora() ) + "', "
                + "'" + contaOperacao.getTipo() + "', "
                + contaOperacao.getUserId() + ", "
                + "'" + contaOperacao.getUserName() + "', "
                + "'" + contaOperacao.getBeneficiario() + "', "
                + contaOperacao.getValor()
                + " ) ";

        return conexao.executeUpdate( INSERT );

    }

    @Override
    public boolean actualizar( Object object )
    {
        return true;
    }

    @Override
    public boolean eliminar( int pk_conta_operacao )
    {
        String DELETE = "DELETE FROM conta_operacoes WHERE pk_conta_operacao = " + pk_conta_operacao;
        return conexao.executeUpdate( DELETE );
    }

    @Override
    public List<ContaOperacoes> listarTodos()
    {
        String FIND_ALL = "SELECT * FROM conta_operacoes ORDER BY pk_conta_operacao ASC";
        ResultSet result = conexao.executeQuery( FIND_ALL );
        List<ContaOperacoes> lista = new ArrayList<>();
        ContaOperacoes tipoProduto;
        try
        {
            while ( result.next() )
            {
                tipoProduto = new ContaOperacoes();
                setResultSetContaOperacoes( result, tipoProduto );
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
        String FIND_ALL = "SELECT designacao FROM conta_operacoes ORDER BY designacao ASC";
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
    public Object findById( int pk_conta_operacao )
    {

        String FIND__BY_CODIGO = "SELECT * FROM conta_operacoes WHERE pk_conta_operacao = " + pk_conta_operacao;
        ResultSet result = conexao.executeQuery( FIND__BY_CODIGO );
        ContaOperacoes tipoProduto = null;
        try
        {
            if ( result.next() )
            {
                tipoProduto = new ContaOperacoes();
                setResultSetContaOperacoes( result, tipoProduto );
            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return tipoProduto;

    }

    public ContaOperacoes getOperacao( long pk_conta_operacao )
    {

        String FIND__BY_CODIGO = "SELECT * FROM conta_operacoes WHERE pk_conta_operacao = " + pk_conta_operacao;
        ResultSet result = conexao.executeQuery( FIND__BY_CODIGO );
        ContaOperacoes tipoProduto = null;
        try
        {
            if ( result.next() )
            {
                tipoProduto = new ContaOperacoes();
                setResultSetContaOperacoes( result, tipoProduto );
            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return tipoProduto;

    }

    public ContaOperacoes getContaOperacoesByDesignacao( String designacao )
    {
        String FIND_BY_CODIGO = "SELECT *  FROM conta_operacoes  WHERE designacao = '" + designacao + "'";
        ResultSet result = conexao.executeQuery( FIND_BY_CODIGO );
        ContaOperacoes tipoProduto = null;
        try
        {
            if ( result.next() )
            {
                tipoProduto = new ContaOperacoes();
                setResultSetContaOperacoes( result, tipoProduto );
            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return tipoProduto;

    }

    private void setResultSetContaOperacoes( ResultSet rs, ContaOperacoes tipoConta )
    {
        try
        {
            tipoConta.setPkContaOperacao( rs.getLong( "pk_conta_operacao" ) );
            tipoConta.setDataHora( rs.getDate( "data_hora" ) );
            tipoConta.setTipo( rs.getString( "tipo" ) );
            tipoConta.setUserId( rs.getInt( "user_id" ) );
            tipoConta.setUserName( rs.getString( "user_name" ) );
            tipoConta.setValor( rs.getBigDecimal( "valor" ) );
        }
        catch ( SQLException e )
        {
        }
    }

    public long getLastID()
    {
        String query = "SELECT MAX(pk_conta_operacao) AS last_id FROM conta_operacoes";
        ResultSet resultSet = conexao.executeQuery( query );
        try
        {
            if ( resultSet.next() )
            {
                Integer last_id = resultSet.getInt( "last_id" );
                return last_id;
            }
        }
        catch ( SQLException ex )
        {
            ex.printStackTrace();
        }

        return 0;

    }

    public long insertContaOperacao( String tipo, Date data, int userId, String userName, String beneficiario, BigDecimal valor )
    {
        ContaOperacoes contaOperacoes = new ContaOperacoes();
        contaOperacoes.setDataHora( data );
        contaOperacoes.setTipo( tipo );
        contaOperacoes.setUserId( userId );
        contaOperacoes.setUserName( userName );
        contaOperacoes.setBeneficiario( beneficiario );
        contaOperacoes.setValor( valor );
        long ID = 0;
        if ( salvar( contaOperacoes ) )
        {
            ID = getLastID();
        }
        return ID;
    }

}
