/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lista;


import java.sql.Connection;
//import java.io.File;
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

public class FacturaEncomenda {
    
    
    
    private  BDConexao conexao  = BDConexao.getInstancia();
    private Integer idEncomenda;
  
    public FacturaEncomenda(Integer idEncomenda)
    {
        this.idEncomenda = idEncomenda;
        mostrarVendas();
    }
    
 
    
    public void mostrarVendas() {
        java.sql.Connection connection = conexao.getConnectionAtiva();
        HashMap hashMap = new HashMap();
        String relatorio = "";
        
        
        
        
        hashMap.put("N_ENCOMENDA", this.idEncomenda);
        
        relatorio = "relatorios/FacturaEncomenda.jasper";
    
        File file = new File(relatorio).getAbsoluteFile();
        String obterCaminho = file.getAbsolutePath();

        try {
            
            JasperFillManager.fillReport(obterCaminho, hashMap, connection);

            JasperPrint jasperPrint = JasperFillManager.fillReport(obterCaminho, hashMap, connection);

            if (jasperPrint.getPages().size() >= 1) {
                JasperViewer jasperViewer = new JasperViewer(jasperPrint, false);

                jasperViewer.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(null, "NAO EXISTE FACTURA PRA ESSA ENCOMENDA!...");
            }
        } catch (JRException jex) {
            jex.printStackTrace();
            //System.out.println("aqui");
            JOptionPane.showMessageDialog(null, "FALHA AO TENTAR MOSTRAR A FACTURA!...");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "FALHA AO TENTAR MOSTRAR A FACTURA!...");
        }
    }
    
   
    
}
