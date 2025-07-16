/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lista;

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
public class Rel_princ_stock {

    private String des_armazem = "", des_fornecedor = "";
    private int cod_fornecedor = 0;
    private boolean com_iva = false;

    public Rel_princ_stock(String des_armazem) {

        this.des_armazem = des_armazem;
        try {
            mostrar();
        } catch (Exception e) {
        }

    }

    public Rel_princ_stock(String des_armazem, boolean com_iva) {

        this.des_armazem = des_armazem;
        this.com_iva = com_iva;
        try {
            mostrar();
        } catch (Exception e) {
        }

    }

    public Rel_princ_stock(String des_armazem, String des_fornecedor, int cod_fornecedor) {

        this.des_armazem = des_armazem;
        this.des_fornecedor = des_fornecedor;
        this.cod_fornecedor = cod_fornecedor;
        try {
            mostrarFornecedor();
        } catch (Exception e) {
        }

    }

    public void mostrar() throws SQLException {

        Connection connection = BDConexao.getConnection();
        HashMap hashMap = new HashMap();
        String relatorio = "";

        hashMap.put("DES_ARMAZEM", this.des_armazem);
        hashMap.put("SUBREPORT_DIR", "relatorios/");
        relatorio = getCaminho();

        //obter o path do relatorio
//        String relatorio = (com_iva) ? "relatorios/princ_stock_imposto.jasper" : "relatorios/princ_stock.jasper";

        File file = new File(relatorio).getAbsoluteFile();
        String obterCaminho = file.getAbsolutePath();

        try {

            JasperFillManager.fillReport(obterCaminho, hashMap, connection);
            JasperPrint jasperPrint = JasperFillManager.fillReport(obterCaminho, hashMap, connection);

            if (jasperPrint.getPages().size() >= 1) {
                JasperViewer jasperViewer = new JasperViewer(jasperPrint, false);
                jasperViewer.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(null, "Não existe registro para esse intervalo de data!...", "ERROR", JOptionPane.ERROR_MESSAGE);
            }

        } catch (JRException jex) {
            jex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Falha ao mostrar o relatorio", "DVML-COMERCIAL, LDA", JOptionPane.ERROR);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao tentar mostrar o relatorio", "DVML-COMERCIAL, LDA", JOptionPane.ERROR_MESSAGE);
        }

    }

    private String getCaminho() {

        return (com_iva) ? "relatorios/princ_stock_imposto.jasper" : "relatorios/princ_stock.jasper";

    }

    public void mostrarFornecedor() throws SQLException {

        Connection connection = BDConexao.getConnection();
        HashMap hashMap = new HashMap();

        hashMap.put("DES_ARMAZEM", this.des_armazem);
        hashMap.put("DES_FORNECEDOR", this.des_fornecedor);
        hashMap.put("COD_FORNECEDOR", this.cod_fornecedor);
        hashMap.put("SUBREPORT_DIR", "relatorios/");

        //obter o path do relatorio
        String relatorio = "relatorios/princ_stock_fornecedor.jasper";

        File file = new File(relatorio).getAbsoluteFile();
        String obterCaminho = file.getAbsolutePath();

        try {

            JasperFillManager.fillReport(obterCaminho, hashMap, connection);
            JasperPrint jasperPrint = JasperFillManager.fillReport(obterCaminho, hashMap, connection);

            if (jasperPrint.getPages().size() >= 1) {
                JasperViewer jasperViewer = new JasperViewer(jasperPrint, false);
                jasperViewer.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(null, "Não existe registro para esse intervalo de data!...", "ERROR", JOptionPane.ERROR_MESSAGE);
            }

        } catch (JRException jex) {
            jex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Falha ao mostrar o relatorio", "DVML-COMERCIAL, LDA", JOptionPane.ERROR);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao tentar mostrar o relatorio", "DVML-COMERCIAL, LDA", JOptionPane.ERROR_MESSAGE);
        }

    }

    public static void main(String[] args) {

        new Rel_princ_stock("SHOFF");

    }

}
