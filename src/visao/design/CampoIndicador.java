package visao.design;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JTextField;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicTextFieldUI;

/**
 * Campo só de leitura que mostra um estado pela cor (ex.: stock disponível
 * a verde, stock crítico a vermelho).
 * <p>
 * A lógica existente pode continuar a chamar {@code setBackground(Color.RED)}:
 * o campo transforma essa cor num fundo suave, numa borda a condizer e num
 * texto escuro da mesma família, legível em qualquer estado.
 */
public class CampoIndicador extends JTextField {

    public CampoIndicador() {
        super();
        setEditable(false);
        setFocusable(false);
        setOpaque(false);
        setHorizontalAlignment(CENTER);
        setFont(Tema.VALOR);
        setBorder(new BordaCampo(4, 8));
        setBackground(Tema.CAMPO_INACTIVO);
    }

    @Override
    public void updateUI() {
        setUI(new BasicTextFieldUI());
    }

    private boolean neutro() {
        Color c = getBackground();
        return c == null || c instanceof UIResource || c.equals(Tema.CAMPO_INACTIVO) || c.equals(Color.WHITE);
    }

    @Override
    public Color getForeground() {
        if (neutro()) {
            return Tema.TEXTO;
        }
        return Tema.escurecer(getBackground(), 0.35f);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Color base = getBackground();
        boolean neutro = neutro();
        BordaCampo.pintarFundo(g, this, neutro ? Tema.CAMPO_INACTIVO : Tema.misturarComBranco(base, 0.16f));
        if (!neutro) {
            Graphics2D g2 = Formularios.suavizar(g);
            g2.setColor(Tema.misturarComBranco(base, 0.55f));
            g2.setStroke(new BasicStroke(1.2f));
            int a = BordaCampo.ANEL;
            g2.draw(new RoundRectangle2D.Float(a, a, getWidth() - 2 * a - 1, getHeight() - 2 * a - 1,
                    BordaCampo.RAIO, BordaCampo.RAIO));
            g2.dispose();
        }
        super.paintComponent(g);
    }
}
