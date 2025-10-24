/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package visao;


import java.sql.Connection;
import comercial.controller.*;

import dao.ItemPermissaoDao;
import dao.VendaDao;
import entity.Amortizacao;
import entity.AnoEconomico;
import entity.Cambio;
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
import java.beans.PropertyChangeEvent;
import java.math.BigDecimal;
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
import javax.persistence.EntityManagerFactory;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import static kitanda.util.CfConstantes.YYYYMMDD_HHMMSS;
import kitanda.util.CfMethods;
import kitanda.util.CfMethodsSwing;
import lista.ListaVenda1;
import util.BDConexao;
import util.DVML;
import util.DVML.Abreviacao;
import static util.DVML.CASAS_DECIMAIS;
import static util.DVML.DOC_FACTURA_RECIBO_FR;
import static util.DVML.DOC_FACTURA_FT;
import util.JPAEntityMannagerFactoryUtil;
import util.MetodosUtil;
import static util.MetodosUtil.rodarComandoWindows;

/**
 *
 * @author Domingos Dala Vunge
 */
public class AlteracaoGuiaTransporteVisao extends javax.swing.JFrame implements Runnable
{

    /**
     * CONTROLLER COMERCIAL
     */
    private EntityManagerFactory emf = JPAEntityMannagerFactoryUtil.em;
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
    private static VendaDao vendaDao;

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
    private static TbVenda guia_transporte_global;
    private static TbDadosInstituicao dadosInstituicao;
    private static BDConexao conexaoTransaction;

    public AlteracaoGuiaTransporteVisao(int cod_usuario, BDConexao conexao ) throws SQLException
    {

        initComponents();

        confiLabel();
        setLocationRelativeTo( null );
        jButton1.setVisible( false );
        status_mensagem_secundaria.setVisible( false );
        txtLocal.setVisible( false );
        txtReferencia.setVisible( false );
        txtRefDoc.requestFocus();
        //pegarResolucao();
        AlteracaoGuiaTransporteVisao.conexao = conexao;

        /**
         * INSTANCIAS DOS CONTROLLER COMERCIAL
         */
        vendasController = new VendasController( AlteracaoGuiaTransporteVisao.conexao );
        itemVendasController = new ItemVendasController( AlteracaoGuiaTransporteVisao.conexao );
        mesasController = new MesasController( AlteracaoGuiaTransporteVisao.conexao );
        lugaresController = new LugaresController( AlteracaoGuiaTransporteVisao.conexao );
        produtosController = new ProdutosController( AlteracaoGuiaTransporteVisao.conexao );
        stocksController = new StoksController( AlteracaoGuiaTransporteVisao.conexao );
        precosController = new PrecosController( AlteracaoGuiaTransporteVisao.conexao );
        tipoProdutoController = new TipoProdutosController( AlteracaoGuiaTransporteVisao.conexao );
        familiaController = new FamiliasController( AlteracaoGuiaTransporteVisao.conexao );
        armazensController = new ArmazensController( AlteracaoGuiaTransporteVisao.conexao );
        localController = new LocalController( AlteracaoGuiaTransporteVisao.conexao );
        unidadesController = new UnidadesController( AlteracaoGuiaTransporteVisao.conexao );
        anoEconomicoController = new AnoEconomicoController( AlteracaoGuiaTransporteVisao.conexao );
        clientesController = new ClientesController( AlteracaoGuiaTransporteVisao.conexao );
        documentosController = new DocumentosController( AlteracaoGuiaTransporteVisao.conexao );
        moedasController = new MoedasController( AlteracaoGuiaTransporteVisao.conexao );
        descontosController = new DescontosController( AlteracaoGuiaTransporteVisao.conexao );
        usuariosController = new UsuariosController( AlteracaoGuiaTransporteVisao.conexao );
        dadosInstituicaoController = new DadosInstituicaoController( AlteracaoGuiaTransporteVisao.conexao );
        produtosImpostoController = new ProdutosImpostoController( AlteracaoGuiaTransporteVisao.conexao );
        produtosIsentoController = new ProdutosIsentoController( AlteracaoGuiaTransporteVisao.conexao );
        caixasController = new CaixasController( AlteracaoGuiaTransporteVisao.conexao );
        armazensAccessoController = new ArmazensAccessoController( AlteracaoGuiaTransporteVisao.conexao );
        cambiosController = new CambiosController( AlteracaoGuiaTransporteVisao.conexao );
        formaPagamentoItemController = new FormaPagamentoItemController( AlteracaoGuiaTransporteVisao.conexao );
        servicosRetencaoController = new ServicosRetencaoController( AlteracaoGuiaTransporteVisao.conexao );
        vendaDao = new VendaDao( emf );
        dadosInstituicao = (TbDadosInstituicao) dadosInstituicaoController.findById( 1 );

        txtQuatindade.setText( "1" );
        txtQuatindade.setDocument( new PermitirNumeros() );
        this.cod_usuario = cod_usuario;
        lbPreco7.setVisible( false );
        jlStockNegativo.setVisible( false );
        rbTranstorno.setVisible( false );
        rbMostrar.setVisible( false );
        rbArmazem1.setVisible( false );
        rbArmazem.setVisible( false );
        setWindowsListener();

        try
        {

            init();

        }
        catch ( Exception e )
        {
//            e.printStackTrace();
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
        //new BuscaProdutoVisao( VendaUsuarioVisao.this, rootPaneCheckingEnabled, getCodigoArmazem(), DVML.JANELA_VENDA ).setVisible(true);
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
                                new BuscaProdutoVisao( getInstance(), rootPaneCheckingEnabled, getCodigoArmazem(), DVML.JANELA_VENDA, BDConexao.getInstancia()).setVisible(true);
                            }
                            catch (Exception ex )
                            {
                                ex.printStackTrace();
                            }
                            return true;

                        }
                        return false;
                    }
                } );

//        verificarCaixa();
    }

    public JFrame getInstance()
    {
        return this;
    }

    public void keypressed( java.awt.event.KeyEvent evt )
    {

        if ( evt.getKeyCode() == KeyEvent.VK_ENTER )
        {

//            txtIniciaisCliente.requestFocus();
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
        txtTotal_AOA_liquido = new javax.swing.JTextField();
        sp_desconto_financeiro = new javax.swing.JSpinner();
        txtTotal_AOA_Retencao = new javax.swing.JLabel();
        lbTotalPagar2 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        btnCancelar = new javax.swing.JButton();
        btnProcessar = new javax.swing.JButton();
        jlStockNegativo1 = new javax.swing.JLabel();
        ck_A4 = new javax.swing.JCheckBox();
        ck_Duplicada = new javax.swing.JCheckBox();
        ck_simplificada = new javax.swing.JCheckBox();
        ck_ComVirgula = new javax.swing.JCheckBox();
        ck_S_A6 = new javax.swing.JCheckBox();
        ck_A7 = new javax.swing.JCheckBox();
        lbPreco7 = new javax.swing.JLabel();
        cmbMoeda = new javax.swing.JComboBox();
        lb_usuario = new javax.swing.JLabel();
        lbValorPorExtenco = new javax.swing.JLabel();
        jlEmpresa = new javax.swing.JLabel();
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
        txtQuantidaStock = new javax.swing.JTextField();
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
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        table_guia_transporte = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        lbVias = new javax.swing.JLabel();
        spnCopia = new javax.swing.JSpinner();
        txtReferencia = new javax.swing.JTextField();
        rbTranstorno = new javax.swing.JRadioButton();
        jlStockNegativo = new javax.swing.JLabel();
        txtLocal = new javax.swing.JTextField();
        rbMostrar = new javax.swing.JRadioButton();
        dc_data_documento = new com.toedter.calendar.JDateChooser();
        jPanelCaracteristicas = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtMatricula = new javax.swing.JTextField();
        txtModelo = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtMotor = new javax.swing.JTextField();
        txtChassi = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtKilometragem = new javax.swing.JTextField();
        status_mensagem_primaria = new javax.swing.JLabel();
        status_mensagem_secundaria = new javax.swing.JLabel();
        cmbCliente = new javax.swing.JComboBox();
        cmbTipoDocumento = new javax.swing.JComboBox();
        lb_proximo_documento = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtRefDoc = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtObs = new javax.swing.JTextArea();
        jButton1 = new javax.swing.JButton();

        jMenuItem1.setText("jMenuItem1");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("...:::::  KITANDA - ALTERAÇÃO GUIA TRANSPORTE ::::...");

        jPanel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jPanel1.setFont(new java.awt.Font("Showcard Gothic", 0, 24)); // NOI18N

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel8.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lbDescontoFinanceiro.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbDescontoFinanceiro.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbDescontoFinanceiro.setText("Desconto :");
        jPanel8.add(lbDescontoFinanceiro, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 100, 20));

        txtTotal_AOA_liquido.setEditable(false);
        txtTotal_AOA_liquido.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N
        txtTotal_AOA_liquido.setForeground(new java.awt.Color(255, 0, 0));
        txtTotal_AOA_liquido.setCaretColor(new java.awt.Color(255, 255, 255));
        txtTotal_AOA_liquido.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtTotal_AOA_liquidoActionPerformed(evt);
            }
        });
        jPanel8.add(txtTotal_AOA_liquido, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 40, 280, 25));

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
        jPanel8.add(sp_desconto_financeiro, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 10, 280, 30));

        txtTotal_AOA_Retencao.setForeground(new java.awt.Color(255, 51, 51));
        txtTotal_AOA_Retencao.setText("Retencao");
        jPanel8.add(txtTotal_AOA_Retencao, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 130, 20));

        lbTotalPagar2.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbTotalPagar2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbTotalPagar2.setText("Total a Pagar :");
        jPanel8.add(lbTotalPagar2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 40, 110, 20));

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
        jPanel3.add(btnCancelar, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 10, 70, 40));

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
        jPanel3.add(btnProcessar, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 10, 140, 40));

        jlStockNegativo1.setForeground(new java.awt.Color(153, 0, 51));
        jlStockNegativo1.setText("Formatos :");
        jPanel3.add(jlStockNegativo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 80, 20));

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
        jPanel3.add(ck_A4, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 70, -1, -1));

        buttonGroup3.add(ck_Duplicada);
        ck_Duplicada.setFont(new java.awt.Font("Lucida Grande", 0, 10)); // NOI18N
        ck_Duplicada.setSelected(true);
        ck_Duplicada.setText("A5");
        ck_Duplicada.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ck_DuplicadaActionPerformed(evt);
            }
        });
        jPanel3.add(ck_Duplicada, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 70, -1, -1));

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
        jPanel3.add(ck_simplificada, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 70, -1, -1));

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
        jPanel3.add(ck_ComVirgula, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 70, 55, -1));

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
        jPanel3.add(ck_S_A6, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 70, -1, -1));

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
        jPanel3.add(ck_A7, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 70, -1, -1));

        lbPreco7.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbPreco7.setText("Moeda");
        jPanel3.add(lbPreco7, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 50, -1));

        cmbMoeda.setBackground(new java.awt.Color(4, 154, 3));
        cmbMoeda.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 14)); // NOI18N
        cmbMoeda.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbMoedaActionPerformed(evt);
            }
        });
        jPanel3.add(cmbMoeda, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 10, 110, -1));

        lb_usuario.setFont(new java.awt.Font("Lucida Grande", 1, 12)); // NOI18N
        lb_usuario.setText("Conta:");

        lbValorPorExtenco.setFont(new java.awt.Font("Lucida Grande", 1, 12)); // NOI18N
        lbValorPorExtenco.setForeground(new java.awt.Color(204, 0, 0));
        lbValorPorExtenco.setText("VALOR POR EXTENSO");

        jlEmpresa.setFont(new java.awt.Font("Lucida Grande", 1, 12)); // NOI18N
        jlEmpresa.setForeground(new java.awt.Color(0, 0, 102));
        jlEmpresa.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jlEmpresa.setText("Empresa");

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
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 570, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(lbValorPorExtenco, javax.swing.GroupLayout.PREFERRED_SIZE, 617, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jlEmpresa, javax.swing.GroupLayout.DEFAULT_SIZE, 430, Short.MAX_VALUE)))
                        .addContainerGap())))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbValorPorExtenco, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jlEmpresa, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(59, 59, 59)
                .addComponent(lb_usuario, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 15), new java.awt.Color(4, 154, 3))); // NOI18N
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
        jPanel4.add(lbQuantidadeStock, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 143, -1, 50));

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

        txtQuantidaStock.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        txtQuantidaStock.setForeground(new java.awt.Color(255, 255, 255));
        jPanel4.add(txtQuantidaStock, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 140, 70, 60));

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
                .addComponent(cmbArmazem, 0, 270, Short.MAX_VALUE)
                .addContainerGap())
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

        jPanel4.add(jPanelData, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 470, 50));

        jPanel2.setBackground(new java.awt.Color(204, 204, 204));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        table_guia_transporte.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        table_guia_transporte.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "Cod.", "Descrição", "Un.", "Preço", "Qtd.", "Desc.", "Taxa ", "Ret.", "V. Ret.", "Valor", "Valor C/ Imposto", "Qtd.Dev.", "Qtd.Rec."
            }
        )
        {
            Class[] types = new Class []
            {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Double.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean []
            {
                false, false, false, false, false, true, false, false, false, false, false, true, false
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
        table_guia_transporte.setCellSelectionEnabled(true);
        table_guia_transporte.setGridColor(new java.awt.Color(51, 153, 0));
        table_guia_transporte.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                table_guia_transporteMouseClicked(evt);
            }
        });
        table_guia_transporte.addPropertyChangeListener(new java.beans.PropertyChangeListener()
        {
            public void propertyChange(java.beans.PropertyChangeEvent evt)
            {
                table_guia_transportePropertyChange(evt);
            }
        });
        jScrollPane1.setViewportView(table_guia_transporte);
        table_guia_transporte.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        if (table_guia_transporte.getColumnModel().getColumnCount() > 0)
        {
            table_guia_transporte.getColumnModel().getColumn(0).setPreferredWidth(30);
            table_guia_transporte.getColumnModel().getColumn(1).setPreferredWidth(350);
            table_guia_transporte.getColumnModel().getColumn(2).setPreferredWidth(30);
            table_guia_transporte.getColumnModel().getColumn(3).setPreferredWidth(100);
            table_guia_transporte.getColumnModel().getColumn(4).setPreferredWidth(40);
            table_guia_transporte.getColumnModel().getColumn(5).setPreferredWidth(50);
            table_guia_transporte.getColumnModel().getColumn(6).setPreferredWidth(40);
            table_guia_transporte.getColumnModel().getColumn(7).setPreferredWidth(1);
            table_guia_transporte.getColumnModel().getColumn(8).setPreferredWidth(1);
            table_guia_transporte.getColumnModel().getColumn(9).setPreferredWidth(85);
            table_guia_transporte.getColumnModel().getColumn(10).setPreferredWidth(110);
        }

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 352, Short.MAX_VALUE))
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jLabel1.setFont(new java.awt.Font("Superclarendon", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(4, 154, 3));
        jLabel1.setText("KITANDA v1.2");

        lbVias.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbVias.setText("Via(s):");

        txtReferencia.setEnabled(false);

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

        txtLocal.setEditable(false);

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

        jPanelCaracteristicas.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Características", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 15), new java.awt.Color(204, 0, 0))); // NOI18N

        jLabel3.setText("Matr:");

        jLabel4.setText("Modelo:");

        txtMatricula.setFont(new java.awt.Font("Lucida Grande", 0, 12)); // NOI18N
        txtMatricula.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtMatriculaActionPerformed(evt);
            }
        });

        txtModelo.setFont(new java.awt.Font("Lucida Grande", 0, 12)); // NOI18N
        txtModelo.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtModeloActionPerformed(evt);
            }
        });

        jLabel5.setText("Nº Motor");

        jLabel6.setText("Nº Chassi:");

        txtMotor.setFont(new java.awt.Font("Lucida Grande", 0, 12)); // NOI18N
        txtMotor.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtMotorActionPerformed(evt);
            }
        });

        txtChassi.setFont(new java.awt.Font("Lucida Grande", 0, 12)); // NOI18N
        txtChassi.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtChassiActionPerformed(evt);
            }
        });

        jLabel7.setText("Klm:");

        txtKilometragem.setFont(new java.awt.Font("Lucida Grande", 0, 12)); // NOI18N
        txtKilometragem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtKilometragemActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelCaracteristicasLayout = new javax.swing.GroupLayout(jPanelCaracteristicas);
        jPanelCaracteristicas.setLayout(jPanelCaracteristicasLayout);
        jPanelCaracteristicasLayout.setHorizontalGroup(
            jPanelCaracteristicasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelCaracteristicasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelCaracteristicasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelCaracteristicasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelCaracteristicasLayout.createSequentialGroup()
                        .addComponent(txtMatricula, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtKilometragem, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtModelo, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelCaracteristicasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanelCaracteristicasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelCaracteristicasLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(txtMotor, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtChassi)))
        );
        jPanelCaracteristicasLayout.setVerticalGroup(
            jPanelCaracteristicasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelCaracteristicasLayout.createSequentialGroup()
                .addGroup(jPanelCaracteristicasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanelCaracteristicasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtMatricula, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanelCaracteristicasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtMotor, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addComponent(txtKilometragem, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                .addGap(7, 7, 7)
                .addGroup(jPanelCaracteristicasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtModelo)
                    .addComponent(txtChassi)))
            .addGroup(jPanelCaracteristicasLayout.createSequentialGroup()
                .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(7, 7, 7)
                .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        status_mensagem_primaria.setFont(new java.awt.Font("Lucida Grande", 1, 12)); // NOI18N
        status_mensagem_primaria.setForeground(new java.awt.Color(204, 153, 0));
        status_mensagem_primaria.setText("sahdbvhsdva");

        status_mensagem_secundaria.setFont(new java.awt.Font("Lucida Grande", 1, 12)); // NOI18N
        status_mensagem_secundaria.setText("...");

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

        cmbTipoDocumento.setBackground(new java.awt.Color(4, 154, 3));
        cmbTipoDocumento.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 12)); // NOI18N
        cmbTipoDocumento.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbTipoDocumentoActionPerformed(evt);
            }
        });

        lb_proximo_documento.setFont(new java.awt.Font("Lucida Grande", 1, 10)); // NOI18N
        lb_proximo_documento.setText("PRÓX. DOC. : XX PP/A1");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        jLabel2.setText("Ref. Guia:");

        txtRefDoc.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtRefDocActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(cmbCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 454, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtRefDoc)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbTipoDocumento, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lb_proximo_documento)
                        .addGap(14, 14, 14))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(lbVias, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(spnCopia, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jlStockNegativo, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(4, 4, 4)
                                .addComponent(txtReferencia, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(dc_data_documento, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(rbTranstorno)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(rbMostrar)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(status_mensagem_primaria, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(status_mensagem_secundaria)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtLocal, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jPanelCaracteristicas, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 6, Short.MAX_VALUE))))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtReferencia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(dc_data_documento, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(rbTranstorno)
                            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(lbVias)
                                .addComponent(spnCopia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jlStockNegativo)
                                .addComponent(txtLocal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(status_mensagem_primaria)
                                .addComponent(status_mensagem_secundaria))
                            .addComponent(rbMostrar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(9, 9, 9))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addComponent(jPanelCaracteristicas, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtRefDoc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lb_proximo_documento, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cmbTipoDocumento, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        txtObs.setColumns(20);
        txtObs.setRows(5);
        txtObs.setBorder(javax.swing.BorderFactory.createTitledBorder("OBS"));
        jScrollPane2.setViewportView(txtObs);

        jButton1.setText("CAIXA");
        jButton1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 481, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addComponent(jScrollPane2))))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jButton1)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
//            lbClienteConsumidorFinal.setVisible( true );
//            txtNomeConsumidorFinal.setVisible( true );
        }
        else
        {
//            lbClienteConsumidorFinal.setVisible( false );
//            txtNomeConsumidorFinal.setVisible( false );

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
            JOptionPane.showMessageDialog( null, "Possivelmente não selecionaste \n nenhuma linha ou não existe dados na tabela", "AVISO", JOptionPane.WARNING_MESSAGE );
        }


    }//GEN-LAST:event_btn_removerActionPerformed

    private void btn_adicionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_adicionarActionPerformed
//        if ( validar() )
//        {
        adicionar_botao();
        scrolltable();
//        }

    }//GEN-LAST:event_btn_adicionarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void txtCodigoProdutoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCodigoProdutoActionPerformed
        // TODO add your handling code here:
//        if ( validar() )
//        {
        accao_codigo_interno_enter();
        scrolltable();
//        }
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
//        if ( validar() )
//        {

        setFocus( dadosInstituicao.getFoco() );
//        }
    }//GEN-LAST:event_txtQuatindadeActionPerformed

    private void txtTotal_AOA_liquidoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTotal_AOA_liquidoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTotal_AOA_liquidoActionPerformed

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
//            if ( validar() )
//            {
            new BuscaProdutoVisao( this, rootPaneCheckingEnabled, getCodigoArmazem(), DVML.JANELA_VENDA, BDConexao.getInstancia()).setVisible(true);
//            }
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

    private void txtMatriculaActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtMatriculaActionPerformed
    {//GEN-HEADEREND:event_txtMatriculaActionPerformed
        txtKilometragem.requestFocus();
    }//GEN-LAST:event_txtMatriculaActionPerformed

    private void txtModeloActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtModeloActionPerformed
    {//GEN-HEADEREND:event_txtModeloActionPerformed
        txtChassi.requestFocus();
    }//GEN-LAST:event_txtModeloActionPerformed

    private void txtMotorActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtMotorActionPerformed
    {//GEN-HEADEREND:event_txtMotorActionPerformed
        txtModelo.requestFocus();
    }//GEN-LAST:event_txtMotorActionPerformed

    private void txtChassiActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtChassiActionPerformed
    {//GEN-HEADEREND:event_txtChassiActionPerformed
        txtQuatindade.requestFocus();
    }//GEN-LAST:event_txtChassiActionPerformed

    private void txtKilometragemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtKilometragemActionPerformed
    {//GEN-HEADEREND:event_txtKilometragemActionPerformed
        txtMotor.requestFocus();
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

    private void btnProcessarActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnProcessarActionPerformed
    {//GEN-HEADEREND:event_btnProcessarActionPerformed
        // TODO add your handling code here:
//        if ( validar() )
//        {
        if ( MetodosUtil.licencaValidada( conexao ) )
        {
            procedimento_salvar_venda_comercial();
//                procedimento_salvar();
        }

//        }
    }//GEN-LAST:event_btnProcessarActionPerformed

    private void ck_ComVirgulaActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_ck_ComVirgulaActionPerformed
    {//GEN-HEADEREND:event_ck_ComVirgulaActionPerformed
        actualizar_abreviacao();
    }//GEN-LAST:event_ck_ComVirgulaActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton1ActionPerformed
    {//GEN-HEADEREND:event_jButton1ActionPerformed

        dispose();
        new LoginVisao(BDConexao.getInstancia()).setVisible(true);

//        new LoginVisao( new BDConexao() );
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

    private void table_guia_transportePropertyChange(java.beans.PropertyChangeEvent evt)//GEN-FIRST:event_table_guia_transportePropertyChange
    {//GEN-HEADEREND:event_table_guia_transportePropertyChange
        // TODO add your handling code here:

        try
        {
            atualizarResumo( evt );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }

    }//GEN-LAST:event_table_guia_transportePropertyChange

    private void table_guia_transporteMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_table_guia_transporteMouseClicked
    {//GEN-HEADEREND:event_table_guia_transporteMouseClicked
        // TODO add your handling code here:
        if ( evt.getClickCount() > 2 )
        {
            System.out.println( "xx" );
        }
    }//GEN-LAST:event_table_guia_transporteMouseClicked

    private void txtRefDocActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtRefDocActionPerformed
    {//GEN-HEADEREND:event_txtRefDocActionPerformed
        procedimento_busca();
    }//GEN-LAST:event_txtRefDocActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelar;
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
    private static javax.swing.JComboBox<String> cmbAnoEconomico;
    public static javax.swing.JComboBox cmbArmazem;
    public static javax.swing.JComboBox cmbCliente;
    public static javax.swing.JComboBox<String> cmbFamilia;
    public static javax.swing.JComboBox cmbMoeda;
    public static javax.swing.JComboBox cmbProduto;
    public static javax.swing.JComboBox cmbSubFamilia;
    public static javax.swing.JComboBox cmbTipoDocumento;
    private static com.toedter.calendar.JDateChooser dc_data_documento;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanelCaracteristicas;
    private javax.swing.JPanel jPanelData;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel jlEmpresa;
    private javax.swing.JLabel jlStockNegativo;
    private javax.swing.JLabel jlStockNegativo1;
    private javax.swing.JLabel lbCategoria;
    private javax.swing.JLabel lbCategoria1;
    private javax.swing.JLabel lbCodigoProduto;
    private javax.swing.JLabel lbCodigoProduto1;
    private javax.swing.JLabel lbCodigoProduto2;
    private javax.swing.JLabel lbDescontoFinanceiro;
    private javax.swing.JLabel lbPreco1;
    private javax.swing.JLabel lbPreco7;
    private javax.swing.JLabel lbProduto;
    private javax.swing.JLabel lbQuantidade;
    private javax.swing.JLabel lbQuantidadeStock;
    private javax.swing.JLabel lbTotalPagar2;
    public static javax.swing.JLabel lbValorPorExtenco;
    private javax.swing.JLabel lbVias;
    private javax.swing.JLabel lb_proximo_documento;
    private javax.swing.JLabel lb_usuario;
    private static javax.swing.JRadioButton rbArmazem;
    private static javax.swing.JRadioButton rbArmazem1;
    private static javax.swing.JRadioButton rbMostrar;
    private static javax.swing.JRadioButton rbTranstorno;
    private static javax.swing.JSpinner sp_desconto_financeiro;
    private static javax.swing.JSpinner spnCopia;
    public static javax.swing.JLabel status_mensagem_primaria;
    public static javax.swing.JLabel status_mensagem_secundaria;
    public static javax.swing.JTable table_guia_transporte;
    private static javax.swing.JTextField txtChassi;
    public static javax.swing.JTextField txtCodigoBarra;
    public static javax.swing.JTextField txtCodigoManual;
    public static javax.swing.JTextField txtCodigoProduto;
    private static javax.swing.JTextField txtKilometragem;
    private javax.swing.JTextField txtLocal;
    private static javax.swing.JTextField txtMatricula;
    private static javax.swing.JTextField txtModelo;
    private static javax.swing.JTextField txtMotor;
    private static javax.swing.JTextArea txtObs;
    public static javax.swing.JTextField txtPreco;
    public static javax.swing.JTextField txtQuantidaStock;
    public static javax.swing.JTextField txtQuatindade;
    private javax.swing.JTextField txtRefDoc;
    private static javax.swing.JTextField txtReferencia;
    private static javax.swing.JLabel txtTotal_AOA_Retencao;
    public static javax.swing.JTextField txtTotal_AOA_liquido;
    // End of variables declaration//GEN-END:variables

    //verifica se o produto existe na tabela do formulário visão isto é na jTable
    private static boolean exist_produto_tabela_formulario( int codigo )
    {

        DefaultTableModel modelo = (DefaultTableModel) table_guia_transporte.getModel();

        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {
            if ( Integer.parseInt( String.valueOf( table_guia_transporte.getValueAt( i, 0 ) ) ) == codigo )
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
//    private static void actuazlizar_quantidade_tabela_formulario( String quantidade, double desconto )
//    {
//        DefaultTableModel modelo = ( DefaultTableModel ) table_guia_transporte.getModel();
//        double qtd = Double.parseDouble( quantidade );
//        double preco_venda = CfMethods.parseMoedaFormatada( String.valueOf( modelo.getValueAt( linha_actual, 3 ) ) );
//        double taxa = Double.parseDouble( String.valueOf( modelo.getValueAt( linha_actual, 6 ) ) );
//
////        double total_iliquido_linha = (( preco_venda * qtd ) - desconto);
//        String total_iliquido_linha = CfMethods.formatarComoMoeda( getValorIliquido(
//                qtd,
//                preco_venda,
//                desconto
//        ) );
//
//        String total_liquido_linha = CfMethods.formatarComoMoeda( getValorComImpostoIva(
//                qtd,
//                taxa,
//                preco_venda,
//                desconto
//        ) );
//
//        modelo.setValueAt( qtd, linha_actual, 4 );
//        modelo.setValueAt( desconto, linha_actual, 5 );
////
//        modelo.setValueAt( total_iliquido_linha, linha_actual, 9 );
//        modelo.setValueAt( total_liquido_linha, linha_actual, 10 );
//        //a linha_actual recebe o default
//        linha_actual = -1;
//
//    }
    //actualiza a quantidade na tabela do formulário visão isto é na jTable
//    private static void actuazlizar_quantidade_tabela_formulario_devolvida( String quantidade_devolvida )
//    {
//        DefaultTableModel modelo = ( DefaultTableModel ) table_guia_transporte.getModel();
////        double qtd_devolvida = Double.parseDouble( String.valueOf( modelo.getValueAt( linha_actual, 11 ) ) ) ;
//        double qtd_devolvida = Double.parseDouble( quantidade_devolvida );
//        double qtd_normal = Double.parseDouble( String.valueOf( modelo.getValueAt( linha_actual, 4 ) ) );
//        double qtd_recepcionada = Double.parseDouble( String.valueOf( modelo.getValueAt( linha_actual, 12 ) ) );
//        double desconto = Double.parseDouble( String.valueOf( modelo.getValueAt( linha_actual, 5 ) ) );
//        double preco_venda = CfMethods.parseMoedaFormatada( String.valueOf( modelo.getValueAt( linha_actual, 3 ) ) );
//        double taxa = Double.parseDouble( String.valueOf( modelo.getValueAt( linha_actual, 6 ) ) );
//
////        double total_iliquido_linha = (( preco_venda * qtd ) - desconto);
//        Double qtd_total_recepcionada = getTotalRecepcionado(
//                qtd_normal,
//                qtd_devolvida,
//                qtd_recepcionada
//        //                preco_venda,
//        //                desconto
//        );
//
////        String total_liquido_linha = CfMethods.formatarComoMoeda( getValorComImpostoIva(
////                qtd_normal,
////                qtd_devolvida,
////                taxa,
////                preco_venda,
////                desconto
////        ) );
////        modelo.setValueAt( qtd_normal, linha_actual, 4 );
////        modelo.setValueAt( desconto, linha_actual, 5 );
//        modelo.setValueAt( qtd_devolvida, linha_actual, 11 );
//        modelo.setValueAt( qtd_total_recepcionada, linha_actual, 12 );
////
////        modelo.setValueAt( total_iliquido_linha, linha_actual, 9 );
////        modelo.setValueAt( total_liquido_linha, linha_actual, 10 );
//        //a linha_actual recebe o default
//        linha_actual = -1;
//
//    }
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
    private void accao_cliente()
    {
        String nomeCliente = (String) cmbCliente.getSelectedItem();
        boolean clienteDiverso = nomeCliente.equalsIgnoreCase( "Consumidor Final" );
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
        txtTotal_AOA_liquido.setText( String.valueOf( MetodosUtil.retirar_dizimas( resultado ) ).trim() );

    }

    private void calcularTotalSemDesconto()
    {

        double totalSemDesconto = 0;
        double resultado1 = 0;
        double percentagem_desconto = 0;
        double total_pagar = Double.parseDouble( txtTotal_AOA_liquido.getText().trim() );

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

    @Override
    public void run()
    {
        while ( true )
        {

//            verificarCaixa();
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
//        lbCliente.setVisible( false );
        cmbCliente.setVisible( false );

    }

    private void mostrar_nome()
    {
        TbUsuario usuario = (TbUsuario) usuariosController.findById( this.cod_usuario );
        //caso masculino
        if ( usuario.getCodigoSexo().getCodigo() == 1 )
        {
            lb_usuario.setText( "Operador: " + usuario.getNome() );
        }
        else
        {
            lb_usuario.setText( "Operadora: " + usuario.getNome() );
        }
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

//            if ( isStocavel && stockLocal.getQuantidadeExistente() <= stockLocal.getQuantCritica() )
//            {
//
//                txtQuantidaStock.setBackground( Color.RED );
//                txtQuantidaStock.setForeground( Color.BLACK );
//            }
//            else
//            {
//                txtQuantidaStock.setBackground( new Color( 51, 153, 0, 255 ) );
//            }
            txtCodigoBarra.setText( String.valueOf( produto_local.getCodBarra() ) );
            //actualizar
            txtLocal.setText( String.valueOf( produto_local.getCodLocal().getDesignacao() ) );
            txtCodigoProduto.setText( String.valueOf( produto_local.getCodigo() ) );

            if ( isStocavel && !Objects.isNull( stockLocal ) )
            {
                txtQuantidaStock.setText( String.valueOf( stockLocal.getQuantidadeExistente() ) );
            }
            else
            {
                txtQuantidaStock.setText( "0" );
            }

        }
        catch ( Exception ex )
        {
            ex.printStackTrace();
            txtQuantidaStock.setText( "0" );
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
                        txtQuantidaStock.setBackground( Color.RED );
                        txtQuantidaStock.setForeground( Color.BLACK );
                    }
                    else
                    {
                        txtQuantidaStock.setBackground( new Color( 51, 153, 0, 255 ) );
                    }
                    TbPreco precoLocal = precosController.getLastIdPrecoByIdProduto( codigo_produto, Double.parseDouble( txtQuatindade.getText() ) );
                    txtPreco.setText( String.valueOf( MetodosUtil.retirar_dizimas( precoLocal.getPrecoVenda().doubleValue() ) ) );
                    txtQuantidaStock.setText( String.valueOf( stockLocal.getQuantidadeExistente() ) );
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
                        txtQuantidaStock.setBackground( Color.RED );
                        txtQuantidaStock.setForeground( Color.BLACK );
                    }
                    else
                    {
                        txtQuantidaStock.setBackground( new Color( 51, 153, 0, 255 ) );
                    }
                    TbPreco precoLocal = precosController.getLastIdPrecoByIdProduto( getCodigoProduto(), Double.parseDouble( txtQuatindade.getText() ) );
                    txtPreco.setText( String.valueOf( MetodosUtil.retirar_dizimas( precoLocal.getPrecoVenda().doubleValue() ) ) );
                    txtQuantidaStock.setText( String.valueOf( stockLocal.getQuantidadeExistente() ) );
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
        txtTotal_AOA_liquido.setText( "0" );
        txtCodigoBarra.setText( "" );
//        txtNomeConsumidorFinal.setText( "" );
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

        int cod_produto = 0;
        double qtd = 0d;
        double qtd_aceite = 0d;

        DefaultTableModel modelo = (DefaultTableModel) table_guia_transporte.getModel();
        boolean transtorno = false;

        for ( int i = 0; i < table_guia_transporte.getRowCount(); i++ )
        {

            cod_produto = Integer.parseInt( String.valueOf( table_guia_transporte.getModel().getValueAt( i, 0 ) ) );
            qtd = Double.parseDouble( String.valueOf( table_guia_transporte.getModel().getValueAt( i, 4 ) ) );
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
        if ( !MetodosUtil.tabela_vazia( table_guia_transporte ) )
        {

            if ( verifica_ano_documento_igual_economico() )
            {

                if ( data_documento_superior_ou_igual_ao_ultimo_doc() )
                {
                    /**
                     * aviso se for necessário ao utilizador quando a data do
                     * documento é superior à actual ou seja do sistema
                     */
                    data_documento_superior_data_actual();
                    if ( aviso_continuar_documento )
                    {
                        //calcularTroco();
                        if ( !campos_invalido_imprimir() )
                        {
                            if ( true )
                            {
                                System.out.println( "STATUS: a processar a factura" );
//                                EntityManager em = JPAEntityMannagerFactoryUtil.createEntityManager();
//                                em.getTransaction().begin();

                                if ( DVML.MOEDA_KWANZA.equalsIgnoreCase( getMoeda().getAbreviacao() ) )
                                {
                                    salvar_venda_comercial();
//                                    salvar_venda();
                                }
                                else
                                {
                                    // JOptionPane.showMessageDialog(null, "Moeda Estrangeira");
                                    actualizar_moeda( DVML.MOEDA_KWANZA );
                                    salvar_venda_comercial();
//                                    salvar_venda();
                                }

//                                em.getTransaction().commit();
                                //actualiza a data para o próximo documento.
                                dc_data_documento.setDate( new Date() );
                                System.out.println( "STATUS: fim do processamento da factura" );
                            }

                        }
                    }

                }
                else
                {
                    JOptionPane.showMessageDialog( null, "O documento não pode ser processado porque possui uma data inferior ao úlimo documento efectuado", "AVISO", JOptionPane.WARNING_MESSAGE );
                }

            }
            else
            {
                JOptionPane.showMessageDialog( null, "A data do documento a ser emitido deve estar no intervalo do ano economico", "Aviso", JOptionPane.WARNING_MESSAGE );
            }
        }
        else
        {
            JOptionPane.showMessageDialog( null, "Caro usuário adicione item na tabela", "Aviso", JOptionPane.WARNING_MESSAGE );
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
        System.out.println( "TOTAL A PAGAR " + txtTotal_AOA_liquido.getText().trim() );

//        double valor_entregue = Double.parseDouble( txtValorEntregue.getText() );
        double total_pagar = Double.parseDouble( txtTotal_AOA_liquido.getText().trim() );
//        troco = valor_entregue - total_pagar;

        System.out.println( "TROCO " + troco );
//        txtTroco.setText( String.valueOf( MetodosUtil.retirar_dizimas( troco ) ).trim() );

    }

    private void tratar_troco()
    {
        try
        {
            double troco = 0.0;

            double total_pagar = CfMethods.parseMoedaFormatada( txtTotal_AOA_liquido.getText() );

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
                txtTotal_AOA_liquido.setText( CfMethods.formatarComoMoeda( desconto ) );
                valor_por_extenco();
            }
            else
            {
                desconto = ( total_pagar - valor_desconto_geral );
                txtTotal_AOA_liquido.setText( CfMethods.formatarComoMoeda( desconto ) );
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

        if ( documentoIsFA || documentoIsPP )
        {
            //EXCLUIR CONSUMIDOR FINAL
            cmbCliente.setModel( new DefaultComboBoxModel( clientesController.getVectorExcetoConsumidorFinal() ) );
        }
        else
        {
            cmbCliente.setModel( new DefaultComboBoxModel( clientesController.getVector() ) );
            cmbCliente.setSelectedItem( DVML._CLIENTE_CONSUMIDOR_FINAL );
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
            double total_pagar = CfMethods.parseMoedaFormatada( txtTotal_AOA_liquido.getText() );
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
                double total_pagar = Double.parseDouble( txtTotal_AOA_liquido.getText().trim() );
                double totalComDesconto = ( total_pagar * desconto ) / 100;
                double resultado = total_pagar - totalComDesconto;

                txtTotal_AOA_liquido.setText( String.valueOf( resultado ).trim() );

            }
            else if ( evt.getKeyCode() == KeyEvent.VK_BACK_SPACE )
            {
                char key = evt.getKeyChar();

                double desconto = 0;
                double total_pagar = Double.parseDouble( txtTotal_AOA_liquido.getText().trim() );

                try
                {
                    desconto = Double.parseDouble( prefixo.toString().trim().substring( 0, prefixo.length() - 2 ) );
                    desconto = Double.parseDouble( prefixo );
                    total_pagar = Double.parseDouble( txtTotal_AOA_liquido.getText().trim() );
                    double totalComDesconto = ( total_pagar * desconto ) / 100;
                    double resultado = total_pagar - totalComDesconto;
                    txtTotal_AOA_liquido.setText( String.valueOf( resultado ).trim() );
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

    public static double getValor_entregue()
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

    public static void adicionar_produto() throws SQLException
    {

        DefaultTableModel modelo = (DefaultTableModel) table_guia_transporte.getModel();
        double total_deconto = Double.parseDouble( String.valueOf( getPreco() ) ) * Double.parseDouble( String.valueOf( getQuantidade() ) );

        int codigo_produto = getCodigoProduto();
        if ( !exist_produto_tabela_formulario( codigo_produto ) )
        {
            if ( !validar_zero() )
            {

                String unidade = getUnidade_Produto();
                String descricao_produto = getDescricao_Produto();
                double desconto = getDescontoPercentagem();
                double qtd = getQuantidade();
                double preco = getPreco();
                System.out.println( "$$$$ PRECO: " + preco );
                double taxa = getTaxaImpostoIva( codigo_produto );
                double ret = getTaxaImpostoRet( codigo_produto );
                System.out.println( "RET: " + ret );
                System.out.println( "TAX: " + taxa );
                String moeda = CfMethods.formatarComoMoeda( getPreco() );
//              ( double qtd, double taxa, double preco_venda, double desconto )
                String total_linha_retencao = CfMethods.formatarComoMoeda( getValorComRetencao(
                        qtd,
                        ret,
                        preco,
                        desconto
                ) );

                String total_iliquido_linha = CfMethods.formatarComoMoeda( getTotal() );
                String total_liquido_linha = CfMethods.formatarComoMoeda( getValorComImpostoIva(
                        qtd,
                        taxa,
                        preco,
                        desconto
                ) );

                modelo.addRow( new Object[]
                {
                    codigo_produto,
                    descricao_produto,
                    unidade,
                    moeda,
                    qtd,
                    desconto,
                    taxa,
                    ret,
                    total_linha_retencao,
                    total_iliquido_linha,
                    total_liquido_linha

                } );

            }
            else
            {
                JOptionPane.showMessageDialog( null, "Atenção\nA quantidade a sair não pode ser igual a zero!" );
            }

        }

        else
        {
            JOptionPane.showMessageDialog( null, "O produto já consta na tabela." );
            //actuazlizar_quantidade_tabela_formulario( txtQuatindade.getText(), getDesconto_produto( total_deconto ) );
            // actuazlizar_quantidade_tabela_formulario( txtQuatindade.getText(), getDescontoPercentagem() );
        }

        System.out.println( "1:::::::::::" );
        //Total Retenção
        setTotalRetencao();
        //Total A Pagar
        setTotalPagar();

        calculaTotalIVA();

        valor_por_extenco();
//        txtQuatindade.setText( "1" );

        txtQuatindade.setText( String.valueOf( 1 ) );
        txtCodigoProduto.requestFocus();
    }

    private static void valor_por_extenco()
    {
        System.out.println( "Valor XXXXXXX: " + CfMethods.parseMoedaFormatada( txtTotal_AOA_liquido.getText() ) );
        lbValorPorExtenco.setText( MetodosUtil.valorPorExtenso( CfMethods.parseMoedaFormatada( txtTotal_AOA_liquido.getText() ), getMoeda().getDesignacao() ) );
    }

    private static void valor_por_extenco( Moeda moeda )
    {
        System.out.println( "Valor XXXXXXX: " + CfMethods.parseMoedaFormatada( txtTotal_AOA_liquido.getText() ) );
        lbValorPorExtenco.setText( MetodosUtil.valorPorExtenso( CfMethods.parseMoedaFormatada( txtTotal_AOA_liquido.getText() ), moeda.getDesignacao() ) );
    }

    public void remover_item_carrinho()
    {

        DefaultTableModel modelo = (DefaultTableModel) table_guia_transporte.getModel();
        modelo.removeRow( table_guia_transporte.getSelectedRow() );
        setTotalPagar();
        setTotalRetencao();
        calculaTotalIVA();
        //txtDesconto.setText("0");

        valor_por_extenco();
        reset_desconto_global();
        //calcularTroco();

    }

    public void adicionar_produto_teclado( int codigo, String descricao ) throws SQLException
    {

        DefaultTableModel modelo = (DefaultTableModel) table_guia_transporte.getModel();
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

    public static void salvar_item_venda_comercial( Integer cod_venda )
    {
        TbItemVenda itemVenda = null;

        List<TbItemVenda> lista = guia_transporte_global.getTbItemVendaList();
        boolean efectuada = true;
        for ( int i = 0; i < lista.size(); i++ )
        {
            try
            {
                TbProduto produto_local = (TbProduto) produtosController.findById( Integer.parseInt( String.valueOf( table_guia_transporte.getModel().getValueAt( i, 0 ) ) ) );
                double qtdDevolvida = Double.parseDouble( String.valueOf( table_guia_transporte.getModel().getValueAt( i, 11 ) ) );
                itemVenda = lista.get( i );
//                itemVenda.setCodigoProduto( produto_local );
//                itemVenda.setCodigoVenda( new TbVenda( cod_venda ) );
                itemVenda.setQuantidade( Double.parseDouble( String.valueOf( table_guia_transporte.getModel().getValueAt( i, 12 ) ) ) );
                itemVenda.setTotal( new BigDecimal( CfMethods.parseMoedaFormatada( String.valueOf( table_guia_transporte.getModel().getValueAt( i, 9 ) ) ) ) );
//                itemVenda.setDesconto( Double.parseDouble( String.valueOf( table_guia_transporte.getModel().getValueAt( i, 5 ) ) ) );
//                itemVenda.setValorIva( Double.parseDouble( String.valueOf( table_guia_transporte.getModel().getValueAt( i, 6 ) ) ) );
//                itemVenda.setValorRetencao( Double.parseDouble( String.valueOf( table_guia_transporte.getModel().getValueAt( i, 7 ) ) ) );
//                itemVenda.setMotivoIsensao( getMotivoIsensao( itemVenda.getCodigoProduto().getCodigo() ) );
//                itemVenda.setCodigoIsensao( MetodosUtil.getCodigoRegime( itemVenda.getCodigoProduto().getCodigo() ) );
//                itemVenda.setFkPreco( precosController.getLastIdPrecoByIdProduto( itemVenda.getCodigoProduto().getCodigo(), itemVenda.getQuantidade() ) );
//                itemVenda.setDataServico( new Date() );
//                /*setando a mesa e lugar para cunprir a formalidade só aplica-se somente para resstauração*/
//                itemVenda.setFkLugares( ( TbLugares ) lugaresController.findById( DVML.LUGAR_BALCAO ) );
//                itemVenda.setFkMesas( ( TbMesas ) mesasController.findById( DVML.MESA_BALCAO ) );

                //cria o item venda
//                int last_id_item_venda = itemVendaDao.criarComProcedimentos( itemVenda, conexao );
                if ( !itemVendasController.actualizar( itemVenda ) )
                {
                    DocumentosController.rollback( conexaoTransaction );
                    conexaoTransaction.close();
                    return;
                }

                boolean isStocavel = produto_local.getStocavel().equals( "true" );

                if ( isStocavel )
                {
                    stock_local = stocksController.getStockByIdProdutoAndIdArmazem( itemVenda.getCodigoProduto().getCodigo(), getCodigoArmazem() );
                }

                if ( getIdDocumento() == DOC_FACTURA_RECIBO_FR || getIdDocumento() == DOC_FACTURA_FT || getIdDocumento() == DVML.DOC_GUIA_TRANSPORTE_GT )
                {
                    System.out.println( "passei quando é FR ou FT" );
                    if ( !Objects.isNull( stock_local ) && isStocavel )
                    {
                        System.out.println( "chamei o actualizar quantidade" );
                        adicionar_quantidade( itemVenda.getCodigoProduto().getCodigo(), qtdDevolvida );
                    }

                }
            }
            catch ( Exception e )
            {
                e.printStackTrace();
                efectuada = false;
                JOptionPane.showMessageDialog( null, "Falha ao registrar o produto: " + itemVenda.getCodigoProduto().getCodigo() + " na Factura" );
                DocumentosController.rollback( conexaoTransaction );
                conexaoTransaction.close();
                return;
            }

        }

        if ( efectuada )
        {
            if ( getIdDocumento() == DOC_FACTURA_RECIBO_FR )
            {
                if ( !registrar_forma_pagamento( cod_venda ) )
                {
                    DocumentosController.rollback( conexaoTransaction );
                    conexaoTransaction.close();
                    JOptionPane.showMessageDialog( null, "Falha ao registrar a forma de pagamento.", "Falha", JOptionPane.WARNING_MESSAGE );
                    return;
                }
            }

            JOptionPane.showMessageDialog( null, "Factura efectuada com sucesso!.." );
            DocumentosController.commit( conexaoTransaction );

            List<TbProduto> lista_produto_isentos = new ArrayList<>();
            lista_produto_isentos = getProdutosIsentos();
            String motivos_isentos = MetodosUtil.getMotivoIsensaoProdutos( lista_produto_isentos );
            System.err.println( "MOTIVOS: " + motivos_isentos );
            try
            {

                limpar();
                remover_all_produto();
            }
            catch ( Exception e )
            {
            }

            txtQuatindade.setText( "1" );
//            txtIniciaisCliente.requestFocus();
            txtQuantidaStock.setText( String.valueOf( conexaoTransaction.getQtdExistenteStock( getCodigoProduto(), getCodigoArmazem() ) ) );
            conexaoTransaction.close();

            int numeroVias = (int) Double.parseDouble( spnCopia.getValue().toString() );
            for ( int i = 1; i <= numeroVias; i++ )
            {

                switch ( i )
                {
                    case 1:
                        ListaVenda1 original = new ListaVenda1( cod_venda, abreviacao, false, ck_simplificada.isSelected(), "Original", motivos_isentos );
                        break;
                    case 2:
                        ListaVenda1 original_duplicado = new ListaVenda1( cod_venda, abreviacao, false, ck_simplificada.isSelected(), "Duplicado", motivos_isentos );
                        break;
                    case 3:
                        ListaVenda1 original_triplicado = new ListaVenda1( cod_venda, abreviacao, false, ck_simplificada.isSelected(), "Triplicado", motivos_isentos );
                        break;
                }

            }

        }

    }

    public static boolean registrar_forma_pagamento( int id_venda )
    {

        DefaultTableModel modelo = (DefaultTableModel) FormaPagamentoVisao.tabela_forma_pagamento.getModel();

        FormaPagamentoItem formaPagamentoItem;

        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {

            formaPagamentoItem = new FormaPagamentoItem();
            Integer id_forma_pagamento = Integer.parseInt( modelo.getValueAt( i, 0 ).toString() );
            String referencia = ( modelo.getValueAt( i, 2 ) != null ) ? modelo.getValueAt( i, 2 ).toString() : "n/a";
//            String valor = ( modelo.getValueAt( i, 3 ) != null ) ? modelo.getValueAt( i, 3 ).toString() : "0";
            String valor = ( !modelo.getValueAt( i, 3 ).toString().equals( "" ) ) ? modelo.getValueAt( i, 3 ).toString() : "0";

            formaPagamentoItem.setValor( new BigDecimal( valor ) );
            formaPagamentoItem.setReferencia( referencia );
            formaPagamentoItem.setFkVenda( new TbVenda( id_venda ) );
            formaPagamentoItem.setFkFormaPagamento( new FormaPagamento( id_forma_pagamento ) );

            try
            {
                if ( !valor.equals( "0" ) )
                {
                    formaPagamentoItemController.salvar( formaPagamentoItem );
                }
            }
            catch ( Exception e )
            {
                return false;
            }
        }

        return true;
    }

    public void remover_items()
    {

        table_guia_transporte.getColumnModel().getColumn( 0 );
        table_guia_transporte.getColumnModel().getColumn( 1 );
        table_guia_transporte.getColumnModel().getColumn( 2 );
        table_guia_transporte.getColumnModel().getColumn( 3 );
        table_guia_transporte.getColumnModel().getColumn( 4 );
        table_guia_transporte.getColumnModel().getColumn( 5 );

        DefaultTableModel modelo = (DefaultTableModel) table_guia_transporte.getModel();

        for ( int i = modelo.getRowCount() - 1; i >= 0; i-- )
        {
            modelo.removeRow( i );
        }

    }

    public static void setTotalPagar()
    {

        DefaultTableModel modelo = (DefaultTableModel) table_guia_transporte.getModel();
        double total_liquido = getTotalAOALiquido();

        txtTotal_AOA_liquido.setText( CfMethods.formatarComoMoeda( total_liquido ) );
    }

    public static void setTotalRetencao()
    {

        DefaultTableModel modelo = (DefaultTableModel) table_guia_transporte.getModel();
        double total_retencao = getTotalRetencaoLiquido();
        txtTotal_AOA_Retencao.setText( CfMethods.formatarComoMoeda( total_retencao ) );
    }

    public static double getTotalPagar()
    {

        DefaultTableModel modelo = (DefaultTableModel) table_guia_transporte.getModel();

        double total_pagar = 0;
        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {
            total_pagar += CfMethods.parseMoedaFormatada( String.valueOf( modelo.getValueAt( i, 10 ) ) );

        }
        return total_pagar;

    }

    public static double getTotal_Retencao()
    {

        DefaultTableModel modelo = (DefaultTableModel) table_guia_transporte.getModel();

        double total_retencao = 0;
        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {
            total_retencao += CfMethods.parseMoedaFormatada( String.valueOf( modelo.getValueAt( i, 8 ) ) );

        }
        return total_retencao;

    }

    private static void calculaTotalIVA()
    {

        DefaultTableModel modelo = (DefaultTableModel) table_guia_transporte.getModel();
        double iva = 0, preco = 0, desconto = 0;
        double qtd = 0d;
        total_iva = 0;
        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {

            preco = CfMethods.parseMoedaFormatada( modelo.getValueAt( i, 3 ).toString() );
            qtd = Double.parseDouble( modelo.getValueAt( i, 4 ).toString() );
            //desconto = CfMethods.parseMoedaFormatada(modelo.getValueAt(i, 4).toString());
            iva = Double.parseDouble( modelo.getValueAt( i, 6 ).toString() );
            total_iva += getIVA( qtd, iva, preco, desconto );
        }

        // System.err.println("Com o método: " + MetodosUtil.valorCasasDecimaisNovo(total_iva, CASAS_DECIMAIS));
        total_iva = MetodosUtil.valorCasasDecimaisNovo( total_iva, CASAS_DECIMAIS );
        System.out.println( "(*)TOTAL IVA: " + total_iva );

    }

    private static void calculaTotalRet()
    {

        DefaultTableModel modelo = (DefaultTableModel) table_guia_transporte.getModel();
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

        table_guia_transporte.getColumnModel().getColumn( 0 );
        table_guia_transporte.getColumnModel().getColumn( 1 );
        table_guia_transporte.getColumnModel().getColumn( 2 );
        table_guia_transporte.getColumnModel().getColumn( 3 );
        table_guia_transporte.getColumnModel().getColumn( 4 );
        table_guia_transporte.getColumnModel().getColumn( 5 );

        DefaultTableModel modelo = (DefaultTableModel) table_guia_transporte.getModel();

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

    public static void remover_all_produto() throws SQLException
    {

        DefaultTableModel modelo = (DefaultTableModel) table_guia_transporte.getModel();
        for ( int i = modelo.getRowCount() - 1; i >= 0; i-- )
        {
            modelo.removeRow( i );
        }

    }

    public void remover_produto() throws SQLException
    {

        if ( linha > 0 )
        {

            table_guia_transporte.getModel().setValueAt( 0, linha - 1, 0 );
            table_guia_transporte.getModel().setValueAt( "", linha - 1, 1 );
            table_guia_transporte.getModel().setValueAt( 0, linha - 1, 2 );
            table_guia_transporte.getModel().setValueAt( 0, linha - 1, 3 );
            table_guia_transporte.getModel().setValueAt( 0, linha - 1, 4 );
            table_guia_transporte.getModel().setValueAt( 0, linha - 1, 5 );

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

    public static void adicionar_quantidade( int cod, double quantidade )
    {

        String sql = "UPDATE tb_stock SET quantidade_existente =  " + ( getQuantidadeProduto( cod ) + quantidade ) + " WHERE cod_produto_codigo = " + cod + " AND  cod_armazem = " + getCodigoArmazem();
        System.out.println( "Quantidade   : " + quantidade );
        conexao.executeUpdate( sql );

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
        conexaoTransaction = BDConexao.getInstancia();
        DocumentosController.start( conexaoTransaction );

        Date data_documento = dc_data_documento.getDate();
//        TbVenda venda_local = new TbVenda();
//        venda_local.setDataVenda( data_documento );
//        venda_local.setRefDataFact( data_documento );
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime( data_documento );
//        //adicionar 15 dias na data do documento.
//        calendar.add( Calendar.DATE, 15 );
//        venda_local.setDataVencimento( calendar.getTime() );
//        venda_local.setHora( data_documento );
//        venda_local.setNomeCliente( getNomeCliente() );
//        venda_local.setClienteNif( getClienteNif() );
//
//        //Total Ilíquido
//        venda_local.setTotalGeral( new BigDecimal( getTotalIliquido() ) );
//        //desconto por linha
//        venda_local.setDescontoComercial( new BigDecimal( getDescontoComercial() ) );
//        //imposto
//        //calculaTotalIVA();
//        venda_local.setTotalIva( new BigDecimal( getTotalImposto() ) );
//        //calculaTotalRetencao();
//        venda_local.setTotalRetencao( new BigDecimal( getTotalRetencaoLiquido() ) );
//        //desconto global
//        venda_local.setDescontoFinanceiro( new BigDecimal( getDescontoFinanceiro() ) );
//        //Total(AOA) <=> Total Líquido
//        venda_local.setTotalVenda( new BigDecimal( getTotalAOALiquido() ) );
//        venda_local.setValorEntregue( new BigDecimal( getValor_entregue() ) );
//        venda_local.setTroco( new BigDecimal( getTroco() ) );
//        venda_local.setTotalIncidencia( new BigDecimal( getTotalIncidencia() ) );
//        venda_local.setTotalIncidenciaIsento( new BigDecimal( getTotalIncidenciaIsento() ) );
//        /*outros campos*/
//        venda_local.setDescontoTotal( new BigDecimal( getDescontoComercial() + getDescontoFinanceiro() ) );
//        venda_local.setIdBanco( new TbBanco( 1 ) );
//        venda_local.setIdArmazemFK( new TbArmazem( getCodigoArmazem() ) );
//        venda_local.setCodigoUsuario( new TbUsuario( cod_usuario ) );
//        venda_local.setCodigoCliente( new TbCliente( getIdCliente() ) );
//        venda_local.setFkAnoEconomico( anoEconomico );
//        venda_local.setReferencia( txtReferencia.getText() );
//        venda_local.setNomeConsumidorFinal( getNomeCliente() );
//        venda_local.setFkDocumento( documento );
//        venda_local.setCodFact( prox_doc );
//        venda_local.setCont( 0 );
//        //this.prox_doc = !vendaDao.existe_codFact( prox_doc, conexao ) ? prox_doc : getCodDocActualizador();
////        venda_local.setCodFact( getCodDocActualizador() );
//
////        venda_local.setHashCod( MetodosUtil.criptografia_hash( prox_doc ) );
//        venda_local.setHashCod( MetodosUtil.criptografia_hash( venda_local, getGrossTotal(), conexaoTransaction ) );
//
//        venda_local.setTotalPorExtenso( iniciais_extenso() + lbValorPorExtenco.getText() );
//        venda_local.setMatricula( txtMatricula.getText() );
//        venda_local.setModelo( txtModelo.getText() );
//        venda_local.setNumMotor( txtMotor.getText() );
//        venda_local.setNumChassi( txtChassi.getText() );
//        venda_local.setKilometro( txtKilometragem.getText() );
//        venda_local.setObs( txtObs.getText() );
//
//        venda_local.setNomeMotorista( "" );
//        venda_local.setMarcaCarro( "" );
//        venda_local.setCorCarro( "" );
//        venda_local.setNDocMotorista( "" );
//        System.out.println( "STATUS:hash cod processado." );
//
//        // venda_local.setAssinatura( this.prox_doc );
//        venda_local.setAssinatura( MetodosUtil.assinatura_doc( venda_local.getHashCod() ) );
////        venda_local.setRefDataFact( CfMethods.fullDateToText( venda_local.getDataVenda() ) );
//
//        //System.out.println( "STATUS:documento assinado com sucesso." );
//        if ( getMoeda().getAbreviacao().equals( DVML.MOEDA_KWANZA ) )
//        {
//            int id = cambiosController.getLastId( getIdMoeda() );
//            venda_local.setFkCambio( new Cambio( id ) );
//        }
//        else
//        {
//            Cambio cambio_nacional = cambiosController.getLastObject( getIdMoeda() );
//            venda_local.setFkCambio( cambio_nacional );
//        }
//
//        /*status documento*/
//        venda_local.setStatusEliminado( "false" );
//        venda_local.setPerformance( "false" );
//        venda_local.setCredito( "false" );
//        venda_local.setGorjeta( new BigDecimal( gorjeta ) );

        guia_transporte_global.setTotalPorExtenso( iniciais_extenso() + lbValorPorExtenco.getText() );
        //Total Ilíquido
        guia_transporte_global.setTotalGeral( new BigDecimal( getTotalIliquido() ) );
        //desconto por linha
        guia_transporte_global.setDescontoComercial( new BigDecimal( getDescontoComercial() ) );
        guia_transporte_global.setDescontoFinanceiro( new BigDecimal( getDescontoFinanceiro() ) );
        guia_transporte_global.setTotalVenda( new BigDecimal( getTotalAOALiquido() ) );
        guia_transporte_global.setCodigoUsuario( new TbUsuario( cod_usuario ) );
        try
        {

            if ( vendasController.actualizar( guia_transporte_global ) )
            {
                Integer last_venda = guia_transporte_global.getCodigo();
                if ( Objects.isNull( last_venda ) || last_venda == 0 )
                {
                    DocumentosController.rollback( conexaoTransaction );
                    conexaoTransaction.close();
                    return;
                }
//                System.err.println( "last_venda: " + last_venda );
//                System.out.println( "STATUS:factura criada com sucesso." );

                if ( last_venda != null )
                {
//                    if ( getIdDocumento() == DOC_FACTURA_RECIBO_FR )
//                    {
//                        MetodosUtil.adicionar_saldo_banco( venda_local.getTotalVenda().doubleValue(), venda_local.getIdBanco().getIdBanco(), conexao );
//                    }
                    salvar_item_venda_comercial( last_venda );
                }
            }
            else
            {
                System.out.println( "ERROR: Já existe venda relacionada." );
            }

        }
        catch ( Exception e )
        {
            System.err.println( "STATUS: falha ao actualizar a factura" );
            e.printStackTrace();
            JOptionPane.showMessageDialog( null, "Falha ao Processar a Factura", "FALHA", JOptionPane.ERROR_MESSAGE );
            DocumentosController.rollback( conexao );
//            conexaoTransaction.close();
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
            TbCliente cliente = (TbCliente) clientesController.findById( getIdCliente() );
            return cliente.getNif();
        }
        catch ( Exception e )
        {
            return "";
        }
    }

    public static void main( String[] args ) throws SQLException
    {
        new AlteracaoGuiaTransporteVisao( 15, BDConexao.getInstancia() ).show( true );
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
//        txtIniciaisCliente.addKeyListener( new TratarEventoTeclado() );

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
//        txtCodClientePesquisa.setDocument( new PermitirNumeros() );
        //FILIPE TUZA - FORÇAR SELECIONAR UMA MOEDA PADRÃO
        cmbMoeda.setSelectedIndex( 0 );
        cmbCliente.setModel( new DefaultComboBoxModel( clientesController.getVector() ) );
        cmbCliente.setSelectedItem( DVML._CLIENTE_CONSUMIDOR_FINAL );

        cmbFamilia.setModel( new DefaultComboBoxModel( familiaController.getVector() ) );
        cmbTipoDocumento.setModel( new DefaultComboBoxModel( documentosController.getVector() ) );
        cmbAnoEconomico.setModel( new DefaultComboBoxModel( anoEconomicoController.getVector() ) );
        txtQuatindade.setText( "1" );
//        txtIniciaisCliente.requestFocus();
        dc_data_documento.setDate( new Date() );
        mostrar_ano_economico_serie();
        lb_proximo_documento.setText( "" );
        txtTotal_AOA_liquido.setText( CfMethods.formatarComoMoeda( 0.0 ) );

        reset_valor_entregue();
        reset_desconto_global();

//        setDocpadrao( dadosInstituicao.getDocpadrao() );
        setDesactivarvias( dadosInstituicao.getDesactivarvias() );
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
//                    prefixo = txtIniciaisCliente.getText().trim() + key;
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

    private void mostrar_proximo_codigo_documento()
    {
        try
        {
            this.documento = (Documento) documentosController.findById( getIdDocumento() );
            this.anoEconomico = (AnoEconomico) anoEconomicoController.findById( getIdAnoEconomico() );
            this.doc_prox_cod = vendasController.getUltimaContagemByIdDocumentoAndAnoEconomico(
                    getIdDocumento(), getIdAnoEconomico() ) + 1;
            prox_doc = documento.getAbreviacao();
            //FA Série / codigo
            prox_doc += " " + this.anoEconomico.getSerie() + "/" + this.doc_prox_cod;
            lb_proximo_documento.setText( "PRÓX.DOC. :" + prox_doc );
        }
        catch ( Exception e )
        {
            this.documento = null;
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
        double subtotal_linha = ( preco_venda * qtd );
        double desconto_valor = ( subtotal_linha * ( desconto / 100 ) );
        double valor_iva = 1 + ( taxa / 100 );//
        return ( ( subtotal_linha - desconto_valor ) * valor_iva );

    }

    private static double getValorIliquido( double qtd, double preco_venda, double desconto )
    {
        double subtotal_linha = ( preco_venda * qtd );
        double desconto_valor = ( subtotal_linha * ( desconto / 100 ) );
        return ( ( subtotal_linha - desconto_valor ) );

    }

    private static double getTotalRecepcionado( double qtd_normal, double qtd_devolvida )
    {
        double subtotal_linha = ( qtd_normal - qtd_devolvida );
        return subtotal_linha;

    }

    private static double getValorComImpostoIva( double qtd, double taxa, double preco_venda, double desconto )
    {
        double subtotal_linha = ( preco_venda * qtd );
        double desconto_valor = ( subtotal_linha * ( desconto / 100 ) );
        double valor_iva = 1 + ( taxa / 100 );//
        return ( ( subtotal_linha - desconto_valor ) * valor_iva );

    }

    private static double getValorComRetencao( double qtd, double ret, double preco_venda, double desconto )
    {
        double subtotal_linha = ( preco_venda * qtd );
        double desconto_valor = ( subtotal_linha * ( desconto / 100 ) );
        double valor_ret = ( ( ( subtotal_linha - desconto_valor ) * ret ) / 100 );//
        return ( valor_ret );

    }

    private static double getIVA( double qtd, double taxa, double preco_venda, double desconto )
    {
        double subtotal_linha = ( preco_venda * qtd );
        double valor_iva = ( taxa / 100 );//
        return ( ( subtotal_linha - desconto ) * valor_iva );

    }

    private static double getRET( double qtd, double taxa_r, double preco_venda, double desconto )
    {
        double subtotal_linha = ( preco_venda * qtd );
        double valor_ret = ( taxa_r / 100 );//
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
            }

        } );

    }

    private void actualizar_abreviacao()
    {

        switch ( getIdDocumento() )
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
//        btnFormaPagamento.setVisible( !documentoIsFA && !documentoIsPP && !documentoIsGT);
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

        DefaultTableModel modelo = (DefaultTableModel) table_guia_transporte.getModel();

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
        DefaultTableModel modelo = (DefaultTableModel) table_guia_transporte.getModel();

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

        DefaultTableModel modelo = (DefaultTableModel) table_guia_transporte.getModel();

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
        DefaultTableModel modelo = (DefaultTableModel) table_guia_transporte.getModel();
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
        DefaultTableModel modelo = (DefaultTableModel) table_guia_transporte.getModel();
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
                double valor_unitario = ( preco_unitario * qtd );
                incidencia += ( ( valor_unitario ) - ( valor_unitario * desconto_valor_linha ) );

            }

        }

        return incidencia;
    }

    private static double getTotalImposto()
    {
        DefaultTableModel modelo = (DefaultTableModel) table_guia_transporte.getModel();
        double qtd = 0d;
        double imposto = 0d, preco_unitario = 0d, desconto_valor_linha = 0d;

        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {
            preco_unitario = CfMethods.parseMoedaFormatada( modelo.getValueAt( i, 3 ).toString() );
            qtd = Double.parseDouble( modelo.getValueAt( i, 12 ).toString() );
            double valor_percentagem = Double.parseDouble( modelo.getValueAt( i, 5 ).toString() );
            double taxa = Double.parseDouble( modelo.getValueAt( i, 6 ).toString() );
            // a incidência só é aplicável ao produtos sujeitos a iva 
            if ( taxa != 0 )
            {
                double valor_unitario = ( preco_unitario * qtd );
                desconto_valor_linha = valor_unitario * ( ( valor_percentagem ) / 100 );
                imposto += ( ( valor_unitario - desconto_valor_linha ) * ( taxa / 100 ) );

            }

        }

        return imposto;
    }

    private static double getTotalRetencao()
    {
        DefaultTableModel modelo = (DefaultTableModel) table_guia_transporte.getModel();
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
        DefaultTableModel modelo = (DefaultTableModel) table_guia_transporte.getModel();
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
                double valor_unitario = ( preco_unitario * qtd );

                desconto_valor_linha = valor_unitario * ( ( valor_percentagem ) / 100 );
                valor_taxa = ( valor_unitario - desconto_valor_linha ) / taxa;
//                imposto += ( ( valor_unitario - desconto_valor_linha ) * ( taxa / 100 ) );

            }

        }

        return valor_taxa;
    }

    private static double getTotalIncidenciaIsento()
    {
        DefaultTableModel modelo = (DefaultTableModel) table_guia_transporte.getModel();
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
                double valor_unitario = ( preco_unitario * qtd );
                incidencia_isento += ( ( valor_unitario ) - ( valor_unitario * desconto_valor_linha ) );

            }

        }

        return incidencia_isento;
    }

    private static double getDescontoComercial()
    {
        DefaultTableModel modelo = (DefaultTableModel) table_guia_transporte.getModel();
        double qtd = 0d;
        double desconto_comercial = 0d, preco_unitario = 0d, desconto_valor_linha = 0d;

        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {
            preco_unitario = CfMethods.parseMoedaFormatada( modelo.getValueAt( i, 3 ).toString() );
            qtd = Double.parseDouble( modelo.getValueAt( i, 12 ).toString() );
            double valor_percentagem = Double.parseDouble( modelo.getValueAt( i, 5 ).toString() );
            desconto_valor_linha = ( ( valor_percentagem ) / 100 );
            double valor_unitario = ( preco_unitario * qtd );
            desconto_comercial += ( valor_unitario * desconto_valor_linha );

        }

        return desconto_comercial;
    }

    private static double getTotalIliquido()
    {

        DefaultTableModel modelo = (DefaultTableModel) table_guia_transporte.getModel();
        double qtd = 0d;
        double total_iliquido = 0d, preco_unitario = 0d;

        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {

            preco_unitario = CfMethods.parseMoedaFormatada( modelo.getValueAt( i, 3 ).toString() );
            qtd = Double.parseDouble( modelo.getValueAt( i, 12 ).toString() );
            total_iliquido += ( preco_unitario * qtd );

        }

        return total_iliquido;

    }

    private static double getTotalRetencaoLiquido()
    {

        DefaultTableModel modelo = (DefaultTableModel) table_guia_transporte.getModel();
        double total_retencao = 0d, valor_retencao = 0d;

        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {

            valor_retencao = CfMethods.parseMoedaFormatada( modelo.getValueAt( i, 8 ).toString() );
            total_retencao += valor_retencao;

        }

        return total_retencao;

    }

    private static double getDescontoFinanceiro()
    {
        double desconto_economico = 0d;
        desconto_economico = Double.parseDouble( sp_desconto_financeiro.getValue().toString() );
        return desconto_economico;
    }

    private static double getTotalAOALiquido()
    {
//        double valores = ( getTotalIliquido() + getTotalImposto() + getTotalRetencao() );
        double valores = ( getTotalIliquido() + getTotalImposto() );
        double descontos = ( getDescontoComercial() + getDescontoFinanceiro() );
        System.out.println( "TotalIliquido: " + getTotalIliquido() );
        System.out.println( "TotalImposto: " + getTotalImposto() );
        System.out.println( "TotalDescontoComercial: " + getDescontoComercial() );
        System.out.println( "TotalDescontoFinanceiro: " + getDescontoFinanceiro() );
        System.out.println( "Total Liquido: " + ( valores - descontos ) );
        return ( valores - descontos );
    }

    private static double getTotalAOARetencoes()
    {
        double valores = ( getTotalRetencao1() );
        return ( valores );
    }

    private static double getTotalVendaIVASemIncluirDesconto()
    {
        double taxa = 0, total_iva_local = 0, preco_unitario = 0, sub_total_iliquido = 0;
        double qtd = 0d;

        DefaultTableModel modelo = (DefaultTableModel) table_guia_transporte.getModel();
        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {
            preco_unitario = CfMethods.parseMoedaFormatada( modelo.getValueAt( i, 3 ).toString() );
            qtd = Double.parseDouble( modelo.getValueAt( i, 4 ).toString() );
            sub_total_iliquido = ( preco_unitario * qtd );
            taxa = Double.parseDouble( modelo.getValueAt( i, 6 ).toString() );
            total_iva_local += ( ( ( sub_total_iliquido ) * ( taxa / 100 ) ) );
        }

        return total_iva_local;
    }

    private static double getGrossTotal()
    {
        System.out.println( "TOTALILIQUIDO: " + getTotalVendaIVASemIncluirDesconto() );
        System.out.println( "TOTALVENDAIVASEMDESCONTO: " + getTotalVendaIVASemIncluirDesconto() );
        return getTotalIliquido() + getTotalVendaIVASemIncluirDesconto();
    }

    private static String iniciais_extenso()
    {
        Documento documento_local = (Documento) documentosController.findById( getIdDocumento() );
        String abreviacao_local = documento_local.getAbreviacao();

        switch ( abreviacao_local )
        {
            case "FT":
                return "Facturamos o valor de: ";
            case "FR":
                return "Recebemos a quantia de: ";
            default:
                return "São: ";
        }
    }

//    private void pesquisa_cliente_by_cod()
//    {
//
//        Integer codCliente = Integer.parseInt( txtCodClientePesquisa.getText() );
//        try
//        {
//
//            TbCliente cliente = ( TbCliente ) clientesController.findById( codCliente );
//            String nome_cliente = cliente.getNome();
//            cmbCliente.setSelectedItem( nome_cliente.trim() );
//            accao_cliente();
//        }
//        catch ( Exception e )
//        {
//            e.printStackTrace();
//            JOptionPane.showMessageDialog( null, "Não existe cliente com código" );
//            cmbCliente.setSelectedItem( "Consumidor Final" );
//        }
//        txtCodClientePesquisa.setText( "" );
//        txtCodClientePesquisa.requestFocus();
//    }
////    5417221915
//
//    private void pesquisa_cliente_by_nif()
//    {
//
//        String nif = txtNifClientePesquisa.getText();
//        try
//        {
//            String nome_cliente = clientesController.getClienteByNifOrberByNome( nif ).getNome();
//            cmbCliente.setSelectedItem( nome_cliente.trim() );
//            accao_cliente();
//        }
//        catch ( Exception e )
//        {
//            e.printStackTrace();
//            JOptionPane.showMessageDialog( null, "Não existe cliente com código" );
//            cmbCliente.setSelectedItem( "Consumidor Final" );
//        }
//        txtNifClientePesquisa.setText( "" );
//        txtNifClientePesquisa.requestFocus();
//    }
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
            this.abreviacao = Abreviacao.FR_A6;
        }
        else if ( folha.equalsIgnoreCase( "A7" ) )
        {
            ck_A7.setSelected( true );
            ck_simplificada.setSelected( false );
            ck_A4.setSelected( false );
            ck_Duplicada.setSelected( false );
            ck_S_A6.setSelected( false );
            ck_ComVirgula.setSelected( false );
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
            txtQuantidaStock.setVisible( true );
            lbQuantidadeStock.setVisible( true );

        }
        else
        {
            txtQuantidaStock.setVisible( false );
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

//    private void setDocpadrao( String documentos )
//    {
//        System.out.println( "DOCUMENTO PADRAO: " + documentos );
//        if ( documentos.equalsIgnoreCase( "Factura/Recibo" ) )
//        {
//            cmbTipoDocumento.setSelectedIndex( 1 );
//
//        }
//        else if ( documentos.equalsIgnoreCase( "Factura" ) )
//        {
//            cmbTipoDocumento.setSelectedIndex( 2 );
//
//        }
//        else if ( documentos.equalsIgnoreCase( "Factura-Proforma" ) )
//        {
//            cmbTipoDocumento.setSelectedIndex( 3 );
//
//        }
//        else if ( documentos.equalsIgnoreCase( "Guia de Transporte" ) )
//        {
//            cmbTipoDocumento.setSelectedIndex( 4 );
//
//        }
//    }
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

        table_guia_transporte.scrollRectToVisible( table_guia_transporte.getCellRect( table_guia_transporte.getRowCount() - 1, table_guia_transporte.getColumnCount(), true ) );

    }

    private void setActivarNegocio( String negocio )
    {
        if ( negocio.equalsIgnoreCase( "Comercial" ) )
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
        setArmazem( dadosInstituicao.getConfigArmazens() );
        try
        {
            if ( !rbArmazem.isSelected() )
            {
                //                Caso for MultiArmazens
                cmbArmazem.setModel( new DefaultComboBoxModel( armazensController.getVector2() ) );
            }
            else if ( rbArmazem.isSelected() )
            {
                //                Caso for apenas Um Armazem
                cmbArmazem.setModel( new DefaultComboBoxModel( armazensAccessoController.getAllArmazemExceptoEconomatoByIdUSuario( cod_usuario ) ) );
            }
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }

//    public boolean validar()
//    {
//        if ( cmbCliente.getSelectedItem().equals( "--Seleccione o Cliente--" ) )
//        {
//
//            JOptionPane.showMessageDialog( null, "Por favor, Seleccione ou Digite o nome do Cliente!" );
//            txtIniciaisCliente.requestFocus();
//            txtIniciaisCliente.setBackground( Color.YELLOW );
//            return false;
//
//        }
//
//        txtIniciaisCliente.setBackground( Color.WHITE );
//        return true;
//    }
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

        linha_actual = table_guia_transporte.getSelectedRow();

        if ( linha_actual > -1 )
        {

            int codProduto = Integer.parseInt( table_guia_transporte.getValueAt( linha_actual, 0 ).toString() );
            double desconto = Double.parseDouble( table_guia_transporte.getValueAt( linha_actual, 5 ).toString() );

            TbProduto produtoLocal = (TbProduto) produtosController.findById( codProduto );
            TbTipoProduto tipoProduto = (TbTipoProduto) tipoProdutoController.findById( produtoLocal.getCodTipoProduto().getCodigo() );

            double qtd;

            try
            {
                qtd = Double.parseDouble( table_guia_transporte.getValueAt( linha_actual, 4 ).toString() );
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

    //actualiza a quantidade na tabela do formulário visão isto é na jTable
    private static void actuazlizar_quantidade_tabela_formulario( String quantidade, double desconto )
    {
        DefaultTableModel modelo = (DefaultTableModel) table_guia_transporte.getModel();
        double qtd = Double.parseDouble( quantidade );
        double preco_venda = CfMethods.parseMoedaFormatada( String.valueOf( modelo.getValueAt( linha_actual, 3 ) ) );
        double taxa = Double.parseDouble( String.valueOf( modelo.getValueAt( linha_actual, 6 ) ) );

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

        modelo.setValueAt( qtd, linha_actual, 4 );
        modelo.setValueAt( desconto, linha_actual, 5 );
//
        modelo.setValueAt( total_iliquido_linha, linha_actual, 9 );
        modelo.setValueAt( total_liquido_linha, linha_actual, 10 );
        //a linha_actual recebe o default
        linha_actual = -1;

    }

    private void procedimento_busca()
    {
        /**
         * @1. inserir a referência;
         * @1.1 Se o campo estiver vazio emitir a mensagem: "por favor insira a
         * refrência da pró-forma"
         * @2. buscar a venda relacionada com esta referência desde que o
         * documento seja do tipo pro-forma;
         * @2.1 Senão existe emitir uma mensagem: "Não existe pró-forma com esta
         * referência"
         * @3. percorrer e preencher os campos do formulário;
         * @4. setar o total
         * @5. setar cliente
         */

        //@1. Inserir a referência
        String ref_doc = txtRefDoc.getText();
        if ( !ref_doc.equals( "" ) )
        {
            //@2. buscar a venda relacionada com esta referência desde que o documento seja do tipo pro-forma;
            guia_transporte_global = vendaDao.findByCodFact( ref_doc, DVML.DOC_GUIA_TRANSPORTE_GT );
//            guia_transporte_global = vendasController.findByCodFact(ref_doc );
            if ( guia_transporte_global != null )
            {

                /*@3. percorrer e preencher os campos do formulário;*/
                //3.1 preeenche o tipo documento
                cmbTipoDocumento.setSelectedItem( guia_transporte_global.getFkDocumento().getDesignacao() );

                //3.2 preenche o armazém
                cmbArmazem.setSelectedItem( guia_transporte_global.getIdArmazemFK().getDesignacao() );

                //3.3 preenche a moeda
                cmbMoeda.setSelectedItem( guia_transporte_global.getFkCambio().getFkMoeda().getDesignacao() );

                //@5.setar o cliente
//                txtClienteNome.setText(guia_transporte_global.getNomeCliente() );
                cmbCliente.setSelectedItem( guia_transporte_global.getCodigoCliente().getNome() );

                //3.4 preencher a tabela com os itens
                List<TbItemVenda> linhas = guia_transporte_global.getTbItemVendaList();
                DefaultTableModel modelo = (DefaultTableModel) table_guia_transporte.getModel();
                //3.4.1 limpa a tabela
                modelo.setRowCount( 0 );
                for ( TbItemVenda object : linhas )
                {
                    modelo.addRow( new Object[]
                    {
                        object.getCodigoProduto().getCodigo(),
                        object.getCodigoProduto().getDesignacao(),
                        object.getCodigoProduto().getCodUnidade().getDescricao(),
                        CfMethods.formatarComoMoeda( object.getFkPreco().getPrecoVenda() ),
                        object.getQuantidade(),
                        //desconto é dados em percentagem
                        object.getDesconto(),
                        object.getValorIva(),
                        0,
                        0,
                        CfMethods.formatarComoMoeda(
                        object.getFkPreco().getPrecoVenda().doubleValue()
                        * object.getQuantidade()
                        - object.getDesconto() ),
                        CfMethods.formatarComoMoeda( object.getTotal() ),
                        0,
                        object.getQuantidade()

                    } );
                }

                //@4.setar o total
                txtTotal_AOA_liquido.setText( CfMethods.formatarComoMoeda( guia_transporte_global.getTotalVenda() ) );
//                sp_valor_entregue.setValue(guia_transporte_global.getTotalVenda() );
                //setTotalPagar();
                lbValorPorExtenco.setText( "São: " + MetodosUtil.valorPorExtenso( CfMethods.parseMoedaFormatada( txtTotal_AOA_liquido.getText() ), getMoeda().getDesignacao() ) );

//                  atualizarResumo( null);
            }
            else
            {
//                procedimento_limpar_dados();
                guia_transporte_global = null;
                JOptionPane.showMessageDialog( null, "Não existe pró-forma com esta referência", "AVISO", JOptionPane.WARNING_MESSAGE );
            }

        }
        else
        {
            guia_transporte_global = null;
            JOptionPane.showMessageDialog( null, "por favor insira a refrência da pró-forma", "AVISO", JOptionPane.WARNING_MESSAGE );
        }

    }

    private void actualizarQtdTableDevolvida()
    {

        linha_actual = table_guia_transporte.getSelectedRow();

        if ( linha_actual > -1 )
        {

            int codProduto = Integer.parseInt( table_guia_transporte.getValueAt( linha_actual, 0 ).toString() );
            double desconto = Double.parseDouble( table_guia_transporte.getValueAt( linha_actual, 5 ).toString() );

            TbProduto produtoLocal = (TbProduto) produtosController.findById( codProduto );
            TbTipoProduto tipoProduto = (TbTipoProduto) tipoProdutoController.findById( produtoLocal.getCodTipoProduto().getCodigo() );

            double qtd;

            try
            {
                qtd = Double.parseDouble( table_guia_transporte.getValueAt( linha_actual, 4 ).toString() );
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

            double qtd_devolvida;

            try
            {
                qtd_devolvida = Double.parseDouble( table_guia_transporte.getValueAt( linha_actual, 11 ).toString() );
            }
            catch ( NumberFormatException e )
            {
                resetValue( "Erro de formatação da quantidade.\nAtenção: Tem que ser número.", 11 );
                return;
            }

            if ( qtd_devolvida <= 0 )
            {
                qtd_devolvida = 1;
                resetValue( "Quantidade não pode ser zero(0) ou número négativo", 11 );
            }

            if ( possivel_quantidade( codProduto, qtd_devolvida ) || tipoProduto.getFkFamilia().getPkFamilia() == DVML.COD_SERVICO )
            {
//                actuazlizar_quantidade_tabela_formulario( String.valueOf( qtd ), desconto );
//                actuazlizar_quantidade_tabela_formulario_devolvida( String.valueOf( qtd_devolvida ) );
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

//    private static void actuazlizar_quantidade_tabela_formulario1( String quantidade )
//    {
//        DefaultTableModel modelo = ( DefaultTableModel ) table_guia_transporte.getModel();
//        double qtd_normal = Double.parseDouble( quantidade );
//        double qtd_devolvida = Double.parseDouble( String.valueOf( modelo.getValueAt( linha_actual, 11 ) ) );
////        double preco_venda = CfMethods.parseMoedaFormatada( String.valueOf( modelo.getValueAt( linha_actual, 3 ) ) );
////        double taxa = Double.parseDouble( String.valueOf( modelo.getValueAt( linha_actual, 6 ) ) );
//
////        double total_iliquido_linha = (( preco_venda * qtd ) - desconto);
//        double total_recepcionado = getTotalRecepcionado(
//                qtd_normal,
//                qtd_devolvida
//        );
////        String total_iliquido_linha = CfMethods.formatarComoMoeda( getValorIliquido(
////                qtd,
////                preco_venda,
////                desconto
////        ) );
//
////        String total_liquido_linha = CfMethods.formatarComoMoeda( getValorComImpostoIva(
////                qtd,
////                taxa,
////                preco_venda,
////                desconto
////        ) );
//        modelo.setValueAt( qtd_devolvida, linha_actual, 11 );
////        modelo.setValueAt( desconto, linha_actual, 5 );
////
////        modelo.setValueAt( total_iliquido_linha, linha_actual, 9 );
////        modelo.setValueAt( total_liquido_linha, linha_actual, 10 );
//        modelo.setValueAt( total_recepcionado, linha_actual, 12 );
//        //a linha_actual recebe o default
//        linha_actual = -1;
//
//    }
    private static TbItemVenda getItemByIdProduto( int fkProduto )
    {
        List<TbItemVenda> lista = guia_transporte_global.getTbItemVendaList();

        for ( int i = 0; i < lista.size(); i++ )
        {
            TbItemVenda get = lista.get( i );

            if ( get.getCodigoProduto().getCodigo() == fkProduto )
            {
                return get;
            }

        }

        return null;

    }

    private static void atualizarResumo( PropertyChangeEvent evt )
    {
        DefaultTableModel modelo = (DefaultTableModel) table_guia_transporte.getModel();

        if ( modelo.getRowCount() > 0 )
        {
            linha_actual = table_guia_transporte.getSelectedRow();

            int codProduto = Integer.parseInt( table_guia_transporte.getValueAt( linha_actual, 0 ).toString() );

            TbItemVenda itemVenda = getItemByIdProduto( codProduto );
            TbPreco precoLocal = itemVenda.getFkPreco();

            double qtd_devolvida = 0;
            double qtd_normal;
            double qtd_recepcionada;
            double desconto = 0;
            qtd_normal = Double.parseDouble( String.valueOf( modelo.getValueAt( linha_actual, 4 ) ) );
            qtd_devolvida = Double.parseDouble( String.valueOf( modelo.getValueAt( linha_actual, 11 ) ) );
            qtd_recepcionada = qtd_normal - qtd_devolvida;

            String totalIliquido = CfMethods.formatarComoMoeda( ( precoLocal.getPrecoVenda().doubleValue() * qtd_recepcionada ) - desconto );

            String totalLiquido = CfMethods.formatarComoMoeda(
                    getValorComImpostoIva(
                            qtd_recepcionada,
                            itemVenda.getValorIva(),
                            precoLocal.getPrecoVenda().doubleValue(),
                            desconto
                    ) );

            modelo.setValueAt( qtd_normal, linha_actual, 4 );
            modelo.setValueAt( totalIliquido, linha_actual, 9 );
            modelo.setValueAt( totalLiquido, linha_actual, 10 );
            modelo.setValueAt( qtd_recepcionada, linha_actual, 12 );

            txtTotal_AOA_liquido.setText( CfMethods.formatarComoMoeda( getTotalActualizado() ) );
            lbValorPorExtenco.setText( "São: " + MetodosUtil.valorPorExtenso( CfMethods.parseMoedaFormatada( txtTotal_AOA_liquido.getText() ), getMoeda().getDesignacao() ) );

        }

    }

    private void resetValue( String msg, int columnValue )
    {
        System.out.println( "Cheguei aqui..." );
        table_guia_transporte.setValueAt( 1, linha_actual, columnValue );
        JOptionPane.showMessageDialog( null, msg );
        table_guia_transporte.clearSelection();
    }

    private static double getTotalActualizado()
    {

        double total = 0;
        DefaultTableModel modelo = (DefaultTableModel) table_guia_transporte.getModel();

        if ( modelo.getRowCount() > 0 )
        {
            for ( int i = 0; i < modelo.getRowCount(); i++ )
            {
                total += CfMethods.parseMoedaFormatada( modelo.getValueAt( i, 10 ).toString() );
            }
        }
        return total;
    }

}
