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


import dao.BancoDao;
import dao.ItemSubsidioFuncionarioDao;
import dao.FuncionarioDao;
import dao.ContaDao;
import dao.EmpresaDao;
import entity.TbConta;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Vector;
import javax.persistence.EntityManagerFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;

import util.JPAEntityMannagerFactoryUtil;
import util.PermitirNumeros;

/***
 * @author Domingos Dala Vunge
 */

public class ContaVisao extends javax.swing.JFrame {
    
    private EntityManagerFactory emf = JPAEntityMannagerFactoryUtil.em;
    private FuncionarioDao funcionarioDao = new FuncionarioDao(emf);  
    private ContaDao contaDao = new ContaDao(emf);
    private BancoDao bancoDao = new BancoDao(emf);
    private EmpresaDao empresaDao = new EmpresaDao(emf);
    private ItemSubsidioFuncionarioDao itemSubsidioProfessorDao =  new ItemSubsidioFuncionarioDao(emf);
    private TbConta conta;
  
    private static String DVML_COMERCIAL = "DVML-COMERCIA, Lda";
    private DefaultTableModel modelo;
    private int cod_conta = 0;
    private int  linha_actual = -1;
    private int idUser = 0;
    private int idEmpresa = 0;
    
    public ContaVisao(int idUser, int idEmpresa) {
        
        initComponents();
        this.idUser = idUser;
        this.idEmpresa = idEmpresa;
        setLocationRelativeTo(null);
        txt_numero_conta.setDocument( new PermitirNumeros() );
        txtIniciaisProfessores.addKeyListener( new TratarEventoTecladoProfessores()  );
        lbEmpresa.setText(  empresaDao.findEmpresa(idEmpresa).getNome()      );
        setComBoBox();   
        //mascara();
        try {
            busca_conta_funcionarioes();
        } catch (Exception e) {
        }
        
    }


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        cmbBanco = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        txtIniciaisProfessores = new javax.swing.JTextField();
        cmbFuncionario = new javax.swing.JComboBox();
        jLabel7 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txt_numero_conta = new javax.swing.JFormattedTextField();
        txt_iban = new javax.swing.JFormattedTextField();
        jLabel8 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        lbEmpresa = new javax.swing.JLabel();
        lb_seja_bem_vindo1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Gestão de Contas de Funcionário");

        jTable1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Codigo", "Funcionario", "Banco", "N.º da Conta", "Iban"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, true
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
        jScrollPane1.setViewportView(jTable1);
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(0).setMaxWidth(100);
        }

        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        cmbBanco.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbBancoActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel5.setText("Banco");

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
        jLabel7.setText("Iniciais Funcionário:");

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel6.setText("N.º da Conta");

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel8.setText("IBAN");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(txtIniciaisProfessores)
                        .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 266, Short.MAX_VALUE))
                    .addComponent(cmbFuncionario, javax.swing.GroupLayout.PREFERRED_SIZE, 397, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                            .addComponent(jLabel5)
                            .addGap(121, 121, 121)
                            .addComponent(jLabel6))
                        .addGroup(jPanel3Layout.createSequentialGroup()
                            .addComponent(cmbBanco, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(txt_numero_conta, javax.swing.GroupLayout.PREFERRED_SIZE, 282, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jLabel8))
                    .addComponent(txt_iban, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 438, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtIniciaisProfessores, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbFuncionario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cmbBanco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_numero_conta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_iban, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel7.setBackground(new java.awt.Color(255, 153, 51));

        jLabel19.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N
        jLabel19.setText("GESTÃO DE CONTAS DO FUNCIONÁRIO");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 419, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(475, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, 66, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel1.setBackground(new java.awt.Color(43, 43, 43));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/salvar_16x16.png"))); // NOI18N
        jButton1.setText("Salvar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/alterar_16x16.png"))); // NOI18N
        jButton2.setText("Alterar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/eliminar_16x16.png"))); // NOI18N
        jButton3.setText("Eliminar");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/LOGOUT - VERMELHO/Logout 16x16.png"))); // NOI18N
        jButton4.setText("Sair");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        lbEmpresa.setFont(new java.awt.Font("Times New Roman", 3, 13)); // NOI18N
        lbEmpresa.setForeground(new java.awt.Color(255, 255, 255));
        lbEmpresa.setText("BEM VINDO!....");

        lb_seja_bem_vindo1.setBackground(new java.awt.Color(0, 0, 0));
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
                .addComponent(lbEmpresa, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(31, 31, 31)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(21, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbEmpresa, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lb_seja_bem_vindo1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 294, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        // TODO add your handling code here:
        if( evt.getClickCount() >=2 ){

            setDadosProdutoModelo();

        }
    }//GEN-LAST:event_jTable1MouseClicked

    private void cmbBancoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbBancoActionPerformed
        // TODO add your handling code here:
          
    }//GEN-LAST:event_cmbBancoActionPerformed

    private void txtIniciaisProfessoresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIniciaisProfessoresActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIniciaisProfessoresActionPerformed

    private void cmbFuncionarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbFuncionarioActionPerformed
        // TODO add your handling code here:
      

    }//GEN-LAST:event_cmbFuncionarioActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        procedimento_salvar();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        procedimento_alterar();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        procedimento_elimniar();
    }//GEN-LAST:event_jButton3ActionPerformed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        
          /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ContaVisao.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ContaVisao.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ContaVisao.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ContaVisao.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ContaVisao(15, 1).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox cmbBanco;
    private javax.swing.JComboBox cmbFuncionario;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel lbEmpresa;
    private javax.swing.JLabel lb_seja_bem_vindo1;
    private javax.swing.JTextField txtIniciaisProfessores;
    private javax.swing.JFormattedTextField txt_iban;
    private javax.swing.JFormattedTextField txt_numero_conta;
    // End of variables declaration//GEN-END:variables


    
    public void setDadosProdutoModelo()
    {    
        DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
        String funcionario =    String.valueOf( modelo.getValueAt(jTable1.getSelectedRow(), 1) ) ;
        String banco =    String.valueOf( modelo.getValueAt(jTable1.getSelectedRow(), 2) ) ;
        String numero_conta  =    String.valueOf( modelo.getValueAt(jTable1.getSelectedRow(), 3) );
        String IBAN  =    String.valueOf( modelo.getValueAt(jTable1.getSelectedRow(), 4) );
        
        
       this.cod_conta   =   Integer.parseInt(  String.valueOf( modelo.getValueAt(jTable1.getSelectedRow(), 0 ) ) );
       
       cmbFuncionario.setSelectedItem(funcionario);
       cmbBanco.setSelectedItem(banco);
       txt_numero_conta.setText( numero_conta );
       txt_iban.setText( IBAN );
       
          
    }
    
    private boolean vazios_campos()
    {        
         return false;
    }
    
    private void setComBoBox()
    {                         
         cmbBanco.setModel(   new DefaultComboBoxModel(  (Vector) bancoDao.buscaTodos() ) );
         cmbFuncionario.setModel(   new DefaultComboBoxModel( (Vector) funcionarioDao.buscaTodosNomeByIdEmpresaCombo(idEmpresa) ) );
    }
    
    public void alterar()
    {
       
    
    }
    
    public void limpar() {
     
        
    }     
    private boolean tabela_vazia()
    {
              DefaultTableModel modelo = (DefaultTableModel)jTable1.getModel();
              return  modelo.getRowCount() == 0;    
    }
    
    private void esvaziar_tabela()
    {
        this. modelo = (DefaultTableModel)jTable1.getModel();
        this.modelo.setRowCount(0);    
    }
    
     
    private void actuazlizar_quantidade(String quantidade)
    {
            DefaultTableModel modelo  = (DefaultTableModel) jTable1.getModel();
            
            double preco =    Double.parseDouble( String.valueOf( modelo.getValueAt( linha_actual, 2) ) );
            int qtd_existente =    Integer.parseInt( String.valueOf( modelo.getValueAt( linha_actual, 3) ) );
          
            int qtd_entrar = Integer.parseInt(quantidade);
            int qtd_actualizada = qtd_existente + qtd_entrar;             
            double total_item = preco * qtd_actualizada;      
            
            modelo.setValueAt(quantidade, linha_actual , 3);
            modelo.setValueAt(  total_item , linha_actual , 5);
            
            this.linha_actual = -1;
    
    }
    
    
    private void remover_item_carrinho()
    {
    
        DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
        modelo.setRowCount(0);
    
    }
    
    private void setDadosFichaTecnica()
    {
                
    }
    
    private void procedimento_salvar()
    {          
        
        if( !txt_numero_conta.getText().equals("") ) 
        {          
               if (!txt_iban.getText().equals("")) {
                
          
                        if (!this.contaDao.exist_relacao_funcionario_conta(getIdProfessor(), getIdBanco() ) ) 
                        {
                                System.out.println("Não Existe");
                                this.conta = new TbConta();
                                preparar_conta_funcionario();

                                try {
                                    salvar_conta();
                                    busca_conta_funcionarioes();
                                    JOptionPane.showMessageDialog(null, "Conta Salva com Sucesso!...", DVML_COMERCIAL, JOptionPane.INFORMATION_MESSAGE);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    JOptionPane.showMessageDialog(null, "Falha ao Registrar a Conta do Funcionário", DVML_COMERCIAL, JOptionPane.ERROR_MESSAGE);
                                }

                        }else {

                                   JOptionPane.showMessageDialog( null, 
                                                                  "O(A) Funcionário(a) " 
                                                                  +funcionarioDao.findTbFuncionario(getIdProfessor()).getNome() 
                                                                  +" Já Possui uma Conta Neste Banco",
                                                                  DVML_COMERCIAL,
                                                                  JOptionPane.WARNING_MESSAGE);
                        }
            
                }else {
            
                             JOptionPane.showMessageDialog( null, 
                                                      "Pf. Digite o iban do(a) Funcionário(a)!" 
                                                      +funcionarioDao.findTbFuncionario(getIdProfessor()).getNome(),
                                                      DVML_COMERCIAL,
                                                      JOptionPane.WARNING_MESSAGE);               
                }
                    
          }else {
            
                JOptionPane.showMessageDialog( null, 
                                                      "Pf. Digite o numero da conta do(a) Funcionário(a)!" 
                                                      +funcionarioDao.findTbFuncionario(getIdProfessor()).getNome(),
                                                      DVML_COMERCIAL,
                                                      JOptionPane.WARNING_MESSAGE);               
          }
          
    }
    
    
    
    
    private void procedimento_alterar()
    {          
        
        if( !txt_numero_conta.getText().equals("") ) 
        {            
            
                    if ( !this.contaDao.exist_relacao_funcionario_conta_para_alem(getIdProfessor(), getIdBanco(), this.cod_conta ) ) 
                    {           
                          //System.out.println("Altera");
                            this.conta = contaDao.findTbConta(   this.cod_conta );
                            preparar_conta_funcionario();

                            try {
                                alterar_conta();
                                busca_conta_funcionarioes();
                                JOptionPane.showMessageDialog(null, "Conta Alterada com Sucesso!...", DVML_COMERCIAL, JOptionPane.INFORMATION_MESSAGE);
                            } catch (Exception e) {
                                JOptionPane.showMessageDialog(null, "Falha ao Alterar a Conta do Funcionário", DVML_COMERCIAL, JOptionPane.ERROR_MESSAGE);
                            }

                    }else  JOptionPane.showMessageDialog(null, "Impossivel Alterar o Registro!\n Já Existe na Tabela Uma Conta deste(a) Funcionário(a) "
                            +funcionarioDao.findTbFuncionario( getIdProfessor()).getNome() +"  no Banco  " 
                            +bancoDao.findTbBanco( getIdBanco() ).getDescrisao() +"\nObrigado!...", DVML_COMERCIAL, JOptionPane.WARNING_MESSAGE);

       }
        
    }

    private void procedimento_elimniar()
    {                  
            if( JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(null, "Tens a Plena certeza que pretendes eliminar essa Conta ?  ") ) 
            {                        
                try {
                    eliminar_conta();
                    busca_conta_funcionarioes();
                    JOptionPane.showMessageDialog(null, "Conta Eliminada com Sucesso!...", DVML_COMERCIAL, JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Falha ao Eliminar a Conta do Funcionário", DVML_COMERCIAL, JOptionPane.ERROR_MESSAGE);
                }

                    
           }
        
    }

    private void salvar_conta()
    {        
         contaDao.create(this.conta);                     
    }
    
    private void alterar_conta()
    {        
        try {
            contaDao.edit(this.conta);               
        } catch (Exception e) {
        }
         
    }
    
    private void eliminar_conta()
    {
        try {
             contaDao.destroy(this.cod_conta);
        } catch (Exception e) {
        }
    
    }

    private void preparar_conta_funcionario()
    {
           this.conta.setIdFuncionarioFK(   funcionarioDao.findTbFuncionario( getIdProfessor() ) );
           this.conta.setIdBancoFK( bancoDao.findTbBanco( getIdBanco() ) );
           this.conta.setSaldoConta( 0.0 ); 
           this.conta.setNumeroConta( txt_numero_conta.getText() );
           this.conta.setIban(txt_iban.getText() );
  
    }
  
    private void imprimir()            
    {

    }

    private void detalhe_ficha_tecnica()
    {    
        
    
    }
    
    class TratarEventoTecladoProfessores implements KeyListener
    {
        String prefixo = "";

        public void keyPressed(KeyEvent evt)
        {
     
            if (evt.getKeyCode() != KeyEvent.VK_BACK_SPACE && evt.getKeyCode() != KeyEvent.VK_ENTER)
            {
                char key = evt.getKeyChar();
                prefixo = txtIniciaisProfessores.getText().trim() + key;
                cmbFuncionario.setModel(   new DefaultComboBoxModel( (Vector) funcionarioDao.getFuncionarioLIKENomeByIdEmpresa(prefixo, idEmpresa) ) );
            }
            else if ( evt.getKeyCode() == KeyEvent.VK_BACK_SPACE  )
            {
                try {
                    
                    prefixo = prefixo.toString().trim().substring(0, prefixo.length() - 1);
                    System.out.println("NOME VOLTAR " + prefixo);
                    cmbFuncionario.setModel(   new DefaultComboBoxModel( (Vector) funcionarioDao.getFuncionarioLIKENomeByIdEmpresa(prefixo, idEmpresa) ) );

                } catch (Exception e) {
                   
                }
               
                
            }
        }

        public void keyReleased(KeyEvent evt)
        {}
        public void keyTyped(KeyEvent evt)
        {}
        
    }
    
    private void busca_conta_funcionarioes()
    {

            List<TbConta>   itens  =   contaDao.getAllContasByIdEmpresa(idEmpresa);                  
            esvaziar_tabela();
            for (int i = 0; i < itens.size(); i++) {
                
                    this.modelo.addRow(new Object[]{
                                   itens.get(i).getIdContaPK() ,
                                   itens.get(i).getIdFuncionarioFK().getNome() ,
                                   itens.get(i).getIdBancoFK().getDescrisao() ,
                                   itens.get(i).getNumeroConta(),
                                   itens.get(i).getIban()
                                  
                                  });
            }
            
    }
    
    private int getIdProfessor()
    {                
        try {                   
            return funcionarioDao.getIdFuncionarioByNome((String) cmbFuncionario.getSelectedItem() );                
        } catch (Exception e) {
             return  0;
        }      
    }
    
    private int getIdBanco()
    {                
        try {                   
            return bancoDao.getIdByDescricao( (String) cmbBanco.getSelectedItem() );                
        } catch (Exception e) {
             return  0;
        }      
    }
    
    private void mascara()
    {
        
            try {
                DecimalFormat decimalFormat = new DecimalFormat("#,###,###.00");
                NumberFormatter numberFormatter = new NumberFormatter(decimalFormat);
                numberFormatter.setFormat(decimalFormat);
                numberFormatter.setAllowsInvalid(false);
                JFormattedTextField ft = new JFormattedTextField();
                txt_numero_conta.setFormatterFactory(new DefaultFormatterFactory(numberFormatter));
            } catch (Exception e) {
            }
            
    }
  
}
