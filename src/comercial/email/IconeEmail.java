package comercial.email;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.Icon;

/** Envelope desenhado (título e botão "Enviar agora" do formulário de e-mail). */
public class IconeEmail implements Icon {

    private final int tamanho;
    private final Color cor;

    public IconeEmail(int tamanho, Color cor) {
        this.tamanho = tamanho;
        this.cor = cor;
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.translate(x, y);
        g2.scale(tamanho / 24.0, tamanho / 24.0);
        g2.setColor(cor);
        g2.setStroke(new BasicStroke(1.9f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.draw(new java.awt.geom.RoundRectangle2D.Double(2.5, 5, 19, 14, 3, 3));
        java.awt.geom.Path2D aba = new java.awt.geom.Path2D.Double();
        aba.moveTo(3.4, 6.4);
        aba.lineTo(12, 13);
        aba.lineTo(20.6, 6.4);
        g2.draw(aba);
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
