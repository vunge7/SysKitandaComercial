/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comercial.controller;

import entity.FormaPagamento;
import entity.FormaPagamentoItem;
import entity.TbVenda;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Vector;
import util.BDConexao;
import util.MetodosUtil;

/**
 *
 * @author Domingos Dala Vunge
 */
public class FormaPagamentoItemController implements EntidadeFactory
{

    private BDConexao conexao;

    public FormaPagamentoItemController( BDConexao conexao )
    {
        this.conexao = conexao;
    }

    @Override
//    public boolean salvar( Object object )
//    {
//        FormaPagamentoItem formaPagamentoItem = ( FormaPagamentoItem ) object;
//
//        String INSERT = "INSERT INTO forma_pagamento_item( valor, referencia, fk_forma_pagamento, fk_venda "
//                + ")"
//                + " VALUES("
//                + formaPagamentoItem.getValor().doubleValue() + ", "
//                + "'" + formaPagamentoItem.getReferencia() + "', "
//                + formaPagamentoItem.getFkFormaPagamento().getPkFormaPagamento() + ", "
//                + formaPagamentoItem.getFkVenda().getCodigo()
//                + " ) ";
//
//        return conexao.executeUpdate( INSERT );
//
//    }

    public boolean salvar( Object object )
    {
        FormaPagamentoItem formaPagamentoItem = (FormaPagamentoItem) object;

        String INSERT = "INSERT INTO forma_pagamento_item( valor, troco, valor_real, referencia, fk_forma_pagamento, fk_venda "
                + ")"
                + " VALUES("
                + formaPagamentoItem.getValor().doubleValue() + ", "
                + formaPagamentoItem.getTroco().doubleValue() + ", "
                + formaPagamentoItem.getValor_real().doubleValue() + ", "
                + "'" + formaPagamentoItem.getReferencia() + "', "
                + formaPagamentoItem.getFkFormaPagamento().getPkFormaPagamento() + ", "
                + formaPagamentoItem.getFkVenda().getCodigo()
                + " ) ";

        return conexao.executeUpdate( INSERT );

    }

    @Override
    public boolean actualizar( Object object )
    {
        return true;
    }

    @Override
    public boolean eliminar( int pk_forma_pagamento_item )
    {
        String DELETE = "DELETE FROM forma_pagamento_item WHERE pk_forma_pagamento_item = " + pk_forma_pagamento_item;
        return conexao.executeUpdate( DELETE );
    }

    @Override
    public List<FormaPagamentoItem> listarTodos()
    {

        String FIND_ALL = "SELECT * FROM forma_pagamento_item ORDER BY pk_forma_pagamento_item ASC";
        ResultSet result = conexao.executeQuery( FIND_ALL );
        List<FormaPagamentoItem> lista_forma_pagamento_items = new ArrayList<>();
        FormaPagamentoItem forma_pagamento_item;
        try
        {
            while ( result.next() )
            {
                forma_pagamento_item = new FormaPagamentoItem();
                forma_pagamento_item.setValor( result.getBigDecimal( "valor" ) );
                forma_pagamento_item.setReferencia( result.getString( "referencia" ) );
                forma_pagamento_item.setFkFormaPagamento( new FormaPagamento( result.getInt( "fk_forma_pagamento" ) ) );
                forma_pagamento_item.setFkVenda( new TbVenda( result.getInt( "fk_venda" ) ) );
                lista_forma_pagamento_items.add( forma_pagamento_item );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return lista_forma_pagamento_items;
    }

    @Override
    public Vector<String> getVector()
    {
        String FIND_ALL = "SELECT * FROM forma_pagamento_item ORDER BY pk_forma_pagamento_item ASC";
        ResultSet result = conexao.executeQuery( FIND_ALL );
        Vector<String> lista_formaPagamentoItem = new Vector<>();
        try
        {
            while ( result.next() )
            {
                lista_formaPagamentoItem.add( result.getString( "referencia" ) );
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return lista_formaPagamentoItem;

    }

    @Override
    public Object findById( int pk_forma_pagamento_item )
    {

        String FIND__BY_CODIGO = "SELECT * FROM forma_pagamento_item WHERE pk_forma_pagamento_item = " + pk_forma_pagamento_item;
        ResultSet result = conexao.executeQuery( FIND__BY_CODIGO );
        FormaPagamentoItem forma_pagamento_item = null;
        try
        {

            if ( result.next() )
            {
                forma_pagamento_item = new FormaPagamentoItem();
                forma_pagamento_item.setValor( result.getBigDecimal( "valor" ) );
                forma_pagamento_item.setReferencia( result.getString( "referencia" ) );
                forma_pagamento_item.setFkFormaPagamento( new FormaPagamento( result.getInt( "fk_forma_pagamento" ) ) );
                forma_pagamento_item.setFkVenda( new TbVenda( result.getInt( "fk_venda" ) ) );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return forma_pagamento_item;

    }

    public FormaPagamentoItem getFormaPagamentoItemByDesignacao( String referencia )
    {

        String FIND__BY_CODIGO = "SELECT *  FROM forma_pagamento_item a WHERE a.referencia = '" + referencia + "'";
        ResultSet result = conexao.executeQuery( FIND__BY_CODIGO );
        FormaPagamentoItem forma_pagamento_item = null;
        try
        {

            if ( result.next() )
            {
                forma_pagamento_item = new FormaPagamentoItem();
                forma_pagamento_item.setValor( result.getBigDecimal( "valor" ) );
                forma_pagamento_item.setReferencia( result.getString( "referencia" ) );
                forma_pagamento_item.setFkFormaPagamento( new FormaPagamento( result.getInt( "fk_forma_pagamento" ) ) );
                forma_pagamento_item.setFkVenda( new TbVenda( result.getInt( "fk_venda" ) ) );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return forma_pagamento_item;

    }

    public Integer getNumeroVendasDiario( BDConexao conexao, Date data_1, Date data_2 )
    {
//        String query = "SELECT COUNT(pk_forma_pagamento_item) AS NUMERO_VENDAS  FROM forma_pagamento_item i, tb_venda v WHERE "
//                + " i.fk_venda = v.codigo AND v.status_eliminado = 'false' AND v.dataVenda BETWEEN  '"
//                + MetodosUtil.getDataBancoFull( data_1 ) + "'  AND  '" + MetodosUtil.getDataBancoFull( data_2 ) + "' AND i.fk_venda = v.codigo";
////        String query = "SELECT COUNT(pk_forma_pagamento_item) AS NUMERO_VENDAS  FROM forma_pagamento_item i, tb_venda v WHERE "
////                + " i.fk_venda = v.codigo AND v.dataVenda BETWEEN  '"
////                + MetodosUtil.getDataBancoFull( data_1 ) + "'  AND  '" + MetodosUtil.getDataBancoFull( data_2 ) + "' AND i.fk_venda = v.codigo";
//
//
        String query = "select count(*) AS NUMERO_VENDAS "
                + "from tb_venda "
                + "where fk_documento = 1 "
                + "and status_eliminado = 'false' "
                + "and dataVenda between  '" + MetodosUtil.getDataBancoFull( data_1 ) + "' "
                + "and  '" + MetodosUtil.getDataBancoFull( data_2 ) + "'";

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

        }

        return 0;

    }

    public Double getValorRealDiario( int idFormaPagamento, Date data_1, Date data_2, BDConexao conexao )
    {

        System.err.println( "PK_DOCUMENTO: " + idFormaPagamento );
        String query = "SELECT SUM(valor) AS TOTAL  FROM forma_pagamento_item i, tb_venda v WHERE i.fk_forma_pagamento = " + idFormaPagamento + ""
                + " AND v.status_eliminado = 'false' AND v.dataVenda BETWEEN  '"
                + MetodosUtil.getDataBancoFull( data_1 ) + "' AND '" + MetodosUtil.getDataBancoFull( data_2 ) + "' AND i.fk_venda = v.codigo";

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

        }

        return 0d;

    }

    public Double getTrocoRealDiario( int idFormaPagamento, Date data_1, Date data_2, BDConexao conexao )
    {

        System.err.println( "PK_DOCUMENTO: " + idFormaPagamento );
        String query = "SELECT SUM(v.troco) AS TOTAL  FROM forma_pagamento_item i, tb_venda v WHERE i.fk_forma_pagamento = " + idFormaPagamento + ""
                + " AND v.status_eliminado = 'false' AND v.dataVenda BETWEEN  '"
                + MetodosUtil.getDataBancoFull( data_1 ) + "' AND '" + MetodosUtil.getDataBancoFull( data_2 ) + "' AND i.fk_venda = v.codigo";

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

        }

        return 0d;

    }

    public Double getTrocoRealDiario( int idUser, int idFormaPagamento, Date data_1, Date data_2, BDConexao conexao )
    {

        System.err.println( "PK_DOCUMENTO: " + idFormaPagamento );
        String query = "SELECT SUM(v.troco) AS TOTAL  FROM forma_pagamento_item i, tb_venda v WHERE i.fk_forma_pagamento = " + idFormaPagamento + ""
                + " AND v.status_eliminado = 'false' AND v.dataVenda BETWEEN  '"
                + MetodosUtil.getDataBancoFull( data_1 ) + "' AND '" + MetodosUtil.getDataBancoFull( data_2 ) + "' AND i.fk_venda = v.codigo AND v.codigo_usuario = " + idUser;

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

        }

        return 0d;

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

        }

        return 0d;

    }

    public Double getValorRealDiario( BDConexao conexao, Date data_1, Date data_2 )
    {
        String query = "SELECT SUM(i.valor_real) AS TOTAL  FROM forma_pagamento_item i, tb_venda v WHERE "
                + " i.fk_venda = v.codigo AND v.status_eliminado = 'false' AND v.dataVenda BETWEEN '"
                + MetodosUtil.getDataBancoFull( data_1 )
                + "'  AND  '" + MetodosUtil.getDataBancoFull( data_2 ) + "'";
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

        }

        return 0d;

    }

    public Double getTrocoRealDiario( BDConexao conexao, Date data_1, Date data_2 )
    {
//        String query = "SELECT SUM(i.troco) AS TOTAL  FROM forma_pagamento_item i, tb_venda v WHERE "
//                + " i.fk_venda = v.codigo AND v.status_eliminado = 'false' AND v.dataVenda BETWEEN '"
//                + MetodosUtil.getDataBancoFull( data_1 ) + "'  AND  '" + MetodosUtil.getDataBancoFull( data_2 ) + "' AND i.fk_venda = v.codigo";

        String query = "select sum(i.troco) as TOTAL "
                + "from forma_pagamento_item i "
                + "inner join tb_venda v on v.codigo = i.fk_venda "
                + "where v.status_eliminado = 'false' "
                + "and v.fk_documento = 1 "
                + "and v.dataVenda between  '" + MetodosUtil.getDataBancoFull( data_1 ) + "' "
                + " AND  '" + MetodosUtil.getDataBancoFull( data_2 ) + "'";

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

        }
        return 0d;

    }
    
    public Integer getNumeroVendasDiario(BDConexao conexao, int idUser, Date data1, Date data2) {
    String query = "SELECT COUNT(*) AS NUMERO_VENDAS "
                 + "FROM tb_venda "
                 + "WHERE fk_documento = 1 "
                 + "AND status_eliminado = 'false' "
                 + "AND dataVenda BETWEEN ? AND ? "
                 + "AND codigo_usuario = ?";

    try {
        PreparedStatement ps = conexao.getConnection1().prepareStatement(query);
        ps.setTimestamp(1, new java.sql.Timestamp(data1.getTime()));
        ps.setTimestamp(2, new java.sql.Timestamp(data2.getTime()));
        ps.setInt(3, idUser);

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return rs.getInt("NUMERO_VENDAS");
        }

    } catch (SQLException e) {
        System.err.println("Erro ao buscar número de vendas: " + e.getMessage());
        e.printStackTrace();
    }

    return 0;
}


//    public Integer getNumeroVendasDiario( BDConexao conexao, int idUser, Date data_1, Date data_2 )
//    {
//        String query = "select count(*) AS NUMERO_VENDAS "
//                + "from tb_venda "
//                + "where fk_documento = 1 "
//                + "and status_eliminado = 'false' "
//                + "and dataVenda between  '" + MetodosUtil.getDataBancoFull( data_1 ) + "' "
//                + "and  '" + MetodosUtil.getDataBancoFull( data_2 ) + "' "
//                + "and codigo_usuario = " + idUser;
//
//        System.out.println( query );
//        ResultSet resultSet = conexao.executeQuery( query );
//
//        try
//        {
//            if ( resultSet.next() )
//            {
//                Integer total_real = resultSet.getInt( "NUMERO_VENDAS" );
//                return total_real;
//            }
//        }
//        catch ( SQLException ex )
//        {
//            ex.printStackTrace();
//
//        }
//
//        return 0;
//
//    }

//    public double getValorRealDiario( BDConexao conexao, int idUser, Date data_1, Date data_2 )
//    {
//        String query = "select sum(i.valor_real) as TOTAL "
//                + "from forma_pagamento_item i "
//                + "inner join tb_venda v on v.codigo = i.fk_venda "
//                + "where v.status_eliminado = 'false' "
//                + "and v.fk_documento = 1 "
//                + "and v.dataVenda between  '" + MetodosUtil.getDataBancoFull( data_1 ) + "' "
//                + " AND  '" + MetodosUtil.getDataBancoFull( data_2 ) + "'"
//                + " AND v.codigo_usuario = " + idUser;
//
//        System.out.println( query );
//        ResultSet resultSet = conexao.executeQuery( query );
//
//        try
//        {
//            if ( resultSet.next() )
//            {
//                Double total_real = resultSet.getDouble( "TOTAL" );
//
//                return total_real;
//            }
//        }
//        catch ( SQLException ex )
//        {
//            ex.printStackTrace();
//
//        }
//
//        return 0d;
//
//    }
public BigDecimal getValorRealDiario(BDConexao conexao, int idUser, Date data1, Date data2) {
    String query = "SELECT SUM(i.valor_real) AS TOTAL " +
                   "FROM forma_pagamento_item i " +
                   "INNER JOIN tb_venda v ON v.codigo = i.fk_venda " +
                   "WHERE v.status_eliminado = 'false' " +
                   "AND v.fk_documento = 1 " +
                   "AND v.dataVenda BETWEEN ? AND ? " +
                   "AND v.codigo_usuario = ?";

    try {
        PreparedStatement ps = conexao.getConnection1().prepareStatement(query);
        ps.setTimestamp(1, new java.sql.Timestamp(data1.getTime()));
        ps.setTimestamp(2, new java.sql.Timestamp(data2.getTime()));
        ps.setInt(3, idUser);

        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            BigDecimal total = rs.getBigDecimal("TOTAL");
            return total != null ? total : BigDecimal.ZERO;
        }

    } catch (SQLException ex) {
        System.err.println("Erro ao obter valor real diário: " + ex.getMessage());
        ex.printStackTrace();
    }

    return BigDecimal.ZERO;
}


    public BigDecimal getTotalDesconto( BDConexao conexao, int idUser, Date data_1, Date data_2 )
    {
//        String query = "SELECT SUM(i.valor_real) AS TOTAL  FROM forma_pagamento_item i, tb_venda v WHERE "
//                + " i.fk_venda = v.codigo AND v.status_eliminado = 'false' AND v.dataVenda BETWEEN '"
//                + MetodosUtil.getDataBancoFull( data_1 )
//                + "'  AND  '" + MetodosUtil.getDataBancoFull( data_2 ) + "'"
//                + " AND v.codigo_usuario = " + idUser;
//        String query = "SELECT SUM(valor) AS TOTAL  FROM forma_pagamento_item i, tb_venda v WHERE "
//                + " i.fk_venda = v.codigo AND v.dataVenda BETWEEN '"
//                + MetodosUtil.getDataBancoFull( data_1 ) + "'  AND  '" + MetodosUtil.getDataBancoFull( data_2 ) + "' AND i.fk_venda = v.codigo";
        String query = "select sum(v.desconto_total) as TOTAL "
                + "from tb_venda v "
                + "where v.status_eliminado = 'false' "
                + "and v.fk_documento = 1 "
                + "and v.dataVenda between  '" + MetodosUtil.getDataBancoFull( data_1 ) + "' "
                + " AND  '" + MetodosUtil.getDataBancoFull( data_2 ) + "'"
                + " AND v.codigo_usuario = " + idUser;

        System.out.println( query );
        ResultSet resultSet = conexao.executeQuery( query );

        try
        {
            if ( resultSet.next() )
            {
                BigDecimal total = resultSet.getBigDecimal( "TOTAL" );
                if ( Objects.isNull( total ) )
                {
                    return new BigDecimal( 0d );
                }
                return total;
            }
        }
        catch ( SQLException ex )
        {
            ex.printStackTrace();

        }

        return new BigDecimal( 0d );

    }

    public BigDecimal getTotalIva( BDConexao conexao, int idUser, Date data_1, Date data_2 )
    {
//        String query = "SELECT SUM(i.valor_real) AS TOTAL  FROM forma_pagamento_item i, tb_venda v WHERE "
//                + " i.fk_venda = v.codigo AND v.status_eliminado = 'false' AND v.dataVenda BETWEEN '"
//                + MetodosUtil.getDataBancoFull( data_1 )
//                + "'  AND  '" + MetodosUtil.getDataBancoFull( data_2 ) + "'"
//                + " AND v.codigo_usuario = " + idUser;
//        String query = "SELECT SUM(valor) AS TOTAL  FROM forma_pagamento_item i, tb_venda v WHERE "
//                + " i.fk_venda = v.codigo AND v.dataVenda BETWEEN '"
//                + MetodosUtil.getDataBancoFull( data_1 ) + "'  AND  '" + MetodosUtil.getDataBancoFull( data_2 ) + "' AND i.fk_venda = v.codigo";
        String query = "select sum(v.total_iva) as TOTAL "
                + "from tb_venda v "
                + "where v.status_eliminado = 'false' "
                + "and v.fk_documento = 1 "
                + "and v.dataVenda between  '" + MetodosUtil.getDataBancoFull( data_1 ) + "' "
                + " AND  '" + MetodosUtil.getDataBancoFull( data_2 ) + "'"
                + " AND v.codigo_usuario = " + idUser;

        System.out.println( query );
        ResultSet resultSet = conexao.executeQuery( query );

        try
        {
            if ( resultSet.next() )
            {
                BigDecimal total = resultSet.getBigDecimal( "TOTAL" );
                if ( Objects.isNull( total ) )
                {
                    return new BigDecimal( 0d );
                }
                return total;
            }
        }
        catch ( SQLException ex )
        {
            ex.printStackTrace();

        }

        return new BigDecimal( 0d );

    }

    public BigDecimal getTotalIliquido( BDConexao conexao, int idUser, Date data_1, Date data_2 )
    {
//        String query = "SELECT SUM(i.valor_real) AS TOTAL  FROM forma_pagamento_item i, tb_venda v WHERE "
//                + " i.fk_venda = v.codigo AND v.status_eliminado = 'false' AND v.dataVenda BETWEEN '"
//                + MetodosUtil.getDataBancoFull( data_1 )
//                + "'  AND  '" + MetodosUtil.getDataBancoFull( data_2 ) + "'"
//                + " AND v.codigo_usuario = " + idUser;
//        String query = "SELECT SUM(valor) AS TOTAL  FROM forma_pagamento_item i, tb_venda v WHERE "
//                + " i.fk_venda = v.codigo AND v.dataVenda BETWEEN '"
//                + MetodosUtil.getDataBancoFull( data_1 ) + "'  AND  '" + MetodosUtil.getDataBancoFull( data_2 ) + "' AND i.fk_venda = v.codigo";
        String query = "select sum(v.total_geral) as TOTAL "
                + "from tb_venda v "
                + "where v.status_eliminado = 'false' "
                + "and v.fk_documento = 1 "
                + "and v.dataVenda between  '" + MetodosUtil.getDataBancoFull( data_1 ) + "' "
                + " AND  '" + MetodosUtil.getDataBancoFull( data_2 ) + "'"
                + " AND v.codigo_usuario = " + idUser;

        System.out.println( query );
        ResultSet resultSet = conexao.executeQuery( query );

        try
        {
            if ( resultSet.next() )
            {
                BigDecimal total = resultSet.getBigDecimal( "TOTAL" );
                if ( Objects.isNull( total ) )
                {
                    return new BigDecimal( 0d );
                }
                return total;
            }
        }
        catch ( SQLException ex )
        {
            ex.printStackTrace();

        }

        return new BigDecimal( 0d );

    }

//    public Double getTrocoRealDiario( BDConexao conexao, int idUser, Date data_1, Date data_2 )
//    {
//        String query = "select sum(i.troco) as TOTAL "
//                + "from forma_pagamento_item i "
//                + "inner join tb_venda v on v.codigo = i.fk_venda "
//                + "where v.status_eliminado = 'false' "
//                + "and v.fk_documento = 1 "
//                + "and v.dataVenda between  '" + MetodosUtil.getDataBancoFull( data_1 ) + "' "
//                + " AND  '" + MetodosUtil.getDataBancoFull( data_2 ) + "'"
//                + " AND v.codigo_usuario = " + idUser;
//
//        System.out.println( query );
//        ResultSet resultSet = conexao.executeQuery( query );
//        try
//        {
//            if ( resultSet.next() )
//            {
//                Double total_real = resultSet.getDouble( "TOTAL" );
//                return total_real;
//            }
//        }
//        catch ( SQLException ex )
//        {
//            ex.printStackTrace();
//
//        }
//        return 0d;
//
//    }
    public BigDecimal getTrocoRealDiario( BDConexao conexao, int idUser, Date data_1, Date data_2 )
    {
        String query = "SELECT SUM(i.troco) AS TOTAL "
                + "FROM forma_pagamento_item i "
                + "INNER JOIN tb_venda v ON v.codigo = i.fk_venda "
                + "WHERE v.status_eliminado = 'false' "
                + "AND v.fk_documento = 1 "
                + "AND v.dataVenda BETWEEN '" + MetodosUtil.getDataBancoFull( data_1 ) + "' "
                + "AND '" + MetodosUtil.getDataBancoFull( data_2 ) + "' "
                + "AND v.codigo_usuario = " + idUser;

        System.out.println( query );
        ResultSet resultSet = conexao.executeQuery( query );

        try
        {
            if ( resultSet.next() )
            {
                BigDecimal total = resultSet.getBigDecimal( "TOTAL" );
                return total != null ? total : BigDecimal.ZERO;
            }
        }
        catch ( SQLException ex )
        {
            ex.printStackTrace();
        }

        return BigDecimal.ZERO;
    }

    public boolean existeVendaDiarioByFormaPagamento( int idUser, int idFormaPagamento, Date data_1, Date data_2, BDConexao conexao )
    {
        System.err.println( "PK_DOCUMENTO: " + idFormaPagamento );

        String query = "select "
                + "   sum(i.valor_real) AS TOTAL "
                + "from forma_pagamento_item i "
                + "inner join tb_venda v on  i.fk_venda  = v.codigo "
                + "inner join forma_pagamento f on f.pk_forma_pagamento  = i.fk_forma_pagamento "
                + "inner join tb_usuario u on u.codigo  = v.codigo_usuario "
                + "where v.status_eliminado = 'false' "
                + " and v.fk_documento = 1 "
                + " and v.codigo_usuario  = " + idUser
                + " and i.fk_forma_pagamento =  " + idFormaPagamento
                + " and v.dataVenda between '" + MetodosUtil.getDataBancoFull( data_1 ) + "' AND '" + MetodosUtil.getDataBancoFull( data_2 ) + "'"
                + " group by f.designacao ";
        System.out.println( query );
        ResultSet resultSet = conexao.executeQuery( query );

        try
        {
            if ( resultSet.next() )
            {
                Double total_real = resultSet.getDouble( "TOTAL" );
                return true;
            }
        }
        catch ( SQLException ex )
        {
            ex.printStackTrace();

        }

        return false;
    }

//    public Double getValorRealDiarioByFormaPagamento( int idUser, int idFormaPagamento, Date data_1, Date data_2, BDConexao conexao )
//    {
//        System.err.println( "PK_DOCUMENTO: " + idFormaPagamento );
//        String query = "select "
//                + "   sum(i.valor_real) AS TOTAL "
//                + "from forma_pagamento_item i "
//                + "inner join tb_venda v on  i.fk_venda  = v.codigo "
//                + "inner join forma_pagamento f on f.pk_forma_pagamento  = i.fk_forma_pagamento "
//                + "inner join tb_usuario u on u.codigo  = v.codigo_usuario "
//                + "where v.status_eliminado = 'false' "
//                + " and v.fk_documento = 1 "
//                + " and i.fk_forma_pagamento =  " + idFormaPagamento
//                + " and v.codigo_usuario  = " + idUser
//                + " and v.dataVenda between '" + MetodosUtil.getDataBancoFull( data_1 ) + "' AND '" + MetodosUtil.getDataBancoFull( data_2 ) + "'"
//                + " group by f.designacao ";
//        System.out.println( query );
//        ResultSet resultSet = conexao.executeQuery( query );
//
//        try
//        {
//            if ( resultSet.next() )
//            {
//                Double total_real = resultSet.getDouble( "TOTAL" );
//                return total_real;
//            }
//        }
//        catch ( SQLException ex )
//        {
//            ex.printStackTrace();
//
//        }
//
//        return 0d;
//    }
    public BigDecimal getValorRealDiarioByFormaPagamento( int idUser, int idFormaPagamento, Date data_1, Date data_2, BDConexao conexao )
    {
        System.err.println( "PK_DOCUMENTO: " + idFormaPagamento );

        String query = "SELECT SUM(i.valor_real) AS TOTAL "
                + "FROM forma_pagamento_item i "
                + "INNER JOIN tb_venda v ON i.fk_venda = v.codigo "
                + "INNER JOIN forma_pagamento f ON f.pk_forma_pagamento = i.fk_forma_pagamento "
                + "INNER JOIN tb_usuario u ON u.codigo = v.codigo_usuario "
                + "WHERE v.status_eliminado = 'false' "
                + "AND v.fk_documento = 1 "
                + "AND i.fk_forma_pagamento = " + idFormaPagamento + " "
                + "AND v.codigo_usuario = " + idUser + " "
                + "AND v.dataVenda BETWEEN '" + MetodosUtil.getDataBancoFull( data_1 ) + "' "
                + "AND '" + MetodosUtil.getDataBancoFull( data_2 ) + "' "
                + "GROUP BY f.designacao";

        System.out.println( query );
        ResultSet resultSet = conexao.executeQuery( query );

        try
        {
            if ( resultSet.next() )
            {
                BigDecimal total = resultSet.getBigDecimal( "TOTAL" );
                return total != null ? total : BigDecimal.ZERO;
            }
        }
        catch ( SQLException ex )
        {
            ex.printStackTrace();
        }

        return BigDecimal.ZERO;
    }

}
