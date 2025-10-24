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
import entity.TbVenda;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import util.BDConexao;
import util.DVML;
import util.JPAEntityMannagerFactoryUtil;
import util.MetodosUtil;

/**
 *
 * @author Domingos Dala Vunge
 */
public class RelatorioVendaEmTempoRealVisao extends javax.swing.JFrame implements Runnable{

    private EntityManagerFactory emf = JPAEntityMannagerFactoryUtil.em;
    private VendaDao vendaDao = new VendaDao(emf);
    private UsuarioDao usuarioDao = new UsuarioDao(emf);    
    private ItemVendaDao itemVendaDao = new ItemVendaDao(emf);
    private PrecoDao precoDao = new PrecoDao(emf);
    private ArmazemDao armazemDao = new ArmazemDao(emf);
    private  double total_geral = 0;
    private Thread t;
    private BDConexao conexao;
    public RelatorioVendaEmTempoRealVisao(BDConexao conexao)  {
        
        initComponents();
        setResizable(false);
        setLocationRelativeTo(null);
        this.conexao = conexao;
        mascara();
        t  = new Thread(this);
        t.start();
       
    }

    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        table_armazem_1 = new javax.swing.JTable();
        jLabel4 = new javax.swing.JLabel();
        form_txt_armazem_1 = new javax.swing.JFormattedTextField();
        jPanel4 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        table_janela = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();
        form_txt_janela = new javax.swing.JFormattedTextField();
        form_txt_total_geral = new javax.swing.JFormattedTextField();
        jButton1 = new javax.swing.JButton();
        rb_RC = new javax.swing.JRadioButton();
        rb_FT = new javax.swing.JRadioButton();
        rb_PP = new javax.swing.JRadioButton();
        rb_FR = new javax.swing.JRadioButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("...:::: DVML - RELATORIO DE VENDA EM TEMPO REAL ::...");
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        table_armazem_1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "Doc. Ref Nº", "Cliente ", "Data", "Hora", "Total"
            }
        )
        {
            Class[] types = new Class []
            {
                java.lang.Object.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean []
            {
                false, false, false, false, false
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
        jScrollPane1.setViewportView(table_armazem_1);
        if (table_armazem_1.getColumnModel().getColumnCount() > 0)
        {
            table_armazem_1.getColumnModel().getColumn(0).setMinWidth(0);
            table_armazem_1.getColumnModel().getColumn(0).setMaxWidth(100);
        }

        jLabel4.setBackground(new java.awt.Color(0, 0, 102));
        jLabel4.setFont(new java.awt.Font("Lucida Grande", 1, 24)); // NOI18N
        jLabel4.setText("ARMAZÉM 1");

        form_txt_armazem_1.setEditable(false);
        form_txt_armazem_1.setBackground(new java.awt.Color(0, 0, 0));
        form_txt_armazem_1.setForeground(new java.awt.Color(255, 255, 255));
        form_txt_armazem_1.setFont(new java.awt.Font("Lucida Grande", 1, 26)); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 543, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(form_txt_armazem_1, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(form_txt_armazem_1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 526, Short.MAX_VALUE)
                .addContainerGap())
        );

        getContentPane().add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 114, -1, 590));

        jPanel4.setBackground(new java.awt.Color(51, 153, 0));

        jLabel3.setBackground(new java.awt.Color(0, 0, 102));
        jLabel3.setFont(new java.awt.Font("Lucida Grande", 1, 24)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("RELATÓRIO DE VENDAS EM TEMPO REAL");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(284, 284, 284)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 514, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(312, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 43, Short.MAX_VALUE)
        );

        getContentPane().add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 52, 1110, -1));

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        table_janela.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "Doc. Ref Nº", "Cliente ", "Data", "Hora", "Total"
            }
        )
        {
            Class[] types = new Class []
            {
                java.lang.Object.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean []
            {
                false, false, false, false, false
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
        jScrollPane2.setViewportView(table_janela);
        if (table_janela.getColumnModel().getColumnCount() > 0)
        {
            table_janela.getColumnModel().getColumn(0).setMinWidth(0);
            table_janela.getColumnModel().getColumn(0).setMaxWidth(100);
        }

        jLabel5.setBackground(new java.awt.Color(0, 0, 102));
        jLabel5.setFont(new java.awt.Font("Lucida Grande", 1, 24)); // NOI18N
        jLabel5.setText("ARMAZÉM 2");

        form_txt_janela.setEditable(false);
        form_txt_janela.setBackground(new java.awt.Color(0, 0, 0));
        form_txt_janela.setForeground(new java.awt.Color(255, 255, 255));
        form_txt_janela.setFont(new java.awt.Font("Lucida Grande", 1, 26)); // NOI18N

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 528, Short.MAX_VALUE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(form_txt_janela, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(form_txt_janela, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 526, Short.MAX_VALUE)
                .addContainerGap())
        );

        getContentPane().add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(571, 114, -1, 590));

        form_txt_total_geral.setEditable(false);
        form_txt_total_geral.setBackground(new java.awt.Color(0, 0, 0));
        form_txt_total_geral.setForeground(new java.awt.Color(255, 255, 255));
        form_txt_total_geral.setFont(new java.awt.Font("Lucida Grande", 1, 30)); // NOI18N
        getContentPane().add(form_txt_total_geral, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, 0, 220, 50));

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/LOGOUT - VERMELHO/Logout 16x16.png"))); // NOI18N
        jButton1.setText("Sair");
        jButton1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(1050, 10, -1, -1));

        buttonGroup1.add(rb_RC);
        rb_RC.setText("RC");
        rb_RC.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                rb_RCActionPerformed(evt);
            }
        });
        getContentPane().add(rb_RC, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 10, -1, 40));

        buttonGroup1.add(rb_FT);
        rb_FT.setText("FT");
        rb_FT.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                rb_FTActionPerformed(evt);
            }
        });
        getContentPane().add(rb_FT, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 10, -1, 40));

        buttonGroup1.add(rb_PP);
        rb_PP.setText("PP");
        rb_PP.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                rb_PPActionPerformed(evt);
            }
        });
        getContentPane().add(rb_PP, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 10, -1, 40));

        buttonGroup1.add(rb_FR);
        rb_FR.setSelected(true);
        rb_FR.setText("FR");
        rb_FR.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                rb_FRActionPerformed(evt);
            }
        });
        getContentPane().add(rb_FR, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 10, -1, 40));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void rb_RCActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_rb_RCActionPerformed
    {//GEN-HEADEREND:event_rb_RCActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rb_RCActionPerformed

    private void rb_FTActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_rb_FTActionPerformed
    {//GEN-HEADEREND:event_rb_FTActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rb_FTActionPerformed

    private void rb_PPActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_rb_PPActionPerformed
    {//GEN-HEADEREND:event_rb_PPActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rb_PPActionPerformed

    private void rb_FRActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_rb_FRActionPerformed
    {//GEN-HEADEREND:event_rb_FRActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rb_FRActionPerformed

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
            java.util.logging.Logger.getLogger(RelatorioVendaEmTempoRealVisao.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(RelatorioVendaEmTempoRealVisao.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(RelatorioVendaEmTempoRealVisao.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(RelatorioVendaEmTempoRealVisao.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new RelatorioVendaEmTempoRealVisao( BDConexao.getInstancia() ).setVisible(true);
                } catch (Exception ex) {
                    Logger.getLogger(RelatorioVendaEmTempoRealVisao.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JFormattedTextField form_txt_armazem_1;
    private javax.swing.JFormattedTextField form_txt_janela;
    private javax.swing.JFormattedTextField form_txt_total_geral;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JRadioButton rb_FR;
    private javax.swing.JRadioButton rb_FT;
    private javax.swing.JRadioButton rb_PP;
    private javax.swing.JRadioButton rb_RC;
    private javax.swing.JTable table_armazem_1;
    private javax.swing.JTable table_janela;
    // End of variables declaration//GEN-END:variables


    
    private void adicionar_tabela(JTable table, int idArmazem, JFormattedTextField label_total, List<TbVenda> lista) {
        
         DefaultTableModel modelo = (DefaultTableModel) table.getModel();
         modelo.setRowCount(0);
         
         try {
            
             for (TbVenda object : lista) {
                 
                 
                    modelo.addRow( new Object[]{
                        
                        object.getCodFact(),
                        object.getNomeCliente(),
//                        getNomeCliente( object ),
                        getData( object.getDataVenda()),
                        getHora( object.getHora()),                      
                        object.getTotalVenda()
                                
                    });
                 
             }             
     
             label_total.setText(  MetodosUtil.getNumeroTransformado( getTotal(table) ).substring(0, 
                    MetodosUtil.getNumeroTransformado( getTotal(table) ).length() - 2)
                   )  ;
           
        } catch (Exception e) {
            e.printStackTrace();
             //lb_total.setText("");
             //JOptionPane.showMessageDialog(null, "Não há registro para esse armazém", DVML.DVML_COMERCIAL, JOptionPane.WARNING_MESSAGE);
        }
    
    }

    private String getData(Date date) {
        
          return   getNumeroDoisDigitos(date.getDate()) 
                  +"/" + (   getNumeroDoisDigitos( date.getMonth() + 1 )  )
                  +"/" + (date.getYear() + 1900);
    
    }
    
    
    private String getData2(Date date) {
        
          return  (date.getYear() + 1900)
                  +"-" + (   getNumeroDoisDigitos( date.getMonth() + 1 )  )
                  +"-" + getNumeroDoisDigitos(date.getDate()) ;
    
    }
    
    private String getHora(Date date) {
        
          return   getNumeroDoisDigitos(  date.getHours() ) +":" 
                  + getNumeroDoisDigitos ( date.getMinutes() ) +":"
                  +getNumeroDoisDigitos(  date.getSeconds() );
    
    }

    private String getNumeroDoisDigitos(int  numero) {
        
        if (numero<10) {
              return "0" +numero;
        }     
        return  String.valueOf(numero);
    
    }
    
    private double getTotal(JTable tabela) {
        
            double  total  = 0;
            DefaultTableModel modelo = (DefaultTableModel) tabela.getModel();           
            for (int i = 0; i < modelo.getRowCount(); i++) {
                 total = total +  Double.parseDouble(tabela.getValueAt( i , 4).toString()   );            
            }               
            return total;
            
    }

    @Override
    public void run() {
        
        while (true) {
            try {
                  Thread.currentThread().sleep(1000);
                  adicionar_tabela(table_armazem_1, 1, form_txt_armazem_1,  MetodosUtil.getAllVendasByIdVenda(    getData2( new Date() ) , 1, conexao, getTipoDocumento()) );                  
                  adicionar_tabela(table_janela, 2, form_txt_janela,  MetodosUtil.getAllVendasByIdVenda(    getData2( new Date() ) , 2, conexao, getTipoDocumento()) );   
                  actualiza_total_geral();
            } catch (Exception e) {
                e.printStackTrace();
            }
            
        }
        
    }
    
    private void actualiza_total_geral() {
        
           double total_retalho  =  getTotal(table_armazem_1), 
                  total_groso =getTotal(table_janela) ,  
                  total_geral = 0;
           
           total_geral = total_retalho + total_groso;          
           //lb_total_geral.setText( String.valueOf(total_geral) );
           //lb_total_geral.setText( MetodosUtil.getNumeroTransformado( total_geral) ) ;
           
           form_txt_total_geral.setText(  MetodosUtil.getNumeroTransformado( total_geral).substring(0, 
                    MetodosUtil.getNumeroTransformado( total_geral).length() - 2)
                   )  ;
           
           
    }
    
    
    
    private void mascara()
    {
              try {

                DecimalFormat decimalFormat = new DecimalFormat("#,###,###.00");
                NumberFormatter numberFormatter = new NumberFormatter(decimalFormat);
                numberFormatter.setFormat(decimalFormat);
                numberFormatter.setAllowsInvalid(false);
                JFormattedTextField ft = new JFormattedTextField();
                form_txt_total_geral.setFormatterFactory(new DefaultFormatterFactory(numberFormatter));

        } catch (Exception e) {
        }
              
               try {

                DecimalFormat decimalFormat = new DecimalFormat("#,###,###.00");
                NumberFormatter numberFormatter = new NumberFormatter(decimalFormat);
                numberFormatter.setFormat(decimalFormat);
                numberFormatter.setAllowsInvalid(false);
                JFormattedTextField ft = new JFormattedTextField();
                form_txt_armazem_1.setFormatterFactory(new DefaultFormatterFactory(numberFormatter));

        } catch (Exception e) {
        }
               
                try {

                DecimalFormat decimalFormat = new DecimalFormat("#,###,###.00");
                NumberFormatter numberFormatter = new NumberFormatter(decimalFormat);
                numberFormatter.setFormat(decimalFormat);
                numberFormatter.setAllowsInvalid(false);
                JFormattedTextField ft = new JFormattedTextField();
                form_txt_janela.setFormatterFactory(new DefaultFormatterFactory(numberFormatter));

        } catch (Exception e) {
        }

    }

    private String getNomeCliente( TbVenda venda_local )
    {

        if ( venda_local.getCodigoCliente().getNome().equals( DVML._CLIENTE_CONSUMIDOR_FINAL ) )
        {
            String nome_cliente_consumidor_final = !venda_local.getNomeConsumidorFinal().equals( "" ) ? " (" + venda_local.getNomeConsumidorFinal() + ")" : "";
            return venda_local.getNomeCliente() + nome_cliente_consumidor_final;
        }
        return venda_local.getCodigoCliente().getNome();
    }
    
    private int getTipoDocumento()
    {
        if ( rb_FR.isSelected() )
        {
            return DVML.DOC_FACTURA_RECIBO_FR;
        }
        else if ( rb_FT.isSelected() )
        {
            return DVML.DOC_FACTURA_FT;
        }
        else if ( rb_PP.isSelected() )
        {
            return DVML.DOC_FACTURA_PROFORMA_PP;
        }
        else
        {
            return DVML.DOC_RECIBO_RC;
        }

    }
}
