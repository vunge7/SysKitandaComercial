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
public class ListagemTodosServicos {
    
    
    
    private BDConexao conexao  = BDConexao.getInstancia();
    private int cod_armazem = 0, cod_fornecedor;
    private String armazem = "", fornecedor = "";
    private boolean a4 =  true;
    public ListagemTodosServicos(int cod_armazem, String armazem, boolean a4)
    {
        this.cod_armazem = cod_armazem;
        this.armazem = armazem;
        this.a4 = a4;
        mostrarProdutos();
    }

      public void mostrarProdutos() {
        java.sql.Connection connection = conexao.getConnectionAtiva();
        HashMap hashMap = new HashMap();
        String relatorio = "";
        
        
//        relatorio = "relatorios/ListarTodosProdutos.jasper";
        //relatorio = "relatorios/ListaDeProdutos.jasper";
        hashMap.put("ID_ARMAZEM", this.cod_armazem);
        hashMap.put("ARMAZEM", this.armazem);
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
                JOptionPane.showMessageDialog(null, "Nao Existem produtos Cadastrados!...");
            }
        } catch (JRException jex) {
            jex.printStackTrace();
            //System.out.println("aqui");
            JOptionPane.showMessageDialog(null, "FALHA AO TENTAR MOSTRAR OS PRODUTOS!...");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "ERRO AO TENTAR OS PRODUTOS!...");
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
        new ListagemTodosServicos(1, "ARMAZEM_1", false);
    }
 
}
