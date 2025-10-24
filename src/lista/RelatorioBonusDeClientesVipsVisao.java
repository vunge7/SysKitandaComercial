/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lista;


import java.sql.Connection;
import dao.ArmazemDao;
import dao.ClienteDao;
import dao.ItemVendaDao;
import dao.PrecoDao;
import dao.ProdutoDao;
import dao.UsuarioDao;
import dao.VendaDao;
import enties.util.RelatorioClienteBonusVipUtil;
import entity.TbProduto;
import entity.TbVenda;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import util.BDConexao;
import util.DVML;
import util.JPAEntityMannagerFactoryUtil;
import util.MetodosUtil;

/**
 *
 * @author Domingos Dala Vunge
 */
public class RelatorioBonusDeClientesVipsVisao extends javax.swing.JFrame {

    /**
     * Creates new form ListaUsuarioVisao
     */
    
    private EntityManagerFactory emf = JPAEntityMannagerFactoryUtil.em;
    private VendaDao vendaDao = new VendaDao(emf);
    private UsuarioDao usuarioDao = new UsuarioDao(emf);    
    private ProdutoDao produtoDao = new ProdutoDao(emf);
    private ItemVendaDao itemVendaDao = new ItemVendaDao(emf);
    private PrecoDao precoDao = new PrecoDao(emf);
    private ClienteDao clienteDao = new ClienteDao(emf);
    private ArmazemDao armazemDao = new ArmazemDao(emf);
    private BDConexao conexao;
//    private List<TbVenda> lista = null;
    private  double total_geral = 0;
    private List<RelatorioClienteBonusVipUtil> lista = new ArrayList<RelatorioClienteBonusVipUtil>();
    
    public RelatorioBonusDeClientesVipsVisao()  {
        
        
        initComponents();
        setResizable(false);
        setLocationRelativeTo(null);
        this.conexao = BDConexao.getInstancia();
        dcDataInicio.setDate( new Date() );
        dcDataFim.setDate( new Date() );
        cmbArmazem.setModel(new DefaultComboBoxModel(armazemDao.buscaTodos1()));
        //cmbClientes.setModel(new DefaultComboBoxModel((Vector) clienteDao.getAllClienteExceptoDiversos()));
        cmbClientes.setModel(new DefaultComboBoxModel((Vector) clienteDao.getAllCliente()));
//        DVML.activar_cmb_armazem(cmbArmazem);
        
       
    }

    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        lbData = new javax.swing.JLabel();
        lbData1 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        cmbArmazem = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        lbData3 = new javax.swing.JLabel();
        txtPercentagem = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        cmbClientes = new javax.swing.JComboBox<>();
        dcDataInicio = new com.toedter.calendar.JDateChooser();
        dcDataFim = new com.toedter.calendar.JDateChooser();
        rb_FR = new javax.swing.JRadioButton();
        rb_FT = new javax.swing.JRadioButton();
        rb_PP = new javax.swing.JRadioButton();
        rb_RC = new javax.swing.JRadioButton();
        lbData2 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        btnCancelar = new javax.swing.JButton();
        btnCancelar1 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        lb_total = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("...:::: DVML - RELATORIO DE BÔNUS PARA CLIENTES VIP´S::...");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        lbData.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 16)); // NOI18N
        lbData.setText("De");

        lbData1.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 16)); // NOI18N
        lbData1.setText("à");

        jLabel1.setText("Clientes :");

        cmbArmazem.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbArmazem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbArmazemActionPerformed(evt);
            }
        });

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/actualizar_1_32x32.png"))); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButton1ActionPerformed(evt);
            }
        });

        lbData3.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 16)); // NOI18N
        lbData3.setText("Percentagem :");

        txtPercentagem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtPercentagemActionPerformed(evt);
            }
        });

        jLabel3.setText("Armazém :");

        cmbClientes.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        dcDataInicio.addPropertyChangeListener(new java.beans.PropertyChangeListener()
        {
            public void propertyChange(java.beans.PropertyChangeEvent evt)
            {
                dcDataInicioPropertyChange(evt);
            }
        });
        dcDataInicio.addVetoableChangeListener(new java.beans.VetoableChangeListener()
        {
            public void vetoableChange(java.beans.PropertyChangeEvent evt)throws java.beans.PropertyVetoException
            {
                dcDataInicioVetoableChange(evt);
            }
        });

        dcDataFim.addPropertyChangeListener(new java.beans.PropertyChangeListener()
        {
            public void propertyChange(java.beans.PropertyChangeEvent evt)
            {
                dcDataFimPropertyChange(evt);
            }
        });
        dcDataFim.addVetoableChangeListener(new java.beans.VetoableChangeListener()
        {
            public void vetoableChange(java.beans.PropertyChangeEvent evt)throws java.beans.PropertyVetoException
            {
                dcDataFimVetoableChange(evt);
            }
        });

        rb_FR.setSelected(true);
        rb_FR.setText("FR");
        rb_FR.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                rb_FRActionPerformed(evt);
            }
        });

        rb_FT.setText("FT");
        rb_FT.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                rb_FTActionPerformed(evt);
            }
        });

        rb_PP.setText("PP");
        rb_PP.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                rb_PPActionPerformed(evt);
            }
        });

        rb_RC.setText("RC");
        rb_RC.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                rb_RCActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(lbData3, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtPercentagem, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lbData, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dcDataInicio, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbData1, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dcDataFim, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbClientes, javax.swing.GroupLayout.PREFERRED_SIZE, 293, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
                        .addComponent(cmbArmazem, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(rb_FR)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rb_FT)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rb_PP)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rb_RC)
                        .addGap(608, 608, 608))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(rb_FR)
                        .addComponent(rb_FT)
                        .addComponent(rb_PP)
                        .addComponent(rb_RC))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cmbArmazem)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(lbData1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lbData, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbData3, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtPercentagem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(cmbClientes))
                        .addComponent(dcDataInicio, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(dcDataFim, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jButton1))
                .addGap(22, 22, 22))
        );

        lbData2.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 36)); // NOI18N
        lbData2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbData2.setText("  RELATÓRIO  DE  BÔNUS  PARA  CLIENTES  VIP´s");

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

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

        jLabel2.setFont(new java.awt.Font("Lucida Grande", 1, 24)); // NOI18N
        jLabel2.setText("TOTAL");

        lb_total.setFont(new java.awt.Font("Lucida Grande", 1, 36)); // NOI18N
        lb_total.setText("0.0");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lb_total, javax.swing.GroupLayout.PREFERRED_SIZE, 283, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnCancelar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCancelar1)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCancelar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnCancelar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lb_total, javax.swing.GroupLayout.DEFAULT_SIZE, 44, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "Cód.Produto", "Produto ", "Qtd", "Preço Fábrica", "Bônus"
            }
        )
        {
            Class[] types = new Class []
            {
                java.lang.Object.class, java.lang.String.class, java.lang.Integer.class, java.lang.Double.class, java.lang.Double.class
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
        jScrollPane1.setViewportView(jTable1);
        if (jTable1.getColumnModel().getColumnCount() > 0)
        {
            jTable1.getColumnModel().getColumn(0).setMinWidth(0);
            jTable1.getColumnModel().getColumn(0).setMaxWidth(100);
            jTable1.getColumnModel().getColumn(2).setMinWidth(0);
            jTable1.getColumnModel().getColumn(2).setMaxWidth(100);
        }

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 393, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lbData2, javax.swing.GroupLayout.DEFAULT_SIZE, 1093, Short.MAX_VALUE)
                            .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(lbData2, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCancelar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelar1ActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_btnCancelar1ActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
      
        //procedimento_imprimir();
//       ResumoVendas rv = new ResumoVendas(dcDataInicio.getDate(), dcDataFim.getDate(), getCodigoArmazem());
//       ResumoVenda rv = new ResumoVenda(dcDataInicio.getDate(), dcDataFim.getDate(), getCodigoArmazem());
         ResumoVendas resumoVenda = new ResumoVendas( dcDataInicio.getDate(), dcDataFim.getDate(), getCodigoArmazem(), getTipoDocumento() );
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        procedimento_adicionar_tabela();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void txtPercentagemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPercentagemActionPerformed
        // TODO add your handling code here:
        procedimento_adicionar_tabela();
    }//GEN-LAST:event_txtPercentagemActionPerformed

    private void cmbArmazemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbArmazemActionPerformed
        // TODO add your handling code here:
        //adicionar_adicionar_tabela();
    }//GEN-LAST:event_cmbArmazemActionPerformed

    private void dcDataInicioPropertyChange(java.beans.PropertyChangeEvent evt)//GEN-FIRST:event_dcDataInicioPropertyChange
    {//GEN-HEADEREND:event_dcDataInicioPropertyChange
        // TODO add your handling code here:
    }//GEN-LAST:event_dcDataInicioPropertyChange

    private void dcDataInicioVetoableChange(java.beans.PropertyChangeEvent evt)throws java.beans.PropertyVetoException//GEN-FIRST:event_dcDataInicioVetoableChange
    {//GEN-HEADEREND:event_dcDataInicioVetoableChange
        // TODO add your handling code here:
        //verificarDataComeco();
    }//GEN-LAST:event_dcDataInicioVetoableChange

    private void dcDataFimPropertyChange(java.beans.PropertyChangeEvent evt)//GEN-FIRST:event_dcDataFimPropertyChange
    {//GEN-HEADEREND:event_dcDataFimPropertyChange
        // TODO add your handling code here:
    }//GEN-LAST:event_dcDataFimPropertyChange

    private void dcDataFimVetoableChange(java.beans.PropertyChangeEvent evt)throws java.beans.PropertyVetoException//GEN-FIRST:event_dcDataFimVetoableChange
    {//GEN-HEADEREND:event_dcDataFimVetoableChange
        // TODO add your handling code here:
        //verificarDataComeco();
    }//GEN-LAST:event_dcDataFimVetoableChange

    private void rb_FRActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_rb_FRActionPerformed
    {//GEN-HEADEREND:event_rb_FRActionPerformed
        // TODO add your handling code here:
        procedimento_adicionar_tabela();
    }//GEN-LAST:event_rb_FRActionPerformed

    private void rb_FTActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_rb_FTActionPerformed
    {//GEN-HEADEREND:event_rb_FTActionPerformed
        // TODO add your handling code here:
        procedimento_adicionar_tabela();
    }//GEN-LAST:event_rb_FTActionPerformed

    private void rb_PPActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_rb_PPActionPerformed
    {//GEN-HEADEREND:event_rb_PPActionPerformed
        // TODO add your handling code here:
        procedimento_adicionar_tabela();
    }//GEN-LAST:event_rb_PPActionPerformed

    private void rb_RCActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_rb_RCActionPerformed
    {//GEN-HEADEREND:event_rb_RCActionPerformed
        // TODO add your handling code here:
        procedimento_adicionar_tabela();
    }//GEN-LAST:event_rb_RCActionPerformed

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
            java.util.logging.Logger.getLogger(RelatorioBonusDeClientesVipsVisao.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(RelatorioBonusDeClientesVipsVisao.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(RelatorioBonusDeClientesVipsVisao.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(RelatorioBonusDeClientesVipsVisao.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new RelatorioBonusDeClientesVipsVisao().setVisible(true);
                } catch (Exception ex) {
                    Logger.getLogger(RelatorioBonusDeClientesVipsVisao.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnCancelar1;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox<String> cmbArmazem;
    private javax.swing.JComboBox<String> cmbClientes;
    private com.toedter.calendar.JDateChooser dcDataFim;
    private com.toedter.calendar.JDateChooser dcDataInicio;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel lbData;
    private javax.swing.JLabel lbData1;
    private javax.swing.JLabel lbData2;
    private javax.swing.JLabel lbData3;
    private javax.swing.JLabel lb_total;
    private javax.swing.JRadioButton rb_FR;
    private javax.swing.JRadioButton rb_FT;
    private javax.swing.JRadioButton rb_PP;
    private javax.swing.JRadioButton rb_RC;
    private javax.swing.JTextField txtPercentagem;
    // End of variables declaration//GEN-END:variables

    private void procedimento_adicionar_tabela() {
        
         DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
         modelo.setRowCount(0);
         
         try {
             
             List<TbProduto> lista = MetodosUtil.getAllProdutosByIdClienteAndIdArmazem(getData(  dcDataInicio.getDate() ), getData(  dcDataFim.getDate()), getCodigoArmazem(), getCodigoCliente(), conexao);
             RelatorioClienteBonusVipUtil clienteVip;
             double preco_bonus = 0, bonus = 0, percentagem = 0;
             
             
             if (  !txtPercentagem.getText().equals("") ) {
                    this.lista.clear();
                    modelo.setRowCount(0);
                    if (lista.size() !=0 ) {                        
                            percentagem = Double.parseDouble(  txtPercentagem.getText());
                            for (TbProduto object : lista) {

                                   clienteVip = new RelatorioClienteBonusVipUtil();
                                   clienteVip.setCod( object.getCodigo() );
                                   clienteVip.setProduto(object.getDesignacao() );
                                   clienteVip.setPreco_venda_fabrica(    MetodosUtil.getPreco( getData(  dcDataInicio.getDate() ), getData(  dcDataFim.getDate()), getCodigoArmazem(), getCodigoCliente(),object.getCodigo(), conexao)  );
                                   clienteVip.setQtd( MetodosUtil.getQTD( getData(  dcDataInicio.getDate() ), getData(  dcDataFim.getDate()), getCodigoArmazem(), getCodigoCliente(),object.getCodigo(), conexao));
                                   preco_bonus = ( clienteVip.getPreco_venda_fabrica()* (percentagem/100) );
                                   bonus = (preco_bonus*clienteVip.getQtd());
                                   clienteVip.setBonus( bonus );

                                   modelo.addRow( new Object[]{
                                       clienteVip.getCod(),
                                       clienteVip.getProduto(),
                                       clienteVip.getQtd(),
                                       clienteVip.getPreco_venda_fabrica(),
                                       clienteVip.getBonus()

                                   });
                                   this.lista.add(clienteVip);
                            }
                           lb_total.setText(   String.valueOf(  getTotal() )    );
                     
                 }else {
                      JOptionPane.showMessageDialog(null, "Não existe vendas registradas para o cliente seleccionado neste período", "AVISO", JOptionPane.WARNING_MESSAGE);
                }
                    
             }else JOptionPane.showMessageDialog(null, "Caro usário define a percentagem de ganho","AVISO", JOptionPane.WARNING_MESSAGE);

        } catch (Exception e) {
            e.printStackTrace();
             lb_total.setText("");
             JOptionPane.showMessageDialog(null, "Não há registro para esse armazém", DVML.DVML_COMERCIAL, JOptionPane.WARNING_MESSAGE);
        }
     
    }

    
    public int getCodigoArmazem() {
        return armazemDao.getArmazemByDescricao(cmbArmazem.getSelectedItem().toString()).getCodigo();
    }

    
    public int getCodigoCliente() {
        return clienteDao.getIdByDescricao(cmbClientes.getSelectedItem().toString());
    }

    
    
    private String getData(Date date) {
        
          return  (date.getYear() + 1900)
                  +"-" +(   getNumeroDoisDigitos( date.getMonth() + 1 )  )
                  +"-" + getNumeroDoisDigitos(date.getDate()) ;
          
    }
    
    private String getHora(Date date)
    {
          return   getNumeroDoisDigitos(  date.getHours() ) +":" 
                  + getNumeroDoisDigitos ( date.getMinutes() ) +":"
                  +getNumeroDoisDigitos(  date.getSeconds() );
    
    }

    private String getNumeroDoisDigitos(int  numero)
    {
        if (numero<10) {
              return "0" +numero;
        }
     
        return  String.valueOf(numero);
    
    }
    
    
    private double getTotal()
    {
            double  total  = 0;
            DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
            
            for (int i = 0; i < modelo.getRowCount(); i++) {
                 total = total +  Double.parseDouble(    jTable1.getValueAt( i , 4).toString()   );
            }
            
            return total;
    
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
