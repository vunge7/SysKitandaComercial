/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lista;


import java.sql.Connection;
import dao.ArmazemDao;
import dao.ItemVendaDao;
import dao.PrecoDao;
import dao.UsuarioDao;
import dao.VendaDao;
import entity.TbProduto;
import entity.TbVenda;
import comercial.controller.AmortizacaoDividaController;
import comercial.controller.ClientesController;
import comercial.controller.DadosInstituicaoController;
//import comercial.controller.ClientesController;
import comercial.controller.VendasController;
import dao.AccessoArmazemDao;
import entity.TbDadosInstituicao;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import static kitanda.util.CfMethods.formatarComoMoeda;
import tesouraria.novo.util.AnyReport;
import util.BDConexao;
import util.DVML;
import util.DVML.Abreviacao;
import util.JPAEntityMannagerFactoryUtil;
import util.MetodosUtil;
import util.tabela_manual.render.RenderTabelaFA;

/**
 *
 * @author Domingos Dala Vunge
 */
public class ListarRelatorioVenda extends javax.swing.JFrame
{

    /**
     * Creates new form ListaUsuarioVisao
     */
    private EntityManagerFactory emf = JPAEntityMannagerFactoryUtil.em;
    private VendaDao vendaDao = new VendaDao( emf );
    private AccessoArmazemDao accessoArmazemDao = new AccessoArmazemDao( emf );
    private static DadosInstituicaoController dadosInstituicaoController;
    private TbVenda venda;
    private UsuarioDao usuarioDao = new UsuarioDao( emf );
    private ItemVendaDao itemVendaDao = new ItemVendaDao( emf );
    private PrecoDao precoDao = new PrecoDao( emf );
    private ArmazemDao armazemDao = new ArmazemDao( emf );
    private double total_geral = 0;
    private List<TbVenda> lista = null;
    private BDConexao conexao;
    private ClientesController clientesController;
    private VendasController vendasController;
    private Vector<String> lista_all_clientes;
    private AmortizacaoDividaController amortizacaoDividaController;
    private static Abreviacao abreviacao;
    private static TbDadosInstituicao dadosInstituicao;

    public ListarRelatorioVenda( BDConexao conexao, int idUser )
    {

        initComponents();
        setResizable( false );
        setLocationRelativeTo( null );
        tabela_factura.setDefaultRenderer( Object.class, new RenderTabelaFA() );
        this.conexao = conexao;
        amortizacaoDividaController = new AmortizacaoDividaController( conexao );
        dadosInstituicaoController = new DadosInstituicaoController( conexao );
        dadosInstituicao = (TbDadosInstituicao) dadosInstituicaoController.findById( 1 );
        vendasController = new VendasController( conexao );
        dcDataInicio.setDate( new Date() );
        dcDataFim.setDate( new Date() );
        cmbArmazem.setModel( new DefaultComboBoxModel( accessoArmazemDao.getAllArmazemByIdUSuario( idUser ) ) );
//        cmbArmazem.setModel( new DefaultComboBoxModel( armazemDao.buscaTodos3() ) );
        configurar_clientes();
        metodo_radio();
        try
        {
            setFolhaImpressora( dadosInstituicao.getImpressora() );
        }
        catch ( Exception e )
        {
        }

    }

    @SuppressWarnings( "unchecked" )
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        buttonGroup3 = new javax.swing.ButtonGroup();
        buttonGroup4 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        lbData = new javax.swing.JLabel();
        dcDataInicio = new com.toedter.calendar.JDateChooser();
        lbData1 = new javax.swing.JLabel();
        dcDataFim = new com.toedter.calendar.JDateChooser();
        jLabel1 = new javax.swing.JLabel();
        cmbArmazem = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        ck_A4 = new javax.swing.JCheckBox();
        ck_Duplicada = new javax.swing.JCheckBox();
        ck_simplificada = new javax.swing.JCheckBox();
        ck_simplificada_O = new javax.swing.JCheckBox();
        ck_simplificada_OS_A6 = new javax.swing.JCheckBox();
        ck_ComVirgula = new javax.swing.JCheckBox();
        ck_S_A6 = new javax.swing.JCheckBox();
        ck_A7 = new javax.swing.JCheckBox();
        lbData2 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        btnCancelar1 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        lb_total = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabela_factura_recibo = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tabela_factura = new javax.swing.JTable();
        cmbCliente = new javax.swing.JComboBox();
        rb_todos_clientes = new javax.swing.JRadioButton();
        rb_por_cliente = new javax.swing.JRadioButton();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tabela_factura_nc = new javax.swing.JTable();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tabela_recibo = new javax.swing.JTable();
        jPanel10 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        tabela_factura_proforma = new javax.swing.JTable();
        jPanel8 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tabela_factura_geral = new javax.swing.JTable();
        jPanel6 = new javax.swing.JPanel();
        ck_estrato_cliente = new javax.swing.JCheckBox();
        ck_mapa_iva = new javax.swing.JCheckBox();
        ck_relatorio_normal = new javax.swing.JCheckBox();
        jLabel3 = new javax.swing.JLabel();
        ck_ordemFR = new javax.swing.JCheckBox();
        btnCancelar = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("...:::: DVML - RELATORIO DIARIO::...");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        lbData.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 16)); // NOI18N
        lbData.setText("De");

        lbData1.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 16)); // NOI18N
        lbData1.setText("à");

        jLabel1.setText("Armazém");

        cmbArmazem.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/actualizar_1_32x32.png"))); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButton1ActionPerformed(evt);
            }
        });

        buttonGroup4.add(ck_A4);
        ck_A4.setFont(new java.awt.Font("Lucida Grande", 0, 10)); // NOI18N
        ck_A4.setText("A4");
        ck_A4.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ck_A4ActionPerformed(evt);
            }
        });

        buttonGroup4.add(ck_Duplicada);
        ck_Duplicada.setFont(new java.awt.Font("Lucida Grande", 0, 10)); // NOI18N
        ck_Duplicada.setText("A5");
        ck_Duplicada.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ck_DuplicadaActionPerformed(evt);
            }
        });

        buttonGroup4.add(ck_simplificada);
        ck_simplificada.setFont(new java.awt.Font("Lucida Grande", 0, 10)); // NOI18N
        ck_simplificada.setText("A6");
        ck_simplificada.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ck_simplificadaActionPerformed(evt);
            }
        });

        buttonGroup4.add(ck_simplificada_O);
        ck_simplificada_O.setFont(new java.awt.Font("Lucida Grande", 0, 10)); // NOI18N
        ck_simplificada_O.setText("A6_O");
        ck_simplificada_O.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ck_simplificada_OActionPerformed(evt);
            }
        });

        buttonGroup4.add(ck_simplificada_OS_A6);
        ck_simplificada_OS_A6.setFont(new java.awt.Font("Lucida Grande", 0, 10)); // NOI18N
        ck_simplificada_OS_A6.setSelected(true);
        ck_simplificada_OS_A6.setText("S_A6_O");
        ck_simplificada_OS_A6.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ck_simplificada_OS_A6ActionPerformed(evt);
            }
        });

        buttonGroup4.add(ck_ComVirgula);
        ck_ComVirgula.setFont(new java.awt.Font("Lucida Grande", 0, 10)); // NOI18N
        ck_ComVirgula.setText("A6V");
        ck_ComVirgula.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ck_ComVirgulaActionPerformed(evt);
            }
        });

        buttonGroup4.add(ck_S_A6);
        ck_S_A6.setFont(new java.awt.Font("Lucida Grande", 0, 10)); // NOI18N
        ck_S_A6.setText("S_A6");
        ck_S_A6.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ck_S_A6ActionPerformed(evt);
            }
        });

        buttonGroup4.add(ck_A7);
        ck_A7.setFont(new java.awt.Font("Lucida Grande", 0, 10)); // NOI18N
        ck_A7.setText("A7");
        ck_A7.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ck_A7ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(lbData, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dcDataInicio, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbData1, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(dcDataFim, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbArmazem, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(ck_A4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ck_Duplicada)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ck_simplificada)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ck_simplificada_O)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ck_simplificada_OS_A6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(ck_ComVirgula, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ck_S_A6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ck_A7)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 61, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cmbArmazem))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(dcDataInicio, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lbData1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(dcDataFim, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lbData, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(16, 16, 16)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ck_Duplicada)
                    .addComponent(ck_simplificada)
                    .addComponent(ck_simplificada_O)
                    .addComponent(ck_ComVirgula)
                    .addComponent(ck_S_A6)
                    .addComponent(ck_A7)
                    .addComponent(ck_A4)
                    .addComponent(ck_simplificada_OS_A6))
                .addContainerGap())
            .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        lbData2.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 30)); // NOI18N
        lbData2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbData2.setText("RELATÓRIO DE VENDAS POR INTERVALO DE DATAS");

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        btnCancelar1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/LOGOUT - VERMELHO/Logout 32x32.png"))); // NOI18N
        btnCancelar1.setText("Sair");
        btnCancelar1.setAlignmentX(0.5F);
        btnCancelar1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnCancelar1ActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Lucida Grande", 1, 24)); // NOI18N
        jLabel2.setText("TOTAL");

        lb_total.setFont(new java.awt.Font("Lucida Grande", 1, 36)); // NOI18N
        lb_total.setText("0.0");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lb_total, javax.swing.GroupLayout.PREFERRED_SIZE, 379, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 414, Short.MAX_VALUE)
                .addComponent(btnCancelar1, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCancelar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lb_total, javax.swing.GroupLayout.PREFERRED_SIZE, 44, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jTabbedPane1.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                jTabbedPane1MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt)
            {
                jTabbedPane1MouseEntered(evt);
            }
        });

        tabela_factura_recibo.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "Doc. Ref Nº", "Cliente ", "Data", "Hora", "Usuário", "Total"
            }
        )
        {
            Class[] types = new Class []
            {
                java.lang.Object.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean []
            {
                false, false, false, false, false, false
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
        tabela_factura_recibo.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                tabela_factura_reciboMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tabela_factura_recibo);
        if (tabela_factura_recibo.getColumnModel().getColumnCount() > 0)
        {
            tabela_factura_recibo.getColumnModel().getColumn(0).setMinWidth(0);
            tabela_factura_recibo.getColumnModel().getColumn(2).setMaxWidth(100);
            tabela_factura_recibo.getColumnModel().getColumn(3).setMaxWidth(100);
        }

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1160, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 468, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Factura/Recibo - FR", jPanel4);

        tabela_factura.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "Doc. Ref Nº", "Cliente ", "Data", "Hora", "Usuário", "Total Factura", "Total Pago", "Dívida (Por pagar)", "Data Vencimento", "Prazo"
            }
        )
        {
            Class[] types = new Class []
            {
                java.lang.Object.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean []
            {
                false, false, false, false, false, false, false, true, true, true
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
        tabela_factura.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                tabela_facturaMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tabela_factura);
        if (tabela_factura.getColumnModel().getColumnCount() > 0)
        {
            tabela_factura.getColumnModel().getColumn(0).setMinWidth(0);
            tabela_factura.getColumnModel().getColumn(2).setMaxWidth(100);
            tabela_factura.getColumnModel().getColumn(3).setMaxWidth(100);
        }

        cmbCliente.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        cmbCliente.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbCliente.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbClienteActionPerformed(evt);
            }
        });

        buttonGroup1.add(rb_todos_clientes);
        rb_todos_clientes.setText("Todos os Clientes");
        rb_todos_clientes.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                rb_todos_clientesActionPerformed(evt);
            }
        });

        buttonGroup1.add(rb_por_cliente);
        rb_por_cliente.setText("Por Cliente");
        rb_por_cliente.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                rb_por_clienteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(rb_todos_clientes, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rb_por_cliente)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmbCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 496, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1160, Short.MAX_VALUE)))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rb_todos_clientes)
                    .addComponent(rb_por_cliente)
                    .addComponent(cmbCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 457, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Factura - FT", jPanel5);

        tabela_factura_nc.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "Doc. Nº", "Cliente ", "Data", "Hora", "Usuário", "Total", "Ref. DOC"
            }
        )
        {
            Class[] types = new Class []
            {
                java.lang.Object.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean []
            {
                false, false, false, false, false, false, false
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
        tabela_factura_nc.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                tabela_factura_ncMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tabela_factura_nc);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 1160, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 468, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Nota/Crédito - NC", jPanel7);

        tabela_recibo.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "Doc. Ref Nº", "Cliente ", "Data", "Hora", "Usuário", "Total"
            }
        )
        {
            Class[] types = new Class []
            {
                java.lang.Object.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean []
            {
                false, false, false, false, false, false
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
        tabela_recibo.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                tabela_reciboMouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(tabela_recibo);
        if (tabela_recibo.getColumnModel().getColumnCount() > 0)
        {
            tabela_recibo.getColumnModel().getColumn(0).setMinWidth(0);
            tabela_recibo.getColumnModel().getColumn(2).setMaxWidth(100);
            tabela_recibo.getColumnModel().getColumn(3).setMaxWidth(100);
        }

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 1160, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 468, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Recibo - RC", jPanel9);

        tabela_factura_proforma.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "Doc. Ref Nº", "Cliente ", "Data", "Hora", "Usuário", "Total"
            }
        )
        {
            Class[] types = new Class []
            {
                java.lang.Object.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean []
            {
                false, false, false, false, false, false
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
        tabela_factura_proforma.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                tabela_factura_proformaMouseClicked(evt);
            }
        });
        jScrollPane6.setViewportView(tabela_factura_proforma);
        if (tabela_factura_proforma.getColumnModel().getColumnCount() > 0)
        {
            tabela_factura_proforma.getColumnModel().getColumn(0).setMinWidth(0);
            tabela_factura_proforma.getColumnModel().getColumn(2).setMaxWidth(100);
            tabela_factura_proforma.getColumnModel().getColumn(3).setMaxWidth(100);
        }

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 1160, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 468, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Factura/Proforma - PP", jPanel10);

        tabela_factura_geral.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "Doc. Nº", "Cliente ", "Data", "Hora", "Usuário", "Total", "Ref. DOC"
            }
        )
        {
            Class[] types = new Class []
            {
                java.lang.Object.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean []
            {
                false, false, false, false, false, false, false
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
        tabela_factura_geral.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                tabela_factura_geralMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(tabela_factura_geral);

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 1160, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 468, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("GERAL", jPanel8);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1166, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 513, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(76, 76, 76))
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        buttonGroup2.add(ck_estrato_cliente);
        ck_estrato_cliente.setText("Extrato de Cliente");
        ck_estrato_cliente.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ck_estrato_clienteActionPerformed(evt);
            }
        });

        buttonGroup2.add(ck_mapa_iva);
        ck_mapa_iva.setText("Mapa do IVA");

        buttonGroup2.add(ck_relatorio_normal);
        ck_relatorio_normal.setSelected(true);
        ck_relatorio_normal.setText("Relatório Normal");

        jLabel3.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        jLabel3.setText("Forma de Impressão");

        buttonGroup2.add(ck_ordemFR);
        ck_ordemFR.setText("Relatorio ordenado");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ck_estrato_cliente)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(ck_ordemFR, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(ck_relatorio_normal)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(ck_mapa_iva)))
                .addContainerGap(7, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(ck_ordemFR))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ck_mapa_iva)
                    .addComponent(ck_relatorio_normal))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(ck_estrato_cliente)
                .addContainerGap())
        );

        btnCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/impressora1.png"))); // NOI18N
        btnCancelar.setText("Imprimir");
        btnCancelar.setAlignmentX(0.5F);
        btnCancelar.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnCancelarActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jButton2.setText("TOP");
        jButton2.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(lbData2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbData2, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnCancelar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 529, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCancelar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelar1ActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_btnCancelar1ActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed

        procedimento_imprimir();


    }//GEN-LAST:event_btnCancelarActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        adicionar_tabela();
//        new ExtratoContaClienteController( conexao ).gerarExtratoAutomatico( dcDataInicio.getDate(), dcDataFim.getDate() );


    }//GEN-LAST:event_jButton1ActionPerformed

    private void ck_estrato_clienteActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_ck_estrato_clienteActionPerformed
    {//GEN-HEADEREND:event_ck_estrato_clienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ck_estrato_clienteActionPerformed

    private void jTabbedPane1MouseEntered(java.awt.event.MouseEvent evt)//GEN-FIRST:event_jTabbedPane1MouseEntered
    {//GEN-HEADEREND:event_jTabbedPane1MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_jTabbedPane1MouseEntered

    private void jTabbedPane1MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_jTabbedPane1MouseClicked
    {//GEN-HEADEREND:event_jTabbedPane1MouseClicked
        // TODO add your handling code here:
        adicionar_relatorio_recibo();
    }//GEN-LAST:event_jTabbedPane1MouseClicked

    private void tabela_factura_geralMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_tabela_factura_geralMouseClicked
    {//GEN-HEADEREND:event_tabela_factura_geralMouseClicked
        if ( evt.getClickCount() > 1 )
        {

            reimprimir_GERAL();

        }
    }//GEN-LAST:event_tabela_factura_geralMouseClicked

    private void tabela_factura_ncMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_tabela_factura_ncMouseClicked
    {//GEN-HEADEREND:event_tabela_factura_ncMouseClicked
        if ( evt.getClickCount() > 1 )
        {

            reimprimir_NC();

        }
    }//GEN-LAST:event_tabela_factura_ncMouseClicked

    private void rb_por_clienteActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_rb_por_clienteActionPerformed
    {//GEN-HEADEREND:event_rb_por_clienteActionPerformed
        // TODO add your handling code here:
        metodo_radio();
    }//GEN-LAST:event_rb_por_clienteActionPerformed

    private void rb_todos_clientesActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_rb_todos_clientesActionPerformed
    {//GEN-HEADEREND:event_rb_todos_clientesActionPerformed
        // TODO add your handling code here:
        metodo_radio();
        adicionar_tabela();

    }//GEN-LAST:event_rb_todos_clientesActionPerformed

    private void cmbClienteActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbClienteActionPerformed
    {//GEN-HEADEREND:event_cmbClienteActionPerformed
        // TODO add your handling code here:
        adicionar_relatorio_factura();
        adicionar_relatorio_recibo();
    }//GEN-LAST:event_cmbClienteActionPerformed

    private void tabela_facturaMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_tabela_facturaMouseClicked
    {//GEN-HEADEREND:event_tabela_facturaMouseClicked
        if ( evt.getClickCount() > 1 )
        {

            reimprimir_FT();

        }
    }//GEN-LAST:event_tabela_facturaMouseClicked

    private void tabela_factura_reciboMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_tabela_factura_reciboMouseClicked
    {//GEN-HEADEREND:event_tabela_factura_reciboMouseClicked
        if ( evt.getClickCount() > 1 )
        {

            reimprimir_FR();

        }
    }//GEN-LAST:event_tabela_factura_reciboMouseClicked

    private void tabela_reciboMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_tabela_reciboMouseClicked
    {//GEN-HEADEREND:event_tabela_reciboMouseClicked
        if ( evt.getClickCount() > 1 )
        {

            reimprimir_R();

        }
    }//GEN-LAST:event_tabela_reciboMouseClicked

    private void ck_A4ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_ck_A4ActionPerformed
    {//GEN-HEADEREND:event_ck_A4ActionPerformed
        // TODO add your handling code here:
        actualizar_abreviacao();
    }//GEN-LAST:event_ck_A4ActionPerformed

    private void ck_simplificadaActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_ck_simplificadaActionPerformed
    {//GEN-HEADEREND:event_ck_simplificadaActionPerformed
        // TODO add your handling code here:
        actualizar_abreviacao();
    }//GEN-LAST:event_ck_simplificadaActionPerformed

    private void ck_simplificada_OActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_ck_simplificada_OActionPerformed
    {//GEN-HEADEREND:event_ck_simplificada_OActionPerformed
        actualizar_abreviacao();
    }//GEN-LAST:event_ck_simplificada_OActionPerformed

    private void ck_DuplicadaActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_ck_DuplicadaActionPerformed
    {//GEN-HEADEREND:event_ck_DuplicadaActionPerformed
        // TODO add your handling code here:
        actualizar_abreviacao();
    }//GEN-LAST:event_ck_DuplicadaActionPerformed

    private void ck_ComVirgulaActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_ck_ComVirgulaActionPerformed
    {//GEN-HEADEREND:event_ck_ComVirgulaActionPerformed
        actualizar_abreviacao();
    }//GEN-LAST:event_ck_ComVirgulaActionPerformed

    private void ck_S_A6ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_ck_S_A6ActionPerformed
    {//GEN-HEADEREND:event_ck_S_A6ActionPerformed
        actualizar_abreviacao();
    }//GEN-LAST:event_ck_S_A6ActionPerformed

    private void ck_A7ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_ck_A7ActionPerformed
    {//GEN-HEADEREND:event_ck_A7ActionPerformed
        // TODO add your handling code here:
        actualizar_abreviacao();
    }//GEN-LAST:event_ck_A7ActionPerformed

    private void ck_simplificada_OS_A6ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_ck_simplificada_OS_A6ActionPerformed
    {//GEN-HEADEREND:event_ck_simplificada_OS_A6ActionPerformed
        actualizar_abreviacao();
    }//GEN-LAST:event_ck_simplificada_OS_A6ActionPerformed

    private void tabela_factura_proformaMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_tabela_factura_proformaMouseClicked
    {//GEN-HEADEREND:event_tabela_factura_proformaMouseClicked
        if ( evt.getClickCount() > 1 )
        {

            reimprimir_PP();

        }
    }//GEN-LAST:event_tabela_factura_proformaMouseClicked

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton2ActionPerformed
    {//GEN-HEADEREND:event_jButton2ActionPerformed
        procedimentoVisualizar();
    }//GEN-LAST:event_jButton2ActionPerformed

//        private void busca_factura(String ref_doc) {
//      
//        try {
//            TbVenda venda = vendaDao.findByCodFactReemprensaoGeral( ref_doc );
////            Integer last_venda = vendasController.getLastVenda().getCodigo();
//            String cod_fact = txtRefDoc.getText();
////              String cod_factura = vendasController.getLastVendaCodFact(cod_fact ).getCodFact();   
////              venda = (TbVenda) vendasController.getLastVendaCodFact(cod_fact );
//              TbVenda venda_local = ( TbVenda ) vendasController.getLastVendaCodFact(cod_fact );
//
//                    try 
//                    {       
//  
////                         dcDataSaida.setDate(this.saidasProdutos.getDataSaida());
//                         txtRefDoc1.setText( venda_local.getNomeCliente() );
//                        preencher_tabela();
//                       
//                    }
//                    catch (Exception e) {
//                       // e.printStackTrace();
//                      
//                    }
//
//            
//        } catch (Exception e) {
////            limpar_dados();
//             JOptionPane.showMessageDialog(null, "Possivelmente esta saida nao existe!... Ou ja foi elimnada!...", "DVML-COMERCIAL, Lda", JOptionPane.WARNING_MESSAGE);            
//        }
//             
//    
//    }
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
            java.util.logging.Logger.getLogger( ListarRelatorioVenda.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        catch ( InstantiationException ex )
        {
            java.util.logging.Logger.getLogger( ListarRelatorioVenda.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        catch ( IllegalAccessException ex )
        {
            java.util.logging.Logger.getLogger( ListarRelatorioVenda.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        catch ( javax.swing.UnsupportedLookAndFeelException ex )
        {
            java.util.logging.Logger.getLogger( ListarRelatorioVenda.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater( new Runnable()
        {
            public void run()
            {
                try
                {
                    new ListarRelatorioVenda( BDConexao.getInstancia(), 15 ).setVisible( true );
                }
                catch ( Exception ex )
                {
                    Logger.getLogger( ListarRelatorioVenda.class.getName() ).log( Level.SEVERE, null, ex );
                }
            }
        } );
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnCancelar1;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.ButtonGroup buttonGroup3;
    private javax.swing.ButtonGroup buttonGroup4;
    private javax.swing.JCheckBox ck_A4;
    public static javax.swing.JCheckBox ck_A7;
    public static javax.swing.JCheckBox ck_ComVirgula;
    private javax.swing.JCheckBox ck_Duplicada;
    public static javax.swing.JCheckBox ck_S_A6;
    private javax.swing.JCheckBox ck_estrato_cliente;
    private javax.swing.JCheckBox ck_mapa_iva;
    private javax.swing.JCheckBox ck_ordemFR;
    private javax.swing.JCheckBox ck_relatorio_normal;
    public static javax.swing.JCheckBox ck_simplificada;
    public static javax.swing.JCheckBox ck_simplificada_O;
    public static javax.swing.JCheckBox ck_simplificada_OS_A6;
    private javax.swing.JComboBox<String> cmbArmazem;
    public static javax.swing.JComboBox cmbCliente;
    private com.toedter.calendar.JDateChooser dcDataFim;
    private com.toedter.calendar.JDateChooser dcDataInicio;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lbData;
    private javax.swing.JLabel lbData1;
    private javax.swing.JLabel lbData2;
    private javax.swing.JLabel lb_total;
    private javax.swing.JRadioButton rb_por_cliente;
    private javax.swing.JRadioButton rb_todos_clientes;
    private javax.swing.JTable tabela_factura;
    private javax.swing.JTable tabela_factura_geral;
    private javax.swing.JTable tabela_factura_nc;
    private javax.swing.JTable tabela_factura_proforma;
    private javax.swing.JTable tabela_factura_recibo;
    private javax.swing.JTable tabela_recibo;
    // End of variables declaration//GEN-END:variables

    private void adicionar_tabela()
    {

        configurar_clientes();

        DefaultTableModel modelo = null;

        try
        {
            int selectedIndex = jTabbedPane1.getSelectedIndex();

            //CASO DA FACTURA RECIBO
            if ( selectedIndex == 0 )
            {
                ck_estrato_cliente.setSelected( false ); //quando o dcumento FR nao e necessario Extrato do Cliente
                ck_estrato_cliente.setVisible( false );
//                String codFact = txtRefDoc.getText();
                modelo = (DefaultTableModel) tabela_factura_recibo.getModel();
                modelo.setRowCount( 0 );
//                lista = vendaDao.getAllVendaByBetweenDataAndArmazemAndDocumento( dcDataInicio.getDate(), dcDataFim.getDate(), getCodigoArmazem(), DVML.DOC_FACTURA_RECIBO_FR );
                lista = vendaDao.getAllFRVendaByBetweenDataAndArmazemAndDocumento( dcDataInicio.getDate(), dcDataFim.getDate(), getCodigoArmazem(), DVML.DOC_FACTURA_RECIBO_FR );
//                lista = vendaDao.getVendaByCodFact( codFact);

                if ( lista != null )
                {
                    for ( TbVenda object : lista )
                    {

                        modelo.addRow( new Object[]
                        {
                            object.getCodFact(),
                            getNomeCliente( object ),
                            getData( object.getDataVenda() ),
                            getHora( object.getHora() ),
                            object.getCodigoUsuario().getNome(),
                            object.getTotalVenda(),

                        } );

                    }
                    lb_total.setText( formatarComoMoeda( getTotal( tabela_factura_recibo ) ) );
                }

            } //CASO DA FACTURA
            else if ( selectedIndex == 1 )
            {
                adicionar_relatorio_factura();
            }
            else if ( selectedIndex == 2 )
            {
                adicionar_relatorio_nota();
            }
            else if ( selectedIndex == 3 )
            {
                adicionar_relatorio_recibo();
            }
            else if ( selectedIndex == 4 )
            {
                adicionar_relatorio_proforma();
            }
            else if ( selectedIndex == 5 )
            {
                adicionar_relatorio_geral();
            }

        }
        catch ( Exception e )
        {
            e.printStackTrace();
            lb_total.setText( "" );
            JOptionPane.showMessageDialog( null, "Não há registro para esse armazém", DVML.DVML_COMERCIAL, JOptionPane.WARNING_MESSAGE );
        }

    }

    public int getCodigoArmazem()
    {
        return armazemDao.getArmazemByDescricao( cmbArmazem.getSelectedItem().toString() ).getCodigo();
    }

    private String getData( Date date )
    {
        try
        {
            return getNumeroDoisDigitos( date.getDate() )
                    + "/" + ( getNumeroDoisDigitos( date.getMonth() + 1 ) )
                    + "/" + ( date.getYear() + 1900 );
        }
        catch ( Exception e )
        {
        }

        return "";
    }

    private String getHora( Date date )
    {
        try
        {
            return getNumeroDoisDigitos( date.getHours() ) + ":"
                    + getNumeroDoisDigitos( date.getMinutes() ) + ":"
                    + getNumeroDoisDigitos( date.getSeconds() );
        }
        catch ( Exception e )
        {
        }
        return "";

    }

    private String getNumeroDoisDigitos( int numero )
    {
        if ( numero < 10 )
        {
            return "0" + numero;
        }

        return String.valueOf( numero );

    }

    private BigDecimal getTotal( JTable tabela )
    {
        BigDecimal total = new BigDecimal( 0.0 );
        DefaultTableModel modelo = (DefaultTableModel) tabela.getModel();

        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {
            total = total.add( new BigDecimal( tabela.getValueAt( i, 5 ).toString() ) ).setScale( 2, RoundingMode.CEILING );

        }
        System.out.println( "Total: " + total.doubleValue() );

        return total;

    }

    private int getTipoDocumento()
    {
        int selectedIndex = jTabbedPane1.getSelectedIndex();
        if ( selectedIndex == 0 )
        {
            return DVML.DOC_FACTURA_RECIBO_FR;
        }
        else if ( selectedIndex == 1 )
        {
            return DVML.DOC_FACTURA_FT;
        }
        else if ( selectedIndex == 2 )
        {
            return DVML.DOC_NOTA_CREDITO_NC;
        }
        else if ( selectedIndex == 3 )
        {
            return DVML.DOC_RECIBO_RC;
        }
//        else if ( selectedIndex == 3 )
//        {
//            return DVML.DOC_FACTURA_FT;
//        }
        return 0;

    }

    private String getNomeCliente( TbVenda venda_local )
    {

        if ( venda_local.getCodigoCliente().getNome().equals( DVML._CLIENTE_CONSUMIDOR_FINAL ) )
        {
            String varConsumidorFinal = venda_local.getNomeConsumidorFinal();
            System.out.println( "Nome Cliente: " + varConsumidorFinal );
            boolean resultado = !Objects.isNull( varConsumidorFinal ) && !varConsumidorFinal.equalsIgnoreCase( "" );
            String nome_cliente_consumidor_final = resultado ? " (" + venda_local.getNomeConsumidorFinal() + ")" : "";

            return venda_local.getNomeCliente() + nome_cliente_consumidor_final;
        }
        return venda_local.getCodigoCliente().getNome();
    }

    private void configurar_clientes()
    {
        clientesController = new ClientesController( conexao );
        lista_all_clientes = clientesController.listarTodosDaVenda( dcDataInicio.getDate(), dcDataFim.getDate() );
        cmbCliente.setModel( new DefaultComboBoxModel( lista_all_clientes ) );
    }

    private int getCodigoCliente()
    {
        try
        {
            return clientesController.findByNome( cmbCliente.getSelectedItem().toString() ).getCodigo();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
        return 0;
    }

    private void procedimento_imprimir()
    {

        if ( ck_relatorio_normal.isSelected() || ck_mapa_iva.isSelected() )
        {
            ResumoVenda resumoVenda = new ResumoVenda( dcDataInicio.getDate(), dcDataFim.getDate(), getCodigoArmazem(), this.lista, getTipoDocumento(), ck_mapa_iva.isSelected() );
        }
        if ( ck_ordemFR.isSelected() )
        {

            ResumoVenda resumoVenda = new ResumoVenda( dcDataInicio.getDate(), dcDataFim.getDate(), getCodigoArmazem(), this.lista, getTipoDocumento(), ck_mapa_iva.isSelected(), ck_ordemFR.isSelected() );
        }
        else if ( ck_mapa_iva.isSelected() )
        {
            ResumoVenda resumoVenda = new ResumoVenda( dcDataInicio.getDate(), dcDataFim.getDate(), getCodigoArmazem(), this.lista, getTipoDocumento(), true );
        }
        else if ( ck_estrato_cliente.isSelected() )
        {
            new ExtratoContaReport( getCodigoCliente(), dcDataInicio.getDate(), dcDataFim.getDate() );
        }

    }

    private void metodo_radio()
    {
        if ( rb_por_cliente.isSelected() )
        {
            cmbCliente.setModel( new DefaultComboBoxModel( lista_all_clientes ) );
            cmbCliente.setVisible( true );
        }
        else
        {
            cmbCliente.setVisible( false );
        }
        adicionar_relatorio_factura();
//        adicionar_relatorio_recibo();
    }

    private void adicionar_relatorio_factura()
    {
        ck_estrato_cliente.setVisible( true );
//        rb_todos_clientes.setSelected( true);
        lista = rb_todos_clientes.isSelected() ? vendaDao.getAllFTVendaByBetweenDataAndArmazemAndDocumento( dcDataInicio.getDate(), dcDataFim.getDate(), getCodigoArmazem(), DVML.DOC_FACTURA_FT ) : vendaDao.getAllVendaByBetweenDataAndArmazemAndDocumentoAndCliente( dcDataInicio.getDate(), dcDataFim.getDate(), getCodigoArmazem(), DVML.DOC_FACTURA_FT, getCodigoCliente() );
//        lista = vendaDao.getAllVendaByBetweenDataAndArmazemAndDocumento( dcDataInicio.getDate(), dcDataFim.getDate(), getCodigoArmazem(), DVML.DOC_FACTURA_FT );
        DefaultTableModel modelo = (DefaultTableModel) tabela_factura.getModel();
        modelo.setRowCount( 0 );
        if ( lista != null )
        {
            for ( TbVenda object : lista )
            {
                int diferencaData = MetodosUtil.getDiferencaDias( object.getDataVencimento(), new Date(), conexao );
//                BigDecimal valor_pago = ( !Objects.isNull( VendaDao.getTotalPagoByCodFact( object.getCodFact(), conexao ) ) ? VendaDao.getTotalPagoByCodFact( object.getCodFact(), conexao ) : new BigDecimal( 0.0 ) );
                BigDecimal valor_pago = new BigDecimal( amortizacaoDividaController.getValorAtribuidoByCodFact( object.getCodFact() ) );
//                System.out.println( "VALOR PAGO = " + valor_pago );

                modelo.addRow( new Object[]
                {
                    object.getCodFact(),
                    getNomeCliente( object ),
                    getData( object.getDataVenda() ),
                    getHora( object.getHora() ),
                    object.getCodigoUsuario().getNome(),
                    //                    new BigDecimal( object.getTotalVenda() ).setScale( 2, RoundingMode.CEILING ),
                    new BigDecimal( object.getTotalVenda().doubleValue() ).setScale( 2, RoundingMode.CEILING ),
                    valor_pago,
                    //                            new BigDecimal( object.getTotalVenda() ).subtract( valor_pago ).setScale( 2, RoundingMode.CEILING )
                    valor_pago.subtract( new BigDecimal( object.getTotalVenda().doubleValue() ) ).setScale( 2, RoundingMode.FLOOR ),
                    //                    valor_pago.subtract( new BigDecimal( object.getTotalVenda() ) ).setScale( 2, RoundingMode.FLOOR ),
                    getData( object.getDataVencimento() ),
                    diferencaData,

                } );

            }
            lb_total.setText( formatarComoMoeda( getTotal( tabela_factura ).doubleValue() ) );
        }
    }

    private void adicionar_relatorio_nota()
    {

//        lista = rb_todos_clientes.isSelected() ? vendaDao.getAllNotasByBetweenDataAndArmazemAndDocumento( dcDataInicio.getDate(), dcDataFim.getDate(), getCodigoArmazem(), DVML.DOC_RECIBO_RC) : vendaDao.getAllNotaByBetweenDataAndArmazemAndDocumentoAndCliente( dcDataInicio.getDate(), dcDataFim.getDate(), getCodigoArmazem(), DVML.DOC_RECIBO_RC, getCodigoCliente() );
        ck_estrato_cliente.setSelected( false ); //quando o dcumento FR nao e necessario Extrato do Cliente
        ck_estrato_cliente.setVisible( false );
//        String codFact = txtRefDoc.getText();
        DefaultTableModel modelo = (DefaultTableModel) tabela_factura_nc.getModel();
        modelo.setRowCount( 0 );
        lista = vendaDao.getAllVendaByBetweenDataAndArmazemAndDocumento( dcDataInicio.getDate(), dcDataFim.getDate(), getCodigoArmazem(), DVML.DOC_NOTA_CREDITO_NC );
//                lista = vendaDao.getVendaByCodFact( codFact);

        if ( lista != null )
        {
            for ( TbVenda object : lista )
            {

                modelo.addRow( new Object[]
                {
                    object.getCodFact(),
                    getNomeCliente( object ),
                    getData( object.getDataVenda() ),
                    getHora( object.getHora() ),
                    object.getCodigoUsuario().getNome(),
                    object.getTotalVenda(),

                } );

            }
            lb_total.setText( formatarComoMoeda( getTotal( tabela_factura_nc ) ) );
        }
    }

    private void adicionar_relatorio_proforma()
    {
//        String codFact = txtRefDoc.getText();
        DefaultTableModel modelo = (DefaultTableModel) tabela_factura_proforma.getModel();
        modelo.setRowCount( 0 );
        lista = vendaDao.getAllVendaByBetweenDataAndArmazemAndDocumento( dcDataInicio.getDate(), dcDataFim.getDate(), getCodigoArmazem(), DVML.DOC_FACTURA_PROFORMA_PP );
//                lista = vendaDao.getVendaByCodFact( codFact);

        if ( lista != null )
        {
            for ( TbVenda object : lista )
            {

                modelo.addRow( new Object[]
                {
                    object.getCodFact(),
                    getNomeCliente( object ),
                    getData( object.getDataVenda() ),
                    getHora( object.getHora() ),
                    object.getCodigoUsuario().getNome(),
                    object.getTotalVenda(),

                } );

            }
            lb_total.setText( formatarComoMoeda( getTotal( tabela_factura_proforma ) ) );
        }
    }

    private void adicionar_relatorio_recibo()
    {

//        lista = rb_todos_clientes.isSelected() ? vendaDao.getAllNotasByBetweenDataAndArmazemAndDocumento( dcDataInicio.getDate(), dcDataFim.getDate(), getCodigoArmazem(), DVML.DOC_RECIBO_RC) : vendaDao.getAllNotaByBetweenDataAndArmazemAndDocumentoAndCliente( dcDataInicio.getDate(), dcDataFim.getDate(), getCodigoArmazem(), DVML.DOC_RECIBO_RC, getCodigoCliente() );
        ck_estrato_cliente.setSelected( false ); //quando o dcumento FR nao e necessario Extrato do Cliente
        ck_estrato_cliente.setVisible( false );
//        String codFact = txtRefDoc.getText();
        DefaultTableModel modelo = (DefaultTableModel) tabela_recibo.getModel();
        modelo.setRowCount( 0 );
        lista = vendaDao.getAllVendaByBetweenDataAndArmazemAndDocumento( dcDataInicio.getDate(), dcDataFim.getDate(), getCodigoArmazem(), DVML.DOC_RECIBO_RC );
//                lista = vendaDao.getVendaByCodFact( codFact);

        if ( lista != null )
        {
            for ( TbVenda object : lista )
            {

                modelo.addRow( new Object[]
                {
                    object.getCodFact(),
                    getNomeCliente( object ),
                    getData( object.getDataVenda() ),
                    getHora( object.getHora() ),
                    object.getCodigoUsuario().getNome(),
                    object.getTotalVenda(),

                } );

            }
            lb_total.setText( formatarComoMoeda( getTotal( tabela_recibo ) ) );
        }
    }

    private void adicionar_relatorio_recibo1()
    {
//        configurar_clientes();
        ck_estrato_cliente.setVisible( true );
        lista = rb_todos_clientes.isSelected() ? vendaDao.getAllVendaByBetweenDataAndArmazemAndDocumento( dcDataInicio.getDate(), dcDataFim.getDate(), getCodigoArmazem(), DVML.DOC_RECIBO_RC ) : vendaDao.getAllVendaByBetweenDataAndArmazemAndDocumentoAndCliente( dcDataInicio.getDate(), dcDataFim.getDate(), getCodigoArmazem(), DVML.DOC_RECIBO_RC, getCodigoCliente() );

//        lista = rb_todos_clientes.isSelected() ? vendaDao.getAllNotasByBetweenDataAndArmazemAndDocumento( dcDataInicio.getDate(), dcDataFim.getDate(), getCodigoArmazem(), DVML.DOC_RECIBO_RC) : vendaDao.getAllNotaByBetweenDataAndArmazemAndDocumentoAndCliente( dcDataInicio.getDate(), dcDataFim.getDate(), getCodigoArmazem(), DVML.DOC_RECIBO_RC, getCodigoCliente() );
//        ck_estrato_cliente.setSelected( false ); //quando o dcumento FR nao e necessario Extrato do Cliente
        ck_estrato_cliente.setVisible( false );
//        String codFact = txtRefDoc.getText();
        DefaultTableModel modelo = (DefaultTableModel) tabela_recibo.getModel();
        modelo.setRowCount( 0 );
//        lista = vendaDao.getAllVendaByBetweenDataAndArmazemAndDocumento( dcDataInicio.getDate(), dcDataFim.getDate(), getCodigoArmazem(), DVML.DOC_RECIBO_RC );
//                lista = vendaDao.getVendaByCodFact( codFact);

        if ( lista != null )
        {
            for ( TbVenda object : lista )
            {

                modelo.addRow( new Object[]
                {
                    object.getCodFact(),
                    getNomeCliente( object ),
                    getData( object.getDataVenda() ),
                    getHora( object.getHora() ),
                    object.getCodigoUsuario().getNome(),
                    object.getTotalVenda(),

                } );

            }
            lb_total.setText( formatarComoMoeda( getTotal( tabela_recibo ) ) );
        }
    }
//    private void adicionar_relatorio_recibo()
//    {
//        ck_estrato_cliente.setVisible( true );
//        lista = rb_todos_clientes.isSelected() ? vendaDao.getAllVendaByBetweenDataAndArmazemAndDocumento( dcDataInicio.getDate(), dcDataFim.getDate(), getCodigoArmazem(), DVML.DOC_RECIBO_RC ) : vendaDao.getAllVendaByBetweenDataAndArmazemAndDocumentoAndCliente( dcDataInicio.getDate(), dcDataFim.getDate(), getCodigoArmazem(), DVML.DOC_RECIBO_RC, getCodigoCliente() );
////        lista = vendaDao.getAllVendaByBetweenDataAndArmazemAndDocumento( dcDataInicio.getDate(), dcDataFim.getDate(), getCodigoArmazem(), DVML.DOC_FACTURA_FT );
//        DefaultTableModel modelo = ( DefaultTableModel ) tabela_recibo.getModel();
//        modelo.setRowCount( 0 );
//        if ( lista != null )
//        {
//            for ( TbVenda object : lista )
//            {
//                int diferencaData = MetodosUtil.getDiferencaDias( object.getDataVencimento(), new Date(), conexao );
////                BigDecimal valor_pago = ( !Objects.isNull( VendaDao.getTotalPagoByCodFact( object.getCodFact(), conexao ) ) ? VendaDao.getTotalPagoByCodFact( object.getCodFact(), conexao ) : new BigDecimal( 0.0 ) );
//                BigDecimal valor_pago = new BigDecimal( amortizacaoDividaController.getValorAtribuidoByCodFact( object.getCodFact() ) );
////                System.out.println( "VALOR PAGO = " + valor_pago );
//
//                modelo.addRow( new Object[]
//                {
//                    object.getCodFact(),
//                    getNomeCliente( object ),
//                    getData( object.getDataVenda() ),
//                    getHora( object.getHora() ),
//                    object.getCodigoUsuario().getNome(),
//                    //                    new BigDecimal( object.getTotalVenda() ).setScale( 2, RoundingMode.CEILING ),
//                    new BigDecimal( object.getTotalVenda().doubleValue() ).setScale( 2, RoundingMode.CEILING ),
//                    valor_pago,
//                    //                            new BigDecimal( object.getTotalVenda() ).subtract( valor_pago ).setScale( 2, RoundingMode.CEILING )
//                    valor_pago.subtract( new BigDecimal( object.getTotalVenda().doubleValue() ) ).setScale( 2, RoundingMode.FLOOR ),
//                    //                    valor_pago.subtract( new BigDecimal( object.getTotalVenda() ) ).setScale( 2, RoundingMode.FLOOR ),
//                    getData( object.getDataVencimento() ),
//                    diferencaData,
//
//                } );
//
//            }
//            lb_total.setText( formatarComoMoeda( getTotal( tabela_recibo ).doubleValue() ) );
//        }
//    }

    private void adicionar_relatorio_geral()
    {
        ck_estrato_cliente.setVisible( true );
        lista = rb_todos_clientes.isSelected() ? vendaDao.getAllVendasGeraisByBetweenDataAndArmazemAndDocumento( dcDataInicio.getDate(), dcDataFim.getDate(), getCodigoArmazem() ) : vendaDao.getAllVendasGeraisByBetweenDataAndArmazemAndDocumentoAndCliente( dcDataInicio.getDate(), dcDataFim.getDate(), getCodigoArmazem(), getCodigoCliente() );

        DefaultTableModel modelo = (DefaultTableModel) tabela_factura_geral.getModel();
        modelo.setRowCount( 0 );
        if ( lista != null )
        {
            for ( TbVenda object : lista )
            {
//                BigDecimal valor_pago = ( !Objects.isNull( VendaDao.getTotalPagoByCodFact( object.getCodFact(), conexao ) ) ? VendaDao.getTotalPagoByCodFact( object.getCodFact(), conexao ) : new BigDecimal( 0.0 ) );
                BigDecimal valor_pago = (!Objects.isNull( VendaDao.getTotalPagoByCodFact( object.getCodFact(), conexao ) ) ? VendaDao.getTotalPagoByCodFact( object.getCodFact(), conexao ) : new BigDecimal( 0.0 ));
                System.out.println( "VALOR PAGO = " + valor_pago );
                modelo.addRow( new Object[]
                {
                    object.getCodFact(),
                    getNomeCliente( object ),
                    getData( object.getDataVenda() ),
                    getHora( object.getHora() ),
                    object.getCodigoUsuario().getNome(),
                    new BigDecimal( object.getTotalVenda().doubleValue() ).setScale( 2, RoundingMode.CEILING ),
                    object.getRefCodFact(),
                    valor_pago,
                    //                            new BigDecimal( object.getTotalVenda() ).subtract( valor_pago ).setScale( 2, RoundingMode.CEILING )
                    valor_pago.subtract( new BigDecimal( object.getTotalVenda().doubleValue() ) ).setScale( 2, RoundingMode.FLOOR )

                } );

            }
            lb_total.setText( formatarComoMoeda( getTotal( tabela_factura_geral ).doubleValue() ) );
        }
    }

    private void reimprimir_FR()
    {
        DefaultTableModel modelo = (DefaultTableModel) tabela_factura_recibo.getModel();
        int selectedRow = tabela_factura_recibo.getSelectedRow();
        String codRef = modelo.getValueAt( selectedRow, 0 ).toString();

        procedimento_reimprimir_FR( codRef );
    }

    private void procedimento_reimprimir_FR( String ref_doc )
    {

        HashMap hashMap = new HashMap();
        TbVenda venda = vendaDao.findByCodFactReemprensao( ref_doc );

        if ( venda != null )
        {

//            Abreviacao abreviacao = DVML.getAbreviacao( venda.getFkDocumento().getPkDocumento() );
//            abreviacao = DVML.Abreviacao.FR_A4;
            List<TbProduto> lista_produto_isentos = new ArrayList<>();
            lista_produto_isentos = MetodosUtil.getProdutosIsentos( venda.getTbItemVendaList() );
            String motivos_isentos = MetodosUtil.getMotivoIsensaoProdutos( lista_produto_isentos );
//            ListaVenda1 original = new ListaVenda1( cod_venda, abreviacao, false, ck_simplificada.isSelected(), "Original", motivos_isentos );

            ListaVenda2 listaVenda2 = new ListaVenda2( venda.getCodigo(), abreviacao, false, false, DVML.SEGUNDA_VIA_CONFORMIDADE_COM_ORIGINAL, motivos_isentos );
//            ListaVenda2 listaVenda2 = new ListaVenda2( venda.getCodigo(), abreviacao, false, false, DVML.SEGUNDA_VIA_CONFORMIDADE_COM_ORIGINAL, motivos_isentos );
        }
        else
        {
            JOptionPane.showMessageDialog( null, "Atenção\nO Documento não existe na base de dados. \nObs: Verifique a referência. " );
        }

    }

    private void reimprimir_PP()
    {
        DefaultTableModel modelo = (DefaultTableModel) tabela_factura_proforma.getModel();
        int selectedRow = tabela_factura_proforma.getSelectedRow();
        String codRef = modelo.getValueAt( selectedRow, 0 ).toString();

        procedimento_reimprimir_PP( codRef );
    }

    private void procedimento_reimprimir_PP( String ref_doc )
    {

        HashMap hashMap = new HashMap();
        TbVenda venda = vendaDao.findByCodFactReemprensao( ref_doc );

        if ( venda != null )
        {

//            Abreviacao abreviacao = DVML.getAbreviacao( venda.getFkDocumento().getPkDocumento() );
//            abreviacao = DVML.Abreviacao.FR_A4;
            List<TbProduto> lista_produto_isentos = new ArrayList<>();
            lista_produto_isentos = MetodosUtil.getProdutosIsentos( venda.getTbItemVendaList() );
            String motivos_isentos = MetodosUtil.getMotivoIsensaoProdutos( lista_produto_isentos );
//            ListaVenda1 original = new ListaVenda1( cod_venda, abreviacao, false, ck_simplificada.isSelected(), "Original", motivos_isentos );

            ListaVenda2 listaVenda2 = new ListaVenda2( venda.getCodigo(), abreviacao, false, false, DVML.SEGUNDA_VIA_CONFORMIDADE_COM_ORIGINAL, motivos_isentos );
//            ListaVenda2 listaVenda2 = new ListaVenda2( venda.getCodigo(), abreviacao, false, false, DVML.SEGUNDA_VIA_CONFORMIDADE_COM_ORIGINAL, motivos_isentos );
        }
        else
        {
            JOptionPane.showMessageDialog( null, "Atenção\nO Documento não existe na base de dados. \nObs: Verifique a referência. " );
        }

    }

    private void reimprimir_R()
    {
        DefaultTableModel modelo = (DefaultTableModel) tabela_recibo.getModel();
        int selectedRow = tabela_recibo.getSelectedRow();
        String codRef = modelo.getValueAt( selectedRow, 0 ).toString();

        procedimento_reimprimir_R( codRef );
    }

    private void procedimento_reimprimir_R( String ref_doc )
    {

        HashMap hashMap = new HashMap();
        TbVenda venda = vendaDao.findByCodFactReemprensao( ref_doc );

        if ( venda != null )
        {

            Abreviacao abreviacao = DVML.getAbreviacao( venda.getFkDocumento().getPkDocumento() );
            abreviacao = DVML.Abreviacao.RC;
            List<TbProduto> lista_produto_isentos = new ArrayList<>();
            lista_produto_isentos = MetodosUtil.getProdutosIsentos( venda.getTbItemVendaList() );
            String motivos_isentos = MetodosUtil.getMotivoIsensaoProdutos( lista_produto_isentos );
            ListaVenda2 listaVenda2 = new ListaVenda2( venda.getCodigo(), abreviacao, false, false, DVML.SEGUNDA_VIA_CONFORMIDADE_COM_ORIGINAL, motivos_isentos );
        }
        else
        {
            JOptionPane.showMessageDialog( null, "Atenção\nO Documento não existe na base de dados. \nObs: Verifique a referência. " );
        }

    }

    private void reimprimir_FT()
    {
        DefaultTableModel modelo = (DefaultTableModel) tabela_factura.getModel();
        int selectedRow = tabela_factura.getSelectedRow();
        String codRef = modelo.getValueAt( selectedRow, 0 ).toString();

        procedimento_reimprimir_FT( codRef );
    }

    private void procedimento_reimprimir_FT( String ref_doc )
    {

        HashMap hashMap = new HashMap();
        TbVenda venda = vendaDao.findByCodFactReemprensao( ref_doc );

        if ( venda != null )
        {

            Abreviacao abreviacao = DVML.getAbreviacao( venda.getFkDocumento().getPkDocumento() );
            abreviacao = DVML.Abreviacao.FA;
            List<TbProduto> lista_produto_isentos = new ArrayList<>();
            lista_produto_isentos = MetodosUtil.getProdutosIsentos( venda.getTbItemVendaList() );
            String motivos_isentos = MetodosUtil.getMotivoIsensaoProdutos( lista_produto_isentos );
            ListaVenda2 listaVenda2 = new ListaVenda2( venda.getCodigo(), abreviacao, false, false, DVML.SEGUNDA_VIA_CONFORMIDADE_COM_ORIGINAL, motivos_isentos );
        }
        else
        {
            JOptionPane.showMessageDialog( null, "Atenção\nO Documento não existe na base de dados. \nObs: Verifique a referência. " );
        }

    }

    private void reimprimir_NC()
    {
        DefaultTableModel modelo = (DefaultTableModel) tabela_factura_nc.getModel();
        int selectedRow = tabela_factura_nc.getSelectedRow();
        String codRef = modelo.getValueAt( selectedRow, 0 ).toString();

        procedimento_reimprimir_NC( codRef );
    }

    private void procedimento_reimprimir_NC( String ref_doc )
    {

        HashMap hashMap = new HashMap();
        TbVenda venda = vendaDao.findByCodFactReemprensaoNota1( ref_doc );

        if ( venda != null )
        {

            Abreviacao abreviacao_nota_credito = DVML.getAbreviacao( venda.getFkDocumento().getPkDocumento() );
            abreviacao_nota_credito = DVML.Abreviacao.NC;
            List<TbProduto> lista_produto_isentos = new ArrayList<>();
            lista_produto_isentos = MetodosUtil.getProdutosIsentos( venda.getTbItemVendaList() );
            String motivos_isentos = MetodosUtil.getMotivoIsensaoProdutos( lista_produto_isentos );
            ListaNotasDebito listaVenda2 = new ListaNotasDebito( venda.getCodigo(), abreviacao_nota_credito, false, true, hashMap, motivos_isentos );
//            ListaVenda2 listaVenda2 = new ListaVenda2(venda.getCodigo(), abreviacao, false, false, DVML.SEGUNDA_VIA_CONFORMIDADE_COM_ORIGINAL, motivos_isentos);
        }
        else
        {
            JOptionPane.showMessageDialog( null, "Atenção\nO Documento não existe na base de dados. \nObs: Verifique a referência. " );
        }

    }

    private void reimprimir_GERAL()
    {
        DefaultTableModel modelo = (DefaultTableModel) tabela_factura_geral.getModel();
        int selectedRow = tabela_factura_geral.getSelectedRow();
        String codRef = modelo.getValueAt( selectedRow, 0 ).toString();

        procedimento_reimprimir_GERAL( codRef );
    }

    private void procedimento_reimprimir_GERAL( String ref_doc )
    {

        HashMap hashMap = new HashMap();
        TbVenda venda = vendaDao.findByCodFactReemprensaoGeral( ref_doc );

        if ( venda != null )
        {

            Abreviacao abreviacao = DVML.getAbreviacao( venda.getFkDocumento().getPkDocumento() );
            Abreviacao abreviacao_nota_credito = DVML.getAbreviacao( venda.getFkDocumento().getPkDocumento() );
            abreviacao_nota_credito = DVML.Abreviacao.NC;
            List<TbProduto> lista_produto_isentos = new ArrayList<>();
            lista_produto_isentos = MetodosUtil.getProdutosIsentos( venda.getTbItemVendaList() );
            String motivos_isentos = MetodosUtil.getMotivoIsensaoProdutos( lista_produto_isentos );

            if ( venda.getStatusEliminado().equals( "ANULADO" ) )
            {
                ListaNotasDebito listaVendaDebito = new ListaNotasDebito( venda.getCodigo(), abreviacao_nota_credito, false, true, hashMap, motivos_isentos );
            }
            else
            {
                ListaVendaGeral listaVendaGeral = new ListaVendaGeral( venda.getCodigo(), abreviacao, false, false, DVML.SEGUNDA_VIA_CONFORMIDADE_COM_ORIGINAL, motivos_isentos );
            }

        }
        else
        {
            JOptionPane.showMessageDialog( null, "Atenção\nO Documento não existe na base de dados. \nObs: Verifique a referência. " );
        }

    }

//    private void preencher_tabela()
//    {
//
//        DefaultTableModel modelo = ( DefaultTableModel ) tabela_factura_geral.getModel();
//        List<TbVenda> list_iten = vendaDao.getVendaByCodFact( this.venda.getCodFact() );
//
//        modelo.setRowCount( 0 );
//        for ( int i = 0; i < list_iten.size(); i++ )
//        {
//            TbVenda vendas = list_iten.get( i );
//            modelo.addRow( new Object[]
//            {
////                            (i+1),
//                vendas.getCodFact(),
//                vendas.getCodigoCliente().getNome(),
//                vendas.getDataVenda(),
//                vendas.getHora(),
//                vendas.getCodigoUsuario().getNome(),
//                vendas.getTotalVenda(),
//                vendas.getRefCodFact(),
//
//            } );
//        }
//
//    }
    private void adicionar_tabela_cod_fact()
    {

        configurar_clientes();

        DefaultTableModel modelo = null;

        try
        {
            int selectedIndex = jTabbedPane1.getSelectedIndex();

            //CASO DA FACTURA RECIBO
            if ( selectedIndex == 0 )
            {
                ck_estrato_cliente.setSelected( false ); //quando o dcumento FR nao e necessario Extrato do Cliente
                ck_estrato_cliente.setVisible( false );
//                String codFact = txtRefDoc.getText();
                modelo = (DefaultTableModel) tabela_factura_recibo.getModel();
                modelo.setRowCount( 0 );
//                lista = vendaDao.getAllVendaByBetweenDataAndArmazemAndDocumento( dcDataInicio.getDate(), dcDataFim.getDate(), getCodigoArmazem(), DVML.DOC_FACTURA_RECIBO_FR );
//                lista = vendaDao.getVendaByCodFact( codFact );

                if ( lista != null )
                {
                    for ( TbVenda object : lista )
                    {

                        modelo.addRow( new Object[]
                        {
                            object.getCodFact(),
                            getNomeCliente( object ),
                            getData( object.getDataVenda() ),
                            getHora( object.getHora() ),
                            object.getCodigoUsuario().getNome(),
                            object.getTotalVenda(),

                        } );

                    }
                    lb_total.setText( formatarComoMoeda( getTotal( tabela_factura_recibo ) ) );
                }

            } //CASO DA FACTURA
            else if ( selectedIndex == 1 )
            {
                adicionar_relatorio_factura();
            }
            else if ( selectedIndex == 2 )
            {
                adicionar_relatorio_nota();
            }
            else if ( selectedIndex == 3 )
            {
                adicionar_relatorio_recibo();
            }
            else if ( selectedIndex == 4 )
            {
                adicionar_relatorio_geral();
            }

        }
        catch ( Exception e )
        {
            e.printStackTrace();
            lb_total.setText( "" );
            JOptionPane.showMessageDialog( null, "Não há registro para esse armazém", DVML.DVML_COMERCIAL, JOptionPane.WARNING_MESSAGE );
        }

    }

    private void setFolhaImpressora( String folha )
    {
        if ( folha.equalsIgnoreCase( "A6" ) )
        {
            ck_simplificada.setSelected( true );
            ck_A7.setSelected( false );
            ck_A4.setSelected( false );
            ck_Duplicada.setSelected( false );
            ck_S_A6.setSelected( false );
            ck_ComVirgula.setSelected( false );
            ck_simplificada_O.setSelected( false );
            ck_simplificada_OS_A6.setSelected( false );
            this.abreviacao = Abreviacao.FR_A6;
        }
        if ( folha.equalsIgnoreCase( "A6_O" ) )
        {
            ck_simplificada_O.setSelected( true );
            ck_A7.setSelected( false );
            ck_A4.setSelected( false );
            ck_Duplicada.setSelected( false );
            ck_S_A6.setSelected( false );
            ck_ComVirgula.setSelected( false );
            ck_simplificada.setSelected( false );
            ck_simplificada_OS_A6.setSelected( false );
            this.abreviacao = Abreviacao.FR_A6_O;
        }
        if ( folha.equalsIgnoreCase( "S_A6_O" ) )
        {
            ck_simplificada_OS_A6.setSelected( true );
            ck_simplificada_O.setSelected( false );
            ck_A7.setSelected( false );
            ck_A4.setSelected( false );
            ck_Duplicada.setSelected( false );
            ck_S_A6.setSelected( false );
            ck_ComVirgula.setSelected( false );
            ck_simplificada.setSelected( false );
            this.abreviacao = Abreviacao.FR_A6_O;
        }
        else if ( folha.equalsIgnoreCase( "A7" ) )
        {
            ck_A7.setSelected( true );
            ck_simplificada_OS_A6.setSelected( false );
            ck_simplificada.setSelected( false );
            ck_A4.setSelected( false );
            ck_Duplicada.setSelected( false );
            ck_S_A6.setSelected( false );
            ck_ComVirgula.setSelected( false );
            ck_simplificada_O.setSelected( false );
            this.abreviacao = Abreviacao.FR_SA7;
        }
        else if ( folha.equalsIgnoreCase( "A5" ) )
        {
            ck_Duplicada.setSelected( true );
            ck_simplificada_OS_A6.setSelected( false );
            ck_simplificada.setSelected( false );
            ck_A4.setSelected( false );
            ck_A7.setSelected( false );
            ck_S_A6.setSelected( false );
            ck_ComVirgula.setSelected( false );
            ck_simplificada_O.setSelected( false );
            this.abreviacao = Abreviacao.FT_A4_Duplicado;
        }
        else if ( folha.equalsIgnoreCase( "S_A6" ) )
        {
            ck_S_A6.setSelected( true );
            ck_simplificada_OS_A6.setSelected( false );
            ck_simplificada.setSelected( false );
            ck_A7.setSelected( false );
            ck_A4.setSelected( false );
            ck_Duplicada.setSelected( false );
            ck_ComVirgula.setSelected( false );
            ck_simplificada_O.setSelected( false );
            this.abreviacao = Abreviacao.FR_S_A6;
        }
        else if ( folha.equalsIgnoreCase( "A6V" ) )
        {
            ck_ComVirgula.setSelected( true );
            ck_simplificada_OS_A6.setSelected( false );
            ck_simplificada.setSelected( false );
            ck_A7.setSelected( false );
            ck_A4.setSelected( false );
            ck_Duplicada.setSelected( false );
            ck_S_A6.setSelected( false );
            ck_simplificada_O.setSelected( false );
            this.abreviacao = Abreviacao.FR_A6_Com_Virgula;
        }

        else
        {
            ck_A4.setSelected( true );
            ck_simplificada_OS_A6.setSelected( false );
            ck_simplificada.setSelected( false );
            ck_A7.setSelected( false );
            ck_Duplicada.setSelected( false );
            ck_S_A6.setSelected( false );
            ck_ComVirgula.setSelected( false );
            ck_simplificada_O.setSelected( false );
            this.abreviacao = Abreviacao.FR_A4;
        }
    }

    private void actualizar_abreviacao()
    {

//        switch ( getIdDocumento() )
//        {
//            case DVML.DOC_FACTURA_RECIBO_FR:
        if ( ck_A4.isSelected() )
        {
            this.abreviacao = Abreviacao.FR_A4;
        }
        else if ( ck_simplificada.isSelected() )
        {
            this.abreviacao = Abreviacao.FR_A6;
        }
        else if ( ck_simplificada_O.isSelected() )
        {
            this.abreviacao = Abreviacao.FR_A6_O;
        }
        else if ( ck_simplificada_OS_A6.isSelected() )
        {
            this.abreviacao = Abreviacao.FR_S_A6_O;
        }
        else if ( ck_A7.isSelected() )
        {
            this.abreviacao = Abreviacao.FR_SA7;
        }
        else if ( ck_S_A6.isSelected() )
        {
            this.abreviacao = Abreviacao.FR_S_A6;
        }
        else if ( ck_ComVirgula.isSelected() )
        {
            this.abreviacao = Abreviacao.FR_A6_Com_Virgula;
        }
        else
        {
            this.abreviacao = Abreviacao.FR_A4_Duplicado;
        }

//                break;
//
//            case DVML.DOC_FACTURA_FT:
//
//                if ( ck_A4.isSelected() )
//                {
//                    this.abreviacao = Abreviacao.FA;
////                    ck_A4.setSelected( true );
//                }
//                else if ( ck_A7.isSelected() || ck_simplificada.isSelected() || ck_S_A6.isSelected() )
//                {
////                    JOptionPane.showMessageDialog( null, "Atenção, selecione outro formato pra venda a crédito!" );
//                    ck_A4.setSelected( true );
//                }
//                else
//                {
//                    this.abreviacao = Abreviacao.FT_A4_Duplicado;
//                }
//
//                break;
//
//            case DVML.DOC_FACTURA_PROFORMA_PP:
//                this.abreviacao = Abreviacao.PP;
//                break;
//
//            case DVML.DOC_GUIA_TRANSPORTE_GT:
//                this.abreviacao = Abreviacao.GT;
//                break;
//
//            default:
//                break;
//        }
    }

    private void procedimentoVisualizar()
    {
        HashMap hashMap = new HashMap();
//        hashMap.put( "COD_REQUISITANTE", getIdRequisitante() );
//        hashMap.put( "COD_REQUISITANTE", getIdRequisitante() );
//        hashMap.put("PARM_DATA_1", dcData.getDate() );
//        hashMap.put( "PARM_DATA_2", dcDataFim.getDate() );

        hashMap.put( "TOP", 20 );
//        hashMap.put( "ANO", ( new Date().getYear() + 1900 ) );
        hashMap.put( "ANO", dcDataInicio.getDate().getYear() + 1900 );
//        hashMap.put( "MES", MetodosUtil.getMesPagarU( new Date().getMonth() + 1 ) );
        hashMap.put( "MES", MetodosUtil.getMesPagarU( dcDataInicio.getDate().getMonth() + 1 ) );
        hashMap.put( "ID_MES", ( dcDataInicio.getDate().getMonth() + 1 ) );

        String file = "relatorio_diario_produto_top.jasper";
        AnyReport anyReport = new AnyReport( hashMap, file );
    }

}
