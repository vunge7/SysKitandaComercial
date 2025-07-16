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


import dao.TempoDao;
import dao.FuncionarioDao;
import entity.TbTempo;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import javax.persistence.EntityManagerFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import util.JPAEntityMannagerFactoryUtil;
import util.PermitirNumeros;

/***
 * @author Domingos Dala Vunge
 */

public class TempoVisao extends javax.swing.JFrame {
    
    private EntityManagerFactory emf = JPAEntityMannagerFactoryUtil.em;
    private FuncionarioDao funcionarioDao = new FuncionarioDao(emf);     
    private TempoDao tempoDao =  new TempoDao(emf);
    private TbTempo tempo;
    private static String DVML_COMERCIAL = "DVML-COMERCIA, Lda";
    private DefaultTableModel modelo;
    private int cod_tempo = 0;
    private int  linha_actual = -1;
    
    public TempoVisao() {
        
        initComponents();
        setLocationRelativeTo(null);
        txtTempoDao.setDocument( new PermitirNumeros() );
        txtIniciaisProfessores.addKeyListener( new TratarEventoTecladoProfessores()  );
        setComBoBox();   
        //mascara();
        try {
            busca_tempo();
        } catch (Exception e) {
        }
        
    }


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        txtIniciaisProfessores = new javax.swing.JTextField();
        cmbFuncionario = new javax.swing.JComboBox();
        jLabel7 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtTempoDao = new javax.swing.JFormattedTextField();
        jPanel7 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Definição do Salário por Tempo");

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
        jLabel7.setText("iniciais professor:");

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel6.setText("qtd. total de tempo mensal");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(txtTempoDao, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbFuncionario, 0, 318, Short.MAX_VALUE)
                    .addComponent(txtIniciaisProfessores))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtIniciaisProfessores, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbFuncionario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtTempoDao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(87, 87, 87))
        );

        jPanel7.setBackground(new java.awt.Color(255, 153, 51));

        jLabel19.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        jLabel19.setText("DEFINIÇÃO DO TEMPO DADO");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 386, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(504, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel1.setBackground(new java.awt.Color(43, 43, 43));
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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(217, 217, 217)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTable1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Cod", "Professor", "Data/Hora", "Tempo Dado"
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
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(0).setMaxWidth(100);
            jTable1.getColumnModel().getColumn(3).setMaxWidth(100);
        }

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane2))
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 262, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

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
            java.util.logging.Logger.getLogger(TempoVisao.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TempoVisao.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TempoVisao.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TempoVisao.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
                new TempoVisao().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox cmbFuncionario;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField txtIniciaisProfessores;
    private javax.swing.JFormattedTextField txtTempoDao;
    // End of variables declaration//GEN-END:variables


    
    public void setDadosProdutoModelo()
    {    
        DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
       
        String professor =    String.valueOf( modelo.getValueAt(jTable1.getSelectedRow(), 1) ) ;               
        String tempo  =    String.valueOf( modelo.getValueAt(jTable1.getSelectedRow(), 3) );
        
       this.cod_tempo   =   Integer.parseInt( String.valueOf( modelo.getValueAt(jTable1.getSelectedRow(), 0) ) );              
       
       cmbFuncionario.setSelectedItem(professor);
      
       txtTempoDao.setText( tempo );
       
          
    }
    
    private boolean vazios_campos()
    {        
         return false;
    }
    
    private void setComBoBox()
    {                                 
         cmbFuncionario.setModel(new DefaultComboBoxModel( (Vector) funcionarioDao.buscaTodosProfessores()) );
    }
    
    public void alterar()
    {
       
    
    }
    
    public void limpar() {
     
        txtTempoDao.setText("");
        
        
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
        
        if( !txtTempoDao.getText().equals("") ) 
        {              
            
            if ( !tempoDao.exist_preofessor(  getIdFuncionario() ) ) 
            {
                
                    this.tempo = new TbTempo();
                    preparar_tempo();                
                    try {
                        salvar_tempo();
                        limpar();
                        busca_tempo();
                        JOptionPane.showMessageDialog(null, "Tempo Efectuado Com Sucesso!...", DVML_COMERCIAL, JOptionPane.INFORMATION_MESSAGE);
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, "Falha ao Registrar o Subsidio do Professor", DVML_COMERCIAL, JOptionPane.ERROR_MESSAGE);
                    }

            } else {
                JOptionPane.showMessageDialog(null, "Já Esta Definido a quantia de Tempo para esse Professor!...", DVML_COMERCIAL, JOptionPane.WARNING_MESSAGE);
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
        
        if( !txtTempoDao.getText().equals("") ) 
        {            
              
                this.tempo = tempoDao.findTbTempo(   this.cod_tempo );
                preparar_tempo();
                try {
                    alterar_tempo();
                    busca_tempo();
                    JOptionPane.showMessageDialog(null, "Tempo Alterado com Sucesso!...", DVML_COMERCIAL, JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Falha ao Alterar o Tempo do Professor", DVML_COMERCIAL, JOptionPane.ERROR_MESSAGE);
                }

       }
        
    }

    private void procedimento_elimniar()
    {                  
            if( JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(null, "Tens a Plena Certeza que Pretendes Eliminar Tempo ?  ") ) 
            {                        
                try {
                    eliminar_tempo();
                    busca_tempo();
                    limpar();
                    JOptionPane.showMessageDialog(null, "Registro Eliminado com Sucesso!...", DVML_COMERCIAL, JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Falha ao Eliminar o Tempo", DVML_COMERCIAL, JOptionPane.ERROR_MESSAGE);
                }

           }
        
    }

    private void salvar_tempo()
    {        
         tempoDao.create(this.tempo);                     
    }
    
    private void alterar_tempo()
    {        
        try {
            tempoDao.edit(this.tempo);               
        } catch (Exception e) {
        }
         
    }
    
    private void eliminar_tempo()
    {
        try {
             tempoDao.destroy(this.cod_tempo);
        } catch (Exception e) {
        }
    
    }

    private void preparar_tempo()
    {        
           this.tempo.setTempoDado( Integer.parseInt(  txtTempoDao.getText() )  );       
           this.tempo.setData( new Date() );
           this.tempo.setHora(new Date() );
           this.tempo.setIdFuncionarioFK(funcionarioDao.findTbFuncionario(getIdFuncionario() ) );          
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
                cmbFuncionario.setModel(new DefaultComboBoxModel( (Vector) funcionarioDao.getFuncionarioProfessoresLIKENome(prefixo) ) );
            }
            else if ( evt.getKeyCode() == KeyEvent.VK_BACK_SPACE  )
            {
                try {
                    
                    prefixo = prefixo.toString().trim().substring(0, prefixo.length() - 1);
                    System.out.println("NOME VOLTAR " + prefixo);
                    cmbFuncionario.setModel(new DefaultComboBoxModel( (Vector) funcionarioDao.getFuncionarioProfessoresLIKENome(prefixo) ) );

                } catch (Exception e) {
                   
                }
               
                
            }
        }

        public void keyReleased(KeyEvent evt)
        {}
        public void keyTyped(KeyEvent evt)
        {}
        
    }
    
    private void busca_tempo()
    {

            List<TbTempo>   itens  =   tempoDao.getAllTempo();                  
            esvaziar_tabela();
            for (int i = 0; i < itens.size(); i++) {
                
                    this.modelo.addRow(new Object[]{
                                   itens.get(i).getIdTempoPK() ,
                                   itens.get(i).getIdFuncionarioFK().getNome() ,
                                   itens.get(i).getData().getDate()  +"/" +(itens.get(i).getData().getMonth() + 1 ) +"/" + (1900 + itens.get(i).getData().getYear()) 
                                   +"  " +itens.get(i).getHora().getHours() +":" + itens.get(i).getHora().getMinutes(),
                                   itens.get(i).getTempoDado()
                                  });
            }
            
    }
    
    private int getIdFuncionario()
    {                
        try {                   
            return funcionarioDao.getIdFuncionarioByNome((String) cmbFuncionario.getSelectedItem() );                
        } catch (Exception e) {
             return  0;
        }      
    }
    
  
  
}
