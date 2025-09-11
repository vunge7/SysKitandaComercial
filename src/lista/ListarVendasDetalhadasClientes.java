/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lista;

import com.mysql.jdbc.Connection;
import dao.ClienteDao;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import util.BDConexao;
import util.JPAEntityMannagerFactoryUtil;

/**
 *
 * @author Domingos Dala Vunge
 */
public class ListarVendasDetalhadasClientes extends javax.swing.JFrame
{

    /**
     * Creates new form ListaClienteVisao
     */
    private EntityManagerFactory emf = JPAEntityMannagerFactoryUtil.em;
    private BDConexao conexao;
    private ClienteDao clienteDao = new ClienteDao(emf);

    public ListarVendasDetalhadasClientes(BDConexao conexao)
    {

        initComponents();
        setResizable( false );
        setLocationRelativeTo( null );
        confLabel();

        dcDataVenda.setDate( new Date() );

        this.conexao = conexao;
        cmbCliente.setModel( new DefaultComboBoxModel( ( Vector ) clienteDao.buscaTodosClientesExceptoConsumidorFinal() ) );
//        cmbCliente.setModel( new DefaultComboBoxModel( conexao.getElementos( "tb_cliente", "nome", false ) ) );
    }

    //SELECT distinct  SUM(total_venda) as soma FROM tb_venda WHERE codigo_cliente = 4;
    public void confLabel()
    {
        lbStatus.setHorizontalAlignment( JLabel.RIGHT );
        lbData.setHorizontalAlignment( JLabel.RIGHT );

    }

    public String getCliente()
    {
        return String.valueOf( cmbCliente.getSelectedItem() );

    }

    public double getTotalValor( int codCliente )
    {
        // SELECT distinct  SUM(total_venda) as soma FROM tb_venda WHERE codigo_cliente = 15 AND dataVenda LIKE '2013-04-05%'
        //String sql = "SELECT  SUM(total_venda) as soma FROM tb_venda WHERE codigo_cliente = "  +codUtilizdor+" AND dataVenda LIKE '" +getDataSelecionadaString() +"%'";
        String sql = "SELECT  SUM(total_venda) as soma FROM tb_venda WHERE codigo_cliente = " + codCliente + "  AND performance = 'false'  AND status_eliminado = 'false'  AND dataVenda = '" + getDataSelecionadaString() + "'";
        System.out.println( sql );
        ResultSet rs = conexao.executeQuery( sql );

        try
        {
            if ( rs.next() )
            {
                return rs.getDouble( "soma" );
            }
        }
        catch ( SQLException ex )
        {
            return 0;
        }

        return 0;

    }

    //devolve o campo de uma determinada tabela em funcao do codigo
    public int getCodigoCliente()
    {

        String sql = "SELECT codigo FROM  tb_cliente WHERE( nome  = '" + getCliente() + "')";

        ResultSet rs = conexao.executeQuery( sql );

        try
        {
            if ( rs.next() )
            {
                return rs.getInt( "codigo" );
            }
        }
        catch ( SQLException ex )
        {
            return 0;
        }
        return 0;
    }

    public Date getDataSelecionada()
    {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime( dcDataVenda.getDate() );

        String dataSelecionada = gc.get( GregorianCalendar.YEAR ) + "-"
                + ( gc.get( GregorianCalendar.MONTH ) + 1 ) + "-"
                + gc.get( GregorianCalendar.DATE );
        return dcDataVenda.getDate();
    }

    public String getDataSelecionadaString()
    {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime( dcDataVenda.getDate() );

        String dataSelecionada = gc.get( GregorianCalendar.YEAR ) + "-"
                + setTransforme( gc.get( GregorianCalendar.MONTH ) + 1 ) + "-"
                + setTransforme( gc.get( GregorianCalendar.DATE ) );
        return dataSelecionada;
    }

    public String setTransforme( int descrisao )
    {
        if ( descrisao <= 9 )
        {
            return "0" + descrisao;
        }
        else
        {
            return String.valueOf( descrisao );
        }
    }

    public String getCaminho()
    {

        return "relatorios/vendas_detalhadas_clientes.jasper";

    }
//    

    public void mostrarClientes() throws SQLException
    {

        Connection connection = ( Connection ) conexao.conectar();
        HashMap hashMap = new HashMap();

        hashMap.put( "NOME_CLIENTE", getCliente() );
        hashMap.put( "DATA", dcDataVenda.getDate() );
        hashMap.put( "DATA_VENDA", dcDataVenda.getDate() );
        hashMap.put( "TOTAL_CLIENTE", getTotalValor( getCodigoCliente() ) );

        String relatorio = getCaminho();

        File file = new File( relatorio ).getAbsoluteFile();

        String obterCaminho = file.getAbsolutePath();

        try
        {

            JasperFillManager.fillReport( obterCaminho, hashMap, connection );

            JasperPrint jasperPrint = JasperFillManager.fillReport( obterCaminho, hashMap, connection );

            if ( jasperPrint.getPages().size() >= 1 )
            {

                JasperViewer jasperViewer = new JasperViewer( jasperPrint, false );
                jasperViewer.setVisible( true );

            }
            else
            {
                JOptionPane.showMessageDialog( null, "Atenção: Não existem vendas deste cliente para esta data!..." );
            }
        }
        catch ( JRException jex )
        {
            jex.printStackTrace();
            JOptionPane.showMessageDialog( null, "FALHA AO TENTAR MOSTRAR O CLIENTE!..." );
        }
        catch ( Exception ex )
        {
            ex.printStackTrace();
            JOptionPane.showMessageDialog( null, "ERRO AO TENTAR O CLIENTE!..." );
        }
    }

    @SuppressWarnings( "unchecked" )
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        lbStatus = new javax.swing.JLabel();
        lbData = new javax.swing.JLabel();
        cmbCliente = new javax.swing.JComboBox();
        dcDataVenda = new com.toedter.calendar.JDateChooser();
        jPanel3 = new javax.swing.JPanel();
        btnCancelar = new javax.swing.JButton();
        btnCancelar1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("...:::: DVML - LISTAGEM DE VENDAS DETALHADAS:::...");

        jPanel1.setBackground(new java.awt.Color(0, 51, 102));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jLabel1.setBackground(new java.awt.Color(51, 153, 0));
        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("VENDAS DETALHADAS POR CLIENTES");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 531, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(12, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(20, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        lbStatus.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 16)); // NOI18N
        lbStatus.setText("Cliente:");

        lbData.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 16)); // NOI18N
        lbData.setText("Data Venda:");

        cmbCliente.setBackground(new java.awt.Color(51, 153, 0));
        cmbCliente.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 14)); // NOI18N
        cmbCliente.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Todos", "Activo", "Desactivo" }));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(84, 84, 84)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lbStatus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lbData, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(dcDataVenda, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cmbCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbData, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dcDataVenda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(31, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        btnCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/impressora1.png"))); // NOI18N
        btnCancelar.setText("Impimrir");
        btnCancelar.setAlignmentX(0.5F);
        btnCancelar.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnCancelarActionPerformed(evt);
            }
        });

        btnCancelar1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/SAIR/sair_verde_32x32.png"))); // NOI18N
        btnCancelar1.setAlignmentX(0.5F);
        btnCancelar1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnCancelar1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCancelar1, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCancelar1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(13, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        try
        {
            // TODO add your handling code here:

            mostrarClientes();
        }
        catch ( SQLException ex )
        {
            Logger.getLogger(ListarVendasDetalhadasClientes.class.getName() ).log( Level.SEVERE, null, ex );
        }

    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnCancelar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelar1ActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_btnCancelar1ActionPerformed

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
                if ( "Nimbus".equals( info.getName() ) )
                {
                    javax.swing.UIManager.setLookAndFeel( info.getClassName() );
                    break;
                }
            }
        }
        catch ( ClassNotFoundException ex )
        {
            java.util.logging.Logger.getLogger(ListarVendasDetalhadasClientes.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        catch ( InstantiationException ex )
        {
            java.util.logging.Logger.getLogger(ListarVendasDetalhadasClientes.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        catch ( IllegalAccessException ex )
        {
            java.util.logging.Logger.getLogger(ListarVendasDetalhadasClientes.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        catch ( javax.swing.UnsupportedLookAndFeelException ex )
        {
            java.util.logging.Logger.getLogger(ListarVendasDetalhadasClientes.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
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

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                new ListarVendasDetalhadasClientes( new BDConexao()).setVisible( true );
            }
        } );
    }

//    private boolean isSemDetalhe()
//    {
//           return  rbSemDetalhe.isSelected();
//    
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnCancelar1;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox cmbCliente;
    private com.toedter.calendar.JDateChooser dcDataVenda;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JLabel lbData;
    private javax.swing.JLabel lbStatus;
    // End of variables declaration//GEN-END:variables
}
