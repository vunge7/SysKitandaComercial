package util;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 * Lotes a expirar (mestre) + movimentos do lote seleccionado (detalhe).
 *
 * - filtro por armazém (ou todos), pesquisa por designação, dias e "já expirados";
 * - ordenado pela data de validade; expirados a vermelho; a expirar (0 a DIAS_ALERTA
 *   dias) a laranja e a piscar (o piscar pode ser desligado);
 * - o detalhe mostra compras, vendas, devoluções e transferências entre armazéns.
 */
public class FrmLotesAExpirar extends JFrame {

    /** Tipos de operação (igual ao ENUM de lote_movimento.tipo). */
    public enum Operacao {
        VENDA("VENDA"),
        DEVOLUCAO("DEVOLUÇÃO"),
        COMPRA("COMPRA"),
        TRANSF_SAIDA("TRANSF. SAÍDA"),
        TRANSF_ENTRADA("TRANSF. ENTRADA");

        private final String rotulo;

        Operacao(String rotulo) {
            this.rotulo = rotulo;
        }

        public String getRotulo() {
            return rotulo;
        }

        /** Devolve null (em vez de rebentar) se aparecer um tipo desconhecido. */
        static Operacao de(String tipo) {
            if (tipo == null) {
                return null;
            }
            try {
                return Operacao.valueOf(tipo.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                return null;
            }
        }
    }

    /** Item da ComboBox de armazéns. */
    private static class ArmazemItem {

        final int id;
        final String designacao;

        ArmazemItem(int id, String designacao) {
            this.id = id;
            this.designacao = designacao;
        }

        @Override
        public String toString() {
            return designacao;
        }
    }

    // ------------------------------------------------------------------
    // Configuração
    // ------------------------------------------------------------------
    /** Id usado para "Todos os armazéns". */
    private static final int TODOS = -1;

    /** Armazém seleccionado ao abrir: 0 = Todos, 1 = 1.º armazém, 2 = 2.º (Farmácia)... */
    private static final int ARMAZEM_INICIAL_INDICE = 0;

    /** Lotes que expiram em 0..DIAS_ALERTA dias ficam laranja e piscam. */
    private static final int DIAS_ALERTA = 30;

    /** true: a expirar em cima e os já expirados (vermelho) no fim; false: tudo por data (expirados primeiro). */
    private static final boolean EXPIRADOS_NO_FIM = true;

    /** true: ao carregar, selecciona a 1.ª linha (mas uma linha seleccionada não pisca). */
    private static final boolean SELECIONAR_PRIMEIRA_LINHA = false;

    // Cores das linhas
    private static final Color COR_EXPIRADO = new Color(255, 170, 170);       // vermelho claro
    private static final Color COR_A_EXPIRAR = new Color(255, 204, 153);      // laranja claro
    private static final Color COR_A_EXPIRAR_FORTE = new Color(255, 120, 0);  // laranja forte (piscar)
    private static final Color COR_AVISO = new Color(255, 245, 170);          // amarelo claro (> DIAS_ALERTA)

    // Índices das colunas de tblLotes
    private static final int COL_ARMAZEM = 3;
    private static final int COL_QTD = 4;
    private static final int COL_DIAS = 7;
    private static final int COL_ESTADO = 8;

    // ------------------------------------------------------------------
    // SQL
    // ------------------------------------------------------------------
    // lote.fk_armazem (confirmado). ASSUMIDO: tb_armazem(codigo, designacao).
    private static final String SQL_ARMAZENS
            = "SELECT codigo, designacao FROM tb_armazem ORDER BY codigo ASC";

    private static final String ORDEM_LOTES = EXPIRADOS_NO_FIM
            ? "ORDER BY (l.data_validade < CURDATE()) ASC, l.data_validade ASC, p.designacao ASC, l.id ASC"
            : "ORDER BY l.data_validade ASC, p.designacao ASC, l.id ASC";

    private static final String SQL_LOTES
            = "SELECT l.id, p.codigo, p.designacao, l.numero_lote, COALESCE( a.designacao, '-' ) AS armazem, "
            + "       l.qtd, l.data_fabrica, l.data_validade, "
            + "       DATEDIFF( l.data_validade, CURDATE() ) AS dias_restantes, "
            + "       CASE WHEN l.data_validade < CURDATE() THEN 'EXPIRADO' ELSE l.estado END AS estado "
            + "FROM lote l "
            + "INNER JOIN tb_produto p ON p.codigo = l.fk_produto "
            + "LEFT JOIN tb_armazem a ON a.codigo = l.fk_armazem "
            + "WHERE l.qtd > 0 AND l.estado IN ('ACTIVO','EXPIRADO') "
            + "  AND l.data_validade <= DATE_ADD( CURDATE(), INTERVAL ? DAY ) "   // 1: dias
            + "  AND ( ? = 1 OR l.data_validade >= CURDATE() ) "                  // 2: incluir expirados
            + "  AND ( ? = " + TODOS + " OR l.fk_armazem = ? ) "                   // 3 e 4: armazém
            + "  AND p.designacao LIKE ? "                                         // 5: pesquisa
            + ORDEM_LOTES;

    // Movimentos do lote. Para transferências, o nome do utilizador vem de transferencia_armazem.
    // ASSUMIDO: compras(pk_compra, codigo_usuario), tb_venda(codigo, codigo_usuario), tb_usuario(codigo, nome)
    private static final String SQL_MOVIMENTOS
            = "SELECT m.data_mov, m.qtd, m.tipo, m.fk_operacao, "
            + "       COALESCE( uc.nome, uv.nome, t.nome_usuario, '' ) AS usuario "
            + "FROM lote_movimento m "
            + "LEFT JOIN compras c   ON m.tipo = 'COMPRA' AND c.pk_compra = m.fk_operacao "
            + "LEFT JOIN tb_usuario uc ON uc.codigo = c.codigo_usuario "
            + "LEFT JOIN tb_venda v  ON m.tipo IN ('VENDA','DEVOLUCAO') AND v.codigo = m.fk_operacao "
            + "LEFT JOIN tb_usuario uv ON uv.codigo = v.codigo_usuario "
            + "LEFT JOIN transferencia_armazem t ON m.tipo IN ('TRANSF_SAIDA','TRANSF_ENTRADA') "
            + "                                 AND t.pk_transferencia_armazem = m.fk_operacao "
            + "WHERE m.fk_lote = ? "
            + "ORDER BY m.data_mov ASC, m.id ASC";

    // Armazém do "outro lado" de uma transferência: o lote do mesmo produto e nº de lote que teve
    // o movimento oposto (TRANSF_ENTRADA <-> TRANSF_SAIDA) na mesma transferência.
    // Parâmetros: 1 = id do lote, 2 = pk da transferência, 3 = tipo oposto.
    private static final String SQL_ARMAZEM_LIGADO
            = "SELECT a2.designacao "
            + "FROM lote l1 "
            + "INNER JOIN lote l2 ON l2.fk_produto = l1.fk_produto AND l2.numero_lote = l1.numero_lote "
            + "INNER JOIN lote_movimento m2 ON m2.fk_lote = l2.id "
            + "INNER JOIN tb_armazem a2 ON a2.codigo = l2.fk_armazem "
            + "WHERE l1.id = ? AND m2.fk_operacao = ? AND m2.tipo = ? "
            + "LIMIT 1";

    private static final String[] COLS_LOTES = {
        "Código", "Designação", "Nº Lote", "Armazém", "Qtd", "Data Fabrico", "Data Validade", "Dias Restantes", "Estado"};
    private static final String[] COLS_MOV = {
        "Data Movimento", "Qtd", "Operação", "Cod.Operação", "Origem/Destino", "Usuário"};

    private final BDConexao conexao;
    private final SimpleDateFormat fmtData = new SimpleDateFormat("dd/MM/yyyy");
    private final SimpleDateFormat fmtDataHora = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    private final NumberFormat fmtQtd = NumberFormat.getNumberInstance(new Locale("pt", "AO"));

    private final JTable tblLotes;
    private final JTable tblMov;
    private final DefaultTableModel modLotes = novoModelo(COLS_LOTES);
    private final DefaultTableModel modMov = novoModelo(COLS_MOV);
    private final JComboBox<ArmazemItem> cmbArmazem = new JComboBox<>();
    private final JTextField txtPesquisa = new JTextField(22);
    private final JSpinner spDias = new JSpinner(new SpinnerNumberModel(90, 1, 3650, 1));
    private final JCheckBox chkExpirados = new JCheckBox("Incluir já expirados com stock", true);
    private final JCheckBox chkPiscar = new JCheckBox("Piscar alertas/Parar de piscar alertas", true); // por defeito pisca
    private final JLabel lblTotal = new JLabel(" ");
    private final JLabel lblDetalhe = new JLabel(" ");

    /** id do lote de cada linha de tblLotes (a tabela não tem sorter, índices coincidem). */
    private final List<Integer> idsLotes = new ArrayList<>();

    // Piscar e recarga com espera (debounce)
    private javax.swing.Timer piscarTimer;
    private javax.swing.Timer recargaTimer;
    private boolean piscarLigado = false;
    private boolean temUrgentes = false;

    public FrmLotesAExpirar(BDConexao conexao) {
        super("Lotes a expirar");
        this.conexao = conexao;
        fmtQtd.setMaximumFractionDigits(3);

        tblLotes = criarTabela(modLotes);
        tblLotes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblMov = criarTabela(modMov);

        configurarRenderers();
        montarLayout();

        tblLotes.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                carregarMovimentos();
            }
        });

        // armazéns carregados e seleccionados ANTES de ligar os listeners (evita consultas a mais)
        carregarArmazens();
        ligarListeners();
        iniciarPiscar();
        carregarLotes();

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                if (piscarTimer != null) {
                    piscarTimer.stop();
                }
                if (recargaTimer != null) {
                    recargaTimer.stop();
                }
            }
        });
        setSize(1100, 680);
        setLocationRelativeTo(null);
    }

    // ------------------------------------------------------------------
    // UI
    // ------------------------------------------------------------------
    private void montarLayout() {
        cmbArmazem.setPreferredSize(new Dimension(220, 26));
        txtPesquisa.setToolTipText("Escreva parte da designação do produto");

        JPanel linha1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        linha1.add(new JLabel("Armazém:"));
        linha1.add(cmbArmazem);
        linha1.add(new JLabel("Pesquisar produto:"));
        linha1.add(txtPesquisa);

        JButton btnActualizar = new JButton("Actualizar");
        btnActualizar.addActionListener(e -> carregarLotes());

        JPanel linha2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        linha2.add(new JLabel("A vencer nos próximos"));
        linha2.add(spDias);
        linha2.add(new JLabel("dias"));
        linha2.add(chkExpirados);
        linha2.add(chkPiscar);
        linha2.add(btnActualizar);

        JPanel topo = new JPanel(new GridLayout(2, 1));
        topo.add(linha1);
        topo.add(linha2);

        JPanel painelLotes = new JPanel(new BorderLayout());
        painelLotes.setBorder(BorderFactory.createTitledBorder("Lotes (por data de validade)"));
        painelLotes.add(new JScrollPane(tblLotes), BorderLayout.CENTER);
        painelLotes.add(lblTotal, BorderLayout.SOUTH);

        JPanel painelMov = new JPanel(new BorderLayout());
        painelMov.setBorder(BorderFactory.createTitledBorder("Detalhe do lote seleccionado"));
        painelMov.add(new JScrollPane(tblMov), BorderLayout.CENTER);
        painelMov.add(lblDetalhe, BorderLayout.SOUTH);

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, painelLotes, painelMov);
        split.setResizeWeight(0.6);

        JButton btnSair = new JButton("Sair");
        btnSair.addActionListener(e -> dispose());
        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rodape.add(btnSair);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(topo, BorderLayout.NORTH);
        getContentPane().add(split, BorderLayout.CENTER);
        getContentPane().add(rodape, BorderLayout.SOUTH);
    }

    private static DefaultTableModel novoModelo(String[] colunas) {
        return new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    private static JTable criarTabela(DefaultTableModel modelo) {
        JTable t = new JTable(modelo);
        t.setRowHeight(24);
        t.getTableHeader().setReorderingAllowed(false);
        t.setFillsViewportHeight(true);
        return t;
    }

    private void configurarRenderers() {
        DefaultTableCellRenderer direita = new DefaultTableCellRenderer();
        direita.setHorizontalAlignment(SwingConstants.RIGHT);
        DefaultTableCellRenderer centro = new DefaultTableCellRenderer();
        centro.setHorizontalAlignment(SwingConstants.CENTER);

        // Lotes: linha inteira pintada (vermelho = expirado, laranja a piscar = a expirar)
        for (int c = 0; c < tblLotes.getColumnCount(); c++) {
            int alinhamento = SwingConstants.CENTER;
            if (c == 1 || c == 2 || c == COL_ARMAZEM) {
                alinhamento = SwingConstants.LEFT;
            } else if (c == COL_QTD) {
                alinhamento = SwingConstants.RIGHT;
            }
            tblLotes.getColumnModel().getColumn(c).setCellRenderer(new RendererLote(alinhamento));
        }

        tblMov.getColumnModel().getColumn(0).setCellRenderer(centro);
        tblMov.getColumnModel().getColumn(1).setCellRenderer(direita);
        tblMov.getColumnModel().getColumn(2).setCellRenderer(centro);
        tblMov.getColumnModel().getColumn(3).setCellRenderer(centro);

        tblLotes.getColumnModel().getColumn(1).setPreferredWidth(280);
        tblLotes.getColumnModel().getColumn(COL_ARMAZEM).setPreferredWidth(110);
        tblLotes.getColumnModel().getColumn(COL_DIAS).setPreferredWidth(110);
        tblMov.getColumnModel().getColumn(4).setPreferredWidth(150);
    }

    /** Pinta a linha conforme os dias restantes. */
    private class RendererLote extends DefaultTableCellRenderer {

        RendererLote(int alinhamento) {
            setHorizontalAlignment(alinhamento);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (!isSelected && row < modLotes.getRowCount()) {
                int dias = (Integer) modLotes.getValueAt(row, COL_DIAS);
                Color fundo;
                if (dias < 0) {
                    fundo = COR_EXPIRADO;
                } else if (dias <= DIAS_ALERTA) {
                    fundo = piscarLigado ? COR_A_EXPIRAR_FORTE : COR_A_EXPIRAR; // pisca
                } else {
                    fundo = COR_AVISO;
                }
                c.setBackground(fundo);
                c.setForeground(Color.BLACK);
            }
            return c;
        }
    }

    // ------------------------------------------------------------------
    // Listeners, piscar e recarga
    // ------------------------------------------------------------------
    private void ligarListeners() {
        cmbArmazem.addActionListener(e -> carregarLotes());
        chkExpirados.addActionListener(e -> carregarLotes());

        spDias.addChangeListener(e -> agendarRecarga());
        JFormattedTextField tf = ((JSpinner.DefaultEditor) spDias.getEditor()).getTextField();
        tf.getDocument().addDocumentListener(aoAlterarTexto());
        txtPesquisa.getDocument().addDocumentListener(aoAlterarTexto());

        chkPiscar.addActionListener(e -> {
            if (!chkPiscar.isSelected()) {
                piscarLigado = false; // fica na cor fixa
            }
            tblLotes.repaint();
        });
    }

    private DocumentListener aoAlterarTexto() {
        return new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                agendarRecarga();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                agendarRecarga();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                agendarRecarga();
            }
        };
    }

    /** Espera 300 ms depois da última tecla antes de consultar a BD. */
    private void agendarRecarga() {
        if (recargaTimer == null) {
            recargaTimer = new javax.swing.Timer(300, e -> carregarLotes());
            recargaTimer.setRepeats(false);
        }
        recargaTimer.restart();
    }

    private void iniciarPiscar() {
        piscarTimer = new javax.swing.Timer(500, e -> {
            piscarLigado = chkPiscar.isSelected() && !piscarLigado;
            if (temUrgentes) {
                tblLotes.repaint();
            }
        });
        piscarTimer.start();
    }

    /** Lê o nº de dias directamente do texto do spinner (mesmo antes de carregar Enter). */
    private int obterDias() {
        String txt = ((JSpinner.DefaultEditor) spDias.getEditor()).getTextField()
                .getText().replaceAll("[^0-9]", "");
        if (txt.isEmpty()) {
            return -1;
        }
        try {
            int d = Integer.parseInt(txt);
            return (d >= 1 && d <= 3650) ? d : -1;
        } catch (NumberFormatException ex) {
            return -1;
        }
    }

    // ------------------------------------------------------------------
    // Dados
    // ------------------------------------------------------------------
    private void carregarArmazens() {
        cmbArmazem.addItem(new ArmazemItem(TODOS, "Todos os armazéns"));
        try (PreparedStatement ps = conexao.getConnection().prepareStatement(SQL_ARMAZENS);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                cmbArmazem.addItem(new ArmazemItem(rs.getInt("codigo"), rs.getString("designacao")));
            }
        } catch (SQLException e) {
            erro("Erro ao carregar armazéns", e);
        }
        int n = cmbArmazem.getItemCount();
        cmbArmazem.setSelectedIndex(ARMAZEM_INICIAL_INDICE < n ? ARMAZEM_INICIAL_INDICE : 0);
    }

    private void carregarLotes() {
        int dias = obterDias();
        if (dias < 0) {
            return; // campo vazio ou inválido: mantém a lista anterior
        }
        ArmazemItem armazem = (ArmazemItem) cmbArmazem.getSelectedItem();
        int idArmazem = (armazem == null) ? TODOS : armazem.id;

        modLotes.setRowCount(0);
        modMov.setRowCount(0);
        idsLotes.clear();
        int urgentes = 0;
        int expirados = 0;

        try (PreparedStatement ps = conexao.getConnection().prepareStatement(SQL_LOTES)) {
            ps.setInt(1, dias);
            ps.setInt(2, chkExpirados.isSelected() ? 1 : 0);
            ps.setInt(3, idArmazem);
            ps.setInt(4, idArmazem);
            ps.setString(5, "%" + escaparLike(txtPesquisa.getText().trim()) + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int diasRestantes = rs.getInt("dias_restantes");
                    if (diasRestantes < 0) {
                        expirados++;
                    } else if (diasRestantes <= DIAS_ALERTA) {
                        urgentes++;
                    }
                    idsLotes.add(rs.getInt("id"));
                    modLotes.addRow(new Object[]{
                        rs.getInt("codigo"),
                        rs.getString("designacao"),
                        rs.getString("numero_lote"),
                        rs.getString("armazem"),
                        fmtQtd.format(rs.getDouble("qtd")),
                        formatar(rs.getDate("data_fabrica")),
                        formatar(rs.getDate("data_validade")),
                        diasRestantes,
                        rs.getString("estado")
                    });
                }
            }
        } catch (SQLException e) {
            erro("Erro ao carregar lotes", e);
        }

        temUrgentes = urgentes > 0;
        lblTotal.setText(" " + modLotes.getRowCount() + " lote(s)   |   a expirar (até " + DIAS_ALERTA
                + " dias): " + urgentes + "   |   expirados: " + expirados
                + "   |   Vermelho: expirado   Laranja (a piscar): até " + DIAS_ALERTA
                + " dias   Amarelo: mais de " + DIAS_ALERTA + " dias");

        if (SELECIONAR_PRIMEIRA_LINHA && modLotes.getRowCount() > 0) {
            tblLotes.setRowSelectionInterval(0, 0); // dispara o carregamento do detalhe
        } else {
            carregarMovimentos(); // limpa o detalhe
        }
    }

    private void carregarMovimentos() {
        modMov.setRowCount(0);
        int row = tblLotes.getSelectedRow();
        if (row < 0 || row >= idsLotes.size()) {
            lblDetalhe.setText(" Seleccione um lote para ver os movimentos (compras, vendas, devoluções e transferências).");
            return;
        }

        try (PreparedStatement ps = conexao.getConnection().prepareStatement(SQL_MOVIMENTOS)) {
            int idLote = idsLotes.get(row);
            ps.setInt(1, idLote);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String tipo = rs.getString("tipo");
                    Operacao op = Operacao.de(tipo);
                    String ligado = null;
                    if (op == Operacao.TRANSF_SAIDA || op == Operacao.TRANSF_ENTRADA) {
                        ligado = armazemLigado(idLote, rs.getInt("fk_operacao"),
                                op == Operacao.TRANSF_SAIDA ? "TRANSF_ENTRADA" : "TRANSF_SAIDA");
                    }

                    String origemDestino = "";
                    if (ligado != null) {
                        if (op == Operacao.TRANSF_SAIDA) {
                            origemDestino = "Destino: " + ligado;
                        } else if (op == Operacao.TRANSF_ENTRADA) {
                            origemDestino = "Origem: " + ligado;
                        }
                    }

                    modMov.addRow(new Object[]{
                        rs.getTimestamp("data_mov") == null ? "" : fmtDataHora.format(rs.getTimestamp("data_mov")),
                        fmtQtd.format(rs.getDouble("qtd")),
                        op != null ? op.getRotulo() : (tipo == null ? "" : tipo),
                        rs.getInt("fk_operacao"),
                        origemDestino,
                        rs.getString("usuario")
                    });
                }
            }
            lblDetalhe.setText(" " + modMov.getRowCount() + " movimento(s)");
        } catch (SQLException e) {
            erro("Erro ao carregar movimentos", e);
        }
    }

    /** Nome do armazém do outro lado da transferência (null se não encontrar; nunca bloqueia o detalhe). */
    private String armazemLigado(int idLote, int idOperacao, String tipoOposto) {
        try (PreparedStatement ps = conexao.getConnection().prepareStatement(SQL_ARMAZEM_LIGADO)) {
            ps.setInt(1, idLote);
            ps.setInt(2, idOperacao);
            ps.setString(3, tipoOposto);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getString(1) : null;
            }
        } catch (SQLException e) {
            e.printStackTrace(); // informação extra: se falhar, o detalhe aparece na mesma
            return null;
        }
    }

    /** Escapa % e _ para a pesquisa LIKE tratar o texto à letra. */
    private static String escaparLike(String texto) {
        return texto.replace("\\", "\\\\").replace("%", "\\%").replace("_", "\\_");
    }

    private String formatar(java.util.Date d) {
        return d == null ? "" : fmtData.format(d);
    }

    private void erro(String msg, Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, msg + ":\n" + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
    }
}
