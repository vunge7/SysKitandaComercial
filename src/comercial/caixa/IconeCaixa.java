package comercial.caixa;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.RoundRectangle2D;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import util.ui.ComponentesWeb;
import util.ui.MenuWeb;

/**
 * Ícones da caixa (2026-11-14): o símbolo de traço do menu (MenuWeb.Simbolo
 * CAIXA_ABRIR / CAIXA_FECHAR / TALAO_FECHO) a branco sobre um quadrado
 * arredondado em degradê — verde na abertura, laranja/vermelho no fecho,
 * azul/roxo nos fechos (reimpressão). Usado nos menus, nos cabeçalhos dos
 * formulários, nos botões e nos avisos.
 */
public final class IconeCaixa implements Icon {

    public enum Tipo {
        ABERTURA(MenuWeb.Simbolo.CAIXA_ABRIR, new Color(34, 197, 94), new Color(21, 128, 61)),
        FECHO(MenuWeb.Simbolo.CAIXA_FECHAR, new Color(249, 115, 22), new Color(220, 38, 38)),
        FECHOS(MenuWeb.Simbolo.TALAO_FECHO, new Color(59, 130, 246), new Color(124, 58, 237));

        final MenuWeb.Simbolo simbolo;
        final Color cima, baixo;

        Tipo(MenuWeb.Simbolo simbolo, Color cima, Color baixo) {
            this.simbolo = simbolo;
            this.cima = cima;
            this.baixo = baixo;
        }
    }

    private final Tipo tipo;
    private final int tamanho;

    public IconeCaixa(Tipo tipo, int tamanho) {
        this.tipo = tipo;
        this.tamanho = tamanho;
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D g2 = ComponentesWeb.suave(g.create());
        double t = tamanho;
        RoundRectangle2D fundo = new RoundRectangle2D.Double(x, y, t, t, t * 0.32, t * 0.32);
        if (tamanho >= 24) {
            g2.setColor(new Color(0, 0, 0, 40)); // sombra suave
            g2.fill(new RoundRectangle2D.Double(x + t * 0.03, y + t * 0.06, t, t, t * 0.32, t * 0.32));
        }
        g2.setPaint(new GradientPaint(x, y, tipo.cima, x, (float) (y + t), tipo.baixo));
        g2.fill(fundo);
        g2.setColor(new Color(255, 255, 255, 60)); // brilho no topo
        g2.setStroke(new BasicStroke(1f));
        g2.draw(new RoundRectangle2D.Double(x + 0.5, y + 0.5, t - 1, t - 1, t * 0.32, t * 0.32));
        double m = t * 0.17;
        MenuWeb.desenharSimbolo(g2, tipo.simbolo, x + m, y + m, t - 2 * m, Color.WHITE);
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

    /** Troca o ícone do hotel do cabeçalho do FormularioWeb pelo ícone da caixa. */
    public static void noCabecalhoPublico(JComponent cabecalho, Tipo tipo) {
        noCabecalho(cabecalho, tipo);
    }

    static void noCabecalho(JComponent cabecalho, Tipo tipo) {
        java.util.Deque<Component> fila = new java.util.ArrayDeque<>();
        fila.add(cabecalho);
        while (!fila.isEmpty()) {
            Component x = fila.poll();
            if (x instanceof JLabel && ((JLabel) x).getIcon() != null) {
                ((JLabel) x).setIcon(new IconeCaixa(tipo, 46));
                return;
            }
            if (x instanceof java.awt.Container) {
                for (Component f : ((java.awt.Container) x).getComponents()) {
                    fila.add(f);
                }
            }
        }
    }

    /**
     * Ícones nos itens "Abertura de Caixa" e "Fecho de Caixa" da barra lateral
     * dos menus (Recepção, Restaurante, Bar, Hospedaria).
     */
    public static void noMenu(JButton abertura, JButton fecho) {
        if (abertura != null) {
            abertura.putClientProperty(MenuWeb.SIMBOLO_LATERAL, MenuWeb.Simbolo.CAIXA_ABRIR);
            abertura.repaint();
        }
        if (fecho != null) {
            fecho.putClientProperty(MenuWeb.SIMBOLO_LATERAL, MenuWeb.Simbolo.CAIXA_FECHAR);
            fecho.repaint();
        }
    }
}
