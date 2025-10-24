/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lista;


import java.sql.Connection;
import java.io.File;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.HashPrintServiceAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.PrintServiceAttributeSet;
import javax.print.attribute.standard.*;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRPrintServiceExporter;
import net.sf.jasperreports.engine.export.JRPrintServiceExporterParameter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import util.BDConexao;
import util.JPAEntityMannagerFactoryUtil;
import static util.DVML.CAMINHO_REPORT;
import static util.DVML.NAME_SOFTWARE;
import static util.DVML.VERSION_SOFTWARE;

/**
 *
 * @author Domingos Dala Vunge
 */
public class ImprimirCozinha
{

    private EntityManagerFactory emf = JPAEntityMannagerFactoryUtil.em;
    private BDConexao conexao = BDConexao.getInstancia();
    private int cod_pedido, cod_produto;
    String impressoraCozinha = "EPSON TM-T88V Receipt";

    public ImprimirCozinha( int cod_pedido, int cod_produto )
    {

        try
        {

            this.cod_pedido = cod_pedido;
            this.cod_produto = cod_produto;

            HashMap hashMap = new HashMap();

            hashMap.put( "CODIGO_PEDIDO", cod_pedido );
            hashMap.put( "CODIGO_PRODUTO", cod_produto );

            printReportToPrinterFILTRAR( CAMINHO_REPORT, impressoraCozinha, hashMap );
//            mostrarVendaFiltrada();

        }
        catch ( Exception ex )
        {
            Logger.getLogger( ImprimirCozinha.class.getName() ).log( Level.SEVERE, null, ex );
        }

    }

    private void printReportToPrinterFILTRAR( String report, String impressora, HashMap parameters ) throws JRException
    {
        /*obtendo a conexão*/
        java.sql.Connection connection = conexao.getConnectionAtiva();
        /*configurando os parametros para a submisão no ficheiro*/
        report = getCaminho();

        /*obtendo o caminho do ficheiro (.jrxml)*/
        String caminho = CAMINHO_REPORT + report;

        System.err.println( "impressora: " + impressora );
        System.err.println( "report: " + CAMINHO_REPORT + report );

        /*lendo o ficheiro*/
        JasperDesign jsd = JRXmlLoader.load( caminho );
        /*compliando o ficheiro*/
        JasperReport jr = JasperCompileManager.compileReport( jsd );
        /*preenche o report com os parametros*/
        JasperPrint jpx = JasperFillManager.fillReport( jr, parameters, connection );
        /*configurando os paramentros para a requisição do serviço na empressora*/
        PrintRequestAttributeSet printRequestAttributeSet = new HashPrintRequestAttributeSet();
        printRequestAttributeSet.add( MediaSizeName.ISO_A6 ); //setting page size
        printRequestAttributeSet.add( new Copies( 1 ) );
//        printRequestAttributeSet.add ( new PageRanges (1, Integer.MAX_VALUE));

        /*selecionando a empressora*/
//        String impressdora = "HP DeskJet 2545 series";
        PrinterName printerName = new PrinterName( listar_impressora( impressora ), null ); //gets printer 
        /*preparando a consiguração dos serviços da empressa*/
        PrintServiceAttributeSet printServiceAttributeSet = new HashPrintServiceAttributeSet();

//adiciona a empressora no serviço
        printServiceAttributeSet.add( printerName );
//        printServiceAttributeSet.add ( new PageRanges (1, 10000) );

        /*preparando o serviço de exportação da empressa*/
        JRPrintServiceExporter exporter = new JRPrintServiceExporter();

        exporter.setParameter( JRExporterParameter.JASPER_PRINT, jpx );
        exporter.setParameter( JRPrintServiceExporterParameter.PRINT_REQUEST_ATTRIBUTE_SET, printRequestAttributeSet );
        exporter.setParameter( JRPrintServiceExporterParameter.PRINT_SERVICE_ATTRIBUTE_SET, printServiceAttributeSet );

        exporter.setParameter( JRPrintServiceExporterParameter.START_PAGE_INDEX, 1 );
        exporter.setParameter( JRPrintServiceExporterParameter.END_PAGE_INDEX, Integer.MAX_VALUE );

        exporter.setParameter( JRPrintServiceExporterParameter.DISPLAY_PAGE_DIALOG, Boolean.FALSE );
        exporter.setParameter( JRPrintServiceExporterParameter.DISPLAY_PRINT_DIALOG, Boolean.FALSE );

        exporter.exportReport();

        System.err.println( "report" + report );
        System.err.println( "impressora" + impressora );

    }

    public String getCaminho()
    {
//        return CAMINHO_REPORT + "cozinha_print.jasper.jrxml";
        return CAMINHO_REPORT + "cozinha_print.jasper";
    }

    private String listar_impressora( String name )
    {

        PrintService[] printServices = PrintServiceLookup.lookupPrintServices( null, null );

        for ( PrintService printService : printServices )
        {
            String name1 = printService.getName();
            System.out.println( "Nome Impressora: " + printService.getName() );
            if ( name1.contains( name ) )
            {
                return name1;
            }

        }
        System.out.println( "not found" );
        return null;

    }

    public void mostrarVendaFiltrada()
    {

        java.sql.Connection connection = conexao.getConnectionAtiva();
        HashMap hashMap = new HashMap();

        hashMap.put( "CODIGO_PEDIDO", cod_pedido );
        hashMap.put( "CODIGO_PRODUTO", cod_produto );

        String relatorio = getCaminho();

        File file = new File( relatorio ).getAbsoluteFile();
        String obterCaminho = file.getAbsolutePath();

        try
        {
            JasperFillManager.fillReport( obterCaminho, hashMap, connection );

            JasperPrint jasperPrint = JasperFillManager.fillReport( obterCaminho, hashMap, connection );

            if ( jasperPrint.getPages().size() >= 1 )
            {

                JasperViewer jasperViewer = new JasperViewer( jasperPrint, false );
                jasperViewer.setVisible( true );

            }
            else
            {
                JOptionPane.showMessageDialog( null, "Nao Existem Pedidos!..." );
            }
        }
        catch ( JRException jex )
        {
            jex.printStackTrace();
            //System.out.println("aqui");
            JOptionPane.showMessageDialog( null, "FALHA AO TENTAR MOSTRAR O TICKET!..." );
        }
        catch ( Exception ex )
        {
            ex.printStackTrace();
            JOptionPane.showMessageDialog( null, "ERRO AO EFECTUAR O TICKET!..." );
        }
    }

    public static void main( String[] args ) throws JRException, SQLException
    {
        new ImprimirCozinha( 2, 4 );

    }

}
