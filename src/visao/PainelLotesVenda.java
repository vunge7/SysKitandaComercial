package visao;

import comercial.controller.EntradaLoteCompra;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 * Painel "Lotes disponiveis (FEFO)" para o ecra de vendas.
 *
 * Lista TODOS os lotes com stock do produto no armazem (por ordem FEFO) e
 * destaca, a negrito, os que saem para a quantidade pedida.
 *
 * Nao precisa do designer: instalar() coloca-o por codigo, a direita do painel
 * que tem o botao Fecho, com a mesma altura, e ocupa o espaco que sobrar
 * (ajusta-se quando a janela e redimensionada ou maximizada). Se nao houver
 * espaco suficiente fica escondido (ver temEspaco()).
 *
 * Uso no formulario de vendas:
 *
 * painelLotes = PainelLotesVenda.instalar(
 * PainelLotesVenda.encontrarPainelDoBotao( getContentPane(), "Fecho" ), 560 );
 */
public class PainelLotesVenda extends JPanel {

    /**
     * Um lote com stock no armazem da venda (independente das tuas classes
     * LoteFEFO / Lote).
     */
    public static final class Linha {

        public final String numeroLote;
        public final Date validade;
        public final double stockExistente;
        /**
         * Quantidade que sai deste lote nesta venda (0 se o lote nao e usado).
         */
        public final double quantidade;

        public Linha(
                String numeroLote,
                Date validade,
                double stockExistente,
                double quantidade) {

            this.numeroLote = numeroLote;
            this.validade = validade;
            this.stockExistente = stockExistente;
            this.quantidade = quantidade;
        }
    }

    static final int GAP = 8;
    static final int MARGEM = 8;
    static final int LARGURA_MIN = 260;
    private static final Color AZUL = new Color(0, 51, 102);
    private static final Color VERDE = new Color(0, 128, 0);
    private static final Color VERMELHO = new Color(192, 0, 0);
    private static final int COL_DIAS = 2;
    private static final int COL_RETIRAR = 4;

    private final JLabel lbProduto = new JLabel();
    private final JLabel lbEstado = new JLabel(" ");
    private final DefaultTableModel modelo;
    private final JTable tabela;
    private int diasAlerta = 90;

    public PainelLotesVenda() {
        super(new BorderLayout(4, 4));
        setBackground(new Color(244, 244, 244));
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK), BorderFactory.createEmptyBorder(0, 0, 4, 0)));

        JLabel titulo = new JLabel("LOTES DISPONÍVEIS", SwingConstants.CENTER);
        titulo.setOpaque(true);
        titulo.setBackground(AZUL);
        titulo.setForeground(Color.WHITE);
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 14f));
        titulo.setBorder(BorderFactory.createEmptyBorder(5, 4, 5, 4));

        lbProduto.setBorder(BorderFactory.createEmptyBorder(2, 8, 2, 8));
        lbProduto.setFont(lbProduto.getFont().deriveFont(Font.PLAIN, 13f));

        modelo = new DefaultTableModel(
                new Object[][]{},
                new String[]{
                    "Lote",
                    "Validade",
                    "Dias",
                    "Stock existente",
                    "Retirar"
                }
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabela = new JTable(modelo);
        tabela.setRowHeight(24);
        tabela.setFont(
                tabela.getFont().deriveFont(Font.PLAIN, 13f)
        );
        tabela.setDefaultRenderer(
                Object.class,
                new RendererValidade()
        );

        tabela.getColumnModel().getColumn(0).setPreferredWidth(95);
        tabela.getColumnModel().getColumn(1).setPreferredWidth(85);
        tabela.getColumnModel().getColumn(2).setPreferredWidth(40);
        tabela.getColumnModel().getColumn(3).setPreferredWidth(100);
        tabela.getColumnModel().getColumn(4).setPreferredWidth(60);

        lbEstado.setBorder(BorderFactory.createEmptyBorder(2, 8, 2, 8));
        lbEstado.setFont(lbEstado.getFont().deriveFont(Font.BOLD, 13f));

        JPanel topo = new JPanel(new BorderLayout());
        topo.setOpaque(false);
        topo.add(titulo, BorderLayout.NORTH);
        topo.add(lbProduto, BorderLayout.CENTER);

        add(topo, BorderLayout.NORTH);
        add(new JScrollPane(tabela), BorderLayout.CENTER);
        add(lbEstado, BorderLayout.SOUTH);
        setPreferredSize(new Dimension(420, 300));
        limpar();
    }

    // ------------------------------------------------------------------
    // Conteudo
    // ------------------------------------------------------------------
    /**
     * Estado inicial / depois de uma venda.
     */
    public void limpar() {
        modelo.setRowCount(0);
        lbProduto.setText("<html><i>Seleccione um produto para ver os lotes disponíveis.</i></html>");
        lbEstado.setText(" ");
    }

    public void mostrar(String medicamento, double qtdPedida, List<Linha> linhas) {
        mostrar(medicamento, qtdPedida, linhas, 90);
    }

    /**
     * Mostra TODOS os lotes com stock (por ordem FEFO) e destaca, a negrito, os
     * que saem para a quantidade pedida (coluna "Retirar" maior que zero).
     *
     * @param diasAlerta limite (em dias) para pintar a amarelo; ate 30 dias e
     * sempre vermelho
     */
    public void mostrar(String medicamento, double qtdPedida, List<Linha> linhas, int diasAlerta) {
        this.diasAlerta = diasAlerta;
        lbProduto.setText("<html><b>" + escapar(medicamento) + "</b><br>Quantidade solicitada: " + formatar(qtdPedida) + "</html>");
        preencher(linhas);

        double totalStock = 0;
        double totalRetirar = 0;
        int lotesUsados = 0;

        for (Linha l : linhas) {
            totalStock += l.stockExistente;
            totalRetirar += l.quantidade;
            if (l.quantidade > 0) {
                lotesUsados++;
            }
        }

        if (linhas.isEmpty()) {
            definirEstado("Não existem lotes válidos disponíveis.", VERMELHO);
        } else if (totalRetirar + 1e-9 < qtdPedida) {
            definirEstado("Stock em lotes insuficiente: faltam " + formatar(qtdPedida - totalRetirar) + ".", VERMELHO);
        } else {
            definirEstado("Stock suficiente: sai de " + lotesUsados + " lote(s). Total em lotes: "
                    + formatar(totalStock) + ".", VERDE);
        }
    }

    private void preencher(List<Linha> linhas) {
        modelo.setRowCount(0);
        for (Linha l : linhas) {
            modelo.addRow(new Object[]{
                l.numeroLote,
                EntradaLoteCompra.formatar(l.validade),
                EntradaLoteCompra.diasAteValidade(l.validade),
                formatar(l.stockExistente),
                formatar(l.quantidade)
            });
        }
    }

    /**
     * Mostra um aviso a vermelho (por exemplo, o texto da excepcao de stock
     * insuficiente), sem lotes.
     */
    public void mostrarAviso(String medicamento, String mensagem) {
        mostrarAviso(medicamento, mensagem, new java.util.ArrayList<Linha>());
    }

    /**
     * Aviso a vermelho, mas ainda com os lotes existentes (retirar = 0), para o
     * operador ver onde ha stock.
     */
    public void mostrarAviso(String medicamento, String mensagem, List<Linha> lotes) {
        lbProduto.setText("<html><b>" + escapar(medicamento) + "</b></html>");
        preencher(lotes);
        definirEstado(mensagem, VERMELHO);
    }

    private void definirEstado(String texto, Color cor) {
        lbEstado.setForeground(cor);
        lbEstado.setText("<html>" + escapar(texto) + "</html>");
    }

    /**
     * Esta visivel (havia espaco a direita do painel de ancora)?
     */
    public boolean temEspaco() {
        return isVisible();
    }

    JTable getTabela() {
        return tabela;
    }

    String textoEstado() {
        return lbEstado.getText();
    }

    String textoProduto() {
        return lbProduto.getText();
    }

    Color corEstado() {
        return lbEstado.getForeground();
    }

    static String formatar(double v) {
        return BigDecimal.valueOf(v).stripTrailingZeros().toPlainString();
    }

    static String escapar(String s) {
        return s == null ? "" : s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }

    /**
     * Vermelho = caducado ou ate 30 dias; amarelo = dentro do alerta do
     * produto; negrito = lote que sai nesta venda.
     */
    private class RendererValidade extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            setHorizontalAlignment(column >= 2 ? SwingConstants.RIGHT : SwingConstants.LEFT);
            if (!isSelected) {
                long dias = ((Number) table.getValueAt(row, COL_DIAS)).longValue();
                if (dias <= 30) {
                    c.setBackground(new Color(255, 205, 205));
                } else if (dias <= diasAlerta) {
                    c.setBackground(new Color(255, 243, 190));
                } else {
                    c.setBackground(Color.WHITE);
                }
                c.setForeground(Color.BLACK);
            }
            // negrito = lote que sai nesta venda
            double retirar = Double.parseDouble(String.valueOf(table.getValueAt(row, COL_RETIRAR)));
            c.setFont(c.getFont().deriveFont(retirar > 0 ? Font.BOLD : Font.PLAIN));
            return c;
        }
    }

    // ------------------------------------------------------------------
    // Posicionamento (sem designer)
    // ------------------------------------------------------------------
    /**
     * Onde fica o painel: a direita da ancora, com a mesma altura, ate
     * larguraMax. Devolve null se nao houver espaco (menos de LARGURA_MIN).
     *
     * @param ancora rectangulo do painel de ancora, em coordenadas do
     * layeredPane
     */
    static Rectangle calcularBounds(Rectangle ancora, int larguraLayered, int larguraMax) {
        int x = ancora.x + ancora.width + GAP;
        int disponivel = larguraLayered - x - MARGEM;
        if (disponivel < LARGURA_MIN) {
            return null;
        }
        return new Rectangle(x, ancora.y, Math.min(disponivel, larguraMax), ancora.height);
    }

    /**
     * Cria o painel e instala-o a direita de "ancora" (o painel onde esta o
     * botao Fecho). Acompanha o redimensionamento/maximizacao da janela.
     *
     * @param larguraMax largura maxima do painel (ex.: 560)
     */
    public static PainelLotesVenda instalar(final Component ancora, final int larguraMax) {
        if (ancora == null) {
            throw new IllegalArgumentException("Painel de ancora não encontrado (verifique o texto do botao ou o nome do painel).");
        }
        JRootPane root = SwingUtilities.getRootPane(ancora);
        if (root == null) {
            throw new IllegalArgumentException("O painel de ancora ainda não está dentro da janela.");
        }
        final JLayeredPane camadas = root.getLayeredPane();
        final PainelLotesVenda painel = new PainelLotesVenda();
        camadas.add(painel, JLayeredPane.PALETTE_LAYER);

        final Runnable reposicionar = new Runnable() {
            @Override
            public void run() {
                Rectangle a = SwingUtilities.convertRectangle(ancora.getParent(), ancora.getBounds(), camadas);
                Rectangle r = calcularBounds(a, camadas.getWidth(), larguraMax);
                if (r == null) {
                    painel.setVisible(false);
                } else {
                    painel.setBounds(r);
                    painel.setVisible(true);
                    painel.revalidate();
                }
            }
        };
        ComponentAdapter ouvinte = new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                reposicionar.run();
            }

            @Override
            public void componentMoved(ComponentEvent e) {
                reposicionar.run();
            }

            @Override
            public void componentShown(ComponentEvent e) {
                reposicionar.run();
            }
        };
        camadas.addComponentListener(ouvinte);
        ancora.addComponentListener(ouvinte);
        reposicionar.run();
        SwingUtilities.invokeLater(reposicionar); // depois de o layout da janela assentar
        return painel;
    }

    /**
     * Procura o botao com esse texto (ex.: "Fecho") e devolve o painel que o
     * contem. Devolve null se nao existir.
     */
    public static Container encontrarPainelDoBotao(Container raiz, String textoBotao) {
        for (Component c : raiz.getComponents()) {
            if (c instanceof JButton) {
                String t = ((JButton) c).getText();
                if (t != null && t.trim().equalsIgnoreCase(textoBotao)) {
                    return c.getParent();
                }
            }
            if (c instanceof Container) {
                Container r = encontrarPainelDoBotao((Container) c, textoBotao);
                if (r != null) {
                    return r;
                }
            }
        }
        return null;
    }
}
