/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package visao;

import comercial.controller.FormaPagamentoController;
import comercial.controller.FormaPagamentoItemController;
import comercial.controller.UsuariosController;
import controller.ItemVendaController;
import controller.StockController;
import controller.TipoClienteController;
import controller.VendaController;
import dao.AccessoArmazemDao;
import dao.AnoEconomicoDao;
import dao.ArmazemDao;
import dao.BancoDao;
import dao.CambioDao;
import dao.ClienteDao;
import dao.DescontoDao;
import dao.DocumentoDao;
import dao.FormaPagamentoItemDao;
import dao.ItemVendaDao;
import dao.LugarDao;
import dao.MesasDao;
import dao.MoedaDao;
import dao.PrecoDao;
import dao.ProdutoDao;
import dao.ProdutoImpostoDao;
import dao.ProdutoIsentoDao;
import dao.StockDao;
import dao.TipoProdutoDao;
import dao.UsuarioDao;
import dao.VasilhameDao;
import dao.VendaDao;
import entity.AnoEconomico;
import entity.Cambio;
import entity.Contas;
import entity.Documento;
import entity.FormaPagamento;
import entity.FormaPagamentoItem;
import entity.TbItemVenda;
import entity.Moeda;
import entity.TbDesconto;
import entity.TbProduto;
import entity.TbStock;
import entity.TbUsuario;
import entity.TbVasilhame;
import entity.TbVenda;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import kitanda.util.CfMethods;
import kitanda.util.CfMethodsSwing;
import lista.ListaVenda1;
import modelo.ClienteModelo;
import modelo.ItemVendaModelo;
import modelo.ProdutoModelo;
import modelo.StockModelo;
import modelo.TipoClienteModelo;
import modelo.VendaModelo;
import tesouraria.novo.controller.ContaController;
import tesouraria.novo.controller.ContaMovimentosController;
import tesouraria.novo.util.MetodosUtilTS;
import util.BDConexao;
import util.DVML;
import util.DVML.Abreviacao;
import static util.DVML.DOC_FACTURA_RECIBO_FR;
import util.JPAEntityMannagerFactoryUtil;
import util.MetodosUtil;
import static util.MetodosUtil.esvaziar_tabela;
import static visao.VendaUsuarioVisao.table;
import static util.DVML.DOC_FACTURA_FT;

/**
 *
 * @author Domingos Dala Vunge
 */
public class ConverterProformaFacturaVisao extends javax.swing.JFrame implements Runnable
{

    private EntityManagerFactory emf = JPAEntityMannagerFactoryUtil.em;
    private static FormaPagamentoItemController formaPagamentoItemController;
    private static UsuariosController usuariosController;
    private static FormaPagamentoController formaPagamentoController;
    private static ContaController contaController;
    private static ContaMovimentosController cmc;
    private static AccessoArmazemDao accessoArmazemDao;
    private VasilhameDao vasilhameDao;
    private static DescontoDao descontoDao;
    private static PrecoDao precoDao;
    private BancoDao bancoDao;
    private static TbItemVenda itemVenda;
    private static TbStock stock_local;
    private static TbVenda proforma_global;
    public static ProdutoDao produtoDao;
    private static StockDao stockDao;
    private static UsuarioDao usuarioDao;
    private static DocumentoDao documentoDao;
    private static ClienteDao clienteDao;
    private static VendaDao vendaDao;
    private static MoedaDao moedaDao;
    private static AnoEconomicoDao anoEconomicoDao;
    private static ArmazemDao armazemDao;
    private static ItemVendaDao itemVendaDao;
    private TbVasilhame vasilhame;
    private Moeda moeda;
    private static AnoEconomico anoEconomico;
    private static BDConexao conexao;
    private StockModelo stockModelo;
    private TipoClienteController tipoClienteController;
    private VendaController vendaController;
    private ItemVendaController itemVendaController;
    private ItemVendaModelo itemVendaModelo;
    private VendaModelo vendaModelo;
    private ClienteModelo clienteModelo;
    private static CambioDao cambioDao;
    private static Documento documento;
    private static ProdutoImpostoDao produtoImpostoDao;
    private static Cambio cambio;
    private TipoProdutoDao tipoProdutoDao;
    private static MesasDao mesasDao;
    private static LugarDao lugarDao;
    private static int cod_usuario;
    private TipoClienteModelo tipoClienteModelo;
    private StockController stockController;
    private static int linha = 0, coordenada = 1, doc_prox_cod = 0;
    private static double soma_total = 0;
    private static double total_iva = 0;
    private ProdutoModelo produtoModelo;
    private static ProdutoIsentoDao produtoIsentoDao;
    private static int linha_actual = -1;
    public static double gorjeta = 0;

    private static Abreviacao abreviacao;

    private Thread t;
    private static String prox_doc;
    private static EntityTransaction transaction;

    public ConverterProformaFacturaVisao( int cod_usuario, BDConexao conexao ) throws SQLException
    {

        initComponents();
        confiLabel();
        setLocationRelativeTo( null );
        setResizable( false );
        this.cod_usuario = cod_usuario;
        this.conexao = conexao;
        setWindowsListener();
        init();

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
        jMenuItem1 = new javax.swing.JMenuItem();
        buttonGroup2 = new javax.swing.ButtonGroup();
        buttonGroup3 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        lbTotalPagar = new javax.swing.JLabel();
        txtTroco = new javax.swing.JTextField();
        txtTotal_AOA_liquido = new javax.swing.JTextField();
        sp_valor_entregue = new javax.swing.JSpinner();
        txtTotal_AOA_Desconto = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        lbValorPorExtenco = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        cmbCliente = new javax.swing.JComboBox();
        lbCliente = new javax.swing.JLabel();
        txtClienteNome = new javax.swing.JTextField();
        btnFormaPagamento = new javax.swing.JButton();
        btnProcessar = new javax.swing.JButton();
        txtObs = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        btnCancelar = new javax.swing.JButton();
        lb_usuario = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        table_proforma = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        lb_proximo_documento = new javax.swing.JLabel();
        lbPreco3 = new javax.swing.JLabel();
        cmbTipoDocumento = new javax.swing.JComboBox();
        lbPreco1 = new javax.swing.JLabel();
        cmbArmazem = new javax.swing.JComboBox();
        lbPreco5 = new javax.swing.JLabel();
        cmbMoeda = new javax.swing.JComboBox();
        lb_cambio = new javax.swing.JLabel();
        dc_data_documento = new com.toedter.calendar.JDateChooser();
        lb_usuario1 = new javax.swing.JLabel();
        txtRefDoc = new javax.swing.JTextField();
        lb_ano_academico1 = new javax.swing.JLabel();
        rb_factura_recibo = new javax.swing.JRadioButton();
        rb_factura = new javax.swing.JRadioButton();
        cmbAnoEconomico = new javax.swing.JComboBox<>();

        jMenuItem1.setText("jMenuItem1");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("...:::::  KITANDA - CONVERSÃO DE PRÓ-FORMA EM FACTURA ::::...");

        jPanel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jPanel1.setFont(new java.awt.Font("Showcard Gothic", 0, 24)); // NOI18N

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel8.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lbTotalPagar.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbTotalPagar.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbTotalPagar.setText("Total a Pagar :");
        jPanel8.add(lbTotalPagar, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 80, 120, 34));

        txtTroco.setEditable(false);
        txtTroco.setBackground(new java.awt.Color(4, 154, 3));
        txtTroco.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        txtTroco.setForeground(new java.awt.Color(255, 255, 255));
        txtTroco.setCaretColor(new java.awt.Color(255, 255, 255));
        jPanel8.add(txtTroco, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 150, 202, 20));

        txtTotal_AOA_liquido.setEditable(false);
        txtTotal_AOA_liquido.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        txtTotal_AOA_liquido.setForeground(new java.awt.Color(255, 0, 0));
        txtTotal_AOA_liquido.setCaretColor(new java.awt.Color(255, 255, 255));
        txtTotal_AOA_liquido.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtTotal_AOA_liquidoActionPerformed(evt);
            }
        });
        jPanel8.add(txtTotal_AOA_liquido, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 80, 250, 40));

        sp_valor_entregue.addChangeListener(new javax.swing.event.ChangeListener()
        {
            public void stateChanged(javax.swing.event.ChangeEvent evt)
            {
                sp_valor_entregueStateChanged(evt);
            }
        });
        sp_valor_entregue.addInputMethodListener(new java.awt.event.InputMethodListener()
        {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt)
            {
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt)
            {
                sp_valor_entregueInputMethodTextChanged(evt);
            }
        });
        sp_valor_entregue.addPropertyChangeListener(new java.beans.PropertyChangeListener()
        {
            public void propertyChange(java.beans.PropertyChangeEvent evt)
            {
                sp_valor_entreguePropertyChange(evt);
            }
        });
        sp_valor_entregue.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                sp_valor_entregueKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt)
            {
                sp_valor_entregueKeyTyped(evt);
            }
        });
        jPanel8.add(sp_valor_entregue, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 120, 200, 30));
        jPanel8.add(txtTotal_AOA_Desconto, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 30, 200, -1));

        jLabel1.setText("Total_desconto");
        jPanel8.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 100, -1));

        lbValorPorExtenco.setFont(new java.awt.Font("Lucida Grande", 1, 18)); // NOI18N
        lbValorPorExtenco.setForeground(new java.awt.Color(204, 0, 0));
        lbValorPorExtenco.setText("VALOR POR EXTENSO");

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        cmbCliente.setBackground(new java.awt.Color(4, 154, 3));
        cmbCliente.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        cmbCliente.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbCliente.setEnabled(false);
        cmbCliente.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbClienteActionPerformed(evt);
            }
        });
        jPanel3.add(cmbCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 90, 440, 40));

        lbCliente.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbCliente.setText("Cliente:");
        jPanel3.add(lbCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 90, 60, 40));

        txtClienteNome.setEditable(false);
        txtClienteNome.setBackground(new java.awt.Color(4, 154, 3));
        txtClienteNome.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txtClienteNome.setForeground(new java.awt.Color(255, 255, 255));
        txtClienteNome.setCaretColor(new java.awt.Color(255, 255, 255));
        txtClienteNome.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtClienteNomeActionPerformed(evt);
            }
        });
        jPanel3.add(txtClienteNome, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, 80, -1));

        btnFormaPagamento.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/1323444801_currency_dollar red.png"))); // NOI18N
        btnFormaPagamento.setText("Forma de Pagamento");
        btnFormaPagamento.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnFormaPagamentoActionPerformed(evt);
            }
        });
        jPanel3.add(btnFormaPagamento, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 30, 260, 60));

        btnProcessar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnProcessar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/impressora1.png"))); // NOI18N
        btnProcessar.setText("Converter");
        btnProcessar.setToolTipText("Efectuar Venda");
        btnProcessar.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnProcessarActionPerformed(evt);
            }
        });
        jPanel3.add(btnProcessar, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 30, 230, 60));

        txtObs.setFont(new java.awt.Font("Lucida Grande", 0, 11)); // NOI18N
        jPanel3.add(txtObs, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 10, 580, 16));

        jLabel8.setText("Obs:");
        jPanel3.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 40, -1));

        btnCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/LOGOUT - VERMELHO/Logout 32x32.png"))); // NOI18N
        btnCancelar.setAlignmentX(0.5F);
        btnCancelar.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnCancelarActionPerformed(evt);
            }
        });

        lb_usuario.setFont(new java.awt.Font("Lucida Grande", 1, 12)); // NOI18N
        lb_usuario.setText("Conta:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, 392, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lb_usuario, javax.swing.GroupLayout.PREFERRED_SIZE, 797, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbValorPorExtenco, javax.swing.GroupLayout.PREFERRED_SIZE, 921, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, 179, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(lbValorPorExtenco)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lb_usuario, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );

        jPanel2.setBackground(new java.awt.Color(204, 204, 204));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        table_proforma.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        table_proforma.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "Cod.Art", "Descrição", "Preço", "Qtd.", "Desconto(%)", "Taxa", "Valor", "Valor(IVA)"
            }
        )
        {
            Class[] types = new Class []
            {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean []
            {
                false, false, false, false, true, false, false, false
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
        table_proforma.setCellSelectionEnabled(true);
        table_proforma.setGridColor(new java.awt.Color(51, 153, 0));
        table_proforma.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                table_proformaMouseClicked(evt);
            }
        });
        table_proforma.addPropertyChangeListener(new java.beans.PropertyChangeListener()
        {
            public void propertyChange(java.beans.PropertyChangeEvent evt)
            {
                table_proformaPropertyChange(evt);
            }
        });
        jScrollPane1.setViewportView(table_proforma);
        table_proforma.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        if (table_proforma.getColumnModel().getColumnCount() > 0)
        {
            table_proforma.getColumnModel().getColumn(0).setPreferredWidth(10);
            table_proforma.getColumnModel().getColumn(1).setPreferredWidth(250);
            table_proforma.getColumnModel().getColumn(2).setPreferredWidth(20);
            table_proforma.getColumnModel().getColumn(3).setPreferredWidth(5);
            table_proforma.getColumnModel().getColumn(5).setPreferredWidth(5);
        }

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1051, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 185, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        lb_proximo_documento.setFont(new java.awt.Font("Lucida Grande", 1, 16)); // NOI18N
        lb_proximo_documento.setText("PRÓXIMO DOC. : XX PP/A1");

        lbPreco3.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbPreco3.setText("Tipo de Documento");

        cmbTipoDocumento.setBackground(new java.awt.Color(4, 154, 3));
        cmbTipoDocumento.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 14)); // NOI18N
        cmbTipoDocumento.setEnabled(false);
        cmbTipoDocumento.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbTipoDocumentoActionPerformed(evt);
            }
        });

        lbPreco1.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbPreco1.setText("Armzém");

        cmbArmazem.setBackground(new java.awt.Color(4, 154, 3));
        cmbArmazem.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 14)); // NOI18N
        cmbArmazem.setEnabled(false);
        cmbArmazem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbArmazemActionPerformed(evt);
            }
        });

        lbPreco5.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbPreco5.setText("Moeda");

        cmbMoeda.setBackground(new java.awt.Color(4, 154, 3));
        cmbMoeda.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 14)); // NOI18N
        cmbMoeda.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbMoedaActionPerformed(evt);
            }
        });

        lb_cambio.setFont(new java.awt.Font("Lucida Grande", 1, 16)); // NOI18N
        lb_cambio.setText("Cambio");

        dc_data_documento.setEnabled(false);
        dc_data_documento.setFont(new java.awt.Font("Lucida Grande", 1, 12)); // NOI18N

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(320, 320, 320)
                .addComponent(lb_proximo_documento, javax.swing.GroupLayout.PREFERRED_SIZE, 281, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(dc_data_documento, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(59, 59, 59))
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbPreco3, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbTipoDocumento, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbPreco1, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbArmazem, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbPreco5, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(cmbMoeda, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lb_cambio, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(103, 103, 103))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lb_proximo_documento, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dc_data_documento, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(lbPreco3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(cmbTipoDocumento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cmbArmazem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(lbPreco5)
                                .addGap(33, 33, 33))))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cmbMoeda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lb_cambio, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbPreco1)))
                .addContainerGap())
        );

        lb_usuario1.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lb_usuario1.setText("Ref. Doc:");

        txtRefDoc.setBackground(new java.awt.Color(4, 154, 3));
        txtRefDoc.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        txtRefDoc.setForeground(new java.awt.Color(255, 255, 255));
        txtRefDoc.setCaretColor(new java.awt.Color(255, 255, 255));
        txtRefDoc.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtRefDocActionPerformed(evt);
            }
        });

        lb_ano_academico1.setBackground(new java.awt.Color(4, 154, 3));
        lb_ano_academico1.setFont(new java.awt.Font("Lucida Grande", 1, 24)); // NOI18N
        lb_ano_academico1.setForeground(new java.awt.Color(255, 255, 255));
        lb_ano_academico1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lb_ano_academico1.setText("CONVERSÃO DE PRÓ-FORMA EM FACTURA");
        lb_ano_academico1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lb_ano_academico1.setOpaque(true);

        buttonGroup1.add(rb_factura_recibo);
        rb_factura_recibo.setSelected(true);
        rb_factura_recibo.setText("FR-Factura recibo");
        rb_factura_recibo.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                rb_factura_reciboActionPerformed(evt);
            }
        });

        buttonGroup1.add(rb_factura);
        rb_factura.setText("FA-Factura");
        rb_factura.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                rb_facturaActionPerformed(evt);
            }
        });

        cmbAnoEconomico.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbAnoEconomico.setEnabled(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lb_ano_academico1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(lb_usuario1, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtRefDoc, javax.swing.GroupLayout.PREFERRED_SIZE, 248, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(26, 26, 26)
                                .addComponent(rb_factura_recibo)
                                .addGap(31, 31, 31)
                                .addComponent(rb_factura)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(cmbAnoEconomico, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lb_ano_academico1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtRefDoc, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
                    .addComponent(lb_usuario1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(rb_factura_recibo)
                    .addComponent(rb_factura)
                    .addComponent(cmbAnoEconomico, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        getAccessibleContext().setAccessibleName("...:::::  KITANDA - FACTURAÃO ::::...");

        pack();
    }// </editor-fold>//GEN-END:initComponents


    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void cmbArmazemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbArmazemActionPerformed
        // TODO add your handling code here:


    }//GEN-LAST:event_cmbArmazemActionPerformed

    private void txtClienteNomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtClienteNomeActionPerformed
        // TODO add your handling code here:
        procedimento_converter();


    }//GEN-LAST:event_txtClienteNomeActionPerformed

    private void cmbClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbClienteActionPerformed
        // TODO add your handling code here:
        accao_cliente();
    }//GEN-LAST:event_cmbClienteActionPerformed

    private void txtTotal_AOA_liquidoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTotal_AOA_liquidoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTotal_AOA_liquidoActionPerformed

    private void btnProcessarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProcessarActionPerformed

        if ( verifica_ano_documento_igual_economico() )
        {
            if ( data_documento_superior_ou_igual_ao_ultimo_doc() )
            {
                procedimento_converter();

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


    }//GEN-LAST:event_btnProcessarActionPerformed

    private void cmbTipoDocumentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbTipoDocumentoActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_cmbTipoDocumentoActionPerformed

    private void cmbMoedaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbMoedaActionPerformed
        // TODO add your handling code here:
        actualizar_moeda();

    }//GEN-LAST:event_cmbMoedaActionPerformed

    private void txtRefDocActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtRefDocActionPerformed
        // TODO add your handling code here:
        btnFormaPagamento.setVisible( true );
        procedimento_busca();
    }//GEN-LAST:event_txtRefDocActionPerformed

    private void sp_valor_entregueStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_sp_valor_entregueStateChanged
        // TODO add your handling code here:
        tratar_troco();

    }//GEN-LAST:event_sp_valor_entregueStateChanged

    private void sp_valor_entreguePropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_sp_valor_entreguePropertyChange
        // TODO add your handling code here:

    }//GEN-LAST:event_sp_valor_entreguePropertyChange

    private void sp_valor_entregueInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_sp_valor_entregueInputMethodTextChanged
        // TODO add your handling code here:

    }//GEN-LAST:event_sp_valor_entregueInputMethodTextChanged

    private void sp_valor_entregueKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_sp_valor_entregueKeyTyped
        // TODO add your handling code here:

    }//GEN-LAST:event_sp_valor_entregueKeyTyped

    private void sp_valor_entregueKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_sp_valor_entregueKeyPressed
        // TODO add your handling code here:

    }//GEN-LAST:event_sp_valor_entregueKeyPressed

    private void rb_factura_reciboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rb_factura_reciboActionPerformed
        // TODO add your handling code here:
        desabilitar_campos();
        mostrar_proximo_codigo_documento();
        actualizar_abreviacao();
        //desabilitar_campos();
    }//GEN-LAST:event_rb_factura_reciboActionPerformed

    private void rb_facturaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rb_facturaActionPerformed
        // TODO add your handling code here:
        desabilitar_campos();
        mostrar_proximo_codigo_documento();
        actualizar_abreviacao();
    }//GEN-LAST:event_rb_facturaActionPerformed

    private void btnFormaPagamentoActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnFormaPagamentoActionPerformed
    {//GEN-HEADEREND:event_btnFormaPagamentoActionPerformed
        // TODO add your handling code here:
        //         new FormaPagamentoVisao( this, rootPaneCheckingEnabled, DVML.VENDA_PONTUAL, emf ).setVisible( true );

        if ( verifica_ano_documento_igual_economico() )
        {
            if ( data_documento_superior_ou_igual_ao_ultimo_doc() )
            {
                new FormaPagamentoVisao( this, rootPaneCheckingEnabled, null, DVML.CONVERSAO_PROFORMA_FACTURA_RECIBO, conexao ).setVisible( true );
//                new FormaPagamentoVisao( this, rootPaneCheckingEnabled, emf, DVML.CONVERSAO_PROFORMA_FACTURA_RECIBO, conexao ).setVisible( true );

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

    }//GEN-LAST:event_btnFormaPagamentoActionPerformed

    private void table_proformaMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_table_proformaMouseClicked
    {//GEN-HEADEREND:event_table_proformaMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_table_proformaMouseClicked

    private void table_proformaPropertyChange(java.beans.PropertyChangeEvent evt)//GEN-FIRST:event_table_proformaPropertyChange
    {//GEN-HEADEREND:event_table_proformaPropertyChange
        try
        {

            actualizar_valor_tabela( evt );
        }
        catch ( Exception e )
        {
        }
    }//GEN-LAST:event_table_proformaPropertyChange

    private void desabilitar_campos()
    {

        boolean proformaNaoSelecionado = !( DVML.DOC_FACTURA_PROFORMA_PP == getIdDocumento() );

        boolean valor = !( DVML.DOC_FACTURA_PROFORMA_PP == getIdDocumento() );

//        lbValorEnregue.setVisible( valor );
//        sp_valor_entregue.setVisible( valor );
////        lbTroco.setVisible( valor );
//        txtTroco.setVisible( valor );
////        lbFormaPagamento.setVisible( valor );
//        cmbFormaPagamento.setVisible( valor );
//        txtTroco.setText( CfMethods.formatarComoMoeda( 0.0 ) );
        boolean documentoIsFA = DVML.DOC_FACTURA_FT == getIdDocumento();
        boolean documentoIsPP = DVML.DOC_FACTURA_PROFORMA_PP == getIdDocumento();
        System.err.println( "documentoIsFA: " + documentoIsFA );
        System.err.println( "documentoIsPP: " + documentoIsPP );
//        lbValorEnregue.setVisible( !documentoIsFA && !documentoIsPP );
//        lbFormaPagamento.setVisible( !documentoIsFA && !documentoIsPP );
//        lbPreco7.setVisible( !documentoIsFA && !documentoIsPP );
//        txtValorEntregue.setVisible( lbValorEnregue.isVisible() );
//        lbTroco.setVisible( lbValorEnregue.isVisible() );
//        txtTroco.setVisible( lbValorEnregue.isVisible() );
//        cmbFormaPagamento.setVisible( lbValorEnregue.isVisible() );
//        cmbMoeda.setVisible( lbValorEnregue.isVisible() );
//        ck_A7.setVisible( !documentoIsFA && !documentoIsPP );
//        ck_A4.setSelected( !documentoIsFA && !documentoIsPP );
        btnProcessar.setVisible( documentoIsPP || documentoIsFA );
        btnFormaPagamento.setVisible( !documentoIsFA && !documentoIsPP );
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelar;
    private static javax.swing.JButton btnFormaPagamento;
    private static javax.swing.JButton btnProcessar;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.ButtonGroup buttonGroup3;
    private static javax.swing.JComboBox<String> cmbAnoEconomico;
    public static javax.swing.JComboBox cmbArmazem;
    public static javax.swing.JComboBox cmbCliente;
    public static javax.swing.JComboBox cmbMoeda;
    public static javax.swing.JComboBox cmbTipoDocumento;
    private static com.toedter.calendar.JDateChooser dc_data_documento;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lbCliente;
    private javax.swing.JLabel lbPreco1;
    private javax.swing.JLabel lbPreco3;
    private javax.swing.JLabel lbPreco5;
    private javax.swing.JLabel lbTotalPagar;
    public static javax.swing.JLabel lbValorPorExtenco;
    private javax.swing.JLabel lb_ano_academico1;
    private javax.swing.JLabel lb_cambio;
    private static javax.swing.JLabel lb_proximo_documento;
    private javax.swing.JLabel lb_usuario;
    private javax.swing.JLabel lb_usuario1;
    private static javax.swing.JRadioButton rb_factura;
    private static javax.swing.JRadioButton rb_factura_recibo;
    private static javax.swing.JSpinner sp_valor_entregue;
    public static javax.swing.JTable table_proforma;
    private static javax.swing.JTextField txtClienteNome;
    private static javax.swing.JTextField txtObs;
    public static javax.swing.JTextField txtRefDoc;
    private static javax.swing.JTextField txtTotal_AOA_Desconto;
    public static javax.swing.JTextField txtTotal_AOA_liquido;
    public static javax.swing.JTextField txtTroco;
    // End of variables declaration//GEN-END:variables

    //verifica se o produto existe na tabela do formulário visão isto é na jTable
    private static boolean exist_produto_tabela_formulario( int codigo )
    {

        DefaultTableModel modelo = ( DefaultTableModel ) table_proforma.getModel();

        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {
            if ( Integer.parseInt( String.valueOf( table_proforma.getValueAt( i, 0 ) ) ) == codigo )
            {
                linha_actual = i;
                return true;
            }
        }
        return false;

    }

    //actualiza a quantidade na tabela do formulário visão isto é na jTable
    private static void actuazlizar_quantidade_tabela_formulario( String quantidade, double desconto )
    {
        DefaultTableModel modelo = ( DefaultTableModel ) table_proforma.getModel();
        double preco_venda = Double.parseDouble( String.valueOf( modelo.getValueAt( linha_actual, 2 ) ) );
        double total_item = preco_venda * Double.parseDouble( quantidade );
        total_item = total_item - desconto;
        modelo.setValueAt( quantidade, linha_actual, 3 );
        modelo.setValueAt( total_item, linha_actual, 5 );
        //a linha_actual recebe o default
        linha_actual = -1;

    }

    private void accao_cliente()
    {
        if ( cmbCliente.getSelectedItem().equals( "DIVERSOS" ) )
        {
            txtClienteNome.setVisible( true );
            txtClienteNome.requestFocus();
        }
        else
        {
            txtClienteNome.setVisible( false );
        }
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

    @Override
    public void run()
    {

    }

    public static void actualizar_quantidade( double quantidade, TbStock stock )
    {

        double qtd = ( stock.getQuantidadeExistente() - quantidade );
        stock.setQuantidadeExistente( qtd );
        try
        {
            stockDao.edit( stock );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            System.err.println( "Falha ao actualizar a quantidade do produto no stock" );
        }

    }

//    private Integer getIdBanco()
//    {
//        return bancoDao.getIdByDescricao( String.valueOf( cmbFormaPagamento.getSelectedItem() ) );
//    }
    private void mostrar_nome()
    {
        TbUsuario usuario = usuarioDao.findTbUsuario( this.cod_usuario );
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

    public static void limpar()
    {

        txtClienteNome.setText( "DIVERSOS" );
        txtRefDoc.setText( "" );
        txtObs.setText( "" );
        btnFormaPagamento.setVisible( false );
        btnProcessar.setVisible( false );
        sp_valor_entregue.setValue( 0.0 );
        txtTotal_AOA_liquido.setText( "0" );
        txtTroco.setText( "0" );
        soma_total = 0;
        gorjeta = 0;

    }

    public static boolean campos_invalido_imprimir()
    {

        if ( getValor_entregue() < CfMethods.parseMoedaFormatada( txtTotal_AOA_liquido.getText() ) && ( getIdDocumento() != DVML.DOC_FACTURA_PROFORMA_PP ) )
        {
            JOptionPane.showMessageDialog( null, "O valor entregue tem quer ser maior ou igual ao Total a Pagar", "AVISO", JOptionPane.WARNING_MESSAGE );
            sp_valor_entregue.requestFocus();
            return true;
        }

        if ( txtClienteNome.getText().equals( "" ) || txtClienteNome.getText() == null )
        {
            txtClienteNome.requestFocus();
            JOptionPane.showMessageDialog( null, "Por favor digite o nome do cliente", "AVISO", JOptionPane.WARNING_MESSAGE );
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

    public static double getPreco( int cod_produto, double qtd )
    {

        try
        {
            // return  precoDao.findTbPreco( precoDao.getUltimoIdPrecoByIdProduto(  getCodigoProduto() )  ).getPrecoVenda();

            return precoDao.findTbPreco( precoDao.getUltimoIdPrecoByIdProduto( cod_produto, qtd ) ).getPrecoVenda().doubleValue();
        }
        catch ( Exception e )
        {
            return 0;
        }

    }

    private static boolean transtorno()
    {

        int cod_produto = 0;
        double qtd = 0d, qtd_aceite = 0d;

        DefaultTableModel modelo = ( DefaultTableModel ) table_proforma.getModel();
        boolean transtorno = false;

        for ( int i = 0; i < table_proforma.getRowCount(); i++ )
        {

            cod_produto = Integer.parseInt( String.valueOf( table_proforma.getModel().getValueAt( i, 0 ) ) );
            qtd = Double.parseDouble( String.valueOf( table_proforma.getModel().getValueAt( i, 3 ) ) );

            if ( isStocavel( produtoDao.findTbProduto( cod_produto ).getStocavel() ) )
            {

                if ( !possivel_quantidade( cod_produto, qtd ) )
                {

                    transtorno = true;
                    //qtd_aceite = conexao.getQuantidade_Existente_Publico(cod_produto, getCodigoArmazem());
                    qtd_aceite = stockDao.getStockByDescricao( cod_produto, getCodigoArmazem() ).getQuantidadeExistente();

                    if ( qtd_aceite != 0 )
                    {

                        int opcao = JOptionPane.showConfirmDialog( null, "Desculpe pelo transtorno, o produto " + produtoDao.findTbProduto( cod_produto ).getDesignacao() + " só é possivel  a " + qtd_aceite + " quantidade(s)" + ", contrariamente de " + qtd + "\n Deseja actualizar ou remover da tabela ?" );

                        if ( opcao == JOptionPane.YES_OPTION )
                        {

                            modelo.setValueAt( qtd_aceite, i, 3 );
                            double valor_iva = 0, taxa = 0, desconto = 0;
                            taxa = Double.parseDouble( modelo.getValueAt( i, 5 ).toString() );
                            desconto = Double.parseDouble( modelo.getValueAt( i, 4 ).toString() );
                            modelo.setValueAt( ( qtd_aceite * ( getPreco( cod_produto, qtd_aceite ) + getValorIVA( taxa, getPreco( cod_produto, qtd_aceite ) ) ) - desconto ), i, 7 );

                        }
                        else
                        {
                            modelo.removeRow( i );
                        }
                        setTotalPagar();
                        valor_por_extenco();
                        sp_valor_entregue.setValue( 0.0 );
                        txtTroco.setText( "0" );

                    }
                    else
                    {
                        modelo.removeRow( i );

                        JOptionPane.showMessageDialog( null, "Desculpe pelo transtorno o produto " + produtoDao.findTbProduto( cod_produto ).getDesignacao() + " já não se encontra disponível no stock", DVML.DVML_COMERCIAL, JOptionPane.WARNING_MESSAGE );
                    }

                }

            }
            else
            {
//                        adicionar_produto();
            }

        }

        return transtorno;

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

    public static void procedimento_converter()
    {

        TbVenda documento_local = getDocumentoRef();
        Date data_actual = new Date();
        Date documento_ref_data = documento_local.getDataVenda();
        if ( documento_ref_data != null )
        {
            if ( MetodosUtil.menor_data_1_data_2( data_actual, documento_ref_data ) )
            {
                JOptionPane.showMessageDialog( null, "Caro usuário verifique a data do sistema", "AVISO", JOptionPane.WARNING_MESSAGE );
            }
            else
            {
                if ( !campos_invalido_imprimir() )
                {
                    if ( !txtClienteNome.getText().equals( "" ) )
                    {
                        if ( !transtorno() )
                        {
                            System.out.println( "STATUS: a processar a factura" );
                            EntityManager em = JPAEntityMannagerFactoryUtil.createEntityManager();
                            transaction = em.getTransaction();
                            em.getTransaction().begin();
                            salvar_venda();
//                            if ( getIdDocumento() == DVML.DOC_FACTURA_RECIBO_FR )
//                            {
//
//                                MetodosUtil.adicionar_saldo_banco( venda.getTotalVenda(), venda.getIdBanco().getIdBanco(), conexao );
//
//                            }
                            System.out.println( "STATUS: fim do processamento da factura" );
                        }
                    }
                    else
                    {
                        txtClienteNome.requestFocus();
                        JOptionPane.showMessageDialog( null, "Pf. Digite o Nome do Cliente!...", "AVISO", JOptionPane.WARNING_MESSAGE );
                    }
                }
            }
        }

    }

    /* CRIACAO DO GETS  */
    public static int getIdCliente()
    {
        try
        {
            String ref_doc = txtRefDoc.getText();
            TbVenda venda = vendaDao.findByCodFact( ref_doc, DVML.DOC_FACTURA_PROFORMA_PP );
            return venda.getCodigoCliente().getCodigo();
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
            if ( rb_factura_recibo.isSelected() )
            {
                return DOC_FACTURA_RECIBO_FR;
            }
            return DOC_FACTURA_FT;

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
            return moedaDao.getIdByDescricao( cmbMoeda.getSelectedItem().toString() );
        }
        catch ( Exception e )
        {
            return 0;
        }

    }

    public void calcularTroco()
    {

        String prefixo = "";
        double troco = 0;

        System.out.println( "VALOR ENTREGUE " + sp_valor_entregue.getValue() );
        System.out.println( "TOTAL A PAGAR " + txtTotal_AOA_liquido.getText().trim() );

        double valor_entregue = ( double ) sp_valor_entregue.getValue();
        double total_pagar = Double.parseDouble( txtTotal_AOA_liquido.getText().trim() );
        troco = valor_entregue - total_pagar;

        System.out.println( "TROCO " + troco );
        txtTroco.setText( String.valueOf( MetodosUtil.retirar_dizimas( troco ) ).trim() );

    }

    private void tratar_troco()
    {
        try
        {
            double troco = 0.0;
            double total_pagar = CfMethods.parseMoedaFormatada( txtTotal_AOA_liquido.getText() );
            double valor_entregue = Double.parseDouble( sp_valor_entregue.getValue().toString() );
            troco = valor_entregue - total_pagar;
            txtTroco.setText( CfMethods.formatarComoMoeda( troco ) );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }

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
            proforma_global = vendaDao.findByCodFact( ref_doc, DVML.DOC_FACTURA_PROFORMA_PP );
            if ( proforma_global != null )
            {

                /*@3. percorrer e preencher os campos do formulário;*/
                //3.1 preeenche o tipo documento
                cmbTipoDocumento.setSelectedItem( proforma_global.getFkDocumento().getDesignacao() );

                //3.2 preenche o armazém
                cmbArmazem.setSelectedItem( proforma_global.getIdArmazemFK().getDesignacao() );

                //3.3 preenche a moeda
                cmbMoeda.setSelectedItem( proforma_global.getFkCambio().getFkMoeda().getDesignacao() );

                //@5.setar o cliente
                txtClienteNome.setText( proforma_global.getNomeCliente() );
                cmbCliente.setSelectedItem( proforma_global.getCodigoCliente().getNome() );

                //3.4 preencher a tabela com os itens
                List<TbItemVenda> linhas = proforma_global.getTbItemVendaList();
                DefaultTableModel modelo = ( DefaultTableModel ) table_proforma.getModel();
                //3.4.1 limpa a tabela
                modelo.setRowCount( 0 );
                for ( TbItemVenda object : linhas )
                {
                    modelo.addRow( new Object[]
                    {
                        object.getCodigoProduto().getCodigo(),
                        object.getCodigoProduto().getDesignacao(),
                        CfMethods.formatarComoMoeda( object.getFkPreco().getPrecoVenda() ),
                        object.getQuantidade(),
                        //desconto é dados em percentagem
                        object.getDesconto(),
                        object.getValorIva(),
                        CfMethods.formatarComoMoeda(
                        object.getFkPreco().getPrecoVenda().doubleValue()
                        * object.getQuantidade()
                        - object.getDesconto() ),
                        CfMethods.formatarComoMoeda( object.getTotal() )

                    } );
                }

                //@4.setar o total
                txtTotal_AOA_liquido.setText( CfMethods.formatarComoMoeda( proforma_global.getTotalVenda() ) );
                sp_valor_entregue.setValue( proforma_global.getTotalVenda() );
                //setTotalPagar();
                lbValorPorExtenco.setText( "São: " + MetodosUtil.valorPorExtenso( CfMethods.parseMoedaFormatada( txtTotal_AOA_liquido.getText() ), getMoeda().getDesignacao() ) );

            }
            else
            {
                procedimento_limpar_dados();
                proforma_global = null;
                JOptionPane.showMessageDialog( null, "Não existe pró-forma com esta referência", "AVISO", JOptionPane.WARNING_MESSAGE );
            }

        }
        else
        {
            proforma_global = null;
            JOptionPane.showMessageDialog( null, "por favor insira a refrência da pró-forma", "AVISO", JOptionPane.WARNING_MESSAGE );
        }

    }

    private void reset_combo()
    {
        cmbTipoDocumento.setSelectedIndex( 0 );
        cmbMoeda.setSelectedIndex( 0 );
        cmbArmazem.setSelectedIndex( 0 );

    }

    private void procedimento_limpar_dados()
    {
        limpar();
        esvaziar_tabela( table_proforma );
        reset_combo();
    }

    private static void valor_por_extenco()
    {
        System.out.println( "Valor XXXXXXX: " + CfMethods.parseMoedaFormatada( txtTotal_AOA_liquido.getText() ) );
        lbValorPorExtenco.setText( "São: " + MetodosUtil.valorPorExtenso( CfMethods.parseMoedaFormatada( txtTotal_AOA_liquido.getText() ), getMoeda().getDesignacao() ) );
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
                prefixo = CfMethods.formatarComoMoeda( ( double ) sp_valor_entregue.getValue() ) + key;
                double troco = 0;
                double valor_entregue = Double.parseDouble( prefixo );

                troco = valor_entregue - total_pagar;
                txtTroco.setText( String.valueOf( MetodosUtil.retirar_dizimas( troco ) ) );

            }
            else if ( evt.getKeyCode() == KeyEvent.VK_BACK_SPACE )
            {
                char key = evt.getKeyChar();

                prefixo = CfMethods.formatarComoMoeda( ( double ) sp_valor_entregue.getValue() ) + key;

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
                txtTroco.setText( String.valueOf( MetodosUtil.retirar_dizimas( troco ) ) );

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
                    txtClienteNome.requestFocus();
                }
                catch ( Exception ex )
                {
                    Logger.getLogger( ConverterProformaFacturaVisao.class.getName() ).log( Level.SEVERE, null, ex );
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

                    procedimento_converter();

                }
                catch ( Exception ex )
                {
                    Logger.getLogger( ConverterProformaFacturaVisao.class.getName() ).log( Level.SEVERE, null, ex );
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

        return Double.parseDouble( sp_valor_entregue.getValue().toString() );

    }

    public static double getTroco()
    {

        try
        {
            return CfMethods.parseMoedaFormatada( txtTroco.getText() );
        }
        catch ( Exception e )
        {
            return 0.0;
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

    public static void salvarItemvenda()
    {

        int cod_venda = vendaDao.getLastVenda();
        boolean efectuada = true;
        proforma_global = vendaDao.findTbVenda( cod_venda );

        for ( int i = 0; i < table_proforma.getRowCount(); i++ )
        {
            try
            {
                TbProduto produto_local = produtoDao.findTbProduto( Integer.parseInt( String.valueOf( table_proforma.getModel().getValueAt( i, 0 ) ) ) );

                itemVenda = new TbItemVenda();
                itemVenda.setCodigoProduto( produto_local );
//                itemVenda.setCodigoProduto( produtoDao.findTbProduto( Integer.parseInt( String.valueOf( table_proforma.getModel().getValueAt( i, 0 ) ) ) ) );
                itemVenda.setCodigoVenda( proforma_global );
                itemVenda.setQuantidade( Double.parseDouble( String.valueOf( table_proforma.getModel().getValueAt( i, 3 ) ) ) );
                itemVenda.setDesconto( Double.parseDouble( String.valueOf( table_proforma.getModel().getValueAt( i, 4 ) ) ) );
                itemVenda.setValorIva( Double.parseDouble( String.valueOf( table_proforma.getModel().getValueAt( i, 5 ) ) ) );
                itemVenda.setMotivoIsensao( getMotivoIsensao( itemVenda.getCodigoProduto().getCodigo() ) );
                itemVenda.setCodigoIsensao( MetodosUtil.getCodigoRegime( itemVenda.getCodigoProduto().getCodigo() ) );
                itemVenda.setTotal( new BigDecimal( CfMethods.parseMoedaFormatada( String.valueOf( table_proforma.getModel().getValueAt( i, 7 ) ) ) ) );
                itemVenda.setFkPreco( precoDao.findTbPreco( precoDao.getUltimoIdPrecoByIdProduto( itemVenda.getCodigoProduto().getCodigo(), itemVenda.getQuantidade() ) ) );
                itemVenda.setDataServico( new Date() );

                itemVenda.setFkLugares( lugarDao.findTbLugares( DVML.LUGAR_BALCAO ) );
                itemVenda.setFkMesas( mesasDao.findTbMesas( DVML.MESA_BALCAO ) );

                //cria o item venda
//                itemVendaDao.create( itemVenda );
//                this.stock_local = stockDao.get_stock_by_id_produto_and_id_armazem( itemVenda.getCodigoProduto().getCodigo(), getCodigoArmazem() );
//
//                if ( getIdDocumento() == DOC_FACTURA_RECIBO_FR || getIdDocumento() == DOC_FACTURA_FT )
//                {
//                    //so retirar caso existir mesmo no armazém em questão.
//                    if ( stock_local.getCodigo() != 0 )
//                    {
//                        actualizar_quantidade( itemVenda.getQuantidade(), stock_local );
//                    }
//
//                }
                //itemVendaDao.create( itemVenda );
                itemVendaDao.criarComProcedimentos( itemVenda, conexao );

                boolean isStocavel = produto_local.getStocavel().equals( "true" );

                if ( isStocavel )
                {
                    stock_local = stockDao.get_stock_by_id_produto_and_id_armazem( itemVenda.getCodigoProduto().getCodigo(), getCodigoArmazem() );
                }

                if ( getIdDocumento() == DOC_FACTURA_RECIBO_FR || getIdDocumento() == DOC_FACTURA_FT )
                {
                    System.out.println( "passei quando é FR ou FT" );
                    //so retirar caso existir mesmo no armazém em questão.
                    if ( !Objects.isNull( stock_local ) && isStocavel )
                    {
                        System.out.println( "chamei o actualizar quantidade" );
                        actualizar_quantidade( itemVenda.getQuantidade(), stock_local );
//                        actualizar_quantidade( itemVenda.getCodigoProduto().getCodigo(), itemVenda.getQuantidade() );
                    }
//                    if ( stock_local.getCodigo() != 0 )
//                    {
//                       // actualizar_quantidade( itemVenda.getQuantidade(), stock_local );
//                        actualizar_quantidade(i, WIDTH);
//                    }
                }

            }
            catch ( Exception e )
            {
                e.printStackTrace();
                efectuada = false;
                transaction.rollback();
                JOptionPane.showMessageDialog( null, "Falha ao registrar o produto: " + itemVenda.getCodigoProduto().getCodigo() + " na Factura" );
                break;
            }
        }

        if ( efectuada )
        {

            if ( rb_factura_recibo.isSelected() )
            {
                registrar_forma_pagamento( cod_venda );
            }

            JOptionPane.showMessageDialog( null, "Factura efectuada com sucesso!.." );
            List<TbProduto> lista_produto_isentos = new ArrayList<>();
            lista_produto_isentos = getProdutosIsentos();
            String motivos_isentos = MetodosUtil.getMotivoIsensaoProdutos( lista_produto_isentos );
            System.err.println( "MOTIVOS: " + motivos_isentos );
            try
            {

                limpar();
                remover_all_produto();
                //adicionar_preco_quantidade_anitgo();

            }
            catch ( Exception e )
            {
            }
            actualizar_cod_doc();
            txtClienteNome.setText( "" );
            // txtClienteNome.requestFocus();
            actualizar_abreviacao();
            //Chama a factura e imprime directamente para a imprissora que estiver devenidade no sistema operativo
            ListaVenda1 listaVenda1 = new ListaVenda1( cod_venda, abreviacao, false, false, "Original", motivos_isentos );
            //ListaVenda1 listaVenda2 = new ListaVenda1(cod_venda, false, ck_simplificada.isSelected());
            //Em caso em que a impreensão é dupla
            //ListaVendaDuplicado listaVenda1 = new ListaVendaDuplicado(cod_venda, setPeformance(), ckImpreesao.isSelected());
        }

    }

    public static void setTotalPagar()
    {
        //DefaultTableModel modelo = ( DefaultTableModel ) table.getModel();
        double total_liquido = getTotalAOALiquido();
        txtTotal_AOA_liquido.setText( CfMethods.formatarComoMoeda( total_liquido ) );
        reset_valor_entregue();
        sp_valor_entregue.setValue( total_liquido );
        txtTroco.setText( CfMethods.formatarComoMoeda( 0 ) );

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
                                cod_usuario,
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
//            String valor = ( modelo.getValueAt( i, 3 ) != null ) ? modelo.getValueAt( i, 3 ).toString() : "0";
//
//            formaPagamentoItem.setValor( new BigDecimal( valor ) );
//            formaPagamentoItem.setReferencia( referencia );
//            formaPagamentoItem.setFkVenda( new TbVenda( id_venda ) );
//            formaPagamentoItem.setFkFormaPagamento( new FormaPagamento( id_forma_pagamento ) );
//
//            try
//            {
//
//                if ( !valor.equals( "0" ) )
//                {
//                    FormaPagamentoItemDao.insert( formaPagamentoItem, conexao );
//                }
//
//            }
//            catch ( Exception e )
//            {
//                return false;
//            }
//        }
//
//        return true;
//    }
    private static void calculaTotalIVA()
    {
        DefaultTableModel modelo = ( DefaultTableModel ) table_proforma.getModel();
        double iva = 0, preco = 0;
        int qtd = 0;

        total_iva = 0;
        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {

            preco = CfMethods.parseMoedaFormatada( modelo.getValueAt( i, 2 ).toString() );
            qtd = Integer.parseInt( modelo.getValueAt( i, 3 ).toString() );
            iva = Double.parseDouble( modelo.getValueAt( i, 5 ).toString() );
            iva = ( iva / 100 );

            total_iva += ( iva * preco ) * qtd;

        }
        System.out.println( "TOTAL IVA: " + total_iva );

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
            valor = resultado.getDouble( "valor" );
        }
        return valor;

    }

    public static void remover_all_produto() throws SQLException
    {

        DefaultTableModel modelo = ( DefaultTableModel ) table_proforma.getModel();
        for ( int i = modelo.getRowCount() - 1; i >= 0; i-- )
        {
            modelo.removeRow( i );
        }

    }

    public void remover_produto() throws SQLException
    {

        if ( linha > 0 )
        {

            table_proforma.getModel().setValueAt( 0, linha - 1, 0 );
            table_proforma.getModel().setValueAt( "", linha - 1, 1 );
            table_proforma.getModel().setValueAt( 0, linha - 1, 2 );
            table_proforma.getModel().setValueAt( 0, linha - 1, 3 );
            table_proforma.getModel().setValueAt( 0, linha - 1, 4 );
            table_proforma.getModel().setValueAt( 0, linha - 1, 5 );

            setTotalPagar();
            linha--;
            coordenada--;

            calcularTroco();
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

    public static int getCodigoArmazem()
    {
        //return conexao.getCodigoPublico("tb_armazem", String.valueOf(  cmbArmazem.getSelectedItem() ) );   
        return armazemDao.getArmazemByDescricao( cmbArmazem.getSelectedItem().toString() ).getCodigo();
    }

    public static void salvar_venda()
    {

//        TbVenda venda = new TbVenda();
//        venda.setCodigoUsuario(usuarioDao.findTbUsuario(cod_usuario));
//        venda.setCodigoCliente(clienteDao.findTbCliente(getIdCliente()));
//        venda.setTotalVenda(CfMethods.parseMoedaFormatada(txtTotalApagar.getText()).floatValue());
//        venda.setPerformance("false");
//        venda.setCredito("false");
//        venda.setValorEntregue(getValor_entregue());
//        venda.setTroco(getTroco());
//        venda.setIdArmazemFK(armazemDao.findTbArmazem(getCodigoArmazem()));
//        venda.setDataVenda(new Date());
//        venda.setHora(new Date());
//        venda.setNomeCliente(getNomeCliente());
//        venda.setDescontoTotal(0.0);
//        venda.setTotalGeral(CfMethods.parseMoedaFormatada(txtTotalApagar.getText()));
//        venda.setIdBanco(bancoDao.findTbBanco(getIdBanco()));
//        venda.setStatusEliminado("false");
//        venda.setFkAnoEconomico(this.anoEconomico);
//        venda.setFkCambio(this.cambio);
//        venda.setFkDocumento(this.documento);
//
//        venda.setCodFact(this.prox_doc);
//        //referência a proforma em questão
//        venda.setRefCodFact(txtRefDoc.getText());
//        calculaTotalIVA();
//        venda.setTotalIva(total_iva);
//        venda.setHashCod(MetodosUtil.criptografia_hash(venda.getCodFact()));
//        venda.setTotalPorExtenso(lbValorPorExtenco.getText());
//        System.out.println("STATUS:hash cod processado.");
//        venda.setAssinatura(MetodosUtil.assinatura_doc(venda.getHashCod()));
//        System.out.println("STATUS:documento assinado com sucesso.");
//
//        try {
//            vendaDao.create(venda);
//            System.out.println("STATUS:factura criada com sucesso.");
//            salvarItemvenda();
//            transaction.commit();
//            System.out.println("STATUS:itens adicionado na facrtura com sucesso.");
//        } catch (Exception e) {
//
//            System.err.println("STATUS: falha ao actualizar a factura");
//            e.printStackTrace();
//            transaction.rollback();
//            JOptionPane.showMessageDialog(null, "Falha ao Processar a Factura", "FALHA", JOptionPane.ERROR_MESSAGE);
//        }
        Date data_documento = new Date();
        TbVenda venda_local = new TbVenda();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime( data_documento );
        //adicionar 15 dias na data do documento.
        calendar.add( Calendar.DATE, 15 );

        venda_local.setDataVenda( data_documento );
        venda_local.setHora( data_documento );
        venda_local.setDataVencimento( calendar.getTime() );
        venda_local.setNomeCliente( getNomeCliente() );
        venda_local.setClienteNif( getClienteNif() );
        venda_local.setObs( txtObs.getText() );
        venda_local.setTotalRetencao( new BigDecimal( getTotalRetencao() ) );
        //Total Ilíquido
//        venda_local.setTotalGeral( getTotalIliquido() );
        venda_local.setTotalGeral( proforma_global.getTotalGeral() );
        //desconto por linha
//        venda_local.setDescontoComercial( getDescontoComercial() );
        venda_local.setDescontoComercial( proforma_global.getDescontoComercial() );
        //imposto
        //calculaTotalIVA();
        venda_local.setTotalIva( new BigDecimal( getTotalImposto() ) );
        //desconto global
//        venda_local.setDescontoFinanceiro( getDescontoFinanceiro() );
        venda_local.setDescontoFinanceiro( proforma_global.getDescontoFinanceiro() );
        //Total(AOA) <=> Total Líquido
//        venda_local.setTotalVenda( new BigDecimal( getTotalAOALiquido() ) );
        venda_local.setTotalVenda( proforma_global.getTotalVenda() );
//        venda_local.setValorEntregue( getValor_entregue() );
        venda_local.setValorEntregue( proforma_global.getValorEntregue() );
        venda_local.setTroco( proforma_global.getTroco() );
        venda_local.setTotalIncidencia( proforma_global.getTotalIncidencia() );
        venda_local.setTotalIncidenciaIsento( proforma_global.getTotalIncidenciaIsento() );

        /*outros campos*/
//        venda_local.setDescontoTotal( getDescontoComercial() + getDescontoFinanceiro() );
        venda_local.setDescontoTotal( proforma_global.getDescontoTotal() );
//        venda_local.setIdBanco( bancoDao.findTbBanco( getIdBanco() ) );
        venda_local.setIdArmazemFK( armazemDao.findTbArmazem( getCodigoArmazem() ) );
        venda_local.setCodigoUsuario( usuarioDao.findTbUsuario( cod_usuario ) );
        venda_local.setCodigoCliente( proforma_global.getCodigoCliente() );
        venda_local.setFkAnoEconomico( anoEconomico );

        venda_local.setFkDocumento( documento );

        venda_local.setCodFact( getCodDocActualizador() );
//        venda_local.setCodFact( this.prox_doc );

//        venda_local.setHashCod( MetodosUtil.criptografia_hash( prox_doc ) );
        venda_local.setHashCod( MetodosUtil.criptografia_hash( venda_local, getGrossTotal(), conexao ) );

        venda_local.setTotalPorExtenso( lbValorPorExtenco.getText() );
        //System.out.println( "STATUS:hash cod processado." );
        venda_local.setAssinatura( MetodosUtil.assinatura_doc( venda_local.getHashCod() ) );
        venda_local.setRefDataFact( venda_local.getDataVenda() );
        venda_local.setRefCodFact( txtRefDoc.getText() );
        //System.out.println( "STATUS:documento assinado com sucesso." );

        if ( cambio.getFkMoeda().getAbreviacao().equals( DVML.MOEDA_KWANZA ) )
        {
            venda_local.setFkCambio( cambio );
        }
        else
        {
            Cambio cambio_nacional = cambioDao.getLastObject( getIdMoeda() );
            venda_local.setFkCambio( cambio_nacional );
        }

        /*status documento*/
        venda_local.setStatusEliminado( "false" );
        venda_local.setPerformance( "false" );
        venda_local.setCredito( "false" );
        venda_local.setGorjeta( new BigDecimal( gorjeta ) );

        try
        {
            Integer last_venda = VendaDao.criarVendaComProcedu( venda_local, conexao );
//            vendaDao.create( venda_local );
            if ( last_venda != null )
            {
                if ( getIdDocumento() == DOC_FACTURA_RECIBO_FR )
                {
//                    MetodosUtil.adicionar_saldo_banco( venda_local.getTotalVenda(), venda_local.getIdBanco().getIdBanco(), conexao );
                }
//                if ( !vendaDao.existeItensVenda( last_venda, conexao ) )
//                {
                salvarItemvenda();
//                }
//                else
//                {
//                    System.out.println( "ERROR: Já existe venda relacionada." );
//                }

            }
        }
        catch ( Exception e )
        {
            System.err.println( "STATUS: falha ao actualizar a factura" );
            e.printStackTrace();
            JOptionPane.showMessageDialog( null, "Falha ao Processar a Factura", "FALHA", JOptionPane.ERROR_MESSAGE );
        }

    }

    private static double getTotalRetencao()
    {
//        DefaultTableModel modelo = ( DefaultTableModel ) jTable1.getModel();
        double imposto = 0d;
        return imposto;
    }

    private static String getCodDocActualizador()
    {
        try
        {
            documento = documentoDao.findDocumento( getIdDocumento() );
            anoEconomico = anoEconomicoDao.findAnoEconomico( getIdAnoEconomico() );
            // this.doc_prox_cod = documento.getCodUltimoDoc() + 1;
            doc_prox_cod = vendaDao.getUltimaContagemByIdDocumentoAndAnoEconomico( getIdDocumento(), getIdAnoEconomico(), conexao ) + 1;
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

    private static String getNomeCliente()
    {

        return cmbCliente.getSelectedItem().toString();

    }

    private static String getClienteNif()
    {
        try
        {
            return clienteDao.findTbCliente( getIdCliente() ).getNif();
        }
        catch ( Exception e )
        {
            return "";
        }
    }

    public static void main( String[] args ) throws SQLException
    {
        new ConverterProformaFacturaVisao( 15, new BDConexao() ).show( true );
    }

    public void confiLabel()
    {

        lbTotalPagar.setHorizontalAlignment( JLabel.RIGHT );
//        lbValorEnregue.setHorizontalAlignment( JLabel.RIGHT );
//        lbTroco.setHorizontalAlignment( JLabel.RIGHT );
        //lbCliente.setHorizontalAlignment(JLabel.RIGHT);

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
            JOptionPane.showMessageDialog( null, "O produto provavelmente nap estocavel!..." );
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

        formaPagamentoController = new FormaPagamentoController( ConverterProformaFacturaVisao.conexao );
        formaPagamentoItemController = new FormaPagamentoItemController( ConverterProformaFacturaVisao.conexao );
        contaController = new ContaController( ConverterProformaFacturaVisao.conexao );
        cmc = new ContaMovimentosController( conexao );
        usuariosController = new UsuariosController( ConverterProformaFacturaVisao.conexao );
        vasilhameDao = new VasilhameDao( emf );
        descontoDao = new DescontoDao( emf );
        precoDao = new PrecoDao( emf );
        produtoDao = new ProdutoDao( emf );
        stockDao = new StockDao( emf );
        usuarioDao = new UsuarioDao( emf );
        clienteDao = new ClienteDao( emf );
        vendaDao = new VendaDao( emf );
        armazemDao = new ArmazemDao( emf );
        lb_cambio.setVisible( false );
        accessoArmazemDao = new AccessoArmazemDao( emf );
        itemVendaDao = new ItemVendaDao( emf );
        tipoProdutoDao = new TipoProdutoDao( emf );
        bancoDao = new BancoDao( emf );
        anoEconomicoDao = new AnoEconomicoDao( emf );
        documentoDao = new DocumentoDao( emf );
        moedaDao = new MoedaDao( emf );
        cambioDao = new CambioDao( emf );
        mesasDao = new MesasDao( emf );
        lugarDao = new LugarDao( emf );
        produtoImpostoDao = new ProdutoImpostoDao( emf );
        produtoIsentoDao = new ProdutoIsentoDao( emf );
        //txtDesconto.setText("0");
        sp_valor_entregue.setVisible( false );
        txtTroco.setVisible( false );
        txtClienteNome.setVisible( false );
        btnFormaPagamento.setVisible( false );
        btnProcessar.setVisible( false );
        lbValorPorExtenco.setText( "" );
        mostrar_nome();
//        cmbArmazem.setModel( new DefaultComboBoxModel( accessoArmazemDao.getAllArmazemByIdUSuario( cod_usuario ) ) );

//        cmbArmazem.setModel( new DefaultComboBoxModel( accessoArmazemDao.getAllArmazemExceptoEconomatoByIdUSuario( cod_usuario ) ) );
        cmbArmazem.setModel( new DefaultComboBoxModel( armazemDao.buscaTodos1() ) );
        dc_data_documento.setDate( new Date() );
//        cmbFormaPagamento.setModel( new DefaultComboBoxModel( ( Vector ) bancoDao.buscaTodos() ) );
        cmbMoeda.setModel( new DefaultComboBoxModel( ( Vector ) moedaDao.buscaTodos() ) );
//        cmbCliente.setModel( new DefaultComboBoxModel( conexao.getElementos( "tb_cliente", "nome", false ) ) );
        cmbCliente.setModel( new DefaultComboBoxModel( clienteDao.buscaTodos() ) );
//        cmbCliente.setSelectedItem( "DIVERSOS" );
        cmbTipoDocumento.setModel( new DefaultComboBoxModel( ( Vector ) documentoDao.buscaTodos() ) );
        cmbAnoEconomico.setModel( new DefaultComboBoxModel( ( Vector ) anoEconomicoDao.buscaTodos() ) );

        txtRefDoc.requestFocus();
        //desactivar_campos();
        mostrar_ano_economico_serie();
        lb_proximo_documento.setText( "" );
        txtTotal_AOA_liquido.setText( CfMethods.formatarComoMoeda( 0.0 ) );
//        sp_valor_entregue.addKeyListener(new TratarEventoValorEntregue());
//        sp_valor_entregue.addKeyListener(new TratarTroco());

        sp_valor_entregue.setModel( CfMethodsSwing.criarSpinnerDoubleModel( 0.0, 10000000000.00, 0.0 ) );

    }// </editor-fold>   

    private void mostrar_ano_economico_serie()
    {

//        this.anoEconomico = anoEconomicoDao.getLastObject();
        this.anoEconomico = anoEconomicoDao.findAnoEconomico( getIdAnoEconomico() );
//        lb_ano_academico.setText( "ANO ECONÔMICO: " + this.anoEconomico.getSerie() );

    }

    private void mostrar_proximo_codigo_documento()
    {
        try
        {
            this.documento = documentoDao.findDocumento( getIdDocumento() );
            this.anoEconomico = anoEconomicoDao.findAnoEconomico( getIdAnoEconomico() );
            // this.doc_prox_cod = documento.getCodUltimoDoc() + 1;
            this.doc_prox_cod = vendaDao.getUltimaContagemByIdDocumentoAndAnoEconomico( getIdDocumento(), getIdAnoEconomico(), conexao ) + 1;
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

    private void mostrar_abreviacao_moeda_cambio()
    {
        try
        {
            this.moeda = moedaDao.findMoeda( getIdMoeda() );
            this.cambio = cambioDao.getLastObject( getIdMoeda() );
            lb_cambio.setText( "CAMBIO: " + String.valueOf( this.cambio.getValor() ) + " " + this.moeda.getAbreviacao() );

        }
        catch ( Exception e )
        {
            this.cambio = null;
            e.printStackTrace();
            lb_cambio.setText( "" );
        }
    }

    private static double getTaxaImposto( int idProduto )
    {
        return produtoImpostoDao.getTaxaByIdProduto( idProduto );
    }

    private static String getMotivoIsensao( int idProduto )
    {
        return produtoIsentoDao.getRegimeIsensaoByIdProduto( idProduto );
    }

    private static double getValorIVA( double taxa, double preco_venda )
    {
        return ( ( ( taxa / 100 ) + 1 ) * preco_venda );
    }

    private static void actualizar_cod_doc()
    {
        documento.setCodUltimoDoc( doc_prox_cod );
        documento.setDescricaoUltimoDoc( prox_doc );
        documento.setUltimaData( new Date() );
        try
        {
            documentoDao.edit( documento );
        }
        catch ( Exception e )
        {
            System.err.println( "Falha ao actualizar o documento" );
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

    private static void actualizar_abreviacao()
    {

        switch ( getIdDocumento() )
        {
            case DVML.DOC_FACTURA_RECIBO_FR:
                if ( true )
                {
                    abreviacao = Abreviacao.FR_A4;
                }
                else
                {
                    abreviacao = Abreviacao.FR_A6;
                }
                break;
            case DVML.DOC_FACTURA_FT:
                abreviacao = Abreviacao.FA;
                break;
            case DVML.DOC_FACTURA_PROFORMA_PP:
                abreviacao = Abreviacao.PP;
                break;
            default:
                break;
        }

    }

//    private void desabilitar_campos()
//    {
//
//        boolean valor = !( DVML.DOC_FACTURA_PROFORMA_PP == getIdDocumento() );
//
//        lbValorEnregue.setVisible( valor );
//        sp_valor_entregue.setVisible( valor );
//        lbTroco.setVisible( valor );
//        txtTroco.setVisible( valor );
////        lbFormaPagamento.setVisible( valor );
//        cmbFormaPagamento.setVisible( valor );
//        txtTroco.setText( CfMethods.formatarComoMoeda( 0.0 ) );
//
//    }
    private void refresh_table()
    {

        DefaultTableModel modelo = ( DefaultTableModel ) table_proforma.getModel();

        double preco = 0, desconto = 0, sub_total_linha = 0, sub_total_linha_com_iva = 0, taxa = 0;
        int idProduto, qtd;

        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {

            /*
                    getCodigoProduto(),
                    getDescricao_Produto(),
                    getPreco(),
                    getQuantidade(),
                    MetodosUtil.retirar_dizimas(getDesconto_produto(total_deconto)),
                    getTaxaImposto(getCodigoProduto()),
                    getTotal() - getDesconto_produto(total_deconto),
                    CfMethods.formatarComoMoeda(getQuantidade() * getValorIVA(getTaxaImposto(getCodigoProduto()), getPreco()) - getDesconto_produto(total_deconto))
            
            
             */
            try
            {

                /**
                 * Recupera os valores da tabela
                 */
                idProduto = Integer.parseInt( modelo.getValueAt( i, 0 ).toString() );
                qtd = Integer.parseInt( modelo.getValueAt( i, 3 ).toString() );
                taxa = Double.parseDouble( modelo.getValueAt( i, 3 ).toString() );

                //busca o preço em função do câmbio
                preco = getPrecoByIdProdutoAndQtd( idProduto, qtd );

                /**
                 * Processa os valores para serem actualizados na tabela
                 */
                desconto = getDesconto_produto( idProduto, preco, qtd );
                sub_total_linha = ( preco * qtd ) - desconto;
                sub_total_linha_com_iva = ( qtd * getValorIVA( getTaxaImposto( idProduto ), preco ) - desconto );

                /**
                 * actualiza os valores na tabela
                 */
                modelo.setValueAt( CfMethods.formatarComoMoeda( preco ), i, 2 );
                modelo.setValueAt( CfMethods.formatarComoMoeda( desconto ), i, 4 );
                modelo.setValueAt( CfMethods.formatarComoMoeda( sub_total_linha ), i, 6 );
                modelo.setValueAt( CfMethods.formatarComoMoeda( sub_total_linha_com_iva ), i, 7 );

            }
            catch ( Exception e )
            {
                e.printStackTrace();
            }

        }

        setTotalPagar();
        calculaTotalIVA();
        valor_por_extenco();

    }

    private static Moeda getMoeda()
    {
        String moedaSelecionada = ( String ) cmbMoeda.getSelectedItem();

        if ( moedaSelecionada == null )
        {
            return moedaDao.findMoeda( DVML.MOEDA_NACIONAL );
        }
        return moedaDao.getByDescricao( moedaSelecionada );

    }

    public static Double getPrecoByIdProdutoAndQtd( int idProduto, int qtd )
    {

        Moeda moeda = getMoeda();
        if ( moeda == null )
        {
            return null;
        }

        Cambio lastCambio = cambioDao.getLastObject( moeda.getPkMoeda() );
        try
        {
            // return  precoDao.findTbPreco( precoDao.getUltimoIdPrecoByIdProduto(  getCodigoProduto() )  ).getPrecoVenda();
            // return MetodosUtil.retirar_dizimas(precoDao.findTbPreco(precoDao.getUltimoIdPrecoByIdProduto(getCodigoProduto(), Integer.parseInt(txtQuatindade.getText()))).getPrecoVenda());
            Double valorCambio = lastCambio.getValor();
            double precoVenda = precoDao.findTbPreco( precoDao.getUltimoIdPrecoByIdProduto( idProduto, qtd ) ).getPrecoVenda().doubleValue();

            System.err.println( "PREÇO VENDA: " + precoVenda );
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

    public static double getDesconto_produto( int id_produto, double preco_total_produto, double qtd )
    {

        TbDesconto desconto = descontoDao.get_desconto_cliente_produto( getIdCliente(), id_produto );
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

    private void actualizar_moeda()
    {
        CfMethods.MOEDA = getMoeda().getAbreviacao();
        mostrar_abreviacao_moeda_cambio();
        refresh_table();
    }

    private static double getTotalIncidencia()
    {
        DefaultTableModel modelo = ( DefaultTableModel ) table_proforma.getModel();
        double qtd = 0;
        double incidencia = 0d, preco_unitario = 0d, desconto_valor_linha = 0;

        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {
            preco_unitario = CfMethods.parseMoedaFormatada( modelo.getValueAt( i, 2 ).toString() );
            qtd = Double.parseDouble( modelo.getValueAt( i, 3 ).toString() );
            double valor_percentagem = Double.parseDouble( modelo.getValueAt( i, 4 ).toString() );
            double taxa = Double.parseDouble( modelo.getValueAt( i, 5 ).toString() );
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
        DefaultTableModel modelo = ( DefaultTableModel ) table_proforma.getModel();
        double qtd = 0;
        double imposto = 0d, preco_unitario = 0d, desconto_valor_linha = 0;

        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {
            preco_unitario = CfMethods.parseMoedaFormatada( modelo.getValueAt( i, 2 ).toString() );
            qtd = Double.parseDouble( modelo.getValueAt( i, 3 ).toString() );
            double valor_percentagem = Double.parseDouble( modelo.getValueAt( i, 4 ).toString() );
            double taxa = Double.parseDouble( modelo.getValueAt( i, 5 ).toString() );
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

    private static double getTotalIncidenciaIsento()
    {
        DefaultTableModel modelo = ( DefaultTableModel ) table_proforma.getModel();
        double qtd = 0;
        double incidencia_isento = 0d, preco_unitario = 0d, desconto_valor_linha = 0;

        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {
            preco_unitario = CfMethods.parseMoedaFormatada( modelo.getValueAt( i, 2 ).toString() );
            qtd = Double.parseDouble( modelo.getValueAt( i, 3 ).toString() );
            double valor_percentagem = Double.parseDouble( modelo.getValueAt( i, 4 ).toString() );
            double taxa = Double.parseDouble( modelo.getValueAt( i, 5 ).toString() );
            // a incidência também é aplicável à produtos isentos do iva 
            if ( taxa == 0 )
            {
                desconto_valor_linha = ( ( valor_percentagem ) / 100 );
                double valor_unitario = ( preco_unitario * qtd );
                incidencia_isento += ( ( valor_unitario ) - ( valor_unitario * desconto_valor_linha ) );

            }

        }
        System.err.println( "INCIDENCIA ISENTO:  " + incidencia_isento );
        return incidencia_isento;
    }

    private static double getDescontoComercial()
    {
        DefaultTableModel modelo = ( DefaultTableModel ) table_proforma.getModel();
        double qtd = 0;
        double desconto_comercial = 0d, preco_unitario = 0d, desconto_valor_linha = 0d;

        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {
            preco_unitario = CfMethods.parseMoedaFormatada( modelo.getValueAt( i, 2 ).toString() );
            qtd = Double.parseDouble( modelo.getValueAt( i, 3 ).toString() );
            double valor_percentagem = Double.parseDouble( modelo.getValueAt( i, 4 ).toString() );
            desconto_valor_linha = ( ( valor_percentagem ) / 100 );
            double valor_unitario = ( preco_unitario * qtd );
            desconto_comercial += ( valor_unitario * desconto_valor_linha );

        }

        return desconto_comercial;
    }

    private static double getTotalIliquido()
    {
        DefaultTableModel modelo = ( DefaultTableModel ) table_proforma.getModel();
        double qtd = 0;
        double total_iliquido = 0, preco_unitario = 0;

        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {
            preco_unitario = CfMethods.parseMoedaFormatada( modelo.getValueAt( i, 2 ).toString() );
            qtd = Double.parseDouble( modelo.getValueAt( i, 3 ).toString() );
            total_iliquido += ( preco_unitario * qtd );

        }

        return total_iliquido;
    }

    private static double getDescontoFinanceiro()
    {
        double desconto_economico = 0d;
        //desconto_economico = Double.parseDouble( sp_valor_desconto_financeiro.getValue().toString() );
        return desconto_economico;
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

    private static List<TbProduto> getProdutosIsentos()
    {
        DefaultTableModel modelo = ( DefaultTableModel ) table_proforma.getModel();
        double taxa = 0.0;
        int codigo_produto = 0;
        List<TbProduto> lista_produtos_isentos = new ArrayList<>();
        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {
            codigo_produto = Integer.parseInt( modelo.getValueAt( i, 0 ).toString() );
            taxa = Double.parseDouble( modelo.getValueAt( i, 5 ).toString() );
            if ( taxa == 0.0 )
            {
                lista_produtos_isentos.add( produtoDao.findTbProduto( codigo_produto ) );
            }
        }

        return lista_produtos_isentos;

    }

    private static void reset_valor_entregue()
    {
        sp_valor_entregue.setModel( CfMethodsSwing.criarSpinnerDoubleModel( 0.0, 10000000000.00, 0.0 ) );
    }

    private static double getTotalVendaIVASemIncluirDesconto()
    {
        double taxa = 0, total_iva_local = 0, preco_unitario = 0, sub_total_iliquido = 0;
        double qtd = 0;

        DefaultTableModel modelo = ( DefaultTableModel ) table_proforma.getModel();
        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {
            preco_unitario = CfMethods.parseMoedaFormatada( modelo.getValueAt( i, 2 ).toString() );
            qtd = Double.parseDouble( modelo.getValueAt( i, 3 ).toString() );
            sub_total_iliquido = ( preco_unitario * qtd );
            taxa = Double.parseDouble( modelo.getValueAt( i, 5 ).toString() );
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

    private static TbVenda getDocumentoRef()
    {
        String ref_doc = txtRefDoc.getText();
        return vendaDao.findByCodFact( ref_doc, DVML.DOC_FACTURA_PROFORMA_PP );
    }

    private static int getIdAnoEconomico()
    {
        return anoEconomicoDao.getIdByDescricao( cmbAnoEconomico.getSelectedItem().toString() );
    }

    private boolean verifica_ano_documento_igual_economico()
    {
        int ano_economico = Integer.parseInt( anoEconomicoDao.findAnoEconomico( getIdAnoEconomico() ).getDesignacao() );
        int ano_documento = dc_data_documento.getDate().getYear() + 1900;
        return ano_documento == ano_economico;

    }

    private void actualizar_valor_tabela( PropertyChangeEvent evt )
    {
        linha_actual = table_proforma.getSelectedRow();
        DefaultTableModel modelo = ( DefaultTableModel ) table_proforma.getModel();

        /**
         * produto.getCodigo(), produto.getDesignacao(), getPrecoCompra(),
         * getQuantidade(), getUnidade_Produto(), 0, getTaxaImposto(),
         * getQuantidade() * getPrecoCompra(), MetodosUtil.getValorComIVA(
         * getQuantidade(), getTaxaImposto(), getPrecoCompra(), 0 )
         */
        double preco_venda = Double.parseDouble( String.valueOf( modelo.getValueAt( linha_actual, 2 ) ) );
        Double quantidade = Double.parseDouble( String.valueOf( modelo.getValueAt( linha_actual, 3 ) ) );

        if ( preco_venda != 0 && quantidade != 0 )
        {
            double desconto = Double.parseDouble( String.valueOf( modelo.getValueAt( linha_actual, 4 ) ) );
            if ( desconto >= 0 && desconto <= 100 )
            {
                double taxa = Double.parseDouble( String.valueOf( modelo.getValueAt( linha_actual, 5 ) ) );

                double total_item = preco_venda * quantidade;
                double total_item_com_iva = MetodosUtil.getValorComIVA( quantidade, taxa, preco_venda, desconto );

                modelo.setValueAt( preco_venda, linha_actual, 2 );
                modelo.setValueAt( quantidade, linha_actual, 3 );
                modelo.setValueAt( desconto, linha_actual, 5 );
                modelo.setValueAt( taxa, linha_actual, 6 );
                modelo.setValueAt( total_item, linha_actual, 7 );
                modelo.setValueAt( total_item_com_iva, linha_actual, 8 );

            }
            else
            {
                modelo.setValueAt( 0.0, linha_actual, 5 );
                JOptionPane.showMessageDialog( null, "Caro usuário o desconto não pode ser maior que 100%", "AVISO", JOptionPane.WARNING_MESSAGE );
            }

        }
        else
        {

            remover_item_carrinho();
            // acrescentar_um_linha_tabela_blank();
        }

        actualizar_total();

    }

    public void remover_item_carrinho()
    {

        DefaultTableModel modelo = ( DefaultTableModel ) table_proforma.getModel();
        modelo.removeRow( table_proforma.getSelectedRow() );
        actualizar_total();

    }

    private static void actualizar_total()
    {
//        txtTotal_AOA_Iliquido.setText( CfMethods.formatarComoMoeda( getTotalIliquido() ) );
//        txtTotal_AOA_IVA.setText( CfMethods.formatarComoMoeda( getTotalIVASobreDesconto() ) );
        txtTotal_AOA_Desconto.setText( CfMethods.formatarComoMoeda( getDescontoComercial() + getDescontoFinanceiro() ) );
        txtTotal_AOA_liquido.setText( CfMethods.formatarComoMoeda( getTotalAOALiquido() ) );

        valor_por_extenco();
    }

    private static boolean data_documento_superior_ou_igual_ao_ultimo_doc()
    {
        //buscando o id do documento.
        int pk_documento = getIdDocumento();
        //buscando o id do ano ecoonomico.
        int pk_ano_economico = getIdAnoEconomico();

        //busca o último documento da série em questão.
        // Integer cod_ultima_venda = vendaDao.getLastVenda( pk_documento );
        Integer cod_ultima_venda = vendaDao.getLastVenda( pk_documento, pk_ano_economico );
        if ( cod_ultima_venda != 0 )
        {

            //busca o objecto para retirar apenas a data do seu procesamento
            TbVenda venda_local = vendaDao.findTbVenda( cod_ultima_venda );
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
}
