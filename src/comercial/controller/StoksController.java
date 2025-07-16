/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comercial.controller;

import enties.util.BuscaModeloProduto;
import entity.Grupo;
import entity.Modelo;
import entity.TbArmazem;
import entity.TbFornecedor;
import entity.TbLocal;
import entity.TbProduto;
import entity.TbStock;
import entity.TbTipoProduto;
import entity.Unidade;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Vector;
import util.BDConexao;
import util.DVML;

/**
 *
 * @author Martinho Luis & Domingos Dala Vunge
 */
public class StoksController implements EntidadeFactory
{

    private BDConexao conexao;

    public StoksController( BDConexao conexao )
    {
        this.conexao = conexao;
    }

    @Override
    public boolean salvar( Object object )
    {

        TbStock stock = (TbStock) object;
        String INSERT = "INSERT INTO tb_stock( cod_produto_codigo, quantidade_existente, status, preco_venda, quant_critica, quant_baixa, quantidade_antiga, "
                + " cod_armazem , preco_venda_grosso , qtd_grosso , preco_venda_fabrica "
                + ")"
                + " VALUES("
                + stock.getCodProdutoCodigo().getCodigo() + " , "
                + stock.getQuantidadeExistente() + " , "
                + "'" + stock.getStatus() + "' , "
                + stock.getPrecoVenda() + " , "
                + "'" + stock.getQuantCritica() + "' , "
                + "'" + stock.getQuantBaixa() + "' , "
                + stock.getQuantidadeAntiga() + " , "
                + stock.getCodArmazem().getCodigo() + " , "
                + stock.getPrecoVendaGrosso() + " , "
                + "'" + stock.getQtdGrosso() + "' , "
                + stock.getPrecoVendaFabrica()
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
        String DELETE = "DELETE FROM tb_stock WHERE codigo = " + codigo;
        return conexao.executeUpdate( DELETE );
    }

    @Override
    public List<TbStock> listarTodos()
    {

        String FIND_ALL = "SELECT * FROM tb_stock ORDER BY codigo ASC";
        ResultSet result = conexao.executeQuery( FIND_ALL );
        List<TbStock> lista_stock = new ArrayList<>();
        TbStock stock;
        try
        {

            while ( result.next() )
            {
                stock = new TbStock();
                stock.setCodigo( result.getInt( "codigo" ) );
                stock.setCodProdutoCodigo( new TbProduto( result.getInt( "cod_produto_codigo" ) ) );
//                stock.setDataEntrada( result.getDate( "data_entrada" ) );
                stock.setQuantidadeExistente( result.getDouble( "quantidade_existente" ) );
                stock.setStatus( result.getString( "status" ) );
                stock.setPrecoVenda( result.getBigDecimal( "preco_venda" ) );
                stock.setQuantCritica( result.getInt( "quant_critica" ) );
                stock.setQuantBaixa( result.getInt( "quant_baixa" ) );
                stock.setQuantidadeAntiga( result.getDouble( "quantidade_antiga" ) );
                stock.setCodArmazem( new TbArmazem( result.getInt( "cod_armazem" ) ) );
                stock.setPrecoVendaGrosso( result.getBigDecimal( "preco_venda_grosso" ) );
                stock.setQtdGrosso( result.getInt( "qtd_grosso" ) );
                stock.setPrecoVendaFabrica( result.getBigDecimal( "preco_venda_fabrica" ) );

                lista_stock.add( stock );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return lista_stock;
    }

    @Override
    public Vector<String> getVector()
    {
        throw new UnsupportedOperationException( "Not supported yet." ); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object findById( int codigo )
    {

        String FIND__BY_CODIGO = "SELECT * FROM tb_stock WHERE codigo = " + codigo;
        ResultSet result = conexao.executeQuery( FIND__BY_CODIGO );
        TbStock stock = null;
        try
        {

            if ( result.next() )
            {
                stock = new TbStock();
                stock.setCodigo( result.getInt( "codigo" ) );
                stock.setCodProdutoCodigo( new TbProduto( result.getInt( "cod_produto_codigo" ) ) );
//                stock.setDataEntrada( result.getDate( "data_entrada" ) );
                stock.setQuantidadeExistente( result.getDouble( "quantidade_existente" ) );
                stock.setStatus( result.getString( "status" ) );
                stock.setPrecoVenda( result.getBigDecimal( "preco_venda" ) );
                stock.setQuantCritica( result.getInt( "quant_critica" ) );
                stock.setQuantBaixa( result.getInt( "quant_baixa" ) );
                stock.setQuantidadeAntiga( result.getDouble( "quantidade_antiga" ) );
                stock.setCodArmazem( new TbArmazem( result.getInt( "cod_armazem" ) ) );
                stock.setPrecoVendaGrosso( result.getBigDecimal( "preco_venda_grosso" ) );
                stock.setQtdGrosso( result.getInt( "qtd_grosso" ) );
                stock.setPrecoVendaFabrica( result.getBigDecimal( "preco_venda_fabrica" ) );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return stock;

    }

    public TbStock getLasStock()
    {

        String FIND__BY_CODIGO = "SELECT MAX(codigo) as maximo_id, s.*  FROM tb_stock s";
        ResultSet result = conexao.executeQuery( FIND__BY_CODIGO );
        TbStock stock = null;
        try
        {

            if ( result.next() )
            {
                stock = new TbStock();
                stock.setCodigo( result.getInt( "maximo_id" ) );
                stock.setCodProdutoCodigo( new TbProduto( result.getInt( "cod_produto_codigo" ) ) );
//                stock.setDataEntrada( result.getDate( "data_entrada" ) );
                stock.setQuantidadeExistente( result.getDouble( "quantidade_existente" ) );
                stock.setStatus( result.getString( "status" ) );
                stock.setPrecoVenda( result.getBigDecimal( "preco_venda" ) );
                stock.setQuantCritica( result.getInt( "quant_critica" ) );
                stock.setQuantBaixa( result.getInt( "quant_baixa" ) );
                stock.setQuantidadeAntiga( result.getDouble( "quantidade_antiga" ) );
                stock.setCodArmazem( new TbArmazem( result.getInt( "cod_armazem" ) ) );
                stock.setPrecoVendaGrosso( result.getBigDecimal( "preco_venda_grosso" ) );
                stock.setQtdGrosso( result.getInt( "qtd_grosso" ) );
                stock.setPrecoVendaFabrica( result.getBigDecimal( "preco_venda_fabrica" ) );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return stock;

    }

    public TbStock getStockByIdProdutoAndIdArmazem1( int idProduto, int idArmazem, BDConexao conexao )
    {

        String FIND__BY_CODIGO = "SELECT * FROM tb_stock s WHERE s.cod_produto_codigo = " + idProduto + " AND s.cod_armazem = " + idArmazem;
        ResultSet result = conexao.executeQuery( FIND__BY_CODIGO );
        TbStock stock = null;
        try
        {

            if ( result.next() )
            {
                stock = new TbStock();
                stock.setCodigo( result.getInt( "codigo" ) );
                stock.setCodProdutoCodigo( new TbProduto( result.getInt( "cod_produto_codigo" ) ) );
//                stock.setDataEntrada( result.getDate("data_entrada" ) );
                stock.setQuantidadeExistente( result.getDouble( "quantidade_existente" ) );
                stock.setStatus( result.getString( "status" ) );
                stock.setPrecoVenda( result.getBigDecimal( "preco_venda" ) );
                stock.setQuantCritica( result.getInt( "quant_critica" ) );
                stock.setQuantBaixa( result.getInt( "quant_baixa" ) );
                stock.setQuantidadeAntiga( result.getDouble( "quantidade_antiga" ) );
                stock.setCodArmazem( new TbArmazem( result.getInt( "cod_armazem" ) ) );
                stock.setPrecoVendaGrosso( result.getBigDecimal( "preco_venda_grosso" ) );
                stock.setQtdGrosso( result.getInt( "qtd_grosso" ) );
                stock.setPrecoVendaFabrica( result.getBigDecimal( "preco_venda_fabrica" ) );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return stock;

    }

    public List<TbProduto> get_all_produtos_by_id_tipo_produto_and_id_armazem( int idTipoProduto, int idArmazem, BDConexao conexao )
    {

//        String FIND__BY_CODIGO = "SELECT * FROM tb_stock s WHERE s.cod_produto_codigo = " + idProduto + " AND s.cod_armazem = " + idArmazem;s.cod_armazem = " + idArmazem + " AND
        String FIND__BY_CODIGO = " SELECT p.* FROM tb_stock s INNER JOIN tb_produto p ON p.codigo = s.cod_produto_codigo WHERE p.cod_Tipo_Produto = " + idTipoProduto + " AND s.cod_armazem = " + idArmazem;
        ResultSet result = conexao.executeQuery( FIND__BY_CODIGO );
//        TbStock stock = null;
        List<TbProduto> lista_stock = new ArrayList<>();
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
                lista_stock.add( produto );
            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return lista_stock;

    }

    public TbStock getStockByIdProdutoAndIdArmazem( int idProduto, int idArmazem )
    {

        String FIND__BY_CODIGO = "SELECT * FROM tb_stock s WHERE s.cod_produto_codigo = " + idProduto + " AND s.cod_armazem = " + idArmazem;
        ResultSet result = conexao.executeQuery( FIND__BY_CODIGO );
        TbStock stock = null;
        try
        {

            if ( result.next() )
            {
                stock = new TbStock();
                stock.setCodigo( result.getInt( "codigo" ) );
                stock.setCodProdutoCodigo( new TbProduto( result.getInt( "cod_produto_codigo" ) ) );
//                stock.setDataEntrada( result.getDate("data_entrada" ) );
                stock.setQuantidadeExistente( result.getDouble( "quantidade_existente" ) );
                stock.setStatus( result.getString( "status" ) );
                stock.setPrecoVenda( result.getBigDecimal( "preco_venda" ) );
                stock.setQuantCritica( result.getInt( "quant_critica" ) );
                stock.setQuantBaixa( result.getInt( "quant_baixa" ) );
                stock.setQuantidadeAntiga( result.getDouble( "quantidade_antiga" ) );
                stock.setCodArmazem( new TbArmazem( result.getInt( "cod_armazem" ) ) );
                stock.setPrecoVendaGrosso( result.getBigDecimal( "preco_venda_grosso" ) );
                stock.setQtdGrosso( result.getInt( "qtd_grosso" ) );
                stock.setPrecoVendaFabrica( result.getBigDecimal( "preco_venda_fabrica" ) );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return stock;

    }

    public boolean existeStock( int codigo, int idArmazem, BDConexao conexao ) throws SQLException
    {
        String query = "SELECT * FROM tb_stock s WHERE s.cod_produto_codigo = " + codigo + " AND s.cod_armazem = " + idArmazem;
        ResultSet resultSet = conexao.executeQuery( query );

        return resultSet.next();

    }

    public boolean existe_stock( int codigo, int idArmazem )
    {
        try
        {
            String query = "SELECT * FROM tb_stock s WHERE s.cod_produto_codigo = " + codigo + " AND s.cod_armazem = " + idArmazem;
            System.err.println( query );
            ResultSet resultSet = conexao.executeQuery( query );
            return resultSet.next();
        }
        catch ( SQLException ex )
        {
//            Logger.getLogger( VendaDao.class.getName() ).log( Level.SEVERE, null, ex );
        }
        return false;

    }

    public Vector<BuscaModeloProduto> getFonte( int idArmazem, int codJanela )
    {

        BDConexao conexaoTemp = new BDConexao();

        String codicao = ( codJanela != DVML.JANELA_COMPRA ) ? "IN(1,2)" : "IN(2)";
        Vector<BuscaModeloProduto> busca = new Vector<>();
        String sql = "SELECT "
                + "	p.codigo AS codigo , "
                + "     p.designacao AS designacao, "
                + "	tp.designacao AS categoria, "
                + "	f.pk_familia AS cod_familia, "
                + "     ("
                + "		 SELECT  quantidade_existente FROM tb_stock WHERE cod_produto_codigo = p.codigo AND cod_armazem = " + idArmazem
                + "     ) AS qtd, "
                + "     ("
                + "		  ("
                + "			(SELECT  quant_baixa FROM tb_stock WHERE cod_produto_codigo = p.codigo AND cod_armazem = " + idArmazem + ")  < "
                + "			(SELECT  quantidade_existente FROM tb_stock WHERE cod_produto_codigo = p.codigo AND cod_armazem =    " + idArmazem + ") "
                + "		  )"
                + "          AND  "
                + "		  ( "
                + "                     (SELECT  quantidade_existente FROM tb_stock WHERE cod_produto_codigo = p.codigo AND cod_armazem = " + idArmazem + ") <"
                + "                     (SELECT  quant_critica FROM tb_stock WHERE cod_produto_codigo = p.codigo AND cod_armazem = " + idArmazem + ") "
                + "		  ) "
                + "     ) AS estado_critico , "
                + "	 ("
                + "       SELECT preco_venda FROM tb_preco where fk_produto = p.codigo AND qtd_baixo = 0 ORDER BY pk_preco DESC LIMIT 1 "
                + "	 ) AS preco_venda "
                + "FROM tb_produto p "
                + "INNER JOIN tb_tipo_produto tp ON tp.codigo = p.cod_Tipo_Produto "
                + "INNER JOIN familia f ON f.pk_familia = tp.fk_familia "
                + " WHERE p.fk_grupo = 1 AND f.pk_familia " + codicao + " "
                //  + "AND p.codigo BETWEEN 1 AND 30  "
                + "GROUP BY p.codigo "
                + "ORDER BY p.designacao ";
//        String sql = "SELECT "
//                + "	p.codigo AS codigo , "
//                + "     p.designacao AS designacao, "
//                + "	tp.designacao AS categoria, "
//                + "	f.pk_familia AS cod_familia, "
//                + "     ("
//                + "		 SELECT  quantidade_existente FROM tb_stock WHERE cod_produto_codigo = p.codigo AND cod_armazem = " + idArmazem
//                + "     ) AS qtd, "
//                + "     ("
//                + "		  ("
//                + "			(SELECT  quant_baixa FROM tb_stock WHERE cod_produto_codigo = p.codigo AND cod_armazem = " + idArmazem + ")  < "
//                + "			(SELECT  quantidade_existente FROM tb_stock WHERE cod_produto_codigo = p.codigo AND cod_armazem =    " + idArmazem + ") "
//                + "		  )"
//                + "          AND  "
//                + "		  ( "
//                + "                     (SELECT  quantidade_existente FROM tb_stock WHERE cod_produto_codigo = p.codigo AND cod_armazem = " + idArmazem + ") <"
//                + "                     (SELECT  quant_critica FROM tb_stock WHERE cod_produto_codigo = p.codigo AND cod_armazem = " + idArmazem + ") "
//                + "		  ) "
//                + "     ) AS estado_critico , "
//                + "	 ("
//                + "         SELECT preco_venda FROM tb_preco WHERE pk_preco = (  SELECT max(pk_preco) FROM tb_preco WHERE fk_produto = p.codigo AND qtd_baixo = ( SELECT MIN(qtd_baixo) AS QTD_MIN FROM tb_preco WHERE fk_produto = p.codigo GROUP BY fk_produto  ) )"
//                + "	 ) AS preco_venda "
//                + "FROM tb_produto p "
//                + "INNER JOIN tb_tipo_produto tp ON tp.codigo = p.cod_Tipo_Produto "
//                + "INNER JOIN familia f ON f.pk_familia = tp.fk_familia "
//                + " WHERE f.pk_familia " + codicao + " "
//                + "AND p.codigo BETWEEN 31 AND 33  "
//                + "GROUP BY p.codigo "
//                + "ORDER BY p.designacao ";

        System.out.println( sql );
        ResultSet rs = conexaoTemp.executeQuery( sql );
        try
        {
            while ( rs.next() )
            {

                int codigo = rs.getInt( "codigo" );
//                System.out.println( "CODIGO: " + codigo );
                String designacao = rs.getString( "designacao" );
                String categoria = rs.getString( "categoria" );
                String qtd = (( rs.getLong( "cod_familia" ) == 1 ) ? "-" : String.valueOf( rs.getLong( "qtd" ) ));
                double precoVenda = rs.getDouble( "preco_venda" );

                String estadoCritico = (( rs.getInt( "cod_familia" ) == 1 ) ? "-" : rs.getString( "estado_critico" ));

                if ( Objects.isNull( estadoCritico ) || estadoCritico.equals( "0" ) )
                {
                    estadoCritico = "false";
                }
                else if ( estadoCritico.equals( "1" ) )
                {
                    estadoCritico = "true";
                }

                BuscaModeloProduto bmp = new BuscaModeloProduto();
                bmp.setCodigo( codigo );
                bmp.setDesignacao( designacao );
                bmp.setCategoria( categoria );
                bmp.setQtd( qtd );
                bmp.setPrecoVenda( precoVenda );
                bmp.setEstadoCritico( estadoCritico );

                busca.add( bmp );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        System.out.println( "SIZE_: " + busca.size() );

        conexaoTemp.close();

        return busca;
    }

    public Vector<BuscaModeloProduto> getFonteRefeicao( int idArmazem, int codJanela )
    {

        BDConexao conexaoTemp = new BDConexao();

        String codicao = ( codJanela != DVML.JANELA_COMPRA ) ? "IN(1,2)" : "IN(2)";
        Vector<BuscaModeloProduto> busca = new Vector<>();
        String sql = "SELECT "
                + "	p.codigo AS codigo , "
                + "     p.designacao AS designacao, "
                + "	tp.designacao AS categoria, "
                + "	f.pk_familia AS cod_familia, "
                + "     ("
                + "		 SELECT  quantidade_existente FROM tb_stock WHERE cod_produto_codigo = p.codigo AND cod_armazem = " + idArmazem
                + "     ) AS qtd, "
                + "     ("
                + "		  ("
                + "			(SELECT  quant_baixa FROM tb_stock WHERE cod_produto_codigo = p.codigo AND cod_armazem = " + idArmazem + ")  < "
                + "			(SELECT  quantidade_existente FROM tb_stock WHERE cod_produto_codigo = p.codigo AND cod_armazem =    " + idArmazem + ") "
                + "		  )"
                + "          AND  "
                + "		  ( "
                + "                     (SELECT  quantidade_existente FROM tb_stock WHERE cod_produto_codigo = p.codigo AND cod_armazem = " + idArmazem + ") <"
                + "                     (SELECT  quant_critica FROM tb_stock WHERE cod_produto_codigo = p.codigo AND cod_armazem = " + idArmazem + ") "
                + "		  ) "
                + "     ) AS estado_critico , "
                + "	 ("
                + "       SELECT preco_venda FROM tb_preco where fk_produto = p.codigo AND qtd_baixo = 0 ORDER BY pk_preco DESC LIMIT 1 "
                + "	 ) AS preco_venda "
                + "FROM tb_produto p "
                + "INNER JOIN tb_tipo_produto tp ON tp.codigo = p.cod_Tipo_Produto "
                + "INNER JOIN familia f ON f.pk_familia = tp.fk_familia "
                + " WHERE p.fk_grupo = 3 AND f.pk_familia " + codicao + " "
                //  + "AND p.codigo BETWEEN 1 AND 30  "
                + "GROUP BY p.codigo "
                + "ORDER BY p.designacao ";
//        String sql = "SELECT "
//                + "	p.codigo AS codigo , "
//                + "     p.designacao AS designacao, "
//                + "	tp.designacao AS categoria, "
//                + "	f.pk_familia AS cod_familia, "
//                + "     ("
//                + "		 SELECT  quantidade_existente FROM tb_stock WHERE cod_produto_codigo = p.codigo AND cod_armazem = " + idArmazem
//                + "     ) AS qtd, "
//                + "     ("
//                + "		  ("
//                + "			(SELECT  quant_baixa FROM tb_stock WHERE cod_produto_codigo = p.codigo AND cod_armazem = " + idArmazem + ")  < "
//                + "			(SELECT  quantidade_existente FROM tb_stock WHERE cod_produto_codigo = p.codigo AND cod_armazem =    " + idArmazem + ") "
//                + "		  )"
//                + "          AND  "
//                + "		  ( "
//                + "                     (SELECT  quantidade_existente FROM tb_stock WHERE cod_produto_codigo = p.codigo AND cod_armazem = " + idArmazem + ") <"
//                + "                     (SELECT  quant_critica FROM tb_stock WHERE cod_produto_codigo = p.codigo AND cod_armazem = " + idArmazem + ") "
//                + "		  ) "
//                + "     ) AS estado_critico , "
//                + "	 ("
//                + "         SELECT preco_venda FROM tb_preco WHERE pk_preco = (  SELECT max(pk_preco) FROM tb_preco WHERE fk_produto = p.codigo AND qtd_baixo = ( SELECT MIN(qtd_baixo) AS QTD_MIN FROM tb_preco WHERE fk_produto = p.codigo GROUP BY fk_produto  ) )"
//                + "	 ) AS preco_venda "
//                + "FROM tb_produto p "
//                + "INNER JOIN tb_tipo_produto tp ON tp.codigo = p.cod_Tipo_Produto "
//                + "INNER JOIN familia f ON f.pk_familia = tp.fk_familia "
//                + " WHERE f.pk_familia " + codicao + " "
//                + "AND p.codigo BETWEEN 31 AND 33  "
//                + "GROUP BY p.codigo "
//                + "ORDER BY p.designacao ";

        System.out.println( sql );
        ResultSet rs = conexaoTemp.executeQuery( sql );
        try
        {
            while ( rs.next() )
            {

                int codigo = rs.getInt( "codigo" );
//                System.out.println( "CODIGO: " + codigo );
                String designacao = rs.getString( "designacao" );
                String categoria = rs.getString( "categoria" );
                String qtd = (( rs.getLong( "cod_familia" ) == 1 ) ? "-" : String.valueOf( rs.getLong( "qtd" ) ));
                double precoVenda = rs.getDouble( "preco_venda" );

                String estadoCritico = (( rs.getInt( "cod_familia" ) == 1 ) ? "-" : rs.getString( "estado_critico" ));

                if ( Objects.isNull( estadoCritico ) || estadoCritico.equals( "0" ) )
                {
                    estadoCritico = "false";
                }
                else if ( estadoCritico.equals( "1" ) )
                {
                    estadoCritico = "true";
                }

                BuscaModeloProduto bmp = new BuscaModeloProduto();
                bmp.setCodigo( codigo );
                bmp.setDesignacao( designacao );
                bmp.setCategoria( categoria );
                bmp.setQtd( qtd );
                bmp.setPrecoVenda( precoVenda );
                bmp.setEstadoCritico( estadoCritico );

                busca.add( bmp );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        System.out.println( "SIZE_: " + busca.size() );

        conexaoTemp.close();

        return busca;
    }

    public Vector<BuscaModeloProduto> getFonteIngredientes( int idArmazem, int codJanela )
    {

        BDConexao conexaoTemp = new BDConexao();

        String codicao = ( codJanela != DVML.JANELA_COMPRA ) ? "IN(1,2)" : "IN(2)";
        Vector<BuscaModeloProduto> busca = new Vector<>();
        String sql = "SELECT "
                + "	p.codigo AS codigo , "
                + "     p.designacao AS designacao, "
                + "	tp.designacao AS categoria, "
                + "	f.pk_familia AS cod_familia, "
                + "     ("
                + "		 SELECT  quantidade_existente FROM tb_stock WHERE cod_produto_codigo = p.codigo AND cod_armazem = " + idArmazem
                + "     ) AS qtd, "
                + "     ("
                + "		  ("
                + "			(SELECT  quant_baixa FROM tb_stock WHERE cod_produto_codigo = p.codigo AND cod_armazem = " + idArmazem + ")  < "
                + "			(SELECT  quantidade_existente FROM tb_stock WHERE cod_produto_codigo = p.codigo AND cod_armazem =    " + idArmazem + ") "
                + "		  )"
                + "          AND  "
                + "		  ( "
                + "                     (SELECT  quantidade_existente FROM tb_stock WHERE cod_produto_codigo = p.codigo AND cod_armazem = " + idArmazem + ") <"
                + "                     (SELECT  quant_critica FROM tb_stock WHERE cod_produto_codigo = p.codigo AND cod_armazem = " + idArmazem + ") "
                + "		  ) "
                + "     ) AS estado_critico , "
                + "	 ("
                + "       SELECT preco_venda FROM tb_preco where fk_produto = p.codigo AND qtd_baixo = 0 ORDER BY pk_preco DESC LIMIT 1 "
                + "	 ) AS preco_venda "
                + "FROM tb_produto p "
                + "INNER JOIN tb_tipo_produto tp ON tp.codigo = p.cod_Tipo_Produto "
                + "INNER JOIN familia f ON f.pk_familia = tp.fk_familia "
                + " WHERE p.fk_grupo = 2 AND f.pk_familia " + codicao + " "
                //  + "AND p.codigo BETWEEN 1 AND 30  "
                + "GROUP BY p.codigo "
                + "ORDER BY p.designacao ";
//        String sql = "SELECT "
//                + "	p.codigo AS codigo , "
//                + "     p.designacao AS designacao, "
//                + "	tp.designacao AS categoria, "
//                + "	f.pk_familia AS cod_familia, "
//                + "     ("
//                + "		 SELECT  quantidade_existente FROM tb_stock WHERE cod_produto_codigo = p.codigo AND cod_armazem = " + idArmazem
//                + "     ) AS qtd, "
//                + "     ("
//                + "		  ("
//                + "			(SELECT  quant_baixa FROM tb_stock WHERE cod_produto_codigo = p.codigo AND cod_armazem = " + idArmazem + ")  < "
//                + "			(SELECT  quantidade_existente FROM tb_stock WHERE cod_produto_codigo = p.codigo AND cod_armazem =    " + idArmazem + ") "
//                + "		  )"
//                + "          AND  "
//                + "		  ( "
//                + "                     (SELECT  quantidade_existente FROM tb_stock WHERE cod_produto_codigo = p.codigo AND cod_armazem = " + idArmazem + ") <"
//                + "                     (SELECT  quant_critica FROM tb_stock WHERE cod_produto_codigo = p.codigo AND cod_armazem = " + idArmazem + ") "
//                + "		  ) "
//                + "     ) AS estado_critico , "
//                + "	 ("
//                + "         SELECT preco_venda FROM tb_preco WHERE pk_preco = (  SELECT max(pk_preco) FROM tb_preco WHERE fk_produto = p.codigo AND qtd_baixo = ( SELECT MIN(qtd_baixo) AS QTD_MIN FROM tb_preco WHERE fk_produto = p.codigo GROUP BY fk_produto  ) )"
//                + "	 ) AS preco_venda "
//                + "FROM tb_produto p "
//                + "INNER JOIN tb_tipo_produto tp ON tp.codigo = p.cod_Tipo_Produto "
//                + "INNER JOIN familia f ON f.pk_familia = tp.fk_familia "
//                + " WHERE f.pk_familia " + codicao + " "
//                + "AND p.codigo BETWEEN 31 AND 33  "
//                + "GROUP BY p.codigo "
//                + "ORDER BY p.designacao ";

        System.out.println( sql );
        ResultSet rs = conexaoTemp.executeQuery( sql );
        try
        {
            while ( rs.next() )
            {

                int codigo = rs.getInt( "codigo" );
//                System.out.println( "CODIGO: " + codigo );
                String designacao = rs.getString( "designacao" );
                String categoria = rs.getString( "categoria" );
                String qtd = (( rs.getLong( "cod_familia" ) == 1 ) ? "-" : String.valueOf( rs.getLong( "qtd" ) ));
                double precoVenda = rs.getDouble( "preco_venda" );

                String estadoCritico = (( rs.getInt( "cod_familia" ) == 1 ) ? "-" : rs.getString( "estado_critico" ));

                if ( Objects.isNull( estadoCritico ) || estadoCritico.equals( "0" ) )
                {
                    estadoCritico = "false";
                }
                else if ( estadoCritico.equals( "1" ) )
                {
                    estadoCritico = "true";
                }

                BuscaModeloProduto bmp = new BuscaModeloProduto();
                bmp.setCodigo( codigo );
                bmp.setDesignacao( designacao );
                bmp.setCategoria( categoria );
                bmp.setQtd( qtd );
                bmp.setPrecoVenda( precoVenda );
                bmp.setEstadoCritico( estadoCritico );

                busca.add( bmp );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        System.out.println( "SIZE_: " + busca.size() );

        conexaoTemp.close();

        return busca;
    }

    public List<TbStock> findStockByDesignacaoProduto( int idArmazem, String designacao )
    {

        String FIND_ALL = "SELECT s.* FROM tb_stock s INNER JOIN tb_produto p ON p.codigo = s.cod_produto_codigo WHERE s.cod_armazem = " + idArmazem + " AND p.designacao LIKE '%" + designacao + "%' ";
        ResultSet result = conexao.executeQuery( FIND_ALL );
        System.out.println( "Query " + FIND_ALL );
        List<TbStock> lista_stock = new ArrayList<>();
        TbStock stock;
        try
        {

            if ( result.next() )
            {
                stock = new TbStock();
                stock.setCodigo( result.getInt( "codigo" ) );
                stock.setCodProdutoCodigo( new TbProduto( result.getInt( "cod_produto_codigo" ) ) );
//                stock.setDataEntrada( result.getDate("data_entrada" ) );
                stock.setQuantidadeExistente( result.getDouble( "quantidade_existente" ) );
                stock.setStatus( result.getString( "status" ) );
                stock.setPrecoVenda( result.getBigDecimal( "preco_venda" ) );
                stock.setQuantCritica( result.getInt( "quant_critica" ) );
                stock.setQuantBaixa( result.getInt( "quant_baixa" ) );
                stock.setQuantidadeAntiga( result.getDouble( "quantidade_antiga" ) );
                stock.setCodArmazem( new TbArmazem( result.getInt( "cod_armazem" ) ) );
                stock.setPrecoVendaGrosso( result.getBigDecimal( "preco_venda_grosso" ) );
                stock.setQtdGrosso( result.getInt( "qtd_grosso" ) );
                stock.setPrecoVendaFabrica( result.getBigDecimal( "preco_venda_fabrica" ) );
                lista_stock.add( stock );
            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return lista_stock;

    }

    public List<TbProduto> get_all_produtos_by_id_tipo_produto_and_id_armazem( int idTipoProduto, int idArmazem )
    {

//        String FIND__BY_CODIGO = "SELECT * FROM tb_stock s WHERE s.cod_produto_codigo = " + idProduto + " AND s.cod_armazem = " + idArmazem;s.cod_armazem = " + idArmazem + " AND
        String FIND__BY_CODIGO = " SELECT p.* FROM tb_stock s INNER JOIN tb_produto p ON p.codigo = s.cod_produto_codigo WHERE p.cod_Tipo_Produto = " + idTipoProduto + " AND s.cod_armazem = " + idArmazem;
        ResultSet result = conexao.executeQuery( FIND__BY_CODIGO );
//        TbStock stock = null;
        List<TbProduto> lista_stock = new ArrayList<>();
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
                lista_stock.add( produto );
            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return lista_stock;

    }

    public List<TbProduto> get_all_produtos_by_id_tipo_produto_and_id_armazem_and_grupo( int idTipoProduto, int idArmazem, int idGrupo )
    {

//        String FIND__BY_CODIGO = "SELECT * FROM tb_stock s WHERE s.cod_produto_codigo = " + idProduto + " AND s.cod_armazem = " + idArmazem;s.cod_armazem = " + idArmazem + " AND
        String FIND__BY_CODIGO = " SELECT p.* FROM tb_stock s "
                + " INNER JOIN tb_produto p ON p.codigo = s.cod_produto_codigo "
                + " WHERE p.cod_Tipo_Produto = " + idTipoProduto
                + " AND s.cod_armazem = " + idArmazem
                + " AND p.fk_grupo = " + idGrupo;
        ResultSet result = conexao.executeQuery( FIND__BY_CODIGO );
//        TbStock stock = null;
        List<TbProduto> lista_stock = new ArrayList<>();
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
                lista_stock.add( produto );
            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return lista_stock;

    }

    public TbStock getStockByCodBarraAndIdArmazem( String codBarra, int idArmazem )
    {

//        String FIND__BY_CODIGO = "SELECT * FROM tb_stock s WHERE s.cod_produto_codigo.codBarra = '" + codBarra  + "' AND s.cod_armazem = " + idArmazem;
//        String FIND_ALL = "SELECT s.* FROM tb_stock s INNER JOIN tb_produto p ON p.codigo = s.cod_produto_codigo WHERE s.cod_armazem = " + idArmazem  + " AND p.designacao LIKE '%" + designacao+ "%' ";
        String FIND__BY_CODIGO = "SELECT s.* FROM tb_stock s "
                + " INNER JOIN tb_produto p ON p.codigo = s.cod_produto_codigo "
                + " WHERE p.codBarra = '" + codBarra + "' AND s.cod_armazem = " + idArmazem;
        ResultSet result = conexao.executeQuery( FIND__BY_CODIGO );
        TbStock stock = null;
        try
        {

            if ( result.next() )
            {
                stock = new TbStock();
                stock.setCodigo( result.getInt( "codigo" ) );
                stock.setCodProdutoCodigo( new TbProduto( result.getInt( "cod_produto_codigo" ) ) );
//                stock.setDataEntrada( result.getDate("data_entrada" ) );
                stock.setQuantidadeExistente( result.getDouble( "quantidade_existente" ) );
                stock.setStatus( result.getString( "status" ) );
                stock.setPrecoVenda( result.getBigDecimal( "preco_venda" ) );
                stock.setQuantCritica( result.getInt( "quant_critica" ) );
                stock.setQuantBaixa( result.getInt( "quant_baixa" ) );
                stock.setQuantidadeAntiga( result.getDouble( "quantidade_antiga" ) );
                stock.setCodArmazem( new TbArmazem( result.getInt( "cod_armazem" ) ) );
                stock.setPrecoVendaGrosso( result.getBigDecimal( "preco_venda_grosso" ) );
                stock.setQtdGrosso( result.getInt( "qtd_grosso" ) );
                stock.setPrecoVendaFabrica( result.getBigDecimal( "preco_venda_fabrica" ) );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return stock;

    }

    public boolean adicionar_quantidades( int cod, double quantidade, double qtdCritica, double qtdBaixa, int idArmazem )
    {

        double qtdExistente = getQuantidadeProduto( cod, idArmazem );
        System.out.println( "QTD_EXISTENTE: " + qtdExistente );
        double qtdUpdate = (qtdExistente + quantidade);
        System.out.println( "QTD_UPDATE: " + qtdUpdate );
        String sql = "UPDATE tb_stock SET quantidade_existente =  " + qtdUpdate + ",  "
                + " quant_critica = " + qtdCritica + ",  "
                + " quant_baixa = " + qtdBaixa
                + " WHERE cod_produto_codigo = " + cod + " AND  cod_armazem = " + idArmazem;
        return conexao.executeUpdate( sql );

    }

    public boolean adicionar_quantidades( int cod, double quantidade, int idArmazem )
    {

        double qtdExistente = getQuantidadeProduto( cod, idArmazem );
//        System.out.println( "QTD_EXISTENTE: " + qtdExistente );
        double qtdUpdate = (qtdExistente + quantidade);
//        System.out.println( "QTD_UPDATE: " + qtdUpdate );
        String sql = "UPDATE tb_stock SET quantidade_existente =  " + qtdUpdate
                + " WHERE cod_produto_codigo = " + cod + " AND  cod_armazem = " + idArmazem;
        return conexao.executeUpdate( sql );

    }

    public static boolean adicionar_quantidades( int cod, double quantidade, int idArmazem, BDConexao conexao )
    {

        double qtdExistente = getQuantidadeProduto( cod, idArmazem, conexao );
//        System.out.println( "QTD_EXISTENTE: " + qtdExistente );
        double qtdUpdate = (qtdExistente + quantidade);
//        System.out.println( "QTD_UPDATE: " + qtdUpdate );
        String sql = "UPDATE tb_stock SET quantidade_existente =  " + qtdUpdate
                + " WHERE cod_produto_codigo = " + cod + " AND  cod_armazem = " + idArmazem;
        return conexao.executeUpdate( sql );

    }

//        
//    public static void actualizar_quantidade( int cod, double quantidade )
//    {
//
//        String sql = "UPDATE tb_stock SET quantidade_existente =  " + ( getQuantidadeProduto( cod ) - quantidade ) + " WHERE cod_produto_codigo = " + cod + " AND  cod_armazem = " + getCodigoArmazem();
//        System.out.println( "Quantidade   : " + quantidade );
//        conexao.executeUpdate( sql );
//
//    }
    public boolean subtrair_quantidades( int cod, double quantidade, int idArmazem )
    {

        double qtdExistente = getQuantidadeProduto( cod, idArmazem );
        double qtdUpdate = (qtdExistente - quantidade);
//        System.out.println( "QTD_UPDATE: " + qtdUpdate );
        String sql = "UPDATE tb_stock SET quantidade_existente =  " + qtdUpdate
                + " WHERE cod_produto_codigo = " + cod + " AND  cod_armazem = " + idArmazem;
        return conexao.executeUpdate( sql );

    }

    public static boolean subtrair_quantidades( int cod, double quantidade, int idArmazem, BDConexao conexao )
    {

        double qtdExistente = getQuantidadeProduto( cod, idArmazem, conexao );
        double qtdUpdate = (qtdExistente - quantidade);
//        System.out.println( "QTD_UPDATE: " + qtdUpdate );
        String sql = "UPDATE tb_stock SET quantidade_existente =  " + qtdUpdate
                + " WHERE cod_produto_codigo = " + cod + " AND  cod_armazem = " + idArmazem;
        return conexao.executeUpdate( sql );

    }

    public double getQuantidadeProduto( int cod_produto, int idArmazem )
    {
        String sql = "SELECT quantidade_existente FROM  tb_stock WHERE  cod_produto_codigo = " + cod_produto + " AND cod_armazem = " + idArmazem;
        ResultSet rs = conexao.executeQuery( sql );
        try
        {
            if ( rs.next() )
            {
                return rs.getDouble( "quantidade_existente" );
            }
        }
        catch ( SQLException ex )
        {
            ex.printStackTrace();
            return 0;
        }

        return 0;
    }

    public Vector<BuscaModeloProduto> getFonteComprasSemRefeicao( int idArmazem, int codJanela )
    {

        BDConexao conexaoTemp = new BDConexao();

        String codicao = ( codJanela != DVML.JANELA_COMPRA ) ? "IN(1,2)" : "IN(2)";
        Vector<BuscaModeloProduto> busca = new Vector<>();
        String sql = "SELECT "
                + "	p.codigo AS codigo , "
                + "     p.designacao AS designacao, "
                + "	tp.designacao AS categoria, "
                + "	f.pk_familia AS cod_familia, "
                + "     ("
                + "		 SELECT  quantidade_existente FROM tb_stock WHERE cod_produto_codigo = p.codigo AND cod_armazem = " + idArmazem
                + "     ) AS qtd, "
                + "     ("
                + "		  ("
                + "			(SELECT  quant_baixa FROM tb_stock WHERE cod_produto_codigo = p.codigo AND cod_armazem = " + idArmazem + ")  < "
                + "			(SELECT  quantidade_existente FROM tb_stock WHERE cod_produto_codigo = p.codigo AND cod_armazem =    " + idArmazem + ") "
                + "		  )"
                + "          AND  "
                + "		  ( "
                + "                     (SELECT  quantidade_existente FROM tb_stock WHERE cod_produto_codigo = p.codigo AND cod_armazem = " + idArmazem + ") <"
                + "                     (SELECT  quant_critica FROM tb_stock WHERE cod_produto_codigo = p.codigo AND cod_armazem = " + idArmazem + ") "
                + "		  ) "
                + "     ) AS estado_critico , "
                + "	 ("
                + "       SELECT preco_venda FROM tb_preco where fk_produto = p.codigo AND qtd_baixo = 0 ORDER BY pk_preco DESC LIMIT 1 "
                + "	 ) AS preco_venda "
                + "FROM tb_produto p "
                + "INNER JOIN tb_tipo_produto tp ON tp.codigo = p.cod_Tipo_Produto "
                + "INNER JOIN familia f ON f.pk_familia = tp.fk_familia "
                + " WHERE p.fk_grupo <> 3 AND f.pk_familia " + codicao + " "
                //  + "AND p.codigo BETWEEN 1 AND 30  "
                + "GROUP BY p.codigo "
                + "ORDER BY p.designacao ";
//        String sql = "SELECT "
//                + "	p.codigo AS codigo , "
//                + "     p.designacao AS designacao, "
//                + "	tp.designacao AS categoria, "
//                + "	f.pk_familia AS cod_familia, "
//                + "     ("
//                + "		 SELECT  quantidade_existente FROM tb_stock WHERE cod_produto_codigo = p.codigo AND cod_armazem = " + idArmazem
//                + "     ) AS qtd, "
//                + "     ("
//                + "		  ("
//                + "			(SELECT  quant_baixa FROM tb_stock WHERE cod_produto_codigo = p.codigo AND cod_armazem = " + idArmazem + ")  < "
//                + "			(SELECT  quantidade_existente FROM tb_stock WHERE cod_produto_codigo = p.codigo AND cod_armazem =    " + idArmazem + ") "
//                + "		  )"
//                + "          AND  "
//                + "		  ( "
//                + "                     (SELECT  quantidade_existente FROM tb_stock WHERE cod_produto_codigo = p.codigo AND cod_armazem = " + idArmazem + ") <"
//                + "                     (SELECT  quant_critica FROM tb_stock WHERE cod_produto_codigo = p.codigo AND cod_armazem = " + idArmazem + ") "
//                + "		  ) "
//                + "     ) AS estado_critico , "
//                + "	 ("
//                + "         SELECT preco_venda FROM tb_preco WHERE pk_preco = (  SELECT max(pk_preco) FROM tb_preco WHERE fk_produto = p.codigo AND qtd_baixo = ( SELECT MIN(qtd_baixo) AS QTD_MIN FROM tb_preco WHERE fk_produto = p.codigo GROUP BY fk_produto  ) )"
//                + "	 ) AS preco_venda "
//                + "FROM tb_produto p "
//                + "INNER JOIN tb_tipo_produto tp ON tp.codigo = p.cod_Tipo_Produto "
//                + "INNER JOIN familia f ON f.pk_familia = tp.fk_familia "
//                + " WHERE f.pk_familia " + codicao + " "
//                + "AND p.codigo BETWEEN 31 AND 33  "
//                + "GROUP BY p.codigo "
//                + "ORDER BY p.designacao ";

        System.out.println( sql );
        ResultSet rs = conexaoTemp.executeQuery( sql );
        try
        {
            while ( rs.next() )
            {

                int codigo = rs.getInt( "codigo" );
//                System.out.println( "CODIGO: " + codigo );
                String designacao = rs.getString( "designacao" );
                String categoria = rs.getString( "categoria" );
                String qtd = (( rs.getLong( "cod_familia" ) == 1 ) ? "-" : String.valueOf( rs.getLong( "qtd" ) ));
                double precoVenda = rs.getDouble( "preco_venda" );

                String estadoCritico = (( rs.getInt( "cod_familia" ) == 1 ) ? "-" : rs.getString( "estado_critico" ));

                if ( Objects.isNull( estadoCritico ) || estadoCritico.equals( "0" ) )
                {
                    estadoCritico = "false";
                }
                else if ( estadoCritico.equals( "1" ) )
                {
                    estadoCritico = "true";
                }

                BuscaModeloProduto bmp = new BuscaModeloProduto();
                bmp.setCodigo( codigo );
                bmp.setDesignacao( designacao );
                bmp.setCategoria( categoria );
                bmp.setQtd( qtd );
                bmp.setPrecoVenda( precoVenda );
                bmp.setEstadoCritico( estadoCritico );

                busca.add( bmp );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        System.out.println( "SIZE_: " + busca.size() );

        conexaoTemp.close();

        return busca;
    }

    public static double getQuantidadeProduto( int cod_produto, int idArmazem, BDConexao conexao )
    {
        String sql = "SELECT quantidade_existente FROM  tb_stock WHERE  cod_produto_codigo = " + cod_produto + " AND cod_armazem = " + idArmazem;
        ResultSet rs = conexao.executeQuery( sql );
        try
        {
            if ( rs.next() )
            {
                return rs.getDouble( "quantidade_existente" );
            }
        }
        catch ( SQLException ex )
        {
            ex.printStackTrace();
            return 0;
        }

        return 0;
    }

//    public double getQuantidadeProduto( int cod_produto, int idArmazem )
//    {
//        String sql = "SELECT quantidade_existente FROM  tb_stock WHERE  cod_produto_codigo = " + cod_produto + " AND cod_armazem = " + idArmazem;
//        ResultSet rs = conexao.executeQuery( sql );
//        try
//        {
//            if ( rs.next() )
//            {
//                return rs.getDouble( "quantidade_existente" );
//            }
//        }
//        catch ( SQLException ex )
//        {
//            ex.printStackTrace();
//            return 0;
//        }
//
//        return 0;
//    }
}
