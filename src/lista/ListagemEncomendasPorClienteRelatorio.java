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
public class ListagemEncomendasPorClienteRelatorio {
    
   private  BDConexao conexao  = BDConexao.getInstancia();
   private Integer idClienteEncomenda = 0;
  
    public ListagemEncomendasPorClienteRelatorio(Integer idClienteEncomenda)
    {
        this.idClienteEncomenda = idClienteEncomenda;
        mostrarProdutos();
    }
    
   
    public void mostrarProdutos() {
        java.sql.Connection connection = conexao.getConnectionAtiva();
        HashMap hashMap = new HashMap();
        String relatorio = "";

        hashMap.put("ID_CLIENTE", this.idClienteEncomenda);
        relatorio = "relatorios/ListarTodasEncomendas.jasper";
       
        File file = new File(relatorio).getAbsoluteFile();
        String obterCaminho = file.getAbsolutePath();

        try {
            
            JasperFillManager.fillReport(obterCaminho, hashMap, connection);

            JasperPrint jasperPrint = JasperFillManager.fillReport(obterCaminho, hashMap, connection);

            if (jasperPrint.getPages().size() >= 1) {
                JasperViewer jasperViewer = new JasperViewer(jasperPrint, false);

                jasperViewer.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(null, "Nao Existe nehuma encomenda deste cliente!...");
            }
        } catch (JRException jex) {
            jex.printStackTrace();
            //System.out.println("aqui");
            JOptionPane.showMessageDialog(null, "FALHA AO TENTAR MOSTRAR AS ENCOMENDAS!...");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "ERRO AO TENTAR AS ENCOMENDAS!...");
        }
    }
 
}
