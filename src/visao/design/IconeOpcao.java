package visao.design;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;
import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.Icon;

/**
 * Ícone desenhado para rádios e caixas de verificação. Lê o estado do botão
 * (seleccionado, rato por cima, foco, desactivado) no momento de pintar.
 */
public class IconeOpcao implements Icon {

    private static final int TAMANHO = 18;

    private final boolean radio;

    public IconeOpcao(boolean radio) {
        this.radio = radio;
    }

    @Override
    public int getIconWidth() {
        return TAMANHO;
    }

    @Override
    public int getIconHeight() {
        return TAMANHO;
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        AbstractButton botao = (AbstractButton) c;
        ButtonModel modelo = botao.getModel();
        boolean activo = botao.isEnabled();
        boolean seleccionado = modelo.isSelected();
        boolean realce = activo && (modelo.isRollover() || botao.hasFocus());
        Color cor = activo ? Tema.PRIMARIA : Tema.BORDA_CAMPO;

        Graphics2D g2 = Formularios.suavizar(g);
        g2.translate(x, y);
        if (realce) {
            g2.setColor(Tema.comAlfa(Tema.PRIMARIA, 40));
            fillForma(g2, -2, 20, radio ? 22 : 8);
        }
        if (radio) {
            g2.setColor(activo ? Color.WHITE : Tema.CAMPO_INACTIVO);
            g2.fillOval(1, 1, 16, 16);
            g2.setStroke(new BasicStroke(1.6f));
            g2.setColor(seleccionado ? cor : realce ? Tema.BORDA_CAMPO_HOVER : Tema.BORDA_CAMPO);
            g2.drawOval(1, 1, 16, 16);
            if (seleccionado) {
                g2.setColor(cor);
                g2.fillOval(5, 5, 8, 8);
            }
        } else {
            if (seleccionado) {
                g2.setColor(cor);
                g2.fillRoundRect(1, 1, 16, 16, 6, 6);
                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                Path2D visto = new Path2D.Float();
                visto.moveTo(5, 9.5f);
                visto.lineTo(8, 12.5f);
                visto.lineTo(13, 6);
                g2.draw(visto);
            } else {
                g2.setColor(activo ? Color.WHITE : Tema.CAMPO_INACTIVO);
                g2.fillRoundRect(1, 1, 16, 16, 6, 6);
                g2.setStroke(new BasicStroke(1.6f));
                g2.setColor(realce ? Tema.BORDA_CAMPO_HOVER : Tema.BORDA_CAMPO);
                g2.drawRoundRect(1, 1, 16, 16, 6, 6);
            }
        }
        g2.dispose();
    }

    private static void fillForma(Graphics2D g2, int inicio, int tamanho, int arco) {
        g2.fillRoundRect(inicio, inicio, tamanho + 2, tamanho + 2, arco, arco);
    }
}
