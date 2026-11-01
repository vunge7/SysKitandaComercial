package visao.design;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JComponent;

/**
 * Marca um componente como "debaixo do rato" (para realçar a borda) e
 * repinta-o quando o rato entra ou sai.
 */
public final class EfeitoHover extends MouseAdapter {

    private static final String CHAVE = "kitanda.hover";

    private final JComponent alvo;

    private EfeitoHover(JComponent alvo) {
        this.alvo = alvo;
    }

    /**
     * Instala o efeito no alvo; os componentes extra (ex.: o botão da seta de
     * um combo) também activam o realce do alvo.
     */
    public static void instalar(JComponent alvo, Component... extras) {
        EfeitoHover efeito = new EfeitoHover(alvo);
        alvo.addMouseListener(efeito);
        for (Component extra : extras) {
            if (extra != null) {
                extra.addMouseListener(efeito);
            }
        }
    }

    public static boolean activo(Component c) {
        return c instanceof JComponent
                && Boolean.TRUE.equals(((JComponent) c).getClientProperty(CHAVE));
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        alvo.putClientProperty(CHAVE, Boolean.TRUE);
        alvo.repaint();
    }

    @Override
    public void mouseExited(MouseEvent e) {
        alvo.putClientProperty(CHAVE, Boolean.FALSE);
        alvo.repaint();
    }
}
