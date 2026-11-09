package util.ui;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.plaf.basic.BasicGraphicsUtils;
import javax.swing.plaf.basic.BasicToggleButtonUI;

/**
 * Componentes com aspecto de página web: cartões arredondados com sombra,
 * botões e "pílulas" arredondados, cabeçalho em gradiente e layouts que se
 * reorganizam conforme a largura disponível (como CSS grid / flex-wrap).
 */
public final class ComponentesWeb {

    public static final Color FUNDO_PAGINA = new Color(237, 241, 247);
    public static final Color TEXTO = new Color(30, 41, 59);
    public static final Color TEXTO_SUAVE = new Color(100, 116, 139);
    public static final Color LINHA = new Color(222, 229, 239);
    public static Color AZUL = EstiloUI.AZUL_ESCURO;
    public static Color AZUL_MEDIO = new Color(0, 98, 178);
    public static Color AZUL_VIVO = new Color(37, 132, 235);
    public static final Color VERDE = new Color(22, 163, 74);
    public static final Color VERMELHO = new Color(220, 38, 38);
    public static final Color LARANJA = new Color(245, 158, 11);
    public static final Color ROXO = new Color(124, 58, 237);
    public static final Color CIANO = new Color(8, 145, 178);
    public static final Color CINZA = new Color(148, 163, 184);

    /** Paleta das séries dos gráficos (tipologias). */
    public static final Color[] PALETA = {AZUL_VIVO, VERDE, LARANJA, ROXO, CIANO,
        new Color(236, 72, 153), new Color(132, 204, 22), new Color(234, 88, 12), AZUL, CINZA};

    public enum Variante {
        PRIMARIO, SUCESSO, PERIGO, AVISO, SECUNDARIO
    }

    private ComponentesWeb() {
    }

    public static Graphics2D suave(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        return g2;
    }

    public static Color mistura(Color a, Color b, double t) {
        return new Color(
                (int) Math.round(a.getRed() + (b.getRed() - a.getRed()) * t),
                (int) Math.round(a.getGreen() + (b.getGreen() - a.getGreen()) * t),
                (int) Math.round(a.getBlue() + (b.getBlue() - a.getBlue()) * t));
    }

    public static Color alfa(Color c, int alfa) {
        return new Color(c.getRed(), c.getGreen(), c.getBlue(), alfa);
    }

    /** Sombra suave por baixo de um rectângulo arredondado. */
    static void sombra(Graphics2D g2, int x, int y, int w, int h, int raio) {
        for (int i = 4; i >= 1; i--) {
            g2.setColor(new Color(15, 23, 42, 7 + (4 - i) * 3));
            g2.fillRoundRect(x - i + 1, y - i + 3, w + 2 * i - 2, h + 2 * i - 2, raio + i * 2, raio + i * 2);
        }
    }

    // =====================================================================
    // CARTÃO
    // =====================================================================
    /**
     * Cartão branco arredondado com sombra e cabeçalho (título, barra de cor,
     * acções à direita). O conteúdo vai para {@link #getCorpo()}.
     */
    public static class Cartao extends JPanel {

        private static final int RAIO = 16;
        private final JPanel corpo;
        private final JPanel accoes;
        private final JLabel lbTitulo;
        private final Color destaque;

        public Cartao(String titulo, Color destaque, LayoutManager layoutCorpo) {
            super(new BorderLayout());
            this.destaque = destaque;
            setOpaque(false);
            setBorder(new EmptyBorder(3, 4, 7, 5)); // espaço para a sombra

            lbTitulo = new JLabel(titulo);
            lbTitulo.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(14.5f));
            lbTitulo.setForeground(TEXTO);
            accoes = transparente(new FlowLayout(FlowLayout.RIGHT, 6, 0));

            if (titulo != null) {
                JPanel cab = transparente(new BorderLayout(8, 0));
                cab.setBorder(new EmptyBorder(11, destaque != null ? 22 : 16, 4, 12));
                cab.add(lbTitulo, BorderLayout.CENTER);
                cab.add(accoes, BorderLayout.EAST);
                add(cab, BorderLayout.NORTH);
            }
            corpo = transparente(layoutCorpo);
            corpo.setBorder(new EmptyBorder(titulo != null ? 4 : 12, 14, 12, 14));
            add(corpo, BorderLayout.CENTER);
        }

        public JPanel getCorpo() {
            return corpo;
        }

        public JLabel getTitulo() {
            return lbTitulo;
        }

        public void adicionarAccao(JComponent c) {
            accoes.add(c);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = suave(g);
            Insets in = getInsets();
            int x = in.left, y = in.top;
            int w = getWidth() - in.left - in.right, h = getHeight() - in.top - in.bottom;
            sombra(g2, x, y, w, h, RAIO);
            g2.setColor(Color.WHITE);
            g2.fillRoundRect(x, y, w, h, RAIO, RAIO);
            g2.setColor(LINHA);
            g2.drawRoundRect(x, y, w - 1, h - 1, RAIO, RAIO);
            if (destaque != null && lbTitulo.getParent() != null) {
                g2.setColor(destaque);
                g2.fillRoundRect(x + 10, y + 13, 5, 20, 5, 5);
            }
            g2.dispose();
        }
    }

    // =====================================================================
    // CARTÃO DE INDICADOR (KPI)
    // =====================================================================
    /**
     * Indicador grande: símbolo num círculo de cor, valor e legenda.
     */
    public static class CartaoKpi extends JPanel {

        // símbolos desenhados (a letra do Windows não tem todos os caracteres)
        public static final String CAMA = "@cama";
        public static final String VISTO = "@visto";
        public static final String CHAVE = "@chave";
        public static final String PESSOA = "@pessoa";
        public static final String ENTRADA = "@entrada";
        public static final String SAIDA = "@saida";

        private final JLabel valor = new JLabel("—");
        private final JLabel legenda = new JLabel();
        private final JLabel detalhe = new JLabel(" ");
        private final Color cor;
        private final String simbolo;

        public CartaoKpi(String simbolo, String textoLegenda, Color cor) {
            super(new BorderLayout(12, 0));
            this.cor = cor;
            this.simbolo = simbolo;
            setOpaque(false);
            setBorder(new EmptyBorder(14, 66, 16, 10));
            valor.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(24f));
            valor.setForeground(TEXTO);
            legenda.setText(textoLegenda);
            legenda.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(12.5f));
            legenda.setForeground(TEXTO_SUAVE);
            detalhe.setFont(EstiloUI.FONTE.deriveFont(12f));
            detalhe.setForeground(cor.darker());
            JPanel textos = transparente(new java.awt.GridLayout(3, 1, 0, 0));
            textos.add(legenda);
            textos.add(valor);
            textos.add(detalhe);
            add(textos, BorderLayout.CENTER);
            setPreferredSize(new Dimension(190, 96));
            setMinimumSize(new Dimension(160, 96));
        }

        public void setValor(String v, String textoDetalhe) {
            valor.setText(v);
            detalhe.setText(textoDetalhe == null || textoDetalhe.isEmpty() ? " " : textoDetalhe);
            setToolTipText(legenda.getText() + ": " + v);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = suave(g);
            int x = 4, y = 3, w = getWidth() - 9, h = getHeight() - 10;
            sombra(g2, x, y, w, h, 16);
            g2.setColor(Color.WHITE);
            g2.fillRoundRect(x, y, w, h, 16, 16);
            g2.setColor(LINHA);
            g2.drawRoundRect(x, y, w - 1, h - 1, 16, 16);
            // faixa de cor no topo
            g2.setClip(x, y, w, 5);
            g2.setColor(cor);
            g2.fillRoundRect(x, y, w, 16, 16, 16);
            g2.setClip(null);
            // círculo com o símbolo
            int d = 44, cx = x + 12, cy = y + (h - d) / 2;
            g2.setPaint(new GradientPaint(cx, cy, mistura(cor, Color.WHITE, 0.15), cx + d, cy + d, cor.darker()));
            g2.fillOval(cx, cy, d, d);
            g2.setColor(Color.WHITE);
            if (simbolo.startsWith("@")) {
                desenharSimbolo(g2, simbolo, cx + d / 2, cy + d / 2);
            } else {
                g2.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(simbolo.length() > 1 ? 15f : 20f));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(simbolo, cx + (d - fm.stringWidth(simbolo)) / 2, cy + (d - fm.getHeight()) / 2 + fm.getAscent());
            }
            g2.dispose();
        }

        /** Ícones simples em linhas brancas, centrados em (mx, my), ~20 px. */
        private static void desenharSimbolo(Graphics2D g2, String s, int mx, int my) {
            g2.setStroke(new BasicStroke(2.4f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            switch (s) {
                case VISTO:
                    g2.drawPolyline(new int[]{mx - 8, mx - 2, mx + 9}, new int[]{my, my + 6, my - 6}, 3);
                    break;
                case CAMA:
                    g2.drawLine(mx - 10, my - 7, mx - 10, my + 8);          // cabeceira
                    g2.drawLine(mx - 10, my + 4, mx + 10, my + 4);          // estrado
                    g2.drawLine(mx + 10, my + 1, mx + 10, my + 8);          // pé
                    g2.fillRoundRect(mx - 7, my - 3, 6, 5, 3, 3);           // almofada
                    g2.drawRoundRect(mx - 1, my - 3, 11, 5, 3, 3);          // colchão
                    break;
                case CHAVE:
                    g2.drawOval(mx - 10, my - 6, 10, 10);
                    g2.drawLine(mx, my - 1, mx + 10, my - 1);
                    g2.drawLine(mx + 6, my - 1, mx + 6, my + 4);
                    g2.drawLine(mx + 10, my - 1, mx + 10, my + 4);
                    break;
                case PESSOA:
                    g2.drawOval(mx - 4, my - 10, 8, 8);
                    g2.drawArc(mx - 9, my + 1, 18, 16, 0, 180);
                    break;
                case ENTRADA:
                    g2.drawPolyline(new int[]{mx + 1, mx + 9, mx + 9, mx + 1}, new int[]{my - 9, my - 9, my + 9, my + 9}, 4);
                    g2.drawLine(mx - 10, my, mx + 3, my);
                    g2.drawPolyline(new int[]{mx - 2, mx + 3, mx - 2}, new int[]{my - 5, my, my + 5}, 3);
                    break;
                case SAIDA:
                    g2.drawPolyline(new int[]{mx - 1, mx - 9, mx - 9, mx - 1}, new int[]{my - 9, my - 9, my + 9, my + 9}, 4);
                    g2.drawLine(mx - 4, my, mx + 10, my);
                    g2.drawPolyline(new int[]{mx + 5, mx + 10, mx + 5}, new int[]{my - 5, my, my + 5}, 3);
                    break;
                default:
                    break;
            }
        }
    }

    // =====================================================================
    // CABEÇALHO EM GRADIENTE
    // =====================================================================
    public static class PainelGradiente extends JPanel {

        private final Color inicio;
        private final Color fim;

        public PainelGradiente(LayoutManager layout, Color inicio, Color fim) {
            super(layout);
            this.inicio = inicio;
            this.fim = fim;
            setOpaque(true);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = suave(g);
            int w = getWidth(), h = getHeight();
            g2.setPaint(new GradientPaint(0, 0, inicio, w, h, fim));
            g2.fillRect(0, 0, w, h);
            // círculos decorativos discretos
            g2.setColor(new Color(255, 255, 255, 14));
            g2.fillOval(w - 260, -120, 300, 300);
            g2.fillOval(w - 420, h - 40, 200, 200);
            g2.setColor(new Color(255, 255, 255, 9));
            g2.fillOval(w / 2, -160, 260, 260);
            g2.dispose();
        }
    }

    // =====================================================================
    // BOTÕES
    // =====================================================================
    public static Color corVariante(Variante v) {
        switch (v) {
            case SUCESSO:
                return VERDE;
            case PERIGO:
                return VERMELHO;
            case AVISO:
                return LARANJA;
            case SECUNDARIO:
                return Color.WHITE;
            default:
                return AZUL_MEDIO;
        }
    }

    /**
     * Botão arredondado preenchido (estilo web). Mantém os ActionListeners.
     */
    public static void botao(AbstractButton b, Variante v, String caminhoIcone, String dica) {
        ImageIcon ic = caminhoIcone != null ? EstiloUI.icone(caminhoIcone, 18) : null;
        if (ic == null && b.getIcon() instanceof ImageIcon) {
            Image actual = ((ImageIcon) b.getIcon()).getImage();
            ic = new ImageIcon(actual.getScaledInstance(18, 18, Image.SCALE_SMOOTH));
        }
        b.setIcon(ic);
        b.setUI(new BotaoWebUI(v));
        b.setOpaque(false);
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setRolloverEnabled(true);
        b.setBorder(new EmptyBorder(8, 16, 8, 16));
        b.setIconTextGap(8);
        b.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(13f));
        b.setForeground(v == Variante.SECUNDARIO ? AZUL : Color.WHITE);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        if (dica != null) {
            b.setToolTipText(dica);
        }
    }

    public static class BotaoWebUI extends BasicButtonUI {

        private final Variante variante;

        public BotaoWebUI(Variante variante) {
            this.variante = variante;
        }

        @Override
        public void paint(Graphics g, JComponent c) {
            AbstractButton b = (AbstractButton) c;
            ButtonModel m = b.getModel();
            Graphics2D g2 = suave(g);
            int w = c.getWidth(), h = c.getHeight();
            // "web.pilula": cantos totalmente redondos, como as pílulas do cabeçalho
            int arco = Boolean.TRUE.equals(c.getClientProperty("web.pilula")) ? h : 12;
            Color base = corVariante(variante);
            if (variante == Variante.SECUNDARIO) {
                Color fundo = !b.isEnabled() ? new Color(246, 248, 251)
                        : m.isPressed() ? new Color(219, 231, 245)
                        : m.isRollover() ? new Color(236, 243, 252) : Color.WHITE;
                g2.setColor(fundo);
                g2.fillRoundRect(0, 0, w - 1, h - 1, arco, arco);
                g2.setColor(m.isRollover() ? AZUL_VIVO : LINHA);
                g2.drawRoundRect(0, 0, w - 1, h - 1, arco, arco);
            } else {
                Color fundo = !b.isEnabled() ? mistura(base, Color.WHITE, 0.55)
                        : m.isPressed() ? base.darker()
                        : m.isRollover() ? mistura(base, Color.WHITE, 0.12) : base;
                if (b.isEnabled() && m.isRollover()) {
                    g2.setColor(alfa(base, 50));
                    g2.fillRoundRect(0, 2, w, h - 1, arco, arco);
                }
                g2.setPaint(new GradientPaint(0, 0, mistura(fundo, Color.WHITE, 0.08), 0, h, fundo));
                g2.fillRoundRect(0, 0, w - 1, h - 2, arco, arco);
            }
            g2.dispose();
            super.paint(g, c);
        }

        @Override
        protected void paintText(Graphics g, AbstractButton b, Rectangle r, String texto) {
            Graphics2D g2 = suave(g);
            Color fg = b.getForeground();
            g2.setColor(b.isEnabled() ? fg : alfa(fg, 150));
            g2.setFont(b.getFont());
            FontMetrics fm = g2.getFontMetrics();
            BasicGraphicsUtils.drawStringUnderlineCharAt(g2, texto, b.getDisplayedMnemonicIndex(),
                    r.x, r.y + fm.getAscent());
            g2.dispose();
        }

        @Override
        protected void paintFocus(Graphics g, AbstractButton b, Rectangle vr, Rectangle tr, Rectangle ir) {
        }

        @Override
        protected void paintButtonPressed(Graphics g, AbstractButton b) {
        }
    }

    /**
     * Botões de opção (radio) como "pílulas": selecção bem visível, sem o
     * círculo do Windows. escuro = sobre o cabeçalho azul.
     */
    public static void pilula(JToggleButton b, boolean escuro) {
        b.setUI(new PilulaUI(escuro));
        b.setOpaque(false);
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setRolloverEnabled(true);
        b.setIcon(new PontoIcone(escuro));
        b.setIconTextGap(7);
        b.setBorder(new EmptyBorder(6, 12, 6, 14));
        b.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(12.5f));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    /** Pílula mais pequena (para caberem várias numa linha estreita). */
    public static void pilulaCompacta(JToggleButton b) {
        pilula(b, false);
        b.setBorder(new EmptyBorder(5, 8, 5, 10));
        b.setIconTextGap(5);
        b.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(12f));
    }

    public static class PilulaUI extends BasicToggleButtonUI {

        private final boolean escuro;

        public PilulaUI(boolean escuro) {
            this.escuro = escuro;
        }

        @Override
        public void paint(Graphics g, JComponent c) {
            AbstractButton b = (AbstractButton) c;
            ButtonModel m = b.getModel();
            Graphics2D g2 = suave(g);
            int w = c.getWidth(), h = c.getHeight();
            int arco = h;
            if (escuro) {
                if (b.isSelected()) {
                    g2.setColor(Color.WHITE);
                    g2.fillRoundRect(0, 0, w - 1, h - 1, arco, arco);
                } else {
                    g2.setColor(new Color(255, 255, 255, m.isRollover() ? 46 : 20));
                    g2.fillRoundRect(0, 0, w - 1, h - 1, arco, arco);
                    g2.setColor(new Color(255, 255, 255, 70));
                    g2.drawRoundRect(0, 0, w - 1, h - 1, arco, arco);
                }
            } else {
                if (b.isSelected()) {
                    g2.setColor(AZUL_MEDIO);
                    g2.fillRoundRect(0, 0, w - 1, h - 1, arco, arco);
                } else {
                    g2.setColor(m.isRollover() ? new Color(232, 240, 251) : new Color(244, 247, 251));
                    g2.fillRoundRect(0, 0, w - 1, h - 1, arco, arco);
                    g2.setColor(m.isRollover() ? AZUL_VIVO : LINHA);
                    g2.drawRoundRect(0, 0, w - 1, h - 1, arco, arco);
                }
            }
            g2.dispose();
            super.paint(g, c);
        }

        @Override
        protected void paintText(Graphics g, AbstractButton b, Rectangle r, String texto) {
            Graphics2D g2 = suave(g);
            Color fg;
            if (escuro) {
                fg = b.isSelected() ? AZUL : Color.WHITE;
            } else {
                fg = b.isSelected() ? Color.WHITE : TEXTO;
            }
            g2.setColor(b.isEnabled() ? fg : alfa(fg, 120));
            g2.setFont(b.getFont());
            FontMetrics fm = g2.getFontMetrics();
            BasicGraphicsUtils.drawStringUnderlineCharAt(g2, texto, b.getDisplayedMnemonicIndex(),
                    r.x, r.y + fm.getAscent());
            g2.dispose();
        }

        @Override
        protected void paintButtonPressed(Graphics g, AbstractButton b) {
        }

        @Override
        protected void paintFocus(Graphics g, AbstractButton b, Rectangle vr, Rectangle tr, Rectangle ir) {
        }
    }

    /** Pequeno círculo à esquerda da pílula (cheio quando seleccionada). */
    private static class PontoIcone implements Icon {

        private final boolean escuro;

        PontoIcone(boolean escuro) {
            this.escuro = escuro;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            boolean sel = c instanceof AbstractButton && ((AbstractButton) c).isSelected();
            Graphics2D g2 = suave(g);
            Color cor;
            if (escuro) {
                cor = sel ? VERDE : Color.WHITE;
            } else {
                cor = sel ? Color.WHITE : CINZA;
            }
            g2.setStroke(new BasicStroke(1.6f));
            g2.setColor(cor);
            if (sel) {
                g2.fillOval(x + 1, y + 1, 10, 10);
                g2.setColor(escuro ? Color.WHITE : AZUL_MEDIO);
                g2.fillOval(x + 4, y + 4, 4, 4);
            } else {
                g2.drawOval(x + 1, y + 1, 10, 10);
            }
            g2.dispose();
        }

        @Override
        public int getIconWidth() {
            return 12;
        }

        @Override
        public int getIconHeight() {
            return 12;
        }
    }

    // =====================================================================
    // BOTÃO "ALOJAR" (avião a descolar, a piscar)
    // =====================================================================
    /**
     * Botão em forma de pílula, com a mesma altura das pílulas do cabeçalho,
     * cor laranja viva e um avião a levantar voo. Enquanto "piscar" estiver
     * ligado, a cor e o contorno pulsam suavemente para chamar a atenção.
     */
    public static class BotaoAlojar extends javax.swing.JButton {

        private final Timer pulsar;
        private double fase = 0;
        private boolean piscar = false;
        private boolean modoVoltar = false;
        private final String textoAlojar;
        private final String textoVoltar;

        /**
         * Dois modos: "alojar" (verde, avião, a piscar) no painel e "voltar"
         * (laranja, ícone de dashboard) na área de alojamento.
         */
        public BotaoAlojar(String textoAlojar, String textoVoltar) {
            super(textoAlojar);
            this.textoAlojar = textoAlojar;
            this.textoVoltar = textoVoltar;
            setIcon(new IconeAviao(16));
            setIconTextGap(8);
            setFont(EstiloUI.FONTE_NEGRITO.deriveFont(12.5f));
            setForeground(Color.WHITE);
            setBorder(new EmptyBorder(6, 12, 6, 16)); // igual às pílulas -> mesma altura
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setOpaque(false);
            setRolloverEnabled(true);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            pulsar = new Timer(30, e -> {
                fase += 0.36; // ~0,5 s por ciclo: pisca depressa para chamar a atenção
                repaint();
            });
        }

        /** true = "Voltar ao Dashboard" (laranja, sem piscar); false = alojar (verde, a piscar). */
        public void setModoVoltar(boolean voltar) {
            modoVoltar = voltar;
            setText(voltar ? textoVoltar : textoAlojar);
            setIcon(voltar ? new IconeDashboard(16, Color.WHITE) : new IconeAviao(16));
            setPiscar(true); // pisca nos dois modos
            revalidate();
        }

        public boolean isModoVoltar() {
            return modoVoltar;
        }

        public void setPiscar(boolean ligar) {
            piscar = ligar;
            if (ligar && isDisplayable()) {
                pulsar.start();
            } else {
                pulsar.stop();
                fase = 0;
                repaint();
            }
        }

        @Override
        public void addNotify() {
            super.addNotify();
            if (piscar) {
                pulsar.start();
            }
        }

        @Override
        public void removeNotify() {
            pulsar.stop(); // janela fechada: não deixar o temporizador a correr
            super.removeNotify();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = suave(g);
            int w = getWidth(), h = getHeight();
            double t = piscar ? (Math.sin(fase) + 1) / 2 : 0; // 0..1
            Color topo, fundo;
            if (modoVoltar) {
                // laranja (a cor antiga do botão): voltar ao painel; pisca
                // entre o laranja e um amarelo-dourado
                Color base = getModel().isPressed() ? new Color(194, 65, 12)
                        : getModel().isRollover() ? new Color(251, 146, 60) : LARANJA;
                topo = mistura(mistura(base, Color.WHITE, 0.10), new Color(250, 204, 21), 0.65 * t);
                fundo = mistura(mistura(base, new Color(234, 88, 12), 0.6), new Color(234, 179, 8), 0.55 * t);
            } else {
                // verde: ir alojar; pisca entre o verde e um verde-claro
                // brilhante, com o contorno branco a acender
                Color base = getModel().isPressed() ? new Color(21, 128, 61)
                        : getModel().isRollover() ? new Color(34, 197, 94) : VERDE;
                Color claro = new Color(134, 239, 172);
                topo = mistura(mistura(base, Color.WHITE, 0.10), claro, 0.70 * t);
                fundo = mistura(mistura(base, new Color(21, 128, 61), 0.5), new Color(74, 222, 128), 0.55 * t);
            }
            g2.setPaint(new GradientPaint(0, 0, topo, 0, h, fundo));
            g2.fillRoundRect(0, 0, w - 1, h - 1, h, h);
            g2.setStroke(new BasicStroke(1.8f));
            g2.setColor(new Color(255, 255, 255, (int) (70 + 185 * t)));
            g2.drawRoundRect(1, 1, w - 3, h - 3, h - 2, h - 2);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    /** Ícone de dashboard: mosaico de painéis com um gráfico de barras. */
    public static class IconeDashboard implements Icon {

        private final int tamanho;
        private final Color cor;

        public IconeDashboard(int tamanho, Color cor) {
            this.tamanho = tamanho;
            this.cor = cor;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = suave(g);
            g2.translate(x, y);
            double s = tamanho / 20.0;
            g2.scale(s, s);
            g2.setColor(cor);
            g2.fillRoundRect(1, 1, 8, 10, 3, 3);     // painel grande (cima esquerda)
            g2.fillRoundRect(11, 1, 8, 5, 3, 3);     // painel pequeno (cima direita)
            g2.fillRoundRect(1, 13, 8, 6, 3, 3);     // painel pequeno (baixo esquerda)
            g2.setColor(alfa(cor, 60));
            g2.fillRoundRect(11, 8, 8, 11, 3, 3);    // painel do gráfico
            g2.setColor(cor);
            g2.fillRect(12, 14, 2, 4);               // barras
            g2.fillRect(14, 11, 2, 7);
            g2.fillRect(16, 9, 2, 9);
            g2.dispose();
        }

        @Override
        public int getIconWidth() {
            return tamanho;
        }

        @Override
        public int getIconHeight() {
            return tamanho;
        }
    }

    /** Ícone "sair": porta aberta com seta para fora. */
    public static class IconeSair implements Icon {

        private final int tamanho;
        private final Color cor;

        public IconeSair(int tamanho, Color cor) {
            this.tamanho = tamanho;
            this.cor = cor;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = suave(g);
            g2.translate(x, y);
            double s = tamanho / 20.0;
            g2.scale(s, s);
            g2.setColor(cor);
            g2.setStroke(new BasicStroke(2.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.drawPolyline(new int[]{9, 3, 3, 9}, new int[]{3, 3, 17, 17}, 4); // aro da porta
            g2.drawLine(8, 10, 18, 10);                                        // seta para fora
            g2.drawPolyline(new int[]{14, 18, 14}, new int[]{6, 10, 14}, 3);
            g2.dispose();
        }

        @Override
        public int getIconWidth() {
            return tamanho;
        }

        @Override
        public int getIconHeight() {
            return tamanho;
        }
    }

    /** Avião visto de cima, a subir (nariz para cima e à direita). */
    public static class IconeAviao implements Icon {

        private final int tamanho;

        public IconeAviao(int tamanho) {
            this.tamanho = tamanho;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = suave(g);
            g2.translate(x + tamanho / 2.0, y + tamanho / 2.0);
            g2.rotate(Math.toRadians(40)); // a descolar
            double s = tamanho / 20.0;
            g2.scale(s, s);
            g2.setColor(Color.WHITE);
            java.awt.geom.Path2D aviao = new java.awt.geom.Path2D.Double();
            // fuselagem com nariz arredondado
            aviao.append(new java.awt.geom.RoundRectangle2D.Double(-1.8, -10, 3.6, 18, 3.6, 4), false);
            // asas
            aviao.moveTo(-1.5, -2.5);
            aviao.lineTo(-10, 2.5);
            aviao.lineTo(-10, 4.5);
            aviao.lineTo(-1.5, 2);
            aviao.lineTo(1.5, 2);
            aviao.lineTo(10, 4.5);
            aviao.lineTo(10, 2.5);
            aviao.lineTo(1.5, -2.5);
            aviao.closePath();
            // cauda
            aviao.moveTo(-1.2, 5.5);
            aviao.lineTo(-5, 8.5);
            aviao.lineTo(-5, 10);
            aviao.lineTo(0, 8.5);
            aviao.lineTo(5, 10);
            aviao.lineTo(5, 8.5);
            aviao.lineTo(1.2, 5.5);
            aviao.closePath();
            g2.fill(aviao);
            g2.dispose();
        }

        @Override
        public int getIconWidth() {
            return tamanho;
        }

        @Override
        public int getIconHeight() {
            return tamanho;
        }
    }

    // =====================================================================
    // ETIQUETAS
    // =====================================================================
    /** Etiqueta arredondada (badge) com fundo suave da cor indicada. */
    public static class Etiqueta extends JLabel {

        private Color cor;

        public Etiqueta(String texto, Color cor) {
            super(texto, SwingConstants.CENTER);
            this.cor = cor;
            setFont(EstiloUI.FONTE_NEGRITO.deriveFont(12f));
            setBorder(new EmptyBorder(4, 12, 4, 12));
            setForeground(cor.darker());
        }

        public void setCor(Color cor) {
            this.cor = cor;
            setForeground(cor.darker());
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = suave(g);
            g2.setColor(alfa(cor, 34));
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, getHeight(), getHeight());
            g2.dispose();
            super.paintComponent(g);
        }
    }

    // =====================================================================
    // CAIXA ARREDONDADA (totais, resumo do cálculo, avisos)
    // =====================================================================
    public static class CaixaSuave extends JPanel {

        private final Color fundo;
        private final Color borda;

        public CaixaSuave(LayoutManager layout, Color fundo, Color borda) {
            super(layout);
            this.fundo = fundo;
            this.borda = borda;
            setOpaque(false);
            setBorder(new EmptyBorder(8, 12, 8, 12));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = suave(g);
            g2.setColor(fundo);
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 14, 14);
            if (borda != null) {
                g2.setColor(borda);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 14, 14);
            }
            g2.dispose();
        }
    }

    // =====================================================================
    // ABAS (sublinhado de cor no separador activo, sem molduras)
    // =====================================================================
    public static void abas(javax.swing.JTabbedPane t) {
        t.setUI(new AbasWebUI());
        t.setOpaque(true);
        t.setBackground(FUNDO_PAGINA);
        t.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(14f));
        t.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    public static class AbasWebUI extends javax.swing.plaf.basic.BasicTabbedPaneUI {

        @Override
        protected void installDefaults() {
            super.installDefaults();
            tabInsets = new Insets(9, 16, 9, 18);
            selectedTabPadInsets = new Insets(0, 0, 0, 0);
            tabAreaInsets = new Insets(4, 6, 0, 6);
            contentBorderInsets = new Insets(1, 0, 0, 0);
        }

        @Override
        protected void paintTabBackground(Graphics g, int lugar, int i, int x, int y, int w, int h, boolean sel) {
            Graphics2D g2 = suave(g);
            if (sel) {
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(x + 2, y + 2, w - 4, h + 8, 12, 12);
            } else if (getRolloverTab() == i) {
                g2.setColor(new Color(225, 233, 244));
                g2.fillRoundRect(x + 2, y + 2, w - 4, h + 8, 12, 12);
            }
            g2.dispose();
        }

        @Override
        protected void paintTabBorder(Graphics g, int lugar, int i, int x, int y, int w, int h, boolean sel) {
            if (sel) {
                Graphics2D g2 = suave(g);
                g2.setColor(AZUL_MEDIO);
                g2.fillRoundRect(x + 10, y + h - 3, w - 20, 3, 3, 3);
                g2.dispose();
            }
        }

        @Override
        protected void paintText(Graphics g, int lugar, Font f, FontMetrics fm, int i, String titulo,
                Rectangle r, boolean sel) {
            Graphics2D g2 = suave(g);
            g2.setFont(f);
            g2.setColor(!tabPane.isEnabledAt(i) ? CINZA : sel ? AZUL : TEXTO_SUAVE);
            BasicGraphicsUtils.drawStringUnderlineCharAt(g2, titulo, tabPane.getDisplayedMnemonicIndexAt(i),
                    r.x, r.y + fm.getAscent());
            g2.dispose();
        }

        @Override
        protected void paintFocusIndicator(Graphics g, int lugar, Rectangle[] rs, int i, Rectangle ir,
                Rectangle tr, boolean sel) {
        }

        @Override
        protected void paintContentBorder(Graphics g, int lugar, int sel) {
            Insets in = tabPane.getInsets();
            int y = in.top + calculateTabAreaHeight(lugar, runCount, maxTabHeight);
            g.setColor(LINHA);
            g.drawLine(in.left, y, tabPane.getWidth() - in.right, y);
        }
    }

    // =====================================================================
    // LISTAS
    // =====================================================================
    /** Ícone: pequeno círculo de cor (estado, legenda). */
    public static class IconeCor implements Icon {

        private final Color cor;
        private final int tamanho;

        public IconeCor(Color cor, int tamanho) {
            this.cor = cor;
            this.tamanho = tamanho;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = suave(g);
            g2.setColor(cor);
            g2.fillOval(x, y, tamanho, tamanho);
            g2.dispose();
        }

        @Override
        public int getIconWidth() {
            return tamanho;
        }

        @Override
        public int getIconHeight() {
            return tamanho;
        }
    }

    /** Linhas da lista com margem, ícone opcional e selecção arredondada. */
    public static class RendererLista extends javax.swing.DefaultListCellRenderer {

        private final Icon icone;
        private boolean seleccionado;

        public RendererLista(Icon icone) {
            this.icone = icone;
        }

        @Override
        public Component getListCellRendererComponent(javax.swing.JList<?> lista, Object valor, int indice,
                boolean sel, boolean foco) {
            super.getListCellRendererComponent(lista, valor, indice, sel, false);
            seleccionado = sel;
            setOpaque(false);
            setIcon(icone);
            setIconTextGap(8);
            setBorder(new EmptyBorder(2, 10, 2, 8));
            setForeground(sel ? Color.WHITE : TEXTO);
            setFont(sel ? EstiloUI.FONTE_NEGRITO : EstiloUI.FONTE);
            return this;
        }

        @Override
        protected void paintComponent(Graphics g) {
            if (seleccionado) {
                Graphics2D g2 = suave(g);
                g2.setColor(AZUL_MEDIO);
                g2.fillRoundRect(2, 1, getWidth() - 4, getHeight() - 2, 10, 10);
                g2.dispose();
            }
            super.paintComponent(g);
        }
    }

    // =====================================================================
    // TABELA: ícones de acção (lupa / eliminar)
    // =====================================================================
    /**
     * Desenha as células com botões (JButton no modelo) como pequenos ícones
     * arredondados: lixo vermelho para o texto de remoção indicado, lupa azul
     * para os restantes. Não cria componentes nem lê imagens do disco a cada
     * repintura (o renderer antigo fazia new ImageIcon(getResource) sempre).
     */
    public static class RendererIconeAccao extends JComponent implements javax.swing.table.TableCellRenderer {

        private final String textoRemover;
        private boolean remover;
        private boolean seleccionada;
        private Color fundo;
        private String tipoIcone;

        public RendererIconeAccao(String textoRemover) {
            this.textoRemover = textoRemover;
        }

        @Override
        public Component getTableCellRendererComponent(javax.swing.JTable t, Object valor, boolean sel,
                boolean foco, int linha, int coluna) {
            String texto = valor instanceof AbstractButton ? ((AbstractButton) valor).getText() : String.valueOf(valor);
            remover = textoRemover.equals(texto);

            if ("Desalojar(Checkout)".equals(texto)) {
                tipoIcone = "saida";
            } else if ("Cancelar".equals(texto)) {
                tipoIcone = "cancelar";
            } else if ("Adicionar Diária".equals(texto)) {
                tipoIcone = "adicionar";
            } else if (remover) {
                tipoIcone = "remover";
            } else {
                tipoIcone = "outro";
            }

            seleccionada = sel;
            fundo = sel ? t.getSelectionBackground() : (linha % 2 == 0 ? Color.WHITE : EstiloUI.LINHA_IMPAR);

            String tooltip = "Desalojar(Checkout)".equals(texto) ? "Desalojar - Realizar checkout" :
                           "Cancelar".equals(texto) ? "Cancelar operação" :
                           "Adicionar Diária".equals(texto) ? "Adicionar nova diária" :
                           "remover".equals(tipoIcone) ? "Remover esta linha do documento" : "Ação";
            setToolTipText(tooltip);
            return this;
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = suave(g);
            g2.setColor(fundo);
            g2.fillRect(0, 0, getWidth(), getHeight());
            int w = 30, h = Math.min(24, getHeight() - 4);
            int x = (getWidth() - w) / 2, y = (getHeight() - h) / 2;

            Color cor;
            if ("saida".equals(tipoIcone)) {
                cor = new Color(34, 197, 94); // Verde
            } else if ("adicionar".equals(tipoIcone)) {
                cor = VERDE;
            } else if ("remover".equals(tipoIcone)) {
                cor = VERMELHO;
            } else {
                cor = LARANJA;
            }

            g2.setColor(seleccionada ? Color.WHITE : alfa(cor, 30));
            g2.fillRoundRect(x, y, w, h, 10, 10);
            g2.setColor(alfa(cor, 120));
            g2.drawRoundRect(x, y, w - 1, h - 1, 10, 10);
            g2.setColor(cor);
            g2.setStroke(new BasicStroke(1.8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            int cx = x + w / 2, cy = y + h / 2;

            if ("saida".equals(tipoIcone)) { // avião decolando
                g2.drawLine(cx - 6, cy + 3, cx + 6, cy - 4); // fuselagem
                g2.drawLine(cx + 4, cy - 3, cx + 6, cy - 4); // nariz
                g2.drawLine(cx - 4, cy - 1, cx + 2, cy - 3); // asa superior
                g2.drawLine(cx - 5, cy + 1, cx + 1, cy - 1); // asa inferior
                g2.drawLine(cx + 2, cy + 2, cx + 3, cy + 4); // cauda
            } else if ("adicionar".equals(tipoIcone)) { // sinal de +
                g2.drawLine(cx - 5, cy, cx + 5, cy);
                g2.drawLine(cx, cy - 5, cx, cy + 5);
            } else if ("cancelar".equals(tipoIcone)) { // X (cancelamento)
                g2.drawLine(cx - 5, cy - 5, cx + 5, cy + 5);
                g2.drawLine(cx - 5, cy + 5, cx + 5, cy - 5);
            } else if ("remover".equals(tipoIcone)) { // caixote do lixo
                g2.drawLine(cx - 6, cy - 4, cx + 6, cy - 4); // tampa
                g2.drawLine(cx - 2, cy - 6, cx + 2, cy - 6); // pega
                g2.drawRoundRect(cx - 4, cy - 4, 8, 10, 2, 2); // corpo
                g2.drawLine(cx - 1, cy - 1, cx - 1, cy + 3); // ranhuras
                g2.drawLine(cx + 1, cy - 1, cx + 1, cy + 3);
            } else { // outro
                g2.drawOval(cx - 6, cy - 6, 9, 9);
                g2.drawLine(cx + 2, cy + 2, cx + 6, cy + 6);
            }
            g2.dispose();
        }
    }

    // =====================================================================
    // CABEÇALHO DE TABELA CUSTOMIZADO
    // =====================================================================
    public static class RendererCabecalho extends DefaultTableCellRenderer {
        public RendererCabecalho() {
            setOpaque(true);
            setHorizontalAlignment(SwingConstants.CENTER);
            setFont(EstiloUI.FONTE_NEGRITO);
            setBackground(new Color(225, 29, 72));
            setForeground(Color.WHITE);
            setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 8, 4, 8));
        }
    }

    // =====================================================================
    // RENDERER COM QUEBRA DE LINHA PARA JLIST
    // =====================================================================
    public static class RendererQuadaLinhaJList extends javax.swing.JTextArea implements javax.swing.ListCellRenderer<Object> {
        public RendererQuadaLinhaJList() {
            setLineWrap(true);
            setWrapStyleWord(true);
            setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 6, 4, 6));
            setFont(EstiloUI.FONTE);
        }

        @Override
        public javax.swing.JComponent getListCellRendererComponent(
                javax.swing.JList<?> list, Object valor, int index,
                boolean isSelected, boolean cellHasFocus) {
            setText(valor == null ? "" : valor.toString());
            setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());
            setForeground(isSelected ? list.getSelectionForeground() : list.getForeground());
            return this;
        }
    }

    // =====================================================================
    // LISTA DE QUARTOS EM MOSAICO
    // =====================================================================
    /** Quarto livre como mosaico (número grande), para JList em HORIZONTAL_WRAP. */
    public static class RendererMosaicoQuarto extends JComponent implements javax.swing.ListCellRenderer<Object> {

        private String texto = "";
        private String legenda = "livre";
        private boolean seleccionado;
        /** Tipologia dos quartos mostrados (ex.: "Casal"); null = só "livre". */
        private final java.util.function.Supplier<String> tipologia;

        public RendererMosaicoQuarto() {
            this(null);
        }

        public RendererMosaicoQuarto(java.util.function.Supplier<String> tipologia) {
            this.tipologia = tipologia;
        }

        @Override
        public Component getListCellRendererComponent(javax.swing.JList<?> lista, Object valor, int indice,
                boolean sel, boolean foco) {
            texto = valor == null ? "" : valor.toString();
            seleccionado = sel;
            String t = tipologia == null ? null : tipologia.get();
            legenda = t == null || t.trim().isEmpty() ? "livre" : "livre (" + t.trim() + ")";
            setToolTipText("Quarto " + texto + " · " + legenda);
            return this;
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = suave(g);
            int x = 3, y = 3, w = getWidth() - 6, h = getHeight() - 6;
            if (seleccionado) {
                g2.setPaint(new GradientPaint(0, y, AZUL_VIVO, 0, y + h, AZUL_MEDIO));
                g2.fillRoundRect(x, y, w, h, 12, 12);
            } else {
                g2.setColor(new Color(240, 253, 244));
                g2.fillRoundRect(x, y, w, h, 12, 12);
                g2.setColor(new Color(134, 239, 172));
                g2.drawRoundRect(x, y, w - 1, h - 1, 12, 12);
                g2.setColor(VERDE);
                g2.fillRoundRect(x, y, 5, h, 5, 5);
                g2.fillRect(x + 2, y, 3, h);
            }
            g2.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(15f));
            FontMetrics fm = g2.getFontMetrics();
            String t = texto;
            while (t.length() > 1 && fm.stringWidth(t) > w - 14) {
                t = t.substring(0, t.length() - 1);
            }
            if (!t.equals(texto)) {
                t = t.substring(0, Math.max(1, t.length() - 1)) + "…";
            }
            g2.setColor(seleccionado ? Color.WHITE : TEXTO);
            g2.drawString(t, x + (w - fm.stringWidth(t)) / 2 + 2, y + h / 2 + 1);
            // "livre (Casal)": letra um pouco menor se não couber; corta com "…"
            g2.setFont(EstiloUI.FONTE.deriveFont(10.5f));
            if (g2.getFontMetrics().stringWidth(legenda) > w - 10) {
                g2.setFont(EstiloUI.FONTE.deriveFont(9.5f));
            }
            FontMetrics fm2 = g2.getFontMetrics();
            String sub = legenda;
            if (fm2.stringWidth(sub) > w - 10) {
                while (sub.length() > 1 && fm2.stringWidth(sub + "…)") > w - 10) {
                    sub = sub.substring(0, sub.length() - 1);
                }
                sub = sub + "…)";
            }
            g2.setColor(seleccionado ? new Color(219, 234, 254) : VERDE.darker());
            g2.drawString(sub, x + (w - fm2.stringWidth(sub)) / 2 + 2, y + h - 6);
            g2.dispose();
        }
    }

    // =====================================================================
    // UTILITÁRIOS
    // =====================================================================
    public static JPanel transparente(LayoutManager layout) {
        JPanel p = layout == null ? new JPanel() : new JPanel(layout);
        p.setOpaque(false);
        return p;
    }

    /**
     * Torna transparentes os painéis e botões de opção de cor por omissão
     * dentro de um cartão (fica o branco do cartão). Painéis com cor própria
     * (ex.: totais) e o conteúdo dos JScrollPane não são alterados.
     */
    public static void transparentar(Container c) {
        for (Component f : c.getComponents()) {
            if (f instanceof JScrollPane) {
                Component vista = ((JScrollPane) f).getViewport().getView();
                if (vista instanceof PainelRolavel) {
                    transparentar((Container) vista); // área deslizante do cartão: tratar o conteúdo
                } else {
                    ((JScrollPane) f).setBorder(new javax.swing.border.LineBorder(LINHA, 1, true));
                }
                continue;
            }
            if ((f instanceof JPanel || f instanceof javax.swing.JRadioButton || f instanceof javax.swing.JCheckBox)
                    && f.getBackground() instanceof UIResource) {
                ((JComponent) f).setOpaque(false);
            }
            if (f instanceof JPanel) {
                transparentar((Container) f);
            }
        }
    }

    // =====================================================================
    // ÁREA DESLIZANTE DO CARTÃO (rodapé fixo fica fora dela)
    // =====================================================================
    /**
     * Envolve o conteúdo do meio de um cartão: ocupa a largura toda e só
     * mostra a barra vertical quando nem com os tamanhos mínimos cabe (as
     * listas/tabelas encolhem primeiro). Assim os botões do rodapé do cartão
     * ficam sempre visíveis.
     */
    public static JScrollPane rolagemVertical(Component conteudo) {
        PainelRolavel p = new PainelRolavel();
        p.add(conteudo, BorderLayout.CENTER);
        JScrollPane sp = new JScrollPane(p, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        sp.setBorder(null);
        sp.setOpaque(false);
        sp.getViewport().setOpaque(false);
        sp.getVerticalScrollBar().setUnitIncrement(16);
        sp.setMinimumSize(new Dimension(60, 40));
        return sp;
    }

    public static class PainelRolavel extends JPanel implements javax.swing.Scrollable {

        PainelRolavel() {
            super(new BorderLayout());
            setOpaque(false);
        }

        @Override
        public Dimension getPreferredScrollableViewportSize() {
            return getPreferredSize();
        }

        @Override
        public int getScrollableUnitIncrement(Rectangle r, int o, int d) {
            return 16;
        }

        @Override
        public int getScrollableBlockIncrement(Rectangle r, int o, int d) {
            return 120;
        }

        @Override
        public boolean getScrollableTracksViewportWidth() {
            return true;
        }

        @Override
        public boolean getScrollableTracksViewportHeight() {
            // compara com o mínimo: enquanto couber, encolhe em vez de deslizar
            return getParent() instanceof javax.swing.JViewport
                    && getParent().getHeight() >= getMinimumSize().height;
        }
    }

    // =====================================================================
    // TEXTO COM QUEBRA DE LINHA (ex.: valor por extenso)
    // =====================================================================
    /**
     * Texto só de leitura que quebra linhas na largura que o layout lhe der
     * (um JLabel não quebra). Altura ajusta-se ao número de linhas.
     */
    public static class TextoQuebrado extends javax.swing.JTextArea {

        public TextoQuebrado() {
            setEditable(false);
            setFocusable(false);
            setOpaque(false);
            setLineWrap(true);
            setWrapStyleWord(true);
            setBorder(null);
            setHighlighter(null);
            addComponentListener(new java.awt.event.ComponentAdapter() {
                private int larguraAnterior = -1;

                @Override
                public void componentResized(java.awt.event.ComponentEvent e) {
                    if (getWidth() != larguraAnterior) {
                        larguraAnterior = getWidth();
                        revalidate(); // nova largura -> pode mudar o número de linhas
                    }
                }
            });
        }

        @Override
        public Dimension getPreferredSize() {
            Dimension d = super.getPreferredSize();
            return new Dimension(40, d.height); // a largura é a que o layout der
        }

        @Override
        public Dimension getMinimumSize() {
            return getPreferredSize();
        }
    }

    /** Rótulo de secção: pequeno, maiúsculas, cinzento (como nos formulários web). */
    public static void rotulo(JLabel l) {
        l.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(12f));
        l.setForeground(TEXTO_SUAVE);
    }

    /** Envolve um componente com um rótulo por cima (campo de formulário web). */
    public static JPanel campo(String titulo, Component c) {
        JPanel p = transparente(new BorderLayout(0, 3));
        JLabel l = new JLabel(titulo);
        rotulo(l);
        p.add(l, BorderLayout.NORTH);
        p.add(c, BorderLayout.CENTER);
        return p;
    }

    public static JPanel campo(JLabel rotulo, Component c) {
        JPanel p = transparente(new BorderLayout(0, 3));
        rotulo(rotulo);
        p.add(rotulo, BorderLayout.NORTH);
        p.add(c, BorderLayout.CENTER);
        return p;
    }

    public static Font fonte(int estilo, float tamanho) {
        return EstiloUI.FONTE.deriveFont(estilo, tamanho);
    }

    // =====================================================================
    // GRELHA RESPONSIVA (tipo CSS grid "auto-fit, minmax(min, 1fr)")
    // =====================================================================
    /**
     * Distribui os componentes em tantas colunas quantas cabem com a largura
     * mínima indicada; as células da mesma linha têm a mesma largura e a
     * altura da linha é a maior altura preferida da linha.
     */
    public static class GrelhaResponsiva implements LayoutManager {

        private final int larguraMinima;
        private final int espaco;
        private final int maxColunas;

        public GrelhaResponsiva(int larguraMinima, int espaco, int maxColunas) {
            this.larguraMinima = larguraMinima;
            this.espaco = espaco;
            this.maxColunas = maxColunas;
        }

        private int colunas(int largura, int n) {
            int c = Math.max(1, (largura + espaco) / (larguraMinima + espaco));
            return Math.max(1, Math.min(Math.min(c, maxColunas), Math.max(n, 1)));
        }

        private Component[] visiveis(Container alvo) {
            java.util.List<Component> l = new java.util.ArrayList<>();
            for (Component c : alvo.getComponents()) {
                if (c.isVisible()) {
                    l.add(c);
                }
            }
            return l.toArray(new Component[0]);
        }

        @Override
        public void addLayoutComponent(String name, Component comp) {
        }

        @Override
        public void removeLayoutComponent(Component comp) {
        }

        @Override
        public Dimension preferredLayoutSize(Container alvo) {
            synchronized (alvo.getTreeLock()) {
                Insets in = alvo.getInsets();
                int largura = larguraDisponivel(alvo) - in.left - in.right;
                Component[] cs = visiveis(alvo);
                int cols = colunas(largura, cs.length);
                int altura = 0;
                for (int i = 0; i < cs.length; i += cols) {
                    int alt = 0;
                    for (int j = i; j < Math.min(i + cols, cs.length); j++) {
                        alt = Math.max(alt, cs[j].getPreferredSize().height);
                    }
                    altura += alt + (i > 0 ? espaco : 0);
                }
                int larg = cols * larguraMinima + (cols - 1) * espaco;
                return new Dimension(larg + in.left + in.right, altura + in.top + in.bottom);
            }
        }

        @Override
        public Dimension minimumLayoutSize(Container alvo) {
            Insets in = alvo.getInsets();
            return new Dimension(larguraMinima + in.left + in.right, preferredLayoutSize(alvo).height);
        }

        @Override
        public void layoutContainer(Container alvo) {
            synchronized (alvo.getTreeLock()) {
                Insets in = alvo.getInsets();
                int largura = alvo.getWidth() - in.left - in.right;
                Component[] cs = visiveis(alvo);
                int cols = colunas(largura, cs.length);
                int larguraCelula = (largura - (cols - 1) * espaco) / cols;
                int y = in.top;
                int linhas = (cs.length + cols - 1) / Math.max(cols, 1);
                // altura extra (se houver) repartida pelas linhas
                int alturaPreferida = 0;
                int[] alturas = new int[linhas];
                for (int r = 0; r < linhas; r++) {
                    for (int j = r * cols; j < Math.min(r * cols + cols, cs.length); j++) {
                        alturas[r] = Math.max(alturas[r], cs[j].getPreferredSize().height);
                    }
                    alturaPreferida += alturas[r];
                }
                int sobra = alvo.getHeight() - in.top - in.bottom - alturaPreferida - (linhas - 1) * espaco;
                for (int r = 0; r < linhas; r++) {
                    int alt = alturas[r] + (sobra > 0 ? sobra / linhas : 0);
                    for (int j = r * cols; j < Math.min(r * cols + cols, cs.length); j++) {
                        int col = j - r * cols;
                        cs[j].setBounds(in.left + col * (larguraCelula + espaco), y, larguraCelula, alt);
                    }
                    y += alt + espaco;
                }
            }
        }
    }

    /** Largura do contentor ou, se ainda não tem tamanho, do primeiro pai que tenha. */
    static int larguraDisponivel(Container alvo) {
        int largura = alvo.getWidth();
        Container pai = alvo;
        while (largura == 0 && pai.getParent() != null) {
            pai = pai.getParent();
            largura = pai.getWidth();
        }
        return largura == 0 ? 1400 : largura;
    }

    // =====================================================================
    // COLUNAS RESPONSIVAS (3 colunas -> 2 + 1 -> empilhadas)
    // =====================================================================
    /**
     * Coloca os componentes lado a lado, com os pesos indicados, enquanto a
     * soma das larguras mínimas couber. Em ecrãs mais estreitos as primeiras
     * colunas ficam numa linha e a última passa para baixo (largura total);
     * em ecrãs muito estreitos ficam todos empilhados. Nada fica escondido:
     * a altura preferida cresce e o JScrollPane exterior permite deslizar.
     */
    public static class ColunasResponsivas implements LayoutManager {

        private final double[] pesos;
        private final int[] minimos;
        private final int espaco;

        public ColunasResponsivas(double[] pesos, int[] minimos, int espaco) {
            this.pesos = pesos;
            this.minimos = minimos;
            this.espaco = espaco;
        }

        @Override
        public void addLayoutComponent(String name, Component comp) {
        }

        @Override
        public void removeLayoutComponent(Component comp) {
        }

        private int soma(int de, int ate) {
            int s = 0;
            for (int i = de; i < ate; i++) {
                s += minimos[i];
            }
            return s + Math.max(0, ate - de - 1) * espaco;
        }

        /** Componentes por linha, conforme a largura. */
        private int[][] linhas(int largura, int n) {
            if (largura >= soma(0, n)) {
                return new int[][]{intervalo(0, n)};
            }
            if (n >= 3 && largura >= soma(0, n - 1)) {
                return new int[][]{intervalo(0, n - 1), {n - 1}};
            }
            int[][] r = new int[n][];
            for (int i = 0; i < n; i++) {
                r[i] = new int[]{i};
            }
            return r;
        }

        private static int[] intervalo(int de, int ate) {
            int[] r = new int[ate - de];
            for (int i = de; i < ate; i++) {
                r[i - de] = i;
            }
            return r;
        }

        @Override
        public Dimension preferredLayoutSize(Container alvo) {
            synchronized (alvo.getTreeLock()) {
                Insets in = alvo.getInsets();
                Component[] cs = alvo.getComponents();
                int largura = larguraDisponivel(alvo) - in.left - in.right;
                int[][] ls = linhas(largura, cs.length);
                // lado a lado: as colunas encolhem até ao mínimo (as listas e
                // tabelas deslizam por dentro); empilhadas: altura preferida
                int altura = 0;
                for (int[] linha : ls) {
                    int a = 0;
                    for (int i : linha) {
                        a = Math.max(a, ls.length == 1 ? cs[i].getMinimumSize().height : cs[i].getPreferredSize().height);
                    }
                    altura += a;
                }
                altura += (ls.length - 1) * espaco;
                return new Dimension(soma(0, cs.length) + in.left + in.right, altura + in.top + in.bottom);
            }
        }

        @Override
        public Dimension minimumLayoutSize(Container alvo) {
            synchronized (alvo.getTreeLock()) {
                Insets in = alvo.getInsets();
                int m = 0;
                for (int v : minimos) {
                    m = Math.max(m, v);
                }
                Component[] cs = alvo.getComponents();
                int largura = larguraDisponivel(alvo) - in.left - in.right;
                int[][] ls = linhas(largura, cs.length);
                int altura = 0;
                for (int[] linha : ls) {
                    int a = 0;
                    for (int i : linha) {
                        a = Math.max(a, ls.length == 1 ? cs[i].getMinimumSize().height : cs[i].getPreferredSize().height);
                    }
                    altura += a;
                }
                altura += (ls.length - 1) * espaco;
                return new Dimension(m + in.left + in.right, altura + in.top + in.bottom);
            }
        }

        @Override
        public void layoutContainer(Container alvo) {
            synchronized (alvo.getTreeLock()) {
                Insets in = alvo.getInsets();
                Component[] cs = alvo.getComponents();
                int largura = alvo.getWidth() - in.left - in.right;
                int alturaTotal = alvo.getHeight() - in.top - in.bottom;
                int[][] ls = linhas(largura, cs.length);

                int[] alturas = new int[ls.length];
                int somaAlturas = 0;
                for (int r = 0; r < ls.length; r++) {
                    for (int i : ls[r]) {
                        alturas[r] = Math.max(alturas[r], cs[i].getPreferredSize().height);
                    }
                    somaAlturas += alturas[r];
                }
                if (ls.length == 1) {
                    alturas[0] = alturaTotal;
                } else {
                    int sobra = alturaTotal - somaAlturas - (ls.length - 1) * espaco;
                    if (sobra > 0) {
                        for (int r = 0; r < ls.length; r++) {
                            alturas[r] += sobra / ls.length;
                        }
                    }
                }

                int y = in.top;
                for (int r = 0; r < ls.length; r++) {
                    int[] linha = ls[r];
                    int livre = largura - (linha.length - 1) * espaco;
                    double somaPesos = 0;
                    int somaMin = 0;
                    for (int i : linha) {
                        somaPesos += pesos[i];
                        somaMin += minimos[i];
                    }
                    int extra = Math.max(0, livre - somaMin);
                    int x = in.left;
                    for (int k = 0; k < linha.length; k++) {
                        int i = linha[k];
                        int w = k == linha.length - 1 ? in.left + largura - x
                                : minimos[i] + (int) Math.round(extra * pesos[i] / somaPesos);
                        if (linha.length == 1) {
                            w = largura;
                        }
                        cs[i].setBounds(x, y, w, alturas[r]);
                        x += w + espaco;
                    }
                    y += alturas[r] + espaco;
                }
            }
        }
    }
}
