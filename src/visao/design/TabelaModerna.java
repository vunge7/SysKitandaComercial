package visao.design;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.plaf.basic.BasicTableHeaderUI;
import javax.swing.plaf.basic.BasicTableUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;

/**
 * Tabela com o aspecto do kit: linhas altas e sem grelha, cabeçalho limpo em
 * maiúsculas, linhas alternadas e selecção azul suave. Os renderizadores
 * podem ser substituídos por subclasses de {@link RenderizadorModerno}.
 */
public class TabelaModerna extends JTable {

    public static final int ALTURA_LINHA = 38;

    public TabelaModerna() {
        super();
        configurar();
    }

    public TabelaModerna(TableModel modelo) {
        super(modelo);
        configurar();
    }

    private void configurar() {
        setRowHeight(ALTURA_LINHA);
        setShowGrid(false);
        setIntercellSpacing(new Dimension(0, 0));
        setFont(new Font("Segoe UI", Font.PLAIN, 14));
        setForeground(Tema.TEXTO);
        setBackground(Color.WHITE);
        setSelectionBackground(Tema.selecaoLista());
        setSelectionForeground(Tema.TEXTO);
        setFillsViewportHeight(true);
        setOpaque(true);

        RenderizadorModerno renderizador = new RenderizadorModerno();
        setDefaultRenderer(Object.class, renderizador);
        setDefaultRenderer(Number.class, renderizador);
        setDefaultRenderer(Double.class, renderizador);
        setDefaultRenderer(Integer.class, renderizador);

        JTableHeader cabecalho = getTableHeader();
        cabecalho.setUI(new BasicTableHeaderUI());
        cabecalho.setReorderingAllowed(false);
        cabecalho.setDefaultRenderer(new RenderizadorCabecalho());
        cabecalho.setPreferredSize(new Dimension(0, 40));
        cabecalho.setBackground(Tema.CAIXA);
    }

    @Override
    public void updateUI() {
        setUI(new BasicTableUI());
    }

    /**
     * Cabeçalho das colunas: texto pequeno em maiúsculas sobre fundo cinza
     * muito claro, com linha inferior.
     */
    private static class RenderizadorCabecalho extends DefaultTableCellRenderer {

        RenderizadorCabecalho() {
            setFont(new Font("Segoe UI", Font.BOLD, 11));
            setForeground(Tema.TEXTO_SUAVE);
            setBackground(Tema.CAIXA);
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable tabela, Object valor, boolean seleccionado,
                boolean foco, int linha, int coluna) {
            super.getTableCellRendererComponent(tabela, valor, false, false, linha, coluna);
            setText(valor == null ? "" : valor.toString().toUpperCase());
            setHorizontalAlignment(alinhamentoDaColuna(tabela, coluna));
            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 0, Tema.BORDA),
                    BorderFactory.createEmptyBorder(0, 14, 0, 14)));
            return this;
        }

        private static int alinhamentoDaColuna(JTable tabela, int coluna) {
            Class<?> tipo = tabela.getColumnClass(coluna);
            return Number.class.isAssignableFrom(tipo) ? SwingConstants.RIGHT : SwingConstants.LEFT;
        }
    }

    /** Etiqueta usada quando a tabela não tem resultados. */
    public static JLabel mensagemVazia(String texto) {
        JLabel label = new JLabel(texto, SwingConstants.CENTER);
        label.setFont(Tema.SUBTITULO_CARTAO);
        label.setForeground(Tema.TEXTO_SUAVE);
        return label;
    }
}
