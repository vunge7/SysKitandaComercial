package visao.design;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

/**
 * Caixa interna (sub-secção de um cartão) com fundo suave e borda
 * arredondada.
 */
public class Caixa extends JPanel {

    private static final int RAIO = 12;

    private final String titulo;
    private final Color fundo;
    private final Color borda;

    public Caixa(String titulo) {
        this(titulo, Tema.CAIXA, Tema.BORDA);
    }

    public Caixa(String titulo, Color fundo, Color borda) {
        this.titulo = titulo;
        this.fundo = fundo;
        this.borda = borda;
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(titulo == null ? 14 : 34, 14, 14, 14));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = Formularios.suavizar(g);
        g2.setColor(fundo);
        g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, RAIO, RAIO);
        g2.setColor(borda);
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, RAIO, RAIO);
        if (titulo != null) {
            g2.setFont(Tema.TITULO_CAIXA);
            g2.setColor(Tema.TEXTO_SUAVE);
            g2.drawString(titulo, 14, 22);
        }
        g2.dispose();
    }
}
