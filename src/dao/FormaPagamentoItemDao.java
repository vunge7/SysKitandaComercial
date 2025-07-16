/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import controlador.FormaPagamentoItemJpaController;
import entity.FormaPagamentoItem;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import util.BDConexao;
import util.MetodosUtil;

/**
 *
 * @author Domingos Dala Vunge
 */
public class FormaPagamentoItemDao extends FormaPagamentoItemJpaController
{

    public FormaPagamentoItemDao( EntityManagerFactory emf )
    {
        super( emf );
    }

    public static Integer insert( FormaPagamentoItem formaPagamentoItem, BDConexao conexao )
    {

        String insert_doc_forma_pagamento = "call criar_forma_pagamento_proc("
                + formaPagamentoItem.getValor() + ", "
                + "'" + formaPagamentoItem.getReferencia() + "', "
                + formaPagamentoItem.getFkFormaPagamento().getPkFormaPagamento() + " , "
                + formaPagamentoItem.getFkVenda().getCodigo()
                + ")";

        ResultSet result = conexao.executeQuery( insert_doc_forma_pagamento );

        try
        {
            if ( result.next() )
            {
                return result.getInt( "LAST_ID" );
            }

        }
        catch ( SQLException e )
        {
        }

        return 0;
    }

    public Integer getNumeroVendasDiario1( BDConexao conexao, Date data_1, Date data_2 )
    {
        String query = "SELECT COUNT(codigo) AS NUMERO_VENDAS  FROM tb_venda v WHERE v.status_eliminado = 'false' AND dataVenda BETWEEN '"
                + MetodosUtil.getDataBancoFull( data_1 ) + "'  AND  '" + MetodosUtil.getDataBancoFull( data_2 ) + "'  AND i.fk_venda = v.codigo";
//        String query = "SELECT COUNT(codigo) AS NUMERO_VENDAS  FROM tb_venda v WHERE dataVenda BETWEEN '"
//                + MetodosUtil.getDataBancoFull( data_1 ) + "'  AND  '" + MetodosUtil.getDataBancoFull( data_2 ) + "'  AND i.fk_venda = v.codigo";

        System.out.println( query );
        ResultSet resultSet = conexao.executeQuery( query );

        try
        {
            if ( resultSet.next() )
            {
                Integer total_real = resultSet.getInt( "NUMERO_VENDAS" );
                return total_real;
            }
        }
        catch ( SQLException ex )
        {
            ex.printStackTrace();
            Logger.getLogger( VendaDao.class.getName() ).log( Level.SEVERE, null, ex );
        }

        return 0;

    }

//    SELECT sum(valor) as TOTAL
//FROM forma_pagamento_item  i, tb_venda v  
//WHERE i.fk_venda = v.codigo and  v.dataVenda BETWEEN '2022-01-13 14:00:00' and '2022-01-14 16:50'
//AND i.fk_forma_pagamento = 1
//GROUP BY i.fk_forma_pagamento
    public Double getValorRealDiario( BDConexao conexao )
    {
        String query = "SELECT SUM(valor) AS TOTAL  FROM forma_pagamento_item i, tb_venda v WHERE"
                + "i.fk_venda = v.codigo AND v.status_eliminado = 'false' AND i.fk_forma_pagamento = 1 AND DATE(v.dataVenda) = '"
                + MetodosUtil.getDataBanco( new Date() ) + "' AND i.fk_venda = v.codigo";
//        String query = "SELECT SUM(valor) AS TOTAL  FROM forma_pagamento_item i, tb_venda v WHERE"
//                + "i.fk_venda = v.codigo AND i.fk_forma_pagamento = 1 AND DATE(v.dataVenda) = '"
//                + MetodosUtil.getDataBanco( new Date() ) + "' AND i.fk_venda = v.codigo";

        System.out.println( query );
        ResultSet resultSet = conexao.executeQuery( query );

        try
        {
            if ( resultSet.next() )
            {
                Double total_real = resultSet.getDouble( "TOTAL" );
                return total_real;
            }
        }
        catch ( SQLException ex )
        {
            ex.printStackTrace();
            Logger.getLogger( VendaDao.class.getName() ).log( Level.SEVERE, null, ex );
        }

        return 0d;

    }

    public Double getValorRealDiario( BDConexao conexao, Date data_1, Date data_2 )
    {
        String query = "SELECT SUM(valor) AS TOTAL  FROM forma_pagamento_item i, tb_venda v WHERE "
                + " i.fk_venda = v.codigo AND v.status_eliminado = 'false' AND v.dataVenda BETWEEN '"
                + MetodosUtil.getDataBancoFull( data_1 ) + "'  AND  '" + MetodosUtil.getDataBancoFull( data_2 ) + "' AND i.fk_venda = v.codigo";
//        String query = "SELECT SUM(valor) AS TOTAL  FROM forma_pagamento_item i, tb_venda v WHERE "
//                + " i.fk_venda = v.codigo AND v.dataVenda BETWEEN '"
//                + MetodosUtil.getDataBancoFull( data_1 ) + "'  AND  '" + MetodosUtil.getDataBancoFull( data_2 ) + "' AND i.fk_venda = v.codigo";

        System.out.println( query );
        ResultSet resultSet = conexao.executeQuery( query );

        try
        {
            if ( resultSet.next() )
            {
                Double total_real = resultSet.getDouble( "TOTAL" );
                return total_real;
            }
        }
        catch ( SQLException ex )
        {
            ex.printStackTrace();
            Logger.getLogger( VendaDao.class.getName() ).log( Level.SEVERE, null, ex );
        }

        return 0d;

    }
    public Double getTrocoRealDiario( BDConexao conexao, Date data_1, Date data_2 )
    {
        String query = "SELECT SUM(v.troco) AS TOTAL  FROM forma_pagamento_item i, tb_venda v WHERE "
                + " i.fk_venda = v.codigo AND v.status_eliminado = 'false' AND v.dataVenda BETWEEN '"
                + MetodosUtil.getDataBancoFull( data_1 ) + "'  AND  '" + MetodosUtil.getDataBancoFull( data_2 ) + "' AND i.fk_venda = v.codigo";
//        String query = "SELECT SUM(v.troco) AS TOTAL  FROM forma_pagamento_item i, tb_venda v WHERE "
//                + " i.fk_venda = v.codigo AND v.dataVenda BETWEEN '"
//                + MetodosUtil.getDataBancoFull( data_1 ) + "'  AND  '" + MetodosUtil.getDataBancoFull( data_2 ) + "' AND i.fk_venda = v.codigo";

        System.out.println( query );
        ResultSet resultSet = conexao.executeQuery( query );

        try
        {
            if ( resultSet.next() )
            {
                Double total_real = resultSet.getDouble( "TOTAL" );
                return total_real;
            }
        }
        catch ( SQLException ex )
        {
            ex.printStackTrace();
            Logger.getLogger( VendaDao.class.getName() ).log( Level.SEVERE, null, ex );
        }

        return 0d;

    }

    public Double getValorRealDiario( int idFormaPagamento, Date data_1, Date data_2, BDConexao conexao )
    {

        System.err.println( "PK_DOCUMENTO: " + idFormaPagamento );
        String query = "SELECT SUM(valor) AS TOTAL  FROM forma_pagamento_item i, tb_venda v WHERE i.fk_forma_pagamento = " + idFormaPagamento + ""
                + " AND v.status_eliminado = 'false' AND v.dataVenda BETWEEN  '"
                + MetodosUtil.getDataBancoFull( data_1 ) + "' AND '" + MetodosUtil.getDataBancoFull( data_2 ) + "' AND i.fk_venda = v.codigo";
//        String query = "SELECT SUM(valor) AS TOTAL  FROM forma_pagamento_item i, tb_venda v WHERE i.fk_forma_pagamento = " + idFormaPagamento + ""
//                + " AND v.dataVenda BETWEEN  '"
//                + MetodosUtil.getDataBancoFull( data_1 ) + "' AND '" + MetodosUtil.getDataBancoFull( data_2 ) + "' AND i.fk_venda = v.codigo";

        System.out.println( query );
        ResultSet resultSet = conexao.executeQuery( query );

        try
        {
            if ( resultSet.next() )
            {
                Double total_real = resultSet.getDouble( "TOTAL" );
                return total_real;
            }
        }
        catch ( SQLException ex )
        {
            ex.printStackTrace();
            Logger.getLogger( VendaDao.class.getName() ).log( Level.SEVERE, null, ex );
        }

        return 0d;

    }
    public Double getTrocoRealDiario( int idFormaPagamento, Date data_1, Date data_2, BDConexao conexao )
    {

        System.err.println( "PK_DOCUMENTO: " + idFormaPagamento );
        String query = "SELECT SUM(v.troco) AS TOTAL  FROM forma_pagamento_item i, tb_venda v WHERE i.fk_forma_pagamento = " + idFormaPagamento + ""
                + " AND v.status_eliminado = 'false' AND v.dataVenda BETWEEN  '"
                + MetodosUtil.getDataBancoFull( data_1 ) + "' AND '" + MetodosUtil.getDataBancoFull( data_2 ) + "' AND i.fk_venda = v.codigo";
//        System.err.println( "PK_DOCUMENTO: " + idFormaPagamento );
//        String query = "SELECT SUM(v.troco) AS TOTAL  FROM forma_pagamento_item i, tb_venda v WHERE i.fk_forma_pagamento = " + idFormaPagamento + ""
//                + " AND v.dataVenda BETWEEN  '"
//                + MetodosUtil.getDataBancoFull( data_1 ) + "' AND '" + MetodosUtil.getDataBancoFull( data_2 ) + "' AND i.fk_venda = v.codigo";

        System.out.println( query );
        ResultSet resultSet = conexao.executeQuery( query );

        try
        {
            if ( resultSet.next() )
            {
                Double total_real = resultSet.getDouble( "TOTAL" );
                return total_real;
            }
        }
        catch ( SQLException ex )
        {
            ex.printStackTrace();
            Logger.getLogger( VendaDao.class.getName() ).log( Level.SEVERE, null, ex );
        }

        return 0d;

    }

    public Integer getNumeroVendasDiario( BDConexao conexao, Date data_1, Date data_2 )
    {
        String query = "SELECT COUNT(pk_forma_pagamento_item) AS NUMERO_VENDAS  FROM forma_pagamento_item i, tb_venda v WHERE "
                + " i.fk_venda = v.codigo AND v.status_eliminado = 'false' AND v.dataVenda BETWEEN  '"
                + MetodosUtil.getDataBancoFull( data_1 ) + "'  AND  '" + MetodosUtil.getDataBancoFull( data_2 ) + "' AND i.fk_venda = v.codigo";
//        String query = "SELECT COUNT(pk_forma_pagamento_item) AS NUMERO_VENDAS  FROM forma_pagamento_item i, tb_venda v WHERE "
//                + " i.fk_venda = v.codigo AND v.dataVenda BETWEEN  '"
//                + MetodosUtil.getDataBancoFull( data_1 ) + "'  AND  '" + MetodosUtil.getDataBancoFull( data_2 ) + "' AND i.fk_venda = v.codigo";

        System.out.println( query );
        ResultSet resultSet = conexao.executeQuery( query );

        try
        {
            if ( resultSet.next() )
            {
                Integer total_real = resultSet.getInt( "NUMERO_VENDAS" );
                return total_real;
            }
        }
        catch ( SQLException ex )
        {
            ex.printStackTrace();
            Logger.getLogger( VendaDao.class.getName() ).log( Level.SEVERE, null, ex );
        }

        return 0;

    }

    public static boolean existe_referencia( String referencia, int fk_forma_pagamento, BDConexao conexao )
    {

        String sql = "SELECT * FROM forma_pagamento_item WHERE referencia = '" + referencia + "' AND fk_forma_pagamento = " + fk_forma_pagamento;

        ResultSet result = conexao.executeQuery( sql );

        try
        {
            if ( result.next() )
            {
                return true;
            }

        }
        catch ( Exception e )
        {
        }
        return false;
    }

}
