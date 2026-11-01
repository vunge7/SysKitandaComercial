package visao.design;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Moldura escura e arredondada para fotografias. Quando a etiqueta não tem
 * imagem, mostra um ícone de câmara e o texto "Sem fotografia".
 */
public class MolduraFoto extends JPanel {

    private final JLabel foto;

    public MolduraFoto(JLabel foto) {
        super(new BorderLayout());
        this.foto = foto;
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        foto.setHorizontalAlignment(JLabel.CENTER);
        add(foto, BorderLayout.CENTER);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = Formularios.suavizar(g);
        int w = getWidth();
        int h = getHeight();
        g2.setPaint(new GradientPaint(0, 0, new Color(51, 65, 85), 0, h, Tema.MOLDURA_FOTO));
        g2.fillRoundRect(0, 0, w, h, 14, 14);
        if (foto.getIcon() == null) {
            int cx = w / 2;
            int cy = h / 2 - 10;
            g2.setColor(Tema.MOLDURA_FOTO_TEXTO);
            g2.setStroke(new BasicStroke(1.8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.drawRoundRect(cx - 17, cy - 10, 34, 24, 7, 7);
            g2.drawRoundRect(cx - 7, cy - 15, 14, 6, 3, 3);
            g2.drawOval(cx - 7, cy - 5, 14, 14);
            g2.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            String texto = "Sem fotografia";
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(texto, cx - fm.stringWidth(texto) / 2, cy + 34);
        }
        g2.dispose();
    }
}
