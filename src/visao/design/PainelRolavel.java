package visao.design;

import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import javax.swing.JPanel;
import javax.swing.Scrollable;

/**
 * Conteúdo de um JScrollPane que acompanha a largura da janela. Só aparece
 * rolagem horizontal quando a janela fica mais estreita do que
 * {@code larguraMinima}.
 */
public class PainelRolavel extends JPanel implements Scrollable {

    private final int larguraMinima;

    public PainelRolavel(LayoutManager layout, int larguraMinima) {
        super(layout);
        this.larguraMinima = larguraMinima;
        setOpaque(false);
    }

    @Override
    public Dimension getPreferredScrollableViewportSize() {
        return getPreferredSize();
    }

    @Override
    public int getScrollableUnitIncrement(Rectangle visivel, int orientacao, int direccao) {
        return 16;
    }

    @Override
    public int getScrollableBlockIncrement(Rectangle visivel, int orientacao, int direccao) {
        return 160;
    }

    @Override
    public boolean getScrollableTracksViewportWidth() {
        return getParent() != null && getParent().getWidth() >= larguraMinima;
    }

    @Override
    public boolean getScrollableTracksViewportHeight() {
        return getParent() != null && getParent().getHeight() >= getPreferredSize().height;
    }
}
