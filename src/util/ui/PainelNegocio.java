package util.ui;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import javax.swing.Timer;

/**
 * Painel de indicadores do menu principal (comercial, restaurante, lavandaria,
 * farmácia, oficina): vendas de hoje (comparadas com ontem), talão médio, mês
 * corrente, contas de mesa em aberto (restaurante) ou documentos de hoje, o
 * gráfico dos últimos 7 dias e os produtos mais vendidos.
 *
 * <p>
 * Conta as vendas FR e FT não eliminadas, pelo valor pago (total_venda, com IVA) (como o histórico das mesas). Lê a
 * base numa thread à parte e actualiza-se sozinho a cada 2 minutos enquanto o
 * menu está aberto. 2026-11-17.
 */
public final class PainelNegocio extends JPanel {

    private static final Color SERIE = ComponentesWeb.AZUL_VIVO;
    private static final Color SERIE_CLARA = ComponentesWeb.mistura(ComponentesWeb.AZUL_VIVO, Color.WHITE, 0.55);
    private static final DecimalFormat KZ;
    private static final DecimalFormat INT = new DecimalFormat("#,##0");
    private static final String[] DIAS = {"Dom", "Seg", "Ter", "Qua", "Qui", "Sex", "Sáb"};

    static {
        DecimalFormatSymbols s = new DecimalFormatSymbols(new Locale("pt", "PT"));
        s.setGroupingSeparator('.');
        s.setDecimalSeparator(',');
        KZ = new DecimalFormat("#,##0.00", s);
        INT.setDecimalFormatSymbols(s);
    }

    private final boolean restaurante;
    private final ComponentesWeb.CartaoKpi kpiHoje;
    private final ComponentesWeb.CartaoKpi kpiMedio;
    private final ComponentesWeb.CartaoKpi kpiMes;
    private final ComponentesWeb.CartaoKpi kpiQuarto;
    private final GraficoDias grafico = new GraficoDias();
    private final TopProdutos top = new TopProdutos();
    private final ComponentesWeb.Cartao cartaoTop;

    /** @param restaurante mostra as contas de mesa em aberto no 4.º indicador */
    public PainelNegocio(boolean restaurante) {
        super(new BorderLayout(0, 14));
        this.restaurante = restaurante;
        setOpaque(false);
        setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 18, 0));

        kpiHoje = new ComponentesWeb.CartaoKpi("Kz", "Vendas de hoje", ComponentesWeb.VERDE).compacto();
        kpiMedio = new ComponentesWeb.CartaoKpi("Ø", "Talão médio de hoje", ComponentesWeb.AZUL_VIVO).compacto();
        kpiMes = new ComponentesWeb.CartaoKpi("#", "Vendas do mês", ComponentesWeb.ROXO).compacto();
        kpiQuarto = restaurante
                ? new ComponentesWeb.CartaoKpi(ComponentesWeb.CartaoKpi.PESSOA, "Contas de mesa abertas", ComponentesWeb.LARANJA).compacto()
                : new ComponentesWeb.CartaoKpi(ComponentesWeb.CartaoKpi.VISTO, "Documentos emitidos hoje", ComponentesWeb.CIANO).compacto();
        JPanel kpis = FormularioWeb.grelha(200, 4, kpiHoje, kpiMedio, kpiMes, kpiQuarto);
        add(kpis, BorderLayout.NORTH);

        ComponentesWeb.Cartao cartaoGrafico = new ComponentesWeb.Cartao("Vendas dos últimos 7 dias", SERIE, new BorderLayout());
        cartaoGrafico.getCorpo().add(grafico, BorderLayout.CENTER);
        cartaoTop = new ComponentesWeb.Cartao("Mais vendidos hoje", ComponentesWeb.VERDE, new BorderLayout());
        cartaoTop.getCorpo().add(top, BorderLayout.CENTER);
        JPanel graficos = ComponentesWeb.transparente(new GridLayout(1, 2, 14, 0));
        graficos.add(cartaoGrafico);
        graficos.add(cartaoTop);
        add(graficos, BorderLayout.CENTER);

        Timer t = new Timer(120_000, e -> {
            if (isShowing()) {
                actualizar();
            }
        });
        t.start();
        addHierarchyListener(e -> {
            if (!isDisplayable()) {
                t.stop();
            }
        });
        actualizar();
    }

    /** Os valores de vendas só aparecem a administradores (tipo de utilizador 1). */
    public static boolean podeVer(int idUsuario) {
        try (PreparedStatement ps = util.BDConexao.getConexao().prepareStatement("SELECT idTipoUsuario FROM tb_usuario WHERE codigo = ?")) {
            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) == 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // =====================================================================
    // DADOS
    // =====================================================================
    static final class Dados {

        double hoje, ontem, mes, abertoValor;
        int docsHoje, docsMes, abertas;
        final List<String> dias = new ArrayList<>();
        final List<Double> valores = new ArrayList<>();
        final List<String> produtos = new ArrayList<>();
        final List<Double> qtds = new ArrayList<>();
        final List<Double> totais = new ArrayList<>();
        boolean topSeteDias;
    }

    private static final String VENDA_VALIDA = " v.fk_documento IN (1, 2) AND (v.status_eliminado IS NULL OR v.status_eliminado = 'false') ";

    public void actualizar() {
        new SwingWorker<Dados, Void>() {
            @Override
            protected Dados doInBackground() throws Exception {
                return ler(restaurante);
            }

            @Override
            protected void done() {
                try {
                    mostrar(get());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }

    static Dados ler(boolean restaurante) throws Exception {
        Dados d = new Dados();
        Calendar c = Calendar.getInstance();
        zerar(c);
        Date hoje = c.getTime();
        c.add(Calendar.DAY_OF_MONTH, 1);
        Date amanha = c.getTime();
        c.add(Calendar.DAY_OF_MONTH, -2);
        Date ontem = c.getTime();
        c.setTime(hoje);
        c.set(Calendar.DAY_OF_MONTH, 1);
        Date inicioMes = c.getTime();
        c.setTime(hoje);
        c.add(Calendar.DAY_OF_MONTH, -6);
        Date seteDias = c.getTime();

        Connection cn = util.BDConexao.getConexao(); // partilhada: só leitura, não se fecha
        double[] r = soma(cn, hoje, amanha);
        d.hoje = r[0];
        d.docsHoje = (int) r[1];
        d.ontem = soma(cn, ontem, hoje)[0];
        r = soma(cn, inicioMes, amanha);
        d.mes = r[0];
        d.docsMes = (int) r[1];

        // últimos 7 dias (os dias sem vendas ficam a zero)
        double[] porDia = new double[7];
        try (PreparedStatement ps = cn.prepareStatement("SELECT DATE(v.dataVenda), COALESCE(SUM(v.total_venda), 0) FROM tb_venda v"
                + " WHERE" + VENDA_VALIDA + " AND v.dataVenda >= ? AND v.dataVenda < ? GROUP BY DATE(v.dataVenda)")) {
            ps.setTimestamp(1, new Timestamp(seteDias.getTime()));
            ps.setTimestamp(2, new Timestamp(amanha.getTime()));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    if (rs.getDate(1) == null) {
                        continue;
                    }
                    long dias = Math.round((rs.getDate(1).getTime() - seteDias.getTime()) / 86_400_000d);
                    if (dias >= 0 && dias < 7) {
                        porDia[(int) dias] += rs.getDouble(2);
                    }
                }
            }
        }
        c.setTime(seteDias);
        SimpleDateFormat dm = new SimpleDateFormat("dd/MM");
        for (int i = 0; i < 7; i++) {
            d.dias.add(i == 6 ? "Hoje" : DIAS[c.get(Calendar.DAY_OF_WEEK) - 1] + " " + dm.format(c.getTime()));
            d.valores.add(porDia[i]);
            c.add(Calendar.DAY_OF_MONTH, 1);
        }

        topProdutos(cn, d, hoje, amanha);
        if (d.produtos.size() < 3) { // pouco movimento hoje: mostra a semana
            d.produtos.clear();
            d.qtds.clear();
            d.totais.clear();
            d.topSeteDias = true;
            topProdutos(cn, d, seteDias, amanha);
        }

        if (restaurante) {
            try (PreparedStatement ps = cn.prepareStatement("SELECT COUNT(DISTINCT p.pk_pedido), COALESCE(SUM(i.total_item), 0)"
                    + " FROM tb_pedido p INNER JOIN tb_item_pedidos i ON i.fk_pedidos = p.pk_pedido WHERE p.status_pedido = 0");
                    ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    d.abertas = rs.getInt(1);
                    d.abertoValor = rs.getDouble(2);
                }
            }
        }
        return d;
    }

    private static double[] soma(Connection cn, Date de, Date ate) throws Exception {
        try (PreparedStatement ps = cn.prepareStatement("SELECT COALESCE(SUM(v.total_venda), 0), COUNT(*) FROM tb_venda v"
                + " WHERE" + VENDA_VALIDA + " AND v.dataVenda >= ? AND v.dataVenda < ?")) {
            ps.setTimestamp(1, new Timestamp(de.getTime()));
            ps.setTimestamp(2, new Timestamp(ate.getTime()));
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? new double[]{rs.getDouble(1), rs.getDouble(2)} : new double[2];
            }
        }
    }

    private static void topProdutos(Connection cn, Dados d, Date de, Date ate) throws Exception {
        try (PreparedStatement ps = cn.prepareStatement("SELECT COALESCE(NULLIF(TRIM(p.designacao), ''), iv.designacao_item, '?') AS nome,"
                + " SUM(iv.quantidade), SUM(iv.total) FROM tb_item_venda iv"
                + " INNER JOIN tb_venda v ON v.codigo = iv.codigo_venda"
                + " LEFT JOIN tb_produto p ON p.codigo = iv.codigo_produto"
                + " WHERE" + VENDA_VALIDA + " AND v.dataVenda >= ? AND v.dataVenda < ?"
                + " GROUP BY nome ORDER BY SUM(iv.total) DESC LIMIT 5")) {
            ps.setTimestamp(1, new Timestamp(de.getTime()));
            ps.setTimestamp(2, new Timestamp(ate.getTime()));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    d.produtos.add(rs.getString(1));
                    d.qtds.add(rs.getDouble(2));
                    d.totais.add(rs.getDouble(3));
                }
            }
        }
    }

    private static void zerar(Calendar c) {
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
    }

    void mostrar(Dados d) {
        kpiHoje.setValor(KZ.format(d.hoje) + " Kz", comparacao(d.hoje, d.ontem));
        kpiMedio.setValor(KZ.format(d.docsHoje == 0 ? 0 : d.hoje / d.docsHoje) + " Kz",
                d.docsHoje == 0 ? "Ainda sem vendas hoje" : d.docsHoje + (d.docsHoje == 1 ? " venda" : " vendas"));
        kpiMes.setValor(KZ.format(d.mes) + " Kz", INT.format(d.docsMes) + " documentos");
        if (restaurante) {
            kpiQuarto.setValor(String.valueOf(d.abertas),
                    d.abertas == 0 ? "Todas as mesas fechadas" : KZ.format(d.abertoValor) + " Kz por facturar");
        } else {
            kpiQuarto.setValor(String.valueOf(d.docsHoje), "FR e FT");
        }
        grafico.dados(d.dias, d.valores);
        cartaoTop.getTitulo().setText(d.topSeteDias ? "Mais vendidos · últimos 7 dias" : "Mais vendidos hoje");
        top.dados(d.produtos, d.qtds, d.totais);
    }

    private static String comparacao(double hoje, double ontem) {
        if (ontem <= 0) {
            return hoje > 0 ? "Ontem: sem vendas" : "Ainda sem vendas hoje";
        }
        double p = (hoje - ontem) / ontem * 100;
        return (p >= 0 ? "▲ " : "▼ ") + new DecimalFormat("0").format(Math.abs(p)) + "% face a ontem";
    }

    static String curto(double v) {
        if (v >= 1_000_000) {
            return new DecimalFormat("0.#", KZ.getDecimalFormatSymbols()).format(v / 1_000_000) + " M";
        }
        if (v >= 1_000) {
            return new DecimalFormat("0.#", KZ.getDecimalFormatSymbols()).format(v / 1_000) + " mil";
        }
        return new DecimalFormat("0").format(v);
    }

    // =====================================================================
    // GRÁFICO: barras verticais, uma série (hoje em destaque)
    // =====================================================================
    static final class GraficoDias extends JComponent {

        private List<String> dias = new ArrayList<>();
        private List<Double> valores = new ArrayList<>();
        private int sobre = -1;

        GraficoDias() {
            setPreferredSize(new Dimension(300, 190));
            setToolTipText("");
            addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
                @Override
                public void mouseMoved(MouseEvent e) {
                    int i = indice(e.getX());
                    if (i != sobre) {
                        sobre = i;
                        repaint();
                    }
                }
            });
            addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseExited(MouseEvent e) {
                    sobre = -1;
                    repaint();
                }
            });
        }

        void dados(List<String> d, List<Double> v) {
            dias = d;
            valores = v;
            repaint();
        }

        private int indice(int x) {
            if (dias.isEmpty()) {
                return -1;
            }
            Insets in = margens();
            double passo = (getWidth() - in.left - in.right) / (double) dias.size();
            int i = (int) ((x - in.left) / passo);
            return i >= 0 && i < dias.size() ? i : -1;
        }

        private Insets margens() {
            return new Insets(22, 6, 26, 6);
        }

        @Override
        public String getToolTipText(MouseEvent e) {
            int i = indice(e.getX());
            return i < 0 ? null : "<html><b>" + dias.get(i) + "</b><br>" + KZ.format(valores.get(i)) + " Kz</html>";
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = ComponentesWeb.suave(g);
            Insets in = margens();
            int w = getWidth() - in.left - in.right, h = getHeight() - in.top - in.bottom;
            int base = in.top + h;
            double max = 0;
            for (double v : valores) {
                max = Math.max(max, v);
            }
            // grelha recessiva: linha de base e uma a meio
            g2.setColor(ComponentesWeb.LINHA);
            g2.setStroke(new BasicStroke(1f));
            g2.drawLine(in.left, base, in.left + w, base);
            g2.setStroke(new BasicStroke(1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10f, new float[]{3f, 4f}, 0));
            g2.drawLine(in.left, in.top + h / 2, in.left + w, in.top + h / 2);
            if (dias.isEmpty()) {
                g2.dispose();
                return;
            }
            FontMetrics fmEixo = g2.getFontMetrics(EstiloUI.FONTE.deriveFont(11f));
            double passo = w / (double) dias.size();
            int largura = (int) Math.max(8, Math.min(46, passo * 0.56));
            int maior = -1;
            for (int i = 0; i < valores.size(); i++) {
                if (maior < 0 || valores.get(i) > valores.get(maior)) {
                    maior = i;
                }
            }
            for (int i = 0; i < dias.size(); i++) {
                double v = valores.get(i);
                int x = (int) (in.left + passo * i + (passo - largura) / 2);
                int alt = max <= 0 ? 0 : (int) Math.round(h * v / max);
                boolean hoje = i == dias.size() - 1;
                g2.setColor(hoje || i == sobre ? SERIE : SERIE_CLARA);
                if (alt > 0) {
                    int r = Math.min(8, Math.min(alt, largura) );
                    g2.fillRoundRect(x, base - alt, largura, alt, r, r);
                    g2.fillRect(x, base - Math.min(alt, r / 2 + 1), largura, Math.min(alt, r / 2 + 1)); // base recta
                }
                // eixo: dia
                g2.setFont(fmEixo.getFont());
                g2.setColor(hoje ? ComponentesWeb.TEXTO : ComponentesWeb.TEXTO_SUAVE);
                String d = dias.get(i);
                if (fmEixo.stringWidth(d) > passo - 2) {
                    d = d.split(" ")[0];
                }
                g2.drawString(d, (int) (in.left + passo * i + (passo - fmEixo.stringWidth(d)) / 2), base + 16);
                // rótulos selectivos: hoje, o maior e o que está debaixo do rato
                if (v > 0 && (hoje || i == maior || i == sobre)) {
                    String t = curto(v);
                    g2.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(11f));
                    FontMetrics fm = g2.getFontMetrics();
                    g2.setColor(ComponentesWeb.TEXTO);
                    g2.drawString(t, x + (largura - fm.stringWidth(t)) / 2, base - alt - 5);
                }
            }
            if (max <= 0) {
                g2.setFont(EstiloUI.FONTE.deriveFont(12.5f));
                g2.setColor(ComponentesWeb.TEXTO_SUAVE);
                String t = "Sem vendas nos últimos 7 dias";
                g2.drawString(t, in.left + (w - g2.getFontMetrics().stringWidth(t)) / 2, in.top + h / 2 - 6);
            }
            g2.dispose();
        }
    }

    // =====================================================================
    // MAIS VENDIDOS: barras horizontais com nome, quantidade e valor
    // =====================================================================
    static final class TopProdutos extends JComponent {

        private List<String> nomes = new ArrayList<>();
        private List<Double> qtds = new ArrayList<>();
        private List<Double> totais = new ArrayList<>();

        TopProdutos() {
            setPreferredSize(new Dimension(300, 190));
        }

        void dados(List<String> n, List<Double> q, List<Double> t) {
            nomes = n;
            qtds = q;
            totais = t;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = ComponentesWeb.suave(g);
            int w = getWidth() - 8;
            if (nomes.isEmpty()) {
                g2.setFont(EstiloUI.FONTE.deriveFont(12.5f));
                g2.setColor(ComponentesWeb.TEXTO_SUAVE);
                String t = "Ainda não há vendas para mostrar";
                g2.drawString(t, (getWidth() - g2.getFontMetrics().stringWidth(t)) / 2, getHeight() / 2);
                g2.dispose();
                return;
            }
            double max = 0;
            for (double v : totais) {
                max = Math.max(max, v);
            }
            int linha = Math.max(30, Math.min(38, (getHeight() - 6) / Math.max(5, nomes.size())));
            FontMetrics fmN = g2.getFontMetrics(EstiloUI.FONTE_NEGRITO.deriveFont(12.5f));
            FontMetrics fmV = g2.getFontMetrics(EstiloUI.FONTE.deriveFont(12f));
            for (int i = 0; i < nomes.size(); i++) {
                int y = 4 + i * linha;
                String valor = KZ.format(totais.get(i)) + " Kz";
                String qtd = new DecimalFormat("0.##", KZ.getDecimalFormatSymbols()).format(qtds.get(i)) + " un";
                String direita = qtd + "  ·  " + valor;
                int larguraDir = fmV.stringWidth(direita);
                // nome (cortado com "..." se não couber)
                g2.setFont(fmN.getFont());
                g2.setColor(ComponentesWeb.TEXTO);
                g2.drawString(MesasWeb.cortar((i + 1) + ".  " + nomes.get(i), fmN, w - larguraDir - 12), 2, y + fmN.getAscent());
                g2.setFont(fmV.getFont());
                g2.setColor(ComponentesWeb.TEXTO_SUAVE);
                g2.drawString(direita, w - larguraDir, y + fmN.getAscent());
                // barra fina, proporcional ao valor
                int by = y + fmN.getHeight() + 3;
                g2.setColor(ComponentesWeb.LINHA);
                g2.fillRoundRect(2, by, w - 2, 6, 6, 6);
                int bw = max <= 0 ? 0 : (int) Math.max(6, (w - 2) * totais.get(i) / max);
                g2.setColor(i == 0 ? ComponentesWeb.VERDE : ComponentesWeb.mistura(ComponentesWeb.VERDE, Color.WHITE, 0.35));
                g2.fillRoundRect(2, by, bw, 6, 6, 6);
            }
            g2.dispose();
        }
    }
}
