package visao.design;

import java.awt.Color;
import java.awt.Component;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * Renderizador das células da {@link TabelaModerna}: espaçamento generoso,
 * linhas alternadas, separador fino entre linhas e números alinhados à
 * direita com duas casas decimais.
 * <p>
 * Para colorir linhas conforme o estado do registo, basta reescrever
 * {@link #corDaLinha(JTable, int)} e {@link #corDoIndicador(JTable, int)}.
 */
public class RenderizadorModerno extends DefaultTableCellRenderer {

    private static final Color LINHA_ALTERNADA = new Color(248, 250, 252);
    private static final DecimalFormat FORMATO_NUMERO =
            new DecimalFormat("#,##0.00", new DecimalFormatSymbols(new Locale("pt", "PT")));

    @Override
    public Component getTableCellRendererComponent(JTable tabela, Object valor, boolean seleccionado,
            boolean foco, int linha, int coluna) {
        super.getTableCellRendererComponent(tabela, valor, seleccionado, false, linha, coluna);

        if (valor instanceof Double || valor instanceof Float || valor instanceof java.math.BigDecimal) {
            setText(FORMATO_NUMERO.format(((Number) valor).doubleValue()));
            setHorizontalAlignment(SwingConstants.RIGHT);
        } else if (valor instanceof Number) {
            setHorizontalAlignment(SwingConstants.RIGHT);
        } else {
            setHorizontalAlignment(SwingConstants.LEFT);
        }

        Color indicador = coluna == 0 ? corDoIndicador(tabela, linha) : null;
        if (seleccionado) {
            setBackground(tabela.getSelectionBackground());
            setForeground(Tema.TEXTO);
            setFont(tabela.getFont().deriveFont(java.awt.Font.BOLD));
            if (coluna == 0) {
                indicador = Tema.PRIMARIA;
            }
        } else {
            Color fundo = corDaLinha(tabela, linha);
            setBackground(fundo != null ? fundo : (linha % 2 == 0 ? Color.WHITE : LINHA_ALTERNADA));
            setForeground(Tema.TEXTO);
            setFont(tabela.getFont());
        }
        setBorder(borda(indicador));
        return this;
    }

    private static Border borda(Color indicador) {
        Border separador = BorderFactory.createMatteBorder(0, 0, 1, 0, Tema.DIVISOR);
        if (indicador == null) {
            return BorderFactory.createCompoundBorder(separador, BorderFactory.createEmptyBorder(0, 14, 0, 14));
        }
        return BorderFactory.createCompoundBorder(separador, BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 4, 0, 0, indicador),
                BorderFactory.createEmptyBorder(0, 10, 0, 14)));
    }

    /** Fundo da linha não seleccionada; null para linhas alternadas. */
    protected Color corDaLinha(JTable tabela, int linha) {
        return null;
    }

    /** Barra de estado à esquerda da primeira coluna; null para nenhuma. */
    protected Color corDoIndicador(JTable tabela, int linha) {
        return null;
    }
}
