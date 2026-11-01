package visao.design;

import java.awt.BasicStroke;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;

/**
 * Combo com o mesmo aspecto dos campos: fundo suave, cantos arredondados,
 * seta minimalista, lista suspensa limpa e anel de foco.
 */
public class ComboModerno<E> extends JComboBox<E> {

    public ComboModerno() {
        super();
        configurar();
    }

    public ComboModerno(ComboBoxModel<E> modelo) {
        super(modelo);
        configurar();
    }

    private void configurar() {
        setOpaque(false);
        setFont(Tema.COMBO);
        setForeground(Tema.TEXTO);
        setBorder(new BordaCampo(2, 8, 2, null));
        setRenderer(new Renderizador());
        setMaximumRowCount(12);
        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                repaint();
            }

            @Override
            public void focusLost(FocusEvent e) {
                repaint();
            }
        });
        addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                repaint();
            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                repaint();
            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {
                repaint();
            }
        });
        EfeitoHover.instalar(this, getComponents());
    }

    @Override
    public void updateUI() {
        setUI(new UI());
    }

    @Override
    protected void paintComponent(Graphics g) {
        BordaCampo.pintarFundo(g, this, isEnabled() ? Tema.fundoCampo() : Tema.CAMPO_INACTIVO);
        super.paintComponent(g);
    }

    /**
     * Renderizador com mais espaço nos itens da lista e sem fundo no valor
     * mostrado dentro do combo.
     */
    private static class Renderizador extends DefaultListCellRenderer {

        @Override
        public Component getListCellRendererComponent(JList<?> lista, Object valor, int indice,
                boolean seleccionado, boolean foco) {
            super.getListCellRendererComponent(lista, valor, indice, seleccionado, false);
            if (indice < 0) {
                setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 0));
                setOpaque(false);
            } else {
                setBorder(BorderFactory.createEmptyBorder(7, 12, 7, 12));
                setOpaque(true);
                setFont(Tema.COMBO);
                setBackground(seleccionado ? Tema.selecaoLista() : java.awt.Color.WHITE);
                setForeground(seleccionado ? Tema.PRIMARIA : Tema.TEXTO);
            }
            return this;
        }
    }

    private static class UI extends BasicComboBoxUI {

        @Override
        protected JButton createArrowButton() {
            JButton seta = new JButton() {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = Formularios.suavizar(g);
                    g2.setColor(isEnabled() ? Tema.ROTULO : Tema.BORDA_CAMPO);
                    g2.setStroke(new BasicStroke(1.8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    int cx = getWidth() / 2;
                    int cy = getHeight() / 2;
                    g2.drawLine(cx - 4, cy - 2, cx, cy + 2);
                    g2.drawLine(cx, cy + 2, cx + 4, cy - 2);
                    g2.dispose();
                }
            };
            seta.setUI(new BasicButtonUI());
            seta.setOpaque(false);
            seta.setContentAreaFilled(false);
            seta.setBorder(BorderFactory.createEmptyBorder());
            seta.setFocusable(false);
            seta.setName("ComboBox.arrowButton");
            return seta;
        }

        @Override
        public void paintCurrentValueBackground(Graphics g, Rectangle area, boolean temFoco) {
            // o fundo é pintado pelo próprio ComboModerno
        }

        @Override
        @SuppressWarnings("unchecked")
        public void paintCurrentValue(Graphics g, Rectangle area, boolean temFoco) {
            ListCellRenderer renderer = comboBox.getRenderer();
            Component c = renderer.getListCellRendererComponent(listBox, comboBox.getSelectedItem(), -1, false, false);
            c.setFont(comboBox.getFont());
            c.setForeground(comboBox.isEnabled() ? Tema.TEXTO : Tema.TEXTO_SUAVE);
            if (c instanceof JComponent) {
                ((JComponent) c).setOpaque(false);
            }
            currentValuePane.paintComponent(g, c, comboBox, area.x, area.y, area.width, area.height,
                    c instanceof JPanel);
        }

        @Override
        protected ComboPopup createPopup() {
            return new BasicComboPopup(comboBox) {
                @Override
                protected void configureList() {
                    super.configureList();
                    list.setBackground(java.awt.Color.WHITE);
                    list.setSelectionBackground(Tema.selecaoLista());
                    list.setSelectionForeground(Tema.PRIMARIA);
                }

                @Override
                protected void configurePopup() {
                    super.configurePopup();
                    setBorder(BorderFactory.createLineBorder(Tema.BORDA_CAMPO));
                }

                @Override
                protected void configureScroller() {
                    super.configureScroller();
                    BarraRolagemUI.aplicar(scroller.getVerticalScrollBar());
                }
            };
        }
    }
}
