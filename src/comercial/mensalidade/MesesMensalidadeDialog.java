package comercial.mensalidade;

import comercial.controller.ClientesController;
import comercial.controller.ConfiguracaoMesComecoController;
import comercial.controller.ProdutosController;
import entity.ConfiguracaoMesComeco;
import entity.TbCliente;
import entity.TbProduto;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
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
 * Janela da venda para escolher os meses a facturar de uma mensalidade.
 *
 * Mostra todos os meses do contrato em cartões (pago, em atraso, mês
 * corrente, por vencer). Um clique num mês leva também os meses anteriores
 * por pagar — paga-se sempre por ordem — e um segundo clique larga esse mês
 * e os seguintes. Os meses que já estão no carrinho não se repetem.
 */
public class MesesMensalidadeDialog extends JDialog {

    private static final Color COR_PAGO = ComponentesWeb.VERDE;
    private static final Color COR_ATRASO = ComponentesWeb.VERMELHO;
    private static final Color COR_ACTUAL = ComponentesWeb.LARANJA;
    private static final Color COR_FUTURO = ComponentesWeb.AZUL_VIVO;

    private final int clienteId;
    private final int produtoId;
    private final BDConexao conexao;

    private final List<Mensalidades.Mes> meses = new ArrayList<>();
    private final Set<Periodo> noCarrinho = new HashSet<>();
    private final List<Periodo> escolhidos = new ArrayList<>(); // sempre por ordem
    private final List<CartaoMes> cartoes = new ArrayList<>();

    private BigDecimal precoMensal = BigDecimal.ZERO;
    private String nomeServico = "";

    private final com.toedter.calendar.JDateChooser dcDataPagamento = new com.toedter.calendar.JDateChooser();
    private ConfiguracaoMesComeco contrato;
    /** Multas ajustadas à mão (por mês); sem entrada = a calculada. */
    private final java.util.Map<Periodo, BigDecimal> ajustes = new java.util.HashMap<>();
    private final javax.swing.JCheckBox chkPerdoar = new javax.swing.JCheckBox("Perdoar as multas");
    private boolean aPreencherTabela;
    private final DefaultTableModel modeloEscolha = new DefaultTableModel(
            new Object[]{"Mês", "Mensalidade", "Multa calculada", "Multa a cobrar"}, 0) {
        @Override
        public boolean isCellEditable(int r, int c) {
            return c == 3 && !chkPerdoar.isSelected();
        }

        @Override
        public void setValueAt(Object v, int r, int c) {
            super.setValueAt(v, r, c);
            if (c == 3 && !aPreencherTabela && r < escolhidos.size()) {
                BigDecimal valor = lerValor(String.valueOf(v));
                if (valor != null) {
                    ajustes.put(escolhidos.get(r), valor.max(BigDecimal.ZERO));
                }
                javax.swing.SwingUtilities.invokeLater(MesesMensalidadeDialog.this::actualizar);
            }
        }
    };
    private final JLabel lbTotal = new JLabel("0,00", SwingConstants.RIGHT);
    private final JLabel lbResumoEscolha = FormularioWeb.dica("Nenhum mês escolhido");
    private final JButton btAdicionar = new JButton("Adicionar à venda");

    public MesesMensalidadeDialog(Window parent, boolean modal, int clienteId, int produtoId, BDConexao conexao) {
        super(parent, modal ? ModalityType.APPLICATION_MODAL : ModalityType.MODELESS);
        this.clienteId = clienteId;
        this.produtoId = produtoId;
        this.conexao = conexao;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setTitle("Mensalidades");

        contrato = carregar();
        montar(contrato);
        FormularioWeb.ajustar(this, 980, 680, 720, 520);
    }

    /** Cliente desistente/desactivado: não abre (oferece a reactivação). */
    @Override
    public void setVisible(boolean b) {
        if (b && !InscricoesVisao.podeCobrar(getOwner(), clienteId, conexao)) {
            dispose();
            return;
        }
        super.setVisible(b);
    }

    // =====================================================================
    // DADOS
    // =====================================================================
    private ConfiguracaoMesComeco carregar() {
        precoMensal = Mensalidades.precoMensal(conexao, produtoId);
        try {
            TbProduto p = (TbProduto) new ProdutosController(conexao).findById(produtoId);
            nomeServico = p != null ? p.getDesignacao() : "";
        } catch (Exception e) {
            nomeServico = "";
        }

        ConfiguracaoMesComeco contrato = new ConfiguracaoMesComecoController(conexao.getConnectionAtiva())
                .buscarDoCliente(clienteId, produtoId);
        if (contrato != null) {
            try {
                meses.addAll(Mensalidades.meses(conexao.getConnectionAtiva(), contrato));
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Erro ao carregar os meses: " + e.getMessage());
            }
        }
        noCarrinho.addAll(Mensalidades.noCarrinho(produtoId));
        return contrato;
    }

    // =====================================================================
    // ECRÃ
    // =====================================================================
    private void montar(ConfiguracaoMesComeco contrato) {
        String nomeCliente = "";
        try {
            TbCliente c = new ClientesController(conexao).findByCodigo(clienteId);
            nomeCliente = c != null ? c.getNome() : "";
        } catch (Exception ignorar) {
        }

        JPanel cabecalho = FormularioWeb.cabecalho("Mensalidades · " + nomeServico,
                nomeCliente + "   ·   " + CfMethods.formatarComoMoeda(precoMensal.doubleValue()) + " por mês", null);

        // ---- resumo do contrato
        int pagos = 0, atraso = 0;
        for (Mensalidades.Mes m : meses) {
            if (m.pago()) {
                pagos++;
            } else if (m.estado == Mensalidades.Estado.ATRASO) {
                atraso++;
            }
        }
        JPanel resumo = ComponentesWeb.transparente(new FlowLayout(FlowLayout.LEFT, 8, 0));
        if (contrato != null) {
            resumo.add(new ComponentesWeb.Etiqueta(Mensalidades.textoVigencia(contrato), COR_FUTURO));
            resumo.add(new ComponentesWeb.Etiqueta(Mensalidades.textoDuracao(contrato.getDuracao()), ComponentesWeb.CINZA));
        }
        resumo.add(new ComponentesWeb.Etiqueta(pagos + " pago(s)", COR_PAGO));
        if (atraso > 0) {
            resumo.add(new ComponentesWeb.Etiqueta(atraso + " em atraso", COR_ATRASO));
        }

        // ---- grelha de meses, um bloco por ano
        JPanel grelha = ComponentesWeb.transparente(null);
        grelha.setLayout(new javax.swing.BoxLayout(grelha, javax.swing.BoxLayout.Y_AXIS));
        int anoActual = Integer.MIN_VALUE;
        JPanel linhaAno = null;
        for (Mensalidades.Mes m : meses) {
            if (m.periodo.getAno() != anoActual) {
                anoActual = m.periodo.getAno();
                JLabel lbAno = new JLabel(String.valueOf(anoActual));
                lbAno.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(13f));
                lbAno.setForeground(ComponentesWeb.TEXTO_SUAVE);
                lbAno.setBorder(BorderFactory.createEmptyBorder(6, 2, 4, 0));
                lbAno.setAlignmentX(0f);
                grelha.add(lbAno);
                linhaAno = ComponentesWeb.transparente(new GridLayout(0, 6, 8, 8));
                linhaAno.setAlignmentX(0f);
                grelha.add(linhaAno);
            }
            CartaoMes cm = new CartaoMes(m);
            cartoes.add(cm);
            linhaAno.add(cm);
        }
        if (meses.isEmpty()) {
            grelha.add(FormularioWeb.dica("Este cliente não tem contrato configurado para este serviço (Ficha do cliente → Mensalidades)."));
        }

        JButton btAtraso = new JButton("Meses em atraso");
        JButton btAteHoje = new JButton("Até ao mês corrente");
        JButton btMaisUm = new JButton("+1 mês");
        JButton btLimpar = new JButton("Limpar");
        ComponentesWeb.botao(btAtraso, ComponentesWeb.Variante.SECUNDARIO, null, "Escolhe todos os meses em atraso");
        ComponentesWeb.botao(btAteHoje, ComponentesWeb.Variante.SECUNDARIO, null, "Escolhe tudo o que está por pagar até ao mês corrente");
        ComponentesWeb.botao(btMaisUm, ComponentesWeb.Variante.SECUNDARIO, null, "Acrescenta o mês seguinte por pagar (+)");
        ComponentesWeb.botao(btLimpar, ComponentesWeb.Variante.SECUNDARIO, null, "Larga todos os meses escolhidos");
        btAtraso.addActionListener(e -> escolherAte(ultimoCom(Mensalidades.Estado.ATRASO)));
        btAteHoje.addActionListener(e -> escolherAte(Periodo.actual()));
        btMaisUm.addActionListener(e -> maisUm());
        btLimpar.addActionListener(e -> {
            escolhidos.clear();
            actualizar();
        });
        JPanel atalhos = ComponentesWeb.transparente(new FlowLayout(FlowLayout.LEFT, 6, 0));
        atalhos.add(btAtraso);
        atalhos.add(btAteHoje);
        atalhos.add(btMaisUm);
        atalhos.add(btLimpar);

        JPanel legenda = ComponentesWeb.transparente(new FlowLayout(FlowLayout.LEFT, 12, 0));
        legenda.add(legendaItem("Pago", COR_PAGO));
        legenda.add(legendaItem("Em atraso", COR_ATRASO));
        legenda.add(legendaItem("Mês corrente", COR_ACTUAL));
        legenda.add(legendaItem("Por vencer", COR_FUTURO));
        legenda.add(legendaItem("Já na venda", ComponentesWeb.CINZA));

        JPanel corpoMeses = ComponentesWeb.transparente(new BorderLayout(0, 10));
        corpoMeses.add(FormularioWeb.pilha(10, resumo, atalhos), BorderLayout.NORTH);
        corpoMeses.add(ComponentesWeb.rolagemVertical(grelha), BorderLayout.CENTER);
        corpoMeses.add(legenda, BorderLayout.SOUTH);
        ComponentesWeb.Cartao cartaoMeses = FormularioWeb.cartao("Meses do contrato", COR_FUTURO, corpoMeses);

        // ---- escolha + total
        dcDataPagamento.setDate(new Date());
        FormularioWeb.data(dcDataPagamento);
        dcDataPagamento.addPropertyChangeListener("date", e -> actualizar());

        JTable tabela = new JTable(modeloEscolha);
        EstiloUI.estilizarTabela(tabela, true);
        tabela.getColumnModel().getColumn(0).setPreferredWidth(130);
        tabela.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
        tabela.setToolTipText("Pode escrever outro valor na coluna \"Multa a cobrar\" (reduzir pede um administrador e o motivo)");
        chkPerdoar.setOpaque(false);
        chkPerdoar.setFont(EstiloUI.FONTE);
        chkPerdoar.setToolTipText("Não cobra multa nestes meses (pede um administrador e o motivo; fica registado)");
        chkPerdoar.addActionListener(e -> actualizar());

        lbTotal.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(22f));
        lbTotal.setForeground(ComponentesWeb.TEXTO);
        JPanel caixaTotal = new ComponentesWeb.CaixaSuave(new BorderLayout(), EstiloUI.AZUL_CLARO, null);
        JLabel lbT = new JLabel("Total a adicionar");
        ComponentesWeb.rotulo(lbT);
        caixaTotal.add(lbT, BorderLayout.NORTH);
        caixaTotal.add(lbTotal, BorderLayout.CENTER);
        caixaTotal.add(lbResumoEscolha, BorderLayout.SOUTH);

        JPanel corpoEscolha = ComponentesWeb.transparente(new BorderLayout(0, 10));
        corpoEscolha.add(FormularioWeb.pilha(6,
                ComponentesWeb.campo("Data do pagamento (conta para a multa)", dcDataPagamento), chkPerdoar), BorderLayout.NORTH);
        javax.swing.JScrollPane rol = new javax.swing.JScrollPane(tabela);
        rol.setPreferredSize(new Dimension(280, 200));
        corpoEscolha.add(rol, BorderLayout.CENTER);
        corpoEscolha.add(caixaTotal, BorderLayout.SOUTH);
        ComponentesWeb.Cartao cartaoEscolha = FormularioWeb.cartao("A facturar", COR_PAGO, corpoEscolha);

        JPanel colunas = ComponentesWeb.transparente(new ComponentesWeb.ColunasResponsivas(
                new double[]{0.62, 0.38}, new int[]{420, 280}, 10));
        colunas.add(cartaoMeses);
        colunas.add(cartaoEscolha);

        // ---- acções
        JButton btCancelar = new JButton("Cancelar");
        FormularioWeb.botaoSair(btCancelar, "Fechar sem adicionar (Esc)");
        ComponentesWeb.botao(btAdicionar, ComponentesWeb.Variante.SUCESSO, "/imagens/salvar_16x16.png",
                "Adiciona os meses escolhidos à venda (Enter)");
        btCancelar.addActionListener(e -> dispose());
        btAdicionar.addActionListener(e -> enviar());

        JPanel accoes = FormularioWeb.barraAccoes(
                FormularioWeb.dica("Clique num mês: leva também os anteriores por pagar. Clique de novo para o largar."),
                btCancelar, btAdicionar);
        FormularioWeb.montar(this, cabecalho, colunas, accoes, "Mensalidades");

        JComponent raiz = getRootPane();
        raiz.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "sair");
        raiz.getActionMap().put("sair", accao(this::dispose));
        raiz.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "enviar");
        raiz.getActionMap().put("enviar", accao(this::enviar));
        raiz.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ADD, 0), "mais");
        raiz.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('+'), "mais");
        raiz.getActionMap().put("mais", accao(this::maisUm));

        // sugestão: tudo o que já venceu (atrasos + mês corrente); se estiver em dia, o mês seguinte
        if (Mensalidades.vencidos(Mensalidades.porPagar(meses, noCarrinho)) > 0) {
            escolherAte(Periodo.actual());
        } else {
            maisUm();
        }
    }

    private static AbstractAction accao(Runnable r) {
        return new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                r.run();
            }
        };
    }

    private static JLabel legendaItem(String texto, Color cor) {
        JLabel l = new JLabel(texto, new ComponentesWeb.IconeCor(cor, 10), SwingConstants.LEFT);
        l.setFont(EstiloUI.FONTE.deriveFont(12f));
        l.setForeground(ComponentesWeb.TEXTO_SUAVE);
        return l;
    }

    // =====================================================================
    // ESCOLHA
    // =====================================================================
    private boolean disponivel(Mensalidades.Mes m) {
        return !m.pago() && !noCarrinho.contains(m.periodo);
    }

    /** Escolhe todos os meses disponíveis até p (inclusive), por ordem. */
    private void escolherAte(Periodo p) {
        if (p == null) {
            return;
        }
        escolhidos.clear();
        for (Mensalidades.Mes m : meses) {
            if (m.periodo.compareTo(p) > 0) {
                break;
            }
            if (disponivel(m)) {
                escolhidos.add(m.periodo);
            }
        }
        actualizar();
    }

    private void maisUm() {
        for (Mensalidades.Mes m : meses) {
            if (disponivel(m) && !escolhidos.contains(m.periodo)) {
                escolhidos.add(m.periodo);
                break;
            }
        }
        actualizar();
    }

    private void alternar(Mensalidades.Mes m) {
        if (!disponivel(m)) {
            return;
        }
        if (escolhidos.contains(m.periodo)) {
            // larga este mês e os seguintes (não pode ficar um buraco no meio)
            escolhidos.removeIf(p -> p.compareTo(m.periodo) >= 0);
            actualizar();
        } else {
            escolherAte(m.periodo);
        }
    }

    private Periodo ultimoCom(Mensalidades.Estado estado) {
        Periodo ultimo = null;
        for (Mensalidades.Mes m : meses) {
            if (m.estado == estado && disponivel(m)) {
                ultimo = m.periodo;
            }
        }
        return ultimo;
    }

    private void actualizar() {
        aPreencherTabela = true;
        modeloEscolha.setRowCount(0);
        BigDecimal total = BigDecimal.ZERO;
        BigDecimal multas = BigDecimal.ZERO;
        for (Periodo p : escolhidos) {
            RegrasMulta.Calculo k = Mensalidades.calculoMulta(contrato, precoMensal, p, dcDataPagamento.getDate());
            BigDecimal multa = cobrar(p, k);
            modeloEscolha.addRow(new Object[]{p.texto(),
                CfMethods.formatarComoMoeda(precoMensal.doubleValue()),
                k.valor.signum() > 0 ? CfMethods.formatarComoMoeda(k.valor.doubleValue()) + "  (" + k.descricao() + ")" : "—",
                k.valor.signum() > 0 || multa.signum() > 0 ? CfMethods.formatarComoMoeda(multa.doubleValue()) : "—"});
            total = total.add(precoMensal).add(multa);
            multas = multas.add(multa);
        }
        aPreencherTabela = false;
        lbTotal.setText(CfMethods.formatarComoMoeda(total.doubleValue()));
        int n = escolhidos.size();
        if (n == 0) {
            lbResumoEscolha.setText("Nenhum mês escolhido");
        } else {
            String s = n + (n == 1 ? " mês: " : " meses: ") + escolhidos.get(0).curto()
                    + (n > 1 ? " a " + escolhidos.get(n - 1).curto() : "");
            if (multas.signum() > 0) {
                s += "  ·  inclui multa";
            }
            lbResumoEscolha.setText(s);
        }
        btAdicionar.setEnabled(n > 0);
        for (CartaoMes c : cartoes) {
            c.repaint();
        }
    }

    /** A multa que vai ser cobrada: 0 se perdoada, o valor escrito à mão ou a calculada. */
    private BigDecimal cobrar(Periodo p, RegrasMulta.Calculo k) {
        if (chkPerdoar.isSelected()) {
            return BigDecimal.ZERO;
        }
        BigDecimal a = ajustes.get(p);
        return a != null ? a : k.valor;
    }

    /** "1.500,00", "1500" ou "1500.5" → número; null se não se perceber. */
    static BigDecimal lerValor(String t) {
        if (t == null) {
            return null;
        }
        String s = t.replaceAll("[^0-9,.-]", "");
        if (s.contains(",")) {
            s = s.replace(".", "").replace(',', '.');
        }
        try {
            return s.isEmpty() ? BigDecimal.ZERO : new BigDecimal(s);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    // =====================================================================
    // ENVIAR PARA A VENDA
    // =====================================================================
    private void enviar() {
        if (escolhidos.isEmpty()) {
            return;
        }
        java.util.Map<Periodo, BigDecimal> cobrado = new java.util.HashMap<>();
        boolean reduziu = false;
        for (Periodo p : escolhidos) {
            RegrasMulta.Calculo k = Mensalidades.calculoMulta(contrato, precoMensal, p, dcDataPagamento.getDate());
            BigDecimal c = cobrar(p, k);
            cobrado.put(p, c);
            reduziu |= c.compareTo(k.valor) < 0;
        }
        String motivo = null;
        if (reduziu) {
            motivo = autorizarReducao(this);
            if (motivo == null) {
                return;
            }
        }
        Mensalidades.enviarParaVenda(conexao, produtoId, new ArrayList<>(escolhidos), dcDataPagamento.getDate(), cobrado, motivo);
        dispose();
    }

    /**
     * Reduzir ou perdoar multas: só um administrador, e com o motivo (fica no
     * registo da multa). Devolve o motivo, ou null se não for autorizado.
     */
    static String autorizarReducao(java.awt.Component pai) {
        if (!util.ui.SoAdministrador.administrador()) {
            JOptionPane.showMessageDialog(pai, "Só um administrador pode reduzir ou perdoar multas.\n"
                    + "Volte a pôr o valor calculado ou peça a um administrador.", "Multas", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        String motivo = JOptionPane.showInputDialog(pai, "Motivo da redução / perdão da multa:", "Multas",
                JOptionPane.QUESTION_MESSAGE);
        if (motivo == null || motivo.trim().isEmpty()) {
            JOptionPane.showMessageDialog(pai, "Indique o motivo (fica registado com a multa).", "Multas", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        return motivo.trim();
    }

    // =====================================================================
    // CARTÃO DE UM MÊS
    // =====================================================================
    private final class CartaoMes extends JComponent {

        private final Mensalidades.Mes mes;
        private boolean sobre;

        CartaoMes(Mensalidades.Mes mes) {
            this.mes = mes;
            setPreferredSize(new Dimension(96, 58));
            setMinimumSize(new Dimension(80, 54));
            if (disponivel(mes)) {
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }
            String dica = mes.periodo.texto() + " — "
                    + (noCarrinho.contains(mes.periodo) ? "já está na venda" : mes.estado.texto())
                    + (mes.vendaId > 0 ? " (venda nº " + mes.vendaId + ")" : "");
            setToolTipText(dica);
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    alternar(mes);
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    sobre = true;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    sobre = false;
                    repaint();
                }
            });
        }

        private Color cor() {
            if (noCarrinho.contains(mes.periodo)) {
                return ComponentesWeb.CINZA;
            }
            switch (mes.estado) {
                case PAGO:
                    return COR_PAGO;
                case ATRASO:
                    return COR_ATRASO;
                case ACTUAL:
                    return COR_ACTUAL;
                default:
                    return COR_FUTURO;
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = ComponentesWeb.suave(g);
            int w = getWidth(), h = getHeight();
            Color cor = cor();
            boolean escolhido = escolhidos.contains(mes.periodo);
            boolean livre = disponivel(mes);

            Color fundo = escolhido ? cor : ComponentesWeb.alfa(cor, livre ? (sobre ? 46 : 26) : 18);
            g2.setColor(fundo);
            g2.fillRoundRect(0, 0, w - 1, h - 1, 12, 12);
            g2.setColor(escolhido ? cor.darker() : ComponentesWeb.alfa(cor, livre ? 150 : 70));
            g2.setStroke(new java.awt.BasicStroke(escolhido ? 2f : 1f));
            g2.drawRoundRect(0, 0, w - 1, h - 1, 12, 12);

            Color texto = escolhido ? Color.WHITE : (livre ? cor.darker() : ComponentesWeb.TEXTO_SUAVE);
            g2.setColor(texto);
            g2.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(14f));
            String titulo = mes.periodo.nomeMes();
            if (g2.getFontMetrics().stringWidth(titulo) > w - 12) {
                titulo = Periodo.MESES_CURTOS[mes.periodo.getMes() - 1];
            }
            g2.drawString(titulo, 10, 23);

            String estado = noCarrinho.contains(mes.periodo) ? "Na venda"
                    : escolhido ? "A facturar" : mes.estado.texto();
            g2.setFont(EstiloUI.FONTE.deriveFont(Font.PLAIN, 11.5f));
            g2.drawString(estado, 10, 42);
            g2.dispose();
        }
    }
}
