package visao.design;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.BorderFactory;
import javax.swing.JLabel;

/**
 * Etiqueta em forma de pílula translúcida, para mostrar informação no
 * cabeçalho (código do registo, estado, utilizador...).
 */
public class Selo extends JLabel {

    public Selo(String texto) {
        super(texto);
        setFont(new Font("Segoe UI", Font.BOLD, 13));
        setForeground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(6, 14, 6, 14));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = Formularios.suavizar(g);
        int h = getHeight();
        g2.setColor(new Color(255, 255, 255, 34));
        g2.fillRoundRect(0, 0, getWidth() - 1, h - 1, h, h);
        g2.setColor(new Color(255, 255, 255, 70));
        g2.drawRoundRect(0, 0, getWidth() - 1, h - 1, h, h);
        g2.dispose();
        super.paintComponent(g);
    }
}
