package visao.design;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.plaf.basic.BasicButtonUI;

/**
 * Botão redondo "×" para fechar janelas sem moldura, pensado para ficar no
 * {@link Cabecalho}.
 */
public class BotaoFechar extends JButton {

    public BotaoFechar() {
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setOpaque(false);
        setRolloverEnabled(true);
        setToolTipText("Fechar (Esc)");
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setPreferredSize(new Dimension(36, 36));
    }

    @Override
    public void updateUI() {
        setUI(new BasicButtonUI());
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = Formularios.suavizar(g);
        ButtonModel modelo = getModel();
        int d = Math.min(getWidth(), getHeight()) - 1;
        if (modelo.isRollover() || hasFocus()) {
            g2.setColor(modelo.isPressed() ? new Color(220, 38, 38, 230) : new Color(239, 68, 68, 200));
        } else {
            g2.setColor(new Color(255, 255, 255, 34));
        }
        g2.fillOval(0, 0, d, d);
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        int c = d / 2;
        g2.drawLine(c - 6, c - 6, c + 6, c + 6);
        g2.drawLine(c + 6, c - 6, c - 6, c + 6);
        g2.dispose();
    }
}
