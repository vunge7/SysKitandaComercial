package visao.design;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JComboBox;
import javax.swing.border.AbstractBorder;

/**
 * Borda arredondada dos campos: cinza suave em repouso, mais escura com o
 * rato por cima e, em foco, borda na cor do negócio com brilho à volta.
 * <p>
 * Reserva {@link #ANEL} píxeis à volta para o brilho; o fundo do campo deve
 * ser pintado com {@link #pintarFundo(Graphics, Component, Color)}.
 */
public class BordaCampo extends AbstractBorder {

    public static final int ANEL = 2;
    public static final int RAIO = 10;

    private final int margemVertical;
    private final int margemEsquerda;
    private final int margemDireita;
    private final Component alvoFoco;

    public BordaCampo(int margemVertical, int margemHorizontal) {
        this(margemVertical, margemHorizontal, margemHorizontal, null);
    }

    /**
     * @param alvoFoco componente cujo foco acende o anel (ex.: a JTextArea
     * dentro de um JScrollPane); null para usar o próprio componente
     */
    public BordaCampo(int margemVertical, int margemEsquerda, int margemDireita, Component alvoFoco) {
        this.margemVertical = margemVertical;
        this.margemEsquerda = margemEsquerda;
        this.margemDireita = margemDireita;
        this.alvoFoco = alvoFoco;
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return getBorderInsets(c, new Insets(0, 0, 0, 0));
    }

    @Override
    public Insets getBorderInsets(Component c, Insets insets) {
        insets.top = ANEL + margemVertical;
        insets.bottom = ANEL + margemVertical;
        insets.left = ANEL + margemEsquerda;
        insets.right = ANEL + margemDireita;
        return insets;
    }

    @Override
    public boolean isBorderOpaque() {
        return false;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int largura, int altura) {
        Graphics2D g2 = Formularios.suavizar(g);
        RoundRectangle2D caixa = forma(x, y, largura, altura);
        boolean activo = c.isEnabled();
        if (activo && temFoco(c)) {
            g2.setColor(Tema.brilhoFoco());
            g2.setStroke(new BasicStroke(4f));
            g2.draw(caixa);
            g2.setColor(Tema.bordaFoco());
            g2.setStroke(new BasicStroke(1.5f));
            g2.draw(caixa);
        } else {
            g2.setColor(activo && EfeitoHover.activo(c) ? Tema.BORDA_CAMPO_HOVER : Tema.BORDA_CAMPO);
            g2.setStroke(new BasicStroke(1f));
            g2.draw(caixa);
        }
        g2.dispose();
    }

    private boolean temFoco(Component c) {
        Component alvo = alvoFoco != null ? alvoFoco : c;
        if (alvo.hasFocus()) {
            return true;
        }
        if (c instanceof JComboBox) {
            JComboBox combo = (JComboBox) c;
            return combo.isPopupVisible()
                    || (combo.isEditable() && combo.getEditor().getEditorComponent().hasFocus());
        }
        return false;
    }

    private static RoundRectangle2D forma(int x, int y, int largura, int altura) {
        return new RoundRectangle2D.Float(x + ANEL, y + ANEL,
                largura - 2 * ANEL - 1, altura - 2 * ANEL - 1, RAIO, RAIO);
    }

    /** Pinta o fundo arredondado do campo (dentro da área da borda). */
    public static void pintarFundo(Graphics g, Component c, Color fundo) {
        Graphics2D g2 = Formularios.suavizar(g);
        g2.setColor(fundo);
        g2.fill(forma(0, 0, c.getWidth(), c.getHeight()));
        g2.dispose();
    }
}
