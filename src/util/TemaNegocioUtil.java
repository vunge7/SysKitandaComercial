/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

/**
 *
 * @author MARTINHO
 */
import java.awt.Color;
import java.awt.Component;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 * Utilitário para aplicar a cor dos campos dos formulários
 * conforme o tipo de negócio.
 */
public final class TemaNegocioUtil {

    public static final Color VERDE_FARMACIA = new Color(140, 198, 63);
    public static final Color CIANO_GENERICO = new Color(0, 255, 255);

    private TemaNegocioUtil() {
        // classe utilitária, não instanciável
    }

    /** Devolve true se o negócio configurado for Farmácia. */
    public static boolean ehFarmacia(String negocio) {
        return "Farmacia".equalsIgnoreCase(negocio);
    }

    /** Devolve a cor conforme o negócio. */
    public static Color corDoNegocio(boolean farmacia) {
        return farmacia ? VERDE_FARMACIA : CIANO_GENERICO;
    }

    /**
     * Aplica a cor de fundo aos campos, conforme o tipo de negócio.
     *
     * @param farmacia true se o negócio for farmácia
     * @param campos   campos do formulário a pintar (nulos são ignorados)
     */
    public static void aplicarCorCampos(boolean farmacia, JComponent... campos) {
        aplicarCorCampos(corDoNegocio(farmacia), campos);
    }

    /**
     * Aplica uma cor de fundo específica aos campos.
     */
    @SuppressWarnings("unchecked")
    public static void aplicarCorCampos(final Color cor, JComponent... campos) {
        if (campos == null) {
            return;
        }

        for (JComponent campo : campos) {
            if (campo == null) {
                continue;
            }

            campo.setOpaque(true);
            campo.setBackground(cor);

            if (campo instanceof JComboBox) {
                pintarCombo((JComboBox<Object>) campo, cor);
            }
        }
    }

    private static void pintarCombo(JComboBox<Object> combo, final Color cor) {
        // Editor do combo (quando é editável)
        Component editor = combo.getEditor().getEditorComponent();
        if (editor instanceof JComponent) {
            JComponent ed = (JComponent) editor;
            ed.setOpaque(true);
            ed.setBackground(cor);
        }

        // Renderer da lista suspensa
        final ListCellRenderer<? super Object> rendererOriginal = combo.getRenderer();

        combo.setRenderer(new ListCellRenderer<Object>() {
            @Override
            public Component getListCellRendererComponent(JList<? extends Object> list,
                    Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component c = rendererOriginal.getListCellRendererComponent(
                        list, value, index, isSelected, cellHasFocus);
                if (!isSelected) {
                    c.setBackground(cor);
                }
                return c;
            }
        });
    }
}