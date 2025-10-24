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
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.view.JasperViewer;
import util.BDConexao;

/**
 *
 * @author Domingos Dala Vunge
 */
public class ImprimirPrecosCodInterno {
    
    
    
    private BDConexao conexao  = BDConexao.getInstancia();
    private int cod_interno = 0;
     
    public ImprimirPrecosCodInterno(int cod_interno)
    {
        
        
        this.cod_interno = cod_interno;       
        mostrar_preco();
        
    }

    public ImprimirPrecosCodInterno() {
            mostrar_preco();
    }
    
    
    public void mostrar_preco(){
        
        java.sql.Connection connection = conexao.getConnectionAtiva();
        HashMap hashMap = new HashMap();        
        hashMap.put("COD_INTERNO", cod_interno);
        
        String relatorio = "relatorios/preco_produto_cod_interno.jasper";
        
       
        File file = new File(relatorio).getAbsoluteFile();
        String obterCaminho = file.getAbsolutePath();

        try {
            JasperFillManager.fillReport(obterCaminho, hashMap, connection);

            JasperPrint jasperPrint = JasperFillManager.fillReport(obterCaminho, hashMap, connection);

            if (jasperPrint.getPages().size() >= 1) {                
                JasperViewer jasperViewer = new JasperViewer(jasperPrint, false);                                
                jasperViewer.setVisible(false);
                JasperPrintManager.printReport(jasperPrint, false);                                      
            } else {
                JOptionPane.showMessageDialog(null, "Nao Existe produto com este codigo!...");
            }
        } catch (JRException jex) {
            jex.printStackTrace();
            //System.out.println("aqui");
            JOptionPane.showMessageDialog(null, "FALHA AO TENTAR MOSTRAR O PREÇO!...");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "ERRO AO TENTAR MOSTRAR OS PREÇOS!...");
        }
    }
    
    //factura - perfoma.jasper
    
    
   
    public static void main(String[] args) {
        new ImprimirPrecosCodInterno( 14   );
    }
    
}
