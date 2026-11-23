package util.ui;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.WindowEvent;
import java.text.Normalizer;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.RootPaneContainer;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

/**
 * Todos os botões de sair com o mesmo padrão: vermelho (Variante.PERIGO) e o
 * mesmo ícone (ComponentesWeb.IconeSair), como FormularioWeb.botaoSair.
 *
 * Em vez de mexer nos ~200 formulários (muitos gerados pelo NetBeans), escuta a
 * abertura/activação de qualquer janela e aplica o padrão aos botões cujo texto
 * é exactamente "Sair", "Fechar", "Fechar esta janela" ou "Sair do sistema"
 * (sem distinguir maiúsculas/acentos). O botão mantém o tamanho que o
 * formulário lhe deu: margens, letra e, no limite, o ícone ajustam-se ao espaço,
 * para não mexer na arrumação dos formulários antigos.
 *
 * Botões com um destes textos que NÃO fecham a janela (ex.: "Fechar" = fecho de
 * caixa) marcam-se com {@code b.putClientProperty(BotoesSair.NAO_E_SAIR, Boolean.TRUE)}.
 */
public final class BotoesSair {

    /** Client property: este botão tem texto de sair mas faz outra coisa (não aplicar o padrão). */
    public static final String NAO_E_SAIR = "web.naoESair";
    /** Client property: o padrão já foi aplicado. */
    static final String JA_APLICADO = "web.botaoSair";

    private static final Set<String> TEXTOS = new HashSet<>(Arrays.asList(
            "sair", "fechar", "fechar esta janela", "sair do sistema"));

    private static boolean instalado;

    private BotoesSair() {
    }

    /** Chamar uma vez no arranque (LoginVisao.main). */
    public static synchronized void instalar() {
        if (instalado) {
            return;
        }
        instalado = true;
        Toolkit.getDefaultToolkit().addAWTEventListener(e -> {
            int id = e.getID();
            if ((id == WindowEvent.WINDOW_OPENED || id == WindowEvent.WINDOW_ACTIVATED) && e.getSource() instanceof Window) {
                Window w = (Window) e.getSource();
                // depois do layout: o tamanho dos botões já é conhecido
                SwingUtilities.invokeLater(() -> aplicar(w));
            }
        }, AWTEvent.WINDOW_EVENT_MASK);
    }

    /** Aplica o padrão aos botões de sair de uma janela/painel (também se pode chamar à mão). */
    public static void aplicar(Component raiz) {
        if (raiz instanceof RootPaneContainer) {
            raiz = ((RootPaneContainer) raiz).getRootPane();
        }
        percorrer(raiz);
    }

    private static void percorrer(Component c) {
        if (c instanceof JButton) {
            JButton b = (JButton) c;
            if (eBotaoSair(b)) {
                estilizar(b);
            }
        }
        if (c instanceof Container) {
            for (Component filho : ((Container) c).getComponents()) {
                percorrer(filho);
            }
        }
    }

    static boolean eBotaoSair(AbstractButton b) {
        if (Boolean.TRUE.equals(b.getClientProperty(NAO_E_SAIR)) || Boolean.TRUE.equals(b.getClientProperty(JA_APLICADO))) {
            return false;
        }
        String t = b.getText();
        if (t == null) {
            return false;
        }
        String n = Normalizer.normalize(t.trim(), Normalizer.Form.NFD).replaceAll("\\p{M}", "")
                .replaceAll("\\s+", " ").toLowerCase(Locale.ROOT);
        return TEXTOS.contains(n);
    }

    /** O mesmo que FormularioWeb.botaoSair, sem mudar o tamanho que o formulário deu ao botão. */
    static void estilizar(JButton b) {
        Dimension tamanho = b.getWidth() > 0 && b.getHeight() > 0 ? b.getSize() : null;
        String dica = b.getToolTipText();
        b.setIcon(null);
        ComponentesWeb.botao(b, ComponentesWeb.Variante.PERIGO, null, dica != null ? dica : "Fechar esta janela");
        b.setIcon(new ComponentesWeb.IconeSair(16, java.awt.Color.WHITE));
        b.putClientProperty(JA_APLICADO, Boolean.TRUE);
        if (tamanho != null) {
            caber(b, tamanho);
            b.setPreferredSize(tamanho);
            b.setMinimumSize(tamanho);
            b.setMaximumSize(tamanho);
        }
        b.repaint();
    }

    /** Ajusta margens, letra e ícone para o texto caber no tamanho que o botão já tinha. */
    private static void caber(JButton b, Dimension tamanho) {
        int vertical = Math.max(1, Math.min(8, (tamanho.height - 18) / 2));
        b.setBorder(new EmptyBorder(vertical, 12, vertical, 12));
        if (b.getPreferredSize().width <= tamanho.width) {
            return;
        }
        b.setBorder(new EmptyBorder(vertical, 6, vertical, 6));
        b.setIconTextGap(5);
        Font f = b.getFont();
        while (b.getPreferredSize().width > tamanho.width && f.getSize2D() > 10f) {
            f = f.deriveFont(f.getSize2D() - 1f);
            b.setFont(f);
        }
        if (b.getPreferredSize().width > tamanho.width) {
            b.setIcon(null); // sem espaço: fica o texto a vermelho
        }
    }

    /** Para quem cria o botão à mão: marca-o como "não é sair". */
    public static void naoESair(JComponent b) {
        b.putClientProperty(NAO_E_SAIR, Boolean.TRUE);
    }
}
