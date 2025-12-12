package lista;

import java.io.File;
import java.sql.Connection;
import java.util.HashMap;
import javax.swing.JOptionPane;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import util.BDConexao;

public class RelatorioVendasMes {

    private BDConexao conexao = BDConexao.getInstancia();
    private String mes;

    public RelatorioVendasMes(String mes) {
        this.mes = mes;
        mostrarRelatorio();
    }

    public void mostrarRelatorio() {
        Connection connection = conexao.getConnectionAtiva();

        HashMap parametros = new HashMap();
        parametros.put("MES_PARAM", mes);   // <<--- PARÂMETRO DO TEU RELATÓRIO

        String relatorio = "relatorios/relatorio_vendas_mes_final.jasper";

        File file = new File(relatorio).getAbsoluteFile();
        String caminho = file.getAbsolutePath();

        try {

            JasperPrint jasperPrint = JasperFillManager.fillReport(caminho, parametros, connection);

            if (jasperPrint.getPages().size() > 0) {
                JasperViewer viewer = new JasperViewer(jasperPrint, false);
                viewer.setTitle("Relatório de Vendas do Mês");
                viewer.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(null, "Nenhuma venda encontrada para o mês informado!");
            }

        } catch (JRException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao gerar o relatório de vendas!");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Falha inesperada ao gerar o relatório!");
        }
    }

    // TESTE RÁPIDO
    public static void main(String[] args) {
        new RelatorioVendasMes("Março"); // Já testando
    }
}
