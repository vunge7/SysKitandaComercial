package comercial.mensalidade;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.DoubleFunction;
import javax.swing.JComponent;
import javax.swing.ToolTipManager;
import util.ui.ComponentesWeb;
import util.ui.EstiloUI;

/**
 * Barras horizontais de uma só série (um só tom: é magnitude, não identidade).
 * Rótulo à esquerda e valor no fim da barra, a cor do texto é sempre a do
 * texto (nunca a da barra). Dica ao passar o rato; clique numa barra chama o
 * "aoClicar" (o relatório usa-o para filtrar). A barra escolhida fica
 * realçada e as outras esbatidas.
 */
public class GraficoBarras extends JComponent {

    public static final class Barra {

        public final String rotulo;
        public final double valor;
        public final String detalhe;
        public final Object chave;

        public Barra(String rotulo, double valor, String detalhe, Object chave) {
            this.rotulo = rotulo;
            this.valor = valor;
            this.detalhe = detalhe;
            this.chave = chave;
        }
    }

    private static final int ALTURA_BARRA = 14, ESPACO = 12, MARGEM = 6;

    private final Color cor;
    private final DoubleFunction<String> formato;
    private List<Barra> barras = new ArrayList<>();
    private int sobre = -1;
    private Object seleccionada;
    private Consumer<Barra> aoClicar;
    private String vazio = "Sem dados para os filtros escolhidos.";

    public GraficoBarras(Color cor, DoubleFunction<String> formato) {
        this.cor = cor;
        this.formato = formato;
        setOpaque(false);
        ToolTipManager.sharedInstance().registerComponent(this);
        MouseAdapter m = new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int i = indice(e.getY());
                if (i != sobre) {
                    sobre = i;
                    setCursor(Cursor.getPredefinedCursor(i >= 0 && aoClicar != null ? Cursor.HAND_CURSOR : Cursor.DEFAULT_CURSOR));
                    repaint();
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                sobre = -1;
                repaint();
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                int i = indice(e.getY());
                if (i >= 0 && aoClicar != null) {
                    aoClicar.accept(barras.get(i));
                }
            }
        };
        addMouseListener(m);
        addMouseMotionListener(m);
    }

    public void setAoClicar(Consumer<Barra> c) {
        this.aoClicar = c;
    }

    /** Barra realçada (a do filtro activo); null = nenhuma. */
    public void setSeleccionada(Object chave) {
        this.seleccionada = chave;
        repaint();
    }

    public void setVazio(String texto) {
        this.vazio = texto;
    }

    public void setBarras(List<Barra> b) {
        this.barras = b == null ? new ArrayList<>() : b;
        sobre = -1;
        revalidate();
        repaint();
    }

    private int indice(int y) {
        int i = (y - MARGEM + ESPACO / 2) / (ALTURA_BARRA + ESPACO);
        return i >= 0 && i < barras.size() ? i : -1;
    }

    @Override
    public Dimension getPreferredSize() {
        int n = Math.max(3, barras.size());
        return new Dimension(320, MARGEM * 2 + n * (ALTURA_BARRA + ESPACO));
    }

    @Override
    public Dimension getMinimumSize() {
        return getPreferredSize();
    }

    @Override
    public String getToolTipText(MouseEvent e) {
        int i = indice(e.getY());
        if (i < 0) {
            return null;
        }
        Barra b = barras.get(i);
        return "<html><b>" + b.rotulo + "</b><br>" + formato.apply(b.valor)
                + (b.detalhe == null ? "" : "<br>" + b.detalhe)
                + (aoClicar != null ? "<br><i>Clique para filtrar</i>" : "") + "</html>";
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = ComponentesWeb.suave(g);
        int w = getWidth();
        g2.setFont(EstiloUI.FONTE.deriveFont(12f));
        FontMetrics fm = g2.getFontMetrics();
        if (barras.isEmpty()) {
            g2.setColor(ComponentesWeb.TEXTO_SUAVE);
            g2.drawString(vazio, 4, MARGEM + fm.getAscent());
            g2.dispose();
            return;
        }
        double max = 0;
        int larguraRotulo = 0, larguraValor = 0;
        for (Barra b : barras) {
            max = Math.max(max, b.valor);
            larguraRotulo = Math.max(larguraRotulo, fm.stringWidth(b.rotulo));
            larguraValor = Math.max(larguraValor, fm.stringWidth(formato.apply(b.valor)));
        }
        larguraRotulo = Math.min(larguraRotulo, Math.max(80, w / 3));
        int x0 = larguraRotulo + 10;
        int larguraBarras = Math.max(20, w - x0 - larguraValor - 12);

        // linha de base, discreta
        g2.setColor(ComponentesWeb.LINHA);
        g2.fillRect(x0, MARGEM - 4, 1, barras.size() * (ALTURA_BARRA + ESPACO));

        for (int i = 0; i < barras.size(); i++) {
            Barra b = barras.get(i);
            int y = MARGEM + i * (ALTURA_BARRA + ESPACO);
            boolean esbatida = seleccionada != null && !seleccionada.equals(b.chave);
            boolean realce = i == sobre || (seleccionada != null && seleccionada.equals(b.chave));

            if (i == sobre) {
                g2.setColor(ComponentesWeb.alfa(cor, 18));
                g2.fillRoundRect(0, y - ESPACO / 2 + 1, w, ALTURA_BARRA + ESPACO - 2, 8, 8);
            }
            String rot = b.rotulo;
            while (fm.stringWidth(rot) > larguraRotulo && rot.length() > 3) {
                rot = rot.substring(0, rot.length() - 2);
            }
            if (!rot.equals(b.rotulo)) {
                rot = rot + "…";
            }
            g2.setColor(esbatida ? ComponentesWeb.TEXTO_SUAVE : ComponentesWeb.TEXTO);
            g2.drawString(rot, x0 - 8 - fm.stringWidth(rot), y + ALTURA_BARRA - 3);

            int lb = max <= 0 ? 0 : (int) Math.round(larguraBarras * (b.valor / max));
            lb = b.valor > 0 ? Math.max(lb, 4) : 0;
            Color c = esbatida ? ComponentesWeb.alfa(cor, 90) : realce ? cor.darker() : cor;
            g2.setColor(c);
            // extremidade de dados arredondada; a base fica rente ao eixo
            g2.fillRoundRect(x0 + 1, y, lb, ALTURA_BARRA, 8, 8);
            if (lb > 4) {
                g2.fillRect(x0 + 1, y, 4, ALTURA_BARRA);
            }
            g2.setColor(ComponentesWeb.TEXTO);
            g2.drawString(formato.apply(b.valor), x0 + lb + 8, y + ALTURA_BARRA - 3);
        }
        g2.dispose();
    }
}
