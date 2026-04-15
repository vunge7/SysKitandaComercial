package visao;

import com.toedter.calendar.JDateChooser;
import comercial.controller.ProdutosController;
import entity.TbProduto;
import util.SaftInventarioService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class FrmSaftInventario extends JFrame {

    private JDateChooser dtInicio;
    private JDateChooser dtFim;
    private JButton btnGerar;
    private JButton btnSair;

    public FrmSaftInventario() {
        setTitle("SAFT Inventário");
        setSize(420, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initComponents();
    }

    private void initComponents() {

        setLayout(new BorderLayout());

        // 🔹 Painel principal (campos)
        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));

        panel.add(new JLabel("Data Início:"));
        dtInicio = new JDateChooser();
        panel.add(dtInicio);

        panel.add(new JLabel("Data Fim:"));
        dtFim = new JDateChooser();
        panel.add(dtFim);

        add(panel, BorderLayout.CENTER);

        // 🔹 Painel de botões
        JPanel painelBotoes = new JPanel();

        btnGerar = new JButton("Gerar SAFT");
        btnSair = new JButton("Sair");

        painelBotoes.add(btnGerar);
        painelBotoes.add(btnSair);

        add(painelBotoes, BorderLayout.SOUTH);

        // 🔥 Eventos
        btnGerar.addActionListener((ActionEvent e) -> gerarSAFT());
        btnSair.addActionListener((ActionEvent e) -> dispose());
    }

    private void gerarSAFT() {

        try {
            Date inicio = dtInicio.getDate();
            Date fim = dtFim.getDate();

            // 🔴 Validação de datas
            if (inicio == null || fim == null) {
                JOptionPane.showMessageDialog(this, "Informe as datas!");
                return;
            }

            LocalDate dataInicio = inicio.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate dataFim = fim.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            if (dataFim.isBefore(dataInicio)) {
                JOptionPane.showMessageDialog(this, "Data fim não pode ser menor que data início!");
                return;
            }

            // 🔹 Buscar produtos
            List<TbProduto> produtos = ProdutosController.buscarProdutosComStockSaft(dataInicio, dataFim);

            System.out.println("TOTAL PRODUTOS CARREGADOS: " + (produtos != null ? produtos.size() : 0));

            if (produtos == null || produtos.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nenhum produto encontrado!");
                return;
            }

            // 🔥 DEBUG
            for (TbProduto p : produtos) {
                System.out.println("Produto: " + p.getDesignacao());
                System.out.println("Stocável: " + p.getStocavel());

                if (p.getTbStockList() == null) {
                    System.out.println("⚠ Sem lista de stock");
                } else {
                    System.out.println("Qtd stocks: " + p.getTbStockList().size());
                }
                System.out.println("-------------------------");
            }

            // 🔹 Escolher ficheiro
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Salvar SAFT");
            fileChooser.setSelectedFile(new java.io.File("SAFT_INVENTARIO.xml"));

            int userSelection = fileChooser.showSaveDialog(this);

            if (userSelection != JFileChooser.APPROVE_OPTION) {
                return;
            }

            String caminho = fileChooser.getSelectedFile().getAbsolutePath();

            // 🔹 Gerar SAFT
            SaftInventarioService.gerarSAFTInventario(
                    produtos,
                    caminho,
                    dataInicio,
                    dataFim
            );

            // 🔴 Verificação final
            java.io.File f = new java.io.File(caminho);

            if (f.exists() && f.length() < 200) {
                JOptionPane.showMessageDialog(this,
                        "SAFT gerado, mas parece vazio!\nVerifique o stock dos produtos.",
                        "Aviso",
                        JOptionPane.WARNING_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "SAFT gerado com sucesso!");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao gerar SAFT!");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new FrmSaftInventario().setVisible(true);
        });
    }
}
