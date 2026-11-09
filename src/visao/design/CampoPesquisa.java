package visao.design;

import java.awt.BasicStroke;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;

/**
 * Campo de pesquisa: ícone de lupa à esquerda e texto de ajuda
 * ("placeholder") enquanto está vazio.
 */
public class CampoPesquisa extends CampoModerno {

    private static final int ESPACO_ICONE = 34;

    private final String ajuda;

    public CampoPesquisa(String ajuda) {
        super();
        this.ajuda = ajuda;
        setBorder(new BordaCampo(4, ESPACO_ICONE, 10, null));
        setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 15));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = Formularios.suavizar(g);
        int cy = getHeight() / 2;

        // Lupa
        g2.setColor(hasFocus() ? Tema.bordaFoco() : Tema.TEXTO_SUAVE);
        g2.setStroke(new BasicStroke(1.8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        int x = BordaCampo.ANEL + 12;
        g2.drawOval(x, cy - 7, 11, 11);
        g2.drawLine(x + 10, cy + 3, x + 14, cy + 7);

        // Texto de ajuda
        if (getText().isEmpty() && ajuda != null) {
            Insets in = getInsets();
            g2.setFont(getFont());
            g2.setColor(Tema.TEXTO_SUAVE);
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(ajuda, in.left + 2, cy + (fm.getAscent() - fm.getDescent()) / 2);
        }
        g2.dispose();
    }
}
