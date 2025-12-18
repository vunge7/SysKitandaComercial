/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package visao;

import comercial.controller.*;
//import hotel.controller.ExtratoContaClienteController;
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
import entity.TbLugares;
import entity.TbMesas;
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
import java.awt.Dimension;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import static kitanda.util.CfConstantes.YYYYMMDD_HHMMSS;
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

/**
 *
 * @author marti
 */
public class FormVendaResponsivaVisaoTop extends javax.swing.JFrame
{

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

    private static List<Vector<TbPreco>> listaPrecoTemp = new ArrayList<>();

    private String doc = "";

    public FormVendaResponsivaVisaoTop( int cod_usuario, BDConexao conexao ) throws SQLException
    {
        initComponents();
//        procedimento_codBarra__jtable();

        this.setExtendedState( JFrame.MAXIMIZED_BOTH ); // abre maximizado

        btnSemFormaPagamento.setVisible( false );
        cmbMoeda.setVisible( false );
        rbArmazem.setVisible( false );
        rbArmazem1.setVisible( false );
        rbMostrar.setVisible( false );
        jlStockNegativo.setVisible( false );
        rbTranstorno.setVisible( false );
        txtLocal.setVisible( false );
        lb_proximo_documento.setVisible( false );

        //pegarResolucao();
        FormVendaResponsivaVisaoTop.conexao = conexao;

        /**
         * INSTANCIAS DOS CONTROLLER COMERCIAL
         */
        vendasController = new VendasController( FormVendaResponsivaVisaoTop.conexao );
        itemVendasController = new ItemVendasController( FormVendaResponsivaVisaoTop.conexao );
        mesasController = new MesasController( FormVendaResponsivaVisaoTop.conexao );
        lugaresController = new LugaresController( FormVendaResponsivaVisaoTop.conexao );
        produtosController = new ProdutosController( FormVendaResponsivaVisaoTop.conexao );
        stocksController = new StoksController( FormVendaResponsivaVisaoTop.conexao );
        precosController = new PrecosController( FormVendaResponsivaVisaoTop.conexao );
        tipoProdutoController = new TipoProdutosController( FormVendaResponsivaVisaoTop.conexao );
        familiaController = new FamiliasController( FormVendaResponsivaVisaoTop.conexao );
        armazensController = new ArmazensController( FormVendaResponsivaVisaoTop.conexao );
        localController = new LocalController( FormVendaResponsivaVisaoTop.conexao );
        unidadesController = new UnidadesController( FormVendaResponsivaVisaoTop.conexao );
        anoEconomicoController = new AnoEconomicoController( FormVendaResponsivaVisaoTop.conexao );
        clientesController = new ClientesController( FormVendaResponsivaVisaoTop.conexao );
        documentosController = new DocumentosController( FormVendaResponsivaVisaoTop.conexao );
        moedasController = new MoedasController( FormVendaResponsivaVisaoTop.conexao );
        descontosController = new DescontosController( FormVendaResponsivaVisaoTop.conexao );
        usuariosController = new UsuariosController( FormVendaResponsivaVisaoTop.conexao );
        dadosInstituicaoController = new DadosInstituicaoController( FormVendaResponsivaVisaoTop.conexao );
        produtosImpostoController = new ProdutosImpostoController( FormVendaResponsivaVisaoTop.conexao );
        produtosIsentoController = new ProdutosIsentoController( FormVendaResponsivaVisaoTop.conexao );
        caixasController = new CaixasController( FormVendaResponsivaVisaoTop.conexao );
        formaPagamentoController = new FormaPagamentoController( FormVendaResponsivaVisaoTop.conexao );
        armazensAccessoController = new ArmazensAccessoController( FormVendaResponsivaVisaoTop.conexao );
        cambiosController = new CambiosController( FormVendaResponsivaVisaoTop.conexao );
        formaPagamentoItemController = new FormaPagamentoItemController( FormVendaResponsivaVisaoTop.conexao );
        servicosRetencaoController = new ServicosRetencaoController( FormVendaResponsivaVisaoTop.conexao );
        contaController = new ContaController( FormVendaResponsivaVisaoTop.conexao );
        movimentacaoController = new MovimentacaoController( conexao.getConnection() );
        dadosInstituicao = ( TbDadosInstituicao ) dadosInstituicaoController.findById( 1 );
        configuracaoMesComecoController = new ConfiguracaoMesComecoController( conexao.getConnectionAtiva() );
        pagamentoMensalidadeController = new PagamentoMensalidadeController( conexao.getConnectionAtiva() );
        mesRhController = new MesRhController( conexao.getConnectionAtiva() );
        cmc = new ContaMovimentosController( conexao );
        txtQuatindade.setText( "1" );
//        txtQuatindade.setDocument( new PermitirNumeros() );

        this.cod_usuario = cod_usuario;
//        lbPreco7.setVisible( false );
        jlStockNegativo.setVisible( false );
        rbTranstorno.setVisible( false );
        rbMostrar.setVisible( false );
        rbArmazem1.setVisible( false );
        rbArmazem.setVisible( false );
        txtTotal_AOA_Retencao.setVisible( false );
        try
        {

            init();

        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
        //pega_ultima_contagem();

        KeyboardFocusManager.getCurrentKeyboardFocusManager()
                .addKeyEventDispatcher( new KeyEventDispatcher()
                {
                    @Override
                    public boolean dispatchKeyEvent( KeyEvent e )
                    {
                        // if ( e.getID() == e.KEY_RELEASED && e.getKeyCode() == KeyEvent.VK_TAB )
                        if ( e.getID() == e.KEY_RELEASED && e.getKeyCode() == KeyEvent.VK_TAB )
                        {
//                            txtValorEntregue.requestFocus();
                            return true;

                        }
                        return false;
                    }
                } );
        //new BuscaProdutoVisao( FormVendaResponsivaVisaoTop.this, rootPaneCheckingEnabled, getCodigoArmazem(), DVML.JANELA_VENDA ).setVisible(true);
//        MetodosUtil.FUNCAO_F1( this, rootPaneCheckingEnabled, getCodigoArmazem(), DVML.JANELA_VENDA);

        // No construtor ou método de inicialização do formulário
        getRootPane().getInputMap( JComponent.WHEN_IN_FOCUSED_WINDOW )
                .put( KeyStroke.getKeyStroke( "F4" ), "abrirBuscaProduto" );

        getRootPane().getActionMap().put( "abrirBuscaProduto", new AbstractAction()
        {
            @Override
            public void actionPerformed( ActionEvent e )
            {
                try
                {
                    if ( validar() )
                    {
                        new BuscaProdutoVisao(
                                FormVendaResponsivaVisaoTop.this,
                                rootPaneCheckingEnabled,
                                getCodigoArmazem(),
                                DVML.JANELA_VENDA,
                                BDConexao.getInstancia()
                        ).setVisible( true );
                    }
                }
                catch ( Exception ex )
                {
                    ex.printStackTrace();
                }
            }
        } );

        // No construtor ou método de inicialização do formulário
        getRootPane().getInputMap( JComponent.WHEN_IN_FOCUSED_WINDOW )
                .put( KeyStroke.getKeyStroke( "F5" ), "abrirFormaPagamento" );

        getRootPane().getActionMap().put( "abrirFormaPagamento", new AbstractAction()
        {
            @Override
            public void actionPerformed( ActionEvent e )
            {
                try
                {
                    procedimentoChamarFormaPagemnto();
                }
                catch ( Exception ex )
                {
                    ex.printStackTrace();
                }
            }
        } );

//        habilitarColunas();
        MetodosUtil.setArmazemByCampoConfigArmazem( cmbArmazem, conexao, cod_usuario );

        setWindowsListener();
//        btnSemFormaPagamento.setVisible( false );

        table.setRowHeight( 25 );
        inserir_uma_linha();

        procedimento_codBarra__jtable();

        configurarTabela();
//        configurarTabela( 3 );
//        configurarTabela( 4 );
//        initStockListener();
    }

    private void init()
    {
        // Dentro do construtor ou depois do initComponents():
        this.getRootPane().getInputMap( JComponent.WHEN_IN_FOCUSED_WINDOW )
                .put( KeyStroke.getKeyStroke( "F5" ), "abrirFormaPagamento" );

        this.getRootPane().getActionMap().put( "abrirFormaPagamento", new AbstractAction()
        {
            @Override
            public void actionPerformed( ActionEvent e )
            {
                abrirFormaPagamento(); // chama o método abaixo
            }
        } );

        cmbMoeda.setVisible( false );
        txtIniciaisCliente.addKeyListener( new TratarEventoTeclado() );

        txtCodigoProduto.setDocument( new PermitirNumeros() );
        txtCodigoBarra.setDocument( new PermitirNumeros() );
        lbValorPorExtenco.setText( "" );
        mostrar_nome();
        try
        {
            configurar_armazens();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }

        cmbSubFamilia.setModel( new DefaultComboBoxModel( tipoProdutoController.getVector() ) );
        cmbProduto.setModel( new DefaultComboBoxModel( produtosController.getVector() ) );
        cmbMoeda.setModel( new DefaultComboBoxModel( moedasController.getVector() ) );
        txtCodClientePesquisa.setDocument( new PermitirNumeros() );

        cmbMoeda.setSelectedIndex( 0 );
        cmbCliente.setModel( new DefaultComboBoxModel( clientesController.getVector() ) );
        cmbCliente.setSelectedItem( DVML._CLIENTE_CONSUMIDOR_FINAL );

//        cmbFamilia.setModel( new DefaultComboBoxModel( familiaController.getVector() ) );
        cmbTipoDocumento.setModel( new DefaultComboBoxModel( documentosController.getVector() ) );
        cmbAnoEconomico.setModel( new DefaultComboBoxModel( anoEconomicoController.getVector() ) );
        txtQuatindade.setText( "1" );
        txtQuatindade.requestFocus();
        dc_data_documento.setDate( new Date() );
        mostrar_ano_economico_serie();
        lb_proximo_documento.setText( "" );
        txtTotalPagar.setText( CfMethods.formatarComoMoeda( 0.0 ) );

        reset_valor_entregue();
        reset_desconto_global();

        setDocpadrao( dadosInstituicao.getDocpadrao() );
        setDesactivarvias( dadosInstituicao.getDesactivarvias() );
//        setEditarPrecos( dadosInstituicao.getEditarPreco() );
//        setActivarNegocio( dadosInstituicao.getNegocio() );
        setArmazem( dadosInstituicao.getConfigArmazens() );
        setTranstorno( dadosInstituicao.getTranstorno() );
        setActivarDescontoFinanceiro( dadosInstituicao.getDescontoFinanceiro() );
        setAnoEconomico( dadosInstituicao.getAnoEconomico() );
        setVizualisarStock( dadosInstituicao.getVizualisarStock() );
        int numero_copia = dadosInstituicao.getNumeroVias();
        spnCopia.setModel( CfMethodsSwing.criarSpinnerDoubleModel( 1, 3, numero_copia ) );
        empresa();
        setFolhaImpressora( dadosInstituicao.getImpressora() );

        actualizar_abreviacao();

    }// </editor-fold>   

    private void mostrar_ano_economico_serie()
    {
        anoEconomico = anoEconomicoController.getLastObject();

    }

    public FormVendaResponsivaVisaoTop( int cod_usuario, BDConexao conexao, String docPadraoPersonalizado ) throws SQLException
    {
        this( cod_usuario, conexao ); // chama o construtor original

        // Agora força o documento padrão personalizado
        setDocpadrao( docPadraoPersonalizado );

        if ( docPadraoPersonalizado.equalsIgnoreCase( "Factura-Proforma" ) )
        {
            cmbTipoDocumento.setEnabled( false );
        }
    }

    public JFrame getInstance()
    {
        return this;
    }

    public void keypressed( java.awt.event.KeyEvent evt )
    {

        if ( evt.getKeyCode() == KeyEvent.VK_ENTER )
        {

            txtQuatindade.requestFocus();

        }

    }

    public void keyTyped( KeyEvent evt )
    {

        if ( evt.getKeyCode() == KeyEvent.VK_ENTER )
        {

            dispose();
        }

    }

    class TratarEventoTeclado implements KeyListener
    {

        String prefixo = "";

        public void keyPressed( KeyEvent evt )
        {

            if ( evt.getKeyCode() != KeyEvent.VK_BACK_SPACE && evt.getKeyCode() != KeyEvent.VK_ENTER )
            {
                char key = evt.getKeyChar();

                try
                {
                    prefixo = txtIniciaisCliente.getText().trim() + key;
                    cmbCliente.setModel( new DefaultComboBoxModel( clientesController.getVectorByIinciais( prefixo ) ) );

                }
                catch ( Exception e )
                {
                    cmbCliente.setSelectedIndex( 0 );
                }

            }
            else if ( evt.getKeyCode() == KeyEvent.VK_BACK_SPACE )
            {
                try
                {
                    prefixo = prefixo.toString().trim().substring( 0, prefixo.length() - 1 );
                    cmbCliente.setModel( new DefaultComboBoxModel( clientesController.getVectorByIinciais( prefixo ) ) );
                }
                catch ( Exception e )
                {
                    cmbCliente.setSelectedIndex( 0 );
                }

            }
        }

        public void keyReleased( KeyEvent evt )
        {
        }

        public void keyTyped( KeyEvent evt )
        {
        }

    }

    private void setWindowsListener()
    {

        this.addWindowListener( new WindowAdapter()
        {
            @Override
            public void windowActivated( WindowEvent e )
            {
                mostrar_proximo_codigo_documento();

                try
                {
                    MetodosUtil.verificarCaixa( caixasController,
                            cod_usuario,
                            RootVisao.btn_abertura_dia_root,
                            RootVisao.btn_abertura_dia_root,
                            btnFormaPagamento, btnSemFormaPagamento );

                }
                catch ( Exception ex )
                {
                    System.err.println( "Não existe abertura para este usuário \n"
                            + ex.getMessage() );
                }

            }

        } );

    }

    private static void mostrar_proximo_codigo_documento()
    {

        try
        {
            BDConexao conexaoLocal = BDConexao.getInstancia();
            documentosController = new DocumentosController( conexaoLocal );
            anoEconomicoController = new AnoEconomicoController( conexaoLocal );
            vendasController = new VendasController( conexaoLocal );

            documento = ( Documento ) documentosController.findById( getIdDocumento( documentosController ) );
            anoEconomico = ( AnoEconomico ) anoEconomicoController.findById( getIdAnoEconomico( anoEconomicoController ) );
            doc_prox_cod = vendasController.getUltimaContagemByIdDocumentoAndAnoEconomico(
                    getIdDocumento( documentosController ), getIdAnoEconomico( anoEconomicoController ) ) + 1;
            prox_doc = documento.getAbreviacao();
            //FA Série / codigo
            prox_doc += " " + anoEconomico.getSerie() + "/" + doc_prox_cod;
            lb_proximo_documento.setText( "PRÓX.DOC. :" + prox_doc );

        }
        catch ( Exception e )
        {
            documento = null;
            lb_proximo_documento.setText( "" );

        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings( "unchecked" )
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        painelTopo = new javax.swing.JPanel();
        painelEsq = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        dc_data_vencimento = new com.toedter.calendar.JDateChooser();
        txtIniciaisCliente = new javax.swing.JTextField();
        lbCliente = new javax.swing.JLabel();
        lbCliente1 = new javax.swing.JLabel();
        txtNifClientePesquisa = new javax.swing.JTextField();
        txtCodClientePesquisa = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        lbClienteConsumidorFinal = new javax.swing.JLabel();
        txtNomeConsumidorFinal = new javax.swing.JTextField();
        cmbCliente = new javax.swing.JComboBox();
        jButton5 = new javax.swing.JButton();
        spnCopia = new javax.swing.JSpinner();
        rbTranstorno = new javax.swing.JRadioButton();
        rbMostrar = new javax.swing.JRadioButton();
        jlStockNegativo = new javax.swing.JLabel();
        cmbTipoDocumento = new javax.swing.JComboBox();
        lb_proximo_documento = new javax.swing.JLabel();
        lb_nome_usuario = new javax.swing.JLabel();
        cmbAnoEconomico = new javax.swing.JComboBox<>();
        rbArmazem1 = new javax.swing.JRadioButton();
        rbArmazem = new javax.swing.JRadioButton();
        cmbMoeda = new javax.swing.JComboBox();
        btnSemFormaPagamento = new javax.swing.JButton();
        lbClienteConsumidorFinal1 = new javax.swing.JLabel();
        txtTelClientePesquisa = new javax.swing.JTextField();
        painelDir = new javax.swing.JPanel();
        txtPreco = new javax.swing.JTextField();
        jButton4 = new javax.swing.JButton();
        txtQuantidadeStock = new javax.swing.JTextField();
        txtReferencia = new javax.swing.JTextField();
        txtQuatindade = new javax.swing.JTextField();
        lbQuantidade = new javax.swing.JLabel();
        btn_adicionar = new javax.swing.JButton();
        btn_remover = new javax.swing.JButton();
        ck_A4 = new javax.swing.JCheckBox();
        lbCodigoProduto = new javax.swing.JLabel();
        txtCodigoProduto = new javax.swing.JTextField();
        lbCodigoProduto1 = new javax.swing.JLabel();
        ck_A7 = new javax.swing.JCheckBox();
        ck_S_A6 = new javax.swing.JCheckBox();
        ck_ComVirgula = new javax.swing.JCheckBox();
        ck_simplificada_O_S = new javax.swing.JCheckBox();
        ck_simplificada_O = new javax.swing.JCheckBox();
        ck_simplificada = new javax.swing.JCheckBox();
        ck_Duplicada = new javax.swing.JCheckBox();
        btnFormaPagamento = new javax.swing.JButton();
        btnProcessar = new javax.swing.JButton();
        txtCodigoBarra = new javax.swing.JTextField();
        txtCodigoManual = new javax.swing.JTextField();
        cmbSubFamilia = new javax.swing.JComboBox();
        cmbProduto = new javax.swing.JComboBox();
        cmbArmazem = new javax.swing.JComboBox();
        lbQuantidadeStock = new javax.swing.JLabel();
        txtTotalPagar = new javax.swing.JTextField();
        lbCodigoProduto2 = new javax.swing.JLabel();
        btnCancelar = new javax.swing.JButton();
        lbCodigoProduto3 = new javax.swing.JLabel();
        lbCodigoProduto4 = new javax.swing.JLabel();
        lbValorPorExtenco = new javax.swing.JLabel();
        jlEmpresa = new javax.swing.JLabel();
        sp_desconto_financeiro = new javax.swing.JSpinner();
        lbDescontoFinanceiro = new javax.swing.JLabel();
        txtTotal_AOA_Retencao = new javax.swing.JLabel();
        txtLocal = new javax.swing.JTextField();
        dc_data_documento = new com.toedter.calendar.JDateChooser();
        painelTabela = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);

        painelEsq.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        jLabel9.setText("DATA VENCIMENTO");

        dc_data_vencimento.setEnabled(false);
        dc_data_vencimento.setFont(new java.awt.Font("Lucida Grande", 1, 12)); // NOI18N

        txtIniciaisCliente.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtIniciaisClienteActionPerformed(evt);
            }
        });

        lbCliente.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbCliente.setText("Iniciais Nome:");

        lbCliente1.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbCliente1.setText("Nome:");

        txtNifClientePesquisa.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtNifClientePesquisaActionPerformed(evt);
            }
        });

        txtCodClientePesquisa.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtCodClientePesquisaActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        jLabel2.setText("Cod.");

        lbClienteConsumidorFinal.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbClienteConsumidorFinal.setText("NIF:");

        txtNomeConsumidorFinal.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtNomeConsumidorFinalActionPerformed(evt);
            }
        });
        txtNomeConsumidorFinal.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtNomeConsumidorFinalKeyPressed(evt);
            }
        });

        cmbCliente.setBackground(new java.awt.Color(0, 255, 255));
        cmbCliente.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        cmbCliente.setForeground(new java.awt.Color(0, 0, 51));
        cmbCliente.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbCliente.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbClienteActionPerformed(evt);
            }
        });

        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/usuario.png"))); // NOI18N
        jButton5.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButton5ActionPerformed(evt);
            }
        });

        rbTranstorno.setSelected(true);
        rbTranstorno.setEnabled(false);
        rbTranstorno.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                rbTranstornoActionPerformed(evt);
            }
        });

        rbMostrar.setSelected(true);
        rbMostrar.setEnabled(false);
        rbMostrar.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                rbMostrarActionPerformed(evt);
            }
        });

        jlStockNegativo.setText("Stock Negativo");

        cmbTipoDocumento.setBackground(new java.awt.Color(0, 255, 255));
        cmbTipoDocumento.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 14)); // NOI18N
        cmbTipoDocumento.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbTipoDocumentoActionPerformed(evt);
            }
        });

        lb_proximo_documento.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        lb_proximo_documento.setText("PRÓX. DOC. : XX PP/A1");

        lb_nome_usuario.setFont(new java.awt.Font("Lucida Grande", 1, 12)); // NOI18N
        lb_nome_usuario.setForeground(new java.awt.Color(0, 102, 102));
        lb_nome_usuario.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lb_nome_usuario.setText("Usuario");

        cmbAnoEconomico.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        cmbAnoEconomico.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbAnoEconomico.setEnabled(false);
        cmbAnoEconomico.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbAnoEconomicoActionPerformed(evt);
            }
        });

        rbArmazem1.setEnabled(false);
        rbArmazem1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                rbArmazem1ActionPerformed(evt);
            }
        });

        rbArmazem.setSelected(true);
        rbArmazem.setEnabled(false);
        rbArmazem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                rbArmazemActionPerformed(evt);
            }
        });

        cmbMoeda.setBackground(new java.awt.Color(0, 255, 255));
        cmbMoeda.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 14)); // NOI18N
        cmbMoeda.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbMoedaActionPerformed(evt);
            }
        });

        btnSemFormaPagamento.setText("Finalizar");
        btnSemFormaPagamento.setEnabled(false);
        btnSemFormaPagamento.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnSemFormaPagamentoActionPerformed(evt);
            }
        });

        lbClienteConsumidorFinal1.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbClienteConsumidorFinal1.setText("TEL:");

        txtTelClientePesquisa.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtTelClientePesquisaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout painelEsqLayout = new javax.swing.GroupLayout(painelEsq);
        painelEsq.setLayout(painelEsqLayout);
        painelEsqLayout.setHorizontalGroup(
            painelEsqLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(painelEsqLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(painelEsqLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(painelEsqLayout.createSequentialGroup()
                        .addComponent(cmbAnoEconomico, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(spnCopia, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lb_nome_usuario, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(painelEsqLayout.createSequentialGroup()
                        .addComponent(lbCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel2))
                    .addGroup(painelEsqLayout.createSequentialGroup()
                        .addComponent(rbTranstorno)
                        .addGap(31, 31, 31)
                        .addComponent(jlStockNegativo, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(14, 14, 14)
                        .addComponent(rbMostrar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rbArmazem1)
                        .addGap(18, 18, 18)
                        .addComponent(rbArmazem)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lb_proximo_documento, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(40, 40, 40)
                        .addComponent(cmbMoeda, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSemFormaPagamento, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, painelEsqLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(painelEsqLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(painelEsqLayout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(lbClienteConsumidorFinal, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtNifClientePesquisa, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(painelEsqLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, painelEsqLayout.createSequentialGroup()
                                    .addComponent(cmbTipoDocumento, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(dc_data_vencimento, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, painelEsqLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(cmbCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 345, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(painelEsqLayout.createSequentialGroup()
                                        .addGroup(painelEsqLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addGroup(painelEsqLayout.createSequentialGroup()
                                                .addComponent(lbClienteConsumidorFinal1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(txtTelClientePesquisa, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(painelEsqLayout.createSequentialGroup()
                                                .addComponent(txtIniciaisCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(painelEsqLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                    .addComponent(txtNomeConsumidorFinal, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(lbCliente1, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(txtCodClientePesquisa, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)))))))
                .addContainerGap())
        );
        painelEsqLayout.setVerticalGroup(
            painelEsqLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, painelEsqLayout.createSequentialGroup()
                .addGroup(painelEsqLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbCliente1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(painelEsqLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtNomeConsumidorFinal, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(painelEsqLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(txtCodClientePesquisa, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtIniciaisCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
                .addGroup(painelEsqLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(painelEsqLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lbClienteConsumidorFinal1, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtTelClientePesquisa, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(painelEsqLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lbClienteConsumidorFinal, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtNifClientePesquisa, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(painelEsqLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(dc_data_vencimento, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(painelEsqLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cmbTipoDocumento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(painelEsqLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(painelEsqLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(painelEsqLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(painelEsqLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(cmbMoeda, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btnSemFormaPagamento, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lb_proximo_documento, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(painelEsqLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(rbTranstorno, javax.swing.GroupLayout.PREFERRED_SIZE, 7, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(painelEsqLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(rbMostrar, javax.swing.GroupLayout.PREFERRED_SIZE, 9, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jlStockNegativo, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))))
                        .addGap(7, 7, 7))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, painelEsqLayout.createSequentialGroup()
                        .addGroup(painelEsqLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(rbArmazem1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(rbArmazem, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(painelEsqLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lb_nome_usuario, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cmbAnoEconomico, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnCopia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(13, 13, 13))
        );

        painelEsqLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btnSemFormaPagamento, cmbMoeda});

        painelDir.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        txtPreco.setEditable(false);
        txtPreco.setBackground(new java.awt.Color(0, 255, 255));
        txtPreco.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txtPreco.setForeground(new java.awt.Color(0, 0, 51));
        txtPreco.setCaretColor(new java.awt.Color(255, 255, 255));

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/proucura.png"))); // NOI18N
        jButton4.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButton4ActionPerformed(evt);
            }
        });

        txtQuantidadeStock.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txtQuantidadeStock.setForeground(new java.awt.Color(255, 255, 255));

        txtQuatindade.setBackground(new java.awt.Color(0, 255, 255));
        txtQuatindade.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        txtQuatindade.setForeground(new java.awt.Color(0, 0, 51));
        txtQuatindade.setText("1");
        txtQuatindade.setCaretColor(new java.awt.Color(255, 255, 255));
        txtQuatindade.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtQuatindadeActionPerformed(evt);
            }
        });

        lbQuantidade.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbQuantidade.setText("Qtd:");

        btn_adicionar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/Button-Add-icon.png"))); // NOI18N
        btn_adicionar.setToolTipText("click para adicionar no carrinho");
        btn_adicionar.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btn_adicionarActionPerformed(evt);
            }
        });

        btn_remover.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/2934_32x32.png"))); // NOI18N
        btn_remover.setToolTipText("click para remover produtos do carrinho");
        btn_remover.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btn_removerActionPerformed(evt);
            }
        });

        ck_A4.setFont(new java.awt.Font("Lucida Grande", 0, 10)); // NOI18N
        ck_A4.setText("A4");
        ck_A4.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ck_A4ActionPerformed(evt);
            }
        });

        lbCodigoProduto.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbCodigoProduto.setText("Cod:");

        txtCodigoProduto.setBackground(new java.awt.Color(0, 255, 255));
        txtCodigoProduto.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        txtCodigoProduto.setForeground(new java.awt.Color(0, 0, 51));
        txtCodigoProduto.setCaretColor(new java.awt.Color(255, 255, 255));
        txtCodigoProduto.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtCodigoProdutoActionPerformed(evt);
            }
        });

        lbCodigoProduto1.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbCodigoProduto1.setText("CodBarra:");

        ck_A7.setFont(new java.awt.Font("Lucida Grande", 0, 10)); // NOI18N
        ck_A7.setText("A7");
        ck_A7.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ck_A7ActionPerformed(evt);
            }
        });

        ck_S_A6.setFont(new java.awt.Font("Lucida Grande", 0, 10)); // NOI18N
        ck_S_A6.setText("S_A6");
        ck_S_A6.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ck_S_A6ActionPerformed(evt);
            }
        });

        ck_ComVirgula.setFont(new java.awt.Font("Lucida Grande", 0, 10)); // NOI18N
        ck_ComVirgula.setText("A6V");
        ck_ComVirgula.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ck_ComVirgulaActionPerformed(evt);
            }
        });

        ck_simplificada_O_S.setFont(new java.awt.Font("Lucida Grande", 0, 10)); // NOI18N
        ck_simplificada_O_S.setText("S_A6_O");
        ck_simplificada_O_S.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ck_simplificada_O_SActionPerformed(evt);
            }
        });

        ck_simplificada_O.setFont(new java.awt.Font("Lucida Grande", 0, 10)); // NOI18N
        ck_simplificada_O.setSelected(true);
        ck_simplificada_O.setText("A6_O");
        ck_simplificada_O.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ck_simplificada_OActionPerformed(evt);
            }
        });

        ck_simplificada.setFont(new java.awt.Font("Lucida Grande", 0, 10)); // NOI18N
        ck_simplificada.setText("A6");
        ck_simplificada.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ck_simplificadaActionPerformed(evt);
            }
        });

        ck_Duplicada.setFont(new java.awt.Font("Lucida Grande", 0, 10)); // NOI18N
        ck_Duplicada.setText("A5");
        ck_Duplicada.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ck_DuplicadaActionPerformed(evt);
            }
        });

        btnFormaPagamento.setFont(new java.awt.Font("Lucida Grande", 0, 12)); // NOI18N
        btnFormaPagamento.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/confirmacao.png"))); // NOI18N
        btnFormaPagamento.setText("F. Pagamento");
        btnFormaPagamento.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnFormaPagamentoActionPerformed(evt);
            }
        });

        btnProcessar.setFont(new java.awt.Font("Lucida Grande", 0, 12)); // NOI18N
        btnProcessar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/pauta academica 16x16.png"))); // NOI18N
        btnProcessar.setText("Processar");
        btnProcessar.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnProcessarActionPerformed(evt);
            }
        });

        txtCodigoBarra.setBackground(new java.awt.Color(0, 255, 255));
        txtCodigoBarra.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        txtCodigoBarra.setForeground(new java.awt.Color(0, 0, 51));
        txtCodigoBarra.setCaretColor(new java.awt.Color(255, 255, 255));
        txtCodigoBarra.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtCodigoBarraActionPerformed(evt);
            }
        });

        txtCodigoManual.setBackground(new java.awt.Color(0, 255, 255));
        txtCodigoManual.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        txtCodigoManual.setForeground(new java.awt.Color(0, 0, 51));
        txtCodigoManual.setCaretColor(new java.awt.Color(255, 255, 255));
        txtCodigoManual.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtCodigoManualActionPerformed(evt);
            }
        });

        cmbSubFamilia.setBackground(new java.awt.Color(0, 255, 255));
        cmbSubFamilia.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        cmbSubFamilia.setForeground(new java.awt.Color(0, 0, 51));
        cmbSubFamilia.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbSubFamilia.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbSubFamiliaActionPerformed(evt);
            }
        });

        cmbProduto.setBackground(new java.awt.Color(0, 255, 255));
        cmbProduto.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        cmbProduto.setForeground(new java.awt.Color(0, 0, 51));
        cmbProduto.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbProduto.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbProdutoActionPerformed(evt);
            }
        });

        cmbArmazem.setBackground(new java.awt.Color(0, 255, 255));
        cmbArmazem.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 1, 10)); // NOI18N
        cmbArmazem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbArmazemActionPerformed(evt);
            }
        });

        lbQuantidadeStock.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbQuantidadeStock.setText("Stock:");

        txtTotalPagar.setEditable(false);
        txtTotalPagar.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N
        txtTotalPagar.setForeground(new java.awt.Color(255, 0, 0));
        txtTotalPagar.setCaretColor(new java.awt.Color(255, 255, 255));
        txtTotalPagar.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtTotalPagarActionPerformed(evt);
            }
        });

        lbCodigoProduto2.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbCodigoProduto2.setText("CodManual:");

        btnCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/LOGOUT - VERMELHO/Logout 32x32.png"))); // NOI18N
        btnCancelar.setAlignmentX(0.5F);
        btnCancelar.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnCancelarActionPerformed(evt);
            }
        });

        lbCodigoProduto3.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbCodigoProduto3.setText("Preço:");

        lbCodigoProduto4.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbCodigoProduto4.setText("TOTAL:");

        lbValorPorExtenco.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbValorPorExtenco.setForeground(new java.awt.Color(204, 0, 0));
        lbValorPorExtenco.setText("VALOR POR EXTENSO");

        jlEmpresa.setFont(new java.awt.Font("Lucida Grande", 1, 16)); // NOI18N
        jlEmpresa.setForeground(new java.awt.Color(0, 0, 102));
        jlEmpresa.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jlEmpresa.setText("Empresa");

        sp_desconto_financeiro.addChangeListener(new javax.swing.event.ChangeListener()
        {
            public void stateChanged(javax.swing.event.ChangeEvent evt)
            {
                sp_desconto_financeiroStateChanged(evt);
            }
        });
        sp_desconto_financeiro.addInputMethodListener(new java.awt.event.InputMethodListener()
        {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt)
            {
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt)
            {
                sp_desconto_financeiroInputMethodTextChanged(evt);
            }
        });
        sp_desconto_financeiro.addPropertyChangeListener(new java.beans.PropertyChangeListener()
        {
            public void propertyChange(java.beans.PropertyChangeEvent evt)
            {
                sp_desconto_financeiroPropertyChange(evt);
            }
        });
        sp_desconto_financeiro.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                sp_desconto_financeiroKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt)
            {
                sp_desconto_financeiroKeyTyped(evt);
            }
        });

        lbDescontoFinanceiro.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbDescontoFinanceiro.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbDescontoFinanceiro.setText("Desc:");

        txtTotal_AOA_Retencao.setForeground(new java.awt.Color(255, 51, 51));
        txtTotal_AOA_Retencao.setText("Retencao");

        dc_data_documento.setEnabled(false);
        dc_data_documento.setFont(new java.awt.Font("Lucida Grande", 1, 12)); // NOI18N

        javax.swing.GroupLayout painelDirLayout = new javax.swing.GroupLayout(painelDir);
        painelDir.setLayout(painelDirLayout);
        painelDirLayout.setHorizontalGroup(
            painelDirLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(painelDirLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(painelDirLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(painelDirLayout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addComponent(btn_adicionar)
                        .addGap(18, 18, 18)
                        .addComponent(btn_remover)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnFormaPagamento)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnProcessar, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jlEmpresa, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(14, 14, 14))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, painelDirLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(lbValorPorExtenco, javax.swing.GroupLayout.PREFERRED_SIZE, 457, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTotal_AOA_Retencao, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(painelDirLayout.createSequentialGroup()
                        .addGroup(painelDirLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(painelDirLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(painelDirLayout.createSequentialGroup()
                                    .addComponent(lbQuantidade, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(txtQuatindade, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(lbCodigoProduto1, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(txtCodigoBarra, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(lbCodigoProduto)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(txtCodigoProduto, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(lbCodigoProduto2)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(txtCodigoManual)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(painelDirLayout.createSequentialGroup()
                                    .addComponent(ck_S_A6)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(ck_ComVirgula, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(ck_A7, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(ck_simplificada_O_S, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(ck_simplificada_O, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(ck_simplificada)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(ck_Duplicada)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(ck_A4, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(lbCodigoProduto3)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(txtPreco, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(painelDirLayout.createSequentialGroup()
                                .addComponent(cmbSubFamilia, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cmbProduto, javax.swing.GroupLayout.PREFERRED_SIZE, 299, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtLocal, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(dc_data_documento, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(painelDirLayout.createSequentialGroup()
                        .addComponent(txtReferencia, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbArmazem, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbQuantidadeStock)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtQuantidadeStock, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbDescontoFinanceiro, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sp_desconto_financeiro, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbCodigoProduto4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTotalPagar))))
        );
        painelDirLayout.setVerticalGroup(
            painelDirLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(painelDirLayout.createSequentialGroup()
                .addGroup(painelDirLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtReferencia, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTotalPagar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(painelDirLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cmbArmazem, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lbQuantidadeStock, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(painelDirLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtQuantidadeStock, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(sp_desconto_financeiro, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lbDescontoFinanceiro, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lbCodigoProduto4, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, Short.MAX_VALUE)
                .addGroup(painelDirLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbValorPorExtenco, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTotal_AOA_Retencao, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(painelDirLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(painelDirLayout.createSequentialGroup()
                        .addGroup(painelDirLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(cmbSubFamilia, javax.swing.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE)
                            .addComponent(txtLocal, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbProduto))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(painelDirLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(painelDirLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txtCodigoManual, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lbCodigoProduto2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(painelDirLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txtQuatindade, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lbCodigoProduto1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtCodigoBarra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtCodigoProduto, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lbCodigoProduto, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(lbQuantidade, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(painelDirLayout.createSequentialGroup()
                        .addComponent(dc_data_documento, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)
                        .addComponent(jButton4)))
                .addGroup(painelDirLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(painelDirLayout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addGroup(painelDirLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(ck_S_A6)
                            .addComponent(ck_ComVirgula)
                            .addComponent(ck_A7)
                            .addComponent(ck_simplificada_O_S)
                            .addComponent(ck_simplificada_O)
                            .addComponent(ck_simplificada)
                            .addComponent(ck_Duplicada)
                            .addComponent(ck_A4)))
                    .addGroup(painelDirLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtPreco, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lbCodigoProduto3, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(painelDirLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(painelDirLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(btnCancelar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btn_remover, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, painelDirLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnFormaPagamento, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnProcessar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jlEmpresa)))
                    .addComponent(btn_adicionar, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(25, Short.MAX_VALUE))
        );

        painelDirLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btn_adicionar, btn_remover});

        painelDirLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {txtCodigoBarra, txtCodigoManual, txtCodigoProduto, txtQuatindade});

        javax.swing.GroupLayout painelTopoLayout = new javax.swing.GroupLayout(painelTopo);
        painelTopo.setLayout(painelTopoLayout);
        painelTopoLayout.setHorizontalGroup(
            painelTopoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(painelTopoLayout.createSequentialGroup()
                .addComponent(painelEsq, javax.swing.GroupLayout.PREFERRED_SIZE, 356, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(painelDir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        painelTopoLayout.setVerticalGroup(
            painelTopoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(painelTopoLayout.createSequentialGroup()
                .addGroup(painelTopoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(painelEsq, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(painelDir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        painelTabela.setLayout(new java.awt.BorderLayout());

        jScrollPane1.addPropertyChangeListener(new java.beans.PropertyChangeListener()
        {
            public void propertyChange(java.beans.PropertyChangeEvent evt)
            {
                jScrollPane1PropertyChange(evt);
            }
        });

        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {
                {null, null, null, null, null, null, null, null, null, null, null}
            },
            new String []
            {
                "Codigo", "Descrição", "Unidade", "Preço", "Quantidade", "Desconto (%)", "Taxa", "Retenção", "Valor Retenção", "Valor", "Valor C/ Imposto"
            }
        )
        {
            boolean[] canEdit = new boolean []
            {
                true, false, false, false, true, true, false, false, false, true, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex)
            {
                return canEdit [columnIndex];
            }
        });
        table.addPropertyChangeListener(new java.beans.PropertyChangeListener()
        {
            public void propertyChange(java.beans.PropertyChangeEvent evt)
            {
                tablePropertyChange(evt);
            }
        });
        jScrollPane1.setViewportView(table);
        if (table.getColumnModel().getColumnCount() > 0)
        {
            table.getColumnModel().getColumn(0).setPreferredWidth(70);
            table.getColumnModel().getColumn(1).setPreferredWidth(300);
            table.getColumnModel().getColumn(2).setPreferredWidth(5);
            table.getColumnModel().getColumn(3).setPreferredWidth(40);
            table.getColumnModel().getColumn(4).setPreferredWidth(10);
            table.getColumnModel().getColumn(5).setPreferredWidth(20);
            table.getColumnModel().getColumn(6).setPreferredWidth(5);
            table.getColumnModel().getColumn(7).setPreferredWidth(10);
            table.getColumnModel().getColumn(8).setPreferredWidth(20);
            table.getColumnModel().getColumn(9).setPreferredWidth(50);
            table.getColumnModel().getColumn(10).setPreferredWidth(70);
        }

        painelTabela.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(painelTopo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(painelTabela, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(painelTopo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(painelTabela, javax.swing.GroupLayout.PREFERRED_SIZE, 723, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cmbClienteActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbClienteActionPerformed
    {//GEN-HEADEREND:event_cmbClienteActionPerformed
        // TODO add your handling code here:
        accao_cliente();
        mostra_consumidor_final();
    }//GEN-LAST:event_cmbClienteActionPerformed

    private void txtIniciaisClienteActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtIniciaisClienteActionPerformed
    {//GEN-HEADEREND:event_txtIniciaisClienteActionPerformed
//        txtQuatindade.requestFocus();
    }//GEN-LAST:event_txtIniciaisClienteActionPerformed

    private void txtNomeConsumidorFinalActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtNomeConsumidorFinalActionPerformed
    {//GEN-HEADEREND:event_txtNomeConsumidorFinalActionPerformed
//        txtQuatindade.requestFocus();

    }//GEN-LAST:event_txtNomeConsumidorFinalActionPerformed

    private void txtNomeConsumidorFinalKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtNomeConsumidorFinalKeyPressed
    {//GEN-HEADEREND:event_txtNomeConsumidorFinalKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNomeConsumidorFinalKeyPressed

    private void txtNifClientePesquisaActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtNifClientePesquisaActionPerformed
    {//GEN-HEADEREND:event_txtNifClientePesquisaActionPerformed
        pesquisa_cliente_by_nif();
    }//GEN-LAST:event_txtNifClientePesquisaActionPerformed

    private void txtCodClientePesquisaActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtCodClientePesquisaActionPerformed
    {//GEN-HEADEREND:event_txtCodClientePesquisaActionPerformed
        pesquisa_cliente_by_cod();
    }//GEN-LAST:event_txtCodClientePesquisaActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton5ActionPerformed
    {//GEN-HEADEREND:event_jButton5ActionPerformed
        new ClienteVisao( this, rootPaneCheckingEnabled, BDConexao.getInstancia() ).setVisible( true );
    }//GEN-LAST:event_jButton5ActionPerformed

    private void txtTotalPagarActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtTotalPagarActionPerformed
    {//GEN-HEADEREND:event_txtTotalPagarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTotalPagarActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton4ActionPerformed
    {//GEN-HEADEREND:event_jButton4ActionPerformed

        try
        {
            if ( validar() )
            {
                new BuscaProdutoVisao( this, rootPaneCheckingEnabled,
                        getCodigoArmazem(),
                        DVML.JANELA_VENDA,
                        BDConexao.getInstancia() ).setVisible( true );
            }
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void cmbProdutoActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbProdutoActionPerformed
    {//GEN-HEADEREND:event_cmbProdutoActionPerformed
        // TODO add your handling code here:
        try
        {
            adicionar_preco_quantidade();
        }
        catch ( Exception e )
        {
        }
    }//GEN-LAST:event_cmbProdutoActionPerformed

        private void pesquisa_cliente_by_cod()
    {

        Integer codCliente = Integer.parseInt( txtCodClientePesquisa.getText() );
        try
        {

            TbCliente cliente = (TbCliente) clientesController.findById( codCliente );
            String nome_cliente = cliente.getNome();
            cmbCliente.setSelectedItem( nome_cliente.trim() );
            accao_cliente();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            JOptionPane.showMessageDialog( null, "Não existe cliente com código" );
            cmbCliente.setSelectedItem( "Consumidor Final" );
        }
        txtCodClientePesquisa.setText( "" );
        txtCodClientePesquisa.requestFocus();
    }

    private void pesquisa_cliente_by_nif()
    {

        String nif = txtNifClientePesquisa.getText();
        try
        {
            String nome_cliente = clientesController.getClienteByNifOrberByNome( nif ).getNome();
            cmbCliente.setSelectedItem( nome_cliente.trim() );
            accao_cliente();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            JOptionPane.showMessageDialog( null, "Não existe cliente com este código" );
            cmbCliente.setSelectedItem( "Consumidor Final" );
        }
        txtNifClientePesquisa.requestFocus();
    }
    
    private void pesquisa_cliente_by_tel()
    {

        String tel = txtTelClientePesquisa.getText();
        try
        {
            String nome_cliente = clientesController.getClienteByTelOrberByNome( tel ).getNome();
            cmbCliente.setSelectedItem( nome_cliente.trim() );
            accao_cliente_tel();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            JOptionPane.showMessageDialog( null, "Não existe cliente com este código" );
            cmbCliente.setSelectedItem( "Consumidor Final" );
        }
        txtTelClientePesquisa.requestFocus();
    }
    
    private void cmbArmazemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbArmazemActionPerformed
    {//GEN-HEADEREND:event_cmbArmazemActionPerformed
        // TODO add your handling code here:
//        adicionar_preco_quantidade();
        //        configurar_armazens();
    }//GEN-LAST:event_cmbArmazemActionPerformed

    private void txtCodigoManualActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtCodigoManualActionPerformed
    {//GEN-HEADEREND:event_txtCodigoManualActionPerformed
        accao_codigo_manual_enter();
    }//GEN-LAST:event_txtCodigoManualActionPerformed

    private void cmbAnoEconomicoActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbAnoEconomicoActionPerformed
    {//GEN-HEADEREND:event_cmbAnoEconomicoActionPerformed
        // TODO add your handling code here:
        mostrar_proximo_codigo_documento();
        actualizar_abreviacao();
    }//GEN-LAST:event_cmbAnoEconomicoActionPerformed

    private void rbTranstornoActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_rbTranstornoActionPerformed
    {//GEN-HEADEREND:event_rbTranstornoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rbTranstornoActionPerformed

    private void rbMostrarActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_rbMostrarActionPerformed
    {//GEN-HEADEREND:event_rbMostrarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rbMostrarActionPerformed

    private void txtQuatindadeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtQuatindadeActionPerformed
    {//GEN-HEADEREND:event_txtQuatindadeActionPerformed
        // TODO add your handling code here:
        if ( validar() )
        {

            setFocus( dadosInstituicao.getFoco() );
            txtCodigoBarra.setText( "" );
        }
    }//GEN-LAST:event_txtQuatindadeActionPerformed

    private void btn_adicionarActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btn_adicionarActionPerformed
    {//GEN-HEADEREND:event_btn_adicionarActionPerformed

        if ( validar() )
        {

            configuracaoMesComecoController = new ConfiguracaoMesComecoController( conexao.getConnectionAtiva() );
            boolean existeConfiguracaoDoCliente = configuracaoMesComecoController.existeConfiguracaoDoCliente( getIdCliente(), getCodigoProduto() );

            if ( existeConfiguracaoDoCliente )
            {
                new MesesPagoClienteVisao( this, rootPaneCheckingEnabled,
                        getIdCliente(),
                        getCodigoProduto(), conexao ).setVisible( true );
            }
            else
            {
                adicionar_botao();
                scrolltable();
            }

        }
    }//GEN-LAST:event_btn_adicionarActionPerformed

    private void btn_removerActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btn_removerActionPerformed
    {//GEN-HEADEREND:event_btn_removerActionPerformed
        try
        {

            DefaultTableModel modelo = ( DefaultTableModel ) table.getModel();
            if ( podeRemoverServico( modelo, table.getSelectedRow() ) )
            {
                actualizarPrecosAntigos2();
                remover_item_carrinho();
                inserir_uma_linha();
            }

        }
        catch ( Exception ex )
        {
            ex.printStackTrace();
            //Logger.getLogger(VendaUsuarioVisao.class.getName()).log(Level.SEVERE, null, ex);
//            JOptionPane.showMessageDialog( null, "Possivelmente não selecionaste \n nenhuma linha ou não existe dados na tabela", "AVISO", JOptionPane.WARNING_MESSAGE );
        }

    }//GEN-LAST:event_btn_removerActionPerformed

    private void ck_A4ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_ck_A4ActionPerformed
    {//GEN-HEADEREND:event_ck_A4ActionPerformed
        // TODO add your handling code here:
//        actualizar_abreviacao();
    }//GEN-LAST:event_ck_A4ActionPerformed

    private void txtCodigoProdutoActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtCodigoProdutoActionPerformed
    {//GEN-HEADEREND:event_txtCodigoProdutoActionPerformed
        if ( validar() )
        {
            accao_codigo_interno_enter();
            scrolltable();

        }
    }//GEN-LAST:event_txtCodigoProdutoActionPerformed

    private void cmbSubFamiliaActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbSubFamiliaActionPerformed
    {//GEN-HEADEREND:event_cmbSubFamiliaActionPerformed

//        cmbProduto.setModel( new DefaultComboBoxModel( ( produtosController.getVectorByIdTipoProduto( getIdTipoProduto() ) ) ) );
    }//GEN-LAST:event_cmbSubFamiliaActionPerformed

    private void txtCodigoBarraActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtCodigoBarraActionPerformed
    {//GEN-HEADEREND:event_txtCodigoBarraActionPerformed
//        accao_codigo_barra_enter();
//        accao_codigo_barra_enter_cb();
        accao_codigo_barra_enter_cb();
        scrolltable();


    }//GEN-LAST:event_txtCodigoBarraActionPerformed

    private void ck_A7ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_ck_A7ActionPerformed
    {//GEN-HEADEREND:event_ck_A7ActionPerformed
        // TODO add your handling code here:
//        actualizar_abreviacao();
    }//GEN-LAST:event_ck_A7ActionPerformed

    private void ck_S_A6ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_ck_S_A6ActionPerformed
    {//GEN-HEADEREND:event_ck_S_A6ActionPerformed
//        actualizar_abreviacao();
    }//GEN-LAST:event_ck_S_A6ActionPerformed

    private void ck_ComVirgulaActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_ck_ComVirgulaActionPerformed
    {//GEN-HEADEREND:event_ck_ComVirgulaActionPerformed
//        actualizar_abreviacao();
    }//GEN-LAST:event_ck_ComVirgulaActionPerformed

    private void ck_simplificada_O_SActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_ck_simplificada_O_SActionPerformed
    {//GEN-HEADEREND:event_ck_simplificada_O_SActionPerformed
//        actualizar_abreviacao();
    }//GEN-LAST:event_ck_simplificada_O_SActionPerformed

    private void ck_simplificada_OActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_ck_simplificada_OActionPerformed
    {//GEN-HEADEREND:event_ck_simplificada_OActionPerformed
//        actualizar_abreviacao();
    }//GEN-LAST:event_ck_simplificada_OActionPerformed

    private void ck_simplificadaActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_ck_simplificadaActionPerformed
    {//GEN-HEADEREND:event_ck_simplificadaActionPerformed
        // TODO add your handling code here:
//        actualizar_abreviacao();
    }//GEN-LAST:event_ck_simplificadaActionPerformed

    private void ck_DuplicadaActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_ck_DuplicadaActionPerformed
    {//GEN-HEADEREND:event_ck_DuplicadaActionPerformed
        // TODO add your handling code here:
//        actualizar_abreviacao();
    }//GEN-LAST:event_ck_DuplicadaActionPerformed

    private void cmbTipoDocumentoActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbTipoDocumentoActionPerformed
    {//GEN-HEADEREND:event_cmbTipoDocumentoActionPerformed
        mostrar_proximo_codigo_documento();
        actualizar_abreviacao();
        desabilitar_campos();
        atualizarCliente1();
        atualizarDataVencimentoFA();
    }//GEN-LAST:event_cmbTipoDocumentoActionPerformed

    private void btnFormaPagamentoActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnFormaPagamentoActionPerformed
    {//GEN-HEADEREND:event_btnFormaPagamentoActionPerformed
        removerUltimaLinhaVazia();

        jScrollPane1.repaint();
        if ( MetodosUtil.licencaValidada( conexao ) )
        {
            if ( !MetodosUtil.tabela_vazia( table ) )
            {
                if ( !validarPrecos_tabela( table ) )
                {
                    return; // Se houver erro, não abre forma de pagamento
                }

                new FormaPagamentoVisao( this, rootPaneCheckingEnabled, null, DVML.VENDA_PONTUAL_TOP, BDConexao.getInstancia() ).setVisible( true );
            }
            else
            {
                JOptionPane.showMessageDialog( null, "Caro usuário, adicione itens na tabela" );
            }
        }

        table.getCellEditor().cancelCellEditing();


    }//GEN-LAST:event_btnFormaPagamentoActionPerformed

    private void abrirFormaPagamento()
    {

        jScrollPane1.repaint();

        if ( MetodosUtil.licencaValidada( conexao ) )
        {

            if ( !MetodosUtil.tabela_vazia( table ) )
            {

                if ( !validarPrecos_tabela( table ) )
                {
                    return; // Se houver erro, não abre forma de pagamento
                }

                new FormaPagamentoVisao(
                        this,
                        rootPaneCheckingEnabled,
                        null,
                        DVML.VENDA_PONTUAL_TOP,
                        BDConexao.getInstancia()
                ).setVisible( true );

            }
            else
            {
                JOptionPane.showMessageDialog( null, "Caro usuário, adicione itens na tabela" );
            }
        }

        table.getCellEditor().cancelCellEditing();
    }


    private void btnProcessarActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnProcessarActionPerformed
    {//GEN-HEADEREND:event_btnProcessarActionPerformed
        if ( validar() )
        {

            removerUltimaLinhaVazia();
            jScrollPane1.repaint();
            if ( MetodosUtil.licencaValidada( conexao ) )
            {
                procedimento_salvar_venda_comercial( true );

            }

        }
    }//GEN-LAST:event_btnProcessarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnCancelarActionPerformed
    {//GEN-HEADEREND:event_btnCancelarActionPerformed
        // TODO add your handling code here:
        actualizarPrecosAntigos2();
        dispose();
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void sp_desconto_financeiroStateChanged(javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_sp_desconto_financeiroStateChanged
    {//GEN-HEADEREND:event_sp_desconto_financeiroStateChanged
        // TODO add your handling code here:
        tratar_desconto();
    }//GEN-LAST:event_sp_desconto_financeiroStateChanged

    private void sp_desconto_financeiroInputMethodTextChanged(java.awt.event.InputMethodEvent evt)//GEN-FIRST:event_sp_desconto_financeiroInputMethodTextChanged
    {//GEN-HEADEREND:event_sp_desconto_financeiroInputMethodTextChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_sp_desconto_financeiroInputMethodTextChanged

    private void sp_desconto_financeiroPropertyChange(java.beans.PropertyChangeEvent evt)//GEN-FIRST:event_sp_desconto_financeiroPropertyChange
    {//GEN-HEADEREND:event_sp_desconto_financeiroPropertyChange
        // TODO add your handling code here:
    }//GEN-LAST:event_sp_desconto_financeiroPropertyChange

    private void sp_desconto_financeiroKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_sp_desconto_financeiroKeyPressed
    {//GEN-HEADEREND:event_sp_desconto_financeiroKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_sp_desconto_financeiroKeyPressed

    private void sp_desconto_financeiroKeyTyped(java.awt.event.KeyEvent evt)//GEN-FIRST:event_sp_desconto_financeiroKeyTyped
    {//GEN-HEADEREND:event_sp_desconto_financeiroKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_sp_desconto_financeiroKeyTyped

    private void rbArmazem1ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_rbArmazem1ActionPerformed
    {//GEN-HEADEREND:event_rbArmazem1ActionPerformed
        try
        {
            configurar_armazens();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }//GEN-LAST:event_rbArmazem1ActionPerformed

    private void rbArmazemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_rbArmazemActionPerformed
    {//GEN-HEADEREND:event_rbArmazemActionPerformed
        try
        {
            configurar_armazens();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }//GEN-LAST:event_rbArmazemActionPerformed

    private void cmbMoedaActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbMoedaActionPerformed
    {//GEN-HEADEREND:event_cmbMoedaActionPerformed
        // TODO add your handling code here:
        actualizar_moeda();
    }//GEN-LAST:event_cmbMoedaActionPerformed

    private void btnSemFormaPagamentoActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnSemFormaPagamentoActionPerformed
    {//GEN-HEADEREND:event_btnSemFormaPagamentoActionPerformed
        // TODO add your handling code here:
        if ( MetodosUtil.licencaValidada( conexao ) )
        {
            if ( !MetodosUtil.tabela_vazia( table ) )
            {
                if ( !validarPrecos_tabela( table ) )
                {
                    return; // Se houver erro, não abre forma de pagamento
                }
                procedimento_salvar_venda_comercial( false );
            }
            else
            {
                JOptionPane.showMessageDialog( null, "Caro usuário, adicione itens na tabela" );
            }
        }
    }//GEN-LAST:event_btnSemFormaPagamentoActionPerformed

    private void tablePropertyChange(java.beans.PropertyChangeEvent evt)//GEN-FIRST:event_tablePropertyChange
    {//GEN-HEADEREND:event_tablePropertyChange
        // TODO add your handling code here:
        if ( table.getSelectedColumn() == 3 || table.getSelectedColumn() == 4 || table.getSelectedColumn() == 5 )
        {
            System.out.println( "Preparar para actualiza a Qtd......" );
            actualizarPreco();
//            JOptionPane.showMessageDialog( null, "Preparar para actualizar qtd" );
            actualizarQtdTable();
        }
    }//GEN-LAST:event_tablePropertyChange

    private void jScrollPane1PropertyChange(java.beans.PropertyChangeEvent evt)//GEN-FIRST:event_jScrollPane1PropertyChange
    {//GEN-HEADEREND:event_jScrollPane1PropertyChange

    }//GEN-LAST:event_jScrollPane1PropertyChange

    private void txtTelClientePesquisaActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtTelClientePesquisaActionPerformed
    {//GEN-HEADEREND:event_txtTelClientePesquisaActionPerformed
        pesquisa_cliente_by_tel();
    }//GEN-LAST:event_txtTelClientePesquisaActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main( String args[] ) throws SQLException
    {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try
        {
            for ( javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels() )
            {
                if ( "Nimbus".equals( info.getName() ) )
                {
                    javax.swing.UIManager.setLookAndFeel( info.getClassName() );
                    break;
                }
            }
        }
        catch ( ClassNotFoundException ex )
        {
            java.util.logging.Logger.getLogger( FormVendaResponsivaVisaoTop.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        catch ( InstantiationException ex )
        {
            java.util.logging.Logger.getLogger( FormVendaResponsivaVisaoTop.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        catch ( IllegalAccessException ex )
        {
            java.util.logging.Logger.getLogger( FormVendaResponsivaVisaoTop.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        catch ( javax.swing.UnsupportedLookAndFeelException ex )
        {
            java.util.logging.Logger.getLogger( FormVendaResponsivaVisaoTop.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater( new Runnable()
        {
            public void run()
            {
                try
                {
                    new FormVendaResponsivaVisaoTop( 15, BDConexao.getInstancia() ).show( true );
                }
                catch ( SQLException ex )
                {
                    Logger.getLogger( FormVendaResponsivaVisaoTop.class.getName() ).log( Level.SEVERE, null, ex );
                }
            }
        } );
    }
//    
//        public static void main( String[] args ) throws SQLException
//    {
//        new FormVendaResponsivaVisaoTop( 15, BDConexao.getInstancia() ).show( true );
//    }

    public static int getIdDocumento()
    {
        try
        {
            Documento documentoLocal = documentosController.getDocumentoByDesignacao( cmbTipoDocumento.getSelectedItem().toString() );
            return documentoLocal.getPkDocumento();
        }
        catch ( Exception e )
        {
            return 0;
        }
    }

    /* CRIACAO DO GETS  */
    public static double getQuantidade()
    {
        return Double.parseDouble( txtQuatindade.getText() );
    }

    public static int getIdCliente()
    {
        try
        {
            TbCliente cliente = clientesController.getClienteByNome( cmbCliente.getSelectedItem().toString() );
            return cliente.getCodigo();
        }
        catch ( Exception e )
        {
            return 0;
        }

    }

    public static int getIdDocumento( DocumentosController documentosControllerLocal )
    {
        try
        {
            Documento documentoLocal = documentosControllerLocal.getDocumentoByDesignacao( cmbTipoDocumento.getSelectedItem().toString() );
            return documentoLocal.getPkDocumento();
        }
        catch ( Exception e )
        {
            return 0;
        }
    }

    public static int getIdAnoEconomico()
    {
        try
        {
            AnoEconomico anoEconomicoLocal = anoEconomicoController
                    .getAnoEconomicoByDesignacao( cmbAnoEconomico.getSelectedItem().toString() );
            return anoEconomicoLocal.getPkAnoEconomico();
        }
        catch ( Exception e )
        {
            return 0;
        }
    }

    public static int getIdAnoEconomico( AnoEconomicoController anoEconomicoControllerLocal )
    {
        try
        {
            AnoEconomico anoEconomicoLocal = anoEconomicoControllerLocal
                    .getAnoEconomicoByDesignacao( cmbAnoEconomico.getSelectedItem().toString() );
            return anoEconomicoLocal.getPkAnoEconomico();
        }
        catch ( Exception e )
        {
            return 0;
        }
    }

    public static int getIdMoeda()
    {
        try
        {
            Moeda moedaLocal = moedasController.getMoedaByDesignacao( cmbMoeda.getSelectedItem().toString() );
            return moedaLocal.getPkMoeda();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            return 0;
        }

    }

    public static Double getPreco()
    {

        Moeda moeda = getMoeda();
        if ( moeda == null )
        {
            return null;
        }

        Cambio lastCambio = cambiosController.getLastObject( moeda.getPkMoeda() );
        try
        {

            Double valorCambio = lastCambio.getValor();
            TbPreco precoLocal = precosController.getLastIdPrecoByIdProduto( getCodigoProduto(), Double.parseDouble( txtQuatindade.getText() ) );
            double precoVenda = precoLocal.getPrecoVenda().doubleValue();

            return ( precoVenda / valorCambio );
        }
        catch ( Exception e )
        {
            return 0.0;
        }

    }

    public static Double getPreco( int codigoProduto, double qtd )
    {

        Moeda moeda = getMoeda();
        if ( moeda == null )
        {
            return null;
        }

        Cambio lastCambio = cambiosController.getLastObject( moeda.getPkMoeda() );
        try
        {

            Double valorCambio = lastCambio.getValor();
            TbPreco precoLocal = precosController.getLastIdPrecoByIdProduto( codigoProduto, qtd );
            double precoVenda = precoLocal.getPrecoVenda().doubleValue();

            return ( precoVenda / valorCambio );
        }
        catch ( Exception e )
        {
            return 0.0;
        }

    }

    private static void mostrar_abreviacao_moeda_cambio()
    {
        try
        {
            cambio = cambiosController.getLastObject( getIdMoeda() );

        }
        catch ( Exception e )
        {
            cambio = null;
            e.printStackTrace();
        }
    }

    private static double getTaxaImposto( int idProduto )
    {
        TbProduto produto = ( TbProduto ) produtosController.findById( idProduto );
        //verifca o artigo se eh produto ou servico.
        if ( produto.getStocavel().equals( "true" ) )
        {
            return produtosImpostoController.getTaxaByIdProduto( idProduto );
        }
        else
        {
            return servicosRetencaoController.getTaxaByIdProduto( idProduto );
        }

    }

    private static double getTaxaImpostoIva( int idProduto )
    {
        try
        {
            return produtosImpostoController.getTaxaByIdProduto( idProduto );
        }
        catch ( Exception e )
        {
        }

        return 0;

    }

    private static double getTaxaImpostoRet( int idProduto )
    {
        try
        {
            return servicosRetencaoController.getTaxaByIdProduto( idProduto );
        }
        catch ( Exception e )
        {
        }
        return 0;

    }

    private static String getMotivoIsensao( int idProduto )
    {
        try
        {
            return produtosIsentoController.getRegimeIsensaoByIdProduto( idProduto );

        }
        catch ( Exception e )
        {
        }

        return "";
    }

    private void actualizar_moeda()
    {
        CfMethods.MOEDA = getMoeda().getAbreviacao();
        mostrar_abreviacao_moeda_cambio();
        try
        {
//            refresh_table();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }

    private static void actualizar_moeda( String moeda )
    {
        CfMethods.MOEDA = moeda;
        mostrar_abreviacao_moeda_cambio();
        refresh_table( 1 );
    }

    public void scrolltable()
    {

        table.scrollRectToVisible( table.getCellRect( table.getRowCount() - 1, table.getColumnCount(), true ) );

    }

    public void configurar_armazens()
    {
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

        cmbArmazem.setModel( new DefaultComboBoxModel( armazensController.getVector2() ) );
    }

    public static Double getPreco( int idProduto, double qtd, int idMoeda )
    {

        Moeda moeda = ( Moeda ) moedasController.findById( idMoeda );
        if ( moeda == null )
        {
            return null;
        }

        Cambio lastCambio = cambiosController.getLastObject( moeda.getPkMoeda() );
        try
        {
            Double valorCambio = lastCambio.getValor();
            TbPreco precoLocal = precosController.getLastIdPrecoByIdProduto( idProduto, qtd );
            double precoVenda = precoLocal.getPrecoVenda().doubleValue();

            if ( moeda.getAbreviacao().equals( DVML.MOEDA_KWANZA ) )
            {
                return precoVenda;
            }

            return ( precoVenda / valorCambio );

        }
        catch ( Exception e )
        {
            e.printStackTrace();
            return null;
        }

    }

    public static String getDescricao_Produto()
    {
        TbProduto produto = ( TbProduto ) produtosController.findById( getCodigoProduto() );
        return produto.getDesignacao();
    }

    public static String getUnidade_Produto()
    {
        TbProduto produto = ( TbProduto ) produtosController.findById( getCodigoProduto() );
        Unidade unidade = ( Unidade ) unidadesController.findById( produto.getCodUnidade().getPkUnidade() );
        return unidade.getAbreviacao();
    }

    private static Moeda getMoeda()
    {
        String moedaSelecionada = ( String ) cmbMoeda.getSelectedItem();

        if ( moedaSelecionada == null )
        {
            return null;
        }

        return moedasController.getMoedaByDesignacao( moedaSelecionada );
    }

    public static void actualizar_quantidade( int cod, double quantidade, BDConexao conexaoLocal )
    {

        String sql = "UPDATE tb_stock SET quantidade_existente =  " + ( getQuantidadeProduto( cod ) - quantidade ) + " WHERE cod_produto_codigo = " + cod + " AND  cod_armazem = " + getCodigoArmazem();
        System.out.println( "Quantidade   : " + quantidade );
        conexaoLocal.executeUpdate( sql );

    }

    public static double getQuantidadeProduto( int cod_produto )
    {

        String sql = "SELECT quantidade_existente FROM  tb_stock WHERE  cod_produto_codigo = " + cod_produto + " AND cod_armazem = " + getCodigoArmazem();

        ResultSet rs = conexao.executeQuery( sql );

        try
        {
            if ( rs.next() )
            {
                return rs.getDouble( "quantidade_existente" );
            }
        }
        catch ( SQLException ex )
        {
            ex.printStackTrace();
            return 0;
        }

        return 0;
    }

    public int getLastCodigo( String tabela )
    {

        String sql = "SELECT max(codigo) FROM " + tabela;

        ResultSet rs = conexao.executeQuery( sql );

        try
        {
            if ( rs.next() )
            {
                return rs.getInt( 1 );
            }
        }
        catch ( SQLException ex )
        {
            ex.printStackTrace();
            return 0;
        }

        return 0;
    }

    public static double getDesconto_produto( double preco_total_produto ) throws SQLException
    {

        TbDesconto desconto = getDesconto();
        Double quantidade = desconto.getQuantidade();
        double percentagem_desconto = desconto.getValor();

        if ( getQuantidade() >= quantidade )
        {
            return preco_total_produto * ( percentagem_desconto / 100 );
        }
        else
        {
            return 0.0;
        }

    }

    private static TbDesconto getDesconto()
    {
        TbDesconto desconto = descontosController.get_desconto_cliente_produto( getIdCliente(), getCodigoProduto() );
        return desconto;
    }

    private static double getDescontoPercentagem()
    {
        TbDesconto desconto = getDesconto();

        if ( !Objects.isNull( desconto ) )
        {
            try
            {
                Double quantidade = desconto.getQuantidade();
                if ( getQuantidade() >= quantidade )
                {
                    return desconto.getValor();
                }
                else
                {
                    return 0.0;
                }
            }
            catch ( Exception e )
            {
            }
            return 0.0;

        }
        return 0d;

//
    }

    public static double getDesconto_produto( double preco_total_produto, int qtd )
    {

        TbDesconto desconto = descontosController.get_desconto_cliente_produto( getIdCliente(), getCodigoProduto() );
        Double quantidade = desconto.getQuantidade();
        double percentagem_desconto = desconto.getValor();

        if ( qtd >= quantidade )
        {
            return preco_total_produto * ( percentagem_desconto / 100 );
        }
        else
        {
            return 0.0;
        }

    }

    public static double getDescontoValorLinha( int codigo_produto, double preco_total_linha, int qtd )
    {

        TbDesconto desconto = descontosController.get_desconto_cliente_produto( getIdCliente(), codigo_produto );
        Double quantidade = desconto.getQuantidade();
        double percentagem_desconto = desconto.getValor();

        if ( qtd >= quantidade )
        {
            return preco_total_linha * ( percentagem_desconto / 100 );
        }
        else
        {
            return 0.0;
        }

    }

    public void getDesconto_Quantidade() throws SQLException
    {
    }

    public double getDescontoActual() throws SQLException
    {

        ResultSet resultado = conexao.executeQuery( "SELECT valor FROM tb_desconto WHERE idDesconto = 1" );
        double valor = 0;
        if ( resultado.next() )
        {
            valor = resultado.getDouble( "valor desconto actual" );
        }
        return valor;

    }

    public static void remover_all_produto()
    {

        DefaultTableModel modelo = ( DefaultTableModel ) table.getModel();
        for ( int i = modelo.getRowCount() - 1; i >= 0; i-- )
        {
            modelo.removeRow( i );
        }

    }

    private static double getValorComImposto( double qtd, double taxa, double preco_venda, double desconto )
    {
        double subtotal_linha = (preco_venda * qtd);
        double desconto_valor = (subtotal_linha * ( desconto / 100 ));
        double valor_iva = 1 + ( taxa / 100 );//
        return ( ( subtotal_linha - desconto_valor ) * valor_iva );
    }

    private static BigDecimal getIVA( BigDecimal qtd, BigDecimal taxa, BigDecimal precoVenda, BigDecimal desconto )
    {
        // subtotal = preco * quantidade
        BigDecimal subtotal = precoVenda.multiply( qtd );

        // subtrai o desconto (se houver)
        BigDecimal baseCalculo = subtotal.subtract( desconto );

        // taxa/100 → para obter o fator de imposto
        BigDecimal fatorIva = taxa.divide( BigDecimal.valueOf( 100 ), 4, RoundingMode.HALF_UP );

        // valor do IVA
        BigDecimal valorIva = baseCalculo.multiply( fatorIva );

        return valorIva.setScale( 2, RoundingMode.HALF_UP );
    }

    private static double getRET( double qtd, double taxa_r, double preco_venda, double desconto )
    {
        double subtotal_linha = (preco_venda * qtd);
        double valor_ret = (taxa_r / 100);//
        return ( ( subtotal_linha - desconto ) * valor_ret );

    }

    public static int getCodigoArmazem()
    {
        return armazensController.getArmazemByDesignacao( cmbArmazem.getSelectedItem().toString() ).getCodigo();
    }

    public static void procedimento_salvar_venda_comercial( boolean frNormal )
    {
        if ( MetodosUtil.tabela_vazia( table ) )
        {
            JOptionPane.showMessageDialog( null, "Caro usuário, adicione item na tabela", "Aviso", JOptionPane.WARNING_MESSAGE );
            return;
        }

        // Verifica se a data do documento é válida
        if ( !data_documento_superior_ou_igual_ao_ultimo_doc() )
        {
            JOptionPane.showMessageDialog( null, "A data do documento é inferior ao último documento emitido", "Aviso", JOptionPane.WARNING_MESSAGE );
            return;
        }

        // Mostra aviso se for necessário
        data_documento_superior_data_actual();
        if ( !aviso_continuar_documento )
        {
            return;
        }

        // Valida campos antes de prosseguir
        if ( campos_invalido_imprimir() )
        {
            return;
        }

        // Verifica se há algum erro lógico ou de transação anterior
//        if ( transtorno() )
//        {
//            return;
//        }
        // Moeda estrangeira?
        if ( !DVML.MOEDA_KWANZA.equalsIgnoreCase( getMoeda().getAbreviacao() ) )
        {
            actualizar_moeda( DVML.MOEDA_KWANZA );
        }

        // Agora chama o método que realmente salva a venda
        salvar_venda_comercial( frNormal );

        // Atualiza a data após a venda
        dc_data_documento.setDate( new Date() );
    }

    private static void salvar_venda_comercial( boolean frNormal )
    {
        BDConexao conexaoTransactionLocal = BDConexao.getInstancia();
        vendasController = new VendasController( conexaoTransactionLocal );
        itemVendasController = new ItemVendasController( conexaoTransactionLocal );
        formaPagamentoItemController = new FormaPagamentoItemController( conexaoTransactionLocal );
        pagamentoMensalidadeController = new PagamentoMensalidadeController( conexaoTransactionLocal.getConnectionAtiva() );
        precosController = new PrecosController( conexaoTransactionLocal );

        StoksController stocksControllerLocal = new StoksController( conexaoTransactionLocal );
        DocumentosController.start( conexaoTransactionLocal ); // Inicia a transação
        try
        {
            System.out.println( "AutoCommit após iniciar transação? "
                    + conexaoTransactionLocal.getConnection().getAutoCommit() );

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        Integer idVendaGerada = 0;
        try
        {
            // Construção do objeto venda
            TbVenda venda = construirVenda();

            // Salvar a venda e obter o ID
            idVendaGerada = vendasController.salvarRetornaID( venda );
//            venda.setHashCod( MetodosUtil.criptografia_hash( vendasController.findById( idVendaGerada), getGrossTotal().doubleValue(), conexaoTransaction ) );

            vendasController.actualizar_hash_and_assinatura( idVendaGerada, getGrossTotal().doubleValue() );

            if ( idVendaGerada == null || idVendaGerada == 0 )
            {
                throw new Exception( "Falha ao obter o ID da venda gravada." );
            }

            // Ações específicas por tipo de documento
            if ( getIdDocumento() == DOC_FACTURA_RECIBO_FR )
            {
                MetodosUtil.adicionar_saldo_banco( venda.getTotalVenda().doubleValue(), venda.getIdBanco().getIdBanco(), conexaoTransactionLocal );
            }

            if ( getIdDocumento() == DOC_FACTURA_FT )
            {
                ExtratoContaClienteController.registro_movimento_conta_cliente( venda, conexaoTransactionLocal );
            }

            // Salvar os itens da venda
            salvar_item_venda_comercial( idVendaGerada, conexaoTransactionLocal, stocksControllerLocal );

            // Registrar formas de pagamento
            if ( getIdDocumento() == DOC_FACTURA_RECIBO_FR )
            {
                registrarFormaPagamento( idVendaGerada, venda.getTotalVenda(), frNormal );

            }

            //actualizar precos antigos
            actualizarPrecosAntigos();

            // Finaliza transação
            DocumentosController.commit( conexaoTransactionLocal );

            JOptionPane.showMessageDialog( null, "Factura efectuada com sucesso!" );
            txtNomeConsumidorFinal.setVisible( true );
//            imprimir_factura( idVendaGerada ); // Imprime a factura

        }
        catch ( Exception e )
        {

            DocumentosController.rollback( conexaoTransactionLocal );
            e.printStackTrace();
            JOptionPane.showMessageDialog( null, "Erro ao processar a venda: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE );
        }
        finally
        {
            conexaoTransactionLocal.close();
        }

        // fora do try-catch da transação
        try
        {
            imprimir_factura( idVendaGerada );
        }
        catch ( Exception ex )
        {
            ex.printStackTrace();
            JOptionPane.showMessageDialog( null, "Erro ao imprimir factura: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE );
        }
    }

    private static FormaPagamentoItem criarItemFormaPagamento( int idVenda, int idForma, BigDecimal valor, BigDecimal troco, String referencia )
    {
        FormaPagamentoItem item = new FormaPagamentoItem();
        item.setValor( valor );
        item.setTroco( troco );
        item.setValor_real( valor.subtract( troco ) );
        item.setReferencia( referencia );
        item.setFkVenda( new TbVenda( idVenda ) );
        item.setFkFormaPagamento( new FormaPagamento( idForma ) );
        return item;
    }

    public static void registrarFormaPagamento( int idVenda, BigDecimal totalVenda, boolean formaNormal ) throws Exception
    {

        if ( !formaNormal )
        {
            registrarPagamentoUnico( idVenda, totalVenda );
            return;
        }
        registrarPagamentosMultiplos( idVenda );
    }

    private static void registrarPagamentoUnico( int idVenda, BigDecimal totalVenda ) throws Exception
    {
        BigDecimal valor = totalVenda;
        String descricao = "";
        String referencia = String.valueOf( idVenda );
        int idForma = 1;

        FormaPagamento forma = formaPagamentoController.findByCodigo( idForma );
        if ( forma == null )
        {
            throw new Exception( "Forma de pagamento não encontrada: " + descricao );
        }

        Contas conta = ( Contas ) contaController.findById( forma.getFkContaAssociada() );
        FormaPagamentoItem item = criarItemFormaPagamento( idVenda, idForma, valor, BigDecimal.ZERO, referencia );
        if ( !formaPagamentoItemController.salvar( item ) )
        {
            throw new Exception( "Erro ao salvar forma de pagamento: " + descricao );
        }

        if ( conta != null )
        {
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

    private static void registrarPagamentosMultiplos( int idVenda ) throws Exception
    {
        DefaultTableModel modelo = ( DefaultTableModel ) FormaPagamentoVisao.tabela_forma_pagamento.getModel();
        BigDecimal troco = CfMethods.parseMoedaSegura( FormaPagamentoVisao.lb_troco.getText() );

        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {

            BigDecimal valor = CfMethods.parseMoedaSegura( modelo.getValueAt( i, 3 ).toString() );
            if ( valor.compareTo( BigDecimal.ZERO ) <= 0 )
            {
                continue;
            }

            int idForma = Integer.parseInt( modelo.getValueAt( i, 0 ).toString() );
            String descricao = modelo.getValueAt( i, 1 ).toString();
            String referencia = modelo.getValueAt( i, 2 ) != null ? modelo.getValueAt( i, 2 ).toString() : "n/a";

            FormaPagamento forma = formaPagamentoController.findByDescrisao( descricao );
            if ( forma == null )
            {
                throw new Exception( "Forma de pagamento não encontrada: " + descricao );
            }

            Contas conta = ( Contas ) contaController.findById( forma.getFkContaAssociada() );

            FormaPagamentoItem item = criarItemFormaPagamento( idVenda, idForma, valor, troco, referencia );

            if ( !formaPagamentoItemController.salvar( item ) )
            {
                throw new Exception( "Erro ao salvar forma de pagamento: " + descricao );
            }

            if ( conta != null )
            {
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

            troco = BigDecimal.ZERO;
        }
    }

    /**
     * Cria um objeto FormaPagamentoItem já populado e com cálculos prontos.
     */
    private static void imprimir_factura( int cod_venda )
    {

        BDConexao conexoaLocal = BDConexao.getInstancia();
        limpar();
        remover_all_produto();
        accao_cliente();
        limpar_consumidor_final();
        txtNomeConsumidorFinal.setVisible( true );
        txtNomeConsumidorFinal.getText().equals( "Consumidor Final" );

        txtQuatindade.setText( "1" );
        txtQuatindade.requestFocus();
        txtQuantidadeStock.setText( String.valueOf( conexoaLocal.getQtdExistenteStock( getCodigoProduto(), getCodigoArmazem() ) ) );

        List<TbProduto> lista_produto_isentos = getProdutosIsentos();
        String motivos_isentos = MetodosUtil.getMotivoIsensaoProdutos( lista_produto_isentos );
        conexoaLocal.close();
        int numeroVias = ( int ) Double.parseDouble( spnCopia.getValue().toString() );

        for ( int i = 1; i <= numeroVias; i++ )
        {
            String via;
            switch (i)
            {
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
            if ( ( getIdDocumento() == DOC_FACTURA_RECIBO_FR || getIdDocumento() == DOC_FACTURA_FT || getIdDocumento() == DVML.DOC_FACTURA_PROFORMA_PP ) )
            {

                ListaVenda1 listaVenda1 = new ListaVenda1( cod_venda, abreviacao, false, ck_simplificada.isSelected(), via, motivos_isentos );
            }
            else
            {
                ListaVendaConsultas listaVenda1 = new ListaVendaConsultas( cod_venda, abreviacao, false, ck_simplificada.isSelected(), via );
            }
        }
    }

    private static void accao_cliente()
    {
        String nomeCliente = ( String ) cmbCliente.getSelectedItem();

        txtNomeConsumidorFinal.setText( nomeCliente );
        String nif = clientesController.findByNome( nomeCliente ).getNif();
        txtNifClientePesquisa.setText( nif );
    }
    
    private static void accao_cliente_tel()
    {
        String nomeCliente = ( String ) cmbCliente.getSelectedItem();

        txtNomeConsumidorFinal.setText( nomeCliente );
        String telefone = clientesController.findByNome( nomeCliente ).getTelefone();
        txtTelClientePesquisa.setText( telefone );
    }

    private static void limpar_consumidor_final()
    {

        if ( cmbCliente.getSelectedItem().equals( "Consumidor Final" ) )
        {
//            lbClienteConsumidorFinal.setVisible( true );
            txtNomeConsumidorFinal.setVisible( true );
        }
        else
        {
//            lbClienteConsumidorFinal.setVisible( false );
            txtNomeConsumidorFinal.setVisible( false );
        }

    }

    private static void data_documento_superior_data_actual()
    {

        //retirando a data do documento
        Date data_documento = dc_data_documento.getDate();
        //pegando a data actual do sistema 
        Date data_sistema = new Date();
        //comparar as datas
        if ( MetodosUtil.maior_data_1_data_2( data_documento, data_sistema ) )
        {
            JOptionPane.showMessageDialog( null, "Após essa emissão, não poderá ser emitido um novo documento\n "
                    + "com a data actual ou anterior, dentro da mesma série.",
                    "AVISO", JOptionPane.WARNING_MESSAGE );

            aviso_continuar_documento = JOptionPane.showConfirmDialog( null, "Ainda assim deseja continuar com a operação ?" )
                    == JOptionPane.YES_OPTION;

        }
        else
        {
            aviso_continuar_documento = true;
        }

    }

    private static List<TbProduto> getProdutosIsentos()
    {
        DefaultTableModel modelo = ( DefaultTableModel ) table.getModel();
        double taxa = 0.0;
        int codigo_produto = 0;
        List<TbProduto> lista_produtos_isentos = new ArrayList<>();
        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {
            codigo_produto = Integer.parseInt( modelo.getValueAt( i, 0 ).toString() );
            taxa = Double.parseDouble( modelo.getValueAt( i, 6 ).toString() );
            if ( taxa == 0.0 )
            {
                lista_produtos_isentos.add( ( TbProduto ) produtosController.findById( codigo_produto ) );
            }
        }

        return lista_produtos_isentos;

    }

    private static Date getDataVencimentoFr()
    {

        if ( ( getIdDocumento() == DVML.DOC_FACTURA_PROFORMA_PP ) || ( getIdDocumento() == DVML.DOC_FACTURA_FT ) )
        {
            return dc_data_vencimento.getDate();
        }
        else
        {
            return new Date();
        }
    }

    private static TbVenda construirVenda()
    {
        TbVenda venda = new TbVenda();

        // Datas
        venda.setDataVenda( dc_data_documento.getDate() );
        venda.setHora( new java.sql.Time( System.currentTimeMillis() ) );
        venda.setDataVencimento( getDataVencimentoFr() ); // se aplicável
//        venda.setDataVencimento( dc_data_vencimento.getDate() ); // se aplicável
        venda.setRefDataFact( dc_data_documento.getDate() );       // se aplicável

        // Totais
        venda.setTotalVenda( getTotalAOALiquido() );
        venda.setValorEntregue( getValor_entregue() );
        venda.setTroco( new BigDecimal( getTroco() ) );
        venda.setDescontoTotal( FinanceUtils.getDescontoComercial( INDEX_TABLE_PRECO, INDEX_TABLE_QTD, INDEX_TABLE_DESCONTO, table )
                .add( BigDecimal.valueOf( getDescontoFinanceiro() ) ) );

        venda.setDescontoComercial(
                FinanceUtils.getDescontoComercial( INDEX_TABLE_PRECO, INDEX_TABLE_QTD, INDEX_TABLE_DESCONTO, table ) );
        venda.setDescontoFinanceiro( new BigDecimal( getDescontoFinanceiro() ) );
        venda.setTotalIva( new BigDecimal(
                FinanceUtils.getTotalImpostoTable(
                        INDEX_TABLE_PRECO,
                        INDEX_TABLE_QTD,
                        INDEX_TABLE_DESCONTO,
                        INDEX_TABLE_TAXA_IVA, table ) )
        );
        venda.setTotalGeral(
                FinanceUtils.getTotalIliquidoTable(
                        INDEX_TABLE_PRECO,
                        INDEX_TABLE_QTD,
                        table ) );

        venda.setTotalIncidencia( new BigDecimal( FinanceUtils.getTotalIncidenciaTable(
                INDEX_TABLE_PRECO,
                INDEX_TABLE_QTD,
                INDEX_TABLE_DESCONTO,
                INDEX_TABLE_TAXA_IVA,
                table ) )
        );

        venda.setTotalIncidenciaIsento( FinanceUtils.getTotalIncidenciaIsento(
                INDEX_TABLE_PRECO,
                INDEX_TABLE_QTD,
                INDEX_TABLE_DESCONTO, INDEX_TABLE_TAXA_IVA, table ) );

        venda.setTotalRetencao( getTotalRetencaoLiquido() );
        venda.setGorjeta( new BigDecimal( gorjeta ) );
        venda.setTotalPorExtenso( iniciais_extenso() + lbValorPorExtenco.getText() );

        // Strings
        if ( getIdDocumento() == DOC_FACTURA_RECIBO_FR && getIdCliente() == 1 )
        {

            venda.setNomeCliente( txtNomeConsumidorFinal.getText().trim() );
            venda.setClienteNif( txtNifClientePesquisa.getText().trim() );
        }
        else if ( getIdDocumento() == DOC_FACTURA_RECIBO_FR && getIdCliente() > 1 )
        {

            venda.setNomeCliente( getNomeCliente() );
            venda.setClienteNif( getClienteNif() );
        }
        else if ( getIdDocumento() != DOC_FACTURA_RECIBO_FR && getIdCliente() > 1 )
        {
            venda.setNomeCliente( getNomeCliente() );
            venda.setClienteNif( getClienteNif() );
        }

        venda.setCodFact( prox_doc );
//    venda.setRefCodFact(txtRefCodFact.getText());
        venda.setPerformance( "false" ); // ou pegar de um campo
        venda.setCredito( "false" );        // depende se venda é a crédito
        venda.setHashCod( "" );           // actualiza o hash depois de salvar
//        venda.setHashCod( MetodosUtil.criptografia_hash( venda, getGrossTotal().doubleValue(), conexaoTransaction ) );
//        venda.setAssinatura( MetodosUtil.assinatura_doc( venda.getHashCod() ) );
        venda.setAssinatura( "" );        // preencher se estiver em uso
        venda.setObs( "" );
        venda.setStatusEliminado( "false" );
        venda.setStatusRecibo( false ); // ou true se for o caso

        // Cliente / usuário / banco / armazém
        venda.setIdBanco( new TbBanco( 1 ) );
        venda.setCodigoUsuario( new TbUsuario( cod_usuario ) );
        venda.setCodigoCliente( new TbCliente( getIdCliente() ) );
        venda.setIdArmazemFK( new TbArmazem( getCodigoArmazem() ) );

        // Documento, ano econômico, câmbio
        venda.setFkDocumento( new Documento( getIdDocumento() ) );
        venda.setFkAnoEconomico( new AnoEconomico( getIdAnoEconomico() ) );

        int id = cambiosController.getLastId( getIdMoeda() );
        venda.setFkCambio( new Cambio( id ) );

        // Dados adicionais do carro (se aplicável)
        venda.setLocalCarga( "" );
        venda.setLocalDescarga( "" );
        venda.setNomeConsumidorFinal( txtNomeConsumidorFinal.getText() );
        venda.setReferencia( txtReferencia.getText() );
        venda.setMatricula( "" );
        venda.setModelo( "" );
        venda.setNumChassi( "" );
        venda.setNumMotor( "" );
        venda.setKilometro( "" );
        venda.setNomeMotorista( "" );
        venda.setMarcaCarro( "" );
        venda.setCorCarro( "" );
        venda.setNDocMotorista( "" );

        // Contador do documento
        venda.setCont( 0 ); // por exemplo

        return venda;
    }

    public static String getMes( String designacao )
    {

        try
        {
            return designacao.split( "#" )[ 1 ];
        }
        catch ( Exception e )
        {
        }

        return "N/S";

    }

    public static void salvar_item_venda_comercial( Integer cod_venda, BDConexao conexaoLocal, StoksController stoksControllerLocal ) throws Exception
    {
        for ( int i = 0; i < table.getRowCount(); i++ )
        {
            try
            {
                int idProduto = Integer.parseInt( table.getModel().getValueAt( i, 0 ).toString() );
                String designacaoItem = table.getModel().getValueAt( i, 1 ).toString();

                TbProduto produto = ( TbProduto ) produtosController.findById( idProduto );

                TbItemVenda item = new TbItemVenda();
                item.setCodigoVenda( new TbVenda( cod_venda ) );
                item.setCodigoProduto( produto );
                item.setDesignacaoItem( designacaoItem );
                item.setQuantidade( Double.parseDouble( table.getModel().getValueAt( i, 4 ).toString() ) );
                item.setDesconto( Double.parseDouble( table.getModel().getValueAt( i, 5 ).toString() ) );
                item.setValorIva( Double.parseDouble( table.getModel().getValueAt( i, 6 ).toString() ) );
                item.setValorRetencao( Double.parseDouble( table.getModel().getValueAt( i, 7 ).toString() ) );
                TbPreco precoProduto = precosController.getLastIdPrecoByIdProduto( idProduto, item.getQuantidade() );
                item.setMotivoIsensao( getMotivoIsensao( idProduto ) );
                item.setCodigoIsensao( MetodosUtil.getCodigoRegime( idProduto ) );
                item.setTotal( new BigDecimal( CfMethods.parseMoedaFormatada( table.getModel().getValueAt( i, 10 ).toString() ) ) );
                item.setFkPreco( precosController.getLastIdPrecoByIdProduto( idProduto, item.getQuantidade() ) );
                item.setDataServico( new Date() );
                item.setFkLugares( ( TbLugares ) lugaresController.findById( DVML.LUGAR_BALCAO ) );
                item.setFkMesas( ( TbMesas ) mesasController.findById( DVML.MESA_BALCAO ) );
                item.setDesignacaoItem( designacaoItem );
                // Salvar item
                if ( !itemVendasController.salvar( item ) )
                {
                    throw new Exception( "Erro ao salvar item da venda. Produto: " + produto.getDesignacao() );
                }
                else
                {
                    String mes = getMes( designacaoItem );
                    if ( !mes.equals( "N/S" ) )
                    {
                        procedimentoPagamentoMensalidadeServico( cod_venda, getIdCliente(), idProduto, mes, conexaoLocal );
                        System.out.println( "Pagmento de mensalidade pago com sucesso!.." );
                    }
                }

                int idArmazem = getCodigoArmazem();
                // Controle de stock (se for estocável)
                boolean isStocavel = "true".equalsIgnoreCase( produto.getStocavel() );
                if ( isStocavel )
                {

                    TbStock stock_local_local = stoksControllerLocal.getStockByIdProdutoAndIdArmazem( idProduto, idArmazem );

                    if ( ( getIdDocumento() == DOC_FACTURA_RECIBO_FR
                            || getIdDocumento() == DOC_FACTURA_FT
                            || getIdDocumento() == DOC_FACTURA_CONSULTA_MESA
                            || getIdDocumento() == DVML.DOC_GUIA_TRANSPORTE_GT ) && stock_local_local != null )
                    {

                        MovimentacaoController.registrarMovimento(
                                idProduto,
                                idArmazem,
                                cod_usuario,
                                new BigDecimal( item.getQuantidade() ),
                                prox_doc,
                                "SAIDA",
                                conexao
                        );

                        if ( getIdDocumento() == DOC_FACTURA_RECIBO_FR || getIdDocumento() == DOC_FACTURA_FT )
                        {
                            actualizar_quantidade( idProduto, item.getQuantidade(), conexaoLocal );
                        }
                    }
                }

            }
            catch ( Exception e )
            {
                throw new Exception( "Erro ao processar item " + ( i + 1 ) + ": " + e.getMessage() );
            }
        }
    }

    private static boolean procedimentoPagamentoMensalidadeServico( int vendaId, int idCliente, int idProduto, String mes, BDConexao conexaoParm )
    {

        mesRhController = new MesRhController( conexaoParm.getConnectionAtiva() );
        int mesId = mesRhController.getIdByDescricao( mes );
        PagamentoMensalidade pagamentoMensalidade = new PagamentoMensalidade();
        pagamentoMensalidade.setClienteId( idCliente );
        pagamentoMensalidade.setProdutoId( idProduto );
        pagamentoMensalidade.setMesId( mesId );
        pagamentoMensalidade.setVendaId( vendaId );
        pagamentoMensalidade.setDataCadastro( new Date() );

        return pagamentoMensalidadeController.salvar( pagamentoMensalidade );

    }

    private static boolean data_documento_superior_ou_igual_ao_ultimo_doc()
    {
        //buscando o id do documento.
        int pk_documento = getIdDocumento();
        //buscando o id do ano ecoonomico.
        int pk_ano_economico = getIdAnoEconomico();

        //busca o último documento da série em questão.
        // Integer cod_ultima_venda = vendaDao.getLastVenda( pk_documento );
        Integer cod_ultima_venda = vendasController.getLastCodigoVenda( pk_documento, pk_ano_economico );
        if ( cod_ultima_venda != 0 )
        {

            //busca o objecto para retirar apenas a data do seu procesamento
            TbVenda venda_local = ( TbVenda ) vendasController.findById( cod_ultima_venda );
            //retirando a data do documebto
            Date data_ultimo_documento = venda_local.getDataVenda();
            //pegando a data do documento (data actual do sistema)
            Date data_actual = dc_data_documento.getDate();
            return MetodosUtil.maior_data_1_data_2( data_actual, data_ultimo_documento )
                    || MetodosUtil.igual_data_1_data_2( data_actual, data_ultimo_documento );

        }
        else
        {
            return true;
        }

    }

    private static String getNomeCliente()
    {
        return cmbCliente.getSelectedItem().toString();
    }

    private static String getClienteNif()
    {
        try
        {
            TbCliente cliente = ( TbCliente ) clientesController.findById( getIdCliente() );

            String nif = cliente.getNif();
            System.out.println( "NIF CLIENTE: " + nif );
            if ( nif.equals( "" ) )
            {
                return DVML.NUMBER_NIF_GENERICO;
            }
            return nif;
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            return "";
        }
    }

    public static int getCodigoProduto()
    {
        //return conexao.getCodigoPublico("tb_produto", String.valueOf(  cmbProduto.getSelectedItem()));   
        return produtosController.findByDesignacao( cmbProduto.getSelectedItem().toString() ).getCodigo();

    }

    public static BigDecimal getValor_entregue()
    {
        return getTotalAOALiquido();
    }

    public static double getTroco()
    {

        try
        {
            return CfMethods.parseMoedaFormatada( FormaPagamentoVisao.lb_troco.getText() );
        }
        catch ( Exception e )
        {
        }

        return 0d;

    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelar;
    private static javax.swing.JButton btnFormaPagamento;
    private static javax.swing.JButton btnProcessar;
    private javax.swing.JButton btnSemFormaPagamento;
    public static javax.swing.JButton btn_adicionar;
    public static javax.swing.JButton btn_remover;
    private javax.swing.JCheckBox ck_A4;
    public static javax.swing.JCheckBox ck_A7;
    public static javax.swing.JCheckBox ck_ComVirgula;
    private javax.swing.JCheckBox ck_Duplicada;
    public static javax.swing.JCheckBox ck_S_A6;
    public static javax.swing.JCheckBox ck_simplificada;
    public static javax.swing.JCheckBox ck_simplificada_O;
    public static javax.swing.JCheckBox ck_simplificada_O_S;
    private static javax.swing.JComboBox<String> cmbAnoEconomico;
    public static javax.swing.JComboBox cmbArmazem;
    public static javax.swing.JComboBox cmbCliente;
    private static javax.swing.JComboBox cmbMoeda;
    public static javax.swing.JComboBox cmbProduto;
    public static javax.swing.JComboBox cmbSubFamilia;
    public static javax.swing.JComboBox cmbTipoDocumento;
    private static com.toedter.calendar.JDateChooser dc_data_documento;
    private static com.toedter.calendar.JDateChooser dc_data_vencimento;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel jlEmpresa;
    private javax.swing.JLabel jlStockNegativo;
    private javax.swing.JLabel lbCliente;
    private javax.swing.JLabel lbCliente1;
    private javax.swing.JLabel lbClienteConsumidorFinal;
    private javax.swing.JLabel lbClienteConsumidorFinal1;
    private javax.swing.JLabel lbCodigoProduto;
    private javax.swing.JLabel lbCodigoProduto1;
    private javax.swing.JLabel lbCodigoProduto2;
    private javax.swing.JLabel lbCodigoProduto3;
    private javax.swing.JLabel lbCodigoProduto4;
    private javax.swing.JLabel lbDescontoFinanceiro;
    private javax.swing.JLabel lbQuantidade;
    private javax.swing.JLabel lbQuantidadeStock;
    public static javax.swing.JLabel lbValorPorExtenco;
    private javax.swing.JLabel lb_nome_usuario;
    private static javax.swing.JLabel lb_proximo_documento;
    private javax.swing.JPanel painelDir;
    private javax.swing.JPanel painelEsq;
    private javax.swing.JPanel painelTabela;
    private javax.swing.JPanel painelTopo;
    private static javax.swing.JRadioButton rbArmazem;
    private static javax.swing.JRadioButton rbArmazem1;
    private static javax.swing.JRadioButton rbMostrar;
    private static javax.swing.JRadioButton rbTranstorno;
    private static javax.swing.JSpinner sp_desconto_financeiro;
    private static javax.swing.JSpinner spnCopia;
    private static javax.swing.JTable table;
    private javax.swing.JTextField txtCodClientePesquisa;
    public static javax.swing.JTextField txtCodigoBarra;
    public static javax.swing.JTextField txtCodigoManual;
    public static javax.swing.JTextField txtCodigoProduto;
    private static javax.swing.JTextField txtIniciaisCliente;
    private javax.swing.JTextField txtLocal;
    private static javax.swing.JTextField txtNifClientePesquisa;
    private static javax.swing.JTextField txtNomeConsumidorFinal;
    public static javax.swing.JTextField txtPreco;
    public static javax.swing.JTextField txtQuantidadeStock;
    public static javax.swing.JTextField txtQuatindade;
    private static javax.swing.JTextField txtReferencia;
    private static javax.swing.JTextField txtTelClientePesquisa;
    public static javax.swing.JTextField txtTotalPagar;
    private static javax.swing.JLabel txtTotal_AOA_Retencao;
    // End of variables declaration//GEN-END:variables

    public boolean validar()
    {
        boolean documentoIsFA = DVML.DOC_FACTURA_FT == getIdDocumento();
        boolean documentoIsPP = DVML.DOC_FACTURA_PROFORMA_PP == getIdDocumento();
        boolean seguradoras = DVML.DOC_FACTURA_PROFORMA_PP == getIdDocumento();
        if ( cmbCliente.getSelectedItem().equals( "--Seleccione o Cliente--" ) )
        {

            JOptionPane.showMessageDialog( null, "Por favor, Seleccione ou Digite o nome do Cliente!" );
            txtIniciaisCliente.requestFocus();
            txtIniciaisCliente.setBackground( Color.YELLOW );
            return false;

        }
        else if ( documentoIsFA && dc_data_vencimento.getDate() == null || documentoIsPP && dc_data_vencimento.getDate() == null )
        {

            JOptionPane.showMessageDialog( null, "Por favor, Seleccione a data de Vencimento para este cliente!" );
            dc_data_vencimento.requestFocus();
            dc_data_vencimento.setBackground( Color.YELLOW );
            return false;

        }

        txtIniciaisCliente.setBackground( Color.WHITE );
        return true;
    }

    private void accao_codigo_interno_enter()
    {

        try
        {

            int codigo = Integer.parseInt( txtCodigoProduto.getText() );
            TbProduto produto = ( TbProduto ) produtosController.findById( codigo );

            Integer codTipoProduto = produto.getCodTipoProduto().getCodigo();
            TbTipoProduto tipoProduto = ( TbTipoProduto ) tipoProdutoController.findById( codTipoProduto );
            Integer codFamilia = tipoProduto.getFkFamilia().getPkFamilia();
            Familia familia = ( Familia ) familiaController.findById( codFamilia );
            cmbSubFamilia.setSelectedItem( tipoProduto.getDesignacao() );

            cmbProduto.setModel( new DefaultComboBoxModel( produtosController.getVector() ) );
            cmbProduto.setSelectedItem( produto.getDesignacao() );

            configuracaoMesComecoController = new ConfiguracaoMesComecoController( conexao.getConnectionAtiva() );

            System.out.println( "***** Chegue 0" );
            boolean existeConfiguracaoDoCliente = configuracaoMesComecoController
                    .existeConfiguracaoDoCliente(
                            getIdCliente(),
                            getCodigoProduto() );

            if ( existeConfiguracaoDoCliente )
            {
                new MesesPagoClienteVisao( this,
                        rootPaneCheckingEnabled,
                        getIdCliente(),
                        getCodigoProduto(), conexao ).setVisible( true );
            }
            else
            {
                procedimentoAdicionarTabela( produto );

            }

        }
        catch ( Exception ex )
        {
            Logger.getLogger( FormVendaResponsivaVisaoTop.class.getName() ).log( Level.SEVERE, null, ex );
            JOptionPane.showMessageDialog( null, "Este produto não existe no armazém " + cmbArmazem.getSelectedItem(), DVML.DVML_COMERCIAL, JOptionPane.ERROR_MESSAGE );
        }
        finally
        {
        }

    }

    private void refresh_table()
    {

        DefaultTableModel modelo = ( DefaultTableModel ) table.getModel();

        double preco = 0, desconto = 0, sub_total_linha = 0, sub_total_linha_com_iva = 0, taxa = 0, taxa_r = 0, sub_total_linha_retencao = 0;
        int idProduto, qtd;

        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {

            try
            {

                /**
                 * Recupera os valores da tabela
                 */
                idProduto = Integer.parseInt( modelo.getValueAt( i, 0 ).toString() );
                qtd = Integer.parseInt( modelo.getValueAt( i, 4 ).toString() );
                taxa = Double.parseDouble( modelo.getValueAt( i, 6 ).toString() );
                taxa_r = Double.parseDouble( modelo.getValueAt( i, 7 ).toString() );
                //desconto_percentagem = Double.parseDouble( modelo.getValueAt( i, 4 ).toString() );

                //busca o preço em função do câmbio
                preco = getPreco( idProduto, qtd );

                /**
                 * Processa os valores para serem actualizados na tabela
                 */
                desconto = getDesconto_produto( preco, qtd );

                sub_total_linha = ( preco * qtd ) - desconto;
                sub_total_linha_com_iva = getValorComImposto( qtd, getTaxaImposto( idProduto ), preco, desconto );

                /**
                 * actualiza os valores na tabela
                 */
                modelo.setValueAt( CfMethods.formatarComoMoeda( preco ), i, 3 );
                modelo.setValueAt( CfMethods.formatarComoMoeda( desconto ), i, 5 );
                modelo.setValueAt( CfMethods.formatarComoMoeda( sub_total_linha_retencao ), i, 8 );
                modelo.setValueAt( CfMethods.formatarComoMoeda( sub_total_linha ), i, 9 );
                modelo.setValueAt( CfMethods.formatarComoMoeda( sub_total_linha_com_iva ), i, 10 );

            }
            catch ( Exception e )
            {
                e.printStackTrace();
            }

        }
        setTotalRetencao();
        setTotalPagar();
        calculaTotalIVA();
        valor_por_extenco();

    }

    private static void calculaTotalIVA()
    {
        DefaultTableModel modelo = ( DefaultTableModel ) table.getModel();
        BigDecimal totalIva = BigDecimal.ZERO;

        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {
            Object precoObj = modelo.getValueAt( i, 3 );
            Object qtdObj = modelo.getValueAt( i, 4 );
            Object ivaObj = modelo.getValueAt( i, 6 );

            // Linha incompleta → ignora
            if ( precoObj == null || qtdObj == null || ivaObj == null )
            {
                continue;
            }

            String precoTxt = precoObj.toString().trim();
            String qtdTxt = qtdObj.toString().trim();
            String ivaTxt = ivaObj.toString().trim();

            // Células vazias → ignora linha
            if ( precoTxt.isEmpty() || qtdTxt.isEmpty() || ivaTxt.isEmpty() )
            {
                continue;
            }

            // --------------- PARSE SEGURO ---------------
            BigDecimal preco;
            try
            {
                preco = BigDecimal.valueOf( CfMethods.parseMoedaFormatada( precoTxt ) );
            }
            catch ( Exception e )
            {
                continue; // Preço inválido → ignora linha
            }

            BigDecimal quantidade;
            try
            {
                quantidade = new BigDecimal( qtdTxt );
            }
            catch ( Exception e )
            {
                continue; // Quantidade inválida → ignora linha
            }

            BigDecimal taxaIva;
            try
            {
                taxaIva = new BigDecimal( ivaTxt );
            }
            catch ( Exception e )
            {
                taxaIva = BigDecimal.ZERO;
            }

            BigDecimal desconto = BigDecimal.ZERO;

            // cálculo
            BigDecimal valorIva = getIVA( quantidade, taxaIva, preco, desconto );
            totalIva = totalIva.add( valorIva );
        }

//    total_iva = MetodosUtil.valorCasasDecimaisNovoBD(totalIva.doubleValue(), CASAS_DECIMAIS);
        total_iva = MetodosUtil.valorCasasDecimaisNovo( totalIva.doubleValue(), CASAS_DECIMAIS );
        System.out.println( "(*)TOTAL IVA: " + total_iva );
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
    private static void refresh_table( int idMoeda )
    {

        Moeda moeda_local = ( Moeda ) moedasController.findById( idMoeda );
        DefaultTableModel modelo = ( DefaultTableModel ) table.getModel();

        double preco = 0, desconto = 0, sub_total_linha = 0, sub_total_linha_com_iva = 0, taxa = 0, taxa_r = 0;
        int idProduto, qtd;

        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {

            try
            {

                /**
                 * Recupera os valores da tabela
                 */
                idProduto = Integer.parseInt( modelo.getValueAt( i, 0 ).toString() );
                qtd = Integer.parseInt( modelo.getValueAt( i, 4 ).toString() );
                taxa = Double.parseDouble( modelo.getValueAt( i, 6 ).toString() );
                taxa_r = Double.parseDouble( modelo.getValueAt( i, 7 ).toString() );

                //busca o preço em função do câmbio em funcção da moeda
                preco = getPreco( idProduto, qtd, idMoeda );

                /**
                 * Processa os valores para serem actualizados na tabela
                 */
                desconto = getDesconto_produto( preco, qtd );
                sub_total_linha = ( preco * qtd ) - desconto;
                sub_total_linha_com_iva = getValorComImposto( qtd, getTaxaImposto( idProduto ), preco, desconto );

                /**
                 * actualiza os valores na tabela
                 */
                modelo.setValueAt( CfMethods.formatarComoMoeda( preco ), i, 3 );
                modelo.setValueAt( CfMethods.formatarComoMoeda( desconto ), i, 5 );
                modelo.setValueAt( CfMethods.formatarComoMoeda( sub_total_linha ), i, 8 );
                modelo.setValueAt( CfMethods.formatarComoMoeda( sub_total_linha_com_iva ), i, 9 );

            }
            catch ( Exception e )
            {
                e.printStackTrace();
            }

        }

        setTotalPagar();
        setTotalRetencao();
        calculaTotalIVA();
        valor_por_extenco( moeda_local );

    }

    private static void procedimentoAdicionarTabela( TbProduto produto )
    {
        try
        {
            if ( !Objects.isNull( produto ) )
            {
                adicionar_preco_quantidade_anitgo();
                if ( rbTranstorno.isSelected() )
                {
                    procedimento_adicionar_sem_transtorno( "" );
                }
                else
                {
                    procedimento_adicionar( "" );
                }
                txtCodigoProduto.setText( "" );
                txtCodigoBarra.setText( "" );
                txtQuatindade.setText( "1" );
                txtQuatindade.requestFocus();

            }
            else
            {
                JOptionPane.showMessageDialog( null, "Nao existe produto/servico relacionado a esta referencia" );
            }

        }
        catch ( Exception e )
        {
            e.printStackTrace();

        }

    }

    public static void procedimento_adicionar_sem_transtorno( String mes )
    {
        try
        {
            if ( !campos_invalidos() )
            {
                if ( !isProdutoExpirado( getCodigoProduto() ) )
                {
                    TbProduto produto = ( TbProduto ) produtosController.findById( getCodigoProduto() );
                    if ( isStocavel( produto.getStocavel() ) )
                    {

                        if ( estado_critico() )
                        {
                            JOptionPane.showMessageDialog( null, "O produto: " + produto.getDesignacao() + " precisa de ser actualizado no stock", DVML.DVML_COMERCIAL, JOptionPane.WARNING_MESSAGE );
                        }
                        adicionar_produto( mes );

                    }
                    else
                    {
                        adicionar_produto( mes );
                    }
                }
                else
                {
                    JOptionPane.showMessageDialog( null, "Impossivel adicionar o produto porque já foi expirado.", "Aviso", JOptionPane.WARNING_MESSAGE );
                }

            }
            else
            {
                JOptionPane.showMessageDialog( null, "Por Favor Digite a Quantidade" );
            }

        }
        catch ( SQLException ex )
        {

        }

    }

    public static void procedimento_adicionar( String mes )
    {
        boolean documentoIsFA = DVML.DOC_FACTURA_FT == getIdDocumento();
        boolean documentoIsPP = DVML.DOC_FACTURA_PROFORMA_PP == getIdDocumento();
        boolean documentoIsFR = DVML.DOC_FACTURA_RECIBO_FR == getIdDocumento();

        try
        {

            if ( !campos_invalidos() )
            {
                if ( !isProdutoExpirado( getCodigoProduto() ) )
                {
                    TbProduto produto = ( TbProduto ) produtosController.findById( getCodigoProduto() );
                    System.out.println( "PRODUTO STOCAVEL: " + produto.getStocavel() );
                    boolean valorStocacel = isStocavel( produto.getStocavel() );
                    System.out.println( "VALOR STOCACEL" + valorStocacel );
                    if ( isStocavel( produto.getStocavel() ) && ( documentoIsFR || documentoIsFA ) )
                    {
                        if ( possivel_quantidade() )
                        {
                            if ( estado_critico() )
                            {
                                JOptionPane.showMessageDialog( null, "O produto: " + produto.getDesignacao() + " precisa de ser actualizado no stock", DVML.DVML_COMERCIAL, JOptionPane.WARNING_MESSAGE );
                            }
                            adicionar_produto( mes );
                        }
                        else
                        {
                            JOptionPane.showMessageDialog( null, "O produto: " + produto.getDesignacao() + " não pode ser vendido pra esta quantidade", DVML.DVML_COMERCIAL, JOptionPane.ERROR_MESSAGE );
                        }
                    }
                    else
                    {
                        adicionar_produto( mes );
                    }
                }
                else
                {
                    JOptionPane.showMessageDialog( null, "Impossivel adicionar o produto porque já foi expirado.", "Aviso", JOptionPane.WARNING_MESSAGE );
                }
            }
            else
            {
                JOptionPane.showMessageDialog( null, "Por Favor Digite a Quantidade" );
            }

        }
        catch ( SQLException ex )
        {

        }

    }

    private static boolean isProdutoExpirado( int codigoProduto )
    {
        // return produtoDao.produtoExpirado( codigoProduto );
        return false;

    }

    private void accao_codigo_barra_enter()
    {
        try
        {
            String codigo_barra = txtCodigoBarra.getText().trim();
            TbProduto produtoLocal = produtosController.findByCodBarra( codigo_barra );
            procedimentoAdicionarTabela( produtoLocal );
        }
        catch ( Exception ex )
        {
            ex.printStackTrace();
            Logger.getLogger( FormVendaResponsivaVisaoTop.class.getName() ).log( Level.SEVERE, null, ex );
            JOptionPane.showMessageDialog( null, "Não existe produto com este código de barra.", DVML.DVML_COMERCIAL, JOptionPane.ERROR_MESSAGE );
        }

    }

    public void adicionar_botao_retificar( String mes )
    {

        try
        {
            if ( !campos_invalidos() )
            {

                TbProduto produto = ( TbProduto ) produtosController.findById( getCodigoProduto() );
                if ( isStocavel( produto.getStocavel() ) )
                {

                    if ( possivel_quantidade() )
                    {

                        if ( estado_critico() )
                        {
                            JOptionPane.showMessageDialog( null, "O produto: " + produto.getDesignacao() + " precisa de ser actualizado no stock", DVML.DVML_COMERCIAL, JOptionPane.WARNING_MESSAGE );
                        }
                        adicionar_produto( mes );
                    }
                    else
                    {
                        JOptionPane.showMessageDialog( null, "O produto: " + produto.getDesignacao() + " nao pode ser vendido pra esta quantidade", DVML.DVML_COMERCIAL, JOptionPane.ERROR_MESSAGE );
                    }

                }
                else
                {
                    adicionar_produto( mes );
                }

            }
            else
            {
                JOptionPane.showMessageDialog( null, "Por Favor Digite a Quantidade" );
            }

        }
        catch ( SQLException ex )
        {

        }

    }

    public static void adicionar_produto( String mes ) throws SQLException
    {
        DefaultTableModel modelo = ( DefaultTableModel ) table.getModel();
        table.setRowHeight( 28 );

        // --------------------------
        // 1. FECHAR EDIÇÃO ATUAL
        // --------------------------
        if ( table.isEditing() )
        {
            TableCellEditor editor = table.getCellEditor();
            if ( editor != null )
            {
                editor.stopCellEditing();
            }
        }

        int codigoProdutoLocal = getCodigoProduto();
        String descricao_produto = getDescricao_Produto();

        if ( !mes.equals( "" ) )
        {
            descricao_produto = "Pgt. Ref. de " + descricao_produto + " de #" + mes;
        }

        // Evitar produto duplicado
        if ( exist_produto_tabela_formulario( descricao_produto ) )
        {
            JOptionPane.showMessageDialog( null, "O produto já consta na tabela." );
            return;
        }

        // Quantidade inválida
        if ( validar_zero() )
        {
            JOptionPane.showMessageDialog( null, "Atenção\nA quantidade a sair não pode ser igual a zero!" );
            return;
        }

        // --------------------------
        // 2. CÁLCULOS
        // --------------------------
        String unidade = getUnidade_Produto();

        BigDecimal qtd = BigDecimal.valueOf( getQuantidade() );
        BigDecimal preco = BigDecimal.valueOf( getPreco() );
        BigDecimal descontoPercent = BigDecimal.valueOf( getDescontoPercentagem() );
        BigDecimal taxaIva = BigDecimal.valueOf( getTaxaImpostoIva( codigoProdutoLocal ) );
        BigDecimal taxaRet = BigDecimal.valueOf( getTaxaImpostoRet( codigoProdutoLocal ) );

        BigDecimal valorIliquidoUnit = FinanceUtils.getValorIliquido(
                BigDecimal.ONE, preco, descontoPercent
        );

        double valorLiquidoDoubleUnit = FinanceUtils.getValorComIVA(
                1d,
                taxaIva.doubleValue(),
                valorIliquidoUnit.doubleValue(),
                descontoPercent.doubleValue()
        );

        BigDecimal valorIliquido = valorIliquidoUnit.multiply( qtd );
        BigDecimal totalComIva = BigDecimal.valueOf( valorLiquidoDoubleUnit * qtd.doubleValue() );

        String totalRetencao = CfMethods.formatarComoMoeda(
                MetodosUtil.getValorComRetencao(
                        qtd.doubleValue(),
                        taxaRet.doubleValue(),
                        preco.doubleValue(),
                        descontoPercent.doubleValue()
                )
        );

        // --------------------------
        // 3. Obter PRIMEIRA linha vazia
        // --------------------------
        int row = -1;
        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {
            if ( linhaTabelaEstaVazia( modelo, i ) )
            {
                row = i;
                break;
            }
        }

        // Se não existe → criar nova linha
        if ( row == -1 )
        {
            modelo.addRow( new Object[]
            {
                "", "", "", "", "", "", "", "", "", "", ""
            } );
            row = modelo.getRowCount() - 1;
        }

        // --------------------------
        // 4. Preencher a linha (BUG Resolvido)
        // --------------------------
        modelo.setValueAt( codigoProdutoLocal, row, 0 );
        modelo.setValueAt( descricao_produto, row, 1 );
        modelo.setValueAt( unidade, row, 2 );
        modelo.setValueAt( CfMethods.formatarComoMoeda( preco ), row, 3 );
        modelo.setValueAt( qtd, row, 4 );
        modelo.setValueAt( descontoPercent, row, 5 );
        modelo.setValueAt( taxaIva, row, 6 );
        modelo.setValueAt( taxaRet, row, 7 );
        modelo.setValueAt( totalRetencao, row, 8 );
        modelo.setValueAt( CfMethods.formatarComoMoeda( valorIliquido ), row, 9 );
        modelo.setValueAt( CfMethods.formatarComoMoeda( totalComIva ), row, 10 );

        // --------------------------
        // 5. Criar sempre última linha vazia
        // --------------------------
        int last = modelo.getRowCount() - 1;
        if ( !linhaTabelaEstaVazia( modelo, last ) )
        {
            modelo.addRow( new Object[]
            {
                "", "", "", "", "", "", "", "", "", "", ""
            } );
        }

        // --------------------------
        // 6. Impressão
        // --------------------------
        TbProduto findByDesignacao = produtosController.findByDesignacao(
                cmbProduto.getSelectedItem().toString()
        );

        int idPedido = 0;
        TbMesas mesaEntity = ( TbMesas ) mesasController.findById( DVML.MESA_BALCAO );
        TbLugares lugarEntity = ( TbLugares ) lugaresController.findById( DVML.LUGAR_BALCAO );
        TbUsuario usuarioEntity = ( TbUsuario ) usuariosController.findById( cod_usuario );
        String usuario = usuarioEntity.getNome();

        if ( findByDesignacao.getCozinha().equals( DVML.ENVIAR_TICKET ) )
        {
            MetodosUtil.imprimir_cozinha(
                    findByDesignacao, idPedido,
                    mesaEntity.getDesignacao(), lugarEntity.getDesignacao(),
                    usuario, "Activo", qtd.intValue(), dadosInstituicaoController
            );
        }
        else if ( findByDesignacao.getCozinha().equals( DVML.ENVIAR_SALA ) )
        {
            MetodosUtil.imprimir_sala(
                    findByDesignacao, idPedido,
                    mesaEntity.getDesignacao(), lugarEntity.getDesignacao(),
                    usuario, "Activo", qtd.intValue(), dadosInstituicaoController
            );
        }

        // --------------------------
        // 7. Totais
        // --------------------------
        setTotalRetencao();
        setTotalPagar();
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
    private static boolean linhaTabelaEstaVazia( DefaultTableModel model, int row )
    {
        for ( int col = 0; col < model.getColumnCount(); col++ )
        {
            Object v = model.getValueAt( row, col );
            if ( v != null && !v.toString().trim().isEmpty() )
            {
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
    public static boolean estado_critico() throws SQLException
    {

        return conexao.getQuantidade_minima_publico( getCodigoProduto(), getCodigoArmazem() )
                < conexao.getQuantidade_Existente_Publico( getCodigoProduto(), getCodigoArmazem() )
                && conexao.getQuantidade_Existente_Publico( getCodigoProduto(), getCodigoArmazem() )
                <= conexao.getQuantidade_critica_public( getCodigoProduto(), getCodigoArmazem() );
    }

    public static boolean possivel_quantidade() throws SQLException
    {

        double quant_possivel = conexao.getQuantidade_Existente_Publico( getCodigoProduto(), getCodigoArmazem() )
                - conexao.getQuantidade_minima_publico( getCodigoProduto(), getCodigoArmazem() );

        return quant_possivel >= getQuantidade();

    }

    public static boolean isStocavel( String status )
    {
        try
        {
            if ( status.equals( "true" ) )
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        catch ( Exception e )
        {
            return true;
        }

    }

    public static boolean campos_invalidos()
    {
        return txtQuatindade.getText().equals( "" );
    }

    //verifica se o produto existe na tabela do formulário visão isto é na jTable
    private static boolean exist_produto_tabela_formulario( String designacao )
    {

        DefaultTableModel modelo = ( DefaultTableModel ) table.getModel();

        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {
            if ( String.valueOf( table.getValueAt( i, 1 ) ).equals( designacao ) )
            {
                linha_actual = i;
                return true;
            }
        }
        return false;

    }

    public void calcularTroco()
    {

        String prefixo = "";
        double troco = 0;

//        System.out.println( "VALOR ENTREGUE " + txtValorEntregue.getText() );
        System.out.println( "TOTAL A PAGAR " + txtTotalPagar.getText().trim() );

//        double valor_entregue = Double.parseDouble( txtValorEntregue.getText() );
        double total_pagar = Double.parseDouble( txtTotalPagar.getText().trim() );
//        troco = valor_entregue - total_pagar;

        System.out.println( "TROCO " + troco );
//        txtTroco.setText( String.valueOf( MetodosUtil.retirar_dizimas( troco ) ).trim() );

    }

    private static void procedimentoAdicionarTabela2( TbProduto produto, String mes )
    {
        try
        {
            if ( !Objects.isNull( produto ) )
            {
                Integer codTipoProduto = produto.getCodTipoProduto().getCodigo();
                TbTipoProduto tipoProduto = ( TbTipoProduto ) tipoProdutoController.findById( codTipoProduto );
                Integer codFamilia = tipoProduto.getFkFamilia().getPkFamilia();
                Familia familia = ( Familia ) familiaController.findById( codFamilia );
//                cmbFamilia.setSelectedItem( familia.getDesignacao() );
                cmbSubFamilia.setSelectedItem( tipoProduto.getDesignacao() );

                cmbProduto.setModel( new DefaultComboBoxModel( produtosController.getVector() ) );
                cmbProduto.setSelectedItem( produto.getDesignacao() );
                adicionar_preco_quantidade_anitgo();
                if ( rbTranstorno.isSelected() )
                {
                    procedimento_adicionar_sem_transtorno( mes );
                }
                else
                {
                    procedimento_adicionar( mes );
                }
                txtCodigoProduto.setText( "" );
                txtCodigoBarra.setText( "" );
                txtQuatindade.setText( "1" );
                txtQuatindade.requestFocus();

            }
            else
            {
                JOptionPane.showMessageDialog( null, "Nao existe produto/servico relacionado a esta referencia" );
            }

        }
        catch ( Exception e )
        {
            e.printStackTrace();

        }

    }

    public static void adicionar_preco_quantidade_anitgo()
    {
        try
        {
            if ( txtQuatindade.getText().isEmpty() )
            {
                JOptionPane.showMessageDialog( null, "Não informou a quantidade, por favor informe a quantidade!" );
            }
            else
            {
                TbStock stockLocal = stocksController.getStockByIdProdutoAndIdArmazem( getCodigoProduto(), getCodigoArmazem() );
                if ( stockLocal != ( null ) )
                {
                    if ( stockLocal.getQuantidadeExistente() <= stockLocal.getQuantCritica() )
                    {
                        txtQuantidadeStock.setBackground( Color.RED );
                        txtQuantidadeStock.setForeground( Color.BLACK );
                    }
                    else
                    {
                        txtQuantidadeStock.setBackground( new Color( 51, 153, 0, 255 ) );
                    }
                    TbPreco precoLocal = precosController.getLastIdPrecoByIdProduto( getCodigoProduto(), Double.parseDouble( txtQuatindade.getText() ) );
                    txtPreco.setText( String.valueOf( MetodosUtil.retirar_dizimas( precoLocal.getPrecoVenda().doubleValue() ) ) );
                    txtQuantidadeStock.setText( String.valueOf( stockLocal.getQuantidadeExistente() ) );
                }

            }
        }
        catch ( Exception ex )
        {
            ex.printStackTrace();
            Logger
                    .getLogger( FormVendaResponsivaVisaoTop.class
                            .getName() ).log( Level.SEVERE, null, ex );
        }

    }

    public static void adicionar_preco_quantidade_anitgo( int codigo_produto )
    {
        try
        {
            if ( txtQuatindade.getText().isEmpty() )
            {
                JOptionPane.showMessageDialog( null, "Não informou a quantidade, por favor informe a quantidade!" );
            }
            else
            {
                TbStock stockLocal = stocksController.getStockByIdProdutoAndIdArmazem( codigo_produto, getCodigoArmazem() );
                if ( stockLocal != ( null ) )
                {
                    if ( stockLocal.getQuantidadeExistente() <= stockLocal.getQuantCritica() )
                    {
                        txtQuantidadeStock.setBackground( Color.RED );
                        txtQuantidadeStock.setForeground( Color.BLACK );
                    }
                    else
                    {
                        txtQuantidadeStock.setBackground( new Color( 51, 153, 0, 255 ) );
                    }
//                    TbPreco precoLocal = precosController.getLastIdPrecoByIdProduto( codigo_produto, Double.parseDouble( txtQuatindade.getText() ) );
//                    txtPreco.setText( String.valueOf( MetodosUtil.retirar_dizimas( precoLocal.getPrecoVenda().doubleValue() ) ) );
                    txtQuantidadeStock.setText( String.valueOf( stockLocal.getQuantidadeExistente() ) );
                }

            }
        }
        catch ( Exception ex )
        {
            ex.printStackTrace();
            Logger
                    .getLogger( FormVendaResponsivaVisaoTop.class
                            .getName() ).log( Level.SEVERE, null, ex );
        }

    }

    public void adicionar_botao()
    {
        try
        {
            if ( !campos_invalidos() )
            {

                if ( !isProdutoExpirado( getCodigoProduto() ) )
                {
                    TbProduto produtoLocal = ( TbProduto ) produtosController.findById( getCodigoProduto() );
                    if ( isStocavel( produtoLocal.getStocavel() ) )
                    {
                        if ( possivel_quantidade() )
                        {
                            if ( estado_critico() )
                            {
                                JOptionPane.showMessageDialog( null, "O produto: " + produtoLocal.getDesignacao() + " precisa de ser actualizado no stock", "DVML", JOptionPane.WARNING_MESSAGE );
                            }
                            adicionar_produto( "" );

                        }
                        else
                        {
                            JOptionPane.showMessageDialog( null, "O produto: " + produtoLocal.getDesignacao() + " nao pode ser vendido pra esta quantidade", "DVML", JOptionPane.ERROR_MESSAGE );
                        }

                    }
                    else
                    {
                        adicionar_produto( "" );
                    }
                }
                else
                {
                    JOptionPane.showMessageDialog( null, "Produto não pode ser vendido porque foi expirado.", "Aviso", JOptionPane.WARNING_MESSAGE );
                }

            }
            else
            {
                JOptionPane.showMessageDialog( null, "Por Favor Digite a Quantidade" );
            }

        }
        catch ( SQLException ex )
        {

        }

    }

    public static boolean validar_zero()
    {
        return Double.parseDouble( txtQuatindade.getText() ) == 0;
    }

    public static void limpar()
    {

        txtQuatindade.setText( "1" );
        txtCodigoProduto.setText( "" );
        txtCodigoManual.setText( "" );
        txtTotalPagar.setText( "0" );
        txtCodigoBarra.setText( "" );
        txtNomeConsumidorFinal.setText( "" );
        dc_data_vencimento.setCalendar( null );
        txtReferencia.setText( "" );
//        txtObs.setText( "" );
        gorjeta = 0;
        reset_desconto_global();
        reset_valor_entregue();

    }

    private static void reset_desconto_global()
    {
        sp_desconto_financeiro.setModel( CfMethodsSwing.criarSpinnerDoubleModel( 0.0, 10000000000.00, 0.0 ) );
    }

    private static void reset_valor_entregue()
    {
//        txtValorEntregue.setText( "" );
    }

    public static boolean campos_invalido_imprimir()
    {

//        if ( getValor_entregue() < CfMethods.parseMoedaFormatada( txtTotal_AOA_liquido.getText() ) && ( getIdDocumento() != DVML.DOC_FACTURA_PROFORMA_PP ) && ( getIdDocumento() != DVML.DOC_FACTURA_FT ) )
        if ( false )
        {
            JOptionPane.showMessageDialog( null, "O valor entregue tem quer ser maior ou igual ao Total a Pagar", "AVISO", JOptionPane.WARNING_MESSAGE );
//            txtValorEntregue.requestFocus();
            return true;
        }

        if ( cambio == null )
        {
            JOptionPane.showMessageDialog( null, "Por favor seleccione a moeda", "AVISO", JOptionPane.WARNING_MESSAGE );
            return true;
        }

        if ( cmbTipoDocumento == null )
        {
            JOptionPane.showMessageDialog( null, "Por favor seleccione o Tipo de Documento", "AVISO", JOptionPane.WARNING_MESSAGE );
            return true;
        }

        return false;

    }

    public static boolean possivel_quantidade( int cod_produto, double qtd )
    {

        //System.err.println(conexao.getQuantidade_Existente_Publico(getCodigoProduto(), getCodigoArmazem()));  
        //  TbStock stock =  stockDao.getStockByDescricao(getCodigoProduto(), getCodigoArmazem() );
        double quant_possivel = conexao.getQuantidade_Existente_Publico( cod_produto, getCodigoArmazem() ) - conexao.getQuantidade_minima_publico( cod_produto, getCodigoArmazem() );
        //int quant_possivel = stock.getQuantidadeExistente() -  stock.getQuantBaixa();

        return quant_possivel >= qtd;

    }

    public void remover_items()
    {

        table.getColumnModel().getColumn( 0 );
        table.getColumnModel().getColumn( 1 );
        table.getColumnModel().getColumn( 2 );
        table.getColumnModel().getColumn( 3 );
        table.getColumnModel().getColumn( 4 );
        table.getColumnModel().getColumn( 5 );

        DefaultTableModel modelo = ( DefaultTableModel ) table.getModel();

        for ( int i = modelo.getRowCount() - 1; i >= 0; i-- )
        {
            modelo.removeRow( i );
        }

    }

    public static void setTotalRetencao()
    {
        try
        {
            BigDecimal totalRetencao = getTotalRetencaoLiquido();
            txtTotal_AOA_Retencao.setText( CfMethods.formatarComoMoeda( totalRetencao ) );
        }
        catch ( Exception e )
        {
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
    private static BigDecimal getTotalRetencaoLiquido()
    {

        DefaultTableModel modelo = ( DefaultTableModel ) table.getModel();
        BigDecimal totalRetencao = BigDecimal.ZERO;

        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {

            int codProduto = Integer.parseInt( modelo.getValueAt( i, 0 ).toString() );
            Object valorObj = modelo.getValueAt( i, 8 );

            // Se estiver vazio, pula
            if ( valorObj == null )
            {
                continue;
            }

            String valorTexto = valorObj.toString().trim();

            if ( valorTexto.isEmpty() || valorTexto.equals( "--" ) )
            {
                continue;
            }

            try
            {
                if ( servicosRetencaoController.existeRetencao( codProduto ) )
                {
                    double valor = CfMethods.parseMoedaFormatada( valorTexto );
                    totalRetencao = totalRetencao.add( BigDecimal.valueOf( valor ) );
                }

            }
            catch ( Exception e )
            {
                System.err.println( "Erro ao converter valor da coluna 8: '" + valorTexto + "'" );
                continue; // continua sem travar o programa
            }
        }

        return totalRetencao.setScale( 2, RoundingMode.HALF_UP );
    }

    public static double getTotalPagar()
    {

        DefaultTableModel modelo = ( DefaultTableModel ) table.getModel();

        double total_pagar = 0;
        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {
            total_pagar += CfMethods.parseMoedaFormatada( String.valueOf( modelo.getValueAt( i, 10 ) ) );

        }
        return total_pagar;

    }

    public static double getTotal_Retencao()
    {

        DefaultTableModel modelo = ( DefaultTableModel ) table.getModel();

        double total_retencao = 0;
        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {
            total_retencao += CfMethods.parseMoedaFormatada( String.valueOf( modelo.getValueAt( i, 8 ) ) );

        }
        return total_retencao;

    }

    private static BigDecimal getTotalAOALiquido()
    {
        BigDecimal totalIliquido = FinanceUtils.getTotalIliquidoTable( INDEX_TABLE_PRECO, INDEX_TABLE_QTD, table );
        BigDecimal totalImposto = BigDecimal.valueOf( FinanceUtils.getTotalImpostoTable( INDEX_TABLE_PRECO, INDEX_TABLE_QTD, INDEX_TABLE_DESCONTO, INDEX_TABLE_TAXA_IVA, table ) );
        BigDecimal descontoComercial = FinanceUtils.getDescontoComercial( INDEX_TABLE_PRECO, INDEX_TABLE_QTD, INDEX_TABLE_DESCONTO, table );
        BigDecimal descontoFinanceiro = BigDecimal.valueOf( getDescontoFinanceiro() );

        return totalIliquido
                .add( totalImposto )
                .subtract( descontoComercial.add( descontoFinanceiro ) )
                .setScale( 2, RoundingMode.HALF_UP );
    }

    public static void setTotalPagar()
    {
//        BigDecimal total = getTotalAOALiquido();
//        String valorFormatado = CfMethods.formatarComoMoeda( total );
//        txtTotalPagar.setText( valorFormatado );

        DefaultTableModel modelo = ( DefaultTableModel ) table.getModel();
        double total_liquido = 0;

        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {
            total_liquido += CfMethods.parseMoedaFormatada( String.valueOf( modelo.getValueAt( i, 10 ) ) );
        }
        txtTotalPagar.setText( CfMethods.formatarComoMoeda( total_liquido ) );
    }

    public static String formatarComoMoeda( BigDecimal valor )
    {
        return valor.setScale( 2, RoundingMode.HALF_UP ).toString().replace( ".", "," ) + " Kz";
    }

    private static void valor_por_extenco()
    {
        BigDecimal total = BigDecimal.valueOf( CfMethods.parseMoedaFormatada( txtTotalPagar.getText() ) );
//        BigDecimal total = CfMethods.parseMoedaFormatadaBigDecimal( txtTotalPagar.getText() );
        lbValorPorExtenco.setText( MetodosUtil.valorPorExtensoBigDecima( total, getMoeda().getDesignacao() ) );
    }

    private static void valor_por_extenco( Moeda moeda )
    {
        System.out.println( "Valor XXXXXXX: " + CfMethods.parseMoedaFormatada( txtTotalPagar.getText() ) );
        lbValorPorExtenco.setText( MetodosUtil.valorPorExtenso( CfMethods.parseMoedaFormatada( txtTotalPagar.getText() ), moeda.getDesignacao() ) );
    }

    private static double getDescontoFinanceiro()
    {
        double desconto_economico = 0d;
        desconto_economico = Double.parseDouble( sp_desconto_financeiro.getValue().toString() );
        return desconto_economico;
    }

    private static double getTotalAOARetencoes()
    {
        double valores = (getTotalRetencao1());
        return ( valores );
    }

    private static double getTotalRetencao1()
    {
        DefaultTableModel modelo = ( DefaultTableModel ) table.getModel();
        double qtd = 0d;
        double imposto = 0d, preco_unitario = 0d, desconto_valor_linha = 0d, valor_taxa = 0d;

        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {
            preco_unitario = CfMethods.parseMoedaFormatada( modelo.getValueAt( i, 3 ).toString() );
            qtd = Double.parseDouble( modelo.getValueAt( i, 4 ).toString() );
            double valor_percentagem = Double.parseDouble( modelo.getValueAt( i, 5 ).toString() );
            double taxa = Double.parseDouble( modelo.getValueAt( i, 7 ).toString() );
            // a incidência só é aplicável ao produtos sujeitos a iva 
            if ( taxa != 0 )
            {
                double valor_unitario = (preco_unitario * qtd);

                desconto_valor_linha = valor_unitario * ( ( valor_percentagem ) / 100 );
                valor_taxa = ( valor_unitario - desconto_valor_linha ) / taxa;
//                imposto += ( ( valor_unitario - desconto_valor_linha ) * ( taxa / 100 ) );

            }

        }

        return valor_taxa;
    }

    private static BigDecimal getTotalVendaIVASemIncluirDesconto()
    {
        BigDecimal totalIva = BigDecimal.ZERO;

        DefaultTableModel modelo = ( DefaultTableModel ) table.getModel();

        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {
            // Coluna 3: Preço Unitário formatado
            BigDecimal precoUnitarioLocal = BigDecimal.valueOf(
                    CfMethods.parseMoedaFormatada( modelo.getValueAt( i, 3 ).toString() )
            );

            // Coluna 4: Quantidade
            BigDecimal quantidade = new BigDecimal( modelo.getValueAt( i, 4 ).toString() );

            // Coluna 6: Taxa de IVA (%)
            BigDecimal taxaIva = new BigDecimal( modelo.getValueAt( i, 6 ).toString() );

            // subtotal ilíquido = preco * qtd
            BigDecimal subtotal = precoUnitarioLocal.multiply( quantidade );

            // valor do IVA = subtotal * (taxa / 100)
            BigDecimal valorIva = subtotal.multiply( taxaIva ).divide( BigDecimal.valueOf( 100 ), 2, RoundingMode.HALF_UP );

            // somar ao total
            totalIva = totalIva.add( valorIva );
        }

        return totalIva.setScale( 2, RoundingMode.HALF_UP );
    }

    private static BigDecimal getGrossTotal()
    {
        return FinanceUtils.getTotalIliquidoTable(
                INDEX_TABLE_PRECO,
                INDEX_TABLE_QTD, table )
                .add( getTotalVendaIVASemIncluirDesconto() )
                .setScale( 2, RoundingMode.HALF_UP );
    }

    private boolean validarPrecos_tabela( JTable tabela )
    {
        DefaultTableModel modelo = ( DefaultTableModel ) tabela.getModel();
        StringBuilder produtosComErro = new StringBuilder();

        // Percorrer todas as linhas
        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {
            double precoUnitario = extrairValorNumerico( modelo.getValueAt( i, 9 ) );
            double precoTotal = extrairValorNumerico( modelo.getValueAt( i, 10 ) );

            // Se qualquer um dos dois preços for zero, marca como erro
            if ( precoUnitario <= 0 || precoTotal <= 0 )
            {
                Object designacaoObj = modelo.getValueAt( i, 1 );
                String designacao = ( designacaoObj != null && !designacaoObj.toString().isEmpty() )
                        ? designacaoObj.toString()
                        : "Produto sem nome";

                if ( produtosComErro.length() > 0 )
                {
                    produtosComErro.append( " e " );
                }
                produtosComErro.append( designacao );
            }
        }

        // Se encontrou produtos com erro
        if ( produtosComErro.length() > 0 )
        {
            int opcao = JOptionPane.showConfirmDialog(
                    null,
                    "Atenção\nO(s) produto(s):\n" + produtosComErro
                    + ". Possuem preços Zero.\n\nDeves removê-lo(s) da lista!!!",
                    "Erro de Validação de Preços",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.ERROR_MESSAGE
            );

            // Se clicar OK, remover as linhas com erro
            if ( opcao == JOptionPane.OK_OPTION )
            {
                for ( int i = modelo.getRowCount() - 1; i >= 0; i-- )
                {
                    double precoUnitario = extrairValorNumerico( modelo.getValueAt( i, 9 ) );
                    double precoTotal = extrairValorNumerico( modelo.getValueAt( i, 10 ) );

                    if ( precoUnitario <= 0 || precoTotal <= 0 )
                    {
                        modelo.removeRow( i );
                    }
                }
            }

            return false; // Impede abrir forma de pagamento
        }

        return true; // Tudo certo, pode continuar
    }

    private double extrairValorNumerico( Object valorObj )
    {
        if ( valorObj == null )
        {
            return 0.0;
        }

        String valor = valorObj.toString().trim();

        if ( valor.isEmpty() )
        {
            return 0.0;
        }

        // Remove espaços e símbolos de moeda (AOA, KZ, etc.)
        valor = valor.replaceAll( "(?i)AOA", "" ) // remove AOA (maiúsculo ou minúsculo)
                .replaceAll( "(?i)KZ", "" )
                .replaceAll( "\\s+", "" );   // remove espaços

        // Substitui vírgulas por pontos
        valor = valor.replace( ",", "." );

        // Remove qualquer caracter que não seja número, ponto ou sinal
        valor = valor.replaceAll( "[^0-9.\\-]", "" );

        // Corrige caso tenha mais de um ponto (ex: "1.600.00" -> "1600.00")
        int firstDot = valor.indexOf( '.' );
        if ( firstDot != -1 )
        {
            int lastDot = valor.lastIndexOf( '.' );
            if ( lastDot != firstDot )
            {
                // remove todos os pontos exceto o último
                valor = valor.substring( 0, lastDot ).replace( ".", "" ) + valor.substring( lastDot );
            }
        }

        // Debug opcional
        System.out.println( "→ Valor processado: '" + valorObj + "' => '" + valor + "'" );

        try
        {
            return Double.parseDouble( valor );
        }
        catch ( NumberFormatException e )
        {
            System.err.println( "Erro ao converter valor: '" + valorObj + "' -> " + e.getMessage() );
            return 0.0;
        }
    }

    private static String iniciais_extenso()
    {
        Documento documento_local = ( Documento ) documentosController.findById( getIdDocumento() );
        String abreviacao_local = documento_local.getAbreviacao();

        switch (abreviacao_local)
        {
            case "FT":
                return "Facturamos o valor de: ";
            case "FR":
                return "Recebemos a quantia de: ";
            default:
                return "São: ";
        }
    }

    private void setFolhaImpressora( String folha )
    {
        if ( folha.equalsIgnoreCase( "A6" ) )
        {
            ck_simplificada.setSelected( true );
            ck_A7.setSelected( false );
            ck_A4.setSelected( false );
            ck_Duplicada.setSelected( false );
            ck_S_A6.setSelected( false );
            ck_ComVirgula.setSelected( false );
            ck_simplificada_O.setSelected( false );
            ck_simplificada_O_S.setSelected( false );
            this.abreviacao = Abreviacao.FR_A6;
        }
        else if ( folha.equalsIgnoreCase( "A6_O" ) )
        {
            ck_simplificada_O.setSelected( true );
            ck_A7.setSelected( false );
            ck_A4.setSelected( false );
            ck_Duplicada.setSelected( false );
            ck_S_A6.setSelected( false );
            ck_ComVirgula.setSelected( false );
            ck_simplificada.setSelected( false );
            ck_simplificada_O_S.setSelected( false );
            this.abreviacao = Abreviacao.FR_A6_O;
        }
        else if ( folha.equalsIgnoreCase( "S_A6_O" ) )
        {
            ck_simplificada_O_S.setSelected( true );
            ck_simplificada_O.setSelected( false );
            ck_A7.setSelected( false );
            ck_A4.setSelected( false );
            ck_Duplicada.setSelected( false );
            ck_S_A6.setSelected( false );
            ck_ComVirgula.setSelected( false );
            ck_simplificada.setSelected( false );
            this.abreviacao = Abreviacao.FR_S_A6_O;
        }
        else if ( folha.equalsIgnoreCase( "A7" ) )
        {
            ck_A7.setSelected( true );
            ck_simplificada.setSelected( false );
            ck_A4.setSelected( false );
            ck_Duplicada.setSelected( false );
            ck_S_A6.setSelected( false );
            ck_ComVirgula.setSelected( false );
            ck_simplificada_O.setSelected( false );
            ck_simplificada_O_S.setSelected( false );
            this.abreviacao = Abreviacao.FR_SA7;
        }
        else if ( folha.equalsIgnoreCase( "A5" ) )
        {
            ck_Duplicada.setSelected( true );
            ck_simplificada.setSelected( false );
            ck_A4.setSelected( false );
            ck_A7.setSelected( false );
            ck_S_A6.setSelected( false );
            ck_ComVirgula.setSelected( false );
            ck_simplificada_O.setSelected( false );
            ck_simplificada_O_S.setSelected( false );
            this.abreviacao = Abreviacao.FT_A4_Duplicado;
        }
        else if ( folha.equalsIgnoreCase( "S_A6" ) )
        {
            ck_S_A6.setSelected( true );
            ck_simplificada.setSelected( false );
            ck_A7.setSelected( false );
            ck_A4.setSelected( false );
            ck_Duplicada.setSelected( false );
            ck_ComVirgula.setSelected( false );
            ck_simplificada_O.setSelected( false );
            ck_simplificada_O_S.setSelected( false );
            this.abreviacao = Abreviacao.FR_S_A6;
        }
        else if ( folha.equalsIgnoreCase( "A6V" ) )
        {
            ck_ComVirgula.setSelected( true );
            ck_simplificada.setSelected( false );
            ck_A7.setSelected( false );
            ck_A4.setSelected( false );
            ck_Duplicada.setSelected( false );
            ck_S_A6.setSelected( false );
            ck_simplificada_O.setSelected( false );
            ck_simplificada_O_S.setSelected( false );
            this.abreviacao = Abreviacao.FR_A6_Com_Virgula;
        }
        else
        {
            ck_A4.setSelected( true );
            ck_simplificada.setSelected( false );
            ck_A7.setSelected( false );
            ck_Duplicada.setSelected( false );
            ck_S_A6.setSelected( false );
            ck_ComVirgula.setSelected( false );
            ck_simplificada_O.setSelected( false );
            ck_simplificada_O_S.setSelected( false );
            this.abreviacao = Abreviacao.FR_A4;
        }
    }

    private void setFocus( String focus )
    {
        if ( focus.equalsIgnoreCase( "Codigo Interno" ) )
        {

            txtCodigoProduto.requestFocus();

        }
        else if ( focus.equalsIgnoreCase( "Codigo de Barra" ) )
        {

            txtCodigoBarra.requestFocus();

        }
        else
        {

            txtCodigoManual.requestFocus();

        }
    }

    private void setDesactivarvias( String desactivarvias )
    {

        if ( desactivarvias.equalsIgnoreCase( "Sim" ) )
        {

            spnCopia.setVisible( true );
//            lbVias.setVisible( true );

        }
        else
        {

            spnCopia.setVisible( false );
//            lbVias.setVisible( false );

        }

    }

    private void setDesactivarLugar( String desactivarLugar )
    {

        if ( desactivarLugar.equalsIgnoreCase( "Sim" ) )
        {

            spnCopia.setVisible( true );
//            lbVias.setVisible( true );

        }
        else
        {

            spnCopia.setVisible( false );
//            lbVias.setVisible( false );

        }

    }

    private void setTranstorno( String transtorno )
    {
        if ( transtorno.equalsIgnoreCase( "Activo" ) )
        {
            rbTranstorno.setSelected( true );
//            jlStockNegativo.setVisible( true );

        }
        else
        {
            rbTranstorno.setSelected( false );
            rbTranstorno.setVisible( false );
//            jlStockNegativo.setVisible( false );

        }
    }

    private void setActivarDescontoFinanceiro( String desconto_financeiro )
    {
        if ( desconto_financeiro.equalsIgnoreCase( "Activar" ) )
        {
            sp_desconto_financeiro.setVisible( true );
            lbDescontoFinanceiro.setVisible( true );

        }
        else
        {
            sp_desconto_financeiro.setVisible( false );
            lbDescontoFinanceiro.setVisible( false );

        }
    }

    private void setVizualisarStock( String stock )
    {
        if ( stock.equalsIgnoreCase( "Vizualisar Stock" ) )
        {
            txtQuantidadeStock.setVisible( true );
            lbQuantidadeStock.setVisible( true );

        }
        else
        {
            txtQuantidadeStock.setVisible( false );
            lbQuantidadeStock.setVisible( false );

        }
    }

    private void setAnoEconomico( String ano_economico )
    {
        if ( ano_economico.equalsIgnoreCase( "Ocultar" ) )
        {
            cmbAnoEconomico.setVisible( true );

        }
        else
        {
            cmbAnoEconomico.setVisible( false );

        }
    }

    private void setDocpadrao( String documentos )
    {
        System.out.println( "DOCUMENTO PADRAO: " + documentos );
        if ( documentos.equalsIgnoreCase( "Factura/Recibo" ) )
        {
            cmbTipoDocumento.setSelectedIndex( 1 );

        }
        else if ( documentos.equalsIgnoreCase( "Factura" ) )
        {
            cmbTipoDocumento.setSelectedIndex( 2 );

        }
        else if ( documentos.equalsIgnoreCase( "Factura-Proforma" ) )
        {
            cmbTipoDocumento.setSelectedIndex( 3 );

        }
        else if ( documentos.equalsIgnoreCase( "Guia de Transporte" ) )
        {
            cmbTipoDocumento.setSelectedIndex( 4 );

        }
    }

    private void setArmazem( String armazem )
    {
        if ( armazem.equalsIgnoreCase( "Multi_armazem" ) )
        {
            rbArmazem.setSelected( true );
            rbArmazem1.setSelected( false );

        }
        else
        {
            rbArmazem.setSelected( false );
            rbArmazem1.setSelected( true );
        }
    }

    private void mostrar_nome()
    {
        TbUsuario usuario = ( TbUsuario ) usuariosController.getUsuarioByCodigo( this.cod_usuario );
        lb_nome_usuario.setText( "Operador: " + usuario.getNome() );
        System.out.println( "&&&&&&&&Conn::::" + cod_usuario );
    }

    private void empresa()
    {
        TbDadosInstituicao dados = ( TbDadosInstituicao ) dadosInstituicaoController.findById( 1 );

        jlEmpresa.setText( "KITANDA 1.2                      " + dados.getNome() );

    }

    private void actualizar_abreviacao()
    {

        switch (getIdDocumento())
        {
            case DVML.DOC_FACTURA_RECIBO_FR:
                if ( ck_A4.isSelected() )
                {
                    this.abreviacao = Abreviacao.FR_A4;
                }
                else if ( ck_simplificada.isSelected() )
                {
                    this.abreviacao = Abreviacao.FR_A6;
                }
                else if ( ck_simplificada_O_S.isSelected() )
                {
                    this.abreviacao = Abreviacao.FR_S_A6_O;
                }
                else if ( ck_simplificada_O.isSelected() )
                {
                    this.abreviacao = Abreviacao.FR_A6_O;
                }
                else if ( ck_A7.isSelected() )
                {
                    this.abreviacao = Abreviacao.FR_SA7;
                }
                else if ( ck_S_A6.isSelected() )
                {
                    this.abreviacao = Abreviacao.FR_S_A6;
                }
                else if ( ck_ComVirgula.isSelected() )
                {
                    this.abreviacao = Abreviacao.FR_A6_Com_Virgula;
                }
                else
                {
                    this.abreviacao = Abreviacao.FR_A4_Duplicado;
                }

                break;

            case DVML.DOC_FACTURA_FT:

                if ( ck_A4.isSelected() )
                {
                    this.abreviacao = Abreviacao.FA;
//                    ck_A4.setSelected( true );
                }
                else if ( ck_A7.isSelected() || ck_simplificada.isSelected() || ck_S_A6.isSelected() )
                {
//                    JOptionPane.showMessageDialog( null, "Atenção, selecione outro formato pra venda a crédito!" );
                    ck_A4.setSelected( true );
                }
                else
                {
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

    private void mostra_consumidor_final()
    {

        if ( cmbCliente.getSelectedItem().equals( "Consumidor Final" ) )
        {
//            lbClienteConsumidorFinal.setVisible( true );
            txtNomeConsumidorFinal.setVisible( true );
        }
        else
        {
//            lbClienteConsumidorFinal.setVisible( false );
            txtNomeConsumidorFinal.setVisible( false );
        }

    }

    private void desabilitar_campos()
    {

        boolean proformaNaoSelecionado = !( DVML.DOC_FACTURA_PROFORMA_PP == getIdDocumento() );

        boolean documentoIsFA = DVML.DOC_FACTURA_FT == getIdDocumento();
        boolean documentoIsPP = DVML.DOC_FACTURA_PROFORMA_PP == getIdDocumento();
        boolean documentoIsGT = DVML.DOC_GUIA_TRANSPORTE_GT == getIdDocumento();
        boolean documentoIsCM = DVML.DOC_FACTURA_CONSULTA_MESA == getIdDocumento();
        System.err.println( "documentoIsFA: " + documentoIsFA );
        System.err.println( "documentoIsPP: " + documentoIsPP );
        ck_A4.setSelected( !documentoIsFA && !documentoIsPP && !documentoIsGT );
        btnProcessar.setVisible( documentoIsPP || documentoIsFA || documentoIsGT || documentoIsCM );
        btnFormaPagamento.setVisible( !documentoIsFA && !documentoIsPP && !documentoIsGT && !documentoIsCM );
//        btnSemFormaPagamento.setVisible( !documentoIsFA && !documentoIsPP && !documentoIsGT && !documentoIsCM );
    }

    private void atualizarCliente1()
    {
        boolean documentoIsFA = DVML.DOC_FACTURA_FT == getIdDocumento();
        boolean documentoIsPP = DVML.DOC_FACTURA_PROFORMA_PP == getIdDocumento();
        boolean documentoIsGT = DVML.DOC_GUIA_TRANSPORTE_GT == getIdDocumento();

        if ( documentoIsFA || documentoIsPP || documentoIsGT )
        {
            //EXCLUIR CONSUMIDOR FINAL
            cmbCliente.setModel( new DefaultComboBoxModel( clientesController.getVectorExecptoConsumidorFinal() ) );
        }
        else
        {
            cmbCliente.setModel( new DefaultComboBoxModel( clientesController.getVector() ) );
            cmbCliente.setSelectedItem( DVML._CLIENTE_CONSUMIDOR_FINAL );
        }
    }

    private void accao_codigo_manual_enter()
    {
        try
        {
            String codigo_manual = txtCodigoManual.getText();
            TbProduto produtoLocal = produtosController.findByCodManual( codigo_manual );
            procedimentoAdicionarTabela( produtoLocal );
        }
        catch ( Exception ex )
        {
            ex.printStackTrace();
            Logger.getLogger( FormVendaResponsivaVisaoTop.class.getName() ).log( Level.SEVERE, null, ex );
            JOptionPane.showMessageDialog( null, "Não existe produto com este código de barra.", DVML.DVML_COMERCIAL, JOptionPane.ERROR_MESSAGE );
        }

    }

    public void adicionar_preco_quantidade()
    {

        try
        {

            TbProduto produto_local = ( TbProduto ) produtosController.findById( getCodigoProduto() );

            TbStock stockLocal = stocksController.getStockByIdProdutoAndIdArmazem( getCodigoProduto(), getCodigoArmazem() );
            boolean isStocavel = produto_local.getStocavel().equals( "true" );

            if ( isStocavel && stockLocal.getQuantidadeExistente() <= stockLocal.getQuantCritica() )
            {

                txtQuantidadeStock.setBackground( Color.RED );
                txtQuantidadeStock.setForeground( Color.BLACK );
            }
            else
            {
                txtQuantidadeStock.setBackground( new Color( 51, 153, 0, 255 ) );
            }

            txtCodigoBarra.setText( String.valueOf( produto_local.getCodBarra() ) );
            //actualizar
            txtLocal.setText( String.valueOf( produto_local.getCodLocal().getDesignacao() ) );
            txtCodigoProduto.setText( String.valueOf( produto_local.getCodigo() ) );

            if ( isStocavel && !Objects.isNull( stockLocal ) )
            {
                txtQuantidadeStock.setText( String.valueOf( stockLocal.getQuantidadeExistente() ) );
            }
            else
            {
                txtQuantidadeStock.setText( "0" );
            }

        }
        catch ( Exception ex )
        {
            ex.printStackTrace();
            txtQuantidadeStock.setText( "0" );
            Logger
                    .getLogger( FormVendaResponsivaVisaoTop.class
                            .getName() ).log( Level.SEVERE, null, ex );
        }

    }

    private void atualizarDataVencimentoFA()
    {
        boolean documentoIsFA = DVML.DOC_FACTURA_FT == getIdDocumento();
        boolean documentoIsPP = DVML.DOC_FACTURA_PROFORMA_PP == getIdDocumento();
//        boolean documentoIsGT = DVML.DOC_GUIA_TRANSPORTE_GT == getIdDocumento();

        if ( documentoIsFA || documentoIsPP )
        {
            dc_data_vencimento.setEnabled( true );
        }
        else
        {
            dc_data_vencimento.setEnabled( false );
        }
    }

    public void remover_item_carrinho()
    {

        DefaultTableModel modelo = ( DefaultTableModel ) table.getModel();

        String designacao = modelo.getValueAt( table.getSelectedRow(), 1 ).toString();
        if ( designacao.contentEquals( "#" ) )
        {
            JOptionPane.showMessageDialog( null, "Não é permitido remover um  serviço " );
        }

        modelo.removeRow( table.getSelectedRow() );
        setTotalPagar();
        setTotalRetencao();
        calculaTotalIVA();
        //txtDesconto.setText("0");

        TbProduto findByDesignacao = produtosController.findByDesignacao( cmbProduto.getSelectedItem().toString() );

        int idPedido = 0;
        TbMesas mesaEntity = ( TbMesas ) mesasController.findById( DVML.MESA_BALCAO );
        String mesa = mesaEntity.getDesignacao();
        TbLugares lugarEntity = ( TbLugares ) lugaresController.findById( DVML.LUGAR_BALCAO );
        String lugar = lugarEntity.getDesignacao();
        TbUsuario usuarioEntity = ( TbUsuario ) usuariosController.findById( cod_usuario );
        String usuario = usuarioEntity.getNome();

        if ( findByDesignacao.getCozinha().equals( "Enviar Ticket" ) )
        {
            MetodosUtil.imprimir_cozinha( findByDesignacao, idPedido, mesa, lugar, usuario, "Cancelado", ( int ) getQuantidade(), dadosInstituicaoController );
        }
        else if ( findByDesignacao.getCozinha().equals( "Enviar Sala" ) )
        {
            MetodosUtil.imprimir_sala( findByDesignacao, idPedido, mesa, lugar, usuario, "Cancelado", ( int ) getQuantidade(), dadosInstituicaoController );
        }

        valor_por_extenco();
        reset_desconto_global();
        //calcularTroco();

    }

    public static void accao_codigo_interno_enter_busca_exterior( int codigo )
    {

        try
        {

            System.out.println( "ID PRODUTO EXTERIOR: " + codigo );
            TbProduto produtoLocal = ( TbProduto ) produtosController.findById( codigo );

            Integer codTipoProduto = produtoLocal.getCodTipoProduto().getCodigo();
            TbTipoProduto tipoProduto = ( TbTipoProduto ) tipoProdutoController.findById( codTipoProduto );
            Integer codFamilia = tipoProduto.getFkFamilia().getPkFamilia();
            Familia familia = ( Familia ) familiaController.findById( codFamilia );
            cmbSubFamilia.setSelectedItem( tipoProduto.getDesignacao() );

            cmbProduto.setModel( new DefaultComboBoxModel( produtosController.getVector() ) );
            cmbProduto.setSelectedItem( produtoLocal.getDesignacao() );

            configuracaoMesComecoController = new ConfiguracaoMesComecoController( conexao.getConnectionAtiva() );

            boolean existeConfiguracaoDoCliente = configuracaoMesComecoController.existeConfiguracaoDoCliente(
                    getIdCliente(), produtoLocal.getCodigo() );
            if ( existeConfiguracaoDoCliente )
            {

                new MesesPagoClienteVisao( null,
                        true,
                        getIdCliente(),
                        getCodigoProduto(), conexao ).setVisible( true );
            }
            else
            {

                procedimentoAdicionarTabela( produtoLocal );
            }

            cursorLinha();
        }
        catch ( Exception ex )
        {
            ex.printStackTrace();
            Logger
                    .getLogger( FormVendaResponsivaVisaoTop.class
                            .getName() ).log( Level.SEVERE, null, ex );
            JOptionPane.showMessageDialog( null, "Este produto não existe no armazém " + cmbArmazem.getSelectedItem(), DVML.DVML_COMERCIAL, JOptionPane.ERROR_MESSAGE );
        }

    }

    /**
     * Normaliza uma string: lower-case, remove acentos, pontuação extra e
     * colapsa espaços.
     */
    private String normalizar( String s )
    {
        if ( s == null )
        {
            return "";
        }

        // remove acentos
        String n = Normalizer.normalize( s, Normalizer.Form.NFD )
                .replaceAll( "\\p{M}", "" );
        // remove pontuação (mas preserva #), transforma espaços múltiplos em 1 e trim
        n = n.replaceAll( "[\\p{Punct}&&[^#]]+", " " )
                .replaceAll( "\\s+", " " )
                .trim()
                .toLowerCase();
        return n;
    }

    /**
     * Extrai (produto, mes) de uma designacao que contenha '#'. Retorna null se
     * não for possível extrair.
     */
    private String[] extrairProdutoEMes( String designacao )
    {
        if ( designacao == null )
        {
            return null;
        }
        Pattern p = Pattern.compile( "^(.*?)#(.*)$" ); // grupo 1: tudo antes do #, grupo2: tudo depois
        Matcher m = p.matcher( designacao );
        if ( !m.find() )
        {
            return null;
        }
        String produto = m.group( 1 ).trim();
        String mes = m.group( 2 ).trim();
        return new String[]
        {
            produto, mes
        };
    }

    private boolean podeRemoverServico( DefaultTableModel model, int selectedRow )
    {
        if ( selectedRow == -1 )
        {
            JOptionPane.showMessageDialog( null, "Selecione um serviço para remover!" );
            return false;
        }

        String designacaoSelecionada = String.valueOf( model.getValueAt( selectedRow, 1 ) );
        if ( designacaoSelecionada == null || !designacaoSelecionada.contains( "#" ) )
        {
            return true; // não é mensalidade
        }

        // mapa de meses em ordem e normalizado
        String[] meses =
        {
            "Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho",
            "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"
        };
        // criar versão normalizada do array meses para comparação
        String[] mesesNorm = new String[ meses.length ];
        for ( int i = 0; i < meses.length; i++ )
        {
            mesesNorm[ i ] = normalizar( meses[ i ] );
        }

        try
        {
            String[] pm = extrairProdutoEMes( designacaoSelecionada );
            if ( pm == null )
            {
                return true;
            }
            String produtoOriginal = pm[ 0 ];
            String mesAtual = pm[ 1 ];

            String produtoNorm = normalizar( produtoOriginal );

            // índice do mês selecionado
            String mesAtualNorm = normalizar( mesAtual );
            int indexMesAtual = -1;
            for ( int i = 0; i < mesesNorm.length; i++ )
            {
                if ( mesesNorm[ i ].equals( mesAtualNorm ) )
                {
                    indexMesAtual = i;
                    break;
                }
            }
            if ( indexMesAtual == -1 )
            {
                JOptionPane.showMessageDialog( null, "Mês inválido: " + mesAtual );
                return false;
            }

            // percorre todas as linhas e identifica as que pertencem EXATAMENTE ao mesmo produto
            int totalMesesMesmoProduto = 0;
            int maxIndexPresente = -1;
            String mesMaisRecentePresente = null;

            // também vamos registrar as linhas que tem o mês mais recente para garantir remover última ocorrência
            java.util.List<Integer> linhasDoMesMaisRecente = new java.util.ArrayList<>();

            for ( int r = 0; r < model.getRowCount(); r++ )
            {
                String d = String.valueOf( model.getValueAt( r, 1 ) );
                if ( d == null || !d.contains( "#" ) )
                {
                    continue;
                }

                String[] pmLinha = extrairProdutoEMes( d );
                if ( pmLinha == null )
                {
                    continue;
                }

                String produtoLinhaNorm = normalizar( pmLinha[ 0 ] );

                // compara produto por igualdade da string normalizada
                if ( !produtoLinhaNorm.equals( produtoNorm ) )
                {
                    continue;
                }

                // extrai mes e normaliza
                String mesLinhaNorm = normalizar( pmLinha[ 1 ] );
                int idxLinha = -1;
                for ( int m = 0; m < mesesNorm.length; m++ )
                {
                    if ( mesesNorm[ m ].equals( mesLinhaNorm ) )
                    {
                        idxLinha = m;
                        break;
                    }
                }
                if ( idxLinha == -1 )
                {
                    continue;
                }

                totalMesesMesmoProduto++;

                if ( idxLinha > maxIndexPresente )
                {
                    maxIndexPresente = idxLinha;
                    mesMaisRecentePresente = meses[ idxLinha ];
                    linhasDoMesMaisRecente.clear();
                    linhasDoMesMaisRecente.add( r );
                }
                else if ( idxLinha == maxIndexPresente )
                {
                    // mesma posição do mês mais recente — acrescenta a linha (duplicatas do mesmo mês)
                    linhasDoMesMaisRecente.add( r );
                }
            }

            // se existe apenas 1 mês daquele produto -> pode remover
            if ( totalMesesMesmoProduto <= 1 )
            {
                return true;
            }

            // se o mês do selectedRow não é o mês mais recente -> bloqueia
            if ( indexMesAtual != maxIndexPresente )
            {
                JOptionPane.showMessageDialog( null,
                        "Não pode remover " + meses[ indexMesAtual ]
                        + " antes de remover " + mesMaisRecentePresente + " (remova do último para o primeiro)." );
                return false;
            }

            // se o mês selecionado é o mês mais recente, mas há várias linhas com o mesmo mês,
            // só permita remover se a linha selecionada for a *última ocorrência* daquele mês (maior índice de linha).
            if ( !linhasDoMesMaisRecente.isEmpty() )
            {
                int maiorLinha = -1;
                for ( Integer ln : linhasDoMesMaisRecente )
                {
                    if ( ln > maiorLinha )
                    {
                        maiorLinha = ln;
                    }
                }
                if ( selectedRow != maiorLinha )
                {
                    JOptionPane.showMessageDialog( null,
                            "Existem várias entradas para " + mesMaisRecentePresente
                            + ". Remova a linha mais abaixo (última ocorrência) primeiro." );
                    return false;
                }
            }

            // passou todas as regras -> pode remover
            return true;

        }
        catch ( Exception ex )
        {
            JOptionPane.showMessageDialog( null, "Erro ao verificar serviço: " + ex.getMessage() );
            return false;
        }
    }

    private static boolean primeiraLinhaVazia()
    {
        DefaultTableModel modelo = ( DefaultTableModel ) table.getModel();

        if ( modelo.getRowCount() == 0 )
        {
            return true;
        }

        Object codigo = modelo.getValueAt( 0, 0 );

        return codigo == null || codigo.toString().trim().isEmpty();
    }

    private static void inserir_uma_linha()
    {
        DefaultTableModel modelo = ( DefaultTableModel ) table.getModel();
        if ( modelo.getRowCount() == 0 )
        {
            modelo.addRow( new Object[]
            {
                null, null, null, null, null, null, null, null, null, null, null
            } );
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
//        Logger.getLogger(FormVendaResponsivaVisaoTop.class.getName()).log(Level.SEVERE, null, ex);
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
    private void accao_codigo_barra_enter_cb()
    {
        try
        {
            String codigoBarra = txtCodigoBarra.getText().trim();

            if ( codigoBarra.isEmpty() )
            {
                JOptionPane.showMessageDialog( null,
                        "Digite o código de barra!",
                        DVML.DVML_COMERCIAL,
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            // Busca o produto pelo código de barra
            TbProduto produto = produtosController.findByCodBarra( codigoBarra );

            if ( produto == null )
            {
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
            Integer codTipoProduto = produto.getCodTipoProduto().getCodigo();
            TbTipoProduto tipoProduto = ( TbTipoProduto ) tipoProdutoController.findById( codTipoProduto );
            cmbSubFamilia.setSelectedItem( tipoProduto.getDesignacao() );
            cmbProduto.setModel( new DefaultComboBoxModel( produtosController.getVector() ) );
            cmbProduto.setSelectedItem( produto.getDesignacao() );

            // Verificação da configuração do cliente
            configuracaoMesComecoController = new ConfiguracaoMesComecoController( conexao.getConnectionAtiva() );
            boolean existeConfiguracaoDoCliente
                    = configuracaoMesComecoController.existeConfiguracaoDoCliente( getIdCliente(), produto.getCodigo() );

            if ( existeConfiguracaoDoCliente )
            {
                new MesesPagoClienteVisao( this, rootPaneCheckingEnabled, getIdCliente(),
                        produto.getCodigo(), conexao ).setVisible( true );
            }
            else
            {
                // --- ADICIONA PRODUTO NA TABELA ---
                adicionarProdutoNaTabelaComLinhaVazia( produto );
            }

            // Limpa campos
            txtCodigoProduto.setText( "" );
            txtCodigoBarra.setText( "" );
            txtQuatindade.setText( "1" );
            txtQuatindade.requestFocus();

        }
        catch ( Exception ex )
        {
            Logger.getLogger( FormVendaResponsivaVisaoTop.class.getName() ).log( Level.SEVERE, null, ex );
            JOptionPane.showMessageDialog( null,
                    "Este produto não existe no armazém " + cmbArmazem.getSelectedItem(),
                    DVML.DVML_COMERCIAL,
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void adicionarProdutoNaTabelaComLinhaVazia( TbProduto produto )
    {
        try
        {
            DefaultTableModel model = ( DefaultTableModel ) table.getModel();

            // --- Corrigir: confirmar edição atual ---
            if ( table.isEditing() )
            {
                table.getCellEditor().stopCellEditing();
            }

            // ---------- 1. Localizar linha vazia ----------
            int row = -1;
            for ( int i = 0; i < model.getRowCount(); i++ )
            {
                if ( linhaEstaVazia( model, i ) )
                {
                    row = i;
                    break;
                }
            }

            if ( row == -1 )
            {
                model.addRow( new Object[]
                {
                    "", "", "", "", "", "", "", "", "", "", ""
                } );
                row = model.getRowCount() - 1;
            }

            // ------------------------------------------
            // 2. Cálculos igual ao teu sistema original
            // ------------------------------------------
            int codigoProduto = produto.getCodigo();
            String descricao = produto.getDesignacao();
            String unidade = getUnidade_Produto();

            BigDecimal qtd = BigDecimal.valueOf( 1 );
            BigDecimal preco = BigDecimal.valueOf( getPreco() );
            BigDecimal descontoPercent = BigDecimal.valueOf( produto.getPercentagemDesconto() );
            BigDecimal taxaIva = BigDecimal.valueOf( getTaxaImpostoIva( codigoProduto ) );
            BigDecimal taxaRet = BigDecimal.valueOf( getTaxaImpostoRet( codigoProduto ) );

            BigDecimal valorIliquido = FinanceUtils.getValorIliquido( qtd, preco, descontoPercent );

            double valorLiquidoDouble = FinanceUtils.getValorComIVA(
                    qtd.doubleValue(),
                    taxaIva.doubleValue(),
                    preco.doubleValue(),
                    descontoPercent.doubleValue()
            );
            BigDecimal totalComIva = BigDecimal.valueOf( valorLiquidoDouble );

            String totalComRetencao = CfMethods.formatarComoMoeda(
                    MetodosUtil.getValorComRetencao(
                            qtd.doubleValue(),
                            taxaRet.doubleValue(),
                            preco.doubleValue(),
                            descontoPercent.doubleValue()
                    )
            );

            // ---------- 3. Preencher a linha ----------
            model.setValueAt( codigoProduto, row, 0 );
            model.setValueAt( descricao, row, 1 );
            model.setValueAt( unidade, row, 2 );
            model.setValueAt( CfMethods.formatarComoMoeda( preco ), row, 3 );
            model.setValueAt( qtd, row, 4 );
            model.setValueAt( descontoPercent, row, 5 );
            model.setValueAt( taxaIva, row, 6 );
            model.setValueAt( taxaRet, row, 7 );
            model.setValueAt( totalComRetencao, row, 8 );
            model.setValueAt( CfMethods.formatarComoMoeda( valorIliquido ), row, 9 );
            model.setValueAt( CfMethods.formatarComoMoeda( totalComIva ), row, 10 );

            // ---------- 4. Criar linha vazia ao final ----------
            int lastRow = model.getRowCount() - 1;
            if ( !linhaEstaVazia( model, lastRow ) )
            {
                model.addRow( new Object[]
                {
                    "", "", "", "", "", "", "", "", "", "", ""
                } );
            }

            // ---------- 5. Focar próximo item ----------
            int novaLinha = model.getRowCount() - 1;
            table.changeSelection( novaLinha, 0, false, false );
            table.editCellAt( novaLinha, 0 );
            table.requestFocus();

            // --------------------------
            // 7. Totais
            // --------------------------
            setTotalRetencao();
            setTotalPagar();
            calculaTotalIVA();
            valor_por_extenco();

        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }

    private boolean linhaEstaVazia( DefaultTableModel model, int row )
    {
        for ( int col = 0; col < model.getColumnCount(); col++ )
        {
            Object v = model.getValueAt( row, col );
            if ( v != null && !v.toString().trim().isEmpty() )
            {
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
    private void accao_codigo_barra_enter_jtable( int row )
    {

        try
        {
            DefaultTableModel model = ( DefaultTableModel ) table.getModel();

            // Lê o código digitado na coluna 0
            String codigoBarra = model.getValueAt( row, 0 ).toString().trim();

            if ( codigoBarra.isEmpty() )
            {
                JOptionPane.showMessageDialog( null,
                        "Digite o código de barra!",
                        DVML.DVML_COMERCIAL,
                        JOptionPane.WARNING_MESSAGE );
                return;
            }

            // --- Buscar produto ---
            TbProduto produto = produtosController.findByCodBarra( codigoBarra );

            if ( produto == null )
            {
                JOptionPane.showMessageDialog( null,
                        "Produto não encontrado!",
                        DVML.DVML_COMERCIAL,
                        JOptionPane.ERROR_MESSAGE );
                model.setValueAt( "", row, 0 );
                table.changeSelection( row, 0, false, false );
                table.editCellAt( row, 0 );
                return;
            }

            // Preencher linha inteira
            preencherLinhaTabelaComProduto( row, produto );

            // Criar nova linha se necessário
            adicionarLinhaVaziaSeNecessario();

            // Atualizar totais (SE existir este método)
//        calcularTotais();
            // Focar na nova última linha
            int last = model.getRowCount() - 1;
            table.changeSelection( last, 0, false, false );
            table.editCellAt( last, 0 );

        }
        catch ( Exception e )
        {
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
    private void preencherLinhaTabelaComProduto( int row, TbProduto produto )
    {

        DefaultTableModel model = ( DefaultTableModel ) table.getModel();
//
        int codigoProduto = produto.getCodigo();
        String descricao = produto.getDesignacao();

        String unidade = getUnidade_Produto();

        BigDecimal qtd = new BigDecimal( "1" );
        BigDecimal preco = BigDecimal.valueOf( getPreco( codigoProduto, qtd.doubleValue() ) );
        BigDecimal descontoPercent = BigDecimal.valueOf( getDescontoPercentagem() );
        BigDecimal taxaIva = BigDecimal.valueOf( getTaxaImpostoIva( codigoProduto ) );
        BigDecimal taxaRet = BigDecimal.valueOf( getTaxaImpostoRet( codigoProduto ) );

        BigDecimal valorIliquido = FinanceUtils.getValorIliquido( qtd, preco, descontoPercent );

        double valorLiquidoDouble = FinanceUtils.getValorComIVA(
                qtd.doubleValue(),
                taxaIva.doubleValue(),
                preco.doubleValue(),
                descontoPercent.doubleValue()
        );
        BigDecimal totalComIva = BigDecimal.valueOf( valorLiquidoDouble );

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
        model.setValueAt( codigoProduto, row, 0 );
        model.setValueAt( descricao, row, 1 );
        model.setValueAt( unidade, row, 2 );
        model.setValueAt( CfMethods.formatarComoMoeda( preco ), row, 3 ); // SEM IVA
        model.setValueAt( qtd, row, 4 );
        model.setValueAt( descontoPercent, row, 5 );
        model.setValueAt( taxaIva, row, 6 );
        model.setValueAt( taxaRet, row, 7 );
        model.setValueAt( totalRetencao, row, 8 );
//    model.setValueAt(CfMethods.formatarComoMoeda(totalRetencao), row, 8);
        model.setValueAt( CfMethods.formatarComoMoeda( valorIliquido ), row, 9 );
        model.setValueAt( CfMethods.formatarComoMoeda( totalComIva ), row, 10 );

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
    private void adicionarLinhaVaziaSeNecessario()
    {
        DefaultTableModel model = ( DefaultTableModel ) table.getModel();

        int last = model.getRowCount() - 1;
        boolean vazia = true;

        for ( int col = 0; col < model.getColumnCount(); col++ )
        {
            Object v = model.getValueAt( last, col );
            if ( v != null && !v.toString().trim().isEmpty() )
            {
                vazia = false;
                break;
            }
        }

        if ( !vazia )
        {
            model.addRow( new Object[]
            {
                "", "", "", "", "", "", "", "", "", "", ""
            } );
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
    private void procedimento_codBarra__jtable()
    {

        // Criar o editor de texto que será usado na coluna 0
        JTextField editor = new JTextField();

        // Capturar o ENTER dentro do editor da célula
        editor.addKeyListener( new KeyAdapter()
        {
            @Override
            public void keyPressed( KeyEvent e )
            {
                if ( e.getKeyCode() == KeyEvent.VK_ENTER )
                {

                    // Salvar o texto digitado no model ANTES de pesquisar
                    if ( table.isEditing() )
                    {
                        table.getCellEditor().stopCellEditing();
                    }

                    int row = table.getSelectedRow();
                    int col = table.getSelectedColumn();

                    // Só dispara pesquisa se estiver na coluna 0
                    if ( col == 0 )
                    {
                        accao_codigo_produto_enter_jtable( row );
                        e.consume(); // impede JTable de mudar de linha automaticamente
                    }
                }
            }
        } );

        // Aplicar o editor na coluna 0 da JTable
        table.getColumnModel().getColumn( 0 ).setCellEditor( new DefaultCellEditor( editor ) );
    }

    private void accao_codigo_produto_enter_jtable( int row )
    {

        try
        {
            DefaultTableModel model = ( DefaultTableModel ) table.getModel();

            String valorDigitado = model.getValueAt( row, 0 ).toString().trim();

            if ( valorDigitado.isEmpty() )
            {
                JOptionPane.showMessageDialog( null,
                        "Digite o código!",
                        DVML.DVML_COMERCIAL,
                        JOptionPane.WARNING_MESSAGE );
                return;
            }

            TbProduto produto = null;

            // -------------------------------
            // 1) DETECTAR AUTOMATICAMENTE
            // -------------------------------
            if ( valorDigitado.matches( "\\d+" ) )
            {

                try
                {
                    // só números → pode ser ID interno
                    int codigo = Integer.parseInt( valorDigitado );

                    // tenta buscar por ID interno
                    produto = ( TbProduto ) produtosController.findById( codigo );
                }
                catch ( Exception e )
                {
                }

                // se não achar por ID → tenta como código de barras numérico
                if ( produto == null )
                {
                    produto = produtosController.findByCodBarra( valorDigitado );
                }

            }
            else
            {
                // contém letras ou formato de código → buscar como código de barras
                produto = produtosController.findByCodBarra( valorDigitado );
            }

            // -------------------------------
            // 2) Se mesmo assim não encontrou
            // -------------------------------
            if ( produto == null )
            {
                JOptionPane.showMessageDialog( null,
                        "Produto não encontrado!",
                        DVML.DVML_COMERCIAL,
                        JOptionPane.ERROR_MESSAGE );

                model.setValueAt( "", row, 0 );
                table.changeSelection( row, 0, false, false );
                table.editCellAt( row, 0 );
                return;
            }

            if ( possivel_quantidade( produto.getCodigo(), 1 ) )
            {
                adicionar_preco_quantidade_anitgo( produto.getCodigo() );

                // -------------------------------
                // 3) Preencher linha normalmente
                // -------------------------------
                preencherLinhaTabelaComProduto( row, produto );

                // linha vazia se necessário
                adicionarLinhaVaziaSeNecessario();

                // focar próxima linha
            }
            else
            {
                JOptionPane.showMessageDialog( null, "Quantidade inexistente no Stock." );
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
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }

    private static void cursorLinha()
    {
        DefaultTableModel model = ( DefaultTableModel ) table.getModel();
        int last = model.getRowCount() - 1;
        table.getModel().setValueAt( "", last, 0 );
        table.changeSelection( last, 0, false, false );
        table.editCellAt( last, 0 );
        table.requestFocusInWindow();

        Component editor = table.getEditorComponent();
        if ( editor != null )
        {
            editor.requestFocusInWindow();
        }
    }

    private void initStockListener()
    {
        // Pegamos a coluna do código do produto
        TableColumn codigoCol = table.getColumnModel().getColumn( 0 );

        // Garantimos que qualquer editor usado seja monitorado
        TableCellEditor editor = codigoCol.getCellEditor();
        if ( editor == null )
        {
            editor = table.getDefaultEditor( Object.class );
            codigoCol.setCellEditor( editor );
        }

        editor.addCellEditorListener( new CellEditorListener()
        {
            @Override
            public void editingStopped( ChangeEvent e )
            {
                int row = table.getSelectedRow();
                if ( row < 0 )
                {
                    return;
                }

                // Executa após a célula terminar de editar
                SwingUtilities.invokeLater( () ->
                {
                    mostrarStockKeyPress( row );
                    // Move foco automaticamente para a coluna quantidade (1)
                    table.changeSelection( row, 1, false, false );
                } );
            }

            @Override
            public void editingCanceled( ChangeEvent e )
            {
                // Nada a fazer
            }
        } );
    }

    /**
     * Mostra a quantidade de stock e preço baseado no código do produto.
     */
    private void mostrarStockKeyPress( int row )
    {
        try
        {
            Object codigoObj = table.getValueAt( row, 0 );
            if ( codigoObj == null || codigoObj.toString().trim().isEmpty() )
            {
                return;
            }

            int codigoProduto;
            try
            {
                codigoProduto = Integer.parseInt( codigoObj.toString().trim() );
            }
            catch ( NumberFormatException ex )
            {
                System.err.println( "Código inválido na linha " + row + ": " + codigoObj );
                return;
            }

            TbStock stockLocal = stocksController.getStockByIdProdutoAndIdArmazem( codigoProduto, getCodigoArmazem() );
            if ( stockLocal != null )
            {
                // Atualiza JTextField
                txtQuantidadeStock.setText( String.valueOf( stockLocal.getQuantidadeExistente() ) );

                // Colore campo se abaixo do critico
                if ( stockLocal.getQuantidadeExistente() <= stockLocal.getQuantCritica() )
                {
                    txtQuantidadeStock.setBackground( Color.RED );
                    txtQuantidadeStock.setForeground( Color.BLACK );
                }
                else
                {
                    txtQuantidadeStock.setBackground( new Color( 51, 153, 0 ) );
                    txtQuantidadeStock.setForeground( Color.BLACK );
                }

                // Atualiza preço se quantidade informada
                if ( !txtQuatindade.getText().isEmpty() )
                {
                    TbPreco precoLocal = precosController.getLastIdPrecoByIdProduto(
                            codigoProduto, Double.parseDouble( txtQuatindade.getText() )
                    );
                    txtPreco.setText( String.valueOf( MetodosUtil.retirar_dizimas( precoLocal.getPrecoVenda().doubleValue() ) ) );
                }
            }
        }
        catch ( Exception ex )
        {
            ex.printStackTrace();
            Logger.getLogger( FormVendaResponsivaVisaoTop.class.getName() )
                    .log( Level.SEVERE, null, ex );
        }
    }

    private static void removerUltimaLinhaVazia()
    {
        DefaultTableModel modelo = ( DefaultTableModel ) table.getModel();
        int lastRow = modelo.getRowCount() - 1;

        if ( lastRow < 0 )
        {
            return; // Não há linhas
        }
        boolean vazia = true;
        // Verifica se todas as células da última linha estão nulas ou vazias
        for ( int col = 0; col < modelo.getColumnCount(); col++ )
        {
            Object valor = modelo.getValueAt( lastRow, col );
            if ( valor != null && !valor.toString().trim().isEmpty() )
            {
                vazia = false;
                break;
            }
        }

        if ( vazia )
        {
            modelo.removeRow( lastRow );
        }

//        table.repaint();
    }

    private void actualizarPreco()
    {
        String preco = table.getValueAt( table.getSelectedRow(), 3 ).toString();
        int idProduto = Integer.parseInt( table.getValueAt( table.getSelectedRow(), 0 ).toString() );
        preco = preco.replaceAll( "A", "" ).
                replace( "O", "" ).
                replace( ".", "" ).
                replace( ",", "." );

        String precoUnitario = CfMethods.formatarComoMoeda( Double.parseDouble( preco ) );
        table.setValueAt( precoUnitario, table.getSelectedRow(), 3 );

        actualizarPrecoVendaManual( idProduto, CfMethods.parseMoedaFormatada( precoUnitario ), precosController );
    }

    private static void actualizarPrecoVendaManual( int idProduto, Double precoVenda, PrecosController precosControllerLocal )
    {

        Vector<TbPreco> vectorDoisPrecoAnterior = new Vector<>();
        conexaoTransaction = BDConexao.getInstancia();
        DocumentosController.start( conexaoTransaction );

        System.out.println( "ID PRODUTO: " + idProduto );

        int idPrecoRetalho = PrecosController.getLastIdPrecoByIdProdutoIntAndQTD( idProduto, 0d, conexaoTransaction );
        System.out.println( "ID RETALHO: " + idPrecoRetalho );
        TbPreco precoAntigoRetalho = ( TbPreco ) precosControllerLocal.findById( idPrecoRetalho );

        System.out.println( "PRECO RETALHO: " + precoAntigoRetalho );

        int idPrecoGrosso = PrecosController.getLastIdPrecoByIdProdutoIntAndPrecoAntigoQtdAlto( idProduto, precoAntigoRetalho.getQtdAlto() + 1, conexaoTransaction );
        TbPreco precoAntigoGrosso = ( TbPreco ) precosControllerLocal.findById( idPrecoGrosso );
        System.out.println( "ID GROSSO: " + idPrecoGrosso );
        System.out.println( "PRECO GROSSO: " + precoAntigoGrosso );

        TbPreco preco_novo_retalho = new TbPreco();

        preco_novo_retalho.setData( precoAntigoRetalho.getData() );
        preco_novo_retalho.setHora( precoAntigoRetalho.getHora() );
        preco_novo_retalho.setPercentagemGanho( precoAntigoRetalho.getPercentagemGanho() );

        preco_novo_retalho.setFkProduto( precoAntigoRetalho.getFkProduto() );
        preco_novo_retalho.setFkUsuario( precoAntigoRetalho.getFkUsuario() );
        preco_novo_retalho.setPrecoCompra( precoAntigoRetalho.getPrecoCompra() );
        preco_novo_retalho.setPrecoVenda( new BigDecimal( precoVenda ) );
        preco_novo_retalho.setQtdBaixo( precoAntigoRetalho.getQtdBaixo() );
        preco_novo_retalho.setQtdAlto( precoAntigoRetalho.getQtdAlto() );
        preco_novo_retalho.setPrecoAnterior( precoAntigoRetalho.getPrecoAnterior() );
        preco_novo_retalho.setRetalho( precoAntigoRetalho.getRetalho() );

        System.out.println( "HORA RETALHO: " + preco_novo_retalho.getHora() );
        System.out.println( "DATA RETAHO: " + preco_novo_retalho.getData() );

        try
        {
            precosControllerLocal.salvar( preco_novo_retalho );
            DocumentosController.commit( conexaoTransaction );
            conexaoTransaction.close();
//            precoDao.create(preco_novo_retalho);
            System.out.println( "Preco de compra retalho actualizado na venda" );
        }
        catch ( Exception e )
        {
            DocumentosController.rollback( conexaoTransaction );
            e.printStackTrace();
            System.err.println( "Falha ao actualizar o preço retalho na venda" );
        }

        conexaoTransaction = BDConexao.getInstancia();
        DocumentosController.start( conexaoTransaction );
        try
        {
            //        preco_novo_grosso = precoAntigoGrosso;
            TbPreco preco_novo_grosso = new TbPreco();

            preco_novo_grosso.setData( precoAntigoGrosso.getData() );
            preco_novo_grosso.setHora( precoAntigoGrosso.getHora() );
            preco_novo_grosso.setPercentagemGanho( precoAntigoGrosso.getPercentagemGanho() );

            preco_novo_grosso.setFkProduto( precoAntigoGrosso.getFkProduto() );
            preco_novo_grosso.setFkUsuario( precoAntigoGrosso.getFkUsuario() );
            preco_novo_grosso.setPrecoCompra( precoAntigoGrosso.getPrecoCompra() );
            preco_novo_grosso.setPrecoVenda( new BigDecimal( precoVenda ) );
            preco_novo_grosso.setQtdBaixo( precoAntigoGrosso.getQtdBaixo() );
            preco_novo_grosso.setQtdAlto( precoAntigoGrosso.getQtdAlto() );
            preco_novo_grosso.setPrecoAnterior( precoAntigoGrosso.getPrecoAnterior() );
            preco_novo_grosso.setRetalho( precoAntigoGrosso.getRetalho() );

            System.out.println( "HORA GROSSO: " + precoAntigoGrosso.getHora() );
            System.out.println( "DATA GROSSO: " + precoAntigoGrosso.getData() );

            precosControllerLocal.salvar( preco_novo_grosso );

            DocumentosController.commit( conexaoTransaction );
            conexaoTransaction.close();
//            precoDao.create(preco_novo_grosso);
            System.out.println( "Preco de compra grosso actualizado na compra" );
        }
        catch ( Exception e )
        {
            DocumentosController.rollback( conexaoTransaction );
            e.printStackTrace();
            System.err.println( "Falha ao actualizar o preço grosso na compra" );
        }

        if ( !existeProdutoPrecoLista( idProduto ) )
        {
            vectorDoisPrecoAnterior.add( precoAntigoRetalho );
            vectorDoisPrecoAnterior.add( precoAntigoGrosso );
            System.err.println( "Adicionei o preco retalho antigo " + precoAntigoRetalho.getPrecoVenda().doubleValue() );
            System.err.println( "Adicionei o preco grosso antigo " + precoAntigoGrosso.getPrecoVenda().doubleValue() );

            listaPrecoTemp.add( vectorDoisPrecoAnterior );
        }

    }

    private static boolean existeProdutoPrecoLista( int idProduto )
    {

        for ( int i = 0; i < listaPrecoTemp.size(); i++ )
        {
            Vector<TbPreco> get = listaPrecoTemp.get( i );
            if ( get.get( 0 ).getFkProduto().getCodigo() == idProduto )
            {
                return true;
            }

        }
        return false;

    }

    private void actualizarQtdTable()
    {

        linha_actual = table.getSelectedRow();

        if ( linha_actual > -1 )
        {

            int codProduto = Integer.parseInt( table.getValueAt( linha_actual, 0 ).toString() );
            double desconto = Double.parseDouble( table.getValueAt( linha_actual, 5 ).toString() );

            TbProduto produtoLocal = ( TbProduto ) produtosController.findById( codProduto );
            TbTipoProduto tipoProduto = ( TbTipoProduto ) tipoProdutoController.findById( produtoLocal.getCodTipoProduto().getCodigo() );

            double qtd;

            try
            {
                qtd = Double.parseDouble( table.getValueAt( linha_actual, 4 ).toString() );
            }
            catch ( NumberFormatException e )
            {
                resetValue( "Erro de formatação da quantidade.\nAtenção: Tem que ser número.", 4 );
                return;
            }

            if ( qtd <= 0 )
            {
                qtd = 1;
                resetValue( "Quantidade não pode ser zero(0) ou número negativo", 4 );
            }

            if ( desconto < 0 )
            {
                desconto = 0;
                resetValue( "O desconto não deve ser um número negativo.", 4 );
            }
            else if ( desconto > 100 )
            {
                desconto = 0;
                resetValue( "O desconto não deve ser maior do que 100%", 4 );
            }

            if ( possivel_quantidade( codProduto, qtd ) || tipoProduto.getFkFamilia().getPkFamilia() == DVML.COD_SERVICO )
            {
                actuazlizar_quantidade_tabela_formulario( String.valueOf( qtd ), desconto );
                setTotalRetencao();
                setTotalPagar();
                calculaTotalIVA();
                valor_por_extenco();
            }
            else
            {
                resetValue( "Não é possivel para esta quantidade.", 4 );
            }
        }
    }

    private static void actuazlizar_quantidade_tabela_formulario( String quantidade, double desconto )
    {
        DefaultTableModel modelo = ( DefaultTableModel ) table.getModel();
        double qtd = Double.parseDouble( quantidade );
        double retencao = 0;

        double preco_venda = CfMethods.parseMoedaFormatada( String.valueOf( modelo.getValueAt( linha_actual, 3 ) ) );
        double taxa = Double.parseDouble( String.valueOf( modelo.getValueAt( linha_actual, 6 ) ) );
//        double ret = CfMethods.parseMoedaFormatada( String.valueOf( modelo.getValueAt( linha_actual, 7) ) );
        double ret = Double.parseDouble( String.valueOf( modelo.getValueAt( linha_actual, 7 ) ) );

//        double total_iliquido_linha = (( preco_venda * qtd ) - desconto);
        String total_iliquido_linha = CfMethods.formatarComoMoeda( FinanceUtils.getValorIliquido(
                new BigDecimal( 1 ),
                new BigDecimal( preco_venda ),
                new BigDecimal( desconto )
        ) );

        double totalComIva = FinanceUtils.getValorComIVA( 1, taxa, CfMethods.parseMoedaFormatada( total_iliquido_linha ), 0 );
        totalComIva = totalComIva * qtd;

        String total_liquido_linha = CfMethods.formatarComoMoeda( totalComIva );

        retencao = MetodosUtil.getValorComRetencao( qtd, ret, preco_venda, desconto );

        String total_retencao = CfMethods.formatarComoMoeda( retencao );

        double totalIliquidoItem = CfMethods.parseMoedaFormatada( total_iliquido_linha ) * qtd;
        String totalIlquidoString = CfMethods.formatarComoMoeda( totalIliquidoItem );

        modelo.setValueAt( qtd, linha_actual, 4 );
        modelo.setValueAt( desconto, linha_actual, 5 );
        modelo.setValueAt( total_retencao, linha_actual, 8 );

        modelo.setValueAt( totalIlquidoString, linha_actual, 9 );
        modelo.setValueAt( total_liquido_linha, linha_actual, 10 );
        //a linha_actual recebe o default
        linha_actual = -1;

    }

    private void resetValue( String msg, int columnValue )
    {
        System.out.println( "Cheguei aqui..." );
        table.setValueAt( 1, linha_actual, columnValue );
        JOptionPane.showMessageDialog( null, msg );
        table.clearSelection();

    }

    public static void accao_codigo_interno_enter_busca_exterior_2( int codigo, String mes )
    {

        try
        {

            produtosController = new ProdutosController( conexao );
            System.out.println( "ID PRODUTO EXTERIOR: " + codigo );
            TbProduto produtoLocal = ( TbProduto ) produtosController.findById( codigo );

            procedimentoAdicionarTabela2( produtoLocal, mes );

        }
        catch ( Exception ex )
        {
            ex.printStackTrace();
            Logger
                    .getLogger( VendaUsuarioVisao.class
                            .getName() ).log( Level.SEVERE, null, ex );
            JOptionPane.showMessageDialog( null, "Este produto não existe no armazém " + cmbArmazem.getSelectedItem(), DVML.DVML_COMERCIAL, JOptionPane.ERROR_MESSAGE );
        }

    }

    private void tratar_desconto()
    {
        try
        {
            double desconto = 0.0;
            double total_pagar = getTotalPagar();
            double valor_desconto_geral = ( double ) sp_desconto_financeiro.getValue();

            if ( valor_desconto_geral > total_pagar )
            {
                JOptionPane.showMessageDialog( null, "O desconto global não pode ser maior que o total à pagar.", "AVISO", JOptionPane.WARNING_MESSAGE );
                //reset desconto global
                reset_desconto_global();
                setTotalPagar();
                sp_desconto_financeiro.requestFocus();
            }
            else if ( valor_desconto_geral == total_pagar )
            {
                reset_valor_entregue();
//                txtTroco.setText( "0.0" );

                desconto = ( total_pagar - valor_desconto_geral );
                txtTotalPagar.setText( CfMethods.formatarComoMoeda( desconto ) );
                valor_por_extenco();
            }
            else
            {
                desconto = ( total_pagar - valor_desconto_geral );
                txtTotalPagar.setText( CfMethods.formatarComoMoeda( desconto ) );
                reset_valor_entregue();
//                txtValorEntregue.setText( String.valueOf( desconto ) );
//                txtTroco.setText( "0.0" );
                valor_por_extenco();

            }
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }

    }

    private void procedimentoChamarFormaPagemnto()
    {
        removerUltimaLinhaVazia();

        jScrollPane1.repaint();
        if ( MetodosUtil.licencaValidada( conexao ) )
        {
            if ( !MetodosUtil.tabela_vazia( table ) )
            {
                if ( !validarPrecos_tabela( table ) )
                {
                    return; // Se houver erro, não abre forma de pagamento
                }

                new FormaPagamentoVisao( this, rootPaneCheckingEnabled, null, DVML.VENDA_PONTUAL_TOP, BDConexao.getInstancia() ).setVisible( true );
            }
            else
            {
                JOptionPane.showMessageDialog( null, "Caro usuário, adicione itens na tabela" );
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
    private void configurarTabela()
    {

        DefaultTableModel model = ( DefaultTableModel ) table.getModel();

        table.setModel( new DefaultTableModel(
                model.getDataVector(),
                getColumnNames( model )
        )
        {
            @Override
            public boolean isCellEditable( int row, int column )
            {
                return ( column == 3 ||  column == 4) ; // 3ª coluna editável
            }
        } );
    }

    private void configurarTabela( int coluna )
    {

        DefaultTableModel model = ( DefaultTableModel ) table.getModel();

        table.setModel( new DefaultTableModel(
                model.getDataVector(),
                getColumnNames( model )
        )
        {
            @Override
            public boolean isCellEditable( int row, int column )
            {
                return column == coluna; // 3ª coluna editável
            }
        } );
    }

    private Vector<String> getColumnNames( DefaultTableModel model )
    {
        Vector<String> colunas = new Vector<>();
        for ( int i = 0; i < model.getColumnCount(); i++ )
        {
            colunas.add( model.getColumnName( i ) );
        }
        return colunas;
    }

    private static void actualizarPrecosAntigos() throws Exception
    {
        prepararPrecosAntigos( precosController );
    }

    private static void actualizarPrecosAntigos2()
    {

        BDConexao conexaoLocal = new BDConexao();
        DocumentosController.start( conexaoLocal );
        PrecosController precosController = new PrecosController( conexaoLocal );
        try
        {
            prepararPrecosAntigos( precosController );
            DocumentosController.commit( conexaoLocal );
        }
        catch ( Exception e )
        {
            DocumentosController.rollback( conexaoLocal );
        }
        finally
        {
            conexaoLocal.close();
        }

    }

    private static void prepararPrecosAntigos( PrecosController precosController )
    {
        System.out.println( "Cheguei no actualizar precos Antigos" );
        if ( Objects.nonNull( listaPrecoTemp ) || listaPrecoTemp.size() > 0 )
        {
            for ( int i = 0; i < listaPrecoTemp.size(); i++ )
            {
                Vector<TbPreco> get = listaPrecoTemp.get( i );
                TbPreco precoRetalhoLocal = get.get( 0 );
                TbPreco precoGrossoLocal = get.get( 1 );
                precosController.salvar( precoRetalhoLocal );
                System.out.println( "Salvei o preco retalho" );
                precosController.salvar( precoGrossoLocal );
                System.out.println( "Salvei o preco groso" );
            }
            listaPrecoTemp.clear();
        }
    }

}
