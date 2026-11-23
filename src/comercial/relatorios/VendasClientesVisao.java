package comercial.relatorios;

import comercial.mensalidade.GraficoBarras;
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
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import kitanda.util.CfMethods;
import util.BDConexao;
import util.ui.ComponentesWeb;
import util.ui.EstiloUI;
import util.ui.FormularioWeb;

/**
 * Vendas de todos os clientes, ou de um só, num intervalo de datas: na tabela
 * (por documento, resumo por cliente ou por artigo) e em relatório do iReport
 * (A4 paisagem) com os mesmos filtros.
 * <p>
 * Lê os documentos de venda do período uma vez; cliente, documentos,
 * utilizador, pesquisa e anuladas filtram em memória. As linhas (artigos) só
 * se lêem quando se pede a vista por artigo.
 */
public class VendasClientesVisao extends JDialog {

    private static final DateTimeFormatter DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter DATA_HORA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final String TODOS_CLIENTES = "Todos os clientes";
    private static final String TODOS_UTILIZADORES = "Todos os utilizadores";
    private static final int[] TODOS_DOCS = {VendasClientesDados.FR, VendasClientesDados.FT, VendasClientesDados.PP,
        VendasClientesDados.ND, VendasClientesDados.NC};
    private static final String[] DOCUMENTOS = {"Facturas (FR + FT)", "Facturas menos notas de crédito",
        "Só Factura/Recibo (FR)", "Só Factura (FT)", "Só notas de crédito (NC)", "Só Factura-Proforma (PP)"};
    private static final int[][] DOCS_DE = {{1, 2}, {1, 2, 5}, {1}, {2}, {5}, {3}};
    private static final String[] VISTAS = {"Por documento", "Resumo por cliente", "Por artigo (linhas)"};

    private final BDConexao conexao;
    private List<VendasClientesDados.Venda> vendas = new ArrayList<>();
    private List<VendasClientesDados.Linha> linhas;  // só lidas na vista por artigo
    private LocalDate lidoDe, lidoAte;
    private boolean aPreencher;

    private final com.toedter.calendar.JDateChooser dcDe = new com.toedter.calendar.JDateChooser();
    private final com.toedter.calendar.JDateChooser dcAte = new com.toedter.calendar.JDateChooser();
    private final JComboBox<String> cmbCliente = new JComboBox<>(new String[]{TODOS_CLIENTES});
    private final JTextField txtPesquisa = new JTextField();
    private final JComboBox<String> cmbDocumentos = new JComboBox<>(DOCUMENTOS);
    private final JComboBox<String> cmbUtilizador = new JComboBox<>(new String[]{TODOS_UTILIZADORES});
    private final JComboBox<String> cmbVista = new JComboBox<>(VISTAS);
    private final JCheckBox chkAnuladas = new JCheckBox("Incluir anuladas", false);

    private final ComponentesWeb.CartaoKpi kpiTotal = new ComponentesWeb.CartaoKpi("Kz", "Total vendido", ComponentesWeb.VERDE).compacto();
    private final ComponentesWeb.CartaoKpi kpiDocs = new ComponentesWeb.CartaoKpi("N", "Documentos", ComponentesWeb.AZUL_VIVO).compacto();
    private final ComponentesWeb.CartaoKpi kpiClientes = new ComponentesWeb.CartaoKpi(ComponentesWeb.CartaoKpi.PESSOA, "Clientes", ComponentesWeb.AZUL_VIVO).compacto();
    private final ComponentesWeb.CartaoKpi kpiMedio = new ComponentesWeb.CartaoKpi("M", "Valor médio", ComponentesWeb.AZUL_VIVO).compacto();
    private final ComponentesWeb.CartaoKpi kpiIva = new ComponentesWeb.CartaoKpi("%", "IVA", ComponentesWeb.LARANJA).compacto();
    private final ComponentesWeb.CartaoKpi kpiDesconto = new ComponentesWeb.CartaoKpi("-", "Descontos", ComponentesWeb.LARANJA).compacto();
    private final GraficoBarras grafClientes = new GraficoBarras(ComponentesWeb.AZUL_VIVO, v -> CfMethods.formatarComoMoeda(v));

    private final DefaultTableModel modelo = new DefaultTableModel() {
        @Override
        public boolean isCellEditable(int r, int c) {
            return false;
        }

        @Override
        public Class<?> getColumnClass(int c) {
            if (getRowCount() == 0) {
                return Object.class;
            }
            Object v = getValueAt(0, c);
            return v == null ? Object.class : v.getClass();
        }
    };
    private final JTable tabela = new JTable(modelo);
    private final JLabel lbContagem = FormularioWeb.dica(" ");
    private final JLabel lbEstado = FormularioWeb.dica(" ");
    private final Timer atrasoPesquisa;
    /** Vendas visíveis (para o relatório e para o duplo clique), pela ordem da tabela antes de ordenar. */
    private final List<VendasClientesDados.Venda> visiveis = new ArrayList<>();

    public static void abrir(Window dono, BDConexao conexao) {
        new VendasClientesVisao(dono, conexao).setVisible(true);
    }

    public VendasClientesVisao(Window dono, BDConexao conexao) {
        super(dono, ModalityType.MODELESS);
        this.conexao = conexao;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setTitle("Vendas por cliente");
        atrasoPesquisa = new Timer(180, e -> aplicar());
        atrasoPesquisa.setRepeats(false);
        LocalDate hoje = LocalDate.now();
        dcDe.setDate(data(hoje.withDayOfMonth(1)));
        dcAte.setDate(data(hoje));
        montar();
        FormularioWeb.ajustar(this, 1360, 860, 900, 600);
        carregar();
    }

    // =====================================================================
    // ECRÃ
    // =====================================================================
    private void montar() {
        FormularioWeb.data(dcDe);
        FormularioWeb.data(dcAte);
        dcDe.addPropertyChangeListener("date", e -> carregarSeMudou());
        dcAte.addPropertyChangeListener("date", e -> carregarSeMudou());
        JButton btActualizar = new JButton("Actualizar");
        ComponentesWeb.botao(btActualizar, ComponentesWeb.Variante.SECUNDARIO, null, "Voltar a ler da base de dados (F5)");
        btActualizar.addActionListener(e -> carregar());
        JPanel campoDe = FormularioWeb.campoCabecalho(new JLabel("De"), dcDe);
        JPanel campoAte = FormularioWeb.campoCabecalho(new JLabel("Até"), dcAte);
        campoDe.setPreferredSize(new Dimension(150, 56));
        campoAte.setPreferredSize(new Dimension(150, 56));
        JPanel direita = ComponentesWeb.transparente(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        direita.add(campoDe);
        direita.add(campoAte);
        direita.add(btActualizar);
        JPanel cabecalho = FormularioWeb.cabecalho("Vendas por cliente",
                "Todas as vendas ou de um só cliente, num intervalo de datas", direita);

        // ---- períodos rápidos
        LocalDate hoje = LocalDate.now();
        JToggleButton pHoje = periodo("Hoje", hoje, hoje);
        JToggleButton pOntem = periodo("Ontem", hoje.minusDays(1), hoje.minusDays(1));
        JToggleButton pSemana = periodo("Esta semana", hoje.with(DayOfWeek.MONDAY), hoje);
        JToggleButton pMes = periodo("Este mês", hoje.withDayOfMonth(1), hoje);
        LocalDate mesPassado = hoje.minusMonths(1);
        JToggleButton pMesPassado = periodo("Mês passado", mesPassado.withDayOfMonth(1), mesPassado.withDayOfMonth(mesPassado.lengthOfMonth()));
        JToggleButton pAno = periodo("Este ano", hoje.withDayOfYear(1), hoje);
        ButtonGroup g = new ButtonGroup();
        for (JToggleButton b : new JToggleButton[]{pHoje, pOntem, pSemana, pMes, pMesPassado, pAno}) {
            g.add(b);
        }
        pMes.setSelected(true);
        JPanel periodos = FormularioWeb.pilulas(pHoje, pOntem, pSemana, pMes, pMesPassado, pAno);
        periodos.setLayout(new EstiloUI.WrapLayout(FlowLayout.LEFT, 6, 2));

        // ---- filtros
        FormularioWeb.campoTexto(txtPesquisa);
        FormularioWeb.combo(cmbCliente, cmbDocumentos, cmbUtilizador, cmbVista);
        txtPesquisa.setToolTipText("Nome do cliente, NIF ou número do documento (ex.: FR 2026/15910)");
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
        cmbCliente.setMaximumRowCount(20);
        cmbCliente.addActionListener(e -> aplicar());
        cmbDocumentos.addActionListener(e -> aplicar());
        cmbUtilizador.addActionListener(e -> aplicar());
        cmbVista.addActionListener(e -> mudarVista());
        chkAnuladas.setOpaque(false);
        chkAnuladas.setFont(EstiloUI.FONTE);
        chkAnuladas.addActionListener(e -> aplicar());
        JPanel filtros = FormularioWeb.grelha(200, 6,
                ComponentesWeb.campo("Cliente", cmbCliente),
                ComponentesWeb.campo("Pesquisar (cliente, NIF, documento)", txtPesquisa),
                ComponentesWeb.campo("Documentos", cmbDocumentos),
                ComponentesWeb.campo("Utilizador", cmbUtilizador),
                ComponentesWeb.campo("Ver", cmbVista),
                ComponentesWeb.campo(" ", chkAnuladas));
        ComponentesWeb.Cartao cartaoFiltros = FormularioWeb.cartao("Parâmetros", ComponentesWeb.AZUL_VIVO,
                FormularioWeb.pilha(10, periodos, filtros));

        // ---- indicadores e top de clientes
        for (ComponentesWeb.CartaoKpi k : new ComponentesWeb.CartaoKpi[]{kpiTotal, kpiDocs, kpiClientes, kpiMedio, kpiIva, kpiDesconto}) {
            k.setValor("—", " ");
        }
        JPanel kpis = FormularioWeb.grelha(170, 6, kpiTotal, kpiDocs, kpiClientes, kpiMedio, kpiIva, kpiDesconto);
        grafClientes.setAoClicar(b -> {
            if (b.chave != null) {
                Object actual = cmbCliente.getSelectedItem();
                cmbCliente.setSelectedItem(b.chave.equals(actual) ? TODOS_CLIENTES : b.chave);
            }
        });
        ComponentesWeb.Cartao cartaoTop = FormularioWeb.cartao("Clientes que mais compraram", ComponentesWeb.AZUL_VIVO,
                FormularioWeb.pilha(6, grafClientes, FormularioWeb.dica("Clique num cliente para ver só as vendas dele.")));

        // ---- tabela
        EstiloUI.estilizarTabela(tabela, true);
        tabela.setRowSorter(new TableRowSorter<>(modelo));
        tabela.setDefaultRenderer(BigDecimal.class, new RendererMoeda());
        tabela.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    focarClienteDaLinha();
                }
            }
        });
        JScrollPane rol = new JScrollPane(tabela);
        rol.setBorder(BorderFactory.createLineBorder(ComponentesWeb.LINHA));
        rol.setPreferredSize(new Dimension(900, 340));
        rol.setMinimumSize(new Dimension(300, 160));

        JButton btRelatorio = new JButton("Relatório (iReport)");
        JButton btCsv = new JButton("Exportar (Excel/CSV)");
        ComponentesWeb.botao(btRelatorio, ComponentesWeb.Variante.PRIMARIO, null,
                "Relatório em A4 paisagem com a vista e os filtros escolhidos");
        ComponentesWeb.botao(btCsv, ComponentesWeb.Variante.SECUNDARIO, null, "Grava a tabela para abrir no Excel");
        btRelatorio.addActionListener(e -> relatorio());
        btCsv.addActionListener(e -> exportarCsv());
        JPanel accoesTabela = ComponentesWeb.transparente(new EstiloUI.WrapLayout(FlowLayout.LEFT, 8, 0));
        accoesTabela.add(btRelatorio);
        accoesTabela.add(btCsv);
        accoesTabela.add(lbContagem);
        JPanel corpoTabela = ComponentesWeb.transparente(new BorderLayout(0, 8));
        corpoTabela.add(accoesTabela, BorderLayout.NORTH);
        corpoTabela.add(rol, BorderLayout.CENTER);
        corpoTabela.add(FormularioWeb.dica("Duplo clique numa linha: mostra só as vendas desse cliente. "
                + "Notas de crédito entram com valor negativo."), BorderLayout.SOUTH);
        ComponentesWeb.Cartao cartaoTabela = FormularioWeb.cartao("Vendas", ComponentesWeb.AZUL_VIVO, corpoTabela);

        JPanel conteudo = FormularioWeb.pilha(10, cartaoFiltros, kpis, cartaoTop, cartaoTabela);
        JButton btFechar = new JButton("Fechar");
        FormularioWeb.botaoSair(btFechar, "Fechar (Esc)");
        btFechar.addActionListener(e -> dispose());
        FormularioWeb.montar(this, cabecalho, conteudo, FormularioWeb.barraAccoes(lbEstado, btFechar), "Vendas por cliente");
        JComponent raiz = getRootPane();
        raiz.registerKeyboardAction(e -> dispose(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
        raiz.registerKeyboardAction(e -> carregar(), KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
        raiz.registerKeyboardAction(e -> txtPesquisa.requestFocusInWindow(),
                KeyStroke.getKeyStroke(KeyEvent.VK_F, java.awt.event.InputEvent.CTRL_DOWN_MASK), JComponent.WHEN_IN_FOCUSED_WINDOW);
    }

    private JToggleButton periodo(String texto, LocalDate de, LocalDate ate) {
        JToggleButton b = new JToggleButton(texto);
        b.addActionListener(e -> {
            aPreencher = true;
            dcDe.setDate(data(de));
            dcAte.setDate(data(ate));
            aPreencher = false;
            carregar();
        });
        return b;
    }

    private static Date data(LocalDate d) {
        return Date.from(d.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    private static LocalDate local(Date d, LocalDate omissao) {
        return d == null ? omissao : d.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    private LocalDate de() {
        return local(dcDe.getDate(), LocalDate.now().withDayOfMonth(1));
    }

    private LocalDate ate() {
        return local(dcAte.getDate(), LocalDate.now());
    }

    // =====================================================================
    // DADOS
    // =====================================================================
    private void carregarSeMudou() {
        if (!aPreencher && (!de().equals(lidoDe) || !ate().equals(lidoAte))) {
            carregar();
        }
    }

    private void carregar() {
        final LocalDate de = de(), ate = ate();
        if (ate.isBefore(de)) {
            lbEstado.setText("A data final é anterior à inicial.");
            return;
        }
        final boolean comLinhas = cmbVista.getSelectedIndex() == 2;
        lbEstado.setText("A carregar…");
        setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.WAIT_CURSOR));
        new SwingWorker<Object[], Void>() {
            @Override
            protected Object[] doInBackground() throws Exception {
                return new Object[]{VendasClientesDados.vendas(de, ate, TODOS_DOCS),
                    comLinhas ? VendasClientesDados.linhas(de, ate, TODOS_DOCS) : null};
            }

            @Override
            @SuppressWarnings("unchecked")
            protected void done() {
                setCursor(java.awt.Cursor.getDefaultCursor());
                try {
                    Object[] r = get();
                    vendas = (List<VendasClientesDados.Venda>) r[0];
                    linhas = (List<VendasClientesDados.Linha>) r[1];
                    lidoDe = de;
                    lidoAte = ate;
                    lbEstado.setText(vendas.size() + " documento(s) de " + de.format(DATA) + " a " + ate.format(DATA)
                            + "  ·  F5 actualiza, Ctrl+F pesquisa");
                    preencherCombos();
                    aplicar();
                } catch (Exception e) {
                    lbEstado.setText("Erro: " + e.getMessage());
                    JOptionPane.showMessageDialog(VendasClientesVisao.this, "Erro ao ler as vendas:\n" + e.getMessage());
                }
            }
        }.execute();
    }

    private void mudarVista() {
        if (cmbVista.getSelectedIndex() == 2 && linhas == null && lidoDe != null) {
            carregar(); // a vista por artigo precisa das linhas
        } else {
            aplicar();
        }
    }

    private void preencherCombos() {
        Object cli = cmbCliente.getSelectedItem(), uti = cmbUtilizador.getSelectedItem();
        TreeSet<String> clientes = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        TreeSet<String> utilizadores = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        for (VendasClientesDados.Venda v : vendas) {
            clientes.add(v.cliente);
            if (!v.utilizador.isEmpty()) {
                utilizadores.add(v.utilizador);
            }
        }
        aPreencher = true;
        List<String> lc = new ArrayList<>();
        lc.add(TODOS_CLIENTES);
        lc.addAll(clientes);
        cmbCliente.setModel(new DefaultComboBoxModel<>(lc.toArray(new String[0])));
        cmbCliente.setSelectedItem(cli != null && clientes.contains(String.valueOf(cli)) ? cli : TODOS_CLIENTES);
        List<String> lu = new ArrayList<>();
        lu.add(TODOS_UTILIZADORES);
        lu.addAll(utilizadores);
        cmbUtilizador.setModel(new DefaultComboBoxModel<>(lu.toArray(new String[0])));
        cmbUtilizador.setSelectedItem(uti != null && utilizadores.contains(String.valueOf(uti)) ? uti : TODOS_UTILIZADORES);
        aPreencher = false;
    }

    // =====================================================================
    // FILTROS
    // =====================================================================
    private static String chave(String s) {
        return Normalizer.normalize(s == null ? "" : s, Normalizer.Form.NFD).replaceAll("\\p{M}", "").toLowerCase();
    }

    private List<VendasClientesDados.Venda> filtradas(boolean comCliente) {
        Set<Integer> docs = new HashSet<>();
        for (int d : DOCS_DE[cmbDocumentos.getSelectedIndex()]) {
            docs.add(d);
        }
        String cliente = String.valueOf(cmbCliente.getSelectedItem());
        String utilizador = String.valueOf(cmbUtilizador.getSelectedItem());
        String q = chave(txtPesquisa.getText().trim());
        List<VendasClientesDados.Venda> r = new ArrayList<>();
        for (VendasClientesDados.Venda v : vendas) {
            if (!docs.contains(v.documento) || (v.anulada && !chkAnuladas.isSelected())) {
                continue;
            }
            if (comCliente && !TODOS_CLIENTES.equals(cliente) && !cliente.equals(v.cliente)) {
                continue;
            }
            if (!TODOS_UTILIZADORES.equals(utilizador) && !utilizador.equals(v.utilizador)) {
                continue;
            }
            if (!q.isEmpty() && !chave(v.cliente).contains(q) && !v.nif.toLowerCase().contains(q)
                    && !chave(v.numero).contains(q)) {
                continue;
            }
            r.add(v);
        }
        return r;
    }

    private void aplicar() {
        if (aPreencher) {
            return;
        }
        List<VendasClientesDados.Venda> lista = filtradas(true);
        visiveis.clear();
        visiveis.addAll(lista);
        switch (cmbVista.getSelectedIndex()) {
            case 1:
                tabelaResumo(lista);
                break;
            case 2:
                tabelaArtigos(lista);
                break;
            default:
                tabelaDocumentos(lista);
        }
        indicadores(lista);
        topClientes();
        avisarInconsistentes(lista);
    }

    /**
     * Documentos com IVA maior do que o total (dados gravados com erro, p.ex.
     * um código de barras no campo da quantidade): os valores entram como estão
     * gravados, mas o utilizador é avisado para os conferir.
     */
    private void avisarInconsistentes(List<VendasClientesDados.Venda> lista) {
        List<String> maus = new ArrayList<>();
        for (VendasClientesDados.Venda v : lista) {
            if (v.total.signum() != 0 && v.iva.abs().compareTo(v.total.abs()) > 0) {
                maus.add(v.numero.isEmpty() ? "venda " + v.codigo : v.numero);
            }
        }
        if (maus.isEmpty()) {
            lbEstado.setForeground(ComponentesWeb.TEXTO_SUAVE);
            lbEstado.setToolTipText(null);
            if (lbEstado.getText().startsWith("Atenção")) {
                lbEstado.setText(" ");
            }
            return;
        }
        lbEstado.setForeground(ComponentesWeb.VERMELHO);
        lbEstado.setText("Atenção: " + maus.size() + " documento(s) com IVA maior do que o total (dados gravados com erro): "
                + String.join(", ", maus.subList(0, Math.min(4, maus.size()))) + (maus.size() > 4 ? "…" : ""));
        lbEstado.setToolTipText("<html>Estes documentos têm o IVA gravado maior do que o valor pago.<br>"
                + "O total está certo, mas o IVA e a incidência destes documentos não.<br>" + String.join(", ", maus) + "</html>");
    }

    private void tabelaDocumentos(List<VendasClientesDados.Venda> lista) {
        modelo.setRowCount(0);
        modelo.setColumnIdentifiers(new Object[]{"Data", "Documento", "Cliente", "NIF", "Utilizador", "Itens",
            "Desconto", "IVA", "Total", "Estado"});
        for (VendasClientesDados.Venda v : lista) {
            modelo.addRow(new Object[]{v.data == null ? "" : v.data.format(DATA_HORA), v.numero, v.cliente, v.nif,
                v.utilizador, v.itens, v.desconto, v.iva, v.total, v.anulada ? "Anulada" : "Normal"});
        }
        larguras(120, 130, 240, 100, 150, 50, 90, 90, 110, 70);
        lbContagem.setText(lista.size() + " documento(s)");
    }

    private void tabelaResumo(List<VendasClientesDados.Venda> lista) {
        List<VendasClientesMapa.Resumo> resumo = resumo(lista);
        modelo.setRowCount(0);
        modelo.setColumnIdentifiers(new Object[]{"Cliente", "NIF", "Documentos", "Primeira", "Última", "Desconto",
            "IVA", "Total", "% do total"});
        for (VendasClientesMapa.Resumo r : resumo) {
            modelo.addRow(new Object[]{r.cliente, r.nif, r.documentos, r.primeira, r.ultima, r.desconto, r.iva, r.total,
                BigDecimal.valueOf(r.percentagem).setScale(1, RoundingMode.HALF_UP)});
        }
        larguras(280, 110, 80, 90, 90, 100, 100, 120, 80);
        lbContagem.setText(resumo.size() + " cliente(s)");
    }

    private void tabelaArtigos(List<VendasClientesDados.Venda> lista) {
        modelo.setRowCount(0);
        modelo.setColumnIdentifiers(new Object[]{"Data", "Documento", "Cliente", "Artigo", "Qtd.", "Preço", "Desconto",
            "IVA %", "Total"});
        int n = 0;
        if (linhas != null) {
            Map<Integer, VendasClientesDados.Venda> porCodigo = new HashMap<>();
            for (VendasClientesDados.Venda v : lista) {
                porCodigo.put(v.codigo, v);
            }
            for (VendasClientesDados.Linha l : linhas) {
                VendasClientesDados.Venda v = porCodigo.get(l.venda);
                if (v == null) {
                    continue;
                }
                modelo.addRow(new Object[]{v.data == null ? "" : v.data.format(DATA), v.numero, v.cliente, l.artigo,
                    BigDecimal.valueOf(l.quantidade).stripTrailingZeros(), l.preco, l.desconto,
                    BigDecimal.valueOf(l.ivaPercentagem).stripTrailingZeros(), l.total});
                n++;
            }
        }
        larguras(90, 130, 200, 320, 60, 90, 80, 60, 110);
        lbContagem.setText(n + " linha(s) de artigos");
    }

    private void larguras(int... w) {
        for (int i = 0; i < w.length && i < tabela.getColumnCount(); i++) {
            tabela.getColumnModel().getColumn(i).setPreferredWidth(w[i]);
        }
    }

    private List<VendasClientesMapa.Resumo> resumo(List<VendasClientesDados.Venda> lista) {
        Map<String, VendasClientesMapa.Resumo> m = new LinkedHashMap<>();
        Map<String, LocalDate[]> datas = new HashMap<>();
        BigDecimal total = BigDecimal.ZERO;
        for (VendasClientesDados.Venda v : lista) {
            VendasClientesMapa.Resumo r = m.computeIfAbsent(v.cliente, k -> {
                VendasClientesMapa.Resumo n = new VendasClientesMapa.Resumo();
                n.cliente = v.cliente;
                n.nif = v.nif;
                return n;
            });
            r.documentos++;
            r.desconto = r.desconto.add(v.desconto);
            r.iva = r.iva.add(v.iva);
            r.total = r.total.add(v.total);
            total = total.add(v.total);
            if (v.data != null) {
                LocalDate d = v.data.toLocalDate();
                LocalDate[] pu = datas.computeIfAbsent(v.cliente, k -> new LocalDate[]{d, d});
                if (d.isBefore(pu[0])) {
                    pu[0] = d;
                }
                if (d.isAfter(pu[1])) {
                    pu[1] = d;
                }
            }
        }
        List<VendasClientesMapa.Resumo> r = new ArrayList<>(m.values());
        for (VendasClientesMapa.Resumo x : r) {
            LocalDate[] pu = datas.get(x.cliente);
            x.primeira = pu == null ? "" : pu[0].format(DATA);
            x.ultima = pu == null ? "" : pu[1].format(DATA);
            x.percentagem = total.signum() == 0 ? 0 : x.total.multiply(BigDecimal.valueOf(100))
                    .divide(total, 4, RoundingMode.HALF_UP).doubleValue();
        }
        r.sort(Comparator.comparing((VendasClientesMapa.Resumo x) -> x.total).reversed());
        return r;
    }

    private void indicadores(List<VendasClientesDados.Venda> lista) {
        BigDecimal total = BigDecimal.ZERO, iva = BigDecimal.ZERO, desconto = BigDecimal.ZERO;
        Set<String> clientes = new HashSet<>();
        for (VendasClientesDados.Venda v : lista) {
            total = total.add(v.total);
            iva = iva.add(v.iva);
            desconto = desconto.add(v.desconto);
            clientes.add(v.cliente);
        }
        kpiTotal.setValor(CfMethods.formatarComoMoeda(total.doubleValue()), de().format(DATA) + " a " + ate().format(DATA));
        kpiDocs.setValor(String.valueOf(lista.size()), String.valueOf(cmbDocumentos.getSelectedItem()));
        kpiClientes.setValor(String.valueOf(clientes.size()), clientes.size() == 1 ? "cliente" : "clientes");
        kpiMedio.setValor(lista.isEmpty() ? "—" : CfMethods.formatarComoMoeda(
                total.divide(BigDecimal.valueOf(lista.size()), 2, RoundingMode.HALF_UP).doubleValue()), "por documento");
        kpiIva.setValor(CfMethods.formatarComoMoeda(iva.doubleValue()), "liquidado");
        kpiDesconto.setValor(CfMethods.formatarComoMoeda(desconto.doubleValue()), "concedidos");
    }

    /** Top 10 (todos os clientes, com os outros filtros): clicar escolhe o cliente. */
    private void topClientes() {
        List<VendasClientesMapa.Resumo> r = resumo(filtradas(false));
        List<GraficoBarras.Barra> barras = new ArrayList<>();
        BigDecimal outros = BigDecimal.ZERO;
        int nOutros = 0;
        for (int i = 0; i < r.size(); i++) {
            VendasClientesMapa.Resumo x = r.get(i);
            if (i < 10) {
                barras.add(new GraficoBarras.Barra(x.cliente, x.total.doubleValue(),
                        x.documentos + " documento(s) · " + BigDecimal.valueOf(x.percentagem).setScale(1, RoundingMode.HALF_UP) + "% do total",
                        x.cliente));
            } else {
                outros = outros.add(x.total);
                nOutros++;
            }
        }
        if (nOutros > 0) {
            barras.add(new GraficoBarras.Barra("Outros (" + nOutros + ")", outros.doubleValue(), null, null));
        }
        grafClientes.setBarras(barras);
        Object sel = cmbCliente.getSelectedItem();
        grafClientes.setSeleccionada(TODOS_CLIENTES.equals(sel) ? null : sel);
    }

    private void focarClienteDaLinha() {
        int r = tabela.getSelectedRow();
        if (r < 0) {
            return;
        }
        int col = cmbVista.getSelectedIndex() == 1 ? 0 : 2;
        Object cliente = modelo.getValueAt(tabela.convertRowIndexToModel(r), col);
        if (cliente != null) {
            cmbCliente.setSelectedItem(String.valueOf(cliente));
        }
    }

    // =====================================================================
    // RELATÓRIO (iReport) E EXPORTAÇÃO
    // =====================================================================
    private String descricaoFiltros() {
        StringBuilder f = new StringBuilder(String.valueOf(cmbCliente.getSelectedItem()));
        f.append(" · ").append(cmbDocumentos.getSelectedItem());
        if (!TODOS_UTILIZADORES.equals(cmbUtilizador.getSelectedItem())) {
            f.append(" · utilizador: ").append(cmbUtilizador.getSelectedItem());
        }
        if (!txtPesquisa.getText().trim().isEmpty()) {
            f.append(" · \"").append(txtPesquisa.getText().trim()).append('"');
        }
        if (chkAnuladas.isSelected()) {
            f.append(" · inclui anuladas");
        }
        return f.toString();
    }

    private void relatorio() {
        if (visiveis.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Não há vendas para os filtros escolhidos.");
            return;
        }
        String periodo = "De " + de().format(DATA) + " a " + ate().format(DATA);
        Map<String, Object> p = RelatorioJasper.parametros(conexao, periodo, descricaoFiltros());
        // agrupado por cliente: ordena por cliente e depois por data
        List<VendasClientesDados.Venda> ord = new ArrayList<>(visiveis);
        ord.sort(Comparator.comparing((VendasClientesDados.Venda v) -> chave(v.cliente))
                .thenComparing(v -> v.data == null ? java.time.LocalDateTime.MIN : v.data));
        switch (cmbVista.getSelectedIndex()) {
            case 1:
                RelatorioJasper.mostrar("vendasClientesResumo", p, resumo(visiveis), "Resumo de vendas por cliente");
                break;
            case 2: {
                List<VendasClientesMapa.Artigo> l = new ArrayList<>();
                Map<Integer, List<VendasClientesDados.Linha>> porVenda = new HashMap<>();
                if (linhas != null) {
                    for (VendasClientesDados.Linha x : linhas) {
                        porVenda.computeIfAbsent(x.venda, k -> new ArrayList<>()).add(x);
                    }
                }
                for (VendasClientesDados.Venda v : ord) {
                    for (VendasClientesDados.Linha x : porVenda.getOrDefault(v.codigo, new ArrayList<>())) {
                        VendasClientesMapa.Artigo a = new VendasClientesMapa.Artigo();
                        a.cliente = v.cliente;
                        a.nif = v.nif;
                        a.data = v.data == null ? "" : v.data.format(DATA);
                        a.documento = v.numero;
                        a.artigo = x.artigo;
                        a.quantidade = x.quantidade;
                        a.ivaPercentagem = x.ivaPercentagem;
                        a.preco = x.preco;
                        a.desconto = x.desconto;
                        a.total = x.total;
                        l.add(a);
                    }
                }
                RelatorioJasper.mostrar("vendasClientesArtigos", p, l, "Vendas por cliente — artigos");
                break;
            }
            default: {
                List<VendasClientesMapa.Documento> l = new ArrayList<>();
                for (VendasClientesDados.Venda v : ord) {
                    VendasClientesMapa.Documento d = new VendasClientesMapa.Documento();
                    d.cliente = v.cliente;
                    d.nif = v.nif;
                    d.data = v.data == null ? "" : v.data.format(DATA_HORA);
                    d.documento = v.numero;
                    d.utilizador = v.utilizador;
                    d.itens = v.itens;
                    d.desconto = v.desconto;
                    d.iva = v.iva;
                    d.total = v.total;
                    d.estado = v.anulada ? "Anulada" : "Normal";
                    l.add(d);
                }
                RelatorioJasper.mostrar("vendasClientes", p, l, "Vendas por cliente");
            }
        }
    }

    private void exportarCsv() {
        JFileChooser fc = new JFileChooser();
        fc.setSelectedFile(new File("vendas_clientes_" + de().format(DateTimeFormatter.BASIC_ISO_DATE) + "_"
                + ate().format(DateTimeFormatter.BASIC_ISO_DATE) + ".csv"));
        if (fc.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }
        File f = fc.getSelectedFile();
        if (!f.getName().toLowerCase().endsWith(".csv")) {
            f = new File(f.getParentFile(), f.getName() + ".csv");
        }
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
            JOptionPane.showMessageDialog(this, "Tabela gravada em:\n" + f.getAbsolutePath());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Não foi possível gravar: " + e.getMessage());
        }
    }

    private static final class RendererMoeda extends EstiloUI.ZebraRenderer {

        RendererMoeda() {
            super(SwingConstants.RIGHT);
        }

        @Override
        public Component getTableCellRendererComponent(JTable t, Object v, boolean sel, boolean foco, int r, int c) {
            Object texto = v;
            if (v instanceof BigDecimal) {
                String nome = t.getColumnName(c);
                boolean dinheiro = !(nome.startsWith("Qtd") || nome.startsWith("IVA %") || nome.startsWith("%"));
                texto = dinheiro ? CfMethods.formatarComoMoeda(((BigDecimal) v).doubleValue())
                        : ((BigDecimal) v).toPlainString().replace('.', ',');
            }
            Component comp = super.getTableCellRendererComponent(t, texto, sel, foco, r, c);
            if (!sel && v instanceof BigDecimal && ((BigDecimal) v).signum() < 0) {
                comp.setForeground(ComponentesWeb.VERMELHO);
            }
            return comp;
        }
    }
}
