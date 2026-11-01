package visao.design;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.plaf.basic.BasicButtonUI;

/**
 * Selector em "pílula" (estilo iOS/Windows 11) para opções mutuamente
 * exclusivas. Os botões recebidos (rádios ou checkboxes num ButtonGroup)
 * mantêm o seu modelo e eventos; só muda a forma como são desenhados.
 */
public class ControloSegmentado extends JPanel {

    private final AbstractButton[] opcoes;

    public ControloSegmentado(AbstractButton... opcoes) {
        super(new GridLayout(1, opcoes.length, 4, 0));
        this.opcoes = opcoes.clone();
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
        for (AbstractButton opcao : this.opcoes) {
            opcao.setUI(new BasicButtonUI());
            opcao.setIcon(null);
            opcao.setOpaque(false);
            opcao.setContentAreaFilled(false);
            opcao.setBorderPainted(false);
            opcao.setFocusPainted(false);
            opcao.setRolloverEnabled(true);
            opcao.setHorizontalAlignment(SwingConstants.CENTER);
            opcao.setBorder(BorderFactory.createEmptyBorder(5, 14, 5, 14));
            opcao.setFont(Tema.OPCAO_FORTE);
            opcao.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            opcao.addItemListener(e -> actualizarCores());
            opcao.getModel().addChangeListener(e -> repaint());
            opcao.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    repaint();
                }

                @Override
                public void focusLost(FocusEvent e) {
                    repaint();
                }
            });
            add(opcao);
        }
        actualizarCores();
    }

    private void actualizarCores() {
        for (AbstractButton opcao : opcoes) {
            opcao.setForeground(opcao.isSelected() ? Tema.PRIMARIA : Tema.ROTULO);
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = Formularios.suavizar(g);
        int w = getWidth();
        int h = getHeight();
        g2.setColor(Tema.DIVISOR);
        g2.fillRoundRect(0, 0, w - 1, h - 1, 12, 12);
        g2.setColor(Tema.BORDA);
        g2.drawRoundRect(0, 0, w - 1, h - 1, 12, 12);
        for (AbstractButton opcao : opcoes) {
            if (!opcao.isVisible()) {
                continue;
            }
            Rectangle r = opcao.getBounds();
            if (opcao.isSelected()) {
                g2.setColor(new Color(15, 23, 42, 22));
                g2.fillRoundRect(r.x, r.y + 1, r.width, r.height, 9, 9);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(r.x, r.y, r.width, r.height - 1, 9, 9);
            } else if (opcao.getModel().isRollover()) {
                g2.setColor(new Color(226, 232, 240));
                g2.fillRoundRect(r.x, r.y, r.width, r.height - 1, 9, 9);
            }
            if (opcao.hasFocus()) {
                g2.setColor(Tema.comAlfa(Tema.PRIMARIA, 90));
                g2.drawRoundRect(r.x, r.y, r.width - 1, r.height - 2, 9, 9);
            }
        }
        g2.dispose();
    }
}
