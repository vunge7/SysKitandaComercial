package comercial.lavandaria;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import util.BDConexao;
import util.ui.ComponentesWeb;
import util.ui.EstiloUI;
import util.ui.FormularioWeb;

/**
 * Painel do dia da lavandaria: recolhas, peças por entregar, prontas para hoje,
 * em atraso, entregas feitas e o facturado; e a lista das peças por entregar
 * (duplo clique abre a entrega já com a recolha procurada).
 *
 * Peças da lavandaria = linhas de FR / FT com data de entrega (as vendas da
 * loja não a têm); a entrega é a nota EL (documento 14).
 */
public class PainelLavandaria extends JPanel {

    private static final String LAVANDARIA = " i.data_entrega IS NOT NULL AND v.fk_documento IN (1, 2)"
            + " AND COALESCE(v.status_eliminado, 'false') <> 'true' ";
    private static final DateTimeFormatter DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final ComponentesWeb.CartaoKpi kRecolhas = new ComponentesWeb.CartaoKpi(ComponentesWeb.CartaoKpi.ENTRADA, "Recolhas hoje", ComponentesWeb.AZUL_VIVO).compacto();
    private final ComponentesWeb.CartaoKpi kPorEntregar = new ComponentesWeb.CartaoKpi("P", "Peças por entregar", ComponentesWeb.ROXO).compacto();
    private final ComponentesWeb.CartaoKpi kHoje = new ComponentesWeb.CartaoKpi(ComponentesWeb.CartaoKpi.VISTO, "Para entregar hoje", ComponentesWeb.CIANO).compacto();
    private final ComponentesWeb.CartaoKpi kAtraso = new ComponentesWeb.CartaoKpi("!", "Em atraso", ComponentesWeb.VERMELHO).compacto();
    private final ComponentesWeb.CartaoKpi kEntregas = new ComponentesWeb.CartaoKpi(ComponentesWeb.CartaoKpi.SAIDA, "Entregas hoje", ComponentesWeb.VERDE).compacto();
    private final ComponentesWeb.CartaoKpi kFacturado = new ComponentesWeb.CartaoKpi("Kz", "Facturado hoje", ComponentesWeb.LARANJA).compacto();

    private final DefaultTableModel modelo = new DefaultTableModel(new String[]{"Recolha", "Cliente", "Telefone", "Peças", "Entrega", "Situação", "Valor"}, 0) {
        @Override
        public boolean isCellEditable(int l, int c) {
            return false;
        }
    };
    private final JTable tabela = new JTable(modelo);
    private final JToggleButton fTodas = new JToggleButton("Todas", true), fAtraso = new JToggleButton("Em atraso"),
            fHoje = new JToggleButton("Para hoje");
    private final JLabel lbEstado = FormularioWeb.dica(" ");
    private final List<Object[]> linhas = new ArrayList<>();
    private final Consumer<String> abrirEntrega;
    private long ultimaLeitura;

    /** abrirEntrega recebe o nº da recolha (FR / FT) a entregar. */
    public PainelLavandaria(Consumer<String> abrirEntrega) {
        super(new BorderLayout(0, 12));
        this.abrirEntrega = abrirEntrega;
        setOpaque(false);

        JPanel kpis = FormularioWeb.grelha(150, 6, kRecolhas, kPorEntregar, kHoje, kAtraso, kEntregas, kFacturado);

        comercial.caixa.UiCaixa.tabela(tabela, new int[]{120, 220, 110, 60, 95, 120, 110}, ComponentesWeb.AZUL);
        tabela.getColumnModel().getColumn(5).setCellRenderer(new RendererSituacao());
        DefaultTableCellRenderer direita = new DefaultTableCellRenderer();
        direita.setHorizontalAlignment(SwingConstants.RIGHT);
        tabela.getColumnModel().getColumn(3).setCellRenderer(direita);
        tabela.getColumnModel().getColumn(6).setCellRenderer(direita);
        tabela.setToolTipText("Duplo clique abre a entrega desta recolha");
        tabela.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    entregarSeleccionada();
                }
            }
        });
        JScrollPane rolagem = new JScrollPane(tabela);
        rolagem.setBorder(javax.swing.BorderFactory.createLineBorder(ComponentesWeb.LINHA));
        rolagem.getViewport().setBackground(Color.WHITE);
        rolagem.setPreferredSize(new Dimension(500, 250));

        ButtonGroup g = new ButtonGroup();
        for (JToggleButton b : new JToggleButton[]{fTodas, fAtraso, fHoje}) {
            g.add(b);
            b.addActionListener(e -> mostrar());
        }
        JButton entregar = new JButton("Entregar seleccionada");
        ComponentesWeb.botao(entregar, ComponentesWeb.Variante.PRIMARIO, null, "Abrir a entrega desta recolha");
        entregar.addActionListener(e -> entregarSeleccionada());
        JButton actualizar = new JButton("Actualizar");
        ComponentesWeb.botao(actualizar, ComponentesWeb.Variante.SECUNDARIO, null, null);
        actualizar.addActionListener(e -> actualizar(true));

        JPanel topo = ComponentesWeb.transparente(new BorderLayout(10, 0));
        topo.add(FormularioWeb.pilulas(fTodas, fAtraso, fHoje), BorderLayout.WEST);
        topo.add(lbEstado, BorderLayout.CENTER);
        JPanel botoes = ComponentesWeb.transparente(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 8, 0));
        botoes.add(actualizar);
        botoes.add(entregar);
        topo.add(botoes, BorderLayout.EAST);

        JPanel corpo = ComponentesWeb.transparente(new BorderLayout(0, 8));
        corpo.add(topo, BorderLayout.NORTH);
        corpo.add(rolagem, BorderLayout.CENTER);

        add(kpis, BorderLayout.NORTH);
        add(FormularioWeb.cartao("Peças por entregar", ComponentesWeb.ROXO, corpo), BorderLayout.CENTER);
    }

    private void entregarSeleccionada() {
        int v = tabela.getSelectedRow();
        if (v < 0) {
            javax.swing.JOptionPane.showMessageDialog(this, "Seleccione uma recolha na lista.");
            return;
        }
        abrirEntrega.accept(String.valueOf(modelo.getValueAt(tabela.convertRowIndexToModel(v), 0)));
    }

    /** Lê os números em segundo plano. forcar = ignora o intervalo mínimo entre leituras (15 s). */
    public void actualizar(boolean forcar) {
        if (!forcar && System.currentTimeMillis() - ultimaLeitura < 15_000) {
            return;
        }
        ultimaLeitura = System.currentTimeMillis();
        lbEstado.setText("A actualizar...");
        new SwingWorker<Dados, Void>() {
            @Override
            protected Dados doInBackground() throws Exception {
                try (Connection cn = BDConexao.novaLigacao()) {
                    return ler(cn);
                }
            }

            @Override
            protected void done() {
                try {
                    Dados d = get();
                    kRecolhas.setValor(String.valueOf(d.recolhasHoje), d.pecasHoje + (d.pecasHoje == 1 ? " peça recebida" : " peças recebidas"));
                    kPorEntregar.setValor(String.valueOf(d.porEntregar), d.recolhasAbertas + (d.recolhasAbertas == 1 ? " recolha" : " recolhas"));
                    kHoje.setValor(String.valueOf(d.paraHoje), d.paraHoje == 0 ? "nada marcado para hoje" : "peças a sair hoje");
                    kAtraso.setValor(String.valueOf(d.emAtraso), d.emAtraso == 0 ? "tudo em dia" : "avisar os clientes");
                    kEntregas.setValor(String.valueOf(d.entregasHoje), "notas de entrega");
                    kFacturado.setValor(moeda(d.facturadoHoje), "recolhas (FR + FT)");
                    linhas.clear();
                    linhas.addAll(d.lista);
                    mostrar();
                } catch (Exception e) {
                    e.printStackTrace();
                    Throwable c = e.getCause() != null ? e.getCause() : e;
                    lbEstado.setText("Não foi possível ler: " + c.getMessage());
                    lbEstado.setForeground(ComponentesWeb.VERMELHO);
                }
            }
        }.execute();
    }

    private void mostrar() {
        modelo.setRowCount(0);
        int n = 0;
        for (Object[] l : linhas) {
            String s = (String) l[5];
            if (fAtraso.isSelected() && !s.startsWith("Atras") || fHoje.isSelected() && !s.equals("Hoje")) {
                continue;
            }
            modelo.addRow(l);
            n++;
        }
        lbEstado.setForeground(ComponentesWeb.TEXTO_SUAVE);
        lbEstado.setText(n == 0 ? "Nada por entregar aqui." : n + (n == 1 ? " recolha" : " recolhas") + " · duplo clique para entregar");
    }

    // =====================================================================
    // DADOS
    // =====================================================================
    static final class Dados {

        int recolhasHoje, pecasHoje, porEntregar, recolhasAbertas, paraHoje, emAtraso, entregasHoje;
        double facturadoHoje;
        final List<Object[]> lista = new ArrayList<>();
    }

    static Dados ler(Connection cn) throws Exception {
        Dados d = new Dados();
        String base = " FROM tb_item_venda i JOIN tb_venda v ON v.codigo = i.codigo_venda WHERE" + LAVANDARIA;
        try (Statement st = cn.createStatement()) {
            try (ResultSet r = st.executeQuery("SELECT COUNT(DISTINCT v.codigo), COUNT(*)" + base + " AND DATE(v.dataVenda) = CURDATE()")) {
                r.next();
                d.recolhasHoje = r.getInt(1);
                d.pecasHoje = r.getInt(2);
            }
            try (ResultSet r = st.executeQuery("SELECT COUNT(*), COUNT(DISTINCT v.codigo),"
                    + " COALESCE(SUM(DATE(i.data_entrega) < CURDATE()), 0), COALESCE(SUM(DATE(i.data_entrega) = CURDATE()), 0)"
                    + base + " AND i.status_entrega = 0")) {
                r.next();
                d.porEntregar = r.getInt(1);
                d.recolhasAbertas = r.getInt(2);
                d.emAtraso = r.getInt(3);
                d.paraHoje = r.getInt(4);
            }
            try (ResultSet r = st.executeQuery("SELECT COUNT(*) FROM tb_venda WHERE fk_documento = " + util.DVML.DOC_ENTREGA_LAVANDARIA
                    + " AND DATE(dataVenda) = CURDATE()")) {
                r.next();
                d.entregasHoje = r.getInt(1);
            }
            try (ResultSet r = st.executeQuery("SELECT COALESCE(SUM(v.total_venda), 0) FROM tb_venda v WHERE DATE(v.dataVenda) = CURDATE()"
                    + " AND v.fk_documento IN (1, 2) AND COALESCE(v.status_eliminado, 'false') <> 'true'"
                    + " AND EXISTS (SELECT 1 FROM tb_item_venda i WHERE i.codigo_venda = v.codigo AND i.data_entrega IS NOT NULL)")) {
                r.next();
                d.facturadoHoje = r.getDouble(1);
            }
            LocalDate hoje = LocalDate.now();
            try (ResultSet r = st.executeQuery("SELECT v.cod_fact, COALESCE(c.nome, ''), COALESCE(c.telefone, ''), COUNT(*),"
                    + " MIN(DATE(i.data_entrega)), SUM(i.total)" + " FROM tb_item_venda i JOIN tb_venda v ON v.codigo = i.codigo_venda"
                    + " LEFT JOIN tb_cliente c ON c.codigo = v.codigo_cliente WHERE" + LAVANDARIA + " AND i.status_entrega = 0"
                    + " GROUP BY v.codigo ORDER BY MIN(i.data_entrega), v.codigo LIMIT 300")) {
                while (r.next()) {
                    LocalDate data = r.getDate(5) == null ? null : r.getDate(5).toLocalDate();
                    d.lista.add(new Object[]{r.getString(1), r.getString(2), r.getString(3), r.getInt(4),
                        data == null ? "" : data.format(DATA), situacao(data, hoje), moeda(r.getDouble(6))});
                }
            }
        }
        return d;
    }

    static String situacao(LocalDate data, LocalDate hoje) {
        if (data == null) {
            return "Sem data";
        }
        long dias = ChronoUnit.DAYS.between(hoje, data);
        if (dias < 0) {
            return "Atrasada " + (-dias) + (dias == -1 ? " dia" : " dias");
        }
        return dias == 0 ? "Hoje" : dias == 1 ? "Amanhã" : "Em " + dias + " dias";
    }

    static String moeda(double v) {
        return new DecimalFormat("#,##0.00", DecimalFormatSymbols.getInstance(new Locale("pt", "PT"))).format(v);
    }

    /** Situação com cor: atrasada a vermelho, hoje a azul, amanhã a laranja. */
    private static final class RendererSituacao extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable t, Object v, boolean sel, boolean foco, int l, int c) {
            super.getTableCellRendererComponent(t, v, sel, foco, l, c);
            String s = v == null ? "" : v.toString();
            setFont(EstiloUI.FONTE_NEGRITO.deriveFont(12.5f));
            setHorizontalAlignment(SwingConstants.CENTER);
            if (!sel) {
                setForeground(s.startsWith("Atras") ? ComponentesWeb.VERMELHO : s.equals("Hoje") ? ComponentesWeb.AZUL_VIVO
                        : s.equals("Amanhã") ? new Color(194, 120, 3) : ComponentesWeb.VERDE.darker());
            }
            return this;
        }
    }
}
