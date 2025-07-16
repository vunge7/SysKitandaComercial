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

public  class RelatorioAdiantamentoCaixa {
    
     private BDConexao conexao  = new BDConexao();
    private int pk_saida_tesouraria;
 
    public RelatorioAdiantamentoCaixa(int pk_saida_tesouraria) throws SQLException {
    
   
        this.pk_saida_tesouraria = pk_saida_tesouraria;

        mostrar();
        
    }
  
    
    
    public void mostrar() throws SQLException{

      
        Connection connection = (Connection) conexao.conectar();
        HashMap hashMap = new HashMap();
  
       
        hashMap.put("ID_SAIDA",  this.pk_saida_tesouraria);
       
        
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

            return "relatorios/adiantamentoCaixa.jasper";

    
    }
    
    public static void main(String[] args) throws SQLException {
       RelatorioAdiantamentoCaixa relatorioSaidaCaixa = new RelatorioAdiantamentoCaixa(1);
    }
 
   
}
