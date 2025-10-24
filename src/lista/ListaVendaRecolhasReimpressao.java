/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lista;


import java.sql.Connection;
import dao.ItemVendaDao;
import dao.VendaDao;
import entity.TbVenda;
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
import util.DVML;
import util.DVML.Abreviacao;
import util.JPAEntityMannagerFactoryUtil;
import static util.DVML.CAMINHO_REPORT;
import static util.DVML.NAME_SOFTWARE;
import static util.DVML.VERSION_SOFTWARE;

/**
 *
 * @author Domingos Dala Vunge
 */
public class ListaVendaRecolhasReimpressao
{

    private EntityManagerFactory emf = JPAEntityMannagerFactoryUtil.em;
    private VendaDao vendaDao = new VendaDao( emf );
    private ItemVendaDao itemVendaDao = new ItemVendaDao( emf );
    private BDConexao conexao = BDConexao.getInstancia();
    private int codigo, pk_mesas, pk_lugares;
    private double valor_entregue, troco;
    private boolean performance, a5;
//    private String motivo_isencao = "";
    private String status_documento;
//    private DVML.Abreviacao doc_breviacao;
//    private DVML.Abreviacao doc_breviacao;
    private Abreviacao doc_breviacao;

    public ListaVendaRecolhasReimpressao( int cod_venda, Abreviacao doc_abrevicao, int cod_mesa, int pk_lugar, boolean performance, boolean a5, String status_documento )
    {

        try
        {
            String impressora = "Adobe PDF";
            String impressora3 = "EPSON TM-T88V Receipt";
            String impressora2 = "Microsoft Print to PDF";
//            this.motivo_isencao = motivo_isencao;
            this.status_documento = status_documento;
            this.codigo = cod_venda;
            this.doc_breviacao = doc_abrevicao;
            this.pk_mesas = cod_mesa;
            this.pk_lugares = pk_lugar;
            this.performance = performance;
            this.a5 = a5;
            HashMap hashMap = new HashMap();

            hashMap.put( "CODIGO_VENDA", cod_venda );
            hashMap.put( "PARM_MESA", cod_mesa );
            hashMap.put( "PARM_LUGAR", pk_lugar );

//             printReportToPrinter("filtrar_sub_factura_A5_1_pedidos.jrxml"Foxit Reader PDF Printer);
            //printReportToPrinterFILTRAR ( "filtrar_sub_factura_A5_1_pedidos.jrxml", impressora3, hashMap );
//            printReportToPrinter ( "factura_A5_1_pedidos.jrxml", impressora );
//             printReportToPrinter("factura_A5_1_pedidos.jrxml", impressora2);
            mostrarVendaFiltrada();

        }
        catch ( Exception ex )
        {
            Logger.getLogger(ListaVendaRecolhasReimpressao.class.getName() ).log( Level.SEVERE, null, ex );
        }

    }

    //
//    public ListaVenda( int codigo, boolean performance, boolean a5, String status_documento )
    public ListaVendaRecolhasReimpressao( int codigo, Abreviacao doc_abrevicao, boolean performance, boolean a5, String status_documento )
    {
        try
        {
            String impressora = "Adobe PDF";
            String impressora2 = "Microsoft Print to PDF";
            String impressora3 = "EPSON TM-T88V Receipt";
            this.status_documento = status_documento;
            this.doc_breviacao = doc_abrevicao;
            this.codigo = codigo;
            this.performance = performance;
            this.a5 = a5;
            try
            {
                mostrarRecolhaCliente();
                mostrarRecolhaLavandaria();
            }
            catch ( Exception e )
            {
            }
        }
        catch ( Exception ex )
        {
            ex.printStackTrace();
            Logger.getLogger(ListaVendaRecolhasReimpressao.class.getName() ).log( Level.SEVERE, null, ex );
        }

    }

    private void printReportToPrinter( String report, String impressora ) throws JRException
    {
        /*obtendo a conexão*/
        java.sql.Connection connection = conexao.getConnectionAtiva();
        /*configurando os parametros para a submisão no ficheiro*/
        Map parameters = new HashMap();

        parameters.put( "CODIGO_VENDA", this.codigo );
        parameters.put( "CODIGO_MESA", itemVendaDao.getAllItemVendasByIdVenda( codigo ).get( 0 ).getFkMesas().getPkMesas() );
        parameters.put( "DESIGNACAO_MESA", itemVendaDao.getAllItemVendasByIdVenda( codigo ).get( 0 ).getFkMesas().getDesignacao() );

        System.err.println( "CODIGO_VENDA: " + this.codigo );
        System.err.println( "CODIGO_MESA: " + itemVendaDao.getAllItemVendasByIdVenda( codigo ).get( 0 ).getFkMesas().getPkMesas() );
        System.err.println( "DESIGNACAO_MESA: " + itemVendaDao.getAllItemVendasByIdVenda( codigo ).get( 0 ).getFkMesas().getDesignacao() );

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

        List<JRPrintPage> pages = jpx.getPages();

        if ( pages.size() > 0 )
        {
            /*configurando os paramentros para a requisição do serviço na empressora*/
            PrintRequestAttributeSet printRequestAttributeSet = new HashPrintRequestAttributeSet();
            printRequestAttributeSet.add( MediaSizeName.ISO_A6 ); //setting page size
            printRequestAttributeSet.add( new Copies( 1 ) );
//Aqui        
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
        else
        {
            System.err.println( "A Factura não tem paginas..." );
        }

    }

    private void printReportToPrinterFILTRAR( String report, String impressora, HashMap parameters ) throws JRException
    {
        /*obtendo a conexão*/
        java.sql.Connection connection = conexao.getConnectionAtiva();
        /*configurando os parametros para a submisão no ficheiro*/

//        parameters.put ( "CODIGO_VENDA", this.codigo );
//        parameters.put ( "CODIGO_MESA", itemVendaDao.getAllItemVendasByIdVenda ( codigo ).get ( 0 ).getFkMesas ().getPkMesas () );
//        parameters.put ( "DESIGNACAO_MESA", itemVendaDao.getAllItemVendasByIdVenda ( codigo ).get ( 0 ).getFkMesas ().getDesignacao () );
        System.err.println( "CODIGO_VENDA: " + this.codigo );
        System.err.println( "CODIGO_MESA: " + this.pk_mesas );
        System.err.println( "DESIGNACAO_MESA: " + itemVendaDao.getAllItemVendasByIdVenda( codigo ).get( 0 ).getFkMesas().getDesignacao() );

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
//Aqui        
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
//        return CAMINHO_REPORT + "ListarUsuario.jrxml";
        return CAMINHO_REPORT + "filtrar_sub_factura_A5_1_pedidos.jrxml";

    }

    public String getCaminho2()
    {
//        return CAMINHO_REPORT + "ListarUsuario.jrxml";
        return CAMINHO_REPORT + "factura_A5_1_pedidos.jrxml";

    }

    private String getRefCodFact( String ref_cod )
    {

        if ( ref_cod == null )
        {
            return null;
        }
        return "Ref. à Doc." + ref_cod;

    }

    public void mostrarVendaFiltrada()
    {

        java.sql.Connection connection = conexao.getConnectionAtiva();
        HashMap hashMap = new HashMap();

        hashMap.put( "CODIGO_VENDA", this.codigo );
        hashMap.put( "PARM_MESA", this.pk_mesas );
        hashMap.put( "PARM_LUGAR", this.pk_lugares );
//        this.motivo_isencao = "Regime de não Sujeição";
        //this.motivo_isencao = "Regime Transitório";
        hashMap.put( "DOCUMENTO", vendaDao.findTbVenda( codigo ).getFkDocumento().getDesignacao() );
        hashMap.put( "SOFTWARE_VERSION", VERSION_SOFTWARE );
        hashMap.put( "SOFTWARE_NAME", NAME_SOFTWARE );
        hashMap.put( "REF_COD_FACT", getRefCodFact( vendaDao.findTbVenda( codigo ).getRefCodFact() ) );
        hashMap.put( "STATUS_DOCUMENTO", this.status_documento );
//        hashMap.put( "MOTIVO_ISENCAO", this.motivo_isencao );
        hashMap.put( "NIF_CLIENTE_CONSOMIDOR_FINAL", setConsumidorFinal( vendaDao.findTbVenda( codigo ) ) );

        String relatorio = getCaminhoFiltrado();

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

                // Imprime directamente
//                if ( !performance )
//                {
//                    JasperPrintManager.printReport( jasperPrint, false );
//                }
            }
            else
            {
                JOptionPane.showMessageDialog( null, "Nao Existem Vendas!..." );
            }
        }
        catch ( JRException jex )
        {
            jex.printStackTrace();
            //System.out.println("aqui");
            JOptionPane.showMessageDialog( null, "FALHA AO TENTAR MOSTRAR A FACTURA!..." );
        }
        catch ( Exception ex )
        {
            ex.printStackTrace();
            JOptionPane.showMessageDialog( null, "ERRO AO EFECTUAR A FACTURA!..." );
        }
    }

    private long getCodMesa()
    {
        return Long.parseLong( String.valueOf( itemVendaDao.getAllItemVendasByIdVenda( codigo ).get( 0 ).getFkMesas().getPkMesas() ) );
    }

    public void mostrarRecolhaCliente()
    {

        System.err.println( "COD VENDA: " + this.codigo );

        java.sql.Connection connection = conexao.getConnectionAtiva();
        HashMap hashMap = new HashMap();

        hashMap.put( "CODIGO_VENDA", this.codigo );
//        hashMap.put( "CODIGO_MESA", getCodMesa() );
//        hashMap.put( "PARM_LUGAR", this.pk_lugares );
//        hashMap.put( "CODIGO_MESA", itemVendaDao.getAllItemVendasByIdVenda( codigo ).get( 0 ).getFkMesas().getPkMesas() );
        hashMap.put( "DESIGNACAO_MESA", itemVendaDao.getAllItemVendasByIdVenda( codigo ).get( 0 ).getFkMesas().getDesignacao() );

        hashMap.put( "DOCUMENTO", vendaDao.findTbVenda( codigo ).getFkDocumento().getDesignacao() );
        hashMap.put( "SOFTWARE_VERSION", VERSION_SOFTWARE );
        hashMap.put( "SOFTWARE_NAME", NAME_SOFTWARE );
        hashMap.put( "REF_COD_FACT", getRefCodFact( vendaDao.findTbVenda( codigo ).getRefCodFact() ) );
        hashMap.put( "STATUS_DOCUMENTO", this.status_documento );

//        hashMap.put( "MOTIVO_ISENCAO", this.motivo_isencao );
        hashMap.put( "NIF_CLIENTE_CONSOMIDOR_FINAL", setConsumidorFinal( vendaDao.findTbVenda( codigo ) ) );
        String relatorio = getCaminhoFacturaRecolhaCliente();

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
                //Imprime directamente
                if ( !performance )
                {
                    JasperPrintManager.printReport( jasperPrint, false );
                }

            }

            else
            {
                JOptionPane.showMessageDialog( null, "Nao Existem Vendas!..." );
            }
        }
        catch ( JRException jex )
        {
            jex.printStackTrace();
            //System.out.println("aqui");
            JOptionPane.showMessageDialog( null, "FALHA AO TENTAR MOSTRAR A FACTURA!..." );
        }
        catch ( Exception ex )
        {
            ex.printStackTrace();
            JOptionPane.showMessageDialog( null, "ERRO AO EFECTUAR A FACTURA!..." );
        }
    }

    public void mostrarRecolhaLavandaria()
    {

        System.err.println( "COD VENDA: " + this.codigo );

        java.sql.Connection connection = conexao.getConnectionAtiva();
        HashMap hashMap = new HashMap();

        hashMap.put( "CODIGO_VENDA", this.codigo );
//        hashMap.put( "CODIGO_MESA", getCodMesa() );
//        hashMap.put( "PARM_LUGAR", this.pk_lugares );
//        hashMap.put( "CODIGO_MESA", itemVendaDao.getAllItemVendasByIdVenda( codigo ).get( 0 ).getFkMesas().getPkMesas() );
        hashMap.put( "DESIGNACAO_MESA", itemVendaDao.getAllItemVendasByIdVenda( codigo ).get( 0 ).getFkMesas().getDesignacao() );

        hashMap.put( "DOCUMENTO", vendaDao.findTbVenda( codigo ).getFkDocumento().getDesignacao() );
        hashMap.put( "SOFTWARE_VERSION", VERSION_SOFTWARE );
        hashMap.put( "SOFTWARE_NAME", NAME_SOFTWARE );
        hashMap.put( "REF_COD_FACT", getRefCodFact( vendaDao.findTbVenda( codigo ).getRefCodFact() ) );
        hashMap.put( "STATUS_DOCUMENTO", this.status_documento );

//        hashMap.put( "MOTIVO_ISENCAO", this.motivo_isencao );
        hashMap.put( "NIF_CLIENTE_CONSOMIDOR_FINAL", setConsumidorFinal( vendaDao.findTbVenda( codigo ) ) );
        String relatorio = getCaminhoFacturaRecolhaLavandaria();

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
                //Imprime directamente
                if ( !performance )
                {
                    JasperPrintManager.printReport( jasperPrint, false );
                }

            }

            else
            {
                JOptionPane.showMessageDialog( null, "Nao Existem Vendas!..." );
            }
        }
        catch ( JRException jex )
        {
            jex.printStackTrace();
            //System.out.println("aqui");
            JOptionPane.showMessageDialog( null, "FALHA AO TENTAR MOSTRAR A FACTURA!..." );
        }
        catch ( Exception ex )
        {
            ex.printStackTrace();
            JOptionPane.showMessageDialog( null, "ERRO AO EFECTUAR A FACTURA!..." );
        }
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

    //factura - perfoma.jasper
    // caso caixa
    //por lugar
    public String getCaminhoFiltrado()
    {

        return "relatorios/filtrar_sub_factura_A5_1_pedidos.jasper";
//        return "relatorios/facturaA6_mesas.jasper";

    }

    private String setConsumidorFinal( TbVenda venda )
    {
        if ( venda.getCodigoCliente().getCodigo() == 1 )
        {
            return "Consumidor Final";
        }
        return venda.getClienteNif();
    }

    //Mesa completa
    public String getCaminhoFacturaRecolhaCliente()
    {

        return "relatorios/recolha_cliente.jasper";

    }

    public String getCaminhoFacturaRecolhaLavandaria()
    {
//        return "relatorios/factura_A5_1_pedidos.jasper";
        return "relatorios/recolha_lavandaria.jasper";

    }

    public static void main( String[] args ) throws JRException, SQLException
    {
        //new ListaVenda(1, 6, 1, false, true);
        //new ListaVenda(1045, false, true);
        Abreviacao abreviacao = Abreviacao.NL;
        new ListaVendaRecolhasReimpressao( 35, abreviacao, false, true, "Original" );

    }

}
