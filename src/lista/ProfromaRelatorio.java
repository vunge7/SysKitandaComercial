/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lista;

import com.mysql.jdbc.Connection;
import java.io.File;
import java.sql.SQLException;
import java.util.HashMap;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.view.JasperViewer;
import util.BDConexao;

/**
 *
 * @author Domingos Dala Vunge
 */
public class ProfromaRelatorio {
    
    
    
     private BDConexao conexao  = new BDConexao();
     private int codigo;
     private double valor_entregue, troco;
     private boolean performance, a5;

     
    public ProfromaRelatorio(int codigo)
    {
  
        this.codigo = codigo;
       
       
        mostrarVenda();
    
    }

    
    
    public void mostrarVenda(){
        
        Connection connection = (Connection) conexao.conectar();
        HashMap hashMap = new HashMap();
        
        
        hashMap.put("CODIGO_PROFORMA", this.codigo);
       
        String relatorio = getCaminho();
       
        File file = new File(relatorio).getAbsoluteFile();
        String obterCaminho = file.getAbsolutePath();

        try {
            JasperFillManager.fillReport(obterCaminho, hashMap, connection);

            JasperPrint jasperPrint = JasperFillManager.fillReport(obterCaminho, hashMap, connection);

            if ( jasperPrint.getPages().size() >= 1 ) {
                
                JasperViewer jasperViewer = new JasperViewer(jasperPrint, false);
                jasperViewer.setVisible(true);
               
//                //Imprime directamente
//                if(!performance)
//                    JasperPrintManager.printReport(jasperPrint, false);       
                
            } else {
                JOptionPane.showMessageDialog(null, "Nao Existem Proforma!...");
            }
        } catch (JRException jex) {
            jex.printStackTrace();
            //System.out.println("aqui");
            JOptionPane.showMessageDialog(null, "FALHA AO TENTAR MOSTRAR A PROFORMA!...");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "ERRO AO EFECTUAR A PROFORMA...");
        }
    }
    
    //factura - perfoma.jasper
    
    // caso caixa
    public String getCaminho()
    {
        
       return "relatorios/proforma.jasper";
        
    }
    
    public String getCaminhoEncomenda(){
               return "relatorios/factura - perfoma.jasper";
    }
    

    public static void main(String[] args)  {
          new ProfromaRelatorio(6);
    }
      
}
    
    
    
    
    
   

