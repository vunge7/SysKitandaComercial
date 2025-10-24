/*

 2259 - ImprimirNota
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package visao;



import java.sql.Connection;
import controller.ItemVendaController;
import controller.StockController;
import controller.TipoClienteController;
import controller.VendaController;
import dao.*;
import entity.*;
import comercial.controller.ExtratoContaClienteController;
import comercial.controller.DocumentosController;
import comercial.controller.ItemVendasController;
import comercial.controller.LugaresController;
import comercial.controller.MesasController;
import comercial.controller.PrecosController;
import comercial.controller.ProdutosController;
import comercial.controller.ProdutosIsentoController;
import comercial.controller.UsuariosController;
import comercial.controller.VendasController;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.persistence.EntityManagerFactory;
import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.DocumentFilter;
import javax.swing.text.NumberFormatter;
import kitanda.util.CfMethods;
import kitanda.util.CfMethodsSwing;
import lista.ListaNotaDebito;
import modelo.ClienteModelo;
import modelo.ItemVendaModelo;
import modelo.ProdutoModelo;
import modelo.StockModelo;
import modelo.TipoClienteModelo;
import modelo.VendaModelo;
import util.BDConexao;
import static util.DVML.*;
import util.DVML;
import util.DVML.Abreviacao;
import static util.DVML.ESTADO_NOTA.ANULADO;
import util.JPAEntityMannagerFactoryUtil;
import util.MetodosUtil;
import static util.MetodosUtil.esvaziar_tabela;
import static util.MetodosUtil.showMessageUtil;
import static visao.NotasCreditoDebitoVisao.*;

/**
 *
 * @author Domingos Dala Vunge
 */
public class NotasCreditoParcialVisao extends javax.swing.JFrame implements Runnable
{

    private static EntityManagerFactory emf = JPAEntityMannagerFactoryUtil.em;
    private ItemVendaDao itemVendaDao;
    private VasilhameDao vasilhameDao;
    private static DescontoDao descontoDao;
    private static PrecoDao precoDao;
    private static VendaDao vendaDao;
//    private BancoDao bancoDao;
    private NotasItem notasItem;
    private TbStock stock_local;
    private Notas notas;
    public static ProdutoDao produtoDao;
    private static StockDao stockDao;
    private UsuarioDao usuarioDao;
    private static DocumentoDao documentoDao;
    private static ClienteDao clienteDao;
    private MoedaDao moedaDao;
    private static AnoEconomicoDao anoEconomicoDao;
    private static ArmazemDao armazemDao;
    private NotasItemDao notasItemDao;
    private TbVasilhame vasilhame;
    private Moeda moeda;
    private static AnoEconomico anoEconomico;
    private static BDConexao conexao;
    private StockModelo stockModelo;
    private TipoClienteController tipoClienteController;
    private static VendasController vendasController;
    private static ProdutosController produtosController;
//    private static DocumentosController documentosController;
    private static ItemVendasController itemVendasController;
    private static UsuariosController usuariosController;
    private static PrecosController precosController;
    private static LugaresController lugaresController;
    private static MesasController mesasController;
    private static ProdutosIsentoController produtosIsentoController;
    private ItemVendaModelo itemVendaModelo;
    private VendaModelo vendaModelo;
    private ClienteModelo clienteModelo;
    private CambioDao cambioDao;
    private static Documento documento;
    private static ProdutoImpostoDao produtoImpostoDao;
    private Cambio cambio;
    private TipoProdutoDao tipoProdutoDao;
    private static int cod_usuario;
    private TipoClienteModelo tipoClienteModelo;
    private StockController stockController;
    private int linha = 0, coordenada = 1;
    private static int doc_prox_cod = 0;
    private double soma_total = 0, total_iva = 0;
    private ProdutoModelo produtoModelo;
    private static ProdutoIsentoDao produtoIsentoDao;
    private static int linha_actual = -1;
    private Abreviacao abreviacao;
    private static String prox_doc;
    private static BDConexao conexaoTransaction;
    private static String codFactura = "";
    private static TbCliente clientes;
    private List<TbItemVenda> fonteItens = new ArrayList<>();
    private JCheckBox chkSelecionarTodas;
    private JRadioButton rbSelecionarTodas;
    private Thread t;

    public NotasCreditoParcialVisao(int cod_usuario, BDConexao conexao) throws SQLException {
    initComponents();  // aqui o 'radio' já foi criado e está no layout visual
    
    // Se seu layout já está configurado pelo NetBeans (GroupLayout), 
    // evite chamar setLayout(new BorderLayout()) aqui para não quebrar o visual
    // setLayout(new BorderLayout()); // comente ou remova se usar GroupLayout

    // Adiciona listener ao botão já criado no editor visual
    radio.addActionListener(e -> {
    boolean selecionar = radio.isSelected();
    for (int i = 0; i < table.getRowCount(); i++) {
        table.setValueAt(selecionar, i, 0);
    }
    table.repaint();  // força repintura para atualizar visualmente
});
//    radio.addActionListener(e -> {
//        boolean selecionar = radio.isSelected();
//        for (int i = 0; i < table.getRowCount(); i++) {
//            table.setValueAt(selecionar, i, 0);
//        }
//    });

    // Se quiser adicionar o 'radio' num painel específico, faça isso no editor visual!
    // Aqui, não crie um novo painel nem adicione outro botão 'rbSelecionarTodas'

    // Continuação da sua inicialização normal
    aplicarFiltroMaisculo(txtRefDoc);
    setLocationRelativeTo(null);
    setResizable(false);

    this.cod_usuario = cod_usuario;
    this.conexao = conexao;
    vendasController = new VendasController(conexao);
    produtosController = new ProdutosController(conexao);
    itemVendasController = new ItemVendasController(conexao);
    usuariosController = new UsuariosController(conexao);
    precosController = new PrecosController(conexao);
    produtosIsentoController = new ProdutosIsentoController(conexao);
    mesasController = new MesasController(conexao);
    lugaresController = new LugaresController(conexao);

    setWindowsListener();
    init();
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
        jMenuItem1 = new javax.swing.JMenuItem();
        buttonGroup2 = new javax.swing.ButtonGroup();
        jPanel5 = new javax.swing.JPanel();
        lbPreco4 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        motivoJTextArea = new javax.swing.JTextArea();
        txtRefDoc = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        lbUsuario = new javax.swing.JLabel();
        lbCliente = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabelOBS = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        lbTotalPagar = new javax.swing.JLabel();
        lbDesconto = new javax.swing.JLabel();
        lbValorEnregue = new javax.swing.JLabel();
        lbTroco = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabelIVA = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabelTotalDescontoFinanceiro = new javax.swing.JLabel();
        jLabelTotalSemDesconto = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabelTotalDesconto = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabelTotal = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        radio = new javax.swing.JRadioButton();
        lb_ano_academico = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        btnCancelar = new javax.swing.JButton();
        anularJButton = new javax.swing.JButton();
        lb_ano_academico1 = new javax.swing.JLabel();
        lb_proximo_documento = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabelSeguradora = new javax.swing.JLabel();
        lbData = new javax.swing.JLabel();

        jMenuItem1.setText("jMenuItem1");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("...:::::  KITANDA - Nota de Crédito Parcial ::::...");

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        lbPreco4.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbPreco4.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lbPreco4.setText("Ref. Documento");
        lbPreco4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jScrollPane2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Motivo da Alteração ou Rectificação", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("SansSerif", 1, 14))); // NOI18N

        motivoJTextArea.setColumns(20);
        motivoJTextArea.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        motivoJTextArea.setRows(5);
        jScrollPane2.setViewportView(motivoJTextArea);

        txtRefDoc.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        txtRefDoc.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtRefDocActionPerformed(evt);
            }
        });

        table.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "Activo", "Cod.", "Descrição", "Preço", "Qtd.", "Desc.", "IVA", "Valor", "Valor(IVA)", "CoPag."
            }
        )
        {
            Class[] types = new Class []
            {
                java.lang.Boolean.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean []
            {
                true, false, false, false, false, false, false, false, false, false
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
            table.getColumnModel().getColumn(0).setPreferredWidth(15);
            table.getColumnModel().getColumn(1).setPreferredWidth(15);
            table.getColumnModel().getColumn(2).setPreferredWidth(140);
            table.getColumnModel().getColumn(3).setPreferredWidth(20);
            table.getColumnModel().getColumn(4).setPreferredWidth(7);
            table.getColumnModel().getColumn(5).setPreferredWidth(20);
            table.getColumnModel().getColumn(6).setPreferredWidth(10);
            table.getColumnModel().getColumn(7).setPreferredWidth(10);
            table.getColumnModel().getColumn(8).setPreferredWidth(30);
            table.getColumnModel().getColumn(9).setPreferredWidth(10);
        }

        lbUsuario.setFont(new java.awt.Font("Lucida Grande", 1, 12)); // NOI18N
        lbUsuario.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbUsuario.setText("Usuário:");

        lbCliente.setFont(new java.awt.Font("Lucida Grande", 1, 12)); // NOI18N
        lbCliente.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lbCliente.setText("Cliente:");

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/2934_32x32.png"))); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButton1ActionPerformed(evt);
            }
        });

        jLabelOBS.setText("OBS");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Dados da Factura"));

        lbTotalPagar.setFont(new java.awt.Font("Lucida Grande", 1, 12)); // NOI18N
        lbTotalPagar.setForeground(new java.awt.Color(153, 0, 0));
        lbTotalPagar.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lbTotalPagar.setText("Total da Factura :");

        lbDesconto.setFont(new java.awt.Font("Lucida Grande", 1, 12)); // NOI18N
        lbDesconto.setForeground(new java.awt.Color(153, 0, 0));
        lbDesconto.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lbDesconto.setText("Desconto da Factura :");

        lbValorEnregue.setFont(new java.awt.Font("Lucida Grande", 1, 12)); // NOI18N
        lbValorEnregue.setForeground(new java.awt.Color(153, 0, 0));
        lbValorEnregue.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lbValorEnregue.setText("Valor Entregue :");

        lbTroco.setFont(new java.awt.Font("Lucida Grande", 1, 12)); // NOI18N
        lbTroco.setForeground(new java.awt.Color(153, 0, 0));
        lbTroco.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lbTroco.setText("Troco:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbTotalPagar, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbDesconto, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbValorEnregue, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbTroco, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(lbTroco, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 8, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbTotalPagar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lbDesconto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lbValorEnregue, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Dados da Nota de Crédito", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 15), new java.awt.Color(255, 0, 0))); // NOI18N
        jPanel2.setForeground(new java.awt.Color(255, 51, 51));

        jLabel4.setText("Total IVA:");

        jLabelIVA.setText(".");

        jLabel6.setText("Desconto Financeiro");

        jLabelTotalDescontoFinanceiro.setText(".");

        jLabelTotalSemDesconto.setText(".");

        jLabel5.setText("Total s/Desconto:");

        jLabelTotalDesconto.setText(".");

        jLabel3.setText("Desconto Comercial:");

        jLabelTotal.setText(".");

        jLabel2.setText("Total Nota Crédito:");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabelTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabelTotalDesconto, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabelTotalSemDesconto, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabelIVA, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabelTotalDescontoFinanceiro, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabelTotal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel2))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabelTotalDesconto)
                        .addComponent(jLabel3)
                        .addComponent(jLabelTotalSemDesconto)
                        .addComponent(jLabel5)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelTotalDescontoFinanceiro)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabelIVA, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel4)
                        .addComponent(jLabel6)))
                .addContainerGap())
        );

        radio.setText("Seleccionar Tudo");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(radio, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel5Layout.createSequentialGroup()
                                    .addComponent(lbPreco4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGap(3, 3, 3))
                                .addComponent(txtRefDoc, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(lbCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 267, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lbUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(158, 158, 158)
                                .addComponent(jLabelOBS, javax.swing.GroupLayout.PREFERRED_SIZE, 440, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(9, 9, 9)))))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lbCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(31, 31, 31)
                                .addComponent(jLabelOBS, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(lbPreco4)
                        .addGap(7, 7, 7)
                        .addComponent(txtRefDoc, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(radio)))
                .addGap(6, 6, 6)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        lb_ano_academico.setFont(new java.awt.Font("Lucida Grande", 3, 16)); // NOI18N
        lb_ano_academico.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lb_ano_academico.setText("2020");
        lb_ano_academico.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Operações", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("SansSerif", 1, 14))); // NOI18N

        btnCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/LOGOUT - VERMELHO/Logout 32x32.png"))); // NOI18N
        btnCancelar.setText("FECHAR ESTA JANELA");
        btnCancelar.setAlignmentX(0.5F);
        btnCancelar.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnCancelarActionPerformed(evt);
            }
        });

        anularJButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/img32x32/_cancel.png"))); // NOI18N
        anularJButton.setText("EFECTUAR NOTA PARCIAL");
        anularJButton.setToolTipText("Emitir nota de credito de anulamento");
        anularJButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                anularJButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 246, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(anularJButton)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(56, 56, 56)
                .addComponent(anularJButton)
                .addGap(31, 31, 31)
                .addComponent(btnCancelar, javax.swing.GroupLayout.DEFAULT_SIZE, 64, Short.MAX_VALUE)
                .addGap(46, 46, 46))
        );

        lb_ano_academico1.setFont(new java.awt.Font("Lucida Grande", 3, 16)); // NOI18N
        lb_ano_academico1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lb_ano_academico1.setText("ANO ECONÔMICO");
        lb_ano_academico1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        lb_proximo_documento.setFont(new java.awt.Font("Lucida Grande", 1, 16)); // NOI18N
        lb_proximo_documento.setText("PRÓXIMO DOC. : XX PP/A1");

        jLabel1.setText("Seguradora: ");

        jLabelSeguradora.setText("jLabel2");

        lbData.setFont(new java.awt.Font("Lucida Grande", 1, 12)); // NOI18N
        lbData.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbData.setText("Data:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lb_ano_academico1, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lb_ano_academico, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lb_proximo_documento, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabelSeguradora, javax.swing.GroupLayout.PREFERRED_SIZE, 443, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lbData, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(50, 50, 50)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lb_ano_academico)
                    .addComponent(lb_ano_academico1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lb_proximo_documento, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(jLabelSeguradora)
                    .addComponent(lbData, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );

        getAccessibleContext().setAccessibleName("...:::::  KITANDA - FACTURAÃO ::::...");

        pack();
    }// </editor-fold>//GEN-END:initComponents


    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void anularJButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_anularJButtonActionPerformed
    {//GEN-HEADEREND:event_anularJButtonActionPerformed

        procedimento_anular_nota();
    }//GEN-LAST:event_anularJButtonActionPerformed

    private void txtRefDocActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtRefDocActionPerformed
    {//GEN-HEADEREND:event_txtRefDocActionPerformed
        // TODO add your handling code here:
        procedimento_busca();

    }//GEN-LAST:event_txtRefDocActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton1ActionPerformed
    {//GEN-HEADEREND:event_jButton1ActionPerformed
        try
        {

            remover_item_carrinho();

        }
        catch ( Exception ex )
        {
            ex.printStackTrace();
            //Logger.getLogger(VendaUsuarioVisao.class.getName()).log(Level.SEVERE, null, ex);
//            JOptionPane.showMessageDialog( null, "Possivelmente não selecionaste \n nenhuma linha ou não existe dados na tabela", "AVISO", JOptionPane.WARNING_MESSAGE );
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void tablePropertyChange(java.beans.PropertyChangeEvent evt)//GEN-FIRST:event_tablePropertyChange
    {//GEN-HEADEREND:event_tablePropertyChange
//        tablePropertyChanges( evt );
    }//GEN-LAST:event_tablePropertyChange

    private void tableMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_tableMouseClicked
    {//GEN-HEADEREND:event_tableMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tableMouseClicked

    public void remover_item_carrinho()
    {

        DefaultTableModel modelo = (DefaultTableModel) table.getModel();
        modelo.removeRow( table.getSelectedRow() );
        setTotalPagar();
        setTotalDesconto();
        setTotalEntregue();
        setTotalTroco();

    }

    public static void setTotalPagar1()
    {

        DefaultTableModel modelo = (DefaultTableModel) table.getModel();
        double total_liquido = getTotalAOALiquido1();

        lbTotalPagar.setText( CfMethods.formatarComoMoeda( total_liquido ) );
    }

    private static void reset_desconto_global()
    {
        lbDesconto.setText( String.valueOf( 0 ) );
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton anularJButton;
    private javax.swing.JButton btnCancelar;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private static javax.swing.JLabel jLabelIVA;
    private javax.swing.JLabel jLabelOBS;
    private static javax.swing.JLabel jLabelSeguradora;
    private static javax.swing.JLabel jLabelTotal;
    private static javax.swing.JLabel jLabelTotalDesconto;
    private static javax.swing.JLabel jLabelTotalDescontoFinanceiro;
    private static javax.swing.JLabel jLabelTotalSemDesconto;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private static javax.swing.JLabel lbCliente;
    private static javax.swing.JLabel lbData;
    private static javax.swing.JLabel lbDesconto;
    private javax.swing.JLabel lbPreco4;
    private static javax.swing.JLabel lbTotalPagar;
    private static javax.swing.JLabel lbTroco;
    private javax.swing.JLabel lbUsuario;
    private static javax.swing.JLabel lbValorEnregue;
    private static javax.swing.JLabel lb_ano_academico;
    private javax.swing.JLabel lb_ano_academico1;
    private static javax.swing.JLabel lb_proximo_documento;
    private static javax.swing.JTextArea motivoJTextArea;
    private javax.swing.JRadioButton radio;
    public static javax.swing.JTable table;
    private static javax.swing.JTextField txtRefDoc;
    // End of variables declaration//GEN-END:variables

    @Override
    public void run()
    {

    }

    public void actualizar_quantidade( double quantidade, TbStock stock )
    {

        double qtd = (stock.getQuantidadeExistente() - quantidade);
        stock.setQuantidadeExistente( (double) qtd );
        try
        {
            stockDao.edit( stock );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            System.err.println( "Falha ao actualizar a quantidade do produto no stock" );
        }

        //MetodosUtil.subtrai_quantidade( i, quantidade, id_armzem, conexao );
    }

    public void limpar()
    {

        soma_total = 0;
        lbTotalPagar.setText( "" );
        lbDesconto.setText( "" );
        lbValorEnregue.setText( "" );
        lbTroco.setText( "" );
        lbUsuario.setText( "" );
        lbData.setText( "" );
        lbCliente.setText( "" );
        motivoJTextArea.setText( "" );

    }

//    private void aplicarFiltroMaisculo( JTextField txtRefDoc )
    private void aplicarFiltroMaisculo( JTextField textField )
    {
        AbstractDocument doc = (AbstractDocument) textField.getDocument();
        doc.setDocumentFilter( new DocumentFilter()
        {
            @Override
            public void insertString( FilterBypass fb, int offset, String text, AttributeSet attr ) throws BadLocationException
            {
                if ( text != null )
                {
                    fb.insertString( offset, text.toUpperCase(), attr );
                }
            }

            @Override
            public void replace( FilterBypass fb, int offset, int length, String text, AttributeSet attr ) throws BadLocationException
            {
                if ( text != null )
                {
                    fb.replace( offset, length, text.toUpperCase(), attr );
                }
            }
        } );
    }

    class Coluna8Renderer extends DefaultTableCellRenderer
    {

        @Override
        public Component getTableCellRendererComponent( JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column )
        {

            Component c = super.getTableCellRendererComponent( table, value, isSelected, hasFocus, row, column );

            Boolean selecionado = (Boolean) table.getValueAt( row, 0 );
            if ( column == 8 && Boolean.TRUE.equals( selecionado ) )
            {
                c.setBackground( Color.PINK ); // ou Color.RED
            }
            else
            {
                c.setBackground( Color.WHITE );
            }

            return c;
        }
    }

//    private void configurarListenerCheckbox()
//    {
//        table.addPropertyChangeListener( evt ->
//        {
//            if ( "tableCellEditor".equals( evt.getPropertyName() ) && !table.isEditing() )
//            {
//                int row = table.getEditingRow();
//                int col = table.getEditingColumn();
//
//                if ( row >= 0 )
//                {
//                    if ( col == 0 )
//                    {
//                        Boolean selecionado = (Boolean) table.getValueAt( row, 0 );
//                        if ( Boolean.TRUE.equals( selecionado ) )
//                        {
//                            focarColunaValorIVA( row );
//                        }
//                        actualizarTotais();
//                    }
//                    else if ( col == 8 )
//                    {
//                        actualizarTotais();
//                    }
//                }
//            }
//        } );
//    }
    private void configurarListenerCheckbox()
    {
table.getModel().addTableModelListener(e -> {
    if (e.getColumn() == 8) {
//        atualizarTotalGeral();
//        atualizarTotalDescontos();
//        atualizarTotalEntregue();
    }
});
    }

    private void focarColunaValorIVA( int row )
    {
        SwingUtilities.invokeLater( () ->
        {
            table.editCellAt( row, 8 );
            table.changeSelection( row, 8, false, false );
        } );
    }

    public boolean campos_invalido_imprimir()
    {

        if ( cambio == null )
        {
            JOptionPane.showMessageDialog( null, "Por favor seleccione a moeda", "AVISO", JOptionPane.WARNING_MESSAGE );
            return true;
        }

        return false;

    }

    public boolean possivel_quantidade( int cod_produto, double qtd )
    {

        //System.err.println(conexao.getQuantidade_Existente_Publico(getFkProduto(), getCodigoArmazem()));  
        //  TbStock stock =  stockDao.getStockByDescricao(getFkProduto(), getCodigoArmazem() );
        double quant_possivel = conexao.getQuantidade_Existente_Publico( cod_produto, getVenda().getIdArmazemFK().getCodigo() ) - conexao.getQuantidade_minima_publico( cod_produto, getVenda().getIdArmazemFK().getCodigo() );
        //int quant_possivel = stock.getQuantidadeExistente() -  stock.getQuantBaixa();

        return quant_possivel >= qtd;

    }


    /* CRIACAO DO GETS  */
    public static int getIdCliente()
    {
        try
        {
//            return clienteDao.getClienteByNome ( cmbCliente.getSelectedItem ().toString () ).getCodigo ();
            return 1;
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
            return DOC_NOTA_CREDITO_NC;
        }
        catch ( Exception e )
        {
            return 0;
        }
    }

    public static String getDescricao_Produto()
    {
        return produtoDao.findTbProduto( getFkProduto() ).getDesignacao();
    }

    private void atualizarCMBVendas()
    {
        VendaDao tbVendaDao = new VendaDao( emf );

        Object[] listarVendaDoDocumento = tbVendaDao.listarDocumentoRetificaveisComOsSeguintesPKs( 1, 2 );

        if ( listarVendaDoDocumento != null )
        {
//            cmbFactura.setModel( new DefaultComboBoxModel( listarVendaDoDocumento ) );
//            cmbFactura.setSelectedIndex( -1 );
        }
        else
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

    public void actualizar_quantidade( int cod, int quantidade )
    {

        String sql = "UPDATE tb_stock SET quantidade_existente =  " + ( getQuantidadeProduto( cod ) - quantidade ) + " WHERE cod_produto_codigo = " + cod + " AND  cod_armazem = " + getVenda().getIdArmazemFK().getCodigo();
        System.out.println( "Quantidade   : " + quantidade );
        conexao.executeUpdate( sql );

    }

    public int getQuantidadeProduto( int cod_produto )
    {

        String sql = "SELECT quantidade_existente FROM  tb_stock WHERE  cod_produto_codigo = " + cod_produto + " AND cod_armazem = " + getVenda().getIdArmazemFK().getCodigo();

        ResultSet rs = BDConexao.getInstancia().executeQuery( sql );

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

        ResultSet rs = BDConexao.getInstancia().executeQuery( sql );

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

    public static TbVenda getVenda()
    {
        codFactura = txtRefDoc.getText();
        //String codFactura = (String) cmbFactura.getSelectedItem();
        return new VendaDao( emf ).findByCodFact( codFactura );
    }

    public static void main( String[] args ) throws SQLException
    {
        new NotasCreditoParcialVisao( 15, BDConexao.getInstancia() ).show( true );
    }

    public static boolean estado_critico() throws SQLException
    {
        TbStock stock = stockDao.getStockByDescricao( getFkProduto(), getVenda().getIdArmazemFK().getCodigo() );
        double qtd_minima = stock.getQuantBaixa(),
                qtd_existente = stock.getQuantidadeExistente(),
                qtd_critica = stock.getQuantCritica();

//           return conexao.getQuantidade_minima_publico(getFkProduto(), getCodigoArmazem() ) < conexao.getQuantidade_Existente_Publico( getFkProduto(), getCodigoArmazem()  ) 
//                   && conexao.getQuantidade_Existente_Publico( getFkProduto(), getCodigoArmazem()  )  <= conexao.getQuantidade_critica_public( getFkProduto(), getCodigoArmazem() );
//   
        return qtd_minima < qtd_existente
                && qtd_existente <= qtd_critica;

    }

    public static boolean possivel_quantidade() throws SQLException
    {

//        int quant_possivel = conexao.getQuantidade_Existente_Publico ( getFkProduto (), getVenda().getIdArmazemFK ().getCodigo () ) - conexao.getQuantidade_minima_publico ( getFkProduto (), getVenda().getIdArmazemFK ().getCodigo () );
//        //int quant_possivel = stock.getQuantidadeExistente() -  stock.getQuantBaixa();
//
//        return quant_possivel >= getQuantidade ();
        return true;
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
            sql = "SELECT preco_venda FROM tb_stock WHERE( cod_produto_codigo = " + codProduto + " AND cod_armazem = " + getVenda().getIdArmazemFK().getCodigo() + ")";
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
        vasilhameDao = new VasilhameDao( emf );
        descontoDao = new DescontoDao( emf );
        precoDao = new PrecoDao( emf );
        produtoDao = new ProdutoDao( emf );
        vendaDao = new VendaDao( emf );
        stockDao = new StockDao( emf );
        usuarioDao = new UsuarioDao( emf );
        clienteDao = new ClienteDao( emf );
        armazemDao = new ArmazemDao( emf );
        notasItemDao = new NotasItemDao( emf );
        itemVendaDao = new ItemVendaDao( emf );
        tipoProdutoDao = new TipoProdutoDao( emf );
//        bancoDao = new BancoDao( emf );
        anoEconomicoDao = new AnoEconomicoDao( emf );
        documentoDao = new DocumentoDao( emf );
        moedaDao = new MoedaDao( emf );
        cambioDao = new CambioDao( emf );
        produtoImpostoDao = new ProdutoImpostoDao( emf );
        produtoIsentoDao = new ProdutoIsentoDao( emf );

        mostrar_ano_economico_serie();
        lb_proximo_documento.setText( "" );

        atualizarCMBVendas();
        anularJButton.setVisible( false );
        motivoJTextArea.getDocument().addDocumentListener( new DocumentListener()
        {

            @Override
            public void insertUpdate( DocumentEvent e )
            {
                atualizarBtnSalvar();
            }

            @Override
            public void removeUpdate( DocumentEvent e )
            {
                atualizarBtnSalvar();
            }

            @Override
            public void changedUpdate( DocumentEvent e )
            {
            }
        } );

    }// </editor-fold>   

    private void mostrar_ano_economico_serie()
    {
        this.anoEconomico = anoEconomicoDao.getLastObject();
        lb_ano_academico.setText( this.anoEconomico.getSerie() );
    }

    private static void mostrar_proximo_codigo_documento()
    {
        lb_proximo_documento.setText( pegarProxDoc( DVML.DOC_NOTA_CREDITO_NC ) );
    }

    private void actualizar_cod_doc()
    {
        Documento documento = documentoDao.findDocumento( DVML.DOC_NOTA_CREDITO_NC );

        documento.setCodUltimoDoc( doc_prox_cod );
        documento.setDescricaoUltimoDoc( pegarProxDoc( DVML.DOC_NOTA_CREDITO_NC ) );
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

    private void actualizar_abreviacao()
    {

        if ( DVML.DOC_FACTURA_RECIBO_FR == getIdDocumento() )
        {

            this.abreviacao = Abreviacao.FR_A4;

        }
        else if ( DVML.DOC_FACTURA_FT == getIdDocumento() )
        {
            this.abreviacao = Abreviacao.FA;
        }
        else if ( DVML.DOC_FACTURA_PROFORMA_PP == getIdDocumento() )
        {
            this.abreviacao = Abreviacao.PP;
        }
        else if ( DVML.DOC_NOTA_CREDITO_NC == getIdDocumento() )
        {
            this.abreviacao = Abreviacao.NC;
        }
        else if ( DVML.DOC_NOTA_DEBITO_ND == getIdDocumento() )
        {
            this.abreviacao = Abreviacao.ND;
        }

    }

    private void procedimento_anular_nota()
    {
//        DefaultTableModel modelo = (DefaultTableModel) table.getModel();
        TbVenda venda = getVenda();

        if ( venda != null )
        {
            if ( venda.getStatusEliminado().equals( "false" ) )
            {
                Date data_actual = new Date();
                Date documento_ref_data = venda.getDataVenda();
                if ( documento_ref_data != null )
                {
                    if ( MetodosUtil.menor_data_1_data_2( data_actual, documento_ref_data ) )
                    {
                        JOptionPane.showMessageDialog( null, "Caro usuário verifique a data do sistema", "AVISO", JOptionPane.WARNING_MESSAGE );
                    }
                    else if ( MetodosUtil.menor_data_1_data_2( data_actual, documento_ref_data ) )
                    {
                        JOptionPane.showMessageDialog( null, "Caro usuário selecione as linhas da tabela que devem afetar a nota de crédito", "AVISO", JOptionPane.WARNING_MESSAGE );
                    }
                    else
                    {
                        anularFaturaNota();
                    }
                }
            }
            else
            {
                showMessageUtil( "Este documento já foi emitido nota de crédito.", TIPO_MENSAGEM_INFOR );
            }
        }
        else
        {
            JOptionPane.showMessageDialog( null, "Não existe documento com esta referência ou já foi eliminado.", "Aviso", JOptionPane.WARNING_MESSAGE );
        }

    }

    public void anularFaturaNota()
    {
        boolean anular = exibirMensagemConfirmacao( this, "Mensagem de confirmação", "Deseja realmentir emitir uma\nNota de Crédito Parcial?" ) == 0;

        if ( anular )
        {
            TbVenda venda_ref = getVenda();
            TbVenda venda_nota = new TbVenda();
            Date data_actual = new Date();

            Calendar calendarHoje = Calendar.getInstance();
            Calendar calendarVenda = Calendar.getInstance();
            calendarVenda.setTime( venda_ref.getDataVenda() );
            System.err.println( "Venda : " + new SimpleDateFormat( "dd/MMM/yyyy" ).format( venda_ref.getDataVenda() ) );
            System.err.println( "Hoje : " + new SimpleDateFormat( "dd/MMM/yyyy" ).format( new Date() ) );
            System.err.println( "calendarHoje.after ( calendarVenda ): " + calendarVenda.after( calendarHoje ) );

            boolean dataDeHojePosteriorADataDaVenda = calendarHoje.after( calendarVenda );

            if ( dataDeHojePosteriorADataDaVenda )
            {

//                procedimento_salvar_nota_credito_parcial_venda();
//                
                TbVenda nota_local = new TbVenda();

                nota_local.setCodigoUsuario( usuarioDao.findTbUsuario( cod_usuario ) );
                nota_local.setTotalVenda( new BigDecimal( CfMethods.parseMoedaFormatada( jLabelTotal.getText() ) ) );
                nota_local.setPerformance( "false" );
                nota_local.setCredito( "false" );
//                nota_local.setTotalVenda( new BigDecimal( CfMethods.parseMoedaFormatada( jLabelTotal.getText() ) ));
                nota_local.setValorEntregue( new BigDecimal( CfMethods.parseMoedaFormatada( jLabelTotal.getText() ) ) );
                nota_local.setTroco( new BigDecimal( 0 ) );
                nota_local.setIdArmazemFK( venda_ref.getIdArmazemFK() );
                nota_local.setDataVenda( new Date() );
                nota_local.setHora( new Date() );
                nota_local.setDataVencimento( venda_ref.getDataVencimento() );
                //Dados do cliente
                nota_local.setNomeCliente( venda_ref.getNomeCliente() );
                nota_local.setClienteNif( venda_ref.getClienteNif() );
                nota_local.setCodigoCliente( venda_ref.getCodigoCliente() );

                nota_local.setDescontoTotal( new BigDecimal( CfMethods.parseMoedaFormatada( jLabelTotalDesconto.getText() ) ) );
//                nota_local.setDescontoTotal(venda_ref.getDescontoTotal() );
                nota_local.setTotalGeral( new BigDecimal( CfMethods.parseMoedaFormatada( jLabelTotalSemDesconto.getText() ) ) );
//                notas.setIdBanco( venda_ref.getIdBanco() );
                nota_local.setStatusEliminado( "true" );
                nota_local.setFkAnoEconomico( this.anoEconomico );
                nota_local.setFkCambio( venda_ref.getFkCambio() );
                // notas.setFkDocumento( getDocumento() );
                nota_local.setFkDocumento( getDocumento() );

//                nota_local.setMotivo( motivoJTextArea.getText() );
                System.err.println( "#1 prox_doc: " + pegarProxDoc( DVML.DOC_NOTA_CREDITO_NC ) );
                nota_local.setCodFact( pegarProxDoc( DVML.DOC_NOTA_CREDITO_NC ) );
                nota_local.setRefCodFact( venda_ref.getCodFact() );
                nota_local.setTotalIva( new BigDecimal( CfMethods.parseMoedaFormatada( jLabelIVA.getText() ) ) );
//#HASH_TESTE                notas.setHashCod ( MetodosUtil.criptografia_hash ( notas.getCodNota () ) );

//                notas.setHashCod( MetodosUtil.criptografia_hash( prox_doc ) );
                nota_local.setHashCod( MetodosUtil.criptografia_hash( nota_local, getGrossTotal( venda_ref ), conexao ) );

                nota_local.setTotalPorExtenso( "0" );
                System.out.println( "STATUS:hash cod processado." );
                nota_local.setAssinatura( MetodosUtil.assinatura_doc( nota_local.getHashCod() ) );
                //notas.setDataNota( new Date() );
                nota_local.setRefDataFact( venda_ref.getDataVenda() );
                System.out.println( "STATUS:documento assinado com sucesso." );
                nota_local.setDescontoComercial( new BigDecimal( CfMethods.parseMoedaFormatada( jLabelTotalDesconto.getText() ) ) );
                nota_local.setDescontoFinanceiro( new BigDecimal( CfMethods.parseMoedaFormatada( jLabelTotalDescontoFinanceiro.getText() ) ) );
                nota_local.setTotalIncidencia( new BigDecimal( 0d ) );
                nota_local.setTotalIncidenciaIsento( new BigDecimal( CfMethods.parseMoedaFormatada( jLabelTotal.getText() ) ) );

                nota_local.setStatusEliminado( ANULADO.toString() );
//                nota_local.setAreaVenda( venda_ref.getAreaVenda() );
                nota_local.setObs( jLabelOBS.getText() );
                nota_local.setReferencia( motivoJTextArea.getText() );
//                nota_local.setStatusConvertido( DVML.STATUS_CONVERTIDO );

                try
                {
//                    Integer last_nota = VendaDao.criarVendaComProceduAnulacao( nota_local, conexao );
                    Integer last_nota = VendaDao.criarVendaComProceduAnulacaoFinal(nota_local, conexao );

                    System.out.println( "STATUS:factura criada com sucesso." );
                    boolean documentosalvoComSucesso = salvarItemNotaAnulamento( last_nota, venda_ref );

                    if ( documentosalvoComSucesso )
                    {

                        if ( venda_ref.getFkDocumento().getPkDocumento().equals( DVML.DOC_FACTURA_FT ) || venda_ref.getFkDocumento().getPkDocumento().equals( DVML.DOC_RECIBO_RC ) )
                        {
                            ExtratoContaClienteController.registro_movimento_conta_cliente( nota_local, conexao );
                        }

                        alterarStatusVenda( venda_ref );

                        if ( venda_ref.getFkDocumento().getPkDocumento() == DVML.DOC_RECIBO_RC )
                        {
                            //eliminar as armotizações da referência do documento
                            if ( !Objects.isNull( VendaDao.findByCodFact( venda_ref.getRefCodFact() ) ) )
                            {
                                eliminar_amortizacoes( VendaDao.findByCodFact( venda_ref.getRefCodFact() ).getCodigo() );
                            }

                        }

                        actualizar_cod_doc();
                        HashMap hashMap = new HashMap();
                        String telefone = venda_ref.getCodigoCliente().getTelefone();
                        String morada = venda_ref.getCodigoCliente().getMorada();
                        String email = venda_ref.getCodigoCliente().getEmail();

                        List<TbProduto> lista_produto_isentos = new ArrayList<>();
                        lista_produto_isentos = getProdutosIsentos( venda_ref );
                        String motivos_isentos = MetodosUtil.getMotivoIsensaoProdutos( lista_produto_isentos );
                        System.err.println( "MOTIVOS: " + motivos_isentos );

//                    boolean clienteConsumidorFinal = venda.getCodigoCliente ().getNome ().equalsIgnoreCase ( "Consumidor Final" );
                        boolean clienteNaoInformouOTelefone = telefone == null || telefone.isEmpty();
                        boolean clienteNaoInformouAMorada = morada == null || morada.isEmpty();
                        boolean clienteNaoInformouOEmail = email == null || email.isEmpty();
//                    hashMap.put ( "_CLIENTE_NOME", DVML._NAO_INCLUIR );
//                    hashMap.put ( "_CLIENTE_NIF", DVML._NAO_INCLUIR );

                        if ( clienteNaoInformouOEmail )
                        {
                            hashMap.put( "_CLIENTE_EMAIL", DVML._NAO_INCLUIR );
                        }
                        if ( clienteNaoInformouOTelefone )
                        {
                            hashMap.put( "_CLIENTE_TELEFONE", DVML._NAO_INCLUIR );
                        }
                        if ( clienteNaoInformouAMorada )
                        {
                            hashMap.put( "_CLIENTE_MORADA", DVML._NAO_INCLUIR );
                        }
                        procedimento_limpar_dados();
                        //Chama a factura e imprime directamente para a imprissora que estiver definidade no sistema operativo
                        ListaNotaDebito listaNotaDebito = new ListaNotaDebito( last_nota, Abreviacao.NC, false, false, hashMap, motivos_isentos );
//                        ListaVenda1 original = new ListaVenda1( last_nota, abreviacao, false, false, "Original", motivos_isentos );

                    }

                    System.out.println( "STATUS:itens adicionado na facrtura com sucesso." );
                }
                catch ( Exception e )
                {
                    System.err.println( "STATUS: falha ao actualizar a factura" );
                    e.printStackTrace();
                    JOptionPane.showMessageDialog( null, "Falha ao Processar a Factura", "FALHA", JOptionPane.ERROR_MESSAGE );
                }

                atualizarFormulario();
            }
            else
            {
                JOptionPane.showMessageDialog( null, "Erro: Não é permitido processar um documento com data anterior a factura.", "FALHA", JOptionPane.ERROR_MESSAGE );
            }

        }
    }

    public static void procedimento_salvar_nota_credito_parcial_venda()
    {
        mostrar_proximo_codigo_documento();
//        conexaoTransaction = BDConexao.getInstancia();
        comercial.controller.DocumentosController.start( conexaoTransaction );
//        Date data_documento = lbData.getDate();
//        Date data_vencimento = lbData.getDate();
        TbVenda venda_local = new TbVenda();
        TbVenda venda_original = getVenda();

        venda_local.setDataVenda( new Date() );
        Calendar calendar = Calendar.getInstance();
        calendar.setTime( new Date() );
        venda_local.setDataVencimento( new Date() );
        venda_local.setRefDataFact( venda_original.getDataVenda() );
        venda_local.setHora( new Date() );
        venda_local.setNomeCliente( lbCliente.getText() );
        venda_local.setClienteNif( venda_original.getClienteNif() );

        venda_local.setTotalGeral( new BigDecimal( getTotalIliquido() ) );
        //desconto por linha
        venda_local.setDescontoComercial( new BigDecimal( getDescontoComercial() ) );
        //imposto
        //calculaTotalIVA();
        venda_local.setTotalIva( new BigDecimal( getTotalImposto() ) );
        //desconto global

        venda_local.setDescontoFinanceiro( new BigDecimal( getDescontoFinanceiro() ) );
        venda_local.setTotalVenda( new BigDecimal( getTotal() ) );
        venda_local.setValorEntregue( new BigDecimal( getTotal() ) );
////        venda_ref.setDescontoFinanceiro( new BigDecimal( CfMethods.parseMoedaFormatada( txtValorSeguradora.getText() ) ) );
//        venda_ref.setTotalVenda( new BigDecimal( CfMethods.parseMoedaFormatada( txtValorCoparticipacao.getText() ) ) );
//        venda_ref.setValorEntregue( new BigDecimal( CfMethods.parseMoedaFormatada( txtValorCoparticipacao.getText() ) ) );
        venda_local.setTroco( new BigDecimal( 0.0 ) );
        venda_local.setTotalIncidencia( new BigDecimal( getTotalIncidencia() ) );
        venda_local.setTotalIncidenciaIsento( new BigDecimal( getTotalIncidenciaIsento() ) );
        venda_local.setDescontoTotal( new BigDecimal( getDescontoComercial() + getDescontoFinanceiro() ) );
        venda_local.setIdArmazemFK( venda_original.getIdArmazemFK() );
        venda_local.setCodigoUsuario( new TbUsuario( cod_usuario ) );
        venda_local.setCodigoCliente( venda_original.getCodigoCliente() );
        venda_local.setFkAnoEconomico( anoEconomico );
        venda_local.setReferencia( "" );
        venda_local.setObs( motivoJTextArea.getText() );
        venda_local.setNomeConsumidorFinal( venda_original.getNomeConsumidorFinal() );
        venda_local.setFkDocumento( documento );
        venda_local.setRefCodFact( venda_original.getCodFact() );
        venda_local.setCodFact( pegarProxDoc( DVML.DOC_NOTA_CREDITO_NC ) );
        venda_local.setHashCod( MetodosUtil.criptografia_hash( venda_local, getTotal(), conexaoTransaction ) );
        venda_local.setTotalPorExtenso( lbValorPorExtenco.getText() );
//        venda_local.setValorCoparticipacao( new BigDecimal( 0d ) );
//        venda_local.setValorSeguradora( new BigDecimal( 0d ) );
//        venda_local.setAreaVenda( venda_original.getAreaVenda() );
        System.out.println( "STATUS:hash cod processado." );
        venda_local.setAssinatura( MetodosUtil.assinatura_doc( venda_local.getHashCod() ) );
        venda_local.setFkCambio( venda_original.getFkCambio() );
        /*status documento*/
        venda_local.setStatusEliminado( "ANULADO" );
        venda_local.setPerformance( "false" );
        venda_local.setCredito( "false" );

        try
        {

            if ( vendasController.salvar( venda_local ) )
            {
                Integer last_venda = vendasController.getLastVenda().getCodigo();

                if ( Objects.isNull( last_venda ) || last_venda == 0 )
                {
                    DocumentosController.rollback( conexaoTransaction );
                    conexaoTransaction.close();
                    return;
                }
                System.err.println( "last_venda: " + last_venda );
                System.out.println( "STATUS:factura criada com sucesso." );

                if ( last_venda != null )
                {

//                    salvar_nota_credito_parcial_item_venda( last_venda );
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
            DocumentosController.rollback( conexaoTransaction );
            conexaoTransaction.close();
        }

    }

    private static double getTotalIliquido()
    {
        DefaultTableModel modelo = (DefaultTableModel) table.getModel();
        double qtd = 0d;
        double total_iliquido = 0, preco_unitario = 0;

        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {
            preco_unitario = CfMethods.parseMoedaFormatada( modelo.getValueAt( i, 3 ).toString() );
            qtd = Double.parseDouble( modelo.getValueAt( i, 4 ).toString() );
            total_iliquido += ( preco_unitario * qtd );

        }

        return total_iliquido;
    }

//    public static void salvar_nota_credito_parcial_item_venda( Integer cod_venda )
//    {
//        TbItemVenda itemVenda = null;
//
//        boolean efectuada = true;
//
//        for ( int i = 0; i < table.getRowCount(); i++ )
//        {
//            try
//            {
//                double valor_clinica = 0;
//                double valor_medico = 0;
//                double valor_requisitante = 0;
//                double valor_instituicao = 0;
////                double preco_generico = CfMethods.parseMoedaFormatada( table.getValueAt( i, 8 ).toString() );
//
//                int codigo_produto = Integer.parseInt( table.getValueAt( i, 1 ).toString() );
//
//                TbProduto produto_local = (TbProduto) produtosController.findById( Integer.parseInt( String.valueOf( table.getModel().getValueAt( i, 1 ) ) ) );
//
//                itemVenda = new TbItemVenda();
//                itemVenda.setCodigoProduto( produto_local );
//                itemVenda.setCodigoVenda( new TbVenda( cod_venda ) );
//                itemVenda.setQuantidade( Double.parseDouble( String.valueOf( table.getModel().getValueAt( i, 4 ) ) ) );
//
//                itemVenda.setDesconto( Double.parseDouble( String.valueOf( table.getModel().getValueAt( i, 5 ) ) ) );
//
//                itemVenda.setValorIva( Double.parseDouble( String.valueOf( table.getModel().getValueAt( i, 6 ) ) ) );
//                itemVenda.setMotivoIsensao( getMotivoIsensao( itemVenda.getCodigoProduto().getCodigo() ) );
//                itemVenda.setCodigoIsensao( MetodosUtil.getCodigoRegime( itemVenda.getCodigoProduto().getCodigo() ) );
//                itemVenda.setTotal( new BigDecimal( CfMethods.parseMoedaFormatada( String.valueOf( table.getModel().getValueAt( i, 8 ) ) ) ) );
//                itemVenda.setPercentagemCliente( Double.parseDouble( String.valueOf( table.getModel().getValueAt( i, 11 ) ) ) );
//                itemVenda.setPercentagemSeguradora( Double.parseDouble( String.valueOf( table.getModel().getValueAt( i, 12 ) ) ) );
//                itemVenda.setFkPreco( precosController.getLastIdPrecoByIdProduto( itemVenda.getCodigoProduto().getCodigo(), itemVenda.getQuantidade(), getIdSeguradora() ) );
//                itemVenda.setStatusConsulta( false );
//                itemVenda.setStatusAnalise( false );
//                itemVenda.setStatusServico( false );
//                itemVenda.setValorClinica( valor_clinica );
//                itemVenda.setValorMedico( valor_medico );
//                itemVenda.setValorLaboratorio( valor_instituicao );
//                itemVenda.setValorRequisitante( valor_requisitante );
//                itemVenda.setCodRequisitante( 1 );
//                itemVenda.setFkUsuario( new TbUsuario( 15 ) );
//                itemVenda.setDataServico( new Date() );
//                /*setando a mesa e lugar para cunprir a formalidade só aplica-se somente para resstauração*/
//                itemVenda.setFkLugares( new TbLugares( 1 ) );
//                itemVenda.setFkMesas( new TbMesas(1 ) );
//                //cria o item venda
//                if ( !itemVendasController.salvar( itemVenda ) )
//                {
//                    DocumentosController.rollback( conexaoTransaction );
//                    conexaoTransaction.close();
//                    return;
//                }
//
//
//                boolean isStocavel = produto_local.getStocavel().equals( "true" );
//
//                if ( isStocavel )
//                {
//                    stock_local = stocksController.getStockByIdProdutoAndIdArmazem( itemVenda.getCodigoProduto().getCodigo(), getCodigoArmazem() );
//                }
//
//                if ( getIdDocumento() == DOC_FACTURA_RECIBO_FR || getIdDocumento() == DOC_FACTURA_FT )
//                {
//                    System.out.println( "passei quando é FR ou FT" );
//                    if ( !Objects.isNull( stock_local ) && isStocavel )
//                    {
//                        System.out.println( "chamei o actualizar quantidade" );
//                        actualizar_quantidade( itemVenda.getCodigoProduto().getCodigo(), itemVenda.getQuantidade(), conexaoTransaction );
//                    }
//
//                }
//            }
//            catch ( Exception e )
//            {
//                vendasController.eliminar( cod_venda );
//                e.printStackTrace();
//                efectuada = false;
//                JOptionPane.showMessageDialog( null, "Falha ao registrar o produto: " + itemVenda.getCodigoProduto().getCodigo() + " na Factura" );
//                DocumentosController.rollback( conexaoTransaction );
//                conexaoTransaction.close();
//                return;
//            }
//
//        }
//
//        if ( efectuada )
//        {
//            if ( getIdDocumento() == DOC_FACTURA_RECIBO_FR )
//            {
//                if ( !registrar_forma_pagamento( cod_venda ) )
//                {
//                    DocumentosController.rollback( conexaoTransaction );
//                    conexaoTransaction.close();
//                    JOptionPane.showMessageDialog( null, "Falha ao registrar a forma de pagamento.", "Falha", JOptionPane.WARNING_MESSAGE );
//                    return;
//                }
//            }
//
//            clientes = clientesController.findByCodigo( getIdCliente() );
//            EntidadeSeguradora entidade = seguradorasController.getSeguradoraByCodigo( clientes.getFkEntidadeSeguradora().getPkEntidadeSeguradora() );
//            if ( entidade.getPkEntidadeSeguradora() > 1 )
//            {
//                ck_A4.setSelected( true );
////                JOptionPane.showMessageDialog( null, "Factura efectuada com sucesso!.." );
//            }
//            else if ( entidade.getPkEntidadeSeguradora() == 1 )
//            {
//
//                JOptionPane.showMessageDialog( null, "Factura efectuada com sucesso!.." );
//            }
//            DocumentosController.commitTransaction( conexaoTransaction );
//
//            List<TbProduto> lista_produto_isentos = new ArrayList<>();
//            lista_produto_isentos = getProdutosIsentos();
//            String motivos_isentos = MetodosUtil.getMotivoIsensaoProdutos( lista_produto_isentos );
//            System.err.println( "MOTIVOS: " + motivos_isentos );
//
//            try
//            {
//                if ( entidade.getPkEntidadeSeguradora() > 1 )
//                {
////                    ck_A4.setSelected( true );
////                    System.out.println( "Estou antes do Limpar" );
////                    limpar();
////                    remover_all_produto();
//
//                }
//                else if ( entidade.getPkEntidadeSeguradora() == 1 )
//                {
////                    ck_A4.setSelected( true );
//                    System.out.println( "Estou antes do Limpar" );
//                    limpar();
//                    remover_all_produto();
//
//                }
//
//            }
//            catch ( Exception e )
//            {
//            }
//
//            txtQuatindade.setText( "1" );
//            txtIniciaisCliente.requestFocus();
//            txtQuantidaStock.setText( String.valueOf( conexaoTransaction.getQtdExistenteStock( getCodigoProduto(), getCodigoArmazem() ) ) );
////            venda = vendasController.getLastVenda();
//            System.err.println( "****************** CHEGUEI NA BUSCA ********************++" );
//            TbVenda vendaLocal = vendasController.findByCod( cod_venda );
//
//            if ( !Objects.isNull( internamentos ) )
//            {
//                procedimentoActualizarInternamento( vendaLocal );
//            }
//
//            actualizarStatusAnalise( vendaLocal );
//            actualizarStatusAvaliacoes( vendaLocal );
//
//            if ( ckExamesExternos.isSelected() && ( Objects.nonNull( listaExamesExternos ) ) && !listaExamesExternos.isEmpty() && getIdDocumento() != DVML.DOC_FACTURA_PROFORMA_PP )
//            {
//                System.out.println( "Entrei na condição dos exames externos" );
//                System.out.println( "Tamanho Lista Exames Externos: " + listaExamesExternos.size() );
//                System.out.println( "Lista Analises Exames Externos: " + listaExamesExternos );
//
//                procedimentoCriarProcessoAutomatico( vendaLocal );
//
//            }
//            cmbArmazem.setSelectedItem( "Clinica" );
//            ckExamesExternos.setSelected( true );
//            conexaoTransaction.close();
//
//            int numeroVias = (int) Double.parseDouble( spnCopia.getValue().toString() );
//            for ( int i = 1; i <= numeroVias; i++ )
//            {
//
//                switch (i)
//                {
//                    case 1:
//                        ListaVenda1 original = new ListaVenda1( cod_venda, abreviacao, false, ck_simplificada.isSelected(), "Original", motivos_isentos );
//                        break;
//                    case 2:
//                        ListaVenda1 original_duplicado = new ListaVenda1( cod_venda, abreviacao, false, ck_simplificada.isSelected(), "Original", motivos_isentos );
//                        break;
//                    case 3:
//                        ListaVenda1 original_triplicado = new ListaVenda1( cod_venda, abreviacao, false, ck_simplificada.isSelected(), "Original", motivos_isentos );
//                        break;
//                }
//            }
//        }
//
//    }
//    private static int getIdSeguradora()
//    {
//        return clientes.getFkEntidadeSeguradora().getPkEntidadeSeguradora();
//    }

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

//    private void aplicarRenderizadorPersonalizado()
//    {
//        TableCellRenderer renderer = new DefaultTableCellRenderer()
//        {
//            @Override
//            public Component getTableCellRendererComponent( JTable table, Object value,
//                    boolean isSelected, boolean hasFocus, int row, int column )
//            {
//
//                Component c = super.getTableCellRendererComponent( table, value, isSelected, hasFocus, row, column );
//
//                Boolean selecionado = (Boolean) table.getValueAt( row, 0 );
//                if ( column == 8 && Boolean.TRUE.equals( selecionado ) )
//                {
//                    c.setBackground( Color.RED );
//                    c.setForeground( Color.WHITE );
//                }
//                else
//                {
//                    c.setBackground( isSelected ? table.getSelectionBackground() : Color.WHITE );
//                    c.setForeground( isSelected ? table.getSelectionForeground() : Color.BLACK );
//                }
//
//                return c;
//            }
//        };
////        table.getColumnModel().getColumn( 8 ).setCellRenderer( new BoldCellRenderer() );
//        table.getColumnModel().getColumn( 8 ).setCellRenderer( renderer );
//    }
    private void aplicarRenderizadorPersonalizado()
    {
        table.getColumnModel().getColumn( 8 ).setCellRenderer( new DefaultTableCellRenderer()
        {
            @Override
public Component getTableCellRendererComponent(JTable table, Object value,
        boolean isSelected, boolean hasFocus, int row, int column) {

    Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

    if (column == 8) {
        Boolean ativo = false;
        Object valCheckbox = table.getValueAt(row, 0);
        if (valCheckbox instanceof Boolean) {
            ativo = (Boolean) valCheckbox;
        }

        if (ativo) {
            // Pinta a célula mesmo com foco
            c.setBackground(new Color(255, 255, 150)); // amarelo claro, por exemplo
            c.setFont(c.getFont().deriveFont(Font.BOLD));
        } else {
            // Cor padrão para desmarcado (sem foco)
            if (isSelected) {
                c.setBackground(table.getSelectionBackground());
                c.setForeground(table.getSelectionForeground());
            } else {
                c.setBackground(Color.WHITE);
                c.setForeground(Color.BLACK);
            }
            c.setFont(c.getFont().deriveFont(Font.PLAIN));
        }

        // Se quiser um destaque especial quando a célula está com foco, pode ajustar aqui:
if (c instanceof JComponent) {
    JComponent jc = (JComponent) c;
    if (hasFocus) {
        jc.setBorder(BorderFactory.createLineBorder(Color.BLUE, 1));
    } else {
        jc.setBorder(null);
    }
}

    } else {
        // Outras colunas - padrão
        if (isSelected) {
            c.setBackground(table.getSelectionBackground());
            c.setForeground(table.getSelectionForeground());
        } else {
            c.setBackground(Color.WHITE);
            c.setForeground(Color.BLACK);
        }
        c.setFont(c.getFont().deriveFont(Font.PLAIN));
((JComponent) c).setBorder(null);
    }

    return c;
}
        } );
    }

    public boolean salvarItemNotaAnulamento( int last_cod, TbVenda documentoParm )
    {
        DefaultTableModel modelo = (DefaultTableModel) table.getModel();

        TbVenda lastVenda = vendaDao.findTbVenda( last_cod );
//        TbItemVenda venda_local = getVenda();

//        selecionado = true;
//        List<TbItemVenda> itensDocumento = documentoParm.getTbItemVendaList();
        boolean sucesso = true;
//        for ( TbItemVenda itemDocumentos : itensDocumento )
//        {
        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {
            boolean selecionado = (boolean) modelo.getValueAt( i, 0 );
            if ( selecionado )
            {
                Integer codigoProduto = Integer.parseInt( modelo.getValueAt( i, 1 ).toString() );

                double quantidade = Double.parseDouble( String.valueOf( modelo.getValueAt( i, 4 ) ) );
                double desconto = Double.parseDouble( String.valueOf( modelo.getValueAt( i, 5 ) ) );
                double iva = Double.parseDouble( String.valueOf( modelo.getValueAt( i, 6 ) ) );
//                Integer preco = Integer.parseInt( String.valueOf( modelo.getValueAt( i, 3 ) ) );
//                double valor = Double.parseDouble( String.valueOf( modelo.getValueAt( i, 7 ) ) );
                String valorStr = String.valueOf( modelo.getValueAt( i, 8 ) ).replaceAll( "[^\\d,\\.]", "" ).replace( ",", "." );

                double Total = 0.0;
                try
                {
                    Total = Double.parseDouble( valorStr );
                }
                catch ( NumberFormatException ex )
                {
                    ex.printStackTrace();
                    // Você pode lançar exceção ou definir Total = 0.0 por segurança
                }
//                double Total = CfMethods.parseMoedaFormatada( String.valueOf( modelo.getValueAt( i, 8 ) ) );

                TbItemVenda itemVendaLocal = new TbItemVenda();
                itemVendaLocal.setCodigoProduto( new TbProduto( codigoProduto ) );
                itemVendaLocal.setCodigoVenda( lastVenda );
                itemVendaLocal.setQuantidade( quantidade );
                itemVendaLocal.setDesconto( desconto );
                itemVendaLocal.setValorIva( iva );
                itemVendaLocal.setMotivoIsensao( getMotivoIsensao( itemVendaLocal.getCodigoProduto().getCodigo() ) );
                itemVendaLocal.setTotal( new BigDecimal( Total ) );
                itemVendaLocal.setFkLugares( (TbLugares) lugaresController.findById( DVML.LUGAR_BALCAO ) );
                itemVendaLocal.setFkMesas( (TbMesas) mesasController.findById( DVML.MESA_BALCAO ) );
//                itemVendaLocal.setFkUsuario( new TbUsuario( cod_usuario ) );
                itemVendaLocal.setDataServico( new Date() );
                itemVendaLocal.setFkPreco( getPrecoByCodPoroduto( codigoProduto ) );

                try
                {
                    //cria o item notas
                    itemVendaDao.criarComProcedimentos( itemVendaLocal, conexao );
                    this.stock_local = stockDao.get_stock_by_id_produto_and_id_armazem( itemVendaLocal.getCodigoProduto().getCodigo(), documentoParm.getIdArmazemFK().getCodigo() );
                    if ( documentoParm.getFkDocumento().getPkDocumento() == DOC_FACTURA_RECIBO_FR || documentoParm.getFkDocumento().getPkDocumento() == DOC_FACTURA_FT )
                    {
                        //so adiciona caso existir o produto no armazém em questão.
//                    if ( stock_local.getCodigo() != 0 )

                        if ( !Objects.isNull( stock_local ) )
                        {
                            MetodosUtil.adiciona_quantidade( itemVendaLocal.getCodigoProduto().getCodigo(), itemVendaLocal.getQuantidade(), getVenda().getIdArmazemFK().getCodigo(), conexao );
                        }
                    }

                }
                catch ( Exception e )
                {
                    sucesso = false;
                    e.printStackTrace();
                }
            }

        }
        if ( sucesso )
        {
            abreviacao = Abreviacao.ND;
            actualizar_cod_doc();
        }

        return sucesso;
    }

    public static void exibirMensagemErro( Component rootPane, String tituloJanela, String menssagem )
    {
        JOptionPane.showMessageDialog( rootPane, menssagem, tituloJanela, JOptionPane.ERROR_MESSAGE );
    }

    public static int exibirMensagemConfirmacao( Component rootPane, String tituloJanela, String menssagem )
    {
        return JOptionPane.showOptionDialog( rootPane, menssagem, tituloJanela, JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, new Object[]
        {
            "Sim", "Não"
        }, "Não" );
    }

    private static double getDescontoFinanceiro()
    {
        double desconto_economico = 0d;
//        desconto_economico = Double.parseDouble( txtDescontoFinanceiro.getText() );
        desconto_economico = CfMethods.parseMoedaFormatada( jLabelTotalDesconto.getText() );
        return desconto_economico;
    }

    private static double getTotal()
    {
        double total = 0d;
//        desconto_economico = Double.parseDouble( txtDescontoFinanceiro.getText() );
        total = CfMethods.parseMoedaFormatada( jLabelTotal.getText() );
        return total;
    }

    //DVML.DOC_NOTA_CREDITO_NC
    public static String pegarProxDoc( int idDocumento )
    {
//        Documento documento = documentoDao.findDocumento( idDocumento );
//
//        if ( documento != null )
//        {
//            System.err.println( "documento: " + documento );
//            this.doc_prox_cod = documento.getCodUltimoDoc() + 1;
//            System.err.println( "#3 " + doc_prox_cod );
//            System.err.println( "#4 " + String.format( "%s %s/%S", documento.getAbreviacao(), this.anoEconomico.getSerie(), documento.getCodUltimoDoc() + 1 ) );
//            return String.format( "%s %s/%S", documento.getAbreviacao(), this.anoEconomico.getSerie(), documento.getCodUltimoDoc() + 1 );
//        }
//
//        return "";

        try
        {
            documento = documentoDao.findDocumento( getIdDocumento() );
            anoEconomico = anoEconomicoDao.findAnoEconomico( getIdAnoEconomico() );
            // this.doc_prox_cod = documento.getCodUltimoDoc() + 1;
            doc_prox_cod = vendaDao.getUltimaContagemByIdDocumentoAndAnoEconomico( getIdDocumento(), getIdAnoEconomico(), conexao ) + 1;
            prox_doc = documento.getAbreviacao();
            //FA Série / codigo
            prox_doc += " " + anoEconomico.getSerie() + "/" + doc_prox_cod;
            lb_proximo_documento.setText( "PRÓX.DOC. :" + prox_doc );
            return prox_doc;
        }
        catch ( Exception e )
        {
            documento = null;
            lb_proximo_documento.setText( "" );
        }
        return "";
    }

    //DVML.DOC_NOTA_CREDITO_NC
    public String pegarDocAnterior( int idDocumento )
    {
        Documento documento = documentoDao.findDocumento( idDocumento );

        if ( documento != null )
        {
            System.err.println( "documento: " + documento );
            this.doc_prox_cod = documento.getCodUltimoDoc() + 1;

            return String.format( "%s %s/%S", documento.getAbreviacao(), this.anoEconomico.getSerie(), documento.getCodUltimoDoc() + 1 );
        }

        return "";
    }

    private Documento getDocumento()
    {
        return documentoDao.findDocumento( DVML.DOC_NOTA_CREDITO_NC );
    }

    private void atualizarFormulario()
    {
        System.err.println( "" );
        atualizarCMBVendas();
    }

//    private static double getTotalIliquido()
//    {
//        DefaultTableModel modelo = (DefaultTableModel) tabelaItensNotas.getModel();
//        int qtd = 0;
//        double total_iliquido = 0, preco_unitario = 0;
//
//        for ( int i = 0; i < modelo.getRowCount(); i++ )
//        {
//            preco_unitario = Double.parseDouble( modelo.getValueAt( i, 2 ).toString() );
//            qtd = Integer.parseInt( modelo.getValueAt( i, 3 ).toString() );
//            total_iliquido += ( preco_unitario * qtd );
//
//        }
//
//        return total_iliquido;
//    }
//    
//        private static double getDescontoFinanceiro()
//    {
//        double desconto_economico = 0d;
//        desconto_economico = Double.parseDouble( sp_valor_desconto_financeiro.getValue().toString() );
//        return desconto_economico;
//    }
    private static double getTotalAOALiquido()
    {
        double valores = (getTotalIliquido() + getTotalImposto());
        double descontos = (getDescontoComercial());
        System.out.println( "TotalIliquido: " + getTotalIliquido() );
        System.out.println( "TotalImposto: " + getTotalImposto() );
        System.out.println( "TotalDescontoComercial: " + getDescontoComercial() );
        System.out.println( "Total Liquido: " + ( valores - descontos ) );
        return ( valores - descontos );
    }

    private static double getTotalAOALiquido1()
    {
        double valores = (getTotalIliquido() + getTotalImposto());
        double descontos = (getDescontoComercial() + getDescontoFinanceiro1());
        System.out.println( "TotalIliquido: " + getTotalIliquido() );
        System.out.println( "TotalImposto: " + getTotalImposto() );
        System.out.println( "TotalDescontoComercial: " + getDescontoComercial() );
        System.out.println( "Total Liquido: " + ( valores - descontos ) );
        return ( valores - descontos );
    }

    private static double getDescontoComercial()
    {
        DefaultTableModel modelo = (DefaultTableModel) tabelaItensNotas.getModel();
        int qtd = 0;
        double desconto_comercial = 0d, preco_unitario = 0d, desconto_valor_linha = 0d;

        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {
            preco_unitario = Double.parseDouble( modelo.getValueAt( i, 2 ).toString() );
            qtd = Integer.parseInt( modelo.getValueAt( i, 3 ).toString() );
            double valor_percentagem = Double.parseDouble( modelo.getValueAt( i, 4 ).toString() );
            desconto_valor_linha = ( ( valor_percentagem ) / 100 );
            double valor_unitario = (preco_unitario * qtd);
            desconto_comercial += ( valor_unitario * desconto_valor_linha );

        }

        return desconto_comercial;
    }

    private static double getDescontoFinanceiro1()
    {
        double desconto_economico = 0d;
        desconto_economico = Double.parseDouble( lbDesconto.getText() );
        return desconto_economico;
    }

    private static double getTotalIncidencia()
    {
        DefaultTableModel modelo = (DefaultTableModel) tabelaItensNotas.getModel();
        int qtd = 0;
        double incidencia = 0d, preco_unitario = 0d, desconto_valor_linha = 0;

        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {
            preco_unitario = Double.parseDouble( modelo.getValueAt( i, 3 ).toString() );
            qtd = Integer.parseInt( modelo.getValueAt( i, 4 ).toString() );
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

    private static double getTotalImposto()
    {
        DefaultTableModel modelo = (DefaultTableModel) tabelaItensNotas.getModel();
        int qtd = 0;
        double imposto = 0d, preco_unitario = 0d, desconto_valor_linha = 0;

        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {
            preco_unitario = Double.parseDouble( modelo.getValueAt( i, 2 ).toString() );
            qtd = Integer.parseInt( modelo.getValueAt( i, 3 ).toString() );
            double valor_percentagem = Double.parseDouble( modelo.getValueAt( i, 4 ).toString() );
            double taxa = Double.parseDouble( modelo.getValueAt( i, 5 ).toString() );
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

    private void atualizarBtnSalvar()
    {
        boolean selecionouFactura = !motivoJTextArea.getText().isEmpty();
        boolean digitouOMotivo = !txtRefDoc.getText().isEmpty();

        anularJButton.setVisible( selecionouFactura && digitouOMotivo );
    }

    private static double getTotalNotaIVASemIncluirDesconto( TbVenda documentoOrigem )
    {
        return MetodosUtil.getTotalVendaIVASemIncluirDesconto( documentoOrigem.getTbItemVendaList() ).doubleValue();
    }

    private double getGrossTotal( TbVenda documentoOrigem )
    {
        return documentoOrigem.getTotalGeral().doubleValue() + getTotalNotaIVASemIncluirDesconto( documentoOrigem );
    }

    private List<TbProduto> getProdutosIsentos()
    {
        DefaultTableModel modelo = (DefaultTableModel) tabelaItensNotas.getModel();
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

    private List<TbProduto> getProdutosIsentos( TbVenda notas )
    {
//        List<NotasItem> list_item = notas.getNotasItemList();
        List<TbProduto> lista_produtos_isentos = new ArrayList<>();
//        for ( NotasItem linha_nota : list_item )
//        {
//            if ( linha_nota.getValorIva() == 0d )
//            {
//                lista_produtos_isentos.add( linha_nota.getFkProduto() );
//            }
//
//        }

//        DefaultTableModel modelo = ( DefaultTableModel ) tabelaItensNotas.getModel();
//        double taxa = 0.0;
//        int codigo_produto = 0;
//        List<TbProduto> lista_produtos_isentos = new ArrayList<>();
//        for ( int i = 0; i < modelo.getRowCount(); i++ )
//        {
//            codigo_produto = Integer.parseInt( modelo.getValueAt( i, 0 ).toString() );
//            taxa = Double.parseDouble( modelo.getValueAt( i, 5 ).toString() );
//            if ( taxa == 0.0 )
//            {
//                lista_produtos_isentos.add( produtoDao.findTbProduto( codigo_produto ) );
//            }
//        }
        return lista_produtos_isentos;

    }

    private void alterarStatusVenda( TbVenda venda )
    {

        try
        {
            venda.setStatusEliminado( "true" );
            new VendaDao( emf ).edit( venda );

        }
        catch ( Exception e )
        {
        }

    }

    public static int getIdAnoEconomico()
    {
        try
        {
            System.out.println( "LB ANO: " + lb_ano_academico.getText() );
            return anoEconomicoDao.getIdByDescricao( lb_ano_academico.getText() );
        }
        catch ( Exception e )
        {
            return 0;
        }
    }

    private DefaultTableModel criarModeloTabela()
    {
        String[] colunas =
        {
            "Seleccionar", "Cod.Art", "Descrição", "Preço", "Qtd", "Desconto", "IVA", "Valor", "Valor(IVA)", "Co.Pag"
        };

        return new DefaultTableModel( null, colunas )
        {
            @Override
            public boolean isCellEditable( int row, int column )
            {
                if ( column == 0 )
                {
                    return true;
                }
                if ( column == 8 && Boolean.TRUE.equals( getValueAt( row, 0 ) ) )
                {
                    return true;
                }
                return false;
            }

            @Override
            public Class<?> getColumnClass( int columnIndex )
            {
                if ( columnIndex == 0 )
                {
                    return Boolean.class;
                }
                if ( columnIndex >= 3 && columnIndex <= 8 )
                {
                    return Double.class;
                }
                return String.class;
            }
        };
    }

    private void configurarRenderizadores()
    {
        DefaultTableCellRenderer moedaRenderer = new DefaultTableCellRenderer()
        {
            @Override
            protected void setValue( Object value )
            {
                if ( value instanceof Number )
                {
                    setText( CfMethods.formatarComoMoeda( ( (Number) value ).doubleValue() ) );
                }
                else
                {
                    setText( "" );
                }
            }
        };

        int[] colunasDeMoeda =
        {
            3, 7, 8
        };
        for ( int col : colunasDeMoeda )
        {
            table.getColumnModel().getColumn( col ).setCellRenderer( moedaRenderer );
        }
    }

//private void configurarListenerCheckbox() {
//table.addPropertyChangeListener(evt -> {
//    if ("tableCellEditor".equals(evt.getPropertyName())) {
//        if (!table.isEditing()) {
//            int linha = table.getEditingRow();
//            if (linha >= 0) {
//                Object valor = table.getValueAt(linha, 0);
//                boolean marcado = (valor instanceof Boolean) && (Boolean) valor;
//
//                if (marcado) {
//                    // Atualiza a célula (se necessário)
//                    // Ex: table.setValueAt(novoValor, linha, 8);
//
//                    // Atualiza totais
//                    actualizarTotais();
//
//                    // Força repaint e foca na célula da coluna 8
//                    table.requestFocus();
//                    table.changeSelection(linha, 8, false, false);
//                    table.editCellAt(linha, 8);
//                }
//
//                // Atualiza a linha para repintar (para que a célula fique vermelha)
//                ((AbstractTableModel) table.getModel()).fireTableRowsUpdated(linha, linha);
//            }
//        }
//    }
//});
//}
    public Class<?> getColumnClass( int columnIndex )
    {
        if ( columnIndex == 0 )
        {
            return Boolean.class;
        }
        if ( columnIndex == 4 || columnIndex == 5 || columnIndex == 6 || columnIndex == 7 || columnIndex == 8 )
        {
            return Double.class;
        }
        return String.class;
    }

    public class CampoTextoMaiusculo
    {

        void aplicarFiltroMaisculo( javax.swing.JTextField textField )
        {
            AbstractDocument doc = (AbstractDocument) textField.getDocument();
            doc.setDocumentFilter( new DocumentFilter()
            {
                @Override
                public void insertString( DocumentFilter.FilterBypass fb, int offset, String text, AttributeSet attr ) throws BadLocationException
                {
                    if ( text != null )
                    {
                        fb.insertString( offset, text.toUpperCase(), attr );
                    }
                }

                @Override
                public void replace( DocumentFilter.FilterBypass fb, int offset, int length, String text, AttributeSet attrs ) throws BadLocationException
                {
                    if ( text != null )
                    {
                        fb.replace( offset, length, text.toUpperCase(), attrs );
                    }
                }
            } );
        }
    }

    public void atualizarTotalGeral()
    {
        double total = 0.0;
        int colunaTotal = table.getColumnCount() - 2;

        for ( int i = 0; i < table.getRowCount(); i++ )
        {
            Object valor = table.getValueAt( i, colunaTotal );
            if ( valor != null && valor instanceof Number )
            {
                total += ( (Number) valor ).doubleValue();
            }
            else
            {
                try
                {
                    total += Double.parseDouble( valor.toString() );
                }
                catch ( NumberFormatException e )
                {
                    // Ignorar células inválidas
                }
            }
        }

        lbTotalPagar.setText( "Total da Factura: " + String.format( "%.2f", total ) );
    }

    public void atualizarTotalDescontos()
    {
        double total = 0.0;
        int colunaTotal = table.getColumnCount() - 5;

        for ( int i = 0; i < table.getRowCount(); i++ )
        {
            Object valor = table.getValueAt( i, colunaTotal );
            if ( valor != null && valor instanceof Number )
            {
                total += ( (Number) valor ).doubleValue();
            }
            else
            {
                try
                {
                    total += Double.parseDouble( valor.toString() );
                }
                catch ( NumberFormatException e )
                {
                    // Ignorar células inválidas
                }
            }
        }

        lbDesconto.setText( "Total Descontos: " + String.format( "%.2f", total ) );
    }

    public void atualizarTotalEntregue()
    {
        double total = 0.0;
        int colunaTotal = table.getColumnCount() - 2;

        for ( int i = 0; i < table.getRowCount(); i++ )
        {
            Object valor = table.getValueAt( i, colunaTotal );
            if ( valor != null && valor instanceof Number )
            {
                total += ( (Number) valor ).doubleValue();
            }
            else
            {
                try
                {
                    total += Double.parseDouble( valor.toString() );
                }
                catch ( NumberFormatException e )
                {
                    // Ignorar células inválidas
                }
            }
        }

        lbValorEnregue.setText( "Total Entregue: " + String.format( "%.2f", total ) );
    }

//    private void procedimento_busca()
//    {
//        //@1. Inserir a referência
//        String ref_doc = txtRefDoc.getText();
//
//        if ( ref_doc.isEmpty() )
//        {
//            JOptionPane.showMessageDialog( null, "Por favor insira a refrência da factura ", "AVISO", JOptionPane.WARNING_MESSAGE );
//            return;
//        }
//
//        //verifica se já existe recibo 
//        if ( vendaDao.existe_codFact( ref_doc ) )
//        {
//            txtRefDoc.setText( "" );
//            txtRefDoc.requestFocus();
//            procedimento_limpar_dados();
//            JOptionPane.showMessageDialog( null, "Atenção: não existe factura relacionada a esta referência.", "AVISO", JOptionPane.WARNING_MESSAGE );
//            return;
//        }
//
//        TbVenda venda_local = vendaDao.findByCodFact( ref_doc );
//        if ( venda_local == null )
//        {
//            procedimento_limpar_dados();
//            JOptionPane.showMessageDialog( null, "Não existe factura com esta referência", "AVISO", JOptionPane.WARNING_MESSAGE );
//            return;
//        }
//        
//        
//
//        // Exemplo para validação temporal, aqui você pode ajustar a regra conforme precisa
//        if ( true )
//        { // Troque pelo seu critério de validação
//            if ( "false".equals( venda_local.getStatusEliminado() ) )
//            {
//
//                List<TbItemVenda> linhas = venda_local.getTbItemVendaList();
//                fonteItens = linhas;
//
//                String[] colunas =
//                {
//                    "Activo", "Cod.Art", "Descrição", "Preço", "Qtd", "Desconto", "IVA", "Valor", "Valor(IVA)", "Co.Pag"
//                };
//
//                DefaultTableModel modelo = new DefaultTableModel( null, colunas )
//                {
//                    @Override
//                    public boolean isCellEditable( int row, int column )
//                    {
//                        if ( column == 0 )
//                        {
//                            return true; // Checkbox sempre editável
//                        }
//                        if ( column == 8 && Boolean.TRUE.equals( getValueAt( row, 0 ) ) )
//                        {
//                            return true; // Coluna 8 editável se checkbox marcada
//                        }
//                        return false;
//                    }
//
//                    @Override
//                    public Class<?> getColumnClass( int columnIndex )
//                    {
//                        if ( columnIndex == 0 )
//                        {
//                            return Boolean.class;
//                        }
//                        if ( columnIndex == 4 || columnIndex == 5 || columnIndex == 6 || columnIndex == 7 || columnIndex == 8 )
//                        {
//                            return Double.class;
//                        }
//                        return String.class;
//                    }
//                };
//
//                table.setModel( modelo );
//                table.getColumnModel().getColumn( 8 ).setCellEditor( new NumberCellEditor() );
//                table.getColumnModel().getColumn( 8 ).setCellRenderer( new HighlightAndBoldCellRenderer() );
////                table.getColumnModel().getColumn(8).setCellRenderer(new BoldCellRenderer());
//
//                // Preencher dados na tabela
//                for ( TbItemVenda obj : linhas )
//                {
//                    double valorIliquido = obj.getFkPreco().getPrecoVenda().doubleValue() * obj.getQuantidade();
//                    double valorDesconto = valorIliquido * ( obj.getDesconto() / 100 );
//
//                    modelo.addRow( new Object[]
//                    {
//                        false,
//                        obj.getCodigoProduto().getCodigo(),
//                        obj.getCodigoProduto().getDesignacao(),
//                        obj.getFkPreco().getPrecoVenda().doubleValue(),
//                        obj.getQuantidade(),
//                        obj.getDesconto(),
//                        obj.getValorIva(),
//                        valorIliquido - valorDesconto,
//                        obj.getTotal(),
//                        obj.getPercentagemCliente()
//                    } );
//                    
//                }
//                
//                for (int i = 0; i < modelo.getRowCount(); i++) {
//    modelo.setValueAt(true, i, 0); // Coluna 0 = checkbox
//}
//
//                // Atualizar labels da interface
//                atualizarTotalGeral();
//                atualizarTotalDescontos();
//                atualizarTotalEntregue();
////                lbTotalPagar.setText( String.valueOf(venda_local.getTotalVenda() ));
//                System.out.println( "Cheguei aqui no Atualizar Totais!!!" );
////                lbDesconto.setText( "Desconto: " + CfMethods.formatarComoMoeda( venda_local.getDescontoTotal() ) );
////                lbValorEnregue.setText( "Valor Entregue: " + CfMethods.formatarComoMoeda( venda_local.getValorEntregue() ) );
//                lbTroco.setText( "Troco: " + CfMethods.formatarComoMoeda( venda_local.getTroco() ) );
//
//                lbUsuario.setText( "Usuário: " + venda_local.getCodigoUsuario().getNome() );
//                jLabelSeguradora.setText( venda_local.getCodigoCliente().getFkEntidadeSeguradora().getNome() );
//                jLabelOBS.setText( venda_local.getObs() );
//                lbData.setText( "Data: " + MetodosUtil.getDataBanco( venda_local.getDataVenda() ) );
//
//                lbCliente.setText( "Cliente: " + getNomeCliente( venda_local ) );
//
//                // Configurar listener para checkbox e edição da coluna 8
//                configurarListenerCheckbox();
//
//                // Aplicar renderizador para pintar coluna 8 quando checkbox marcada
//                aplicarRenderizadorPersonalizado();
//
//                // Atualizar totais iniciais
//                actualizarTotais();
//                
//                actualizarTotaisDecontoComercial();
//
//            }
//            else
//            {
//                procedimento_limpar_dados();
//                MetodosUtil.showMessageUtil( "Atenção: esta factura já foi emitida\n a Nota de Crédito.", TIPO_MENSAGEM_AVISO );
//            }
//        }
//        else
//        {
//            JOptionPane.showMessageDialog( null, "Atenção: o documento a ser anulado deve estar no mesmo período do mês em curso.", "Aviso", JOptionPane.WARNING_MESSAGE );
//        }
//    }
    private void procedimento_busca()
    {
        String ref_doc = txtRefDoc.getText();

        if ( ref_doc.isEmpty() )
        {
            JOptionPane.showMessageDialog( null, "Por favor insira a refrência da factura", "AVISO", JOptionPane.WARNING_MESSAGE );
            return;
        }

        if ( vendaDao.existe_codFact( ref_doc ) )
        {
            txtRefDoc.setText( "" );
            txtRefDoc.requestFocus();
            procedimento_limpar_dados();
            JOptionPane.showMessageDialog( null, "Atenção: não existe factura relacionada a esta referência.", "AVISO", JOptionPane.WARNING_MESSAGE );
            return;
        }

        TbVenda venda_local = vendaDao.findByCodFact( ref_doc );
        if ( venda_local == null )
        {
            procedimento_limpar_dados();
            JOptionPane.showMessageDialog( null, "Não existe factura com esta referência", "AVISO", JOptionPane.WARNING_MESSAGE );
            return;
        }

        if ( "false".equals( venda_local.getStatusEliminado() ) )
        {
            List<TbItemVenda> linhas = venda_local.getTbItemVendaList();
            fonteItens = linhas;

            String[] colunas =
            {
                "Activo", "Cod.Art", "Descrição", "Preço", "Qtd", "Desconto", "IVA", "Valor", "Valor(IVA)", "Co.Pag"
            };

            DefaultTableModel modelo = new DefaultTableModel( null, colunas )
            {
                @Override
public boolean isCellEditable(int row, int column) {
    if (column == 0) return true;
    if (column == 8 && Boolean.TRUE.equals(getValueAt(row, 0))) return true;
    return false;
}

                @Override
                public Class<?> getColumnClass( int columnIndex )
                {
                    if ( columnIndex == 0 )
                    {
                        return Boolean.class;
                    }
                    if ( columnIndex >= 4 && columnIndex <= 8 )
                    {
                        return Double.class;
                    }
                    return String.class;
                }
            };

            table.setModel( modelo );
            modelo.addTableModelListener(e -> {
    if (e.getType() == TableModelEvent.UPDATE) {
        // Atualiza totais quando alguma célula for editada
//        atualizarTotalGeral();
//        atualizarTotalDescontos();
//        atualizarTotalEntregue();
        actualizarTotais();
        actualizarTotaisDecontoComercial();
    }
});
            
table.getModel().addTableModelListener(e -> {
    if (e.getType() == TableModelEvent.UPDATE) {
        int col = e.getColumn();
        int penultimaColuna = table.getColumnCount() - 2;
        if (col == penultimaColuna || col == TableModelEvent.ALL_COLUMNS) {
//            atualizarTotalGeral();
//            atualizarTotalDescontos();
//            atualizarTotalEntregue();
        }
    }
});
            table.getColumnModel().getColumn(8).setCellEditor(new NumberCellEditor(() -> {
//    atualizarTotalGeral();
//    atualizarTotalDescontos();
//    atualizarTotalEntregue();
}));
            table.getColumnModel().getColumn(8).setCellRenderer(new HighlightAndBoldCellRenderer());
            
            
            
table.addMouseListener(new java.awt.event.MouseAdapter() {
    @Override
    public void mouseClicked(java.awt.event.MouseEvent evt) {
        int row = table.rowAtPoint(evt.getPoint());
        int col = table.columnAtPoint(evt.getPoint());

        if (col == 0) {
            Boolean checked = (Boolean) table.getValueAt(row, 0);
            if (Boolean.TRUE.equals(checked)) {
                table.requestFocus();
                table.changeSelection(row, 8, false, false);
                table.editCellAt(row, 8);
            }
            // Atualizar totais ao clicar na checkbox
//            atualizarTotalGeral();
//            atualizarTotalDescontos();
//            atualizarTotalEntregue();
        }
    }
});

// Atualizar totais quando a célula da coluna 8 for editada
table.getModel().addTableModelListener(e -> {
    if (e.getType() == TableModelEvent.UPDATE) {
        int col = e.getColumn();
        int penultimaColuna = table.getColumnCount() - 2;
        if (col == penultimaColuna) {
//            atualizarTotalGeral();
        }
    }
});
            for ( TbItemVenda obj : linhas )
            {
                double valorIliquido = obj.getFkPreco().getPrecoVenda().doubleValue() * obj.getQuantidade();
                double valorDesconto = valorIliquido * ( obj.getDesconto() / 100 );

                modelo.addRow( new Object[]
                {
                    false,
                    obj.getCodigoProduto().getCodigo(),
                    obj.getCodigoProduto().getDesignacao(),
                    obj.getFkPreco().getPrecoVenda().doubleValue(),
                    obj.getQuantidade(),
                    obj.getDesconto(),
                    obj.getValorIva(),
                    valorIliquido - valorDesconto,
                    obj.getTotal()
//                    obj.getPercentagemCliente()
                } );
            }

            // Atualizar labels
            atualizarTotalGeral();
            atualizarTotalDescontos();
            atualizarTotalEntregue();
            lbTroco.setText( "Troco: " + CfMethods.formatarComoMoeda( venda_local.getTroco() ) );
            lbUsuario.setText( "Usuário: " + venda_local.getCodigoUsuario().getNome() );
//            jLabelSeguradora.setText( venda_local.getCodigoCliente().getFkEntidadeSeguradora().getNome() );
            jLabelOBS.setText( venda_local.getObs() );
            lbData.setText( "Data: " + MetodosUtil.getDataBanco( venda_local.getDataVenda() ) );
            lbCliente.setText( "Cliente: " + getNomeCliente( venda_local ) );

            // Listeners e renderizador
            configurarListenerCheckbox();
//            aplicarRenderizadorPersonalizado();

            // Totais iniciais
            actualizarTotais();
            actualizarTotaisDecontoComercial();

        }
        else
        {
            procedimento_limpar_dados();
            MetodosUtil.showMessageUtil( "Atenção: esta factura já foi emitida\n a Nota de Crédito.", TIPO_MENSAGEM_AVISO );
        }
    }

public class NumberCellEditor extends DefaultCellEditor {

    public NumberCellEditor(Runnable atualizarTotaisCallback) {
        super(new JFormattedTextField());

        JFormattedTextField textField = (JFormattedTextField) getComponent();
        NumberFormat format = NumberFormat.getNumberInstance();
        format.setGroupingUsed(false); // sem separador de milhar

        textField.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(format)));
        textField.setHorizontalAlignment(JTextField.RIGHT);
        textField.setFocusLostBehavior(JFormattedTextField.PERSIST);

        // Permitir apenas números e ponto
        ((AbstractDocument) textField.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (string.matches("[0-9.]*")) {
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String string, AttributeSet attrs) throws BadLocationException {
                if (string.matches("[0-9.]*")) {
                    super.replace(fb, offset, length, string, attrs);
                }
            }
        });

        // ⏎ Enter para confirmar edição e atualizar totais
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    stopCellEditing(); // encerra a edição da célula
                    if (atualizarTotaisCallback != null) {
                        atualizarTotaisCallback.run(); // chama o método de totais
                    }
                }
            }
        });
    }
}

    private void preencherTotais( TbVenda venda )
    {
        lbTotalPagar.setText( "Total Iliquido: " + CfMethods.formatarComoMoeda( venda.getTotalVenda() ) );
        lbDesconto.setText( "Desconto: " + CfMethods.formatarComoMoeda( venda.getDescontoTotal() ) );
        lbValorEnregue.setText( "Valor Entregue: " + CfMethods.formatarComoMoeda( venda.getValorEntregue() ) );
        lbTroco.setText( "Troco: " + CfMethods.formatarComoMoeda( venda.getTroco() ) );
    }

    private void preencherCabecalhoVenda( TbVenda venda )
    {
        lbUsuario.setText( "Usuário: " + venda.getCodigoUsuario().getNome() );
//        jLabelSeguradora.setText( venda.getCodigoCliente().getFkEntidadeSeguradora().getNome() );
        jLabelOBS.setText( venda.getObs() );
        lbData.setText( "Data: " + MetodosUtil.getDataBanco( venda.getDataVenda() ) );
        lbCliente.setText( "Cliente: " + getNomeCliente( venda ) );
    }

    private void limparDadosComAviso( String mensagem )
    {
        procedimento_limpar_dados();
        JOptionPane.showMessageDialog( null, mensagem, "AVISO", JOptionPane.WARNING_MESSAGE );
    }

//private void carregarTabelaComVenda(TbVenda venda) {
//    List<TbItemVenda> linhas = venda.getTbItemVendaList();
//    fonteItens = linhas;
//
//    String[] colunas = {
//        "Seleccionar", "Cod.Art", "Descrição", "Preço", "Qtd", "Desconto", "IVA", "Valor", "Valor(IVA)", "Co.Pag"
//    };
//
//    DefaultTableModel modelo = new DefaultTableModel(null, colunas) {
//        @Override
//        public boolean isCellEditable(int row, int column) {
//            if (column == 0) return true;
//            return column == 8 && Boolean.TRUE.equals(getValueAt(row, 0));
//        }
//
//        @Override
//        public Class<?> getColumnClass(int columnIndex) {
//            return switch (columnIndex) {
//                case 0 -> Boolean.class;
//                case 3, 4, 5, 6, 7, 8 -> Double.class;
//                default -> String.class;
//            };
//        }
//    };
//
//    table.setModel(modelo);
//    modelo.setRowCount(0);
//
//    for (TbItemVenda item : linhas) {
//        double preco = item.getFkPreco().getPrecoVenda().doubleValue();
//        double quantidade = item.getQuantidade();
//        double valorIliquido = preco * quantidade;
//        double valorDesconto = valorIliquido * (item.getDesconto() / 100);
//
//        modelo.addRow(new Object[]{
//            false,
//            item.getCodigoProduto().getCodigo(),
//            item.getCodigoProduto().getDesignacao(),
//            preco,
//            quantidade,
//            item.getDesconto(),
//            item.getValorIva(),
//            valorIliquido - valorDesconto,
//            item.getTotal(),
//            item.getPercentagemCliente()
//        });
//    }
//}
    private void actualizarTotais()
    {
        double total = 0.0;

        for ( int i = 0; i < table.getRowCount(); i++ )
        {
            Boolean selecionado = (Boolean) table.getValueAt( i, 0 );
            if ( Boolean.TRUE.equals( selecionado ) )
            {
                Object valorObj = table.getValueAt( i, 8 );

                double valorDouble = 0.0;
                if ( valorObj instanceof Number )
                {
                    valorDouble = ( (Number) valorObj ).doubleValue();
                }
                else if ( valorObj instanceof String )
                {
                    // Remove símbolos, formata e tenta converter
                    String strValor = ( (String) valorObj ).replaceAll( "[^0-9,\\.]", "" ).replace( ",", "." );
                    try
                    {
                        valorDouble = Double.parseDouble( strValor );
                    }
                    catch ( NumberFormatException e )
                    {
                        valorDouble = 0.0; // ou trate o erro como quiser
                    }
                }

                total += valorDouble;
            }
        }

        jLabelTotal.setText( CfMethods.formatarComoMoeda( total ) );
        jLabelTotalSemDesconto.setText( CfMethods.formatarComoMoeda( total ) );
//        lbTotalPagar.setText( "Total Selecionado: " + CfMethods.formatarComoMoeda( total ) );
    }

    private void actualizarTotaisDecontoComercial()
    {
        double total = 0.0;

        for ( int i = 0; i < table.getRowCount(); i++ )
        {
            Boolean selecionado = (Boolean) table.getValueAt( i, 0 );
            if ( Boolean.TRUE.equals( selecionado ) )
            {
                Object valorObj = table.getValueAt( i, 5 );

                double valorDouble = 0.0;
                if ( valorObj instanceof Number )
                {
                    valorDouble = ( (Number) valorObj ).doubleValue();
                }
                else if ( valorObj instanceof String )
                {
                    // Remove símbolos, formata e tenta converter
                    String strValor = ( (String) valorObj ).replaceAll( "[^0-9,\\.]", "" ).replace( ",", "." );
                    try
                    {
                        valorDouble = Double.parseDouble( strValor );
                    }
                    catch ( NumberFormatException e )
                    {
                        valorDouble = 0.0; // ou trate o erro como quiser
                    }
                }

                total += valorDouble;
            }
        }

        jLabelTotalDesconto.setText( CfMethods.formatarComoMoeda( total ) );
        jLabelTotalDescontoFinanceiro.setText( CfMethods.formatarComoMoeda( total ) );
        jLabelIVA.setText( CfMethods.formatarComoMoeda( total ) );

//        lbTotalPagar.setText( "Total Selecionado: " + CfMethods.formatarComoMoeda( total ) );
    }

    private double toDouble( Object value )
    {
        if ( value instanceof BigDecimal )
        {
            return ( (BigDecimal) value ).doubleValue();
        }
        else if ( value instanceof Number )
        {
            return ( (Number) value ).doubleValue();
        }
        else
        {
            try
            {
                return Double.parseDouble( value.toString() );
            }
            catch ( Exception e )
            {
                return 0.0;
            }
        }
    }

public class HighlightAndBoldCellRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(
        JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column
    ) {
        Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        // Reset visual
        comp.setFont(table.getFont());
        comp.setForeground(Color.BLACK);

        // Pintar apenas se a checkbox estiver marcada e for coluna 8
        boolean isMarcado = Boolean.TRUE.equals(table.getValueAt(row, 0));
        if (column == 8 && isMarcado) {
            comp.setBackground(new Color(255, 200, 200)); // vermelho claro
            comp.setFont(comp.getFont().deriveFont(Font.BOLD));
        } else {
            if (isSelected) {
                comp.setBackground(table.getSelectionBackground());
                comp.setForeground(table.getSelectionForeground());
            } else {
                comp.setBackground(Color.WHITE);
            }
        }

        return comp;
    }
}
    private void configurarListenerCheckbox1()
    {
        table.addPropertyChangeListener( evt ->
        {
            if ( "tableCellEditor".equals( evt.getPropertyName() ) )
            {
                int linha = table.getSelectedRow();
                int coluna = table.getSelectedColumn();

                if ( coluna == 0 && linha >= 0 )
                {
                    Boolean estado = (Boolean) table.getValueAt( linha, 0 );
                    ( (DefaultTableModel) table.getModel() ).fireTableRowsUpdated( linha, linha );
                    actualizarTotais();
                }
            }
        } );
    }

    private void procedimento_limpar_dados()
    {
        limpar();
        esvaziar_tabela( table );
    }

    public static void setTotalPagar()
    {

        DefaultTableModel modelo = (DefaultTableModel) table.getModel();
        double total_pagar = 0;
        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {
            total_pagar += CfMethods.parseMoedaFormatada( modelo.getValueAt( i, 7 ).toString() );

        }
        lbTotalPagar.setText( "Total Iliquido: " + CfMethods.formatarComoMoeda( total_pagar ) );

    }

    public static void setTotalDesconto()
    {

        DefaultTableModel modelo = (DefaultTableModel) table.getModel();
        double preco = 0;
        double total_desconto = 0;
        double taxa_desconto = 0;
//        preco = 0;
//        taxa_desconto = 0;
//        total_desconto = 0;
        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {
            preco += CfMethods.parseMoedaFormatada( modelo.getValueAt( i, 2 ).toString() );
            taxa_desconto += Double.parseDouble( modelo.getValueAt( i, 4 ).toString() );
            total_desconto = preco * ( taxa_desconto / 100 );
//            total_desconto += CfMethods.parseMoedaFormatada( modelo.getValueAt( i, 4 ).toString() );

        }
        lbDesconto.setText( "Desconto: " + CfMethods.formatarComoMoeda( total_desconto ) );

    }

    public static void setTotalEntregue()
    {

        DefaultTableModel modelo = (DefaultTableModel) table.getModel();
        double total_pagar = 0;
        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {
            total_pagar += CfMethods.parseMoedaFormatada( modelo.getValueAt( i, 7 ).toString() );

        }
        lbValorEnregue.setText( "Valor Entregue: " + CfMethods.formatarComoMoeda( total_pagar ) );

    }

    public static void setTotalTroco()
    {

        DefaultTableModel modelo = (DefaultTableModel) table.getModel();
        double total_pagar = 0;
        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {
            total_pagar += 0;

        }
        lbTroco.setText( "Troco: " + CfMethods.formatarComoMoeda( total_pagar ) );

    }

    private String getNomeCliente( TbVenda venda_local )
    {

        if ( venda_local.getCodigoCliente().getNome().equals( DVML._CLIENTE_CONSUMIDOR_FINAL ) )
        {
            String nome_cliente_consumidor_final = !venda_local.getNomeConsumidorFinal().equals( "" ) ? " (" + venda_local.getNomeConsumidorFinal() + ")" : "";
            return venda_local.getNomeCliente() + nome_cliente_consumidor_final;
        }
        return venda_local.getCodigoCliente().getNome();
    }

    private void eliminar_amortizacoes( Integer codigo_documento )
    {
        AmortizacaoDividaDao.eliminarAmortizacaoByCodVenda( codigo_documento, conexao );
    }

//    private void tablePropertyChanges( java.beans.PropertyChangeEvent evt )
//    {
////        actualizarPreco( evt );
//
//        if ( table.getSelectedColumn() == 0 )
//        {
//
//            try
//            {
//                DefaultTableModel modelo = (DefaultTableModel) table.getModel();
//                int linhaSelecionada = table.getSelectedRow();
//                System.out.println( " Cheguei aqui property" );
//                boolean estado = (Boolean) modelo.getValueAt( linhaSelecionada, 0 );
//                modelo.setValueAt( estado, linhaSelecionada, 0 );
////                double totalServico = CfMethods.parseMoedaFormatada( modelo.getValueAt( linhaSelecionada, 8 ).toString() );
//
//                actualizarTotais();
//            }
//            catch ( Exception e )
//            {
//                e.printStackTrace();
//            }
//
//        }
//    }
//    private static void actualizarTotais()
//    {
//
//        DefaultTableModel modelo = (DefaultTableModel) table.getModel();
//        int linhaSelecionada = table.getSelectedRow();
//        System.out.println( " Cheguei aqui property" );
//
//        double totalServico = 0;
//        double totalDesconto = 0;
//        double totalDescontoFincanceiro = 0;
//        double totalIva = 0;
//        double totalIliquido = 0;
//
//        for ( int i = 0; i < modelo.getRowCount(); i++ )
//        {
//            boolean estado = (Boolean) modelo.getValueAt( i, 0 );
//            System.out.println( "Valor doi Estado: " + estado );
//
//            if ( estado == true )
//            {
//
//                double preco = CfMethods.parseMoedaFormatada( modelo.getValueAt( i, 3 ).toString() );
//                double qtd = Double.parseDouble( modelo.getValueAt( i, 4 ).toString() );
//                double desconto = Double.parseDouble( modelo.getValueAt( i, 5 ).toString() );
//                double coPagamento = Double.parseDouble( modelo.getValueAt( i, 9 ).toString() );
//
//                double valorIliquido = preco * qtd;
//
//                double valorTotal = CfMethods.parseMoedaFormatada( modelo.getValueAt( i, 8 ).toString() );
//                double valorIva = Double.parseDouble( modelo.getValueAt( i, 6 ).toString() );
//                double descontoComercial = (valorIliquido * ( desconto / 100 ));
//
//                totalIliquido += valorIliquido;
//                totalServico += valorTotal - descontoComercial - coPagamento;
//                totalIva += valorIva;
//                totalDesconto += descontoComercial;
//                totalDescontoFincanceiro += coPagamento;
//            }
//
//            try
//            {
//                System.out.println( "Total Linha: " + totalServico );
//            }
//            catch ( NumberFormatException e )
//            {
//                e.printStackTrace();
//
//            }
//
//        }
//
//        jLabelTotalSemDesconto.setText( CfMethods.formatarComoMoeda( totalIliquido ) );
//        jLabelTotal.setText( CfMethods.formatarComoMoeda( totalServico ) );
//        jLabelIVA.setText( CfMethods.formatarComoMoeda( totalIva ) );
//        jLabelTotalDesconto.setText( CfMethods.formatarComoMoeda( totalDesconto ) );
//        jLabelTotalDescontoFinanceiro.setText( CfMethods.formatarComoMoeda( totalDescontoFincanceiro ) );
//
//    }
    private TbPreco getPrecoByCodPoroduto( int idProduto )
    {

        if ( !fonteItens.isEmpty() )
        {

            for ( TbItemVenda item : fonteItens )
            {

                if ( item.getCodigoProduto().getCodigo() == idProduto )
                {
                    return item.getFkPreco();
                }
            }
        }

        return null;
    }

}
