package visao;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Base64;
import java.util.HashMap;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import tesouraria.novo.util.AnyReport;
import util.BDConexao;
// TODO: mantem a tua linha "package ...;" no topo e ajusta os imports abaixo
// import comercial.....BDConexao;
// import comercial.....AnyReport;

/**
 * Formulário do INVENTÁRIO dos armazéns.
 *
 * Filtros: armazém, produto (combobox, designação, código interno, código de
 * barras, código manual ou lupa), situação dos lotes, alerta de validade (dias)
 * e incluir/excluir stock zero. "Inventário com Valores" acrescenta preço e
 * valor do stock.
 */
public class InventariosVisao extends JFrame {

    private static final String REPORT = "relatorio_inventario.jasper";
    private static final String REPORT_VALORES = "relatorio_inventario_valores.jasper";

    /** Icone da lupa (PNG 24x24 embutido: nao precisa de ficheiro nem de pasta de recursos). */
    private static final String LUPA_PNG_BASE64 = "iVBORw0KGgoAAAANSUhEUgAAABgAAAAYCAYAAADgdz34AAAEg0lEQVR4nK2V/0/VVRjH3+ece+fl3isKlkAI6NXcaribjgplefuhVX7BLxWbxmZh4GiJFVSgWzFW84fmH9DSpVZY44sZytClKNylRXzJoShf7gW5DAm4CNwvHy738zlPP3BvMQPE5bOd7bPnnM/zep73nvMc4L/GysrKxAx+hPxspr3Z7P7DDAABQFXVL4k6RsleZXyh0Whyt9/tay7IyRkBgOLiYl5SUiIfCmCz2XR1dXXq0aMnn5oMKp/8XFkZ1+l0jJrNZtXn80W+sGF9VLLV2mgwGL7Iy8tzzxfCpmd06lTZztorFwsv1FRX7d6d6Vy0KFo1GCLcN286huz2GpNOUNZrr+9OiYw07yssLGyeL4QDwLGvjm18c9euqxaLZefg4PARIjpHREeI6G0iWsnZVLFWqzXr0MGDjXa7PTGc3FzBdUREjDFzj6v7o3Zn35cOhyMbkOsAnNY0rVsIca28vLynpqXFpE1MiM2pqceXLIlZEZhUDzHOc0tKSmjO7BljtGNH+uZ2x8Do2fP2EQAuTeI6pPQLIVx+wJOekZHwnNX6+DKr9bFOooQFlje+N0SYVn+Qm7sKU00xa2dxAIiPX5b2fNorA3FROBzUtDvg/AI4D6qARw8kCSBhAbAsxmB4IqgELXnFOYalcZaBALA+JNOsAB0AA+f6mKfXpNwDYNYLtgXA2SDQS0D89MN6AHqh5+RDVMLyZNnrbF06lzxhABmNJhiNkXEAPAA0AM8S0KEBExzgEkB4qZBcC3JPbExMrJRqYD6AwJ3ujl6/4n7VhyVjJpAxKLEwyLEKEirjUyJrAIIAPONg0BBYoPMmjoyM3HgQgAkhoNczW8WZS2WWlI33zBGabqFRBFQgKADioeAagHEJ1tisqeuSRPTta5UjXx89sbG6utpPNEcjEREDwLP2ZP44ND5JlzvJW+fSlPZJUlxESh+R0kuktPpIqWhR/b86ydN6q20iLTUtPfT/nPcAABibukTLD3/+WatHkXSli6i8ieS5WyRrOkhW3SBZ0USypZcoMOmn0tIflObG5neJyDQvCBExIQQArM7O2lPd2PIn9f41SR3DRG1DRF3DRH2DAbLXX1K2bd125NvSUw2//9FEPS7XeSJaEYqhmzH7ad+ccy6llCaTybT9pRc3bH9585bVUVHRhrv9rrEzFaeb7L9dLwXUq3uzszOt1rXHg6qm37Ft6+2VlhVvMcYaiEgwxrTZAADAOOck5T/zazGm2t8DYCIsB2MM+w/sL05es/ZTn19h6Vs3DT5psexljFWH9h88AMOyCSHAOQ8PNRZKAgD0H+bnV1b8VEXfnPxOOhxOHxHtmkuumeRjmBon91fKdTodACwtKiq6XFtXTxdrr1B//4CfiHaGIDO+ig9jIlRJQkHBx1cbmlqos8spR8c8Y263e1MI8lDP60wW7r74/IKC+rb2DvL6FBrzeIa8Xu+mjIyM/10FAIiQXPHvHXi/tst5h4iI7HZ7XVJS0uJHAQD+lSv2nex9F44dP9Fks9meeVTBwxburlgAiWHn38PLPdzD5i+eAAAAAElFTkSuQmCC";
    private static final String TODOS_PRODUTOS = "TODOS OS PRODUTOS";

    private final JComboBox<ItemCombo> cmbArmazem = new JComboBox<>();
    private final JComboBox<ItemSituacao> cmbSituacao = new JComboBox<>();
    private final JSpinner spDias = new JSpinner(new SpinnerNumberModel(90, 1, 3650, 1));

    private final JTextField txtDesignacao = new JTextField();
    private final JTextField txtCodInterno = new JTextField();
    private final JTextField txtCodBarra = new JTextField();
    private final JTextField txtCodManual = new JTextField();
    private final JComboBox<ItemCombo> cmbProduto = new JComboBox<>();
    private final JButton btnLupa = new JButton();
    private final JCheckBox chkValores = new JCheckBox("Inventário com Valores");
    private final JCheckBox chkIncluirZero = new JCheckBox("Incluir produtos sem stock (qtd. zero)");

    private final JButton btnGerar = new JButton("Gerar inventário");
    private final JButton btnFechar = new JButton("Fechar");

    /** true enquanto o programa mexe na combo (evita reagir a eventos que nao sao do utilizador). */
    private boolean adjustando = false;

    /** Item de combo: id 0 = "todos". */
    private static class ItemCombo {

        final int id;
        final String nome;

        ItemCombo(int id, String nome) {
            this.id = id;
            this.nome = nome;
        }

        @Override
        public String toString() {
            return nome;
        }
    }

    /** Item do filtro de situação: o código vai para o SQL, o nome para o título. */
    private static class ItemSituacao {

        final String codigo;
        final String nome;

        ItemSituacao(String codigo, String nome) {
            this.codigo = codigo;
            this.nome = nome;
        }

        @Override
        public String toString() {
            return nome;
        }
    }

    public InventariosVisao() {
        super("Inventário");
        cmbSituacao.addItem(new ItemSituacao("TODAS", "TODAS"));
        cmbSituacao.addItem(new ItemSituacao("VENCIDO", "VENCIDOS"));
        cmbSituacao.addItem(new ItemSituacao("A_VENCER", "A VENCER"));
        cmbSituacao.addItem(new ItemSituacao("OK", "VÁLIDOS"));
        cmbSituacao.addItem(new ItemSituacao("SEM_LOTE", "SEM LOTE"));
        cmbSituacao.addItem(new ItemSituacao("DIVERGENTE", "DIVERGENTES (lotes ≠ stock)"));
        chkIncluirZero.setSelected(false);

        configurarLupa();
        carregarArmazens();
        carregarProdutos("", null); // todos

        JPanel campos = new JPanel(new GridBagLayout());
        campos.setBorder(new EmptyBorder(12, 12, 6, 12));
        int y = 0;
        addLinha(campos, y++, "Armazém:", cmbArmazem, null, null);
        addLinha(campos, y++, "Situação dos lotes:", cmbSituacao, "Alerta de validade (dias):", spDias);
        addLinha(campos, y++, "Pesquisar pela Designação:", txtDesignacao, null, null);
        addLinha(campos, y++, "Pesquisar por Código Interno:", txtCodInterno,
                "Pesquisar por Código de Barras:", txtCodBarra);
        addLinha(campos, y++, "Pesquisar por Código Manual:", txtCodManual, null, null);

        // produto + lupa
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(3, 3, 3, 3);
        g.gridy = y;
        g.gridx = 0;
        g.anchor = GridBagConstraints.WEST;
        campos.add(new JLabel("Produto:"), g);
        g.gridx = 1;
        g.gridwidth = 2;
        g.weightx = 1;
        g.fill = GridBagConstraints.HORIZONTAL;
        campos.add(cmbProduto, g);
        g.gridx = 3;
        g.gridwidth = 1;
        g.weightx = 0;
        g.fill = GridBagConstraints.NONE;
        campos.add(btnLupa, g);

        // opções: valores (preço e valor do stock) e stock zero
        JPanel opcoes = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        opcoes.add(chkValores);
        opcoes.add(Box.createHorizontalStrut(24));
        opcoes.add(chkIncluirZero);
        g.gridy = y + 1;
        g.gridx = 1;
        g.gridwidth = 3;
        g.weightx = 1;
        g.fill = GridBagConstraints.HORIZONTAL;
        campos.add(opcoes, g);

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        botoes.add(btnGerar);
        botoes.add(btnFechar);

        setLayout(new BorderLayout());
        add(campos, BorderLayout.CENTER);
        add(botoes, BorderLayout.SOUTH);

        // --- pesquisas: Enter em cada campo (a designação também filtra ao escrever)
        txtDesignacao.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                pesquisarDesignacao();
            }
        });
        txtCodInterno.addActionListener(e -> pesquisarCodInterno());
        txtCodBarra.addActionListener(e -> pesquisarCodBarra());
        txtCodManual.addActionListener(e -> pesquisarCodManual());
        btnLupa.addActionListener(e -> abrirLupa());
        cmbProduto.addActionListener(e -> {
            if (adjustando) {
                return;
            }
            ItemCombo it = (ItemCombo) cmbProduto.getSelectedItem();
            if (it == null) {
                return;
            }
            if (it.id == 0) {
                limparCamposExcepto(null);
            } else {
                preencherCampos(it.id, true);
            }
        });

        btnGerar.addActionListener(e -> gerar());
        btnFechar.addActionListener(e -> dispose());

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(780, 420);
        setResizable(false);
        setLocationRelativeTo(null);
    }

    // ------------------------------------------------------------------
    // layout
    // ------------------------------------------------------------------
    private void configurarLupa() {
        try {
            byte[] png = Base64.getDecoder().decode(LUPA_PNG_BASE64);
            ImageIcon icone = new ImageIcon(png);
            btnLupa.setIcon(new ImageIcon(icone.getImage().getScaledInstance(
                    icone.getIconWidth(), icone.getIconHeight(), Image.SCALE_SMOOTH)));
            btnLupa.setPreferredSize(new Dimension(icone.getIconWidth() + 10, icone.getIconHeight() + 6));
        } catch (Exception ex) {
            btnLupa.setText("Lupa"); // se o icone falhar, fica o texto
        }
        btnLupa.setToolTipText("Pesquisar produto");
        btnLupa.setFocusPainted(false);
        btnLupa.setMargin(new Insets(2, 2, 2, 2));
    }

    private void addLinha(JPanel p, int y, String l1, java.awt.Component c1, String l2, java.awt.Component c2) {
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(3, 3, 3, 3);
        g.gridy = y;
        g.anchor = GridBagConstraints.WEST;

        g.gridx = 0;
        p.add(new JLabel(l1), g);

        g.gridx = 1;
        g.weightx = 1;
        g.fill = GridBagConstraints.HORIZONTAL;
        if (l2 == null) {
            g.gridwidth = 3; // campo ocupa a linha toda
            p.add(c1, g);
            return;
        }
        p.add(c1, g);

        g.gridx = 2;
        g.weightx = 0;
        g.fill = GridBagConstraints.NONE;
        p.add(new JLabel(l2), g);

        g.gridx = 3;
        g.weightx = 1;
        g.fill = GridBagConstraints.HORIZONTAL;
        p.add(c2, g);
    }

    // ------------------------------------------------------------------
    // dados
    // ------------------------------------------------------------------
    private void carregarArmazens() {
        cmbArmazem.addItem(new ItemCombo(0, "TODOS OS ARMAZÉNS"));
        try (Connection cn = BDConexao.getConnection();
                PreparedStatement ps = cn.prepareStatement("SELECT codigo, designacao FROM tb_armazem ORDER BY designacao");
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                cmbArmazem.addItem(new ItemCombo(rs.getInt(1), rs.getString(2)));
            }
        } catch (Exception ex) {
            erro("Erro ao carregar armazéns: ", ex);
        }
    }

    /**
     * Enche a combobox de produtos. Primeiro item: TODOS OS PRODUTOS.
     *
     * @param condicao fragmento SQL (ex.: "designacao LIKE ?") ou "" para todos
     * @param valor valor do parâmetro da condição (null se não houver)
     * @return número de produtos carregados
     */
    private int carregarProdutos(String condicao, String valor) {
        adjustando = true;
        cmbProduto.removeAllItems();
        cmbProduto.addItem(new ItemCombo(0, TODOS_PRODUTOS));
        int n = 0;
        String sql = "SELECT codigo, designacao FROM tb_produto"
                + (condicao.isEmpty() ? "" : " WHERE " + condicao)
                + " ORDER BY designacao LIMIT 5000";
        try (Connection cn = BDConexao.getConnection();
                PreparedStatement ps = cn.prepareStatement(sql)) {
            if (valor != null) {
                ps.setString(1, valor);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    cmbProduto.addItem(new ItemCombo(rs.getInt(1), rs.getString(2)));
                    n++;
                }
            }
        } catch (Exception ex) {
            erro("Erro ao pesquisar produtos: ", ex);
        } finally {
            adjustando = false;
        }
        return n;
    }

    // ------------------------------------------------------------------
    // pesquisas
    // ------------------------------------------------------------------
    private void limparCamposExcepto(JTextField manter) {
        for (JTextField t : new JTextField[]{txtDesignacao, txtCodInterno, txtCodBarra, txtCodManual}) {
            if (t != manter) {
                t.setText("");
            }
        }
    }

    /** Selecciona o 1.o produto da combo (sem disparar o listener) e preenche os campos. */
    private void seleccionarPrimeiro(boolean incluirDesignacao) {
        if (cmbProduto.getItemCount() < 2) {
            return;
        }
        adjustando = true;
        cmbProduto.setSelectedIndex(1);
        adjustando = false;
        preencherCampos(((ItemCombo) cmbProduto.getItemAt(1)).id, incluirDesignacao);
    }

    /**
     * Preenche designação, código interno, código de barras e código manual
     * do produto. Enquanto o utilizador escreve a designação, esta não é
     * reescrita (incluirDesignacao = false).
     */
    private void preencherCampos(int idProduto, boolean incluirDesignacao) {
        String sql = "SELECT codigo, designacao, codBarra, codigo_manual FROM tb_produto WHERE codigo = ?";
        try (Connection cn = BDConexao.getConnection();
                PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, idProduto);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    if (incluirDesignacao) {
                        txtDesignacao.setText(nvl(rs.getString(2)));
                    }
                    txtCodInterno.setText(String.valueOf(rs.getInt(1)));
                    txtCodBarra.setText(nvl(rs.getString(3)));
                    txtCodManual.setText(nvl(rs.getString(4)));
                }
            }
        } catch (Exception ex) {
            erro("Erro ao preencher os dados do produto: ", ex);
        }
    }

    private static String nvl(String t) {
        return t == null ? "" : t;
    }

    /** Depois de pesquisar: 1 resultado fica seleccionado; vários abrem a lista. */
    private void aposPesquisa(int n, String vazio) {
        if (n == 0) {
            JOptionPane.showMessageDialog(this, vazio, "AVISO", JOptionPane.WARNING_MESSAGE);
            return;
        }
        seleccionarPrimeiro(true);
        if (n > 1) {
            cmbProduto.showPopup();
        }
    }

    private void pesquisarDesignacao() {
        limparCamposExcepto(txtDesignacao);
        String t = txtDesignacao.getText().trim();
        if (t.isEmpty()) {
            carregarProdutos("", null);
            return;
        }
        // sem janela de aviso enquanto escreve
        int n = carregarProdutos("designacao LIKE ?", "%" + t + "%");
        if (n > 0) {
            seleccionarPrimeiro(false);
        }
    }

    private void pesquisarCodInterno() {
        limparCamposExcepto(txtCodInterno);
        String t = txtCodInterno.getText().trim();
        if (t.isEmpty()) {
            carregarProdutos("", null);
            return;
        }
        aposPesquisa(carregarProdutos("codigo = ?", t), "Nenhum produto com o código interno " + t + ".");
    }

    private void pesquisarCodBarra() {
        limparCamposExcepto(txtCodBarra);
        String t = txtCodBarra.getText().trim();
        if (t.isEmpty()) {
            carregarProdutos("", null);
            return;
        }
        aposPesquisa(carregarProdutos("codBarra = ?", t), "Nenhum produto com o código de barras " + t + ".");
    }

    private void pesquisarCodManual() {
        limparCamposExcepto(txtCodManual);
        String t = txtCodManual.getText().trim();
        if (t.isEmpty()) {
            carregarProdutos("", null);
            return;
        }
        aposPesquisa(carregarProdutos("codigo_manual = ?", t), "Nenhum produto com o código manual " + t + ".");
    }

    /** Lupa: janela de pesquisa (designação, código interno, barras ou manual). */
    private void abrirLupa() {
        JDialog dlg = new JDialog(this, "Pesquisar produto", true);
        JTextField txt = new JTextField();
        DefaultTableModel modelo = new DefaultTableModel(
                new Object[]{"Código", "Designação", "Cód. Barras", "Cód. Manual"}, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        JTable tabela = new JTable(modelo);
        tabela.getColumnModel().getColumn(0).setPreferredWidth(60);
        tabela.getColumnModel().getColumn(1).setPreferredWidth(320);

        Runnable pesquisar = () -> {
            modelo.setRowCount(0);
            String like = "%" + txt.getText().trim() + "%";
            String sql = "SELECT codigo, designacao, codBarra, codigo_manual FROM tb_produto "
                    + "WHERE designacao LIKE ? OR codBarra LIKE ? OR codigo_manual LIKE ? "
                    + "OR CAST(codigo AS CHAR) LIKE ? ORDER BY designacao LIMIT 300";
            try (Connection cn = BDConexao.getConnection();
                    PreparedStatement ps = cn.prepareStatement(sql)) {
                for (int i = 1; i <= 4; i++) {
                    ps.setString(i, like);
                }
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        modelo.addRow(new Object[]{rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4)});
                    }
                }
            } catch (Exception ex) {
                erro("Erro ao pesquisar produtos: ", ex);
            }
        };

        Runnable escolher = () -> {
            int r = tabela.getSelectedRow();
            if (r < 0) {
                return;
            }
            int id = (Integer) modelo.getValueAt(r, 0);
            String nome = String.valueOf(modelo.getValueAt(r, 1));
            limparCamposExcepto(null);
            carregarProdutos("codigo = ?", String.valueOf(id));
            seleccionarPrimeiro(true);
            dlg.dispose();
        };

        txt.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                pesquisar.run();
            }
        });
        tabela.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    escolher.run();
                }
            }
        });
        JButton btnOk = new JButton("Seleccionar");
        btnOk.addActionListener(e -> escolher.run());

        JPanel topo = new JPanel(new BorderLayout(6, 6));
        topo.setBorder(new EmptyBorder(8, 8, 4, 8));
        topo.add(new JLabel("Designação, código interno, barras ou manual:"), BorderLayout.NORTH);
        topo.add(txt, BorderLayout.CENTER);
        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rodape.add(btnOk);

        dlg.setLayout(new BorderLayout());
        dlg.add(topo, BorderLayout.NORTH);
        dlg.add(new JScrollPane(tabela), BorderLayout.CENTER);
        dlg.add(rodape, BorderLayout.SOUTH);
        dlg.setPreferredSize(new Dimension(640, 400));
        dlg.pack();
        dlg.setLocationRelativeTo(this);
        pesquisar.run();
        dlg.setVisible(true);
    }

    // ------------------------------------------------------------------
    // relatório
    // ------------------------------------------------------------------
    private void gerar() {
        ItemCombo armazem = (ItemCombo) cmbArmazem.getSelectedItem();
        ItemCombo produto = (ItemCombo) cmbProduto.getSelectedItem();
        ItemSituacao situacao = (ItemSituacao) cmbSituacao.getSelectedItem();
        if (armazem == null || produto == null || situacao == null) {
            return;
        }

        HashMap<String, Object> parametros = new HashMap<>();
        parametros.put("ARMAZEM_ID", armazem.id);
        parametros.put("ARMAZEM_NOME", armazem.nome);
        parametros.put("PRODUTO_ID", produto.id);
        parametros.put("PRODUTO_NOME", produto.id == 0 ? TODOS_PRODUTOS : produto.id + " - " + produto.nome);
        parametros.put("SITUACAO", situacao.codigo);
        parametros.put("SITUACAO_NOME", situacao.nome);
        parametros.put("DIAS_ALERTA", ((Number) spDias.getValue()).intValue());
        parametros.put("INCLUIR_ZERO", chkIncluirZero.isSelected() ? 1 : 0);

        new AnyReport(parametros, chkValores.isSelected() ? REPORT_VALORES : REPORT);
    }

    private void erro(String msg, Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, msg + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> new InventariosVisao().setVisible(true));
    }
}
