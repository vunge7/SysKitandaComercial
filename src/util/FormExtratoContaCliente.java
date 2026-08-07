/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

/**
 *
 * @author DELL
 */


import comercial.controller.ExtratoContaClienteController;
import util.BDConexao;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

public class FormExtratoContaCliente extends JFrame {

    private JSpinner spnDataInicio;
    private JSpinner spnDataFim;
    private JButton btnProcessar;
    private JButton btnSair;

    private BDConexao conexao;

    public FormExtratoContaCliente(BDConexao conexao) {
        this.conexao = conexao;
        initComponents();
    }

    private void initComponents() {
        setTitle("Gerar Extrato de Conta de Cliente");
        setSize(420, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Painel Principal
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Label e Spinner Data Início
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(new JLabel("Data Início:"), gbc);

        gbc.gridx = 1;
        spnDataInicio = criarSpinnerData();
        mainPanel.add(spnDataInicio, gbc);

        // Label e Spinner Data Fim
        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(new JLabel("Data Fim:"), gbc);

        gbc.gridx = 1;
        spnDataFim = criarSpinnerData();
        mainPanel.add(spnDataFim, gbc);

        // Painel de Botões
        JPanel panelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnProcessar = new JButton("Processar");
        btnSair = new JButton("Sair");

        panelBotoes.add(btnProcessar);
        panelBotoes.add(btnSair);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(panelBotoes, gbc);

        add(mainPanel);

        configurarEventos();
    }

    private JSpinner criarSpinnerData() {
        SpinnerDateModel model = new SpinnerDateModel();
        JSpinner spinner = new JSpinner(model);
        JSpinner.DateEditor editor = new JSpinner.DateEditor(spinner, "dd/MM/yyyy");
        spinner.setEditor(editor);
        return spinner;
    }

    private void configurarEventos() {
        // Ação do botão Processar
        btnProcessar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                processarExtrato();
            }
        });

        // Ação do botão Sair
        btnSair.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

    private void processarExtrato() {
        Date dataInicio = (Date) spnDataInicio.getValue();
        Date dataFim = (Date) spnDataFim.getValue();

        // Validação de intervalo de datas
        if (dataFim.before(dataInicio)) {
            JOptionPane.showMessageDialog(
                    this,
                    "A 'Data Fim' não pode ser anterior à 'Data Início'.",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        // Execução do processo em background para não travar a interface
        btnProcessar.setEnabled(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                // Instancia o controller utilizando a conexão
                ExtratoContaClienteController controller = new ExtratoContaClienteController(conexao);
                // Executa a rotina estática com as datas selecionadas
                return ExtratoContaClienteController.gerarExtratoAutomatico(dataInicio, dataFim);
            }

            @Override
            protected void done() {
                btnProcessar.setEnabled(true);
                setCursor(Cursor.getDefaultCursor());
            }
        };

        worker.execute();
    }

    public static void main(String[] args) {
        // Aplica o Look & Feel do sistema operacional
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        // Inicializa a interface e a conexão no main
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                BDConexao conexaoLocal = new BDConexao();
                new FormExtratoContaCliente(conexaoLocal).setVisible(true);
            }
        });
    }
}