package util.excel;

import comercial.controller.PrecosController;
import comercial.controller.ProdutosController;
import comercial.controller.StoksController;
import comercial.controller.TipoProdutosController;
import entity.*;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import util.BDConexao;
import util.DVML;

public class LerUtil
{

    // Colunas do Excel
    private final int POS_CATEGORIA = 2;
    private final int POS_COD_MANUAL = 0;
    private final int POS_DESIGNACAO = 3;
    private final int POS_PRECO_COMPRA = 5;
    private final int POS_PRECO = 6;
    private final int POS_COD_BARRA = 7;
    private final int POS_QTD_EXISTENTE = 8;

    private static BDConexao conexao = BDConexao.getInstancia();
    private ProdutosController produtosController;
    private PrecosController precosController;
    private TipoProdutosController tipoProdutosController;
    private StoksController stoksController;

    public LerUtil()
    {
        produtosController = new ProdutosController( conexao );
        precosController = new PrecosController( conexao );
        tipoProdutosController = new TipoProdutosController( conexao );
        stoksController = new StoksController( conexao );
    }

    public static void main( String[] args )
    {
        LerUtil l = new LerUtil();
        l.ler();
    }

    public void ler()
    {
        System.out.println( "Iniciando importação..." );

        String file = DVML.CAMINHO_DOCUMENTO + "/TABELA_PRODUTOS_SAUDE_VIDA.xls";
        File arquivoExcel = new File( file );
        List<DadosUtil> listaItens = new ArrayList<>();

        try ( BufferedInputStream buf = new BufferedInputStream( new FileInputStream( arquivoExcel ) ) )
        {
            POIFSFileSystem fs = new POIFSFileSystem( buf );
            HSSFWorkbook workbook = new HSSFWorkbook( fs );
            HSSFSheet sheet = workbook.getSheetAt( 0 );

            for ( int i = 1; i <= sheet.getLastRowNum(); i++ )
            { // Ignora cabeçalho
                HSSFRow row = sheet.getRow( i );
                if ( row == null )
                {
                    continue;
                }

                DadosUtil dados = new DadosUtil();

                for ( int j = 0; j < row.getLastCellNum(); j++ )
                {
                    HSSFCell cell = row.getCell( j );
                    if ( cell == null )
                    {
                        continue;
                    }

                    switch (j)
                    {
                        case POS_CATEGORIA:
                            dados.setCategoria( cell.toString().trim() );
                            break;
                        case POS_COD_MANUAL:
                            dados.setCodManual( cell.toString().trim() );
                            break;
                        case POS_DESIGNACAO:
                            dados.setDesignacao( cell.toString().trim() );
                            break;
                        case POS_COD_BARRA:
                            switch (cell.getCellType())
                            {
                                case HSSFCell.CELL_TYPE_STRING:
                                    dados.setCodBarra( cell.getStringCellValue().trim() );
                                    break;

                                case HSSFCell.CELL_TYPE_NUMERIC:
                                    // Força a leitura como texto sem notação científica
                                    String codigo = new BigDecimal( cell.getNumericCellValue() )
                                            .toPlainString();
                                    dados.setCodBarra( codigo );
                                    break;

                                default:
                                    dados.setCodBarra( "" );
                            }
                            break;
//                        case POS_COD_BARRA:
//                            dados.setCodBarra(cell.toString().trim());
//                            break;
                        case POS_QTD_EXISTENTE:
                            dados.setStock_actual( parseDouble( cell ) );
                            break;
                        case POS_PRECO:
                            dados.setPrecoVenda( parseBigDecimal( cell ) );
                            break;
                        case POS_PRECO_COMPRA:
                            dados.setPrecoCompra( parseBigDecimal( cell ) );
                            break;
                    }
                }
                listaItens.add( dados );
            }

            // Importar produtos
            int posicao = 0;
            for ( DadosUtil item : listaItens )
            {
                posicao++;
                System.out.println( "Importando " + posicao + " de " + listaItens.size() + ": " + item.getDesignacao() );
                importarProduto( item );
            }

        }
        catch ( IOException ex )
        {
            Logger.getLogger( LerUtil.class.getName() ).log( Level.SEVERE, null, ex );
        }
    }

    private Double parseDouble( HSSFCell cell )
    {
        if ( cell == null )
        {
            return 0d;
        }
        try
        {
            int tipo = cell.getCellType();

            switch (tipo)
            {
                case HSSFCell.CELL_TYPE_NUMERIC:
                    return cell.getNumericCellValue();
                case HSSFCell.CELL_TYPE_STRING:
                    String valor = cell.getStringCellValue().trim();
                    if ( valor.isEmpty() )
                    {
                        return 0d;
                    }
                    valor = valor.replace( ",", "" );
                    return Double.parseDouble( valor );
                default:
                    return 0d;
            }
        }
        catch ( Exception e )
        {
            System.err.println( "Erro ao converter célula para double: " + cell.toString() );
            return 0d;
        }
    }

    private BigDecimal parseBigDecimal( HSSFCell cell )
    {
        if ( cell == null )
        {
            return null;
        }
        try
        {
            int tipo = cell.getCellType();
            String valor = "";

            switch (tipo)
            {
                case HSSFCell.CELL_TYPE_NUMERIC:
                    return BigDecimal.valueOf( cell.getNumericCellValue() ).setScale( 2, RoundingMode.HALF_UP );
                case HSSFCell.CELL_TYPE_STRING:
                    valor = cell.getStringCellValue().trim();
                    if ( valor.isEmpty() )
                    {
                        return null;
                    }
                    valor = valor.replace( ",", "" );
                    return new BigDecimal( valor ).setScale( 2, RoundingMode.HALF_UP );
                case HSSFCell.CELL_TYPE_BLANK:
                    return null;
                default:
                    return null;
            }
        }
        catch ( Exception e )
        {
            System.err.println( "Erro ao converter célula para BigDecimal: " + cell.toString() );
            return null;
        }
    }

    private void importarProduto( DadosUtil dados )
    {
        try
        {
            String categoria = dados.getCategoria();
            TbTipoProduto tipo;

            if ( categoria == null || categoria.trim().isEmpty() )
            {
                System.err.println( "Categoria não informada -> usando categoria padrão" );
                tipo = tipoProdutosController.getTipoFamiliaByDesignacao( "SEM CATEGORIA" );
            }
            else
            {
                tipo = tipoProdutosController.getTipoFamiliaByDesignacao( categoria );
                if ( tipo == null )
                {
                    System.err.println( "Categoria não encontrada: " + categoria + " -> usando categoria padrão" );
                    tipo = tipoProdutosController.getTipoFamiliaByDesignacao( "SEM CATEGORIA" );
                }
            }

            if ( tipo == null )
            {
                throw new RuntimeException( "Categoria padrão 'SEM CATEGORIA' não existe no banco!" );
            }

            TbProduto produto = new TbProduto();
            prepararProduto( produto, dados, tipo.getCodigo() );
            if ( produtosController.salvar( produto ) )
            {
                TbProduto produtoLast = produtosController.findByDesignacao( produto.getDesignacao() );
                registrarPreco( produtoLast.getCodigo(), dados.getPrecoCompra(), dados.getPrecoVenda() );
            }

        }
        catch ( Exception e )
        {
            System.err.println( "Erro ao importar produto " + dados.getDesignacao() + ": " + e.getMessage() );
            e.printStackTrace();
        }
    }

    private void prepararProduto( TbProduto produto, DadosUtil dados, int idTipoProduto )
    {
        produto.setDesignacao( dados.getDesignacao().replace( "'", "''" ) );
        produto.setPreco( dados.getPrecoVenda() );
        produto.setDataFabrico( new Date() );
        produto.setDataExpiracao( new Date() );
        produto.setCodBarra( dados.getCodBarra() );
        produto.setStatus( "Activo" );
        produto.setDataEntrada( new Date() );
        produto.setStocavel( "false" );
        produto.setPrecoVenda( 0d );
        produto.setQuantidadeDesconto( 0 );
        produto.setCodigoManual( dados.getCodManual() );
        produto.setCodUnidade( new Unidade( 1 ) );
        produto.setCodLocal( new TbLocal( 1 ) );
        produto.setCodFornecedores( new TbFornecedor( 1 ) );
        produto.setCodTipoProduto( new TbTipoProduto( idTipoProduto ) );
        produto.setFkModelo( new Modelo( 1 ) );
        produto.setFkGrupo( new Grupo( 1 ) );
        produto.setStatusIva( "false" );
        produto.setPhoto( null );
        produto.setPercentagemDesconto( 0d );
        produto.setCozinha( "Nao Enviar Ticket" );
        produto.setPhoto( null );
        produto.setCodPai( 0 );
        produto.setUnidadeCompra( 0d );
    }

    private void registrarPreco( int idProduto, BigDecimal precoCompra, BigDecimal precoVenda )
    {
        if ( precoCompra == null || precoVenda == null )
        {
            return;
        }

        // Retalho
        TbPreco preco = new TbPreco();
        preco.setFkProduto( new TbProduto( idProduto ) );
        preco.setPrecoCompra( precoCompra );
        preco.setPrecoVenda( precoVenda );
        preco.setPercentagemGanho( BigDecimal.ZERO );
        preco.setData( new Date() );
        preco.setHora( new Date() );
        preco.setQtdBaixo( 0 );
        preco.setQtdAlto( (int) DVML.QTD_DEFAULT - 1 );
        preco.setRetalho( true );
        preco.setFkUsuario( new TbUsuario( 15 ) );
        precosController.salvar( preco );

        // Grosso
        TbPreco precoGrosso = new TbPreco();
        precoGrosso.setFkProduto( new TbProduto( idProduto ) );
        precoGrosso.setPrecoCompra( precoCompra );
        precoGrosso.setPrecoVenda( precoVenda );
        precoGrosso.setPercentagemGanho( BigDecimal.ZERO );
        precoGrosso.setData( new Date() );
        precoGrosso.setHora( new Date() );
        precoGrosso.setQtdBaixo( (int) DVML.QTD_DEFAULT );
        precoGrosso.setQtdAlto( Integer.MAX_VALUE );
        precoGrosso.setRetalho( false );
        precoGrosso.setFkUsuario( new TbUsuario( 15 ) );
        precosController.salvar( precoGrosso );
    }
}

//    private void preparar_produto( TbProduto produto, String designacao, BigDecimal preco, String codManual, int idTipoProduto )
//    {
//
//        boolean isStocavel = false;
//
//        String designacao_produto = designacao;
//        produto.setDesignacao( designacao_produto );
//        produto.setPreco( preco );
//        produto.setDataFabrico( new Date() );
//        produto.setDataExpiracao( new Date() );
//        produto.setCodBarra( "2147483647" );
//        produto.setStatus( "Activo" );
//        produto.setDataEntrada( new Date() );
//        produto.setStocavel( isStocavel ? "true" : "false" );
//        produto.setPrecoVenda( 0d );
//        produto.setQuantidadeDesconto( 0 );
//        produto.setCodigoManual( codManual );
//        produto.setCodUnidade( new Unidade( 1 ) );
//        produto.setCodLocal( new TbLocal( 1 ) );
//        produto.setCodFornecedores( new TbFornecedor( 1 ) );
//        produto.setCodTipoProduto( new TbTipoProduto( idTipoProduto ) );
//        produto.setFkModelo( new Modelo( 1 ) );
//        produto.setFkGrupo( new Grupo( 1 ) );
//        produto.setStatusIva( "true" );
////        produto.setPercentagemDesconto( 0d );
//        produto.setCozinha( "Nao Enviar Ticket" );
//        produto.setPhoto( null );
//        produto.setCodPai(0 );
//        produto.setUnidadeCompra(0d );
//
//    }

//}
