/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util.excel;

import entity.Grupo;
import entity.Modelo;
import entity.TbFornecedor;
import entity.TbLocal;
import entity.TbProduto;
import entity.TbTipoProduto;
import entity.Unidade;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import util.DVML;

/**
 *
 * @author Domingos Dala Vunge
 */
public class LerUtil
{

    public static void main( String[] args )
    {
        LerUtil l = new LerUtil();

        l.ler();
    }

    private void ler()
    {

        System.out.println( "inciar: " );
        String file = DVML.CAMINHO_DOCUMENTO + "/novo_file.xls";
        File arquivoAnalistas = new File( file );

        List<DadosUtil> listaItem = new ArrayList<>();

        try
        {
            BufferedInputStream bufAnalistas = new BufferedInputStream(
                    new FileInputStream( arquivoAnalistas ) );

            POIFSFileSystem fileSystem = new POIFSFileSystem( bufAnalistas );

            HSSFWorkbook workbook = new HSSFWorkbook( fileSystem );
            HSSFSheet sheet = workbook.getSheetAt( 0 );

            Iterator linhas = sheet.rowIterator();

            while ( linhas.hasNext() )
            {
                HSSFRow linha = ( HSSFRow ) linhas.next();
                Iterator celulas = linha.cellIterator();
                int row = linha.getRowNum();

                if ( row > 0 )
                {
                    DadosUtil dadosUtil = new DadosUtil();
                    while ( celulas.hasNext() )
                    {
                        HSSFCell celula = ( HSSFCell ) celulas.next();
                        int z = celula.getColumnIndex();

                        System.err.println( "ROW: " + row );

                        switch ( z )
                        {
                            case 2:
                                dadosUtil.setCategoria( celula.toString() );
                                break;
                            case 3:
                                dadosUtil.setCodManual( celula.toString() );
                                break;
                            case 5:
                                dadosUtil.setDesignacao( celula.toString() );
                                break;
                            case 6:
                                dadosUtil.setPrecoVenda( new BigDecimal( Double.parseDouble( celula.toString() ) ).setScale( 2, BigDecimal.ROUND_UP ) );
                                break;

                        }

                    }
                    if ( Objects.nonNull( dadosUtil ) )
                    {
                        listaItem.add( dadosUtil );

                    }

                }

            }

            Iterator<DadosUtil> iterator = listaItem.iterator();
            while ( iterator.hasNext() )
            {
                DadosUtil next = iterator.next();
                if ( Objects.nonNull( next.getCodManual() ) )
                {
                    System.out.println( "*****************************************************" );
                    System.out.println( "Cod Manual: " + next.getCodManual() );
                    System.out.println( "Designacacao: " + next.getDesignacao() );
                    System.out.println( "Categoria: " + next.getCategoria() );
                    System.out.println( "Preco: " + next.getCategoria() );
                    System.out.println( "*****************************************************" );
                }

            }

        }
        catch ( IOException ex )
        {
            Logger.getLogger( LerUtil.class.getName() ).log( Level.SEVERE, null, ex );
        }

    }

    private void importar()
    {

        TbProduto produto = new TbProduto();
        preparar_produto( produto, "", BigDecimal.ZERO, "", 0 );
    }

    private void preparar_produto( TbProduto produto, String designacao, BigDecimal preco, String codManual, int idTipoProduto )
    {

        boolean isStocavel = false;

        String designacao_produto = designacao;
        produto.setDesignacao( designacao_produto );
        produto.setPreco( preco );
        produto.setDataFabrico( new Date() );
        produto.setDataExpiracao( new Date() );
        produto.setCodBarra( "2147483647" );
        produto.setStatus( "Activo" );
        produto.setDataEntrada( new Date() );
        produto.setStocavel( isStocavel ? "true" : "false" );
        produto.setPrecoVenda( 0d );
        produto.setQuantidadeDesconto( 0 );
        produto.setCodigoManual( codManual );
        produto.setCodUnidade( new Unidade( 1 ) );
        produto.setCodLocal( new TbLocal( 1 ) );
        produto.setCodFornecedores( new TbFornecedor( 1 ) );
        produto.setCodTipoProduto( new TbTipoProduto( idTipoProduto ) );
        produto.setFkModelo( new Modelo( 1 ) );
        produto.setFkGrupo( new Grupo( 1 ) );
        produto.setStatusIva( "false" );
//        produto.setPercentagemDesconto( 0d );
        produto.setCozinha( "Nao Enviar Ticket" );
        produto.setPhoto( null );

    }

}
