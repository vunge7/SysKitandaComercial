/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lista;


import java.sql.Connection;
import dao.AccessoArmazemDao;
import dao.ArmazemDao;
import dao.FormaPagamentoDao;
import dao.ItemVendaDao;
import dao.PrecoDao;
import dao.UsuarioDao;
import dao.VendaDao;
import entity.TbVenda;
import kitanda.controller.ClienteController;
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
import util.BDConexao;
import util.DVML;
import util.JPAEntityMannagerFactoryUtil;

/**
 *
 * @author Domingos Dala Vunge
 */
public class ListarRelatorioFormaPagamento extends javax.swing.JFrame
{

    /**
     * Creates new form ListaUsuarioVisao
     */
    private EntityManagerFactory emf = JPAEntityMannagerFactoryUtil.em;
    private VendaDao vendaDao = new VendaDao( emf );
    private AccessoArmazemDao accessoArmazemDao = new AccessoArmazemDao( emf );
    private UsuarioDao usuarioDao = new UsuarioDao( emf );
    private ItemVendaDao itemVendaDao = new ItemVendaDao( emf );
    private PrecoDao precoDao = new PrecoDao( emf );
    private ArmazemDao armazemDao = new ArmazemDao( emf );
    private FormaPagamentoDao formaPagamentoDao = new FormaPagamentoDao( emf );
    private double total_geral = 0;
    private List<TbVenda> lista = null;
    private BDConexao conexao;
    private ClienteController clienteController;
    private Vector<String> lista_all_clientes;
    private int idUser;

    public ListarRelatorioFormaPagamento( BDConexao conexao, int idUser )
    {

        initComponents();
        setResizable( false );
        setLocationRelativeTo( null );
        this.conexao = conexao;
        this.idUser = idUser;
        dcDataInicio.setDate( new Date() );
        dcDataFim.setDate( new Date() );
        cmbFormaPagamento.setVisible( false );
        jLabel3.setVisible( false );
        cmbFormaPagamento.setModel( new DefaultComboBoxModel( formaPagamentoDao.getAllFormasPagto() ) );
        cmbArmazem.setModel( new DefaultComboBoxModel( accessoArmazemDao.getAllArmazemByIdUSuario( idUser ) ) );
//        configurar_clientes();
//        metodo_radio();

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        lbData = new javax.swing.JLabel();
        dcDataInicio = new com.toedter.calendar.JDateChooser();
        lbData1 = new javax.swing.JLabel();
        dcDataFim = new com.toedter.calendar.JDateChooser();
        jLabel2 = new javax.swing.JLabel();
        cmbArmazem = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        cmbFormaPagamento = new javax.swing.JComboBox<>();
        rbArmazem1 = new javax.swing.JRadioButton();
        rbArmazem = new javax.swing.JRadioButton();
        lbData2 = new javax.swing.JLabel();
        btnCancelar = new javax.swing.JButton();
        btnCancelar1 = new javax.swing.JButton();
        rbTodasFormas = new javax.swing.JRadioButton();
        rbSelecionarFormas = new javax.swing.JRadioButton();
        jLabel4 = new javax.swing.JLabel();
        rbA4 = new javax.swing.JRadioButton();
        rbA6 = new javax.swing.JRadioButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("...:::: DVML - RELATORIO DIARIO::...");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        lbData.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 16)); // NOI18N
        lbData.setText("De");

        lbData1.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 16)); // NOI18N
        lbData1.setText("à");

        jLabel2.setText("Armazem");

        jLabel3.setText("Formas Pagamentos");

        cmbFormaPagamento.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbFormaPagamentoActionPerformed(evt);
            }
        });

        rbArmazem1.setEnabled(false);
        rbArmazem1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                rbArmazem1ActionPerformed(evt);
            }
        });

        rbArmazem.setSelected(true);
        rbArmazem.setEnabled(false);
        rbArmazem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                rbArmazemActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbFormaPagamento, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addComponent(lbData, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(dcDataInicio, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lbData1, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dcDataFim, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cmbArmazem, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(rbArmazem1)
                        .addGap(18, 18, 18)
                        .addComponent(rbArmazem)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(cmbFormaPagamento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(28, 28, 28))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(dcDataInicio, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lbData1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(dcDataFim, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lbData, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(1, 1, 1))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(rbArmazem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(rbArmazem1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                        .addComponent(cmbArmazem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        lbData2.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 36)); // NOI18N
        lbData2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbData2.setText("RELATÓRIO DE VENDAS POR FORMAS DE PAGAMENTOS");

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

        buttonGroup1.add(rbTodasFormas);
        rbTodasFormas.setSelected(true);
        rbTodasFormas.setText("Todas Formas");
        rbTodasFormas.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                rbTodasFormasActionPerformed(evt);
            }
        });

        buttonGroup1.add(rbSelecionarFormas);
        rbSelecionarFormas.setText("Selecionar Formas");
        rbSelecionarFormas.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                rbSelecionarFormasActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        jLabel4.setText("Forma de Imprensão");

        buttonGroup2.add(rbA4);
        rbA4.setFont(new java.awt.Font("Lucida Sans Typewriter", 0, 12)); // NOI18N
        rbA4.setSelected(true);
        rbA4.setText("A4");

        buttonGroup2.add(rbA6);
        rbA6.setFont(new java.awt.Font("Lucida Sans Typewriter", 0, 12)); // NOI18N
        rbA6.setText("A6");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbData2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(rbTodasFormas)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(rbSelecionarFormas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 146, Short.MAX_VALUE)
                            .addComponent(btnCancelar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnCancelar1)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(rbA4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(rbA6)))))
                .addGap(37, 37, 37))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(lbData2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(rbA6)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel4)
                        .addComponent(rbA4)))
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCancelar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(rbTodasFormas)
                    .addComponent(rbSelecionarFormas)
                    .addComponent(btnCancelar1))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCancelar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelar1ActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_btnCancelar1ActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed

        try
        {
            if ( rbA4.isSelected() )
            {

                procedimento_imprimir();

            }
            else
            {

                procedimento_imprimir_A6();

            }
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }


    }//GEN-LAST:event_btnCancelarActionPerformed

    private void rbSelecionarFormasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbSelecionarFormasActionPerformed
        metodo_radio();
    }//GEN-LAST:event_rbSelecionarFormasActionPerformed

    private void rbTodasFormasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbTodasFormasActionPerformed
        metodo_radio();
    }//GEN-LAST:event_rbTodasFormasActionPerformed

    private void cmbFormaPagamentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbFormaPagamentoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbFormaPagamentoActionPerformed

    private void rbArmazemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_rbArmazemActionPerformed
    {//GEN-HEADEREND:event_rbArmazemActionPerformed
        try
        {
//            configurar_armazens();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }//GEN-LAST:event_rbArmazemActionPerformed

    private void rbArmazem1ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_rbArmazem1ActionPerformed
    {//GEN-HEADEREND:event_rbArmazem1ActionPerformed
        try
        {
//            configurar_armazens();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }//GEN-LAST:event_rbArmazem1ActionPerformed

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
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater( new Runnable()
        {
            public void run()
            {
                try
                {
                    new ListarRelatorioFormaPagamento( BDConexao.getInstancia(), 15 ).setVisible( true );
                }
                catch ( Exception ex )
                {
                    Logger.getLogger( ListarRelatorioFormaPagamento.class.getName() ).log( Level.SEVERE, null, ex );
                }
            }
        } );
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnCancelar1;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JComboBox<String> cmbArmazem;
    private javax.swing.JComboBox<String> cmbFormaPagamento;
    private com.toedter.calendar.JDateChooser dcDataFim;
    private com.toedter.calendar.JDateChooser dcDataInicio;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lbData;
    private javax.swing.JLabel lbData1;
    private javax.swing.JLabel lbData2;
    public static javax.swing.JRadioButton rbA4;
    public static javax.swing.JRadioButton rbA6;
    private static javax.swing.JRadioButton rbArmazem;
    private static javax.swing.JRadioButton rbArmazem1;
    private javax.swing.JRadioButton rbSelecionarFormas;
    private javax.swing.JRadioButton rbTodasFormas;
    // End of variables declaration//GEN-END:variables

    public int getCodigoArmazem()
    {
        return armazemDao.getArmazemByDescricao( cmbArmazem.getSelectedItem().toString() ).getCodigo();
    }

    public int getCodigoFormaPagamento()
    {
        return formaPagamentoDao.getFormaPagamentoByDescricao( cmbFormaPagamento.getSelectedItem().toString() ).getPkFormaPagamento();
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

    private void procedimento_imprimir()
    {

        if ( rbTodasFormas.isSelected() )
        {
            ResumoFormaPagamento resumoFormaPagamento = new ResumoFormaPagamento( dcDataInicio.getDate(), dcDataFim.getDate(), getCodigoArmazem() );
        }
        else if ( rbSelecionarFormas.isSelected() )
        {
            ResumoFormaPagamento resumoFormaPagamento1 = new ResumoFormaPagamento( dcDataInicio.getDate(), dcDataFim.getDate(), getCodigoArmazem(), getCodigoFormaPagamento() );
        }

    }

    private void procedimento_imprimir_A6()
    {

        if ( rbTodasFormas.isSelected() )
        {
            ResumoFormaPagamento_A6 resumoFormaPagamento = new ResumoFormaPagamento_A6( dcDataInicio.getDate(), dcDataFim.getDate(), getCodigoArmazem() );
        }
        else if ( rbSelecionarFormas.isSelected() )
        {
            ResumoFormaPagamento_A6 resumoFormaPagamento1 = new ResumoFormaPagamento_A6( dcDataInicio.getDate(), dcDataFim.getDate(), getCodigoArmazem(), getCodigoFormaPagamento() );
        }

    }

//    private void procedimento_imprimir_A6() {
//
//        if (rbTodasFormas.isSelected()) {
//            ResumoFormaPagamento_A6 resumoFormaPagamento = new ResumoFormaPagamento_A6(dcDataInicio.getDate(), dcDataFim.getDate(), getCodigoArmazem());
//        } else if (rbSelecionarFormas.isSelected()) {
//            ResumoFormaPagamento_A6 resumoFormaPagamento1 = new ResumoFormaPagamento_A6(dcDataInicio.getDate(), dcDataFim.getDate(), getCodigoArmazem(), getCodigoFormaPagamento());
//        }
//
//    }
    private void metodo_radio()
    {
        if ( rbSelecionarFormas.isSelected() )
        {
//            cmbFormaPagamento.setModel( new DefaultComboBoxModel( lista_all_clientes ) );
            cmbFormaPagamento.setVisible( true );
            jLabel3.setVisible( true );

        }
        else
        {
            cmbFormaPagamento.setVisible( false );
            jLabel3.setVisible( false );
        }
//        adicionar_relatorio_factura();
    }

//    public void configurar_armazens()
//    {
//        setArmazem( dadosInstituicaoDao.findTbDadosInstituicao( 1 ).getConfigArmazens() );
//        try
//        {
//            if ( !rbArmazem.isSelected() )
//            {
//                //                Caso for MultiArmazens
//                cmbArmazem.setModel( new DefaultComboBoxModel( armazemDao.buscaTodos2() ) );
////                cmbArmazem.setModel( new DefaultComboBoxModel( accessoArmazemDao.getAllArmazemByIdUSuario( cod_usuario ) ) );
//
//            }
//            else if ( rbArmazem.isSelected() )
//            {
//                //                Caso for apenas Um Armazem
//                cmbArmazem.setModel( new DefaultComboBoxModel( accessoArmazemDao.getAllArmazemExceptoEconomatoByIdUSuario( idUser
//                ) ) );
//
//            }
//        }
//        catch ( Exception e )
//        {
//            e.printStackTrace();
//        }
//    }
}
