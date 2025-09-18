/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package visao;

import comercial.controller.FamiliasController;
import comercial.controller.ItemVendasController;
import comercial.controller.PrecosController;
import comercial.controller.ProdutosController;
import comercial.controller.StoksController;
import comercial.controller.TipoProdutosController;
import comercial.controller.VendasController;
import controller.ProdutoController;
import controller.StockController;
import controller.TipoClienteController;
import controller.TipoProdutoController;
import dao.ArmazemDao;
import dao.BancoDao;
import dao.ClienteDao;
import dao.DescontoDao;
import dao.EstornoDao;
import dao.ItemEstornoDao;
import dao.ItemProformaDao;
import dao.ItemSaidaDao;
import dao.PrecoDao;
import dao.ProFormaDao;
import dao.ProdutoDao;
import dao.StockDao;
import dao.UsuarioDao;
import dao.VasilhameDao;
import dao.SaidasProdutosDao;
import entity.Familia;
import entity.TbEstorno;
import entity.TbItemProForma;
import entity.TbItemEstorno;
import entity.TbProForma;
import entity.TbProduto;
import entity.TbStock;
import entity.TbVasilhame;
import entity.TbEstorno;
import entity.TbItemEstorno;
import entity.TbPreco;
import entity.TbTipoProduto;
import exemplos.PermitirNumeros;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import lista.ListaEstornoProdutos;
import lista.ListaSaidaProdutos;
import lista.ProfromaRelatorio;
import modelo.ClienteModelo;
import modelo.ProdutoModelo;
import modelo.StockModelo;
import modelo.TipoClienteModelo;
import util.BDConexao;
import util.DVML;
import util.JPAEntityMannagerFactoryUtil;
import util.MetodosUtil;
import util.OperacaoSistemaUtil;
import static visao.EntradaProdutoVisao.cmbArmazem;
import static visao.VendaUsuarioVisao.cmbFamilia;

/**
 *
 * @author Domingos Dala Vunge
 */
public class EstornoVisao extends javax.swing.JFrame
{

    private EntityManagerFactory emf = JPAEntityMannagerFactoryUtil.em;
    private static ProdutosController produtosController;
    private static TipoProdutosController tipoProdutoController;
    private static VendasController vendasController;
    private static ItemVendasController itemVendasController;
    private static FamiliasController familiaController;
    private static PrecosController precosController;
    private static StoksController stocksController;
    private VasilhameDao vasilhameDao = new VasilhameDao( emf );
    private ProFormaDao proFormaDao = new ProFormaDao( emf );
    private ItemProformaDao itemProformaDao = new ItemProformaDao( emf );
    private ItemEstornoDao itemEstornoDao = new ItemEstornoDao( emf );
    private DescontoDao descontoDao = new DescontoDao( emf );
    private PrecoDao precoDao = new PrecoDao( emf );
    private TbItemEstorno itemEstorno;
    private TbStock stock_local;
    private TbEstorno estorno;
    private static ProdutoDao produtoDao;
    private StockDao stockDao = new StockDao( emf );
    private UsuarioDao usuarioDao = new UsuarioDao( emf );
    private ClienteDao clienteDao = new ClienteDao( emf );
    private EstornoDao estornoDao = new EstornoDao( emf );
    private ArmazemDao armazemDao = new ArmazemDao( emf );
    private ItemSaidaDao itemSaidasProdutosDao = new ItemSaidaDao( emf );
    private TbVasilhame vasilhame;
    private BancoDao bancoDao = new BancoDao( emf );
    private static BDConexao conexao;
    private static int cod_usuario;
    private TipoClienteModelo tipoClienteModelo;
    private StockController stockController;
    private int linha = 0, coordenada = 1;
    private double soma_total = 0;
    private ProdutoModelo produtoModelo;
    private static int linha_actual = -1;
    static EstornoVisao obj;
    private OperacaoSistemaUtil osu = new OperacaoSistemaUtil();

    public EstornoVisao( int cod_usuario, BDConexao conexao ) throws SQLException
    {

        initComponents();
        confiLabel();
        setLocationRelativeTo( null );
        EstornoVisao.conexao = conexao;
        produtosController = new ProdutosController( EstornoVisao.conexao );
        tipoProdutoController = new TipoProdutosController( EstornoVisao.conexao );
        vendasController = new VendasController( EstornoVisao.conexao );
        itemVendasController = new ItemVendasController( EstornoVisao.conexao );
        precosController = new PrecosController( EstornoVisao.conexao );
        familiaController = new FamiliasController( EstornoVisao.conexao );
        stocksController = new StoksController( EstornoVisao.conexao );

        txtQuatindade.setDocument( new PermitirNumeros() );
        txtCodigoProduto.setDocument( new PermitirNumeros() );
        txtCodigoBarra.setDocument( new PermitirNumeros() );
        this.cod_usuario = cod_usuario;
        this.conexao = conexao;
        cmbArmazem.setModel( new DefaultComboBoxModel( armazemDao.buscaTodos() ) );
        DVML.activar_cmb_armazem( cmbArmazem );
        cmbSubFamilia.setModel( new DefaultComboBoxModel( conexao.getElementos( "tb_tipo_produto", "designacao", false ) ) );
        cmbProduto.setModel( new DefaultComboBoxModel( conexao.getElementos2( "tb_produto", "designacao", "cod_Tipo_Produto", getCodigoTipoProduto() ) ) );
        txtCodigoBarra.requestFocus();
        txtQuatindade.setText( "1" );

    }

    public void confiLabel()
    {

        lbCodigoProduto.setHorizontalAlignment( JLabel.RIGHT );
        lbCategoria.setHorizontalAlignment( JLabel.RIGHT );
        lbProduto.setHorizontalAlignment( JLabel.RIGHT );
        lbPreco.setHorizontalAlignment( JLabel.RIGHT );
        lbQuantidade.setHorizontalAlignment( JLabel.RIGHT );
        lbQuantidadeStock.setHorizontalAlignment( JLabel.RIGHT );

    }

    public int getCodigoTipoProduto() throws SQLException
    {
        return conexao.getCodigoPublico( "tb_tipo_produto", String.valueOf( cmbSubFamilia.getSelectedItem() ) );
    }

//    public static int getCodigoProduto()
//    {
//        return produtoDao.getProdutoByDescricao( cmbProduto.getSelectedItem().toString() ).getCodigo();
//    }
    public boolean estado_critico() throws SQLException
    {
        TbStock stock = stockDao.getStockByDescricao( getCodigoProduto(), getCodigoArmazem() );
        double qtd_minima = stock.getQuantBaixa(),
                qtd_existente = stock.getQuantidadeExistente(),
                qtd_critica = stock.getQuantCritica();
        return qtd_minima < qtd_existente
                && qtd_existente <= qtd_critica;

    }

    public static boolean possivel_quantidade() throws SQLException
    {

        double quant_possivel = conexao.getQuantidade_Existente_Publico( getCodigoProduto(), getCodigoArmazem() ) - conexao.getQuantidade_minima_publico( getCodigoProduto(), getCodigoArmazem() );

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
                    return rs.getDouble( "preco_estorno" );
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
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        lbCategoria = new javax.swing.JLabel();
        cmbSubFamilia = new javax.swing.JComboBox();
        lbCodigoProduto = new javax.swing.JLabel();
        txtCodigoProduto = new javax.swing.JTextField();
        lbPreco = new javax.swing.JLabel();
        cmbProduto = new javax.swing.JComboBox();
        lbProduto = new javax.swing.JLabel();
        txtPreco = new javax.swing.JTextField();
        lbQuantidade = new javax.swing.JLabel();
        txtQuatindade = new javax.swing.JTextField();
        btn_remover = new javax.swing.JButton();
        btn_adicionar = new javax.swing.JButton();
        txtQuantidaStock = new javax.swing.JTextField();
        lbQuantidadeStock = new javax.swing.JLabel();
        lbCodigoProduto1 = new javax.swing.JLabel();
        txtCodigoBarra = new javax.swing.JTextField();
        status_mensagem_primaria = new javax.swing.JLabel();
        status_mensagem_secundaria = new javax.swing.JLabel();
        cmbArmazem = new javax.swing.JComboBox();
        lbPreco1 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        cmbFamilia = new javax.swing.JComboBox<>();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        btnCancelar = new javax.swing.JButton();
        btn_imprimir_factura = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtAreaOBS = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("...:::::  KITANDA - ESTORNOS ::::...");

        jPanel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jPanel1.setFont(new java.awt.Font("Showcard Gothic", 0, 24)); // NOI18N
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Quebra", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 15), new java.awt.Color(51, 153, 0))); // NOI18N
        jPanel4.setForeground(new java.awt.Color(102, 153, 0));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lbCategoria.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 16)); // NOI18N
        lbCategoria.setText("Categoria:");
        jPanel4.add(lbCategoria, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, -1, -1));

        cmbSubFamilia.setBackground(new java.awt.Color(51, 153, 0));
        cmbSubFamilia.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 14)); // NOI18N
        cmbSubFamilia.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbSubFamiliaActionPerformed(evt);
            }
        });
        jPanel4.add(cmbSubFamilia, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 70, 200, -1));

        lbCodigoProduto.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 16)); // NOI18N
        lbCodigoProduto.setText("CodProd:");
        jPanel4.add(lbCodigoProduto, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 220, 60, 20));

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
        jPanel4.add(txtCodigoProduto, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 215, 100, 30));

        lbPreco.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 16)); // NOI18N
        lbPreco.setText("Preco:");
        jPanel4.add(lbPreco, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 140, -1, -1));

        cmbProduto.setBackground(new java.awt.Color(51, 153, 0));
        cmbProduto.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 14)); // NOI18N
        cmbProduto.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbProdutoActionPerformed(evt);
            }
        });
        jPanel4.add(cmbProduto, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 100, 200, -1));

        lbProduto.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 16)); // NOI18N
        lbProduto.setText("Produto:");
        jPanel4.add(lbProduto, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 100, -1, -1));

        txtPreco.setEditable(false);
        txtPreco.setBackground(new java.awt.Color(51, 153, 0));
        txtPreco.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        txtPreco.setForeground(new java.awt.Color(255, 255, 255));
        txtPreco.setCaretColor(new java.awt.Color(255, 255, 255));
        jPanel4.add(txtPreco, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 140, 88, -1));

        lbQuantidade.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 16)); // NOI18N
        lbQuantidade.setText("Qtd:");
        jPanel4.add(lbQuantidade, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 220, -1, -1));

        txtQuatindade.setBackground(new java.awt.Color(51, 153, 0));
        txtQuatindade.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
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
        jPanel4.add(txtQuatindade, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 210, 90, 30));

        btn_remover.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/2934_32x32.png"))); // NOI18N
        btn_remover.setToolTipText("click para remover produtos do carrinho");
        btn_remover.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btn_removerActionPerformed(evt);
            }
        });
        jPanel4.add(btn_remover, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 290, 50, 41));

        btn_adicionar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/Button-Add-icon.png"))); // NOI18N
        btn_adicionar.setToolTipText("click para adicionar no carrinho");
        btn_adicionar.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btn_adicionarActionPerformed(evt);
            }
        });
        jPanel4.add(btn_adicionar, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 290, 50, 41));

        txtQuantidaStock.setEditable(false);
        txtQuantidaStock.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        txtQuantidaStock.setForeground(new java.awt.Color(255, 255, 255));
        txtQuantidaStock.setCaretColor(new java.awt.Color(255, 255, 255));
        jPanel4.add(txtQuantidaStock, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 140, 100, 59));

        lbQuantidadeStock.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 16)); // NOI18N
        lbQuantidadeStock.setText("Q. Stock:");
        jPanel4.add(lbQuantidadeStock, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 170, -1, -1));

        lbCodigoProduto1.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 16)); // NOI18N
        lbCodigoProduto1.setText("CodBarra:");
        jPanel4.add(lbCodigoProduto1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 260, -1, -1));

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
        jPanel4.add(txtCodigoBarra, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 260, 150, -1));

        status_mensagem_primaria.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        status_mensagem_primaria.setForeground(new java.awt.Color(204, 153, 0));
        status_mensagem_primaria.setText("sahdbvhsdva");
        jPanel4.add(status_mensagem_primaria, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 380, -1));

        status_mensagem_secundaria.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        status_mensagem_secundaria.setText("...");
        jPanel4.add(status_mensagem_secundaria, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, 40, -1));

        cmbArmazem.setBackground(new java.awt.Color(51, 153, 0));
        cmbArmazem.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 14)); // NOI18N
        cmbArmazem.setEnabled(false);
        cmbArmazem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbArmazemActionPerformed(evt);
            }
        });
        jPanel4.add(cmbArmazem, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 310, 270, -1));

        lbPreco1.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 16)); // NOI18N
        lbPreco1.setText("Armzém");
        jPanel4.add(lbPreco1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 290, 78, 17));

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/proucura.png"))); // NOI18N
        jButton4.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel4.add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 210, 40, 40));

        cmbFamilia.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        cmbFamilia.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbFamiliaActionPerformed(evt);
            }
        });
        jPanel4.add(cmbFamilia, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 40, 200, 30));

        jPanel1.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 410, 340));

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Produtos para quebra"));

        table.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "Cod Prod", "Designacao", "Qtd"
            }
        )
        {
            Class[] types = new Class []
            {
                java.lang.Integer.class, java.lang.String.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean []
            {
                false, false, false
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
        jScrollPane1.setViewportView(table);
        table.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        if (table.getColumnModel().getColumnCount() > 0)
        {
            table.getColumnModel().getColumn(0).setMinWidth(50);
            table.getColumnModel().getColumn(1).setMinWidth(250);
            table.getColumnModel().getColumn(2).setMaxWidth(100);
        }

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 392, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 340, 410, 90));

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
        jPanel3.add(btnCancelar, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 10, -1, -1));

        btn_imprimir_factura.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/impressora1.png"))); // NOI18N
        btn_imprimir_factura.setText("Efectuar Quebra");
        btn_imprimir_factura.setAlignmentX(0.5F);
        btn_imprimir_factura.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btn_imprimir_facturaActionPerformed(evt);
            }
        });
        jPanel3.add(btn_imprimir_factura, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 10, 210, -1));

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 510, 410, 60));

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Motivo da Quebra"));

        txtAreaOBS.setColumns(20);
        txtAreaOBS.setRows(5);
        txtAreaOBS.setText("\n");
        jScrollPane2.setViewportView(txtAreaOBS);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 392, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 44, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel1.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 430, 410, 80));

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
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
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

            txtPreco.setText( String.valueOf( getPrecoProduto( getCodigoProduto(), isStocavel( produto.getStocavel() ) ) ) );
            adicionar_preco_quantidade();

        }
        catch ( SQLException ex )
        {
            Logger.getLogger( EstornoVisao.class.getName() ).log( Level.SEVERE, null, ex );
        }
        catch ( Exception ex )
        {

        }

    }//GEN-LAST:event_cmbSubFamiliaActionPerformed

    private void cmbProdutoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbProdutoActionPerformed

        try
        {
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
            JOptionPane.showMessageDialog( null, "Possivelmente não selecionaste \n nenhuma linha ou não existe dados na tabela", "AVISO", JOptionPane.WARNING_MESSAGE );
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
        procedimento_adicionar();
    }//GEN-LAST:event_txtQuatindadeActionPerformed

    private void btn_imprimir_facturaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_imprimir_facturaActionPerformed
        // TODO add your handling code here:
        procedimento_salvar();
    }//GEN-LAST:event_btn_imprimir_facturaActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton4ActionPerformed
    {//GEN-HEADEREND:event_jButton4ActionPerformed

        try
        {

            new BuscaProdutoVisao( this, rootPaneCheckingEnabled, getCodigoArmazem(), DVML.JANELA_ESTORNO, conexao ).show();

        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void cmbFamiliaActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbFamiliaActionPerformed
    {//GEN-HEADEREND:event_cmbFamiliaActionPerformed

        cmbSubFamilia.setModel( new DefaultComboBoxModel( tipoProdutoController.getVectorByIdFamilia( getIdFamilia() ) ) );
        cmbProduto.setModel( new DefaultComboBoxModel( ( produtosController.getVectorByIdTipoProduto( getIdTipoProduto() ) ) ) );
    }//GEN-LAST:event_cmbFamiliaActionPerformed

    public static void accao_codigo_interno_enter_busca_exterior( int codigo )
    {

        try
        {
            System.out.println( "ID PRODUTO EXTERIOR: " + codigo );
            TbProduto produtoLocal = ( TbProduto ) produtosController.findById( codigo );
            procedimentoAdicionarTabela( produtoLocal );
        }
        catch ( Exception ex )
        {
            ex.printStackTrace();
            Logger.getLogger( VendaUsuarioVisao.class.getName() ).log( Level.SEVERE, null, ex );
            JOptionPane.showMessageDialog( null, "Este produto não existe no armazém " + cmbArmazem.getSelectedItem(), DVML.DVML_COMERCIAL, JOptionPane.ERROR_MESSAGE );
        }

    }

//    public static void accao_codigo_interno_enter_busca_exterior( int codigo )
//    {
//
//        try
//        {
//
////            int   codigo = Integer.parseInt(txtCodigoProduto.getText() );
//            TbProduto produto = produtoDao.findTbProduto( codigo );
////            cmbFamilia.setSelectedItem( produto.getCodTipoProduto().getFkFamilia().getDesignacao() );
//            cmbSubFamilia.setSelectedItem( produto.getCodTipoProduto().getDesignacao() );
//            cmbProduto.setSelectedItem( produto.getDesignacao() );
////            adicionar_preco_quantidade_anitgo();
////            if ( rbTranstorno.isSelected() )
////            {
////                procedimento_adicionar_sem_transtorno();
////            }
////            elsecodigo
////            {
//            procedimento_adicionar1();
////            }
//            txtCodigoProduto.setText( "" );
//            txtQuatindade.setText( "1" );
//            txtQuatindade.requestFocus();
//
//        }
//        catch ( Exception ex )
//        {
//            Logger.getLogger( VendaUsuarioVisao.class.getName() ).log( Level.SEVERE, null, ex );
//            JOptionPane.showMessageDialog( null, "Este produto não existe no armazém " + cmbArmazem.getSelectedItem(), DVML.DVML_COMERCIAL, JOptionPane.ERROR_MESSAGE );
//        }
//
//    }
    public static void procedimento_adicionar1()
    {

        try
        {

            if ( !campos_invalidos() )
            {

                if ( isStocavel( produtoDao.findTbProduto( getCodigoProduto() ).getStocavel() ) )
                {
                    System.out.println( "&&&&&&&&&&#### ENTREI AQUI." );
                    if ( possivel_quantidade() )
                    {

                        adicionar_produto();

                    }
                    else
                    {
                        JOptionPane.showMessageDialog( null, "O produto: " + produtoDao.findTbProduto( getCodigoProduto() ).getDesignacao() + " não pode ser sir esta quantidade", DVML.DVML_COMERCIAL, JOptionPane.ERROR_MESSAGE );
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

    public static int getCodigoProduto()
    {
        //return conexao.getCodigoPublico("tb_produto", String.valueOf(  cmbProduto.getSelectedItem()));   
        return produtosController.findByDesignacao( cmbProduto.getSelectedItem().toString() ).getCodigo();

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

    public static boolean campos_invalidos()
    {
        return txtQuatindade.getText().equals( "" );
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

                txtQuantidaStock.setBackground( Color.RED );
                txtQuantidaStock.setForeground( Color.BLACK );
            }
            else
            {
                txtQuantidaStock.setBackground( new Color( 51, 153, 0, 255 ) );
            }

            txtCodigoBarra.setText( String.valueOf( produto_local.getCodBarra() ) );
            //actualizar
//            txtLocal.setText( String.valueOf( produto_local.getCodLocal().getDesignacao() ) );
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

//    public void adicionar_preco_quantidade()
//    {
//
//        try
//        {
//
//            if ( stockDao.get_stock_by_id_produto_and_id_armazem( getCodigoProduto(), getCodigoArmazem() ).getQuantidadeExistente() <= stockDao.get_stock_by_id_produto_and_id_armazem( getCodigoProduto(), getCodigoArmazem() ).getQuantCritica() )
//            {
//
//                txtQuantidaStock.setBackground( Color.RED );
//                txtQuantidaStock.setForeground( Color.BLACK );
//            }
//            else
//            {
//                txtQuantidaStock.setBackground( new Color( 51, 153, 0, 255 ) );
//            }
//
//            TbProduto produto = produtoDao.findTbProduto( getCodigoProduto() );
//
//            if ( stockDao.exist_produto_stock( getCodigoProduto(), getCodigoArmazem() ) )
//            {
//                txtQuantidaStock.setText( String.valueOf( stockDao.get_stock_by_id_produto_and_id_armazem( getCodigoProduto(), getCodigoArmazem() ).getQuantidadeExistente() ) );
//
//            }
//            else
//            {
//                txtQuantidaStock.setText( "0" );
//            }
//
//        }
//        catch ( Exception ex )
//        {
//            ex.printStackTrace();
//            txtQuantidaStock.setText( "0" );
//            Logger.getLogger( EstornoVisao.class.getName() ).log( Level.SEVERE, null, ex );
//        }
//
//    }
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
//          txtFuncionario.setText("");
//          soma_total = 0;

    }

    private boolean transtorno()
    {

        int cod_produto = 0;
        double qtd = 0d, qtd_aceite = 0d;

        DefaultTableModel modelo = ( DefaultTableModel ) table.getModel();
        boolean transtorno = false;

        for ( int i = 0; i < table.getRowCount(); i++ )
        {

            cod_produto = Integer.parseInt( String.valueOf( table.getModel().getValueAt( i, 0 ) ) );
            qtd = Double.parseDouble( String.valueOf( table.getModel().getValueAt( i, 2 ) ) );

            if ( !possivel_quantidade( cod_produto, qtd ) )
            {

                transtorno = true;
                qtd_aceite = conexao.getQuantidade_Existente_Publico( cod_produto, getCodigoArmazem() );

                if ( qtd_aceite != 0 )
                {

                    int opcao = JOptionPane.showConfirmDialog( null, "Desculpe pelo transtorno, o produto " + produtoDao.findTbProduto( cod_produto ).getDesignacao() + " só é possivel  a " + qtd_aceite + " quantidade(s)" + ", contrariamente de " + qtd + "\n Deseja actualizar ou remover da tabela ?" );

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

    public boolean possivel_quantidade( int cod_produto, double qtd )
    {

        double quant_possivel = conexao.getQuantidade_Existente_Publico( cod_produto, getCodigoArmazem() ) - conexao.getQuantidade_minima_publico( cod_produto, getCodigoArmazem() );
        return quant_possivel >= qtd;

    }

    private boolean validar_campo()
    {

        if ( txtAreaOBS.getText().trim().equalsIgnoreCase( "" ) )
        {
            txtAreaOBS.requestFocus();
            txtAreaOBS.setBackground( Color.YELLOW );
            JOptionPane.showMessageDialog( null, "Pf. Digite o Motivo da Quebra!...", "AVISO", JOptionPane.WARNING_MESSAGE );
            txtAreaOBS.setBackground( Color.WHITE );
            return true;
        }

        else
        {
            txtAreaOBS.setBackground( Color.WHITE );
            return false;
        }

    }

    public void procedimento_salvar()
    {
        if ( !validar_campo() )
        {

            if ( !tabela_vazia() )
            {

                try
                {
                    if ( !transtorno() )
                    {
                        salvar_estorno();
                        salvarItemestorno();
                    }
                }
                catch ( Exception e )
                {
                    e.printStackTrace();
                }

            }
            else
            {
                JOptionPane.showMessageDialog( null, "Impossivel fazer quebra porque não há produtos", "ERRO", JOptionPane.ERROR_MESSAGE );
            }

        }

    }

    private boolean tabela_vazia()
    {
        DefaultTableModel modelo = ( DefaultTableModel ) table.getModel();
        return modelo.getRowCount() == 0;

    }

    private void operacao_salvar_estorno()
    {
        try
        {

            salvar_estorno();
            salvarItemestorno();

        }
        catch ( Exception ex )
        {
            Logger.getLogger( EstornoVisao.class.getName() ).log( Level.SEVERE, null, ex );
        }

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

    public boolean campos_invalidos_qtd()
    {

        return txtQuatindade.getText().equals( "" );

    }

//    //CLASSE EVENTO TECLADO 
//  
    //----------- evento do teclado ---------------------------------------
    class TratarEventoTecladoCodigoProduto implements KeyListener
    {

        String prefixo = "";
        int codigo = 0, codigo_categoria = 0, quatidade_produto = 0;

        public void keyPressed( KeyEvent evt )
        {

            if ( evt.getKeyCode() != KeyEvent.VK_BACK_SPACE && evt.getKeyCode() == KeyEvent.VK_ENTER && evt.getKeyCode() != KeyEvent.VK_BACK_SPACE )
            {
                char key = evt.getKeyChar();

                prefixo = txtCodigoProduto.getText().trim() + key;
                System.out.println( "Codigo: " + prefixo );

                codigo = Integer.parseInt( prefixo.trim() );

                try
                {

                    TbProduto produto = produtoDao.findTbProduto( codigo );

                    cmbSubFamilia.setSelectedItem( produto.getCodTipoProduto().getDesignacao() );
                    cmbProduto.setSelectedItem( produto.getDesignacao() );

                    adicionar_preco_quantidade();
                    adicionar_botao_retificar();

                    txtCodigoProduto.setText( "" );
                    txtQuatindade.setText( "" );
                    txtQuatindade.requestFocus();

                }
                catch ( Exception ex )
                {
                    Logger.getLogger( EstornoVisao.class.getName() ).log( Level.SEVERE, null, ex );

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

    //----------- evento do teclado do codigo_barra Barra---------------------------------------
    class TratarEventoTecladoCodBarra implements KeyListener
    {

        String prefixo = "";
        long codigo_barra = 0, codigo_categoria = 0, quatidade_produto = 0;

        public void keyPressed( KeyEvent evt )
        {

            if ( evt.getKeyCode() != KeyEvent.VK_BACK_SPACE && evt.getKeyCode() == KeyEvent.VK_ENTER && evt.getKeyCode() != KeyEvent.VK_BACK_SPACE )
            {

                char key = evt.getKeyChar();

                prefixo = txtCodigoBarra.getText().trim() + key;
                System.out.println( "Codigo: " + prefixo );

                codigo_barra = Long.parseLong( prefixo.trim() );

                try
                {

                    ProdutoModelo produtoModelo = new ProdutoController( conexao ).getProdutoByCodigoBarra( codigo_barra );
                    Vector<String> vector = new Vector<String>();
                    vector.add( produtoModelo.getDesignacao() );
                    cmbProduto.setModel( new DefaultComboBoxModel( vector ) );

                    adicionar_preco_quantidade();
                    adicionar_botao();

                    txtCodigoBarra.setText( "" );
                    txtCodigoBarra.requestFocus();
                }
                catch ( SQLException ex )
                {
                    JOptionPane.showMessageDialog( null, "Provavelmente o produto nao é stocavel!..." );
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

        DefaultTableModel modelo = ( DefaultTableModel ) table.getModel();

        if ( !exist_produto( getCodigoProduto() ) )
        {
            if ( !validar_zero() )
            {
                modelo.addRow( new Object[]
                {
                    getCodigoProduto(),
                    getDescricao_Produto(),
                    getQuantidade()

                } );
            }
            else
            {
                JOptionPane.showMessageDialog( null, "Atenção\nA quantidade a ser quebrada não pode ser igual a zero!" );
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

        DefaultTableModel modelo = ( DefaultTableModel ) table.getModel();

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

        DefaultTableModel modelo = ( DefaultTableModel ) table.getModel();

        modelo.addRow( new Object[]
        {
            codigo,
            descricao,
            getQuantidade()
        } );
    }

    public void salvarItemestorno()
    {
        int cod_estorno = estornoDao.getLastSaidasProdutos();
        boolean efectuada = true;
        this.estorno = estornoDao.findTbEstorno( cod_estorno );

        int cod_produto, qtd, qtd_aceite;

        for ( int i = 0; i < table.getRowCount(); i++ )
        {
            try
            {

                itemEstorno = new TbItemEstorno();
                TbProduto produto_local = ( TbProduto ) produtosController.findById( Integer.parseInt( String.valueOf( table.getModel().getValueAt( i, 0 ) ) ) );
                itemEstorno.setFkProdutos( produto_local );
                itemEstorno.setFkEstorno( new TbEstorno( cod_estorno ) );

                itemEstorno.setQuantidade( Integer.parseInt( String.valueOf( table.getModel().getValueAt( i, 2 ) ) ) );
                itemEstornoDao.create( itemEstorno );
                this.stock_local = stockDao.get_stock_by_id_produto_and_id_armazem( itemEstorno.getFkProdutos().getCodigo(), getCodigoArmazem() );

                //so retirar caso existir mesmo no armazém em questão.
                if ( stock_local.getCodigo() != 0 )
                {
                    actualizar_quantidade( itemEstorno.getFkProdutos().getCodigo(), itemEstorno.getQuantidade(), getCodigoArmazem() );
                }

            }
            catch ( Exception e )
            {
                e.printStackTrace();
                efectuada = false;
                JOptionPane.showMessageDialog( null, "Falha ao registrar quebra: " + itemEstorno.getFkProdutos().getCodigo() );
                break;
            }
        }

        if ( efectuada )
        {
            JOptionPane.showMessageDialog( null, "Quebra efectuado com sucesso!.." );

            try
            {
                limpar();
                remover_all_produto();
//                adicionar_preco_quantidade_anitgo();

            }
            catch ( Exception e )
            {
            }

            txtQuatindade.requestFocus();

            ListaEstornoProdutos listaEstornos = new ListaEstornoProdutos( cod_estorno );

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

        DefaultTableModel modelo = ( DefaultTableModel ) table.getModel();

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

        DefaultTableModel modelo = ( DefaultTableModel ) table.getModel();

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

    private static int getCodigoArmazem()
    {
        return conexao.getCodigoPublico( "tb_armazem", String.valueOf( cmbArmazem.getSelectedItem() ) );
    }

    public void salvar_estorno()
    {

        TbEstorno estorno = new TbEstorno();

        estorno.setDataEstorno( new Date() );
        estorno.setFkUsuario( usuarioDao.findTbUsuario( cod_usuario ) );
        estorno.setIdArmazemFK( armazemDao.findTbArmazem( getCodigoArmazem() ) );
        estorno.setHoraEstorno( new Date() );
        estorno.setObs( txtAreaOBS.getText() );
        estorno.setStatusEliminado( "false" );

        try
        {
            estornoDao.create( estorno );

        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }

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

        new EstornoVisao( 15, new BDConexao() ).show( true );

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelar;
    public static javax.swing.JButton btn_adicionar;
    public static javax.swing.JButton btn_imprimir_factura;
    public static javax.swing.JButton btn_remover;
    private javax.swing.ButtonGroup buttonGroup1;
    private static javax.swing.JComboBox cmbArmazem;
    public static javax.swing.JComboBox<String> cmbFamilia;
    public static javax.swing.JComboBox cmbProduto;
    public static javax.swing.JComboBox cmbSubFamilia;
    private javax.swing.JButton jButton4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lbCategoria;
    private javax.swing.JLabel lbCodigoProduto;
    private javax.swing.JLabel lbCodigoProduto1;
    private javax.swing.JLabel lbPreco;
    private javax.swing.JLabel lbPreco1;
    private javax.swing.JLabel lbProduto;
    private javax.swing.JLabel lbQuantidade;
    private javax.swing.JLabel lbQuantidadeStock;
    public static javax.swing.JLabel status_mensagem_primaria;
    public static javax.swing.JLabel status_mensagem_secundaria;
    private static javax.swing.JTable table;
    private javax.swing.JTextArea txtAreaOBS;
    public static javax.swing.JTextField txtCodigoBarra;
    public static javax.swing.JTextField txtCodigoProduto;
    private static javax.swing.JTextField txtPreco;
    private static javax.swing.JTextField txtQuantidaStock;
    public static javax.swing.JTextField txtQuatindade;
    // End of variables declaration//GEN-END:variables

    private static boolean exist_produto( int codigo )
    {

        DefaultTableModel modelo = ( DefaultTableModel ) table.getModel();

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
        DefaultTableModel modelo = ( DefaultTableModel ) table.getModel();
        modelo.setValueAt( quantidade, linha_actual, 3 );
        linha_actual = -1;

    }

    private void accao_codigo_interno_enter()
    {
        try
        {
            int codigo = Integer.parseInt( txtCodigoProduto.getText() );
            TbProduto produto = ( TbProduto ) produtosController.findById( codigo );
            procedimentoAdicionarTabela( produto );
        }
        catch ( Exception ex )
        {
//            ex.printStackTrace();
            Logger.getLogger( VendaUsuarioVisao.class.getName() ).log( Level.SEVERE, null, ex );
            JOptionPane.showMessageDialog( null, "Este produto não existe no armazém " + cmbArmazem.getSelectedItem(), DVML.DVML_COMERCIAL, JOptionPane.ERROR_MESSAGE );
        }

    }

    private static void procedimentoAdicionarTabela( TbProduto produto )
    {
        try
        {
            if ( !Objects.isNull( produto ) )
            {
                Integer codTipoProduto = produto.getCodTipoProduto().getCodigo();
                TbTipoProduto tipoProduto = ( TbTipoProduto ) tipoProdutoController.findById( codTipoProduto );
                Integer codFamilia = tipoProduto.getFkFamilia().getPkFamilia();
                Familia familia = ( Familia ) familiaController.findById( codFamilia );
                cmbFamilia.setSelectedItem( familia.getDesignacao() );
                cmbSubFamilia.setSelectedItem( tipoProduto.getDesignacao() );

                cmbProduto.setModel( new DefaultComboBoxModel( produtosController.getVector() ) );
                cmbProduto.setSelectedItem( produto.getDesignacao() );
                adicionar_preco_quantidade_anitgo();
//                if ( rbTranstorno.isSelected() )
//                {
//                    procedimento_adicionar_sem_transtorno();
//                }
//                else
//                {
                procedimento_adicionar();
//                }
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

//    public void adicionar_preco_quantidade_anitgo()
//    {
//
//        try
//        {
//
//            if ( conexao.getQtdExistenteStock( getCodigoProduto(), getCodigoArmazem() ) <= conexao.getQtdCriticaStock( getCodigoProduto(), getCodigoArmazem() ) )
//            {
//                txtQuantidaStock.setBackground( Color.RED );
//                txtQuantidaStock.setForeground( Color.BLACK );
//            }
//
//            else
//            {
//                txtQuantidaStock.setBackground( new Color( 51, 153, 0, 255 ) );
//            }
//
//            txtQuantidaStock.setText( String.valueOf( conexao.getQtdExistenteStock( getCodigoProduto(), getCodigoArmazem() ) ) );
//
//        }
//        catch ( Exception ex )
//        {
//            Logger.getLogger( EstornoVisao.class.getName() ).log( Level.SEVERE, null, ex );
//        }
//
//    }
    public static void procedimento_adicionar()
    {

        try
        {

            if ( !campos_invalidos() )
            {
//                if ( !isProdutoExpirado( getCodigoProduto() ) )
//                {
                TbProduto produto = ( TbProduto ) produtosController.findById( getCodigoProduto() );
                if ( isStocavel( produto.getStocavel() ) )
                {

                    if ( possivel_quantidade() )
                    {
//                            if ( estado_critico() )
//                            {
//                                JOptionPane.showMessageDialog( null, "O produto: " + produto.getDesignacao() + " precisa de ser actualizado no stock", DVML.DVML_COMERCIAL, JOptionPane.WARNING_MESSAGE );
//                            }
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
//                }
//                else
//                {
//                    JOptionPane.showMessageDialog( null, "Impossivel adicionar o produto porque já foi expirado.", "Aviso", JOptionPane.WARNING_MESSAGE );
//                }

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

    private void accao_codigo_barra_enter()
    {
//        try {
//
//           long   codigo_barra = Long.parseLong(txtCodigoBarra.getText() );
//
//           TbProduto produto = produtoDao.getProdutoByCodigoBarra(codigo_barra);
//
//            cmbCategoria.setSelectedItem(  produto.getCodTipoProduto().getDesignacao() );
//            cmbProduto.setSelectedItem(  produto.getDesignacao() );
//
//            adicionar_preco_quantidade();   
//
//            adicionar_botao_retificar();
//       
//            txtCodigoBarra.setText("");                           
//            txtCodigoBarra.requestFocus();
//
//        } catch (Exception    ex) {
//            Logger.getLogger(SaidaUsuarioVisao.class.getName()).log(Level.SEVERE, null, ex);
//            JOptionPane.showMessageDialog(null, "Falha ao buscar o produto", DVML.DVML_COMERCIAL, JOptionPane.ERROR_MESSAGE);
//        }

        String codigo_barra = txtCodigoBarra.getText().trim();

        TbProduto produto = produtoDao.getProdutoByCodigoBarra( codigo_barra );

//            cmbSubFamilia.setSelectedItem( produto.getCodTipoProduto().getDesignacao() );
        //actualizar
//            txtLocal.setText( produto.getCodLocal().getDesignacao() );
//            txtCodigoManual.setText( produto.getCodigoManual() );
//            cmbSubFamilia.setSelectedItem( tipoProdutoDao.findTbTipoProduto(produto.getCodTipoProduto().getCodigo() ).getDesignacao() );
//            cmbFamilia.setSelectedItem( produto.getCodTipoProduto().getFkFamilia().getDesignacao() );
        //Devo setar a combo dos produtos (Isto porque quando a se faz a busca na cmbCategoria remove todos os produtos exceptos os de categoria actual)
        cmbProduto.setModel( new DefaultComboBoxModel( produtoDao.getAllDesingnacaoProduto() ) );
        cmbProduto.setSelectedItem( produto.getDesignacao() );

        adicionar_preco_quantidade_anitgo();
        procedimento_adicionar();

        txtCodigoBarra.setText( "" );
//            txtCodigoManual.setText( "" );
        txtQuatindade.setText( "1" );
        txtCodigoBarra.requestFocus();

    }

    public static EstornoVisao getObj( int cod_usuario )
    {
        if ( obj == null )
        {
            try
            {
                obj = new EstornoVisao( cod_usuario, conexao );
            }
            catch ( Exception e )
            {
            }
        }

        return obj;

    }

    public void actualizar_quantidade( int cod, int quantidade, int idArmazem )
    {

        String sql = "UPDATE tb_stock SET quantidade_existente =  " + ( getQuantidadeProduto( cod ) - quantidade ) + " WHERE cod_produto_codigo = " + cod + " AND cod_armazem = " + idArmazem;

        conexao.executeUpdate( sql );

    }

}
