/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package visao;

import comercial.controller.AnoEconomicoController;
import comercial.controller.ArmazensController;
import comercial.controller.ComprasController;
import comercial.controller.DocumentosController;
import comercial.controller.FornecedoresController;
import comercial.controller.ItemComprasController;
import comercial.controller.LugaresController;
import comercial.controller.PrecosController;
import comercial.controller.ProdutosController;
import comercial.controller.StoksController;
import comercial.controller.UsuariosController;
import entity.*;
import comercial.controller.ExtratoContaFornecedorController;
import comercial.controller.UnidadesController;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.io.File;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import static kitanda.util.CfConstantes.YYYYMMDD_HHMMSS;
import kitanda.util.CfMethods;
import kitanda.util.CfMethodsSwing;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;
import util.BDConexao;
import util.DVML;
import static util.DVML.CAMINHO_REPORT;
import util.FinanceUtils;
import util.MetodosUtil;
import static util.MetodosUtil.getMotivoIsensao;
import static util.MetodosUtil.rodarComandoWindows;
import util.PermitirNumeros;
import static visao.VendaUsuarioVisao.getCodigoArmazem;

/**
 *
 * @author Domingos Dala Vunge
 */
public class ConverterEncomendasCompraVisao extends javax.swing.JFrame implements Runnable
{

    private ItemCompras itemCompras;
    private static PrecosController precosController;
    private static LugaresController lugaresController;
    private static ProdutosController produtosController;
    private static StoksController stocksController;
    private static ArmazensController armazensController;
    private static FornecedoresController fornecedoresController;
    private static AnoEconomicoController anoEconomicoController;
    private static DocumentosController documentosController;
    private static ComprasController comprasController;
    private static ItemComprasController itemComprasController;
    private static UsuariosController usuariosController;
    private static UnidadesController unidadesController;
    private static TbFornecedor fornecedor;
    private static TbArmazem armazem;
    private static TbStock stock_local;
    private static Compras compra;
    private static TbPreco preco;
    private static TbProduto produto;
    private static TbUsuario usuario;
    private static AnoEconomico anoEconomico;
    private static BDConexao conexao;
    private static BDConexao conexaoTransaction;
    private static Documento documento;
    private static TbStock stock;
    private int cod_usuario;
    private static int linha = 0, coordenada = 1, doc_prox_cod = 0;
    private double soma_total = 0;
    private static double total_iva = 0;
    private static DefaultListModel lista_model_compras_apuradas = new DefaultListModel();
    private static double total_iliquido = 0, total_desconto_linha = 0;
    private boolean aviso_continuar_documento = false;
    public static int linha_actual = -1;
    private Thread t;
    private static String prox_doc;
    private static List<ItemCompras> listaItensCompras = new ArrayList<>();
    private static String codFactEncomendas;

    public ConverterEncomendasCompraVisao( int cod_usuario, BDConexao conexao, String codFactEncomendas, List<ItemCompras> listaItensCompras )
    {

        initComponents();

        precosController = new PrecosController( conexao );
        lugaresController = new LugaresController( conexao );
        produtosController = new ProdutosController( conexao );
        stocksController = new StoksController( conexao );
        armazensController = new ArmazensController( conexao );
        fornecedoresController = new FornecedoresController( conexao );
        anoEconomicoController = new AnoEconomicoController( conexao );
        documentosController = new DocumentosController( conexao );
        comprasController = new ComprasController( conexao );
        itemComprasController = new ItemComprasController( conexao );
        usuariosController = new UsuariosController( conexao );
        unidadesController = new UnidadesController( conexao );
        ConverterEncomendasCompraVisao.listaItensCompras = listaItensCompras;
        ConverterEncomendasCompraVisao.codFactEncomendas = codFactEncomendas;

        //confiLabel();
        setLocationRelativeTo( null );
        setResizable( false );
        this.cod_usuario = cod_usuario;
        ConverterEncomendasCompraVisao.conexao = conexao;

        init();
        initJDialogue();
        //DESATIVAR COMVERTER COMPRAS
//        jButton1.setVisible( false );
        setWindowsListener();

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
//                                cmbArmazem.setModel(new DefaultComboBoxModel(armazemDao.buscaTodos1()));
//                                cmbArmazem.setModel(new DefaultComboBoxModel(armazemDao.buscaTodos1()));
//                                new BuscaProdutoVisao( getInstance(), rootPaneCheckingEnabled, getIdArmazens(), DVML.JANELA_COMPRA, conexao, ConverterEncomendasCompraVisao.class ).setVisible(true);
                                new BuscaProdutoVisao( getInstance(), rootPaneCheckingEnabled, getIdArmazens(), DVML.JANELA_COMPRA, BDConexao.getInstancia()).setVisible(true);
                            }
                            catch ( Exception ex )
                            {
                                ex.printStackTrace();
                            }
                            return true;

                        }
                        return false;
                    }
                } );

        adicionar_tabela();
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

        jMenuItem1 = new javax.swing.JMenuItem();
        buttonGroup3 = new javax.swing.ButtonGroup();
        produtoPesquisaJDialog = new javax.swing.JDialog();
        jPanel4 = new javax.swing.JPanel();
        txtDesignacao = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        tabela_busca = new javax.swing.JTable();
        jButton6 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableCompra = new javax.swing.JTable();
        jPanel6 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        lb_proximo_documento = new javax.swing.JLabel();
        lbPreco1 = new javax.swing.JLabel();
        cmbArmazem = new javax.swing.JComboBox();
        jPanel3 = new javax.swing.JPanel();
        ld_data_vencimento = new javax.swing.JLabel();
        lb_fornecedor_nome = new javax.swing.JLabel();
        lb_fornecedor_endereco = new javax.swing.JLabel();
        lb_fornecedor_telefone = new javax.swing.JLabel();
        lb_fornecedor_email = new javax.swing.JLabel();
        cmbAnoEconomico = new javax.swing.JComboBox<>();
        cmbTipoDocumento = new javax.swing.JComboBox<>();
        lb_ano_encomico1 = new javax.swing.JLabel();
        lb_ano_encomico2 = new javax.swing.JLabel();
        txtNumeroDocumento = new javax.swing.JTextField();
        lb_ano_encomico3 = new javax.swing.JLabel();
        dcDataVencimento = new com.toedter.calendar.JDateChooser();
        dcDataDocumento = new com.toedter.calendar.JDateChooser();
        lb_data_documento = new javax.swing.JLabel();
        cmbVossoDocumento = new javax.swing.JComboBox<>();
        lb_fornecedor_nif = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        lbTotalPagar = new javax.swing.JLabel();
        txtTotal_AOA_Iliquido = new javax.swing.JTextField();
        lbTotalPagar1 = new javax.swing.JLabel();
        lbTotalPagar2 = new javax.swing.JLabel();
        txtTotal_AOA_liquido = new javax.swing.JTextField();
        lbTotalPagar3 = new javax.swing.JLabel();
        txtTotal_AOA_IVA = new javax.swing.JTextField();
        txtTotal_AOA_Desconto = new javax.swing.JTextField();
        jButton4 = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        lb_usuario = new javax.swing.JLabel();
        lbValorPorExtenco = new javax.swing.JLabel();
        lbLegenda = new javax.swing.JLabel();
        lb_data_apuramento = new javax.swing.JLabel();
        lb_data_solicitacao = new javax.swing.JLabel();
        lb_numero_artigos = new javax.swing.JLabel();

        jMenuItem1.setText("jMenuItem1");

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        tabela_busca.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "Cod.", "Designacao", "Categoria", "Qtd"
            }
        )
        {
            Class[] types = new Class []
            {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Integer.class
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
        tabela_busca.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                tabela_buscaMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tabela_busca);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 893, Short.MAX_VALUE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(txtDesignacao, javax.swing.GroupLayout.PREFERRED_SIZE, 294, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtDesignacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 399, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/LOGOUT - VERMELHO/Logout 32x32.png"))); // NOI18N
        jButton6.setText("Sair");
        jButton6.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButton6ActionPerformed(evt);
            }
        });

        jLabel1.setText("BUSCA DE PRODUTO");

        javax.swing.GroupLayout produtoPesquisaJDialogLayout = new javax.swing.GroupLayout(produtoPesquisaJDialog.getContentPane());
        produtoPesquisaJDialog.getContentPane().setLayout(produtoPesquisaJDialogLayout);
        produtoPesquisaJDialogLayout.setHorizontalGroup(
            produtoPesquisaJDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(produtoPesquisaJDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(produtoPesquisaJDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(produtoPesquisaJDialogLayout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton6)))
                .addContainerGap())
        );
        produtoPesquisaJDialogLayout.setVerticalGroup(
            produtoPesquisaJDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, produtoPesquisaJDialogLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(produtoPesquisaJDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 450, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("EMISSÃO DE COMPRAS");

        jPanel2.setBackground(new java.awt.Color(204, 204, 204));
        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tableCompra.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        tableCompra.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "", "Cod.Art", "Descrição", "Preço", "Qtd.", "Unidade", "Desconto(%)", "Taxa ", "Valor", "Valor C/ Imposto", "Critica", "Baixa"
            }
        )
        {
            Class[] types = new Class []
            {
                java.lang.Boolean.class, java.lang.Integer.class, java.lang.String.class, java.lang.Double.class, java.lang.Double.class, java.lang.String.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean []
            {
                true, false, false, true, true, false, true, true, false, false, true, true
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
        tableCompra.setCellSelectionEnabled(true);
        tableCompra.setGridColor(new java.awt.Color(51, 153, 0));
        tableCompra.addPropertyChangeListener(new java.beans.PropertyChangeListener()
        {
            public void propertyChange(java.beans.PropertyChangeEvent evt)
            {
                tableCompraPropertyChange(evt);
            }
        });
        jScrollPane1.setViewportView(tableCompra);
        tableCompra.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        if (tableCompra.getColumnModel().getColumnCount() > 0)
        {
            tableCompra.getColumnModel().getColumn(0).setPreferredWidth(10);
            tableCompra.getColumnModel().getColumn(1).setPreferredWidth(10);
            tableCompra.getColumnModel().getColumn(2).setPreferredWidth(240);
            tableCompra.getColumnModel().getColumn(4).setPreferredWidth(10);
            tableCompra.getColumnModel().getColumn(5).setPreferredWidth(20);
            tableCompra.getColumnModel().getColumn(7).setPreferredWidth(5);
            tableCompra.getColumnModel().getColumn(10).setResizable(false);
            tableCompra.getColumnModel().getColumn(10).setPreferredWidth(1);
            tableCompra.getColumnModel().getColumn(11).setPreferredWidth(1);
        }

        jPanel2.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 1020, 270));

        jPanel6.setBackground(new java.awt.Color(0, 0, 102));
        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("REGISTRO DE COMPRAS");

        lb_proximo_documento.setFont(new java.awt.Font("Lucida Grande", 1, 18)); // NOI18N
        lb_proximo_documento.setForeground(new java.awt.Color(255, 255, 255));
        lb_proximo_documento.setText("PRÓXIMO DOC: XX PP/A1");

        lbPreco1.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbPreco1.setForeground(new java.awt.Color(255, 255, 255));
        lbPreco1.setText("Local da Descarga");

        cmbArmazem.setBackground(new java.awt.Color(0, 0, 102));
        cmbArmazem.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 14)); // NOI18N
        cmbArmazem.setForeground(new java.awt.Color(255, 255, 255));
        cmbArmazem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbArmazemActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lb_proximo_documento, javax.swing.GroupLayout.PREFERRED_SIZE, 302, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 44, Short.MAX_VALUE)
                .addComponent(lbPreco1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbArmazem, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 22, Short.MAX_VALUE)
                    .addComponent(lb_proximo_documento, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lbPreco1)
                    .addComponent(cmbArmazem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        ld_data_vencimento.setFont(new java.awt.Font("Lucida Grande", 1, 12)); // NOI18N
        ld_data_vencimento.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        ld_data_vencimento.setText("Data de Vencimento:");
        jPanel3.add(ld_data_vencimento, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 130, 120, 30));

        lb_fornecedor_nome.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        lb_fornecedor_nome.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lb_fornecedor_nome.setText("Fornecedor");
        jPanel3.add(lb_fornecedor_nome, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 300, -1));

        lb_fornecedor_endereco.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        lb_fornecedor_endereco.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lb_fornecedor_endereco.setText("Endereço");
        jPanel3.add(lb_fornecedor_endereco, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 370, -1));

        lb_fornecedor_telefone.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        lb_fornecedor_telefone.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lb_fornecedor_telefone.setText("Telefone");
        jPanel3.add(lb_fornecedor_telefone, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 370, -1));

        lb_fornecedor_email.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        lb_fornecedor_email.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lb_fornecedor_email.setText("email");
        jPanel3.add(lb_fornecedor_email, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, 300, -1));

        cmbAnoEconomico.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbAnoEconomico.setEnabled(false);
        cmbAnoEconomico.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbAnoEconomicoActionPerformed(evt);
            }
        });
        jPanel3.add(cmbAnoEconomico, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 30, 160, 30));

        cmbTipoDocumento.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel3.add(cmbTipoDocumento, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 70, 230, 30));

        lb_ano_encomico1.setFont(new java.awt.Font("Lucida Grande", 1, 12)); // NOI18N
        lb_ano_encomico1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lb_ano_encomico1.setText("Ano Econômico");
        jPanel3.add(lb_ano_encomico1, new org.netbeans.lib.awtextra.AbsoluteConstraints(930, 0, -1, 30));

        lb_ano_encomico2.setFont(new java.awt.Font("Lucida Grande", 1, 12)); // NOI18N
        lb_ano_encomico2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lb_ano_encomico2.setText("Documento");
        jPanel3.add(lb_ano_encomico2, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 10, 110, 40));
        jPanel3.add(txtNumeroDocumento, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 50, 150, -1));

        lb_ano_encomico3.setFont(new java.awt.Font("Lucida Grande", 1, 12)); // NOI18N
        lb_ano_encomico3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lb_ano_encomico3.setText("N.º Documento *");
        jPanel3.add(lb_ano_encomico3, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 50, 120, 40));
        jPanel3.add(dcDataVencimento, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 130, -1, 30));
        jPanel3.add(dcDataDocumento, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 90, -1, 30));

        lb_data_documento.setFont(new java.awt.Font("Lucida Grande", 1, 12)); // NOI18N
        lb_data_documento.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lb_data_documento.setText("Data do Documento: *");
        jPanel3.add(lb_data_documento, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 90, 140, 30));

        cmbVossoDocumento.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "V/Factura", "V/Factura-Recibo" }));
        cmbVossoDocumento.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbVossoDocumentoActionPerformed(evt);
            }
        });
        jPanel3.add(cmbVossoDocumento, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 10, 190, -1));

        lb_fornecedor_nif.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        lb_fornecedor_nif.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lb_fornecedor_nif.setText("NIF");
        jPanel3.add(lb_fornecedor_nif, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 300, -1));

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel8.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lbTotalPagar.setFont(new java.awt.Font("Lucida Grande", 1, 12)); // NOI18N
        lbTotalPagar.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbTotalPagar.setText("Desconto :");
        jPanel8.add(lbTotalPagar, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 10, 130, 20));

        txtTotal_AOA_Iliquido.setEditable(false);
        txtTotal_AOA_Iliquido.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        txtTotal_AOA_Iliquido.setForeground(new java.awt.Color(255, 0, 0));
        txtTotal_AOA_Iliquido.setCaretColor(new java.awt.Color(255, 255, 255));
        txtTotal_AOA_Iliquido.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtTotal_AOA_IliquidoActionPerformed(evt);
            }
        });
        jPanel8.add(txtTotal_AOA_Iliquido, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 30, 280, 20));

        lbTotalPagar1.setFont(new java.awt.Font("Lucida Grande", 1, 12)); // NOI18N
        lbTotalPagar1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbTotalPagar1.setText("TOTAL LÍQUIDO :");
        jPanel8.add(lbTotalPagar1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, 170, 20));

        lbTotalPagar2.setFont(new java.awt.Font("Lucida Grande", 1, 12)); // NOI18N
        lbTotalPagar2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbTotalPagar2.setText("Total Iliquido :");
        jPanel8.add(lbTotalPagar2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 170, 20));

        txtTotal_AOA_liquido.setEditable(false);
        txtTotal_AOA_liquido.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        txtTotal_AOA_liquido.setForeground(new java.awt.Color(255, 0, 0));
        txtTotal_AOA_liquido.setCaretColor(new java.awt.Color(255, 255, 255));
        txtTotal_AOA_liquido.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtTotal_AOA_liquidoActionPerformed(evt);
            }
        });
        jPanel8.add(txtTotal_AOA_liquido, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 80, 280, 20));

        lbTotalPagar3.setFont(new java.awt.Font("Lucida Grande", 1, 12)); // NOI18N
        lbTotalPagar3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbTotalPagar3.setText("IVA :");
        jPanel8.add(lbTotalPagar3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 170, 20));

        txtTotal_AOA_IVA.setEditable(false);
        txtTotal_AOA_IVA.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        txtTotal_AOA_IVA.setForeground(new java.awt.Color(255, 0, 0));
        txtTotal_AOA_IVA.setCaretColor(new java.awt.Color(255, 255, 255));
        txtTotal_AOA_IVA.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtTotal_AOA_IVAActionPerformed(evt);
            }
        });
        jPanel8.add(txtTotal_AOA_IVA, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 50, 280, 20));

        txtTotal_AOA_Desconto.setEditable(false);
        txtTotal_AOA_Desconto.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        txtTotal_AOA_Desconto.setForeground(new java.awt.Color(255, 0, 0));
        txtTotal_AOA_Desconto.setCaretColor(new java.awt.Color(255, 255, 255));
        txtTotal_AOA_Desconto.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtTotal_AOA_DescontoActionPerformed(evt);
            }
        });
        jPanel8.add(txtTotal_AOA_Desconto, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 10, 280, 20));

        jButton4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/impressora1.png"))); // NOI18N
        jButton4.setText("Registrar Compra");
        jButton4.setToolTipText("Efectuar Venda");
        jButton4.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButton4ActionPerformed(evt);
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

        lb_usuario.setFont(new java.awt.Font("Lucida Grande", 1, 12)); // NOI18N
        lb_usuario.setText("Conta:");

        lbValorPorExtenco.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        lbValorPorExtenco.setForeground(new java.awt.Color(204, 0, 0));
        lbValorPorExtenco.setText("VALOR POR EXTENSO");

        lbLegenda.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbLegenda.setForeground(new java.awt.Color(204, 0, 0));
        lbLegenda.setText("LEGENDA");

        lb_data_apuramento.setFont(new java.awt.Font("Lucida Grande", 1, 12)); // NOI18N
        lb_data_apuramento.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lb_data_apuramento.setText("Data do Apuramento");

        lb_data_solicitacao.setFont(new java.awt.Font("Lucida Grande", 1, 12)); // NOI18N
        lb_data_solicitacao.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lb_data_solicitacao.setText("Data Solicitação");

        lb_numero_artigos.setFont(new java.awt.Font("Lucida Grande", 1, 12)); // NOI18N
        lb_numero_artigos.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lb_numero_artigos.setText("Nº de Artigos");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                    .addGap(3, 3, 3)
                                    .addComponent(lbLegenda, javax.swing.GroupLayout.PREFERRED_SIZE, 285, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(lbValorPorExtenco, javax.swing.GroupLayout.PREFERRED_SIZE, 359, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(lb_usuario, javax.swing.GroupLayout.PREFERRED_SIZE, 383, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(lb_data_solicitacao, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(lb_numero_artigos, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(lb_data_apuramento, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jButton4)
                                .addGap(1, 1, 1)
                                .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, 480, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 284, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addComponent(lb_data_apuramento))
                            .addComponent(lb_numero_artigos))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lb_data_solicitacao))
                    .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lbLegenda, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lbValorPorExtenco))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lb_usuario, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getAccessibleContext().setAccessibleName("...:::::  KITANDA - FACTURAÃO ::::...");

        pack();
    }// </editor-fold>//GEN-END:initComponents


    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void txtTotal_AOA_IliquidoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTotal_AOA_IliquidoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTotal_AOA_IliquidoActionPerformed


    private void txtTotal_AOA_liquidoActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtTotal_AOA_liquidoActionPerformed
    {//GEN-HEADEREND:event_txtTotal_AOA_liquidoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTotal_AOA_liquidoActionPerformed

    private void txtTotal_AOA_IVAActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtTotal_AOA_IVAActionPerformed
    {//GEN-HEADEREND:event_txtTotal_AOA_IVAActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTotal_AOA_IVAActionPerformed

    private void txtTotal_AOA_DescontoActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtTotal_AOA_DescontoActionPerformed
    {//GEN-HEADEREND:event_txtTotal_AOA_DescontoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTotal_AOA_DescontoActionPerformed

    private void cmbArmazemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbArmazemActionPerformed
    {//GEN-HEADEREND:event_cmbArmazemActionPerformed
//        cmbArmazem.setSelectedIndex( 0 );
    }//GEN-LAST:event_cmbArmazemActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton4ActionPerformed
    {//GEN-HEADEREND:event_jButton4ActionPerformed

        int opcao = JOptionPane.showConfirmDialog( null, "Deseja continuar com a operação?" );
        if ( opcao == JOptionPane.YES_OPTION )
        {
            procedimento_salvar_compra();
        }


    }//GEN-LAST:event_jButton4ActionPerformed

    private void tableCompraPropertyChange(java.beans.PropertyChangeEvent evt)//GEN-FIRST:event_tableCompraPropertyChange
    {//GEN-HEADEREND:event_tableCompraPropertyChange
        // TODO add your handling code here:
        try
        {

            actualizar_valor_tabela( evt );
        }
        catch ( Exception e )
        {
        }
    }//GEN-LAST:event_tableCompraPropertyChange

    private void tabela_buscaMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_tabela_buscaMouseClicked
    {//GEN-HEADEREND:event_tabela_buscaMouseClicked
        // TODO add your handling code here:
        adicionarAocarrinhoViaTabela( evt );
    }//GEN-LAST:event_tabela_buscaMouseClicked

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton6ActionPerformed
    {//GEN-HEADEREND:event_jButton6ActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_jButton6ActionPerformed

    private void cmbAnoEconomicoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbAnoEconomicoActionPerformed
        // TODO add your handling code here:
        mostrar_proximo_codigo_documento();
//        actualizar_abreviacao();
    }//GEN-LAST:event_cmbAnoEconomicoActionPerformed

    private void cmbVossoDocumentoActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbVossoDocumentoActionPerformed
    {//GEN-HEADEREND:event_cmbVossoDocumentoActionPerformed
        // TODO add your handling code here:
        disableDatas();
    }//GEN-LAST:event_cmbVossoDocumentoActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelar;
    private javax.swing.ButtonGroup buttonGroup3;
    private static javax.swing.JComboBox<String> cmbAnoEconomico;
    public static javax.swing.JComboBox cmbArmazem;
    private static javax.swing.JComboBox<String> cmbTipoDocumento;
    private static javax.swing.JComboBox<String> cmbVossoDocumento;
    private static com.toedter.calendar.JDateChooser dcDataDocumento;
    private static com.toedter.calendar.JDateChooser dcDataVencimento;
    private static javax.swing.JButton jButton4;
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    public static javax.swing.JLabel lbLegenda;
    private javax.swing.JLabel lbPreco1;
    private javax.swing.JLabel lbTotalPagar;
    private javax.swing.JLabel lbTotalPagar1;
    private javax.swing.JLabel lbTotalPagar2;
    private javax.swing.JLabel lbTotalPagar3;
    public static javax.swing.JLabel lbValorPorExtenco;
    private static javax.swing.JLabel lb_ano_encomico1;
    private static javax.swing.JLabel lb_ano_encomico2;
    private static javax.swing.JLabel lb_ano_encomico3;
    private static javax.swing.JLabel lb_data_apuramento;
    private static javax.swing.JLabel lb_data_documento;
    private static javax.swing.JLabel lb_data_solicitacao;
    private static javax.swing.JLabel lb_fornecedor_email;
    private static javax.swing.JLabel lb_fornecedor_endereco;
    private static javax.swing.JLabel lb_fornecedor_nif;
    private static javax.swing.JLabel lb_fornecedor_nome;
    private static javax.swing.JLabel lb_fornecedor_telefone;
    private static javax.swing.JLabel lb_numero_artigos;
    private static javax.swing.JLabel lb_proximo_documento;
    private javax.swing.JLabel lb_usuario;
    private static javax.swing.JLabel ld_data_vencimento;
    private javax.swing.JDialog produtoPesquisaJDialog;
    private javax.swing.JTable tabela_busca;
    public static javax.swing.JTable tableCompra;
    private javax.swing.JTextField txtDesignacao;
    private static javax.swing.JTextField txtNumeroDocumento;
    public static javax.swing.JTextField txtTotal_AOA_Desconto;
    public static javax.swing.JTextField txtTotal_AOA_IVA;
    public static javax.swing.JTextField txtTotal_AOA_Iliquido;
    public static javax.swing.JTextField txtTotal_AOA_liquido;
    // End of variables declaration//GEN-END:variables

    public static void main( String[] args )
    {
        new ConverterEncomendasCompraVisao( 15, BDConexao.getInstancia(), "", null ).setVisible(true);
    }

    @Override
    public void run()
    {
        throw new UnsupportedOperationException( "Not supported yet." ); //To change body of generated methods, choose Tools | Templates.
    }

    private void init()
    {

        lb_proximo_documento.setText( "" );
        txtTotal_AOA_Iliquido.setText( CfMethods.formatarComoMoeda( 0.0 ) );
        anoEconomico = anoEconomicoController.getLastObject();
        try
        {
            cmbTipoDocumento.setModel( new DefaultComboBoxModel( documentosController.getVector2() ) );
            setWindowsListener();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
        limpar_cabecario();
        limpar_rodape();
        cmbArmazem.setModel( new DefaultComboBoxModel<>( armazensController.getVector() ) );
        cmbAnoEconomico.setModel( new DefaultComboBoxModel<>( anoEconomicoController.getVector() ) );

        usuario = ( TbUsuario ) usuariosController.findById( this.cod_usuario );

    }// </editor-fold>   

    private void adicionar_tabela()
    {
        DefaultTableModel modelo = ( DefaultTableModel ) tableCompra.getModel();
        //limpa a tabela
        modelo.setRowCount( 0 );
        tableCompra.setRowHeight( 25 );

        if ( Objects.nonNull( listaItensCompras ) )
        {
            fornecedor = ( TbFornecedor ) fornecedoresController.findById( getIdFornecedor() );
            mostrar_dados_fornecedor();
            for ( ItemCompras itemCompra : listaItensCompras )
            {
                TbProduto produtoLocal = produtosController.findByCod( itemCompra.getFkProduto().getCodigo() );
                Unidade unidade = ( Unidade ) unidadesController.findById( produtoLocal.getCodUnidade().getPkUnidade() );

                modelo.addRow( new Object[]
                {

                    true,
                    itemCompra.getFkProduto().getCodigo(),
                    produtoLocal.getDesignacao(),
                    itemCompra.getPrecoCompra(),
                    itemCompra.getQuantidade(),
                    unidade.getAbreviacao(),
                    itemCompra.getDesconto(),
                    itemCompra.getValorIva(),
                    itemCompra.getPrecoCompra() * itemCompra.getQuantidade(),
                    itemCompra.getTotal(),
                    5,
                    0

                } );
            }
        }
        actualizar_total();

    }

    private static void limpar_cabecario()
    {
        lb_fornecedor_endereco.setText( "" );
        lb_fornecedor_telefone.setText( "" );
        lb_fornecedor_email.setText( "" );

        lb_numero_artigos.setText( "" );
        lb_data_solicitacao.setText( "" );
        lb_data_apuramento.setText( "" );

    }

    private static void limpar_rodape()
    {
        txtTotal_AOA_Desconto.setText( "0" );
        txtTotal_AOA_Iliquido.setText( "0" );
        txtTotal_AOA_IVA.setText( "0" );
        txtTotal_AOA_liquido.setText( "0" );
        lbValorPorExtenco.setText( "" );
    }

    private static double getTotalIliquido()
    {
        DefaultTableModel modelo = ( DefaultTableModel ) tableCompra.getModel();
        double qtd = 0;
        double total_iliquido_local = 0, preco_unitario = 0;

        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {
            preco_unitario = Double.parseDouble( modelo.getValueAt( i, 3 ).toString() );
            qtd = Double.parseDouble( modelo.getValueAt( i, 4 ).toString() );
            total_iliquido_local += ( preco_unitario * qtd );

        }

        return total_iliquido_local;
    }

    private static boolean salvar_item_compra_comercial( Integer cod_compras )
    {

        System.out.println( "Cod Compra no Item" + cod_compras );
        boolean sucesso = true;
        if ( !MetodosUtil.tabela_vazia( tableCompra ) )
        {
            StoksController stocksControllerLocal = new StoksController( conexaoTransaction );
            PrecosController precosControllerLocal = new PrecosController( conexaoTransaction );
            ItemComprasController itemComprasControllerLocal = new ItemComprasController( conexaoTransaction );
            ItemCompras itemCompraLocal;
            for ( int i = 0; i < tableCompra.getRowCount(); i++ )
            {
                try
                {

                    double qtd = Double.parseDouble( String.valueOf( tableCompra.getModel().getValueAt( i, 4 ) ) );
                    TbProduto produto_local = ( TbProduto ) produtosController
                            .findById( Integer.parseInt( String.valueOf( tableCompra.getModel().getValueAt( i, 1 ) ) )
                            );

                    itemCompraLocal = new ItemCompras();
                    itemCompraLocal.setFkProduto( produto_local );
                    itemCompraLocal.setFkCompra( new Compras( cod_compras ) );
                    itemCompraLocal.setPrecoCompra( Double.parseDouble( String.valueOf( tableCompra.getModel().getValueAt( i, 3 ) ) ) );
                    itemCompraLocal.setQuantidade( Double.parseDouble( String.valueOf( tableCompra.getModel().getValueAt( i, 4 ) ) ) );
                    itemCompraLocal.setDesconto( Double.parseDouble( String.valueOf( tableCompra.getModel().getValueAt( i, 6 ) ) ) );
                    itemCompraLocal.setValorIva( Double.parseDouble( String.valueOf( tableCompra.getModel().getValueAt( i, 7 ) ) ) );
                    itemCompraLocal.setTotal( Double.parseDouble( String.valueOf( tableCompra.getModel().getValueAt( i, 9 ) ) ) );

                    itemCompraLocal.setMotivoIsensao( getMotivoIsensao( itemCompraLocal.getFkProduto().getCodigo() ) );
                    itemCompraLocal.setCodigoIsensao( MetodosUtil.getCodigoRegime( itemCompraLocal.getFkProduto().getCodigo() ) );

                    double qtdCritica = Double.parseDouble( String.valueOf( tableCompra.getModel().getValueAt( i, 10 ) ) );
                    double qtdBaixa = Double.parseDouble( String.valueOf( tableCompra.getModel().getValueAt( i, 11 ) ) );

                    //cria o item compra
                    if ( !itemComprasControllerLocal.salvar( itemCompraLocal ) )
                    {
                        DocumentosController.rollback( conexaoTransaction );
                        conexaoTransaction.close();
                        return false;
                    }

                    //verifca se existe o produto no stock caso nao registra.
                    if ( !stocksControllerLocal.existe_stock( produto_local.getCodigo(), getIdArmazens() ) )
                    {
                        boolean registrar_stock = registrar_stock( produto_local, itemCompraLocal.getQuantidade(), qtdCritica, qtdBaixa, stocksControllerLocal );
                        System.err.println( "Registro no Stock pela primeira vez: " + registrar_stock );
                        if ( !registrar_stock )
                        {
                            sucesso = false;
                            DocumentosController.rollback( conexaoTransaction );
                            conexaoTransaction.close();
                            return false;
                        }
                    }
                    else
                    {
                        actualizar_dados_stock( produto_local.getCodigo(),
                                qtd,
                                qtdCritica,
                                qtdBaixa, stocksControllerLocal );

                    }

                    double precoMedio = getPrecoCompraMedio( produto_local.getCodigo(), itemCompraLocal.getPrecoCompra() );
                    actualizarPreco( itemCompraLocal.getFkProduto().getCodigo(), precoMedio, precosControllerLocal );

                }
                catch ( Exception e )
                {
                    sucesso = false;
                    e.printStackTrace();
                    JOptionPane.showMessageDialog( null, "Falha ao registrar a compra", "Falha", JOptionPane.ERROR_MESSAGE );
                    DocumentosController.rollback( conexaoTransaction );
                    conexaoTransaction.close();
                    return false;

                }

            }

            if ( sucesso )
            {

                comprasController.actualizarEncomenda( codFactEncomendas, conexaoTransaction );
                DocumentosController.commit( conexaoTransaction );

                limpar_cabecario();
                limpar_rodape();
                esvaziar_tabela();
                compra = null;
                fornecedor = null;
                JOptionPane.showMessageDialog( null, "Compra efectuada com sucesso!.." );
                exibirReciboDeCompras();
//                FornecedorOutroVisao.adicionar_compras();
                conexaoTransaction.close();
                return true;

            }

        }
        else
        {
            JOptionPane.showMessageDialog( null, "Adiciona itens na tabela caro usuário", "AVISO", JOptionPane.WARNING_MESSAGE );
        }
        return false;
    }

    private static void esvaziar_tabela()
    {
        DefaultTableModel modelo = ( DefaultTableModel ) tableCompra.getModel();
        modelo.setRowCount( 0 );
    }

    public JFrame getInstance()
    {
        return this;
    }

    public void adicionarProdutoATabelaPrincipal()
    {

        Integer codProdutoInt = ( Integer ) tabela_busca.getValueAt( tabela_busca.getSelectedRow(), 0 );
        TbProduto produto = ( TbProduto ) produtosController.findById( codProdutoInt );

    }

    public static boolean validar_zero()
    {

        return true;
    }

    public static boolean validar_zero_preco()
    {

        return true;

    }

    public static boolean validar_zero_qtd_preco()
    {

        return true;

    }

    public static String getUnidade_Produto()
    {
        return produto.getCodUnidade().getAbreviacao();
    }

    private static void actualizar_total()
    {
        System.out.println( "CHEGUEI AQUI.....NO ACTUALIZAR TOTAL: " );
        txtTotal_AOA_Iliquido.setText( CfMethods.formatarComoMoeda( getTotalIliquido() ) );
        txtTotal_AOA_IVA.setText( CfMethods.formatarComoMoeda( getTotalIVASobreDesconto() ) );
        txtTotal_AOA_Desconto.setText( CfMethods.formatarComoMoeda( getDescontoComercial() + getDescontoFinanceiro() ) );
        txtTotal_AOA_liquido.setText( CfMethods.formatarComoMoeda( getTotalAOALiquido() ) );

        valor_por_extenco();
    }

    private void procedimento_salvar_compra()
    {

        if ( !MetodosUtil.tabela_vazia( tableCompra ) )
        {
            if ( Objects.nonNull( fornecedor ) )
            {
                if ( validarCampos() )
                {
                    try
                    {
                        salvar_compra_comercial();
                    }
                    catch ( HeadlessException e )
                    {
                        JOptionPane.showMessageDialog( null, "Falha ao criar o documento", "Falha", JOptionPane.ERROR_MESSAGE );
                    }
                }

            }
            else
            {
                JOptionPane.showMessageDialog( null, "Caro usuário seleccione o fornecedor", "Aviso", JOptionPane.WARNING_MESSAGE );
            }

        }
        else
        {
            JOptionPane.showMessageDialog( null, "Caro usuário adiciona item na tabela", "Aviso", JOptionPane.WARNING_MESSAGE );
        }

    }

    public static boolean salvar_compra_comercial()
    {
        conexaoTransaction = BDConexao.getInstancia();
        DocumentosController.start( conexaoTransaction );

        Date data_documento = new Date();

        Compras compra_local = new Compras();

        System.err.println( "#salvar_compra: " );

        compra_local.setDataCompra( data_documento );
        Calendar calendar = Calendar.getInstance();
        calendar.setTime( data_documento );
        //adicionar 15 dias na data do documento.
        calendar.add( Calendar.DATE, 15 );
        compra_local.setDataLimiteLevantamento( calendar.getTime() );
        compra_local.setFornecedorNif( fornecedor.getNif() );

        //Total Ilíquido
        compra_local.setTotalGeral( getTotalIliquido() );
        //desconto por linha
        compra_local.setDescontoComercial( getDescontoComercial() );
        //imposto
        //calculaTotalIVA();
        compra_local.setTotalIva( getTotalImposto() );
        //desconto global

        compra_local.setDescontoFinanceiro( getDescontoFinanceiro() );
        //Total(AOA) <=> Total Líquido
        compra_local.setTotalCompra( getTotalAOALiquido() );
        compra_local.setTotalIncidencia( getTotalIncidencia() );
        compra_local.setTotalIncidenciaIsento( getTotalIncidenciaIsento() );

        /*outros campos*/
        compra_local.setDescontoTotal( getDescontoComercial() + getDescontoFinanceiro() );
//        compra_local.setIdArmazemFK( armazemDao.findTbArmazem( DVML.ARMAZEM_DEFAUTL ) );compra_local.setFkAnoEconomico( anoEconomicoController.findById( anoEconomico )  );
        compra_local.setIdArmazemFK( ( TbArmazem ) armazensController.findById( DVML.ARMAZEM_DEFAUTL ) );
//        compra_local.setFkAnoEconomico( (AnoEconomico) anoEconomicoController.fi anoEconomico );
        compra_local.setFkAnoEconomico( anoEconomico );
        compra_local.setFkDocumento( documento );
//        compra_local.setFkFornecedor( FornecedorDao.findByNome( ( String ) cmbFornecedor.getSelectedItem() ) );
//        compra_local.setFkFornecedor( ( TbFornecedor ) fornecedoresController.findByName( ( String ) cmbFornecedor.getSelectedItem() ) );
//        venda_local.setIdBanco( new TbBanco(1));
        System.err.println( "Codigo do Fornecedor :" + getIdFornecedor() );
        compra_local.setCodigoUsuario( usuario );
        compra_local.setCodFact( prox_doc );
        compra_local.setRefCodFact( "" );
        compra_local.setHashCod( MetodosUtil.criptografia_hash( prox_doc ) );
//        compra_local.setHashCod ( MetodosUtil.criptografia_hash ( compra_local, getGrossTotal (), conexao ) );
        compra_local.setTotalPorExtenso( MetodosUtil.iniciais_extenso_comercial( documento.getPkDocumento(), documentosController ) + lbValorPorExtenco.getText() );
        compra_local.setStatusEliminado( "false" );
//        compra_local.setStatusRecibo(false );
        System.out.println( "STATUS:hash cod processado." );
        compra_local.setAssinatura( MetodosUtil.assinatura_doc( compra_local.getHashCod() ) );
        compra_local.setDocVosso( cmbVossoDocumento.getSelectedItem().toString() );
        compra_local.setDocVossoNumero( txtNumeroDocumento.getText() );
        compra_local.setDocVossoData( dcDataDocumento.getDate() );
        compra_local.setDocVossoDataVencimento( dcDataVencimento.getDate() );

        fornecedor = ( TbFornecedor ) fornecedoresController.findById( getIdFornecedor() );
        compra_local.setFkFornecedor( fornecedor );

        compra_local.setValorPago( BigDecimal.ZERO );
        compra_local.setValorPorPagar( new BigDecimal( compra_local.getTotalCompra() ) );

//        JOptionPane.showMessageDialog( null, +getIdFornecedor() );
        try
        {
            if ( comprasController.salvar_encomenda_to_compra( compra_local ) )
            {
                Integer last_compra = comprasController.getLastCompras().getPkCompra();

                System.err.println( "EXTRATO DE CONTA FORNECEDOR :" + last_compra );
//                Compras comprasExtrato = ( Compras ) comprasController.findById( last_compra );
                System.out.println( "CompraLocal: " + compra_local.getFkFornecedor().getCodigo() );

                if ( cmbVossoDocumento.getSelectedIndex() == 0
                        && !ExtratoContaFornecedorController.registro_movimento_conta_fornecedor( compra_local, conexao ) )
                {
                    DocumentosController.rollback( conexaoTransaction );
                    conexaoTransaction.close();
                    return false;
                }

                if ( Objects.isNull( last_compra ) || last_compra == 0 )
                {
                    DocumentosController.rollback( conexaoTransaction );
                    conexaoTransaction.close();
                    return false;
                }
                if ( !Objects.isNull( last_compra ) )
                {
                    return salvar_item_compra_comercial( last_compra );
                }
                else
                {
                    DocumentosController.rollback( conexaoTransaction );
                    conexaoTransaction.close();
                    return false;
                }

            }
            else
            {
                System.out.println( "ERROR: Já existe compra relacionada." );
            }

            return true;
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            System.err.println( "STATUS: falha ao actualizar a factura" );
            e.printStackTrace();
            JOptionPane.showMessageDialog( null, "Falha ao Processar a Factura", "FALHA", JOptionPane.ERROR_MESSAGE );
            DocumentosController.rollback( conexaoTransaction );
            conexaoTransaction.close();

        }
        return false;

    }

    private static double getTotalImposto()
    {
        DefaultTableModel modelo = ( DefaultTableModel ) tableCompra.getModel();
        double qtd = 0;
        double imposto = 0d, preco_unitario = 0d, desconto_valor_linha = 0;

        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {
            preco_unitario = Double.parseDouble( modelo.getValueAt( i, 3 ).toString() );
            qtd = Double.parseDouble( modelo.getValueAt( i, 4 ).toString() );
            double valor_percentagem = Double.parseDouble( modelo.getValueAt( i, 6 ).toString() );
            double taxa = Double.parseDouble( modelo.getValueAt( i, 7 ).toString() );
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

    private static Double getDescontoComercial()
    {
        DefaultTableModel modelo = ( DefaultTableModel ) tableCompra.getModel();
        double qtd = 0;
        double desconto_comercial = 0d, preco_unitario = 0d, desconto_valor_linha = 0d;

        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {
            preco_unitario = Double.parseDouble( modelo.getValueAt( i, 3 ).toString() );
            qtd = Double.parseDouble( modelo.getValueAt( i, 4 ).toString() );
            double valor_percentagem = Double.parseDouble( modelo.getValueAt( i, 6 ).toString() );
            desconto_valor_linha = ( ( valor_percentagem ) / 100 );
            double valor_unitario = ( preco_unitario * qtd );
            desconto_comercial += ( valor_unitario * desconto_valor_linha );

        }

        return desconto_comercial;
    }

    private static Double getDescontoFinanceiro()
    {
        return 0d;
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

    private static double getTotalIncidencia()
    {
        DefaultTableModel modelo = ( DefaultTableModel ) tableCompra.getModel();
        double qtd = 0;
        double incidencia = 0d, preco_unitario = 0d, desconto_valor_linha = 0;

        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {
            preco_unitario = Double.parseDouble( modelo.getValueAt( i, 3 ).toString() );
            qtd = Double.parseDouble( modelo.getValueAt( i, 4 ).toString() );
            double valor_percentagem = Double.parseDouble( modelo.getValueAt( i, 6 ).toString() );
            double taxa = Double.parseDouble( modelo.getValueAt( i, 7 ).toString() );
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

    private static double getTotalIncidenciaIsento()
    {
        DefaultTableModel modelo = ( DefaultTableModel ) tableCompra.getModel();
        double qtd = 0;
        double incidencia_isento = 0d, preco_unitario = 0d, desconto_valor_linha = 0;

        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {
            preco_unitario = Double.parseDouble( modelo.getValueAt( i, 3 ).toString() );
            qtd = Double.parseDouble( modelo.getValueAt( i, 4 ).toString() );
            double valor_percentagem = Double.parseDouble( modelo.getValueAt( i, 6 ).toString() );
            double taxa = Double.parseDouble( modelo.getValueAt( i, 7 ).toString() );
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

    public void remover_item_carrinho()
    {

        DefaultTableModel modelo = ( DefaultTableModel ) tableCompra.getModel();
        modelo.removeRow( tableCompra.getSelectedRow() );
        actualizar_total();

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

    private void mostrar_proximo_codigo_documento()
    {
        try
        {
            this.documento = ( Documento ) documentosController.findById( getIdDocumento() );
            this.anoEconomico = ( AnoEconomico ) anoEconomicoController.findById( getIdAnoEconomico() );
            this.doc_prox_cod = comprasController.getUltimaContagemByIdDocumentoAndAnoEconomico(
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

    private static double getTotalIVASobreDesconto()
    {

        DefaultTableModel modelo = ( DefaultTableModel ) tableCompra.getModel();
        double iva = 0, preco = 0, desconto = 0;
        double qtd = 0;
        total_iva = 0;
        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {

            preco = Double.parseDouble( modelo.getValueAt( i, 3 ).toString() );
            qtd = Double.parseDouble( modelo.getValueAt( i, 4 ).toString() );
            desconto = Double.parseDouble( modelo.getValueAt( i, 6 ).toString() );
            iva = Double.parseDouble( modelo.getValueAt( i, 7 ).toString() );

            total_iva += getIVA( qtd, iva, preco, desconto );
        }

        // System.err.println("Com o método: " + MetodosUtil.valorCasasDecimaisNovo(total_iva, CASAS_DECIMAIS));
        return total_iva;
        //total_iva = MetodosUtil.valorCasasDecimaisNovo( total_iva, CASAS_DECIMAIS );

    }

    private static double getIVA( double qtd, double taxa, double preco_venda, double desconto )
    {
        double subtotal_linha = ( preco_venda * qtd );
        double desconto_valor = ( subtotal_linha * ( desconto / 100 ) );
        double valor_iva = ( taxa / 100 );//
        return ( ( subtotal_linha - desconto_valor ) * valor_iva );

    }

    private static void valor_por_extenco()
    {
//        lbLegenda.setText( MetodosUtil.iniciais_extenso_comercial( documento.getPkDocumento(), documentosController ) );
        System.out.println( "Total_Liquido" + txtTotal_AOA_liquido.getText() );
        lbValorPorExtenco.setText( MetodosUtil.valorPorExtenso( CfMethods.parseMoedaFormatada( txtTotal_AOA_liquido.getText() ), "Kwanza" ) );
    }

    private static void exibirReciboDeCompras()
    {
        try
        {
//          s  Compras lastCompras = new ComprasDao( emf ).findLastRow();
            Integer last_compras = comprasController.getLastCompras().getPkCompra();
//            int last_compra = compraDao.getLastCompra();
            Compras compra_local = ( Compras ) comprasController.findById( last_compras );

            Connection connection = ( Connection ) BDConexao.getConexao();
            HashMap hashMap = new HashMap();
            //this.motivo_isencao = "Regime Transitório";
//        hashMap.put( "CODIGO_VENDA", this.codigo );
//        hashMap.put( "DOCUMENTO", vendaDao.findTbVenda( codigo ).getFkDocumento().getDesignacao() );
//        hashMap.put( "SOFTWARE_VERSION", VERSION_SOFTWARE );
//        hashMap.put( "SOFTWARE_NAME", NAME_SOFTWARE );
//        hashMap.put( "REF_COD_FACT", getRefCodFact( vendaDao.findTbVenda( codigo ).getRefCodFact() ) );
//        hashMap.put( "STATUS_DOCUMENTO", this.status_documento );
//        hashMap.put( "MOTIVO_ISENCAO", this.motivo_isencao );
//        hashMap.put( "NIF_CLIENTE_CONSOMIDOR_FINAL", setConsumidorFinal( vendaDao.findTbVenda( codigo ) ) );
//            hashMap.put( "PK_COMPRA", last_compras.getPkCompra() );
            hashMap.put( "PK_COMPRA", last_compras );
            String relatorio = CAMINHO_REPORT + "compra_recibo.jasper";;

            File file = new File( relatorio ).getAbsoluteFile();
            String obterCaminho = file.getAbsolutePath();

            try
            {
                JasperFillManager.fillReport( obterCaminho, hashMap, connection );
                JasperPrint jasperPrint = JasperFillManager.fillReport( obterCaminho, hashMap, connection );
                if ( jasperPrint.getPages().size() >= 1 )
                {
                    JasperViewer jasperViewer = new JasperViewer( jasperPrint, false );
                    jasperViewer.setVisible( true );
                }
                else
                {
                    JOptionPane.showMessageDialog( null, "Nao Existem Operações!..." );
                }
            }
            catch ( JRException jex )
            {
                jex.printStackTrace();
                //System.out.println("aqui");
                JOptionPane.showMessageDialog( null, "FALHA AO TENTAR MOSTRAR A FACTURA!..." );
            }
            catch ( Exception ex )
            {
                ex.printStackTrace();
                JOptionPane.showMessageDialog( null, "ERRO AO EFECTUAR A FACTURA!..." );
            }
        }
        catch ( SQLException ex )
        {
            Logger.getLogger( ConverterEncomendasCompraVisao.class.getName() ).log( Level.SEVERE, null, ex );
        }
    }

    private void initJDialogue()
    {
        txtDesignacao.requestFocus();

        txtDesignacao.getDocument().addDocumentListener( new DocumentListener()
        {
            @Override
            public void insertUpdate( DocumentEvent e )
            {
                String prefixo = txtDesignacao.getText();
                adicionar( prefixo );
            }

            @Override
            public void removeUpdate( DocumentEvent e )
            {
                String prefixo = txtDesignacao.getText();
                adicionar( prefixo );
            }

            @Override
            public void changedUpdate( DocumentEvent e )
            {
                throw new UnsupportedOperationException( "Not supported yet." ); //To change body of generated methods, choose Tools | Templates.
            }
        } );

        try
        {

            adicionar( "%" );

        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }

    }

    private void adicionar( String designacao )
    {

        String armazemSelecionado = ( String ) cmbArmazem.getSelectedItem();
//        TbArmazem armazem = ArmazemDao.findByDesigncao(armazemSelecionado);
        armazem = ( TbArmazem ) armazensController.findByName( armazemSelecionado );
        if ( Objects.nonNull( armazem ) )
        {
            produtosController.findByCodBarra( String.valueOf( cod_usuario ) );
//            ProdutoDao.findProdutoByCodigoBarra(String.valueOf(cod_usuario));
//            List<TbStock> stocks = StockDao.findByProdutoWhitDesignacaoLike(armazem.getCodigo(), designacao);
            List<TbStock> stocks = stocksController.findStockByDesignacaoProduto( armazem.getCodigo(), designacao );
            stocks = ( designacao.isEmpty() ) ? stocksController.listarTodos() : stocks;
            DefaultTableModel modelo = ( DefaultTableModel ) tabela_busca.getModel();

            modelo.setRowCount( 0 );
            if ( !Objects.isNull( stocks ) )
            {
                for ( TbStock stock : stocks )
                {
                    modelo.addRow(
                            new Object[]
                            {
                                stock.getCodProdutoCodigo().getCodigo(),
                                stock.getCodProdutoCodigo().getDesignacao(),
                                //                                stock.getCodProdutoCodigo().getCodTipoProduto().getDesignacao(),
                                stock.getQuantidadeExistente()
                            }
                    );

                }
            }
        }

    }

    private void adicionarAocarrinhoViaTabela( MouseEvent evt )
    {

        if ( evt.getClickCount() == 2 )
        {

            DefaultTableModel modelo = ( DefaultTableModel ) tabela_busca.getModel();
            int linha = tabela_busca.getSelectedRow();
            Integer codigo = ( Integer ) modelo.getValueAt( linha, 0 );
            accao_codigo_interno_enter_busca_exterior( codigo );
            produtoPesquisaJDialog.dispose();
        }
    }

    public static void accao_codigo_interno_enter_busca_exterior( int codigo )
    {

        try
        {

            //int   codigo = Integer.parseInt(txtCodigoProduto.getText() );
//            TbProduto produto = produtoDao.findTbProduto(codigo);
            produto = ( TbProduto ) produtosController.findById( codigo );
//            busca_produto_by_cod_interno(codigo);

        }
        catch ( Exception ex )
        {
            //  ex.printStackTrace();
            Logger.getLogger( VendaUsuarioVisao.class.getName() ).log( Level.SEVERE, null, ex );
            JOptionPane.showMessageDialog( null, "Este produto não existe no armazém " + cmbArmazem.getSelectedItem(), DVML.DVML_COMERCIAL, JOptionPane.ERROR_MESSAGE );
        }

    }
//

    private static void actualizarPreco( int idProduto, Double precoCompra, PrecosController precosControllerLocal )
    {
        System.out.println( "ID PRODUTO: " + idProduto );

        int idPrecoRetalho = PrecosController.getLastIdPrecoByIdProdutoIntAndQTD( idProduto, 1d, conexao );
        System.out.println( "ID RETALHO: " + idPrecoRetalho );
        TbPreco precoAntigoRetalho = ( TbPreco ) precosControllerLocal.findById( idPrecoRetalho );
        System.out.println( "PRECO RETALHO: " + precoAntigoRetalho );

        int idPrecoGrosso = PrecosController.getLastIdPrecoByIdProdutoIntAndPrecoAntigoQtdAlto( idProduto, precoAntigoRetalho.getQtdAlto() + 1, conexao );
        TbPreco precoAntigoGrosso = ( TbPreco ) precosControllerLocal.findById( idPrecoGrosso );
        System.out.println( "ID GROSSO: " + idPrecoGrosso );
        System.out.println( "PRECO GROSSO: " + precoAntigoGrosso );

        TbPreco preco_novo_retalho = new TbPreco();

        preco_novo_retalho.setData( precoAntigoRetalho.getData() );
        preco_novo_retalho.setHora( precoAntigoRetalho.getHora() );
        preco_novo_retalho.setPercentagemGanho( precoAntigoRetalho.getPercentagemGanho() );

        preco_novo_retalho.setFkProduto( precoAntigoRetalho.getFkProduto() );
        preco_novo_retalho.setFkUsuario( precoAntigoRetalho.getFkUsuario() );
        preco_novo_retalho.setPrecoCompra( new BigDecimal( precoCompra ) );
        preco_novo_retalho.setPrecoVenda( precoAntigoRetalho.getPrecoVenda() );
        preco_novo_retalho.setQtdBaixo( precoAntigoRetalho.getQtdBaixo() );
        preco_novo_retalho.setQtdAlto( precoAntigoRetalho.getQtdAlto() );
        preco_novo_retalho.setPrecoAnterior( precoAntigoRetalho.getPrecoAnterior() );
        preco_novo_retalho.setRetalho( precoAntigoRetalho.getRetalho() );

        System.out.println( "HORA RETALHO: " + preco_novo_retalho.getHora() );
        System.out.println( "DATA RETAHO: " + preco_novo_retalho.getData() );

        try
        {
            precosControllerLocal.salvar( preco_novo_retalho );
//            precoDao.create(preco_novo_retalho);
            System.out.println( "Preco de compra retalho actualizado na compra" );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            System.err.println( "Falha ao actualizar o preço retalho na compra" );
        }

        try
        {
            //        preco_novo_grosso = precoAntigoGrosso;
            TbPreco preco_novo_grosso = new TbPreco();

            preco_novo_grosso.setData( precoAntigoGrosso.getData() );
            preco_novo_grosso.setHora( precoAntigoGrosso.getHora() );
            preco_novo_grosso.setPercentagemGanho( precoAntigoGrosso.getPercentagemGanho() );

            preco_novo_grosso.setFkProduto( precoAntigoGrosso.getFkProduto() );
            preco_novo_grosso.setFkUsuario( precoAntigoGrosso.getFkUsuario() );
            preco_novo_grosso.setPrecoCompra( new BigDecimal( precoCompra ) );
            preco_novo_grosso.setPrecoVenda( precoAntigoGrosso.getPrecoVenda() );
            preco_novo_grosso.setQtdBaixo( precoAntigoGrosso.getQtdBaixo() );
            preco_novo_grosso.setQtdAlto( precoAntigoGrosso.getQtdAlto() );
            preco_novo_grosso.setPrecoAnterior( precoAntigoGrosso.getPrecoAnterior() );
            preco_novo_grosso.setRetalho( precoAntigoGrosso.getRetalho() );

            System.out.println( "HORA GROSSO: " + precoAntigoGrosso.getHora() );
            System.out.println( "DATA GROSSO: " + precoAntigoGrosso.getData() );

            precosControllerLocal.salvar( preco_novo_grosso );
//            precoDao.create(preco_novo_grosso);
            System.out.println( "Preco de compra grosso actualizado na compra" );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            System.err.println( "Falha ao actualizar o preço grosso na compra" );
        }

    }

    private static boolean registrar_stock( TbProduto produto_local, double qtd, double qtdCritica, double qtdbaixa, StoksController stocksControllerLocal )
    {

        TbStock stockLocal = new TbStock();

        stockLocal.setDataEntrada( new Date() );
//        stockLocal.setQuantidadeExistente( 0d );
        stockLocal.setQuantidadeExistente( qtd );
        stockLocal.setStatus( "true" );
        stockLocal.setPrecoVenda( new BigDecimal( MetodosUtil.convertToDouble( "0.0" ) ) );
        stockLocal.setPrecoVendaGrosso( new BigDecimal( stockLocal.getPrecoVenda().doubleValue() ) );
        stockLocal.setQtdGrosso( DVML.QTD_DEFAULT );
        stockLocal.setQuantCritica( ( int ) qtdCritica );
        stockLocal.setQuantBaixa( ( int ) qtdbaixa );
        stockLocal.setQuantidadeAntiga( 0d );
        stockLocal.setCodArmazem( new TbArmazem( getIdArmazens() ) );
        stockLocal.setCodProdutoCodigo( new TbProduto( produto_local.getCodigo() ) );
        System.out.println( "Produto Registrado no Stock." );
        return stocksControllerLocal.salvar( stockLocal );

    }

    private static void actualizar_dados_stock( int idProduto, double qtd, double qtdCritica, double qtdBaixa, StoksController stocksControllerLocal )
    {

        try
        {
//            System.out.println( "QTD ACTUALIZAR COMPRA: " + qtd );
            stocksControllerLocal.adicionar_quantidades( idProduto,
                    qtd,
                    qtdCritica, qtdBaixa, getIdArmazens() );

        }
        catch ( Exception e )
        {
        }

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

    public int getIdAnoEconomico()
    {
        try
        {

            anoEconomico = ( AnoEconomico ) anoEconomicoController.findByName( cmbAnoEconomico.getSelectedItem().toString() );
            return anoEconomico.getPkAnoEconomico();

        }
        catch ( Exception e )
        {
            e.printStackTrace();
            return 0;
        }

    }

    public static int getIdFornecedor()
    {
        try
        {
            int idCompra = listaItensCompras.get( 0 ).getFkCompra().getPkCompra();
            Compras comprasLocal = ( Compras ) comprasController.findById( idCompra );
            return comprasLocal.getFkFornecedor().getCodigo();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            return 0;
        }

    }

    public static int getIdArmazens()
    {
        try
        {

            armazem = ( TbArmazem ) armazensController.findByName( cmbArmazem.getSelectedItem().toString() );
            return armazem.getCodigo();

        }
        catch ( Exception e )
        {
            e.printStackTrace();
            return 0;
        }

    }

    public static int getCodigoProduto()
    {
        return 0;

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

    public void scrolltable()
    {

        tableCompra.scrollRectToVisible( tableCompra.getCellRect( tableCompra.getRowCount() - 1, tableCompra.getColumnCount(), true ) );

    }

    private static double getPrecoCompraMedio( int codProduto, double precoNovo )
    {

        preco = ( TbPreco ) precosController.getLastIdPrecoByIdProdutos( codProduto );
        double precoAntigo = preco.getPrecoCompra().doubleValue();
        System.err.println( "Preco Antigo: " + precoAntigo );
        System.err.println( "Preco Novo: " + precoNovo );
        double precoMedio = ( ( precoNovo + precoAntigo ) / 2 );
        System.err.println( "Preco Médio: " + precoMedio );
        return precoMedio;

    }

    public static void actualizar_quantidade( int cod, double quantidade )
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

    private void actualizar_valor_tabela( PropertyChangeEvent evt )
    {
        linha_actual = tableCompra.getSelectedRow();
        DefaultTableModel modelo = ( DefaultTableModel ) tableCompra.getModel();

        /**
         * produto.getCodigo(), produto.getDesignacao(), getPrecoCompra(),
         * getQuantidade(), getUnidade_Produto(), 0, getTaxaImposto(),
         * getQuantidade() * getPrecoCompra(), MetodosUtil.getValorComIVA(
         * getQuantidade(), getTaxaImposto(), getPrecoCompra(), 0 )
         */
        double preco_compra = Double.parseDouble( String.valueOf( modelo.getValueAt( linha_actual, 3 ) ) );
        Double quantidade = Double.parseDouble( String.valueOf( modelo.getValueAt( linha_actual, 4 ) ) );

        if ( preco_compra != 0 && quantidade != 0 )
        {
            double desconto = Double.parseDouble( String.valueOf( modelo.getValueAt( linha_actual, 6 ) ) );
            if ( desconto >= 0 && desconto <= 100 )
            {
                double taxa = Double.parseDouble( String.valueOf( modelo.getValueAt( linha_actual, 7 ) ) );

                double total_item = preco_compra * quantidade;
                double total_item_com_iva = FinanceUtils.getValorComIVA( quantidade, taxa, preco_compra, desconto );

                modelo.setValueAt( preco_compra, linha_actual, 3 );
                modelo.setValueAt( quantidade, linha_actual, 4 );
                modelo.setValueAt( desconto, linha_actual, 6 );
                modelo.setValueAt( taxa, linha_actual, 7 );
                modelo.setValueAt( total_item, linha_actual, 8 );
                modelo.setValueAt( total_item_com_iva, linha_actual, 9 );

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

    private static void mostrar_dados_fornecedor()
    {
        if ( Objects.nonNull( fornecedor ) )
        {
            lb_fornecedor_nome.setText( "Fornecedor: " + fornecedor.getNome() );
            lb_fornecedor_nif.setText( "Nif: " + fornecedor.getNif() );
            lb_fornecedor_telefone.setText( "Telefone: " + fornecedor.getTelefone() );
            lb_fornecedor_endereco.setText( "Endereço: " + fornecedor.getEndereco() );
            lb_fornecedor_email.setText( "Email: " + fornecedor.getEmail() );
        }
    }

    private void disableDatas()
    {
        if ( cmbVossoDocumento.getSelectedIndex() == 0 )
        {
            dcDataDocumento.setVisible( true );
            dcDataVencimento.setVisible( true );
            dcDataDocumento.setDate( new Date() );
            dcDataVencimento.setDate( null );
            lb_data_documento.setVisible( true );
            ld_data_vencimento.setVisible( true );
        }
        else
        {
            dcDataDocumento.setVisible( false );
            dcDataVencimento.setVisible( false );
            dcDataDocumento.setDate( new Date() );
            dcDataVencimento.setDate( new Date() );

            lb_data_documento.setVisible( false );
            ld_data_vencimento.setVisible( false );

        }
    }

    private boolean validarCampos()
    {

        if ( txtNumeroDocumento.getText().equals( "" ) )
        {
            JOptionPane.showMessageDialog( null, "Insira a ref. do documento.", "Aviso", JOptionPane.WARNING_MESSAGE );
            return false;
        }

        if ( cmbVossoDocumento.getSelectedIndex() == 0 )
        {
            if ( dcDataDocumento.getCalendar() == null )
            {
                JOptionPane.showMessageDialog( null, "Seleccione a data do documeto", "Aviso", JOptionPane.WARNING_MESSAGE );
                return false;
            }
            if ( dcDataVencimento.getCalendar() == null )
            {
                JOptionPane.showMessageDialog( null, "Seleccione a data de vencimento do documento.", "Aviso", JOptionPane.WARNING_MESSAGE );
                return false;
            }
        }

        return true;
    }

}
