/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comercial.controller;


import java.sql.Connection;
import entity.Grupo;
import entity.LinhaTransferencia;
import entity.Modelo;
import entity.TbFornecedor;
import entity.TbLocal;
import entity.TbProduto;
import entity.TbTipoProduto;
import entity.TransferenciaArmazem;
import entity.Unidade;
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
public class LinhasTransferenciasController implements EntidadeFactory
{

    private BDConexao conexao;

    public LinhasTransferenciasController( BDConexao conexao )
    {
        this.conexao = conexao;
    }

    @Override
//    public boolean salvar( Object object )
//    {
//        LinhaTransferencia linhaTransferencia = ( LinhaTransferencia ) object;
//        String INSERT = "INSERT INTO linha_transferencia( qtd_before, qtd, qtd_after, fk_armazem_origem, armazem_origem, fk_armazem_destino, "
//                + " armazem_destino , fk_produto, produto,   fk_transferencia_armazem "
//                + ")"
//                + " VALUES("
//                + linhaTransferencia.getQtdBefore() + " , "
//                + linhaTransferencia.getQtd() + " , "
//                + linhaTransferencia.getQtdAfter() + " , "
//                + linhaTransferencia.getFkArmazemOrigem() + " , "
//                + "'" + linhaTransferencia.getArmazemOrigem() + "' , "
//                + linhaTransferencia.getFkArmazemDestino() + " , "
//                + linhaTransferencia.getFkProduto() + " , "
//                + "'" + linhaTransferencia.getProduto() + "' , "
//                + linhaTransferencia.getFkTransferenciaArmazem().getPkTransferenciaArmazem() + ""
//                + " ) ";
////                + "'" + linhaTransferencia.getArmazemDestino() + "' , "
////                + linhaTransferencia.getFkTransferenciaArmazem().getPkTransferenciaArmazem()+ ""
////                + " ) ";
//
//        return conexao.executeUpdate( INSERT );
//
//    }

    public boolean salvar( Object object )
    {
        LinhaTransferencia linhaTransferencia = ( LinhaTransferencia ) object;
        String INSERT = "INSERT INTO linha_transferencia( qtd_before, qtd, qtd_after, fk_armazem_origem, armazem_origem, fk_armazem_destino, "
                + " armazem_destino ,  fk_produto, produto,   fk_transferencia_armazem "
                + ")"
                + " VALUES("
                + linhaTransferencia.getQtdBefore() + " , "
                + linhaTransferencia.getQtd() + " , "
                + linhaTransferencia.getQtdAfter() + " , "
                + linhaTransferencia.getFkArmazemOrigem() + " , "
                + "'" + linhaTransferencia.getArmazemOrigem() + "' , "
                + linhaTransferencia.getFkArmazemDestino() + " , "
                + "'" + linhaTransferencia.getArmazemDestino() + "' , "
                + linhaTransferencia.getFkProduto() + " , "
                + "'" + linhaTransferencia.getProduto() + "' , "
                + linhaTransferencia.getFkTransferenciaArmazem().getPkTransferenciaArmazem() + ""
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
        String DELETE = "DELETE FROM linha_transferencia WHERE codigo = " + codigo;
        return conexao.executeUpdate( DELETE );
    }

    @Override
    public List<LinhaTransferencia> listarTodos()
    {

        String FIND_ALL = "SELECT * FROM linha_transferencia ORDER BY codigo ASC";
        ResultSet result = conexao.executeQuery( FIND_ALL );
        List<LinhaTransferencia> lista_linhaTransferencia = new ArrayList<>();
        LinhaTransferencia linhaTransferencia;
        try
        {

            while ( result.next() )
            {

                linhaTransferencia = new LinhaTransferencia();
                linhaTransferencia.setPkLinhaTransferencia( result.getInt( "codigo" ) );
                linhaTransferencia.setQtdBefore( result.getDouble( "qtd_before" ) );
                linhaTransferencia.setQtd( result.getDouble( "qtd" ) );
                linhaTransferencia.setQtdAfter( result.getDouble( "qtd_after" ) );
                linhaTransferencia.setFkArmazemOrigem( result.getInt( "fk_armazem_origem" ) );
                linhaTransferencia.setArmazemOrigem( result.getString( "armazem_origem" ) );
                linhaTransferencia.setFkArmazemDestino( result.getInt( "fk_armazem_destino" ) );
                linhaTransferencia.setArmazemDestino( result.getString( "armazem_destino" ) );
                linhaTransferencia.setFkProduto( result.getInt( "fk_produto" ) );
                linhaTransferencia.setProduto( result.getString( "produto" ) );
                linhaTransferencia.setFkTransferenciaArmazem( new TransferenciaArmazem( result.getInt( "fk_transferencia_armazem" ) ) );

                lista_linhaTransferencia.add( linhaTransferencia );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return lista_linhaTransferencia;
    }

    @Override
    public Vector<String> getVector()
    {
        String FIND_ALL = "SELECT designacao FROM linha_transferencia ORDER BY codigo ASC";
        ResultSet result = conexao.executeQuery( FIND_ALL );
        Vector<String> lista_mesas = new Vector<>();
        try
        {
            while ( result.next() )
            {
                lista_mesas.add( result.getString( "designacao" ) );
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return lista_mesas;

    }

    @Override
    public Object findById( int codigo )
    {

        String FIND__BY_CODIGO = "SELECT * FROM linha_transferencia WHERE codigo = " + codigo;
        ResultSet result = conexao.executeQuery( FIND__BY_CODIGO );
        LinhaTransferencia linhaTransferencia = null;
        try
        {

            if ( result.next() )
            {
                linhaTransferencia = new LinhaTransferencia();
                linhaTransferencia.setPkLinhaTransferencia( result.getInt( "codigo" ) );
                linhaTransferencia.setQtdBefore( result.getDouble( "qtd_before" ) );
                linhaTransferencia.setQtd( result.getDouble( "qtd" ) );
                linhaTransferencia.setQtdAfter( result.getDouble( "qtd_after" ) );
                linhaTransferencia.setFkArmazemOrigem( result.getInt( "fk_armazem_origem" ) );
                linhaTransferencia.setArmazemOrigem( result.getString( "armazem_origem" ) );
                linhaTransferencia.setFkArmazemDestino( result.getInt( "fk_armazem_destino" ) );
                linhaTransferencia.setArmazemDestino( result.getString( "armazem_destino" ) );
                linhaTransferencia.setFkProduto( result.getInt( "fk_produto" ) );
                linhaTransferencia.setProduto( result.getString( "produto" ) );
                linhaTransferencia.setFkTransferenciaArmazem( new TransferenciaArmazem( result.getInt( "fk_transferencia_armazem" ) ) );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return linhaTransferencia;

    }

    public LinhaTransferencia findByCodBarra( String codBarra )
    {

        String FIND_BY_COD_BARRA = "SELECT * FROM linha_transferencia WHERE codBarra = '" + codBarra + "'";
        ResultSet result = conexao.executeQuery( FIND_BY_COD_BARRA );
        LinhaTransferencia linhaTransferencia = null;
        try
        {

            if ( result.next() )
            {
                linhaTransferencia = new LinhaTransferencia();
                linhaTransferencia.setPkLinhaTransferencia( result.getInt( "codigo" ) );
                linhaTransferencia.setQtdBefore( result.getDouble( "qtd_before" ) );
                linhaTransferencia.setQtd( result.getDouble( "qtd" ) );
                linhaTransferencia.setQtdAfter( result.getDouble( "qtd_after" ) );
                linhaTransferencia.setFkArmazemOrigem( result.getInt( "fk_armazem_origem" ) );
                linhaTransferencia.setArmazemOrigem( result.getString( "armazem_origem" ) );
                linhaTransferencia.setFkArmazemDestino( result.getInt( "fk_armazem_destino" ) );
                linhaTransferencia.setArmazemDestino( result.getString( "armazem_destino" ) );
                linhaTransferencia.setFkProduto( result.getInt( "fk_produto" ) );
                linhaTransferencia.setProduto( result.getString( "produto" ) );
                linhaTransferencia.setFkTransferenciaArmazem( new TransferenciaArmazem( result.getInt( "fk_transferencia_armazem" ) ) );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return linhaTransferencia;

    }

    public LinhaTransferencia findByDesignacao( String designacao )
    {

        String FIND__BY_CODIGO = "SELECT * FROM linha_transferencia WHERE designacao = '" + designacao + "'";
        ResultSet result = conexao.executeQuery( FIND__BY_CODIGO );
        LinhaTransferencia linhaTransferencia = null;
        try
        {

            if ( result.next() )
            {
                linhaTransferencia = new LinhaTransferencia();
                linhaTransferencia.setPkLinhaTransferencia( result.getInt( "codigo" ) );
                linhaTransferencia.setQtdBefore( result.getDouble( "qtd_before" ) );
                linhaTransferencia.setQtd( result.getDouble( "qtd" ) );
                linhaTransferencia.setQtdAfter( result.getDouble( "qtd_after" ) );
                linhaTransferencia.setFkArmazemOrigem( result.getInt( "fk_armazem_origem" ) );
                linhaTransferencia.setArmazemOrigem( result.getString( "armazem_origem" ) );
                linhaTransferencia.setFkArmazemDestino( result.getInt( "fk_armazem_destino" ) );
                linhaTransferencia.setArmazemDestino( result.getString( "armazem_destino" ) );
                linhaTransferencia.setFkProduto( result.getInt( "fk_produto" ) );
                linhaTransferencia.setProduto( result.getString( "produto" ) );
                linhaTransferencia.setFkTransferenciaArmazem( new TransferenciaArmazem( result.getInt( "fk_transferencia_armazem" ) ) );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return linhaTransferencia;

    }

    public LinhaTransferencia getLastProduto()
    {

        String FIND__BY_CODIGO = "SELECT MAX(codigo) as maximo_id, v.*  FROM linha_transferencia v";
        ResultSet result = conexao.executeQuery( FIND__BY_CODIGO );
        LinhaTransferencia linhaTransferencia = null;
        try
        {

            if ( result.next() )
            {
                linhaTransferencia = new LinhaTransferencia();
                linhaTransferencia.setPkLinhaTransferencia( result.getInt( "codigo" ) );
                linhaTransferencia.setQtdBefore( result.getDouble( "qtd_before" ) );
                linhaTransferencia.setQtd( result.getDouble( "qtd" ) );
                linhaTransferencia.setQtdAfter( result.getDouble( "qtd_after" ) );
                linhaTransferencia.setFkArmazemOrigem( result.getInt( "fk_armazem_origem" ) );
                linhaTransferencia.setArmazemOrigem( result.getString( "armazem_origem" ) );
                linhaTransferencia.setFkArmazemDestino( result.getInt( "fk_armazem_destino" ) );
                linhaTransferencia.setArmazemDestino( result.getString( "armazem_destino" ) );
                linhaTransferencia.setFkProduto( result.getInt( "fk_produto" ) );
                linhaTransferencia.setProduto( result.getString( "produto" ) );
                linhaTransferencia.setFkTransferenciaArmazem( new TransferenciaArmazem( result.getInt( "fk_transferencia_armazem" ) ) );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return linhaTransferencia;

    }

    public Vector<String> getVectorByIdTipoProduto( int idTipoProduto )
    {
        String FIND_ALL = "SELECT designacao FROM linha_transferencia WHERE  cod_Tipo_Produto = " + idTipoProduto + " ORDER BY designacao ASC";
        ResultSet result = conexao.executeQuery( FIND_ALL );
        Vector<String> lista_mesas = new Vector<>();
        try
        {
            while ( result.next() )
            {
                lista_mesas.add( result.getString( "designacao" ) );
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return lista_mesas;

    }

}
