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
public class ListagemTodosProdutosDesactivos {

    private BDConexao conexao = new BDConexao();
    private int cod_armazem = 0, cod_fornecedor;
    private String armazem = "", fornecedor = "";
    private boolean a4 = true, com_iva = false;

    public ListagemTodosProdutosDesactivos(int cod_armazem, String armazem, boolean a4) {
        this.cod_armazem = cod_armazem;
        this.armazem = armazem;
        this.a4 = a4;
        mostrarProdutos();
    }

    public ListagemTodosProdutosDesactivos(int cod_armazem, String armazem, boolean a4, boolean com_iva) {
        this.cod_armazem = cod_armazem;
        this.armazem = armazem;
        this.a4 = a4;
        this.com_iva = com_iva;
        mostrarProdutos();
    }

    public ListagemTodosProdutosDesactivos(int cod_armazem, int cod_fornecedor, String armazem, String fornecedor) {
        this.cod_armazem = cod_armazem;
        this.cod_fornecedor = cod_fornecedor;
        this.armazem = armazem;
        this.fornecedor = fornecedor;
        mostrarProdutosFornecedor();
    }

    public void mostrarProdutos() {
        Connection connection = (Connection) conexao.conectar();
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

    public void mostrarProdutosFornecedor() {

        Connection connection = (Connection) conexao.conectar();
        HashMap hashMap = new HashMap();
        String relatorio = "";

        hashMap.put("ID_ARMAZEM", this.cod_armazem);
        hashMap.put("ID_FORNECEDOR", this.cod_fornecedor);
        hashMap.put("ARMAZEM", this.armazem);
        hashMap.put("FORNECEDOR", this.fornecedor);
        relatorio = "relatorios/princ_produto_desactivo.jasper";

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
            JOptionPane.showMessageDialog(null, "ERRO AO LISTAR PRODUTOS!...");
        }
    }

    private String getCaminho() {

        if (a4) {
            return (com_iva) ? "relatorios/princ_produto_impostos_desactivos.jasper" : "relatorios/princ_produto_desactivo.jasper";
        } else {
            return "relatorios/princ_produto_A6_desactivos.jasper";

        }

    }

//    private String getCaminho() {
//
//        if (a4) {
//            return "relatorios/princ_produto.jasper";
//        } else {
//            return "relatorios/princ_produto_A6.jasper";
//
//        }
//
//    }
    public static void main(String[] args) {
        new ListagemTodosProdutosDesactivos(1, "ARMAZEM_1", false);
    }

}
