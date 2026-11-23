package util.ui;

import comercial.controller.MesasController.EstadoMesa;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.Scrollable;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicButtonUI;

/**
 * Peças da gestão de mesas e do ecrã de pedidos (estilo POS de restauração):
 * cartões de mesa desenhados (mesa redonda com cadeiras), lugares, categorias
 * e produtos em mosaico e uma grelha deslizante que acompanha a largura.
 * As cores seguem o alojamento: verde = livre, vermelho = ocupada.
 */
public final class MesasWeb {

    public static final Color LIVRE = ComponentesWeb.VERDE;
    public static final Color OCUPADA = ComponentesWeb.VERMELHO;
    /** Pedido aberto há muito tempo (provavelmente esquecido). */
    public static final Color DEMORADA = ComponentesWeb.LARANJA;
    /** A partir de quantos minutos uma mesa aberta fica com o relógio laranja. */
    public static final long MINUTOS_DEMORA = 120;
    /** Mesa livre que já teve vendas hoje. */
    public static final Color VENDIDA = new Color(234, 179, 8);
    /** Número de vendas no dia a partir do qual a mesa livre fica amarela (basta uma). */
    public static final int VENDAS_AMARELO = 1;

    private MesasWeb() {
    }

    // =====================================================================
    // GRELHA DESLIZANTE (acompanha a largura, cresce para baixo)
    // =====================================================================
    /**
     * Painel para pôr dentro de um JScrollPane: as colunas são as que cabem
     * na largura (GrelhaResponsiva) e as linhas não esticam; o que não cabe
     * desliza na vertical.
     */
    public static class PainelGrelha extends JPanel implements Scrollable {

        public PainelGrelha(int larguraMinima, int espaco) {
            super(new ComponentesWeb.GrelhaResponsiva(larguraMinima, espaco, 99));
            setOpaque(false);
            setBorder(new EmptyBorder(4, 2, 8, 2));
            // a altura depende da largura: ao mudar a largura volta a medir
            addComponentListener(new ComponentAdapter() {
                private int larguraAnterior = -1;

                @Override
                public void componentResized(ComponentEvent e) {
                    if (getWidth() != larguraAnterior) {
                        larguraAnterior = getWidth();
                        SwingUtilities.invokeLater(() -> {
                            invalidate();
                            revalidate();
                        });
                    }
                }
            });
        }

        /** Troca a largura mínima das células (categorias e produtos têm tamanhos diferentes). */
        public void setLarguraCelula(int larguraMinima, int espaco) {
            setLayout(new ComponentesWeb.GrelhaResponsiva(larguraMinima, espaco, 99));
        }

        @Override
        public Dimension getPreferredScrollableViewportSize() {
            return getPreferredSize();
        }

        @Override
        public int getScrollableUnitIncrement(Rectangle r, int o, int d) {
            return 24;
        }

        @Override
        public int getScrollableBlockIncrement(Rectangle r, int o, int d) {
            return Math.max(24, r.height - 40);
        }

        @Override
        public boolean getScrollableTracksViewportWidth() {
            return true;
        }

        @Override
        public boolean getScrollableTracksViewportHeight() {
            return false;
        }
    }

    // =====================================================================
    // CARTÃO DE MESA (plano de sala)
    // =====================================================================
    /**
     * Mesa redonda vista de cima com quatro cadeiras. Livre: verde claro;
     * ocupada: vermelho, com itens, valor e há quanto tempo está aberta.
     */
    public static class CartaoMesa extends JComponent {

        private EstadoMesa estado;
        private final List<Runnable> accoes = new ArrayList<>();
        private boolean rato;
        private boolean premido;
        private boolean seleccionado;
        private String textoLivre = "Toque para abrir";

        public CartaoMesa(EstadoMesa estado) {
            this.estado = estado;
            setPreferredSize(new Dimension(150, 168));
            setMinimumSize(new Dimension(120, 150));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setFocusable(true);
            setFont(EstiloUI.FONTE);
            MouseAdapter m = new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    rato = true;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    rato = false;
                    premido = false;
                    repaint();
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    if (SwingUtilities.isLeftMouseButton(e) && isEnabled()) {
                        premido = true;
                        requestFocusInWindow();
                        repaint();
                    }
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    boolean clicou = premido && contains(e.getPoint());
                    premido = false;
                    repaint();
                    if (clicou) {
                        abrir();
                    }
                }
            };
            addMouseListener(m);
            addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_SPACE) {
                        abrir();
                    }
                }
            });
            addFocusListener(new java.awt.event.FocusAdapter() {
                @Override
                public void focusGained(java.awt.event.FocusEvent e) {
                    repaint();
                }

                @Override
                public void focusLost(java.awt.event.FocusEvent e) {
                    repaint();
                }
            });
            actualizarDica();
        }

        public void addAccao(Runnable r) {
            accoes.add(r);
        }

        public void abrir() {
            if (isEnabled()) {
                for (Runnable r : accoes) {
                    r.run();
                }
            }
        }

        public EstadoMesa getEstado() {
            return estado;
        }

        /** Texto do rodapé das mesas livres (por omissão "Toque para abrir"). */
        public void setTextoLivre(String t) {
            textoLivre = t;
            repaint();
        }

        /** Moldura azul fixa (ex.: mesa escolhida no cadastro de mesas). */
        public void setSeleccionado(boolean s) {
            seleccionado = s;
            repaint();
        }

        public boolean isSeleccionado() {
            return seleccionado;
        }

        public void setEstado(EstadoMesa e) {
            this.estado = e;
            actualizarDica();
            repaint();
        }

        public String getNumero() {
            return estado.getNumero();
        }

        public boolean isOcupada() {
            return estado.itens > 0;
        }

        /** Livre, mas já com vendas hoje (amarela). */
        public boolean isMuitoVendida() {
            return !isOcupada() && estado.vendasHoje >= VENDAS_AMARELO;
        }

        private String textoVendas() {
            int n = estado.vendasHoje;
            return n == 1 ? "1 venda hoje" : n + " vendas hoje";
        }

        public long minutosAberta() {
            if (!isOcupada() || estado.desde == null) {
                return -1;
            }
            return Math.max(0, (System.currentTimeMillis() - estado.desde.getTime()) / 60000L);
        }

        private void actualizarDica() {
            if (isOcupada()) {
                setToolTipText("<html><b>" + estado.designacao + "</b> · ocupada<br>"
                        + estado.itens + (estado.itens == 1 ? " item" : " itens") + " por facturar · "
                        + kitanda.util.CfMethods.formatarComoMoeda(estado.total)
                        + (estado.desde != null ? "<br>Aberta " + tempo(minutosAberta(), estado.desde) : "")
                        + (estado.vendasHoje > 0 ? "<br>" + textoVendas() + " · " + kitanda.util.CfMethods.formatarComoMoeda(estado.valorVendasHoje) : "")
                        + "<br><i>Clique para continuar o pedido · botão direito: mais opções</i></html>");
            } else {
                setToolTipText("<html><b>" + estado.designacao + "</b> · livre"
                        + (estado.vendasHoje > 0 ? "<br>" + textoVendas() + " · " + kitanda.util.CfMethods.formatarComoMoeda(estado.valorVendasHoje) : "")
                        + "<br><i>Clique para abrir um pedido · botão direito: mais opções</i></html>");
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = ComponentesWeb.suave(g);
            boolean ocupada = isOcupada();
            boolean activo = isEnabled();
            boolean vendida = isMuitoVendida();
            Color cor = !activo ? ComponentesWeb.CINZA : ocupada ? OCUPADA : vendida ? VENDIDA : LIVRE;
            int sobe = rato && !premido && activo ? 2 : 0;
            int x = 5, y = 5 - sobe, w = getWidth() - 10, h = getHeight() - 12;
            int raio = 18;

            // cartão
            ComponentesWeb.sombra(g2, x, y + sobe, w, h, raio);
            Color fundoTopo = ocupada ? new Color(255, 247, 247) : vendida ? new Color(254, 252, 232) : new Color(246, 254, 249);
            g2.setPaint(new GradientPaint(0, y, Color.WHITE, 0, y + h, premido ? ComponentesWeb.mistura(fundoTopo, cor, 0.10) : fundoTopo));
            g2.fillRoundRect(x, y, w, h, raio, raio);
            boolean destaque = (rato || isFocusOwner() || seleccionado) && activo;
            g2.setStroke(new BasicStroke(seleccionado ? 3f : destaque ? 2f : 1f));
            g2.setColor(destaque ? ComponentesWeb.AZUL_VIVO : ComponentesWeb.mistura(cor, Color.WHITE, 0.62));
            g2.drawRoundRect(x, y, w - 1, h - 1, raio, raio);
            g2.setStroke(new BasicStroke(1f));

            // faixa de estado no topo
            Shape clip = g2.getClip();
            g2.clip(new RoundRectangle2D.Double(x, y, w, h, raio, raio));
            g2.setColor(cor);
            g2.fillRect(x, y, w, 5);
            g2.setClip(clip);

            // cabeçalho: "MESA" e pílula do estado
            String txtEstado = !activo ? "Bloqueada" : ocupada ? "Ocupada" : "Livre";
            g2.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(10.5f));
            FontMetrics fe = g2.getFontMetrics();
            int pw = fe.stringWidth(txtEstado) + 20, ph = 18;
            int px = x + w - pw - 9, py = y + 11;
            // "MESA" só se couber ao lado da pílula (cartões estreitos)
            if (x + 12 + fe.stringWidth("MESA") + 6 <= px) {
                g2.setColor(ComponentesWeb.TEXTO_SUAVE);
                g2.drawString("MESA", x + 12, y + 24);
            } else {
                px = x + (w - pw) / 2;
            }
            g2.setColor(ComponentesWeb.alfa(cor, 30));
            g2.fillRoundRect(px, py, pw, ph, ph, ph);
            g2.setColor(cor);
            g2.fillOval(px + 7, py + 6, 6, 6);
            g2.setColor(cor.darker());
            g2.drawString(txtEstado, px + 15, py + 13);

            // mesa redonda com cadeiras
            int topo = y + 34, baixo = y + h - (ocupada ? 40 : 26);
            int area = Math.min(w - 30, baixo - topo);
            int cx = x + w / 2, cy = topo + (baixo - topo) / 2;
            int d = (int) (area * 0.62);
            int cadL = Math.max(14, (int) (d * 0.42)), cadA = Math.max(7, (int) (d * 0.17));
            int afast = d / 2 + cadA / 2 + 4;
            Color corCadeira = ocupada ? cor : ComponentesWeb.mistura(cor, Color.WHITE, 0.45);
            g2.setColor(corCadeira);
            g2.fillRoundRect(cx - cadL / 2, cy - afast - cadA / 2, cadL, cadA, cadA, cadA);   // cima
            g2.fillRoundRect(cx - cadL / 2, cy + afast - cadA / 2, cadL, cadA, cadA, cadA);   // baixo
            g2.fillRoundRect(cx - afast - cadA / 2, cy - cadL / 2, cadA, cadL, cadA, cadA);   // esquerda
            g2.fillRoundRect(cx + afast - cadA / 2, cy - cadL / 2, cadA, cadL, cadA, cadA);   // direita
            // sombra da mesa e tampo
            g2.setColor(new Color(15, 23, 42, 22));
            g2.fillOval(cx - d / 2 + 1, cy - d / 2 + 3, d, d);
            if (ocupada) {
                g2.setPaint(new GradientPaint(0, cy - d / 2, ComponentesWeb.mistura(cor, Color.WHITE, 0.18), 0, cy + d / 2, cor.darker()));
            } else {
                g2.setPaint(new GradientPaint(0, cy - d / 2, Color.WHITE, 0, cy + d / 2,
                        vendida ? new Color(254, 240, 138) : new Color(220, 252, 231)));
            }
            g2.fillOval(cx - d / 2, cy - d / 2, d, d);
            g2.setColor(ocupada ? cor.darker() : ComponentesWeb.mistura(cor, Color.WHITE, 0.35));
            g2.setStroke(new BasicStroke(1.6f));
            g2.drawOval(cx - d / 2, cy - d / 2, d, d);
            g2.setStroke(new BasicStroke(1f));

            // número no tampo
            String numero = getNumero();
            float tam = Math.max(13f, d * (numero.length() > 2 ? 0.30f : 0.40f));
            g2.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(tam));
            FontMetrics fn = g2.getFontMetrics();
            while (fn.stringWidth(numero) > d - 8 && tam > 10f) {
                tam -= 1f;
                g2.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(tam));
                fn = g2.getFontMetrics();
            }
            g2.setColor(ocupada ? Color.WHITE : cor.darker());
            g2.drawString(numero, cx - fn.stringWidth(numero) / 2, cy + fn.getAscent() / 2 - 2);

            // rodapé
            if (ocupada) {
                g2.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(13f));
                String valor = kitanda.util.CfMethods.formatarComoMoeda(estado.total);
                FontMetrics fv = g2.getFontMetrics();
                g2.setColor(ComponentesWeb.TEXTO);
                g2.drawString(cortar(valor, fv, w - 16), cx - Math.min(fv.stringWidth(valor), w - 16) / 2, y + h - 24);
                long min = minutosAberta();
                String linha = estado.itens + (estado.itens == 1 ? " item" : " itens")
                        + (min >= 0 ? " · " + tempoCurto(min, estado.desde) : "");
                g2.setFont(EstiloUI.FONTE.deriveFont(11f));
                FontMetrics fl = g2.getFontMetrics();
                linha = cortar(linha, fl, w - 16);
                g2.setColor(min >= MINUTOS_DEMORA ? DEMORADA.darker() : ComponentesWeb.TEXTO_SUAVE);
                g2.drawString(linha, cx - fl.stringWidth(linha) / 2, y + h - 9);
            } else {
                g2.setFont(EstiloUI.FONTE.deriveFont(11.5f));
                String linha = !activo ? "Indisponível" : estado.vendasHoje > 0 ? textoVendas() : textoLivre;
                if (estado.vendasHoje > 0) {
                    g2.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(11.5f));
                }
                FontMetrics fl = g2.getFontMetrics();
                g2.setColor(estado.vendasHoje > 0 ? cor.darker() : ComponentesWeb.TEXTO_SUAVE);
                g2.drawString(linha, cx - fl.stringWidth(linha) / 2, y + h - 10);
            }
            g2.dispose();
        }
    }

    /** "há 25 min", "há 2 h 05", "desde 20/08". */
    public static String tempo(long minutos, java.util.Date desde) {
        if (minutos < 0) {
            return "";
        }
        if (minutos < 1) {
            return "agora mesmo";
        }
        if (minutos < 60) {
            return "há " + minutos + " min";
        }
        if (minutos < 24 * 60) {
            return String.format("há %d h %02d", minutos / 60, minutos % 60);
        }
        return "desde " + new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(desde);
    }

    private static String tempoCurto(long minutos, java.util.Date desde) {
        if (minutos < 60) {
            return minutos + " min";
        }
        if (minutos < 24 * 60) {
            return String.format("%dh%02d", minutos / 60, minutos % 60);
        }
        return "desde " + new java.text.SimpleDateFormat("dd/MM").format(desde);
    }

    static String cortar(String t, FontMetrics fm, int largura) {
        if (fm.stringWidth(t) <= largura) {
            return t;
        }
        String r = t;
        while (r.length() > 1 && fm.stringWidth(r + "…") > largura) {
            r = r.substring(0, r.length() - 1);
        }
        return r + "…";
    }

    /** Até duas linhas centradas, a cortar com "…". */
    static void textoDuasLinhas(Graphics2D g2, String texto, int cx, int yTopo, int largura, Color cor) {
        FontMetrics fm = g2.getFontMetrics();
        String[] palavras = texto.trim().split("\\s+");
        StringBuilder l1 = new StringBuilder();
        int i = 0;
        for (; i < palavras.length; i++) {
            String tentativa = l1.length() == 0 ? palavras[i] : l1 + " " + palavras[i];
            if (fm.stringWidth(tentativa) > largura && l1.length() > 0) {
                break;
            }
            l1.setLength(0);
            l1.append(tentativa);
        }
        StringBuilder l2 = new StringBuilder();
        for (; i < palavras.length; i++) {
            l2.append(l2.length() == 0 ? "" : " ").append(palavras[i]);
        }
        String a = cortar(l1.toString(), fm, largura);
        String b = cortar(l2.toString(), fm, largura);
        g2.setColor(cor);
        int alt = fm.getAscent();
        if (b.isEmpty()) {
            g2.drawString(a, cx - fm.stringWidth(a) / 2, yTopo + alt + fm.getHeight() / 2);
        } else {
            g2.drawString(a, cx - fm.stringWidth(a) / 2, yTopo + alt);
            g2.drawString(b, cx - fm.stringWidth(b) / 2, yTopo + alt + fm.getHeight());
        }
    }

    // =====================================================================
    // LUGARES (no ecrã de pedidos: botão DESACTIVADO = lugar seleccionado)
    // =====================================================================
    /**
     * A lógica antiga marca o lugar escolhido desactivando o botão; este
     * aspecto mostra esse botão como seleccionado (azul) em vez de cinzento.
     * O texto ("Lugar 01") não muda.
     */
    public static void botaoLugar(AbstractButton b) {
        b.setUI(new LugarUI());
        b.setOpaque(false);
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setRolloverEnabled(true);
        b.setPreferredSize(new Dimension(52, 46));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setToolTipText("Lançar os próximos produtos para o " + b.getText().toLowerCase());
    }

    /** Propriedade do botão de lugar: número de itens por facturar nesse lugar (0 = livre). */
    public static final String LUGAR_ITENS = "lugar.itens";

    public static class LugarUI extends BasicButtonUI {

        @Override
        public void paint(Graphics g, JComponent c) {
            AbstractButton b = (AbstractButton) c;
            ButtonModel m = b.getModel();
            Graphics2D g2 = ComponentesWeb.suave(g);
            int w = c.getWidth(), h = c.getHeight();
            boolean escolhido = !b.isEnabled();
            Object itens = b.getClientProperty(LUGAR_ITENS);
            boolean ocupado = itens instanceof Number && ((Number) itens).intValue() > 0;
            if (escolhido) {
                g2.setColor(ComponentesWeb.alfa(ComponentesWeb.AZUL_VIVO, 45));
                g2.fillRoundRect(0, 2, w, h - 2, 14, 14);
                g2.setPaint(new GradientPaint(0, 0, ComponentesWeb.AZUL_VIVO, 0, h, ComponentesWeb.AZUL_MEDIO));
                g2.fillRoundRect(0, 0, w - 1, h - 3, 14, 14);
            } else if (ocupado) {
                // lugar com consumo por facturar: vermelho, como as mesas ocupadas
                g2.setColor(m.isPressed() ? ComponentesWeb.alfa(OCUPADA, 70) : ComponentesWeb.alfa(OCUPADA, m.isRollover() ? 45 : 26));
                g2.fillRoundRect(0, 0, w - 1, h - 3, 14, 14);
                g2.setColor(ComponentesWeb.alfa(OCUPADA, m.isRollover() ? 255 : 160));
                g2.drawRoundRect(0, 0, w - 2, h - 4, 14, 14);
            } else {
                g2.setColor(m.isPressed() ? new Color(219, 231, 245) : m.isRollover() ? new Color(236, 243, 252) : Color.WHITE);
                g2.fillRoundRect(0, 0, w - 1, h - 3, 14, 14);
                g2.setColor(m.isRollover() ? ComponentesWeb.AZUL_VIVO : ComponentesWeb.LINHA);
                g2.drawRoundRect(0, 0, w - 2, h - 4, 14, 14);
            }
            if (ocupado) {
                g2.setColor(escolhido ? Color.WHITE : OCUPADA);
                g2.fillOval(w - 9, 4, 6, 6);
            }
            String texto = b.getText() == null ? "" : b.getText();
            String numero = texto.replaceAll("\\D+", "");
            if (numero.length() > 1 && numero.startsWith("0")) {
                numero = numero.substring(1);
            }
            g2.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(9f));
            FontMetrics f1 = g2.getFontMetrics();
            g2.setColor(escolhido ? new Color(219, 234, 254) : ComponentesWeb.TEXTO_SUAVE);
            g2.drawString("LUGAR", (w - f1.stringWidth("LUGAR")) / 2, 14);
            g2.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(17f));
            FontMetrics f2 = g2.getFontMetrics();
            g2.setColor(escolhido ? Color.WHITE : ocupado ? OCUPADA.darker() : ComponentesWeb.AZUL);
            g2.drawString(numero, (w - f2.stringWidth(numero)) / 2, h - 10);
            g2.dispose();
        }
    }

    // =====================================================================
    // CATEGORIAS (botões de cor, como as famílias de um POS)
    // =====================================================================
    /** Cor estável para uma categoria (a mesma categoria tem sempre a mesma cor). */
    public static Color corCategoria(String nome) {
        Color[] paleta = {ComponentesWeb.AZUL_MEDIO, ComponentesWeb.VERDE, new Color(234, 88, 12),
            ComponentesWeb.ROXO, ComponentesWeb.CIANO, new Color(219, 39, 119), new Color(101, 163, 13),
            new Color(202, 138, 4), new Color(79, 70, 229), new Color(13, 148, 136), new Color(190, 18, 60)};
        int h = nome == null ? 0 : nome.trim().toUpperCase().hashCode();
        return paleta[Math.floorMod(h, paleta.length)];
    }

    /** O texto do botão continua a ser a designação (é por ele que a categoria é procurada). */
    public static void botaoCategoria(JButton b) {
        b.setUI(new CategoriaUI(corCategoria(b.getText())));
        b.setOpaque(false);
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setRolloverEnabled(true);
        b.setPreferredSize(new Dimension(150, 92));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setToolTipText("Ver os produtos de " + b.getText());
    }

    public static class CategoriaUI extends BasicButtonUI {

        private final Color cor;

        public CategoriaUI(Color cor) {
            this.cor = cor;
        }

        @Override
        public Dimension getPreferredSize(JComponent c) {
            return c.isPreferredSizeSet() ? c.getPreferredSize() : new Dimension(150, 92);
        }

        @Override
        public void paint(Graphics g, JComponent c) {
            AbstractButton b = (AbstractButton) c;
            ButtonModel m = b.getModel();
            Graphics2D g2 = ComponentesWeb.suave(g);
            int sobe = m.isRollover() && !m.isPressed() ? 2 : 0;
            int x = 4, y = 4 - sobe, w = c.getWidth() - 8, h = c.getHeight() - 10;
            ComponentesWeb.sombra(g2, x, y + sobe, w, h, 16);
            Color base = m.isPressed() ? cor.darker() : m.isRollover() ? ComponentesWeb.mistura(cor, Color.WHITE, 0.10) : cor;
            g2.setPaint(new GradientPaint(x, y, ComponentesWeb.mistura(base, Color.WHITE, 0.14), x + w, y + h, base.darker()));
            g2.fillRoundRect(x, y, w, h, 16, 16);
            // círculos decorativos
            Shape clip = g2.getClip();
            g2.clip(new RoundRectangle2D.Double(x, y, w, h, 16, 16));
            g2.setColor(new Color(255, 255, 255, 30));
            g2.fillOval(x + w - 46, y - 22, 70, 70);
            g2.setColor(new Color(255, 255, 255, 16));
            g2.fillOval(x - 20, y + h - 26, 60, 60);
            g2.setClip(clip);
            // talheres pequenos no canto
            MenuWeb.desenharSimbolo(g2, MenuWeb.Simbolo.TALHERES, x + 10, y + 9, 16, new Color(255, 255, 255, 200));
            g2.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(13.5f));
            textoDuasLinhas(g2, b.getText() == null ? "" : b.getText().toUpperCase(), x + w / 2, y + h / 2 - 12, w - 18, Color.WHITE);
            g2.dispose();
        }
    }

    // =====================================================================
    // PRODUTO (fotografia ou iniciais, nome, preço com IVA e stock)
    // =====================================================================
    public static class CartaoProduto extends JComponent {

        private final String nome;
        private final String preco;
        private final String stock;
        private final boolean semStock;
        private Image foto;
        private final Color cor;
        private Image fotoEscalada;
        private Dimension tamanhoFoto;
        private boolean rato;
        private boolean premido;

        /**
         * @param stock texto do stock (null = produto não stocável)
         */
        public CartaoProduto(String nome, String preco, String stock, boolean semStock, Image foto, Color cor, Runnable accao) {
            this.nome = nome == null ? "" : nome;
            this.preco = preco;
            this.stock = stock;
            this.semStock = semStock;
            this.foto = foto;
            this.cor = cor;
            setPreferredSize(new Dimension(160, 176));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setToolTipText("<html><b>" + this.nome + "</b><br>" + preco
                    + (stock != null ? "<br>Stock: " + stock : "") + "<br><i>Clique para lançar no pedido</i></html>");
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    rato = true;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    rato = false;
                    premido = false;
                    repaint();
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        premido = true;
                        repaint();
                    }
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    boolean clicou = premido && contains(e.getPoint());
                    premido = false;
                    repaint();
                    if (clicou && accao != null) {
                        accao.run();
                    }
                }
            });
        }

        public String getNome() {
            return nome;
        }

        /** Troca a fotografia (null = iniciais), ex.: pré-visualização no formulário de imagens. */
        public void setFoto(Image f) {
            foto = f;
            fotoEscalada = null;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = ComponentesWeb.suave(g);
            int sobe = rato && !premido ? 2 : 0;
            int x = 4, y = 4 - sobe, w = getWidth() - 8, h = getHeight() - 10;
            int raio = 16;
            ComponentesWeb.sombra(g2, x, y + sobe, w, h, raio);
            g2.setColor(premido ? new Color(241, 245, 250) : Color.WHITE);
            g2.fillRoundRect(x, y, w, h, raio, raio);

            // imagem (ou iniciais em fundo de cor)
            int ih = (int) (h * 0.52);
            Shape clip = g2.getClip();
            g2.clip(new RoundRectangle2D.Double(x, y, w, h, raio, raio));
            if (foto != null) {
                Dimension alvo = new Dimension(w, ih);
                if (fotoEscalada == null || !alvo.equals(tamanhoFoto)) {
                    tamanhoFoto = alvo;
                    int fw = Math.max(1, foto.getWidth(null)), fh = Math.max(1, foto.getHeight(null));
                    double esc = Math.max((double) w / fw, (double) ih / fh); // cobre a área toda
                    fotoEscalada = foto.getScaledInstance(Math.max(1, (int) (fw * esc)), Math.max(1, (int) (fh * esc)), Image.SCALE_SMOOTH);
                    new javax.swing.ImageIcon(fotoEscalada); // espera pelo carregamento
                }
                int fw = fotoEscalada.getWidth(null), fh = fotoEscalada.getHeight(null);
                g2.setColor(new Color(241, 245, 249));
                g2.fillRect(x, y, w, ih);
                Shape c2 = g2.getClip();
                g2.clipRect(x, y, w, ih);
                g2.drawImage(fotoEscalada, x + (w - fw) / 2, y + (ih - fh) / 2, null);
                g2.setClip(c2);
            } else {
                g2.setPaint(new GradientPaint(x, y, ComponentesWeb.mistura(cor, Color.WHITE, 0.72), x + w, y + ih, ComponentesWeb.mistura(cor, Color.WHITE, 0.50)));
                g2.fillRect(x, y, w, ih);
                g2.setColor(new Color(255, 255, 255, 60));
                g2.fillOval(x + w - 50, y - 25, 80, 80);
                String ini = iniciais(nome);
                g2.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(Math.max(18f, ih * 0.36f)));
                FontMetrics fi = g2.getFontMetrics();
                g2.setColor(cor.darker());
                g2.drawString(ini, x + (w - fi.stringWidth(ini)) / 2, y + ih / 2 + fi.getAscent() / 2 - 3);
            }
            g2.setClip(clip);

            // stock
            if (stock != null) {
                g2.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(10.5f));
                FontMetrics fs = g2.getFontMetrics();
                String t = "Stock " + stock;
                int sw = fs.stringWidth(t) + 14, sh = 18;
                Color cs = semStock ? ComponentesWeb.VERMELHO : new Color(15, 23, 42);
                g2.setColor(ComponentesWeb.alfa(cs, semStock ? 230 : 170));
                g2.fillRoundRect(x + w - sw - 7, y + 7, sw, sh, sh, sh);
                g2.setColor(Color.WHITE);
                g2.drawString(t, x + w - sw - 7 + 7, y + 7 + 13);
            }

            // nome e preço
            g2.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(12.5f));
            textoDuasLinhas(g2, nome, x + w / 2, y + ih + 6, w - 14, ComponentesWeb.TEXTO);
            g2.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(14f));
            FontMetrics fp = g2.getFontMetrics();
            String p = cortar(preco, fp, w - 40);
            g2.setColor(ComponentesWeb.AZUL_MEDIO);
            g2.drawString(p, x + 10, y + h - 11);

            // botão "+"
            int bd = 24, bx = x + w - bd - 8, by = y + h - bd - 6;
            g2.setColor(rato ? ComponentesWeb.VERDE : ComponentesWeb.alfa(ComponentesWeb.VERDE, 40));
            g2.fillOval(bx, by, bd, bd);
            g2.setColor(rato ? Color.WHITE : ComponentesWeb.VERDE.darker());
            g2.setStroke(new BasicStroke(2.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.drawLine(bx + 7, by + bd / 2, bx + bd - 7, by + bd / 2);
            g2.drawLine(bx + bd / 2, by + 7, bx + bd / 2, by + bd - 7);
            g2.setStroke(new BasicStroke(1f));

            g2.setColor(rato ? ComponentesWeb.AZUL_VIVO : ComponentesWeb.LINHA);
            g2.setStroke(new BasicStroke(rato ? 2f : 1f));
            g2.drawRoundRect(x, y, w - 1, h - 1, raio, raio);
            g2.dispose();
        }
    }

    /** "ANA MARIA DOS SANTOS" -> "Ana Santos". */
    public static String primeiroEUltimo(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            return "";
        }
        String[] p = nome.trim().split("\\s+");
        String r = p.length == 1 ? p[0] : p[0] + " " + p[p.length - 1];
        StringBuilder sb = new StringBuilder();
        for (String s : r.toLowerCase(new java.util.Locale("pt", "PT")).split(" ")) {
            if (!s.isEmpty()) {
                sb.append(sb.length() > 0 ? " " : "").append(Character.toUpperCase(s.charAt(0))).append(s.substring(1));
            }
        }
        return sb.toString();
    }

    /** Painel para pôr num JScrollPane: acompanha a largura e cresce para baixo. */
    public static class PainelLargura extends JPanel implements Scrollable {

        public PainelLargura(java.awt.LayoutManager layout) {
            super(layout);
            setOpaque(false);
        }

        @Override
        public Dimension getPreferredScrollableViewportSize() {
            return getPreferredSize();
        }

        @Override
        public int getScrollableUnitIncrement(Rectangle r, int o, int d) {
            return 20;
        }

        @Override
        public int getScrollableBlockIncrement(Rectangle r, int o, int d) {
            return Math.max(20, r.height - 20);
        }

        @Override
        public boolean getScrollableTracksViewportWidth() {
            return true;
        }

        @Override
        public boolean getScrollableTracksViewportHeight() {
            return false;
        }
    }

    static String iniciais(String nome) {
        String[] p = nome.trim().split("\\s+");
        StringBuilder sb = new StringBuilder();
        for (String s : p) {
            if (!s.isEmpty() && Character.isLetterOrDigit(s.charAt(0))) {
                sb.append(Character.toUpperCase(s.charAt(0)));
            }
            if (sb.length() == 2) {
                break;
            }
        }
        return sb.length() == 0 ? "?" : sb.toString();
    }

    // =====================================================================
    // TECLADO NUMÉRICO (acesso rápido a uma mesa, para ecrãs tácteis)
    // =====================================================================
    /** Tecla grande arredondada. */
    public static JButton tecla(String texto, ComponentesWeb.Variante v) {
        JButton b = new JButton(texto);
        ComponentesWeb.botao(b, v, null, null);
        b.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(18f));
        b.setBorder(new EmptyBorder(8, 4, 8, 4));
        b.setPreferredSize(new Dimension(56, 42));
        b.setFocusable(false);
        return b;
    }

    /** Ponto de cor da legenda. */
    public static javax.swing.JLabel legenda(String texto, Color cor) {
        javax.swing.JLabel l = new javax.swing.JLabel(texto, new ComponentesWeb.IconeCor(cor, 11), javax.swing.SwingConstants.LEFT);
        l.setIconTextGap(6);
        l.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(12f));
        l.setForeground(ComponentesWeb.TEXTO_SUAVE);
        return l;
    }

    /** Fonte maior para números (visor do total). */
    public static Font fonteVisor(float tamanho) {
        return new Font("Consolas", Font.BOLD, Math.round(tamanho));
    }

    /** Desenho em alta qualidade das fotografias reduzidas. */
    static void qualidade(Graphics2D g2) {
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    }
}
