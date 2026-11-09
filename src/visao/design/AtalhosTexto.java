package visao.design;

import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.UIDefaults;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.text.JTextComponent;

/**
 * Garante as teclas de edição (Backspace, Delete, setas, Home/End,
 * Ctrl+C/V/X/A, selecção com Shift...) nos componentes de texto do kit.
 * <p>
 * Os campos do kit usam os UIs "Basic" para terem o mesmo aspecto em
 * qualquer Look and Feel. Esses UIs vão buscar o mapa de teclas ao
 * UIManager ("TextField.focusInputMap"), mas os Look and Feel baseados em
 * Synth (como o Synthetica) guardam-no noutro sítio, e o campo ficava sem
 * Backspace. Aqui liga-se ao campo o mapa padrão do Swing.
 */
public final class AtalhosTexto {

    private static InputMap mapaCampo;
    private static InputMap mapaArea;

    private AtalhosTexto() {
    }

    /** Para campos de uma linha (JTextField). */
    public static void instalarCampo(JTextComponent campo) {
        instalar(campo, mapaPadrao(false));
    }

    /** Para áreas de várias linhas (JTextArea). */
    public static void instalarArea(JTextComponent area) {
        instalar(area, mapaPadrao(true));
    }

    private static void instalar(JTextComponent componente, InputMap padrao) {
        if (padrao == null) {
            return;
        }
        InputMap mapa = componente.getInputMap(JComponent.WHEN_FOCUSED);
        if (mapa.get(KeyStroke.getKeyStroke("BACK_SPACE")) != null) {
            return; // o Look and Feel já forneceu as teclas
        }
        InputMap fim = mapa;
        while (fim.getParent() != null && fim.getParent() != padrao) {
            fim = fim.getParent();
        }
        if (fim.getParent() == null) {
            fim.setParent(padrao);
        }
    }

    private static synchronized InputMap mapaPadrao(boolean multilinha) {
        if (mapaCampo == null) {
            UIDefaults padroes = new MetalLookAndFeel().getDefaults();
            mapaCampo = (InputMap) padroes.get("TextField.focusInputMap");
            mapaArea = (InputMap) padroes.get("TextArea.focusInputMap");
        }
        return multilinha ? mapaArea : mapaCampo;
    }
}
