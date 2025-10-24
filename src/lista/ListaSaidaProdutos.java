/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lista;


import java.sql.Connection;
import java.io.File;
import java.sql.SQLException;
import java.util.HashMap;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import util.BDConexao;

/**
 *
 * @author Martinho Luis
 */
public class ListaSaidaProdutos {
    
    
    
     private BDConexao conexao  = BDConexao.getInstancia();
     private int codigo;
 
    public ListaSaidaProdutos(int codigo)
    {
  
        this.codigo = codigo;
       
        mostrarVenda();
    
    }

    
    
    public void mostrarVenda(){
        
        java.sql.Connection connection = conexao.getConnectionAtiva();
        HashMap hashMap = new HashMap();
        
        
        hashMap.put("COD_SAIDA", this.codigo);
       
     
        
        String relatorio = getCaminho();
       
        File file = new File(relatorio).getAbsoluteFile();
        String obterCaminho = file.getAbsolutePath();

//        try {
//            JasperFillManager.fillReport(obterCaminho, hashMap, connection);
//
//            JasperPrint jasperPrint = JasperFillManager.fillReport(obterCaminho, hashMap, connection);
//
//            if ( jasperPrint.getPages().size() >= 1 ) {
//                
//                JasperViewer jasperViewer = new JasperViewer(jasperPrint, false);
//                jasperViewer.setVisible(true);
//
//                    JasperPrintManager.printReport(jasperPrint, false);       
//                
//            } else {
//                JOptionPane.showMessageDialog(null, "Nao Existem Saidas!...");
//            }
//        } catch (JRException jex) {
//            jex.printStackTrace();
//            JOptionPane.showMessageDialog(null, "FALHA AO TENTAR MOSTRAR o COMPROVATIVO DE SAIDA!...");
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            JOptionPane.showMessageDialog(null, "ERRO AO EFECTUAR A SAIDA!...");
//        }

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

            return "relatorios/factura_saida_prod_A4.jasper";

    }

    public static void main(String[] args) throws JRException, SQLException {
          new ListaSaidaProdutos(1);
    }
      
}
    
    
    
    
    
   

