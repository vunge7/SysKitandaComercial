/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lista;


import java.sql.Connection;
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
public class ListarPrecarios {
  
     private BDConexao conexao  = BDConexao.getInstancia();
     private int codigo;
     private float valor_entregue, troco;
     private boolean performance;
    
   
    public ListarPrecarios()
    {        
        mostrarVenda();
    }

    public void mostrarVenda()
    {
        
        java.sql.Connection connection = conexao.getConnectionAtiva();
        HashMap hashMap = new HashMap();
    
        
        String relatorio = "relatorios/preco.jasper";
       
        File file = new File(relatorio).getAbsoluteFile();
        String obterCaminho = file.getAbsolutePath();

        try {
            
            JasperFillManager.fillReport(obterCaminho, hashMap, connection);
            JasperPrint jasperPrint = JasperFillManager.fillReport(obterCaminho, hashMap, connection);

            if (jasperPrint.getPages().size() >= 1) {
                JasperViewer jasperViewer = new JasperViewer(jasperPrint, false);    
                jasperViewer.setVisible(true);
                
            } else {
                JOptionPane.showMessageDialog(null, "Nao há produtos!...", "AVISO", JOptionPane.WARNING_MESSAGE);
            }
        } catch (JRException jex) {
            jex.printStackTrace();
            //System.out.println("aqui");
            JOptionPane.showMessageDialog(null, "FALHA AO TENTAR MOSTRAR!...");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "ERRO AO TENTAR MOSTRAT!...");
        }
        
        
    }
    
    public static void main(String[] args) {
        //new ProdutosActualizar();
    }
    
}
