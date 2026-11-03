package visao.design;

import java.awt.Color;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JTextField;
import javax.swing.plaf.basic.BasicTextFieldUI;

/**
 * Valor só de leitura em destaque (ex.: total a pagar). É um JTextField para
 * que a lógica existente continue a usar getText/setText, mas não tem fundo
 * nem borda: deve ficar dentro de uma {@link Caixa} colorida.
 */
public class Mostrador extends JTextField {

    public Mostrador(Font fonte, Color cor) {
        super();
        setEditable(false);
        setFocusable(false);
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 2));
        setHorizontalAlignment(RIGHT);
        setFont(fonte);
        setForeground(cor);
        setDisabledTextColor(cor);
    }

    @Override
    public void updateUI() {
        setUI(new BasicTextFieldUI());
    }
}
