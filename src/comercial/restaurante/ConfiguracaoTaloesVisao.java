package comercial.restaurante;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableRowSorter;
import util.BDConexao;
import util.DVML;
import util.ui.ComponentesWeb;
import util.ui.EstiloUI;
import util.ui.FormularioWeb;

/**
 * Configuração dos talões da cozinha e do bar (pedido do utilizador em
 * 2026-11-17): em vez de abrir produto a produto, escolhe-se aqui, por
 * categoria, o que sai na cozinha, o que sai no bar/sala e o que não leva
 * talão. Grava em tb_produto.cozinha ('Enviar Ticket' / 'Enviar Sala' /
 * 'Nao Enviar Ticket') e as impressoras em tb_dados_instituicao.
 *
 * <p>
 * Só para administradores. Nada fica gravado até carregar em "Gravar"; tudo é
 * gravado numa só transacção (ligação própria, não a partilhada).
 */
public class ConfiguracaoTaloesVisao extends JDialog {

    /** Os três destinos, pela ordem em que aparecem. */
    enum Destino {
        COZINHA(DVML.ENVIAR_TICKET, "Cozinha", new Color(234, 88, 12)),
        BAR(DVML.ENVIAR_SALA, "Bar / Sala", ComponentesWeb.ROXO),
        NAO(DVML.NAO_ENVIAR_TICKET, "Sem talão", ComponentesWeb.CINZA);

        final String valor, rotulo;
        final Color cor;

        Destino(String valor, String rotulo, Color cor) {
            this.valor = valor;
            this.rotulo = rotulo;
            this.cor = cor;
        }

        static Destino de(String v) {
            for (Destino d : values()) {
                if (d.valor.equalsIgnoreCase(v == null ? "" : v.trim())) {
                    return d;
                }
            }
            return NAO;
        }

        @Override
        public String toString() {
            return rotulo;
        }
    }

    static final class Produto {

        int codigo;
        String nome, categoria;
        boolean servico;
        Destino original, actual;
    }

    private static final String TODAS = "Todas as categorias";

    private final List<Produto> produtos = new ArrayList<>();
    private final ModeloTabela modelo = new ModeloTabela();
    private final JTable tabela = new JTable(modelo);
    private final TableRowSorter<ModeloTabela> ordenador = new TableRowSorter<>(modelo);
    private final DefaultListModel<String> categorias = new DefaultListModel<>();
    private final JList<String> lista = new JList<>(categorias);
    private final Map<String, int[]> contagens = new LinkedHashMap<>();
    private final JTextField txtPesquisa = new JTextField();
    private final JTextField txtImpCozinha = new JTextField();
    private final JTextField txtImpBar = new JTextField();
    private final JLabel lbPendentes = new JLabel(" ");
    private final JButton btnGravar = new JButton("Gravar");
    private String impCozinhaOriginal = "", impBarOriginal = "";

    public ConfiguracaoTaloesVisao(java.awt.Window pai) {
        super(pai, "Talões da Cozinha e Bar", ModalityType.APPLICATION_MODAL);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                fechar();
            }
        });
        montar();
        carregar();
        FormularioWeb.ajustar(this, 1180, 760, 900, 560);
    }

    /** Abre o ecrã (só administradores). */
    public static void abrir(java.awt.Window pai) {
        if (!util.ui.SoAdministrador.verificar(pai, "Talões da Cozinha e Bar")) {
            return;
        }
        new ConfiguracaoTaloesVisao(pai).setVisible(true);
    }

    // =====================================================================
    // LAYOUT
    // =====================================================================
    private void montar() {
        JPanel raiz = new JPanel(new BorderLayout());
        raiz.setBackground(ComponentesWeb.FUNDO_PAGINA);
        setContentPane(raiz);
        raiz.add(FormularioWeb.cabecalho("Talões da Cozinha e Bar",
                "Escolha o que sai na cozinha, o que sai no bar e o que não leva talão", null), BorderLayout.NORTH);

        JPanel centro = new JPanel(new BorderLayout(0, 10));
        centro.setOpaque(false);
        centro.setBorder(BorderFactory.createEmptyBorder(12, 14, 8, 14));
        centro.add(impressoras(), BorderLayout.NORTH);

        JPanel colunas = new JPanel(new BorderLayout(12, 0));
        colunas.setOpaque(false);
        colunas.add(cartaoCategorias(), BorderLayout.WEST);
        colunas.add(cartaoProdutos(), BorderLayout.CENTER);
        centro.add(colunas, BorderLayout.CENTER);
        raiz.add(centro, BorderLayout.CENTER);

        JButton cancelar = new JButton("Fechar");
        ComponentesWeb.botao(cancelar, ComponentesWeb.Variante.SECUNDARIO, null, "Fechar (pergunta se houver alterações por gravar)");
        cancelar.addActionListener(e -> fechar());
        FormularioWeb.botaoSalvar(btnGravar, "Gravar os destinos dos produtos e as impressoras");
        btnGravar.addActionListener(e -> gravar());
        lbPendentes.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(12.5f));
        raiz.add(FormularioWeb.barraAccoes(lbPendentes, cancelar, btnGravar), BorderLayout.SOUTH);
    }

    private JComponent impressoras() {
        util.ui.EscolhaImpressora.ligar(txtImpCozinha, txtImpBar);
        FormularioWeb.campoTexto(txtImpCozinha, txtImpBar);
        txtImpCozinha.getDocument().addDocumentListener(alterou());
        txtImpBar.getDocument().addDocumentListener(alterou());
        JButton testeCozinha = new JButton("Imprimir teste");
        JButton testeBar = new JButton("Imprimir teste");
        testeCozinha.addActionListener(e -> TicketCozinha.teste(false, txtImpCozinha.getText()));
        testeBar.addActionListener(e -> TicketCozinha.teste(true, txtImpBar.getText().trim().isEmpty()
                ? txtImpCozinha.getText() : txtImpBar.getText()));
        JPanel p = ComponentesWeb.transparente(new GridLayout(1, 2, 12, 0));
        p.add(ComponentesWeb.campo("Impressora da cozinha (clique para escolher)", comTeste(txtImpCozinha, testeCozinha)));
        p.add(ComponentesWeb.campo("Impressora do bar / sala (vazia = a da cozinha)", comTeste(txtImpBar, testeBar)));
        return FormularioWeb.cartao("Impressoras", ComponentesWeb.AZUL_VIVO, p);
    }

    private static JPanel comTeste(JTextField campo, JButton teste) {
        ComponentesWeb.botao(teste, ComponentesWeb.Variante.SECUNDARIO, null, "Imprime um talão de teste nesta impressora");
        JPanel l = ComponentesWeb.transparente(new BorderLayout(8, 0));
        l.add(campo, BorderLayout.CENTER);
        l.add(teste, BorderLayout.EAST);
        return l;
    }

    private JComponent cartaoCategorias() {
        lista.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lista.setFixedCellHeight(46);
        lista.setCellRenderer(new RendererCategoria());
        lista.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                filtrar();
            }
        });
        JScrollPane sp = new JScrollPane(lista);
        sp.setBorder(BorderFactory.createLineBorder(ComponentesWeb.LINHA));
        sp.setPreferredSize(new Dimension(280, 300));
        return FormularioWeb.cartao("Categorias", ComponentesWeb.AZUL_VIVO, sp);
    }

    private JComponent cartaoProdutos() {
        FormularioWeb.campoTexto(txtPesquisa);
        txtPesquisa.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                filtrar();
            }

            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                filtrar();
            }

            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                filtrar();
            }
        });

        JPanel massa = ComponentesWeb.transparente(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        for (Destino d : Destino.values()) {
            JButton b = new JButton(d == Destino.NAO ? "Todos sem talão" : "Todos para " + (d == Destino.BAR ? "o Bar" : "a Cozinha"));
            ComponentesWeb.botao(b, d == Destino.NAO ? ComponentesWeb.Variante.SECUNDARIO : ComponentesWeb.Variante.PRIMARIO, null,
                    "Aplica a todos os produtos à vista (categoria e pesquisa actuais)");
            b.setIcon(new ComponentesWeb.IconeCor(d.cor, 10));
            b.addActionListener(e -> aplicarAVisiveis(d));
            massa.add(b);
        }
        JPanel topo = ComponentesWeb.transparente(new BorderLayout(10, 0));
        topo.add(ComponentesWeb.campo("Pesquisar produto", txtPesquisa), BorderLayout.CENTER);
        topo.add(massa, BorderLayout.SOUTH);

        tabela.setRowSorter(ordenador);
        tabela.setRowHeight(32);
        EstiloUI.estilizarTabela(tabela, true);
        tabela.getColumnModel().getColumn(0).setPreferredWidth(420);
        tabela.getColumnModel().getColumn(1).setPreferredWidth(160);
        tabela.getColumnModel().getColumn(2).setPreferredWidth(80);
        tabela.getColumnModel().getColumn(3).setPreferredWidth(160);
        JComboBox<Destino> combo = new JComboBox<>(Destino.values());
        combo.setRenderer(new javax.swing.DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> l, Object v, int i, boolean s, boolean f) {
                JLabel c = (JLabel) super.getListCellRendererComponent(l, v, i, s, f);
                if (v instanceof Destino) {
                    c.setIcon(new ComponentesWeb.IconeCor(((Destino) v).cor, 10));
                }
                return c;
            }
        });
        tabela.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(combo));
        tabela.getColumnModel().getColumn(3).setCellRenderer(new RendererDestino());
        tabela.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);

        JScrollPane sp = new JScrollPane(tabela);
        sp.setBorder(BorderFactory.createLineBorder(ComponentesWeb.LINHA));
        JPanel corpo = ComponentesWeb.transparente(new BorderLayout(0, 8));
        corpo.add(topo, BorderLayout.NORTH);
        corpo.add(sp, BorderLayout.CENTER);
        corpo.add(FormularioWeb.dica("Dica: clique na coluna \"Destino\" para mudar um produto; os botões acima mudam todos os que estão à vista."),
                BorderLayout.SOUTH);
        return FormularioWeb.cartao("Produtos e serviços", ComponentesWeb.VERDE, corpo);
    }

    private javax.swing.event.DocumentListener alterou() {
        return new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                actualizarPendentes();
            }

            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                actualizarPendentes();
            }

            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                actualizarPendentes();
            }
        };
    }

    // =====================================================================
    // DADOS
    // =====================================================================
    private void carregar() {
        produtos.clear();
        String sql = "SELECT p.codigo, p.designacao, COALESCE(t.designacao, '(sem categoria)'), p.cozinha, t.fk_familia"
                + " FROM tb_produto p LEFT JOIN tb_tipo_produto t ON t.codigo = p.cod_Tipo_Produto"
                + " WHERE p.status IS NULL OR p.status <> 'Inactivo'"
                + " ORDER BY t.designacao, p.designacao";
        try (Connection cn = BDConexao.novaLigacao()) {
            try (PreparedStatement ps = cn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Produto p = new Produto();
                    p.codigo = rs.getInt(1);
                    p.nome = rs.getString(2) == null ? "" : rs.getString(2).trim();
                    p.categoria = rs.getString(3).trim();
                    p.original = p.actual = Destino.de(rs.getString(4));
                    p.servico = rs.getInt(5) == DVML.COD_SERVICO;
                    produtos.add(p);
                }
            }
            try (PreparedStatement ps = cn.prepareStatement("SELECT impressora_cozinha, impressora_sala FROM tb_dados_instituicao LIMIT 1");
                    ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    impCozinhaOriginal = rs.getString(1) == null ? "" : rs.getString(1).trim();
                    impBarOriginal = rs.getString(2) == null ? "" : rs.getString(2).trim();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Não foi possível ler os produtos:\n" + e.getMessage(),
                    getTitle(), JOptionPane.ERROR_MESSAGE);
        }
        txtImpCozinha.setText(impCozinhaOriginal);
        txtImpBar.setText(impBarOriginal);
        modelo.fireTableDataChanged();
        recontar();
        if (lista.getSelectedIndex() < 0 && !categorias.isEmpty()) {
            // começa nas categorias de serviço (pratos) se houver
            int escolhida = 0;
            for (int i = 1; i < categorias.size(); i++) {
                if (categorias.get(i).toLowerCase().contains("prato")) {
                    escolhida = i;
                    break;
                }
            }
            lista.setSelectedIndex(escolhida);
        }
        actualizarPendentes();
    }

    /** Contagens por categoria (cozinha, bar, sem talão) e a lista da esquerda. */
    private void recontar() {
        String sel = lista.getSelectedValue();
        contagens.clear();
        int[] total = new int[3];
        contagens.put(TODAS, total);
        for (Produto p : produtos) {
            int[] c = contagens.computeIfAbsent(p.categoria, k -> new int[3]);
            c[p.actual.ordinal()]++;
            total[p.actual.ordinal()]++;
        }
        boolean mudou = categorias.size() != contagens.size();
        if (mudou) {
            categorias.clear();
            for (String k : contagens.keySet()) {
                categorias.addElement(k);
            }
            if (sel != null) {
                lista.setSelectedValue(sel, true);
            }
        }
        lista.repaint();
    }

    private void filtrar() {
        final String cat = lista.getSelectedValue();
        final String txt = txtPesquisa.getText().trim().toLowerCase();
        ordenador.setRowFilter(new RowFilter<ModeloTabela, Integer>() {
            @Override
            public boolean include(Entry<? extends ModeloTabela, ? extends Integer> e) {
                Produto p = produtos.get(e.getIdentifier());
                return (cat == null || TODAS.equals(cat) || cat.equals(p.categoria))
                        && (txt.isEmpty() || p.nome.toLowerCase().contains(txt));
            }
        });
    }

    private void aplicarAVisiveis(Destino d) {
        if (tabela.isEditing()) {
            tabela.getCellEditor().stopCellEditing();
        }
        int n = tabela.getRowCount();
        if (n == 0) {
            return;
        }
        if (n > 50 && JOptionPane.showConfirmDialog(this, "Mudar " + n + " produtos para \"" + d.rotulo + "\"?",
                getTitle(), JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
            return;
        }
        for (int i = 0; i < n; i++) {
            produtos.get(tabela.convertRowIndexToModel(i)).actual = d;
        }
        modelo.fireTableRowsUpdated(0, produtos.size() - 1);
        recontar();
        actualizarPendentes();
    }

    private int pendentes() {
        int n = 0;
        for (Produto p : produtos) {
            if (p.actual != p.original) {
                n++;
            }
        }
        return n;
    }

    private boolean impressorasMudaram() {
        return !txtImpCozinha.getText().trim().equals(impCozinhaOriginal) || !txtImpBar.getText().trim().equals(impBarOriginal);
    }

    private void actualizarPendentes() {
        int n = pendentes();
        boolean imp = impressorasMudaram();
        if (n == 0 && !imp) {
            lbPendentes.setForeground(ComponentesWeb.TEXTO_SUAVE);
            lbPendentes.setText("Sem alterações por gravar");
        } else {
            lbPendentes.setForeground(ComponentesWeb.LARANJA.darker());
            lbPendentes.setText("Por gravar: " + (n > 0 ? n + (n == 1 ? " produto" : " produtos") : "")
                    + (n > 0 && imp ? " e " : "") + (imp ? "impressoras" : ""));
        }
        btnGravar.setEnabled(n > 0 || imp);
    }

    private void gravar() {
        if (tabela.isEditing()) {
            tabela.getCellEditor().stopCellEditing();
        }
        int n = 0;
        try (Connection cn = BDConexao.novaLigacao()) {
            cn.setAutoCommit(false);
            try {
                try (PreparedStatement ps = cn.prepareStatement("UPDATE tb_produto SET cozinha = ? WHERE codigo = ?")) {
                    for (Produto p : produtos) {
                        if (p.actual != p.original) {
                            ps.setString(1, p.actual.valor);
                            ps.setInt(2, p.codigo);
                            ps.addBatch();
                            n++;
                        }
                    }
                    if (n > 0) {
                        ps.executeBatch();
                    }
                }
                if (impressorasMudaram()) {
                    try (PreparedStatement ps = cn.prepareStatement("UPDATE tb_dados_instituicao SET impressora_cozinha = ?, impressora_sala = ?")) {
                        ps.setString(1, txtImpCozinha.getText().trim());
                        ps.setString(2, txtImpBar.getText().trim());
                        ps.executeUpdate();
                    }
                }
                cn.commit();
            } catch (Exception e) {
                cn.rollback();
                throw e;
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Não foi possível gravar (nada foi alterado):\n" + e.getMessage(),
                    getTitle(), JOptionPane.ERROR_MESSAGE);
            return;
        }
        for (Produto p : produtos) {
            p.original = p.actual;
        }
        impCozinhaOriginal = txtImpCozinha.getText().trim();
        impBarOriginal = txtImpBar.getText().trim();
        actualizarPendentes();
        modelo.fireTableDataChanged();
        // o JPA guarda os produtos em memória: a gestão de mesas tem de ver os destinos novos
        try {
//            util.JPAEntityMannagerFactoryUtil.em.getEntityManagerFactory().getCache().evict(entity.TbProduto.class);
//            util.JPAEntityMannagerFactoryUtil.em.getEntityManagerFactory().getCache().evict(entity.TbDadosInstituicao.class);
        } catch (Exception ignorar) {
            // sem cache partilhada: nada a limpar
        }
//        util.ui.ConfirmacaoPositiva.mostrar(this, "Gravado", (n > 0 ? n + (n == 1 ? " produto actualizado." : " produtos actualizados.") : "Impressoras actualizadas."));
  
    
    }

    private void fechar() {
        if (tabela.isEditing()) {
            tabela.getCellEditor().stopCellEditing();
        }
        if ((pendentes() > 0 || impressorasMudaram()) && JOptionPane.showConfirmDialog(this,
                "Há alterações por gravar. Sair sem gravar?", getTitle(), JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE) != JOptionPane.YES_OPTION) {
            return;
        }
        dispose();
    }

    // =====================================================================
    // TABELA
    // =====================================================================
    final class ModeloTabela extends AbstractTableModel {

        private final String[] colunas = {"Produto / Serviço", "Categoria", "Tipo", "Destino do talão"};

        @Override
        public int getRowCount() {
            return produtos.size();
        }

        @Override
        public int getColumnCount() {
            return colunas.length;
        }

        @Override
        public String getColumnName(int c) {
            return colunas[c];
        }

        @Override
        public Class<?> getColumnClass(int c) {
            return c == 3 ? Destino.class : String.class;
        }

        @Override
        public boolean isCellEditable(int r, int c) {
            return c == 3;
        }

        @Override
        public Object getValueAt(int r, int c) {
            Produto p = produtos.get(r);
            switch (c) {
                case 0:
                    return p.nome;
                case 1:
                    return p.categoria;
                case 2:
                    return p.servico ? "Serviço" : "Produto";
                default:
                    return p.actual;
            }
        }

        @Override
        public void setValueAt(Object v, int r, int c) {
            if (c == 3 && v instanceof Destino) {
                produtos.get(r).actual = (Destino) v;
                fireTableRowsUpdated(r, r);
                recontar();
                actualizarPendentes();
            }
        }
    }

    /** Destino como etiqueta colorida; "•" quando ainda não foi gravado. */
    final class RendererDestino extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable t, Object v, boolean s, boolean f, int r, int c) {
            JLabel l = (JLabel) super.getTableCellRendererComponent(t, v, s, f, r, c);
            Destino d = (Destino) v;
            Produto p = produtos.get(t.convertRowIndexToModel(r));
            l.setIcon(new ComponentesWeb.IconeCor(d.cor, 12));
            l.setText(d.rotulo + (p.actual != p.original ? "   • por gravar" : ""));
            l.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(12.5f));
            if (!s) {
                l.setForeground(d == Destino.NAO ? ComponentesWeb.TEXTO_SUAVE : d.cor.darker());
            }
            l.setHorizontalAlignment(SwingConstants.LEFT);
            return l;
        }
    }

    /** Categoria com as três contagens em barras finas. */
    final class RendererCategoria extends JComponent implements javax.swing.ListCellRenderer<String> {

        private String nome;
        private int[] c = new int[3];
        private boolean seleccionada;

        @Override
        public Component getListCellRendererComponent(JList<? extends String> l, String v, int i, boolean s, boolean f) {
            nome = v;
            c = contagens.getOrDefault(v, new int[3]);
            seleccionada = s;
            return this;
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = ComponentesWeb.suave(g);
            int w = getWidth(), h = getHeight();
            if (seleccionada) {
                g2.setColor(ComponentesWeb.mistura(ComponentesWeb.AZUL_VIVO, Color.WHITE, 0.85));
                g2.fillRoundRect(2, 2, w - 4, h - 4, 10, 10);
            }
            g2.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(13f));
            g2.setColor(ComponentesWeb.TEXTO);
            g2.drawString(nome == null ? "" : nome, 10, 19);
            String resumo = c[0] + " cozinha · " + c[1] + " bar · " + c[2] + " sem talão";
            g2.setFont(EstiloUI.FONTE.deriveFont(Font.PLAIN, 11.5f));
            g2.setColor(ComponentesWeb.TEXTO_SUAVE);
            g2.drawString(resumo, 10, 35);
            int total = c[0] + c[1] + c[2];
            if (total > 0) {
                int x = 10, largura = w - 20;
                for (int k = 0; k < 3; k++) {
                    int bw = (int) Math.round(largura * c[k] / (double) total);
                    if (bw > 0) {
                        g2.setColor(Destino.values()[k].cor);
                        g2.fillRect(x, h - 6, bw, 3);
                        x += bw;
                    }
                }
            }
            g2.dispose();
        }
    }
}
