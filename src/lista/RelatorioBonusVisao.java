/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lista;

import dao.*;
import enties.util.RelatorioClienteBonusVipUtil;
import entity.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import kitanda.util.CfMethods;
import static kitanda.util.CfMethodsSwing.*;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;
import util.BDConexao;
import util.DVML;
import util.JPAEntityMannagerFactoryUtil;
import util.MetodosUtil;

/**
 *
 * @author Domingos Dala Vunge
 */
public class RelatorioBonusVisao extends javax.swing.JFrame
{

    /**
     * Creates new form ListaUsuarioVisao
     */
    private EntityManagerFactory emf = JPAEntityMannagerFactoryUtil.em;
    private VendaDao vendaDao = new VendaDao ( emf );
    private UsuarioDao usuarioDao = new UsuarioDao ( emf );
    private ProdutoDao produtoDao = new ProdutoDao ( emf );
    private ItemVendaDao itemVendaDao = new ItemVendaDao ( emf );
    private PrecoDao precoDao = new PrecoDao ( emf );
    private ClienteDao clienteDao = new ClienteDao ( emf );
    private ArmazemDao armazemDao = new ArmazemDao ( emf );
    private BDConexao conexao;
//    private List<TbVenda> lista = null;
    private double total_geral = 0;
    private List<RelatorioClienteBonusVipUtil> lista = new ArrayList<RelatorioClienteBonusVipUtil> ();
    private int CLIENTES_TAB;
    private int EMPRESAS_TAB;
    private Double totalProcessadoBonusCliente;
    private Double totalBonusCliente;
    private TbUsuario tbUsuario;
    private Double totalProcessadoBonusEmpresa;
    private Double totalBonusEmpresa;

    public RelatorioBonusVisao ()
    {
        this ( 15 );
    }

    public RelatorioBonusVisao ( Integer fkUsuario )
    {

        tbUsuario = new UsuarioDao ().findTbUsuario ( fkUsuario );

        initComponents ();
//        setResizable(false);
        setLocationRelativeTo ( null );
        this.conexao = BDConexao.getInstancia();

        inicializar ();

    }

    @SuppressWarnings ( "unchecked" )
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {
        java.awt.GridBagConstraints gridBagConstraints;

        docButtonGroup = new javax.swing.ButtonGroup();
        detalhesBonusClienteJDialog = new javax.swing.JDialog();
        jPanel4 = new javax.swing.JPanel();
        norteJPanel1 = new javax.swing.JPanel();
        tituloJLabel1 = new javax.swing.JLabel();
        detalhesCentroJPanel = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        bonusClientesJTable = new javax.swing.JTable();
        sulJPanel1 = new javax.swing.JPanel();
        fecharDetalhesBonusJButton = new javax.swing.JButton();
        docButtonGroup1 = new javax.swing.ButtonGroup();
        detalhesBonusEmpresaJDialog = new javax.swing.JDialog();
        jPanel8 = new javax.swing.JPanel();
        norteJPanel2 = new javax.swing.JPanel();
        tituloJLabel2 = new javax.swing.JLabel();
        detalhesCentroJPanel1 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        bonusEmpresaJTable = new javax.swing.JTable();
        sulJPanel3 = new javax.swing.JPanel();
        fecharDetalhesBonusJButton1 = new javax.swing.JButton();
        principalJPanle = new javax.swing.JPanel();
        norteJPanel = new javax.swing.JPanel();
        tituloJLabel = new javax.swing.JLabel();
        centroJPanel = new javax.swing.JPanel();
        opcoesJTabbedPane = new javax.swing.JTabbedPane();
        jPanel5 = new javax.swing.JPanel();
        bodyJPanel = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        armazemJCombobox = new javax.swing.JComboBox<>();
        jLabel10 = new javax.swing.JLabel();
        clientesJCombobox = new javax.swing.JComboBox<>();
        lbData15 = new javax.swing.JLabel();
        percentagemJSpinner = new javax.swing.JSpinner();
        lbData16 = new javax.swing.JLabel();
        lbData17 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        rb_FR4 = new javax.swing.JRadioButton();
        rb_FT4 = new javax.swing.JRadioButton();
        rb_PP4 = new javax.swing.JRadioButton();
        rb_RC4 = new javax.swing.JRadioButton();
        jPanel1 = new javax.swing.JPanel();
        lbData13 = new javax.swing.JLabel();
        dataInicioJDateChooser = new com.toedter.calendar.JDateChooser();
        lbData14 = new javax.swing.JLabel();
        dataFimJDateChooser = new com.toedter.calendar.JDateChooser();
        jPanel2 = new javax.swing.JPanel();
        footerJPanel = new javax.swing.JPanel();
        lbData18 = new javax.swing.JLabel();
        totalProcessadoJTextField = new javax.swing.JTextField();
        lbData19 = new javax.swing.JLabel();
        totalBonusJTextField = new javax.swing.JTextField();
        sulJPanel2 = new javax.swing.JPanel();
        imprimirJButton = new javax.swing.JButton();
        detalhesJButton = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        bodyJPanel2 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        armazemJCombobox2 = new javax.swing.JComboBox<>();
        jLabel14 = new javax.swing.JLabel();
        fornecedorJCombobox = new javax.swing.JComboBox<>();
        lbData24 = new javax.swing.JLabel();
        percentagemJSpinner2 = new javax.swing.JSpinner();
        lbData27 = new javax.swing.JLabel();
        lbData28 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        rb_CO = new javax.swing.JRadioButton();
        jPanel10 = new javax.swing.JPanel();
        lbData29 = new javax.swing.JLabel();
        dataInicioJDateChooser2 = new com.toedter.calendar.JDateChooser();
        lbData30 = new javax.swing.JLabel();
        dataFimJDateChooser2 = new com.toedter.calendar.JDateChooser();
        jPanel11 = new javax.swing.JPanel();
        footerJPanel2 = new javax.swing.JPanel();
        lbData31 = new javax.swing.JLabel();
        totalProcessadoJTextField2 = new javax.swing.JTextField();
        lbData32 = new javax.swing.JLabel();
        totalBonusJTextField2 = new javax.swing.JTextField();
        sulJPanel4 = new javax.swing.JPanel();
        imprimirJButton2 = new javax.swing.JButton();
        detalhesJButton2 = new javax.swing.JButton();
        sulJPanel = new javax.swing.JPanel();
        fecharJButton = new javax.swing.JButton();

        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel4.setLayout(new java.awt.BorderLayout());

        norteJPanel1.setLayout(new java.awt.GridBagLayout());

        tituloJLabel1.setFont(new java.awt.Font("SansSerif", 1, 24)); // NOI18N
        tituloJLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        tituloJLabel1.setText("DETALHES DO RELATÓRIO DE BONUS");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.01;
        gridBagConstraints.weighty = 0.01;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        norteJPanel1.add(tituloJLabel1, gridBagConstraints);

        jPanel4.add(norteJPanel1, java.awt.BorderLayout.NORTH);

        detalhesCentroJPanel.setLayout(new java.awt.GridBagLayout());

        bonusClientesJTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String []
            {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(bonusClientesJTable);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.01;
        gridBagConstraints.weighty = 0.01;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        detalhesCentroJPanel.add(jScrollPane2, gridBagConstraints);

        jPanel4.add(detalhesCentroJPanel, java.awt.BorderLayout.CENTER);

        fecharDetalhesBonusJButton.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        fecharDetalhesBonusJButton.setText("Fechar");
        fecharDetalhesBonusJButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                fecharDetalhesBonusJButtonActionPerformed(evt);
            }
        });
        sulJPanel1.add(fecharDetalhesBonusJButton);

        jPanel4.add(sulJPanel1, java.awt.BorderLayout.SOUTH);

        detalhesBonusClienteJDialog.getContentPane().add(jPanel4, java.awt.BorderLayout.CENTER);

        jPanel8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel8.setLayout(new java.awt.BorderLayout());

        norteJPanel2.setLayout(new java.awt.GridBagLayout());

        tituloJLabel2.setFont(new java.awt.Font("SansSerif", 1, 24)); // NOI18N
        tituloJLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        tituloJLabel2.setText("DETALHES DO RELATÓRIO DE BONUS");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.01;
        gridBagConstraints.weighty = 0.01;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        norteJPanel2.add(tituloJLabel2, gridBagConstraints);

        jPanel8.add(norteJPanel2, java.awt.BorderLayout.NORTH);

        detalhesCentroJPanel1.setLayout(new java.awt.GridBagLayout());

        bonusEmpresaJTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String []
            {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane3.setViewportView(bonusEmpresaJTable);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.01;
        gridBagConstraints.weighty = 0.01;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        detalhesCentroJPanel1.add(jScrollPane3, gridBagConstraints);

        jPanel8.add(detalhesCentroJPanel1, java.awt.BorderLayout.CENTER);

        fecharDetalhesBonusJButton1.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        fecharDetalhesBonusJButton1.setText("Fechar");
        fecharDetalhesBonusJButton1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                fecharDetalhesBonusJButton1ActionPerformed(evt);
            }
        });
        sulJPanel3.add(fecharDetalhesBonusJButton1);

        jPanel8.add(sulJPanel3, java.awt.BorderLayout.SOUTH);

        detalhesBonusEmpresaJDialog.getContentPane().add(jPanel8, java.awt.BorderLayout.CENTER);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("...:::: DVML - RELATORIO DE BÔNUS PARA CLIENTES VIP´S::...");

        principalJPanle.setLayout(new java.awt.BorderLayout());

        norteJPanel.setLayout(new java.awt.GridBagLayout());

        tituloJLabel.setFont(new java.awt.Font("SansSerif", 1, 24)); // NOI18N
        tituloJLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        tituloJLabel.setText("RELATÓRIO DE BONUS");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.01;
        gridBagConstraints.weighty = 0.01;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        norteJPanel.add(tituloJLabel, gridBagConstraints);

        principalJPanle.add(norteJPanel, java.awt.BorderLayout.NORTH);

        centroJPanel.setLayout(new java.awt.BorderLayout());

        opcoesJTabbedPane.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N

        jPanel5.setLayout(new java.awt.BorderLayout());

        bodyJPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "PARÂMETROS DA CONSULTA", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP, new java.awt.Font("SansSerif", 1, 14))); // NOI18N
        bodyJPanel.setLayout(new java.awt.GridBagLayout());

        jLabel11.setFont(new java.awt.Font("SansSerif", 1, 16)); // NOI18N
        jLabel11.setText("Armazém :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(15, 0, 5, 0);
        bodyJPanel.add(jLabel11, gridBagConstraints);

        armazemJCombobox.setFont(new java.awt.Font("SansSerif", 0, 16)); // NOI18N
        armazemJCombobox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(15, 5, 5, 0);
        bodyJPanel.add(armazemJCombobox, gridBagConstraints);

        jLabel10.setFont(new java.awt.Font("SansSerif", 1, 16)); // NOI18N
        jLabel10.setText("Cliente :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        bodyJPanel.add(jLabel10, gridBagConstraints);

        clientesJCombobox.setFont(new java.awt.Font("SansSerif", 0, 16)); // NOI18N
        clientesJCombobox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 0);
        bodyJPanel.add(clientesJCombobox, gridBagConstraints);

        lbData15.setFont(new java.awt.Font("SansSerif", 1, 16)); // NOI18N
        lbData15.setText("Taxa ( 0% - 100%) :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        bodyJPanel.add(lbData15, gridBagConstraints);

        percentagemJSpinner.setFont(new java.awt.Font("SansSerif", 0, 16)); // NOI18N
        percentagemJSpinner.setModel(new javax.swing.SpinnerNumberModel(0.0d, 0.0d, 100.0d, 1.0d));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 0);
        bodyJPanel.add(percentagemJSpinner, gridBagConstraints);

        lbData16.setFont(new java.awt.Font("SansSerif", 1, 16)); // NOI18N
        lbData16.setText("Período :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        bodyJPanel.add(lbData16, gridBagConstraints);

        lbData17.setFont(new java.awt.Font("SansSerif", 1, 16)); // NOI18N
        lbData17.setText("Documento :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        bodyJPanel.add(lbData17, gridBagConstraints);

        jPanel3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        docButtonGroup.add(rb_FR4);
        rb_FR4.setFont(new java.awt.Font("SansSerif", 0, 16)); // NOI18N
        rb_FR4.setSelected(true);
        rb_FR4.setText("FR");
        jPanel3.add(rb_FR4);

        docButtonGroup.add(rb_FT4);
        rb_FT4.setFont(new java.awt.Font("SansSerif", 0, 16)); // NOI18N
        rb_FT4.setText("FT");
        jPanel3.add(rb_FT4);

        docButtonGroup.add(rb_PP4);
        rb_PP4.setFont(new java.awt.Font("SansSerif", 0, 16)); // NOI18N
        rb_PP4.setText("PP");
        jPanel3.add(rb_PP4);

        docButtonGroup.add(rb_RC4);
        rb_RC4.setFont(new java.awt.Font("SansSerif", 0, 16)); // NOI18N
        rb_RC4.setText("RC");
        jPanel3.add(rb_RC4);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        bodyJPanel.add(jPanel3, gridBagConstraints);

        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        lbData13.setFont(new java.awt.Font("SansSerif", 1, 16)); // NOI18N
        lbData13.setText("De");
        jPanel1.add(lbData13);

        dataInicioJDateChooser.setDateFormatString("dd/MMM/yyyy");
        dataInicioJDateChooser.setFont(new java.awt.Font("SansSerif", 0, 16)); // NOI18N
        dataInicioJDateChooser.setPreferredSize(new java.awt.Dimension(150, 20));
        jPanel1.add(dataInicioJDateChooser);

        lbData14.setFont(new java.awt.Font("SansSerif", 1, 16)); // NOI18N
        lbData14.setText("à");
        jPanel1.add(lbData14);

        dataFimJDateChooser.setDateFormatString("dd/MMM/yyyy");
        dataFimJDateChooser.setFont(new java.awt.Font("SansSerif", 0, 16)); // NOI18N
        dataFimJDateChooser.setPreferredSize(new java.awt.Dimension(150, 20));
        jPanel1.add(dataFimJDateChooser);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        bodyJPanel.add(jPanel1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        bodyJPanel.add(jPanel2, gridBagConstraints);

        jPanel5.add(bodyJPanel, java.awt.BorderLayout.CENTER);

        footerJPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "RESUMO", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP, new java.awt.Font("SansSerif", 1, 14))); // NOI18N
        footerJPanel.setLayout(new java.awt.GridBagLayout());

        lbData18.setFont(new java.awt.Font("SansSerif", 1, 16)); // NOI18N
        lbData18.setText("Total processado :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 5);
        footerJPanel.add(lbData18, gridBagConstraints);

        totalProcessadoJTextField.setColumns(25);
        totalProcessadoJTextField.setFont(new java.awt.Font("SansSerif", 0, 16)); // NOI18N
        totalProcessadoJTextField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 30);
        footerJPanel.add(totalProcessadoJTextField, gridBagConstraints);

        lbData19.setFont(new java.awt.Font("SansSerif", 1, 16)); // NOI18N
        lbData19.setText("Total de Bonus :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 5);
        footerJPanel.add(lbData19, gridBagConstraints);

        totalBonusJTextField.setColumns(25);
        totalBonusJTextField.setFont(new java.awt.Font("SansSerif", 0, 16)); // NOI18N
        totalBonusJTextField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 30);
        footerJPanel.add(totalBonusJTextField, gridBagConstraints);

        imprimirJButton.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        imprimirJButton.setText("Imprimir");
        imprimirJButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                imprimirJButtonActionPerformed(evt);
            }
        });
        sulJPanel2.add(imprimirJButton);

        detalhesJButton.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        detalhesJButton.setText("Detalhes");
        detalhesJButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                detalhesJButtonActionPerformed(evt);
            }
        });
        sulJPanel2.add(detalhesJButton);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        footerJPanel.add(sulJPanel2, gridBagConstraints);

        jPanel5.add(footerJPanel, java.awt.BorderLayout.SOUTH);

        opcoesJTabbedPane.addTab("tab1", jPanel5);

        jPanel6.setLayout(new java.awt.BorderLayout());

        bodyJPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "PARÂMETROS DA CONSULTA", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP, new java.awt.Font("SansSerif", 1, 14))); // NOI18N
        bodyJPanel2.setLayout(new java.awt.GridBagLayout());

        jLabel13.setFont(new java.awt.Font("SansSerif", 1, 16)); // NOI18N
        jLabel13.setText("Armazém :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(15, 0, 5, 0);
        bodyJPanel2.add(jLabel13, gridBagConstraints);

        armazemJCombobox2.setFont(new java.awt.Font("SansSerif", 0, 16)); // NOI18N
        armazemJCombobox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(15, 5, 5, 0);
        bodyJPanel2.add(armazemJCombobox2, gridBagConstraints);

        jLabel14.setFont(new java.awt.Font("SansSerif", 1, 16)); // NOI18N
        jLabel14.setText("Fornecedor :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        bodyJPanel2.add(jLabel14, gridBagConstraints);

        fornecedorJCombobox.setFont(new java.awt.Font("SansSerif", 0, 16)); // NOI18N
        fornecedorJCombobox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 0);
        bodyJPanel2.add(fornecedorJCombobox, gridBagConstraints);

        lbData24.setFont(new java.awt.Font("SansSerif", 1, 16)); // NOI18N
        lbData24.setText("Taxa ( 0% - 100%) :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        bodyJPanel2.add(lbData24, gridBagConstraints);

        percentagemJSpinner2.setFont(new java.awt.Font("SansSerif", 0, 16)); // NOI18N
        percentagemJSpinner2.setModel(new javax.swing.SpinnerNumberModel(0.0d, 0.0d, 100.0d, 1.0d));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 0);
        bodyJPanel2.add(percentagemJSpinner2, gridBagConstraints);

        lbData27.setFont(new java.awt.Font("SansSerif", 1, 16)); // NOI18N
        lbData27.setText("Período :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        bodyJPanel2.add(lbData27, gridBagConstraints);

        lbData28.setFont(new java.awt.Font("SansSerif", 1, 16)); // NOI18N
        lbData28.setText("Documento :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        bodyJPanel2.add(lbData28, gridBagConstraints);

        jPanel7.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        docButtonGroup1.add(rb_CO);
        rb_CO.setFont(new java.awt.Font("SansSerif", 0, 16)); // NOI18N
        rb_CO.setSelected(true);
        rb_CO.setText("CO");
        jPanel7.add(rb_CO);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        bodyJPanel2.add(jPanel7, gridBagConstraints);

        jPanel10.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        lbData29.setFont(new java.awt.Font("SansSerif", 1, 16)); // NOI18N
        lbData29.setText("De");
        jPanel10.add(lbData29);

        dataInicioJDateChooser2.setDateFormatString("dd/MMM/yyyy");
        dataInicioJDateChooser2.setFont(new java.awt.Font("SansSerif", 0, 16)); // NOI18N
        dataInicioJDateChooser2.setPreferredSize(new java.awt.Dimension(150, 20));
        jPanel10.add(dataInicioJDateChooser2);

        lbData30.setFont(new java.awt.Font("SansSerif", 1, 16)); // NOI18N
        lbData30.setText("à");
        jPanel10.add(lbData30);

        dataFimJDateChooser2.setDateFormatString("dd/MMM/yyyy");
        dataFimJDateChooser2.setFont(new java.awt.Font("SansSerif", 0, 16)); // NOI18N
        dataFimJDateChooser2.setPreferredSize(new java.awt.Dimension(150, 20));
        jPanel10.add(dataFimJDateChooser2);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        bodyJPanel2.add(jPanel10, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        bodyJPanel2.add(jPanel11, gridBagConstraints);

        jPanel6.add(bodyJPanel2, java.awt.BorderLayout.CENTER);

        footerJPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "RESUMO", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP, new java.awt.Font("SansSerif", 1, 14))); // NOI18N
        footerJPanel2.setLayout(new java.awt.GridBagLayout());

        lbData31.setFont(new java.awt.Font("SansSerif", 1, 16)); // NOI18N
        lbData31.setText("Total processado :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 5);
        footerJPanel2.add(lbData31, gridBagConstraints);

        totalProcessadoJTextField2.setColumns(25);
        totalProcessadoJTextField2.setFont(new java.awt.Font("SansSerif", 0, 16)); // NOI18N
        totalProcessadoJTextField2.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 30);
        footerJPanel2.add(totalProcessadoJTextField2, gridBagConstraints);

        lbData32.setFont(new java.awt.Font("SansSerif", 1, 16)); // NOI18N
        lbData32.setText("Total de Bonus :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 5);
        footerJPanel2.add(lbData32, gridBagConstraints);

        totalBonusJTextField2.setColumns(25);
        totalBonusJTextField2.setFont(new java.awt.Font("SansSerif", 0, 16)); // NOI18N
        totalBonusJTextField2.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 30);
        footerJPanel2.add(totalBonusJTextField2, gridBagConstraints);

        imprimirJButton2.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        imprimirJButton2.setText("Imprimir");
        imprimirJButton2.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                imprimirJButton2ActionPerformed(evt);
            }
        });
        sulJPanel4.add(imprimirJButton2);

        detalhesJButton2.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        detalhesJButton2.setText("Detalhes");
        detalhesJButton2.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                detalhesJButton2ActionPerformed(evt);
            }
        });
        sulJPanel4.add(detalhesJButton2);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        footerJPanel2.add(sulJPanel4, gridBagConstraints);

        jPanel6.add(footerJPanel2, java.awt.BorderLayout.SOUTH);

        opcoesJTabbedPane.addTab("tab2", jPanel6);

        centroJPanel.add(opcoesJTabbedPane, java.awt.BorderLayout.CENTER);

        principalJPanle.add(centroJPanel, java.awt.BorderLayout.CENTER);

        fecharJButton.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        fecharJButton.setText("Fechar");
        fecharJButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                fecharJButtonActionPerformed(evt);
            }
        });
        sulJPanel.add(fecharJButton);

        principalJPanle.add(sulJPanel, java.awt.BorderLayout.SOUTH);

        getContentPane().add(principalJPanle, java.awt.BorderLayout.CENTER);

        setSize(new java.awt.Dimension(560, 635));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void fecharJButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_fecharJButtonActionPerformed
    {//GEN-HEADEREND:event_fecharJButtonActionPerformed
        fechar ();
    }//GEN-LAST:event_fecharJButtonActionPerformed

    private void fecharDetalhesBonusJButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_fecharDetalhesBonusJButtonActionPerformed
    {//GEN-HEADEREND:event_fecharDetalhesBonusJButtonActionPerformed
        fecharJanelaDetalhesBonusCliente ();
    }//GEN-LAST:event_fecharDetalhesBonusJButtonActionPerformed

    private void imprimirJButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_imprimirJButtonActionPerformed
    {//GEN-HEADEREND:event_imprimirJButtonActionPerformed
        imprimirRelatorioBonusCliente ();
    }//GEN-LAST:event_imprimirJButtonActionPerformed

    private void detalhesJButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_detalhesJButtonActionPerformed
    {//GEN-HEADEREND:event_detalhesJButtonActionPerformed
        exibirDetalhesBonusCliente ();
    }//GEN-LAST:event_detalhesJButtonActionPerformed

    private void imprimirJButton2ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_imprimirJButton2ActionPerformed
    {//GEN-HEADEREND:event_imprimirJButton2ActionPerformed
        imprimirRelatorioBonusEmpresa ();
    }//GEN-LAST:event_imprimirJButton2ActionPerformed

    private void detalhesJButton2ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_detalhesJButton2ActionPerformed
    {//GEN-HEADEREND:event_detalhesJButton2ActionPerformed
        exibirDetalhesBonusEmpresa ();
    }//GEN-LAST:event_detalhesJButton2ActionPerformed

    private void fecharDetalhesBonusJButton1ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_fecharDetalhesBonusJButton1ActionPerformed
    {//GEN-HEADEREND:event_fecharDetalhesBonusJButton1ActionPerformed
        fecharJanelaDetalhesBonusEmpresa ();
    }//GEN-LAST:event_fecharDetalhesBonusJButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main ( String args[] )
    {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try
        {
            for ( javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels () )
            {
                if ( "Windows".equals ( info.getName () ) )
                {
                    javax.swing.UIManager.setLookAndFeel ( info.getClassName () );
                    break;
                }
            }
        }
        catch ( ClassNotFoundException ex )
        {
            java.util.logging.Logger.getLogger ( RelatorioBonusVisao.class.getName () ).log ( java.util.logging.Level.SEVERE, null, ex );
        }
        catch ( InstantiationException ex )
        {
            java.util.logging.Logger.getLogger ( RelatorioBonusVisao.class.getName () ).log ( java.util.logging.Level.SEVERE, null, ex );
        }
        catch ( IllegalAccessException ex )
        {
            java.util.logging.Logger.getLogger ( RelatorioBonusVisao.class.getName () ).log ( java.util.logging.Level.SEVERE, null, ex );
        }
        catch ( javax.swing.UnsupportedLookAndFeelException ex )
        {
            java.util.logging.Logger.getLogger ( RelatorioBonusVisao.class.getName () ).log ( java.util.logging.Level.SEVERE, null, ex );
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater (new Runnable ()
        {
            public void run ()
            {
                try
                {
                    new RelatorioBonusVisao ().setVisible ( true );
                }
                catch ( Exception ex )
                {
                    Logger.getLogger ( RelatorioBonusVisao.class.getName () ).log ( Level.SEVERE, null, ex );
                }
            }
        } );
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> armazemJCombobox;
    private javax.swing.JComboBox<String> armazemJCombobox2;
    private javax.swing.JPanel bodyJPanel;
    private javax.swing.JPanel bodyJPanel2;
    private javax.swing.JTable bonusClientesJTable;
    private javax.swing.JTable bonusEmpresaJTable;
    private javax.swing.JPanel centroJPanel;
    private javax.swing.JComboBox<String> clientesJCombobox;
    private com.toedter.calendar.JDateChooser dataFimJDateChooser;
    private com.toedter.calendar.JDateChooser dataFimJDateChooser2;
    private com.toedter.calendar.JDateChooser dataInicioJDateChooser;
    private com.toedter.calendar.JDateChooser dataInicioJDateChooser2;
    private javax.swing.JDialog detalhesBonusClienteJDialog;
    private javax.swing.JDialog detalhesBonusEmpresaJDialog;
    private javax.swing.JPanel detalhesCentroJPanel;
    private javax.swing.JPanel detalhesCentroJPanel1;
    private javax.swing.JButton detalhesJButton;
    private javax.swing.JButton detalhesJButton2;
    private javax.swing.ButtonGroup docButtonGroup;
    private javax.swing.ButtonGroup docButtonGroup1;
    private javax.swing.JButton fecharDetalhesBonusJButton;
    private javax.swing.JButton fecharDetalhesBonusJButton1;
    private javax.swing.JButton fecharJButton;
    private javax.swing.JPanel footerJPanel;
    private javax.swing.JPanel footerJPanel2;
    private javax.swing.JComboBox<String> fornecedorJCombobox;
    private javax.swing.JButton imprimirJButton;
    private javax.swing.JButton imprimirJButton2;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lbData13;
    private javax.swing.JLabel lbData14;
    private javax.swing.JLabel lbData15;
    private javax.swing.JLabel lbData16;
    private javax.swing.JLabel lbData17;
    private javax.swing.JLabel lbData18;
    private javax.swing.JLabel lbData19;
    private javax.swing.JLabel lbData24;
    private javax.swing.JLabel lbData27;
    private javax.swing.JLabel lbData28;
    private javax.swing.JLabel lbData29;
    private javax.swing.JLabel lbData30;
    private javax.swing.JLabel lbData31;
    private javax.swing.JLabel lbData32;
    private javax.swing.JPanel norteJPanel;
    private javax.swing.JPanel norteJPanel1;
    private javax.swing.JPanel norteJPanel2;
    private javax.swing.JTabbedPane opcoesJTabbedPane;
    private javax.swing.JSpinner percentagemJSpinner;
    private javax.swing.JSpinner percentagemJSpinner2;
    private javax.swing.JPanel principalJPanle;
    private javax.swing.JRadioButton rb_CO;
    private javax.swing.JRadioButton rb_FR4;
    private javax.swing.JRadioButton rb_FT4;
    private javax.swing.JRadioButton rb_PP4;
    private javax.swing.JRadioButton rb_RC4;
    private javax.swing.JPanel sulJPanel;
    private javax.swing.JPanel sulJPanel1;
    private javax.swing.JPanel sulJPanel2;
    private javax.swing.JPanel sulJPanel3;
    private javax.swing.JPanel sulJPanel4;
    private javax.swing.JLabel tituloJLabel;
    private javax.swing.JLabel tituloJLabel1;
    private javax.swing.JLabel tituloJLabel2;
    private javax.swing.JTextField totalBonusJTextField;
    private javax.swing.JTextField totalBonusJTextField2;
    private javax.swing.JTextField totalProcessadoJTextField;
    private javax.swing.JTextField totalProcessadoJTextField2;
    // End of variables declaration//GEN-END:variables

    private void inicializar ()
    {
        totalProcessadoBonusEmpresa = 0.0;
        totalBonusEmpresa = 0.0;

        CLIENTES_TAB = 0;
        EMPRESAS_TAB = 1;
        resizeJLableIcon ( 50, 50, getClass ().getResource ( "/imagens/bonus/icons8_gift_filled_100px.png" ), tituloJLabel );
        resizeJButtonIcon ( 32, 32, getClass ().getResource ( "/imagens/2934_32x32.png" ), fecharJButton );

        opcoesJTabbedPane.setIconAt ( CLIENTES_TAB, resizeIcon ( 40, 50, getClass ().getResource ( "/imagens/bonus/icons8_client_company_200px.png" ) ) );
        opcoesJTabbedPane.setIconAt ( EMPRESAS_TAB, resizeIcon ( 40, 50, getClass ().getResource ( "/imagens/bonus/icons8_company_200px.png" ) ) );

        opcoesJTabbedPane.setTitleAt ( CLIENTES_TAB, "CLIENTES" );
        opcoesJTabbedPane.setTitleAt ( EMPRESAS_TAB, "EMPRESA" );

        inicializarBonusClientesTab ();
        inicializarBonusEmpresaTab ();
        atualizarTabelaBonusClientes ();
        atualizarTabelaBonusEmpresa ();
    }

    private void fechar ()
    {
        dispose ();
    }

    private ComboBoxModel<String> criarArmazemQueVenderamJComboboxModel ()
    {
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String> ();

        List<TbArmazem> armazems = ArmazemDao.buscarArmazensComVenda ();

        for ( TbArmazem armazem : armazems )
        {
            model.addElement ( armazem.getDesignacao () );
        }

        return model;
    }

    private ComboBoxModel<String> criarClientesQueCompraramNoArmazemJComboboxModel ( String desigancaoArmazem )
    {
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String> ();

        List<TbCliente> clientes = ClienteDao.buscarClientesQueCompraramNoArmazem ( desigancaoArmazem );

        for ( TbCliente cliente : clientes )
        {
            model.addElement ( cliente.getNome () );
        }

        return model;
    }

    private ComboBoxModel<String> criarFornecedoreQueForneceramAoArmazemJComboboxModel ( String desigancaoArmazem )
    {
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String> ();

        List<TbFornecedor> fornecedors = FornecedorDao.buscarFornecedoresQueForneceramAoArmazem ( desigancaoArmazem );

        for ( TbFornecedor fornecedor : fornecedors )
        {
            model.addElement ( fornecedor.getNome () );
        }

        return model;
    }

    private void ativarDesativarCamposBonusCliente ()
    {
        boolean registrosEncontrados = bonusClientesJTable.getRowCount () > 0;

        imprimirJButton.setVisible ( registrosEncontrados );
        detalhesJButton.setVisible ( registrosEncontrados );
    }

    private void ativarDesativarCamposBonusEmpresa ()
    {
        boolean registrosEncontrados = bonusEmpresaJTable.getRowCount () > 0;

        imprimirJButton2.setVisible ( registrosEncontrados );
        detalhesJButton2.setVisible ( registrosEncontrados );
    }

    private void atualizarTabelaBonusClientes ()
    {
        System.err.println ( "" );
        totalProcessadoBonusCliente = 0.0;
        totalBonusCliente = 0.0;
        int colIndex = 0;
        double percentBonus = ( ( double ) percentagemJSpinner.getValue () );
        final int fk_produto = colIndex ++;
        final int designacao_produto = colIndex ++;
        final int quantidade = colIndex ++;
        final int preco_fabrica = colIndex ++;
        final int fk_cliente = colIndex ++;
        final int nome_cliente = colIndex ++;
        final int data = colIndex ++;
        final int fk_armazem = colIndex ++;
        final int armazem = colIndex ++;
        final int fk_documento = colIndex ++;
        final int abreviacao_documento = colIndex ++;
        final int codigo_venda = colIndex ++;
        final int cod_fact = colIndex ++;
        final int sub_total = colIndex ++;
        final int bonus = colIndex ++;
        /*
             
         */

        String docoumentoSelecionado = ( Objects.isNull ( getSelectedBonusClienteDoc () ) ) ? "%%" : getSelectedBonusClienteDoc ();

        List<Object[]> relatorios = UtilDao.getExtratoBonusCliente ( ( String ) armazemJCombobox.getSelectedItem (), ( String ) clientesJCombobox.getSelectedItem (), dataInicioJDateChooser.getDate (), dataFimJDateChooser.getDate (), docoumentoSelecionado, percentBonus / 100 );
        DefaultTableModel defaultTableModel = new DefaultTableModel ( new Object[]
        {
            "Cód. Venda",
            "Ref. da Fact",
            "Produto ",
            "Preco de Fáb",
            "quantidade ",
            "Sub-Total (AOA)",
            "Bonus de [ " + percentBonus + " % ] (AOA)"
        }, 0 );

        if (  ! Objects.isNull ( relatorios ) )
        {
            for ( Object[] relatorio : relatorios )
            {
                Object[] rowData = new Object[]
                {
                    relatorio[ codigo_venda ],
                    relatorio[ cod_fact ],
                    relatorio[ designacao_produto ],
                    relatorio[ preco_fabrica ],
                    relatorio[ quantidade ],
                    CfMethods.formatarComoMoeda ( ( double ) relatorio[ sub_total ] ),
                    CfMethods.formatarComoMoeda ( ( double ) relatorio[ bonus ] )

                };
                totalProcessadoBonusCliente += ( Double ) relatorio[ sub_total ];
                totalBonusCliente += ( Double ) relatorio[ bonus ];
                defaultTableModel.addRow ( rowData );
            }
        }

        bonusClientesJTable.setModel ( defaultTableModel );
        bonusClientesJTable.getColumnModel ().getColumn ( 0 ).setMinWidth ( 0 );
        bonusClientesJTable.getColumnModel ().getColumn ( 0 ).setMaxWidth ( 100 );
        bonusClientesJTable.getColumnModel ().getColumn ( 1 ).setMinWidth ( 0 );
        bonusClientesJTable.getColumnModel ().getColumn ( 1 ).setMaxWidth ( 100 );
        bonusClientesJTable.getColumnModel ().getColumn ( 3 ).setMinWidth ( 0 );
        bonusClientesJTable.getColumnModel ().getColumn ( 3 ).setMaxWidth ( 100 );
        bonusClientesJTable.getColumnModel ().getColumn ( 4 ).setMinWidth ( 0 );
        bonusClientesJTable.getColumnModel ().getColumn ( 4 ).setMaxWidth ( 100 );
        bonusClientesJTable.getColumnModel ().getColumn ( 5 ).setMinWidth ( 100 );
        bonusClientesJTable.getColumnModel ().getColumn ( 5 ).setMaxWidth ( 250 );

        bonusClientesJTable.getColumnModel ().getColumn ( 6 ).setMinWidth ( 150 );
        bonusClientesJTable.getColumnModel ().getColumn ( 6 ).setMaxWidth ( 250 );

        atualizarResumoBonusCliente ();
        ativarDesativarCamposBonusCliente ();
    }

    private void atualizarTabelaBonusEmpresa ()
    {
        System.err.println ( "" );
        totalProcessadoBonusEmpresa = 0.0;
        totalBonusEmpresa = 0.0;
        int colIndex = 0;
        double percentBonus = ( ( double ) percentagemJSpinner2.getValue () );
        final int fk_produto = colIndex ++;
        final int designacao_produto = colIndex ++;
        final int quantidade = colIndex ++;
        final int preco_fabrica = colIndex ++;

        final int fk_fornecedor = colIndex ++;
        final int nome_fornecedor = colIndex ++;
        final int data = colIndex ++;
        final int fk_armazem = colIndex ++;

        final int armazem = colIndex ++;
        final int fk_documento = colIndex ++;
        final int abreviacao_documento = colIndex ++;
        final int fk_compra = colIndex ++;

        final int cod_fact = colIndex ++;
        final int sub_total = colIndex ++;
        final int bonus = colIndex ++;
        /*
             
         */

        String docoumentoSelecionado = ( Objects.isNull ( getSelectedBonusEmpresaDoc () ) ) ? "%%" : getSelectedBonusEmpresaDoc ();

        List<Object[]> relatorios = UtilDao.getExtratoBonusEmpresa ( ( String ) armazemJCombobox2.getSelectedItem (), ( String ) fornecedorJCombobox.getSelectedItem (), dataInicioJDateChooser2.getDate (), dataFimJDateChooser2.getDate (), docoumentoSelecionado, percentBonus / 100 );
        DefaultTableModel defaultTableModel = new DefaultTableModel ( new Object[]
        {
            "Cód. Compra",
            "Ref. da Fact",
            "Produto ",
            "Preco de Fáb",
            "quantidade ",
            "Sub-Total (AOA)",
            "Bonus de [ " + percentBonus + " % ] (AOA)"
        }, 0 );

        if (  ! Objects.isNull ( relatorios ) )
        {
            for ( Object[] relatorio : relatorios )
            {
                Object[] rowData = new Object[]
                {
                    relatorio[ fk_compra ],
                    relatorio[ cod_fact ],
                    relatorio[ designacao_produto ],
                    relatorio[ preco_fabrica ],
                    relatorio[ quantidade ],
                    CfMethods.formatarComoMoeda ( ( double ) relatorio[ sub_total ] ),
                    CfMethods.formatarComoMoeda ( ( double ) relatorio[ bonus ] )

                };

                totalProcessadoBonusEmpresa += ( Double ) relatorio[ sub_total ];
                totalBonusEmpresa += ( Double ) relatorio[ bonus ];

                defaultTableModel.addRow ( rowData );
            }
        }

        bonusEmpresaJTable.setModel ( defaultTableModel );
        bonusEmpresaJTable.getColumnModel ().getColumn ( 0 ).setMinWidth ( 0 );
        bonusEmpresaJTable.getColumnModel ().getColumn ( 0 ).setMaxWidth ( 100 );
        bonusEmpresaJTable.getColumnModel ().getColumn ( 1 ).setMinWidth ( 0 );
        bonusEmpresaJTable.getColumnModel ().getColumn ( 1 ).setMaxWidth ( 100 );
        bonusEmpresaJTable.getColumnModel ().getColumn ( 3 ).setMinWidth ( 0 );
        bonusEmpresaJTable.getColumnModel ().getColumn ( 3 ).setMaxWidth ( 100 );
        bonusEmpresaJTable.getColumnModel ().getColumn ( 4 ).setMinWidth ( 0 );
        bonusEmpresaJTable.getColumnModel ().getColumn ( 4 ).setMaxWidth ( 100 );
        bonusEmpresaJTable.getColumnModel ().getColumn ( 5 ).setMinWidth ( 100 );
        bonusEmpresaJTable.getColumnModel ().getColumn ( 5 ).setMaxWidth ( 250 );

        bonusEmpresaJTable.getColumnModel ().getColumn ( 6 ).setMinWidth ( 150 );
        bonusEmpresaJTable.getColumnModel ().getColumn ( 6 ).setMaxWidth ( 250 );

        atualizarResumoBonusEmpresa ();
        ativarDesativarCamposBonusEmpresa ();
    }

    private void atualizarResumoBonusCliente ()
    {
        totalProcessadoJTextField.setText ( CfMethods.formatarComoMoeda ( totalProcessadoBonusCliente ) );
        totalBonusJTextField.setText ( CfMethods.formatarComoMoeda ( totalBonusCliente ) );
    }

    private void atualizarResumoBonusEmpresa ()
    {
        totalProcessadoJTextField2.setText ( CfMethods.formatarComoMoeda ( totalProcessadoBonusEmpresa ) );
        totalBonusJTextField2.setText ( CfMethods.formatarComoMoeda ( totalBonusEmpresa ) );
    }

    private void exibirDetalhesBonusCliente ()
    {
        detalhesBonusClienteJDialog.setUndecorated ( true );
        detalhesBonusClienteJDialog.setSize ( getWidth () + 100, getHeight () );

        detalhesBonusClienteJDialog.setModal ( true );
        detalhesBonusClienteJDialog.setLocationRelativeTo ( this );
        detalhesBonusClienteJDialog.setVisible ( true );
    }
    private void exibirDetalhesBonusEmpresa ()
    {
        detalhesBonusEmpresaJDialog.setUndecorated ( true );
        detalhesBonusEmpresaJDialog.setSize ( getWidth () + 100, getHeight () );

        detalhesBonusEmpresaJDialog.setModal ( true );
        detalhesBonusEmpresaJDialog.setLocationRelativeTo ( this );
        detalhesBonusEmpresaJDialog.setVisible ( true );
    }

    private void fecharJanelaDetalhesBonusCliente ()
    {
        detalhesBonusClienteJDialog.dispose ();
    }
    private void fecharJanelaDetalhesBonusEmpresa ()
    {
        detalhesBonusEmpresaJDialog.dispose ();
    }

    private void imprimirRelatorioBonusCliente ()
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat ( "dd/MM/yyyy" );

        Date date1 = ( Objects.isNull ( dataInicioJDateChooser.getDate () ) ? new Date () : dataInicioJDateChooser.getDate () );
        Date date2 = ( Objects.isNull ( dataFimJDateChooser.getDate () ) ? new Date () : dataFimJDateChooser.getDate () );
        TbCliente tbCliente = clienteDao.getClienteByNome ( ( String ) clientesJCombobox.getSelectedItem () );

//        Date date2 = dataFimJDateChooser.getDate ())?new Date(): dataFimJDateChooser.getDate ();
        //PARM_LUCRO
        HashMap hashMap = new HashMap ();

        hashMap.put ( "H1", "EXTRATO DE BONUS DO CLIENTE -  (  " + tbCliente.getNome () + " ) " );
        hashMap.put ( "H2", "Período ( de " + dateFormat.format ( date1 ) + " à " + dateFormat.format ( date2 ) + " ) " );
        hashMap.put ( "REPORT_LOCALE", new Locale ("pt") );
        hashMap.put ( "SUBREPORT_DIR", "relatorios/" );
        hashMap.put ( "FK_USUARIO", tbUsuario.getCodigo () );
        hashMap.put ( "USUARIO", tbUsuario.getNome () );
        hashMap.put ( "FK_ARMAZEM", armazemDao.getArmazemByDescricao ( ( String ) armazemJCombobox.getSelectedItem () ).getCodigo () );
        hashMap.put ( "FK_CLIENTE", clienteDao.getClienteByNome ( ( String ) clientesJCombobox.getSelectedItem () ).getCodigo () );
        hashMap.put ( "DATA_INICIO", date1 );
        hashMap.put ( "DATA_FIM", date2 );
        hashMap.put ( "TAXA", percentagemJSpinner.getValue () );
        hashMap.put ( "TIPO_DOC", getSelectedBonusClienteDoc () );

        String relatorio = "relatorios/bonus_cliente_relatotorio.jasper";

        File file = new File ( relatorio ).getAbsoluteFile ();
        String obterCaminho = file.getAbsolutePath ();

        try
        {
            Connection connection = BDConexao.getConnection ();
            JasperFillManager.fillReport ( obterCaminho, hashMap, connection );
            JasperPrint jasperPrint = JasperFillManager.fillReport ( obterCaminho, hashMap, connection );

            if ( jasperPrint.getPages ().size () >= 1 )
            {
                JasperViewer jasperViewer = new JasperViewer ( jasperPrint, false );
                jasperViewer.setVisible ( true );
            }
            else
            {
                JOptionPane.showMessageDialog ( null, "Não existe registro para esse intervalo de data!...", "ERROR", JOptionPane.ERROR_MESSAGE );
            }

        }
        catch ( JRException jex )
        {
            jex.printStackTrace ();
            JOptionPane.showMessageDialog ( null, "Falha ao mostrar o relatorio", "DVML-COMERCIAL, LDA", JOptionPane.ERROR );
        }
        catch ( Exception ex )
        {
            ex.printStackTrace ();
            JOptionPane.showMessageDialog ( null, "Erro ao tentar mostrar o relatorio", "DVML-COMERCIAL, LDA", JOptionPane.ERROR_MESSAGE );
        }
    }
    
    private void imprimirRelatorioBonusEmpresa ()
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat ( "dd/MM/yyyy" );

        Date date1 = ( Objects.isNull ( dataInicioJDateChooser2.getDate () ) ? new Date () : dataInicioJDateChooser2.getDate () );
        Date date2 = ( Objects.isNull ( dataFimJDateChooser2.getDate () ) ? new Date () : dataFimJDateChooser2.getDate () );
        TbFornecedor tbFornecedor = FornecedorDao.findByNome ( ( String ) fornecedorJCombobox.getSelectedItem () );

//        Date date2 = dataFimJDateChooser.getDate ())?new Date(): dataFimJDateChooser.getDate ();
        //PARM_LUCRO
        HashMap hashMap = new HashMap ();

        hashMap.put ( "H1", "EXTRATO DE BONUS DA EMPRESA - Forn. (  " + tbFornecedor.getNome () + " ) " );
        hashMap.put ( "H2", "Período ( de " + dateFormat.format ( date1 ) + " à " + dateFormat.format ( date2 ) + " ) " );
        hashMap.put ( "SUBREPORT_DIR", "relatorios/" );
        hashMap.put ( "REPORT_LOCALE", new Locale ("pt") );
        
        hashMap.put ( "FK_USUARIO", tbUsuario.getCodigo () );
        hashMap.put ( "USUARIO", tbUsuario.getNome () );
        hashMap.put ( "FK_ARMAZEM", armazemDao.getArmazemByDescricao ( ( String ) armazemJCombobox2.getSelectedItem () ).getCodigo () );
        hashMap.put ( "FK_FORNECEDOR", tbFornecedor.getCodigo () );
        hashMap.put ( "DATA_INICIO", date1 );
        hashMap.put ( "DATA_FIM", date2 );
        hashMap.put ( "TAXA", percentagemJSpinner2.getValue () );
        hashMap.put ( "TIPO_DOC", getSelectedBonusEmpresaDoc () );

        String relatorio = "relatorios/bonus_empresa_relatotorio.jasper";

        File file = new File ( relatorio ).getAbsoluteFile ();
        String obterCaminho = file.getAbsolutePath ();

        try
        {
            Connection connection = BDConexao.getConnection ();
            JasperFillManager.fillReport ( obterCaminho, hashMap, connection );
            JasperPrint jasperPrint = JasperFillManager.fillReport ( obterCaminho, hashMap, connection );

            if ( jasperPrint.getPages ().size () >= 1 )
            {
                JasperViewer jasperViewer = new JasperViewer ( jasperPrint, false );
                jasperViewer.setVisible ( true );
            }
            else
            {
                JOptionPane.showMessageDialog ( null, "Não existe registro para esse intervalo de data!...", "ERROR", JOptionPane.ERROR_MESSAGE );
            }

        }
        catch ( JRException jex )
        {
            jex.printStackTrace ();
            JOptionPane.showMessageDialog ( null, "Falha ao mostrar o relatorio", "DVML-COMERCIAL, LDA", JOptionPane.ERROR );
        }
        catch ( Exception ex )
        {
            ex.printStackTrace ();
            JOptionPane.showMessageDialog ( null, "Erro ao tentar mostrar o relatorio", "DVML-COMERCIAL, LDA", JOptionPane.ERROR_MESSAGE );
        }
    }

    public String getSelectedBonusClienteDoc ()
    {
        if ( rb_FR4.isSelected () )
        {
            return rb_FR4.getText ();
        }
        if ( rb_FT4.isSelected () )
        {
            return rb_FT4.getText ();
        }
        if ( rb_PP4.isSelected () )
        {
            return rb_PP4.getText ();
        }
        if ( rb_RC4.isSelected () )
        {
            return rb_RC4.getText ();
        }

        return null;
    }

    public String getSelectedBonusEmpresaDoc ()
    {
        if ( rb_CO.isSelected () )
        {
            return rb_CO.getText ();
        }
       
        return null;
    }

    private void inicializarBonusClientesTab ()
    {

        //<editor-fold defaultstate="collapsed" desc=" ÁREA DE CRIAÇÃO E INICIALIZAÇÃO DAS VARIÁVEL">        
        System.err.println ( "" );
        int colIndex = 0;
        double percentBonus = ( double ) percentagemJSpinner.getValue ();
        final int fk_produto = colIndex ++;
        final int designacao_produto = colIndex ++;
        final int quantidade = colIndex ++;
        final int preco_fabrica = colIndex ++;
        final int fk_cliente = colIndex ++;
        final int nome_cliente = colIndex ++;
        final int data = colIndex ++;
        final int fk_armazem = colIndex ++;
        final int armazem = colIndex ++;
        final int fk_documento = colIndex ++;
        final int abreviacao_documento = colIndex ++;
        final int codigo_venda = colIndex ++;
        final int cod_fact = colIndex ++;
        final int sub_total = colIndex ++;
        final int bonus = colIndex ++;
        //</editor-fold>

        armazemJCombobox.setModel ( criarArmazemQueVenderamJComboboxModel () );
        armazemJCombobox.addItemListener ( new ItemListener ()
        {
            @Override
            public void itemStateChanged ( ItemEvent e )
            {
                clientesJCombobox.setModel ( criarClientesQueCompraramNoArmazemJComboboxModel ( ( String ) e.getItem () ) );
                atualizarTabelaBonusClientes ();
            }
        } );

        clientesJCombobox.setModel ( criarClientesQueCompraramNoArmazemJComboboxModel ( ( String ) armazemJCombobox.getSelectedItem () ) );
        clientesJCombobox.addItemListener ( new ItemListener ()
        {
            @Override
            public void itemStateChanged ( ItemEvent e )
            {
                atualizarTabelaBonusClientes ();
            }
        } );

        percentagemJSpinner.addChangeListener ( new ChangeListener ()
        {
            @Override
            public void stateChanged ( ChangeEvent e )
            {
                atualizarTabelaBonusClientes ();
            }
        } );

        dataInicioJDateChooser.setLocale ( new Locale ( "pt" ) );
        dataFimJDateChooser.setLocale ( new Locale ( "pt" ) );

        dataInicioJDateChooser.getDateEditor ().addPropertyChangeListener ( new PropertyChangeListener ()
        {
            @Override
            public void propertyChange ( PropertyChangeEvent evt )
            {
                atualizarTabelaBonusClientes ();
            }
        } );

        dataFimJDateChooser.getDateEditor ().addPropertyChangeListener ( new PropertyChangeListener ()
        {
            @Override
            public void propertyChange ( PropertyChangeEvent evt )
            {
                atualizarTabelaBonusClientes ();
            }
        } );

        resizeJButtonIcon ( 32, 32, getClass ().getResource ( "/imagens/bonus/icons8_print_200px.png" ), imprimirJButton );
        resizeJButtonIcon ( 32, 32, getClass ().getResource ( "/imagens/bonus/icons8_details_popup_128px.png" ), detalhesJButton );
        resizeJButtonIcon ( 32, 32, getClass ().getResource ( "/imagens/2934_32x32.png" ), fecharDetalhesBonusJButton );
    }

    private void inicializarBonusEmpresaTab ()
    {
        //<editor-fold defaultstate="collapsed" desc=" ÁREA DE CRIAÇÃO E INICIALIZAÇÃO DAS VARIÁVEL">        
        System.err.println ( "" );
        int colIndex = 0;
        double percentBonus = ( double ) percentagemJSpinner2.getValue ();
        final int fk_produto = colIndex ++;
        final int designacao_produto = colIndex ++;
        final int quantidade = colIndex ++;
        final int preco_fabrica = colIndex ++;
        final int fk_cliente = colIndex ++;
        final int nome_cliente = colIndex ++;
        final int data = colIndex ++;
        final int fk_armazem = colIndex ++;
        final int armazem = colIndex ++;
        final int fk_documento = colIndex ++;
        final int abreviacao_documento = colIndex ++;
        final int codigo_venda = colIndex ++;
        final int cod_fact = colIndex ++;
        final int sub_total = colIndex ++;
        final int bonus = colIndex ++;
        //</editor-fold>

        armazemJCombobox2.setModel ( criarArmazemQueVenderamJComboboxModel () );
        armazemJCombobox2.addItemListener ( new ItemListener ()
        {
            @Override
            public void itemStateChanged ( ItemEvent e )
            {
                fornecedorJCombobox.setModel ( criarClientesQueCompraramNoArmazemJComboboxModel ( ( String ) e.getItem () ) );
                atualizarTabelaBonusEmpresa ();
            }
        } );

        fornecedorJCombobox.setModel ( criarFornecedoreQueForneceramAoArmazemJComboboxModel ( ( String ) armazemJCombobox2.getSelectedItem () ) );
        fornecedorJCombobox.addItemListener ( new ItemListener ()
        {
            @Override
            public void itemStateChanged ( ItemEvent e )
            {
                atualizarTabelaBonusEmpresa ();
            }
        } );

        percentagemJSpinner2.addChangeListener ( new ChangeListener ()
        {
            @Override
            public void stateChanged ( ChangeEvent e )
            {
                atualizarTabelaBonusEmpresa ();
            }
        } );

        dataInicioJDateChooser2.setLocale ( new Locale ( "pt" ) );
        dataFimJDateChooser2.setLocale ( new Locale ( "pt" ) );

        dataInicioJDateChooser2.getDateEditor ().addPropertyChangeListener ( new PropertyChangeListener ()
        {
            @Override
            public void propertyChange ( PropertyChangeEvent evt )
            {
                atualizarTabelaBonusEmpresa ();
            }
        } );

        dataFimJDateChooser2.getDateEditor ().addPropertyChangeListener ( new PropertyChangeListener ()
        {
            @Override
            public void propertyChange ( PropertyChangeEvent evt )
            {
                atualizarTabelaBonusEmpresa ();
            }
        } );

        resizeJButtonIcon ( 32, 32, getClass ().getResource ( "/imagens/bonus/icons8_print_200px.png" ), imprimirJButton2 );
        resizeJButtonIcon ( 32, 32, getClass ().getResource ( "/imagens/bonus/icons8_details_popup_128px.png" ), detalhesJButton2 );
        resizeJButtonIcon ( 32, 32, getClass ().getResource ( "/imagens/2934_32x32.png" ), fecharDetalhesBonusJButton1 );
    }
}
