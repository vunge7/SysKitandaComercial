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

import dao.TipoSubsidioDao;
import dao.FuncionarioDao;
import entity.TbSalario;
import entity.TbSubsidios;
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

/**
 * *
 * @author Domingos Dala Vunge
 */
public class TipoSubsidioVisao extends javax.swing.JFrame
{

    private EntityManagerFactory emf = JPAEntityMannagerFactoryUtil.em;
    private FuncionarioDao professoresDao = new FuncionarioDao( emf );
    private TipoSubsidioDao tipoSubsidioDao = new TipoSubsidioDao( emf );
    private TbSubsidios subsidios;
    private static String DVML_COMERCIAL = "DVML-COMERCIA, Lda";
    private DefaultTableModel modelo;
    private int cod_subsidios = 0;
    private int linha_actual = -1;
    private boolean status_eliminado = false;

    public TipoSubsidioVisao()
    {

        initComponents();
        setLocationRelativeTo( null );

        try
        {
            busca_subsidios();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }

    }

    @SuppressWarnings( "unchecked" )
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        jPanel3 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        txtDescricao = new javax.swing.JFormattedTextField();
        ckInsidenciaINSS = new javax.swing.JCheckBox();
        jLabel1 = new javax.swing.JLabel();
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
        setTitle("Definição de Subsidios");

        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel6.setText("Descrição");

        ckInsidenciaINSS.setSelected(true);
        ckInsidenciaINSS.setText("Incidência INSS");

        jLabel1.setForeground(new java.awt.Color(204, 0, 0));
        jLabel1.setText("Atenção, não incluir o Abono de Familia neste formulário! ");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtDescricao, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addGap(33, 33, 33)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(ckInsidenciaINSS, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(21, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtDescricao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ckInsidenciaINSS))
                .addContainerGap())
        );

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));

        jLabel19.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        jLabel19.setText("NOVO SUBSÍDIO");

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
                .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel1.setBackground(new java.awt.Color(43, 43, 43));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/salvar_32x32.png"))); // NOI18N
        jButton1.setText("salvar");
        jButton1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/actualizar_1_32x32_1.png"))); // NOI18N
        jButton2.setText("alterar");
        jButton2.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/adicionar.png"))); // NOI18N
        jButton3.setText("eliminar");
        jButton3.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/LOGOUT - VERMELHO/Logout 32x32.png"))); // NOI18N
        jButton4.setText("sair");
        jButton4.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(91, 91, 91))
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
            new Object [][]
            {

            },
            new String []
            {
                "Cod", "Descrição", "Incidência INSS"
            }
        )
        {
            boolean[] canEdit = new boolean []
            {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex)
            {
                return canEdit [columnIndex];
            }
        });
        jTable1.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                jTable1MouseClicked(evt);
            }
        });
        jTable1.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyReleased(java.awt.event.KeyEvent evt)
            {
                jTable1KeyReleased(evt);
            }
        });
        jScrollPane2.setViewportView(jTable1);
        if (jTable1.getColumnModel().getColumnCount() > 0)
        {
            jTable1.getColumnModel().getColumn(0).setMaxWidth(100);
        }

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                .addContainerGap()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

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
        if ( evt.getClickCount() >= 2 )
        {

            setDadosProdutoModelo();

        }
    }//GEN-LAST:event_jTable1MouseClicked

    private void jTable1KeyReleased(java.awt.event.KeyEvent evt)//GEN-FIRST:event_jTable1KeyReleased
    {//GEN-HEADEREND:event_jTable1KeyReleased
        // TODO add your handling code here:
        setDadosProdutoModelo();
    }//GEN-LAST:event_jTable1KeyReleased

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
            java.util.logging.Logger.getLogger( TipoSubsidioVisao.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        catch ( InstantiationException ex )
        {
            java.util.logging.Logger.getLogger( TipoSubsidioVisao.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        catch ( IllegalAccessException ex )
        {
            java.util.logging.Logger.getLogger( TipoSubsidioVisao.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        catch ( javax.swing.UnsupportedLookAndFeelException ex )
        {
            java.util.logging.Logger.getLogger( TipoSubsidioVisao.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
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
                new TipoSubsidioVisao().setVisible( true );
            }
        } );
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox ckInsidenciaINSS;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JFormattedTextField txtDescricao;
    // End of variables declaration//GEN-END:variables

    public void setDadosProdutoModelo()
    {
        DefaultTableModel modelo = ( DefaultTableModel ) jTable1.getModel();

        this.cod_subsidios = Integer.parseInt( String.valueOf( modelo.getValueAt( jTable1.getSelectedRow(), 0 ) ) );
        boolean inss = String.valueOf( modelo.getValueAt( jTable1.getSelectedRow(), 2 ) ).toString().equals( "Activo" ) ? true : false;

        txtDescricao.setText( String.valueOf( modelo.getValueAt( jTable1.getSelectedRow(), 1 ) ) );
        ckInsidenciaINSS.setSelected( inss );

    }

    private boolean vazios_campos()
    {
        return false;
    }

    public void alterar()
    {

    }

    public void limpar()
    {

        txtDescricao.setText( "" );

    }

    private boolean tabela_vazia()
    {
        DefaultTableModel modelo = ( DefaultTableModel ) jTable1.getModel();
        return modelo.getRowCount() == 0;
    }

    private void esvaziar_tabela()
    {
        this.modelo = ( DefaultTableModel ) jTable1.getModel();
        this.modelo.setRowCount( 0 );
    }

    private void actuazlizar_quantidade( String quantidade )
    {
        DefaultTableModel modelo = ( DefaultTableModel ) jTable1.getModel();

        double preco = Double.parseDouble( String.valueOf( modelo.getValueAt( linha_actual, 2 ) ) );
        int qtd_existente = Integer.parseInt( String.valueOf( modelo.getValueAt( linha_actual, 3 ) ) );

        int qtd_entrar = Integer.parseInt( quantidade );
        int qtd_actualizada = qtd_existente + qtd_entrar;
        double total_item = preco * qtd_actualizada;

        modelo.setValueAt( quantidade, linha_actual, 3 );
        modelo.setValueAt( total_item, linha_actual, 5 );

        this.linha_actual = -1;

    }

    private void remover_item_carrinho()
    {

        DefaultTableModel modelo = ( DefaultTableModel ) jTable1.getModel();
        modelo.setRowCount( 0 );

    }

    private void setDadosFichaTecnica()
    {

    }

    private void procedimento_salvar()
    {

        if ( !txtDescricao.getText().equals( "" ) )
        {

            if ( !tipoSubsidioDao.exist_subsidio( txtDescricao.getText() ) )
            {

                this.subsidios = new TbSubsidios();
                preparar_subsidios();
                try
                {
                    salvar_subsidios();
                    limpar();
                    busca_subsidios();
                    JOptionPane.showMessageDialog( null, "Dado salvo com Sucesso!...", DVML_COMERCIAL, JOptionPane.INFORMATION_MESSAGE );
                }
                catch ( Exception e )
                {
                    JOptionPane.showMessageDialog( null, "Falha ao Registrar o novo Subsidio", DVML_COMERCIAL, JOptionPane.ERROR_MESSAGE );
                }

            }
            else
            {
                JOptionPane.showMessageDialog( null, "Já Existe um Subsidio com essa Descrição!...", DVML_COMERCIAL, JOptionPane.WARNING_MESSAGE );
            }

        }
        else
        {
            JOptionPane.showMessageDialog( null,
                    "Pf. insira o novo subsídio!",
                    DVML_COMERCIAL,
                    JOptionPane.WARNING_MESSAGE );
        }
    }

    private void procedimento_alterar()
    {
        if ( !txtDescricao.getText().equals( "" ) )
        {

            this.subsidios = tipoSubsidioDao.findTbSubsidios( this.cod_subsidios );
            preparar_subsidios();
            try
            {
                alterar_subsidios();
                busca_subsidios();
                JOptionPane.showMessageDialog( null, "Subsidio Alterado com Sucesso!...", DVML_COMERCIAL, JOptionPane.INFORMATION_MESSAGE );
            }
            catch ( Exception e )
            {
                JOptionPane.showMessageDialog( null, "Falha ao Alterar o Subsidio", DVML_COMERCIAL, JOptionPane.ERROR_MESSAGE );
            }
        }
    }

    private void procedimento_elimniar()
    {
        if ( JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog( null, "Tens a Plena Certeza que Pretendes Eliminar Subsidio ?  " ) )
        {
            try
            {
                eliminar_subsidios();
                busca_subsidios();
                limpar();
                if ( !status_eliminado )
                {
                    JOptionPane.showMessageDialog( null, "Registro Eliminado com Sucesso!...", DVML_COMERCIAL, JOptionPane.INFORMATION_MESSAGE );
                }
            }
            catch ( Exception e )
            {
                JOptionPane.showMessageDialog( null, "Falha ao Eliminar o Salario\n Ou Ja est contida em outras tabelas", DVML_COMERCIAL, JOptionPane.ERROR_MESSAGE );
            }
        }
    }

    private void salvar_subsidios()
    {
        tipoSubsidioDao.create( this.subsidios );
    }

    private void alterar_subsidios()
    {
        try
        {
            tipoSubsidioDao.edit( this.subsidios );
        }
        catch ( Exception e )
        {
        }

    }

    private void eliminar_subsidios()
    {
        try
        {
            tipoSubsidioDao.destroy( this.cod_subsidios );
        }
        catch ( Exception e )
        {
            //  e.printStackTrace();
            this.status_eliminado = false;
            JOptionPane.showMessageDialog( null, "Falha ao Eliminar o Salario\n Ou Ja est contida em outras tabelas", DVML_COMERCIAL, JOptionPane.ERROR_MESSAGE );

        }

    }

    private void preparar_subsidios()
    {
        this.subsidios.setDescricao( txtDescricao.getText() );
        this.subsidios.setIncidencia( ckInsidenciaINSS.isSelected() );

    }

    private String getSubsidio( Boolean status )
    {
        return status ? "Activo" : "Desactivo";
    }

    private void imprimir()
    {

    }

    private void detalhe_ficha_tecnica()
    {

    }

    private void busca_subsidios()
    {

        List<TbSubsidios> itens = tipoSubsidioDao.getAllSubsidios();
        esvaziar_tabela();
        for ( int i = 0; i < itens.size(); i++ )
        {

            this.modelo.addRow( new Object[]
            {
                itens.get( i ).getIdSubsidios(),
                itens.get( i ).getDescricao(),
                getSubsidio( itens.get( i ).getIncidencia_inss() )

            } );
        }

    }

}
