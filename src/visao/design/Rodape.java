package visao.design;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 * Barra de acções no fundo da janela. Em janelas largas mostra o grupo da
 * esquerda e o da direita na mesma linha; abaixo de {@code quebra} píxeis
 * empilha-os.
 */
public class Rodape extends JPanel {

    public Rodape(final JComponent esquerda, final JComponent direita, int quebra) {
        super(new BorderLayout());
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(14, 20, 12, 20));
        add(new PainelResponsivo(new int[]{quebra}, (p, modo) -> {
            if (modo == 0) {
                p.add(esquerda, Formularios.gbc(0, 0, 1, 1, 1, 0, GridBagConstraints.NONE, new Insets(0, 0, 10, 0)));
                p.add(direita, Formularios.gbc(0, 1, 1, 1, 1, 0, GridBagConstraints.NONE, new Insets(0, 0, 0, 0)));
            } else {
                GridBagConstraints c = Formularios.gbc(0, 0, 1, 1, 1, 0, GridBagConstraints.NONE, new Insets(0, 0, 0, 0));
                c.anchor = GridBagConstraints.WEST;
                p.add(esquerda, c);
                c = Formularios.gbc(1, 0, 1, 1, 0, 0, GridBagConstraints.NONE, new Insets(0, 0, 0, 0));
                c.anchor = GridBagConstraints.EAST;
                p.add(direita, c);
            }
        }), BorderLayout.CENTER);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        int w = getWidth();
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, w, getHeight());
        // sombra suave a separar o rodapé do conteúdo
        g2.setPaint(new GradientPaint(0, 0, new Color(15, 23, 42, 22), 0, 6, new Color(15, 23, 42, 0)));
        g2.fillRect(0, 0, w, 6);
        g2.setColor(Tema.BORDA);
        g2.drawLine(0, 0, w, 0);
        g2.dispose();
    }
}
