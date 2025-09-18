/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package visao;

import comercial.controller.ArmazensController;
import comercial.controller.DocumentosController;
import comercial.controller.ItemSaidasController;
import comercial.controller.MovimentacaoController;
import comercial.controller.PrecosController;
import comercial.controller.ProdutosController;
import comercial.controller.SaidasProdutosController;
import comercial.controller.StoksController;
import comercial.controller.TipoProdutosController;
import comercial.controller.UsuariosController;
import controller.ProdutoController;
import controller.StockController;
import dao.ArmazemDao;
import dao.BancoDao;
import dao.ClienteDao;
import dao.DescontoDao;
import dao.ItemProformaDao;
import dao.ItemSaidaDao;
import dao.PrecoDao;
import dao.ProFormaDao;
import dao.ProdutoDao;
import dao.StockDao;
import dao.UsuarioDao;
import dao.VasilhameDao;
import dao.SaidasProdutosDao;
import entity.TbArmazem;
import entity.TbItemSaidas;
import entity.TbPreco;
import entity.TbProduto;
import entity.TbStock;
import entity.TbVasilhame;
import entity.TbSaidasProdutos;
import entity.TbTipoProduto;
import entity.TbUsuario;
import exemplos.PermitirNumeros;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import lista.ListaSaidaProdutos;
import modelo.ProdutoModelo;
import modelo.TipoClienteModelo;
import util.BDConexao;
import util.DVML;
import util.JPAEntityMannagerFactoryUtil;
import util.MetodosUtil;
import util.OperacaoSistemaUtil;

/**
 *
 * @author Domingos Dala Vunge
 */
public class SaidaProdutoVisao extends javax.swing.JFrame
{

    private EntityManagerFactory emf = JPAEntityMannagerFactoryUtil.em;
    private VasilhameDao vasilhameDao = new VasilhameDao( emf );
    private ProFormaDao proFormaDao = new ProFormaDao( emf );
    private ItemProformaDao itemProformaDao = new ItemProformaDao( emf );
    private DescontoDao descontoDao = new DescontoDao( emf );
    private PrecoDao precoDao = new PrecoDao( emf );
    private SaidasProdutosController saidasProdutosController;
    private ItemSaidasController itemSaidasController;
    private TbItemSaidas itemSaidas;
    private TbStock stock_local;
    private TbSaidasProdutos saidasProdutos;
    private ProdutoDao produtoDao = new ProdutoDao( emf );
    private StockDao stockDao = new StockDao( emf );
    private UsuarioDao usuarioDao = new UsuarioDao( emf );
    private ClienteDao clienteDao = new ClienteDao( emf );
    private SaidasProdutosDao saidasProdutosDao = new SaidasProdutosDao( emf );
    private ArmazemDao armazemDao = new ArmazemDao( emf );
    private ItemSaidaDao itemSaidasProdutosDao = new ItemSaidaDao( emf );
    private TbVasilhame vasilhame;
    private BancoDao bancoDao = new BancoDao( emf );
    private static BDConexao conexao, conexaoTransaction;

    private static int cod_usuario;
    private TipoClienteModelo tipoClienteModelo;
    private StockController stockController;
    private int linha = 0, coordenada = 1;
    private double soma_total = 0;
    private ProdutoModelo produtoModelo;
    private static int linha_actual = -1;
    static SaidaProdutoVisao obj;
    private OperacaoSistemaUtil osu = new OperacaoSistemaUtil();
    private static ProdutosController produtosController;
    private static ArmazensController armazensController;
    private static TipoProdutosController tipoProdutoController;
    private static PrecosController precosController;
    private List<TbItemSaidas> listaItemSaida;
    private StoksController stoksController;
    private UsuariosController usuariosController;

    public SaidaProdutoVisao( int cod_usuario, BDConexao conexao ) throws SQLException
    {
        this.conexao = conexao;
        this.cod_usuario = cod_usuario;
        initComponents();
        confiLabel();
        setLocationRelativeTo( null );
        txtQuatindade.setDocument( new PermitirNumeros() );
        txtCodigoProduto.setDocument( new PermitirNumeros() );
        txtCodigoBarra.setDocument( new PermitirNumeros() );

        produtosController = new ProdutosController( SaidaProdutoVisao.conexao );
        tipoProdutoController = new TipoProdutosController( SaidaProdutoVisao.conexao );
        precosController = new PrecosController( SaidaProdutoVisao.conexao );
        saidasProdutosController = new SaidasProdutosController( conexao.getConnection1() );
        itemSaidasController = new ItemSaidasController( conexao.getConnection1() );
        stoksController = new StoksController( conexao );
        usuariosController = new UsuariosController( conexao );
        armazensController = new ArmazensController( conexao );

        cmbArmazem.setModel( new DefaultComboBoxModel( armazensController.getVector() ) );
        DVML.activar_cmb_armazem( cmbArmazem );
        cmbSubFamilia.setModel( new DefaultComboBoxModel( tipoProdutoController.getVector1() ) );
//        cmbSubFamilia.setModel( new DefaultComboBoxModel( conexao.getElementos( "tb_tipo_produto", "designacao", false ) ) );
//        cmbProduto.setModel( new DefaultComboBoxModel( conexao.getElementos2( "tb_produto", "designacao", "cod_Tipo_Produto", getCodigoTipoProduto() ) ) );
//                        Integer codTipoProduto = produto.getCodTipoProduto().getCodigo();
//                TbTipoProduto tipoProduto = (TbTipoProduto) tipoProdutoController.findById( codTipoProduto );
//                Integer codFamilia = tipoProduto.getFkFamilia().getPkFamilia();
//                cmbSubFamilia.setSelectedItem( tipoProduto.getDesignacao() );
//
        cmbProduto.setModel( new DefaultComboBoxModel( produtosController.getVectorStocavel() ) );
//        txtQuatindade.setText( "1" );
        txtQuatindade.requestFocus();

        MetodosUtil.setArmazemByCampoConfigArmazem( cmbArmazem, conexao, cod_usuario );
        ( (AbstractDocument) txtCodigoDoc.getDocument() ).setDocumentFilter( new UppercaseDocumentFilter() );

        btnNova.setEnabled( false );
        btnFinalizar.setEnabled( true );
        btnActualizar.setEnabled( false );
    }

    public void confiLabel()
    {

        lbCodigoProduto.setHorizontalAlignment( JLabel.RIGHT );
        lbCategoria.setHorizontalAlignment( JLabel.RIGHT );
        lbProduto.setHorizontalAlignment( JLabel.RIGHT );
        lbQuantidade.setHorizontalAlignment( JLabel.RIGHT );
        lbQuantidadeStock.setHorizontalAlignment( JLabel.RIGHT );

    }

    public int getCodigoTipoProduto() throws SQLException
    {
        return conexao.getCodigoPublico( "tb_tipo_produto", String.valueOf( cmbSubFamilia.getSelectedItem() ) );
    }

    public static int getCodigoProduto()
    {
        try
        {
            return produtosController.findByDesignacao( cmbProduto.getSelectedItem().toString() ).getCodigo();

        }
        catch ( Exception e )
        {
            return 0;
        }
    }

//    public boolean estado_critico() throws SQLException
//    {
//        TbStock stock = stockDao.getStockByDescricao( getCodigoProduto(), getCodigoArmazem() );
//        double qtd_minima = stock.getQuantBaixa(),
//                qtd_existente = stock.getQuantidadeExistente(),
//                qtd_critica = stock.getQuantCritica();
//        return qtd_minima < qtd_existente
//                && qtd_existente <= qtd_critica;
//
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

    public static boolean possivel_quantidade( int codigo, double qtd )
    {

        double quant_possivel = conexao.getQuantidade_Existente_Publico( codigo, getCodigoArmazem() )
                - conexao.getQuantidade_minima_publico( codigo, getCodigoArmazem() );

        return quant_possivel >= qtd;

    }

    public boolean isStocavel( String status )
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

    public double getPrecoProduto( int codProduto, boolean stocavel )
    {

        String sql = "";

        if ( stocavel )
        {
            return precoDao.getUltimoIdPrecoByIdProduto( getCodigoProduto() );
        }
        else
        {
            sql = "SELECT preco FROM tb_produto WHERE( codigo = " + codProduto + ")";
        }
        ResultSet rs = conexao.executeQuery( sql );

        try
        {
            if ( rs.next() )
            {
                if ( stocavel )
                {
                    return rs.getDouble( "preco_saidasProdutos" );
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

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings( "unchecked" )
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jButton4 = new javax.swing.JButton();
        txtCodigoProduto = new javax.swing.JTextField();
        lbCodigoProduto = new javax.swing.JLabel();
        lbCodigoProduto1 = new javax.swing.JLabel();
        txtCodigoBarra = new javax.swing.JTextField();
        lbPreco1 = new javax.swing.JLabel();
        cmbArmazem = new javax.swing.JComboBox();
        txtCodigoSaida = new javax.swing.JTextField();
        lbPreco2 = new javax.swing.JLabel();
        lbCodigoProduto3 = new javax.swing.JLabel();
        txtCodigoManual = new javax.swing.JTextField();
        lbQuantidade = new javax.swing.JLabel();
        txtQuatindade = new javax.swing.JTextField();
        jPanel8 = new javax.swing.JPanel();
        lbCategoria = new javax.swing.JLabel();
        cmbSubFamilia = new javax.swing.JComboBox();
        lbProduto = new javax.swing.JLabel();
        cmbProduto = new javax.swing.JComboBox();
        lbQuantidadeStock = new javax.swing.JLabel();
        txtQuantidaStock = new javax.swing.JTextField();
        btn_adicionar = new javax.swing.JButton();
        btn_remover = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        lbCodigoProduto2 = new javax.swing.JLabel();
        txtCodigoDoc = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        txtMotorista = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtAreaOBS = new javax.swing.JTextArea();
        jPanel3 = new javax.swing.JPanel();
        btnFinalizar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        btnActualizar = new javax.swing.JButton();
        btnNova = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("...:::::  KITANDA - SAÍDAS DE PRODUTOS ::::...");

        jPanel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jPanel1.setFont(new java.awt.Font("Showcard Gothic", 0, 24)); // NOI18N

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 12), new java.awt.Color(51, 153, 0))); // NOI18N
        jPanel4.setForeground(new java.awt.Color(102, 153, 0));

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/proucura.png"))); // NOI18N
        jButton4.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButton4ActionPerformed(evt);
            }
        });

        txtCodigoProduto.setBackground(new java.awt.Color(51, 153, 0));
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

        lbCodigoProduto.setFont(new java.awt.Font("Segoe UI Black", 0, 11)); // NOI18N
        lbCodigoProduto.setText("CodProd:");

        lbCodigoProduto1.setFont(new java.awt.Font("Segoe UI Black", 0, 11)); // NOI18N
        lbCodigoProduto1.setText("CodBarra:");

        txtCodigoBarra.setBackground(new java.awt.Color(51, 153, 0));
        txtCodigoBarra.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txtCodigoBarra.setForeground(new java.awt.Color(255, 255, 255));
        txtCodigoBarra.setCaretColor(new java.awt.Color(255, 255, 255));
        txtCodigoBarra.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtCodigoBarraActionPerformed(evt);
            }
        });

        lbPreco1.setFont(new java.awt.Font("Segoe UI Black", 0, 11)); // NOI18N
        lbPreco1.setText("Armzém");

        cmbArmazem.setBackground(new java.awt.Color(51, 153, 0));
        cmbArmazem.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 1, 14)); // NOI18N
        cmbArmazem.setEnabled(false);
        cmbArmazem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbArmazemActionPerformed(evt);
            }
        });

        txtCodigoSaida.setBackground(new java.awt.Color(51, 153, 0));
        txtCodigoSaida.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txtCodigoSaida.setForeground(new java.awt.Color(255, 255, 255));
        txtCodigoSaida.setCaretColor(new java.awt.Color(255, 255, 255));
        txtCodigoSaida.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtCodigoSaidaActionPerformed(evt);
            }
        });

        lbPreco2.setFont(new java.awt.Font("Segoe UI Black", 0, 11)); // NOI18N
        lbPreco2.setText("Cod. Saida");

        lbCodigoProduto3.setFont(new java.awt.Font("Segoe UI Black", 0, 11)); // NOI18N
        lbCodigoProduto3.setText("CodManual:");

        txtCodigoManual.setBackground(new java.awt.Color(51, 153, 0));
        txtCodigoManual.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txtCodigoManual.setForeground(new java.awt.Color(255, 255, 255));
        txtCodigoManual.setCaretColor(new java.awt.Color(255, 255, 255));
        txtCodigoManual.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtCodigoManualActionPerformed(evt);
            }
        });

        lbQuantidade.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 16)); // NOI18N
        lbQuantidade.setText("Qtd:");

        txtQuatindade.setBackground(new java.awt.Color(51, 153, 0));
        txtQuatindade.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        txtQuatindade.setForeground(new java.awt.Color(255, 255, 255));
        txtQuatindade.setCaretColor(new java.awt.Color(255, 255, 255));
        txtQuatindade.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtQuatindadeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtCodigoSaida, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbPreco2, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(67, 67, 67)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbPreco1, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbArmazem, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtQuatindade, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbQuantidade))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtCodigoProduto, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbCodigoProduto))
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(txtCodigoBarra, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lbCodigoProduto1))
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbCodigoProduto3)
                    .addComponent(txtCodigoManual, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                            .addComponent(lbPreco1, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(cmbArmazem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel7Layout.createSequentialGroup()
                            .addComponent(lbCodigoProduto3)
                            .addGap(6, 6, 6)
                            .addComponent(txtCodigoManual, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtCodigoSaida, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addComponent(lbPreco2, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(28, 28, 28)))
                        .addGroup(jPanel7Layout.createSequentialGroup()
                            .addComponent(lbCodigoProduto1)
                            .addGap(2, 2, 2)
                            .addComponent(txtCodigoBarra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtQuatindade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(lbCodigoProduto, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lbQuantidade))
                                .addGap(2, 2, 2)
                                .addComponent(txtCodigoProduto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(4, 4, 4)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        lbCategoria.setFont(new java.awt.Font("Segoe UI Black", 0, 11)); // NOI18N
        lbCategoria.setText("Categoria:");

        cmbSubFamilia.setBackground(new java.awt.Color(51, 153, 0));
        cmbSubFamilia.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 14)); // NOI18N
        cmbSubFamilia.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbSubFamiliaActionPerformed(evt);
            }
        });

        lbProduto.setFont(new java.awt.Font("Segoe UI Black", 0, 11)); // NOI18N
        lbProduto.setText("Produto:");

        cmbProduto.setBackground(new java.awt.Color(51, 153, 0));
        cmbProduto.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 14)); // NOI18N
        cmbProduto.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbProdutoActionPerformed(evt);
            }
        });

        lbQuantidadeStock.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        lbQuantidadeStock.setText("Q. Stock:");

        txtQuantidaStock.setEditable(false);
        txtQuantidaStock.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txtQuantidaStock.setForeground(new java.awt.Color(255, 255, 255));
        txtQuantidaStock.setCaretColor(new java.awt.Color(255, 255, 255));

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

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbCategoria)
                    .addComponent(cmbSubFamilia, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cmbProduto, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbProduto))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btn_adicionar, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btn_remover, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(75, 75, 75)
                        .addComponent(txtQuantidaStock, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbQuantidadeStock)
                        .addGap(17, 17, 17)))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(lbQuantidadeStock)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtQuantidaStock, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addComponent(lbProduto)
                                .addGap(2, 2, 2)
                                .addComponent(cmbProduto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addComponent(lbCategoria)
                                .addGap(2, 2, 2)
                                .addComponent(cmbSubFamilia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(btn_adicionar, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btn_remover, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Produtos à Saír"));

        table.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "Cod Prod", "Designacao", "Preço de Compra", "Qtd."
            }
        )
        {
            Class[] types = new Class []
            {
                java.lang.Integer.class, java.lang.String.class, java.lang.Object.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean []
            {
                false, false, false, false
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
            table.getColumnModel().getColumn(0).setMinWidth(25);
            table.getColumnModel().getColumn(1).setMinWidth(400);
            table.getColumnModel().getColumn(3).setMaxWidth(50);
        }

        lbCodigoProduto2.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 13)); // NOI18N
        lbCodigoProduto2.setText("Documento:");

        txtCodigoDoc.setBackground(new java.awt.Color(51, 153, 0));
        txtCodigoDoc.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txtCodigoDoc.setForeground(new java.awt.Color(255, 255, 255));
        txtCodigoDoc.setCaretColor(new java.awt.Color(255, 255, 255));
        txtCodigoDoc.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtCodigoDocActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        jLabel2.setText("Motivo :");

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        jLabel1.setText("Motorista :");

        txtMotorista.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtMotoristaActionPerformed(evt);
            }
        });

        txtAreaOBS.setColumns(20);
        txtAreaOBS.setRows(5);
        jScrollPane3.setViewportView(txtAreaOBS);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbCodigoProduto2, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCodigoDoc, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addComponent(jLabel2))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 323, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 46, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(txtMotorista, javax.swing.GroupLayout.PREFERRED_SIZE, 364, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lbCodigoProduto2)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtCodigoDoc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtMotorista, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 2, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 338, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        btnFinalizar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/6665_32x32.png"))); // NOI18N
        btnFinalizar.setText("Finalizar");
        btnFinalizar.setAlignmentX(0.5F);
        btnFinalizar.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnFinalizar.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnFinalizarActionPerformed(evt);
            }
        });

        btnCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/LOGOUT - VERMELHO/Logout 32x32.png"))); // NOI18N
        btnCancelar.setAlignmentX(0.5F);
        btnCancelar.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnCancelarActionPerformed(evt);
            }
        });

        btnActualizar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/actualizar_1_32x32_1.png"))); // NOI18N
        btnActualizar.setText("Actualizar");
        btnActualizar.setAlignmentX(0.5F);
        btnActualizar.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnActualizar.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnActualizarActionPerformed(evt);
            }
        });

        btnNova.setText("Nova");
        btnNova.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnNovaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnActualizar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 125, Short.MAX_VALUE)
                    .addComponent(btnFinalizar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnCancelar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnNova, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 14, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnNova, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(42, 42, 42)
                .addComponent(btnFinalizar)
                .addGap(20, 20, 20)
                .addComponent(btnActualizar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnCancelar)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        getAccessibleContext().setAccessibleName("...:::::  KITANDA - FACTURAÃO ::::...");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cmbSubFamiliaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSubFamiliaActionPerformed

        try
        {
            // TODO add your handling code here:

            System.err.println( "CODIGO TIPO PRODUTO: " + getCodigoTipoProduto() );
            cmbProduto.setModel( new DefaultComboBoxModel( conexao.getElementos2( "tb_produto", "designacao", "cod_Tipo_Produto", getCodigoTipoProduto() ) ) );

            if ( conexao.getCodigoByCodigo( "tb_stock", "quantidade_existente", "cod_produto_codigo", getCodigoProduto() ) <= 5 )
            {
                txtQuantidaStock.setBackground( Color.RED );
                txtQuantidaStock.setForeground( Color.WHITE );
            }
            else
            {
                txtQuantidaStock.setBackground( Color.WHITE );
            }

            txtQuantidaStock.setText( String.valueOf( conexao.getCodigoByCodigo( "tb_stock", "quantidade_existente", "cod_produto_codigo", getCodigoProduto() ) ) );
            TbProduto produto = produtoDao.findTbProduto( getCodigoProduto() );

            adicionar_preco_quantidade();

        }
        catch ( SQLException ex )
        {
            Logger.getLogger( SaidaProdutoVisao.class.getName() ).log( Level.SEVERE, null, ex );
        }
        catch ( Exception ex )
        {

        }

    }//GEN-LAST:event_cmbSubFamiliaActionPerformed

    private void cmbProdutoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbProdutoActionPerformed

        try
        {
            txtCodigoProduto.setText( "0" );
            adicionar_preco_quantidade();
        }
        catch ( Exception e )
        {
        }


    }//GEN-LAST:event_cmbProdutoActionPerformed


    private void btn_removerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_removerActionPerformed
        try
        {

            remover_item_carrinho();

        }
        catch ( Exception ex )
        {
            JOptionPane.showMessageDialog( null, "Possivelmente não selecionaste \n nenhuma linha ou não existe dados na table", "AVISO", JOptionPane.WARNING_MESSAGE );
        }


    }//GEN-LAST:event_btn_removerActionPerformed

    private void btn_adicionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_adicionarActionPerformed

        adicionar_botao();

    }//GEN-LAST:event_btn_adicionarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void txtCodigoProdutoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCodigoProdutoActionPerformed
        // TODO add your handling code here:
//        txtCodigoProduto.requestFocus();
        accao_codigo_interno_enter();
    }//GEN-LAST:event_txtCodigoProdutoActionPerformed

    private void cmbArmazemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbArmazemActionPerformed
        // TODO add your handling code here:
        adicionar_preco_quantidade();
    }//GEN-LAST:event_cmbArmazemActionPerformed

    private void txtCodigoBarraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCodigoBarraActionPerformed
        // TODO add your handling code here:
        accao_codigo_barra_enter();
    }//GEN-LAST:event_txtCodigoBarraActionPerformed

    private void txtQuatindadeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtQuatindadeActionPerformed
//        procedimento_adicionar();
        txtCodigoProduto.requestFocus();
    }//GEN-LAST:event_txtQuatindadeActionPerformed

    private void btnFinalizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFinalizarActionPerformed
        // TODO add your handling code here:
        procedimento_salvar();
    }//GEN-LAST:event_btnFinalizarActionPerformed

    private void txtMotoristaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMotoristaActionPerformed
        procedimento_salvar();
    }//GEN-LAST:event_txtMotoristaActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton4ActionPerformed
    {//GEN-HEADEREND:event_jButton4ActionPerformed

        try
        {

            System.out.println( "Codigo do Armazem em questão: " + getCodigoArmazem() );
            new BuscaProdutoVisao( this, rootPaneCheckingEnabled, getCodigoArmazem(), DVML.JANELA_SAIDA, conexao ).show();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void txtCodigoDocActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtCodigoDocActionPerformed
    {//GEN-HEADEREND:event_txtCodigoDocActionPerformed
        txtAreaOBS.requestFocus();
    }//GEN-LAST:event_txtCodigoDocActionPerformed

    private void txtCodigoSaidaActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtCodigoSaidaActionPerformed
    {//GEN-HEADEREND:event_txtCodigoSaidaActionPerformed
        try
        {
            // TODO add your handling code here
            buscaSaida();
        }
        catch ( SQLException ex )
        {
            JOptionPane.showMessageDialog( null, "Não existe saida com esta referência!" );
//            Logger.getLogger( SaidaProdutoVisao.class.getName() ).log( Level.SEVERE, null, ex );
        }


    }//GEN-LAST:event_txtCodigoSaidaActionPerformed

    private void txtCodigoManualActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtCodigoManualActionPerformed
    {//GEN-HEADEREND:event_txtCodigoManualActionPerformed
        // TODO add your handling code here:
        accao_codigo_manual_enter();
    }//GEN-LAST:event_txtCodigoManualActionPerformed

    private void btnActualizarActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnActualizarActionPerformed
    {//GEN-HEADEREND:event_btnActualizarActionPerformed

        // TODO add your handling code here:
        procedimentoActualizarSaida();


    }//GEN-LAST:event_btnActualizarActionPerformed

    private void tablePropertyChange(java.beans.PropertyChangeEvent evt)//GEN-FIRST:event_tablePropertyChange
    {//GEN-HEADEREND:event_tablePropertyChange
        // TODO add your handling code here:
        verificar();
    }//GEN-LAST:event_tablePropertyChange

    private void btnNovaActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnNovaActionPerformed
    {//GEN-HEADEREND:event_btnNovaActionPerformed
        // TODO add your handling code here:
        btnNova.setEnabled( true );
        btnFinalizar.setEnabled( true );
        btnActualizar.setEnabled( false );
    }//GEN-LAST:event_btnNovaActionPerformed

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
            Logger.getLogger( SaidaProdutoVisao.class.getName() ).log( Level.SEVERE, null, ex );
            JOptionPane.showMessageDialog( null, "Este produto não existe no armazém " + cmbArmazem.getSelectedItem(), DVML.DVML_COMERCIAL, JOptionPane.ERROR_MESSAGE );
        }

    }

    private static void procedimentoAdicionarTabela( TbProduto produto )
    {
        try
        {
            if ( txtQuatindade.getText().isEmpty() )
            {
                JOptionPane.showMessageDialog( null, "Não informou a quantidade, por favor informe a quantidade!" );
                txtQuatindade.requestFocus();
            }
            else if ( !Objects.isNull( produto ) )
            {
                Integer codTipoProduto = produto.getCodTipoProduto().getCodigo();
                TbTipoProduto tipoProduto = (TbTipoProduto) tipoProdutoController.findById( codTipoProduto );
                cmbSubFamilia.setSelectedItem( tipoProduto.getDesignacao() );
                cmbProduto.setModel( new DefaultComboBoxModel( produtosController.getVector() ) );
                cmbProduto.setSelectedItem( produto.getDesignacao() );

                procedimento_adicionar();
                txtCodigoProduto.setText( "" );
                txtCodigoBarra.setText( "" );
                txtQuatindade.requestFocus();

            }
            else
            {
                JOptionPane.showMessageDialog( null, "Não existe produto relacionado a esta referência" );
            }

        }
        catch ( Exception e )
        {
            e.printStackTrace();

        }

    }

    public static void procedimento_adicionar()
    {

        try
        {

            TbProduto produto = (TbProduto) produtosController.findById( getCodigoProduto() );
            if ( possivel_quantidade() )
            {
                if ( estado_critico() )
                {
                    JOptionPane.showMessageDialog( null, "O produto: " + produto.getDesignacao() + " precisa ser actualizado no stock", DVML.DVML_COMERCIAL, JOptionPane.WARNING_MESSAGE );
                }
                adicionar_produto();
            }
            else
            {
                JOptionPane.showMessageDialog( null, "O produto: " + produto.getDesignacao() + " não pode ser saír com esta quantidade", DVML.DVML_COMERCIAL, JOptionPane.ERROR_MESSAGE );
            }

        }
        catch ( SQLException ex )
        {

        }

    }

    public void adicionar_preco_quantidade()
    {

        try
        {
            TbProduto produto_local = (TbProduto) produtosController.findById( getCodigoProduto() );

//            TbStock stockLocal = stocksController.getStockByIdProdutoAndIdArmazem( getCodigoProduto(), getCodigoArmazem() );
//            boolean isStocavel = produto_local.getStocavel().equals( "true" );
            if ( Objects.nonNull( produto_local ) )
            {
                if ( stoksController.getStockByIdProdutoAndIdArmazem( getCodigoProduto(), getCodigoArmazem() ).getQuantidadeExistente()
                        <= stoksController.getStockByIdProdutoAndIdArmazem( getCodigoProduto(), getCodigoArmazem() ).getQuantCritica() )
                {

                    txtQuantidaStock.setBackground( Color.RED );
                    txtQuantidaStock.setForeground( Color.BLACK );
                }
                else
                {
                    txtQuantidaStock.setBackground( new Color( 51, 153, 0, 255 ) );
                }

                TbProduto produto = produtoDao.findTbProduto( getCodigoProduto() );

                if ( stockController.exist_armazem( getCodigoProduto(), getCodigoArmazem() ) )
                {
                    txtQuantidaStock.setText( String.valueOf( stockDao.get_stock_by_id_produto_and_id_armazem( getCodigoProduto(), getCodigoArmazem() ).getQuantidadeExistente() ) );

                }
                else
                {
                    txtQuantidaStock.setText( "0" );
                }
                txtCodigoProduto.setText( String.valueOf( produto_local.getCodigo() ) );
                TbPreco precoLocal = precosController.getLastIdPrecoByIdProduto( produto_local.getCodigo(), Double.parseDouble( txtQuatindade.getText() ) );
            }

        }
        catch ( Exception ex )
        {
            ex.printStackTrace();
            txtQuantidaStock.setText( "0" );
            Logger.getLogger( SaidaProdutoVisao.class.getName() ).log( Level.SEVERE, null, ex );
        }

    }

    public void adicionar_botao()
    {

        try
        {

            if ( !campos_invalidos() )
            {
                if ( isStocavel( new ProdutoController( conexao ).getProduto( getCodigoProduto() ).getStocavel() ) )
                {
                    if ( possivel_quantidade() )
                    {

                        if ( estado_critico() )
                        {
                            JOptionPane.showMessageDialog( null, "O produto: " + new ProdutoController( conexao ).getProduto( getCodigoProduto() ).getDesignacao() + " precisa de ser actualizado no stock", "DVML", JOptionPane.WARNING_MESSAGE );
                        }
                        adicionar_produto();

                    }
                    else
                    {
                        JOptionPane.showMessageDialog( null, "O produto: " + new ProdutoController( conexao ).getProduto( getCodigoProduto() ).getDesignacao() + " não pode sair pra esta quantidade", "DVML", JOptionPane.ERROR_MESSAGE );
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

    public void adicionar_botao_retificar()
    {

        try
        {

            if ( !campos_invalidos() )
            {
                if ( isStocavel( produtoDao.findTbProduto( getCodigoProduto() ).getStocavel() ) )
                {
                    if ( possivel_quantidade() )
                    {

                        if ( estado_critico() )
                        {
                            JOptionPane.showMessageDialog( null, "O produto: " + produtoDao.findTbProduto( getCodigoProduto() ).getDesignacao() + " precisa de ser actualizado no stock", DVML.DVML_COMERCIAL, JOptionPane.WARNING_MESSAGE );
                        }
                        adicionar_produto();

                    }
                    else
                    {
                        JOptionPane.showMessageDialog( null, "O produto: " + produtoDao.findTbProduto( getCodigoProduto() ).getDesignacao() + " não pode ser sair pra esta quantidade", DVML.DVML_COMERCIAL, JOptionPane.ERROR_MESSAGE );
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

    public void limpar()
    {

        txtQuatindade.setText( "" );
        txtCodigoProduto.setText( "" );
        txtAreaOBS.setText( "" );
        txtCodigoDoc.setText( "" );
        txtMotorista.setText( "" );
//          soma_total = 0;

    }

    private boolean transtorno()
    {

        int cod_produto = 0;
        double qtd = 0d, qtd_aceite = 0d;

        DefaultTableModel modelo = (DefaultTableModel) table.getModel();
        boolean transtorno = false;

        for ( int i = 0; i < table.getRowCount(); i++ )
        {

            cod_produto = Integer.parseInt( String.valueOf( table.getModel().getValueAt( i, 0 ) ) );
            qtd = Double.parseDouble( String.valueOf( table.getModel().getValueAt( i, 3 ) ) );

            if ( !possivel_quantidade( cod_produto, qtd ) )
            {

                transtorno = true;
                qtd_aceite = conexao.getQuantidade_Existente_Publico( cod_produto, getCodigoArmazem() );

                if ( qtd_aceite != 0 )
                {

                    int opcao = JOptionPane.showConfirmDialog( null, "Desculpe pelo transtorno, o produto " + produtoDao.findTbProduto( cod_produto ).getDesignacao() + " só é possivel  a " + qtd_aceite + " quantidade(s)" + ", contrariamente de " + qtd + "\n Deseja actualizar ou remover da table ?" );

                    if ( opcao == JOptionPane.YES_OPTION )
                    {

                        modelo.setValueAt( qtd_aceite, i, 3 );

                    }
                    else
                    {
                        modelo.removeRow( i );

                    }

                    adicionar_preco_quantidade_anitgo();

                }
                else
                {

                    modelo.removeRow( i );
                    adicionar_preco_quantidade_anitgo();
                    JOptionPane.showMessageDialog( null, "Desculpe pelo transtorno o produto " + produtoDao.findTbProduto( cod_produto ).getDesignacao() + " já não se encontra disponível no stock", DVML.DVML_COMERCIAL, JOptionPane.WARNING_MESSAGE );
                }
            }
        }
        return transtorno;

    }

    private boolean validar_campo()
    {

        if ( txtAreaOBS.getText().trim().equalsIgnoreCase( "" ) )
        {
            txtAreaOBS.requestFocus();
            txtAreaOBS.setBackground( Color.YELLOW );
            JOptionPane.showMessageDialog( null, "Pf. Digite o Motivo da Saida de Produtos!...", "AVISO", JOptionPane.WARNING_MESSAGE );
            txtAreaOBS.setBackground( Color.WHITE );
            return true;
        }
        else if ( txtMotorista.getText().trim().equalsIgnoreCase( "" ) )
        {
            txtMotorista.requestFocus();
            txtMotorista.setBackground( Color.YELLOW );
            JOptionPane.showMessageDialog( null, "Pf. Digite o nome do responsável da Saída!...", "AVISO", JOptionPane.WARNING_MESSAGE );
            txtMotorista.setBackground( Color.WHITE );
            return true;
        }
        else
        {
            txtAreaOBS.setBackground( Color.WHITE );
            txtMotorista.setBackground( Color.WHITE );
            return false;
        }

    }

    public void procedimento_salvar()
    {
        if ( !validar_campo() )
        {
            if ( !table_vazia() )
            {
                try
                {
                    if ( !transtorno() )
                    {
                        operacaoSalvar();

                    }
                }
                catch ( Exception e )
                {
                    e.printStackTrace();
                }
            }
            else
            {
                JOptionPane.showMessageDialog( null, "Impossivel dar saida porque não há produtos para sair", "ERRO", JOptionPane.ERROR_MESSAGE );
            }
        }

    }

    private void operacaoSalvar()
    {
        conexaoTransaction = new BDConexao();
        Connection conn = null;
        try
        {
            conn = conexaoTransaction.getConnection1();
            conn.setAutoCommit( false ); // Inicia a transação manualmente

            saidasProdutosController = new SaidasProdutosController( conn );
            itemSaidasController = new ItemSaidasController( conn );
            stoksController = new StoksController( conexaoTransaction );

            // Salvar a saída
            TbSaidasProdutos saidasProdutos = salvar_saidasProdutos();

            // Salvar os itens
            salvarItemsaidasProdutos( saidasProdutos );

            // Se tudo deu certo, confirma a transação
            conn.commit();

            JOptionPane.showMessageDialog( null, "Saída de Produtos efectuada com sucesso!.." );

            limpar();
            remover_all_produto();
            adicionar_preco_quantidade_anitgo();
            txtQuatindade.requestFocus();
            ListaSaidaProdutos listaSaidas = new ListaSaidaProdutos( saidasProdutos.getPkSaidasProdutos() );

        }
        catch ( Exception e )
        {
            e.printStackTrace();
            try
            {
                if ( conn != null )
                {
                    conn.rollback(); // desfaz alterações se houve erro
                    JOptionPane.showMessageDialog( null, "Erro ao salvar. Transação revertida!" );
                }
            }
            catch ( SQLException ex )
            {
                ex.printStackTrace();
            }
        }
        finally
        {
            try
            {
                if ( conn != null )
                {
                    conn.setAutoCommit( true ); // volta para o padrão
                }
                if ( conn != null )
                {
                    conn.close();
                }
                /**
                 * Instancia com um outra conexao para as proximas actividades
                 *
                 */
                saidasProdutosController = new SaidasProdutosController( conexao.getConnection1() );
                itemSaidasController = new ItemSaidasController( conexao.getConnection1() );
                stoksController = new StoksController( conexao );
            }
            catch ( SQLException e )
            {
                e.printStackTrace();
            }
        }
    }

    public void salvarItemsaidasProdutos( TbSaidasProdutos saidasProdutos ) throws SQLException
    {
        for ( int i = 0; i < table.getRowCount(); i++ )
        {
            TbItemSaidas itemSaidas = new TbItemSaidas();
            int codProduto = Integer.parseInt( String.valueOf( table.getModel().getValueAt( i, 0 ) ) );
            double qtd = Double.parseDouble( String.valueOf( table.getModel().getValueAt( i, 3 ) ) );

            itemSaidas.setFkProdutos( new TbProduto( codProduto ) );
            itemSaidas.setFkSaidasProdutos( saidasProdutos );
            itemSaidas.setQuantidade( qtd );

            BigDecimal precoCompra = getPreco( itemSaidas.getFkProdutos().getCodigo(), itemSaidas.getQuantidade() );
            itemSaidas.setPrecoCompra( precoCompra );

            itemSaidasController.salvar( itemSaidas );

            // atualizar stock
            this.stock_local = stoksController.getStockByIdProdutoAndIdArmazem(
                    itemSaidas.getFkProdutos().getCodigo(), getCodigoArmazem() );

            if ( Objects.nonNull( stock_local ) )
            {
                if ( MovimentacaoController.registrarMovimento(
                        itemSaidas.getFkProdutos().getCodigo(),
                        getCodigoArmazem(),
                        cod_usuario,
                        new BigDecimal( itemSaidas.getQuantidade() ),
                        "SAIDA " + saidasProdutos.getPkSaidasProdutos(),
                        "SAIDA",
                        conexaoTransaction ) )
                {

                    actualizar_quantidade(
                            itemSaidas.getFkProdutos().getCodigo(),
                            itemSaidas.getQuantidade(),
                            getCodigoArmazem() );
                }
            }
        }
    }

    private boolean table_vazia()
    {
        DefaultTableModel modelo = (DefaultTableModel) table.getModel();
        return modelo.getRowCount() == 0;

    }


    /* CRIACAO DO GETS  */
    public static int getQuantidade()
    {
        System.err.println( "QUANTIDADE DIGTADA:" + txtQuatindade.getText().trim() );
        return Integer.parseInt( txtQuatindade.getText().trim() );
    }

    public static String getDescricao_Produto() throws SQLException
    {

        return BDConexao.getDescrisoByCodigo( "tb_produto", "designacao", getCodigoProduto() );

    }

    public boolean campos_invalidos()
    {

        return txtQuatindade.getText().equals( "" );

    }

    private void buscaSaida() throws SQLException
    {
        saidasProdutosController = new SaidasProdutosController( conexao.getConnection1() );
        itemSaidasController = new ItemSaidasController( conexao.getConnection1() );

        int codSaida = Integer.parseInt( txtCodigoSaida.getText() );
        listaItemSaida = itemSaidasController.getAllItemSaidasByIdSaida( codSaida );
        DefaultTableModel modelo = (DefaultTableModel) table.getModel();
        modelo.setRowCount( 0 );
        txtMotorista.setText( "" );
        txtAreaOBS.setText( "" );
        txtCodigoDoc.setText( "" );

        if ( Objects.nonNull( listaItemSaida ) )
        {
            saidasProdutos = saidasProdutosController.buscarPorId( codSaida );

            for ( TbItemSaidas linha : listaItemSaida )
            {
                TbProduto produto = produtosController.findByCod( linha.getFkProdutos().getCodigo() );
                modelo.addRow( new Object[]
                {
                    linha.getFkProdutos().getCodigo(),
                    produto.getDesignacao(),
                    linha.getPrecoCompra(),
                    linha.getQuantidade()
                } );
            }

            txtMotorista.setText( saidasProdutos.getNomeFuncionario() );
            txtAreaOBS.setText( saidasProdutos.getObs() );
            txtCodigoDoc.setText( saidasProdutos.getDocumento() );
            btnNova.setEnabled( true );
            btnFinalizar.setEnabled( false );
            btnActualizar.setEnabled( true );

            configurarColunaDouble( table, 3, true );
        }
        else
        {

            JOptionPane.showMessageDialog( null,
                    "Não existe saida com esta referência.",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE );
        }

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

    public static boolean validar_zero()
    {

        return Integer.parseInt( txtQuatindade.getText() ) == 0;

    }

    public static void adicionar_produto() throws SQLException
    {

        DefaultTableModel modelo = (DefaultTableModel) table.getModel();

        if ( !exist_produto( getCodigoProduto() ) )
        {
            if ( !validar_zero() )
            {
                modelo.addRow( new Object[]
                {
                    getCodigoProduto(),
                    getDescricao_Produto(),
                    getPreco(),
                    getQuantidade()

                } );
            }
            else
            {
                JOptionPane.showMessageDialog( null, "Atenção\nA quantidade a saír não pode ser igual a zero!" );
            }
            txtQuatindade.setText( "" );
            txtCodigoProduto.requestFocus();
        }
        else
        {
            actuazlizar_quantidade( txtQuatindade.getText() );
        }

        txtQuatindade.requestFocus();

    }

    public void remover_item_carrinho()
    {

        table.getColumnModel().getColumn( 0 );
        table.getColumnModel().getColumn( 1 );
        table.getColumnModel().getColumn( 2 );
        txtQuatindade.setText( "" );

        DefaultTableModel modelo = (DefaultTableModel) table.getModel();

        modelo.removeRow( table.getSelectedRow() );
    }

    public void adicionar_produto_teclado( int codigo, String descricao ) throws SQLException
    {

        table.getColumnModel().getColumn( 0 );
        table.getColumnModel().getColumn( 1 );
        table.getColumnModel().getColumn( 2 );
        table.getColumnModel().getColumn( 3 );
        table.getColumnModel().getColumn( 4 );
        table.getColumnModel().getColumn( 5 );

        DefaultTableModel modelo = (DefaultTableModel) table.getModel();

        modelo.addRow( new Object[]
        {
            codigo,
            descricao,
            getQuantidade()
        } );
    }

    public void salvarItemsaidasProdutos() throws SQLException
    {
        boolean efectuada = true;
        this.saidasProdutos = saidasProdutosController.buscarUltimaSaida();
        for ( int i = 0; i < table.getRowCount(); i++ )
        {
            try
            {
                itemSaidas = new TbItemSaidas();
                int codProduto = Integer.parseInt( String.valueOf( table.getModel().getValueAt( i, 0 ) ) );
                double qtd = Double.parseDouble( String.valueOf( table.getModel().getValueAt( i, 3 ) ) );
                itemSaidas.setFkProdutos( new TbProduto( codProduto ) );
                BigDecimal precoCompra = getPreco( itemSaidas.getFkProdutos().getCodigo(), itemSaidas.getQuantidade() );

                itemSaidas.setFkSaidasProdutos( this.saidasProdutos );
                itemSaidas.setQuantidade( qtd );
                itemSaidas.setPrecoCompra( precoCompra );
                itemSaidasController.salvar( itemSaidas );
                this.stock_local = stoksController.getStockByIdProdutoAndIdArmazem(
                        itemSaidas.getFkProdutos().getCodigo(), getCodigoArmazem() );
                //so retirar caso existir mesmo no armazém em questão.
                if ( Objects.nonNull( stock_local ) )
                {
                    if ( MovimentacaoController.registrarMovimento(
                            itemSaidas.getFkProdutos().getCodigo(),
                            getCodigoArmazem(),
                            cod_usuario,
                            new BigDecimal( itemSaidas.getQuantidade() ),
                            "SAIDA " + saidasProdutos.getPkSaidasProdutos(),
                            "SAIDA",
                            conexao ) )
                    {
                        actualizar_quantidade(
                                itemSaidas.getFkProdutos().getCodigo(),
                                itemSaidas.getQuantidade(),
                                getCodigoArmazem() );
                    }
//                    if ( vasilhameDao.exist_vasilhame( itemSaidas.getFkProdutos().getCodigo(), getCodigoArmazem() ) )
//                    {
//                        actualizar_vasilhame( vasilhameDao.getVasilhameByIdProdutoAndIdArmazem( itemSaidas.getFkProdutos().getCodigo(), getCodigoArmazem() ), itemSaidas.getQuantidade() );
//                    }

                }
            }
            catch ( Exception e )
            {
                e.printStackTrace();
                efectuada = false;
                JOptionPane.showMessageDialog( null, "Falha ao registrar o produto: " + itemSaidas.getFkProdutos().getCodigo() );
                break;
            }
        }

        if ( efectuada )
        {
            JOptionPane.showMessageDialog( null, "Saída de Produtos efectuada com sucesso!.." );

            try
            {
                limpar();
                remover_all_produto();
                adicionar_preco_quantidade_anitgo();

            }
            catch ( Exception e )
            {
            }
            txtQuatindade.requestFocus();
            ListaSaidaProdutos listaSaidas = new ListaSaidaProdutos( saidasProdutos.getPkSaidasProdutos() );

        }

    }

    public void actluazarItemsaidasProdutos( TbSaidasProdutos saidasProdutos ) throws SQLException
    {
        boolean efectuada = true;

        // remove todos os itens antigos antes de inserir os novos
        itemSaidasController.deleteAllItemSaidasByIdSaidaReciclagem( saidasProdutos.getPkSaidasProdutos() );

        for ( int i = 0; i < table.getRowCount(); i++ )
        {
            try
            {
                itemSaidas = new TbItemSaidas();
                itemSaidas.setFkProdutos(
                        produtoDao.findTbProduto( Integer.parseInt( String.valueOf( table.getModel().getValueAt( i, 0 ) ) ) )
                );
                itemSaidas.setFkSaidasProdutos( saidasProdutos );
                itemSaidas.setQuantidade( Double.parseDouble( String.valueOf( table.getModel().getValueAt( i, 3 ) ) ) );
                itemSaidas.setPrecoCompra(
                        getPreco( itemSaidas.getFkProdutos().getCodigo(), itemSaidas.getQuantidade() )
                );

                itemSaidasController.salvar( itemSaidas );

                this.stock_local = stoksController.getStockByIdProdutoAndIdArmazem(
                        itemSaidas.getFkProdutos().getCodigo(), getCodigoArmazem()
                );

                if ( Objects.nonNull( stock_local ) )
                {
                    String tipo = getTipo( itemSaidas.getFkProdutos().getCodigo(), itemSaidas.getQuantidade() );
                    double qtdAcertar = getQtdAcertar( itemSaidas.getFkProdutos().getCodigo(), itemSaidas.getQuantidade() );

                    if ( MovimentacaoController.registrarMovimento(
                            itemSaidas.getFkProdutos().getCodigo(),
                            getCodigoArmazem(),
                            cod_usuario,
                            new BigDecimal( qtdAcertar ),
                            "ACTUALIZAÇÃO SAIDA " + saidasProdutos.getPkSaidasProdutos(),
                            tipo,
                            conexaoTransaction ) )
                    {

                        if ( tipo.equals( "ENTRADA" ) )
                        {
                            stoksController.adicionar_quantidades(
                                    itemSaidas.getFkProdutos().getCodigo(),
                                    qtdAcertar,
                                    getCodigoArmazem()
                            );
                        }
                        else
                        {
                            stoksController.subtrair_quantidades(
                                    itemSaidas.getFkProdutos().getCodigo(),
                                    qtdAcertar,
                                    getCodigoArmazem()
                            );
                        }
                    }
                }
            }
            catch ( Exception e )
            {
                e.printStackTrace();
                efectuada = false;
                JOptionPane.showMessageDialog( null,
                        "Falha ao actualizar o produto: " + itemSaidas.getFkProdutos().getCodigo() );
                break;
            }
        }

        if ( !efectuada )
        {
            throw new SQLException( "Erro ao actualizar itens da saída, transação será revertida." );
        }
    }

    private boolean actualizar_vasilhame( TbVasilhame vasilhame, double qtd )
    {
        double qtd_actualizada = vasilhame.getQtdExistente() + qtd;
        vasilhame.setQtdExistente( qtd_actualizada );
        try
        {
            vasilhameDao.edit( vasilhame );
            return true;
        }
        catch ( Exception e )
        {
            return false;
        }

    }

    public void remover_items()
    {

        table.getColumnModel().getColumn( 0 );
        table.getColumnModel().getColumn( 1 );
        table.getColumnModel().getColumn( 2 );

        DefaultTableModel modelo = (DefaultTableModel) table.getModel();

        for ( int i = modelo.getRowCount() - 1; i >= 0; i-- )
        {
            modelo.removeRow( i );
        }

    }

    public void remover_all_produto()
    {

        table.getColumnModel().getColumn( 0 );
        table.getColumnModel().getColumn( 1 );
        table.getColumnModel().getColumn( 2 );

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
            linha--;
            coordenada--;

        }
        else
        {
            JOptionPane.showMessageDialog( null, "Impossivel Remover Produtos na Tabela!...." );
        }

    }

    public void actualizar_quantidade( int cod, int quantidade )
    {

        String sql = "UPDATE tb_stock SET quantidade_existente =  " + ( getQuantidadeProduto( cod ) - quantidade ) + " WHERE cod_produto_codigo = " + cod + " AND  cod_armazem = " + getCodigoArmazem();

        System.out.println( "Quantidade   : " + quantidade );

        conexao.executeUpdate( sql );

    }

    public int getQuantidadeProduto( int cod_produto )
    {

        String sql = "SELECT quantidade_existente FROM  tb_stock WHERE  cod_produto_codigo = " + cod_produto + " AND cod_armazem = " + getCodigoArmazem();

        ResultSet rs = conexao.executeQuery( sql );

        try
        {
            if ( rs.next() )
            {
                return rs.getInt( "quantidade_existente" );
            }
        }
        catch ( SQLException ex )
        {
            ex.printStackTrace();
            return 0;
        }

        return 0;
    }

    public int getQuantidadeProduto( int cod_produto, BDConexao conexaoParm )
    {

        String sql = "SELECT quantidade_existente FROM  tb_stock WHERE  cod_produto_codigo = "
                + cod_produto + " AND cod_armazem = " + getCodigoArmazem( conexaoParm );

        ResultSet rs = conexaoParm.executeQuery( sql );

        try
        {
            if ( rs.next() )
            {
                return rs.getInt( "quantidade_existente" );
            }
        }
        catch ( SQLException ex )
        {
            ex.printStackTrace();
            return 0;
        }

        return 0;
    }

    public int getLastCodigo( String table )
    {

        String sql = "SELECT max(codigo) FROM " + table;

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

    private static int getCodigoArmazem()
    {
        return conexao.getCodigoPublico( "tb_armazem", String.valueOf( cmbArmazem.getSelectedItem() ) );
    }

    private static int getCodigoArmazem( BDConexao conexaoParm )
    {
        return conexaoParm.getCodigoPublico( "tb_armazem", String.valueOf( cmbArmazem.getSelectedItem() ) );
    }

    public TbSaidasProdutos salvar_saidasProdutos() throws SQLException
    {
        TbSaidasProdutos saidasProdutos = new TbSaidasProdutos();
        saidasProdutos.setDataSaida( new Date() );
        saidasProdutos.setFkUsuario( new TbUsuario( cod_usuario ) );
        saidasProdutos.setIdArmazemFK( new TbArmazem( getCodigoArmazem() ) );
        saidasProdutos.setHoraSaida( new Date() );
        saidasProdutos.setObs( txtAreaOBS.getText() );
        saidasProdutos.setDocumento( txtCodigoDoc.getText() );
        saidasProdutos.setNomeFuncionario( txtMotorista.getText() );
        saidasProdutos.setStatusEliminado( "false" );

        saidasProdutosController.inserir( saidasProdutos );

        return saidasProdutosController.buscarUltimaSaida();
    }

    public String isPefromance( boolean performance )
    {
        if ( performance )
        {
            return "true";
        }
        return "false";
    }

    public String isCredito( boolean credito )
    {
        if ( credito )
        {
            return "true";
        }
        return "false";
    }

    public static void main( String[] args ) throws SQLException
    {

        new SaidaProdutoVisao( 15, new BDConexao() ).show( true );

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static javax.swing.JButton btnActualizar;
    private javax.swing.JButton btnCancelar;
    public static javax.swing.JButton btnFinalizar;
    private javax.swing.JButton btnNova;
    public static javax.swing.JButton btn_adicionar;
    public static javax.swing.JButton btn_remover;
    private javax.swing.ButtonGroup buttonGroup1;
    private static javax.swing.JComboBox cmbArmazem;
    public static javax.swing.JComboBox cmbProduto;
    public static javax.swing.JComboBox cmbSubFamilia;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lbCategoria;
    private javax.swing.JLabel lbCodigoProduto;
    private javax.swing.JLabel lbCodigoProduto1;
    private javax.swing.JLabel lbCodigoProduto2;
    private javax.swing.JLabel lbCodigoProduto3;
    private javax.swing.JLabel lbPreco1;
    private javax.swing.JLabel lbPreco2;
    private javax.swing.JLabel lbProduto;
    private javax.swing.JLabel lbQuantidade;
    private javax.swing.JLabel lbQuantidadeStock;
    private static javax.swing.JTable table;
    private javax.swing.JTextArea txtAreaOBS;
    public static javax.swing.JTextField txtCodigoBarra;
    public static javax.swing.JTextField txtCodigoDoc;
    public static javax.swing.JTextField txtCodigoManual;
    public static javax.swing.JTextField txtCodigoProduto;
    public static javax.swing.JTextField txtCodigoSaida;
    private javax.swing.JTextField txtMotorista;
    private javax.swing.JTextField txtQuantidaStock;
    public static javax.swing.JTextField txtQuatindade;
    // End of variables declaration//GEN-END:variables

    private static boolean exist_produto( int codigo )
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

    private static void actuazlizar_quantidade( String quantidade )
    {
        DefaultTableModel modelo = (DefaultTableModel) table.getModel();
        modelo.setValueAt( quantidade, linha_actual, 3 );
        linha_actual = -1;

    }

    private void accao_codigo_interno_enter()
    {

        try
        {

            int codigo = Integer.parseInt( txtCodigoProduto.getText() );
            TbProduto produto = produtoDao.findTbProduto( codigo );

            //Devo setar a combo dos produtos (Isto porque quando a se faz a busca na cmbCategoria remove todos os produtos exceptos os de categoria actual)
            cmbProduto.setModel( new DefaultComboBoxModel( produtoDao.getAllDesingnacaoProduto() ) );
            cmbProduto.setSelectedItem( produto.getDesignacao() );
            //adicionar_preco_quantidade();   
            adicionar_preco_quantidade_anitgo();
            procedimento_adicionar1();
            txtCodigoProduto.setText( "" );
            txtQuatindade.setText( "" );
            txtQuatindade.requestFocus();

        }
        catch ( Exception ex )
        {
            ex.printStackTrace();
            Logger.getLogger( VendaUsuarioVisao.class.getName() ).log( Level.SEVERE, null, ex );
            JOptionPane.showMessageDialog( null, "Este produto não existe no armazém " + cmbArmazem.getSelectedItem(), DVML.DVML_COMERCIAL, JOptionPane.ERROR_MESSAGE );
        }

    }

    public void adicionar_preco_quantidade_anitgo()
    {

        try
        {

            if ( conexao.getQtdExistenteStock( getCodigoProduto(), getCodigoArmazem() ) <= conexao.getQtdCriticaStock( getCodigoProduto(), getCodigoArmazem() ) )
            {
                txtQuantidaStock.setBackground( Color.RED );
                txtQuantidaStock.setForeground( Color.BLACK );
            }

            else
            {
                txtQuantidaStock.setBackground( new Color( 51, 153, 0, 255 ) );
            }

            txtQuantidaStock.setText( String.valueOf( conexao.getQtdExistenteStock( getCodigoProduto(), getCodigoArmazem() ) ) );

        }
        catch ( Exception ex )
        {
            Logger.getLogger( SaidaProdutoVisao.class.getName() ).log( Level.SEVERE, null, ex );
        }

    }

    public void procedimento_adicionar1()
    {

        try
        {

            if ( !campos_invalidos() )
            {

                if ( isStocavel( produtoDao.findTbProduto( getCodigoProduto() ).getStocavel() ) )
                {
                    if ( possivel_quantidade() )
                    {

                        if ( estado_critico() )
                        {
                            JOptionPane.showMessageDialog( null, "O produto: " + produtoDao.findTbProduto( getCodigoProduto() ).getDesignacao() + " precisa de ser actualizado no stock", DVML.DVML_COMERCIAL, JOptionPane.WARNING_MESSAGE );
                        }
                        adicionar_produto();

                    }
                    else
                    {
                        JOptionPane.showMessageDialog( null, "O produto: " + produtoDao.findTbProduto( getCodigoProduto() ).getDesignacao() + " não pode sair pra esta quantidade", DVML.DVML_COMERCIAL, JOptionPane.ERROR_MESSAGE );
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

    private void accao_codigo_barra_enter()
    {

        String codigo_barra = txtCodigoBarra.getText().trim();

        TbProduto produto = produtoDao.getProdutoByCodigoBarra( codigo_barra );
        cmbProduto.setModel( new DefaultComboBoxModel( produtoDao.getAllDesingnacaoProduto() ) );
        cmbProduto.setSelectedItem( produto.getDesignacao() );
        adicionar_preco_quantidade_anitgo();
        procedimento_adicionar1();
        txtCodigoBarra.setText( "" );
        txtQuatindade.setText( "1" );
        txtQuatindade.requestFocus();

    }

    private void accao_codigo_manual_enter()
    {

        String codigo_barra = txtCodigoManual.getText().trim();

        TbProduto produto = produtoDao.getProdutoByCodigoManual( codigo_barra );
        cmbProduto.setModel( new DefaultComboBoxModel( produtoDao.getAllDesingnacaoProduto() ) );
        cmbProduto.setSelectedItem( produto.getDesignacao() );
        adicionar_preco_quantidade_anitgo();
        procedimento_adicionar1();
        txtCodigoManual.setText( "" );
        txtQuatindade.setText( "1" );
        txtQuatindade.requestFocus();

    }

    private static BigDecimal getPreco()
    {
        try
        {
            return precosController.getLastIdPrecoByIdProduto( getCodigoProduto(), getQuantidade() ).getPrecoCompra();
        }
        catch ( Exception e )
        {
            return new BigDecimal( 0d );
        }
    }

    private static BigDecimal getPreco( int codigo, double qtd )
    {
        try
        {
            return precosController.getLastIdPrecoByIdProduto( codigo, qtd ).getPrecoCompra();
        }
        catch ( Exception e )
        {
            return new BigDecimal( 0d );
        }

    }

    public static SaidaProdutoVisao getObj( int cod_usuario )
    {
        if ( obj == null )
        {
            try
            {
                obj = new SaidaProdutoVisao( cod_usuario, conexao );
            }
            catch ( Exception e )
            {
            }
        }

        return obj;

    }

    public void actualizar_quantidade( int cod, double quantidade, int idArmazem, BDConexao conexaoParm )
    {

        String sql = "UPDATE tb_stock SET quantidade_existente =  "
                + ( getQuantidadeProduto( cod ) - quantidade )
                + " WHERE cod_produto_codigo = " + cod + " AND cod_armazem = " + idArmazem;

        conexaoParm.executeUpdate( sql );

    }

    public void actualizar_quantidade( int cod, double quantidade, int idArmazem )
    {

        String sql = "UPDATE tb_stock SET quantidade_existente =  " + ( getQuantidadeProduto( cod ) - quantidade ) + " WHERE cod_produto_codigo = " + cod + " AND cod_armazem = " + idArmazem;

        conexao.executeUpdate( sql );

    }

    public void actualizar_quantidade_mais( int cod, double quantidade, int idArmazem )
    {

        String sql = "UPDATE tb_stock SET quantidade_existente =  " + ( getQuantidadeProduto( cod ) + quantidade ) + " WHERE cod_produto_codigo = " + cod + " AND cod_armazem = " + idArmazem;

        conexao.executeUpdate( sql );

    }

    private void procedimentoActualizarSaida()
    {
        conexaoTransaction = new BDConexao();
        Connection conn = null;

        try
        {
            conn = conexaoTransaction.getConnection1();
            conn.setAutoCommit( false ); // inicia transação

            saidasProdutosController = new SaidasProdutosController( conn );
            itemSaidasController = new ItemSaidasController( conn );
            stoksController = new StoksController( conexaoTransaction );

            // Atualiza os dados principais da saída
            TbSaidasProdutos saidasProdutos = atualizar_saidasProdutos();

            // Atualiza os itens vinculados
            actluazarItemsaidasProdutos( saidasProdutos );

            // Confirma transação se tudo correu bem
            conn.commit();

            JOptionPane.showMessageDialog( null, "Saída de Produtos actualizada com sucesso!.." );

            limpar();
            remover_all_produto();
            txtQuatindade.requestFocus();
            new ListaSaidaProdutos( saidasProdutos.getPkSaidasProdutos() );

        }
        catch ( Exception e )
        {
            e.printStackTrace();
            try
            {
                if ( conn != null )
                {
                    conn.rollback(); // desfaz alterações
                    JOptionPane.showMessageDialog( null, "Erro ao actualizar. Transação revertida!" );
                }
            }
            catch ( SQLException ex )
            {
                ex.printStackTrace();
            }
        }
        finally
        {
            try
            {
                if ( conn != null )
                {
                    conn.setAutoCommit( true );
                }
                if ( conn != null )
                {
                    conn.close();
                }

                /**
                 * Instancia com um outra conexao para as proximas actividades
                 *
                 */
                saidasProdutosController = new SaidasProdutosController( conexao.getConnection1() );
                itemSaidasController = new ItemSaidasController( conexao.getConnection1() );
                stoksController = new StoksController( conexao );
            }
            catch ( SQLException e )
            {
                e.printStackTrace();
            }
        }
    }

    public TbSaidasProdutos atualizar_saidasProdutos() throws SQLException
    {
        TbSaidasProdutos saidasProdutos = listaItemSaida.get( 0 ).getFkSaidasProdutos();
        saidasProdutos.setDataSaida( new Date() );
        saidasProdutos.setFkUsuario( new TbUsuario( cod_usuario ) );
        saidasProdutos.setIdArmazemFK( new TbArmazem( getCodigoArmazem() ) );
        saidasProdutos.setHoraSaida( new Date() );
        saidasProdutos.setObs( txtAreaOBS.getText() );
        saidasProdutos.setDocumento( txtCodigoDoc.getText() );
        saidasProdutos.setNomeFuncionario( txtMotorista.getText() );
        saidasProdutos.setStatusEliminado( "false" );

        saidasProdutosController.atualizar( saidasProdutos );

        return saidasProdutos;
    }

    private String getTipo( Integer codigo, double quantidade )
    {

        for ( TbItemSaidas linha : listaItemSaida )
        {
            if ( linha.getFkProdutos().getCodigo() == codigo )
            {
                if ( linha.getQuantidade() < quantidade )
                {
                    return "SAIDA";
                }
                else
                {
                    return "ENTRADA";
                }
            }
        }
        return "SAIDA";
    }

    private double getQtdAcertar( Integer codigo, double quantidade )
    {
        for ( TbItemSaidas linha : listaItemSaida )
        {
            if ( linha.getFkProdutos().getCodigo() == codigo )
            {
                if ( linha.getQuantidade() >= quantidade )
                {
                    return linha.getQuantidade() - quantidade;
                }
                else
                {
                    return quantidade - linha.getQuantidade();
                }
            }
        }
        return quantidade;
    }

    private double getQtdAnterior( Integer codigo )
    {
        System.out.println( "SIZE$$$$$: " + listaItemSaida.size() );
        for ( TbItemSaidas linha : listaItemSaida )
        {
            if ( linha.getFkProdutos().getCodigo() == codigo )
            {
                return linha.getQuantidade();
            }
        }
        return 0;
    }

    public class UppercaseDocumentFilter extends DocumentFilter
    {

        @Override
        public void insertString( FilterBypass fb, int offset, String text, AttributeSet attr ) throws BadLocationException
        {
            if ( text != null )
            {
                super.insertString( fb, offset, text.toUpperCase(), attr );
            }
        }

        @Override
        public void replace( FilterBypass fb, int offset, int length, String text, AttributeSet attrs ) throws BadLocationException
        {
            if ( text != null )
            {
                super.replace( fb, offset, length, text.toUpperCase(), attrs );
            }
        }
    }

    // Configura a tabela: define se uma coluna é editável ou não, e valida double
    public void configurarColunaDouble( JTable tabela, int colunaIndex, boolean editavel )
    {
        DefaultTableModel modelo = (DefaultTableModel) tabela.getModel();

        DefaultTableModel modeloCustom = new DefaultTableModel(
                modelo.getDataVector(),
                getColumnNames( modelo )
        )
        {
            @Override
            public boolean isCellEditable( int row, int column )
            {
                // Só será editável se o parâmetro permitir e for a coluna escolhida
                return editavel && column == colunaIndex;
            }
        };

        tabela.setModel( modeloCustom );

        if ( editavel )
        {
            configurarEditorDouble( tabela, colunaIndex );
        }
    }

// Retorna os nomes das colunas do modelo
    private Vector<String> getColumnNames( DefaultTableModel modelo )
    {
        Vector<String> colunas = new Vector<>();
        for ( int i = 0; i < modelo.getColumnCount(); i++ )
        {
            colunas.add( modelo.getColumnName( i ) );
        }
        return colunas;
    }

// Configura o editor da coluna para aceitar apenas double
    private void configurarEditorDouble( JTable tabela, int colunaIndex )
    {

        tabela.getColumnModel().getColumn( colunaIndex ).setCellEditor(
                new DefaultCellEditor( new JTextField() )
        {
            @Override
            public boolean stopCellEditing()
            {
                try
                {
                    String value = (String) getCellEditorValue();
                    Double.parseDouble( value ); // valida número decimal

                    return super.stopCellEditing();
                }
                catch ( NumberFormatException e )
                {
                    JOptionPane.showMessageDialog( null, "Digite um número válido (ex: 1 ou 1.4)!" );
                    return false;
                }
            }
        }
        );
    }

    private void verificar()
    {

        try
        {
            int linhaSelecionada = table.getSelectedRow();

            if ( linhaSelecionada > -1 )
            {
                int codigo = Integer.parseInt( table.getValueAt( linhaSelecionada, 0 ).toString() );
                double qtd = Double.parseDouble( table.getValueAt( linhaSelecionada, 3 ).toString() );

                String tipo = getTipo( codigo, qtd );
                double qtdAcertar = getQtdAcertar( codigo, qtd );
                System.err.println( "LINHA SELECIONADA: " + linhaSelecionada );

                System.out.println( "CODIGO: " + codigo );
                System.out.println( "QTD: " + qtd );
                System.out.println( "QTD ACERTAR: " + qtdAcertar );

                if ( tipo.equals( "SAIDA" ) )
                {
                    if ( !possivel_quantidade( codigo, qtdAcertar ) )
                    {
                        double qtdAnterior = getQtdAnterior( codigo );
                        System.err.println( "Quantidade Anterior: " + qtdAnterior );
                        table.getModel().setValueAt( qtdAnterior, linhaSelecionada, 3 );
                        JOptionPane.showMessageDialog( null, "Não é possivel actualizar para esta quantidade" );

                    }
                }
            }

        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }

    }

}
