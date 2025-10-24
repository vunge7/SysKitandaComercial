package visao;


import java.sql.Connection;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.*;
import javax.swing.*;
import util.BDConexao;

public class FrmAplicarIvaPrecos extends JFrame {

    private static BDConexao conexao;

    private JTextField txtTaxa;
    private JProgressBar progressBar;
    private JTextArea txtLog;
    private JLabel lblStatus;
    private JButton btnAplicar, btnSair;

    public FrmAplicarIvaPrecos(BDConexao conexao) {
        FrmAplicarIvaPrecos.conexao = conexao;
        initComponents();
    }

    private void initComponents() {
        setTitle("Aplicar IVA mantendo preço final");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(550, 460);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel painelTop = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelTop.add(new JLabel("Taxa IVA (%):"));
        txtTaxa = new JTextField("14", 5);
        painelTop.add(txtTaxa);

        btnAplicar = new JButton("Aplicar IVA (mantendo preço final)");
        painelTop.add(btnAplicar);
        
                // Botão Sair
        btnSair = new JButton("Sair");
        painelTop.add(btnSair);
        btnSair.addActionListener(e -> dispose());
        
        add(painelTop, BorderLayout.NORTH);

        txtLog = new JTextArea();
        txtLog.setEditable(false);
        txtLog.setFont(new Font("Monospaced", Font.PLAIN, 13));
        JScrollPane scroll = new JScrollPane(txtLog);
        add(scroll, BorderLayout.CENTER);

        JPanel painelBottom = new JPanel(new BorderLayout());
        progressBar = new JProgressBar();
        progressBar.setStringPainted(true);
        painelBottom.add(progressBar, BorderLayout.CENTER);

        lblStatus = new JLabel("Aguardando...");
        lblStatus.setHorizontalAlignment(SwingConstants.CENTER);
        painelBottom.add(lblStatus, BorderLayout.SOUTH);

        add(painelBottom, BorderLayout.SOUTH);

        btnAplicar.addActionListener(this::aplicarIvaAction);
    }

    private void aplicarIvaAction(ActionEvent evt) {
        btnAplicar.setEnabled(false);
        txtLog.setText("");
        lblStatus.setText("Processando...");

        new Thread(() -> {
            try {
                BigDecimal taxa = new BigDecimal(txtTaxa.getText().replace(",", "."));
                aplicarIvaNosPrecos(taxa);
                txtLog.append("\n✅ Processo concluído com sucesso!");
                lblStatus.setText("Concluído!");
            } catch (Exception e) {
                txtLog.append("\n❌ Erro: " + e.getMessage());
                e.printStackTrace();
                lblStatus.setText("Erro!");
            } finally {
                btnAplicar.setEnabled(true);
                progressBar.setValue(100);
            }
        }).start();
    }

    private void aplicarIvaNosPrecos(BigDecimal taxaIva) {
        String sqlSelect = "SELECT pk_preco, fk_produto, preco_venda FROM tb_preco";
        String sqlUpdatePreco = "UPDATE tb_preco SET preco_anterior = ?, preco_venda = ? WHERE pk_preco = ?";
        String sqlUpdateProduto = "UPDATE tb_produto SET preco_venda = ? WHERE codigo = ?";

        try (Connection conn = conexao.getConnectionAtiva();
             PreparedStatement pstSelect = conn.prepareStatement(sqlSelect, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
             PreparedStatement pstUpdatePreco = conn.prepareStatement(sqlUpdatePreco);
             PreparedStatement pstUpdateProduto = conn.prepareStatement(sqlUpdateProduto)) {

            conn.setAutoCommit(false);
            ResultSet rs = pstSelect.executeQuery();
            rs.last();
            int total = rs.getRow();
            rs.beforeFirst();

            int count = 0;

            while (rs.next()) {
                count++;
                progressBar.setValue((count * 100) / total);
                lblStatus.setText("Processando " + count + " de " + total);

                int idPreco = rs.getInt("pk_preco");
                int idProduto = rs.getInt("fk_produto");
                BigDecimal precoAtual = rs.getBigDecimal("preco_venda");

                if (precoAtual == null || precoAtual.compareTo(BigDecimal.ZERO) <= 0)
                    continue;

                // ✅ Cálculo com 6 casas decimais
                BigDecimal divisor = BigDecimal.ONE.add(
                        taxaIva.divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP)
                );

                BigDecimal precoSemIva = precoAtual
                        .divide(divisor, 6, RoundingMode.HALF_UP)
                        .setScale(6, RoundingMode.HALF_UP);

                // Atualizar tb_preco
                pstUpdatePreco.setBigDecimal(1, precoAtual.setScale(6, RoundingMode.HALF_UP));
                pstUpdatePreco.setBigDecimal(2, precoSemIva);
                pstUpdatePreco.setInt(3, idPreco);
                pstUpdatePreco.addBatch();

                // Atualizar tb_produto
                pstUpdateProduto.setBigDecimal(1, precoSemIva);
                pstUpdateProduto.setInt(2, idProduto);
                pstUpdateProduto.addBatch();

                txtLog.append(String.format("Produto ID %d | De: %.6f → Novo: %.6f%n",
                        idProduto, precoAtual, precoSemIva));
            }

            pstUpdatePreco.executeBatch();
            pstUpdateProduto.executeBatch();
            conn.commit();

            txtLog.append("\n\n💾 Alterações gravadas com sucesso no banco de dados!");

        } catch (SQLException e) {
            txtLog.append("\n⚠️ Erro SQL: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        conexao = BDConexao.getInstancia();
        SwingUtilities.invokeLater(() -> new FrmAplicarIvaPrecos(conexao).setVisible(true));
    }
}
