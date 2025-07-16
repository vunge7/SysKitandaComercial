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
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import kitanda.util.CfMethods;
import static kitanda.util.CfMethods.parseMoedaFormatada;
import static kitanda.util.CfMethodsSwing.*;
import util.*;
import static util.DVML.DOC_RECIBO_RC;
import visao.FormaPagamentoVisao;

/**
 *
 * @author Domingos Dala Vunge
 */
public class ReciboVisao extends javax.swing.JFrame
{

    /**
     * Creates new form ListaUsuarioVisao
     */
    private static EntityManagerFactory emf = JPAEntityMannagerFactoryUtil.em;
    private static VendaDao vendaDao = new VendaDao( emf );
    private static UsuarioDao usuarioDao = new UsuarioDao( emf );
    private static ProdutoDao produtoDao = new ProdutoDao( emf );
    private static ItemVendaDao itemVendaDao = new ItemVendaDao( emf );
    private static DocumentoDao documentoDao = new DocumentoDao( emf );
    private static PrecoDao precoDao = new PrecoDao( emf );
    private ClienteDao clienteDao = new ClienteDao( emf );
    private static ArmazemDao armazemDao = new ArmazemDao( emf );
//    private BancoDao bancoDao = new BancoDao( emf );
    private static AnoEconomicoDao anoEconomicoDao = new AnoEconomicoDao( emf );
    private static BDConexao conexao;
//    private List<TbVenda> lista = null;
    private double total_geral = 0;
    private List<RelatorioClienteBonusVipUtil> lista = new ArrayList<RelatorioClienteBonusVipUtil>();
    private int CLIENTES_TAB;
    private int EMPRESAS_TAB;
    private static Double totalProcessado;
    private static Double totalBonusCliente;
    private static TbUsuario tbUsuario;
    private Double totalProcessadoBonusEmpresa;
    private Double totalBonusEmpresa;
    private static String prox_doc;
    public static double gorjeta = 0;

    public ReciboVisao( BDConexao conexao )
    {
        this( 15, conexao );
    }

    public ReciboVisao( Integer fkUsuario, BDConexao conexao )
    {

        tbUsuario = new UsuarioDao().findTbUsuario( fkUsuario );

        initComponents();
        setLocationRelativeTo( null );
        this.conexao = conexao;

        inicializar();

    }

    @SuppressWarnings("unchecked")
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
        detalhesJTable = new javax.swing.JTable();
        sulJPanel1 = new javax.swing.JPanel();
        fecharDetalhesBonusJButton = new javax.swing.JButton();
        docButtonGroup1 = new javax.swing.ButtonGroup();
        principalJPanle = new javax.swing.JPanel();
        norteJPanel = new javax.swing.JPanel();
        tituloJLabel = new javax.swing.JLabel();
        centroJPanel = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        proximoDocJLabel = new javax.swing.JLabel();
        footerJPanel = new javax.swing.JPanel();
        lbObs = new javax.swing.JLabel();
        totalAPagarJTextField = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        fecharJButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        taObs = new javax.swing.JTextArea();
        lbData21 = new javax.swing.JLabel();
        bodyJPanel = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        armazemJCombobox = new javax.swing.JComboBox<>();
        jLabel10 = new javax.swing.JLabel();
        clientesJCombobox = new javax.swing.JComboBox<>();
        lbData15 = new javax.swing.JLabel();
        faturaJSpinner = new javax.swing.JSpinner();
        lbData16 = new javax.swing.JLabel();
        lbData17 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        totalPagoJTextField = new javax.swing.JTextField();
        valorTotalFaturaJTextField = new javax.swing.JTextField();
        lbData20 = new javax.swing.JLabel();
        totalRetencaoJTextField = new javax.swing.JTextField();
        sulJPanel = new javax.swing.JPanel();

        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel4.setLayout(new java.awt.BorderLayout());

        norteJPanel1.setLayout(new java.awt.GridBagLayout());

        tituloJLabel1.setFont(new java.awt.Font("SansSerif", 1, 24)); // NOI18N
        tituloJLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        tituloJLabel1.setText("DETALHES DA FATURA");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.01;
        gridBagConstraints.weighty = 0.01;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        norteJPanel1.add(tituloJLabel1, gridBagConstraints);

        jPanel4.add(norteJPanel1, java.awt.BorderLayout.NORTH);

        detalhesCentroJPanel.setLayout(new java.awt.GridBagLayout());

        detalhesJTable.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane2.setViewportView(detalhesJTable);

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

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("...:::: DVML - EMISSÃO DE RECIBOS ::...");

        principalJPanle.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        norteJPanel.setLayout(new java.awt.GridBagLayout());

        tituloJLabel.setFont(new java.awt.Font("SansSerif", 1, 24)); // NOI18N
        tituloJLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        tituloJLabel.setText("PROCESSAMENTO DE RECIBOS");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.01;
        gridBagConstraints.weighty = 0.01;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        norteJPanel.add(tituloJLabel, gridBagConstraints);

        principalJPanle.add(norteJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 957, -1));

        jPanel5.setLayout(new java.awt.BorderLayout());

        proximoDocJLabel.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jPanel5.add(proximoDocJLabel, java.awt.BorderLayout.PAGE_START);

        footerJPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "PAGAMENTO", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP, new java.awt.Font("SansSerif", 1, 14))); // NOI18N
        footerJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lbObs.setFont(new java.awt.Font("SansSerif", 1, 16)); // NOI18N
        lbObs.setText("OBS :");
        footerJPanel.add(lbObs, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 10, 50, -1));

        totalAPagarJTextField.setEditable(false);
        totalAPagarJTextField.setFont(new java.awt.Font("SansSerif", 0, 16)); // NOI18N
        totalAPagarJTextField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        footerJPanel.add(totalAPagarJTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 10, 170, 40));

        jButton1.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/1323444801_currency_dollar red.png"))); // NOI18N
        jButton1.setText("Forma Pagamento");
        jButton1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButton1ActionPerformed(evt);
            }
        });
        footerJPanel.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 90, 270, 60));

        fecharJButton.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        fecharJButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagem/out/out_32x32.png"))); // NOI18N
        fecharJButton.setText("Sair");
        fecharJButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                fecharJButtonActionPerformed(evt);
            }
        });
        footerJPanel.add(fecharJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 90, 100, 60));

        taObs.setColumns(20);
        taObs.setRows(5);
        jScrollPane1.setViewportView(taObs);

        footerJPanel.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 10, 410, 60));

        lbData21.setFont(new java.awt.Font("SansSerif", 1, 16)); // NOI18N
        lbData21.setText("Total a pagar :");
        footerJPanel.add(lbData21, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, -1, -1));

        bodyJPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "DADOS DA FATURA", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP, new java.awt.Font("SansSerif", 1, 14))); // NOI18N
        bodyJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel11.setFont(new java.awt.Font("SansSerif", 1, 16)); // NOI18N
        jLabel11.setText("Armazém :");
        bodyJPanel.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, -1, -1));

        armazemJCombobox.setFont(new java.awt.Font("SansSerif", 0, 16)); // NOI18N
        armazemJCombobox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        bodyJPanel.add(armazemJCombobox, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 30, 336, -1));

        jLabel10.setFont(new java.awt.Font("SansSerif", 1, 16)); // NOI18N
        jLabel10.setText("Cliente :");
        bodyJPanel.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 70, -1, -1));

        clientesJCombobox.setFont(new java.awt.Font("SansSerif", 0, 16)); // NOI18N
        clientesJCombobox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        clientesJCombobox.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                clientesJComboboxActionPerformed(evt);
            }
        });
        bodyJPanel.add(clientesJCombobox, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 70, 460, -1));

        lbData15.setFont(new java.awt.Font("SansSerif", 1, 16)); // NOI18N
        lbData15.setText("Fatura :");
        bodyJPanel.add(lbData15, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 100, -1, -1));

        faturaJSpinner.setFont(new java.awt.Font("SansSerif", 0, 16)); // NOI18N
        faturaJSpinner.setModel(new javax.swing.SpinnerNumberModel(0.0d, 0.0d, 100.0d, 1.0d));
        bodyJPanel.add(faturaJSpinner, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 100, 180, -1));

        lbData16.setFont(new java.awt.Font("SansSerif", 1, 16)); // NOI18N
        lbData16.setText("Valor total :");
        bodyJPanel.add(lbData16, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 140, -1, -1));

        lbData17.setFont(new java.awt.Font("SansSerif", 1, 16)); // NOI18N
        lbData17.setText("Total pago :");
        bodyJPanel.add(lbData17, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 170, -1, -1));
        bodyJPanel.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(447, 170, 310, -1));

        totalPagoJTextField.setEditable(false);
        totalPagoJTextField.setFont(new java.awt.Font("SansSerif", 0, 16)); // NOI18N
        totalPagoJTextField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        bodyJPanel.add(totalPagoJTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 170, 336, -1));

        valorTotalFaturaJTextField.setEditable(false);
        valorTotalFaturaJTextField.setFont(new java.awt.Font("SansSerif", 0, 16)); // NOI18N
        valorTotalFaturaJTextField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        bodyJPanel.add(valorTotalFaturaJTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 130, 336, -1));

        lbData20.setFont(new java.awt.Font("SansSerif", 1, 16)); // NOI18N
        lbData20.setText("T.Retencao :");
        bodyJPanel.add(lbData20, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 200, -1, -1));

        totalRetencaoJTextField.setEditable(false);
        totalRetencaoJTextField.setFont(new java.awt.Font("SansSerif", 0, 16)); // NOI18N
        totalRetencaoJTextField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        bodyJPanel.add(totalRetencaoJTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 200, 330, -1));

        javax.swing.GroupLayout centroJPanelLayout = new javax.swing.GroupLayout(centroJPanel);
        centroJPanel.setLayout(centroJPanelLayout);
        centroJPanelLayout.setHorizontalGroup(
            centroJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(footerJPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(centroJPanelLayout.createSequentialGroup()
                .addGroup(centroJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 814, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bodyJPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 843, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        centroJPanelLayout.setVerticalGroup(
            centroJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(centroJPanelLayout.createSequentialGroup()
                .addGroup(centroJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bodyJPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(footerJPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(48, 48, 48))
        );

        principalJPanle.add(centroJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 46, -1, 400));
        principalJPanle.add(sulJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 560, 611, -1));

        getContentPane().add(principalJPanle, java.awt.BorderLayout.CENTER);

        setSize(new java.awt.Dimension(843, 502));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void fecharJButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_fecharJButtonActionPerformed
    {//GEN-HEADEREND:event_fecharJButtonActionPerformed
        fechar();
    }//GEN-LAST:event_fecharJButtonActionPerformed

    private void fecharDetalhesBonusJButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_fecharDetalhesBonusJButtonActionPerformed
    {//GEN-HEADEREND:event_fecharDetalhesBonusJButtonActionPerformed
        fecharJanelaDetalhesBonusCliente();
    }//GEN-LAST:event_fecharDetalhesBonusJButtonActionPerformed

    private void clientesJComboboxActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_clientesJComboboxActionPerformed
    {//GEN-HEADEREND:event_clientesJComboboxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_clientesJComboboxActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        if ( MetodosUtil.licencaValidada( conexao ) )
        {
            finalizar_recibo();
        }
    }//GEN-LAST:event_jButton1ActionPerformed

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
            java.util.logging.Logger.getLogger( ReciboVisao.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        catch ( InstantiationException ex )
        {
            java.util.logging.Logger.getLogger( ReciboVisao.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        catch ( IllegalAccessException ex )
        {
            java.util.logging.Logger.getLogger( ReciboVisao.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        catch ( javax.swing.UnsupportedLookAndFeelException ex )
        {
            java.util.logging.Logger.getLogger( ReciboVisao.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
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

        /* Create and display the form */
        java.awt.EventQueue.invokeLater( new Runnable()
        {
            public void run()
            {
                try
                {
                    new ReciboVisao( new BDConexao() ).setVisible( true );
                }
                catch ( Exception ex )
                {
                    Logger.getLogger( ReciboVisao.class.getName() ).log( Level.SEVERE, null, ex );
                }
            }
        } );
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private static javax.swing.JComboBox<String> armazemJCombobox;
    private javax.swing.JPanel bodyJPanel;
    private javax.swing.JPanel centroJPanel;
    private static javax.swing.JComboBox<String> clientesJCombobox;
    private javax.swing.JDialog detalhesBonusClienteJDialog;
    private javax.swing.JPanel detalhesCentroJPanel;
    private static javax.swing.JTable detalhesJTable;
    private javax.swing.ButtonGroup docButtonGroup;
    private javax.swing.ButtonGroup docButtonGroup1;
    private static javax.swing.JSpinner faturaJSpinner;
    private javax.swing.JButton fecharDetalhesBonusJButton;
    private javax.swing.JButton fecharJButton;
    private javax.swing.JPanel footerJPanel;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lbData15;
    private javax.swing.JLabel lbData16;
    private javax.swing.JLabel lbData17;
    private javax.swing.JLabel lbData20;
    private javax.swing.JLabel lbData21;
    private javax.swing.JLabel lbObs;
    private javax.swing.JPanel norteJPanel;
    private javax.swing.JPanel norteJPanel1;
    private javax.swing.JPanel principalJPanle;
    private javax.swing.JLabel proximoDocJLabel;
    private javax.swing.JPanel sulJPanel;
    private javax.swing.JPanel sulJPanel1;
    private static javax.swing.JTextArea taObs;
    private javax.swing.JLabel tituloJLabel;
    private javax.swing.JLabel tituloJLabel1;
    public static javax.swing.JTextField totalAPagarJTextField;
    private static javax.swing.JTextField totalPagoJTextField;
    private static javax.swing.JTextField totalRetencaoJTextField;
    private static javax.swing.JTextField valorTotalFaturaJTextField;
    // End of variables declaration//GEN-END:variables

    private void inicializar()
    {
        totalProcessadoBonusEmpresa = 0.0;
        totalBonusEmpresa = 0.0;

        CLIENTES_TAB = 0;
        EMPRESAS_TAB = 1;
        resizeJLableIcon( 50, 50, getClass().getResource( "/imagens/bonus/icons8_receipt_terminal_128px.png" ), tituloJLabel );
        resizeJButtonIcon( 32, 32, getClass().getResource( "/imagens/2934_32x32.png" ), fecharJButton );

        inicializarComponentes();
        atualizarTabela();
    }

    private void fechar()
    {
        dispose();
    }

    private static ComboBoxModel<String> criarArmazemQueVenderamAPrazoJComboboxModel()
    {
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String>();

        List<TbArmazem> armazems = ArmazemDao.buscarArmazensComVendaAPrazo( 2 );

        for ( TbArmazem armazem : armazems )
        {
            model.addElement( armazem.getDesignacao() );
        }

        return model;
    }

    private static ComboBoxModel<String> criarClientesQueCompraramAPrazoNoArmazemJComboboxModel( String desigancaoArmazem )
    {
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String>();

        List<TbCliente> clientes = ClienteDao.buscarClientesQueCompraramAPrazoNoArmazem( desigancaoArmazem, 2 );

        System.out.println( "TAMANHO CLIENTE >>>>>> " + clientes.size() );
        if ( !Objects.isNull( clientes ) )
        {
            for ( TbCliente cliente : clientes )
            {
                model.addElement( cliente.getNome() );
            }
        }

        return model;
    }

    private static void ativarDesativarCamposBonusCliente()
    {
        boolean registrosEncontrados = detalhesJTable.getRowCount() > 0;

    }

    private static void atualizarTabela()
    {

        try
        {
            Object refDocObject = faturaJSpinner.getValue();
            String refDoc = Objects.isNull( refDocObject ) ? "" : (String) refDocObject;

            System.err.println( "" );
            totalProcessado = 0.0;
            totalBonusCliente = 0.0;
            int colIndex = 0;
            final int pk_venda = colIndex++;
            final int documento_designacao = colIndex++;
            final int tb_venda_cod_fact = colIndex++;
            final int moeda_abreviacao = colIndex++;
//        final int tb_banco_descrisao = colIndex++;
            final int tb_venda_dataVenda = colIndex++;
            final int tb_venda_hora = colIndex++;
            final int tb_produto_codigo = colIndex++;
            final int tb_produto_designacao = colIndex++;
            final int tb_item_venda_codigo_isensao = colIndex++;
            final int tb_item_venda_quantidade = colIndex++;
            final int tb_preco_preco_venda = colIndex++;
            final int tb_item_venda_desconto = colIndex++;
            final int tb_item_venda_valor_iva = colIndex++;
            final int tb_item_venda_total = colIndex++;
            final int tb_venda_total_geral = colIndex++;
            final int tb_venda_desconto_comercial = colIndex++;
            final int tb_venda_desconto_financeiro = colIndex++;
            final int tb_venda_total_iva = colIndex++;
            final int tb_venda_total_venda = colIndex++;
            final int tb_venda_valor_entregue = colIndex++;
            final int tb_venda_troco = colIndex++;
            final int tb_venda_total_por_extenso = colIndex++;
            /*
             
             */
//

            List<Object[]> relatorios = UtilDao.getDetalhesFatura( refDoc );
            DefaultTableModel defaultTableModel = new DefaultTableModel( new Object[]
            {
                "Cod. Prod",
                "Produto",
                "Cod. Isensao",
                "Qauntidade",
                "Preco Unit",
                "Desconto",
                "Taxa Iva",
                "Subtotal"
            }, 0 );

            if ( !Objects.isNull( relatorios ) )
            {
                for ( Object[] relatorio : relatorios )
                {

                    BigDecimal bigDecimalDesconto = new BigDecimal( relatorio[tb_item_venda_desconto].toString() );
                    BigDecimal bigDecimalTotal = new BigDecimal( relatorio[tb_item_venda_total].toString() );

                    System.out.println( "CODIGO: " + relatorio[tb_produto_codigo] );
                    System.out.println( "DESIGNACAO: " + relatorio[tb_produto_designacao] );
                    System.out.println( "CODIGO ISENSAO: " + relatorio[tb_item_venda_codigo_isensao] );
                    System.out.println( "QTD: " + relatorio[tb_item_venda_quantidade] );
                    System.out.println( "PRECO VENDA: " + relatorio[tb_preco_preco_venda] );
                    System.out.println( "DESCONTO: " + bigDecimalDesconto );
                    System.out.println( "IVA: " + relatorio[tb_item_venda_valor_iva] );
                    System.out.println( "TOTAL VENDA: " + relatorio[tb_venda_total_venda] );
                    System.out.println( "TOTAL: " + bigDecimalTotal );

                    Object[] rowData = new Object[]
                    {
                        relatorio[tb_produto_codigo],
                        relatorio[tb_produto_designacao],
                        relatorio[tb_item_venda_codigo_isensao],
                        relatorio[tb_item_venda_quantidade],
                        //                    CfMethods.formatarComoMoeda( (double) relatorio[tb_preco_preco_venda] ),
                        relatorio[tb_preco_preco_venda],
                        CfMethods.formatarComoMoeda( bigDecimalDesconto ),
                        relatorio[tb_item_venda_valor_iva],
                        CfMethods.formatarComoMoeda( bigDecimalTotal )
                    };
//                    totalProcessado += bigDecimalTotal.doubleValue();
                    defaultTableModel.addRow( rowData );
                    totalProcessado = Double.parseDouble( String.valueOf( relatorio[tb_venda_total_venda] ) );
                }
            }

            detalhesJTable.setModel( defaultTableModel );
            detalhesJTable.getColumnModel().getColumn( 0 ).setMinWidth( 0 );
            detalhesJTable.getColumnModel().getColumn( 0 ).setMaxWidth( 100 );
            detalhesJTable.getColumnModel().getColumn( 2 ).setMinWidth( 0 );
            detalhesJTable.getColumnModel().getColumn( 2 ).setMaxWidth( 200 );
            detalhesJTable.getColumnModel().getColumn( 3 ).setMinWidth( 0 );
            detalhesJTable.getColumnModel().getColumn( 3 ).setMaxWidth( 100 );
            detalhesJTable.getColumnModel().getColumn( 4 ).setMinWidth( 0 );
            detalhesJTable.getColumnModel().getColumn( 4 ).setMaxWidth( 100 );
            detalhesJTable.getColumnModel().getColumn( 5 ).setMinWidth( 100 );
            detalhesJTable.getColumnModel().getColumn( 5 ).setMaxWidth( 250 );

            detalhesJTable.getColumnModel().getColumn( 6 ).setMinWidth( 150 );
            detalhesJTable.getColumnModel().getColumn( 6 ).setMaxWidth( 250 );

            detalhesJTable.getColumnModel().getColumn( 7 ).setMinWidth( 150 );
            detalhesJTable.getColumnModel().getColumn( 7 ).setMaxWidth( 250 );

            atualizarResumo();
            ativarDesativarCamposBonusCliente();

        }
        catch ( Exception e )
        {
        }

    }

    private void exibirDetalhes()
    {
        detalhesBonusClienteJDialog.setUndecorated( true );
        detalhesBonusClienteJDialog.setSize( getWidth() + 100, getHeight() );

        detalhesBonusClienteJDialog.setModal( true );
        detalhesBonusClienteJDialog.setLocationRelativeTo( this );
        detalhesBonusClienteJDialog.setVisible( true );
    }

    private void fecharJanelaDetalhesBonusCliente()
    {
        detalhesBonusClienteJDialog.dispose();
    }

    private void imprimirRelatorioBonusCliente()
    {
//        SimpleDateFormat dateFormat = new SimpleDateFormat ( "dd/MM/yyyy" );
//
//        Date date1 = ( Objects.isNull ( dataInicioJDateChooser.getDate () ) ? new Date () : dataInicioJDateChooser.getDate () );
//        Date date2 = ( Objects.isNull ( dataFimJDateChooser.getDate () ) ? new Date () : dataFimJDateChooser.getDate () );
//        TbCliente tbCliente = clienteDao.getClienteByNome ( ( String ) clientesJCombobox.getSelectedItem () );
//
////        Date date2 = dataFimJDateChooser.getDate ())?new Date(): dataFimJDateChooser.getDate ();
//        //PARM_LUCRO
//        HashMap hashMap = new HashMap ();
//
//        hashMap.put ( "H1", "EXTRATO DE BONUS DO CLIENTE -  (  " + tbCliente.getNome () + " ) " );
//        hashMap.put ( "H2", "Período ( de " + dateFormat.format ( date1 ) + " à " + dateFormat.format ( date2 ) + " ) " );
//        hashMap.put ( "REPORT_LOCALE", new Locale ("pt") );
//        hashMap.put ( "SUBREPORT_DIR", "relatorios/" );
//        hashMap.put ( "FK_USUARIO", tbUsuario.getCodigo () );
//        hashMap.put ( "USUARIO", tbUsuario.getNome () );
//        hashMap.put ( "FK_ARMAZEM", armazemDao.getArmazemByDescricao ( ( String ) armazemJCombobox.getSelectedItem () ).getCodigo () );
//        hashMap.put ( "FK_CLIENTE", clienteDao.getClienteByNome ( ( String ) clientesJCombobox.getSelectedItem () ).getCodigo () );
//        hashMap.put ( "DATA_INICIO", date1 );
//        hashMap.put ( "DATA_FIM", date2 );
//        hashMap.put ("TAXA", faturaJSpinner.getValue () );
//        hashMap.put ( "TIPO_DOC", getSelectedBonusClienteDoc () );
//
//        String relatorio = "relatorios/bonus_cliente_relatotorio.jasper";
//
//        File file = new File ( relatorio ).getAbsoluteFile ();
//        String obterCaminho = file.getAbsolutePath ();
//
//        try
//        {
//            Connection connection = BDConexao.getConnection ();
//            JasperFillManager.fillReport ( obterCaminho, hashMap, connection );
//            JasperPrint jasperPrint = JasperFillManager.fillReport ( obterCaminho, hashMap, connection );
//
//            if ( jasperPrint.getPages ().size () >= 1 )
//            {
//                JasperViewer jasperViewer = new JasperViewer ( jasperPrint, false );
//                jasperViewer.setVisible ( true );
//            }
//            else
//            {
//                JOptionPane.showMessageDialog ( null, "Não existe registro para esse intervalo de data!...", "ERROR", JOptionPane.ERROR_MESSAGE );
//            }
//
//        }
//        catch ( JRException jex )
//        {
//            jex.printStackTrace ();
//            JOptionPane.showMessageDialog ( null, "Falha ao mostrar o relatorio", "DVML-COMERCIAL, LDA", JOptionPane.ERROR );
//        }
//        catch ( Exception ex )
//        {
//            ex.printStackTrace ();
//            JOptionPane.showMessageDialog ( null, "Erro ao tentar mostrar o relatorio", "DVML-COMERCIAL, LDA", JOptionPane.ERROR_MESSAGE );
//        }
    }

    private static void inicializarComponentes()
    {

        //<editor-fold defaultstate="collapsed" desc=" ÁREA DE CRIAÇÃO E INICIALIZAÇÃO DAS VARIÁVEL">        
        System.err.println( "" );
        int colIndex = 0;
//        double percentBonus = ( double ) faturaJSpinner.getValue();
        final int fk_produto = colIndex++;
        final int designacao_produto = colIndex++;
        final int quantidade = colIndex++;
        final int preco_fabrica = colIndex++;
        final int fk_cliente = colIndex++;
        final int nome_cliente = colIndex++;
        final int data = colIndex++;
        final int fk_armazem = colIndex++;
        final int armazem = colIndex++;
        final int fk_documento = colIndex++;
        final int abreviacao_documento = colIndex++;
        final int codigo_venda = colIndex++;
        final int cod_fact = colIndex++;
        final int sub_total = colIndex++;
        final int bonus = colIndex++;
        //</editor-fold>

        armazemJCombobox.setModel( criarArmazemQueVenderamAPrazoJComboboxModel() );

        armazemJCombobox.addItemListener( new ItemListener()
        {
            @Override
            public void itemStateChanged( ItemEvent e )
            {
                clientesJCombobox.setModel( criarClientesQueCompraramAPrazoNoArmazemJComboboxModel( (String) e.getItem() ) );
                atualizarTabela();
            }
        } );

        clientesJCombobox.setModel( criarClientesQueCompraramAPrazoNoArmazemJComboboxModel( (String) armazemJCombobox.getSelectedItem() ) );
        clientesJCombobox.addItemListener( new ItemListener()
        {
            @Override
            public void itemStateChanged( ItemEvent e )
            {
                try
                {
                    faturaJSpinner.setModel( criarFaturasAPrazoSpinnerListModel( (String) e.getItem() ) );
                }
                catch ( Exception ex )
                {
                    ex.printStackTrace();
                }

                atualizarTabela();
            }

        } );

        try
        {
            faturaJSpinner.setModel( criarFaturasAPrazoSpinnerListModel( (String) clientesJCombobox.getSelectedItem() ) );

        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }

        faturaJSpinner.addChangeListener( new ChangeListener()
        {
            @Override
            public void stateChanged( ChangeEvent e )
            {
                atualizarTabela();
            }
        } );

//        formaPagamentoJCombobox.setModel( new DefaultComboBoxModel( ( Vector ) BancoDao.buscaTodos() ) );
//        formaPagamentoJCombobox.setSelectedIndex( -1 );
//        resizeJButtonIcon( 32, 32, getClass().getResource( "/imagens/bonus/icons8_print_200px.png" ), imprimirJButton );
//        resizeJButtonIcon( 32, 32, getClass().getResource( "/imagens/bonus/icons8_details_popup_128px.png" ), detalhesJButton );
//        resizeJButtonIcon( 32, 32, getClass().getResource( "/imagens/2934_32x32.png" ), fecharDetalhesBonusJButton );
//        resizeJButtonIcon( 32, 32, getClass().getResource( "/imagens/bonus/icons8_save_close_200px.png" ), salvarJButton );
    }

    public static SpinnerModel criarFaturasAPrazoSpinnerListModel( String clienteSlecionado )
    {
        List<TbVenda> tbVendas = VendaDao.findFaturasCliente( clienteSlecionado, 2 );

        ArrayList<String> arrayList = new ArrayList<>();
        Vector vector = new Vector();

        try
        {
            if ( !Objects.isNull( tbVendas ) )
            {
                for ( TbVenda tbVenda : tbVendas )
                {
                    System.err.println( "CodFact: " + tbVenda.getCodFact() );
                    arrayList.add( tbVenda.getCodFact() );
                    vector.add( tbVenda.getCodFact() );
                }
            }
            else
            {
                System.err.println( "CHEGUEI AQUI" );
                arrayList.add( "N/A" );
            }

//        cmbFacturaPorLiquidar.setModel( new DefaultComboBoxModel<>( vector ) );
            return criarSpinnerListModel( arrayList.toArray() );

        }
        catch ( Exception e )
        {
        }
        return null;

    }

    public static void salvar( double valor_entregue )
    {
//        DocumentoDao.startTransaction( conexao );
        System.out.println( "VALOR ENTREGUE: " + valor_entregue );

        String ref_doc = (String) faturaJSpinner.getValue();
        TbVenda facturaDeReferencia = VendaDao.findByCodFact( ref_doc );
        Date data_actual = new Date();
        Date documento_ref_data = facturaDeReferencia.getDataVenda();
        if ( documento_ref_data != null )
        {
            if ( MetodosUtil.menor_data_1_data_2( data_actual, documento_ref_data ) )
            {
                JOptionPane.showMessageDialog( null, "Caro usário verifique a data do sistema", "AVISO", JOptionPane.WARNING_MESSAGE );
            }
            else
            {

                System.out.println( "STATUS: a processar a factura" );

                boolean vendaBemSucedida = salvar_venda( valor_entregue );

//                MetodosUtil.adicionar_saldo_banco( facturaDeReferencia.getTotalVenda(), facturaDeReferencia.getIdBanco().getIdBanco(), conexao );
                if ( vendaBemSucedida )
                {
                    System.out.println( "" );
                    Double totalAmortizado = AmortizacaoDividaDao.findTotalAmortizadoByRefDoc( (String) faturaJSpinner.getValue() );
                    Double valorEntregue = valor_entregue;
                    Double totalAPagar = totalProcessado - totalAmortizado;
                    Double troco = ( valorEntregue - totalAPagar ) > 0 ? valorEntregue - totalAPagar : 0;

                    AmortizacaoDivida amortizacaoDivida = new AmortizacaoDivida();
                    amortizacaoDivida.setData( new Date() );
                    amortizacaoDivida.setDesconto( 0.0 );
                    amortizacaoDivida.setFkUsuario( tbUsuario );
                    amortizacaoDivida.setFkVenda( facturaDeReferencia );
                    amortizacaoDivida.setObs( "" );
                    amortizacaoDivida.setTroco( troco );
                    amortizacaoDivida.setValorEntregue( valorEntregue );
                    amortizacaoDivida.setValorPendente( totalProcessado - ( ( valorEntregue - troco ) + totalAmortizado ) );
                    new AmortizacaoDividaDao().create( amortizacaoDivida );
                    inicializarComponentes();
                    atualizarTabela();
                }

                System.out.println( "STATUS: fim do processamento da factura" );
                limpar();

            }
        }
        atualizarTabela();

    }

    private static void atualizarResumo()
    {
        String faturaSlecionada = (String) faturaJSpinner.getValue();

        Double totalAmortizado = AmortizacaoDividaDao.findTotalAmortizadoByRefDoc( (String) faturaJSpinner.getValue() );
        Double totalRetencao = VendaDao.getTotalRetencaoByCodFact( (String) faturaJSpinner.getValue(), conexao );
        System.out.println( "FACTURA:" + (String) faturaJSpinner.getValue() );
        Double total_retencao = totalRetencao;
        Double valorEntregue = totalProcessado;
        Double totalAPagar = (totalProcessado - total_retencao) - totalAmortizado;
        Double troco = ( valorEntregue - totalAPagar ) > 0 ? valorEntregue - totalAPagar : 0;

//        trocoJTextField.setText( CfMethods.formatarComoMoeda( troco ) );
        totalPagoJTextField.setText( CfMethods.formatarComoMoeda( totalAmortizado ) );
        System.out.println( "TOTAL_AMORTIZADO_VERIFICACAO:" + totalAmortizado );
        System.out.println( "TOTAL_PROCESSADO_VERIFICACAO:" + totalProcessado );
        valorTotalFaturaJTextField.setText( CfMethods.formatarComoMoeda( totalProcessado  ) );
        totalRetencaoJTextField.setText( CfMethods.formatarComoMoeda( total_retencao ) );
        totalAPagarJTextField.setText( CfMethods.formatarComoMoeda( totalAPagar ) );
//        valorEntregueJSpinner.setModel( criarSpinnerDoubleModel( 0, totalAPagar, valorEntregue ) );

    }

    private static void atualizarResumoCombo()
    {
//        String faturaSlecionada = ( String ) faturaJSpinner.getValue();
//        Double totalAmortizado = AmortizacaoDividaDao.findTotalAmortizadoByRefDoc( ( String ) cmbFacturaPorLiquidar.getSelectedItem() );
//        Double valorEntregue = totalProcessado;
//        Double totalAPagar = totalProcessado - totalAmortizado;
//        Double troco = ( valorEntregue - totalAPagar ) > 0 ? valorEntregue - totalAPagar : 0;
////        trocoJTextField.setText( CfMethods.formatarComoMoeda( troco ) );
//        totalPagoJTextField.setText( CfMethods.formatarComoMoeda( totalAmortizado ) );
//        valorTotalFaturaJTextField.setText( CfMethods.formatarComoMoeda( totalProcessado ) );
//        totalAPagarJTextField.setText( CfMethods.formatarComoMoeda( totalAPagar ) );
//        valorEntregueJSpinner.setModel( criarSpinnerDoubleModel( 0, totalAPagar, valorEntregue ) );

    }

    public double getPreco( int cod_produto, int qtd )
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

    private static double getValorIVA( double taxa, double preco_venda )
    {
        return ( ( ( taxa / 100 ) + 1 ) * preco_venda );
    }

    private static String getValor_por_extenco( Double valor )
    {
        System.out.println( "Valor XXXXXXX: " + valor );
//        lbValorPorExtenco.setText ( MetodosUtil.valorPorExtenso ( totalAmortizado, "Kwanza" ) );

        return MetodosUtil.valorPorExtenso( valor, "Kwanza" );
    }

    private void valor_por_extenco()
    {

        Double totalAmortizado = AmortizacaoDividaDao.findTotalAmortizadoByRefDoc( (String) faturaJSpinner.getValue() );

        System.out.println( "Valor XXXXXXX: " + totalAmortizado );
//        lbValorPorExtenco.setText ( MetodosUtil.valorPorExtenso ( totalAmortizado, "Kwanza" ) );
    }

    public static boolean salvar_venda( double valor_entregue )
    {
        DocumentoDao.startTransaction( conexao );

        Date data_actual = new Date();
        Double totalAmortizado = AmortizacaoDividaDao.findTotalAmortizadoByRefDoc( (String) faturaJSpinner.getValue() );
        Double valorEntregue = valor_entregue;
        Double totalAPagar = valor_entregue - totalAmortizado;
//        Double troco = ( valorEntregue - totalAPagar ) > 0 ? valorEntregue - totalAPagar : 0;
        Double troco = 0d;

        String ref_doc = (String) faturaJSpinner.getValue();
        TbVenda factura = VendaDao.findByCodFact( ref_doc );

        TbVenda recibo = new TbVenda();

        recibo.setDataVenda( data_actual );
        recibo.setHora( data_actual );
        recibo.setNomeCliente( factura.getNomeCliente() );
        recibo.setObs( taObs.getText() );
        recibo.setClienteNif( factura.getClienteNif() );

        //Total Ilíquido
        recibo.setTotalGeral( factura.getTotalGeral() );
        //desconto por linha
        recibo.setDescontoComercial( factura.getDescontoComercial() );
        //imposto
        //calculaTotalIVA();
        recibo.setTotalIva( factura.getTotalIva() );
        //desconto global
        recibo.setDescontoFinanceiro( factura.getDescontoFinanceiro() );
        //Total(AOA) <=> Total Líquido
        recibo.setTotalVenda( factura.getTotalVenda() );
        
        recibo.setTotalRetencao(factura.getTotalRetencao() );

        //#MONTANTE
        System.err.println( "valorEntregue: " + valorEntregue );
        System.err.println( "troco: " + troco );
        recibo.setValorEntregue( new BigDecimal( valorEntregue - troco ) );
        recibo.setTroco( new BigDecimal( troco ) );

        recibo.setTotalIncidencia( factura.getTotalIncidencia() );
        recibo.setTotalIncidenciaIsento( factura.getTotalIncidenciaIsento() );
//        recibo.setRefFactData( factura.getDataVenda() );
        //venda_local.setRefDataFact(dc_data_factura_recibo.getDate() );
        recibo.setRefDataFact( factura.getDataVenda() );
        recibo.setDataVencimento( data_actual );

        /*outros campos*/
        recibo.setDescontoTotal( new BigDecimal( factura.getDescontoComercial().doubleValue() + factura.getDescontoFinanceiro().doubleValue() ) );
        recibo.setIdArmazemFK( armazemDao.findTbArmazem( factura.getIdArmazemFK().getCodigo() ) );
        recibo.setCodigoUsuario( tbUsuario );
        recibo.setCodigoCliente( factura.getCodigoCliente() );
        recibo.setFkAnoEconomico( getAnoEconomicoActual() );
//        venda_local.setFkAnoEconomico( tbVenda.getFkAnoEconomico() );

        recibo.setFkDocumento( getDocumento() );
        recibo.setCodFact( getProximoDoc( factura ) );
        recibo.setRefCodFact( (String) faturaJSpinner.getValue() );

//#HASH_TESTE        
        recibo.setHashCod( MetodosUtil.criptografia_hash( recibo, getGrossTotal(), conexao ) );
//        recibo.setHashCod( MetodosUtil.criptografia_hash( prox_doc ) );
//        venda_local.setHashCod( MetodosUtil.criptografia_hash( prox_doc ) );
        recibo.setTotalPorExtenso( "Recebemos a quantia de: " + getValor_por_extenco( valorEntregue - troco ) );
        //System.out.println( "STATUS:hash cod processado." );
        recibo.setAssinatura( MetodosUtil.assinatura_doc( recibo.getHashCod() ) );
        //System.out.println( "STATUS:documento assinado com sucesso." );

        recibo.setFkCambio( factura.getFkCambio() );


        /*status documento*/
        recibo.setStatusEliminado( "false" );
        recibo.setPerformance( "false" );
        recibo.setCredito( "false" );
        recibo.setGorjeta( new BigDecimal( gorjeta ) );

        try
        {
            Integer last_venda = VendaDao.criarVendaComProcedu( recibo, conexao );
//                            int last_id_item_venda = itemVendaDao.criarComProcedimentos( itemVenda, conexao );

            if ( Objects.isNull(last_venda)||last_venda == 0)
            {
                DocumentoDao.rollBackTransaction( conexao );
                return false;
            }
            System.err.println( "last_venda: " + last_venda );
            System.out.println( "STATUS:factura criada com sucesso." );
            DocumentoDao.commitTransaction( conexao );

//            Integer last_venda = VendaDao.criarVendaComProcedu( recibo, conexao );
            System.out.println( "STATUS:factura criada com sucesso." );

            if ( last_venda != null )
            {
                if( !vendaDao.existeItensVenda( last_venda, conexao ))
                {
                salvarItemvenda( last_venda );
                }
                else
                    {
                        System.out.println( "ERRO: Ja existe venda relacionada." );
                    }

            }
            System.out.println( "STATUS:itens adicionado na facrtura com sucesso." );
        }
        catch ( Exception e )
        {
            System.err.println( "STATUS: falha ao actualizar a factura" );
            e.printStackTrace();
            JOptionPane.showMessageDialog( null, "Falha ao Processar a Factura", "FALHA", JOptionPane.ERROR_MESSAGE );
            DocumentoDao.rollBackTransaction( conexao );

            return false;
        }

        return true;

    }

    private static Documento getDocumento()
    {
        return new DocumentoDao().findDocumento( DOC_RECIBO_RC );
    }

    private void mostrar_proximo_codigo_documento()
    {
        try
        {
            Documento documento = getDocumento();
            int doc_prox_cod = documento.getCodUltimoDoc() + 1;
            //prox_doc = " " + documento.getAbreviacao();
            String prox_doc = documento.getAbreviacao();
            //FA Série / codigo
            prox_doc += " " + getAnoEconomicoSerie().getSerie() + "/" + doc_prox_cod;

            tituloJLabel.setText( prox_doc );
            proximoDocJLabel.setText( "PRÓXIMO RECIBO. :" + prox_doc );
        }
        catch ( Exception e )
        {
            proximoDocJLabel.setText( "" );
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

    private static String getProximoDoc( TbVenda factura )
    {

        try
        {
            Documento documento = getDocumento();
            //AnoEconomico anoEconomico = anoEconomicoDao.findAnoEconomico( factura.getFkAnoEconomico().getPkAnoEconomico() );
            AnoEconomico anoEconomico = anoEconomicoDao.getLastObject();
            // this.doc_prox_cod = documento.getCodUltimoDoc() + 1;
            int doc_prox_cod = vendaDao.getUltimaContagemByIdDocumentoAndAnoEconomico( documento.getPkDocumento(), factura.getFkAnoEconomico().getPkAnoEconomico(), conexao ) + 1;
            prox_doc = documento.getAbreviacao();
            //FA Série / codigo
            prox_doc += " " + anoEconomico.getSerie() + "/" + doc_prox_cod;
            return prox_doc;
        }
        catch ( Exception e )
        {
            return "";
        }

//        try
//        {
//            Documento documento = getDocumento();
//            int doc_prox_cod = documento.getCodUltimoDoc() + 1;
//            //prox_doc = " " + documento.getAbreviacao();
//            String prox_doc = documento.getAbreviacao();
//            //FA Série / codigo
//            prox_doc += " " + getAnoEconomicoSerie().getSerie() + "/" + doc_prox_cod;
//
//            return prox_doc;
//
//        }
//        catch ( Exception e )
//        {
//            return null;
//        }
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

    private static double getGrossTotal()
    {
        System.out.println( "TOTALILIQUIDO: " + getTotalVendaIVASemIncluirDesconto() );
        System.out.println( "TOTALVENDAIVASEMDESCONTO: " + getTotalVendaIVASemIncluirDesconto() );
        return getTotalIliquido() + getTotalVendaIVASemIncluirDesconto();
    }

    private static double getTotalVendaIVASemIncluirDesconto()
    {
        double taxa = 0, total_iva_local = 0, preco_unitario = 0, sub_total_iliquido = 0;
        double qtd = 0;

        DefaultTableModel modelo = (DefaultTableModel) detalhesJTable.getModel();
        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {

            /*
                "Cod. Prod",
            "Produto",
            "Cod. Isensao",
            "Qauntidade",
            "Preco Unit",
            "Desconto",
            "Taxa Iva",
            "Subtotal"
             */
//            preco_unitario = CfMethods.parseMoedaFormatada( modelo.getValueAt( i, 4 ).toString() );
            preco_unitario = Double.parseDouble( modelo.getValueAt( i, 4 ).toString() );
//            qtd = Integer.parseInt( modelo.getValueAt( i, 3 ).toString() );
            qtd = Double.parseDouble( modelo.getValueAt( i, 3 ).toString() );
            sub_total_iliquido = ( preco_unitario * qtd );
            taxa = Double.parseDouble( modelo.getValueAt( i, 6 ).toString() );
            total_iva_local += ( ( ( sub_total_iliquido ) * ( taxa / 100 ) ) );
        }

        return total_iva_local;
    }

    private static double getTotalIliquido()
    {
        DefaultTableModel modelo = (DefaultTableModel) detalhesJTable.getModel();
        double qtd = 0;
        double total_iliquido = 0, preco_unitario = 0;

        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {
//            preco_unitario = CfMethods.parseMoedaFormatada( modelo.getValueAt( i, 4 ).toString() );
            preco_unitario = Double.parseDouble( modelo.getValueAt( i, 4 ).toString() );
            qtd = Double.parseDouble( modelo.getValueAt( i, 3 ).toString() );
            total_iliquido += ( preco_unitario * qtd );

        }

        return total_iliquido;
    }

    public static void salvarItemvenda( Integer last_cod )
    {
        int cod_venda = last_cod;
        boolean efectuada = true;
        TbVenda venda = vendaDao.findTbVenda( cod_venda );
        TbItemVenda itemVenda = null;
        for ( int i = 0; i < detalhesJTable.getRowCount(); i++ )
        {
            try
            {
                itemVenda = new TbItemVenda();
                System.out.println( "CODIGO PRODUTO: " + String.valueOf( detalhesJTable.getModel().getValueAt( i, 0 ) ) );
                System.out.println( "QTD: " + String.valueOf( detalhesJTable.getModel().getValueAt( i, 3 ) ) );

                itemVenda.setCodigoProduto( produtoDao.findTbProduto( Integer.parseInt( String.valueOf( detalhesJTable.getModel().getValueAt( i, 0 ) ) ) ) );
                itemVenda.setCodigoVenda( venda );
                itemVenda.setQuantidade( Double.parseDouble( String.valueOf( detalhesJTable.getModel().getValueAt( i, 3 ) ) ) );
//                itemVenda.setDesconto( parseMoedaFormatada( String.valueOf( detalhesJTable.getModel().getValueAt( i, 4 ) ) ) );
                itemVenda.setDesconto( Double.parseDouble( detalhesJTable.getModel().getValueAt( i, 4 ).toString() ) );
                itemVenda.setValorIva( parseMoedaFormatada( String.valueOf( detalhesJTable.getModel().getValueAt( i, 5 ) ) ) );
                itemVenda.setMotivoIsensao( getMotivoIsensao( itemVenda.getCodigoProduto().getCodigo() ) );
                itemVenda.setCodigoIsensao( MetodosUtil.getCodigoRegime( itemVenda.getCodigoProduto().getCodigo() ) );
                itemVenda.setTotal( new BigDecimal( parseMoedaFormatada( String.valueOf( detalhesJTable.getModel().getValueAt( i, 7 ) ) ) ) );
                itemVenda.setFkPreco( precoDao.findTbPreco( precoDao.getUltimoIdPrecoByIdProduto( itemVenda.getCodigoProduto().getCodigo(), itemVenda.getQuantidade() ) ) );

                itemVenda.setDataServico( new Date() );

//                itemVenda.setFkUsuario( tbUsuario );
                itemVenda.setFkLugares( new LugarDao().findTbLugares( DVML.LUGAR_BALCAO ) );
                itemVenda.setFkMesas( new MesasDao().findTbMesas( DVML.MESA_BALCAO ) );
                //cria o item venda
//                itemVendaDao.create( itemVenda );
//                itemVendaDao.criarComProcedimentos( itemVenda, conexao );
                int last_id_item_venda = itemVendaDao.criarComProcedimentos( itemVenda, conexao );

                if ( Objects.isNull( last_id_item_venda ) || last_id_item_venda == 0 )
                {
                    DocumentoDao.rollBackTransaction( conexao );
                    return;
                }

            }
            catch ( Exception e )
            {
                e.printStackTrace();
                efectuada = false;
                JOptionPane.showMessageDialog( null, "Falha ao registrar o produto: " + itemVenda.getCodigoProduto().getCodigo() + " na Factura" );
                DocumentoDao.rollBackTransaction( conexao );
                break;
            }
        }

        if ( efectuada )
        {
            List<TbProduto> lista_produto_isentos = new ArrayList<>();
            lista_produto_isentos = getProdutosIsentos();
            String motivos_isentos = MetodosUtil.getMotivoIsensaoProdutos( lista_produto_isentos );
            System.err.println( "MOTIVOS: " + motivos_isentos );
            try
            {
                registrar_forma_pagamento( cod_venda );


//                limpar ();
                remover_all_produto();
                //adicionar_preco_quantidade_anitgo();
                String ref_doc = (String) faturaJSpinner.getValue();
                TbVenda factura = VendaDao.findByCodFact( ref_doc );
//                factura.setStatusRecibo( true );
                factura.setStatusRecibo( new Boolean( "1" ) );
                vendaDao.edit( factura );
                                
                DocumentoDao.commitTransaction( conexao );

            }
            catch ( Exception e )
            {
            }
            gorjeta = 0;
//HERE
//            txtClienteNome.setText ( "" );
            // txtClienteNome.requestFocus();
            //Chama a factura e imprime directamente para a imprissora que estiver devenidade no sistema operativo
            ListaVenda1 listaVenda = new ListaVenda1( cod_venda, DVML.Abreviacao.RC, false, false, "Original", motivos_isentos );
            //ListaVenda1 listaVenda2 = new ListaVenda1(cod_venda, false, ck_simplificada.isSelected());

            //Em caso em que a impreensão é dupla
            //ListaVendaDuplicado listaVenda1 = new ListaVendaDuplicado(cod_venda, setPeformance(), ckImpreesao.isSelected());
        }
    }

    public static void remover_all_produto() throws SQLException
    {

        DefaultTableModel modelo = (DefaultTableModel) detalhesJTable.getModel();

        for ( int i = modelo.getRowCount() - 1; i >= 0; i-- )
        {
            modelo.removeRow( i );
        }

    }

    private static String getMotivoIsensao( int idProduto )
    {
        try
        {
            return new ProdutoIsentoDao().getRegimeIsensaoByIdProduto( idProduto );

        }
        catch ( Exception e )
        {
            return "";
        }
    }

    private static List<TbProduto> getProdutosIsentos()
    {
        DefaultTableModel modelo = (DefaultTableModel) detalhesJTable.getModel();
        double taxa = 0.0;
        int codigo_produto = 0;
        List<TbProduto> lista_produtos_isentos = new ArrayList<>();
        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {
            codigo_produto = Integer.parseInt( modelo.getValueAt( i, 0 ).toString() );
            taxa = parseMoedaFormatada( modelo.getValueAt( i, 5 ).toString() );
            if ( taxa == 0.0 )
            {
                lista_produtos_isentos.add( produtoDao.findTbProduto( codigo_produto ) );
            }
        }

        return lista_produtos_isentos;

    }

    private static void limpar()
    {
        taObs.setText( "" );
    }

    private static boolean camposFormularioDevidamentePreenchidos()
    {
        String faturaSlecionada = (String) faturaJSpinner.getValue();
        Double totalAmortizado = AmortizacaoDividaDao.findTotalAmortizadoByRefDoc( (String) faturaJSpinner.getValue() );
        Double valorEntregue = totalProcessado;
        System.out.println( "VALOR ENTREGUE: " + valorEntregue );
        Double totalAPagar = totalProcessado - totalAmortizado;
        Double troco = ( valorEntregue - totalAPagar ) > 0 ? valorEntregue - totalAPagar : 0;
        boolean clienteEntregouValo = valorEntregue > 0;
        boolean faturaSelecionada = !Objects.isNull( faturaSlecionada ) && !faturaSlecionada.isEmpty();

        return clienteEntregouValo && faturaSelecionada;

    }

    private Integer getIdFormaPagamento()
    {
//        return bancoDao.getIdByDescricao( formaPagamento );
        return 0;

    }

    private static AnoEconomico getAnoEconomicoActual()
    {

        List<AnoEconomico> list_ano_economico = anoEconomicoDao.buscaTodosObject();
        return list_ano_economico.get( 0 );

    }

    private void finalizar_recibo()
    {
        if ( JOptionPane.showConfirmDialog( null, "Caro usuario este processo e irreversivel, deseja continuar?" ) == JOptionPane.YES_OPTION )
        {
            new FormaPagamentoVisao( this, rootPaneCheckingEnabled, emf, DVML.EMISSAO_RECIBOS, conexao ).setVisible( true );

        }
    }

    public static boolean registrar_forma_pagamento( int id_venda )
    {

        DefaultTableModel modelo = (DefaultTableModel) FormaPagamentoVisao.tabela_forma_pagamento.getModel();

        FormaPagamentoItem formaPagamentoItem;
        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {
            formaPagamentoItem = new FormaPagamentoItem();
            Integer id_banco = Integer.parseInt( modelo.getValueAt( i, 0 ).toString() );
            String referencia = ( modelo.getValueAt( i, 2 ) != null ) ? modelo.getValueAt( i, 2 ).toString() : "n/a";
            String valor = ( modelo.getValueAt( i, 3 ) != null ) ? modelo.getValueAt( i, 3 ).toString() : "0";

            formaPagamentoItem.setValor( new BigDecimal( valor ) );
            formaPagamentoItem.setReferencia( referencia );
            formaPagamentoItem.setFkFormaPagamento( new FormaPagamento( id_banco ) );
            formaPagamentoItem.setFkVenda( new TbVenda( id_venda ) );

            try
            {
                if ( !valor.equals( "0" ) )
                {
                    FormaPagamentoItemDao.insert( formaPagamentoItem, conexao );
                }
            }
            catch ( Exception e )
            {
                return false;
            }

        }

        return true;

    }

}
