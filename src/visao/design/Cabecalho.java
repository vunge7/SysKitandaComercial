package visao.design;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RadialGradientPaint;
import java.awt.geom.Point2D;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Cabeçalho das janelas: faixa em degradê com brilho, emblema com sigla,
 * título, subtítulo e uma área à direita para selos/informação.
 */
public class Cabecalho extends JPanel {

    private final JPanel direita;

    public Cabecalho(String sigla, String titulo, String subtitulo) {
        super(new BorderLayout(16, 0));
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(14, 24, 14, 24));

        JLabel lbTitulo = new JLabel(titulo);
        lbTitulo.setFont(Tema.TITULO_JANELA);
        lbTitulo.setForeground(Color.WHITE);
        JLabel lbSubtitulo = new JLabel(subtitulo);
        lbSubtitulo.setFont(Tema.SUBTITULO_JANELA);
        lbSubtitulo.setForeground(Tema.CABECALHO_SUBTITULO);
        JPanel textos = Formularios.transparente(new GridLayout(2, 1, 0, 0));
        textos.add(lbTitulo);
        textos.add(lbSubtitulo);

        JPanel esquerda = Formularios.transparente(new BorderLayout(14, 0));
        esquerda.add(new Emblema(sigla), BorderLayout.WEST);
        esquerda.add(textos, BorderLayout.CENTER);

        direita = Formularios.transparente(new FlowLayout(FlowLayout.RIGHT, 14, 8));
        add(esquerda, BorderLayout.WEST);
        add(direita, BorderLayout.EAST);
    }

    public void adicionarDireita(JComponent componente) {
        direita.add(componente);
    }

    /**
     * Permite mover a janela arrastando o cabeçalho (útil em janelas sem
     * moldura, que de outra forma ficariam presas no sítio).
     */
    public void permitirArrastar(final Window janela) {
        MouseAdapter arrastar = new MouseAdapter() {
            private Point inicio;

            @Override
            public void mousePressed(MouseEvent e) {
                inicio = e.getPoint();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (inicio != null) {
                    Point local = janela.getLocation();
                    janela.setLocation(local.x + e.getX() - inicio.x, local.y + e.getY() - inicio.y);
                }
            }
        };
        addMouseListener(arrastar);
        addMouseMotionListener(arrastar);
        setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = Formularios.suavizar(g);
        int w = getWidth();
        int h = getHeight();
        g2.setPaint(new GradientPaint(0, 0, Tema.CABECALHO_INICIO, w, h, Tema.CABECALHO_FIM));
        g2.fillRect(0, 0, w, h);

        // Brilhos decorativos
        float raio = Math.max(h * 2.4f, 1f);
        g2.setPaint(new RadialGradientPaint(new Point2D.Float(w * 0.78f, -h * 0.4f), raio,
                new float[]{0f, 1f}, new Color[]{new Color(255, 255, 255, 46), new Color(255, 255, 255, 0)}));
        g2.fillRect(0, 0, w, h);
        g2.setColor(new Color(255, 255, 255, 14));
        g2.fillOval(w - 220, -90, 260, 260);
        g2.fillOval(w - 90, h - 40, 140, 140);

        // Linha de luz na base
        g2.setPaint(new GradientPaint(0, 0, new Color(96, 165, 250, 0), w / 2f, 0,
                new Color(147, 197, 253, 150), true));
        g2.fillRect(0, h - 2, w, 2);
        g2.dispose();
    }

    /** Quadrado arredondado translúcido com a sigla da janela. */
    private static class Emblema extends JComponent {

        private final String sigla;

        Emblema(String sigla) {
            this.sigla = sigla;
            setPreferredSize(new Dimension(46, 46));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = Formularios.suavizar(g);
            int lado = Math.min(getWidth(), getHeight());
            int y = (getHeight() - lado) / 2;
            g2.setPaint(new GradientPaint(0, y, new Color(255, 255, 255, 70), 0, y + lado,
                    new Color(255, 255, 255, 22)));
            g2.fillRoundRect(0, y, lado, lado, 14, 14);
            g2.setColor(new Color(255, 255, 255, 90));
            g2.drawRoundRect(0, y, lado - 1, lado - 1, 14, 14);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 20));
            g2.setColor(Color.WHITE);
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(sigla, (lado - fm.stringWidth(sigla)) / 2,
                    y + (lado + fm.getAscent() - fm.getDescent()) / 2);
            g2.dispose();
        }
    }
}
