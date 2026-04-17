package visao;

import com.toedter.calendar.JDateChooser;
import comercial.controller.ProdutosController;
import entity.TbProduto;
import util.SaftInventarioService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class FrmSaftInventario extends JFrame {

    private JDateChooser dtInicio;
    private JDateChooser dtFim;
    private JButton btnGerar;
    private JButton btnSair;
    private JProgressBar progressBar;

    public FrmSaftInventario() {
        setTitle("SAFT Inventário");
        setSize(460, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initComponents();
    }

    private void initComponents() {

        JPanel container = new JPanel(new BorderLayout());
        container.setBorder(new EmptyBorder(15, 20, 15, 20));
        add(container);

        // 🔹 Título
        JLabel titulo = new JLabel("Gerar SAFT Inventário");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        container.add(titulo, BorderLayout.NORTH);

        // 🔹 Painel central
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(20, 10, 20, 10));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Data Início
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Data Início:"), gbc);

        gbc.gridx = 1;
        dtInicio = new JDateChooser();
        configurarCalendario(dtInicio);
        panel.add(dtInicio, gbc);

        // Data Fim
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Data Fim:"), gbc);

        gbc.gridx = 1;
        dtFim = new JDateChooser();
        configurarCalendario(dtFim);
        panel.add(dtFim, gbc);

        // 🔹 ProgressBar
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;

        progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setVisible(false);

        panel.add(progressBar, gbc);

        container.add(panel, BorderLayout.CENTER);

        // 🔹 Botões
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));

        btnGerar = new JButton("Gerar");
        btnGerar.setIcon(new ImageIcon(getClass().getResource("/icons/save.png"))); // coloca ícone no projeto
        btnGerar.setPreferredSize(new Dimension(110, 35));

        btnSair = new JButton("Sair");
        btnSair.setIcon(new ImageIcon(getClass().getResource("/icons/exit.png")));
        btnSair.setPreferredSize(new Dimension(110, 35));

        painelBotoes.add(btnGerar);
        painelBotoes.add(btnSair);

        container.add(painelBotoes, BorderLayout.SOUTH);

        // 🔥 Eventos
        btnGerar.addActionListener(e -> gerarSAFTAsync());
        btnSair.addActionListener(e -> dispose());
    }

    private void configurarCalendario(JDateChooser dateChooser) {

        dateChooser.setDateFormatString("yyyy-MM-dd");
        dateChooser.setPreferredSize(new Dimension(150, 28));

        JTextField txt = (JTextField) dateChooser.getDateEditor().getUiComponent();
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 13));
    }

    // 🔥 PROCESSAMENTO EM THREAD
    private void gerarSAFTAsync() {

        btnGerar.setEnabled(false);
        progressBar.setVisible(true);

        new Thread(() -> {

            try {

                Date inicio = dtInicio.getDate();
                Date fim = dtFim.getDate();

                if (inicio == null || fim == null) {
                    showMsg("Informe as datas!");
                    return;
                }

                LocalDate dataInicio = inicio.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                LocalDate dataFim = fim.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                if (dataFim.isBefore(dataInicio)) {
                    showMsg("Data fim não pode ser menor que data início!");
                    return;
                }

                List<TbProduto> produtos =
                        ProdutosController.buscarProdutosComStockSaftFullEmpty(dataInicio, dataFim);

                if (produtos == null) {
                    produtos = new java.util.ArrayList<>();
                }

                if (produtos.isEmpty()) {
                    showMsg("Sem movimentos. Será gerado SAFT vazio.");
                }

                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setSelectedFile(new java.io.File("SAFT_INVENTARIO.xml"));

                if (fileChooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
                    return;
                }

                String caminho = fileChooser.getSelectedFile().getAbsolutePath();

                SaftInventarioService.gerarSAFTInventario(
                        produtos, caminho, dataInicio, dataFim
                );

                showMsg("SAFT gerado com sucesso!");

            } catch (Exception ex) {
                ex.printStackTrace();
                showMsg("Erro ao gerar SAFT!");
            } finally {

                SwingUtilities.invokeLater(() -> {
                    btnGerar.setEnabled(true);
                    progressBar.setVisible(false);
                });
            }

        }).start();
    }

    private void showMsg(String msg) {
        SwingUtilities.invokeLater(() ->
                JOptionPane.showMessageDialog(this, msg));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
                new FrmSaftInventario().setVisible(true));
    }
}