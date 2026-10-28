/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package visao;

/**
 *
 * @author MARTINHO
 */
import java.awt.*;
import java.awt.geom.*;
import java.util.Random;
import javax.swing.JPanel;

/** Fundo temático de farmácia: gradiente verde, cruz luminosa e comprimidos. */
public class PainelFundoFarmacia extends JPanel {

    private static final Color VERDE_ESCURO = new Color(5, 46, 34);
    private static final Color VERDE_MEDIO  = new Color(15, 90, 58);
    private static final Color LIMA         = new Color(140, 198, 63);
    private static final Color PETROLEO     = new Color(38, 166, 154);
    private static final Color AMBAR        = new Color(242, 184, 75);

    public PainelFundoFarmacia() {
        setOpaque(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        int w = getWidth(), h = getHeight();
        if (w <= 0 || h <= 0) {
            g2.dispose();
            return;
        }

        // 1. Gradiente de fundo
        g2.setPaint(new GradientPaint(0, 0, VERDE_ESCURO, w, h, VERDE_MEDIO));
        g2.fillRect(0, 0, w, h);

        // 2. Cruzes decorativas discretas
        Random rnd = new Random(7);
        g2.setColor(new Color(255, 255, 255, 16));
        for (int i = 0; i < 45; i++) {
            double x = rnd.nextDouble() * w;
            double y = rnd.nextDouble() * h;
            double t = h * (0.012 + rnd.nextDouble() * 0.018);
            g2.setStroke(new BasicStroke((float) (t * 0.35), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.draw(new Line2D.Double(x - t, y, x + t, y));
            g2.draw(new Line2D.Double(x, y - t, x, y + t));
        }

        // 3. Halo luminoso no canto inferior direito
        double r = h * 0.26;
        double cx = w - r * 0.25;
        double cy = h * 0.68;
        g2.setPaint(new RadialGradientPaint(new Point2D.Double(cx, cy), (float) (h * 0.85),
                new float[]{0f, 1f},
                new Color[]{new Color(140, 198, 63, 150), new Color(140, 198, 63, 0)}));
        g2.fillRect(0, 0, w, h);

        // 4. Círculo com cruz
        g2.setPaint(new RadialGradientPaint(new Point2D.Double(cx - r * 0.2, cy - r * 0.2), (float) (r * 1.2),
                new float[]{0f, 1f},
                new Color[]{new Color(120, 205, 170), new Color(20, 120, 90)}));
        g2.fill(new Ellipse2D.Double(cx - r, cy - r, r * 2, r * 2));
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke((float) (r * 0.09)));
        g2.draw(new Ellipse2D.Double(cx - r, cy - r, r * 2, r * 2));
        desenharCruz(g2, cx, cy, r * 0.55);

        // 5. Cápsulas e comprimidos
        capsula(g2, w - h * 0.48, h * 1.00, h * 0.22, h * 0.09, 20, LIMA, Color.WHITE);
        capsula(g2, w - h * 0.30, h * 0.93, h * 0.26, h * 0.11, -25, Color.WHITE, PETROLEO);
        comprimido(g2, w - h * 0.12, h * 0.95, h * 0.13, LIMA);
        comprimido(g2, w - h * 0.40, h * 0.84, h * 0.07, AMBAR);

        g2.dispose();
    }

    /** Cruz branca com brilho. */
    private void desenharCruz(Graphics2D g, double cx, double cy, double meio) {
        double barra = meio * 0.62;
        for (int i = 8; i >= 1; i--) {
            g.setColor(new Color(255, 255, 255, 14));
            g.setStroke(new BasicStroke((float) (barra + i * meio * 0.12),
                    BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g.draw(new Line2D.Double(cx - meio, cy, cx + meio, cy));
            g.draw(new Line2D.Double(cx, cy - meio, cx, cy + meio));
        }
        g.setColor(Color.WHITE);
        g.setStroke(new BasicStroke((float) barra, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.draw(new Line2D.Double(cx - meio, cy, cx + meio, cy));
        g.draw(new Line2D.Double(cx, cy - meio, cx, cy + meio));
    }

    /** Cápsula bicolor com sombra e brilho. */
    private void capsula(Graphics2D g, double cx, double cy, double comp, double esp,
            double ang, Color a, Color b) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.translate(cx, cy);
        g2.rotate(Math.toRadians(ang));
        RoundRectangle2D forma = new RoundRectangle2D.Double(-comp / 2, -esp / 2, comp, esp, esp, esp);

        // sombra
        g2.setColor(new Color(0, 0, 0, 70));
        g2.translate(esp * 0.12, esp * 0.18);
        g2.fill(forma);
        g2.translate(-esp * 0.12, -esp * 0.18);

        g2.setClip(forma);
        g2.setColor(a);
        g2.fill(new Rectangle2D.Double(-comp / 2, -esp / 2, comp / 2, esp));
        g2.setColor(b);
        g2.fill(new Rectangle2D.Double(0, -esp / 2, comp / 2, esp));

        // volume
        g2.setPaint(new GradientPaint(0, (float) (-esp / 2), new Color(255, 255, 255, 80),
                0, (float) (esp / 2), new Color(0, 0, 0, 80)));
        g2.fill(forma);

        // brilho
        g2.setColor(new Color(255, 255, 255, 110));
        g2.fill(new RoundRectangle2D.Double(-comp / 2 + esp * 0.25, -esp / 2 + esp * 0.12,
                comp - esp * 0.5, esp * 0.18, esp * 0.18, esp * 0.18));
        g2.dispose();
    }

    /** Comprimido redondo com sombra, brilho e risco central. */
    private void comprimido(Graphics2D g, double cx, double cy, double d, Color c) {
        Graphics2D g2 = (Graphics2D) g.create();
        Ellipse2D forma = new Ellipse2D.Double(cx - d / 2, cy - d / 2, d, d * 0.85);

        g2.setColor(new Color(0, 0, 0, 70));
        g2.fill(new Ellipse2D.Double(cx - d / 2 + d * 0.06, cy - d / 2 + d * 0.1, d, d * 0.85));

        g2.setPaint(new RadialGradientPaint(new Point2D.Double(cx - d * 0.15, cy - d * 0.2),
                (float) (d * 0.8), new float[]{0f, 1f},
                new Color[]{c.brighter(), c.darker()}));
        g2.fill(forma);

        g2.setColor(new Color(0, 0, 0, 55));
        g2.setStroke(new BasicStroke((float) (d * 0.04), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.draw(new Line2D.Double(cx - d * 0.35, cy, cx + d * 0.35, cy));

        g2.setColor(new Color(255, 255, 255, 120));
        g2.fill(new Ellipse2D.Double(cx - d * 0.3, cy - d * 0.32, d * 0.3, d * 0.14));
        g2.dispose();
    }
}
