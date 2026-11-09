package util.ui;

import java.awt.AWTEvent;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.WindowEvent;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JScrollBar;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.plaf.basic.BasicScrollBarUI;

/**
 * Peças do menu principal com aspecto de web app de hotelaria: azul-marinho e
 * dourado sobre fundo marfim, ícones vectoriais do mesmo desenho e cartões de
 * módulo iguais.
 * <p>
 * Os botões mantêm o texto original em getText() porque as permissões são
 * comparadas com ele; o que se vê no ecrã vem das propriedades "menu.*".
 */
public final class MenuWeb {

    public static Color AZUL_NOITE = new Color(0, 33, 71);
    public static Color AZUL_MARINHO = EstiloUI.AZUL_ESCURO;
    public static Color DOURADO = new Color(196, 160, 92);
    public static Color DOURADO_CLARO = new Color(228, 204, 152);
    public static final Color MARFIM = new Color(247, 245, 240);
    public static final Color LINHA_QUENTE = new Color(232, 227, 216);
    public static final Color TEXTO = ComponentesWeb.TEXTO;
    public static final Color TEXTO_SUAVE = ComponentesWeb.TEXTO_SUAVE;
    public static final Font FONTE_TITULO = new Font("Georgia", Font.PLAIN, 30);

    private static final String ROTULO = "menu.rotulo";
    private static final String DESCRICAO = "menu.descricao";
    private static final String SIMBOLO = "menu.simbolo";
    private static final String SEM_ACESSO = "Sem permissão ou módulo desactivado";

    public enum Simbolo {
        CAMA, CHAVE, CALENDARIO, SINO, TALHERES, COCKTAIL, LAVANDARIA, CARTEIRA, PRANCHETA, GRAFICO,
        // comercial e negócios (lavandaria, restaurante, farmácia, oficina)
        CARRINHO, CAIXA, PESSOAS, CABIDE, CAMISA, FERRO, FARMACIA, COMPRIMIDO, FERRAMENTA, CARRO, CHAVENA, RECIBO
    }

    private MenuWeb() {
    }

    // =====================================================================
    // ÍCONES (grelha de 24 x 24, traço único, sem preenchimentos)
    // =====================================================================
    public static void desenharSimbolo(Graphics2D origem, Simbolo s, double x, double y, double tamanho, Color cor) {
        Graphics2D g = (Graphics2D) origem.create();
        g.translate(x, y);
        g.scale(tamanho / 24.0, tamanho / 24.0);
        g.setColor(cor);
        g.setStroke(new BasicStroke(1.8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        switch (s) {
            case CAMA: {
                linha(g, 3, 5, 3, 19);
                Path2D p = new Path2D.Double();
                p.moveTo(3, 15.5);
                p.lineTo(21, 15.5);
                p.moveTo(21, 19);
                p.lineTo(21, 12.5);
                p.quadTo(21, 10, 18.5, 10);
                p.lineTo(11, 10);
                p.lineTo(11, 15.5);
                g.draw(p);
                g.draw(new Ellipse2D.Double(5.2, 9.2, 3.6, 3.6));
                break;
            }
            case CHAVE:
                g.draw(new Ellipse2D.Double(3, 11, 9.5, 9.5));
                linha(g, 11.2, 12.3, 20.5, 3);
                linha(g, 15.5, 8, 18.5, 11);
                linha(g, 18.3, 5.2, 21, 7.9);
                break;
            case CALENDARIO:
                g.draw(new RoundRectangle2D.Double(3, 4.5, 18, 16.5, 5, 5));
                linha(g, 3, 9.8, 21, 9.8);
                linha(g, 8, 2.5, 8, 6.5);
                linha(g, 16, 2.5, 16, 6.5);
                polilinha(g, 8.5, 15.3, 11, 17.8, 15.7, 13);
                break;
            case SINO:
                g.draw(new Arc2D.Double(3.5, 8.5, 17, 17, 0, 180, Arc2D.OPEN));
                linha(g, 2.5, 17, 21.5, 17);
                linha(g, 3.5, 20.5, 20.5, 20.5);
                linha(g, 12, 8.5, 12, 5.5);
                linha(g, 9.8, 5, 14.2, 5);
                break;
            case TALHERES: {
                linha(g, 4.5, 3, 4.5, 8);
                linha(g, 8, 3, 8, 21);
                linha(g, 11.5, 3, 11.5, 8);
                g.draw(new Arc2D.Double(4.5, 4.5, 7, 7, 180, 180, Arc2D.OPEN));
                Path2D faca = new Path2D.Double();
                faca.moveTo(19, 21);
                faca.lineTo(19, 3);
                faca.quadTo(15.5, 5, 15.5, 10.5);
                faca.quadTo(15.5, 14, 19, 14);
                g.draw(faca);
                break;
            }
            case COCKTAIL: {
                Path2D copo = new Path2D.Double();
                copo.moveTo(3.5, 3.5);
                copo.lineTo(20.5, 3.5);
                copo.lineTo(12, 13);
                copo.closePath();
                g.draw(copo);
                linha(g, 7, 7.4, 17, 7.4);
                linha(g, 12, 13, 12, 20.5);
                linha(g, 7.5, 20.5, 16.5, 20.5);
                break;
            }
            case LAVANDARIA: {
                g.draw(new RoundRectangle2D.Double(4, 2.5, 16, 19, 5, 5));
                g.draw(new Ellipse2D.Double(7.5, 9, 9, 9));
                linha(g, 7.5, 5.8, 7.6, 5.8);
                linha(g, 10.5, 5.8, 10.6, 5.8);
                Path2D onda = new Path2D.Double();
                onda.moveTo(7.8, 13.5);
                onda.quadTo(10, 11.5, 12, 13.5);
                onda.quadTo(14, 15.5, 16.2, 13.5);
                g.draw(onda);
                break;
            }
            case CARTEIRA:
                g.draw(new RoundRectangle2D.Double(3, 6.5, 18, 14, 5, 5));
                polilinha(g, 5.5, 6.5, 16, 3, 17.5, 6.5);
                g.draw(new RoundRectangle2D.Double(14.5, 11.3, 6.5, 4.5, 3, 3));
                break;
            case PRANCHETA:
                g.draw(new RoundRectangle2D.Double(4.5, 4.5, 15, 17, 4.5, 4.5));
                g.draw(new RoundRectangle2D.Double(8.8, 2.5, 6.4, 4, 2.5, 2.5));
                polilinha(g, 8, 11.3, 9.3, 12.6, 11.3, 10.3);
                linha(g, 13.3, 11.6, 16.5, 11.6);
                polilinha(g, 8, 16.3, 9.3, 17.6, 11.3, 15.3);
                linha(g, 13.3, 16.6, 16.5, 16.6);
                break;
            case GRAFICO:
                polilinha(g, 3, 3, 3, 21, 21, 21);
                linha(g, 8, 17, 8, 12);
                linha(g, 13, 17, 13, 6.5);
                linha(g, 18, 17, 18, 9.5);
                break;
            case CARRINHO: {
                polilinha(g, 2.5, 4, 5.5, 4, 8, 15.5, 18.5, 15.5, 20.5, 7.5, 6.5, 7.5);
                g.draw(new Ellipse2D.Double(8, 17.5, 3, 3));
                g.draw(new Ellipse2D.Double(16, 17.5, 3, 3));
                break;
            }
            case CAIXA: {
                Path2D p = new Path2D.Double();
                p.moveTo(3, 7.5);
                p.lineTo(12, 3);
                p.lineTo(21, 7.5);
                p.lineTo(21, 17);
                p.lineTo(12, 21.5);
                p.lineTo(3, 17);
                p.closePath();
                g.draw(p);
                polilinha(g, 3, 7.5, 12, 12, 21, 7.5);
                linha(g, 12, 12, 12, 21.5);
                linha(g, 7.5, 5.2, 16.5, 9.8);
                break;
            }
            case PESSOAS: {
                g.draw(new Ellipse2D.Double(5, 4, 6, 6));
                g.draw(new Arc2D.Double(2.5, 12, 11, 12, 0, 180, Arc2D.OPEN));
                g.draw(new Ellipse2D.Double(14, 6, 5, 5));
                g.draw(new Arc2D.Double(12.5, 13, 9, 10, 0, 180, Arc2D.OPEN));
                break;
            }
            case CABIDE: {
                Path2D p = new Path2D.Double();
                p.moveTo(10, 6);
                p.quadTo(10, 3.5, 12, 3.5);
                p.quadTo(14.3, 3.5, 14.3, 6);
                p.quadTo(14.3, 8, 12, 9);
                p.lineTo(12, 10.5);
                p.lineTo(2.5, 17.5);
                p.quadTo(1.8, 19.5, 4, 19.5);
                p.lineTo(20, 19.5);
                p.quadTo(22.2, 19.5, 21.5, 17.5);
                p.lineTo(12, 10.5);
                g.draw(p);
                break;
            }
            case CAMISA: {
                Path2D p = new Path2D.Double();
                p.moveTo(8.5, 3);
                p.lineTo(3, 6);
                p.lineTo(4.8, 10.5);
                p.lineTo(7, 9.5);
                p.lineTo(7, 21);
                p.lineTo(17, 21);
                p.lineTo(17, 9.5);
                p.lineTo(19.2, 10.5);
                p.lineTo(21, 6);
                p.lineTo(15.5, 3);
                p.quadTo(12, 6.5, 8.5, 3);
                g.draw(p);
                break;
            }
            case FERRO: {
                Path2D p = new Path2D.Double();
                p.moveTo(2.5, 18.5);
                p.lineTo(21.5, 18.5);
                p.lineTo(21.5, 13);
                p.quadTo(21.5, 8, 15, 8);
                p.lineTo(8, 8);
                p.quadTo(3.5, 10, 2.5, 18.5);
                g.draw(p);
                polilinha(g, 9, 8, 10, 4.5, 19, 4.5, 20, 8);
                linha(g, 7, 15, 7.1, 15);
                linha(g, 11, 15, 11.1, 15);
                linha(g, 15, 15, 15.1, 15);
                break;
            }
            case FARMACIA: {
                Path2D p = new Path2D.Double();
                p.moveTo(9, 3);
                p.lineTo(15, 3);
                p.lineTo(15, 9);
                p.lineTo(21, 9);
                p.lineTo(21, 15);
                p.lineTo(15, 15);
                p.lineTo(15, 21);
                p.lineTo(9, 21);
                p.lineTo(9, 15);
                p.lineTo(3, 15);
                p.lineTo(3, 9);
                p.lineTo(9, 9);
                p.closePath();
                g.draw(p);
                break;
            }
            case COMPRIMIDO: {
                Graphics2D r = (Graphics2D) g.create();
                r.rotate(Math.toRadians(-45), 12, 12);
                r.draw(new RoundRectangle2D.Double(3, 8, 18, 8, 8, 8));
                r.draw(new Line2D.Double(12, 8, 12, 16));
                r.dispose();
                break;
            }
            case FERRAMENTA: {
                Path2D p = new Path2D.Double();
                p.moveTo(14.5, 3.2);
                p.quadTo(11, 4.5, 11.5, 8.5);
                p.lineTo(3.5, 16.5);
                p.quadTo(2.5, 19.5, 5, 20.5);
                p.quadTo(6.5, 21.3, 7.5, 20.5);
                p.lineTo(15.5, 12.5);
                p.quadTo(19.5, 13, 20.8, 9.5);
                p.lineTo(18, 9.8);
                p.lineTo(16, 8);
                p.lineTo(16.2, 5.6);
                p.closePath();
                g.draw(p);
                break;
            }
            case CARRO: {
                Path2D p = new Path2D.Double();
                p.moveTo(2.5, 16.5);
                p.lineTo(2.5, 12.5);
                p.lineTo(5, 11.5);
                p.lineTo(7.5, 6.5);
                p.lineTo(16.5, 6.5);
                p.lineTo(19, 11.5);
                p.lineTo(21.5, 12.5);
                p.lineTo(21.5, 16.5);
                p.closePath();
                g.draw(p);
                linha(g, 5, 11.5, 19, 11.5);
                g.draw(new Ellipse2D.Double(5, 15, 4, 4));
                g.draw(new Ellipse2D.Double(15, 15, 4, 4));
                break;
            }
            case CHAVENA: {
                Path2D p = new Path2D.Double();
                p.moveTo(3.5, 9);
                p.lineTo(16.5, 9);
                p.lineTo(16.5, 14);
                p.quadTo(16.5, 19.5, 10, 19.5);
                p.quadTo(3.5, 19.5, 3.5, 14);
                p.closePath();
                g.draw(p);
                g.draw(new Arc2D.Double(14, 10, 7, 6, -90, 180, Arc2D.OPEN));
                linha(g, 2.5, 21.5, 17.5, 21.5);
                g.draw(new Arc2D.Double(6.5, 3, 3, 4, 90, 180, Arc2D.OPEN));
                g.draw(new Arc2D.Double(10.5, 3, 3, 4, 90, 180, Arc2D.OPEN));
                break;
            }
            case RECIBO: {
                Path2D p = new Path2D.Double();
                p.moveTo(5, 2.5);
                p.lineTo(19, 2.5);
                p.lineTo(19, 21.5);
                p.lineTo(16.7, 20);
                p.lineTo(14.3, 21.5);
                p.lineTo(12, 20);
                p.lineTo(9.7, 21.5);
                p.lineTo(7.3, 20);
                p.lineTo(5, 21.5);
                p.closePath();
                g.draw(p);
                linha(g, 8.5, 7.5, 15.5, 7.5);
                linha(g, 8.5, 11.5, 15.5, 11.5);
                linha(g, 8.5, 15.5, 12.5, 15.5);
                break;
            }
            default:
                break;
        }
        g.dispose();
    }

    private static void linha(Graphics2D g, double x1, double y1, double x2, double y2) {
        g.draw(new Line2D.Double(x1, y1, x2, y2));
    }

    private static void polilinha(Graphics2D g, double... xy) {
        Path2D p = new Path2D.Double();
        p.moveTo(xy[0], xy[1]);
        for (int i = 2; i < xy.length; i += 2) {
            p.lineTo(xy[i], xy[i + 1]);
        }
        g.draw(p);
    }

    // =====================================================================
    // ÍCONE DA APLICAÇÃO (substitui a chávena do Java)
    // =====================================================================
    /**
     * Dá o ícone da aplicação a todas as janelas que não tenham um próprio.
     * Chamar uma vez no arranque.
     */
    public static void instalarIconeAplicacao() {
        final List<Image> icones = iconesAplicacao();
        Toolkit.getDefaultToolkit().addAWTEventListener(e -> {
            if (e.getID() == WindowEvent.WINDOW_OPENED) {
                Window janela = ((WindowEvent) e).getWindow();
                if ((janela instanceof Frame || janela instanceof Dialog) && janela.getIconImages().isEmpty()) {
                    janela.setIconImages(icones);
                }
            }
        }, AWTEvent.WINDOW_EVENT_MASK);
    }

    /** Vários tamanhos: o Windows escolhe o da barra de título e o da barra de tarefas. */
    public static List<Image> iconesAplicacao() {
        List<Image> icones = new ArrayList<>();
        for (int t : new int[]{16, 20, 24, 32, 40, 48, 64, 128, 256}) {
            BufferedImage img = new BufferedImage(t, t, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = ComponentesWeb.suave(img.getGraphics());
            pintarLogotipo(g2, t);
            g2.dispose();
            icones.add(img);
        }
        return icones;
    }

    private static void pintarLogotipo(Graphics2D g2, int t) {
        pintarLogotipo(g2, t, Tema.simbolo());
    }

    private static void pintarLogotipo(Graphics2D g2, int t, Simbolo simbolo) {
        g2.setPaint(new GradientPaint(0, 0, DOURADO_CLARO, t, t, DOURADO));
        g2.fillRoundRect(0, 0, t, t, Math.round(t * 0.28f), Math.round(t * 0.28f));
        desenharSimbolo(g2, simbolo, t * 0.11, t * 0.1, t * 0.78, AZUL_NOITE);
    }

    /** Logótipo da barra lateral: carteira num quadrado dourado. */
    public static class Logotipo extends JComponent {

        private Simbolo simbolo = Tema.simbolo();

        /** Logótipo com o símbolo do negócio (lavandaria, restaurante, farmácia...). */
        public Logotipo(int tamanho, Simbolo simbolo) {
            this(tamanho);
            if (simbolo != null) {
                this.simbolo = simbolo;
            }
        }

        public Logotipo(int tamanho) {
            Dimension d = new Dimension(tamanho, tamanho);
            setPreferredSize(d);
            setMinimumSize(d);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = ComponentesWeb.suave(g);
            pintarLogotipo(g2, Math.min(getWidth(), getHeight()), simbolo);
            g2.dispose();
        }
    }

    /**
     * Nome do software em destaque: letras espaçadas, gradiente vertical e uma
     * sombra leve, com a versão pequena à direita (pode ser vazia).
     */
    public static class MarcaSoftware extends JComponent {

        private final String nome;
        private final String versao;
        private final Font fonte;
        private final Font fonteVersao;
        private final int espaco;
        private final Color topo;
        private final Color base;
        private final Color corVersao;
        private final boolean sombra;

        public MarcaSoftware(String nome, String versao, float tamanho, Color topo, Color base, Color corVersao, boolean sombra) {
            this.nome = nome;
            this.versao = versao == null ? "" : versao;
            this.fonte = new Font("Georgia", Font.BOLD, 10).deriveFont(tamanho);
            this.fonteVersao = EstiloUI.FONTE_NEGRITO.deriveFont(Math.max(11f, tamanho * 0.3f));
            this.espaco = Math.round(tamanho * 0.12f);
            this.topo = topo;
            this.base = base;
            this.corVersao = corVersao;
            this.sombra = sombra;
            Dimension d = medir();
            setPreferredSize(d);
            setMinimumSize(new Dimension(10, d.height)); // em pouco espaço encolhe, não corta
            setToolTipText((nome + " " + this.versao).trim());
        }

        private int larguraNome(FontMetrics fm) {
            int w = 0;
            for (int i = 0; i < nome.length(); i++) {
                w += fm.charWidth(nome.charAt(i)) + espaco;
            }
            return w;
        }

        private Dimension medir() {
            FontMetrics fm = getFontMetrics(fonte);
            FontMetrics fv = getFontMetrics(fonteVersao);
            int largura = larguraNome(fm) + (versao.isEmpty() ? 0 : 4 + fv.stringWidth(versao)) + 3;
            return new Dimension(largura, fm.getAscent() + fm.getDescent() + 3);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = ComponentesWeb.suave(g);
            double escala = Math.min(1, getWidth() / (double) getPreferredSize().width);
            g2.scale(escala, escala);
            FontMetrics fm = g2.getFontMetrics(fonte);
            int baseLinha = fm.getAscent();
            g2.setFont(fonte);
            if (sombra) {
                g2.setColor(new Color(0, 0, 0, 90));
                desenharNome(g2, fm, 2, baseLinha + 2);
            }
            g2.setPaint(new GradientPaint(0, baseLinha - fm.getAscent() * 0.8f, topo, 0, baseLinha, base));
            int x = desenharNome(g2, fm, 0, baseLinha);
            if (!versao.isEmpty()) {
                g2.setFont(fonteVersao);
                g2.setColor(corVersao);
                g2.drawString(versao, x + 2, baseLinha);
            }
            g2.dispose();
        }

        private int desenharNome(Graphics2D g2, FontMetrics fm, int x, int y) {
            for (int i = 0; i < nome.length(); i++) {
                String letra = String.valueOf(nome.charAt(i));
                g2.drawString(letra, x, y);
                x += fm.charWidth(nome.charAt(i)) + espaco;
            }
            return x;
        }
    }

    /** Círculo com as iniciais do utilizador; lê o nome do rótulo indicado. */
    public static class Avatar extends JComponent {

        private final JLabel origem;

        public Avatar(JLabel origem, int tamanho) {
            this.origem = origem;
            Dimension d = new Dimension(tamanho, tamanho);
            setPreferredSize(d);
            setMinimumSize(d);
        }

        private String iniciais() {
            String nome = origem.getText() == null ? "" : origem.getText().trim();
            if (nome.isEmpty()) {
                return "?";
            }
            String[] partes = nome.split("\\s+");
            String r = partes[0].substring(0, 1);
            if (partes.length > 1) {
                r += partes[partes.length - 1].substring(0, 1);
            }
            return r.toUpperCase();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = ComponentesWeb.suave(g);
            int t = Math.min(getWidth(), getHeight());
            g2.setColor(DOURADO);
            g2.fillOval(0, 0, t - 1, t - 1);
            g2.setColor(AZUL_NOITE);
            g2.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(13f));
            FontMetrics fm = g2.getFontMetrics();
            String s = iniciais();
            g2.drawString(s, (t - fm.stringWidth(s)) / 2, (t - fm.getHeight()) / 2 + fm.getAscent());
            g2.dispose();
        }
    }

    // =====================================================================
    // CARTÃO DE MÓDULO
    // =====================================================================
    /**
     * Transforma o botão num cartão de módulo. Todos têm a mesma estrutura:
     * quadrado com o ícone, título e descrição. O texto do botão não muda.
     */
    public static void cartaoModulo(JButton b, Simbolo simbolo, String titulo, String descricao, int largura, int altura) {
        b.putClientProperty(SIMBOLO, simbolo);
        b.putClientProperty(ROTULO, titulo);
        b.putClientProperty(DESCRICAO, descricao);
        prepararBotao(b, new CartaoModuloUI(), titulo + " - " + descricao);
        b.setPreferredSize(new Dimension(largura, altura));
        b.setMinimumSize(new Dimension(largura, altura));
    }

    private static void prepararBotao(final AbstractButton b, BasicButtonUI ui, final String dica) {
        b.setUI(ui);
        b.setOpaque(false);
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setRolloverEnabled(true);
        // o estado pode já ter sido decidido (permissões aplicadas antes do layout)
        b.setToolTipText(b.isEnabled() ? dica : SEM_ACESSO);
        b.setCursor(Cursor.getPredefinedCursor(b.isEnabled() ? Cursor.HAND_CURSOR : Cursor.DEFAULT_CURSOR));
        b.addPropertyChangeListener("enabled", e -> {
            boolean activo = b.isEnabled();
            b.setToolTipText(activo ? dica : SEM_ACESSO);
            b.setCursor(Cursor.getPredefinedCursor(activo ? Cursor.HAND_CURSOR : Cursor.DEFAULT_CURSOR));
        });
    }

    private static class CartaoModuloUI extends BasicButtonUI {

        private static final int ARCO = 18;
        private static final int MARGEM = 18;
        private static final String TITULO_MAIS_LONGO = "Alojamento (Check In)";

        @Override
        public void paint(Graphics g, JComponent c) {
            AbstractButton b = (AbstractButton) c;
            ButtonModel m = b.getModel();
            boolean activo = b.isEnabled();
            boolean sobre = activo && m.isRollover();
            Graphics2D g2 = ComponentesWeb.suave(g);

            // espaço à volta para a sombra; ao passar o rato o cartão sobe
            int x = 4, w = c.getWidth() - 9;
            int y = sobre && !m.isPressed() ? 2 : 4;
            int h = c.getHeight() - 12;
            if (activo) {
                ComponentesWeb.sombra(g2, x, y, w, h, ARCO);
                if (sobre) {
                    ComponentesWeb.sombra(g2, x, y + 2, w, h, ARCO);
                }
            }
            g2.setColor(activo ? Color.WHITE : new Color(244, 243, 240));
            g2.fillRoundRect(x, y, w, h, ARCO, ARCO);
            g2.setColor(sobre || b.isFocusOwner() ? DOURADO : LINHA_QUENTE);
            g2.setStroke(new BasicStroke(sobre || b.isFocusOwner() ? 1.6f : 1f));
            g2.drawRoundRect(x, y, w - 1, h - 1, ARCO, ARCO);

            // conteúdo centrado na vertical; o ícone cresce com o cartão
            int interior = w - 2 * MARGEM;
            int tile = Math.max(50, Math.min(68, h / 3));
            Font fTitulo = fonteTitulo(g2, interior);
            Font fDesc = EstiloUI.FONTE.deriveFont(12.5f);
            FontMetrics fmT = g2.getFontMetrics(fTitulo);
            FontMetrics fmD = g2.getFontMetrics(fDesc);
            List<String> linhas = quebrar((String) b.getClientProperty(DESCRICAO), fmD, interior, 2);
            int total = tile + 14 + fmT.getHeight() + 3 + 2 * fmD.getHeight();
            int cy = y + Math.max(8, (h - total) / 2);
            int tx = x + MARGEM;

            if (!activo) {
                g2.setColor(new Color(224, 224, 222));
            } else if (sobre) {
                g2.setPaint(new GradientPaint(tx, cy, DOURADO_CLARO, tx + tile, cy + tile, DOURADO));
            } else {
                g2.setPaint(new GradientPaint(tx, cy, AZUL_MARINHO, tx + tile, cy + tile, AZUL_NOITE));
            }
            g2.fillRoundRect(tx, cy, tile, tile, 14, 14);
            Color corIcone = !activo ? new Color(150, 150, 150) : sobre ? AZUL_NOITE : DOURADO_CLARO;
            double icone = tile * 0.58;
            desenharSimbolo(g2, (Simbolo) b.getClientProperty(SIMBOLO),
                    tx + (tile - icone) / 2, cy + (tile - icone) / 2, icone, corIcone);

            if (activo) {
                // seta "entrar", alinhada com o ícone
                g2.setColor(sobre ? DOURADO : new Color(205, 200, 190));
                g2.setStroke(new BasicStroke(1.8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                int sx = x + w - MARGEM - 12, sy = cy + tile / 2;
                g2.drawLine(sx, sy, sx + 12, sy);
                g2.drawPolyline(new int[]{sx + 7, sx + 12, sx + 7}, new int[]{sy - 5, sy, sy + 5}, 3);
            }

            int ty = cy + tile + 14;
            g2.setFont(fTitulo);
            g2.setColor(activo ? AZUL_NOITE : new Color(150, 150, 150));
            g2.drawString((String) b.getClientProperty(ROTULO), tx, ty + fmT.getAscent());
            ty += fmT.getHeight() + 3;
            g2.setFont(fDesc);
            g2.setColor(activo ? TEXTO_SUAVE : new Color(165, 165, 165));
            for (String l : linhas) {
                g2.drawString(l, tx, ty + fmD.getAscent());
                ty += fmD.getHeight();
            }
            g2.dispose();
        }

        /**
         * 15,5 pt; em cartões estreitos desce até 12,5 pt. O tamanho é medido
         * com o título mais longo para ser igual em todos os cartões.
         */
        private Font fonteTitulo(Graphics2D g2, int largura) {
            float tam = 15.5f;
            Font f = EstiloUI.FONTE_NEGRITO.deriveFont(tam);
            while (tam > 12.5f && g2.getFontMetrics(f).stringWidth(TITULO_MAIS_LONGO) > largura) {
                tam -= 0.5f;
                f = EstiloUI.FONTE_NEGRITO.deriveFont(tam);
            }
            return f;
        }

        @Override
        protected void paintFocus(Graphics g, AbstractButton b, Rectangle vr, Rectangle tr, Rectangle ir) {
        }
    }

    // =====================================================================
    // CARTÃO COMPACTO (listas grandes de opções, ex.: relatórios)
    // =====================================================================
    /** Como o cartão de módulo, mais baixo: ícone à esquerda e título até 2 linhas. */
    public static void cartaoCompacto(JButton b, Simbolo simbolo, String titulo, int largura, int altura) {
        b.putClientProperty(SIMBOLO, simbolo);
        b.putClientProperty(ROTULO, titulo);
        prepararBotao(b, new CartaoCompactoUI(), titulo);
        b.setPreferredSize(new Dimension(largura, altura));
        b.setMinimumSize(new Dimension(largura, altura));
    }

    private static class CartaoCompactoUI extends BasicButtonUI {

        private static final int ARCO = 14;
        private static final int TILE = 36;

        @Override
        public void paint(Graphics g, JComponent c) {
            AbstractButton b = (AbstractButton) c;
            ButtonModel m = b.getModel();
            boolean activo = b.isEnabled();
            boolean sobre = activo && m.isRollover();
            Graphics2D g2 = ComponentesWeb.suave(g);

            int x = 3, w = c.getWidth() - 7;
            int y = sobre && !m.isPressed() ? 2 : 3;
            int h = c.getHeight() - 8;
            if (activo) {
                ComponentesWeb.sombra(g2, x, y, w, h, ARCO);
            }
            g2.setColor(activo ? Color.WHITE : new Color(244, 243, 240));
            g2.fillRoundRect(x, y, w, h, ARCO, ARCO);
            g2.setColor(sobre || b.isFocusOwner() ? DOURADO : LINHA_QUENTE);
            g2.setStroke(new BasicStroke(sobre || b.isFocusOwner() ? 1.6f : 1f));
            g2.drawRoundRect(x, y, w - 1, h - 1, ARCO, ARCO);

            int tx = x + 10, ty = y + (h - TILE) / 2;
            if (!activo) {
                g2.setColor(new Color(224, 224, 222));
            } else if (sobre) {
                g2.setPaint(new GradientPaint(tx, ty, DOURADO_CLARO, tx + TILE, ty + TILE, DOURADO));
            } else {
                g2.setPaint(new GradientPaint(tx, ty, AZUL_MARINHO, tx + TILE, ty + TILE, AZUL_NOITE));
            }
            g2.fillRoundRect(tx, ty, TILE, TILE, 10, 10);
            double icone = TILE * 0.6;
            desenharSimbolo(g2, (Simbolo) b.getClientProperty(SIMBOLO), tx + (TILE - icone) / 2, ty + (TILE - icone) / 2, icone,
                    !activo ? new Color(150, 150, 150) : sobre ? AZUL_NOITE : DOURADO_CLARO);

            int textoX = tx + TILE + 10;
            g2.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(13f));
            FontMetrics fm = g2.getFontMetrics();
            List<String> linhas = quebrar((String) b.getClientProperty(ROTULO), fm, x + w - 10 - textoX, 2);
            int linhaY = y + (h - linhas.size() * fm.getHeight()) / 2 + fm.getAscent();
            g2.setColor(activo ? AZUL_NOITE : new Color(150, 150, 150));
            for (String l : linhas) {
                g2.drawString(l, textoX, linhaY);
                linhaY += fm.getHeight();
            }
            g2.dispose();
        }

        @Override
        protected void paintFocus(Graphics g, AbstractButton b, Rectangle vr, Rectangle tr, Rectangle ir) {
        }
    }

    /** Quebra por palavras até ao máximo de linhas; o excesso termina em "…". */
    static List<String> quebrar(String texto, FontMetrics fm, int largura, int maxLinhas) {
        if (texto == null || texto.isEmpty()) {
            return Collections.emptyList();
        }
        List<String> linhas = new ArrayList<>();
        String actual = "";
        String[] palavras = texto.split(" ");
        for (int i = 0; i < palavras.length; i++) {
            String tentativa = actual.isEmpty() ? palavras[i] : actual + " " + palavras[i];
            if (fm.stringWidth(tentativa) <= largura || actual.isEmpty()) {
                actual = tentativa;
                continue;
            }
            if (linhas.size() == maxLinhas - 1) {
                actual = tentativa;
                break;
            }
            linhas.add(actual);
            actual = palavras[i];
        }
        boolean cortado = linhas.size() == maxLinhas - 1 && fm.stringWidth(actual) > largura;
        while (fm.stringWidth(actual + (cortado ? "…" : "")) > largura && actual.length() > 1) {
            actual = actual.substring(0, actual.length() - 1);
        }
        linhas.add(cortado ? actual.trim() + "…" : actual);
        return linhas;
    }

    // =====================================================================
    // BARRA LATERAL
    // =====================================================================
    /** Linha de navegação da barra lateral. */
    public static void itemLateral(AbstractButton b, String rotulo) {
        b.putClientProperty(ROTULO, rotulo);
        prepararBotao(b, new ItemLateralUI(), null);
        b.setAlignmentX(Component.LEFT_ALIGNMENT);
        b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));
        b.setMinimumSize(new Dimension(100, 28));
        b.setPreferredSize(new Dimension(200, 28));
    }

    private static class ItemLateralUI extends BasicButtonUI {

        @Override
        public void paint(Graphics g, JComponent c) {
            AbstractButton b = (AbstractButton) c;
            ButtonModel m = b.getModel();
            Graphics2D g2 = ComponentesWeb.suave(g);
            int w = c.getWidth(), h = c.getHeight();
            boolean sobre = b.isEnabled() && m.isRollover();
            if (sobre || b.isFocusOwner()) {
                g2.setColor(new Color(255, 255, 255, m.isPressed() ? 40 : 24));
                g2.fillRoundRect(0, 1, w - 1, h - 2, 10, 10);
                g2.setColor(DOURADO);
                g2.fillRoundRect(0, 7, 3, h - 14, 3, 3);
            }
            g2.setFont(EstiloUI.FONTE.deriveFont(13f));
            g2.setColor(!b.isEnabled() ? new Color(255, 255, 255, 95)
                    : sobre ? Color.WHITE : new Color(220, 228, 240));
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString((String) b.getClientProperty(ROTULO), 12, (h - fm.getHeight()) / 2 + fm.getAscent());
            g2.dispose();
        }

        @Override
        protected void paintFocus(Graphics g, AbstractButton b, Rectangle vr, Rectangle tr, Rectangle ir) {
        }
    }

    /** Botão de contorno claro, para o fundo azul da barra lateral (Sair, Fechar). */
    public static void botaoContorno(AbstractButton b, String rotulo, String dica) {
        b.putClientProperty(ROTULO, rotulo);
        prepararBotao(b, new ContornoUI(), dica);
        b.setPreferredSize(new Dimension(90, 32));
    }

    private static class ContornoUI extends BasicButtonUI {

        @Override
        public void paint(Graphics g, JComponent c) {
            AbstractButton b = (AbstractButton) c;
            ButtonModel m = b.getModel();
            Graphics2D g2 = ComponentesWeb.suave(g);
            int w = c.getWidth(), h = c.getHeight();
            boolean destaque = m.isRollover() || b.isFocusOwner();
            if (destaque) {
                g2.setColor(m.isPressed() ? DOURADO : DOURADO_CLARO);
                g2.fillRoundRect(0, 0, w - 1, h - 1, 10, 10);
            } else {
                g2.setColor(new Color(255, 255, 255, 90));
                g2.drawRoundRect(0, 0, w - 1, h - 1, 10, 10);
            }
            g2.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(12.5f));
            g2.setColor(destaque ? AZUL_NOITE : Color.WHITE);
            FontMetrics fm = g2.getFontMetrics();
            String s = (String) b.getClientProperty(ROTULO);
            g2.drawString(s, (w - fm.stringWidth(s)) / 2, (h - fm.getHeight()) / 2 + fm.getAscent());
            g2.dispose();
        }

        @Override
        protected void paintFocus(Graphics g, AbstractButton b, Rectangle vr, Rectangle tr, Rectangle ir) {
        }
    }

    // =====================================================================
    // CABEÇALHO DE GRUPO EXPANSÍVEL (barra lateral)
    // =====================================================================
    private static final String ABERTO = "menu.aberto";

    /**
     * Cabeçalho de grupo da barra lateral que abre/fecha os itens: título em
     * maiúsculas (como o rotuloGrupo) e uma seta à direita (▸ fechado, ▾ aberto).
     */
    public static void cabecalhoGrupo(AbstractButton b, String titulo) {
        b.putClientProperty(ROTULO, titulo.toUpperCase());
        b.putClientProperty(ABERTO, Boolean.FALSE);
        prepararBotao(b, new CabecalhoGrupoUI(), null);
        b.setToolTipText("Clique para mostrar / esconder as opções de " + titulo);
        b.setAlignmentX(Component.LEFT_ALIGNMENT);
        b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        b.setMinimumSize(new Dimension(100, 30));
        b.setPreferredSize(new Dimension(200, 30));
    }

    public static void cabecalhoGrupoAberto(AbstractButton b, boolean aberto) {
        b.putClientProperty(ABERTO, aberto);
        b.repaint();
    }

    private static class CabecalhoGrupoUI extends BasicButtonUI {

        @Override
        public void paint(Graphics g, JComponent c) {
            AbstractButton b = (AbstractButton) c;
            ButtonModel m = b.getModel();
            Graphics2D g2 = ComponentesWeb.suave(g);
            int w = c.getWidth(), h = c.getHeight();
            boolean aberto = Boolean.TRUE.equals(b.getClientProperty(ABERTO));
            boolean sobre = m.isRollover() || b.isFocusOwner();
            if (sobre || aberto) {
                g2.setColor(new Color(255, 255, 255, m.isPressed() ? 34 : sobre ? 22 : 12));
                g2.fillRoundRect(0, 1, w - 1, h - 2, 10, 10);
            }
            java.util.Map<TextAttribute, Object> atributos = new java.util.HashMap<>();
            atributos.put(TextAttribute.TRACKING, 0.09f);
            g2.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(11.5f).deriveFont(atributos));
            g2.setColor(sobre || aberto ? DOURADO_CLARO : DOURADO);
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString((String) b.getClientProperty(ROTULO), 12, (h - fm.getHeight()) / 2 + fm.getAscent());

            // seta: ▸ fechado, ▾ aberto
            g2.setStroke(new BasicStroke(1.8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            int cx = w - 18, cy = h / 2;
            if (aberto) {
                g2.drawPolyline(new int[]{cx - 4, cx, cx + 4}, new int[]{cy - 2, cy + 2, cy - 2}, 3);
            } else {
                g2.drawPolyline(new int[]{cx - 2, cx + 2, cx - 2}, new int[]{cy - 4, cy, cy + 4}, 3);
            }
            g2.dispose();
        }

        @Override
        protected void paintFocus(Graphics g, AbstractButton b, Rectangle vr, Rectangle tr, Rectangle ir) {
        }
    }

    /** Título de grupo: pequeno, em maiúsculas e com letras espaçadas. */
    public static JLabel rotuloGrupo(String texto, Color cor) {
        JLabel l = new JLabel(texto.toUpperCase());
        java.util.Map<TextAttribute, Object> atributos = new java.util.HashMap<>();
        atributos.put(TextAttribute.TRACKING, 0.09f);
        l.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(11f).deriveFont(atributos));
        l.setForeground(cor);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    /** Barra de deslocamento fina, para o fundo azul. */
    public static void barraFina(JScrollBar barra) {
        barra.setOpaque(false);
        barra.setPreferredSize(new Dimension(7, 0));
        barra.setUI(new BasicScrollBarUI() {
            @Override
            protected void paintTrack(Graphics g, JComponent c, Rectangle r) {
            }

            @Override
            protected void paintThumb(Graphics g, JComponent c, Rectangle r) {
                Graphics2D g2 = ComponentesWeb.suave(g);
                g2.setColor(new Color(255, 255, 255, isDragging ? 120 : 70));
                g2.fillRoundRect(r.x + 1, r.y, r.width - 2, r.height, 5, 5);
                g2.dispose();
            }

            @Override
            protected JButton createDecreaseButton(int o) {
                return semBotao();
            }

            @Override
            protected JButton createIncreaseButton(int o) {
                return semBotao();
            }

            private JButton semBotao() {
                JButton b = new JButton();
                b.setPreferredSize(new Dimension(0, 0));
                return b;
            }
        });
    }
}
