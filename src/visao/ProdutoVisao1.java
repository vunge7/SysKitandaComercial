/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package visao;


import java.sql.Connection;
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
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import kitanda.util.CfMethods;
import modelo.ProdutoModelo;
import modelo.StockModelo;
import util.BDConexao;
import util.DVML;
import util.JPAEntityMannagerFactoryUtil;
import util.MetodosUtil;
import util.NumeroDocument;
import util.PictureChooser;

public class ProdutoVisao1 extends javax.swing.JFrame
{

    private static EntityManagerFactory emf = JPAEntityMannagerFactoryUtil.em;
    private static ProdutoModelo produtoModelo;
    private static TbProduto produto;
    private static ProdutoImposto produto_imposto;
    private static Retencao retencao;
    private static ProdutoDao produtoDao = new ProdutoDao( emf );
    private DadosInstituicaoDao dadosInstituicaoDao = new DadosInstituicaoDao( emf );
    private static ProdutosMotivosIsensaoDao produtosMotivosIsensaoDao;
    private static UsuarioDao usuarioDao = new UsuarioDao( emf );
    private static PrecoDao precoDao = new PrecoDao( emf );
    private static LocalDao localDao = new LocalDao( emf );
    private static ImpostoDao impostoDao = new ImpostoDao( emf );
//    private static RetencaoDao retencaoDao = new RetencaoDao( emf );
    private static UnidadeDao unidadeDao = new UnidadeDao( emf );
    private static StockDao stockDao = new StockDao( emf );
    private static TbStock stock;
    private static TbStock stock_publico;
    public TbDadosInstituicao dadosInstituicao;
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
    private Frame parent;
    private boolean stocavel = true;
    private double preco_compra_anterior = 0, preco_venda_anterior = 0;
    private static TbPreco preco;
    private static ImageIcon imageIcon;

    static
    {
        produtosMotivosIsensaoDao = new ProdutosMotivosIsensaoDao( emf );
    }
    private Imposto imposto;

    public ProdutoVisao1( java.awt.Frame parent, boolean modal, int idUSer, BDConexao conexao )
    {

        //super(parent, modal);
        this.parent = parent;
        initComponents();
        confiLabel();
        setLocationRelativeTo( null );
        this.idUser = idUSer;

        set_combos();
        cmbImposto.setVisible( false );
        this.conexao = conexao;
        jPanel_retencao.setVisible( true );
        servico_produto();
        proximo_codigo();
        produtoController = new ProdutoController( this.conexao );
        txtCodigoProduto.setDocument( new PermitirNumeros() );
        txtQuantidadeBaixa.setDocument( new PermitirNumeros() );
        txtQuantidadeCritica.setDocument( new PermitirNumeros() );
        ck_produto.setSelected( true );
        jcDataFabrico.setDate( new Date() );
        jcDataExpiracao.setDate( new Date() );
        //txtCodigoProduto.addKeyListener( new BuscaProduto() );
        //txtPercentagemGanhoRetalho.addKeyListener( new PercentagemGanhoRetalho());
        //txtPercentagemGanhoGrosso.addKeyListener( new PercentagemGanhoGrosso());
        txtPrecoCompra.addKeyListener( new PrecoCompraPercentagem() );
        txtPrecoVenda.addKeyListener( new PrecoVendaPercentagem() );
//        txtCodigoBarraProcura.requestFocus();
//        txtCodigoProduto.requestFocus();
        setFocus( dadosInstituicaoDao.findTbDadosInstituicao( 1 ).getFoco() );
        txtPrecoCompra.setDocument( new NumeroDocument( 12, DVML.CASAS_DECIMAIS ) );
        txtPrecoCompra.setHorizontalAlignment( JTextField.RIGHT );
        txtPrecoVenda.setDocument( new NumeroDocument( 12, DVML.CASAS_DECIMAIS ) );
        txtPrecoVenda.setHorizontalAlignment( JTextField.RIGHT );
        txtPercentagemGanhoRetalho.setDocument( new NumeroDocument( 12, DVML.CASAS_DECIMAIS ) );
        txtPercentagemGanhoRetalho.setHorizontalAlignment( JTextField.RIGHT );
        txtQuantidadeCritica.setText( "5" );
        txtQuantidadeBaixa.setText( "0" );

        popularComponentes();

        KeyboardFocusManager.getCurrentKeyboardFocusManager()
                .addKeyEventDispatcher( new KeyEventDispatcher()
                {
                    @Override
                    public boolean dispatchKeyEvent( KeyEvent e )
                    {
                        if ( e.getID() == e.KEY_RELEASED && e.getKeyCode() == KeyEvent.VK_TAB )
                        {

                            txtPrecoVenda.requestFocus();
                            return true;

                        }
                        return false;
                    }
                } );

    }

    public void confiLabel()
    {

        lbTipoProduto.setHorizontalAlignment( JLabel.RIGHT );
        lbCusto.setHorizontalAlignment( JLabel.RIGHT );
        lbProduto.setHorizontalAlignment( JLabel.RIGHT );
        lbUnidade.setHorizontalAlignment( JLabel.RIGHT );
//        lbStatus.setHorizontalAlignment(JLabel.RIGHT);

    }

//    public void metodoBuscas(){
//        
//        if(dadosInstituicao.getEmail().equals("dvml.comercial@gmail.com")){
//            
//            txtCodigoManualProcura.requestFocus();
//            
//        }
//        
//        
//    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     *
     */
    @SuppressWarnings( "unchecked" )
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
        lbProduto = new javax.swing.JLabel();
        txtDesignacao = new javax.swing.JTextField();
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
        btnCancelar = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        ivaJPanel = new javax.swing.JPanel();
        ivaTaxaJLabel = new javax.swing.JLabel();
        ivaMotivoJLabel = new javax.swing.JLabel();
        ivaMotivoJComboBox = new javax.swing.JComboBox();
        ivaAplicarJRadioButton = new javax.swing.JRadioButton();
        ivaNaoAplicarJRadioButton = new javax.swing.JRadioButton();
        cmbImposto = new javax.swing.JComboBox<>();
        jPanel_retencao = new javax.swing.JPanel();
        retencaoAplicarJRadioButton = new javax.swing.JRadioButton();
        retencaoNaoAplicarJRadioButton = new javax.swing.JRadioButton();
        retencaoTaxaJTextField = new javax.swing.JTextField();
        retencaoTaxaJLabel = new javax.swing.JLabel();
        retencaoZeroTaxaJTextField = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        txtCodigoProduto = new javax.swing.JTextField();
        lbProdutos2 = new javax.swing.JLabel();
        txtCodigoBarraProcura = new javax.swing.JTextField();
        lbProdutos3 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        lbProdutos4 = new javax.swing.JLabel();
        txtCodigoManualProcura = new javax.swing.JTextField();
        cmbStatus = new javax.swing.JComboBox();
        painel_stock = new javax.swing.JPanel();
        lbQuantidade = new javax.swing.JLabel();
        lbQuantidadeCritica = new javax.swing.JLabel();
        lbQuantidadeBaixa = new javax.swing.JLabel();
        txtQuantidadeBaixa = new javax.swing.JTextField();
        txtQuantidadeCritica = new javax.swing.JTextField();
        txtQuantidade = new javax.swing.JTextField();
        lbCategoria1 = new javax.swing.JLabel();
        cmbArmazem = new javax.swing.JComboBox();
        llbDataFabrico = new javax.swing.JLabel();
        jcDataFabrico = new com.toedter.calendar.JDateChooser();
        lbDataExpiracao = new javax.swing.JLabel();
        jcDataExpiracao = new com.toedter.calendar.JDateChooser();
        lbPreco = new javax.swing.JLabel();
        txtPercentagemGanhoRetalho = new javax.swing.JTextField();
        lbStatus1 = new javax.swing.JLabel();
        lbPrecoVenda = new javax.swing.JLabel();
        txtPrecoVenda = new javax.swing.JTextField();
        lbCategoria3 = new javax.swing.JLabel();
        cmbLocal = new javax.swing.JComboBox<>();
        lbBarra1 = new javax.swing.JLabel();
        txtCodigoManual = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        lbPhoto = new javax.swing.JLabel();
        btnCarregar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("...::::: KITANDA - PRODUTO/SERVIÇO ::::...");

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lbTipoProduto.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 16)); // NOI18N
        lbTipoProduto.setText("Sub Família:");
        jPanel5.add(lbTipoProduto, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 90, 100, 22));

        cmbTipoProduto.setBackground(new java.awt.Color(4, 154, 3));
        cmbTipoProduto.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 14)); // NOI18N
        cmbTipoProduto.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbTipoProdutoActionPerformed(evt);
            }
        });
        jPanel5.add(cmbTipoProduto, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 90, 180, -1));

        lbProduto.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 16)); // NOI18N
        lbProduto.setText("Designação:");
        jPanel5.add(lbProduto, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 160, 100, 30));

        txtDesignacao.setBackground(new java.awt.Color(4, 154, 3));
        txtDesignacao.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtDesignacao.setForeground(new java.awt.Color(255, 255, 255));
        txtDesignacao.setCaretColor(new java.awt.Color(255, 255, 255));
        txtDesignacao.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtDesignacaoActionPerformed(evt);
            }
        });
        jPanel5.add(txtDesignacao, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 160, 340, 30));

        lbCusto.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 16)); // NOI18N
        lbCusto.setText("Preço Compra:");
        jPanel5.add(lbCusto, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 200, 120, -1));

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
        jPanel5.add(txtPrecoCompra, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 190, 130, 40));

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
        jPanel5.add(lbUnidade, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 200, 90, 30));

        jPanel5.add(cmbUnidade, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 200, 210, -1));

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
        jPanel5.add(lbTipoProduto3, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 50, 100, 22));

        cmbModelo.setBackground(new java.awt.Color(4, 154, 3));
        cmbModelo.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 14)); // NOI18N
        cmbModelo.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbModeloActionPerformed(evt);
            }
        });
        jPanel5.add(cmbModelo, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 50, 180, -1));

        lbTipoProduto4.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 16)); // NOI18N
        lbTipoProduto4.setText("Família:");
        jPanel5.add(lbTipoProduto4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, 70, 22));

        jPanel5.add(cmbGrupo, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 130, 190, -1));

        jLabelCodProduto.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        jLabelCodProduto.setText("Codigo");
        jPanel5.add(jLabelCodProduto, new org.netbeans.lib.awtextra.AbsoluteConstraints(395, 20, 160, -1));

        lbBarra.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 16)); // NOI18N
        lbBarra.setText("Cod Barra:");
        jPanel5.add(lbBarra, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 130, 90, 30));

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
        jPanel5.add(txtCodigoBarra, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 160, 122, -1));

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
        jPanel5.add(btnSalvar, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 440, 102, 37));

        btnAlterar2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/alterar_16x16.png"))); // NOI18N
        btnAlterar2.setText("Alterar");
        btnAlterar2.setAlignmentX(0.5F);
        btnAlterar2.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnAlterar2ActionPerformed(evt);
            }
        });
        jPanel5.add(btnAlterar2, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 440, 102, 37));

        btnCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/SAIR/sair_verde_32x32.png"))); // NOI18N
        btnCancelar.setAlignmentX(0.5F);
        btnCancelar.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnCancelarActionPerformed(evt);
            }
        });
        jPanel5.add(btnCancelar, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 440, 57, 37));

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Impostos"));

        ivaJPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Aplicar IVA ao Produto", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 16))); // NOI18N
        ivaJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        ivaTaxaJLabel.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 16)); // NOI18N
        ivaTaxaJLabel.setText("Taxa de IVA ( % )");
        ivaJPanel.add(ivaTaxaJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 140, -1));

        ivaMotivoJLabel.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 12)); // NOI18N
        ivaMotivoJLabel.setText("Motivos de exclusão");
        ivaJPanel.add(ivaMotivoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 50, 160, -1));

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
        ivaJPanel.add(ivaMotivoJComboBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 70, 300, 30));

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
        ivaJPanel.add(ivaAplicarJRadioButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, 210, -1));

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
        ivaJPanel.add(ivaNaoAplicarJRadioButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 20, 280, -1));

        cmbImposto.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbImposto.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbImpostoActionPerformed(evt);
            }
        });
        ivaJPanel.add(cmbImposto, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 70, 90, -1));

        jPanel_retencao.setBorder(javax.swing.BorderFactory.createTitledBorder("Aplicar Retenção ao Serviço"));

        buttonGroup2.add(retencaoAplicarJRadioButton);
        retencaoAplicarJRadioButton.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 12)); // NOI18N
        retencaoAplicarJRadioButton.setText("Sim ( Aplicar Retenção )");
        retencaoAplicarJRadioButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                retencaoAplicarJRadioButtonActionPerformed(evt);
            }
        });

        buttonGroup2.add(retencaoNaoAplicarJRadioButton);
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
                .addComponent(retencaoAplicarJRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(retencaoTaxaJLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(retencaoTaxaJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(retencaoNaoAplicarJRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(retencaoZeroTaxaJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel_retencaoLayout.setVerticalGroup(
            jPanel_retencaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_retencaoLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel_retencaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(retencaoAplicarJRadioButton, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(retencaoNaoAplicarJRadioButton, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(retencaoTaxaJLabel)))
            .addGroup(jPanel_retencaoLayout.createSequentialGroup()
                .addGroup(jPanel_retencaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(retencaoTaxaJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(retencaoZeroTaxaJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(2, 2, 2))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ivaJPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel_retencao, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addComponent(ivaJPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel_retencao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel5.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 240, 610, 190));

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
        jPanel3.add(txtCodigoBarraProcura, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 30, 180, -1));

        lbProdutos3.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 16)); // NOI18N
        lbProdutos3.setText("Cód. Barra:");
        jPanel3.add(lbProdutos3, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 30, 90, -1));

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/proucura.png"))); // NOI18N
        jButton4.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 20, 40, 30));

        lbProdutos4.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 16)); // NOI18N
        lbProdutos4.setText("Cód. Manual:");
        jPanel3.add(lbProdutos4, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 30, 110, -1));

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
        jPanel3.add(txtCodigoManualProcura, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 30, 170, -1));

        cmbStatus.setBackground(new java.awt.Color(4, 154, 3));
        cmbStatus.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 14)); // NOI18N
        cmbStatus.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Activo", "Desactivo" }));
        cmbStatus.setEnabled(false);
        jPanel3.add(cmbStatus, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 10, 100, 30));

        painel_stock.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        lbQuantidade.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 16)); // NOI18N
        lbQuantidade.setText("Quantidade:");

        lbQuantidadeCritica.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 16)); // NOI18N
        lbQuantidadeCritica.setText("Qt.Critica:");

        lbQuantidadeBaixa.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 16)); // NOI18N
        lbQuantidadeBaixa.setText("Qt.Baixa:");

        txtQuantidadeBaixa.setBackground(new java.awt.Color(4, 154, 3));
        txtQuantidadeBaixa.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txtQuantidadeBaixa.setForeground(new java.awt.Color(255, 255, 255));
        txtQuantidadeBaixa.setText("0");
        txtQuantidadeBaixa.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtQuantidadeBaixaActionPerformed(evt);
            }
        });

        txtQuantidadeCritica.setBackground(new java.awt.Color(4, 154, 3));
        txtQuantidadeCritica.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txtQuantidadeCritica.setForeground(new java.awt.Color(255, 255, 255));
        txtQuantidadeCritica.setText("5");
        txtQuantidadeCritica.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtQuantidadeCriticaActionPerformed(evt);
            }
        });

        txtQuantidade.setEditable(false);
        txtQuantidade.setBackground(new java.awt.Color(4, 154, 3));
        txtQuantidade.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txtQuantidade.setForeground(new java.awt.Color(255, 255, 255));
        txtQuantidade.setText("0");
        txtQuantidade.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtQuantidadeActionPerformed(evt);
            }
        });

        lbCategoria1.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 16)); // NOI18N
        lbCategoria1.setText("Armazém:");

        cmbArmazem.setBackground(new java.awt.Color(4, 154, 3));
        cmbArmazem.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 14)); // NOI18N
        cmbArmazem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbArmazemActionPerformed(evt);
            }
        });

        llbDataFabrico.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 16)); // NOI18N
        llbDataFabrico.setText("Data de Fabrico:");

        jcDataFabrico.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N

        lbDataExpiracao.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 16)); // NOI18N
        lbDataExpiracao.setText("Data de Expiracao:");

        jcDataExpiracao.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N

        lbPreco.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 16)); // NOI18N
        lbPreco.setText("Percentagem Ganho:");

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
        lbPrecoVenda.setText("Preco Venda Retalho:");

        txtPrecoVenda.setBackground(new java.awt.Color(4, 154, 3));
        txtPrecoVenda.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        txtPrecoVenda.setForeground(new java.awt.Color(255, 255, 255));
        txtPrecoVenda.setCaretColor(new java.awt.Color(255, 255, 255));
        txtPrecoVenda.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtPrecoVendaActionPerformed(evt);
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
        btnCarregar.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnCarregarActionPerformed(evt);
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
                        .addGroup(painel_stockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(lbCategoria3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lbCategoria1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 102, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(painel_stockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cmbArmazem, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbLocal, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(painel_stockLayout.createSequentialGroup()
                        .addGroup(painel_stockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(painel_stockLayout.createSequentialGroup()
                                .addGroup(painel_stockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(llbDataFabrico, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lbDataExpiracao, javax.swing.GroupLayout.DEFAULT_SIZE, 155, Short.MAX_VALUE)
                                    .addComponent(lbQuantidade, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGroup(painel_stockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(painel_stockLayout.createSequentialGroup()
                                        .addGap(13, 13, 13)
                                        .addComponent(jcDataFabrico, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(painel_stockLayout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(painel_stockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txtQuantidade, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jcDataExpiracao, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                            .addGroup(painel_stockLayout.createSequentialGroup()
                                .addComponent(lbQuantidadeCritica, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtQuantidadeCritica, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lbQuantidadeBaixa, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtQuantidadeBaixa, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(painel_stockLayout.createSequentialGroup()
                                .addGroup(painel_stockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lbPrecoVenda)
                                    .addComponent(lbPreco))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(painel_stockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(txtPercentagemGanhoRetalho, javax.swing.GroupLayout.DEFAULT_SIZE, 135, Short.MAX_VALUE)
                                    .addComponent(txtPrecoVenda))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lbStatus1, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(painel_stockLayout.createSequentialGroup()
                                .addComponent(lbBarra1, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(41, 41, 41)
                                .addGroup(painel_stockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(painel_stockLayout.createSequentialGroup()
                                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(btnCarregar))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, painel_stockLayout.createSequentialGroup()
                                        .addComponent(txtCodigoManual, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(41, 41, 41)))))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        painel_stockLayout.setVerticalGroup(
            painel_stockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, painel_stockLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(painel_stockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCarregar))
                .addGroup(painel_stockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(painel_stockLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(cmbArmazem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, painel_stockLayout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addComponent(lbCategoria1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(painel_stockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbCategoria3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(painel_stockLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(cmbLocal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(painel_stockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(painel_stockLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(lbBarra1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtCodigoManual, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(painel_stockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(llbDataFabrico, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jcDataFabrico, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(painel_stockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(painel_stockLayout.createSequentialGroup()
                        .addComponent(lbDataExpiracao)
                        .addGap(6, 6, 6)
                        .addGroup(painel_stockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lbQuantidade)
                            .addComponent(txtQuantidade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(6, 6, 6)
                        .addGroup(painel_stockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lbQuantidadeCritica)
                            .addComponent(txtQuantidadeCritica, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbQuantidadeBaixa)
                            .addComponent(txtQuantidadeBaixa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jcDataExpiracao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(painel_stockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbPreco)
                    .addComponent(txtPercentagemGanhoRetalho, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbStatus1, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(painel_stockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbPrecoVenda, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPrecoVenda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 11, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 621, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(painel_stock, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, 486, Short.MAX_VALUE)
                    .addComponent(painel_stock, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalvarActionPerformed

        if ( ck_produto.isSelected() )
        {
            procedimento_salvar_produto();

        }
        else
        {
            procedimento_salvar_servico();
        }

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
            Logger.getLogger( ProdutoVisao1.class.getName() ).log( Level.SEVERE, null, ex );
        }


    }//GEN-LAST:event_cmbTipoProdutoActionPerformed

    private void txtDesignacaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDesignacaoActionPerformed
        // TODO add your handling code here:

        if ( !txtDesignacao.getText().equals( "" ) )
        {
            txtCodigoBarra.requestFocus();
            txtCodigoBarra.setText( "" );
        }
        else
        {
            JOptionPane.showMessageDialog( null, "Pf. Digite a designação do serviço", DVML.DVML_COMERCIAL, JOptionPane.WARNING_MESSAGE );
        }


    }//GEN-LAST:event_txtDesignacaoActionPerformed

    private void txtQuantidadeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtQuantidadeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtQuantidadeActionPerformed

    private void cmbArmazemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbArmazemActionPerformed

//        try
//        {
//            ver_dados_produtos( getCodigoProduto() );
//
//        }
//        catch ( Exception e )
//        {
//        }
    }//GEN-LAST:event_cmbArmazemActionPerformed

    private void txtCodigoProdutoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCodigoProdutoActionPerformed
        // TODO add your handling code here:

        try
        {
            ver_dados_produtos( getCodigoProduto() );

        }
        catch ( Exception e )
        {
        }


    }//GEN-LAST:event_txtCodigoProdutoActionPerformed

    private void txtCodigoBarraProcuraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCodigoBarraProcuraActionPerformed
        // TODO add your handling code here:
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
            txtPrecoVenda.requestFocus();
        }


    }//GEN-LAST:event_txtPrecoCompraActionPerformed

    private void txtCodigoBarraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCodigoBarraActionPerformed
        // TODO add your handling code here:
//        txtPrecoVenda.requestFocus();
    }//GEN-LAST:event_txtCodigoBarraActionPerformed

    private void txtQuantidadeCriticaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtQuantidadeCriticaActionPerformed
        // TODO add your handling code here:
        txtPercentagemGanhoRetalho.requestFocus();
    }//GEN-LAST:event_txtQuantidadeCriticaActionPerformed

    private void txtQuantidadeBaixaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtQuantidadeBaixaActionPerformed
        // TODO add your handling code here:
        txtPercentagemGanhoRetalho.requestFocus();
    }//GEN-LAST:event_txtQuantidadeBaixaActionPerformed

    private void txtPercentagemGanhoRetalhoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPercentagemGanhoRetalhoActionPerformed
        // TODO add your handling code here:
        // procedimento_salvar_servico_produto();
        txtPrecoVenda.requestFocus();
    }//GEN-LAST:event_txtPercentagemGanhoRetalhoActionPerformed

    private void txtPrecoVendaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPrecoVendaActionPerformed
        // TODO add your handling code here:
        //txtQtdGrosso.requestFocus();
        if ( ck_produto.isSelected() )
        {
            procedimento_salvar_produto();

        }
        else
        {
            procedimento_salvar_servico();
        }
    }//GEN-LAST:event_txtPrecoVendaActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed

        try
        {

            new BuscaProdutoVisao( this, rootPaneCheckingEnabled, getIdArmazem(), DVML.JANELA_PRODUTO, conexao ).show();

        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }

    }//GEN-LAST:event_jButton4ActionPerformed

    private void ivaMotivoJComboBoxItemStateChanged(java.awt.event.ItemEvent evt)//GEN-FIRST:event_ivaMotivoJComboBoxItemStateChanged
    {//GEN-HEADEREND:event_ivaMotivoJComboBoxItemStateChanged

    }//GEN-LAST:event_ivaMotivoJComboBoxItemStateChanged

    private void ivaAplicarJRadioButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_ivaAplicarJRadioButtonActionPerformed
    {//GEN-HEADEREND:event_ivaAplicarJRadioButtonActionPerformed
        atualizarIvaForm();
    }//GEN-LAST:event_ivaAplicarJRadioButtonActionPerformed

    private void ivaNaoAplicarJRadioButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_ivaNaoAplicarJRadioButtonActionPerformed
    {//GEN-HEADEREND:event_ivaNaoAplicarJRadioButtonActionPerformed
        atualizarIvaForm();
    }//GEN-LAST:event_ivaNaoAplicarJRadioButtonActionPerformed

    private void ivaMotivoJComboBoxActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_ivaMotivoJComboBoxActionPerformed
    {//GEN-HEADEREND:event_ivaMotivoJComboBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ivaMotivoJComboBoxActionPerformed

    private void txtCodigoManualProcuraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCodigoManualProcuraActionPerformed
        buscar_by_cod_manual();
        txtCodigoManualProcura.setText( "" );
    }//GEN-LAST:event_txtCodigoManualProcuraActionPerformed

    private void ck_servicoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ck_servicoActionPerformed
        // TODO add your handling code here:
        // TODO add your handling code here:
//        cmbFamilia.setModel( new DefaultComboBoxModel( (Vector) familiaDao.buscaTodasFamiliaServico() ) );
//        cmbFamilia.setSelectedIndex(1);
        ver_retencao();
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
        if ( ck_produto.isSelected() )
        {
            procedimento_alterar_servico_produto();

        }
        else
        {
            procedimento_alterar_servico();
        }
    }//GEN-LAST:event_btnAlterar2ActionPerformed

    private void cmbFamiliaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbFamiliaActionPerformed
        try
        {
            cmbTipoProduto.setModel( new DefaultComboBoxModel( ( Vector ) tipoProdutoDao.getDescricaoByIdFamilias( getIdFamilia() ) ) );

        }
        catch ( Exception e )
        {
        }
    }//GEN-LAST:event_cmbFamiliaActionPerformed

    private void cmbMarcaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbMarcaActionPerformed
        try
        {
            cmbModelo.setModel( new DefaultComboBoxModel( ( Vector ) modeloDao.getDescricaoByIdMarca( getIdMarca() ) ) );

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
            java.util.logging.Logger.getLogger( ProdutoVisao1.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        catch ( InstantiationException ex )
        {
            java.util.logging.Logger.getLogger( ProdutoVisao1.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        catch ( IllegalAccessException ex )
        {
            java.util.logging.Logger.getLogger( ProdutoVisao1.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        catch ( javax.swing.UnsupportedLookAndFeelException ex )
        {
            java.util.logging.Logger.getLogger( ProdutoVisao1.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater( new Runnable()
        {
            public void run()
            {
                try
                {
                    ProdutoVisao1 dialog = new ProdutoVisao1( new javax.swing.JFrame(), true, 15, BDConexao.getInstancia() );
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
                    Logger.getLogger( ProdutoVisao1.class.getName() ).log( Level.SEVERE, null, ex );
                }
            }
        } );
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

        TbStock stock = stockDao.getStockByCodigoManual( txtCodigoManualProcura.getText(), getIdArmazem() );
        System.out.println( "ID STOCK " + stock.getCodigo() );

        if ( stock.getCodigo() != 0 )
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
        System.err.println( "codigo: " + codigo );
        TbProduto produto_local = produtoDao.findTbProduto( codigo );
        try
        {

            txtDesignacao.setText( produto_local.getDesignacao() );
            cmbStatus.setSelectedItem( String.valueOf( produto_local.getStatus() ) );
            txtPrecoCompra.setText( String.valueOf( produto_local.getPreco() ) );

//            cmbTipoProduto.setSelectedItem(produto.getCodTipoProduto().getDesignacao());
//            cmbModelo.setSelectedItem(produto.getFkModelo().getDesignacao());
            cmbMarca.setSelectedItem( produto_local.getFkModelo().getFkMarca().getDesignacao() );
            cmbModelo.setSelectedItem( produto_local.getFkModelo().getDesignacao() );
            cmbFamilia.setSelectedItem( produto_local.getCodTipoProduto().getFkFamilia().getDesignacao() );
            cmbTipoProduto.setSelectedItem( produto_local.getCodTipoProduto().getDesignacao() );
            //actualizar
            cmbLocal.setSelectedItem( produto_local.getCodLocal().getDesignacao() );
            cmbGrupo.setSelectedItem( produto_local.getFkGrupo().getDesignacao() );
            cmbUnidade.setSelectedItem( produto_local.getCodUnidade().getDescricao() );
//            cmbArmazem.setSelectedItem( produto_local.getCodUnidade().getDescricao() );

            //ver_dados( getCodigoProduto() );
            ver_dados( produto_local.getCodigo() );

        }
        catch ( Exception e )
        {
            e.printStackTrace();
            procedimento_limpar();
            JOptionPane.showMessageDialog( null, "Não existe produto com este código", DVML.DVML_COMERCIAL, JOptionPane.WARNING_MESSAGE );
        }

    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
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
    public static javax.swing.JComboBox cmbArmazem;
    public static javax.swing.JComboBox<String> cmbFamilia;
    public static javax.swing.JComboBox<String> cmbGrupo;
    private static javax.swing.JComboBox<String> cmbImposto;
    public static javax.swing.JComboBox<String> cmbLocal;
    public static javax.swing.JComboBox<String> cmbMarca;
    public static javax.swing.JComboBox cmbModelo;
    public static javax.swing.JComboBox cmbStatus;
    public static javax.swing.JComboBox cmbTipoProduto;
    public static javax.swing.JComboBox<String> cmbUnidade;
    private static javax.swing.JRadioButton ivaAplicarJRadioButton;
    private javax.swing.JPanel ivaJPanel;
    private static javax.swing.JComboBox ivaMotivoJComboBox;
    private static javax.swing.JLabel ivaMotivoJLabel;
    private static javax.swing.JRadioButton ivaNaoAplicarJRadioButton;
    private static javax.swing.JLabel ivaTaxaJLabel;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabelCodProduto;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    public static javax.swing.JPanel jPanel5;
    private static javax.swing.JPanel jPanel_retencao;
    public static com.toedter.calendar.JDateChooser jcDataExpiracao;
    public static com.toedter.calendar.JDateChooser jcDataFabrico;
    private javax.swing.JLabel lbBarra;
    private javax.swing.JLabel lbBarra1;
    private javax.swing.JLabel lbCategoria1;
    private javax.swing.JLabel lbCategoria3;
    private javax.swing.JLabel lbCusto;
    private javax.swing.JLabel lbDataExpiracao;
    private static javax.swing.JLabel lbPhoto;
    private javax.swing.JLabel lbPreco;
    private javax.swing.JLabel lbPrecoVenda;
    private javax.swing.JLabel lbProduto;
    private javax.swing.JLabel lbProdutos2;
    private javax.swing.JLabel lbProdutos3;
    private javax.swing.JLabel lbProdutos4;
    private javax.swing.JLabel lbQuantidade;
    private javax.swing.JLabel lbQuantidadeBaixa;
    private javax.swing.JLabel lbQuantidadeCritica;
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
    public static javax.swing.JTextField txtCodigoBarra;
    public static javax.swing.JTextField txtCodigoBarraProcura;
    public static javax.swing.JTextField txtCodigoManual;
    public static javax.swing.JTextField txtCodigoManualProcura;
    private static javax.swing.JTextField txtCodigoProduto;
    public static javax.swing.JTextField txtDesignacao;
    public static javax.swing.JTextField txtPercentagemGanhoRetalho;
    public static javax.swing.JTextField txtPrecoCompra;
    public static javax.swing.JTextField txtPrecoVenda;
    public static javax.swing.JTextField txtQuantidade;
    public static javax.swing.JTextField txtQuantidadeBaixa;
    public static javax.swing.JTextField txtQuantidadeCritica;
    // End of variables declaration//GEN-END:variables

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
            cmbFamilia.setEnabled( true );
        }
        else
        {
            painel_stock.setVisible( false );
            cmbFamilia.setSelectedItem( "Serviços" );
            cmbFamilia.setEnabled( false );
        }
        // txtDesignacao.requestFocus();

    }
//    private static void servico_produto()
//    {
//
//        if ( ck_produto.isSelected() )
//        {
//            painel_stock.setVisible( true );
//            cmbTipoProduto.setEnabled( true );
//        }
//        else
//        {
//            painel_stock.setVisible( false );
//            cmbTipoProduto.setSelectedItem( "Serviços" );
//            cmbTipoProduto.setEnabled( false );
//        }
//        // txtDesignacao.requestFocus();
//
//    }

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

    private void set_dados_produto_alterar()
    {

        String codigoBarraText = txtCodigoBarra.getText();
        String codigoBarra = ( codigoBarraText.isEmpty() ) ? String.valueOf( new ProdutoDao().findTbProdutoEntities().size() ) : codigoBarraText;
        TbProduto tbProduto = ProdutoDao.findProdutoByCodigoBarra( codigoBarra );

        String designacao_produto = getDesignacaoText();

        this.produto.setDesignacao( designacao_produto );
        this.produto.setPreco( new BigDecimal( MetodosUtil.convertToDouble( txtPrecoCompra.getText() ) ) );
        this.produto.setCodTipoProduto( tipoProdutoDao.findTbTipoProduto( getIdTipoPrdouto() ) );
        this.produto.setFkModelo( modeloDao.findModelo( getIdModelo() ) );
        this.produto.setFkGrupo( grupoDao.findGrupo( getIdGrupo() ) );
        this.produto.setCodFornecedores( fornecedorDao.findTbFornecedor( 1 ) );
        this.produto.setCodLocal( localDao.findTbLocal( getIdLocal() ) );
        this.produto.setCodUnidade( unidadeDao.findUnidade( getIdUnidade() ) );
        this.produto.setCodigoManual( txtCodigoManual.getText() );
        this.produto.setDataFabrico( jcDataFabrico.getDate() );
        this.produto.setDataEntrada( new Date() );
        this.produto.setDataExpiracao( jcDataExpiracao.getDate() );
        this.produto.setCodBarra( txtCodigoBarra.getText().trim() );
        this.produto.setStatus( cmbStatus.getSelectedItem().toString() );
        this.produto.setStocavel( "true" );

        try
        {

//            if ( image_View.getBystebyteImg () != null )
//            {
//                produto.setPhoto ( image_View.getBystebyteImg () );
//            }
            // image_View.chooseImg( lbPhoto );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            // produto.setPhoto( null );
        }

    }

    private void set_dados_produto()
    {

        String codigoBarraText = txtCodigoBarra.getText();
        String codigoBarra = ( codigoBarraText.isEmpty() ) ? String.valueOf( new ProdutoDao().findTbProdutoEntities().size() ) : codigoBarraText;
        TbProduto tbProduto = ProdutoDao.findProdutoByCodigoBarra( codigoBarra );

        if ( Objects.isNull( tbProduto ) )
        {
            String designacao_produto = getDesignacaoText();

            this.produto.setDesignacao( designacao_produto );
            this.produto.setPreco( new BigDecimal( MetodosUtil.convertToDouble( txtPrecoCompra.getText() ) ) );
            this.produto.setCodTipoProduto( tipoProdutoDao.findTbTipoProduto( getIdTipoPrdouto() ) );
            this.produto.setFkModelo( modeloDao.findModelo( getIdModelo() ) );
            this.produto.setFkGrupo( grupoDao.findGrupo( getIdGrupo() ) );
            this.produto.setCodFornecedores( fornecedorDao.findTbFornecedor( 1 ) );
            this.produto.setCodLocal( localDao.findTbLocal( getIdLocal() ) );
            this.produto.setCodUnidade( unidadeDao.findUnidade( getIdUnidade() ) );
            this.produto.setCodigoManual( txtCodigoManual.getText() );
            this.produto.setDataFabrico( jcDataFabrico.getDate() );
            this.produto.setDataEntrada( new Date() );
            this.produto.setDataExpiracao( jcDataExpiracao.getDate() );
            this.produto.setCodBarra( txtCodigoBarra.getText().trim() );
            this.produto.setStatus( cmbStatus.getSelectedItem().toString() );
            this.produto.setStocavel( "true" );

            try
            {
                if ( image_View.getBystebyteImg() != null )
                {
                    produto.setPhoto( image_View.getBystebyteImg() );
                }
                // image_View.chooseImg( lbPhoto );
            }
            catch ( Exception e )
            {
                e.printStackTrace();
                // produto.setPhoto( null );
            }
        }
        else
        {
            JOptionPane.showMessageDialog( null, "Este codigo de barra já existe!" );
        }
    }

    private void set_dados_stock()
    {
        this.stock.setDataEntrada( new Date() );
        this.stock.setQuantidadeExistente( 0d );
        this.stock.setStatus( "" );
        this.stock.setPrecoVenda( new BigDecimal( MetodosUtil.convertToDouble( txtPrecoVenda.getText() ) ) );
//        this.stock.setPrecoVendaGrosso( this.stock.getPrecoVenda() );
        this.stock.setQtdGrosso( DVML.QTD_DEFAULT );
        this.stock.setQuantCritica( Integer.parseInt( txtQuantidadeCritica.getText() ) );
        this.stock.setQuantBaixa( Integer.parseInt( txtQuantidadeBaixa.getText() ) );
        this.stock.setQuantidadeAntiga( 0d );
        this.stock.setQuantidadeExistente( Double.parseDouble( txtQuantidade.getText() ) );
        this.stock.setCodArmazem( armazemDao.findTbArmazem( getIdArmazem() ) );

    }

    private void set_dados_stock_alterar()
    {
        this.stock.setDataEntrada( new Date() );
        this.stock.setStatus( "" );
        this.stock.setPrecoVenda( new BigDecimal( MetodosUtil.convertToDouble( txtPrecoVenda.getText() ) ) );
//        this.stock.setPrecoVendaGrosso( this.stock.getPrecoVenda() );
        this.stock.setQuantCritica( Integer.parseInt( txtQuantidadeCritica.getText() ) );
        this.stock.setQuantBaixa( Integer.parseInt( txtQuantidadeBaixa.getText() ) );
        this.stock.setCodArmazem( armazemDao.findTbArmazem( getIdArmazem() ) );

    }

    private void set_dados_servicos()
    {
        this.produto.setDesignacao( txtDesignacao.getText() );
//        this.produto.setPreco( (double) MetodosUtil.convertToDouble( txtPrecoCompra.getText() ) );
//        this.produto.setPreco( ( double ) MetodosUtil.convertToDouble( txtPrecoCompra.getText() ) );
//        this.produto.setCodTipoProduto( tipoProdutoDao.getTipoProdutoByDescricao( "Serviços" ) );
        this.produto.setCodTipoProduto( tipoProdutoDao.getTipoProdutoByDescricao( String.valueOf( cmbTipoProduto.getSelectedItem() ) ) );
        this.produto.setFkModelo( modeloDao.findModelo( getIdModelo() ) );
        this.produto.setFkGrupo( grupoDao.findGrupo( getIdGrupo() ) );
        this.produto.setCodFornecedores( fornecedorDao.findTbFornecedor( 1 ) );
        produto.setCodUnidade( unidadeDao.findUnidade( getIdUnidade() ) );
        produto.setCodLocal( localDao.findTbLocal( 1 ) );
        this.produto.setDataFabrico( new Date() );
        this.produto.setDataEntrada( new Date() );
        this.produto.setDataExpiracao( new Date() );

//        this.produto.setCodBarra( txtCodigoBarra.getText().trim() );
        this.produto.setCodBarra( "2147483647" );

//        this.produto.setCodBarra(Long.parseLong( "2147483647" ));
        this.produto.setStatus( cmbStatus.getSelectedItem().toString() );
        this.produto.setStocavel( "false" );

        try
        {
            if ( image_View.getBystebyteImg() != null )
            {
                produto.setPhoto( image_View.getBystebyteImg() );
            }
            // image_View.chooseImg( lbPhoto );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            produto.setPhoto( null );
        }
    }

    private void set_dados_stock_servico()
    {
        this.stock.setDataEntrada( new Date() );
//        this.stock.setQuantidadeExistente( Double.MAX_VALUE );
        this.stock.setQuantidadeExistente( DVML.QTD_EXISTENTE_PADRAO_SERVICO );
        this.stock.setStatus( "Activo" );
        this.stock.setPrecoVenda( new BigDecimal( MetodosUtil.convertToDouble( txtPrecoCompra.getText() ) ) );
//        this.stock.setPrecoVendaGrosso( 0.2f );
//        this.stock.setPrecoVendaGrosso( new BigDecimal( 0d ) );
        this.stock.setQtdGrosso( DVML.QTD_DEFAULT );
        this.stock.setQuantCritica( 1000 );
        this.stock.setQuantBaixa( 0 );
        this.stock.setQuantidadeAntiga( 0d );
        this.stock.setCodArmazem( armazemDao.findTbArmazem( 1 ) );

    }

    private void procedimento_salvar_produto()
    {
        if ( campos_validos() )
        {

            this.stock = new TbStock();
            String designacao_produto = getDesignacaoText();

            //ALTERAR PRODUTO
            if ( produtoDao.exist_designacao_produto( designacao_produto ) )
            {
                try
                {

                    this.produto = produtoDao.getProdutoByDescricao( designacao_produto );
                    set_dados_produto();
                    produtoDao.edit( this.produto );

                }
                catch ( Exception e )
                {
                }

            }
            //CRIAR NOVO PRODUTO
            else
            {

                this.produto = new TbProduto();
                set_dados_produto();
                produtoDao.create( this.produto );

                this.produto = produtoDao.findTbProduto( produtoDao.getUltimoProduto() );

                if ( !ivaAplicarJRadioButton.isSelected() )//APLICAR ISENÇÃO
                {
                    String regimeIsencao = ( String ) ivaMotivoJComboBox.getSelectedItem();
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
            }

            if ( !stockDao.exist_produto_stock( produtoDao.getProdutoByDescricao( designacao_produto ).getCodigo(), getIdArmazem() ) )
            {
                this.produto = produtoDao.getProdutoByDescricao( designacao_produto );

                try
                {

                    this.stock.setCodProdutoCodigo( this.produto );
                    set_dados_stock();
                    stockDao.create( stock );

                    /*PRIMEIRO PRECO RETALHO*/
                    setDadosPreco();
                    preco.setQtdBaixo( 1 );
                    preco.setQtdAlto( ( int ) DVML.QTD_DEFAULT - 1 );
                    //preco.setPrecoVenda( MetodosUtil.retirar_dizimas( Double.parseDouble(  txtPrecoVenda.getText()  )  )   );        
                    preco.setPrecoVenda( new BigDecimal( MetodosUtil.convertToDouble( txtPrecoVenda.getText() ) ) );
                    precoDao.create( preco );

                    /*SEGUNDO PRECO GROSSO*/
                    setDadosPreco();
                    preco.setQtdBaixo( ( int ) DVML.QTD_DEFAULT );
                    preco.setQtdAlto( 214748364 );
                    preco.setPrecoVenda( new BigDecimal( MetodosUtil.convertToDouble( txtPrecoVenda.getText() ) ) );
                    precoDao.create( preco );

                    JOptionPane.showMessageDialog( null, "Dados salvos com sucesso!", DVML.DVML_COMERCIAL, JOptionPane.INFORMATION_MESSAGE );
                    txtDesignacao.requestFocus();
                    detalhe_produto();
                    proximo_codigo();
                    procedimento_limpar();

                }
                catch ( Exception e )
                {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog( null, "Dados salvos com sucesso!", DVML.DVML_COMERCIAL, JOptionPane.INFORMATION_MESSAGE );
//                    JOptionPane.showMessageDialog ( null, "Falha ao salvar os dados", DVML.DVML_COMERCIAL, JOptionPane.INFORMATION_MESSAGE );
                }

            }
            else
            {
                JOptionPane.showMessageDialog( null, "O " + designacao_produto + "  já se encontra no armazén " + cmbArmazem.getSelectedItem().toString(), DVML.DVML_COMERCIAL, JOptionPane.WARNING_MESSAGE );
            }

        }
        else
        {
            JOptionPane.showMessageDialog( null, "Atenção\nEste código de barra já consta na base de dados! ", "AVISO", JOptionPane.WARNING_MESSAGE );
        }

    }

    private void procedimento_alterar_servico_produto()
    {

        this.produto = this.stock_publico.getCodProdutoCodigo();
        this.stock = this.stock_publico;

        //STOCK         
        //if( validado_stock() )
        if ( campos_validos() )
        {
            try
            {
                set_dados_produto_alterar();
                System.out.println( "PRODUTO " + this.produto.getDesignacao() );
                try
                {
                    produtoDao.edit( this.produto );
                    ivaAtualizar( produto.getCodigo() );
                }
                catch ( Exception e )
                {
                    e.printStackTrace();
                }

                set_dados_stock_alterar();

                if ( !Objects.isNull( stock_publico ) )
                {
                    try
                    {
//                        new StockDao().edit( stock_publico );
                        stockDao.edit( stock_publico );
                    }
                    catch ( Exception e )
                    {
                        System.err.println( "Excepção!" );
                    }
                }
                System.out.println( "Houve Alteracao" );

                /*PRIMEIRO PRECO RETALHO*/
                setDadosPreco();
                preco.setQtdBaixo( 1 );
                preco.setQtdAlto( ( int ) DVML.QTD_DEFAULT - 1 );
                preco.setPrecoVenda( new BigDecimal( MetodosUtil.convertToDouble( txtPrecoVenda.getText() ) ) );
                precoDao.create( preco );

                /*SEGUNDO PRECO GROSSO*/
                setDadosPreco();
                preco.setQtdBaixo( ( int ) DVML.QTD_DEFAULT );
                preco.setQtdAlto( 214748364 );
                preco.setPrecoVenda( new BigDecimal( MetodosUtil.convertToDouble( txtPrecoVenda.getText() ) ) );
                precoDao.create( preco );

                JOptionPane.showMessageDialog( null, "Dados alterados com sucesso!", DVML.DVML_COMERCIAL, JOptionPane.INFORMATION_MESSAGE );

            }
            catch ( Exception e )
            {
                e.printStackTrace();
                JOptionPane.showMessageDialog( null, "Falha ao alterar os dados", DVML.DVML_COMERCIAL, JOptionPane.INFORMATION_MESSAGE );
            }
        }

    }

    private void set_combos()
    {
        cmbMarca.setModel( new DefaultComboBoxModel( ( Vector ) marcaDao.buscaTodasMarcas() ) );
        cmbGrupo.setModel( new DefaultComboBoxModel( ( Vector ) grupoDao.buscaTodos() ) );
        try
        {

            cmbModelo.setModel( new DefaultComboBoxModel( ( Vector ) modeloDao.getDescricaoByIdMarca( getIdMarca() ) ) );

        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }

//        cmbFamilia.setModel( new DefaultComboBoxModel( (Vector) familiaDao.buscaFamiliasSelecione() ) );
        try
        {
            cmbFamilia.setModel( new DefaultComboBoxModel( familiaDao.buscaFamiliasSelecione() ) );
            cmbTipoProduto.setModel( new DefaultComboBoxModel( ( Vector ) tipoProdutoDao.getDescricaoByIdFamilias( getIdFamilia() ) ) );

        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
        cmbLocal.setModel( new DefaultComboBoxModel( localDao.buscaTodos() ) );
//        cmbImposto.setModel( new DefaultComboBoxModel( impostoDao.buscaTodos() ) );
        cmbUnidade.setModel( new DefaultComboBoxModel( unidadeDao.buscaTodos() ) );
        cmbArmazem.setModel( new DefaultComboBoxModel( armazemDao.buscaTodos() ) );
//        cmbFornecedores.setModel(new DefaultComboBoxModel(fornecedorDao.buscaTodos()));
//        lbStatus.setVisible(false);
        cmbStatus.setVisible( false );

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

    private static int getIdArmazem()
    {
        return armazemDao.getArmazemByDescricao( String.valueOf( cmbArmazem.getSelectedItem() ) ).getCodigo();
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
        // this.stock_publico =  stockDao.get_stock_by_id_produto_and_id_armazem(codigo, getIdArmazem());
        stock_publico = BDConexao.getStockByIdProdutoAndIdArmazem( codigo, getIdArmazem(), conexao );

        //BDConexao.existe_produto_armazem(codigo, codigo)
        if ( stock_publico.getCodigo() != 0 )
        {
            mostrar_painel( stock_publico.getCodProdutoCodigo() );
            System.out.println( "Cheguei aqui" );
            try
            {
                preco = precoDao.findTbPreco( precoDao.getUltimoIdPrecoByIdProduto( stock_publico.getCodProdutoCodigo().getCodigo() ) );
            }
            catch ( Exception e )
            {
                e.printStackTrace();
            }
            cmbArmazem.setSelectedItem( stock_publico.getCodArmazem().getDesignacao() );
//            cmbTipoProduto.setSelectedItem( stock_publico.getCodProdutoCodigo().getCodTipoProduto().getDesignacao() );
            cmbMarca.setSelectedItem( stock_publico.getCodProdutoCodigo().getFkModelo().getFkMarca().getDesignacao() );
            cmbModelo.setSelectedItem( stock_publico.getCodProdutoCodigo().getFkModelo().getDesignacao() );
            cmbFamilia.setSelectedItem( stock_publico.getCodProdutoCodigo().getCodTipoProduto().getFkFamilia().getDesignacao() );
            cmbTipoProduto.setSelectedItem( stock_publico.getCodProdutoCodigo().getCodTipoProduto().getDesignacao() );
//            cmbArmazem.setSelectedItem(stock_publico.getCodArmazem().getDesignacao());

            try
            {
                if ( !Objects.isNull( stock_publico.getCodProdutoCodigo().getPhoto() ) )
                {
                    imageIcon = new ImageIcon( stock_publico.getCodProdutoCodigo().getPhoto() );
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

                cmbLocal.setSelectedItem( stock_publico.getCodProdutoCodigo().getCodLocal().getDesignacao() );
                cmbModelo.setSelectedItem( stock_publico.getCodProdutoCodigo().getFkModelo().getDesignacao() );
                cmbGrupo.setSelectedItem( stock_publico.getCodProdutoCodigo().getFkGrupo().getDesignacao() );
//                cmbArmazem.setSelectedItem(stock_publico.getCodArmazem().getDesignacao());
            }
            catch ( Exception e )
            {
                e.printStackTrace();
            }
            cmbArmazem.setSelectedItem( stock_publico.getCodArmazem().getDesignacao() );
            cmbUnidade.setSelectedItem( stock_publico.getCodProdutoCodigo().getCodUnidade().getDescricao() );
            txtDesignacao.setText( stock_publico.getCodProdutoCodigo().getDesignacao() );
            //txtPrecoCompra.setText( String.valueOf( precoDao.getLastPrecoByIdProduto( stock_publico.getCodProdutoCodigo().getCodigo() ).getPrecoCompra() ) );
            System.out.println( "PRECO VENDA @@@@@@:" + precoDao.getLastPrecoByIdProduto( stock_publico.getCodProdutoCodigo().getCodigo() ).getPrecoCompra() );

            Double preco_compra = precoDao.getLastPrecoByIdProduto( stock_publico.getCodProdutoCodigo().getCodigo() ).getPrecoCompra().doubleValue();

            txtPrecoCompra.setText( CfMethods.formatarComoPorcoes( preco_compra ) );

//            try {
//                cmbFornecedores.setSelectedItem(stock_publico.getCodProdutoCodigo().getCodFornecedores().getNome());
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
            txtCodigoBarra.setText( String.valueOf( stock_publico.getCodProdutoCodigo().getCodBarra() ) );
            txtCodigoManual.setText( String.valueOf( stock_publico.getCodProdutoCodigo().getCodigoManual() ) );
            txtCodigoBarraProcura.setText( String.valueOf( stock_publico.getCodProdutoCodigo().getCodBarra() ) );
            txtCodigoManualProcura.setText( String.valueOf( stock_publico.getCodProdutoCodigo().getCodigoManual() ) );
            jcDataExpiracao.setDate( stock_publico.getCodProdutoCodigo().getDataExpiracao() );
            jcDataFabrico.setDate( stock_publico.getCodProdutoCodigo().getDataFabrico() );
            txtQuantidade.setText( String.valueOf( conexao.getQtdExistenteStock( codigo, getIdArmazem() ) ) );

            BigDecimal bigDecimalCritica = new BigDecimal( stock_publico.getQuantCritica() );
            BigDecimal bigDecimalBaixa = new BigDecimal( stock_publico.getQuantBaixa() );
            int qtd_critica = bigDecimalCritica.intValue();
            int qtd_baixa = bigDecimalBaixa.intValue();
//            System.out.println( "Qtd Baixa: " + stock_publico.getQuantBaixa() );
            txtQuantidadeCritica.setText( String.valueOf( qtd_critica ) );
            txtQuantidadeBaixa.setText( String.valueOf( qtd_baixa ) );

            System.err.println( "TEM ISENCAO: " + !new ProdutoImpostoDao( emf ).exist( stock_publico.getCodProdutoCodigo().getCodigo() ) );

            Integer pkProduto = stock_publico.getCodProdutoCodigo().getCodigo();
            boolean temIva = new ProdutoImpostoDao( emf ).exist( pkProduto );

            if ( !temIva )
            {
                String regime = new ProdutoIsentoDao( emf ).getRegimeIsensaoByIdProduto( pkProduto );
                ivaMotivoJComboBox.setSelectedItem( regime );
            }

            ivaAplicarJRadioButton.setSelected( temIva );
            ivaNaoAplicarJRadioButton.setSelected( !temIva );

            atualizarIvaForm();

            /*Preço Venda a Retalho*/
//            txtPrecoVenda.setText(
//                    String.valueOf(
//                            MetodosUtil.retirar_dizimas(
//                                    precoDao.findTbPreco(
//                                            precoDao.getUltimoIdPrecoByIdProduto(
//                                                    stock_publico.getCodProdutoCodigo().getCodigo(),
//                                                    DVML.QTD_DEFAULT - 1 )
//                                    ).getPrecoVenda()
//                            )
//                    )
//            );
            int idPreco = precoDao.getUltimoIdPrecoByIdProduto( stock_publico.getCodProdutoCodigo().getCodigo(), DVML.QTD_DEFAULT - 1 );

            TbPreco precoLocal = precoDao.findTbPreco( idPreco );
            double preco_venda = precoLocal.getPrecoVenda().doubleValue();
            double percentagem_ganho = precoLocal.getPercentagemGanho().doubleValue();
//            double preco_venda = precoDao.findTbPreco(
//                    precoDao.getUltimoIdPrecoByIdProduto(
//                            stock_publico.getCodProdutoCodigo().getCodigo(),
//                            DVML.QTD_DEFAULT - 1 )
//            ).getPrecoVenda();

            System.out.println( "@@@@@@@@ PRECO VENDA @@@@@@@@@@: " + CfMethods.formatarComoPorcoes( preco_venda ) );

            txtPrecoVenda.setText(
                    String.valueOf( CfMethods.formatarComoPorcoes( preco_venda ) )
            );
            System.out.println( "@@@@@@@@ PERCENTAGEM GANHO @@@@@@@@@@: " + CfMethods.formatarComoPorcoes( percentagem_ganho ) );

            txtPercentagemGanhoRetalho.setText( CfMethods.formatarComoPorcoes( percentagem_ganho ) );
            txtCodigoProduto.setText( String.valueOf( stock_publico.getCodProdutoCodigo().getCodigo() ) );

//            txtPercentagemGanhoRetalho.setText(
//                    String.valueOf(
//                            MetodosUtil.percentagem_ganho(
//                                    precoDao.findTbPreco(
//                                            precoDao.getUltimoIdPrecoByIdProduto( stock_publico.getCodProdutoCodigo().getCodigo(),
//                                                    DVML.QTD_DEFAULT ) - 1
//                                    ).getPrecoCompra(),
//                                    precoDao.findTbPreco(
//                                            precoDao.getUltimoIdPrecoByIdProduto( stock_publico.getCodProdutoCodigo().getCodigo(),
//                                                    DVML.QTD_DEFAULT ) - 1
//                                    ).getPrecoVenda()
//                            )
//                    )
//            );
            if ( stock_publico.getCodProdutoCodigo().getStocavel().equals( "true" ) )
            {
                ck_produto.setSelected( true );
            }
            else
            {
                ck_produto.setSelected( false );
            }

            servico_produto();

        }
        else
        {
            limpar_direito();
            JOptionPane.showMessageDialog( null, "O Produto ainda não se encontra no armazém " + cmbArmazem.getSelectedItem().toString(), DVML.DVML_COMERCIAL, JOptionPane.INFORMATION_MESSAGE );

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

    private static void limpar_esquerdo()
    {
        lbPhoto.setIcon( null );
        txtDesignacao.setText( "" );
        txtPrecoCompra.setText( "" );
    }

    private static void limpar_direito()
    {

        txtCodigoBarra.setText( "0" );
        txtQuantidade.setText( "0" );
        txtQuantidadeCritica.setText( "5" );
        txtQuantidadeBaixa.setText( "0" );
        txtPrecoVenda.setText( "" );
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
        else if ( txtPrecoVenda.getText().equals( "" ) )
        {
            txtPrecoVenda.requestFocus();
            JOptionPane.showMessageDialog( null, "Pf Insira o preço unitário do produto", DVML.DVML_COMERCIAL, JOptionPane.WARNING_MESSAGE );
            return false;
        }
        return true;

    }

    public boolean exist_produto_stock( int codigo_produto )
    {

        try
        {
            return stockDao.exist_produto_stock( codigo_produto, getIdArmazem() );
        }
        catch ( Exception e )
        {
            return false;
        }

    }

    private void buscar_by_cod_barra()
    {

        TbStock stock = stockDao.getStockByCodBarra( txtCodigoBarraProcura.getText().trim(), getIdArmazem() );
        System.out.println( "ID STOCK " + stock.getCodigo() );

        if ( stock.getCodigo() != 0 )
        {

            ver_dados( stock.getCodProdutoCodigo().getCodigo() );

            txtCodigoBarraProcura.requestFocus();
        }
        else
        {
            procedimento_limpar();
        }

    }

    private void detalhe_produto()
    {

        String designacao_produto = getDesignacaoText();
        TbProduto produto = produtoDao.getProdutoByDescricao( designacao_produto );

        String output = "";
        TbPreco preco = precoDao.getPrecoByIdProduto( produto.getCodigo() );
        output += "CodInterno           : " + produto.getCodigo() + "\n";
        output += "CodManual           : " + produto.getCodigoManual() + "\n";
        output += "Designação           : " + produto.getDesignacao().toUpperCase() + "\n";
        output += "Unidade           : " + produto.getCodUnidade().getDescricao().toUpperCase() + "\n";
        output += "Preço da Compra      : " + preco.getPrecoCompra() + "\n";
        output += "Percentagem Ganho    : " + preco.getPercentagemGanho() + "  % \n";
        output += "Preço de Venda       : " + preco.getPrecoVenda() + "\n";

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
        preco.setFkProduto( produtoDao.getProdutoByDescricao( getDesignacaoText() ) );
        preco.setFkUsuario( usuarioDao.findTbUsuario( idUser ) );

    }

    private boolean campos_validos()
    {

        MetodosUtil.getValorTransformadoString( preco_venda_anterior );

        String designacao_produto = getDesignacaoText();

        if ( designacao_produto.equals( "" ) )
        {

            JOptionPane.showMessageDialog( null, "Caro usuário " + usuarioDao.findTbUsuario( this.idUser ).getNome() + " " + usuarioDao.findTbUsuario( this.idUser ).getSobreNome() + " por favor digite a designação do produto" );
            txtPrecoCompra.requestFocus();
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
        else if ( txtPrecoVenda.getText().equals( "" ) )
        {

            JOptionPane.showMessageDialog( null, "Caro usuário " + usuarioDao.findTbUsuario( this.idUser ).getNome() + " " + usuarioDao.findTbUsuario( this.idUser ).getSobreNome() + " , insira o preço da venda do produto" );
            txtPrecoVenda.requestFocus();
            return false;

        }
        else if ( txtPrecoVenda.getText().equals( "0" ) )
        {
            JOptionPane.showMessageDialog( null, "Caro usuário " + usuarioDao.findTbUsuario( this.idUser ).getNome() + " " + usuarioDao.findTbUsuario( this.idUser ).getSobreNome() + ",  o preço da venda não pode ser igual a zero(0)." );
            txtPrecoVenda.requestFocus();
            return false;
        }
//        else if ( MetodosUtil.convertToDouble( txtPrecoVenda.getText() ) <= MetodosUtil.convertToDouble( txtPrecoCompra.getText() ) )
//        {
//
//            JOptionPane.showMessageDialog( null, "Caro usuário " + usuarioDao.findTbUsuario( this.idUser ).getNome() + " " + usuarioDao.findTbUsuario( this.idUser ).getSobreNome() + ", o preço da venda não pode ser menor ou igual ao preço da compra." );
//            txtPrecoVenda.requestFocus();
//            return false;
//
//        } //        else if (cmbLocal.getSelectedItem().equals("--SELECIONE--")) {
        //                     JOptionPane.showMessageDialog(null, "Pf. Selecione o local de armazenamento", DVML.DVML_COMERCIAL, JOptionPane.WARNING_MESSAGE);
        //                     return false;
        //        }
        else if ( cmbUnidade.getSelectedItem().equals( "--SELECIONE--" ) )
        {
            JOptionPane.showMessageDialog( null, "Pf. Selecione a unidade de medida do produto", DVML.DVML_COMERCIAL, JOptionPane.WARNING_MESSAGE );
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
        imposto = new ImpostoDao( emf ).findFirst();
//        ivaTaxaJTextField.setText( MetodosUtil.formatarComoPercentagem( imposto.getTaxa() ) );

        ivaMotivoJComboBox.setSelectedItem( "IVA - Regime de não sujeição" );

        atualizarIvaForm();

        MetodosUtil.FUNCAO_F1( this, rootPaneCheckingEnabled, getIdArmazem(), DVML.JANELA_PRODUTO );

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
        ver_retencao();
//        if ( ivaAplicarJRadioButton.isSelected() )
//        {
//            Imposto imposto = new ImpostoDao( emf ).findFirst();
//            ivaTaxaJTextField.setText( MetodosUtil.formatarComoPercentagem( imposto.getTaxa() ) );
//        }
//        else
//        {
//            ivaTaxaJTextField.setText( MetodosUtil.formatarComoPercentagem( 0.0 ) );
//
//        }
//        cmbImposto.setVisible( true );
//        cmbImposto.setModel( new DefaultComboBoxModel( impostoDao.buscaTodos() ) );
        cmbImposto.setVisible( ivaAplicarJRadioButton.isSelected() );
        ivaTaxaJLabel.setVisible( cmbImposto.isVisible() );

        ivaMotivoJComboBox.setVisible( !cmbImposto.isVisible() );
        ivaMotivoJLabel.setVisible( ivaMotivoJComboBox.isVisible() );

//        ivaTaxaJTextField.setVisible( ivaAplicarJRadioButton.isSelected() );
//        ivaTaxaJLabel.setVisible( ivaTaxaJTextField.isVisible() );
//
//        ivaMotivoJComboBox.setVisible( !ivaTaxaJTextField.isVisible() );
//        ivaMotivoJLabel.setVisible( ivaMotivoJComboBox.isVisible() );
    }

    private void ivaAtualizar( int fkProduto )
    {
        ProdutoIsentoDao produtoIsentoDao = new ProdutoIsentoDao( emf );
        ProdutoImpostoDao produtoImpostoDao = new ProdutoImpostoDao( emf );

        if ( ivaAplicarJRadioButton.isSelected() )
        {
            produtoImpostoDao.aplicarIva( fkProduto, imposto.getPkImposto() );
        }
        else
        {
            String regimeIsencao = ( String ) ivaMotivoJComboBox.getSelectedItem();
            ProdutosMotivosIsensao isensao = produtosMotivosIsensaoDao.findByRegime( regimeIsencao );
            produtoIsentoDao.aplicarIsencao( fkProduto, isensao.getPkProdutosMotivosIsensao() );
        }
    }

    private void aplicar_iva()
    {
        if ( !ivaAplicarJRadioButton.isSelected() )//APLICAR ISENÇÃO
        {
            String regimeIsencao = ( String ) ivaMotivoJComboBox.getSelectedItem();
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

    private void procedimento_salvar_servico()
    {
        if ( true )
        {

            stock = new TbStock();

            String designcao_produto = getDesignacaoText();
            //ALTERAR PRODUTO
            if ( produtoDao.exist_designacao_produto( designcao_produto ) )
            {
                try
                {

                    this.produto = produtoDao.getProdutoByDescricao( designcao_produto );
                    set_dados_servicos();
                    produtoDao.edit( produto );

                }
                catch ( Exception e )
                {
                }

            } //CRIAR NOVO SERVIÇO
            else
            {

                this.produto = new TbProduto();
                set_dados_servicos();
                produtoDao.create( this.produto );
                this.produto = produtoDao.findTbProduto( produtoDao.getUltimoProduto() );
                aplicar_iva();
//                aplicar_retencao();
            }

            if ( retencaoAplicarJRadioButton.isSelected() )
            {
                ServicoRetencaoDao servicoRetencaoDao = new ServicoRetencaoDao( emf );
                Retencao retencao = new RetencaoDao( emf ).findFirst();
                ServicoRetencao produtoRetencao = new ServicoRetencao();
                produtoRetencao.setFkProduto( produto );
                produtoRetencao.setFkRetencao( retencao );

                servicoRetencaoDao.create( produtoRetencao );

            }
            else //APLICAR RETENCAO
            {
                ServicoRetencaoDao servicoRetencaoDao = new ServicoRetencaoDao( emf );
                Retencao retencao = new RetencaoDao( emf ).findLast();
                ServicoRetencao produtoRetencao = new ServicoRetencao();
                produtoRetencao.setFkProduto( produto );
                produtoRetencao.setFkRetencao( retencao );

                servicoRetencaoDao.create( produtoRetencao );

            }

            if ( !stockDao.exist_produto_stock( produtoDao.getProdutoByDescricao( designcao_produto ).getCodigo(), getIdArmazem() ) )
            {
                this.produto = produtoDao.getProdutoByDescricao( designcao_produto );

                try
                {

                    this.stock.setCodProdutoCodigo( this.produto );
                    set_dados_stock_servico();
                    stockDao.create( stock );

                    /*PRIMEIRO PRECO RETALHO*/
                    setDadosPreco();
                    preco.setQtdBaixo( 1 );
                    preco.setQtdAlto( ( int ) DVML.QTD_DEFAULT - 1 );
                    //preco.setPrecoVenda( MetodosUtil.retirar_dizimas( Double.parseDouble(  txtPrecoVenda.getText()  )  )   );        
                    preco.setPrecoVenda( new BigDecimal( MetodosUtil.convertToDouble( txtPrecoCompra.getText() ) ) );
                    precoDao.create( preco );

                    /*SEGUNDO PRECO GROSSO*/
                    setDadosPreco();
                    preco.setQtdBaixo( ( int ) DVML.QTD_DEFAULT );
                    preco.setQtdAlto( 214748364 );
                    preco.setPrecoVenda( new BigDecimal( MetodosUtil.convertToDouble( txtPrecoCompra.getText() ) ) );
                    precoDao.create( preco );

                    JOptionPane.showMessageDialog( null, "Dados salvos com sucesso!", DVML.DVML_COMERCIAL, JOptionPane.INFORMATION_MESSAGE );
                    txtDesignacao.requestFocus();
                    detalhe_produto();
                    proximo_codigo();
                    procedimento_limpar();

                }
                catch ( Exception e )
                {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog( null, "Falha ao salvar os dados", DVML.DVML_COMERCIAL, JOptionPane.INFORMATION_MESSAGE );
                }

            }
            else
            {
                JOptionPane.showMessageDialog( null, "O " + designcao_produto + "  já se encontra no armazén " + cmbArmazem.getSelectedItem().toString(), DVML.DVML_COMERCIAL, JOptionPane.WARNING_MESSAGE );
            }

        }
        else
        {
            JOptionPane.showMessageDialog( null, "Atenção\nEste código de barra já consta na base de dados! ", "AVISO", JOptionPane.WARNING_MESSAGE );
        }

    }

    private void procedimento_alterar_servico()
    {

        this.produto = this.stock_publico.getCodProdutoCodigo();
        this.stock = this.stock_publico;

        //STOCK         
        //if( validado_stock() )
        if ( true )
        {
            try
            {
                set_dados_servicos();
                System.out.println( "PRODUTO " + this.produto.getDesignacao() );
                try
                {
                    produtoDao.edit( this.produto );
                    ivaAtualizar( produto.getCodigo() );
                    set_dados_stock_servico();
                    stockDao.edit( stock_publico );
                    System.out.println( "Houve Alteracao" );
                }
                catch ( Exception e )
                {
                    e.printStackTrace();
                }


                /*PRIMEIRO PRECO RETALHO*/
                setDadosPreco();
                preco.setQtdBaixo( 1 );
                preco.setQtdAlto( ( int ) DVML.QTD_DEFAULT - 1 );
                preco.setPrecoVenda( new BigDecimal( MetodosUtil.convertToDouble( txtPrecoCompra.getText() ) ) );
                precoDao.create( preco );

                /*SEGUNDO PRECO GROSSO*/
                setDadosPreco();
                preco.setQtdBaixo( ( int ) DVML.QTD_DEFAULT );
                preco.setQtdAlto( 214748364 );
                preco.setPrecoVenda( new BigDecimal( MetodosUtil.convertToDouble( txtPrecoCompra.getText() ) ) );
                precoDao.create( preco );

                JOptionPane.showMessageDialog( null, "Dados alterados com sucesso!", DVML.DVML_COMERCIAL, JOptionPane.INFORMATION_MESSAGE );

            }
            catch ( Exception e )
            {
                e.printStackTrace();
                JOptionPane.showMessageDialog( null, "Falha ao alterar os dados", DVML.DVML_COMERCIAL, JOptionPane.INFORMATION_MESSAGE );
            }
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

        }
        else
        {
            txtCodigoBarraProcura.requestFocus();

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
                txtPrecoVenda.setText( String.valueOf( MetodosUtil.preco_venda( percentagem, preco_compra ) ) );

            }
            else if ( evt.getKeyCode() == KeyEvent.VK_BACK_SPACE )
            {
                try
                {

                    prefixo = prefixo.toString().trim().substring( 0, prefixo.length() - 1 );
                    preco_compra = MetodosUtil.convertToDouble( txtPrecoCompra.getText() );
                    percentagem = Double.parseDouble( prefixo );
                    preco_venda = ( ( percentagem * preco_compra ) / 100 ) + preco_compra;
                    txtPrecoVenda.setText( String.valueOf( MetodosUtil.preco_venda( percentagem, preco_compra ) ) );

                }
                catch ( Exception e )
                {
                    txtPrecoVenda.setText( "" );
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
                prefixo = txtPrecoVenda.getText().trim() + key;
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
                    prefixo = txtPrecoVenda.getText().trim();
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

                preco_venda = MetodosUtil.convertToDouble( ( txtPrecoVenda.getText().equals( "" ) || txtPrecoVenda.getText() == null ) ? "0.0" : txtPrecoVenda.getText() );

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

        double preco_venda = MetodosUtil.convertToDouble( txtPrecoVenda.getText().replace( ",", "." ) );
        double preco_compra = MetodosUtil.convertToDouble( txtPrecoCompra.getText().replace( ",", "." ) );
        txtPercentagemGanhoRetalho.setText( CfMethods.formatarComoPorcoes( MetodosUtil.percentagemGanho( preco_compra, preco_venda ) ) );
    }

    private void tratar_preco_venda()
    {

        double percentagem_ganho = MetodosUtil.convertToDouble( txtPercentagemGanhoRetalho.getText().replace( ",", "." ) );
        double preco_compra = MetodosUtil.convertToDouble( txtPrecoCompra.getText().replace( ",", "." ) );
        System.out.println( "Preco Venda : " + MetodosUtil.precoVenda( preco_compra, percentagem_ganho ) );
        txtPrecoVenda.setText( CfMethods.formatarComoPorcoes( MetodosUtil.precoVenda( preco_compra, percentagem_ganho ) ) );
    }

    private void determinarPercetagemGanho()
    {
        txtPrecoVenda.getDocument().addDocumentListener( new DocumentListener()
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
            Retencao retencao = new RetencaoDao( emf ).findFirst();
            retencaoTaxaJTextField.setText( MetodosUtil.formatarComoPercentagem( retencao.getTaxa() ) );
        }
        else
        {
            retencaoZeroTaxaJTextField.setText( MetodosUtil.formatarComoPercentagem( 0.0 ) );

        }

        retencaoTaxaJTextField.setVisible( retencaoAplicarJRadioButton.isSelected() );
        retencaoTaxaJLabel.setVisible( retencaoTaxaJTextField.isVisible() );

        retencaoZeroTaxaJTextField.setVisible( !retencaoTaxaJTextField.isVisible() );
//        ivaMotivoJLabel.setVisible( ivaMotivoJComboBox.isVisible() );

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

}
