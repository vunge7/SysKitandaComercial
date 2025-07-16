/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tesouraria.novo.controller;

import entity.Contas;
import java.math.BigDecimal;
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
public class ContaController implements EntidadeFactory
{

    private BDConexao conexao;

    public ContaController( BDConexao conexao )
    {
        this.conexao = conexao;
    }

    @Override
    public boolean salvar( Object object )
    {
        Contas contas = ( Contas ) object;
        String INSERT = "INSERT INTO contas( designacao, numero, iban, titular_1,"
                + " titular_2, saldo, objecto, data_criacao,  user_id, tipo_conta_id "
                + ")"
                + " VALUES("
                + "'" + contas.getDesignacao() + "', "
                + "'" + contas.getNumero() + "', "
                + "'" + contas.getIban() + "', "
                + "'" + contas.getTitular1() + "', "
                + "'" + contas.getTitular2() + "', "
                + contas.getSaldo() + ", "
                + "'" + contas.getObjecto() + "', "
                + "'" + MetodosUtil.getDataBancoFull( contas.getDataCriacao() ) + "', "
                + contas.getUserId() + ","
                + contas.getTipoContaId()
                + " ) ";

        return conexao.executeUpdate( INSERT );

    }

    @Override
    public boolean actualizar( Object object )
    {
        return true;
    }

    public boolean actualizarSaldo( int idConta, BigDecimal saldo )
    {

        String UPDATE = "UPDATE contas SET saldo = " + saldo + "  WHERE pk_contas = " + idConta;
        return conexao.executeUpdate( UPDATE );
    }

    @Override
    public boolean eliminar( int pk_contas )
    {
        String DELETE = "DELETE FROM contas WHERE pk_contas = " + pk_contas;
        return conexao.executeUpdate( DELETE );
    }

    @Override
    public List<Contas> listarTodos()
    {
        String FIND_ALL = "SELECT * FROM contas ORDER BY pk_contas ASC";
        ResultSet result = conexao.executeQuery( FIND_ALL );
        List<Contas> lista = new ArrayList<>();
        Contas conta;
        try
        {
            while ( result.next() )
            {
                conta = new Contas();
                setResultSetContas( result, conta );
                lista.add( conta );
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return lista;
    }

    public List<Contas> listarTodosByIdUsuario( int idUsuario )
    {
        String FIND_ALL = "select c.* from conta_permissoes p "
                + "inner join contas c on c.pk_contas =  p.cod_conta "
                + "where p.cod_usuario = " + idUsuario;
        ResultSet result = conexao.executeQuery( FIND_ALL );
        List<Contas> lista = new ArrayList<>();
        Contas conta;
        try
        {
            while ( result.next() )
            {
                conta = new Contas();
                setResultSetContas( result, conta );
                lista.add( conta );
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
        String FIND_ALL = "SELECT designacao FROM contas ORDER BY designacao ASC";
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

    public Vector<String> getVector( int idUser )
    {
        String FIND_ALL = "select c.designacao as designacao from conta_permissoes p "
                + "inner join contas c on c.pk_contas =  p.cod_conta "
                + "where p.cod_usuario = " + idUser;
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
    public Object findById( int pk_contas )
    {

        String FIND__BY_CODIGO = "SELECT * FROM contas WHERE pk_contas = " + pk_contas;
        ResultSet result = conexao.executeQuery( FIND__BY_CODIGO );
        Contas tipoProduto = null;
        try
        {
            if ( result.next() )
            {
                tipoProduto = new Contas();
                setResultSetContas( result, tipoProduto );
            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return tipoProduto;

    }

    public Contas getContasByDesignacao( String designacao )
    {
        String FIND_BY_CODIGO = "SELECT *  FROM contas  WHERE designacao = '" + designacao + "'";
        ResultSet result = conexao.executeQuery( FIND_BY_CODIGO );
        Contas tipoProduto = null;
        try
        {
            if ( result.next() )
            {
                tipoProduto = new Contas();
                setResultSetContas( result, tipoProduto );
            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return tipoProduto;

    }

    public boolean existeContaByDesignacao( String designacao )
    {
        String FIND_BY_CODIGO = "SELECT *  FROM contas  WHERE designacao = '" + designacao + "'";
        ResultSet result = conexao.executeQuery( FIND_BY_CODIGO );
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

    public boolean existeContaByNumero( String numero )
    {
        String FIND_BY_CODIGO = "SELECT *  FROM contas  WHERE numero = '" + numero + "'";
        ResultSet result = conexao.executeQuery( FIND_BY_CODIGO );
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

    public Contas getContasByNumero( String numero )
    {
        String query = "SELECT *  FROM contas  WHERE numero = '" + numero + "'";
        ResultSet result = conexao.executeQuery( query );
        Contas tipoProduto = null;
        try
        {
            if ( result.next() )
            {
                tipoProduto = new Contas();
                setResultSetContas( result, tipoProduto );
            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return tipoProduto;

    }

    public BigDecimal getTotalSalo()
    {
        String query = "select sum(saldo) as total_saldo from contas";
        ResultSet result = conexao.executeQuery( query );
        BigDecimal totalSaldo = new BigDecimal( 0 );
        try
        {
            if ( result.next() )
            {
                totalSaldo = result.getBigDecimal( "total_saldo" );
            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return totalSaldo;

    }

    public BigDecimal getTotalSaloByIdUser( int idUser )
    {
        String query = "select sum(c.saldo) as total_saldo from conta_permissoes p "
                + "inner join contas c on c.pk_contas =  p.cod_conta "
                + "where p.cod_usuario = " + idUser;
        ResultSet result = conexao.executeQuery( query );
        BigDecimal totalSaldo = new BigDecimal( 0 );
        try
        {
            if ( result.next() )
            {
                totalSaldo = result.getBigDecimal( "total_saldo" );
            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return totalSaldo;

    }

    private void setResultSetContas( ResultSet rs, Contas conta )
    {
        try
        {
            conta.setPkContas( rs.getInt( "pk_contas" ) );
            conta.setDesignacao( rs.getString( "designacao" ) );
            conta.setNumero( rs.getString( "numero" ) );
            conta.setIban( rs.getString( "iban" ) );
            conta.setTitular1( rs.getString( "titular_1" ) );
            conta.setTitular2( rs.getString( "titular_2" ) );
            conta.setSaldo( rs.getBigDecimal( "saldo" ) );
            conta.setObjecto( rs.getString( "objecto" ) );
            conta.setDataCriacao( rs.getDate( "data_criacao" ) );
            conta.setUserId( rs.getInt( "user_id" ) );
            conta.setTipoContaId( rs.getInt( "tipo_conta_id" ) );
        }
        catch ( SQLException e )
        {
        }
    }

}
