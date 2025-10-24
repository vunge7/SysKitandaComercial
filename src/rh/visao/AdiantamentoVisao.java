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
import dao.AdiantamentoDao;
import dao.EmpresaDao;
import dao.FuncionarioDao;
import entity.TbAdiantamento;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import javax.persistence.EntityManagerFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import lista.RelatorioAdiantamentoCaixa;
import util.BDConexao;
import util.DVML;

import util.JPAEntityMannagerFactoryUtil;
import util.MetodosUtil;
import util.PermitirNumeros;

/***
 * @author Domingos Dala Vunge
 */

public class AdiantamentoVisao extends javax.swing.JFrame {
    
    private EntityManagerFactory emf = JPAEntityMannagerFactoryUtil.em;
    private FuncionarioDao funcionarioDao = new FuncionarioDao(emf);     
    private AdiantamentoDao adiantamentoDao =  new AdiantamentoDao(emf);
    private EmpresaDao empresaDao =  new EmpresaDao(emf);
    private TbAdiantamento adiantamento;
    private static String DVML_COMERCIAL = "DVML-COMERCIA, Lda";
    private DefaultTableModel modelo;
    private int cod_adiantamento = 0;
    private int  linha_actual = -1;
    private int idUser = 0;
    private int idEmpresa = 0;
    private BDConexao conexao;
    
    public AdiantamentoVisao(int idUser, int idEmpresa) {
        
        initComponents();
        this.idUser = idUser;
        this.idEmpresa = idEmpresa;
        setLocationRelativeTo(null);
        conexao = BDConexao.getInstancia();
        txtValor.setDocument( new PermitirNumeros() );
        txtIniciaisProfessores.addKeyListener( new TratarEventoTecladoProfessores()  );
        lbEmpresa.setText(  empresaDao.findEmpresa(idEmpresa).getNome()      );
        setComBoBox();   
        //mascara();
        try {
            busca_adiantamento();
        } catch (Exception e) {
        }
        
    }


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        txtIniciaisProfessores = new javax.swing.JTextField();
        cmbFuncionarios = new javax.swing.JComboBox();
        jLabel7 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtValor = new javax.swing.JFormattedTextField();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtDescricao = new javax.swing.JTextArea();
        jPanel7 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        lbEmpresa = new javax.swing.JLabel();
        lb_seja_bem_vindo1 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Adiantamento de Salário");

        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        txtIniciaisProfessores.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIniciaisProfessoresActionPerformed(evt);
            }
        });

        cmbFuncionarios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbFuncionariosActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel7.setText("Iniciais Funcionário:");

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel6.setText("Valor:");

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel8.setText("Descrição:");

        txtDescricao.setColumns(20);
        txtDescricao.setRows(5);
        jScrollPane1.setViewportView(txtDescricao);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(cmbFuncionarios, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtIniciaisProfessores)
                        .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 266, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(txtValor, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(94, 94, 94)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 379, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(201, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtIniciaisProfessores, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbFuncionarios, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtValor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel7.setBackground(new java.awt.Color(255, 153, 51));

        jLabel19.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        jLabel19.setText("ADIANTAMENTO DE SALÁRIO");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel19)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jButton1.setText("Salvar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Alterar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Eliminar");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("Sair");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        lbEmpresa.setFont(new java.awt.Font("Times New Roman", 3, 13)); // NOI18N
        lbEmpresa.setText("BEM VINDO!....");

        lb_seja_bem_vindo1.setBackground(new java.awt.Color(0, 0, 0));
        lb_seja_bem_vindo1.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
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
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lbEmpresa, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lb_seja_bem_vindo1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTable1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Cod", "Funcionário", "Data/Hora", "Valor"
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
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jScrollPane2)))
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtIniciaisProfessoresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIniciaisProfessoresActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIniciaisProfessoresActionPerformed

    private void cmbFuncionariosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbFuncionariosActionPerformed
        // TODO add your handling code here:
      

    }//GEN-LAST:event_cmbFuncionariosActionPerformed

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

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        // TODO add your handling code here:
        if( evt.getClickCount() >=2 ){

            setDadosProdutoModelo();

        }
    }//GEN-LAST:event_jTable1MouseClicked

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
            java.util.logging.Logger.getLogger(AdiantamentoVisao.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AdiantamentoVisao.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AdiantamentoVisao.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AdiantamentoVisao.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
                new AdiantamentoVisao(15, 1).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox cmbFuncionarios;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel lbEmpresa;
    private javax.swing.JLabel lb_seja_bem_vindo1;
    private javax.swing.JTextArea txtDescricao;
    private javax.swing.JTextField txtIniciaisProfessores;
    private javax.swing.JFormattedTextField txtValor;
    // End of variables declaration//GEN-END:variables


    
    public void setDadosProdutoModelo()
    {    
        DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
       
        String professor =    String.valueOf( modelo.getValueAt(jTable1.getSelectedRow(), 1) ) ;               
        String valor  =    String.valueOf( modelo.getValueAt(jTable1.getSelectedRow(), 3) ).substring(
                           0, 
                           String.valueOf( modelo.getValueAt(jTable1.getSelectedRow(), 3) ).length() - 1 ) ;
        
        
       this.cod_adiantamento   =   Integer.parseInt( String.valueOf( modelo.getValueAt(jTable1.getSelectedRow(), 0) ) );              
       
      txtDescricao.setText( adiantamentoDao.findTbAdiantamento(cod_adiantamento).getDescricao() );
       
       cmbFuncionarios.setSelectedItem(professor);
      
       txtValor.setText( valor );
       
          
    }
    
    private boolean vazios_campos()
    {        
         return false;
    }
    
    private void setComBoBox()
    {                                 
         cmbFuncionarios.setModel(new DefaultComboBoxModel( (Vector) funcionarioDao.buscaTodosNomeByIdEmpresaCombo(idEmpresa)) );
    }
    
    public void alterar()
    {
       
    
    }
    
    public void limpar() {
     
        txtValor.setText("");
        txtDescricao.setText("");
        
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
    
    public boolean validar_valor_saldo(){

        if((Double.parseDouble(txtValor.getText())) >  MetodosUtil.getSaldoByBanco(  DVML.COD_BANCO_CAIXA) ){
            JOptionPane.showMessageDialog(null, "Atenção\nO valor a Sair não pode ser superior ao saldo do Caixa!");
            return false;
        }return true;
    }
    
    private void procedimento_salvar()
    {          
        
        if( !txtValor.getText().equals("") ) 
        {
            if (validar_valor_saldo()) {
                this.adiantamento = new TbAdiantamento();
                preparar_adiantamento();
                
                try {
                    salvar_adiantamento();

                    limpar();
                    busca_adiantamento();
                    JOptionPane.showMessageDialog(null, "Adiantamento Efectuado Com Sucesso!...", DVML_COMERCIAL, JOptionPane.INFORMATION_MESSAGE);
                                            try {
                            new RelatorioAdiantamentoCaixa(adiantamentoDao.getUltimoAdiantamento());

                        } catch (Exception e) {
                        }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Falha ao Registrar o Subsidio do Professor", DVML_COMERCIAL, JOptionPane.ERROR_MESSAGE);
                }
                
                 }
                
          }else {
            
                JOptionPane.showMessageDialog( null, 
                                                      "Pf. Digite a quantia!",
                                                      DVML_COMERCIAL,
                                                      JOptionPane.WARNING_MESSAGE);               
          }
          
    }
    
    private void procedimento_alterar()
    {          
        
        if( !txtValor.getText().equals("") ) 
        {            
              
                this.adiantamento = adiantamentoDao.findTbAdiantamento(   this.cod_adiantamento );
                preparar_adiantamento();
                try {
                    alterar_adiantamento();
                    busca_adiantamento();
                    JOptionPane.showMessageDialog(null, "Adiantamento Alterado com Sucesso!...", DVML_COMERCIAL, JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Falha ao Alterar o Adiantamento do Professor", DVML_COMERCIAL, JOptionPane.ERROR_MESSAGE);
                }

       }
        
    }

    private void procedimento_elimniar()
    {                  
            if( JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(null, "Tens a Plena Certeza que Pretendes Eliminar Adiantamento ?  ") ) 
            {                        
                try {
                    eliminar_adiantamento();
                    busca_adiantamento();
                    limpar();
                    JOptionPane.showMessageDialog(null, "Registro Eliminado com Sucesso!...", DVML_COMERCIAL, JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Falha ao Eliminar o Adiantamento", DVML_COMERCIAL, JOptionPane.ERROR_MESSAGE);
                }

           }
        
    }

    private void salvar_adiantamento()
    {        
         adiantamentoDao.create(this.adiantamento);
         MetodosUtil.subtrair_saldo_banco(adiantamento.getValorAdiantado(), DVML.COD_BANCO_CAIXA, conexao);

         
    }
    
    private void alterar_adiantamento()
    {        
        try {
            adiantamentoDao.edit(this.adiantamento);               
        } catch (Exception e) {
        }
         
    }
    
    private void eliminar_adiantamento()
    {
        try {
             adiantamentoDao.destroy(this.cod_adiantamento);
        } catch (Exception e) {
        }
    
    }

    private void preparar_adiantamento()
    {
        
           this.adiantamento.setValorAdiantado(  Double.parseDouble(  txtValor.getText() )  );
           this.adiantamento.setDescricao( txtDescricao.getText() );
           this.adiantamento.setData( new Date() );
           this.adiantamento.setHora( new Date() );
           this.adiantamento.setIdFuncionarioFK(funcionarioDao.findTbFuncionario(getIdFuncionario() ) );
          
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
                cmbFuncionarios.setModel(new DefaultComboBoxModel( (Vector) funcionarioDao.getFuncionarioLIKENomeByIdEmpresa(prefixo, idEmpresa) ) );
            }
            else if ( evt.getKeyCode() == KeyEvent.VK_BACK_SPACE  )
            {
                try {
                    
                    prefixo = prefixo.toString().trim().substring(0, prefixo.length() - 1);
                    System.out.println("NOME VOLTAR " + prefixo);
                    cmbFuncionarios.setModel(new DefaultComboBoxModel( (Vector) funcionarioDao.getFuncionarioLIKENomeByIdEmpresa(prefixo, idEmpresa) ) );

                } catch (Exception e) {
                   
                }
               
                
            }
        }

        public void keyReleased(KeyEvent evt)
        {}
        public void keyTyped(KeyEvent evt)
        {}
        
    }
    
    private void busca_adiantamento()
    {

            List<TbAdiantamento>   itens  =   adiantamentoDao.getAllAdiantamentoByIdEmpresa(idEmpresa);                  
            esvaziar_tabela();
            for (int i = 0; i < itens.size(); i++) {
                
                    this.modelo.addRow(new Object[]{
                                   itens.get(i).getIdAdiantamentoFK() ,
                                   itens.get(i).getIdFuncionarioFK().getNome() ,
                                   itens.get(i).getData().getDate()  +"/" +(itens.get(i).getData().getMonth() + 1 ) +"/" + (1900 + itens.get(i).getData().getYear()) 
                                   +"  " +itens.get(i).getHora().getHours() +":" + itens.get(i).getHora().getMinutes(),
                                   itens.get(i).getValorAdiantado()
                                  });
            }
            
    }
    
    private int getIdFuncionario()
    {                
        try {                   
            return funcionarioDao.getIdFuncionarioByNome((String) cmbFuncionarios.getSelectedItem() );                
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
                txtValor.setFormatterFactory(new DefaultFormatterFactory(numberFormatter));
            } catch (Exception e) {
            }
            
    }
    
    
    
  
}
