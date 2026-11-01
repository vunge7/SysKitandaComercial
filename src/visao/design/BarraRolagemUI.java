package visao.design;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.plaf.basic.BasicScrollBarUI;

/**
 * Barra de rolagem fina, sem setas, com o "polegar" arredondado.
 */
public class BarraRolagemUI extends BasicScrollBarUI {

    private static final int ESPESSURA = 10;

    public static void aplicar(JScrollBar barra) {
        barra.setUI(new BarraRolagemUI());
        barra.setOpaque(false);
        barra.setUnitIncrement(16);
    }

    @Override
    protected void installDefaults() {
        super.installDefaults();
        scrollBarWidth = ESPESSURA;
    }

    @Override
    protected JButton createDecreaseButton(int orientacao) {
        return botaoVazio();
    }

    @Override
    protected JButton createIncreaseButton(int orientacao) {
        return botaoVazio();
    }

    private static JButton botaoVazio() {
        JButton botao = new JButton();
        Dimension zero = new Dimension(0, 0);
        botao.setPreferredSize(zero);
        botao.setMinimumSize(zero);
        botao.setMaximumSize(zero);
        return botao;
    }

    @Override
    protected void paintTrack(Graphics g, JComponent c, Rectangle area) {
        // trilho transparente
    }

    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle area) {
        if (area.isEmpty() || !scrollbar.isEnabled()) {
            return;
        }
        Graphics2D g2 = Formularios.suavizar(g);
        g2.setColor(isThumbRollover() || isDragging ? Tema.BORDA_CAMPO_HOVER : new Color(203, 213, 225, 200));
        int margem = 2;
        int arco = ESPESSURA - 2 * margem;
        g2.fillRoundRect(area.x + margem, area.y + margem,
                area.width - 2 * margem, area.height - 2 * margem, arco, arco);
        g2.dispose();
    }
}
