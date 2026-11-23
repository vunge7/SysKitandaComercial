package comercial.mensalidade;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.table.DefaultTableModel;
import kitanda.util.CfMethods;
import util.BDConexao;
import util.ui.ComponentesWeb;
import util.ui.EstiloUI;
import util.ui.FormularioWeb;

/**
 * Regras das multas das mensalidades, num só ecrã:
 * <ul>
 * <li>serviço usado nas linhas de multa da factura (ou criado automaticamente);</li>
 * <li>de quando se contam os dias (dia 1 do mês ou dia de vencimento do contrato);</li>
 * <li>por serviço, as faixas de dias com a multa em <b>%</b> da mensalidade ou em <b>Kz</b>.</li>
 * </ul>
 * Só administradores. Avisa quando uma percentagem passa de 100 % (ex.: escreveram
 * 1000 a pensar em Kz).
 */
public class ConfigMultasVisao extends JDialog {

    private static final String AUTO = "(criar automaticamente: \"Multa por atraso\")";
    private static final String[] BASES = {"Dia 1 do mês da mensalidade", "Dia de pagamento do contrato (vencimento)"};
    private static final String TIPO_PC = "% da mensalidade", TIPO_KZ = "Valor fixo (Kz)";

    private final BDConexao conexao;
    private final Map<String, Integer> servicos = new LinkedHashMap<>(); // nome → código (não stocáveis)
    private final Map<String, BigDecimal> precos = new LinkedHashMap<>();
    private final JComboBox<String> cmbProdutoMulta = new JComboBox<>();
    private final JComboBox<String> cmbBase = new JComboBox<>(BASES);
    private final JComboBox<String> cmbServico = new JComboBox<>();
    private final JLabel lbExemplo = FormularioWeb.dica(" ");
    private final DefaultTableModel modelo = new DefaultTableModel(new Object[]{"Do dia", "Até ao dia", "Tipo", "Valor"}, 0);
    private final JTable tabela = new JTable(modelo);
    private String servicoActual;
    private boolean alterado;

    public static void abrir(Window dono, BDConexao conexao) {
        if (!util.ui.SoAdministrador.verificar(dono, "Multas das mensalidades")) {
            return;
        }
        new ConfigMultasVisao(dono, conexao).setVisible(true);
    }

    private ConfigMultasVisao(Window dono, BDConexao conexao) {
        super(dono, ModalityType.APPLICATION_MODAL);
        this.conexao = conexao;
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                fechar();
            }
        });
        setTitle("Multas das mensalidades");
        Mensalidades.garantirEstrutura();
        carregarServicos();
        montar();
        FormularioWeb.ajustar(this, 980, 720, 640, 480);
    }

    private void carregarServicos() {
        String sql = "SELECT p.codigo, p.designacao,"
                + " (SELECT pr.preco_venda FROM tb_preco pr WHERE pr.fk_produto = p.codigo AND 1 BETWEEN pr.qtd_baixo AND pr.qtd_alto"
                + "  ORDER BY pr.pk_preco DESC LIMIT 1) AS preco,"
                + " (SELECT COUNT(*) FROM configuracao_mes_comeco c WHERE c.produto_id = p.codigo) AS contratos"
                + " FROM tb_produto p WHERE p.stocavel = 'false' AND p.status = 'Activo' ORDER BY contratos DESC, p.designacao";
        try (Connection cn = BDConexao.novaLigacao(); PreparedStatement ps = cn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String nome = rs.getString("designacao") + (rs.getInt("contratos") > 0 ? "  · " + rs.getInt("contratos") + " contrato(s)" : "");
                servicos.put(nome, rs.getInt("codigo"));
                precos.put(nome, rs.getBigDecimal("preco") == null ? BigDecimal.ZERO : rs.getBigDecimal("preco"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao ler os serviços: " + e.getMessage());
        }
    }

    // =====================================================================
    // ECRÃ
    // =====================================================================
    private void montar() {
        ConfigMensalidade cfg = ConfigMensalidade.ler();
        List<String> produtos = new ArrayList<>();
        produtos.add(AUTO);
        String escolhido = AUTO;
        for (Map.Entry<String, Integer> e : servicos.entrySet()) {
            produtos.add(e.getKey());
            if (e.getValue() == cfg.produtoMulta) {
                escolhido = e.getKey();
            }
        }
        cmbProdutoMulta.setModel(new javax.swing.DefaultComboBoxModel<>(produtos.toArray(new String[0])));
        cmbProdutoMulta.setSelectedItem(escolhido);
        cmbBase.setSelectedIndex(RegrasMulta.BASE_VENCIMENTO.equalsIgnoreCase(cfg.multaBase) ? 1 : 0);
        FormularioWeb.combo(cmbProdutoMulta, cmbBase, cmbServico);
        cmbBase.addActionListener(e -> exemplo());
        JPanel gerais = FormularioWeb.pilha(8,
                FormularioWeb.grelha(260, 2,
                        ComponentesWeb.campo("Serviço das linhas de multa na factura", cmbProdutoMulta),
                        ComponentesWeb.campo("Contar os dias de atraso a partir do", cmbBase)),
                FormularioWeb.dica("<html>A multa entra na factura como linha própria (\"Multa por atraso: Serviço - Mês/Ano\"), "
                        + "com o IVA/isenção deste serviço. Sem serviço escolhido, cria-se \"Multa por atraso\" "
                        + "com o tratamento fiscal do serviço da mensalidade.</html>"));
        ComponentesWeb.Cartao cartaoGerais = FormularioWeb.cartao("Regras gerais", ComponentesWeb.AZUL_VIVO, gerais);

        // ---- faixas do serviço
        List<String> nomes = new ArrayList<>(servicos.keySet());
        cmbServico.setModel(new javax.swing.DefaultComboBoxModel<>(nomes.toArray(new String[0])));
        cmbServico.addActionListener(e -> trocarServico());
        EstiloUI.estilizarTabela(tabela, true);
        tabela.setRowHeight(30);
        tabela.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
        tabela.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(new JComboBox<>(new String[]{TIPO_PC, TIPO_KZ})));
        modelo.addTableModelListener(e -> {
            alterado = true;
            exemplo();
        });
        JScrollPane rol = new JScrollPane(tabela);
        rol.setBorder(BorderFactory.createLineBorder(ComponentesWeb.LINHA));
        rol.setPreferredSize(new Dimension(600, 200));
        JButton btMais = new JButton("Acrescentar faixa");
        JButton btMenos = new JButton("Remover faixa");
        JButton btCopiar = new JButton("Copiar para os outros serviços com contrato");
        ComponentesWeb.botao(btMais, ComponentesWeb.Variante.SECUNDARIO, null, "Nova faixa de dias");
        ComponentesWeb.botao(btMenos, ComponentesWeb.Variante.SECUNDARIO, null, "Remove a faixa seleccionada");
        ComponentesWeb.botao(btCopiar, ComponentesWeb.Variante.SECUNDARIO, null, "As mesmas faixas em todos os serviços com contratos");
        btMais.addActionListener(e -> {
            int de = 1;
            for (int r = 0; r < modelo.getRowCount(); r++) {
                de = Math.max(de, inteiro(modelo.getValueAt(r, 1)) + 1);
            }
            modelo.addRow(new Object[]{de, de + 9, TIPO_PC, "5"});
        });
        btMenos.addActionListener(e -> {
            int r = tabela.getSelectedRow();
            if (r >= 0) {
                modelo.removeRow(tabela.convertRowIndexToModel(r));
            }
        });
        btCopiar.addActionListener(e -> copiarParaTodos());
        JPanel botoes = ComponentesWeb.transparente(new EstiloUI.WrapLayout(FlowLayout.LEFT, 8, 0));
        botoes.add(btMais);
        botoes.add(btMenos);
        botoes.add(btCopiar);
        lbExemplo.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(12.5f));
        JPanel corpo = FormularioWeb.pilha(8, ComponentesWeb.campo("Serviço", cmbServico), rol, botoes, lbExemplo,
                FormularioWeb.dica("<html>Exemplo de faixas: 1–10 sem multa (não crie faixa), 11–20 = 5 %, 21–31 = 10 %. "
                        + "Passada a última faixa aplica-se a mais alta. Os meses pagos adiantados não têm multa.</html>"));
        ComponentesWeb.Cartao cartaoFaixas = FormularioWeb.cartao("Faixas de dias por serviço", ComponentesWeb.LARANJA, corpo);

        JButton btGravar = new JButton("Gravar");
        JButton btFechar = new JButton("Fechar");
        FormularioWeb.botaoSalvar(btGravar, "Grava as regras gerais e as faixas do serviço");
        FormularioWeb.botaoSair(btFechar, "Fechar (Esc)");
        btGravar.addActionListener(e -> gravar());
        btFechar.addActionListener(e -> fechar());
        FormularioWeb.montar(this, FormularioWeb.cabecalho("Multas das mensalidades",
                "Percentagem ou valor fixo, por faixas de dias de atraso", null),
                FormularioWeb.pilha(10, cartaoGerais, cartaoFaixas),
                FormularioWeb.barraAccoes(null, btFechar, btGravar), "Multas das mensalidades");
        getRootPane().registerKeyboardAction(e -> fechar(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW);
        if (!nomes.isEmpty()) {
            trocarServico();
        }
    }

    // =====================================================================
    // FAIXAS
    // =====================================================================
    private void trocarServico() {
        String novo = (String) cmbServico.getSelectedItem();
        if (alterado && servicoActual != null && !servicoActual.equals(novo)) {
            int op = JOptionPane.showConfirmDialog(this, "Gravar as faixas de \"" + servicoActual + "\" antes de mudar?",
                    "Multas", JOptionPane.YES_NO_CANCEL_OPTION);
            if (op == JOptionPane.CANCEL_OPTION) {
                cmbServico.setSelectedItem(servicoActual);
                return;
            }
            if (op == JOptionPane.YES_OPTION && !gravarFaixas(servicoActual, faixasDaTabela())) {
                cmbServico.setSelectedItem(servicoActual);
                return;
            }
        }
        servicoActual = novo;
        if (tabela.isEditing()) {
            tabela.getCellEditor().cancelCellEditing();
        }
        modelo.setRowCount(0);
        try (Connection cn = BDConexao.novaLigacao()) {
            for (RegrasMulta.Faixa f : RegrasMulta.doServico(cn, servicos.get(novo))) {
                modelo.addRow(new Object[]{f.de, f.ate, f.percentagem() ? TIPO_PC : TIPO_KZ,
                    f.valor.stripTrailingZeros().toPlainString()});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao ler as faixas: " + e.getMessage());
        }
        alterado = false;
        exemplo();
    }

    private static int inteiro(Object o) {
        try {
            return Integer.parseInt(String.valueOf(o).trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private List<RegrasMulta.Faixa> faixasDaTabela() {
        if (tabela.isEditing()) {
            tabela.getCellEditor().stopCellEditing();
        }
        List<RegrasMulta.Faixa> l = new ArrayList<>();
        for (int r = 0; r < modelo.getRowCount(); r++) {
            RegrasMulta.Faixa f = new RegrasMulta.Faixa();
            f.de = inteiro(modelo.getValueAt(r, 0));
            f.ate = inteiro(modelo.getValueAt(r, 1));
            f.tipo = TIPO_KZ.equals(modelo.getValueAt(r, 2)) ? RegrasMulta.VALOR : RegrasMulta.PERCENTAGEM;
            BigDecimal v = MesesMensalidadeDialog.lerValor(String.valueOf(modelo.getValueAt(r, 3)));
            f.valor = v == null ? BigDecimal.valueOf(-1) : v;
            l.add(f);
        }
        l.sort((a, b) -> Integer.compare(a.de, b.de));
        return l;
    }

    /** Erros que impedem gravar; null se estiver tudo bem. */
    private String validar(List<RegrasMulta.Faixa> l) {
        for (int i = 0; i < l.size(); i++) {
            RegrasMulta.Faixa f = l.get(i);
            if (f.de < 1 || f.ate < f.de) {
                return "Faixa " + (i + 1) + ": o \"até\" tem de ser maior ou igual ao \"do dia\" (e começar no dia 1 ou depois).";
            }
            if (f.valor.signum() < 0) {
                return "Faixa " + (i + 1) + ": valor inválido.";
            }
            if (i > 0 && f.de <= l.get(i - 1).ate) {
                return "As faixas " + i + " e " + (i + 1) + " sobrepõem-se (dia " + f.de + ").";
            }
        }
        return null;
    }

    private void exemplo() {
        String nome = (String) cmbServico.getSelectedItem();
        BigDecimal preco = nome == null ? BigDecimal.ZERO : precos.getOrDefault(nome, BigDecimal.ZERO);
        List<RegrasMulta.Faixa> l = new ArrayList<>();
        for (RegrasMulta.Faixa f : faixasSemEditar()) {
            if (f.valor.signum() >= 0) {
                l.add(f);
            }
        }
        if (l.isEmpty()) {
            lbExemplo.setText("Sem faixas: este serviço não tem multa.");
            return;
        }
        String base = cmbBase.getSelectedIndex() == 1 ? RegrasMulta.BASE_VENCIMENTO : RegrasMulta.BASE_MES;
        // exemplo: mês de Janeiro (vence dia 10), pago a 25 de Janeiro
        Periodo p = new Periodo(LocalDate.now().getYear(), 1);
        LocalDate venc = LocalDate.of(p.getAno(), 1, 10);
        RegrasMulta.Calculo k = RegrasMulta.calcular(l, preco, p, venc, LocalDate.of(p.getAno(), 1, 25), base);
        lbExemplo.setText("Exemplo: mensalidade de " + CfMethods.formatarComoMoeda(preco.doubleValue())
                + (base.equals(RegrasMulta.BASE_MES) ? ", paga a 25 do mês" : ", vence a 10 e paga a 25")
                + " → " + (k.valor.signum() > 0 ? "multa " + CfMethods.formatarComoMoeda(k.valor.doubleValue()) + " (" + k.descricao() + ")"
                        : "sem multa (" + k.dias + " dias)"));
    }

    private List<RegrasMulta.Faixa> faixasSemEditar() {
        List<RegrasMulta.Faixa> l = new ArrayList<>();
        for (int r = 0; r < modelo.getRowCount(); r++) {
            RegrasMulta.Faixa f = new RegrasMulta.Faixa();
            f.de = inteiro(modelo.getValueAt(r, 0));
            f.ate = inteiro(modelo.getValueAt(r, 1));
            f.tipo = TIPO_KZ.equals(modelo.getValueAt(r, 2)) ? RegrasMulta.VALOR : RegrasMulta.PERCENTAGEM;
            BigDecimal v = MesesMensalidadeDialog.lerValor(String.valueOf(modelo.getValueAt(r, 3)));
            f.valor = v == null ? BigDecimal.valueOf(-1) : v;
            l.add(f);
        }
        return l;
    }

    // =====================================================================
    // GRAVAR
    // =====================================================================
    private boolean gravarFaixas(String nome, List<RegrasMulta.Faixa> l) {
        String erro = validar(l);
        if (erro != null) {
            JOptionPane.showMessageDialog(this, erro, "Multas", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        for (RegrasMulta.Faixa f : l) {
            if (f.percentagem() && f.valor.compareTo(BigDecimal.valueOf(100)) > 0) {
                int op = JOptionPane.showConfirmDialog(this, "A faixa " + f.de + "–" + f.ate + " tem multa de "
                        + f.valor.stripTrailingZeros().toPlainString() + "% da mensalidade (mais do que a própria mensalidade).\n\n"
                        + "Queria dizer " + CfMethods.formatarComoMoeda(f.valor.doubleValue()) + " (valor fixo)?\n"
                        + "Sim = passa a valor fixo em Kz   ·   Não = fica em percentagem", "Confirmar multa",
                        JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
                if (op == JOptionPane.CANCEL_OPTION || op == JOptionPane.CLOSED_OPTION) {
                    return false;
                }
                if (op == JOptionPane.YES_OPTION) {
                    f.tipo = RegrasMulta.VALOR;
                }
            }
        }
        int produto = servicos.get(nome);
        Integer utilizador = util.SessaoUtil.getCodUsuario();
        try (Connection cn = BDConexao.novaLigacao()) {
            cn.setAutoCommit(false);
            try (PreparedStatement del = cn.prepareStatement("DELETE FROM multa_servico WHERE produto_id = ?");
                    PreparedStatement ins = cn.prepareStatement("INSERT INTO multa_servico (day_start, day_end, valor,"
                            + " data_registro, produto_id, usuario_id, tipo) VALUES (?, ?, ?, ?, ?, ?, ?)")) {
                del.setInt(1, produto);
                del.executeUpdate();
                for (RegrasMulta.Faixa f : l) {
                    ins.setInt(1, f.de);
                    ins.setInt(2, f.ate);
                    ins.setBigDecimal(3, f.valor);
                    ins.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
                    ins.setInt(5, produto);
                    ins.setInt(6, utilizador == null ? 0 : utilizador);
                    ins.setString(7, f.tipo);
                    ins.executeUpdate();
                }
                cn.commit();
            } catch (SQLException e) {
                cn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Não foi possível gravar: " + e.getMessage());
            return false;
        }
        Mensalidades.esquecerRegrasMulta();
        alterado = false;
        return true;
    }

    private void gravar() {
        ConfigMensalidade cfg = ConfigMensalidade.ler();
        Object p = cmbProdutoMulta.getSelectedItem();
        cfg.produtoMulta = AUTO.equals(p) || p == null ? 0 : servicos.getOrDefault(String.valueOf(p), 0);
        cfg.multaBase = cmbBase.getSelectedIndex() == 1 ? RegrasMulta.BASE_VENCIMENTO : RegrasMulta.BASE_MES;
        if (!cfg.gravar()) {
            JOptionPane.showMessageDialog(this, "Não foi possível gravar as regras gerais.");
            return;
        }
        if (servicoActual != null && !gravarFaixas(servicoActual, faixasDaTabela())) {
            return;
        }
        Mensalidades.esquecerRegrasMulta();
        JOptionPane.showMessageDialog(this, "Regras das multas gravadas.");
    }

    private void copiarParaTodos() {
        List<RegrasMulta.Faixa> l = faixasDaTabela();
        String erro = validar(l);
        if (erro != null) {
            JOptionPane.showMessageDialog(this, erro, "Multas", JOptionPane.WARNING_MESSAGE);
            return;
        }
        List<String> alvo = new ArrayList<>();
        for (String nome : servicos.keySet()) {
            if (nome.contains(" contrato(s)") && !nome.equals(servicoActual)) {
                alvo.add(nome);
            }
        }
        if (alvo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Não há outros serviços com contratos.");
            return;
        }
        if (JOptionPane.showConfirmDialog(this, "Aplicar estas faixas a " + alvo.size() + " serviço(s)?\n"
                + String.join("\n", alvo), "Copiar faixas", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
            return;
        }
        for (String nome : alvo) {
            if (!gravarFaixas(nome, faixasDaTabela())) {
                return;
            }
        }
        gravarFaixas(servicoActual, faixasDaTabela());
        JOptionPane.showMessageDialog(this, "Faixas copiadas.");
    }

    private void fechar() {
        if (alterado && JOptionPane.showConfirmDialog(this, "Há alterações por gravar. Sair na mesma?", "Multas",
                JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
            return;
        }
        dispose();
    }
}
