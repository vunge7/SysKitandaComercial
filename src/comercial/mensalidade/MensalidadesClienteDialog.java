package comercial.mensalidade;

import comercial.controller.ClientesController;
import comercial.controller.ConfiguracaoMesComecoController;
import comercial.controller.ProdutosController;
import entity.ConfiguracaoMesComeco;
import entity.TbCliente;
import entity.TbProduto;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import kitanda.util.CfMethods;
import util.BDConexao;
import util.ui.ComponentesWeb;
import util.ui.EstiloUI;
import util.ui.FormularioWeb;

/**
 * Atalho da venda (F8): todas as mensalidades do cliente numa só janela.
 * Cada serviço já vem com os meses vencidos (atrasos + mês corrente)
 * escolhidos; muda-se só o nº de meses se quiser adiantar ou pagar menos, e
 * Enter põe tudo na venda.
 */
public class MensalidadesClienteDialog extends JDialog {

    private static final int C_INCLUIR = 0, C_SERVICO = 1, C_DIVIDA = 2, C_MESES = 3, C_PERIODO = 4, C_VALOR = 5;

    private final BDConexao conexao;
    private final List<Linha> linhas = new ArrayList<>();
    private final com.toedter.calendar.JDateChooser dcData = new com.toedter.calendar.JDateChooser();
    private final JLabel lbTotal = new JLabel("0,00", SwingConstants.RIGHT);
    private final JButton btAdicionar = new JButton("Adicionar à venda");
    private final javax.swing.JCheckBox chkPerdoar = new javax.swing.JCheckBox("Perdoar as multas");
    private boolean aActualizar;

    private final DefaultTableModel modelo = new DefaultTableModel(
            new Object[]{"Incluir", "Serviço", "Por pagar", "Nº meses", "Período", "Valor"}, 0) {
        @Override
        public Class<?> getColumnClass(int c) {
            return c == C_INCLUIR ? Boolean.class : c == C_MESES ? Integer.class : Object.class;
        }

        @Override
        public boolean isCellEditable(int r, int c) {
            return c == C_INCLUIR || c == C_MESES;
        }
    };

    private static final class Linha {

        int produtoId;
        ConfiguracaoMesComeco contrato;
        String servico;
        BigDecimal preco;
        List<Periodo> porPagar;
        int vencidos;
    }

    /** Abre a janela; se o cliente não tiver mensalidades, avisa e não abre. */
    public static void abrir(Window parent, int clienteId, BDConexao conexao) {
        if (clienteId <= 0) {
            JOptionPane.showMessageDialog(parent, "Escolha primeiro o cliente da venda.");
            return;
        }
        if (!InscricoesVisao.podeCobrar(parent, clienteId, conexao)) {
            return;
        }
        MensalidadesClienteDialog d = new MensalidadesClienteDialog(parent, clienteId, conexao);
        if (d.linhas.isEmpty()) {
            d.dispose();
            JOptionPane.showMessageDialog(parent,
                    "Este cliente não tem mensalidades por pagar.\n(Os contratos configuram-se em Clientes → Mensalidades.)");
            return;
        }
        d.setVisible(true);
    }

    private MensalidadesClienteDialog(Window parent, int clienteId, BDConexao conexao) {
        super(parent, ModalityType.APPLICATION_MODAL);
        this.conexao = conexao;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setTitle("Mensalidades do cliente");
        carregar(clienteId);
        if (!linhas.isEmpty()) {
            montar(clienteId);
        }
    }

    private void carregar(int clienteId) {
        ProdutosController produtos = new ProdutosController(conexao);
        List<ConfiguracaoMesComeco> contratos = new ConfiguracaoMesComecoController(conexao.getConnectionAtiva())
                .listarTodos(clienteId);
        for (ConfiguracaoMesComeco c : contratos) {
            try {
                List<Periodo> porPagar = Mensalidades.porPagar(
                        Mensalidades.meses(conexao.getConnectionAtiva(), c), Mensalidades.noCarrinho(c.getProdutoId()));
                if (porPagar.isEmpty()) {
                    continue;
                }
                Linha l = new Linha();
                l.produtoId = c.getProdutoId();
                l.contrato = c;
                TbProduto p = (TbProduto) produtos.findById(c.getProdutoId());
                l.servico = p != null ? p.getDesignacao() : "Serviço " + c.getProdutoId();
                l.preco = Mensalidades.precoMensal(conexao, c.getProdutoId());
                l.porPagar = porPagar;
                l.vencidos = Mensalidades.vencidos(porPagar);
                linhas.add(l);
            } catch (Exception e) {
                System.err.println("Mensalidades: " + e.getMessage());
            }
        }
    }

    private void montar(int clienteId) {
        String nome = "";
        try {
            TbCliente c = new ClientesController(conexao).findByCodigo(clienteId);
            nome = c != null ? c.getNome() : "";
        } catch (Exception ignorar) {
        }
        JPanel cabecalho = FormularioWeb.cabecalho("Mensalidades do cliente", nome, null);

        for (Linha l : linhas) {
            boolean temDivida = l.vencidos > 0;
            modelo.addRow(new Object[]{temDivida, l.servico,
                temDivida ? l.vencidos + " vencido(s)" : "em dia",
                Math.max(1, l.vencidos), "", ""});
        }
        modelo.addTableModelListener(e -> {
            if (!aActualizar) {
                actualizar();
            }
        });

        JTable tabela = new JTable(modelo);
        EstiloUI.estilizarTabela(tabela, true);
        tabela.setRowHeight(32);
        tabela.getColumnModel().getColumn(C_INCLUIR).setMaxWidth(70);
        tabela.getColumnModel().getColumn(C_SERVICO).setPreferredWidth(220);
        tabela.getColumnModel().getColumn(C_DIVIDA).setPreferredWidth(110);
        tabela.getColumnModel().getColumn(C_MESES).setMaxWidth(90);
        tabela.getColumnModel().getColumn(C_PERIODO).setPreferredWidth(190);
        tabela.getColumnModel().getColumn(C_VALOR).setPreferredWidth(110);
        tabela.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
        JScrollPane rol = new JScrollPane(tabela);
        rol.setBorder(javax.swing.BorderFactory.createLineBorder(ComponentesWeb.LINHA));
        rol.setPreferredSize(new Dimension(600, 220));

        dcData.setDate(new Date());
        FormularioWeb.data(dcData);
        dcData.addPropertyChangeListener("date", e -> actualizar());

        lbTotal.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(22f));
        JPanel caixaTotal = new ComponentesWeb.CaixaSuave(new BorderLayout(12, 0), EstiloUI.AZUL_CLARO, null);
        JLabel lbT = new JLabel("Total a adicionar");
        ComponentesWeb.rotulo(lbT);
        caixaTotal.add(lbT, BorderLayout.WEST);
        caixaTotal.add(lbTotal, BorderLayout.CENTER);

        chkPerdoar.setOpaque(false);
        chkPerdoar.setFont(EstiloUI.FONTE);
        chkPerdoar.setToolTipText("Não cobra multas (pede um administrador e o motivo; fica registado)");
        chkPerdoar.addActionListener(e -> actualizar());
        JPanel topo = FormularioWeb.grelha(220, 2,
                FormularioWeb.pilha(6, ComponentesWeb.campo("Data do pagamento (conta para a multa)", dcData), chkPerdoar),
                caixaTotal);
        JPanel corpo = ComponentesWeb.transparente(new BorderLayout(0, 10));
        corpo.add(topo, BorderLayout.NORTH);
        corpo.add(rol, BorderLayout.CENTER);
        corpo.add(FormularioWeb.dica("Já vêm escolhidos os meses vencidos. Mude o \"Nº meses\" para adiantar ou pagar menos; "
                + "os meses vão sempre por ordem, do mais antigo."), BorderLayout.SOUTH);
        ComponentesWeb.Cartao cartao = FormularioWeb.cartao("Serviços com meses por pagar", ComponentesWeb.VERDE, corpo);

        JButton btCancelar = new JButton("Cancelar");
        FormularioWeb.botaoSair(btCancelar, "Fechar sem adicionar (Esc)");
        ComponentesWeb.botao(btAdicionar, ComponentesWeb.Variante.SUCESSO, "/imagens/salvar_16x16.png", "Adiciona à venda (Enter)");
        btCancelar.addActionListener(e -> dispose());
        btAdicionar.addActionListener(e -> enviar(tabela));
        JPanel accoes = FormularioWeb.barraAccoes(null, btCancelar, btAdicionar);

        FormularioWeb.montar(this, cabecalho, cartao, accoes, "Mensalidades do cliente");
        JComponent raiz = getRootPane();
        raiz.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "sair");
        raiz.getActionMap().put("sair", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                dispose();
            }
        });
        // Enter na tabela também adiciona (em vez de descer de linha)
        AbstractAction enviar = new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                enviar(tabela);
            }
        };
        raiz.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "enviar");
        raiz.getActionMap().put("enviar", enviar);
        tabela.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "enviar");
        tabela.getActionMap().put("enviar", enviar);

        actualizar();
        FormularioWeb.ajustar(this, 900, 560, 620, 420);
    }

    private List<Periodo> escolhidos(int linha) {
        Linha l = linhas.get(linha);
        Object inc = modelo.getValueAt(linha, C_INCLUIR);
        if (!Boolean.TRUE.equals(inc)) {
            return new ArrayList<>();
        }
        int n;
        try {
            n = Integer.parseInt(String.valueOf(modelo.getValueAt(linha, C_MESES)));
        } catch (NumberFormatException e) {
            n = 1;
        }
        n = Math.max(1, Math.min(n, l.porPagar.size()));
        return new ArrayList<>(l.porPagar.subList(0, n));
    }

    private void actualizar() {
        aActualizar = true;
        try {
            BigDecimal total = BigDecimal.ZERO;
            for (int r = 0; r < linhas.size(); r++) {
                Linha l = linhas.get(r);
                // nº de meses limitado ao que há por pagar
                Object v = modelo.getValueAt(r, C_MESES);
                int n = v instanceof Integer ? (Integer) v : 1;
                int lim = Math.max(1, Math.min(n, l.porPagar.size()));
                if (lim != n) {
                    modelo.setValueAt(lim, r, C_MESES);
                }
                List<Periodo> ps = escolhidos(r);
                BigDecimal valor = BigDecimal.ZERO;
                for (Periodo p : ps) {
                    valor = valor.add(l.preco).add((chkPerdoar.isSelected() ? BigDecimal.ZERO : Mensalidades.multa(l.contrato, l.preco, p, dcData.getDate())));
                }
                String periodo = ps.isEmpty() ? "—"
                        : ps.get(0).curto() + (ps.size() > 1 ? "  →  " + ps.get(ps.size() - 1).curto() : "");
                modelo.setValueAt(periodo, r, C_PERIODO);
                modelo.setValueAt(ps.isEmpty() ? "—" : CfMethods.formatarComoMoeda(valor.doubleValue()), r, C_VALOR);
                total = total.add(valor);
            }
            lbTotal.setText(CfMethods.formatarComoMoeda(total.doubleValue()));
            btAdicionar.setEnabled(total.signum() > 0 || algumEscolhido());
        } finally {
            aActualizar = false;
        }
    }

    private boolean algumEscolhido() {
        for (int r = 0; r < linhas.size(); r++) {
            if (!escolhidos(r).isEmpty()) {
                return true;
            }
        }
        return false;
    }

    private void enviar(JTable tabela) {
        if (tabela.isEditing()) {
            tabela.getCellEditor().stopCellEditing();
        }
        if (!algumEscolhido()) {
            return;
        }
        String motivo = null;
        if (chkPerdoar.isSelected()) {
            motivo = MesesMensalidadeDialog.autorizarReducao(this);
            if (motivo == null) {
                return;
            }
        }
        for (int r = 0; r < linhas.size(); r++) {
            List<Periodo> ps = escolhidos(r);
            if (!ps.isEmpty()) {
                java.util.Map<Periodo, BigDecimal> cobrado = null;
                if (chkPerdoar.isSelected()) {
                    cobrado = new java.util.HashMap<>();
                    for (Periodo p : ps) {
                        cobrado.put(p, BigDecimal.ZERO);
                    }
                }
                Mensalidades.enviarParaVenda(conexao, linhas.get(r).produtoId, ps, dcData.getDate(), cobrado, motivo);
            }
        }
        dispose();
    }
}
