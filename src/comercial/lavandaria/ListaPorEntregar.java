package comercial.lavandaria;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.RoundRectangle2D;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.SwingWorker;
import util.BDConexao;
import util.ui.ComponentesWeb;
import util.ui.EstiloUI;
import util.ui.FormularioWeb;

/**
 * Lista das recolhas com peças por entregar, para o ecrã da entrega: pesquisa
 * por cliente / telefone / nº, filtros (Hoje, Em atraso, Próximas, Todas) e um
 * clique abre a recolha. Primeiro as mais próximas de hoje (as de hoje, as dos
 * próximos dias e as atrasadas mais recentes), porque há muitas recolhas antigas
 * nunca entregues.
 */
public class ListaPorEntregar extends JPanel {

    private static final String LAVANDARIA = " i.data_entrega IS NOT NULL AND v.fk_documento IN (1, 2)"
            + " AND COALESCE(v.status_eliminado, 'false') <> 'true' ";
    private static final DateTimeFormatter DATA = DateTimeFormatter.ofPattern("dd/MM");
    /** Há milhares de recolhas antigas nunca entregues: mostram-se as mais próximas de hoje (a pesquisa pelo nº continua a achar as outras). */
    private static final int LIMITE = 400;

    /** Uma recolha por entregar. */
    static final class Linha {

        String codFact, cliente, telefone;
        int pecas;
        long dias; // data de entrega - hoje (negativo = atrasada)
        LocalDate data;
        double valor;
    }

    private final List<Linha> todas = new ArrayList<>();
    private final DefaultListModel<Linha> modelo = new DefaultListModel<>();
    private final JList<Linha> lista = new JList<>(modelo);
    private final JTextField txtProcurar = new JTextField();
    private final JToggleButton fHoje = new JToggleButton("Hoje"), fAtraso = new JToggleButton("Atrasadas"),
            fProximas = new JToggleButton("Próximas"), fTodas = new JToggleButton("Todas", true);
    private final JLabel lbEstado = FormularioWeb.dica(" ");
    private final Consumer<String> abrir;
    private boolean aLer;

    /** abrir recebe o nº da recolha (FR / FT) escolhida. */
    public ListaPorEntregar(Consumer<String> abrir) {
        super(new BorderLayout(0, 8));
        this.abrir = abrir;
        setOpaque(false);

        FormularioWeb.campoTexto(txtProcurar);
        txtProcurar.setToolTipText("Nome do cliente, telefone ou nº da recolha");
        txtProcurar.putClientProperty("JTextField.placeholderText", "Procurar cliente, telefone ou nº");
        txtProcurar.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                filtrar();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                filtrar();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                filtrar();
            }
        });
        ButtonGroup g = new ButtonGroup();
        for (JToggleButton b : new JToggleButton[]{fTodas, fHoje, fAtraso, fProximas}) {
            g.add(b);
            b.addActionListener(e -> filtrar());
        }

        lista.setCellRenderer(new Cartao());
        lista.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lista.setOpaque(false);
        lista.setFixedCellHeight(62);
        lista.setToolTipText("Clique para abrir a recolha");
        lista.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int i = lista.locationToIndex(e.getPoint());
                if (i >= 0 && lista.getCellBounds(i, i).contains(e.getPoint())) {
                    abrirLinha(modelo.get(i));
                }
            }
        });
        lista.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent e) {
                if (e.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER && lista.getSelectedValue() != null) {
                    abrirLinha(lista.getSelectedValue());
                }
            }
        });
        JScrollPane rolagem = new JScrollPane(lista);
        rolagem.setBorder(null);
        rolagem.setOpaque(false);
        rolagem.getViewport().setOpaque(false);
        rolagem.getVerticalScrollBar().setUnitIncrement(24);
        rolagem.setPreferredSize(new Dimension(260, 240));
        rolagem.setMinimumSize(new Dimension(200, 120));

        JPanel topo = ComponentesWeb.transparente(new BorderLayout(0, 6));
        topo.add(ComponentesWeb.campo("Procurar (cliente, telefone ou nº)", txtProcurar), BorderLayout.NORTH);
        topo.add(FormularioWeb.pilulas(fTodas, fHoje, fAtraso, fProximas), BorderLayout.CENTER);
        topo.add(lbEstado, BorderLayout.SOUTH);

        add(topo, BorderLayout.NORTH);
        add(rolagem, BorderLayout.CENTER);
    }

    private void abrirLinha(Linha l) {
        if (l != null && abrir != null) {
            abrir.accept(l.codFact);
        }
    }

    /** Lê as recolhas por entregar em segundo plano. */
    public void actualizar() {
        if (aLer) {
            return;
        }
        aLer = true;
        lbEstado.setText("A carregar...");
        new SwingWorker<List<Linha>, Void>() {
            @Override
            protected List<Linha> doInBackground() throws Exception {
                try (Connection cn = BDConexao.novaLigacao()) {
                    return ler(cn);
                }
            }

            @Override
            protected void done() {
                aLer = false;
                try {
                    todas.clear();
                    todas.addAll(get());
                    filtrar();
                } catch (Exception e) {
                    Throwable c = e.getCause() != null ? e.getCause() : e;
                    lbEstado.setText("Não foi possível ler: " + c.getMessage());
                }
            }
        }.execute();
    }

    static List<Linha> ler(Connection cn) throws Exception {
        List<Linha> r = new ArrayList<>();
        LocalDate hoje = LocalDate.now();
        try (Statement st = cn.createStatement();
                ResultSet rs = st.executeQuery("SELECT v.cod_fact, COALESCE(c.nome, ''), COALESCE(c.telefone, ''), COUNT(*),"
                        + " MIN(DATE(i.data_entrega)), SUM(i.total)"
                        + " FROM tb_item_venda i JOIN tb_venda v ON v.codigo = i.codigo_venda"
                        + " LEFT JOIN tb_cliente c ON c.codigo = v.codigo_cliente WHERE" + LAVANDARIA + " AND i.status_entrega = 0"
                        + " GROUP BY v.codigo"
                        // primeiro hoje e os próximos dias, depois as atrasadas mais recentes
                        + " ORDER BY (MIN(DATE(i.data_entrega)) < CURDATE()), ABS(DATEDIFF(MIN(DATE(i.data_entrega)), CURDATE())), v.codigo DESC"
                        + " LIMIT " + LIMITE)) {
            while (rs.next()) {
                Linha l = new Linha();
                l.codFact = rs.getString(1);
                l.cliente = rs.getString(2);
                l.telefone = rs.getString(3);
                l.pecas = rs.getInt(4);
                l.data = rs.getDate(5) == null ? null : rs.getDate(5).toLocalDate();
                l.dias = l.data == null ? 0 : ChronoUnit.DAYS.between(hoje, l.data);
                l.valor = rs.getDouble(6);
                r.add(l);
            }
        }
        return r;
    }

    private void filtrar() {
        String q = txtProcurar.getText() == null ? "" : txtProcurar.getText().trim().toLowerCase();
        Linha antes = lista.getSelectedValue();
        modelo.clear();
        int n = 0;
        for (Linha l : todas) {
            if (fHoje.isSelected() && l.dias != 0 || fAtraso.isSelected() && l.dias >= 0 || fProximas.isSelected() && l.dias <= 0) {
                continue;
            }
            if (!q.isEmpty() && !(l.cliente.toLowerCase().contains(q) || l.telefone.contains(q) || l.codFact.toLowerCase().contains(q))) {
                continue;
            }
            modelo.addElement(l);
            n++;
        }
        if (antes != null) {
            lista.setSelectedValue(antes, false);
        }
        lbEstado.setText(n == 0 ? "Nenhuma recolha por entregar aqui."
                : n + (n == 1 ? " recolha" : " recolhas") + (todas.size() >= LIMITE ? " (as mais próximas de hoje)" : "") + " · clique para abrir");
    }

    /** Recolha em cartão: nº e cliente, peças, data e a situação colorida. */
    private static final class Cartao extends JComponent implements ListCellRenderer<Linha> {

        private Linha l;
        private boolean sel;

        @Override
        public Component getListCellRendererComponent(JList<? extends Linha> list, Linha v, int i, boolean s, boolean f) {
            l = v;
            sel = s;
            return this;
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(240, 62);
        }

        @Override
        protected void paintComponent(Graphics g) {
            if (l == null) {
                return;
            }
            Graphics2D g2 = ComponentesWeb.suave(g);
            int w = getWidth(), h = getHeight();
            Color cor = l.dias < 0 ? ComponentesWeb.VERMELHO : l.dias == 0 ? ComponentesWeb.AZUL_VIVO
                    : l.dias == 1 ? new Color(217, 119, 6) : ComponentesWeb.VERDE;
            g2.setColor(sel ? ComponentesWeb.mistura(ComponentesWeb.AZUL_VIVO, Color.WHITE, 0.88) : Color.WHITE);
            g2.fill(new RoundRectangle2D.Double(1, 2, w - 3, h - 6, 12, 12));
            g2.setColor(sel ? ComponentesWeb.AZUL_VIVO : ComponentesWeb.LINHA);
            g2.draw(new RoundRectangle2D.Double(1, 2, w - 3, h - 6, 12, 12));
            g2.setColor(cor);
            g2.fill(new RoundRectangle2D.Double(1, 10, 4, h - 22, 4, 4));

            // situação (chip à direita)
            String sit = l.data == null ? "Sem data" : l.dias < 0 ? "Atrasada " + (-l.dias) + "d" : l.dias == 0 ? "Hoje"
                    : l.dias == 1 ? "Amanhã" : "Em " + l.dias + " dias";
            g2.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(11f));
            FontMetrics fc = g2.getFontMetrics();
            int cw = fc.stringWidth(sit) + 16, ch = 20, cx = w - cw - 10, cy = 9;
            g2.setColor(ComponentesWeb.mistura(cor, Color.WHITE, 0.86));
            g2.fillRoundRect(cx, cy, cw, ch, ch, ch);
            g2.setColor(cor.darker());
            g2.drawString(sit, cx + 8, cy + (ch + fc.getAscent() - fc.getDescent()) / 2);

            // nº da recolha e cliente
            g2.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(13f));
            g2.setColor(ComponentesWeb.TEXTO);
            g2.drawString(cortar(g2, l.codFact, cx - 22), 14, 24);
            g2.setFont(EstiloUI.FONTE.deriveFont(12f));
            g2.setColor(ComponentesWeb.TEXTO_SUAVE);
            String quem = l.cliente.isEmpty() ? "(sem cliente)" : l.cliente;
            String det = l.pecas + (l.pecas == 1 ? " peça" : " peças") + (l.data == null ? "" : " · " + l.data.format(DATA));
            FontMetrics f = g2.getFontMetrics();
            int dw = f.stringWidth(det);
            g2.drawString(cortar(g2, quem, w - dw - 40), 14, 45);
            g2.drawString(det, w - dw - 12, 45);
            g2.dispose();
        }

        private static String cortar(Graphics2D g2, String s, int largura) {
            FontMetrics f = g2.getFontMetrics();
            if (largura <= 10 || f.stringWidth(s) <= largura) {
                return largura <= 10 ? "" : s;
            }
            while (s.length() > 1 && f.stringWidth(s + "…") > largura) {
                s = s.substring(0, s.length() - 1);
            }
            return s + "…";
        }
    }
}
