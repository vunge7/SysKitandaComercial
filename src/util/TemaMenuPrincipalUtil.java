package util;

import java.awt.Container;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.JLabel;
import org.netbeans.lib.awtextra.AbsoluteConstraints;
import visao.PainelFundoFarmacia;

public final class TemaMenuPrincipalUtil {

    private TemaMenuPrincipalUtil() { }

    public static void aplicar(final JLabel fundo, boolean farmacia) {
        if (!farmacia || fundo == null) {
            return;
        }
        final Container pai = fundo.getParent();
        if (pai == null) {
            return;
        }

        // Remove a imagem azul e torna o label transparente (o texto continua visível)
        fundo.setIcon(null);
        fundo.setOpaque(false);

        final PainelFundoFarmacia painel = new PainelFundoFarmacia();

        // Com AbsoluteLayout é obrigatório passar AbsoluteConstraints
        Rectangle b = fundo.getBounds();
        pai.add(painel, new AbsoluteConstraints(b.x, b.y, b.width, b.height));

        // Colocar o painel logo atrás do jLabel1
        pai.setComponentZOrder(painel, pai.getComponentZOrder(fundo) + 1);

        // Acompanhar o tamanho e a posição do label
        fundo.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                painel.setBounds(fundo.getBounds());
            }

            @Override
            public void componentMoved(ComponentEvent e) {
                painel.setBounds(fundo.getBounds());
            }
        });

        pai.revalidate();
        pai.repaint();
    }
}