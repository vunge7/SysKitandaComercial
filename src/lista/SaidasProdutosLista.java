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
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.view.JasperViewer;
import util.BDConexao;

/**
 *
 * @author Martinho Luis
 */
public class SaidasProdutosLista {
    
    
    
     private BDConexao conexao  = BDConexao.getInstancia();
     private int codigo;
  
    public SaidasProdutosLista(int codigo)
    {
  
        this.codigo = codigo;
       
       
        mostrarSaida();
    
    }

    
    
    public void mostrarSaida(){
        
        java.sql.Connection connection = conexao.getConnectionAtiva();
        HashMap hashMap = new HashMap();
        
        
        hashMap.put("COD_SAIDA", this.codigo);
       
        String relatorio = getCaminho();
       
        File file = new File(relatorio).getAbsoluteFile();
        String obterCaminho = file.getAbsolutePath();

        try {
            JasperFillManager.fillReport(obterCaminho, hashMap, connection);

            JasperPrint jasperPrint = JasperFillManager.fillReport(obterCaminho, hashMap, connection);

            if ( jasperPrint.getPages().size() >= 1 ) {
                
                JasperViewer jasperViewer = new JasperViewer(jasperPrint, false);
                jasperViewer.setVisible(true);
     
            } else {
                JOptionPane.showMessageDialog(null, "Não Existem Requisições!...");
            }
        } catch (JRException jex) {
            jex.printStackTrace();
            //System.out.println("aqui");
            JOptionPane.showMessageDialog(null, "FALHA AO TENTAR MOSTRAR A REQUISIÇÃO!...");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "ERRO AO EFECTUAR A REQUISIÇÃO...");
        }
    }
    
    // caso caixa
    public String getCaminho()
    {
        
       return "relatorios/factura_saida_prod_A14.jasper";
        
    }
    
    public String getCaminhoEncomenda(){
               return "relatorios/factura_saida_prod_A14.jasper";
    }
    

    public static void main(String[] args)  {
          new SaidasProdutosLista(1);
    }
      
}
    
    
    
    
    
   

