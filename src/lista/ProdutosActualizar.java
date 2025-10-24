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
public class ProdutosActualizar {
  
     private BDConexao conexao  = BDConexao.getInstancia();
     private int codigo;
     private float valor_entregue, troco;
     private boolean performance;
     private int id_armazem = 0, id_fornecedor = 0;
   
    public ProdutosActualizar( int id_armazem)
    {
        this.id_armazem = id_armazem;
        mostrarVenda();
    }

      public ProdutosActualizar( int id_armazem, int id_fornecedor)
    {
        this.id_armazem = id_armazem;
        this.id_fornecedor = id_fornecedor;
        mostrarVendaFornecedor();
    }
    
    public void mostrarVenda()
    {
        
        java.sql.Connection connection = conexao.getConnectionAtiva();
        HashMap hashMap = new HashMap();
        hashMap.put("ID_ARMAZEM", this.id_armazem );
        
        String relatorio = "relatorios/Produtos_Actualizar.jasper";
       
        File file = new File(relatorio).getAbsoluteFile();
        String obterCaminho = file.getAbsolutePath();

        try {
            
            JasperFillManager.fillReport(obterCaminho, hashMap, connection);
            JasperPrint jasperPrint = JasperFillManager.fillReport(obterCaminho, hashMap, connection);

            if (jasperPrint.getPages().size() >= 1) {
                JasperViewer jasperViewer = new JasperViewer(jasperPrint, false);    
                jasperViewer.setVisible(true);
                
            } else {
                JOptionPane.showMessageDialog(null, "Nao há produtos para a compra!...", "AVISO", JOptionPane.WARNING_MESSAGE);
            }
        } catch (JRException jex) {
            jex.printStackTrace();
            //System.out.println("aqui");
            JOptionPane.showMessageDialog(null, "FALHA AO TENTAR MOSTRAR OS PRODUTOS A COMPRAR!...");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "ERRO AO TENTAR OS PRODUTOS A COMPRAR!...");
        }
        
        
    }
    
    public void mostrarVendaFornecedor()
    {
        
        java.sql.Connection connection = conexao.getConnectionAtiva();
        HashMap hashMap = new HashMap();
        hashMap.put("ID_ARMAZEM", this.id_armazem );
        hashMap.put("ID_FORNECEDOR", this.id_fornecedor );
        String relatorio = "relatorios/Produtos_Actualizar_Fornecedor.jasper";
       
        File file = new File(relatorio).getAbsoluteFile();
        String obterCaminho = file.getAbsolutePath();

        try {
            
            JasperFillManager.fillReport(obterCaminho, hashMap, connection);
            JasperPrint jasperPrint = JasperFillManager.fillReport(obterCaminho, hashMap, connection);

            if (jasperPrint.getPages().size() >= 1) {
                JasperViewer jasperViewer = new JasperViewer(jasperPrint, false);    
                jasperViewer.setVisible(true);
                
            } else {
                JOptionPane.showMessageDialog(null, "Nao há produtos para a compra!...", "AVISO", JOptionPane.WARNING_MESSAGE);
            }
        } catch (JRException jex) {
            jex.printStackTrace();
            //System.out.println("aqui");
            JOptionPane.showMessageDialog(null, "FALHA AO TENTAR MOSTRAR OS PRODUTOS A COMPRAR!...");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "ERRO AO TENTAR OS PRODUTOS A COMPRAR!...");
        }
        
        
    }
    
    
    
    public static void main(String[] args) {
        new ProdutosActualizar(1,27);
    }
    
}
