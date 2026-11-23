package util.ui;

import java.awt.Component;
import java.util.function.BiFunction;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 * Muda só o texto mostrado numa linha da JList, mantendo o desenho do renderer
 * actual e o valor da lista (a selecção continua a devolver o objecto original).
 */
public class RendererComTexto implements ListCellRenderer<Object> {

    private final ListCellRenderer<Object> base;
    private final BiFunction<Object, Integer, Object> texto;

    /** Instalar depois de o renderer final da lista estar definido. */
    @SuppressWarnings("unchecked")
    public RendererComTexto(JList<?> lista, BiFunction<Object, Integer, Object> texto) {
        this.base = (ListCellRenderer<Object>) lista.getCellRenderer();
        this.texto = texto;
    }

    @Override
    public Component getListCellRendererComponent(JList<?> lista, Object valor, int indice, boolean seleccionado, boolean foco) {
        return base.getListCellRendererComponent(lista, texto.apply(valor, indice), indice, seleccionado, foco);
    }
}
