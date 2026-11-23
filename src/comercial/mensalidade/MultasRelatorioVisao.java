package comercial.mensalidade;

import comercial.relatorios.RelatorioJasper;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TreeSet;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import kitanda.util.CfMethods;
import util.BDConexao;
import util.ui.ComponentesWeb;
import util.ui.EstiloUI;
import util.ui.FormularioWeb;

/**
 * Multas das mensalidades facturadas num período: calculadas, cobradas,
 * reduzidas e perdoadas (com o motivo e o utilizador). Na tabela e no iReport.
 */
public class MultasRelatorioVisao extends JDialog {

    private static final DateTimeFormatter DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final String TODOS = "Todos os clientes";
    private static final String[] SITUACOES = {"Todas", "Cobradas", "Reduzidas", "Perdoadas"};

    /** Uma multa (bean do relatório). */
    public static final class Multa {

        String data, documento, cliente, servico, mes, regra, situacao, motivo, utilizador;
        int dias;
        boolean anulada;
        BigDecimal calculada = BigDecimal.ZERO, cobrada = BigDecimal.ZERO, diferenca = BigDecimal.ZERO;

        public String getData() { return data; }
        public String getDocumento() { return documento; }
        public String getCliente() { return cliente; }
        public String getServico() { return servico; }
        public String getMes() { return mes; }
        public String getRegra() { return regra; }
        public String getSituacao() { return situacao; }
        public String getMotivo() { return motivo; }
        public String getUtilizador() { return utilizador; }
        public Integer getDias() { return dias; }
        public BigDecimal getCalculada() { return calculada; }
        public BigDecimal getCobrada() { return cobrada; }
        public BigDecimal getDiferenca() { return diferenca; }
    }

    private final BDConexao conexao;
    private List<Multa> dados = new ArrayList<>();
    private final List<Multa> visiveis = new ArrayList<>();
    private final com.toedter.calendar.JDateChooser dcDe = new com.toedter.calendar.JDateChooser();
    private final com.toedter.calendar.JDateChooser dcAte = new com.toedter.calendar.JDateChooser();
    private final JComboBox<String> cmbCliente = new JComboBox<>(new String[]{TODOS});
    private final JComboBox<String> cmbSituacao = new JComboBox<>(SITUACOES);
    private final JCheckBox chkAnuladas = new JCheckBox("Incluir anuladas", false);
    private final ComponentesWeb.CartaoKpi kpiCalc = new ComponentesWeb.CartaoKpi("Kz", "Multas calculadas", ComponentesWeb.AZUL_VIVO).compacto();
    private final ComponentesWeb.CartaoKpi kpiCob = new ComponentesWeb.CartaoKpi(ComponentesWeb.CartaoKpi.VISTO, "Cobradas", ComponentesWeb.VERDE).compacto();
    private final ComponentesWeb.CartaoKpi kpiPerd = new ComponentesWeb.CartaoKpi("-", "Perdoadas / reduzidas", ComponentesWeb.LARANJA).compacto();
    private final DefaultTableModel modelo = new DefaultTableModel(new Object[]{"Data", "Documento", "Cliente", "Serviço",
        "Mês", "Dias", "Regra", "Calculada", "Cobrada", "Diferença", "Situação", "Motivo", "Utilizador"}, 0) {
        @Override
        public boolean isCellEditable(int r, int c) {
            return false;
        }

        @Override
        public Class<?> getColumnClass(int c) {
            return c == 5 ? Integer.class : c >= 7 && c <= 9 ? BigDecimal.class : Object.class;
        }
    };
    private final JTable tabela = new JTable(modelo);
    private final JLabel lbEstado = FormularioWeb.dica(" ");

    public static void abrir(Window dono, BDConexao conexao) {
        new MultasRelatorioVisao(dono, conexao).setVisible(true);
    }

    private MultasRelatorioVisao(Window dono, BDConexao conexao) {
        super(dono, ModalityType.MODELESS);
        this.conexao = conexao;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setTitle("Relatório de multas");
        LocalDate hoje = LocalDate.now();
        dcDe.setDate(Date.from(hoje.withDayOfMonth(1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        dcAte.setDate(Date.from(hoje.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        montar();
        FormularioWeb.ajustar(this, 1300, 780, 860, 520);
        carregar();
    }

    private LocalDate data(com.toedter.calendar.JDateChooser d, LocalDate omissao) {
        return d.getDate() == null ? omissao : d.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    private void montar() {
        FormularioWeb.data(dcDe);
        FormularioWeb.data(dcAte);
        dcDe.addPropertyChangeListener("date", e -> carregar());
        dcAte.addPropertyChangeListener("date", e -> carregar());
        JPanel cDe = FormularioWeb.campoCabecalho(new JLabel("De"), dcDe);
        JPanel cAte = FormularioWeb.campoCabecalho(new JLabel("Até"), dcAte);
        cDe.setPreferredSize(new Dimension(150, 56));
        cAte.setPreferredSize(new Dimension(150, 56));
        JPanel direita = ComponentesWeb.transparente(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        direita.add(cDe);
        direita.add(cAte);
        FormularioWeb.combo(cmbCliente, cmbSituacao);
        cmbCliente.addActionListener(e -> aplicar());
        cmbSituacao.addActionListener(e -> aplicar());
        chkAnuladas.setOpaque(false);
        chkAnuladas.setFont(EstiloUI.FONTE);
        chkAnuladas.addActionListener(e -> aplicar());
        JPanel filtros = FormularioWeb.grelha(220, 3, ComponentesWeb.campo("Cliente", cmbCliente),
                ComponentesWeb.campo("Situação", cmbSituacao), ComponentesWeb.campo(" ", chkAnuladas));
        for (ComponentesWeb.CartaoKpi k : new ComponentesWeb.CartaoKpi[]{kpiCalc, kpiCob, kpiPerd}) {
            k.setValor("—", " ");
        }
        JPanel kpis = FormularioWeb.grelha(200, 3, kpiCalc, kpiCob, kpiPerd);

        EstiloUI.estilizarTabela(tabela, true);
        tabela.setRowSorter(new TableRowSorter<>(modelo));
        tabela.setDefaultRenderer(BigDecimal.class, new EstiloUI.ZebraRenderer(SwingConstants.RIGHT) {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v, boolean s, boolean f, int r, int c) {
                return super.getTableCellRendererComponent(t, v instanceof BigDecimal
                        ? CfMethods.formatarComoMoeda(((BigDecimal) v).doubleValue()) : v, s, f, r, c);
            }
        });
        int[] w = {90, 120, 180, 140, 110, 50, 90, 100, 100, 100, 90, 200, 120};
        for (int i = 0; i < w.length; i++) {
            tabela.getColumnModel().getColumn(i).setPreferredWidth(w[i]);
        }
        JScrollPane rol = new JScrollPane(tabela);
        rol.setBorder(BorderFactory.createLineBorder(ComponentesWeb.LINHA));
        rol.setPreferredSize(new Dimension(900, 360));
        JButton btRel = new JButton("Relatório (iReport)");
        JButton btCsv = new JButton("Exportar (Excel/CSV)");
        JButton btRegras = new JButton("Regras das multas…");
        ComponentesWeb.botao(btRel, ComponentesWeb.Variante.PRIMARIO, null, "A4 paisagem com os filtros escolhidos");
        ComponentesWeb.botao(btCsv, ComponentesWeb.Variante.SECUNDARIO, null, "Grava a tabela para o Excel");
        ComponentesWeb.botao(btRegras, ComponentesWeb.Variante.SECUNDARIO, null, "Faixas, % ou valor fixo, serviço da multa");
        btRel.addActionListener(e -> relatorio());
        btCsv.addActionListener(e -> exportar());
        btRegras.addActionListener(e -> ConfigMultasVisao.abrir(this, conexao));
        JPanel acoes = ComponentesWeb.transparente(new EstiloUI.WrapLayout(FlowLayout.LEFT, 8, 0));
        acoes.add(btRel);
        acoes.add(btCsv);
        acoes.add(btRegras);
        JPanel corpo = ComponentesWeb.transparente(new BorderLayout(0, 8));
        corpo.add(acoes, BorderLayout.NORTH);
        corpo.add(rol, BorderLayout.CENTER);
        JPanel conteudo = FormularioWeb.pilha(10, FormularioWeb.cartao("Parâmetros", ComponentesWeb.AZUL_VIVO, filtros), kpis,
                FormularioWeb.cartao("Multas", ComponentesWeb.AZUL_VIVO, corpo));
        JButton btFechar = new JButton("Fechar");
        FormularioWeb.botaoSair(btFechar, "Fechar (Esc)");
        btFechar.addActionListener(e -> dispose());
        FormularioWeb.montar(this, FormularioWeb.cabecalho("Relatório de multas",
                "Multas das mensalidades: calculadas, cobradas, reduzidas e perdoadas", direita),
                conteudo, FormularioWeb.barraAccoes(lbEstado, btFechar), "Relatório de multas");
        getRootPane().registerKeyboardAction(e -> dispose(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW);
    }

    private void carregar() {
        final LocalDate de = data(dcDe, LocalDate.now().withDayOfMonth(1)), ate = data(dcAte, LocalDate.now());
        lbEstado.setText("A carregar…");
        new SwingWorker<List<Multa>, Void>() {
            @Override
            protected List<Multa> doInBackground() throws Exception {
                return ler(de, ate);
            }

            @Override
            protected void done() {
                try {
                    dados = get();
                    TreeSet<String> nomes = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
                    for (Multa m : dados) {
                        nomes.add(m.cliente);
                    }
                    Object sel = cmbCliente.getSelectedItem();
                    List<String> l = new ArrayList<>();
                    l.add(TODOS);
                    l.addAll(nomes);
                    cmbCliente.setModel(new DefaultComboBoxModel<>(l.toArray(new String[0])));
                    cmbCliente.setSelectedItem(sel != null && nomes.contains(String.valueOf(sel)) ? sel : TODOS);
                    aplicar();
                    lbEstado.setText(dados.size() + " multa(s) de " + de.format(DATA) + " a " + ate.format(DATA));
                } catch (Exception e) {
                    lbEstado.setText("Erro: " + e.getMessage());
                }
            }
        }.execute();
    }

    private static List<Multa> ler(LocalDate de, LocalDate ate) throws SQLException {
        Mensalidades.garantirEstrutura();
        List<Multa> r = new ArrayList<>();
        String sql = "SELECT m.*, v.cod_fact, COALESCE(c.nome, '') AS cliente, COALESCE(u.nome, '') AS utilizador"
                + " FROM mensalidade_multa m"
                + " LEFT JOIN tb_venda v ON v.codigo = m.venda_id"
                + " LEFT JOIN tb_cliente c ON c.codigo = m.cliente_id"
                + " LEFT JOIN tb_usuario u ON u.codigo = m.usuario_id"
                + " WHERE m.data >= ? AND m.data < ? ORDER BY m.data, m.id";
        try (Connection cn = BDConexao.novaLigacao(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(de.atStartOfDay()));
            ps.setTimestamp(2, Timestamp.valueOf(ate.plusDays(1).atStartOfDay()));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Multa m = new Multa();
                    m.data = rs.getTimestamp("data").toLocalDateTime().format(DATA);
                    m.documento = rs.getString("cod_fact") == null ? "venda " + rs.getInt("venda_id") : rs.getString("cod_fact");
                    m.cliente = rs.getString("cliente");
                    m.servico = rs.getString("servico") == null ? "" : rs.getString("servico");
                    m.mes = Periodo.MESES[Math.max(1, Math.min(12, rs.getInt("mes"))) - 1] + "/" + rs.getInt("ano");
                    m.dias = rs.getInt("dias_atraso");
                    m.regra = rs.getString("regra") == null ? "" : rs.getString("regra");
                    m.calculada = rs.getBigDecimal("valor_calculado");
                    m.cobrada = rs.getBigDecimal("valor_cobrado");
                    m.diferenca = m.calculada.subtract(m.cobrada).max(BigDecimal.ZERO);
                    m.anulada = rs.getInt("anulada") != 0;
                    m.situacao = m.anulada ? "Anulada" : rs.getInt("perdoada") != 0 ? "Perdoada"
                            : m.cobrada.compareTo(m.calculada) < 0 ? "Reduzida" : "Cobrada";
                    m.motivo = rs.getString("motivo") == null ? "" : rs.getString("motivo");
                    m.utilizador = rs.getString("utilizador");
                    r.add(m);
                }
            }
        }
        return r;
    }

    private void aplicar() {
        String cli = String.valueOf(cmbCliente.getSelectedItem());
        String sit = String.valueOf(cmbSituacao.getSelectedItem());
        visiveis.clear();
        BigDecimal calc = BigDecimal.ZERO, cob = BigDecimal.ZERO, dif = BigDecimal.ZERO;
        int nPerd = 0;
        modelo.setRowCount(0);
        for (Multa m : dados) {
            if (m.anulada && !chkAnuladas.isSelected()) {
                continue;
            }
            if (!TODOS.equals(cli) && !cli.equals(m.cliente)) {
                continue;
            }
            if (!"Todas".equals(sit) && !m.situacao.startsWith(sit.substring(0, sit.length() - 1))) {
                continue;
            }
            visiveis.add(m);
            modelo.addRow(new Object[]{m.data, m.documento, m.cliente, m.servico, m.mes, m.dias, m.regra, m.calculada,
                m.cobrada, m.diferenca, m.situacao, m.motivo, m.utilizador});
            if (!m.anulada) {
                calc = calc.add(m.calculada);
                cob = cob.add(m.cobrada);
                dif = dif.add(m.diferenca);
                if (m.diferenca.signum() > 0) {
                    nPerd++;
                }
            }
        }
        kpiCalc.setValor(CfMethods.formatarComoMoeda(calc.doubleValue()), visiveis.size() + " multa(s)");
        kpiCob.setValor(CfMethods.formatarComoMoeda(cob.doubleValue()), "facturadas");
        kpiPerd.setValor(CfMethods.formatarComoMoeda(dif.doubleValue()), nPerd + " com redução ou perdão");
    }

    private void relatorio() {
        if (visiveis.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Não há multas para os filtros escolhidos.");
            return;
        }
        String periodo = "De " + data(dcDe, LocalDate.now()).format(DATA) + " a " + data(dcAte, LocalDate.now()).format(DATA);
        String filtros = cmbCliente.getSelectedItem() + " · " + cmbSituacao.getSelectedItem()
                + (chkAnuladas.isSelected() ? " · inclui anuladas" : "");
        RelatorioJasper.mostrar("mensalidadeMultas", RelatorioJasper.parametros(conexao, periodo, filtros),
                new ArrayList<>(visiveis), "Relatório de multas");
    }

    private void exportar() {
        JFileChooser fc = new JFileChooser();
        fc.setSelectedFile(new File("multas.csv"));
        if (fc.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }
        File f = fc.getSelectedFile();
        try (PrintWriter w = new PrintWriter(new OutputStreamWriter(new FileOutputStream(f), StandardCharsets.UTF_8))) {
            w.print('﻿');
            for (int c = 0; c < modelo.getColumnCount(); c++) {
                w.print((c > 0 ? ";" : "") + modelo.getColumnName(c));
            }
            w.println();
            for (int r = 0; r < tabela.getRowCount(); r++) {
                int m = tabela.convertRowIndexToModel(r);
                for (int c = 0; c < modelo.getColumnCount(); c++) {
                    Object o = modelo.getValueAt(m, c);
                    String s = o instanceof BigDecimal ? ((BigDecimal) o).toPlainString().replace('.', ',') : String.valueOf(o);
                    w.print((c > 0 ? ";" : "") + s.replace(";", ","));
                }
                w.println();
            }
            JOptionPane.showMessageDialog(this, "Gravado em:\n" + f.getAbsolutePath());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Não foi possível gravar: " + e.getMessage());
        }
    }
}
