/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package visao.venda;

import comercial.controller.*;
import dao.ItemPermissaoDao;
import entity.AnoEconomico;
import entity.Cambio;
import entity.Contas;
import entity.TbDesconto;
import entity.Documento;
import entity.Familia;
import entity.FormaPagamento;
import entity.FormaPagamentoItem;
import entity.TbItemVenda;
import entity.Moeda;
import entity.PagamentoMensalidade;
import entity.TbArmazem;
import entity.TbBanco;
import entity.TbCliente;
import entity.TbDadosInstituicao;
import entity.TbItemPedidos;
import entity.TbItemPermissao;
import entity.TbLugares;
import entity.TbMesas;
import entity.TbPedido;
import entity.TbPreco;
import entity.TbProduto;
import entity.TbStock;
import entity.TbTipoProduto;
import entity.TbUsuario;
import entity.TbVenda;
import entity.Unidade;
import exemplos.PermitirNumeros;
import hotel.controller.MesasController;
import java.awt.Color;
import java.awt.Component;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.print.PrinterJob;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.SimpleDoc;
import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import kitanda.util.CfMethods;
import kitanda.util.CfMethodsSwing;
import lista.ListaVenda1;
import lista.ListaVendaConsultas;
import tesouraria.novo.controller.ContaController;
import tesouraria.novo.controller.ContaMovimentosController;
import tesouraria.novo.util.MetodosUtilTS;
import util.BDConexao;
import util.DVML;
import util.DVML.Abreviacao;
import static util.DVML.CASAS_DECIMAIS;
import static util.DVML.DOC_FACTURA_CONSULTA_MESA;
import static util.DVML.DOC_FACTURA_RECIBO_FR;
import static util.DVML.DOC_FACTURA_FT;
import util.FinanceUtils;
import util.MetodosUtil;
import util.TemaNegocioUtil;
import visao.*;
import visao.design.Formularios;
import visao.design.Tema;

/**
 *
 * @author marti
 */
public class VendaModernaVisao extends javax.swing.JFrame {

    /**
     * CONTROLLER COMERCIAL
     */
    private static VendasController vendasController;
    private static ItemVendasController itemVendasController;
    private static FamiliasController familiaController;
    private static PrecosController precosController;
    private static LugaresController lugaresController;
    private static ProdutosController produtosController;
    private static StoksController stocksController;
    private static MesasController mesasController;
    private static ArmazensController armazensController;
    private static TipoProdutosController tipoProdutoController;
    private static LocalController localController;
    private static UnidadesController unidadesController;
    private static AnoEconomicoController anoEconomicoController;
    private static ClientesController clientesController;
    private static DocumentosController documentosController;
    private static MoedasController moedasController;
    private static DescontosController descontosController;
    private static CambiosController cambiosController;
    private static UsuariosController usuariosController;
    private static DadosInstituicaoController dadosInstituicaoController;
    private static ProdutosImpostoController produtosImpostoController;
    private static ProdutosIsentoController produtosIsentoController;
    private static CaixasController caixasController;
    private static ServicosRetencaoController servicosRetencaoController;
    private static ArmazensAccessoController armazensAccessoController;
    private static FormaPagamentoItemController formaPagamentoItemController;
    private static FormaPagamentoController formaPagamentoController;
    private static ContaController contaController;
    private static MovimentacaoController movimentacaoController;
    private static ContaMovimentosController cmc;
    private static TbPreco precoUnitario;
    private static ConfiguracaoMesComecoController configuracaoMesComecoController;
    private static PagamentoMensalidadeController pagamentoMensalidadeController;
    private static MesRhController mesRhController;
    private static RetencaoController retencaoController;
    private static ReferenciasController referenciasController;
    private static PedidosController pedidosController;
    private static ItemPedidosController itemPedidosController;
    private static LoteController loteController;

    /**
     * OUTROS
     */
    private static TbStock stock_local;
    public static ItemPermissaoDao itemPermissaoDao;
    private static AnoEconomico anoEconomico;
    private static BDConexao conexao;
    private static Documento documento;
    private static Cambio cambio;
    private static int cod_usuario;
    private static int linha = 0, doc_prox_cod = 0;
    private static double total_iva = 0;
    private static double total_ret = 0;
    private static int linha_actual = -1;
    private static DVML.Abreviacao abreviacao;
    private static double total_iliquido = 0, total_desconto_linha = 0;
    private static boolean aviso_continuar_documento = false;
    private static String prox_doc;
    public static double gorjeta = 0;
    private static TbDadosInstituicao dadosInstituicao;
    private static BDConexao conexaoTransaction;

    private static final int INDEX_TABLE_PRECO = 3;
    private static final int INDEX_TABLE_QTD = 4;
    private static final int INDEX_TABLE_DESCONTO = 5;
    private static final int INDEX_TABLE_TAXA_IVA = 6;
    private static int linha_existente_produto;

    private boolean permitirAlterarPreco = false;

    private static List<Vector<TbPreco>> listaPrecoTemp = new ArrayList<>();

    private String doc = "";
    public static boolean statusConversao = false;
    private static boolean descontoStocavel = true;
    public static Vector<String> consultaDoc = new Vector();
    private String folhaEscolhida = null; // null = utilizador não clicou
    private static PainelLotesVenda painelLotes;
    private static VendaModernaVisao instancia;
    // campo da classe:
    private final javax.swing.Timer timerQtdLotes
            = new javax.swing.Timer(200, e -> atualizarPainelLotesProdutoPesquisado());

    public VendaModernaVisao(int cod_usuario, BDConexao conexao) throws SQLException {
        initComponents();

        timerQtdLotes.setRepeats(false);
        txtQuatindade.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                timerQtdLotes.restart();
            }

            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                timerQtdLotes.restart();
            }

            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                timerQtdLotes.restart();
            }
        });

        ligarPainelLotesASelecaoDaTabela();

        // Atualizar quando o produto selecionado mudar
        cmbProduto.addActionListener(evt -> {
            atualizarPainelLotesProdutoPesquisado();
        });

        // Atualizar quando selecionar uma linha da jtable da venda
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                atualizarPainelLotesDaLinhaSelecionada();
            }
        });

        configurarTxtBuscaRefMaiusculo();
//        procedimento_codBarra__jtable();

        this.setExtendedState(JFrame.MAXIMIZED_BOTH); // abre maximizado

        btnSemFormaPagamento.setVisible(false);
        cmbMoeda.setVisible(false);
        rbArmazem.setVisible(false);
        rbArmazem1.setVisible(false);
        rbMostrar.setVisible(false);
        jlStockNegativo.setVisible(false);
        rbTranstorno.setVisible(false);
        txtLocal.setVisible(false);
        lb_proximo_documento.setVisible(false);

        //pegarResolucao();
        VendaModernaVisao.conexao = conexao;

        /**
         * INSTANCIAS DOS CONTROLLER COMERCIAL
         */
        vendasController = new VendasController(VendaModernaVisao.conexao);
        itemVendasController = new ItemVendasController(VendaModernaVisao.conexao);
        mesasController = new MesasController(VendaModernaVisao.conexao);
        lugaresController = new LugaresController(VendaModernaVisao.conexao);
        produtosController = new ProdutosController(VendaModernaVisao.conexao);
        stocksController = new StoksController(VendaModernaVisao.conexao);
        precosController = new PrecosController(VendaModernaVisao.conexao);
        tipoProdutoController = new TipoProdutosController(VendaModernaVisao.conexao);
        familiaController = new FamiliasController(VendaModernaVisao.conexao);
        armazensController = new ArmazensController(VendaModernaVisao.conexao);
        localController = new LocalController(VendaModernaVisao.conexao);
        unidadesController = new UnidadesController(VendaModernaVisao.conexao);
        anoEconomicoController = new AnoEconomicoController(VendaModernaVisao.conexao);
        clientesController = new ClientesController(VendaModernaVisao.conexao);
        documentosController = new DocumentosController(VendaModernaVisao.conexao);
        moedasController = new MoedasController(VendaModernaVisao.conexao);
        descontosController = new DescontosController(VendaModernaVisao.conexao);
        usuariosController = new UsuariosController(VendaModernaVisao.conexao);
        dadosInstituicaoController = new DadosInstituicaoController(VendaModernaVisao.conexao);
        produtosImpostoController = new ProdutosImpostoController(VendaModernaVisao.conexao);
        produtosIsentoController = new ProdutosIsentoController(VendaModernaVisao.conexao);
        caixasController = new CaixasController(VendaModernaVisao.conexao);
        formaPagamentoController = new FormaPagamentoController(VendaModernaVisao.conexao);
        armazensAccessoController = new ArmazensAccessoController(VendaModernaVisao.conexao);
        cambiosController = new CambiosController(VendaModernaVisao.conexao);
        formaPagamentoItemController = new FormaPagamentoItemController(VendaModernaVisao.conexao);
        servicosRetencaoController = new ServicosRetencaoController(VendaModernaVisao.conexao);
        contaController = new ContaController(VendaModernaVisao.conexao);
        movimentacaoController = new MovimentacaoController(conexao.getConnection());
        dadosInstituicao = (TbDadosInstituicao) dadosInstituicaoController.findById(1);
        configuracaoMesComecoController = new ConfiguracaoMesComecoController(conexao.getConnectionAtiva());
        pagamentoMensalidadeController = new PagamentoMensalidadeController(conexao.getConnectionAtiva());
        itemPedidosController = new ItemPedidosController(conexao.getConnectionAtiva());
        pedidosController = new PedidosController(conexao.getConnectionAtiva());
        referenciasController = new ReferenciasController(conexao);
        mesRhController = new MesRhController(conexao.getConnectionAtiva());
        loteController = new LoteController(conexao);
        cmc = new ContaMovimentosController(conexao);
        txtQuatindade.setText("1");
//        txtQuatindade.setDocument( new PermitirNumeros() );

        this.cod_usuario = cod_usuario;
//        lbPreco7.setVisible( false );
        jlStockNegativo.setVisible(false);
        rbTranstorno.setVisible(false);
        rbMostrar.setVisible(false);
        rbArmazem1.setVisible(false);
        rbArmazem.setVisible(false);
        txtTotalPagarRetencao.setVisible(false);
        try {

            init();

        } catch (Exception e) {
            e.printStackTrace();
        }
        //pega_ultima_contagem();

        KeyboardFocusManager.getCurrentKeyboardFocusManager()
                .addKeyEventDispatcher(new KeyEventDispatcher() {
                    @Override
                    public boolean dispatchKeyEvent(KeyEvent e) {
                        // if ( e.getID() == e.KEY_RELEASED && e.getKeyCode() == KeyEvent.VK_TAB )
                        if (e.getID() == e.KEY_RELEASED && e.getKeyCode() == KeyEvent.VK_TAB) {
//                            txtValorEntregue.requestFocus();
                            return true;

                        }
                        return false;
                    }
                });
        //new BuscaProdutoVisao( VendaModernaVisao.this, rootPaneCheckingEnabled, getCodigoArmazem(), DVML.JANELA_VENDA ).setVisible(true);
//        MetodosUtil.FUNCAO_F1( this, rootPaneCheckingEnabled, getCodigoArmazem(), DVML.JANELA_VENDA);

        // No construtor ou método de inicialização do formulário
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke("F4"), "abrirBuscaProduto");

        getRootPane().getActionMap().put("abrirBuscaProduto", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (validar()) {
                        new BuscaProdutoVisao(
                                VendaModernaVisao.this,
                                rootPaneCheckingEnabled,
                                getCodigoArmazem(),
                                DVML.JANELA_VENDA,
                                BDConexao.getInstancia()
                        ).setVisible(true);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        // No construtor ou método de inicialização do formulário
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke("F5"), "abrirFormaPagamento");

        getRootPane().getActionMap().put("abrirFormaPagamento", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // ✅ Só executa se o botão estiver ativo
                    if (btnFormaPagamento.isEnabled()) {
                        procedimentoChamarFormaPagemnto();
                    } else {
                        // Opcional: feedback ao usuário
                        System.out.println("Botão desactivado. A acção não pode ser executada.");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        // No construtor ou método de inicialização do formulário
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke("F7"), "abrirDocumentosPendentes");

        getRootPane().getActionMap().put("abrirDocumentosPendentes", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    procedimentoCharFacturasPendentes();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

        });

        MetodosUtil.setArmazemByCampoConfigArmazem(cmbArmazem, conexao, cod_usuario);
        inserirLinhaEmBranco();

        System.err.println("####STATUS ALterar Preco: " + dadosInstituicao.getAlterarPreco());
        if (permitirAlterarPreco) {
            System.err.println("####Permitir ALterar Preco: " + permitirAlterarPreco);
            configurarPrecoTabela();
        } else {
            System.err.println("####Não Permitir ALterar Preco: " + permitirAlterarPreco);
            configurarPrecosQTDTabela();
        }

        procedimento_codBarra__jtable();
        txtCodigoBarra.requestFocus();

//        txtBuscaRef.setEnabled(false);
        // Mostrar o painel de lotes apenas para Farmácia
//        if (dadosInstituicao != null && "Farmacia".equalsIgnoreCase(dadosInstituicao.getNegocio())) {
//            painelLotes = PainelLotesVenda.instalar(
//                    PainelLotesVenda.encontrarPainelDoBotao(
//                            getContentPane(), "Fecho"
//                    ),
//                    560
//            );
//        }
boolean ehFarmacia = dadosInstituicao != null
        && TemaNegocioUtil.ehFarmacia(dadosInstituicao.getNegocio());

if (ehFarmacia) {
    // o painel de lotes entra como cartão na coluna do total
    painelLotes = new PainelLotesVenda();
    layoutVenda.mostrarLotes(painelLotes);
}

// Os campos do kit visual usam um tom suave desta cor e o anel de foco.
Tema.definirCorNegocio(TemaNegocioUtil.corDoNegocio(ehFarmacia));
repaint();

//aplicarCorCamposVenda(ehFarmacia);

        aplicarFolha();

    }

    private void init() {
        // Dentro do construtor ou depois do initComponents():
        this.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke("F5"), "abrirFormaPagamento");

        this.getRootPane().getActionMap().put("abrirFormaPagamento", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirFormaPagamento(); // chama o método abaixo
            }
        });

        cmbMoeda.setVisible(false);
        txtIniciaisCliente.addKeyListener(new TratarEventoTeclado());

        txtCodigoProduto.setDocument(new PermitirNumeros());
        txtCodigoBarra.setDocument(new PermitirNumeros());
        lbValorPorExtenco.setText("");
        mostrar_nome();
        try {
            configurar_armazens();
        } catch (Exception e) {
            e.printStackTrace();
        }

        cmbSubFamilia.setModel(new DefaultComboBoxModel(tipoProdutoController.getVector()));
        cmbProduto.setModel(new DefaultComboBoxModel(produtosController.getVector()));
        cmbMoeda.setModel(new DefaultComboBoxModel(moedasController.getVector()));
        txtCodClientePesquisa.setDocument(new PermitirNumeros());

        cmbMoeda.setSelectedIndex(0);
        cmbCliente.setModel(new DefaultComboBoxModel(clientesController.getVector()));
        cmbCliente.setSelectedItem(DVML._CLIENTE_CONSUMIDOR_FINAL);

//        cmbFamilia.setModel( new DefaultComboBoxModel( familiaController.getVector() ) );
        cmbTipoDocumento.setModel(new DefaultComboBoxModel(documentosController.getVector()));
        cmbAnoEconomico.setModel(new DefaultComboBoxModel(anoEconomicoController.getVector()));
        txtQuatindade.setText("1");
        txtQuatindade.requestFocus();
        dc_data_documento.setDate(new Date());
        mostrar_ano_economico_serie();
        lb_proximo_documento.setText("");
        txtTotalPagar.setText(CfMethods.formatarComoMoeda(0.0));

        reset_valor_entregue();
        reset_desconto_global();

        setDocpadrao(dadosInstituicao.getDocpadrao());
        setDesactivarvias(dadosInstituicao.getDesactivarvias());
//        setEditarPrecos( dadosInstituicao.getEditarPreco() );
//        setActivarNegocio( dadosInstituicao.getNegocio() );
        setArmazem(dadosInstituicao.getConfigArmazens());
        setTranstorno(dadosInstituicao.getTranstorno());
        setActivarDescontoFinanceiro(dadosInstituicao.getDescontoFinanceiro());
        setAlterarPreco(dadosInstituicao.getAlterarPreco());
        setAnoEconomico(dadosInstituicao.getAnoEconomico());
        setVizualisarStock(dadosInstituicao.getVizualisarStock());
        int numero_copia = dadosInstituicao.getNumeroVias();
        spnCopia.setModel(CfMethodsSwing.criarSpinnerDoubleModel(1, 3, numero_copia));
        empresa();

        instancia = this;
//        aplicarFolha();
        configurarEscolhaFolha();

//        setFolhaImpressora(dadosInstituicao.getImpressora());
        busca_permissao();
        actualizar_abreviacao();

        setWindowsListener();
//        alterar_status_botao();

    }// </editor-fold>   

    private void mostrar_ano_economico_serie() {
        anoEconomico = anoEconomicoController.getLastObject();

    }

    private void setAlterarPreco(String alterarPreco) {

        if (alterarPreco.equalsIgnoreCase("Bloquear")) {
            permitirAlterarPreco = false;
        } else {
            permitirAlterarPreco = true;
        }

    }

    public VendaModernaVisao(int cod_usuario, BDConexao conexao, String docPadraoPersonalizado) throws SQLException {
        this(cod_usuario, conexao); // chama o construtor original

        // Agora força o documento padrão personalizado
        setDocpadrao(docPadraoPersonalizado);

        if (docPadraoPersonalizado.equalsIgnoreCase("Factura-Proforma")) {
            cmbTipoDocumento.setEnabled(false);
        }
    }

    /**
     * true se esta janela de vendas está aberta: as lupas e diálogos
     * (BuscaProdutoVisao, FormaPagamentoVisao...) usam-no para devolver os
     * dados aqui em vez de no FormVendaResponsivaVisaoTop.
     */
    public static boolean estaAberta() {
        return instancia != null && instancia.isDisplayable();
    }

    public JFrame getInstance() {
        return this;
    }

    public void keypressed(java.awt.event.KeyEvent evt) {

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            txtQuatindade.requestFocus();

        }

    }

    public void keyTyped(KeyEvent evt) {

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            dispose();
        }

    }

    class TratarEventoTeclado implements KeyListener {

        String prefixo = "";

        public void keyPressed(KeyEvent evt) {

            if (evt.getKeyCode() != KeyEvent.VK_BACK_SPACE && evt.getKeyCode() != KeyEvent.VK_ENTER) {
                char key = evt.getKeyChar();

                try {
                    prefixo = txtIniciaisCliente.getText().trim() + key;
                    cmbCliente.setModel(new DefaultComboBoxModel(clientesController.getVectorByIinciais(prefixo)));

                } catch (Exception e) {
                    cmbCliente.setSelectedIndex(0);
                }

            } else if (evt.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                try {
                    prefixo = prefixo.toString().trim().substring(0, prefixo.length() - 1);
                    cmbCliente.setModel(new DefaultComboBoxModel(clientesController.getVectorByIinciais(prefixo)));
                } catch (Exception e) {
                    cmbCliente.setSelectedIndex(0);
                }

            }
        }

        public void keyReleased(KeyEvent evt) {
        }

        public void keyTyped(KeyEvent evt) {
        }

    }

    /**
     * Actualiza o painel de lotes para (produto, quantidade).
     *
     * @return null se ha stock suficiente (ou o produto nao tem lotes); senao o
     * texto do erro (o painel, se existir, ja o mostra a vermelho; quem quiser
     * bloquear mostra tambem uma janela)
     */
    private static String actualizarPainelLotes(int codProduto, String descricao, double qtd) throws SQLException {

        // produto sem lotes registados (ex.: negocio que nao usa lotes):
        // nada a mostrar nem a validar
        if (!loteController.existeLotes(codProduto)) {
            if (painelLotes != null) {
                painelLotes.limpar();
            }
            return null;
        }
        int armazem = getCodigoArmazem();

        // todos os lotes com stock, por ordem FEFO
        List<entity.Lote> disponiveis = loteController.listarDisponiveisFEFO(codProduto, armazem);
        if (disponiveis.isEmpty()) {
            String msg = "Este produto tem lotes registados, mas não possui lotes disponíveis, "
                    + "activos e dentro da validade no armazém selecionado.";
            if (painelLotes != null) {
                painelLotes.mostrarAviso(descricao, msg);
            }
            return msg;
        }

        // quanto sai de cada lote para a quantidade dada (por id do lote)
        java.util.Map<Integer, Double> aRetirar = new java.util.HashMap<>();
        String erro = null;
        try {
            for (LoteController.LoteFEFO item : loteController.preverBaixaFEFO(codProduto, armazem, qtd)) {
                aRetirar.put(item.getLote().getId(), (double) item.getQuantidadeRetirar());
            }
        } catch (IllegalStateException e) {
            erro = e.getMessage();
        }

        List<PainelLotesVenda.Linha> linhas = new java.util.ArrayList<>();
        for (entity.Lote l : disponiveis) {
            linhas.add(new PainelLotesVenda.Linha(
                    l.getNumeroLote(),
                    l.getDataValidade(),
                    l.getQtd(),
                    aRetirar.getOrDefault(l.getId(), 0.0)));
        }

        if (erro != null) {
            if (painelLotes != null) {
                painelLotes.mostrarAviso(descricao, erro, linhas);
            }
            return erro;
        }
        if (painelLotes != null) {
            painelLotes.mostrar(descricao, qtd, linhas);
        }
        return null;
    }

// ---------------------------------------------------------------------
// 3) PESQUISA: substitui TODO o teu atualizarPainelLotesProdutoPesquisado()
//    por este, e acrescenta o metodo quantidadeDoProdutoNaVenda().
//
//    REGRA: a quantidade do plano e
//      - a que JA ESTA NA VENDA, se o produto ja tem linha na JTable
//        (e o que vai sair de facto: ex. 17 -> 14 + 3);
//      - senao, a que o operador esta a escrever no campo Qtd.
//    Assim o painel nunca volta a "1" so porque o campo Qtd foi reposto a 1
//    depois de adicionar, seja qual for o evento que dispare a pesquisa.
// ---------------------------------------------------------------------
    private void atualizarPainelLotesProdutoPesquisado() {

        if (painelLotes == null) {
            return;
        }

        if (cmbProduto.getSelectedItem() == null) {
            painelLotes.limpar();
            return;
        }

        try {
            int codigoProdutoLocal = getCodigoProduto();
            String descricao_produto = getDescricao_Produto();

            if (codigoProdutoLocal <= 0) {
                painelLotes.limpar();
                return;
            }

            // 1.o: o produto ja esta na venda? entao o plano e o da venda
            double qtd = quantidadeDoProdutoNaVenda(codigoProdutoLocal);

            // 2.o: senao, a quantidade que o operador esta a escrever
            if (qtd <= 0) {
                String textoQtd = txtQuatindade.getText().trim();
                if (textoQtd.isEmpty()) {
                    painelLotes.limpar();
                    return;
                }
                qtd = Double.parseDouble(textoQtd.replace(",", "."));
                if (qtd <= 0) {
                    painelLotes.limpar();
                    return;
                }
            }

            // aqui nao ha janela: so mostra no painel
            actualizarPainelLotes(codigoProdutoLocal, descricao_produto, qtd);

        } catch (SQLException | NumberFormatException e) {
            painelLotes.limpar();
            e.printStackTrace();
        }
    }

    /**
     * Quantidade total deste produto que ja esta na JTable da venda (soma das
     * linhas; 0 se nao esta). Ignora linhas vazias ou em edicao. Colunas: 0 =
     * Codigo, 4 = Quantidade.
     */
    private double quantidadeDoProdutoNaVenda(int codProduto) {
        double total = 0;
        for (int i = 0; i < table.getRowCount(); i++) {
            try {
                int codigo = Integer.parseInt(String.valueOf(table.getValueAt(i, 0)).trim());
                if (codigo == codProduto) {
                    total += Double.parseDouble(String.valueOf(table.getValueAt(i, 4)).trim().replace(",", "."));
                }
            } catch (NumberFormatException e) {
                // linha vazia ou nao numerica: ignora
            }
        }
        return total;
    }

    private void setWindowsListener() {

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowActivated(WindowEvent e) {
//                mostrar_proximo_codigo_documento();

                try {
//                                    MetodosUtil.verificarCaixa(
//                        caixa_controller,
//                        idUser,
//                        RootVisao.btn_abertura_dia_root,
//                        RootVisao.btn_feicho_dia_root );

                    MetodosUtil.verificarCaixa(caixasController,
                            cod_usuario,
                            VendaModernaVisao.btn_abertura_dia_venda,
                            VendaModernaVisao.btn_fecho_dia_venda,
                            btnFormaPagamento, btnSemFormaPagamento);

                } catch (Exception ex) {
                    System.err.println("Não existe abertura para este usuário \n"
                            + ex.getMessage());
                }

            }

        });

    }

//        private void setWindowsListener1()
//    {
//        this.addWindowListener( new WindowAdapter()
//        {
//            @Override
//            public void windowActivated( WindowEvent e )
//            {
//                MetodosUtil.verificarCaixa(
//                        caixasController,
//                        cod_usuario,
//                        RootVisao.btn_abertura_dia_root,
//                        RootVisao.btn_feicho_dia_root );
//            }
//
//        } );
//
//    }
    /**
     * Metodo actualizado do proximo doumento
     */
//    private static void mostrar_proximo_codigo_documento()
//    {
//
//        try
//        {
//
//            //metodo actualizado
//            documento = ( Documento ) documentosController.findById( getIdDocumento( documentosController ) );
//            anoEconomico = ( AnoEconomico ) anoEconomicoController.findById( getIdAnoEconomico( anoEconomicoController ) );
//            doc_prox_cod = vendasController.getUltimaContagemByIdDocumentoAndAnoEconomico(
//                    getIdDocumento( documentosController ), getIdAnoEconomico( anoEconomicoController ) ) + 1;
//            prox_doc = documento.getAbreviacao();
//            //FA Série / codigo
//            prox_doc += " " + anoEconomico.getSerie() + "/" + doc_prox_cod;
    ////            lb_proximo_documento.setText( "PRÓX.DOC. :" + prox_doc );
//        }
//        catch ( Exception e )
//        {
//            documento = null;
//            lb_proximo_documento.setText( "" );
//        }
//    }
    private static void mostrar_proximo_codigo_documento() {
        try {
            documento = (Documento) documentosController.findById(
                    getIdDocumento(documentosController)
            );

            anoEconomico = (AnoEconomico) anoEconomicoController.findById(
                    getIdAnoEconomico(anoEconomicoController)
            );

            int proximoNumero = vendasController.getProximoNumeroFacturaComLock(
                    documento.getPkDocumento(),
                    anoEconomico.getPkAnoEconomico()
            );

            prox_doc = documento.getAbreviacao()
                    + " " + anoEconomico.getSerie()
                    + "/" + proximoNumero;

        } catch (Exception e) {
            e.printStackTrace();
            prox_doc = null;
        }
    }

//    private static void mostrar_proximo_codigo_documento()
//    {
//
//        try
//        {
//            BDConexao conexaoLocal = BDConexao.getInstancia();
//            documentosController = new DocumentosController( conexaoLocal );
//            anoEconomicoController = new AnoEconomicoController( conexaoLocal );
//            vendasController = new VendasController( conexaoLocal );
//
//            documento = ( Documento ) documentosController.findById( getIdDocumento( documentosController ) );
//            anoEconomico = ( AnoEconomico ) anoEconomicoController.findById( getIdAnoEconomico( anoEconomicoController ) );
//            doc_prox_cod = vendasController.getUltimaContagemByIdDocumentoAndAnoEconomico(
//                    getIdDocumento( documentosController ), getIdAnoEconomico( anoEconomicoController ) ) + 1;
//            prox_doc = documento.getAbreviacao();
//            //FA Série / codigo
//            prox_doc += " " + anoEconomico.getSerie() + "/" + doc_prox_cod;
    ////            lb_proximo_documento.setText( "PRÓX.DOC. :" + prox_doc );
//
//        }
//        catch ( Exception e )
//        {
//            documento = null;
//            lb_proximo_documento.setText( "" );
//
//        }
//
//    }
    /**
     * Constrói a interface em código (sem .form do NetBeans): os componentes
     * são criados em {@link VendaComponentes}, os eventos ligados em
     * {@link VendaEventos} e a disposição responsiva feita em
     * {@link VendaLayout}. Nomes de componentes e eventos são os mesmos do
     * VendaModernaVisao.
     */
    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("...::::: KITANDA - PONTO DE VENDA ::::...");

        new VendaComponentes(this).criar();
        new VendaEventos(this).ligar();
        layoutVenda = new VendaLayout(this);
        layoutVenda.montar();
    }

    void cmbClienteActionPerformed(java.awt.event.ActionEvent evt)
    {
        // TODO add your handling code here:
        accao_cliente();
        mostra_consumidor_final();
    }

    void txtIniciaisClienteActionPerformed(java.awt.event.ActionEvent evt)
    {
//        txtQuatindade.requestFocus();
    }

    void txtNomeConsumidorFinalActionPerformed(java.awt.event.ActionEvent evt)
    {
//        txtQuatindade.requestFocus();

    }

    void txtNomeConsumidorFinalKeyPressed(java.awt.event.KeyEvent evt)
    {
        // TODO add your handling code here:
    }

    void txtNifClientePesquisaActionPerformed(java.awt.event.ActionEvent evt)
    {
        pesquisa_cliente_by_nif();
    }

    void txtCodClientePesquisaActionPerformed(java.awt.event.ActionEvent evt)
    {
        pesquisa_cliente_by_cod();
    }

    void jButton5ActionPerformed(java.awt.event.ActionEvent evt)
    {
        new ClienteInternoVisao(this, rootPaneCheckingEnabled, BDConexao.getInstancia()).setVisible(true);
    }

    void txtTotalPagarActionPerformed(java.awt.event.ActionEvent evt)
    {
        // TODO add your handling code here:
    }

    void jButton4ActionPerformed(java.awt.event.ActionEvent evt)
    {

        try {
            if (validar()) {

                BuscaProdutoVisao busca = new BuscaProdutoVisao(
                        this,
                        rootPaneCheckingEnabled,
                        getCodigoArmazem(),
                        DVML.JANELA_VENDA,
                        BDConexao.getInstancia()
                );

                busca.setVisible(true);

                // Quando a janela de pesquisa fechar, atualizar
                // os lotes do produto que foi selecionado.
//            atualizarPainelLotesDepoisDaPesquisa();
                atualizarPainelLotesProdutoPesquisado();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

//        try {
//            if (validar()) {
//                new BuscaProdutoVisao(this, rootPaneCheckingEnabled,
//                        getCodigoArmazem(),
//                        DVML.JANELA_VENDA,
//                        BDConexao.getInstancia()).setVisible(true);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    void cmbProdutoActionPerformed(java.awt.event.ActionEvent evt)
    {
        // TODO add your handling code here:
        try {
            adicionar_preco_quantidade();
        } catch (Exception e) {
        }
    }

    private void pesquisa_cliente_by_cod() {

        Integer codCliente = Integer.parseInt(txtCodClientePesquisa.getText());
        try {

            TbCliente cliente = (TbCliente) clientesController.findById(codCliente);
            String nome_cliente = cliente.getNome();
            cmbCliente.setSelectedItem(nome_cliente.trim());
            accao_cliente();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Não existe cliente com código");
            cmbCliente.setSelectedItem("Consumidor Final");
        }
        txtCodClientePesquisa.setText("");
        txtCodClientePesquisa.requestFocus();
    }

    private void pesquisa_cliente_by_nif() {

        String nif = txtNifClientePesquisa.getText();
        try {
            String nome_cliente = clientesController.getClienteByNifOrberByNome(nif).getNome();
            cmbCliente.setSelectedItem(nome_cliente.trim());
            accao_cliente();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Não existe cliente com este código");
            cmbCliente.setSelectedItem("Consumidor Final");
        }
        txtNifClientePesquisa.requestFocus();
    }

    private void pesquisa_cliente_by_tel() {

//        String tel = txtTelClientePesquisa.getText();
//        try
//        {
//            String nome_cliente = clientesController.getClienteByTelOrberByNome( tel ).getNome();
//            cmbCliente.setSelectedItem( nome_cliente.trim() );
//            accao_cliente_tel();
//        }
//        catch ( Exception e )
//        {
//            e.printStackTrace();
//            JOptionPane.showMessageDialog( null, "Não existe cliente com este código" );
//            cmbCliente.setSelectedItem( "Consumidor Final" );
//        }
//        txtTelClientePesquisa.requestFocus();
    }

    void cmbArmazemActionPerformed(java.awt.event.ActionEvent evt)
    {
        // TODO add your handling code here:
//        adicionar_preco_quantidade();
        //        configurar_armazens();
    }

    void txtCodigoManualActionPerformed(java.awt.event.ActionEvent evt)
    {
        accao_codigo_manual_enter();
        atualizarPainelLotesProdutoPesquisado();
    }

    void cmbAnoEconomicoActionPerformed(java.awt.event.ActionEvent evt)
    {
        // TODO add your handling code here:
//        mostrar_proximo_codigo_documento();
        actualizar_abreviacao();
    }

    void rbTranstornoActionPerformed(java.awt.event.ActionEvent evt)
    {
        // TODO add your handling code here:
    }

    void rbMostrarActionPerformed(java.awt.event.ActionEvent evt)
    {
        // TODO add your handling code here:
    }

    void txtQuatindadeActionPerformed(java.awt.event.ActionEvent evt)
    {
        // TODO add your handling code here:
        if (validar()) {

            setFocus(dadosInstituicao.getFoco());
            txtCodigoBarra.setText("");
        }
    }

    void btn_adicionarActionPerformed(java.awt.event.ActionEvent evt)
    {

        double qtd = Double.parseDouble(txtQuatindade.getText());
        if (validar()) {

            configuracaoMesComecoController = new ConfiguracaoMesComecoController(conexao.getConnectionAtiva());
            boolean existeConfiguracaoDoCliente = configuracaoMesComecoController.existeConfiguracaoDoCliente(getIdCliente(), getCodigoProduto());

            if (existeConfiguracaoDoCliente) {
                new MesesPagoClienteVisao(this, rootPaneCheckingEnabled,
                        getIdCliente(),
                        getCodigoProduto(), conexao).setVisible(true);
            } else {
                adicionar_botao(qtd);
                scrolltable();
            }

        }
    }

    void btn_removerActionPerformed(java.awt.event.ActionEvent evt)
    {
//        try {
//
//            DefaultTableModel modelo = (DefaultTableModel) table.getModel();
//            if (podeRemoverServico(modelo, table.getSelectedRow())) {
        ////                actualizarPrecosAntigos2();
//                remover_item_carrinho();
//                inserirLinhaEmBranco();
//            }
//
//            txtCodigoBarra.setText("");
//            txtCodigoBarra.requestFocus();
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//
//        }


    try {
            DefaultTableModel modelo
                    = (DefaultTableModel) table.getModel();

            int linhaSelecionada = table.getSelectedRow();

            // Verificar se existe uma linha selecionada
            if (linhaSelecionada < 0) {
                txtCodigoBarra.setText("");
                txtCodigoBarra.requestFocus();
                return;
            }

            // Verificar se a linha pode ser removida
            if (podeRemoverServico(modelo, linhaSelecionada)) {

                remover_item_carrinho();

                // Limpar o painel lateral de lotes
                if (painelLotes != null) {
                    painelLotes.limpar();
                }

                inserirLinhaEmBranco();
            }

            txtCodigoBarra.setText("");
            txtCodigoBarra.requestFocus();

        } catch (Exception ex) {
            ex.printStackTrace();

            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao remover o produto: " + ex.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
        }

        if (painelLotes != null) {
            painelLotes.limpar();
        }

        atualizarPainelLotesDaLinhaSelecionada();
    }

    void ck_A4ActionPerformed(java.awt.event.ActionEvent evt)
    {
        // TODO add your handling code here:
//        actualizar_abreviacao();
    }

    void txtCodigoProdutoActionPerformed(java.awt.event.ActionEvent evt)
    {
        if (validar()) {
            accao_codigo_interno_enter();
            atualizarPainelLotesProdutoPesquisado();
            scrolltable();

        }
    }

    void cmbSubFamiliaActionPerformed(java.awt.event.ActionEvent evt)
    {

//        cmbProduto.setModel( new DefaultComboBoxModel( ( produtosController.getVectorByIdTipoProduto( getIdTipoProduto() ) ) ) );
    }

    void txtCodigoBarraActionPerformed(java.awt.event.ActionEvent evt)
    {
//        accao_codigo_barra_enter();
//        accao_codigo_barra_enter_cb();
        accao_codigo_barra_enter_cb();
        atualizarPainelLotesProdutoPesquisado();
        scrolltable();

    }

    void ck_simplificada_A7ActionPerformed(java.awt.event.ActionEvent evt)
    {
        // TODO add your handling code here:
//        actualizar_abreviacao();
    }

    void ck_S_A6ActionPerformed(java.awt.event.ActionEvent evt)
    {
//        actualizar_abreviacao();

    }

    void ck_ComVirgulaActionPerformed(java.awt.event.ActionEvent evt)
    {
//        actualizar_abreviacao();
    }

    void ck_simplificada_O_SActionPerformed(java.awt.event.ActionEvent evt)
    {
//        actualizar_abreviacao();
    }

    void ck_simplificada_OActionPerformed(java.awt.event.ActionEvent evt)
    {
//        actualizar_abreviacao();
    }

    void ck_simplificadaActionPerformed(java.awt.event.ActionEvent evt)
    {
        // TODO add your handling code here:
//        actualizar_abreviacao();
    }

    void ck_DuplicadaActionPerformed(java.awt.event.ActionEvent evt)
    {
        // TODO add your handling code here:
//        actualizar_abreviacao();
    }

    void cmbTipoDocumentoActionPerformed(java.awt.event.ActionEvent evt)
    {
//        mostrar_proximo_codigo_documento();
        txtBuscaRef.setEnabled(true);
        txtBuscaRef.requestFocus();
        actualizar_abreviacao();
        desabilitar_campos();
        atualizarCliente1();
        atualizarDataVencimentoFA();
        setTotalPagar();
        setTotalPagarGeral();
    }

    void btnFormaPagamentoActionPerformed(java.awt.event.ActionEvent evt)
    {
        removerUltimaLinhaVazia();

        jScrollPane1.repaint();
        if (MetodosUtil.licencaValidada(conexao)) {
            if (!MetodosUtil.tabela_vazia(table)) {
                if (!validarPrecos_tabela(table)) {
                    return; // Se houver erro, não abre forma de pagamento
                }

                new FormaPagamentoVisao(this, rootPaneCheckingEnabled, null, DVML.VENDA_PONTUAL_TOP, BDConexao.getInstancia()).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(null, "Caro usuário, adicione itens na tabela");
            }
        }

        table.getCellEditor().cancelCellEditing();

    }

    private void abrirFormaPagamento() {

        jScrollPane1.repaint();

        if (MetodosUtil.licencaValidada(conexao)) {

            if (!MetodosUtil.tabela_vazia(table)) {

                if (!validarPrecos_tabela(table)) {
                    return; // Se houver erro, não abre forma de pagamento
                }

                new FormaPagamentoVisao(
                        this,
                        rootPaneCheckingEnabled,
                        null,
                        DVML.VENDA_PONTUAL_TOP,
                        BDConexao.getInstancia()
                ).setVisible(true);

            } else {
                JOptionPane.showMessageDialog(null, "Caro usuário, adicione itens na tabela");
            }
        }

        table.getCellEditor().cancelCellEditing();
    }

    void btnProcessarActionPerformed(java.awt.event.ActionEvent evt)
    {
        if (validar()) {

            removerUltimaLinhaVazia();
            jScrollPane1.repaint();
            if (MetodosUtil.licencaValidada(conexao)) {
                procedimento_salvar_venda_comercial(true);

            }

        }
    }

    void btnCancelarActionPerformed(java.awt.event.ActionEvent evt)
    {
        // TODO add your handling code here:

        if (table.getRowCount() > 1) {
            int opcao = JOptionPane.showConfirmDialog(null, "Existem itens na tabela deseja pendurar?");

            if (opcao == JOptionPane.YES_OPTION) {
                procedimentoPenderFactura();
            }
        }

//        actualizarPrecosAntigos2();
        dispose();
    }

    void sp_desconto_financeiroStateChanged(javax.swing.event.ChangeEvent evt)
    {
        // TODO add your handling code here:
        tratar_desconto();
    }

    void sp_desconto_financeiroInputMethodTextChanged(java.awt.event.InputMethodEvent evt)
    {
        // TODO add your handling code here:
    }

    void sp_desconto_financeiroPropertyChange(java.beans.PropertyChangeEvent evt)
    {
        // TODO add your handling code here:
    }

    void sp_desconto_financeiroKeyPressed(java.awt.event.KeyEvent evt)
    {
        // TODO add your handling code here:
    }

    void sp_desconto_financeiroKeyTyped(java.awt.event.KeyEvent evt)
    {
        // TODO add your handling code here:
    }

    void rbArmazem1ActionPerformed(java.awt.event.ActionEvent evt)
    {
        try {
            configurar_armazens();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void rbArmazemActionPerformed(java.awt.event.ActionEvent evt)
    {
        try {
            configurar_armazens();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void cmbMoedaActionPerformed(java.awt.event.ActionEvent evt)
    {
        // TODO add your handling code here:
        actualizar_moeda();
    }

    void btnSemFormaPagamentoActionPerformed(java.awt.event.ActionEvent evt)
    {
        // TODO add your handling code here:
        if (MetodosUtil.licencaValidada(conexao)) {
            if (!MetodosUtil.tabela_vazia(table)) {
                if (!validarPrecos_tabela(table)) {
                    return; // Se houver erro, não abre forma de pagamento
                }
                procedimento_salvar_venda_comercial(false);
            } else {
                JOptionPane.showMessageDialog(null, "Caro usuário, adicione itens na tabela");
            }
        }
    }

    void tablePropertyChange(java.beans.PropertyChangeEvent evt)
    {
        // TODO add your handling code here:
        if (table.getSelectedColumn() == 3 || table.getSelectedColumn() == 4 || table.getSelectedColumn() == 5) {
            System.out.println("Preparar para actualiza a Qtd......");
            actualizarPreco();
//            JOptionPane.showMessageDialog( null, "Preparar para actualizar qtd" );
            actualizarQtdTable();
        }
    }

    void jScrollPane1PropertyChange(java.beans.PropertyChangeEvent evt)
    {

    }

    void txtTelClientePesquisaActionPerformed(java.awt.event.ActionEvent evt)
    {
        pesquisa_cliente_by_tel();
    }

    void txtQuantidadeStockActionPerformed(java.awt.event.ActionEvent evt)
    {
        // TODO add your handling code here:
    }

    void jButton1ActionPerformed(java.awt.event.ActionEvent evt)
    {
        // TODO add your handling code here:

        procedimentoPenderFactura();
    }

    void jButton2ActionPerformed(java.awt.event.ActionEvent evt)
    {
        // TODO add your handling code here:

        procedimentoCharFacturasPendentes();
    }

    void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {
        try {

//            String nomeImpressora = "EPSON TM-T88IV Receipt"; // nome exato no Windows
            String nomeImpressora = dadosInstituicaoController.findByCodigo(1).getImpressoraCaixa();

            PrintService[] services = PrinterJob.lookupPrintServices();
            PrintService impressora = null;

            for (PrintService ps : services) {
                if (ps.getName().equalsIgnoreCase(nomeImpressora)) {
                    impressora = ps;
                    break;
                }
            }

            if (impressora == null) {
                JOptionPane.showMessageDialog(null, "Impressora não encontrada! " + nomeImpressora);
                return;
            }

            DocPrintJob job = impressora.createPrintJob();

            // Comando oficial Epson para abrir gaveta
            byte[] abrirGaveta = new byte[]{
                27, 112, 0, 25, (byte) 250
            };

            Doc doc = new SimpleDoc(abrirGaveta, DocFlavor.BYTE_ARRAY.AUTOSENSE, null);
            job.print(doc, null);

            System.out.println("Gaveta aberta com sucesso!");

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao abrir gaveta: " + e.getMessage());
        }
    }
    public void busca_permissao() {

        try {
            // TODO add your handling code here
            setStatusUsuario((Vector) itemPermissaoDao.getAllPermissoesByIdUsuarioAndModulo(this.cod_usuario, DVML.MODULO_GESTAO_COMERCIAL));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void setStatusUsuario(Vector<TbItemPermissao> vector) {

        String permissao = "";
        //limpa
        setPermissoes(false);

        for (int i = 0; i < vector.size(); i++) {

            permissao = vector.get(i).getIdPermissao().getDescricao();

            System.out.println("PERMISSAO PRINCIPAL " + permissao);

            if (permissao.equals(btn_abertura_dia_venda.getText())) {
                btn_abertura_dia_venda.setEnabled(true);
            } else if (permissao.equals(btn_fecho_dia_venda.getText())) {
                btn_fecho_dia_venda.setEnabled(true);
            }

        }

    }

    public void setPermissoes(boolean status) {
        btn_abertura_dia_venda.setEnabled(status);
        btn_fecho_dia_venda.setEnabled(status);
    }
    void btn_abertura_dia_vendaActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        //        OperacaoSistemaUtil.procedimento_abrir_dia( this.idUser );
        //        procedimento_abrir_contagem( this.idUser );
        new CaixaAberturaFactVisao(cod_usuario, conexao, false).setVisible(true);

    }

    void btn_fecho_dia_vendaActionPerformed(java.awt.event.ActionEvent evt) {
        if (dadosInstituicao.getTipoFechoCaixa().equals("Normal")) {
            new CaixaFechoVisao(cod_usuario, conexao, false).setVisible(true);
        } else if (dadosInstituicao.getTipoFechoCaixa().equals("Simplificado")) {
            new CaixaFechoGoldVisao(cod_usuario, conexao, false).setVisible(true);
        } else {
            new CaixaFechoGoldDetalhadoVisao(cod_usuario, conexao, false).setVisible(true);
        }
    }

    void txtBuscaRefActionPerformed(java.awt.event.ActionEvent evt) {
        procedimento_busca();
    }

    void txtTotalPagarGeralActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        statusConversao = true;
        descontoStocavel = false;
        new ModalConsultasMesas(conexao).setVisible(true);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) throws SQLException {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(VendaModernaVisao.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VendaModernaVisao.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VendaModernaVisao.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VendaModernaVisao.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new VendaModernaVisao(15, BDConexao.getInstancia()).show(true);
                } catch (SQLException ex) {
                    Logger.getLogger(VendaModernaVisao.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
//    
//        public static void main( String[] args ) throws SQLException
//    {
//        new VendaModernaVisao( 15, BDConexao.getInstancia() ).show( true );
//    }

    private void configurarTxtBuscaRefMaiusculo() {

        ((AbstractDocument) txtBuscaRef.getDocument()).setDocumentFilter(
                new DocumentFilter() {

            @Override
            public void insertString(DocumentFilter.FilterBypass fb,
                    int offset,
                    String string,
                    AttributeSet attr)
                    throws BadLocationException {

                if (string != null) {
                    string = string.toUpperCase();
                }

                super.insertString(fb, offset, string, attr);
            }

            @Override
            public void replace(DocumentFilter.FilterBypass fb,
                    int offset,
                    int length,
                    String text,
                    AttributeSet attrs)
                    throws BadLocationException {

                if (text != null) {
                    text = text.toUpperCase();
                }

                super.replace(fb, offset, length, text, attrs);
            }
        });
    }

    public static int getIdDocumento() {
        try {
            String toString = cmbTipoDocumento.getSelectedItem().toString();
            System.out.println("Designacao: " + toString);
            Documento documentoLocal = documentosController.getDocumentoByDesignacao(cmbTipoDocumento.getSelectedItem().toString());
            return documentoLocal.getPkDocumento();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /* CRIACAO DO GETS  */
    public static double getQuantidade() {
        return Double.parseDouble(txtQuatindade.getText());
    }

    public static int getIdCliente() {
        try {
            TbCliente cliente = clientesController.getClienteByNome(cmbCliente.getSelectedItem().toString());
            return cliente.getCodigo();
        } catch (Exception e) {
            return 0;
        }

    }

    public static int getIdDocumento(DocumentosController documentosControllerLocal) {
        try {
            Documento documentoLocal = documentosControllerLocal.getDocumentoByDesignacao(cmbTipoDocumento.getSelectedItem().toString());
            return documentoLocal.getPkDocumento();
        } catch (Exception e) {
            return 0;
        }
    }

    public static int getIdAnoEconomico() {
        try {
            AnoEconomico anoEconomicoLocal = anoEconomicoController
                    .getAnoEconomicoByDesignacao(cmbAnoEconomico.getSelectedItem().toString());
            return anoEconomicoLocal.getPkAnoEconomico();
        } catch (Exception e) {
            return 0;
        }
    }

    public static int getIdAnoEconomico(AnoEconomicoController anoEconomicoControllerLocal) {
        try {
            AnoEconomico anoEconomicoLocal = anoEconomicoControllerLocal
                    .getAnoEconomicoByDesignacao(cmbAnoEconomico.getSelectedItem().toString());
            return anoEconomicoLocal.getPkAnoEconomico();
        } catch (Exception e) {
            return 0;
        }
    }

    public static int getIdMoeda() {
        try {
            Moeda moedaLocal = moedasController.getMoedaByDesignacao(cmbMoeda.getSelectedItem().toString());
            return moedaLocal.getPkMoeda();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }

    }

    public static Double getPreco() {

        Moeda moeda = getMoeda();
        if (moeda == null) {
            return null;
        }

        Cambio lastCambio = cambiosController.getLastObject(moeda.getPkMoeda());
        try {

            Double valorCambio = lastCambio.getValor();
            TbPreco precoLocal = precosController.getLastIdPrecoByIdProduto(getCodigoProduto(), Double.parseDouble(txtQuatindade.getText()));
            double precoVenda = precoLocal.getPrecoVenda().doubleValue();

            return (precoVenda / valorCambio);
        } catch (Exception e) {
            return 0.0;
        }

    }

    public static Double getPreco(int codigoProduto, double qtd) {

        Moeda moeda = getMoeda();
        if (moeda == null) {
            return null;
        }

        Cambio lastCambio = cambiosController.getLastObject(moeda.getPkMoeda());
        try {

            Double valorCambio = lastCambio.getValor();
            TbPreco precoLocal = precosController.getLastIdPrecoByIdProduto(codigoProduto, qtd);
            double precoVenda = precoLocal.getPrecoVenda().doubleValue();

            return (precoVenda / valorCambio);
        } catch (Exception e) {
            return 0.0;
        }

    }

    private static void mostrar_abreviacao_moeda_cambio() {
        try {
            cambio = cambiosController.getLastObject(getIdMoeda());

        } catch (Exception e) {
            cambio = null;
            e.printStackTrace();
        }
    }

    private static double getTaxaImposto(int idProduto) {
        TbProduto produto = (TbProduto) produtosController.findById(idProduto);
        //verifca o artigo se eh produto ou servico.
        if (produto.getStocavel().equals("true")) {
            return produtosImpostoController.getTaxaByIdProduto(idProduto);
        } else {
            return servicosRetencaoController.getTaxaByIdProduto(idProduto);
        }

    }

    private static double getTaxaImpostoIva(int idProduto) {
        try {
            return produtosImpostoController.getTaxaByIdProduto(idProduto);
        } catch (Exception e) {
        }

        return 0;

    }

    private static double getTaxaImpostoRet(int idProduto) {
        try {
            return servicosRetencaoController.getTaxaByIdProduto(idProduto);
        } catch (Exception e) {
        }
        return 0;

    }

    private static String getMotivoIsensao(int idProduto) {
        try {
            return produtosIsentoController.getRegimeIsensaoByIdProduto(idProduto);

        } catch (Exception e) {
        }

        return "";
    }

    private void actualizar_moeda() {
        CfMethods.MOEDA = getMoeda().getAbreviacao();
        mostrar_abreviacao_moeda_cambio();
        try {
//            refresh_table();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void actualizar_moeda(String moeda) {
        CfMethods.MOEDA = moeda;
        mostrar_abreviacao_moeda_cambio();
        refresh_table(1);
    }

    public static void scrolltable() {
        table.scrollRectToVisible(table.getCellRect(table.getRowCount() - 1, table.getColumnCount(), true));
    }

    public void configurar_armazens() {
//        setArmazem( dadosInstituicao.getConfigArmazens() );
//        try
//        {
//            if ( !rbArmazem.isSelected() )
//            {
//                //                Caso for MultiArmazens
//                cmbArmazem.setModel( new DefaultComboBoxModel( armazensController.getVector2() ) );
//            }
//            else if ( rbArmazem.isSelected() )
//            {
//                //                Caso for apenas Um Armazem
//                cmbArmazem.setModel( new DefaultComboBoxModel( 
//                        armazensAccessoController.getAllArmazemExceptoEconomatoByIdUSuario( cod_usuario ) )
//                );
//            }
//        }
//        catch ( Exception e )
//        {
//            e.printStackTrace();
//        }

        cmbArmazem.setModel(new DefaultComboBoxModel(armazensController.getVector2()));
    }

    public static Double getPreco(int idProduto, double qtd, int idMoeda) {

        Moeda moeda = (Moeda) moedasController.findById(idMoeda);
        if (moeda == null) {
            return null;
        }

        Cambio lastCambio = cambiosController.getLastObject(moeda.getPkMoeda());
        try {
            Double valorCambio = lastCambio.getValor();
            TbPreco precoLocal = precosController.getLastIdPrecoByIdProduto(idProduto, qtd);
            double precoVenda = precoLocal.getPrecoVenda().doubleValue();

            if (moeda.getAbreviacao().equals(DVML.MOEDA_KWANZA)) {
                return precoVenda;
            }

            return (precoVenda / valorCambio);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public static String getDescricao_Produto() {
        TbProduto produto = (TbProduto) produtosController.findById(getCodigoProduto());
        return produto.getDesignacao();
    }

    public static String getUnidade_Produto() {
        TbProduto produto = (TbProduto) produtosController.findById(getCodigoProduto());
        Unidade unidade = (Unidade) unidadesController.findById(produto.getCodUnidade().getPkUnidade());
        return unidade.getAbreviacao();
    }

    private static Moeda getMoeda() {
        String moedaSelecionada = (String) cmbMoeda.getSelectedItem();

        if (moedaSelecionada == null) {
            return null;
        }

        return moedasController.getMoedaByDesignacao(moedaSelecionada);
    }

    public static void actualizar_quantidade_antigo(int cod, double quantidade, BDConexao conexaoLocal) {

        String sql = "UPDATE tb_stock SET quantidade_existente =  " + (getQuantidadeProduto(cod, conexaoLocal) - quantidade) + " WHERE cod_produto_codigo = " + cod + " AND  cod_armazem = " + getCodigoArmazem();
        System.out.println("Quantidade   : " + quantidade);
        conexaoLocal.executeUpdate(sql);

    }

    public static void actualizar_quantidade(
            int cod,
            double quantidade,
            BDConexao conexaoLocal) {

        String sql = "UPDATE tb_stock "
                + "SET quantidade_existente = quantidade_existente - ? "
                + "WHERE cod_produto_codigo = ? "
                + "AND cod_armazem = ? "
                + "AND quantidade_existente >= ?";

        try (java.sql.PreparedStatement ps
                = conexaoLocal.getConnection().prepareStatement(sql)) {

            ps.setDouble(1, quantidade);
            ps.setInt(2, cod);
            ps.setInt(3, getCodigoArmazem());
            ps.setDouble(4, quantidade);

            int linhasActualizadas = ps.executeUpdate();

            if (linhasActualizadas == 0) {
                throw new SQLException(
                        "Stock insuficiente ou inexistente para o produto "
                        + cod + " no armazém " + getCodigoArmazem()
                );
            }

        } catch (SQLException ex) {
            throw new RuntimeException(
                    "Erro ao actualizar o stock do produto " + cod,
                    ex
            );
        }
    }

    public static double getQuantidadeProduto(int cod_produto, BDConexao conexaoLocal) {

        String sql = "SELECT quantidade_existente FROM  tb_stock WHERE  cod_produto_codigo = " + cod_produto + " AND cod_armazem = " + getCodigoArmazem();

        ResultSet rs = conexaoLocal.executeQuery(sql);

        try {
            if (rs.next()) {
                return rs.getDouble("quantidade_existente");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return 0;
        }

        return 0;
    }

    public int getLastCodigo(String tabela) {

        String sql = "SELECT max(codigo) FROM " + tabela;

        ResultSet rs = conexao.executeQuery(sql);

        try {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return 0;
        }

        return 0;
    }

    public static double getDesconto_produto(double preco_total_produto) throws SQLException {

        TbDesconto desconto = getDesconto();
        Double quantidade = desconto.getQuantidade();
        double percentagem_desconto = desconto.getValor();

        if (getQuantidade() >= quantidade) {
            return preco_total_produto * (percentagem_desconto / 100);
        } else {
            return 0.0;
        }

    }

    private static TbDesconto getDesconto() {
        TbDesconto desconto = descontosController.get_desconto_cliente_produto(getIdCliente(), getCodigoProduto());
        return desconto;
    }

    private static double getDescontoPercentagem() {
        TbDesconto desconto = getDesconto();

        if (!Objects.isNull(desconto)) {
            try {
                Double quantidade = desconto.getQuantidade();
                if (getQuantidade() >= quantidade) {
                    return desconto.getValor();
                } else {
                    return 0.0;
                }
            } catch (Exception e) {
            }
            return 0.0;

        }
        return 0d;

//
    }

    public static double getDesconto_produto(double preco_total_produto, int qtd) {

        TbDesconto desconto = descontosController.get_desconto_cliente_produto(getIdCliente(), getCodigoProduto());
        Double quantidade = desconto.getQuantidade();
        double percentagem_desconto = desconto.getValor();

        if (qtd >= quantidade) {
            return preco_total_produto * (percentagem_desconto / 100);
        } else {
            return 0.0;
        }

    }

    public static double getDescontoValorLinha(int codigo_produto, double preco_total_linha, int qtd) {

        TbDesconto desconto = descontosController.get_desconto_cliente_produto(getIdCliente(), codigo_produto);
        Double quantidade = desconto.getQuantidade();
        double percentagem_desconto = desconto.getValor();

        if (qtd >= quantidade) {
            return preco_total_linha * (percentagem_desconto / 100);
        } else {
            return 0.0;
        }

    }

    public void getDesconto_Quantidade() throws SQLException {
    }

    public double getDescontoActual() throws SQLException {

        ResultSet resultado = conexao.executeQuery("SELECT valor FROM tb_desconto WHERE idDesconto = 1");
        double valor = 0;
        if (resultado.next()) {
            valor = resultado.getDouble("valor desconto actual");
        }
        return valor;

    }

    public static void remover_all_produto() {

        DefaultTableModel modelo = (DefaultTableModel) table.getModel();
        for (int i = modelo.getRowCount() - 1; i >= 0; i--) {
            modelo.removeRow(i);
        }

    }

    private static double getValorComImposto(double qtd, double taxa, double preco_venda, double desconto) {
        double subtotal_linha = (preco_venda * qtd);
        double desconto_valor = (subtotal_linha * (desconto / 100));
        double valor_iva = 1 + (taxa / 100);//
        return ((subtotal_linha - desconto_valor) * valor_iva);
    }

    private static BigDecimal getIVA(BigDecimal qtd, BigDecimal taxa, BigDecimal precoVenda, BigDecimal desconto) {
        // subtotal = preco * quantidade
        BigDecimal subtotal = precoVenda.multiply(qtd);

        // subtrai o desconto (se houver)
        BigDecimal baseCalculo = subtotal.subtract(desconto);

        // taxa/100 → para obter o fator de imposto
        BigDecimal fatorIva = taxa.divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);

        // valor do IVA
        BigDecimal valorIva = baseCalculo.multiply(fatorIva);

        return valorIva.setScale(2, RoundingMode.HALF_UP);
    }

    private static double getRET(double qtd, double taxa_r, double preco_venda, double desconto) {
        double subtotal_linha = (preco_venda * qtd);
        double valor_ret = (taxa_r / 100);//
        return ((subtotal_linha - desconto) * valor_ret);

    }

    public static int getCodigoArmazem() {
        return armazensController.getArmazemByDesignacao(cmbArmazem.getSelectedItem().toString()).getCodigo();
    }

    public static void procedimento_salvar_venda_comercial(boolean frNormal) {
        if (MetodosUtil.tabela_vazia(table)) {
            JOptionPane.showMessageDialog(null, "Caro usuário, adicione item na tabela", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Verifica se a data do documento é válida
        if (!data_documento_superior_ou_igual_ao_ultimo_doc()) {
            JOptionPane.showMessageDialog(null, "A data do documento é inferior ao último documento emitido", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Mostra aviso se for necessário
        data_documento_superior_data_actual();
        if (!aviso_continuar_documento) {
            return;
        }

        // Valida campos antes de prosseguir
        if (campos_invalido_imprimir()) {
            return;
        }

        // Verifica se há algum erro lógico ou de transação anterior
//        if ( transtorno() )
//        {
//            return;
//        }
        // Moeda estrangeira?
        if (!DVML.MOEDA_KWANZA.equalsIgnoreCase(getMoeda().getAbreviacao())) {
            actualizar_moeda(DVML.MOEDA_KWANZA);
        }

        // Agora chama o método que realmente salva a venda
        salvar_venda_comercial(frNormal);

        // Atualiza a data após a venda
        dc_data_documento.setDate(new Date());
        descontoStocavel = true;
    }

    private static void salvar_venda_comercial(boolean frNormal) {
        BDConexao conexaoTransactionLocal = BDConexao.getInstancia();
        vendasController = new VendasController(conexaoTransactionLocal);
        armazensController = new ArmazensController(conexaoTransactionLocal);
        itemVendasController = new ItemVendasController(conexaoTransactionLocal);
        produtosController = new ProdutosController(conexaoTransactionLocal);
        clientesController = new ClientesController(conexaoTransactionLocal);
        documentosController = new DocumentosController(conexaoTransactionLocal);
        anoEconomicoController = new AnoEconomicoController(conexaoTransactionLocal);
        formaPagamentoController = new FormaPagamentoController(conexaoTransactionLocal);
        formaPagamentoItemController = new FormaPagamentoItemController(conexaoTransactionLocal);
        pagamentoMensalidadeController = new PagamentoMensalidadeController(conexaoTransactionLocal.getConnectionAtiva());
        precosController = new PrecosController(conexaoTransactionLocal);
        usuariosController = new UsuariosController(conexaoTransactionLocal);
        lugaresController = new LugaresController(conexaoTransactionLocal);
        mesasController = new MesasController(conexaoTransactionLocal);
        contaController = new ContaController(conexaoTransactionLocal);

        StoksController stocksControllerLocal = new StoksController(conexaoTransactionLocal);
        DocumentosController.start(conexaoTransactionLocal); // Inicia a transação

        boolean vendaConcluida = false;
        Integer idVendaGerada = 0;

        try {
            System.out.println("AutoCommit após iniciar transação? "
                    + conexaoTransactionLocal.getConnection().getAutoCommit());

        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            // Construção do objeto venda
            TbVenda venda = construirVenda();

            // Salvar a venda e obter o ID
            idVendaGerada = vendasController.salvarRetornaID(venda);
//            venda.setHashCod( MetodosUtil.criptografia_hash( vendasController.findById( idVendaGerada), getGrossTotal().doubleValue(), conexaoTransaction ) );

            vendasController.actualizar_hash_and_assinatura(idVendaGerada, getGrossTotal().doubleValue());

            if (idVendaGerada == null || idVendaGerada == 0) {
                throw new Exception("Falha ao obter o ID da venda gravada.");
            }

            // Ações específicas por tipo de documento
            if (getIdDocumento() == DOC_FACTURA_RECIBO_FR) {
                MetodosUtil.adicionar_saldo_banco(venda.getTotalVenda().doubleValue(), venda.getIdBanco().getIdBanco(), conexaoTransactionLocal);
            }

            if (getIdDocumento() == DOC_FACTURA_FT) {
                ExtratoContaClienteController.registro_movimento_conta_cliente(venda, conexaoTransactionLocal);
            }

            // Salvar os itens da venda
            salvar_item_venda_comercial(idVendaGerada, conexaoTransactionLocal, stocksControllerLocal);

            // Registrar formas de pagamento
            if (getIdDocumento() == DOC_FACTURA_RECIBO_FR) {
                registrarFormaPagamento(idVendaGerada, venda.getTotalVenda(), frNormal, conexaoTransactionLocal);
            }
            //actualizar precos antigos
//            actualizarPrecosAntigos();

            if (!consultaDoc.isEmpty()) {
                procedimentoActualizarStausConsulta();
            }

            // Finaliza transação
//            DocumentosController.commit(conexaoTransactionLocal);
            // Finaliza transação
            DocumentosController.commit(conexaoTransactionLocal);

            DocumentosController.commit(conexaoTransactionLocal);
            vendaConcluida = true;

            txtCodigoBarra.requestFocus();
            JOptionPane.showMessageDialog(
                    null,
                    "Factura efectuada com sucesso!"
            );
            txtNomeConsumidorFinal.setVisible(true);

        } catch (Exception e) {

            DocumentosController.rollback(conexaoTransactionLocal);
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao processar a venda: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } finally {
            conexaoTransactionLocal.close();
        }

        // fora do try-catch da transação
        // Imprimir apenas se a venda foi concluída com sucesso.
// Imprimir apenas quando a transação foi concluída com sucesso.
        if (vendaConcluida && idVendaGerada != null && idVendaGerada > 0) {
            try {
                System.out.println("Antes de imprimir: " + idVendaGerada);

                imprimir_factura(idVendaGerada);
                System.out.println("Depois de imprimir: " + idVendaGerada);
                voltarFolhaPadrao();

            } catch (Exception ex) {
                ex.printStackTrace();

                JOptionPane.showMessageDialog(
                        null,
                        "A venda foi gravada, mas ocorreu um erro ao imprimir a factura: "
                        + ex.getMessage(),
                        "Erro de impressão",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
//        try {
//            imprimir_factura(idVendaGerada);
//            voltarFolhaPadrao();
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            JOptionPane.showMessageDialog(null, "Erro ao imprimir factura: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
//        }
    }

    private static FormaPagamentoItem criarItemFormaPagamento(int idVenda, int idForma, BigDecimal valor, BigDecimal troco, String referencia) {
        FormaPagamentoItem item = new FormaPagamentoItem();
        item.setValor(valor);
        item.setTroco(troco);
        item.setValor_real(valor.subtract(troco));
        item.setReferencia(referencia);
        item.setFkVenda(new TbVenda(idVenda));
        item.setFkFormaPagamento(new FormaPagamento(idForma));
        return item;
    }

    public static void registrarFormaPagamento(int idVenda, BigDecimal totalVenda, boolean formaNormal, BDConexao conexaoLocal) throws Exception {
//
//        if ( !formaNormal )
//        {
//            registrarPagamentoUnico( idVenda, totalVenda );
//            return;
//        }
        registrarPagamentosMultiplos(idVenda, conexaoLocal);
    }

    private static void registrarPagamentoUnico(int idVenda, BigDecimal totalVenda) throws Exception {
        BigDecimal valor = totalVenda;
        String descricao = "";
        String referencia = String.valueOf(idVenda);
        int idForma = 1;

        FormaPagamento forma = formaPagamentoController.findByCodigo(idForma);
        if (forma == null) {
            throw new Exception("Forma de pagamento não encontrada: " + descricao);
        }

        Contas conta = (Contas) contaController.findById(forma.getFkContaAssociada());
        FormaPagamentoItem item = criarItemFormaPagamento(idVenda, idForma, valor, BigDecimal.ZERO, referencia);
        if (!formaPagamentoItemController.salvar(item)) {
            throw new Exception("Erro ao salvar forma de pagamento: " + descricao);
        }

        if (conta != null) {
            MetodosUtilTS.entradaTesouraria(
                    conta,
                    lb_proximo_documento.getText(),
                    forma,
                    referencia,
                    valor,
                    cod_usuario,
                    usuariosController,
                    cmc,
                    conexao
            );
        }
    }

    private static void registrarPagamentosMultiplos(int idVenda, BDConexao conexaoLocal) throws Exception {
        DefaultTableModel modelo = (DefaultTableModel) FormaPagamentoVisao.tabela_forma_pagamento.getModel();
        double trocoExterno = CfMethods.parseMoedaFormatada(FormaPagamentoVisao.lb_troco.getText());
        BigDecimal troco = new BigDecimal(trocoExterno);

        for (int i = 0; i < modelo.getRowCount(); i++) {

            BigDecimal valor = CfMethods.parseMoedaSegura(modelo.getValueAt(i, 3).toString());
            if (valor.compareTo(BigDecimal.ZERO) <= 0) {
                continue;
            }

            int idForma = Integer.parseInt(modelo.getValueAt(i, 0).toString());
            String descricao = modelo.getValueAt(i, 1).toString();
            String referencia = modelo.getValueAt(i, 2) != null ? modelo.getValueAt(i, 2).toString() : "n/a";

            FormaPagamento forma = formaPagamentoController.findByDescrisao(descricao);
            if (forma == null) {
                throw new Exception("Forma de pagamento não encontrada: " + descricao);
            }

            Contas conta = (Contas) contaController.findById(forma.getFkContaAssociada());

            System.out.println("FORMA PAGAMENTO: " + descricao);
            System.out.println("FORMA PAGAMENTO VALOR: " + valor);
            System.out.println("FORMA PAGAMENTO TROCO: " + troco);

            FormaPagamentoItem item = criarItemFormaPagamento(idVenda, idForma, valor, troco, referencia);

            if (!formaPagamentoItemController.salvar(item)) {
                throw new Exception("Erro ao salvar forma de pagamento: " + descricao);
            }

            if (conta != null) {
                MetodosUtilTS.entradaTesouraria(
                        conta,
                        lb_proximo_documento.getText(),
                        forma,
                        referencia,
                        valor,
                        cod_usuario,
                        usuariosController,
                        cmc,
                        conexaoLocal
                );
            }

            troco = BigDecimal.ZERO;
        }
    }

    /**
     * Cria um objeto FormaPagamentoItem já populado e com cálculos prontos.
     */
    private static void imprimir_factura(int cod_venda) {

        BDConexao conexoaLocal = BDConexao.getInstancia();
        limpar();
        remover_all_produto();
        accao_cliente();
        limpar_consumidor_final();
        txtNomeConsumidorFinal.setVisible(true);
        txtNomeConsumidorFinal.getText().equals("Consumidor Final");

        txtQuatindade.setText("1");
        txtQuatindade.requestFocus();
        txtQuantidadeStock.setText(String.valueOf(conexoaLocal.getQtdExistenteStock(getCodigoProduto(), getCodigoArmazem())));

        List<TbProduto> lista_produto_isentos = getProdutosIsentos();
        String motivos_isentos = MetodosUtil.getMotivoIsensaoProdutos(lista_produto_isentos);
        conexoaLocal.close();
        int numeroVias = (int) Double.parseDouble(spnCopia.getValue().toString());

        for (int i = 1; i <= numeroVias; i++) {
            String via;
            switch (i) {
                case 1:
                    via = "Original";
                    break;
                case 2:
                    via = "Duplicado";
                    break;
                case 3:
                    via = "Triplicado";
                    break;
                default:
                    via = "Cópia";
            }
            if ((getIdDocumento() == DOC_FACTURA_RECIBO_FR || getIdDocumento() == DOC_FACTURA_FT || getIdDocumento() == DVML.DOC_FACTURA_PROFORMA_PP)) {

                ListaVenda1 listaVenda1 = new ListaVenda1(cod_venda, abreviacao, false, ck_simplificada.isSelected(), via, motivos_isentos);
            } else {
                ListaVendaConsultas listaVenda1 = new ListaVendaConsultas(cod_venda, abreviacao, false, ck_simplificada.isSelected(), via);
            }
        }
    }

    private static void accao_cliente() {
        String nomeCliente = (String) cmbCliente.getSelectedItem();

        txtNomeConsumidorFinal.setText(nomeCliente);
        String nif = clientesController.findByNome(nomeCliente).getNif();
        txtNifClientePesquisa.setText(nif);
    }

    private static void accao_cliente_tel() {
        String nomeCliente = (String) cmbCliente.getSelectedItem();

        txtNomeConsumidorFinal.setText(nomeCliente);
        String telefone = clientesController.findByNome(nomeCliente).getTelefone();
//        txtTelClientePesquisa.setText( telefone );
    }

    private static void limpar_consumidor_final() {

        if (cmbCliente.getSelectedItem().equals("Consumidor Final")) {
//            lbClienteConsumidorFinal.setVisible( true );
            txtNomeConsumidorFinal.setVisible(true);
        } else {
//            lbClienteConsumidorFinal.setVisible( false );
            txtNomeConsumidorFinal.setVisible(false);
        }

    }

    private static void data_documento_superior_data_actual() {

        //retirando a data do documento
        Date data_documento = dc_data_documento.getDate();
        //pegando a data actual do sistema 
        Date data_sistema = new Date();
        //comparar as datas
        if (MetodosUtil.maior_data_1_data_2(data_documento, data_sistema)) {
            JOptionPane.showMessageDialog(null, "Após essa emissão, não poderá ser emitido um novo documento\n "
                    + "com a data actual ou anterior, dentro da mesma série.",
                    "AVISO", JOptionPane.WARNING_MESSAGE);

            aviso_continuar_documento = JOptionPane.showConfirmDialog(null, "Ainda assim deseja continuar com a operação ?")
                    == JOptionPane.YES_OPTION;

        } else {
            aviso_continuar_documento = true;
        }

    }

    private static List<TbProduto> getProdutosIsentos() {
        DefaultTableModel modelo = (DefaultTableModel) table.getModel();
        double taxa = 0.0;
        int codigo_produto = 0;
        List<TbProduto> lista_produtos_isentos = new ArrayList<>();
        for (int i = 0; i < modelo.getRowCount(); i++) {
            codigo_produto = Integer.parseInt(modelo.getValueAt(i, 0).toString());
            taxa = Double.parseDouble(modelo.getValueAt(i, 6).toString());
            if (taxa == 0.0) {
                lista_produtos_isentos.add((TbProduto) produtosController.findById(codigo_produto));
            }
        }

        return lista_produtos_isentos;

    }

    private static Date getDataVencimentoFr() {

        if ((getIdDocumento() == DVML.DOC_FACTURA_PROFORMA_PP) || (getIdDocumento() == DVML.DOC_FACTURA_FT)) {
            return dc_data_vencimento.getDate();
        } else {
            return new Date();
        }
    }

    private static TbVenda construirVenda() throws SQLException, InterruptedException, Exception {
        TbVenda venda = new TbVenda();

        // Datas
        venda.setDataVenda(dc_data_documento.getDate());
        venda.setHora(new java.sql.Time(System.currentTimeMillis()));
        venda.setDataVencimento(getDataVencimentoFr()); // se aplicável
//        venda.setDataVencimento( dc_data_vencimento.getDate() ); // se aplicável
        venda.setRefDataFact(dc_data_documento.getDate());       // se aplicável

        // Totais
        venda.setTotalVenda(getTotalAOALiquido());
        venda.setValorEntregue(getValor_entregue());
        venda.setTroco(new BigDecimal(getTroco()));
        venda.setDescontoTotal(FinanceUtils.getDescontoComercial(INDEX_TABLE_PRECO, INDEX_TABLE_QTD, INDEX_TABLE_DESCONTO, table)
                .add(BigDecimal.valueOf(getDescontoFinanceiro())));

        venda.setDescontoComercial(
                FinanceUtils.getDescontoComercial(INDEX_TABLE_PRECO, INDEX_TABLE_QTD, INDEX_TABLE_DESCONTO, table));
        venda.setDescontoFinanceiro(new BigDecimal(getDescontoFinanceiro()));
        venda.setTotalIva(new BigDecimal(
                FinanceUtils.getTotalImpostoTable(
                        INDEX_TABLE_PRECO,
                        INDEX_TABLE_QTD,
                        INDEX_TABLE_DESCONTO,
                        INDEX_TABLE_TAXA_IVA, table))
        );
        venda.setTotalGeral(
                FinanceUtils.getTotalIliquidoTable(
                        INDEX_TABLE_PRECO,
                        INDEX_TABLE_QTD,
                        table));

        venda.setTotalIncidencia(new BigDecimal(FinanceUtils.getTotalIncidenciaTable(
                INDEX_TABLE_PRECO,
                INDEX_TABLE_QTD,
                INDEX_TABLE_DESCONTO,
                INDEX_TABLE_TAXA_IVA,
                table))
        );

        venda.setTotalIncidenciaIsento(FinanceUtils.getTotalIncidenciaIsento(
                INDEX_TABLE_PRECO,
                INDEX_TABLE_QTD,
                INDEX_TABLE_DESCONTO, INDEX_TABLE_TAXA_IVA, table));

        venda.setTotalRetencao(getTotalRetencaoLiquido());
        venda.setGorjeta(new BigDecimal(gorjeta));
        venda.setTotalPorExtenso(iniciais_extenso() + lbValorPorExtenco.getText());

        // Strings
        if (getIdDocumento() == DOC_FACTURA_RECIBO_FR && getIdCliente() == 1) {

            venda.setNomeCliente(txtNomeConsumidorFinal.getText().trim());
            venda.setClienteNif(txtNifClientePesquisa.getText().trim());
        } else if (getIdDocumento() == DOC_FACTURA_RECIBO_FR && getIdCliente() > 1) {

            venda.setNomeCliente(getNomeCliente());
            venda.setClienteNif(getClienteNif());
        } else if (getIdDocumento() != DOC_FACTURA_RECIBO_FR && getIdCliente() > 1) {
            venda.setNomeCliente(getNomeCliente());
            venda.setClienteNif(getClienteNif());
        }

//###
//        mostrar_proximo_codigo_documento();
        String codFactGerado = null;
        int tentativas = 0;

        while (tentativas < 3) {
            tentativas++;

            mostrar_proximo_codigo_documento();
            codFactGerado = prox_doc;

            if (!vendasController.existeCodFact(codFactGerado)) {
                break;
            }

            // Pequena pausa para evitar colisão simultânea
            Thread.sleep(50);
        }

        if (codFactGerado == null) {
            throw new Exception("Não foi possível gerar número de factura único.");
        }

        venda.setCodFact(codFactGerado);

        venda.setCodFact(prox_doc);
        venda.setPerformance("false"); // ou pegar de um campo
        venda.setCredito("false");        // depende se venda é a crédito
        venda.setHashCod("");           // actualiza o hash depois de salvar
        venda.setAssinatura("");        // preencher se estiver em uso
        venda.setObs("");
        venda.setStatusEliminado("false");
        venda.setStatusRecibo(false); // ou true se for o caso
        // Cliente / usuário / banco / armazém
        venda.setIdBanco(new TbBanco(1));
        venda.setCodigoUsuario(new TbUsuario(cod_usuario));
        venda.setCodigoCliente(new TbCliente(getIdCliente()));
        venda.setIdArmazemFK(new TbArmazem(getCodigoArmazem()));
        // Documento, ano econômico, câmbio
        venda.setFkDocumento(new Documento(getIdDocumento()));
        venda.setFkAnoEconomico(new AnoEconomico(getIdAnoEconomico()));
        int id = cambiosController.getLastId(getIdMoeda());
        venda.setFkCambio(new Cambio(id));
        // Dados adicionais do carro (se aplicável)
        venda.setLocalCarga("");
        venda.setLocalDescarga("");
        venda.setNomeConsumidorFinal(txtNomeConsumidorFinal.getText());
        venda.setReferencia(txtBuscaRef.getText());
        venda.setMatricula("");
        venda.setModelo("");
        venda.setNumChassi("");
        venda.setNumMotor("");
        venda.setKilometro("");
        venda.setNomeMotorista("");
        venda.setMarcaCarro("");
        venda.setCorCarro("");
        venda.setNDocMotorista("");
        // Contador do documento
        venda.setCont(0); // por exemplo
        venda.setStatusConvertidoConsulta(true);
        return venda;
    }

    public static String getMes(String designacao) {

        try {
            return designacao.split("#")[1];
        } catch (Exception e) {
        }

        return "N/S";

    }

    public static void salvar_item_venda_comercial(Integer cod_venda, BDConexao conexaoLocal, StoksController stoksControllerLocal) throws Exception {
        for (int i = 0; i < table.getRowCount(); i++) {
            try {
                int idProduto = Integer.parseInt(table.getModel().getValueAt(i, 0).toString());
                String designacaoItem = table.getModel().getValueAt(i, 1).toString();

                TbProduto produto = (TbProduto) produtosController.findById(idProduto);

                TbItemVenda item = new TbItemVenda();
                item.setCodigoVenda(new TbVenda(cod_venda));
                item.setCodigoProduto(produto);
                item.setDesignacaoItem(designacaoItem);
                item.setQuantidade(Double.parseDouble(table.getModel().getValueAt(i, 4).toString()));
                item.setDesconto(Double.parseDouble(table.getModel().getValueAt(i, 5).toString()));
                item.setValorIva(Double.parseDouble(table.getModel().getValueAt(i, 6).toString()));
                item.setValorRetencao(Double.parseDouble(table.getModel().getValueAt(i, 7).toString()));
                TbPreco precoProduto = precosController.getLastIdPrecoByIdProduto(idProduto, item.getQuantidade());
                item.setMotivoIsensao(getMotivoIsensao(idProduto));
                item.setCodigoIsensao(MetodosUtil.getCodigoRegime(idProduto));
                item.setTotal(new BigDecimal(CfMethods.parseMoedaFormatada(table.getModel().getValueAt(i, 10).toString())));
                item.setFkPreco(precosController.getLastIdPrecoByIdProduto(idProduto, item.getQuantidade()));
                item.setDataServico(new Date());
                item.setFkLugares((TbLugares) lugaresController.findById(DVML.LUGAR_BALCAO));
                item.setFkMesas((TbMesas) mesasController.findById(DVML.MESA_BALCAO));
                item.setDesignacaoItem(designacaoItem);
                // Salvar item
                if (!itemVendasController.salvar(item)) {
                    throw new Exception("Erro ao salvar item da venda. Produto: " + produto.getDesignacao());
                } else {
                    String mes = getMes(designacaoItem);
                    if (!mes.equals("N/S")) {
                        procedimentoPagamentoMensalidadeServico(cod_venda, getIdCliente(), idProduto, mes, conexaoLocal);
                        System.out.println("Pagmento de mensalidade pago com sucesso!..");
                    }
                }

                int idArmazem = getCodigoArmazem();

                if (descontoStocavel) {
                    // Controle de stock (se for estocável)
                    boolean isStocavel = "true".equalsIgnoreCase(produto.getStocavel());
                    if (isStocavel) {

                        TbStock stock_local_local = stoksControllerLocal.getStockByIdProdutoAndIdArmazem(idProduto, idArmazem);

                        if ((getIdDocumento() == DOC_FACTURA_RECIBO_FR
                                || getIdDocumento() == DOC_FACTURA_FT
                                || getIdDocumento() == DOC_FACTURA_CONSULTA_MESA
                                || getIdDocumento() == DVML.DOC_GUIA_TRANSPORTE_GT) && stock_local_local != null) {

                            MovimentacaoController.registrarMovimento(
                                    idProduto,
                                    idArmazem,
                                    cod_usuario,
                                    new BigDecimal(item.getQuantidade()),
                                    prox_doc,
                                    "SAIDA",
                                    conexaoLocal
                            );

                            boolean farmacia = dadosInstituicao != null
                                    && "Farmacia".equalsIgnoreCase(
                                            dadosInstituicao.getNegocio()
                                    );

                            if (farmacia
                                    && (getIdDocumento() == DOC_FACTURA_RECIBO_FR
                                    || getIdDocumento() == DOC_FACTURA_FT)) {

                                if (loteController.existeLotesDisponiveis(
                                        idProduto, idArmazem)) {

                                    loteController.baixarStock(
                                            idProduto,
                                            idArmazem,
                                            item.getQuantidade(),
                                            cod_venda
                                    );
                                }
                            }

                            if (getIdDocumento() == DOC_FACTURA_RECIBO_FR
                                    || getIdDocumento() == DOC_FACTURA_FT) {

                                actualizar_quantidade(
                                        idProduto,
                                        item.getQuantidade(),
                                        conexaoLocal
                                );
                            }

                        }
                    }
                }

            } catch (Exception e) {
                throw new Exception("Erro ao processar item " + (i + 1) + ": " + e.getMessage());
            }
        }
    }

    private static boolean procedimentoPagamentoMensalidadeServico(int vendaId, int idCliente, int idProduto, String mes, BDConexao conexaoParm) {

        mesRhController = new MesRhController(conexaoParm.getConnectionAtiva());
        int mesId = mesRhController.getIdByDescricao(mes);
        PagamentoMensalidade pagamentoMensalidade = new PagamentoMensalidade();
        pagamentoMensalidade.setClienteId(idCliente);
        pagamentoMensalidade.setProdutoId(idProduto);
        pagamentoMensalidade.setMesId(mesId);
        pagamentoMensalidade.setVendaId(vendaId);
        pagamentoMensalidade.setDataCadastro(new Date());

        return pagamentoMensalidadeController.salvar(pagamentoMensalidade);

    }

    private static boolean data_documento_superior_ou_igual_ao_ultimo_doc() {
        //buscando o id do documento.
        int pk_documento = getIdDocumento();
        //buscando o id do ano ecoonomico.
        int pk_ano_economico = getIdAnoEconomico();

        //busca o último documento da série em questão.
        // Integer cod_ultima_venda = vendaDao.getLastVenda( pk_documento );
        Integer cod_ultima_venda = vendasController.getLastCodigoVenda(pk_documento, pk_ano_economico);
        if (cod_ultima_venda != 0) {

            //busca o objecto para retirar apenas a data do seu procesamento
            TbVenda venda_local = (TbVenda) vendasController.findById(cod_ultima_venda);
            //retirando a data do documebto
            Date data_ultimo_documento = venda_local.getDataVenda();
            //pegando a data do documento (data actual do sistema)
            Date data_actual = dc_data_documento.getDate();
            return MetodosUtil.maior_data_1_data_2(data_actual, data_ultimo_documento)
                    || MetodosUtil.igual_data_1_data_2(data_actual, data_ultimo_documento);

        } else {
            return true;
        }

    }

    private static String getNomeCliente() {
        return cmbCliente.getSelectedItem().toString();
    }

    private static String getClienteNif() {
        try {
            TbCliente cliente = (TbCliente) clientesController.findById(getIdCliente());

            String nif = cliente.getNif();
            System.out.println("NIF CLIENTE: " + nif);
            if (nif.equals("")) {
                return DVML.NUMBER_NIF_GENERICO;
            }
            return nif;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static int getCodigoProduto() {
        //return conexao.getCodigoPublico("tb_produto", String.valueOf(  cmbProduto.getSelectedItem()));   

        return produtosController.findByDesignacao(cmbProduto.getSelectedItem().toString()).getCodigo();

    }

    public static BigDecimal getValor_entregue() {
        return getTotalAOALiquido();
    }

    public static double getTroco() {

        try {
            return CfMethods.parseMoedaFormatada(FormaPagamentoVisao.lb_troco.getText());
        } catch (Exception e) {
        }

        return 0d;

    }

    // Componentes da interface (criados em VendaComponentes)
    javax.swing.JButton btnCancelar;
    static javax.swing.JButton btnFormaPagamento;
    static javax.swing.JButton btnProcessar;
    javax.swing.JButton btnSemFormaPagamento;
    public static javax.swing.JButton btn_abertura_dia_venda;
    public static javax.swing.JButton btn_adicionar;
    static javax.swing.JButton btn_fecho_dia_venda;
    public static javax.swing.JButton btn_remover;
    javax.swing.ButtonGroup buttonGroup1;
    javax.swing.JCheckBox ck_A4;
    public static javax.swing.JCheckBox ck_ComVirgula;
    javax.swing.JCheckBox ck_Duplicada;
    public static javax.swing.JCheckBox ck_S_A6;
    public static javax.swing.JCheckBox ck_simplificada;
    public static javax.swing.JCheckBox ck_simplificada_A7;
    public static javax.swing.JCheckBox ck_simplificada_O;
    public static javax.swing.JCheckBox ck_simplificada_O_S;
    static javax.swing.JComboBox<String> cmbAnoEconomico;
    public static javax.swing.JComboBox cmbArmazem;
    public static javax.swing.JComboBox cmbCliente;
    static javax.swing.JComboBox cmbMoeda;
    public static javax.swing.JComboBox cmbProduto;
    public static javax.swing.JComboBox cmbSubFamilia;
    public static javax.swing.JComboBox cmbTipoDocumento;
    static visao.design.CampoData dc_data_documento;
    static visao.design.CampoData dc_data_vencimento;
    javax.swing.JButton jButton1;
    javax.swing.JButton jButton2;
    javax.swing.JButton jButton3;
    javax.swing.JButton jButton4;
    javax.swing.JButton jButton5;
    javax.swing.JButton jButton6;
    javax.swing.JLabel jLabel1;
    javax.swing.JLabel jLabel2;
    javax.swing.JLabel jLabel9;
    javax.swing.JScrollPane jScrollPane1;
    javax.swing.JLabel jlEmpresa;
    javax.swing.JLabel jlStockNegativo;
    javax.swing.JLabel lbCliente;
    javax.swing.JLabel lbCliente1;
    javax.swing.JLabel lbClienteConsumidorFinal;
    javax.swing.JLabel lbCodigoProduto;
    javax.swing.JLabel lbCodigoProduto2;
    javax.swing.JLabel lbCodigoProduto3;
    javax.swing.JLabel lbCodigoProduto4;
    javax.swing.JLabel lbCodigoProduto5;
    javax.swing.JLabel lbDescontoFinanceiro;
    javax.swing.JLabel lbQuantidade;
    javax.swing.JLabel lbQuantidadeStock;
    public static javax.swing.JLabel lbValorPorExtenco;
    javax.swing.JLabel lb_nome_usuario;
    static javax.swing.JLabel lb_proximo_documento;
    static javax.swing.JRadioButton rbArmazem;
    static javax.swing.JRadioButton rbArmazem1;
    static javax.swing.JRadioButton rbMostrar;
    static javax.swing.JRadioButton rbTranstorno;
    static javax.swing.JSpinner sp_desconto_financeiro;
    static javax.swing.JSpinner spnCopia;
    public static javax.swing.JTable table;
    static javax.swing.JTextField txtBuscaRef;
    javax.swing.JTextField txtCodClientePesquisa;
    public static javax.swing.JTextField txtCodigoBarra;
    public static javax.swing.JTextField txtCodigoManual;
    public static javax.swing.JTextField txtCodigoProduto;
    static javax.swing.JTextField txtIniciaisCliente;
    javax.swing.JTextField txtLocal;
    static javax.swing.JTextField txtNifClientePesquisa;
    static javax.swing.JTextField txtNomeConsumidorFinal;
    public static javax.swing.JTextField txtPreco;
    public static javax.swing.JTextField txtQuantidadeStock;
    public static javax.swing.JTextField txtQuatindade;
    public static javax.swing.JTextField txtTotalPagar;
    public static javax.swing.JTextField txtTotalPagarGeral;
    static javax.swing.JLabel txtTotalPagarRetencao;
    private VendaLayout layoutVenda;

    public boolean validar() {
        boolean documentoIsFA = DVML.DOC_FACTURA_FT == getIdDocumento();
        boolean documentoIsPP = DVML.DOC_FACTURA_PROFORMA_PP == getIdDocumento();
        boolean seguradoras = DVML.DOC_FACTURA_PROFORMA_PP == getIdDocumento();
        if (cmbCliente.getSelectedItem().equals("--Seleccione o Cliente--")) {

            JOptionPane.showMessageDialog(null, "Por favor, Seleccione ou Digite o nome do Cliente!");
            txtIniciaisCliente.requestFocus();
            Formularios.marcarErro(txtIniciaisCliente, true);
            return false;

        } else if (documentoIsFA && dc_data_vencimento.getDate() == null || documentoIsPP && dc_data_vencimento.getDate() == null) {

            JOptionPane.showMessageDialog(null, "Por favor, Seleccione a data de Vencimento para este cliente!");
            dc_data_vencimento.requestFocus();
            Formularios.marcarErro(dc_data_vencimento, true);
            return false;

        }

        Formularios.marcarErro(txtIniciaisCliente, false);
        Formularios.marcarErro(dc_data_vencimento, false);
        return true;
    }

    private void refresh_table() {

        DefaultTableModel modelo = (DefaultTableModel) table.getModel();

        double preco = 0, desconto = 0, sub_total_linha = 0, sub_total_linha_com_iva = 0, taxa = 0, taxa_r = 0, sub_total_linha_retencao = 0;
        int idProduto, qtd;

        for (int i = 0; i < modelo.getRowCount(); i++) {

            try {

                /**
                 * Recupera os valores da tabela
                 */
                idProduto = Integer.parseInt(modelo.getValueAt(i, 0).toString());
                qtd = Integer.parseInt(modelo.getValueAt(i, 4).toString());
                taxa = Double.parseDouble(modelo.getValueAt(i, 6).toString());
                taxa_r = Double.parseDouble(modelo.getValueAt(i, 7).toString());
                //desconto_percentagem = Double.parseDouble( modelo.getValueAt( i, 4 ).toString() );

                //busca o preço em função do câmbio
                preco = getPreco(idProduto, qtd);

                /**
                 * Processa os valores para serem actualizados na tabela
                 */
                desconto = getDesconto_produto(preco, qtd);

                sub_total_linha = (preco * qtd) - desconto;
                sub_total_linha_com_iva = getValorComImposto(qtd, getTaxaImposto(idProduto), preco, desconto);

                /**
                 * actualiza os valores na tabela
                 */
                modelo.setValueAt(CfMethods.formatarComoMoeda(preco), i, 3);
                modelo.setValueAt(CfMethods.formatarComoMoeda(desconto), i, 5);
                modelo.setValueAt(CfMethods.formatarComoMoeda(sub_total_linha_retencao), i, 8);
                modelo.setValueAt(CfMethods.formatarComoMoeda(sub_total_linha), i, 9);
                modelo.setValueAt(CfMethods.formatarComoMoeda(sub_total_linha_com_iva), i, 10);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        setTotalRetencao();
        setTotalPagar();
        calculaTotalIVA();
        valor_por_extenco();

    }

    private static void calculaTotalIVA() {
        DefaultTableModel modelo = (DefaultTableModel) table.getModel();
        BigDecimal totalIva = BigDecimal.ZERO;

        for (int i = 0; i < modelo.getRowCount(); i++) {
            Object precoObj = modelo.getValueAt(i, 3);
            Object qtdObj = modelo.getValueAt(i, 4);
            Object ivaObj = modelo.getValueAt(i, 6);

            // Linha incompleta → ignora
            if (precoObj == null || qtdObj == null || ivaObj == null) {
                continue;
            }

            String precoTxt = precoObj.toString().trim();
            String qtdTxt = qtdObj.toString().trim();
            String ivaTxt = ivaObj.toString().trim();

            // Células vazias → ignora linha
            if (precoTxt.isEmpty() || qtdTxt.isEmpty() || ivaTxt.isEmpty()) {
                continue;
            }

            // --------------- PARSE SEGURO ---------------
            BigDecimal preco;
            try {
                preco = BigDecimal.valueOf(CfMethods.parseMoedaFormatada(precoTxt));
            } catch (Exception e) {
                continue; // Preço inválido → ignora linha
            }

            BigDecimal quantidade;
            try {
                quantidade = new BigDecimal(qtdTxt);
            } catch (Exception e) {
                continue; // Quantidade inválida → ignora linha
            }

            BigDecimal taxaIva;
            try {
                taxaIva = new BigDecimal(ivaTxt);
            } catch (Exception e) {
                taxaIva = BigDecimal.ZERO;
            }

            BigDecimal desconto = BigDecimal.ZERO;

            // cálculo
            BigDecimal valorIva = getIVA(quantidade, taxaIva, preco, desconto);
            totalIva = totalIva.add(valorIva);
        }

//    total_iva = MetodosUtil.valorCasasDecimaisNovoBD(totalIva.doubleValue(), CASAS_DECIMAIS);
        total_iva = MetodosUtil.valorCasasDecimaisNovo(totalIva.doubleValue(), CASAS_DECIMAIS);
        System.out.println("(*)TOTAL IVA: " + total_iva);
    }

//    private static void calculaTotalIVA()
//    {
//        DefaultTableModel modelo = (DefaultTableModel) table.getModel();
//        BigDecimal totalIva = BigDecimal.ZERO;
//
//        for ( int i = 0; i < modelo.getRowCount(); i++ )
//        {
//            // Preço unitário (coluna 3)
//            BigDecimal preco = BigDecimal.valueOf(
//                    CfMethods.parseMoedaFormatada( modelo.getValueAt( i, 3 ).toString() )
//            );
//
//            // Quantidade (coluna 4)
//            BigDecimal quantidade = new BigDecimal( modelo.getValueAt( i, 4 ).toString() );
//
//            // Taxa de IVA (coluna 6)
//            BigDecimal taxaIva = new BigDecimal( modelo.getValueAt( i, 6 ).toString() );
//
//            // Desconto (se aplicável - aqui ainda é zero porque está comentado no original)
//            BigDecimal desconto = BigDecimal.ZERO;
//
//            // Valor do IVA calculado
//            BigDecimal valorIva = getIVA( quantidade, taxaIva, preco, desconto );
//
//            totalIva = totalIva.add( valorIva );
//        }
//
//        // Arredondar o total de IVA
//        total_iva = MetodosUtil.valorCasasDecimaisNovo( totalIva.doubleValue(), CASAS_DECIMAIS );
//        System.out.println( "(*)TOTAL IVA: " + total_iva );
//    }
    private static void refresh_table(int idMoeda) {

        Moeda moeda_local = (Moeda) moedasController.findById(idMoeda);
        DefaultTableModel modelo = (DefaultTableModel) table.getModel();

        double preco = 0, desconto = 0, sub_total_linha = 0, sub_total_linha_com_iva = 0, taxa = 0, taxa_r = 0;
        int idProduto, qtd;

        for (int i = 0; i < modelo.getRowCount(); i++) {

            try {

                /**
                 * Recupera os valores da tabela
                 */
                idProduto = Integer.parseInt(modelo.getValueAt(i, 0).toString());
                qtd = Integer.parseInt(modelo.getValueAt(i, 4).toString());
                taxa = Double.parseDouble(modelo.getValueAt(i, 6).toString());
                taxa_r = Double.parseDouble(modelo.getValueAt(i, 7).toString());

                //busca o preço em função do câmbio em funcção da moeda
                preco = getPreco(idProduto, qtd, idMoeda);

                /**
                 * Processa os valores para serem actualizados na tabela
                 */
                desconto = getDesconto_produto(preco, qtd);
                sub_total_linha = (preco * qtd) - desconto;
                sub_total_linha_com_iva = getValorComImposto(qtd, getTaxaImposto(idProduto), preco, desconto);

                /**
                 * actualiza os valores na tabela
                 */
                modelo.setValueAt(CfMethods.formatarComoMoeda(preco), i, 3);
                modelo.setValueAt(CfMethods.formatarComoMoeda(desconto), i, 5);
                modelo.setValueAt(CfMethods.formatarComoMoeda(sub_total_linha), i, 8);
                modelo.setValueAt(CfMethods.formatarComoMoeda(sub_total_linha_com_iva), i, 9);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        setTotalPagar();
        setTotalRetencao();
        calculaTotalIVA();
        valor_por_extenco(moeda_local);

    }

    public static void procedimento_adicionar_sem_transtorno(String mes, double qtd) {
        try {
            if (!campos_invalidos()) {
                if (!isProdutoExpirado(getCodigoProduto())) {
                    TbProduto produto = (TbProduto) produtosController.findById(getCodigoProduto());
                    if (isStocavel(produto.getStocavel())) {

                        if (estado_critico()) {
                            JOptionPane.showMessageDialog(null, "O produto: " + produto.getDesignacao() + " precisa de ser actualizado no stock", DVML.DVML_COMERCIAL, JOptionPane.WARNING_MESSAGE);
                        }
                        adicionar_produto(mes, qtd);

                    } else {
                        adicionar_produto(mes, qtd);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Impossivel adicionar o produto porque já foi expirado.", "Aviso", JOptionPane.WARNING_MESSAGE);
                }

            } else {
                JOptionPane.showMessageDialog(null, "Por Favor Digite a Quantidade");
            }

        } catch (SQLException ex) {

        }

    }

    public static void procedimento_adicionar(String mes, double qtd) {
        boolean documentoIsFA = DVML.DOC_FACTURA_FT == getIdDocumento();
        boolean documentoIsPP = DVML.DOC_FACTURA_PROFORMA_PP == getIdDocumento();
        boolean documentoIsFR = DVML.DOC_FACTURA_RECIBO_FR == getIdDocumento();

        try {

            if (!campos_invalidos()) {
                if (!isProdutoExpirado(getCodigoProduto())) {
                    TbProduto produto = (TbProduto) produtosController.findById(getCodigoProduto());
                    System.out.println("PRODUTO STOCAVEL: " + produto.getStocavel());
                    boolean valorStocacel = isStocavel(produto.getStocavel());
                    System.out.println("VALOR STOCACEL" + valorStocacel);
                    if (isStocavel(produto.getStocavel()) && (documentoIsFR || documentoIsFA)) {
                        if (possivel_quantidade()) {
                            if (estado_critico()) {
                                JOptionPane.showMessageDialog(null, "O produto: " + produto.getDesignacao() + " precisa de ser actualizado no stock\nEmitindo compra ou transferência!", DVML.DVML_COMERCIAL, JOptionPane.WARNING_MESSAGE);
                            }
                            adicionar_produto(mes, qtd);
                        } else {
                            JOptionPane.showMessageDialog(null, "O produto: " + produto.getDesignacao() + " não pode ser vendido pra esta quantidade\nDeve-se emitir compra ou tranferência pra preencher o stock!", DVML.DVML_COMERCIAL, JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        adicionar_produto(mes, qtd);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Impossivel adicionar o produto porque já foi expirado.", "Aviso", JOptionPane.WARNING_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Por Favor Digite a Quantidade");
            }

        } catch (SQLException ex) {

        }

    }

    private static boolean isProdutoExpirado(int codigoProduto) {
        // return produtoDao.produtoExpirado( codigoProduto );
        return false;

    }

    private void accao_codigo_barra_enter() {
        try {
            String codigo_barra = txtCodigoBarra.getText().trim();
            TbProduto produtoLocal = produtosController.findByCodBarra(codigo_barra);
            double qtd = Double.parseDouble(txtQuatindade.getText());
            procedimentoAdicionarTabela(produtoLocal, qtd);
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(VendaModernaVisao.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Não existe produto com este código de barra.", DVML.DVML_COMERCIAL, JOptionPane.ERROR_MESSAGE);
        }

    }

    public void adicionar_botao_retificar(String mes, double qtd) {

        try {
            if (!campos_invalidos()) {

                TbProduto produto = (TbProduto) produtosController.findById(getCodigoProduto());
                if (isStocavel(produto.getStocavel())) {

                    if (possivel_quantidade()) {

                        if (estado_critico()) {
                            JOptionPane.showMessageDialog(null, "O produto: " + produto.getDesignacao() + " precisa de ser actualizado no stock", DVML.DVML_COMERCIAL, JOptionPane.WARNING_MESSAGE);
                        }
                        adicionar_produto(mes, qtd);
                    } else {
                        JOptionPane.showMessageDialog(null, "O produto: " + produto.getDesignacao() + " não pode ser vendido pra esta quantidade\nDeve-se emitir compra ou tranferência pra preencher o stock!", DVML.DVML_COMERCIAL, JOptionPane.ERROR_MESSAGE);
                    }

                } else {
                    adicionar_produto(mes, qtd);
                }

            } else {
                JOptionPane.showMessageDialog(null, "Por Favor Digite a Quantidade");
            }

        } catch (SQLException ex) {

        }

    }

    public static void adicionar_produto(String mes, double qtd) throws SQLException {

        DefaultTableModel modelo = (DefaultTableModel) table.getModel();

        // 1. Fechar edição atual
        if (table.isEditing()) {
            TableCellEditor editor = table.getCellEditor();
            if (editor != null) {
                editor.stopCellEditing();
            }
        }

        int codigoProdutoLocal = getCodigoProduto();
        String descricao_produto = getDescricao_Produto();

        if (!mes.equals("")) {
            descricao_produto = "Pgt. Ref. de " + descricao_produto
                    + " de #" + mes;
        }

        // Evitar produto duplicado
        if (!statusConversao
                && exist_produto_tabela_formulario(descricao_produto)) {
            JOptionPane.showMessageDialog(
                    null, "O produto já consta na tabela."
            );
            return;
        }

        // Validar quantidade
        if (validar_zero() || qtd <= 0) {
            JOptionPane.showMessageDialog(
                    null,
                    "Atenção!\nA quantidade a sair deve ser superior a zero."
            );
            return;
        }

        // =====================================================
        // 2. CONSULTAR LOTES FEFO E ATUALIZAR PAINEL LATERAL
        // =====================================================
        int armazemAtual = getCodigoArmazem(); // mantem se mais abaixo usares esta variavel

        String erroLotes = actualizarPainelLotes(codigoProdutoLocal, descricao_produto, qtd);
        if (erroLotes != null) {
            JOptionPane.showMessageDialog(null, erroLotes, "Lotes / stock", JOptionPane.WARNING_MESSAGE);
            return;

        }

        // =====================================================
        // 3. CÁLCULOS ORIGINAIS
        // =====================================================
        String unidade = getUnidade_Produto();

        BigDecimal quantidade = BigDecimal.valueOf(qtd);
        BigDecimal preco = BigDecimal.valueOf(
                getPreco(codigoProdutoLocal, quantidade.doubleValue())
        );
        BigDecimal descontoPercent = BigDecimal.valueOf(
                getDescontoPercentagem()
        );
        BigDecimal taxaIva = BigDecimal.valueOf(
                getTaxaImpostoIva(codigoProdutoLocal)
        );
        BigDecimal taxaRet = BigDecimal.valueOf(
                getTaxaImpostoRet(codigoProdutoLocal)
        );

        BigDecimal valorIliquidoUnit = FinanceUtils.getValorIliquido(
                BigDecimal.ONE, preco, descontoPercent
        );

        double valorLiquidoDoubleUnit = FinanceUtils.getValorComIVA(
                1d,
                taxaIva.doubleValue(),
                valorIliquidoUnit.doubleValue(),
                descontoPercent.doubleValue()
        );

        BigDecimal valorIliquido = valorIliquidoUnit.multiply(quantidade);

        BigDecimal totalComIva = BigDecimal.valueOf(
                valorLiquidoDoubleUnit * quantidade.doubleValue()
        );

        String totalRetencao = CfMethods.formatarComoMoeda(
                MetodosUtil.getValorComRetencao(
                        quantidade.doubleValue(),
                        taxaRet.doubleValue(),
                        preco.doubleValue(),
                        descontoPercent.doubleValue()
                )
        );

        // =====================================================
        // 4. OBTER PRIMEIRA LINHA VAZIA
        // =====================================================
        int row = -1;

        for (int i = 0; i < modelo.getRowCount(); i++) {
            if (linhaTabelaEstaVazia(modelo, i)) {
                row = i;
                break;
            }
        }

        if (row == -1) {
            modelo.addRow(new Object[]{
                "", "", "", "", "", "", "", "", "", "", ""
            });
            row = modelo.getRowCount() - 1;
        }

        // =====================================================
        // 5. PREENCHER A JTable
        // =====================================================
        modelo.setValueAt(codigoProdutoLocal, row, 0);
        modelo.setValueAt(descricao_produto, row, 1);
        modelo.setValueAt(unidade, row, 2);
        modelo.setValueAt(CfMethods.formatarComoMoeda(preco), row, 3);
        modelo.setValueAt(qtd, row, 4);
        modelo.setValueAt(descontoPercent, row, 5);
        modelo.setValueAt(taxaIva, row, 6);
        modelo.setValueAt(taxaRet, row, 7);
        modelo.setValueAt(totalRetencao, row, 8);
        modelo.setValueAt(
                CfMethods.formatarComoMoeda(valorIliquido), row, 9
        );
        modelo.setValueAt(
                CfMethods.formatarComoMoeda(totalComIva), row, 10
        );

        // 6. Garantir última linha vazia
        int last = modelo.getRowCount() - 1;

        if (!linhaTabelaEstaVazia(modelo, last)) {
            modelo.addRow(new Object[]{
                "", "", "", "", "", "", "", "", "", "", ""
            });
        }

        // =====================================================
        // 7. IMPRESSÃO ORIGINAL
        // =====================================================
        TbProduto findByDesignacao = produtosController.findByDesignacao(
                cmbProduto.getSelectedItem().toString()
        );

        int idPedido = 0;

        TbMesas mesaEntity = (TbMesas) mesasController.findById(DVML.MESA_BALCAO);

        TbLugares lugarEntity = (TbLugares) lugaresController.findById(DVML.LUGAR_BALCAO);

        TbUsuario usuarioEntity = (TbUsuario) usuariosController.findById(cod_usuario);

        String usuario = usuarioEntity.getNome();

        if (findByDesignacao.getCozinha().equals(DVML.ENVIAR_TICKET)) {

            MetodosUtil.imprimir_cozinha(
                    findByDesignacao,
                    idPedido,
                    mesaEntity.getDesignacao(),
                    lugarEntity.getDesignacao(),
                    usuario,
                    "Activo",
                    quantidade.intValue(),
                    dadosInstituicaoController
            );

        } else if (findByDesignacao.getCozinha().equals(DVML.ENVIAR_SALA)) {

            MetodosUtil.imprimir_sala(
                    findByDesignacao,
                    idPedido,
                    mesaEntity.getDesignacao(),
                    lugarEntity.getDesignacao(),
                    usuario,
                    "Activo",
                    quantidade.intValue(),
                    dadosInstituicaoController
            );
        }

        // =====================================================
        // 8. ATUALIZAR TOTAIS
        // =====================================================
        setTotalRetencao();
        setTotalPagar();
        setTotalPagarGeral();
        calculaTotalIVA();
        valor_por_extenco();

        cursorLinha();
    }

    public static void adicionar_produto_antigo(String mes, double qtd) throws SQLException {
        DefaultTableModel modelo = (DefaultTableModel) table.getModel();

        // --------------------------
        // 1. FECHAR EDIÇÃO ATUAL
        // --------------------------
        if (table.isEditing()) {
            TableCellEditor editor = table.getCellEditor();
            if (editor != null) {
                editor.stopCellEditing();
            }
        }

        int codigoProdutoLocal = getCodigoProduto();
        String descricao_produto = getDescricao_Produto();

        if (!mes.equals("")) {
            descricao_produto = "Pgt. Ref. de " + descricao_produto + " de #" + mes;
        }

        if (!statusConversao) {
            // Evitar produto duplicado
            if (exist_produto_tabela_formulario(descricao_produto)) {
                JOptionPane.showMessageDialog(null, "O produto já consta na tabela.");
                return;
            }
        }

        // Quantidade inválida
        if (validar_zero()) {
            JOptionPane.showMessageDialog(null, "Atenção\nA quantidade a sair não pode ser igual a zero!");
            return;
        }

        // --------------------------
        // 2. CÁLCULOS
        // --------------------------
        String unidade = getUnidade_Produto();

        BigDecimal quantidade = BigDecimal.valueOf(qtd);
        BigDecimal preco = BigDecimal.valueOf(getPreco(codigoProdutoLocal, quantidade.doubleValue()));
        BigDecimal descontoPercent = BigDecimal.valueOf(getDescontoPercentagem());
        BigDecimal taxaIva = BigDecimal.valueOf(getTaxaImpostoIva(codigoProdutoLocal));
        BigDecimal taxaRet = BigDecimal.valueOf(getTaxaImpostoRet(codigoProdutoLocal));

        BigDecimal valorIliquidoUnit = FinanceUtils.getValorIliquido(
                new BigDecimal(1d), preco, descontoPercent
        );//valor unitario

        double valorLiquidoDoubleUnit = FinanceUtils.getValorComIVA(
                1d,
                taxaIva.doubleValue(),
                valorIliquidoUnit.doubleValue(),
                descontoPercent.doubleValue()
        ); // valor unitario

        BigDecimal valorIliquido = valorIliquidoUnit.multiply(quantidade);
        BigDecimal totalComIva = BigDecimal.valueOf(valorLiquidoDoubleUnit * quantidade.doubleValue());

        String totalRetencao = CfMethods.formatarComoMoeda(
                MetodosUtil.getValorComRetencao(
                        quantidade.doubleValue(),
                        taxaRet.doubleValue(),
                        preco.doubleValue(),
                        descontoPercent.doubleValue()
                )
        );

        // --------------------------
        // 3. Obter PRIMEIRA linha vazia
        // --------------------------
        int row = -1;
        for (int i = 0; i < modelo.getRowCount(); i++) {
            if (linhaTabelaEstaVazia(modelo, i)) {
                row = i;
                break;
            }
        }

        // Se não existe → criar nova linha
        if (row == -1) {
            modelo.addRow(new Object[]{
                "", "", "", "", "", "", "", "", "", "", ""
            });
            row = modelo.getRowCount() - 1;
        }

        // --------------------------
        // 4. Preencher a linha (BUG Resolvido)
        // --------------------------
        modelo.setValueAt(codigoProdutoLocal, row, 0);
        modelo.setValueAt(descricao_produto, row, 1);
        modelo.setValueAt(unidade, row, 2);
        modelo.setValueAt(CfMethods.formatarComoMoeda(preco), row, 3);
        modelo.setValueAt(qtd, row, 4);
        modelo.setValueAt(descontoPercent, row, 5);
        modelo.setValueAt(taxaIva, row, 6);
        modelo.setValueAt(taxaRet, row, 7);
        modelo.setValueAt(totalRetencao, row, 8);
        modelo.setValueAt(CfMethods.formatarComoMoeda(valorIliquido), row, 9);
        modelo.setValueAt(CfMethods.formatarComoMoeda(totalComIva), row, 10);

        // --------------------------
        // 5. Criar sempre última linha vazia
        // --------------------------
        int last = modelo.getRowCount() - 1;
        if (!linhaTabelaEstaVazia(modelo, last)) {
            modelo.addRow(new Object[]{
                "", "", "", "", "", "", "", "", "", "", ""
            });
        }

        // --------------------------
        // 6. Impressão
        // --------------------------
        TbProduto findByDesignacao = produtosController.findByDesignacao(
                cmbProduto.getSelectedItem().toString()
        );

        int idPedido = 0;
        TbMesas mesaEntity = (TbMesas) mesasController.findById(DVML.MESA_BALCAO);
        TbLugares lugarEntity = (TbLugares) lugaresController.findById(DVML.LUGAR_BALCAO);
        TbUsuario usuarioEntity = (TbUsuario) usuariosController.findById(cod_usuario);
        String usuario = usuarioEntity.getNome();

        if (findByDesignacao.getCozinha().equals(DVML.ENVIAR_TICKET)) {
            MetodosUtil.imprimir_cozinha(
                    findByDesignacao, idPedido,
                    mesaEntity.getDesignacao(), lugarEntity.getDesignacao(),
                    usuario, "Activo", quantidade.intValue(), dadosInstituicaoController
            );
        } else if (findByDesignacao.getCozinha().equals(DVML.ENVIAR_SALA)) {
            MetodosUtil.imprimir_sala(
                    findByDesignacao, idPedido,
                    mesaEntity.getDesignacao(), lugarEntity.getDesignacao(),
                    usuario, "Activo", quantidade.intValue(), dadosInstituicaoController
            );
        }

        // --------------------------
        // 7. Totais
        // --------------------------
        setTotalRetencao();
        setTotalPagar();
        setTotalPagarGeral();
        calculaTotalIVA();
        valor_por_extenco();

//        // --------------------------
//        // 8. Foco na próxima linha
//        // --------------------------
//        int novaLinha = modelo.getRowCount() - 1;
//
//        // Garante que o foco cai na coluna 0 da nova linha
//        SwingUtilities.invokeLater( () ->
//        {
//            table.changeSelection( novaLinha, 0, false, false );
//            table.editCellAt( novaLinha, 0 );
//            table.requestFocus();
//        } );
//
//        txtQuatindade.setText( "1" );
        cursorLinha();
    }

//    public static void adicionar_produto( String mes ) throws SQLException
//    {
//        DefaultTableModel modelo = (DefaultTableModel) table.getModel();
//        table.setRowHeight( 28 );
//
    ////        // --- 1. CONFIRMAR edição atual para não perder o valor da coluna 0 ---
////        if ( table.isEditing() )
////        {
////            table.getCellEditor().stopCellEditing();
////        }
//        int codigoProdutoLocal = getCodigoProduto();
//        String descricao_produto = getDescricao_Produto();
//
//        if ( !mes.equals( "" ) )
//        {
//            descricao_produto = "Pgt. Ref. de " + descricao_produto + " de #" + mes;
//        }
//
//        // Não permitir duplicados
//        if ( exist_produto_tabela_formulario( descricao_produto ) )
//        {
//            JOptionPane.showMessageDialog( null, "O produto já consta na tabela." );
//            return;
//        }
//
//        // Quantidade inválida
//        if ( validar_zero() )
//        {
//            JOptionPane.showMessageDialog( null, "Atenção\nA quantidade a sair não pode ser igual a zero!" );
//            return;
//        }
//
//        // --------------------------
//        // 2. CÁLCULOS ORIGINAIS
//        // --------------------------
//        String unidade = getUnidade_Produto();
//
//        BigDecimal qtd = BigDecimal.valueOf( getQuantidade() );
//        BigDecimal preco = BigDecimal.valueOf( getPreco() );
//        BigDecimal descontoPercent = BigDecimal.valueOf( getDescontoPercentagem() );
//        BigDecimal taxaIva = BigDecimal.valueOf( getTaxaImpostoIva( codigoProdutoLocal ) );
//        BigDecimal taxaRet = BigDecimal.valueOf( getTaxaImpostoRet( codigoProdutoLocal ) );
//
////        BigDecimal valorIliquido = FinanceUtils.getValorIliquido( qtd, preco, descontoPercent );
//        BigDecimal valorIliquido = FinanceUtils.getValorIliquido( new BigDecimal( 1 ), preco, descontoPercent );
//
//        double valorLiquidoDouble = FinanceUtils.getValorComIVA(
//                1d,
//                taxaIva.doubleValue(),
//                valorIliquido.doubleValue(),
//                descontoPercent.doubleValue()
//        );
//
//        valorIliquido = valorIliquido.multiply( qtd );
//
//        valorLiquidoDouble = valorLiquidoDouble * qtd.doubleValue();
//
//        BigDecimal totalComIva = BigDecimal.valueOf( valorLiquidoDouble );
//
//        String totalRetencao = CfMethods.formatarComoMoeda(
//                MetodosUtil.getValorComRetencao(
//                        qtd.doubleValue(),
//                        taxaRet.doubleValue(),
//                        preco.doubleValue(),
//                        descontoPercent.doubleValue()
//                )
//        );
//
//        // --------------------------
//        // 3. Encontrar PRIMEIRA linha vazia
//        // --------------------------
//        int row = -1;
//        for ( int i = 0; i < modelo.getRowCount(); i++ )
//        {
//            if ( linhaTabelaEstaVazia( modelo, i ) )
//            {
//                row = i;
//                break;
//            }
//        }
//
//        // Se não existe nenhuma linha vazia → cria nova
//        if ( row == -1 )
//        {
//            modelo.addRow( new Object[]
//            {
//                "", "", "", "", "", "", "", "", "", "", ""
//            } );
//            row = modelo.getRowCount() - 1;
//        }
//
//        // --------------------------
//        // 4. Preencher a linha
//        // --------------------------
//        modelo.setValueAt( codigoProdutoLocal, row, 0 );
//        modelo.setValueAt( descricao_produto, row, 1 );
//        modelo.setValueAt( unidade, row, 2 );
//        modelo.setValueAt( CfMethods.formatarComoMoeda( preco ), row, 3 );
//        modelo.setValueAt( qtd, row, 4 );
//        modelo.setValueAt( descontoPercent, row, 5 );
//        modelo.setValueAt( taxaIva, row, 6 );
//        modelo.setValueAt( taxaRet, row, 7 );
//        modelo.setValueAt( totalRetencao, row, 8 );
//        modelo.setValueAt( CfMethods.formatarComoMoeda( valorIliquido ), row, 9 );
//        modelo.setValueAt( CfMethods.formatarComoMoeda( totalComIva ), row, 10 );
//
//        // --------------------------
//        // 5. Adicionar linha vazia no FINAL sempre
//        // --------------------------
//        int last = modelo.getRowCount() - 1;
//        if ( !linhaTabelaEstaVazia( modelo, last ) )
//        {
//            modelo.addRow( new Object[]
//            {
//                "", "", "", "", "", "", "", "", "", "", ""
//            } );
//        }
//
//        // --------------------------
//        // 6. Impressão (mantido exatamente como o teu)
//        // --------------------------
//        TbProduto findByDesignacao = produtosController.findByDesignacao(
//                cmbProduto.getSelectedItem().toString()
//        );
//
//        int idPedido = 0;
//        TbMesas mesaEntity = (TbMesas) mesasController.findById( DVML.MESA_BALCAO );
//        TbLugares lugarEntity = (TbLugares) lugaresController.findById( DVML.LUGAR_BALCAO );
//        TbUsuario usuarioEntity = (TbUsuario) usuariosController.findById( cod_usuario );
//        String usuario = usuarioEntity.getNome();
//
//        if ( findByDesignacao.getCozinha().equals( DVML.ENVIAR_TICKET ) )
//        {
//            MetodosUtil.imprimir_cozinha( findByDesignacao, idPedido,
//                    mesaEntity.getDesignacao(), lugarEntity.getDesignacao(),
//                    usuario, "Activo", qtd.intValue(), dadosInstituicaoController );
//        }
//        else if ( findByDesignacao.getCozinha().equals( DVML.ENVIAR_SALA ) )
//        {
//            MetodosUtil.imprimir_sala( findByDesignacao, idPedido,
//                    mesaEntity.getDesignacao(), lugarEntity.getDesignacao(),
//                    usuario, "Activo", qtd.intValue(), dadosInstituicaoController );
//        }
//
//        // --------------------------
//        // 7. Totais
//        // --------------------------
//        setTotalRetencao();
//        setTotalPagar();
//        calculaTotalIVA();
//        valor_por_extenco();
//
//        // --------------------------
//        // 8. Foco na próxima linha
//        // --------------------------
//        int novaLinha = modelo.getRowCount() - 1;
//        table.changeSelection( novaLinha, 0, false, false );
//        table.editCellAt( novaLinha, 0 );
//        table.requestFocus();
//
//        txtQuatindade.setText( "1" );
////        txtCodigoProduto.requestFocus();
//
////        cursorLinha();
//    }
    private static boolean linhaTabelaEstaVazia(DefaultTableModel model, int row) {
        for (int col = 0; col < model.getColumnCount(); col++) {
            Object v = model.getValueAt(row, col);
            if (v != null && !v.toString().trim().isEmpty()) {
                return false;
            }
        }
        return true;
    }

//    public static void adicionar_produto( String mes ) throws SQLException
//    {
//        DefaultTableModel modelo = (DefaultTableModel) table.getModel();
//        int codigo_produto = getCodigoProduto();
//        //define a altura das linhas
//        table.setRowHeight( 28 );
//
//        String descricao_produto = getDescricao_Produto();
//
//        if ( !mes.equals( "" ) )
//        {
//            descricao_produto = "Pgt. Ref. de " + descricao_produto + " de #" + mes;
//        }
//
//        if ( !exist_produto_tabela_formulario( descricao_produto ) )
//        {
//            if ( !validar_zero() )
//            {
//                String unidade = getUnidade_Produto();
//
//                // Usar BigDecimal para valores monetários
//                BigDecimal qtd = BigDecimal.valueOf( getQuantidade() );
//                BigDecimal preco = BigDecimal.valueOf( getPreco() );
//                BigDecimal descontoPercent = BigDecimal.valueOf( getDescontoPercentagem() );
//                BigDecimal taxaIva = BigDecimal.valueOf( getTaxaImpostoIva( codigo_produto ) );
//                BigDecimal taxaRet = BigDecimal.valueOf( getTaxaImpostoRet( codigo_produto ) );
//
//                BigDecimal valorIliquido = FinanceUtils.getValorIliquido( qtd, preco, descontoPercent );
//
//                double valorLiquido = FinanceUtils.getValorComIVA(
//                        qtd.doubleValue(),
//                        taxaIva.doubleValue(),
//                        preco.doubleValue(),
//                        descontoPercent.doubleValue()
//                );
//
//                BigDecimal totalComIva = new BigDecimal( valorLiquido );
//
//                // Valor com Retenção
//                String total_linha_retencao = CfMethods.formatarComoMoeda(
//                        MetodosUtil.getValorComRetencao(
//                                qtd.doubleValue(),
//                                taxaRet.doubleValue(),
//                                preco.doubleValue(),
//                                descontoPercent.doubleValue()
//                        )
//                );
//
//                if ( primeiraLinhaVazia() )
//                {
//                    // Preenche a linha 0
//                    modelo.setValueAt( codigo_produto, 0, 0 );
//                    modelo.setValueAt( descricao_produto, 0, 1 );
//                    modelo.setValueAt( unidade, 0, 2 );
//                    modelo.setValueAt( CfMethods.formatarComoMoeda( preco ), 0, 3 );
//                    modelo.setValueAt( qtd, 0, 4 );
//                    modelo.setValueAt( descontoPercent, 0, 5 );
//                    modelo.setValueAt( taxaIva, 0, 6 );
//                    modelo.setValueAt( taxaRet, 0, 7 );
//                    modelo.setValueAt( total_linha_retencao, 0, 8 );
//                    modelo.setValueAt( CfMethods.formatarComoMoeda( valorIliquido ), 0, 9 );
//                    modelo.setValueAt( CfMethods.formatarComoMoeda( totalComIva ), 0, 10 );
//                }
//                else
//                {
//                    // A primeira linha já está ocupada → criar nova
//                    modelo.addRow( new Object[]
//                    {
//                        codigo_produto,
//                        descricao_produto,
//                        unidade,
//                        CfMethods.formatarComoMoeda( preco ),
//                        qtd,
//                        descontoPercent,
//                        taxaIva,
//                        taxaRet,
//                        total_linha_retencao,
//                        CfMethods.formatarComoMoeda( valorIliquido ),
//                        CfMethods.formatarComoMoeda( totalComIva )
//                    } );
//                }
//
//                // Impressão de ticket (cozinha ou sala)
//                TbProduto findByDesignacao = produtosController.findByDesignacao( cmbProduto.getSelectedItem().toString() );
//                int idPedido = 0;
//                TbMesas mesaEntity = (TbMesas) mesasController.findById( DVML.MESA_BALCAO );
//                TbLugares lugarEntity = (TbLugares) lugaresController.findById( DVML.LUGAR_BALCAO );
//                TbUsuario usuarioEntity = (TbUsuario) usuariosController.findById( cod_usuario );
//                String usuario = usuarioEntity.getNome();
//
//                if ( findByDesignacao.getCozinha().equals( DVML.ENVIAR_TICKET ) )
//                {
//                    MetodosUtil.imprimir_cozinha( findByDesignacao, idPedido, mesaEntity.getDesignacao(),
//                            lugarEntity.getDesignacao(), usuario, "Activo", qtd.intValue(), dadosInstituicaoController );
//                }
//                else if ( findByDesignacao.getCozinha().equals( DVML.ENVIAR_SALA ) )
//                {
//                    MetodosUtil.imprimir_sala( findByDesignacao, idPedido, mesaEntity.getDesignacao(),
//                            lugarEntity.getDesignacao(), usuarioEntity.getNome(), "Activo", qtd.intValue(), dadosInstituicaoController );
//                }
//            }
//            else
//            {
//                JOptionPane.showMessageDialog( null, "Atenção\nA quantidade a sair não pode ser igual a zero!" );
//            }
//        }
//        else
//        {
//            JOptionPane.showMessageDialog( null, "O produto já consta na tabela." );
//        }
//
//        // Atualização de totais
//        setTotalRetencao();
//        setTotalPagar();
//        calculaTotalIVA();
//        valor_por_extenco();
//
//        txtQuatindade.setText( String.valueOf( 1 ) );
//        txtCodigoProduto.requestFocus();
//    }
    public static boolean estado_critico() throws SQLException {

        return conexao.getQuantidade_minima_publico(getCodigoProduto(), getCodigoArmazem())
                < conexao.getQuantidade_Existente_Publico(getCodigoProduto(), getCodigoArmazem())
                && conexao.getQuantidade_Existente_Publico(getCodigoProduto(), getCodigoArmazem())
                <= conexao.getQuantidade_critica_public(getCodigoProduto(), getCodigoArmazem());
    }

    public static boolean possivel_quantidade() throws SQLException {

        double quant_possivel = conexao.getQuantidade_Existente_Publico(getCodigoProduto(), getCodigoArmazem())
                - conexao.getQuantidade_minima_publico(getCodigoProduto(), getCodigoArmazem());

        return quant_possivel >= getQuantidade();

    }

    public static boolean isStocavel(String status) {
        try {
            if (status.equals("true")) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return true;
        }

    }

    public static boolean campos_invalidos() {
        return txtQuatindade.getText().equals("");
    }

    //verifica se o produto existe na tabela do formulário visão isto é na jTable
    private static boolean exist_produto_tabela_formulario(String designacao) {

        DefaultTableModel modelo = (DefaultTableModel) table.getModel();

        for (int i = 0; i < modelo.getRowCount(); i++) {
            if (String.valueOf(table.getValueAt(i, 1)).equals(designacao)) {
                linha_actual = i;
                return true;
            }
        }
        return false;

    }

    public void calcularTroco() {

        String prefixo = "";
        double troco = 0;

//        System.out.println( "VALOR ENTREGUE " + txtValorEntregue.getText() );
        System.out.println("TOTAL A PAGAR " + txtTotalPagar.getText().trim());

//        double valor_entregue = Double.parseDouble( txtValorEntregue.getText() );
        double total_pagar = Double.parseDouble(txtTotalPagar.getText().trim());
//        troco = valor_entregue - total_pagar;

        System.out.println("TROCO " + troco);
//        txtTroco.setText( String.valueOf( MetodosUtil.retirar_dizimas( troco ) ).trim() );

    }

    private static void procedimentoAdicionarTabela2(TbProduto produto, String mes, double qtd) {
        try {
            if (!Objects.isNull(produto)) {
                Integer codTipoProduto = produto.getCodTipoProduto().getCodigo();
                TbTipoProduto tipoProduto = (TbTipoProduto) tipoProdutoController.findById(codTipoProduto);
                Integer codFamilia = tipoProduto.getFkFamilia().getPkFamilia();
                Familia familia = (Familia) familiaController.findById(codFamilia);
//                cmbFamilia.setSelectedItem( familia.getDesignacao() );
                cmbSubFamilia.setSelectedItem(tipoProduto.getDesignacao());

                cmbProduto.setModel(new DefaultComboBoxModel(produtosController.getVector()));
                cmbProduto.setSelectedItem(produto.getDesignacao());
                adicionar_preco_quantidade_anitgo();
                if (rbTranstorno.isSelected()) {
                    procedimento_adicionar_sem_transtorno(mes, qtd);
                } else {
                    procedimento_adicionar(mes, qtd);
                }
                txtCodigoProduto.setText("");
                txtCodigoBarra.setText("");
                txtQuatindade.setText("1");
                txtQuatindade.requestFocus();

            } else {
                JOptionPane.showMessageDialog(null, "Nao existe produto/servico relacionado com esta referencia");
            }

        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    public static void adicionar_preco_quantidade_anitgo() {
        try {
            if (txtQuatindade.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Não informou a quantidade, por favor informe a quantidade!");
            } else {
                TbStock stockLocal = stocksController.getStockByIdProdutoAndIdArmazem(getCodigoProduto(), getCodigoArmazem());
                if (stockLocal != (null)) {
                    if (stockLocal.getQuantidadeExistente() <= stockLocal.getQuantCritica()) {
                        txtQuantidadeStock.setBackground(Color.RED);
                        txtQuantidadeStock.setForeground(Color.BLACK);
                    } else {
                        txtQuantidadeStock.setBackground(new Color(51, 153, 0, 255));
                    }
                    TbPreco precoLocal = precosController.getLastIdPrecoByIdProduto(getCodigoProduto(), Double.parseDouble(txtQuatindade.getText()));
                    txtPreco.setText(String.valueOf(MetodosUtil.retirar_dizimas(precoLocal.getPrecoVenda().doubleValue())));
                    txtQuantidadeStock.setText(String.valueOf(stockLocal.getQuantidadeExistente()));
                }

            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger
                    .getLogger(VendaModernaVisao.class
                            .getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void adicionar_preco_quantidade_anitgo(int codigo_produto) {
        try {
            if (txtQuatindade.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Não informou a quantidade, por favor informe a quantidade!");
            } else {
                TbStock stockLocal = stocksController.getStockByIdProdutoAndIdArmazem(codigo_produto, getCodigoArmazem());
                if (stockLocal != (null)) {
                    if (stockLocal.getQuantidadeExistente() <= stockLocal.getQuantCritica()) {
                        txtQuantidadeStock.setBackground(Color.RED);
                        txtQuantidadeStock.setForeground(Color.BLACK);
                    } else {
                        txtQuantidadeStock.setBackground(new Color(51, 153, 0, 255));
                    }
//                    TbPreco precoLocal = precosController.getLastIdPrecoByIdProduto( codigo_produto, Double.parseDouble( txtQuatindade.getText() ) );
//                    txtPreco.setText( String.valueOf( MetodosUtil.retirar_dizimas( precoLocal.getPrecoVenda().doubleValue() ) ) );
                    txtQuantidadeStock.setText(String.valueOf(stockLocal.getQuantidadeExistente()));
                }

            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger
                    .getLogger(VendaModernaVisao.class
                            .getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void adicionar_botao(double qtd) {
        try {
            if (!campos_invalidos()) {

                if (!isProdutoExpirado(getCodigoProduto())) {
                    TbProduto produtoLocal = (TbProduto) produtosController.findById(getCodigoProduto());
                    if (isStocavel(produtoLocal.getStocavel())) {
                        if (possivel_quantidade()) {
                            if (estado_critico()) {
                                JOptionPane.showMessageDialog(null, "O produto: " + produtoLocal.getDesignacao() + " precisa de ser actualizado no stock\nEmitindo compra ou transfrência!", "DVML", JOptionPane.WARNING_MESSAGE);
                            }
                            adicionar_produto("", qtd);

                        } else {
                            JOptionPane.showMessageDialog(null, "O produto: " + produtoLocal.getDesignacao() + " não pode ser vendido pra esta quantidade\nDeve-se emitir compra ou tranferência pra preencher o stock!", "DVML", JOptionPane.ERROR_MESSAGE);
                        }

                    } else {
                        adicionar_produto("", qtd);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Produto não pode ser vendido porque foi expirado.", "Aviso", JOptionPane.WARNING_MESSAGE);
                }

            } else {
                JOptionPane.showMessageDialog(null, "Por Favor Digite a Quantidade");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao adicionar o produto: " + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }

//        } catch (SQLException ex) {
//
//        }
    }

    public static boolean validar_zero() {
        return Double.parseDouble(txtQuatindade.getText()) == 0;
    }

    public static void limpar() {

        txtQuatindade.setText("1");
        txtCodigoProduto.setText("");
        txtCodigoManual.setText("");
        txtTotalPagar.setText("0");
        txtCodigoBarra.setText("");
        txtNomeConsumidorFinal.setText("");
        dc_data_vencimento.setDate(null);
        txtBuscaRef.setText("");
//        txtObs.setText( "" );
        gorjeta = 0;
        reset_desconto_global();
        reset_valor_entregue();

    }

    private static void reset_desconto_global() {
        sp_desconto_financeiro.setModel(CfMethodsSwing.criarSpinnerDoubleModel(0.0, 10000000000.00, 0.0));
    }

    private static void reset_valor_entregue() {
//        txtValorEntregue.setText( "" );
    }

    public static boolean campos_invalido_imprimir() {

//        if ( getValor_entregue() < CfMethods.parseMoedaFormatada( txtTotal_AOA_liquido.getText() ) && ( getIdDocumento() != DVML.DOC_FACTURA_PROFORMA_PP ) && ( getIdDocumento() != DVML.DOC_FACTURA_FT ) )
        if (false) {
            JOptionPane.showMessageDialog(null, "O valor entregue tem quer ser maior ou igual ao Total a Pagar", "AVISO", JOptionPane.WARNING_MESSAGE);
//            txtValorEntregue.requestFocus();
            return true;
        }

        if (cambio == null) {
            JOptionPane.showMessageDialog(null, "Por favor seleccione a moeda", "AVISO", JOptionPane.WARNING_MESSAGE);
            return true;
        }

        if (cmbTipoDocumento == null) {
            JOptionPane.showMessageDialog(null, "Por favor seleccione o Tipo de Documento", "AVISO", JOptionPane.WARNING_MESSAGE);
            return true;
        }

        return false;

    }

    public static boolean possivel_quantidade(int cod_produto, double qtd) {

        //System.err.println(conexao.getQuantidade_Existente_Publico(getCodigoProduto(), getCodigoArmazem()));  
        //  TbStock stock =  stockDao.getStockByDescricao(getCodigoProduto(), getCodigoArmazem() );
        double quant_possivel = conexao.getQuantidade_Existente_Publico(cod_produto, getCodigoArmazem()) - conexao.getQuantidade_minima_publico(cod_produto, getCodigoArmazem());
        //int quant_possivel = stock.getQuantidadeExistente() -  stock.getQuantBaixa();

        return quant_possivel >= qtd;

    }

    public void remover_items() {

        table.getColumnModel().getColumn(0);
        table.getColumnModel().getColumn(1);
        table.getColumnModel().getColumn(2);
        table.getColumnModel().getColumn(3);
        table.getColumnModel().getColumn(4);
        table.getColumnModel().getColumn(5);

        DefaultTableModel modelo = (DefaultTableModel) table.getModel();

        for (int i = modelo.getRowCount() - 1; i >= 0; i--) {
            modelo.removeRow(i);
        }

    }

    public static void setTotalRetencao() {
        try {
            BigDecimal totalRetencao = getTotalRetencaoLiquido();
            txtTotalPagarRetencao.setText(CfMethods.formatarComoMoeda(totalRetencao));
        } catch (Exception e) {
        }

    }

//    private static BigDecimal getTotalRetencaoLiquido()
//    {
//        DefaultTableModel modelo = (DefaultTableModel) table.getModel();
//        BigDecimal totalRetencao = BigDecimal.ZERO;
//
//        for ( int i = 0; i < modelo.getRowCount(); i++ )
//        {
//            BigDecimal valorRetencao = BigDecimal.valueOf(
//                    CfMethods.parseMoedaFormatada( modelo.getValueAt( i, 8 ).toString() )
//            );
//
//            totalRetencao = totalRetencao.add( valorRetencao );
//        }
//
//        return totalRetencao.setScale( 2, RoundingMode.HALF_UP );
//    }
    private static BigDecimal getTotalRetencaoLiquido() {

        DefaultTableModel modelo = (DefaultTableModel) table.getModel();
        BigDecimal totalRetencao = BigDecimal.ZERO;

        for (int i = 0; i < modelo.getRowCount(); i++) {

            int codProduto = Integer.parseInt(modelo.getValueAt(i, 0).toString());
            Object valorObj = modelo.getValueAt(i, 8);

            // Se estiver vazio, pula
            if (valorObj == null) {
                continue;
            }

            String valorTexto = valorObj.toString().trim();

            if (valorTexto.isEmpty() || valorTexto.equals("--")) {
                continue;
            }

            try {
                if (servicosRetencaoController.existeRetencao(codProduto)) {
                    double valor = CfMethods.parseMoedaFormatada(valorTexto);
                    totalRetencao = totalRetencao.add(BigDecimal.valueOf(valor));
                }

            } catch (Exception e) {
                System.err.println("Erro ao converter valor da coluna 8: '" + valorTexto + "'");
                continue; // continua sem travar o programa
            }
        }

        return totalRetencao.setScale(2, RoundingMode.HALF_UP);
    }

    public static double getTotalPagar() {

        DefaultTableModel modelo = (DefaultTableModel) table.getModel();

        double total_pagar = 0;
        for (int i = 0; i < modelo.getRowCount(); i++) {
            total_pagar += CfMethods.parseMoedaFormatada(String.valueOf(modelo.getValueAt(i, 10)));

        }
        return total_pagar;

    }

    public static double getTotal_Retencao() {

        DefaultTableModel modelo = (DefaultTableModel) table.getModel();

        double total_retencao = 0;
        for (int i = 0; i < modelo.getRowCount(); i++) {
            total_retencao += CfMethods.parseMoedaFormatada(String.valueOf(modelo.getValueAt(i, 8)));

        }
        return total_retencao;

    }

    private static BigDecimal getTotalAOALiquido() {
        BigDecimal totalIliquido = FinanceUtils.getTotalIliquidoTable(INDEX_TABLE_PRECO, INDEX_TABLE_QTD, table);
        BigDecimal totalImposto = BigDecimal.valueOf(FinanceUtils.getTotalImpostoTable(INDEX_TABLE_PRECO, INDEX_TABLE_QTD, INDEX_TABLE_DESCONTO, INDEX_TABLE_TAXA_IVA, table));
        BigDecimal descontoComercial = FinanceUtils.getDescontoComercial(INDEX_TABLE_PRECO, INDEX_TABLE_QTD, INDEX_TABLE_DESCONTO, table);
        BigDecimal descontoFinanceiro = BigDecimal.valueOf(getDescontoFinanceiro());

        return totalIliquido
                .add(totalImposto)
                .subtract(descontoComercial.add(descontoFinanceiro))
                .setScale(2, RoundingMode.HALF_UP);
    }

    public static void setTotalPagar() {

        DefaultTableModel modelo = (DefaultTableModel) table.getModel();

        double totalPagar = 0;
        double totalRetencao = 0;

        for (int i = 0; i < modelo.getRowCount(); i++) {

            totalPagar += CfMethods.parseMoedaFormatada(
                    String.valueOf(modelo.getValueAt(i, 10)));

            totalRetencao += CfMethods.parseMoedaFormatada(
                    String.valueOf(modelo.getValueAt(i, 8)));
        }

        // Apenas aplica retenção para Factura-Recibo
        if (getIdDocumento() == DOC_FACTURA_RECIBO_FR) {

            totalPagar -= totalRetencao;

//        txtTotalPagarRetencao.setText(
//                CfMethods.formatarComoMoeda(totalRetencao));
        } else {

            txtTotalPagarRetencao.setText(
                    CfMethods.formatarComoMoeda(totalRetencao));

        }

        txtTotalPagar.setText(
                CfMethods.formatarComoMoeda(totalPagar));
    }

//    public static void setTotalPagar() {
    ////        BigDecimal total = getTotalAOALiquido();
////        String valorFormatado = CfMethods.formatarComoMoeda( total );
////        txtTotalPagar.setText( valorFormatado );
//
//        DefaultTableModel modelo = (DefaultTableModel) table.getModel();
//        double total_liquido = 0;
//
//        for (int i = 0; i < modelo.getRowCount(); i++) {
//            total_liquido += CfMethods.parseMoedaFormatada(String.valueOf(modelo.getValueAt(i, 10)));
//        }
//        txtTotalPagar.setText(CfMethods.formatarComoMoeda(total_liquido));
//    }
    public static String formatarComoMoeda(BigDecimal valor) {
        return valor.setScale(2, RoundingMode.HALF_UP).toString().replace(".", ",") + " Kz";
    }

//    private static void valor_por_extenco() {
//        BigDecimal total = BigDecimal.valueOf(CfMethods.parseMoedaFormatada(txtTotalPagar.getText()));
    ////        BigDecimal total = CfMethods.parseMoedaFormatadaBigDecimal( txtTotalPagar.getText() );
//        lbValorPorExtenco.setText(MetodosUtil.valorPorExtensoBigDecima(total, getMoeda().getDesignacao()));
//    }
    private static void valor_por_extenco() {

        BigDecimal total = BigDecimal.valueOf(
                CfMethods.parseMoedaFormatada(txtTotalPagar.getText()));

        if (getIdDocumento() == DVML.DOC_FACTURA_PROFORMA_PP
                || getIdDocumento() == DVML.DOC_FACTURA_FT) {

            BigDecimal totalRetencao = BigDecimal.valueOf(
                    CfMethods.parseMoedaFormatada(txtTotalPagarRetencao.getText()));

            total = total.subtract(totalRetencao);
        }

        lbValorPorExtenco.setText(
                MetodosUtil.valorPorExtensoBigDecima(
                        total,
                        getMoeda().getDesignacao()));
    }

    private static void valor_por_extenco(Moeda moeda) {
        System.out.println("Valor XXXXXXX: " + CfMethods.parseMoedaFormatada(txtTotalPagar.getText()));
        lbValorPorExtenco.setText(MetodosUtil.valorPorExtenso(CfMethods.parseMoedaFormatada(txtTotalPagar.getText()), moeda.getDesignacao()));
    }

    private static double getDescontoFinanceiro() {
        double desconto_economico = 0d;
        desconto_economico = Double.parseDouble(sp_desconto_financeiro.getValue().toString());
        return desconto_economico;
    }

    private static double getTotalAOARetencoes() {
        double valores = (getTotalRetencao1());
        return (valores);
    }

    private static double getTotalRetencao1() {
        DefaultTableModel modelo = (DefaultTableModel) table.getModel();
        double qtd = 0d;
        double imposto = 0d, preco_unitario = 0d, desconto_valor_linha = 0d, valor_taxa = 0d;

        for (int i = 0; i < modelo.getRowCount(); i++) {
            preco_unitario = CfMethods.parseMoedaFormatada(modelo.getValueAt(i, 3).toString());
            qtd = Double.parseDouble(modelo.getValueAt(i, 4).toString());
            double valor_percentagem = Double.parseDouble(modelo.getValueAt(i, 5).toString());
            double taxa = Double.parseDouble(modelo.getValueAt(i, 7).toString());
            // a incidência só é aplicável ao produtos sujeitos a iva 
            if (taxa != 0) {
                double valor_unitario = (preco_unitario * qtd);

                desconto_valor_linha = valor_unitario * ((valor_percentagem) / 100);
                valor_taxa = (valor_unitario - desconto_valor_linha) / taxa;
//                imposto += ( ( valor_unitario - desconto_valor_linha ) * ( taxa / 100 ) );

            }

        }

        return valor_taxa;
    }

    private static BigDecimal getTotalVendaIVASemIncluirDesconto() {
        BigDecimal totalIva = BigDecimal.ZERO;

        DefaultTableModel modelo = (DefaultTableModel) table.getModel();

        for (int i = 0; i < modelo.getRowCount(); i++) {
            // Coluna 3: Preço Unitário formatado
            BigDecimal precoUnitarioLocal = BigDecimal.valueOf(
                    CfMethods.parseMoedaFormatada(modelo.getValueAt(i, 3).toString())
            );

            // Coluna 4: Quantidade
            BigDecimal quantidade = new BigDecimal(modelo.getValueAt(i, 4).toString());

            // Coluna 6: Taxa de IVA (%)
            BigDecimal taxaIva = new BigDecimal(modelo.getValueAt(i, 6).toString());

            // subtotal ilíquido = preco * qtd
            BigDecimal subtotal = precoUnitarioLocal.multiply(quantidade);

            // valor do IVA = subtotal * (taxa / 100)
            BigDecimal valorIva = subtotal.multiply(taxaIva).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

            // somar ao total
            totalIva = totalIva.add(valorIva);
        }

        return totalIva.setScale(2, RoundingMode.HALF_UP);
    }

    private static BigDecimal getGrossTotal() {
        return FinanceUtils.getTotalIliquidoTable(
                INDEX_TABLE_PRECO,
                INDEX_TABLE_QTD, table)
                .add(getTotalVendaIVASemIncluirDesconto())
                .setScale(2, RoundingMode.HALF_UP);
    }

    private boolean validarPrecos_tabela(JTable tabela) {
        DefaultTableModel modelo = (DefaultTableModel) tabela.getModel();
        StringBuilder produtosComErro = new StringBuilder();

        // Percorrer todas as linhas
        for (int i = 0; i < modelo.getRowCount(); i++) {
            double precoUnitario = extrairValorNumerico(modelo.getValueAt(i, 9));
            double precoTotal = extrairValorNumerico(modelo.getValueAt(i, 10));

            // Se qualquer um dos dois preços for zero, marca como erro
            if (precoUnitario <= 0 || precoTotal <= 0) {
                Object designacaoObj = modelo.getValueAt(i, 1);
                String designacao = (designacaoObj != null && !designacaoObj.toString().isEmpty())
                        ? designacaoObj.toString()
                        : "Produto sem nome";

                if (produtosComErro.length() > 0) {
                    produtosComErro.append(" e ");
                }
                produtosComErro.append(designacao);
            }
        }

        // Se encontrou produtos com erro
        if (produtosComErro.length() > 0) {
            int opcao = JOptionPane.showConfirmDialog(
                    null,
                    "Atenção\nO(s) produto(s):\n" + produtosComErro
                    + ". Possuem preços Zero.\n\nDeve-se removê-lo(s) da lista!!!",
                    "Erro de Validação de Preços",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.ERROR_MESSAGE
            );

            // Se clicar OK, remover as linhas com erro
            if (opcao == JOptionPane.OK_OPTION) {
                for (int i = modelo.getRowCount() - 1; i >= 0; i--) {
                    double precoUnitario = extrairValorNumerico(modelo.getValueAt(i, 9));
                    double precoTotal = extrairValorNumerico(modelo.getValueAt(i, 10));

                    if (precoUnitario <= 0 || precoTotal <= 0) {
                        modelo.removeRow(i);
                    }
                }
            }

            return false; // Impede abrir forma de pagamento
        }

        return true; // Tudo certo, pode continuar
    }

    private double extrairValorNumerico(Object valorObj) {
        if (valorObj == null) {
            return 0.0;
        }

        String valor = valorObj.toString().trim();

        if (valor.isEmpty()) {
            return 0.0;
        }

        // Remove espaços e símbolos de moeda (AOA, KZ, etc.)
        valor = valor.replaceAll("(?i)AOA", "") // remove AOA (maiúsculo ou minúsculo)
                .replaceAll("(?i)KZ", "")
                .replaceAll("\\s+", "");   // remove espaços

        // Substitui vírgulas por pontos
        valor = valor.replace(",", ".");

        // Remove qualquer caracter que não seja número, ponto ou sinal
        valor = valor.replaceAll("[^0-9.\\-]", "");

        // Corrige caso tenha mais de um ponto (ex: "1.600.00" -> "1600.00")
        int firstDot = valor.indexOf('.');
        if (firstDot != -1) {
            int lastDot = valor.lastIndexOf('.');
            if (lastDot != firstDot) {
                // remove todos os pontos exceto o último
                valor = valor.substring(0, lastDot).replace(".", "") + valor.substring(lastDot);
            }
        }

        // Debug opcional
        System.out.println("→ Valor processado: '" + valorObj + "' => '" + valor + "'");

        try {
            return Double.parseDouble(valor);
        } catch (NumberFormatException e) {
            System.err.println("Erro ao converter valor: '" + valorObj + "' -> " + e.getMessage());
            return 0.0;
        }
    }

    private static String iniciais_extenso() {
        Documento documento_local = (Documento) documentosController.findById(getIdDocumento());
        String abreviacao_local = documento_local.getAbreviacao();

        switch (abreviacao_local) {
            case "FT":
                return "Facturamos o valor de: ";
            case "FR":
                return "Recebemos a quantia de: ";
            default:
                return "São: ";
        }
    }

    private void setFolhaImpressora1(String folha) {
        if (folha.equalsIgnoreCase("A6")) {
            ck_simplificada.setSelected(true);
            ck_simplificada_A7.setSelected(false);
            ck_A4.setSelected(false);
            ck_Duplicada.setSelected(false);
            ck_S_A6.setSelected(false);
            ck_ComVirgula.setSelected(false);
            ck_simplificada_O.setSelected(false);
            ck_simplificada_O_S.setSelected(false);
            this.abreviacao = Abreviacao.FR_A6;
        } else if (folha.equalsIgnoreCase("A6_O")) {
            ck_simplificada_O.setSelected(true);
            ck_simplificada_A7.setSelected(false);
            ck_A4.setSelected(false);
            ck_Duplicada.setSelected(false);
            ck_S_A6.setSelected(false);
            ck_ComVirgula.setSelected(false);
            ck_simplificada.setSelected(false);
            ck_simplificada_O_S.setSelected(false);
            this.abreviacao = Abreviacao.FR_A6_O;
        } else if (folha.equalsIgnoreCase("S_A6_O")) {
            ck_simplificada_O_S.setSelected(true);
            ck_simplificada_O.setSelected(false);
            ck_simplificada_A7.setSelected(false);
            ck_A4.setSelected(false);
            ck_Duplicada.setSelected(false);
            ck_S_A6.setSelected(false);
            ck_ComVirgula.setSelected(false);
            ck_simplificada.setSelected(false);
            this.abreviacao = Abreviacao.FR_S_A6_O;
        } else if (folha.equalsIgnoreCase("A7")) {
            ck_simplificada_A7.setSelected(true);
            ck_simplificada.setSelected(false);
            ck_A4.setSelected(false);
            ck_Duplicada.setSelected(false);
            ck_S_A6.setSelected(false);
            ck_ComVirgula.setSelected(false);
            ck_simplificada_O.setSelected(false);
            ck_simplificada_O_S.setSelected(false);
            this.abreviacao = Abreviacao.FR_SA7;
        } else if (folha.equalsIgnoreCase("A5")) {
            ck_Duplicada.setSelected(true);
            ck_simplificada.setSelected(false);
            ck_A4.setSelected(false);
            ck_simplificada_A7.setSelected(false);
            ck_S_A6.setSelected(false);
            ck_ComVirgula.setSelected(false);
            ck_simplificada_O.setSelected(false);
            ck_simplificada_O_S.setSelected(false);
            this.abreviacao = Abreviacao.FT_A4_Duplicado;
        } else if (folha.equalsIgnoreCase("S_A6")) {
            ck_S_A6.setSelected(true);
            ck_simplificada.setSelected(false);
            ck_simplificada_A7.setSelected(false);
            ck_A4.setSelected(false);
            ck_Duplicada.setSelected(false);
            ck_ComVirgula.setSelected(false);
            ck_simplificada_O.setSelected(false);
            ck_simplificada_O_S.setSelected(false);
            this.abreviacao = Abreviacao.FR_S_A6;
        } else if (folha.equalsIgnoreCase("A6V")) {
            ck_ComVirgula.setSelected(true);
            ck_simplificada.setSelected(false);
            ck_simplificada_A7.setSelected(false);
            ck_A4.setSelected(false);
            ck_Duplicada.setSelected(false);
            ck_S_A6.setSelected(false);
            ck_simplificada_O.setSelected(false);
            ck_simplificada_O_S.setSelected(false);
            this.abreviacao = Abreviacao.FR_A6_Com_Virgula;
        } else {
            ck_A4.setSelected(true);
            ck_simplificada.setSelected(false);
            ck_simplificada_A7.setSelected(false);
            ck_Duplicada.setSelected(false);
            ck_S_A6.setSelected(false);
            ck_ComVirgula.setSelected(false);
            ck_simplificada_O.setSelected(false);
            ck_simplificada_O_S.setSelected(false);
            this.abreviacao = Abreviacao.FR_A4;
        }
    }

    private void setFocus(String focus) {
        if (focus.equalsIgnoreCase("Codigo Interno")) {

            txtCodigoProduto.requestFocus();

        } else if (focus.equalsIgnoreCase("Codigo de Barra")) {

            txtCodigoBarra.requestFocus();

        } else {

            txtCodigoManual.requestFocus();

        }
    }

    private void setDesactivarvias(String desactivarvias) {

        if (desactivarvias.equalsIgnoreCase("Sim")) {

            spnCopia.setVisible(true);
//            lbVias.setVisible( true );

        } else {

            spnCopia.setVisible(false);
//            lbVias.setVisible( false );

        }

    }

    private void setDesactivarLugar(String desactivarLugar) {

        if (desactivarLugar.equalsIgnoreCase("Sim")) {

            spnCopia.setVisible(true);
//            lbVias.setVisible( true );

        } else {

            spnCopia.setVisible(false);
//            lbVias.setVisible( false );

        }

    }

    private void setTranstorno(String transtorno) {
        if (transtorno.equalsIgnoreCase("Activo")) {
            rbTranstorno.setSelected(true);
//            jlStockNegativo.setVisible( true );

        } else {
            rbTranstorno.setSelected(false);
            rbTranstorno.setVisible(false);
//            jlStockNegativo.setVisible( false );

        }
    }

    private void setActivarDescontoFinanceiro(String desconto_financeiro) {
        if (desconto_financeiro.equalsIgnoreCase("Activar")) {
            sp_desconto_financeiro.setVisible(true);
            lbDescontoFinanceiro.setVisible(true);

        } else {
            sp_desconto_financeiro.setVisible(false);
            lbDescontoFinanceiro.setVisible(false);

        }
    }

    private void setVizualisarStock(String stock) {
        if (stock.equalsIgnoreCase("Vizualisar Stock")) {
            txtQuantidadeStock.setVisible(true);
            lbQuantidadeStock.setVisible(true);

        } else {
            txtQuantidadeStock.setVisible(false);
            lbQuantidadeStock.setVisible(false);

        }
    }

    private void setAnoEconomico(String ano_economico) {
        if (ano_economico.equalsIgnoreCase("Ocultar")) {
            cmbAnoEconomico.setVisible(true);

        } else {
            cmbAnoEconomico.setVisible(false);

        }
    }

    private void setDocpadrao(String documentos) {
        System.out.println("DOCUMENTO PADRAO: " + documentos);
        if (documentos.equalsIgnoreCase("Factura/Recibo")) {
            cmbTipoDocumento.setSelectedIndex(1);

        } else if (documentos.equalsIgnoreCase("Factura")) {
            cmbTipoDocumento.setSelectedIndex(2);

        } else if (documentos.equalsIgnoreCase("Factura-Proforma")) {
            cmbTipoDocumento.setSelectedIndex(3);

        } else if (documentos.equalsIgnoreCase("Guia de Transporte")) {
            cmbTipoDocumento.setSelectedIndex(4);

        }
    }

    private void setArmazem(String armazem) {
        if (armazem.equalsIgnoreCase("Multi_armazem")) {
            rbArmazem.setSelected(true);
            rbArmazem1.setSelected(false);

        } else {
            rbArmazem.setSelected(false);
            rbArmazem1.setSelected(true);
        }
    }

    private void mostrar_nome() {
        TbUsuario usuario = (TbUsuario) usuariosController.getUsuarioByCodigo(this.cod_usuario);
        lb_nome_usuario.setText("Operador: " + usuario.getNome());
        System.out.println("&&&&&&&&Conn::::" + cod_usuario);
    }

    private void empresa() {
        TbDadosInstituicao dados = (TbDadosInstituicao) dadosInstituicaoController.findById(1);

        jlEmpresa.setText("KITANDA 1.2     " + dados.getNome());

    }

//    private void actualizar_abreviacao() {
//
//        switch (getIdDocumento()) {
//            case DVML.DOC_FACTURA_RECIBO_FR:
//                if (ck_A4.isSelected()) {
//                    this.abreviacao = Abreviacao.FR_A4;
//                } else if (ck_simplificada.isSelected()) {
//                    this.abreviacao = Abreviacao.FR_A6;
//                } else if (ck_simplificada_O_S.isSelected()) {
//                    this.abreviacao = Abreviacao.FR_S_A6_O;
//                } else if (ck_simplificada_O.isSelected()) {
//                    this.abreviacao = Abreviacao.FR_A6_O;
//                } else if (ck_simplificada_A7.isSelected()) {
//                    this.abreviacao = Abreviacao.FR_SA7;
//                } else if (ck_S_A6.isSelected()) {
//                    this.abreviacao = Abreviacao.FR_S_A6;
//                } else if (ck_ComVirgula.isSelected()) {
//                    this.abreviacao = Abreviacao.FR_A6_Com_Virgula;
//                } else {
//                    this.abreviacao = Abreviacao.FR_A4_Duplicado;
//                }
//
//                break;
//
//            case DVML.DOC_FACTURA_FT:
//
//                if (ck_A4.isSelected()) {
//                    this.abreviacao = Abreviacao.FA;
    ////                    ck_A4.setSelected( true );
//                } else if (ck_simplificada_A7.isSelected() || ck_simplificada.isSelected() || ck_S_A6.isSelected()) {
////                    JOptionPane.showMessageDialog( null, "Atenção, selecione outro formato pra venda a crédito!" );
//                    ck_A4.setSelected(true);
//                } else {
//                    this.abreviacao = Abreviacao.FT_A4_Duplicado;
//                }
//
//                break;
//
//            case DVML.DOC_FACTURA_PROFORMA_PP:
//                this.abreviacao = Abreviacao.PP;
//                break;
//
//            case DVML.DOC_FACTURA_CONSULTA_MESA:
//                this.abreviacao = Abreviacao.CM;
//                break;
//
//            case DVML.DOC_GUIA_TRANSPORTE_GT:
//                this.abreviacao = Abreviacao.GT;
//                break;
//
//            default:
//                break;
//        }
//
//    }
    
    private void actualizar_abreviacao() {

        switch (getIdDocumento()) {
            case DVML.DOC_FACTURA_RECIBO_FR:
                if (ck_A4.isSelected()) {
                    this.abreviacao = Abreviacao.FR_A4;
                } else if (ck_simplificada.isSelected()) {
                    this.abreviacao = Abreviacao.FR_A6;
                } else if (ck_simplificada_O_S.isSelected()) {
                    this.abreviacao = Abreviacao.FR_S_A6_O;
                } else if (ck_simplificada_O.isSelected()) {
                    this.abreviacao = Abreviacao.FR_A6_O;
                } else if (ck_simplificada_A7.isSelected()) {
                    this.abreviacao = Abreviacao.FR_SA7;
                } else if (ck_S_A6.isSelected()) {
                    this.abreviacao = Abreviacao.FR_S_A6;
                } else if (ck_ComVirgula.isSelected()) {
                    this.abreviacao = Abreviacao.FR_A6_Com_Virgula;
                } else {
                    this.abreviacao = Abreviacao.FR_A4_Duplicado;
                }
                break;

            case DVML.DOC_FACTURA_FT:
                if (ck_A4.isSelected()) {
                    this.abreviacao = Abreviacao.FA;
                } else if (ck_simplificada_A7.isSelected() || ck_simplificada.isSelected() || ck_S_A6.isSelected()
                        || ck_simplificada_O.isSelected() || ck_simplificada_O_S.isSelected() || ck_ComVirgula.isSelected()) {
                    // Formato não permitido para venda a crédito: força A4
                    ck_A4.setSelected(true);
                    ck_simplificada_A7.setSelected(false);
                    ck_simplificada.setSelected(false);
                    ck_S_A6.setSelected(false);
                    ck_simplificada_O.setSelected(false);
                    ck_simplificada_O_S.setSelected(false);
                    ck_ComVirgula.setSelected(false);
                    this.abreviacao = Abreviacao.FA;
                    folhaEscolhida = "A4";
                } else {
                    // Só chega aqui com A5 (ck_Duplicada) marcado
                    this.abreviacao = Abreviacao.FT_A4_Duplicado;
                }
                break;

            case DVML.DOC_FACTURA_PROFORMA_PP:
                this.abreviacao = Abreviacao.PP;
                break;

            case DVML.DOC_FACTURA_CONSULTA_MESA:
                this.abreviacao = Abreviacao.CM;
                break;

            case DVML.DOC_GUIA_TRANSPORTE_GT:
                this.abreviacao = Abreviacao.GT;
                break;

            default:
                break;
        }
    }

    private void mostra_consumidor_final() {

        if (cmbCliente.getSelectedItem().equals("Consumidor Final")) {
//            lbClienteConsumidorFinal.setVisible( true );
            txtNomeConsumidorFinal.setVisible(true);
        } else {
//            lbClienteConsumidorFinal.setVisible( false );
            txtNomeConsumidorFinal.setVisible(false);
        }

    }

    private void desabilitar_campos() {

        boolean proformaNaoSelecionado = !(DVML.DOC_FACTURA_PROFORMA_PP == getIdDocumento());

        boolean documentoIsFA = DVML.DOC_FACTURA_FT == getIdDocumento();
        boolean documentoIsPP = DVML.DOC_FACTURA_PROFORMA_PP == getIdDocumento();
        boolean documentoIsGT = DVML.DOC_GUIA_TRANSPORTE_GT == getIdDocumento();
        boolean documentoIsCM = DVML.DOC_FACTURA_CONSULTA_MESA == getIdDocumento();
        System.err.println("documentoIsFA: " + documentoIsFA);
        System.err.println("documentoIsPP: " + documentoIsPP);
//        ck_A4.setSelected(!documentoIsFA && !documentoIsPP && !documentoIsGT);
        btnProcessar.setVisible(documentoIsPP || documentoIsFA || documentoIsGT || documentoIsCM);
        btnFormaPagamento.setVisible(!documentoIsFA && !documentoIsPP && !documentoIsGT && !documentoIsCM);
//        btnSemFormaPagamento.setVisible( !documentoIsFA && !documentoIsPP && !documentoIsGT && !documentoIsCM );
    }

    private void atualizarCliente1() {
        boolean documentoIsFA = DVML.DOC_FACTURA_FT == getIdDocumento();
        boolean documentoIsPP = DVML.DOC_FACTURA_PROFORMA_PP == getIdDocumento();
        boolean documentoIsGT = DVML.DOC_GUIA_TRANSPORTE_GT == getIdDocumento();

        if (documentoIsFA || documentoIsPP || documentoIsGT) {
            //EXCLUIR CONSUMIDOR FINAL
            cmbCliente.setModel(new DefaultComboBoxModel(clientesController.getVectorExecptoConsumidorFinal()));
        } else {
            cmbCliente.setModel(new DefaultComboBoxModel(clientesController.getVector()));
            cmbCliente.setSelectedItem(DVML._CLIENTE_CONSUMIDOR_FINAL);
        }
    }

    private void accao_codigo_manual_enter() {
        try {

            String codigo_manual = txtCodigoManual.getText();
            TbProduto produtoLocal = produtosController.findByCodManual(codigo_manual);
            double qtd = Double.parseDouble(txtQuatindade.getText());
            procedimentoAdicionarTabela(produtoLocal, qtd);
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(VendaModernaVisao.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Não existe produto com este código manual.", DVML.DVML_COMERCIAL, JOptionPane.ERROR_MESSAGE);
        }

    }

    public void adicionar_preco_quantidade() {

        try {

            TbProduto produto_local = (TbProduto) produtosController.findById(getCodigoProduto());

            TbStock stockLocal = stocksController.getStockByIdProdutoAndIdArmazem(getCodigoProduto(), getCodigoArmazem());
            boolean isStocavel = produto_local.getStocavel().equals("true");

            if (isStocavel && stockLocal.getQuantidadeExistente() <= stockLocal.getQuantCritica()) {

                txtQuantidadeStock.setBackground(Color.RED);
                txtQuantidadeStock.setForeground(Color.BLACK);
            } else {
                txtQuantidadeStock.setBackground(new Color(51, 153, 0, 255));
            }

            txtCodigoBarra.setText(String.valueOf(produto_local.getCodBarra()));
            //actualizar
            txtLocal.setText(String.valueOf(produto_local.getCodLocal().getDesignacao()));
            txtCodigoProduto.setText(String.valueOf(produto_local.getCodigo()));

            if (isStocavel && !Objects.isNull(stockLocal)) {
                txtQuantidadeStock.setText(String.valueOf(stockLocal.getQuantidadeExistente()));
            } else {
                txtQuantidadeStock.setText("0");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            txtQuantidadeStock.setText("0");
            Logger
                    .getLogger(VendaModernaVisao.class
                            .getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void atualizarDataVencimentoFA() {
        boolean documentoIsFA = DVML.DOC_FACTURA_FT == getIdDocumento();
        boolean documentoIsPP = DVML.DOC_FACTURA_PROFORMA_PP == getIdDocumento();
//        boolean documentoIsGT = DVML.DOC_GUIA_TRANSPORTE_GT == getIdDocumento();

        if (documentoIsFA || documentoIsPP) {
            dc_data_vencimento.setEnabled(true);
        } else {
            dc_data_vencimento.setEnabled(false);
        }
    }

    public void remover_item_carrinho() {

        DefaultTableModel modelo
                = (DefaultTableModel) table.getModel();

        int linhaSelecionada = table.getSelectedRow();

        if (linhaSelecionada < 0) {
            return;
        }

        String designacao = String.valueOf(
                modelo.getValueAt(linhaSelecionada, 1)
        );

        // Não permitir remover serviços
        if (designacao.contentEquals("#")) {
            JOptionPane.showMessageDialog(
                    null,
                    "Não é permitido remover um serviço."
            );
            return;
        }

        // Remover a linha selecionada
        modelo.removeRow(linhaSelecionada);

        // Atualizar os totais
        setTotalPagar();
        setTotalRetencao();
        calculaTotalIVA();

        TbProduto findByDesignacao
                = produtosController.findByDesignacao(
                        cmbProduto.getSelectedItem().toString()
                );

        int idPedido = 0;

        TbMesas mesaEntity = (TbMesas) mesasController.findById(DVML.MESA_BALCAO);

        String mesa = mesaEntity.getDesignacao();

        TbLugares lugarEntity = (TbLugares) lugaresController.findById(DVML.LUGAR_BALCAO);

        String lugar = lugarEntity.getDesignacao();

        TbUsuario usuarioEntity = (TbUsuario) usuariosController.findById(cod_usuario);

        String usuario = usuarioEntity.getNome();

        if (findByDesignacao != null) {

            if (findByDesignacao.getCozinha().equals("Enviar Ticket")) {

                MetodosUtil.imprimir_cozinha(
                        findByDesignacao,
                        idPedido,
                        mesa,
                        lugar,
                        usuario,
                        "Cancelado",
                        (int) getQuantidade(),
                        dadosInstituicaoController
                );

            } else if (findByDesignacao.getCozinha().equals("Enviar Sala")) {

                MetodosUtil.imprimir_sala(
                        findByDesignacao,
                        idPedido,
                        mesa,
                        lugar,
                        usuario,
                        "Cancelado",
                        (int) getQuantidade(),
                        dadosInstituicaoController
                );
            }
        }

        valor_por_extenco();
        reset_desconto_global();
    }

//    public void remover_item_carrinho() {
//
//        DefaultTableModel modelo = (DefaultTableModel) table.getModel();
//
//        String designacao = modelo.getValueAt(table.getSelectedRow(), 1).toString();
//        if (designacao.contentEquals("#")) {
//            JOptionPane.showMessageDialog(null, "Não é permitido remover um  serviço ");
//        }
//
//        modelo.removeRow(table.getSelectedRow());
//        setTotalPagar();
//        setTotalRetencao();
//        calculaTotalIVA();
//        //txtDesconto.setText("0");
//
//        TbProduto findByDesignacao = produtosController.findByDesignacao(cmbProduto.getSelectedItem().toString());
//
//        int idPedido = 0;
//        TbMesas mesaEntity = (TbMesas) mesasController.findById(DVML.MESA_BALCAO);
//        String mesa = mesaEntity.getDesignacao();
//        TbLugares lugarEntity = (TbLugares) lugaresController.findById(DVML.LUGAR_BALCAO);
//        String lugar = lugarEntity.getDesignacao();
//        TbUsuario usuarioEntity = (TbUsuario) usuariosController.findById(cod_usuario);
//        String usuario = usuarioEntity.getNome();
//
//        if (findByDesignacao.getCozinha().equals("Enviar Ticket")) {
//            MetodosUtil.imprimir_cozinha(findByDesignacao, idPedido, mesa, lugar, usuario, "Cancelado", (int) getQuantidade(), dadosInstituicaoController);
//        } else if (findByDesignacao.getCozinha().equals("Enviar Sala")) {
//            MetodosUtil.imprimir_sala(findByDesignacao, idPedido, mesa, lugar, usuario, "Cancelado", (int) getQuantidade(), dadosInstituicaoController);
//        }
//
//        valor_por_extenco();
//        reset_desconto_global();
//        //calcularTroco();
//
//    }
    public static void accao_codigo_interno_enter_busca_exterior(int codigo, double qtd) {

        try {

            System.out.println("ID PRODUTO EXTERIOR: " + codigo);
            TbProduto produtoLocal = (TbProduto) produtosController.findById(codigo);

            Integer codTipoProduto = produtoLocal.getCodTipoProduto().getCodigo();
            TbTipoProduto tipoProduto = (TbTipoProduto) tipoProdutoController.findById(codTipoProduto);
            Integer codFamilia = tipoProduto.getFkFamilia().getPkFamilia();
            Familia familia = (Familia) familiaController.findById(codFamilia);
            cmbSubFamilia.setSelectedItem(tipoProduto.getDesignacao());

            cmbProduto.setModel(new DefaultComboBoxModel(produtosController.getVector()));
            cmbProduto.setSelectedItem(produtoLocal.getDesignacao());

            configuracaoMesComecoController = new ConfiguracaoMesComecoController(conexao.getConnectionAtiva());

            boolean existeConfiguracaoDoCliente = configuracaoMesComecoController.existeConfiguracaoDoCliente(
                    getIdCliente(), produtoLocal.getCodigo());

            if (existeConfiguracaoDoCliente) {

                new MesesPagoClienteVisao(null,
                        true,
                        getIdCliente(),
                        getCodigoProduto(), conexao).setVisible(true);
            } else {

                procedimentoAdicionarTabela(produtoLocal, qtd);
            }

            cursorLinha();
            scrolltable();
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger
                    .getLogger(VendaModernaVisao.class
                            .getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Este produto não existe no armazém " + cmbArmazem.getSelectedItem(), DVML.DVML_COMERCIAL, JOptionPane.ERROR_MESSAGE);
        }

    }

    /**
     * Normaliza uma string: lower-case, remove acentos, pontuação extra e
     * colapsa espaços.
     */
    private String normalizar(String s) {
        if (s == null) {
            return "";
        }

        // remove acentos
        String n = Normalizer.normalize(s, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
        // remove pontuação (mas preserva #), transforma espaços múltiplos em 1 e trim
        n = n.replaceAll("[\\p{Punct}&&[^#]]+", " ")
                .replaceAll("\\s+", " ")
                .trim()
                .toLowerCase();
        return n;
    }

    /**
     * Extrai (produto, mes) de uma designacao que contenha '#'. Retorna null se
     * não for possível extrair.
     */
    private String[] extrairProdutoEMes(String designacao) {
        if (designacao == null) {
            return null;
        }
        Pattern p = Pattern.compile("^(.*?)#(.*)$"); // grupo 1: tudo antes do #, grupo2: tudo depois
        Matcher m = p.matcher(designacao);
        if (!m.find()) {
            return null;
        }
        String produto = m.group(1).trim();
        String mes = m.group(2).trim();
        return new String[]{
            produto, mes
        };
    }

    private boolean podeRemoverServico(DefaultTableModel model, int selectedRow) {
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Selecione um serviço para remover!");
            return false;
        }

        String designacaoSelecionada = String.valueOf(model.getValueAt(selectedRow, 1));
        if (designacaoSelecionada == null || !designacaoSelecionada.contains("#")) {
            return true; // não é mensalidade
        }

        // mapa de meses em ordem e normalizado
        String[] meses
                = {
                    "Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho",
                    "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"
                };
        // criar versão normalizada do array meses para comparação
        String[] mesesNorm = new String[meses.length];
        for (int i = 0; i < meses.length; i++) {
            mesesNorm[i] = normalizar(meses[i]);
        }

        try {
            String[] pm = extrairProdutoEMes(designacaoSelecionada);
            if (pm == null) {
                return true;
            }
            String produtoOriginal = pm[0];
            String mesAtual = pm[1];

            String produtoNorm = normalizar(produtoOriginal);

            // índice do mês selecionado
            String mesAtualNorm = normalizar(mesAtual);
            int indexMesAtual = -1;
            for (int i = 0; i < mesesNorm.length; i++) {
                if (mesesNorm[i].equals(mesAtualNorm)) {
                    indexMesAtual = i;
                    break;
                }
            }
            if (indexMesAtual == -1) {
                JOptionPane.showMessageDialog(null, "Mês inválido: " + mesAtual);
                return false;
            }

            // percorre todas as linhas e identifica as que pertencem EXATAMENTE ao mesmo produto
            int totalMesesMesmoProduto = 0;
            int maxIndexPresente = -1;
            String mesMaisRecentePresente = null;

            // também vamos registrar as linhas que tem o mês mais recente para garantir remover última ocorrência
            java.util.List<Integer> linhasDoMesMaisRecente = new java.util.ArrayList<>();

            for (int r = 0; r < model.getRowCount(); r++) {
                String d = String.valueOf(model.getValueAt(r, 1));
                if (d == null || !d.contains("#")) {
                    continue;
                }

                String[] pmLinha = extrairProdutoEMes(d);
                if (pmLinha == null) {
                    continue;
                }

                String produtoLinhaNorm = normalizar(pmLinha[0]);

                // compara produto por igualdade da string normalizada
                if (!produtoLinhaNorm.equals(produtoNorm)) {
                    continue;
                }

                // extrai mes e normaliza
                String mesLinhaNorm = normalizar(pmLinha[1]);
                int idxLinha = -1;
                for (int m = 0; m < mesesNorm.length; m++) {
                    if (mesesNorm[m].equals(mesLinhaNorm)) {
                        idxLinha = m;
                        break;
                    }
                }
                if (idxLinha == -1) {
                    continue;
                }

                totalMesesMesmoProduto++;

                if (idxLinha > maxIndexPresente) {
                    maxIndexPresente = idxLinha;
                    mesMaisRecentePresente = meses[idxLinha];
                    linhasDoMesMaisRecente.clear();
                    linhasDoMesMaisRecente.add(r);
                } else if (idxLinha == maxIndexPresente) {
                    // mesma posição do mês mais recente — acrescenta a linha (duplicatas do mesmo mês)
                    linhasDoMesMaisRecente.add(r);
                }
            }

            // se existe apenas 1 mês daquele produto -> pode remover
            if (totalMesesMesmoProduto <= 1) {
                return true;
            }

            // se o mês do selectedRow não é o mês mais recente -> bloqueia
            if (indexMesAtual != maxIndexPresente) {
                JOptionPane.showMessageDialog(null,
                        "Não pode remover " + meses[indexMesAtual]
                        + " antes de remover " + mesMaisRecentePresente + " (remova do último para o primeiro).");
                return false;
            }

            // se o mês selecionado é o mês mais recente, mas há várias linhas com o mesmo mês,
            // só permita remover se a linha selecionada for a *última ocorrência* daquele mês (maior índice de linha).
            if (!linhasDoMesMaisRecente.isEmpty()) {
                int maiorLinha = -1;
                for (Integer ln : linhasDoMesMaisRecente) {
                    if (ln > maiorLinha) {
                        maiorLinha = ln;
                    }
                }
                if (selectedRow != maiorLinha) {
                    JOptionPane.showMessageDialog(null,
                            "Existem várias entradas para " + mesMaisRecentePresente
                            + ". Remova a linha mais abaixo (última ocorrência) primeiro.");
                    return false;
                }
            }

            // passou todas as regras -> pode remover
            return true;

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erro ao verificar serviço: " + ex.getMessage());
            return false;
        }
    }

    private static boolean primeiraLinhaVazia() {
        DefaultTableModel modelo = (DefaultTableModel) table.getModel();

        if (modelo.getRowCount() == 0) {
            return true;
        }

        Object codigo = modelo.getValueAt(0, 0);

        return codigo == null || codigo.toString().trim().isEmpty();
    }

    private static void inserirLinhaEmBranco() {
        DefaultTableModel modelo = (DefaultTableModel) table.getModel();
        if (modelo.getRowCount() == 0) {
            modelo.addRow(new Object[]{
                null, null, null, null, null, null, null, null, null, null, null
            });
        }

    }

//    private void accao_codigo_barra_enter_cb() {
//    try {
//        String codigoBarra = txtCodigoBarra.getText().trim();
//
//        if (codigoBarra.isEmpty()) {
//            JOptionPane.showMessageDialog(null, 
//                "Digite o código de barra!", 
//                DVML.DVML_COMERCIAL, 
//                JOptionPane.WARNING_MESSAGE
//            );
//            return;
//        }
//
//        // Busca o produto pelo código de barra
//        TbProduto produto = produtosController.findByCodBarra(codigoBarra);
//
//        if (produto == null) {
//            JOptionPane.showMessageDialog(
//                null,
//                "Nenhum produto encontrado com o código de barra informado.",
//                DVML.DVML_COMERCIAL,
//                JOptionPane.ERROR_MESSAGE
//            );
//            txtCodigoBarra.requestFocus();
//            txtCodigoBarra.selectAll();
//            return;
//        }
//
//        // Obtém a família e subfamília como no método original
//        Integer codTipoProduto = produto.getCodTipoProduto().getCodigo();
//        TbTipoProduto tipoProduto = (TbTipoProduto) tipoProdutoController.findById(codTipoProduto);
//        Integer codFamilia = tipoProduto.getFkFamilia().getPkFamilia();
//        Familia familia = (Familia) familiaController.findById(codFamilia);
//
//        // Preenche os combos (igual ao método original)
//        cmbSubFamilia.setSelectedItem(tipoProduto.getDesignacao());
//        cmbProduto.setModel(new DefaultComboBoxModel(produtosController.getVector()));
//        cmbProduto.setSelectedItem(produto.getDesignacao());
//
//        // Verificação da configuração do cliente (igual ao original)
//        configuracaoMesComecoController = new ConfiguracaoMesComecoController(conexao.getConnectionAtiva());
//        boolean existeConfiguracaoDoCliente =
//                configuracaoMesComecoController.existeConfiguracaoDoCliente(
//                        getIdCliente(),
//                        produto.getCodigo()
//                );
//
//        if (existeConfiguracaoDoCliente) {
//            new MesesPagoClienteVisao(
//                    this,
//                    rootPaneCheckingEnabled,
//                    getIdCliente(),
//                    produto.getCodigo(),
//                    conexao
//            ).setVisible(true);
//        } else {
//            procedimentoAdicionarTabela(produto);
//        }
//
//    } catch (Exception ex) {
//        Logger.getLogger(VendaModernaVisao.class.getName()).log(Level.SEVERE, null, ex);
//        JOptionPane.showMessageDialog(null,
//            "Este produto não existe no armazém " + cmbArmazem.getSelectedItem(),
//            DVML.DVML_COMERCIAL,
//            JOptionPane.ERROR_MESSAGE
//        );
//
//    } finally {
//        // Nada por agora
//    }
//}
    private void accao_codigo_interno_enter() {

        try {

            double qtd = Double.parseDouble(txtQuatindade.getText());
            int codigo = Integer.parseInt(txtCodigoProduto.getText());
            TbProduto produto = (TbProduto) produtosController.findByIdStatus(codigo);

            Integer codTipoProduto = produto.getCodTipoProduto().getCodigo();
            TbTipoProduto tipoProduto = (TbTipoProduto) tipoProdutoController.findById(codTipoProduto);
            Integer codFamilia = tipoProduto.getFkFamilia().getPkFamilia();
            Familia familia = (Familia) familiaController.findById(codFamilia);
            cmbSubFamilia.setSelectedItem(tipoProduto.getDesignacao());

            cmbProduto.setModel(new DefaultComboBoxModel(produtosController.getVector()));
            cmbProduto.setSelectedItem(produto.getDesignacao());

            configuracaoMesComecoController = new ConfiguracaoMesComecoController(conexao.getConnectionAtiva());

            System.out.println("***** Chegue 0");
            boolean existeConfiguracaoDoCliente = configuracaoMesComecoController
                    .existeConfiguracaoDoCliente(
                            getIdCliente(),
                            getCodigoProduto());

            if (existeConfiguracaoDoCliente) {
                new MesesPagoClienteVisao(this,
                        rootPaneCheckingEnabled,
                        getIdCliente(),
                        getCodigoProduto(), conexao).setVisible(true);
            } else {
                procedimentoAdicionarTabela(produto, qtd);

            }

        } catch (Exception ex) {
            Logger.getLogger(VendaModernaVisao.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Este produto não existe no armazém " + cmbArmazem.getSelectedItem(), DVML.DVML_COMERCIAL, JOptionPane.ERROR_MESSAGE);
        } finally {
        }

    }

    private void accao_codigo_barra_enter_cb() {

        try {
            String codigoBarra = txtCodigoBarra.getText().trim();
            // Busca o produto pelo código de barra
            TbProduto produtoLocal = produtosController.findByCodBarra(codigoBarra);
//            System.out.println( "ID PRODUTO: " + produtoLocal.getCodigo() );

            /**
             * Realiza a busca nas referencias caso na primeira instancia for
             * nula
             */
            if (Objects.isNull(produtoLocal)) {
                int idProdutoByCodigoBarra = referenciasController.getIdProdutoByCodigoBarra(codigoBarra);
                produtoLocal = produtosController.findByCodInterno(idProdutoByCodigoBarra);
            }

            /**
             * Verrifica se nao existe na tabela.
             */
            if (!existProduto(produtoLocal.getCodigo())) {
                if (codigoBarra.isEmpty()) {
                    JOptionPane.showMessageDialog(null,
                            "Digite o código de barra!",
                            DVML.DVML_COMERCIAL,
                            JOptionPane.WARNING_MESSAGE
                    );
                    return;
                }

                if (produtoLocal == null) {
                    JOptionPane.showMessageDialog(
                            null,
                            "Nenhum produto encontrado com o código de barra informado.",
                            DVML.DVML_COMERCIAL,
                            JOptionPane.ERROR_MESSAGE
                    );
                    txtCodigoBarra.requestFocus();
                    txtCodigoBarra.selectAll();
                    return;
                }

                // Preenche combos (igual ao original)
                Integer codTipoProduto = produtoLocal.getCodTipoProduto().getCodigo();
                TbTipoProduto tipoProduto = (TbTipoProduto) tipoProdutoController.findById(codTipoProduto);
                cmbSubFamilia.setSelectedItem(tipoProduto.getDesignacao());
                cmbProduto.setModel(new DefaultComboBoxModel(produtosController.getVector()));
                cmbProduto.setSelectedItem(produtoLocal.getDesignacao());

                // Verificação da configuração do cliente
                configuracaoMesComecoController = new ConfiguracaoMesComecoController(conexao.getConnectionAtiva());
                boolean existeConfiguracaoDoCliente
                        = configuracaoMesComecoController.existeConfiguracaoDoCliente(getIdCliente(), produtoLocal.getCodigo());

                if (existeConfiguracaoDoCliente) {
                    new MesesPagoClienteVisao(this, rootPaneCheckingEnabled, getIdCliente(),
                            produtoLocal.getCodigo(), conexao).setVisible(true);
                } else {
                    // --- ADICIONA PRODUTO NA TABELA ---
                    adicionarProdutoNaTabelaComLinhaVazia(produtoLocal);
                }

                // Limpa campos
                txtCodigoProduto.setText("");
                txtCodigoBarra.setText("");
                txtQuatindade.setText("1");
                txtCodigoBarra.requestFocus();

            } else {
                DefaultTableModel modelo = (DefaultTableModel) table.getModel();
                int cod_interno = Integer.parseInt(modelo.getValueAt(linha_existente_produto, 0).toString());
                double qtd = Double.parseDouble(modelo.getValueAt(linha_existente_produto, 4).toString());
                qtd = qtd + 1;

                double preco = precosController.getLastIdPrecoByIdProduto(cod_interno, qtd).getPrecoVenda().doubleValue();
                double taxa = Double.parseDouble(modelo.getValueAt(linha_existente_produto, 6).toString());
                double desconto = Double.parseDouble(modelo.getValueAt(linha_existente_produto, 5).toString());

                if (possivel_quantidade(cod_interno, qtd)) {

                    BigDecimal valorIliquido = FinanceUtils.getValorIliquido(new BigDecimal(qtd), new BigDecimal(preco), new BigDecimal(desconto));
                    modelo.setValueAt(qtd, linha_existente_produto, 4);
                    modelo.setValueAt(CfMethods.formatarComoMoeda(preco), linha_existente_produto, 3);

                    double valorLiquidoDouble = FinanceUtils.getValorComIVA(
                            qtd,
                            taxa,
                            preco,
                            desconto
                    );
                    BigDecimal totalComIva = BigDecimal.valueOf(valorLiquidoDouble);

                    modelo.setValueAt(CfMethods.formatarComoMoeda(valorIliquido), linha_existente_produto, 9);
                    modelo.setValueAt(CfMethods.formatarComoMoeda(totalComIva), linha_existente_produto, 10);

                } else {
                    JOptionPane.showMessageDialog(null, "O Produto não pode ser vendido para esta quantidade\nDeve-se emitir compra ou tranferência pra preencher o stock!");
                }

                actualizaTotaisVerificacaoExistencia();

            }
            txtCodigoBarra.setText("");
            txtCodigoBarra.requestFocus();

        } catch (Exception ex) {
            Logger.getLogger(VendaModernaVisao.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null,
                    "Este produto não existe no armazém " + cmbArmazem.getSelectedItem(),
                    DVML.DVML_COMERCIAL,
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private static void procedimentoAdicionarTabela(TbProduto produto, double qtd) {
        try {
            if (!Objects.isNull(produto)) {
                adicionar_preco_quantidade_anitgo();
                if (rbTranstorno.isSelected()) {
                    procedimento_adicionar_sem_transtorno("", qtd);
                } else {
                    procedimento_adicionar("", qtd);
                }
                txtCodigoProduto.setText("");
                txtCodigoBarra.setText("");
                txtQuatindade.setText("1");
                txtQuatindade.requestFocus();

            } else {
                JOptionPane.showMessageDialog(null, "Nao existe produto/servico relacionado com esta referencia");
            }

        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    public static boolean existProduto(int codigo) {

        try {
            DefaultTableModel modelo = (DefaultTableModel) table.getModel();
            if (modelo.getRowCount() > 0) {
                System.out.println("Tamanho: " + modelo.getRowCount());
                for (int i = 0; i < modelo.getRowCount(); i++) {
                    if (modelo.getValueAt(i, 0).toString().equals(String.valueOf(codigo))) {
                        linha_existente_produto = i;
                        return true;
                    }
                }
            }

            linha_existente_produto = -1;
            return false;
        } catch (Exception e) {
            linha_existente_produto = -1;
            return false;
        }

    }

    private void adicionarProdutoNaTabelaComLinhaVazia(TbProduto produto) {
        try {

            if (!rbTranstorno.isSelected()) {

                if (possivel_quantidade()) {

                    DefaultTableModel model = (DefaultTableModel) table.getModel();

                    // --- Corrigir: confirmar edição atual ---
                    if (table.isEditing()) {
                        table.getCellEditor().stopCellEditing();
                    }

                    // ---------- 1. Localizar linha vazia ----------
                    int row = -1;
                    for (int i = 0; i < model.getRowCount(); i++) {
                        if (linhaEstaVazia(model, i)) {
                            row = i;
                            break;
                        }
                    }

                    if (row == -1) {
                        model.addRow(new Object[]{
                            "", "", "", "", "", "", "", "", "", "", ""
                        });
                        row = model.getRowCount() - 1;
                    }

                    // ------------------------------------------
                    // 2. Cálculos igual ao teu sistema original
                    // ------------------------------------------
                    int codigoProduto = produto.getCodigo();
                    String descricao = produto.getDesignacao();
                    String unidade = getUnidade_Produto();

                    BigDecimal qtd = BigDecimal.valueOf(Double.parseDouble(txtQuatindade.getText()));
                    BigDecimal preco = BigDecimal.valueOf(getPreco());
                    BigDecimal descontoPercent = BigDecimal.valueOf(produto.getPercentagemDesconto());
                    BigDecimal taxaIva = BigDecimal.valueOf(getTaxaImpostoIva(codigoProduto));
                    BigDecimal taxaRet = BigDecimal.valueOf(getTaxaImpostoRet(codigoProduto));

                    BigDecimal valorIliquido = FinanceUtils.getValorIliquido(qtd, preco, descontoPercent);

                    double valorLiquidoDouble = FinanceUtils.getValorComIVA(
                            qtd.doubleValue(),
                            taxaIva.doubleValue(),
                            preco.doubleValue(),
                            descontoPercent.doubleValue()
                    );
                    BigDecimal totalComIva = BigDecimal.valueOf(valorLiquidoDouble);

                    String totalComRetencao = CfMethods.formatarComoMoeda(
                            MetodosUtil.getValorComRetencao(
                                    qtd.doubleValue(),
                                    taxaRet.doubleValue(),
                                    preco.doubleValue(),
                                    descontoPercent.doubleValue()
                            )
                    );

                    // ---------- 3. Preencher a linha ----------
                    model.setValueAt(codigoProduto, row, 0);
                    model.setValueAt(descricao, row, 1);
                    model.setValueAt(unidade, row, 2);
                    model.setValueAt(CfMethods.formatarComoMoeda(preco), row, 3);
                    model.setValueAt(qtd, row, 4);
                    model.setValueAt(descontoPercent, row, 5);
                    model.setValueAt(taxaIva, row, 6);
                    model.setValueAt(taxaRet, row, 7);
                    model.setValueAt(totalComRetencao, row, 8);
                    model.setValueAt(CfMethods.formatarComoMoeda(valorIliquido), row, 9);
                    model.setValueAt(CfMethods.formatarComoMoeda(totalComIva), row, 10);

                    // ---------- 4. Criar linha vazia ao final ----------
                    int lastRow = model.getRowCount() - 1;
                    if (!linhaEstaVazia(model, lastRow)) {
                        model.addRow(new Object[]{
                            "", "", "", "", "", "", "", "", "", "", ""
                        });
                    }

                    // ---------- 5. Focar próximo item ----------
                    int novaLinha = model.getRowCount() - 1;
                    table.changeSelection(novaLinha, 0, false, false);
                    table.editCellAt(novaLinha, 0);
                    table.requestFocus();

                } else {
                    JOptionPane.showMessageDialog(null, "O produto: " + produto.getDesignacao() + " não pode ser vendido pra esta quantidade\nDeve-se emitir compra ou tranferência pra preencher o stock!", DVML.DVML_COMERCIAL, JOptionPane.ERROR_MESSAGE);
                }

                actualizaTotaisVerificacaoExistencia();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void actualizaTotaisVerificacaoExistencia() {
        // --------------------------
        // 7. Totais
        // --------------------------
        setTotalRetencao();
        setTotalPagar();
        calculaTotalIVA();
        valor_por_extenco();
    }

    private boolean linhaEstaVazia(DefaultTableModel model, int row) {
        for (int col = 0; col < model.getColumnCount(); col++) {
            Object v = model.getValueAt(row, col);
            if (v != null && !v.toString().trim().isEmpty()) {
                return false;
            }
        }
        return true;
    }

//private void procedimento_codBarra__jtable(){
//    
//    table.addKeyListener(new java.awt.event.KeyAdapter() {
//    @Override
//    public void keyPressed(java.awt.event.KeyEvent evt) {
//        if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
//            int row = table.getSelectedRow();
//            int col = table.getSelectedColumn();
//
//            // ENTER pressionado na coluna 0 → pesquisar produto
//            if (col == 0) {
//                accao_codigo_barra_enter_jtable(row);
//                evt.consume(); // impede pular linha automaticamente
//            }
//        }
//    }
//});
//
//    
//}
    private void accao_codigo_barra_enter_jtable(int row) {

        try {
            DefaultTableModel model = (DefaultTableModel) table.getModel();

            // Lê o código digitado na coluna 0
            String codigoBarra = model.getValueAt(row, 0).toString().trim();

            if (codigoBarra.isEmpty()) {
                JOptionPane.showMessageDialog(null,
                        "Digite o código de barra!",
                        DVML.DVML_COMERCIAL,
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            // --- Buscar produto ---
            TbProduto produto = produtosController.findByCodBarra(codigoBarra);

            if (produto == null) {
                JOptionPane.showMessageDialog(null,
                        "Produto não encontrado!",
                        DVML.DVML_COMERCIAL,
                        JOptionPane.ERROR_MESSAGE);
                model.setValueAt("", row, 0);
                table.changeSelection(row, 0, false, false);
                table.editCellAt(row, 0);
                return;
            }

            // Preencher linha inteira
            preencherLinhaTabelaComProduto(row, produto);

            // Criar nova linha se necessário
            adicionarLinhaVaziaSeNecessario();

            // Atualizar totais (SE existir este método)
//        calcularTotais();
            // Focar na nova última linha
            int last = model.getRowCount() - 1;
            table.changeSelection(last, 0, false, false);
            table.editCellAt(last, 0);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//private void preencherLinhaTabelaComProduto(int row, TbProduto produto) {
//
//    DefaultTableModel model = (DefaultTableModel) table.getModel();
//
//    int codigoProduto = produto.getCodigo();
//    String descricao = produto.getDesignacao();
//    String unidade = getUnidade_Produto();
//
//    BigDecimal qtd = BigDecimal.valueOf(1);
//    BigDecimal preco = BigDecimal.valueOf(getPreco());
//    BigDecimal descontoPercent = BigDecimal.valueOf(produto.getPercentagemDesconto());
//    BigDecimal taxaIva = BigDecimal.valueOf(getTaxaImpostoIva(codigoProduto));
//    BigDecimal taxaRet = BigDecimal.valueOf(getTaxaImpostoRet(codigoProduto));
//    BigDecimal valorIliquido = FinanceUtils.getValorIliquido(qtd, preco, descontoPercent);
//
//    double valorLiquidoDouble = FinanceUtils.getValorComIVA(
//            qtd.doubleValue(),
//            taxaIva.doubleValue(),
//            preco.doubleValue(),
//            descontoPercent.doubleValue()
//    );
//
//    BigDecimal totalComIva = BigDecimal.valueOf(valorLiquidoDouble);
//
//    String totalComRet = CfMethods.formatarComoMoeda(
//            MetodosUtil.getValorComRetencao(
//                    qtd.doubleValue(),
//                    taxaRet.doubleValue(),
//                    preco.doubleValue(),
//                    descontoPercent.doubleValue()
//            )
//    );
//
//    // Preencher colunas
//    model.setValueAt(codigoProduto, row, 0);
//    model.setValueAt(descricao, row, 1);
//    model.setValueAt(unidade, row, 2);
//    model.setValueAt(CfMethods.formatarComoMoeda(preco), row, 3);
//    model.setValueAt(qtd, row, 4);
//    model.setValueAt(descontoPercent, row, 5);
//    model.setValueAt(taxaIva, row, 6);
//    model.setValueAt(taxaRet, row, 7);
//    model.setValueAt(totalComRet, row, 8);
//    model.setValueAt(CfMethods.formatarComoMoeda(valorIliquido), row, 9);
//    model.setValueAt(CfMethods.formatarComoMoeda(totalComIva), row, 10);
//}
//private void preencherLinhaTabelaComProduto(int row, TbProduto produto) {
//
//    DefaultTableModel model = (DefaultTableModel) table.getModel();
//
//    int codigoProduto = produto.getCodigo();
//    String descricao = produto.getDesignacao();
//    String unidade = getUnidade_Produto();
//
//    // ------------------------------
//    // PEGAR PREÇO CORRETO DO PRODUTO
//    // ------------------------------
//    BigDecimal qtd = BigDecimal.ONE; // normalmente 1
//    BigDecimal preco = produto.getPreco(); // <- pegar do produto, SEM IVA
//    BigDecimal descontoPercent = BigDecimal.valueOf(produto.getPercentagemDesconto());
//    BigDecimal taxaIva = BigDecimal.valueOf(getTaxaImpostoIva(codigoProduto));
//    BigDecimal taxaRet = BigDecimal.valueOf(getTaxaImpostoRet(codigoProduto));
//
//    // ------------------------------
//    // CALCULAR VALORES
//    // ------------------------------
//    BigDecimal valorIliquido = FinanceUtils.getValorIliquido(qtd, preco, descontoPercent);
//
//    double valorComIvaDouble = FinanceUtils.getValorComIVA(
//            qtd.doubleValue(),
//            taxaIva.doubleValue(),
//            preco.doubleValue(),
//            descontoPercent.doubleValue()
//    );
//    BigDecimal totalComIva = BigDecimal.valueOf(valorComIvaDouble)
//            .setScale(2, RoundingMode.HALF_UP);
//
//    double valorComRetDouble = MetodosUtil.getValorComRetencao(
//            qtd.doubleValue(),
//            taxaRet.doubleValue(),
//            preco.doubleValue(),
//            descontoPercent.doubleValue()
//    );
//    BigDecimal totalComRetencao = BigDecimal.valueOf(valorComRetDouble)
//            .setScale(2, RoundingMode.HALF_UP);
//
//    // ------------------------------
//    // PREENCHER LINHA
//    // ------------------------------
//    model.setValueAt(codigoProduto, row, 0);
//    model.setValueAt(descricao, row, 1);
//    model.setValueAt(unidade, row, 2);
//    model.setValueAt(CfMethods.formatarComoMoeda(preco), row, 3); // SEM IVA
//    model.setValueAt(qtd, row, 4);
//    model.setValueAt(descontoPercent, row, 5);
//    model.setValueAt(taxaIva, row, 6);
//    model.setValueAt(taxaRet, row, 7);
//    model.setValueAt(CfMethods.formatarComoMoeda(totalComRetencao), row, 8);
//    model.setValueAt(CfMethods.formatarComoMoeda(valorIliquido), row, 9);
//    model.setValueAt(CfMethods.formatarComoMoeda(totalComIva), row, 10);
//}
    private void preencherLinhaTabelaComProduto(int row, TbProduto produto) {

        DefaultTableModel model = (DefaultTableModel) table.getModel();
//
        int codigoProduto = produto.getCodigo();
        String descricao = produto.getDesignacao();

        String unidade = getUnidade_Produto();

        BigDecimal qtd = new BigDecimal(txtQuatindade.getText());
        BigDecimal preco = BigDecimal.valueOf(getPreco(codigoProduto, qtd.doubleValue()));
        BigDecimal descontoPercent = BigDecimal.valueOf(getDescontoPercentagem());
        BigDecimal taxaIva = BigDecimal.valueOf(getTaxaImpostoIva(codigoProduto));
        BigDecimal taxaRet = BigDecimal.valueOf(getTaxaImpostoRet(codigoProduto));

        BigDecimal valorIliquido = FinanceUtils.getValorIliquido(qtd, preco, descontoPercent);

        double valorLiquidoDouble = FinanceUtils.getValorComIVA(
                qtd.doubleValue(),
                taxaIva.doubleValue(),
                preco.doubleValue(),
                descontoPercent.doubleValue()
        );
        BigDecimal totalComIva = BigDecimal.valueOf(valorLiquidoDouble);

        String totalRetencao = CfMethods.formatarComoMoeda(
                MetodosUtil.getValorComRetencao(
                        qtd.doubleValue(),
                        taxaRet.doubleValue(),
                        preco.doubleValue(),
                        descontoPercent.doubleValue()
                )
        );

        // ------------------------------
        // Preencher tabela
        // ------------------------------
        model.setValueAt(codigoProduto, row, 0);
        model.setValueAt(descricao, row, 1);
        model.setValueAt(unidade, row, 2);
        model.setValueAt(CfMethods.formatarComoMoeda(preco), row, 3); // SEM IVA
        model.setValueAt(qtd, row, 4);
        model.setValueAt(descontoPercent, row, 5);
        model.setValueAt(taxaIva, row, 6);
        model.setValueAt(taxaRet, row, 7);
        model.setValueAt(totalRetencao, row, 8);
//    model.setValueAt(CfMethods.formatarComoMoeda(totalRetencao), row, 8);
        model.setValueAt(CfMethods.formatarComoMoeda(valorIliquido), row, 9);
        model.setValueAt(CfMethods.formatarComoMoeda(totalComIva), row, 10);

        // --------------------------
        // 7. Totais
        // --------------------------
        setTotalRetencao();
        setTotalPagar();
        calculaTotalIVA();
        valor_por_extenco();
    }

//private void preencherLinhaTabelaComProduto(int row, TbProduto produto) {
//
//    DefaultTableModel model = (DefaultTableModel) table.getModel();
//
//    int codigoProduto = produto.getCodigo();
//    String descricao = produto.getDesignacao();
//    String unidade = getUnidade_Produto();
//
//    BigDecimal taxaIva = BigDecimal.valueOf(getTaxaImpostoIva(codigoProduto));
//    BigDecimal taxaRet = BigDecimal.valueOf(getTaxaImpostoRet(codigoProduto));
//    BigDecimal descontoPercent = BigDecimal.valueOf(produto.getPercentagemDesconto());
//    BigDecimal qtd = BigDecimal.ONE; // normalmente 1
//
//    // ------------------------------
//    // PREÇO SEM IVA
//    // ------------------------------
//    BigDecimal preco = produto.getPreco(); // preço do produto (pode vir com IVA)
//    BigDecimal precoSemIva = preco.divide(
//            BigDecimal.ONE.add(taxaIva.divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP)),
//            2,
//            RoundingMode.HALF_UP
//    );
//
//    // ------------------------------
//    // CALCULAR VALORES
//    // ------------------------------
//    BigDecimal valorIliquido = FinanceUtils.getValorIliquido(qtd, precoSemIva, descontoPercent);
//
//    BigDecimal totalComIva = valorIliquido
//            .multiply(BigDecimal.ONE.add(taxaIva.divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP)))
//            .setScale(2, RoundingMode.HALF_UP);
//
//    BigDecimal totalComRetencao = valorIliquido
//            .multiply(BigDecimal.ONE.add(taxaRet.divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP)))
//            .setScale(2, RoundingMode.HALF_UP);
//
//    // ------------------------------
//    // PREENCHER LINHA
//    // ------------------------------
//    model.setValueAt(codigoProduto, row, 0);
//    model.setValueAt(descricao, row, 1);
//    model.setValueAt(unidade, row, 2);
//    model.setValueAt(CfMethods.formatarComoMoeda(precoSemIva), row, 3); // SEM IVA
//    model.setValueAt(qtd, row, 4);
//    model.setValueAt(descontoPercent, row, 5);
//    model.setValueAt(taxaIva, row, 6);
//    model.setValueAt(taxaRet, row, 7);
//    model.setValueAt(CfMethods.formatarComoMoeda(totalComRetencao), row, 8);
//    model.setValueAt(CfMethods.formatarComoMoeda(valorIliquido), row, 9);
//    model.setValueAt(CfMethods.formatarComoMoeda(totalComIva), row, 10);
//}
    private void adicionarLinhaVaziaSeNecessario() {
        DefaultTableModel model = (DefaultTableModel) table.getModel();

        int last = model.getRowCount() - 1;
        boolean vazia = true;

        for (int col = 0; col < model.getColumnCount(); col++) {
            Object v = model.getValueAt(last, col);
            if (v != null && !v.toString().trim().isEmpty()) {
                vazia = false;
                break;
            }
        }

        if (!vazia) {
            model.addRow(new Object[]{
                "", "", "", "", "", "", "", "", "", "", ""
            });
        }
    }

//private void procedimento_codBarra__jtable() {
//
//    table.putClientProperty("terminateEditOnFocusLost", true);
//
//    table.getColumnModel().getColumn(0).setCellEditor(new DefaultCellEditor(new JTextField() {
//        @Override
//        protected void processKeyEvent(KeyEvent e) {
//            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
//
//                int row = table.getSelectedRow();
//                int col = table.getSelectedColumn();
//
//                if (col == 0) {
    ////                    accao_codigo_barra_enter_jtable(row);
//                    accao_codigo_produto_enter_jtable(row);
//                    e.consume();
//                    return;
//                }
//            }
//
//            super.processKeyEvent(e);
//        }
//    }));
//}
    private void procedimento_codBarra__jtable() {

        // Criar o editor de texto que será usado na coluna 0
        JTextField editor = new JTextField();

        // Capturar o ENTER dentro do editor da célula
        editor.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {

                    // Salvar o texto digitado no model ANTES de pesquisar
                    if (table.isEditing()) {
                        table.getCellEditor().stopCellEditing();
                    }

                    int row = table.getSelectedRow();
                    int col = table.getSelectedColumn();

                    // Só dispara pesquisa se estiver na coluna 0
                    if (col == 0) {
                        accao_codigo_produto_enter_jtable(row);
                        e.consume(); // impede JTable de mudar de linha automaticamente
                    }
                }
            }
        });

        // Aplicar o editor na coluna 0 da JTable
        table.getColumnModel().getColumn(0).setCellEditor(new DefaultCellEditor(editor));
    }

    private void accao_codigo_produto_enter_jtable(int row) {

        try {
            DefaultTableModel model = (DefaultTableModel) table.getModel();

            String valorDigitado = model.getValueAt(row, 0).toString().trim();

            if (valorDigitado.isEmpty()) {
                JOptionPane.showMessageDialog(null,
                        "Digite o código!",
                        DVML.DVML_COMERCIAL,
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            TbProduto produto = null;

            // -------------------------------
            // 1) DETECTAR AUTOMATICAMENTE
            // -------------------------------
            if (valorDigitado.matches("\\d+")) {

                try {
                    // só números → pode ser ID interno
                    int codigo = Integer.parseInt(valorDigitado);

                    // tenta buscar por ID interno
                    produto = (TbProduto) produtosController.findById(codigo);
                } catch (Exception e) {
                }

                // se não achar por ID → tenta como código de barras numérico
                if (produto == null) {
                    produto = produtosController.findByCodBarra(valorDigitado);
                }

            } else {
                // contém letras ou formato de código → buscar como código de barras
                produto = produtosController.findByCodBarra(valorDigitado);
            }

            // -------------------------------
            // 2) Se mesmo assim não encontrou
            // -------------------------------
            if (produto == null) {
                JOptionPane.showMessageDialog(null,
                        "Produto não encontrado!",
                        DVML.DVML_COMERCIAL,
                        JOptionPane.ERROR_MESSAGE);

                model.setValueAt("", row, 0);
                table.changeSelection(row, 0, false, false);
                table.editCellAt(row, 0);
                return;
            }

            if (possivel_quantidade(produto.getCodigo(), 1)) {
                adicionar_preco_quantidade_anitgo(produto.getCodigo());

                // -------------------------------
                // 3) Preencher linha normalmente
                // -------------------------------
                preencherLinhaTabelaComProduto(row, produto);

                // linha vazia se necessário
                adicionarLinhaVaziaSeNecessario();

                // focar próxima linha
            } else {
                JOptionPane.showMessageDialog(null, "Quantidade inexistente no Stock.");
            }

//            int last = model.getRowCount() - 1;
//            table.getModel().setValueAt( "", last, 0 );
//            table.changeSelection( last, 0, false, false );
//            table.editCellAt( last, 0 );
//            table.requestFocusInWindow();
//
//            Component editor = table.getEditorComponent();
//            if ( editor != null )
//            {
//                editor.requestFocusInWindow();
//            }
            cursorLinha();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void cursorLinha() {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        int last = model.getRowCount() - 1;
        table.getModel().setValueAt("", last, 0);
        table.changeSelection(last, 0, false, false);
        table.editCellAt(last, 0);
        table.requestFocusInWindow();

        Component editor = table.getEditorComponent();
        if (editor != null) {
            editor.requestFocusInWindow();
        }
    }

    private void initStockListener() {
        // Pegamos a coluna do código do produto
        TableColumn codigoCol = table.getColumnModel().getColumn(0);

        // Garantimos que qualquer editor usado seja monitorado
        TableCellEditor editor = codigoCol.getCellEditor();
        if (editor == null) {
            editor = table.getDefaultEditor(Object.class);
            codigoCol.setCellEditor(editor);
        }

        editor.addCellEditorListener(new CellEditorListener() {
            @Override
            public void editingStopped(ChangeEvent e) {
                int row = table.getSelectedRow();
                if (row < 0) {
                    return;
                }

                // Executa após a célula terminar de editar
                SwingUtilities.invokeLater(()
                        -> {
                    mostrarStockKeyPress(row);
                    // Move foco automaticamente para a coluna quantidade (1)
                    table.changeSelection(row, 1, false, false);
                });
            }

            @Override
            public void editingCanceled(ChangeEvent e) {
                // Nada a fazer
            }
        });
    }

    /**
     * Mostra a quantidade de stock e preço baseado no código do produto.
     */
    private void mostrarStockKeyPress(int row) {
        try {
            Object codigoObj = table.getValueAt(row, 0);
            if (codigoObj == null || codigoObj.toString().trim().isEmpty()) {
                return;
            }

            int codigoProduto;
            try {
                codigoProduto = Integer.parseInt(codigoObj.toString().trim());
            } catch (NumberFormatException ex) {
                System.err.println("Código inválido na linha " + row + ": " + codigoObj);
                return;
            }

            TbStock stockLocal = stocksController.getStockByIdProdutoAndIdArmazem(codigoProduto, getCodigoArmazem());
            if (stockLocal != null) {
                // Atualiza JTextField
                txtQuantidadeStock.setText(String.valueOf(stockLocal.getQuantidadeExistente()));

                // Colore campo se abaixo do critico
                if (stockLocal.getQuantidadeExistente() <= stockLocal.getQuantCritica()) {
                    txtQuantidadeStock.setBackground(Color.RED);
                    txtQuantidadeStock.setForeground(Color.BLACK);
                } else {
                    txtQuantidadeStock.setBackground(new Color(51, 153, 0));
                    txtQuantidadeStock.setForeground(Color.BLACK);
                }

                // Atualiza preço se quantidade informada
                if (!txtQuatindade.getText().isEmpty()) {
                    TbPreco precoLocal = precosController.getLastIdPrecoByIdProduto(
                            codigoProduto, Double.parseDouble(txtQuatindade.getText())
                    );
                    txtPreco.setText(String.valueOf(MetodosUtil.retirar_dizimas(precoLocal.getPrecoVenda().doubleValue())));
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(VendaModernaVisao.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
    }

    private static void removerUltimaLinhaVazia() {
        DefaultTableModel modelo = (DefaultTableModel) table.getModel();
        int lastRow = modelo.getRowCount() - 1;

        if (lastRow < 0) {
            return; // Não há linhas
        }
        boolean vazia = true;
        // Verifica se todas as células da última linha estão nulas ou vazias
        for (int col = 0; col < modelo.getColumnCount(); col++) {
            Object valor = modelo.getValueAt(lastRow, col);
            if (valor != null && !valor.toString().trim().isEmpty()) {
                vazia = false;
                break;
            }
        }

        if (vazia) {
            modelo.removeRow(lastRow);
        }

//        table.repaint();
    }

    private void actualizarPreco() {
        String preco = table.getValueAt(table.getSelectedRow(), 3).toString();
        int idProduto = Integer.parseInt(table.getValueAt(table.getSelectedRow(), 0).toString());
        double taxa = Double.parseDouble(table.getValueAt(table.getSelectedRow(), 6).toString());
        // Aceita tanto "1.200,50 AOA" (formatado) como "1200.50" (BigDecimal)
        double parseMoedaFormatada = CfMethods.parseMoedaFormatadaNovo(preco);

//        parseMoedaFormatada = (parseMoedaFormatada / (1 + (taxa / 100)));
        BigDecimal precoSemIva = BigDecimal.valueOf(parseMoedaFormatada).setScale(
                DVML.CASAS_DECIMAIS_TAXA,
                RoundingMode.HALF_UP);

        System.out.println("PREÇO SEM IVA:  " + precoSemIva);

        // A célula do preço tem de ficar SEMPRE no formato de moeda (vírgula decimal).
        // O BigDecimal cru ("701.7500000") era lido como 7.017.500.000 pelo
        // CfMethods.parseMoedaFormatada (pt-PT), inflacionando os totais da venda.
        table.setValueAt(CfMethods.formatarComoMoeda(precoSemIva), table.getSelectedRow(), 3);

        actualizarPrecoVendaManual(idProduto,
                precoSemIva.doubleValue(),
                precosController);
    }

    private static void actualizarPrecoVendaManual(int idProduto, Double precoVenda, PrecosController precosControllerLocal) {

        Vector<TbPreco> vectorDoisPrecoAnterior = new Vector<>();
        conexaoTransaction = BDConexao.getInstancia();
        DocumentosController.start(conexaoTransaction);

        System.out.println("ID PRODUTO: " + idProduto);

        int idPrecoRetalho = PrecosController.getLastIdPrecoByIdProdutoIntAndQTD(idProduto, 0d, conexaoTransaction);
        System.out.println("ID RETALHO: " + idPrecoRetalho);
        TbPreco precoAntigoRetalho = (TbPreco) precosControllerLocal.findById(idPrecoRetalho);

        System.out.println("PRECO RETALHO: " + precoAntigoRetalho);

        int idPrecoGrosso = PrecosController.getLastIdPrecoByIdProdutoIntAndPrecoAntigoQtdAlto(idProduto, precoAntigoRetalho.getQtdAlto() + 1, conexaoTransaction);
        TbPreco precoAntigoGrosso = (TbPreco) precosControllerLocal.findById(idPrecoGrosso);
        System.out.println("ID GROSSO: " + idPrecoGrosso);
        System.out.println("PRECO GROSSO: " + precoAntigoGrosso);

        TbPreco preco_novo_retalho = new TbPreco();

        preco_novo_retalho.setData(precoAntigoRetalho.getData());
        preco_novo_retalho.setHora(precoAntigoRetalho.getHora());
        preco_novo_retalho.setPercentagemGanho(precoAntigoRetalho.getPercentagemGanho());

        preco_novo_retalho.setFkProduto(precoAntigoRetalho.getFkProduto());
        preco_novo_retalho.setFkUsuario(precoAntigoRetalho.getFkUsuario());
        preco_novo_retalho.setPrecoCompra(precoAntigoRetalho.getPrecoCompra());
        preco_novo_retalho.setPrecoVenda(new BigDecimal(precoVenda));
        preco_novo_retalho.setQtdBaixo(precoAntigoRetalho.getQtdBaixo());
        preco_novo_retalho.setQtdAlto(precoAntigoRetalho.getQtdAlto());
        preco_novo_retalho.setPrecoAnterior(precoAntigoRetalho.getPrecoAnterior());
        preco_novo_retalho.setRetalho(precoAntigoRetalho.getRetalho());

        System.out.println("HORA RETALHO: " + preco_novo_retalho.getHora());
        System.out.println("DATA RETAHO: " + preco_novo_retalho.getData());

        try {
            precosControllerLocal.salvar(preco_novo_retalho);
            DocumentosController.commit(conexaoTransaction);
            conexaoTransaction.close();
//            precoDao.create(preco_novo_retalho);
            System.out.println("Preco de compra retalho actualizado na venda");
        } catch (Exception e) {
            DocumentosController.rollback(conexaoTransaction);
            e.printStackTrace();
            System.err.println("Falha ao actualizar o preço retalho na venda");
        }

        conexaoTransaction = BDConexao.getInstancia();
        DocumentosController.start(conexaoTransaction);
        try {
            //        preco_novo_grosso = precoAntigoGrosso;
            TbPreco preco_novo_grosso = new TbPreco();

            preco_novo_grosso.setData(precoAntigoGrosso.getData());
            preco_novo_grosso.setHora(precoAntigoGrosso.getHora());
            preco_novo_grosso.setPercentagemGanho(precoAntigoGrosso.getPercentagemGanho());

            preco_novo_grosso.setFkProduto(precoAntigoGrosso.getFkProduto());
            preco_novo_grosso.setFkUsuario(precoAntigoGrosso.getFkUsuario());
            preco_novo_grosso.setPrecoCompra(precoAntigoGrosso.getPrecoCompra());
            preco_novo_grosso.setPrecoVenda(new BigDecimal(precoVenda));
            preco_novo_grosso.setQtdBaixo(precoAntigoGrosso.getQtdBaixo());
            preco_novo_grosso.setQtdAlto(precoAntigoGrosso.getQtdAlto());
            preco_novo_grosso.setPrecoAnterior(precoAntigoGrosso.getPrecoAnterior());
            preco_novo_grosso.setRetalho(precoAntigoGrosso.getRetalho());

            System.out.println("HORA GROSSO: " + precoAntigoGrosso.getHora());
            System.out.println("DATA GROSSO: " + precoAntigoGrosso.getData());

            precosControllerLocal.salvar(preco_novo_grosso);

            DocumentosController.commit(conexaoTransaction);
            conexaoTransaction.close();
//            precoDao.create(preco_novo_grosso);
            System.out.println("Preco de compra grosso actualizado na compra");
        } catch (Exception e) {
            DocumentosController.rollback(conexaoTransaction);
            e.printStackTrace();
            System.err.println("Falha ao actualizar o preço grosso na compra");
        }

        if (!existeProdutoPrecoLista(idProduto)) {
            vectorDoisPrecoAnterior.add(precoAntigoRetalho);
            vectorDoisPrecoAnterior.add(precoAntigoGrosso);
            System.err.println("Adicionei o preco retalho antigo " + precoAntigoRetalho.getPrecoVenda().doubleValue());
            System.err.println("Adicionei o preco grosso antigo " + precoAntigoGrosso.getPrecoVenda().doubleValue());

            listaPrecoTemp.add(vectorDoisPrecoAnterior);
        }

    }

    private static boolean existeProdutoPrecoLista(int idProduto) {

        for (int i = 0; i < listaPrecoTemp.size(); i++) {
            Vector<TbPreco> get = listaPrecoTemp.get(i);
            if (get.get(0).getFkProduto().getCodigo() == idProduto) {
                return true;
            }

        }
        return false;

    }

//    private void actualizarQtdTable() {
//
//        linha_actual = table.getSelectedRow();
//
//        if (linha_actual > -1) {
//
//            int codProduto = Integer.parseInt(table.getValueAt(linha_actual, 0).toString());
//            double desconto = Double.parseDouble(table.getValueAt(linha_actual, 5).toString());
//
//            TbProduto produtoLocal = (TbProduto) produtosController.findById(codProduto);
//            TbTipoProduto tipoProduto = (TbTipoProduto) tipoProdutoController.findById(produtoLocal.getCodTipoProduto().getCodigo());
//
//            double qtd;
//
//            try {
//                qtd = Double.parseDouble(table.getValueAt(linha_actual, 4).toString());
//            } catch (NumberFormatException e) {
//                resetValue("Erro de formatação da quantidade.\nAtenção: Tem que ser número.", 4);
//                return;
//            }
//
//            if (qtd <= 0) {
//                qtd = 1;
//                resetValue("Quantidade não pode ser zero(0) ou número negativo", 4);
//            }
//
//            if (desconto < 0) {
//                desconto = 0;
//                resetValue("O desconto não deve ser um número negativo.", 4);
//            } else if (desconto > 100) {
//                desconto = 0;
//                resetValue("O desconto não deve ser maior do que 100%", 4);
//            }
//
//            if (possivel_quantidade(codProduto, qtd) || tipoProduto.getFkFamilia().getPkFamilia() == DVML.COD_SERVICO) {
//                actuazlizar_quantidade_tabela_formulario(String.valueOf(qtd), desconto);
//                setTotalRetencao();
//                setTotalPagar();
//                setTotalPagarGeral();
//                calculaTotalIVA();
//                valor_por_extenco();
//            } else {
//                resetValue("Não é possivel para esta quantidade.", 4);
//            }
//        }
//    }
    // =====================================================================
    // VENDA: painel de lotes ao SELECCIONAR uma linha e ao MUDAR A QUANTIDADE
    // na JTable da venda.
    //
    // 1) SUBSTITUI o teu actualizarQtdTable() por este (marcado com // LOTE).
    // 2) ACRESCENTA os dois métodos novos abaixo.
    // 3) No CONSTRUTOR, logo a seguir a initComponents(), acrescenta:
    //        ligarPainelLotesASelecaoDaTabela();
    //
    // O actualizarPainelLotes(...) que já tens é o método único que enche o
    // painel; aqui só se chama com os dados da linha. Não mexe em mais nada.
    // =====================================================================
    private void actualizarQtdTable() {

        linha_actual = table.getSelectedRow();

        if (linha_actual > -1) {

            int codProduto = Integer.parseInt(table.getValueAt(linha_actual, 0).toString());
            String descricaoLinha = table.getValueAt(linha_actual, 1).toString(); // LOTE: descrição da linha
            double desconto = Double.parseDouble(table.getValueAt(linha_actual, 5).toString());

            TbProduto produtoLocal = (TbProduto) produtosController.findById(codProduto);
            TbTipoProduto tipoProduto = (TbTipoProduto) tipoProdutoController.findById(produtoLocal.getCodTipoProduto().getCodigo());

            double qtd;

            try {
                qtd = Double.parseDouble(table.getValueAt(linha_actual, 4).toString());
            } catch (NumberFormatException e) {
                resetValue("Erro de formatação da quantidade.\nAtenção: Tem que ser número.", 4);
                return;
            }

            if (qtd <= 0) {
                qtd = 1;
                resetValue("Quantidade não pode ser zero(0) ou número negativo", 4);
            }

            if (desconto < 0) {
                desconto = 0;
                resetValue("O desconto não deve ser um número negativo.", 4);
            } else if (desconto > 100) {
                desconto = 0;
                resetValue("O desconto não deve ser maior do que 100%", 4);
            }

            boolean servico = tipoProduto.getFkFamilia().getPkFamilia() == DVML.COD_SERVICO; // LOTE

            if (possivel_quantidade(codProduto, qtd) || servico) {

                // LOTE: valida a nova quantidade nos lotes (FEFO) e actualiza o painel
                // "a retirar". Serviços não têm lotes. Produtos sem lotes registados
                // devolvem null (nada a validar). Faz-se ANTES de gravar a linha:
                // se não houver lotes suficientes, repõe a quantidade e mostra porquê.
                if (!servico) {
                    String erroLotes;
                    try {
                        erroLotes = actualizarPainelLotes(codProduto, descricaoLinha, qtd);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        erroLotes = "Erro ao consultar os lotes: " + ex.getMessage();
                    }
                    if (erroLotes != null) {
                        resetValue(erroLotes, 4); // repõe a quantidade (e volta a chamar este método)
                        return;
                    }
                }

                actuazlizar_quantidade_tabela_formulario(String.valueOf(qtd), desconto);
                setTotalRetencao();
                setTotalPagar();
                setTotalPagarGeral();
                calculaTotalIVA();
                valor_por_extenco();
            } else {
                resetValue("Não é possivel para esta quantidade.", 4);
            }
        }
    }

    // ---------------------------------------------------------------------
    // NOVO: ao seleccionar uma linha da venda, o painel mostra os lotes desse
    // produto com a quantidade da linha (o que vai sair de cada lote).
    // ---------------------------------------------------------------------
    private void ligarPainelLotesASelecaoDaTabela() {
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                actualizarPainelLotesLinhaSeleccionada();
            }
        });
    }

    private void actualizarPainelLotesLinhaSeleccionada() {

        if (painelLotes == null) {
            return;
        }

        int linhaSel = table.getSelectedRow();
        if (linhaSel < 0 || linhaSel >= table.getRowCount()) {
            if (table.getRowCount() == 0) {
                painelLotes.limpar(); // venda vazia: sem lotes a mostrar
            }
            return; // sem selecção (mas com linhas): mantém o que está
        }

        try {
            int codProduto = Integer.parseInt(table.getValueAt(linhaSel, 0).toString());
            String descricao = table.getValueAt(linhaSel, 1).toString();
            double qtd = Double.parseDouble(table.getValueAt(linhaSel, 4).toString().replace(",", "."));

            if (qtd <= 0) {
                painelLotes.limpar();
                return;
            }

            // aqui não há janela: só mostra no painel (o aviso, se houver, fica a vermelho nele)
            actualizarPainelLotes(codProduto, descricao, qtd);

        } catch (SQLException | NumberFormatException ex) {
            painelLotes.limpar();
            ex.printStackTrace();
        }
    }

    private static void actuazlizar_quantidade_tabela_formulario(String quantidade, double desconto) {
        DefaultTableModel modelo = (DefaultTableModel) table.getModel();
        double qtd = Double.parseDouble(quantidade);
        double retencao = 0;

        int idProduto = Integer.parseInt(String.valueOf(modelo.getValueAt(linha_actual, 0)));

//        double preco_venda = CfMethods.parseMoedaFormatada( String.valueOf( modelo.getValueAt( linha_actual, 3 ) ) );
        System.err.println("QUANTIDADE:  " + qtd);

        double preco_venda = getPreco(idProduto, qtd);

        System.out.println("PRECO VENDA: " + preco_venda);
        double taxa = Double.parseDouble(String.valueOf(modelo.getValueAt(linha_actual, 6)));
//        double ret = CfMethods.parseMoedaFormatada( String.valueOf( modelo.getValueAt( linha_actual, 7) ) );
        double ret = Double.parseDouble(String.valueOf(modelo.getValueAt(linha_actual, 7)));

//        double total_iliquido_linha = (( preco_venda * qtd ) - desconto);
        String total_iliquido_linha = CfMethods.formatarComoMoeda(FinanceUtils.getValorIliquido(
                new BigDecimal(1),
                new BigDecimal(preco_venda),
                new BigDecimal(desconto)
        ));//Unidade

        double totalComIva = FinanceUtils.getValorComIVA(1, taxa, CfMethods.parseMoedaFormatada(total_iliquido_linha), 0); //Unidade
        totalComIva = totalComIva * qtd;

        String total_liquido_linha = CfMethods.formatarComoMoeda(totalComIva);

        retencao = MetodosUtil.getValorComRetencao(qtd, ret, preco_venda, desconto);

        String total_retencao = CfMethods.formatarComoMoeda(retencao);

        double totalIliquidoItem = CfMethods.parseMoedaFormatada(total_iliquido_linha) * qtd;
        String precoString = CfMethods.formatarComoMoeda(preco_venda);
        String totalIlquidoString = CfMethods.formatarComoMoeda(totalIliquidoItem);

        modelo.setValueAt(precoString, linha_actual, 3);
        modelo.setValueAt(qtd, linha_actual, 4);
        modelo.setValueAt(desconto, linha_actual, 5);
        modelo.setValueAt(total_retencao, linha_actual, 8);

        modelo.setValueAt(totalIlquidoString, linha_actual, 9);
        modelo.setValueAt(total_liquido_linha, linha_actual, 10);
        //a linha_actual recebe o default
        linha_actual = -1;

    }

    private void resetValue(String msg, int columnValue) {
        System.out.println("Cheguei aqui...");
        table.setValueAt(1, linha_actual, columnValue);
        JOptionPane.showMessageDialog(null, msg);
        table.clearSelection();

    }

    public static void accao_codigo_interno_enter_busca_exterior_2(int codigo, String mes) {

        try {

            produtosController = new ProdutosController(conexao);
            System.out.println("ID PRODUTO EXTERIOR: " + codigo);
            TbProduto produtoLocal = (TbProduto) produtosController.findById(codigo);
            double qtd = Double.parseDouble(txtQuatindade.getText());
            procedimentoAdicionarTabela2(produtoLocal, mes, qtd);

        } catch (Exception ex) {
            ex.printStackTrace();
            Logger
                    .getLogger(VendaUsuarioVisao.class
                            .getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Este produto não existe no armazém " + cmbArmazem.getSelectedItem(), DVML.DVML_COMERCIAL, JOptionPane.ERROR_MESSAGE);
        }

    }

    private void tratar_desconto() {
        try {
            double desconto = 0.0;
            double total_pagar = getTotalPagar();
            double valor_desconto_geral = (double) sp_desconto_financeiro.getValue();

            if (valor_desconto_geral > total_pagar) {
                JOptionPane.showMessageDialog(null, "O desconto global não pode ser maior que o total à pagar.", "AVISO", JOptionPane.WARNING_MESSAGE);
                //reset desconto global
                reset_desconto_global();
                setTotalPagar();
                sp_desconto_financeiro.requestFocus();
            } else if (valor_desconto_geral == total_pagar) {
                reset_valor_entregue();
//                txtTroco.setText( "0.0" );

                desconto = (total_pagar - valor_desconto_geral);
                txtTotalPagar.setText(CfMethods.formatarComoMoeda(desconto));
                valor_por_extenco();
            } else {
                desconto = (total_pagar - valor_desconto_geral);
                txtTotalPagar.setText(CfMethods.formatarComoMoeda(desconto));
                reset_valor_entregue();
//                txtValorEntregue.setText( String.valueOf( desconto ) );
//                txtTroco.setText( "0.0" );
                valor_por_extenco();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void procedimentoChamarFormaPagemnto() {
        removerUltimaLinhaVazia();

        jScrollPane1.repaint();
        if (MetodosUtil.licencaValidada(conexao)) {
            if (!MetodosUtil.tabela_vazia(table)) {
                if (!validarPrecos_tabela(table)) {
                    return; // Se houver erro, não abre forma de pagamento
                }

                new FormaPagamentoVisao(this, rootPaneCheckingEnabled, null, DVML.VENDA_PONTUAL_TOP, BDConexao.getInstancia()).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(null, "Caro usuário, adicione itens na tabela");
            }
        }

        table.getCellEditor().cancelCellEditing();
    }

//    private void configurarTabela() {
//    table.setModel(new DefaultTableModel(
//        table.getModel().getDataVector(),
//        getColumnNames()
//    ) {
//        @Override
//        public boolean isCellEditable(int row, int column) {
//            return column == 2;
//        }
//    });
//}
    private void configurarPrecosQTDTabela() {

        DefaultTableModel model = (DefaultTableModel) table.getModel();

        table.setModel(new DefaultTableModel(
                model.getDataVector(),
                getColumnNames(model)
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {

                return (column == 0 || column == 4); // 3ª coluna editável
//                return (column == 0 || column == 3 || column == 4); // 3ª coluna editável

            }
        });
    }

    private void configurarQTDTabela() {

        DefaultTableModel model = (DefaultTableModel) table.getModel();

        table.setModel(new DefaultTableModel(
                model.getDataVector(),
                getColumnNames(model)
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {

                return (column == 0 || column == 4); // 3ª coluna editável
//                return (column == 0 || column == 3 || column == 4); // 3ª coluna editável

            }
        });
    }

    private void configurarPrecoTabela() {

//        DefaultTableModel model = (DefaultTableModel) table.getModel();
//
//        table.setModel(new DefaultTableModel(
//                model.getDataVector(),
//                getColumnNames(model)
//        ) {
//
//            @Override
//            public boolean isCellEditable(int row, int column) {
//
//              if (permitirAlterarPreco) {
//                     return (column == 0 || column == 3 || column == 4); // 3ª coluna editável
//                }
//              
//              
//            }
//        });
        DefaultTableModel model = (DefaultTableModel) table.getModel();

        table.setModel(new DefaultTableModel(
                model.getDataVector(),
                getColumnNames(model)
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {

//                return (column == 0 || column == 3 ); // 3ª coluna editável
                return (column == 0 || column == 3 || column == 4); // 3ª coluna editável

            }
        });
    }

//    private void configurarPrecoTabela() {
//
//        DefaultTableModel model = (DefaultTableModel) table.getModel();
//
//        table.setModel(new DefaultTableModel(
//                model.getDataVector(),
//                getColumnNames(model)
//        ) {
//            @Override
//            public boolean isCellEditable(int row, int column) {
//
//                return column == 3;// 3ª coluna editável
//
//            }
//        });
//    }
    private void configurarTabela(int coluna) {

        DefaultTableModel model = (DefaultTableModel) table.getModel();

        table.setModel(new DefaultTableModel(
                model.getDataVector(),
                getColumnNames(model)
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == coluna; // 3ª coluna editável
            }
        });
    }

    private Vector<String> getColumnNames(DefaultTableModel model) {
        Vector<String> colunas = new Vector<>();
        for (int i = 0; i < model.getColumnCount(); i++) {
            colunas.add(model.getColumnName(i));
        }
        return colunas;
    }

    private static void actualizarPrecosAntigos() throws Exception {
        prepararPrecosAntigos(precosController);
    }

    private static void actualizarPrecosAntigos2() {

        BDConexao conexaoLocal = new BDConexao();
        DocumentosController.start(conexaoLocal);
        PrecosController precosController = new PrecosController(conexaoLocal);
        try {
            prepararPrecosAntigos(precosController);
            DocumentosController.commit(conexaoLocal);
        } catch (Exception e) {
            DocumentosController.rollback(conexaoLocal);
        } finally {
            conexaoLocal.close();
        }

    }

    private static void prepararPrecosAntigos(PrecosController precosController) {
        System.out.println("Cheguei no actualizar precos Antigos");
        if (Objects.nonNull(listaPrecoTemp) || listaPrecoTemp.size() > 0) {
            for (int i = 0; i < listaPrecoTemp.size(); i++) {
                Vector<TbPreco> get = listaPrecoTemp.get(i);
                TbPreco precoRetalhoLocal = get.get(0);
                TbPreco precoGrossoLocal = get.get(1);
                precosController.salvar(precoRetalhoLocal);
                System.out.println("Salvei o preco retalho");
                precosController.salvar(precoGrossoLocal);
                System.out.println("Salvei o preco groso");
            }
            listaPrecoTemp.clear();
        }
    }

    private void procedimentoPenderFactura() {

        BDConexao conexaoTransactionLocal = new BDConexao();

        pedidosController = new PedidosController(conexaoTransactionLocal.getConnectionAtiva());
        itemPedidosController = new ItemPedidosController(conexaoTransactionLocal.getConnectionAtiva());
        mesasController = new MesasController(conexaoTransactionLocal);
        lugaresController = new LugaresController(conexaoTransactionLocal);

        DocumentosController.start(conexaoTransactionLocal); // Inicia a transação

        if (table.getModel().getRowCount() > 1) {
            removerUltimaLinhaVazia();
            try {
                TbMesas mesa = (TbMesas) mesasController.findById(DVML.MESA_PENDENTE);
                System.out.println("Mesa ID: " + mesa);
                TbLugares lugar = (TbLugares) lugaresController.findById(DVML.LUGAR_PENDENTE);
                TbPedido pedido = new TbPedido();
                pedido.setDataPedido(new Date());
                pedido.setHoraPedido(new Date());
                pedido.setStatusPedido(false);
                pedido.setDeposito(0d);
                pedido.setValorEmFalta(0d);
                pedido.setFkMesas(mesa);

                try {
                    pedidosController.create(pedido);
                    int lastPedidoByMesa = pedidosController.getLastPedidoByMesa(mesa.getDesignacao());

                    for (int i = 0; i < table.getRowCount(); i++) {
                        TbItemPedidos itemPedidos = new TbItemPedidos();

                        int idProduto = Integer.parseInt(table.getModel().getValueAt(i, 0).toString());
                        double qtd = Double.parseDouble(table.getModel().getValueAt(i, 4).toString());
                        double preco = CfMethods.parseMoedaFormatada(table.getModel().getValueAt(i, 3).toString());
                        double totalItem = CfMethods.parseMoedaFormatada(table.getModel().getValueAt(i, 10).toString());

                        itemPedidos.setFkProdutos(new TbProduto(idProduto));
                        itemPedidos.setFkLugares(lugar);
                        itemPedidos.setFkPedidos(new TbPedido(lastPedidoByMesa));
                        itemPedidos.setStatusConvertido(false);
                        itemPedidos.setStatusEnviado(false);
                        itemPedidos.setStatusEfectuado(false);
                        itemPedidos.setDataEntrega(new Date());
                        itemPedidos.setObs("");
                        itemPedidos.setQtd(qtd);
                        itemPedidos.setPreco(preco);
                        itemPedidos.setTotalItem(totalItem);

                        try {
                            itemPedidosController.create(itemPedidos);
                            System.out.println("Item do pedido criado com sucesso!");

                        } catch (SQLException e) {
                            JOptionPane.showMessageDialog(null,
                                    "Falha ao pendurar a factura",
                                    "FALHA",
                                    JOptionPane.ERROR_MESSAGE);

                            DocumentosController.rollback(conexaoTransactionLocal);
                            break;
                        }
                    }

                    DocumentosController.commit(conexaoTransactionLocal);
                    MetodosUtil.esvaziar_tabela(table);
                    JOptionPane.showMessageDialog(null, "Factura pendurada.");
                    inserirLinhaEmBranco();
                    configurarQTDTabela();

                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null,
                            "Falha ao pendurar a factura",
                            "FALHA",
                            JOptionPane.ERROR_MESSAGE);

                    System.out.println("Erro ao finalizar a pendencia., " + e.getMessage());
                    DocumentosController.rollback(conexaoTransactionLocal);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null,
                        "Falha ao pendurar a factura",
                        "FALHA",
                        JOptionPane.ERROR_MESSAGE);

                e.printStackTrace();
                DocumentosController.rollback(conexaoTransactionLocal);
            }
        } else {
            System.out.println("NAO EXISTE ITENS NA TABELA.");
            JOptionPane.showMessageDialog(null, "Adiciona itens na tabela");
            DocumentosController.rollback(conexaoTransactionLocal);
        }

        conexaoTransactionLocal.close();

    }

    private void procedimentoCharFacturasPendentes() {

        if (table.getRowCount() > 1) {
            int opcao = JOptionPane.showConfirmDialog(null, "Existem itens na tabela deseja pendurar?");

            if (opcao == JOptionPane.YES_OPTION) {
                procedimentoPenderFactura();
            }

        } else {
            MetodosUtil.esvaziar_tabela(VendaModernaVisao.table);
            inserirLinhaEmBranco();
            configurarQTDTabela();
        }

        new FacturasPendentesVisao(null, rootPaneCheckingEnabled, conexao).setVisible(true);
    }

    private void procedimento_busca() {
        String referencia = txtBuscaRef.getText().trim();

        if (referencia.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Informe a referência da factura proforma.",
                    "AVISO",
                    JOptionPane.WARNING_MESSAGE
            );
            txtBuscaRef.requestFocus();
            return;
        }

        PreparedStatement psVenda = null;
        PreparedStatement psItens = null;
        ResultSet rsVenda = null;
        ResultSet rsItens = null;

        try {
            //====================================================
            // 1. BUSCAR CABEÇALHO DA PRÓ-FORMA
            //====================================================
            String sqlVenda
                    = "SELECT "
                    + "v.codigo, "
                    + "v.cod_fact, "
                    + "v.total_venda, "
                    + "v.referencia, "
                    + "v.nome_cliente, "
                    + "v.dataVencimento, "
                    + "v.codigo_cliente, "
                    + "v.fk_documento, "
                    + "v.idArmazemFK, "
                    + "v.fk_cambio "
                    + "FROM tb_venda v "
                    + "WHERE v.cod_fact = ? "
                    + "AND v.fk_documento = ?";

            psVenda = conexao.getConnection().prepareStatement(sqlVenda);
            psVenda.setString(1, referencia);
            psVenda.setInt(2, DVML.DOC_FACTURA_PROFORMA_PP);

            rsVenda = psVenda.executeQuery();

            if (!rsVenda.next()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Factura proforma não encontrada.",
                        "AVISO",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            int codigoVenda = rsVenda.getInt("codigo");

            //====================================================
            // 2. PREENCHER DADOS GERAIS
            //====================================================
            txtBuscaRef.setText(
                    rsVenda.getString("referencia")
            );

            cmbCliente.setSelectedItem(
                    rsVenda.getString("nome_cliente")
            );

            // Se tiver combobox de cliente
            try {
                TbCliente cliente
                        = (TbCliente) clientesController.findById(
                                rsVenda.getInt("codigo_cliente")
                        );

                if (cliente != null) {
                    cmbCliente.setSelectedItem(
                            cliente.getNome()
                    );
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            Date dataVencimento = rsVenda.getDate("dataVencimento");

            if (dataVencimento != null) {
                dc_data_vencimento.setDate(dataVencimento);
            } else {
                dc_data_vencimento.setDate(null);
            }

            //====================================================
            // 3. LIMPAR TABELA
            //====================================================
            DefaultTableModel modelo
                    = (DefaultTableModel) table.getModel();

            modelo.setRowCount(0);

            //====================================================
            // 4. BUSCAR ITENS DA PRÓ-FORMA
            //====================================================
            String sqlItens
                    = "SELECT "
                    + "i.codigo, "
                    + "i.quantidade, "
                    + "i.desconto, "
                    + "i.valor_iva, "
                    + "i.valor_retencao, "
                    + "i.total, "
                    + "i.designacao_item, "
                    + "p.codigo AS codigo_produto, "
                    + "u.abreviacao AS unidade, "
                    + "pr.preco_venda "
                    + "FROM tb_item_venda i "
                    + "INNER JOIN tb_produto p "
                    + "ON p.codigo = i.codigo_produto "
                    + "INNER JOIN unidade u "
                    + "ON u.pk_unidade = p.cod_unidade "
                    + "INNER JOIN tb_preco pr "
                    + "ON pr.pk_preco = i.fk_preco "
                    + "WHERE i.codigo_venda = ? "
                    + "ORDER BY i.codigo";

            psItens = conexao.getConnection().prepareStatement(sqlItens);
            psItens.setInt(1, codigoVenda);

            rsItens = psItens.executeQuery();

            while (rsItens.next()) {

                BigDecimal preco = rsItens.getBigDecimal("preco_venda");

                double quantidade = rsItens.getDouble("quantidade");
                double desconto = rsItens.getDouble("desconto");
                double valorRetencao = rsItens.getDouble("valor_retencao");

                BigDecimal qtd = BigDecimal.valueOf(quantidade);

                BigDecimal totalIliquido = preco.multiply(qtd);

                double totalRetencao = MetodosUtil.getValorComRetencao(
                        quantidade,
                        valorRetencao,
                        preco.doubleValue(),
                        desconto
                );

                modelo.addRow(new Object[]{
                    rsItens.getInt("codigo_produto"), // 0
                    rsItens.getString("designacao_item"), // 1
                    rsItens.getString("unidade"), // 2

                    CfMethods.formatarComoMoeda(preco), // 3

                    quantidade, // 4
                    desconto, // 5
                    rsItens.getDouble("valor_iva"), // 6
                    valorRetencao, // 7

                    CfMethods.formatarComoMoeda(
                    totalRetencao
                    ), // 8

                    CfMethods.formatarComoMoeda(
                    totalIliquido
                    ), // 9

                    CfMethods.formatarComoMoeda(
                    rsItens.getBigDecimal("total")
                    ) // 10
                });
            }

            txtTotalPagar.setText(
                    CfMethods.formatarComoMoeda(
                            rsVenda.getBigDecimal("total_venda")
                    )
            );

            setTotalRetencao();
            calculaTotalIVA();
            setTotalPagar();
            setTotalPagarGeral();
            valor_por_extenco();

            JOptionPane.showMessageDialog(
                    this,
                    "factura proforma carregada com sucesso."
            );
        } catch (Exception e) {
            e.printStackTrace();

            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao carregar factura proforma:\n"
                    + e.getMessage(),
                    "ERRO",
                    JOptionPane.ERROR_MESSAGE
            );
        } finally {
            try {
                if (rsItens != null) {
                    rsItens.close();
                }

                if (psItens != null) {
                    psItens.close();
                }

                if (rsVenda != null) {
                    rsVenda.close();
                }

                if (psVenda != null) {
                    psVenda.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void setTotalPagarGeral() {

        BigDecimal totalPagar = BigDecimal.valueOf(
                CfMethods.parseMoedaFormatada(txtTotalPagar.getText()));

        BigDecimal totalRetencao = BigDecimal.valueOf(
                CfMethods.parseMoedaFormatada(txtTotalPagarRetencao.getText()));

        BigDecimal totalGeral;

        if (getIdDocumento() == DVML.DOC_FACTURA_PROFORMA_PP
                || getIdDocumento() == DOC_FACTURA_FT) {

            totalGeral = totalPagar.subtract(totalRetencao);

        } else if (getIdDocumento() == DOC_FACTURA_RECIBO_FR) {

            totalGeral = totalPagar;

        } else {

            totalGeral = totalPagar;
        }

        txtTotalPagarGeral.setText(
                CfMethods.formatarComoMoeda(totalGeral));
    }

    public static void alterar_status_botao_venda() {

        if (!caixasController.existeCaixas()) {
            btn_abertura_dia_venda.setEnabled(true);
            btn_fecho_dia_venda.setEnabled(false);
        } else if (caixasController.existe_abertura() && caixasController.existe_fecho()) {
            btn_abertura_dia_venda.setEnabled(true);
            btn_fecho_dia_venda.setEnabled(false);
        } else {
            btn_abertura_dia_venda.setEnabled(false);
            btn_fecho_dia_venda.setEnabled(true);
        }
    }

    private static void procedimentoActualizarStausConsulta() {
        for (String doc : consultaDoc) {
            vendasController.actualiarStatusConsulta(doc);
        }
        consultaDoc.clear();
    }

//    private String folhaEscolhida = null; // null = utilizador não clicou
// static: prioridade ao clique; senão, padrão do banco
    public static String resolverFolha(String escolhida, String padraoBanco) {
        return (escolhida != null && !escolhida.trim().isEmpty()) ? escolhida : padraoBanco;
    }

    private void setFolhaImpressora(String folha) {
        ck_simplificada.setSelected(false);
        ck_simplificada_A7.setSelected(false);
        ck_A4.setSelected(false);
        ck_Duplicada.setSelected(false);
        ck_S_A6.setSelected(false);
        ck_ComVirgula.setSelected(false);
        ck_simplificada_O.setSelected(false);
        ck_simplificada_O_S.setSelected(false);

        switch (folha == null ? "A4" : folha.toUpperCase()) {
            case "A6":
                ck_simplificada.setSelected(true);
                break;
            case "A6_O":
                ck_simplificada_O.setSelected(true);
                break;
            case "S_A6_O":
                ck_simplificada_O_S.setSelected(true);
                break;
            case "A7":
                ck_simplificada_A7.setSelected(true);
                break;
            case "A5":
                ck_Duplicada.setSelected(true);
                break;
            case "S_A6":
                ck_S_A6.setSelected(true);
                break;
            case "A6V":
                ck_ComVirgula.setSelected(true);
                break;
            default:
                ck_A4.setSelected(true);
                break;
        }
        actualizar_abreviacao(); // calcula a abreviação conforme o documento
    }

    // ---------------------------------------------------------------------
// 5) SELECAO NA JTable: o teu atualizarPainelLotesDaLinhaSelecionada()
//    (nao o vi). Troca o que la esta para encher o painel por este esquema.
//    Indices das colunas segundo a captura (0 = Codigo, 1 = Descricao,
//    4 = Quantidade): ajusta se os teus forem outros.
// ---------------------------------------------------------------------
    private void atualizarPainelLotesDaLinhaSelecionada() {
        if (painelLotes == null) {
            return;
        }
        int linha = table.getSelectedRow();
        if (linha < 0) {
            painelLotes.limpar();
            return;
        }
        try {
            int codigo = Integer.parseInt(String.valueOf(table.getValueAt(linha, 0)));
            String descricao = String.valueOf(table.getValueAt(linha, 1));
            double qtd = Double.parseDouble(String.valueOf(table.getValueAt(linha, 4)));

            // sem janela: so seleccionar uma linha nao deve interromper o balcao
            actualizarPainelLotes(codigo, descricao, qtd);

        } catch (SQLException | NumberFormatException e) {
            painelLotes.limpar();
            e.printStackTrace();
        }
    }

    private void atualizarPainelLotesDaLinhaSelecionada2() {

        int linhaView = table.getSelectedRow();

        // Nenhuma linha selecionada
        if (linhaView < 0) {
            if (painelLotes != null) {
                painelLotes.limpar();
            }
            return;
        }

        try {
            // Converter o índice visual para o índice do modelo
            int linha = table.convertRowIndexToModel(linhaView);

            DefaultTableModel modelo
                    = (DefaultTableModel) table.getModel();

            Object codigoObj = modelo.getValueAt(linha, 0);
            Object quantidadeObj = modelo.getValueAt(linha, 4);

            // Ignorar linhas vazias
            if (codigoObj == null
                    || codigoObj.toString().trim().isEmpty()
                    || quantidadeObj == null
                    || quantidadeObj.toString().trim().isEmpty()) {

                if (painelLotes != null) {
                    painelLotes.limpar();
                }
                return;
            }

            int codigoProduto = Integer.parseInt(
                    codigoObj.toString().trim()
            );

            double quantidade = Double.parseDouble(
                    quantidadeObj.toString().trim().replace(",", ".")
            );

            // Verificar se o produto tem lotes registados
            if (!loteController.existeLotes(codigoProduto)) {
                if (painelLotes != null) {
                    painelLotes.limpar();
                }
                return;
            }

            int armazemAtual = getCodigoArmazem();

            // Verificar se existem lotes disponíveis neste armazém
            if (!loteController.existeLotesDisponiveis(
                    codigoProduto, armazemAtual)) {

                if (painelLotes != null) {
                    painelLotes.limpar();
                }
                return;
            }

            // Prever os lotes para a quantidade da linha selecionada
            List<LoteController.LoteFEFO> lotesFEFO
                    = loteController.preverBaixaFEFO(
                            codigoProduto,
                            armazemAtual,
                            quantidade
                    );

            if (lotesFEFO == null || lotesFEFO.isEmpty()) {
                if (painelLotes != null) {
                    painelLotes.limpar();
                }
                return;
            }

            // Obter a descrição do produto selecionado
            String descricao = String.valueOf(
                    modelo.getValueAt(linha, 1)
            );

            // Converter os lotes para o formato do painel
            List<PainelLotesVenda.Linha> linhasPainel
                    = new java.util.ArrayList<>();

            for (LoteController.LoteFEFO item : lotesFEFO) {

                entity.Lote lote = item.getLote();

                linhasPainel.add(
                        new PainelLotesVenda.Linha(
                                lote.getNumeroLote(),
                                lote.getDataValidade(),
                                lote.getQtd(),
                                item.getQuantidadeRetirar()
                        )
                );
            }

            // Atualizar o painel lateral
            if (painelLotes != null) {
                painelLotes.mostrar(
                        descricao,
                        quantidade,
                        linhasPainel
                );
            }

        } catch (IllegalStateException e) {

            if (painelLotes != null) {
                painelLotes.mostrarAviso(
                        "Produto selecionado",
                        e.getMessage()
                );
            }

        } catch (SQLException | NumberFormatException e) {

            if (painelLotes != null) {
                painelLotes.limpar();
            }

            e.printStackTrace();
        }
    }

    // ---------------------------------------------------------------------
// 3) PESQUISA: substitui TODO o teu atualizarPainelLotesProdutoPesquisado()
//    por este (mesma estrutura; a logica dos lotes passou para o metodo unico)
// ---------------------------------------------------------------------
    private void atualizarPainelLotesProdutoPesquisado3() {

        if (painelLotes == null) {
            return;
        }

        if (cmbProduto.getSelectedItem() == null) {
            painelLotes.limpar();
            return;
        }

        try {
            int codigoProdutoLocal = getCodigoProduto();
            String descricao_produto = getDescricao_Produto();
            String textoQtd = txtQuatindade.getText().trim();

            if (codigoProdutoLocal <= 0 || textoQtd.isEmpty()) {
                painelLotes.limpar();
                return;
            }

            double qtd = Double.parseDouble(textoQtd.replace(",", "."));
            if (qtd <= 0) {
                painelLotes.limpar();
                return;
            }

            // aqui nao ha janela: so mostra no painel
            actualizarPainelLotes(codigoProdutoLocal, descricao_produto, qtd);

        } catch (SQLException | NumberFormatException e) {
            painelLotes.limpar();
            e.printStackTrace();
        }
    }

    private void atualizarPainelLotesProdutoPesquisado2() {

        if (painelLotes == null) {
            return;
        }

        Object selecionado = cmbProduto.getSelectedItem();

        if (selecionado == null) {
            painelLotes.limpar();
            return;
        }

        try {
            // Obter os dados do produto selecionado na pesquisa
            int codigoProdutoLocal = getCodigoProduto();
            String descricao_produto = getDescricao_Produto();

            String textoQtd = txtQuatindade.getText().trim();

            if (codigoProdutoLocal <= 0 || textoQtd.isEmpty()) {
                painelLotes.limpar();
                return;
            }

            double qtd = Double.parseDouble(
                    textoQtd.replace(",", ".")
            );

            if (qtd <= 0) {
                painelLotes.limpar();
                return;
            }

            int armazemAtual = getCodigoArmazem();

            // Verificar se o produto tem lotes registados
            if (!loteController.existeLotes(codigoProdutoLocal)) {
                painelLotes.limpar();
                return;
            }

            // Verificar se existem lotes disponíveis no armazém
            if (!loteController.existeLotesDisponiveis(
                    codigoProdutoLocal, armazemAtual)) {

                painelLotes.mostrarAviso(
                        descricao_produto,
                        "Não existem lotes disponíveis neste armazém."
                );
                return;
            }

            // Consultar todos os lotes necessários segundo o FEFO
            List<LoteController.LoteFEFO> lotesFEFO
                    = loteController.preverBaixaFEFO(
                            codigoProdutoLocal,
                            armazemAtual,
                            qtd
                    );

            if (lotesFEFO == null || lotesFEFO.isEmpty()) {
                painelLotes.limpar();
                return;
            }

            // Converter cada lote para uma linha do painel
            List<PainelLotesVenda.Linha> linhasPainel
                    = new java.util.ArrayList<>();

            for (LoteController.LoteFEFO item : lotesFEFO) {

                entity.Lote lote = item.getLote();

                linhasPainel.add(
                        new PainelLotesVenda.Linha(
                                lote.getNumeroLote(),
                                lote.getDataValidade(),
                                lote.getQtd(),
                                item.getQuantidadeRetirar()
                        )
                );
            }

            // Mostrar todas as linhas devolvidas pelo FEFO
            painelLotes.mostrar(
                    descricao_produto,
                    qtd,
                    linhasPainel
            );

        } catch (IllegalStateException e) {

            painelLotes.mostrarAviso(
                    selecionado.toString(),
                    e.getMessage()
            );

        } catch (SQLException | NumberFormatException e) {

            painelLotes.limpar();
            e.printStackTrace();
        }
    }

    private void atualizarPainelLotesProdutoPesquisado1() {

        if (painelLotes == null) {
            return;
        }

        Object selecionado = cmbProduto.getSelectedItem();

        if (selecionado == null) {
            painelLotes.limpar();
            return;
        }

        try {
            int codigoProduto = getCodigoProduto();

            double qtd = Double.parseDouble(
                    txtQuatindade.getText().trim().replace(",", ".")
            );

            if (codigoProduto <= 0 || qtd <= 0) {
                painelLotes.limpar();
                return;
            }

            int armazemAtual = getCodigoArmazem();

            // Produto sem lotes registados
            if (!loteController.existeLotes(codigoProduto)) {
                painelLotes.limpar();
                return;
            }

            // Produto com lotes, mas sem lotes vendáveis
            if (!loteController.existeLotesDisponiveis(
                    codigoProduto, armazemAtual)) {

                painelLotes.mostrarAviso(
                        selecionado.toString(),
                        "Não existem lotes disponíveis neste armazém."
                );
                return;
            }

            List<LoteController.LoteFEFO> lotesFEFO
                    = loteController.preverBaixaFEFO(
                            codigoProduto,
                            armazemAtual,
                            qtd
                    );

            List<PainelLotesVenda.Linha> linhasPainel
                    = new java.util.ArrayList<>();

            for (LoteController.LoteFEFO item : lotesFEFO) {

                entity.Lote lote = item.getLote();

                linhasPainel.add(
                        new PainelLotesVenda.Linha(
                                lote.getNumeroLote(),
                                lote.getDataValidade(),
                                lote.getQtd(),
                                item.getQuantidadeRetirar()
                        )
                );
            }

            painelLotes.mostrar(
                    selecionado.toString(),
                    qtd,
                    linhasPainel
            );

        } catch (IllegalStateException e) {

            painelLotes.mostrarAviso(
                    selecionado.toString(),
                    e.getMessage()
            );

        } catch (java.sql.SQLException | NumberFormatException e) {

            painelLotes.limpar();
            e.printStackTrace();
        }
    }

    private void mostrarLotesDoProdutoSelecionado(double qtd) {

        if (painelLotes == null) {
            return;
        }

        try {
            int codigoProdutoLocal = getCodigoProduto();
            String descricao = getDescricao_Produto();

            if (codigoProdutoLocal <= 0) {
                painelLotes.limpar();
                return;
            }

            // Produto sem lotes registados
            if (!loteController.existeLotes(codigoProdutoLocal)) {
                painelLotes.limpar();
                return;
            }

            int armazemAtual = getCodigoArmazem();

            // Produto com lotes, mas sem lotes disponíveis
            if (!loteController.existeLotesDisponiveis(
                    codigoProdutoLocal, armazemAtual)) {

                painelLotes.mostrarAviso(
                        descricao,
                        "Não existem lotes disponíveis neste armazém."
                );
                return;
            }

            // Se a quantidade ainda não foi preenchida,
            // não é possível calcular a previsão de retirada.
            if (qtd <= 0) {
                painelLotes.mostrarAviso(
                        descricao,
                        "Introduza uma quantidade para prever os lotes."
                );
                return;
            }

            List<LoteController.LoteFEFO> lotesFEFO
                    = loteController.preverBaixaFEFO(
                            codigoProdutoLocal,
                            armazemAtual,
                            qtd
                    );

            List<PainelLotesVenda.Linha> linhasPainel
                    = new java.util.ArrayList<>();

            for (LoteController.LoteFEFO item : lotesFEFO) {

                entity.Lote lote = item.getLote();

                linhasPainel.add(
                        new PainelLotesVenda.Linha(
                                lote.getNumeroLote(),
                                lote.getDataValidade(),
                                lote.getQtd(),
                                item.getQuantidadeRetirar()
                        )
                );
            }

            painelLotes.mostrar(
                    descricao,
                    qtd,
                    linhasPainel
            );

        } catch (IllegalStateException e) {

            painelLotes.mostrarAviso(
                    getDescricao_Produto(),
                    e.getMessage()
            );

        } catch (SQLException e) {

            painelLotes.limpar();
            e.printStackTrace();
        }
    }

    private void atualizarPainelLotesDepoisDaPesquisa() {

        try {
            int codigoProduto = getCodigoProduto();

            if (codigoProduto <= 0) {
                if (painelLotes != null) {
                    painelLotes.limpar();
                }
                return;
            }

            // Utiliza a quantidade atual do formulário.
            // Substitui txtQuantidade pelo nome real do teu campo.
            String textoQtd = txtQuatindade.getText().trim();

            if (textoQtd.isEmpty()) {
                if (painelLotes != null) {
                    painelLotes.limpar();
                }
                return;
            }

            double qtd = Double.parseDouble(
                    textoQtd.replace(",", ".")
            );

            mostrarLotesDoProdutoSelecionado(qtd);

        } catch (NumberFormatException e) {
            if (painelLotes != null) {
                painelLotes.limpar();
            }
        }
    }

    private void aplicarFolha() {
        setFolhaImpressora(resolverFolha(folhaEscolhida, dadosInstituicao.getImpressora()));
    }

    private void ligar(javax.swing.JCheckBox ck, String folha) {
        ck.addActionListener(e -> {
            folhaEscolhida = ck.isSelected() ? folha : null;
            aplicarFolha();
        });
    }

    private void configurarEscolhaFolha() {
        ligar(ck_S_A6, "S_A6");
        ligar(ck_ComVirgula, "A6V");
        ligar(ck_simplificada_A7, "A7");
        ligar(ck_simplificada_O_S, "S_A6_O");
        ligar(ck_simplificada_O, "A6_O");
        ligar(ck_simplificada, "A6");
        ligar(ck_Duplicada, "A5");
        ligar(ck_A4, "A4");
    }

// método static que o finalizar venda vai chamar
    public static void voltarFolhaPadrao() {
        if (instancia != null) {
            instancia.folhaEscolhida = null;
            instancia.aplicarFolha(); // volta ao padrão do banco
        }
    }


    // ---------------------------------------------------------------------
// 2) NA CLASSE DO ECRA DE VENDAS: o metodo UNICO que enche o painel.
//    Usa-o em todos os sitios (pesquisa, adicionar, selecao na JTable).
//    E static porque o adicionar_produto e static.
// ---------------------------------------------------------------------
    /**
     * Actualiza o painel de lotes para (produto, quantidade).
     *
     * @return null se ha stock suficiente (ou o produto nao tem lotes); senao o
     * texto do erro (o painel ja o mostra a vermelho; quem quiser bloquear
     * mostra tambem uma janela)
     */
//    private static String actualizarPainelLotes(int codProduto, String descricao, double qtd) throws SQLException {
//
//        // produto sem lotes registados: nada a mostrar
//        if (!loteController.existeLotes(codProduto)) {
//            painelLotes.limpar();
//            return null;
//        }
//        int armazem = getCodigoArmazem();
//
//        // todos os lotes com stock, por ordem FEFO
//        List<entity.Lote> disponiveis = loteController.listarDisponiveisFEFO(codProduto, armazem);
//        if (disponiveis.isEmpty()) {
//            String msg = "Este produto tem lotes registados, mas não possui lotes disponíveis, "
//                    + "activos e dentro da validade no armazém selecionado.";
//            painelLotes.mostrarAviso(descricao, msg);
//            return msg;
//        }
//
//        // quanto sai de cada lote para a quantidade dada (por id do lote)
//        java.util.Map<Integer, Double> aRetirar = new java.util.HashMap<>();
//        String erro = null;
//        try {
//            for (LoteController.LoteFEFO item : loteController.preverBaixaFEFO(codProduto, armazem, qtd)) {
//                aRetirar.put(item.getLote().getId(), (double) item.getQuantidadeRetirar());
//            }
//        } catch (IllegalStateException e) {
//            erro = e.getMessage();
//        }
//
//        List<PainelLotesVenda.Linha> linhas = new java.util.ArrayList<>();
//        for (entity.Lote l : disponiveis) {
//            linhas.add(new PainelLotesVenda.Linha(
//                    l.getNumeroLote(),
//                    l.getDataValidade(),
//                    l.getQtd(),
//                    aRetirar.getOrDefault(l.getId(), 0.0)));
//        }
//
//        if (erro != null) {
//            painelLotes.mostrarAviso(descricao, erro, linhas);
//            return erro;
//        }
//        painelLotes.mostrar(descricao, qtd, linhas);
//        return null;
//    }
// ---------------------------------------------------------------------
// 3) PESQUISA: substitui TODO o teu atualizarPainelLotesProdutoPesquisado()
//    por este, e acrescenta o metodo quantidadeDoProdutoNaVenda().
//
//    REGRA: a quantidade do plano e
//      - a que JA ESTA NA VENDA, se o produto ja tem linha na JTable
//        (e o que vai sair de facto: ex. 17 -> 14 + 3);
//      - senao, a que o operador esta a escrever no campo Qtd.
//    Assim o painel nunca volta a "1" so porque o campo Qtd foi reposto a 1
//    depois de adicionar, seja qual for o evento que dispare a pesquisa.
// ---------------------------------------------------------------------
//    private void atualizarPainelLotesProdutoPesquisado() {
//
//        if (painelLotes == null) {
//            return;
//        }
//
//        if (cmbProduto.getSelectedItem() == null) {
//            painelLotes.limpar();
//            return;
//        }
//
//        try {
//            int codigoProdutoLocal = getCodigoProduto();
//            String descricao_produto = getDescricao_Produto();
//
//            if (codigoProdutoLocal <= 0) {
//                painelLotes.limpar();
//                return;
//            }
//
//            // 1.o: o produto ja esta na venda? entao o plano e o da venda
//            double qtd = quantidadeDoProdutoNaVenda(codigoProdutoLocal);
//
//            // 2.o: senao, a quantidade que o operador esta a escrever
//            if (qtd <= 0) {
//                String textoQtd = txtQuatindade.getText().trim();
//                if (textoQtd.isEmpty()) {
//                    painelLotes.limpar();
//                    return;
//                }
//                qtd = Double.parseDouble(textoQtd.replace(",", "."));
//                if (qtd <= 0) {
//                    painelLotes.limpar();
//                    return;
//                }
//            }
//
//            // aqui nao ha janela: so mostra no painel
//            actualizarPainelLotes(codigoProdutoLocal, descricao_produto, qtd);
//
//        } catch (SQLException | NumberFormatException e) {
//            painelLotes.limpar();
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * Quantidade total deste produto que ja esta na JTable da venda (soma das
//     * linhas; 0 se nao esta). Ignora linhas vazias ou em edicao.
//     * Colunas: 0 = Codigo, 4 = Quantidade.
//     */
//    private double quantidadeDoProdutoNaVenda(int codProduto) {
//        double total = 0;
//        for (int i = 0; i < table.getRowCount(); i++) {
//            try {
//                int codigo = Integer.parseInt(String.valueOf(table.getValueAt(i, 0)).trim());
//                if (codigo == codProduto) {
//                    total += Double.parseDouble(String.valueOf(table.getValueAt(i, 4)).trim().replace(",", "."));
//                }
//            } catch (NumberFormatException e) {
//                // linha vazia ou nao numerica: ignora
//            }
//        }
//        return total;
//    }
}
