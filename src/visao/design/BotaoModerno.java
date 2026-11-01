package visao.design;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.BorderFactory;
import javax.swing.ButtonModel;
import javax.swing.JButton;

/**
 * Botão plano com cantos arredondados, preenchido (com degradê e sombra
 * colorida) ou só com contorno. Usa BasicButtonUI para ter o mesmo aspecto
 * em qualquer Look and Feel.
 */
public class BotaoModerno extends JButton {

    private static final int RAIO = 10;
    private static final int SOMBRA = 2;

    private final Color base;
    private final boolean contorno;

    public BotaoModerno(String texto, Color base, boolean contorno) {
        super(texto);
        this.base = base;
        this.contorno = contorno;
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setOpaque(false);
        setRolloverEnabled(true);
        setFont(Tema.BOTAO);
        setForeground(contorno ? base : Color.WHITE);
        setBackground(new Color(148, 163, 184)); // usado pelo texto desactivado
        setBorder(BorderFactory.createEmptyBorder(8, 16, 8 + SOMBRA, 16));
        setIconTextGap(8);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                repaint();
            }

            @Override
            public void focusLost(FocusEvent e) {
                repaint();
            }
        });
    }

    public BotaoModerno(String texto, String icone, Color base, boolean contorno) {
        this(texto, base, contorno);
        setIcon(Formularios.icone(icone));
    }

    @Override
    public void updateUI() {
        setUI(new javax.swing.plaf.basic.BasicButtonUI());
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = Formularios.suavizar(g);
        ButtonModel modelo = getModel();
        int w = getWidth() - 1;
        int h = getHeight() - 1 - SOMBRA;
        boolean premido = modelo.isPressed() && modelo.isArmed();
        int desvio = premido ? 1 : 0;

        if (!isEnabled()) {
            g2.setColor(Tema.DIVISOR);
            g2.fillRoundRect(0, 0, w, h, RAIO, RAIO);
            g2.setColor(Tema.BORDA);
            g2.drawRoundRect(0, 0, w, h, RAIO, RAIO);
        } else if (contorno) {
            g2.setColor(Tema.comAlfa(Tema.SOMBRA, 10));
            g2.fillRoundRect(0, SOMBRA, w, h, RAIO, RAIO);
            g2.setColor(premido ? Tema.misturarComBranco(base, 0.20f)
                    : modelo.isRollover() ? Tema.misturarComBranco(base, 0.08f) : Color.WHITE);
            g2.fillRoundRect(0, desvio, w, h, RAIO, RAIO);
            g2.setColor(modelo.isRollover() ? base : Tema.misturarComBranco(base, 0.40f));
            g2.drawRoundRect(0, desvio, w, h, RAIO, RAIO);
        } else {
            if (!premido) {
                g2.setColor(Tema.comAlfa(base, modelo.isRollover() ? 90 : 60));
                g2.fillRoundRect(1, SOMBRA, w - 2, h, RAIO, RAIO);
            }
            Color topo = modelo.isRollover() ? Tema.misturarComBranco(base, 0.78f) : Tema.misturarComBranco(base, 0.88f);
            Color fundo = premido ? Tema.escurecer(base, 0.12f) : base;
            g2.setPaint(new GradientPaint(0, desvio, premido ? fundo : topo, 0, h + desvio, fundo));
            g2.fillRoundRect(0, desvio, w, h, RAIO, RAIO);
            g2.setColor(new Color(255, 255, 255, 40));
            g2.drawLine(RAIO / 2, desvio + 1, w - RAIO / 2, desvio + 1);
        }

        if (isEnabled() && hasFocus()) {
            g2.setColor(Tema.comAlfa(contorno ? base : Tema.escurecer(base, 0.3f), 110));
            g2.drawRoundRect(2, 2 + desvio, w - 4, h - 4, RAIO - 2, RAIO - 2);
        }
        g2.dispose();

        if (desvio > 0) {
            g.translate(0, desvio);
            super.paintComponent(g);
            g.translate(0, -desvio);
        } else {
            super.paintComponent(g);
        }
    }
}
