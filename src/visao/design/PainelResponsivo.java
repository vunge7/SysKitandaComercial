package visao.design;

import java.awt.GridBagLayout;
import java.util.Arrays;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * Painel que reorganiza os seus componentes conforme a largura disponível,
 * como os "breakpoints" de uma página web.
 * <p>
 * Com quebras {900, 1280}: largura &lt; 900 é o modo 0, entre 900 e 1280 o
 * modo 1 e a partir de 1280 o modo 2. Sempre que o modo muda, o painel é
 * esvaziado e a {@link Disposicao} volta a montá-lo.
 */
public class PainelResponsivo extends JPanel {

    /**
     * Monta os componentes no painel (que usa GridBagLayout) para o modo
     * indicado.
     */
    public interface Disposicao {

        void aplicar(JPanel painel, int modo);
    }

    /**
     * Folga ao subir de modo, para o layout não "saltar" entre dois modos
     * quando a barra de rolagem aparece/desaparece perto de uma quebra.
     */
    private static final int HISTERESE = 24;

    private final int[] quebras;
    private final Disposicao disposicao;
    private int modo = -1;

    public PainelResponsivo(int[] quebras, Disposicao disposicao) {
        super(new GridBagLayout());
        setOpaque(false);
        this.quebras = quebras.clone();
        Arrays.sort(this.quebras);
        this.disposicao = disposicao;
        reorganizar(this.quebras.length);
    }

    public int getModo() {
        return modo;
    }

    @Override
    @SuppressWarnings("deprecation")
    public void reshape(int x, int y, int largura, int altura) {
        super.reshape(x, y, largura, altura);
        if (largura > 0) {
            reorganizar(modoPara(largura));
        }
    }

    private int modoPara(int largura) {
        int novo = 0;
        for (int i = 0; i < quebras.length; i++) {
            int limiar = quebras[i] + (i >= modo ? HISTERESE : 0);
            if (largura < limiar) {
                break;
            }
            novo = i + 1;
        }
        return novo;
    }

    private void reorganizar(int novoModo) {
        if (novoModo == modo) {
            return;
        }
        modo = novoModo;
        removeAll();
        disposicao.aplicar(this, novoModo);
        revalidate();
        repaint();
        // A troca acontece a meio de um ciclo de layout, que no fim marcaria os
        // contentores-pai como válidos com a altura antiga. Revalidar depois
        // desse ciclo garante que o rodapé/conteúdo crescem para o novo modo.
        SwingUtilities.invokeLater(() -> {
            revalidate();
            repaint();
        });
    }
}
