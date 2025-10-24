/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tesouraria.novo.controller;


import java.sql.Connection;
import entity.ContaMovimentos;
import entity.Contas;
import comercial.controller.DocumentosController;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import javax.swing.JOptionPane;
import tesouraria.novo.util.AnyReport;
import util.BDConexao;
import util.MetodosUtil;

/**
 *
 * @author Domingos Dala Vunge
 */
public class ContaMovimentosController implements EntidadeFactory
{

    private BDConexao conexao;

    public ContaMovimentosController( BDConexao conexao )
    {
        this.conexao = conexao;
    }

    @Override
    public boolean salvar( Object object )
    {
        ContaMovimentos cm = ( ContaMovimentos ) object;
        String INSERT = "INSERT INTO conta_movimentos( data_hora, conta_id, conta_designacao, saldo_anterior,"
                + " valor_entrada, valor_saida, saldo_final, tipo,  descricao, documento, cod_operacao "
                + ")"
                + " VALUES("
                + "'" + MetodosUtil.getDataBancoFull( cm.getDataHora() ) + "', "
                + cm.getContaId() + ", "
                + "'" + cm.getContaDesignacao() + "', "
                + cm.getSaldoAnterior() + ", "
                + cm.getValorEntrada() + ", "
                + cm.getValorSaida() + ", "
                + cm.getSaldoFinal() + ", "
                + "'" + cm.getTipo() + "', "
                + "'" + cm.getDescricao() + "', "
                + "'" + cm.getDocumento() + "', "
                + cm.getCodOperacao()
                + " ) ";

        System.err.println( INSERT );
        return conexao.executeUpdate( INSERT );

    }

    @Override
    public boolean actualizar( Object object )
    {
        return true;
    }

    @Override
    public boolean eliminar( int pk_conta_movimento )
    {
        String DELETE = "DELETE FROM conta_movimentos WHERE pk_conta_movimento = " + pk_conta_movimento;
        return conexao.executeUpdate( DELETE );
    }

    @Override
    public List<ContaMovimentos> listarTodos()
    {
        String FIND_ALL = "SELECT * FROM conta_movimentos ORDER BY pk_conta_movimento ASC";
        ResultSet result = conexao.executeQuery( FIND_ALL );
        List<ContaMovimentos> lista = new ArrayList<>();
        ContaMovimentos conta;
        try
        {
            while ( result.next() )
            {
                conta = new ContaMovimentos();
                setResultSetContaMovimentos( result, conta );
                lista.add( conta );
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return lista;
    }

    public List<ContaMovimentos> listarTodosByIdConta( int idConta, Date data_1, Date data_2 )
    {
        String FIND_ALL = "SELECT * FROM conta_movimentos  WHERE conta_id = " + idConta
                + " AND DATE(data_hora) BETWEEN '" + MetodosUtil.getDataBanco( data_1 ) + "' AND '" + MetodosUtil.getDataBanco( data_2 ) + "'";
        ResultSet result = conexao.executeQuery( FIND_ALL );
        List<ContaMovimentos> lista = new ArrayList<>();
        ContaMovimentos conta;
        try
        {
            while ( result.next() )
            {
                conta = new ContaMovimentos();
                setResultSetContaMovimentos( result, conta );
                lista.add( conta );
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return lista;
    }

    public List<ContaMovimentos> listarTodosByCodOperacao( long codOperacao )
    {
        String FIND_ALL = "SELECT * FROM conta_movimentos  WHERE cod_operacao = " + codOperacao;
        ResultSet result = conexao.executeQuery( FIND_ALL );
        List<ContaMovimentos> lista = new ArrayList<>();
        ContaMovimentos conta;
        try
        {
            while ( result.next() )
            {
                conta = new ContaMovimentos();
                setResultSetContaMovimentos( result, conta );
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
        return null;
    }

    @Override
    public Object findById( int pk_conta_movimento )
    {

        String FIND__BY_CODIGO = "SELECT * FROM conta_movimentos WHERE pk_conta_movimento = " + pk_conta_movimento;
        ResultSet result = conexao.executeQuery( FIND__BY_CODIGO );
        ContaMovimentos tipoProduto = null;
        try
        {
            if ( result.next() )
            {
                tipoProduto = new ContaMovimentos();
                setResultSetContaMovimentos( result, tipoProduto );
            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return tipoProduto;

    }

    public ContaMovimentos getContaMovimentosByDesignacao( String designacao )
    {
        String FIND_BY_CODIGO = "SELECT *  FROM conta_movimentos  WHERE designacao = '" + designacao + "'";
        ResultSet result = conexao.executeQuery( FIND_BY_CODIGO );
        ContaMovimentos tipoProduto = null;
        try
        {
            if ( result.next() )
            {
                tipoProduto = new ContaMovimentos();
                setResultSetContaMovimentos( result, tipoProduto );
            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return tipoProduto;

    }

    private void setResultSetContaMovimentos( ResultSet rs, ContaMovimentos cm )
    {
        try
        {
            cm.setPkContaMovimento( rs.getLong( "pk_conta_movimento" ) );
            cm.setDataHora( rs.getDate( "data_hora" ) );
            cm.setContaId( rs.getInt( "conta_id" ) );
            cm.setContaDesignacao( rs.getString( "conta_designacao" ) );
            cm.setSaldoAnterior( rs.getBigDecimal( "saldo_anterior" ) );
            cm.setValorEntrada( rs.getBigDecimal( "valor_entrada" ) );
            cm.setValorSaida( rs.getBigDecimal( "valor_saida" ) );
            cm.setSaldoFinal( rs.getBigDecimal( "saldo_final" ) );
            cm.setTipo( rs.getString( "tipo" ) );
            cm.setDescricao( rs.getString( "descricao" ) );
            cm.setDocumento( rs.getString( "documento" ) );
            cm.setCodOperacao( rs.getLong( "cod_operacao" ) );
        }
        catch ( SQLException e )
        {
        }
    }

    public boolean procedimentoEntradaContas( Contas conta, String documeto, BigDecimal valor, String descricao, String tipo, 
            int userId, String userName, String beneficiario, BDConexao conexao )
    {
        ContaOperacaoController coc = new ContaOperacaoController( conexao );
        DocumentosController.start( conexao );
        long LAST_ID = coc.insertContaOperacao( tipo, new Date(), userId, userName, beneficiario, valor );

        boolean sucesso = false;
        if ( LAST_ID > 0 )
        {
            DocumentosController.commit( conexao );
            sucesso = entrada( LAST_ID, conta, documeto, valor, descricao, conexao );
        }
        else
        {
            DocumentosController.rollback( conexao );
            JOptionPane.showMessageDialog( null, "Falha ao registrar a operacao.", "Falha", JOptionPane.ERROR_MESSAGE );
        }

        return sucesso;
    }

    public boolean procedimentoTransferencia( Contas contaSaida, String documento, Contas contaEntrada, BigDecimal valor, String descricao,
            int userId, String userName, String beneficiario, BDConexao conexao )
    {
        ContaOperacaoController coc = new ContaOperacaoController( conexao );
        DocumentosController.start( conexao );
        long LAST_ID = coc.insertContaOperacao( "Transferencia", new Date(), userId, userName, beneficiario, valor );
        if ( LAST_ID > 0 )
        {
            DocumentosController.commit( conexao );
            boolean saida = saida( LAST_ID, contaSaida, documento, valor, descricao, conexao );
            boolean entrada = entrada( LAST_ID, contaEntrada, documento, valor, descricao, conexao );
            return ( saida && entrada );

        }
        else
        {
            DocumentosController.rollback( conexao );
            JOptionPane.showMessageDialog( null, "Falha ao registrar a operacao.", "Falha", JOptionPane.ERROR_MESSAGE );
        }
        return false;
    }

    public boolean procedimentoSaidaContas( Contas conta, String documento, BigDecimal valor, String descricao,
            String tipo, int userId, String userName, String beneficiario, BDConexao conexao )
    {
        ContaOperacaoController coc = new ContaOperacaoController( conexao );
        DocumentosController.start( conexao );
        long LAST_ID = coc.insertContaOperacao( tipo, new Date(), userId, userName, beneficiario, valor );
        if ( LAST_ID > 0 )
        {
            DocumentosController.commit( conexao );
            return saida( LAST_ID, conta, documento, valor, descricao, conexao );
        }
        else
        {
            DocumentosController.rollback( conexao );
            JOptionPane.showMessageDialog( null, "Falha ao registrar a operacao.", "Falha", JOptionPane.ERROR_MESSAGE );
        }
        return false;
    }

    public boolean entrada( long lasID, Contas conta, String documento, BigDecimal valor, String descricao, BDConexao conexao )
    {

        ContaController ccc = new ContaController( conexao );
        DocumentosController.start( conexao );

        ContaMovimentos movimento = new ContaMovimentos();
        movimento.setContaDesignacao( conta.getDesignacao() );
        movimento.setContaId( conta.getPkContas() );
        movimento.setDataHora( new Date() );
        movimento.setDescricao( descricao );
        movimento.setDocumento( documento );
        movimento.setSaldoAnterior( conta.getSaldo() );
        movimento.setValorEntrada( valor );
        movimento.setValorSaida( BigDecimal.ZERO );
        movimento.setSaldoFinal( conta.getSaldo().add( movimento.getValorEntrada() ) );
        movimento.setTipo( "Entrada" );
        movimento.setCodOperacao( lasID );

        if ( salvar( movimento )
                && ccc.actualizarSaldo( conta.getPkContas(), movimento.getSaldoFinal() ) )
        {
            DocumentosController.commit( conexao );
            return true;
        }
        else
        {
            DocumentosController.rollback( conexao );
            JOptionPane.showMessageDialog( null, "Falha ao registrar a entrada.", "Falha", JOptionPane.ERROR_MESSAGE );
        }
        return false;

    }

    public boolean saida( long lasID, Contas conta, String documento, BigDecimal valor, String descricao, BDConexao conexao )
    {
        ContaController ccc = new ContaController( conexao );
        DocumentosController.start( conexao );
        ContaMovimentos movimento = new ContaMovimentos();
        movimento.setContaDesignacao( conta.getDesignacao() );
        movimento.setContaId( conta.getPkContas() );
        movimento.setDataHora( new Date() );
        movimento.setDescricao( descricao );
        movimento.setDocumento( documento );
        movimento.setSaldoAnterior( conta.getSaldo() );
        movimento.setValorEntrada( BigDecimal.ZERO );
        movimento.setValorSaida( valor );
        movimento.setSaldoFinal( conta.getSaldo().subtract( movimento.getValorSaida() ) );
        movimento.setTipo( "Saida" );
        movimento.setCodOperacao( lasID );
        if ( salvar( movimento )
                && ccc.actualizarSaldo( conta.getPkContas(), movimento.getSaldoFinal() ) )
        {
            DocumentosController.commit( conexao );
            return true;
        }
        else
        {
            DocumentosController.rollback( conexao );
            JOptionPane.showMessageDialog( null, "Falha ao registrar a saida.", "Falha", JOptionPane.ERROR_MESSAGE );
        }
        return false;
    }

}
