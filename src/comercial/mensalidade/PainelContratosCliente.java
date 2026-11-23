package comercial.mensalidade;

import comercial.controller.ConfiguracaoMesComecoController;
import comercial.controller.FamiliasController;
import comercial.controller.MesRhController;
import comercial.controller.ProdutosController;
import comercial.controller.TipoProdutosController;
import entity.ConfiguracaoMesComeco;
import entity.TbCliente;
import entity.TbProduto;
import entity.TbTipoProduto;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import kitanda.util.CfMethods;
import util.BDConexao;
import util.DVML;
import util.ui.ComponentesWeb;
import util.ui.EstiloUI;
import util.ui.FormularioWeb;

/**
 * Separador "Mensalidades" das fichas de cliente (ClienteVisao e
 * ClienteInternoVisao): formulário do contrato em cima, contratos do cliente
 * com o estado de pagamento em baixo e os meses do contrato seleccionado.
 * Os campos já vêm preenchidos (mês e ano actuais, 12 meses).
 */
public class PainelContratosCliente extends JPanel {

    private final BDConexao conexao;
    private final ProdutosController produtos;
    private final TipoProdutosController tiposProduto;
    private TbCliente cliente;

    private final JComboBox<String> cmbSubFamilia = new JComboBox<>();
    private final JComboBox<String> cmbServico = new JComboBox<>();
    private final JComboBox<String> cmbMes = new JComboBox<>(Periodo.MESES);
    private final JSpinner spAno = new JSpinner(new SpinnerNumberModel(Periodo.actual().getAno(), 2000, 2100, 1));
    private final JComboBox<String> cmbDuracao = new JComboBox<>();
    /** Dia do mês em que vence (0 = fim do mês). No ginásio cada atleta tem o seu. */
    private final JSpinner spDia = new JSpinner(new SpinnerNumberModel(Mensalidades.diaPagamentoSugerido(), 0, 31, 1));

    private final JButton btAdicionar = new JButton("Adicionar contrato");
    private final JButton btGuardar = new JButton("Guardar alterações");
    private final JButton btRemover = new JButton("Remover");
    private final JButton btNovo = new JButton("Novo contrato");
    private final JLabel lbCliente = new JLabel();
    private final JLabel lbPrevisao = FormularioWeb.dica(" ");
    private final JLabel lbAviso = FormularioWeb.dica(" ");
    private final JPanel painelMeses = ComponentesWeb.transparente(new EstiloUI.WrapLayout(FlowLayout.LEFT, 6, 6));

    private final DefaultTableModel modelo = new DefaultTableModel(
            new Object[]{"Cód", "Serviço", "Vigência", "Duração", "Vence", "Pagos", "Em atraso", "Próximo a pagar", "Estado"}, 0) {
        @Override
        public boolean isCellEditable(int r, int c) {
            return false;
        }
    };
    private final JTable tabela = new JTable(modelo);

    public PainelContratosCliente(BDConexao conexao) {
        super(new BorderLayout(0, 10));
        setOpaque(false);
        this.conexao = conexao;
        this.produtos = new ProdutosController(conexao);
        this.tiposProduto = new TipoProdutosController(conexao);
        carregarCombos();
        montar();
        setCliente(null);
    }

    // =====================================================================
    // API
    // =====================================================================
    /** O cliente cujos contratos se mostram (null = nenhum: só leitura). */
    public void setCliente(TbCliente c) {
        this.cliente = c != null && c.getCodigo() != null && c.getCodigo() > 0 ? c : null;
        lbAviso.setText(" ");
        recarregar();
    }

    public TbCliente getCliente() {
        return cliente;
    }

    /** Os contratos e os meses (pagos e por pagar) de um cliente, numa janela. */
    public static void abrirDialogo(java.awt.Window dono, BDConexao conexao, TbCliente c) {
        javax.swing.JDialog d = new javax.swing.JDialog(dono, "Mensalidades · " + c.getNome(),
                java.awt.Dialog.ModalityType.APPLICATION_MODAL);
        PainelContratosCliente p = new PainelContratosCliente(conexao);
        p.setCliente(c);
        String tel = c.getTelefone() == null || c.getTelefone().trim().isEmpty() ? "" : "Tel. " + c.getTelefone() + "   ·   ";
        JPanel cab = FormularioWeb.cabecalho(c.getNome(), tel + "Contratos e meses", null);
        JButton fechar = new JButton("Fechar");
        FormularioWeb.botaoSair(fechar, "Fechar (Esc)");
        fechar.addActionListener(e -> d.dispose());
        FormularioWeb.montar(d, cab, p, FormularioWeb.barraAccoes(null, fechar), "Mensalidades");
        d.getRootPane().registerKeyboardAction(e -> d.dispose(),
                javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0),
                javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW);
        FormularioWeb.ajustar(d, 1000, 680, 600, 420);
        d.setVisible(true);
    }

    // =====================================================================
    // COMBOS
    // =====================================================================
    private void carregarCombos() {
        // como antes: a 2ª família da lista é a dos serviços
        int idFamilia = 0;
        try {
            FamiliasController familias = new FamiliasController(conexao);
            Vector<?> v = familias.getVector();
            if (v.size() > 1) {
                idFamilia = familias.getFamiliaByDesignacao(String.valueOf(v.get(1))).getPkFamilia();
            }
        } catch (Exception ignorar) {
        }
        try {
            cmbSubFamilia.setModel(new DefaultComboBoxModel(tiposProduto.getVectorByIdFamilia(idFamilia)));
        } catch (Exception e) {
            cmbSubFamilia.setModel(new DefaultComboBoxModel(tiposProduto.getVector()));
        }
        cmbSubFamilia.addActionListener(e -> carregarServicos());
        carregarServicos();

        // os nomes dos meses vêm da BD se existirem (iguais aos de tb_mes_rh)
        try {
            Vector<String> meses = new MesRhController(conexao.getConnectionAtiva()).getVector();
            if (meses.size() == 12) {
                cmbMes.setModel(new DefaultComboBoxModel<>(meses));
            }
        } catch (Exception ignorar) {
        }
        cmbMes.setSelectedIndex(Periodo.actual().getMes() - 1);

        Vector<String> duracoes = new Vector<>();
        duracoes.add("Contínuo (sem fim)");
        for (int i = 1; i <= 36; i++) {
            duracoes.add(i + (i == 1 ? " mês" : " meses"));
        }
        cmbDuracao.setModel(new DefaultComboBoxModel<>(duracoes));
        cmbDuracao.setSelectedIndex(12);
    }

    private void carregarServicos() {
        try {
            int idTipo = tiposProduto.getTipoFamiliaByDesignacao(String.valueOf(cmbSubFamilia.getSelectedItem())).getCodigo();
            cmbServico.setModel(new DefaultComboBoxModel(produtos.getVectorByIdTipoProduto(idTipo)));
        } catch (Exception e) {
            cmbServico.setModel(new DefaultComboBoxModel<>());
        }
        actualizarPrevisao();
    }

    private TbProduto servicoSeleccionado() {
        try {
            return produtos.findByDesignacao(cmbServico.getSelectedItem().toString());
        } catch (Exception e) {
            return null;
        }
    }

    // =====================================================================
    // ECRÃ
    // =====================================================================
    private void montar() {
        FormularioWeb.combo(cmbSubFamilia, cmbServico, cmbMes, cmbDuracao);
        spAno.setEditor(new JSpinner.NumberEditor(spAno, "0"));
        FormularioWeb.spinner(spAno);
        FormularioWeb.spinner(spDia);
        spDia.setToolTipText("Dia do mês em que a mensalidade vence. 0 = último dia do mês.");
        spDia.addChangeListener(e -> actualizarPrevisao());
        cmbServico.addActionListener(e -> actualizarPrevisao());
        cmbMes.addActionListener(e -> actualizarPrevisao());
        cmbDuracao.addActionListener(e -> actualizarPrevisao());
        spAno.addChangeListener(e -> actualizarPrevisao());

        FormularioWeb.botaoSalvar(btAdicionar, "Cria o contrato de mensalidade deste serviço para o cliente");
        FormularioWeb.botaoAlterar(btGuardar, "Grava as alterações no contrato seleccionado");
        FormularioWeb.botaoEliminar(btRemover, "Remove o contrato seleccionado");
        ComponentesWeb.botao(btNovo, ComponentesWeb.Variante.SECUNDARIO, null, "Limpa a selecção para criar outro contrato");
        btAdicionar.addActionListener(e -> adicionar());
        btGuardar.addActionListener(e -> guardar());
        btRemover.addActionListener(e -> remover());
        btNovo.addActionListener(e -> tabela.clearSelection());
        JPanel botoes = ComponentesWeb.transparente(new EstiloUI.WrapLayout(FlowLayout.LEFT, 8, 0));
        botoes.add(btAdicionar);
        botoes.add(btGuardar);
        botoes.add(btRemover);
        botoes.add(btNovo);

        lbCliente.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(13.5f));
        lbCliente.setForeground(ComponentesWeb.TEXTO);
        JPanel form = FormularioWeb.pilha(10,
                lbCliente,
                FormularioWeb.grelha(170, 6,
                        ComponentesWeb.campo("Sub-família", cmbSubFamilia),
                        ComponentesWeb.campo("Serviço", cmbServico),
                        ComponentesWeb.campo("Mês de começo", cmbMes),
                        ComponentesWeb.campo("Ano", spAno),
                        ComponentesWeb.campo("Duração", cmbDuracao),
                        ComponentesWeb.campo("Dia de pagamento (0 = fim do mês)", spDia)),
                lbPrevisao,
                botoes,
                lbAviso);
        ComponentesWeb.Cartao cartaoForm = FormularioWeb.cartao("Contrato de mensalidade", ComponentesWeb.VERDE, form);

        EstiloUI.estilizarTabela(tabela, true);
        TableColumnModel cm = tabela.getColumnModel();
        cm.getColumn(0).setMinWidth(0);
        cm.getColumn(0).setMaxWidth(0);
        cm.getColumn(0).setPreferredWidth(0);
        cm.getColumn(1).setPreferredWidth(170);
        cm.getColumn(2).setPreferredWidth(190);
        cm.getColumn(4).setPreferredWidth(90);
        cm.getColumn(5).setMaxWidth(70);
        cm.getColumn(6).setMaxWidth(80);
        cm.getColumn(8).setCellRenderer(new RendererEstado());
        tabela.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                contratoSeleccionado();
            }
        });
        JScrollPane rol = new JScrollPane(tabela);
        rol.setBorder(BorderFactory.createLineBorder(ComponentesWeb.LINHA));
        rol.setPreferredSize(new Dimension(500, 150));
        rol.setMinimumSize(new Dimension(200, 90));

        JPanel meses = ComponentesWeb.transparente(new BorderLayout(0, 4));
        JLabel lbMeses = new JLabel("Meses do contrato seleccionado");
        ComponentesWeb.rotulo(lbMeses);
        meses.add(lbMeses, BorderLayout.NORTH);
        meses.add(painelMeses, BorderLayout.CENTER);

        JPanel corpo = ComponentesWeb.transparente(new BorderLayout(0, 8));
        corpo.add(rol, BorderLayout.CENTER);
        corpo.add(meses, BorderLayout.SOUTH);
        ComponentesWeb.Cartao cartaoLista = FormularioWeb.cartao("Contratos do cliente", ComponentesWeb.AZUL_VIVO, corpo);

        add(cartaoForm, BorderLayout.NORTH);
        add(cartaoLista, BorderLayout.CENTER);
    }

    // =====================================================================
    // CONTRATOS
    // =====================================================================
    private ConfiguracaoMesComecoController contratos() {
        return new ConfiguracaoMesComecoController(conexao.getConnectionAtiva());
    }

    private void recarregar() {
        modelo.setRowCount(0);
        if (cliente != null) {
            Connection cn = conexao.getConnectionAtiva();
            for (ConfiguracaoMesComeco c : contratos().listarTodos(cliente.getCodigo())) {
                String servico;
                try {
                    servico = produtos.findByCod(c.getProdutoId()).getDesignacao();
                } catch (Exception e) {
                    servico = "(serviço " + c.getProdutoId() + ")";
                }
                Mensalidades.Resumo r;
                try {
                    r = Mensalidades.resumo(cn, c);
                } catch (SQLException e) {
                    r = null;
                }
                modelo.addRow(new Object[]{c.getId(), servico, Mensalidades.textoVigencia(c),
                    Mensalidades.textoDuracao(c.getDuracao()),
                    Mensalidades.textoDiaPagamento(c.getDiaPagamento()),
                    r == null ? "" : r.pagos,
                    r == null ? "" : r.emAtraso,
                    r == null || r.proximo == null ? "—" : r.proximo.texto(),
                    r == null ? "" : r.estadoTexto()});
            }
        }
        lbCliente.setText(cliente == null
                ? "Escolha ou grave primeiro o cliente para configurar as mensalidades."
                : "Mensalidades de " + cliente.getNome());
        mostrarMeses(null);
        actualizarBotoes();
    }

    private ConfiguracaoMesComeco doFormulario() {
        ConfiguracaoMesComeco c = new ConfiguracaoMesComeco();
        c.setDataCadastro(new Date());
        c.setMesId(cmbMes.getSelectedIndex() + 1);
        c.setAnoInicio(((Number) spAno.getValue()).intValue());
        TbProduto p = servicoSeleccionado();
        c.setProdutoId(p == null ? 0 : p.getCodigo());
        c.setUsuarioId(0);
        c.setDuracao(Math.max(0, cmbDuracao.getSelectedIndex())); // 0 = contínuo
        c.setDiaPagamento(((Number) spDia.getValue()).intValue());
        c.setClienteId(cliente == null ? 0 : cliente.getCodigo());
        return c;
    }

    private boolean valido() {
        if (cliente == null) {
            JOptionPane.showMessageDialog(this, "Escolha ou grave primeiro o cliente.", DVML.DVML_COMERCIAL, JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (servicoSeleccionado() == null) {
            JOptionPane.showMessageDialog(this, "Escolha o serviço.", DVML.DVML_COMERCIAL, JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private boolean repetido(int produtoId, int idEmEdicao) {
        for (ConfiguracaoMesComeco c : contratos().listarTodos(cliente.getCodigo())) {
            if (c.getProdutoId() == produtoId && c.getId() != idEmEdicao) {
                return true;
            }
        }
        return false;
    }

    private void adicionar() {
        if (!valido()) {
            return;
        }
        ConfiguracaoMesComeco c = doFormulario();
        if (repetido(c.getProdutoId(), -1)) {
            JOptionPane.showMessageDialog(this,
                    "Este cliente já tem um contrato para este serviço.\nSeleccione-o na lista e use \"Guardar alterações\".",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (contratos().salvar(c)) {
            recarregar();
            seleccionarProduto(c.getProdutoId());
            aviso("Contrato adicionado: " + Mensalidades.textoVigencia(c), ComponentesWeb.VERDE);
        }
    }

    private void guardar() {
        int id = idSeleccionado();
        if (id <= 0 || !valido()) {
            return;
        }
        ConfiguracaoMesComeco c = doFormulario();
        c.setId(id);
        if (repetido(c.getProdutoId(), id)) {
            JOptionPane.showMessageDialog(this, "Este cliente já tem outro contrato para este serviço.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (contratos().atualizar(c)) {
            recarregar();
            seleccionarProduto(c.getProdutoId());
            aviso("Contrato actualizado: " + Mensalidades.textoVigencia(c), ComponentesWeb.VERDE);
        }
    }

    private void remover() {
        int id = idSeleccionado();
        if (id <= 0) {
            return;
        }
        ConfiguracaoMesComeco c = contratos().buscarPorId(id);
        int pagos = 0;
        try {
            pagos = c == null ? 0 : Mensalidades.pagamentos(conexao.getConnectionAtiva(), c.getClienteId(), c.getProdutoId()).size();
        } catch (SQLException ignorar) {
        }
        String msg = "Remover este contrato de mensalidade?";
        if (pagos > 0) {
            msg += "\n\nJá tem " + pagos + " mês(es) pago(s). As facturas e o histórico de pagamentos ficam;"
                    + "\nsó deixa de aparecer na venda.";
        }
        if (JOptionPane.showConfirmDialog(this, msg, "Remover contrato", JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE) != JOptionPane.YES_OPTION) {
            return;
        }
        if (contratos().deletar(id)) {
            recarregar();
            aviso("Contrato removido.", ComponentesWeb.LARANJA);
        }
    }

    private int idSeleccionado() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) {
            return -1;
        }
        try {
            return Integer.parseInt(String.valueOf(modelo.getValueAt(tabela.convertRowIndexToModel(linha), 0)));
        } catch (Exception e) {
            return -1;
        }
    }

    private void seleccionarProduto(int produtoId) {
        Map<Integer, Integer> produtoDe = new HashMap<>();
        for (ConfiguracaoMesComeco c : contratos().listarTodos(cliente.getCodigo())) {
            produtoDe.put(c.getId(), c.getProdutoId());
        }
        for (int r = 0; r < modelo.getRowCount(); r++) {
            Integer p = produtoDe.get(Integer.parseInt(String.valueOf(modelo.getValueAt(r, 0))));
            if (p != null && p == produtoId) {
                int v = tabela.convertRowIndexToView(r);
                tabela.setRowSelectionInterval(v, v);
                return;
            }
        }
    }

    /** Ao clicar num contrato: preenche o formulário e mostra os meses. */
    private void contratoSeleccionado() {
        int id = idSeleccionado();
        ConfiguracaoMesComeco c = id > 0 ? contratos().buscarPorId(id) : null;
        if (c != null) {
            try {
                TbProduto produto = produtos.findByCod(c.getProdutoId());
                TbTipoProduto tipo = (TbTipoProduto) tiposProduto.findById(produto.getCodTipoProduto().getCodigo());
                cmbSubFamilia.setSelectedItem(tipo.getDesignacao());
                cmbServico.setSelectedItem(produto.getDesignacao());
            } catch (Exception ignorar) {
                // serviço apagado entretanto
            }
            Periodo inicio = Mensalidades.inicio(c);
            cmbMes.setSelectedIndex(inicio.getMes() - 1);
            spAno.setValue(inicio.getAno());
            cmbDuracao.setSelectedIndex(Math.min(Math.max(0, c.getDuracao()), cmbDuracao.getItemCount() - 1));
            spDia.setValue(Math.max(0, Math.min(31, c.getDiaPagamento())));
        }
        mostrarMeses(c);
        actualizarBotoes();
    }

    private void actualizarBotoes() {
        boolean temCliente = cliente != null;
        boolean temContrato = idSeleccionado() > 0;
        btAdicionar.setEnabled(temCliente && !temContrato);
        btGuardar.setEnabled(temCliente && temContrato);
        btRemover.setEnabled(temCliente && temContrato);
        btNovo.setEnabled(temContrato);
    }

    /** "Vigência: Março/2026 → Fevereiro/2027 · 5.000,00 por mês · total 60.000,00" */
    private void actualizarPrevisao() {
        if (cmbMes.getSelectedIndex() < 0 || cmbDuracao.getItemCount() == 0) {
            return;
        }
        ConfiguracaoMesComeco c = new ConfiguracaoMesComeco();
        c.setMesId(cmbMes.getSelectedIndex() + 1);
        c.setAnoInicio(((Number) spAno.getValue()).intValue());
        c.setDuracao(Math.max(0, cmbDuracao.getSelectedIndex()));
        c.setDiaPagamento(((Number) spDia.getValue()).intValue());
        String texto = "Vigência: " + Mensalidades.textoVigencia(c) + "   ·   vence " + Mensalidades.textoDiaPagamento(c.getDiaPagamento());
        TbProduto p = servicoSeleccionado();
        if (p != null) {
            BigDecimal mensal = Mensalidades.precoMensal(conexao, p.getCodigo());
            if (mensal.signum() > 0) {
                texto += "   ·   " + CfMethods.formatarComoMoeda(mensal.doubleValue()) + " por mês";
                if (c.getDuracao() > 0) {
                    texto += "   ·   total " + CfMethods.formatarComoMoeda(mensal.doubleValue() * c.getDuracao());
                }
            }
        }
        lbPrevisao.setText(texto);
    }

    private void aviso(String texto, Color cor) {
        lbAviso.setText(texto);
        lbAviso.setForeground(cor.darker());
    }

    private void mostrarMeses(ConfiguracaoMesComeco c) {
        painelMeses.removeAll();
        if (c == null) {
            painelMeses.add(FormularioWeb.dica("Seleccione um contrato para ver os meses pagos e por pagar."));
        } else {
            try {
                List<Mensalidades.Mes> meses = Mensalidades.meses(conexao.getConnectionAtiva(), c);
                for (Mensalidades.Mes m : meses) {
                    Color cor = m.estado == Mensalidades.Estado.PAGO ? ComponentesWeb.VERDE
                            : m.estado == Mensalidades.Estado.ATRASO ? ComponentesWeb.VERMELHO
                            : m.estado == Mensalidades.Estado.ACTUAL ? ComponentesWeb.LARANJA
                            : ComponentesWeb.CINZA;
                    ComponentesWeb.Etiqueta e = new ComponentesWeb.Etiqueta(m.periodo.curto(), cor);
                    e.setToolTipText(m.periodo.texto() + " — " + m.estado.texto()
                            + (m.vendaId > 0 ? " (venda nº " + m.vendaId + ")" : ""));
                    painelMeses.add(e);
                }
            } catch (SQLException e) {
                painelMeses.add(FormularioWeb.dica("Erro ao ler os meses: " + e.getMessage()));
            }
        }
        painelMeses.revalidate();
        painelMeses.repaint();
    }

    /** Coluna "Estado": vermelho com meses em atraso, verde quando está em dia. */
    private static final class RendererEstado extends EstiloUI.ZebraRenderer {

        RendererEstado() {
            super(SwingConstants.LEFT);
        }

        @Override
        public Component getTableCellRendererComponent(JTable t, Object v, boolean sel, boolean foco, int r, int c) {
            Component comp = super.getTableCellRendererComponent(t, v, sel, foco, r, c);
            if (!sel) {
                String s = String.valueOf(v);
                comp.setForeground(s.contains("atraso") ? ComponentesWeb.VERMELHO
                        : s.contains("dia") || s.contains("pago") ? ComponentesWeb.VERDE.darker()
                        : ComponentesWeb.TEXTO);
            }
            comp.setFont(EstiloUI.FONTE_NEGRITO);
            return comp;
        }
    }
}
