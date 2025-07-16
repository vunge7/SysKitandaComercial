/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lista;

import com.mysql.jdbc.Connection;
import java.io.File;
import java.util.HashMap;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import util.BDConexao;

/**
 *
 * @author Domingos Dala Vunge
 */
public class ImprimirCredencial {
    
    
    
     BDConexao conexao  = new BDConexao();
  
    public ImprimirCredencial()
    {
        mostrarVendas();
    }
    
 
    
    public void mostrarVendas() {
        Connection connection = (Connection) conexao.conectar();
        HashMap hashMap = new HashMap();
        String relatorio = "";
        
        hashMap.put("TOTAL_VENDAS", BDConexao.getTotalVendas());
        
        relatorio = "relatorios/credencial.jasper";
    
        File file = new File(relatorio).getAbsoluteFile();
        String obterCaminho = file.getAbsolutePath();

        try {
            
            JasperFillManager.fillReport(obterCaminho, hashMap, connection);

            JasperPrint jasperPrint = JasperFillManager.fillReport(obterCaminho, hashMap, connection);

            if (jasperPrint.getPages().size() >= 1) {
                JasperViewer jasperViewer = new JasperViewer(jasperPrint, false);

                jasperViewer.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(null, "Nao Existem credencial!...");
            }
        } catch (JRException jex) {
            jex.printStackTrace();
            //System.out.println("aqui");
            JOptionPane.showMessageDialog(null, "FALHA AO IMPRIMIR A CREDENCIAL!...");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "FALHA AO IMPRIMIR A CREDENCIAL!...");
        }
    }
    
   
    
}
