package visao.design;

import java.awt.BorderLayout;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

/**
 * Painel raiz das janelas: fundo em degradê vertical muito suave.
 */
public class FundoJanela extends JPanel {

    public FundoJanela() {
        super(new BorderLayout());
        setOpaque(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setPaint(new GradientPaint(0, 0, Tema.FUNDO, 0, getHeight(), Tema.FUNDO_FIM));
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.dispose();
    }
}
