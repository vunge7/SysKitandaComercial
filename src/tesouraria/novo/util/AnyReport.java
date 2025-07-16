/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tesouraria.novo.util;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.HashPrintServiceAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.PrintServiceAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.PrinterName;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.export.JRPrintServiceExporter;
import net.sf.jasperreports.engine.export.JRPrintServiceExporterParameter;
import net.sf.jasperreports.view.JasperViewer;
import util.BDConexao;

/**
 *
 * @author Domingos Dala Vunge
 */
public class AnyReport
{

    private HashMap hashMap;
    private String file;
    private String impressora, impressora_sala;
    private int codigo;

    public AnyReport( HashMap hashMap, String file )
    {
        this.file = file;
        this.hashMap = hashMap;
        try
        {
            mostrar3();
        }
        catch ( Exception e )
        {
        }

    }

    public AnyReport( HashMap hashMap, String file, boolean imprimir_directo )
    {
        this.file = file;
        this.hashMap = hashMap;
        try
        {
            imprimirDirectamente();
        }
        catch ( Exception e )
        {
        }

    }
    
    public AnyReport( HashMap hashMap, int codigo, String file)
    {
        this.file = file;
        this.hashMap = hashMap;
//        this.impressora = impressora;
        this.codigo = codigo;
        try
        {
            mostrar_reimpressao();
//            mostrar_sala();
        }
        catch ( Exception e )
        {
        }

    }

    public AnyReport( HashMap hashMap, String file, boolean imprimir_directo, String impressora )
    {
        this.file = file;
        this.hashMap = hashMap;
        this.impressora = impressora;
        try
        {
            mostrar2();
//            mostrar_sala();
        }
        catch ( Exception e )
        {
        }

    }

    public AnyReport( HashMap hashMap, String file, String impressora )
    {
        this.file = file;
        this.hashMap = hashMap;
        this.impressora = impressora;
        try
        {
            mostrar2();
//            mostrar_sala();
        }
        catch ( Exception e )
        {
        }

    }

    public void mostrar() throws SQLException
    {
        Connection connection = BDConexao.getConexao();
        //obter o path do relatorio
        String relatorio = getWays();
        String obterCaminho = new File( relatorio ).getAbsoluteFile().getAbsolutePath();
        try
        {
            JasperFillManager.fillReport( obterCaminho, hashMap, connection );
            JasperPrint jasperPrint = JasperFillManager.fillReport( obterCaminho, hashMap, connection );

            if ( jasperPrint.getPages().size() >= 1 )
            {
                JasperViewer jasperViewer = new JasperViewer( jasperPrint, false );
                jasperViewer.setVisible( false );
                JasperPrintManager.printReport( jasperPrint, false );

            }
            else
            {
                JOptionPane.showMessageDialog( null, "Falha ao visualizar o documento.\nNão existe linha no corpo do documento.", "Falha", JOptionPane.ERROR_MESSAGE );
            }
        }
        catch ( JRException jex )
        {
            jex.printStackTrace();
            System.err.println( "Erro: " + jex.getLocalizedMessage() );
            JOptionPane.showMessageDialog( null, "Falha ao visualizar o documento", "Falha", JOptionPane.ERROR_MESSAGE );
        }
        catch ( Exception ex )
        {
            ex.printStackTrace();
            System.err.println( "Erro: " + ex.getLocalizedMessage() );
            JOptionPane.showMessageDialog( null, "Falha ao visualizar o documento", "Falha", JOptionPane.ERROR_MESSAGE );
        }

    }
    
    public void mostrar3() throws SQLException
    {
        Connection connection = BDConexao.getConexao();
        //obter o path do relatorio
        String relatorio = getWays();
        String obterCaminho = new File( relatorio ).getAbsoluteFile().getAbsolutePath();
        try
        {
            JasperFillManager.fillReport( obterCaminho, hashMap, connection );
            JasperPrint jasperPrint = JasperFillManager.fillReport( obterCaminho, hashMap, connection );

            if ( jasperPrint.getPages().size() >= 1 )
            {
                JasperViewer jasperViewer = new JasperViewer( jasperPrint, false );
                jasperViewer.setVisible( true );
//                JasperPrintManager.printReport( jasperPrint, false );

            }
            else
            {
                JOptionPane.showMessageDialog( null, "Falha ao visualizar o documento.\nNão existe linha no corpo do documento.", "Falha", JOptionPane.ERROR_MESSAGE );
            }
        }
        catch ( JRException jex )
        {
            jex.printStackTrace();
            System.err.println( "Erro: " + jex.getLocalizedMessage() );
            JOptionPane.showMessageDialog( null, "Falha ao visualizar o documento", "Falha", JOptionPane.ERROR_MESSAGE );
        }
        catch ( Exception ex )
        {
            ex.printStackTrace();
            System.err.println( "Erro: " + ex.getLocalizedMessage() );
            JOptionPane.showMessageDialog( null, "Falha ao visualizar o documento", "Falha", JOptionPane.ERROR_MESSAGE );
        }

    }

    public void imprimirDirectamente() throws SQLException
    {
        Connection connection = BDConexao.getConexao();
        //obter o path do relatorio
        String relatorio = getWays();
        String obterCaminho = new File( relatorio ).getAbsoluteFile().getAbsolutePath();
        try
        {
            JasperFillManager.fillReport( obterCaminho, hashMap, connection );
            JasperPrint jasperPrint = JasperFillManager.fillReport( obterCaminho, hashMap, connection );

            if ( jasperPrint.getPages().size() >= 1 )
            {
                JasperViewer jasperViewer = new JasperViewer( jasperPrint, false );
                jasperViewer.setVisible( true );
                //Imprime directamente
                JasperPrintManager.printReport( jasperPrint, false );

            }
            else
            {
                JOptionPane.showMessageDialog( null, "Falha ao visualizar o documento.\nNão existe linha no corpo do documento.", "Falha", JOptionPane.ERROR_MESSAGE );
            }
        }
        catch ( JRException jex )
        {
            jex.printStackTrace();
            System.err.println( "Erro: " + jex.getLocalizedMessage() );
            JOptionPane.showMessageDialog( null, "Falha ao visualizar o documento", "Falha", JOptionPane.ERROR_MESSAGE );
        }
        catch ( Exception ex )
        {
            ex.printStackTrace();
            System.err.println( "Erro: " + ex.getLocalizedMessage() );
            JOptionPane.showMessageDialog( null, "Falha ao visualizar o documento", "Falha", JOptionPane.ERROR_MESSAGE );
        }

    }

    public void mostrar2() throws SQLException
    {
        try
        {
            Connection connection = BDConexao.getConexao();

            String jasper = getWays();
            JasperPrint print = JasperFillManager.fillReport( jasper, hashMap, connection );
            JasperViewer jasperViewer = new JasperViewer( print, false );
//                jasperViewer.setVisible(true);
            jasperViewer.setVisible( true );

//                JasperPrintManager.printReport(jasperPrint, false);  
            String impressoraSelecionada = impressora;

            System.out.println( "Impressora da Cozinha: " + impressoraSelecionada );
            PrintService[] services = PrintServiceLookup.lookupPrintServices( null, null );
            PrintService psSelected = null;

            for ( PrintService ps : services )
            {
                if ( ps.getName().equals( impressoraSelecionada ) )
                {
                    psSelected = ps;
                    break;
                }

            }
            if ( psSelected != null )
            {
                PrintRequestAttributeSet printRequestAttributeSet = new HashPrintRequestAttributeSet();
                PrintServiceAttributeSet printServiceAttributeSet = new HashPrintServiceAttributeSet();
                printServiceAttributeSet.add( new PrinterName( impressoraSelecionada, null ) );
                printRequestAttributeSet.add( new Copies( 1 ) );
                JRPrintServiceExporter exporter = new JRPrintServiceExporter();
                exporter.setParameter( JRPrintServiceExporterParameter.PRINT_SERVICE, psSelected );
                exporter.setParameter( JRExporterParameter.JASPER_PRINT, print );
                exporter.setParameter( JRPrintServiceExporterParameter.PRINT_REQUEST_ATTRIBUTE_SET, printRequestAttributeSet );
                exporter.setParameter( JRPrintServiceExporterParameter.PRINT_SERVICE_ATTRIBUTE_SET, printServiceAttributeSet );
                exporter.setParameter( JRPrintServiceExporterParameter.DISPLAY_PAGE_DIALOG, Boolean.FALSE );
                exporter.setParameter( JRPrintServiceExporterParameter.DISPLAY_PRINT_DIALOG, Boolean.FALSE );
                exporter.exportReport();
            }

        }
        catch ( Exception ex )
        {
            ex.printStackTrace();
        }

    }
    public void mostrar_reimpressao() throws SQLException
    {
        try
        {
            Connection connection = BDConexao.getConexao();

            String jasper = getWays();
            JasperPrint print = JasperFillManager.fillReport( jasper, hashMap, connection );
            JasperViewer jasperViewer = new JasperViewer( print, false );
//                jasperViewer.setVisible(false);
            jasperViewer.setVisible( true );

//                JasperPrintManager.printReport(jasperPrint, false);  
//            String impressoraSelecionada = impressora;

//            System.out.println( "Impressora da Cozinha: " + impressoraSelecionada );
            PrintService[] services = PrintServiceLookup.lookupPrintServices( null, null );
            PrintService psSelected = null;

            for ( PrintService ps : services )
            {
//                if ( ps.getName().equals( impressoraSelecionada ) )
//                {
//                    psSelected = ps;
//                    break;
//                }

            }
            if ( psSelected != null )
            {
                PrintRequestAttributeSet printRequestAttributeSet = new HashPrintRequestAttributeSet();
                PrintServiceAttributeSet printServiceAttributeSet = new HashPrintServiceAttributeSet();
//                printServiceAttributeSet.add( new PrinterName( impressoraSelecionada, null ) );
                printRequestAttributeSet.add( new Copies( 1 ) );
                JRPrintServiceExporter exporter = new JRPrintServiceExporter();
                exporter.setParameter( JRPrintServiceExporterParameter.PRINT_SERVICE, psSelected );
                exporter.setParameter( JRExporterParameter.JASPER_PRINT, print );
                exporter.setParameter( JRPrintServiceExporterParameter.PRINT_REQUEST_ATTRIBUTE_SET, printRequestAttributeSet );
                exporter.setParameter( JRPrintServiceExporterParameter.PRINT_SERVICE_ATTRIBUTE_SET, printServiceAttributeSet );
                exporter.setParameter( JRPrintServiceExporterParameter.DISPLAY_PAGE_DIALOG, Boolean.FALSE );
                exporter.setParameter( JRPrintServiceExporterParameter.DISPLAY_PRINT_DIALOG, Boolean.FALSE );
                exporter.exportReport();
            }

        }
        catch ( Exception ex )
        {
            ex.printStackTrace();
        }

    }

//    public void mostrar_sala() throws SQLException
//    {
//        try
//        {
//            Connection connection = BDConexao.getConexao();
//
//            String jasper = getWays();
//            JasperPrint print = JasperFillManager.fillReport( jasper, hashMap, connection );
//            JasperViewer jasperViewer = new JasperViewer( print, false );
////                jasperViewer.setVisible(false);
//            jasperViewer.setVisible( false );
//
////                JasperPrintManager.printReport(jasperPrint, false);  
//            String impressoraSelecionadaSala = impressora_sala;
//
//            System.out.println( "Impressora da Sala: " + impressoraSelecionadaSala );
//            PrintService[] services = PrintServiceLookup.lookupPrintServices( null, null );
//            PrintService psSelected = null;
//
//            for ( PrintService ps : services )
//            {
//                if ( ps.getName().equals( impressoraSelecionadaSala ) )
//                {
//                    psSelected = ps;
//                    break;
//                }
//
//            }
//            if ( psSelected != null )
//            {
//                PrintRequestAttributeSet printRequestAttributeSet = new HashPrintRequestAttributeSet();
//                PrintServiceAttributeSet printServiceAttributeSet = new HashPrintServiceAttributeSet();
//                printServiceAttributeSet.add( new PrinterName( impressoraSelecionadaSala, null ) );
//                printRequestAttributeSet.add( new Copies( 1 ) );
//                JRPrintServiceExporter exporter = new JRPrintServiceExporter();
//                exporter.setParameter( JRPrintServiceExporterParameter.PRINT_SERVICE, psSelected );
//                exporter.setParameter( JRExporterParameter.JASPER_PRINT, print );
//                exporter.setParameter( JRPrintServiceExporterParameter.PRINT_REQUEST_ATTRIBUTE_SET, printRequestAttributeSet );
//                exporter.setParameter( JRPrintServiceExporterParameter.PRINT_SERVICE_ATTRIBUTE_SET, printServiceAttributeSet );
//                exporter.setParameter( JRPrintServiceExporterParameter.DISPLAY_PAGE_DIALOG, Boolean.FALSE );
//                exporter.setParameter( JRPrintServiceExporterParameter.DISPLAY_PRINT_DIALOG, Boolean.FALSE );
//                exporter.exportReport();
//            }
//
//        }
//        catch ( Exception ex )
//        {
//            ex.printStackTrace();
//        }
//
//    }
    private String getWays()
    {
        return "relatorios/" + file;
    }

    public static void main( String[] args )
    {
        HashMap hashMap = new HashMap();
        hashMap.put( "CODIGO_VENDA", 1 );
        hashMap.put( "SOFTWARE_VERSION", "" );
        hashMap.put( "SOFTWARE_NAME", "" );
        hashMap.put( "MOTIVO_ISENCAO", "" );

        new AnyReport( hashMap, "recolha_lavandaria.jasper", false );
//        new AnyReport( hashMap, "ticket_recolha.jasper", false );
//        new AnyReport( hashMap, "ld/factura_recolha_02.jasper", false );

    }

}
