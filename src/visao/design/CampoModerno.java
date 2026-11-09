package visao.design;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.JTextField;
import javax.swing.plaf.basic.BasicTextFieldUI;

/**
 * Campo de texto com cantos arredondados, fundo no tom suave do negócio e
 * anel de foco. Não depende do Look and Feel instalado.
 */
public class CampoModerno extends JTextField {

    public CampoModerno() {
        super();
        setOpaque(false);
        setBorder(new BordaCampo(4, 10));
        setForeground(Tema.TEXTO);
        setCaretColor(Tema.TEXTO);
        setSelectedTextColor(Tema.TEXTO);
        setDisabledTextColor(Tema.TEXTO_SUAVE);
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
        EfeitoHover.instalar(this);
    }

    @Override
    public void updateUI() {
        setUI(new BasicTextFieldUI());
        AtalhosTexto.instalarCampo(this);
    }

    @Override
    public Color getSelectionColor() {
        return Tema.selecaoTexto();
    }

    @Override
    protected void paintComponent(Graphics g) {
        BordaCampo.pintarFundo(g, this, isEnabled() && isEditable() ? Tema.fundoCampo() : Tema.CAMPO_INACTIVO);
        super.paintComponent(g);
    }
}
