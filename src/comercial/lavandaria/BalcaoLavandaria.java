package comercial.lavandaria;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.geom.Path2D;
import java.awt.geom.RoundRectangle2D;
import java.sql.Connection;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import util.BDConexao;
import util.ui.ComponentesWeb;
import util.ui.EstiloUI;
import util.ui.MenuWeb;

/**
 * Canto do balcão da recolha (coluna direita, em baixo): um pequeno painel do
 * dia (recolhas, facturado, por entregar, em atraso e a barra das peças por
 * entregar), o botão das Entregas (com o nº de peças para hoje / em atraso) e a
 * abertura / o fecho do caixa, como na venda (FormVendaResponsivaVisaoTop).
 *
 * Os números vêm de {@link PainelLavandaria#ler} numa ligação própria, em
 * segundo plano; o estado do caixa é o de MetodosUtil.verificarCaixa.
 */
public class BalcaoLavandaria extends JPanel {

    private final Window dono;
    private final int idUser;
    private final Runnable abrirEntregas;
    /** Chamado depois de ver o caixa: true = aberto (a recolha liga / desliga o "Cobrar"). */
    private final java.util.function.Consumer<Boolean> caixaMudou;

    private final Mosaico mRecolhas = new Mosaico("Recolhas", ComponentesWeb.AZUL_VIVO, MenuWeb.Simbolo.CABIDE);
    private final Mosaico mFacturado = new Mosaico("Facturado", ComponentesWeb.VERDE, MenuWeb.Simbolo.CARTEIRA);
    private final Mosaico mPorEntregar = new Mosaico("Por entregar", ComponentesWeb.ROXO, MenuWeb.Simbolo.CAMISA);
    private final Mosaico mAtraso = new Mosaico("Em atraso", ComponentesWeb.VERMELHO, MenuWeb.Simbolo.SINO);
    private final BarraPecas barra = new BarraPecas();
    private final BotaoEntregas btEntregas = new BotaoEntregas();
    private final JButton btAbrir = new JButton("Abertura Caixa");
    private final JButton btFechar = new JButton("Fecho Caixa");
    private final BotaoSair btSair = new BotaoSair();
    private final Operador operador = new Operador();
    /** Laranja escuro: o fecho do caixa não se confunde com o Sair (vermelho). */
    private static final Color COR_FECHO = new Color(234, 88, 12);
    private final EstadoCaixa estadoCaixa = new EstadoCaixa();
    private final JLabel lbActualizado = new JLabel(" ");
    private long ultimaLeitura;
    private boolean aLer;

    /**
     * @param utilizador nome do utilizador com sessão (aparece no balcão e no botão Sair)
     * @param sair       o que o botão Sair faz (fechar a recolha)
     */
    public BalcaoLavandaria(Window dono, int idUser, String utilizador, Runnable abrirEntregas, Runnable sair,
            java.util.function.Consumer<Boolean> caixaMudou) {
        super(new BorderLayout());
        this.dono = dono;
        this.idUser = idUser;
        this.abrirEntregas = abrirEntregas;
        this.caixaMudou = caixaMudou;
        setOpaque(false);

        // ---- painel do dia: 4 mosaicos (numa linha se houver largura, senão 2 x 2) e a barra das peças por entregar
        mosaicos.add(mRecolhas);
        mosaicos.add(mFacturado);
        mosaicos.add(mPorEntregar);
        mosaicos.add(mAtraso);
        mAtraso.setToolTipText("Peças com a data de entrega já passada: avisar os clientes");
        mPorEntregar.setToolTipText("Peças recolhidas que ainda não foram entregues");

        lbActualizado.setFont(EstiloUI.FONTE.deriveFont(11f));
        lbActualizado.setForeground(ComponentesWeb.TEXTO_SUAVE);
        JButton btActualizar = new JButton();
        ComponentesWeb.botao(btActualizar, ComponentesWeb.Variante.SECUNDARIO, null, "Actualizar os números");
        btActualizar.setIcon(new IconeActualizar());
        btActualizar.putClientProperty("web.pilula", Boolean.TRUE);
        btActualizar.setBorder(BorderFactory.createEmptyBorder(3, 6, 3, 6));
        btActualizar.addActionListener(e -> actualizar(true));
        accoesCartao.add(estadoCaixa);
        accoesCartao.add(lbActualizado);
        accoesCartao.add(btActualizar);

        dia = ComponentesWeb.transparente(new BorderLayout(0, 6));
        dia.add(mosaicos, BorderLayout.CENTER);
        dia.add(barra, BorderLayout.SOUTH);

        // ---- entregas
        btEntregas.addActionListener(e -> {
            if (this.abrirEntregas != null) {
                this.abrirEntregas.run();
            }
        });

        // ---- caixa: estado e os dois botões (só um fica ligado de cada vez)
        botaoCaixa(btAbrir, MenuWeb.Simbolo.CAIXA_ABRIR, ComponentesWeb.VERDE, "Abertura de caixa (fundo de maneio)");
        botaoCaixa(btFechar, MenuWeb.Simbolo.CAIXA_FECHAR, COR_FECHO, "Fecho de caixa: contar e fechar o dia");
        // ---- utilizador e Sair (grande, vermelho: não se confunde com o fecho do caixa)
        operador.nome = utilizador == null ? "" : utilizador.trim();
        btSair.nome = operador.nome;
        btSair.addActionListener(e -> {
            if (sair != null) {
                sair.run();
            }
        });
        btAbrir.addActionListener(e -> {
            if (comercial.caixa.AberturaCaixaWeb.abrir(this.dono, this.idUser, false)) {
                verificarCaixa();
                actualizar(true);
            }
        });
        btFechar.addActionListener(e -> {
            comercial.caixa.FechoCaixaWeb.mostrar(this.dono, this.idUser, "Lavandaria", false);
            verificarCaixa();
            actualizar(true);
        });
        montar(false);

        // de minuto a minuto, enquanto a janela estiver à vista (a leitura é leve)
        Timer t = new Timer(60_000, e -> {
            if (isShowing()) {
                actualizar(false);
            }
        });
        t.start();
        addHierarchyListener(e -> {
            if ((e.getChangeFlags() & java.awt.event.HierarchyEvent.DISPLAYABILITY_CHANGED) != 0 && !isDisplayable()) {
                t.stop();
            }
        });
    }

    private JPanel dia;
    private Boolean compacto;
    private final JPanel mosaicos = ComponentesWeb.transparente(new QuatroOuDois());
    private final JPanel accoesCartao = ComponentesWeb.transparente(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 6, 0));

    /**
     * Completo: cartão "Balcão" com o painel do dia, as Entregas e o caixa.
     * Compacto (pouca altura, ex.: 1366 x 768): só uma faixa com as Entregas e
     * os botões Abrir / Fechar caixa, para nunca ficarem escondidos.
     */
    public void setCompacto(boolean c) {
        if (compacto == null || compacto != c) {
            montar(c);
        }
    }

    public boolean isCompacto() {
        return Boolean.TRUE.equals(compacto);
    }

    /**
     * Os quatro botões grandes, 2 x 2: Entregas | Sair e Abertura Caixa | Fecho Caixa.
     * Iguais nos dois modos; o completo junta por cima o utilizador e o painel do dia.
     */
    private void montar(boolean c) {
        compacto = c;
        removeAll();
        int alt = c ? 42 : 50; // compacto: os 4 botões cabem por baixo da peça a 1366 x 768
        btEntregas.compacto = true;
        for (JComponent b : new JComponent[]{btEntregas, btSair, btAbrir, btFechar}) {
            b.setPreferredSize(new Dimension(150, alt));
        }
        JPanel botoes = ComponentesWeb.transparente(new GridLayout(2, 2, 8, c ? 6 : 8));
        botoes.add(btEntregas);
        botoes.add(btSair);
        botoes.add(btAbrir);
        botoes.add(btFechar);
        if (c) {
            add(botoes, BorderLayout.CENTER);
        } else {
            JPanel corpo = ComponentesWeb.transparente(new BorderLayout(0, 10));
            corpo.add(operador, BorderLayout.NORTH);
            corpo.add(dia, BorderLayout.CENTER);
            corpo.add(botoes, BorderLayout.SOUTH);
            ComponentesWeb.Cartao cartao = new ComponentesWeb.Cartao("Hoje no balcão", ComponentesWeb.LARANJA, new BorderLayout());
            cartao.adicionarAccao(accoesCartao);
            cartao.getCorpo().add(corpo, BorderLayout.CENTER);
            add(cartao, BorderLayout.CENTER);
        }
        revalidate();
        repaint();
    }

    /** Altura do modo completo numa coluna desta largura (para a coluna decidir se cabe). */
    public int alturaCompleta(int largura) {
        int base = baseCompleta > 0 ? baseCompleta : 290;
        return base + QuatroOuDois.altura(largura - 34);
    }

    /** Altura do modo completo sem os mosaicos (cartão, barra, entregas e caixa), medida quando está montado. */
    private int baseCompleta;

    @Override
    public void doLayout() {
        super.doLayout();
        if (!isCompacto() && mosaicos.getWidth() > 0) {
            baseCompleta = getPreferredSize().height - mosaicos.getPreferredSize().height;
        }
    }

    /** Os quatro mosaicos: numa linha a partir de 440 px, senão 2 x 2. */
    private static final class QuatroOuDois implements java.awt.LayoutManager {

        static final int ALTURA = 58, ESPACO = 8;

        static int colunas(int largura) {
            return largura >= 440 ? 4 : 2;
        }

        static int altura(int largura) {
            int linhas = 4 / colunas(largura);
            return linhas * ALTURA + (linhas - 1) * ESPACO;
        }

        @Override
        public void addLayoutComponent(String n, java.awt.Component c) {
        }

        @Override
        public void removeLayoutComponent(java.awt.Component c) {
        }

        @Override
        public Dimension preferredLayoutSize(java.awt.Container p) {
            int w = p.getWidth() > 0 ? p.getWidth() : 300;
            return new Dimension(240, altura(w));
        }

        @Override
        public Dimension minimumLayoutSize(java.awt.Container p) {
            return preferredLayoutSize(p);
        }

        @Override
        public void layoutContainer(java.awt.Container p) {
            int w = p.getWidth(), n = p.getComponentCount();
            int cols = colunas(w);
            int cw = (w - (cols - 1) * ESPACO) / cols;
            for (int i = 0; i < n; i++) {
                int r = i / cols, c = i % cols;
                int x = c == cols - 1 ? w - cw : c * (cw + ESPACO);
                p.getComponent(i).setBounds(x, r * (ALTURA + ESPACO), cw, ALTURA);
            }
        }
    }

    // =====================================================================
    // ESTADO
    // =====================================================================
    /** Liga / desliga a abertura e o fecho conforme o caixa do utilizador e avisa a recolha. */
    public void verificarCaixa() {
        boolean aberto;
        try {
            comercial.controller.CaixasController cc = new comercial.controller.CaixasController(BDConexao.getInstancia());
            util.MetodosUtil.verificarCaixa(cc, idUser, btAbrir, btFechar);
            aberto = btFechar.isEnabled();
        } catch (Exception ex) {
            System.err.println("[BalcaoLavandaria] caixa: " + ex.getMessage());
            return;
        }
        estadoCaixa.setAberto(aberto);
        if (caixaMudou != null) {
            caixaMudou.accept(aberto);
        }
    }

    /** Lê os números em segundo plano. forcar = ignora o intervalo mínimo (20 s). */
    public void actualizar(boolean forcar) {
        if (aLer || !forcar && System.currentTimeMillis() - ultimaLeitura < 20_000) {
            return;
        }
        aLer = true;
        ultimaLeitura = System.currentTimeMillis();
        new SwingWorker<PainelLavandaria.Dados, Void>() {
            @Override
            protected PainelLavandaria.Dados doInBackground() throws Exception {
                try (Connection cn = BDConexao.novaLigacao()) {
                    return PainelLavandaria.ler(cn);
                }
            }

            @Override
            protected void done() {
                aLer = false;
                try {
                    PainelLavandaria.Dados d = get();
                    mRecolhas.set(String.valueOf(d.recolhasHoje), d.pecasHoje + (d.pecasHoje == 1 ? " peça" : " peças"));
                    mFacturado.set(compacto(d.facturadoHoje), "Kz");
                    mPorEntregar.set(String.valueOf(d.porEntregar), d.recolhasAbertas + (d.recolhasAbertas == 1 ? " recolha" : " recolhas"));
                    mAtraso.set(String.valueOf(d.emAtraso), d.emAtraso == 0 ? "tudo em dia" : "avisar clientes");
                    mAtraso.alerta = d.emAtraso > 0;
                    mAtraso.repaint();
                    barra.set(d.emAtraso, d.paraHoje, Math.max(0, d.porEntregar - d.emAtraso - d.paraHoje));
                    btEntregas.set(d.paraHoje + d.emAtraso, d.entregasHoje);
                    lbActualizado.setText(new java.text.SimpleDateFormat("HH:mm").format(new java.util.Date()));
                    lbActualizado.setToolTipText("Actualizado às " + lbActualizado.getText());
                } catch (Exception e) {
                    Throwable c = e.getCause() != null ? e.getCause() : e;
                    System.err.println("[BalcaoLavandaria] " + c.getMessage());
                    lbActualizado.setText("sem dados");
                    lbActualizado.setToolTipText(c.getMessage());
                }
            }
        }.execute();
    }

    /** 1 250 000 -> "1,25 M"; 18 500 -> "18,5 mil"; abaixo de 10 mil, o valor inteiro. */
    static String compacto(double v) {
        java.text.DecimalFormatSymbols s = java.text.DecimalFormatSymbols.getInstance(new java.util.Locale("pt", "PT"));
        if (v >= 1_000_000) {
            return new java.text.DecimalFormat("#,##0.##", s).format(v / 1_000_000) + " M";
        }
        if (v >= 10_000) {
            return new java.text.DecimalFormat("#,##0.#", s).format(v / 1_000) + " mil";
        }
        return new java.text.DecimalFormat("#,##0", s).format(v);
    }

    private static void botaoCaixa(JButton b, MenuWeb.Simbolo simbolo, Color cor, String dica) {
        b.setUI(new BotaoCaixaUI(simbolo, cor));
        b.setOpaque(false);
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setRolloverEnabled(true);
        b.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(13f));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setToolTipText(dica);
    }

    // =====================================================================
    // PEÇAS DESENHADAS
    // =====================================================================
    /** Mosaico pequeno: símbolo num círculo, número grande e legenda. */
    private static final class Mosaico extends JComponent {

        private final String legenda;
        private final Color cor;
        private final MenuWeb.Simbolo simbolo;
        private String valor = "—", detalhe = " ";
        boolean alerta;

        Mosaico(String legenda, Color cor, MenuWeb.Simbolo simbolo) {
            this.legenda = legenda;
            this.cor = cor;
            this.simbolo = simbolo;
            setPreferredSize(new Dimension(120, 58));
            setMinimumSize(new Dimension(90, 58));
        }

        void set(String v, String d) {
            valor = v;
            detalhe = d;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = ComponentesWeb.suave(g);
            int w = getWidth(), h = getHeight();
            boolean vivo = alerta;
            g2.setColor(vivo ? ComponentesWeb.mistura(cor, Color.WHITE, 0.86) : ComponentesWeb.mistura(cor, Color.WHITE, 0.94));
            g2.fill(new RoundRectangle2D.Double(0, 0, w - 1, h - 1, 14, 14));
            g2.setColor(ComponentesWeb.mistura(cor, Color.WHITE, vivo ? 0.55 : 0.78));
            g2.draw(new RoundRectangle2D.Double(0.5, 0.5, w - 2, h - 2, 14, 14));
            // faixa da cor à esquerda
            g2.setColor(cor);
            g2.fill(new RoundRectangle2D.Double(0, 10, 4, h - 20, 4, 4));
            // símbolo
            int d = 22;
            g2.setColor(ComponentesWeb.mistura(cor, Color.WHITE, 0.75));
            g2.fillOval(w - d - 7, 7, d, d);
            MenuWeb.desenharSimbolo(g2, simbolo, w - d - 7 + 4, 7 + 4, d - 8, cor.darker());
            // textos
            g2.setFont(EstiloUI.FONTE.deriveFont(11.5f));
            g2.setColor(ComponentesWeb.TEXTO_SUAVE);
            g2.drawString(cortar(g2, legenda, w - d - 22), 12, 19);
            g2.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(20f));
            g2.setColor(vivo ? cor.darker() : ComponentesWeb.TEXTO);
            FontMetrics f = g2.getFontMetrics();
            String v = cortar(g2, valor, w - 20);
            g2.drawString(v, 12, 44);
            g2.setFont(EstiloUI.FONTE.deriveFont(11f));
            g2.setColor(ComponentesWeb.TEXTO_SUAVE);
            int xd = 12 + f.stringWidth(v) + 6;
            if (xd < w - 30) {
                g2.drawString(cortar(g2, detalhe, w - xd - 8), xd, 44);
            }
            g2.dispose();
        }
    }

    /** Barra das peças por entregar: em atraso (vermelho), hoje (azul), próximos dias (verde). */
    private static final class BarraPecas extends JComponent {

        private int atraso, hoje, depois;

        BarraPecas() {
            setPreferredSize(new Dimension(200, 32));
            setToolTipText("Peças por entregar: em atraso · para hoje · próximos dias");
        }

        void set(int atraso, int hoje, int depois) {
            this.atraso = atraso;
            this.hoje = hoje;
            this.depois = depois;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = ComponentesWeb.suave(g);
            int w = getWidth();
            int total = atraso + hoje + depois;
            int y = 4, a = 10;
            g2.setColor(new Color(226, 232, 240));
            g2.fill(new RoundRectangle2D.Double(0, y, w - 1, a, a, a));
            if (total > 0) {
                java.awt.Shape clip = g2.getClip();
                g2.clip(new RoundRectangle2D.Double(0, y, w - 1, a, a, a));
                int x = 0;
                int[] v = {atraso, hoje, depois};
                Color[] c = {ComponentesWeb.VERMELHO, ComponentesWeb.AZUL_VIVO, ComponentesWeb.VERDE};
                for (int i = 0; i < 3; i++) {
                    int lw = i == 2 ? w - 1 - x : (int) Math.round((w - 1) * (double) v[i] / total);
                    if (v[i] > 0) {
                        g2.setColor(c[i]);
                        g2.fillRect(x, y, Math.max(lw, 3), a);
                    }
                    x += lw;
                }
                g2.setClip(clip);
            }
            // legenda
            g2.setFont(EstiloUI.FONTE.deriveFont(11f));
            FontMetrics f = g2.getFontMetrics();
            int x = 0, base = y + a + 4 + f.getAscent();
            String[] t = {atraso + " em atraso", hoje + " para hoje", depois + " depois"};
            Color[] c = {ComponentesWeb.VERMELHO, ComponentesWeb.AZUL_VIVO, ComponentesWeb.VERDE};
            for (int i = 0; i < 3; i++) {
                if (x + 12 + f.stringWidth(t[i]) > w) {
                    break;
                }
                g2.setColor(c[i]);
                g2.fillOval(x, base - 8, 8, 8);
                g2.setColor(ComponentesWeb.TEXTO_SUAVE);
                g2.drawString(t[i], x + 12, base);
                x += 12 + f.stringWidth(t[i]) + 14;
            }
            g2.dispose();
        }
    }

    /**
     * Botão largo das Entregas: gradiente roxo → ciano, ícone próprio (saco de
     * lavandaria com cabide e visto) e a bolha com as peças para hoje / em atraso.
     */
    private static final class BotaoEntregas extends JButton {

        private int pendentes, feitas;
        boolean compacto;

        BotaoEntregas() {
            super("Entregas");
            setOpaque(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setRolloverEnabled(true);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setToolTipText("Entregar peças ao cliente (nota de entrega)");
            setPreferredSize(new Dimension(220, 62));
            setMinimumSize(new Dimension(160, 62));
        }

        void set(int pendentes, int feitas) {
            this.pendentes = pendentes;
            this.feitas = feitas;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = ComponentesWeb.suave(g);
            int w = getWidth(), h = getHeight();
            boolean rato = getModel().isRollover(), carregado = getModel().isArmed() && getModel().isPressed();
            Color c1 = new Color(124, 58, 237), c2 = new Color(8, 145, 178);
            if (rato) {
                c1 = c1.brighter();
                c2 = c2.brighter();
            }
            int dy = carregado ? 1 : 0;
            // sombra
            g2.setColor(new Color(15, 23, 42, 28));
            g2.fill(new RoundRectangle2D.Double(1, 3, w - 2, h - 3, 16, 16));
            g2.setPaint(new GradientPaint(0, 0, c1, w, h, c2));
            g2.fill(new RoundRectangle2D.Double(0, dy, w - 1, h - 4, 16, 16));
            // bolhas decorativas
            java.awt.Shape clip = g2.getClip();
            g2.clip(new RoundRectangle2D.Double(0, dy, w - 1, h - 4, 16, 16));
            g2.setColor(new Color(255, 255, 255, 22));
            g2.fillOval(w - 70, -24, 80, 80);
            g2.fillOval(w - 120, h - 26, 46, 46);
            g2.setClip(clip);
            // ícone num círculo branco
            int d = Math.min(40, h - 14), ix = compacto ? 8 : 12, iy = (h - 4 - d) / 2 + dy;
            g2.setColor(Color.WHITE);
            g2.fillOval(ix, iy, d, d);
            desenharSacoEntrega(g2, ix + d * 0.15, iy + d * 0.12, d * 0.7, c1);
            // textos
            g2.setColor(Color.WHITE);
            g2.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(16f));
            int tx = ix + d + 12;
            if (compacto) {
                FontMetrics ft = g2.getFontMetrics();
                g2.drawString(cortar(g2, "Entregas", w - tx - 40), tx, (h - 4 + ft.getAscent() - ft.getDescent()) / 2 + dy);
            } else {
                g2.drawString("Entregas", tx, iy + 17);
                g2.setFont(EstiloUI.FONTE.deriveFont(11.5f));
                g2.setColor(new Color(255, 255, 255, 215));
                String sub = feitas > 0 ? feitas + (feitas == 1 ? " entrega feita hoje" : " entregas feitas hoje") : "Entregar ao cliente";
                g2.drawString(cortar(g2, sub, w - tx - 50), tx, iy + 34);
            }
            // bolha com as pendentes (hoje + atraso)
            if (pendentes > 0) {
                String n = pendentes > 99 ? "99+" : String.valueOf(pendentes);
                g2.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(12.5f));
                FontMetrics f = g2.getFontMetrics();
                int bw = Math.max(26, f.stringWidth(n) + 14), bh = 24;
                int bx = w - bw - 12, by = (h - 4 - bh) / 2 + dy;
                g2.setColor(new Color(251, 191, 36));
                g2.fillRoundRect(bx, by, bw, bh, bh, bh);
                g2.setColor(new Color(69, 26, 3));
                g2.drawString(n, bx + (bw - f.stringWidth(n)) / 2, by + (bh + f.getAscent() - f.getDescent()) / 2);
            } else {
                // seta
                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(2.4f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                int ax = w - 26, ay = (h - 4) / 2 + dy;
                g2.drawLine(ax - 6, ay - 7, ax + 1, ay);
                g2.drawLine(ax + 1, ay, ax - 6, ay + 7);
            }
            g2.dispose();
        }

        @Override
        public String getToolTipText(java.awt.event.MouseEvent e) {
            return pendentes > 0 ? pendentes + (pendentes == 1 ? " peça" : " peças") + " para hoje ou em atraso — abrir as entregas"
                    : super.getToolTipText(e);
        }
    }

    /** Iniciais do nome (ex.: "DVML COMERCIAL" -> "DC"). */
    static String iniciais(String nome) {
        StringBuilder b = new StringBuilder();
        for (String p : nome.trim().split("\\s+")) {
            if (!p.isEmpty() && b.length() < 2) {
                b.append(Character.toUpperCase(p.charAt(0)));
            }
        }
        return b.length() == 0 ? "?" : b.toString();
    }

    /** Avatar com as iniciais e o nome do utilizador com sessão aberta. */
    private static final class Operador extends JComponent {

        String nome = "";

        Operador() {
            setPreferredSize(new Dimension(200, 36));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = ComponentesWeb.suave(g);
            int h = getHeight(), d = 32, y = (h - d) / 2;
            g2.setPaint(new GradientPaint(0, y, ComponentesWeb.AZUL_VIVO, d, y + d, new Color(124, 58, 237)));
            g2.fillOval(0, y, d, d);
            g2.setColor(Color.WHITE);
            g2.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(12.5f));
            FontMetrics f = g2.getFontMetrics();
            String ini = iniciais(nome);
            g2.drawString(ini, (d - f.stringWidth(ini)) / 2, y + (d + f.getAscent() - f.getDescent()) / 2);
            g2.setFont(EstiloUI.FONTE.deriveFont(11f));
            g2.setColor(ComponentesWeb.TEXTO_SUAVE);
            g2.drawString("Utilizador em sessão", d + 10, y + 12);
            g2.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(13.5f));
            g2.setColor(ComponentesWeb.TEXTO);
            g2.drawString(cortar(g2, nome.isEmpty() ? "—" : nome, getWidth() - d - 14), d + 10, y + 28);
            g2.dispose();
        }
    }

    /** Sair: vermelho cheio, ícone de porta com seta e o nome do utilizador por baixo. */
    private static final class BotaoSair extends JButton {

        String nome = "";

        BotaoSair() {
            super("Sair");
            setOpaque(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setRolloverEnabled(true);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setToolTipText("Sair da recolha (o caixa continua aberto)");
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = ComponentesWeb.suave(g);
            int w = getWidth(), h = getHeight();
            boolean rato = getModel().isRollover(), carregado = getModel().isArmed() && getModel().isPressed();
            Color c1 = rato ? new Color(239, 68, 68) : new Color(220, 38, 38), c2 = rato ? new Color(220, 38, 38) : new Color(185, 28, 28);
            int dy = carregado ? 1 : 0;
            g2.setColor(new Color(15, 23, 42, 28));
            g2.fill(new RoundRectangle2D.Double(1, 3, w - 2, h - 3, 14, 14));
            g2.setPaint(new GradientPaint(0, 0, c1, 0, h, c2));
            g2.fill(new RoundRectangle2D.Double(0, dy, w - 1, h - 4, 14, 14));
            // porta com seta para fora
            int s = 20, ix = 14, iy = (h - 4 - s) / 2 + dy;
            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.drawLine(ix + 9, iy + 2, ix + 2, iy + 2);
            g2.drawLine(ix + 2, iy + 2, ix + 2, iy + s - 2);
            g2.drawLine(ix + 2, iy + s - 2, ix + 9, iy + s - 2);
            g2.drawLine(ix + 7, iy + s / 2, ix + s + 1, iy + s / 2);
            g2.drawLine(ix + s - 3, iy + s / 2 - 4, ix + s + 1, iy + s / 2);
            g2.drawLine(ix + s - 3, iy + s / 2 + 4, ix + s + 1, iy + s / 2);
            int tx = ix + s + 12;
            g2.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(16f));
            FontMetrics f = g2.getFontMetrics();
            boolean comNome = !nome.isEmpty() && h >= 40;
            if (comNome) {
                g2.drawString("Sair", tx, (h - 4) / 2 + dy - 1);
                g2.setFont(EstiloUI.FONTE.deriveFont(11f));
                g2.setColor(new Color(255, 255, 255, 215));
                g2.drawString(cortar(g2, nome, w - tx - 8), tx, (h - 4) / 2 + dy + 13);
            } else {
                g2.drawString("Sair", tx, (h - 4 + f.getAscent() - f.getDescent()) / 2 + dy);
            }
            g2.dispose();
        }
    }

    /** Saco de lavandaria com o gancho do cabide e um visto (peça pronta a entregar). */
    static void desenharSacoEntrega(Graphics2D origem, double x, double y, double t, Color cor) {
        Graphics2D g = (Graphics2D) origem.create();
        g.translate(x, y);
        g.scale(t / 28.0, t / 28.0);
        g.setColor(cor);
        g.setStroke(new BasicStroke(2.1f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        // gancho do cabide
        Path2D gancho = new Path2D.Double();
        gancho.moveTo(14, 9);
        gancho.lineTo(14, 6.5);
        gancho.curveTo(14, 4.5, 17, 4.5, 17, 2.6);
        gancho.curveTo(17, 1, 15.7, 0, 14.3, 0);
        gancho.curveTo(12.9, 0, 11.8, 1, 11.8, 2.2);
        g.draw(gancho);
        // ombros do cabide
        g.drawLine(14, 9, 4, 13);
        g.drawLine(14, 9, 24, 13);
        // saco
        Path2D saco = new Path2D.Double();
        saco.moveTo(5, 13);
        saco.lineTo(23, 13);
        saco.lineTo(25, 27);
        saco.quadTo(25, 28, 24, 28);
        saco.lineTo(4, 28);
        saco.quadTo(3, 28, 3, 27);
        saco.closePath();
        g.setColor(ComponentesWeb.mistura(cor, Color.WHITE, 0.82));
        g.fill(saco);
        g.setColor(cor);
        g.draw(saco);
        // visto
        g.setStroke(new BasicStroke(2.4f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        Path2D visto = new Path2D.Double();
        visto.moveTo(9, 20.5);
        visto.lineTo(12.5, 24);
        visto.lineTo(19.5, 17);
        g.draw(visto);
        g.dispose();
    }

    /** Estado do caixa em pílula: ponto verde "Caixa aberto" / vermelho "Caixa fechado". */
    private static final class EstadoCaixa extends JComponent {

        private Boolean aberto;

        EstadoCaixa() {
            setPreferredSize(new Dimension(134, 22));
        }

        void setAberto(boolean a) {
            aberto = a;
            setToolTipText(a ? "O seu caixa está aberto: pode cobrar" : "Abra o caixa para poder cobrar");
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = ComponentesWeb.suave(g);
            int h = getHeight();
            g2.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(12f));
            FontMetrics f = g2.getFontMetrics();
            String rot = "CAIXA";
            g2.setColor(ComponentesWeb.TEXTO_SUAVE);
            g2.drawString(rot, 0, (h + f.getAscent() - f.getDescent()) / 2);
            int x = f.stringWidth(rot) + 10;
            String t = aberto == null ? "a verificar..." : aberto ? "Aberto" : "Fechado";
            Color c = aberto == null ? ComponentesWeb.TEXTO_SUAVE : aberto ? ComponentesWeb.VERDE : ComponentesWeb.VERMELHO;
            int pw = f.stringWidth(t) + 30, ph = h - 2;
            g2.setColor(ComponentesWeb.mistura(c, Color.WHITE, 0.88));
            g2.fillRoundRect(x, 1, pw, ph, ph, ph);
            g2.setColor(c);
            g2.fillOval(x + 9, (h - 8) / 2, 8, 8);
            if (Boolean.TRUE.equals(aberto)) { // halo
                g2.setColor(ComponentesWeb.mistura(c, Color.WHITE, 0.6));
                g2.setStroke(new BasicStroke(1.4f));
                g2.drawOval(x + 6, (h - 14) / 2, 14, 14);
            }
            g2.setColor(c.darker());
            g2.drawString(t, x + 22, (h + f.getAscent() - f.getDescent()) / 2);
            g2.dispose();
        }
    }

    /** Botão do caixa: contorno da cor, símbolo e texto; cheio da cor quando o rato passa; apagado se desligado. */
    private static final class BotaoCaixaUI extends javax.swing.plaf.basic.BasicButtonUI {

        private final MenuWeb.Simbolo simbolo;
        private final Color cor;

        BotaoCaixaUI(MenuWeb.Simbolo simbolo, Color cor) {
            this.simbolo = simbolo;
            this.cor = cor;
        }

        @Override
        public void paint(Graphics g, JComponent c) {
            javax.swing.AbstractButton b = (javax.swing.AbstractButton) c;
            Graphics2D g2 = ComponentesWeb.suave(g);
            int w = c.getWidth(), h = c.getHeight();
            boolean ligado = b.isEnabled(), rato = ligado && b.getModel().isRollover();
            Color base = ligado ? cor : new Color(203, 213, 225);
            g2.setColor(rato ? base : ligado ? ComponentesWeb.mistura(base, Color.WHITE, 0.9) : new Color(248, 250, 252));
            g2.fill(new RoundRectangle2D.Double(0, 0, w - 1, h - 1, 12, 12));
            g2.setColor(ligado ? base : new Color(226, 232, 240));
            g2.setStroke(new BasicStroke(1.3f));
            g2.draw(new RoundRectangle2D.Double(0.5, 0.5, w - 2, h - 2, 12, 12));
            Color fg = rato ? Color.WHITE : ligado ? base.darker() : new Color(148, 163, 184);
            g2.setFont(c.getFont());
            FontMetrics f = g2.getFontMetrics();
            String t = b.getText();
            int s = 22;
            int conteudo = s + 8 + f.stringWidth(t);
            boolean soIcone = conteudo > w - 12;
            int x = soIcone ? (w - s) / 2 : (w - conteudo) / 2;
            MenuWeb.desenharSimbolo(g2, simbolo, x, (h - s) / 2.0, s, fg);
            if (!soIcone) {
                g2.setColor(fg);
                g2.drawString(t, x + s + 8, (h + f.getAscent() - f.getDescent()) / 2);
            }
            g2.dispose();
        }
    }

    /** Duas setas em círculo (actualizar). */
    private static final class IconeActualizar implements javax.swing.Icon {

        @Override
        public int getIconWidth() {
            return 14;
        }

        @Override
        public int getIconHeight() {
            return 14;
        }

        @Override
        public void paintIcon(java.awt.Component c, Graphics g, int x, int y) {
            Graphics2D g2 = ComponentesWeb.suave(g);
            g2.setColor(ComponentesWeb.AZUL);
            g2.setStroke(new BasicStroke(1.7f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.drawArc(x + 2, y + 2, 10, 10, 40, 250);
            g2.drawLine(x + 11, y + 1, x + 11, y + 5);
            g2.drawLine(x + 11, y + 5, x + 7, y + 5);
            g2.dispose();
        }
    }

    private static String cortar(Graphics2D g2, String s, int largura) {
        if (s == null) {
            return "";
        }
        FontMetrics f = g2.getFontMetrics();
        if (f.stringWidth(s) <= largura) {
            return s;
        }
        while (s.length() > 1 && f.stringWidth(s + "…") > largura) {
            s = s.substring(0, s.length() - 1);
        }
        return s + "…";
    }
}
