/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package visao;


import java.sql.Connection;
//import comercial.ProdutoItemVisao;
import controller.TipoClienteController;
import comercial.controller.VendasController;
import dao.CaixaDao;
import dao.ItemPedidosDao;
import dao.MesasDao;
import dao.MoedaDao;
import dao.PedidoDao;
import dao.PrecoDao;
import dao.ProductoDao;
import dao.ProductosDao;
import dao.ProdutoDao;
import dao.StockDao;
import entity.*;
import comercial.controller.AnoEconomicoController;
import comercial.controller.ArmazensAccessoController;
import comercial.controller.ArmazensController;
import comercial.controller.CaixasController;
import comercial.controller.CambiosController;
import comercial.controller.ClientesController;
import comercial.controller.DadosInstituicaoController;
import comercial.controller.DescontosController;
import comercial.controller.DocumentosController;
import comercial.controller.FamiliasController;
import comercial.controller.FormaPagamentoController;
import comercial.controller.FormaPagamentoItemController;
import comercial.controller.GruposController;
import comercial.controller.ItemVendasController;
import comercial.controller.LocalController;
import comercial.controller.LugaresController;
import comercial.controller.MesasController;
import comercial.controller.MoedasController;
import comercial.controller.PrecosController;
import comercial.controller.ProdutosController;
import comercial.controller.ProdutosImpostoController;
import comercial.controller.ProdutosIsentoController;
import comercial.controller.StoksController;
import comercial.controller.TipoProdutosController;
import comercial.controller.UnidadesController;
import comercial.controller.UsuariosController;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import static kitanda.util.CfConstantes.YYYYMMDD_HHMMSS;
import kitanda.util.CfMethods;
import kitanda.util.CfMethodsSwing;
import lista.Cozinha;
import lista.ListaPedidos;
import lista.ListaVendaPorMesas;
import lista.ListaVendasMesas;
import modelo.ProdutoModelo;
import org.netbeans.lib.awtextra.AbsoluteLayout;
import tesouraria.novo.controller.ContaController;
import tesouraria.novo.controller.ContaMovimentosController;
import tesouraria.novo.util.MetodosUtilTS;
import util.BDConexao;
import util.DVML;
import util.JPAEntityMannagerFactoryUtil;
import util.MetodosUtil;
import static util.DVML.*;
import util.FinanceUtils;
import static util.MetodosUtil.rodarComandoWindows;
import static visao.PrincipalPedidosVisao.procedimento_mesas_livre;

/**
 *
 * @author Domingos Dala Vunge & Martinho Luís
 */
public class VendaPraticaVisao extends javax.swing.JFrame
{

    private static EntityManagerFactory emf = JPAEntityMannagerFactoryUtil.em;
    private static ClientesController clientesController;
    private static FamiliasController familiaController;
    private static PrecosController precosController;
    private static LugaresController lugaresController;
    private static ProdutosController produtosController;
    private static DocumentosController documentosController;
    private static MoedasController moedasController;
    private static DescontosController descontosController;
    private static CambiosController cambiosController;
    private static UsuariosController usuariosController;
    private static DadosInstituicaoController dadosInstituicaoController;
    private static ProdutosImpostoController produtosImpostoController;
    private static ProdutosIsentoController produtosIsentoController;
    private static CaixasController caixasController;
    private static FormaPagamentoItemController formaPagamentoItemController;
    private static ArmazensAccessoController armazensAccessoController;
    private static TbDadosInstituicao dadosInstituicao;
    private static MesasController mesasController;
    private static ArmazensController armazensController;
    private static LocalController localController;
    private static UnidadesController unidadesController;
    private static AnoEconomicoController anoEconomicoController;
    private static GruposController gruposController;
    private static TipoClienteController tipoClienteController;
    private static VendasController vendasController;
    private static ItemVendasController itemVendasController;
    private static StoksController stocksController;
    private static TipoProdutosController tipoProdutosController;
//    private static Ite tipoProdutosController;
    private static TbStock stock_local;
    private TbItemPedidos itemPedidos;
    private static MesasDao mesasDao = new MesasDao(emf );
    private static PrecoDao precoDao = new PrecoDao( emf );
    private static CaixaDao caixaDao;
    private static PedidoDao pedidoDao = new PedidoDao( emf );
    private static ProdutoDao produtoDao = new ProdutoDao( emf );
    private static ItemPedidosDao itemPedidosDao = new ItemPedidosDao( emf );
    private static StockDao stockDao = new StockDao( emf );
    private static ProductoDao productoDao;
    private static String prox_doc;
    private static Documento documento;
    private DefaultListModel lista_lugares = new DefaultListModel();
    private static TbPedido pedido;
    private static TbProduto produtos;
    private static ProductosDao productosDao;
    private static int linha_acutal = 0;
    private static BDConexao conexao;
    private ProdutoModelo produtoModelo;
    private static String mesa = "";
    private static String lugar = "";
    private static int lugares = 0;
    private int pk_mesa = 0;
    private int idpedido = 0;
    private static int linha_actual = -1;
    private ArrayList<JButton> botoes_object = new ArrayList<>();
    private ArrayList<JButton> botoes_categorias = new ArrayList<>();
    private int TAMANHO_ESQUERDO = 10;
    private int TAMANHO_CENTRO = 100;
    private final int idCategoria = 0;
    private JPanel painel_central = new JPanel();
    private GridLayout gl;
    private ImageIcon imageIcon;
    private int codigo = 0;
    private static int idUser = 0;
    private static int doc_prox_cod;
    public static Abreviacao abreviacao;
    private boolean aviso_continuar_documento = false;
    private static int id_armzem;
    public static double gorjeta = 0;
    private static int GRUPO_AREA;
    private static AnoEconomico anoEconomico;
    private static FormaPagamentoController formaPagamentoController;
    private static ContaController contaController;
    private static ContaMovimentosController cmc;
    private static BDConexao conexaoTransaction;

    /**
     * Creates new form GestaoItemPedidosVisao
     */
    public VendaPraticaVisao( String mesa, String lugar, int idUser, int id_armazem, BDConexao conexao )
    {

        initComponents();
        setLocationRelativeTo( null );
        cmbMoeda.setVisible( false );
        jButton3.setVisible( false );
        jPanelLugares.setVisible( false );
        //this.setExtendedState(   this.getExtendedState()|GestaoPedidosVisao.MAXIMIZED_BOTH  );   
        this.conexao = conexao;
        this.idUser = idUser;
        this.id_armzem = id_armazem;
        this.mesa = "MESA " + mesa.trim();
        this.lugar = "LUGAR " + lugar.trim();
        txtIniciaisCliente.addKeyListener( new TratarEventoTeclado() );
        vendasController = new VendasController( VendaPraticaVisao.conexao );
        itemVendasController = new ItemVendasController( VendaPraticaVisao.conexao );
        mesasController = new MesasController( VendaPraticaVisao.conexao );
        lugaresController = new LugaresController( VendaPraticaVisao.conexao );
        produtosController = new ProdutosController( VendaPraticaVisao.conexao );
        stocksController = new StoksController( VendaPraticaVisao.conexao );
        precosController = new PrecosController( VendaPraticaVisao.conexao );
        tipoProdutosController = new TipoProdutosController( VendaPraticaVisao.conexao );
        familiaController = new FamiliasController( VendaPraticaVisao.conexao );
        armazensController = new ArmazensController( VendaPraticaVisao.conexao );
        localController = new LocalController( VendaPraticaVisao.conexao );
        unidadesController = new UnidadesController( VendaPraticaVisao.conexao );
        anoEconomicoController = new AnoEconomicoController( VendaPraticaVisao.conexao );
        clientesController = new ClientesController( VendaPraticaVisao.conexao );
        documentosController = new DocumentosController( VendaPraticaVisao.conexao );
        produtosImpostoController = new ProdutosImpostoController( VendaPraticaVisao.conexao );
        cambiosController = new CambiosController( VendaPraticaVisao.conexao );
        dadosInstituicaoController = new DadosInstituicaoController( VendaPraticaVisao.conexao );
        formaPagamentoItemController = new FormaPagamentoItemController( VendaPraticaVisao.conexao );
        gruposController = new GruposController( VendaPraticaVisao.conexao );
        armazensAccessoController = new ArmazensAccessoController( VendaPraticaVisao.conexao );
        usuariosController = new UsuariosController( VendaPraticaVisao.conexao );
        moedasController = new MoedasController( VendaPraticaVisao.conexao );
        formaPagamentoController = new FormaPagamentoController( VendaPraticaVisao.conexao );
        contaController = new ContaController( VendaPraticaVisao.conexao );
        usuariosController = new UsuariosController( VendaPraticaVisao.conexao );
        dadosInstituicao = ( TbDadosInstituicao ) dadosInstituicaoController.findById( 1 );
        cmc = new ContaMovimentosController( conexao );

        rbArmazem1.setVisible( false );
        rbArmazem.setVisible( false );
        caixaDao = new CaixaDao( emf );
        txtMesa.setText( this.mesa );
        lbValorPorExtenco.setText( "" );

        cmbTipoDocumento.setModel( new DefaultComboBoxModel( documentosController.getVectorMesas() ) );

        cmbAnoEconomico.setModel( new DefaultComboBoxModel( anoEconomicoController.getVector() ) );
        cmbMoeda.setModel( new DefaultComboBoxModel( moedasController.getVector() ) );
        cmbCliente.setModel( new DefaultComboBoxModel( clientesController.getVector() ) );
        cmbCliente.setSelectedItem( DVML._CLIENTE_CONSUMIDOR_FINAL );

        try
        {
            setDocpadrao( dadosInstituicao.getDocpadrao() );
            setFolhaImpressora( dadosInstituicao.getImpressora() );

            int numero_copia = dadosInstituicao.getNumeroVias();
            spnCopia.setModel( CfMethodsSwing.criarSpinnerDoubleModel( 1, 3, numero_copia ) );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }

        try
        {
            jPanel1.setLayout( new AbsoluteLayout() );
            adicionar_catgorias();
            setSalvarPedidos();
//            actualizar();
//            adicionar_lugares();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
        dc_data_documento.setDate( new Date() );
        jPanel4.setVisible( true );
        jPanelLugares.setVisible( false );
        cmbArmazem.setVisible( false );
        cmbMoeda.setVisible( false );

//        mostrar_proximo_codigo_documento();
//        verificarCaixa();
        setWindowsListener();

        try
        {
            configurar_armazens();
//            setDesactivarLugares(dadosInstituicao.getDesactivarLugares() );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
        setArmazem( dadosInstituicao.getConfigArmazens() );

        visualizarCheckbox();

        desactivarLugares();
    }

    private void setArmazem( String armazem )
    {
        if ( armazem.equalsIgnoreCase( "Multi_armazem" ) )
        {
            rbArmazem.setSelected( true );

        }
        else
        {
            rbArmazem1.setSelected( true );

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
                cmbArmazem.setModel( new DefaultComboBoxModel( armazensAccessoController.getAllArmazemExceptoEconomatoByIdUSuario( idUser ) ) );
            }
        }
        catch ( Exception e )
        {
            e.printStackTrace();
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
                    System.out.println( "NOME VOLTAR " + prefixo );
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
        buttonGroup2 = new javax.swing.ButtonGroup();
        buttonGroup3 = new javax.swing.ButtonGroup();
        buttonGroup4 = new javax.swing.ButtonGroup();
        buttonGroup5 = new javax.swing.ButtonGroup();
        txtMesa = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        btn_voltar = new javax.swing.JButton();
        designacao_categoria = new javax.swing.JLabel();
        rbTicketSimples = new javax.swing.JRadioButton();
        rbTicketGeral = new javax.swing.JRadioButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jPanelLugares = new javax.swing.JPanel();
        btn_01 = new javax.swing.JButton();
        btn_06 = new javax.swing.JButton();
        btn_07 = new javax.swing.JButton();
        btn_02 = new javax.swing.JButton();
        btn_08 = new javax.swing.JButton();
        btn_03 = new javax.swing.JButton();
        btn_09 = new javax.swing.JButton();
        btn_04 = new javax.swing.JButton();
        btn_05 = new javax.swing.JButton();
        btn_10 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtTotalApagar = new javax.swing.JTextField();
        cmbCliente = new javax.swing.JComboBox<>();
        txtNomeConsumidorFinal = new javax.swing.JTextField();
        lbClienteConsumidorFinal = new javax.swing.JLabel();
        btCliente = new javax.swing.JButton();
        lbClienteConsumidorFinal1 = new javax.swing.JLabel();
        txtIniciaisCliente = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        BT_Conversao = new javax.swing.JButton();
        btFT = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        lbQuantidadeExistente = new javax.swing.JLabel();
        txtQuatidadeExistente = new javax.swing.JTextField();
        status_mensagem_secundaria = new javax.swing.JLabel();
        status_mensagem_primaria = new javax.swing.JLabel();
        cmbAnoEconomico = new javax.swing.JComboBox<>();
        lb_proximo_documento = new javax.swing.JLabel();
        cmbArmazem = new javax.swing.JComboBox<>();
        lbVias = new javax.swing.JLabel();
        spnCopia = new javax.swing.JSpinner();
        rbArmazem1 = new javax.swing.JRadioButton();
        rbArmazem = new javax.swing.JRadioButton();
        jButton4 = new javax.swing.JButton();
        lbValorPorExtenco = new javax.swing.JLabel();
        cmbMoeda = new javax.swing.JComboBox();
        cmb_area_venda_restaurante = new javax.swing.JComboBox<>();
        cmbTipoDocumento = new javax.swing.JComboBox();
        dc_data_documento = new com.toedter.calendar.JDateChooser();
        ck_simplificada = new javax.swing.JCheckBox();
        ck_ComVirgula = new javax.swing.JCheckBox();
        ck_S_A6 = new javax.swing.JCheckBox();
        ck_A7 = new javax.swing.JCheckBox();
        jButton3 = new javax.swing.JButton();
        rbSim_lugar = new javax.swing.JRadioButton();
        rbNao_lugar = new javax.swing.JRadioButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("VENDA PRÁTICA POS");

        txtMesa.setEditable(false);
        txtMesa.setFont(new java.awt.Font("Tahoma", 0, 1)); // NOI18N
        txtMesa.setForeground(new java.awt.Color(204, 0, 0));
        txtMesa.setText("MESA X");
        txtMesa.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtMesaActionPerformed(evt);
            }
        });

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel4.setEnabled(false);
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jScrollPane3.setViewportView(jPanel1);

        jPanel4.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(7, 46, 620, 520));

        btn_voltar.setText("<<");
        btn_voltar.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btn_voltarActionPerformed(evt);
            }
        });
        jPanel4.add(btn_voltar, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 60, 50));

        designacao_categoria.setBackground(new java.awt.Color(255, 255, 255));
        designacao_categoria.setFont(new java.awt.Font("Lucida Grande", 3, 22)); // NOI18N
        designacao_categoria.setForeground(new java.awt.Color(204, 0, 0));
        designacao_categoria.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        designacao_categoria.setText("designacao_categoria");
        designacao_categoria.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        designacao_categoria.setOpaque(true);
        jPanel4.add(designacao_categoria, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 10, 250, -1));

        buttonGroup4.add(rbTicketSimples);
        rbTicketSimples.setSelected(true);
        rbTicketSimples.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                rbTicketSimplesActionPerformed(evt);
            }
        });
        jPanel4.add(rbTicketSimples, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 9, -1, 30));

        buttonGroup4.add(rbTicketGeral);
        rbTicketGeral.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                rbTicketGeralActionPerformed(evt);
            }
        });
        jPanel4.add(rbTicketGeral, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 10, -1, 30));

        jLabel2.setText("Ticket Geral");
        jPanel4.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 10, 90, 30));

        jLabel3.setText("Ticket Simples");
        jPanel4.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 10, 110, 30));

        jPanelLugares.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        btn_01.setFont(new java.awt.Font("Lucida Grande", 1, 12)); // NOI18N
        btn_01.setText("Lugar 01");
        btn_01.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btn_01ActionPerformed(evt);
            }
        });

        btn_06.setFont(new java.awt.Font("Lucida Grande", 1, 12)); // NOI18N
        btn_06.setText("Lugar 06");
        btn_06.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btn_06ActionPerformed(evt);
            }
        });

        btn_07.setFont(new java.awt.Font("Lucida Grande", 1, 12)); // NOI18N
        btn_07.setText("Lugar 07");
        btn_07.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btn_07ActionPerformed(evt);
            }
        });

        btn_02.setFont(new java.awt.Font("Lucida Grande", 1, 12)); // NOI18N
        btn_02.setText("Lugar 02");
        btn_02.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btn_02ActionPerformed(evt);
            }
        });

        btn_08.setFont(new java.awt.Font("Lucida Grande", 1, 12)); // NOI18N
        btn_08.setText("Lugar 08");
        btn_08.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btn_08ActionPerformed(evt);
            }
        });

        btn_03.setFont(new java.awt.Font("Lucida Grande", 1, 12)); // NOI18N
        btn_03.setText("Lugar 03");
        btn_03.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btn_03ActionPerformed(evt);
            }
        });

        btn_09.setFont(new java.awt.Font("Lucida Grande", 1, 12)); // NOI18N
        btn_09.setText("Lugar 09");
        btn_09.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btn_09ActionPerformed(evt);
            }
        });

        btn_04.setFont(new java.awt.Font("Lucida Grande", 1, 12)); // NOI18N
        btn_04.setText("Lugar 04");
        btn_04.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btn_04ActionPerformed(evt);
            }
        });

        btn_05.setFont(new java.awt.Font("Lucida Grande", 1, 12)); // NOI18N
        btn_05.setText("Lugar 05");
        btn_05.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btn_05ActionPerformed(evt);
            }
        });

        btn_10.setFont(new java.awt.Font("Lucida Grande", 1, 12)); // NOI18N
        btn_10.setText("Lugar 10");
        btn_10.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btn_10ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelLugaresLayout = new javax.swing.GroupLayout(jPanelLugares);
        jPanelLugares.setLayout(jPanelLugaresLayout);
        jPanelLugaresLayout.setHorizontalGroup(
            jPanelLugaresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelLugaresLayout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(jPanelLugaresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btn_01, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_06, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelLugaresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btn_02, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_07, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE))
                .addGap(14, 14, 14)
                .addGroup(jPanelLugaresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btn_03, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_08, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE))
                .addGap(16, 16, 16)
                .addGroup(jPanelLugaresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btn_04, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_09, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(22, 22, 22)
                .addGroup(jPanelLugaresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanelLugaresLayout.createSequentialGroup()
                        .addComponent(btn_05)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanelLugaresLayout.setVerticalGroup(
            jPanelLugaresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelLugaresLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanelLugaresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelLugaresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btn_01, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btn_02, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btn_03, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanelLugaresLayout.createSequentialGroup()
                        .addComponent(btn_04, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelLugaresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btn_09, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanelLugaresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btn_06, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btn_07, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btn_08, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(btn_10, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(btn_05, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jLabel1.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        jLabel1.setText("Valor Pagar");

        txtTotalApagar.setEditable(false);
        txtTotalApagar.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        txtTotalApagar.setForeground(new java.awt.Color(255, 51, 51));
        txtTotalApagar.setText("0");

        cmbCliente.setFont(new java.awt.Font("Lucida Grande", 1, 15)); // NOI18N
        cmbCliente.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbCliente.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbClienteActionPerformed(evt);
            }
        });

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
        lbClienteConsumidorFinal.setText("Nome:");

        btCliente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/usuario.png"))); // NOI18N
        btCliente.setText("jButton1");
        btCliente.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btClienteActionPerformed(evt);
            }
        });

        lbClienteConsumidorFinal1.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbClienteConsumidorFinal1.setText("Pesquisar Clientes:");

        txtIniciaisCliente.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtIniciaisClienteActionPerformed(evt);
            }
        });
        txtIniciaisCliente.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtIniciaisClienteKeyPressed(evt);
            }
        });

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/LOGOUT - VERMELHO/Logout 32x32.png"))); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtTotalApagar, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(cmbCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 362, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                                .addComponent(lbClienteConsumidorFinal, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(txtNomeConsumidorFinal, javax.swing.GroupLayout.PREFERRED_SIZE, 263, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(lbClienteConsumidorFinal1, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtIniciaisCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbClienteConsumidorFinal1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtIniciaisCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                .addGap(9, 9, 9)
                .addComponent(cmbCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbClienteConsumidorFinal, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNomeConsumidorFinal, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtTotalApagar, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jButton2))
                .addGap(22, 22, 22))
        );

        BT_Conversao.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        BT_Conversao.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/1323444801_currency_dollar red.png"))); // NOI18N
        BT_Conversao.setText("Facturar");
        BT_Conversao.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                BT_ConversaoActionPerformed(evt);
            }
        });

        btFT.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        btFT.setText("Processar");
        btFT.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btFTActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(BT_Conversao, javax.swing.GroupLayout.DEFAULT_SIZE, 196, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btFT, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(17, 17, 17))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btFT, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(BT_Conversao)))
                .addContainerGap())
        );

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "ID", ".", "Produto", "QTD", "Total"
            }
        )
        {
            boolean[] canEdit = new boolean []
            {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex)
            {
                return canEdit [columnIndex];
            }
        });
        jTable1.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                jTable1MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt)
            {
                jTable1MouseEntered(evt);
            }
        });
        jTable1.addPropertyChangeListener(new java.beans.PropertyChangeListener()
        {
            public void propertyChange(java.beans.PropertyChangeEvent evt)
            {
                jTable1PropertyChange(evt);
            }
        });
        jScrollPane2.setViewportView(jTable1);
        if (jTable1.getColumnModel().getColumnCount() > 0)
        {
            jTable1.getColumnModel().getColumn(0).setPreferredWidth(35);
            jTable1.getColumnModel().getColumn(1).setPreferredWidth(1);
            jTable1.getColumnModel().getColumn(2).setPreferredWidth(260);
            jTable1.getColumnModel().getColumn(3).setPreferredWidth(40);
            jTable1.getColumnModel().getColumn(4).setPreferredWidth(80);
        }

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 405, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 10, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 335, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        lbQuantidadeExistente.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lbQuantidadeExistente.setText("QTD Stock :");

        txtQuatidadeExistente.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        txtQuatidadeExistente.setEnabled(false);

        status_mensagem_secundaria.setFont(new java.awt.Font("Lucida Grande", 1, 12)); // NOI18N
        status_mensagem_secundaria.setText("...");

        status_mensagem_primaria.setFont(new java.awt.Font("Lucida Grande", 1, 12)); // NOI18N
        status_mensagem_primaria.setForeground(new java.awt.Color(204, 153, 0));
        status_mensagem_primaria.setText("sahdbvhsdva");

        cmbAnoEconomico.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbAnoEconomico.setEnabled(false);
        cmbAnoEconomico.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbAnoEconomicoActionPerformed(evt);
            }
        });

        lb_proximo_documento.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        lb_proximo_documento.setText("PRÓX. DOC. : XX PP/A1");

        cmbArmazem.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbArmazem.setEnabled(false);
        cmbArmazem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbArmazemActionPerformed(evt);
            }
        });

        lbVias.setFont(new java.awt.Font("Lucida Grande", 1, 12)); // NOI18N
        lbVias.setText("Via(s):");

        buttonGroup2.add(rbArmazem1);
        rbArmazem1.setEnabled(false);
        rbArmazem1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                rbArmazem1ActionPerformed(evt);
            }
        });

        buttonGroup2.add(rbArmazem);
        rbArmazem.setEnabled(false);
        rbArmazem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                rbArmazemActionPerformed(evt);
            }
        });

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/proucura.png"))); // NOI18N
        jButton4.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButton4ActionPerformed(evt);
            }
        });

        lbValorPorExtenco.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbValorPorExtenco.setForeground(new java.awt.Color(204, 0, 0));
        lbValorPorExtenco.setText("VALOR POR EXTENSO");

        cmbMoeda.setBackground(new java.awt.Color(0, 51, 102));
        cmbMoeda.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 14)); // NOI18N
        cmbMoeda.setEnabled(false);
        cmbMoeda.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbMoedaActionPerformed(evt);
            }
        });

        cmb_area_venda_restaurante.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cmb_area_venda_restaurante.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Restaurante", "Alojamento" }));
        cmb_area_venda_restaurante.setEnabled(false);

        cmbTipoDocumento.setBackground(new java.awt.Color(0, 51, 102));
        cmbTipoDocumento.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        cmbTipoDocumento.setForeground(new java.awt.Color(255, 255, 255));
        cmbTipoDocumento.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbTipoDocumentoActionPerformed(evt);
            }
        });

        dc_data_documento.setFont(new java.awt.Font("Lucida Grande", 1, 12)); // NOI18N

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

        buttonGroup3.add(ck_A7);
        ck_A7.setFont(new java.awt.Font("Lucida Grande", 0, 10)); // NOI18N
        ck_A7.setSelected(true);
        ck_A7.setText("A7");
        ck_A7.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ck_A7ActionPerformed(evt);
            }
        });

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/actualizar_1.png"))); // NOI18N
        jButton3.setText("Enviar Ticket");
        jButton3.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButton3ActionPerformed(evt);
            }
        });

        buttonGroup5.add(rbSim_lugar);
        rbSim_lugar.setText("Sim");
        rbSim_lugar.setEnabled(false);
        rbSim_lugar.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                rbSim_lugarActionPerformed(evt);
            }
        });

        buttonGroup5.add(rbNao_lugar);
        rbNao_lugar.setSelected(true);
        rbNao_lugar.setText("Nao");
        rbNao_lugar.setEnabled(false);
        rbNao_lugar.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                rbNao_lugarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lbValorPorExtenco, javax.swing.GroupLayout.PREFERRED_SIZE, 575, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtMesa, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(rbSim_lugar)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(rbNao_lugar)))
                        .addGap(13, 13, 13)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jPanelLugares, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(21, 21, 21)
                                .addComponent(cmb_area_venda_restaurante, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cmbAnoEconomico, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(16, 16, 16)
                                .addComponent(ck_simplificada)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ck_ComVirgula, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ck_S_A6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(ck_A7)
                                .addGap(33, 33, 33)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(cmbTipoDocumento, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(dc_data_documento, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(6, 212, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(rbArmazem)
                                    .addComponent(rbArmazem1))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jButton3))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(24, 24, 24)
                                        .addComponent(cmbArmazem, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(cmbMoeda, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap(162, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lbVias, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(spnCopia, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(status_mensagem_primaria, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(status_mensagem_secundaria, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(138, 138, 138))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lb_proximo_documento, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lbQuantidadeExistente)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtQuatidadeExistente, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(ck_simplificada)
                            .addComponent(ck_ComVirgula)
                            .addComponent(ck_S_A6)
                            .addComponent(ck_A7)
                            .addComponent(cmbAnoEconomico, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmb_area_venda_restaurante, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtMesa, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanelLugares, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(43, 43, 43)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(rbSim_lugar)
                                    .addComponent(rbNao_lugar)))))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtQuatidadeExistente, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(lbQuantidadeExistente, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lb_proximo_documento, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(lbVias, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(spnCopia, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(10, 10, 10))
                                    .addComponent(jButton4, javax.swing.GroupLayout.Alignment.TRAILING))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 8, Short.MAX_VALUE)
                                .addComponent(cmbMoeda, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(35, 35, 35))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addComponent(rbArmazem1, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(rbArmazem)
                                        .addGap(13, 13, 13))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addComponent(cmbArmazem, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jButton3))
                                    .addComponent(cmbTipoDocumento, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(dc_data_documento, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(status_mensagem_primaria)
                            .addComponent(status_mensagem_secundaria)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 576, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lbValorPorExtenco)))
                .addGap(11, 11, 11))
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {cmbAnoEconomico, cmbArmazem});

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public static int getCodigoProduto() throws SQLException
    {
        return 0;
    }

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        dispose();
    }//GEN-LAST:event_jButton2ActionPerformed


    private void BT_ConversaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BT_ConversaoActionPerformed

        if ( true )
        {
            if ( MetodosUtil.licencaValidada( conexao ) )
            {

                if ( cmbTipoDocumento.getSelectedIndex() == 0 )
                {
                    JOptionPane.showMessageDialog( null, "Atenção\nSelecione o tipo de documento!" );
                }
                else
                {
                    if ( validar_cliente() )
                    {
                        procedimento_processar_facturacao();
                    }

                }

            }

        }

//       

    }//GEN-LAST:event_BT_ConversaoActionPerformed

    public boolean validar_cliente()
    {
        if ( cmbCliente.getSelectedItem().equals( "--Seleccione o Cliente--" ) )
        {

            JOptionPane.showMessageDialog( null, "Por favor, Seleccione ou Digite o nome do Cliente!" );
            txtIniciaisCliente.requestFocus();
            return false;

//        } else if ( dc_data_vencimento.getSelectedItem().equals( "--Seleccione--" ) )
        }

        return true;
    }

    private void procedimento_processar_facturacao()
    {

        if ( verifica_ano_documento_igual_economico() )
        {
            if ( data_documento_superior_ou_igual_ao_ultimo_doc() )
            {
                if ( true )
                {
                    int opcao = JOptionPane.showConfirmDialog( null, "Por Favor\nDeseja pagar a Mesa Completa?" );

                    if ( opcao == JOptionPane.YES_OPTION )
                    {

                        dispose();

                        new FormaPagamentoVisao( this, rootPaneCheckingEnabled, emf, DVML.VENDA_RESTAURANTE, BDConexao.getInstancia()).setVisible(true);

                    }
                    else if (opcao == JOptionPane.NO_OPTION )
                    {

                        try
                        {

                            int lugar = Integer.parseInt( JOptionPane.showInputDialog( null, "Por favor\nQual lugar deseja facturar?" ) );

                            System.out.println( "LUGAR ACTUAL1 :" + lugar );

                            System.out.println( "LUGAR ACTUAL2 :" + lugar );
                            if ( exite_pedido_lugar( lugar ) )
                            {

                                new FormaPagamentoPorLugaresVisao( this, rootPaneCheckingEnabled, emf, DVML.VENDA_RESTAURANTE, conexao, lugar, getTotalAOALiquido( lugar ) ).setVisible( true );
//                                new FormaPagamentoLugarVisao( this, rootPaneCheckingEnabled, emf, DVML.VENDA_RESTAURANTE, conexao, lugar, getTotalAOALiquido( lugar ) ).setVisible( true );

                            }
                            else
                            {

                                JOptionPane.showMessageDialog( null, "Nao existe pedido neste lugar" );
                            }

                        }
                        catch ( Exception e )
                        {
                        }
                    }
                    else
                    {
                        JOptionPane.showMessageDialog( null, "Operacao cancelada" );
                    }

                }
                else
                {
                    JOptionPane.showMessageDialog( null, "O documento não pode ser processado porque possui uma data inferior ao úlimo documento efectuado", "AVISO", JOptionPane.WARNING_MESSAGE );
                }
            }
        }
        else
        {
            JOptionPane.showMessageDialog( null, "A data do documento a ser emitido deve estar no intervalo do ano economico", "Aviso", JOptionPane.WARNING_MESSAGE );
        }

    }

    public static void procedimento_converter_factura()
    {

        if ( true )
        {
            TbVenda salvar_venda = salvar_venda();

            if ( salvar_venda != null )
            {
                System.err.println( "VENDA AQUI " + salvar_venda.getCodigo() );
                registrar_forma_pagamento( salvar_venda.getCodigo() );
                salvarItemvenda( salvar_venda );
                remover_dados_tabela();

            }
            else
            {
                System.err.println( "  erro ao salvar venda!!!" );
            }
        }

    }

    public static void procedimento_converter_factura_FT()
    {

        if ( true )
        {
            TbVenda salvar_venda = salvar_venda();

            if ( salvar_venda != null )
            {
                System.err.println( "VENDA AQUI " + salvar_venda.getCodigo() );
//                registrar_forma_pagamento( salvar_venda.getCodigo() );
                salvarItemvenda( salvar_venda );
                remover_dados_tabela();

            }
            else
            {
                System.err.println( "  erro ao salvar venda!!!" );
            }
        }

    }

    public static void procedimento_processar_factura_consulta()
    {

        if ( true )
        {
            TbVenda salvar_venda = salvar_venda();

            if ( salvar_venda != null )
            {
                System.err.println( "VENDA AQUI " + salvar_venda.getCodigo() );
                salvarItemvenda( salvar_venda );
                remover_dados_tabela();

            }
            else
            {
                System.err.println( "  erro ao salvar venda!!!" );
            }
        }

    }


    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        // TODO add your handling code here:

        if ( evt.getClickCount() >= 2 )
        {
            procedimento_eliminar_item_pedido();
        }

    }//GEN-LAST:event_jTable1MouseClicked

    private void cmbClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbClienteActionPerformed
        mostra_consumidor_final();
    }//GEN-LAST:event_cmbClienteActionPerformed

    private void jTable1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_jTable1MouseEntered

    private void cmbAnoEconomicoActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbAnoEconomicoActionPerformed
    {//GEN-HEADEREND:event_cmbAnoEconomicoActionPerformed
        // TODO add your handling code here:
        mostrar_proximo_codigo_documento();
        actualizar_abreviacao();
    }//GEN-LAST:event_cmbAnoEconomicoActionPerformed

    private void txtNomeConsumidorFinalActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtNomeConsumidorFinalActionPerformed
    {//GEN-HEADEREND:event_txtNomeConsumidorFinalActionPerformed

    }//GEN-LAST:event_txtNomeConsumidorFinalActionPerformed

    private void txtNomeConsumidorFinalKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtNomeConsumidorFinalKeyPressed
    {//GEN-HEADEREND:event_txtNomeConsumidorFinalKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNomeConsumidorFinalKeyPressed

    private void txtMesaActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtMesaActionPerformed
    {//GEN-HEADEREND:event_txtMesaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMesaActionPerformed

    private void btn_10ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btn_10ActionPerformed
    {//GEN-HEADEREND:event_btn_10ActionPerformed
        // TODO add your handling code here:
        activar_button_lugar( true, true, true, true, true, true, true, true, true, false );
    }//GEN-LAST:event_btn_10ActionPerformed

    private void btn_05ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btn_05ActionPerformed
    {//GEN-HEADEREND:event_btn_05ActionPerformed
        // TODO add your handling code here:
        activar_button_lugar( true, true, true, true, false, true, true, true, true, true );
    }//GEN-LAST:event_btn_05ActionPerformed

    private void btn_04ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btn_04ActionPerformed
    {//GEN-HEADEREND:event_btn_04ActionPerformed
        // TODO add your handling code here:
        activar_button_lugar( true, true, true, false, true, true, true, true, true, true );
    }//GEN-LAST:event_btn_04ActionPerformed

    private void btn_09ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btn_09ActionPerformed
    {//GEN-HEADEREND:event_btn_09ActionPerformed
        // TODO add your handling code here:
        activar_button_lugar( true, true, true, true, true, true, true, true, false, true );
    }//GEN-LAST:event_btn_09ActionPerformed

    private void btn_03ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btn_03ActionPerformed
    {//GEN-HEADEREND:event_btn_03ActionPerformed
        // TODO add your handling code here:
        activar_button_lugar( true, true, false, true, true, true, true, true, true, true );
    }//GEN-LAST:event_btn_03ActionPerformed

    private void btn_08ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btn_08ActionPerformed
    {//GEN-HEADEREND:event_btn_08ActionPerformed
        // TODO add your handling code here:
        activar_button_lugar( true, true, true, true, true, true, true, false, true, true );
    }//GEN-LAST:event_btn_08ActionPerformed

    private void btn_02ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btn_02ActionPerformed
    {//GEN-HEADEREND:event_btn_02ActionPerformed
        // TODO add your handling code here:
        activar_button_lugar( true, false, true, true, true, true, true, true, true, true );
    }//GEN-LAST:event_btn_02ActionPerformed

    private void btn_07ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btn_07ActionPerformed
    {//GEN-HEADEREND:event_btn_07ActionPerformed
        // TODO add your handling code here:
        activar_button_lugar( true, true, true, true, true, true, false, true, true, true );
    }//GEN-LAST:event_btn_07ActionPerformed

    private void btn_06ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btn_06ActionPerformed
    {//GEN-HEADEREND:event_btn_06ActionPerformed
        // TODO add your handling code here:
        activar_button_lugar( true, true, true, true, true, false, true, true, true, true );
    }//GEN-LAST:event_btn_06ActionPerformed

    private void btn_01ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btn_01ActionPerformed
    {//GEN-HEADEREND:event_btn_01ActionPerformed
        // TODO add your handling code here:
        activar_button_lugar( false, true, true, true, true, true, true, true, true, true );
    }//GEN-LAST:event_btn_01ActionPerformed

    private void cmbArmazemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbArmazemActionPerformed
    {//GEN-HEADEREND:event_cmbArmazemActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbArmazemActionPerformed

    private void rbArmazem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbArmazem1ActionPerformed
        try
        {
            configurar_armazens();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }//GEN-LAST:event_rbArmazem1ActionPerformed

    private void rbArmazemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbArmazemActionPerformed
        try
        {
            configurar_armazens();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }//GEN-LAST:event_rbArmazemActionPerformed

    private void ck_simplificadaActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_ck_simplificadaActionPerformed
    {//GEN-HEADEREND:event_ck_simplificadaActionPerformed
        // TODO add your handling code here:
        actualizar_abreviacao();
    }//GEN-LAST:event_ck_simplificadaActionPerformed

    private void ck_ComVirgulaActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_ck_ComVirgulaActionPerformed
    {//GEN-HEADEREND:event_ck_ComVirgulaActionPerformed
        actualizar_abreviacao();
    }//GEN-LAST:event_ck_ComVirgulaActionPerformed

    private void ck_S_A6ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_ck_S_A6ActionPerformed
    {//GEN-HEADEREND:event_ck_S_A6ActionPerformed
        actualizar_abreviacao();
    }//GEN-LAST:event_ck_S_A6ActionPerformed

    private void btn_voltarActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btn_voltarActionPerformed
    {//GEN-HEADEREND:event_btn_voltarActionPerformed
        // TODO add your handling code here:
        adicionar_catgorias();
    }//GEN-LAST:event_btn_voltarActionPerformed

    private void ck_A7ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_ck_A7ActionPerformed
    {//GEN-HEADEREND:event_ck_A7ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ck_A7ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton4ActionPerformed
    {//GEN-HEADEREND:event_jButton4ActionPerformed
        try
        {
//            if ( rbNao_lugar.isSelected() && activo_um_lugar() )
//            {
                new BuscaProdutoVisao( this, rootPaneCheckingEnabled, getCodigoArmazem(), DVML.JANELA_RETAURANTE, BDConexao.getInstancia()).setVisible(true);
//            }
//            else
//            {
//
//                new BuscaProdutoVisao(this, rootPaneCheckingEnabled, getCodigoArmazem(), DVML.JANELA_RETAURANTE, BDConexao.getInstancia()).setVisible(true);
//            }
        }
        catch (Exception e )
        {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jTable1PropertyChange(java.beans.PropertyChangeEvent evt)//GEN-FIRST:event_jTable1PropertyChange
    {//GEN-HEADEREND:event_jTable1PropertyChange
        // TODO add your handling code here:

        if ( jTable1.getSelectedColumn() == 3 )
        {
            System.out.println( "change" );
            actualizarQtdTable();
        }

    }//GEN-LAST:event_jTable1PropertyChange

    private void cmbMoedaActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbMoedaActionPerformed
    {//GEN-HEADEREND:event_cmbMoedaActionPerformed
        // TODO add your handling code here:
        actualizar_moeda();
    }//GEN-LAST:event_cmbMoedaActionPerformed

    private void cmbTipoDocumentoActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbTipoDocumentoActionPerformed
    {//GEN-HEADEREND:event_cmbTipoDocumentoActionPerformed
        // TODO add your handling code here:
        mostrar_proximo_codigo_documento();
        actualizar_abreviacao();
        selecionar_documento();
        atualizarCliente1();
    }//GEN-LAST:event_cmbTipoDocumentoActionPerformed

    private void btClienteActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btClienteActionPerformed
    {//GEN-HEADEREND:event_btClienteActionPerformed
        new ClienteVisao( this, rootPaneCheckingEnabled, BDConexao.getInstancia()).setVisible(true);
    }//GEN-LAST:event_btClienteActionPerformed

    private void txtIniciaisClienteActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtIniciaisClienteActionPerformed
    {//GEN-HEADEREND:event_txtIniciaisClienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIniciaisClienteActionPerformed

    private void txtIniciaisClienteKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtIniciaisClienteKeyPressed
    {//GEN-HEADEREND:event_txtIniciaisClienteKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIniciaisClienteKeyPressed

    private void rbTicketGeralActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_rbTicketGeralActionPerformed
    {//GEN-HEADEREND:event_rbTicketGeralActionPerformed
        jButton3.setVisible( true );
    }//GEN-LAST:event_rbTicketGeralActionPerformed

    private void rbTicketSimplesActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_rbTicketSimplesActionPerformed
    {//GEN-HEADEREND:event_rbTicketSimplesActionPerformed
        jButton3.setVisible( false );
    }//GEN-LAST:event_rbTicketSimplesActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton3ActionPerformed
    {//GEN-HEADEREND:event_jButton3ActionPerformed
        processar_ticket_geral();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void rbSim_lugarActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_rbSim_lugarActionPerformed
    {//GEN-HEADEREND:event_rbSim_lugarActionPerformed
        jPanelLugares.setVisible( false );
    }//GEN-LAST:event_rbSim_lugarActionPerformed

    private void rbNao_lugarActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_rbNao_lugarActionPerformed
    {//GEN-HEADEREND:event_rbNao_lugarActionPerformed
        jPanelLugares.setVisible( true );
    }//GEN-LAST:event_rbNao_lugarActionPerformed

    private void btFTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btFTActionPerformed

        if ( validar() )
        {
            if ( MetodosUtil.licencaValidada( conexao ) )
            {
                procedimento_converter_factura_FT();
                dispose();
//                procedimento_salvar();
            }

        }
    }//GEN-LAST:event_btFTActionPerformed

    private void atualizarCliente1()
    {
        boolean documentoIsFA = DVML.DOC_FACTURA_FT == getIdDocumento();
        boolean documentoIsPP = DVML.DOC_FACTURA_PROFORMA_PP == getIdDocumento();
//        boolean documentoIsGT = DVML.DOC_GUIA_TRANSPORTE_GT == getIdDocumento();

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

    private void actualizar_moeda()
    {
        CfMethods.MOEDA = getMoeda().getAbreviacao();
    }

    private static void actualizar_moeda( String moeda )
    {
        CfMethods.MOEDA = moeda;
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
    }

    public boolean validar()
    {
        if ( cmbCliente.getSelectedItem().equals( "--Seleccione o Cliente--" ) )
        {

            JOptionPane.showMessageDialog( null, "Por favor, Seleccione ou Digite o nome do Cliente!" );
            txtIniciaisCliente.requestFocus();
            txtIniciaisCliente.setBackground( Color.YELLOW );
            return false;

        }

        txtIniciaisCliente.setBackground( Color.WHITE );
        return true;
    }

//    private void setDocpadrao1( String desactivar_lugares )
//    {
//        System.out.println( "Desactivar Lugares: " + desactivar_lugares );
//        if ( desactivar_lugares.equalsIgnoreCase( "Sim" ) )
//        {
//            jCheckBox1.setSelected( true );
//            rbNao_lugar.setSelected( false );
//            rbSim_lugar.setSelected( true );
//
//        }
//        else
//        {
//            jCheckBox1.setSelected( false );
//            rbSim_lugar.setSelected( false );
//            rbNao_lugar.setSelected( true );
//
//        }
//    }
    private void actualizar_abreviacao()
    {

        switch ( getIdDocumento() )
        {
            case DVML.DOC_FACTURA_RECIBO_FR:

                if ( ck_simplificada.isSelected() )
                {
                    this.abreviacao = Abreviacao.FR_A6;
                }
                else if ( ck_S_A6.isSelected() )
                {
                    this.abreviacao = Abreviacao.FR_S_A6;
                }
                else if ( ck_ComVirgula.isSelected() )
                {
                    this.abreviacao = Abreviacao.FR_A6_Com_Virgula;
                }
                break;
            case DVML.DOC_FACTURA_FT:
                this.abreviacao = Abreviacao.FA;
                break;
            case DVML.DOC_FACTURA_CONSULTA_MESA:
                this.abreviacao = Abreviacao.CM;
                break;
            case DVML.DOC_FACTURA_PROFORMA_PP:
                this.abreviacao = Abreviacao.PP;
                break;
            default:
                break;
        }

    }

    public int getCodigoTipoProduto() throws SQLException
    {
        return 0;
    }

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
                if ( "Skin".equals( info.getName() ) )
                {
                    javax.swing.UIManager.setLookAndFeel( info.getClassName() );
                    break;
                }
            }
        }
        catch ( ClassNotFoundException ex )
        {
            java.util.logging.Logger.getLogger(VendaPraticaVisao.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        catch ( InstantiationException ex )
        {
            java.util.logging.Logger.getLogger(VendaPraticaVisao.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        catch ( IllegalAccessException ex )
        {
            java.util.logging.Logger.getLogger(VendaPraticaVisao.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        catch ( javax.swing.UnsupportedLookAndFeelException ex )
        {
            java.util.logging.Logger.getLogger(VendaPraticaVisao.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
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

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                new VendaPraticaVisao( "Mesa 2", "Lugar 2", 1, 1, BDConexao.getInstancia() ).setVisible( true );
            }
        } );
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private static javax.swing.JButton BT_Conversao;
    private javax.swing.JButton btCliente;
    private javax.swing.JButton btFT;
    private static javax.swing.JButton btn_01;
    private static javax.swing.JButton btn_02;
    private static javax.swing.JButton btn_03;
    private static javax.swing.JButton btn_04;
    private static javax.swing.JButton btn_05;
    private static javax.swing.JButton btn_06;
    private static javax.swing.JButton btn_07;
    private static javax.swing.JButton btn_08;
    private static javax.swing.JButton btn_09;
    private static javax.swing.JButton btn_10;
    private javax.swing.JButton btn_voltar;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.ButtonGroup buttonGroup3;
    private javax.swing.ButtonGroup buttonGroup4;
    private javax.swing.ButtonGroup buttonGroup5;
    public static javax.swing.JCheckBox ck_A7;
    public static javax.swing.JCheckBox ck_ComVirgula;
    public static javax.swing.JCheckBox ck_S_A6;
    public static javax.swing.JCheckBox ck_simplificada;
    private static javax.swing.JComboBox<String> cmbAnoEconomico;
    private static javax.swing.JComboBox<String> cmbArmazem;
    public static javax.swing.JComboBox<String> cmbCliente;
    public static javax.swing.JComboBox cmbMoeda;
    public static javax.swing.JComboBox cmbTipoDocumento;
    public static javax.swing.JComboBox<String> cmb_area_venda_restaurante;
    private static com.toedter.calendar.JDateChooser dc_data_documento;
    private javax.swing.JLabel designacao_categoria;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanelLugares;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private static javax.swing.JTable jTable1;
    private javax.swing.JLabel lbClienteConsumidorFinal;
    private javax.swing.JLabel lbClienteConsumidorFinal1;
    private static javax.swing.JLabel lbQuantidadeExistente;
    public static javax.swing.JLabel lbValorPorExtenco;
    private javax.swing.JLabel lbVias;
    private static javax.swing.JLabel lb_proximo_documento;
    private static javax.swing.JRadioButton rbArmazem;
    private static javax.swing.JRadioButton rbArmazem1;
    public static javax.swing.JRadioButton rbNao_lugar;
    public static javax.swing.JRadioButton rbSim_lugar;
    private static javax.swing.JRadioButton rbTicketGeral;
    private static javax.swing.JRadioButton rbTicketSimples;
    private static javax.swing.JSpinner spnCopia;
    public static javax.swing.JLabel status_mensagem_primaria;
    public static javax.swing.JLabel status_mensagem_secundaria;
    private static javax.swing.JTextField txtIniciaisCliente;
    private javax.swing.JTextField txtMesa;
    private static javax.swing.JTextField txtNomeConsumidorFinal;
    private static javax.swing.JTextField txtQuatidadeExistente;
    public static javax.swing.JTextField txtTotalApagar;
    // End of variables declaration//GEN-END:variables

    private void adicionar_lugares()
    {

        try
        {
            List<TbLugares> itens = lugaresController.listarTodos();
            lista_lugares.clear();
            for ( int i = 0; i < itens.size(); i++ )
            {
                lista_lugares.addElement( itens.get( i ).getDesignacao() );
            }
        }
        catch ( Exception e )
        {
        }
    }

    public static void procedimento_salvar_pedidos_iten_pedidos_lugar_desactivo( String designacao_produto )
    {

//        if ( stock_local.getCodigo() != 0  )
//        {
        try
        {

            /* MOSTRA A QUANTIDADE NO STOCK */
            int codigo_produto = produtoDao.getIdByDescricao( designacao_produto );

            TbProduto produto_local = produtoDao.findTbProduto( codigo_produto );
//            TbStock stock = stockDao.getStockByDescricao( codigo_produto, id_armzem );

//            if ( stock.getCodigo() != 0)
//            {
            // txtQuatidadeExistente.setText( String.valueOf(  stock.getQuantidadeExistente() ) );           
//            if ( stock.getCodProdutoCodigo().getStocavel().equals( "true" ) )
            if ( produto_local.getStocavel().equals( "true" ) && produto_local.getCodTipoProduto().getFkFamilia().getPkFamilia() == DVML.COD_PRODUTO )
            {
                lbQuantidadeExistente.setVisible( true );
                txtQuatidadeExistente.setVisible( true );

//            txtQuatidadeExistente.setText( String.valueOf( stockDao.get_stock_by_id_produto_and_id_armazem( getCodigoProduto(), getCodigoArmazem() ).getQuantidadeExistente() ) );
                txtQuatidadeExistente.setText( String.valueOf( conexao.getQtdExistenteStock( codigo_produto, getCodigoArmazem() ) ) );
            }
            else
            {
                lbQuantidadeExistente.setVisible( false );
                txtQuatidadeExistente.setVisible( false );
                txtQuatidadeExistente.setText( "" );
            }

            visualizarQtdStock();
            TbItemPedidos itemPedidosLocal = new TbItemPedidos();
            int cod_pedido = ( pedidoDao.getLastPedidoByDefignacaoMesaFALSE( mesa ) );
            pedido = pedidoDao.findTbPedido( cod_pedido );

            if ( rbSim_lugar.isSelected() )
            {

                itemPedidosLocal.setFkLugares( ( TbLugares ) lugaresController.findByLugar( String.valueOf( DVML.LUGAR_BALCAO ) ) );

            }
            else
            {

                itemPedidosLocal.setFkLugares( ( TbLugares ) lugaresController.findByLugar( getDescricaoLugar() ) );

            }

            itemPedidosLocal.setFkProdutos( produtoDao.findTbProduto( produtoDao.getIdByDescricao( designacao_produto ) ) );
            itemPedidosLocal.setQtd( 1 );
            itemPedidosLocal.setObs( "" );
            itemPedidosLocal.setDataEntrega( new Date() );
            itemPedidosLocal.setStatusConvertido( false );
            /*Envia para a área da cozinha*/
            //para enviar  da cozinha
            itemPedidosLocal.setStatusEnviado( true );
            //para saber se o prato ja foi feito 
            itemPedidosLocal.setStatusEfectuado( false );

            double preco = precoDao.findTbPreco( precoDao.getUltimoIdPrecoByIdProduto( itemPedidosLocal.getFkProdutos().getCodigo() ) ).getPrecoVenda().doubleValue();
            double total = itemPedidosLocal.getQtd() * preco;
            itemPedidosLocal.setPreco( preco );
            itemPedidosLocal.setTotalItem( total );
            itemPedidosLocal.setFkPedidos( pedido );

            try
            {
                if ( possivel_quantidade( itemPedidosLocal.getFkProdutos().getCodigo() ) )
                {

                    // if( (conexao.getQtdSolicitados(  itemPedidos.getFkProdutos().getCodigo() ) + 1 ) <=  conexao.getQtdExistenteStock(codigo_produto, 1)  ){
                    if ( produto_local.getCodTipoProduto().getFkFamilia().getPkFamilia() == DVML.COD_PRODUTO )
                    {
                        if ( estado_critico( itemPedidosLocal.getFkProdutos().getCodigo() ) )
                        {
                            JOptionPane.showMessageDialog( null, "O produto: " + designacao_produto + " precisa de ser actualizado no stock", "AVISO", JOptionPane.WARNING_MESSAGE );
                        }

                    }

//#ped2z
                    Integer idLastItemPedido = itemPedidosDao.criarComProcedimento( itemPedidosLocal, conexao );

                    if ( idLastItemPedido != null )
                    {
                        //##PINTAR
                        PrincipalPedidosVisao.mesas_livres( getLabelMesaByMesa() );
                        PrincipalPedidosVisao.pintar_mesas( getLabelMesaByMesa(), mesa );
                        System.out.println( "MESA A PINTAR: " + getLabelMesaByMesa().getText() );
                        System.out.println( "VALOR MESA A PINTAR: " + mesa );
                        //adiciona os produtos na tabela
                        actualizar();
                        //imprimir para a cozinha
//                        if ( itemPedidosLocal.getFkProdutos().getCodTipoProduto().getFkFamilia().getPkFamilia() == DVML.COD_SERVICO )
//                        if ( itemPedidosLocal.getFkProdutos().getCozinha().equals(DVML.ENVIAR_TICKET ) );
//                        {
//                            System.err.println( "PK_ITEM_PEDIDO_LOCAL" + idLastItemPedido );
////                      new ImprimirCozinha( itemPedidosLocal.getFkPedidos().getPkPedido(), itemPedidosLocal.getFkProdutos().getCodigo() );
////                            new Cozinha( idLastItemPedido );
//
//
//
//                            MetodosUtil.imprimir_cozinha( itemPedidosLocal.getFkProdutos(),
//                                    "Activo", itemPedidosLocal.getQtd(),
//                                    dadosInstituicaoController );
//                            
//                            
//                        }

                        int idPedido = 0;

                        TbLugares lugarEntity = ( TbLugares ) lugaresController.findById( DVML.LUGAR_BALCAO );
//                        TbLugares lugarEntity = ( TbLugares ) lugaresController.findById( itemPedidosLocal.getFkLugares().getPkLugares() );
                        String lugarLocal = lugarEntity.getDesignacao();
                        TbUsuario usuarioEntity = ( TbUsuario ) usuariosController.findById( idUser );
                        String usuario = usuarioEntity.getNome();

//            TbProduto findByDesignacao = produtosController.findByDesignacao( cmbProduto.getSelectedItem().toString() );
                        TbProduto findByDesignacao = produtosController.findByDesignacao( itemPedidosLocal.getFkProdutos().getDesignacao() );
                        if ( ( rbTicketSimples.isSelected() ) && ( findByDesignacao.getCozinha().equals( DVML.ENVIAR_TICKET ) ) )
                        {
                            MetodosUtil.imprimir_cozinha( findByDesignacao, idPedido, mesa, lugarLocal, usuario, "Activo", ( int ) itemPedidosLocal.getQtd(), dadosInstituicaoController );
                        }
                        else if ( ( rbTicketSimples.isSelected() ) && ( findByDesignacao.getCozinha().equals( DVML.ENVIAR_SALA ) ) )
                        {
                            MetodosUtil.imprimir_sala( findByDesignacao, idPedido, mesa, lugarLocal, usuario, "Activo", ( int ) itemPedidosLocal.getQtd(), dadosInstituicaoController );
                        }
                        else
                        {

                        }

                    } //                    Integer idLastPedido = itemPedidosDao.criarComProcedimento( itemPedidosLocal, conexao );
                    //
                    //                    if ( idLastPedido != null )
                    //                    {
                    //                        PrincipalPedidosVisao.mesas_livres( getLabelMesaByMesa() );
                    //                        PrincipalPedidosVisao.pintar_mesas( getLabelMesaByMesa(), this.mesa );
                    //                        //adiciona os produtos na tabela
                    //                        actualizar();
                    //
                    //                        //imprimir para a cozinha
                    //                        if ( itemPedidosLocal.getFkProdutos().getCodTipoProduto().getFkFamilia().getPkFamilia() == DVML.COD_SERVICO )
                    //                        {
                    ////                            new ImprimirCozinha( itemPedidosLocal.getFkPedidos().getPkPedido(), itemPedidosLocal.getFkProdutos().getCodigo() );
                    //                            new Cozinha( itemPedidosLocal.getFkPedidos().getPkPedido(), itemPedidosLocal.getFkProdutos().getCodigo() );
                    //                        }
                    //                    }
                    else
                    {
                        System.err.println( "ERRO AO INSERIR O ITEM ..." );
                    }

                    //  }else JOptionPane.showMessageDialog(null, "Este produto já não esta disponível.", "AVISO", JOptionPane.WARNING_MESSAGE  );
                }
                else
                {
                    JOptionPane.showMessageDialog( null, "O Produto " + designacao_produto + " não pode ser vendido para esta quantidade.", "AVISO", JOptionPane.WARNING_MESSAGE );
                }

            }
            catch ( Exception e )
            {
                e.printStackTrace();
            }

        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
//        }

    }

    public static void procedimento_salvar_pedidos_iten_pedidos( String designacao_produto )
    {

//        if ( stock_local.getCodigo() != 0  )
//        {
        try
        {

            /* MOSTRA A QUANTIDADE NO STOCK */
            int codigo_produto = produtoDao.getIdByDescricao( designacao_produto );

            TbProduto produto_local = produtoDao.findTbProduto( codigo_produto );
//            TbStock stock = stockDao.getStockByDescricao( codigo_produto, id_armzem );

//            if ( stock.getCodigo() != 0)
//            {
            // txtQuatidadeExistente.setText( String.valueOf(  stock.getQuantidadeExistente() ) );           
//            if ( stock.getCodProdutoCodigo().getStocavel().equals( "true" ) )
            if ( produto_local.getStocavel().equals( "true" ) && produto_local.getCodTipoProduto().getFkFamilia().getPkFamilia() == DVML.COD_PRODUTO )
            {
                lbQuantidadeExistente.setVisible( true );
                txtQuatidadeExistente.setVisible( true );

//            txtQuatidadeExistente.setText( String.valueOf( stockDao.get_stock_by_id_produto_and_id_armazem( getCodigoProduto(), getCodigoArmazem() ).getQuantidadeExistente() ) );
                txtQuatidadeExistente.setText( String.valueOf( conexao.getQtdExistenteStock( codigo_produto, getCodigoArmazem() ) ) );
            }
            else
            {
                lbQuantidadeExistente.setVisible( false );
                txtQuatidadeExistente.setVisible( false );
                txtQuatidadeExistente.setText( "" );
            }

            visualizarQtdStock();
            TbItemPedidos itemPedidosLocal = new TbItemPedidos();
            int cod_pedido = ( pedidoDao.getLastPedidoByDefignacaoMesaFALSE( mesa ) );
            pedido = pedidoDao.findTbPedido( cod_pedido );

            if ( rbSim_lugar.isSelected() )
            {

                itemPedidosLocal.setFkLugares( ( TbLugares ) lugaresController.findByLugar( String.valueOf( DVML.LUGAR_BALCAO ) ) );

            }
            else if ( rbNao_lugar.isSelected() )
            {

//                if ( activo_um_lugar() )
//                {
                    itemPedidosLocal.setFkLugares( ( TbLugares ) lugaresController.findByLugar( getDescricaoLugar() ) );
//                }
//                else
//                {
//                    return;
//                }

            }

            itemPedidosLocal.setFkProdutos( produtoDao.findTbProduto( produtoDao.getIdByDescricao( designacao_produto ) ) );
            itemPedidosLocal.setQtd( 1 );
            itemPedidosLocal.setObs( "" );
            itemPedidosLocal.setDataEntrega( new Date() );
            itemPedidosLocal.setStatusConvertido( false );
            /*Envia para a área da cozinha*/
            //para enviar  da cozinha
            itemPedidosLocal.setStatusEnviado( true );
            //para saber se o prato ja foi feito 
            itemPedidosLocal.setStatusEfectuado( false );

            double preco = precoDao.findTbPreco( precoDao.getUltimoIdPrecoByIdProduto( itemPedidosLocal.getFkProdutos().getCodigo() ) ).getPrecoVenda().doubleValue();
            double total = itemPedidosLocal.getQtd() * preco;
            itemPedidosLocal.setPreco( preco );
            itemPedidosLocal.setTotalItem( total );
            itemPedidosLocal.setFkPedidos( pedido );

            try
            {
                if ( possivel_quantidade( itemPedidosLocal.getFkProdutos().getCodigo() ) )
                {

                    // if( (conexao.getQtdSolicitados(  itemPedidos.getFkProdutos().getCodigo() ) + 1 ) <=  conexao.getQtdExistenteStock(codigo_produto, 1)  ){
                    if ( produto_local.getCodTipoProduto().getFkFamilia().getPkFamilia() == DVML.COD_PRODUTO )
                    {
                        if ( estado_critico( itemPedidosLocal.getFkProdutos().getCodigo() ) )
                        {
                            JOptionPane.showMessageDialog( null, "O produto: " + designacao_produto + " precisa de ser actualizado no stock", "AVISO", JOptionPane.WARNING_MESSAGE );
                        }

                    }

//#ped2z
                    if ( rbSim_lugar.isSelected() )
                    {

                        Integer idLastItemPedidoLugarActivo = itemPedidosDao.criarComProcedimentoLugar( itemPedidosLocal, conexao );

                        if ( idLastItemPedidoLugarActivo != null )
                        {
                            //##PINTAR
                            PrincipalPedidosVisao.mesas_livres( getLabelMesaByMesa() );
                            PrincipalPedidosVisao.pintar_mesas( getLabelMesaByMesa(), mesa );
                            System.out.println( "MESA A PINTAR: " + getLabelMesaByMesa().getText() );
                            System.out.println( "VALOR MESA A PINTAR: " + mesa );
                            //adiciona os produtos na tabela
                            actualizar();

                            int idPedido = 0;

                            TbLugares lugarEntity = ( TbLugares ) lugaresController.findById( DVML.LUGAR_BALCAO );
//                        TbLugares lugarEntity = ( TbLugares ) lugaresController.findById( itemPedidosLocal.getFkLugares().getPkLugares() );
                            String lugarLocal = lugarEntity.getDesignacao();
                            TbUsuario usuarioEntity = ( TbUsuario ) usuariosController.findById( idUser );
                            String usuario = usuarioEntity.getNome();

//            TbProduto findByDesignacao = produtosController.findByDesignacao( cmbProduto.getSelectedItem().toString() );
                            TbProduto findByDesignacao = produtosController.findByDesignacao( itemPedidosLocal.getFkProdutos().getDesignacao() );
                            if ( ( rbTicketSimples.isSelected() ) && ( findByDesignacao.getCozinha().equals( DVML.ENVIAR_TICKET ) ) )
                            {
                                MetodosUtil.imprimir_cozinha( findByDesignacao, idPedido, mesa, lugarLocal, usuario, "Activo", ( int ) itemPedidosLocal.getQtd(), dadosInstituicaoController );
                            }
                            else if ( ( rbTicketSimples.isSelected() ) && ( findByDesignacao.getCozinha().equals( DVML.ENVIAR_SALA ) ) )
                            {
                                MetodosUtil.imprimir_sala( findByDesignacao, idPedido, mesa, lugarLocal, usuario, "Activo", ( int ) itemPedidosLocal.getQtd(), dadosInstituicaoController );
                            }
                            else
                            {

                            }

                        }

                    }
                    else if ( rbNao_lugar.isSelected() )
                    {

//                        Integer idLastItemPedido = item( itemPedidosLocal, conexao );
                        Integer idLastItemPedido = itemPedidosDao.criarComProcedimento( itemPedidosLocal, conexao );

                        if ( idLastItemPedido != null )
                        {
                            //##PINTAR
                            PrincipalPedidosVisao.mesas_livres( getLabelMesaByMesa() );
                            PrincipalPedidosVisao.pintar_mesas( getLabelMesaByMesa(), mesa );
                            System.out.println( "MESA A PINTAR: " + getLabelMesaByMesa().getText() );
                            System.out.println( "VALOR MESA A PINTAR: " + mesa );
                            //adiciona os produtos na tabela
                            actualizar();

                            int idPedido = 0;

//                        TbLugares lugarEntity = ( TbLugares ) lugaresController.findById( DVML.LUGAR_BALCAO );
                            TbLugares lugarEntity = ( TbLugares ) lugaresController.findById( itemPedidosLocal.getFkLugares().getPkLugares() );
                            String lugarLocal = lugarEntity.getDesignacao();
                            TbUsuario usuarioEntity = ( TbUsuario ) usuariosController.findById( idUser );
                            String usuario = usuarioEntity.getNome();

//            TbProduto findByDesignacao = produtosController.findByDesignacao( cmbProduto.getSelectedItem().toString() );
                            TbProduto findByDesignacao = produtosController.findByDesignacao( itemPedidosLocal.getFkProdutos().getDesignacao() );
                            if ( ( rbTicketSimples.isSelected() ) && ( findByDesignacao.getCozinha().equals( DVML.ENVIAR_TICKET ) ) )
                            {
                                MetodosUtil.imprimir_cozinha( findByDesignacao, idPedido, mesa, lugarLocal, usuario, "Activo", ( int ) itemPedidosLocal.getQtd(), dadosInstituicaoController );
                            }
                            else if ( ( rbTicketSimples.isSelected() ) && ( findByDesignacao.getCozinha().equals( DVML.ENVIAR_SALA ) ) )
                            {
                                MetodosUtil.imprimir_sala( findByDesignacao, idPedido, mesa, lugarLocal, usuario, "Activo", ( int ) itemPedidosLocal.getQtd(), dadosInstituicaoController );
                            }
                            else
                            {

                            }

                        }

                    }
                    else
                    {
                        System.err.println( "ERRO AO INSERIR O ITEM ..." );
                    }

                    //  }else JOptionPane.showMessageDialog(null, "Este produto já não esta disponível.", "AVISO", JOptionPane.WARNING_MESSAGE  );
                }
                else
                {
                    JOptionPane.showMessageDialog( null, "O Produto " + designacao_produto + " não pode ser vendido para esta quantidade.", "AVISO", JOptionPane.WARNING_MESSAGE );
                }

            }
            catch ( Exception e )
            {
                e.printStackTrace();
            }

        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
//        }

    }

//    public boolean possivel_quantidade( int codigo_produto )
    public static boolean possivel_quantidade( int codigo_produto )
    {

        TbProduto produtoLocal = ( TbProduto ) produtosController.findById( codigo_produto );
        if ( true ) //            ERRORR
        //        if ( produto.getCodTipoProduto().getFkFamilia().getPkFamilia() == DVML.COD_SERVICO )
        {
            return true;
        }

        System.err.println( conexao.getQuantidade_Existente_Publico( codigo_produto, id_armzem ) );
        System.err.println( "ID ARMAZEM : " + id_armzem );
        System.err.println( "ID ARMAZEM : " + getCodigoArmazem() );
        double quant_possivel = 0d;
        try
        {
            quant_possivel = ( conexao.getQuantidade_Existente_Publico( codigo_produto, id_armzem ) - conexao.getQuantidade_minima_publico( codigo_produto, id_armzem ) );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }

        // int quant_possivel = stock.getQuantidadeExistente() -  stock.getQuantBaixa();
        System.out.println( "Quantidade Possivel: " + quant_possivel );
        System.out.println( "Quantidade Itens Pedidos: " + MetodosUtil.getQtdInItemPedidos( conexao, codigo_produto ) );
        return quant_possivel >= ( 1 + MetodosUtil.getQtdInItemPedidos( conexao, codigo_produto ) );

    }

    public static boolean possivel_quantidade( int codigo_produto, int qtd )
    {
        TbProduto produtoLocal = ( TbProduto ) produtosController.findById( codigo_produto );
        TbTipoProduto tipo = tipoProdutosController.getTipoProdutoByCodigo( produtoLocal.getCodTipoProduto().getCodigo() );
//ESTE
        if ( tipo.getFkFamilia().getPkFamilia() == DVML.COD_SERVICO ) //        if ( true )
        {
            return true;
        }

        System.err.println( conexao.getQuantidade_Existente_Publico( codigo_produto, id_armzem ) );
        System.err.println( "ID ARMAZEM : " + id_armzem );
        System.err.println( "ID ARMAZEM : " + getCodigoArmazem() );
        double quant_possivel = 0d;
        try
        {
            quant_possivel = ( conexao.getQuantidade_Existente_Publico( codigo_produto, id_armzem ) - conexao.getQuantidade_minima_publico( codigo_produto, id_armzem ) );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
        System.out.println( "Quantidade Possivel: " + quant_possivel );
        System.out.println( "Quantidade Itens Pedidos: " + MetodosUtil.getQtdInItemPedidos( conexao, codigo_produto ) );
        return quant_possivel >= ( qtd + MetodosUtil.getQtdInItemPedidos( conexao, codigo_produto ) );

    }

    public void setSalvarPedidos()
    {

        
        System.out.println( "ID PEDIDO : " + pedidoDao.getLastPedidoByDefignacaoMesaSemStatus( mesa ) );
        TbPedido pedido_local;
        //busca o último pedido de uma determinada mesa, senão existe instancia um pedido como 
        if ( pedidoDao.getLastPedidoByDefignacaoMesaSemStatus( mesa ) == 0 )
        {

            pedido_local = new TbPedido();
            pedido_local.setStatusPedido( true );

        }
        else
        {
            pedido_local = pedidoDao.findTbPedido( pedidoDao.getLastPedidoByDefignacaoMesaSemStatus( mesa ) );
        }

        if ( pedido_local.getStatusPedido() )
        {
            try
            {

                TbPedido pedido_2 = new TbPedido();
                pedido_2.setDataPedido( new Date() );
                pedido_2.setHoraPedido( new Date() );
                pedido_2.setStatusPedido( false );
//                pedido_2.setFkMesas( mesasDao.findTbMesas( mesasDao.getIdByDescricao( mesa ) ) );
//#ped1                
                Integer idLastPedido = pedidoDao.criarComProcedimentos1( pedido_2, conexao );

//                if ( idLastPedido != null )
//                {
//                    PrincipalPedidosVisao.mesas_livres( getLabelMesaByMesa() );
//                    PrincipalPedidosVisao.pintar_mesas( getLabelMesaByMesa(), this.mesa );
//                }
//                else
//                {
//                    System.err.println( "ERRO AO SALVAR O PEDIDO...." );
//                }
            }
            catch ( Exception e )
            {
                e.printStackTrace();
            }

        }
        else
        {
            //JOptionPane.showMessageDialog(null, "Não Houve Feicho");
        }

    }

    public void setSalvarItemPedidos()
    {

        try
        {

            TbItemPedidos itemPedidos = new TbItemPedidos();
            int cod_pedido = pedidoDao.getLastPedidoByDefignacaoMesaFALSE( mesa );

            this.pedido = pedidoDao.findTbPedido( cod_pedido );
            itemPedidos.setFkLugares( ( TbLugares ) lugaresController.findByLugar( getDescricaoLugar() ) );

            itemPedidos.setFkProdutos( ( TbProduto ) produtosController.findByDesignacao( getDescricaoProduto() ) );

        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }

    }

    private String getDescricaoMesa()
    {
        return String.valueOf( txtMesa.getText() );
    }

    private static String getDescricaoLugar()
    {

        if ( !btn_01.isEnabled() )
        {
            return "LUGAR 1";
        }
        else if ( !btn_02.isEnabled() )
        {
            return "LUGAR 2";
        }
        else if ( !btn_03.isEnabled() )
        {
            return "LUGAR 3";
        }
        else if ( !btn_04.isEnabled() )
        {
            return "LUGAR 4";
        }
        else if ( !btn_05.isEnabled() )
        {
            return "LUGAR 5";
        }
        else if ( !btn_06.isEnabled() )
        {
            return "LUGAR 6";
        }
        else if ( !btn_07.isEnabled() )
        {
            return "LUGAR 7";
        }
        else if ( !btn_08.isEnabled() )
        {
            return "LUGAR 8";
        }
        else if ( !btn_09.isEnabled() )
        {
            return "LUGAR 9";
        }
        else if ( !btn_10.isEnabled() )
        {
            return "LUGAR 10";
        }

        return "";
    }

    private String getDescricaoProduto()
    {
        //return String.valueOf( cmbProduto.getSelectedItem());    
        return "";
    }

    public static void actualizar()
    {
        try
        {

            adicionar_tabela( itemPedidosDao.buscaTodosItemPedidos( pedidoDao.getLastPedidoByDefignacaoMesaFALSE( mesa ) ) );
            valor_por_extenco();
            scrolltable();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }

    }

    public static void actualizar_lugar( int lugar )
    {
        try
        {

            adicionar_tabela_lugar( itemPedidosDao.buscaTodosItemPedidos2( pedidoDao.getLastPedidoByDefignacaoMesaLugarFALSE( lugar ) ) );
            scrolltable();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }

    }

    public static void adicionar_tabela( List<TbItemPedidos> itemPedidos_list )
    {
        DefaultTableModel modelo = ( DefaultTableModel ) jTable1.getModel();
        Double preco = 0d, desconto = 0d, taxa = 0d;
        double qtd = 0;

//        if ( validar_zero() )
//        {
        //limpa dados na jTabel1
//            stock_local = stockDao.get_stock_by_id_produto_and_id_armazem( this.itemPedidos.getFkProdutos().getCodigo(), getCodigoArmazem() );
        //so retirar caso existir mesmo no armazém em questão.
//        if ( stock_local.getCodigo() != 0 && itemPedidos.getFkProdutos().getStocavel().equals( "true" ) )
        modelo.setRowCount( 0 );

        if ( itemPedidos_list != null )
        {

            //define a altura das linhas
            jTable1.setRowHeight( 50 );
            try
            {
                for ( int i = 0; i < itemPedidos_list.size(); i++ )
                {

                    qtd = itemPedidos_list.get( i ).getQtd();

                    preco = precoDao.findTbPreco( precoDao.getUltimoIdPrecoByIdProduto( itemPedidos_list.get( i ).getFkProdutos().getCodigo(), qtd ) ).getPrecoVenda().doubleValue();
//                    preco = precoDao.findTbPreco( precoDao.getUltimoIdPrecoByIdProduto( itemPedidos_list.get( i ).getFkProdutos().getCodigo(), qtd ) ).getPrecoVenda().doubleValue();
                    System.err.println( "QTD ADD: " + qtd );
                    System.err.println( "PU  ADD: " + preco );

                    taxa = MetodosUtil.getTaxaPercantagem( itemPedidos_list.get( i ).getFkProdutos().getCodigo() );
                    modelo.addRow( new Object[]
                    {
                        itemPedidos_list.get( i ).getPkItemPedidos(),
                        itemPedidos_list.get( i ).getFkLugares().getDesignacao(),
                        itemPedidos_list.get( i ).getFkProdutos().getDesignacao(),
                        qtd,
                        //                        CfMethods.formatarComoMoeda(MetodosUtil.getValorComIVA( qtd, taxa, preco, desconto ))
                        //                        MetodosUtil.getValorComIVA( qtd, taxa, preco, desconto )
                        CfMethods.formatarComoMoeda( FinanceUtils.getValorComIVA( qtd, taxa, preco, desconto ) )
                    //itemPedidos.get( i ).getQtd() * precoDao.findTbPreco( precoDao.getUltimoIdPrecoByIdProduto( itemPedidos.get( i ).getFkProdutos().getCodigo() ) ).getPrecoVenda()
                    } );
                }
            }
            catch ( Exception e )
            {
                e.printStackTrace();
            }

            setTotalPagar();
            valor_por_extenco();

        }
//        }
//        else
//        {
//            JOptionPane.showMessageDialog( null, "Atenção\nA quantidade a sair não pode ser igual a zero!" );
//        }

    }

    public static void adicionar_tabela_lugar( List<TbItemPedidos> itemPedidos_list )
    {
        DefaultTableModel modelo = ( DefaultTableModel ) jTable1.getModel();
        Double preco = 0d, desconto = 0d, taxa = 0d;
        double qtd = 0;

//        if ( validar_zero() )
//        {
        //limpa dados na jTabel1
//            stock_local = stockDao.get_stock_by_id_produto_and_id_armazem( this.itemPedidos.getFkProdutos().getCodigo(), getCodigoArmazem() );
        //so retirar caso existir mesmo no armazém em questão.
//        if ( stock_local.getCodigo() != 0 && itemPedidos.getFkProdutos().getStocavel().equals( "true" ) )
        modelo.setRowCount( 0 );

        if ( itemPedidos_list != null )
        {

            //define a altura das linhas
            jTable1.setRowHeight( 50 );
            try
            {
                for ( int i = 0; i < itemPedidos_list.size(); i++ )
                {

                    qtd = itemPedidos_list.get( i ).getQtd();

                    preco = precoDao.findTbPreco( precoDao.getUltimoIdPrecoByIdProduto( itemPedidos_list.get( i ).getFkProdutos().getCodigo(), qtd ) ).getPrecoVenda().doubleValue();
                    System.err.println( "QTD ADD: " + qtd );
                    System.err.println( "PU  ADD: " + preco );

                    taxa = MetodosUtil.getTaxaPercantagem( itemPedidos_list.get( i ).getFkProdutos().getCodigo() );
                    modelo.addRow( new Object[]
                    {
                        itemPedidos_list.get( i ).getPkItemPedidos(),
                        itemPedidos_list.get( i ).getFkLugares().getDesignacao(),
                        itemPedidos_list.get( i ).getFkProdutos().getDesignacao(),
                        qtd,
                        //                        CfMethods.formatarComoMoeda(MetodosUtil.getValorComIVA( qtd, taxa, preco, desconto ))
                        //                        MetodosUtil.getValorComIVA( qtd, taxa, preco, desconto )
                        CfMethods.formatarComoMoeda( FinanceUtils.getValorComIVA( qtd, taxa, preco, desconto ) )
                    //itemPedidos.get( i ).getQtd() * precoDao.findTbPreco( precoDao.getUltimoIdPrecoByIdProduto( itemPedidos.get( i ).getFkProdutos().getCodigo() ) ).getPrecoVenda()
                    } );
                }
            }
            catch ( Exception e )
            {
                e.printStackTrace();
            }

            setTotalPagar();
            valor_por_extenco();
        }
//        }
//        else
//        {
//            JOptionPane.showMessageDialog( null, "Atenção\nA quantidade a sair não pode ser igual a zero!" );
//        }

    }

    public boolean validar_zero()
    {
        if ( Double.parseDouble( txtQuatidadeExistente.getText() ) == 0.0 )
        {
            JOptionPane.showMessageDialog( null, "Atenção\nA quantidade a sair não pode ser igual a zero!" );

            return false;
        }
        return true;
    }

    private Date getDataPedido()
    {
        return new Date( Integer.parseInt( String.valueOf( new Date().getYear() ) ) - 1900, new Date().getMonth(), new Date().getDay() );
    }

    public int getLastCodigo( String tabela )
    {

        String sql = "SELECT max(pk_pedido) FROM " + tabela;
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
        }
        return 0;

    }

    public void imprimirPedidos()
    {

        try
        {
            TbPedido pedido = pedidoDao.findTbPedido( pedidoDao.getLastPedidoByDefignacaoMesaFALSE( mesa ) );

            List<TbItemPedidos> item = itemPedidosDao.buscaTodosItemPedidos( pedido.getPkPedido() );
            double totalPedidos = 0;
            for ( TbItemPedidos tbItemPedidos : item )
            {
                totalPedidos += tbItemPedidos.getTotalItem();
            }

            //pedido.setStatusPedido(true);
            //pedidoDao.edit(pedido);
            ListaPedidos listaPedidos = new ListaPedidos( pedido.getPkPedido(), totalPedidos );
            //eliminar_toda_tabela(jTable1);
            //eliminar_toda_tabela(jTable1);

        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }

    }

    public void imprimirPedidosTicket()
    {

        try
        {

            ListaPedidos listaPedidos = new ListaPedidos( pedido.getPkPedido() );

        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }

    }

    public void imprimirPedidosPorLugar( int opcao )
    {

        try
        {
            TbPedido pedido = pedidoDao.findTbPedido( pedidoDao.getLastPedidoByDefignacaoMesaFALSE( mesa ) );
            //TbPedido pedido = pedidoDao.findTbPedido(   pedidoDao.getLastPedidoByDefignacaoMesaFALSE(mesa)     );
            //pedido.setStatusPedido(true);
            pedidoDao.edit( pedido );
            ListaPedidos listaPedidos = new ListaPedidos( pedido.getPkPedido(), opcao );
            //eliminar_toda_tabela(jTable1);
            //eliminar_toda_tabela(jTable1);

        }
        catch ( Exception e )
        {
        }
    }

//    //----------- evento do teclado ---------------------------------------
//    public class TratarTroco implements KeyListener
//    {
//
//        String prefixo = "";
//
//        public void keyPressed( KeyEvent evt )
//        {
//
//            String prefixo = "";
//
//            if ( evt.getKeyCode() != KeyEvent.VK_BACK_SPACE )
//            {
//                char key = evt.getKeyChar();
//                prefixo = txtValorEntregue.getText().trim() + key;
//                double troco = 0;
//                double valor_entregue = Double.parseDouble( prefixo );
//                double total_pagar = Double.parseDouble( txtTotalApagar.getText().trim() );
//                troco = valor_entregue - total_pagar;
//                txtTroco.setText( String.valueOf( troco ) );
//
//            }
//            else if ( evt.getKeyCode() == KeyEvent.VK_BACK_SPACE )
//            {
//                char key = evt.getKeyChar();
//
//                prefixo = txtValorEntregue.getText() + key;
//
//                double valor_entregue = 0;
//                double troco = 0;
//                double total_pagar = Double.parseDouble( txtTotalApagar.getText().trim() );
//
//                try
//                {
//                    valor_entregue = Double.parseDouble( prefixo.trim().substring( 0, prefixo.length() - 2 ) );
//                    total_pagar = Double.parseDouble( txtTotalApagar.getText().trim() );
//                    troco = valor_entregue - total_pagar;
//                }
//                catch ( Exception e )
//                {
//                    valor_entregue = 0;
//                }
//
//                troco = valor_entregue - total_pagar;
//                txtTroco.setText( String.valueOf( troco ) );
//
//            }
//
//        }
//
//        public void keyReleased( KeyEvent evt )
//        {
//        }
//
//        public void keyTyped( KeyEvent evt )
//        {
//        }
//
//    }
    private void eliminar()
    {

        int opcao = JOptionPane.showConfirmDialog( null, "Tens a certeza que queres mesmo eliminar esse pedido\n Uma vez eliminado todas as tabelas relacionadas com o mesmo deixaram de existir", "AVISO", JOptionPane.WARNING_MESSAGE );

        System.out.println( "OPCAO " + opcao );
        if ( opcao == 0 )
        {

            try
            {

                itemPedidos = itemPedidosDao.findTbItemPedidos( idpedido );
                this.itemPedidos.getFkPedidos().setStatusPedido( false );
                itemPedidosDao.destroy( idpedido );
                // limpar();
                adicionar_tabela( itemPedidosDao.getAllPedidosNovos() );
                JOptionPane.showMessageDialog( null, "Item eliminado com sucesso!..." );

            }
            catch ( Exception e )
            {
                e.printStackTrace();
                JOptionPane.showMessageDialog( null, "Erro ao eliminar o Item", "ERRO", JOptionPane.ERROR_MESSAGE );
            }

        }
        else
        {
            JOptionPane.showMessageDialog( null, "O Item não foi eliminado" );
        }

    }

    public static void eliminar_item( JTable tabela )
    {

        DefaultTableModel modelo = ( DefaultTableModel ) tabela.getModel();
        int id_item_pedido = Integer.parseInt( modelo.getValueAt( tabela.getSelectedRow(), 0 ).toString() );
        TbItemPedidos itemPedidosLocal = itemPedidosDao.findTbItemPedidos( id_item_pedido );
        try
        {

            int idPedido = itemPedidosLocal.getPkItemPedidos();

            TbLugares lugarEntity = ( TbLugares ) lugaresController.findById( itemPedidosLocal.getFkLugares().getPkLugares() );
            String lugarLocal = lugarEntity.getDesignacao();
            TbUsuario usuarioEntity = ( TbUsuario ) usuariosController.findById( idUser );
            String usuario = usuarioEntity.getNome();

            TbProduto findByDesignacao = produtosController.findByDesignacao( itemPedidosLocal.getFkProdutos().getDesignacao() );
            if ( findByDesignacao.getCozinha().equals( DVML.ENVIAR_TICKET ) )
            {
                MetodosUtil.imprimir_cozinha( findByDesignacao, idPedido, mesa, lugarLocal, usuario, "Cancelado", ( int ) itemPedidosLocal.getQtd(), dadosInstituicaoController );
            }
            else if ( findByDesignacao.getCozinha().equals( DVML.ENVIAR_SALA ) )
            {
                MetodosUtil.imprimir_sala( findByDesignacao, idPedido, mesa, lugarLocal, usuario, "Cancelado", ( int ) itemPedidosLocal.getQtd(), dadosInstituicaoController );
            }

//            MetodosUtil.imprimir_cozinha( itemPedidosLocal.getFkProdutos(),
//                    "Cancelado", itemPedidosLocal.getQtd(),
//                    dadosInstituicaoController );
            itemPedidosDao.destroy( id_item_pedido );
            actualizar();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            JOptionPane.showMessageDialog( null, "Erro:\n Não foi eliminado o Item" );
        }

    }

    public static void salvar_item_cancelado( JTable tabela )
    {

        DefaultTableModel modelo = ( DefaultTableModel ) tabela.getModel();
        int id_item_pedido = Integer.parseInt( modelo.getValueAt( tabela.getSelectedRow(), 0 ).toString() );

        try
        {

            itemPedidosDao.destroy( id_item_pedido );
            actualizar();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            JOptionPane.showMessageDialog( null, "Erro:\n Não foi eliminado o Item" );
        }

    }

    private void actualizar_campo( int qtd )
    {

        DefaultTableModel modelo = ( DefaultTableModel ) jTable1.getModel();
        TbItemPedidos itemPedidos = itemPedidosDao.findTbItemPedidos( Integer.parseInt( jTable1.getValueAt( this.linha_actual, 0 ).toString() ) );
        itemPedidos.setQtd( qtd );

        try
        {
            itemPedidosDao.edit( itemPedidos );
            actualizar();
        }
        catch ( Exception e )
        {
        }

    }

    public static void gravar_item_eliminado( JTable tabela )
    {
//        linha_acutal = jTable1.getSelectedRow();
        DefaultTableModel modelo = ( DefaultTableModel ) tabela.getModel();
        TbItemPedidos itemPedidos = itemPedidosDao.findTbItemPedidos( Integer.parseInt( jTable1.getValueAt( linha_actual, 0 ).toString() ) );
//        int id_item_pedido = Integer.parseInt( modelo.getValueAt( tabela.getSelectedRow(), 0 ).toString() );

        try
        {
            itemPedidosDao.create( itemPedidos );
//            actualizar();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            JOptionPane.showMessageDialog( null, "Erro:\n Não foi salvo o Item" );
        }

    }

    private void eliminar_toda_tabela( JTable tabela )
    {

        try
        {
            DefaultTableModel modelo = ( DefaultTableModel ) tabela.getModel();
            int filas = tabela.getRowCount();

            for ( int i = 0; filas > i; i++ )
            {

                modelo.removeRow( 0 );
                actualizar();
            }
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            JOptionPane.showMessageDialog( null, "Erro:\n Não foi eliminado o Item" );
        }

    }

    public int getCodigoCliente()
    {
        return 1;
    }

    public static TbVenda salvar_venda()
    {
        conexaoTransaction = BDConexao.getInstancia();
        DocumentosController.start( conexaoTransaction );
//        Date data_documento = new Date ();
        Date data_documento = dc_data_documento.getDate();
        TbVenda venda_local = new TbVenda();
        venda_local.setDataVenda( data_documento );
        Calendar calendar = Calendar.getInstance();
        calendar.setTime( data_documento );
        calendar.add( Calendar.DATE, 15 );
        venda_local.setDataVencimento( calendar.getTime() );
        venda_local.setHora( data_documento );
        venda_local.setNomeCliente( getNomeCliente() );
        venda_local.setClienteNif( getClienteNif() );
        TbCliente clienteSelecionado = clientesController.getClienteByNome( ( String ) cmbCliente.getSelectedItem() );

        if ( !Objects.isNull( clienteSelecionado ) )
        {
            venda_local.setNomeCliente( clienteSelecionado.getNome() );
            venda_local.setClienteNif( clienteSelecionado.getNif() );
            venda_local.setCodigoCliente( clienteSelecionado );
        }
        else
        {
//            venda_local.setNomeCliente ( _CLIENTE_CONSUMIDOR_FINAL );
            venda_local.setNomeCliente( _CLIENTE_CONSUMIDOR_FINAL );

            venda_local.setNomeConsumidorFinal( txtNomeConsumidorFinal.getText() );
            venda_local.setCodigoCliente( clientesController.findByCodigo( getIdCliente() ) );
            venda_local.setClienteNif( NUMBER_NIF_GENERICO );
        }
        //Total Ilíquido
        venda_local.setTotalGeral( new BigDecimal( getTotalIliquido() ) );
        //desconto por linha
        venda_local.setDescontoComercial( new BigDecimal( getDescontoComercial() ) );
        //imposto
        //calculaTotalIVA();
        venda_local.setTotalIva( new BigDecimal( getTotalImposto() ) );
        //desconto global
        venda_local.setDescontoFinanceiro( new BigDecimal( getDescontoFinanceiro() ) );
        //Total(AOA) <=> Total Líquido
        venda_local.setTotalVenda( new BigDecimal( getTotalAOALiquido() ) );
        venda_local.setValorEntregue( new BigDecimal( getValor_entregue() ) );
        venda_local.setTroco( new BigDecimal( getTroco() ) );
        venda_local.setTotalIncidencia( new BigDecimal( getTotalIncidencia() ) );
        venda_local.setTotalIncidenciaIsento( new BigDecimal( getTotalIncidenciaIsento() ) );
        venda_local.setDescontoTotal( new BigDecimal( getDescontoComercial() + getDescontoFinanceiro() ) );
        venda_local.setIdArmazemFK( new TbArmazem( id_armzem ) );
        venda_local.setCodigoUsuario( new TbUsuario( idUser ) );
        venda_local.setFkAnoEconomico( anoEconomico );
        venda_local.setFkDocumento( documento );
        venda_local.setCodFact( getCodDocActualizador() );
        venda_local.setRefDataFact( data_documento );
        venda_local.setTotalPorExtenso( iniciais_extenso() + lbValorPorExtenco.getText() );
//        venda_local.setTotalPorExtenso( MetodosUtil.iniciais_extenso( DOC_FACTURA_RECIBO_FR, documentoDao ) + MetodosUtil.valorPorExtenso( venda_local.getTotalVenda().doubleValue(), "Kwanza" ) );
        System.out.println( "STATUS:hash cod processado." );
        venda_local.setHashCod( MetodosUtil.criptografia_hash( venda_local, getGrossTotal(), conexao ) );
        venda_local.setAssinatura( MetodosUtil.assinatura_doc( venda_local.getHashCod() ) );
        venda_local.setFkCambio( cambiosController.findByCodigo( ID_CAMBIO_NACIONAL ) );
        /*status documento*/
        venda_local.setStatusEliminado( "false" );
        venda_local.setPerformance( "false" );
        venda_local.setCredito( "false" );
        venda_local.setGorjeta( new BigDecimal( gorjeta ) );

        Integer last_venda = 0;

        try
        {

            if ( vendasController.salvar( venda_local ) )
            {
                last_venda = vendasController.getLastVenda().getCodigo();

                if ( Objects.isNull( last_venda ) || last_venda == 0 )
                {
                    DocumentosController.rollback( conexaoTransaction );
                    conexaoTransaction.close();
                    return venda_local;
                }
                System.err.println( "last_venda: " + last_venda );
                System.out.println( "STATUS:factura criada com sucesso." );

                if ( last_venda != null )
                {
                    if ( getIdDocumento() == DOC_FACTURA_RECIBO_FR )
                    {
//                        MetodosUtil.adicionar_saldo_banco( venda_local.getTotalVenda().doubleValue(), venda_local.get.getIdBanco(), conexao );
                    }
//                    salvar_item_venda_comercial( last_venda );
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
//        return vendaDao.findTbVenda( last_venda );
        return new TbVenda( last_venda );

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

    private static double getTotalRetencao()
    {
//        DefaultTableModel modelo = ( DefaultTableModel ) jTable1.getModel();
        double imposto = 0d;
        return imposto;
    }

    public static TbVenda salvar_venda( int lugar )
    {
        conexaoTransaction = BDConexao.getInstancia();
        DocumentosController.start( conexaoTransaction );
//        Date data_documento = new Date ();
        Date data_documento = dc_data_documento.getDate();
        TbVenda venda_local = new TbVenda();
        venda_local.setDataVenda( data_documento );
        Calendar calendar = Calendar.getInstance();
        calendar.setTime( data_documento );
        calendar.add( Calendar.DATE, 15 );
        venda_local.setDataVencimento( calendar.getTime() );
        venda_local.setHora( data_documento );
        venda_local.setNomeCliente( getNomeCliente() );
        venda_local.setClienteNif( getClienteNif() );
        TbCliente clienteSelecionado = clientesController.getClienteByNome( ( String ) cmbCliente.getSelectedItem() );

        if ( !Objects.isNull( clienteSelecionado ) )
        {
            venda_local.setNomeCliente( clienteSelecionado.getNome() );
            venda_local.setClienteNif( clienteSelecionado.getNif() );
            venda_local.setCodigoCliente( clienteSelecionado );
        }
        else
        {
//            venda_local.setNomeCliente ( _CLIENTE_CONSUMIDOR_FINAL );
            venda_local.setNomeCliente( _CLIENTE_CONSUMIDOR_FINAL );
            venda_local.setNomeConsumidorFinal( txtNomeConsumidorFinal.getText() );
            venda_local.setCodigoCliente( clientesController.findByCodigo( getIdCliente() ) );
            venda_local.setClienteNif( NUMBER_NIF_GENERICO );
//            venda_local.setCodigoCliente( clienteDao.findTbCliente( getIdCliente() ) );
        }

        //Total Ilíquido
        venda_local.setTotalGeral( new BigDecimal( getTotalIliquido( lugar ) ) );
        venda_local.setDescontoComercial( new BigDecimal( getDescontoComercial( lugar ) ) );
        venda_local.setDescontoFinanceiro( new BigDecimal( getDescontoFinanceiro( lugar ) ) );
        venda_local.setTotalIva( new BigDecimal( getTotalImposto( lugar ) ) );
        venda_local.setTotalVenda( new BigDecimal( getTotalAOALiquido( lugar ) ) );
        venda_local.setValorEntregue( new BigDecimal( getValor_entregue() ) );
        venda_local.setTroco( new BigDecimal( getTrocoLugar() ) );
        venda_local.setTotalIncidencia( new BigDecimal( getTotalIncidencia( lugar ) ) );
        venda_local.setTotalIncidenciaIsento( new BigDecimal( getTotalIncidenciaIsento( lugar ) ) );
        venda_local.setDescontoTotal( new BigDecimal( getDescontoComercial( lugar ) + getDescontoFinanceiro( lugar ) ) );
        venda_local.setIdArmazemFK( new TbArmazem( id_armzem ) );
        venda_local.setCodigoUsuario( new TbUsuario( idUser ) );
        venda_local.setFkAnoEconomico( anoEconomico );
        venda_local.setFkDocumento( documento );
        venda_local.setCodFact( getCodDocActualizador() );
        venda_local.setRefDataFact( data_documento );
        venda_local.setTotalPorExtenso( iniciais_extenso() + lbValorPorExtenco.getText() );
        venda_local.setHashCod( MetodosUtil.criptografia_hash( venda_local, getGrossTotal( lugar ), conexao ) );
        venda_local.setAssinatura( MetodosUtil.assinatura_doc( venda_local.getHashCod() ) );
        venda_local.setFkCambio( cambiosController.findByCodigo( ID_CAMBIO_NACIONAL ) );
        /*status documento*/
        venda_local.setStatusEliminado( "false" );
        venda_local.setPerformance( "false" );
        venda_local.setCredito( "false" );
        venda_local.setGorjeta( new BigDecimal( gorjeta ) );

        Integer last_venda = 0;

        try
        {

            if ( vendasController.salvar( venda_local ) )
            {
                last_venda = vendasController.getLastVenda().getCodigo();

                if ( Objects.isNull( last_venda ) || last_venda == 0 )
                {
                    DocumentosController.rollback( conexaoTransaction );
                    conexaoTransaction.close();
                    return venda_local;
                }
                System.err.println( "last_venda: " + last_venda );
                System.out.println( "STATUS:factura criada com sucesso." );

                if ( last_venda != null )
                {
                    if ( getIdDocumento() == DOC_FACTURA_RECIBO_FR )
                    {
//                        MetodosUtil.adicionar_saldo_banco( venda_local.getTotalVenda().doubleValue(), venda_local.get.getIdBanco(), conexao );
                    }
//                    salvar_item_venda_comercial( last_venda );
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
//        return vendaDao.findTbVenda( last_venda );
        return new TbVenda( last_venda );

    }

    private static String getNomeCliente()
    {
        return cmbCliente.getSelectedItem().toString();
    }

//        private static String iniciais_extenso()
//    {
//        Documento documento_local = ( Documento ) documentosController.findById( getIdDocumento() );
//        String abreviacao_local = documento_local.getAbreviacao();
//
//        switch ( abreviacao_local )
//        {
//            case "FT":
//                return "Facturamos o valor de: ";
//            case "FR":
//                return "Recebemos a quantia de: ";
//            default:
//                return "São: ";
//        }
//    }
    private static String iniciais_extenso()
    {
        Documento documento_local = ( Documento ) documentosController.findById( getIdDocumento() );
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

    private static String getCodDocActualizador()
    {
        try
        {
            documento = ( Documento ) documentosController.findById( getIdDocumento() );
            anoEconomico = ( AnoEconomico ) anoEconomicoController.findById( getIdAnoEconomico() );
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

    private static Moeda getMoeda()
    {
        String moedaSelecionada = ( String ) cmbMoeda.getSelectedItem();

        if ( moedaSelecionada == null )
        {
            return null;
        }

        return new MoedaDao( emf ).getByDescricao( moedaSelecionada );
    }

//    private static void valor_por_extenco()
//    {
////        System.out.println( "Valor XXXXXXX: " + CfMethods.parseMoedaFormatada( txtTotal_AOA_liquido.getText() ) );
//        lbValorPorExtenco.setText( MetodosUtil.valorPorExtenso( CfMethods.parseMoedaFormatada( txtTotalApagar.getText() ), getMoeda().getDesignacao() ) );
//    }
//    private static void valor_por_extenco()
//    {
////        System.out.println( "Valor XXXXXXX: " + CfMethods.parseMoedaFormatada( txtTotalApagar.getText() ) );
//
//        if ( !txtTotalApagar.getText().equals( "" ) )
//        {
//            lbValorPorExtenco.setText( MetodosUtil.valorPorExtenso( CfMethods.parseMoedaFormatada( txtTotalApagar.getText() ), "Kwanza" ) );
//
//        }
//    }
    private static void valor_por_extenco()
    {
//        System.out.println( "Valor XXXXXXX: " + CfMethods.parseMoedaFormatada( txtTotalApagar.getText() ) );

        if ( !txtTotalApagar.getText().equals( "" ) && !txtTotalApagar.getText().equals( "0" ) )
        {

            System.out.println( "VALOR: " + txtTotalApagar.getText() );
            lbValorPorExtenco.setText( MetodosUtil.valorPorExtenso( CfMethods.parseMoedaFormatada( txtTotalApagar.getText() ), "Kwanza" ) );

        }
    }

//    private static void valor_por_extenco( Moeda moeda )
//    {
//        System.out.println( "Valor XXXXXXX: " + CfMethods.parseMoedaFormatada( txtTotalApagar.getText() ) );
//        lbValorPorExtenco.setText( MetodosUtil.valorPorExtenso( CfMethods.parseMoedaFormatada( txtTotalApagar.getText() ), moeda.getDesignacao() ) );
//    }
    private static String getClienteNif()
    {
        try
        {
            TbCliente cliente = ( TbCliente ) clientesController.findById( getIdCliente() );
            return cliente.getNif();
        }
        catch ( Exception e )
        {
            return "";
        }
    }

    public static void salvarItemvenda( TbVenda venda )
    {

        DefaultTableModel modelo = ( DefaultTableModel ) jTable1.getModel();
        boolean efectuada = true;
        int idProduto = 0;
        double qtd = 0d;
        String lugar = "1";
        TbItemVenda itemVenda = null;
        TbProduto produto;
//        TbStock stock_local;
        double sub_total_iliquido = 0, preco_unitario = 0d, taxa = 0d, desconto = 0d, valor_iva = 0d;

        for ( int i = 0; i < jTable1.getRowCount(); i++ )
        {
            try
            {

                itemVenda = new TbItemVenda();
                TbProduto produto_local = ( TbProduto ) produtosController.findByDesignacao( modelo.getValueAt( i, 2 ).toString() );
                lugar = modelo.getValueAt( i, 1 ).toString();
                idProduto = produtosController.findByDesignacao( modelo.getValueAt( i, 2 ).toString() ).getCodigo();
//                idProduto = produtoDao.getProdutoByDescricao( modelo.getValueAt( i, 2 ).toString() ).getCodigo();
                qtd = Double.parseDouble( modelo.getValueAt( i, 3 ).toString() );
                preco_unitario = precosController.getLastIdPrecoByIdProduto( idProduto, qtd ).getPrecoVenda().doubleValue();
//                preco_unitario = precoDao.findTbPreco( precoDao.getUltimoIdPrecoByIdProduto( idProduto, qtd ) ).getPrecoVenda().doubleValue();
                taxa = MetodosUtil.getTaxaPercantagem( idProduto );
//                sub_total_iliquido = MetodosUtil.getValorComIVA( qtd, taxa, preco_unitario, desconto );
                sub_total_iliquido = CfMethods.parseMoedaFormatada( modelo.getValueAt( i, 4 ).toString() );

                itemVenda.setCodigoProduto( produtosController.findByCod( idProduto ) );
                itemVenda.setCodigoVenda( venda );
                itemVenda.setQuantidade( qtd );
                itemVenda.setDesconto( desconto );
                itemVenda.setValorIva( new BigDecimal( taxa ).doubleValue() );
                itemVenda.setMotivoIsensao( MetodosUtil.getMotivoIsensao( idProduto ) );
                itemVenda.setCodigoIsensao( MetodosUtil.getCodigoRegime( idProduto ) );
                itemVenda.setTotal( new BigDecimal( sub_total_iliquido ) );
                itemVenda.setFkPreco( precosController.getLastIdPrecoByIdProduto( itemVenda.getCodigoProduto().getCodigo(), itemVenda.getQuantidade() ) );
//                itemVenda.setFkPreco( precoDao.findTbPreco( precoDao.getUltimoIdPrecoByIdProduto( itemVenda.getCodigoProduto().getCodigo(), itemVenda.getQuantidade() ) ) );
//                itemVenda.setValorRetencao( 0d );
                itemVenda.setDataServico( new Date() );
                /*setando a mesa e lugar para cunprir a formalidade só aplica-se somente para resstauração*/
//                itemVenda.setFkLugares( ( TbLugares ) lugaresController.findById( DVML.LUGAR_BALCAO ) );
                itemVenda.setFkLugares( ( TbLugares ) lugaresController.findByLugar( lugar ) );
//                itemVenda.setFkLugares( lugarDao.findTbLugares( lugarDao.getIdByDescricao( lugar ) ) );
                itemVenda.setFkMesas( ( TbMesas ) mesasController.findByDesignacao( mesa ) );

                //cria o item venda
//                itemVendaDao.create ( itemVenda );
//                int last_venda = itemVendaDao.criarComProcedimentos( itemVenda, conexao );
                if ( !itemVendasController.salvar( itemVenda ) )
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
//                stock_local = stockDao.get_stock_by_id_produto_and_id_armazem( idProduto, id_armazem );
//                stock_local = stockDao.get_stock_by_id_produto_and_id_armazem( itemVenda.getCodigoProduto().getCodigo(), id_armzem );
                if ( !Objects.isNull( stock_local ) )
                {
                    if ( getIdDocumento() == DOC_FACTURA_RECIBO_FR || getIdDocumento() == DOC_FACTURA_FT || getIdDocumento() == DOC_FACTURA_CONSULTA_MESA )
                    {
                        System.out.println( "passei quando é FR ou FT" );
                        //so retirar caso existir mesmo no armazém em questão.
                        if ( stock_local.getCodigo() != 0 && itemVenda.getCodigoProduto().getStocavel().equals( "true" ) )
                        {
                            actualizar_quantidade( itemVenda.getCodigoProduto().getCodigo(), itemVenda.getQuantidade() );
                        }
                    }

                }

            }
            catch ( Exception e )
            {
                e.printStackTrace();
                efectuada = false;
                JOptionPane.showMessageDialog( null, "Falha ao registrar o produto: " + itemVenda.getCodigoProduto().getCodigo() + " na Factura" );
                break;
            }
        }

        if ( efectuada )
        {
            JOptionPane.showMessageDialog( null, "Factura efectuada com sucesso!.." );
            TbPedido pedido = pedidoDao.findTbPedido( pedidoDao.getLastPedidoByDefignacaoMesaFALSE( mesa ) );
            PedidoDao.eliminarPedido( pedido, conexao ); // Elimina o pedido

            System.err.println( "Codigo Venda: " + venda.getCodigo() );
            System.err.println( "Venda: " + venda.getCodFact() );
            gorjeta = 0;

//            int numeroVias = 1;
            int numeroVias = ( int ) Double.parseDouble( spnCopia.getValue().toString() );
            for ( int i = 1; i <= numeroVias; i++ )
            {

                switch ( i )
                {
                    case 1:

                        ListaVendaPorMesas listaVenda1 = new ListaVendaPorMesas( venda.getCodigo(), abreviacao, false, true, "Original" );
                        break;
                    case 2:
                        ListaVendaPorMesas listaVenda2 = new ListaVendaPorMesas( venda.getCodigo(), abreviacao, false, true, "Original" );
                        break;
                    case 3:
                        ListaVendaPorMesas listaVenda3 = new ListaVendaPorMesas( venda.getCodigo(), abreviacao, false, true, "Triplicado" );
                        break;
                }

            }

//            ListaVenda listaVenda = new ListaVenda( venda.getCodigo(), false, true );
//            ListaVenda listaVenda1 = new ListaVenda( venda.getCodigo(), false, true );
//            ListaVenda listaVenda = new ListaVenda ( venda.getCodigo(), false, true, "Original" );
//            ListaVenda listaVenda1 = new ListaVenda ( venda.getCodigo(), false, true, "Duplicado" );
            PrincipalPedidosVisao.mesas_livres( getLabelMesaByMesa() );
            PrincipalPedidosVisao.pintar_mesas( getLabelMesaByMesa(), mesa );
            procedimento_mesas_livre();
//            JOptionPane.showMessageDialog( null, "Factura Convertida com sucesso!.." );

        }

    }

    public static void salvarItemvendaLugar( TbVenda venda, String lugar )
    {

        DefaultTableModel modelo = ( DefaultTableModel ) jTable1.getModel();
        boolean efectuada = true;
        int idProduto = 0;
        double qtd = 0d;
        int cod_mesa = mesasDao.getIdByDescricao( mesa );
//        String lugar = "1";
        TbItemVenda itemVenda = null;
        TbProduto produto;
//        TbStock stock_local;
        double sub_total_iliquido = 0, preco_unitario = 0d, taxa = 0d, desconto = 0d, valor_iva = 0d;
        int valor_lugar_tabela = 0;

        for ( int i = 0; i < jTable1.getRowCount(); i++ )
        {
            try
            {

                valor_lugar_tabela = getLugarSelecionado( i );
                if ( valor_lugar_tabela == Integer.parseInt( lugar ) )
                {

                    itemVenda = new TbItemVenda();
                    TbProduto produto_local = ( TbProduto ) produtosController.findByDesignacao( modelo.getValueAt( i, 2 ).toString() );
//                lugar = modelo.getValueAt( i, 1 ).toString();
                    idProduto = produtosController.findByDesignacao( modelo.getValueAt( i, 2 ).toString() ).getCodigo();
//                idProduto = produtoDao.getProdutoByDescricao( modelo.getValueAt( i, 2 ).toString() ).getCodigo();
                    qtd = Double.parseDouble( modelo.getValueAt( i, 3 ).toString() );
                    preco_unitario = precosController.getLastIdPrecoByIdProduto( idProduto, qtd ).getPrecoVenda().doubleValue();
//                preco_unitario = precoDao.findTbPreco( precoDao.getUltimoIdPrecoByIdProduto( idProduto, qtd ) ).getPrecoVenda().doubleValue();
                    taxa = MetodosUtil.getTaxaPercantagem( idProduto );
//                sub_total_iliquido = MetodosUtil.getValorComIVA( qtd, taxa, preco_unitario, desconto );
                    sub_total_iliquido = CfMethods.parseMoedaFormatada( modelo.getValueAt( i, 4 ).toString() );

                    itemVenda.setCodigoProduto( produtosController.findByCod( idProduto ) );
                    itemVenda.setCodigoVenda( venda );
                    itemVenda.setQuantidade( qtd );
                    itemVenda.setDesconto( desconto );
                    itemVenda.setValorIva( new BigDecimal( taxa ).doubleValue() );
                    itemVenda.setMotivoIsensao( MetodosUtil.getMotivoIsensao( idProduto ) );
                    itemVenda.setCodigoIsensao( MetodosUtil.getCodigoRegime( idProduto ) );
                    itemVenda.setTotal( new BigDecimal( sub_total_iliquido ) );
                    itemVenda.setFkPreco( precosController.getLastIdPrecoByIdProduto( itemVenda.getCodigoProduto().getCodigo(), itemVenda.getQuantidade() ) );
//                itemVenda.setFkPreco( precoDao.findTbPreco( precoDao.getUltimoIdPrecoByIdProduto( itemVenda.getCodigoProduto().getCodigo(), itemVenda.getQuantidade() ) ) );
//                itemVenda.setValorRetencao( 0d );
                    itemVenda.setDataServico( new Date() );
                    /*setando a mesa e lugar para cunprir a formalidade só aplica-se somente para resstauração*/
//                itemVenda.setFkLugares( ( TbLugares ) lugaresController.findById( DVML.LUGAR_BALCAO ) );
//                    itemVenda.setFkLugares( ( TbLugares ) lugaresController.findByLugar( lugar ) );
                    itemVenda.setFkLugares( ( TbLugares ) lugaresController.findById( Integer.parseInt( lugar ) ) );
//                itemVenda.setFkLugares( lugarDao.findTbLugares( lugarDao.getIdByDescricao( lugar ) ) );
                    itemVenda.setFkMesas( ( TbMesas ) mesasController.findByDesignacao( mesa ) );

                    //cria o item venda
//                itemVendaDao.create ( itemVenda );
//                int last_venda = itemVendaDao.criarComProcedimentos( itemVenda, conexao );
                    if ( !itemVendasController.salvar( itemVenda ) )
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
//                stock_local = stockDao.get_stock_by_id_produto_and_id_armazem( idProduto, id_armazem );
//                stock_local = stockDao.get_stock_by_id_produto_and_id_armazem( itemVenda.getCodigoProduto().getCodigo(), id_armzem );
                    if ( !Objects.isNull( stock_local ) )
                    {
                        if ( getIdDocumento() == DOC_FACTURA_RECIBO_FR || getIdDocumento() == DOC_FACTURA_FT )
                        {
                            System.out.println( "passei quando é FR ou FT" );
                            //so retirar caso existir mesmo no armazém em questão.
                            if ( stock_local.getCodigo() != 0 && itemVenda.getCodigoProduto().getStocavel().equals( "true" ) )
                            {
                                actualizar_quantidade( itemVenda.getCodigoProduto().getCodigo(), itemVenda.getQuantidade() );
                            }
                        }

                    }

                }

            }
            catch ( Exception e )
            {
                e.printStackTrace();
                efectuada = false;
                JOptionPane.showMessageDialog( null, "Falha ao registrar o produto: " + itemVenda.getCodigoProduto().getCodigo() + " na Factura" );
                break;
            }
        }

        if ( efectuada )
        {
            alterar_status_pedido_lugar( Integer.parseInt( lugar ) );
            JOptionPane.showMessageDialog( null, "Factura efectuada com sucesso!.." );
            actualizar_lugar( Integer.parseInt( lugar ) );
            procedimento_mesas_livre();
            TbPedido pedido = pedidoDao.findTbPedido( pedidoDao.getLastPedidoByDefignacaoMesaFALSE( mesa ) );
            int fkLugar = lugaresController.getIdByDescricao( lugar );

            PedidoDao.eliminarPedidoByLugar( pedido, fkLugar, conexao ); // Elimina o pedido

            System.err.println( "Codigo Venda: " + venda.getCodigo() );
            System.err.println( "Venda: " + venda.getCodFact() );
            gorjeta = 0;

//            int numeroVias = 1;
            int numeroVias = ( int ) Double.parseDouble( spnCopia.getValue().toString() );
            for ( int i = 1; i <= numeroVias; i++ )
            {

                switch ( i )
                {
                    case 1:
                        ListaVendasMesas listaVenda1 = new ListaVendasMesas( venda.getCodigo(), abreviacao, cod_mesa, Integer.parseInt( lugar ), false, true, "Original" );
//                        ListaVendasMesas listaVenda1 = new ListaVendasMesas( venda.getCodigo(), abreviacao, cod_mesa, "", lugar, false, true, "Original" );
//                        ListaVendaPorMesas listaVenda1 = new ListaVendaPorMesas( venda.getCodigo(), abreviacao, false, true, "Original" );
                        break;
                    case 2:
                        ListaVendasMesas listaVenda2 = new ListaVendasMesas( venda.getCodigo(), abreviacao, cod_mesa, Integer.parseInt( lugar ), false, true, "Duplicado" );
                        break;
                    case 3:
                        ListaVendasMesas listaVenda3 = new ListaVendasMesas( venda.getCodigo(), abreviacao, cod_mesa, Integer.parseInt( lugar ), false, true, "Triplicado" );
                        break;
                }

            }
            PrincipalPedidosVisao.mesas_livres( getLabelMesaByMesa() );
            PrincipalPedidosVisao.pintar_mesas( getLabelMesaByMesa(), mesa );

//             actualizar();
//            JOptionPane.showMessageDialog( null, "Factura Convertida com sucesso!.." );
        }

    }

    public static void actualizar_quantidade( int cod, double quantidade )
    {

        System.err.println( "Entrei no actualizar quantidade" );
        String sql = "UPDATE tb_stock SET quantidade_existente =  " + ( getQuantidadeProduto( cod ) - quantidade ) + " WHERE cod_produto_codigo = " + cod + " AND  cod_armazem = " + id_armzem;
        System.out.println( "Quantidade   : " + quantidade );
        conexao.executeUpdate( sql );

    }

//    public static double getValor_entregue()  
//    {
//        return FormaPagamentoVisao.get_total_valor().doubleValue();
//    }
//
////    public static double getTroco()
////    {
////        return 0d;
////    }
//    public static double getTroco()
//    {
//        return CfMethods.parseMoedaFormatada( FormaPagamentoVisao.lb_troco.getText() );
//    }
    public static double getValor_entregue()
    {
        return 0d;
    }

//    public static double getTroco()
//    {
//        return 0d;
//    }
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

    public static double getTrocoLugar()
    {

        try
        {
            return CfMethods.parseMoedaFormatada( FormaPagamentoLugarVisao.lb_troco.getText() );
        }
        catch ( Exception e )
        {
        }

        return 0d;

    }

//    JTable jTable = new JTable()
//    {
//        public boolean isCellEditable( int r, int c )
//        {
//
//            if ( c == 1 )
//            {
//                return true;
//            }
//            return false;
//
//        }
//    };
    public static void setTotalGeral()
    {
        DefaultTableModel modelo = ( DefaultTableModel ) jTable1.getModel();
        double total_geral = 0;
        for ( int i = 0; i <= modelo.getRowCount() - 1; i++ )
        {
            total_geral += Double.parseDouble( modelo.getValueAt( i, 4 ).toString() );
        }
        txtTotalApagar.setText( CfMethods.formatarComoMoeda( total_geral ) );
    }

    public boolean campos_invalido_imprimir()
    {

        if ( getValor_entregue() < Double.parseDouble( txtTotalApagar.getText() ) )
        {
            JOptionPane.showMessageDialog( null, "O valor entregue tem quer ser maior ou igual ao Total a Pagar", "AVISO", JOptionPane.WARNING_MESSAGE );
//            txtValorEntregue.requestFocus();
            return true;
        }
        return false;
    }

    public static boolean registrar_forma_pagamento( int id_venda )
    {

        DefaultTableModel modelo = ( DefaultTableModel ) FormaPagamentoVisao.tabela_forma_pagamento.getModel();

        FormaPagamentoItem formaPagamentoItem;
        Contas contas;
        double troco = CfMethods.parseMoedaFormatada( FormaPagamentoVisao.lb_troco.getText() );
        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {

            formaPagamentoItem = new FormaPagamentoItem();
            Integer id_forma_pagamento = Integer.parseInt( modelo.getValueAt( i, 0 ).toString() );
            FormaPagamento formaPagamento = formaPagamentoController.findByDescrisao( modelo.getValueAt( i, 1 ).toString() );
            contas = ( Contas ) contaController.findById( formaPagamento.getFkContaAssociada() );

            String referencia = ( modelo.getValueAt( i, 2 ) != null ) ? modelo.getValueAt( i, 2 ).toString() : "n/a";
//            String valor = ( modelo.getValueAt( i, 3 ) != null ) ? modelo.getValueAt( i, 3 ).toString() : "0";
            String valor = ( !modelo.getValueAt( i, 3 ).toString().equals( "" ) ) ? modelo.getValueAt( i, 3 ).toString() : "0";

            formaPagamentoItem.setValor( new BigDecimal( valor ) );
            formaPagamentoItem.setReferencia( referencia );
            formaPagamentoItem.setTroco( new BigDecimal( troco ) );
            formaPagamentoItem.setValor_real(
                    formaPagamentoItem.getValor().subtract( formaPagamentoItem.getTroco() ) );
            formaPagamentoItem.setFkVenda( new TbVenda( id_venda ) );
            formaPagamentoItem.setFkFormaPagamento( new FormaPagamento( id_forma_pagamento ) );

            try
            {
                if ( !valor.equals( "0" ) )
                {
                    formaPagamentoItemController.salvar( formaPagamentoItem );

                    if ( Objects.nonNull( contas ) )
                    {
                        MetodosUtilTS.entradaTesouraria( contas,
                                lb_proximo_documento.getText(),
                                formaPagamento,
                                referencia,
                                new BigDecimal( valor ),
                                idUser,
                                usuariosController,
                                cmc,
                                conexao
                        );
                    }
                    troco = 0;

                }
            }
            catch ( Exception e )
            {
                return false;
            }
        }

        return true;
    }

    public static boolean registrar_forma_pagamento_lugar( int id_venda, int lugar )
    {

        DefaultTableModel modelo = ( DefaultTableModel ) FormaPagamentoPorLugaresVisao.tabela_forma_pagamento.getModel();

        FormaPagamentoItem formaPagamentoItem;
        Contas contas;
        double troco = CfMethods.parseMoedaFormatada( FormaPagamentoPorLugaresVisao.lb_troco.getText() );
        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {

            formaPagamentoItem = new FormaPagamentoItem();
            Integer id_forma_pagamento = Integer.parseInt( modelo.getValueAt( i, 0 ).toString() );
            FormaPagamento formaPagamento = formaPagamentoController.findByDescrisao( modelo.getValueAt( i, 1 ).toString() );
            contas = ( Contas ) contaController.findById( formaPagamento.getFkContaAssociada() );

            String referencia = ( modelo.getValueAt( i, 2 ) != null ) ? modelo.getValueAt( i, 2 ).toString() : "n/a";
//            String valor = ( modelo.getValueAt( i, 3 ) != null ) ? modelo.getValueAt( i, 3 ).toString() : "0";
            String valor = ( !modelo.getValueAt( i, 3 ).toString().equals( "" ) ) ? modelo.getValueAt( i, 3 ).toString() : "0";

            formaPagamentoItem.setValor( new BigDecimal( valor ) );
            formaPagamentoItem.setReferencia( referencia );
            formaPagamentoItem.setTroco( new BigDecimal( troco ) );
            formaPagamentoItem.setValor_real(
                    formaPagamentoItem.getValor().subtract( formaPagamentoItem.getTroco() ) );
            formaPagamentoItem.setFkVenda( new TbVenda( id_venda ) );
            formaPagamentoItem.setFkFormaPagamento( new FormaPagamento( id_forma_pagamento ) );

            try
            {
                if ( !valor.equals( "0" ) )
                {
                    formaPagamentoItemController.salvar( formaPagamentoItem );

                    if ( Objects.nonNull( contas ) )
                    {
                        MetodosUtilTS.entradaTesouraria( contas,
                                lb_proximo_documento.getText(),
                                formaPagamento,
                                referencia,
                                new BigDecimal( valor ),
                                idUser,
                                usuariosController,
                                cmc,
                                conexao
                        );
                    }
                    troco = 0;

                }
            }
            catch ( Exception e )
            {
                return false;
            }
        }

        return true;
    }

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
////            String valor = ( Objects.nonNull( modelo.getValueAt( i, 3 ) ) ) ? modelo.getValueAt( i, 3 ).toString() : "0";
//            String valor = ( !modelo.getValueAt( i, 3 ).equals( "" ) ) ? modelo.getValueAt( i, 3 ).toString() : "0";
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
//
//        }
//
//        return true;
//    }
//    public static boolean registrar_forma_pagamento_lugar( int id_venda, int lugar )
//    {
//
//        DefaultTableModel modelo = ( DefaultTableModel ) FormaPagamentoLugarVisao.tabela_forma_pagamento.getModel();
//
//        FormaPagamentoItem formaPagamentoItem;
//
//        for ( int i = 0; i < modelo.getRowCount(); i++ )
//        {
//
//            formaPagamentoItem = new FormaPagamentoItem();
//            Integer id_forma_pagamento = Integer.parseInt( modelo.getValueAt( i, 0 ).toString() );
//            String referencia = ( modelo.getValueAt( i, 2 ) != null ) ? modelo.getValueAt( i, 2 ).toString() : "n/a";
//            String valor = ( !modelo.getValueAt( i, 3 ).equals( "" ) ) ? modelo.getValueAt( i, 3 ).toString() : "0";
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
    public static void setTotalPagar()
    {

        DefaultTableModel modelo = ( DefaultTableModel ) jTable1.getModel();
        double total_pagar = 0;
        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {
            total_pagar += CfMethods.parseMoedaFormatada( modelo.getValueAt( i, 4 ).toString() );
        }
        txtTotalApagar.setText( CfMethods.formatarComoMoeda( total_pagar ) );

    }

    public static void setTotalPagarByLugar( int lugar )
    {

        DefaultTableModel modelo = ( DefaultTableModel ) jTable1.getModel();

        double total_pagar = 0;

        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {

            if ( lugaresController.getIdByDescricao( jTable1.getModel().getValueAt( i, 1 ).toString() ) == lugar )
            {
//                total_pagar += Double.parseDouble( String.valueOf( modelo.getValueAt( i, 4 ) ) );
                total_pagar += CfMethods.parseMoedaFormatada( modelo.getValueAt( i, 4 ).toString() );
            }

        }
        txtTotalApagar.setText( CfMethods.formatarComoMoeda( total_pagar ) );
//        txtTotalApagar.setText( String.valueOf( total_pagar ) );
//        txtValorEntregue.setText( txtTotalApagar.getText() );
//        valor_por_extenco();

    }

//    last_venda
//    private void procedimento_converter_factura()
//    {
//
//        if ( !campos_invalido_imprimir() )
//        {
//            TbVenda salvar_venda = salvar_venda();
//
//            if ( salvar_venda != null )
//            {
//                salvarItemvenda( salvar_venda );
//                remover_dados_tabela();
//            }
//            else
//            {
//                System.err.println( "  erro ao salvar venda!!!" );
//            }
//        }
//
//    }
//    public void setTotalPagar()
//    {
//
//        DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
//        double total_pagar = 0;
//        for ( int i = 0; i < modelo.getRowCount(); i++ )
//        {
////            total_pagar += Double.parseDouble( String.valueOf( modelo.getValueAt( i, 4 ) ) );
//            total_pagar += CfMethods.parseMoedaFormatada( modelo.getValueAt( i, 4 ).toString() );
//        }
////        txtTotalApagar.setText( String.valueOf( total_pagar ) );
//            txtTotalApagar.setText( CfMethods.formatarComoMoeda( total_pagar ) );
//        try
//        {
//            calcularTroco();
//
//        }
//        catch ( Exception e )
//        {
//        }
//
//    }
//z
//    public void setTotalPagar()
//    {
//
//        DefaultTableModel modelo = ( DefaultTableModel ) jTable1.getModel();
//        double total_pagar = 0;
//        for ( int i = 0; i < modelo.getRowCount(); i++ )
//        {
//            total_pagar += Double.parseDouble( String.valueOf( modelo.getValueAt( i, 4 ) ) );
//        }
//        txtTotalApagar.setText( String.valueOf( total_pagar ) );
//        try
//        {
//            getTroco();
//
//        }
//        catch ( Exception e )
//        {
//        }
//
//    }
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

//    private static void valor_por_extenco()
//    {
//        //lbValorPorExtenco.setText(    "São: "   +  MetodosUtil .valorPorExtenso(     Double.parseDouble(   txtTotalApagar.getText() )   ) );
//    }
    private static void remover_dados_tabela()
    {
        DefaultTableModel modelo = ( DefaultTableModel ) jTable1.getModel();
        modelo.setRowCount( 0 );
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

    private void procedimento_processar_pedidos()
    {
        int opcao = JOptionPane.showConfirmDialog( null, "Por Favor\nDeseja ver a Conta da Mesa Completa?" );
        //Mostra Pedidos Gerais ou seja Pedidos de todos lugares de uma Mesa
        if ( opcao == JOptionPane.YES_OPTION )
        {
            imprimirPedidos();
        }
        else if ( opcao == JOptionPane.NO_OPTION )
        {
            try
            {
                opcao = Integer.parseInt( JOptionPane.showInputDialog( null, "Por Favor\nQual lugar Deseja ver a Conta?" ) );
                if ( exite_pedido_lugar( opcao ) )
                {
                    //Mostra Pedidos Por TbLugares ou seja TbPedido de um lugar na Mesa
                    imprimirPedidosPorLugar( opcao );
                }
                else
                {
                    JOptionPane.showMessageDialog( null, "Não existe pedido neste lugar!", "AVISO", JOptionPane.WARNING_MESSAGE );
                }
            }
            catch ( Exception e )
            {
                e.printStackTrace();
                JOptionPane.showMessageDialog( null, "Erro: tem que ser um número." );
            }
        }
        else
        {
            JOptionPane.showMessageDialog( null, "Operação cancelada" );
        }
    }

//    //Lugar
    public static void salvarItemvendaLugar( int lugar )
    {
        DefaultTableModel modelo = ( DefaultTableModel ) jTable1.getModel();

        Integer cod_venda = vendasController.getLastVenda().getCodigo();
//        int cod_venda = vendaDao.getLastVenda();
        int cod_mesa = mesasDao.getIdByDescricao( mesa );
        boolean efectuada = true;
        TbVenda venda_local = vendasController.getVendas( cod_venda );
//ESTE
//        TbVenda venda_local = vendaDao.findTbVenda( cod_venda );
        TbItemVenda itemVenda = null;
        TbProduto produto;
//        TbStock stock_local;
        double total = 0;
        int valor_lugar_tabela = 0;

        for ( int i = 0; i < jTable1.getRowCount(); i++ )
        {
            try
            {
                valor_lugar_tabela = getLugarSelecionado( i );
                if ( valor_lugar_tabela == lugar )
                {

                    itemVenda = new TbItemVenda();
                    TbProduto produto_local = ( TbProduto ) produtosController.findByDesignacao( modelo.getValueAt( i, 2 ).toString() );

                    int idProduto = produtosController.findByDesignacao( modelo.getValueAt( i, 2 ).toString() ).getCodigo();
                    double qtd = Double.parseDouble( modelo.getValueAt( i, 3 ).toString() );
                    double preco_unitario = precoDao.findTbPreco( precoDao.getUltimoIdPrecoByIdProduto( idProduto, qtd ) ).getPrecoVenda().doubleValue();
                    double taxa = MetodosUtil.getTaxaPercantagem( idProduto );
                    double desconto = 0d;
                    double sub_total_iliquido = FinanceUtils.getValorComIVA( qtd, taxa, preco_unitario, desconto );

                    itemVenda.setCodigoProduto( produtosController.findByCod( idProduto ) );
                    itemVenda.setCodigoVenda( venda_local );
                    itemVenda.setQuantidade( qtd );
                    itemVenda.setDesconto( desconto );
                    itemVenda.setValorIva( new BigDecimal( taxa ).doubleValue() );
//                    itemVenda.setValorRetencao( 0d );
                    itemVenda.setDataServico( new Date() );
                    itemVenda.setMotivoIsensao( MetodosUtil.getMotivoIsensao( idProduto ) );
                    itemVenda.setCodigoIsensao( MetodosUtil.getCodigoRegime( idProduto ) );
                    itemVenda.setTotal( new BigDecimal( sub_total_iliquido ) );
                    itemVenda.setFkPreco( precoDao.findTbPreco( precoDao.getUltimoIdPrecoByIdProduto( itemVenda.getCodigoProduto().getCodigo(), itemVenda.getQuantidade() ) ) );

                    /*setando a mesa e lugar para cunprir a formalidade só aplica-se somente para resstauração*/
                    itemVenda.setFkLugares( ( TbLugares ) lugaresController.findById( lugar ) );
//                itemVenda.setFkLugares( lugarDao.findTbLugares( lugarDao.getIdByDescricao( lugar ) ) );
                    itemVenda.setFkMesas( ( TbMesas ) mesasController.findByDesignacao( mesa ) );

                    //cria o item venda
//                    itemVendaDao.create ( itemVenda );
                    if ( !itemVendasController.salvar( itemVenda ) )
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
//                if ( stock_local.getCodigo() != 0 && itemVenda.getCodigoProduto().getStocavel().equals( "true" ) )
//                {
//                    System.out.println( "chamei o actualizar quantidade" );
//                    MetodosUtil.subtrai_quantidade( idProduto, qtd, id_armazem, conexao );
//                }
                    if ( getIdDocumento() == DOC_FACTURA_RECIBO_FR || getIdDocumento() == DOC_FACTURA_FT )
                    {
                        System.out.println( "passei quando é FR ou FT" );
                        if ( !Objects.isNull( stock_local ) && isStocavel )
                        {
                            System.out.println( "chamei o actualizar quantidade" );
                            actualizar_quantidade( itemVenda.getCodigoProduto().getCodigo(), itemVenda.getQuantidade() );
                        }

                    }

//                    if ( stock_local.getCodigo() != 0 && itemVenda.getCodigoProduto().getStocavel().equals( "true" ) )
//                    {
//                        MetodosUtil.subtrai_quantidade( idProduto, qtd, id_armzem, conexao );
//                    }
                    ///
//                    if ( stock_local.getCodigo() != 0 && itemVenda.getCodigoProduto().getStocavel().equals( "true" ) )
//                    {
//                        actualizar_quantidade( itemVenda.getCodigoProduto().getCodigo(), itemVenda.getQuantidade() );
//                    }
                    //
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
//            DocumentoDao.commitTransaction( conexao );
            gorjeta = 0;
//          act actualizar_cod_doc ( venda_local.getDataVenda () );
            alterar_status_pedido_lugar( lugar );
            JOptionPane.showMessageDialog( null, "Factura Convertida com sucesso!.." );
            actualizar_lugar( lugar );
            procedimento_mesas_livre();

//            ListaVenda1 original = new ListaVenda1( last_cod, this.abreviacao, false, ck_simplificada.isSelected(), "Original", motivos_isentos )
            ListaVendasMesas listaVenda1 = new ListaVendasMesas( cod_venda, abreviacao, cod_mesa, lugar, false, true, "Duplicado" );
            ListaVendasMesas listaVenda2 = new ListaVendasMesas( cod_venda, abreviacao, cod_mesa, lugar, false, true, "Original" );
//            ListaVenda listaVenda1 = new ListaVenda( cod_venda, abreviacao, false, true, "Duplicado" );
//            ListaVenda listaVenda2 = new ListaVenda( cod_venda, abreviacao, false, true, "Original" );

//            ListaVenda listaVenda = new ListaVenda(cod_venda, cod_mesa, lugar, false, true, "Original");
//            ListaVenda listaVenda1 = new ListaVenda(cod_venda, cod_mesa, lugar, false, true, "Duplicado");
            //ListaVenda listaVenda1 = new ListaVenda(cod_venda, cod_mesa, lugar, false, true);
        }

    }

    public static double getTotal_por_lugar( int lugar )
    {

        DefaultTableModel modelo = ( DefaultTableModel ) jTable1.getModel();
        double total_local = 0d;
        for ( int i = 0; i < jTable1.getRowCount(); i++ )
        {

            int lugar_local = Integer.parseInt( modelo.getValueAt( i, 1 ).toString() );
            if ( lugar_local == lugar )
            {

                total_local += Double.parseDouble( modelo.getValueAt( i, 4 ).toString() );
            }

        }

        return total_local;
    }

//        public static void dispose() {
//        doDispose();
//    }
    public static void alterar_status_pedido_lugar( int lugar )
    {

        int cod_pedido = pedidoDao.getLastPedidoByDefignacaoMesaFALSE( mesa );
        List<TbItemPedidos> list = itemPedidosDao.buscaTodosItemPedidos( cod_pedido, lugar );

        for ( int i = 0; i < list.size(); i++ )
        {

            TbItemPedidos itemPedidos = list.get( i );
            itemPedidos.setStatusConvertido( true );

            try
            {
                itemPedidosDao.edit( itemPedidos );
                System.out.println( "1:linha do pedido convertido" );
            }
            catch ( Exception e )
            {
                System.out.println( "0:falha ao actualizar a linha do pedido" );
                e.printStackTrace();
            }

        }

    }

    private static void alterar_status_pedido_all_lugar()
    {

        int cod_pedido = pedidoDao.getLastPedidoByDefignacaoMesaFALSE( mesa );
        List<TbItemPedidos> list = itemPedidosDao.buscaTodosItemPedidos( cod_pedido );
        for ( int i = 0; i < list.size(); i++ )
        {

            TbItemPedidos itemPedidos = list.get( i );
            itemPedidos.setStatusConvertido( true );

            try
            {
                itemPedidosDao.edit( itemPedidos );
            }
            catch ( Exception e )
            {
            }
        }
    }

    public static boolean exite_pedido_lugar( int lugar )
    {

        boolean efectuada = false;
        for ( int i = 0; i < jTable1.getRowCount(); i++ )
        {
            if ( lugaresController.getIdByDescricao( jTable1.getModel().getValueAt( i, 1 ).toString() ) == lugar )
            {
                efectuada = true;
                break;
            }
        }
        return efectuada;
    }

//    public void setTotalPagarByLugar( int lugar )
//    {
//
//        DefaultTableModel modelo = ( DefaultTableModel ) jTable1.getModel();
//
//        double total_pagar = 0;
//
//        for ( int i = 0; i < modelo.getRowCount(); i++ )
//        {
//
//            if ( lugarDao.getIdByDescricao( jTable1.getModel().getValueAt( i, 1 ).toString() ) == lugar )
//            {
//                total_pagar += Double.parseDouble( String.valueOf( modelo.getValueAt( i, 4 ) ) );
//            }
//
//        }
//
//        txtTotalApagar.setText( String.valueOf( total_pagar ) );
////        txtValorEntregue.setText( txtTotalApagar.getText() );
//        valor_por_extenco();
//
//        try
//        {
//
//            getTroco();
//
//        }
//        catch ( Exception e )
//        {
//            //e.printStackTrace();
//        }
//
//    }
//    private void actualizar_campo( int qtd )
//    {
//
//        DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
//        TbItemPedidos itemPedidos = itemPedidosDao.findTbItemPedidos( Integer.parseInt( jTable1.getValueAt( this.linha_actual, 0 ).toString() ) );
//        itemPedidos.setQtd( qtd );
//
//        try
//        {
//            itemPedidosDao.edit( itemPedidos );
//            actualizar();
//        }
//        catch ( Exception e )
//        {
//        }
//
//    }
    private void adicionar_butons()
    {

    }

    private void adicionar_catgorias()
    {

        List<TbTipoProduto> lista_categoria = tipoProdutosController.buscaTodasCategoriasByArea();
//        List<TbTipoProduto> lista_categoria = tipoProdutoDao.getAll_filtro();
        this.TAMANHO_CENTRO = lista_categoria.size();

//        this.gl = new GridLayout( TAMANHO_CENTRO, 1 );
        int raiz_quadrada = ( int ) Math.sqrt( TAMANHO_CENTRO );

        int linhas = raiz_quadrada;
        int colunas = raiz_quadrada;

        painel_central.setLayout( new java.awt.GridLayout( linhas, colunas ) );
        painel_central.removeAll();
        jScrollPane3.setViewportView( painel_central );

        botoes_object.clear();
        btn_voltar.setVisible( false );
        designacao_categoria.setVisible( false );

        for ( int i = 0; i < TAMANHO_CENTRO; i++ )
        {

            JButton jButton = new JButton( lista_categoria.get( i ).getDesignacao() );
            jButton.addActionListener( new ButtonHandler1() );
            botoes_object.add( jButton );
            painel_central.add( jButton );

        }

    }

    private void adicionar_centro_botao( int idTipoPorduto )
    {
//        try
//        {
//
//            TbTipoProduto tipoProduto = tipoProdutosController.getTipoProdutoByCodigo( idTipoPorduto );
////            TbTipoProduto tipoProduto = tipoProdutoDao.findTbTipoProduto( idTipoPorduto );
//            btn_voltar.setVisible( true );
//            designacao_categoria.setVisible( true );
//            designacao_categoria.setText( tipoProduto.getDesignacao() );
//            System.err.println( "Cod Tipo de Produtos: "+tipoProduto.getDesignacao() );
//
//            List<TbProduto> lista_prdutos = ( tipoProduto.getFkFamilia().getPkFamilia() != DVML.COD_SERVICO ) ? stocksController.get_all_produtos_by_id_tipo_produto_and_id_armazem( idTipoPorduto, id_armzem ) : produtosController.getProdutosByTipoProduto(idTipoPorduto );
//            painel_central.removeAll();
//            jScrollPane3.setViewportView( painel_central );
//
//            botoes_object.clear();
//
//            if ( !Objects.isNull( lista_prdutos ) )
//            {
//
//                boolean adcionar_imagem = true;
//                this.TAMANHO_CENTRO = lista_prdutos.size();
//
//                int raiz_quadrada = ( int ) Math.sqrt( TAMANHO_CENTRO );
//                int linhas = raiz_quadrada;
//                int colunas = raiz_quadrada;
//
//                painel_central.setLayout( new java.awt.GridLayout( linhas, colunas ) );
//                jScrollPane3.setViewportView( painel_central );
//
//                double preco = 0;
//                TbPreco preco_object;
//                for ( int i = 0; i < this.TAMANHO_CENTRO; i++ )
//                {
//
//                    preco_object = precosController.getLastIdPrecoByIdProdutos( lista_prdutos.get( i ).getCodigo() );
//                    preco = ( preco_object != null ? preco_object.getPrecoVenda().doubleValue() : 0.0 );
//
//                    try
//                    {
//                        adcionar_imagem = true;
//                        imageIcon = new ImageIcon( lista_prdutos.get( i ).getPhoto() );
//                        imageIcon.setImage( imageIcon.getImage().getScaledInstance( 112, 109, 100 ) );
//                    }
//                    catch ( Exception e )
//                    {
//                        adcionar_imagem = false;
////                        e.printStackTrace();
//                    }
//
//                    JButton jButton = new JButton( lista_prdutos.get( i ).getDesignacao()
//                            + "-" + String.valueOf( preco )
//                            + "AKz"
//                    );
//                    botoes_object.add( jButton );
//
//                    if ( adcionar_imagem )
//                    {
//                        try
//                        {
//                            jButton.setIcon( imageIcon );
//                        }
//                        catch ( Exception e )
//                        {
//                            e.printStackTrace();
//                            jButton.setIcon( null );
//                        }
//                    }
//
//                    jButton.addActionListener( new ButtonHandler() );
//                    painel_central.add( jButton );
//
//                }
//
//            }
//
//        }
//        catch ( Exception e )
//        {
//            e.printStackTrace();
//            painel_central.removeAll();
//            botoes_object.clear();
//
//        }

        try
        {

            TbTipoProduto tipoProduto = tipoProdutosController.getTipoProdutoByCodigo( idTipoPorduto );
//            TbTipoProduto tipoProduto = tipoProdutoDao.findTbTipoProduto( idTipoPorduto );
            btn_voltar.setVisible( true );
            designacao_categoria.setVisible( true );
            designacao_categoria.setText( tipoProduto.getDesignacao() );
            System.err.println( "Cod Tipo de Produtos: " + tipoProduto.getDesignacao() );

//            Familia familia = familiaController.findById( 0 )
            System.out.println( " ID ARMAZEM: " + id_armzem );
            System.out.println( " FAMILIA: " + tipoProduto.getFkFamilia().getPkFamilia() );
            List<TbProduto> lista_prdutos = ( tipoProduto.getFkFamilia().getPkFamilia() != DVML.COD_SERVICO )
                    ? stocksController.get_all_produtos_by_id_tipo_produto_and_id_armazem( idTipoPorduto, id_armzem )
                    : produtosController.getProdutosByTipoProduto( idTipoPorduto );
            painel_central.removeAll();
            jScrollPane3.setViewportView( painel_central );

            TbUsuario usuarioLocal = ( TbUsuario ) usuariosController.findById( idUser );

            botoes_object.clear();
            this.TAMANHO_CENTRO = lista_prdutos.size();
            if ( !Objects.isNull( lista_prdutos ) && TAMANHO_CENTRO > 0 )
            {

                boolean adcionar_imagem = true;

                System.out.println( "TAMANHO CENTRO: " + TAMANHO_CENTRO );

                int raiz_quadrada = ( int ) Math.sqrt( TAMANHO_CENTRO );
                int linhas = raiz_quadrada;
                int colunas = raiz_quadrada;

                painel_central.setLayout( new java.awt.GridLayout( linhas, colunas ) );
                jScrollPane3.setViewportView( painel_central );

                double preco = 0;
                TbPreco preco_object;
                for ( int i = 0; i < this.TAMANHO_CENTRO; i++ )
                {
                    TbProduto get = lista_prdutos.get( i );

                    preco_object = precosController.getLastIdPrecoByIdProdutos( lista_prdutos.get( i ).getCodigo() );
                    double quantidadeProduto = stocksController.getQuantidadeProduto( get.getCodigo(), id_armzem );

//                    preco = ( preco_object != null ? preco_object.getPrecoVenda().doubleValue() : 0.0 );
//
//                    try
//                    {
//                        adcionar_imagem = true;
//                        imageIcon = new ImageIcon( lista_prdutos.get( i ).getPhoto() );
//                        imageIcon.setImage( imageIcon.getImage().getScaledInstance( 112, 109, 100 ) );
//                    }
//                    catch ( Exception e )
//                    {
//                        adcionar_imagem = false;
////                        e.printStackTrace();
//                    }
//
//                    JButton jButton = new JButton( lista_prdutos.get( i ).getDesignacao()
//                            + "-" + String.valueOf( preco )
//                            + "AKz"
//                    );
//                    botoes_object.add( jButton );
//
//                    if ( adcionar_imagem )
//                    {
//                        try
//                        {
//                            jButton.setIcon( imageIcon );
//                        }
//                        catch ( Exception e )
//                        {
//                            e.printStackTrace();
//                            jButton.setIcon( null );
//                        }
//                    }
//                    jButton.addActionListener( new ButtonHandler() );
//                    painel_central.add( jButton );
                    painel_central.add( new ProdutoItemVisao( get.getStocavel(), get.getDesignacao(), get.getPhoto(), quantidadeProduto, preco_object, DVML.FORMULARIO_PEDIDO_RESTAURANTE, usuarioLocal.getIdTipoUsuario().getIdTipoUsuario() ) );
//                    painel_central.add( new ProdutoItemVisao(  get.getCodigo(),  get.getStocavel(), get.getDesignacao(), get.getPhoto(), quantidadeProduto, preco_object, DVML.FORMULARIO_PEDIDO_RESTAURANTE, conexao ) );

                }

            }
            else
            {
                System.out.println( "LISTA DE PRODUTO VAZIA." );
            }

        }
        catch ( Exception e )
        {
            e.printStackTrace();
            painel_central.removeAll();
            botoes_object.clear();

        }

    }

    public class ButtonHandler implements ActionListener
    {

        @Override
        public void actionPerformed( ActionEvent evento )
        {

            for ( int i = 0; i < TAMANHO_CENTRO; i++ )
            {

                if ( evento.getSource() == botoes_object.get( i ) )
                {

                    String designacao = botoes_object.get( i ).getText();

                    String[] parts = designacao.split( "-" );
                    designacao = parts[ 0 ];

//                    if ( activo_um_lugar() && rbNao_lugar.isSelected() )
//                    {

                        if ( qtd_possivel( designacao ) )
                        {
                            try
                            {

                                procedimento_salvar_pedidos_iten_pedidos( designacao );
                                status_button_lugar( true );
                            }
                            catch ( Exception e )
                            {
                                JOptionPane.showMessageDialog( null, "Fallha ao adicionar o pedido", DVML.DVML_COMERCIAL, JOptionPane.ERROR_MESSAGE );
                                e.printStackTrace();
                            }
                        }
                        else
                        {
                            JOptionPane.showMessageDialog( null, "Nao existe quantidade no stock.", "Aviso", JOptionPane.WARNING_MESSAGE );
                        }

//                    }

                }

            }

        }

    }

    public class ButtonHandler1 implements ActionListener
    {

        @Override
        public void actionPerformed( ActionEvent evento )
        {

            for ( int i = 0; i < TAMANHO_CENTRO; i++ )
            {

                if ( evento.getSource() == botoes_object.get( i ) )
                {

                    String categoria = botoes_object.get( i ).getText();
                    System.out.println( "BOTOES :" + categoria );
//                    System.out.println( "ID : " + tipoProdutoDao.getCategoriaByDescricao( categoria ).getCodigo() );
//                    int cod_categoria = tipoProdutoDao.getCategoriaByDescricao( categoria ).getCodigo();
                    int cod_categoria = tipoProdutosController.getTipoFamiliaByDesignacao( categoria ).getCodigo();
                    adicionar_centro_botao( cod_categoria );
                    break;

                }

            }

        }

    }

    private void construir_painel_central()
    {
        painel_central.setLayout( new java.awt.GridLayout( 10, 1 ) );
        jScrollPane3.setViewportView( painel_central );
    }

    private void activar_button_lugar( boolean bt1, boolean bt2, boolean bt3, boolean bt4, boolean bt5, boolean bt6, boolean bt7, boolean bt8, boolean bt9, boolean bt10 )
    {

        status_button_lugar( true );
        btn_01.setEnabled( bt1 );
        btn_02.setEnabled( bt2 );
        btn_03.setEnabled( bt3 );
        btn_04.setEnabled( bt4 );
        btn_05.setEnabled( bt5 );
        btn_06.setEnabled( bt6 );
        btn_07.setEnabled( bt7 );
        btn_08.setEnabled( bt8 );
        btn_09.setEnabled( bt9 );
        btn_10.setEnabled( bt10 );

    }

    private static void status_button_lugar( boolean status )
    {
        btn_01.setEnabled( status );
        btn_02.setEnabled( status );
        btn_03.setEnabled( status );
        btn_04.setEnabled( status );
        btn_05.setEnabled( status );
        btn_06.setEnabled( status );
        btn_07.setEnabled( status );
        btn_08.setEnabled( status );
        btn_09.setEnabled( status );
        btn_10.setEnabled( status );

    }

//    private void procedimento_eliminar_item_pedido()
//    {
//
//        if ( validar1() )
//        {
//            eliminar_item( jTable1 );
//            PrincipalPedidosVisao.mesas_livres( getLabelMesaByMesa() );
//            PrincipalPedidosVisao.pintar_mesas( getLabelMesaByMesa(), this.mesa );
//        }
//
//    }
    public void procedimento_eliminar_item_pedido()
    {
        linha_acutal = jTable1.getSelectedRow();
        DefaultTableModel modelo = ( DefaultTableModel ) jTable1.getModel();
//        int id_item_consumo_alojamento = Integer.parseInt( modelo.getValueAt( linha_acutal, 0 ).toString() );

        try
        {

            new SenhaMestreVisao( this, rootPaneCheckingEnabled, DVML.FORMULARIO_PEDIDO_RESTAURANTE ).setVisible( true );
//                if ( validar1() )
//                {
//                    eliminar_item( jTable1 );
//                    PrincipalPedidosVisao.mesas_livres( getLabelMesaByMesa() );
//                    PrincipalPedidosVisao.pintar_mesas( getLabelMesaByMesa(), mesa );
//                }

//                ItemAlojamentoConsumo item = itemAlojamentoDao.findItemAlojamentoConsumo( id_item_consumo_alojamento );
//                //chamar o
//                itemAlojamentoDao.destroy( id_item_consumo_alojamento );
//                if ( item.getFkProduto().getStocavel().equals( "true" ) )
//                {
//                    MetodosUtil.adiciona_quantidade( item.getFkProduto().getCodigo(), item.getQtd(), DVML.ARMAZEM_DEFAUTL, conexao );
//                }
//                adicionar_consumos_tabela();
//                JOptionPane.showMessageDialog( null, "Produto removido com sucesso!...", DVML.DVML_COMERCIAL, JOptionPane.INFORMATION_MESSAGE );
        }
        catch ( Exception e )
        {
        }

    }

    public void procedimento_salver_item_pedido()
    {
        linha_acutal = jTable1.getSelectedRow();
        DefaultTableModel modelo = ( DefaultTableModel ) jTable1.getModel();
//        int id_item_consumo_alojamento = Integer.parseInt( modelo.getValueAt( linha_acutal, 0 ).toString() );
        try
        {

//            new SenhaMestreVisao( this, rootPaneCheckingEnabled ).setVisible( true );
//                if ( validar1() )
//                {
//                    eliminar_item( jTable1 );
//                    PrincipalPedidosVisao.mesas_livres( getLabelMesaByMesa() );
//                    PrincipalPedidosVisao.pintar_mesas( getLabelMesaByMesa(), mesa );
//                }
//                ItemAlojamentoConsumo item = itemAlojamentoDao.findItemAlojamentoConsumo( id_item_consumo_alojamento );
//                //chamar o
//                itemAlojamentoDao.destroy( id_item_consumo_alojamento );
//                if ( item.getFkProduto().getStocavel().equals( "true" ) )
//                {
//                    MetodosUtil.adiciona_quantidade( item.getFkProduto().getCodigo(), item.getQtd(), DVML.ARMAZEM_DEFAUTL, conexao );
//                }
//                adicionar_consumos_tabela();
//                JOptionPane.showMessageDialog( null, "Produto removido com sucesso!...", DVML.DVML_COMERCIAL, JOptionPane.INFORMATION_MESSAGE );
        }
        catch ( Exception e )
        {
        }

    }

    public static void procedimento_eliminar_item_pedido( String chave_mestre )
    {
        linha_actual = jTable1.getSelectedRow();
        DefaultTableModel modelo = ( DefaultTableModel ) jTable1.getModel();
        String lugar = modelo.getValueAt( linha_actual, 1 ).toString();
        String consumo = modelo.getValueAt( linha_actual, 2 ).toString();
        int idProduto = produtosController.getIdProduto( consumo );
        double qtd = Double.parseDouble( modelo.getValueAt( linha_actual, 3 ).toString() );
//ESTE
//        double preco_unitario = precoDao.findTbPreco( precoDao.getUltimoIdPrecoByIdProduto( idProduto, qtd ) ).getPrecoVenda().doubleValue();
        double preco_unitario = precosController.getLastIdPrecoByIdProduto( idProduto, qtd ).getPrecoVenda().doubleValue();
//        String usuario = usuarioDao.findTbUsuario( idUser ).getNome();
        String usuario = usuariosController.getUsuarioByCodigo( idUser ).getNome();

        String chave_mestre_local = dadosInstituicaoController.findByCodigo( 1 ).getChaveMestre();
//        String chave_mestre_local = dadosInstituicaoController.findByCodigo( 1 ).getChaveMestre();
        System.out.println( "Chave Apresentação1: " + chave_mestre );
        System.out.println( "Chave Apresentação2 sem chave: " + chave_mestre_local );
        if ( chave_mestre.equals( chave_mestre_local ) )
        {
            System.out.println( "Chave Apresentação2: " + chave_mestre );
            System.out.println( "Chave Real: " + chave_mestre );
            try
            {
                if ( validar1() )
                {
                    eliminar_item( jTable1 );
                    PrincipalPedidosVisao.mesas_livres( getLabelMesaByMesa() );
                    PrincipalPedidosVisao.pintar_mesas( getLabelMesaByMesa(), mesa );

                    Productos p = new Productos();
                    p.setConsumo( consumo );
                    p.setLugar( lugar );
                    p.setMesa( mesa );
                    p.setQtd( qtd );
                    p.setPreco( preco_unitario );
                    p.setUsuario( usuario );
                    p.setDataHora( new Date() );

                    if ( ProductoDao.insert( p, conexao ) )
                    {
                        System.err.println( "dados eliminado registrado com successo." );
                    }
                    else
                    {
                        System.err.println( "Falha ao registrar os dados eliminados" );
                    }
                }
            }
            catch ( Exception e )
            {
            }
        }
        else
        {
            JOptionPane.showMessageDialog( null, "Accesso negado." );
        }

    }

//    public static void procedimento_eliminar_item_pedido( String chave_mestre )
//    {
//        linha_acutal = jTable1.getSelectedRow();
//        DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
//        String chave_mestre_local;
//        chave_mestre_local = dadosInstituicaoDao.findTbDadosInstituicao( 1 ).getChaveMestre();
//
////        int id_item_consumo_alojamento = Integer.parseInt( modelo.getValueAt( linha_acutal, 0 ).toString() );
//        TbDadosInstituicao dadosInstituicao = new TbDadosInstituicao();
//        if ( chave_mestre.equals( chave_mestre_local ) )
//        {
//            try
//            {
//                if ( validar1() )
//                {
//                    eliminar_item( jTable1 );
//                    PrincipalPedidosVisao.mesas_livres( getLabelMesaByMesa() );
//                    PrincipalPedidosVisao.pintar_mesas( getLabelMesaByMesa(), mesa );
//                }
////                ItemAlojamentoConsumo item = itemAlojamentoDao.findItemAlojamentoConsumo( id_item_consumo_alojamento );
////                //chamar o
////                itemAlojamentoDao.destroy( id_item_consumo_alojamento );
//
////                if ( item.getFkProduto().getStocavel().equals( "true" ) )
////                {
////                    MetodosUtil.adiciona_quantidade( item.getFkProduto().getCodigo(), item.getQtd(), DVML.ARMAZEM_DEFAUTL, conexao );
////                }
////                adicionar_consumos_tabela();
////                JOptionPane.showMessageDialog( null, "Produto removido com sucesso!...", DVML.DVML_COMERCIAL, JOptionPane.INFORMATION_MESSAGE );
//            }
//            catch ( Exception e )
//            {
//            }
//        }
//        else
//        {
//            JOptionPane.showMessageDialog( null, "Accesso negado." );
//        }
//
//    }
//    public static void procedimento_gravar_item_pedido_eliminado()
//    {
//        linha_acutal = jTable1.getSelectedRow();
//        DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
//
////        int id_item_consumo_alojamento = Integer.parseInt( modelo.getValueAt( linha_acutal, 0 ).toString() );
//        try
//        {
//            if ( validar1() )
//            {
//                gravar_item_eliminado( jTable1 );
//
//            }
////                ItemAlojamentoConsumo item = itemAlojamentoDao.findItemAlojamentoConsumo( id_item_consumo_alojamento );
////                //chamar o
////                itemAlojamentoDao.destroy( id_item_consumo_alojamento );
//
////                if ( item.getFkProduto().getStocavel().equals( "true" ) )
////                {
////                    MetodosUtil.adiciona_quantidade( item.getFkProduto().getCodigo(), item.getQtd(), DVML.ARMAZEM_DEFAUTL, conexao );
////                }
////                adicionar_consumos_tabela();
////                JOptionPane.showMessageDialog( null, "Produto removido com sucesso!...", DVML.DVML_COMERCIAL, JOptionPane.INFORMATION_MESSAGE );
//        }
//        catch ( Exception e )
//        {
//        }
//
//    }
//    private static boolean activo_um_lugar()
//    {
//
//        if ( !btn_01.isEnabled() )
//        {
//            return true;
//        }
//        else if ( !btn_02.isEnabled() )
//        {
//            return true;
//        }
//        else if ( !btn_03.isEnabled() )
//        {
//            return true;
//        }
//        else if ( !btn_04.isEnabled() )
//        {
//            return true;
//        }
//        else if ( !btn_05.isEnabled() )
//        {
//            return true;
//        }
//        else if ( !btn_06.isEnabled() )
//        {
//            return true;
//        }
//        else if ( !btn_07.isEnabled() )
//        {
//            return true;
//        }
//        else if ( !btn_08.isEnabled() )
//        {
//            return true;
//        }
//        else if ( !btn_09.isEnabled() )
//        {
//            return true;
//        }
//        else if ( !btn_10.isEnabled() )
//        {
//            return true;
//        }
//
////        JOptionPane.showMessageDialog( rootPane, "Caro usuário seleccione pelo menos um lugar da " + mesa, DVML.DVML_COMERCIAL, JOptionPane.WARNING_MESSAGE );
//        JOptionPane.showMessageDialog( null, "Caro usuário seleccione pelo menos um lugar da " + mesa, DVML.DVML_COMERCIAL, JOptionPane.WARNING_MESSAGE );
//
//        return false;
//
//    }

    public static int getQuantidadeProduto( int cod_produto )
    {

        String sql = "SELECT quantidade_existente FROM  tb_stock WHERE  cod_produto_codigo = " + cod_produto + " AND cod_armazem = " + id_armzem;

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
//    public int getQuantidadeProduto( int cod_produto )
//    {
//
////        String sql = "SELECT quantidade_existente FROM  tb_stock WHERE  cod_produto_codigo = "  +cod_produto +" AND cod_armazem = " +getCodigoArmazem(); 
//        String sql = "SELECT quantidade_existente FROM  tb_stock WHERE  cod_produto_codigo = " + cod_produto;
//
//        ResultSet rs = conexao.executeQuery( sql );
//
//        try
//        {
//            if ( rs.next() )
//            {
//                return rs.getInt( "quantidade_existente" );
//            }
//        }
//        catch ( SQLException ex )
//        {
//            ex.printStackTrace();
//            return 0;
//        }
//
//        return 0;
//    }

    public void actualizar_quantidade( int cod, int quantidade, int idArmazem )
    {
//        public  void actualizar_quantidade(int cod, int quantidade) {

        String sql = "UPDATE tb_stock SET quantidade_existente =  " + ( getQuantidadeProduto( cod ) - quantidade ) + " WHERE cod_produto_codigo = " + cod + " AND cod_armazem = " + idArmazem;
//        String sql = "UPDATE tb_stock SET quantidade_existente =  "  + ( getQuantidadeProduto(cod) - quantidade)     +" WHERE cod_produto_codigo = "   +cod;

        conexao.executeUpdate( sql );

    }

//    public static boolean possivel_quantidade( int codigo_produto )
//    {
//
//        System.err.println( conexao.getQuantidade_Existente_Publico( codigo_produto, 1 ) );
//        // TbStock stock =  stockDao.getStockByDescricao(codigo_produto, 1 );
//        System.err.println( "ID ARMAZEM : " + id_armzem );
//        double quant_possivel = 0d;
//        try
//        {
//            quant_possivel = ( conexao.getQuantidade_Existente_Publico( codigo_produto, getCodigoArmazem() ) - conexao.getQuantidade_minima_publico( codigo_produto, getCodigoArmazem() ) );
//        }
//        catch ( Exception e )
//        {
//            e.printStackTrace();
//        }
//
//        // int quant_possivel = stock.getQuantidadeExistente() -  stock.getQuantBaixa();
//        System.out.println( "Quantidade Possivel: " + quant_possivel );
//        System.out.println( "Quantidade Itens Pedidos: " + MetodosUtil.getQtdInItemPedidos( conexao, codigo_produto ) );
//        return quant_possivel >= ( 1 + MetodosUtil.getQtdInItemPedidos( conexao, codigo_produto ) );
//
//    }
    public static boolean estado_critico( int codigo_produto ) throws SQLException
    {
        TbStock stock = stocksController.getStockByIdProdutoAndIdArmazem( codigo_produto, id_armzem );
        double qtd_minima = stock.getQuantBaixa(),
                qtd_existente = stock.getQuantidadeExistente(),
                qtd_critica = stock.getQuantCritica();

        return qtd_minima < qtd_existente
                && qtd_existente <= qtd_critica;

    }

    //AQUI
//    private boolean verificar_disponiblidade ( int lugar )
//    {
//
//        int codigo_produto = 0, opcao = 0, diferenca = 0;
//
//        for ( int i = 0; i < jTable1.getRowCount (); i ++ )
//        {
//
//            if ( lugarDao.getIdByDescricao ( jTable1.getModel ().getValueAt ( i, 1 ).toString () ) == lugar )
//            {
//                codigo_produto = Integer.parseInt ( String.valueOf ( jTable1.getModel ().getValueAt ( i, 0 ) ) );
//
//                if (  ! possivel_quantidade ( getQtdPedidosTabela ( codigo_produto ) ) )
//                {
//
//                    diferenca = ( MetodosUtil.getQtdInItemPedidos ( conexao, codigo_produto ) - qtd_diferenca_existente_baixa ( codigo_produto ) );
//                    remover_item_all_pedidos_amais_tabela ( codigo_produto, diferenca );
//                    opcao = JOptionPane.showConfirmDialog ( null, "De momento encontra-se apenas disponível a quantidade "
//                            + qtd_possivel_quantidade ( codigo_produto ) + " do produto " + produtoDao.findTbProduto ( codigo_produto ).getDesignacao () );
//                    return false;
//                }
//            }
//
//        }
//        return true;
//
//    }
    public int getQtdPedidosTabela( int cod_produto )
    {

        DefaultTableModel modelo = ( DefaultTableModel ) jTable1.getModel();
        int soma = 0;
        int cod_produto_local = 0;

        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {
            cod_produto_local = Integer.parseInt( modelo.getValueAt( i, 0 ).toString() );
            if ( cod_produto_local == cod_produto )
            {
                soma += Integer.parseInt( modelo.getValueAt( i, 3 ).toString() );
            }
        }

        return soma;

    }

    public void remover_item_all_pedidos_tabela( int cod_produto )
    {

        DefaultTableModel modelo = ( DefaultTableModel ) jTable1.getModel();
        int soma = 0;
        int cod_produto_local = 0;

        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {
            cod_produto_local = Integer.parseInt( modelo.getValueAt( i, 0 ).toString() );
            if ( cod_produto_local == cod_produto )
            {
                modelo.removeRow( i );
            }
        }

    }

    public double qtd_possivel_quantidade( int codigo_produto )
    {

        TbStock stock = stocksController.getStockByIdProdutoAndIdArmazem( codigo_produto, 1 );
        double quant_possivel = stock.getQuantidadeExistente() - stock.getQuantBaixa();

        return quant_possivel + ( 1 + MetodosUtil.getQtdInItemPedidos( conexao, codigo_produto ) );

    }

    public void remover_item_all_pedidos_amais_tabela( int cod_produto, int qtd )
    {

        DefaultTableModel modelo = ( DefaultTableModel ) jTable1.getModel();
        int cont = 1;
        int cod_produto_local = 0;

        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {
            cod_produto_local = Integer.parseInt( modelo.getValueAt( i, 0 ).toString() );
            if ( cod_produto_local == cod_produto && cont <= qtd )
            {
                modelo.removeRow( i );
                cont++;
            }
            else
            {
                break;
            }
        }

    }

//    private double qtd_diferenca_existente_baixa( int codigo_produto )
//    {
//        TbStock stock = stockDao.getStockByDescricao( codigo_produto, 1 );
//        return ( stock.getQuantidadeExistente() - stock.getQuantBaixa() );
//
//    }
    private static JLabel getLabelMesaByMesa()
    {

        if ( mesa.equals( "MESA 1" ) )
        {
            return PrincipalPedidosVisao.lb_mesa_01;
        }
        else if ( mesa.equals( "MESA 2" ) )
        {
            return PrincipalPedidosVisao.lb_mesa_02;
        }
        else if ( mesa.equals( "MESA 3" ) )
        {
            return PrincipalPedidosVisao.lb_mesa_03;
        }
        else if ( mesa.equals( "MESA 4" ) )
        {
            return PrincipalPedidosVisao.lb_mesa_04;
        }
        else if ( mesa.equals( "MESA 5" ) )
        {
            return PrincipalPedidosVisao.lb_mesa_05;
        }
        else if ( mesa.equals( "MESA 6" ) )
        {
            return PrincipalPedidosVisao.lb_mesa_06;
        }
        else if ( mesa.equals( "MESA 7" ) )
        {
            return PrincipalPedidosVisao.lb_mesa_07;
        }
        else if ( mesa.equals( "MESA 8" ) )
        {
            return PrincipalPedidosVisao.lb_mesa_08;
        }
        else if ( mesa.equals( "MESA 9" ) )
        {
            return PrincipalPedidosVisao.lb_mesa_09;
        }
        else if ( mesa.equals( "MESA 10" ) )
        {
            return PrincipalPedidosVisao.lb_mesa_10;
        }
        else if ( mesa.equals( "MESA 11" ) )
        {
            return PrincipalPedidosVisao.lb_mesa_11;
        }
        else if ( mesa.equals( "MESA 12" ) )
        {
            return PrincipalPedidosVisao.lb_mesa_12;
        }
        else if ( mesa.equals( "MESA 13" ) )
        {
            return PrincipalPedidosVisao.lb_mesa_13;
        }
        else if ( mesa.equals( "MESA 14" ) )
        {
            return PrincipalPedidosVisao.lb_mesa_14;
        }
        else if ( mesa.equals( "MESA 15" ) )
        {
            return PrincipalPedidosVisao.lb_mesa_15;
        }
        else if ( mesa.equals( "MESA 16" ) )
        {
            return PrincipalPedidosVisao.lb_mesa_16;
        }
        else if ( mesa.equals( "MESA 17" ) )
        {
            return PrincipalPedidosVisao.lb_mesa_17;
        }
        else if ( mesa.equals( "MESA 18" ) )
        {
            return PrincipalPedidosVisao.lb_mesa_18;
        }
        else if ( mesa.equals( "MESA 19" ) )
        {
            return PrincipalPedidosVisao.lb_mesa_19;
        }
        else if ( mesa.equals( "MESA 20" ) )
        {
            return PrincipalPedidosVisao.lb_mesa_20;
        }
        else if ( mesa.equals( "MESA 21" ) )
        {
            return PrincipalPedidosVisao.lb_mesa_21;
        }
        else if ( mesa.equals( "MESA 22" ) )
        {
            return PrincipalPedidosVisao.lb_mesa_22;
        }
        else if ( mesa.equals( "MESA 23" ) )
        {
            return PrincipalPedidosVisao.lb_mesa_23;
        }
        else if ( mesa.equals( "MESA 24" ) )
        {
            return PrincipalPedidosVisao.lb_mesa_24;
        }
        else if ( mesa.equals( "MESA 25" ) )
        {
            return PrincipalPedidosVisao.lb_mesa_25;
        }
        else if ( mesa.equals( "MESA 26" ) )
        {
            return PrincipalPedidosVisao.lb_mesa_26;
        }
        else if ( mesa.equals( "MESA 27" ) )
        {
            return PrincipalPedidosVisao.lb_mesa_27;
        }
        else if ( mesa.equals( "MESA 28" ) )
        {
            return PrincipalPedidosVisao.lb_mesa_28;
        }
        else if ( mesa.equals( "MESA 29" ) )
        {
            return PrincipalPedidosVisao.lb_mesa_29;
        }
        else if ( mesa.equals( "MESA 30" ) )
        {
            return PrincipalPedidosVisao.lb_mesa_30;
        }
        else if ( mesa.equals( "MESA 31" ) )
        {
            return PrincipalPedidosVisao.lb_mesa_31;
        }
        else if ( mesa.equals( "MESA 32" ) )
        {
            return PrincipalPedidosVisao.lb_mesa_32;
        }
        else if ( mesa.equals( "MESA 33" ) )
        {
            return PrincipalPedidosVisao.lb_mesa_33;
        }
        else if ( mesa.equals( "MESA 34" ) )
        {
            return PrincipalPedidosVisao.lb_mesa_34;
        }
        else if ( mesa.equals( "MESA 35" ) )
        {
            return PrincipalPedidosVisao.lb_mesa_35;
        }
        else if ( mesa.equals( "MESA 36" ) )
        {
            return PrincipalPedidosVisao.lb_mesa_36;
        }
        else if ( mesa.equals( "MESA 37" ) )
        {
            return PrincipalPedidosVisao.lb_mesa_37;
        }
        else if ( mesa.equals( "MESA 38" ) )
        {
            return PrincipalPedidosVisao.lb_mesa_38;
        }
        else if ( mesa.equals( "MESA 39" ) )
        {
            return PrincipalPedidosVisao.lb_mesa_39;
        }
        else
        {
            System.out.println( "CHEGUEI AQUI NA MESA 40." );
            return PrincipalPedidosVisao.lb_mesa_40;
        }

    }

    public static boolean validar1()
    {

        if ( jTable1.getModel().getRowCount() < 0 )
        {
            JOptionPane.showMessageDialog( null, "Atenção\nNão existe itens na tabela!" );
            return false;
        }
        return true;
    }

//    private boolean validar_cliente()
//    {
//        if ( cmbCliente.getSelectedIndex() == 0 )
//        {
//            JOptionPane.showMessageDialog( null, "Atenção\nSeleccione a Entidade ou o Cliente!..." );
//            return true;
//        }
//        else
//        {
//            return true;
//        }
//
//    }
//    private boolean validar_forma_pagamento()
//    {
//        if ( cmbFormaPagamento.getSelectedIndex() == 0 )
//        {
//            JOptionPane.showMessageDialog( null, "Atenção\nSeleccione a Forma de Pagamento!..." );
//            return true;
//        }
//        else
//        {
//            return true;
//        }
//
//    }
//    private Integer getIdBanco()
//    {
//        return bancoDao.getIdByDescricao( String.valueOf( cmbFormaPagamento.getSelectedItem() ) );
//    }
//    private void mostrar_proximo_codigo_documento ()
//    {
//        try
//        {
//            this.anoEconomico = anoEconomicoDao.getLastObject ();
//            this.documento = documentoDao.findDocumento ( DOC_FACTURA_RECIBO_FR );
//            this.doc_prox_cod = documento.getCodUltimoDoc () + 1;
//            prox_doc = documento.getAbreviacao ();
//            //FA Série / codigo
//            prox_doc += " " + this.anoEconomico.getSerie () + "/" + this.doc_prox_cod;
//            //lb_proximo_documento.setText( "PRÓXIMO DOC. :" + prox_doc );
//        }
//        catch ( Exception e )
//        {
//            e.printStackTrace ();
//            this.documento = null;
//            //lb_proximo_documento.setText( "" );
//        }
//    }
//    private void mostrar_proximo_codigo_documento()
//    {
//        try
//        {
//            this.documento = documentoDao.findDocumento( getIdDocumento() );
//            this.anoEconomico = anoEconomicoDao.findAnoEconomico( getIdAnoEconomico() );
//            // this.doc_prox_cod = documento.getCodUltimoDoc() + 1;
//            this.doc_prox_cod = vendaDao.getUltimaContagemByIdDocumentoAndAnoEconomico( getIdDocumento(), getIdAnoEconomico(), conexao ) + 1;
//            prox_doc = documento.getAbreviacao();
//            //FA Série / codigo
//            prox_doc += " " + this.anoEconomico.getSerie() + "/" + this.doc_prox_cod;
//            lb_proximo_documento.setText( "PRÓX.DOC. :" + prox_doc );
//        }
//        catch ( Exception e )
//        {
//            this.documento = null;
//            lb_proximo_documento.setText( "" );
//        }
//    }
    private void mostrar_proximo_codigo_documento()
    {
        try
        {
            this.documento = ( Documento ) documentosController.findById( getIdDocumento() );
            this.anoEconomico = ( AnoEconomico ) anoEconomicoController.findById( getIdAnoEconomico() );
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

//    public static int getIdDocumento()
//    {
//        try
//        {
//            return DVML.DOC_FACTURA_RECIBO_FR;
//        }
//        catch ( Exception e )
//        {
//            return 0;
//        }
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

    private static double getTotalIliquido()
    {
        DefaultTableModel modelo = ( DefaultTableModel ) jTable1.getModel();
        double qtd = 0;
        int idProduto = 0;
        double total_iliquido = 0, preco_unitario = 0;

        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {
            idProduto = produtosController.findByDesignacao( modelo.getValueAt( i, 2 ).toString() ).getCodigo();
//            idProduto = produtoDao.getProdutoByDescricao( modelo.getValueAt( i, 2 ).toString() ).getCodigo();
            qtd = Double.parseDouble( modelo.getValueAt( i, 3 ).toString() );
//ESTE
//            preco_unitario = precoDao.findTbPreco( precoDao.getUltimoIdPrecoByIdProduto( idProduto, qtd ) ).getPrecoVenda().doubleValue();
            preco_unitario = precosController.getLastIdPrecoByIdProduto( idProduto, qtd ).getPrecoVenda().doubleValue();
            System.err.println( "PU: " + preco_unitario );
            System.err.println( "QTD: " + qtd );
            total_iliquido += ( preco_unitario * qtd );

        }

        return total_iliquido;
    }

    private static double getTotalIliquido( int lugar )
    {
        DefaultTableModel modelo = ( DefaultTableModel ) jTable1.getModel();
        double qtd = 0;
        int idProduto = 0;
        double total_iliquido = 0, preco_unitario = 0;

        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {

            int valor_lugar_tabela = getLugarSelecionado( i );
            if ( valor_lugar_tabela == lugar )
            {

                idProduto = produtosController.findByDesignacao( modelo.getValueAt( i, 2 ).toString() ).getCodigo();
                qtd = Double.parseDouble( modelo.getValueAt( i, 3 ).toString() );
//                ESTE
//                preco_unitario = precoDao.findTbPreco( precoDao.getUltimoIdPrecoByIdProduto( idProduto, qtd ) ).getPrecoVenda().doubleValue();
                preco_unitario = precosController.getLastIdPrecoByIdProduto( idProduto, qtd ).getPrecoVenda().doubleValue();
                System.err.println( "PU: " + preco_unitario );
                System.err.println( "QTD: " + qtd );
                total_iliquido += ( preco_unitario * qtd );
            }
        }

        return total_iliquido;
    }

    private static double getDescontoComercial()
    {
        return 0d;
    }

    private static double getDescontoComercial( int lugar )
    {
        return 0d;
    }

    private static double getDescontoFinanceiro()
    {
        return 0d;
    }

    private static double getDescontoFinanceiro( int lugar )
    {
        return 0d;
    }

    private static double getTotalImposto()
    {
        DefaultTableModel modelo = ( DefaultTableModel ) jTable1.getModel();
        double qtd = 0;
        int idProduto = 0;
        double imposto = 0d, preco_unitario = 0d, desconto_valor_linha = 0;

        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {
            idProduto = produtosController.findByDesignacao( modelo.getValueAt( i, 2 ).toString() ).getCodigo();
            qtd = Double.parseDouble( modelo.getValueAt( i, 3 ).toString() );
            preco_unitario = precosController.getLastIdPrecoByIdProduto( idProduto, qtd ).getPrecoVenda().doubleValue();
            double valor_percentagem = 0d;
            double taxa = MetodosUtil.getTaxaPercantagem( idProduto );
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

    private static double getTotalImposto( int lugar )
    {
        DefaultTableModel modelo = ( DefaultTableModel ) jTable1.getModel();
        double qtd = 0;
        int idProduto = 0;
        double imposto = 0d, preco_unitario = 0d, desconto_valor_linha = 0;

        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {
            int valor_lugar_tabela = getLugarSelecionado( i );
            if ( valor_lugar_tabela == lugar )
            {

                idProduto = produtosController.findByDesignacao( modelo.getValueAt( i, 2 ).toString() ).getCodigo();
                qtd = Double.parseDouble( modelo.getValueAt( i, 3 ).toString() );
                preco_unitario = precosController.getLastIdPrecoByIdProduto( idProduto, qtd ).getPrecoVenda().doubleValue();
                double valor_percentagem = 0d;
                double taxa = MetodosUtil.getTaxaPercantagem( idProduto );
                // a incidência só é aplicável ao produtos sujeitos a iva 
                if ( taxa != 0 )
                {
                    double valor_unitario = ( preco_unitario * qtd );
                    desconto_valor_linha = valor_unitario * ( ( valor_percentagem ) / 100 );
                    imposto += ( ( valor_unitario - desconto_valor_linha ) * ( taxa / 100 ) );

                }
            }

        }

        return imposto;
    }

    private static double getTotalAOALiquido()
    {
        double valores = ( getTotalIliquido() + getTotalImposto() );
        double descontos = ( getDescontoComercial() + getDescontoFinanceiro() );
        System.out.println( "TotalIliquido: " + getTotalIliquido() );
        System.out.println( "TotalImposto: " + getTotalImposto() );
        System.out.println( "TotalDescontoComercial: " + getDescontoComercial() );
        System.out.println( "TotalDescontoFinanceiro: " + getDescontoFinanceiro() );
        System.out.println( "Total Liquido: " + ( valores - descontos ) );
        return ( valores - descontos );
    }

    private static double getTotalAOALiquido( int lugar )
    {
        double valores = ( getTotalIliquido( lugar ) + getTotalImposto( lugar ) );
        double descontos = ( getDescontoComercial( lugar ) + getDescontoFinanceiro( lugar ) );
        System.out.println( "TotalIliquido: " + getTotalIliquido( lugar ) );
        System.out.println( "TotalImposto: " + getTotalImposto( lugar ) );
        System.out.println( "TotalDescontoComercial: " + getDescontoComercial( lugar ) );
        System.out.println( "TotalDescontoFinanceiro: " + getDescontoFinanceiro( lugar ) );
        System.out.println( "Total Liquido: " + ( valores - descontos ) );
        return ( valores - descontos );
    }

    private static double getTotalIncidencia()
    {
        DefaultTableModel modelo = ( DefaultTableModel ) jTable1.getModel();
        double qtd = 0;
        int idProduto = 0;
        double incidencia = 0d, preco_unitario = 0d, desconto_valor_linha = 0, taxa = 0d;

        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {

            idProduto = produtosController.findByDesignacao( modelo.getValueAt( i, 2 ).toString() ).getCodigo();
            qtd = Double.parseDouble( modelo.getValueAt( i, 3 ).toString() );
            preco_unitario = precosController.getLastIdPrecoByIdProduto( idProduto, qtd ).getPrecoVenda().doubleValue();

            double valor_percentagem = 0d;
            taxa = MetodosUtil.getTaxaPercantagem( idProduto );
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

    private static double getTotalIncidencia( int lugar )
    {
        DefaultTableModel modelo = ( DefaultTableModel ) jTable1.getModel();
        double qtd = 0;
        int idProduto = 0;
        double incidencia = 0d, preco_unitario = 0d, desconto_valor_linha = 0, taxa = 0d;

        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {

            int valor_lugar_tabela = getLugarSelecionado( i );
            if ( valor_lugar_tabela == lugar )
            {
                idProduto = produtosController.findByDesignacao( modelo.getValueAt( i, 2 ).toString() ).getCodigo();
                qtd = Double.parseDouble( modelo.getValueAt( i, 3 ).toString() );
                preco_unitario = precosController.getLastIdPrecoByIdProduto( idProduto, qtd ).getPrecoVenda().doubleValue();

                double valor_percentagem = 0d;
                taxa = MetodosUtil.getTaxaPercantagem( idProduto );
                // a incidência só é aplicável ao produtos sujeitos a iva 
                if ( taxa != 0 )
                {
                    desconto_valor_linha = ( ( valor_percentagem ) / 100 );
                    double valor_unitario = ( preco_unitario * qtd );
                    incidencia += ( ( valor_unitario ) - ( valor_unitario * desconto_valor_linha ) );

                }
            }

        }

        return incidencia;
    }

    private static double getTotalIncidenciaIsento()
    {
        DefaultTableModel modelo = ( DefaultTableModel ) jTable1.getModel();
        double qtd = 0;
        int idProduto = 0;
        double incidencia_isento = 0d, preco_unitario = 0d, desconto_valor_linha = 0, taxa = 0;

        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {
            idProduto = produtosController.findByDesignacao( modelo.getValueAt( i, 2 ).toString() ).getCodigo();
            qtd = Double.parseDouble( modelo.getValueAt( i, 3 ).toString() );
            preco_unitario = precosController.getLastIdPrecoByIdProduto( idProduto, qtd ).getPrecoVenda().doubleValue();

            double valor_percentagem = 0d;
            taxa = MetodosUtil.getTaxaPercantagem( idProduto );
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

    private static double getTotalIncidenciaIsento( int lugar )
    {
        DefaultTableModel modelo = ( DefaultTableModel ) jTable1.getModel();
        double qtd = 0;
        int idProduto = 0;
        double incidencia_isento = 0d, preco_unitario = 0d, desconto_valor_linha = 0, taxa = 0;

        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {
            int valor_lugar_tabela = getLugarSelecionado( i );
            if ( valor_lugar_tabela == lugar )
            {
                idProduto = produtosController.findByDesignacao( modelo.getValueAt( i, 2 ).toString() ).getCodigo();
                qtd = Double.parseDouble( modelo.getValueAt( i, 3 ).toString() );
                preco_unitario = precosController.getLastIdPrecoByIdProduto( idProduto, qtd ).getPrecoVenda().doubleValue();

                double valor_percentagem = 0d;
                taxa = MetodosUtil.getTaxaPercantagem( idProduto );
                // a incidência também é aplicável à produtos isentos do iva 
                if ( taxa == 0 )
                {
                    desconto_valor_linha = ( ( valor_percentagem ) / 100 );
                    double valor_unitario = ( preco_unitario * qtd );
                    incidencia_isento += ( ( valor_unitario ) - ( valor_unitario * desconto_valor_linha ) );

                }
            }

        }

        return incidencia_isento;
    }

    private static double getGrossTotal()
    {
        System.out.println( "TOTALILIQUIDO: " + getTotalVendaIVASemIncluirDesconto() );
        System.out.println( "TOTALVENDAIVASEMDESCONTO: " + getTotalVendaIVASemIncluirDesconto() );
        return getTotalIliquido() + getTotalVendaIVASemIncluirDesconto();
    }

    private static double getGrossTotal( int lugar )
    {
        System.out.println( "TOTALILIQUIDO: " + getTotalVendaIVASemIncluirDesconto( lugar ) );
        System.out.println( "TOTALVENDAIVASEMDESCONTO: " + getTotalVendaIVASemIncluirDesconto( lugar ) );
        return getTotalIliquido( lugar ) + getTotalVendaIVASemIncluirDesconto( lugar );
    }

    private static double getTotalVendaIVASemIncluirDesconto()
    {
        double taxa = 0, total_iva_local = 0, preco_unitario = 0, sub_total_iliquido = 0;
        double qtd = 0;
        int idProduto = 0;

        DefaultTableModel modelo = ( DefaultTableModel ) jTable1.getModel();
        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {
            idProduto = produtosController.findByDesignacao( modelo.getValueAt( i, 2 ).toString() ).getCodigo();
            qtd = Double.parseDouble( modelo.getValueAt( i, 3 ).toString() );
            preco_unitario = precosController.getLastIdPrecoByIdProduto( idProduto, qtd ).getPrecoVenda().doubleValue();
            sub_total_iliquido = ( preco_unitario * qtd );
            taxa = MetodosUtil.getTaxaPercantagem( idProduto );
            total_iva_local += ( ( ( sub_total_iliquido ) * ( taxa / 100 ) ) );
        }

        return total_iva_local;
    }

    private static double getTotalVendaIVASemIncluirDesconto( int lugar )
    {
        double taxa = 0, total_iva_local = 0, preco_unitario = 0, sub_total_iliquido = 0;
        double qtd = 0;
        int idProduto = 0;

        DefaultTableModel modelo = ( DefaultTableModel ) jTable1.getModel();
        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {
            int valor_lugar_tabela = getLugarSelecionado( i );
            if ( valor_lugar_tabela == lugar )
            {
                idProduto = produtosController.findByDesignacao( modelo.getValueAt( i, 2 ).toString() ).getCodigo();
                qtd = Double.parseDouble( modelo.getValueAt( i, 3 ).toString() );
                preco_unitario = precosController.getLastIdPrecoByIdProduto( idProduto, qtd ).getPrecoVenda().doubleValue();
                sub_total_iliquido = ( preco_unitario * qtd );
                taxa = MetodosUtil.getTaxaPercantagem( idProduto );
                total_iva_local += ( ( ( sub_total_iliquido ) * ( taxa / 100 ) ) );
            }
        }

        return total_iva_local;
    }

//    private void actualizar_cod_doc( Date data_documento )
//    {
//        this.documento.setCodUltimoDoc( this.doc_prox_cod );
//        this.documento.setDescricaoUltimoDoc( this.prox_doc );
//        this.documento.setUltimaData( data_documento );
//        try
//        {
//            documentoDao.edit( documento );
//        }
//        catch ( Exception e )
//        {
//            System.err.println( "Falha ao actualizar o documento" );
//        }
//    }
    public static int getLugarSelecionado( int linha )
    {
        DefaultTableModel modelo = ( DefaultTableModel ) jTable1.getModel();
        String lugar_valor_tabela = modelo.getValueAt( linha, 1 ).toString();
        return lugaresController.getIdByDescricao( lugar_valor_tabela );
//        return lugarDao.getIdByDescricao( lugar_valor_tabela );

    }

    public static void verificarCaixa()
    {

        if ( !caixasController.existeCaixas() )
        {
//            BT_Pedidos.setEnabled( false );
            BT_Conversao.setEnabled( false );
            status_mensagem_primaria.setText( "" );
        }
        else if ( caixasController.existe_abertura() && caixasController.existe_fecho() )
        {
            BT_Conversao.setEnabled( false );
            BT_Conversao.setEnabled( false );
            status_mensagem_primaria.setText( "Deves abrir o caixa" );

        }
        else
        {
            BT_Conversao.setEnabled( true );
            BT_Conversao.setEnabled( true );
            status_mensagem_primaria.setText( "" );
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
            }

        } );

    }

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

//    private boolean data_documento_superior_ou_igual_ao_ultimo_doc()
//    {
//        //buscando o id do documento.
//        int pk_documento = getIdDocumento();
//        //buscando o id do ano ecoonomico.
//        int pk_ano_economico = getIdAnoEconomico();
//
//        //busca o último documento da série em questão.
//        // Integer cod_ultima_venda = vendaDao.getLastVenda( pk_documento );
//        Integer cod_ultima_venda = vendaDao.getLastVenda( pk_documento, pk_ano_economico );
//        if ( cod_ultima_venda != 0 )
//        {
//
//            //busca o objecto para retirar apenas a data do seu procesamento
//            TbVenda venda_local = vendaDao.findTbVenda( cod_ultima_venda );
//            //retirando a data do documebto
//            Date data_ultimo_documento = venda_local.getDataVenda();
//            //pegando a data do documento (data actual do sistema)
//            Date data_actual = dc_data_documento.getDate();
//            return MetodosUtil.maior_data_1_data_2( data_actual, data_ultimo_documento )
//                    || MetodosUtil.igual_data_1_data_2( data_actual, data_ultimo_documento );
//
//        }
//        else
//        {
//            return true;
//        }
//
//    }
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
//    private boolean verifica_ano_documento_igual_economico()
//    {
//        int ano_economico = Integer.parseInt( anoEconomicoDao.findAnoEconomico( getIdAnoEconomico() ).getDesignacao() );
//        int ano_documento = dc_data_documento.getDate().getYear() + 1900;
//        return ano_documento == ano_economico;
//
//    }
    private static boolean verifica_ano_documento_igual_economico()
    {
        AnoEconomico anoEconomicoLocal = ( AnoEconomico ) anoEconomicoController.findById( getIdAnoEconomico() );
        int ano_economico = Integer.parseInt( anoEconomicoLocal.getDesignacao() );
        int ano_documento = dc_data_documento.getDate().getYear() + 1900;
        return ano_documento == ano_economico;

    }

    private void mostrar_armazem()
    {
        TbArmazem armazem = armazensController.findByCodigo( id_armzem );

        if ( !Objects.isNull( armazem ) )
        {
            cmbArmazem.setSelectedItem( armazem.getDesignacao() );
        }
    }

//    public static int getCodigoArmazem()
//    {
//        //return conexao.getCodigoPublico("tb_armazem", String.valueOf(  cmbArmazem.getSelectedItem() ) );   
//        return armazemDao.getArmazemByDescricao( cmbArmazem.getSelectedItem().toString() ).getCodigo();
//    }
    public static int getCodigoArmazem()
    {
        return armazensController.getArmazemByDesignacao( cmbArmazem.getSelectedItem().toString() ).getCodigo();
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

//    private void setArmazem(String armazem) {
//        if (armazem.equalsIgnoreCase("Multi_armazem")) {
//            rbArmazem.setSelected(true);
//
//        } else {
//            rbArmazem1.setSelected(true);
//
//        }
//    }
//
//    public void configurar_armazens() {
//        setArmazem(dadosInstituicaoDao.findTbDadosInstituicao(1).getConfigArmazens());
//        try {
//            if (!rbArmazem.isSelected()) {
//                //                Caso for MultiArmazens
//                cmbArmazem.setModel(new DefaultComboBoxModel(armazemDao.buscaTodos2()));
////                cmbArmazem.setModel( new DefaultComboBoxModel( accessoArmazemDao.getAllArmazemByIdUSuario( cod_usuario ) ) );
//
//            } else if (rbArmazem.isSelected()) {
//                //                Caso for apenas Um Armazem
//                cmbArmazem.setModel(new DefaultComboBoxModel(accessoArmazemDao.getAllArmazemExceptoEconomatoByIdUSuario(idUser)));
//
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//    public static void setTotalGeral()
//    {
//        DefaultTableModel modelo = ( DefaultTableModel ) jTable1.getModel();
//        double total_geral = 0;
//        for ( int i = 0; i <= modelo.getRowCount() - 1; i++ )
//        {
//            total_geral += Double.parseDouble( modelo.getValueAt( i, 4 ).toString() );
//        }
//        lb_total_geral.setText( CfMethods.formatarComoMoeda( total_geral ) );
//    }
//    private String getCodDocActualizador()
//    {
//        try
//        {
//            this.documento = documentoDao.findDocumento( getIdDocumento() );
//            this.anoEconomico = anoEconomicoDao.findAnoEconomico( getIdAnoEconomico() );
//            // this.doc_prox_cod = documento.getCodUltimoDoc() + 1;
//            this.doc_prox_cod = vendaDao.getUltimaContagemByIdDocumentoAndAnoEconomico( getIdDocumento(), getIdAnoEconomico(), conexao ) + 1;
//            prox_doc = documento.getAbreviacao();
//            //FA Série / codigo
//            prox_doc += " " + this.anoEconomico.getSerie() + "/" + this.doc_prox_cod;
//            return prox_doc;
//        }
//        catch ( Exception e )
//        {
//            return "";
//        }
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

    private void setFolhaImpressora( String folha )
    {
        if ( folha.equalsIgnoreCase( "A6" ) )
        {
            ck_simplificada.setSelected( true );
            ck_S_A6.setSelected( false );
            ck_ComVirgula.setSelected( false );
            ck_A7.setSelected( false );
            this.abreviacao = Abreviacao.FR_A6;
        }
        else if ( folha.equalsIgnoreCase( "S_A6" ) )
        {
            ck_S_A6.setSelected( true );
            ck_simplificada.setSelected( false );
            ck_ComVirgula.setSelected( false );
            this.abreviacao = Abreviacao.FR_S_A6;
            ck_A7.setSelected( false );
        }
        else if ( folha.equalsIgnoreCase( "A7" ) )
        {
            ck_A7.setSelected( true );
            ck_S_A6.setSelected( false );
            ck_simplificada.setSelected( false );
            ck_ComVirgula.setSelected( false );
            this.abreviacao = Abreviacao.FR_SA7;
        }
        else
        {
            ck_ComVirgula.setSelected( true );
            ck_simplificada.setSelected( false );
            ck_S_A6.setSelected( false );
            ck_A7.setSelected( false );
            this.abreviacao = Abreviacao.FR_A6_Com_Virgula;
        }

    }

    public static void accao_codigo_interno_enter_busca_exterior( int codigo )
    {

        try
        {
//            int codigo = produtoDao.getIdByDescricao( designacao );

            String designacao1 = produtosController.getDescricaoById( codigo );
            System.out.println( "Codigo do Produto no Armazem 2" + codigo );
            System.out.println( "Designacao do Produto no Armazem 2" + designacao1 );
            //int   codigo = Integer.parseInt(txtCodigoProduto.getText() );
            TbProduto produto = produtosController.findByCod( codigo );
//            cmbFamilia.setSelectedItem( produto.getCodTipoProduto().getFkFamilia().getDesignacao() );
//            cmbSubFamilia.setSelectedItem( produto.getCodTipoProduto().getDesignacao() );
//            cmbProduto.setSelectedItem( produto.getDesignacao() );

            adicionar_preco_quantidade_anitgo();

            procedimento_adicionar( designacao1 );
//            }
//            txtCodigoProduto.setText( "" );
//            txtQuatindade.setText( "1" );
//            txtQuatindade.requestFocus();

        }
        catch ( Exception ex )
        {
            Logger.getLogger(VendaPraticaVisao.class.getName() ).log( Level.SEVERE, null, ex );
            JOptionPane.showMessageDialog( null, "Este produto não existe no armazém " + cmbArmazem.getSelectedItem(), DVML.DVML_COMERCIAL, JOptionPane.ERROR_MESSAGE );
        }

    }

    public static void adicionar_preco_quantidade_anitgo()
    {

        try
        {
//            if ( txtQuatindade.getText().isEmpty() )
//            {
//                JOptionPane.showMessageDialog( null, "Não informou a quantidade, por favor informe a quantidade!" );
//            }
//            else
//            {
            // TODO add your handling code here
//Mano
            //if(BDConexao.getCodigoByCodigo("tb_stock", "quantidade_existente", "cod_produto_codigo", getCodigoProduto())<=5)
            //if (conexao.getQtdExistenteStock(getCodigoProduto(), getCodigoArmazem()) <= conexao.getQtdCriticaStock(getCodigoProduto(), getCodigoArmazem())) {
            TbStock stock = stocksController.getStockByIdProdutoAndIdArmazem( getCodigoProduto(), getCodigoArmazem() );

            if ( stock != ( null ) )
            {

                txtQuatidadeExistente.setText( String.valueOf( conexao.getQtdExistenteStock( getCodigoProduto(), getCodigoArmazem() ) ) );

                System.err.println( "Codigo Produto:  " + getCodigoProduto() );
//                    System.err.println( "Qtd:  " + txtQuatindade.getText() );
                //txtPreco.setText(String.valueOf(MetodosUtil.retirar_dizimas(precoDao.findTbPreco(precoDao.getUltimoIdPrecoByIdProduto(getCodigoProduto(), Integer.parseInt(txtQuatindade.getText()))).getPrecoVenda())));
//                    txtPreco.setText( String.valueOf( MetodosUtil.retirar_dizimas( precoDao.findTbPreco( precoDao.getUltimoIdPrecoByIdProduto( getCodigoProduto(), Double.parseDouble( txtQuatindade.getText() ) ) ).getPrecoVenda().doubleValue() ) ) );
            }

//            }
        }
        catch ( Exception ex )
        {
            ex.printStackTrace();
            Logger.getLogger(VendaPraticaVisao.class.getName() ).log( Level.SEVERE, null, ex );
        }

    }

//        private void accao_codigo_manual_enter()
//    {
//        try
//        {
//            String codigo_manual = txtCodigoManual.getText();
//            TbProduto produtoLocal = produtosController.findByCodManual( codigo_manual );
//            procedimentoAdicionarTabela( produtoLocal );
//        }
//        catch ( Exception ex )
//        {
//            ex.printStackTrace();
//            Logger.getLogger( VendaUsuarioVisao.class.getName() ).log( Level.SEVERE, null, ex );
//            JOptionPane.showMessageDialog( null, "Não existe produto com este código de barra.", DVML.DVML_COMERCIAL, JOptionPane.ERROR_MESSAGE );
//        }
//
//    }
    public static void procedimento_adicionar( String designacao )
    {

        int codigo_produto = produtosController.getIdProduto( designacao );
        System.out.println( "Aqui vai o codigo" + codigo_produto );
        String designacao1 = produtosController.getDescricaoById( codigo_produto );
        System.out.println( "Aqui vai a designacao" + designacao1 );
        TbProduto produto_local = produtosController.findByCod( codigo_produto );

//        if ( activo_um_lugar() && rbNao_lugar.isSelected() )
//        {

            if ( qtd_possivel( designacao1 ) )
            {
                try
                {

                    procedimento_salvar_pedidos_iten_pedidos( designacao1 );
                    status_button_lugar( true );
                }
                catch ( Exception e )
                {
                    JOptionPane.showMessageDialog( null, "Fallha ao adicionar o pedido", DVML.DVML_COMERCIAL, JOptionPane.ERROR_MESSAGE );
                    e.printStackTrace();
                }
            }
            else
            {
                JOptionPane.showMessageDialog( null, "Atenção!!!\nNão existe quantidade no stock.", "Aviso", JOptionPane.WARNING_MESSAGE );
            }

//        }
//        else
//        {
//            JOptionPane.showMessageDialog( null, "Por Favor Digite a Quantidade" );
//        }
    }

//    public static boolean campos_invalidos()
//    {
//        return txtQuatindade.getText().equals( "" );
//    }
    private static boolean isProdutoExpirado( int codigoProduto )
    {
        // return produtoDao.produtoExpirado( codigoProduto );
        return false;
    }

    public static boolean estado_critico() throws SQLException
    {
        TbStock stock = stocksController.getStockByIdProdutoAndIdArmazem( getCodigoProduto(), getCodigoArmazem() );
        double qtd_minima = stock.getQuantBaixa(),
                qtd_existente = stock.getQuantidadeExistente(),
                qtd_critica = stock.getQuantCritica();

        return conexao.getQuantidade_minima_publico( getCodigoProduto(), getCodigoArmazem() )
                < conexao.getQuantidade_Existente_Publico( getCodigoProduto(), getCodigoArmazem() )
                && conexao.getQuantidade_Existente_Publico( getCodigoProduto(), getCodigoArmazem() )
                <= conexao.getQuantidade_critica_public( getCodigoProduto(), getCodigoArmazem() );
//   
//        return qtd_minima < qtd_existente
//                && qtd_existente <= qtd_critica;

    }

    private static boolean qtd_possivel( String designacao )
    {
//            TbDadosInstituicao dadosInstituicao = dadosInstituicaoDao.findTbDadosInstituicao( 1 );
        int idProduto = produtoDao.getIdByDescricao( designacao );
        TbProduto produto = produtoDao.findTbProduto( idProduto );

        if ( produto.getCodTipoProduto().getFkFamilia().getPkFamilia() == DVML.COD_SERVICO )
        {
            return true;
        }

        boolean isStocavel = produto.getStocavel().equals( "true" );

        if ( isStocavel )
        {
            double qtdExistentesNoutrosPedidos = ItemPedidosDao.getQtdItensByIdPedidoTradicional( idProduto, conexao );
            qtdExistentesNoutrosPedidos++;// Mais Um porque causa do novo pedido actual
            double qtdExistente = conexao.getQuantidade_Existente_Publico( idProduto, getCodigoArmazem() );

            return qtdExistente >= qtdExistentesNoutrosPedidos;

        }

        return true;

    }

//    private static boolean qtd_possivel( String designacao )
//    {
////            TbDadosInstituicao dadosInstituicao = dadosInstituicaoDao.findTbDadosInstituicao( 1 );
//        int idProduto = produtosController.getIdProduto(designacao );
//        TbProduto produto = produtosController.findByCod(idProduto );
//
//        if ( produto.getCodTipoProduto().getFkFamilia().getPkFamilia() == DVML.COD_SERVICO )
//        {
//            return true;
//        }
//
//        boolean isStocavel = produto.getStocavel().equals( "true" );
//
//        if ( isStocavel )
//        {
//            double qtdExistentesNoutrosPedidos = ItemPedidosDao.getQtdItensByIdPedidoTradicional( idProduto, conexao );
//            qtdExistentesNoutrosPedidos++;// Mais Um porque causa do novo pedido actual
//            double qtdExistente = conexao.getQuantidade_Existente_Publico( idProduto, getCodigoArmazem() );
//
//            return qtdExistente >= qtdExistentesNoutrosPedidos;
//
//        }
//
//        return true;
//
//    }
    public static void scrolltable()
    {

        jTable1.scrollRectToVisible( jTable1.getCellRect( jTable1.getRowCount() - 1, jTable1.getColumnCount(), true ) );

    }

    private void actualizarQtdTable()
    {

        int linhaSelecionada = jTable1.getSelectedRow();

        if ( linhaSelecionada > -1 )
        {
            int idItemPedidos = Integer.parseInt( jTable1.getValueAt( linhaSelecionada, 0 ).toString() );
            TbItemPedidos itemPedidosLocal = itemPedidosDao.findTbItemPedidos( idItemPedidos );

//            try
//            {
//                itemPedidosLocal.setQtd( 1 );
//                itemPedidosLocal.setTotalItem( itemPedidosLocal.getTotalItem() * 1 );
//                itemPedidosDao.edit( itemPedidosLocal );
//            }
//            catch ( Exception e )
//            {
//            }
            double qtd;

            try
            {
                qtd = Double.parseDouble( jTable1.getValueAt( linhaSelecionada, 3 ).toString() );
            }
            catch ( NumberFormatException e )
            {
                resetQtd( "Erro de formatação da quantidade.\nAtenção: Tem que ser número." );
                return;
            }

            if ( qtd <= 0 )
            {
                resetQtd( "Quantidade não pode ser zero(0) ou número négativo" );
                qtd = 1;
            }

            double preco = precosController.getLastIdPrecoByIdProdutos( itemPedidosLocal.getFkProdutos().getCodigo() ).getPrecoVenda().doubleValue();

            if ( !possivel_quantidade( itemPedidosLocal.getFkProdutos().getCodigo() ) ) //verifica se é possivel adicionar
            {
                resetQtd( "Impossivel para esta quantidade no stock" );
            }
            else if ( possivel_quantidade( itemPedidosLocal.getFkProdutos().getCodigo(), ( int ) qtd ) ) //verifica se  há disponiblidade ao  adicionar depois da  'qtd'  ser lançada.
            {
                itemPedidosLocal.setQtd( ( int ) qtd );
                itemPedidosLocal.setPreco( preco );
                itemPedidosLocal.setTotalItem( preco * qtd );
                try
                {
                    itemPedidosDao.edit( itemPedidosLocal );
                    actualizar();
                }
                catch ( Exception e )
                {
                }
            }
            else
            {
                resetQtd( "Impossivel para esta quantidade no stock" );
            }

        }

    }

    private void selecionar_documento()
    {

        if ( cmbTipoDocumento.getSelectedItem().equals( "Factura/Recibo" ) )
        {
            BT_Conversao.setVisible( true );
//            BtnFCons.setVisible( false );
            btFT.setVisible( false );
        }
        else if ( cmbTipoDocumento.getSelectedItem().equals( "Factura" ) )
        {
            btFT.setVisible( true );
//            BtnFCons.setVisible( false );
            BT_Conversao.setVisible( false );
        }
        else if ( cmbTipoDocumento.getSelectedItem().equals( "Consulta Mesa" ) )
        {
//            BtnFCons.setVisible( true );
            BT_Conversao.setVisible( false );
            btFT.setVisible( false );
        }

    }

    private void resetQtd( String msg )
    {
        jTable1.setValueAt( 1, jTable1.getSelectedRow(), 3 );
        JOptionPane.showMessageDialog( null, msg );
        jTable1.clearSelection();
    }

    public static void adicionarImportar( String designacao )
    {

//        if ( activo_um_lugar() )
//        {
        if ( qtd_possivel( designacao ) )
        {
            try
            {

                procedimento_salvar_pedidos_iten_pedidos( designacao );
                status_button_lugar( true );
            }
            catch ( Exception e )
            {
                JOptionPane.showMessageDialog( null, "Fallha ao adicionar o pedido", DVML.DVML_COMERCIAL, JOptionPane.ERROR_MESSAGE );
                e.printStackTrace();
            }
        }
        else
        {
            JOptionPane.showMessageDialog( null, "Nao existe quantidade no stock.", "Aviso", JOptionPane.WARNING_MESSAGE );
        }

//        }
    }

    private void visualizarCheckbox()
    {
        TbUsuario usuario = ( TbUsuario ) usuariosController.findById( idUser );

        if ( usuario.getIdTipoUsuario().getIdTipoUsuario() == 1 )
        {
            ck_S_A6.setVisible( true );
            ck_A7.setVisible( true );
            ck_ComVirgula.setVisible( true );
            ck_simplificada.setVisible( true );
        }
        else
        {
            ck_S_A6.setVisible( false );
            ck_A7.setVisible( false );
            ck_ComVirgula.setVisible( false );
            ck_simplificada.setVisible( false );
        }
    }

    private static void visualizarQtdStock()
    {
        TbUsuario usuario = ( TbUsuario ) usuariosController.findById( idUser );

        if ( usuario.getIdTipoUsuario().getIdTipoUsuario() == 1 )
        {
            txtQuatidadeExistente.setVisible( true );
        }
        else
        {
            txtQuatidadeExistente.setVisible( false );
        }
    }

//    public void setDesactivarLugares( String desactivar_lugares )
//    {
//        if ( desactivar_lugares.equalsIgnoreCase( "Sim" ) )
//        {
//            jCheckBox1.setSelected( true );
////            rbSim_lugar.setSelected( true );
////            rbNao_lugar.setSelected( false );
////            jLabel4.setEnabled( true );
//        }
//        else
//        {
//            jCheckBox1.setSelected( false );
////            rbNao_lugar.setSelected( true );
////            rbSim_lugar.setSelected( false );
////            jLabel4.setEnabled( false );
////            jPanelLugares.setVisible( true );
//        }
//    }
//
//    private void setDesactivarLugar( String desactivarLugar )
//    {
//
//        if ( desactivarLugar.equalsIgnoreCase( "Sim" ) )
//        {
//
//            rbLugarAcrbSimLugarted(false );
//            rbLugarDesactivo.setrbNao_lugar;
//
//        }
//        else
//        {
//
//            rbLugarActivrbSimLugar( true );
//            rbLugarDesactivo.setSelecrbNao_lugar       }
//
//    }
    private void desactivarLugares()
    {
        TbDadosInstituicao dados = ( TbDadosInstituicao ) dadosInstituicaoController.findById( 1 );

        if ( dados.getDesactivarLugares().equalsIgnoreCase( "Nao" ) )
        {
            rbNao_lugar.setSelected( true );
            rbSim_lugar.setSelected( false );
            jPanelLugares.setVisible( true );
        }
        else
        {
            rbSim_lugar.setSelected( true );
            rbNao_lugar.setSelected( false );
//            jLabel4.setVisible( false );
            jPanelLugares.setVisible( false );
        }
    }

    private void processar_ticket_geral()
    {
        TbPedido pedido_local = pedidoDao.findTbPedido( pedidoDao.getLastPedidoByDefignacaoMesaFALSE( mesa ) );
//        
//        TbProduto produtoLocal = ( TbProduto ) produtosController.findByCod(  );
        MetodosUtil.imprimir_geral_cozinha( pedido_local.getPkPedido(), dadosInstituicaoController );
        MetodosUtil.imprimir_geral_sala( pedido_local.getPkPedido(), dadosInstituicaoController );

//        imprimirPedidosTicket();
    }

}
