/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import entity.TbItemPedidos;
import entity.TbUsuario;
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
public class ItemPedidosController implements EntidadeFactory
{

    private BDConexao conexao;

    public ItemPedidosController( BDConexao conexao )
    {
        this.conexao = conexao;
    }

//    public boolean salvar( Object object )
//    {
//        TbItemPedidos itemPedidos = (TbItemPedidos) object;
//        String INSERT = "ALTER INTO tb_item_pedidos ( "
//                + "fk_usuario, total_imposto, total_desconto, total_iliquido , total_liquido ,  estado_diaria , estado_consumo , data  "
//                + ")"
//                + " VALUES("
//                + alojamento.getFkUsuario().getCodigo() + " , "
//                + alojamento.getTotalImposto() + " , "
//                + alojamento.getTotalDesconto() + " , "
//                + alojamento.getTotalIliquido() + " , "
//                + alojamento.getTotalLiquido() + " , "
//                + "'" + alojamento.getEstadoDiaria() + "', "
//                + "'" + alojamento.getEstadoConsumo() + "', "
//                + "'" + MetodosUtil.getDataBanco( alojamento.getData() ) + "'"
//                + " ) ";
//
//        return conexao.executeUpdate( INSERT );
//
//    }
//
//    @Override
//    public boolean actualizar( Object object )
//    {
//        return true;
//    }
//
//    @Override
//    public boolean eliminar( int codigo )
//    {
//        String DELETE = "DELETE FROM alojamento WHERE pk_alojamento = " + codigo;
//        return conexao.executeUpdate( DELETE );
//    }
//
//
//    @Override
//    public Vector<String> getVector()
//    {
//        throw new UnsupportedOperationException( "Not supported yet." ); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    @Override
//    public Object findById( int codigo )
//    {
//
//        String FIND__BY_CODIGO = "SELECT * FROM alojamento WHERE pk_alojamento = " + codigo;
//        ResultSet result = conexao.executeQuery( FIND__BY_CODIGO );
//        Alojamento alojamento = null;
//        try
//        {
//
//            if ( result.next() )
//            {
//                alojamento = new Alojamento();
//                alojamento.setFkUsuario( new TbUsuario( result.getInt( "fk_usuario" ) ) );
//                alojamento.setTotalImposto( result.getBigDecimal( "total_imposto" ) );
//                alojamento.setTotalDesconto( result.getBigDecimal( "total_desconto" ) );
//                alojamento.setTotalIliquido( result.getBigDecimal( "total_iliquido" ) );
//                alojamento.setTotalLiquido( result.getBigDecimal( "total_liquido" ) );
//                alojamento.setEstadoDiaria( result.getString( "estado_diaria" ) );
//                alojamento.setEstadoConsumo( result.getString( "estado_consumo" ) );
//                alojamento.setData( result.getDate( "data" ) );
//
//            }
//
//        }
//        catch ( SQLException e )
//        {
//            e.printStackTrace();
//        }
//        return alojamento;
//
//    }
//
//    public Alojamento getLastAlojamento()
//    {
//
//        String FIND__BY_CODIGO = "SELECT MAX(idAlojamento) as maximo_id, a.*  FROM alojamento a WHERE idAlojamento = (SELECT MAX(idAlojamento) as maximo_id FROM alojamento a)";
//        ResultSet result = conexao.executeQuery( FIND__BY_CODIGO );
//        Alojamento alojamento = null;
//        try
//        {
//
//            if ( result.next() )
//            {
//                alojamento = new Alojamento();
//                alojamento.setIdAlojamento( result.getLong( "maximo_id" ) );
//                alojamento.setFkUsuario( new TbUsuario( result.getInt( "fk_usuario" ) ) );
//                alojamento.setTotalImposto( result.getBigDecimal( "total_imposto" ) );
//                alojamento.setTotalDesconto( result.getBigDecimal( "total_desconto" ) );
//                alojamento.setTotalIliquido( result.getBigDecimal( "total_iliquido" ) );
//                alojamento.setTotalLiquido( result.getBigDecimal( "total_liquido" ) );
//                alojamento.setEstadoDiaria( result.getString( "estado_diaria" ) );
//                alojamento.setEstadoConsumo( result.getString( "estado_consumo" ) );
//                alojamento.setData( result.getDate( "data" ) );
//
//            }
//
//        }
//        catch ( SQLException e )
//        {
//            e.printStackTrace();
//        }
//        return alojamento;
//
//    }
    
    
//    public boolean alterar_item_pedidos( int fk_pedidos )
//    {
////        String sql = "UPDATE tb_item_pedidos SET estado_quarto = '" + estado + "' WHERE designacao = '" + desiganacao + "'";
//        String sql = "UPDATE tb_item_pedidos SET fk_pedidos = '" + fk_pedidos + "' WHERE designacao = '" + desiganacao + "'";
//        return conexao.executeUpdate( sql );
//
//    }

    @Override
    public List<?> listarTodos()
    {
        throw new UnsupportedOperationException( "Not supported yet." ); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean salvar( Object object )
    {
        throw new UnsupportedOperationException( "Not supported yet." ); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean actualizar( Object object )
    {
        throw new UnsupportedOperationException( "Not supported yet." ); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean eliminar( int codigo )
    {
        throw new UnsupportedOperationException( "Not supported yet." ); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object findById( int codigo )
    {
        throw new UnsupportedOperationException( "Not supported yet." ); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Vector<String> getVector()
    {
        throw new UnsupportedOperationException( "Not supported yet." ); //To change body of generated methods, choose Tools | Templates.
    }

}
