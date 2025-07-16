/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lista;

import dao.ArmazemDao;
import dao.ItemVendaDao;
import dao.PrecoDao;
import dao.UsuarioDao;
import dao.VendaDao;
import entity.TbProduto;
import entity.TbVenda;
import comercial.controller.AmortizacaoDividaController;
import hotel.controller.ClienteController;
import comercial.controller.VendasController;
import dao.AccessoArmazemDao;
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
import util.DVML.Abreviacao;
import util.JPAEntityMannagerFactoryUtil;
import util.MetodosUtil;
import util.tabela_manual.render.RenderTabelaFA;

/**
 *
 * @author Domingos Dala Vunge
 */
public class ListarRelatorioVendaData extends javax.swing.JFrame
{

    /**
     * Creates new form ListaUsuarioVisao
     */
    private EntityManagerFactory emf = JPAEntityMannagerFactoryUtil.em;
    private VendaDao vendaDao = new VendaDao( emf );
    private AccessoArmazemDao accessoArmazemDao = new AccessoArmazemDao( emf );
    private TbVenda venda;
    private UsuarioDao usuarioDao = new UsuarioDao( emf );
    private ItemVendaDao itemVendaDao = new ItemVendaDao( emf );
    private PrecoDao precoDao = new PrecoDao( emf );
    private ArmazemDao armazemDao = new ArmazemDao( emf );
    private double total_geral = 0;
    private List<TbVenda> lista = null;
    private BDConexao conexao;
    private ClienteController clienteController;
    private VendasController vendasController;
    private Vector<String> lista_all_clientes;
    private AmortizacaoDividaController amortizacaoDividaController;

    public ListarRelatorioVendaData( BDConexao conexao, int idUser )
    {

        initComponents();
        setResizable( false );
        setLocationRelativeTo( null );
//        tabela_factura.setDefaultRenderer( Object.class, new RenderTabelaFA() );
        this.conexao = conexao;
        amortizacaoDividaController = new AmortizacaoDividaController( conexao );
        vendasController = new VendasController( conexao );
        dcDataInicio.setDate( new Date() );
        dcDataFim.setDate( new Date() );
        cmbArmazem.setModel( new DefaultComboBoxModel( accessoArmazemDao.getAllArmazemByIdUSuario( idUser ) ) );
//        cmbArmazem.setModel( new DefaultComboBoxModel( armazemDao.buscaTodos3() ) );
//        configurar_clientes();
//        metodo_radio();

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        buttonGroup3 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        lbData = new javax.swing.JLabel();
        dcDataInicio = new com.toedter.calendar.JDateChooser();
        lbData1 = new javax.swing.JLabel();
        dcDataFim = new com.toedter.calendar.JDateChooser();
        jLabel1 = new javax.swing.JLabel();
        cmbArmazem = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        txtRefDoc = new javax.swing.JTextField();
        txtRefDoc1 = new javax.swing.JTextField();
        lbData2 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        btnCancelar = new javax.swing.JButton();
        btnCancelar1 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        lb_total = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel8 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tabela_factura_geral = new javax.swing.JTable();
        jPanel6 = new javax.swing.JPanel();
        ck_relatorio_normal = new javax.swing.JCheckBox();
        jLabel3 = new javax.swing.JLabel();
        ck_ordemFR = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("...:::: DVML - RELATORIO DIARIO::...");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        lbData.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 16)); // NOI18N
        lbData.setText("De");

        lbData1.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 16)); // NOI18N
        lbData1.setText("à");

        jLabel1.setText("Armazém");

        cmbArmazem.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/actualizar_1_32x32.png"))); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButton1ActionPerformed(evt);
            }
        });

        txtRefDoc.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtRefDocActionPerformed(evt);
            }
        });

        txtRefDoc1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtRefDoc1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbData, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtRefDoc)
                    .addComponent(dcDataInicio, javax.swing.GroupLayout.DEFAULT_SIZE, 133, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(lbData1, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(dcDataFim, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbArmazem, 0, 227, Short.MAX_VALUE))
                    .addComponent(txtRefDoc1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cmbArmazem))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(dcDataInicio, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lbData1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(dcDataFim, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lbData, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtRefDoc, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtRefDoc1, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(7, 7, 7))
            .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        lbData2.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 30)); // NOI18N
        lbData2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbData2.setText("ACTUALIZAÇÃO DE VENDAS");

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
                .addComponent(lb_total, javax.swing.GroupLayout.PREFERRED_SIZE, 379, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnCancelar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCancelar1)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCancelar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnCancelar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lb_total, javax.swing.GroupLayout.PREFERRED_SIZE, 44, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jTabbedPane1.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                jTabbedPane1MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt)
            {
                jTabbedPane1MouseEntered(evt);
            }
        });

        tabela_factura_geral.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "Doc. Nº", "Cliente ", "Data", "Hora", "Usuário", "Total", "Ref. DOC"
            }
        )
        {
            Class[] types = new Class []
            {
                java.lang.Object.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean []
            {
                false, false, false, false, false, false, false
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
        tabela_factura_geral.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                tabela_factura_geralMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(tabela_factura_geral);

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 1269, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 468, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("GERAL", jPanel8);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 513, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(76, 76, 76))
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        buttonGroup2.add(ck_relatorio_normal);
        ck_relatorio_normal.setSelected(true);
        ck_relatorio_normal.setText("Relatório Normal");

        jLabel3.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        jLabel3.setText("Forma de Impressão");

        buttonGroup2.add(ck_ordemFR);
        ck_ordemFR.setText("Relatorio ordenado");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ck_relatorio_normal)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(ck_ordemFR, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(ck_ordemFR))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ck_relatorio_normal)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lbData2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbData2, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 529, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCancelar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelar1ActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_btnCancelar1ActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed

        procedimento_imprimir();


    }//GEN-LAST:event_btnCancelarActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        adicionar_relatorio_geral();
//        new ExtratoContaClienteController( conexao ).gerarExtratoAutomatico( dcDataInicio.getDate(), dcDataFim.getDate() );


    }//GEN-LAST:event_jButton1ActionPerformed

    private void txtRefDocActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtRefDocActionPerformed
    {//GEN-HEADEREND:event_txtRefDocActionPerformed
//        adicionar_tabela_cod_fact();
    }//GEN-LAST:event_txtRefDocActionPerformed

    private void txtRefDoc1ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtRefDoc1ActionPerformed
    {//GEN-HEADEREND:event_txtRefDoc1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtRefDoc1ActionPerformed

    private void jTabbedPane1MouseEntered(java.awt.event.MouseEvent evt)//GEN-FIRST:event_jTabbedPane1MouseEntered
    {//GEN-HEADEREND:event_jTabbedPane1MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_jTabbedPane1MouseEntered

    private void jTabbedPane1MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_jTabbedPane1MouseClicked
    {//GEN-HEADEREND:event_jTabbedPane1MouseClicked
        // TODO add your handling code here:
//        adicionar_relatorio_recibo();
    }//GEN-LAST:event_jTabbedPane1MouseClicked

    private void tabela_factura_geralMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_tabela_factura_geralMouseClicked
    {//GEN-HEADEREND:event_tabela_factura_geralMouseClicked
        if ( evt.getClickCount() > 1 )
        {

            reimprimir_GERAL();

        }
    }//GEN-LAST:event_tabela_factura_geralMouseClicked

//        private void busca_factura(String ref_doc) {
//      
//        try {
//            TbVenda venda = vendaDao.findByCodFactReemprensaoGeral( ref_doc );
////            Integer last_venda = vendasController.getLastVenda().getCodigo();
//            String cod_fact = txtRefDoc.getText();
////              String cod_factura = vendasController.getLastVendaCodFact(cod_fact ).getCodFact();   
////              venda = (TbVenda) vendasController.getLastVendaCodFact(cod_fact );
//              TbVenda venda_local = ( TbVenda ) vendasController.getLastVendaCodFact(cod_fact );
//
//                    try 
//                    {       
//  
////                         dcDataSaida.setDate(this.saidasProdutos.getDataSaida());
//                         txtRefDoc1.setText( venda_local.getNomeCliente() );
//                        preencher_tabela();
//                       
//                    }
//                    catch (Exception e) {
//                       // e.printStackTrace();
//                      
//                    }
//
//            
//        } catch (Exception e) {
////            limpar_dados();
//             JOptionPane.showMessageDialog(null, "Possivelmente esta saida nao existe!... Ou ja foi elimnada!...", "DVML-COMERCIAL, Lda", JOptionPane.WARNING_MESSAGE);            
//        }
//             
//    
//    }
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

        /* Create and display the form */
        java.awt.EventQueue.invokeLater( new Runnable()
        {
            public void run()
            {
                try
                {
                    new ListarRelatorioVendaData( new BDConexao(), 15 ).setVisible( true );
                }
                catch ( Exception ex )
                {
                    Logger.getLogger( ListarRelatorioVenda.class.getName() ).log( Level.SEVERE, null, ex );
                }
            }
        } );
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnCancelar1;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.ButtonGroup buttonGroup3;
    private javax.swing.JCheckBox ck_ordemFR;
    private javax.swing.JCheckBox ck_relatorio_normal;
    private javax.swing.JComboBox<String> cmbArmazem;
    private com.toedter.calendar.JDateChooser dcDataFim;
    private com.toedter.calendar.JDateChooser dcDataInicio;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lbData;
    private javax.swing.JLabel lbData1;
    private javax.swing.JLabel lbData2;
    private javax.swing.JLabel lb_total;
    private javax.swing.JTable tabela_factura_geral;
    private javax.swing.JTextField txtRefDoc;
    private javax.swing.JTextField txtRefDoc1;
    // End of variables declaration//GEN-END:variables

//    private void adicionar_tabela()
//    {
//
//        configurar_clientes();
//
//        DefaultTableModel modelo = null;
//
//        try
//        {
//            int selectedIndex = jTabbedPane1.getSelectedIndex();
//
//            //CASO DA FACTURA RECIBO
//            if ( selectedIndex == 0 )
//            {
//                ck_estrato_cliente.setSelected( false ); //quando o dcumento FR nao e necessario Extrato do Cliente
//                ck_estrato_cliente.setVisible( false );
//                String codFact = txtRefDoc.getText();
//                modelo = ( DefaultTableModel ) tabela_factura_recibo.getModel();
//                modelo.setRowCount( 0 );
//                lista = vendaDao.getAllVendaByBetweenDataAndArmazemAndDocumento( dcDataInicio.getDate(), dcDataFim.getDate(), getCodigoArmazem(), DVML.DOC_FACTURA_RECIBO_FR );
////                lista = vendaDao.getVendaByCodFact( codFact);
//
//                if ( lista != null )
//                {
//                    for ( TbVenda object : lista )
//                    {
//
//                        modelo.addRow( new Object[]
//                        {
//                            object.getCodFact(),
//                            getNomeCliente( object ),
//                            getData( object.getDataVenda() ),
//                            getHora( object.getHora() ),
//                            object.getCodigoUsuario().getNome(),
//                            object.getTotalVenda(),
//
//                        } );
//
//                    }
////                    lb_total.setText( formatarComoMoeda( getTotal( tabela_factura_recibo ) ) );
//                }
//
//            } //CASO DA FACTURA
//            else if ( selectedIndex == 1 )
//            {
//                adicionar_relatorio_factura();
//            }
//            else if ( selectedIndex == 2 )
//            {
//                adicionar_relatorio_nota();
//            }
//            else if ( selectedIndex == 3 )
//            {
//                adicionar_relatorio_recibo();
//            }
//            else if ( selectedIndex == 4 )
//            {
//                adicionar_relatorio_geral();
//            }
//
//        }
//        catch ( Exception e )
//        {
//            e.printStackTrace();
//            lb_total.setText( "" );
//            JOptionPane.showMessageDialog( null, "Não há registro para esse armazém", DVML.DVML_COMERCIAL, JOptionPane.WARNING_MESSAGE );
//        }
//
//    }

    public int getCodigoArmazem()
    {
        return armazemDao.getArmazemByDescricao( cmbArmazem.getSelectedItem().toString() ).getCodigo();
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

    private BigDecimal getTotal( JTable tabela )
    {
        BigDecimal total = new BigDecimal( 0.0 );
        DefaultTableModel modelo = ( DefaultTableModel ) tabela.getModel();

        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {
            total = total.add( new BigDecimal( tabela.getValueAt( i, 5 ).toString() ) ).setScale( 2, RoundingMode.CEILING );

        }
        System.out.println( "Total: " + total.doubleValue() );

        return total;

    }

    private int getTipoDocumento()
    {
        int selectedIndex = jTabbedPane1.getSelectedIndex();
        if ( selectedIndex == 0 )
        {
            return DVML.DOC_FACTURA_RECIBO_FR;
        }
        else if ( selectedIndex == 1 )
        {
            return DVML.DOC_FACTURA_FT;
        }
        else if ( selectedIndex == 2 )
        {
            return DVML.DOC_NOTA_CREDITO_NC;
        }
        else if ( selectedIndex == 3 )
        {
            return DVML.DOC_RECIBO_RC;
        }
//        else if ( selectedIndex == 3 )
//        {
//            return DVML.DOC_FACTURA_FT;
//        }
        return 0;

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

//    private void configurar_clientes()
//    {
//        clienteController = new ClienteController( conexao );
//        lista_all_clientes = clienteController.listarTodosDaVenda( dcDataInicio.getDate(), dcDataFim.getDate() );
//        cmbCliente.setModel( new DefaultComboBoxModel( lista_all_clientes ) );
//    }

//    private int getCodigoCliente()
//    {
//        try
//        {
//            return clienteController.findByNome( cmbCliente.getSelectedItem().toString() ).getCodigo();
//        }
//        catch ( Exception e )
//        {
//            e.printStackTrace();
//        }
//        return 0;
//    }

    private void procedimento_imprimir()
    {
//
//        if ( ck_relatorio_normal.isSelected() || ck_mapa_iva.isSelected() )
//        {
//            ResumoVenda resumoVenda = new ResumoVenda( dcDataInicio.getDate(), dcDataFim.getDate(), getCodigoArmazem(), this.lista, getTipoDocumento(), ck_mapa_iva.isSelected() );
//        }
//        if ( ck_ordemFR.isSelected() )
//        {
//
//            ResumoVenda resumoVenda = new ResumoVenda( dcDataInicio.getDate(), dcDataFim.getDate(), getCodigoArmazem(), this.lista, getTipoDocumento(), ck_mapa_iva.isSelected(), ck_ordemFR.isSelected() );
//        }
//        else if ( ck_mapa_iva.isSelected() )
//        {
//            ResumoVenda resumoVenda = new ResumoVenda( dcDataInicio.getDate(), dcDataFim.getDate(), getCodigoArmazem(), this.lista, getTipoDocumento(), true );
//        }
//        else if ( ck_estrato_cliente.isSelected() )
//        {
//            new ExtratoContaReport( getCodigoCliente(), dcDataInicio.getDate(), dcDataFim.getDate() );
//        }

    }
//
//    private void metodo_radio()
//    {
//        if ( rb_por_cliente.isSelected() )
//        {
//            cmbCliente.setModel( new DefaultComboBoxModel( lista_all_clientes ) );
//            cmbCliente.setVisible( true );
//        }
//        else
//        {
//            cmbCliente.setVisible( false );
//        }
//        adicionar_relatorio_factura();
////        adicionar_relatorio_recibo();
//    }
//
//    private void adicionar_relatorio_factura()
//    {
//        ck_estrato_cliente.setVisible( true );
////        rb_todos_clientes.setSelected( true);
//        lista = rb_todos_clientes.isSelected() ? vendaDao.getAllVendaByBetweenDataAndArmazemAndDocumento( dcDataInicio.getDate(), dcDataFim.getDate(), getCodigoArmazem(), DVML.DOC_FACTURA_FT ) : vendaDao.getAllVendaByBetweenDataAndArmazemAndDocumentoAndCliente( dcDataInicio.getDate(), dcDataFim.getDate(), getCodigoArmazem(), DVML.DOC_FACTURA_FT, getCodigoCliente() );
////        lista = vendaDao.getAllVendaByBetweenDataAndArmazemAndDocumento( dcDataInicio.getDate(), dcDataFim.getDate(), getCodigoArmazem(), DVML.DOC_FACTURA_FT );
//        DefaultTableModel modelo = ( DefaultTableModel ) tabela_factura.getModel();
//        modelo.setRowCount( 0 );
//        if ( lista != null )
//        {
//            for ( TbVenda object : lista )
//            {
//                int diferencaData = MetodosUtil.getDiferencaDias( object.getDataVencimento(), new Date(), conexao );
////                BigDecimal valor_pago = ( !Objects.isNull( VendaDao.getTotalPagoByCodFact( object.getCodFact(), conexao ) ) ? VendaDao.getTotalPagoByCodFact( object.getCodFact(), conexao ) : new BigDecimal( 0.0 ) );
//                BigDecimal valor_pago = new BigDecimal( amortizacaoDividaController.getValorAtribuidoByCodFact( object.getCodFact() ) );
////                System.out.println( "VALOR PAGO = " + valor_pago );
//
//                modelo.addRow( new Object[]
//                {
//                    object.getCodFact(),
//                    getNomeCliente( object ),
//                    getData( object.getDataVenda() ),
//                    getHora( object.getHora() ),
//                    object.getCodigoUsuario().getNome(),
//                    //                    new BigDecimal( object.getTotalVenda() ).setScale( 2, RoundingMode.CEILING ),
//                    new BigDecimal( object.getTotalVenda().doubleValue() ).setScale( 2, RoundingMode.CEILING ),
//                    valor_pago,
//                    //                            new BigDecimal( object.getTotalVenda() ).subtract( valor_pago ).setScale( 2, RoundingMode.CEILING )
//                    valor_pago.subtract( new BigDecimal( object.getTotalVenda().doubleValue() ) ).setScale( 2, RoundingMode.FLOOR ),
//                    //                    valor_pago.subtract( new BigDecimal( object.getTotalVenda() ) ).setScale( 2, RoundingMode.FLOOR ),
//                    getData( object.getDataVencimento() ),
//                    diferencaData,
//
//                } );
//
//            }
//            lb_total.setText( formatarComoMoeda( getTotal( tabela_factura ).doubleValue() ) );
//        }
//    }

//    private void adicionar_relatorio_nota()
//    {
//
////        lista = rb_todos_clientes.isSelected() ? vendaDao.getAllNotasByBetweenDataAndArmazemAndDocumento( dcDataInicio.getDate(), dcDataFim.getDate(), getCodigoArmazem(), DVML.DOC_RECIBO_RC) : vendaDao.getAllNotaByBetweenDataAndArmazemAndDocumentoAndCliente( dcDataInicio.getDate(), dcDataFim.getDate(), getCodigoArmazem(), DVML.DOC_RECIBO_RC, getCodigoCliente() );
//        ck_estrato_cliente.setSelected( false ); //quando o dcumento FR nao e necessario Extrato do Cliente
//        ck_estrato_cliente.setVisible( false );
//        String codFact = txtRefDoc.getText();
//        DefaultTableModel modelo = ( DefaultTableModel ) tabela_factura_nc.getModel();
//        modelo.setRowCount( 0 );
//        lista = vendaDao.getAllVendaByBetweenDataAndArmazemAndDocumento( dcDataInicio.getDate(), dcDataFim.getDate(), getCodigoArmazem(), DVML.DOC_NOTA_CREDITO_NC );
////                lista = vendaDao.getVendaByCodFact( codFact);
//
//        if ( lista != null )
//        {
//            for ( TbVenda object : lista )
//            {
//
//                modelo.addRow( new Object[]
//                {
//                    object.getCodFact(),
//                    getNomeCliente( object ),
//                    getData( object.getDataVenda() ),
//                    getHora( object.getHora() ),
//                    object.getCodigoUsuario().getNome(),
//                    object.getTotalVenda(),
//
//                } );
//
//            }
//            lb_total.setText( formatarComoMoeda( getTotal( tabela_factura_nc ) ) );
//        }
//    }

//    private void adicionar_relatorio_recibo()
//    {
//
////        lista = rb_todos_clientes.isSelected() ? vendaDao.getAllNotasByBetweenDataAndArmazemAndDocumento( dcDataInicio.getDate(), dcDataFim.getDate(), getCodigoArmazem(), DVML.DOC_RECIBO_RC) : vendaDao.getAllNotaByBetweenDataAndArmazemAndDocumentoAndCliente( dcDataInicio.getDate(), dcDataFim.getDate(), getCodigoArmazem(), DVML.DOC_RECIBO_RC, getCodigoCliente() );
//        ck_estrato_cliente.setSelected( false ); //quando o dcumento FR nao e necessario Extrato do Cliente
//        ck_estrato_cliente.setVisible( false );
//        String codFact = txtRefDoc.getText();
//        DefaultTableModel modelo = ( DefaultTableModel ) tabela_recibo.getModel();
//        modelo.setRowCount( 0 );
//        lista = vendaDao.getAllVendaByBetweenDataAndArmazemAndDocumento( dcDataInicio.getDate(), dcDataFim.getDate(), getCodigoArmazem(), DVML.DOC_RECIBO_RC );
////                lista = vendaDao.getVendaByCodFact( codFact);
//
//        if ( lista != null )
//        {
//            for ( TbVenda object : lista )
//            {
//
//                modelo.addRow( new Object[]
//                {
//                    object.getCodFact(),
//                    getNomeCliente( object ),
//                    getData( object.getDataVenda() ),
//                    getHora( object.getHora() ),
//                    object.getCodigoUsuario().getNome(),
//                    object.getTotalVenda(),
//
//                } );
//
//            }
//            lb_total.setText( formatarComoMoeda( getTotal( tabela_recibo ) ) );
//        }
//    }

//    private void adicionar_relatorio_recibo1()
//    {
////        configurar_clientes();
//        ck_estrato_cliente.setVisible( true );
//        lista = rb_todos_clientes.isSelected() ? vendaDao.getAllVendaByBetweenDataAndArmazemAndDocumento( dcDataInicio.getDate(), dcDataFim.getDate(), getCodigoArmazem(), DVML.DOC_RECIBO_RC ) : vendaDao.getAllVendaByBetweenDataAndArmazemAndDocumentoAndCliente( dcDataInicio.getDate(), dcDataFim.getDate(), getCodigoArmazem(), DVML.DOC_RECIBO_RC, getCodigoCliente() );
//
////        lista = rb_todos_clientes.isSelected() ? vendaDao.getAllNotasByBetweenDataAndArmazemAndDocumento( dcDataInicio.getDate(), dcDataFim.getDate(), getCodigoArmazem(), DVML.DOC_RECIBO_RC) : vendaDao.getAllNotaByBetweenDataAndArmazemAndDocumentoAndCliente( dcDataInicio.getDate(), dcDataFim.getDate(), getCodigoArmazem(), DVML.DOC_RECIBO_RC, getCodigoCliente() );
////        ck_estrato_cliente.setSelected( false ); //quando o dcumento FR nao e necessario Extrato do Cliente
//        ck_estrato_cliente.setVisible( false );
//        String codFact = txtRefDoc.getText();
//        DefaultTableModel modelo = ( DefaultTableModel ) tabela_recibo.getModel();
//        modelo.setRowCount( 0 );
////        lista = vendaDao.getAllVendaByBetweenDataAndArmazemAndDocumento( dcDataInicio.getDate(), dcDataFim.getDate(), getCodigoArmazem(), DVML.DOC_RECIBO_RC );
////                lista = vendaDao.getVendaByCodFact( codFact);
//
//        if ( lista != null )
//        {
//            for ( TbVenda object : lista )
//            {
//
//                modelo.addRow( new Object[]
//                {
//                    object.getCodFact(),
//                    getNomeCliente( object ),
//                    getData( object.getDataVenda() ),
//                    getHora( object.getHora() ),
//                    object.getCodigoUsuario().getNome(),
//                    object.getTotalVenda(),
//
//                } );
//
//            }
//            lb_total.setText( formatarComoMoeda( getTotal( tabela_recibo ) ) );
//        }
//    }
//    private void adicionar_relatorio_recibo()
//    {
//        ck_estrato_cliente.setVisible( true );
//        lista = rb_todos_clientes.isSelected() ? vendaDao.getAllVendaByBetweenDataAndArmazemAndDocumento( dcDataInicio.getDate(), dcDataFim.getDate(), getCodigoArmazem(), DVML.DOC_RECIBO_RC ) : vendaDao.getAllVendaByBetweenDataAndArmazemAndDocumentoAndCliente( dcDataInicio.getDate(), dcDataFim.getDate(), getCodigoArmazem(), DVML.DOC_RECIBO_RC, getCodigoCliente() );
////        lista = vendaDao.getAllVendaByBetweenDataAndArmazemAndDocumento( dcDataInicio.getDate(), dcDataFim.getDate(), getCodigoArmazem(), DVML.DOC_FACTURA_FT );
//        DefaultTableModel modelo = ( DefaultTableModel ) tabela_recibo.getModel();
//        modelo.setRowCount( 0 );
//        if ( lista != null )
//        {
//            for ( TbVenda object : lista )
//            {
//                int diferencaData = MetodosUtil.getDiferencaDias( object.getDataVencimento(), new Date(), conexao );
////                BigDecimal valor_pago = ( !Objects.isNull( VendaDao.getTotalPagoByCodFact( object.getCodFact(), conexao ) ) ? VendaDao.getTotalPagoByCodFact( object.getCodFact(), conexao ) : new BigDecimal( 0.0 ) );
//                BigDecimal valor_pago = new BigDecimal( amortizacaoDividaController.getValorAtribuidoByCodFact( object.getCodFact() ) );
////                System.out.println( "VALOR PAGO = " + valor_pago );
//
//                modelo.addRow( new Object[]
//                {
//                    object.getCodFact(),
//                    getNomeCliente( object ),
//                    getData( object.getDataVenda() ),
//                    getHora( object.getHora() ),
//                    object.getCodigoUsuario().getNome(),
//                    //                    new BigDecimal( object.getTotalVenda() ).setScale( 2, RoundingMode.CEILING ),
//                    new BigDecimal( object.getTotalVenda().doubleValue() ).setScale( 2, RoundingMode.CEILING ),
//                    valor_pago,
//                    //                            new BigDecimal( object.getTotalVenda() ).subtract( valor_pago ).setScale( 2, RoundingMode.CEILING )
//                    valor_pago.subtract( new BigDecimal( object.getTotalVenda().doubleValue() ) ).setScale( 2, RoundingMode.FLOOR ),
//                    //                    valor_pago.subtract( new BigDecimal( object.getTotalVenda() ) ).setScale( 2, RoundingMode.FLOOR ),
//                    getData( object.getDataVencimento() ),
//                    diferencaData,
//
//                } );
//
//            }
//            lb_total.setText( formatarComoMoeda( getTotal( tabela_recibo ).doubleValue() ) );
//        }
//    }

    private void adicionar_relatorio_geral()
    {
//        ck_estrato_cliente.setVisible( true );
        lista = vendaDao.getAllVendasGeraisByBetweenDataAndArmazemAndDocumento( dcDataInicio.getDate(), dcDataFim.getDate(), getCodigoArmazem() );

        DefaultTableModel modelo = ( DefaultTableModel ) tabela_factura_geral.getModel();
        modelo.setRowCount( 0 );
        if ( lista != null )
        {
            for ( TbVenda object : lista )
            {
//                BigDecimal valor_pago = ( !Objects.isNull( VendaDao.getTotalPagoByCodFact( object.getCodFact(), conexao ) ) ? VendaDao.getTotalPagoByCodFact( object.getCodFact(), conexao ) : new BigDecimal( 0.0 ) );
                BigDecimal valor_pago = ( !Objects.isNull( VendaDao.getTotalPagoByCodFact( object.getCodFact(), conexao ) ) ? VendaDao.getTotalPagoByCodFact( object.getCodFact(), conexao ) : new BigDecimal( 0.0 ) );
                System.out.println( "VALOR PAGO = " + valor_pago );
                modelo.addRow( new Object[]
                {
                    object.getCodFact(),
                    getNomeCliente( object ),
                    getData( object.getDataVenda() ),
                    getHora( object.getHora() ),
                    object.getCodigoUsuario().getNome(),
                    new BigDecimal( object.getTotalVenda().doubleValue() ).setScale( 2, RoundingMode.CEILING ),
                    object.getRefCodFact(),
                    valor_pago,
                    //                            new BigDecimal( object.getTotalVenda() ).subtract( valor_pago ).setScale( 2, RoundingMode.CEILING )
                    valor_pago.subtract( new BigDecimal( object.getTotalVenda().doubleValue() ) ).setScale( 2, RoundingMode.FLOOR )

                } );

            }
            lb_total.setText( formatarComoMoeda( getTotal( tabela_factura_geral ).doubleValue() ) );
        }
    }


    private void procedimento_reimprimir_FR( String ref_doc )
    {

        HashMap hashMap = new HashMap();
        TbVenda venda = vendaDao.findByCodFactReemprensao( ref_doc );

        if ( venda != null )
        {

            Abreviacao abreviacao = DVML.getAbreviacao( venda.getFkDocumento().getPkDocumento() );
            abreviacao = DVML.Abreviacao.FR_A4;
            List<TbProduto> lista_produto_isentos = new ArrayList<>();
            lista_produto_isentos = MetodosUtil.getProdutosIsentos( venda.getTbItemVendaList() );
            String motivos_isentos = MetodosUtil.getMotivoIsensaoProdutos( lista_produto_isentos );
            ListaVenda2 listaVenda2 = new ListaVenda2( venda.getCodigo(), abreviacao, false, false, DVML.SEGUNDA_VIA_CONFORMIDADE_COM_ORIGINAL, motivos_isentos );
        }
        else
        {
            JOptionPane.showMessageDialog( null, "Atenção\nO Documento não existe na base de dados. \nObs: Verifique a referência. " );
        }

    }



    private void reimprimir_GERAL()
    {
        DefaultTableModel modelo = ( DefaultTableModel ) tabela_factura_geral.getModel();
        int selectedRow = tabela_factura_geral.getSelectedRow();
        String codRef = modelo.getValueAt( selectedRow, 0 ).toString();

        procedimento_reimprimir_GERAL( codRef );
    }

    private void procedimento_reimprimir_GERAL( String ref_doc )
    {

        HashMap hashMap = new HashMap();
        TbVenda venda = vendaDao.findByCodFactReemprensaoGeral( ref_doc );

        if ( venda != null )
        {

            Abreviacao abreviacao = DVML.getAbreviacao( venda.getFkDocumento().getPkDocumento() );
            Abreviacao abreviacao_nota_credito = DVML.getAbreviacao( venda.getFkDocumento().getPkDocumento() );
            abreviacao_nota_credito = DVML.Abreviacao.NC;
            List<TbProduto> lista_produto_isentos = new ArrayList<>();
            lista_produto_isentos = MetodosUtil.getProdutosIsentos( venda.getTbItemVendaList() );
            String motivos_isentos = MetodosUtil.getMotivoIsensaoProdutos( lista_produto_isentos );

            if ( venda.getStatusEliminado().equals( "ANULADO" ) )
            {
                ListaNotasDebito listaVendaDebito = new ListaNotasDebito( venda.getCodigo(), abreviacao_nota_credito, false, true, hashMap, motivos_isentos );
            }
            else
            {
                ListaVendaGeral listaVendaGeral = new ListaVendaGeral( venda.getCodigo(), abreviacao, false, false, DVML.SEGUNDA_VIA_CONFORMIDADE_COM_ORIGINAL, motivos_isentos );
            }

        }
        else
        {
            JOptionPane.showMessageDialog( null, "Atenção\nO Documento não existe na base de dados. \nObs: Verifique a referência. " );
        }

    }

//    private void adicionar_tabela_cod_fact()
//    {
//
//        configurar_clientes();
//
//        DefaultTableModel modelo = null;
//
//        try
//        {
//            int selectedIndex = jTabbedPane1.getSelectedIndex();
//
//            //CASO DA FACTURA RECIBO
//            if ( selectedIndex == 0 )
//            {
//                ck_estrato_cliente.setSelected( false ); //quando o dcumento FR nao e necessario Extrato do Cliente
//                ck_estrato_cliente.setVisible( false );
//                String codFact = txtRefDoc.getText();
//                modelo = ( DefaultTableModel ) tabela_factura_recibo.getModel();
//                modelo.setRowCount( 0 );
////                lista = vendaDao.getAllVendaByBetweenDataAndArmazemAndDocumento( dcDataInicio.getDate(), dcDataFim.getDate(), getCodigoArmazem(), DVML.DOC_FACTURA_RECIBO_FR );
//                lista = vendaDao.getVendaByCodFact( codFact );
//
//                if ( lista != null )
//                {
//                    for ( TbVenda object : lista )
//                    {
//
//                        modelo.addRow( new Object[]
//                        {
//                            object.getCodFact(),
//                            getNomeCliente( object ),
//                            getData( object.getDataVenda() ),
//                            getHora( object.getHora() ),
//                            object.getCodigoUsuario().getNome(),
//                            object.getTotalVenda(),
//
//                        } );
//
//                    }
//                    lb_total.setText( formatarComoMoeda( getTotal( tabela_factura_recibo ) ) );
//                }
//
//            } //CASO DA FACTURA
//            else if ( selectedIndex == 1 )
//            {
//                adicionar_relatorio_factura();
//            }
//            else if ( selectedIndex == 2 )
//            {
//                adicionar_relatorio_nota();
//            }
//            else if ( selectedIndex == 3 )
//            {
//                adicionar_relatorio_recibo();
//            }
//            else if ( selectedIndex == 4 )
//            {
//                adicionar_relatorio_geral();
//            }
//
//        }
//        catch ( Exception e )
//        {
//            e.printStackTrace();
//            lb_total.setText( "" );
//            JOptionPane.showMessageDialog( null, "Não há registro para esse armazém", DVML.DVML_COMERCIAL, JOptionPane.WARNING_MESSAGE );
//        }
//
//    }

}
