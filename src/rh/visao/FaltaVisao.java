/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

 /*
 * ProvinciaVisao.java
 *
 * Created on 27/12/2013, 12:24:14
 */
package rh.visao;


import java.sql.Connection;
import dao.EmpresaDao;
import dao.FaltaDao;
import dao.FuncionarioDao;
import entity.TbFalta;
import entity.TbFuncionario;
import java.awt.HeadlessException;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import javax.persistence.EntityManagerFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import kitanda.util.CfMethodsSwing;
import util.DVML;

import util.JPAEntityMannagerFactoryUtil;
import util.MetodosUtil;

/**
 * *
 * @author Domingos Dala Vunge
 */
public class FaltaVisao extends javax.swing.JFrame
{
    
    private EntityManagerFactory emf = JPAEntityMannagerFactoryUtil.em;
    private FuncionarioDao funcionarioDao = new FuncionarioDao( emf );
    private FaltaDao faltaDao = new FaltaDao( emf );
    private EmpresaDao empresaDao = new EmpresaDao( emf );
    private TbFalta falta;
    private static String DVML_COMERCIAL = "DVML-COMERCIA, Lda";
    private DefaultTableModel modelo;
    private int cod_falta = 0;
    private int linha_actual = -1;
    private int idUser = 0;
    private int idEmpresa = 0;
    
    public FaltaVisao( int idUser, int idEmpresa )
    {
        
        initComponents();
        this.idUser = idUser;
        this.idEmpresa = idEmpresa;
        setLocationRelativeTo( null );
        //txtFalta.setDocument( new PermitirNumeros() );
        txtIniciaisProfessores.addKeyListener( new TratarEventoTecladoProfessores() );
        dcDataFalta.setDate( new Date() );
        lbEmpresa.setText(  empresaDao.findEmpresa(idEmpresa).getNome()      );
        setComBoBox();
        jSpFalta.setModel( CfMethodsSwing.criarSpinnerDoubleModel( 0.0, 8, 0.0 ) );
        //mascara();
        try
        {
            busca_falta( getIdFuncionario() );
        }
        catch ( Exception e )
        {
        }
        
    }
    
    @SuppressWarnings( "unchecked" )
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        txtIniciaisProfessores = new javax.swing.JTextField();
        cmbFuncionario = new javax.swing.JComboBox();
        jLabel7 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtDescricao = new javax.swing.JTextArea();
        dcDataFalta = new com.toedter.calendar.JDateChooser();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jSpFalta = new javax.swing.JSpinner();
        jLabel11 = new javax.swing.JLabel();
        txtNumeroFuncionario = new javax.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jButton4 = new javax.swing.JButton();
        lbEmpresa = new javax.swing.JLabel();
        lb_seja_bem_vindo1 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Gestão de Faltas");

        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        txtIniciaisProfessores.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIniciaisProfessoresActionPerformed(evt);
            }
        });

        cmbFuncionario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbFuncionarioActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel7.setText("Iniciais Funcionario:");

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel6.setText("Tipo da Falta");

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel8.setText("Descrição  da Falta");

        txtDescricao.setColumns(20);
        txtDescricao.setRows(5);
        jScrollPane1.setViewportView(txtDescricao);

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel9.setText("Data da Falta");

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel10.setText("hora(s)");

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/salvar_16x16.png"))); // NOI18N
        jButton1.setText("salvar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/eliminar_16x16.png"))); // NOI18N
        jButton3.setText("eliminar");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel11.setText("Cod .Funcionário:");

        txtNumeroFuncionario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNumeroFuncionarioActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(cmbFuncionario, 0, 295, Short.MAX_VALUE)
                                .addComponent(txtIniciaisProfessores)
                                .addComponent(jLabel6)
                                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jSpFalta, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel10)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 72, Short.MAX_VALUE)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 379, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel9)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(dcDataFalta, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtNumeroFuncionario, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtNumeroFuncionario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtIniciaisProfessores, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel9))
                    .addComponent(dcDataFalta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(cmbFuncionario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jSpFalta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(8, Short.MAX_VALUE))
        );

        jPanel7.setBackground(new java.awt.Color(255, 153, 51));

        jLabel19.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N
        jLabel19.setText("MARCAÇÃO DE FALTAS");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 386, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, 65, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel1.setBackground(new java.awt.Color(43, 43, 43));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/LOGOUT - VERMELHO/Logout 16x16.png"))); // NOI18N
        jButton4.setText("sair");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        lbEmpresa.setFont(new java.awt.Font("Times New Roman", 3, 13)); // NOI18N
        lbEmpresa.setForeground(new java.awt.Color(255, 255, 255));
        lbEmpresa.setText("BEM VINDO!....");

        lb_seja_bem_vindo1.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        lb_seja_bem_vindo1.setForeground(new java.awt.Color(255, 255, 255));
        lb_seja_bem_vindo1.setText("EMPRESA:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lb_seja_bem_vindo1, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbEmpresa, javax.swing.GroupLayout.PREFERRED_SIZE, 304, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lbEmpresa, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lb_seja_bem_vindo1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTable1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Cod", "Professor", "Data/Hora", "Falta"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jTable1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jScrollPane2))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtIniciaisProfessoresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIniciaisProfessoresActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIniciaisProfessoresActionPerformed

    private void cmbFuncionarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbFuncionarioActionPerformed
        // TODO add your handling code here:
        busca_falta( getIdFuncionario() );

    }//GEN-LAST:event_cmbFuncionarioActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        procedimento_salvar();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        procedimento_elimniar();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        // TODO add your handling code here:
        if ( evt.getClickCount() >= 2 )
        {
            
            setDadosFaltaModelo();
            
        }
    }//GEN-LAST:event_jTable1MouseClicked

    private void txtNumeroFuncionarioActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtNumeroFuncionarioActionPerformed
    {//GEN-HEADEREND:event_txtNumeroFuncionarioActionPerformed
        // TODO add your handling code here:
        buscaFuncionario( true, false );
    }//GEN-LAST:event_txtNumeroFuncionarioActionPerformed

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
            java.util.logging.Logger.getLogger( FaltaVisao.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        catch ( InstantiationException ex )
        {
            java.util.logging.Logger.getLogger( FaltaVisao.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        catch ( IllegalAccessException ex )
        {
            java.util.logging.Logger.getLogger( FaltaVisao.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        catch ( javax.swing.UnsupportedLookAndFeelException ex )
        {
            java.util.logging.Logger.getLogger( FaltaVisao.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
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

        java.awt.EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                new FaltaVisao( 15, 1 ).setVisible( true );
            }
        } );
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox cmbFuncionario;
    private com.toedter.calendar.JDateChooser dcDataFalta;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSpinner jSpFalta;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel lbEmpresa;
    private javax.swing.JLabel lb_seja_bem_vindo1;
    private javax.swing.JTextArea txtDescricao;
    private javax.swing.JTextField txtIniciaisProfessores;
    private javax.swing.JTextField txtNumeroFuncionario;
    // End of variables declaration//GEN-END:variables

    public void setDadosFaltaModelo()
    {
        DefaultTableModel modeloLocal = ( DefaultTableModel ) jTable1.getModel();
        
        String professor = String.valueOf( modeloLocal.getValueAt( jTable1.getSelectedRow(), 1 ) );
        String faltaLocal = String.valueOf( modeloLocal.getValueAt( jTable1.getSelectedRow(), 3 ) );
        
        this.cod_falta = Integer.parseInt( String.valueOf( modeloLocal.getValueAt( jTable1.getSelectedRow(), 0 ) ) );
        txtDescricao.setText( faltaDao.findTbFalta( cod_falta ).getDescricaoFalta() );
        dcDataFalta.setDate( faltaDao.findTbFalta( cod_falta ).getData() );
        
        cmbFuncionario.setSelectedItem( professor );
        Double faltaDouble = Double.parseDouble( faltaLocal );
        jSpFalta.setValue( faltaDouble );
        
    }
    
    private void setComBoBox()
    {
        cmbFuncionario.setModel( new DefaultComboBoxModel( ( Vector ) funcionarioDao.buscaTodosNomeByIdEmpresaCombo(idEmpresa) ) );
    }
    
    public void limpar()
    {
        
        jSpFalta.setValue( 0d );
        txtDescricao.setText( "" );
        
    }
    
    private void esvaziar_tabela()
    {
        this.modelo = ( DefaultTableModel ) jTable1.getModel();
        this.modelo.setRowCount( 0 );
    }
    
    private void procedimento_salvar()
    {
        
        if ( ( Double ) jSpFalta.getValue() != 0d )
        {
            this.falta = new TbFalta();
            preparar_falta();
            
            try
            {
                salvar_falta();
                limpar();
                busca_falta( getIdFuncionario() );
                MetodosUtil.showMessageUtil( "Falta Efectuado Com Sucesso!...", DVML.TIPO_MENSAGEM_AVISO );
            }
            catch ( HeadlessException e )
            {
                MetodosUtil.showMessageUtil( "Falha ao Registrar a falta do Funcionário", DVML.TIPO_MENSAGEM_ERRO );
            }
        }
        else
        {
            MetodosUtil.showMessageUtil( "Pf. Insira as horas de falta", DVML.TIPO_MENSAGEM_AVISO );
            
        }
        
    }
    
    private void procedimento_elimniar()
    {
        if ( JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog( null, "Tens a Plena Certeza que Pretendes Eliminar Falta ?  " ) )
        {
            try
            {
                eliminar_falta();
                busca_falta( getIdFuncionario() );
                limpar();
                JOptionPane.showMessageDialog( null, "Registro Eliminado com Sucesso!...", DVML_COMERCIAL, JOptionPane.INFORMATION_MESSAGE );
            }
            catch ( HeadlessException e )
            {
                JOptionPane.showMessageDialog( null, "Falha ao Eliminar o Falta", DVML_COMERCIAL, JOptionPane.ERROR_MESSAGE );
            }
            
        }
        
    }
    
    private void salvar_falta()
    {
        faltaDao.create( this.falta );
    }
    
    private void alterar_falta()
    {
        try
        {
            faltaDao.edit( this.falta );
        }
        catch ( Exception e )
        {
        }
        
    }
    
    private void eliminar_falta()
    {
        try
        {
            faltaDao.destroy( this.cod_falta );
        }
        catch ( Exception e )
        {
        }
        
    }
    
    private void preparar_falta()
    {
        
        this.falta.setNFalta( ( Double ) jSpFalta.getValue() );
        this.falta.setDescricaoFalta( txtDescricao.getText() );
        this.falta.setData( dcDataFalta.getDate() );
        this.falta.setHora( dcDataFalta.getDate() );
//        this.falta.setJustificadaInjustificada( false );
        this.falta.setIdFuncionarioFK( funcionarioDao.findTbFuncionario( getIdFuncionario() ) );
        
    }
    
    private void buscaFuncionario( boolean b, boolean b0 )
    {
        String numeroFuncionario = txtNumeroFuncionario.getText();
        if ( !numeroFuncionario.equals( "" ) )
        {
            TbFuncionario funcionario = funcionarioDao.getFuncionarioByNumeroFuncionario( numeroFuncionario );
            busca_falta( funcionario.getIdFuncionario() );
        }
        
    }
    
    class TratarEventoTecladoProfessores implements KeyListener
    {
        
        String prefixo = "";
        
        public void keyPressed( KeyEvent evt )
        {
            
            if ( evt.getKeyCode() != KeyEvent.VK_BACK_SPACE && evt.getKeyCode() != KeyEvent.VK_ENTER )
            {
                char key = evt.getKeyChar();
                prefixo = txtIniciaisProfessores.getText().trim() + key;
                cmbFuncionario.setModel( new DefaultComboBoxModel( ( Vector ) funcionarioDao.getFuncionarioLIKENomeByIdEmpresa(prefixo, idEmpresa ) ) );
            }
            else if ( evt.getKeyCode() == KeyEvent.VK_BACK_SPACE )
            {
                try
                {
                    
                    prefixo = prefixo.toString().trim().substring( 0, prefixo.length() - 1 );
                    System.out.println( "NOME VOLTAR " + prefixo );
                    cmbFuncionario.setModel( new DefaultComboBoxModel( ( Vector ) funcionarioDao.getFuncionarioLIKENomeByIdEmpresa(prefixo, idEmpresa ) ) );
                    
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
    
    private void busca_falta( int idFuncionario )
    {
        
        List<TbFalta> itens = faltaDao.getAllFalta( idFuncionario );
        esvaziar_tabela();
        
        if ( itens != null )
        {
            for ( int i = 0; i < itens.size(); i++ )
            {
                
                this.modelo.addRow( new Object[]
                {
                    itens.get( i ).getIdFaltaPK(),
                    itens.get( i ).getIdFuncionarioFK().getNome(),
                    itens.get( i ).getData().getDate() + "/" + ( itens.get( i ).getData().getMonth() + 1 ) + "/" + ( 1900 + itens.get( i ).getData().getYear() )
                    + "  " + itens.get( i ).getHora().getHours() + ":" + itens.get( i ).getHora().getMinutes(),
                    itens.get( i ).getNFalta()
                } );
            }
        }
        
    }
    
    private int getIdFuncionario()
    {
        try
        {
            return funcionarioDao.getIdFuncionarioByNome( ( String ) cmbFuncionario.getSelectedItem() );
        }
        catch ( Exception e )
        {
            return 0;
        }
    }
    
    
    
    
    
    
}
