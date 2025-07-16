/*2
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package visao;

import comercial.controller.VendasController;
import dao.AmortizacaoDividaDao;
import dao.AnoEconomicoDao;
import dao.ArmazemDao;
import dao.CambioDao;
import dao.ClienteDao;
import dao.DocumentoDao;
import dao.FormaPagamentoItemDao;
import dao.UsuarioDao;
import dao.VendaDao;
import entity.AmortizacaoDivida;
import entity.AnoEconomico;
import entity.Cambio;
import entity.Documento;
import entity.FormaPagamento;
import entity.FormaPagamentoItem;
import entity.TbArmazem;
import entity.TbCliente;
import entity.TbProduto;
import entity.TbUsuario;
import entity.TbVenda;
import comercial.controller.AmortizacaoDividaController;
import hotel.controller.ClienteController;
import comercial.controller.DadosInstituicaoController;
import comercial.controller.FormaPagamentoController;
import comercial.controller.FormaPagamentoItemController;
import comercial.controller.UsuariosController;
import entity.Contas;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Vector;
import javax.persistence.EntityManagerFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import kitanda.util.CfMethods;
import lista.ListaVenda1;
import tesouraria.novo.controller.ContaController;
import tesouraria.novo.controller.ContaMovimentosController;
import tesouraria.novo.util.MetodosUtilTS;
import util.BDConexao;
import util.DVML;
import static util.DVML.DOC_RECIBO_RC;
import util.JPAEntityMannagerFactoryUtil;
import util.MetodosUtil;

/**
 *
 * @author lenovo
 */
public class EmissaoRecibos extends javax.swing.JFrame
{

    private static EntityManagerFactory emf = JPAEntityMannagerFactoryUtil.em;
    private static AmortizacaoDivida amortizacaoDivida = new AmortizacaoDivida();
    private static DVML.Abreviacao abreviacao;
    private static VendaDao vendaDao = new VendaDao( emf );
    private static UsuarioDao usuarioDao = new UsuarioDao( emf );
    private static DocumentoDao documentoDao;
    private static ClienteDao clienteDao = new ClienteDao( emf );
    private static CambioDao cambioDao = new CambioDao( emf );
    private static ArmazemDao armazemDao = new ArmazemDao( emf );
//    private BancoDao bancoDao = new BancoDao( emf );
    private static AnoEconomicoDao anoEconomicoDao = new AnoEconomicoDao( emf );
    private static AmortizacaoDividaDao amortizacaoDividaDao = new AmortizacaoDividaDao( emf );
//    private static ClienteDao clienteDao = new ClienteDao( emf );
    private static DefaultListModel listModelHospedes = new DefaultListModel();
    private static DefaultListModel listModelClientes = new DefaultListModel();
    private static TbVenda venda = new TbVenda();
    private static List<TbVenda> lista_clientes = new ArrayList<>();
    private static List<TbCliente> lista_cliente = new ArrayList<>();
    private static ClienteController clienteController;
    private static VendasController vendasController;
    private static FormaPagamentoController formaPagamentoController;
    private static ContaController contaController;
    private static ContaMovimentosController cmc;
    private static UsuariosController usuariosController;
    private static FormaPagamentoItemController formaPagamentoItemController;
    private static AmortizacaoDividaController amortizacaoDividaController;
    private static int idUser = 0;
    private static Cambio cambio;
    private static BDConexao conexao;
    private static AnoEconomico anoEconomico;
    private static Documento documento;
    private static int linha = 0, coordenada = 1, doc_prox_cod = 0;
//    private String prox_doc;
    private static String prox_doc;
    private static boolean aviso_continuar_documento = false;
    private static int linha_actual = -1;
    private static Double totalProcessado = 0d;

    /**
     * Creates new form EmissaoRecibos
     */
    public EmissaoRecibos( int idUser, BDConexao conexao )
    {
        initComponents();
        setLocationRelativeTo( null );
        cmbArmazem.setVisible( false );
        cmb_area_venda_restaurante.setVisible( false );
//        txtIniciaisNome.requestFocus();
//        txtIniciaisNome.addKeyListener( new TratarEvento() );
//        jListClientesDevedores.setModel( listModelHospedes );
//        cmb_area_venda_restaurante.setVisible( false );
        dc_data_documento.setDate( new Date() );
        anoEconomicoDao = new AnoEconomicoDao( emf );
        lbValorPorExtenco.setText( "" );
        clienteController = new ClienteController( conexao );
        vendasController = new VendasController( conexao );
        usuariosController = new UsuariosController( EmissaoRecibos.conexao );
        formaPagamentoItemController = new FormaPagamentoItemController( EmissaoRecibos.conexao );
        formaPagamentoController = new FormaPagamentoController( EmissaoRecibos.conexao );
        contaController = new ContaController( EmissaoRecibos.conexao );
        cmc = new ContaMovimentosController( conexao );
        documentoDao = new DocumentoDao( emf );
        cmbArmazem.setModel( new DefaultComboBoxModel( armazemDao.buscaTodos1() ) );
        setWindowsListener();
        this.idUser = idUser;
        this.conexao = conexao;
        clienteController = new ClienteController( conexao );
        amortizacaoDividaController = new AmortizacaoDividaController( conexao );
        
        jListClientesDevedores.setModel( listModelClientes );
        
        
//        actualizar_lista_clientes();
//        actulizar_lista_modelo_clientes1();
        try
        {
            listar_all_clientes_ft();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }

        try
        {
            cmbTipoDocumento.setModel( new DefaultComboBoxModel( ( Vector ) documentoDao.buscaTodos6() ) );
//        cmbTipoDocumento.setSelectedItem("Factura-Proforma");
            cmbAnoEconomico.setModel( new DefaultComboBoxModel( ( Vector ) anoEconomicoDao.buscaTodos() ) );
            lbValorPorExtenco.setText( "" );
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
            }

        } );

    }

    class TratarEvento implements KeyListener
    {

        String prefixo = "";

        public void keyPressed( KeyEvent evt )
        {

            if ( evt.getKeyCode() != KeyEvent.VK_BACK_SPACE && evt.getKeyCode() != KeyEvent.VK_ENTER )
            {
                char key = evt.getKeyChar();
//                prefixo = txtIniciaisNome.getText().trim() + key;
                adicionar( prefixo );

            }
            else if ( evt.getKeyCode() == KeyEvent.VK_BACK_SPACE )
            {
                try
                {
                    prefixo = prefixo.toString().trim().substring( 0, prefixo.length() - 1 );
                    adicionar( prefixo );
                }
                catch ( Exception e )
                {

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

    private void adicionar( String nome )
    {
        Iterator<TbCliente> iterator = lista_cliente.iterator();
        listModelHospedes.clear();
        while ( iterator.hasNext() )
        {
            TbCliente next = iterator.next();
            if ( next.getNome().replaceAll( " ", "" ).toLowerCase().contains( nome.replaceAll( " ", "" ).toLowerCase() ) )
            {
                listModelHospedes.addElement( next.getNome() );
            }

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

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabela_linhas = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        jListClientesDevedores = new javax.swing.JList<>();
        jPanel8 = new javax.swing.JPanel();
        lbTotalPagar1 = new javax.swing.JLabel();
        totalAPagarJTextField = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        btnCancelar = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        cmbAnoEconomico = new javax.swing.JComboBox<>();
        lbPreco3 = new javax.swing.JLabel();
        cmbTipoDocumento = new javax.swing.JComboBox();
        lb_proximo_documento = new javax.swing.JLabel();
        lbPreco6 = new javax.swing.JLabel();
        dc_data_documento = new com.toedter.calendar.JDateChooser();
        lb_hospede_cliente = new javax.swing.JLabel();
        lb_telefone = new javax.swing.JLabel();
        lb_email = new javax.swing.JLabel();
        lb_NIF = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        cmbArmazem = new javax.swing.JComboBox();
        cmb_area_venda_restaurante = new javax.swing.JComboBox<>();
        jPanel9 = new javax.swing.JPanel();
        lbTotalPagar2 = new javax.swing.JLabel();
        TxtTotalDesconto = new javax.swing.JTextField();
        lbValorPorExtenco = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Recibos");

        jPanel1.setBackground(new java.awt.Color(153, 153, 153));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(51, 0, 102));
        jLabel1.setText("EMISSÃO DE RECIBOS");

        tabela_linhas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "Selec.", "Abrev.", "Doc.", "Cliente", "Contacto", "Data", "Total", "V. a Pagar", "V. Pago", "Desconto", "Retenção", "V. Pendente"
            }
        )
        {
            Class[] types = new Class []
            {
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean []
            {
                true, false, false, false, false, false, false, true, false, false, false, false
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
        tabela_linhas.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                tabela_linhasMouseClicked(evt);
            }
        });
        tabela_linhas.addPropertyChangeListener(new java.beans.PropertyChangeListener()
        {
            public void propertyChange(java.beans.PropertyChangeEvent evt)
            {
                tabela_linhasPropertyChange(evt);
            }
        });
        jScrollPane1.setViewportView(tabela_linhas);
        if (tabela_linhas.getColumnModel().getColumnCount() > 0)
        {
            tabela_linhas.getColumnModel().getColumn(0).setPreferredWidth(5);
            tabela_linhas.getColumnModel().getColumn(1).setPreferredWidth(5);
            tabela_linhas.getColumnModel().getColumn(2).setPreferredWidth(55);
            tabela_linhas.getColumnModel().getColumn(3).setPreferredWidth(120);
            tabela_linhas.getColumnModel().getColumn(4).setPreferredWidth(45);
            tabela_linhas.getColumnModel().getColumn(5).setPreferredWidth(29);
            tabela_linhas.getColumnModel().getColumn(7).setPreferredWidth(40);
            tabela_linhas.getColumnModel().getColumn(9).setPreferredWidth(40);
            tabela_linhas.getColumnModel().getColumn(10).setPreferredWidth(28);
            tabela_linhas.getColumnModel().getColumn(11).setPreferredWidth(45);
        }

        jListClientesDevedores.setBorder(javax.swing.BorderFactory.createTitledBorder("Clientes Devedores"));
        jListClientesDevedores.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jListClientesDevedores.setModel(new javax.swing.AbstractListModel<String>()
        {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jListClientesDevedores.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                jListClientesDevedoresMouseClicked(evt);
            }
        });
        jListClientesDevedores.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                jListClientesDevedoresKeyPressed(evt);
            }
        });
        jScrollPane2.setViewportView(jListClientesDevedores);

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel8.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lbTotalPagar1.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbTotalPagar1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbTotalPagar1.setText("Total a Pagar :");
        jPanel8.add(lbTotalPagar1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 110, 40));

        totalAPagarJTextField.setEditable(false);
        totalAPagarJTextField.setBackground(new java.awt.Color(0, 0, 0));
        totalAPagarJTextField.setFont(new java.awt.Font("Tahoma", 1, 23)); // NOI18N
        totalAPagarJTextField.setForeground(new java.awt.Color(255, 255, 255));
        totalAPagarJTextField.setCaretColor(new java.awt.Color(255, 255, 255));
        totalAPagarJTextField.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                totalAPagarJTextFieldActionPerformed(evt);
            }
        });
        jPanel8.add(totalAPagarJTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 10, 320, 40));

        btnCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/LOGOUT - VERMELHO/Logout 32x32.png"))); // NOI18N
        btnCancelar.setAlignmentX(0.5F);
        btnCancelar.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnCancelarActionPerformed(evt);
            }
        });

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/1323444801_currency_dollar red.png"))); // NOI18N
        jButton1.setText("Forma Pagamento");
        jButton1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(78, 78, 78))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnCancelar, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        cmbAnoEconomico.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbAnoEconomico.setEnabled(false);
        cmbAnoEconomico.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbAnoEconomicoActionPerformed(evt);
            }
        });

        lbPreco3.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbPreco3.setText("Doc.");

        cmbTipoDocumento.setBackground(new java.awt.Color(0, 51, 102));
        cmbTipoDocumento.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 14)); // NOI18N
        cmbTipoDocumento.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbTipoDocumentoActionPerformed(evt);
            }
        });

        lb_proximo_documento.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        lb_proximo_documento.setText("PRÓX. DOC. : XX PP/A1");

        lbPreco6.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbPreco6.setText("Data:");

        dc_data_documento.setEnabled(false);
        dc_data_documento.setFont(new java.awt.Font("Lucida Grande", 1, 12)); // NOI18N

        lb_hospede_cliente.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lb_hospede_cliente.setText("Xxxxxx Yxxxx Vvvv");

        lb_telefone.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lb_telefone.setText("999 999 999");

        lb_email.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lb_email.setText("Email: xxx@gmail.com");

        lb_NIF.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lb_NIF.setText("Email: xxx@gmail.com");

        jLabel2.setText("Cliente: ");

        jLabel3.setText("Telefone");

        jLabel4.setText("Email:");

        jLabel5.setText("NIF:");

        cmbArmazem.setBackground(new java.awt.Color(0, 51, 102));
        cmbArmazem.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 1, 12)); // NOI18N
        cmbArmazem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbArmazemActionPerformed(evt);
            }
        });

        cmb_area_venda_restaurante.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cmb_area_venda_restaurante.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Restaurante", "Alojamento" }));
        cmb_area_venda_restaurante.setEnabled(false);

        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel9.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lbTotalPagar2.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbTotalPagar2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbTotalPagar2.setText("Desconto :");
        jPanel9.add(lbTotalPagar2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 110, 40));

        TxtTotalDesconto.setEditable(false);
        TxtTotalDesconto.setBackground(new java.awt.Color(0, 0, 0));
        TxtTotalDesconto.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        TxtTotalDesconto.setForeground(new java.awt.Color(255, 255, 255));
        TxtTotalDesconto.setCaretColor(new java.awt.Color(255, 255, 255));
        TxtTotalDesconto.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                TxtTotalDescontoActionPerformed(evt);
            }
        });
        jPanel9.add(TxtTotalDesconto, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 20, 180, 26));

        lbValorPorExtenco.setFont(new java.awt.Font("Lucida Grande", 1, 16)); // NOI18N
        lbValorPorExtenco.setForeground(new java.awt.Color(204, 0, 0));
        lbValorPorExtenco.setText("VALOR POR EXTENSO");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(lbPreco3)
                                        .addGap(9, 9, 9)
                                        .addComponent(cmbTipoDocumento, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, 462, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, 462, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1161, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(lb_proximo_documento, javax.swing.GroupLayout.PREFERRED_SIZE, 307, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(lbPreco6)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(dc_data_documento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(17, 17, 17)
                                        .addComponent(cmbAnoEconomico, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(cmbArmazem, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(cmb_area_venda_restaurante, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(24, 24, 24))
                                    .addComponent(lbValorPorExtenco, javax.swing.GroupLayout.PREFERRED_SIZE, 986, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addContainerGap(16, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 64, Short.MAX_VALUE)
                                .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jLabel5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lb_NIF, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lb_hospede_cliente, javax.swing.GroupLayout.PREFERRED_SIZE, 406, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lb_telefone, javax.swing.GroupLayout.PREFERRED_SIZE, 296, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lb_email, javax.swing.GroupLayout.PREFERRED_SIZE, 363, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(174, 174, 174))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbPreco3)
                            .addComponent(cmbTipoDocumento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(lb_proximo_documento, javax.swing.GroupLayout.Alignment.TRAILING))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cmbAnoEconomico, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbPreco6, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(dc_data_documento, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(cmbArmazem, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(cmb_area_venda_restaurante, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(31, 31, 31)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lb_email)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(lb_hospede_cliente))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(lb_telefone))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lb_NIF)
                    .addComponent(jLabel5))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lbValorPorExtenco, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 471, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jListClientesDevedoresMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_jListClientesDevedoresMouseClicked
    {//GEN-HEADEREND:event_jListClientesDevedoresMouseClicked
        adicionar_tabela();
        setDadosClientes();
        actualizar_Desconto_total();
//        String selectedHospespe = jListHospede.getSelectedValue();

    }//GEN-LAST:event_jListClientesDevedoresMouseClicked

    private void tabela_linhasMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_tabela_linhasMouseClicked
    {//GEN-HEADEREND:event_tabela_linhasMouseClicked
//        linha_selecionada();
//        actualizarQtdTable();
    }//GEN-LAST:event_tabela_linhasMouseClicked

    private void tabela_linhasPropertyChange(java.beans.PropertyChangeEvent evt)//GEN-FIRST:event_tabela_linhasPropertyChange
    {//GEN-HEADEREND:event_tabela_linhasPropertyChange
//        if ( tabela_linhas.getSelectedColumn() == 5 )
//        {
//            System.out.println( "Qtd......" );
////            actualizarValorPagarTable();
//            
//        }

        try
        {
//            actualizar_valor_tabela( evt );
            atualizarResumo( evt );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }

    }//GEN-LAST:event_tabela_linhasPropertyChange

    private void totalAPagarJTextFieldActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_totalAPagarJTextFieldActionPerformed
    {//GEN-HEADEREND:event_totalAPagarJTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_totalAPagarJTextFieldActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnCancelarActionPerformed
    {//GEN-HEADEREND:event_btnCancelarActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton1ActionPerformed
    {//GEN-HEADEREND:event_jButton1ActionPerformed
        if ( MetodosUtil.licencaValidada( conexao ) )
        {
            finalizar_recibo();
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void cmbAnoEconomicoActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbAnoEconomicoActionPerformed
    {//GEN-HEADEREND:event_cmbAnoEconomicoActionPerformed
        // TODO add your handling code here:
        mostrar_proximo_codigo_documento();
        actualizar_abreviacao();
    }//GEN-LAST:event_cmbAnoEconomicoActionPerformed

    private void cmbTipoDocumentoActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbTipoDocumentoActionPerformed
    {//GEN-HEADEREND:event_cmbTipoDocumentoActionPerformed
        // TODO add your handling code here:
        mostrar_proximo_codigo_documento();
        actualizar_abreviacao();
        //        desabilitar_campos();
        //        selecionar_documento();
        //        atualizarCliente();
    }//GEN-LAST:event_cmbTipoDocumentoActionPerformed

    private void jListClientesDevedoresKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_jListClientesDevedoresKeyPressed
    {//GEN-HEADEREND:event_jListClientesDevedoresKeyPressed
        setDadosClientes();
    }//GEN-LAST:event_jListClientesDevedoresKeyPressed

    private void TxtTotalDescontoActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_TxtTotalDescontoActionPerformed
    {//GEN-HEADEREND:event_TxtTotalDescontoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TxtTotalDescontoActionPerformed

    private void cmbArmazemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbArmazemActionPerformed
    {//GEN-HEADEREND:event_cmbArmazemActionPerformed
        // TODO add your handling code here:
//        adicionar_preco_quantidade();
    }//GEN-LAST:event_cmbArmazemActionPerformed

    private void actualizarValorPagarTable()
    {

        linha_actual = tabela_linhas.getSelectedRow();

        if ( linha_actual > -1 )
        {

//            int codProduto = Integer.parseInt( table.getValueAt( linha_actual, 0 ).toString() );
//            double valor_atribuido = Double.parseDouble( tabela_linhas.getValueAt( linha_actual, 6 ).toString() );
//            TbProduto produtoLocal = ( TbProduto ) produtosController.findById( codProduto );
//            TbTipoProduto tipoProduto = ( TbTipoProduto ) tipoProdutoController.findById( produtoLocal.getCodTipoProduto().getCodigo() );
            double valor_pagar;

            try
            {
                valor_pagar = Double.parseDouble( tabela_linhas.getValueAt( linha_actual, 7 ).toString() );
            }
            catch ( NumberFormatException e )
            {
                resetValue( "Erro de formatação do valor.\nAtenção: Tem que ser número.", 7 );
                return;
            }

            if ( valor_pagar <= 0 )
            {
                valor_pagar = 1;
                resetValue( "valor não pode ser zero(0) ou número négativo", 7 );
            }

//            if ( possivel_quantidade( codProduto, qtd ) || tipoProduto.getFkFamilia().getPkFamilia() == DVML.COD_SERVICO )
//            {
            actuazlizar_valor_pagar_tabela_formulario( String.valueOf( valor_pagar ) );
//                setTotalRetencao();
//                setTotalPagar();
//                calculaTotalIVA();
//                valor_por_extenco();
//            }
//            else
//            {
//                resetValue( "Não é possivel para esta quantidade.", 4 );
//            }
        }
    }

    private static void actuazlizar_valor_pagar_tabela_formulario( String valor_pagar_par )
    {
        DefaultTableModel modelo = ( DefaultTableModel ) tabela_linhas.getModel();
        double valor_pagar = Double.parseDouble( valor_pagar_par );
        double valor_doc = Double.parseDouble( modelo.getValueAt( linha_actual, 6 ).toString() );
        double valor_atribuido = Double.parseDouble( String.valueOf( modelo.getValueAt( linha_actual, 8 ) ) );

//        double total_iliquido_linha = (( preco_venda * qtd ) - desconto);
        String total_iliquido_linha = String.valueOf( getValorIliquido(
                valor_pagar,
                valor_doc,
                valor_atribuido
        ) );

//        String total_liquido_linha = CfMethods.formatarComoMoeda( getValorComImpostoIva(
//                qtd,
//                taxa,
//                preco_venda,
//                desconto
//        ) );
        modelo.setValueAt( valor_pagar, linha_actual, 7 );
//        modelo.setValueAt( desconto, linha_actual, 5 );
//
        modelo.setValueAt( total_iliquido_linha, linha_actual, 11 );
//        modelo.setValueAt( total_liquido_linha, linha_actual, 10 );
        //a linha_actual recebe o default
        linha_actual = -1;

    }

    private static double getValorIliquido( double valor_pagar, double valor_doc, double valor_atribuido )
    {
        double subtotal_linha = ( valor_doc - valor_pagar );
        double valor_pendente = ( subtotal_linha - valor_pagar );
        return ( ( subtotal_linha - valor_pendente ) );

    }

    public static void actualizar_lista_clientes()
    {
//        lista_cliente = clienteController.listarTodos( DVML.CLIENTE_SINGULAR );
        lista_cliente = clienteController.listarTodos();
        filtrar_lista_cliente();

    }

    public static void actualizar_lista_clientes1()
    {
//        lista_clientes = clienteController.listarTodos( DVML.CLIENTE_SINGULAR );
//        lista_clientes = vendaController.
//        filtrar_lista_cliente1();

    }

    private static void filtrar_lista_cliente()
    {

        for ( int i = 0; i < lista_cliente.size(); i++ )
        {
            TbCliente get = lista_cliente.get( i );
//            if ( existe_cliente_lista( get.getNome() ) )
//            {
//                lista_cliente.remove( get );
//
//            }
        }
    }

    private static void filtrar_lista_cliente1()
    {

        for ( int i = 0; i < lista_clientes.size(); i++ )
        {
            TbVenda get = lista_clientes.get( i );
//            if ( existe_cliente_lista( get.getNome() ) )
//            {
//                lista_cliente.remove( get );
//
//            }
        }
    }

    public static void actulizar_lista_modelo_clientes()
    {
        actualizar_lista_clientes();
        if ( !Objects.isNull( lista_cliente ) )
        {
            listModelClientes.clear();
            for ( TbCliente cliente_object : lista_cliente )
            {
                listModelClientes.addElement( cliente_object.getNome() );
            }
        }

    }

    public static void actulizar_lista_modelo_clientes1()
    {
        actualizar_lista_clientes1();
        if ( !Objects.isNull( lista_clientes ) )
        {
            listModelClientes.clear();
            for ( TbVenda cliente_object : lista_clientes )
            {
                listModelClientes.addElement( cliente_object.getCodigoCliente().getNome() );
            }
        }

    }

    public static void listar_all_clientes_ft()
    {

        try
        {

//            List<TbVenda> item = vendaController.getAllVendasClientes( DVML.DOC_FACTURA_FT );
            List<TbVenda> item = vendaDao.getAllVendasClientes( DVML.DOC_FACTURA_FT );

//            lista_clientes.clear();
            listModelClientes.clear();
            for ( int i = 0; i < item.size(); i++ )
            {
                listModelClientes.addElement( item.get( i ).getCodigoCliente().getNome() );

            }

        }
        catch ( Exception e )
        {
        }

    }

    public static void adicionar_tabela()
    {

        DefaultTableModel modelo = ( DefaultTableModel ) tabela_linhas.getModel();
        tabela_linhas.setRowHeight( 30 );
        try
        {

            List<TbVenda> list = vendasController.findVendasByClientesDoc( getIdCliente() );
//            List<TbVenda> list = vendaController.findVendasByClientes2( getIdCliente(), venda.getCodigo() );

            modelo.setRowCount( 0 );
            for ( int i = 0; i < list.size(); i++ )
            {
                TbVenda get = list.get( i );
                double valor_pendente = amortizacaoDividaController.getValorPendente( get.getCodigo() );
                double valor_atribuido = amortizacaoDividaController.getValorAtribuido( get.getCodigo() );

                int codigo_cliente = get.getCodigoCliente().getCodigo();
                TbCliente cliente = ( TbCliente ) clienteController.findById( codigo_cliente );
//                AmortizacaoDivida amortizacaoD-ivida_local = amortizacaoDividaDao.getAllAmortizacaoByIdCliente( getIdCliente() );

                if ( valor_atribuido < get.getTotalVenda().doubleValue() )
                {
                    modelo.addRow( new Object[]
                    {
                        false,
                        "FT",
                        get.getCodFact(),
                        cliente.getNome(),
                        cliente.getTelefone(),
                        get.getDataVenda(),
                        get.getTotalVenda(),
                        0.0,
                        valor_atribuido,
                        get.getDescontoTotal(),
                        0.0,
                        valor_pendente,
                    } );

                }

            }

        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }

    }

    private void setDadosClientes()
    {
        TbCliente clienteSeleccionado = getHospedeSeleccionado();
        System.out.println( "Cheguei na lista de clientes..." );
        if ( !Objects.isNull( clienteSeleccionado ) )
        {
            lb_hospede_cliente.setText( clienteSeleccionado.getNome() );
            System.out.println( "Nome Cliente..." + clienteSeleccionado.getNome() );
            lb_telefone.setText( clienteSeleccionado.getTelefone() );
            lb_email.setText( clienteSeleccionado.getEmail() );
            lb_NIF.setText( clienteSeleccionado.getNif() );
        }
        else
        {
            limpar_dados_clientes_hospede();
        }

    }

    private void limpar_dados_clientes_hospede()
    {

        lb_hospede_cliente.setText( "" );
        lb_telefone.setText( "" );
        lb_email.setText( "" );
        lb_NIF.setText( "" );
    }

    private static TbCliente getHospedeSeleccionado()
    {
        String selectedHospespe = jListClientesDevedores.getSelectedValue();
        return clienteController.findByNome( selectedHospespe );
    }

    private static int getIdCliente()
    {
        try
        {
            return clienteController.findByNome( jListClientesDevedores.getSelectedValue().toString() ).getCodigo();
        }
        catch ( Exception e )
        {
            return 0;
        }

    }

    private static String getNomeCliente()
    {

        return jListClientesDevedores.getSelectedValue();

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
                if ( "Windows".equals( info.getName() ) )
                {
                    javax.swing.UIManager.setLookAndFeel( info.getClassName() );
                    break;
                }
            }
        }
        catch ( ClassNotFoundException ex )
        {
            java.util.logging.Logger.getLogger( EmissaoRecibos.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        catch ( InstantiationException ex )
        {
            java.util.logging.Logger.getLogger( EmissaoRecibos.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        catch ( IllegalAccessException ex )
        {
            java.util.logging.Logger.getLogger( EmissaoRecibos.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        catch ( javax.swing.UnsupportedLookAndFeelException ex )
        {
            java.util.logging.Logger.getLogger( EmissaoRecibos.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater( new Runnable()
        {
            public void run()
            {
                new EmissaoRecibos( 15, new BDConexao() ).setVisible( true );
            }
        } );
    }

    public static void linha_selecionada()
    {
        DefaultTableModel modelo = ( DefaultTableModel ) tabela_linhas.getModel();
        int selectedRow = tabela_linhas.getSelectedRow();
        String checkbox = modelo.getValueAt( selectedRow, 0 ).toString();
//        JOptionPane.showMessageDialog( null, "Linha selecionada" +selectedRow);
        JOptionPane.showMessageDialog( null, "Linha selecionada" + checkbox );
    }

    private void resetValue( String msg, int columnValue )
    {
        System.out.println( "Cheguei aqui..." );
        tabela_linhas.setValueAt( 1, linha_actual, columnValue );
        JOptionPane.showMessageDialog( null, msg );
        tabela_linhas.clearSelection();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static javax.swing.JTextField TxtTotalDesconto;
    private javax.swing.JButton btnCancelar;
    public static javax.swing.JComboBox<String> cmbAnoEconomico;
    public static javax.swing.JComboBox cmbArmazem;
    public static javax.swing.JComboBox cmbTipoDocumento;
    public static javax.swing.JComboBox<String> cmb_area_venda_restaurante;
    private static com.toedter.calendar.JDateChooser dc_data_documento;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private static javax.swing.JList<String> jListClientesDevedores;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lbPreco3;
    private javax.swing.JLabel lbPreco6;
    private javax.swing.JLabel lbTotalPagar1;
    private javax.swing.JLabel lbTotalPagar2;
    public static javax.swing.JLabel lbValorPorExtenco;
    private javax.swing.JLabel lb_NIF;
    private javax.swing.JLabel lb_email;
    private javax.swing.JLabel lb_hospede_cliente;
    private static javax.swing.JLabel lb_proximo_documento;
    private javax.swing.JLabel lb_telefone;
    private static javax.swing.JTable tabela_linhas;
    public static javax.swing.JTextField totalAPagarJTextField;
    // End of variables declaration//GEN-END:variables

    private void actualizar_valor_tabela( PropertyChangeEvent evt )
    {
        linha_actual = tabela_linhas.getSelectedRow();
        DefaultTableModel modelo = ( DefaultTableModel ) tabela_linhas.getModel();

        /**
         * produto.getCodigo(), produto.getDesignacao(), getPrecoCompra(),
         * getQuantidade(), getUnidade_Produto(), 0, getTaxaImposto(),
         * getQuantidade() * getPrecoCompra(), MetodosUtil.getValorComIVA(
         * getQuantidade(), getTaxaImposto(), getPrecoCompra(), 0 )
         */
        double valor_doc = Double.parseDouble( String.valueOf( modelo.getValueAt( linha_actual, 6 ) ) );
//        double valor_pagar = Double.parseDouble( String.valueOf( modelo.getValueAt( linha_actual, 5 ) ) );
        double valor_pagar = 0.0;

//        if ( preco_compra != 0 && quantidade != 0 )
//        {
//            double valor_atribuido = Double.parseDouble( String.valueOf( modelo.getValueAt( linha_actual, 6 ) ) );
//            if ( desconto >= 0 && desconto <= 100 )
//            {
        double valor_pendente = Double.parseDouble( String.valueOf( modelo.getValueAt( linha_actual, 11 ) ) );

        double valor_atribuido = valor_doc - valor_pagar;
//                double total_item_com_iva = MetodosUtil.getValorComIVA( quantidade, taxa, preco_compra, desconto );

        modelo.setValueAt( valor_doc, linha_actual, 6 );
        modelo.setValueAt( valor_pagar, linha_actual, 7 );
        modelo.setValueAt( valor_atribuido, linha_actual, 8 );
        modelo.setValueAt( valor_pendente, linha_actual, 11 );
//                modelo.setValueAt( total_item, linha_actual, 7 );
//                modelo.setValueAt( total_item_com_iva, linha_actual, 8 );

//            }
//            else
//            {
//                modelo.setValueAt( 0.0, linha_actual, 5 );
//                JOptionPane.showMessageDialog( null, "Caro usuário o desconto não pode ser maior que 100%", "AVISO", JOptionPane.WARNING_MESSAGE );
//            }
//        }
//        else
//        {
//
//            remover_item_carrinho();
//            // acrescentar_um_linha_tabela_blank();
//        }
//        actualizar_total();
//            atualizarResumo();
    }

//    private static void atualizarResumo()
//    {
//        DefaultTableModel modelo = (DefaultTableModel)tabela_linhas.getModel();
//        String faturaSlecionada = ( String ) faturaJSpinner.getValue();
//        Double totalAmortizado = AmortizacaoDividaDao.findTotalAmortizadoByRefDoc( ( String ) faturaJSpinner.getValue() );
//        Double valorEntregue = totalProcessado;
//        Double totalAPagar = totalProcessado - totalAmortizado;
//        Double troco = ( valorEntregue - totalAPagar ) > 0 ? valorEntregue - totalAPagar : 0;
////        trocoJTextField.setText( CfMethods.formatarComoMoeda( troco ) );
//        totalPagoJTextField.setText( CfMethods.formatarComoMoeda( totalAmortizado ) );
//        valorTotalFaturaJTextField.setText( CfMethods.formatarComoMoeda( totalProcessado ) );
//        totalAPagarJTextField.setText( CfMethods.formatarComoMoeda( totalAPagar ) );
////        valorEntregueJSpinner.setModel( criarSpinnerDoubleModel( 0, totalAPagar, valorEntregue ) );
//
//    }
    private static void atualizarResumo( PropertyChangeEvent evt )
    {

        DefaultTableModel modelo = ( DefaultTableModel ) tabela_linhas.getModel();
        linha_actual = tabela_linhas.getSelectedRow();
        System.out.println( "Antes Linha Actual: " + linha_actual );
        double valor_pagar = 0;
        double valorPendente;
        double valorAtribuido;
        String codFact;

        if ( tabela_linhas.getSelectedColumn() == 0 )
        {
            boolean estado = ( Boolean ) modelo.getValueAt( linha_actual, 0 );
            System.out.println( "ESTADO: " + estado );
            modelo.setValueAt( estado, linha_actual, 0 );
            codFact = tabela_linhas.getValueAt( linha_actual, 2 ).toString();
            valorPendente = amortizacaoDividaController.getValorPendenteByCodFact( codFact );
            valorAtribuido = amortizacaoDividaController.getValorAtribuidoByCodFact( codFact );
            modelo.setValueAt( 0.0, linha_actual, 7 );
            modelo.setValueAt( valorAtribuido, linha_actual, 8 );
            modelo.setValueAt( valorPendente, linha_actual, 11 );

        }

        else if ( linha_actual > -1 )
        {
            System.out.println( "Depois Linha Actual: " + linha_actual );
            codFact = tabela_linhas.getValueAt( linha_actual, 2 ).toString();
            TbVenda vendaByCodFact = vendasController.getVendaByCodFactFT( codFact );
//        boolean cb = tabela_linhas.getValueAt( linha_actual, 0 ).equals( evt );

            Double totalAmortizado = amortizacaoDividaController.getValorAtribuidoByCodFact( codFact );
            Double valorEntregue = totalProcessado;
            Double troco = 0d;

            double totalAPagar;
//            totalAPagar = Double.parseDouble( String.valueOf( modelo.getValueAt( linha_actual, 5 ) ) );
            totalAPagar = Double.parseDouble( String.valueOf( modelo.getValueAt( linha_actual, 7 ) ) );

            valorAtribuido = amortizacaoDividaController.getValorAtribuidoByCodFact( codFact );
//            valorPago = Double.parseDouble( String.valueOf( modelo.getValueAt( linha_actual, 6 ) ) );

            double totalFactura = Double.parseDouble( String.valueOf( modelo.getValueAt( linha_actual, 6 ) ) );

            valorPendente = amortizacaoDividaController.getValorPendente( vendaByCodFact.getCodigo() );
//            valorPendente = amortizacaoDividaController.getValorPendenteByCodFact( codFact );

//            if ( totalAPagar != 0.0 )
            if ( totalAPagar > 0.0 )
            {
                System.out.println( "Cheguei depois do IF" );
//            totalAPagar = totalProcessado - totalAmortizado;
                valorAtribuido = valorAtribuido + totalAPagar;

                System.out.println( "TotalFactura  vazio: " + Objects.nonNull( totalFactura ) );
                System.out.println( "Valor Atribuido vazio: " + Objects.nonNull( valorAtribuido ) );
                valorPendente = totalFactura - valorAtribuido;
                totalAPagar = totalFactura - totalAmortizado;
                System.out.println( "Cheguei depois do Total pagar" );
                System.out.println( "Total Amortizado: " + totalAmortizado );
                troco = ( valorEntregue - totalAPagar ) > 0 ? valorEntregue - totalAPagar : 0;

                modelo.setValueAt( true, linha_actual, 0 );

            }
            else
            {

                modelo.setValueAt( false, linha_actual, 0 );
//            remover_item_carrinho();
                // acrescentar_um_linha_tabela_blank();
            }

            modelo.setValueAt( valorAtribuido, linha_actual, 8 );
            modelo.setValueAt( valorPendente, linha_actual, 11 );

//        setTotalPagar();
        }

        actualizar_total();
        actualizar_Desconto_total();
        valor_por_extenco();

    }

//    private static void atualizarTabela()
//    {
//
//        try
//        {
//            Object refDocObject = faturaJSpinner.getValue();
//            String refDoc = Objects.isNull( refDocObject ) ? "" : ( String ) refDocObject;
//
//            System.err.println( "" );
//            totalProcessado = 0.0;
////            totalBonusCliente = 0.0;
//            int colIndex = 0;
//            final int pk_venda = colIndex++;
//            final int documento_designacao = colIndex++;
//            final int tb_venda_cod_fact = colIndex++;
//            final int moeda_abreviacao = colIndex++;
////        final int tb_banco_descrisao = colIndex++;
//            final int tb_venda_dataVenda = colIndex++;
//            final int tb_venda_hora = colIndex++;
//            final int tb_produto_codigo = colIndex++;
//            final int tb_produto_designacao = colIndex++;
//            final int tb_item_venda_codigo_isensao = colIndex++;
//            final int tb_item_venda_quantidade = colIndex++;
//            final int tb_preco_preco_venda = colIndex++;
//            final int tb_item_venda_desconto = colIndex++;
//            final int tb_item_venda_valor_iva = colIndex++;
//            final int tb_item_venda_total = colIndex++;
//            final int tb_venda_total_geral = colIndex++;
//            final int tb_venda_desconto_comercial = colIndex++;
//            final int tb_venda_desconto_financeiro = colIndex++;
//            final int tb_venda_total_iva = colIndex++;
//            final int tb_venda_total_venda = colIndex++;
//            final int tb_venda_valor_entregue = colIndex++;
//            final int tb_venda_troco = colIndex++;
//            final int tb_venda_total_por_extenso = colIndex++;
//            /*
//             
//             */
////
//
//            List<Object[]> relatorios = UtilDao.getDetalhesFatura( refDoc );
//            DefaultTableModel defaultTableModel = new DefaultTableModel( new Object[]
//            {
//                "Cod. Prod",
//                "Produto",
//                "Cod. Isensao",
//                "Qauntidade",
//                "Preco Unit",
//                "Desconto",
//                "Taxa Iva",
//                "Subtotal"
//            }, 0 );
//
//            if ( !Objects.isNull( relatorios ) )
//            {
//                for ( Object[] relatorio : relatorios )
//                {
//
//                    BigDecimal bigDecimalDesconto = new BigDecimal( relatorio[ tb_item_venda_desconto ].toString() );
//                    BigDecimal bigDecimalTotal = new BigDecimal( relatorio[ tb_item_venda_total ].toString() );
//
//                    System.out.println( "CODIGO: " + relatorio[ tb_produto_codigo ] );
//                    System.out.println( "DESIGNACAO: " + relatorio[ tb_produto_designacao ] );
//                    System.out.println( "CODIGO ISENSAO: " + relatorio[ tb_item_venda_codigo_isensao ] );
//                    System.out.println( "QTD: " + relatorio[ tb_item_venda_quantidade ] );
//                    System.out.println( "PRECO VENDA: " + relatorio[ tb_preco_preco_venda ] );
//                    System.out.println( "DESCONTO: " + bigDecimalDesconto );
//                    System.out.println( "IVA: " + relatorio[ tb_item_venda_valor_iva ] );
//                    System.out.println( "TOTAL: " + bigDecimalTotal );
//
//                    Object[] rowData = new Object[]
//                    {
//                        relatorio[ tb_produto_codigo ],
//                        relatorio[ tb_produto_designacao ],
//                        relatorio[ tb_item_venda_codigo_isensao ],
//                        relatorio[ tb_item_venda_quantidade ],
//                        //                    CfMethods.formatarComoMoeda( (double) relatorio[tb_preco_preco_venda] ),
//                        relatorio[ tb_preco_preco_venda ],
//                        CfMethods.formatarComoMoeda( bigDecimalDesconto ),
//                        relatorio[ tb_item_venda_valor_iva ],
//                        CfMethods.formatarComoMoeda( bigDecimalTotal )
//                    };
//                    totalProcessado += bigDecimalTotal.doubleValue();
//                    defaultTableModel.addRow( rowData );
//                }
//            }
//
//            detalhesJTable.setModel( defaultTableModel );
//            detalhesJTable.getColumnModel().getColumn( 0 ).setMinWidth( 0 );
//            detalhesJTable.getColumnModel().getColumn( 0 ).setMaxWidth( 100 );
//            detalhesJTable.getColumnModel().getColumn( 2 ).setMinWidth( 0 );
//            detalhesJTable.getColumnModel().getColumn( 2 ).setMaxWidth( 200 );
//            detalhesJTable.getColumnModel().getColumn( 3 ).setMinWidth( 0 );
//            detalhesJTable.getColumnModel().getColumn( 3 ).setMaxWidth( 100 );
//            detalhesJTable.getColumnModel().getColumn( 4 ).setMinWidth( 0 );
//            detalhesJTable.getColumnModel().getColumn( 4 ).setMaxWidth( 100 );
//            detalhesJTable.getColumnModel().getColumn( 5 ).setMinWidth( 100 );
//            detalhesJTable.getColumnModel().getColumn( 5 ).setMaxWidth( 250 );
//
//            detalhesJTable.getColumnModel().getColumn( 6 ).setMinWidth( 150 );
//            detalhesJTable.getColumnModel().getColumn( 6 ).setMaxWidth( 250 );
//
//            detalhesJTable.getColumnModel().getColumn( 7 ).setMinWidth( 150 );
//            detalhesJTable.getColumnModel().getColumn( 7 ).setMaxWidth( 250 );
//
//            atualizarResumo();
//            ativarDesativarCamposBonusCliente();
//
//        }
//        catch ( Exception e )
//        {
//        }
//
//    }
//    public static void setTotalPagar()
//    {
//
//        DefaultTableModel modelo = ( DefaultTableModel ) tabela_linhas.getModel();
//        double total_liquido = 0;
//
//        for ( int i = 0; i < modelo.getRowCount(); i++ )
//        {
//            total_liquido += CfMethods.parseMoedaFormatada( String.valueOf( modelo.getValueAt( i, 7 ) ) );
//        }
//        totalAPagarJTextField.setText( CfMethods.formatarComoMoeda( total_liquido ) );
//
//    }
    public static void setTotalDesconto()
    {

        DefaultTableModel modelo = ( DefaultTableModel ) tabela_linhas.getModel();
        double total_liquido = 0;

        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {
            total_liquido += CfMethods.parseMoedaFormatada( String.valueOf( modelo.getValueAt( i, 9 ) ) );
        }
        TxtTotalDesconto.setText( CfMethods.formatarComoMoeda( total_liquido ) );

    }

    private static void actualizar_total()
    {
//        txtTotal_AOA_Iliquido.setText( CfMethods.formatarComoMoeda( getTotalIliquido() ) );
//        txtTotal_AOA_IVA.setText( CfMethods.formatarComoMoeda( getTotalIVASobreDesconto() ) );
//        txtTotal_AOA_Desconto.setText( CfMethods.formatarComoMoeda( getDescontoComercial() + getDescontoFinanceiro() ) );
        totalAPagarJTextField.setText( CfMethods.formatarComoMoeda( getTotalAOALiquido() ) );

//        valor_por_extenco();
    }

    private static void actualizar_Desconto_total()
    {
//        txtTotal_AOA_Iliquido.setText( CfMethods.formatarComoMoeda( getTotalIliquido() ) );
//        txtTotal_AOA_IVA.setText( CfMethods.formatarComoMoeda( getTotalIVASobreDesconto() ) );
//        txtTotal_AOA_Desconto.setText( CfMethods.formatarComoMoeda( getDescontoComercial() + getDescontoFinanceiro() ) );
        TxtTotalDesconto.setText( CfMethods.formatarComoMoeda( getTotalDescontoAOALiquido() ) );

//        valor_por_extenco();
    }

    private static double getTotalAOALiquido()
    {
        double valores = getTotalliquido();
//        double valores = ( getTotalIliquido() + getTotalImposto() );
//        double descontos = ( getDescontoComercial() + getDescontoFinanceiro() );
//        System.out.println( "TotalIliquido: " + getTotalIliquido() );
//        System.out.println( "TotalImposto: " + getTotalImposto() );
//        System.out.println( "TotalDescontoComercial: " + getDescontoComercial() );
//        System.out.println( "TotalDescontoFinanceiro: " + getDescontoFinanceiro() );
//        System.out.println( "Total Liquido: " + ( valores - descontos ) );
//        return ( valores - descontos );
        return valores;
    }

    private static double getTotalDescontoAOALiquido()
    {
//        double valores = getTotalliquido();
//        double valores = ( getTotalIliquido() + getTotalImposto() );
        double descontos = ( getDescontoTotal() );
//        System.out.println( "TotalIliquido: " + getTotalIliquido() );
//        System.out.println( "TotalImposto: " + getTotalImposto() );
//        System.out.println( "TotalDescontoComercial: " + getDescontoComercial() );
//        System.out.println( "TotalDescontoFinanceiro: " + getDescontoFinanceiro() );
//        System.out.println( "Total Liquido: " + ( valores - descontos ) );
//        return ( valores - descontos );
        return descontos;
    }

    private static double getDescontoTotal()
    {
        DefaultTableModel modelo = ( DefaultTableModel ) tabela_linhas.getModel();
        double qtd = 0d;
        double desconto_total = 0d, preco_unitario = 0d, desconto_valor_linha = 0d;

        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {
//            preco_unitario = CfMethods.parseMoedaFormatada( modelo.getValueAt( i, 3 ).toString() );
//            qtd = Double.parseDouble( modelo.getValueAt( i, 4 ).toString() );
            desconto_valor_linha = Double.parseDouble( modelo.getValueAt( i, 9 ).toString() );
//            double valor_unitario = ( preco_unitario * qtd );
            desconto_total += desconto_valor_linha;

        }

        return desconto_total;
    }

    private static double getTotalliquido()
    {
        DefaultTableModel modelo = ( DefaultTableModel ) tabela_linhas.getModel();

        double total_liquido = 0;

        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {
            total_liquido += Double.parseDouble( modelo.getValueAt( i, 7 ).toString() );
//            total_liquido = CfMethods.parseMoedaFormatada( modelo.getValueAt( i, 5 ).toString() );

//            total_iliquido += ( preco_unitario * qtd );
        }

        return total_liquido;
    }

    public static void salvar( double valor_entregue )
    {
        if ( !MetodosUtil.tabela_vazia( tabela_linhas ) )
        {
            Date data_documento = dc_data_documento.getDate();
//            if ( verifica_ano_documento_igual_economico() )
//            {

            if ( data_documento_superior_ou_igual_ao_ultimo_doc() )
            {

                salvar_venda( valor_entregue );

            }
            else
            {
                JOptionPane.showMessageDialog( null, "O documento não pode ser processado porque possui uma data inferior ao úlimo documento efectuado", "AVISO", JOptionPane.WARNING_MESSAGE );
            }

//            }
//            else
//            {
//                JOptionPane.showMessageDialog( null, "A data do documento a ser emitido deve estar no intervalo do ano economico", "Aviso", JOptionPane.WARNING_MESSAGE );
//            }
        }
        else
        {
            JOptionPane.showMessageDialog( null, "Caro usuário adicione item na tabela", "Aviso", JOptionPane.WARNING_MESSAGE );
        }

    }

    private static boolean verifica_ano_documento_igual_economico()
    {
        int ano_economico = Integer.parseInt( anoEconomicoDao.findAnoEconomico( getIdAnoEconomico() ).getDesignacao() );
        int ano_documento = dc_data_documento.getDate().getYear() + 1900;
        return ano_documento == ano_economico;

    }

    public static boolean salvar_venda( double valor_entregue )
    {

        Date data_documento = dc_data_documento.getDate();
        TbCliente hospedeSeleccionado = getHospedeSeleccionado();
//        TbVenda venda_local = new TbVenda();
//        Integer codVenda = vendas.getCodigo();
//        Integer cod_venda = vendaController.findById(pk_documento, pk_ano_economico);
//        TbVenda venda_local = ( TbVenda ) vendaController.getVendasByClientes( getCodCliente() );
//        TbVenda venda_local = getHospedeSeleccionado();
        Date data_actual = new Date();
//        Double totalAmortizado = AmortizacaoDividaDao.findTotalAmortizadoByRefDoc( ( String ) faturaJSpinner.getValue() );
        Double valorEntregue = valor_entregue;
//        Double totalAPagar = valor_entregue - totalAmortizado;
//        Double troco = ( valorEntregue - totalAPagar ) > 0 ? valorEntregue - totalAPagar : 0;
        Double troco = 0d;

//        String ref_doc = ( String ) faturaJSpinner.getValue();
//        TbVenda factura = VendaDao.findByCodFact( ref_doc );
        TbVenda recibo = new TbVenda();

        recibo.setDataVenda( data_actual );
        recibo.setHora( data_actual );

        recibo.setNomeCliente( getNomeCliente() );
        recibo.setClienteNif( hospedeSeleccionado.getNif() );

        //Total Ilíquido
        recibo.setTotalGeral( new BigDecimal( CfMethods.parseMoedaFormatada( totalAPagarJTextField.getText() ) ) );
        //desconto por linha
        recibo.setDescontoComercial( new BigDecimal( CfMethods.parseMoedaFormatada( TxtTotalDesconto.getText() ) ) );
        //imposto
        //calculaTotalIVA();
        recibo.setTotalIva( new BigDecimal( 0 ) );
        //desconto global
        recibo.setDescontoFinanceiro( new BigDecimal( CfMethods.parseMoedaFormatada( TxtTotalDesconto.getText() ) ) );
        //Total(AOA) <=> Total Líquido
        recibo.setTotalVenda( new BigDecimal( CfMethods.parseMoedaFormatada( totalAPagarJTextField.getText() ) ) );

        //#MONTANTE
        System.err.println( "valorEntregue: " + valorEntregue );
        System.err.println( "troco: " + troco );
        recibo.setValorEntregue( new BigDecimal( valorEntregue - troco ) );
        recibo.setTroco( new BigDecimal( troco ) );

        recibo.setTotalIncidencia( new BigDecimal( 0 ) );
        recibo.setTotalIncidenciaIsento( new BigDecimal( 0 ) );
        recibo.setRefDataFact( recibo.getDataVenda() );
        recibo.setDataVencimento( data_documento );
//cmbArmazem
        /*outros campos*/
        recibo.setDescontoTotal( new BigDecimal( CfMethods.parseMoedaFormatada( TxtTotalDesconto.getText() ) ) );
        recibo.setIdArmazemFK( new TbArmazem( 2 ) );
        recibo.setCodigoUsuario( usuarioDao.findTbUsuario( idUser ) );
        recibo.setCodigoCliente( clienteDao.findTbCliente( getIdCliente() ) );
        recibo.setFkAnoEconomico( anoEconomico );

        recibo.setFkDocumento( getDocumento() );
        recibo.setCodFact( prox_doc );
        recibo.setRefCodFact( "" );
//#HASH_TESTE        

        recibo.setHashCod( MetodosUtil.criptografia_hash( recibo, recibo.getTotalGeral().doubleValue(), conexao ) );
        recibo.setTotalPorExtenso( lbValorPorExtenco.getText() );

        recibo.setAssinatura( MetodosUtil.assinatura_doc( recibo.getHashCod() ) );
//        recibo.setAreaVenda( String.valueOf( cmb_area_venda_restaurante.getSelectedItem() ) );
//        recibo.setQuarto( "" );
        recibo.setFkCambio( new Cambio( 1 ) );

        /*status documento*/
        recibo.setStatusEliminado( "false" );
        recibo.setPerformance( "false" );
        recibo.setCredito( "false" );

        try
        {
            Integer last_venda = VendaDao.criarVendaComProcedu( recibo, conexao );
            System.out.println( "STATUS:factura criada com sucesso." );

            if ( last_venda != null )
            {
//                if ( getIdDocumento() == DVML.DOC_FACTURA_FT )
//                {
//                    ExtratoContaClienteController.registro_movimento_conta_cliente( vendasController.getLastVenda(), conexao );
//                }
                salvarAmortizacaoDivida( last_venda );

            }
            System.out.println( "STATUS:itens adicionado na facrtura com sucesso." );
        }
        catch ( Exception e )
        {
            System.err.println( "STATUS: falha ao actualizar a factura" );
            e.printStackTrace();
            JOptionPane.showMessageDialog( null, "Falha ao Processar a Factura", "FALHA", JOptionPane.ERROR_MESSAGE );

            return false;
        }

        return true;

    }

    public static void salvarAmortizacaoDivida( Integer last_cod_recibo )
    {
        DefaultTableModel modelo = ( DefaultTableModel ) tabela_linhas.getModel();
        Date data_actual = new Date();
        boolean efectuada = true;
        Date data_emissao = null;
        double valor_pagar = 0;
        TbVenda recibo = vendaDao.findTbVenda( last_cod_recibo );
        AmortizacaoDivida amortizacaoDivida;
        for ( int i = 0; i < tabela_linhas.getRowCount(); i++ )
        {
            boolean selecionado = ( boolean ) modelo.getValueAt( i, 0 );
            String codFact = modelo.getValueAt( i, 2 ).toString();
            String vslorPagarLocal = modelo.getValueAt( i, 7 ).toString();
            TbVenda factura = vendasController.getVendaByCodFact( codFact );
            BigDecimal tax = new BigDecimal( MetodosUtil.getTaxaIva( getRegime() ) );
            BigDecimal netNotal = new BigDecimal( 0 );
            data_emissao = MetodosUtil.stringToDate( modelo.getValueAt( i, 5 ).toString(), "dd-MM-yyyy" );
            try
            {
                if ( selecionado && Double.parseDouble( vslorPagarLocal ) > 0.0 )
                {

                    double totalVendaFact = Double.parseDouble( String.valueOf( tabela_linhas.getModel().getValueAt( i, 6 ) ) );
                    double valorPago = Double.parseDouble( String.valueOf( tabela_linhas.getModel().getValueAt( i, 7 ) ) );
                    double valorEntregue = Double.parseDouble( String.valueOf( tabela_linhas.getModel().getValueAt( i, 8 ) ) );
                    double desconto = Double.parseDouble( String.valueOf( tabela_linhas.getModel().getValueAt( i, 9 ) ) );
                    double valorPendente = Double.parseDouble( String.valueOf( tabela_linhas.getModel().getValueAt( i, 11 ) ) );

                    double valorPercentual = ( ( valorPago * tax.doubleValue() ) / 100 );
                    netNotal = new BigDecimal( valorPago - valorPercentual );

                    amortizacaoDivida = new AmortizacaoDivida();

                    amortizacaoDivida.setFkVenda( factura );
                    amortizacaoDivida.setRefCodFact( recibo.getCodFact() );
                    amortizacaoDivida.setData( data_actual );
                    amortizacaoDivida.setTotalVendaFact( totalVendaFact );
                    amortizacaoDivida.setValorEntregue( valorEntregue );
                    amortizacaoDivida.setDesconto( desconto );
                    amortizacaoDivida.setValorPendente( valorPendente );
                    amortizacaoDivida.setValorPago( new BigDecimal( valorPago ) );
                    amortizacaoDivida.setFkUsuario( new TbUsuario( idUser ) );
                    amortizacaoDivida.setNetTotal( netNotal );
                    amortizacaoDivida.setTax( tax );
                    //cria amortizacaoDivida
                    amortizacaoDividaDao.create( amortizacaoDivida );

                }

            }
            catch ( Exception e )
            {
                e.printStackTrace();
                efectuada = false;
                break;
            }
//            modelo.setValueAt( false, linha_actual, 0 );
//            modelo.setValueAt( selecionado, linha_actual, 0 );
        }

        if ( efectuada )
        {
            List<TbProduto> lista_produto_isentos = new ArrayList<>();
//            lista_produto_isentos = getProdutosIsentos();
            String motivos_isentos = MetodosUtil.getMotivoIsensaoProdutos( lista_produto_isentos );
            System.err.println( "MOTIVOS: " + motivos_isentos );
            try
            {
                registrar_forma_pagamento( last_cod_recibo );

//                String ref_doc = ( String ) faturaJSpinner.getValue();
//                TbVenda factura = VendaDao.findByCodFact( ref_doc );
//                factura.setStatusRecibo( true );
//                factura.setStatusRecibo( new Boolean( "1" ) );
//                vendaDao.edit( factura );
            }
            catch ( Exception e )
            {
            }
            actualizar_cod_doc();
            //Chama a factura e imprime directamente para a imprissora que estiver definidad no sistema

            ListaVenda1 listaVenda = new ListaVenda1( last_cod_recibo, DVML.Abreviacao.RC, false, false, "Original", motivos_isentos );
            //ListaVenda1 listaVenda2 = new ListaVenda1(cod_venda, false, ck_simplificada.isSelected());

            //Em caso em que a impreensão é dupla
            //ListaVendaDuplicado listaVenda1 = new ListaVendaDuplicado(cod_venda, setPeformance(), ckImpreesao.isSelected());
        }

        modelo.setValueAt( valor_pagar, linha_actual, 7 );
    }

    private static void actualizar_cod_doc()
    {
        Documento documento = getDocumento();
        documento.setCodUltimoDoc( getDocProxCod() );
        documento.setDescricaoUltimoDoc( getProximoDoc() );
        documento.setUltimaData( new Date() );
        try
        {
            new DocumentoDao().edit( documento );
        }
        catch ( Exception e )
        {
            System.err.println( "Falha ao actualizar o documento" );
        }
    }

    public static int getIdMoeda()
    {
        try
        {
            return 1;
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            return 0;
        }

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
////            String valor = ( modelo.getValueAt( i, 3 ) != null ) ? modelo.getValueAt( i, 3 ).toString() : "0";
//            String valor = ( !modelo.getValueAt( i, 3 ).toString().equals( "" ) ) ? modelo.getValueAt( i, 3 ).toString() : "0";
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
    private void finalizar_recibo()
    {
        if ( JOptionPane.showConfirmDialog( null, "Caro usuario este processo é irreversivel, deseja continuar?" ) == JOptionPane.YES_OPTION )
        {
            new FormaPagamentoVisao( this, rootPaneCheckingEnabled, null, DVML.EMISSAO_RECIBOS, conexao ).setVisible( true );
//            new FormaPagamentoVisao( this, rootPaneCheckingEnabled, DVML.EMISSAO_RECIBOS, emf ).setVisible( true );
        }
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

    private static Documento getDocumento()
    {
        return new DocumentoDao().findDocumento( DOC_RECIBO_RC );
    }

    private void mostrar_proximo_codigo_documento1()
    {
        try
        {
            Documento documento = getDocumento();
            int doc_prox_cod = documento.getCodUltimoDoc() + 1;
            //prox_doc = " " + documento.getAbreviacao();
            String prox_doc = documento.getAbreviacao();
            //FA Série / codigo
            prox_doc += " " + getAnoEconomicoSerie().getSerie() + "/" + doc_prox_cod;

//            tituloJLabel.setText( prox_doc );
            lb_proximo_documento.setText( "PRÓXIMO RECIBO. :" + prox_doc );
//            proximoDocJLabel.setText( "PRÓXIMO RECIBO. :" + prox_doc );
        }
        catch ( Exception e )
        {
//            proximoDocJLabel.setText( "" );
        }
    }

    private static String iniciais_extenso()
    {
        Documento documento_local = documentoDao.findDocumento( getIdDocumento() );
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

    private void actualizar_abreviacao()
    {

        switch ( getIdDocumento() )
        {
            case DVML.DOC_RECIBO_RC:
                this.abreviacao = DVML.Abreviacao.RC;
                break;

            default:
                break;
        }

    }

//    private void mostrar_ano_economico_serie ()
//    {
//        this.anoEconomico = anoEconomicoDao.getLastObject ();
//        lb_ano_academico.setText ( "ANO ECONÔMICO: " + this.anoEconomico.getSerie () );
//
//    }
    private static AnoEconomico getAnoEconomicoSerie()
    {

        //return new AnoEconomicoDao().getLastObject();
        return getAnoEconomicoActual();
    }

    private static AnoEconomico getAnoEconomicoActual()
    {

        List<AnoEconomico> list_ano_economico = anoEconomicoDao.buscaTodosObject();
        return list_ano_economico.get( 0 );

    }

    private static String getProximoDoc()
    {
        try
        {
            Documento documento = getDocumento();
            int doc_prox_cod = documento.getCodUltimoDoc() + 1;
            //prox_doc = " " + documento.getAbreviacao();
            String prox_doc = documento.getAbreviacao();
            //FA Série / codigo
            prox_doc += " " + getAnoEconomicoSerie().getSerie() + "/" + doc_prox_cod;

            return prox_doc;

        }
        catch ( Exception e )
        {
            return null;
        }
    }

    public static int getCodigoArmazem()
    {
        //return conexao.getCodigoPublico("tb_armazem", String.valueOf(  cmbArmazem.getSelectedItem() ) );   
        return armazemDao.getArmazemByDescricao( cmbArmazem.getSelectedItem().toString() ).getCodigo();
    }

    private static int getDocProxCod()
    {
        try
        {
            Documento documento = getDocumento();
            int doc_prox_cod = documento.getCodUltimoDoc() + 1;
            //prox_doc = " " + documento.getAbreviacao();
            String prox_doc = documento.getAbreviacao();
            //FA Série / codigo
            prox_doc += " " + getAnoEconomicoSerie().getSerie() + "/" + doc_prox_cod;

            return doc_prox_cod;

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
            return documentoDao.getIdByDescricao( cmbTipoDocumento.getSelectedItem().toString() );
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
            return anoEconomicoDao.getIdByDescricao( cmbAnoEconomico.getSelectedItem().toString() );
        }
        catch ( Exception e )
        {
            return 0;
        }
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

    /**
     * @author: Engº. Domingos Dala Vunge
     * @nomefunção: data_documento_superior_data_actual
     * @data: 2020/02/14 04:45:43
     * @descrição: Verifica se a data do documento é superior ao actual, retorna
     * verdadeiro se é inferior caso contrário falso
     */
    private static void data_documento_superior_data_actual()
    {

        //retirando a data do documento
        Date data_documento = dc_data_documento.getDate();
        //pegando a data actual do sistema 
        Date data_sistema = new Date();
        //comparar as datas
        if ( MetodosUtil.maior_data_1_data_2( data_documento, data_sistema ) )
        {
            JOptionPane.showMessageDialog( null, "Após essa emissão, não poderá ser emitido um novo documento\n "
                    + "com a data actual ou anterior, dentro da mesma série.",
                    "AVISO", JOptionPane.WARNING_MESSAGE );

            aviso_continuar_documento = JOptionPane.showConfirmDialog( null, "Ainda assim deseja continuar com a operação ?" )
                    == JOptionPane.YES_OPTION;

        }
        else
        {
            aviso_continuar_documento = true;
        }

    }

    private static String getValor_por_extenco( Double valor )
    {
        System.out.println( "Valor XXXXXXX: " + valor );
//        lbValorPorExtenco.setText ( MetodosUtil.valorPorExtenso ( totalAmortizado, "Kwanza" ) );

        return MetodosUtil.valorPorExtenso( valor, "Kwanza" );
    }

    private static void valor_por_extenco()
    {
        System.out.println( "Valor XXXXXXX: " + CfMethods.parseMoedaFormatada( totalAPagarJTextField.getText() ) );
        lbValorPorExtenco.setText( MetodosUtil.valorPorExtenso( CfMethods.parseMoedaFormatada( totalAPagarJTextField.getText() ), "Kwanzas" ) );
    }

    public static void remover_item_carrinho()
    {

        DefaultTableModel modelo = ( DefaultTableModel ) tabela_linhas.getModel();
        modelo.removeRow( tabela_linhas.getSelectedRow() );
//        actualizar_total();

    }

    private static String getRegime()
    {
        DadosInstituicaoController d = new DadosInstituicaoController( conexao );
        return d.findByCodigo( 1 ).getRegime();
    }
}
