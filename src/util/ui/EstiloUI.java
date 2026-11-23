package util.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

/**
 * Aparência comum dos formulários: letra, cores, ícones, botões, tabelas,
 * layout que se adapta à largura e barra de estado com relógio.
 */
public final class EstiloUI {

    public static final String FAMILIA = "Segoe UI";
    public static final Font FONTE = new Font(FAMILIA, Font.PLAIN, 13);
    public static final Font FONTE_NEGRITO = FONTE.deriveFont(Font.BOLD);

    public static Color AZUL_ESCURO = new Color(0, 51, 102);
    public static Color AZUL_TEXTO = new Color(21, 67, 120);
    public static Color AZUL_CLARO = new Color(226, 235, 247);
    public static Color AZUL_BORDA = new Color(170, 190, 220);
    public static Color LINHA_IMPAR = new Color(242, 246, 252);
    public static Color SELECCAO = new Color(51, 122, 183);
    public static final Color VERDE = new Color(0, 120, 60);
    public static final Color VERMELHO = new Color(190, 0, 0);

    private EstiloUI() {
    }

    // =====================================================
    // JANELA
    // =====================================================
    /**
     * Ocupa ~94% da área útil do ecrã (sem a barra de tarefas) e fica
     * redimensionável; em ecrãs pequenos abre maximizada.
     */
    public static void ajustarAoEcra(JFrame f, int larguraMinima, int alturaMinima) {
        Rectangle util = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        f.setResizable(true);
        f.setMinimumSize(new Dimension(Math.min(larguraMinima, util.width), Math.min(alturaMinima, util.height)));
        f.setSize((int) (util.width * 0.94), (int) (util.height * 0.94));
        f.setLocationRelativeTo(null);
        if (util.width <= 1440 || util.height <= 820) {
            f.setExtendedState(JFrame.MAXIMIZED_BOTH);
        }
    }

    // =====================================================
    // ÍCONES
    // =====================================================
    /**
     * Ícone do projecto redimensionado; null se o ficheiro não existir.
     */
    public static ImageIcon icone(String caminho, int tamanho) {
        java.net.URL url = EstiloUI.class.getResource(caminho);
        if (url == null) {
            System.err.println("[EstiloUI] Ícone não encontrado: " + caminho);
            return null;
        }
        Image img = new ImageIcon(url).getImage().getScaledInstance(tamanho, tamanho, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }

    // =====================================================
    // BOTÕES
    // =====================================================
    /**
     * Botão mais visível: letra a negrito, ícone de 24 px (o indicado ou o
     * que já tinha, redimensionado), mão como cursor.
     */
    public static void estilizarBotao(AbstractButton b, String caminhoIcone, String dica) {
        ImageIcon ic = caminhoIcone != null ? icone(caminhoIcone, 24) : null;
        if (ic == null && b.getIcon() instanceof ImageIcon) {
            Image actual = ((ImageIcon) b.getIcon()).getImage();
            ic = new ImageIcon(actual.getScaledInstance(24, 24, Image.SCALE_SMOOTH));
        }
        if (ic != null) {
            b.setIcon(ic);
        }
        b.setFont(FONTE_NEGRITO);
        b.setIconTextGap(8);
        b.setFocusPainted(false);
        b.setMargin(new Insets(5, 10, 5, 12));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        if (dica != null) {
            b.setToolTipText(dica);
        }
    }

    /**
     * Botão grande com texto por baixo do ícone (menus/relatórios).
     */
    public static void estilizarBotaoCartao(JButton b, String caminhoIcone, String dica) {
        estilizarBotao(b, null, dica);
        ImageIcon ic = caminhoIcone != null ? icone(caminhoIcone, 40) : null;
        if (ic != null) {
            b.setIcon(ic);
        }
        b.setFont(FONTE_NEGRITO.deriveFont(14f));
        b.setVerticalTextPosition(SwingConstants.BOTTOM);
        b.setHorizontalTextPosition(SwingConstants.CENTER);
        b.setPreferredSize(new Dimension(240, 110));
    }

    // =====================================================
    // TABELAS
    // =====================================================
    /**
     * Cabeçalho maior e a negrito, linhas de 28 px, selecção azul. Com zebra,
     * as células sem renderer próprio ficam com linhas alternadas.
     */
    public static void estilizarTabela(JTable t, boolean zebra) {
        t.setRowHeight(Math.max(t.getRowHeight(), 28));
        t.setGridColor(new Color(225, 230, 238));
        t.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        t.setFillsViewportHeight(true);
        JTableHeader h = t.getTableHeader();
        h.setFont(FONTE_NEGRITO);
        h.setPreferredSize(new Dimension(h.getPreferredSize().width, 32));
        h.setReorderingAllowed(false);
        if (zebra) {
            t.setFont(FONTE);
            t.setSelectionBackground(SELECCAO);
            t.setSelectionForeground(Color.WHITE);
            ZebraRenderer texto = new ZebraRenderer(SwingConstants.LEFT);
            ZebraRenderer numero = new ZebraRenderer(SwingConstants.RIGHT);
            t.setDefaultRenderer(Object.class, texto);
            t.setDefaultRenderer(String.class, texto);
            t.setDefaultRenderer(Number.class, numero);
            t.setDefaultRenderer(Double.class, numero);
            t.setDefaultRenderer(Integer.class, numero);
            t.setDefaultRenderer(java.math.BigDecimal.class, numero);
        }
    }

    /**
     * Renderer com linhas alternadas e um pouco de margem.
     */
    public static class ZebraRenderer extends DefaultTableCellRenderer {

        public ZebraRenderer(int alinhamento) {
            setHorizontalAlignment(alinhamento);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (!isSelected) {
                c.setBackground(row % 2 == 0 ? Color.WHITE : LINHA_IMPAR);
                c.setForeground(table.getForeground());
            }
            setBorder(BorderFactory.createEmptyBorder(0, 6, 0, 6));
            return c;
        }
    }

    // =====================================================
    // PAINÉIS E LETRA
    // =====================================================
    public static JPanel painelTitulado(String titulo, LayoutManager layout) {
        JPanel p = new JPanel(layout);
        p.setBorder(bordaTitulada(titulo));
        return p;
    }

    public static javax.swing.border.Border bordaTitulada(String titulo) {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(BorderFactory.createLineBorder(AZUL_BORDA),
                        titulo, TitledBorder.LEFT, TitledBorder.TOP, FONTE_NEGRITO, AZUL_TEXTO),
                BorderFactory.createEmptyBorder(2, 6, 4, 6));
    }

    /**
     * Troca a família de letra para Segoe UI em todo o contentor, mantendo o
     * negrito/itálico e com tamanho mínimo; ignora os componentes indicados.
     */
    public static void aplicarFonte(Container c, int tamanhoMinimo, Component... ignorar) {
        for (Component filho : c.getComponents()) {
            if (contem(ignorar, filho)) {
                continue;
            }
            Font f = filho.getFont();
            if (f != null && !(filho instanceof JTable)) {
                filho.setFont(new Font(FAMILIA, f.getStyle(), Math.max(f.getSize(), tamanhoMinimo)));
            }
            if (filho instanceof JScrollPane) {
                Component v = ((JScrollPane) filho).getViewport().getView();
                if (v != null && !(v instanceof JTable) && !contem(ignorar, v) && v.getFont() != null) {
                    v.setFont(new Font(FAMILIA, v.getFont().getStyle(), Math.max(v.getFont().getSize(), tamanhoMinimo)));
                }
                // painéis dentro de JScrollPane (ex.: EstiloUI.adaptavel): tratar o conteúdo
                if (v instanceof JPanel) {
                    aplicarFonte((Container) v, tamanhoMinimo, ignorar);
                }
            } else if (filho instanceof Container) {
                aplicarFonte((Container) filho, tamanhoMinimo, ignorar);
            }
        }
    }

    private static boolean contem(Component[] lista, Component c) {
        for (Component x : lista) {
            if (x == c) {
                return true;
            }
        }
        return false;
    }

    /**
     * Envolve o conteúdo num JScrollPane que só mostra barras quando a janela
     * fica mais pequena do que o mínimo do conteúdo (ecrãs muito pequenos).
     */
    public static JScrollPane adaptavel(Component conteudo) {
        PainelAdaptavel p = new PainelAdaptavel();
        p.add(conteudo, BorderLayout.CENTER);
        JScrollPane sp = new JScrollPane(p);
        sp.setBorder(null);
        sp.getVerticalScrollBar().setUnitIncrement(16);
        sp.getHorizontalScrollBar().setUnitIncrement(16);
        return sp;
    }

    private static class PainelAdaptavel extends JPanel implements Scrollable {

        PainelAdaptavel() {
            super(new BorderLayout());
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
            return 160;
        }

        @Override
        public boolean getScrollableTracksViewportWidth() {
            return getParent() instanceof JViewport && getParent().getWidth() >= getMinimumSize().width;
        }

        @Override
        public boolean getScrollableTracksViewportHeight() {
            // altura: compara com a preferida (calculada na hora, com a letra actual)
            return getParent() instanceof JViewport && getParent().getHeight() >= getPreferredSize().height;
        }
    }

    // =====================================================
    // BARRA DE ESTADO COM RELÓGIO
    // =====================================================
    /**
     * Barra inferior: texto à esquerda e relógio (actualizado a cada segundo)
     * à direita. O relógio pára quando a janela é fechada.
     */
    public static JPanel barraEstadoComRelogio(JFrame janela, String textoEsquerda) {
        return barraEstadoComRelogio(janela, textoEsquerda, null);
    }

    /**
     * Igual, com um componente extra à esquerda, 25 px depois do texto
     * (ex.: utilizador com sessão iniciada).
     */
    public static JPanel barraEstadoComRelogio(JFrame janela, String textoEsquerda, Component extraEsquerda) {
        return barraEstadoComRelogio((java.awt.Window) janela, textoEsquerda, extraEsquerda);
    }

    /** Igual, para qualquer janela (ex.: JDialog). */
    public static JPanel barraEstadoComRelogio(java.awt.Window janela, String textoEsquerda, Component extraEsquerda) {
        JPanel barra = new JPanel(new BorderLayout());
        barra.setBackground(AZUL_ESCURO);
        barra.setBorder(BorderFactory.createEmptyBorder(4, 12, 4, 12));

        JLabel esquerda = new JLabel(textoEsquerda);
        esquerda.setFont(FONTE_NEGRITO);
        esquerda.setForeground(new Color(200, 215, 235));
        JPanel ladoEsquerdo = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        ladoEsquerdo.setOpaque(false);
        ladoEsquerdo.add(esquerda);
        if (extraEsquerda != null) {
            ladoEsquerdo.add(javax.swing.Box.createHorizontalStrut(25));
            ladoEsquerdo.add(extraEsquerda);
        }
        barra.add(ladoEsquerdo, BorderLayout.WEST);

        final JLabel relogio = new JLabel();
        relogio.setFont(FONTE_NEGRITO.deriveFont(14f));
        relogio.setForeground(Color.WHITE);
        ImageIcon ic = icone("/imagens/calendario 16x16.png", 16);
        if (ic != null) {
            relogio.setIcon(ic);
            relogio.setIconTextGap(8);
        }
        barra.add(relogio, BorderLayout.EAST);

        final SimpleDateFormat fmt = new SimpleDateFormat("EEEE, dd/MM/yyyy   HH:mm:ss", new Locale("pt", "PT"));
        Runnable actualizar = () -> {
            String s = fmt.format(new Date());
            relogio.setText(Character.toUpperCase(s.charAt(0)) + s.substring(1));
        };
        actualizar.run();
        final Timer t = new Timer(1000, e -> actualizar.run());
        t.start();
        janela.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                t.stop();
            }
        });
        return barra;
    }

    // =====================================================
    // WRAPLAYOUT
    // =====================================================
    /**
     * FlowLayout que passa os componentes para a linha seguinte quando falta
     * largura e aumenta a altura do painel (o FlowLayout normal corta-os).
     */
    public static class WrapLayout extends FlowLayout {

        private int alturaAnterior = -1;

        public WrapLayout(int align, int hgap, int vgap) {
            super(align, hgap, vgap);
        }

        /**
         * Quando a largura muda e as linhas passam a ser mais (ou menos), a
         * altura preferida muda: pede-se novo layout aos contentores de cima,
         * senão as linhas novas ficavam cortadas (ex.: cabeçalho ao estreitar).
         */
        @Override
        public void layoutContainer(Container alvo) {
            int altura = tamanho(alvo, true).height;
            if (alturaAnterior >= 0 && altura != alturaAnterior && alvo instanceof javax.swing.JComponent) {
                // depois do layout em curso (que volta a marcar o painel como
                // válido e guardaria a altura antiga)
                javax.swing.SwingUtilities.invokeLater(() -> {
                    alvo.invalidate();
                    ((javax.swing.JComponent) alvo).revalidate();
                });
            }
            alturaAnterior = altura;
            super.layoutContainer(alvo);
        }

        @Override
        public Dimension preferredLayoutSize(Container alvo) {
            return tamanho(alvo, true);
        }

        @Override
        public Dimension minimumLayoutSize(Container alvo) {
            Dimension d = tamanho(alvo, false);
            d.width -= getHgap() + 1;
            return d;
        }

        private Dimension tamanho(Container alvo, boolean preferido) {
            synchronized (alvo.getTreeLock()) {
                int largura = alvo.getSize().width;
                Container pai = alvo;
                while (largura == 0 && pai.getParent() != null) {
                    pai = pai.getParent();
                    largura = pai.getSize().width;
                }
                if (largura == 0) {
                    largura = Integer.MAX_VALUE;
                }
                Insets in = alvo.getInsets();
                int maxLargura = largura - (in.left + in.right + getHgap() * 2);

                Dimension total = new Dimension(0, 0);
                int linhaLarg = 0;
                int linhaAlt = 0;
                for (Component c : alvo.getComponents()) {
                    if (!c.isVisible()) {
                        continue;
                    }
                    Dimension d = preferido ? c.getPreferredSize() : c.getMinimumSize();
                    if (linhaLarg + d.width > maxLargura && linhaLarg > 0) {
                        total.width = Math.max(total.width, linhaLarg);
                        total.height += linhaAlt + getVgap();
                        linhaLarg = 0;
                        linhaAlt = 0;
                    }
                    linhaLarg += (linhaLarg > 0 ? getHgap() : 0) + d.width;
                    linhaAlt = Math.max(linhaAlt, d.height);
                }
                total.width = Math.max(total.width, linhaLarg);
                total.height += linhaAlt;
                total.width += in.left + in.right + getHgap() * 2;
                total.height += in.top + in.bottom + getVgap() * 2;
                return total;
            }
        }
    }

}
