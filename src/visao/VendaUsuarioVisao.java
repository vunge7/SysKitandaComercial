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
import java.awt.Dimension;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import static kitanda.util.CfConstantes.YYYYMMDD_HHMMSS;
import kitanda.util.CfMethods;
import kitanda.util.CfMethodsSwing;
import lista.ListaVenda1;
import tesouraria.novo.controller.ContaController;
import tesouraria.novo.controller.ContaMovimentosController;
import tesouraria.novo.util.MetodosUtilTS;
import util.BDConexao;
import util.DVML;
import util.DVML.Abreviacao;
import static util.DVML.CASAS_DECIMAIS;
import static util.DVML.DOC_FACTURA_RECIBO_FR;
import static util.DVML.DOC_FACTURA_FT;
import util.MetodosUtil;
import static util.MetodosUtil.rodarComandoWindows;

/**
 *
 * @author Domingos Dala Vunge
 */
public class VendaUsuarioVisao extends javax.swing.JFrame
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
    private static ContaMovimentosController cmc;
    private static TbPreco precoUnitario;

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
    private static Abreviacao abreviacao;
    private static double total_iliquido = 0, total_desconto_linha = 0;
    private static boolean aviso_continuar_documento = false;
    private static String prox_doc;
    public static double gorjeta = 0;
    private static TbDadosInstituicao dadosInstituicao;
    private static BDConexao conexaoTransaction;

    public VendaUsuarioVisao( int cod_usuario, BDConexao conexao ) throws SQLException
    {

        initComponents();

        confiLabel();
        setLocationRelativeTo( null );
        jLabel7.setVisible( false );
        txtKilometragem.setVisible( false );
        //pegarResolucao();
        VendaUsuarioVisao.conexao = conexao;

        /**
         * INSTANCIAS DOS CONTROLLER COMERCIAL
         */
        vendasController = new VendasController( VendaUsuarioVisao.conexao );
        itemVendasController = new ItemVendasController( VendaUsuarioVisao.conexao );
        mesasController = new MesasController( VendaUsuarioVisao.conexao );
        lugaresController = new LugaresController( VendaUsuarioVisao.conexao );
        produtosController = new ProdutosController( VendaUsuarioVisao.conexao );
        stocksController = new StoksController( VendaUsuarioVisao.conexao );
        precosController = new PrecosController( VendaUsuarioVisao.conexao );
        tipoProdutoController = new TipoProdutosController( VendaUsuarioVisao.conexao );
        familiaController = new FamiliasController( VendaUsuarioVisao.conexao );
        armazensController = new ArmazensController( VendaUsuarioVisao.conexao );
        localController = new LocalController( VendaUsuarioVisao.conexao );
        unidadesController = new UnidadesController( VendaUsuarioVisao.conexao );
        anoEconomicoController = new AnoEconomicoController( VendaUsuarioVisao.conexao );
        clientesController = new ClientesController( VendaUsuarioVisao.conexao );
        documentosController = new DocumentosController( VendaUsuarioVisao.conexao );
        moedasController = new MoedasController( VendaUsuarioVisao.conexao );
        descontosController = new DescontosController( VendaUsuarioVisao.conexao );
        usuariosController = new UsuariosController( VendaUsuarioVisao.conexao );
        dadosInstituicaoController = new DadosInstituicaoController( VendaUsuarioVisao.conexao );
        produtosImpostoController = new ProdutosImpostoController( VendaUsuarioVisao.conexao );
        produtosIsentoController = new ProdutosIsentoController( VendaUsuarioVisao.conexao );
        caixasController = new CaixasController( VendaUsuarioVisao.conexao );
        formaPagamentoController = new FormaPagamentoController( VendaUsuarioVisao.conexao );
        armazensAccessoController = new ArmazensAccessoController( VendaUsuarioVisao.conexao );
        cambiosController = new CambiosController( VendaUsuarioVisao.conexao );
        formaPagamentoItemController = new FormaPagamentoItemController( VendaUsuarioVisao.conexao );
        servicosRetencaoController = new ServicosRetencaoController( VendaUsuarioVisao.conexao );
        contaController = new ContaController( VendaUsuarioVisao.conexao );
        dadosInstituicao = (TbDadosInstituicao) dadosInstituicaoController.findById( 1 );
        cmc = new ContaMovimentosController( conexao );
        txtQuatindade.setText( "1" );
//        txtQuatindade.setDocument( new PermitirNumeros() );
        this.cod_usuario = cod_usuario;
        lbPreco7.setVisible( false );
        jlStockNegativo.setVisible( false );
        rbTranstorno.setVisible( false );
        rbMostrar.setVisible( false );
        rbArmazem1.setVisible( false );
        rbArmazem.setVisible( false );
        jButton2.setVisible( false );

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
        //new BuscaProdutoVisao( VendaUsuarioVisao.this, rootPaneCheckingEnabled, getCodigoArmazem(), DVML.JANELA_VENDA ).show();
//        MetodosUtil.FUNCAO_F1( this, rootPaneCheckingEnabled, getCodigoArmazem(), DVML.JANELA_VENDA);

        KeyboardFocusManager.getCurrentKeyboardFocusManager()
                .addKeyEventDispatcher( new KeyEventDispatcher()
                {
                    @Override
                    public boolean dispatchKeyEvent( KeyEvent e )
                    {
                        if ( e.getID() == KeyEvent.KEY_RELEASED && e.getKeyCode() == KeyEvent.VK_F1 )
                        {
                            try
                            {
                                new BuscaProdutoVisao( getInstance(), rootPaneCheckingEnabled, getCodigoArmazem(), DVML.JANELA_VENDA, conexao ).show();
                            }
                            catch ( Exception ex )
                            {
                                ex.printStackTrace();
                            }
                            return true;

                        }
                        return false;
                    }
                } );

//        habilitarColunas();
        MetodosUtil.setArmazemByCampoConfigArmazem( cmbArmazem, conexao, cod_usuario );

        setWindowsListener();
    }

    public JFrame getInstance()
    {
        return this;
    }

    public void keypressed( java.awt.event.KeyEvent evt )
    {

        if ( evt.getKeyCode() == KeyEvent.VK_ENTER )
        {

            txtIniciaisCliente.requestFocus();

        }

    }

    public void keyTyped( KeyEvent evt )
    {

        if ( evt.getKeyCode() == KeyEvent.VK_ENTER )
        {

            dispose();
        }

    }

    private void pegarResolucao()
    {
        Toolkit t = Toolkit.getDefaultToolkit();
        Dimension dimensao = t.getScreenSize();
        this.setSize( ( dimensao.width + 5 ), ( dimensao.height - 38 ) );

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

        jMenuItem1 = new javax.swing.JMenuItem();
        buttonGroup3 = new javax.swing.ButtonGroup();
        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        buttonGroup4 = new javax.swing.ButtonGroup();
        buttonGroup5 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        lbDescontoFinanceiro = new javax.swing.JLabel();
        txtTotalPagar = new javax.swing.JTextField();
        sp_desconto_financeiro = new javax.swing.JSpinner();
        lbTotalPagar1 = new javax.swing.JLabel();
        txtTotal_AOA_Retencao = new javax.swing.JLabel();
        lbPreco7 = new javax.swing.JLabel();
        lbTotalPagar2 = new javax.swing.JLabel();
        cmbMoeda = new javax.swing.JComboBox();
        jButton1 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        btnCancelar = new javax.swing.JButton();
        btnFormaPagamento = new javax.swing.JButton();
        btnProcessar = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        txtObs = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jlEmpresa = new javax.swing.JLabel();
        lb_usuario = new javax.swing.JLabel();
        lbValorPorExtenco = new javax.swing.JLabel();
        lb_nome_usuario = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        lbCategoria = new javax.swing.JLabel();
        lbCodigoProduto = new javax.swing.JLabel();
        txtCodigoProduto = new javax.swing.JTextField();
        lbProduto = new javax.swing.JLabel();
        lbQuantidade = new javax.swing.JLabel();
        btn_remover = new javax.swing.JButton();
        lbQuantidadeStock = new javax.swing.JLabel();
        lbCodigoProduto1 = new javax.swing.JLabel();
        txtCodigoBarra = new javax.swing.JTextField();
        cmbSubFamilia = new javax.swing.JComboBox();
        cmbProduto = new javax.swing.JComboBox();
        txtQuantidadeStock = new javax.swing.JTextField();
        jButton4 = new javax.swing.JButton();
        txtPreco = new javax.swing.JTextField();
        txtQuatindade = new javax.swing.JTextField();
        lbCodigoProduto2 = new javax.swing.JLabel();
        txtCodigoManual = new javax.swing.JTextField();
        cmbFamilia = new javax.swing.JComboBox<>();
        lbCategoria1 = new javax.swing.JLabel();
        lbPreco1 = new javax.swing.JLabel();
        btn_adicionar = new javax.swing.JButton();
        jPanelData = new javax.swing.JPanel();
        cmbArmazem = new javax.swing.JComboBox();
        cmbAnoEconomico = new javax.swing.JComboBox<>();
        rbArmazem = new javax.swing.JRadioButton();
        rbArmazem1 = new javax.swing.JRadioButton();
        ck_A4 = new javax.swing.JCheckBox();
        ck_simplificada = new javax.swing.JCheckBox();
        ck_A7 = new javax.swing.JCheckBox();
        ck_Duplicada = new javax.swing.JCheckBox();
        jlStockNegativo1 = new javax.swing.JLabel();
        ck_ComVirgula = new javax.swing.JCheckBox();
        ck_S_A6 = new javax.swing.JCheckBox();
        ck_simplificada_O_S = new javax.swing.JCheckBox();
        ck_simplificada_O = new javax.swing.JCheckBox();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        lbCliente = new javax.swing.JLabel();
        cmbCliente = new javax.swing.JComboBox();
        txtCodClientePesquisa = new javax.swing.JTextField();
        jButton5 = new javax.swing.JButton();
        lbCliente1 = new javax.swing.JLabel();
        txtNifClientePesquisa = new javax.swing.JTextField();
        txtIniciaisCliente = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtNomeConsumidorFinal = new javax.swing.JTextField();
        lbClienteConsumidorFinal = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        lbVias = new javax.swing.JLabel();
        spnCopia = new javax.swing.JSpinner();
        lbPreco2 = new javax.swing.JLabel();
        txtReferencia = new javax.swing.JTextField();
        rbTranstorno = new javax.swing.JRadioButton();
        jlStockNegativo = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        lb_proximo_documento = new javax.swing.JLabel();
        jPanelCaracteristicas = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtMarca = new javax.swing.JTextField();
        txtMotorista = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtMatricula = new javax.swing.JTextField();
        txtNumeroCarta = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtKilometragem = new javax.swing.JTextField();
        txtLocal = new javax.swing.JTextField();
        lbPreco3 = new javax.swing.JLabel();
        cmbTipoDocumento = new javax.swing.JComboBox();
        rbMostrar = new javax.swing.JRadioButton();
        dc_data_documento = new com.toedter.calendar.JDateChooser();
        dc_data_vencimento = new com.toedter.calendar.JDateChooser();
        jLabel9 = new javax.swing.JLabel();

        jMenuItem1.setText("jMenuItem1");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("...:::::  KITANDA - FACTURAÇÃO ::::...");

        jPanel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jPanel1.setFont(new java.awt.Font("Showcard Gothic", 0, 24)); // NOI18N

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel8.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lbDescontoFinanceiro.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbDescontoFinanceiro.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbDescontoFinanceiro.setText("Desconto :");
        jPanel8.add(lbDescontoFinanceiro, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 120, 20));

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
        jPanel8.add(txtTotalPagar, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 40, 320, 25));

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
            public void keyTyped(java.awt.event.KeyEvent evt)
            {
                sp_desconto_financeiroKeyTyped(evt);
            }
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                sp_desconto_financeiroKeyPressed(evt);
            }
        });
        jPanel8.add(sp_desconto_financeiro, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 10, 340, 30));

        lbTotalPagar1.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbTotalPagar1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbTotalPagar1.setText("Retenção :");
        jPanel8.add(lbTotalPagar1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 70, 130, 20));

        txtTotal_AOA_Retencao.setForeground(new java.awt.Color(255, 51, 51));
        txtTotal_AOA_Retencao.setText("Retencao");
        jPanel8.add(txtTotal_AOA_Retencao, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 70, 130, 20));

        lbPreco7.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbPreco7.setText("Moeda");
        jPanel8.add(lbPreco7, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 70, 50, -1));

        lbTotalPagar2.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbTotalPagar2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbTotalPagar2.setText("Total a Pagar :");
        jPanel8.add(lbTotalPagar2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 40, 130, 20));

        cmbMoeda.setBackground(new java.awt.Color(4, 154, 3));
        cmbMoeda.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 14)); // NOI18N
        cmbMoeda.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbMoedaActionPerformed(evt);
            }
        });
        jPanel8.add(cmbMoeda, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 70, 120, -1));

        jButton1.setText("CAIXA");
        jButton1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel8.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 70, 10, -1));

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/LOGOUT - VERMELHO/Logout 32x32.png"))); // NOI18N
        btnCancelar.setAlignmentX(0.5F);
        btnCancelar.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnCancelarActionPerformed(evt);
            }
        });
        jPanel3.add(btnCancelar, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 30, 70, 40));

        btnFormaPagamento.setFont(new java.awt.Font("Lucida Grande", 0, 12)); // NOI18N
        btnFormaPagamento.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/1323444801_currency_dollar red.png"))); // NOI18N
        btnFormaPagamento.setText("Forma de Pagamento");
        btnFormaPagamento.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnFormaPagamentoActionPerformed(evt);
            }
        });
        jPanel3.add(btnFormaPagamento, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 210, 40));

        btnProcessar.setFont(new java.awt.Font("Lucida Grande", 0, 12)); // NOI18N
        btnProcessar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/impressora1.png"))); // NOI18N
        btnProcessar.setText("Processar");
        btnProcessar.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnProcessarActionPerformed(evt);
            }
        });
        jPanel3.add(btnProcessar, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 30, 170, 40));

        jLabel8.setText("Obs:");
        jPanel3.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 40, -1));

        txtObs.setFont(new java.awt.Font("Lucida Grande", 0, 11)); // NOI18N
        jPanel3.add(txtObs, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 0, 130, 30));

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagem/calendario 32x32.png"))); // NOI18N
        jButton2.setText("VISTORIA");
        jButton2.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 0, 170, 30));

        jlEmpresa.setFont(new java.awt.Font("Lucida Grande", 1, 12)); // NOI18N
        jlEmpresa.setForeground(new java.awt.Color(0, 0, 102));
        jlEmpresa.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jlEmpresa.setText("Empresa");
        jPanel3.add(jlEmpresa, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, 460, -1));

        lb_usuario.setFont(new java.awt.Font("Lucida Grande", 1, 12)); // NOI18N
        lb_usuario.setText("Conta:");

        lbValorPorExtenco.setFont(new java.awt.Font("Lucida Grande", 1, 12)); // NOI18N
        lbValorPorExtenco.setForeground(new java.awt.Color(204, 0, 0));
        lbValorPorExtenco.setText("VALOR POR EXTENSO");

        lb_nome_usuario.setFont(new java.awt.Font("Lucida Grande", 1, 12)); // NOI18N
        lb_nome_usuario.setForeground(new java.awt.Color(0, 102, 102));
        lb_nome_usuario.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lb_nome_usuario.setText("Usuario");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(lb_usuario, javax.swing.GroupLayout.PREFERRED_SIZE, 363, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 481, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(lbValorPorExtenco, javax.swing.GroupLayout.PREFERRED_SIZE, 617, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lb_nome_usuario, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(14, 14, 14))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbValorPorExtenco, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lb_nome_usuario))
                .addGap(59, 59, 59)
                .addComponent(lb_usuario, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 12), new java.awt.Color(4, 154, 3))); // NOI18N
        jPanel4.setForeground(new java.awt.Color(102, 153, 0));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lbCategoria.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbCategoria.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbCategoria.setText("Família:");
        jPanel4.add(lbCategoria, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 50, 100, 30));

        lbCodigoProduto.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbCodigoProduto.setText("CodArt:");
        jPanel4.add(lbCodigoProduto, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 180, 60, 30));

        txtCodigoProduto.setBackground(new java.awt.Color(4, 154, 3));
        txtCodigoProduto.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        txtCodigoProduto.setForeground(new java.awt.Color(255, 255, 255));
        txtCodigoProduto.setCaretColor(new java.awt.Color(255, 255, 255));
        txtCodigoProduto.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtCodigoProdutoActionPerformed(evt);
            }
        });
        jPanel4.add(txtCodigoProduto, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 170, 60, 40));

        lbProduto.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbProduto.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbProduto.setText("Artigo:");
        jPanel4.add(lbProduto, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 110, 60, 30));

        lbQuantidade.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbQuantidade.setText("Qtd:");
        jPanel4.add(lbQuantidade, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 180, 39, 30));

        btn_remover.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/2934_32x32.png"))); // NOI18N
        btn_remover.setToolTipText("click para remover produtos do carrinho");
        btn_remover.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btn_removerActionPerformed(evt);
            }
        });
        jPanel4.add(btn_remover, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 230, 50, 50));

        lbQuantidadeStock.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbQuantidadeStock.setText("Q.Stock:");
        jPanel4.add(lbQuantidadeStock, new org.netbeans.lib.awtextra.AbsoluteConstraints(308, 143, 70, 50));

        lbCodigoProduto1.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbCodigoProduto1.setText("CodBarra:");
        jPanel4.add(lbCodigoProduto1, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 210, 100, 33));

        txtCodigoBarra.setBackground(new java.awt.Color(4, 154, 3));
        txtCodigoBarra.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        txtCodigoBarra.setForeground(new java.awt.Color(255, 255, 255));
        txtCodigoBarra.setCaretColor(new java.awt.Color(255, 255, 255));
        txtCodigoBarra.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtCodigoBarraActionPerformed(evt);
            }
        });
        jPanel4.add(txtCodigoBarra, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 210, 180, 33));

        cmbSubFamilia.setBackground(new java.awt.Color(4, 154, 3));
        cmbSubFamilia.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        cmbSubFamilia.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbSubFamilia.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbSubFamiliaActionPerformed(evt);
            }
        });
        jPanel4.add(cmbSubFamilia, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 80, 360, 31));

        cmbProduto.setBackground(new java.awt.Color(4, 154, 3));
        cmbProduto.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        cmbProduto.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbProduto.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbProdutoActionPerformed(evt);
            }
        });
        jPanel4.add(cmbProduto, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 110, 360, 30));

        txtQuantidadeStock.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txtQuantidadeStock.setForeground(new java.awt.Color(255, 255, 255));
        jPanel4.add(txtQuantidadeStock, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 140, 70, 60));

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/proucura.png"))); // NOI18N
        jButton4.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel4.add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 200, 40, 40));

        txtPreco.setEditable(false);
        txtPreco.setBackground(new java.awt.Color(4, 154, 3));
        txtPreco.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txtPreco.setForeground(new java.awt.Color(255, 255, 255));
        txtPreco.setCaretColor(new java.awt.Color(255, 255, 255));
        jPanel4.add(txtPreco, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 140, 130, 30));

        txtQuatindade.setBackground(new java.awt.Color(4, 154, 3));
        txtQuatindade.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        txtQuatindade.setForeground(new java.awt.Color(255, 255, 255));
        txtQuatindade.setText("1");
        txtQuatindade.setCaretColor(new java.awt.Color(255, 255, 255));
        txtQuatindade.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtQuatindadeActionPerformed(evt);
            }
        });
        jPanel4.add(txtQuatindade, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 180, 90, 30));

        lbCodigoProduto2.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbCodigoProduto2.setText("CodManual:");
        jPanel4.add(lbCodigoProduto2, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 240, 90, 33));

        txtCodigoManual.setBackground(new java.awt.Color(4, 154, 3));
        txtCodigoManual.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        txtCodigoManual.setForeground(new java.awt.Color(255, 255, 255));
        txtCodigoManual.setCaretColor(new java.awt.Color(255, 255, 255));
        txtCodigoManual.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtCodigoManualActionPerformed(evt);
            }
        });
        jPanel4.add(txtCodigoManual, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 240, 180, 33));

        cmbFamilia.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        cmbFamilia.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbFamiliaActionPerformed(evt);
            }
        });
        jPanel4.add(cmbFamilia, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 50, 360, 30));

        lbCategoria1.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbCategoria1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbCategoria1.setText("Sub Família:");
        jPanel4.add(lbCategoria1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 90, 100, -1));

        lbPreco1.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbPreco1.setText("Preco:");
        jPanel4.add(lbPreco1, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 140, 50, -1));

        btn_adicionar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/Button-Add-icon.png"))); // NOI18N
        btn_adicionar.setToolTipText("click para adicionar no carrinho");
        btn_adicionar.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btn_adicionarActionPerformed(evt);
            }
        });
        jPanel4.add(btn_adicionar, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 230, 50, 50));

        jPanelData.setBorder(javax.swing.BorderFactory.createTitledBorder("-"));

        cmbArmazem.setBackground(new java.awt.Color(4, 154, 3));
        cmbArmazem.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 1, 10)); // NOI18N
        cmbArmazem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbArmazemActionPerformed(evt);
            }
        });

        cmbAnoEconomico.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbAnoEconomico.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbAnoEconomicoActionPerformed(evt);
            }
        });

        buttonGroup5.add(rbArmazem);
        rbArmazem.setSelected(true);
        rbArmazem.setEnabled(false);
        rbArmazem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                rbArmazemActionPerformed(evt);
            }
        });

        buttonGroup5.add(rbArmazem1);
        rbArmazem1.setEnabled(false);
        rbArmazem1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                rbArmazem1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelDataLayout = new javax.swing.GroupLayout(jPanelData);
        jPanelData.setLayout(jPanelDataLayout);
        jPanelDataLayout.setHorizontalGroup(
            jPanelDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelDataLayout.createSequentialGroup()
                .addComponent(cmbAnoEconomico, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rbArmazem1)
                .addGap(18, 18, 18)
                .addComponent(rbArmazem)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbArmazem, javax.swing.GroupLayout.PREFERRED_SIZE, 305, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanelDataLayout.setVerticalGroup(
            jPanelDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(rbArmazem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(rbArmazem1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelDataLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(cmbAnoEconomico, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(cmbArmazem)
        );

        jPanel4.add(jPanelData, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 460, 50));

        buttonGroup3.add(ck_A4);
        ck_A4.setFont(new java.awt.Font("Lucida Grande", 0, 10)); // NOI18N
        ck_A4.setText("A4");
        ck_A4.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ck_A4ActionPerformed(evt);
            }
        });
        jPanel4.add(ck_A4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 290, -1, -1));

        buttonGroup3.add(ck_simplificada);
        ck_simplificada.setFont(new java.awt.Font("Lucida Grande", 0, 10)); // NOI18N
        ck_simplificada.setText("A6");
        ck_simplificada.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ck_simplificadaActionPerformed(evt);
            }
        });
        jPanel4.add(ck_simplificada, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 290, -1, -1));

        buttonGroup3.add(ck_A7);
        ck_A7.setFont(new java.awt.Font("Lucida Grande", 0, 10)); // NOI18N
        ck_A7.setText("A7");
        ck_A7.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ck_A7ActionPerformed(evt);
            }
        });
        jPanel4.add(ck_A7, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 290, -1, -1));

        buttonGroup3.add(ck_Duplicada);
        ck_Duplicada.setFont(new java.awt.Font("Lucida Grande", 0, 10)); // NOI18N
        ck_Duplicada.setText("A5");
        ck_Duplicada.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ck_DuplicadaActionPerformed(evt);
            }
        });
        jPanel4.add(ck_Duplicada, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 290, -1, -1));

        jlStockNegativo1.setForeground(new java.awt.Color(153, 0, 51));
        jlStockNegativo1.setText("Formatos ");
        jPanel4.add(jlStockNegativo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 270, 110, 20));

        buttonGroup3.add(ck_ComVirgula);
        ck_ComVirgula.setFont(new java.awt.Font("Lucida Grande", 0, 10)); // NOI18N
        ck_ComVirgula.setText("A6V");
        ck_ComVirgula.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ck_ComVirgulaActionPerformed(evt);
            }
        });
        jPanel4.add(ck_ComVirgula, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 290, 55, -1));

        buttonGroup3.add(ck_S_A6);
        ck_S_A6.setFont(new java.awt.Font("Lucida Grande", 0, 10)); // NOI18N
        ck_S_A6.setText("S_A6");
        ck_S_A6.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ck_S_A6ActionPerformed(evt);
            }
        });
        jPanel4.add(ck_S_A6, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 290, -1, -1));

        buttonGroup3.add(ck_simplificada_O_S);
        ck_simplificada_O_S.setFont(new java.awt.Font("Lucida Grande", 0, 10)); // NOI18N
        ck_simplificada_O_S.setText("S_A6_O");
        ck_simplificada_O_S.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ck_simplificada_O_SActionPerformed(evt);
            }
        });
        jPanel4.add(ck_simplificada_O_S, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 290, -1, -1));

        buttonGroup3.add(ck_simplificada_O);
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
        jPanel4.add(ck_simplificada_O, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 290, -1, -1));

        jPanel2.setBackground(new java.awt.Color(204, 204, 204));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        table.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "Cod.", "Descrição", "Un.", "Preço", "Qtd.", "Desc.", "Taxa ", "Ret.", "V. Ret.", "Valor", "Valor C/ Imposto"
            }
        )
        {
            Class[] types = new Class []
            {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Double.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean []
            {
                false, false, false, false, true, true, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex)
            {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex)
            {
                return canEdit [columnIndex];
            }
        });
        table.setCellSelectionEnabled(true);
        table.setGridColor(new java.awt.Color(51, 153, 0));
        table.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                tableMouseClicked(evt);
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
        table.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        if (table.getColumnModel().getColumnCount() > 0)
        {
            table.getColumnModel().getColumn(0).setPreferredWidth(30);
            table.getColumnModel().getColumn(1).setPreferredWidth(350);
            table.getColumnModel().getColumn(2).setPreferredWidth(30);
            table.getColumnModel().getColumn(3).setPreferredWidth(100);
            table.getColumnModel().getColumn(4).setPreferredWidth(40);
            table.getColumnModel().getColumn(5).setPreferredWidth(50);
            table.getColumnModel().getColumn(6).setPreferredWidth(40);
            table.getColumnModel().getColumn(7).setPreferredWidth(30);
            table.getColumnModel().getColumn(8).setPreferredWidth(75);
            table.getColumnModel().getColumn(9).setPreferredWidth(85);
            table.getColumnModel().getColumn(10).setPreferredWidth(110);
        }

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 958, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 6, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder("Pesquisar Clientes"));

        lbCliente.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbCliente.setText("Iniciais:");

        cmbCliente.setBackground(new java.awt.Color(4, 154, 3));
        cmbCliente.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        cmbCliente.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbCliente.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbClienteActionPerformed(evt);
            }
        });

        txtCodClientePesquisa.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtCodClientePesquisaActionPerformed(evt);
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

        lbCliente1.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbCliente1.setText("Nome:");

        txtNifClientePesquisa.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtNifClientePesquisaActionPerformed(evt);
            }
        });

        txtIniciaisCliente.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtIniciaisClienteActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        jLabel2.setText("Cod.");

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

        lbClienteConsumidorFinal.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbClienteConsumidorFinal.setText("NIF");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtIniciaisCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(lbCliente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(6, 6, 6)))
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbCliente1, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNomeConsumidorFinal, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(lbClienteConsumidorFinal, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(txtNifClientePesquisa)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtCodClientePesquisa, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(49, 49, 49))
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(cmbCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 413, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lbCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbCliente1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtIniciaisCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtNomeConsumidorFinal, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lbClienteConsumidorFinal, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtCodClientePesquisa, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtNifClientePesquisa, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jLabel1.setFont(new java.awt.Font("Superclarendon", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(4, 154, 3));
        jLabel1.setText("KITANDA v1.1");

        lbVias.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbVias.setText("Via(s):");

        lbPreco2.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbPreco2.setText("Ref:");

        rbTranstorno.setSelected(true);
        rbTranstorno.setEnabled(false);
        rbTranstorno.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                rbTranstornoActionPerformed(evt);
            }
        });

        jlStockNegativo.setText("Stock Negativo");

        lb_proximo_documento.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        lb_proximo_documento.setText("PRÓX. DOC. : XX PP/A1");

        jPanelCaracteristicas.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Características", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 12), new java.awt.Color(204, 0, 0))); // NOI18N

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel3.setText("Marca:");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel4.setText("Motorista:");

        txtMarca.setFont(new java.awt.Font("Lucida Grande", 0, 12)); // NOI18N
        txtMarca.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtMarcaActionPerformed(evt);
            }
        });

        txtMotorista.setFont(new java.awt.Font("Lucida Grande", 0, 12)); // NOI18N
        txtMotorista.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtMotoristaActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel5.setText("Matricula");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel6.setText("Nº Carta:");

        txtMatricula.setFont(new java.awt.Font("Lucida Grande", 0, 12)); // NOI18N
        txtMatricula.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtMatriculaActionPerformed(evt);
            }
        });

        txtNumeroCarta.setFont(new java.awt.Font("Lucida Grande", 0, 12)); // NOI18N
        txtNumeroCarta.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtNumeroCartaActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel7.setText("Klm:");

        txtKilometragem.setFont(new java.awt.Font("Lucida Grande", 0, 12)); // NOI18N
        txtKilometragem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtKilometragemActionPerformed(evt);
            }
        });

        txtLocal.setEditable(false);

        javax.swing.GroupLayout jPanelCaracteristicasLayout = new javax.swing.GroupLayout(jPanelCaracteristicas);
        jPanelCaracteristicas.setLayout(jPanelCaracteristicasLayout);
        jPanelCaracteristicasLayout.setHorizontalGroup(
            jPanelCaracteristicasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelCaracteristicasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelCaracteristicasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 61, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelCaracteristicasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtMarca, javax.swing.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE)
                    .addComponent(txtMotorista))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelCaracteristicasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtLocal, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(txtKilometragem, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanelCaracteristicasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel6)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 14, Short.MAX_VALUE)
                .addGroup(jPanelCaracteristicasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtMatricula)
                    .addComponent(txtNumeroCarta))
                .addGap(24, 24, 24))
        );
        jPanelCaracteristicasLayout.setVerticalGroup(
            jPanelCaracteristicasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelCaracteristicasLayout.createSequentialGroup()
                .addGroup(jPanelCaracteristicasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanelCaracteristicasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtMarca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanelCaracteristicasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtMatricula, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addComponent(txtKilometragem, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                .addGroup(jPanelCaracteristicasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelCaracteristicasLayout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addGroup(jPanelCaracteristicasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtMotorista, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtNumeroCarta)))
                    .addGroup(jPanelCaracteristicasLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtLocal))))
            .addGroup(jPanelCaracteristicasLayout.createSequentialGroup()
                .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(7, 7, 7)
                .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        lbPreco3.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbPreco3.setText("Doc.");

        cmbTipoDocumento.setBackground(new java.awt.Color(4, 154, 3));
        cmbTipoDocumento.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 14)); // NOI18N
        cmbTipoDocumento.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbTipoDocumentoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanelCaracteristicas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(lbPreco3, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)
                        .addComponent(cmbTipoDocumento, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lb_proximo_documento, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addComponent(jPanelCaracteristicas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cmbTipoDocumento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lbPreco3))
                    .addComponent(lb_proximo_documento, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        rbMostrar.setSelected(true);
        rbMostrar.setEnabled(false);
        rbMostrar.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                rbMostrarActionPerformed(evt);
            }
        });

        dc_data_documento.setEnabled(false);
        dc_data_documento.setFont(new java.awt.Font("Lucida Grande", 1, 12)); // NOI18N

        dc_data_vencimento.setEnabled(false);
        dc_data_vencimento.setFont(new java.awt.Font("Lucida Grande", 1, 12)); // NOI18N

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        jLabel9.setText("DATA VENCIMENTO");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jPanel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel5Layout.createSequentialGroup()
                            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel5Layout.createSequentialGroup()
                                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(lbPreco2, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(5, 5, 5)
                                    .addComponent(txtReferencia, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel5Layout.createSequentialGroup()
                                    .addComponent(lbVias, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(spnCopia, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jlStockNegativo, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(rbTranstorno, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(rbMostrar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGap(14, 14, 14)
                                    .addComponent(jLabel9)))
                            .addGap(10, 10, 10)
                            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(dc_data_documento, javax.swing.GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE)
                                .addComponent(dc_data_vencimento, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, 472, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lbPreco2)
                        .addComponent(txtReferencia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(dc_data_documento, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(lbVias)
                                .addComponent(spnCopia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jlStockNegativo))
                            .addComponent(dc_data_vencimento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(rbTranstorno, javax.swing.GroupLayout.PREFERRED_SIZE, 7, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(rbMostrar, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(61, 61, 61))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 484, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 476, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        getAccessibleContext().setAccessibleName("...:::::  KITANDA - FACTURAÃO ::::...");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void mostra_consumidor_final()
    {

        if ( cmbCliente.getSelectedItem().equals( "Consumidor Final" ) )
        {
            lbClienteConsumidorFinal.setVisible( true );
            txtNomeConsumidorFinal.setVisible( true );
        }
        else
        {
            lbClienteConsumidorFinal.setVisible( false );
            txtNomeConsumidorFinal.setVisible( false );
        }

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

    private void btn_removerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_removerActionPerformed
        try
        {

            remover_item_carrinho();

        }
        catch ( Exception ex )
        {
            //Logger.getLogger(VendaUsuarioVisao.class.getName()).log(Level.SEVERE, null, ex);
//            JOptionPane.showMessageDialog( null, "Possivelmente não selecionaste \n nenhuma linha ou não existe dados na tabela", "AVISO", JOptionPane.WARNING_MESSAGE );
        }


    }//GEN-LAST:event_btn_removerActionPerformed

    private void btn_adicionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_adicionarActionPerformed
        if ( validar() )
        {
            adicionar_botao();
            scrolltable();
        }

    }//GEN-LAST:event_btn_adicionarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void txtCodigoProdutoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCodigoProdutoActionPerformed
        // TODO add your handling code here:
        if ( validar() )
        {
            accao_codigo_interno_enter();
            scrolltable();
        }
    }//GEN-LAST:event_txtCodigoProdutoActionPerformed

    private void cmbArmazemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbArmazemActionPerformed
        // TODO add your handling code here:
        adicionar_preco_quantidade();
//        configurar_armazens();
    }//GEN-LAST:event_cmbArmazemActionPerformed

    private void txtCodigoBarraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCodigoBarraActionPerformed
        // TODO add your handling code here:
        accao_codigo_barra_enter();
    }//GEN-LAST:event_txtCodigoBarraActionPerformed

    private void cmbClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbClienteActionPerformed
        // TODO add your handling code here:
        accao_cliente();
        mostra_consumidor_final();
    }//GEN-LAST:event_cmbClienteActionPerformed

    private void txtQuatindadeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtQuatindadeActionPerformed
        // TODO add your handling code here:
        if ( validar() )
        {

            setFocus( dadosInstituicao.getFoco() );
            txtCodigoBarra.setText( "" );
        }
    }//GEN-LAST:event_txtQuatindadeActionPerformed

    private void txtTotalPagarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTotalPagarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTotalPagarActionPerformed

    private void cmbSubFamiliaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSubFamiliaActionPerformed

        cmbProduto.setModel( new DefaultComboBoxModel( ( produtosController.getVectorByIdTipoProduto( getIdTipoProduto() ) ) ) );

    }//GEN-LAST:event_cmbSubFamiliaActionPerformed

    private void cmbProdutoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbProdutoActionPerformed
        // TODO add your handling code here:
        try
        {
            adicionar_preco_quantidade();
        }
        catch ( Exception e )
        {
        }
    }//GEN-LAST:event_cmbProdutoActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed

        try
        {
            if ( validar() )
            {
                System.out.println( "Codigo do Armazem em questão: " + getCodigoArmazem() );
                new BuscaProdutoVisao( this, rootPaneCheckingEnabled, getCodigoArmazem(), DVML.JANELA_VENDA, conexao ).show();
            }
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }

    }//GEN-LAST:event_jButton4ActionPerformed

    private void cmbTipoDocumentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbTipoDocumentoActionPerformed
        // TODO add your handling code here:
        mostrar_proximo_codigo_documento();
        actualizar_abreviacao();
        desabilitar_campos();
        atualizarCliente1();
        atualizarDataVencimentoFA();
    }//GEN-LAST:event_cmbTipoDocumentoActionPerformed

    private void cmbMoedaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbMoedaActionPerformed
        // TODO add your handling code here:
        actualizar_moeda();

    }//GEN-LAST:event_cmbMoedaActionPerformed

    private void ck_A4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ck_A4ActionPerformed
        // TODO add your handling code here:
        actualizar_abreviacao();
    }//GEN-LAST:event_ck_A4ActionPerformed

    private void ck_simplificadaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ck_simplificadaActionPerformed
        // TODO add your handling code here:
        actualizar_abreviacao();
    }//GEN-LAST:event_ck_simplificadaActionPerformed

    private void sp_desconto_financeiroStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_sp_desconto_financeiroStateChanged
        // TODO add your handling code here:
        tratar_desconto();
    }//GEN-LAST:event_sp_desconto_financeiroStateChanged

    private void sp_desconto_financeiroInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_sp_desconto_financeiroInputMethodTextChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_sp_desconto_financeiroInputMethodTextChanged

    private void sp_desconto_financeiroPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_sp_desconto_financeiroPropertyChange
        // TODO add your handling code here:
    }//GEN-LAST:event_sp_desconto_financeiroPropertyChange

    private void sp_desconto_financeiroKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_sp_desconto_financeiroKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_sp_desconto_financeiroKeyTyped

    private void sp_desconto_financeiroKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_sp_desconto_financeiroKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_sp_desconto_financeiroKeyPressed

    private void txtCodigoManualActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCodigoManualActionPerformed
        accao_codigo_manual_enter();
    }//GEN-LAST:event_txtCodigoManualActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        new ClienteVisao( this, rootPaneCheckingEnabled, conexao ).show();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void txtNifClientePesquisaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNifClientePesquisaActionPerformed
        pesquisa_cliente_by_nif();
    }//GEN-LAST:event_txtNifClientePesquisaActionPerformed

    private void cmbFamiliaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbFamiliaActionPerformed

        cmbSubFamilia.setModel( new DefaultComboBoxModel( tipoProdutoController.getVectorByIdFamilia( getIdFamilia() ) ) );
        cmbProduto.setModel( new DefaultComboBoxModel( ( produtosController.getVectorByIdTipoProduto( getIdTipoProduto() ) ) ) );

    }//GEN-LAST:event_cmbFamiliaActionPerformed

    private void cmbAnoEconomicoActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbAnoEconomicoActionPerformed
    {//GEN-HEADEREND:event_cmbAnoEconomicoActionPerformed
        // TODO add your handling code here:
        mostrar_proximo_codigo_documento();
        actualizar_abreviacao();
    }//GEN-LAST:event_cmbAnoEconomicoActionPerformed

    private void txtNomeConsumidorFinalActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtNomeConsumidorFinalActionPerformed
    {//GEN-HEADEREND:event_txtNomeConsumidorFinalActionPerformed
        txtQuatindade.requestFocus();


    }//GEN-LAST:event_txtNomeConsumidorFinalActionPerformed

    private void txtNomeConsumidorFinalKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtNomeConsumidorFinalKeyPressed
    {//GEN-HEADEREND:event_txtNomeConsumidorFinalKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNomeConsumidorFinalKeyPressed

    private void txtMarcaActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtMarcaActionPerformed
    {//GEN-HEADEREND:event_txtMarcaActionPerformed
        txtKilometragem.requestFocus();
    }//GEN-LAST:event_txtMarcaActionPerformed

    private void txtMotoristaActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtMotoristaActionPerformed
    {//GEN-HEADEREND:event_txtMotoristaActionPerformed
        txtNumeroCarta.requestFocus();
    }//GEN-LAST:event_txtMotoristaActionPerformed

    private void txtMatriculaActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtMatriculaActionPerformed
    {//GEN-HEADEREND:event_txtMatriculaActionPerformed
        txtMotorista.requestFocus();
    }//GEN-LAST:event_txtMatriculaActionPerformed

    private void txtNumeroCartaActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtNumeroCartaActionPerformed
    {//GEN-HEADEREND:event_txtNumeroCartaActionPerformed
        txtQuatindade.requestFocus();
    }//GEN-LAST:event_txtNumeroCartaActionPerformed

    private void txtKilometragemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtKilometragemActionPerformed
    {//GEN-HEADEREND:event_txtKilometragemActionPerformed
        txtMatricula.requestFocus();
    }//GEN-LAST:event_txtKilometragemActionPerformed

    private void ck_A7ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_ck_A7ActionPerformed
    {//GEN-HEADEREND:event_ck_A7ActionPerformed
        // TODO add your handling code here:
        actualizar_abreviacao();
    }//GEN-LAST:event_ck_A7ActionPerformed

    private void ck_DuplicadaActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_ck_DuplicadaActionPerformed
    {//GEN-HEADEREND:event_ck_DuplicadaActionPerformed
        // TODO add your handling code here:
        actualizar_abreviacao();
    }//GEN-LAST:event_ck_DuplicadaActionPerformed

    private void ck_S_A6ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_ck_S_A6ActionPerformed
    {//GEN-HEADEREND:event_ck_S_A6ActionPerformed
        actualizar_abreviacao();
    }//GEN-LAST:event_ck_S_A6ActionPerformed

    private void btnFormaPagamentoActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnFormaPagamentoActionPerformed
    {//GEN-HEADEREND:event_btnFormaPagamentoActionPerformed
        // TODO add your handling code here:
//         new FormaPagamentoVisao( this, rootPaneCheckingEnabled, DVML.VENDA_PONTUAL, emf ).setVisible( true );
        if ( MetodosUtil.licencaValidada( conexao ) )
        {
            new FormaPagamentoVisao( this, rootPaneCheckingEnabled, null, DVML.VENDA_PONTUAL, conexao ).setVisible( true );

        }
    }//GEN-LAST:event_btnFormaPagamentoActionPerformed

    private void btnProcessarActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnProcessarActionPerformed
    {//GEN-HEADEREND:event_btnProcessarActionPerformed
        // TODO add your handling code here:
        if ( validar() )
        {
            if ( MetodosUtil.licencaValidada( conexao ) )
            {
                procedimento_salvar_venda_comercial();

            }

        }
    }//GEN-LAST:event_btnProcessarActionPerformed

    private void ck_ComVirgulaActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_ck_ComVirgulaActionPerformed
    {//GEN-HEADEREND:event_ck_ComVirgulaActionPerformed
        actualizar_abreviacao();
    }//GEN-LAST:event_ck_ComVirgulaActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton1ActionPerformed
    {//GEN-HEADEREND:event_jButton1ActionPerformed

        dispose();
        new LoginVisao();
        new CaixaAberturaVisao( cod_usuario, conexao, false ).setVisible( true );
//        fazerBackupAgora();
    }//GEN-LAST:event_jButton1ActionPerformed

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

    private void rbTranstornoActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_rbTranstornoActionPerformed
    {//GEN-HEADEREND:event_rbTranstornoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rbTranstornoActionPerformed

    private void rbMostrarActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_rbMostrarActionPerformed
    {//GEN-HEADEREND:event_rbMostrarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rbMostrarActionPerformed

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

    private void txtIniciaisClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIniciaisClienteActionPerformed
        txtQuatindade.requestFocus();
    }//GEN-LAST:event_txtIniciaisClienteActionPerformed

    private void tablePropertyChange(java.beans.PropertyChangeEvent evt)//GEN-FIRST:event_tablePropertyChange
    {//GEN-HEADEREND:event_tablePropertyChange
        // TODO add your handling code here:

//        if ( table.getSelectedColumn() == 3 )
//        {
//           
//        }
        if ( table.getSelectedColumn() == 3 || table.getSelectedColumn() == 4 || table.getSelectedColumn() == 5 )
        {
            actualizarPreco();
            System.out.println( "Qtd......" );
            actualizarQtdTable();
        }

    }//GEN-LAST:event_tablePropertyChange

    private void tableMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_tableMouseClicked
    {//GEN-HEADEREND:event_tableMouseClicked
        // TODO add your handling code here:
        if ( evt.getClickCount() > 2 )
        {
            System.out.println( "xx" );
        }
    }//GEN-LAST:event_tableMouseClicked

    private void ck_simplificada_O_SActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_ck_simplificada_O_SActionPerformed
    {//GEN-HEADEREND:event_ck_simplificada_O_SActionPerformed
        actualizar_abreviacao();
    }//GEN-LAST:event_ck_simplificada_O_SActionPerformed

    private void ck_simplificada_OActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_ck_simplificada_OActionPerformed
    {//GEN-HEADEREND:event_ck_simplificada_OActionPerformed
        actualizar_abreviacao();
    }//GEN-LAST:event_ck_simplificada_OActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton2ActionPerformed
    {//GEN-HEADEREND:event_jButton2ActionPerformed
        try
        {
            new VistoriaVisao( cod_usuario, this.conexao ).setVisible( true );
        }
        catch ( Exception e )
        {
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void txtCodClientePesquisaActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtCodClientePesquisaActionPerformed
    {//GEN-HEADEREND:event_txtCodClientePesquisaActionPerformed
        pesquisa_cliente_by_cod();
    }//GEN-LAST:event_txtCodClientePesquisaActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelar;
    private static javax.swing.JButton btnFormaPagamento;
    private static javax.swing.JButton btnProcessar;
    public static javax.swing.JButton btn_adicionar;
    public static javax.swing.JButton btn_remover;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.ButtonGroup buttonGroup3;
    private javax.swing.ButtonGroup buttonGroup4;
    private javax.swing.ButtonGroup buttonGroup5;
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
    public static javax.swing.JComboBox<String> cmbFamilia;
    public static javax.swing.JComboBox cmbMoeda;
    public static javax.swing.JComboBox cmbProduto;
    public static javax.swing.JComboBox cmbSubFamilia;
    public static javax.swing.JComboBox cmbTipoDocumento;
    private static com.toedter.calendar.JDateChooser dc_data_documento;
    private static com.toedter.calendar.JDateChooser dc_data_vencimento;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanelCaracteristicas;
    private javax.swing.JPanel jPanelData;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel jlEmpresa;
    private javax.swing.JLabel jlStockNegativo;
    private javax.swing.JLabel jlStockNegativo1;
    private javax.swing.JLabel lbCategoria;
    private javax.swing.JLabel lbCategoria1;
    private javax.swing.JLabel lbCliente;
    private javax.swing.JLabel lbCliente1;
    private javax.swing.JLabel lbClienteConsumidorFinal;
    private javax.swing.JLabel lbCodigoProduto;
    private javax.swing.JLabel lbCodigoProduto1;
    private javax.swing.JLabel lbCodigoProduto2;
    private javax.swing.JLabel lbDescontoFinanceiro;
    private javax.swing.JLabel lbPreco1;
    private javax.swing.JLabel lbPreco2;
    private javax.swing.JLabel lbPreco3;
    private javax.swing.JLabel lbPreco7;
    private javax.swing.JLabel lbProduto;
    private javax.swing.JLabel lbQuantidade;
    private javax.swing.JLabel lbQuantidadeStock;
    private javax.swing.JLabel lbTotalPagar1;
    private javax.swing.JLabel lbTotalPagar2;
    public static javax.swing.JLabel lbValorPorExtenco;
    private javax.swing.JLabel lbVias;
    private javax.swing.JLabel lb_nome_usuario;
    private static javax.swing.JLabel lb_proximo_documento;
    private javax.swing.JLabel lb_usuario;
    private static javax.swing.JRadioButton rbArmazem;
    private static javax.swing.JRadioButton rbArmazem1;
    private static javax.swing.JRadioButton rbMostrar;
    private static javax.swing.JRadioButton rbTranstorno;
    private static javax.swing.JSpinner sp_desconto_financeiro;
    private static javax.swing.JSpinner spnCopia;
    public static javax.swing.JTable table;
    private javax.swing.JTextField txtCodClientePesquisa;
    public static javax.swing.JTextField txtCodigoBarra;
    public static javax.swing.JTextField txtCodigoManual;
    public static javax.swing.JTextField txtCodigoProduto;
    private static javax.swing.JTextField txtIniciaisCliente;
    private static javax.swing.JTextField txtKilometragem;
    private javax.swing.JTextField txtLocal;
    private static javax.swing.JTextField txtMarca;
    private static javax.swing.JTextField txtMatricula;
    private static javax.swing.JTextField txtMotorista;
    private static javax.swing.JTextField txtNifClientePesquisa;
    private static javax.swing.JTextField txtNomeConsumidorFinal;
    private static javax.swing.JTextField txtNumeroCarta;
    private static javax.swing.JTextField txtObs;
    public static javax.swing.JTextField txtPreco;
    public static javax.swing.JTextField txtQuantidadeStock;
    public static javax.swing.JTextField txtQuatindade;
    private static javax.swing.JTextField txtReferencia;
    public static javax.swing.JTextField txtTotalPagar;
    private static javax.swing.JLabel txtTotal_AOA_Retencao;
    // End of variables declaration//GEN-END:variables

    //verifica se o produto existe na tabela do formulário visão isto é na jTable
    private static boolean exist_produto_tabela_formulario( int codigo )
    {

        DefaultTableModel modelo = (DefaultTableModel) table.getModel();

        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {
            if ( Integer.parseInt( String.valueOf( table.getValueAt( i, 0 ) ) ) == codigo )
            {
                linha_actual = i;
                return true;
            }
        }
        return false;

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
            Logger.getLogger( VendaUsuarioVisao.class.getName() ).log( Level.SEVERE, null, ex );
            JOptionPane.showMessageDialog( null, "Não existe produto com este código de barra.", DVML.DVML_COMERCIAL, JOptionPane.ERROR_MESSAGE );
        }

    }

//            double subtotal_linha = ( preco_venda * qtd );
//        double desconto_valor = ( subtotal_linha * ( desconto / 100 ) );
//        double valor_iva = 1 + ( taxa / 100 );//
//        return ( ( subtotal_linha - desconto_valor ) * valor_iva );
    //actualiza a quantidade na tabela do formulário visão isto é na jTable
    private static void actuazlizar_quantidade_tabela_formulario( String quantidade, double desconto )
    {
        DefaultTableModel modelo = (DefaultTableModel) table.getModel();
        double qtd = Double.parseDouble( quantidade );
        double retencao = 0;

        double preco_venda = CfMethods.parseMoedaFormatada( String.valueOf( modelo.getValueAt( linha_actual, 3 ) ) );
        double taxa = Double.parseDouble( String.valueOf( modelo.getValueAt( linha_actual, 6 ) ) );
//        double ret = CfMethods.parseMoedaFormatada( String.valueOf( modelo.getValueAt( linha_actual, 7) ) );
        double ret = Double.parseDouble( String.valueOf( modelo.getValueAt( linha_actual, 7 ) ) );

//        double total_iliquido_linha = (( preco_venda * qtd ) - desconto);
        String total_iliquido_linha = CfMethods.formatarComoMoeda( getValorIliquido(
                qtd,
                preco_venda,
                desconto
        ) );

        String total_liquido_linha = CfMethods.formatarComoMoeda( getValorComImpostoIva(
                qtd,
                taxa,
                preco_venda,
                desconto
        ) );

        retencao = getValorComRetencao( qtd, ret, preco_venda, desconto );

        String total_retencao = CfMethods.formatarComoMoeda( retencao );

        modelo.setValueAt( qtd, linha_actual, 4 );
        modelo.setValueAt( desconto, linha_actual, 5 );
        modelo.setValueAt( total_retencao, linha_actual, 8 );
//
        modelo.setValueAt( total_iliquido_linha, linha_actual, 9 );
        modelo.setValueAt( total_liquido_linha, linha_actual, 10 );
        //a linha_actual recebe o default
        linha_actual = -1;

    }

//    public static void procedimento_salvar()
//    {
//        if ( !MetodosUtil.tabela_vazia( table ) )
//        {
//
//            if ( verifica_ano_documento_igual_economico() )
//            {
//
//                if ( data_documento_superior_ou_igual_ao_ultimo_doc() )
//                {
//                    /**
//                     * aviso se for necessário ao utilizador quando a data do
//                     * documento é superior à actual ou seja do sistema
//                     */
//                    data_documento_superior_data_actual();
//                    if ( aviso_continuar_documento )
//                    {
//                        //calcularTroco();
//                        if ( !campos_invalido_imprimir() )
//                        {
//                            if ( !transtorno() )
//                            {
//                                System.out.println( "STATUS: a processar a factura" );
//                                EntityManager em = JPAEntityMannagerFactoryUtil.createEntityManager();
//                                em.getTransaction().begin();
//
//                                if ( DVML.MOEDA_KWANZA.equalsIgnoreCase( getMoeda().getAbreviacao() ) )
//                                {
//                                    salvar_venda();
//                                }
//                                else
//                                {
//                                    // JOptionPane.showMessageDialog(null, "Moeda Estrangeira");
//                                    actualizar_moeda( DVML.MOEDA_KWANZA );
//                                    salvar_venda();
//                                }
//
//                                em.getTransaction().commit();
//                                //actualiza a data para o próximo documento.
//                                dc_data_documento.setDate( new Date() );
//                                System.out.println( "STATUS: fim do processamento da factura" );
//                            }
//
//                        }
//                    }
//
//                }
//                else
//                {
//                    JOptionPane.showMessageDialog( null, "O documento não pode ser processado porque possui uma data inferior ao úlimo documento efectuado", "AVISO", JOptionPane.WARNING_MESSAGE );
//                }
//
//            }
//            else
//            {
//                JOptionPane.showMessageDialog( null, "A data do documento a ser emitido deve estar no intervalo do ano economico", "Aviso", JOptionPane.WARNING_MESSAGE );
//            }
//        }
//        else
//        {
//            JOptionPane.showMessageDialog( null, "Caro usuário adicione item na tabela", "Aviso", JOptionPane.WARNING_MESSAGE );
//        }
//
//    }
    private static void accao_cliente()
    {
        String nomeCliente = (String) cmbCliente.getSelectedItem();

        txtNomeConsumidorFinal.setText( nomeCliente );
        String nif = clientesController.findByNome( nomeCliente ).getNif();
        txtNifClientePesquisa.setText( nif );

//        txtClienteNome.setEditable (  ! clienteDiverso );
//        txtClienteNome.setText ( clienteDiverso ? "Consumidor Final" : nomeCliente );
//        txtClienteNome.setVisible ( cmbCliente.getSelectedItem ().equals ( "DIVERSOS" ) );
//        if ( clienteDiverso )
//        {
//            txtClienteNome.requestFocus ();
//        }
    }

    private void calcularTotalComDesconto()
    {
        double totalComDesconto = 0;
        double resultado = 0;
        double percentagem_desconto = 0;
        double total_pagar = 0;
        totalComDesconto = ( total_pagar * percentagem_desconto ) / 100;
        resultado = total_pagar - totalComDesconto;
        txtTotalPagar.setText( String.valueOf( MetodosUtil.retirar_dizimas( resultado ) ).trim() );

    }

    private void calcularTotalSemDesconto()
    {

        double totalSemDesconto = 0;
        double resultado1 = 0;
        double percentagem_desconto = 0;
        double total_pagar = Double.parseDouble( txtTotalPagar.getText().trim() );

        totalSemDesconto = total_pagar + ( percentagem_desconto ) / 100;
        resultado1 = total_pagar + totalSemDesconto;

    }

    public static void procedimento_adicionar()
    {

        try
        {

            if ( !campos_invalidos() )
            {
                if ( !isProdutoExpirado( getCodigoProduto() ) )
                {
                    TbProduto produto = (TbProduto) produtosController.findById( getCodigoProduto() );
                    if ( isStocavel( produto.getStocavel() ) )
                    {

                        if ( possivel_quantidade() )
                        {
                            if ( estado_critico() )
                            {
                                JOptionPane.showMessageDialog( null, "O produto: " + produto.getDesignacao() + " precisa de ser actualizado no stock", DVML.DVML_COMERCIAL, JOptionPane.WARNING_MESSAGE );
                            }
                            adicionar_produto();

                        }
                        else
                        {
                            JOptionPane.showMessageDialog( null, "O produto: " + produto.getDesignacao() + " nao pode ser vendido pra esta quantidade", DVML.DVML_COMERCIAL, JOptionPane.ERROR_MESSAGE );
                        }

                    }
                    else
                    {
                        adicionar_produto();
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

    public static void procedimento_adicionar_sem_transtorno()
    {

        try
        {

            if ( !campos_invalidos() )
            {
                if ( !isProdutoExpirado( getCodigoProduto() ) )
                {
                    TbProduto produto = (TbProduto) produtosController.findById( getCodigoProduto() );
                    if ( isStocavel( produto.getStocavel() ) )
                    {

                        if ( estado_critico() )
                        {
                            JOptionPane.showMessageDialog( null, "O produto: " + produto.getDesignacao() + " precisa de ser actualizado no stock", DVML.DVML_COMERCIAL, JOptionPane.WARNING_MESSAGE );
                        }
                        adicionar_produto();

                    }
                    else
                    {
                        adicionar_produto();
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
            Logger.getLogger( VendaUsuarioVisao.class.getName() ).log( Level.SEVERE, null, ex );
            JOptionPane.showMessageDialog( null, "Não existe produto com este código de barra.", DVML.DVML_COMERCIAL, JOptionPane.ERROR_MESSAGE );
        }

    }

    public void adicionar_botao_retificar()
    {

        try
        {
            if ( !campos_invalidos() )
            {

                TbProduto produto = (TbProduto) produtosController.findById( getCodigoProduto() );
                if ( isStocavel( produto.getStocavel() ) )
                {

                    if ( possivel_quantidade() )
                    {

                        if ( estado_critico() )
                        {
                            JOptionPane.showMessageDialog( null, "O produto: " + produto.getDesignacao() + " precisa de ser actualizado no stock", DVML.DVML_COMERCIAL, JOptionPane.WARNING_MESSAGE );
                        }
                        adicionar_produto();
                    }
                    else
                    {
                        JOptionPane.showMessageDialog( null, "O produto: " + produto.getDesignacao() + " nao pode ser vendido pra esta quantidade", DVML.DVML_COMERCIAL, JOptionPane.ERROR_MESSAGE );
                    }

                }
                else
                {
                    adicionar_produto();
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

    private void habilitar_campos_creditos()
    {

    }

    private static void procedimentoAdicionarTabela( TbProduto produto )
    {
        try
        {
            if ( !Objects.isNull( produto ) )
            {
                Integer codTipoProduto = produto.getCodTipoProduto().getCodigo();
                TbTipoProduto tipoProduto = (TbTipoProduto) tipoProdutoController.findById( codTipoProduto );
                Integer codFamilia = tipoProduto.getFkFamilia().getPkFamilia();
                Familia familia = (Familia) familiaController.findById( codFamilia );
                cmbFamilia.setSelectedItem( familia.getDesignacao() );
                cmbSubFamilia.setSelectedItem( tipoProduto.getDesignacao() );

                cmbProduto.setModel( new DefaultComboBoxModel( produtosController.getVector() ) );
                cmbProduto.setSelectedItem( produto.getDesignacao() );
                adicionar_preco_quantidade_anitgo();
                if ( rbTranstorno.isSelected() )
                {
                    procedimento_adicionar_sem_transtorno();
                }
                else
                {
                    procedimento_adicionar();
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

    private void accao_codigo_interno_enter()
    {
        try
        {
            int codigo = Integer.parseInt( txtCodigoProduto.getText() );
            TbProduto produto = (TbProduto) produtosController.findById( codigo );
            procedimentoAdicionarTabela( produto );
        }
        catch ( Exception ex )
        {
//            ex.printStackTrace();
            Logger.getLogger( VendaUsuarioVisao.class.getName() ).log( Level.SEVERE, null, ex );
            JOptionPane.showMessageDialog( null, "Este produto não existe no armazém " + cmbArmazem.getSelectedItem(), DVML.DVML_COMERCIAL, JOptionPane.ERROR_MESSAGE );
        }

    }

    public static void accao_codigo_interno_enter_busca_exterior( int codigo )
    {

        try
        {
            System.out.println( "ID PRODUTO EXTERIOR: " + codigo );
            TbProduto produtoLocal = (TbProduto) produtosController.findById( codigo );
            procedimentoAdicionarTabela( produtoLocal );
        }
        catch ( Exception ex )
        {
            ex.printStackTrace();
            Logger.getLogger( VendaUsuarioVisao.class.getName() ).log( Level.SEVERE, null, ex );
            JOptionPane.showMessageDialog( null, "Este produto não existe no armazém " + cmbArmazem.getSelectedItem(), DVML.DVML_COMERCIAL, JOptionPane.ERROR_MESSAGE );
        }

    }

//    private static Integer getIdBanco()
//    {
//        return bancoDao.getIdByDescricao( String.valueOf( cmbFormaPagamento.getSelectedItem() ) );
//    }
    private void desactivar_campos()
    {
//        lbValorEnregue.setVisible( false );
//        txtValorEntregue.setVisible( false );
//        lbTroco.setVisible( false );
//        txtTroco.setVisible( false );
        lbCliente.setVisible( false );
        cmbCliente.setVisible( false );

    }

    private void mostrar_nome()
    {
        TbUsuario usuario = (TbUsuario) usuariosController.getUsuarioByCodigo( this.cod_usuario );
        //caso masculino
//        if (usuario.getCodigoSexo().getCodigo() == 1) {
        lb_nome_usuario.setText( "Operador: " + usuario.getNome() );
//        } else {
//            lb_usuario.setText("Operadora: " + usuario.getNome());
//        }
    }

    private void empresa()
    {
        TbDadosInstituicao dados = (TbDadosInstituicao) dadosInstituicaoController.findById( 1 );

        jlEmpresa.setText( dados.getNome() );

    }

    public void adicionar_preco_quantidade()
    {

        try
        {

            TbProduto produto_local = (TbProduto) produtosController.findById( getCodigoProduto() );

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
            Logger.getLogger( VendaUsuarioVisao.class.getName() ).log( Level.SEVERE, null, ex );
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
                    TbPreco precoLocal = precosController.getLastIdPrecoByIdProduto( codigo_produto, Double.parseDouble( txtQuatindade.getText() ) );
                    txtPreco.setText( String.valueOf( MetodosUtil.retirar_dizimas( precoLocal.getPrecoVenda().doubleValue() ) ) );
                    txtQuantidadeStock.setText( String.valueOf( stockLocal.getQuantidadeExistente() ) );
                }

            }
        }
        catch ( Exception ex )
        {
            ex.printStackTrace();
            Logger.getLogger( VendaUsuarioVisao.class.getName() ).log( Level.SEVERE, null, ex );
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
            Logger.getLogger( VendaUsuarioVisao.class.getName() ).log( Level.SEVERE, null, ex );
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
                    TbProduto produtoLocal = (TbProduto) produtosController.findById( getCodigoProduto() );
                    if ( isStocavel( produtoLocal.getStocavel() ) )
                    {
                        if ( possivel_quantidade() )
                        {
                            if ( estado_critico() )
                            {
                                JOptionPane.showMessageDialog( null, "O produto: " + produtoLocal.getDesignacao() + " precisa de ser actualizado no stock", "DVML", JOptionPane.WARNING_MESSAGE );
                            }
                            adicionar_produto();

                        }
                        else
                        {
                            JOptionPane.showMessageDialog( null, "O produto: " + produtoLocal.getDesignacao() + " nao pode ser vendido pra esta quantidade", "DVML", JOptionPane.ERROR_MESSAGE );
                        }

                    }
                    else
                    {
                        adicionar_produto();
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
        txtObs.setText( "" );
        gorjeta = 0;
        reset_desconto_global();
        reset_valor_entregue();

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

    private static boolean transtorno()
    {

        String statusTranstorno = dadosInstituicaoController.findByCodigo( 1 ).getTranstorno();

        if ( statusTranstorno.equals( "Activo" ) )
        {
            return false;
        }

        int cod_produto = 0;
        double qtd = 0d;
        double qtd_aceite = 0d;

        DefaultTableModel modelo = (DefaultTableModel) table.getModel();
        boolean transtorno = false;

        for ( int i = 0; i < table.getRowCount(); i++ )
        {

            cod_produto = Integer.parseInt( String.valueOf( table.getModel().getValueAt( i, 0 ) ) );
            qtd = Double.parseDouble( String.valueOf( table.getModel().getValueAt( i, 4 ) ) );
            TbProduto produto_local = (TbProduto) produtosController.findById( cod_produto );
            boolean stocavel = produto_local.getStocavel().equals( "true" ) ? true : false;

            if ( !possivel_quantidade( cod_produto, qtd ) && stocavel )
            {

                transtorno = true;
                qtd_aceite = stocksController.getStockByIdProdutoAndIdArmazem( cod_produto, getCodigoArmazem() ).getQuantidadeExistente();

                if ( qtd_aceite != 0 )
                {

                    TbProduto produto = (TbProduto) produtosController.findById( getCodigoProduto() );
                    int opcao = JOptionPane.showConfirmDialog( null, "Desculpe pelo transtorno"
                            + ", o produto " + produto.getDesignacao() + " só é possivel  a " + qtd_aceite + " quantidade(s)" + ", contrariamente de " + qtd + "\n Deseja actualizar(Yes) ou remover da tabela(No) ?" );

                    if ( opcao == JOptionPane.YES_OPTION )
                    {

                        modelo.setValueAt( qtd_aceite, i, 4 );
                        double valor_iva = 0, taxa = 0, desconto = 0;
                        taxa = Double.parseDouble( modelo.getValueAt( i, 6 ).toString() );
                        desconto = Double.parseDouble( modelo.getValueAt( i, 5 ).toString() );
                        modelo.setValueAt( getValorComImposto( qtd_aceite, taxa, getPreco( cod_produto, qtd_aceite ), desconto ), i, 10 );

                    }
                    else
                    {
                        modelo.removeRow( i );
                    }
                    setTotalPagar();
                    setTotalRetencao();
                    valor_por_extenco();
                    adicionar_preco_quantidade_anitgo();

                }
                else
                {
                    modelo.removeRow( i );
                    adicionar_preco_quantidade_anitgo();
                    JOptionPane.showMessageDialog( null, "Desculpe pelo transtorno o produto " + produto_local.getDesignacao() + " já não se encontra disponível no stock", DVML.DVML_COMERCIAL, JOptionPane.WARNING_MESSAGE );
                }

            }

        }

        return transtorno;

    }

    public static void procedimento_salvar_venda_comercial()
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
        salvar_venda_comercial();

        // Atualiza a data após a venda
        dc_data_documento.setDate( new Date() );
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

    public static Double getPreco( int idProduto, double qtd )
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

    public static Double getPreco( int idProduto, double qtd, int idMoeda )
    {

        Moeda moeda = (Moeda) moedasController.findById( idMoeda );
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
        TbProduto produto = (TbProduto) produtosController.findById( getCodigoProduto() );
        return produto.getDesignacao();
    }

    public static String getUnidade_Produto()
    {
        TbProduto produto = (TbProduto) produtosController.findById( getCodigoProduto() );
        Unidade unidade = (Unidade) unidadesController.findById( produto.getCodUnidade().getPkUnidade() );
        return unidade.getAbreviacao();
    }

    public static boolean campos_invalidos()
    {
        return txtQuatindade.getText().equals( "" );
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

    private void tratar_troco()
    {
        try
        {
            double troco = 0.0;

            double total_pagar = CfMethods.parseMoedaFormatada( txtTotalPagar.getText() );

//            double valor_entregue = ( txtValorEntregue.getText().isEmpty() ) ? 0 : Double.parseDouble( txtValorEntregue.getText() );
//            troco = valor_entregue - total_pagar;
//            if ( !( txtValorEntregue.getText().isEmpty() ) || troco > 0 )
//            {
//
//                txtTroco.setText( CfMethods.formatarComoMoeda( troco ) );
//            }
//            else
//            {
//                txtTroco.setText( CfMethods.formatarComoMoeda( troco ) );
//            }
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }

    }

    private void tratar_desconto()
    {
        try
        {
            double desconto = 0.0;
            double total_pagar = getTotalPagar();
            double valor_desconto_geral = (double) sp_desconto_financeiro.getValue();

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

    private static String getCodDocActualizador()
    {
        try
        {
            documento = (Documento) documentosController.findById( getIdDocumento() );
            anoEconomico = (AnoEconomico) anoEconomicoController.findById( getIdAnoEconomico() );
            // this.doc_prox_cod = documento.getCodUltimoDoc() + 1;
            doc_prox_cod = vendasController.getUltimaContagemByIdDocumentoAndAnoEconomico(
                    getIdDocumento(), getIdAnoEconomico() ) + 1;
            prox_doc = documento.getAbreviacao();
            //FA Série / codigo
            prox_doc += " " + anoEconomico.getSerie() + "/" + doc_prox_cod;
            return prox_doc;
        }
        catch ( Exception e )
        {
            return "";
        }
    }

    private static boolean isProdutoExpirado( int codigoProduto )
    {
        // return produtoDao.produtoExpirado( codigoProduto );
        return false;
    }

    //----------- evento do teclado ---------------------------------------
    class TratarTroco implements KeyListener
    {

        String prefixo = "";

        public void keyPressed( KeyEvent evt )
        {

            String prefixo = "";
            double total_pagar = CfMethods.parseMoedaFormatada( txtTotalPagar.getText() );
            if ( evt.getKeyCode() != KeyEvent.VK_BACK_SPACE )
            {
                char key = evt.getKeyChar();
//                prefixo = txtValorEntregue.getText() + key;
                double troco = 0;
                double valor_entregue = Double.parseDouble( prefixo );

                troco = valor_entregue - total_pagar;
//                txtTroco.setText( String.valueOf( MetodosUtil.retirar_dizimas( troco ) ) );

            }
            else if ( evt.getKeyCode() == KeyEvent.VK_BACK_SPACE )
            {
                char key = evt.getKeyChar();

//                prefixo = txtValorEntregue.getText() + key;
                double valor_entregue = 0;
                double troco = 0;

                try
                {
                    valor_entregue = Double.parseDouble( prefixo.toString().trim().substring( 0, prefixo.length() - 2 ) );
                    troco = valor_entregue - total_pagar;
                }
                catch ( Exception e )
                {
                    valor_entregue = 0;
                }

                troco = valor_entregue - total_pagar;
//                txtTroco.setText( String.valueOf( MetodosUtil.retirar_dizimas( troco ) ) );

            }

        }

        public void keyReleased( KeyEvent evt )
        {
        }

        public void keyTyped( KeyEvent evt )
        {
        }
    }

    //----------- evento do teclado ---------------------------------------
    class TratarDesconto implements KeyListener
    {

        String prefixo = "";

        public void keyPressed( KeyEvent evt )
        {

            String prefixo = "";

            if ( evt.getKeyCode() != KeyEvent.VK_BACK_SPACE )
            {
                char key = evt.getKeyChar();
                double desconto = Double.parseDouble( prefixo );
                double total_pagar = Double.parseDouble( txtTotalPagar.getText().trim() );
                double totalComDesconto = ( total_pagar * desconto ) / 100;
                double resultado = total_pagar - totalComDesconto;

                txtTotalPagar.setText( String.valueOf( resultado ).trim() );

            }
            else if ( evt.getKeyCode() == KeyEvent.VK_BACK_SPACE )
            {
                char key = evt.getKeyChar();

                double desconto = 0;
                double total_pagar = Double.parseDouble( txtTotalPagar.getText().trim() );

                try
                {
                    desconto = Double.parseDouble( prefixo.toString().trim().substring( 0, prefixo.length() - 2 ) );
                    desconto = Double.parseDouble( prefixo );
                    total_pagar = Double.parseDouble( txtTotalPagar.getText().trim() );
                    double totalComDesconto = ( total_pagar * desconto ) / 100;
                    double resultado = total_pagar - totalComDesconto;
                    txtTotalPagar.setText( String.valueOf( resultado ).trim() );
                }
                catch ( Exception e )
                {
                    desconto = 0;
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

    //----------- evento do teclado ---------------------------------------
    class TratarEventoValorEntregue implements KeyListener
    {

        String prefixo = "";
        int codigo = 0, codigo_categoria = 0, quatidade_produto = 0;

        public void keyPressed( KeyEvent evt )
        {

            if ( evt.getKeyCode() == KeyEvent.VK_ENTER )
            {

                try
                {
//                    txtClienteNome.requestFocus ();
                }
                catch ( Exception ex )
                {
                    Logger.getLogger( VendaUsuarioVisao.class.getName() ).log( Level.SEVERE, null, ex );
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

    //----------- evento do teclado ---------------------------------------
    class TratarEventoCliente implements KeyListener
    {

        String prefixo = "";
        int codigo = 0, codigo_categoria = 0, quatidade_produto = 0;

        public void keyPressed( KeyEvent evt )
        {

            if ( evt.getKeyCode() == KeyEvent.VK_ENTER )
            {

                try
                {

                }
                catch ( Exception ex )
                {
                    Logger.getLogger( VendaUsuarioVisao.class.getName() ).log( Level.SEVERE, null, ex );
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

    //----------- evento do teclado ---------------------------------------
    class TratarEventtoTroco implements KeyListener
    {

        String prefixo = "";

        public void keyPressed( KeyEvent evt )
        {

            if ( evt.getKeyCode() != KeyEvent.VK_BACK_SPACE && evt.getKeyCode() != KeyEvent.VK_ENTER && evt.getKeyCode() != KeyEvent.VK_BACK_SPACE )
            {

            }
        }

        public void keyReleased( KeyEvent evt )
        {
        }

        public void keyTyped( KeyEvent evt )
        {
        }
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

    public String getDataActual()
    {
        Calendar calendario = Calendar.getInstance();

        //buscar data
        int dia = calendario.get( Calendar.DAY_OF_MONTH );
        int mes = calendario.get( Calendar.MONTH );
        int ano = calendario.get( Calendar.YEAR );

        int hora = calendario.get( Calendar.HOUR_OF_DAY );
        int minuto = calendario.get( Calendar.MINUTE );
        int segundo = calendario.get( Calendar.SECOND );
        String data = ano + "-" + ( mes + 1 ) + "-" + dia + " " + hora + ":" + minuto + ":" + segundo;

        return data;

    }

    public static double getTotal()
    {
        return getQuantidade() * getPreco();
    }
//Esta

    private static void habilitarColunas()
    {
//        TbDadosInstituicao dadosInstituicao_local = ( TbDadosInstituicao ) dadosInstituicaoController.findById( 1 );

//        boolean editarpreco = dadosInstituicao_local.getEditarPreco();
        boolean editarpreco = true;
        TbUsuario usuario = (TbUsuario) usuariosController.findById( cod_usuario );
        if ( usuario.getIdTipoUsuario().getIdTipoUsuario() == 1 && editarpreco )
        {
            setEditableColunaPreco( true );
        }
        else
        {
            setEditableColunaPreco( false );
        }
    }

//    public static void adicionar_produto() throws SQLException
//    {
//
//        DefaultTableModel modelo = (DefaultTableModel) table.getModel();
//
////        double total_deconto = Double.parseDouble( String.valueOf( getPreco() ) ) * Double.parseDouble( String.valueOf( getQuantidade() ) );
//
//        int codigo_produto = getCodigoProduto();
//
//        if ( !exist_produto_tabela_formulario( codigo_produto ) )
//        {
//            System.out.println( "NÃO EXISTE " );
//            if ( !validar_zero() )
//            {
//
//                String unidade = getUnidade_Produto();
//                String descricao_produto = getDescricao_Produto();
//                double desconto = getDescontoPercentagem();
//                double qtd = getQuantidade();
//                double preco = getPreco();
//                String precoUnitarioLocal = CfMethods.formatarComoMoedaPreco( getPreco() );
//                System.out.println( "$$$$ PRECO: " + preco );
//                double taxa = getTaxaImpostoIva( codigo_produto );
//                double ret = getTaxaImpostoRet( codigo_produto );
//                System.out.println( "RET: " + ret );
//                System.out.println( "TAX: " + taxa );
////              ( double qtd, double taxa, double preco_venda, double desconto )
//                String total_linha_retencao = CfMethods.formatarComoMoeda( getValorComRetencao(
//                        qtd,
//                        ret,
//                        preco,
//                        desconto
//                ) );
//
//                String total_iliquido_linha = CfMethods.formatarComoMoeda( getTotal() );
//                String total_liquido_linha = CfMethods.formatarComoMoeda( getValorComImpostoIva(
//                        qtd,
//                        taxa,
//                        preco,
//                        desconto
//                ) );
//
//                modelo.addRow( new Object[]
//                {
//                    codigo_produto,
//                    descricao_produto,
//                    unidade,
//                    precoUnitarioLocal,
//                    qtd,
//                    desconto,
//                    taxa,
//                    ret,
//                    total_linha_retencao,
//                    total_iliquido_linha,
//                    total_liquido_linha
//
//                } );
//
//            }
//            else
//            {
//                JOptionPane.showMessageDialog( null, "Atenção\nA quantidade a sair não pode ser igual a zero!" );
//            }
//
//            TbProduto findByDesignacao = produtosController.findByDesignacao( cmbProduto.getSelectedItem().toString() );
//
//            int idPedido = 0;
//            TbMesas mesaEntity = (TbMesas) mesasController.findById( DVML.MESA_BALCAO );
//            String mesa = mesaEntity.getDesignacao();
//            TbLugares lugarEntity = (TbLugares) lugaresController.findById( DVML.LUGAR_BALCAO );
//            String lugar = lugarEntity.getDesignacao();
//            TbUsuario usuarioEntity = (TbUsuario) usuariosController.findById( cod_usuario );
//            String usuario = usuarioEntity.getNome();
//
//            if ( findByDesignacao.getCozinha().equals( DVML.ENVIAR_TICKET ) )
//            {
//                MetodosUtil.imprimir_cozinha( findByDesignacao, idPedido, mesa, lugar, usuario, "Activo", (int) getQuantidade(), dadosInstituicaoController );
//
//            }
//            else if ( findByDesignacao.getCozinha().equals( DVML.ENVIAR_SALA ) )
//            {
//                MetodosUtil.imprimir_sala( findByDesignacao, idPedido, mesa, lugar, usuario, "Activo", (int) getQuantidade(), dadosInstituicaoController );
//            }
//
//        }
//        else
//        {
//            JOptionPane.showMessageDialog( null, "O produto já consta na tabela." );
//        }
//
//        System.out.println( "1:::::::::::" );
//        //Total Retenção
//        setTotalRetencao();
//        //Total A Pagar
//        setTotalPagar();
//
//        calculaTotalIVA();
//
//        valor_por_extenco();
//
//        txtQuatindade.setText( String.valueOf( 1 ) );
//        txtCodigoProduto.requestFocus();
//    }
    public static void adicionar_produto() throws SQLException
    {
        DefaultTableModel modelo = (DefaultTableModel) table.getModel();
        int codigo_produto = getCodigoProduto();

        if ( !exist_produto_tabela_formulario( codigo_produto ) )
        {
            if ( !validar_zero() )
            {
                String unidade = getUnidade_Produto();
                String descricao_produto = getDescricao_Produto();

                // Usar BigDecimal para valores monetários
                BigDecimal qtd = BigDecimal.valueOf( getQuantidade() );
                BigDecimal preco = BigDecimal.valueOf( getPreco() );
                BigDecimal descontoPercent = BigDecimal.valueOf( getDescontoPercentagem() );
                BigDecimal taxaIva = BigDecimal.valueOf( getTaxaImpostoIva( codigo_produto ) );
                BigDecimal taxaRet = BigDecimal.valueOf( getTaxaImpostoRet( codigo_produto ) );

                // Total Bruto
                BigDecimal totalBruto = preco.multiply( qtd ).setScale( 2, RoundingMode.HALF_UP );

                // Aplicar Desconto
                BigDecimal descontoValor = totalBruto.multiply( descontoPercent ).divide( BigDecimal.valueOf( 100 ) );
                BigDecimal totalComDesconto = totalBruto.subtract( descontoValor ).setScale( 2, RoundingMode.HALF_UP );

                // Valor com IVA
                BigDecimal ivaValor = totalComDesconto.multiply( taxaIva ).divide( BigDecimal.valueOf( 100 ) );
                BigDecimal totalComIva = totalComDesconto.add( ivaValor ).setScale( 2, RoundingMode.HALF_UP );

                // Valor com Retenção
                String total_linha_retencao = CfMethods.formatarComoMoeda(
                        getValorComRetencao(
                                qtd,
                                taxaRet,
                                preco,
                                descontoPercent
                        )
                );
//                BigDecimal retencaoValor = totalComDesconto.multiply( taxaRet ).divide( BigDecimal.valueOf( 100 ) );
//                BigDecimal totalComRet = totalComDesconto.subtract( retencaoValor ).setScale( 2, RoundingMode.HALF_UP );

                // Adicionar à tabela
                modelo.addRow( new Object[]
                {
                    codigo_produto,
                    descricao_produto,
                    unidade,
                    CfMethods.formatarComoMoeda( preco ),
                    qtd,
                    descontoPercent,
                    taxaIva,
                    taxaRet,
                    total_linha_retencao,
                    CfMethods.formatarComoMoeda( totalComDesconto ),
                    CfMethods.formatarComoMoeda( totalComIva )
                } );

                // Impressão de ticket (cozinha ou sala)
                TbProduto findByDesignacao = produtosController.findByDesignacao( cmbProduto.getSelectedItem().toString() );
                int idPedido = 0;
                TbMesas mesaEntity = (TbMesas) mesasController.findById( DVML.MESA_BALCAO );
                TbLugares lugarEntity = (TbLugares) lugaresController.findById( DVML.LUGAR_BALCAO );
                TbUsuario usuarioEntity = (TbUsuario) usuariosController.findById( cod_usuario );
                String usuario = usuarioEntity.getNome();

                if ( findByDesignacao.getCozinha().equals( DVML.ENVIAR_TICKET ) )
                {
                    MetodosUtil.imprimir_cozinha( findByDesignacao, idPedido, mesaEntity.getDesignacao(),
                            lugarEntity.getDesignacao(), usuario, "Activo", qtd.intValue(), dadosInstituicaoController );
                }
                else if ( findByDesignacao.getCozinha().equals( DVML.ENVIAR_SALA ) )
                {
                    MetodosUtil.imprimir_sala( findByDesignacao, idPedido, mesaEntity.getDesignacao(),
                            lugarEntity.getDesignacao(), usuarioEntity.getNome(), "Activo", qtd.intValue(), dadosInstituicaoController );
                }
            }
            else
            {
                JOptionPane.showMessageDialog( null, "Atenção\nA quantidade a sair não pode ser igual a zero!" );
            }
        }
        else
        {
            JOptionPane.showMessageDialog( null, "O produto já consta na tabela." );
        }

        // Atualização de totais
        setTotalRetencao();
        setTotalPagar();
        calculaTotalIVA();
        valor_por_extenco();

        txtQuatindade.setText( String.valueOf( 1 ) );
        txtCodigoProduto.requestFocus();
    }

//    public static void setTotalPagar()
//    {
//
//        DefaultTableModel modelo = (DefaultTableModel) table.getModel();
//        double total_liquido = getTotalAOALiquido();
//
//        txtTotal_AOA_liquido.setText( CfMethods.formatarComoMoeda( total_liquido ) );
//    }
//
//    private static double getTotalAOALiquido()
//    {
//        double valores = (getTotalIliquido() + getTotalImposto());
//        double descontos = (getDescontoComercial() + getDescontoFinanceiro());
//        return ( valores - descontos );
//    }
//
//    private static double getTotalIliquido()
//    {
//
//        DefaultTableModel modelo = (DefaultTableModel) table.getModel();
//        double qtd = 0d;
//        double total_iliquido_local = 0d, preco_unitario = 0d;
//
//        for ( int i = 0; i < modelo.getRowCount(); i++ )
//        {
//
//            preco_unitario = CfMethods.parseMoedaFormatada( modelo.getValueAt( i, 3 ).toString() );
//            qtd = Double.parseDouble( modelo.getValueAt( i, 4 ).toString() );
//            total_iliquido_local += ( preco_unitario * qtd );
//
//        }
//
//        return total_iliquido_local;
//
//    }
    private static BigDecimal getValorComRetencao( BigDecimal qtd, BigDecimal ret, BigDecimal precoVenda, BigDecimal desconto )
    {
        // subtotal_linha = preco_venda * qtd
        BigDecimal subtotalLinha = precoVenda.multiply( qtd );

        // desconto_valor = subtotal_linha * (desconto / 100)
        BigDecimal descontoValor = subtotalLinha.multiply( desconto.divide( BigDecimal.valueOf( 100 ), 10, RoundingMode.HALF_UP ) );

        // valor_ret = ((subtotal_linha - desconto_valor) * ret) / 100
        BigDecimal valorRet = ( subtotalLinha.subtract( descontoValor ) )
                .multiply( ret )
                .divide( BigDecimal.valueOf( 100 ), 10, RoundingMode.HALF_UP );

        // Arredonda para 2 casas decimais
        return valorRet.setScale( 2, RoundingMode.HALF_UP );
    }

    private static BigDecimal getTotalIliquido()
    {
        DefaultTableModel modelo = (DefaultTableModel) table.getModel();
        BigDecimal totalIliquido = BigDecimal.ZERO;

        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {
            // Coluna 3: Preço Unitário, Coluna 4: Quantidade
            BigDecimal precoUnitario = BigDecimal.valueOf(
                    CfMethods.parseMoedaFormatada( modelo.getValueAt( i, 3 ).toString() )
            );
            BigDecimal quantidade = new BigDecimal( modelo.getValueAt( i, 4 ).toString() );

            BigDecimal subtotal = precoUnitario.multiply( quantidade );
            totalIliquido = totalIliquido.add( subtotal );
        }

        return totalIliquido.setScale( 2, RoundingMode.HALF_UP );
    }

    private static BigDecimal getTotalAOALiquido()
    {
        BigDecimal totalIliquido = getTotalIliquido();
        BigDecimal totalImposto = BigDecimal.valueOf( getTotalImposto() );
        BigDecimal descontoComercial = BigDecimal.valueOf( getDescontoComercial() );
        BigDecimal descontoFinanceiro = BigDecimal.valueOf( getDescontoFinanceiro() );

        return totalIliquido
                .add( totalImposto )
                .subtract( descontoComercial.add( descontoFinanceiro ) )
                .setScale( 2, RoundingMode.HALF_UP );
    }

    public static void setTotalPagar()
    {
        BigDecimal total = getTotalAOALiquido();
        String valorFormatado = CfMethods.formatarComoMoeda( total );
        txtTotalPagar.setText( valorFormatado );
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

    public void remover_item_carrinho()
    {

        DefaultTableModel modelo = (DefaultTableModel) table.getModel();
        modelo.removeRow( table.getSelectedRow() );
        setTotalPagar();
        setTotalRetencao();
        calculaTotalIVA();
        //txtDesconto.setText("0");

        TbProduto findByDesignacao = produtosController.findByDesignacao( cmbProduto.getSelectedItem().toString() );

        int idPedido = 0;
        TbMesas mesaEntity = (TbMesas) mesasController.findById( DVML.MESA_BALCAO );
        String mesa = mesaEntity.getDesignacao();
        TbMesas lugarEntity = (TbMesas) lugaresController.findById( DVML.LUGAR_BALCAO );
        String lugar = lugarEntity.getDesignacao();
        TbUsuario usuarioEntity = (TbUsuario) usuariosController.findById( cod_usuario );
        String usuario = usuarioEntity.getNome();

        if ( findByDesignacao.getCozinha().equals( "Enviar Ticket" ) )
        {
            MetodosUtil.imprimir_cozinha( findByDesignacao, idPedido, mesa, lugar, usuario, "Cancelado", (int) getQuantidade(), dadosInstituicaoController );
        }
        else if ( findByDesignacao.getCozinha().equals( "Enviar Sala" ) )
        {
            MetodosUtil.imprimir_sala( findByDesignacao, idPedido, mesa, lugar, usuario, "Cancelado", (int) getQuantidade(), dadosInstituicaoController );
        }

        valor_por_extenco();
        reset_desconto_global();
        //calcularTroco();

    }

    public void adicionar_produto_teclado( int codigo, String descricao ) throws SQLException
    {

        DefaultTableModel modelo = (DefaultTableModel) table.getModel();
        double total_deconto = Double.parseDouble( String.valueOf( getPreco() ) ) * Double.parseDouble( String.valueOf( getQuantidade() ) );

        modelo.addRow( new Object[]
        {
            codigo,
            descricao,
            getPreco(),
            getQuantidade(),
            getDesconto_produto( total_deconto ),
            getTotal() - getDesconto_produto( total_deconto )
        } );

        setTotalPagar();
        setTotalRetencao();

//        double valor_entregue = Double.parseDouble( txtValorEntregue.getText() );
//        if ( valor_entregue > 0 )
//        {
//            calcularTroco();
//        }
    }
//    

//    public static boolean registrar_forma_pagamento( int id_venda )
//    {
//
//        DefaultTableModel modelo = ( DefaultTableModel ) FormaPagamentoVisao.tabela_forma_pagamento.getModel();
//
//        FormaPagamentoItem formaPagamentoItem;
//
//        for ( int i = 0; i < modelo.getRowCount(); i++ )
//        {
//
//            formaPagamentoItem = new FormaPagamentoItem();
//            Integer id_forma_pagamento = Integer.parseInt( modelo.getValueAt( i, 0 ).toString() );
//            String referencia = ( modelo.getValueAt( i, 2 ) != null ) ? modelo.getValueAt( i, 2 ).toString() : "n/a";
////            String valor = ( modelo.getValueAt( i, 3 ) != null ) ? modelo.getValueAt( i, 3 ).toString() : "0";
//            String valor = ( !modelo.getValueAt( i, 3 ).toString().equals( "" ) ) ? modelo.getValueAt( i, 3 ).toString() : "0";
//
//            formaPagamentoItem.setValor( new BigDecimal( valor ) );
//            formaPagamentoItem.setReferencia( referencia );
//            formaPagamentoItem.setFkVenda( new TbVenda( id_venda ) );
//            formaPagamentoItem.setFkFormaPagamento( new FormaPagamento( id_forma_pagamento ) );
//
//            try
//            {
//                if ( !valor.equals( "0" ) )
//                {
//                    formaPagamentoItemController.salvar( formaPagamentoItem );
//                }
//            }
//            catch ( Exception e )
//            {
//                return false;
//            }
//        }
//
//        return true;
//    }
    public void remover_items()
    {

        table.getColumnModel().getColumn( 0 );
        table.getColumnModel().getColumn( 1 );
        table.getColumnModel().getColumn( 2 );
        table.getColumnModel().getColumn( 3 );
        table.getColumnModel().getColumn( 4 );
        table.getColumnModel().getColumn( 5 );

        DefaultTableModel modelo = (DefaultTableModel) table.getModel();

        for ( int i = modelo.getRowCount() - 1; i >= 0; i-- )
        {
            modelo.removeRow( i );
        }

    }

    public static void setTotalRetencao()
    {
        BigDecimal totalRetencao = getTotalRetencaoLiquido();
        txtTotal_AOA_Retencao.setText( CfMethods.formatarComoMoeda( totalRetencao ) );
    }

    private static BigDecimal getTotalRetencaoLiquido()
    {
        DefaultTableModel modelo = (DefaultTableModel) table.getModel();
        BigDecimal totalRetencao = BigDecimal.ZERO;

        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {
            BigDecimal valorRetencao = BigDecimal.valueOf(
                    CfMethods.parseMoedaFormatada( modelo.getValueAt( i, 8 ).toString() )
            );

            totalRetencao = totalRetencao.add( valorRetencao );
        }

        return totalRetencao.setScale( 2, RoundingMode.HALF_UP );
    }

    public static double getTotalPagar()
    {

        DefaultTableModel modelo = (DefaultTableModel) table.getModel();

        double total_pagar = 0;
        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {
            total_pagar += CfMethods.parseMoedaFormatada( String.valueOf( modelo.getValueAt( i, 10 ) ) );

        }
        return total_pagar;

    }

    public static double getTotal_Retencao()
    {

        DefaultTableModel modelo = (DefaultTableModel) table.getModel();

        double total_retencao = 0;
        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {
            total_retencao += CfMethods.parseMoedaFormatada( String.valueOf( modelo.getValueAt( i, 8 ) ) );

        }
        return total_retencao;

    }

    private static void calculaTotalIVA()
    {
        DefaultTableModel modelo = (DefaultTableModel) table.getModel();
        BigDecimal totalIva = BigDecimal.ZERO;

        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {
            // Preço unitário (coluna 3)
            BigDecimal preco = BigDecimal.valueOf(
                    CfMethods.parseMoedaFormatada( modelo.getValueAt( i, 3 ).toString() )
            );

            // Quantidade (coluna 4)
            BigDecimal quantidade = new BigDecimal( modelo.getValueAt( i, 4 ).toString() );

            // Taxa de IVA (coluna 6)
            BigDecimal taxaIva = new BigDecimal( modelo.getValueAt( i, 6 ).toString() );

            // Desconto (se aplicável - aqui ainda é zero porque está comentado no original)
            BigDecimal desconto = BigDecimal.ZERO;

            // Valor do IVA calculado
            BigDecimal valorIva = getIVA( quantidade, taxaIva, preco, desconto );

            totalIva = totalIva.add( valorIva );
        }

        // Arredondar o total de IVA
        total_iva = MetodosUtil.valorCasasDecimaisNovo( totalIva.doubleValue(), CASAS_DECIMAIS );
        System.out.println( "(*)TOTAL IVA: " + total_iva );
    }

    private static void calculaTotalRet()
    {

        DefaultTableModel modelo = (DefaultTableModel) table.getModel();
        double ret = 0, preco = 0, desconto = 0;
        double qtd = 0d;
        total_ret = 0;
        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {

            preco = CfMethods.parseMoedaFormatada( modelo.getValueAt( i, 3 ).toString() );
            qtd = Double.parseDouble( modelo.getValueAt( i, 4 ).toString() );
            //desconto = CfMethods.parseMoedaFormatada(modelo.getValueAt(i, 4).toString());
            ret = Double.parseDouble( modelo.getValueAt( i, 7 ).toString() );
            total_ret += getRET( qtd, ret, preco, desconto );
        }

        // System.err.println("Com o método: " + MetodosUtil.valorCasasDecimaisNovo(total_iva, CASAS_DECIMAIS));
        total_ret = MetodosUtil.valorCasasDecimaisNovo( total_ret, CASAS_DECIMAIS );
        System.out.println( "(*)TOTAL RET: " + total_ret );

    }
//    private static void calculaTotalIVA()
//    {
//
//        DefaultTableModel modelo = ( DefaultTableModel ) table.getModel();
//        double iva = 0, preco = 0, desconto = 0;
//        int qtd = 0;
//        total_iva = 0;
//        for ( int i = 0; i < modelo.getRowCount(); i++ )
//        {
//
//            preco = CfMethods.parseMoedaFormatada( modelo.getValueAt( i, 2 ).toString() );
//            qtd = Integer.parseInt( modelo.getValueAt( i, 3 ).toString() );
//            //desconto = CfMethods.parseMoedaFormatada(modelo.getValueAt(i, 4).toString());
//            iva = Double.parseDouble( modelo.getValueAt( i, 5 ).toString() );
//            total_iva += getIVA( qtd, iva, preco, desconto );
//        }
//
//        // System.err.println("Com o método: " + MetodosUtil.valorCasasDecimaisNovo(total_iva, CASAS_DECIMAIS));
//        total_iva = MetodosUtil.valorCasasDecimaisNovo( total_iva, CASAS_DECIMAIS );
//        System.out.println( "(*)TOTAL IVA: " + total_iva );
//
//    }

    public void setTotalCompleto()
    {

        table.getColumnModel().getColumn( 0 );
        table.getColumnModel().getColumn( 1 );
        table.getColumnModel().getColumn( 2 );
        table.getColumnModel().getColumn( 3 );
        table.getColumnModel().getColumn( 4 );
        table.getColumnModel().getColumn( 5 );

        DefaultTableModel modelo = (DefaultTableModel) table.getModel();

        double total_pagar = 0;

        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {
            total_pagar += Double.parseDouble( String.valueOf( modelo.getValueAt( i, 5 ) ) );

        }

        try
        {

            calcularTroco();

        }
        catch ( Exception e )
        {
            //e.printStackTrace();
        }

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

//    public double getDesconto_produto( double valor ) throws SQLException
//    {
//
//        Desconto desconto = descontoDao.get_desconto_cliente_produto( getCodigoCliente(), getCodigoProduto() );
//        Integer quantidade = desconto.getQuantidade();
//        double percentagem_desconto = desconto.getValor();
//
//        if ( getQuantidade() >= quantidade )
//        {
//            return valor * ( percentagem_desconto / 100 );
//        }
//        else
//        {
//            return 0.0;
//        }
//
//    }
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

        DefaultTableModel modelo = (DefaultTableModel) table.getModel();
        for ( int i = modelo.getRowCount() - 1; i >= 0; i-- )
        {
            modelo.removeRow( i );
        }

    }

    public void remover_produto() throws SQLException
    {

        if ( linha > 0 )
        {

            table.getModel().setValueAt( 0, linha - 1, 0 );
            table.getModel().setValueAt( "", linha - 1, 1 );
            table.getModel().setValueAt( 0, linha - 1, 2 );
            table.getModel().setValueAt( 0, linha - 1, 3 );
            table.getModel().setValueAt( 0, linha - 1, 4 );
            table.getModel().setValueAt( 0, linha - 1, 5 );

            setTotalPagar();
            setTotalRetencao();
            linha--;
            calcularTroco();
        }
        else
        {
            JOptionPane.showMessageDialog( null, "Impossivel Remover Produtos na Tabela!...." );
        }

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

    public static int getCodigoArmazem()
    {
        return armazensController.getArmazemByDesignacao( cmbArmazem.getSelectedItem().toString() ).getCodigo();
    }

    private static void salvar_venda_comercial()
    {

//        JOptionPane.showMessageDialog( null, "Estou dentro" );
        BDConexao conexaoTransactionLocal = new BDConexao();
        vendasController = new VendasController( conexaoTransactionLocal );
        itemVendasController = new ItemVendasController( conexaoTransactionLocal );
        formaPagamentoItemController = new FormaPagamentoItemController( conexaoTransactionLocal );
        StoksController stocksControllerLocal = new StoksController( conexaoTransactionLocal );

        DocumentosController.startTransaction( conexaoTransactionLocal ); // Inicia a transação

        try
        {
            System.out.println( "AutoCommit após iniciar transação? " + conexaoTransactionLocal.getConnection1().getAutoCommit() );
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
           
            vendasController.actualizar_hash_and_assinatura( idVendaGerada, getGrossTotal().doubleValue());
            
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
                registrar_forma_pagamento( idVendaGerada );

            }

            // Finaliza transação
            DocumentosController.commitTransaction( conexaoTransactionLocal );

            JOptionPane.showMessageDialog( null, "Factura efectuada com sucesso!" );
//            imprimir_factura( idVendaGerada ); // Imprime a factura

        }
        catch ( Exception e )
        {

            DocumentosController.rollBackTransaction( conexaoTransactionLocal );
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

//    private static TbVenda construirVenda()
//    {
//        TbVenda venda = new TbVenda();
//        venda.setDataVenda( dc_data_documento.getDate() );
//        venda.setDataVencimento( dc_data_vencimento.getDate() );
//        venda.setRefDataFact( dc_data_documento.getDate() );
//        venda.setHora( dc_data_documento.getDate() );
//        venda.setNomeCliente( txtNomeConsumidorFinal.getText() );
//        venda.setClienteNif( txtNifClientePesquisa.getText() );
//        venda.setTotalGeral( getTotalIliquido() );
//        venda.setDescontoComercial( new BigDecimal( getDescontoComercial() ) );
//        venda.setTotalIva( new BigDecimal( getTotalImposto() ) );
//        venda.setTotalRetencao( getTotalRetencaoLiquido() );
//        venda.setDescontoFinanceiro( new BigDecimal( getDescontoFinanceiro() ) );
//// getTotalAOALiquido() já retorna BigDecimal
//        venda.setTotalVenda( getTotalAOALiquido() );
//
//// getValor_entregue() também já retorna BigDecimal (você ajustou anteriormente)
//        venda.setValorEntregue( getValor_entregue() );
//
//        venda.setTroco( new BigDecimal( getTroco() ) );
//        venda.setTotalIncidencia( new BigDecimal( getTotalIncidencia() ) );
//        venda.setTotalIncidenciaIsento( new BigDecimal( getTotalIncidenciaIsento() ) );
//        venda.setDescontoTotal( new BigDecimal( getDescontoComercial() + getDescontoFinanceiro() ) );
//
//        venda.setIdBanco( new TbBanco( 1 ) );
//        venda.setIdArmazemFK( new TbArmazem( getCodigoArmazem() ) );
//        venda.setCodigoUsuario( new TbUsuario( cod_usuario ) );
//        venda.setCodigoCliente( new TbCliente( getIdCliente() ) );
//        venda.setFkAnoEconomico( anoEconomico );
//        venda.setReferencia( txtReferencia.getText() );
//        venda.setNomeConsumidorFinal( txtNomeConsumidorFinal.getText() );
//        venda.setFkDocumento( documento );
//        venda.setCodFact( prox_doc );
//        venda.setCont( 0 );
//
//        venda.setHashCod( MetodosUtil.criptografia_hash( venda, getGrossTotal().doubleValue(), conexao ) );
//        venda.setAssinatura( MetodosUtil.assinatura_doc( venda.getHashCod() ) );
//        venda.setTotalPorExtenso( iniciais_extenso() + lbValorPorExtenco.getText() );
//
//        venda.setModelo( txtMotorista.getText() );
//        venda.setNumMotor( txtMatricula.getText() );
//        venda.setNomeMotorista( txtMotorista.getText() );
//        venda.setMatricula( txtMatricula.getText() );
//        venda.setMarcaCarro( txtMarca.getText() );
//        venda.setKilometro( txtKilometragem.getText() );
//        venda.setNumChassi( txtNumeroCarta.getText() );
//        venda.setObs( txtObs.getText() );
//        venda.setCorCarro( "" );
//        venda.setNDocMotorista( txtNumeroCarta.getText() );
//
//        if ( getMoeda().getAbreviacao().equals( DVML.MOEDA_KWANZA ) )
//        {
//            int id = cambiosController.getLastId( getIdMoeda() );
//            venda.setFkCambio( new Cambio( id ) );
//        }
//        else
//        {
//            venda.setFkCambio( cambiosController.getLastObject( getIdMoeda() ) );
//        }
//
//        venda.setStatusEliminado( "false" );
//        venda.setPerformance( "false" );
//        venda.setCredito( "false" );
//        venda.setGorjeta( new BigDecimal( gorjeta ) );
//
//        return venda;
//    }
    
    private static Date getDataVencimentoFr(){
        
        if((getIdDocumento() == DVML.DOC_FACTURA_PROFORMA_PP ) ||( getIdDocumento() == DVML.DOC_FACTURA_FT)){
        return dc_data_vencimento.getDate();}
        else {
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
        venda.setDescontoTotal( new BigDecimal( getDescontoComercial() + getDescontoFinanceiro() ) );
        venda.setDescontoComercial( new BigDecimal( getDescontoComercial() ) );
        venda.setDescontoFinanceiro( new BigDecimal( getDescontoFinanceiro() ) );
        venda.setTotalIva( new BigDecimal( getTotalImposto() ) );
        venda.setTotalGeral( getTotalIliquido() );
        venda.setTotalIncidencia( new BigDecimal( getTotalIncidencia() ) );
        venda.setTotalIncidenciaIsento( new BigDecimal( getTotalIncidenciaIsento() ) );
        venda.setTotalRetencao( getTotalRetencaoLiquido() );
        venda.setGorjeta( new BigDecimal( gorjeta ) );
        venda.setTotalPorExtenso( iniciais_extenso() + lbValorPorExtenco.getText() );

        // Strings
        venda.setNomeCliente( txtNomeConsumidorFinal.getText().trim() );
        venda.setClienteNif( txtNifClientePesquisa.getText().trim() );
        venda.setCodFact( prox_doc );
//    venda.setRefCodFact(txtRefCodFact.getText());
        venda.setPerformance( "false" ); // ou pegar de um campo
        venda.setCredito( "false" );        // depende se venda é a crédito
        venda.setHashCod( "" );           // actualiza o hash depois de salvar
//        venda.setHashCod( MetodosUtil.criptografia_hash( venda, getGrossTotal().doubleValue(), conexaoTransaction ) );
//        venda.setAssinatura( MetodosUtil.assinatura_doc( venda.getHashCod() ) );
        venda.setAssinatura( "" );        // preencher se estiver em uso
        venda.setObs( txtObs.getText() );
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
        venda.setMatricula( txtMatricula.getText() );
        venda.setModelo( txtMotorista.getText() );
        venda.setNumChassi( txtNumeroCarta.getText() );
        venda.setNumMotor( txtMatricula.getText() );
        venda.setKilometro( txtKilometragem.getText() );
        venda.setNomeMotorista( txtMotorista.getText() );
        venda.setMarcaCarro( txtMarca.getText() );
        venda.setCorCarro( "" );
        venda.setNDocMotorista( txtNumeroCarta.getText() );

        // Contador do documento
        venda.setCont( 0 ); // por exemplo

        return venda;
    }

    public static void salvar_item_venda_comercial( Integer cod_venda, BDConexao conexaoLocal, StoksController stoksControllerLocal ) throws Exception
    {
        for ( int i = 0; i < table.getRowCount(); i++ )
        {
            try
            {
                int idProduto = Integer.parseInt( table.getModel().getValueAt( i, 0 ).toString() );
                TbProduto produto = (TbProduto) produtosController.findById( idProduto );

                TbItemVenda item = new TbItemVenda();
                item.setCodigoVenda( new TbVenda( cod_venda ) );
                item.setCodigoProduto( produto );
                item.setQuantidade( Double.parseDouble( table.getModel().getValueAt( i, 4 ).toString() ) );
                item.setDesconto( Double.parseDouble( table.getModel().getValueAt( i, 5 ).toString() ) );
                item.setValorIva( Double.parseDouble( table.getModel().getValueAt( i, 6 ).toString() ) );
                item.setValorRetencao( Double.parseDouble( table.getModel().getValueAt( i, 7 ).toString() ) );
                item.setMotivoIsensao( getMotivoIsensao( idProduto ) );
                item.setCodigoIsensao( MetodosUtil.getCodigoRegime( idProduto ) );
                item.setTotal( new BigDecimal( CfMethods.parseMoedaFormatada( table.getModel().getValueAt( i, 10 ).toString() ) ) );
                item.setFkPreco( precosController.getLastIdPrecoByIdProduto( idProduto, item.getQuantidade() ) );
                item.setDataServico( new Date() );
                item.setFkLugares( (TbLugares) lugaresController.findById( DVML.LUGAR_BALCAO ) );
                item.setFkMesas( (TbMesas) mesasController.findById( DVML.MESA_BALCAO ) );

                // Salvar item
                if ( !itemVendasController.salvar( item ) )
                {
                    throw new Exception( "Erro ao salvar item da venda. Produto: " + produto.getDesignacao() );
                }

                // Controle de stock (se for estocável)
                boolean isStocavel = "true".equalsIgnoreCase( produto.getStocavel() );
                if ( isStocavel )
                {
                    TbStock stock_local_local = stoksControllerLocal.getStockByIdProdutoAndIdArmazem( idProduto, getCodigoArmazem() );

                    if ( ( getIdDocumento() == DOC_FACTURA_RECIBO_FR || getIdDocumento() == DOC_FACTURA_FT || getIdDocumento() == DVML.DOC_GUIA_TRANSPORTE_GT ) && stock_local_local != null )
                    {
                        actualizar_quantidade( idProduto, item.getQuantidade(), conexaoLocal );
                    }
                }

            }
            catch ( Exception e )
            {
                throw new Exception( "Erro ao processar item " + ( i + 1 ) + ": " + e.getMessage() );
            }
        }
    }

//    public static void registrar_forma_pagamento( int id_venda ) throws Exception
//    {
//        DefaultTableModel modelo = (DefaultTableModel) FormaPagamentoVisao.tabela_forma_pagamento.getModel();
//
//        double troco = CfMethods.parseMoedaFormatada( FormaPagamentoVisao.lb_troco.getText() );
//
//        for ( int i = 0; i < modelo.getRowCount(); i++ )
//        {
//            FormaPagamentoItem formaPagamentoItem = new FormaPagamentoItem();
//
//            Integer id_forma_pagamento = Integer.parseInt( modelo.getValueAt( i, 0 ).toString() );
//            String descricaoForma = modelo.getValueAt( i, 1 ).toString();
//
//            FormaPagamento formaPagamento = formaPagamentoController.findByDescrisao( descricaoForma );
//            Contas contas = (Contas) contaController.findById( formaPagamento.getFkContaAssociada() );
//
//            String referencia = ( modelo.getValueAt( i, 2 ) != null ) ? modelo.getValueAt( i, 2 ).toString() : "n/a";
//            String valorStr = modelo.getValueAt( i, 3 ).toString().trim();
//            if ( valorStr.isEmpty() )
//            {
//                valorStr = "0";
//            }
//
//            BigDecimal valor = new BigDecimal( valorStr );
//
//            if ( valor.compareTo( BigDecimal.ZERO ) > 0 )
//            {
//                formaPagamentoItem.setValor( valor );
//                formaPagamentoItem.setReferencia( referencia );
//                formaPagamentoItem.setTroco( new BigDecimal( troco ) );
//                formaPagamentoItem.setValor_real( valor.subtract( formaPagamentoItem.getTroco() ) );
//                formaPagamentoItem.setFkVenda( new TbVenda( id_venda ) );
//                formaPagamentoItem.setFkFormaPagamento( new FormaPagamento( id_forma_pagamento ) );
//
//                // Tenta salvar
//                if ( !formaPagamentoItemController.salvar( formaPagamentoItem ) )
//                {
//                    throw new Exception( "Erro ao salvar forma de pagamento: " + descricaoForma );
//                }
//
//                // Registra entrada na tesouraria
//                if ( contas != null )
//                {
//                    MetodosUtilTS.entradaTesouraria(
//                            contas,
//                            lb_proximo_documento.getText(),
//                            formaPagamento,
//                            referencia,
//                            valor,
//                            cod_usuario,
//                            usuariosController,
//                            cmc,
//                            conexao
//                    );
//                }
//
//                troco = 0; // Troco só desconta na primeira forma
//            }
//        }
//    }
    public static void registrar_forma_pagamento( int id_venda ) throws Exception
    {
        DefaultTableModel modelo = (DefaultTableModel) FormaPagamentoVisao.tabela_forma_pagamento.getModel();
        double troco = CfMethods.parseMoedaFormatada( FormaPagamentoVisao.lb_troco.getText() );

        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {
            String valorStr = modelo.getValueAt( i, 3 ).toString().trim();
            BigDecimal valor = CfMethods.parseMoedaSegura( valorStr ); // <<< método seguro
            System.out.println( "VALOR LIMPO: " + valor );

            if ( valor.compareTo( BigDecimal.ZERO ) <= 0 )
            {
                continue;
            }

            FormaPagamentoItem item = new FormaPagamentoItem();
            int idForma = Integer.parseInt( modelo.getValueAt( i, 0 ).toString() );
            String descricao = modelo.getValueAt( i, 1 ).toString();
            String referencia = ( modelo.getValueAt( i, 2 ) != null ) ? modelo.getValueAt( i, 2 ).toString() : "n/a";

            FormaPagamento forma = formaPagamentoController.findByDescrisao( descricao );
            Contas conta = (Contas) contaController.findById( forma.getFkContaAssociada() );

            item.setValor( valor );
            item.setTroco( BigDecimal.valueOf( troco ) );
            item.setValor_real( valor.subtract( item.getTroco() ) );
            item.setReferencia( referencia );
            item.setFkVenda( new TbVenda( id_venda ) );
            item.setFkFormaPagamento( new FormaPagamento( idForma ) );

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

            troco = 0;
        }
    }

    private static void imprimir_factura( int cod_venda )
    {

        BDConexao conexoaLocal = new BDConexao();
        limpar();
        remover_all_produto();
        accao_cliente();
        limpar_consumidor_final();

        txtQuatindade.setText( "1" );
        txtIniciaisCliente.requestFocus();
        txtQuantidadeStock.setText( String.valueOf( conexoaLocal.getQtdExistenteStock( getCodigoProduto(), getCodigoArmazem() ) ) );

        List<TbProduto> lista_produto_isentos = getProdutosIsentos();
        String motivos_isentos = MetodosUtil.getMotivoIsensaoProdutos( lista_produto_isentos );
        conexoaLocal.close();
        int numeroVias = (int) Double.parseDouble( spnCopia.getValue().toString() );

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
            ListaVenda1 listaVenda1 = new ListaVenda1( cod_venda, abreviacao, false, ck_simplificada.isSelected(), via, motivos_isentos );
        }
    }

//    public static void salvar_item_venda_comercial( Integer cod_venda, BDConexao conexaoLocal, StoksController stoksControllerLocal ) throws Exception
//    {
//        TbItemVenda itemVenda;
//        for ( int i = 0; i < table.getRowCount(); i++ )
//        {
//            TbProduto produto_local = (TbProduto) produtosController.findById(
//                    Integer.parseInt( String.valueOf( table.getModel().getValueAt( i, 0 ) ) )
//            );
//
//            itemVenda = new TbItemVenda();
//            itemVenda.setCodigoProduto( produto_local );
//            itemVenda.setCodigoVenda( new TbVenda( cod_venda ) );
//            itemVenda.setQuantidade( Double.parseDouble( String.valueOf( table.getModel().getValueAt( i, 4 ) ) ) );
//            itemVenda.setDesconto( Double.parseDouble( String.valueOf( table.getModel().getValueAt( i, 5 ) ) ) );
//            itemVenda.setValorIva( Double.parseDouble( String.valueOf( table.getModel().getValueAt( i, 6 ) ) ) );
//            itemVenda.setValorRetencao( Double.parseDouble( String.valueOf( table.getModel().getValueAt( i, 7 ) ) ) );
//            itemVenda.setMotivoIsensao( getMotivoIsensao( itemVenda.getCodigoProduto().getCodigo() ) );
//            itemVenda.setCodigoIsensao( MetodosUtil.getCodigoRegime( itemVenda.getCodigoProduto().getCodigo() ) );
//            itemVenda.setTotal( new BigDecimal( CfMethods.parseMoedaFormatada( String.valueOf( table.getModel().getValueAt( i, 10 ) ) ) ) );
//            itemVenda.setFkPreco( precosController.getLastIdPrecoByIdProduto( itemVenda.getCodigoProduto().getCodigo(), itemVenda.getQuantidade() ) );
//            itemVenda.setDataServico( new Date() );
//
//            // Campos para restauração
//            itemVenda.setFkLugares( (TbLugares) lugaresController.findById( DVML.LUGAR_BALCAO ) );
//            itemVenda.setFkMesas( (TbMesas) mesasController.findById( DVML.MESA_BALCAO ) );
//
//            // Salva item da venda
//            if ( !itemVendasController.salvar( itemVenda ) )
//            {
//                throw new Exception( "Erro ao salvar item da venda. Produto: " + itemVenda.getCodigoProduto().getDesignacao() );
//            }
//
//            // Controle de stock
//            boolean isStocavel = "true".equals( produto_local.getStocavel() );
//            if ( isStocavel )
//            {
//                stock_local = stoksControllerLocal.getStockByIdProdutoAndIdArmazem( itemVenda.getCodigoProduto().getCodigo(), getCodigoArmazem() );
//            }
//
//            if ( ( getIdDocumento() == DOC_FACTURA_RECIBO_FR || getIdDocumento() == DOC_FACTURA_FT || getIdDocumento() == DVML.DOC_GUIA_TRANSPORTE_GT )
//                    && isStocavel && stock_local != null )
//            {
//                actualizar_quantidade( itemVenda.getCodigoProduto().getCodigo(), itemVenda.getQuantidade(), conexaoLocal );
//            }
//        }
//    }
    private static String getNomeCliente()
    {
        return cmbCliente.getSelectedItem().toString();
    }

    private static String getClienteNif()
    {
        try
        {
            TbCliente cliente = (TbCliente) clientesController.findById( getIdCliente() );

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

//    private static String getClienteNif()
//    {
//        try
//        {
//            TbCliente cliente = ( TbCliente ) clientesController.findById( getIdCliente() );
//            return cliente.getNif();
//        }
//        catch ( Exception e )
//        {
//            return "";
//        }
//    }
    public static void main( String[] args ) throws SQLException
    {
        new VendaUsuarioVisao( 15, new BDConexao() ).show( true );
    }

    public void confiLabel()
    {

        lbCodigoProduto.setHorizontalAlignment( JLabel.RIGHT );
        lbCategoria.setHorizontalAlignment( JLabel.RIGHT );
        lbProduto.setHorizontalAlignment( JLabel.RIGHT );
        lbVias.setHorizontalAlignment( JLabel.RIGHT );
        lbQuantidade.setHorizontalAlignment( JLabel.RIGHT );
        lbDescontoFinanceiro.setHorizontalAlignment( JLabel.RIGHT );
//        lbValorEnregue.setHorizontalAlignment( JLabel.RIGHT );
//        lbTroco.setHorizontalAlignment( JLabel.RIGHT );
        //lbCliente.setHorizontalAlignment(JLabel.RIGHT);
        lbQuantidadeStock.setHorizontalAlignment( JLabel.RIGHT );

    }

    public int getCodigoTipoProduto() throws SQLException
    {
        return conexao.getCodigoPublico( "tb_tipo_produto", String.valueOf( cmbSubFamilia.getSelectedItem() ) );
    }

    public static int getCodigoProduto()
    {
        //return conexao.getCodigoPublico("tb_produto", String.valueOf(  cmbProduto.getSelectedItem()));   
        return produtosController.findByDesignacao( cmbProduto.getSelectedItem().toString() ).getCodigo();

    }

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

    //devolve o codigo_barra de uma determinada tabela
    public double getPrecoProduto( int codProduto, boolean stocavel )
    {

        String sql = "";

        if ( stocavel )
        {
            sql = "SELECT preco_venda FROM tb_stock WHERE( cod_produto_codigo = " + codProduto + " AND cod_armazem = " + getCodigoArmazem() + ")";
        }
        else
        {
            sql = "SELECT preco FROM tb_produto WHERE( codigo = " + codProduto + ")";
            JOptionPane.showMessageDialog( null, "O produto provavelmente nao é estocavel!..." );
        }
        ResultSet rs = conexao.executeQuery( sql );

        try
        {
            if ( rs.next() )
            {
                if ( stocavel )
                {
                    return rs.getDouble( "preco_venda" );
                }
                else
                {
                    return rs.getDouble( "preco" );
                }
            }
        }
        catch ( SQLException ex )
        {
            return 0;
        }

        return 0;
    }

    public boolean setCredito()
    {
        //return rbCredito.isSelected();
        return false;
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">    
    private void init()
    {
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
        //FILIPE TUZA - FORÇAR SELECIONAR UMA MOEDA PADRÃO
        cmbMoeda.setSelectedIndex( 0 );
        cmbCliente.setModel( new DefaultComboBoxModel( clientesController.getVector() ) );
        cmbCliente.setSelectedItem( DVML._CLIENTE_CONSUMIDOR_FINAL );

        cmbFamilia.setModel( new DefaultComboBoxModel( familiaController.getVector() ) );
        cmbTipoDocumento.setModel( new DefaultComboBoxModel( documentosController.getVector() ) );
        cmbAnoEconomico.setModel( new DefaultComboBoxModel( anoEconomicoController.getVector() ) );
        txtQuatindade.setText( "1" );
        txtIniciaisCliente.requestFocus();
        dc_data_documento.setDate( new Date() );
        mostrar_ano_economico_serie();
        lb_proximo_documento.setText( "" );
        txtTotalPagar.setText( CfMethods.formatarComoMoeda( 0.0 ) );

        reset_valor_entregue();
        reset_desconto_global();

        setDocpadrao( dadosInstituicao.getDocpadrao() );
        setDesactivarvias( dadosInstituicao.getDesactivarvias() );
//        setEditarPrecos( dadosInstituicao.getEditarPreco() );
        setActivarNegocio( dadosInstituicao.getNegocio() );
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

    private int getIdFamilia()
    {
        try
        {
            return familiaController.getFamiliaByDesignacao( String.valueOf( cmbFamilia.getSelectedItem() ) ).getPkFamilia();

        }
        catch ( Exception e )
        {
            return 0;
        }
    }

    private int getIdTipoProduto()
    {
        try
        {
            return tipoProdutoController.getTipoFamiliaByDesignacao( String.valueOf( cmbSubFamilia.getSelectedItem() ) ).getCodigo();

        }
        catch ( Exception e )
        {
            return 0;
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

    private static void mostrar_proximo_codigo_documento()
    {

        try
        {
            BDConexao conexaoLocal = new BDConexao();
            documentosController = new DocumentosController( conexaoLocal );
            anoEconomicoController = new AnoEconomicoController( conexaoLocal );
            vendasController = new VendasController( conexaoLocal );

            documento = (Documento) documentosController.findById( getIdDocumento( documentosController ) );
            anoEconomico = (AnoEconomico) anoEconomicoController.findById( getIdAnoEconomico( anoEconomicoController ) );
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

    private static void mostrar_proximo_codigo_documento( BDConexao conexao )
    {
        try
        {
            documentosController = new DocumentosController( conexao );
            anoEconomicoController = new AnoEconomicoController( conexao );

            documento = (Documento) documentosController.findById( getIdDocumento() );
            anoEconomico = (AnoEconomico) anoEconomicoController.findById( getIdAnoEconomico() );
            doc_prox_cod = vendasController.getUltimaContagemByIdDocumentoAndAnoEconomico(
                    getIdDocumento(), getIdAnoEconomico() ) + 1;
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
        TbProduto produto = (TbProduto) produtosController.findById( idProduto );
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

    private static double getValorComImposto( double qtd, double taxa, double preco_venda, double desconto )
    {
        double subtotal_linha = (preco_venda * qtd);
        double desconto_valor = (subtotal_linha * ( desconto / 100 ));
        double valor_iva = 1 + ( taxa / 100 );//
        return ( ( subtotal_linha - desconto_valor ) * valor_iva );

//        double valor_iva = 1 + ( taxa / 100 );//
//        double desconto_valor =  ( preco_venda * ( desconto / 100 ) );
//        double preco_unitario_com_taxa = ( preco_venda - desconto_valor ) * valor_iva;
//        double subtotal_linha = preco_unitario_com_taxa * qtd;
//
//        return subtotal_linha;
    }

    private static double getValorIliquido( double qtd, double preco_venda, double desconto )
    {
        double subtotal_linha = (preco_venda * qtd);
        double desconto_valor = (subtotal_linha * ( desconto / 100 ));
        return ( ( subtotal_linha - desconto_valor ) );

    }

    private static double getValorComImpostoIva( double qtd, double taxa, double preco_venda, double desconto )
    {
        double subtotal_linha = (preco_venda * qtd);
        double desconto_valor = (subtotal_linha * ( desconto / 100 ));
        double valor_iva = 1 + ( taxa / 100 );//
        return ( ( subtotal_linha - desconto_valor ) * valor_iva );

    }

    private static double getValorComRetencao( double qtd, double ret, double preco_venda, double desconto )
    {
        double subtotal_linha = (preco_venda * qtd);
        double desconto_valor = (subtotal_linha * ( desconto / 100 ));
        double valor_ret = (( ( subtotal_linha - desconto_valor ) * ret ) / 100);//
        return ( valor_ret );

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

    private void setWindowsListener()
    {

        this.addWindowListener( new WindowAdapter()
        {
            @Override
            public void windowActivated( WindowEvent e )
            {
                mostrar_proximo_codigo_documento();
                MetodosUtil.verificarCaixa( caixasController,
                        cod_usuario,
                        RootVisao.btn_abertura_dia_root,
                        RootVisao.btn_abertura_dia_root,
                        btnFormaPagamento );

            }

        } );

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

            case DVML.DOC_GUIA_TRANSPORTE_GT:
                this.abreviacao = Abreviacao.GT;
                break;

            default:
                break;
        }

    }

    private void desabilitar_campos()
    {

        boolean proformaNaoSelecionado = !( DVML.DOC_FACTURA_PROFORMA_PP == getIdDocumento() );

        boolean documentoIsFA = DVML.DOC_FACTURA_FT == getIdDocumento();
        boolean documentoIsPP = DVML.DOC_FACTURA_PROFORMA_PP == getIdDocumento();
        boolean documentoIsGT = DVML.DOC_GUIA_TRANSPORTE_GT == getIdDocumento();
        System.err.println( "documentoIsFA: " + documentoIsFA );
        System.err.println( "documentoIsPP: " + documentoIsPP );
        ck_A4.setSelected( !documentoIsFA && !documentoIsPP && !documentoIsGT );
        btnProcessar.setVisible( documentoIsPP || documentoIsFA || documentoIsGT );
        btnFormaPagamento.setVisible( !documentoIsFA && !documentoIsPP && !documentoIsGT );
    }

    private static Moeda getMoeda()
    {
        String moedaSelecionada = (String) cmbMoeda.getSelectedItem();

        if ( moedaSelecionada == null )
        {
            return null;
        }

        return moedasController.getMoedaByDesignacao( moedaSelecionada );
    }
//refresh

    private void refresh_table()
    {

        DefaultTableModel modelo = (DefaultTableModel) table.getModel();

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

    private static void refresh_table( int idMoeda )
    {

        Moeda moeda_local = (Moeda) moedasController.findById( idMoeda );
        DefaultTableModel modelo = (DefaultTableModel) table.getModel();

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

    private double getValorTotalDescontoLinha()
    {

        DefaultTableModel modelo = (DefaultTableModel) table.getModel();

        double desconto_linha = 0;

        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {
            desconto_linha += CfMethods.parseMoedaFormatada( modelo.getValueAt( i, 5 ).toString() );
        }
        return desconto_linha;

    }

    private void actualizar_moeda()
    {
        CfMethods.MOEDA = getMoeda().getAbreviacao();
        mostrar_abreviacao_moeda_cambio();
        refresh_table();
    }

    private static void actualizar_moeda( String moeda )
    {
        CfMethods.MOEDA = moeda;
        mostrar_abreviacao_moeda_cambio();
        refresh_table( 1 );
    }

    /**
     * @author: Engº. Domingos Dala Vunge
     * @nomefunção: data_documento_superior_ao_ultimo_doc
     * @datacriação: 2020/02/14 04:33:43
     * @dataactualização: 2020/02/17 04:33:43
     * @descrição: Verifica se a data do documento é inferior ao último
     * documento da mesma série, retorna verdadeiro se é inferior caso contrário
     * falso
     * @return
     */
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
            TbVenda venda_local = (TbVenda) vendasController.findById( cod_ultima_venda );
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

    /**
     * @author: Engº. Domingos Dala Vunge
     * @nomefunção: data_documento_superior_data_actual
     * @data: 2020/02/14 04:45:43
     * @descrição: Verifica se a data do documento é superior ao actual, retorna
     * verdadeiro se é inferior caso contrário falso
     */
//    private void data_documento_superior_data_actual()
//    {
//
//        //retirando a data do documento
//        Date data_documento = dc_data_documento.getDate();
//        //pegando a data actual do sistema 
//        Date data_sistema = new Date();
//        //comparar as datas
//        if ( MetodosUtil.maior_data_1_data_2( data_documento, data_sistema ) )
//        {
//            JOptionPane.showMessageDialog( null, "Após essa emissão, não poderá ser emitido um novo documento\n "
//                    + "com a data actual ou anterior, dentro da mesma série.",
//                    "AVISO", JOptionPane.WARNING_MESSAGE );
//
//            this.aviso_continuar_documento = JOptionPane.showConfirmDialog( null, "Ainda assim deseja continuar com a operação ?" )
//                    == JOptionPane.YES_OPTION;
//
//        }
//        else
//        {
//            this.aviso_continuar_documento = true;
//        }
//
//    }
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
        DefaultTableModel modelo = (DefaultTableModel) table.getModel();
        double taxa = 0.0;
        int codigo_produto = 0;
        List<TbProduto> lista_produtos_isentos = new ArrayList<>();
        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {
            codigo_produto = Integer.parseInt( modelo.getValueAt( i, 0 ).toString() );
            taxa = Double.parseDouble( modelo.getValueAt( i, 6 ).toString() );
            if ( taxa == 0.0 )
            {
                lista_produtos_isentos.add( (TbProduto) produtosController.findById( codigo_produto ) );
            }
        }

        return lista_produtos_isentos;

    }

    private static void reset_desconto_global()
    {
        sp_desconto_financeiro.setModel( CfMethodsSwing.criarSpinnerDoubleModel( 0.0, 10000000000.00, 0.0 ) );
    }

    private static void reset_valor_entregue()
    {
//        txtValorEntregue.setText( "" );
    }

    private static double getTotalIncidencia()
    {
        DefaultTableModel modelo = (DefaultTableModel) table.getModel();
        double qtd = 0;
        double incidencia = 0d, preco_unitario = 0d, desconto_valor_linha = 0;

        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {
            preco_unitario = CfMethods.parseMoedaFormatada( modelo.getValueAt( i, 3 ).toString() );
            qtd = Double.parseDouble( modelo.getValueAt( i, 4 ).toString() );
            double valor_percentagem = Double.parseDouble( modelo.getValueAt( i, 5 ).toString() );
            double taxa = Double.parseDouble( modelo.getValueAt( i, 6 ).toString() );
            // a incidência só é aplicável ao produtos sujeitos a iva 
            if ( taxa != 0 )
            {
                desconto_valor_linha = ( ( valor_percentagem ) / 100 );
                double valor_unitario = (preco_unitario * qtd);
                incidencia += ( ( valor_unitario ) - ( valor_unitario * desconto_valor_linha ) );

            }

        }

        return incidencia;
    }

    private static double getTotalImposto()
    {
        DefaultTableModel modelo = (DefaultTableModel) table.getModel();
        double qtd = 0d;
        double imposto = 0d, preco_unitario = 0d, desconto_valor_linha = 0d;

        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {
            preco_unitario = CfMethods.parseMoedaFormatada( modelo.getValueAt( i, 3 ).toString() );
            qtd = Double.parseDouble( modelo.getValueAt( i, 4 ).toString() );
            double valor_percentagem = Double.parseDouble( modelo.getValueAt( i, 5 ).toString() );
            double taxa = Double.parseDouble( modelo.getValueAt( i, 6 ).toString() );
            // a incidência só é aplicável ao produtos sujeitos a iva 
            if ( taxa != 0 )
            {
                double valor_unitario = (preco_unitario * qtd);
                desconto_valor_linha = valor_unitario * ( ( valor_percentagem ) / 100 );
                imposto += ( ( valor_unitario - desconto_valor_linha ) * ( taxa / 100 ) );

            }

        }

        return imposto;
    }

    private static double getTotalRetencao()
    {
        DefaultTableModel modelo = (DefaultTableModel) table.getModel();
        double valor_retencao = 0d;

        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {
            valor_retencao = CfMethods.parseMoedaFormatada( modelo.getValueAt( i, 8 ).toString() );

        }

        return valor_retencao;
    }
//    private static double getTotalRetencao()
//    {
//        DefaultTableModel modelo = ( DefaultTableModel ) table.getModel();
//        double qtd = 0d;
//        double imposto = 0d, preco_unitario = 0d, desconto_valor_linha = 0d, valor_taxa = 0d;
//
//        for ( int i = 0; i < modelo.getRowCount(); i++ )
//        {
//            preco_unitario = CfMethods.parseMoedaFormatada( modelo.getValueAt( i, 3 ).toString() );
//            qtd = Double.parseDouble( modelo.getValueAt( i, 4 ).toString() );
//            double valor_percentagem = Double.parseDouble( modelo.getValueAt( i, 5 ).toString() );
//            double taxa = Double.parseDouble( modelo.getValueAt( i, 7 ).toString() );
//            // a incidência só é aplicável ao produtos sujeitos a iva 
//            if ( taxa != 0 )
//            {
//                double valor_unitario = ( preco_unitario * qtd );
//                desconto_valor_linha = valor_unitario * ( ( valor_percentagem ) / 100 );
//                imposto += ( ( valor_unitario - desconto_valor_linha ) * ( taxa / 100 ) );
//                
//
//            }
//
//        }
//
//        return imposto;
//    }

    private static double getTotalRetencao1()
    {
        DefaultTableModel modelo = (DefaultTableModel) table.getModel();
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

    private static double getTotalIncidenciaIsento()
    {
        DefaultTableModel modelo = (DefaultTableModel) table.getModel();
        double qtd = 0d;
        double incidencia_isento = 0d, preco_unitario = 0d, desconto_valor_linha = 0;

        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {
            preco_unitario = CfMethods.parseMoedaFormatada( modelo.getValueAt( i, 3 ).toString() );
            qtd = Double.parseDouble( modelo.getValueAt( i, 4 ).toString() );
            double valor_percentagem = Double.parseDouble( modelo.getValueAt( i, 5 ).toString() );
            double taxa = Double.parseDouble( modelo.getValueAt( i, 6 ).toString() );
            // a incidência também é aplicável à produtos isentos do iva 
            if ( taxa == 0 )
            {
                desconto_valor_linha = ( ( valor_percentagem ) / 100 );
                double valor_unitario = (preco_unitario * qtd);
                incidencia_isento += ( ( valor_unitario ) - ( valor_unitario * desconto_valor_linha ) );

            }

        }

        return incidencia_isento;
    }

    private static double getDescontoComercial()
    {
        DefaultTableModel modelo = (DefaultTableModel) table.getModel();
        double qtd = 0d;
        double desconto_comercial = 0d, preco_unitario = 0d, desconto_valor_linha = 0d;

        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {
            preco_unitario = CfMethods.parseMoedaFormatada( modelo.getValueAt( i, 3 ).toString() );
            qtd = Double.parseDouble( modelo.getValueAt( i, 4 ).toString() );
            double valor_percentagem = Double.parseDouble( modelo.getValueAt( i, 5 ).toString() );
            desconto_valor_linha = ( ( valor_percentagem ) / 100 );
            double valor_unitario = (preco_unitario * qtd);
            desconto_comercial += ( valor_unitario * desconto_valor_linha );

        }

        return desconto_comercial;
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

    private static BigDecimal getTotalVendaIVASemIncluirDesconto()
    {
        BigDecimal totalIva = BigDecimal.ZERO;

        DefaultTableModel modelo = (DefaultTableModel) table.getModel();

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
        return getTotalIliquido().add( getTotalVendaIVASemIncluirDesconto() ).setScale( 2, RoundingMode.HALF_UP );
    }

    private static String iniciais_extenso()
    {
        Documento documento_local = (Documento) documentosController.findById( getIdDocumento() );
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
//    5417221915

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
            JOptionPane.showMessageDialog( null, "Não existe cliente com código" );
            cmbCliente.setSelectedItem( "Consumidor Final" );
        }
//        txtNifClientePesquisa.setText( "" );
        txtNifClientePesquisa.requestFocus();
    }

    private static boolean verifica_ano_documento_igual_economico()
    {
        AnoEconomico anoEconomicoLocal = (AnoEconomico) anoEconomicoController.findById( getIdAnoEconomico() );
        int ano_economico = Integer.parseInt( anoEconomicoLocal.getDesignacao() );
        int ano_documento = dc_data_documento.getDate().getYear() + 1900;
        return ano_documento == ano_economico;

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
//    private void setFolhaImpressora( String folha )
//    {
//        if ( folha.equalsIgnoreCase( "A6" ) )
//        {
//            ck_simplificada.setSelected( true );
//            ck_A4.setSelected( false );
//            this.abreviacao = Abreviacao.FR_S;
//        }
//        else
//        {
//            ck_A4.setSelected( true );
//            ck_simplificada.setSelected( false );
//            this.abreviacao = Abreviacao.FR_A4;
//        }
//    }
//    
//    private void actualizarPreco( PropertyChangeEvent evt )
//    {
//        DefaultTableModel modelo = (DefaultTableModel) table.getModel();
//        int linhaSelecionada = table.getSelectedRow();
//
//        try
//        {
//            Float precoDigitado = Float.parseFloat( modelo.getValueAt( linhaSelecionada, 3 ).toString() );
//            System.out.println( "Preco: " + precoDigitado );
//
//            int codProduto = Integer.parseInt( modelo.getValueAt( linhaSelecionada, 0 ).toString() );
//            TbPreco precoAnterior = precoDao.getLastPrecoByIdProduto( codProduto );
//            System.out.println( "Preco Anterior: " + precoAnterior.getPrecoVenda() );
//            if ( precoDigitado != precoAnterior.getPrecoVenda() )
//            {
//                //criar um preco temprario
//                TbPreco precoTemporario = new TbPreco();
//                precoTemporario.setPercentagemGanho( 0d );
//                precoTemporario.setPrecoCompra( precoAnterior.getPrecoCompra() );
//                precoTemporario.setPrecoVenda( precoDigitado );
//                precoTemporario.setQtdBaixo( 0 );
//                precoTemporario.setQtdAlto( precoAnterior.getQtdAlto() );
//                precoTemporario.setFkUsuario( usuarioDao.findTbUsuario( cod_usuario ) );
//                precoTemporario.setFkProduto( produtoDao.findTbProduto( codProduto ) );
//                precoTemporario.setData( new Date() );
//                precoTemporario.setHora( new Date() );
//                precoTemporario.setPrecoAnterior( precoAnterior.getPrecoAnterior() );
//
//                precoDao.create( precoTemporario );
//                System.out.println( "preco temprario criado" );
//                map.put( codProduto, precoAnterior );
//                System.out.println( "adicionado na tabela hash" );
//            }
//
//            int qtd = Integer.parseInt( modelo.getValueAt( linhaSelecionada, 4 ).toString() );
//            Float desconto = Float.parseFloat( modelo.getValueAt( linhaSelecionada, 5 ).toString() );
//            Float subtotal = ( precoDigitado * qtd );
//
//            modelo.setValueAt( MetodosUtil.retirar_dizimas( getDesconto_produto( subtotal ) ), linhaSelecionada, 5 );
//            modelo.setValueAt( subtotal, linhaSelecionada, 8 );
//
//            setTotalPagar();
//            setTotalCompleto();
//            valor_por_extenco();
//
//        }
//        catch ( Exception e )
//        {
//            e.printStackTrace();
//        }
//
//    }
//
//    private void restaurarPrecos(){
//        
//        if( !map.isEmpty()){
//            
//            for ( Map.Entry<Integer, TbPreco> entry : map.entrySet() ){
//                
//                Integer codProduto = entry.getKey();
//                TbPreco preco = entry.getValue();
//                precoDao.create( preco );
//                
//            }
//            map.clear();
//            
//            
//        }
//        
//        
//        
//    }

    private void setFocus( String focus )
    {
        if ( focus.equalsIgnoreCase( "Codigo Interno" ) )
        {

            txtCodigoProduto.requestFocus();

        }
        else
        {

            txtCodigoBarra.requestFocus();

        }
    }

    private void setDesactivarvias( String desactivarvias )
    {

        if ( desactivarvias.equalsIgnoreCase( "Sim" ) )
        {

            spnCopia.setVisible( true );
            lbVias.setVisible( true );

        }
        else
        {

            spnCopia.setVisible( false );
            lbVias.setVisible( false );

        }

    }

    private void setDesactivarLugar( String desactivarLugar )
    {

        if ( desactivarLugar.equalsIgnoreCase( "Sim" ) )
        {

            spnCopia.setVisible( true );
            lbVias.setVisible( true );

        }
        else
        {

            spnCopia.setVisible( false );
            lbVias.setVisible( false );

        }

    }

//    private static void setEditarPrecos( String editarPrecos )
//    {
//
//        if ( editarPrecos.equalsIgnoreCase( "Nao" ) )
//        {
//
//            jRadioButtonEditarPreco.setEnabled(true );
//       
//            
//
//        }
//        else
//        {
//
//            jRadioButtonEditarPreco.setEnabled( false );
//           
//            
//        }
//
//    }
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

//    private void setImprimirA6( String imprimirA6 )
//    {
//        if ( imprimirA6.equalsIgnoreCase( "Directamente" ) )
//        {
//            rbMostrar.setSelected(true);
//
//        }
//        else
//        {
//            rbMostrar.setSelected(false);
//
//        }
//    }
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

//    public static void verificarCaixa()
//    {
//
//        if ( !caixasController.existeCaixas() )
//        {
//            btnFormaPagamento.setEnabled( false );
//            btnProcessar.setEnabled( false );
//            status_mensagem_primaria.setText( "" );
//        }
//        else if ( caixasController.existe_abertura() && caixasController.existe_fecho() )
//        {
//            btnFormaPagamento.setEnabled( false );
//            btnProcessar.setEnabled( false );
//            status_mensagem_primaria.setText( "Deves abrir o caixa." );
//
//        }
//        else
//        {
//            btnFormaPagamento.setEnabled( true );
//            btnProcessar.setEnabled( true );
//            status_mensagem_primaria.setText( "" );
//        }
//    }
    public void scrolltable()
    {

        table.scrollRectToVisible( table.getCellRect( table.getRowCount() - 1, table.getColumnCount(), true ) );

    }

    private void setActivarNegocio( String negocio )
    {
        if ( negocio.equalsIgnoreCase( "Comercial" ) )
        {
            jPanelCaracteristicas.setVisible( false );
        }
        else if ( negocio.equalsIgnoreCase( "Restaurante" ) )
        {
            jPanelCaracteristicas.setVisible( false );
        }
        else if ( negocio.equalsIgnoreCase( "Farmacia" ) )
        {
            jPanelCaracteristicas.setVisible( false );
        }
        else if ( negocio.equalsIgnoreCase( "Lavandaria" ) )
        {
            jPanelCaracteristicas.setVisible( false );
        }
        else if ( negocio.equalsIgnoreCase( "Prestação de Serviço" ) )
        {
            jPanelCaracteristicas.setVisible( false );
        }
        else
        {
            jPanelCaracteristicas.setVisible( true );

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

    public boolean validar()
    {
        boolean documentoIsFA = DVML.DOC_FACTURA_FT == getIdDocumento();
        boolean documentoIsPP = DVML.DOC_FACTURA_PROFORMA_PP == getIdDocumento();
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

    public static void fazerBackupAgora()
    {
        String data = new SimpleDateFormat( YYYYMMDD_HHMMSS ).format( new Date() );
//        String rodar_camando = "cmd /c mysqldump -uroot -pDoV90x?# --dump-date --triggers --tables --routines --skip-quote-names --compact --skip-opt --skip-set-charset --hex-blob kitanda_db > \"..\\BD_BACKUP\\_database_backup_" + data + ".sql\"";
        String rodar_camando = "cmd /c mysqldump --single-transaction -uroot -pDoV90x?# --dump-date --triggers --add-drop-database --routines --skip-quote-names --skip-set-charset --add-locks --disable-keys --databases kitanda_db > \"..\\BD_BACKUP\\_database_backup_" + data + ".sql\"";
//String rodar_camando = "cmd /c mysqldump --single-transaction=TRUE -uroot -pDoV90x?# --dump-date --triggers --add-drop-database  --routines --skip-quote-names --compact --skip-opt --skip-set-charset --hex-blob --add-locks --disable-keys --lock-tables  --databases kitanda_db > \"..\\BD_BACKUP\\_database_backup_" + data + ".sql\"";
        Process rodarComandoWindows = rodarComandoWindows( rodar_camando, true );

//        JOptionPane.showMessageDialog ( null, "Backup realizado com sucesso! ", "Notificação", JOptionPane.INFORMATION_MESSAGE );
        System.err.println( "Backup realizado com sucesso! " );

    }

    private void actualizarQtdTable()
    {

        linha_actual = table.getSelectedRow();

        if ( linha_actual > -1 )
        {

            int codProduto = Integer.parseInt( table.getValueAt( linha_actual, 0 ).toString() );
            double desconto = Double.parseDouble( table.getValueAt( linha_actual, 5 ).toString() );

            TbProduto produtoLocal = (TbProduto) produtosController.findById( codProduto );
            TbTipoProduto tipoProduto = (TbTipoProduto) tipoProdutoController.findById( produtoLocal.getCodTipoProduto().getCodigo() );

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
                resetValue( "Quantidade não pode ser zero(0) ou número négativo", 4 );
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

    private void resetValue( String msg, int columnValue )
    {
        System.out.println( "Cheguei aqui..." );
        table.setValueAt( 1, linha_actual, columnValue );
        JOptionPane.showMessageDialog( null, msg );
        table.clearSelection();
    }

    private static void setEditableColunaPreco( boolean isServico )
    {
        table.setModel( new javax.swing.table.DefaultTableModel(
                new Object[][]
                {
                },
                new String[]
                {
                    "Cod.", "Descrição", "Un.", "Preço", "Qtd.", "Desc.", "Taxa ", "Ret.", "V. Ret.", "Valor", "Valor C/ Imposto"
                }
        )
        {
            Class[] types = new Class[]
            {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Double.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean[]
            {
                false, false, false, isServico, true, true, false, false, false, false, false
            };

            @Override
            public Class getColumnClass( int columnIndex )
            {
                return types[ columnIndex ];
            }

            @Override
            public boolean isCellEditable( int rowIndex, int columnIndex )
            {
                return canEdit[ columnIndex ];
            }
        } );

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
        conexaoTransaction = new BDConexao();
        DocumentosController.startTransaction( conexaoTransaction );

        System.out.println( "ID PRODUTO: " + idProduto );

        int idPrecoRetalho = PrecosController.getLastIdPrecoByIdProdutoIntAndQTD( idProduto, 0d, conexao );
        System.out.println( "ID RETALHO: " + idPrecoRetalho );
        TbPreco precoAntigoRetalho = (TbPreco) precosControllerLocal.findById( idPrecoRetalho );
        System.out.println( "PRECO RETALHO: " + precoAntigoRetalho );

        int idPrecoGrosso = PrecosController.getLastIdPrecoByIdProdutoIntAndPrecoAntigoQtdAlto( idProduto, precoAntigoRetalho.getQtdAlto() + 1, conexao );
        TbPreco precoAntigoGrosso = (TbPreco) precosControllerLocal.findById( idPrecoGrosso );
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
            DocumentosController.commitTransaction( conexaoTransaction );
            conexaoTransaction.close();
//            precoDao.create(preco_novo_retalho);
            System.out.println( "Preco de compra retalho actualizado na venda" );
        }
        catch ( Exception e )
        {
            DocumentosController.rollBackTransaction( conexaoTransaction );
            e.printStackTrace();
            System.err.println( "Falha ao actualizar o preço retalho na venda" );
        }

        conexaoTransaction = new BDConexao();
        DocumentosController.startTransaction( conexaoTransaction );
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

            DocumentosController.commitTransaction( conexaoTransaction );
            conexaoTransaction.close();
//            precoDao.create(preco_novo_grosso);
            System.out.println( "Preco de compra grosso actualizado na compra" );
        }
        catch ( Exception e )
        {
            DocumentosController.rollBackTransaction( conexaoTransaction );
            e.printStackTrace();
            System.err.println( "Falha ao actualizar o preço grosso na compra" );
        }

    }

}
