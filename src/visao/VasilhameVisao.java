/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package visao;


import java.sql.Connection;
import dao.ArmazemDao;
import dao.CategoriaDao;
import dao.ProdutoDao;
import dao.UsuarioDao;
import dao.VasilhameDao;
import entity.TbArmazem;
import entity.TbProduto;
import entity.TbVasilhame;
import java.util.List;
import java.util.Vector;
import javax.persistence.EntityManagerFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import modelo.StockModelo;
import util.BDConexao;
import util.JPAEntityMannagerFactoryUtil;

/**
 *
 * @author Dallas
 */
public class VasilhameVisao extends javax.swing.JFrame {

    private EntityManagerFactory emf = JPAEntityMannagerFactoryUtil.em;
    private TbProduto produto;
    private VasilhameDao vasilhameDao = new VasilhameDao(emf);
    private TbVasilhame vasilhame;
    private CategoriaDao categoriaDao = new CategoriaDao(emf);
    private ProdutoDao produtoDao = new ProdutoDao(emf);
    private UsuarioDao usuarioDao = new UsuarioDao(emf);
    private ArmazemDao armazemDao = new ArmazemDao(emf);
    private BDConexao conexao;
    private StockModelo stockModelo;
    
    private int idVasilhame = 0;
    private int id_armazem = 0;
    private int idUser = 0;
    
    public VasilhameVisao(   int idUser) {
        
        initComponents();
        setLocationRelativeTo(null);
        this.idUser = idUser;
        this.conexao = BDConexao.getInstancia();
        setComboCategoria();
        //setVasilhameExistente();
        cmbArmazem.setModel( new DefaultComboBoxModel( conexao.getElementos("tb_armazem", "designacao", false)   ) );
        cmbProduto.setModel( new DefaultComboBoxModel( conexao.getElementos2("tb_produto", "designacao", "cod_Tipo_Produto", getCodigoTipoProduto())   ) );   
        adicionar_tabela(   vasilhameDao.findTbVasilhameEntities() );
        
    }

    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        txtVasilhame = new javax.swing.JTextField();
        lbCategoria = new javax.swing.JLabel();
        lbProduto = new javax.swing.JLabel();
        cmbCategoria = new javax.swing.JComboBox();
        cmbProduto = new javax.swing.JComboBox();
        lbProduto2 = new javax.swing.JLabel();
        lbArmazem = new javax.swing.JLabel();
        cmbArmazem = new javax.swing.JComboBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("...:::::: DVML-VASILHAME :::::::....");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        lbCategoria.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 16)); // NOI18N
        lbCategoria.setText("Categoria:");

        lbProduto.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 16)); // NOI18N
        lbProduto.setText("Produto:");

        cmbCategoria.setBackground(new java.awt.Color(51, 153, 0));
        cmbCategoria.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 14)); // NOI18N
        cmbCategoria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbCategoriaActionPerformed(evt);
            }
        });

        cmbProduto.setBackground(new java.awt.Color(51, 153, 0));
        cmbProduto.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 14)); // NOI18N
        cmbProduto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbProdutoActionPerformed(evt);
            }
        });

        lbProduto2.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 16)); // NOI18N
        lbProduto2.setText("Novo Vasilhame");

        lbArmazem.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 16)); // NOI18N
        lbArmazem.setText("Armazém");

        cmbArmazem.setBackground(new java.awt.Color(51, 153, 0));
        cmbArmazem.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 14)); // NOI18N
        cmbArmazem.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Todos", "Activo", "Desactivo" }));
        cmbArmazem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbArmazemActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(lbProduto2)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtVasilhame)
                            .addComponent(lbProduto, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbCategoria)
                            .addComponent(cmbCategoria, 0, 229, Short.MAX_VALUE)
                            .addComponent(cmbProduto, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lbArmazem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cmbArmazem, 0, 215, Short.MAX_VALUE))
                        .addGap(152, 152, 152))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbCategoria)
                    .addComponent(lbArmazem, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(7, 7, 7)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbArmazem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbProduto)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbProduto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbProduto2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtVasilhame, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(19, Short.MAX_VALUE))
        );

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Cod.", "Descriçao", "Produto", "Armazem"
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
        jScrollPane1.setViewportView(jTable1);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

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
        jButton3.setEnabled(false);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("Cancelar");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2)
                    .addComponent(jButton3)
                    .addComponent(jButton4))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 205, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        salvar();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        alterar();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        // TODO add your handling code here:
        
          if( evt.getClickCount() >=2 ){
        
                setDadosTabela();
                    
        }
        
    }//GEN-LAST:event_jTable1MouseClicked

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
                eliminar();        // TODO add your handling code here:
    }//GEN-LAST:event_jButton3ActionPerformed

    private void cmbCategoriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbCategoriaActionPerformed

        try {

            System.err.println("CODIGO TIPO PRODUTO: " + getCodigoTipoProduto());
            cmbProduto.setModel(new DefaultComboBoxModel(conexao.getElementos2("tb_produto", "designacao", "cod_Tipo_Produto", getCodigoTipoProduto())));

        } catch (Exception ex) {

        }

    }//GEN-LAST:event_cmbCategoriaActionPerformed

    private void cmbProdutoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbProdutoActionPerformed

//        try {
//            adicionar_preco_quantidade();
//        } catch (Exception e) {
//        }

    }//GEN-LAST:event_cmbProdutoActionPerformed

    private void cmbArmazemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbArmazemActionPerformed
        // TODO add your handling code here:

        id_armazem = getCodigoArmazem();

      
    }//GEN-LAST:event_cmbArmazemActionPerformed
 
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
            java.util.logging.Logger.getLogger(VasilhameVisao.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VasilhameVisao.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VasilhameVisao.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VasilhameVisao.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
               new VasilhameVisao(1).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox cmbArmazem;
    private javax.swing.JComboBox cmbCategoria;
    private javax.swing.JComboBox cmbProduto;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel lbArmazem;
    private javax.swing.JLabel lbCategoria;
    private javax.swing.JLabel lbProduto;
    private javax.swing.JLabel lbProduto2;
    private javax.swing.JTextField txtVasilhame;
    // End of variables declaration//GEN-END:variables


    private void setDadosVasilhame()
    {
            vasilhame.setDescricao( txtVasilhame.getText() );
            vasilhame.setQtdExistente(0d); 
            vasilhame.setFkProduto( produtoDao.getProdutoByDescricao(   String.valueOf(    cmbProduto.getSelectedItem()) ) );
            vasilhame.setFkArmazem( armazemDao.getArmazemByDescricao (   String.valueOf(    cmbArmazem.getSelectedItem()) ) );
       
    }
    
     private void setDadosVasilhame_alterar()
    {
            vasilhame.setDescricao( txtVasilhame.getText() );
           
            vasilhame.setFkProduto( produtoDao.getProdutoByDescricao(   String.valueOf(    cmbProduto.getSelectedItem()) ) );
            vasilhame.setFkArmazem( armazemDao.getArmazemByDescricao (   String.valueOf(    cmbArmazem.getSelectedItem()) ) );
       
    }


    private void salvar()
    {
        if (!vazio_campo()) 
        {
               if( !vasilhameDao.exist_vasilhame(  getCodigoProduto(), getCodigoArmazem() ) )
               {
                   if ( !vasilhameDao.exist_vasilhame(  txtVasilhame.getText() ) )
                   {
                       
                         try {
                            vasilhame = new TbVasilhame();
                            setDadosVasilhame();
                            vasilhameDao.create(vasilhame);
                            limpar();
                            adicionar_tabela(   vasilhameDao.findTbVasilhameEntities() );
                            JOptionPane.showMessageDialog(null, "Dados salvos com sucesso!...");
                        } catch (Exception e) {
                            e.printStackTrace();
                            JOptionPane.showMessageDialog(null, "Erro ao salvar os dados","ERRO", JOptionPane.ERROR_MESSAGE);
                        }

                   } else {
                       JOptionPane.showMessageDialog(null, "Já existe um vasilhame  com esta descrição", "AVISO", JOptionPane.WARNING_MESSAGE);
                   }
                   
                
               }else  JOptionPane.showMessageDialog(null, "Erro: Este produto já contém vasilhame!","ERRO", JOptionPane.ERROR_MESSAGE);

        } else {
            txtVasilhame.requestFocus();
            JOptionPane.showMessageDialog(null, "Pf. digita a descrição do vasilhame", "AVISO", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void alterar()
    {        

        if (!vazio_campo()) 
        {
//            if( vasilhameDao.exist_vasilhame(  getCodigoProduto() ) )
//            { 
//                JOptionPane.showMessageDialog(null, "Este produto ja contém vssilhame", "AVISO", JOptionPane.WARNING_MESSAGE);           
//            }else{

                  try {
                       vasilhame =   vasilhameDao.findTbVasilhame(idVasilhame);
                       setDadosVasilhame_alterar();
                       vasilhameDao.edit(vasilhame);
                       limpar();
                       adicionar_tabela(   vasilhameDao.findTbVasilhameEntities());
                       JOptionPane.showMessageDialog(null, "Dados alterados com sucesso!...");

                   } catch (Exception e) {
                       JOptionPane.showMessageDialog(null, "Erro ao alterar os dados","ERRO", JOptionPane.ERROR_MESSAGE);
                   }
                  
           // }
            
        } else {
            txtVasilhame.requestFocus();
            JOptionPane.showMessageDialog(null, "Pf. digita a descrição do vasilhame", "AVISO", JOptionPane.WARNING_MESSAGE);
        }
           
    }
    
    public void adicionar_tabela(List<TbVasilhame> vasilhames) 
     {
            DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
            modelo.setRowCount(0);

            for (int i = 0; i < vasilhames.size(); i++) {

                modelo.addRow(new Object[]{

                    vasilhames.get(i).getPkVasilhame(),
                    vasilhames.get(i).getDescricao(),
                    vasilhames.get(i).getFkProduto().getDesignacao(),
                    vasilhames.get(i).getFkArmazem().getDesignacao()


                });
            }
    }
    
     
  private void setDadosTabela()
  {
        DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
            
        this.idVasilhame =   Integer.parseInt( String.valueOf( modelo.getValueAt(jTable1.getSelectedRow(), 0) ) ) ;
        txtVasilhame.setText(String.valueOf( modelo.getValueAt(jTable1.getSelectedRow(), 1) )   ); 
        
        cmbProduto.setModel( new DefaultComboBoxModel( conexao.getElementos("tb_produto", "designacao", false)   ) );
        String produto = String.valueOf( modelo.getValueAt(jTable1.getSelectedRow(), 2) ) ;        
      
//  
        TbProduto produto_vital = produtoDao.getProdutoByDescricao(produto);
        
        cmbCategoria.setSelectedItem(  produto_vital.getCodTipoProduto().getDesignacao());
        cmbArmazem.setSelectedItem(      modelo.getValueAt(   jTable1.getSelectedRow(), 3)  );
        cmbProduto.setSelectedItem(   produto );
        
             
        
        
        System.out.println("PRODUTO " +produto);
        System.out.println("TIPO PRODUTO " +produto_vital.getCodTipoProduto().getDesignacao());
        
  }
  
  
  private void setComboCategoria()
  {
                cmbCategoria.setModel(   new DefaultComboBoxModel(  (Vector)  categoriaDao.getAllCategoria()  ) );
  
  }
  
  private void limpar()
  {
        txtVasilhame.setText("");
//        txtPreco.setText("");
//        txtComposicao.setText("");
  
  }

  
    private void eliminar() 
    {
        try {
               vasilhame =   vasilhameDao.findTbVasilhame(idVasilhame);
                setDadosVasilhame();
                produtoDao.destroy(idVasilhame);
                adicionar_tabela(   vasilhameDao.findTbVasilhameEntities() );
                JOptionPane.showMessageDialog(null, "Dados eliminados com sucesso!...");
            }
        catch (Exception e) {
           JOptionPane.showMessageDialog(null, "Erro ao eliminar os dados","ERRO", JOptionPane.ERROR_MESSAGE);  
        }
    }



    public int getCodigoTipoProduto() 
    {
           return conexao.getCodigoPublico("tb_tipo_produto", String.valueOf(  cmbCategoria.getSelectedItem()));   
    }
    
    public int getCodigoProduto() 
    {
           return conexao.getCodigoPublico("tb_produto", String.valueOf(  cmbProduto.getSelectedItem()));   
    }

    
    private boolean vazio_campo()
    {
        return txtVasilhame.getText().equals("");
    }

    
//   private void setVasilhameExistente()
//   {
//        cmbVasilhamesExistentes.setModel( new DefaultComboBoxModel(  (Vector) vasilhameDao.getAllVasilhames()  )  );
//   }

    public int getCodigoArmazem() 
    {
           return conexao.getCodigoPublico("tb_armazem", String.valueOf(  cmbArmazem.getSelectedItem() ) );   
    }


    
}
