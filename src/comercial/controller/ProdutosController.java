/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comercial.controller;

import entity.Grupo;
import entity.Modelo;
import entity.TbFornecedor;
import entity.TbLocal;
import entity.TbProduto;
import entity.TbTipoProduto;
import entity.Unidade;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Vector;
import util.BDConexao;
import util.DVML;
import util.MetodosUtil;

/**
 *
 * @author Martinho Luis & Domingos Dala Vunge
 */
public class ProdutosController implements EntidadeFactory
{

    private BDConexao conexao;

    public ProdutosController( BDConexao conexao )
    {
        this.conexao = conexao;
    }

    @Override
    public boolean salvar( Object object )
    {

        TbProduto produto = (TbProduto) object;
        PreparedStatement stmt;
        System.out.println( "UNIDADE CONTROLLER: " + produto.getCodUnidade() );
        String INSERT = "INSERT INTO tb_produto( "
                + " designacao, preco, data_fabrico, data_expiracao, "
                + " codBarra, status, data_entrada, stocavel, "
                + " preco_venda, "
                + " quantidade_desconto , codigo_manual , cod_unidade , cod_local , "
                + " cod_fornecedores , cod_Tipo_Produto , fk_modelo , fk_grupo , "
                + " photo , status_iva , cozinha , unidade_compra "
                + ")"
                + " VALUES("
                + " ? , ?, ? , ?, "
                + " ? , ?, ? , ?, "
                + " ? , ?, ? , ?, "
                + " ? , ?, ? , ?, "
                + " ? , ?, ?, ?, ? "
                //                + " ?"
                + " ) ";

        try
        {

            stmt = BDConexao.getConnection().prepareStatement( INSERT );

            int cod = 1;
            stmt.setString( cod++, produto.getDesignacao() );
            stmt.setBigDecimal( cod++, produto.getPreco() );
            stmt.setString( cod++, MetodosUtil.getDataBanco( produto.getDataFabrico() ) );
            stmt.setString( cod++, MetodosUtil.getDataBanco( produto.getDataExpiracao() ) );

            stmt.setString( cod++, produto.getCodBarra() );
            stmt.setString( cod++, produto.getStatus() );
            stmt.setString( cod++, MetodosUtil.getDataBanco( produto.getDataEntrada() ) );
            stmt.setString( cod++, produto.getStocavel() );

            stmt.setDouble( cod++, produto.getPrecoVenda() );
//            stmt.setDouble( cod++, produto.getPercentagemDesconto() );
            stmt.setInt( cod++, produto.getQuantidadeDesconto() );
            stmt.setString( cod++, produto.getCodigoManual() );

            stmt.setInt( cod++, produto.getCodUnidade().getPkUnidade() );
            stmt.setInt( cod++, produto.getCodLocal().getCodigo() );
            stmt.setInt( cod++, produto.getCodFornecedores().getCodigo() );
            stmt.setInt( cod++, produto.getCodTipoProduto().getCodigo() );

            stmt.setInt( cod++, produto.getFkModelo().getPkModelo() );
            stmt.setInt( cod++, produto.getFkGrupo().getPkGrupo() );
            stmt.setBytes( cod++, produto.getPhoto() );

            stmt.setString( cod++, produto.getStatusIva() );
            stmt.setString( cod++, produto.getCozinha() );
            stmt.setDouble( cod++, produto.getUnidadeCompra() );
            stmt.executeUpdate();
            stmt.close();
            return true;

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return false;

    }
//    public boolean salvar( Object object )
//    {
//
//        TbProduto produto = ( TbProduto ) object;
//        System.out.println( "UNIDADE CONTROLLER: " + produto.getCodUnidade() );
//        String INSERT = "INSERT INTO tb_produto( "
//                + " designacao, preco, data_fabrico, data_expiracao, "
//                + " codBarra, status, data_entrada, stocavel, "
//                + " preco_venda, percentagem_desconto, "
//                + " quantidade_desconto , codigo_manual , cod_unidade , cod_local  ,"
//                + "  cod_fornecedores , cod_Tipo_Produto , fk_modelo , fk_grupo ,"
//                + " photo , status_iva , cozinha "
//                + ")"
//                + ""
//                + ""
//                + " VALUES("
//                + "'" + produto.getDesignacao() + "' , "
//                + produto.getPreco() + " , "
//                + "'" + MetodosUtil.getDataBanco( produto.getDataFabrico() ) + "' , "
//                + "'" + MetodosUtil.getDataBanco( produto.getDataExpiracao() ) + "' , "
//                + "'" + produto.getCodBarra() + "' , "
//                + "'" + produto.getStatus() + "' , "
//                + "'" + MetodosUtil.getDataBanco( produto.getDataEntrada() ) + "' , "
//                + "'" + produto.getStocavel() + "' , "
//                + produto.getPrecoVenda() + " , "
//                + produto.getPercentagemDesconto() + " , "
//                + "'" + produto.getQuantidadeDesconto() + "' , "
//                + "'" + produto.getCodigoManual() + "' , "
//                + produto.getCodUnidade().getPkUnidade() + " , "
//                + produto.getCodLocal().getCodigo() + " , "
//                + produto.getCodFornecedores().getCodigo() + " , "
//                + produto.getCodTipoProduto().getCodigo() + " , "
//                + produto.getFkModelo().getPkModelo() + " , "
//                + produto.getFkGrupo().getPkGrupo() + " , "
////                + produto.getPhoto() + " , "
//                + "'" + produto.getStatusIva() + "' , "
//                + "'" + produto.getPhoto() + "' , "
//                + "'" + produto.getCozinha() + "'  "
//                + " ) ";
//
//        return conexao.executeUpdate( INSERT );
//
//    }

    public int getIdProduto( String designacao )
    {
        String FIND_BY_CODIGO = "SELECT codigo AS id FROM tb_produto WHERE designacao = '" + designacao + "'";
        ResultSet result = conexao.executeQuery( FIND_BY_CODIGO );
        try
        {
            if ( result.next() )
            {
                return result.getInt( "id" );

            }
        }
        catch ( SQLException e )
        {
//            e.printStackTrace();
        }
        return 0;

    }

    public boolean desactivar( TbProduto produto )
    {
        String sql = "UPDATE tb_produto SET status = ? WHERE codigo = ?";

        try
        {
            PreparedStatement stmt = BDConexao.conectar().prepareStatement( sql );
            stmt.setString( 1, produto.getStatus() );
            stmt.setInt( 2, produto.getCodigo() );
            stmt.executeUpdate();
            stmt.close();
            return true;
        }
        catch ( SQLException e )
        {
        }
        return false;

    }

    @Override
    public boolean actualizar( Object object )
    {
        TbProduto produto = (TbProduto) object;
        PreparedStatement stmt;
        String UPDATE = "UPDATE tb_produto SET "
                + " designacao = ?, "
                + " preco = ? ,   "
                + " data_fabrico = ? , "
                + " data_expiracao = ?,  "
                + " codBarra = ? , "
                + " status = ? , "
                + " data_entrada = ?  , "
                + " stocavel = ?, "
                + " preco_venda = ? , "
                + " percentagem_desconto = ?, "
                + " quantidade_desconto = ?, "
                + " codigo_manual = ?, "
                + " cod_unidade = ?, "
                + " cod_local = ?, "
                + " cod_fornecedores = ? , "
                + " cod_Tipo_Produto = ? ,  "
                + " fk_modelo = ? ,  "
                + " fk_grupo = ? , "
                + " photo = ?, "
                + " status_iva = ? , "
                + " cozinha = ? , "
                + " unidade_compra = ? "
                + " WHERE codigo = ?";
        try
        {
            System.out.println( UPDATE );

            stmt = conexao.getConnection().prepareStatement( UPDATE );

            int cod = 1;
            stmt.setString( cod++, produto.getDesignacao() );
            stmt.setBigDecimal( cod++, produto.getPreco() );
            stmt.setString( cod++, MetodosUtil.getDataBanco( produto.getDataFabrico() ) );
            stmt.setString( cod++, MetodosUtil.getDataBanco( produto.getDataExpiracao() ) );
            stmt.setString( cod++, produto.getCodBarra() );
            stmt.setString( cod++, produto.getStatus() );
            stmt.setString( cod++, MetodosUtil.getDataBanco( produto.getDataEntrada() ) );
            stmt.setString( cod++, produto.getStocavel() );
            stmt.setDouble( cod++, produto.getPrecoVenda() );
            stmt.setDouble( cod++, produto.getPercentagemDesconto() );
            stmt.setInt( cod++, produto.getQuantidadeDesconto() );
            stmt.setString( cod++, produto.getCodigoManual() );
            stmt.setInt( cod++, produto.getCodUnidade().getPkUnidade() );
            stmt.setInt( cod++, produto.getCodLocal().getCodigo() );
            stmt.setInt( cod++, produto.getCodFornecedores().getCodigo() );
            stmt.setInt( cod++, produto.getCodTipoProduto().getCodigo() );
            stmt.setInt( cod++, produto.getFkModelo().getPkModelo() );
            stmt.setInt( cod++, produto.getFkGrupo().getPkGrupo() );
            stmt.setBytes( cod++, produto.getPhoto() );
            stmt.setString( cod++, produto.getStatusIva() );
            stmt.setString( cod++, produto.getCozinha() );
            stmt.setDouble( cod++, produto.getUnidadeCompra() );
            stmt.setInt( cod++, produto.getCodigo() );
            stmt.executeUpdate();
            stmt.close();
            return true;

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return false;

    }

    @Override
    public boolean eliminar( int codigo )
    {
        String DELETE = "DELETE FROM tb_produto WHERE codigo = " + codigo;
        return conexao.executeUpdate( DELETE );
    }

    @Override
    public List<TbProduto> listarTodos()
    {

        String FIND_ALL = "SELECT * FROM tb_produto ORDER BY codigo ASC";
        ResultSet result = conexao.executeQuery( FIND_ALL );
        List<TbProduto> lista_produto = new ArrayList<>();
        TbProduto produto;
        try
        {

            while ( result.next() )
            {
                produto = new TbProduto();
                produto.setCodigo( result.getInt( "maximo_id" ) );
                produto.setDesignacao( result.getString( "designacao" ) );
                produto.setPreco( result.getBigDecimal( "preco" ) );
                produto.setDataFabrico( result.getDate( "data_fabrico" ) );
                produto.setDataExpiracao( result.getDate( "data_expiracao" ) );
                produto.setCodBarra( result.getString( "codBarra" ) );
                produto.setStatus( result.getString( "status" ) );
                produto.setDataEntrada( result.getDate( "data_entrada" ) );
                produto.setStocavel( result.getString( "stocavel" ) );
                produto.setPrecoVenda( result.getDouble( "preco_venda" ) );
                produto.setPercentagemDesconto( result.getDouble( "percentagem_desconto" ) );
                produto.setQuantidadeDesconto( result.getInt( "quantidade_desconto" ) );
                produto.setCodigoManual( result.getString( "codigo_manual" ) );
                produto.setCodUnidade( new Unidade( result.getInt( "cod_unidade" ) ) );
                produto.setCodLocal( new TbLocal( result.getInt( "cod_local" ) ) );
                produto.setCodFornecedores( new TbFornecedor( result.getInt( "cod_fornecedores" ) ) );
                produto.setCodTipoProduto( new TbTipoProduto( result.getInt( "cod_Tipo_Produto" ) ) );
                produto.setFkModelo( new Modelo( result.getInt( "fk_modelo" ) ) );
                produto.setFkGrupo( new Grupo( result.getInt( "fk_grupo" ) ) );
                produto.setPhoto( result.getBytes( "photo" ) );
                produto.setStatusIva( result.getString( "status_iva" ) );
                produto.setCozinha( result.getString( "cozinha" ) );
                produto.setUnidadeCompra( result.getDouble( "unidade_compra" ) );

                lista_produto.add( produto );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return lista_produto;
    }

    @Override
    public Vector<String> getVector()
    {
        String FIND_ALL = "SELECT designacao FROM tb_produto ORDER BY codigo ASC";
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
    public Vector<String> getVectorStocavel()
    {
        String FIND_ALL = "SELECT designacao FROM tb_produto WHERE stocavel = true ORDER BY codigo ASC";
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
    public Vector<String> getVectorDesignacao()
    {
        String FIND_ALL = "SELECT designacao FROM tb_produto ORDER BY designacao ASC";
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

    public Vector<String> getVectorRefeicao()
    {
        String FIND_ALL = "SELECT designacao FROM tb_produto WHERE fk_grupo = 3 ORDER BY codigo ASC";
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

    public Vector<TbProduto> getProdutosByTipoProduto( int idTipoProduto )
    {
        System.out.println( "D:SERVICO" );
        System.out.println( "ID_TIPO_PRODUTO: " + idTipoProduto );
        String FIND_ALL = "SELECT * FROM tb_produto WHERE cod_Tipo_Produto = " + idTipoProduto + " ORDER BY codigo ASC";
        ResultSet result = conexao.executeQuery( FIND_ALL );
        TbProduto produto;
        Vector<TbProduto> lista_produtos = new Vector<>();
        try
        {
            while ( result.next() )
            {
                produto = new TbProduto();
                setResultProduto( result, produto );
                lista_produtos.add( produto );
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return lista_produtos;
    }

    public Vector<TbProduto> getProdutosByUnidade( int idUnidade )
    {

        String FIND_ALL = "SELECT * FROM tb_produto WHERE cod_unidade = " + idUnidade + " ORDER BY codigo ASC";
        ResultSet result = conexao.executeQuery( FIND_ALL );
        TbProduto produto;
        Vector<TbProduto> lista_produtos = new Vector<>();
        try
        {
            while ( result.next() )
            {
                produto = new TbProduto();
                setResultProduto( result, produto );
                lista_produtos.add( produto );
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return lista_produtos;
    }

    public Vector<TbProduto> getAllProdutosAssociados( int idProduto )
    {
        System.out.println( "D:SERVICO" );
        System.out.println( "ID_TIPO_PRODUTO: " + idProduto );
        String FIND_ALL = "SELECT * FROM tb_produto WHERE cod_pai = " + idProduto;
        System.out.println( FIND_ALL );
        ResultSet result = conexao.executeQuery( FIND_ALL );
        TbProduto produto;
        Vector<TbProduto> lista_produtos = new Vector<>();
        try
        {
            while ( result.next() )
            {
                produto = new TbProduto();
                setResultProduto( result, produto );
                lista_produtos.add( produto );
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return lista_produtos;
    }

    public Vector<TbProduto> getProdutosByTipoProdutoAndIdGrupo( int idTipoProduto, int idGrupo )
    {
        System.out.println( "D:SERVICO" );
        System.out.println( "ID_TIPO_PRODUTO: " + idTipoProduto );
        String FIND_ALL = "SELECT * FROM tb_produto "
                + " WHERE cod_Tipo_Produto = " + idTipoProduto
                + " AND fk_grupo = " + idGrupo
                + " ORDER BY codigo ASC";
        ResultSet result = conexao.executeQuery( FIND_ALL );
        TbProduto produto;
        Vector<TbProduto> lista_produtos = new Vector<>();
        try
        {
            while ( result.next() )
            {
                produto = new TbProduto();
                setResultProduto( result, produto );
                lista_produtos.add( produto );
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return lista_produtos;
    }

    public String getDescricaoById( int codigo )
    {
        String FIND_BY_CODIGO = "SELECT designacao AS designacao FROM tb_produto WHERE codigo = " + codigo;
        ResultSet result = conexao.executeQuery( FIND_BY_CODIGO );
        try
        {
            if ( result.next() )
            {
                return result.getString( "designacao" );

            }
        }
        catch ( SQLException e )
        {
//            e.printStackTrace();
        }
        return "";

    }

    public TbProduto findByCod( int codigo )
    {

        String FIND__BY_CODIGO = "SELECT * FROM tb_produto WHERE codigo = " + codigo + "";
        ResultSet result = conexao.executeQuery( FIND__BY_CODIGO );
        TbProduto produto = null;

        try
        {
            if ( result.next() )
            {
                produto = new TbProduto();
                setResultProduto( result, produto );
            }
        }
        catch ( SQLException e )
        {
        }
        return produto;

    }

    @Override
    public Object findById( int codigo )
    {

        String sql = "SELECT * FROM tb_produto WHERE codigo = " + codigo;
        ResultSet result = conexao.executeQuery( sql );
        TbProduto produto = null;

        try
        {
            if ( result.next() )
            {
                produto = new TbProduto();
                setResultProduto( result, produto );
            }
        }
        catch ( SQLException e )
        {
        }
        return produto;

    }

    public Object findByIdCod( int codigo )
    {

        String sql = "SELECT * FROM tb_produto WHERE fk_grupo = " + DVML.COD_GRUPO_INGREDIENTES + " AND codigo = " + codigo;
        ResultSet result = conexao.executeQuery( sql );
        TbProduto produto = null;

        try
        {
            if ( result.next() )
            {
                produto = new TbProduto();
                setResultProduto( result, produto );
            }
        }
        catch ( SQLException e )
        {
        }
        return produto;

    }

    public Object findByIdUnid( int codUnidade )
    {

        String sql = "SELECT p.cod_unidade FROM tb_produto p WHERE p.cod_unidade = " + codUnidade;
        ResultSet result = conexao.executeQuery( sql );
        TbProduto produto = null;

        try
        {
            if ( result.next() )
            {
                produto = new TbProduto();
                setResultProduto( result, produto );
            }
        }
        catch ( SQLException e )
        {
        }
        return produto;

    }

    public TbProduto findByCodBarra( String codBarra )
    {

        String FIND_BY_COD_BARRA = "SELECT * FROM tb_produto WHERE codBarra = '" + codBarra + "'";
        ResultSet result = conexao.executeQuery( FIND_BY_COD_BARRA );
        TbProduto produto = null;

        try
        {
            if ( result.next() )
            {
                produto = new TbProduto();
                setResultProduto( result, produto );
            }
        }
        catch ( SQLException e )
        {
        }
        return produto;

    }
    
    public TbProduto findByCodManual1( String codigo_manual )
    {

        String FIND_BY_COD_BARRA = "SELECT * FROM tb_produto WHERE codigo_manual = '" + codigo_manual + "'";
        ResultSet result = conexao.executeQuery( FIND_BY_COD_BARRA );
        TbProduto produto = null;

        try
        {
            if ( result.next() )
            {
                produto = new TbProduto();
                setResultProduto( result, produto );
            }
        }
        catch ( SQLException e )
        {
        }
        return produto;

    }

    public TbProduto findByCodBarra1( int codBarra )
    {

        String FIND_BY_COD_BARRA = "SELECT * FROM tb_produto WHERE codBarra = " + codBarra + "";
        ResultSet result = conexao.executeQuery( FIND_BY_COD_BARRA );
        TbProduto produto = null;

        try
        {
            if ( result.next() )
            {
                produto = new TbProduto();
                setResultProduto( result, produto );
            }
        }
        catch ( SQLException e )
        {
        }
        return produto;

    }

    public TbProduto findByCodManual( String codManual )
    {

        String FIND_BY_COD_BARRA = "SELECT * FROM tb_produto WHERE codigo_manual = '" + codManual + "'";
        ResultSet result = conexao.executeQuery( FIND_BY_COD_BARRA );
        TbProduto produto = null;

        try
        {
            if ( result.next() )
            {
                produto = new TbProduto();
                setResultProduto( result, produto );
            }
        }
        catch ( SQLException e )
        {
        }
        return produto;

    }

    public TbProduto findByDesignacao( String designacao )
    {

        String FIND__BY_CODIGO = "SELECT * FROM tb_produto WHERE designacao = '" + designacao + "'";
        ResultSet result = conexao.executeQuery( FIND__BY_CODIGO );
        TbProduto produto = null;

        try
        {
            if ( result.next() )
            {
                produto = new TbProduto();
                setResultProduto( result, produto );
            }
        }
        catch ( SQLException e )
        {
        }
        return produto;

    }

    public TbProduto getLastProduto()
    {
        String LAST_ID = "SELECT * FROM tb_produto WHERE codigo = (SELECT MAX(codigo) as codigo FROM tb_produto)";
        ResultSet result = conexao.executeQuery( LAST_ID );
        TbProduto produto = null;

        try
        {
            if ( result.next() )
            {
                produto = new TbProduto();
                setResultProduto( result, produto );
            }
        }
        catch ( SQLException e )
        {
        }

        return produto;

    }

    private void setResultProduto( ResultSet result, TbProduto produto )
    {
        try
        {

            produto.setCodigo( result.getInt( "codigo" ) );
            produto.setDesignacao( result.getString( "designacao" ) );
            produto.setPreco( result.getBigDecimal( "preco" ) );
            produto.setDataFabrico( result.getDate( "data_fabrico" ) );
            produto.setDataExpiracao( result.getDate( "data_expiracao" ) );
            produto.setCodBarra( result.getString( "codBarra" ) );
            produto.setStatus( result.getString( "status" ) );
            produto.setDataEntrada( result.getDate( "data_entrada" ) );
            produto.setStocavel( result.getString( "stocavel" ) );
            produto.setPrecoVenda( result.getDouble( "preco_venda" ) );
            produto.setPercentagemDesconto( result.getDouble( "percentagem_desconto" ) );
            produto.setQuantidadeDesconto( result.getInt( "quantidade_desconto" ) );
            produto.setCodigoManual( result.getString( "codigo_manual" ) );
            produto.setCodUnidade( new Unidade( result.getInt( "cod_unidade" ) ) );
            produto.setCodLocal( new TbLocal( result.getInt( "cod_local" ) ) );
            produto.setCodFornecedores( new TbFornecedor( result.getInt( "cod_fornecedores" ) ) );
            produto.setCodTipoProduto( new TbTipoProduto( result.getInt( "cod_Tipo_Produto" ) ) );
            produto.setFkModelo( new Modelo( result.getInt( "fk_modelo" ) ) );
            produto.setFkGrupo( new Grupo( result.getInt( "fk_grupo" ) ) );
            produto.setPhoto( result.getBytes( "photo" ) );
            produto.setStatusIva( result.getString( "status_iva" ) );
            produto.setCozinha( result.getString( "cozinha" ) );
            produto.setUnidadeCompra( result.getDouble( "unidade_compra" ) );

        }
        catch ( SQLException e )
        {
        }

    }

    public Vector<String> getVectorByIdTipoProduto( int idTipoProduto )
    {
        String FIND_ALL = "SELECT designacao FROM tb_produto WHERE  cod_Tipo_Produto = " + idTipoProduto + " ORDER BY designacao ASC";
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

    public Vector<String> getVectorByIdTipoProdutoRefeicao( int idTipoProduto )
    {
        String FIND_ALL = "SELECT designacao FROM tb_produto WHERE fk_grupo = 3 AND cod_Tipo_Produto = " + idTipoProduto + " ORDER BY designacao ASC";
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

    public boolean existProdutoByCodigoBarraExcepto( int idProduto, String codBarra )
    {

        String sql = "SELECT p.codigo FROM tb_produto p WHERE p.codBarra = '" + codBarra + "' AND p.codigo <> " + idProduto;
        ResultSet rs = conexao.executeQuery( sql );

        try
        {
            return rs.next();
        }
        catch ( SQLException e )
        {
        }

        return false;
    }

    public boolean existProdutoByCodigoBarra( String codBarra )
    {
        String sql = "SELECT p.codigo FROM tb_produto p WHERE p.codBarra = '" + codBarra + "'";
        ResultSet rs = conexao.executeQuery( sql );

        try
        {
            return rs.next();
        }
        catch ( SQLException e )
        {
        }

        return false;
    }
    
//    public boolean existProdutoByCodigoManual( String codigo_manual )
//    {
//        String sql = "SELECT p.codigo FROM tb_produto p WHERE p.codigo_manual = " + codigo_manual + "";
//        ResultSet rs = conexao.executeQuery( sql );
//
//        try
//        {
//            return rs.next();
//        }
//        catch ( SQLException e )
//        {
//        }
//
//        return false;
//    }
    
    public boolean existProdutoByCodigoManual(String codigo_manual) {
    String sql = "SELECT 1 FROM tb_produto p WHERE p.codigo_manual = ?";
    try (PreparedStatement ps = conexao.getConnection().prepareStatement(sql)) {
        ps.setString(1, codigo_manual);
        try (ResultSet rs = ps.executeQuery()) {
            return rs.next(); // true se encontrou
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
}


    public boolean existProdutoByDesignacaoExcepto( int idProduto, String designacao )
    {
        String sql = "SELECT p.codigo FROM tb_produto p WHERE p.designacao = '" + designacao + "' AND p.codigo <> " + idProduto;
        ResultSet rs = conexao.executeQuery( sql );
        try
        {
            return rs.next();
        }
        catch ( SQLException e )
        {
        }

        return false;
    }

    public boolean exist_designacao_produto( String designacao )
    {
        String sql = "SELECT p.codigo FROM tb_produto p WHERE p.designacao = '" + designacao + "'";
        ResultSet rs = conexao.executeQuery( sql );
        try
        {
            return rs.next();
        }
        catch ( SQLException e )
        {
        }

        return false;
    }

    public Object findByName( String designacao )
    {

        String FIND_BY_CODIGO = "SELECT * FROM tb_produto WHERE designacao = '" + designacao + "' ";
        ResultSet result = conexao.executeQuery( FIND_BY_CODIGO );
        TbProduto produto = null;
        try
        {

            if ( result.next() )
            {
                produto = new TbProduto();
                produto.setCodigo( result.getInt( "codigo" ) );
                produto.setDesignacao( result.getString( "designacao" ) );
                produto.setPreco( result.getBigDecimal( "preco" ) );
                produto.setDataFabrico( result.getDate( "data_fabrico" ) );
                produto.setDataExpiracao( result.getDate( "data_expiracao" ) );
                produto.setCodBarra( result.getString( "codBarra" ) );
                produto.setStatus( result.getString( "status" ) );
                produto.setDataEntrada( result.getDate( "data_entrada" ) );
                produto.setStocavel( result.getString( "stocavel" ) );
                produto.setPrecoVenda( result.getDouble( "preco_venda" ) );
                produto.setPercentagemDesconto( result.getDouble( "percentagem_desconto" ) );
                produto.setQuantidadeDesconto( result.getInt( "quantidade_desconto" ) );
                produto.setCodigoManual( result.getString( "codigo_manual" ) );
                produto.setCodUnidade( new Unidade( result.getInt( "cod_unidade" ) ) );
                produto.setCodLocal( new TbLocal( result.getInt( "cod_local" ) ) );
                produto.setCodFornecedores( new TbFornecedor( result.getInt( "cod_fornecedores" ) ) );
                produto.setCodTipoProduto( new TbTipoProduto( result.getInt( "cod_Tipo_Produto" ) ) );
                produto.setFkModelo( new Modelo( result.getInt( "fk_modelo" ) ) );
                produto.setFkGrupo( new Grupo( result.getInt( "fk_grupo" ) ) );
                produto.setPhoto( result.getBytes( "photo" ) );
                produto.setStatusIva( result.getString( "status_iva" ) );
                produto.setCozinha( result.getString( "cozinha" ) );
                produto.setUnidadeCompra( result.getDouble( "unidade_compra" ) );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return produto;

    }

    public List<TbProduto> listarTodosByCodPai( int codPai )
    {
        String FIND_ALL = "SELECT * FROM tb_produto  WHERE cod_pai = " + codPai + "   ORDER BY codigo ASC";
        ResultSet result = conexao.executeQuery( FIND_ALL );
        List<TbProduto> lista_produto = new ArrayList<>();
        TbProduto produto;
        try
        {
            while ( result.next() )
            {
                produto = new TbProduto();
                produto.setCodigo( result.getInt( "codigo" ) );
                produto.setDesignacao( result.getString( "designacao" ) );
                produto.setPreco( result.getBigDecimal( "preco" ) );
                produto.setDataFabrico( result.getDate( "data_fabrico" ) );
                produto.setDataExpiracao( result.getDate( "data_expiracao" ) );
                produto.setCodBarra( result.getString( "codBarra" ) );
                produto.setStatus( result.getString( "status" ) );
                produto.setDataEntrada( result.getDate( "data_entrada" ) );
                produto.setStocavel( result.getString( "stocavel" ) );
                produto.setPrecoVenda( result.getDouble( "preco_venda" ) );
                produto.setPercentagemDesconto( result.getDouble( "percentagem_desconto" ) );
                produto.setQuantidadeDesconto( result.getInt( "quantidade_desconto" ) );
                produto.setCodigoManual( result.getString( "codigo_manual" ) );
                produto.setCodUnidade( new Unidade( result.getInt( "cod_unidade" ) ) );
                produto.setCodLocal( new TbLocal( result.getInt( "cod_local" ) ) );
                produto.setCodFornecedores( new TbFornecedor( result.getInt( "cod_fornecedores" ) ) );
                produto.setCodTipoProduto( new TbTipoProduto( result.getInt( "cod_Tipo_Produto" ) ) );
                produto.setFkModelo( new Modelo( result.getInt( "fk_modelo" ) ) );
                produto.setFkGrupo( new Grupo( result.getInt( "fk_grupo" ) ) );
                produto.setPhoto( result.getBytes( "photo" ) );
                produto.setStatusIva( result.getString( "status_iva" ) );
                produto.setCozinha( result.getString( "cozinha" ) );
                produto.setCodPai( result.getInt( "cod_pai" ) );
                produto.setUnidadeCompra( result.getDouble( "unidade_compra" ) );
                lista_produto.add( produto );
            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return lista_produto;
    }

    public boolean actualizarCodPai( int codigo, int codPai )
    {
        String UPDATE = "UPDATE  tb_produto SET cod_pai = " + codPai + " WHERE codigo = " + codigo;
        return conexao.executeUpdate( UPDATE );
    }

    public boolean eliminarCodPai( int codigo )
    {
        String UPDATE = "UPDATE  tb_produto SET cod_pai = 0 WHERE codigo = " + codigo;
        return conexao.executeUpdate( UPDATE );
    }

    public TbProduto getProdutoByDesignacao( String designacao )
    {
        String FIND_BY_CODIGO = "SELECT *  FROM tb_produto  WHERE designacao = '" + designacao + "'";
        ResultSet result = conexao.executeQuery( FIND_BY_CODIGO );
        TbProduto produtos = null;
        try
        {
            if ( result.next() )
            {
                produtos = new TbProduto();
                setResultProduto(   result, produtos );
            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return produtos;

    }

//    public List<Object[]> listarStockPorArmazem(Connection conn, int codArmazem) {
//        List<Object[]> lista = new ArrayList<>();
//
//        String sql = "SELECT p.codigo, p.designacao, tp.designacao AS tipo, " +
//                     "       pr.preco_compra, pr.preco_venda, " +
//                     "       s.quantidade_existente " +
//                     "FROM tb_produto p " +
//                     "JOIN tb_tipo_produto tp ON p.cod_Tipo_Produto = tp.codigo " +
//                     "JOIN tb_stock s ON s.cod_produto_codigo = p.codigo " +
//                     "JOIN tb_preco pr ON pr.fk_produto = p.codigo " +
//                     "WHERE s.cod_armazem = ? " +
//                     "  AND pr.data = ( " +
//                     "      SELECT MAX(p2.data) " +
//                     "      FROM tb_preco p2 " +
//                     "      WHERE p2.fk_produto = p.codigo " +
//                     "  ) " +
//                     "ORDER BY p.designacao ASC";
//
//        try (PreparedStatement pst = conn.prepareStatement(sql)) {
//            pst.setInt(1, codArmazem);
//            ResultSet rs = pst.executeQuery();
//
//            while (rs.next()) {
//                Object[] linha = new Object[]{
//                    rs.getInt("codigo"),
//                    rs.getString("designacao"),
//                    rs.getString("tipo"),
//                    rs.getBigDecimal("preco_compra"),
//                    rs.getBigDecimal("preco_venda"),
//                    rs.getDouble("quantidade_existente"),
//                    0.0, // quantidade de acerto
//                    rs.getDouble("quantidade_existente") // quantidade final
//                };
//                lista.add(linha);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return lista;
//    }
    
    public List<Object[]> listarStockPorArmazem(Connection conn, int codArmazem) {
    List<Object[]> lista = new ArrayList<>();

    String sql =
        "SELECT p.codigo, p.designacao, tp.designacao AS tipo_produto, " +
        "       (SELECT AVG(pr.preco_compra) FROM tb_preco pr WHERE pr.fk_produto = p.codigo) AS preco_medio_compra, " +
        "       (SELECT pr2.preco_venda FROM tb_preco pr2 WHERE pr2.fk_produto = p.codigo " +
        "            ORDER BY pr2.data DESC, pr2.hora DESC LIMIT 1) AS preco_venda_atual, " +
        "       (SELECT COALESCE(SUM(s.quantidade_existente),0) FROM tb_stock s " +
        "            WHERE s.cod_produto_codigo = p.codigo AND s.cod_armazem = ?) AS qtd_existente " +
        "FROM tb_produto p " +
        "JOIN tb_tipo_produto tp ON tp.codigo = p.cod_Tipo_Produto " +
        "WHERE EXISTS (SELECT 1 FROM tb_stock s2 WHERE s2.cod_produto_codigo = p.codigo AND s2.cod_armazem = ?) " +
        "ORDER BY p.designacao ASC";

    try (PreparedStatement pst = conn.prepareStatement(sql)) {
        pst.setInt(1, codArmazem);
        pst.setInt(2, codArmazem);

        try (ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                Object[] linha = new Object[] {
                    rs.getInt("codigo"),
                    rs.getString("designacao"),
                    rs.getString("tipo_produto"),
                    rs.getBigDecimal("preco_medio_compra"),
                    rs.getBigDecimal("preco_venda_atual"),
                    rs.getDouble("qtd_existente"),
                    0.0,                         // qtd acerto (editável)
                    rs.getDouble("qtd_existente")// existência total inicial
                };
                lista.add(linha);
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return lista;
}


//    public List<Object[]> listarStockPorArmazem(Connection conn, int codArmazem) {
//    List<Object[]> lista = new ArrayList<>();
//
//    String sql = "SELECT p.codigo, "
//               + "       p.designacao, "
//               + "       t.designacao AS tipo_produto, "
//               + "       COALESCE(AVG(pr.preco_compra), 0) AS preco_medio_compra, "
//               + "       (SELECT pr2.preco_venda "
//               + "        FROM tb_preco pr2 "
//               + "        WHERE pr2.fk_produto = p.codigo "
//               + "        ORDER BY pr2.data DESC, pr2.hora DESC "
//               + "        LIMIT 1) AS preco_venda_atual, "
//               + "       SUM(s.quantidade_existente) AS qtd_existente "
//               + "FROM tb_stock s "
//               + "JOIN tb_produto p ON p.codigo = s.cod_produto_codigo "
//               + "JOIN tb_tipo_produto t ON t.codigo = p.cod_Tipo_Produto "
//               + "LEFT JOIN tb_preco pr ON pr.fk_produto = p.codigo "
//               + "WHERE s.cod_armazem = ? "
//               + "GROUP BY p.codigo, p.designacao, t.designacao "
//               + "ORDER BY p.designacao ASC";
//
//    try (PreparedStatement ps = conn.prepareStatement(sql)) {
//        ps.setInt(1, codArmazem);
//        try (ResultSet rs = ps.executeQuery()) {
//            while (rs.next()) {
//                Object[] linha = {
//                    rs.getInt("codigo"),
//                    rs.getString("designacao"),
//                    rs.getString("tipo_produto"),
//                    rs.getBigDecimal("preco_medio_compra"),
//                    rs.getBigDecimal("preco_venda_atual"),
//                    rs.getDouble("qtd_existente"),
//                    null, // quantidade a acertar (editável no JTable)
//                    null  // existência total (calculada no JTable)
//                };
//                lista.add(linha);
//            }
//        }
//    } catch (SQLException e) {
//        e.printStackTrace();
//    }
//
//    return lista;
//}

    
//public List<Object[]> listarStockPorArmazem(Connection conn, int codArmazem) {
//    List<Object[]> lista = new ArrayList<>();
//
//    String sql = "SELECT DISTINCT p.codigo, p.designacao, SUM(s.quantidade_existente) AS quantidade, pr.preco_venda "
//               + "FROM tb_stock s "
//               + "JOIN tb_produto p ON p.codigo = s.cod_produto_codigo "
//               + "JOIN tb_preco pr ON pr.fk_produto = p.codigo "
//               + "WHERE s.cod_armazem = ? "
//               + "GROUP BY p.codigo, p.designacao, pr.preco_venda "
//               + "ORDER BY p.designacao ASC";
//
//    try (PreparedStatement ps = conn.prepareStatement(sql)) {
//        ps.setInt(1, codArmazem);
//        try (ResultSet rs = ps.executeQuery()) {
//            while (rs.next()) {
//                Object[] linha = {
//                    rs.getInt("codigo"),
//                    rs.getString("designacao"),
//                    rs.getInt("quantidade"),
//                    rs.getBigDecimal("preco_venda")
//                };
//                lista.add(linha);
//            }
//        }
//    } catch (SQLException e) {
//        e.printStackTrace();
//    }
//
//    return lista;
//}



    public static void main( String[] args )
    {
        BDConexao conexao = new BDConexao();
        ProdutosController pc = new ProdutosController( conexao );
        System.out.println( "LAST ID PRODUTO: " + pc.getLastProduto().getCodigo() );
    }

}
