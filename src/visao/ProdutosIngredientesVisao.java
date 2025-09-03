/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package visao;

import controller.ProdutoController;
import comercial.controller.*;
import dao.DocumentoDao;
import dao.ItemPermissaoDao;
import entity.*;
import exemplos.PermitirNumeros;
import java.awt.Color;
import java.awt.Frame;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.swing.*;
import static kitanda.util.CfConstantes.YYYYMMDD_HHMMSS;
import kitanda.util.CfMethods;
import util.BDConexao;
import util.DVML;
import util.FinanceUtils;
import util.JPAEntityMannagerFactoryUtil;
import util.MetodosUtil;
import static util.MetodosUtil.rodarComandoWindows;
import util.NumeroDocument;
import util.PictureChooser;

public class ProdutosIngredientesVisao extends javax.swing.JFrame
{

    /**
     * CONTROLLER COMERCIAL
     */
    private EntityManagerFactory emf = JPAEntityMannagerFactoryUtil.em;
    private ItemPermissaoDao itemPermissaoDao = new ItemPermissaoDao( emf );
    private static ProdutosController produtosController;
    private static ModelosController modelosController;
    private static MarcasController marcasController;
    private static GruposController gruposController;
    private static FamiliasController familiasController;
    private static TipoProdutosController tipoProdutosController;
    private static LocalController localController;
    private static UnidadesController unidadesController;
    private static PrecosController precosController;
    private static ArmazensController armazensController;
    private static ProdutosMotivosIsensaoController produtosMotivosIsensaoController;
    private static ProdutosIsentoController produtosIsentoController;
    private static ProdutosImpostoController produtosImpostoController;
    private static ImpostosController impostosController;
    private static ServicosRetencaoController servicosRetencaoController;
    private static RetencaoController retencaoController;
    private static StoksController stoksController;
    private static UsuariosController usuariosController;
    private static DadosInstituicaoController dadosInstituicaoController;

    /**
     * ENTIDADES
     */
    private static TbProduto produto;
    private static Retencao retencao;
    private static TbStock stock;
    public static TbDadosInstituicao dadosInstituicao;
    private static TbPreco preco;

    /**
     * UTILITARIOS
     */
    private PictureChooser image_View = new PictureChooser();
    private static BDConexao conexao, conexaoTransaction;
    private Frame parent;
    private static ImageIcon imageIcon;

    /**
     * OUTROS
     */
    private static int codigo = 0, idUser = 0;
    private boolean stocavel = true;
    private double preco_venda_anterior = 0;

    public ProdutosIngredientesVisao( java.awt.Frame parent, boolean modal, int idUSer, BDConexao conexao )
    {

        this.parent = parent;
        initComponents();
        rbJanelaServico.setVisible( false );

        /**
         * INSTANCIAS DOS CONTROLLER
         */
        produtosController = new ProdutosController( conexao );
        modelosController = new ModelosController( conexao );
        marcasController = new MarcasController( conexao );
        gruposController = new GruposController( conexao );
        familiasController = new FamiliasController( conexao );
        tipoProdutosController = new TipoProdutosController( conexao );
        localController = new LocalController( conexao );
        unidadesController = new UnidadesController( conexao );
        precosController = new PrecosController( conexao );
        armazensController = new ArmazensController( conexao );
        produtosIsentoController = new ProdutosIsentoController( conexao );
        produtosMotivosIsensaoController = new ProdutosMotivosIsensaoController( conexao );
        produtosImpostoController = new ProdutosImpostoController( conexao );
        servicosRetencaoController = new ServicosRetencaoController( conexao );
        impostosController = new ImpostosController( conexao );
        retencaoController = new RetencaoController( conexao );
        usuariosController = new UsuariosController( conexao );
        stoksController = new StoksController( conexao );
        dadosInstituicaoController = new DadosInstituicaoController( conexao );

        dadosInstituicao = (TbDadosInstituicao) dadosInstituicaoController.findById( 1 );

        confiLabel();
        setWindowsListener();

        setLocationRelativeTo( null );
        ProdutosIngredientesVisao.idUser = idUSer;

        set_combos();

        setRegime( dadosInstituicao.getRegime() );

        txtPrecoDeVendaComIva.setVisible( false );
        TotalIvaLabel.setVisible( false );
        cmbImposto.setVisible( false );
//        TotalIvaLabel.setVisible( false );
//        txtPrecoDeVendaComIva.setVisible( false );

        this.conexao = conexao;
        jPanel_retencao.setVisible( true );
        ck_produto.setSelected( true );
//        servico_produto();
        actualizarRetencaoForm();
        proximo_codigo( produtosController );
        busca_permissao();
        txtCodigoProduto.setDocument( new PermitirNumeros() );
        jcDataFabrico.setDate( new Date() );
        jcDataExpiracao.setDate( new Date() );
        txtPrecoCompra.addKeyListener( new PrecoCompraPercentagem() );
        txtPrecoVendaRetalho.addKeyListener( new PrecoVendaPercentagem() );
        setFocus( dadosInstituicao.getFoco() );
        txtPrecoCompra.setDocument( new NumeroDocument( 12, DVML.CASAS_DECIMAIS ) );
        txtPrecoCompra.setHorizontalAlignment( JTextField.RIGHT );
        txtPrecoVendaRetalho.setDocument( new NumeroDocument( 12, DVML.CASAS_DECIMAIS ) );
        txtPrecoVendaRetalho.setHorizontalAlignment( JTextField.RIGHT );

        txtPercentagemGanhoRetalho.setDocument( new NumeroDocument( 12, DVML.CASAS_DECIMAIS ) );
        txtPercentagemGanhoRetalho.setHorizontalAlignment( JTextField.RIGHT );

        txtPrecoVendaGrosso.setDocument( new NumeroDocument( 12, DVML.CASAS_DECIMAIS ) );
        txtPrecoVendaGrosso.setHorizontalAlignment( JTextField.RIGHT );
        popularComponentes();
        configurar_dois_precos();

        KeyboardFocusManager.getCurrentKeyboardFocusManager()
                .addKeyEventDispatcher( new KeyEventDispatcher()
                {
                    @Override
                    public boolean dispatchKeyEvent( KeyEvent e )
                    {
                        if ( e.getID() == e.KEY_RELEASED && e.getKeyCode() == KeyEvent.VK_TAB )
                        {

                            txtPrecoVendaRetalho.requestFocus();
                            return true;

                        }
                        return false;
                    }
                } );

        try
        {

            setActivarNegocio( dadosInstituicao.getNegocio() );
            setActivarJanelaServico( dadosInstituicao.getJanelaServico() );

        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }

        activarCampoPreco();

    }



    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     *
     */
    @SuppressWarnings( "unchecked" )

    public void busca_permissao()
    {

        try
        {
            // TODO add your handling code here
            setStatusUsuario( (Vector) itemPermissaoDao.getAllPermissoesByIdUsuarioAndModulo( idUser, DVML.MODULO_GESTAO_COMERCIAL ) );
        }
        catch ( Exception ex )
        {
            ex.printStackTrace();
        }
    }

    public void setStatusUsuario( Vector<TbItemPermissao> vector )
    {

        String permissao = "";
        //limpa
        setPermissoes( false );

        for ( int i = 0; i < vector.size(); i++ )
        {

            permissao = vector.get( i ).getIdPermissao().getDescricao();
            if ( permissao.equals( jButtonCompras.getText() ) )
            {
                jButtonCompras.setVisible( true );
            }

        }

    }

    public void setPermissoes( boolean status )
    {
        jButtonCompras.setVisible( status );
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        buttonGroup3 = new javax.swing.ButtonGroup();
        buttonGroup4 = new javax.swing.ButtonGroup();
        buttonGroup5 = new javax.swing.ButtonGroup();
        jPanel5 = new javax.swing.JPanel();
        lbTipoProduto = new javax.swing.JLabel();
        cmbTipoProduto = new javax.swing.JComboBox();
        lbProduto = new javax.swing.JLabel();
        lbCusto = new javax.swing.JLabel();
        txtPrecoCompra = new javax.swing.JTextField();
        ck_produto = new javax.swing.JCheckBox();
        cmbUnidade = new javax.swing.JComboBox<>();
        ck_servico = new javax.swing.JCheckBox();
        cmbFamilia = new javax.swing.JComboBox<>();
        lbTipoProduto2 = new javax.swing.JLabel();
        cmbMarca = new javax.swing.JComboBox<>();
        lbTipoProduto3 = new javax.swing.JLabel();
        cmbModelo = new javax.swing.JComboBox();
        lbTipoProduto4 = new javax.swing.JLabel();
        cmbGrupo = new javax.swing.JComboBox<>();
        jLabelCodProduto = new javax.swing.JLabel();
        lbBarra = new javax.swing.JLabel();
        txtCodigoBarra = new javax.swing.JTextField();
        btnSalvar = new javax.swing.JButton();
        btnAlterar2 = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        ivaJPanel = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        cmbImposto = new javax.swing.JComboBox<>();
        ivaTaxaJLabel = new javax.swing.JLabel();
        ivaAplicarJRadioButton = new javax.swing.JRadioButton();
        txtPrecoDeVendaComIva = new javax.swing.JTextField();
        TotalIvaLabel = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        ivaMotivoJComboBox = new javax.swing.JComboBox();
        ivaMotivoJLabel = new javax.swing.JLabel();
        ivaNaoAplicarJRadioButton = new javax.swing.JRadioButton();
        txtPrecoDeVendaSemIva = new javax.swing.JTextField();
        TotalIvaLabel1 = new javax.swing.JLabel();
        jPanel_retencao = new javax.swing.JPanel();
        retencaoAplicarJRadioButton = new javax.swing.JRadioButton();
        retencaoNaoAplicarJRadioButton = new javax.swing.JRadioButton();
        retencaoTaxaJTextField = new javax.swing.JTextField();
        retencaoTaxaJLabel = new javax.swing.JLabel();
        retencaoZeroTaxaJTextField = new javax.swing.JTextField();
        btnLimpar = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtDesignacao = new javax.swing.JTextArea();
        jButton3 = new javax.swing.JButton();
        lbTipoProduto5 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        lbPhoto = new javax.swing.JLabel();
        btnCarregar = new javax.swing.JButton();
        rbNaoEnviarCozinha = new javax.swing.JRadioButton();
        rbEnviarCozinha = new javax.swing.JRadioButton();
        lbTicket = new javax.swing.JLabel();
        btnAssociar = new javax.swing.JButton();
        rbEnviarSala = new javax.swing.JRadioButton();
        jButton2 = new javax.swing.JButton();
        lbUnidade = new javax.swing.JLabel();
        txtUnidadeCompra = new javax.swing.JTextField();
        label_qtd_bruta = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        txtCodigoProduto = new javax.swing.JTextField();
        lbProdutos2 = new javax.swing.JLabel();
        txtCodigoBarraProcura = new javax.swing.JTextField();
        lbProdutos3 = new javax.swing.JLabel();
        lbProdutos4 = new javax.swing.JLabel();
        txtCodigoManualProcura = new javax.swing.JTextField();
        jButton4 = new javax.swing.JButton();
        rbJanelaServico = new javax.swing.JRadioButton();
        jLabel2 = new javax.swing.JLabel();
        painel_stock = new javax.swing.JPanel();
        llbDataFabrico = new javax.swing.JLabel();
        jcDataFabrico = new com.toedter.calendar.JDateChooser();
        lbDataExpiracao = new javax.swing.JLabel();
        jcDataExpiracao = new com.toedter.calendar.JDateChooser();
        txtPercentagemGanhoRetalho = new javax.swing.JTextField();
        txtPrecoVendaRetalho = new javax.swing.JTextField();
        lbCategoria3 = new javax.swing.JLabel();
        cmbLocal = new javax.swing.JComboBox<>();
        lbBarra1 = new javax.swing.JLabel();
        txtCodigoManual = new javax.swing.JTextField();
        lbPrecoVenda1 = new javax.swing.JLabel();
        txtPrecoVendaGrosso = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        txtQtdGrosso = new javax.swing.JTextField();
        txtArmazen1 = new javax.swing.JTextField();
        txtArmazen2 = new javax.swing.JTextField();
        txtArmazen3 = new javax.swing.JTextField();
        txtArmazen4 = new javax.swing.JTextField();
        cmbarmazem1 = new javax.swing.JComboBox<>();
        cmbarmazem2 = new javax.swing.JComboBox<>();
        cmbarmazem3 = new javax.swing.JComboBox<>();
        cmbarmazem4 = new javax.swing.JComboBox<>();
        jButtonCompras = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("...::::: KITANDA - INGREDIENTES ::::...");

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lbTipoProduto.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 16)); // NOI18N
        lbTipoProduto.setText("Sub Família:");
        jPanel5.add(lbTipoProduto, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 80, 90, 22));

        cmbTipoProduto.setBackground(new java.awt.Color(4, 154, 3));
        cmbTipoProduto.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 14)); // NOI18N
        cmbTipoProduto.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbTipoProdutoActionPerformed(evt);
            }
        });
        jPanel5.add(cmbTipoProduto, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 80, 220, -1));

        lbProduto.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 16)); // NOI18N
        lbProduto.setText("Designação:");
        jPanel5.add(lbProduto, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 160, 90, 30));

        lbCusto.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 16)); // NOI18N
        lbCusto.setText("Preço Compra Retalho:");
        jPanel5.add(lbCusto, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 240, 180, 20));

        txtPrecoCompra.setBackground(new java.awt.Color(4, 154, 3));
        txtPrecoCompra.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        txtPrecoCompra.setForeground(new java.awt.Color(255, 255, 255));
        txtPrecoCompra.setCaretColor(new java.awt.Color(255, 255, 255));
        txtPrecoCompra.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtPrecoCompraActionPerformed(evt);
            }
        });
        jPanel5.add(txtPrecoCompra, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 260, 200, 30));

        buttonGroup2.add(ck_produto);
        ck_produto.setText("Produto");
        ck_produto.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ck_produtoActionPerformed(evt);
            }
        });
        jPanel5.add(ck_produto, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, -1));

        cmbUnidade.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbUnidadeActionPerformed(evt);
            }
        });
        jPanel5.add(cmbUnidade, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 260, 140, -1));

        buttonGroup2.add(ck_servico);
        ck_servico.setText("Serviço");
        ck_servico.setEnabled(false);
        ck_servico.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ck_servicoActionPerformed(evt);
            }
        });
        jPanel5.add(ck_servico, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 10, -1, -1));

        cmbFamilia.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbFamiliaActionPerformed(evt);
            }
        });
        jPanel5.add(cmbFamilia, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 70, 260, -1));

        lbTipoProduto2.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 16)); // NOI18N
        lbTipoProduto2.setText("Marca:");
        jPanel5.add(lbTipoProduto2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 40, 70, 30));

        cmbMarca.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbMarcaActionPerformed(evt);
            }
        });
        jPanel5.add(cmbMarca, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 40, 260, -1));

        lbTipoProduto3.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 16)); // NOI18N
        lbTipoProduto3.setText("Modelo:");
        jPanel5.add(lbTipoProduto3, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 40, 90, 22));

        cmbModelo.setBackground(new java.awt.Color(4, 154, 3));
        cmbModelo.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 14)); // NOI18N
        cmbModelo.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbModeloActionPerformed(evt);
            }
        });
        jPanel5.add(cmbModelo, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 40, 280, -1));

        lbTipoProduto4.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 16)); // NOI18N
        lbTipoProduto4.setText("Família:");
        jPanel5.add(lbTipoProduto4, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 72, 70, 20));

        jPanel5.add(cmbGrupo, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 110, 260, -1));

        jLabelCodProduto.setFont(new java.awt.Font("Lucida Grande", 1, 12)); // NOI18N
        jLabelCodProduto.setText("Codigo");
        jPanel5.add(jLabelCodProduto, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 109, 120, 30));

        lbBarra.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 16)); // NOI18N
        lbBarra.setText("Cod Barra:");
        jPanel5.add(lbBarra, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 120, 80, 20));

        txtCodigoBarra.setBackground(new java.awt.Color(4, 154, 3));
        txtCodigoBarra.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txtCodigoBarra.setForeground(new java.awt.Color(255, 255, 255));
        txtCodigoBarra.setText("0");
        txtCodigoBarra.setCaretColor(new java.awt.Color(255, 255, 255));
        txtCodigoBarra.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtCodigoBarraActionPerformed(evt);
            }
        });
        jPanel5.add(txtCodigoBarra, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 110, 190, 30));

        btnSalvar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/salvar_16x16.png"))); // NOI18N
        btnSalvar.setText("Salvar");
        btnSalvar.setAlignmentX(0.5F);
        btnSalvar.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnSalvarActionPerformed(evt);
            }
        });
        jPanel5.add(btnSalvar, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 530, 110, 50));

        btnAlterar2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/alterar_16x16.png"))); // NOI18N
        btnAlterar2.setText("Alterar");
        btnAlterar2.setAlignmentX(0.5F);
        btnAlterar2.setEnabled(false);
        btnAlterar2.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnAlterar2ActionPerformed(evt);
            }
        });
        jPanel5.add(btnAlterar2, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 530, 110, 50));

        btnCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/LOGOUT - VERMELHO/Logout 32x32.png"))); // NOI18N
        btnCancelar.setText("Sair");
        btnCancelar.setAlignmentX(0.5F);
        btnCancelar.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnCancelarActionPerformed(evt);
            }
        });
        jPanel5.add(btnCancelar, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 530, 100, 50));

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Impostos"));

        ivaJPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Aplicar IVA ao Produto", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 16))); // NOI18N
        ivaJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        cmbImposto.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        cmbImposto.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbImposto.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbImpostoActionPerformed(evt);
            }
        });

        ivaTaxaJLabel.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 10)); // NOI18N
        ivaTaxaJLabel.setForeground(new java.awt.Color(255, 0, 0));
        ivaTaxaJLabel.setText("Taxa IVA ( % )");

        buttonGroup1.add(ivaAplicarJRadioButton);
        ivaAplicarJRadioButton.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 14)); // NOI18N
        ivaAplicarJRadioButton.setText("Sim ( Aplicar IVA )");
        ivaAplicarJRadioButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ivaAplicarJRadioButtonActionPerformed(evt);
            }
        });

        txtPrecoDeVendaComIva.setEditable(false);
        txtPrecoDeVendaComIva.setBackground(new java.awt.Color(102, 102, 102));
        txtPrecoDeVendaComIva.setFont(new java.awt.Font("Lucida Grande", 1, 12)); // NOI18N
        txtPrecoDeVendaComIva.setForeground(new java.awt.Color(255, 255, 255));

        TotalIvaLabel.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 16)); // NOI18N
        TotalIvaLabel.setText("P.Venda Com IVA");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(cmbImposto, 0, 150, Short.MAX_VALUE)
                            .addComponent(ivaAplicarJRadioButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(ivaTaxaJLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(TotalIvaLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 146, Short.MAX_VALUE)
                    .addComponent(txtPrecoDeVendaComIva))
                .addContainerGap(14, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(ivaAplicarJRadioButton, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addGap(7, 7, 7)
                        .addComponent(ivaTaxaJLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbImposto, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(TotalIvaLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 23, Short.MAX_VALUE)
                        .addComponent(txtPrecoDeVendaComIva, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        ivaJPanel.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 330, 80));

        jPanel6.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        ivaMotivoJComboBox.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 14)); // NOI18N
        ivaMotivoJComboBox.addItemListener(new java.awt.event.ItemListener()
        {
            public void itemStateChanged(java.awt.event.ItemEvent evt)
            {
                ivaMotivoJComboBoxItemStateChanged(evt);
            }
        });
        ivaMotivoJComboBox.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ivaMotivoJComboBoxActionPerformed(evt);
            }
        });

        ivaMotivoJLabel.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 12)); // NOI18N
        ivaMotivoJLabel.setForeground(new java.awt.Color(255, 0, 0));
        ivaMotivoJLabel.setText("Motivos");

        buttonGroup1.add(ivaNaoAplicarJRadioButton);
        ivaNaoAplicarJRadioButton.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 14)); // NOI18N
        ivaNaoAplicarJRadioButton.setSelected(true);
        ivaNaoAplicarJRadioButton.setText("Não (Não aplicar IVA)");
        ivaNaoAplicarJRadioButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ivaNaoAplicarJRadioButtonActionPerformed(evt);
            }
        });

        txtPrecoDeVendaSemIva.setEditable(false);
        txtPrecoDeVendaSemIva.setBackground(new java.awt.Color(102, 102, 102));
        txtPrecoDeVendaSemIva.setFont(new java.awt.Font("Lucida Grande", 1, 12)); // NOI18N
        txtPrecoDeVendaSemIva.setForeground(new java.awt.Color(255, 255, 255));

        TotalIvaLabel1.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 16)); // NOI18N
        TotalIvaLabel1.setText("P.Venda Sem IVA");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(ivaMotivoJLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ivaMotivoJComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(ivaNaoAplicarJRadioButton, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtPrecoDeVendaSemIva, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
                    .addComponent(TotalIvaLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ivaNaoAplicarJRadioButton, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(TotalIvaLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5)
                .addComponent(ivaMotivoJLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ivaMotivoJComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPrecoDeVendaSemIva, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12))
        );

        ivaJPanel.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 20, 400, 80));

        jPanel_retencao.setBorder(javax.swing.BorderFactory.createTitledBorder("Aplicar Retenção ao Serviço"));

        buttonGroup4.add(retencaoAplicarJRadioButton);
        retencaoAplicarJRadioButton.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 12)); // NOI18N
        retencaoAplicarJRadioButton.setText("Sim ( Aplicar Retenção )");
        retencaoAplicarJRadioButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                retencaoAplicarJRadioButtonActionPerformed(evt);
            }
        });

        buttonGroup4.add(retencaoNaoAplicarJRadioButton);
        retencaoNaoAplicarJRadioButton.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 12)); // NOI18N
        retencaoNaoAplicarJRadioButton.setSelected(true);
        retencaoNaoAplicarJRadioButton.setText("Não (Não aplicar Retenção)");
        retencaoNaoAplicarJRadioButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                retencaoNaoAplicarJRadioButtonActionPerformed(evt);
            }
        });

        retencaoTaxaJTextField.setEditable(false);
        retencaoTaxaJTextField.setBackground(new java.awt.Color(4, 154, 3));
        retencaoTaxaJTextField.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 14)); // NOI18N
        retencaoTaxaJTextField.setForeground(new java.awt.Color(255, 255, 255));
        retencaoTaxaJTextField.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                retencaoTaxaJTextFieldActionPerformed(evt);
            }
        });

        retencaoTaxaJLabel.setText("Retenção");

        retencaoZeroTaxaJTextField.setEditable(false);
        retencaoZeroTaxaJTextField.setBackground(new java.awt.Color(4, 154, 3));
        retencaoZeroTaxaJTextField.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 14)); // NOI18N
        retencaoZeroTaxaJTextField.setForeground(new java.awt.Color(255, 255, 255));
        retencaoZeroTaxaJTextField.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                retencaoZeroTaxaJTextFieldActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel_retencaoLayout = new javax.swing.GroupLayout(jPanel_retencao);
        jPanel_retencao.setLayout(jPanel_retencaoLayout);
        jPanel_retencaoLayout.setHorizontalGroup(
            jPanel_retencaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_retencaoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(retencaoAplicarJRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(retencaoTaxaJLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(retencaoTaxaJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(retencaoNaoAplicarJRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(retencaoZeroTaxaJTextField))
        );
        jPanel_retencaoLayout.setVerticalGroup(
            jPanel_retencaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_retencaoLayout.createSequentialGroup()
                .addGroup(jPanel_retencaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel_retencaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(retencaoTaxaJTextField)
                        .addComponent(retencaoAplicarJRadioButton, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(retencaoTaxaJLabel))
                    .addComponent(retencaoNaoAplicarJRadioButton, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(2, 2, 2))
            .addGroup(jPanel_retencaoLayout.createSequentialGroup()
                .addComponent(retencaoZeroTaxaJTextField)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ivaJPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel_retencao, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addComponent(ivaJPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel_retencao, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(14, 14, 14))
        );

        jPanel5.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 320, 780, 210));

        btnLimpar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/lixo.png"))); // NOI18N
        btnLimpar.setText("Limpar");
        btnLimpar.setAlignmentX(0.5F);
        btnLimpar.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnLimparActionPerformed(evt);
            }
        });
        jPanel5.add(btnLimpar, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 530, 130, 50));

        jButton1.setText("+");
        jButton1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel5.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 80, 50, 20));

        txtDesignacao.setColumns(20);
        txtDesignacao.setRows(5);
        jScrollPane1.setViewportView(txtDesignacao);

        jPanel5.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 150, 530, 80));

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagem/eliminar/destroy_32x32.png"))); // NOI18N
        jButton3.setText("Desactivar");
        jButton3.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel5.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 530, 150, 50));

        lbTipoProduto5.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 16)); // NOI18N
        lbTipoProduto5.setText("Grupo:");
        jPanel5.add(lbTipoProduto5, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 120, 70, 22));

        jPanel4.setBackground(new java.awt.Color(0, 0, 0));
        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        lbPhoto.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lbPhoto.setForeground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbPhoto, javax.swing.GroupLayout.DEFAULT_SIZE, 136, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lbPhoto, javax.swing.GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE)
        );

        jPanel5.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 190, 150, -1));

        btnCarregar.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        btnCarregar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/proucura.png"))); // NOI18N
        btnCarregar.setText("Carregar Foto");
        btnCarregar.setToolTipText("Carregue aqui a Foto do Produto");
        btnCarregar.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnCarregarActionPerformed(evt);
            }
        });
        jPanel5.add(btnCarregar, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 150, 150, 30));

        buttonGroup5.add(rbNaoEnviarCozinha);
        rbNaoEnviarCozinha.setFont(new java.awt.Font("Lucida Sans Typewriter", 0, 12)); // NOI18N
        rbNaoEnviarCozinha.setSelected(true);
        rbNaoEnviarCozinha.setText("Nao Enviar Ticket");
        jPanel5.add(rbNaoEnviarCozinha, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 300, 160, -1));

        buttonGroup5.add(rbEnviarCozinha);
        rbEnviarCozinha.setFont(new java.awt.Font("Lucida Sans Typewriter", 0, 12)); // NOI18N
        rbEnviarCozinha.setText("Enviar Ticket");
        jPanel5.add(rbEnviarCozinha, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 300, 140, -1));

        lbTicket.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 16)); // NOI18N
        lbTicket.setText("Impressão de Tickets:");
        jPanel5.add(lbTicket, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 300, 140, 22));

        btnAssociar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens32x32/actualizar_32x32.png"))); // NOI18N
        btnAssociar.setText("associação");
        btnAssociar.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnAssociarActionPerformed(evt);
            }
        });
        jPanel5.add(btnAssociar, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 0, 280, -1));

        buttonGroup5.add(rbEnviarSala);
        rbEnviarSala.setFont(new java.awt.Font("Lucida Sans Typewriter", 0, 12)); // NOI18N
        rbEnviarSala.setText("Enviar Sala");
        jPanel5.add(rbEnviarSala, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 300, 130, -1));

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/img32x32/_ok.png"))); // NOI18N
        jButton2.setText("Novo");
        jButton2.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel5.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 530, -1, 50));

        lbUnidade.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 16)); // NOI18N
        lbUnidade.setText("Unidade:");
        jPanel5.add(lbUnidade, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 240, 120, 20));

        txtUnidadeCompra.setBackground(new java.awt.Color(4, 154, 3));
        txtUnidadeCompra.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 14)); // NOI18N
        txtUnidadeCompra.setForeground(new java.awt.Color(255, 255, 255));
        txtUnidadeCompra.setText("1");
        txtUnidadeCompra.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtUnidadeCompraActionPerformed(evt);
            }
        });
        jPanel5.add(txtUnidadeCompra, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 260, 150, 30));

        label_qtd_bruta.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 16)); // NOI18N
        label_qtd_bruta.setText("Unidade de Compra:");
        jPanel5.add(label_qtd_bruta, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 240, 140, -1));

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("BUSCA DE INGREDIENTES"));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtCodigoProduto.setBackground(new java.awt.Color(4, 154, 3));
        txtCodigoProduto.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txtCodigoProduto.setForeground(new java.awt.Color(255, 255, 255));
        txtCodigoProduto.setCaretColor(new java.awt.Color(255, 255, 255));
        txtCodigoProduto.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtCodigoProdutoActionPerformed(evt);
            }
        });
        jPanel3.add(txtCodigoProduto, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 20, 70, 30));

        lbProdutos2.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 16)); // NOI18N
        lbProdutos2.setText("Cód. Interno:");
        jPanel3.add(lbProdutos2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, -1, 30));

        txtCodigoBarraProcura.setBackground(new java.awt.Color(4, 154, 3));
        txtCodigoBarraProcura.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txtCodigoBarraProcura.setForeground(new java.awt.Color(255, 255, 255));
        txtCodigoBarraProcura.setCaretColor(new java.awt.Color(255, 255, 255));
        txtCodigoBarraProcura.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtCodigoBarraProcuraActionPerformed(evt);
            }
        });
        jPanel3.add(txtCodigoBarraProcura, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 20, 160, 30));

        lbProdutos3.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 16)); // NOI18N
        lbProdutos3.setText("Cód. Barra:");
        jPanel3.add(lbProdutos3, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 20, 90, 30));

        lbProdutos4.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 16)); // NOI18N
        lbProdutos4.setText("Cód. Manual:");
        jPanel3.add(lbProdutos4, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 20, 100, 30));

        txtCodigoManualProcura.setBackground(new java.awt.Color(4, 154, 3));
        txtCodigoManualProcura.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txtCodigoManualProcura.setForeground(new java.awt.Color(255, 255, 255));
        txtCodigoManualProcura.setCaretColor(new java.awt.Color(255, 255, 255));
        txtCodigoManualProcura.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtCodigoManualProcuraActionPerformed(evt);
            }
        });
        jPanel3.add(txtCodigoManualProcura, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 20, 170, 30));

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/proucura.png"))); // NOI18N
        jButton4.setToolTipText("Lupa de Pesquisa de Produtos/Serviços");
        jButton4.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 20, 40, 30));

        rbJanelaServico.setSelected(true);
        rbJanelaServico.setEnabled(false);
        rbJanelaServico.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                rbJanelaServicoActionPerformed(evt);
            }
        });
        jPanel3.add(rbJanelaServico, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 20, 10, -1));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 51, 102));
        jLabel2.setText("KITANDA 1.2");
        jPanel3.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 20, 239, 39));

        painel_stock.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        llbDataFabrico.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 16)); // NOI18N
        llbDataFabrico.setText("Data Fabrico:");

        jcDataFabrico.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N

        lbDataExpiracao.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 16)); // NOI18N
        lbDataExpiracao.setText("Data Expiracao:");

        jcDataExpiracao.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N

        txtPercentagemGanhoRetalho.setBackground(new java.awt.Color(4, 154, 3));
        txtPercentagemGanhoRetalho.setFont(new java.awt.Font("Tahoma", 0, 5)); // NOI18N
        txtPercentagemGanhoRetalho.setForeground(new java.awt.Color(255, 255, 255));
        txtPercentagemGanhoRetalho.setText("0");
        txtPercentagemGanhoRetalho.setCaretColor(new java.awt.Color(255, 255, 255));
        txtPercentagemGanhoRetalho.setEnabled(false);
        txtPercentagemGanhoRetalho.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtPercentagemGanhoRetalhoActionPerformed(evt);
            }
        });

        txtPrecoVendaRetalho.setBackground(new java.awt.Color(4, 154, 3));
        txtPrecoVendaRetalho.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        txtPrecoVendaRetalho.setForeground(new java.awt.Color(255, 255, 255));
        txtPrecoVendaRetalho.setCaretColor(new java.awt.Color(255, 255, 255));
        txtPrecoVendaRetalho.setEnabled(false);
        txtPrecoVendaRetalho.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtPrecoVendaRetalhoActionPerformed(evt);
            }
        });

        lbCategoria3.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 16)); // NOI18N
        lbCategoria3.setText("Local:");

        lbBarra1.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 16)); // NOI18N
        lbBarra1.setText("Cod Manual:");

        txtCodigoManual.setBackground(new java.awt.Color(4, 154, 3));
        txtCodigoManual.setForeground(new java.awt.Color(255, 255, 255));

        lbPrecoVenda1.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 16)); // NOI18N
        lbPrecoVenda1.setText("Preco Grosso:");

        txtPrecoVendaGrosso.setBackground(new java.awt.Color(4, 154, 3));
        txtPrecoVendaGrosso.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        txtPrecoVendaGrosso.setForeground(new java.awt.Color(255, 255, 255));
        txtPrecoVendaGrosso.setCaretColor(new java.awt.Color(255, 255, 255));
        txtPrecoVendaGrosso.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtPrecoVendaGrossoActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tw Cen MT Condensed", 1, 16)); // NOI18N
        jLabel1.setText("Qtd Grosso:");

        txtQtdGrosso.setBackground(new java.awt.Color(4, 154, 3));
        txtQtdGrosso.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        txtQtdGrosso.setForeground(new java.awt.Color(255, 255, 255));
        txtQtdGrosso.setCaretColor(new java.awt.Color(255, 255, 255));
        txtQtdGrosso.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtQtdGrossoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout painel_stockLayout = new javax.swing.GroupLayout(painel_stock);
        painel_stock.setLayout(painel_stockLayout);
        painel_stockLayout.setHorizontalGroup(
            painel_stockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(painel_stockLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(painel_stockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(painel_stockLayout.createSequentialGroup()
                        .addComponent(lbBarra1, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCodigoManual, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(painel_stockLayout.createSequentialGroup()
                        .addGroup(painel_stockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(lbPrecoVenda1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 109, Short.MAX_VALUE))
                        .addGap(30, 30, 30)
                        .addGroup(painel_stockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(painel_stockLayout.createSequentialGroup()
                                .addComponent(txtQtdGrosso, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(txtPrecoVendaGrosso)))
                    .addGroup(painel_stockLayout.createSequentialGroup()
                        .addComponent(lbCategoria3, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmbLocal, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(painel_stockLayout.createSequentialGroup()
                        .addGroup(painel_stockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(llbDataFabrico, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbDataExpiracao, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(painel_stockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jcDataFabrico, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jcDataExpiracao, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(painel_stockLayout.createSequentialGroup()
                        .addGap(139, 139, 139)
                        .addComponent(txtPrecoVendaRetalho, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(17, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, painel_stockLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txtPercentagemGanhoRetalho, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(38, 38, 38))
        );
        painel_stockLayout.setVerticalGroup(
            painel_stockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, painel_stockLayout.createSequentialGroup()
                .addGap(83, 83, 83)
                .addGroup(painel_stockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbCategoria3, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbLocal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(painel_stockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCodigoManual, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbBarra1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(41, 41, 41)
                .addGroup(painel_stockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(llbDataFabrico, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jcDataFabrico, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(painel_stockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbDataExpiracao)
                    .addComponent(jcDataExpiracao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtPercentagemGanhoRetalho, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtPrecoVendaRetalho, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addGroup(painel_stockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtQtdGrosso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(painel_stockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtPrecoVendaGrosso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbPrecoVenda1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(172, Short.MAX_VALUE))
        );

        txtArmazen1.setEditable(false);
        txtArmazen1.setBackground(new java.awt.Color(4, 154, 3));
        txtArmazen1.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        txtArmazen1.setForeground(new java.awt.Color(255, 255, 255));

        txtArmazen2.setEditable(false);
        txtArmazen2.setBackground(new java.awt.Color(4, 154, 3));
        txtArmazen2.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        txtArmazen2.setForeground(new java.awt.Color(255, 255, 255));

        txtArmazen3.setEditable(false);
        txtArmazen3.setBackground(new java.awt.Color(4, 154, 3));
        txtArmazen3.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        txtArmazen3.setForeground(new java.awt.Color(255, 255, 255));

        txtArmazen4.setEditable(false);
        txtArmazen4.setBackground(new java.awt.Color(4, 154, 3));
        txtArmazen4.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        txtArmazen4.setForeground(new java.awt.Color(255, 255, 255));
        txtArmazen4.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtArmazen4ActionPerformed(evt);
            }
        });

        cmbarmazem1.setBackground(new java.awt.Color(4, 154, 3));
        cmbarmazem1.setFont(new java.awt.Font("Times New Roman", 1, 13)); // NOI18N
        cmbarmazem1.setEnabled(false);

        cmbarmazem2.setBackground(new java.awt.Color(4, 154, 3));
        cmbarmazem2.setFont(new java.awt.Font("Times New Roman", 1, 13)); // NOI18N
        cmbarmazem2.setEnabled(false);

        cmbarmazem3.setBackground(new java.awt.Color(4, 154, 3));
        cmbarmazem3.setFont(new java.awt.Font("Times New Roman", 1, 13)); // NOI18N
        cmbarmazem3.setEnabled(false);

        cmbarmazem4.setBackground(new java.awt.Color(4, 154, 3));
        cmbarmazem4.setFont(new java.awt.Font("Times New Roman", 1, 13)); // NOI18N
        cmbarmazem4.setEnabled(false);

        jButtonCompras.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagem/caixa_32x_32.png"))); // NOI18N
        jButtonCompras.setText("Compras");
        jButtonCompras.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButtonComprasActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 821, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(cmbarmazem1, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtArmazen1, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(cmbarmazem2, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(txtArmazen2, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cmbarmazem3, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtArmazen3, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cmbarmazem4, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtArmazen4, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(13, 13, 13)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(painel_stock, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButtonCompras, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {txtArmazen1, txtArmazen2});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtArmazen1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtArmazen2, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtArmazen3, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtArmazen4, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cmbarmazem1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cmbarmazem4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButtonCompras)
                        .addComponent(cmbarmazem2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cmbarmazem3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 597, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(painel_stock, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalvarActionPerformed

        procedimento_salvar_produto();


    }//GEN-LAST:event_btnSalvarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void cmbTipoProdutoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbTipoProdutoActionPerformed
        try
        {
            // TODO add your handling code here:
            System.out.println( "CODIGO: " + getCodigoTipoProduto() );
        }
        catch ( SQLException ex )
        {
            Logger.getLogger(ProdutosIngredientesVisao.class.getName() ).log( Level.SEVERE, null, ex );
        }


    }//GEN-LAST:event_cmbTipoProdutoActionPerformed

    private void txtCodigoProdutoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCodigoProdutoActionPerformed
        // TODO add your handling code here:
        try
        {
            btnSalvar.setEnabled( false );
            btnAlterar2.setEnabled( true );
            String codInternoString = txtCodigoProduto.getText();
            Integer codigoInternoInt = (codInternoString.isEmpty() ? 0 : Integer.parseInt( codInternoString ));
            produto = (TbProduto) produtosController.findById( codigoInternoInt );

            ver_dados_produtos( codigoInternoInt );
            if ( ivaAplicarJRadioButton.isSelected() )
            {
                calcularTotalComIva();

            }
            else
            {
                txtPrecoDeVendaComIva.setVisible( false );
                ivaTaxaJLabel.setVisible( false );
                TotalIvaLabel.setVisible( false );
            }

            mostrar_qtd_stock_armazem( txtArmazen1, 1 );
            mostrar_qtd_stock_armazem( txtArmazen2, 2 );
            mostrar_qtd_stock_armazem( txtArmazen3, 3 );
            mostrar_qtd_stock_armazem( txtArmazen4, 4 );

            setButtonAssociar();

        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }


    }//GEN-LAST:event_txtCodigoProdutoActionPerformed

    private void calcularTotalComIva()
    {

        double totalPrecoVenda, resultado, taxa_iva = 0;
        taxa_iva = Double.parseDouble( cmbImposto.getSelectedItem().toString() );

        totalPrecoVenda = MetodosUtil.convertToDouble( txtPrecoVendaRetalho.getText() );
        double valor_iva = 1 + ( taxa_iva ) / 100;

        resultado = totalPrecoVenda * valor_iva;

        txtPrecoDeVendaComIva.setText(
                String.valueOf( CfMethods.formatarComoPorcoes( resultado ) ) );

    }

    private void txtCodigoBarraProcuraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCodigoBarraProcuraActionPerformed
        // TODO add your handling code here:
        btnSalvar.setEnabled( false );
        btnAlterar2.setEnabled( true );

        buscar_by_cod_barra();
//        txtCodigoBarraProcura.setText( "" );
    }//GEN-LAST:event_txtCodigoBarraProcuraActionPerformed

    private void ck_produtoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ck_produtoActionPerformed
        // TODO add your handling code here:
        nao_ver_retencao();
        servico_produto();
        txtDesignacao.requestFocus();
    }//GEN-LAST:event_ck_produtoActionPerformed

    private void txtPrecoCompraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPrecoCompraActionPerformed
        // TODO add your handling code here:

        if ( !ck_produto.isSelected() )
        {
            procedimento_salvar_produto();
        }
        else
        {
            txtPrecoVendaRetalho.requestFocus();
        }


    }//GEN-LAST:event_txtPrecoCompraActionPerformed

    private void txtCodigoBarraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCodigoBarraActionPerformed
        // TODO add your handling code here:
        txtPrecoCompra.requestFocus();
    }//GEN-LAST:event_txtCodigoBarraActionPerformed

    private void txtPercentagemGanhoRetalhoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPercentagemGanhoRetalhoActionPerformed
        // TODO add your handling code here:
        // procedimento_salvar_servico_produto();
        txtPrecoVendaRetalho.requestFocus();
    }//GEN-LAST:event_txtPercentagemGanhoRetalhoActionPerformed

    private void txtPrecoVendaRetalhoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPrecoVendaRetalhoActionPerformed
        // TODO add your handling code here:
        //txtQtdGrosso.requestFocus();

        procedimento_salvar_produto();


    }//GEN-LAST:event_txtPrecoVendaRetalhoActionPerformed

    private void ivaMotivoJComboBoxItemStateChanged(java.awt.event.ItemEvent evt)//GEN-FIRST:event_ivaMotivoJComboBoxItemStateChanged
    {//GEN-HEADEREND:event_ivaMotivoJComboBoxItemStateChanged

    }//GEN-LAST:event_ivaMotivoJComboBoxItemStateChanged

    private void ivaAplicarJRadioButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_ivaAplicarJRadioButtonActionPerformed
    {//GEN-HEADEREND:event_ivaAplicarJRadioButtonActionPerformed

        atualizarIvaForm();
        txtPrecoDeVendaComIva.setVisible( true );
        TotalIvaLabel.setVisible( true );
        calcularSemIva();
    }//GEN-LAST:event_ivaAplicarJRadioButtonActionPerformed

    private void ivaNaoAplicarJRadioButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_ivaNaoAplicarJRadioButtonActionPerformed
    {//GEN-HEADEREND:event_ivaNaoAplicarJRadioButtonActionPerformed

        atualizarIvaForm();
        txtPrecoDeVendaComIva.setVisible( false );
        TotalIvaLabel.setVisible( false );
        calcularSemIva();
    }//GEN-LAST:event_ivaNaoAplicarJRadioButtonActionPerformed

    private void ivaMotivoJComboBoxActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_ivaMotivoJComboBoxActionPerformed
    {//GEN-HEADEREND:event_ivaMotivoJComboBoxActionPerformed
        calcularSemIva();
    }//GEN-LAST:event_ivaMotivoJComboBoxActionPerformed

    private void txtCodigoManualProcuraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCodigoManualProcuraActionPerformed
        btnSalvar.setEnabled( false );
        btnAlterar2.setEnabled( true );
        buscar_by_cod_manual();
        txtCodigoManualProcura.setText( "" );
    }//GEN-LAST:event_txtCodigoManualProcuraActionPerformed

    private void ck_servicoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ck_servicoActionPerformed
        // TODO add your handling code here:
        // TODO add your handling code here:
//        cmbFamilia.setModel( new DefaultComboBoxModel( (Vector) familiaDao.buscaTodasFamiliaServico() ) );
//        cmbFamilia.setSelectedIndex(1);
        rbJanelaServico.setSelected( false );
        procedimento_limpar();
        ver_retencao();
        actualizarRetencaoForm();
        servico_produto();
        txtDesignacao.requestFocus();
//        lbCusto.setText("Preço");
        mostrarPreco();
    }//GEN-LAST:event_ck_servicoActionPerformed

    private void mostrarPreco()
    {

        if ( ck_servico.isSelected() )
        {

            lbCusto.setText( "Preço:" );
        }

    }


    private void btnAlterar2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAlterar2ActionPerformed
        // TODO add your handling code here:

        procedimento_alterar_servico_produto();


    }//GEN-LAST:event_btnAlterar2ActionPerformed

    private void cmbFamiliaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbFamiliaActionPerformed

        try
        {
            servico_produto();
            cmbTipoProduto.setModel( new DefaultComboBoxModel( tipoProdutosController.getVectorByIdFamilia( getIdFamilia() ) ) );

        }
        catch ( Exception e )
        {
        }
    }//GEN-LAST:event_cmbFamiliaActionPerformed

    private void cmbMarcaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbMarcaActionPerformed
        try
        {
            cmbModelo.setModel( new DefaultComboBoxModel( modelosController.getVectorByIdMarca( getIdMarca() ) ) );

        }
        catch ( Exception e )
        {
        }
    }//GEN-LAST:event_cmbMarcaActionPerformed

    private void cmbModeloActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbModeloActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbModeloActionPerformed

    private void btnCarregarActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnCarregarActionPerformed
    {//GEN-HEADEREND:event_btnCarregarActionPerformed
        // TODO add your handling code here:
        image_View.chooseImg( lbPhoto );
    }//GEN-LAST:event_btnCarregarActionPerformed

    private void retencaoAplicarJRadioButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_retencaoAplicarJRadioButtonActionPerformed
    {//GEN-HEADEREND:event_retencaoAplicarJRadioButtonActionPerformed
        actualizarRetencaoForm();
    }//GEN-LAST:event_retencaoAplicarJRadioButtonActionPerformed

    private void retencaoNaoAplicarJRadioButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_retencaoNaoAplicarJRadioButtonActionPerformed
    {//GEN-HEADEREND:event_retencaoNaoAplicarJRadioButtonActionPerformed
        actualizarRetencaoForm();
    }//GEN-LAST:event_retencaoNaoAplicarJRadioButtonActionPerformed

    private void retencaoTaxaJTextFieldActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_retencaoTaxaJTextFieldActionPerformed
    {//GEN-HEADEREND:event_retencaoTaxaJTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_retencaoTaxaJTextFieldActionPerformed

    private void cmbImpostoActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbImpostoActionPerformed
    {//GEN-HEADEREND:event_cmbImpostoActionPerformed
        // TODO add your handling code here:
        calcularIva();
        calcularSemIva();
    }//GEN-LAST:event_cmbImpostoActionPerformed

    private void retencaoZeroTaxaJTextFieldActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_retencaoZeroTaxaJTextFieldActionPerformed
    {//GEN-HEADEREND:event_retencaoZeroTaxaJTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_retencaoZeroTaxaJTextFieldActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton4ActionPerformed
    {//GEN-HEADEREND:event_jButton4ActionPerformed

        btnSalvar.setEnabled( false );
        btnAlterar2.setEnabled( true );
        try
        {

            new BuscaProdutoIngredientesVisao( this, rootPaneCheckingEnabled, getCodigoArmazem( cmbarmazem2.getSelectedItem().toString() ), DVML.JANELA_PRODUTO, conexao ).setVisible( true );
            mostrar_qtd_stock_armazem( txtArmazen1, 1 );
            mostrar_qtd_stock_armazem( txtArmazen2, 2 );
            mostrar_qtd_stock_armazem( txtArmazen3, 3 );
            mostrar_qtd_stock_armazem( txtArmazen4, 4 );

            if ( ivaAplicarJRadioButton.isSelected() )
            {
                calcularTotalComIva();
            }
            else
            {
                txtPrecoDeVendaComIva.setVisible( false );
                ivaTaxaJLabel.setVisible( false );
            }

        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void btnLimparActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnLimparActionPerformed
    {//GEN-HEADEREND:event_btnLimparActionPerformed
        procedimento_limpar_campos();

//        txtCodigoBarra.setText( String.valueOf( produtosController.getLastProduto().getCodigo() + 1 ) );
        btnAlterar2.setEnabled( false );
    }//GEN-LAST:event_btnLimparActionPerformed

    private void cmbUnidadeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbUnidadeActionPerformed
        txtPrecoVendaRetalho.requestFocus();
    }//GEN-LAST:event_cmbUnidadeActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        new CategoriasLugarVisao().show();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButtonComprasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonComprasActionPerformed
        try
        {

            new CompraInformalVisao( idUser, this.conexao ).show();

        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButtonComprasActionPerformed

    private void txtPrecoVendaGrossoActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtPrecoVendaGrossoActionPerformed
    {//GEN-HEADEREND:event_txtPrecoVendaGrossoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPrecoVendaGrossoActionPerformed

    private void txtQtdGrossoActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtQtdGrossoActionPerformed
    {//GEN-HEADEREND:event_txtQtdGrossoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtQtdGrossoActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton3ActionPerformed
    {//GEN-HEADEREND:event_jButton3ActionPerformed
        procedimento_alterar_servico_produto_eliminar();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void rbJanelaServicoActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_rbJanelaServicoActionPerformed
    {//GEN-HEADEREND:event_rbJanelaServicoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rbJanelaServicoActionPerformed

    private void txtArmazen4ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtArmazen4ActionPerformed
    {//GEN-HEADEREND:event_txtArmazen4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtArmazen4ActionPerformed

    private void btnAssociarActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnAssociarActionPerformed
    {//GEN-HEADEREND:event_btnAssociarActionPerformed
        // TODO add your handling code here:
        new AssociacaoServicoVisao( idUser,
                getCodigoProduto(),
                getCodigoArmazem( cmbarmazem1.getSelectedItem().toString() ), conexao ).setVisible( true );
    }//GEN-LAST:event_btnAssociarActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton2ActionPerformed
    {//GEN-HEADEREND:event_jButton2ActionPerformed
        novo();
        txtDesignacao.requestFocus();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void txtUnidadeCompraActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtUnidadeCompraActionPerformed
    {//GEN-HEADEREND:event_txtUnidadeCompraActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtUnidadeCompraActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main( String args[] )
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
                if ( "Windows".equals( info.getName() ) )
                {
                    javax.swing.UIManager.setLookAndFeel( info.getClassName() );
                    break;
                }
            }
        }
        catch ( ClassNotFoundException ex )
        {
            java.util.logging.Logger.getLogger(ProdutosIngredientesVisao.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        catch ( InstantiationException ex )
        {
            java.util.logging.Logger.getLogger(ProdutosIngredientesVisao.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        catch ( IllegalAccessException ex )
        {
            java.util.logging.Logger.getLogger(ProdutosIngredientesVisao.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        catch ( javax.swing.UnsupportedLookAndFeelException ex )
        {
            java.util.logging.Logger.getLogger(ProdutosIngredientesVisao.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                try
                {
                    ProdutosIngredientesVisao dialog = new ProdutosIngredientesVisao( new javax.swing.JFrame(), true, 15, new BDConexao() );
                    dialog.addWindowListener( new java.awt.event.WindowAdapter()
                    {
                        @Override
                        public void windowClosing( java.awt.event.WindowEvent e )
                        {
                            System.exit( 0 );
                        }
                    } );
                    dialog.setVisible( true );
                }
                catch ( Exception ex )
                {
                    Logger.getLogger(ProdutosIngredientesVisao.class.getName() ).log( Level.SEVERE, null, ex );
                }
            }
        } );
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private static javax.swing.JLabel TotalIvaLabel;
    private static javax.swing.JLabel TotalIvaLabel1;
    private javax.swing.JButton btnAlterar2;
    public static javax.swing.JButton btnAssociar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnCarregar;
    private javax.swing.JButton btnLimpar;
    private static javax.swing.JButton btnSalvar;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.ButtonGroup buttonGroup3;
    private javax.swing.ButtonGroup buttonGroup4;
    private javax.swing.ButtonGroup buttonGroup5;
    public static javax.swing.JCheckBox ck_produto;
    public static javax.swing.JCheckBox ck_servico;
    public static javax.swing.JComboBox<String> cmbFamilia;
    public static javax.swing.JComboBox<String> cmbGrupo;
    private static javax.swing.JComboBox<String> cmbImposto;
    public static javax.swing.JComboBox<String> cmbLocal;
    public static javax.swing.JComboBox<String> cmbMarca;
    public static javax.swing.JComboBox cmbModelo;
    public static javax.swing.JComboBox cmbTipoProduto;
    public static javax.swing.JComboBox<String> cmbUnidade;
    private static javax.swing.JComboBox<String> cmbarmazem1;
    private static javax.swing.JComboBox<String> cmbarmazem2;
    private static javax.swing.JComboBox<String> cmbarmazem3;
    private static javax.swing.JComboBox<String> cmbarmazem4;
    private static javax.swing.JRadioButton ivaAplicarJRadioButton;
    private javax.swing.JPanel ivaJPanel;
    private static javax.swing.JComboBox ivaMotivoJComboBox;
    private static javax.swing.JLabel ivaMotivoJLabel;
    private static javax.swing.JRadioButton ivaNaoAplicarJRadioButton;
    private static javax.swing.JLabel ivaTaxaJLabel;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private static javax.swing.JButton jButtonCompras;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabelCodProduto;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    public static javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private static javax.swing.JPanel jPanel_retencao;
    private javax.swing.JScrollPane jScrollPane1;
    public static com.toedter.calendar.JDateChooser jcDataExpiracao;
    public static com.toedter.calendar.JDateChooser jcDataFabrico;
    private javax.swing.JLabel label_qtd_bruta;
    private javax.swing.JLabel lbBarra;
    private javax.swing.JLabel lbBarra1;
    private javax.swing.JLabel lbCategoria3;
    private javax.swing.JLabel lbCusto;
    private javax.swing.JLabel lbDataExpiracao;
    private static javax.swing.JLabel lbPhoto;
    private javax.swing.JLabel lbPrecoVenda1;
    private javax.swing.JLabel lbProduto;
    private javax.swing.JLabel lbProdutos2;
    private javax.swing.JLabel lbProdutos3;
    private javax.swing.JLabel lbProdutos4;
    private javax.swing.JLabel lbTicket;
    private javax.swing.JLabel lbTipoProduto;
    private javax.swing.JLabel lbTipoProduto2;
    private javax.swing.JLabel lbTipoProduto3;
    private javax.swing.JLabel lbTipoProduto4;
    private javax.swing.JLabel lbTipoProduto5;
    private javax.swing.JLabel lbUnidade;
    private javax.swing.JLabel llbDataFabrico;
    public static javax.swing.JPanel painel_stock;
    public static javax.swing.JRadioButton rbEnviarCozinha;
    private static javax.swing.JRadioButton rbEnviarSala;
    private static javax.swing.JRadioButton rbJanelaServico;
    public static javax.swing.JRadioButton rbNaoEnviarCozinha;
    private static javax.swing.JRadioButton retencaoAplicarJRadioButton;
    private static javax.swing.JRadioButton retencaoNaoAplicarJRadioButton;
    private static javax.swing.JLabel retencaoTaxaJLabel;
    private static javax.swing.JTextField retencaoTaxaJTextField;
    private static javax.swing.JTextField retencaoZeroTaxaJTextField;
    private static javax.swing.JTextField txtArmazen1;
    private static javax.swing.JTextField txtArmazen2;
    private static javax.swing.JTextField txtArmazen3;
    private static javax.swing.JTextField txtArmazen4;
    public static javax.swing.JTextField txtCodigoBarra;
    public static javax.swing.JTextField txtCodigoBarraProcura;
    public static javax.swing.JTextField txtCodigoManual;
    public static javax.swing.JTextField txtCodigoManualProcura;
    private static javax.swing.JTextField txtCodigoProduto;
    private static javax.swing.JTextArea txtDesignacao;
    public static javax.swing.JTextField txtPercentagemGanhoRetalho;
    public static javax.swing.JTextField txtPrecoCompra;
    private static javax.swing.JTextField txtPrecoDeVendaComIva;
    private static javax.swing.JTextField txtPrecoDeVendaSemIva;
    public static javax.swing.JTextField txtPrecoVendaGrosso;
    public static javax.swing.JTextField txtPrecoVendaRetalho;
    public static javax.swing.JTextField txtQtdGrosso;
    private static javax.swing.JTextField txtUnidadeCompra;
    // End of variables declaration//GEN-END:variables

    public void confiLabel()
    {

        lbTipoProduto.setHorizontalAlignment( JLabel.RIGHT );
        lbCusto.setHorizontalAlignment( JLabel.RIGHT );
        lbProduto.setHorizontalAlignment( JLabel.RIGHT );
        lbUnidade.setHorizontalAlignment( JLabel.RIGHT );

    }

    /* CRIACAO DOS GETS  */
    public String getProduto()
    {
        return txtDesignacao.getText();
    }

    public int getCodigoTipoProduto() throws SQLException
    {
        return conexao.getCodigoPublico( "tb_tipo_produto", String.valueOf( cmbTipoProduto.getSelectedItem() ) );
    }

    public static int getCodigoProduto()
    {

        try
        {
            return Integer.parseInt( txtCodigoProduto.getText() );
        }
        catch ( Exception e )
        {
            return 0;
        }

    }

    public int getCodigoBarras()
    {

        try
        {
            return Integer.parseInt( txtCodigoBarraProcura.getText() );
        }
        catch ( Exception e )
        {
            return 0;
        }

    }

    public Integer lastProduto()
    {

        String sql = ("select MAX(codigo) from tb_produto");

        ResultSet rs = conexao.executeQuery( sql );

        try
        {
            if ( rs.next() )
            {
                return rs.getInt( "MAX(codigo)" );
            }
        }
        catch ( SQLException ex )
        {
            return 0;
        }

        return 0;
    }

    private void buscar_by_cod_manual()
    {

        TbProduto produto_local = produtosController.findByCodManual( txtCodigoManualProcura.getText() );

        if ( !Objects.isNull( produto_local ) )
        {
            ver_dados( stock.getCodProdutoCodigo().getCodigo() );
            txtCodigoManualProcura.requestFocus();
        }
        else
        {
            procedimento_limpar();
        }

    }

    public float getPreco()
    {

        try
        {
            return Float.parseFloat( txtPrecoCompra.getText() );
        }
        catch ( Exception e )
        {
            return 0;
        }

    }

    public String getDataFabrico()
    {

        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime( new Date() );

        String dataSelecionada = gc.get( GregorianCalendar.YEAR ) + "-"
                + ( gc.get( GregorianCalendar.MONTH ) + 1 ) + "-"
                + gc.get( GregorianCalendar.DATE );
        return dataSelecionada;
    }

    public String getDataExpiraco()
    {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime( new Date() );

        String dataSelecionada = gc.get( GregorianCalendar.YEAR ) + "-"
                + ( gc.get( GregorianCalendar.MONTH ) + 1 ) + "-"
                + gc.get( GregorianCalendar.DATE );
        return dataSelecionada;
    }

    public String getDataEntrada()
    {
        GregorianCalendar gc = new GregorianCalendar();

        String dataSelecionada = gc.get( GregorianCalendar.YEAR ) + "-"
                + ( gc.get( GregorianCalendar.MONTH ) + 1 ) + "-"
                + gc.get( GregorianCalendar.DATE );
        return dataSelecionada;
    }

    public boolean campos_invalidos()
    {
        Color backGround = Color.WHITE;
        Color foreGround = Color.BLACK;
        Color caretColor = Color.BLACK;

        if ( getProduto().equals( "" ) )
        {
            txtDesignacao.setBackground( backGround );
            txtDesignacao.setForeground( foreGround );
            txtDesignacao.setCaretColor( caretColor );
            JOptionPane.showMessageDialog( null, "Pf. Preêncha o campo Produto: " );
            return true;
        }

        return false;
    }

    public boolean exist_produto() throws SQLException
    {
        return new ProdutoController( conexao ).exist( txtDesignacao.getText() );
    }

    public static void ver_dados_produtos( int codigo )
    {
        try
        {
            TbProduto produto_local = (TbProduto) produtosController.findById( codigo );
            if ( !Objects.isNull( produto_local ) )
            {
                ver_dados( produto_local.getCodigo() );
            }
            else
            {
                procedimento_limpar();
                JOptionPane.showMessageDialog( null, "Não existe produto com este código", "Alerta", JOptionPane.WARNING_MESSAGE );
            }

        }
        catch ( Exception e )
        {
            e.printStackTrace();
            procedimento_limpar();
            JOptionPane.showMessageDialog( null, e.getMessage(), "Falha na busca do produto\n"
                    + "ou produto desactivado.", JOptionPane.WARNING_MESSAGE );
        }

    }

    private static void servico_produto()
    {

        if ( ck_produto.isSelected() )
        {
            painel_stock.setVisible( true );
            cmbFamilia.setSelectedItem( "Produtos" );
            cmbFamilia.setEnabled( false );

        }
        else
        {
            painel_stock.setVisible( false );
            jButtonCompras.setVisible( false );
            cmbFamilia.setSelectedItem( "Serviços" );
            cmbFamilia.setEnabled( false );

        }

    }

    private static void servico_produto1()
    {

        if ( cmbFamilia.getSelectedItem().equals( "Serviços" ) )
        {
            painel_stock.setVisible( false );
            cmbFamilia.setEnabled( false );
            ck_servico.setSelected( true );
        }
        else
        {
            painel_stock.setVisible( true );
            cmbFamilia.setEnabled( true );
        }

    }

    private static void mostrar_painel( TbProduto produto )
    {

        if ( produto.getStocavel().equalsIgnoreCase( "true" ) )
        {
            ck_produto.setSelected( true );
        }
        else
        {
            ck_servico.setSelected( true );
        }

    }

    private void preparar_produto()
    {

        boolean isStocavel = ck_produto.isSelected();

        String designacao_produto = getDesignacaoText();
        produto.setDesignacao( designacao_produto );
        produto.setPreco( new BigDecimal( MetodosUtil.convertToDouble( txtPrecoCompra.getText() ) ) );
        produto.setDataFabrico( isStocavel ? jcDataFabrico.getDate() : new Date() );
        produto.setDataExpiracao( isStocavel ? jcDataExpiracao.getDate() : new Date() );
        produto.setCodBarra( isStocavel ? txtCodigoBarra.getText().trim() : "2147483647" );
        produto.setStatus( "Activo" );
        produto.setDataEntrada( new Date() );
        produto.setStocavel( isStocavel ? "true" : "false" );
        produto.setPrecoVenda( MetodosUtil.convertToDouble( txtPrecoCompra.getText() ) );
        produto.setQuantidadeDesconto( 0 );
        produto.setCodigoManual( isStocavel ? txtCodigoManual.getText() : "" );
        produto.setCodUnidade( new Unidade( getIdUnidade() ) );
        produto.setCodLocal( new TbLocal( getIdLocal() ) );
        produto.setCodFornecedores( new TbFornecedor( 1 ) );
        produto.setCodTipoProduto( new TbTipoProduto( getIdTipoPrdouto() ) );
        produto.setFkModelo( new Modelo( getIdModelo() ) );
        produto.setFkGrupo( new Grupo( getIdGrupo() ) );
        produto.setStatusIva( ivaAplicarJRadioButton.isSelected() ? "true" : "false" );
//        produto.setPercentagemDesconto( 0d );
        produto.setCozinha( getCozinha() );
        produto.setUnidadeCompra( Double.valueOf( txtUnidadeCompra.getText() ) );

        try
        {
            if ( Objects.nonNull( image_View.getBystebyteImg() ) )
            {
                produto.setPhoto( image_View.getBystebyteImg() );
            }
        }
        catch ( Exception e )
        {
        }

    }

    private void set_dados_produto()
    {
        try
        {
            preparar_produto();
            if ( Objects.nonNull( image_View.getBystebyteImg() ) )
            {
                produto.setPhoto( image_View.getBystebyteImg() );
            }
            else
            {
                produto.setPhoto( null );
            }
        }
        catch ( Exception e )
        {
        }

    }

    private boolean campos_validos_alterar()
    {

        String codigoBarraText = txtCodigoBarra.getText();
        boolean isProduto = ck_produto.isSelected();

        if ( produtosController.existProdutoByCodigoBarraExcepto( produto.getCodigo(), codigoBarraText ) && isProduto )
        {
            JOptionPane.showMessageDialog( null, "Já existe um produto com este codigo de barra." );
            txtCodigoBarra.requestFocus();
            return false;
        }
        else if ( produtosController.existProdutoByDesignacaoExcepto( produto.getCodigo(), getDesignacaoText() ) )
        {
            JOptionPane.showMessageDialog( null, "Já existe um produto na base de dados com esta designacao!" );
            txtDesignacao.requestFocus();
            return false;
        }

        if ( dadosInstituicao.getUsarDoisPrecos().equals( "sim" ) )
        {
            if ( !camposValidosQtdGrossoPrecoGrosso() )
            {
                return false;
            }

        }

        return true;

    }

    private void set_dados_produto_alterar()
    {

    }

    private void set_dados_eliminar()
    {

        String codigoBarraText = txtCodigoBarra.getText();

        try
        {
            produto.setStatus( "Desactivo" );
            produtosController.desactivar( produto );
            JOptionPane.showMessageDialog( null, "Produto desactivado com sucesso!", DVML.DVML_COMERCIAL, JOptionPane.INFORMATION_MESSAGE );
            procedimento_limpar_campos();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }

    }

    private void retencaoAtualizar( int fkProduto )
    {

        servicosRetencaoController.eliminarByIdProduto( fkProduto );
        if ( retencaoAplicarJRadioButton.isSelected() && ck_servico.isSelected() )
        {
            ServicoRetencao servicoRetencao = new ServicoRetencao();
            servicoRetencao.setFkProduto( new TbProduto( fkProduto ) );
            servicoRetencao.setFkRetencao( new Retencao( retencao.getPkRetencao() ) );

            servicosRetencaoController.salvar( servicoRetencao );
            retencaoTaxaJTextField.getText();
        }

    }

    private void procedimento_salvar_produto()
    {
        if ( campos_validos() )
        {
            conexaoTransaction = new BDConexao();
            DocumentoDao.startTransaction( conexaoTransaction );
            String designacao_produto = getDesignacaoText();
            ProdutosController produtosControllerLocal = new ProdutosController( conexaoTransaction );
            ProdutosMotivosIsensaoController produtosMotivosIsensaoControllerLocal = new ProdutosMotivosIsensaoController( conexaoTransaction );
            ProdutosIsentoController produtosIsentoControllerLocal = new ProdutosIsentoController( conexaoTransaction );
            ProdutosImpostoController produtosImpostoControllerLocal = new ProdutosImpostoController( conexaoTransaction );
            ServicosRetencaoController servicosRetencaoControllerLocal = new ServicosRetencaoController( conexaoTransaction );
            PrecosController precosControllerLocal = new PrecosController( conexaoTransaction );

            System.out.println( "CHEGUEI AQUI!....... INICIAL" );
            //ALTERAR PRODUTO
            if ( produtosControllerLocal.exist_designacao_produto( designacao_produto ) )
            {

                try
                {
                    produto = produtosControllerLocal.findByDesignacao( designacao_produto );
                    set_dados_produto();
                    produtosControllerLocal.actualizar( produto );
                    DocumentoDao.commitTransaction( conexaoTransaction );
                    proximo_codigo( produtosControllerLocal );
                    conexaoTransaction.close();
                    JOptionPane.showMessageDialog( null, "Produto actualizado com sucesso!" );
//                    procedimento_limpar();
                }
                catch ( Exception e )
                {
                    DocumentoDao.rollBackTransaction( conexaoTransaction );
                    conexaoTransaction.close();
                    JOptionPane.showMessageDialog( null, "Falha ao actualizar o produto", "Falha", JOptionPane.WARNING_MESSAGE );
                    return;
                }

            } //CRIAR NOVO PRODUTO
            else
            {

                produto = new TbProduto();
                set_dados_produto();
                if ( produtosControllerLocal.salvar( produto ) )
                {

                    System.err.println( "Produto Salvo" );
                    DocumentoDao.commitTransaction( conexaoTransaction ); // aceita a transacao
                    produto = produtosControllerLocal.getLastProduto();
                    DocumentoDao.startTransaction( conexaoTransaction ); // inicia uma nova transacao
                    if ( !ivaAplicarJRadioButton.isSelected() )//APLICAR ISENÇÃO
                    {
                        String regimeIsencao = (String) ivaMotivoJComboBox.getSelectedItem();
                        ProdutosMotivosIsensao motivosIsensao = produtosMotivosIsensaoControllerLocal.getRegime( regimeIsencao );

                        if ( Objects.nonNull( motivosIsensao ) )
                        {
                            ProdutoIsento produtoIsento = new ProdutoIsento();
                            produtoIsento.setFkProdutosMotivosIsensao( motivosIsensao );
                            produtoIsento.setFkProduto( new TbProduto( produto.getCodigo() ) );
                            if ( !produtosIsentoControllerLocal.salvar( produtoIsento ) )
                            {
                                DocumentoDao.rollBackTransaction( conexaoTransaction );
                                conexaoTransaction.close();
                                JOptionPane.showMessageDialog( null, "Falha ao registrar o produto", "Falha", JOptionPane.WARNING_MESSAGE );
                                return;

                            }
                            System.err.println( "Produto Isento Salvo" );

                        }

                    }
                    else //APLICAR IVA
                    {

                        ProdutoImposto produtoImposto = new ProdutoImposto();
                        produtoImposto.setFkProduto( new TbProduto( produto.getCodigo() ) );
                        produtoImposto.setFkImposto( new Imposto( getIdImposto() ) );
                        if ( !produtosImpostoControllerLocal.salvar( produtoImposto ) )
                        {
                            DocumentoDao.rollBackTransaction( conexaoTransaction );
                            conexaoTransaction.close();
                            JOptionPane.showMessageDialog( null, "Falha ao registrar o produto", "Falha", JOptionPane.WARNING_MESSAGE );
                            return;
                        }

                    }

//                    if ( !retencaoAplicarJRadioButton.isSelected() && ck_servico.isSelected() )
//                    if ( !retencaoAplicarJRadioButton.isSelected() )
//                    {
//                        ServicoRetencao produtoRetencao = new ServicoRetencao();
//                        produtoRetencao.setFkProduto( new TbProduto( produto.getCodigo() ) );
//                        produtoRetencao.setFkRetencao( new Retencao( 2 ) );
//                        if ( !servicosRetencaoControllerLocal.salvar( produtoRetencao ) )
//                        {
//                            DocumentoDao.rollBackTransaction( conexaoTransaction );
//                            conexaoTransaction.close();
//                            JOptionPane.showMessageDialog( null, "Falha ao registrar o produto", "Falha", JOptionPane.WARNING_MESSAGE );
//                            return;
//                        }
//
//                    }
//                    else 
                    //APLICAR RETENCAO
                    if ( retencaoAplicarJRadioButton.isSelected() )
                    {
                        ServicoRetencao produtoRetencao = new ServicoRetencao();
                        produtoRetencao.setFkProduto( new TbProduto( produto.getCodigo() ) );
                        produtoRetencao.setFkRetencao( new Retencao( 1 ) );

                        if ( !servicosRetencaoControllerLocal.salvar( produtoRetencao ) )
                        {
                            DocumentoDao.rollBackTransaction( conexaoTransaction );
                            conexaoTransaction.close();
                            JOptionPane.showMessageDialog( null, "Falha ao registrar o produto", "Falha", JOptionPane.WARNING_MESSAGE );
                            return;

                        }

                    }

                    if ( registrar_preco( precosControllerLocal ) )
                    {

                        DocumentoDao.commitTransaction( conexaoTransaction );
                        proximo_codigo( produtosControllerLocal );

                        conexaoTransaction.close();
                        JOptionPane.showMessageDialog( null, "Dados salvos com sucesso!", DVML.DVML_COMERCIAL, JOptionPane.INFORMATION_MESSAGE );
                        setActivarNegocio( dadosInstituicao.getNegocio() );
                        if ( rbJanelaServico.isSelected() )
                        {
                            painel_stock.setVisible( false );
                        }
                        else
                        {
                            painel_stock.setVisible( true );
                            procedimento_limpar();
                            txtDesignacao.requestFocus();
                            txtCodigoBarra.setText( String.valueOf( produtosController.getLastProduto().getCodigo() + 1 ) );

                        }

                        servico_produto();

                    }
                    detalhe_produto();

//                        procedimento_limpar();
//                        txtDesignacao.requestFocus();
//                        txtCodigoBarra.setText( String.valueOf( produtosController.getLastProduto().getCodigo() + 1 ) );
//
//                    }
                }
                else
                {
                    DocumentoDao.rollBackTransaction( conexaoTransaction );
                    JOptionPane.showMessageDialog( null, "Falha ao registrar o produto", "Falha", JOptionPane.WARNING_MESSAGE );
                    return;

                }

            }

        }

    }

    private void procedimento_alterar_servico_produto()
    {

        if ( !Objects.isNull( produto ) )
        {

            if ( campos_validos_alterar() )
            {

                try
                {

                    conexaoTransaction = new BDConexao();
                    ProdutosController produtosControllerLocal = new ProdutosController( conexaoTransaction );
                    PrecosController precosControllerLocal = new PrecosController( conexaoTransaction );
                    ProdutosImpostoController produtosImpostoControllerLocal = new ProdutosImpostoController( conexaoTransaction );
                    ProdutosIsentoController produtosIsentoControllerLocal = new ProdutosIsentoController( conexaoTransaction );
                    DocumentoDao.startTransaction( conexaoTransaction );
                    preparar_produto();
                    produtosControllerLocal.actualizar( produto );

                    ivaAtualizar( produto.getCodigo(), produtosImpostoControllerLocal, produtosIsentoControllerLocal );
                    retencaoAtualizar( produto.getCodigo() );
                    if ( registrar_preco( precosControllerLocal ) )
                    {
                        DocumentoDao.commitTransaction( conexaoTransaction );
                        JOptionPane.showMessageDialog( null, "Dados alterados com sucesso!", DVML.DVML_COMERCIAL, JOptionPane.INFORMATION_MESSAGE );
                        proximo_codigo( produtosControllerLocal );
                        conexaoTransaction.close();
                    }
                    else
                    {
                        DocumentoDao.rollBackTransaction( conexaoTransaction );
                        conexaoTransaction.close();
                        JOptionPane.showMessageDialog( null, "Falha ao actualizar o produto", "Falha", JOptionPane.WARNING_MESSAGE );
                        return;

                    }

                }
                catch ( Exception e )
                {
                    e.printStackTrace();

                    DocumentoDao.rollBackTransaction( conexaoTransaction );
                    JOptionPane.showMessageDialog( null, "Falha ao actualizar o produto", "Falha", JOptionPane.WARNING_MESSAGE );
                    return;
                }

            }

        }

    }

    private void procedimento_alterar_servico_produto_eliminar()
    {

        try
        {
            set_dados_eliminar();

        }
        catch ( Exception e )
        {
            e.printStackTrace();
            JOptionPane.showMessageDialog( null, e.getMessage(), "Falha ao desactivar produto", JOptionPane.ERROR_MESSAGE );
        }

    }

    private void set_combos()
    {
        cmbMarca.setModel( new DefaultComboBoxModel( marcasController.getVector() ) );
        cmbGrupo.setModel( new DefaultComboBoxModel( gruposController.getVectorIngredientes() ) );
        try
        {
            cmbModelo.setModel( new DefaultComboBoxModel( modelosController.getVectorByIdMarca( getIdMarca() ) ) );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }

        try
        {
            cmbFamilia.setModel( new DefaultComboBoxModel( familiasController.getVector() ) );
            cmbTipoProduto.setModel( new DefaultComboBoxModel( tipoProdutosController.getVectorByIdFamilia( getIdFamilia() ) ) );

        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
        cmbLocal.setModel( new DefaultComboBoxModel( localController.getVector() ) );
        cmbUnidade.setModel( new DefaultComboBoxModel( unidadesController.getVector() ) );

        try
        {
            cmbarmazem1.setModel( new DefaultComboBoxModel( armazensController.getVector( 1 ) ) );
            cmbarmazem2.setModel( new DefaultComboBoxModel( armazensController.getVector( 2 ) ) );
            cmbarmazem3.setModel( new DefaultComboBoxModel( armazensController.getVector( 3 ) ) );
            cmbarmazem4.setModel( new DefaultComboBoxModel( armazensController.getVector( 4 ) ) );

        }
        catch ( Exception e )
        {
        }

    }

    private void mostrar_qtd_stock_armazem( JTextField text, int idArmazem )
    {
        if ( Objects.nonNull( produto ) && idArmazem > 0 )
        {
            TbStock stockLocal1 = stoksController.getStockByIdProdutoAndIdArmazem( getCodigoProduto(), idArmazem );
//            TbStock stockLocal = stoksController.getStockByIdProdutoAndIdArmazem( produto.getCodigo(), idArmazem );

            if ( !Objects.isNull( stockLocal1 ) )
            {
//                txtQuantidadeStock.setText( String.valueOf( stockLocal.getQuantidadeExistente() ) );
                text.setText( String.valueOf( stockLocal1.getQuantidadeExistente() ) );
            }
            else
            {
                text.setText( "0.0" );
            }

        }

    }

    //actualizar
    private int getIdLocal()
    {
        try
        {
            return localController
                    .getTbLocalByDesignacao( cmbLocal.getSelectedItem().toString() )
                    .getCodigo();
        }
        catch ( Exception e )
        {
        }
        return 0;
    }

    private int getIdUnidade()
    {
        try
        {
            return unidadesController
                    .getUnidadeByDesignacao( cmbUnidade.getSelectedItem().toString() )
                    .getPkUnidade();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
        return 0;

    }

    private int getIdImposto()
    {

        try
        {
            return impostosController
                    .getImpostoByDesignacao( cmbImposto.getSelectedItem().toString() )
                    .getPkImposto();

        }
        catch ( Exception e )
        {
        }
        return 0;

    }

    private int getIdGrupo()
    {
        try
        {
            System.out.println( "ESTADO GRUPO: " + Objects.nonNull( gruposController ) );
            return gruposController
                    .getGrupoByDesignacao( cmbGrupo.getSelectedItem().toString() )
                    .getPkGrupo();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
        return 0;
    }

    private int getIdTipoPrdouto()
    {
        try
        {
            return tipoProdutosController
                    .getTipoFamiliaByDesignacao( cmbTipoProduto.getSelectedItem().toString() )
                    .getCodigo();
        }
        catch ( Exception e )
        {
        }
        return 0;
    }

    private static int getIdFamilia()
    {
        try
        {
            return familiasController
                    .getFamiliaByDesignacao( cmbFamilia.getSelectedItem().toString() )
                    .getPkFamilia();
        }
        catch ( Exception e )
        {
        }

        return 0;
    }

    private static int getIdMarca()
    {
        try
        {
            return marcasController
                    .getMarcaByDesignacao( cmbMarca.getSelectedItem().toString() )
                    .getPkMarca();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
        return 0;

    }

    private static int getIdModelo()
    {
        try
        {
            return modelosController
                    .getModeloByDesignacao( cmbMarca.getSelectedItem().toString() )
                    .getPkModelo();
        }
        catch ( Exception e )
        {
        }
        return 0;
    }

    public static void ver_dados( int codigo )
    {

        TbProduto produto_local = (TbProduto) produtosController.findById( codigo );
        produto = produto_local;
        if ( !Objects.isNull( produto_local ) )
        {
            mostrar_painel( produto_local );

            preco = (TbPreco) precosController.getLastIdPrecoByIdProduto( codigo, 1 );

            if ( Objects.isNull( preco ) )
            {
                return;
            }

            Integer pkModelo = produto_local.getFkModelo().getPkModelo();
            Modelo modeloLocal = (Modelo) modelosController.findById( pkModelo );

            Integer pkGrupo = produto_local.getFkGrupo().getPkGrupo();
            Grupo grupoLocal = (Grupo) gruposController.findById( pkGrupo );

            Integer pkMarca = modeloLocal.getFkMarca().getPkMarca();
            Marca marcaLocal = (Marca) marcasController.findById( pkMarca );

            Integer idTipoProduto = produto_local.getCodTipoProduto().getCodigo();
            TbTipoProduto tipoProdutoLocal = (TbTipoProduto) tipoProdutosController.findById( idTipoProduto );

            Integer pkFamilia = tipoProdutoLocal.getFkFamilia().getPkFamilia();
            Familia familiaLocal = (Familia) familiasController.findById( pkFamilia );

            String marca = marcaLocal.getDesignacao();
            String modelo = modeloLocal.getDesignacao();
            String grupo = grupoLocal.getDesignacao();

            String familia = familiaLocal.getDesignacao();
            String tipoProduto = tipoProdutoLocal.getDesignacao();

            cmbMarca.setSelectedItem( marca );
            cmbModelo.setSelectedItem( modelo );
            cmbGrupo.setSelectedItem( grupo );

            cmbFamilia.setSelectedItem( familia );
            cmbTipoProduto.setSelectedItem( tipoProduto );

            try
            {
                if ( !Objects.isNull( produto_local.getPhoto() ) )
                {
                    imageIcon = new ImageIcon( produto_local.getPhoto() );
                    imageIcon.setImage( imageIcon.getImage().getScaledInstance( 112, 109, 100 ) );
                    lbPhoto.setIcon( imageIcon );
                }
                else
                {
                    lbPhoto.setIcon( null );
                }
            }
            catch ( Exception e )
            {
                e.printStackTrace();
                lbPhoto.setIcon( null );
            }

            Unidade unidadeLocal = (Unidade) unidadesController.findById( produto_local.getCodUnidade().getPkUnidade() );
            cmbUnidade.setSelectedItem( unidadeLocal.getDescricao() );
            txtDesignacao.setText( produto_local.getDesignacao() );

            Double preco_compra = ( !Objects.isNull( preco ) ) ? preco.getPrecoCompra().doubleValue() : 0;

            txtPrecoCompra.setText( CfMethods.formatarComoPorcoes( preco_compra ) );

            txtCodigoBarra.setText( String.valueOf( produto_local.getCodBarra() ) );
            txtCodigoManual.setText( String.valueOf( produto_local.getCodigoManual() ) );
            txtCodigoBarraProcura.setText( String.valueOf( produto_local.getCodBarra() ) );
            txtCodigoManualProcura.setText( String.valueOf( produto_local.getCodigoManual() ) );
            jcDataExpiracao.setDate( produto_local.getDataExpiracao() );
            jcDataFabrico.setDate( produto_local.getDataFabrico() );
            txtUnidadeCompra.setText( String.valueOf(produto_local.getUnidadeCompra() ));

            Integer pkProduto = produto_local.getCodigo();
            boolean temIva = produtosImpostoController.existeProdutoImposto( pkProduto );

            if ( !temIva )
            {
                String regime = produtosIsentoController.getRegimeIsensaoByIdProduto( pkProduto );
                ivaMotivoJComboBox.setSelectedItem( regime );

            }

            ivaAplicarJRadioButton.setSelected( temIva );
            ivaNaoAplicarJRadioButton.setSelected( !temIva );

            boolean temRetencao = servicosRetencaoController.existeRetencao( pkProduto );

            if ( !temRetencao )
            {
                String retencao = servicosRetencaoController.getRetensaoByIdProduto( pkProduto );
                ivaMotivoJComboBox.setSelectedItem( retencao );

            }

            retencaoAplicarJRadioButton.setSelected( temRetencao );
            retencaoNaoAplicarJRadioButton.setSelected( !temRetencao );

            atualizarIvaForm();

            Double taxa = produtosImpostoController.getTaxaByIdProduto( pkProduto );
            cmbImposto.setSelectedItem( String.valueOf( taxa ) );
            System.out.println( "TAXA REAL: " + taxa );

            actualizarRetencaoForm();
            /**
             * A IDEIA BUSCAR O ULTIMO PRECO(GROSSO) E DEPOIS BUSCAR O PRECO
             * RETALHO EM FUNCAO DO PRECO GROSSO
             */
            TbPreco precoGrosso = precosController.getLastPreco( produto.getCodigo() );
            TbPreco precoRetalhoLocal = precosController.getLastIdPrecoByIdProduto( produto_local.getCodigo(), precoGrosso.getQtdBaixo() - 1 );
            double preco_venda_retalho = precoRetalhoLocal.getPrecoVenda().doubleValue();
            double preco_venda_grosso = precoGrosso.getPrecoVenda().doubleValue();
            double percentagem_ganho = precoRetalhoLocal.getPercentagemGanho().doubleValue();

            txtPrecoVendaRetalho.setText(
                    String.valueOf( CfMethods.formatarComoPorcoes( preco_venda_retalho ) )
            );
            txtPrecoDeVendaSemIva.setText(
                    String.valueOf( CfMethods.formatarComoPorcoes( preco_venda_retalho ) )
            );

            txtQtdGrosso.setText( String.valueOf( precoGrosso.getQtdBaixo() ) );
            txtPrecoVendaGrosso.setText(
                    String.valueOf( CfMethods.formatarComoPorcoes( preco_venda_grosso ) )
            );

            txtPercentagemGanhoRetalho.setText( CfMethods.formatarComoPorcoes( percentagem_ganho ) );
            txtCodigoProduto.setText( String.valueOf( produto_local.getCodigo() ) );

            if ( produto_local.getStocavel().equals( "true" ) )
            {
                ck_produto.setSelected( true );
            }
            else
            {
                ck_produto.setSelected( false );
            }

            setCozinha( produto_local.getCozinha() );
        }
        else
        {
            limpar_direito();
            JOptionPane.showMessageDialog( null, "O Produto ainda não existe. ", DVML.DVML_COMERCIAL, JOptionPane.INFORMATION_MESSAGE );

        }

    }

    private static void procedimento_limpar()
    {
        limpar_esquerdo();
        limpar_direito();
        //actualizar
        cmbLocal.setModel( new DefaultComboBoxModel( localController.getVector() ) );
        cmbUnidade.setModel( new DefaultComboBoxModel( unidadesController.getVector() ) );
//        ck_produto.setSelected( true );

    }

    private static void alterar_estrutura_janela()
    {

        if ( rbJanelaServico.isSelected() )
        {
            painel_stock.setVisible( false );
        }
        else
        {
            painel_stock.setVisible( true );
        }
    }

    private static void procedimento_limpar_campos()
    {
        limpar_esquerdo();
        limpar_direito_campos();
        //actualizar
        cmbLocal.setModel( new DefaultComboBoxModel( localController.getVector() ) );
        cmbUnidade.setModel( new DefaultComboBoxModel( unidadesController.getVector() ) );
//        ck_produto.setSelected( true );
//        servico_produto();
        btnSalvar.setEnabled( true );
        txtDesignacao.requestFocus();
    }

    private static void limpar_esquerdo()
    {
        lbPhoto.setIcon( null );
        txtDesignacao.setText( "" );
        txtPrecoCompra.setText( "" );

    }

    private static void limpar_direito()
    {

//        txtCodigoBarra.setText( "0" );
        txtPrecoVendaRetalho.setText( "" );
        txtPercentagemGanhoRetalho.setText( "" );
        txtCodigoManual.setText( "" );
        txtPrecoVendaGrosso.setText( "" );
        txtQtdGrosso.setText( "" );
        txtUnidadeCompra.setText( "" );

    }

    private static void limpar_direito_campos()
    {

        txtPrecoVendaRetalho.setText( "" );
        txtPrecoVendaRetalho.setText( "" );
        txtCodigoProduto.setText( "" );
        txtCodigoBarraProcura.setText( "" );
        txtPercentagemGanhoRetalho.setText( "" );
        txtPrecoDeVendaComIva.setText( "" );
        txtCodigoManual.setText( "" );
        txtUnidadeCompra.setText( "" );

    }

    public boolean exist_produto_stock( int codigo_produto )
    {
        try
        {
            return stoksController.existeStock( codigo_produto, DVML.ARMAZEM_DEFAUTL, conexao );
        }
        catch ( Exception e )
        {
            return false;
        }
    }

    private void buscar_by_cod_barra()
    {

        TbProduto produto_local = produtosController.findByCodBarra( txtCodigoBarraProcura.getText().trim() );
        if ( !Objects.isNull( produto_local ) )
        {
            ver_dados( produto_local.getCodigo() );
            txtCodigoBarraProcura.requestFocus();
            if ( ivaAplicarJRadioButton.isSelected() )
            {
                calcularTotalComIva();
            }
            else
            {
                txtPrecoDeVendaComIva.setVisible( false );
                ivaTaxaJLabel.setVisible( false );

            }
//            
//            mostrar_cod_dados_stock_armazem1cb( produto_local );
//            mostrar_cod_dados_stock_armazem2ci( produto_local );
//            mostrar_cod_dados_stock_armazem3ci( produto_local );
//            mostrar_cod_dados_stock_armazem4ci( produto_local );
        }
        else
        {
            procedimento_limpar();
        }
    }

    private void detalhe_produto()
    {

        Unidade unidade = (Unidade) unidadesController.findById( produto.getCodUnidade().getPkUnidade() );

        String output = "";
        TbPreco preco_local = precosController.getLastIdPrecoByIdProduto( produto.getCodigo(), 1 );
        output += "CodInterno           : " + produto.getCodigo() + "\n";
        output += "CodManual           : " + produto.getCodigoManual() + "\n";
        output += "Designação           : " + produto.getDesignacao() + "\n";
        output += "Unidade           : " + unidade.getDescricao() + "\n";
        if ( produto.getStocavel().equals( "true" ) )
        {
            output += "Preço da Compra      : " + preco_local.getPrecoCompra() + "\n";
            output += "Percentagem Ganho    : " + preco_local.getPercentagemGanho() + "  % \n";
            output += "Preço de Venda       : " + preco_local.getPrecoVenda() + "\n";
        }
        else
        {
            output += "Preço do Serviço      : " + preco_local.getPrecoCompra() + "\n";
        }

        JOptionPane.showMessageDialog( null, output );

    }

    private void setDadosPreco()
    {

        preco = new TbPreco();
        preco.setPrecoCompra( new BigDecimal( MetodosUtil.convertToDouble( txtPrecoCompra.getText() ) ) );

        if ( ck_produto.isSelected() )
        {
            preco.setPercentagemGanho( new BigDecimal( MetodosUtil.convertToDouble( txtPercentagemGanhoRetalho.getText() ) ) );

        }
        else
        {
            preco.setPercentagemGanho( new BigDecimal( "0" ) );
        }

        preco.setData( new Date() );
        preco.setHora( new Date() );
        preco.setFkProduto( new TbProduto( produto.getCodigo() ) );
        preco.setRetalho( true );

        preco.setFkUsuario( new TbUsuario( idUser ) );

    }

    private boolean campos_validos()
    {
        boolean isProduto = ck_produto.isSelected();

        System.out.println( "Status Produto: " + isProduto );
        MetodosUtil.getValorTransformadoString( preco_venda_anterior );

        String designacao_produto = getDesignacaoText();

        TbUsuario usuarioLocal = (TbUsuario) usuariosController.findById( idUser );

        if ( designacao_produto.equals( "" ) )
        {

            JOptionPane.showMessageDialog( null, "Caro usuário " + usuarioLocal.getNome() + " " + usuarioLocal.getSobreNome() + " por favor digite a designação do produto" );
            txtPrecoCompra.requestFocus();
            return false;

        }
        else if ( produtosController.exist_designacao_produto( getDesignacaoText() ) )
        {
            JOptionPane.showMessageDialog( null, "Já existe um produto com esta designação!!!", DVML.DVML_COMERCIAL, JOptionPane.WARNING_MESSAGE );
            txtDesignacao.requestFocus();
            return false;
        }
        else if ( produtosController.existProdutoByCodigoBarra( txtCodigoBarra.getText() ) && isProduto )
        {
            JOptionPane.showMessageDialog( null, "Este codigo de barra ja existe!!!", DVML.DVML_COMERCIAL, JOptionPane.WARNING_MESSAGE );
            txtCodigoBarra.requestFocus();
            txtCodigoBarra.setBackground( Color.YELLOW );
            return false;
        }
        else if ( txtPrecoCompra.getText().equals( "" ) )
        {

            JOptionPane.showMessageDialog( null, "Caro usuário " + usuarioLocal.getNome() + " " + usuarioLocal.getSobreNome() + " , insira o preço da compra do produto" );
            txtPrecoCompra.requestFocus();
            return false;

        }
        else if ( txtPrecoCompra.getText().equals( "0" ) )
        {
            JOptionPane.showMessageDialog( null, "Caro usuário " + usuarioLocal.getNome() + " " + usuarioLocal.getSobreNome() + ", o preço da compra não pode ser igual a zero(0)." );
            txtPrecoCompra.requestFocus();
            return false;
        }
        else if ( txtPrecoVendaRetalho.getText().equals( "" ) && isProduto )
        {

            JOptionPane.showMessageDialog( null, "Caro usuário " + usuarioLocal.getNome() + " " + usuarioLocal.getSobreNome() + " , insira o preço da venda do produto" );
            txtPrecoVendaRetalho.requestFocus();
            return false;

        }
        else if ( txtPrecoVendaRetalho.getText().equals( "0" ) && isProduto )
        {
            JOptionPane.showMessageDialog( null, "Caro usuário " + usuarioLocal.getNome() + " " + usuarioLocal.getSobreNome() + ",  o preço da venda não pode ser igual a zero(0)." );
            txtPrecoVendaRetalho.requestFocus();
            return false;
        }
        else if ( cmbUnidade.getSelectedItem().equals( "--SELECIONE--" ) )
        {
            JOptionPane.showMessageDialog( null, "Pf. Selecione a unidade de medida do produto", DVML.DVML_COMERCIAL, JOptionPane.WARNING_MESSAGE );
            return false;
        }
        else if ( ivaAplicarJRadioButton.isSelected() && cmbImposto.getSelectedItem().equals( "--SELECIONE--" ) )
        {
            JOptionPane.showMessageDialog( null, "Pf. Selecione a taxa do IVA", DVML.DVML_COMERCIAL, JOptionPane.WARNING_MESSAGE );
            return false;
        }
        else if ( cmbImposto.getSelectedItem().equals( "--SELECIONE--" ) && ivaAplicarJRadioButton.isSelected() )
        {
            JOptionPane.showMessageDialog( null, "Pf. Selecione a taxa do IVA do produto recomendado pela AGT", DVML.DVML_COMERCIAL, JOptionPane.WARNING_MESSAGE );
            return false;
        }

        if ( dadosInstituicao.getUsarDoisPrecos().equals( "sim" ) )
        {
            if ( !camposValidosQtdGrossoPrecoGrosso() )
            {
                return false;
            }

        }

        return true;
    }

    private boolean camposValidosQtdGrossoPrecoGrosso()
    {
        if ( txtQtdGrosso.getText().equals( "" ) )
        {
            txtQtdGrosso.requestFocus();
            JOptionPane.showMessageDialog( null, "Pf. digite a quantidade grosso.", DVML.DVML_COMERCIAL, JOptionPane.WARNING_MESSAGE );
            return false;
        }
        else if ( Double.parseDouble( txtQtdGrosso.getText() ) <= 1 )
        {
            txtQtdGrosso.requestFocus();
            JOptionPane.showMessageDialog( null, "A quantidade grosso, nao pode ser menor do que 2.", DVML.DVML_COMERCIAL, JOptionPane.WARNING_MESSAGE );
            return false;

        }
        else if ( txtPrecoVendaGrosso.getText().equals( "" ) )
        {
            txtPrecoVendaGrosso.requestFocus();
            JOptionPane.showMessageDialog( null, "Pf. Seleccione o preco de venda a grosso.", DVML.DVML_COMERCIAL, JOptionPane.WARNING_MESSAGE );
            return false;
        }
        else if ( MetodosUtil.convertToDouble( txtPrecoVendaGrosso.getText() ) == 0 )
        {
            txtPrecoVendaGrosso.requestFocus();
            JOptionPane.showMessageDialog( null, "O preco grosso nao pode ser zero..", DVML.DVML_COMERCIAL, JOptionPane.WARNING_MESSAGE );
            return false;
        }

        if ( MetodosUtil.convertToDouble( txtPrecoVendaGrosso.getText() ) < MetodosUtil.convertToDouble( txtPrecoCompra.getText() ) )
        {
            txtPrecoVendaGrosso.requestFocus();
            int opcao = JOptionPane.showConfirmDialog( null, "O preco grosso e menor que o preco de compra .." );

            if ( opcao == JOptionPane.YES_OPTION )
            {
                return true;

            }
            return false;
        }

        return true;
    }

    private void popularComponentes()
    {
        System.err.println( "listarMotivos (): " + listarMotivos() );
        cmbImposto.setModel( new DefaultComboBoxModel( impostosController.getVector() ) );
        ivaMotivoJComboBox.setModel( listarMotivos() );
        ivaMotivoJComboBox.setSelectedIndex( -1 );

        if ( !Objects.isNull( dadosInstituicao ) )
        {
            ivaMotivoJComboBox.setSelectedItem( dadosInstituicao.getRegime() );
        }

        atualizarIvaForm();

        MetodosUtil.FUNCAO_F1( this, rootPaneCheckingEnabled, DVML.ARMAZEM_DEFAUTL, DVML.JANELA_PRODUTO );

    }

    private DefaultComboBoxModel listarMotivos()
    {
        DefaultComboBoxModel boxModel = new DefaultComboBoxModel();

        List<ProdutosMotivosIsensao> ProdutosMotivosIsensao = produtosMotivosIsensaoController.listarTodos();

        for ( ProdutosMotivosIsensao isensao : ProdutosMotivosIsensao )
        {
            // boxModel.addElement ( isensao.getRegime ().substring(0, 4) );
            boxModel.addElement( isensao.getRegime() );
        }

        return boxModel;
    }

    private static void atualizarIvaForm()
    {
        try
        {
            ver_retencao();
            actualizar_campos();

        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }

//        TotalIvaLabel.setVisible( true );
//        txtPrecoDeVendaComIva.setVisible( true );
        cmbImposto.setVisible( ivaAplicarJRadioButton.isSelected() );
//        ivaTaxaJLabel.setVisible( cmbImposto.isVisible() );
        ivaMotivoJComboBox.setVisible( !cmbImposto.isVisible() );
        ivaMotivoJLabel.setVisible( ivaMotivoJComboBox.isVisible() );

        if ( !Objects.isNull( dadosInstituicao ) )
        {
            ivaMotivoJComboBox.setSelectedItem( dadosInstituicao.getRegime() );

        }

    }

    public static void actualizar_campos()
    {

        if ( !ivaAplicarJRadioButton.isSelected() )
        {

            TotalIvaLabel.setVisible( false );
            txtPrecoDeVendaComIva.setVisible( false );
        }
        else
        {
            TotalIvaLabel.setVisible( true );
            txtPrecoDeVendaComIva.setVisible( true );

        }

    }

    private void ivaAtualizar( int fkProduto, ProdutosImpostoController produtosImpostoControllerLocal, ProdutosIsentoController produtosIsentoControllerLocal )
    {

        if ( produtosImpostoControllerLocal.existeProdutoImposto( fkProduto ) )
        {
            produtosImpostoControllerLocal.eliminar( fkProduto );
        }

        if ( ivaAplicarJRadioButton.isSelected() )
        {

            ProdutoImposto produtoImposto = new ProdutoImposto();
            produtoImposto.setFkProduto( new TbProduto( fkProduto ) );
            produtoImposto.setFkImposto( new Imposto( getIdImposto() ) );
            produtosImpostoControllerLocal.salvar( produtoImposto );

        }
        else
        {

            String regimeIsencao = (String) ivaMotivoJComboBox.getSelectedItem();
            ProdutosMotivosIsensao isensao = produtosMotivosIsensaoController.getRegime( regimeIsencao );
            if ( !Objects.isNull( isensao ) )
            {
                produtosIsentoControllerLocal.eliminarByIdProduto( fkProduto );
            }
            ProdutoIsento produtoIsento = new ProdutoIsento();
            produtoIsento.setFkProduto( new TbProduto( fkProduto ) );
            produtoIsento.setFkProdutosMotivosIsensao( new ProdutosMotivosIsensao( isensao.getPkProdutosMotivosIsensao() ) );
            produtosIsentoControllerLocal.salvar( produtoIsento );

        }
    }

    private String getDesignacaoText()
    {

        String designacao_produto = txtDesignacao.getText();

        if ( designacao_produto.contains( "-" ) )
        {
            return designacao_produto.replaceAll( "-", " " );
        }
        return designacao_produto;
    }

    private void proximo_codigo( ProdutosController produtosControllerLocal )
    {
        try
        {
            TbProduto lastProduto = produtosControllerLocal.getLastProduto();
            int codProduto = 1;
            if ( Objects.nonNull( lastProduto ) )
            {
                codProduto = produtosControllerLocal.getLastProduto().getCodigo() + 1;
//                txtCodigoBarra.setText( String.valueOf( codProduto ) );
            }

            jLabelCodProduto.setText( "Cod. Prod: " + codProduto );
            txtCodigoBarra.setText( String.valueOf( codProduto ) );
        }
        catch ( Exception e )
        {
            jLabelCodProduto.setText( "Cod. Prod: " );
            txtCodigoBarra.setText( "" );
        }
    }

    private void setFocus( String focus )
    {
        if ( focus.equalsIgnoreCase( "Codigo Interno" ) )
        {
            txtCodigoProduto.requestFocus();
            txtCodigoBarra.setEnabled( true );

        }
        else
        {
            txtCodigoBarraProcura.requestFocus();

        }
    }

    private boolean registrar_preco( PrecosController precosControllerLocal )
    {

        if ( dadosInstituicao.getUsarDoisPrecos().equals( "nao" ) )
        {
            txtQtdGrosso.setText( String.valueOf( (int) DVML.QTD_DEFAULT ) );
            txtPrecoVendaGrosso.setText( txtPrecoVendaRetalho.getText() );
        }

        boolean isStocavel = ck_produto.isSelected();
        /*PRIMEIRO PRECO RETALHO*/
        setDadosPreco();
        preco.setQtdBaixo( 0 );
        preco.setQtdAlto( Integer.parseInt( txtQtdGrosso.getText() ) - 1 );
        BigDecimal precoVenda = new BigDecimal( MetodosUtil.convertToDouble( isStocavel ? txtPrecoVendaRetalho.getText() : "0.0" ) );
        BigDecimal precoVendaGrosso = new BigDecimal( MetodosUtil.convertToDouble( isStocavel ? txtPrecoVendaGrosso.getText() : "0.0" ) );
        BigDecimal precoCompra = new BigDecimal( MetodosUtil.convertToDouble( txtPrecoCompra.getText() ) );

        try
        {

            preco.setPrecoVenda( isStocavel ? precoVenda : precoCompra );
            precosControllerLocal.salvar( preco );

            /*SEGUNDO PRECO GROSSO*/
            setDadosPreco();
//        preco.setQtdBaixo( (int) DVML.QTD_DEFAULT );
            preco.setQtdBaixo( Integer.parseInt( txtQtdGrosso.getText() ) );
            preco.setQtdAlto( 214748364 );

            preco.setPrecoVenda( isStocavel ? precoVendaGrosso : precoCompra );
            precosControllerLocal.salvar( preco );

            return true;

        }
        catch ( Exception e )
        {
        }
        return false;

    }

    private void calcularIva()
    {

        if ( !txtPrecoCompra.getText().equals( "" ) )
        {

            try
            {
                double qtd = 1d;
                double taxa = Double.parseDouble( cmbImposto.getSelectedItem().toString() );
                double precoLocal = MetodosUtil.convertToDouble( txtPrecoVendaRetalho.getText() );
                double desconto = 0d;
                double valorComIVA = FinanceUtils.getValorComIVA( qtd, taxa, precoLocal, desconto );

                txtPrecoDeVendaComIva.setText( String.valueOf( valorComIVA ) );
            }
            catch ( Exception e )
            {
            }

        }

    }

    private void calcularSemIva()
    {

        if ( !txtPrecoCompra.getText().equals( "" ) )
        {

            try
            {
                double qtd = 1d;
                double taxa = Double.parseDouble( cmbImposto.getSelectedItem().toString() );
                double precoLocal = MetodosUtil.convertToDouble( txtPrecoVendaRetalho.getText() );

                txtPrecoDeVendaSemIva.setText( String.valueOf( precoLocal ) );
            }
            catch ( Exception e )
            {
            }

        }

    }

    //----------- evento do teclado ---------------------------------------
    // <editor-fold defaultstate="collapsed" desc="Generated Code">       
    class BuscaProduto implements KeyListener
    {

        String prefixo = "";

        public void keyPressed( KeyEvent evt )
        {
            if ( evt.getKeyCode() != KeyEvent.VK_BACK_SPACE && evt.getKeyCode() != KeyEvent.VK_ENTER )
            {
                char key = evt.getKeyChar();
                prefixo = txtCodigoProduto.getText().trim() + key;
                codigo = Integer.parseInt( prefixo );

                try
                {
                    ver_dados( codigo );
                    ver_dados_produtos( codigo );
                }
                catch ( Exception e )
                {
                }

            }
            else if ( evt.getKeyCode() == KeyEvent.VK_BACK_SPACE )
            {

                prefixo = prefixo.toString().trim().substring( 0, prefixo.length() );
                codigo = Integer.parseInt( prefixo );

                try
                {
                    ver_dados( codigo );
                    ver_dados_produtos( codigo );
                }
                catch ( Exception e )
                {
                    procedimento_limpar();
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
    class PercentagemGanhoRetalho implements KeyListener
    {

        String prefixo = "";

        public void keyPressed( KeyEvent evt )
        {

            double preco_compra = 0;
            double percentagem = 0;
            double preco_venda = 0;

            if ( evt.getKeyCode() != KeyEvent.VK_BACK_SPACE && evt.getKeyCode() != KeyEvent.VK_ENTER )
            {
                char key = evt.getKeyChar();

                prefixo = txtPercentagemGanhoRetalho.getText().trim() + key;
                preco_compra = MetodosUtil.convertToDouble( txtPrecoCompra.getText() );
                percentagem = Double.parseDouble( prefixo ) / 10;
                preco_venda = ( ( percentagem * preco_compra ) / 100 ) + preco_compra;
                txtPrecoVendaRetalho.setText( String.valueOf( MetodosUtil.preco_venda( percentagem, preco_compra ) ) );

            }
            else if ( evt.getKeyCode() == KeyEvent.VK_BACK_SPACE )
            {
                try
                {

                    prefixo = prefixo.toString().trim().substring( 0, prefixo.length() - 1 );
                    preco_compra = MetodosUtil.convertToDouble( txtPrecoCompra.getText() );
                    percentagem = Double.parseDouble( prefixo );
                    preco_venda = ( ( percentagem * preco_compra ) / 100 ) + preco_compra;
                    txtPrecoVendaRetalho.setText( String.valueOf( MetodosUtil.preco_venda( percentagem, preco_compra ) ) );

                }
                catch ( Exception e )
                {
                    txtPrecoVendaRetalho.setText( "" );
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
    class PrecoVendaPercentagem implements KeyListener
    {

        String prefixo = "";

        public void keyPressed( KeyEvent evt )
        {

            double preco_compra = 0;
            double percentagem = 0;
            double preco_venda = 0;

            if ( evt.getKeyCode() != KeyEvent.VK_BACK_SPACE && evt.getKeyCode() != KeyEvent.VK_ENTER )
            {
                char key = evt.getKeyChar();

                System.err.println( "KEY: " + key );
                prefixo = txtPrecoVendaRetalho.getText().trim() + key;
//                preco_venda = MetodosUtil.convertToDouble( prefixo );
//                preco_venda = preco_venda * 10;
//                preco_compra = MetodosUtil.convertToDouble( txtPrecoCompra.getText() );
//                percentagem = ( ( preco_venda - preco_compra ) * 100 ) / preco_compra;
//                BigDecimal bigDecimal = new BigDecimal( percentagem / 10 );
//                bigDecimal = bigDecimal.setScale( 2, BigDecimal.ROUND_UP );
//                txtPercentagemGanhoRetalho.setText( String.valueOf( bigDecimal ) );

                preco_venda = MetodosUtil.convertToDouble( prefixo ) * 10;
                preco_compra = MetodosUtil.convertToDouble( txtPrecoCompra.getText() );
                txtPercentagemGanhoRetalho.setText( CfMethods.formatarComoPorcoes( MetodosUtil.percentagemGanho( preco_compra, preco_venda ) ) );

            }
            else if ( evt.getKeyCode() == KeyEvent.VK_BACK_SPACE )
            {
                try
                {

                    prefixo = prefixo.toString().trim().substring( 0, prefixo.length() - 1 );
//                    char key = evt.getKeyChar();
//                    prefixo = txtPrecoVenda.getText().trim() + key;
                    prefixo = txtPrecoVendaRetalho.getText().trim();
                    System.out.println( "PREFIXO: " + prefixo );

                    preco_venda = MetodosUtil.convertToDouble( prefixo.replace( ",", "." ) );

//                    preco_venda = MetodosUtil.convertToDouble( prefixo ) * 10;
                    preco_compra = MetodosUtil.convertToDouble( txtPrecoCompra.getText() );
                    txtPercentagemGanhoRetalho.setText( CfMethods.formatarComoPorcoes( MetodosUtil.percentagemGanho( preco_compra, preco_venda ) ) );

                    System.out.println( "PRECO VENDA BACK " + preco_venda );
//                    preco_venda = preco_venda * 10;
//                    percentagem = ( ( preco_venda - preco_compra ) * 100 ) / preco_compra;
//                    BigDecimal bigDecimal = new BigDecimal( percentagem );
//                    bigDecimal = bigDecimal.setScale( 2, BigDecimal.ROUND_UP );
//                    txtPercentagemGanhoRetalho.setText( String.valueOf( bigDecimal ) );

                }
                catch ( Exception e )
                {
                    txtPercentagemGanhoRetalho.setText( "" );

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

    class PrecoCompraPercentagem implements KeyListener
    {

        String prefixo = "";

        public void keyPressed( KeyEvent evt )
        {

            double preco_compra = 0;
            double percentagem = 0;
            double preco_venda = 0;

            if ( evt.getKeyCode() != KeyEvent.VK_BACK_SPACE && evt.getKeyCode() != KeyEvent.VK_ENTER )
            {
                char key = evt.getKeyChar();

                System.err.println( "KEY: " + key );
                prefixo = txtPrecoCompra.getText().trim() + key;
                preco_compra = MetodosUtil.convertToDouble( prefixo );
                preco_compra = preco_compra * 10;

                preco_venda = MetodosUtil.convertToDouble( ( txtPrecoVendaRetalho.getText().equals( "" ) || txtPrecoVendaRetalho.getText() == null ) ? "0.0" : txtPrecoVendaRetalho.getText() );

                percentagem = ( ( preco_venda - preco_compra ) * 100 ) / preco_compra;
                BigDecimal bigDecimal = new BigDecimal( percentagem / 10 );
                bigDecimal = bigDecimal.setScale( 2, BigDecimal.ROUND_UP );
                txtPercentagemGanhoRetalho.setText( String.valueOf( bigDecimal ) );
                txtPrecoVendaRetalho.setText( String.valueOf( prefixo ) );

            }
            else if ( evt.getKeyCode() == KeyEvent.VK_BACK_SPACE )
            {
                try
                {

                    prefixo = prefixo.toString().trim().substring( 0, prefixo.length() - 1 );
                    System.out.println( "PREFIXO: " + prefixo );
                    preco_compra = MetodosUtil.convertToDouble( prefixo );
                    preco_compra = preco_compra * 10;

                    percentagem = ( ( preco_venda - preco_compra ) * 100 ) / preco_compra;
                    BigDecimal bigDecimal = new BigDecimal( percentagem );
                    bigDecimal = bigDecimal.setScale( 2, BigDecimal.ROUND_UP );
                    txtPercentagemGanhoRetalho.setText( String.valueOf( bigDecimal ) );
                    txtPrecoVendaRetalho.setText( String.valueOf( prefixo ) );

                }
                catch ( Exception e )
                {
                    txtPercentagemGanhoRetalho.setText( "" );
                    txtPrecoVendaRetalho.setText( "" );

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
    // </editor-fold>

    public static int getCodigoArmazem( String designacao )
    {
        try
        {
            return armazensController.getArmazemByDesignacao( designacao ).getCodigo();
        }
        catch ( Exception e )
        {
            return 0;
        }
    }

    private static void actualizarRetencaoForm()
    {

        if ( retencaoAplicarJRadioButton.isSelected() )
        {
            retencao = (Retencao) retencaoController.findById( 1 );
            retencaoTaxaJTextField.setText( MetodosUtil.formatarComoPercentagem( retencao.getTaxa() ) );
        }
        else
        {
            retencao = (Retencao) retencaoController.findById( 2 );
            retencaoZeroTaxaJTextField.setText( MetodosUtil.formatarComoPercentagem( 0.0 ) );
        }
        retencaoTaxaJTextField.setVisible( retencaoAplicarJRadioButton.isSelected() );
        retencaoTaxaJLabel.setVisible( retencaoTaxaJTextField.isVisible() );
        retencaoZeroTaxaJTextField.setVisible( !retencaoTaxaJTextField.isVisible() );

    }

    public static void ver_retencao()
    {

        if ( ck_servico.isSelected() )
        {
            jPanel_retencao.setVisible( true );
        }
        else
        {
            jPanel_retencao.setVisible( false );
        }

    }

    public static void nao_ver_retencao()
    {
        if ( !ck_servico.isSelected() )
        {
            jPanel_retencao.setVisible( false );
        }

    }

    private void setRegime( String regime )
    {
        if ( regime.equalsIgnoreCase( "Regime Geral" ) )
        {
            ivaAplicarJRadioButton.setSelected( true );
            txtPrecoDeVendaComIva.setVisible( true );
            TotalIvaLabel.setVisible( true );

        }
        else
        {
            ivaNaoAplicarJRadioButton.setSelected( true );
            txtPrecoDeVendaComIva.setVisible( false );
            TotalIvaLabel.setVisible( false );

        }
    }

    public static void bloquearCampoCodBarra()
    {

        txtCodigoBarra.setEnabled( true );

    }

    private void setFocusCodigo( String focus )
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

    public static void getDescricao()
    {

        txtDesignacao.requestFocus();

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

    public void configurar_dois_precos()
    {
        try
        {
            if ( dadosInstituicao.getUsarDoisPrecos().equals( "nao" ) )
            {
                jLabel1.setVisible( false );
                txtQtdGrosso.setVisible( false );
                lbPrecoVenda1.setVisible( false );
                txtPrecoVendaGrosso.setVisible( false );

            }
            else
            {
                jLabel1.setVisible( true );
                txtQtdGrosso.setVisible( true );
                lbPrecoVenda1.setVisible( true );
                txtPrecoVendaGrosso.setVisible( true );

            }
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }

    }

    private void setActivarNegocio( String negocio )
    {
        if ( negocio.equalsIgnoreCase( "Comercial" ) )
        {

            ck_produto.setSelected( true );
            ck_servico.setSelected( false );
            lbTicket.setVisible( false );
            rbNaoEnviarCozinha.setVisible( false );
            rbEnviarCozinha.setVisible( false );
            rbEnviarSala.setVisible( false );
            btnAssociar.setVisible( false );
            servico_produto();
        }
        else if ( negocio.equalsIgnoreCase( "Transportes" ) )
        {

            ck_produto.setSelected( true );
            ck_servico.setSelected( false );
            lbTicket.setVisible( false );
            rbNaoEnviarCozinha.setVisible( false );
            rbEnviarCozinha.setVisible( false );
            rbEnviarSala.setVisible( false );
            btnAssociar.setVisible( false );
            servico_produto();
        }
        else if ( negocio.equalsIgnoreCase( "Restaurante" ) )
        {

            ck_produto.setSelected( true );
            ck_servico.setSelected( false );
//            lbTicket.setVisible( false );
            btnAssociar.setVisible( false );
//            rbEnviarCozinha.setVisible(false );
            servico_produto();
        }
        else if ( negocio.equalsIgnoreCase( "Farmacia" ) )
        {

            ck_produto.setSelected( true );
            ck_servico.setSelected( false );
            lbTicket.setVisible( false );
            rbNaoEnviarCozinha.setVisible( false );
            rbEnviarSala.setVisible( false );
            rbEnviarCozinha.setVisible( false );
            btnAssociar.setVisible( false );
            servico_produto();
        }
        else if ( negocio.equalsIgnoreCase( "Lavandaria" ) )
        {

            ck_produto.setSelected( false );
            ck_produto.setVisible( false );
            ck_servico.setSelected( true );
//            lbTipoProduto1.setVisible( false );
//            rbNaoEnviarCozinha.setVisible( false );
//            rbEnviarCozinha.setVisible( false );
            servico_produto();
//            ck_servico.setSelected( true);
        }

        else
        {

            ck_servico.setSelected( true );
            ck_produto.setSelected( false );
            lbTicket.setVisible( false );
            rbNaoEnviarCozinha.setVisible( false );
            rbEnviarCozinha.setVisible( false );
            rbEnviarSala.setVisible( false );
            btnAssociar.setVisible( false );
            servico_produto();
//            ivaNaoAplicarJRadioButton.setSelected( true);
            ver_retencao();
            actualizarRetencaoForm();
//            servico_produto();
//            txtDesignacao.requestFocus();
////        lbCusto.setText("Preço");
//            mostrarPreco();

        }
    }

    private void setActivarJanelaServico( String negocio )
    {
        try
        {
            if ( negocio.equalsIgnoreCase( "Manter fixa" ) )
            {
                rbJanelaServico.setSelected( true );
//            alterar_estrutura_janela();
            }
            else
            {
                rbJanelaServico.setSelected( false );
//            alterar_estrutura_janela();
            }
        }
        catch ( Exception e )
        {
        }

    }

    private String getCozinha()
    {
//        return rbComercial.isSelected() ? "Comercial" : "Oficina";
        return rbNaoEnviarCozinha.isSelected() ? "Nao Enviar Ticket"
                : rbEnviarCozinha.isSelected() ? "Enviar Ticket"
                : "Enviar Sala";

    }

    private String getCozinha1()
    {
        return rbNaoEnviarCozinha.isSelected() ? "Nao Enviar Ticket" : "Enviar Ticket";
    }

    private static void setCozinha( String cozinha )
    {
        if ( cozinha.equalsIgnoreCase( "Enviar Ticket" ) )
        {
            rbEnviarCozinha.setSelected( true );
            rbNaoEnviarCozinha.setSelected( false );
            rbEnviarSala.setSelected( false );
        }
        else if ( cozinha.equalsIgnoreCase( "Nao Enviar Ticket" ) )
        {
            rbNaoEnviarCozinha.setSelected( true );
            rbEnviarCozinha.setSelected( false );
            rbEnviarSala.setSelected( false );
        }
        else
        {
            rbNaoEnviarCozinha.setSelected( false );
            rbEnviarCozinha.setSelected( false );
            rbEnviarSala.setSelected( true );
        }
    }

    private static void setButtonAssociar()
    {
        boolean status = !( txtCodigoProduto.getText().equals( "" ) );
        btnAssociar.setVisible( status );

    }

    private void novo()
    {

        limpar_esquerdo();
        limpar_direito();
        btnSalvar.setEnabled( true );
        btnAlterar2.setEnabled( false );
        //actualizar
        cmbLocal.setModel( new DefaultComboBoxModel( localController.getVector() ) );
        cmbUnidade.setModel( new DefaultComboBoxModel( unidadesController.getVector() ) );
        ck_servico.setSelected( true );
        txtCodigoProduto.setText( "" );
        txtCodigoBarraProcura.setText( "" );
        txtCodigoManualProcura.setText( "" );

    }

    private void setWindowsListener()
    {

        this.addWindowListener( new WindowAdapter()
        {
            @Override
            public void windowActivated( WindowEvent e )
            {
//                proximo_codigo( produtosController );
            }

        } );

    }

    private void activarCampoPreco()
    {
        TbUsuario usuario = (TbUsuario) usuariosController.findById( idUser );

        if ( usuario.getIdTipoUsuario().getIdTipoUsuario() == 1 )
        {
//            txtPrecoVendaRetalho.setEnabled( true );
            txtPrecoVendaGrosso.setEnabled( true );
            txtPrecoCompra.setEnabled( true );
        }
        else
        {
            txtPrecoCompra.setEnabled( false );
//            txtPrecoVendaRetalho.setEnabled( false );
            txtPrecoVendaGrosso.setEnabled( false );
        }
    }

}
