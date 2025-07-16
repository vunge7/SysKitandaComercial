/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package visao;

import controller.ProdutoController;
import controller.StockController;
import dao.*;

import entity.*;
import exemplos.PermitirNumeros;
import java.awt.Color;
import java.awt.Frame;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import static kitanda.util.CfConstantes.YYYYMMDD_HHMMSS;
import kitanda.util.CfMethods;
import modelo.ProdutoModelo;
import modelo.StockModelo;
import util.BDConexao;
import util.DVML;
import util.JPAEntityMannagerFactoryUtil;
import util.MetodosUtil;
import static util.MetodosUtil.rodarComandoWindows;
import util.NumeroDocument;
import util.PictureChooser;

public class ReactivarProdutosVisao extends javax.swing.JFrame
{
    
    private static EntityManagerFactory emf = JPAEntityMannagerFactoryUtil.em;
    private static DadosInstituicaoDao dadosInstituicaoDao = new DadosInstituicaoDao( emf );
    private static ProdutoModelo produtoModelo;
    private static TbProduto produto;
    private static TbProduto produto1;
    private static ProdutoImposto produto_imposto;
    private static Retencao retencao;
    private static ProdutoDao produtoDao = new ProdutoDao( emf );
//    private DadosInstituicaoDao dadosInstituicaoDao = new DadosInstituicaoDao( emf );
    private static ProdutosMotivosIsensaoDao produtosMotivosIsensaoDao;
    private static UsuarioDao usuarioDao = new UsuarioDao( emf );
    private static PrecoDao precoDao = new PrecoDao( emf );
    private static LocalDao localDao = new LocalDao( emf );
    private static ImpostoDao impostoDao = new ImpostoDao( emf );
    private static UnidadeDao unidadeDao = new UnidadeDao( emf );
    private static StockDao stockDao = new StockDao( emf );
    private static TbStock stock;
    private static TbStock stock_publico;
    public static TbDadosInstituicao dadosInstituicao;
    private static FornecedorDao fornecedorDao = new FornecedorDao( emf );
    private static ArmazemDao armazemDao = new ArmazemDao( emf );
    private static FamiliaDao familiaDao = new FamiliaDao( emf );
    private static MarcaDao marcaDao = new MarcaDao( emf );
    private static GrupoDao grupoDao = new GrupoDao( emf );
    private static ModeloDao modeloDao = new ModeloDao( emf );
    private static TipoProdutoDao tipoProdutoDao = new TipoProdutoDao( emf );
    private PictureChooser image_View = new PictureChooser();
    private static BDConexao conexao;
    private static ProdutoController produtoController;
    private static int codigo = 0, idUser = 0;
//    public int idUser = 0;
    private Frame parent;
    private boolean stocavel = true;
    private double preco_compra_anterior = 0, preco_venda_anterior = 0;
    private static TbPreco preco;
    private static ImageIcon imageIcon;
    private Vector<String> lista_armazem1 = new Vector<>();
    private Vector<String> lista_armazem2 = new Vector<>();
    private Vector<String> lista_armazem3 = new Vector<>();
    private Vector<String> lista_armazem4 = new Vector<>();
    
    static
    {
        produtosMotivosIsensaoDao = new ProdutosMotivosIsensaoDao( emf );
    }
    private Imposto imposto;
    
    public ReactivarProdutosVisao( java.awt.Frame parent, boolean modal, int idUSer, BDConexao conexao )
    {
        
        this.parent = parent;
        initComponents();
        jPanel5.setVisible( false);
        jButton3.setEnabled(false);
        painel_stock.setVisible( false);
        dadosInstituicao = dadosInstituicaoDao.findTbDadosInstituicao( 1 );
        confiLabel();
        
        setLocationRelativeTo( null );
        this.idUser = idUSer;
        
        set_combos();
        lista_armazem1 = armazemDao.buscaTodos_1();
        lista_armazem2 = armazemDao.buscaTodos_2();
        lista_armazem3 = armazemDao.buscaTodos_3();
        lista_armazem4 = armazemDao.buscaTodos_4();
        
        cmbarmazem1.setModel( new DefaultComboBoxModel( lista_armazem1 ) );
        cmbarmazem2.setModel( new DefaultComboBoxModel( lista_armazem2 ) );
        cmbarmazem3.setModel( new DefaultComboBoxModel( lista_armazem3 ) );
        cmbarmazem4.setModel( new DefaultComboBoxModel( lista_armazem4 ) );
        setRegime( dadosInstituicaoDao.findTbDadosInstituicao( 1 ).getRegime() );
        txtPrecoDeVendaComIva.setVisible( false );
        TotalIvaLabel.setVisible( false );
        cmbImposto.setVisible( false );
//        TotalIvaLabel.setVisible( false );
//        txtPrecoDeVendaComIva.setVisible( false );

        this.conexao = conexao;
        jPanel_retencao.setVisible( true );
        ck_produto.setSelected( true );
        servico_produto();
        actualizarRetencaoForm();
        proximo_codigo();
        
        produtoController = new ProdutoController( this.conexao );
        txtCodigoProduto.setDocument( new PermitirNumeros() );
        jcDataFabrico.setDate( new Date() );
        jcDataExpiracao.setDate( new Date() );
        txtPrecoCompra.addKeyListener( new PrecoCompraPercentagem() );
        txtPrecoVendaRetalho.addKeyListener( new PrecoVendaPercentagem() );
        setFocus( dadosInstituicaoDao.findTbDadosInstituicao( 1 ).getFoco() );
        txtPrecoCompra.setDocument( new NumeroDocument( 12, DVML.CASAS_DECIMAIS ) );
        txtPrecoCompra.setHorizontalAlignment( JTextField.RIGHT );
        txtPrecoVendaRetalho.setDocument( new NumeroDocument( 12, DVML.CASAS_DECIMAIS ) );
        txtPrecoVendaRetalho.setHorizontalAlignment( JTextField.RIGHT );
        
        txtPercentagemGanhoRetalho.setDocument( new NumeroDocument( 12, DVML.CASAS_DECIMAIS ) );
        txtPercentagemGanhoRetalho.setHorizontalAlignment( JTextField.RIGHT );
        
        txtPrecoVendaGrosso.setDocument( new NumeroDocument( 12, DVML.CASAS_DECIMAIS ) );
        txtPrecoVendaGrosso.setHorizontalAlignment( JTextField.RIGHT );
//        txtPrecoDeVendaComIva.setVisible( false );
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
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     *
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        buttonGroup3 = new javax.swing.ButtonGroup();
        buttonGroup4 = new javax.swing.ButtonGroup();
        jPanel5 = new javax.swing.JPanel();
        lbTipoProduto = new javax.swing.JLabel();
        cmbTipoProduto = new javax.swing.JComboBox();
        lbCusto = new javax.swing.JLabel();
        txtPrecoCompra = new javax.swing.JTextField();
        ck_produto = new javax.swing.JCheckBox();
        lbUnidade = new javax.swing.JLabel();
        cmbUnidade = new javax.swing.JComboBox<>();
        ck_servico = new javax.swing.JCheckBox();
        lbTipoProduto1 = new javax.swing.JLabel();
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
        jPanel1 = new javax.swing.JPanel();
        ivaJPanel = new javax.swing.JPanel();
        ivaMotivoJLabel = new javax.swing.JLabel();
        ivaMotivoJComboBox = new javax.swing.JComboBox();
        ivaAplicarJRadioButton = new javax.swing.JRadioButton();
        ivaNaoAplicarJRadioButton = new javax.swing.JRadioButton();
        cmbImposto = new javax.swing.JComboBox<>();
        ivaTaxaJLabel = new javax.swing.JLabel();
        jPanel_retencao = new javax.swing.JPanel();
        retencaoAplicarJRadioButton = new javax.swing.JRadioButton();
        retencaoNaoAplicarJRadioButton = new javax.swing.JRadioButton();
        retencaoTaxaJTextField = new javax.swing.JTextField();
        retencaoTaxaJLabel = new javax.swing.JLabel();
        retencaoZeroTaxaJTextField = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        txtCodigoProduto = new javax.swing.JTextField();
        lbProdutos2 = new javax.swing.JLabel();
        txtCodigoBarraProcura = new javax.swing.JTextField();
        lbProdutos3 = new javax.swing.JLabel();
        lbProdutos4 = new javax.swing.JLabel();
        txtCodigoManualProcura = new javax.swing.JTextField();
        jButton4 = new javax.swing.JButton();
        painel_stock = new javax.swing.JPanel();
        llbDataFabrico = new javax.swing.JLabel();
        jcDataFabrico = new com.toedter.calendar.JDateChooser();
        lbDataExpiracao = new javax.swing.JLabel();
        jcDataExpiracao = new com.toedter.calendar.JDateChooser();
        lbPreco = new javax.swing.JLabel();
        txtPercentagemGanhoRetalho = new javax.swing.JTextField();
        lbStatus1 = new javax.swing.JLabel();
        lbPrecoVenda = new javax.swing.JLabel();
        txtPrecoVendaRetalho = new javax.swing.JTextField();
        lbCategoria3 = new javax.swing.JLabel();
        cmbLocal = new javax.swing.JComboBox<>();
        lbBarra1 = new javax.swing.JLabel();
        txtCodigoManual = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        lbPhoto = new javax.swing.JLabel();
        btnCarregar = new javax.swing.JButton();
        txtPrecoDeVendaComIva = new javax.swing.JTextField();
        TotalIvaLabel = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
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
        jButton3 = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        lbProduto = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtDesignacao = new javax.swing.JTextArea();
        jLabel2 = new javax.swing.JLabel();
        cmbStatus = new javax.swing.JComboBox();
        jButton5 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("...::::: KITANDA - REACTIVAR PRODUTO/SERVIÇO ::::...");

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lbTipoProduto.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 16)); // NOI18N
        lbTipoProduto.setText("Sub Família:");
        jPanel5.add(lbTipoProduto, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 90, 90, 22));

        cmbTipoProduto.setBackground(new java.awt.Color(4, 154, 3));
        cmbTipoProduto.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 14)); // NOI18N
        cmbTipoProduto.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbTipoProdutoActionPerformed(evt);
            }
        });
        jPanel5.add(cmbTipoProduto, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 90, 170, -1));

        lbCusto.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 16)); // NOI18N
        lbCusto.setText("Preço Compra Retalho:");
        jPanel5.add(lbCusto, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 270, 160, 30));

        txtPrecoCompra.setBackground(new java.awt.Color(4, 154, 3));
        txtPrecoCompra.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        txtPrecoCompra.setForeground(new java.awt.Color(255, 255, 255));
        txtPrecoCompra.setCaretColor(new java.awt.Color(255, 255, 255));
        txtPrecoCompra.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtPrecoCompraActionPerformed(evt);
            }
        });
        jPanel5.add(txtPrecoCompra, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 270, 130, 30));

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

        lbUnidade.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 16)); // NOI18N
        lbUnidade.setText("Unidade:");
        jPanel5.add(lbUnidade, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 270, 90, 30));

        cmbUnidade.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbUnidadeActionPerformed(evt);
            }
        });
        jPanel5.add(cmbUnidade, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 270, 160, -1));

        buttonGroup2.add(ck_servico);
        ck_servico.setText("Serviço");
        ck_servico.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ck_servicoActionPerformed(evt);
            }
        });
        jPanel5.add(ck_servico, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 10, -1, -1));

        lbTipoProduto1.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 16)); // NOI18N
        lbTipoProduto1.setText("Grupo:");
        jPanel5.add(lbTipoProduto1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, 70, 22));

        cmbFamilia.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbFamiliaActionPerformed(evt);
            }
        });
        jPanel5.add(cmbFamilia, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 90, 190, -1));

        lbTipoProduto2.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 16)); // NOI18N
        lbTipoProduto2.setText("Marca:");
        jPanel5.add(lbTipoProduto2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 70, 22));

        cmbMarca.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbMarcaActionPerformed(evt);
            }
        });
        jPanel5.add(cmbMarca, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 50, 190, -1));

        lbTipoProduto3.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 16)); // NOI18N
        lbTipoProduto3.setText("Modelo:");
        jPanel5.add(lbTipoProduto3, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 50, 90, 22));

        cmbModelo.setBackground(new java.awt.Color(4, 154, 3));
        cmbModelo.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 14)); // NOI18N
        cmbModelo.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbModeloActionPerformed(evt);
            }
        });
        jPanel5.add(cmbModelo, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 50, 230, -1));

        lbTipoProduto4.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 16)); // NOI18N
        lbTipoProduto4.setText("Família:");
        jPanel5.add(lbTipoProduto4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, 70, 22));

        jPanel5.add(cmbGrupo, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 130, 190, -1));

        jLabelCodProduto.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        jLabelCodProduto.setText("Codigo");
        jPanel5.add(jLabelCodProduto, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 20, 160, -1));

        lbBarra.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 16)); // NOI18N
        lbBarra.setText("Cod Barra:");
        jPanel5.add(lbBarra, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 130, 80, 20));

        txtCodigoBarra.setBackground(new java.awt.Color(4, 154, 3));
        txtCodigoBarra.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
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
        jPanel5.add(txtCodigoBarra, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 130, 230, -1));

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
        jPanel5.add(btnSalvar, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 530, 110, 50));

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
        jPanel5.add(btnAlterar2, new org.netbeans.lib.awtextra.AbsoluteConstraints(122, 530, 110, 50));

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Impostos"));

        ivaJPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Aplicar IVA ao Produto", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 16))); // NOI18N
        ivaJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        ivaMotivoJLabel.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 12)); // NOI18N
        ivaMotivoJLabel.setForeground(new java.awt.Color(255, 0, 0));
        ivaMotivoJLabel.setText("Motivos");
        ivaJPanel.add(ivaMotivoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 50, 160, -1));

        ivaMotivoJComboBox.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 16)); // NOI18N
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
        ivaJPanel.add(ivaMotivoJComboBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 70, 440, 30));

        buttonGroup1.add(ivaAplicarJRadioButton);
        ivaAplicarJRadioButton.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 16)); // NOI18N
        ivaAplicarJRadioButton.setText("Sim ( Aplicar IVA )");
        ivaAplicarJRadioButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ivaAplicarJRadioButtonActionPerformed(evt);
            }
        });
        ivaJPanel.add(ivaAplicarJRadioButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 210, -1));

        buttonGroup1.add(ivaNaoAplicarJRadioButton);
        ivaNaoAplicarJRadioButton.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 16)); // NOI18N
        ivaNaoAplicarJRadioButton.setSelected(true);
        ivaNaoAplicarJRadioButton.setText("Não (Não aplicar IVA)");
        ivaNaoAplicarJRadioButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ivaNaoAplicarJRadioButtonActionPerformed(evt);
            }
        });
        ivaJPanel.add(ivaNaoAplicarJRadioButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 20, 280, -1));

        cmbImposto.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbImposto.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbImpostoActionPerformed(evt);
            }
        });
        ivaJPanel.add(cmbImposto, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 150, -1));

        ivaTaxaJLabel.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 12)); // NOI18N
        ivaTaxaJLabel.setForeground(new java.awt.Color(255, 0, 0));
        ivaTaxaJLabel.setText("Taxa IVA ( % )");
        ivaJPanel.add(ivaTaxaJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, 160, -1));

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

        jPanel5.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 310, 630, 220));

        jButton1.setText("+");
        jButton1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel5.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(583, 90, 50, 20));

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("BUSCA DO SERVIÇO/PRODUTO"));
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
        jPanel3.add(txtCodigoBarraProcura, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 20, 180, -1));

        lbProdutos3.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 16)); // NOI18N
        lbProdutos3.setText("Cód. Barra:");
        jPanel3.add(lbProdutos3, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 20, 90, -1));

        lbProdutos4.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 16)); // NOI18N
        lbProdutos4.setText("Cód. Manual:");
        jPanel3.add(lbProdutos4, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 20, 110, -1));

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
        jPanel3.add(txtCodigoManualProcura, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 20, 170, -1));

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/proucura.png"))); // NOI18N
        jButton4.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 20, 40, 30));

        painel_stock.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        llbDataFabrico.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 16)); // NOI18N
        llbDataFabrico.setText("Data Fabrico:");

        jcDataFabrico.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N

        lbDataExpiracao.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 16)); // NOI18N
        lbDataExpiracao.setText("Data Expiracao:");

        jcDataExpiracao.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N

        lbPreco.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 16)); // NOI18N
        lbPreco.setText("Percent. Ganho:");

        txtPercentagemGanhoRetalho.setBackground(new java.awt.Color(4, 154, 3));
        txtPercentagemGanhoRetalho.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        txtPercentagemGanhoRetalho.setForeground(new java.awt.Color(255, 255, 255));
        txtPercentagemGanhoRetalho.setText("0");
        txtPercentagemGanhoRetalho.setCaretColor(new java.awt.Color(255, 255, 255));
        txtPercentagemGanhoRetalho.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtPercentagemGanhoRetalhoActionPerformed(evt);
            }
        });

        lbStatus1.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 1, 16)); // NOI18N
        lbStatus1.setText("%");

        lbPrecoVenda.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 16)); // NOI18N
        lbPrecoVenda.setText("Preco Venda Ret:");

        txtPrecoVendaRetalho.setBackground(new java.awt.Color(4, 154, 3));
        txtPrecoVendaRetalho.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        txtPrecoVendaRetalho.setForeground(new java.awt.Color(255, 255, 255));
        txtPrecoVendaRetalho.setCaretColor(new java.awt.Color(255, 255, 255));
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

        jPanel4.setBackground(new java.awt.Color(0, 0, 0));
        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        lbPhoto.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lbPhoto.setForeground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbPhoto, javax.swing.GroupLayout.DEFAULT_SIZE, 109, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lbPhoto, javax.swing.GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE)
        );

        btnCarregar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/actualizar_1_32x32.png"))); // NOI18N
        btnCarregar.setEnabled(false);
        btnCarregar.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnCarregarActionPerformed(evt);
            }
        });

        txtPrecoDeVendaComIva.setEditable(false);
        txtPrecoDeVendaComIva.setBackground(new java.awt.Color(102, 102, 102));
        txtPrecoDeVendaComIva.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        txtPrecoDeVendaComIva.setForeground(new java.awt.Color(255, 255, 255));

        TotalIvaLabel.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 16)); // NOI18N
        TotalIvaLabel.setText("P.Venda com IVA");

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagem/caixa_32x_32.png"))); // NOI18N
        jButton2.setText("Compras");
        jButton2.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButton2ActionPerformed(evt);
            }
        });

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
                .addGroup(painel_stockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(painel_stockLayout.createSequentialGroup()
                        .addComponent(lbBarra1, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCodigoManual, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(painel_stockLayout.createSequentialGroup()
                        .addGroup(painel_stockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(llbDataFabrico, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbDataExpiracao))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(painel_stockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jcDataFabrico, javax.swing.GroupLayout.DEFAULT_SIZE, 159, Short.MAX_VALUE)
                            .addComponent(jcDataExpiracao, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(painel_stockLayout.createSequentialGroup()
                        .addComponent(lbCategoria3, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(painel_stockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(painel_stockLayout.createSequentialGroup()
                                .addGroup(painel_stockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(painel_stockLayout.createSequentialGroup()
                                        .addGap(36, 36, 36)
                                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnCarregar))
                            .addComponent(cmbLocal, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(painel_stockLayout.createSequentialGroup()
                        .addGroup(painel_stockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lbPrecoVenda, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lbPreco, javax.swing.GroupLayout.DEFAULT_SIZE, 133, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(painel_stockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(txtPercentagemGanhoRetalho)
                            .addComponent(txtPrecoVendaRetalho, javax.swing.GroupLayout.DEFAULT_SIZE, 135, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbStatus1, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(painel_stockLayout.createSequentialGroup()
                        .addComponent(TotalIvaLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtPrecoDeVendaComIva, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(painel_stockLayout.createSequentialGroup()
                        .addGroup(painel_stockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(lbPrecoVenda1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 109, Short.MAX_VALUE))
                        .addGap(25, 25, 25)
                        .addGroup(painel_stockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtPrecoVendaGrosso, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtQtdGrosso, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        painel_stockLayout.setVerticalGroup(
            painel_stockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, painel_stockLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(painel_stockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCarregar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(painel_stockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbCategoria3, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbLocal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(painel_stockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCodigoManual, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbBarra1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8)
                .addGroup(painel_stockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(llbDataFabrico, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jcDataFabrico, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(painel_stockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbDataExpiracao)
                    .addComponent(jcDataExpiracao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(painel_stockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbPreco)
                    .addComponent(txtPercentagemGanhoRetalho, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbStatus1, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(painel_stockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbPrecoVenda, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPrecoVendaRetalho, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(painel_stockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtPrecoDeVendaComIva, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TotalIvaLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(painel_stockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtQtdGrosso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(painel_stockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtPrecoVendaGrosso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbPrecoVenda1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(55, 55, 55))
        );

        txtArmazen1.setEditable(false);
        txtArmazen1.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        txtArmazen1.setForeground(new java.awt.Color(0, 102, 102));

        txtArmazen2.setEditable(false);
        txtArmazen2.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        txtArmazen2.setForeground(new java.awt.Color(0, 102, 102));

        txtArmazen3.setEditable(false);
        txtArmazen3.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        txtArmazen3.setForeground(new java.awt.Color(0, 102, 102));

        txtArmazen4.setEditable(false);
        txtArmazen4.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        txtArmazen4.setForeground(new java.awt.Color(0, 102, 102));

        cmbarmazem1.setEnabled(false);

        cmbarmazem2.setEnabled(false);

        cmbarmazem3.setEnabled(false);

        cmbarmazem4.setEnabled(false);

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens32x32/actualizar_32x32.png"))); // NOI18N
        jButton3.setText("Reactivar");
        jButton3.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButton3ActionPerformed(evt);
            }
        });

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

        lbProduto.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 16)); // NOI18N
        lbProduto.setText("Designação:");

        txtDesignacao.setColumns(20);
        txtDesignacao.setFont(new java.awt.Font("Monospaced", 1, 13)); // NOI18N
        txtDesignacao.setRows(5);
        jScrollPane1.setViewportView(txtDesignacao);

        jLabel2.setText("Status Activar");

        cmbStatus.setBackground(new java.awt.Color(4, 154, 3));
        cmbStatus.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 14)); // NOI18N
        cmbStatus.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Activo", "Desactivo" }));
        cmbStatus.setEnabled(false);

        jButton5.setText("Ver Produtos Desactivos");
        jButton5.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 11, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 981, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 650, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(cmbarmazem1, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtArmazen1, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(cmbarmazem2, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtArmazen2, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(cmbarmazem3, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(lbProduto, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 435, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel2)
                                        .addComponent(cmbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(13, 13, 13)
                                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(txtArmazen3, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(cmbarmazem4, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtArmazen4, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(painel_stock, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtArmazen1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtArmazen2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtArmazen3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtArmazen4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbarmazem1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbarmazem2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbarmazem3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbarmazem4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lbProduto, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(8, 8, 8)
                                .addComponent(cmbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                        .addGap(32, 32, 32)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
                            .addComponent(btnCancelar, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
                            .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(painel_stock, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
            Logger.getLogger(ReactivarProdutosVisao.class.getName() ).log( Level.SEVERE, null, ex );
        }
        

    }//GEN-LAST:event_cmbTipoProdutoActionPerformed

    private void txtCodigoProdutoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCodigoProdutoActionPerformed
        // TODO add your handling code here:
        try
        {
//            procedimento_esconder();
            btnSalvar.setEnabled( false );
            jButton3.setEnabled(true);
//            btnAlterar2.setEnabled( true );
            cmbStatus.setEnabled( true );
            String codInternoString = txtCodigoProduto.getText();
            Integer codigoInternoInt = ( codInternoString.isEmpty() ? 0 : Integer.parseInt( codInternoString ) );
            produto = produtoDao.getProdutoDesctivoByCodigoProduto( codigoInternoInt );
            
            ver_dados_produtos( getCodigoProduto() );
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
//            detalhe_produto();
//            detalhe_produto_por_armazem();
//            if ( stock.getQuantidadeExistente() <= 0 )
//            {
//                if(stock.getQuantidadeExistente() != 0){
            mostrar_cod_dados_stock_armazem1ci( produto );
//                }else{
//                    cmbarmazem1.setVisible( false);
//                    txtArmazen1.setVisible( false);
//                    
//                }

            mostrar_cod_dados_stock_armazem2ci( produto );
            mostrar_cod_dados_stock_armazem3ci( produto );
            mostrar_cod_dados_stock_armazem4ci( produto );

//            }
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
        txtCodigoBarraProcura.setText( "" );
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
    }//GEN-LAST:event_ivaAplicarJRadioButtonActionPerformed

    private void ivaNaoAplicarJRadioButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_ivaNaoAplicarJRadioButtonActionPerformed
    {//GEN-HEADEREND:event_ivaNaoAplicarJRadioButtonActionPerformed
        
        atualizarIvaForm();
        txtPrecoDeVendaComIva.setVisible( false );
        TotalIvaLabel.setVisible( false );
    }//GEN-LAST:event_ivaNaoAplicarJRadioButtonActionPerformed

    private void ivaMotivoJComboBoxActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_ivaMotivoJComboBoxActionPerformed
    {//GEN-HEADEREND:event_ivaMotivoJComboBoxActionPerformed
        // TODO add your handling code here:
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
            cmbTipoProduto.setModel( new DefaultComboBoxModel( (Vector) tipoProdutoDao.getDescricaoByIdFamilias( getIdFamilia() ) ) );
            
        }
        catch ( Exception e )
        {
        }
    }//GEN-LAST:event_cmbFamiliaActionPerformed

    private void cmbMarcaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbMarcaActionPerformed
        try
        {
            cmbModelo.setModel( new DefaultComboBoxModel( (Vector) modeloDao.getDescricaoByIdMarca( getIdMarca() ) ) );
            
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
            
            new BuscaNovoProdutoVisao( this, rootPaneCheckingEnabled, DVML.JANELA_PRODUTO ).show();
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

    private void cmbUnidadeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbUnidadeActionPerformed
        txtPrecoVendaRetalho.requestFocus();
    }//GEN-LAST:event_cmbUnidadeActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        new CategoriasLugarVisao().show();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        try
        {
            
//            new CompraVisao( idUser, this.conexao ).show();
            
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButton2ActionPerformed

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

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton5ActionPerformed
    {//GEN-HEADEREND:event_jButton5ActionPerformed
        new ListarProdutosDesactivosVisao(idUser ).show();
    }//GEN-LAST:event_jButton5ActionPerformed

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
            java.util.logging.Logger.getLogger(ReactivarProdutosVisao.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        catch ( InstantiationException ex )
        {
            java.util.logging.Logger.getLogger(ReactivarProdutosVisao.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        catch ( IllegalAccessException ex )
        {
            java.util.logging.Logger.getLogger(ReactivarProdutosVisao.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        catch ( javax.swing.UnsupportedLookAndFeelException ex )
        {
            java.util.logging.Logger.getLogger(ReactivarProdutosVisao.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
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

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                try
                {
                    ReactivarProdutosVisao dialog = new ReactivarProdutosVisao( new javax.swing.JFrame(), true, 15, new BDConexao() );
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
                    Logger.getLogger(ReactivarProdutosVisao.class.getName() ).log( Level.SEVERE, null, ex );
                }
            }
        } );
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private static javax.swing.JLabel TotalIvaLabel;
    private javax.swing.JButton btnAlterar2;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnCarregar;
    private javax.swing.JButton btnSalvar;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.ButtonGroup buttonGroup3;
    private javax.swing.ButtonGroup buttonGroup4;
    public static javax.swing.JCheckBox ck_produto;
    public static javax.swing.JCheckBox ck_servico;
    public static javax.swing.JComboBox<String> cmbFamilia;
    public static javax.swing.JComboBox<String> cmbGrupo;
    private static javax.swing.JComboBox<String> cmbImposto;
    public static javax.swing.JComboBox<String> cmbLocal;
    public static javax.swing.JComboBox<String> cmbMarca;
    public static javax.swing.JComboBox cmbModelo;
    public static javax.swing.JComboBox cmbStatus;
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
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabelCodProduto;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    public static javax.swing.JPanel jPanel5;
    private static javax.swing.JPanel jPanel_retencao;
    private javax.swing.JScrollPane jScrollPane1;
    public static com.toedter.calendar.JDateChooser jcDataExpiracao;
    public static com.toedter.calendar.JDateChooser jcDataFabrico;
    private javax.swing.JLabel lbBarra;
    private javax.swing.JLabel lbBarra1;
    private javax.swing.JLabel lbCategoria3;
    private javax.swing.JLabel lbCusto;
    private javax.swing.JLabel lbDataExpiracao;
    private static javax.swing.JLabel lbPhoto;
    private javax.swing.JLabel lbPreco;
    private javax.swing.JLabel lbPrecoVenda;
    private javax.swing.JLabel lbPrecoVenda1;
    private javax.swing.JLabel lbProduto;
    private javax.swing.JLabel lbProdutos2;
    private javax.swing.JLabel lbProdutos3;
    private javax.swing.JLabel lbProdutos4;
    private javax.swing.JLabel lbStatus1;
    private javax.swing.JLabel lbTipoProduto;
    private javax.swing.JLabel lbTipoProduto1;
    private javax.swing.JLabel lbTipoProduto2;
    private javax.swing.JLabel lbTipoProduto3;
    private javax.swing.JLabel lbTipoProduto4;
    private javax.swing.JLabel lbUnidade;
    private javax.swing.JLabel llbDataFabrico;
    public static javax.swing.JPanel painel_stock;
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
    public static javax.swing.JTextField txtPrecoVendaGrosso;
    public static javax.swing.JTextField txtPrecoVendaRetalho;
    public static javax.swing.JTextField txtQtdGrosso;
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
        
        String sql = ( "select MAX(codigo) from tb_produto" );
        
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
        
        TbProduto produto_local = produtoDao.getStockByCodigoManual( txtCodigoManualProcura.getText() );
        
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
    
    public String getStatus()
    {
        return String.valueOf( cmbStatus.getSelectedItem() );
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
    
    private void salvarProdutoSemStock()
    {
        StockModelo stockModelo = new StockModelo();
        stockModelo.setCod_armazem( 1 );
        stockModelo.setCodigo( 0 );
        stockModelo.setCodigo_produto( lastProduto() );
        stockModelo.setDataEntrada( "2016-05-10" );
        stockModelo.setPreco_venda( Double.parseDouble( txtPrecoCompra.getText() ) );
        stockModelo.setPreco_venda_fabrica( Double.parseDouble( txtPrecoCompra.getText() ) );
        stockModelo.setQuantidade( 214748364 );
        stockModelo.setQuantidadeBaixa( 0 );
        stockModelo.setQuantidadeCritica( 0 );
        stockModelo.setQuantidade_antiga( 0 );
        stockModelo.setQuantidade_existente( 214748364 );
        
        StockController stockController = new StockController( conexao );
        
        if ( stockController.operacao( 1, stockModelo ) )
        {
            
        }
        else
        {
        }
        
    }
    
    public void operacao( int operacao, String descricaoCerto, String descricaoErro ) throws SQLException
    {
        
        produtoModelo = new ProdutoModelo( getCodigoTipoProduto(), 1, getProduto(), getDataFabrico(), getDataExpiraco(), getDataEntrada(), "0", getStatus(), getPreco() );
        produtoModelo.setCodigo( getCodigoProduto() );
        
        if ( !campos_invalidos() )
        {
            if ( operacao == 1 )
            {
                if ( !exist_produto() )
                {
                    if ( produtoController.operacao( operacao, produtoModelo, this.stocavel ) )
                    {
                        salvarProdutoSemStock();
                        limpar_and_reset();
                        JOptionPane.showMessageDialog( null, "DADOS   " + descricaoCerto + " COM SUCESSO NA BD!..." );
                        txtCodigoBarra.setText( String.valueOf( produtoDao.getUltimaCodProduto() + 1 ) );
                    }
                    else
                    {
                        JOptionPane.showMessageDialog( null, "ERRO AO " + descricaoErro + "NA BD!...", "ERRO", JOptionPane.ERROR_MESSAGE );
                    }
                    
                }
                else
                {
                    JOptionPane.showMessageDialog( null, "ERRO JA EXISTE ESTE PRODUTO", "ERRO", JOptionPane.ERROR_MESSAGE );
                }
                
            }
            else
            {
                if ( produtoController.operacao( operacao, produtoModelo, this.stocavel ) )
                {
                    JOptionPane.showMessageDialog( null, "DADOS   " + descricaoCerto + " COM SUCESSO NA BD!..." );
                    txtCodigoBarra.setText( String.valueOf( produtoDao.getUltimaCodProduto() + 1 ) );
                    //limpar();
                }
                else
                {
                    JOptionPane.showMessageDialog( null, "ERRO AO " + descricaoErro + "NA BD!...", "ERRO", JOptionPane.ERROR_MESSAGE );
                }
            }
            
        }
        
    }
    
    public static void ver_dados_produtos( int codigo )
    {
        try
        {
//            TbProduto produto_local = produtoDao.findTbProduto( codigo );
            TbProduto produto_local = produtoDao.getProdutoDesctivoByCodigoProduto(codigo );
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
            procedimento_limpar();
            JOptionPane.showMessageDialog( null, e.getMessage(), "Este Produto esta activo", JOptionPane.WARNING_MESSAGE );
        }
        
    }
    
    private void limpar_and_reset()
    {
        txtDesignacao.setText( "" );
        txtPrecoCompra.setText( "" );
        txtDesignacao.requestFocus();
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
//            cmbFamilia.setSelectedItem( "Serviços" );
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
        
        boolean isProduto = ck_produto.isSelected();
        String designacao_produto = getDesignacaoText();
        produto.setDesignacao( designacao_produto );
        produto.setPreco( new BigDecimal( MetodosUtil.convertToDouble( txtPrecoCompra.getText() ) ) );
        produto.setCodTipoProduto( tipoProdutoDao.findTbTipoProduto( getIdTipoPrdouto() ) );
        produto.setFkModelo( modeloDao.findModelo( getIdModelo() ) );
        produto.setFkGrupo( grupoDao.findGrupo( getIdGrupo() ) );
        produto.setCodFornecedores( fornecedorDao.findTbFornecedor( 1 ) );
        produto.setCodLocal( localDao.findTbLocal( getIdLocal() ) );
        produto.setCodUnidade( unidadeDao.findUnidade( getIdUnidade() ) );
        produto.setCodigoManual( isProduto ? txtCodigoManual.getText() : "" );
        produto.setDataFabrico( isProduto ? jcDataFabrico.getDate() : new Date() );
        produto.setDataEntrada( new Date() );
        produto.setDataExpiracao( isProduto ? jcDataExpiracao.getDate() : new Date() );
        produto.setStocavel( isProduto ? "true" : "false" );
        produto.setCodBarra( produto.getStocavel().equals( "true" ) ? txtCodigoBarra.getText().trim() : "2147483647" );
//        produto.setCodBarra( txtCodigoBarra.getText() );
        produto.setStatus( cmbStatus.getSelectedItem().toString() );
        if ( ivaAplicarJRadioButton.isSelected() )
        {
            produto.setStatusIva( "true" );
        }
        else
        {
            produto.setStatusIva( "false" );
        }
        
    }
    
    private void preparar_produto_eliminar()
    {
//        
//        boolean isProduto = ck_produto.isSelected();
//        String designacao_produto = getDesignacaoText();
//        produto.setDesignacao( designacao_produto );
//        produto.setPreco( new BigDecimal( MetodosUtil.convertToDouble( txtPrecoCompra.getText() ) ) );
//        produto.setCodTipoProduto( tipoProdutoDao.findTbTipoProduto( getIdTipoPrdouto() ) );
//        produto.setFkModelo( modeloDao.findModelo( getIdModelo() ) );
//        produto.setFkGrupo( grupoDao.findGrupo( getIdGrupo() ) );
//        produto.setCodFornecedores( fornecedorDao.findTbFornecedor( 1 ) );
//        produto.setCodLocal( localDao.findTbLocal( getIdLocal() ) );
//        produto.setCodUnidade( unidadeDao.findUnidade( getIdUnidade() ) );
//        produto.setCodigoManual( isProduto ? txtCodigoManual.getText() : "" );
//        produto.setDataFabrico( isProduto ? jcDataFabrico.getDate() : new Date() );
//        produto.setDataEntrada( new Date() );
//        produto.setDataExpiracao( isProduto ? jcDataExpiracao.getDate() : new Date() );
//        produto.setStocavel( isProduto ? "true" : "false" );
//        produto.setCodBarra( produto.getStocavel().equals( "true" ) ? txtCodigoBarra.getText().trim() : "2147483647" );
//        produto.setCodBarra( txtCodigoBarra.getText() );
        produto.setStatus( cmbStatus.getSelectedItem().toString() );
//        if ( ivaAplicarJRadioButton.isSelected() )
//        {
//            produto.setStatusIva( "true" );
//        }
//        else
//        {
//            produto.setStatusIva( "false" );
//        }
        
    }
    
    private void set_dados_produto()
    {
        
        String codigoBarraText = txtCodigoBarra.getText();
        String codigoBarra = ( codigoBarraText.isEmpty() ) ? String.valueOf( new ProdutoDao().findTbProdutoEntities().size() ) : codigoBarraText;
        TbProduto tbProduto = ProdutoDao.findProdutoByCodigoBarra( codigoBarra );
        
        boolean isProduto = ck_produto.isSelected();

//        if ( Objects.isNull( tbProduto )  )
        if ( true )
        {
            System.out.println( "CHEGUEIIII AQUIIIII" );
            try
            {
                preparar_produto();
                if ( image_View.getBystebyteImg() != null )
                {
                    produto.setPhoto( image_View.getBystebyteImg() );
                }
            }
            catch ( Exception e )
            {
            }
        }
//        else
//        {
//            JOptionPane.showMessageDialog( null, "Este codigo de barra já existe!" );
//        }
    }
    
    private boolean campos_validos_alterar()
    {
        
        String codigoBarraText = txtCodigoBarra.getText();
        String codigoBarra = ( codigoBarraText.isEmpty() ) ? String.valueOf( new ProdutoDao().findTbProdutoEntities().size() ) : codigoBarraText;
        boolean isProduto = ck_produto.isSelected();
        
        if ( produtoDao.existProdutoByCodigoBarraExcepto( produto.getCodigo(), codigoBarra ) && isProduto )
        {
            JOptionPane.showMessageDialog( null, "Ja existe um produto com este codigo de barra." );
            txtCodigoBarra.requestFocus();
//            txtCodigoBarra.setBackground( Color.YELLOW );
            return false;
        }
        else if ( produtoDao.existProdutoByDesignacaoExcepto( produto.getCodigo(), getDesignacaoText() ) )
        {
            JOptionPane.showMessageDialog( null, "Ja existe um produto na base de dados com esta designacao!" );
            txtDesignacao.requestFocus();
//            txtDesignacao.setBackground( Color.YELLOW );
            return false;
        }
        
        if ( dadosInstituicao.getUsarDoisPrecos().equals( "sim" ) )
        {
            if ( !camposValidosQtdGrossoPrecoGrosso() )
            {
                return false;
            }
            
        }

//        txtCodigoBarra.setBackground( Color.GREEN );
//        txtDesignacao.setBackground( Color.GREEN );
        return true;
        
    }
    
    private void set_dados_produto_alterar()
    {
        
        String codigoBarraText = txtCodigoBarra.getText();
        String codigoBarra = ( codigoBarraText.isEmpty() ) ? String.valueOf( new ProdutoDao().findTbProdutoEntities().size() ) : codigoBarraText;
        
        System.out.println( "Codigo:>>>>>>>> " + produto.getCodigo() );
        System.out.println( "Codigo Barra:>>>>>>>> " + codigoBarra );
        System.out.println( "Designacao :>>>>>>>> " + getDesignacaoText() );
        
        if ( campos_validos_alterar() )
        {
            preparar_produto();
            try
            {
                if ( image_View.getBystebyteImg() != null )
                {
                    produto.setPhoto( image_View.getBystebyteImg() );
                }
            }
            catch ( Exception e )
            {
//                e.printStackTrace();
            }
            
            try
            {
                produtoDao.edit( produto );
            }
            catch ( Exception e )
            {
                e.printStackTrace();
            }
            
            try
            {
                ivaAtualizar( produto.getCodigo() );
            }
            catch ( Exception e )
            {
                e.printStackTrace();
                System.err.println( "$$$$$$$ falha no IVA $$$$$$$$$$$" + e );
            }
            
            try
            {
                retencaoAtualizar( produto.getCodigo() );
                //                registrar_preco_alterar();
            }
            catch ( Exception e )
            {
                System.err.println( "$$$$$$$ falha na retencao $$$$$$$$$$$" );
            }
            
            try
            {
                registrar_preco();
            }
            catch ( Exception e )
            {
            }
            
            JOptionPane.showMessageDialog( null, "Dados alterados com sucesso!", DVML.DVML_COMERCIAL, JOptionPane.INFORMATION_MESSAGE );
            
        }
        
    }
    
    private void set_dados_eliminar()
    {
        
        String codigoBarraText = txtCodigoBarra.getText();
        String codigoBarra = ( codigoBarraText.isEmpty() ) ? String.valueOf( new ProdutoDao().findTbProdutoEntities().size() ) : codigoBarraText;
        
        System.out.println( "Codigo:>>>>>>>> " + produto.getCodigo() );
        System.out.println( "Codigo Barra:>>>>>>>> " + codigoBarra );
        System.out.println( "Designacao :>>>>>>>> " + getDesignacaoText() );
        
//        if ( campos_validos_alterar() )
//        {
            preparar_produto_eliminar();
//            try
//            {
//                if ( image_View.getBystebyteImg() != null )
//                {
//                    produto.setPhoto( image_View.getBystebyteImg() );
//                }
//            }
//            catch ( Exception e )
//            {
////                e.printStackTrace();
//            }
            
            try
            {
                produtoDao.edit( produto );
            }
            catch ( Exception e )
            {
                e.printStackTrace();
            }
            
//            try
//            {
//                ivaAtualizar( produto.getCodigo() );
//            }
//            catch ( Exception e )
//            {
//                e.printStackTrace();
//                System.err.println( "$$$$$$$ falha no IVA $$$$$$$$$$$" + e );
//            }
//            
//            try
//            {
//                retencaoAtualizar( produto.getCodigo() );
//                //                registrar_preco_alterar();
//            }
//            catch ( Exception e )
//            {
//                System.err.println( "$$$$$$$ falha na retencao $$$$$$$$$$$" );
//            }
//            
//            try
//            {
//                registrar_preco();
//            }
//            catch ( Exception e )
//            {
//            }
            
            JOptionPane.showMessageDialog( null, "Produto reactivado com sucesso!", DVML.DVML_COMERCIAL, JOptionPane.INFORMATION_MESSAGE );
            cmbStatus.setSelectedIndex( 0);
            procedimento_limpar_campos();
//        }
        
    }
    
    private void retencaoAtualizar( int fkProduto )
    {
        
        ServicoRetencaoDao servicoRetencaoDao = new ServicoRetencaoDao( emf );
        
        if ( retencaoAplicarJRadioButton.isSelected() )
        {
            servicoRetencaoDao.aplicarRetencao( fkProduto, retencao.getPkRetencao() );
            retencaoTaxaJTextField.getText();
        }
        else
        {
            servicoRetencaoDao.aplicarRetencao( fkProduto, retencao.getPkRetencao() );
            retencaoZeroTaxaJTextField.getText();
        }
    }
    
    private void iVaAtualizar( int fkProduto )
    {
        
        ServicoRetencaoDao servicoRetencaoDao = new ServicoRetencaoDao( emf );
        
        if ( retencaoAplicarJRadioButton.isSelected() )
        {
            servicoRetencaoDao.aplicarRetencao( fkProduto, retencao.getPkRetencao() );
            retencaoTaxaJTextField.getText();
        }
        else
        {
            servicoRetencaoDao.aplicarRetencao( fkProduto, retencao.getPkRetencao() );
            retencaoZeroTaxaJTextField.getText();
        }
    }
    
    private void procedimento_salvar_produto()
    {
        if ( campos_validos() )
        {
            
            String designacao_produto = getDesignacaoText();

            //ALTERAR PRODUTO
            if ( produtoDao.exist_designacao_produto( designacao_produto ) )
            {
                try
                {
                    produto = produtoDao.getProdutoByDescricao( designacao_produto );
                    set_dados_produto();
                    produtoDao.edit( produto );
                }
                catch ( Exception e )
                {
                }
                
            } //CRIAR NOVO PRODUTO
            else
            {
                
                produto = new TbProduto();
                set_dados_produto();
                System.out.println( "Codigo de Barra: " + produto.getCodBarra() );
                produtoDao.create( this.produto );
                
                this.produto = produtoDao.findTbProduto( produtoDao.getUltimoProduto() );
                
                if ( !ivaAplicarJRadioButton.isSelected() )//APLICAR ISENÇÃO
                {
                    String regimeIsencao = (String) ivaMotivoJComboBox.getSelectedItem();
                    System.err.println( "produto isento iva" );
                    System.err.println( produto );
                    ProdutosMotivosIsensao motivosIsensao = produtosMotivosIsensaoDao.findByRegime( regimeIsencao );
                    
                    if ( motivosIsensao != null )
                    {
                        ProdutoIsentoDao produtoIsentoDao = new ProdutoIsentoDao( emf );
                        
                        ProdutoIsento produtoIsento = new ProdutoIsento();
                        produtoIsento.setFkProdutosMotivosIsensao( motivosIsensao );
                        produtoIsento.setFkProduto( produto );
                        
                        produtoIsentoDao.create( produtoIsento );
                    }
                }
                else //APLICAR IVA
                {
                    System.err.println( "aplicar iva" );
                    System.err.println( produto );
                    ProdutoImpostoDao produto_impostoDao = new ProdutoImpostoDao( emf );
//                    Imposto imposto = new ImpostoDao( emf ).findFirst();
//                    this.produto.setCodUnidade( unidadeDao.findUnidade( getIdUnidade() ) );
//                    this.produto_imposto.setFkImposto(impostoDao.findImposto(getIdImposto() ) );
//                    Imposto imposto = new ImpostoDao( emf ).buscaTodos();
                    ProdutoImposto produtoImposto = new ProdutoImposto();
                    produtoImposto.setFkProduto( produto );
//                    produtoImposto.setStatusImposto("true");
                    produtoImposto.setFkImposto( impostoDao.findImposto( getIdImposto() ) );
//                    produtoImposto.setFkImposto( getIdImposto() );

                    produto_impostoDao.create( produtoImposto );
                    
                }
                
                if ( !retencaoAplicarJRadioButton.isSelected() )
                {
                    ServicoRetencaoDao servicoRetencaoDao = new ServicoRetencaoDao( emf );
                    Retencao retencao_local = new RetencaoDao( emf ).findLast();
                    ServicoRetencao produtoRetencao = new ServicoRetencao();
                    produtoRetencao.setFkProduto( produto );
                    produtoRetencao.setFkRetencao( retencao_local );
                    
                    servicoRetencaoDao.create( produtoRetencao );
                    
                }
                else //APLICAR RETENCAO
                {
                    ServicoRetencaoDao servicoRetencaoDao = new ServicoRetencaoDao( emf );
                    Retencao retencao_local = new RetencaoDao( emf ).findFirst();
                    ServicoRetencao produtoRetencao = new ServicoRetencao();
                    produtoRetencao.setFkProduto( produto );
                    produtoRetencao.setFkRetencao( retencao_local );
                    servicoRetencaoDao.create( produtoRetencao );
                    
                }
                this.produto = produtoDao.getProdutoByDescricao( designacao_produto );
                
                registrar_preco();
                
                JOptionPane.showMessageDialog( null, "Dados salvos com sucesso!", DVML.DVML_COMERCIAL, JOptionPane.INFORMATION_MESSAGE );
                txtDesignacao.requestFocus();
                detalhe_produto();
                proximo_codigo();
                txtCodigoBarra.setText( String.valueOf( produtoDao.getUltimaCodProduto() + 1 ) );
                
                procedimento_limpar();
                
            }
            
        }
        
    }
    
    private void procedimento_alterar_servico_produto()
    {
        
        try
        {
            set_dados_produto_alterar();
            
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            JOptionPane.showMessageDialog( null, e.getMessage(), "Falha ao alterar os dados", JOptionPane.ERROR_MESSAGE );
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
            JOptionPane.showMessageDialog( null, e.getMessage(), "Falha ao reactivar produto", JOptionPane.ERROR_MESSAGE );
        }
        
    }
    
    private void set_combos()
    {
        cmbMarca.setModel( new DefaultComboBoxModel( (Vector) marcaDao.buscaTodasMarcas() ) );
        cmbGrupo.setModel( new DefaultComboBoxModel( (Vector) grupoDao.buscaTodos() ) );
        try
        {
            cmbModelo.setModel( new DefaultComboBoxModel( (Vector) modeloDao.getDescricaoByIdMarca( getIdMarca() ) ) );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
        
        try
        {
            cmbFamilia.setModel( new DefaultComboBoxModel( familiaDao.buscaFamiliasSelecione() ) );
            cmbTipoProduto.setModel( new DefaultComboBoxModel( (Vector) tipoProdutoDao.getDescricaoByIdFamilias( getIdFamilia() ) ) );
            
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
        cmbLocal.setModel( new DefaultComboBoxModel( localDao.buscaTodos() ) );
        cmbUnidade.setModel( new DefaultComboBoxModel( unidadeDao.buscaTodos() ) );
        cmbStatus.setVisible( true );
        
    }

    //actualizar
    private int getIdLocal()
    {
        return localDao.getLocalByDescricao( String.valueOf( cmbLocal.getSelectedItem() ) ).getCodigo();
    }
    
    private int getIdUnidade()
    {
        return unidadeDao.getUnidadeByDescricao( String.valueOf( cmbUnidade.getSelectedItem() ) ).getPkUnidade();
    }
    
    private int getIdImposto()
    {
        return impostoDao.getImpostoByTaxa( Double.parseDouble( String.valueOf( cmbImposto.getSelectedItem() ) ) ).getPkImposto();
    }
    
    private int getIdGrupo()
    {
        return grupoDao.getGrupoByDescricao( String.valueOf( cmbGrupo.getSelectedItem() ) ).getPkGrupo();
    }
    
    private int getIdTipoPrdouto()
    {
        return tipoProdutoDao.getTipoProdutoByDescricao( String.valueOf( cmbTipoProduto.getSelectedItem() ) ).getCodigo();
    }
    
    private static int getIdFamilia()
    {
        Familia familia = familiaDao.getFamiliaByDescricao( String.valueOf( cmbFamilia.getSelectedItem() ) );
        return Objects.isNull( familia ) ? -1 : familia.getPkFamilia();
    }
    
    private static int getIdMarca()
    {
        return marcaDao.getMarcaByDescricao( String.valueOf( cmbMarca.getSelectedItem() ) ).getPkMarca();
    }
    
    private static int getIdModelo()
    {
        return modeloDao.getModeloByDescricao( String.valueOf( cmbModelo.getSelectedItem() ) ).getPkModelo();
    }
    
    public static void ver_dados( int codigo )
    {
        
        TbProduto produto_local = produtoDao.getProdutoDesctivoByCodigoProduto(codigo );

//        if ( produto.getCodigo() != 0 )
        if ( !Objects.isNull( produto_local ) )
        {
            mostrar_painel( produto_local );
            System.out.println( "Cheguei aqui" );

            preco = precoDao.findTbPreco( precoDao.getUltimoIdPrecoByIdProduto( produto_local.getCodigo() ) );
            
            if ( Objects.isNull( preco ) )
            {
                return;
            }
            
            cmbMarca.setSelectedItem( produto_local.getFkModelo().getFkMarca().getDesignacao() );
            cmbModelo.setSelectedItem( produto_local.getFkModelo().getDesignacao() );
            cmbFamilia.setSelectedItem( produto_local.getCodTipoProduto().getFkFamilia().getDesignacao() );
            cmbTipoProduto.setSelectedItem( produto_local.getCodTipoProduto().getDesignacao() );
            cmbStatus.setSelectedIndex(0);
            
            try
            {
                if ( !Objects.isNull( produto_local.getPhoto() ) )
                {
                    imageIcon = new ImageIcon( produto_local.getPhoto() );
                    imageIcon.setImage( imageIcon.getImage().getScaledInstance( 112, 109, 100 ) );
                    lbPhoto.setIcon( imageIcon );
                }
            }
            catch ( Exception e )
            {
                e.printStackTrace();
                lbPhoto.setIcon( null );
            }
            
            try
            {
                
                cmbLocal.setSelectedItem( produto_local.getCodLocal().getDesignacao() );
                cmbModelo.setSelectedItem( produto_local.getFkModelo().getDesignacao() );
                cmbGrupo.setSelectedItem( produto_local.getFkGrupo().getDesignacao() );
            }
            catch ( Exception e )
            {
                e.printStackTrace();
            }
            cmbUnidade.setSelectedItem( produto_local.getCodUnidade().getDescricao() );
            txtDesignacao.setText( produto_local.getDesignacao() );
            System.out.println( "PRECO VENDA @@@@@@:" + precoDao.getLastPrecoByIdProduto( produto_local.getCodigo() ).getPrecoCompra() );
            
            Double preco_compra = precoDao.getLastPrecoByIdProduto( produto_local.getCodigo() ).getPrecoCompra().doubleValue();
            
            txtPrecoCompra.setText( CfMethods.formatarComoPorcoes( preco_compra ) );
            
            txtCodigoBarra.setText( String.valueOf( produto_local.getCodBarra() ) );
            txtCodigoManual.setText( String.valueOf( produto_local.getCodigoManual() ) );
            txtCodigoBarraProcura.setText( String.valueOf( produto_local.getCodBarra() ) );
            txtCodigoManualProcura.setText( String.valueOf( produto_local.getCodigoManual() ) );
            jcDataExpiracao.setDate( produto_local.getDataExpiracao() );
            jcDataFabrico.setDate( produto_local.getDataFabrico() );
            
            Integer pkProduto = produto_local.getCodigo();
            boolean temIva = new ProdutoImpostoDao( emf ).exist( pkProduto );
            
            if ( !temIva )
            {
                String regime = new ProdutoIsentoDao( emf ).getRegimeIsensaoByIdProduto( pkProduto );
                ivaMotivoJComboBox.setSelectedItem( regime );
                
            }
            
            ivaAplicarJRadioButton.setSelected( temIva );
            ivaNaoAplicarJRadioButton.setSelected( !temIva );
//            cmbImposto.setSelectedItem( temIva );
            boolean temRetencao = new ServicoRetencaoDao( emf ).exist_retencao( pkProduto );
            
            retencaoAplicarJRadioButton.setSelected( temRetencao );
            retencaoNaoAplicarJRadioButton.setSelected( !temRetencao );
            
            atualizarIvaForm();
            
            Double taxa = new ProdutoImpostoDao( emf ).getTaxaByIdProduto( pkProduto );
            cmbImposto.setSelectedItem( taxa );
            System.out.println( "TAXA REAL: " + taxa );
            
            actualizarRetencaoForm();

            /**
             * A IDEIA BUSCAR O ULTIMO PRECO(GROSSO) E DEPOIS BUSCAR O PRECO
             * RETALHO EM FUNCAO DO PRECO GROSSO
             */
            TbPreco precoGrosso = precoDao.getLastPrecoByIdProduto( produto_local.getCodigo() );
            
            int idPrecoRetalho = precoDao.getUltimoIdPrecoByIdProduto( produto_local.getCodigo(), precoGrosso.getQtdBaixo() - 1 );
            
            TbPreco precoRetalhoLocal = precoDao.findTbPreco( idPrecoRetalho );
            double preco_venda_retalho = precoRetalhoLocal.getPrecoVenda().doubleValue();
            double preco_venda_grosso = precoGrosso.getPrecoVenda().doubleValue();
            double percentagem_ganho = precoRetalhoLocal.getPercentagemGanho().doubleValue();
            
            txtPrecoVendaRetalho.setText(
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
            
//            servico_produto();
            
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
        cmbLocal.setModel( new DefaultComboBoxModel( localDao.buscaTodos() ) );
        cmbUnidade.setModel( new DefaultComboBoxModel( unidadeDao.buscaTodos() ) );
        ck_produto.setSelected( true );
        servico_produto();
    }
    
    private static void procedimento_limpar_campos()
    {
        limpar_esquerdo();
        limpar_direito_campos();
        //actualizar
        cmbLocal.setModel( new DefaultComboBoxModel( localDao.buscaTodos() ) );
        cmbUnidade.setModel( new DefaultComboBoxModel( unidadeDao.buscaTodos() ) );
        ck_produto.setSelected( true );
        servico_produto();
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
        
    }
    
    private static void limpar_direito_campos()
    {

//        txtCodigoBarra.setText( "0" );
        txtPrecoVendaRetalho.setText( "" );
        txtCodigoProduto.setText( "" );
        txtCodigoBarraProcura.setText( "" );
        txtPercentagemGanhoRetalho.setText( "" );
        txtCodigoManual.setText( "" );
        
    }
    
    private boolean validado_stock()
    {
        
        if ( txtDesignacao.getText().equals( "" ) )
        {
            txtDesignacao.requestFocus();
            JOptionPane.showMessageDialog( null, "Pf Digite a designação do produto", DVML.DVML_COMERCIAL, JOptionPane.WARNING_MESSAGE );
            return false;
        }
        else if ( txtPrecoCompra.getText().equals( "" ) )
        {
            txtPrecoCompra.requestFocus();
            JOptionPane.showMessageDialog( null, "Pf Digite o preço do produto", DVML.DVML_COMERCIAL, JOptionPane.WARNING_MESSAGE );
            return false;
        }
        else if ( txtPrecoVendaRetalho.getText().equals( "" ) )
        {
            txtPrecoVendaRetalho.requestFocus();
            JOptionPane.showMessageDialog( null, "Pf Insira o preço unitário do produto", DVML.DVML_COMERCIAL, JOptionPane.WARNING_MESSAGE );
            return false;
        }
        return true;
        
    }
    
    public boolean exist_produto_stock( int codigo_produto )
    {
        try
        {
            return stockDao.exist_produto_stock( codigo_produto, DVML.ARMAZEM_DEFAUTL );
        }
        catch ( Exception e )
        {
            return false;
        }
    }
    
    private void buscar_by_cod_barra()
    {
        
        TbProduto produto_local = produtoDao.getStockByCodBarra( txtCodigoBarraProcura.getText().trim() );
        String codInternoString = txtCodigoBarraProcura.getText();
        String codigoInternoBarra = ( codInternoString.isEmpty() ? "0" : codInternoString );
//        produto1 = produtoDao.getProdutoByCodigoBarra( codigoInternoBarra );
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
            mostrar_cod_dados_stock_armazem1cb( produto_local );
//                }else{
//                    cmbarmazem1.setVisible( false);
//                    txtArmazen1.setVisible( false);
//                    
//                }

            mostrar_cod_dados_stock_armazem2ci( produto1 );
            mostrar_cod_dados_stock_armazem3ci( produto1 );
            mostrar_cod_dados_stock_armazem4ci( produto1 );
        }
        else
        {
            procedimento_limpar();
        }
    }
    
    private void detalhe_produto()
    {
        
        String designacao_produto = getDesignacaoText();
        TbProduto produto_local = produtoDao.getProdutoByDescricao( designacao_produto );
        
        String output = "";
        TbPreco preco_local = precoDao.getPrecoByIdProduto( produto_local.getCodigo() );
        output += "CodInterno           : " + produto_local.getCodigo() + "\n";
        output += "CodManual           : " + produto_local.getCodigoManual() + "\n";
        output += "Designação           : " + produto_local.getDesignacao().toUpperCase() + "\n";
        output += "Unidade           : " + produto_local.getCodUnidade().getDescricao().toUpperCase() + "\n";
        output += "Preço da Compra      : " + preco_local.getPrecoCompra() + "\n";
        output += "Percentagem Ganho    : " + preco_local.getPercentagemGanho() + "  % \n";
        output += "Preço de Venda       : " + preco_local.getPrecoVenda() + "\n";
        
        JOptionPane.showMessageDialog( null, output );
        
    }
    
    private void detalhe_produto_por_armazem()
    {
        
        String designacao_produto = getDesignacaoText();
        TbProduto produto_local = produtoDao.getProdutoByDescricao( designacao_produto );
        
        String output = "";
        TbPreco preco_local = precoDao.getPrecoByIdProduto( produto_local.getCodigo() );
        TbStock stock_local = stockDao.get_stock_by_id_produto_and_sem_armazem( produto_local.getCodigo() );
        TbStock stock_local_1 = stockDao.get_stock_by_id_produto_1( produto_local.getCodigo() );
        TbStock stock_local_2 = stockDao.get_stock_by_id_produto_2( produto_local.getCodigo() );
        
        output += "CodInterno           : " + produto_local.getCodigo() + "\n";
        output += "CodManual           : " + produto_local.getCodigoManual() + "\n";
        output += "Designação           : " + produto_local.getDesignacao().toUpperCase() + "\n";
        output += "Unidade           : " + produto_local.getCodUnidade().getDescricao().toUpperCase() + "\n";
        output += "Preço da Compra      : " + preco_local.getPrecoCompra() + "\n";
        output += "Preço de Venda       : " + preco_local.getPrecoVenda() + "\n";
        
        output += "Quantidade Existente Armazem 1       : " + stock_local_1.getQuantidadeExistente() + "\n";
        output += "Quantidade Existente Armazem 2       : " + stock_local_2.getQuantidadeExistente() + "\n";
        
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
//            preco.setPrecoVenda( new BigDecimal( MetodosUtil.convertToDouble( txtPrecoCompra.getText() ) ) );
        }
        
        preco.setData( new Date() );
        preco.setHora( new Date() );
        preco.setFkProduto( produtoDao.getProdutoByDescricao( getDesignacaoText() ) );
        preco.setFkUsuario( usuarioDao.findTbUsuario( idUser ) );
        
    }
    
    private void setDadosPrecoAlterar()
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
        preco.setFkProduto( produto );
        preco.setFkUsuario( usuarioDao.findTbUsuario( idUser ) );
        
    }
    
    private boolean campos_validos()
    {
        boolean isProduto = ck_produto.isSelected();
        
        System.out.println( "Status Produto: " + isProduto );
        MetodosUtil.getValorTransformadoString( preco_venda_anterior );
        
        String designacao_produto = getDesignacaoText();
        
        if ( designacao_produto.equals( "" ) )
        {
            
            JOptionPane.showMessageDialog( null, "Caro usuário " + usuarioDao.findTbUsuario( this.idUser ).getNome() + " " + usuarioDao.findTbUsuario( this.idUser ).getSobreNome() + " por favor digite a designação do produto" );
            txtPrecoCompra.requestFocus();
            return false;
            
        }
        else if ( produtoDao.exist_designacao_produto( getDesignacaoText() ) )
        {
            JOptionPane.showMessageDialog( null, "Ja existe um produto com esta designacao!!!", DVML.DVML_COMERCIAL, JOptionPane.WARNING_MESSAGE );
            txtDesignacao.requestFocus();
            return false;
        }
        else if ( produtoDao.existProdutoByCodigoBarra( txtCodigoBarra.getText() ) && isProduto )
        {
            JOptionPane.showMessageDialog( null, "Este codigo de barra ja existe!!!", DVML.DVML_COMERCIAL, JOptionPane.WARNING_MESSAGE );
            txtCodigoBarra.requestFocus();
            txtCodigoBarra.setBackground( Color.YELLOW );
            return false;
        }
        else if ( txtPrecoCompra.getText().equals( "" ) )
        {
            
            JOptionPane.showMessageDialog( null, "Caro usuário " + usuarioDao.findTbUsuario( this.idUser ).getNome() + " " + usuarioDao.findTbUsuario( this.idUser ).getSobreNome() + " , insira o preço da compra do produto" );
            txtPrecoCompra.requestFocus();
            return false;
            
        }
        else if ( txtPrecoCompra.getText().equals( "0" ) )
        {
            JOptionPane.showMessageDialog( null, "Caro usuário " + usuarioDao.findTbUsuario( this.idUser ).getNome() + " " + usuarioDao.findTbUsuario( this.idUser ).getSobreNome() + ", o preço da compra não pode ser igual a zero(0)." );
            txtPrecoCompra.requestFocus();
            return false;
        }
        else if ( txtPrecoVendaRetalho.getText().equals( "" ) && isProduto )
        {
            
            JOptionPane.showMessageDialog( null, "Caro usuário " + usuarioDao.findTbUsuario( this.idUser ).getNome() + " " + usuarioDao.findTbUsuario( this.idUser ).getSobreNome() + " , insira o preço da venda do produto" );
            txtPrecoVendaRetalho.requestFocus();
            return false;
            
        }
        else if ( txtPrecoVendaRetalho.getText().equals( "0" ) && isProduto )
        {
            JOptionPane.showMessageDialog( null, "Caro usuário " + usuarioDao.findTbUsuario( this.idUser ).getNome() + " " + usuarioDao.findTbUsuario( this.idUser ).getSobreNome() + ",  o preço da venda não pode ser igual a zero(0)." );
            txtPrecoVendaRetalho.requestFocus();
            return false;
        }
        else if ( cmbUnidade.getSelectedItem().equals( "--SELECIONE--" ) )
        {
            JOptionPane.showMessageDialog( null, "Pf. Selecione a unidade de medida do produto", DVML.DVML_COMERCIAL, JOptionPane.WARNING_MESSAGE );
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
        cmbImposto.setModel( new DefaultComboBoxModel( impostoDao.buscaTodos() ) );
        ivaMotivoJComboBox.setModel( listarMotivos() );
        ivaMotivoJComboBox.setSelectedIndex( -1 );
        imposto = new ImpostoDao( emf ).findALL();
//        ivaTaxaJTextField.setText( MetodosUtil.formatarComoPercentagem( imposto.getTaxa() ) );

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
        
        List<ProdutosMotivosIsensao> ProdutosMotivosIsensao = new ProdutosMotivosIsensaoDao( emf ).findAll();
        
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
            dadosInstituicao = dadosInstituicaoDao.findTbDadosInstituicao( 1 );
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
        ivaTaxaJLabel.setVisible( cmbImposto.isVisible() );
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
    
    private void ivaAtualizar( int fkProduto )
    {
        ProdutoIsentoDao produtoIsentoDao = new ProdutoIsentoDao( emf );
        ProdutoImpostoDao produto_impostoDao = new ProdutoImpostoDao( emf );
        
        if ( ivaAplicarJRadioButton.isSelected() )
        {
            produto_impostoDao.aplicarIva( fkProduto, imposto.getPkImposto() );
            ProdutoImposto produtoImposto = new ProdutoImposto();
            produtoImposto.setFkProduto( produto );
//            produtoImposto.setStatusImposto("true");
            produtoImposto.setFkImposto( impostoDao.findImposto( getIdImposto() ) );
            
            produto_impostoDao.create( produtoImposto );
            
            cmbImposto.getSelectedItem();
            
        }
        else
        {
            String regimeIsencao = (String) ivaMotivoJComboBox.getSelectedItem();
            ProdutosMotivosIsensao isensao = produtosMotivosIsensaoDao.findByRegime( regimeIsencao );
            if ( !Objects.isNull( isensao ) )
            {
                produtoIsentoDao.aplicarIsencao( fkProduto, isensao.getPkProdutosMotivosIsensao() );
            }
            
        }
    }
    
    private void ivaAtualizar1( int fkProduto )
    {
        ProdutoIsentoDao produtoIsentoDao = new ProdutoIsentoDao( emf );
        ProdutoImpostoDao produtoImpostoDao = new ProdutoImpostoDao( emf );
        
        if ( ivaAplicarJRadioButton.isSelected() )
        {
            produtoImpostoDao.aplicarIva( fkProduto, imposto.getPkImposto() );
            
        }
        else
        {
            String regimeIsencao = (String) ivaMotivoJComboBox.getSelectedItem();
            ProdutosMotivosIsensao isensao = produtosMotivosIsensaoDao.findByRegime( regimeIsencao );
            produtoIsentoDao.aplicarIsencao( fkProduto, isensao.getPkProdutosMotivosIsensao() );
        }
    }

//    private void retencaoAtualizar( int fkProduto )
//    {
//
//        ServicoRetencaoDao servicoRetencaoDao = new ServicoRetencaoDao( emf );
//
////        if ( retencaoAplicarJRadioButton.isSelected() )
////        {
//            servicoRetencaoDao.aplicarRetencao( fkProduto, retencao.getPkRetencao() );
////        }
////        else
////        {
////            servicoRetencaoDao.aplicarRetencao( fkProduto, retencao.getPkRetencao() );
////        }
//    }
    private void aplicar_iva()
    {
        if ( !ivaAplicarJRadioButton.isSelected() )//APLICAR ISENÇÃO
        {
            String regimeIsencao = (String) ivaMotivoJComboBox.getSelectedItem();
            System.err.println( "produto isento iva" );
            System.err.println( produto );
            ProdutosMotivosIsensao motivosIsensao = produtosMotivosIsensaoDao.findByRegime( regimeIsencao );
            
            if ( motivosIsensao != null )
            {
                ProdutoIsentoDao produtoIsentoDao = new ProdutoIsentoDao( emf );
                
                ProdutoIsento produtoIsento = new ProdutoIsento();
                produtoIsento.setFkProdutosMotivosIsensao( motivosIsensao );
                produtoIsento.setFkProduto( produto );
                
                produtoIsentoDao.create( produtoIsento );
            }
        }
        else //APLICAR IVA
        {
            System.err.println( "aplicar iva" );
            System.err.println( produto );
            ProdutoImpostoDao impostoDao = new ProdutoImpostoDao( emf );
            Imposto imposto = new ImpostoDao( emf ).findFirst();
            ProdutoImposto produtoImposto = new ProdutoImposto();
            produtoImposto.setFkProduto( produto );
//            produtoImposto.setStatusImposto("true");
            produtoImposto.setFkImposto( imposto );
            
            impostoDao.create( produtoImposto );
            
        }
    }
    
    private void aplicar_retencao()
    {
        
        if ( retencaoAplicarJRadioButton.isSelected() )
        {
            ServicoRetencaoDao servicoRetencaoDao = new ServicoRetencaoDao( emf );
            Retencao retencao = new RetencaoDao( emf ).findFirst();
            ServicoRetencao produtoRetencao = new ServicoRetencao();
            produtoRetencao.setFkProduto( produto );
            produtoRetencao.setFkRetencao( retencao );
            
            servicoRetencaoDao.create( produtoRetencao );
            
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
    
    private void proximo_codigo()
    {
        try
        {
            jLabelCodProduto.setText( "Próximo Código: " + String.valueOf( produtoDao.getUltimaCodProduto() + 1 ) );
            txtCodigoBarra.setText( String.valueOf( produtoDao.getUltimaCodProduto() + 1 ) );
        }
        catch ( Exception e )
        {
        }
        
    }
    
    private void setFocus( String focus )
    {
        if ( focus.equalsIgnoreCase( "Codigo Interno" ) )
        {
            txtCodigoProduto.requestFocus();
            txtCodigoBarra.setEnabled( false );
            
        }
        else
        {
            txtCodigoBarraProcura.requestFocus();
            
        }
    }
    
    private void registrar_preco()
    {
        
        if ( dadosInstituicao.getUsarDoisPrecos().equals( "nao" ) )
        {
            txtQtdGrosso.setText( String.valueOf( (int) DVML.QTD_DEFAULT ) );
            txtPrecoVendaGrosso.setText( txtPrecoVendaRetalho.getText() );
        }
        
        System.out.println( "&&&&&&&&&& REGISTRAR PRECO $$$$$$$$$$" );
        boolean isProduto = ck_produto.isSelected();
        /*PRIMEIRO PRECO RETALHO*/
        setDadosPreco();
        preco.setQtdBaixo( 0 );
        preco.setQtdAlto( Integer.parseInt( txtQtdGrosso.getText() ) - 1 );
        //preco.setPrecoVenda( MetodosUtil.retirar_dizimas( Double.parseDouble(  txtPrecoVenda.getText()  )  )   );
        BigDecimal precoVenda = new BigDecimal( MetodosUtil.convertToDouble( isProduto ? txtPrecoVendaRetalho.getText() : "0.0" ) );
        BigDecimal precoVendaGrosso = new BigDecimal( MetodosUtil.convertToDouble( isProduto ? txtPrecoVendaGrosso.getText() : "0.0" ) );
        BigDecimal precoCompra = new BigDecimal( MetodosUtil.convertToDouble( txtPrecoCompra.getText() ) );
        
        preco.setPrecoVenda( isProduto ? precoVenda : precoCompra );
        precoDao.create( preco );

        /*SEGUNDO PRECO GROSSO*/
        setDadosPreco();
//        preco.setQtdBaixo( (int) DVML.QTD_DEFAULT );
        preco.setQtdBaixo( Integer.parseInt( txtQtdGrosso.getText() ) );
        preco.setQtdAlto( 214748364 );
        
        preco.setPrecoVenda( isProduto ? precoVendaGrosso : precoCompra );
        precoDao.create( preco );
    }
    
    private void registrar_preco_alterar()
    {
        /*PRIMEIRO PRECO RETALHO*/
        setDadosPrecoAlterar();
        preco.setQtdBaixo( 0 );
        preco.setQtdAlto( (int) DVML.QTD_DEFAULT - 1 );
        preco.setPrecoVenda( new BigDecimal( MetodosUtil.convertToDouble( txtPrecoVendaRetalho.getText() ) ) );
        precoDao.create( preco );

        /*SEGUNDO PRECO GROSSO*/
        setDadosPrecoAlterar();
        preco.setQtdBaixo( (int) DVML.QTD_DEFAULT );
        preco.setQtdAlto( 214748364 );
        preco.setPrecoVenda( new BigDecimal( MetodosUtil.convertToDouble( txtPrecoVendaRetalho.getText() ) ) );
        precoDao.create( preco );
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
    // </editor-fold>

    private void tratar_percentagem_ganho()
    {
        
        double preco_venda = MetodosUtil.convertToDouble( txtPrecoVendaRetalho.getText().replace( ",", "." ) );
        double preco_compra = MetodosUtil.convertToDouble( txtPrecoCompra.getText().replace( ",", "." ) );
        txtPercentagemGanhoRetalho.setText( CfMethods.formatarComoPorcoes( MetodosUtil.percentagemGanho( preco_compra, preco_venda ) ) );
    }
    
    private void tratar_preco_venda()
    {
        
        double percentagem_ganho = MetodosUtil.convertToDouble( txtPercentagemGanhoRetalho.getText().replace( ",", "." ) );
        double preco_compra = MetodosUtil.convertToDouble( txtPrecoCompra.getText().replace( ",", "." ) );
        System.out.println( "Preco Venda : " + MetodosUtil.precoVenda( preco_compra, percentagem_ganho ) );
        txtPrecoVendaRetalho.setText( CfMethods.formatarComoPorcoes( MetodosUtil.precoVenda( preco_compra, percentagem_ganho ) ) );
    }

//    private static void mostrar_dados_stock_origem( TbProduto produto_parm )
//    {
////        TbStock stockByCodBarra = stockDao.getStockByCodBarra( produto_parm.getCodBarra(), getIdArmazem() );
//        TbStock stockByCodBarra = stockDao.getStockByCodBarra( produto_parm.getCodBarra(), getCodigoArmazem() );
//        if ( !Objects.isNull( stockByCodBarra ) )
//        {
//            txtQtdExistente.setText( String.valueOf( stockByCodBarra.getQuantidadeExistente() ) );
//        }
//        else
//        {
//            txtQtdExistente.setText( "0" );
//        }
//    }
//
//    private static void mostrar_dados_stock_destino( TbProduto produto_parm )
//    {
////        TbStock stockByCodBarra = stockDao.getStockByCodBarra( produto_parm.getCodBarra(), getIdArmazem() );
//        TbStock stockByCodBarra = stockDao.getStockByCodBarra( produto_parm.getCodBarra(), getCodigoArmazem1() );
//        if ( !Objects.isNull( stockByCodBarra ) )
//        {
//            txtQtdExistente1.setText( String.valueOf( stockByCodBarra.getQuantidadeExistente() ) );
//        }
//        else
//        {
//            txtQtdExistente1.setText( "0" );
//        }
//    }
//    private static void mostrar_cod_dados_stock_origem( TbProduto produto_parm )
//    {
////        TbStock stockByCodBarra = stockDao.getStockByCodBarra( produto_parm.getCodBarra(), getIdArmazem() );
//        TbStock stockByCodInterno = stockDao.getStockByCodInterno( produto_parm.getCodigo(), getCodigoArmazem() );
//        if ( !Objects.isNull( stockByCodInterno ) )
//        {
//            txtQtdExistente.setText( String.valueOf( stockByCodInterno.getQuantidadeExistente() ) );
//        }
//        else
//        {
//            txtQtdExistente.setText( "0" );
//        }
//    }
    private static void mostrar_cod_dados_stock_armazem1ci( TbProduto produto_parm )
    {
//        TbStock stockByCodBarra = stockDao.getStockByCodBarra( produto_parm.getCodBarra(), getIdArmazem() );
        TbStock stockByCodInterno = stockDao.getStockByCodInterno( produto_parm.getCodigo(), getCodigoArmazem1() );
        if ( !Objects.isNull( stockByCodInterno ) )
        {
            txtArmazen1.setText( String.valueOf( stockByCodInterno.getQuantidadeExistente() ) );
        }
        else
        {
            txtArmazen1.setText( "0" );
            
        }
    }
    
    private static void mostrar_cod_dados_stock_armazem2ci( TbProduto produto_parm )
    {
//        TbStock stockByCodBarra = stockDao.getStockByCodBarra( produto_parm.getCodBarra(), getIdArmazem() );
        TbStock stockByCodInterno = stockDao.getStockByCodInterno( produto_parm.getCodigo(), getCodigoArmazem2() );
        if ( !Objects.isNull( stockByCodInterno ) )
        {
            txtArmazen2.setText( String.valueOf( stockByCodInterno.getQuantidadeExistente() ) );
        }
        else
        {
            txtArmazen2.setText( "0" );
        }
    }
    
    private static void mostrar_cod_dados_stock_armazem3ci( TbProduto produto_parm )
    {
//        TbStock stockByCodBarra = stockDao.getStockByCodBarra( produto_parm.getCodBarra(), getIdArmazem() );
        TbStock stockByCodInterno = stockDao.getStockByCodInterno( produto_parm.getCodigo(), getCodigoArmazem3() );
        if ( !Objects.isNull( stockByCodInterno ) )
        {
            txtArmazen3.setText( String.valueOf( stockByCodInterno.getQuantidadeExistente() ) );
        }
        else
        {
            txtArmazen3.setText( "0" );
        }
    }
    
    private static void mostrar_cod_dados_stock_armazem4ci( TbProduto produto_parm )
    {
//        TbStock stockByCodBarra = stockDao.getStockByCodBarra( produto_parm.getCodBarra(), getIdArmazem() );
        TbStock stockByCodInterno = stockDao.getStockByCodInterno( produto_parm.getCodigo(), getCodigoArmazem4() );
        if ( !Objects.isNull( stockByCodInterno ) )
        {
            txtArmazen4.setText( String.valueOf( stockByCodInterno.getQuantidadeExistente() ) );
        }
        else
        {
            txtArmazen4.setText( "0" );
        }
    }
    
    private static void mostrar_cod_dados_stock_armazem1cb( TbProduto produto_parm )
    {
        TbStock stockByCodBarra = stockDao.getStockByCodBarra( produto_parm.getCodBarra(), getCodigoArmazem1() );
//        TbStock stockByCodInterno = stockDao.getStockByCodInterno( produto_parm.getCodigo(), getCodigoArmazem1() );
        if ( !Objects.isNull( stockByCodBarra ) )
        {
            txtArmazen1.setText( String.valueOf( stockByCodBarra.getQuantidadeExistente() ) );
        }
        else
        {
            txtArmazen1.setText( "0" );
        }
    }
    
    public static int getCodigoArmazem1()
    {
        try
        {
            return armazemDao.getArmazemByDescricao( cmbarmazem1.getSelectedItem().toString() ).getCodigo();
            
        }
        catch ( Exception e )
        {
            return 0;
        }
    }
    
    public static int getCodigoArmazem2()
    {
        try
        {
            return armazemDao.getArmazemByDescricao( cmbarmazem2.getSelectedItem().toString() ).getCodigo();
        }
        catch ( Exception e )
        {
            return 0;
        }
        
    }
    
    public static int getCodigoArmazem3()
    {
        try
        {
            return armazemDao.getArmazemByDescricao( cmbarmazem3.getSelectedItem().toString() ).getCodigo();
        }
        catch ( Exception e )
        {
            return 0;
        }
        
    }
    
    public static int getCodigoArmazem4()
    {
        try
        {
            return armazemDao.getArmazemByDescricao( cmbarmazem4.getSelectedItem().toString() ).getCodigo();
        }
        catch ( Exception e )
        {
            return 0;
        }
        
    }
//    public static int getCodigoArmazem5()
//    {
//        return armazemDao.getArmazemByDescricao( cmbArmazem5.getSelectedItem().toString() ).getCodigo();
//    }
//    public static int getCodigoArmazem6()
//    {
//        return armazemDao.getArmazemByDescricao( cmbArmazem6.getSelectedItem().toString() ).getCodigo();
//    }

    private void determinarPercetagemGanho()
    {
        txtPrecoVendaRetalho.getDocument().addDocumentListener( new DocumentListener()
        {
            @Override
            public void insertUpdate( DocumentEvent e )
            {
                tratar_percentagem_ganho();
                
            }
            
            @Override
            public void removeUpdate( DocumentEvent e )
            {
            }
            
            @Override
            public void changedUpdate( DocumentEvent e )
            {
                throw new UnsupportedOperationException( "Not supported yet." ); //To change body of generated methods, choose Tools | Templates.
            }
        } );
        
    }
    
    private void determinarPrecoVenda()
    {
        txtPercentagemGanhoRetalho.getDocument().addDocumentListener( new DocumentListener()
        {
            @Override
            public void insertUpdate( DocumentEvent e )
            {
                tratar_preco_venda();
                
            }
            
            @Override
            public void removeUpdate( DocumentEvent e )
            {
            }
            
            @Override
            public void changedUpdate( DocumentEvent e )
            {
                throw new UnsupportedOperationException( "Not supported yet." ); //To change body of generated methods, choose Tools | Templates.
            }
        } );
        
    }
    
    private static void actualizarRetencaoForm()
    {
        
        if ( retencaoAplicarJRadioButton.isSelected() )
        {
            retencao = new RetencaoDao( emf ).findFirst();
            retencaoTaxaJTextField.setText( MetodosUtil.formatarComoPercentagem( retencao.getTaxa() ) );
        }
        else
        {
            retencao = new RetencaoDao( emf ).findLast();
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
        
        txtCodigoBarra.setEnabled( false );
        
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
//        setUsarDoisPrecos( dadosInstituicaoDao.findTbDadosInstituicao( 1 ).getUsarDoisPrecos() );
        TbDadosInstituicao tbDadosInstituicao;
        tbDadosInstituicao = dadosInstituicaoDao.findTbDadosInstituicao( 1 );
        try
        {
            if ( tbDadosInstituicao.getUsarDoisPrecos().equals( "nao" ) )
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
    
}
