package comercial.mensalidade;

import comercial.controller.ClientesController;
import entity.TbCliente;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Window;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.text.Normalizer;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.KeyStroke;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import kitanda.util.CfMethods;
import util.BDConexao;
import util.ui.ComponentesWeb;
import util.ui.EstiloUI;
import util.ui.FormularioWeb;

/**
 * Estado das mensalidades de todos os clientes activos (com contrato), em
 * cartões:
 * <ul>
 * <li>verde — pago / ainda dentro do prazo;</li>
 * <li>laranja — faltam até N dias (dias_alerta) para pagar;</li>
 * <li>vermelho — falta 1 dia, vence hoje ou já está em atraso.</li>
 * </ul>
 * Abre sozinho na janela de vendas quando o alerta dispara (os cartões em
 * alerta piscam) e abre-se à mão (F9 na venda, menu). Clicar num cartão em
 * alerta pergunta se quer pagar: "Sim" põe o cliente e os meses na venda;
 * num cartão verde mostra os meses já pagos.
 */
public class EstadoClientesDialog extends JDialog {

    private static final Color VERDE = ComponentesWeb.VERDE;
    private static final Color LARANJA = ComponentesWeb.LARANJA;
    private static final Color VERMELHO = ComponentesWeb.VERMELHO;
    private static final DateTimeFormatter DM = DateTimeFormatter.ofPattern("dd/MM");

    private static EstadoClientesDialog aberto;

    private enum Nivel {
        VERMELHO, LARANJA, VERDE
    }

    /** Um cliente e os seus contratos. */
    private static final class Cliente {

        int id;
        String nome, telefone = "";
        final List<Devedores.Linha> contratos = new ArrayList<>();
        Nivel nivel = Nivel.VERDE;
        long dias = Long.MAX_VALUE; // dias para o próximo vencimento (negativo = atraso)
        int mesesAtraso;
        BigDecimal aPagar = BigDecimal.ZERO;
        LocalDate proximo;

        boolean emAlerta() {
            return nivel != Nivel.VERDE;
        }

        String estado() {
            if (mesesAtraso > 0) {
                return "Em atraso · " + mesesAtraso + (mesesAtraso == 1 ? " mês" : " meses");
            }
            if (dias == 0) {
                return "Vence hoje";
            }
            if (dias == 1) {
                return "Falta 1 dia";
            }
            if (nivel == Nivel.LARANJA) {
                return "Faltam " + dias + " dias";
            }
            return proximo == null ? "Tudo pago" : "Em dia · vence " + proximo.format(DM);
        }
    }

    private final BDConexao conexao;
    private final boolean pornAlerta;
    private ConfigMensalidade config = ConfigMensalidade.ler();
    private final List<Cliente> clientes = new ArrayList<>();
    private final JPanel grelha = ComponentesWeb.transparente(new ComponentesWeb.GrelhaResponsiva(210, 10, 6));
    private final JTextField txtPesquisa = new JTextField();
    private final JToggleButton fTodos = new JToggleButton("Todos");
    private final JToggleButton fAlerta = new JToggleButton("Em alerta");
    private final JToggleButton fUrgente = new JToggleButton("Urgentes");
    private final JToggleButton fVerde = new JToggleButton("Em dia");
    private final JLabel lbResumo = new JLabel(" ");
    private final JLabel lbEstado = FormularioWeb.dica(" ");
    private final Timer pisca;
    private Timer fecho;
    private boolean aceso = true;
    private int restante;
    private java.awt.event.AWTEventListener ouvinteInteraccao;

    // =====================================================================
    // ABRIR
    // =====================================================================
    /** Aberto à mão (F9 na venda, menu): fica até o fecharem. */
    public static void abrir(Window dono, BDConexao conexao) {
        if (!Mensalidades.activo()) {
            return;
        }
        if (aberto != null && aberto.isDisplayable()) {
            aberto.toFront();
            aberto.recarregar();
            return;
        }
        aberto = new EstadoClientesDialog(dono, conexao, false, true);
        aberto.setVisible(true);
    }

    /** Aberto pelo alerta: os cartões em alerta piscam e fecha sozinho ao fim da duração configurada. */
    static void abrirPorAlerta(Window dono, BDConexao conexao) {
        if (aberto != null && aberto.isDisplayable()) {
            aberto.dispose();
        }
        aberto = new EstadoClientesDialog(dono, conexao, true, true);
        aberto.setVisible(true);
    }

    private EstadoClientesDialog(Window dono, BDConexao conexao, boolean porAlerta, boolean carregar) {
        super(dono, ModalityType.MODELESS);
        this.conexao = conexao;
        this.pornAlerta = porAlerta;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setTitle("Estado das mensalidades");
        pisca = new Timer(600, e -> {
            aceso = !aceso;
            grelha.repaint();
        });
        montar();
        FormularioWeb.ajustar(this, 1180, 760, 640, 460);
        if (porAlerta) {
            fAlerta.setSelected(true);
            restante = config.duracaoSegundos;
            fecho = new Timer(1000, e -> {
                if (--restante <= 0) {
                    dispose();
                } else {
                    lbEstado.setText("Alerta de devedores e renovações · fecha em " + restante / 60 + ":" + String.format("%02d", restante % 60)
                            + "  ·  mexa na janela para silenciar o alarme e a manter aberta");
                }
            });
            fecho.start();
        } else {
            fTodos.setSelected(true);
        }
        if (carregar) {
            recarregar();
        }
    }

    /** Só para pré-visualizações: dados já calculados, sem BD. */
    static EstadoClientesDialog previa(List<Devedores.Linha> linhas, boolean porAlerta) {
        EstadoClientesDialog d = new EstadoClientesDialog(null, null, porAlerta, false);
        d.clientes.addAll(d.agrupar(linhas));
        d.mostrar();
        return d;
    }

    @Override
    public void dispose() {
        pisca.stop();
        if (pornAlerta) {
            AlertasMensalidade.pararAlarme();
        }
        if (ouvinteInteraccao != null) {
            java.awt.Toolkit.getDefaultToolkit().removeAWTEventListener(ouvinteInteraccao);
        }
        if (fecho != null) {
            fecho.stop();
        }
        if (aberto == this) {
            aberto = null;
        }
        super.dispose();
    }

    /** O utilizador pegou na janela: deixa de fechar sozinha. */
    private void manterAberta() {
        // alguém está a tratar do alerta: o alarme cala-se
        if (pornAlerta) {
            AlertasMensalidade.pararAlarme();
        }
        if (fecho != null && fecho.isRunning()) {
            fecho.stop();
            lbEstado.setText("Clique num cartão em alerta para cobrar; num verde para ver os meses pagos.");
        }
    }

    // =====================================================================
    // ECRÃ
    // =====================================================================
    private void montar() {
        FormularioWeb.campoTexto(txtPesquisa);
        txtPesquisa.setToolTipText("Nome ou telefone (Ctrl+F)");
        txtPesquisa.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                mostrar();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                mostrar();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });
        // Enter na pesquisa com um só cliente: age sobre ele
        txtPesquisa.addActionListener(e -> {
            List<Cliente> v = visiveis();
            if (v.size() == 1) {
                clicar(v.get(0));
            }
        });
        JPanel campo = FormularioWeb.campoCabecalho(new JLabel("Procurar cliente"), txtPesquisa);
        campo.setPreferredSize(new Dimension(260, 56));
        JPanel direita = ComponentesWeb.transparente(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        direita.add(campo);
        JPanel cabecalho = FormularioWeb.cabecalho("Estado das mensalidades",
                "Verde: em dia  ·  Laranja: faltam até " + config.diasAlerta + " dias  ·  Vermelho: 1 dia, hoje ou em atraso",
                direita);

        ButtonGroup g = new ButtonGroup();
        for (JToggleButton b : new JToggleButton[]{fTodos, fAlerta, fUrgente, fVerde}) {
            g.add(b);
            b.addActionListener(e -> mostrar());
        }
        JPanel filtros = FormularioWeb.pilulas(fTodos, fAlerta, fUrgente, fVerde);
        lbResumo.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(13f));
        lbResumo.setForeground(ComponentesWeb.TEXTO);
        JPanel topo = ComponentesWeb.transparente(new BorderLayout(10, 0));
        topo.add(filtros, BorderLayout.WEST);
        topo.add(lbResumo, BorderLayout.EAST);

        JPanel corpo = ComponentesWeb.transparente(new BorderLayout(0, 10));
        corpo.add(topo, BorderLayout.NORTH);
        // os cartões ficam com a altura deles (não esticam até ao fundo)
        JPanel topoGrelha = ComponentesWeb.transparente(new BorderLayout());
        topoGrelha.add(grelha, BorderLayout.NORTH);
        corpo.add(topoGrelha, BorderLayout.CENTER);
        ComponentesWeb.Cartao cartao = FormularioWeb.cartao("Clientes", ComponentesWeb.AZUL_VIVO, corpo);

        JButton btInscricoes = new JButton("Desistências e reactivações");
        ComponentesWeb.botao(btInscricoes, ComponentesWeb.Variante.SECUNDARIO, null,
                "Marcar clientes como desistentes ou desactivados e reactivá-los quando voltam");
        btInscricoes.addActionListener(e -> {
            InscricoesVisao.abrir(this, conexao);
            recarregar();
        });
        JButton btDevedores = new JButton("Controlo de devedores");
        JButton btActualizar = new JButton("Actualizar");
        JButton btFechar = new JButton("Fechar");
        ComponentesWeb.botao(btDevedores, ComponentesWeb.Variante.SECUNDARIO, null, "Relatório com todos os filtros");
        ComponentesWeb.botao(btActualizar, ComponentesWeb.Variante.SECUNDARIO, null, "Ler de novo (F5)");
        FormularioWeb.botaoSair(btFechar, "Fechar (Esc)");
        btDevedores.addActionListener(e -> DevedoresVisao.abrir(this, conexao));
        btActualizar.addActionListener(e -> recarregar());
        btFechar.addActionListener(e -> dispose());
        lbEstado.setText("Clique num cartão em alerta para cobrar; num verde para ver os meses pagos.");
        JPanel accoes = FormularioWeb.barraAccoes(lbEstado, btInscricoes, btDevedores, btActualizar, btFechar);

        FormularioWeb.montar(this, cabecalho, cartao, accoes, "Estado das mensalidades");
        JComponent raiz = getRootPane();
        raiz.registerKeyboardAction(e -> dispose(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
        raiz.registerKeyboardAction(e -> recarregar(), KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
        raiz.registerKeyboardAction(e -> txtPesquisa.requestFocusInWindow(),
                KeyStroke.getKeyStroke(KeyEvent.VK_F, java.awt.event.InputEvent.CTRL_DOWN_MASK), JComponent.WHEN_IN_FOCUSED_WINDOW);
        // qualquer interacção mantém a janela aberta
        ouvinteInteraccao = ev -> {
            if (ev.getSource() instanceof java.awt.Component
                    && javax.swing.SwingUtilities.getWindowAncestor((java.awt.Component) ev.getSource()) == this
                    && (ev.getID() == MouseEvent.MOUSE_PRESSED || ev.getID() == KeyEvent.KEY_PRESSED)) {
                manterAberta();
            }
        };
        java.awt.Toolkit.getDefaultToolkit().addAWTEventListener(ouvinteInteraccao,
                java.awt.AWTEvent.MOUSE_EVENT_MASK | java.awt.AWTEvent.KEY_EVENT_MASK);
    }

    // =====================================================================
    // DADOS
    // =====================================================================
    void recarregar() {
        lbResumo.setText("A carregar…");
        new SwingWorker<List<Cliente>, Void>() {
            @Override
            protected List<Cliente> doInBackground() throws Exception {
                config = ConfigMensalidade.ler();
                return agrupar(Devedores.carregar(LocalDate.now(), config.diasAlerta));
            }

            @Override
            protected void done() {
                try {
                    clientes.clear();
                    clientes.addAll(get());
                    mostrar();
                } catch (Exception e) {
                    lbResumo.setText("Erro: " + e.getMessage());
                }
            }
        }.execute();
    }

    private List<Cliente> agrupar(List<Devedores.Linha> linhas) {
        Map<Integer, Cliente> m = new LinkedHashMap<>();
        for (Devedores.Linha l : linhas) {
            if (l.inactivo()) {
                continue; // desistentes e desactivados: ver "Inscrições"
            }
            Cliente c = m.computeIfAbsent(l.clienteId, k -> {
                Cliente n = new Cliente();
                n.id = l.clienteId;
                n.nome = l.cliente;
                n.telefone = l.telefone;
                return n;
            });
            c.contratos.add(l);
            Nivel nv = nivel(l);
            if (nv.ordinal() < c.nivel.ordinal()) {
                c.nivel = nv;
            }
            c.mesesAtraso += l.emAtraso.size();
            long d = !l.emAtraso.isEmpty() ? -l.diasAtraso : l.diasParaVencer;
            c.dias = Math.min(c.dias, d);
            if (l.proximoVencimento != null && (c.proximo == null || l.proximoVencimento.isBefore(c.proximo))) {
                c.proximo = l.proximoVencimento;
            }
            c.aPagar = c.aPagar.add(l.preco.multiply(new BigDecimal(aPagar(l).size())));
            c.aPagar = c.aPagar.add(l.multa);
        }
        List<Cliente> r = new ArrayList<>(m.values());
        // os mais urgentes primeiro, depois por nome
        r.sort((a, b) -> a.nivel != b.nivel ? a.nivel.compareTo(b.nivel)
                : a.dias != b.dias ? Long.compare(a.dias, b.dias) : a.nome.compareToIgnoreCase(b.nome));
        return r;
    }

    private Nivel nivel(Devedores.Linha l) {
        if (!l.emAtraso.isEmpty() || l.diasParaVencer <= 1) {
            return Nivel.VERMELHO; // 1 dia, hoje ou em atraso
        }
        if (l.diasParaVencer <= config.diasAlerta) {
            return Nivel.LARANJA;
        }
        return Nivel.VERDE;
    }

    /** Meses a cobrar de um contrato: os em atraso e o que está prestes a vencer. */
    private List<Periodo> aPagar(Devedores.Linha l) {
        List<Periodo> r = new ArrayList<>(l.emAtraso);
        if (l.proximoPeriodo != null && l.diasParaVencer <= config.diasAlerta) {
            r.add(l.proximoPeriodo);
        }
        return r;
    }

    private static String chave(String s) {
        return Normalizer.normalize(s == null ? "" : s, Normalizer.Form.NFD).replaceAll("\\p{M}", "").toLowerCase();
    }

    private List<Cliente> visiveis() {
        String q = chave(txtPesquisa.getText().trim());
        List<Cliente> r = new ArrayList<>();
        for (Cliente c : clientes) {
            if (!q.isEmpty() && !chave(c.nome).contains(q) && !c.telefone.replace(" ", "").contains(q.replace(" ", ""))) {
                continue;
            }
            if (fAlerta.isSelected() && !c.emAlerta()) {
                continue;
            }
            if (fUrgente.isSelected() && c.nivel != Nivel.VERMELHO) {
                continue;
            }
            if (fVerde.isSelected() && c.nivel != Nivel.VERDE) {
                continue;
            }
            r.add(c);
        }
        return r;
    }

    private void mostrar() {
        int verm = 0, lar = 0, ver = 0;
        for (Cliente c : clientes) {
            if (c.nivel == Nivel.VERMELHO) {
                verm++;
            } else if (c.nivel == Nivel.LARANJA) {
                lar++;
            } else {
                ver++;
            }
        }
        fTodos.setText("Todos (" + clientes.size() + ")");
        fAlerta.setText("Em alerta (" + (verm + lar) + ")");
        fUrgente.setText("Urgentes (" + verm + ")");
        fVerde.setText("Em dia (" + ver + ")");

        grelha.removeAll();
        List<Cliente> v = visiveis();
        for (Cliente c : v) {
            grelha.add(new CartaoCliente(c));
        }
        if (v.isEmpty()) {
            grelha.add(FormularioWeb.dica(clientes.isEmpty() ? "Nenhum cliente com contrato de mensalidade."
                    : "Nenhum cliente com estes filtros."));
        }
        lbResumo.setText(verm + " urgente(s)  ·  " + lar + " a vencer  ·  " + ver + " em dia");
        grelha.revalidate();
        grelha.repaint();
        boolean piscar = pornAlerta && (verm + lar) > 0;
        if (piscar && !pisca.isRunning()) {
            pisca.start();
        } else if (!piscar) {
            pisca.stop();
            aceso = true;
        }
    }

    // =====================================================================
    // CLIQUE
    // =====================================================================
    private void clicar(Cliente c) {
        manterAberta();
        if (!c.emAlerta()) {
            verMeses(c);
            return;
        }
        StringBuilder sb = new StringBuilder("Pretende pagar a mensalidade de " + c.nome + "?\n\n");
        for (Devedores.Linha l : c.contratos) {
            List<Periodo> ps = aPagar(l);
            if (ps.isEmpty()) {
                continue;
            }
            sb.append("• ").append(l.servico).append(": ");
            for (int i = 0; i < ps.size(); i++) {
                sb.append(i > 0 ? ", " : "").append(ps.get(i).curto());
            }
            sb.append('\n');
        }
        sb.append("\nTotal: ").append(CfMethods.formatarComoMoeda(c.aPagar.doubleValue()));
        Object[] opcoes = {"Sim", "Não"};
        int r = JOptionPane.showOptionDialog(this, sb.toString(), "Pagar mensalidade",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, opcoes, opcoes[0]);
        if (r == 0) {
            pagarNaVenda(c);
        }
    }

    /** "Sim": cliente escolhido na venda e os meses no carrinho; a venda vem para a frente. */
    private void pagarNaVenda(Cliente c) {
        Window venda = Mensalidades.janelaVendaAberta();
        if (venda == null) {
            JOptionPane.showMessageDialog(this, "Abra a janela de vendas para cobrar a mensalidade.");
            return;
        }
        Mensalidades.seleccionarClienteNaVenda(c.nome);
        Date hoje = new Date();
        for (Devedores.Linha l : c.contratos) {
            List<Periodo> ps = aPagar(l);
            ps.removeAll(Mensalidades.noCarrinho(l.produtoId));
            if (!ps.isEmpty()) {
                Mensalidades.enviarParaVenda(conexao, l.produtoId, ps, hoje);
            }
        }
        dispose();
        venda.toFront();
        venda.requestFocus();
    }

    private void verMeses(Cliente c) {
        TbCliente cli = new ClientesController(conexao).findByCodigo(c.id);
        if (cli != null) {
            PainelContratosCliente.abrirDialogo(this, conexao, cli);
            recarregar();
        }
    }

    private void menu(Cliente c, MouseEvent e) {
        JPopupMenu m = new JPopupMenu();
        JMenuItem pagar = new JMenuItem("Pagar na venda");
        pagar.setEnabled(c.emAlerta());
        pagar.addActionListener(ev -> clicar(c));
        JMenuItem ver = new JMenuItem("Ver meses pagos e por pagar");
        ver.addActionListener(ev -> verMeses(c));
        JMenuItem copiar = new JMenuItem("Copiar telefone" + (c.telefone.isEmpty() ? "" : " (" + c.telefone + ")"));
        copiar.setEnabled(!c.telefone.isEmpty());
        copiar.addActionListener(ev -> java.awt.Toolkit.getDefaultToolkit().getSystemClipboard()
                .setContents(new StringSelection(c.telefone), null));
        m.add(pagar);
        m.add(ver);
        m.addSeparator();
        m.add(copiar);
        m.show(e.getComponent(), e.getX(), e.getY());
    }

    // =====================================================================
    // CARTÃO
    // =====================================================================
    private final class CartaoCliente extends JComponent {

        private final Cliente c;
        private boolean sobre;

        CartaoCliente(Cliente c) {
            this.c = c;
            setPreferredSize(new Dimension(210, 96));
            setMinimumSize(new Dimension(160, 96));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            StringBuilder dica = new StringBuilder("<html><b>" + c.nome + "</b>");
            if (!c.telefone.isEmpty()) {
                dica.append("<br>Tel. ").append(c.telefone);
            }
            for (Devedores.Linha l : c.contratos) {
                dica.append("<br>").append(l.servico).append(" — ").append(l.situacao.texto())
                        .append(", vence ").append(Mensalidades.textoDiaPagamento(l.contrato.getDiaPagamento()));
            }
            dica.append("<br><i>").append(c.emAlerta() ? "Clique para cobrar" : "Clique para ver os meses")
                    .append(" · botão direito: mais opções</i></html>");
            setToolTipText(dica.toString());
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (javax.swing.SwingUtilities.isLeftMouseButton(e)) {
                        clicar(c);
                    }
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    if (e.isPopupTrigger()) {
                        menu(c, e);
                    }
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    if (e.isPopupTrigger()) {
                        menu(c, e);
                    }
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
            return c.nivel == Nivel.VERMELHO ? VERMELHO : c.nivel == Nivel.LARANJA ? LARANJA : VERDE;
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = ComponentesWeb.suave(g);
            int w = getWidth(), h = getHeight();
            Color cor = cor();
            // em alerta: cartão "aceso" com a cor cheia; a piscar alterna com o tom suave
            boolean cheio = c.emAlerta() && aceso;
            g2.setColor(cheio ? cor : ComponentesWeb.alfa(cor, sobre ? 50 : 30));
            g2.fillRoundRect(0, 0, w - 1, h - 1, 14, 14);
            g2.setColor(sobre ? cor.darker() : ComponentesWeb.alfa(cor, 160));
            g2.setStroke(new java.awt.BasicStroke(sobre ? 2f : 1f));
            g2.drawRoundRect(0, 0, w - 1, h - 1, 14, 14);

            Color texto = cheio ? Color.WHITE : ComponentesWeb.TEXTO;
            Color suave = cheio ? new Color(255, 255, 255, 215) : ComponentesWeb.TEXTO_SUAVE;
            g2.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(14f));
            FontMetrics fm = g2.getFontMetrics();
            g2.setColor(texto);
            g2.drawString(cortar(c.nome, fm, w - 24), 12, 24);

            g2.setFont(EstiloUI.FONTE.deriveFont(12f));
            fm = g2.getFontMetrics();
            g2.setColor(suave);
            StringBuilder serv = new StringBuilder();
            for (Devedores.Linha l : c.contratos) {
                serv.append(serv.length() > 0 ? ", " : "").append(l.servico);
            }
            g2.drawString(cortar(serv.toString(), fm, w - 24), 12, 44);

            // estado (com texto: nunca só a cor)
            g2.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(12.5f));
            g2.setColor(cheio ? Color.WHITE : cor.darker());
            g2.drawString("● " + c.estado(), 12, 68);

            if (c.emAlerta() && c.aPagar.signum() > 0) {
                g2.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(12f));
                fm = g2.getFontMetrics();
                String v = CfMethods.formatarComoMoeda(c.aPagar.doubleValue());
                g2.setColor(texto);
                g2.drawString(v, w - 12 - fm.stringWidth(v), 86);
            }
            g2.dispose();
        }

        private String cortar(String s, FontMetrics fm, int max) {
            if (fm.stringWidth(s) <= max) {
                return s;
            }
            while (s.length() > 1 && fm.stringWidth(s + "…") > max) {
                s = s.substring(0, s.length() - 1);
            }
            return s + "…";
        }
    }
}
