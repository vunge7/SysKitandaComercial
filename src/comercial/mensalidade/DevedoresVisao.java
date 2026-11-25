package comercial.mensalidade;

import comercial.controller.ClientesController;
import entity.TbCliente;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.text.Normalizer;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
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
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.KeyStroke;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import kitanda.util.CfMethods;
import util.BDConexao;
import util.ui.ComponentesWeb;
import util.ui.EstiloUI;
import util.ui.FormularioWeb;

/**
 * Controlo de devedores das mensalidades: indicadores, dívida por serviço,
 * antiguidade da dívida e a lista com todos os filtros.
 * <p>
 * Os dados lêem-se uma vez ({@link Devedores#carregar}, em segundo plano) e
 * todos os filtros trabalham em memória — mudam o ecrã na hora. Só a data de
 * referência e "Actualizar" voltam à base de dados.
 */
public class DevedoresVisao extends JDialog {

    private static final DateTimeFormatter DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final Color COR_ATRASO = ComponentesWeb.VERMELHO;
    private static final Color COR_A_VENCER = ComponentesWeb.LARANJA;
    private static final Color COR_EM_DIA = ComponentesWeb.VERDE;
    private static final String TODOS_SERVICOS = "Todos os serviços";

    private static final String[] SITUACOES = {
        "Devedores (em atraso + vence hoje)", "Em atraso", "Vence hoje", "A vencer (próximos dias de alerta)",
        "Em dia", "Concluídos", "Todos os contratos"};
    private static final String[] ORDENS = {
        "Maior dívida", "Mais dias de atraso", "Mais meses em atraso", "Próximo vencimento", "Nome do cliente"};
    private static final int[][] FAIXAS = {{1, 30}, {31, 60}, {61, 90}, {91, 180}, {181, Integer.MAX_VALUE}};

    private final BDConexao conexao;
    private List<Devedores.Linha> dados = new ArrayList<>();
    private ConfigMensalidade config = ConfigMensalidade.ler();

    // ---- parâmetros
    private final com.toedter.calendar.JDateChooser dcReferencia = new com.toedter.calendar.JDateChooser(new Date());
    private final JTextField txtPesquisa = new JTextField();
    private final JComboBox<String> cmbServico = new JComboBox<>(new String[]{TODOS_SERVICOS});
    private final JComboBox<String> cmbSituacao = new JComboBox<>(SITUACOES);
    private final JSpinner spDiasMin = new JSpinner(new SpinnerNumberModel(0, 0, 9999, 1));
    private final JSpinner spMesesMin = new JSpinner(new SpinnerNumberModel(0, 0, 999, 1));
    private final JTextField txtDividaMin = new JTextField("0");
    private final JComboBox<String> cmbOrdem = new JComboBox<>(ORDENS);
    private final JComboBox<String> cmbVer = new JComboBox<>(new String[]{"Por contrato (cliente + serviço)", "Por cliente (soma dos serviços)"});
    private final JCheckBox chkMulta = new JCheckBox("Incluir multas no total", true);
    private final JComboBox<String> cmbInscricao = new JComboBox<>(new String[]{
        "Só clientes activos", "Só desistentes e desactivados", "Todos os clientes"});
    private int[] faixaAntiguidade; // filtro vindo do gráfico (null = nenhum)
    private final JLabel lbFaixa = new JLabel();
    private final JButton btTirarFaixa = new JButton("✕");

    // ---- indicadores, gráficos, tabela
    private final ComponentesWeb.CartaoKpi kpiDivida = new ComponentesWeb.CartaoKpi("Kz", "Total em dívida", COR_ATRASO).compacto();
    private final ComponentesWeb.CartaoKpi kpiDevedores = new ComponentesWeb.CartaoKpi(ComponentesWeb.CartaoKpi.PESSOA, "Devedores", COR_ATRASO).compacto();
    private final ComponentesWeb.CartaoKpi kpiMeses = new ComponentesWeb.CartaoKpi("M", "Meses em atraso", COR_ATRASO).compacto();
    private final ComponentesWeb.CartaoKpi kpiHoje = new ComponentesWeb.CartaoKpi("!", "Vencem hoje", COR_ATRASO).compacto();
    private final ComponentesWeb.CartaoKpi kpiAVencer = new ComponentesWeb.CartaoKpi("5", "A vencer", COR_A_VENCER).compacto();
    private final ComponentesWeb.CartaoKpi kpiEmDia = new ComponentesWeb.CartaoKpi(ComponentesWeb.CartaoKpi.VISTO, "Em dia", COR_EM_DIA).compacto();
    private final GraficoBarras grafServicos = new GraficoBarras(ComponentesWeb.AZUL_VIVO, v -> CfMethods.formatarComoMoeda(v));
    private final GraficoBarras grafAntiguidade = new GraficoBarras(ComponentesWeb.AZUL_VIVO, v -> CfMethods.formatarComoMoeda(v));
    private final DefaultTableModel modelo = new DefaultTableModel() {
        @Override
        public boolean isCellEditable(int r, int c) {
            return false;
        }

        @Override
        public Class<?> getColumnClass(int c) {
            return c == C_MESES || c == C_DIAS ? Long.class
                    : c == C_DIVIDA || c == C_MULTA || c == C_TOTAL ? BigDecimal.class : Object.class;
        }
    };
    private final JTable tabela = new JTable(modelo);
    private final JLabel lbContagem = FormularioWeb.dica(" ");
    private final JLabel lbEstado = FormularioWeb.dica("A carregar…");
    private final List<Linha> visiveis = new ArrayList<>();
    private final Timer atrasoPesquisa;
    private boolean aCarregar;

    private static final int C_CLIENTE = 0, C_TELEFONE = 1, C_SERVICO = 2, C_SITUACAO = 3, C_DIA = 4, C_MESES = 5,
            C_DESDE = 6, C_DIAS = 7, C_DIVIDA = 8, C_MULTA = 9, C_TOTAL = 10, C_PROXIMO = 11, C_ULTIMO = 12;

    /** Linha mostrada: um contrato ou a soma dos contratos de um cliente. */
    private static final class Linha {

        int clienteId;
        String cliente, telefone, nif, servico;
        Devedores.Situacao situacao;
        String dia;
        long meses, dias;
        Periodo desde;
        BigDecimal divida = BigDecimal.ZERO, multa = BigDecimal.ZERO;
        LocalDate proximo, ultimo;
        long diasParaVencer = Long.MAX_VALUE;
    }

    public static void abrir(Window dono, BDConexao conexao) {
        if (!Mensalidades.activo()) {
            JOptionPane.showMessageDialog(dono, "O controlo de mensalidades só existe nos negócios Ginásio, Centro Infantil e Prestação de Serviço.");
            return;
        }
        new DevedoresVisao(dono, conexao).setVisible(true);
    }

    public DevedoresVisao(Window dono, BDConexao conexao) {
        this(dono, conexao, true);
    }

    /** carregar = false só para pré-visualizações (os dados entram por {@link #mostrarDados}). */
    DevedoresVisao(Window dono, BDConexao conexao, boolean carregar) {
        super(dono, ModalityType.MODELESS);
        this.conexao = conexao;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setTitle("Controlo de devedores");
        atrasoPesquisa = new Timer(180, e -> aplicarFiltros());
        atrasoPesquisa.setRepeats(false);
        montar();
        FormularioWeb.ajustar(this, 1360, 860, 900, 600);
        if (carregar) {
            carregar();
        }
    }

    void mostrarDados(List<Devedores.Linha> linhas) {
        dados = linhas;
        preencherServicos();
        aplicarFiltros();
    }

    // =====================================================================
    // ECRÃ
    // =====================================================================
    private void montar() {
        // ---- cabeçalho: data de referência + actualizar
        FormularioWeb.data(dcReferencia);
        dcReferencia.setToolTipText("Situação nesta data (pagamentos registados até ela). Hoje por omissão.");
        dcReferencia.addPropertyChangeListener("date", e -> carregar());
        JButton btActualizar = new JButton("Actualizar");
        ComponentesWeb.botao(btActualizar, ComponentesWeb.Variante.SECUNDARIO, null, "Voltar a ler da base de dados (F5)");
        btActualizar.addActionListener(e -> carregar());
        JPanel direita = ComponentesWeb.transparente(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        JPanel campoData = FormularioWeb.campoCabecalho(new JLabel("Situação na data"), dcReferencia);
        campoData.setPreferredSize(new Dimension(170, 56));
        direita.add(campoData);
        direita.add(btActualizar);
        JPanel cabecalho = FormularioWeb.cabecalho("Controlo de devedores",
                "Mensalidades em atraso, a vencer e em dia", direita);

        // ---- parâmetros
        FormularioWeb.campoTexto(txtPesquisa, txtDividaMin);
        FormularioWeb.combo(cmbServico, cmbSituacao, cmbOrdem, cmbVer, cmbInscricao);
        cmbInscricao.setToolTipText("Desistentes e desactivados: só a dívida até ao dia em que saíram");
        cmbInscricao.addActionListener(e -> aplicarFiltros());
        FormularioWeb.spinner(spDiasMin);
        FormularioWeb.spinner(spMesesMin);
        txtPesquisa.setToolTipText("Nome, NIF ou telefone (sem acentos também serve)");
        txtPesquisa.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                atrasoPesquisa.restart();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                atrasoPesquisa.restart();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });
        txtDividaMin.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                atrasoPesquisa.restart();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                atrasoPesquisa.restart();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });
        cmbServico.addActionListener(e -> aplicarFiltros());
        cmbSituacao.addActionListener(e -> aplicarFiltros());
        cmbOrdem.addActionListener(e -> aplicarFiltros());
        cmbVer.addActionListener(e -> aplicarFiltros());
        spDiasMin.addChangeListener(e -> aplicarFiltros());
        spMesesMin.addChangeListener(e -> aplicarFiltros());
        chkMulta.setOpaque(false);
        chkMulta.setFont(EstiloUI.FONTE);
        chkMulta.addActionListener(e -> aplicarFiltros());

        JPanel grelhaFiltros = FormularioWeb.grelha(190, 5,
                ComponentesWeb.campo("Cliente, NIF ou telefone", txtPesquisa),
                ComponentesWeb.campo("Serviço", cmbServico),
                ComponentesWeb.campo("Situação", cmbSituacao),
                ComponentesWeb.campo("Dias de atraso (mínimo)", spDiasMin),
                ComponentesWeb.campo("Meses em atraso (mínimo)", spMesesMin),
                ComponentesWeb.campo("Dívida mínima (Kz)", txtDividaMin),
                ComponentesWeb.campo("Ordenar por", cmbOrdem),
                ComponentesWeb.campo("Ver", cmbVer),
                ComponentesWeb.campo("Inscrição", cmbInscricao),
                ComponentesWeb.campo(" ", chkMulta));

        // atalhos (pílulas): um clique e o filtro está feito
        JToggleButton pDevedores = pilula("Devedores", () -> atalho(0, 0, null));
        JToggleButton pAtraso = pilula("Em atraso", () -> atalho(1, 0, null));
        JToggleButton pHoje = pilula("Vence hoje", () -> atalho(2, 0, null));
        JToggleButton pAVencer = pilula("A vencer", () -> atalho(3, 0, null));
        JToggleButton p30 = pilula("+30 dias", () -> atalho(1, 31, null));
        JToggleButton p60 = pilula("+60 dias", () -> atalho(1, 61, null));
        JToggleButton p90 = pilula("+90 dias", () -> atalho(1, 91, null));
        JToggleButton pTodos = pilula("Todos", () -> atalho(6, 0, null));
        ButtonGroup g = new ButtonGroup();
        for (JToggleButton b : new JToggleButton[]{pDevedores, pAtraso, pHoje, pAVencer, p30, p60, p90, pTodos}) {
            g.add(b);
        }
        pDevedores.setSelected(true);
        JButton btLimpar = new JButton("Limpar filtros");
        ComponentesWeb.botao(btLimpar, ComponentesWeb.Variante.SECUNDARIO, null, "Volta aos filtros iniciais");
        btLimpar.addActionListener(e -> {
            pDevedores.setSelected(true);
            txtPesquisa.setText("");
            txtDividaMin.setText("0");
            cmbServico.setSelectedIndex(0);
            cmbOrdem.setSelectedIndex(0);
            atalho(0, 0, null);
        });
        lbFaixa.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(12f));
        lbFaixa.setForeground(ComponentesWeb.AZUL);
        ComponentesWeb.botao(btTirarFaixa, ComponentesWeb.Variante.SECUNDARIO, null, "Tirar o filtro de antiguidade");
        btTirarFaixa.addActionListener(e -> {
            faixaAntiguidade = null;
            aplicarFiltros();
        });
        JPanel atalhos = FormularioWeb.pilulas(pDevedores, pAtraso, pHoje, pAVencer, p30, p60, p90, pTodos);
        atalhos.add(btLimpar);
        atalhos.add(lbFaixa);
        atalhos.add(btTirarFaixa);
        atalhos.setLayout(new EstiloUI.WrapLayout(FlowLayout.LEFT, 6, 2));

        ComponentesWeb.Cartao cartaoFiltros = FormularioWeb.cartao("Parâmetros da pesquisa", ComponentesWeb.AZUL_VIVO,
                FormularioWeb.pilha(10, atalhos, grelhaFiltros));

        // ---- indicadores
        for (ComponentesWeb.CartaoKpi k : new ComponentesWeb.CartaoKpi[]{kpiDivida, kpiDevedores, kpiMeses, kpiHoje, kpiAVencer, kpiEmDia}) {
            k.setValor("—", " ");
        }
        JPanel kpis = FormularioWeb.grelha(170, 6, kpiDivida, kpiDevedores, kpiMeses, kpiHoje, kpiAVencer, kpiEmDia);

        // ---- gráficos
        grafServicos.setAoClicar(b -> {
            if (b.chave == null) {
                return; // "Outros"
            }
            cmbServico.setSelectedItem(cmbServico.getSelectedItem().equals(b.chave) ? TODOS_SERVICOS : b.chave);
        });
        grafAntiguidade.setAoClicar(b -> {
            int[] f = (int[]) b.chave;
            faixaAntiguidade = faixaAntiguidade != null && faixaAntiguidade[0] == f[0] ? null : f;
            aplicarFiltros();
        });
        ComponentesWeb.Cartao cartaoServicos = FormularioWeb.cartao("Dívida por serviço", ComponentesWeb.AZUL_VIVO,
                FormularioWeb.pilha(6, grafServicos, FormularioWeb.dica("Clique numa barra para ver só esse serviço.")));
        ComponentesWeb.Cartao cartaoAntiguidade = FormularioWeb.cartao("Antiguidade da dívida", ComponentesWeb.AZUL_VIVO,
                FormularioWeb.pilha(6, grafAntiguidade, FormularioWeb.dica("Valor dos meses em atraso por dias de atraso. Clique para filtrar.")));
        JPanel graficos = ComponentesWeb.transparente(new ComponentesWeb.ColunasResponsivas(
                new double[]{0.5, 0.5}, new int[]{340, 340}, 10));
        graficos.add(cartaoServicos);
        graficos.add(cartaoAntiguidade);

        // ---- tabela
        modelo.setColumnIdentifiers(new Object[]{"Cliente", "Telefone", "Serviço", "Situação", "Vence", "Meses",
            "Desde", "Dias atraso", "Dívida", "Multa", "Total", "Próx. vencimento", "Últ. pagamento"});
        EstiloUI.estilizarTabela(tabela, true);
        tabela.setAutoCreateRowSorter(false);
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(modelo);
        tabela.setRowSorter(sorter);
        RendererMoeda moeda = new RendererMoeda();
        tabela.getColumnModel().getColumn(C_DIVIDA).setCellRenderer(moeda);
        tabela.getColumnModel().getColumn(C_MULTA).setCellRenderer(moeda);
        tabela.getColumnModel().getColumn(C_TOTAL).setCellRenderer(moeda);
        tabela.getColumnModel().getColumn(C_SITUACAO).setCellRenderer(new RendererSituacao());
        int[] larguras = {200, 110, 170, 110, 80, 80, 90, 80, 110, 90, 120, 120, 120};
        for (int i = 0; i < larguras.length; i++) {
            tabela.getColumnModel().getColumn(i).setPreferredWidth(larguras[i]);
        }
        tabela.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
        tabela.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    verCliente();
                }
            }
        });
        JScrollPane rol = new JScrollPane(tabela);
        rol.setBorder(BorderFactory.createLineBorder(ComponentesWeb.LINHA));
        rol.setPreferredSize(new Dimension(900, 320));
        rol.setMinimumSize(new Dimension(300, 160));

        JButton btVer = new JButton("Ver meses do cliente");
        JButton btCsv = new JButton("Exportar (Excel/CSV)");
        JButton btImprimir = new JButton("Imprimir lista");
        JButton btMapaGeral = new JButton("Mapa geral (PDF)");
        JButton btMapaCliente = new JButton("Mapa por cliente (PDF)");
        ComponentesWeb.botao(btMapaGeral, ComponentesWeb.Variante.PRIMARIO, null,
                "Mapa de controlo de devedores em paisagem, com a lista filtrada");
        ComponentesWeb.botao(btMapaCliente, ComponentesWeb.Variante.PRIMARIO, null,
                "Mês a mês do cliente seleccionado (ou de todos os da lista, um por página)");
        btMapaGeral.addActionListener(e -> mapaGeral());
        btMapaCliente.addActionListener(e -> mapaPorCliente());
        ComponentesWeb.botao(btVer, ComponentesWeb.Variante.PRIMARIO, null, "Contratos e meses do cliente seleccionado (duplo clique)");
        ComponentesWeb.botao(btCsv, ComponentesWeb.Variante.SECUNDARIO, null, "Grava a lista filtrada para abrir no Excel");
        ComponentesWeb.botao(btImprimir, ComponentesWeb.Variante.SECUNDARIO, null, "Imprime a lista filtrada");
        btVer.addActionListener(e -> verCliente());
        btCsv.addActionListener(e -> exportarCsv());
        btImprimir.addActionListener(e -> imprimir());
        JPanel accoesTabela = ComponentesWeb.transparente(new EstiloUI.WrapLayout(FlowLayout.LEFT, 8, 0));
        accoesTabela.add(btVer);
        accoesTabela.add(btMapaGeral);
        accoesTabela.add(btMapaCliente);
        accoesTabela.add(btCsv);
        accoesTabela.add(btImprimir);
        accoesTabela.add(lbContagem);

        JPanel corpoTabela = ComponentesWeb.transparente(new BorderLayout(0, 8));
        corpoTabela.add(accoesTabela, BorderLayout.NORTH);
        corpoTabela.add(rol, BorderLayout.CENTER);
        ComponentesWeb.Cartao cartaoTabela = FormularioWeb.cartao("Lista", ComponentesWeb.AZUL_VIVO, corpoTabela);

        JPanel conteudo = FormularioWeb.pilha(10, cartaoFiltros, kpis, graficos, cartaoTabela);

        // ---- acções
        JButton btAlertas = new JButton("Alertas de renovação…");
        JButton btMultas = new JButton("Multas…");
        ComponentesWeb.botao(btMultas, ComponentesWeb.Variante.SECUNDARIO, null, "Relatório e regras das multas");
        btMultas.addActionListener(e -> MultasRelatorioVisao.abrir(this, conexao));
        JButton btTestar = new JButton("Ver alertas agora");
        JButton btFechar = new JButton("Fechar");
        ComponentesWeb.botao(btAlertas, ComponentesWeb.Variante.SECUNDARIO, null, "Dias de aviso, intervalo, duração e som dos alertas");
        ComponentesWeb.botao(btTestar, ComponentesWeb.Variante.AVISO, null, "Mostra já os alertas de renovação de hoje");
        FormularioWeb.botaoSair(btFechar, "Fechar (Esc)");
        btAlertas.addActionListener(e -> configurarAlertas());
        btTestar.addActionListener(e -> AlertasMensalidade.verificarAgora(true));
        btFechar.addActionListener(e -> dispose());
        JPanel accoes = FormularioWeb.barraAccoes(lbEstado, btMultas, btAlertas, btTestar, btFechar);

        FormularioWeb.montar(this, cabecalho, conteudo, accoes, "Controlo de devedores");
        JComponent raiz = getRootPane();
        raiz.registerKeyboardAction(e -> dispose(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
        raiz.registerKeyboardAction(e -> carregar(), KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
        raiz.registerKeyboardAction(e -> txtPesquisa.requestFocusInWindow(),
                KeyStroke.getKeyStroke(KeyEvent.VK_F, java.awt.event.InputEvent.CTRL_DOWN_MASK), JComponent.WHEN_IN_FOCUSED_WINDOW);
        actualizarRotuloAVencer();
    }

    private static JToggleButton pilula(String texto, Runnable accao) {
        JToggleButton b = new JToggleButton(texto);
        b.addActionListener(e -> accao.run());
        return b;
    }

    private void atalho(int situacao, int diasMin, int[] faixa) {
        faixaAntiguidade = faixa;
        aCarregar = true; // um só refresh para as várias mudanças
        cmbSituacao.setSelectedIndex(situacao);
        spDiasMin.setValue(diasMin);
        spMesesMin.setValue(0);
        aCarregar = false;
        aplicarFiltros();
    }

    private void actualizarRotuloAVencer() {
        SITUACOES[3] = "A vencer (próximos " + config.diasAlerta + " dias)";
        int sel = cmbSituacao.getSelectedIndex();
        aCarregar = true;
        cmbSituacao.setModel(new DefaultComboBoxModel<>(SITUACOES));
        cmbSituacao.setSelectedIndex(Math.max(0, sel));
        aCarregar = false;
    }

    // =====================================================================
    // DADOS
    // =====================================================================
    private LocalDate referencia() {
        Date d = dcReferencia.getDate();
        return d == null ? LocalDate.now() : d.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    private void carregar() {
        final LocalDate ref = referencia();
        final int dias = config.diasAlerta;
        lbEstado.setText("A carregar…");
        setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.WAIT_CURSOR));
        new SwingWorker<List<Devedores.Linha>, Void>() {
            @Override
            protected List<Devedores.Linha> doInBackground() throws Exception {
                return Devedores.carregar(ref, dias);
            }

            @Override
            protected void done() {
                setCursor(java.awt.Cursor.getDefaultCursor());
                try {
                    dados = get();
                    preencherServicos();
                    aplicarFiltros();
                    lbEstado.setText(dados.size() + " contratos lidos · situação em " + ref.format(DATA)
                            + " · " + java.time.LocalTime.now().withNano(0).withSecond(0) + "  ·  F5 actualiza, Ctrl+F pesquisa");
                } catch (Exception e) {
                    lbEstado.setText("Erro ao carregar: " + e.getMessage());
                    JOptionPane.showMessageDialog(DevedoresVisao.this, "Erro ao carregar as mensalidades:\n" + e.getMessage());
                }
            }
        }.execute();
    }

    private void preencherServicos() {
        Object sel = cmbServico.getSelectedItem();
        TreeSet<String> nomes = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        for (Devedores.Linha l : dados) {
            nomes.add(l.servico);
        }
        List<String> v = new ArrayList<>();
        v.add(TODOS_SERVICOS);
        v.addAll(nomes);
        aCarregar = true;
        cmbServico.setModel(new DefaultComboBoxModel<>(v.toArray(new String[0])));
        cmbServico.setSelectedItem(sel != null && nomes.contains(String.valueOf(sel)) ? sel : TODOS_SERVICOS);
        aCarregar = false;
    }

    // =====================================================================
    // FILTROS (em memória)
    // =====================================================================
    private boolean passaSituacao(Devedores.Situacao s) {
        switch (cmbSituacao.getSelectedIndex()) {
            case 0:
                return s == Devedores.Situacao.ATRASO || s == Devedores.Situacao.VENCE_HOJE;
            case 1:
                return s == Devedores.Situacao.ATRASO;
            case 2:
                return s == Devedores.Situacao.VENCE_HOJE;
            case 3:
                return s == Devedores.Situacao.A_VENCER || s == Devedores.Situacao.VENCE_HOJE;
            case 4:
                return s == Devedores.Situacao.EM_DIA || s == Devedores.Situacao.A_VENCER;
            case 5:
                return s == Devedores.Situacao.CONCLUIDO;
            default:
                return true;
        }
    }

    private boolean passaSituacaoComInactivos(Devedores.Situacao s) {
        // "Todos os contratos" e a vista dos inactivos mostram também os que já não devem nada
        return s == Devedores.Situacao.INACTIVO
                ? cmbSituacao.getSelectedIndex() == 6 || cmbInscricao.getSelectedIndex() == 1
                : passaSituacao(s);
    }

    private static String chaveTexto(String s) {
        return Normalizer.normalize(s == null ? "" : s, Normalizer.Form.NFD).replaceAll("\\p{M}", "").toLowerCase();
    }

    /** Os contratos que passam nos filtros que não dependem do agrupamento. */
    private List<Devedores.Linha> filtrados(boolean comServico, boolean comFaixa) {
        String q = chaveTexto(txtPesquisa.getText().trim());
        String servico = String.valueOf(cmbServico.getSelectedItem());
        int diasMin = ((Number) spDiasMin.getValue()).intValue();
        int mesesMin = ((Number) spMesesMin.getValue()).intValue();
        List<Devedores.Linha> r = new ArrayList<>();
        int inscricao = cmbInscricao.getSelectedIndex();
        for (Devedores.Linha l : dados) {
            if ((inscricao == 0 && l.inactivo()) || (inscricao == 1 && !l.inactivo())) {
                continue;
            }
            if (!q.isEmpty() && !chaveTexto(l.cliente).contains(q) && !l.nif.toLowerCase().contains(q)
                    && !l.telefone.replace(" ", "").contains(q.replace(" ", ""))) {
                continue;
            }
            if (comServico && !TODOS_SERVICOS.equals(servico) && !servico.equals(l.servico)) {
                continue;
            }
            if (!passaSituacaoComInactivos(l.situacao)) {
                continue;
            }
            if (diasMin > 0 && l.diasAtraso < diasMin) {
                continue;
            }
            if (mesesMin > 0 && l.emAtraso.size() < mesesMin) {
                continue;
            }
            if (comFaixa && faixaAntiguidade != null
                    && (l.emAtraso.isEmpty() || l.diasAtraso < faixaAntiguidade[0] || l.diasAtraso > faixaAntiguidade[1])) {
                continue;
            }
            r.add(l);
        }
        return r;
    }

    private BigDecimal dividaMinima() {
        try {
            return new BigDecimal(txtDividaMin.getText().trim().replace(".", "").replace(",", "."));
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }

    private void aplicarFiltros() {
        if (aCarregar) {
            return;
        }
        boolean comMulta = chkMulta.isSelected();
        List<Devedores.Linha> base = filtrados(true, true);

        // ---- linhas a mostrar (por contrato ou somadas por cliente)
        visiveis.clear();
        if (cmbVer.getSelectedIndex() == 1) {
            Map<Integer, Linha> porCliente = new LinkedHashMap<>();
            for (Devedores.Linha l : base) {
                Linha a = porCliente.get(l.clienteId);
                if (a == null) {
                    a = deContrato(l);
                    a.servico = l.servico;
                    porCliente.put(l.clienteId, a);
                } else {
                    a.servico = a.servico + ", " + l.servico;
                    a.meses += l.emAtraso.size();
                    a.dias = Math.max(a.dias, l.diasAtraso);
                    a.divida = a.divida.add(l.divida);
                    a.multa = a.multa.add(l.multa);
                    if (l.situacao.ordinal() < a.situacao.ordinal()) {
                        a.situacao = l.situacao;
                    }
                    if (l.desde() != null && (a.desde == null || l.desde().compareTo(a.desde) < 0)) {
                        a.desde = l.desde();
                    }
                    if (l.proximoVencimento != null && (a.proximo == null || l.proximoVencimento.isBefore(a.proximo))) {
                        a.proximo = l.proximoVencimento;
                    }
                    if (l.ultimoPagamento != null && (a.ultimo == null || l.ultimoPagamento.isAfter(a.ultimo))) {
                        a.ultimo = l.ultimoPagamento;
                    }
                    a.diasParaVencer = Math.min(a.diasParaVencer, l.diasParaVencer);
                    a.dia = "vários";
                }
            }
            visiveis.addAll(porCliente.values());
        } else {
            for (Devedores.Linha l : base) {
                visiveis.add(deContrato(l));
            }
        }
        BigDecimal minimo = dividaMinima();
        if (minimo.signum() > 0) {
            visiveis.removeIf(v -> total(v, comMulta).compareTo(minimo) < 0);
        }
        visiveis.sort(ordem(comMulta));

        // ---- tabela
        modelo.setRowCount(0);
        BigDecimal soma = BigDecimal.ZERO;
        for (Linha v : visiveis) {
            modelo.addRow(new Object[]{v.cliente, v.telefone, v.servico, v.situacao, v.dia, v.meses,
                v.desde == null ? "—" : v.desde.curto(), v.dias, v.divida, v.multa, total(v, comMulta),
                v.proximo == null ? "—" : v.proximo.format(DATA) + quando(v.diasParaVencer),
                v.ultimo == null ? "—" : v.ultimo.format(DATA)});
            soma = soma.add(total(v, comMulta));
        }
        lbContagem.setText(visiveis.size() + (cmbVer.getSelectedIndex() == 1 ? " cliente(s)" : " contrato(s)")
                + "  ·  total " + CfMethods.formatarComoMoeda(soma.doubleValue()));

        // ---- indicadores: dívida sobre os filtros; vencem hoje / a vencer / em dia sobre todos os contratos
        actualizarIndicadores(comMulta);
        // ---- gráficos (cada um ignora o seu próprio filtro, para se poder trocar de barra)
        actualizarGraficos(comMulta);
        lbFaixa.setText(faixaAntiguidade == null ? "" : "Antiguidade: " + textoFaixa(faixaAntiguidade));
        btTirarFaixa.setVisible(faixaAntiguidade != null);
    }

    private static String quando(long dias) {
        if (dias == Long.MAX_VALUE) {
            return "";
        }
        return dias == 0 ? " (hoje)" : dias == 1 ? " (amanhã)" : " (" + dias + " dias)";
    }

    private Linha deContrato(Devedores.Linha l) {
        Linha a = new Linha();
        a.clienteId = l.clienteId;
        a.cliente = l.cliente;
        a.telefone = l.telefone;
        a.nif = l.nif;
        a.servico = l.servico;
        a.situacao = l.situacao;
        a.dia = Mensalidades.textoDiaPagamento(l.contrato.getDiaPagamento());
        a.meses = l.emAtraso.size();
        a.dias = l.diasAtraso;
        a.desde = l.desde();
        a.divida = l.divida;
        a.multa = l.multa;
        a.proximo = l.proximoVencimento;
        a.ultimo = l.ultimoPagamento;
        a.diasParaVencer = l.diasParaVencer;
        return a;
    }

    private static BigDecimal total(Linha v, boolean comMulta) {
        return comMulta ? v.divida.add(v.multa) : v.divida;
    }

    private Comparator<Linha> ordem(boolean comMulta) {
        Comparator<Linha> porNome = Comparator.comparing(v -> chaveTexto(v.cliente));
        switch (cmbOrdem.getSelectedIndex()) {
            case 1:
                return Comparator.comparingLong((Linha v) -> v.dias).reversed().thenComparing(porNome);
            case 2:
                return Comparator.comparingLong((Linha v) -> v.meses).reversed().thenComparing(porNome);
            case 3:
                return Comparator.comparingLong((Linha v) -> v.diasParaVencer).thenComparing(porNome);
            case 4:
                return porNome;
            default:
                return Comparator.comparing((Linha v) -> total(v, comMulta)).reversed().thenComparing(porNome);
        }
    }

    private void actualizarIndicadores(boolean comMulta) {
        BigDecimal divida = BigDecimal.ZERO, multas = BigDecimal.ZERO;
        java.util.Set<Integer> devedores = new java.util.HashSet<>();
        int meses = 0, hoje = 0, aVencer = 0, emDia = 0;
        for (Devedores.Linha l : filtrados(true, true)) {
            if (l.situacao == Devedores.Situacao.ATRASO || l.situacao == Devedores.Situacao.VENCE_HOJE) {
                divida = divida.add(l.divida);
                multas = multas.add(l.multa);
                devedores.add(l.clienteId);
            }
            meses += l.emAtraso.size();
        }
        // vencem hoje / a vencer / em dia: contam sobre todos os contratos (pesquisa e serviço aplicados)
        String q = txtPesquisa.getText().trim();
        String servico = String.valueOf(cmbServico.getSelectedItem());
        for (Devedores.Linha l : dados) {
            if (l.inactivo()) {
                continue;
            }
            if (!q.isEmpty() && !chaveTexto(l.cliente).contains(chaveTexto(q))) {
                continue;
            }
            if (!TODOS_SERVICOS.equals(servico) && !servico.equals(l.servico)) {
                continue;
            }
            if (l.situacao == Devedores.Situacao.VENCE_HOJE) {
                hoje++;
            } else if (l.situacao == Devedores.Situacao.A_VENCER) {
                aVencer++;
            } else if (l.situacao == Devedores.Situacao.EM_DIA || l.situacao == Devedores.Situacao.CONCLUIDO) {
                emDia++;
            }
        }
        BigDecimal total = comMulta ? divida.add(multas) : divida;
        kpiDivida.setValor(CfMethods.formatarComoMoeda(total.doubleValue()),
                multas.signum() > 0 ? "inclui " + CfMethods.formatarComoMoeda(multas.doubleValue()) + " de multas"
                        : "sem multas");
        kpiDevedores.setValor(String.valueOf(devedores.size()), devedores.size() == 1 ? "cliente" : "clientes");
        kpiMeses.setValor(String.valueOf(meses), meses == 0 ? "nenhum" : "mensalidades vencidas");
        kpiHoje.setValor(String.valueOf(hoje), hoje == 0 ? "nenhuma" : "cobrar hoje");
        kpiAVencer.setValor(String.valueOf(aVencer), "nos próximos " + config.diasAlerta + " dias");
        kpiEmDia.setValor(String.valueOf(emDia), "contratos");
    }

    private void actualizarGraficos(boolean comMulta) {
        // dívida por serviço: filtros todos menos o do serviço
        Map<String, BigDecimal> porServico = new LinkedHashMap<>();
        Map<String, Integer> clientesServico = new LinkedHashMap<>();
        for (Devedores.Linha l : filtrados(false, true)) {
            BigDecimal v = l.total(comMulta);
            if (v.signum() <= 0) {
                continue;
            }
            porServico.merge(l.servico, v, BigDecimal::add);
            clientesServico.merge(l.servico, 1, Integer::sum);
        }
        List<Map.Entry<String, BigDecimal>> ord = new ArrayList<>(porServico.entrySet());
        ord.sort((a, b) -> b.getValue().compareTo(a.getValue()));
        List<GraficoBarras.Barra> bs = new ArrayList<>();
        BigDecimal outros = BigDecimal.ZERO;
        int nOutros = 0;
        for (int i = 0; i < ord.size(); i++) {
            Map.Entry<String, BigDecimal> e = ord.get(i);
            if (i < 7) {
                bs.add(new GraficoBarras.Barra(e.getKey(), e.getValue().doubleValue(),
                        clientesServico.get(e.getKey()) + " contrato(s) com dívida", e.getKey()));
            } else {
                outros = outros.add(e.getValue());
                nOutros++;
            }
        }
        if (nOutros > 0) {
            bs.add(new GraficoBarras.Barra("Outros (" + nOutros + ")", outros.doubleValue(), null, null));
        }
        grafServicos.setBarras(bs);
        grafServicos.setSeleccionada(TODOS_SERVICOS.equals(cmbServico.getSelectedItem()) ? null : cmbServico.getSelectedItem());

        // antiguidade: valor de cada mês em atraso pela sua idade; ignora o filtro de antiguidade
        double[] valores = new double[FAIXAS.length];
        int[] contagem = new int[FAIXAS.length];
        for (Devedores.Linha l : filtrados(true, false)) {
            for (int i = 0; i < l.emAtraso.size(); i++) {
                long d = l.diasPorMes.get(i);
                for (int f = 0; f < FAIXAS.length; f++) {
                    if (d >= FAIXAS[f][0] && d <= FAIXAS[f][1]) {
                        valores[f] += l.preco.doubleValue();
                        contagem[f]++;
                        break;
                    }
                }
            }
        }
        List<GraficoBarras.Barra> ba = new ArrayList<>();
        for (int f = 0; f < FAIXAS.length; f++) {
            ba.add(new GraficoBarras.Barra(textoFaixa(FAIXAS[f]), valores[f],
                    contagem[f] + " mensalidade(s)", FAIXAS[f]));
        }
        grafAntiguidade.setBarras(ba);
        grafAntiguidade.setSeleccionada(faixaAntiguidade);
    }

    private static String textoFaixa(int[] f) {
        return f[1] == Integer.MAX_VALUE ? "mais de " + (f[0] - 1) + " dias" : f[0] + " a " + f[1] + " dias";
    }

    // =====================================================================
    // ACÇÕES
    // =====================================================================
    private void verCliente() {
        int r = tabela.getSelectedRow();
        if (r < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione um cliente na lista.");
            return;
        }
        Linha v = visiveis.get(tabela.convertRowIndexToModel(r));
        TbCliente c = new ClientesController(conexao).findByCodigo(v.clienteId);
        if (c == null) {
            return;
        }
        PainelContratosCliente.abrirDialogo(this, conexao, c);
        carregar(); // pode ter mudado contratos
    }

    private String descricaoFiltros() {
        StringBuilder f = new StringBuilder(String.valueOf(cmbSituacao.getSelectedItem()));
        if (!TODOS_SERVICOS.equals(cmbServico.getSelectedItem())) {
            f.append(" · ").append(cmbServico.getSelectedItem());
        }
        if (((Number) spDiasMin.getValue()).intValue() > 0) {
            f.append(" · atraso ≥ ").append(spDiasMin.getValue()).append(" dias");
        }
        if (((Number) spMesesMin.getValue()).intValue() > 0) {
            f.append(" · ≥ ").append(spMesesMin.getValue()).append(" meses");
        }
        if (faixaAntiguidade != null) {
            f.append(" · ").append(textoFaixa(faixaAntiguidade));
        }
        if (!txtPesquisa.getText().trim().isEmpty()) {
            f.append(" · \"").append(txtPesquisa.getText().trim()).append('"');
        }
        if (cmbInscricao.getSelectedIndex() > 0) {
            f.append(" · ").append(cmbInscricao.getSelectedItem());
        }
        return f.toString();
    }

    private void mapaGeral() {
        if (visiveis.isEmpty()) {
            JOptionPane.showMessageDialog(this, "A lista está vazia com estes filtros.");
            return;
        }
        List<MapaDevedores.LinhaGeral> linhas = new ArrayList<>();
        boolean comMulta = chkMulta.isSelected();
        for (int r = 0; r < tabela.getRowCount(); r++) {
            Linha v = visiveis.get(tabela.convertRowIndexToModel(r));
            MapaDevedores.LinhaGeral g = new MapaDevedores.LinhaGeral();
            g.cliente = v.cliente;
            g.telefone = v.telefone;
            g.servico = v.servico;
            g.situacao = v.situacao.texto();
            g.vence = v.dia;
            g.meses = (int) v.meses;
            g.desde = v.desde == null ? "" : v.desde.curto();
            g.dias = (int) v.dias;
            g.divida = v.divida;
            g.multa = comMulta ? v.multa : BigDecimal.ZERO;
            g.total = total(v, comMulta);
            g.proximo = v.proximo == null ? "" : v.proximo.format(DATA);
            g.ultimo = v.ultimo == null ? "" : v.ultimo.format(DATA);
            linhas.add(g);
        }
        MapaDevedores.geral(conexao, linhas, referencia(), descricaoFiltros());
    }

    private void mapaPorCliente() {
        List<Integer> clientesMapa = new ArrayList<>();
        int sel = tabela.getSelectedRow();
        if (sel >= 0) {
            clientesMapa.add(visiveis.get(tabela.convertRowIndexToModel(sel)).clienteId);
        } else {
            if (visiveis.isEmpty()) {
                JOptionPane.showMessageDialog(this, "A lista está vazia com estes filtros.");
                return;
            }
            if (JOptionPane.showConfirmDialog(this, "Nenhum cliente seleccionado.\nFazer o mapa de todos os "
                    + visiveis.size() + " da lista (um por página)?", "Mapa por cliente",
                    JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
                return;
            }
            for (Linha v : visiveis) {
                if (!clientesMapa.contains(v.clienteId)) {
                    clientesMapa.add(v.clienteId);
                }
            }
        }
        List<Devedores.Linha> contratos = new ArrayList<>();
        for (Integer id : clientesMapa) {
            for (Devedores.Linha l : filtrados(true, true)) {
                if (l.clienteId == id) {
                    contratos.add(l);
                }
            }
        }
        MapaDevedores.porCliente(conexao, contratos, referencia(), chkMulta.isSelected());
    }

    private void exportarCsv() {
        JFileChooser fc = new JFileChooser();
        fc.setSelectedFile(new File("devedores_" + referencia().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + ".csv"));
        if (fc.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }
        File f = fc.getSelectedFile();
        if (!f.getName().toLowerCase().endsWith(".csv")) {
            f = new File(f.getParentFile(), f.getName() + ".csv");
        }
        try (PrintWriter w = new PrintWriter(new OutputStreamWriter(new FileOutputStream(f), StandardCharsets.UTF_8))) {
            w.print('﻿'); // o Excel reconhece o UTF-8 (acentos)
            for (int c = 0; c < modelo.getColumnCount(); c++) {
                w.print((c > 0 ? ";" : "") + modelo.getColumnName(c));
            }
            w.println();
            for (int r = 0; r < tabela.getRowCount(); r++) {
                int m = tabela.convertRowIndexToModel(r);
                for (int c = 0; c < modelo.getColumnCount(); c++) {
                    Object o = modelo.getValueAt(m, c);
                    String s = o instanceof BigDecimal ? ((BigDecimal) o).setScale(2, java.math.RoundingMode.HALF_UP).toPlainString().replace('.', ',')
                            : o instanceof Devedores.Situacao ? ((Devedores.Situacao) o).texto() : String.valueOf(o);
                    w.print((c > 0 ? ";" : "") + s.replace(";", ","));
                }
                w.println();
            }
            JOptionPane.showMessageDialog(this, "Lista gravada em:\n" + f.getAbsolutePath());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Não foi possível gravar: " + e.getMessage());
        }
    }

    private void imprimir() {
        try {
            tabela.print(JTable.PrintMode.FIT_WIDTH,
                    new MessageFormat("Controlo de devedores — " + referencia().format(DATA)),
                    new MessageFormat("Página {0}  ·  " + lbContagem.getText().replace("'", "''")));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Não foi possível imprimir: " + e.getMessage());
        }
    }

    private void configurarAlertas() {
        ConfigMensalidade c = ConfigMensalidade.ler();
        JSpinner spDias = new JSpinner(new SpinnerNumberModel(c.diasAlerta, 0, 60, 1));
        JSpinner spIntervalo = new JSpinner(new SpinnerNumberModel(c.intervaloMinutos, 1, 1440, 5));
        JSpinner spDuracao = new JSpinner(new SpinnerNumberModel(c.duracaoSegundos, 5, 3600, 10));
        JCheckBox chkSom = new JCheckBox("Tocar som", c.som);
        JCheckBox chkActivo = new JCheckBox("Alertas ligados", c.activos);
        JCheckBox chkAlarme = new JCheckBox("Alarme: repetir o som até alguém o silenciar", c.alarmeContinuo);
        JCheckBox chkDevedores = new JCheckBox("Avisar também os devedores (mensalidades em atraso)", c.alertarAtrasados);
        for (JSpinner s : new JSpinner[]{spDias, spIntervalo, spDuracao}) {
            FormularioWeb.spinner(s);
        }
        chkSom.setOpaque(false);
        chkActivo.setOpaque(false);
        chkAlarme.setOpaque(false);
        chkDevedores.setOpaque(false);
        chkAlarme.setEnabled(chkSom.isSelected());
        chkSom.addActionListener(e -> chkAlarme.setEnabled(chkSom.isSelected()));
        JPanel p = FormularioWeb.pilha(10,
                FormularioWeb.dica("Laranja: faltam até N dias para a renovação.  Vermelho: vence hoje ou em atraso (devedores)."),
                FormularioWeb.grelha(170, 3,
                        ComponentesWeb.campo("Avisar com (dias de antecedência)", spDias),
                        ComponentesWeb.campo("Repetir a cada (minutos)", spIntervalo),
                        ComponentesWeb.campo("Fica no ecrã (segundos)", spDuracao)),
                chkSom, chkAlarme, chkDevedores, chkActivo);
        p.setPreferredSize(new Dimension(620, 230));
        if (JOptionPane.showConfirmDialog(this, p, "Alertas de renovação das mensalidades",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE) != JOptionPane.OK_OPTION) {
            return;
        }
        c.diasAlerta = ((Number) spDias.getValue()).intValue();
        c.intervaloMinutos = ((Number) spIntervalo.getValue()).intValue();
        c.duracaoSegundos = ((Number) spDuracao.getValue()).intValue();
        c.som = chkSom.isSelected();
        c.alarmeContinuo = chkAlarme.isSelected();
        c.alertarAtrasados = chkDevedores.isSelected();
        c.activos = chkActivo.isSelected();
        if (c.gravar()) {
            config = c;
            AlertasMensalidade.reprogramar();
            actualizarRotuloAVencer();
            carregar();
        } else {
            JOptionPane.showMessageDialog(this, "Não foi possível gravar a configuração.");
        }
    }

    // =====================================================================
    // RENDERERS
    // =====================================================================
    private static final class RendererMoeda extends EstiloUI.ZebraRenderer {

        RendererMoeda() {
            super(SwingConstants.RIGHT);
        }

        @Override
        public Component getTableCellRendererComponent(JTable t, Object v, boolean sel, boolean foco, int r, int c) {
            Object texto = v instanceof BigDecimal
                    ? (((BigDecimal) v).signum() == 0 ? "—" : CfMethods.formatarComoMoeda(((BigDecimal) v).doubleValue())) : v;
            return super.getTableCellRendererComponent(t, texto, sel, foco, r, c);
        }
    }

    /** Situação com a cor de estado E o texto (nunca só a cor). */
    private static final class RendererSituacao extends EstiloUI.ZebraRenderer {

        RendererSituacao() {
            super(SwingConstants.LEFT);
        }

        @Override
        public Component getTableCellRendererComponent(JTable t, Object v, boolean sel, boolean foco, int r, int c) {
            Devedores.Situacao s = v instanceof Devedores.Situacao ? (Devedores.Situacao) v : null;
            Component comp = super.getTableCellRendererComponent(t, s == null ? v : "●  " + s.texto(), sel, foco, r, c);
            if (!sel && s != null) {
                Color cor = s == Devedores.Situacao.ATRASO || s == Devedores.Situacao.VENCE_HOJE ? COR_ATRASO
                        : s == Devedores.Situacao.A_VENCER ? COR_A_VENCER.darker()
                        : s == Devedores.Situacao.INACTIVO ? ComponentesWeb.TEXTO_SUAVE : COR_EM_DIA.darker();
                comp.setForeground(cor);
            }
            comp.setFont(EstiloUI.FONTE_NEGRITO);
            return comp;
        }
    }
}
