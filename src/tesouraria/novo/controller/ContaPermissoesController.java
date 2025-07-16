/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tesouraria.novo.controller;

import entity.ContaPermissoes;
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
public class ContaPermissoesController implements EntidadeFactory
{

    private BDConexao conexao;

    public ContaPermissoesController( BDConexao conexao )
    {
        this.conexao = conexao;
    }

    @Override
    public boolean salvar( Object object )
    {

        ContaPermissoes contaPermissao = ( ContaPermissoes ) object;
        String INSERT = "INSERT INTO conta_permissoes( cod_usuario, cod_conta, entrada, saida, transferencia,vis_entrato, anulacao  "
                + ")"
                + " VALUES("
                + contaPermissao.getCodUsuario() + ", "
                + contaPermissao.getCodConta() + ", "
                + contaPermissao.getEntrada() + ", "
                + contaPermissao.getSaida() + ", "
                + contaPermissao.getTransferencia() + ", "
                + contaPermissao.getVisEntrato() + ", "
                + contaPermissao.getAnulacao()
                + " ) ";

        return conexao.executeUpdate( INSERT );

    }

    @Override
    public boolean actualizar( Object object )
    {
        ContaPermissoes contaPermissao = ( ContaPermissoes ) object;
        String UPDATE = "UPDATE conta_permissoes SET cod_usuario = " + contaPermissao.getCodUsuario() + ", "
                + "cod_conta = " + contaPermissao.getCodConta() + ", "
                + "entrada = " + contaPermissao.getEntrada() + ", "
                + "saida = " + contaPermissao.getSaida() + ", "
                + "transferencia = " + contaPermissao.getTransferencia() + ", "
                + "vis_entrato = " + contaPermissao.getVisEntrato() + ", "
                + "anulacao = " + contaPermissao.getAnulacao()
                + " WHERE pk_conta_permissao = " + contaPermissao.getPkContaPermissao();

        return conexao.executeUpdate( UPDATE );
    }

    @Override
    public boolean eliminar( int pk_conta_permissao )
    {
        String DELETE = "DELETE FROM conta_permissoes WHERE pk_conta_permissao = " + pk_conta_permissao;
        return conexao.executeUpdate( DELETE );
    }

    @Override
    public List<ContaPermissoes> listarTodos()
    {
        String FIND_ALL = "SELECT * FROM conta_permissoes ORDER BY pk_conta_permissao ASC";
        ResultSet result = conexao.executeQuery( FIND_ALL );
        List<ContaPermissoes> lista = new ArrayList<>();
        ContaPermissoes contaPermissoes;
        try
        {
            while ( result.next() )
            {
                contaPermissoes = new ContaPermissoes();
                setResultSetContaPermissoes( result, contaPermissoes );
                lista.add( contaPermissoes );
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return lista;
    }

    public List<ContaPermissoes> listarTodosByIdCodUSuario( int codUsuario )
    {
        String FIND_ALL = "SELECT * FROM conta_permissoes WHERE cod_usuario = " + codUsuario;
        ResultSet result = conexao.executeQuery( FIND_ALL );
        List<ContaPermissoes> lista = new ArrayList<>();
        ContaPermissoes contaPermissoes;
        try
        {
            while ( result.next() )
            {
                contaPermissoes = new ContaPermissoes();
                setResultSetContaPermissoes( result, contaPermissoes );
                lista.add( contaPermissoes );
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
        String FIND_ALL = "SELECT designacao FROM conta_permissoes ORDER BY designacao ASC";
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
    public Object findById( int pk_conta_permissao )
    {

        String FIND__BY_CODIGO = "SELECT * FROM conta_permissoes WHERE pk_conta_permissao = " + pk_conta_permissao;
        ResultSet result = conexao.executeQuery( FIND__BY_CODIGO );
        ContaPermissoes tipoProduto = null;
        try
        {
            if ( result.next() )
            {
                tipoProduto = new ContaPermissoes();
                setResultSetContaPermissoes( result, tipoProduto );
            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return tipoProduto;

    }

    public ContaPermissoes getById( long pk_conta_permissao )
    {

        String FIND__BY_CODIGO = "SELECT * FROM conta_permissoes WHERE pk_conta_permissao = " + pk_conta_permissao;
        ResultSet result = conexao.executeQuery( FIND__BY_CODIGO );
        ContaPermissoes tipoProduto = null;
        try
        {
            if ( result.next() )
            {
                tipoProduto = new ContaPermissoes();
                setResultSetContaPermissoes( result, tipoProduto );
            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return tipoProduto;

    }

    public ContaPermissoes getByIdUserAndIdConta( int idUser, int idConta )
    {

        String FIND__BY_CODIGO = "SELECT * FROM conta_permissoes WHERE cod_usuario = " + idUser + " AND cod_conta = " + idConta;
        ResultSet result = conexao.executeQuery( FIND__BY_CODIGO );
        ContaPermissoes contaPermissao = null;
        try
        {
            if ( result.next() )
            {
                contaPermissao = new ContaPermissoes();
                setResultSetContaPermissoes( result, contaPermissao );
            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return contaPermissao;

    }

    public boolean existe( int idUser, int idConta )
    {
        String FIND__BY_CODIGO = "SELECT * FROM conta_permissoes WHERE cod_usuario = " + idUser + " AND cod_conta = " + idConta;
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
            e.printStackTrace();
        }
        return false;

    }

    public ContaPermissoes getOperacao( long pk_conta_permissao )
    {

        String FIND__BY_CODIGO = "SELECT * FROM conta_permissoes WHERE pk_conta_permissao = " + pk_conta_permissao;
        ResultSet result = conexao.executeQuery( FIND__BY_CODIGO );
        ContaPermissoes tipoProduto = null;
        try
        {
            if ( result.next() )
            {
                tipoProduto = new ContaPermissoes();
                setResultSetContaPermissoes( result, tipoProduto );
            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return tipoProduto;

    }

    public ContaPermissoes getContaPermissoesByDesignacao( String designacao )
    {
        String FIND_BY_CODIGO = "SELECT *  FROM conta_permissoes  WHERE designacao = '" + designacao + "'";
        ResultSet result = conexao.executeQuery( FIND_BY_CODIGO );
        ContaPermissoes tipoProduto = null;
        try
        {
            if ( result.next() )
            {
                tipoProduto = new ContaPermissoes();
                setResultSetContaPermissoes( result, tipoProduto );
            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return tipoProduto;

    }

    private void setResultSetContaPermissoes( ResultSet rs, ContaPermissoes tipoConta )
    {
        try
        {
            tipoConta.setPkContaPermissao( rs.getLong( "pk_conta_permissao" ) );
            tipoConta.setCodUsuario( rs.getInt( "cod_usuario" ) );
            tipoConta.setCodConta( rs.getInt( "cod_conta" ) );
            tipoConta.setEntrada( rs.getBoolean( "entrada" ) );
            tipoConta.setSaida( rs.getBoolean( "saida" ) );
            tipoConta.setTransferencia( rs.getBoolean( "transferencia" ) );
            tipoConta.setVisEntrato( rs.getBoolean( "vis_entrato" ) );
            tipoConta.setAnulacao( rs.getBoolean( "anulacao" ) );
        }
        catch ( SQLException e )
        {
        }
    }

    public long getLastID()
    {
        String query = "SELECT MAX(pk_conta_permissao) AS last_id FROM conta_permissoes";
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

}
