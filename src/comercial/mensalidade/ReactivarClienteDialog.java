package comercial.mensalidade;

import comercial.controller.ConfiguracaoMesComecoController;
import comercial.controller.ProdutosController;
import entity.ConfiguracaoMesComeco;
import entity.TbProduto;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import kitanda.util.CfMethods;
import util.BDConexao;
import util.ui.ComponentesWeb;
import util.ui.EstiloUI;
import util.ui.FormularioWeb;

/**
 * Reactivar um cliente que desistiu ou foi desactivado e voltou. Não se faz
 * nova inscrição: os contratos dele recomeçam na data escolhida (com o novo
 * dia de pagamento) e, se houver venda aberta, vão logo para o carrinho a
 * taxa de reactivação, a dívida antiga (com ou sem multa) e o 1.º mês.
 */
public class ReactivarClienteDialog extends JDialog {

    private static final DateTimeFormatter DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final String SEM_TAXA = "(sem taxa de reactivação)";

    private final BDConexao conexao;
    private final int clienteId;
    private String nome = "";
    private EstadoCliente.Registo registo;
    private final List<Devedores.Linha> contratos = new ArrayList<>();
    private final ConfigMensalidade config = ConfigMensalidade.ler();

    private final DefaultTableModel modelo = new DefaultTableModel(new Object[]{"Retomar", "Serviço", "Mensalidade",
        "Duração", "Dívida antiga"}, 0) {
        @Override
        public Class<?> getColumnClass(int c) {
            return c == 0 ? Boolean.class : Object.class;
        }

        @Override
        public boolean isCellEditable(int r, int c) {
            return c == 0;
        }
    };
    private final com.toedter.calendar.JDateChooser dcRecomeco = new com.toedter.calendar.JDateChooser(new Date());
    private final JSpinner spDia = new JSpinner(new SpinnerNumberModel(0, 0, 31, 1));
    private final JComboBox<String> cmbTaxa = new JComboBox<>();
    private final JCheckBox chkTaxa = new JCheckBox("Cobrar a taxa de reactivação", true);
    private final JCheckBox chkDivida = new JCheckBox("Cobrar a dívida antiga", true);
    private final JCheckBox chkSemMulta = new JCheckBox("Perdoar as multas da dívida antiga", false);
    private final JCheckBox chkPrimeiroMes = new JCheckBox("Cobrar já o 1.º mês", true);
    private final JLabel lbTotal = new JLabel("0,00", SwingConstants.RIGHT);
    private final JLabel lbDetalhe = FormularioWeb.dica(" ");
    private final JButton btReactivar = new JButton();

    public static void abrir(Window dono, BDConexao conexao, int clienteId) {
        ReactivarClienteDialog d = new ReactivarClienteDialog(dono, conexao, clienteId);
        if (d.contratos.isEmpty()) {
            d.dispose();
            JOptionPane.showMessageDialog(dono, "Este cliente não tem contratos de mensalidade para retomar.\n"
                    + "Configure-os na ficha do cliente (separador Mensalidades).");
            EstadoCliente.alterar(clienteId, EstadoCliente.Estado.ACTIVO, "Reactivado (sem contratos)", BigDecimal.ZERO);
            return;
        }
        d.setVisible(true);
    }

    private ReactivarClienteDialog(Window dono, BDConexao conexao, int clienteId) {
        super(dono, ModalityType.APPLICATION_MODAL);
        this.conexao = conexao == null ? BDConexao.getInstancia() : conexao;
        this.clienteId = clienteId;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setTitle("Reactivar cliente");
        carregar();
        if (!contratos.isEmpty()) {
            montar();
            FormularioWeb.ajustar(this, 960, 700, 640, 480);
        }
    }

    private void carregar() {
        try {
            for (Devedores.Linha l : Devedores.carregar(LocalDate.now(), config.diasAlerta)) {
                if (l.clienteId == clienteId) {
                    contratos.add(l);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao ler os contratos: " + e.getMessage());
        }
        registo = EstadoCliente.de(clienteId);
        if (!contratos.isEmpty()) {
            nome = contratos.get(0).cliente;
        }
    }

    // =====================================================================
    // ECRÃ
    // =====================================================================
    private void montar() {
        String saida = registo.estado.texto() + (registo.data == null ? "" : " desde " + registo.data.toLocalDate().format(DATA))
                + (registo.motivo.isEmpty() ? "" : " · " + registo.motivo);
        JPanel cabecalho = FormularioWeb.cabecalho("Reactivar " + nome, saida, null);

        // ---- contratos a retomar
        for (Devedores.Linha l : contratos) {
            BigDecimal antiga = l.divida.add(l.multa);
            modelo.addRow(new Object[]{true, l.servico, CfMethods.formatarComoMoeda(l.preco.doubleValue()),
                Mensalidades.textoDuracao(l.contrato.getDuracao()),
                l.emAtraso.isEmpty() ? "—" : l.emAtraso.size() + " mês(es) · " + CfMethods.formatarComoMoeda(antiga.doubleValue())});
        }
        modelo.addTableModelListener(e -> actualizar());
        JTable tabela = new JTable(modelo);
        EstiloUI.estilizarTabela(tabela, true);
        tabela.getColumnModel().getColumn(0).setMaxWidth(70);
        JScrollPane rol = new JScrollPane(tabela);
        rol.setBorder(BorderFactory.createLineBorder(ComponentesWeb.LINHA));
        rol.setPreferredSize(new Dimension(600, 30 + 30 * Math.min(5, contratos.size())));
        ComponentesWeb.Cartao cartaoContratos = FormularioWeb.cartao("Serviços a retomar", ComponentesWeb.AZUL_VIVO,
                FormularioWeb.pilha(6, rol, FormularioWeb.dica("Os serviços não marcados deixam de ser cobrados "
                        + "(o contrato sai; o histórico de pagamentos fica).")));

        // ---- recomeço
        FormularioWeb.data(dcRecomeco);
        FormularioWeb.spinner(spDia);
        int diaContrato = contratos.get(0).contrato.getDiaPagamento();
        spDia.setValue(util.ui.Tema.actual() == util.ui.Tema.Negocio.GINASIO ? LocalDate.now().getDayOfMonth() : diaContrato);
        spDia.setToolTipText("Dia do mês em que passa a pagar. 0 = fim do mês.");
        dcRecomeco.addPropertyChangeListener("date", e -> {
            if (util.ui.Tema.actual() == util.ui.Tema.Negocio.GINASIO && dcRecomeco.getDate() != null) {
                spDia.setValue(recomeco().getDayOfMonth()); // no ginásio o dia é o do regresso
            }
            actualizar();
        });
        spDia.addChangeListener(e -> actualizar());
        ComponentesWeb.Cartao cartaoRecomeco = FormularioWeb.cartao("Recomeço", ComponentesWeb.VERDE,
                FormularioWeb.grelha(200, 2,
                        ComponentesWeb.campo("Volta a treinar em", dcRecomeco),
                        ComponentesWeb.campo("Dia de pagamento (0 = fim do mês)", spDia)));

        // ---- pagamento
        Vector<String> nomes = new Vector<>();
        nomes.add(SEM_TAXA);
        String sugerido = null;
        try {
            ProdutosController pc = new ProdutosController(conexao);
            nomes.addAll(pc.getVector());
            if (config.produtoReactivacao > 0) {
                TbProduto p = (TbProduto) pc.findById(config.produtoReactivacao);
                sugerido = p == null ? null : p.getDesignacao();
            }
        } catch (Exception ignorar) {
        }
        cmbTaxa.setModel(new DefaultComboBoxModel<>(nomes));
        if (sugerido != null) {
            cmbTaxa.setSelectedItem(sugerido);
        }
        FormularioWeb.combo(cmbTaxa);
        cmbTaxa.setToolTipText("Serviço que representa a taxa de reactivação (o preço vem do cadastro do produto)");
        cmbTaxa.addActionListener(e -> actualizar());
        boolean temDivida = false;
        for (Devedores.Linha l : contratos) {
            temDivida |= !l.emAtraso.isEmpty();
        }
        chkDivida.setSelected(temDivida);
        chkDivida.setEnabled(temDivida);
        chkSemMulta.setEnabled(temDivida);
        for (JCheckBox c : new JCheckBox[]{chkTaxa, chkDivida, chkSemMulta, chkPrimeiroMes}) {
            c.setOpaque(false);
            c.setFont(EstiloUI.FONTE);
            c.addActionListener(e -> actualizar());
        }
        lbTotal.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(22f));
        JPanel caixaTotal = new ComponentesWeb.CaixaSuave(new BorderLayout(), EstiloUI.AZUL_CLARO, null);
        JLabel lbT = new JLabel("A cobrar na reactivação");
        ComponentesWeb.rotulo(lbT);
        caixaTotal.add(lbT, BorderLayout.NORTH);
        caixaTotal.add(lbTotal, BorderLayout.CENTER);
        caixaTotal.add(lbDetalhe, BorderLayout.SOUTH);
        JPanel opcoes = FormularioWeb.pilha(6, ComponentesWeb.campo("Taxa de reactivação", cmbTaxa),
                chkTaxa, chkDivida, chkSemMulta, chkPrimeiroMes);
        JPanel colPag = ComponentesWeb.transparente(new ComponentesWeb.ColunasResponsivas(
                new double[]{0.55, 0.45}, new int[]{280, 240}, 10));
        colPag.add(opcoes);
        colPag.add(caixaTotal);
        ComponentesWeb.Cartao cartaoPagamento = FormularioWeb.cartao("Pagamento", ComponentesWeb.LARANJA, colPag);

        JPanel conteudo = FormularioWeb.pilha(10, cartaoContratos, cartaoRecomeco, cartaoPagamento);

        JButton btCancelar = new JButton("Cancelar");
        FormularioWeb.botaoSair(btCancelar, "Fechar sem reactivar (Esc)");
        btCancelar.addActionListener(e -> dispose());
        ComponentesWeb.botao(btReactivar, ComponentesWeb.Variante.SUCESSO, "/imagens/salvar_16x16.png", null);
        btReactivar.addActionListener(e -> reactivar());
        FormularioWeb.montar(this, cabecalho, conteudo, FormularioWeb.barraAccoes(null, btCancelar, btReactivar),
                "Reactivar cliente");
        getRootPane().registerKeyboardAction(e -> dispose(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW);
        actualizar();
    }

    // =====================================================================
    // CÁLCULO
    // =====================================================================
    private LocalDate recomeco() {
        Date d = dcRecomeco.getDate();
        return d == null ? LocalDate.now() : d.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    private TbProduto taxaCache;
    private String taxaCacheNome;

    /** O serviço escolhido para a taxa (lido só quando muda a escolha). */
    private TbProduto taxa() {
        Object n = cmbTaxa.getSelectedItem();
        if (n == null || SEM_TAXA.equals(n)) {
            return null;
        }
        if (!n.equals(taxaCacheNome)) {
            taxaCacheNome = String.valueOf(n);
            try {
                taxaCache = new ProdutosController(conexao).findByDesignacao(taxaCacheNome);
            } catch (Exception e) {
                taxaCache = null;
            }
        }
        return taxaCache;
    }

    private boolean retomar(int i) {
        return Boolean.TRUE.equals(modelo.getValueAt(i, 0));
    }

    private void actualizar() {
        BigDecimal total = BigDecimal.ZERO;
        List<String> partes = new ArrayList<>();
        TbProduto t = taxa();
        chkTaxa.setEnabled(t != null);
        if (t != null && chkTaxa.isSelected()) {
            BigDecimal v = Mensalidades.precoMensal(conexao, t.getCodigo());
            total = total.add(v);
            partes.add("taxa " + CfMethods.formatarComoMoeda(v.doubleValue()));
        }
        BigDecimal divida = BigDecimal.ZERO, mens = BigDecimal.ZERO;
        int servicos = 0;
        for (int i = 0; i < contratos.size(); i++) {
            if (!retomar(i)) {
                continue;
            }
            servicos++;
            Devedores.Linha l = contratos.get(i);
            if (chkDivida.isSelected()) {
                divida = divida.add(l.divida).add(chkSemMulta.isSelected() ? BigDecimal.ZERO : l.multa);
            }
            if (chkPrimeiroMes.isSelected()) {
                mens = mens.add(l.preco);
            }
        }
        if (divida.signum() > 0) {
            partes.add("dívida " + CfMethods.formatarComoMoeda(divida.doubleValue()));
        }
        if (mens.signum() > 0) {
            partes.add("1.º mês " + CfMethods.formatarComoMoeda(mens.doubleValue()));
        }
        total = total.add(divida).add(mens);
        lbTotal.setText(CfMethods.formatarComoMoeda(total.doubleValue()));
        LocalDate r = recomeco();
        int dia = ((Number) spDia.getValue()).intValue();
        lbDetalhe.setText((partes.isEmpty() ? "Nada a cobrar" : String.join(" + ", partes))
                + "  ·  recomeça em " + Periodo.MESES[r.getMonthValue() - 1] + "/" + r.getYear()
                + ", vence " + Mensalidades.textoDiaPagamento(dia));
        boolean venda = Mensalidades.janelaVendaAberta() != null;
        btReactivar.setText(total.signum() > 0 && venda ? "Reactivar e cobrar na venda" : "Reactivar");
        btReactivar.setToolTipText(total.signum() > 0 && !venda
                ? "Não há venda aberta: o cliente fica activo e cobra-se depois (F8 / F9 na venda)" : null);
        btReactivar.setEnabled(servicos > 0);
    }

    // =====================================================================
    // REACTIVAR
    // =====================================================================
    private void reactivar() {
        LocalDate r = recomeco();
        int dia = ((Number) spDia.getValue()).intValue();
        Periodo inicio = new Periodo(r.getYear(), r.getMonthValue());
        ConfiguracaoMesComecoController ctl = new ConfiguracaoMesComecoController(conexao.getConnectionAtiva());
        Window venda = Mensalidades.janelaVendaAberta();
        TbProduto t = taxa();
        BigDecimal valorTaxa = t != null && chkTaxa.isSelected() ? Mensalidades.precoMensal(conexao, t.getCodigo()) : BigDecimal.ZERO;

        // perdoar as multas da dívida antiga: administrador + motivo (fica no registo)
        String motivoPerdao = null;
        if (chkDivida.isSelected() && chkSemMulta.isSelected() && venda != null) {
            motivoPerdao = MesesMensalidadeDialog.autorizarReducao(this);
            if (motivoPerdao == null) {
                return;
            }
            motivoPerdao = "Reactivação: " + motivoPerdao;
        }

        // 1) o que se vai cobrar (lido antes de mexer nos contratos)
        List<int[]> dividaSemMulta = new ArrayList<>(); // {produto, ano, mes}
        List<Object[]> dividaComMulta = new ArrayList<>(); // {produto, List<Periodo>}
        List<Integer> primeiroMes = new ArrayList<>();
        StringBuilder servicos = new StringBuilder();
        for (int i = 0; i < contratos.size(); i++) {
            Devedores.Linha l = contratos.get(i);
            if (!retomar(i)) {
                if (JOptionPane.showConfirmDialog(this, "\"" + l.servico + "\" não está marcado: o contrato deste serviço sai.\nContinuar?",
                        "Serviço não retomado", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
                    return;
                }
                continue;
            }
            servicos.append(servicos.length() > 0 ? ", " : "").append(l.servico);
            if (chkDivida.isSelected() && !l.emAtraso.isEmpty()) {
                if (chkSemMulta.isSelected()) {
                    for (Periodo p : l.emAtraso) {
                        dividaSemMulta.add(new int[]{l.produtoId, p.getAno(), p.getMes()});
                    }
                } else {
                    dividaComMulta.add(new Object[]{l.produtoId, new ArrayList<>(l.emAtraso)});
                }
            }
            if (chkPrimeiroMes.isSelected()) {
                primeiroMes.add(l.produtoId);
            }
        }

        // 2) contratos: os marcados recomeçam; os outros saem
        for (int i = 0; i < contratos.size(); i++) {
            ConfiguracaoMesComeco c = contratos.get(i).contrato;
            if (retomar(i)) {
                c.setMesId(inicio.getMes());
                c.setAnoInicio(inicio.getAno());
                c.setDiaPagamento(dia);
                if (c.getDataCadastro() == null) {
                    c.setDataCadastro(new Date());
                }
                ctl.atualizar(c);
            } else {
                ctl.deletar(c.getId());
            }
        }

        // 3) estado + histórico (e a taxa escolhida fica como sugestão)
        String motivo = "Reactivado · recomeça em " + inicio.texto() + " · " + servicos;
        EstadoCliente.alterar(clienteId, EstadoCliente.Estado.ACTIVO, motivo, valorTaxa);
        if (t != null && t.getCodigo() != config.produtoReactivacao) {
            config.produtoReactivacao = t.getCodigo();
            config.gravar();
        }

        // 4) cobrança na venda
        boolean haCobrar = valorTaxa.signum() > 0 || !dividaSemMulta.isEmpty() || !dividaComMulta.isEmpty() || !primeiroMes.isEmpty();
        if (haCobrar && venda != null) {
            Mensalidades.seleccionarClienteNaVenda(nome);
            if (valorTaxa.signum() > 0) {
                Mensalidades.adicionarNaVenda(t.getCodigo(), "");
            }
            Date hoje = new Date();
            for (Object[] d : dividaComMulta) {
                @SuppressWarnings("unchecked")
                List<Periodo> ps = (List<Periodo>) d[1];
                Mensalidades.enviarParaVenda(conexao, (Integer) d[0], ps, hoje);
            }
            if (!dividaSemMulta.isEmpty()) {
                java.util.Map<Integer, List<Periodo>> porServico = new java.util.LinkedHashMap<>();
                for (int[] d : dividaSemMulta) {
                    porServico.computeIfAbsent(d[0], k -> new ArrayList<>()).add(new Periodo(d[1], d[2]));
                }
                for (java.util.Map.Entry<Integer, List<Periodo>> e : porServico.entrySet()) {
                    java.util.Map<Periodo, BigDecimal> zero = new java.util.HashMap<>();
                    for (Periodo p : e.getValue()) {
                        zero.put(p, BigDecimal.ZERO);
                    }
                    Mensalidades.enviarParaVenda(conexao, e.getKey(), e.getValue(), hoje, zero, motivoPerdao);
                }
            }
            for (Integer p : primeiroMes) {
                List<Periodo> um = new ArrayList<>();
                um.add(inicio);
                Mensalidades.enviarParaVenda(conexao, p, um, hoje);
            }
            dispose();
            venda.toFront();
            JOptionPane.showMessageDialog(venda, nome + " foi reactivado.\nA taxa e as mensalidades estão no carrinho: conclua a venda.");
        } else {
            dispose();
            JOptionPane.showMessageDialog(getOwner(), nome + " foi reactivado (recomeça em " + inicio.texto() + ")."
                    + (haCobrar ? "\n\nNão há venda aberta: cobre na venda com F9 (estado dos clientes) ou F8."
                            + (valorTaxa.signum() > 0 ? "\nNão se esqueça da taxa de reactivação (" + t.getDesignacao() + ")." : "")
                            : ""));
        }
    }
}
