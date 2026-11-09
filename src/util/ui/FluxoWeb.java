package util.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JComponent;

/**
 * Fluxo de etapas desenhado (ex.: o circuito da roupa na lavandaria:
 * Recolha → Lavagem → Engomagem → Pronto → Entrega). Cada etapa tem um
 * símbolo num círculo; a etapa activa fica com a cor do tema e as anteriores
 * marcadas como feitas. Só desenha: não tem eventos.
 */
public class FluxoWeb extends JComponent {

    private final String[] nomes;
    private final MenuWeb.Simbolo[] simbolos;
    private int activa;

    /** activa: índice da etapa em destaque (-1 = nenhuma). */
    public FluxoWeb(String[] nomes, MenuWeb.Simbolo[] simbolos, int activa) {
        this.nomes = nomes;
        this.simbolos = simbolos;
        this.activa = activa;
        setPreferredSize(new Dimension(520, 96));
        setMinimumSize(new Dimension(260, 96));
    }

    /** O circuito da roupa na lavandaria. */
    public static FluxoWeb lavandaria(int activa) {
        return new FluxoWeb(new String[]{"Recolha", "Lavagem", "Engomagem", "Pronto", "Entrega"},
                new MenuWeb.Simbolo[]{MenuWeb.Simbolo.CAMISA, MenuWeb.Simbolo.LAVANDARIA, MenuWeb.Simbolo.FERRO,
                    MenuWeb.Simbolo.CABIDE, MenuWeb.Simbolo.CARRINHO}, activa);
    }

    public void setActiva(int i) {
        activa = i;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = ComponentesWeb.suave(g);
        int n = nomes.length;
        int w = getWidth(), h = getHeight();
        int raio = Math.max(16, Math.min(26, Math.min(h / 3, w / (n * 3))));
        int cy = raio + 6;
        int passo = n == 1 ? 0 : (w - 2 * (raio + 14)) / (n - 1);
        Color tema = ComponentesWeb.AZUL_VIVO;
        Color feito = EstiloUI.AZUL_ESCURO;
        Color apagado = new Color(203, 213, 225);

        // ligações
        for (int i = 0; i < n - 1; i++) {
            int x1 = raio + 14 + i * passo + raio, x2 = raio + 14 + (i + 1) * passo - raio;
            g2.setStroke(new BasicStroke(3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                    10f, i < activa ? null : new float[]{6f, 6f}, 0f));
            g2.setColor(i < activa ? feito : apagado);
            g2.drawLine(x1, cy, x2, cy);
        }
        // etapas
        Font fonte = EstiloUI.FONTE_NEGRITO.deriveFont(12f);
        g2.setFont(fonte);
        FontMetrics fm = g2.getFontMetrics();
        for (int i = 0; i < n; i++) {
            int cx = raio + 14 + i * passo;
            boolean ehActiva = i == activa;
            boolean ehFeita = activa >= 0 && i < activa;
            if (ehActiva) {
                g2.setColor(ComponentesWeb.alfa(tema, 45));
                g2.fillOval(cx - raio - 6, cy - raio - 6, 2 * raio + 12, 2 * raio + 12);
            }
            g2.setColor(ehActiva ? tema : ehFeita ? feito : Color.WHITE);
            g2.fillOval(cx - raio, cy - raio, 2 * raio, 2 * raio);
            g2.setStroke(new BasicStroke(2f));
            g2.setColor(ehActiva ? tema : ehFeita ? feito : apagado);
            g2.drawOval(cx - raio, cy - raio, 2 * raio, 2 * raio);
            double t = raio * 1.15;
            MenuWeb.desenharSimbolo(g2, simbolos[i], cx - t / 2, cy - t / 2, t,
                    ehActiva || ehFeita ? Color.WHITE : ComponentesWeb.TEXTO_SUAVE);
            g2.setColor(ehActiva ? EstiloUI.AZUL_ESCURO : ComponentesWeb.TEXTO_SUAVE);
            int tw = fm.stringWidth(nomes[i]);
            g2.drawString(nomes[i], cx - tw / 2, cy + raio + 8 + fm.getAscent());
        }
        g2.dispose();
    }
}
