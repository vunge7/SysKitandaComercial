package util.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JViewport;
import javax.swing.Scrollable;

/**
 * Grelha de botões (categorias, serviços, produtos) que se adapta à largura:
 * tantas colunas quantas couberem com a largura mínima de cada mosaico, que
 * esticam para ocupar a linha toda. Dentro de um JScrollPane acompanha a
 * largura e desliza na vertical.
 *
 * Os ecrãs antigos fazem {@code painel.setLayout(new GridLayout(l, c))}: esse
 * pedido é ignorado (fica esta grelha), por isso a lógica deles não muda.
 * Os JButton simples que lá entram (categorias) ganham o aspecto de mosaico.
 */
public class GrelhaAdaptavel extends JPanel implements Scrollable {

    private final int espaco;
    private int larguraMinima, altura;

    public GrelhaAdaptavel(int larguraMinima, int altura, int espaco) {
        this.larguraMinima = larguraMinima;
        this.altura = altura;
        this.espaco = espaco;
        super.setLayout(new Disposicao());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(espaco, espaco, espaco, espaco));
    }

    /**
     * Muda o tamanho dos mosaicos, ex.: categorias baixas e cartões de produto
     * (com fotografia) mais altos na mesma grelha.
     */
    public void setCelula(int larguraMinima, int altura) {
        if (this.larguraMinima == larguraMinima && this.altura == altura) {
            return;
        }
        this.larguraMinima = larguraMinima;
        this.altura = altura;
        revalidate();
        repaint();
    }

    @Override
    public void setLayout(LayoutManager l) {
        // só a própria grelha (no construtor da JPanel ainda não há campos: aceita o FlowLayout por omissão)
        if (l instanceof Disposicao || larguraMinima == 0) {
            super.setLayout(l);
        }
    }

    @Override
    protected void addImpl(Component c, Object restricoes, int indice) {
        if (c.getClass() == JButton.class) {
            mosaico((JButton) c);
        }
        super.addImpl(c, null, indice);
    }

    /** Aspecto de mosaico para um botão simples de texto. */
    public static void mosaico(AbstractButton b) {
        ComponentesWeb.botao(b, ComponentesWeb.Variante.SECUNDARIO, null, b.getToolTipText());
        b.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(14f));
        b.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    }

    private int colunas(int largura) {
        Insets in = getInsets();
        int util = largura - in.left - in.right;
        return Math.max(1, (util + espaco) / (larguraMinima + espaco));
    }

    private int larguraDisponivel() {
        if (getParent() instanceof JViewport) {
            return getParent().getWidth();
        }
        return getWidth() > 0 ? getWidth() : larguraMinima * 4;
    }

    private final class Disposicao implements LayoutManager {

        @Override
        public void addLayoutComponent(String n, Component c) {
        }

        @Override
        public void removeLayoutComponent(Component c) {
        }

        @Override
        public Dimension preferredLayoutSize(Container p) {
            Insets in = p.getInsets();
            int largura = larguraDisponivel();
            int n = p.getComponentCount(), cols = colunas(largura);
            int linhas = (n + cols - 1) / cols;
            return new Dimension(Math.max(largura, larguraMinima + in.left + in.right),
                    in.top + in.bottom + Math.max(0, linhas * (altura + espaco) - espaco));
        }

        @Override
        public Dimension minimumLayoutSize(Container p) {
            Insets in = p.getInsets();
            return new Dimension(larguraMinima + in.left + in.right, altura + in.top + in.bottom);
        }

        @Override
        public void layoutContainer(Container p) {
            Insets in = p.getInsets();
            int util = p.getWidth() - in.left - in.right;
            int cols = colunas(p.getWidth());
            int w = (util - (cols - 1) * espaco) / cols;
            for (int i = 0; i < p.getComponentCount(); i++) {
                int l = i / cols, c = i % cols;
                p.getComponent(i).setBounds(in.left + c * (w + espaco), in.top + l * (altura + espaco), w, altura);
            }
        }
    }

    // ---- Scrollable: acompanha a largura, desliza na altura
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
        return altura + espaco;
    }

    @Override
    public boolean getScrollableTracksViewportWidth() {
        return true;
    }

    @Override
    public boolean getScrollableTracksViewportHeight() {
        return getParent() instanceof JViewport && getParent().getHeight() > getPreferredSize().height;
    }
}
