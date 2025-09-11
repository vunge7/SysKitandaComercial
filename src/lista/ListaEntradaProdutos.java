/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lista;

import com.mysql.jdbc.Connection;
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
 * @author Martinho Luis
 */
public class ListaEntradaProdutos {
    
    
    
     private BDConexao conexao  = new BDConexao();
     private int codigo;
 
    public ListaEntradaProdutos(int codigo)
    {
  
        this.codigo = codigo;
       
        mostrarEntrada();
    
    }

    
    
    public void mostrarEntrada(){
        
        Connection connection = (Connection) conexao.conectar();
        HashMap hashMap = new HashMap();
        
        
        hashMap.put("COD_ENTRADA", this.codigo);
       
     
        
        String relatorio = getCaminho();
       
        File file = new File(relatorio).getAbsoluteFile();
        String obterCaminho = file.getAbsolutePath();

        try {
            
            //JasperFillManager.fillReport(obterCaminho, hashMap, connection);
            JasperFillManager.fillReport(obterCaminho, hashMap, connection);
            JasperPrint jasperPrint = JasperFillManager.fillReport(obterCaminho, hashMap, connection);

            if (jasperPrint.getPages().size() >= 1) {
                JasperViewer jasperViewer = new JasperViewer(jasperPrint, false);
                jasperViewer.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(null, "ERRO AO TENTAR MOSTRAR O COMPROVATIVO!...", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (JRException jex) {
            jex.printStackTrace();
            JOptionPane.showMessageDialog(null, "FALHA AO TENTAR MOSTRAR O COMPROVATIVO");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "ERRO AO TENTAR MOSTRAR O COMPROVATIVO");
        }
    }

    public String getCaminho()
    {

            return "relatorios/factura_entrada_prod_A4.jasper";

    }

    public static void main(String[] args) throws JRException, SQLException {
          new ListaEntradaProdutos(2);
    }
      
}
    
    
    
    
    
   

