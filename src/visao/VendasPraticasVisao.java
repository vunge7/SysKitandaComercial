/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package visao;

//import comercial.visao.ProdutoItemVisao;
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
import comercial.controller.FichaTecnicaController;
import comercial.controller.FormaPagamentoController;
import comercial.controller.FormaPagamentoItemController;
import comercial.controller.GruposController;
import comercial.controller.ItemVendasController;
import comercial.controller.LinhaFichaTecnicaController;
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
import java.awt.Window;
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
import lista.ListaVenda1;
//import lista.ListaPedidos1;
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
import static visao.VendaUsuarioVisao.txtCodigoBarra;
import static visao.VendaUsuarioVisao.txtCodigoProduto;

/**
 *
 * @author Domingos Dala Vunge & Martinho Luís
 */
public class VendasPraticasVisao extends javax.swing.JFrame
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
    private static ContaController contaController;
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
    private static FichaTecnicaController fichaTecnicaController;
    private static LinhaFichaTecnicaController linhaFichaTecnicaController;
    private static TipoProdutosController tipoProdutosController;
    private static FormaPagamentoController formaPagamentoController;
    private static TbStock stock_local;

    private TbItemPedidos itemPedidos;
    private static MesasDao mesasDao = new MesasDao( emf );
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
    private static ContaMovimentosController cmc;
    private static int id_armzem;
    public static double gorjeta = 0;
    private static int GRUPO_AREA;
    private static AnoEconomico anoEconomico;
    private static BDConexao conexaoTransaction;

    /**
     * Creates new form GestaoItemPedidosVisao
     */
    public VendasPraticasVisao( String mesa, String lugar, int idUser, int id_armazem, BDConexao conexao )
    {

        initComponents();
        setLocationRelativeTo( null );
        cmbMoeda.setVisible( false );
        BtnFCons.setVisible( false );
//        cmbFamilia.setVisible( false );
        BT_Pedidos.setVisible( false );
        labelGrupo.setVisible( false );
        txtLocal.setVisible( false );
        cmb_area_venda_restaurante.setVisible( false );
        status_mensagem_primaria.setVisible( false );
        status_mensagem_secundaria.setVisible( false );
        //this.setExtendedState(   this.getExtendedState()|GestaoPedidosVisao.MAXIMIZED_BOTH  );   
        this.conexao = conexao;
//        this.GRUPO_AREA = GRUPO_AREA;
        this.idUser = idUser;
        this.id_armzem = id_armazem;
        this.mesa = "MESA " + mesa.trim();
        this.lugar = "LUGAR " + lugar.trim();
        txtIniciaisCliente.addKeyListener( new TratarEventoTeclado() );
        vendasController = new VendasController( VendasPraticasVisao.conexao );
        itemVendasController = new ItemVendasController( VendasPraticasVisao.conexao );
        mesasController = new MesasController( VendasPraticasVisao.conexao );
        lugaresController = new LugaresController( VendasPraticasVisao.conexao );
        produtosController = new ProdutosController( VendasPraticasVisao.conexao );
        stocksController = new StoksController( VendasPraticasVisao.conexao );
        precosController = new PrecosController( VendasPraticasVisao.conexao );
        cmc = new ContaMovimentosController( conexao );
        tipoProdutosController = new TipoProdutosController( VendasPraticasVisao.conexao );
        familiaController = new FamiliasController( VendasPraticasVisao.conexao );
        contaController = new ContaController( VendasPraticasVisao.conexao );
        armazensController = new ArmazensController( VendasPraticasVisao.conexao );
        localController = new LocalController( VendasPraticasVisao.conexao );
        unidadesController = new UnidadesController( VendasPraticasVisao.conexao );
        anoEconomicoController = new AnoEconomicoController( VendasPraticasVisao.conexao );
        clientesController = new ClientesController( VendasPraticasVisao.conexao );
        documentosController = new DocumentosController( VendasPraticasVisao.conexao );
        produtosImpostoController = new ProdutosImpostoController( VendasPraticasVisao.conexao );
        cambiosController = new CambiosController( VendasPraticasVisao.conexao );
        dadosInstituicaoController = new DadosInstituicaoController( VendasPraticasVisao.conexao );
        formaPagamentoItemController = new FormaPagamentoItemController( VendasPraticasVisao.conexao );
        formaPagamentoController = new FormaPagamentoController( VendasPraticasVisao.conexao );
        gruposController = new GruposController( VendasPraticasVisao.conexao );
        armazensAccessoController = new ArmazensAccessoController( VendasPraticasVisao.conexao );
        usuariosController = new UsuariosController( VendasPraticasVisao.conexao );
        moedasController = new MoedasController( VendasPraticasVisao.conexao );
        fichaTecnicaController = new FichaTecnicaController( VendasPraticasVisao.conexao );
        linhaFichaTecnicaController = new LinhaFichaTecnicaController( VendasPraticasVisao.conexao );
        dadosInstituicao = (TbDadosInstituicao) dadosInstituicaoController.findById( 1 );
        caixasController = new CaixasController( conexao );
        rbArmazem1.setVisible( false );
        rbArmazem.setVisible( false );
        caixaDao = new CaixaDao( emf );
//        txtMesa.setText( this.mesa );
        lbValorPorExtenco.setText( "" );

        cmbTipoDocumento.setModel( new DefaultComboBoxModel( documentosController.getVector() ) );

        cmbFamilia.setModel( new DefaultComboBoxModel( familiaController.getVector() ) );
        cmbSubFamilia.setModel( new DefaultComboBoxModel( tipoProdutosController.getVectorDesignacao() ) );
        cmbProduto.setModel( new DefaultComboBoxModel( produtosController.getVectorDesignacao() ) );
//        cmbMoeda.setModel(new DefaultComboBoxModel(moedasController.getVector()));

        cmbAnoEconomico.setModel( new DefaultComboBoxModel( anoEconomicoController.getVector() ) );
        cmbMoeda.setModel( new DefaultComboBoxModel( moedasController.getVector() ) );
        cmbCliente.setModel( new DefaultComboBoxModel( clientesController.getVector() ) );
        cmbCliente.setSelectedItem( DVML._CLIENTE_CONSUMIDOR_FINAL );

        try
        {
            setDocpadrao( dadosInstituicao.getDocpadrao() );
            setFolhaImpressora( dadosInstituicao.getImpressora() );
            setDesactivarvias( dadosInstituicao.getDesactivarvias() );
            int numero_copia = dadosInstituicao.getNumeroVias();
            spnCopia.setModel( CfMethodsSwing.criarSpinnerDoubleModel( 1, 3, numero_copia ) );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }

        try
        {
            configurar_armazens();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
        setArmazem( dadosInstituicao.getConfigArmazens() );
//        Espera
        jPanel1.setLayout( new AbsoluteLayout() );
        adicionar_catgorias();

        try
        {

            Integer last_pedido = pedidoDao.getUltimoPedido();
            System.err.println( "LAST ID PEDIDO ############ " + last_pedido );
            TbPedido pedido_local1 = pedidoDao.findTbPedido( last_pedido );
            if ( Objects.nonNull( pedido_local1 ) )
            {
                if ( !pedido_local1.getFacturado().equals( DVML.STATUS_FACTURADO_ESPERA ) )
                {
                    setSalvarPedidos();
                }

            }
            else
            {
                setSalvarPedidos();
            }

        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
        actualizar();
//        actualizar( produtos );
        adicionar_lugares();
        dc_data_documento.setDate( new Date() );
//        jPanel6.setVisible( true );
        jPanel4.setVisible( true );
//        mostrar_proximo_codigo_documento();
//        verificarCaixa();

        try
        {
            lbUsuario.setText( usuariosController.getUsuarioByCodigo( this.idUser ).getNome() );
        }
        catch ( Exception e )
        {
        }
        setWindowsListener();
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
    @SuppressWarnings( "unchecked" )
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        buttonGroup3 = new javax.swing.ButtonGroup();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        btn_voltar = new javax.swing.JButton();
        designacao_categoria = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        cmbCliente = new javax.swing.JComboBox<>();
        txtNomeConsumidorFinal = new javax.swing.JTextField();
        lbClienteConsumidorFinal = new javax.swing.JLabel();
        btCliente = new javax.swing.JButton();
        lbClienteConsumidorFinal1 = new javax.swing.JLabel();
        txtIniciaisCliente = new javax.swing.JTextField();
        lbClienteConsumidorFinal2 = new javax.swing.JLabel();
        txtNifClientePesquisa = new javax.swing.JTextField();
        txtCodClientePesquisa = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        BT_Pedidos = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        BtnFCons = new javax.swing.JButton();
        rbArmazem1 = new javax.swing.JRadioButton();
        rbArmazem = new javax.swing.JRadioButton();
        labelGrupo = new javax.swing.JLabel();
        btFT = new javax.swing.JButton();
        btnFormaPagamentoVendasPraticas = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        lbQuantidadeExistente = new javax.swing.JLabel();
        txtQuantidadeStock = new javax.swing.JTextField();
        status_mensagem_secundaria = new javax.swing.JLabel();
        status_mensagem_primaria = new javax.swing.JLabel();
        cmbAnoEconomico = new javax.swing.JComboBox<>();
        lb_proximo_documento = new javax.swing.JLabel();
        cmbArmazem = new javax.swing.JComboBox<>();
        lbVias = new javax.swing.JLabel();
        spnCopia = new javax.swing.JSpinner();
        jButton4 = new javax.swing.JButton();
        lbValorPorExtenco = new javax.swing.JLabel();
        cmbMoeda = new javax.swing.JComboBox();
        cmb_area_venda_restaurante = new javax.swing.JComboBox<>();
        cmbTipoDocumento = new javax.swing.JComboBox();
        ck_simplificada = new javax.swing.JCheckBox();
        ck_ComVirgula = new javax.swing.JCheckBox();
        ck_S_A6 = new javax.swing.JCheckBox();
        ck_A7 = new javax.swing.JCheckBox();
        cmbFamilia = new javax.swing.JComboBox<>();
        cmbSubFamilia = new javax.swing.JComboBox();
        cmbProduto = new javax.swing.JComboBox();
        txtPreco = new javax.swing.JTextField();
        txtCodigoBarra = new javax.swing.JTextField();
        txtLocal = new javax.swing.JTextField();
        txtQuantidade = new javax.swing.JTextField();
        btn_adicionar = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        txtCodigoProduto = new javax.swing.JTextField();
        lbUsuario = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtTotalApagar = new javax.swing.JTextField();
        dc_data_documento = new com.toedter.calendar.JDateChooser();
        dc_data_vencimento = new com.toedter.calendar.JDateChooser();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("VENDA EXPRESSO");

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel4.setEnabled(false);
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jScrollPane3.setViewportView(jPanel1);

        jPanel4.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(7, 46, 610, 520));

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
        jPanel4.add(designacao_categoria, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 10, 540, -1));

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
        btCliente.setToolTipText("Cadastre Novo Cliente!");
        btCliente.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btClienteActionPerformed(evt);
            }
        });

        lbClienteConsumidorFinal1.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbClienteConsumidorFinal1.setText("Pesq. Clientes:");

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

        lbClienteConsumidorFinal2.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbClienteConsumidorFinal2.setText("NIF:");

        txtNifClientePesquisa.setToolTipText("Insira novo NIF ou Pesquise NIF!");
        txtNifClientePesquisa.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtNifClientePesquisaActionPerformed(evt);
            }
        });
        txtNifClientePesquisa.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtNifClientePesquisaKeyPressed(evt);
            }
        });

        txtCodClientePesquisa.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtCodClientePesquisaActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        jLabel5.setText("Cod.");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lbClienteConsumidorFinal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtNomeConsumidorFinal, javax.swing.GroupLayout.DEFAULT_SIZE, 226, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtNifClientePesquisa)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(lbClienteConsumidorFinal2, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(lbClienteConsumidorFinal1, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtIniciaisCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtCodClientePesquisa, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(cmbCliente, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(73, 73, 73))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txtIniciaisCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtCodClientePesquisa, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(lbClienteConsumidorFinal1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(11, 11, 11)
                        .addComponent(cmbCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btCliente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbClienteConsumidorFinal, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbClienteConsumidorFinal2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtNifClientePesquisa, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNomeConsumidorFinal, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        BT_Pedidos.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        BT_Pedidos.setText("Ver Conta");
        BT_Pedidos.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                BT_PedidosActionPerformed(evt);
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

        BtnFCons.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/1588_32x32.png"))); // NOI18N
        BtnFCons.setText("Consulta");
        BtnFCons.setEnabled(false);
        BtnFCons.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                BtnFConsActionPerformed(evt);
            }
        });

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
        rbArmazem.setSelected(true);
        rbArmazem.setEnabled(false);
        rbArmazem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                rbArmazemActionPerformed(evt);
            }
        });

        labelGrupo.setText("jLabel8");

        btFT.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btFT.setText("Processar");
        btFT.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btFTActionPerformed(evt);
            }
        });

        btnFormaPagamentoVendasPraticas.setFont(new java.awt.Font("Lucida Grande", 0, 12)); // NOI18N
        btnFormaPagamentoVendasPraticas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/1323444801_currency_dollar red.png"))); // NOI18N
        btnFormaPagamentoVendasPraticas.setText("Pagamento");
        btnFormaPagamentoVendasPraticas.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnFormaPagamentoVendasPraticasActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(rbArmazem1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(rbArmazem)
                    .addComponent(labelGrupo, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(BT_Pedidos, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(BtnFCons, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnFormaPagamentoVendasPraticas, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btFT, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(rbArmazem1)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(rbArmazem, javax.swing.GroupLayout.PREFERRED_SIZE, 9, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labelGrupo, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(BT_Pedidos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(BtnFCons, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jButton2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btFT)
                            .addComponent(btnFormaPagamentoVendasPraticas))))
                .addContainerGap())
        );

        jPanel5Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btFT, btnFormaPagamentoVendasPraticas});

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "ID", "", "Consumo", "QTD", "Total"
            }
        )
        {
            Class[] types = new Class []
            {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Integer.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean []
            {
                false, false, false, true, false
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
            jTable1.getColumnModel().getColumn(0).setResizable(false);
            jTable1.getColumnModel().getColumn(0).setPreferredWidth(120);
            jTable1.getColumnModel().getColumn(1).setResizable(false);
            jTable1.getColumnModel().getColumn(1).setPreferredWidth(0);
            jTable1.getColumnModel().getColumn(2).setResizable(false);
            jTable1.getColumnModel().getColumn(2).setPreferredWidth(400);
            jTable1.getColumnModel().getColumn(3).setResizable(false);
            jTable1.getColumnModel().getColumn(3).setPreferredWidth(60);
            jTable1.getColumnModel().getColumn(4).setResizable(false);
            jTable1.getColumnModel().getColumn(4).setPreferredWidth(140);
        }

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 474, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 20, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 403, Short.MAX_VALUE)
                .addContainerGap())
        );

        lbQuantidadeExistente.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lbQuantidadeExistente.setText("Stock :");

        txtQuantidadeStock.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N
        txtQuantidadeStock.setEnabled(false);

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

        lb_proximo_documento.setFont(new java.awt.Font("Lucida Grande", 1, 10)); // NOI18N
        lb_proximo_documento.setText(" XX PP/A1");

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

        cmb_area_venda_restaurante.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        cmb_area_venda_restaurante.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Restaurante", "Alojamento" }));
        cmb_area_venda_restaurante.setEnabled(false);

        cmbTipoDocumento.setBackground(new java.awt.Color(0, 51, 102));
        cmbTipoDocumento.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        cmbTipoDocumento.setForeground(new java.awt.Color(255, 255, 255));
        cmbTipoDocumento.setOpaque(true);
        cmbTipoDocumento.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbTipoDocumentoActionPerformed(evt);
            }
        });

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

        cmbFamilia.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        cmbFamilia.setForeground(new java.awt.Color(255, 255, 255));
        cmbFamilia.setEnabled(false);
        cmbFamilia.setOpaque(true);
        cmbFamilia.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbFamiliaActionPerformed(evt);
            }
        });

        cmbSubFamilia.setBackground(new java.awt.Color(0, 51, 102));
        cmbSubFamilia.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        cmbSubFamilia.setForeground(new java.awt.Color(255, 255, 255));
        cmbSubFamilia.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbSubFamilia.setOpaque(true);
        cmbSubFamilia.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbSubFamiliaActionPerformed(evt);
            }
        });

        cmbProduto.setBackground(new java.awt.Color(0, 51, 102));
        cmbProduto.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        cmbProduto.setForeground(new java.awt.Color(255, 255, 255));
        cmbProduto.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbProduto.setOpaque(true);
        cmbProduto.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbProdutoActionPerformed(evt);
            }
        });

        txtPreco.setEditable(false);
        txtPreco.setBackground(new java.awt.Color(0, 51, 102));
        txtPreco.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txtPreco.setForeground(new java.awt.Color(255, 255, 255));
        txtPreco.setCaretColor(new java.awt.Color(255, 255, 255));
        txtPreco.setOpaque(true);
        txtPreco.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtPrecoActionPerformed(evt);
            }
        });

        txtCodigoBarra.setBackground(new java.awt.Color(0, 51, 102));
        txtCodigoBarra.setForeground(new java.awt.Color(255, 255, 255));
        txtCodigoBarra.setOpaque(true);
        txtCodigoBarra.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtCodigoBarraActionPerformed(evt);
            }
        });

        txtQuantidade.setBackground(new java.awt.Color(0, 51, 102));
        txtQuantidade.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        txtQuantidade.setForeground(new java.awt.Color(255, 255, 255));
        txtQuantidade.setText("0");
        txtQuantidade.setCaretColor(new java.awt.Color(255, 255, 255));
        txtQuantidade.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtQuantidadeActionPerformed(evt);
            }
        });

        btn_adicionar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/Button-Add-icon.png"))); // NOI18N
        btn_adicionar.setToolTipText("click para adicionar no carrinho");
        btn_adicionar.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btn_adicionarActionPerformed(evt);
            }
        });

        jLabel2.setText("Código");

        txtCodigoProduto.setBackground(new java.awt.Color(0, 51, 102));
        txtCodigoProduto.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        txtCodigoProduto.setForeground(new java.awt.Color(255, 255, 255));
        txtCodigoProduto.setCaretColor(new java.awt.Color(255, 255, 255));
        txtCodigoProduto.setOpaque(true);
        txtCodigoProduto.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtCodigoProdutoActionPerformed(evt);
            }
        });

        lbUsuario.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbUsuario.setForeground(new java.awt.Color(102, 102, 102));
        lbUsuario.setText(".");

        jLabel3.setText("QTD:");

        jLabel4.setText("Cód.Barra");

        jLabel1.setFont(new java.awt.Font("Lucida Grande", 1, 12)); // NOI18N
        jLabel1.setText("Valor Pagar");

        txtTotalApagar.setEditable(false);
        txtTotalApagar.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        txtTotalApagar.setForeground(new java.awt.Color(255, 51, 51));
        txtTotalApagar.setText("0");

        dc_data_documento.setFont(new java.awt.Font("Lucida Grande", 1, 12)); // NOI18N

        dc_data_vencimento.setFont(new java.awt.Font("Lucida Grande", 1, 12)); // NOI18N

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtTotalApagar, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dc_data_documento, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dc_data_vencimento, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtTotalApagar, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dc_data_vencimento, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dc_data_documento, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(lbValorPorExtenco, javax.swing.GroupLayout.PREFERRED_SIZE, 575, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(cmbArmazem, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(cmbAnoEconomico, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(cmbFamilia, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(lbVias, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(spnCopia, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(ck_simplificada))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(cmbSubFamilia, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(cmbProduto, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(ck_ComVirgula, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(ck_S_A6)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(ck_A7)
                                        .addGap(6, 6, 6))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(20, 20, 20)
                                        .addComponent(txtPreco)
                                        .addGap(47, 47, 47))))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(cmb_area_venda_restaurante, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cmbMoeda, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtLocal, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtQuantidade, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btn_adicionar)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtCodigoProduto, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtCodigoBarra, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(47, 47, 47))))
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(0, 13, Short.MAX_VALUE)
                                .addComponent(cmbTipoDocumento, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lb_proximo_documento, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lbQuantidadeExistente, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtQuantidadeStock, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(118, 118, 118))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lbUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 448, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(layout.createSequentialGroup()
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                    .addGap(139, 139, 139)
                                    .addComponent(status_mensagem_primaria, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(status_mensagem_secundaria, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addContainerGap(70, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(70, 70, 70))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addGap(7, 7, 7)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(ck_simplificada, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(ck_ComVirgula, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(ck_S_A6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(ck_A7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lbVias, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(cmbTipoDocumento, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(cmbArmazem, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(cmbAnoEconomico, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(cmbFamilia, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(14, 14, 14)))))
                        .addGap(4, 4, 4))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(spnCopia, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtQuantidadeStock, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbQuantidadeExistente, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lb_proximo_documento, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cmbSubFamilia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbProduto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtPreco, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(38, 38, 38)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(cmb_area_venda_restaurante, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cmbMoeda, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtLocal, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txtCodigoBarra, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtCodigoProduto, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(txtQuantidade)
                                            .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(btn_adicionar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))))
                        .addGap(9, 9, 9)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 576, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(status_mensagem_primaria)
                            .addComponent(status_mensagem_secundaria))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbUsuario)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lbValorPorExtenco)
                .addGap(11, 11, 11))
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {cmbAnoEconomico, cmbArmazem});

        pack();
    }// </editor-fold>//GEN-END:initComponents


    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void BT_PedidosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BT_PedidosActionPerformed

        if ( verifica_ano_documento_igual_economico() )
        {
            if ( data_documento_superior_ou_igual_ao_ultimo_doc() )
            {

                if ( true )
                {
                    if ( MetodosUtil.licencaValidada( conexao ) )
                    {
                        procedimento_processar_pedidos();
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

    }//GEN-LAST:event_BT_PedidosActionPerformed

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

        conexaoTransaction = new BDConexao();
        DocumentosController.startTransaction( conexaoTransaction );

        vendasController = new VendasController( conexaoTransaction );
        itemVendasController = new ItemVendasController( conexaoTransaction );
        formaPagamentoItemController = new FormaPagamentoItemController( conexaoTransaction );
        stocksController = new StoksController( conexaoTransaction );
        precosController = new PrecosController( conexaoTransaction );
        if ( verifica_ano_documento_igual_economico() )
        {
            if ( data_documento_superior_ou_igual_ao_ultimo_doc() )
            {
                if ( true )
                {
//                    int opcao = JOptionPane.showConfirmDialog( null, "Por Favor\nDeseja pagar a Mesa Completa?" );
//
//                    if ( opcao == JOptionPane.YES_OPTION )
//                    {
//
//                        dispose();
//
                    new FormaPagamentoVisao( this, rootPaneCheckingEnabled, emf, DVML.VENDA_EXPRESSO, conexao ).setVisible( true );
//
//                    }
//                    else if ( opcao == JOptionPane.NO_OPTION )
//                    {
//
//                        try
//                        {
//
//                            int lugar = Integer.parseInt( JOptionPane.showInputDialog( null, "Por favor\nQual lugar deseja facturar?" ) );
//
//                            System.out.println( "LUGAR ACTUAL1 :" + lugar );
//
//                            System.out.println( "LUGAR ACTUAL2 :" + lugar );
//                            if ( exite_pedido_lugar( lugar ) )
//                            {
//
//                                new FormaPagamentoLugarVisao( this, rootPaneCheckingEnabled, emf, DVML.VENDA_RESTAURANTE, conexao, lugar, getTotalAOALiquido( lugar ) ).setVisible( true );
//
//                            }
//                            else
//                            {
//
//                                JOptionPane.showMessageDialog( null, "Nao existe pedido neste lugar" );
//                            }
//
//                        }
//                        catch ( Exception e )
//                        {
//                        }
//                    }
//                    else
//                    {
//                        JOptionPane.showMessageDialog( null, "Operacao cancelada" );
//                    }

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
//            DocumentosController.commitTransaction( conexaoTransaction );

            if ( salvar_venda != null )
            {
                System.err.println( "VENDA AQUI " + salvar_venda.getCodigo() );
                registrar_forma_pagamento( salvar_venda.getCodigo() );
                salvarItemvenda( salvar_venda );
                remover_dados_tabela();
                accao_cliente();
                limpar_consumidor_final();
                cmbCliente.setSelectedIndex( 1 );
                txtTotalApagar.setText( "0" );
                lbValorPorExtenco.setText( "" );

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
//        mostra_consumidor_final();
        accao_cliente();
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
            BDConexao conexaoLocal = new BDConexao();
            if ( !validar_qtd_zero() )
            {

                System.out.println( "PARAMETROS LUPA Área: " + GRUPO_AREA );
                new BuscaProdutoVisao( this, rootPaneCheckingEnabled, getCodigoArmazem(), DVML.JANELA_VENDA_EXPRESSO, conexao ).show();
            }
            else
            {
                JOptionPane.showMessageDialog( null, "Atenção\n Insira a quantidade do produto que desejas vender!", "AVISO", JOptionPane.INFORMATION_MESSAGE );
                txtQuantidade.setBackground( Color.RED );
                txtQuantidade.requestFocus();
                txtQuantidade.setText( "" );
            }
            conexaoLocal.close();
        }
        catch ( Exception e )
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

    private void BtnFConsActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_BtnFConsActionPerformed
    {//GEN-HEADEREND:event_BtnFConsActionPerformed

        try
        {

            if ( MetodosUtil.licencaValidada( conexao ) )
            {

                procedimento_processar_factura_consulta();

            }

        }
        catch ( Exception e )
        {
        }

    }//GEN-LAST:event_BtnFConsActionPerformed

    private void cmbTipoDocumentoActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbTipoDocumentoActionPerformed
    {//GEN-HEADEREND:event_cmbTipoDocumentoActionPerformed
        // TODO add your handling code here:
        mostrar_proximo_codigo_documento();
        actualizar_abreviacao();
        desabilitar_campos();
        selecionar_documento();
        atualizarCliente1();
    }//GEN-LAST:event_cmbTipoDocumentoActionPerformed

    private void desabilitar_campos()
    {

        boolean proformaNaoSelecionado = !( DVML.DOC_FACTURA_PROFORMA_PP == getIdDocumento() );

        boolean documentoIsFA = DVML.DOC_FACTURA_FT == getIdDocumento();
        boolean documentoIsPP = DVML.DOC_FACTURA_PROFORMA_PP == getIdDocumento();
        boolean documentoIsGT = DVML.DOC_GUIA_TRANSPORTE_GT == getIdDocumento();
        System.err.println( "documentoIsFA: " + documentoIsFA );
        System.err.println( "documentoIsPP: " + documentoIsPP );
//        ck_A4.setSelected( !documentoIsFA && !documentoIsPP && !documentoIsGT );
        btFT.setVisible( documentoIsPP || documentoIsFA || documentoIsGT );
        btnFormaPagamentoVendasPraticas.setVisible( !documentoIsFA && !documentoIsPP && !documentoIsGT );
    }

    private void btClienteActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btClienteActionPerformed
    {//GEN-HEADEREND:event_btClienteActionPerformed
        new ClienteVisao( this, rootPaneCheckingEnabled, conexao ).show();
    }//GEN-LAST:event_btClienteActionPerformed

    private void txtIniciaisClienteActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtIniciaisClienteActionPerformed
    {//GEN-HEADEREND:event_txtIniciaisClienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIniciaisClienteActionPerformed

    private void txtIniciaisClienteKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtIniciaisClienteKeyPressed
    {//GEN-HEADEREND:event_txtIniciaisClienteKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIniciaisClienteKeyPressed

    private void cmbFamiliaActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbFamiliaActionPerformed
    {//GEN-HEADEREND:event_cmbFamiliaActionPerformed
        //        cmbSubFamilia.setModel( new DefaultComboBoxModel( ( Vector ) tipoProdutoDao.getDescricaoByIdFamilias( getIdFamilia() ) ) );
        //        cmbProduto.setModel( new DefaultComboBoxModel( ( Vector ) produtoDao.getDescricaoByIdTipoProduto( getIdTipoProduto() ) ) );
        cmbSubFamilia.setModel( new DefaultComboBoxModel( tipoProdutosController.getVectorByIdFamilia( getIdFamilia() ) ) );
        cmbProduto.setModel( new DefaultComboBoxModel( ( produtosController.getVectorByIdTipoProduto( getIdTipoProduto() ) ) ) );
    }//GEN-LAST:event_cmbFamiliaActionPerformed

    private void cmbSubFamiliaActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbSubFamiliaActionPerformed
    {//GEN-HEADEREND:event_cmbSubFamiliaActionPerformed

        cmbProduto.setModel( new DefaultComboBoxModel( ( produtosController.getVectorByIdTipoProduto( getIdTipoProduto() ) ) ) );
    }//GEN-LAST:event_cmbSubFamiliaActionPerformed

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

    private void txtPrecoActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtPrecoActionPerformed
    {//GEN-HEADEREND:event_txtPrecoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPrecoActionPerformed

    private void txtQuantidadeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtQuantidadeActionPerformed
    {//GEN-HEADEREND:event_txtQuantidadeActionPerformed
        // TODO add your handling code here:
//        setFocus( dadosInstituicao.getFoco() );
        txtCodigoProduto.requestFocus();
    }//GEN-LAST:event_txtQuantidadeActionPerformed

    private void txtCodigoBarraActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtCodigoBarraActionPerformed
    {//GEN-HEADEREND:event_txtCodigoBarraActionPerformed
        if ( !validar_qtd_zero() )
        {
            accao_codigo_barra_enter();
        }
        else
        {
            JOptionPane.showMessageDialog( null, "Atenção\n Insira a quantidade do produto que desejas vender!", "AVISO", JOptionPane.INFORMATION_MESSAGE );
            txtQuantidade.setBackground( Color.RED );
            txtQuantidade.requestFocus();
            txtQuantidade.setText( "" );

        }
    }//GEN-LAST:event_txtCodigoBarraActionPerformed

    private void btn_adicionarActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btn_adicionarActionPerformed
    {//GEN-HEADEREND:event_btn_adicionarActionPerformed

        if ( validar_data_validade() )
        {
            if ( !validar_qtd_zero() )
            {
                adicionar_botao();
                scrolltable();
            }
            else
            {
                JOptionPane.showMessageDialog( null, "Atenção\n Insira a quantidade do produto que desejas vender!", "AVISO", JOptionPane.INFORMATION_MESSAGE );
                txtQuantidade.setBackground( Color.RED );
                txtQuantidade.requestFocus();
                txtQuantidade.setText( "" );
            }
        }

    }//GEN-LAST:event_btn_adicionarActionPerformed

    private void atualizarCliente1()
    {
        boolean documentoIsFA = DVML.DOC_FACTURA_FT == getIdDocumento();
        boolean documentoIsPP = DVML.DOC_FACTURA_PROFORMA_PP == getIdDocumento();
//        boolean documentoIsGT = DVML.DOC_GUIA_TRANSPORTE_GT == getIdDocumento();

        if ( documentoIsFA || documentoIsPP )
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

    private void txtCodigoProdutoActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtCodigoProdutoActionPerformed
    {//GEN-HEADEREND:event_txtCodigoProdutoActionPerformed
        // TODO add your handling code here:
        if ( validar_data_validade() )
        {
            if ( !validar_qtd_zero() )
            {
                accao_codigo_interno_enter();
                scrolltable();

            }
            else
            {
                JOptionPane.showMessageDialog( null, "Atenção\n Insira a quantidade do produto que desejas vender!", "AVISO", JOptionPane.INFORMATION_MESSAGE );
                txtQuantidade.setBackground( Color.RED );
                txtQuantidade.requestFocus();
                txtQuantidade.setText( "" );
            }

        }
    }//GEN-LAST:event_txtCodigoProdutoActionPerformed

    private void btFTActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btFTActionPerformed
    {//GEN-HEADEREND:event_btFTActionPerformed

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

    private void btnFormaPagamentoVendasPraticasActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnFormaPagamentoVendasPraticasActionPerformed
    {//GEN-HEADEREND:event_btnFormaPagamentoVendasPraticasActionPerformed
        // TODO add your handling code here:
        //         new FormaPagamentoVisao( this, rootPaneCheckingEnabled, DVML.VENDA_PONTUAL, emf ).setVisible( true );
        if ( !MetodosUtil.tabela_vazia( jTable1 ) )
        {
            if ( possivelVender() )
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
        }
        else
        {
            JOptionPane.showMessageDialog( null, "Caro usuário adicione item na tabela", "Aviso", JOptionPane.WARNING_MESSAGE );
        }
    }//GEN-LAST:event_btnFormaPagamentoVendasPraticasActionPerformed

    private void txtNifClientePesquisaActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtNifClientePesquisaActionPerformed
    {//GEN-HEADEREND:event_txtNifClientePesquisaActionPerformed
        pesquisa_cliente_by_nif();
    }//GEN-LAST:event_txtNifClientePesquisaActionPerformed

    private void txtNifClientePesquisaKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtNifClientePesquisaKeyPressed
    {//GEN-HEADEREND:event_txtNifClientePesquisaKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNifClientePesquisaKeyPressed

    private void txtCodClientePesquisaActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtCodClientePesquisaActionPerformed
    {//GEN-HEADEREND:event_txtCodClientePesquisaActionPerformed
        pesquisa_cliente_by_cod();
    }//GEN-LAST:event_txtCodClientePesquisaActionPerformed

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

    private void adicionar_botao()
    {

        int codigo_local = Integer.parseInt( txtCodigoProduto.getText() );
        TbProduto produto = (TbProduto) produtosController.findById( codigo_local );

        procedimento_adicionar( produto.getDesignacao() );

    }

    public boolean validar_data_validade()
    {

        int idDocumento = getIdDocumento();

        if ( idDocumento == DVML.DOC_FACTURA_RECIBO_FR || idDocumento == DVML.DOC_FACTURA_CONSULTA_MESA )
        {
            return true;
        }

        if ( dc_data_vencimento.getCalendar() == null )
        {

            JOptionPane.showMessageDialog( null, "Por favor, Seleccione uma Data de Vencimento do documento!" );
            dc_data_vencimento.requestFocus();
            dc_data_vencimento.setBackground( Color.RED );
            return false;

        }

        txtIniciaisCliente.setBackground( Color.WHITE );
        return true;
    }

    private void accao_codigo_interno_enter()
    {
        try
        {
            int codigo_local = Integer.parseInt( txtCodigoProduto.getText() );
            TbProduto produto = (TbProduto) produtosController.findById( codigo_local );
//            procedimentoAdicionarTabela( produto );
//            actualizar(produto);

            Integer codTipoProduto = produto.getCodTipoProduto().getCodigo();
            TbTipoProduto tipoProduto = (TbTipoProduto) tipoProdutosController.findById( codTipoProduto );
            Integer codFamilia = tipoProduto.getFkFamilia().getPkFamilia();
            Familia familia = (Familia) familiaController.findById( codFamilia );
            cmbFamilia.setSelectedItem( familia.getDesignacao() );
            cmbSubFamilia.setSelectedItem( tipoProduto.getDesignacao() );

            cmbProduto.setModel( new DefaultComboBoxModel( produtosController.getVector() ) );
            cmbProduto.setSelectedItem( produto.getDesignacao() );

            TbPreco precoLocal = precosController.getLastIdPrecoByIdProduto( produto.getCodigo(), Double.parseDouble( txtQuantidade.getText() ) );
            txtPreco.setText( String.valueOf( precoLocal.getPrecoVenda().doubleValue() ) );
//            String designacao1 = produtosController.getDescricaoById( codigo );
//            System.out.println( "Codigo do Produto no Armazem 2" + codigo );
//            System.out.println( "Designacao do Produto no Armazem 2" + designacao1 );
            //int   codigo = Integer.parseInt(txtCodigoProduto.getText() );
//            TbProduto produto = produtosController.findByCod( codigo );
//            cmbFamilia.setSelectedItem( produto.getCodTipoProduto().getFkFamilia().getDesignacao() );
//            cmbSubFamilia.setSelectedItem( produto.getCodTipoProduto().getDesignacao() );
//            cmbProduto.setSelectedItem( produto.getDesignacao() );

            adicionar_preco_quantidade_anitgo();

            if ( possivel_quantidade() )
            {
                if ( estado_critico() )
                {
                    JOptionPane.showMessageDialog( null, "O produto: " + produto.getDesignacao() + " precisa de ser actualizado no stock", DVML.DVML_COMERCIAL, JOptionPane.WARNING_MESSAGE );
                }
                procedimento_adicionar( produto.getDesignacao() );

            }
            else
            {
                JOptionPane.showMessageDialog( null, "O produto: " + produto.getDesignacao() + " não pode ser vendido pra esta quantidade", DVML.DVML_COMERCIAL, JOptionPane.ERROR_MESSAGE );
            }

        }
        catch ( Exception ex )
        {
            ex.printStackTrace();
            Logger.getLogger( VendasPraticasVisao.class.getName() ).log( Level.SEVERE, null, ex );
            JOptionPane.showMessageDialog( null, "Este produto não existe no armazém " + cmbArmazem.getSelectedItem(), DVML.DVML_COMERCIAL, JOptionPane.ERROR_MESSAGE );
        }

    }

    private static void valor_por_extenco2()
    {
        System.out.println( "Valor XXXXXXX: " + CfMethods.parseMoedaFormatada( txtTotalApagar.getText() ) );
        lbValorPorExtenco.setText( MetodosUtil.valorPorExtenso( CfMethods.parseMoedaFormatada( txtTotalApagar.getText() ), getMoeda().getDesignacao() ) );
    }

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

    private void actualizar_abreviacao()
    {

        switch (getIdDocumento())
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
            java.util.logging.Logger.getLogger( VendasPraticasVisao.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        catch ( InstantiationException ex )
        {
            java.util.logging.Logger.getLogger( VendasPraticasVisao.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        catch ( IllegalAccessException ex )
        {
            java.util.logging.Logger.getLogger( VendasPraticasVisao.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        catch ( javax.swing.UnsupportedLookAndFeelException ex )
        {
            java.util.logging.Logger.getLogger( VendasPraticasVisao.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
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

        /* Create and display the form */
        java.awt.EventQueue.invokeLater( new Runnable()
        {
            public void run()
            {
                new VendasPraticasVisao( "Mesa 2", "Lugar 2", 1, 1, new BDConexao() ).setVisible( true );
            }
        } );
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private static javax.swing.JButton BT_Pedidos;
    private javax.swing.JButton BtnFCons;
    private javax.swing.JButton btCliente;
    private javax.swing.JButton btFT;
    private static javax.swing.JButton btnFormaPagamentoVendasPraticas;
    public static javax.swing.JButton btn_adicionar;
    private javax.swing.JButton btn_voltar;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.ButtonGroup buttonGroup3;
    public static javax.swing.JCheckBox ck_A7;
    public static javax.swing.JCheckBox ck_ComVirgula;
    public static javax.swing.JCheckBox ck_S_A6;
    public static javax.swing.JCheckBox ck_simplificada;
    private static javax.swing.JComboBox<String> cmbAnoEconomico;
    private static javax.swing.JComboBox<String> cmbArmazem;
    public static javax.swing.JComboBox<String> cmbCliente;
    public static javax.swing.JComboBox<String> cmbFamilia;
    public static javax.swing.JComboBox cmbMoeda;
    public static javax.swing.JComboBox cmbProduto;
    public static javax.swing.JComboBox cmbSubFamilia;
    public static javax.swing.JComboBox cmbTipoDocumento;
    public static javax.swing.JComboBox<String> cmb_area_venda_restaurante;
    private static com.toedter.calendar.JDateChooser dc_data_documento;
    private static com.toedter.calendar.JDateChooser dc_data_vencimento;
    private javax.swing.JLabel designacao_categoria;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private static javax.swing.JTable jTable1;
    private static javax.swing.JLabel labelGrupo;
    private javax.swing.JLabel lbClienteConsumidorFinal;
    private javax.swing.JLabel lbClienteConsumidorFinal1;
    private javax.swing.JLabel lbClienteConsumidorFinal2;
    private static javax.swing.JLabel lbQuantidadeExistente;
    public static javax.swing.JLabel lbUsuario;
    public static javax.swing.JLabel lbValorPorExtenco;
    private javax.swing.JLabel lbVias;
    private static javax.swing.JLabel lb_proximo_documento;
    private static javax.swing.JRadioButton rbArmazem;
    private static javax.swing.JRadioButton rbArmazem1;
    private static javax.swing.JSpinner spnCopia;
    public static javax.swing.JLabel status_mensagem_primaria;
    public static javax.swing.JLabel status_mensagem_secundaria;
    private javax.swing.JTextField txtCodClientePesquisa;
    private static javax.swing.JTextField txtCodigoBarra;
    public static javax.swing.JTextField txtCodigoProduto;
    private static javax.swing.JTextField txtIniciaisCliente;
    private javax.swing.JTextField txtLocal;
    private static javax.swing.JTextField txtNifClientePesquisa;
    private static javax.swing.JTextField txtNomeConsumidorFinal;
    public static javax.swing.JTextField txtPreco;
    public static javax.swing.JTextField txtQuantidade;
    private static javax.swing.JTextField txtQuantidadeStock;
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
                txtQuantidadeStock.setVisible( true );

//            txtQuatidadeExistente.setText( String.valueOf( stockDao.get_stock_by_id_produto_and_id_armazem( getCodigoProduto(), getCodigoArmazem() ).getQuantidadeExistente() ) );
                txtQuantidadeStock.setText( String.valueOf( conexao.getQtdExistenteStock( codigo_produto, getCodigoArmazem() ) ) );
            }
            else
            {
                lbQuantidadeExistente.setVisible( false );
                txtQuantidadeStock.setVisible( false );
                txtQuantidadeStock.setText( "" );
            }

            TbItemPedidos itemPedidosLocal = new TbItemPedidos();
            int cod_pedido = (pedidoDao.getLastPedidoByDefignacaoMesaFALSE( getMesa() ));
            pedido = pedidoDao.findTbPedido( cod_pedido );
//            if ( activo_um_lugar() )
//            {
//                //#LUGAR
//                System.out.println( "#LUGAR: " + getDescricaoLugar() );
            itemPedidosLocal.setFkLugares( (TbLugares) lugaresController.findByLugar( getLugar() ) );
////                itemPedidosLocal.setFkLugares( (TbLugares) lugaresController.findByLugar( getDescricaoLugar() ) );
//            }
//            else
//            {
////                itemPedidosLocal.setFkLugares( (TbLugares) lugaresController.findByLugar( getDescricaoLugar() ) );
//                return;
//            }

//            itemPedidosLocal.setFkLugares( (TbLugares) lugaresController.findByLugar( getDescricaoLugar() ) );
            itemPedidosLocal.setFkProdutos( produtoDao.findTbProduto( produtoDao.getIdByDescricao( designacao_produto ) ) );
//            itemPedidosLocal.setQtd( 1 );
            itemPedidosLocal.setQtd( Integer.parseInt( txtQuantidade.getText() ) );

//            itemPedidosLocal.setStatusConvertido( false );
//            /*Envia para a área da cozinha*/
//            //para enviar  da cozinha
//            
//            itemPedidosLocal.setStatusEnviado( true );
//            //para saber se o prato ja foi feito 
//            itemPedidosLocal.setStatusEfectuado( false );
            double qtd = txtQuantidade.getText().equals( "" ) ? 1 : Double.parseDouble( txtQuantidade.getText() );
            int idProduto = produtoDao.getIdByDescricao( designacao_produto );
            double preco = precoDao.findTbPreco( precoDao.getUltimoIdPrecoByIdProduto( itemPedidosLocal.getFkProdutos().getCodigo() ) ).getPrecoVenda().doubleValue();
//            double preco = precoDao.findTbPreco( precoDao.getUltimoIdPrecoByIdProduto( idProduto, qtd ) ).getPrecoVenda().doubleValue();

            double taxa = MetodosUtil.getTaxaPercantagem( idProduto );
            double desconto = 0;
//            double total = itemPedidosLocal.getQtd() * preco;
            double total = FinanceUtils.getValorComIVA( qtd, taxa, preco, desconto );
//            double total = itemPedidosLocal.getQtd() * precoDao.findTbPreco( precoDao.getUltimoIdPrecoByIdProduto( idProduto ) ).getPrecoVenda().doubleValue();
//            itemPedidosLocal.setPreco( preco );
//            
//            itemPedidosLocal.setTotalItem( total );
//            itemPedidosLocal.setFkPedidos( pedido );

            itemPedidosLocal.setObs( "" );
            itemPedidosLocal.setDataEntrega( new Date() );
            itemPedidosLocal.setStatusConvertido( false );
            /*Envia para a área da cozinha*/
            //para enviar  da cozinha
            itemPedidosLocal.setStatusEnviado( true );
            //para saber se o prato ja foi feito 
            itemPedidosLocal.setStatusEfectuado( false );

//            double preco = precoDao.findTbPreco( precoDao.getUltimoIdPrecoByIdProduto( itemPedidosLocal.getFkProdutos().getCodigo() ) ).getPrecoVenda().doubleValue();
//            double total = itemPedidosLocal.getQtd() * preco;
            itemPedidosLocal.setPreco( preco );
//            itemPedidosLocal.setValorIva(taxa );
            itemPedidosLocal.setTotalItem( total );
            itemPedidosLocal.setFkPedidos( pedido );

            try
            {
                if ( !validar_qtd_zero() )
                {
                    if ( possivel_quantidade_pedidos_existentes( itemPedidosLocal.getFkProdutos().getCodigo() ) )
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
                        BDConexao conexaoLocal = new BDConexao();
                        Integer idLastItemPedido = itemPedidosDao.criarComProcedimentoLugar( itemPedidosLocal, conexaoLocal );

                        if ( idLastItemPedido != null )
                        {
                            //##PINTAR
//                        PrincipalPedidosVisao.mesas_livres( getLabelMesaByMesa() );
//                        PrincipalPedidosVisao.pintar_mesas( getLabelMesaByMesa(), mesa, DVML.getAreaByIdArea( GRUPO_AREA ) );
//                        System.out.println( "MESA A PINTAR: " + getLabelMesaByMesa().getText() );
//                        System.out.println( "VALOR MESA A PINTAR: " + mesa );
                            //adiciona os produtos na tabela
                            actualizar();
                            //imprimir para a cozinha
//                        if ( itemPedidosLocal.getFkProdutos().getCodTipoProduto().getFkFamilia().getPkFamilia() == DVML.COD_SERVICO )
//                        if ( itemPedidosLocal.getFkProdutos().getCozinha().equals( DVML.ENVIAR_TICKET ) );
//                        {
//                            System.err.println( "PK_ITEM_PEDIDO_LOCAL" + idLastItemPedido );
////                      new ImprimirCozinha( itemPedidosLocal.getFkPedidos().getPkPedido(), itemPedidosLocal.getFkProdutos().getCodigo() );
////                            new Cozinha( idLastItemPedido );
////
////                            MetodosUtil.imprimir_cozinha( itemPedidosLocal.getFkProdutos(),
////                                    "Activo", itemPedidosLocal.getQtd(),
////                                    dadosInstituicaoController );
//                        }
                        } //  

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

                        conexaoLocal.close();

                        //  }else JOptionPane.showMessageDialog(null, "Este produto já não esta disponível.", "AVISO", JOptionPane.WARNING_MESSAGE  );
                    }
                    else
                    {
                        JOptionPane.showMessageDialog( null, "O Produto " + designacao_produto + " não pode ser vendido para esta quantidade.", "AVISO", JOptionPane.WARNING_MESSAGE );
                    }
                }
                else
                {
                    JOptionPane.showMessageDialog( null, "Atenção\n Insira a quantidade do produto que desejas vender!", "AVISO", JOptionPane.INFORMATION_MESSAGE );
                    txtQuantidade.setBackground( Color.RED );
                    txtQuantidade.requestFocus();
                    txtQuantidade.setText( "" );
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

    private static String getMesa()
    {
        TbMesas mesa = (TbMesas) mesasController.findById( DVML.MESA_BALCAO );
        return mesa.getDesignacao();
    }

    private static String getLugar()
    {
        TbLugares lugares = (TbLugares) lugaresController.findById( DVML.LUGAR_BALCAO );
        return lugares.getDesignacao();
    }

    public static void procedimento_salvar_pedidos_iten_pedidos_interno( String designacao_produto )
    {
        System.out.println( "### CHEGUEI NO ITEM-PEDIDO INTERNO" );
        BDConexao conexaoLocal = new BDConexao();

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
                txtQuantidadeStock.setVisible( true );

//            txtQuatidadeExistente.setText( String.valueOf( stockDao.get_stock_by_id_produto_and_id_armazem( getCodigoProduto(), getCodigoArmazem() ).getQuantidadeExistente() ) );
                txtQuantidadeStock.setText( String.valueOf( conexaoLocal.getQtdExistenteStock( codigo_produto, getCodigoArmazem() ) ) );
            }
            else
            {
                lbQuantidadeExistente.setVisible( false );
                txtQuantidadeStock.setVisible( false );
                txtQuantidadeStock.setText( "" );
            }

            TbItemPedidos itemPedidosLocal = new TbItemPedidos();
            int cod_pedido = (pedidoDao.getLastPedidoByDefignacaoMesaFALSE( getMesa() ));
            pedido = pedidoDao.findTbPedido( cod_pedido );

//            if ( activo_um_lugar() )
//            {
            itemPedidosLocal.setFkLugares( (TbLugares) lugaresController.findByLugar( getLugar() ) );
//            }
//            else
//            {
//                return;
//            }
//            itemPedidosLocal.setFkLugares( (TbLugares) lugaresController.findByLugar( getDescricaoLugar() ) );

//            itemPedidosLocal.setFkLugares( lugarDao.findTbLugares( lugarDao.getIdByDescricao( getDescricaoLugar() ) ) );
            itemPedidosLocal.setFkProdutos( produtoDao.findTbProduto( produtoDao.getIdByDescricao( designacao_produto ) ) );
            itemPedidosLocal.setQtd( Integer.parseInt( txtQuantidade.getText() ) );
//            itemPedidosLocal.setStatusConvertido( false );
//            /*Envia para a área da cozinha*/
//            //para enviar  da cozinha
//            itemPedidosLocal.setStatusEnviado( true );
//            //para saber se o prato ja foi feito 
//            itemPedidosLocal.setStatusEfectuado( false );

            double qtd = Double.parseDouble( txtQuantidade.getText() );
            int idProduto = produtoDao.getIdByDescricao( designacao_produto );
            double preco = precoDao.findTbPreco( precoDao.getUltimoIdPrecoByIdProduto( itemPedidosLocal.getFkProdutos().getCodigo() ) ).getPrecoVenda().doubleValue();
//            double preco = precoDao.findTbPreco( precoDao.getUltimoIdPrecoByIdProduto( idProduto, qtd ) ).getPrecoVenda().doubleValue();

            double taxa = MetodosUtil.getTaxaPercantagem( idProduto );
            double desconto = 0;
//            double total = itemPedidosLocal.getQtd() * preco;
            double total = FinanceUtils.getValorComIVA( qtd, taxa, preco, desconto );
//            double total = itemPedidosLocal.getQtd() * precoDao.findTbPreco( precoDao.getUltimoIdPrecoByIdProduto( idProduto ) ).getPrecoVenda().doubleValue();

//            itemPedidosLocal.setTotalItem( total );
//            itemPedidosLocal.setFkPedidos( pedido );
            itemPedidosLocal.setObs( "" );
            itemPedidosLocal.setDataEntrega( new Date() );
            itemPedidosLocal.setStatusConvertido( false );
            /*Envia para a área da cozinha*/
            //para enviar  da cozinha
            itemPedidosLocal.setStatusEnviado( true );
            //para saber se o prato ja foi feito 
            itemPedidosLocal.setStatusEfectuado( false );

//            double preco = precoDao.findTbPreco( precoDao.getUltimoIdPrecoByIdProduto( itemPedidosLocal.getFkProdutos().getCodigo() ) ).getPrecoVenda().doubleValue();
//            double total = itemPedidosLocal.getQtd() * preco;
            itemPedidosLocal.setPreco( preco );
//            itemPedidosLocal.setValorIva(taxa );
            itemPedidosLocal.setTotalItem( total );
            itemPedidosLocal.setFkPedidos( pedido );

            try
            {
                if ( possivel_quantidade_pedidos_existentes( itemPedidosLocal.getFkProdutos().getCodigo() ) )
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
                    Integer idLastItemPedido = itemPedidosDao.criarComProcedimentoLugar( itemPedidosLocal, conexaoLocal );

                    if ( idLastItemPedido != null )
                    {
                        //##PINTAR
//                        PrincipalPedidosVisao.mesas_livres( getLabelMesaByMesa() );
//                        PrincipalPedidosVisao.pintar_mesas( getLabelMesaByMesa(), mesa, DVML.getAreaByIdArea( GRUPO_AREA ) );
//                        System.out.println( "MESA A PINTAR: " + getLabelMesaByMesa().getText() );
//                        System.out.println( "VALOR MESA A PINTAR: " + mesa );
                        //adiciona os produtos na tabela
                        actualizar( produto_local );
                        //imprimir para a cozinha
//                        if ( itemPedidosLocal.getFkProdutos().getCodTipoProduto().getFkFamilia().getPkFamilia() == DVML.COD_SERVICO )
//                        if ( itemPedidosLocal.getFkProdutos().getCozinha().equals( DVML.ENVIAR_TICKET ) );
//                        {
//                            System.err.println( "PK_ITEM_PEDIDO_LOCAL" + idLastItemPedido );
//                      new ImprimirCozinha( itemPedidosLocal.getFkPedidos().getPkPedido(), itemPedidosLocal.getFkProdutos().getCodigo() );
//                            new Cozinha( idLastItemPedido );
//
//                            MetodosUtil.imprimir_cozinha( itemPedidosLocal.getFkProdutos(),
//                                    "Activo", itemPedidosLocal.getQtd(),
//                                    dadosInstituicaoController );
//                        }
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

//    public boolean possivel_quantidade_pedidos_existentes( int codigo_produto )
    public static boolean possivel_quantidade_pedidos_existentes( int codigo_produto )
    {
        BDConexao conexaoLocal = new BDConexao();
        produtosController = new ProdutosController( conexaoLocal );

        TbProduto produtoLocal = (TbProduto) produtosController.findById( codigo_produto );
        if ( true ) //            ERRORR
        //        if ( produto.getCodTipoProduto().getFkFamilia().getPkFamilia() == DVML.COD_SERVICO )
        {
            return true;
        }

//        System.err.println( conexaoLocal.getQuantidade_Existente_Publico( codigo_produto, id_armzem ) );
        System.err.println( "ID ARMAZEM : " + id_armzem );
        System.err.println( "ID ARMAZEM : " + getCodigoArmazem() );
        double quant_possivel = 0d;
        try
        {
            quant_possivel = ( conexaoLocal.getQuantidade_Existente_Publico( codigo_produto, id_armzem ) - conexaoLocal.getQuantidade_minima_publico( codigo_produto, id_armzem ) );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }

        // int quant_possivel = stock.getQuantidadeExistente() -  stock.getQuantBaixa();
        System.out.println( "Quantidade Possivel: " + quant_possivel );
        System.out.println( "Quantidade Itens Pedidos: " + MetodosUtil.getQtdInItemPedidos( conexaoLocal, codigo_produto ) );
        boolean resultado = quant_possivel >= ( 1 + MetodosUtil.getQtdInItemPedidos( conexaoLocal, codigo_produto ) );
        conexaoLocal.close();
        return resultado;

    }

    public static boolean possivel_quantidade( int codigo_produto, int qtd )
    {
        TbProduto produtoLocal = (TbProduto) produtosController.findById( codigo_produto );
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

    public static void setSalvarPedidos()
    {

//        System.out.println( "ID PEDIDO : " + pedidoDao.getLastPedidoByDefignacaoMesaSemStatus( mesa, DVML.getAreaByIdArea( GRUPO_AREA ) ) );
        TbPedido pedido_local;
        //busca o último pedido de uma determinada mesa, senão existe instancia um pedido como 
        if ( pedidoDao.getLastPedidoByDefignacaoMesaSemStatus( getMesa() ) == 0 )
        {

            pedido_local = new TbPedido();
            pedido_local.setStatusPedido( true );

        }
        else
        {
            pedido_local = pedidoDao.findTbPedido( pedidoDao.getLastPedidoByDefignacaoMesaSemStatus( getMesa() ) );
        }

        if ( pedido_local.getStatusPedido() )
        {
            try
            {

                TbPedido pedido_2 = new TbPedido();
                pedido_2.setDataPedido( new Date() );
                pedido_2.setHoraPedido( new Date() );
                pedido_2.setStatusPedido( false );
//                System.out.println( "Mesa ao salvar o Procedimento Pedidos " + mesa );
                pedido_2.setFkMesas( mesasDao.findTbMesas( DVML.MESA_BALCAO ) );
//                pedido_2.setAreaPedido( setar_grupo() );
//#ped1                
                BDConexao conexaoTransction = new BDConexao();

                Integer idLastPedido = pedidoDao.criarComProcedimentos( pedido_2, conexao );

                if ( idLastPedido != null )
                {

//                    PrincipalPedidosVisao.mesas_livres( getLabelMesaByMesa() );
//                    PrincipalPedidosVisao.pintar_mesas( getLabelMesaByMesa(), this.mesa, DVML.getAreaByIdArea( GRUPO_AREA ) );
                }
                else
                {
                    System.err.println( "ERRO AO SALVAR O PEDIDO...." );
                }
                conexaoTransction.close();

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
//            int cod_pedido = pedidoDao.getLastPedidoByDefignacaoMesaFALSE( mesa );
            int cod_pedido = pedidoDao.getLastPedidoByDefignacaoMesaFALSE( mesa );

            this.pedido = pedidoDao.findTbPedido( cod_pedido );
            itemPedidos.setFkLugares( (TbLugares) lugaresController.findByLugar( getLugar() ) );

            itemPedidos.setFkProdutos( (TbProduto) produtosController.findByDesignacao( getDescricaoProduto() ) );

        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }

    }

//    private String getDescricaoMesa()
//    {
//        return String.valueOf( txtMesa.getText() );
//    }
//    private static String getDescricaoLugar()
//    {
//
//        if ( !btn_01.isEnabled() )
//        {
//            return "LUGAR 1";
//        }
//        else if ( !btn_02.isEnabled() )
//        {
//            return "LUGAR 2";
//        }
//        else if ( !btn_03.isEnabled() )
//        {
//            return "LUGAR 3";
//        }
//        else if ( !btn_04.isEnabled() )
//        {
//            return "LUGAR 4";
//        }
//        else if ( !btn_05.isEnabled() )
//        {
//            return "LUGAR 5";
//        }
//        else if ( !btn_06.isEnabled() )
//        {
//            return "LUGAR 6";
//        }
//        else if ( !btn_07.isEnabled() )
//        {
//            return "LUGAR 7";
//        }
//        else if ( !btn_08.isEnabled() )
//        {
//            return "LUGAR 8";
//        }
//        else if ( !btn_09.isEnabled() )
//        {
//            return "LUGAR 9";
//        }
//        else if ( !btn_10.isEnabled() )
//        {
//            return "LUGAR 10";
//        }
//
//        return "";
//    }
    private String getDescricaoProduto()
    {
        //return String.valueOf( cmbProduto.getSelectedItem());    
        return "";
    }

//    public static void actualizar()
//    {
//        try
//        {
//
//            adicionar_tabela( itemPedidosDao.buscaTodosItemPedidos( pedidoDao.getLastPedidoByDefignacaoMesaFALSE( mesa ) ) );
//            valor_por_extenco();
//            scrolltable();
//        }
//        catch ( Exception e )
//        {
//            e.printStackTrace();
//        }
//
//    }
    public static void actualizar()
    {
        try
        {

            adicionar_tabela( itemPedidosDao.buscaTodosItemPedidos( pedidoDao.getLastPedidoByDefignacaoMesaFALSE( getMesa() ) ) );
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

    public static void actualizar( TbProduto produto )
    {
        try
        {

            adicionar_tabela( itemPedidosDao.buscaTodosItemPedidos( pedidoDao.getLastPedidoByDefignacaoMesaFALSE( getMesa() ) ) );
            valor_por_extenco();
//            valor_por_extenco();
            scrolltable();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }

    }
//
//    public static void actualizar_lugar( int lugar )
//    {
//        try
//        {
//
//            adicionar_tabela_lugar( itemPedidosDao.buscaTodosItemPedidos2( pedidoDao.getLastPedidoByDefignacaoMesaLugarFALSE( lugar ) ) );
//            scrolltable();
//        }
//        catch ( Exception e )
//        {
//            e.printStackTrace();
//        }
//
//    }

    public static void adicionar_tabela( List<TbItemPedidos> itemPedidos_list )
    {
        DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
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
                        (int) qtd,
                        //                        CfMethods.formatarComoMoeda(FinanceUtils.getValorComIVA( qtd, taxa, preco, desconto ))
                        //                        FinanceUtils.getValorComIVA( qtd, taxa, preco, desconto )
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
//    public static void adicionar_tabela( List<TbItemPedidos> itemPedidos_list )
//    {
//        DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
//        Double preco = 0d, desconto = 0d, taxa = 0d;
//        Integer qtd = 0;
//
//        modelo.setRowCount( 0 );
//
//        if ( itemPedidos_list != null )
//        {
//
//            //define a altura das linhas
//            jTable1.setRowHeight( 50 );
//            try
//            {
//                for ( int i = 0; i < itemPedidos_list.size(); i++ )
//                {
//
//                    qtd = itemPedidos_list.get( i ).getQtd();
//
//                    preco = precoDao.findTbPreco( precoDao.getUltimoIdPrecoByIdProduto( itemPedidos_list.get( i ).getFkProdutos().getCodigo(), qtd ) ).getPrecoVenda().doubleValue();
////                    preco = precoDao.findTbPreco( precoDao.getUltimoIdPrecoByIdProduto( itemPedidos_list.get( i ).getFkProdutos().getCodigo(), qtd ) ).getPrecoVenda().doubleValue();
//                    System.err.println( "QTD ADD: " + qtd );
//                    System.err.println( "PU  ADD: " + preco );
//
//                    taxa = MetodosUtil.getTaxaPercantagem( itemPedidos_list.get( i ).getFkProdutos().getCodigo() );
//                    modelo.addRow( new Object[]
//                    {
//                        itemPedidos_list.get( i ).getPkItemPedidos(),
//                        itemPedidos_list.get( i ).getFkLugares().getDesignacao(),
//                        itemPedidos_list.get( i ).getFkProdutos().getDesignacao(),
//                        qtd,
//                        //                        CfMethods.formatarComoMoeda(FinanceUtils.getValorComIVA( qtd, taxa, preco, desconto ))
//                        //                        FinanceUtils.getValorComIVA( qtd, taxa, preco, desconto )
//                        CfMethods.formatarComoMoeda( FinanceUtils.getValorComIVA( qtd, taxa, preco, desconto ) )
//                    //itemPedidos.get( i ).getQtd() * precoDao.findTbPreco( precoDao.getUltimoIdPrecoByIdProduto( itemPedidos.get( i ).getFkProdutos().getCodigo() ) ).getPrecoVenda()
//                    } );
//                }
//            }
//            catch ( Exception e )
//            {
//                e.printStackTrace();
//            }
//
//            setTotalPagar();
//            valor_por_extenco();
//
//        }
////        }
////        else
////        {
////            JOptionPane.showMessageDialog( null, "Atenção\nA quantidade a sair não pode ser igual a zero!" );
////        }
//
//    }

    public static void adicionar_tabela_lugar( List<TbItemPedidos> itemPedidos_list )
    {
        DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
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
                        //                        CfMethods.formatarComoMoeda(FinanceUtils.getValorComIVA( qtd, taxa, preco, desconto ))
                        //                        FinanceUtils.getValorComIVA( qtd, taxa, preco, desconto )
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
        if ( Double.parseDouble( txtQuantidadeStock.getText() ) == 0.0 )
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

//    public void imprimirPedidos()
//    {
//
//        try
//        {
//            TbPedido pedido = pedidoDao.findTbPedido( pedidoDao.getLastPedidoByDefignacaoMesaFALSE( mesa ) );
//            //pedido.setStatusPedido(true);
//            //pedidoDao.edit(pedido);
//
//            ListaPedidos listaPedidos = new ListaPedidos( pedido.getPkPedido() );
//            //eliminar_toda_tabela(jTable1);
//            //eliminar_toda_tabela(jTable1);
//
//        }
//        catch ( Exception e )
//        {
//            e.printStackTrace();
//        }
//
//    }
//    public void imprimirPedidos()
//    {
//
//        try
//        {
//            TbPedido pedido = pedidoDao.findTbPedido( pedidoDao.getLastPedidoByDefignacaoMesaFALSE( mesa ) );
//
//            List<TbItemPedidos> item = itemPedidosDao.buscaTodosItemPedidos( pedido.getPkPedido() );
//            double totalPedidos = 0;
//            for ( TbItemPedidos tbItemPedidos : item )
//            {
//                totalPedidos += tbItemPedidos.getTotalItem();
//            }
//
//            //pedido.setStatusPedido(true);
//            //pedidoDao.edit(pedido);
//            ListaPedidos1 listaPedidos = new ListaPedidos1( pedido.getPkPedido(), totalPedidos );
//            //eliminar_toda_tabela(jTable1);
//            //eliminar_toda_tabela(jTable1);
//
//        }
//        catch ( Exception e )
//        {
//            e.printStackTrace();
//        }
//
//    }
    public void imprimirPedidosPorLugar( int opcao )
    {

        try
        {
            TbPedido pedido = pedidoDao.findTbPedido( pedidoDao.getLastPedidoByDefignacaoMesaFALSE( getMesa() ) );
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

        DefaultTableModel modelo = (DefaultTableModel) tabela.getModel();
        int id_item_pedido = Integer.parseInt( modelo.getValueAt( tabela.getSelectedRow(), 0 ).toString() );
        TbItemPedidos itemPedidosLocal = itemPedidosDao.findTbItemPedidos( id_item_pedido );
        try
        {

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

        DefaultTableModel modelo = (DefaultTableModel) tabela.getModel();
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

        DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
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
        DefaultTableModel modelo = (DefaultTableModel) tabela.getModel();
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
            DefaultTableModel modelo = (DefaultTableModel) tabela.getModel();
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
        conexaoTransaction = new BDConexao();
        DocumentosController.startTransaction( conexaoTransaction );
        vendasController = new VendasController( conexaoTransaction );
//        Date data_documento = new Date ();
        Date data_documento = dc_data_documento.getDate();
        TbVenda venda_local = new TbVenda();
        venda_local.setDataVenda( data_documento );
        Calendar calendar = Calendar.getInstance();
        calendar.setTime( data_documento );
        calendar.add( Calendar.DATE, 15 );
        venda_local.setDataVencimento( calendar.getTime() );
        venda_local.setHora( data_documento );
//        venda_local.setNomeCliente( getNomeCliente() );
//        venda_local.setClienteNif( getClienteNif() );
        venda_local.setNomeCliente( txtNomeConsumidorFinal.getText() );
        venda_local.setClienteNif( txtNifClientePesquisa.getText() );
        venda_local.setCodigoCliente( new TbCliente( getIdCliente() ) );
//        TbCliente clienteSelecionado = clientesController.getClienteByNome( (String) cmbCliente.getSelectedItem() );
        venda_local.setNomeConsumidorFinal( txtNomeConsumidorFinal.getText() );
//        if ( !Objects.isNull( clienteSelecionado ) )
//        {
//            venda_local.setNomeCliente( clienteSelecionado.getNome() );
//            venda_local.setClienteNif( clienteSelecionado.getNif() );
//            venda_local.setCodigoCliente( clienteSelecionado );
//        }
//        else
//        {
////            venda_local.setNomeCliente ( _CLIENTE_CONSUMIDOR_FINAL );
//            venda_local.setNomeCliente( _CLIENTE_CONSUMIDOR_FINAL );
//
//            venda_local.setNomeConsumidorFinal( txtNomeConsumidorFinal.getText() );
//            venda_local.setCodigoCliente( clientesController.findByCodigo( getIdCliente() ) );
//            venda_local.setClienteNif( NUMBER_NIF_GENERICO );
//        }
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
        venda_local.setHashCod( MetodosUtil.criptografia_hash( venda_local, getGrossTotal(), conexaoTransaction ) );
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

                DocumentosController.commitTransaction( conexaoTransaction );

                if ( Objects.isNull( last_venda ) || last_venda == 0 )
                {
                    DocumentosController.rollBackTransaction( conexaoTransaction );
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
            DocumentosController.rollBackTransaction( conexaoTransaction );
//            conexaoTransaction.close();
        }
//        return vendaDao.findTbVenda( last_venda );
        return new TbVenda( last_venda );

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

//    public static String setar_grupo()
//    {
//
//        TbArmazem findArmazem = armazensController.findByCodigo( getCodigoArmazem() );
//        if ( findArmazem.getCodigo() == DVML.ARMAZEM_DEFAUTL_FACTURACAO )
//        {
////            labelGrupo.setText(  String.valueOf( DVML.COD_GRUPO_RESTAURANTE));
//            return DVML.AREA_RESTAURANTE;
//        }
//        else
//        {
////            labelGrupo.setText(  String.valueOf( DVML.COD_GRUPO_BAR));
//            return DVML.AREA_BAR;
//        }
//
//    }
    private static double getTotalRetencao()
    {
//        DefaultTableModel modelo = ( DefaultTableModel ) jTable1.getModel();
        double imposto = 0d;
        return imposto;
    }

    public static TbVenda salvar_venda( int lugar )
    {
        conexaoTransaction = new BDConexao();
        DocumentosController.startTransaction( conexaoTransaction );
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
        TbCliente clienteSelecionado = clientesController.getClienteByNome( (String) cmbCliente.getSelectedItem() );

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
                    DocumentosController.rollBackTransaction( conexaoTransaction );
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
            DocumentosController.rollBackTransaction( conexao );
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

//    private static String getCodDocActualizador( BDConexao conexaoParm )
//    {
//        try
//        {
//            documentosController = new DocumentosController( conexaoParm );
//            anoEconomicoController = new AnoEconomicoController( conexaoParm );
//
//            documento = (Documento) documentosController.findById( getIdDocumento() );
//            anoEconomico = (AnoEconomico) anoEconomicoController.findById( getIdAnoEconomico() );
//            // this.doc_prox_cod = documento.getCodUltimoDoc() + 1;
//            doc_prox_cod = VendasController.getUltimaContagemByIdDocumentoAndAnoEconomico(
//                    getIdDocumento(), getIdAnoEconomico(), conexaoParm ) + 1;
//            prox_doc = documento.getAbreviacao();
//            //FA Série / codigo
//            prox_doc += " " + anoEconomico.getSerie() + "/" + doc_prox_cod;
//            return prox_doc;
//        }
//        catch ( Exception e )
//        {
//            return "";
//        }
//    }
    private static Moeda getMoeda()
    {
        String moedaSelecionada = (String) cmbMoeda.getSelectedItem();

        if ( moedaSelecionada == null )
        {
            return null;
        }

        return new MoedaDao( emf ).getByDescricao( moedaSelecionada );
    }

    private static void valor_por_extenco()
    {
//        System.out.println( "Valor XXXXXXX: " + CfMethods.parseMoedaFormatada( txtTotalApagar.getText() ) );

        if ( !txtTotalApagar.getText().equals( "" ) && !txtTotalApagar.getText().equals( "0" ) )
        {

            System.out.println( "VALOR: " + txtTotalApagar.getText() );
            lbValorPorExtenco.setText( MetodosUtil.valorPorExtenso( CfMethods.parseMoedaFormatada( txtTotalApagar.getText() ), "Kwanza" ) );

        }
    }

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
//    private static void valor_por_extenco( Moeda moeda )
//    {
//        System.out.println( "Valor XXXXXXX: " + CfMethods.parseMoedaFormatada( txtTotalApagar.getText() ) );
//        lbValorPorExtenco.setText( MetodosUtil.valorPorExtenso( CfMethods.parseMoedaFormatada( txtTotalApagar.getText() ), moeda.getDesignacao() ) );
//    }
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

    private static boolean possivelVender()
    {
        boolean possivelVender = true;
        DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
        for ( int i = 0; i < jTable1.getRowCount(); i++ )
        {
            TbProduto produtoLocal = (TbProduto) produtosController.findByDesignacao( modelo.getValueAt( i, 2 ).toString() );
            FichaTecnica fichaTecnica = fichaTecnicaController.getFichaByRefeicao( produtoLocal.getCodigo() );
            if ( Objects.nonNull( fichaTecnica ) )//existe ficha tecnica
            {
                List<LinhaFichaTecnica> linhasFichaTecnica = linhaFichaTecnicaController.listarTodosByCodigoFichaTecnica( fichaTecnica.getId() );
                int qtd = Integer.parseInt( modelo.getValueAt( i, 3 ).toString() );
                if ( possivelAdicionar( qtd, fichaTecnica.getPrato(), linhasFichaTecnica ) )
                {
                    possivelVender = possivelVender && true;
                }
                else
                {
                    possivelVender = possivelVender && false;
                }
            }
            else
            {
                possivelVender = possivelVender && true;
            }
        }
        return possivelVender;
    }

    private static void actualizarQtdIngredientes( FichaTecnica fichaTecnica, double qtdPrato )
    {
        BDConexao conexaoLocal = new BDConexao();
        if ( Objects.nonNull( fichaTecnica ) )//existe ficha tecnica
        {
            List<LinhaFichaTecnica> linhasFichaTecnica = linhaFichaTecnicaController.listarTodosByCodigoFichaTecnica( fichaTecnica.getId() );
            for ( LinhaFichaTecnica l : linhasFichaTecnica )
            {
                double qtdExistente = 0d;
                try
                {
                    qtdExistente = conexaoLocal.getQuantidade_Existente_Publico( l.getIgrendienteId(), DVML.ARMAZEM_DEFAUTL );
                    double qtdActualizar = (l.getQtdBruto() * qtdPrato);
                    actualizar_quantidade( l.getIgrendienteId(), qtdActualizar, DVML.ARMAZEM_DEFAUTL );
                }
                catch ( Exception e )
                {
                    e.printStackTrace();
                }
            }
        }
        conexaoLocal.close();
    }

    public static void salvarItemvenda( TbVenda venda )
    {
        itemVendasController = new ItemVendasController( conexaoTransaction );
        DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
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
                TbProduto produto_local = (TbProduto) produtosController.findByDesignacao( modelo.getValueAt( i, 2 ).toString() );
                lugar = modelo.getValueAt( i, 1 ).toString();
                idProduto = produtosController.findByDesignacao( modelo.getValueAt( i, 2 ).toString() ).getCodigo();
//                idProduto = produtoDao.getProdutoByDescricao( modelo.getValueAt( i, 2 ).toString() ).getCodigo();
                qtd = Double.parseDouble( modelo.getValueAt( i, 3 ).toString() );
                preco_unitario = precosController.getLastIdPrecoByIdProduto( idProduto, qtd ).getPrecoVenda().doubleValue();
//                preco_unitario = precoDao.findTbPreco( precoDao.getUltimoIdPrecoByIdProduto( idProduto, qtd ) ).getPrecoVenda().doubleValue();
                taxa = MetodosUtil.getTaxaPercantagem( idProduto );
//                sub_total_iliquido = FinanceUtils.getValorComIVA( qtd, taxa, preco_unitario, desconto );
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
                itemVenda.setFkLugares( (TbLugares) lugaresController.findByLugar( getLugar() ) );
//                itemVenda.setFkLugares( lugarDao.findTbLugares( lugarDao.getIdByDescricao( lugar ) ) );
                itemVenda.setFkMesas( (TbMesas) mesasController.findByDesignacao( getMesa() ) );

                //cria o item venda
//                itemVendaDao.create ( itemVenda );
//                int last_venda = itemVendaDao.criarComProcedimentos( itemVenda, conexao );
                if ( !itemVendasController.salvar( itemVenda ) )
                {
                    DocumentosController.rollBackTransaction( conexaoTransaction );
                    conexaoTransaction.close();
                    return;
                }

                boolean isStocavel = produto_local.getStocavel().equals( "true" );

                if ( isStocavel )
                {
                    stock_local = stocksController.getStockByIdProdutoAndIdArmazem( itemVenda.getCodigoProduto().getCodigo(), getCodigoArmazem() );
                }
                else
                {
                    dadosInstituicao = dadosInstituicaoController.findByCodigo( 1 );
                    if ( dadosInstituicao.getTipoFichaTecnica().equals( "B" ) )
                    {
                        FichaTecnica fichaTecnica = fichaTecnicaController.getFichaByRefeicao( produto_local.getCodigo() );
                        actualizarQtdIngredientes( fichaTecnica, qtd );
                    }
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
            txtQuantidade.setBackground( Color.BLUE );

            DocumentosController.commitTransaction( conexaoTransaction );
            TbPedido pedido = pedidoDao.findTbPedido( pedidoDao.getLastPedidoByDefignacaoMesaFALSE( getMesa() ) );
            PedidoDao.eliminarPedido( pedido, conexao ); // Elimina o pedido

            try
            {
                setSalvarPedidos();
            }
            catch ( Exception e )
            {
                e.printStackTrace();
            }

            System.out.println( "NOVO Codigo Venda: " + venda.getCodigo() );
            System.err.println( "Codigo Venda: " + venda.getCodigo() );
            System.err.println( "Venda: " + venda.getCodFact() );
            gorjeta = 0;

//            List<TbProduto> lista_produto_isentos = new ArrayList<>();
//            lista_produto_isentos = getProdutosIsentos();
//            String motivos_isentos = MetodosUtil.getMotivoIsensaoProdutos( lista_produto_isentos );
//            int numeroVias = 1;
            int numeroVias = (int) Double.parseDouble( spnCopia.getValue().toString() );
            for ( int i = 1; i <= numeroVias; i++ )
            {

                switch (i)
                {
                    case 1:
                        ListaVenda1 original = new ListaVenda1( venda.getCodigo(), abreviacao, false, ck_simplificada.isSelected(), "Original", "" );
//            ListaVendaPorMesas listaVenda1 = new ListaVendaPorMesas( venda.getCodigo(), abreviacao, false, true, "Original" );
                        break;
                    case 2:
                        ListaVenda1 original_duplicado = new ListaVenda1( venda.getCodigo(), abreviacao, false, ck_simplificada.isSelected(), "Duplicado", "" );
//                        ListaVenda1 original_duplicado = new ListaVenda1( cod_venda, abreviacao, false, ck_simplificada.isSelected(), "Duplicado", motivos_isentos );
                        break;
                    case 3:
                        ListaVenda1 original_triplicado = new ListaVenda1( venda.getCodigo(), abreviacao, false, ck_simplificada.isSelected(), "Triplicado", "" );
                        break;
                }

            }
//            for ( int i = 1; i <= numeroVias; i++ )
//            {
//
//                switch (i)
//                {
//                    case 1:
//
//                        ListaVendaPorMesas listaVenda1 = new ListaVendaPorMesas( venda.getCodigo(), abreviacao, false, true, "Original" );
//                        break;
//                    case 2:
//                        ListaVendaPorMesas listaVenda2 = new ListaVendaPorMesas( venda.getCodigo(), abreviacao, false, true, "Original" );
//                        break;
//                    case 3:
//                        ListaVendaPorMesas listaVenda3 = new ListaVendaPorMesas( venda.getCodigo(), abreviacao, false, true, "Triplicado" );
//                        break;
//                }
//
//            }

//            ListaVenda listaVenda = new ListaVenda( venda.getCodigo(), false, true );
//            ListaVenda listaVenda1 = new ListaVenda( venda.getCodigo(), false, true );
//            ListaVenda listaVenda = new ListaVenda ( venda.getCodigo(), false, true, "Original" );
//            ListaVenda listaVenda1 = new ListaVenda ( venda.getCodigo(), false, true, "Duplicado" );
//            PrincipalPedidosVisao.mesas_livres( getLabelMesaByMesa() );
//            PrincipalPedidosVisao.pintar_mesas( getLabelMesaByMesa(), mesa );
//            procedimento_mesas_livre();
//            JOptionPane.showMessageDialog( null, "Factura Convertida com sucesso!.." );
        }

    }

    public static void fechar_todas_janelas()
    {
        System.gc();
        for ( Window window : Window.getWindows() )
        {
            window.dispose();
            // por vezes pode ser melhor usar setVisivel(false);
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

    public static void setTotalGeral()
    {
        DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
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

//    public static boolean registrar_forma_pagamento( int id_venda )
//    {
//
//        DefaultTableModel modelo = ( DefaultTableModel ) FormaPagamentoVisao.tabela_forma_pagamento.getModel();
//
//        FormaPagamentoItem formaPagamentoItem;
//        for ( int i = 0; i < modelo.getRowCount(); i++ )
//        {
//            formaPagamentoItem = new FormaPagamentoItem();
//            Integer id_banco = Integer.parseInt( modelo.getValueAt( i, 0 ).toString() );
//            String referencia = ( modelo.getValueAt( i, 2 ) != null ) ? modelo.getValueAt( i, 2 ).toString() : "n/a";
//            String valor = ( modelo.getValueAt( i, 3 ) != null ) ? modelo.getValueAt( i, 3 ).toString() : "0";
//
//            formaPagamentoItem.setValor( new BigDecimal( valor ) );
//            formaPagamentoItem.setReferencia( referencia );
//            formaPagamentoItem.setFkFormaPagamento( new FormaPagamento( id_banco ) );
//            formaPagamentoItem.setFkVenda( new TbVenda( id_venda ) );
//
//            try
//            {
//                if ( !valor.equals( "0" ) )
//                {
//                    FormaPagamentoItemDao.insert( formaPagamentoItem, conexao );
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
//
//    }
//    public static boolean registrar_forma_pagamento( int id_venda )
//    {
//
//        DefaultTableModel modelo = (DefaultTableModel) FormaPagamentoVisao.tabela_forma_pagamento.getModel();
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
////            try
////            {
////
////                System.out.println( "VALOR: " + valor );
////                System.out.println( "ID FORMA PAGAMENTO antes: " + id_forma_pagamento );
////                if ( Double.parseDouble( valor ) > 0 )
////                {
////                    System.out.println( "ID FORMA PAGAMENTO depois: " + id_forma_pagamento );
////
////                    FormaPagamentoItemDao.insert( formaPagamentoItem, conexao );
////                }
////
////            }
////            catch ( Exception e )
////            {
////                e.printStackTrace();
////                return false;
////            }
//        }
//
//        return true;
//    }
//
//    public static boolean registrar_forma_pagamento_lugar( int id_venda, int lugar )
//    {
//
//        DefaultTableModel modelo = (DefaultTableModel) FormaPagamentoLugarVisao.tabela_forma_pagamento.getModel();
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
    public static boolean registrar_forma_pagamento( int id_venda )
    {
        formaPagamentoItemController = new FormaPagamentoItemController( conexaoTransaction );

        DefaultTableModel modelo = (DefaultTableModel) FormaPagamentoVisao.tabela_forma_pagamento.getModel();

        FormaPagamentoItem formaPagamentoItem;
        Contas contas;
        double troco = CfMethods.parseMoedaFormatada( FormaPagamentoVisao.lb_troco.getText() );
        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {

            formaPagamentoItem = new FormaPagamentoItem();
            Integer id_forma_pagamento = Integer.parseInt( modelo.getValueAt( i, 0 ).toString() );
            FormaPagamento formaPagamento = formaPagamentoController.findByDescrisao( modelo.getValueAt( i, 1 ).toString() );
            contas = (Contas) contaController.findById( formaPagamento.getFkContaAssociada() );

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
                                conexaoTransaction
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

        DefaultTableModel modelo = (DefaultTableModel) FormaPagamentoLugarVisao.tabela_forma_pagamento.getModel();

        FormaPagamentoItem formaPagamentoItem;
        Contas contas;
        double troco = CfMethods.parseMoedaFormatada( FormaPagamentoLugarVisao.lb_troco.getText() );
        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {

            formaPagamentoItem = new FormaPagamentoItem();
            Integer id_forma_pagamento = Integer.parseInt( modelo.getValueAt( i, 0 ).toString() );
            FormaPagamento formaPagamento = formaPagamentoController.findByDescrisao( modelo.getValueAt( i, 1 ).toString() );
            contas = (Contas) contaController.findById( formaPagamento.getFkContaAssociada() );

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

    public static void setTotalPagar()
    {

        DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
        double total_pagar = 0;
        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {
            total_pagar += CfMethods.parseMoedaFormatada( modelo.getValueAt( i, 4 ).toString() );
        }
        txtTotalApagar.setText( CfMethods.formatarComoMoeda( total_pagar ) );

    }

    public static void setTotalPagarByLugar( int lugar )
    {

        DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();

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
        DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
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
//            imprimirPedidos();
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

    //Lugar
    public static void salvarItemvendaLugar( int lugar )
    {
        DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();

        Integer cod_venda = vendasController.getLastVenda().getCodigo();
//        int cod_venda = vendaDao.getLastVenda();
        int cod_mesa = mesasDao.getIdByDescricao( getMesa() );
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
                    TbProduto produto_local = (TbProduto) produtosController.findByDesignacao( modelo.getValueAt( i, 2 ).toString() );

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
                    itemVenda.setFkLugares( (TbLugares) lugaresController.findById( DVML.LUGAR_BALCAO ) );
//                itemVenda.setFkLugares( lugarDao.findTbLugares( lugarDao.getIdByDescricao( lugar ) ) );
                    itemVenda.setFkMesas( (TbMesas) mesasController.findByDesignacao( getMesa() ) );

                    //cria o item venda
//                    itemVendaDao.create ( itemVenda );
                    if ( !itemVendasController.salvar( itemVenda ) )
                    {
                        DocumentosController.rollBackTransaction( conexaoTransaction );
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
                DocumentosController.rollBackTransaction( conexaoTransaction );
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

        DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
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

        int cod_pedido = pedidoDao.getLastPedidoByDefignacaoMesaFALSE( getMesa() );
        List<TbItemPedidos> list = itemPedidosDao.buscaTodosItemPedidos( cod_pedido, DVML.LUGAR_BALCAO );

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

        int cod_pedido = pedidoDao.getLastPedidoByDefignacaoMesaFALSE( getMesa() );
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

        try
        {

            List<String> lista_categoria = tipoProdutosController.buscaTodasCategoriaNaoIngredientes();
//        List<TbTipoProduto> lista_categoria = tipoProdutoDao.getAll_filtro();
            this.TAMANHO_CENTRO = lista_categoria.size();

            if ( this.TAMANHO_CENTRO > 0 )
            {
                int raiz_quadrada = (int) Math.sqrt( TAMANHO_CENTRO );

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

                    JButton jButton = new JButton( lista_categoria.get( i ) );
                    jButton.addActionListener( new ButtonHandler1() );
                    botoes_object.add( jButton );
                    painel_central.add( jButton );

                }

            }

//        this.gl = new GridLayout( TAMANHO_CENTRO, 1 );
        }
        catch ( Exception e )
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

                MetodosUtil.verificarCaixa( caixasController,
                        idUser,
                        RootVisao.btn_abertura_dia_root,
                        RootVisao.btn_abertura_dia_root,
                        btnFormaPagamentoVendasPraticas );

            }

        } );

    }

    private void adicionar_centro_botao( int idTipoPorduto )
    {
        BDConexao conexaoLocal = new BDConexao();
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
                    ? stocksController.get_all_produtos_by_id_tipo_produto_and_id_armazem( idTipoPorduto, id_armzem, conexaoLocal )
                    : produtosController.getProdutosByTipoProduto( idTipoPorduto );
            painel_central.removeAll();
            jScrollPane3.setViewportView( painel_central );

            TbUsuario usuarioLocal = (TbUsuario) usuariosController.findById( idUser );

            botoes_object.clear();
            this.TAMANHO_CENTRO = lista_prdutos.size();
            if ( !Objects.isNull( lista_prdutos ) && TAMANHO_CENTRO > 0 )
            {

                boolean adcionar_imagem = true;

                System.out.println( "TAMANHO CENTRO: " + TAMANHO_CENTRO );

                int raiz_quadrada = (int) Math.sqrt( TAMANHO_CENTRO );
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
                    double quantidadeProduto = stocksController.getQuantidadeProduto( get.getCodigo(), id_armzem, conexaoLocal );

                    painel_central.add( new ProdutoItemVisao( get.getStocavel(), get.getDesignacao(), get.getPhoto(), quantidadeProduto, preco_object, DVML.FORMULARIO_VENDA_EXPRESSO, usuarioLocal.getIdTipoUsuario().getIdTipoUsuario() ) );
//                    painel_central.add( new ProdutoItemVisao(  get.getCodigo(),  get.getStocavel(), get.getDesignacao(), get.getPhoto(), quantidadeProduto, preco_object, DVML.FORMULARIO_PEDIDO_RESTAURANTE, conexao ) );

                }

            }
            else
            {
                System.out.println( "LISTA DE PRODUTO VAZIA." );
            }

            conexaoLocal.close();

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

                    if ( qtd_possivel( designacao ) )
                    {
                        try
                        {
//                            if ( !validar_qtd_zero() )
//                            {
                            procedimento_salvar_pedidos_iten_pedidos( designacao );
//                            }
//                            else
//                            {
//                                JOptionPane.showMessageDialog( null, "Nao existe quantidade no stock.", "Aviso", JOptionPane.WARNING_MESSAGE );
//                            }
//                                status_button_lugar( true );
                        }
                        catch ( Exception e )
                        {
                            JOptionPane.showMessageDialog( null, "Fallha ao adicionar o pedido", DVML.DVML_COMERCIAL, JOptionPane.ERROR_MESSAGE );
                            e.printStackTrace();
                        }

                    }

                    else
                    {
                        JOptionPane.showMessageDialog( null, "Atenção\nA quantidade não pode ser igual a zero!" );
                    }
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

//    private void activar_button_lugar( boolean bt1, boolean bt2, boolean bt3, boolean bt4, boolean bt5, boolean bt6, boolean bt7, boolean bt8, boolean bt9, boolean bt10 )
//    {
//
//        status_button_lugar( true );
//        btn_01.setEnabled( bt1 );
//        btn_02.setEnabled( bt2 );
//        btn_03.setEnabled( bt3 );
//        btn_04.setEnabled( bt4 );
//        btn_05.setEnabled( bt5 );
//        btn_06.setEnabled( bt6 );
//        btn_07.setEnabled( bt7 );
//        btn_08.setEnabled( bt8 );
//        btn_09.setEnabled( bt9 );
//        btn_10.setEnabled( bt10 );
//
//    }
//
//    private static void status_button_lugar( boolean status )
//    {
//        btn_01.setEnabled( status );
//        btn_02.setEnabled( status );
//        btn_03.setEnabled( status );
//        btn_04.setEnabled( status );
//        btn_05.setEnabled( status );
//        btn_06.setEnabled( status );
//        btn_07.setEnabled( status );
//        btn_08.setEnabled( status );
//        btn_09.setEnabled( status );
//        btn_10.setEnabled( status );
//
//    }
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
        DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
//        int id_item_consumo_alojamento = Integer.parseInt( modelo.getValueAt( linha_acutal, 0 ).toString() );

        try
        {

            new SenhaMestreVisao( this, rootPaneCheckingEnabled, DVML.FORMULARIO_VENDA_EXPRESSO ).setVisible( true );
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
        DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
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
        DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
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
//                    PrincipalPedidosVisao.mesas_livres( getLabelMesaByMesa() );
//                    PrincipalPedidosVisao.pintar_mesas( getLabelMesaByMesa(), mesa );

                    Productos p = new Productos();
                    p.setConsumo( consumo );
                    p.setLugar( getLugar() );
                    p.setMesa( getMesa() );
                    p.setQtd( qtd );
                    p.setPreco( preco_unitario );
                    p.setUsuario( usuario );
                    p.setDataHora( new Date() );

                    if ( ProductoDao.insert( p, conexaoTransaction ) )
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
//    }
    public static int getQuantidadeProduto( int cod_produto )
    {

        BDConexao conexaoLocal = new BDConexao();
        String sql = "SELECT quantidade_existente FROM  tb_stock WHERE  cod_produto_codigo = " + cod_produto + " AND cod_armazem = " + id_armzem;

        ResultSet rs = conexaoLocal.executeQuery( sql );

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

        conexaoLocal.close();

        return 0;
    }

    public static int getQuantidadeProduto( int cod_produto, int idArmazem )
    {

        BDConexao conexaoLocal = new BDConexao();
        String sql = "SELECT quantidade_existente FROM  tb_stock WHERE  cod_produto_codigo = " + cod_produto + " AND cod_armazem = " + idArmazem;

        ResultSet rs = conexaoLocal.executeQuery( sql );

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

        conexaoLocal.close();

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

    public static void actualizar_quantidade( int cod, double quantidade, int idArmazem )
    {
//        public  void actualizar_quantidade(int cod, int quantidade) {

        BDConexao conexaoLocal = new BDConexao();
        String sql = "UPDATE tb_stock SET quantidade_existente =  " + ( getQuantidadeProduto( cod, idArmazem ) - quantidade ) + " WHERE cod_produto_codigo = " + cod + " AND cod_armazem = " + idArmazem;
//        String sql = "UPDATE tb_stock SET quantidade_existente =  "  + ( getQuantidadeProduto(cod) - quantidade)     +" WHERE cod_produto_codigo = "   +cod;
        conexaoLocal.executeUpdate( sql );
        conexaoLocal.close();

    }

//    public static boolean possivel_quantidade_pedidos_existentes( int codigo_produto )
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
        BDConexao conexaoLocal = new BDConexao();
        stocksController = new StoksController( conexaoLocal );

        TbStock stock = stocksController.getStockByIdProdutoAndIdArmazem( codigo_produto, id_armzem );
        double qtd_minima = stock.getQuantBaixa(),
                qtd_existente = stock.getQuantidadeExistente(),
                qtd_critica = stock.getQuantCritica();

        boolean resultado = qtd_minima < qtd_existente
                && qtd_existente <= qtd_critica;

        conexaoLocal.close();
        return resultado;

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
//                if (  ! possivel_quantidade_pedidos_existentes ( getQtdPedidosTabela ( codigo_produto ) ) )
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

        DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
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

        DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
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
        BDConexao conexaoLocal = new BDConexao();
        TbStock stock = stocksController.getStockByIdProdutoAndIdArmazem( codigo_produto, 1 );
        double quant_possivel = stock.getQuantidadeExistente() - stock.getQuantBaixa();

        conexaoLocal.close();
        return quant_possivel + ( 1 + MetodosUtil.getQtdInItemPedidos( conexaoLocal, codigo_produto ) );
    }

    public void remover_item_all_pedidos_amais_tabela( int cod_produto, int qtd )
    {

        DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
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
//    private static JLabel getLabelMesaByMesa()
//    {
//
//        if ( mesa.equals( "MESA 1" ) )
//        {
//            return PrincipalPedidosVisao.lb_mesa_01;
//        }
//        else if ( mesa.equals( "MESA 2" ) )
//        {
//            return PrincipalPedidosVisao.lb_mesa_02;
//        }
//        else if ( mesa.equals( "MESA 3" ) )
//        {
//            return PrincipalPedidosVisao.lb_mesa_03;
//        }
//        else if ( mesa.equals( "MESA 4" ) )
//        {
//            return PrincipalPedidosVisao.lb_mesa_04;
//        }
//        else if ( mesa.equals( "MESA 5" ) )
//        {
//            return PrincipalPedidosVisao.lb_mesa_05;
//        }
//        else if ( mesa.equals( "MESA 6" ) )
//        {
//            return PrincipalPedidosVisao.lb_mesa_06;
//        }
//        else if ( mesa.equals( "MESA 7" ) )
//        {
//            return PrincipalPedidosVisao.lb_mesa_07;
//        }
//        else if ( mesa.equals( "MESA 8" ) )
//        {
//            return PrincipalPedidosVisao.lb_mesa_08;
//        }
//        else if ( mesa.equals( "MESA 9" ) )
//        {
//            return PrincipalPedidosVisao.lb_mesa_09;
//        }
//        else if ( mesa.equals( "MESA 10" ) )
//        {
//            return PrincipalPedidosVisao.lb_mesa_10;
//        }
//        else if ( mesa.equals( "MESA 11" ) )
//        {
//            return PrincipalPedidosVisao.lb_mesa_11;
//        }
//        else if ( mesa.equals( "MESA 12" ) )
//        {
//            return PrincipalPedidosVisao.lb_mesa_12;
//        }
//        else if ( mesa.equals( "MESA 13" ) )
//        {
//            return PrincipalPedidosVisao.lb_mesa_13;
//        }
//        else if ( mesa.equals( "MESA 14" ) )
//        {
//            return PrincipalPedidosVisao.lb_mesa_14;
//        }
//        else if ( mesa.equals( "MESA 15" ) )
//        {
//            return PrincipalPedidosVisao.lb_mesa_15;
//        }
//        else if ( mesa.equals( "MESA 16" ) )
//        {
//            return PrincipalPedidosVisao.lb_mesa_16;
//        }
//        else if ( mesa.equals( "MESA 17" ) )
//        {
//            return PrincipalPedidosVisao.lb_mesa_17;
//        }
//        else if ( mesa.equals( "MESA 18" ) )
//        {
//            return PrincipalPedidosVisao.lb_mesa_18;
//        }
//        else if ( mesa.equals( "MESA 19" ) )
//        {
//            return PrincipalPedidosVisao.lb_mesa_19;
//        }
//        else if ( mesa.equals( "MESA 20" ) )
//        {
//            return PrincipalPedidosVisao.lb_mesa_20;
//        }
//        else if ( mesa.equals( "MESA 21" ) )
//        {
//            return PrincipalPedidosVisao.lb_mesa_21;
//        }
//        else if ( mesa.equals( "MESA 22" ) )
//        {
//            return PrincipalPedidosVisao.lb_mesa_22;
//        }
//        else if ( mesa.equals( "MESA 23" ) )
//        {
//            return PrincipalPedidosVisao.lb_mesa_23;
//        }
//        else if ( mesa.equals( "MESA 24" ) )
//        {
//            return PrincipalPedidosVisao.lb_mesa_24;
//        }
//        else if ( mesa.equals( "MESA 25" ) )
//        {
//            return PrincipalPedidosVisao.lb_mesa_25;
//        }
//        else if ( mesa.equals( "MESA 26" ) )
//        {
//            return PrincipalPedidosVisao.lb_mesa_26;
//        }
//        else if ( mesa.equals( "MESA 27" ) )
//        {
//            return PrincipalPedidosVisao.lb_mesa_27;
//        }
//        else if ( mesa.equals( "MESA 28" ) )
//        {
//            return PrincipalPedidosVisao.lb_mesa_28;
//        }
//        else if ( mesa.equals( "MESA 29" ) )
//        {
//            return PrincipalPedidosVisao.lb_mesa_29;
//        }
//        else if ( mesa.equals( "MESA 30" ) )
//        {
//            return PrincipalPedidosVisao.lb_mesa_30;
//        }
//        else if ( mesa.equals( "MESA 31" ) )
//        {
//            return PrincipalPedidosVisao.lb_mesa_31;
//        }
//        else if ( mesa.equals( "MESA 32" ) )
//        {
//            return PrincipalPedidosVisao.lb_mesa_32;
//        }
//        else if ( mesa.equals( "MESA 33" ) )
//        {
//            return PrincipalPedidosVisao.lb_mesa_33;
//        }
//        else if ( mesa.equals( "MESA 34" ) )
//        {
//            return PrincipalPedidosVisao.lb_mesa_34;
//        }
//        else if ( mesa.equals( "MESA 35" ) )
//        {
//            return PrincipalPedidosVisao.lb_mesa_35;
//        }
//        else if ( mesa.equals( "MESA 36" ) )
//        {
//            return PrincipalPedidosVisao.lb_mesa_36;
//        }
//        else if ( mesa.equals( "MESA 37" ) )
//        {
//            return PrincipalPedidosVisao.lb_mesa_37;
//        }
//        else if ( mesa.equals( "MESA 38" ) )
//        {
//            return PrincipalPedidosVisao.lb_mesa_38;
//        }
//        else if ( mesa.equals( "MESA 39" ) )
//        {
//            return PrincipalPedidosVisao.lb_mesa_39;
//        }
//        else
//        {
//            System.out.println( "CHEGUEI AQUI NA MESA 40." );
//            return PrincipalPedidosVisao.lb_mesa_40;
//        }
//
//    }
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
        DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
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
        DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
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
        DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
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
                double valor_unitario = (preco_unitario * qtd);
                desconto_valor_linha = valor_unitario * ( ( valor_percentagem ) / 100 );
                imposto += ( ( valor_unitario - desconto_valor_linha ) * ( taxa / 100 ) );

            }

        }

        return imposto;
    }

    private static double getTotalImposto( int lugar )
    {
        DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
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
                    double valor_unitario = (preco_unitario * qtd);
                    desconto_valor_linha = valor_unitario * ( ( valor_percentagem ) / 100 );
                    imposto += ( ( valor_unitario - desconto_valor_linha ) * ( taxa / 100 ) );

                }
            }

        }

        return imposto;
    }

    private static double getTotalAOALiquido()
    {
        double valores = (getTotalIliquido() + getTotalImposto());
        double descontos = (getDescontoComercial() + getDescontoFinanceiro());
        System.out.println( "TotalIliquido: " + getTotalIliquido() );
        System.out.println( "TotalImposto: " + getTotalImposto() );
        System.out.println( "TotalDescontoComercial: " + getDescontoComercial() );
        System.out.println( "TotalDescontoFinanceiro: " + getDescontoFinanceiro() );
        System.out.println( "Total Liquido: " + ( valores - descontos ) );
        return ( valores - descontos );
    }

    private static double getTotalAOALiquido( int lugar )
    {
        double valores = (getTotalIliquido( lugar ) + getTotalImposto( lugar ));
        double descontos = (getDescontoComercial( lugar ) + getDescontoFinanceiro( lugar ));
        System.out.println( "TotalIliquido: " + getTotalIliquido( lugar ) );
        System.out.println( "TotalImposto: " + getTotalImposto( lugar ) );
        System.out.println( "TotalDescontoComercial: " + getDescontoComercial( lugar ) );
        System.out.println( "TotalDescontoFinanceiro: " + getDescontoFinanceiro( lugar ) );
        System.out.println( "Total Liquido: " + ( valores - descontos ) );
        return ( valores - descontos );
    }

    private static double getTotalIncidencia()
    {
        DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
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
                double valor_unitario = (preco_unitario * qtd);
                incidencia += ( ( valor_unitario ) - ( valor_unitario * desconto_valor_linha ) );

            }

        }

        return incidencia;
    }

    private static double getTotalIncidencia( int lugar )
    {
        DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
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
                    double valor_unitario = (preco_unitario * qtd);
                    incidencia += ( ( valor_unitario ) - ( valor_unitario * desconto_valor_linha ) );

                }
            }

        }

        return incidencia;
    }

    private static double getTotalIncidenciaIsento()
    {
        DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
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
                double valor_unitario = (preco_unitario * qtd);
                incidencia_isento += ( ( valor_unitario ) - ( valor_unitario * desconto_valor_linha ) );

            }

        }

        return incidencia_isento;
    }

    private static double getTotalIncidenciaIsento( int lugar )
    {
        DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
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
                    double valor_unitario = (preco_unitario * qtd);
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

        DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
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

        DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
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
        DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
        String lugar_valor_tabela = modelo.getValueAt( linha, 1 ).toString();
        return lugaresController.getIdByDescricao( lugar_valor_tabela );
//        return lugarDao.getIdByDescricao( lugar_valor_tabela );

    }

    public static void verificarCaixa()
    {

        if ( !caixasController.existeCaixas() )
        {
            BT_Pedidos.setEnabled( false );
            btnFormaPagamentoVendasPraticas.setEnabled( false );
            status_mensagem_primaria.setText( "" );
        }
        else if ( caixasController.existe_abertura() && caixasController.existe_fecho() )
        {
            btnFormaPagamentoVendasPraticas.setEnabled( false );
            btnFormaPagamentoVendasPraticas.setEnabled( false );
            status_mensagem_primaria.setText( "Deves abrir o caixa" );

        }
        else
        {
            btnFormaPagamentoVendasPraticas.setEnabled( true );
            btnFormaPagamentoVendasPraticas.setEnabled( true );
            status_mensagem_primaria.setText( "" );
        }
    }

//    private void setWindowsListener()
//    {
//
//        this.addWindowListener( new WindowAdapter()
//        {
//            @Override
//            public void windowActivated( WindowEvent e )
//            {
//                mostrar_proximo_codigo_documento();
//            }
//
//        } );
//
//    }
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
        AnoEconomico anoEconomicoLocal = (AnoEconomico) anoEconomicoController.findById( getIdAnoEconomico() );
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
        System.out.println( "Armazem Selecionado na Area: " + cmbArmazem.getSelectedItem().toString() );
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
        BDConexao conexaoLocal = new BDConexao();
        ProdutosController produtosControllerLocal = new ProdutosController( conexaoLocal );

        try
        {
//            int codigo = produtoDao.getIdByDescricao( designacao );

            String designacao1 = produtosControllerLocal.getDescricaoById( codigo );
            System.out.println( "Codigo do Produto no Armazem: " + codigo );
            System.out.println( "Designacao do Produto no Armazem: " + designacao1 );
            //int   codigo = Integer.parseInt(txtCodigoProduto.getText() );
//            TbProduto produto = produtosControllerLocal.findByCod( codigo );
//            cmbFamilia.setSelectedItem( produto.getCodTipoProduto().getFkFamilia().getDesignacao() );
//            cmbSubFamilia.setSelectedItem( produto.getCodTipoProduto().getDesignacao() );
//            cmbProduto.setSelectedItem( produto.getDesignacao() );

            adicionar_preco_quantidade_anitgo();
            System.out.println( "### PASSEI O PROC. ADCIONAR PREÇO" );

            procedimento_adicionar( designacao1 );
//            }
//            txtCodigoProduto.setText( "" );
//            txtQuatindade.setText( "1" );
//            txtQuatindade.requestFocus();

        }
        catch ( Exception ex )
        {
            ex.printStackTrace();
            Logger.getLogger( NovaGestaoPedidosVisao.class.getName() ).log( Level.SEVERE, null, ex );
            JOptionPane.showMessageDialog( null, "Este produto não existe no armazém " + cmbArmazem.getSelectedItem(), DVML.DVML_COMERCIAL, JOptionPane.ERROR_MESSAGE );
        }

        conexaoLocal.close();

    }

//    public static void accao_codigo_interno_enter_busca_exterior( int codigo )
//    {
//
//        try
//        {
////            int codigo = produtoDao.getIdByDescricao( designacao );
//
//            String designacao1 = produtosController.getDescricaoById( codigo );
//            System.out.println( "Codigo do Produto no Armazem 2" + codigo );
//            System.out.println( "Designacao do Produto no Armazem 2" + designacao1 );
//            //int   codigo = Integer.parseInt(txtCodigoProduto.getText() );
//            TbProduto produto = produtosController.findByCod( codigo );
////            cmbFamilia.setSelectedItem( produto.getCodTipoProduto().getFkFamilia().getDesignacao() );
////            cmbSubFamilia.setSelectedItem( produto.getCodTipoProduto().getDesignacao() );
////            cmbProduto.setSelectedItem( produto.getDesignacao() );
//
//            adicionar_preco_quantidade_anitgo();
//
//            procedimento_adicionar( designacao1 );
////            }
////            txtCodigoProduto.setText( "" );
////            txtQuatindade.setText( "1" );
////            txtQuatindade.requestFocus();
//
//        }
//        catch ( Exception ex )
//        {
//            Logger.getLogger( NovaGestaoPedidosVisao.class.getName() ).log( Level.SEVERE, null, ex );
//            JOptionPane.showMessageDialog( null, "Este produto não existe no armazém " + cmbArmazem.getSelectedItem(), DVML.DVML_COMERCIAL, JOptionPane.ERROR_MESSAGE );
//        }
//
//    }
    public static void adicionar_preco_quantidade_anitgo()
    {
        BDConexao conexaoLocal = new BDConexao();
        StoksController stockControllerLocal = new StoksController( conexaoLocal );
        try
        {

            TbStock stock = stockControllerLocal.getStockByIdProdutoAndIdArmazem( getCodigoProduto(), getCodigoArmazem() );

            if ( stock != ( null ) )
            {

                txtQuantidadeStock.setText( String.valueOf( conexaoLocal.getQtdExistenteStock( getCodigoProduto(), getCodigoArmazem() ) ) );

                System.err.println( "Codigo Produto:  " + getCodigoProduto() );

            }

//            }
        }
        catch ( Exception ex )
        {
            ex.printStackTrace();
            Logger.getLogger( VendasPraticasVisao.class.getName() ).log( Level.SEVERE, null, ex );
        }
        conexaoLocal.close();
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
//    public static void procedimento_adicionar( String designacao )
//    {
//
//        BDConexao conexaoLocal = new BDConexao();
//        ProdutosController produtosControllerLocal = new ProdutosController( conexaoLocal );
//        int codigo_produto = produtosControllerLocal.getIdProduto( designacao );
//        System.out.println( "Aqui vai o codigo" + codigo_produto );
//        System.out.println( "### ENTREI NO O PROC. ADCIONAR PELA DESIGNAÇÃO" );
////        String designacao1 = produtosController.getDescricaoById( codigo_produto );getLugarSelecionado
////        System.out.println( "Aqui vai a designacao" + designacao1 );
//        TbProduto produto_local = produtosControllerLocal.findByCod( codigo_produto );
////        if ( activo_um_lugar() )
////        {
//        if ( qtd_possivel( produto_local.getDesignacao(), Double.parseDouble( txtQuantidade.getText() ) ) )
//        {
//            try
//            {
//
//                procedimento_salvar_pedidos_iten_pedidos_interno( produto_local.getDesignacao() );
////                    status_button_lugar( true );
//            }
//            catch ( Exception e )
//            {
//                JOptionPane.showMessageDialog( null, "Fallha ao adicionar o pedido", DVML.DVML_COMERCIAL, JOptionPane.ERROR_MESSAGE );
//                e.printStackTrace();
//            }
//        }
//        else
//        {
//            JOptionPane.showMessageDialog( null, "Atenção!!!\nNão existe quantidade no stock.", "Aviso", JOptionPane.WARNING_MESSAGE );
//        }
//
////        }
//        conexaoLocal.close();
//
//    }
//    
    public static void procedimento_adicionar( String designacao )
    {

        BDConexao conexaoLocal = new BDConexao();
        ProdutosController produtosControllerLocal = new ProdutosController( conexaoLocal );
        int codigo_produto = produtosControllerLocal.getIdProduto( designacao );
        System.out.println( "Aqui vai o codigo" + codigo_produto );
        System.out.println( "### ENTREI NO O PROC. ADCIONAR PELA DESIGNAÇÃO" );
//        String designacao1 = produtosController.getDescricaoById( codigo_produto );getLugarSelecionado
//        System.out.println( "Aqui vai a designacao" + designacao1 );
        TbProduto produto_local = produtosControllerLocal.findByCod( codigo_produto );
//        if ( activo_um_lugar() )
//        {
        if ( qtd_possivel( produto_local.getDesignacao(), Double.parseDouble( txtQuantidade.getText() ) ) )
        {
            try
            {

                procedimento_salvar_pedidos_iten_pedidos_interno( produto_local.getDesignacao() );

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
        conexaoLocal.close();

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
        BDConexao conexaoLocal = new BDConexao();
        TbStock stock = stocksController.getStockByIdProdutoAndIdArmazem1( getCodigoProduto(), getCodigoArmazem(), conexaoLocal );
        double qtd_minima = stock.getQuantBaixa(),
                qtd_existente = stock.getQuantidadeExistente(),
                qtd_critica = stock.getQuantCritica();
        try
        {
            return conexaoLocal.getQuantidade_minima_publico( getCodigoProduto(), getCodigoArmazem() )
                    < conexaoLocal.getQuantidade_Existente_Publico( getCodigoProduto(), getCodigoArmazem() )
                    && conexaoLocal.getQuantidade_Existente_Publico( getCodigoProduto(), getCodigoArmazem() )
                    <= conexaoLocal.getQuantidade_critica_public( getCodigoProduto(), getCodigoArmazem() );

        }
        catch ( Exception e )
        {
        }
        conexaoLocal.close();

//   
//        return qtd_minima < qtd_existente
//                && qtd_existente <= qtd_critica;
        return true;
    }

    private static boolean qtd_possivel( String designacao )
    {
        BDConexao conexaoLocal = new BDConexao();
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
            double qtdExistentesNoutrosPedidos = ItemPedidosDao.getQtdItensByIdPedidoTradicional( idProduto, conexaoLocal );
            qtdExistentesNoutrosPedidos++;// Mais Um porque causa do novo pedido actual
            double qtdExistente = conexaoLocal.getQuantidade_Existente_Publico( idProduto, getCodigoArmazem() );

            return qtdExistente >= qtdExistentesNoutrosPedidos;

        }
        conexaoLocal.close();
        return true;

    }

    private static boolean qtd_possivel( String designacao, double qtd )
    {
        conexaoTransaction = new BDConexao();
        DocumentosController.startTransaction( conexaoTransaction );
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
            double qtdExistentesNoutrosPedidos = ItemPedidosDao.getQtdItensByIdPedidoTradicional( idProduto, conexaoTransaction );
            double qtdExistente = conexaoTransaction.getQuantidade_Existente_Publico( idProduto, getCodigoArmazem() );
            System.out.println( "Resultado noutros pedidos = " + ( qtdExistentesNoutrosPedidos ) );
            System.out.println( "Resultado de pedidos = " + ( qtdExistentesNoutrosPedidos + qtd ) );
            System.out.println( "Resultado Existente1 = " + ( qtdExistente ) );
            System.out.println( "Armazem = " + ( getCodigoArmazem() ) );
            return qtdExistente >= ( qtdExistentesNoutrosPedidos + qtd );

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

            try
            {
                itemPedidosLocal.setQtd( 1 );
                itemPedidosLocal.setTotalItem( itemPedidosLocal.getTotalItem() * 1 );
                itemPedidosDao.edit( itemPedidosLocal );
            }
            catch ( Exception e )
            {
            }

            int qtd;

            try
            {
                qtd = Integer.parseInt( jTable1.getValueAt( linhaSelecionada, 3 ).toString() );
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

            if ( !possivel_quantidade_pedidos_existentes( itemPedidosLocal.getFkProdutos().getCodigo() ) ) //verifica se é possivel adicionar
            {
                resetQtd( "Impossivel para esta quantidade no stock" );
            }
            else if ( possivel_quantidade( itemPedidosLocal.getFkProdutos().getCodigo(), qtd ) ) //verifica se  há disponiblidade ao  adicionar depois da  'qtd'  ser lançada.
            {
                int idProduto = itemPedidosLocal.getFkProdutos().getCodigo();
                double preco = precoDao.findTbPreco( precoDao.getUltimoIdPrecoByIdProduto( idProduto, qtd ) ).getPrecoVenda().doubleValue();

                double taxa = MetodosUtil.getTaxaPercantagem( idProduto );
                double desconto = 0;
                double total = FinanceUtils.getValorComIVA( qtd, taxa, preco, desconto );
                itemPedidosLocal.setQtd( qtd );
                itemPedidosLocal.setTotalItem( total );
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

//    private void selecionar_documento()
//    {
//
//        if ( cmbTipoDocumento.getSelectedItem().equals( "Factura/Recibo" ) )
//        {
//            BT_Conversao.setVisible( true );
//            BtnFCons.setVisible( false );
//        }
//        else if ( cmbTipoDocumento.getSelectedItem().equals( "Factura Consulta" ) )
//        {
//            BtnFCons.setVisible( true );
//            BT_Conversao.setVisible( false );
//        }
//
//    }
    private void selecionar_documento()
    {

        if ( cmbTipoDocumento.getSelectedItem().equals( "Factura/Recibo" ) )
        {
            btnFormaPagamentoVendasPraticas.setVisible( true );
            BtnFCons.setVisible( false );
            btFT.setVisible( false );
        }
        else if ( cmbTipoDocumento.getSelectedItem().equals( "Factura" ) )
        {
            btFT.setVisible( true );
            BtnFCons.setVisible( false );
            btnFormaPagamentoVendasPraticas.setVisible( false );
        }
        else if ( cmbTipoDocumento.getSelectedItem().equals( "Consulta Mesa" ) )
        {
            BtnFCons.setVisible( true );
            btnFormaPagamentoVendasPraticas.setVisible( false );
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
        TbProduto produtoLocal = produtosController.getProdutoByDesignacao( designacao );

        if ( produtoLocal.getStocavel().equals( "false" ) )
        {
            TbDadosInstituicao dadosInstituicaoLocal = dadosInstituicaoController.findByCodigo( 1 );
            if ( dadosInstituicaoLocal.getNegocio().equals( "Restaurante" ) )
            {
                int qtd = Integer.parseInt( txtQuantidade.getText() );
                FichaTecnica fichaTecnica = fichaTecnicaController.getFichaByRefeicao( produtoLocal.getCodigo() );
                if ( Objects.nonNull( fichaTecnica ) )//existe ficha tecnica
                {
                    List<LinhaFichaTecnica> linhasFichaTecnica = linhaFichaTecnicaController.listarTodosByCodigoFichaTecnica( fichaTecnica.getId() );
                    if ( possivelAdicionar( qtd, fichaTecnica.getPrato(), linhasFichaTecnica ) )
                    {
                        procedimento_salvar_pedidos_iten_pedidos( designacao );
                    }
                }
                else
                {
                    procedimento_salvar_pedidos_iten_pedidos( designacao );
                }
            }
            else
            {
                procedimento_salvar_pedidos_iten_pedidos( designacao );
            }

        }

        else if ( qtd_possivel( designacao, Double.parseDouble( txtQuantidade.getText() ) ) )
        {
            try
            {
                procedimento_salvar_pedidos_iten_pedidos( designacao );
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
            return tipoProdutosController.getTipoFamiliaByDesignacao( String.valueOf( cmbSubFamilia.getSelectedItem() ) ).getCodigo();

        }
        catch ( Exception e )
        {
            return 0;
        }

    }

//    public void adicionar_preco_quantidade()
//    {
//
//        try
//        {
//
//            TbProduto produto_local = (TbProduto) produtosController.findById( getCodigoProduto() );
//            stocksController = new StoksController( conexao );
//            TbStock stockLocal = stocksController.getStockByIdProdutoAndIdArmazem( produto_local.getCodigo(), getCodigoArmazem() );
//
//            boolean isStocavel = produto_local.getStocavel().equals( "true" );
//
//            if ( isStocavel && stockLocal.getQuantidadeExistente() <= stockLocal.getQuantCritica() )
//            {
//
//                txtQuantidadeStock.setBackground( Color.RED );
//                txtQuantidadeStock.setForeground( Color.BLACK );
//            }
//            else
//            {
//                txtQuantidadeStock.setBackground( new Color( 51, 153, 0, 255 ) );
//            }
//
//            txtCodigoBarra.setText( String.valueOf( produto_local.getCodBarra() ) );
//            //actualizar
//            txtLocal.setText( String.valueOf( produto_local.getCodLocal().getDesignacao() ) );
//            txtCodigoProduto.setText( String.valueOf( produto_local.getCodigo() ) );
//
//            if ( isStocavel && !Objects.isNull( stockLocal ) )
//            {
//                txtQuantidadeStock.setText( String.valueOf( stockLocal.getQuantidadeExistente() ) );
//            }
//            else
//            {
//                txtQuantidadeStock.setText( "0" );
//            }
//
//        }
//        catch ( Exception ex )
//        {
//            ex.printStackTrace();
//            txtQuantidadeStock.setText( "0" );
//            Logger.getLogger( VendasPraticasVisao.class.getName() ).log( Level.SEVERE, null, ex );
//        }
//
//    }
    public void adicionar_preco_quantidade()
    {

        try
        {
            BDConexao conexaoLocal = new BDConexao();
            TbProduto produto_local = (TbProduto) produtosController.findById( getCodigoProduto1() );

            TbStock stockLocal = stocksController.getStockByIdProdutoAndIdArmazem1( getCodigoProduto1(), getCodigoArmazem(), conexaoLocal );
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
            conexaoLocal.close();
        }
        catch ( Exception ex )
        {
            ex.printStackTrace();
            txtQuantidadeStock.setText( "0" );
            Logger.getLogger( VendasPraticasVisao.class.getName() ).log( Level.SEVERE, null, ex );
        }

    }

    public static boolean possivel_quantidade() throws SQLException
    {

        double quant_possivel = conexao.getQuantidade_Existente_Publico( getIdProduto(), id_armzem ) - conexao.getQuantidade_minima_publico( getIdProduto(), id_armzem );
        //int quant_possivel = stock.getQuantidadeExistente() -  stock.getQuantBaixa();

        return quant_possivel >= getQuantidade();

    }

    public static int getIdProduto()
    {
        System.err.println( "QUANTIDADE DIGTADA:" + txtCodigoProduto.getText().trim() );
        return Integer.parseInt( txtCodigoProduto.getText().trim() );
    }

    public static double getQuantidade()
    {
        System.err.println( "QUANTIDADE DIGTADA:" + txtQuantidade.getText().trim() );
        return Double.parseDouble( txtQuantidade.getText().trim() );
    }

    public static int getCodigoProduto() throws SQLException
    {
        try
        {
            int codProduto = Integer.parseInt( txtCodigoProduto.getText() );
            return codProduto;
        }
        catch ( Exception e )
        {
        }

        return 0;

    }

    public static int getCodigoProduto1()
    {
        //return conexao.getCodigoPublico("tb_produto", String.valueOf(  cmbProduto.getSelectedItem()));   
        return produtosController.findByDesignacao( cmbProduto.getSelectedItem().toString() ).getCodigo();

    }

    private static void procedimentoAdicionarTabela( TbProduto produto )
    {
        try
        {
            if ( !Objects.isNull( produto ) )
            {
                Integer codTipoProduto = produto.getCodTipoProduto().getCodigo();
                TbTipoProduto tipoProduto = (TbTipoProduto) tipoProdutosController.findById( codTipoProduto );
                Integer codFamilia = tipoProduto.getFkFamilia().getPkFamilia();
                Familia familia = (Familia) familiaController.findById( codFamilia );
                cmbFamilia.setSelectedItem( familia.getDesignacao() );
                cmbSubFamilia.setSelectedItem( tipoProduto.getDesignacao() );

                cmbProduto.setModel( new DefaultComboBoxModel( produtosController.getVector() ) );
                cmbProduto.setSelectedItem( produto.getDesignacao() );
                adicionar_preco_quantidade_anitgo();

                procedimento_adicionar( produto.getDesignacao() );

//                txtCodigoProduto.setText( "" );
                txtCodigoBarra.setText( "" );
                txtQuantidade.requestFocus();

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

    public static boolean validar_qtd_zero()
    {
        return Double.parseDouble( txtQuantidade.getText() ) == 0;
    }

    private static boolean possivelAdicionar( int qtdPrato, String prato, List<LinhaFichaTecnica> linhasFichaTecnica )
    {
        boolean possivel = true;
        String msg = "O prato: " + prato + " não poder ser vendido, porque o(s) ingrediente(s): \n";

        BDConexao conexaoLocal = new BDConexao();
        for ( LinhaFichaTecnica l : linhasFichaTecnica )
        {

            double qtdExistente = 0;
            try
            {
                qtdExistente = conexaoLocal.getQuantidade_Existente_Publico( l.getIgrendienteId(), DVML.ARMAZEM_DEFAUTL );
            }
            catch ( Exception e )
            {
                e.printStackTrace();
            }

            if ( qtdExistente < ( qtdPrato * l.getQtdBruto() ) )
            {
                msg += " " + l.getIgrendienteDesignacao() + ",";
                possivel = possivel && false;
            }
            else
            {
                possivel = possivel && true;
            }

        }

        System.out.println( "POSSIVEL: " + possivel );
        if ( !possivel )
        {
            msg += " não existe(em) no stock.";
            JOptionPane.showMessageDialog( null, msg );
        }

        conexaoLocal.close();

        return possivel;
    }

//    private static boolean activo_qtd()
//    {
//
//        if ( !txtQuantidade.getText().equals( "0" ) )
//        {
//            return true;
//        }
//
//        JOptionPane.showMessageDialog( null, "Caro usuário seleccione pelo menos um lugar da " + mesa, DVML.DVML_COMERCIAL, JOptionPane.WARNING_MESSAGE );
//
//        return false;
//
//    }
}
