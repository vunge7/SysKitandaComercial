package visao.design;

import java.awt.Graphics;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.BorderFactory;
import javax.swing.JTextArea;
import javax.swing.plaf.basic.BasicTextAreaUI;

/**
 * Área de texto multi-linha com o mesmo aspecto dos campos (fundo suave,
 * cantos arredondados e anel de foco).
 */
public class AreaModerna extends RolagemModerna {

    private final JTextArea area;

    public AreaModerna(final JTextArea area) {
        super(area);
        this.area = area;
        area.setUI(new BasicTextAreaUI());
        AtalhosTexto.instalarArea(area);
        area.setOpaque(false);
        area.setForeground(Tema.TEXTO);
        area.setCaretColor(Tema.TEXTO);
        area.setSelectedTextColor(Tema.TEXTO);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
        setBorder(new BordaCampo(4, 8, 4, area));
        area.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                area.setSelectionColor(Tema.selecaoTexto());
                repaint();
            }

            @Override
            public void focusLost(FocusEvent e) {
                repaint();
            }
        });
        EfeitoHover.instalar(this, area);
    }

    @Override
    protected void paintComponent(Graphics g) {
        boolean editavel = isEnabled() && (area == null || area.isEditable());
        BordaCampo.pintarFundo(g, this, editavel ? Tema.fundoCampo() : Tema.CAMPO_INACTIVO);
        super.paintComponent(g);
    }
}
