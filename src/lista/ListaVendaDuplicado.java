/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lista;


import java.sql.Connection;
import java.io.File;
import java.sql.SQLException;
import java.util.HashMap;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.HashPrintServiceAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.PrintServiceAttributeSet;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.PrinterName;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPrintServiceExporter;
import net.sf.jasperreports.engine.export.JRPrintServiceExporterParameter;
import net.sf.jasperreports.view.JasperViewer;
import util.BDConexao;

/**
 *
 * @author Domingos Dala Vunge
 */
public class ListaVendaDuplicado {
    
    
    
     private BDConexao conexao  = BDConexao.getInstancia();
     private int codigo;
     private double valor_entregue, troco;
     private boolean performance, a5;

     
     
     
     
     
    public ListaVendaDuplicado(int codigo, boolean performance, boolean a5)
    {
  
        this.codigo = codigo;
        this.performance = performance;
        this.a5 = a5;
       
        mostrarVenda();
    
    }

    
    
    public void mostrarVenda(){
        
        java.sql.Connection connection = conexao.getConnectionAtiva();
        HashMap hashMap = new HashMap();
        
        
        hashMap.put("CODIGO_VENDA", this.codigo);
       
     
        
        String relatorio = getCaminho();
       
        File file = new File(relatorio).getAbsoluteFile();
        String obterCaminho = file.getAbsolutePath();

        try {
            JasperFillManager.fillReport(obterCaminho, hashMap, connection);

            JasperPrint jasperPrint = JasperFillManager.fillReport(obterCaminho, hashMap, connection);

            if ( jasperPrint.getPages().size() >= 1 ) {
                
                JasperViewer jasperViewer = new JasperViewer(jasperPrint, false);
                jasperViewer.setVisible(false);
               
                //Imprime directamente
                if(!performance)
                    JasperPrintManager.printReport(jasperPrint, false);       
                
            } else {
                JOptionPane.showMessageDialog(null, "Nao Existem Vendas!...");
            }
        } catch (JRException jex) {
            jex.printStackTrace();
            //System.out.println("aqui");
            JOptionPane.showMessageDialog(null, "FALHA AO TENTAR MOSTRAR A FACTURA!...");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "ERRO AO EFECTUAR A FACTURA!...");
        }
    }
    
    //factura - perfoma.jasper
    
    // caso caixa
    public String getCaminho()
    {
        
        if(!performance)
        {
            if(a5)
                  return "relatorios/factura_A5_1.jasper";
            return "relatorios/facturaA4.jasper";
        }
        else return "relatorios/proforma.jasper";
        
    }
    
    public String getCaminhoEncomenda(){
               return "relatorios/factura - perfoma.jasper";
    }
    

    public static void main(String[] args) throws JRException, SQLException {
          new ListaVendaDuplicado(1, false, false);
    }
      
}
