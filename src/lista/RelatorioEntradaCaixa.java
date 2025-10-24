/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lista;

//import entity.Confirmacao;
import java.io.File;
import java.sql.Connection;
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
 * @author Domingos Dala Vunge
 */

public  class RelatorioEntradaCaixa {
    
     private BDConexao conexao  = BDConexao.getInstancia();
    private int pk_entrada_tesouraria;
 
    public RelatorioEntradaCaixa(int pk_entrada_tesouraria) throws SQLException {
    
   
        this.pk_entrada_tesouraria = pk_entrada_tesouraria;

        mostrar();
        
    }
  
    
    
    public void mostrar() throws SQLException{

        java.sql.Connection connection = conexao.getConnectionAtiva();
        HashMap hashMap = new HashMap();
  
       
        hashMap.put("ID_ENTRADA",  this.pk_entrada_tesouraria);
       
        
        //obter o path do relatorio
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

    
   private String getCaminho()
    {

            return "relatorios/entradaCaixa.jasper";

    
    }
    
    public static void main(String[] args) throws SQLException {
       RelatorioEntradaCaixa relatorioSaidaCaixa = new RelatorioEntradaCaixa(1);
    }
 
   
}
