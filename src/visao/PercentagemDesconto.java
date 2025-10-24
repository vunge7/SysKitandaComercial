/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package visao;


import java.sql.Connection;
import controller.TipoProdutoController;
import dao.ClienteDao;
import dao.DescontoDao;
import dao.ProdutoDao;
import dao.UsuarioDao;
import entity.TbCliente;
import entity.TbDesconto;
import entity.TbProduto;
import java.awt.Frame;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import kitanda.util.CfMethodsSwing;
import modelo.TipoProdutoModelo;
import modelo.VendaModelo;
import util.BDConexao;
import util.DVML;
import static util.DVML.LIMITE_PERCENTAGEM;
import util.JPAEntityMannagerFactoryUtil;

/**
 *
 * @author Domingos Dala Vunge
 */
public class PercentagemDesconto extends javax.swing.JFrame
{

    /**
     * Creates new form UsuarioVisao
     */
    private EntityManagerFactory emf = JPAEntityMannagerFactoryUtil.em;

    private TipoProdutoModelo tipoProdutoModelo;
    private TipoProdutoController tipoProdutoController;

    private DescontoDao descontoDao = new DescontoDao( emf );
    private TbDesconto desconto;
    private ProdutoDao produtoDao = new ProdutoDao( emf );
    private DefaultListModel lista_aux_clientes = new DefaultListModel();
    private ClienteDao clienteDao = new ClienteDao( emf );
    private UsuarioDao usuarioDao = new UsuarioDao( emf );
    private Integer codigo_produto = 0;
    private Frame parent;
    private int codigo = 0;
    private int codigo_venda = 0;
    private boolean eliminar = false;
    private int id_user = 0;
    private int id_desconto = 0;
    private BDConexao conexao;

    public PercentagemDesconto( java.awt.Frame parent, boolean modal, int id_user, BDConexao conexao ) throws SQLException
    {
        //this.parent =  parent1;
        this.conexao = conexao;
        this.parent = parent;
        initComponents();
        setLocationRelativeTo( null );
        this.id_user = id_user;
        jl_cliente.setModel( lista_aux_clientes );
        procedimento_mostrar_clientes_na_lista();

        //txtDesconto.addKeyListener( new TratarEventoTeclado() );
        reset_desconto_percetagem();

    }

    @SuppressWarnings( "unchecked" )
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        jPanel4 = new javax.swing.JPanel();
        lbFactura = new javax.swing.JLabel();
        lbFactura5 = new javax.swing.JLabel();
        lbCodigoProduto = new javax.swing.JLabel();
        txtCodigoProduto = new javax.swing.JTextField();
        lbCodigoProduto1 = new javax.swing.JLabel();
        txtDesignacao = new javax.swing.JTextField();
        lbFactura1 = new javax.swing.JLabel();
        lbCodigoBarra = new javax.swing.JLabel();
        txtCodigoBarra = new javax.swing.JTextField();
        jsp_quantidade = new javax.swing.JSpinner();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jsf_desconto = new javax.swing.JSpinner();
        jPanel1 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        jl_cliente = new javax.swing.JList<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("...::::: DVML- PERCENTAGEM DESCONTO ::::...");

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        lbFactura.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 16)); // NOI18N
        lbFactura.setText("Desconto");

        lbFactura5.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 16)); // NOI18N
        lbFactura5.setText("Define a Quantidade");

        lbCodigoProduto.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 16)); // NOI18N
        lbCodigoProduto.setText("Codigo Produto");

        txtCodigoProduto.setBackground(new java.awt.Color(51, 153, 0));
        txtCodigoProduto.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        txtCodigoProduto.setForeground(new java.awt.Color(255, 255, 255));
        txtCodigoProduto.setCaretColor(new java.awt.Color(255, 255, 255));
        txtCodigoProduto.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtCodigoProdutoActionPerformed(evt);
            }
        });

        lbCodigoProduto1.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 16)); // NOI18N
        lbCodigoProduto1.setText("Produto");

        txtDesignacao.setEditable(false);
        txtDesignacao.setBackground(new java.awt.Color(51, 153, 0));
        txtDesignacao.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        txtDesignacao.setForeground(new java.awt.Color(255, 255, 255));
        txtDesignacao.setCaretColor(new java.awt.Color(255, 255, 255));

        lbFactura1.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 16)); // NOI18N
        lbFactura1.setText(" (%)");

        lbCodigoBarra.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 16)); // NOI18N
        lbCodigoBarra.setText("Codigo Barra");

        txtCodigoBarra.setBackground(new java.awt.Color(51, 153, 0));
        txtCodigoBarra.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        txtCodigoBarra.setForeground(new java.awt.Color(255, 255, 255));
        txtCodigoBarra.setCaretColor(new java.awt.Color(255, 255, 255));
        txtCodigoBarra.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtCodigoBarraActionPerformed(evt);
            }
        });

        jsp_quantidade.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/Button-Add-icon.png"))); // NOI18N
        jButton3.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/2934_32x32.png"))); // NOI18N
        jButton4.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButton4ActionPerformed(evt);
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

        jsf_desconto.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(61, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel4Layout.createSequentialGroup()
                            .addGap(8, 8, 8)
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(lbFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lbFactura5, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(12, 12, 12)
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jsp_quantidade)
                                .addComponent(jsf_desconto, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(lbFactura1))
                        .addGroup(jPanel4Layout.createSequentialGroup()
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(jPanel4Layout.createSequentialGroup()
                                    .addGap(8, 8, 8)
                                    .addComponent(lbCodigoBarra, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGap(10, 10, 10))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(lbCodigoProduto1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(lbCodigoProduto, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGap(18, 18, 18)))
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(txtCodigoProduto, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtDesignacao)
                                .addComponent(txtCodigoBarra, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jButton3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton4)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbCodigoBarra, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCodigoBarra, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(7, 7, 7)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCodigoProduto, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbCodigoProduto, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtDesignacao, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbCodigoProduto1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jsp_quantidade, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)
                    .addComponent(lbFactura5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jsf_desconto, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbFactura1)
                    .addComponent(lbFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton3)
                    .addComponent(jButton4)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(19, 19, 19))
        );

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/LOGOUT - VERMELHO/Logout 32x32.png"))); // NOI18N
        jButton2.setText("Sair");
        jButton2.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jButton2))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 15, Short.MAX_VALUE))
        );

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "Cod.", "Designação", "Cliente", "Qtd Desc", "Percentagem"
            }
        )
        {
            boolean[] canEdit = new boolean []
            {
                false, false, false, false, false
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
        jScrollPane1.setViewportView(jTable1);
        if (jTable1.getColumnModel().getColumnCount() > 0)
        {
            jTable1.getColumnModel().getColumn(0).setMinWidth(0);
            jTable1.getColumnModel().getColumn(0).setMaxWidth(100);
            jTable1.getColumnModel().getColumn(3).setMinWidth(0);
            jTable1.getColumnModel().getColumn(3).setMaxWidth(100);
            jTable1.getColumnModel().getColumn(4).setMinWidth(0);
            jTable1.getColumnModel().getColumn(4).setMaxWidth(100);
        }

        jl_cliente.setFont(new java.awt.Font("Lucida Grande", 1, 12)); // NOI18N
        jl_cliente.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                jl_clienteMouseClicked(evt);
            }
        });
        jl_cliente.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyTyped(java.awt.event.KeyEvent evt)
            {
                jl_clienteKeyTyped(evt);
            }
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                jl_clienteKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt)
            {
                jl_clienteKeyReleased(evt);
            }
        });
        jScrollPane2.setViewportView(jl_cliente);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jScrollPane1))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 477, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_jButton2ActionPerformed


    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        procedimento_adcionar();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void txtCodigoProdutoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCodigoProdutoActionPerformed
        // TODO add your handling code here:
        procedimento_busca_codigo_produto();
    }//GEN-LAST:event_txtCodigoProdutoActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        procedimento_eliminar_desconto();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jl_clienteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jl_clienteMouseClicked
        // TODO add your handling code here:

        try
        {
            mostrar_dados();
        }
        catch ( Exception e )
        {
        }


    }//GEN-LAST:event_jl_clienteMouseClicked

    private void jl_clienteKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jl_clienteKeyPressed
        // TODO add your handling code here:


    }//GEN-LAST:event_jl_clienteKeyPressed

    private void jl_clienteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jl_clienteKeyTyped
        // TODO add your handling code here:

    }//GEN-LAST:event_jl_clienteKeyTyped

    private void jl_clienteKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jl_clienteKeyReleased
        // TODO add your handling code here:
        try
        {
            mostrar_dados();
        }
        catch ( Exception e )
        {
        }
    }//GEN-LAST:event_jl_clienteKeyReleased

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        // TODO add your handling code here:

        if ( evt.getClickCount() == 2 )
        {
            mostrar_dados_formulario();
        }

    }//GEN-LAST:event_jTable1MouseClicked

    private void jTable1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTable1KeyReleased
        // TODO add your handling code here:


    }//GEN-LAST:event_jTable1KeyReleased

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        procedimento_actualizar();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void txtCodigoBarraActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtCodigoBarraActionPerformed
    {//GEN-HEADEREND:event_txtCodigoBarraActionPerformed
        procedimento_busca_cod_barra();
    }//GEN-LAST:event_txtCodigoBarraActionPerformed

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
            java.util.logging.Logger.getLogger( PercentagemDesconto.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        catch ( InstantiationException ex )
        {
            java.util.logging.Logger.getLogger( PercentagemDesconto.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        catch ( IllegalAccessException ex )
        {
            java.util.logging.Logger.getLogger( PercentagemDesconto.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        catch ( javax.swing.UnsupportedLookAndFeelException ex )
        {
            java.util.logging.Logger.getLogger( PercentagemDesconto.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater( new Runnable()
        {
            public void run()
            {
                PercentagemDesconto dialog = null;
                try
                {
                    dialog = new PercentagemDesconto( new javax.swing.JFrame(), true, 15, BDConexao.getInstancia() );
                }
                catch ( SQLException ex )
                {
                    Logger.getLogger( PercentagemDesconto.class.getName() ).log( Level.SEVERE, null, ex );
                }
                dialog.addWindowListener( new java.awt.event.WindowAdapter()
                {
                    @Override
                    public void windowClosing( java.awt.event.WindowEvent e )
                    {
                        System.exit( 0 );
                    }
                } );
                dialog.setVisible( true );
            }
        } );
    }

    public void limpar()
    {
        txtCodigoProduto.setText( "" );
        txtDesignacao.setText( "" );

        txtCodigoProduto.requestFocus();

    }

    //CLASSE EVENTO TECLADO 
    //----------- evento do teclado ---------------------------------------
    class TratarEventoTeclado implements KeyListener
    {

        String prefixo = null;

        public void keyPressed( KeyEvent evt )
        {

            VendaModelo vendaModelo = new VendaModelo();

            if ( evt.getKeyCode() == KeyEvent.VK_ENTER )
            {
                char key = evt.getKeyChar();
                //prefixo = txtDesconto.getText().trim() + key;

                //guardar o desconto
                // salvar(  Double.parseDouble(prefixo));
            }
        }

        public void keyReleased( KeyEvent evt )
        {
        }

        public void keyTyped( KeyEvent evt )
        {
        }
    }

    //----------- evento do teclado ---------------------------------------
    class TratarEventoTecladoProduto implements KeyListener
    {

        String prefixo = "";

        public void keyPressed( KeyEvent evt )
        {

            if ( evt.getKeyCode() != KeyEvent.VK_BACK_SPACE && evt.getKeyCode() == KeyEvent.VK_ENTER )
            {
                char key = evt.getKeyChar();

                prefixo = txtCodigoProduto.getText().trim() + key;
                codigo_produto = Integer.parseInt( prefixo.trim() );

                try
                {
                    ver_dados_produtos( codigo_produto );
                }
                catch ( Exception ex )
                {

                    Logger.getLogger( PercentagemDesconto.class.getName() ).log( Level.SEVERE, null, ex );
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


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JList<String> jl_cliente;
    private javax.swing.JSpinner jsf_desconto;
    private javax.swing.JSpinner jsp_quantidade;
    private javax.swing.JLabel lbCodigoBarra;
    private javax.swing.JLabel lbCodigoProduto;
    private javax.swing.JLabel lbCodigoProduto1;
    private javax.swing.JLabel lbFactura;
    private javax.swing.JLabel lbFactura1;
    private javax.swing.JLabel lbFactura5;
    private javax.swing.JTextField txtCodigoBarra;
    private javax.swing.JTextField txtCodigoProduto;
    private javax.swing.JTextField txtDesignacao;
    // End of variables declaration//GEN-END:variables

    private void procedimento_mostrar_clientes_na_lista()
    {
        List<TbCliente> lista = clienteDao.getAllClienteOrdenado();
        lista_aux_clientes.clear();
        for ( int i = 0; i < lista.size(); i++ )
        {
            lista_aux_clientes.addElement( lista.get( i ).getNome() );
        }
    }

    public void ver_dados_produtos( int codigo )
    {

        try
        {
            TbProduto produto = produtoDao.findTbProduto( codigo );
            txtDesignacao.setText( produto.getDesignacao() );
            txtCodigoBarra.setText( String.valueOf( produto.getCodBarra() ) );
        }
        catch ( Exception e )
        {
            limpar();
            JOptionPane.showMessageDialog( null, "Não existe nenhum produto relacionado a este código", DVML.DVML_COMERCIAL, JOptionPane.WARNING_MESSAGE );
        }

    }

    public void ver_dados_produtos( String codigo_barra )
    {

        try
        {
            TbProduto produto = produtoDao.getProdutoByCodigoBarra( codigo_barra );
            txtCodigoProduto.setText( String.valueOf( produto.getCodigo() ) );
            txtDesignacao.setText( produto.getDesignacao() );
            jsp_quantidade.requestFocus();
            
        }
        catch ( Exception e )
        {
            limpar();
            JOptionPane.showMessageDialog( null, "Não existe nenhum produto relacionado a este código", DVML.DVML_COMERCIAL, JOptionPane.WARNING_MESSAGE );
        }

    }

    private boolean is_valido()
    {

        if ( jl_cliente.isSelectionEmpty() )
        {
            JOptionPane.showMessageDialog( null, "Por favor seleccione o cliente", DVML.DVML_COMERCIAL, JOptionPane.WARNING_MESSAGE );
            return false;
        }
        else if ( txtCodigoProduto.getText().equals( "" ) )
        {
            JOptionPane.showMessageDialog( null, "Por favor, faça a busca do produto", DVML.DVML_COMERCIAL, JOptionPane.WARNING_MESSAGE );
            txtCodigoProduto.requestFocus();
            return false;
        }
        else if ( jsp_quantidade.getValue().toString().equals( "0" ) || jsp_quantidade.getValue().toString().equals( "" ) )
        {
            JOptionPane.showMessageDialog( null, "Por favor, define a quantidade que pretende fazer o desconto", DVML.DVML_COMERCIAL, JOptionPane.WARNING_MESSAGE );
            return false;
        }
//        else if ( txtDesconto.getText().equals( "" ) )
//        {
//            JOptionPane.showMessageDialog( null, "Por favor, define a percentagem de desconto do produto", DVML.DVML_COMERCIAL, JOptionPane.WARNING_MESSAGE );
//            txtDesconto.requestFocus();
//            return false;
//        }
        else
        {
            return true;
        }

    }

    private void procedimento_adcionar()
    {

        if ( is_valido() )
        {

            if ( !descontoDao.exist_produto_cliente( getIdCliente(), Integer.parseInt( txtCodigoProduto.getText() ) ) )
            {

                this.desconto = new TbDesconto();
                set_desconto_modelo();
                try
                {
                    descontoDao.create( desconto );
                    mostrar_dados();
                    JOptionPane.showMessageDialog( null, "Desconto adicionado com sucesso", DVML.DVML_COMERCIAL, JOptionPane.WARNING_MESSAGE );
                }
                catch ( Exception e )
                {
                    JOptionPane.showMessageDialog( null, "Erro ao adcionar o desconto", DVML.DVML_COMERCIAL, JOptionPane.ERROR_MESSAGE );
                }

            }
            else
            {
                JOptionPane.showMessageDialog( null, "Já existe um desconto deste produto para esse cliente\nConvém actualizar se puder", DVML.DVML_COMERCIAL, JOptionPane.WARNING_MESSAGE );
            }

        }

    }

    private void set_desconto_modelo()
    {

        this.desconto.setData( new Date() );
        this.desconto.setHora( new Date() );
        this.desconto.setFkUsuario( usuarioDao.findTbUsuario( this.id_user ) );
        this.desconto.setFkCliente( clienteDao.findTbCliente( getIdCliente() ) );
        this.desconto.setFkProduto( produtoDao.findTbProduto( Integer.parseInt( txtCodigoProduto.getText() ) ) );
        this.desconto.setQuantidade( Double.parseDouble( jsp_quantidade.getValue().toString() ) );
        this.desconto.setValor(Double.parseDouble(jsf_desconto.getValue().toString() ) );

    }

    private int getIdCliente()
    {

        try
        {
            return clienteDao.getClienteByNome( jl_cliente.getSelectedValue().toString() ).getCodigo();

        }
        catch ( Exception e )
        {
            return 0;
        }
    }

    private void mostrar_dados()
    {
        DefaultTableModel modelo = ( DefaultTableModel ) jTable1.getModel();
        modelo.setRowCount( 0 );
        List<TbDesconto> lista = descontoDao.getAllDescontoByIdCliente( getIdCliente() );

        for ( TbDesconto item : lista )
        {

            modelo.addRow( new Object[]
            {
                item.getIdDesconto(),
                item.getFkProduto().getDesignacao(),
                item.getFkCliente().getNome(),
                item.getQuantidade(),
                item.getValor()
            } );

        }

    }

    private void procedimento_eliminar_desconto()
    {

        if ( is_select() )
        {

            int opcao = JOptionPane.showConfirmDialog( null, "Tens a certeza que pretendes eliminar esse desconto ?" );

            if ( opcao == JOptionPane.YES_OPTION )
            {

                try
                {
                    descontoDao.destroy( getIdDesconto() );
                    mostrar_dados();
                    JOptionPane.showMessageDialog( null, "Operação efectuada com sucesso!..", DVML.DVML_COMERCIAL, JOptionPane.WARNING_MESSAGE );
                }
                catch ( Exception e )
                {
                    JOptionPane.showMessageDialog( null, "Erro ao eliminar o desconto", DVML.DVML_COMERCIAL, JOptionPane.ERROR_MESSAGE );
                }

            }
            else
            {
                JOptionPane.showMessageDialog( null, "Erro ao eliminar o desconto", DVML.DVML_COMERCIAL, JOptionPane.ERROR_MESSAGE );
            }

        }
        else
        {
            JOptionPane.showMessageDialog( null, "Não existe dados na tabela" );
        }

    }

    private int getIdDesconto()
    {
        DefaultTableModel modelo = ( DefaultTableModel ) jTable1.getModel();

        try
        {
            return Integer.parseInt( modelo.getValueAt( jTable1.getSelectedRow(), 0 ).toString() );
        }
        catch ( Exception e )
        {
            JOptionPane.showMessageDialog( null, "Possivelmento não seleccionaste nenhuma linha ou não existe nenhum desconto na tabela" );
            return 0;
        }

    }

    private boolean is_select()
    {
        return jTable1.getSelectedRow() > - 1;
    }

    private void mostrar_dados_formulario()
    {
        this.desconto = descontoDao.findTbDesconto( getIdDesconto() );

        txtCodigoBarra.setText( String.valueOf( desconto.getFkProduto().getCodBarra() ) );
        txtCodigoProduto.setText( String.valueOf( desconto.getFkProduto().getCodigo() ) );
        txtDesignacao.setText( String.valueOf( desconto.getFkProduto().getDesignacao() ) );
        jsp_quantidade.setValue( desconto.getQuantidade() );
        jsf_desconto.setValue( desconto.getValor() );

    }

    private void limpar_all()
    {
        txtCodigoBarra.setText( "" );
        txtCodigoProduto.setText( "" );
        txtDesignacao.setText( "" );
        jsp_quantidade.setValue( "" );
        reset_desconto_percetagem();
        this.id_desconto = 0;

    }

    private void procedimento_actualizar()
    {

        if ( is_select() )
        {

            if ( is_valido() )
            {

                set_desconto_modelo();
                try
                {
                    descontoDao.edit( desconto );
                    mostrar_dados();
                    JOptionPane.showMessageDialog( null, "Dados alterados com sucesso", DVML.DVML_COMERCIAL, JOptionPane.WARNING_MESSAGE );
                }
                catch ( Exception e )
                {
                    JOptionPane.showMessageDialog( null, "Erro ao alterar os dados", DVML.DVML_COMERCIAL, JOptionPane.ERROR_MESSAGE );
                }
            }

        }
        else
        {
            JOptionPane.showMessageDialog( null, "Tabela vazia ou não seleccionaste nenhuma linha", DVML.DVML_COMERCIAL, JOptionPane.WARNING_MESSAGE );
        }

    }

    private void reset_desconto_percetagem()
    {
        jsf_desconto.setModel( CfMethodsSwing.criarSpinnerDoubleModel( 0.0, LIMITE_PERCENTAGEM, 0.0 ) );
    }

    private String getCodBarra()
    {
        String txtCodBarra = txtCodigoBarra.getText();
        long resultado = ( !txtCodBarra.equalsIgnoreCase( "" ) ) ? Long.parseLong( txtCodBarra ) : 0;
        return txtCodigoBarra.getText().trim();
    }
    
    private int getCodigoProduto()
    {
        String txtCodProduto = this.txtCodigoProduto.getText();
        int resultado = ( !txtCodProduto.equalsIgnoreCase( "" ) ) ? Integer.parseInt( txtCodProduto ) : 0;
        return resultado;
    }

    private void procedimento_busca_cod_barra()
    {
        String cod_barra = getCodBarra();
        if ( txtCodigoBarra.getText().isEmpty () )
        {
            ver_dados_produtos( cod_barra );

        }
        else
        {
            JOptionPane.showMessageDialog( null, "O campo esta vazio.", "AVISO", JOptionPane.WARNING_MESSAGE );
        }
    }
    private void procedimento_busca_codigo_produto()
    {
        int codigo_produto = getCodigoProduto();
        if ( codigo_produto != 0 )
        {
            ver_dados_produtos( codigo_produto );

        }
        else
        {
            JOptionPane.showMessageDialog( null, "O campo esta vazio.", "AVISO", JOptionPane.WARNING_MESSAGE );
        }
    }

}
