package visao.design;

import java.awt.Component;
import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.plaf.basic.BasicScrollPaneUI;
import javax.swing.plaf.basic.BasicViewportUI;

/**
 * JScrollPane transparente com barras de rolagem finas, independente do
 * Look and Feel.
 */
public class RolagemModerna extends JScrollPane {

    public RolagemModerna(Component vista) {
        super(vista);
        setOpaque(false);
        getViewport().setUI(new BasicViewportUI());
        getViewport().setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder());
        setViewportBorder(null);
        BarraRolagemUI.aplicar(getVerticalScrollBar());
        BarraRolagemUI.aplicar(getHorizontalScrollBar());
    }

    @Override
    public void updateUI() {
        setUI(new BasicScrollPaneUI());
    }
}
