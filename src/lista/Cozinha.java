/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lista;

import dao.DadosInstituicaoDao;
import java.sql.Connection;
import java.util.HashMap;
import javax.persistence.EntityManagerFactory;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.HashPrintServiceAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.PrintServiceAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.PrinterName;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPrintServiceExporter;
import net.sf.jasperreports.engine.export.JRPrintServiceExporterParameter;
import net.sf.jasperreports.view.JasperViewer;
import util.BDConexao;
import util.JPAEntityMannagerFactoryUtil;

/**
 *
 * @author Administrator
 */
public class Cozinha {

    private EntityManagerFactory emf = JPAEntityMannagerFactoryUtil.em;
    private BDConexao conexao = BDConexao.getInstancia();
    private DadosInstituicaoDao dadosInstituicaoDao = new DadosInstituicaoDao(emf);
    private int cod_item_pedido;

    public Cozinha(int cod_item_pedido) {
        try {
            java.sql.Connection connection = conexao.getConnectionAtiva();
            String impressora;
            impressora = dadosInstituicaoDao.findTbDadosInstituicao( 1 ).getImpressoraCozinha();
            
            String jasper = "relatorios/cozinha_print.jasper";
//            Connection conexao = null;
            this.cod_item_pedido = cod_item_pedido;
            System.out.println( "Cod Item"+cod_item_pedido );
//            this.cod_produto = cod_produto;

            HashMap hashMap = new HashMap();

//            hashMap.put("CODIGO_PEDIDO", cod_item_pedido);
//            hashMap.put("CODIGO_PRODUTO", cod_produto);
            hashMap.put("CODIGO_ITEM_PEDIDO", cod_item_pedido);
            System.out.println( "Cod Item"+cod_item_pedido );
            JasperPrint print = JasperFillManager.fillReport(jasper, hashMap, connection);
//            String impressoraSelecionada = "EPSON TM-T88V Receipt";

            JasperViewer jasperViewer = new JasperViewer(print, false);                                
                
//                jasperViewer.setVisible(false);
                jasperViewer.setVisible(false);
                
//                JasperPrintManager.printReport(jasperPrint, false);  

            String impressoraSelecionada = impressora;
            System.out.println( "Impressora da Cozinha: "+impressoraSelecionada );
            PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
            PrintService psSelected = null;
            
            for (PrintService ps : services) {
                if (ps.getName().equals(impressoraSelecionada)) {
                    psSelected = ps;
                    break;
                }

            }
            if (psSelected != null) {
                PrintRequestAttributeSet printRequestAttributeSet = new HashPrintRequestAttributeSet();
                PrintServiceAttributeSet printServiceAttributeSet = new HashPrintServiceAttributeSet();
                printServiceAttributeSet.add(new PrinterName(impressoraSelecionada, null));
                printRequestAttributeSet.add(new Copies(1));
                JRPrintServiceExporter exporter = new JRPrintServiceExporter();
                exporter.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE, psSelected);
                exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
                exporter.setParameter(JRPrintServiceExporterParameter.PRINT_REQUEST_ATTRIBUTE_SET, printRequestAttributeSet);
                exporter.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE_ATTRIBUTE_SET, printServiceAttributeSet);
                exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PAGE_DIALOG, Boolean.FALSE);
                exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PRINT_DIALOG, Boolean.FALSE);
                exporter.exportReport();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
