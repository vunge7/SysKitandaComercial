/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package visao;

import dao.AnoEconomicoDao;
import dao.ArmazemDao;
import dao.ComprasDao;
import dao.DocumentoDao;
import dao.FornecedorDao;
import dao.ItemComprasDao;
import dao.PrecoDao;
import dao.ProdutoDao;
import dao.StockDao;
import dao.UsuarioDao;
import entity.*;
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
import comercial.controller.TipoProdutosController;
import comercial.controller.UsuariosController;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.io.File;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import kitanda.util.CfMethods;
import kitanda.util.CfMethodsSwing;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;
import util.BDConexao;
import util.DVML;
import static util.DVML.CAMINHO_REPORT;
import util.JPAEntityMannagerFactoryUtil;
import util.MetodosUtil;
import static util.MetodosUtil.getMotivoIsensao;

/**
 *
 * @author Domingos Dala Vunge
 */
public class NovaEncomendaVisao extends javax.swing.JFrame implements Runnable
{

    private static EntityManagerFactory emf = JPAEntityMannagerFactoryUtil.em;

    private ItemCompras itemCompras;
    private static PrecosController precosController;
    private static LugaresController lugaresController;
    private static ProdutosController produtosController;
    private static TipoProdutosController tipoProdutosController;
    private static StoksController stocksController;
    private static ArmazensController armazensController;
    private static FornecedoresController fornecedoresController;
    private static AnoEconomicoController anoEconomicoController;
    private static DocumentosController documentosController;
    private static ComprasController comprasController;
    private static ItemComprasController itemComprasController;
    private static UsuariosController usuariosController;
    private static TbFornecedor fornecedor;
    private static TbStock stock_local;
    private static Compras encomenda_global;
    private static TbProduto produto;
    private static TbUsuario usuario;
    private static DocumentoDao documentoDao;
    private static AnoEconomico anoEconomico;
    private static TbPreco preco;
    private static BDConexao conexao;
    private static Documento documento;
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
    private static BDConexao conexaoTransaction;
//    private static int GRUPO_AREA;

    public NovaEncomendaVisao( TbFornecedor fornecedor, int cod_usuario, BDConexao conexao )
    {
        initComponents();

        //confiLabel();
        precosController = new PrecosController( conexao );
        lugaresController = new LugaresController( conexao );
        produtosController = new ProdutosController( conexao );
        tipoProdutosController = new TipoProdutosController( conexao );
        stocksController = new StoksController( conexao );
        armazensController = new ArmazensController( conexao );
        fornecedoresController = new FornecedoresController( conexao );
        anoEconomicoController = new AnoEconomicoController( conexao );
        documentosController = new DocumentosController( conexao );
        comprasController = new ComprasController( conexao );
        itemComprasController = new ItemComprasController( conexao );
        usuariosController = new UsuariosController( conexao );
        setLocationRelativeTo( null );
        setResizable( false );
        this.cod_usuario = cod_usuario;
        this.conexao = conexao;
//        this.GRUPO_AREA = GRUPO_AREA;
        NovaEncomendaVisao.fornecedor = fornecedor;
        init();
        initJDialogue();
        //DESATIVAR COMVERTER COMPRAS
        setWindowsListener();

        KeyboardFocusManager.getCurrentKeyboardFocusManager()
                .addKeyEventDispatcher( new KeyEventDispatcher()
                {
                    @Override
                    public boolean dispatchKeyEvent( KeyEvent e )
                    {
                        if ( e.getID() == KeyEvent.KEY_RELEASED && e.getKeyCode() == KeyEvent.VK_F1 )
                        {
//                            try
//                            {
//                                cmbArmazem.setModel( new DefaultComboBoxModel( armazemDao.buscaTodos1() ) );
//                                new BuscaProdutoVisao( getInstance(), rootPaneCheckingEnabled, getIdArmazem(), DVML.JANELA_COMPRA, CompraVisao.class ).show();
//                            }
//                            catch ( Exception ex )
//                            {
//                                ex.printStackTrace();
//                            }
                            return true;

                        }
                        return false;
                    }
                } );

        mostrar_fornecedor();
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
        cmbTipoDocumento = new javax.swing.JComboBox<>();
        cmbAnoEconomico = new javax.swing.JComboBox<>();
        jPanel3 = new javax.swing.JPanel();
        lb_fornecedor_nome1 = new javax.swing.JLabel();
        lb_fornecedor_endereco = new javax.swing.JLabel();
        lb_fornecedor_telefone = new javax.swing.JLabel();
        lb_fornecedor_email = new javax.swing.JLabel();
        lb_data_apuramento1 = new javax.swing.JLabel();
        txtCodBarra = new javax.swing.JTextField();
        txtCodManual = new javax.swing.JTextField();
        lb_produto = new javax.swing.JLabel();
        lb_data_apuramento3 = new javax.swing.JLabel();
        spPrecoCompra = new javax.swing.JSpinner();
        spQtd = new javax.swing.JSpinner();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        cmbFornecedor = new javax.swing.JComboBox<>();
        lb_data_apuramento5 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jButton5 = new javax.swing.JButton();
        txtCodigoInterno = new javax.swing.JTextField();
        lbProdutos2 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        lb_data_apuramento6 = new javax.swing.JLabel();
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
        setTitle("...:::::  SOLICITAÇÃO DE COMPRAS ::::...");

        jPanel2.setBackground(new java.awt.Color(204, 204, 204));
        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tableCompra.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        tableCompra.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "Cod.Art", "Descrição", "Preço", "Qtd.", "Unidade", "Desconto(%)", "Taxa ", "Valor", "Valor C/ Imposto"
            }
        )
        {
            Class[] types = new Class []
            {
                java.lang.Integer.class, java.lang.String.class, java.lang.Double.class, java.lang.Integer.class, java.lang.String.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean []
            {
                false, false, true, true, false, true, true, false, false
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

        jPanel2.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 1080, 200));

        jPanel6.setBackground(new java.awt.Color(0, 51, 102));
        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("REGISTRO DE ENCOMENDAS");

        lb_proximo_documento.setFont(new java.awt.Font("Lucida Grande", 1, 18)); // NOI18N
        lb_proximo_documento.setForeground(new java.awt.Color(255, 255, 255));
        lb_proximo_documento.setText("PRÓXIMO DOC: XX PP/A1");

        cmbTipoDocumento.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cmbAnoEconomico.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbAnoEconomico.setEnabled(false);
        cmbAnoEconomico.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbAnoEconomicoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 302, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lb_proximo_documento, javax.swing.GroupLayout.PREFERRED_SIZE, 428, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbTipoDocumento, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cmbAnoEconomico, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(56, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)
                    .addComponent(lb_proximo_documento, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cmbTipoDocumento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbAnoEconomico, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lb_fornecedor_nome1.setFont(new java.awt.Font("Lucida Grande", 1, 12)); // NOI18N
        lb_fornecedor_nome1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lb_fornecedor_nome1.setText("Fornecedor");
        jPanel3.add(lb_fornecedor_nome1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 90, -1));

        lb_fornecedor_endereco.setFont(new java.awt.Font("Lucida Grande", 1, 11)); // NOI18N
        lb_fornecedor_endereco.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lb_fornecedor_endereco.setText("Endereço");
        jPanel3.add(lb_fornecedor_endereco, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 80, 390, -1));

        lb_fornecedor_telefone.setFont(new java.awt.Font("Lucida Grande", 1, 12)); // NOI18N
        lb_fornecedor_telefone.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lb_fornecedor_telefone.setText("Telefone");
        jPanel3.add(lb_fornecedor_telefone, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 100, 390, -1));

        lb_fornecedor_email.setFont(new java.awt.Font("Lucida Grande", 1, 12)); // NOI18N
        lb_fornecedor_email.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lb_fornecedor_email.setText("email");
        jPanel3.add(lb_fornecedor_email, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 120, 390, -1));

        lb_data_apuramento1.setFont(new java.awt.Font("SansSerif", 1, 16)); // NOI18N
        lb_data_apuramento1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lb_data_apuramento1.setText("Qtd.");
        jPanel3.add(lb_data_apuramento1, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 140, 110, 30));

        txtCodBarra.setFont(new java.awt.Font("SansSerif", 0, 16)); // NOI18N
        txtCodBarra.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtCodBarraActionPerformed(evt);
            }
        });
        jPanel3.add(txtCodBarra, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 30, 170, -1));

        txtCodManual.setFont(new java.awt.Font("SansSerif", 0, 16)); // NOI18N
        txtCodManual.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtCodManualActionPerformed(evt);
            }
        });
        jPanel3.add(txtCodManual, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 90, 170, -1));

        lb_produto.setFont(new java.awt.Font("SansSerif", 3, 16)); // NOI18N
        lb_produto.setForeground(new java.awt.Color(0, 51, 102));
        lb_produto.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lb_produto.setText("Produto/Artigo");
        lb_produto.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153), 2));
        jPanel3.add(lb_produto, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 50, 480, 30));

        lb_data_apuramento3.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        lb_data_apuramento3.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lb_data_apuramento3.setText("Cod. Manual");
        jPanel3.add(lb_data_apuramento3, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 60, 110, 30));

        spPrecoCompra.setFont(new java.awt.Font("SansSerif", 0, 16)); // NOI18N
        jPanel3.add(spPrecoCompra, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 90, 160, -1));

        spQtd.setFont(new java.awt.Font("SansSerif", 0, 16)); // NOI18N
        jPanel3.add(spQtd, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 140, 70, -1));

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/adicionar.png"))); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 130, 50, -1));

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/Button-Add-icon.png"))); // NOI18N
        jButton3.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 130, 50, -1));

        cmbFornecedor.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbFornecedor.setEnabled(false);
        cmbFornecedor.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbFornecedorActionPerformed(evt);
            }
        });
        jPanel3.add(cmbFornecedor, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 40, 390, 30));

        lb_data_apuramento5.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        lb_data_apuramento5.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lb_data_apuramento5.setText("Cod. Barra");
        jPanel3.add(lb_data_apuramento5, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 0, 100, 20));

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jPanel3.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 0, 10, 180));

        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/proucura.png"))); // NOI18N
        jButton5.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButton5ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton5, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 140, 40, 30));

        txtCodigoInterno.setBackground(new java.awt.Color(0, 51, 102));
        txtCodigoInterno.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txtCodigoInterno.setForeground(new java.awt.Color(255, 255, 255));
        txtCodigoInterno.setCaretColor(new java.awt.Color(255, 255, 255));
        txtCodigoInterno.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtCodigoInternoActionPerformed(evt);
            }
        });
        jPanel3.add(txtCodigoInterno, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 140, 130, 30));

        lbProdutos2.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        lbProdutos2.setText("Cód. Interno");
        jPanel3.add(lbProdutos2, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 110, -1, 30));

        jSeparator2.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jPanel3.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 0, 10, 180));

        lb_data_apuramento6.setFont(new java.awt.Font("SansSerif", 1, 16)); // NOI18N
        lb_data_apuramento6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lb_data_apuramento6.setText("Preço Compra");
        jPanel3.add(lb_data_apuramento6, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 90, 120, 30));

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel8.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lbTotalPagar.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbTotalPagar.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbTotalPagar.setText("Desconto :");
        jPanel8.add(lbTotalPagar, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 10, 130, 40));

        txtTotal_AOA_Iliquido.setEditable(false);
        txtTotal_AOA_Iliquido.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N
        txtTotal_AOA_Iliquido.setForeground(new java.awt.Color(255, 0, 0));
        txtTotal_AOA_Iliquido.setCaretColor(new java.awt.Color(255, 255, 255));
        txtTotal_AOA_Iliquido.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtTotal_AOA_IliquidoActionPerformed(evt);
            }
        });
        jPanel8.add(txtTotal_AOA_Iliquido, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 50, 280, 30));

        lbTotalPagar1.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbTotalPagar1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbTotalPagar1.setText("TOTAL LÍQUIDO :");
        jPanel8.add(lbTotalPagar1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 120, 170, 34));

        lbTotalPagar2.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbTotalPagar2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbTotalPagar2.setText("Total Iliquido :");
        jPanel8.add(lbTotalPagar2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 50, 170, 34));

        txtTotal_AOA_liquido.setEditable(false);
        txtTotal_AOA_liquido.setFont(new java.awt.Font("Tahoma", 1, 25)); // NOI18N
        txtTotal_AOA_liquido.setForeground(new java.awt.Color(255, 0, 0));
        txtTotal_AOA_liquido.setCaretColor(new java.awt.Color(255, 255, 255));
        txtTotal_AOA_liquido.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtTotal_AOA_liquidoActionPerformed(evt);
            }
        });
        jPanel8.add(txtTotal_AOA_liquido, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 120, 280, 30));

        lbTotalPagar3.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbTotalPagar3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbTotalPagar3.setText("IVA :");
        jPanel8.add(lbTotalPagar3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 80, 170, 34));

        txtTotal_AOA_IVA.setEditable(false);
        txtTotal_AOA_IVA.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N
        txtTotal_AOA_IVA.setForeground(new java.awt.Color(255, 0, 0));
        txtTotal_AOA_IVA.setCaretColor(new java.awt.Color(255, 255, 255));
        txtTotal_AOA_IVA.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtTotal_AOA_IVAActionPerformed(evt);
            }
        });
        jPanel8.add(txtTotal_AOA_IVA, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 80, 280, 30));

        txtTotal_AOA_Desconto.setEditable(false);
        txtTotal_AOA_Desconto.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N
        txtTotal_AOA_Desconto.setForeground(new java.awt.Color(255, 0, 0));
        txtTotal_AOA_Desconto.setCaretColor(new java.awt.Color(255, 255, 255));
        txtTotal_AOA_Desconto.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtTotal_AOA_DescontoActionPerformed(evt);
            }
        });
        jPanel8.add(txtTotal_AOA_Desconto, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 10, 280, 30));

        jButton4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/impressora1.png"))); // NOI18N
        jButton4.setText("Registrar Encomenda");
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

        lbValorPorExtenco.setFont(new java.awt.Font("Lucida Grande", 1, 16)); // NOI18N
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
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(3, 3, 3)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(lb_numero_artigos, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(lb_data_solicitacao, javax.swing.GroupLayout.PREFERRED_SIZE, 293, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(lb_data_apuramento, javax.swing.GroupLayout.PREFERRED_SIZE, 293, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addComponent(lbLegenda, javax.swing.GroupLayout.PREFERRED_SIZE, 348, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, 485, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lb_usuario, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lbValorPorExtenco, javax.swing.GroupLayout.PREFERRED_SIZE, 631, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 1095, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lbLegenda, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(5, 5, 5)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addComponent(lb_data_solicitacao)
                                .addGap(5, 5, 5)
                                .addComponent(lb_data_apuramento))
                            .addComponent(lb_numero_artigos)))
                    .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lb_usuario, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbValorPorExtenco))
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

    public static int getCodigoProduto()
    {
        try
        {

            produto = ( TbProduto ) produtosController.findByName( lb_produto.getText() );
            return produto.getCodigo();

        }
        catch ( Exception e )
        {
            e.printStackTrace();
            return 0;
        }

    }

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

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton2ActionPerformed
    {//GEN-HEADEREND:event_jButton2ActionPerformed
        // TODO add your handling code here:
        remover_item_carrinho();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton3ActionPerformed
    {//GEN-HEADEREND:event_jButton3ActionPerformed
        // TODO add your handling code here:
        adicionarProdutoATabelaPrincipalPeloBotao();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void cmbFornecedorActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbFornecedorActionPerformed
    {//GEN-HEADEREND:event_cmbFornecedorActionPerformed
        // TODO add your handling code here:
        mostrar_fornecedor();
    }//GEN-LAST:event_cmbFornecedorActionPerformed

    private void txtCodBarraActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtCodBarraActionPerformed
    {//GEN-HEADEREND:event_txtCodBarraActionPerformed
        // TODO add your handling code here:
        busca_produto_by_cod_barra();
    }//GEN-LAST:event_txtCodBarraActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton4ActionPerformed
    {//GEN-HEADEREND:event_jButton4ActionPerformed
        procedimento_salvar_compra();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void txtCodManualActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtCodManualActionPerformed
    {//GEN-HEADEREND:event_txtCodManualActionPerformed
        // TODO add your handling code here:
        busca_produto_by_cod_manual();
    }//GEN-LAST:event_txtCodManualActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton5ActionPerformed
    {//GEN-HEADEREND:event_jButton5ActionPerformed

        // exibirDialogoPesquisaProduto();
        try
        {
//            new BuscaNovoProdutoVisao(this, rootPaneCheckingEnabled, coordenada )
            new BuscaProdutoVisao( this, rootPaneCheckingEnabled, getIdArmazem(), DVML.JANELA_COMPRA, conexao ).show();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }

    }//GEN-LAST:event_jButton5ActionPerformed

    private void txtCodigoInternoActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtCodigoInternoActionPerformed
    {//GEN-HEADEREND:event_txtCodigoInternoActionPerformed
        // TODO add your handling code here:

        busca_produto_by_cod_interno();

    }//GEN-LAST:event_txtCodigoInternoActionPerformed

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

    private void cmbAnoEconomicoActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbAnoEconomicoActionPerformed
    {//GEN-HEADEREND:event_cmbAnoEconomicoActionPerformed
        // TODO add your handling code here:
        mostrar_proximo_codigo_documento();
        //        actualizar_abreviacao();
    }//GEN-LAST:event_cmbAnoEconomicoActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelar;
    private javax.swing.ButtonGroup buttonGroup3;
    private static javax.swing.JComboBox<String> cmbAnoEconomico;
    private static javax.swing.JComboBox<String> cmbFornecedor;
    private static javax.swing.JComboBox<String> cmbTipoDocumento;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
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
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    public static javax.swing.JLabel lbLegenda;
    private javax.swing.JLabel lbProdutos2;
    private javax.swing.JLabel lbTotalPagar;
    private javax.swing.JLabel lbTotalPagar1;
    private javax.swing.JLabel lbTotalPagar2;
    private javax.swing.JLabel lbTotalPagar3;
    public static javax.swing.JLabel lbValorPorExtenco;
    private static javax.swing.JLabel lb_data_apuramento;
    private static javax.swing.JLabel lb_data_apuramento1;
    private static javax.swing.JLabel lb_data_apuramento3;
    private static javax.swing.JLabel lb_data_apuramento5;
    private static javax.swing.JLabel lb_data_apuramento6;
    private static javax.swing.JLabel lb_data_solicitacao;
    private static javax.swing.JLabel lb_fornecedor_email;
    private static javax.swing.JLabel lb_fornecedor_endereco;
    private static javax.swing.JLabel lb_fornecedor_nome1;
    private static javax.swing.JLabel lb_fornecedor_telefone;
    private static javax.swing.JLabel lb_numero_artigos;
    public static javax.swing.JLabel lb_produto;
    private javax.swing.JLabel lb_proximo_documento;
    private javax.swing.JLabel lb_usuario;
    private javax.swing.JDialog produtoPesquisaJDialog;
    public static javax.swing.JSpinner spPrecoCompra;
    public static javax.swing.JSpinner spQtd;
    private javax.swing.JTable tabela_busca;
    public static javax.swing.JTable tableCompra;
    private static javax.swing.JTextField txtCodBarra;
    private static javax.swing.JTextField txtCodManual;
    private static javax.swing.JTextField txtCodigoInterno;
    private javax.swing.JTextField txtDesignacao;
    public static javax.swing.JTextField txtTotal_AOA_Desconto;
    public static javax.swing.JTextField txtTotal_AOA_IVA;
    public static javax.swing.JTextField txtTotal_AOA_Iliquido;
    public static javax.swing.JTextField txtTotal_AOA_liquido;
    // End of variables declaration//GEN-END:variables

    public static void main( String[] args )
    {
        new NovaEncomendaVisao( null, 15, new BDConexao() ).show();
    }

    @Override
    public void run()
    {
        throw new UnsupportedOperationException( "Not supported yet." ); //To change body of generated methods, choose Tools | Templates.
    }

    private void init()
    {
        lb_proximo_documento.setText( "" );
        anoEconomico = anoEconomicoController.getLastObject();
//        documentoDao = new DocumentoDao( emf );
        try
        {
            cmbTipoDocumento.setModel( new DefaultComboBoxModel( documentosController.getVector3() ) );
            setWindowsListener();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
        txtTotal_AOA_Iliquido.setText( CfMethods.formatarComoMoeda( 0.0 ) );
//        mostrar_proximo_codigo_documento();
        spQtd.setModel( CfMethodsSwing.criarSpinnerDoubleModel( 0, 10000000000.00, 1 ) );
        spPrecoCompra.setModel( CfMethodsSwing.criarSpinnerDoubleModel( 0.0, 10000000000.00, 0.0 ) );
        limpar_cabecario();
        limpar_rodape();
        cmbFornecedor.setModel( new DefaultComboBoxModel<>( fornecedoresController.getVector() ) );
        cmbAnoEconomico.setModel( new DefaultComboBoxModel<>( anoEconomicoController.getVector() ) );
        usuario = ( TbUsuario ) usuariosController.findById( this.cod_usuario );
        try
        {
            adicionar_encomendas();
        }
        catch ( Exception e )
        {
        }

    }// </editor-fold>   

    private static void adicionar_encomendas()
    {
        try
        {
            List<Compras> list = comprasController.getAllEncomendas();

            if ( list != null )
            {

                lista_model_compras_apuradas.clear();
                for ( int i = 0; i < list.size(); i++ )
                {
                    lista_model_compras_apuradas.addElement( list.get( i ).getCodFact() );
                }
            }

        }
        catch ( Exception e )
        {
        }

    }

//    private void accao_mostrar_dados()
//    {
//        if ( !lista_compras_apuradas.isSelectionEmpty() )
//        {
//            String cod_fact = lista_compras_apuradas.getSelectedValue();
//            encomenda_global = compraDao.findByCodFact( cod_fact );
//            fornecedor = encomenda_global.getFkFornecedor();
//            if ( encomenda_global != null )
//            {
//                /*VISUALIZA O CABEÇARIO - PARTE 1*/
//                lb_ano_encomico.setText( "Ano Económico: " + encomenda_global.getFkAnoEconomico().getDesignacao() );
//                cmbFornecedor.setSelectedItem( encomenda_global.getFkFornecedor().getNome() );
//                lb_fornecedor_endereco.setText( "Endereço: " + encomenda_global.getFkFornecedor().getEndereco() );
//                lb_fornecedor_telefone.setText( "Telefone: " + encomenda_global.getFkFornecedor().getTelefone() );
//                lb_fornecedor_email.setText( "Email: " + encomenda_global.getFkFornecedor().getEmail() );
//
//                /*VISUALIZA O CABEÇARIO - PARTE 2*/
//                lb_numero_artigos.setText( "Nº de artigos: " + String.valueOf( encomenda_global.getItemComprasList().size() ) );
//                lb_data_solicitacao.setText( "Data da solicitação: " + MetodosUtil.getDataString( encomenda_global.getDataCompra() ) );
//                lb_data_apuramento.setText( "Data do apuramento: " + MetodosUtil.getDataString( encomenda_global.getDataCompra() ) );
//
//                adicionar_tabela( encomenda_global.getItemComprasList() );
//
//                txtTotal_AOA_Desconto.setText( CfMethods.formatarComoMoeda( encomenda_global.getDescontoTotal() ) );
//                txtTotal_AOA_Iliquido.setText( CfMethods.formatarComoMoeda( encomenda_global.getTotalGeral() ) );
//                txtTotal_AOA_IVA.setText( CfMethods.formatarComoMoeda( encomenda_global.getTotalIva() ) );
//                txtTotal_AOA_liquido.setText( CfMethods.formatarComoMoeda( encomenda_global.getTotalCompra() ) );
//
//                lbValorPorExtenco.setText(
//                        MetodosUtil.valorPorExtenso( CfMethods.parseMoedaFormatada( txtTotal_AOA_liquido.getText() ), "Kwanza" )
//                );
//            }
//            else
//            {
//                fornecedor = null;
//                limpar_cabecario();
//                limpar_rodape();
//            }
//
//        }
//
//    }
    private void adicionar_tabela( List<ItemCompras> itemCompras )
    {
        DefaultTableModel modelo = ( DefaultTableModel ) tableCompra.getModel();
        //limpa a tabela
        modelo.setRowCount( 0 );
        for ( ItemCompras itemCompra : itemCompras )
        {
            modelo.addRow( new Object[]
            {
                itemCompra.getFkProduto().getCodigo(),
                itemCompra.getFkProduto().getDesignacao(),
                itemCompra.getPrecoCompra(),
                itemCompra.getQuantidade(),
                itemCompra.getFkProduto().getCodUnidade().getAbreviacao(),
                itemCompra.getDesconto(),
                itemCompra.getValorIva(),
                itemCompra.getPrecoCompra() * itemCompra.getQuantidade(),
                itemCompra.getTotal()

            } );
        }
        //acrescentar_um_linha_tabela_blank();

    }

    private static void limpar_cabecario()
    {
//        lb_ano_encomico.setText( "" );
        cmbFornecedor.setSelectedIndex( 0 );
        lb_fornecedor_endereco.setText( "" );
        lb_fornecedor_telefone.setText( "" );
        lb_fornecedor_email.setText( "" );

        lb_numero_artigos.setText( "" );
        lb_data_solicitacao.setText( "" );
        lb_data_apuramento.setText( "" );
        txtCodigoInterno.setText( "" );
        spPrecoCompra.setValue( 0 );
        spQtd.setValue( 0 );

    }

    private static void limpar_rodape()
    {
        txtTotal_AOA_Desconto.setText( "0" );
        txtTotal_AOA_Iliquido.setText( "0" );
        txtTotal_AOA_IVA.setText( "0" );
        txtTotal_AOA_liquido.setText( "0" );
        lbValorPorExtenco.setText( "" );
    }

//    private void mostrar_proximo_codigo_documento()
//    {
//        try
//        {
////            documento = documentoDao.findDocumento( DVML.DOC_NOTA_ENCOMENDA_NE );
//            documento = ( Documento ) documentosController.findById( DVML.DOC_NOTA_ENCOMENDA_NE );
//            doc_prox_cod = documento.getCodUltimoDoc() + 1;
//            prox_doc = documento.getAbreviacao();
//            //FA Série / codigo
//            prox_doc += " " + this.anoEconomico.getSerie() + "/" + doc_prox_cod;
//            lb_proximo_documento.setText( "PRÓXIMO DOC: " + prox_doc );
//        }
//        catch ( Exception e )
//        {
//            e.printStackTrace();
//            documento = null;
//            lb_proximo_documento.setText( "" );
//        }
//    }
    private void mostrar_proximo_codigo_documento()
    {
        try
        {
            this.documento = ( Documento ) documentosController.findById( DVML.DOC_NOTA_ENCOMENDA_NE );
            this.anoEconomico = ( AnoEconomico ) anoEconomicoController.findById( getIdAnoEconomico() );
            this.doc_prox_cod = comprasController.getUltimaContagemByIdDocumentoAndAnoEconomico( getIdDocumento(), getIdAnoEconomico() ) + 1;
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

    private static double getTotalVendaIVASemIncluirDesconto()
    {
        double taxa = 0, total_iva_local = 0, preco_unitario = 0, sub_total_iliquido = 0;
        double qtd = 0;

        DefaultTableModel modelo = ( DefaultTableModel ) tableCompra.getModel();
        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {
            preco_unitario = Double.parseDouble( modelo.getValueAt( i, 2 ).toString() );
            qtd = Double.parseDouble( modelo.getValueAt( i, 3 ).toString() );
            sub_total_iliquido = ( preco_unitario * qtd );
            taxa = Double.parseDouble( modelo.getValueAt( i, 6 ).toString() );
            total_iva_local += ( ( ( sub_total_iliquido ) * ( taxa / 100 ) ) );
        }

        return total_iva_local;
    }

    private static double getTotalVendaIVASemIncluirDesconto( List<ItemCompras> list )
    {
        double taxa = 0, total_iva_local = 0, preco_unitario = 0, sub_total_iliquido = 0;
        double qtd = 0;

        DefaultTableModel modelo = ( DefaultTableModel ) tableCompra.getModel();

        for ( ItemCompras itemCompras : list )
        {
            preco_unitario = itemCompras.getPrecoCompra();
            qtd = itemCompras.getQuantidade();
            sub_total_iliquido = ( preco_unitario * qtd );
            taxa = itemCompras.getValorIva();
            total_iva_local += ( ( ( sub_total_iliquido ) * ( taxa / 100 ) ) );

        }

        return total_iva_local;
    }

    private static double getTotalIliquido()
    {
        DefaultTableModel modelo = ( DefaultTableModel ) tableCompra.getModel();
        int qtd = 0;
        double total_iliquido_local = 0, preco_unitario = 0;

        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {
            preco_unitario = Double.parseDouble( modelo.getValueAt( i, 2 ).toString() );
            qtd = Integer.parseInt( modelo.getValueAt( i, 3 ).toString() );
            total_iliquido_local += ( preco_unitario * qtd );

        }

        return total_iliquido_local;
    }

    private static double getTotalIliquido( List<ItemCompras> lista )
    {
        DefaultTableModel modelo = ( DefaultTableModel ) tableCompra.getModel();
        double qtd = 0;
        double total_iliquido_local = 0, preco_unitario = 0;

        for ( ItemCompras itemCompras : lista )
        {
            preco_unitario = itemCompras.getPrecoCompra();
            qtd = itemCompras.getQuantidade();
            total_iliquido_local += ( preco_unitario * qtd );
        }

        return total_iliquido_local;
    }

    private static double getGrossTotal()
    {
        System.out.println( "TOTALILIQUIDO: " + getTotalVendaIVASemIncluirDesconto() );
        System.out.println( "TOTALVENDAIVASEMDESCONTO: " + getTotalVendaIVASemIncluirDesconto() );
        return getTotalIliquido() + getTotalVendaIVASemIncluirDesconto();
    }

    private static double getGrossTotal( List<ItemCompras> lista )
    {
        System.out.println( "TOTALILIQUIDO: " + getTotalVendaIVASemIncluirDesconto( lista ) );
        System.out.println( "TOTALVENDAIVASEMDESCONTO: " + getTotalVendaIVASemIncluirDesconto( lista ) );
        return getTotalIliquido( lista ) + getTotalVendaIVASemIncluirDesconto( lista );
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

        addWindowListener( new WindowAdapter()
        {
            @Override
            public void windowActivated( WindowEvent e )
            {
                mostrar_proximo_codigo_documento();
            }

        } );

    }

//    private static boolean salvar_item( List<ItemCompras> itens )
//    {
//        int last_compra = compraDao.getLastCompra();
//        Compras encomenda = compraDao.findCompras( last_compra );
//        for ( ItemCompras linha_local : itens )
//        {
//            try
//            {
//                linha_local.setFkCompra( encomenda );
//                itemComprasDao.create( linha_local );
//
//            }
//            catch ( Exception e )
//            {
//                e.printStackTrace();
//                return false;
//            }
//        }
//        return true;
//    }
    private static boolean salvar_item()
    {
        boolean sucesso = true;

        if ( !MetodosUtil.tabela_vazia( tableCompra ) )
        {
            Integer last_compra = comprasController.getLastCompras().getPkCompra();
            Compras compra = ( Compras ) comprasController.findById( last_compra );
            StoksController stocksControllerLocal = new StoksController( conexaoTransaction );
            PrecosController precosControllerLocal = new PrecosController( conexaoTransaction );
            ItemComprasController itemComprasControllerLocal = new ItemComprasController( conexaoTransaction );
            ItemCompras itemCompraLocal;
            for ( int i = 0; i < tableCompra.getRowCount(); i++ )
            {
                try
                {

                    double qtd = Double.parseDouble( String.valueOf( tableCompra.getModel().getValueAt( i, 3 ) ) );
                    TbProduto produto_local = ( TbProduto ) produtosController
                            .findById( Integer.parseInt( String.valueOf( tableCompra.getModel().getValueAt( i, 0 ) ) )
                            );

                    itemCompraLocal = new ItemCompras();
                    itemCompraLocal.setFkProduto( produto_local );
                    itemCompraLocal.setPrecoCompra( Double.parseDouble( String.valueOf( tableCompra.getModel().getValueAt( i, 2 ) ) ) );
                    itemCompraLocal.setQuantidade( Double.parseDouble( String.valueOf( tableCompra.getModel().getValueAt( i, 3 ) ) ) );
                    itemCompraLocal.setDesconto( Double.parseDouble( String.valueOf( tableCompra.getModel().getValueAt( i, 5 ) ) ) );
                    itemCompraLocal.setValorIva( Double.parseDouble( String.valueOf( tableCompra.getModel().getValueAt( i, 6 ) ) ) );
                    itemCompraLocal.setTotal( Double.parseDouble( String.valueOf( tableCompra.getModel().getValueAt( i, 8 ) ) ) );
                    itemCompraLocal.setMotivoIsensao( getMotivoIsensao( itemCompraLocal.getFkProduto().getCodigo() ) );
                    itemCompraLocal.setCodigoIsensao( MetodosUtil.getCodigoRegime( itemCompraLocal.getFkProduto().getCodigo() ) );
                    itemCompraLocal.setFkCompra( new Compras( last_compra ) );
                    //cria o item compra
                    if ( !itemComprasControllerLocal.salvar( itemCompraLocal ) )
                    {
                        DocumentosController.rollBackTransaction( conexaoTransaction );
                        conexaoTransaction.close();
                        return false;
                    }

                }
                catch ( Exception e )
                {
                    sucesso = false;
                    e.printStackTrace();
                    JOptionPane.showMessageDialog( null, "Falha ao registrar a compra", "Falha", JOptionPane.ERROR_MESSAGE );
                    DocumentosController.rollBackTransaction( conexaoTransaction );
                    conexaoTransaction.close();
                    return false;

                }

            }

            if ( sucesso )
            {

                DocumentosController.commitTransaction( conexaoTransaction );
                limpar_cabecario();
                limpar_rodape();
                esvaziar_tabela();
                compra = null;
                fornecedor = null;
//                JOptionPane.showMessageDialog( null, "Compra efectuada com sucesso!.." );
//                exibirReciboDeCompras();
                conexaoTransaction.close();
                return true;

            }
        }
        JOptionPane.showMessageDialog( null, "Adiciona itens na tabela caro usuário", "AVISO", JOptionPane.WARNING_MESSAGE );
        return false;

    }

    private static int getIdArmazem()
    {
        return 1;
    }

    private static void esvaziar_tabela()
    {
        DefaultTableModel modelo = ( DefaultTableModel ) tableCompra.getModel();
        modelo.setRowCount( 0 );
    }

//    private static void actualizar_status()
//    {
//        try
//        {
////            String cod_fact = lista_compras_apuradas.getSelectedValue();
//            Compras compra_local = compraDao.findByCodFact( cod_fact );
//            compra_local.setDespachoEncomenda( Boolean.TRUE );
//            compraDao.edit( compra_local );
//        }
//        catch ( Exception e )
//        {
//            e.printStackTrace();
//        }
//
//    }
    private static void mostrar_fornecedor()
    {

        limpar_cabecario();
        if ( fornecedor != null )
        {
            cmbFornecedor.setSelectedItem( fornecedor.getNome() );
            lb_fornecedor_endereco.setText( "Endereço: " + fornecedor.getEndereco() );
            lb_fornecedor_telefone.setText( "Telefone: " + fornecedor.getTelefone() );
            lb_fornecedor_email.setText( "Email: " + fornecedor.getEmail() );
        }
        else
        {
            fornecedor = null;
            //cmbFornecedor.setSelectedIndex( 0 );
        }

    }

    private void busca_produto_by_cod_barra()
    {
//        String codBarra = getCodBarra( txtCodBarra.getText().trim());
//        
//
//        Integer codBarra_int = ( codBarra.isEmpty() ? 0 : Integer.parseInt( codBarra ) );
//        
//        
//        System.err.println( "busca_produto_by_cod_barra: " );
//        if ( !codBarra.equals( "" ) )
//        {
//            System.err.println( "codBarra: " + codBarra );
//            
//            preco = ( TbPreco ) precosController.getLastIdPrecoByIdProdutos( Integer.parseInt(String.valueOf(txtCodBarra ) ));
//
//            produto = ( TbProduto ) produtosController.findByCodBarra( codBarra );
//            if ( produto.getCodigo() != 0 )
//            {
//                lb_produto.setText( produto.getDesignacao() );
//                txtCodigoInterno.setText( String.valueOf(produto.getCodigo() ) );
//                spPrecoCompra.setValue( preco.getPrecoCompra() );
//                txtPrecoVenda.setText( String.valueOf( preco.getPrecoVenda() ) );
//            }
//            else
//            {
//                lb_produto.setText( "" );
//                txtPrecoVenda.setText( "" );
//                spPrecoCompra.setValue( 0 );
//                txtCodBarra.requestFocus();
//            }
//            mostrar_dados_stock( produto );
//            txtQtdEntrar.setText( "" );
//            txtQtdEntrar.requestFocus();
//
//        }
//        else
//        {
//            System.err.println( "codBarra: " + codBarra );
//            lb_produto.setText( "" );
//            txtCodBarra.requestFocus();
//        }
//        System.err.println( "codInterno: " + produto.getCodigo() );
//        System.err.println( "codBarra: " + codBarra );
//        System.err.println( "Preco Comprar: " + preco.getPrecoCompra() );
//        System.err.println( "Preco Venda: " + preco.getPrecoVenda() );

        String codBarra = txtCodBarra.getText().trim();
//        Integer codBarraInt = ( codBarra.isEmpty() ? 0 : Integer.parseInt( codBarra ) );
        System.err.println( "busca_produto_by_cod_barra: " );
//        produto = ( TbProduto ) produtosController.findById( codBarraInt );
        produto = ( TbProduto ) produtosController.findByCodBarra( codBarra );
        preco = precosController.getLastIdPrecoByIdProdutos( produto.getCodigo() );

        if ( produto.getCodigo() != 0 )
        {
            lb_produto.setText( produto.getDesignacao() );
            txtCodigoInterno.setText( String.valueOf( produto.getCodigo() ) );
            spPrecoCompra.setValue( preco.getPrecoCompra() );
        }
        else
        {
            lb_produto.setText( "" );
            spPrecoCompra.setValue( 0 );
            txtCodBarra.requestFocus();
        }

//        mostrar_dados_stock( produto );
//                String codBarra = txtCodBarra.getText().trim();
//        System.err.println( "busca_produto_by_cod_barra: " );
//        if ( !codBarra.equals( "" ) )
//        {
//            System.err.println( "codBarra: " + codBarra );
////            Long cod_barra = Long.parseLong ( codBarra );
//            produto = produtoDao.getProdutoByCodigoBarra( codBarra );
//            if ( produto.getCodigo() != 0 )
//            {
//                procedimento_actualizar_quantidade();
//                lb_produto.setText( produto.getDesignacao() );
////                txtCodManual.setText( produto.getCodigoManual() );
//                TbPreco preco = precoDao.getLastPrecoByIdProduto( produto.getCodigo() );
//                spPrecoCompra.setValue( preco.getPrecoCompra() );
//            }
//            else
//            {
//                lb_produto.setText( "" );
//                txtCodBarra.requestFocus();
//
//            }
//
//        }
//        else
//        {
//            System.err.println( "codBarra: " + codBarra );
//            lb_produto.setText( "" );
//            txtCodBarra.requestFocus();
//        }
    }

    private void busca_produto_by_cod_interno()
    {

        String codInternoString = txtCodigoInterno.getText();
        Integer codigoInternoInt = ( codInternoString.isEmpty() ? 0 : Integer.parseInt( codInternoString ) );
        System.err.println( "busca_produto_by_cod_barra: " );
        preco = precosController.getLastIdPrecoByIdProdutos( codigoInternoInt );
        System.err.println( "codInternoString: " + codInternoString );
        produto = ( TbProduto ) produtosController.findById( codigoInternoInt );
        if ( produto.getCodigo() != 0 )
        {
            lb_produto.setText( produto.getDesignacao() );
            txtCodBarra.setText( String.valueOf( produto.getCodBarra() ) );
            spPrecoCompra.setValue( preco.getPrecoCompra() );
//            txtPrecoVenda.setText( String.valueOf( preco.getPrecoVenda() ) );
        }
        else
        {
            lb_produto.setText( "" );
//            txtPrecoVenda.setText( "" );
            spPrecoCompra.setValue( 0 );
            txtCodBarra.requestFocus();
        }

//        mostrar_dados_stock( produto );
    }

    public int getCodigoArmazem()
    {
        return 1;
    }

    private void procedimento_actualizar_quantidade()
    {
//        Stock stock = stockDao.get_stock_by_id_produto_and_id_armazem(getIdCodigoProduto(), getIdArmazem());
//        txtQuatidadeExistente.setText(stock.getQuantidadeExistente().toString());
        System.out.println( "  " + getCodigoProduto() + " - " + getCodigoArmazem() );

    }

    public static void busca_produto_by_cod_interno( int codProduto )
    {

        produto = produtosController.findByCod( codProduto );

        TbPreco preco_local = precosController.getLastIdPrecoByIdProdutos( codProduto );

//       produto = produtosController.findByCod(codProduto );
        if ( !Objects.isNull( produto ) )
        {
            txtCodigoInterno.setText( String.valueOf( produto.getCodigo() ) );
            lb_produto.setText( produto.getDesignacao() );
            txtCodManual.setText( produto.getCodigoManual() );
            spPrecoCompra.setValue( preco_local.getPrecoCompra() );
        }
        else
        {
            lb_produto.setText( "" );
            txtCodBarra.requestFocus();
        }

//        if ( produto.getCodigo() != 0 )
//        {
//            lb_produto.setText( produto.getDesignacao() );
//            txtCodManual.setText( produto.getCodigoManual() );
////            txtCodBarra.setText( produto.getCodBarra() );
//            spPrecoCompra.requestFocus();
//            txtCodigoInterno.setText( String.valueOf( produto.getCodigo() ) );
//        }
//        else
//        {
//            lb_produto.setText( "" );
//            txtCodManual.setText( "" );
//            txtCodBarra.setText( "" );
//            txtCodigoInterno.setText( "" );
//            txtCodBarra.requestFocus();
//        }
    }

    private static void busca_produto_by_cod_manual()
    {

        String codManual = txtCodManual.getText();

        if ( !codManual.equals( "" ) )
        {

            produto = produtosController.findByCodManual( codManual );
            if ( produto.getCodigo() != 0 )
            {
                lb_produto.setText( produto.getDesignacao() );
                txtCodBarra.setText( String.valueOf( produto.getCodBarra() ) );
//                TbPreco preco = precoDao.getLastPrecoByIdProduto( produto.getCodigo() );
                TbPreco preco = precosController.getLastIdPrecoByIdProdutos( produto.getCodigo() );
                spPrecoCompra.setValue( preco.getPrecoCompra() );

            }
            else
            {
                lb_produto.setText( "" );
                txtCodBarra.requestFocus();

            }

        }
        else
        {
            lb_produto.setText( "" );
            txtCodBarra.requestFocus();
        }

    }

    public void adicionarProdutoATabelaPrincipal()
    {

        Integer codProdutoInt = ( Integer ) tabela_busca.getValueAt( tabela_busca.getSelectedRow(), 0 );
        TbProduto produto = ( TbProduto ) produtosController.findById( codProdutoInt );
        adicionar_produto( produto );

    }

    public void adicionarProdutoATabelaPrincipalPeloBotao()
    {

        TbProduto produto_local = ( TbProduto ) produtosController.findByDesignacao( lb_produto.getText() );

        adicionar_produto( produto_local );

    }

    public void adicionar_produto( TbProduto produto )
    {

        System.err.println( "Adicionar: " + produto.getCodigo() );
        System.err.println( "produto: " + produto.getDesignacao() );
        DefaultTableModel modelo = ( DefaultTableModel ) tableCompra.getModel();

//        double total_deconto = 0d;
        if ( !exist_produto_tabela_formulario( tableCompra, produto.getCodigo() ) )
        {
            System.err.println( "ProdutoExiste" );
//            if ( !validar_zero() && validar_zero_preco() )
            if ( validar_zero_qtd_preco() )
            {
                modelo.addRow( new Object[]
                {
                    produto.getCodigo(),
                    produto.getDesignacao(),
                    getPrecoCompra(),
                    getQuantidade(),
                    produto.getCodUnidade().getAbreviacao(),
                    0,
                    getTaxaImposto(),
                    getQuantidade() * getPrecoCompra(),
                    MetodosUtil.getValorComIVA(
                    getQuantidade(),
                    getTaxaImposto(),
                    getPrecoCompra(),
                    0
                    )

                } );

            }
//            else
//            {
//                // JOptionPane.showMessageDialog ( null, "Atenção\nA quantidade a sair não pode ser igual a zero!" );
//            }

        }
        else
        {
            System.err.println( "Produto não existe" );
            System.out.println( "Linha Actual: " + linha_actual );
            actuazlizar_quantidade_tabela_formulario( String.valueOf( getQuantidade() ), 0.0 );
        }

        actualizar_total();

    }

    public void adicionar_produto_a_busca( TbStock stock )
    {

        System.err.println( "Adicionar: " + stock.getCodProdutoCodigo().getCodigo() );
        System.err.println( "produto: " + stock.getCodProdutoCodigo().getDesignacao() );
        DefaultTableModel modelo = ( DefaultTableModel ) tabela_busca.getModel();

//        double total_deconto = 0d;
        if ( !exist_produto_tabela_formulario( tabela_busca, stock.getCodigo() ) )
        {
            System.err.println( "ProdutoExiste" );
            if ( !validar_zero() )
            {
                modelo.addRow( new Object[]
                {
                    stock.getCodProdutoCodigo().getCodigo(),
                    stock.getCodProdutoCodigo().getDesignacao(),
                    stock.getCodProdutoCodigo().getFkGrupo().getDesignacao(),
                    stock.getQuantidadeExistente()

                } );

            }
            else
            {
                JOptionPane.showMessageDialog( null, "Atenção\nA quantidade a sair não pode ser igual a zero!" );
            }

        }
        else
        {
            System.err.println( "Produto não existe" );
            System.out.println( "Linha Actual: " + linha_actual );
            actuazlizar_quantidade_tabela_formulario( String.valueOf( getQuantidade() ), 0.0 );
        }

        actualizar_total();

    }

    public static boolean validar_zero()
    {
        if ( Double.parseDouble( spQtd.getValue().toString() ) == 0 )
        {
            JOptionPane.showMessageDialog( null, "Atenção\nA quantidade a sair não pode ser igual a zero!" );

            return false;
        }
        return true;
    }

    public static boolean validar_zero_preco()
    {
        if ( Double.parseDouble( spPrecoCompra.getValue().toString() ) == 0 )
        {
            JOptionPane.showMessageDialog( null, "Atenção\nO preço não pode ser igual a zero!" );

            return false;
        }
        return true;

    }

    public static boolean validar_zero_qtd_preco()
    {
        if ( Double.parseDouble( spQtd.getValue().toString() ) == 0 )
        {
            JOptionPane.showMessageDialog( null, "Atenção\nA quantidade a entrar não pode ser igual a zero!" );

            return false;
        }

        else if ( Double.parseDouble( spPrecoCompra.getValue().toString() ) == 0 )
        {
            JOptionPane.showMessageDialog( null, "Atenção\nO preço não pode ser igual a zero!" );

            return false;
        }
        return true;

    }

    public static String getUnidade_Produto()
    {
        return produto.getCodUnidade().getAbreviacao();
    }

    public static Integer getQuantidade()
    {
        System.err.println( "QUANTIDADE DIGTADA:" + spQtd.getValue() );
        return ( int ) Double.parseDouble( spQtd.getValue().toString() );
    }

    private static double getPrecoCompra()
    {
        return Double.parseDouble( spPrecoCompra.getValue().toString() );
    }

    private static void actuazlizar_quantidade_tabela_formulario( String qtd, double desconto )
    {
        DefaultTableModel modelo = ( DefaultTableModel ) tableCompra.getModel();

        if ( linha_actual >= 0 && linha_actual < modelo.getRowCount() )
        {
            double preco_compra = Double.parseDouble( String.valueOf( modelo.getValueAt( linha_actual, 2 ) ) );
            Integer quantidade = Integer.parseInt( qtd );
            double taxa = Double.parseDouble( String.valueOf( modelo.getValueAt( linha_actual, 6 ) ) );
            double total_item = preco_compra * quantidade;
            double total_item_com_iva = MetodosUtil.getValorComIVA( quantidade, taxa, preco_compra, desconto );

            modelo.setValueAt( preco_compra, linha_actual, 2 );
            modelo.setValueAt( quantidade, linha_actual, 3 );
            modelo.setValueAt( desconto, linha_actual, 5 );
            modelo.setValueAt( taxa, linha_actual, 6 );
            modelo.setValueAt( total_item, linha_actual, 7 );
            modelo.setValueAt( total_item_com_iva, linha_actual, 8 );
            System.out.println( "Linha Actual: " + linha_actual );

            linha_actual = -1;
        }

    }

    private static double getTaxaImposto()
    {

        if ( fornecedor != null )
        {
            try
            {

                return fornecedor.getFkRegime().getValor();
            }
            catch ( Exception e )
            {
                return 0;
            }
        }
        return 0;

    }

    private static void actualizar_total()
    {
        txtTotal_AOA_Iliquido.setText( CfMethods.formatarComoMoeda( getTotalIliquido() ) );
        txtTotal_AOA_IVA.setText( CfMethods.formatarComoMoeda( getTotalIVASobreDesconto() ) );
        txtTotal_AOA_Desconto.setText( CfMethods.formatarComoMoeda( getDescontoComercial() + getDescontoFinanceiro() ) );
        txtTotal_AOA_liquido.setText( CfMethods.formatarComoMoeda( getTotalAOALiquido() ) );

        valor_por_extenco();
    }

    private void procedimento_salvar_compra()
    {
        int opcao = JOptionPane.showConfirmDialog( null, "Deseja mesmo realizar a encomenda?" );

        if ( opcao != JOptionPane.YES_OPTION )
        {
            return;
        }
        if ( !MetodosUtil.tabela_vazia( tableCompra ) )
        {
            if ( fornecedor != null )
            {
                try
                {
                    if ( salvar_compra() )
                    {
                        if ( salvar_item() )
                        {
                            //actualizar_status();
                            actualizar_cod_doc();
                            limpar_cabecario();
                            limpar_rodape();
                            esvaziar_tabela();
                            adicionar_encomendas();
                            encomenda_global = null;
                            fornecedor = null;

                            exibirReciboDeCompras();

                        }
                        else
                        {
                            JOptionPane.showMessageDialog( null, "Falha ao criar o documento", "Falha", JOptionPane.ERROR_MESSAGE );
                        }
                        procedimento_actualizar_quantidade();
                    }

                }
                catch ( HeadlessException e )
                {
                    JOptionPane.showMessageDialog( null, "Falha ao criar o documento", "Falha", JOptionPane.ERROR_MESSAGE );
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

    public static boolean salvar_compra()
    {
        conexaoTransaction = new BDConexao();
        DocumentosController.startTransaction( conexaoTransaction );

        Date data_documento = new Date();

        Compras encomenda_local = new Compras();

        System.err.println( "#salvar_compra: " );

        encomenda_local.setDataCompra( data_documento );
        Calendar calendar = Calendar.getInstance();
        calendar.setTime( data_documento );
        //adicionar 15 dias na data do documento.
        calendar.add( Calendar.DATE, 15 );
        encomenda_local.setDataLimiteLevantamento( calendar.getTime() );
        encomenda_local.setFornecedorNif( fornecedor.getNif() );
        encomenda_local.setNomeFornecedor( fornecedor.getNome() );

        //Total Ilíquido
        encomenda_local.setTotalGeral( getTotalIliquido() );
        //desconto por linha
        encomenda_local.setDescontoComercial( getDescontoComercial() );
        //imposto
        //calculaTotalIVA();
        encomenda_local.setTotalIva( getTotalImposto() );
        //desconto global

        encomenda_local.setDescontoFinanceiro( getDescontoFinanceiro() );
        //Total(AOA) <=> Total Líquido
        encomenda_local.setTotalCompra( getTotalAOALiquido() );
        encomenda_local.setTotalIncidencia( getTotalIncidencia() );
        encomenda_local.setTotalIncidenciaIsento( getTotalIncidenciaIsento() );

        /*outros campos*/
        encomenda_local.setDescontoTotal( getDescontoComercial() + getDescontoFinanceiro() );
//        encomenda_local.setIdArmazemFK( armazemDao.findTbArmazem( DVML.ARMAZEM_DEFAUTL ) );
        encomenda_local.setIdArmazemFK( ( TbArmazem ) armazensController.findById( DVML.ARMAZEM_DEFAUTL ) );
        encomenda_local.setFkAnoEconomico( anoEconomico );
        encomenda_local.setFkDocumento( documento );
//        encomenda_local.setFkFornecedor( FornecedorDao.findByNome( ( String ) cmbFornecedor.getSelectedItem() ) );
        encomenda_local.setFkFornecedor( ( TbFornecedor ) fornecedoresController.findByName( ( String ) cmbFornecedor.getSelectedItem() ) );
        encomenda_local.setCodigoUsuario( usuario );
        encomenda_local.setCodFact( prox_doc );
        encomenda_local.setRefCodFact( "" );
//        compra_local.setHashCod( MetodosUtil.criptografia_hash( prox_doc ) );
        encomenda_local.setHashCod( MetodosUtil.criptografia_hash( encomenda_local, getGrossTotal(), conexao ) );
        encomenda_local.setStatusEliminado( "false" );
//        encomenda_local.setTotalPorExtenso( MetodosUtil.iniciais_extenso( documento.getPkDocumento(), documentoDao ) + lbValorPorExtenco.getText() );
        encomenda_local.setTotalPorExtenso( MetodosUtil.iniciais_extenso_comercial( documento.getPkDocumento(), documentosController ) + lbValorPorExtenco.getText() );
        System.out.println( "STATUS:hash cod processado." );
        encomenda_local.setAssinatura( MetodosUtil.assinatura_doc( encomenda_local.getHashCod() ) );
        encomenda_local.setDespachoEncomenda( false );

        try
        {
            if ( comprasController.salvar( encomenda_local ) )
            {
//                System.err.println( "Entrei no salvar" );
                Integer last_compra = comprasController.getLastCompras().getPkCompra();

                if ( Objects.isNull( last_compra ) || last_compra == 0 )
                {
                    DocumentosController.rollBackTransaction( conexaoTransaction );
                    conexaoTransaction.close();
                    return false;
                }
//                if ( !Objects.isNull( last_compra ) )
//                {
//                    return salvar_item(last_compra );
//                }
//                else
//                {
//                    return false;
//                }

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
            DocumentosController.rollBackTransaction( conexaoTransaction );
            conexaoTransaction.close();

        }
        return false;

    }

    private static double getTotalImposto()
    {
        DefaultTableModel modelo = ( DefaultTableModel ) tableCompra.getModel();
        int qtd = 0;
        double imposto = 0d, preco_unitario = 0d, desconto_valor_linha = 0;

        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {
            preco_unitario = Double.parseDouble( modelo.getValueAt( i, 2 ).toString() );
            qtd = Integer.parseInt( modelo.getValueAt( i, 3 ).toString() );
            double valor_percentagem = Double.parseDouble( modelo.getValueAt( i, 5 ).toString() );
            double taxa = Double.parseDouble( modelo.getValueAt( i, 6 ).toString() );
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
        int qtd = 0;
        double desconto_comercial = 0d, preco_unitario = 0d, desconto_valor_linha = 0d;

        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {
            preco_unitario = Double.parseDouble( modelo.getValueAt( i, 2 ).toString() );
            qtd = Integer.parseInt( modelo.getValueAt( i, 3 ).toString() );
            double valor_percentagem = Double.parseDouble( modelo.getValueAt( i, 5 ).toString() );
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
        int qtd = 0;
        double incidencia = 0d, preco_unitario = 0d, desconto_valor_linha = 0;

        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {
            preco_unitario = Double.parseDouble( modelo.getValueAt( i, 2 ).toString() );
            qtd = Integer.parseInt( modelo.getValueAt( i, 3 ).toString() );
            double valor_percentagem = Double.parseDouble( modelo.getValueAt( i, 5 ).toString() );
            double taxa = Double.parseDouble( modelo.getValueAt( i, 6 ).toString() );
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
        int qtd = 0;
        double incidencia_isento = 0d, preco_unitario = 0d, desconto_valor_linha = 0;

        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {
            preco_unitario = Double.parseDouble( modelo.getValueAt( i, 2 ).toString() );
            qtd = Integer.parseInt( modelo.getValueAt( i, 3 ).toString() );
            double valor_percentagem = Double.parseDouble( modelo.getValueAt( i, 5 ).toString() );
            double taxa = Double.parseDouble( modelo.getValueAt( i, 6 ).toString() );
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
        double preco_compra = Double.parseDouble( String.valueOf( modelo.getValueAt( linha_actual, 2 ) ) );
        Integer quantidade = Integer.parseInt( String.valueOf( modelo.getValueAt( linha_actual, 3 ) ) );

        if ( preco_compra != 0 && quantidade != 0 )
        {
            double desconto = Double.parseDouble( String.valueOf( modelo.getValueAt( linha_actual, 5 ) ) );
            if ( desconto >= 0 && desconto <= 100 )
            {
                double taxa = Double.parseDouble( String.valueOf( modelo.getValueAt( linha_actual, 6 ) ) );

                double total_item = preco_compra * quantidade;
                double total_item_com_iva = MetodosUtil.getValorComIVA( quantidade, taxa, preco_compra, desconto );

                modelo.setValueAt( preco_compra, linha_actual, 2 );
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

    private static void acrescentar_um_linha_tabela_blank()
    {
        /**
         * produto.getCodigo(), produto.getDesignacao(), getPrecoCompra(),
         * getQuantidade(), getUnidade_Produto(), 0, getTaxaImposto(),
         * getQuantidade() * getPrecoCompra(), MetodosUtil.getValorComIVA(
         * getQuantidade(), getTaxaImposto(), getPrecoCompra(), 0 )
         */
        DefaultTableModel modelo = ( DefaultTableModel ) tableCompra.getModel();
        modelo.addRow( new Object[]
        {
            0,
            "",
            0d,
            0,
            "Un",
            0d,
            0d

        } );

    }

    private static double getTotalIVASobreDesconto()
    {

        DefaultTableModel modelo = ( DefaultTableModel ) tableCompra.getModel();
        double iva = 0, preco = 0, desconto = 0;
        int qtd = 0;
        total_iva = 0;
        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {

            preco = Double.parseDouble( modelo.getValueAt( i, 2 ).toString() );
            qtd = Integer.parseInt( modelo.getValueAt( i, 3 ).toString() );
            desconto = Double.parseDouble( modelo.getValueAt( i, 5 ).toString() );
            iva = Double.parseDouble( modelo.getValueAt( i, 6 ).toString() );

            total_iva += getIVA( qtd, iva, preco, desconto );
        }

        // System.err.println("Com o método: " + MetodosUtil.valorCasasDecimaisNovo(total_iva, CASAS_DECIMAIS));
        return total_iva;
        //total_iva = MetodosUtil.valorCasasDecimaisNovo( total_iva, CASAS_DECIMAIS );

    }

    private static double getValorComIVA( int qtd, double taxa, double preco_venda, double desconto )
    {
        double subtotal_linha = ( preco_venda * qtd );
        double desconto_valor = ( subtotal_linha * ( desconto / 100 ) );
        double valor_iva = 1 + ( taxa / 100 );//
        return ( ( subtotal_linha - desconto_valor ) * valor_iva );

    }

    private static double getIVA( int qtd, double taxa, double preco_venda, double desconto )
    {
        double subtotal_linha = ( preco_venda * qtd );
        double desconto_valor = ( subtotal_linha * ( desconto / 100 ) );
        double valor_iva = ( taxa / 100 );//
        return ( ( subtotal_linha - desconto_valor ) * valor_iva );

    }

    private static void valor_por_extenco()
    {
        lbLegenda.setText( MetodosUtil.iniciais_extenso( documento.getPkDocumento(), documentoDao ) );
        System.out.println( "Total_Liquido" + txtTotal_AOA_liquido.getText() );
        lbValorPorExtenco.setText( MetodosUtil.valorPorExtenso( CfMethods.parseMoedaFormatada( txtTotal_AOA_liquido.getText() ), "Kwanza" ) );
    }

    private static void exibirReciboDeCompras()
    {
        try
        {
            Compras lastCompra = new ComprasDao( emf ).findLastRow();

            Connection connection = ( Connection ) BDConexao.getConexao();
            HashMap hashMap = new HashMap();
            hashMap.put( "PK_COMPRA", lastCompra.getPkCompra() );
            hashMap.put( "PK_COMPRA", lastCompra.getPkCompra() );
            String relatorio = CAMINHO_REPORT + "compra_recibo.jasper";

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
            Logger.getLogger( NovaEncomendaVisao.class.getName() ).log( Level.SEVERE, null, ex );
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

//        String armazemSelecionado = ( String ) cmbArmazem.getSelectedItem();
//        TbArmazem armazem = ArmazemDao.findByDesigncao( armazemSelecionado );
//        ProdutoDao.findProdutoByCodigoBarra( String.valueOf( cod_usuario ) );
//        List<TbStock> stocks = StockDao.findByProdutoWhitDesignacaoLike( armazem.getCodigo(), designacao );
//        stocks = ( designacao.isEmpty() ) ? StockDao.findAllStock() : stocks;
//        DefaultTableModel modelo = ( DefaultTableModel ) tabela_busca.getModel();
//
//        modelo.setRowCount( 0 );
//        if ( !Objects.isNull( stocks ) )
//        {
//            for ( TbStock stock : stocks )
//            {
//                modelo.addRow(
//                        new Object[]
//                        {
//                            stock.getCodProdutoCodigo().getCodigo(),
//                            stock.getCodProdutoCodigo().getDesignacao(),
//                            stock.getCodProdutoCodigo().getCodTipoProduto().getDesignacao(),
//                            stock.getQuantidadeExistente()
//                        }
//                );
//
//            }
//        }
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

    public void accao_codigo_interno_enter_busca_exterior( int codigo )
    {

        try
        {

            //int   codigo = Integer.parseInt(txtCodigoProduto.getText() );
//            TbProduto produto = produtoDao.findTbProduto( codigo );
            TbProduto produto = produtosController.findByCod( codigo );
            adicionar_produto( produto );

        }
        catch ( Exception ex )
        {
            //  ex.printStackTrace();
            Logger.getLogger( NovaEncomendaVisao.class.getName() ).log( Level.SEVERE, null, ex );
//            JOptionPane.showMessageDialog( null, "Este produto não existe no armazém " + cmbArmazem.getSelectedItem(), DVML.DVML_COMERCIAL, JOptionPane.ERROR_MESSAGE );
        }

    }
//

    private void exibirDialogoPesquisaProduto()
    {
//        produtoPesquisaJDialog.setSize( 937, 519 );
//        produtoPesquisaJDialog.setLocationRelativeTo( this );
//        produtoPesquisaJDialog.setModal( true );
//        produtoPesquisaJDialog.setModalityType( Dialog.ModalityType.APPLICATION_MODAL );
//        produtoPesquisaJDialog.setVisible( true );

    }

    private boolean exist_produto_tabela_formulario( JTable tabela_busca, Integer codigo )
    {
        DefaultTableModel modelo = ( DefaultTableModel ) tabela_busca.getModel();

        if ( modelo.getRowCount() == 0 )
        {
            return false;
        }

        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {
            System.err.println( "Codigo: " + modelo.getValueAt( i, 0 ) );
            System.err.println( "Designação: " + modelo.getValueAt( i, 1 ) );
            if ( modelo.getValueAt( i, 0 ) == codigo )
            {
                return true;
            }
        }

        return false;
    }

    public JFrame getInstance()
    {
        return this;
    }

//    private static void actualizarPreco( int idProduto, Double precoCompra )
//    {
//        int idPrecoRetalho = precoDao.getUltimoIdPrecoByIdProduto( idProduto, 1.0 );
//        int idPrecoGrosso = precoDao.getUltimoIdPrecoByIdProduto( idProduto, DVML.QTD_DEFAULT );
//
//        TbPreco precoAntigoRetalho = precoDao.findTbPreco( idPrecoRetalho );
//        TbPreco precoAntigoGrosso = precoDao.findTbPreco( idPrecoGrosso );
//
//        TbPreco preco_novo_retalho;
//        TbPreco preco_novo_grosso;
//
//        preco_novo_retalho = precoAntigoRetalho;
//
//        preco_novo_retalho.setPrecoCompra( new BigDecimal( precoCompra ) );
//        try
//        {
//            precoDao.create( preco_novo_retalho );
//            System.out.println( "Preco de compra retalho actualizado na compra" );
//        }
//        catch ( Exception e )
//        {
//            e.printStackTrace();
//            System.err.println( "Falha ao actualizar o preço retalho na compra" );
//        }
//
//        preco_novo_grosso = precoAntigoGrosso;
//        preco_novo_grosso.setPrecoCompra( new BigDecimal( precoCompra ) );
//        try
//        {
//            precoDao.create( preco_novo_grosso );
//            System.out.println( "Preco de compra retalho actualizado na compra" );
//        }
//        catch ( Exception e )
//        {
//            e.printStackTrace();
//            System.err.println( "Falha ao actualizar o preço grosso na compra" );
//        }
//
//    }
}
