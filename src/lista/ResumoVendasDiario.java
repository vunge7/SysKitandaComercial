package lista;

import java.io.File;
import java.sql.Connection;
import java.util.Date;
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
public class ResumoVendasDiario {

    private final Date data_inicio;
    private final Date data_fim;

    public ResumoVendasDiario(Date data_inicio, Date data_fim) {
        this.data_inicio = data_inicio;
        this.data_fim = data_fim;
        mostrar();
    }

    public void mostrar() {

        HashMap<String, Object> parametros = new HashMap<String, Object>();
        parametros.put("PARM_DATA_1", this.data_inicio);
        parametros.put("PARM_DATA_2", this.data_fim);

        String relatorio = "relatorios/relatorio_vendas_diarias.jasper";
        String caminho = new File(relatorio).getAbsolutePath();

        Connection connection = null;
        try {
            connection = BDConexao.getConnection();

            // Preenche o relatório uma única vez
            JasperPrint jasperPrint = JasperFillManager.fillReport(caminho, parametros, connection);

            if (jasperPrint.getPages().size() >= 1) {
                JasperViewer jasperViewer = new JasperViewer(jasperPrint, false);
                jasperViewer.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(null,
                        "Não existe registo para esse intervalo de datas!...",
                        "ERROR", JOptionPane.ERROR_MESSAGE);
            }

        } catch (JRException jex) {
            jex.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Falha ao mostrar o relatório",
                    "DVML-COMERCIAL, LDA", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Erro ao tentar mostrar o relatório",
                    "DVML-COMERCIAL, LDA", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}