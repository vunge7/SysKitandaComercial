package visao.design;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

/**
 * Cartão branco com cantos arredondados, sombra difusa e título com marca
 * de cor (e subtítulo opcional). Pinta-se a si próprio, por isso não depende
 * do Look and Feel.
 */
public class Cartao extends JPanel {

    private static final int RAIO = 16;
    private static final int SOMBRA = 4;

    private final String titulo;
    private final String subtitulo;
    private final Color destaque;
    private int larguraFixa;

    public Cartao(String titulo, Color destaque) {
        this(titulo, null, destaque);
    }

    public Cartao(String titulo, String subtitulo, Color destaque) {
        this.titulo = titulo;
        this.subtitulo = subtitulo;
        this.destaque = destaque;
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(titulo == null ? 18 : 58, 20, 18 + SOMBRA, 20));
    }

    /**
     * Largura preferida fixa (o cartão continua a esticar quando o layout o
     * mandar preencher o espaço).
     */
    public void fixarLargura(int largura) {
        this.larguraFixa = largura;
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension d = super.getPreferredSize();
        if (larguraFixa > 0) {
            d.width = larguraFixa;
        }
        return d;
    }

    @Override
    public Dimension getMinimumSize() {
        Dimension d = super.getMinimumSize();
        if (larguraFixa > 0) {
            d.width = larguraFixa;
        }
        return d;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = Formularios.suavizar(g);
        int w = getWidth();
        int h = getHeight() - SOMBRA;

        // Sombra difusa em camadas
        for (int i = SOMBRA; i >= 1; i--) {
            g2.setColor(Tema.comAlfa(Tema.SOMBRA, 7 + (SOMBRA - i) * 2));
            g2.fillRoundRect(i - 1, i, w - 2 * (i - 1) - 1, h, RAIO + i, RAIO + i);
        }

        g2.setColor(Color.WHITE);
        g2.fillRoundRect(0, 0, w - 1, h - 1, RAIO, RAIO);
        g2.setColor(Tema.BORDA);
        g2.drawRoundRect(0, 0, w - 1, h - 1, RAIO, RAIO);

        if (titulo != null) {
            g2.setPaint(new GradientPaint(0, 18, destaque, 0, 38, Tema.misturarComBranco(destaque, 0.55f)));
            g2.fillRoundRect(20, 19, 4, 20, 4, 4);
            g2.setFont(Tema.TITULO_CARTAO);
            g2.setColor(Tema.TEXTO);
            FontMetrics fm = g2.getFontMetrics();
            int base = 19 + (20 + fm.getAscent() - fm.getDescent()) / 2;
            g2.drawString(titulo, 33, base);
            if (subtitulo != null) {
                int x = 33 + fm.stringWidth(titulo) + 10;
                g2.setFont(Tema.SUBTITULO_CARTAO);
                g2.setColor(Tema.TEXTO_SUAVE);
                g2.drawString(subtitulo, x, base);
            }
            g2.setColor(Tema.DIVISOR);
            g2.fillRect(20, 46, w - 40, 1);
        }
        g2.dispose();
    }
}
