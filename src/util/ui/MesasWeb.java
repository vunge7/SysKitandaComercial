package util.ui;

import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.border.EmptyBorder;

/**
 * Versão reduzida do MesasWeb do SysKitandaHotel: só os utilitários visuais
 * usados pelos formulários de venda/compra (tecla e fonte do visor).
 */
public final class MesasWeb {

    private MesasWeb() {
    }

    /** Tecla grande arredondada. */
    public static JButton tecla(String texto, ComponentesWeb.Variante v) {
        JButton b = new JButton(texto);
        ComponentesWeb.botao(b, v, null, null);
        b.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(18f));
        b.setBorder(new EmptyBorder(8, 4, 8, 4));
        b.setPreferredSize(new Dimension(56, 42));
        b.setFocusable(false);
        return b;
    }

    /** Fonte maior para números (visor do total). */
    public static Font fonteVisor(float tamanho) {
        return new Font("Consolas", Font.BOLD, Math.round(tamanho));
    }
}
