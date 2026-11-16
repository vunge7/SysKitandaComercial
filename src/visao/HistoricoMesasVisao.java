package visao;

import comercial.controller.MesasController;
import comercial.controller.MesasController.VendaMesa;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import kitanda.util.CfMethods;
import util.BDConexao;
import util.ui.ComponentesWeb;
import util.ui.EstiloUI;
import util.ui.FormularioWeb;
import util.ui.MesasWeb;

/**
 * Histórico do dia: quantas vezes cada mesa foi vendida (facturas FR/FT do
 * restaurante) e as facturas de cada mesa. Feito à mão, sem .form.
 */
public class HistoricoMesasVisao extends JDialog {

    private final MesasController mesasController;
    private final com.toedter.calendar.JDateChooser dcDia = new com.toedter.calendar.JDateChooser(new Date());
    private final ComponentesWeb.CartaoKpi kpiVendas = new ComponentesWeb.CartaoKpi(ComponentesWeb.CartaoKpi.VISTO, "Vendas no dia", ComponentesWeb.VERDE);
    private final ComponentesWeb.CartaoKpi kpiValor = new ComponentesWeb.CartaoKpi("Kz", "Valor vendido", ComponentesWeb.AZUL_MEDIO);
    private final ComponentesWeb.CartaoKpi kpiMesas = new ComponentesWeb.CartaoKpi("#", "Mesas com vendas", ComponentesWeb.ROXO);
    private final ComponentesWeb.CartaoKpi kpiTop = new ComponentesWeb.CartaoKpi("1", "Mesa mais vendida", MesasWeb.VENDIDA);
    private final DefaultTableModel modeloMesas = modelo("Mesa", "Vendas", "Valor", "Primeira", "Última");
    private final DefaultTableModel modeloVendas = modelo("Documento", "Hora", "Mesa", "Cliente", "Utilizador", "Total");
    private final JTable tbMesas = new JTable(modeloMesas);
    private final JTable tbVendas = new JTable(modeloVendas);
    private final ComponentesWeb.Cartao cartaoVendas;
    private List<VendaMesa> vendas = new ArrayList<>();
    private int maxVendas = 1;

    public HistoricoMesasVisao(Frame pai, BDConexao conexao, String mesaInicial) {
        super(pai, "Histórico de vendas por mesa", true);
        mesasController = new MesasController(conexao);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // ------------------------------------------------ cabeçalho
        FormularioWeb.data(dcDia);
        dcDia.setPreferredSize(new Dimension(150, 30));
        dcDia.setDateFormatString("dd/MM/yyyy");
        JButton btHoje = new JButton("Hoje");
        ComponentesWeb.botao(btHoje, ComponentesWeb.Variante.SECUNDARIO, null, "Voltar ao dia de hoje");
        btHoje.putClientProperty("web.pilula", Boolean.TRUE);
        btHoje.addActionListener(e -> dcDia.setDate(new Date()));
        JPanel direita = ComponentesWeb.transparente(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        direita.add(FormularioWeb.campoCabecalho(new JLabel("Dia"), dcDia));
        JPanel pHoje = ComponentesWeb.transparente(new BorderLayout());
        pHoje.add(btHoje, BorderLayout.SOUTH);
        direita.add(pHoje);
        JPanel cabecalho = FormularioWeb.cabecalho("Histórico de vendas por mesa",
                "Quantas vezes cada mesa foi vendida (facturas FR e FT válidas)", direita);

        // ------------------------------------------------ tabelas
        estilizar(tbMesas, new int[]{110, 140, 120, 70, 70});
        estilizar(tbVendas, new int[]{200, 50, 75, 170, 120, 125});
        tbVendas.setToolTipText("Passe o rato numa linha para ver o documento completo");
        tbVendas.getColumnModel().getColumn(0).setCellRenderer(new EstiloUI.ZebraRenderer(SwingConstants.LEFT) {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v, boolean s, boolean f, int r, int c) {
                Component comp = super.getTableCellRendererComponent(t, v, s, f, r, c);
                ((javax.swing.JComponent) comp).setToolTipText(v == null ? null : v.toString());
                return comp;
            }
        });
        tbMesas.getColumnModel().getColumn(1).setCellRenderer(new RendererBarra());
        tbMesas.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                mostrarVendas();
            }
        });
        JButton btTodas = new JButton("Ver todas");
        ComponentesWeb.botao(btTodas, ComponentesWeb.Variante.SECUNDARIO, null, "Mostrar as facturas de todas as mesas");
        btTodas.putClientProperty("web.pilula", Boolean.TRUE);
        btTodas.setBorder(BorderFactory.createEmptyBorder(4, 12, 4, 12));
        btTodas.addActionListener(e -> tbMesas.clearSelection());

        ComponentesWeb.Cartao cartaoMesas = FormularioWeb.cartao("Vendas por mesa", MesasWeb.VENDIDA, rolagem(tbMesas));
        cartaoVendas = FormularioWeb.cartao("Facturas do dia", ComponentesWeb.AZUL_VIVO, rolagem(tbVendas));
        cartaoVendas.adicionarAccao(btTodas);
        JPanel colunas = ComponentesWeb.transparente(new ComponentesWeb.ColunasResponsivas(
                new double[]{0.42, 0.58}, new int[]{380, 460}, 10));
        colunas.add(cartaoMesas);
        colunas.add(cartaoVendas);

        JPanel conteudo = ComponentesWeb.transparente(new BorderLayout(0, 8));
        conteudo.add(FormularioWeb.grelha(200, 4, kpiVendas, kpiValor, kpiMesas, kpiTop), BorderLayout.NORTH);
        conteudo.add(colunas, BorderLayout.CENTER);

        JButton btFechar = new JButton("Fechar");
        FormularioWeb.botaoSair(btFechar, "Fechar (Esc)");
        btFechar.addActionListener(e -> dispose());
        JLabel dica = FormularioWeb.dica(MesasWeb.VENDAS_AMARELO == 1
                ? "No plano de mesas, uma mesa livre fica amarela quando já teve vendas no dia."
                : "No plano de mesas, uma mesa livre fica amarela quando já foi vendida " + MesasWeb.VENDAS_AMARELO + " ou mais vezes no dia.");
        FormularioWeb.montar(this, cabecalho, conteudo, FormularioWeb.barraAccoes(dica, btFechar), "Histórico de vendas por mesa");
        getRootPane().registerKeyboardAction(e -> dispose(),
                javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0),
                javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW);

        dcDia.addPropertyChangeListener("date", e -> carregar(null));
        carregar(mesaInicial);
        FormularioWeb.ajustar(this, 1200, 720, 760, 520);
    }

    private static DefaultTableModel modelo(String... colunas) {
        return new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
    }

    private static void estilizar(JTable t, int[] larguras) {
        EstiloUI.estilizarTabela(t, true);
        t.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        t.setRowHeight(30);
        for (int i = 0; i < larguras.length; i++) {
            t.getColumnModel().getColumn(i).setPreferredWidth(larguras[i]);
        }
        EstiloUI.ZebraRenderer direita = new EstiloUI.ZebraRenderer(SwingConstants.RIGHT);
        int ultima = larguras.length - 1;
        t.getColumnModel().getColumn(ultima == 4 ? 2 : ultima).setCellRenderer(direita);
    }

    private static JScrollPane rolagem(JTable t) {
        JScrollPane sp = new JScrollPane(t);
        sp.setBorder(new javax.swing.border.LineBorder(ComponentesWeb.LINHA, 1, true));
        sp.getViewport().setBackground(Color.WHITE);
        sp.setPreferredSize(new Dimension(400, 300));
        return sp;
    }

    // =====================================================================
    // DADOS
    // =====================================================================
    private void carregar(String mesaEscolher) {
        Date dia = dcDia.getDate() == null ? new Date() : dcDia.getDate();
        try {
            vendas = mesasController.historicoVendas(dia);
        } catch (Exception e) {
            e.printStackTrace();
            vendas = new ArrayList<>();
        }
        // resumo por mesa (mais vendidas primeiro)
        Map<String, Object[]> porMesa = new LinkedHashMap<>();
        double totalDia = 0;
        java.util.Set<Integer> facturas = new java.util.HashSet<>();
        for (VendaMesa v : vendas) {
            Object[] r = porMesa.get(v.mesa);
            if (r == null) {
                r = new Object[]{v.mesa, 0, 0.0, v.dataHora, v.dataHora};
                porMesa.put(v.mesa, r);
            }
            r[1] = (Integer) r[1] + 1;
            r[2] = (Double) r[2] + v.total;
            r[4] = v.dataHora;
            if (facturas.add(v.codigoVenda)) {
                totalDia += v.total;
            }
        }
        List<Object[]> linhas = new ArrayList<>(porMesa.values());
        linhas.sort((a, b) -> (Integer) b[1] - (Integer) a[1]);
        maxVendas = 1;
        modeloMesas.setRowCount(0);
        SimpleDateFormat hora = new SimpleDateFormat("HH:mm");
        int linhaEscolher = -1;
        for (Object[] r : linhas) {
            maxVendas = Math.max(maxVendas, (Integer) r[1]);
            if (r[0].toString().equalsIgnoreCase(mesaEscolher)) {
                linhaEscolher = modeloMesas.getRowCount();
            }
            modeloMesas.addRow(new Object[]{r[0], r[1], CfMethods.formatarComoMoeda((Double) r[2]),
                r[3] == null ? "" : hora.format((Date) r[3]), r[4] == null ? "" : hora.format((Date) r[4])});
        }
        kpiVendas.setValor(String.valueOf(facturas.size()), new SimpleDateFormat("dd/MM/yyyy").format(dia));
        kpiValor.setValor(CfMethods.formatarComoMoeda(totalDia), "facturas FR e FT");
        kpiMesas.setValor(String.valueOf(linhas.size()), linhas.isEmpty() ? "nenhuma venda" : "mesas diferentes");
        if (linhas.isEmpty()) {
            kpiTop.setValor("—", "sem vendas neste dia");
        } else {
            Object[] top = linhas.get(0);
            kpiTop.setValor(top[0].toString(), top[1] + ((Integer) top[1] == 1 ? " venda" : " vendas"));
        }
        if (linhaEscolher >= 0) {
            tbMesas.setRowSelectionInterval(linhaEscolher, linhaEscolher);
        } else {
            tbMesas.clearSelection();
        }
        mostrarVendas();
    }

    private void mostrarVendas() {
        int linha = tbMesas.getSelectedRow();
        String mesa = linha < 0 ? null : tbMesas.getValueAt(linha, 0).toString();
        modeloVendas.setRowCount(0);
        SimpleDateFormat hora = new SimpleDateFormat("HH:mm");
        int n = 0;
        for (VendaMesa v : vendas) {
            if (mesa == null || mesa.equals(v.mesa)) {
                n++;
                modeloVendas.addRow(new Object[]{v.documento, v.dataHora == null ? "" : hora.format(v.dataHora), v.mesa,
                    v.cliente, v.utilizador, CfMethods.formatarComoMoeda(v.total)});
            }
        }
        cartaoVendas.getTitulo().setText(mesa == null
                ? "Facturas do dia · todas as mesas (" + n + ")"
                : "Facturas da " + mesa + " (" + n + ")");
    }

    /** Número de vendas com uma barra proporcional (a mesa mais vendida enche a barra). */
    private class RendererBarra extends DefaultTableCellRenderer {

        private int valor;
        private boolean sel;

        @Override
        public Component getTableCellRendererComponent(JTable t, Object v, boolean s, boolean f, int r, int c) {
            valor = v instanceof Integer ? (Integer) v : 0;
            sel = s;
            setText("");
            return this;
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = ComponentesWeb.suave(g);
            int w = getWidth(), h = getHeight();
            g2.setColor(sel ? EstiloUI.SELECCAO : Color.WHITE);
            g2.fillRect(0, 0, w, h);
            int area = w - 44;
            int bw = Math.max(6, (int) Math.round(area * (valor / (double) maxVendas)));
            Color cor = valor >= MesasWeb.VENDAS_AMARELO ? MesasWeb.VENDIDA : MesasWeb.LIVRE;
            g2.setColor(sel ? Color.WHITE : ComponentesWeb.alfa(cor, 60));
            g2.fillRoundRect(8, h / 2 - 6, area, 12, 12, 12);
            g2.setColor(cor);
            g2.fillRoundRect(8, h / 2 - 6, bw, 12, 12, 12);
            g2.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(13f));
            g2.setColor(sel ? Color.WHITE : ComponentesWeb.TEXTO);
            String t = String.valueOf(valor);
            g2.drawString(t, w - 8 - g2.getFontMetrics().stringWidth(t), h / 2 + 5);
            g2.dispose();
        }
    }
}
