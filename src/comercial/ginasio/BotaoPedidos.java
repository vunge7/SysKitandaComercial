package comercial.ginasio;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JButton;
import javax.swing.Timer;
import util.ui.ComponentesWeb;
import util.ui.EstiloUI;
import util.ui.MenuWeb;
import util.ui.Tema;

/**
 * Botão "Pedidos" da janela de vendas do ginásio: degradê com as cores do
 * negócio, o desenho da prancheta e, à direita, o número de clientes com
 * pedidos em aberto (a pulsar devagar enquanto houver algum). 2026-11-21.
 */
public final class BotaoPedidos extends JButton {

    private int contagem;
    private float pulso;
    private final Timer animacao;

    public BotaoPedidos() {
        super("Pedidos");
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setOpaque(false);
        setRolloverEnabled(true);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setFont(EstiloUI.FONTE_NEGRITO.deriveFont(12.5f));
        actualizarDica();
        long inicio = System.currentTimeMillis();
        animacao = new Timer(60, e -> {
            pulso = (float) (0.5 + 0.5 * Math.sin((System.currentTimeMillis() - inicio) / 380.0));
            repaint();
        });
    }

    public int getContagem() {
        return contagem;
    }

    public void setContagem(int n) {
        contagem = Math.max(0, n);
        actualizarDica();
        if (contagem > 0 && isShowing()) {
            animacao.start();
        } else {
            animacao.stop();
        }
        repaint();
    }

    @Override
    public void addNotify() {
        super.addNotify();
        if (contagem > 0) {
            animacao.start();
        }
    }

    @Override
    public void removeNotify() {
        animacao.stop();
        super.removeNotify();
    }

    private void actualizarDica() {
        setToolTipText("<html><b>Pedidos dos clientes (F6)</b><br>"
                + (contagem == 0 ? "Nenhum cliente com pedidos em aberto."
                        : contagem + (contagem == 1 ? " cliente com pedidos" : " clientes com pedidos") + " por pagar.")
                + (util.ui.Tema.actual() == util.ui.Tema.Negocio.RESTAURANTE ? "<br><i>Registe o que o cliente vai pedindo e facture tudo no fim.</i></html>" : "<br><i>Registe o que o cliente pede durante o treino e facture tudo no fim.</i></html>"));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = ComponentesWeb.suave(g);
        Color[] cores = Tema.amostra(Tema.actual());
        Color escuro = cores[0], vivo = cores[1], destaque = cores[2];
        boolean sobre = getModel().isRollover(), premido = getModel().isPressed();
        int w = getWidth(), h = getHeight(), raio = h >= 52 ? 18 : h;

        Color a = premido ? escuro : sobre ? ComponentesWeb.mistura(vivo, Color.WHITE, 0.12) : vivo;
        g2.setPaint(new GradientPaint(0, 0, a, w, h, ComponentesWeb.mistura(escuro, vivo, 0.25)));
        g2.fillRoundRect(0, 0, w - 1, h - 1, raio, raio);
        g2.setColor(ComponentesWeb.alfa(Color.WHITE, 40));
        g2.fillRoundRect(2, 2, w - 5, h / 2 - 2, raio - 4, raio - 4);

        if (h >= 52) {
            pintarAlto(g2, w, h, raio, escuro, destaque);
            g2.dispose();
            return;
        }

        // prancheta + texto
        int ic = Math.min(18, h - 12);
        MenuWeb.desenharSimbolo(g2, MenuWeb.Simbolo.PRANCHETA, 9, (h - ic) / 2.0, ic, Color.WHITE);
        g2.setFont(getFont());
        FontMetrics fm = g2.getFontMetrics();
        g2.setColor(Color.WHITE);
        g2.drawString(getText(), 9 + ic + 5, (h + fm.getAscent() - fm.getDescent()) / 2);

        // contador
        String n = contagem > 99 ? "99+" : String.valueOf(contagem);
        g2.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(11.5f));
        FontMetrics fn = g2.getFontMetrics();
        int bh = h - 10, bw = Math.max(bh, fn.stringWidth(n) + 10), bx = w - bw - 5, by = 5;
        if (contagem > 0) {
            g2.setColor(ComponentesWeb.alfa(destaque, (int) (60 + 90 * pulso)));
            g2.setStroke(new BasicStroke(2f + 2f * pulso));
            g2.drawRoundRect(bx - 1, by - 1, bw + 1, bh + 1, bh + 2, bh + 2);
            g2.setColor(destaque);
        } else {
            g2.setColor(ComponentesWeb.alfa(Color.WHITE, 70));
        }
        g2.fillRoundRect(bx, by, bw, bh, bh, bh);
        g2.setColor(contagem > 0 ? escuro : Color.WHITE);
        g2.drawString(n, bx + (bw - fn.stringWidth(n)) / 2, by + (bh + fn.getAscent() - fn.getDescent()) / 2);

        if (isFocusOwner()) {
            g2.setStroke(new BasicStroke(1.5f));
            g2.setColor(ComponentesWeb.alfa(Color.WHITE, 200));
            g2.drawRoundRect(2, 2, w - 5, h - 5, raio - 4, raio - 4);
        }
        g2.dispose();
    }

    /** Botão alto (ex.: cabeçalho da venda): prancheta em cima, texto em baixo, contador no canto. */
    private void pintarAlto(Graphics2D g2, int w, int h, int raio, Color escuro, Color destaque) {
        int ic = Math.min(26, h - 34);
        g2.setFont(getFont().deriveFont(14f));
        FontMetrics fm = g2.getFontMetrics();
        int bloco = ic + 4 + fm.getAscent();
        int iy = (h - bloco) / 2;
        MenuWeb.desenharSimbolo(g2, MenuWeb.Simbolo.PRANCHETA, (w - ic) / 2.0, iy, ic, Color.WHITE);
        g2.setColor(Color.WHITE);
        g2.drawString(getText(), (w - fm.stringWidth(getText())) / 2, iy + ic + 4 + fm.getAscent() - 2);

        String n = contagem > 99 ? "99+" : String.valueOf(contagem);
        g2.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(12f));
        FontMetrics fn = g2.getFontMetrics();
        int bh = 22, bw = Math.max(bh, fn.stringWidth(n) + 12), bx = w - bw - 7, by = 7;
        if (contagem > 0) {
            g2.setColor(ComponentesWeb.alfa(destaque, (int) (60 + 90 * pulso)));
            g2.setStroke(new BasicStroke(2f + 2f * pulso));
            g2.drawRoundRect(bx - 1, by - 1, bw + 1, bh + 1, bh + 2, bh + 2);
            g2.setColor(destaque);
        } else {
            g2.setColor(ComponentesWeb.alfa(Color.WHITE, 70));
        }
        g2.fillRoundRect(bx, by, bw, bh, bh, bh);
        g2.setColor(contagem > 0 ? escuro : Color.WHITE);
        g2.drawString(n, bx + (bw - fn.stringWidth(n)) / 2, by + (bh + fn.getAscent() - fn.getDescent()) / 2);

        if (isFocusOwner()) {
            g2.setStroke(new BasicStroke(1.5f));
            g2.setColor(ComponentesWeb.alfa(Color.WHITE, 200));
            g2.drawRoundRect(2, 2, w - 5, h - 5, raio - 4, raio - 4);
        }
    }
}
