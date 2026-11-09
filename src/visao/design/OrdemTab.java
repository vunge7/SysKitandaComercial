package visao.design;

import java.awt.Component;
import java.awt.Container;
import java.awt.FocusTraversalPolicy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JSpinner;
import javax.swing.LayoutFocusTraversalPolicy;
import javax.swing.SwingUtilities;

/**
 * Ordem do Tab fixa, independente da posição no ecrã. Nas janelas
 * responsivas os cartões mudam de sítio conforme a largura; com esta
 * política o Tab segue sempre a ordem lógica de preenchimento.
 * <p>
 * Componentes ocultos, desactivados ou não focáveis são saltados. Se o foco
 * estiver num componente fora da lista (ex.: editor da tabela), usa-se a
 * ordem normal do Swing.
 */
public class OrdemTab extends FocusTraversalPolicy {

    private final List<Component> ordem;
    private final LayoutFocusTraversalPolicy normal = new LayoutFocusTraversalPolicy();
    private Component inicial;

    public OrdemTab(Component... componentes) {
        this.ordem = new ArrayList<>(Arrays.asList(componentes));
    }

    /** Componente que recebe o foco quando a janela abre (por omissão, o primeiro). */
    public void definirInicial(Component componente) {
        this.inicial = componente;
    }

    /** Componente que recebe de facto o foco (ex.: o texto dentro de um spinner). */
    private static Component alvo(Component c) {
        if (c instanceof JSpinner && ((JSpinner) c).getEditor() instanceof JSpinner.DefaultEditor) {
            return ((JSpinner.DefaultEditor) ((JSpinner) c).getEditor()).getTextField();
        }
        if (c instanceof CampoData) {
            return ((CampoData) c).getCampo();
        }
        return c;
    }

    private static boolean aceita(Component c) {
        Component a = alvo(c);
        return a != null && a.isShowing() && a.isEnabled() && a.isFocusable()
                && (!(a instanceof JComponent) || ((JComponent) a).isRequestFocusEnabled());
    }

    private int indice(Component c) {
        for (int i = 0; i < ordem.size(); i++) {
            Component o = ordem.get(i);
            if (o == c || alvo(o) == c || (o instanceof Container && SwingUtilities.isDescendingFrom(c, o))) {
                return i;
            }
        }
        return -1;
    }

    private Component procurar(int inicio, int passo) {
        int n = ordem.size();
        for (int k = 1; k <= n; k++) {
            Component c = ordem.get(((inicio + passo * k) % n + n) % n);
            if (aceita(c)) {
                return alvo(c);
            }
        }
        return null;
    }

    @Override
    public Component getComponentAfter(Container raiz, Component actual) {
        int i = indice(actual);
        if (i < 0) {
            return normal.getComponentAfter(raiz, actual);
        }
        Component c = procurar(i, 1);
        return c != null ? c : normal.getComponentAfter(raiz, actual);
    }

    @Override
    public Component getComponentBefore(Container raiz, Component actual) {
        int i = indice(actual);
        if (i < 0) {
            return normal.getComponentBefore(raiz, actual);
        }
        Component c = procurar(i, -1);
        return c != null ? c : normal.getComponentBefore(raiz, actual);
    }

    @Override
    public Component getFirstComponent(Container raiz) {
        Component c = procurar(-1, 1);
        return c != null ? c : normal.getFirstComponent(raiz);
    }

    @Override
    public Component getLastComponent(Container raiz) {
        Component c = procurar(0, -1);
        return c != null ? c : normal.getLastComponent(raiz);
    }

    @Override
    public Component getDefaultComponent(Container raiz) {
        if (inicial != null && aceita(inicial)) {
            return alvo(inicial);
        }
        return getFirstComponent(raiz);
    }
}
