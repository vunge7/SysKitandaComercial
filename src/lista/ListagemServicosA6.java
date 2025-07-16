/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lista;

import com.mysql.jdbc.Connection;
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
public class ListagemServicosA6 {
    
    
    
    private BDConexao conexao  = new BDConexao();
    private int cod_armazem = 0, cod_fornecedor;
    private String armazem = "", fornecedor = "";
    private boolean a4 =  true;
    public ListagemServicosA6(boolean a4)
    {

        this.a4 = a4;
        mostrarProdutos();
    }

      public void mostrarProdutos() {
        Connection connection = (Connection) conexao.conectar();
        HashMap hashMap = new HashMap();
        String relatorio = "";
        
        relatorio = getCaminho();
     
      
       
        File file = new File(relatorio).getAbsoluteFile();
        String obterCaminho = file.getAbsolutePath();

        try {
            
            JasperFillManager.fillReport(obterCaminho, hashMap, connection);

            JasperPrint jasperPrint = JasperFillManager.fillReport(obterCaminho, hashMap, connection);

            if (jasperPrint.getPages().size() >= 1) {
                JasperViewer jasperViewer = new JasperViewer(jasperPrint, false);

                jasperViewer.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(null, "Nao Existem Servicos Cadastrados!...");
            }
        } catch (JRException jex) {
            jex.printStackTrace();
            //System.out.println("aqui");
            JOptionPane.showMessageDialog(null, "FALHA AO TENTAR MOSTRAR OS SERVICOS!...");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "ERRO AO TENTAR OS SERVICOS!...");
        }
    }
 
    private String getCaminho(){
    
            if (a4) {
                 return "relatorios/princ_servico.jasper";
            }else {
             return "relatorios/princ_servico_A6.jasper";
            
            }
    
    
    }
    
    
    
    public static void main(String[] args) {
        new ListagemServicosA6(false);
    }
 
}
