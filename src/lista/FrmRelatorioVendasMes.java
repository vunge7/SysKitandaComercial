package lista;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.*;
import java.awt.event.*;
import lista.RelatorioVendasMes;
import util.BDConexao;

public class FrmRelatorioVendasMes extends JFrame {

    private JComboBox<String> comboMeses = new JComboBox<>();
    private JButton btnGerar = new JButton("Gerar Relatório");
    private JButton btnSair = new JButton("SAIR");
    private BDConexao conexao = BDConexao.getInstancia();

    public FrmRelatorioVendasMes() {
        setTitle("Relatório de Vendas por Mês");
        setSize(610, 400);
        setLayout(null);

        comboMeses.setBounds(50, 30, 150, 25);
        btnGerar.setBounds(220, 30, 150, 25);
        btnSair.setBounds(390, 30, 150, 25);

        add(comboMeses);
        add(btnGerar);
        add(btnSair);

        // Carrega os meses existentes na BD
        carregarMeses();

        // Evento do botão
        btnGerar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gerarRelatorio();
            }
        });
        
        btnSair.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void carregarMeses() {
        comboMeses.removeAllItems();

        String sql = "SELECT DISTINCT TRIM(SUBSTRING_INDEX(designacao_item, '#', -1)) AS mes " +
                     "FROM tb_item_venda WHERE designacao_item LIKE '%#%' " +
                     "ORDER BY FIELD(TRIM(SUBSTRING_INDEX(designacao_item, '#', -1)), " +
                     "'Janeiro','Fevereiro','Março','Abril','Maio','Junho','Julho','Agosto'," +
                     "'Setembro','Outubro','Novembro','Dezembro')";

        try (Connection conn = conexao.getConnectionAtiva();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while(rs.next()) {
                comboMeses.addItem(rs.getString("mes"));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao carregar meses da base de dados!");
        }
    }

    private void gerarRelatorio() {
        String mes = (String) comboMeses.getSelectedItem();
        if (mes == null || mes.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecione um mês válido!");
            return;
        }

        // Verifica se existe registro para o mês selecionado
        String sqlVerifica = "SELECT COUNT(*) AS total FROM tb_item_venda " +
                             "WHERE TRIM(SUBSTRING_INDEX(designacao_item, '#', -1)) = ?";

        try (Connection conn = conexao.getConnectionAtiva();
             PreparedStatement ps = conn.prepareStatement(sqlVerifica)) {

            ps.setString(1, mes);
            ResultSet rs = ps.executeQuery();
            if (rs.next() && rs.getInt("total") > 0) {
                // Existe registro → chama o relatório
                new RelatorioVendasMes(mes);
            } else {
                // Não existe registro
                JOptionPane.showMessageDialog(this,
                        "O mês selecionado (" + mes + ") não possui registros!");
            }

            rs.close();

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao verificar registros do mês!");
        }
    }

    public static void main(String[] args) {
        new FrmRelatorioVendasMes();
    }
}
