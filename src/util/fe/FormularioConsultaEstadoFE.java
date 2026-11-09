/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util.fe;

import com.toedter.calendar.JDateChooser;
import dao.VendaDao;
import entity.TbDadosInstituicao;
import entity.TbVenda;
import comercial.controller.DadosInstituicaoController; // <-- ajusta o import ao teu pacote real
import comercial.controller.VendasController;           // <-- ajusta o import ao teu pacote real
import entity.Documento;
import entity.TbCliente;
import entity.TbItemVenda;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import lista.ListaNotasDebito;
import lista.ListaVenda2;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;
import util.BDConexao; // <-- ajusta o import ao teu pacote real
import util.DVML;
import util.DVML.Abreviacao;
import util.JPAEntityMannagerFactoryUtil;
import util.MetodosUtil;
import util.fe.dto.ConsultaEstadoDTO;
import util.fe.dto.DocumentoHistoricoDTO;
import util.fe.dto.FacturaRemotaDTO;
import util.fe.payloads.PayloadFactory;

/**
 * Formulário para consultar o estado das facturas na AGT e actualizar o estado
 * local (Pendente / Validado / Inválido).
 *
 * Uso:
 * new FormularioConsultaEstadoFE(conexao).setVisible(true);
 *
 * Requer FEConfig.isHomologacao() e VendasController.findCodigosVendasFE(...).
 *
 * @author Engº Domingos Dala Vunge
 */
public class FormularioConsultaEstadoFE extends JFrame {

    private static final int COL_REQUEST_ID = 5;
    private static final int COL_ESTADO = 6;
    private static final int COL_AGT_ACEITACAO = 6;
    private static final int COL_AGT_EXISTE_LOCAL = 11;
    private static final int COL_AGT_ESTADO = 12;

    private final BDConexao conexao;
    private final VendasController vendasController;
    private final DadosInstituicaoController dadosInstituicaoController;
    private final comercial.controller.ItemVendasController itensController;
    private final comercial.controller.ClientesController clientesController;
    private final comercial.controller.DocumentosController documentosController;
    private final VendaDao vendaDao = new VendaDao(JPAEntityMannagerFactoryUtil.em);

    private final List<TbVenda> vendas = new ArrayList<>();

    private final JComboBox<String> cbFiltro = new JComboBox<>(
            new String[]{"Todas", "Pendentes", "Validadas", "Inválidas"}); // "Todas" é a opção por defeito
    private boolean ocupado = false;
    private boolean recarregarPendente = false; // data alterada enquanto outra operação decorria
    private final JCheckBox chkDatas = new JCheckBox("Filtrar por datas", true);
    private final JDateChooser dcDataInicio = criarDateChooser(-30); // por defeito: últimos 30 dias
    private final JDateChooser dcDataFim = criarDateChooser(0);      // até hoje
    private final JButton btnSair = new JButton("Sair");
    private final JButton btnPesquisar = new JButton("Pesquisar");
    private final JButton btnConsultar = new JButton("Consultar seleccionada");
    private final JButton btnActualizarTodas = new JButton("Actualizar todas as pendentes");
    private final JButton btnImprimir = new JButton("Imprimir");
    // Período com que a tabela local foi carregada (null = sem filtro de datas)
    private Date localPesquisaInicio;
    private Date localPesquisaFim;
    private final JProgressBar barra = new JProgressBar(0, 100);
    private final JTextArea txtLog = new JTextArea(7, 40);
    private final JLabel lblResumo = new JLabel(" ");

    private final DefaultTableModel model = new DefaultTableModel(
            new Object[]{"Código", "Nº Factura", "NIF Cliente", "Data", "Total", "RequestID", "Estado"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable tabela = new TabelaFE(model);

    // ---- Facturas no portal da AGT (listarFacturas)
    private final JButton btnPesquisarAGT = new JButton("Pesquisar na AGT");
    private final JButton btnEnviarPendentes = new JButton("Enviar para AGT");
    private final JButton btnComunicarEmFalta = new JButton("Comunicar facturas em falta");
    private final List<FacturaRemotaDTO> facturasAGT = new ArrayList<>();
    // Última pesquisa na AGT: período usado e se a lista veio completa (sem erro).
    // O envio usa SEMPRE este período, para comparar o mesmo intervalo dos dois lados.
    private Date agtPesquisaInicio;
    private Date agtPesquisaFim;
    private boolean agtListaCompleta = false;
    // Nºs já enviados nesta janela (a lista da AGT só se actualiza com nova pesquisa)
    private final java.util.Set<String> enviadasNestaSessao = new java.util.HashSet<>();
    private final DefaultTableModel modelAGT = new DefaultTableModel(
            new Object[]{"Nº Documento", "NIF Cliente", "Tipo", "Data", "Data Registo", "Estado Doc.",
                "Aceitação Cliente", "Total Líquido", "Imposto", "Total", "Ref./Pag.",
                "Existe na BD local?", "Estado"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable tabelaAGT = new TabelaFE(modelAGT);
    private final JTabbedPane abas = new JTabbedPane();

    // ---- Modo de consulta
    private static final int ABA_INDIVIDUAL = 2;
    private final JRadioButton rbEstadoFacturas = new JRadioButton("Consulta Estado das Facturas", true);
    private final JRadioButton rbIndividualLocal = new JRadioButton("Consulta Individual Local");
    private final JRadioButton rbIndividualAGT = new JRadioButton("Consulta Individual AGT");

    // ---- Consulta individual (histórico de um cliente pelo NIF)
    private final JTextField txtNif = new JTextField(12);
    private final JTextField txtNome = new JTextField(18);
    private final JLabel lblContribuinte = new JLabel(" ");
    private final JComboBox<String> cbTipoDocumento = new JComboBox<>(new String[]{
        "Todos Documentos",
        DocumentoHistoricoDTO.FACTURA,
        DocumentoHistoricoDTO.FACTURA_RECIBO,
        DocumentoHistoricoDTO.RECIBO,
        DocumentoHistoricoDTO.NOTA_CREDITO});
    private final JButton btnVisualizar = new JButton("Visualizar Histórico do Intervalo de Data");
    private final JCheckBox chkSoPendentes = new JCheckBox("Mostrar todos no estado Pendente");
    private final JButton btnActualizarEstado = new JButton("Actualizar Estado");
    private final JLabel lblTotalIndividual = new JLabel(" ");
    private final List<DocumentoHistoricoDTO> historico = new ArrayList<>();         // tudo o que veio da consulta
    private final List<DocumentoHistoricoDTO> historicoVisivel = new ArrayList<>();  // filtrado pelo tipo
    private String historicoNif;      // NIF da última consulta individual
    private String historicoNome;     // nome do cliente (BD local ou AGT)
    private String historicoMorada;
    private String historicoOrigem;   // "Base de dados local" / "Portal AGT ..." (null = sem consulta)
    private boolean historicoDaAGT;
    // AGT: pendentes da BD local que ainda não constam da listagem da AGT
    private final List<DocumentoHistoricoDTO> historicoPendentesLocais = new ArrayList<>();
    private Date historicoInicio;
    private Date historicoFim;
    private final DefaultTableModel modelIndividual = new DefaultTableModel(
            new Object[]{"Nº Documento", "NIF Cliente", "Tipo Documento", "Data", "Estado", "Total Geral"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable tabelaIndividual = new TabelaFE(modelIndividual);

    /**
     * Construtor simples, no estilo do FormularioSeries: cria os controllers
     * a partir da conexão.
     */
    public FormularioConsultaEstadoFE(BDConexao conexao) {
        this(conexao,
                new VendasController(conexao),
                new DadosInstituicaoController(conexao));
    }

    public FormularioConsultaEstadoFE(BDConexao conexao,
            VendasController vendasController,
            DadosInstituicaoController dadosInstituicaoController) {

        this.conexao = conexao;
        this.vendasController = vendasController;
        this.dadosInstituicaoController = dadosInstituicaoController;
        this.itensController = new comercial.controller.ItemVendasController(conexao);
        this.clientesController = new comercial.controller.ClientesController(conexao);
        this.documentosController = new comercial.controller.DocumentosController(conexao);

        montarInterface();
        carregar();
    }

    // =====================================================
    // INTERFACE
    // =====================================================
    // ---- Aparência
    private static final Font FONTE = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font FONTE_NEGRITO = FONTE.deriveFont(Font.BOLD);
    private static final Color AZUL_ESCURO = new Color(21, 67, 120);
    private static final Color AZUL_CABECALHO = new Color(226, 235, 247);

    private void montarInterface() {
        setTitle("Facturação Electrónica - Consultar Estado das Facturas ["
                + (FEConfig.isHomologacao() ? "HOMOLOGAÇÃO" : "PRODUÇÃO") + "]");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        ajustarAoEcra();
        javax.swing.ImageIcon iconeJanela = icone("/imagens/REPORTE.png", 32);
        if (iconeJanela != null) {
            setIconImage(iconeJanela.getImage());
        }

        // ---------------- Tipo de consulta
        ButtonGroup grupoModo = new ButtonGroup();
        JPanel modos = painelTitulado("Tipo de Consulta", new WrapLayout(FlowLayout.LEFT, 24, 4));
        for (JRadioButton rb : new JRadioButton[]{rbEstadoFacturas, rbIndividualLocal, rbIndividualAGT}) {
            grupoModo.add(rb);
            modos.add(rb);
            rb.setFont(FONTE_NEGRITO.deriveFont(15f));
            rb.setForeground(AZUL_ESCURO);
            rb.setFocusPainted(false);
            rb.addActionListener(e -> aplicarModo());
        }

        // ---------------- Período e estado
        JPanel filtros = painelTitulado("Período e Estado", new WrapLayout(FlowLayout.LEFT, 10, 4));
        filtros.add(rotulo("Mostrar:"));
        filtros.add(cbFiltro);
        filtros.add(chkDatas);
        filtros.add(rotulo("De:"));
        filtros.add(dcDataInicio);
        filtros.add(rotulo("Até:"));
        filtros.add(dcDataFim);

        // ---------------- Cliente / empresa (consulta individual)
        JPanel individual = painelTitulado("Cliente / Empresa (Consulta Individual)",
                new WrapLayout(FlowLayout.LEFT, 10, 4));
        individual.add(rotulo("NIF:"));
        individual.add(txtNif);
        individual.add(rotulo("ou Nome:"));
        individual.add(txtNome);
        individual.add(rotulo("Documentos:"));
        individual.add(cbTipoDocumento);
        individual.add(chkSoPendentes);
        individual.add(lblContribuinte);
        chkSoPendentes.setToolTipText("Consulta Individual AGT: mostra só os documentos pendentes do cliente no período");
        txtNif.setToolTipText("Escreva o NIF e carregue em Enter");
        txtNome.setToolTipText("Nome (ou parte do nome) do cliente/empresa; carregue em Enter. "
                + "Local: pesquisa na BD local; AGT: pesquisa no portal da AGT");
        lblContribuinte.setFont(FONTE_NEGRITO.deriveFont(15f));
        lblContribuinte.setForeground(AZUL_ESCURO);


        // ---------------- Acções
        JPanel accoes = painelTitulado("Acções", new WrapLayout(FlowLayout.LEFT, 10, 4));
        accoes.add(btnPesquisar);
        accoes.add(btnPesquisarAGT);
        accoes.add(btnEnviarPendentes);
        accoes.add(btnComunicarEmFalta);
        accoes.add(btnConsultar);
        accoes.add(btnActualizarTodas);
        accoes.add(btnVisualizar);
        accoes.add(btnActualizarEstado);
        accoes.add(btnImprimir);
        btnActualizarEstado.setToolTipText("Consulta na AGT o estado de cada documento pendente e grava-o na base de dados local");
        btnActualizarEstado.setVisible(false); // aparece ao marcar "Mostrar todos no estado Pendente"
        btnEnviarPendentes.setToolTipText("Enviar para a AGT as facturas do banco local que ainda não constam no portal");

        estilizarBotao(btnPesquisar, "/imagens/proucura.png", "Pesquisar na base de dados local (ou consulta individual)");
        estilizarBotao(btnPesquisarAGT, "/imagem/find/seach_32x32.png", "Listar as facturas registadas no portal da AGT");
        estilizarBotao(btnEnviarPendentes, "/imagens/actualizar_1_32x32.png", "Enviar para a AGT as facturas locais não presentes");
        estilizarBotao(btnComunicarEmFalta, "/imagens/actualizar_1_32x32.png",
                "Modo contingência: enviar à AGT as facturas emitidas sem ligação (estado P sem RequestID) "
                + "e consultar as que aguardam validação");
        btnComunicarEmFalta.setForeground(new Color(150, 80, 0));
        estilizarBotao(btnConsultar, "/imagens/img32x32/_info.png", "Consultar na AGT o estado da factura seleccionada");
        estilizarBotao(btnActualizarTodas, "/imagens/actualizar_1_32x32.png", "Consultar na AGT todas as facturas pendentes da lista");
        estilizarBotao(btnVisualizar, "/imagens/REPORTE.png", "Relatório \"Histórico de Facturação\" do intervalo de datas");
        estilizarBotao(btnActualizarEstado, "/imagens/actualizar_1_32x32.png", null);
        estilizarBotao(btnImprimir, "/imagens/bonus/icons8_print_200px.png", "Ver em relatório os dados da tabela actual, no intervalo de datas pesquisado");
        estilizarBotao(btnSair, "/imagens/LOGOUT - VERMELHO/Logout 32x32.png", "Fechar o formulário");
        btnActualizarEstado.setForeground(new Color(0, 110, 0));
        btnEnviarPendentes.setForeground(new Color(0, 0, 150));

        JPanel topo = new JPanel();
        topo.setLayout(new javax.swing.BoxLayout(topo, javax.swing.BoxLayout.Y_AXIS));
        topo.setBorder(BorderFactory.createEmptyBorder(6, 8, 0, 8));
        JPanel linhaModoPeriodo = new JPanel(new BorderLayout(8, 0));
        linhaModoPeriodo.add(modos, BorderLayout.WEST);
        linhaModoPeriodo.add(filtros, BorderLayout.CENTER);
        topo.add(linhaModoPeriodo);
        topo.add(individual);
        topo.add(accoes);

        // ---------------- Tabelas
        estilizarTabela(tabela);
        tabela.getColumnModel().getColumn(0).setPreferredWidth(60);
        tabela.getColumnModel().getColumn(1).setPreferredWidth(200);
        tabela.getColumnModel().getColumn(2).setPreferredWidth(120);
        tabela.getColumnModel().getColumn(3).setPreferredWidth(170);
        tabela.getColumnModel().getColumn(4).setPreferredWidth(130);
        tabela.getColumnModel().getColumn(5).setPreferredWidth(180);
        tabela.getColumnModel().getColumn(COL_ESTADO).setPreferredWidth(110);
        tabela.getColumnModel().getColumn(COL_ESTADO).setCellRenderer(new EstadoRenderer());
        tabela.setToolTipText("Duplo clique para ver o documento impresso (2ª via)");
        tabela.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2 && tabela.getSelectedRow() >= 0) {
                    int row = tabela.convertRowIndexToModel(tabela.getSelectedRow());
                    reimprimirDocumento(String.valueOf(model.getValueAt(row, 1)));
                }
            }
        });

        estilizarTabela(tabelaAGT);
        tabelaAGT.getColumnModel().getColumn(0).setPreferredWidth(200);
        tabelaAGT.getColumnModel().getColumn(1).setPreferredWidth(120);
        tabelaAGT.getColumnModel().getColumn(2).setPreferredWidth(50);
        tabelaAGT.getColumnModel().getColumn(3).setPreferredWidth(100);
        tabelaAGT.getColumnModel().getColumn(4).setPreferredWidth(160);
        tabelaAGT.getColumnModel().getColumn(5).setPreferredWidth(110);
        tabelaAGT.getColumnModel().getColumn(6).setPreferredWidth(130);
        tabelaAGT.getColumnModel().getColumn(10).setPreferredWidth(80);
        tabelaAGT.getColumnModel().getColumn(COL_AGT_EXISTE_LOCAL).setPreferredWidth(150);
        tabelaAGT.getColumnModel().getColumn(COL_AGT_EXISTE_LOCAL).setCellRenderer(new ExisteLocalRenderer());
        tabelaAGT.getColumnModel().getColumn(COL_AGT_ESTADO).setPreferredWidth(110);
        tabelaAGT.getColumnModel().getColumn(COL_AGT_ESTADO).setCellRenderer(new EstadoRenderer());
        tabelaAGT.setToolTipText("Duplo clique para ver o documento impresso (2ª via)");
        tabelaAGT.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2 && tabelaAGT.getSelectedRow() >= 0) {
                    reimprimirLinhaAGT(tabelaAGT.convertRowIndexToModel(tabelaAGT.getSelectedRow()));
                }
            }
        });

        estilizarTabela(tabelaIndividual);
        tabelaIndividual.getColumnModel().getColumn(0).setPreferredWidth(260);
        tabelaIndividual.getColumnModel().getColumn(1).setPreferredWidth(120);
        tabelaIndividual.getColumnModel().getColumn(2).setPreferredWidth(170);
        tabelaIndividual.getColumnModel().getColumn(3).setPreferredWidth(180);
        tabelaIndividual.getColumnModel().getColumn(4).setPreferredWidth(180);
        tabelaIndividual.getColumnModel().getColumn(4).setCellRenderer(new EstadoRenderer());
        tabelaIndividual.getColumnModel().getColumn(5).setPreferredWidth(170);
        DefaultTableCellRenderer direita = new DefaultTableCellRenderer();
        direita.setHorizontalAlignment(JLabel.RIGHT);
        tabelaIndividual.getColumnModel().getColumn(5).setCellRenderer(direita);
        tabelaIndividual.setToolTipText("Clique numa linha para ver o documento impresso (2ª via)");
        tabelaIndividual.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));
        tabelaIndividual.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseReleased(java.awt.event.MouseEvent e) {
                // um clique reimprime (o 2º clique de um duplo clique é ignorado).
                // mouseReleased e não mouseClicked: este não dispara se o rato
                // se mexer entre carregar e largar.
                int linha = tabelaIndividual.rowAtPoint(e.getPoint());
                if (javax.swing.SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 1 && linha >= 0) {
                    reimprimirLinhaIndividual(tabelaIndividual.convertRowIndexToModel(linha));
                }
            }
        });

        // Faixa do Total Geral da consulta individual
        lblTotalIndividual.setFont(FONTE_NEGRITO.deriveFont(16f));
        lblTotalIndividual.setForeground(AZUL_ESCURO);
        lblTotalIndividual.setOpaque(true);
        lblTotalIndividual.setBackground(AZUL_CABECALHO);
        lblTotalIndividual.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(170, 190, 220)),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        JPanel painelIndividual = new JPanel(new BorderLayout());
        painelIndividual.add(new JScrollPane(tabelaIndividual), BorderLayout.CENTER);
        painelIndividual.add(lblTotalIndividual, BorderLayout.SOUTH);

        abas.setFont(FONTE_NEGRITO);
        abas.addTab("Base de dados local", icone("/imagens/proucura.png", 18), new JScrollPane(tabela));
        abas.addTab("Portal AGT", icone("/imagem/find/seach_32x32.png", 18), new JScrollPane(tabelaAGT));
        abas.addTab("Consulta Individual", icone("/imagens/REPORTE.png", 18), painelIndividual);
        // "Consultar seleccionada" e "Actualizar todas" só fazem sentido na lista local
        abas.addChangeListener(e -> actualizarControlos());
        JPanel centro = new JPanel(new BorderLayout());
        centro.setBorder(BorderFactory.createEmptyBorder(4, 0, 0, 0));
        centro.add(abas, BorderLayout.CENTER);

        // ---------------- Rodapé: progresso, registo e Sair
        txtLog.setEditable(false);
        txtLog.setRows(4);
        txtLog.setFont(new Font("Consolas", Font.PLAIN, 13));
        JScrollPane scrollLog = new JScrollPane(txtLog);
        scrollLog.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(170, 190, 220)),
                "Registo", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP,
                FONTE_NEGRITO, AZUL_ESCURO));

        barra.setStringPainted(true);
        barra.setPreferredSize(new Dimension(100, 22));
        lblResumo.setFont(FONTE_NEGRITO);

        JPanel rodape = new JPanel(new BorderLayout(12, 0));
        JPanel linhaBarra = new JPanel(new BorderLayout(8, 0));
        linhaBarra.add(barra, BorderLayout.CENTER);
        linhaBarra.add(lblResumo, BorderLayout.EAST);
        // barra centrada na vertical (não estica até à altura do botão Sair)
        JPanel centroBarra = new JPanel(new java.awt.GridBagLayout());
        java.awt.GridBagConstraints gc = new java.awt.GridBagConstraints();
        gc.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gc.weightx = 1;
        centroBarra.add(linhaBarra, gc);
        rodape.add(centroBarra, BorderLayout.CENTER);
        rodape.add(btnSair, BorderLayout.EAST);
        rodape.setBorder(BorderFactory.createEmptyBorder(6, 8, 8, 8));

        JLabel lblAmbiente = new JLabel(
                FEConfig.isHomologacao()
                        ? "AMBIENTE: HOMOLOGAÇÃO (testes) - os estados gravados aqui são de teste"
                        : "AMBIENTE: PRODUÇÃO - os estados gravados aqui são reais",
                JLabel.CENTER);
        lblAmbiente.setOpaque(true);
        lblAmbiente.setFont(FONTE_NEGRITO.deriveFont(15f));
        lblAmbiente.setForeground(Color.WHITE);
        lblAmbiente.setBackground(FEConfig.isHomologacao() ? new Color(190, 0, 0) : new Color(0, 130, 0));
        lblAmbiente.setBorder(BorderFactory.createEmptyBorder(6, 4, 6, 4));

        JPanel norte = new JPanel(new BorderLayout());
        norte.add(lblAmbiente, BorderLayout.NORTH);
        norte.add(topo, BorderLayout.CENTER);

        add(norte, BorderLayout.NORTH);
        // tabela e Registo separados por uma divisória que o utilizador pode arrastar
        javax.swing.JSplitPane divisoria = new javax.swing.JSplitPane(javax.swing.JSplitPane.VERTICAL_SPLIT, centro, scrollLog);
        divisoria.setResizeWeight(0.82);
        divisoria.setOneTouchExpandable(true);
        divisoria.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
        add(divisoria, BorderLayout.CENTER);
        add(rodape, BorderLayout.SOUTH);

        // Letra maior nos campos e rótulos (os botões/tabelas já foram tratados)
        aplicarFonte(norte);
        txtNif.setPreferredSize(new Dimension(150, 30));
        txtNome.setPreferredSize(new Dimension(230, 30));
        cbFiltro.setPreferredSize(new Dimension(130, 30));
        cbTipoDocumento.setPreferredSize(new Dimension(170, 30));
        dcDataInicio.setPreferredSize(new Dimension(150, 30));
        dcDataFim.setPreferredSize(new Dimension(150, 30));

        chkDatas.addActionListener(e -> {
            dcDataInicio.setEnabled(chkDatas.isSelected());
            dcDataFim.setEnabled(chkDatas.isSelected());
        });
        // Pesquisar automaticamente ao escolher/alterar a data inicial ou final
        java.beans.PropertyChangeListener aoMudarData = evt -> {
            if (!chkDatas.isSelected()
                    || dcDataInicio.getDate() == null
                    || dcDataFim.getDate() == null) {
                return; // data incompleta (ex.: a escrever): não pesquisa
            }
            // Consulta individual AGT: só ao carregar em Enter/Pesquisar (é lenta)
            if (rbIndividualAGT.isSelected()
                    || (rbIndividualLocal.isSelected() && txtNif.getText().trim().isEmpty()
                    && txtNome.getText().trim().isEmpty())) {
                return;
            }
            if (ocupado) {
                recarregarPendente = true; // pesquisa quando a operação actual terminar
            } else {
                pesquisarModoActual();
            }
        };
        dcDataInicio.addPropertyChangeListener("date", aoMudarData);
        dcDataFim.addPropertyChangeListener("date", aoMudarData);

        btnSair.addActionListener(e -> dispose());
        btnPesquisar.addActionListener(e -> pesquisarModoActual());
        txtNif.addActionListener(e -> consultarIndividual()); // Enter
        txtNome.addActionListener(e -> consultarIndividual()); // Enter
        cbTipoDocumento.addActionListener(e -> filtrarHistorico());
        btnVisualizar.addActionListener(e -> visualizarHistorico());
        chkSoPendentes.addActionListener(e -> filtrarHistorico());
        btnActualizarEstado.addActionListener(e -> actualizarEstadoPendentesIndividual());
        btnPesquisarAGT.addActionListener(e -> pesquisarNaAGT());
        btnEnviarPendentes.addActionListener(e -> enviarPendentesParaAGT());
        btnComunicarEmFalta.addActionListener(e -> comunicarFacturasEmFalta());
        cbFiltro.addActionListener(e -> carregar());
        btnConsultar.addActionListener(e -> consultarSeleccionada());
        btnActualizarTodas.addActionListener(e -> actualizarTodasPendentes());
        btnImprimir.addActionListener(e -> imprimirTabelaActual());

        aplicarModo();
    }

    // =====================================================
    // APARÊNCIA (ícones, tamanhos, cores)
    // =====================================================
    /**
     * Ocupa ~92% da área útil do ecrã (sem a barra de tarefas); em ecrãs
     * pequenos abre maximizado.
     */
    private void ajustarAoEcra() {
        java.awt.Rectangle util = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        setMinimumSize(new Dimension(Math.min(1100, util.width), Math.min(650, util.height)));
        setSize((int) (util.width * 0.92), (int) (util.height * 0.92));
        setLocationRelativeTo(null);
        if (util.width <= 1440 || util.height <= 800) {
            setExtendedState(MAXIMIZED_BOTH);
        }
    }

    /**
     * Ícone do projecto redimensionado; null se o ficheiro não existir.
     */
    private static javax.swing.ImageIcon icone(String caminho, int tamanho) {
        java.net.URL url = FormularioConsultaEstadoFE.class.getResource(caminho);
        if (url == null) {
            System.err.println("[FormularioConsultaEstadoFE] Ícone não encontrado: " + caminho);
            return null;
        }
        java.awt.Image img = new javax.swing.ImageIcon(url).getImage()
                .getScaledInstance(tamanho, tamanho, java.awt.Image.SCALE_SMOOTH);
        return new javax.swing.ImageIcon(img);
    }

    private static JPanel painelTitulado(String titulo, java.awt.LayoutManager layout) {
        JPanel p = new JPanel(layout);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(170, 190, 220)),
                        titulo, javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP,
                        FONTE_NEGRITO.deriveFont(13f), AZUL_ESCURO),
                BorderFactory.createEmptyBorder(0, 4, 2, 4)));
        return p;
    }

    private static JLabel rotulo(String texto) {
        JLabel l = new JLabel(texto);
        l.setFont(FONTE_NEGRITO);
        return l;
    }

    private static void estilizarBotao(JButton b, String caminhoIcone, String dica) {
        javax.swing.ImageIcon ic = icone(caminhoIcone, 24);
        if (ic != null) {
            b.setIcon(ic);
        }
        b.setFont(FONTE_NEGRITO);
        b.setIconTextGap(8);
        b.setFocusPainted(false);
        b.setMargin(new java.awt.Insets(6, 12, 6, 14));
        b.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));
        if (dica != null) {
            b.setToolTipText(dica);
        }
    }

    private static void estilizarTabela(JTable t) {
        t.setFont(FONTE);
        t.setRowHeight(30);
        t.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        t.setGridColor(new Color(225, 230, 238));
        t.setSelectionBackground(new Color(51, 122, 183));
        t.setSelectionForeground(Color.WHITE);
        t.setFillsViewportHeight(true);
        javax.swing.table.JTableHeader h = t.getTableHeader();
        h.setFont(FONTE_NEGRITO);
        h.setPreferredSize(new Dimension(h.getPreferredSize().width, 34));
        h.setReorderingAllowed(false);
    }

    /**
     * Letra maior nos campos de texto, combos e checkboxes (rótulos, botões e
     * radio buttons têm estilo próprio).
     */
    private static void aplicarFonte(java.awt.Container c) {
        for (Component filho : c.getComponents()) {
            if (filho instanceof JTextField || filho instanceof JComboBox || filho instanceof JCheckBox) {
                filho.setFont(filho instanceof JCheckBox ? FONTE_NEGRITO : FONTE);
            }
            if (filho instanceof java.awt.Container && !(filho instanceof JComboBox)) {
                aplicarFonte((java.awt.Container) filho);
            }
        }
    }

    /**
     * FlowLayout que passa os componentes para a linha seguinte quando falta
     * largura E aumenta a altura do painel (o FlowLayout normal corta-os).
     */
    private static class WrapLayout extends FlowLayout {

        WrapLayout(int align, int hgap, int vgap) {
            super(align, hgap, vgap);
        }

        @Override
        public Dimension preferredLayoutSize(java.awt.Container alvo) {
            return tamanho(alvo, true);
        }

        @Override
        public Dimension minimumLayoutSize(java.awt.Container alvo) {
            Dimension d = tamanho(alvo, false);
            d.width -= getHgap() + 1;
            return d;
        }

        private Dimension tamanho(java.awt.Container alvo, boolean preferido) {
            synchronized (alvo.getTreeLock()) {
                // largura disponível: a do próprio painel ou, antes de ter tamanho, a do pai
                int largura = alvo.getSize().width;
                java.awt.Container pai = alvo;
                while (largura == 0 && pai.getParent() != null) {
                    pai = pai.getParent();
                    largura = pai.getSize().width;
                }
                if (largura == 0) {
                    largura = Integer.MAX_VALUE;
                }
                java.awt.Insets in = alvo.getInsets();
                int maxLargura = largura - (in.left + in.right + getHgap() * 2);

                Dimension total = new Dimension(0, 0);
                int linhaLarg = 0;
                int linhaAlt = 0;
                for (Component c : alvo.getComponents()) {
                    if (!c.isVisible()) {
                        continue;
                    }
                    Dimension d = preferido ? c.getPreferredSize() : c.getMinimumSize();
                    if (linhaLarg + d.width > maxLargura && linhaLarg > 0) {
                        total.width = Math.max(total.width, linhaLarg);
                        total.height += linhaAlt + getVgap();
                        linhaLarg = 0;
                        linhaAlt = 0;
                    }
                    linhaLarg += (linhaLarg > 0 ? getHgap() : 0) + d.width;
                    linhaAlt = Math.max(linhaAlt, d.height);
                }
                total.width = Math.max(total.width, linhaLarg);
                total.height += linhaAlt;
                total.width += in.left + in.right + getHgap() * 2;
                total.height += in.top + in.bottom + getVgap() * 2;
                return total;
            }
        }
    }

    /**
     * JTable com linhas alternadas (branco / azul muito claro).
     */
    private static class TabelaFE extends JTable {

        private static final Color LINHA_PAR = Color.WHITE;
        private static final Color LINHA_IMPAR = new Color(242, 246, 252);

        TabelaFE(javax.swing.table.TableModel m) {
            super(m);
        }

        @Override
        public Component prepareRenderer(javax.swing.table.TableCellRenderer r, int row, int column) {
            Component c = super.prepareRenderer(r, row, column);
            if (!isRowSelected(row)) {
                c.setBackground(row % 2 == 0 ? LINHA_PAR : LINHA_IMPAR);
            }
            return c;
        }
    }

    private boolean isModoIndividual() {
        return rbIndividualLocal.isSelected() || rbIndividualAGT.isSelected();
    }

    /**
     * Activa os controlos do modo escolhido e desactiva os restantes.
     */
    private void aplicarModo() {
        boolean individual = isModoIndividual();

        if (individual) {
            // a consulta individual exige sempre um intervalo de datas
            chkDatas.setSelected(true);
            dcDataInicio.setEnabled(true);
            dcDataFim.setEnabled(true);

            // trocar entre Local e AGT limpa o resultado anterior
            boolean agt = rbIndividualAGT.isSelected();
            if (historicoOrigem != null && historicoDaAGT != agt) {
                limparHistorico();
            }
            abas.setSelectedIndex(ABA_INDIVIDUAL);
            txtNif.requestFocusInWindow();
        } else if (abas.getSelectedIndex() == ABA_INDIVIDUAL) {
            abas.setSelectedIndex(0);
        }
        abas.setEnabledAt(0, !individual);
        abas.setEnabledAt(1, !individual);
        abas.setEnabledAt(ABA_INDIVIDUAL, individual);
        actualizarControlos();
    }

    private void pesquisarModoActual() {
        if (isModoIndividual()) {
            consultarIndividual();
        } else {
            abas.setSelectedIndex(0);
            carregar();
        }
    }

    private void setOcupado(boolean ocupado, String texto) {
        this.ocupado = ocupado;
        actualizarControlos();
        barra.setIndeterminate(ocupado && texto != null);
        if (!ocupado) {
            barra.setIndeterminate(false);
            barra.setValue(0);
        }
        lblResumo.setText(texto == null ? " " : texto);

        // Se uma data foi alterada durante a operação, pesquisa agora
        if (!ocupado && recarregarPendente) {
            recarregarPendente = false;
            pesquisarModoActual();
        }
    }

    private void log(String msg) {
        txtLog.append(msg + "\n");
        txtLog.setCaretPosition(txtLog.getDocument().getLength());
    }

    // =====================================================
    // CARREGAR LISTA
    // =====================================================
    private void carregar() {

        final String estado = estadoDoFiltro();

        // Intervalo [inicio, fimExclusivo): do início do dia "De" ao fim do dia "Até"
        final Date inicio;
        final Date fimExclusivo;
        if (chkDatas.isSelected()) {
            if (dcDataInicio.getDate() == null || dcDataFim.getDate() == null) {
                JOptionPane.showMessageDialog(this, "Preencha a data inicial e a data final.");
                return;
            }
            inicio = inicioDoDia(dcDataInicio.getDate(), 0);
            fimExclusivo = inicioDoDia(dcDataFim.getDate(), 1);
            if (!inicio.before(fimExclusivo)) {
                JOptionPane.showMessageDialog(this, "A data inicial não pode ser posterior à data final.");
                return;
            }
        } else {
            inicio = null;
            fimExclusivo = null;
        }

        setOcupado(true, "A carregar...");

        new SwingWorker<List<TbVenda>, Void>() {
            @Override
            protected List<TbVenda> doInBackground() {
                List<Integer> codigos = vendasController.findCodigosVendasFE(
                        conexao.getConnectionAtiva(), estado, inicio, fimExclusivo);
                List<TbVenda> resultado = new ArrayList<>();
                for (Integer codigo : codigos) {
                    TbVenda v = (TbVenda) vendasController.findById(codigo);
                    if (v != null) {
                        resultado.add(v);
                    }
                }
                return resultado;
            }

            @Override
            protected void done() {
                try {
                    vendas.clear();
                    vendas.addAll(get());
                    localPesquisaInicio = inicio;
                    localPesquisaFim = fimExclusivo == null ? null : inicioDoDia(fimExclusivo, -1);
                    preencherTabela();
                    SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
                    String periodo = chkDatas.isSelected()
                            ? " de " + f.format(dcDataInicio.getDate()) + " a " + f.format(dcDataFim.getDate())
                            : " (todas as datas)";
                    log("Carregadas " + vendas.size() + " facturas (" + cbFiltro.getSelectedItem() + ")" + periodo + ".");
                } catch (Exception e) {
                    e.printStackTrace();
                    log("Erro ao carregar facturas: " + e.getMessage());
                } finally {
                    setOcupado(false, " ");
                }
            }
        }.execute();
    }

    private static JDateChooser criarDateChooser(int diasOffset) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_YEAR, diasOffset);
        JDateChooser dc = new JDateChooser();
        dc.setDateFormatString("dd/MM/yyyy");
        dc.setDate(c.getTime());
        dc.setPreferredSize(new Dimension(130, 26));
        return dc;
    }

    /**
     * Devolve a meia-noite do dia dado, somando "somarDias" dias.
     */
    private static Date inicioDoDia(Date data, int somarDias) {
        Calendar c = Calendar.getInstance();
        c.setTime(data);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        c.add(Calendar.DAY_OF_YEAR, somarDias);
        return c.getTime();
    }

    private String estadoDoFiltro() {
        String f = (String) cbFiltro.getSelectedItem();
        if ("Pendentes".equals(f)) {
            return "P";
        }
        if ("Validadas".equals(f)) {
            return "V";
        }
        if ("Inválidas".equals(f)) {
            return "I";
        }
        return null; // Todas
    }

    private void preencherTabela() {
        model.setRowCount(0);
        SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        for (TbVenda v : vendas) {
            Date dataHora = dataHoraVenda(v);
            model.addRow(new Object[]{
                v.getCodigo(),
                v.getCodFact(),
                v.getClienteNif(),
                dataHora == null ? "" : fmt.format(dataHora),
                v.getTotalGeral(),
                temRequestIDReal(v) ? v.getRequestID() : SEM_REQUEST_ID,
                textoEstado(v.getEstado())
            });
        }
    }

    /** Texto da coluna RequestID para facturas emitidas em contingência. */
    private static final String SEM_REQUEST_ID = "(por comunicar à AGT)";

    /**
     * true se a venda já foi submetida à AGT (requestID devolvido pela AGT).
     * requestID vazio, ou igual ao nº do documento (marcador local), significa
     * que a factura foi emitida sem ligação e ainda está por comunicar.
     */
    private static boolean temRequestIDReal(TbVenda v) {
        String r = v.getRequestID();
        return r != null && !r.trim().isEmpty() && !r.trim().equals(v.getCodFact());
    }

    /**
     * Data e hora da venda. Se dataVenda vier sem hora (00:00:00), completa-a
     * com a coluna "hora" da venda (mesma lógica do processarEnvioFE).
     */
    private static Date dataHoraVenda(TbVenda v) {
        Date data = v.getDataVenda();
        if (data == null) {
            return null;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(data);

        boolean semHora = c.get(Calendar.HOUR_OF_DAY) == 0
                && c.get(Calendar.MINUTE) == 0
                && c.get(Calendar.SECOND) == 0;

        if (semHora && v.getHora() != null) {
            Calendar h = Calendar.getInstance();
            h.setTime(v.getHora());
            c.set(Calendar.HOUR_OF_DAY, h.get(Calendar.HOUR_OF_DAY));
            c.set(Calendar.MINUTE, h.get(Calendar.MINUTE));
            c.set(Calendar.SECOND, h.get(Calendar.SECOND));
        }
        return c.getTime();
    }

    private void actualizarLinha(int row) {
        model.setValueAt(textoEstado(vendas.get(row).getEstado()), row, COL_ESTADO);
    }

    private static String textoEstado(String estado) {
        if ("V".equalsIgnoreCase(estado)) {
            return "Validado";
        }
        if ("I".equalsIgnoreCase(estado)) {
            return "Inválido";
        }
        return "Pendente";
    }

    private String nifInstituicao() {
        TbDadosInstituicao dados = dadosInstituicaoController.findByCodigo(1);
        return dados.getNif();
    }

    /**
     * Activa/desactiva os controlos conforme o modo escolhido e se há uma
     * operação a decorrer.
     */
    private void actualizarControlos() {
        boolean individual = isModoIndividual();
        boolean abaLocal = abas.getSelectedIndex() <= 0;
        boolean abaAGT = abas.getSelectedIndex() == 1;

        // modo "Consulta Estado das Facturas"
        cbFiltro.setEnabled(!ocupado && !individual);
        chkDatas.setEnabled(!ocupado && !individual);
        btnPesquisarAGT.setEnabled(!ocupado && !individual);
        // basta uma pesquisa completa na AGT (mesmo com 0 facturas no portal)
        btnEnviarPendentes.setEnabled(!ocupado && !individual && abaAGT && agtListaCompleta);
        btnComunicarEmFalta.setEnabled(!ocupado);
        btnConsultar.setEnabled(!ocupado && !individual && abaLocal);
        btnActualizarTodas.setEnabled(!ocupado && !individual && abaLocal);

        // modo "Consulta Individual" (Local / AGT)
        txtNif.setEnabled(!ocupado && individual);
        txtNome.setEnabled(!ocupado && individual);
        cbTipoDocumento.setEnabled(!ocupado && individual);
        btnVisualizar.setEnabled(!ocupado && individual && historicoOrigem != null);

        // "Mostrar todos no estado Pendente" / "Actualizar Estado": só na Consulta Individual AGT
        boolean agtComResultado = rbIndividualAGT.isSelected() && historicoOrigem != null;
        chkSoPendentes.setVisible(rbIndividualAGT.isSelected());
        chkSoPendentes.setEnabled(!ocupado && agtComResultado);
        btnActualizarEstado.setVisible(agtComResultado && chkSoPendentes.isSelected());
        btnActualizarEstado.setEnabled(!ocupado && !historicoVisivel.isEmpty());

        // comuns
        btnPesquisar.setEnabled(!ocupado);
        btnImprimir.setEnabled(!ocupado);
        for (JRadioButton rb : new JRadioButton[]{rbEstadoFacturas, rbIndividualLocal, rbIndividualAGT}) {
            rb.setEnabled(!ocupado);
        }
    }

    // =====================================================
    // PESQUISAR NO PORTAL DA AGT (listarFacturas)
    // =====================================================
    private void pesquisarNaAGT() {

        // A AGT exige sempre um intervalo de datas
        final Date inicio = dcDataInicio.getDate();
        final Date fim = dcDataFim.getDate();
        if (inicio == null || fim == null) {
            JOptionPane.showMessageDialog(this, "Preencha a data inicial e a data final.");
            return;
        }
        if (inicioDoDia(inicio, 0).after(inicioDoDia(fim, 0))) {
            JOptionPane.showMessageDialog(this, "A data inicial não pode ser posterior à data final.");
            return;
        }
        final String nif = nifInstituicao();
        final SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
        final String periodo = " de " + f.format(inicio) + " a " + f.format(fim);

        abas.setSelectedIndex(1);
        agtListaCompleta = false; // até a nova lista chegar
        setOcupado(true, "A pesquisar na AGT...");

        new SwingWorker<ListarFacturasAGTUtil.Resultado, Void>() {
            @Override
            protected ListarFacturasAGTUtil.Resultado doInBackground() {
                ListarFacturasAGTUtil.Resultado r = ListarFacturasAGTUtil.listar(nif, inicio, fim);

                // Cruzar com a BD local pelo nº da factura (cod_fact)
                java.util.Set<String> numeros = new java.util.HashSet<>();
                for (FacturaRemotaDTO f : r.getFacturas()) {
                    if (f.getDocumentNo() != null) {
                        numeros.add(f.getDocumentNo());
                    }
                }
                if (!numeros.isEmpty()) {
                    java.util.Map<String, String> estados = new java.util.HashMap<>();
                    java.util.Map<String, Integer> locais = vendasController.findCodigosPorCodFact(
                            conexao.getConnectionAtiva(), numeros, estados);
                    // NIF do cliente: o da AGT ou, se não vier, o da BD local
                    java.util.Map<String, String> nifsLocais = vendasController.findClienteNifPorCodFact(
                            conexao.getConnectionAtiva(), numeros, null, null);
                    for (FacturaRemotaDTO f : r.getFacturas()) {
                        f.setCodigoLocal(locais.get(f.getDocumentNo()));
                        f.setEstadoLocal(estados.get(f.getDocumentNo()));
                        if (f.getCustomerTaxID() == null) {
                            f.setCustomerTaxID(nifsLocais.get(f.getDocumentNo()));
                        }
                    }
                }
                return r;
            }

            @Override
            protected void done() {
                try {
                    ListarFacturasAGTUtil.Resultado r = get();
                    facturasAGT.clear();
                    facturasAGT.addAll(r.getFacturas());
                    agtPesquisaInicio = inicio;
                    agtPesquisaFim = fim;
                    agtListaCompleta = r.getMensagemErro() == null;
                    preencherTabelaAGT();

                    int naoExistem = 0;
                    for (FacturaRemotaDTO f : facturasAGT) {
                        if (f.getCodigoLocal() == null) {
                            naoExistem++;
                        }
                    }

                    final String origem = " [" + (FEConfig.isHomologacao() ? "HOMOLOGAÇÃO" : "PRODUÇÃO")
                            + ", NIF " + nif + "]";

                    if (r.getMensagemErro() == null && facturasAGT.isEmpty()) {
                        int validadasLocais = 0;
                        for (TbVenda v : vendas) {
                            if ("V".equalsIgnoreCase(v.getEstado())) {
                                validadasLocais++;
                            }
                        }
                        log("AGT" + origem + ": 0 facturas no portal" + periodo + "."
                                + (validadasLocais > 0
                                        ? " ATENÇÃO: a lista local actual tem " + validadasLocais
                                        + " facturas validadas - confirme o ambiente e o NIF."
                                        : " A lista local actual não tem facturas validadas."));
                        // Mostrar o que a AGT respondeu, para diagnóstico sem ir à consola
                        log("Resposta da AGT: " + r.getRespostaBruta());
                    } else if (r.getMensagemErro() == null) {
                        log("AGT" + origem + ": " + facturasAGT.size() + " facturas no portal" + periodo
                                + (naoExistem > 0 ? " (" + naoExistem + " não existem na BD local)." : "."));
                    } else {
                        log("AGT: erro ao listar facturas" + periodo + ": " + r.getMensagemErro());
                        String msg = r.getMensagemErro();
                        if (!facturasAGT.isEmpty()) {
                            msg = "Foram recebidas " + facturasAGT.size() + " de " + r.getTotalNaAGT()
                                    + " facturas (lista incompleta).\n\n" + msg;
                        }
                        mostrarMensagemLonga(msg, "Pesquisar na AGT", JOptionPane.WARNING_MESSAGE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    log("Erro ao pesquisar na AGT: " + e.getMessage());
                } finally {
                    setOcupado(false, " ");
                }
            }
        }.execute();
    }

    /**
     * JOptionPane com o texto numa JTextArea com quebra de linha e scroll,
     * para mensagens longas da AGT não ultrapassarem o ecrã.
     */
    private void mostrarMensagemLonga(String msg, String titulo, int tipo) {
        JTextArea txt = new JTextArea(msg);
        txt.setEditable(false);
        txt.setLineWrap(true);
        txt.setWrapStyleWord(true);
        txt.setFont(new JLabel().getFont());
        txt.setBackground(new JLabel().getBackground());
        txt.setCaretPosition(0);

        JScrollPane scroll = new JScrollPane(txt);
        scroll.setPreferredSize(new Dimension(550, 220));
        JOptionPane.showMessageDialog(this, scroll, titulo, tipo);
    }

    private void preencherTabelaAGT() {
        modelAGT.setRowCount(0);
        for (FacturaRemotaDTO r : facturasAGT) {
            modelAGT.addRow(new Object[]{
                r.getDocumentNo(),
                r.getCustomerTaxID(),
                r.getDocumentType(),
                r.getDocumentDate(),
                r.getSystemEntryDate() == null ? "" : r.getSystemEntryDate().replace('T', ' '),
                textoEstadoDocumento(r.getDocumentStatus()),
                textoAceitacao(r.getCustomerAcceptance()),
                r.getNetTotal(),
                r.getTaxPayable(),
                r.getGrossTotal(),
                r.getNumReferencias() + " / " + r.getNumPagamentos(),
                r.getCodigoLocal() == null ? "Não" : "Sim (cód. " + r.getCodigoLocal() + ")",
                // Mesmo texto da 1ª aba; sem venda local não há estado
                r.getCodigoLocal() == null ? "-" : textoEstado(r.getEstadoLocal())
            });
        }
    }

    /**
     * Linha do portal AGT: mostra o documento impresso se existir na BD local;
     * senão oferece os dados devolvidos pela AGT.
     */
    private void reimprimirLinhaAGT(int row) {
        FacturaRemotaDTO r = facturasAGT.get(row);
        if (r.getCodigoLocal() == null || !procedimento_reimprimir_documentos(r.getDocumentNo())) {
            int op = JOptionPane.showConfirmDialog(this,
                    "O documento " + r.getDocumentNo() + " não existe na base de dados local.\n"
                    + "Quer ver os dados devolvidos pela AGT?",
                    "Documento não encontrado", JOptionPane.YES_NO_OPTION);
            if (op == JOptionPane.YES_OPTION) {
                mostrarDetalheAGT(row);
            }
        }
    }

    private void reimprimirDocumento(String ref_doc) {
        if (!procedimento_reimprimir_documentos(ref_doc)) {
            JOptionPane.showMessageDialog(this,
                    "Atenção\nO Documento não existe na base de dados. \nObs: Verifique a referência. ");
        }
    }

    /**
     * Mostra a 2ª via de qualquer documento (FR, FT, RC/RG ou NC), com base nos
     * procedimento_reimprimir_* do ListarRelatorioVenda.
     *
     * @return false se o documento não existir na BD
     */
    private boolean procedimento_reimprimir_documentos(String ref_doc) {

        TbVenda venda = vendaDao.findByCodFactReemprensao(ref_doc);
        if (venda == null) {
            venda = vendaDao.findByCodFactReemprensaoNota(ref_doc);
        }
        if (venda == null) {
            return false;
        }
        mostrarSegundaVia(venda);
        return true;
    }

    private void mostrarSegundaVia(TbVenda venda) {

        List<entity.TbProduto> lista_produto_isentos = MetodosUtil.getProdutosIsentos(venda.getTbItemVendaList());
        String motivos_isentos = MetodosUtil.getMotivoIsensaoProdutos(lista_produto_isentos);

        Abreviacao abreviacao = DVML.getAbreviacao(venda.getFkDocumento().getPkDocumento());

        if (abreviacao == Abreviacao.NC || "ANULADO".equals(venda.getStatusEliminado())) {
            new ListaNotasDebito(venda.getCodigo(), Abreviacao.NC, false, true, new HashMap(), motivos_isentos);
            return;
        }
        if (abreviacao == Abreviacao.RG) {
            abreviacao = Abreviacao.RC; // o ListaVenda2 só tem relatório de recibo para RC
        }
        if (abreviacao == null) {
            JOptionPane.showMessageDialog(this, "Tipo de documento não suportado na reimpressão: "
                    + venda.getFkDocumento().getDesignacao());
            return;
        }

        new ListaVenda2(venda.getCodigo(), abreviacao, false, false,
                DVML.SEGUNDA_VIA_CONFORMIDADE_COM_ORIGINAL, motivos_isentos);
    }

    // =====================================================
    // CONSULTA INDIVIDUAL (histórico de um cliente pelo NIF)
    // =====================================================
    private void consultarIndividual() {

        final String nif = txtNif.getText().trim();
        final String nome = txtNome.getText().trim();
        if (nif.isEmpty() && nome.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Indique o NIF ou o nome do cliente / empresa.");
            txtNif.requestFocusInWindow();
            return;
        }
        if (nif.isEmpty() && nome.length() < 3) {
            JOptionPane.showMessageDialog(this, "Escreva pelo menos 3 letras do nome.");
            txtNome.requestFocusInWindow();
            return;
        }
        // texto para mensagens / registo
        final String criterio = (nif.isEmpty() ? "" : "NIF " + nif)
                + (!nif.isEmpty() && !nome.isEmpty() ? " e " : "")
                + (nome.isEmpty() ? "" : "nome \"" + nome + "\"");
        if (dcDataInicio.getDate() == null || dcDataFim.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Preencha a data inicial e a data final.");
            return;
        }
        final Date inicio = inicioDoDia(dcDataInicio.getDate(), 0);
        final Date fimExclusivo = inicioDoDia(dcDataFim.getDate(), 1);
        if (!inicio.before(fimExclusivo)) {
            JOptionPane.showMessageDialog(this, "A data inicial não pode ser posterior à data final.");
            return;
        }
        final boolean agt = rbIndividualAGT.isSelected();
        final String nifInstituicao = agt ? nifInstituicao() : null;

        setOcupado(true, agt ? "A consultar no portal da AGT..." : "A consultar a base de dados local...");

        new SwingWorker<ResultadoIndividual, Void>() {
            @Override
            protected ResultadoIndividual doInBackground() {
                ResultadoIndividual r = agt
                        ? consultarIndividualAGT(nif, nome, nifInstituicao, inicio, fimExclusivo)
                        : consultarIndividualLocal(nif, nome, inicio, fimExclusivo);
                resumirCliente(r, nif, nome);
                return r;
            }

            @Override
            protected void done() {
                try {
                    ResultadoIndividual r = get();
                    if (r.erroBloqueante != null) {
                        limparHistorico();
                        lblContribuinte.setText(" ");
                        log("Consulta individual (" + criterio + "): " + r.erroBloqueante);
                        mostrarMensagemLonga(r.erroBloqueante, "Consulta Individual", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    historico.clear();
                    historico.addAll(r.documentos);
                    historicoPendentesLocais.clear();
                    historicoPendentesLocais.addAll(r.pendentesLocais);
                    historicoNif = r.nif;
                    historicoNome = r.nome;
                    historicoMorada = r.morada;
                    historicoOrigem = r.origem;
                    historicoDaAGT = agt;
                    historicoInicio = inicio;
                    historicoFim = inicioDoDia(fimExclusivo, -1);
                    lblContribuinte.setText((r.nome == null || r.nome.isEmpty() ? "(nome desconhecido)" : r.nome)
                            + (r.nif == null ? "" : " - NIF " + r.nif));
                    filtrarHistorico();

                    SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
                    log("Consulta individual " + (agt ? "AGT" : "local") + " - " + criterio
                            + (r.nome == null ? "" : " (" + r.nome + ")") + ": "
                            + historico.size() + " documentos de " + f.format(historicoInicio)
                            + " a " + f.format(historicoFim) + ".");
                    if (r.aviso != null) {
                        log("Aviso: " + r.aviso);
                        mostrarMensagemLonga(r.aviso, "Consulta Individual AGT", JOptionPane.WARNING_MESSAGE);
                    } else if (historico.isEmpty()) {
                        JOptionPane.showMessageDialog(FormularioConsultaEstadoFE.this,
                                "Não há documentos para " + criterio + " no período escolhido.");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    log("Erro na consulta individual: " + e.getMessage());
                } finally {
                    setOcupado(false, " ");
                }
            }
        }.execute();
    }

    private static class ResultadoIndividual {

        final List<DocumentoHistoricoDTO> documentos = new ArrayList<>();
        String nif;            // NIF do cliente, ou "Vários" se a pesquisa por nome apanhou vários
        String nome;
        String morada;
        String origem;
        String erroBloqueante; // a consulta não pôde ser feita
        String aviso;          // lista possivelmente incompleta
        final List<DocumentoHistoricoDTO> pendentesLocais = new ArrayList<>(); // AGT: pendentes fora da listagem
    }

    /**
     * Preenche nif/nome/morada do resultado a partir dos documentos, quando a
     * consulta não os trouxe (ex.: pesquisa por nome).
     */
    private static void resumirCliente(ResultadoIndividual r, String nifPesquisado, String nomePesquisado) {
        java.util.Set<String> nifs = new java.util.LinkedHashSet<>();
        java.util.Set<String> nomes = new java.util.LinkedHashSet<>();
        for (DocumentoHistoricoDTO d : r.documentos) {
            if (d.getNifCliente() != null && !d.getNifCliente().trim().isEmpty()) {
                nifs.add(d.getNifCliente().trim());
            }
            if (d.getNomeCliente() != null && !d.getNomeCliente().trim().isEmpty()) {
                nomes.add(d.getNomeCliente().trim().toUpperCase());
            }
        }
        if (!nifPesquisado.isEmpty()) {
            r.nif = nifPesquisado;
        } else if (nifs.size() == 1) {
            r.nif = nifs.iterator().next();
        } else if (nifs.size() > 1) {
            r.nif = "Vários (" + nifs.size() + ")";
        }
        if (r.nome == null) {
            // nome/morada do documento mais recente; vários clientes: o texto pesquisado
            if (nomes.size() > 1) {
                r.nome = "Pesquisa por nome: \"" + nomePesquisado + "\"";
            } else {
                for (int i = r.documentos.size() - 1; i >= 0 && r.nome == null; i--) {
                    DocumentoHistoricoDTO d = r.documentos.get(i);
                    if (d.getNomeCliente() != null && !d.getNomeCliente().trim().isEmpty()) {
                        r.nome = d.getNomeCliente().trim();
                        if (r.morada == null) {
                            r.morada = d.getMoradaCliente();
                        }
                    }
                }
            }
        }
    }

    /**
     * Corre fora da EDT.
     */
    private ResultadoIndividual consultarIndividualLocal(String nif, String nome, Date inicio, Date fimExclusivo) {
        ResultadoIndividual r = new ResultadoIndividual();
        r.origem = "Base de dados local";
        r.documentos.addAll(vendasController.findHistoricoCliente(
                conexao.getConnectionAtiva(), nif, nome, inicio, fimExclusivo));
        return r;
    }

    /**
     * "Ágata  Lda" -> "AGATA LDA": para comparar nomes sem acentos nem
     * maiúsculas.
     */
    private static String normalizarNome(String s) {
        if (s == null) {
            return "";
        }
        String semAcentos = java.text.Normalizer.normalize(s, java.text.Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
        return semAcentos.toUpperCase().replaceAll("\\s+", " ").trim();
    }

    /**
     * Corre fora da EDT. Com NIF: valida-o no serviço de Consulta de NIF da
     * AGT. Depois lista no portal da AGT os documentos emitidos no período e
     * fica só com os do cliente (NIF e/ou nome - customerName da AGT).
     */
    private ResultadoIndividual consultarIndividualAGT(String nif, String nome, String nifInstituicao,
            Date inicio, Date fimExclusivo) {

        ResultadoIndividual r = new ResultadoIndividual();
        r.origem = "Portal AGT (" + (FEConfig.isHomologacao() ? "HOMOLOGAÇÃO" : "PRODUÇÃO") + ")";

        String descricao = "";
        if (!nif.isEmpty()) {
            ConsultaNifAGTUtil.Contribuinte c = ConsultaNifAGTUtil.consultar(nif);
            if (!c.isEncontrado()) {
                r.erroBloqueante = c.getMensagemErro();
                return r;
            }
            r.nome = c.getNome();
            r.morada = c.getMorada();
            descricao = "NIF " + nif + " (" + c.getNome() + ") válido na AGT";
        }
        final String nomeNormalizado = normalizarNome(nome);

        ListarFacturasAGTUtil.Resultado lista = ListarFacturasAGTUtil.listar(
                nifInstituicao, inicio, inicioDoDia(fimExclusivo, -1));
        if (lista.getMensagemErro() != null) {
            if (lista.getFacturas().isEmpty()) {
                r.erroBloqueante = (descricao.isEmpty() ? "Não" : descricao + ", mas não")
                        + " foi possível listar os documentos no portal:\n" + lista.getMensagemErro();
                return r;
            }
            r.aviso = "Foram recebidos " + lista.getFacturas().size() + " de " + lista.getTotalNaAGT()
                    + " documentos da AGT (lista incompleta).\n\n" + lista.getMensagemErro();
        }

        // O NIF do cliente vem da AGT (customerTaxID) ou, se não vier, da BD local
        java.util.Set<String> numeros = new java.util.HashSet<>();
        for (FacturaRemotaDTO f : lista.getFacturas()) {
            if (f.getDocumentNo() != null) {
                numeros.add(f.getDocumentNo());
            }
        }
        java.util.Map<String, Integer> codigos = new java.util.HashMap<>();
        java.util.Map<String, String> estadosLocais = new java.util.HashMap<>();
        java.util.Map<String, String> nifsLocais = numeros.isEmpty()
                ? new java.util.HashMap<>()
                : vendasController.findClienteNifPorCodFact(conexao.getConnectionAtiva(), numeros, codigos, estadosLocais);

        for (FacturaRemotaDTO f : lista.getFacturas()) {
            String tipo = DocumentoHistoricoDTO.tipoPorDocumentoAGT(f.getDocumentType());
            String nifCliente = f.getCustomerTaxID() != null ? f.getCustomerTaxID() : nifsLocais.get(f.getDocumentNo());
            if (tipo == null) {
                continue;
            }
            if (!nif.isEmpty() && (nifCliente == null || !nif.equalsIgnoreCase(nifCliente.trim()))) {
                continue;
            }
            if (!nomeNormalizado.isEmpty() && !normalizarNome(f.getCustomerName()).contains(nomeNormalizado)) {
                continue;
            }
            // A listagem da AGT não traz o estado de validação: usa-se o estado
            // local; se o documento só existir na AGT, está registado lá (válido).
            String estado;
            if (codigos.containsKey(f.getDocumentNo())) {
                estado = estadosLocais.get(f.getDocumentNo());
                if (estado == null) {
                    continue; // inválido na BD local: não faz parte
                }
            } else {
                estado = DocumentoHistoricoDTO.ESTADO_SO_AGT;
            }
            DocumentoHistoricoDTO d = new DocumentoHistoricoDTO(codigos.get(f.getDocumentNo()), f.getDocumentNo(),
                    tipo, dataAGT(f), valorAGT(f.getGrossTotal()));
            d.setEstado(estado);
            d.setNifCliente(nifCliente);
            d.setNomeCliente(f.getCustomerName());
            r.documentos.add(d);
        }
        r.documentos.sort((a, b) -> {
            if (a.getData() == null || b.getData() == null) {
                return a.getData() == null ? (b.getData() == null ? 0 : 1) : -1;
            }
            return a.getData().compareTo(b.getData());
        });

        // Pendentes da BD local (mesmo cliente e período) que ainda não aparecem
        // na listagem da AGT - só são mostrados com "Mostrar todos no estado Pendente"
        java.util.Set<String> naListagem = new java.util.HashSet<>();
        for (DocumentoHistoricoDTO d : r.documentos) {
            naListagem.add(d.getNumero());
        }
        for (DocumentoHistoricoDTO d : vendasController.findHistoricoCliente(
                conexao.getConnectionAtiva(), nif, nome, inicio, fimExclusivo)) {
            if (DocumentoHistoricoDTO.ESTADO_PENDENTE.equals(d.getEstado()) && !naListagem.contains(d.getNumero())) {
                r.pendentesLocais.add(d);
            }
        }
        return r;
    }

    /**
     * systemEntryDate ("yyyy-MM-ddTHH:mm:ss") ou, na falta, documentDate.
     */
    private static Date dataAGT(FacturaRemotaDTO f) {
        try {
            if (f.getSystemEntryDate() != null && f.getSystemEntryDate().length() >= 19) {
                return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                        .parse(f.getSystemEntryDate().replace(' ', 'T').substring(0, 19));
            }
            if (f.getDocumentDate() != null && f.getDocumentDate().length() >= 10) {
                return new SimpleDateFormat("yyyy-MM-dd").parse(f.getDocumentDate().substring(0, 10));
            }
        } catch (Exception e) {
            // data em formato inesperado: fica sem data
        }
        return null;
    }

    private static BigDecimal valorAGT(String s) {
        if (s == null || s.trim().isEmpty()) {
            return BigDecimal.ZERO;
        }
        try {
            return new BigDecimal(s.trim().replace(",", ""));
        } catch (NumberFormatException e) {
            return BigDecimal.ZERO;
        }
    }

    private void limparHistorico() {
        historico.clear();
        historicoVisivel.clear();
        historicoNif = null;
        historicoNome = null;
        historicoMorada = null;
        historicoOrigem = null;
        historicoPendentesLocais.clear();
        modelIndividual.setRowCount(0);
        lblTotalIndividual.setText(" ");
        lblContribuinte.setText(" ");
        actualizarControlos();
    }

    private boolean filtroTodosDocumentos() {
        return cbTipoDocumento.getSelectedIndex() <= 0;
    }

    /**
     * Mostra na tabela os documentos do tipo escolhido e o Total Geral.
     */
    private void filtrarHistorico() {
        historicoVisivel.clear();
        String tipo = (String) cbTipoDocumento.getSelectedItem();
        boolean soPendentes = chkSoPendentes.isSelected() && rbIndividualAGT.isSelected();

        List<DocumentoHistoricoDTO> fonte = new ArrayList<>(historico);
        if (soPendentes) {
            fonte.addAll(historicoPendentesLocais);
            fonte.sort((a, b) -> a.getData() == null || b.getData() == null
                    ? (a.getData() == null ? (b.getData() == null ? 0 : 1) : -1)
                    : a.getData().compareTo(b.getData()));
        }
        for (DocumentoHistoricoDTO d : fonte) {
            boolean pendenteOk = !soPendentes || DocumentoHistoricoDTO.ESTADO_PENDENTE.equals(d.getEstado());
            if (pendenteOk && (filtroTodosDocumentos() || tipo.equals(d.getTipo()))) {
                historicoVisivel.add(d);
            }
        }

        modelIndividual.setRowCount(0);
        SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        for (DocumentoHistoricoDTO d : historicoVisivel) {
            modelIndividual.addRow(new Object[]{
                d.getNumero(),
                d.getNifCliente(),
                d.getTipo(),
                d.getData() == null ? "" : fmt.format(d.getData()),
                d.getEstado(),
                formatarValor(d.getTotal())
            });
        }

        if (historicoOrigem == null) {
            lblTotalIndividual.setText(" ");
        } else {
            lblTotalIndividual.setText("Documentos: " + historicoVisivel.size()
                    + "      Total Geral: " + formatarValor(totalGeral()) + " AOA"
                    + "      [" + historicoOrigem + "]");
        }
        actualizarControlos();
    }

    /**
     * 1234567.5 -> "1.234.567,50"
     */
    private static String formatarValor(BigDecimal valor) {
        java.text.DecimalFormatSymbols s = new java.text.DecimalFormatSymbols();
        s.setDecimalSeparator(',');
        s.setGroupingSeparator('.');
        return new java.text.DecimalFormat("#,##0.00", s).format(valor == null ? BigDecimal.ZERO : valor);
    }

    private BigDecimal totalPorTipo(String tipo) {
        BigDecimal soma = BigDecimal.ZERO;
        for (DocumentoHistoricoDTO d : historicoVisivel) {
            if (tipo.equals(d.getTipo()) && d.getTotal() != null) {
                soma = soma.add(d.getTotal());
            }
        }
        return soma;
    }

    /**
     * "Todos Documentos": Facturas + Facturas Recibo - Notas de Crédito (os
     * recibos liquidam facturas já contadas). Um só tipo: a soma desse tipo.
     */
    private BigDecimal totalGeral() {
        if (!filtroTodosDocumentos()) {
            return totalPorTipo((String) cbTipoDocumento.getSelectedItem());
        }
        return totalPorTipo(DocumentoHistoricoDTO.FACTURA)
                .add(totalPorTipo(DocumentoHistoricoDTO.FACTURA_RECIBO))
                .subtract(totalPorTipo(DocumentoHistoricoDTO.NOTA_CREDITO));
    }

    private void reimprimirLinhaIndividual(int row) {
        DocumentoHistoricoDTO d = historicoVisivel.get(row);
        try {
            // JPA (como nas outras abas): o findById do VendasController não
            // carrega os artigos e a 2ª via precisa deles
            TbVenda venda = d.getCodigo() == null ? null : vendaDao.findTbVenda(d.getCodigo());
            if (venda == null) {
                JOptionPane.showMessageDialog(this,
                        "O documento " + d.getNumero() + " não existe na base de dados local.");
                return;
            }
            mostrarSegundaVia(venda);
        } catch (Exception e) {
            e.printStackTrace();
            log("Erro ao reimprimir " + d.getNumero() + ": " + e);
            JOptionPane.showMessageDialog(this, "Erro ao reimprimir o documento " + d.getNumero() + ":\n" + e,
                    "Reimpressão", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * "Actualizar Estado": consulta na AGT (obterEstado, pelo requestID) cada
     * documento pendente da tabela e grava o estado devolvido na BD local. Os
     * que a AGT der como inválidos saem da lista (não fazem parte do
     * histórico). Quem decide o estado é a AGT: só passa a Validado o que a
     * AGT já validou.
     */
    private void actualizarEstadoPendentesIndividual() {

        final List<DocumentoHistoricoDTO> pendentes = new ArrayList<>();
        int semLocal = 0;
        for (DocumentoHistoricoDTO d : historicoVisivel) {
            if (DocumentoHistoricoDTO.ESTADO_PENDENTE.equals(d.getEstado())) {
                if (d.getCodigo() == null) {
                    semLocal++;
                } else {
                    pendentes.add(d);
                }
            }
        }
        if (pendentes.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Não há documentos pendentes na lista para actualizar.");
            return;
        }
        int ok = JOptionPane.showConfirmDialog(this,
                "Consultar na AGT o estado de " + pendentes.size() + " documento(s) pendente(s)\n"
                + "e gravar o resultado na base de dados local?\n"
                + "Ambiente: " + (FEConfig.isHomologacao() ? "HOMOLOGAÇÃO" : "PRODUÇÃO")
                + (semLocal > 0 ? "\n\n(" + semLocal + " documento(s) sem venda local serão ignorados)" : ""),
                "Actualizar Estado", JOptionPane.YES_NO_OPTION);
        if (ok != JOptionPane.YES_OPTION) {
            return;
        }

        final String nif = nifInstituicao();
        final int[] contagem = new int[3]; // 0 validados, 1 inválidos, 2 ainda pendentes
        final List<DocumentoHistoricoDTO> invalidos = new ArrayList<>();
        setOcupado(true, null);
        barra.setIndeterminate(false);
        barra.setValue(0);
        log("Actualizar Estado: a consultar " + pendentes.size() + " documento(s) pendente(s) na AGT...");

        SwingWorker<Void, Object[]> worker = new SwingWorker<Void, Object[]>() {
            @Override
            protected Void doInBackground() throws Exception {
                int feitos = 0;
                for (DocumentoHistoricoDTO d : pendentes) {
                    TbVenda venda = (TbVenda) vendasController.findById(d.getCodigo());
                    ConsultaEstadoDTO r;
                    if (venda == null) {
                        r = new ConsultaEstadoDTO();
                        r.setCodFact(d.getNumero());
                        r.setMensagem("A venda já não existe na base de dados local.");
                    } else {
                        r = ConsultaEstadoFacturaUtil.consultarEActualizar(
                                venda, nif, vendasController, conexao.getConnectionAtiva());
                    }
                    feitos++;
                    setProgress(feitos * 100 / pendentes.size());
                    publish(new Object[]{d, r});

                    // E98 = demasiados pedidos -> parar e tentar mais tarde
                    if ("E98".equals(r.getErrorCode())) {
                        break;
                    }
                    Thread.sleep(1000); // pausa entre chamadas
                }
                return null;
            }

            @Override
            protected void process(List<Object[]> chunks) {
                for (Object[] p : chunks) {
                    DocumentoHistoricoDTO d = (DocumentoHistoricoDTO) p[0];
                    ConsultaEstadoDTO r = (ConsultaEstadoDTO) p[1];
                    registarResultado(r);
                    if ("V".equals(r.getEstadoLocal())) {
                        d.setEstado(DocumentoHistoricoDTO.ESTADO_VALIDADO);
                        contagem[0]++;
                    } else if ("I".equals(r.getEstadoLocal())) {
                        invalidos.add(d);
                        contagem[1]++;
                    } else {
                        contagem[2]++;
                    }
                    lblResumo.setText("V: " + contagem[0] + "  I: " + contagem[1] + "  P: " + contagem[2]);
                }
            }

            @Override
            protected void done() {
                try {
                    get();
                } catch (Exception e) {
                    e.printStackTrace();
                    log("Erro ao actualizar o estado: " + e.getMessage());
                }
                // inválidos não fazem parte do histórico
                historico.removeAll(invalidos);
                historicoPendentesLocais.removeAll(invalidos);
                setOcupado(false, "V: " + contagem[0] + "  I: " + contagem[1] + "  P: " + contagem[2]);
                filtrarHistorico();
                JOptionPane.showMessageDialog(FormularioConsultaEstadoFE.this,
                        "Concluído.\nPassaram a Validado: " + contagem[0]
                        + "\nInválidos (retirados da lista): " + contagem[1]
                        + "\nAinda pendentes na AGT: " + contagem[2]);
            }
        };
        worker.addPropertyChangeListener(evt -> {
            if ("progress".equals(evt.getPropertyName())) {
                barra.setValue((Integer) evt.getNewValue());
            }
        });
        worker.execute();
    }

    /**
     * Relatório "Histórico de Facturação" (iReport historico_facturacao_A4)
     * com os documentos que estão na tabela.
     */
    private void visualizarHistorico() {
        if (historicoOrigem == null) {
            JOptionPane.showMessageDialog(this, "Faça primeiro a consulta pelo NIF.");
            return;
        }
        try {
            TbDadosInstituicao inst = dadosInstituicaoController.findByCodigo(1);
            SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");

            HashMap<String, Object> p = new HashMap<>();
            p.put("INST_NOME", inst.getNome());
            p.put("INST_NOME_COMERCIAL", inst.getNomeComercial());
            p.put("INST_MORADA_COMERCIAL", inst.getMoradaComercial());
            p.put("INST_ENDERECOS", inst.getEnderecos());
            p.put("INST_NIF", inst.getNif());
            p.put("INST_TELEFONE", inst.getTelefone());
            p.put("INST_EMAIL", inst.getEmail());
            p.put("CLIENTE_NOME", historicoNome);
            p.put("CLIENTE_NIF", historicoNif);
            p.put("CLIENTE_MORADA", historicoMorada);
            p.put("PERIODO", f.format(historicoInicio) + " a " + f.format(historicoFim));
            p.put("TIPO_DOCUMENTO", cbTipoDocumento.getSelectedItem());
            p.put("ORIGEM", historicoOrigem);
            p.put("TOTAL_FACTURAS", totalPorTipo(DocumentoHistoricoDTO.FACTURA));
            p.put("TOTAL_FACTURAS_RECIBO", totalPorTipo(DocumentoHistoricoDTO.FACTURA_RECIBO));
            p.put("TOTAL_RECIBOS", totalPorTipo(DocumentoHistoricoDTO.RECIBO));
            p.put("TOTAL_NOTAS_CREDITO", totalPorTipo(DocumentoHistoricoDTO.NOTA_CREDITO));
            p.put("TOTAL_GERAL", totalGeral());
            p.put("SOFTWARE_NAME", DVML.NAME_SOFTWARE);
            p.put("SOFTWARE_VERSION", DVML.VERSION_SOFTWARE);

            String relatorio = new File(DVML.CAMINHO_REPORT + "historico_facturacao_A4.jasper").getAbsolutePath();
            JasperPrint print = JasperFillManager.fillReport(relatorio, p,
                    new JRBeanCollectionDataSource(new ArrayList<>(historicoVisivel)));
            JasperViewer viewer = new JasperViewer(print, false);
            viewer.setTitle("Histórico de Facturação - " + (historicoNome == null ? "" : historicoNome)
                    + (historicoNif == null ? "" : " - NIF " + historicoNif));
            viewer.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao gerar o Histórico de Facturação: " + e.getMessage());
        }
    }

    /**
     * "Imprimir": mostra no JasperViewer as linhas da tabela da aba actual. Só
     * imprime por intervalo de datas: o período tem de ser o mesmo com que a
     * tabela foi carregada, para o relatório dizer exactamente o que contém.
     */
    private void imprimirTabelaActual() {
        int aba = abas.getSelectedIndex();
        DefaultTableModel origem;
        String relatorio;
        String titulo;
        String detalhe;
        String resumo = null;
        Date inicio;
        Date fim;

        if (aba == ABA_INDIVIDUAL) {
            origem = modelIndividual;
            relatorio = "consulta_estado_fe_individual_A4.jasper";
            titulo = "Consulta Individual - " + (historicoDaAGT ? "Portal AGT" : "Base de dados local");
            detalhe = "Cliente: " + (historicoNome == null ? "" : historicoNome)
                    + (historicoNif == null ? "" : " - NIF " + historicoNif)
                    + "     Documentos: " + cbTipoDocumento.getSelectedItem()
                    + (chkSoPendentes.isSelected() && rbIndividualAGT.isSelected() ? " (só pendentes)" : "");
            resumo = lblTotalIndividual.getText();
            inicio = historicoOrigem == null ? null : historicoInicio;
            fim = historicoOrigem == null ? null : historicoFim;
        } else if (aba == 1) {
            origem = modelAGT;
            relatorio = "consulta_estado_fe_agt_A4.jasper";
            titulo = "Facturas no Portal da AGT";
            detalhe = agtListaCompleta ? "" : "ATENÇÃO: lista incompleta - a AGT não devolveu todas as facturas";
            inicio = agtPesquisaInicio;
            fim = agtPesquisaFim;
        } else {
            origem = model;
            relatorio = "consulta_estado_fe_local_A4.jasper";
            titulo = "Estado das Facturas - Base de dados local";
            detalhe = "Mostrar: " + cbFiltro.getSelectedItem();
            inicio = localPesquisaInicio;
            fim = localPesquisaFim;
        }

        if (inicio == null || fim == null) {
            JOptionPane.showMessageDialog(this, "O relatório é sempre por intervalo de datas.\n"
                    + "Marque \"Filtrar por datas\", preencha as datas e faça a pesquisa.");
            return;
        }
        if (dcDataInicio.getDate() == null || dcDataFim.getDate() == null
                || !inicioDoDia(dcDataInicio.getDate(), 0).equals(inicioDoDia(inicio, 0))
                || !inicioDoDia(dcDataFim.getDate(), 0).equals(inicioDoDia(fim, 0))) {
            JOptionPane.showMessageDialog(this, "As datas seleccionadas não são as da pesquisa mostrada na tabela.\n"
                    + "Pesquise de novo antes de imprimir.");
            return;
        }
        if (origem.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Não há dados na tabela para imprimir.");
            return;
        }

        // Ordem crescente só no relatório (a JTable fica como está): data e, na mesma
        // data, nº do documento - o mais curto primeiro, para FT 2026/9 vir antes de FT 2026/10
        final int colNumero = aba == 0 ? 1 : 0;
        final String[] chaves = new String[origem.getRowCount()];
        final String[] numeros = new String[origem.getRowCount()];
        List<Integer> ordem = new ArrayList<>();
        for (int l = 0; l < origem.getRowCount(); l++) {
            chaves[l] = chaveDataRelatorio(aba, l);
            numeros[l] = String.valueOf(origem.getValueAt(l, colNumero));
            ordem.add(l);
        }
        ordem.sort((a, b) -> {
            int r = chaves[a].compareTo(chaves[b]);
            if (r == 0) {
                r = Integer.compare(numeros[a].length(), numeros[b].length());
            }
            return r != 0 ? r : numeros[a].compareTo(numeros[b]);
        });

        // Cópia em texto, tal como aparece no ecrã (todos os campos do relatório são String),
        // com o nome do cliente antes do NIF: esta coluna só existe no relatório
        final int colNif = aba == 0 ? 2 : 1;
        String[] nomes = nomesClientesRelatorio(aba, numeros);
        // Só na JTable: "Aceitação Cliente" e "Existe na BD local?" (Portal AGT) e "RequestID" (local)
        final java.util.List<Integer> omitidas = aba == 1
                ? java.util.Arrays.asList(COL_AGT_ACEITACAO, COL_AGT_EXISTE_LOCAL)
                : aba == ABA_INDIVIDUAL
                        ? new ArrayList<Integer>()
                        : java.util.Arrays.asList(COL_REQUEST_ID);
        DefaultTableModel dados = new DefaultTableModel(origem.getRowCount(),
                origem.getColumnCount() + 1 - omitidas.size());
        for (int i = 0; i < ordem.size(); i++) {
            int l = ordem.get(i);
            int destino = 0;
            for (int c = 0; c < origem.getColumnCount(); c++) {
                if (c == colNif) {
                    dados.setValueAt(nomes[l], i, destino++);
                }
                if (omitidas.contains(c)) {
                    continue;
                }
                Object valor = origem.getValueAt(l, c);
                dados.setValueAt(valor instanceof BigDecimal ? formatarValor((BigDecimal) valor)
                        : valor == null ? null : valor.toString(), i, destino++);
            }
        }

        try {
            TbDadosInstituicao inst = dadosInstituicaoController.findByCodigo(1);
            SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");

            HashMap<String, Object> p = new HashMap<>();
            p.put("INST_NOME", inst.getNome());
            p.put("INST_NIF", inst.getNif());
            p.put("INST_MORADA", inst.getMoradaComercial());
            p.put("TITULO", titulo);
            p.put("PERIODO", f.format(inicio) + " a " + f.format(fim));
            p.put("DETALHE", detalhe);
            p.put("AMBIENTE", FEConfig.isHomologacao() ? "HOMOLOGAÇÃO" : "PRODUÇÃO");
            p.put("RESUMO", resumo);

            String caminho = new File(DVML.CAMINHO_REPORT + relatorio).getAbsolutePath();
            JasperPrint print = JasperFillManager.fillReport(caminho, p,
                    new net.sf.jasperreports.engine.data.JRTableModelDataSource(dados));
            JasperViewer viewer = new JasperViewer(print, false);
            viewer.setTitle(titulo + " - " + f.format(inicio) + " a " + f.format(fim));
            viewer.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
            log("Erro ao gerar o relatório: " + e);
            JOptionPane.showMessageDialog(this, "Erro ao gerar o relatório:\n" + e,
                    "Imprimir", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Nome do cliente de cada linha da tabela da aba: o que a linha já traz ou,
     * se faltar, o da BD local pelo nº do documento.
     */
    private String[] nomesClientesRelatorio(int aba, String[] numeros) {
        String[] nomes = new String[numeros.length];
        java.util.Set<String> faltam = new java.util.HashSet<>();
        for (int l = 0; l < numeros.length; l++) {
            nomes[l] = aba == 1 ? facturasAGT.get(l).getCustomerName()
                    : aba == ABA_INDIVIDUAL ? historicoVisivel.get(l).getNomeCliente()
                    : vendas.get(l).getNomeCliente();
            if (nomes[l] == null || nomes[l].trim().isEmpty()) {
                faltam.add(numeros[l]);
            }
        }
        if (!faltam.isEmpty()) {
            java.util.Map<String, String> locais = vendasController.findNomesClientePorCodFact(
                    conexao.getConnectionAtiva(), faltam);
            for (int l = 0; l < numeros.length; l++) {
                if (nomes[l] == null || nomes[l].trim().isEmpty()) {
                    nomes[l] = locais.get(numeros[l]);
                }
            }
        }
        return nomes;
    }

    /**
     * Texto que ordena por data a linha dada da tabela da aba. Sem data: fica
     * no fim do relatório.
     */
    private String chaveDataRelatorio(int aba, int linha) {
        final String semData = "9999";
        if (aba == 1) {
            FacturaRemotaDTO f = facturasAGT.get(linha);
            return (f.getDocumentDate() == null ? semData : f.getDocumentDate()) + " "
                    + (f.getSystemEntryDate() == null ? "" : f.getSystemEntryDate());
        }
        Date data = aba == ABA_INDIVIDUAL
                ? historicoVisivel.get(linha).getData()
                : dataHoraVenda(vendas.get(linha));
        return data == null ? semData : new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(data);
    }

    private void mostrarDetalheAGT(int row) {
        FacturaRemotaDTO r = facturasAGT.get(row);
        JTextArea txt = new JTextArea(r.getJsonBruto(), 25, 70);
        txt.setEditable(false);
        txt.setCaretPosition(0);
        JOptionPane.showMessageDialog(this, new JScrollPane(txt),
                "Factura " + r.getDocumentNo() + " (AGT)", JOptionPane.PLAIN_MESSAGE);
    }

    private static String textoEstadoDocumento(String s) {
        if ("N".equalsIgnoreCase(s)) {
            return "Normal";
        }
        if ("C".equalsIgnoreCase(s)) {
            return "Correcção";
        }
        return s == null ? "" : s;
    }

    private static String textoAceitacao(String s) {
        if ("V".equalsIgnoreCase(s)) {
            return "Confirmada";
        }
        if ("N".equalsIgnoreCase(s)) {
            return "Por confirmar";
        }
        if ("R".equalsIgnoreCase(s)) {
            return "Rejeitada";
        }
        return s == null ? "" : s;
    }

    // =====================================================
    // CONSULTAR UMA FACTURA
    // =====================================================
    private void consultarSeleccionada() {

        int viewRow = tabela.getSelectedRow();
        if (viewRow < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione uma factura na tabela.");
            return;
        }
        final int row = tabela.convertRowIndexToModel(viewRow);
        final TbVenda venda = vendas.get(row);
        final String nif = nifInstituicao();

        if (!temRequestIDReal(venda)) {
            JOptionPane.showMessageDialog(this,
                    "A factura " + venda.getCodFact() + " foi emitida sem ligação à AGT e ainda não foi"
                    + " comunicada (não tem RequestID), por isso ainda não há estado para consultar.\n\n"
                    + "Será enviada automaticamente em segundo plano quando houver ligação,\n"
                    + "ou pode enviá-la já em \"" + btnComunicarEmFalta.getText() + "\".",
                    "Factura por comunicar", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        setOcupado(true, "A consultar " + venda.getCodFact() + "...");

        new SwingWorker<ConsultaEstadoDTO, Void>() {
            @Override
            protected ConsultaEstadoDTO doInBackground() {
                return ConsultaEstadoFacturaUtil.consultarEActualizar(
                        venda, nif, vendasController, conexao.getConnectionAtiva());
            }

            @Override
            protected void done() {
                try {
                    ConsultaEstadoDTO r = get();
                    actualizarLinha(row);
                    registarResultado(r);
                    mostrarDialogo(venda, r);
                } catch (Exception e) {
                    e.printStackTrace();
                    log("Erro: " + e.getMessage());
                } finally {
                    setOcupado(false, " ");
                }
            }
        }.execute();
    }

    private void mostrarDialogo(TbVenda venda, ConsultaEstadoDTO r) {
        StringBuilder msg = new StringBuilder(r.getMensagem() == null ? "" : r.getMensagem());
        for (String erro : r.getErros()) {
            msg.append("\n• ").append(erro);
        }
        if (msg.length() == 0) {
            msg.append("Sem mensagem da AGT. Resposta recebida:\n").append(r.getRespostaBruta());
        }
        int tipo = "V".equals(r.getEstadoLocal()) ? JOptionPane.INFORMATION_MESSAGE
                : "I".equals(r.getEstadoLocal()) ? JOptionPane.ERROR_MESSAGE
                : JOptionPane.WARNING_MESSAGE;
        mostrarMensagemLonga(msg.toString(), "Factura " + venda.getCodFact(), tipo);
    }

    private void registarResultado(ConsultaEstadoDTO r) {
        StringBuilder sb = new StringBuilder();
        sb.append(r.getCodFact()).append(": ")
                .append(r.getMensagem() != null ? r.getMensagem()
                        : "(sem mensagem) resposta AGT: " + r.getRespostaBruta());
        for (String erro : r.getErros()) {
            sb.append("\n      • ").append(erro);
        }
        if (r.isEstadoAlterado()) {
            sb.append("  [estado local actualizado]");
        }
        log(sb.toString());
    }

    // =====================================================
    // ACTUALIZAR TODAS AS PENDENTES (em lote)
    // =====================================================
    private static class Progresso {

        final int row;
        final ConsultaEstadoDTO dto;

        Progresso(int row, ConsultaEstadoDTO dto) {
            this.row = row;
            this.dto = dto;
        }
    }

    private void actualizarTodasPendentes() {

        final List<Integer> linhas = new ArrayList<>();
        int porComunicar = 0;
        for (int i = 0; i < vendas.size(); i++) {
            String e = vendas.get(i).getEstado();
            if (e == null || "P".equalsIgnoreCase(e)) {
                if (temRequestIDReal(vendas.get(i))) {
                    linhas.add(i);
                } else {
                    porComunicar++; // sem RequestID: ainda não há estado na AGT
                }
            }
        }
        String avisoPorComunicar = porComunicar == 0 ? ""
                : porComunicar + " factura(s) pendente(s) ainda por comunicar (sem RequestID) não são consultadas:\n"
                + "são enviadas em segundo plano ou em \"" + btnComunicarEmFalta.getText() + "\".\n\n";
        if (linhas.isEmpty()) {
            JOptionPane.showMessageDialog(this, avisoPorComunicar
                    + "Não há facturas pendentes já submetidas à AGT na lista para o período escolhido.");
            return;
        }
        int ok = JOptionPane.showConfirmDialog(this, avisoPorComunicar
                + "Consultar " + linhas.size() + " facturas pendentes na AGT?\n"
                + "Ambiente: " + (FEConfig.isHomologacao() ? "HOMOLOGAÇÃO" : "PRODUÇÃO"),
                "Confirmar", JOptionPane.YES_NO_OPTION);
        if (ok != JOptionPane.YES_OPTION) {
            return;
        }

        final String nif = nifInstituicao();
        final int[] contagem = new int[3]; // 0 validadas, 1 inválidas, 2 ainda pendentes
        setOcupado(true, null);
        barra.setIndeterminate(false);
        barra.setValue(0);

        SwingWorker<Void, Progresso> worker = new SwingWorker<Void, Progresso>() {
            @Override
            protected Void doInBackground() throws Exception {
                int feitos = 0;
                for (Integer row : linhas) {
                    TbVenda venda = vendas.get(row);
                    ConsultaEstadoDTO r = ConsultaEstadoFacturaUtil.consultarEActualizar(
                            venda, nif, vendasController, conexao.getConnectionAtiva());
                    feitos++;
                    setProgress(feitos * 100 / linhas.size());
                    publish(new Progresso(row, r));

                    // E98 = demasiados pedidos -> parar e tentar mais tarde
                    if ("E98".equals(r.getErrorCode())) {
                        break;
                    }
                    Thread.sleep(1000); // pausa entre chamadas
                }
                return null;
            }

            @Override
            protected void process(List<Progresso> chunks) {
                for (Progresso p : chunks) {
                    actualizarLinha(p.row);
                    registarResultado(p.dto);
                    if ("V".equals(p.dto.getEstadoLocal())) {
                        contagem[0]++;
                    } else if ("I".equals(p.dto.getEstadoLocal())) {
                        contagem[1]++;
                    } else {
                        contagem[2]++;
                    }
                    lblResumo.setText("V: " + contagem[0] + "  I: " + contagem[1] + "  P: " + contagem[2]);
                }
            }

            @Override
            protected void done() {
                try {
                    get();
                } catch (Exception e) {
                    e.printStackTrace();
                    log("Erro no lote: " + e.getMessage());
                }
                setOcupado(false, "V: " + contagem[0] + "  I: " + contagem[1] + "  P: " + contagem[2]);
                JOptionPane.showMessageDialog(FormularioConsultaEstadoFE.this,
                        "Concluído.\nValidadas: " + contagem[0]
                        + "\nInválidas: " + contagem[1]
                        + "\nAinda pendentes: " + contagem[2]);
            }
        };

        // barra de progresso ligada ao setProgress do worker
        worker.addPropertyChangeListener(evt -> {
            if ("progress".equals(evt.getPropertyName())) {
                barra.setValue((Integer) evt.getNewValue());
            }
        });
        worker.execute();
    }

    // =====================================================
    // ENVIAR PENDENTES PARA AGT
    // =====================================================
    /**
     * Envia para a AGT as Facturas (FT), Facturas Recibo (FR), Notas de Crédito
     * (NC) e Recibos (RC) da BD local que NÃO constam da última pesquisa no
     * portal (mesmo período), por ordem cronológica. Não cria nem altera
     * vendas: só actualiza requestID / estado / submissionUUID com o que a AGT
     * devolveu (registarFactura + obterEstado).
     */
    private void enviarPendentesParaAGT() {
        if (!agtListaCompleta || agtPesquisaInicio == null || agtPesquisaFim == null) {
            JOptionPane.showMessageDialog(this,
                    "Faça primeiro uma pesquisa completa na AGT (botão \"Pesquisar na AGT\").\n"
                    + "Só se envia depois de saber o que já está no portal, para não repetir facturas.");
            return;
        }

        // Mesmo período da pesquisa na AGT (não o que estiver agora nas datas)
        final Date inicio = inicioDoDia(agtPesquisaInicio, 0);
        final Date fimExclusivo = inicioDoDia(agtPesquisaFim, 1);
        final SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
        final String periodo = f.format(agtPesquisaInicio) + " a " + f.format(agtPesquisaFim);

        // Nºs que já estão no portal + os já enviados nesta janela
        final java.util.Set<String> jaExistem = new java.util.HashSet<>();
        for (FacturaRemotaDTO r : facturasAGT) {
            if (r.getDocumentNo() != null) {
                jaExistem.add(r.getDocumentNo().trim());
            }
        }
        jaExistem.addAll(enviadasNestaSessao);

        setOcupado(true, "A comparar a BD local com a AGT...");

        new SwingWorker<List<TbVenda>, Void>() {
            @Override
            protected List<TbVenda> doInBackground() {
                List<Integer> codigos = vendasController.findCodigosVendasParaComunicarAGT(
                        conexao.getConnectionAtiva(), inicio, fimExclusivo);
                List<TbVenda> faltam = new ArrayList<>();
                for (Integer cod : codigos) {
                    TbVenda v = (TbVenda) vendasController.findById(cod);
                    if (v != null && v.getCodFact() != null
                            && !jaExistem.contains(v.getCodFact().trim())) {
                        faltam.add(v);
                    }
                }
                return faltam;
            }

            @Override
            protected void done() {
                List<TbVenda> faltam;
                try {
                    faltam = get();
                } catch (Exception e) {
                    e.printStackTrace();
                    log("Erro ao comparar com a AGT: " + e.getMessage());
                    setOcupado(false, " ");
                    return;
                }
                setOcupado(false, " ");
                confirmarEEnviarParaAGT(faltam, periodo);
            }
        }.execute();
    }

    private void confirmarEEnviarParaAGT(final List<TbVenda> pendentes, String periodo) {
        if (pendentes.isEmpty()) {
            log("Envio AGT (" + periodo + "): todas as facturas locais já constam no portal.");
            JOptionPane.showMessageDialog(this,
                    "Todos os documentos (FT/FR/NC/RC) de " + periodo + " já constam no portal da AGT.\n"
                    + "Nada para enviar.");
            return;
        }

        StringBuilder lista = new StringBuilder();
        for (int i = 0; i < pendentes.size() && i < 15; i++) {
            lista.append("  • ").append(pendentes.get(i).getCodFact()).append("\n");
        }
        if (pendentes.size() > 15) {
            lista.append("  ... e mais ").append(pendentes.size() - 15).append("\n");
        }

        int ok = JOptionPane.showConfirmDialog(this,
                pendentes.size() + " documento(s) da BD local NÃO constam no portal da AGT:\n\n"
                + lista
                + "\nAmbiente: " + (FEConfig.isHomologacao() ? "HOMOLOGAÇÃO" : "PRODUÇÃO")
                + "\nPeríodo: " + periodo
                + "\n\nEnviar agora para a AGT?"
                + "\n(na BD local só são actualizados o RequestID e o estado devolvidos pela AGT)",
                "Confirmar Envio", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (ok != JOptionPane.YES_OPTION) {
            return;
        }

        final TbDadosInstituicao dadosInst = dadosInstituicaoController.findByCodigo(1);
        final int[] contagem = new int[3]; // 0 enviadas, 1 já existiam, 2 erro
        final List<String> numerosNaAGT = new ArrayList<>(); // enviadas + já existentes
        setOcupado(true, null);
        barra.setIndeterminate(false);
        barra.setValue(0);
        log("A enviar " + pendentes.size() + " factura(s) para a AGT (" + periodo + ")...");

        SwingWorker<Void, String> worker = new SwingWorker<Void, String>() {
            @Override
            protected Void doInBackground() throws Exception {
                PayloadFactory.setModoSilencioso(true); // sem janelas por cada factura
                util.fe.http.HttpClientUtil.setTimeoutsThread(10000, 20000); // lote: sem pressa
                AgtConectividade.invalidarCache();
                try {
                    int feitos = 0;
                    for (TbVenda venda : pendentes) {
                        enviarUma(venda);
                        feitos++;
                        setProgress(feitos * 100 / pendentes.size());
                        if (feitos < pendentes.size()) {
                            Thread.sleep(800); // pausa entre envios
                        }
                    }
                } finally {
                    util.fe.http.HttpClientUtil.limparTimeoutsThread();
                    PayloadFactory.setModoSilencioso(false);
                }
                return null;
            }

            private void enviarUma(TbVenda venda) {
                // Mesmo envio que o modo contingência (ComunicacaoPendentesFE)
                ComunicacaoPendentesFE.ResultadoEnvio r = ComunicacaoPendentesFE.enviarVenda(
                        venda, dadosInst, conexao, vendasController, documentosController,
                        clientesController, itensController);
                switch (r.tipo) {
                    case DUPLICADA:
                        // a AGT diz que o nº já está no repositório: não é erro, não repete
                        contagem[1]++;
                        numerosNaAGT.add(venda.getCodFact());
                        break;
                    case ENVIADA:
                        contagem[0]++;
                        numerosNaAGT.add(venda.getCodFact());
                        break;
                    default:
                        contagem[2]++;
                        break;
                }
                publish(r.mensagem);
            }

            @Override
            protected void process(List<String> chunks) {
                for (String msg : chunks) {
                    log(msg);
                }
                lblResumo.setText("Enviadas: " + contagem[0] + "  Já existiam: " + contagem[1]
                        + "  Erro: " + contagem[2]);
            }

            @Override
            protected void done() {
                try {
                    get();
                } catch (Exception e) {
                    e.printStackTrace();
                    log("Erro no envio em lote: " + e.getMessage());
                }
                enviadasNestaSessao.addAll(numerosNaAGT);
                String resumo = "Enviadas: " + contagem[0] + "  Já existiam: " + contagem[1]
                        + "  Erro: " + contagem[2];
                log("Envio AGT concluído - " + resumo);
                setOcupado(false, resumo);
                JOptionPane.showMessageDialog(FormularioConsultaEstadoFE.this,
                        "Envio concluído.\n\nEnviadas para a AGT: " + contagem[0]
                        + "\nJá existiam no repositório (não repetidas): " + contagem[1]
                        + "\nCom erro: " + contagem[2]
                        + (contagem[2] > 0 ? "\n\nVeja os erros no Registo." : "")
                        + "\n\nA lista do Portal AGT vai ser actualizada.");
                pesquisarNaAGT(); // confirmar o que ficou no portal
            }
        };

        worker.addPropertyChangeListener(evt -> {
            if ("progress".equals(evt.getPropertyName())) {
                barra.setValue((Integer) evt.getNewValue());
            }
        });
        worker.execute();
    }

    // =====================================================
    // MODO CONTINGÊNCIA: COMUNICAR FACTURAS EM FALTA
    // =====================================================
    /**
     * Envia à AGT todas as facturas emitidas sem ligação (estado 'P' sem
     * RequestID), por ordem cronológica, e depois consulta as que aguardam
     * validação. Não precisa de pesquisa prévia no portal: os números já
     * existentes na AGT são detectados (duplicado) e não se repetem.
     */
    private void comunicarFacturasEmFalta() {
        final int porComunicar = vendasController.contarPorComunicarAGT(conexao.getConnectionAtiva());
        final int aguardam = vendasController.findCodigosVendasAguardandoAGT(conexao.getConnectionAtiva()).size();
        if (porComunicar == 0 && aguardam == 0) {
            JOptionPane.showMessageDialog(this, "Não há facturas por comunicar nem a aguardar validação da AGT.");
            return;
        }
        int ok = JOptionPane.showConfirmDialog(this,
                "Facturas emitidas sem ligação (por comunicar): " + porComunicar
                + "\nFacturas submetidas a aguardar validação: " + aguardam
                + "\n\nAmbiente: " + (FEConfig.isHomologacao() ? "HOMOLOGAÇÃO" : "PRODUÇÃO")
                + "\n\nComunicar agora com a AGT?",
                "Comunicar facturas em falta", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (ok != JOptionPane.YES_OPTION) {
            return;
        }

        setOcupado(true, null);
        barra.setIndeterminate(false);
        barra.setValue(0);
        log("Comunicar facturas em falta: " + porComunicar + " por comunicar, " + aguardam + " a aguardar validação...");

        SwingWorker<ComunicacaoPendentesFE.Resumo, String> worker
                = new SwingWorker<ComunicacaoPendentesFE.Resumo, String>() {
            @Override
            protected ComunicacaoPendentesFE.Resumo doInBackground() {
                return ComunicacaoPendentesFE.comunicarPorEnviar(conexao, false, (feitos, total, msg) -> {
                    setProgress(total == 0 ? 100 : Math.min(100, feitos * 100 / total));
                    publish(msg);
                });
            }

            @Override
            protected void process(List<String> chunks) {
                for (String msg : chunks) {
                    log(msg);
                }
            }

            @Override
            protected void done() {
                ComunicacaoPendentesFE.Resumo r;
                try {
                    r = get();
                } catch (Exception e) {
                    e.printStackTrace();
                    log("Erro ao comunicar: " + e.getMessage());
                    setOcupado(false, " ");
                    return;
                }
                if (r.ocupado || (r.semLigacao && r.enviadas == 0)) {
                    for (String msg : r.registo) {
                        log(msg);
                    }
                }
                log("Comunicação concluída - " + r);
                setOcupado(false, r.toString());
                int faltam = vendasController.contarPorComunicarAGT(conexao.getConnectionAtiva());
                JOptionPane.showMessageDialog(FormularioConsultaEstadoFE.this,
                        (r.ocupado ? "Já está a decorrer uma comunicação automática. Tente daqui a pouco.\n\n" : "")
                        + (r.semLigacao ? "ATENÇÃO: sem ligação à AGT (internet ou serviço indisponível).\n\n" : "")
                        + "Enviadas: " + r.enviadas
                        + "\nJá existiam no repositório: " + r.duplicadas
                        + "\nCom erro: " + r.erros
                        + "\nValidadas (V): " + r.validadas
                        + "\nInválidas (I): " + r.invalidadas
                        + "\n\nAinda por comunicar: " + faltam
                        + (r.erros > 0 ? "\n\nVeja os erros no Registo." : ""));
                carregar(); // actualizar a lista local
            }
        };
        worker.addPropertyChangeListener(evt -> {
            if ("progress".equals(evt.getPropertyName())) {
                barra.setValue((Integer) evt.getNewValue());
            }
        });
        worker.execute();
    }

    // =====================================================
    // COR DA COLUNA ESTADO
    // =====================================================
    private static class EstadoRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (!isSelected) {
                String s = String.valueOf(value);
                if (s.startsWith("Validado")) {
                    c.setForeground(new Color(0, 130, 0));
                } else if ("Inválido".equals(s)) {
                    c.setForeground(new Color(190, 0, 0));
                } else if ("Pendente".equals(s)) {
                    c.setForeground(new Color(200, 120, 0));
                } else {
                    c.setForeground(table.getForeground()); // "-" na tabela AGT
                }
            }
            setHorizontalAlignment(CENTER);
            return c;
        }
    }

    // Cor da coluna "Existe na BD local?" (tabela AGT)
    private static class ExisteLocalRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (!isSelected) {
                c.setForeground(String.valueOf(value).startsWith("Sim")
                        ? new Color(0, 130, 0)
                        : new Color(190, 0, 0));
            }
            setHorizontalAlignment(CENTER);
            return c;
        }
    }
}
