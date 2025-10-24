/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package visao;


import java.sql.Connection;
import dao.AccessoArmazemDao;
import dao.ArmazemDao;
import dao.ItemVendaDao;
import dao.PrecoDao;
import dao.UsuarioDao;
import dao.VendaDao;
import entity.TbPreco;
import entity.TbStock;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.swing.DefaultComboBoxModel;
import lista.MapaExistencia;
import util.BDConexao;
import util.JPAEntityMannagerFactoryUtil;
import util.MapaExistenciaUtil;
import util.MetodosUtil;

/**
 *
 * @author Domingos Dala Vunge
 */
public class MapaExistenciaVisao extends javax.swing.JFrame
{

    private EntityManagerFactory emf = JPAEntityMannagerFactoryUtil.em;
    private VendaDao vendaDao = new VendaDao( emf );
    private UsuarioDao usuarioDao = new UsuarioDao( emf );
    private ItemVendaDao itemVendaDao = new ItemVendaDao( emf );
    private PrecoDao precoDao = new PrecoDao( emf );
    private ArmazemDao armazemDao = new ArmazemDao( emf );
    private double total_geral = 0;
    private BDConexao conexao;
    private AccessoArmazemDao accessoArmazemDao = new AccessoArmazemDao( emf );
    

    public MapaExistenciaVisao( int idUser, BDConexao conexao )
    {

        this.conexao = conexao;
        initComponents();
        setResizable( false );
        setLocationRelativeTo( null );
        dcData.setDate( new Date() );
        cmbArmazem.setModel( new DefaultComboBoxModel( accessoArmazemDao.getAllArmazemByIdUSuario( idUser ) ) );
//        cmbArmazem.setModel( new DefaultComboBoxModel( armazemDao.buscaTodos() ) );

    }

    @SuppressWarnings( "unchecked" )
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        lbData = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        cmbArmazem = new javax.swing.JComboBox<>();
        dcData = new com.toedter.calendar.JDateChooser();
        lbData2 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        btnCancelar = new javax.swing.JButton();
        btnCancelar1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("...:::: DVML - MAPA DE EXISTÊNCIA::...");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        lbData.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 16)); // NOI18N
        lbData.setText("De");

        jLabel1.setText("Armazém");

        cmbArmazem.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        dcData.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                dcDataPropertyChange(evt);
            }
        });
        dcData.addVetoableChangeListener(new java.beans.VetoableChangeListener() {
            public void vetoableChange(java.beans.PropertyChangeEvent evt)throws java.beans.PropertyVetoException {
                dcDataVetoableChange(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbData, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dcData, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 40, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbArmazem, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(121, 121, 121))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(dcData, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbData, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbArmazem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        lbData2.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 36)); // NOI18N
        lbData2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbData2.setText("MAPA DE EXISTÊNCIA");

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        btnCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/impressora1.png"))); // NOI18N
        btnCancelar.setText("Imprimir");
        btnCancelar.setAlignmentX(0.5F);
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        btnCancelar1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/LOGOUT - VERMELHO/Logout 32x32.png"))); // NOI18N
        btnCancelar1.setText("Sair");
        btnCancelar1.setAlignmentX(0.5F);
        btnCancelar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelar1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnCancelar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCancelar1, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCancelar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnCancelar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(lbData2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 2, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(lbData2, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 24, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCancelar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelar1ActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_btnCancelar1ActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        processar_mapa();
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void dcDataPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_dcDataPropertyChange
        // TODO add your handling code here:
        try
        {
//            diferencaDatasMaiorDiasUteis();

        }
        catch ( Exception e )
        {
        }
    }//GEN-LAST:event_dcDataPropertyChange

    private void dcDataVetoableChange(java.beans.PropertyChangeEvent evt)throws java.beans.PropertyVetoException {//GEN-FIRST:event_dcDataVetoableChange
        // TODO add your handling code here:
        //verificarDataComeco();
    }//GEN-LAST:event_dcDataVetoableChange

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
            java.util.logging.Logger.getLogger( MapaExistenciaVisao.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        catch ( InstantiationException ex )
        {
            java.util.logging.Logger.getLogger( MapaExistenciaVisao.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        catch ( IllegalAccessException ex )
        {
            java.util.logging.Logger.getLogger( MapaExistenciaVisao.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        catch ( javax.swing.UnsupportedLookAndFeelException ex )
        {
            java.util.logging.Logger.getLogger( MapaExistenciaVisao.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater( new Runnable()
        {
            public void run()
            {
                try
                {
                    new MapaExistenciaVisao( 15, BDConexao.getInstancia() ).setVisible( true );
                }
                catch ( Exception ex )
                {
                    Logger.getLogger( MapaExistenciaVisao.class.getName() ).log( Level.SEVERE, null, ex );
                }
            }
        } );
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnCancelar1;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox<String> cmbArmazem;
    private com.toedter.calendar.JDateChooser dcData;
    private com.toedter.calendar.JDateChooser dcDataComeco;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel lbData;
    private javax.swing.JLabel lbData2;
    // End of variables declaration//GEN-END:variables

    public int getCodigoArmazem()
    {
        return armazemDao.getArmazemByDescricao( cmbArmazem.getSelectedItem().toString() ).getCodigo();
    }

    private String getData( Date date )
    {

        return ( date.getYear() + 1900 )
                + "/" + ( getNumeroDoisDigitos( date.getMonth() + 1 ) )
                + "/" + getNumeroDoisDigitos( date.getDate() );

    }

    private String getDataBefore( Date date )
    {

        if ( ( date.getMonth() + 1 ) != 1 )
        {

            if ( date.getDate() == 1 )
            {
                return ( date.getYear() + 1900 )
                        + "/" + ( getNumeroDoisDigitos( date.getMonth() ) )
                        + "/" + getNumeroDoisDigitos( getDias( date.getMonth(), date.getYear() + 1900 ) );
            }
            else
            {
                return ( date.getYear() + 1900 )
                        + "/" + ( getNumeroDoisDigitos( date.getMonth() + 1 ) )
                        + "/" + getNumeroDoisDigitos( date.getDate() - 1 );
            }

        }
        else
        {

            return ( date.getYear() + 1900 )
                    + "/" + ( getNumeroDoisDigitos( 12 ) )
                    + "/" + getNumeroDoisDigitos( 31 );

        }

    }

    private int getDias( int mes, int ano )
    {

        switch ( mes )
        {
            case 0:
            case 2:
            case 4:
            case 6:
            case 7:
            case 9:
            case 11:
                return 31;
            case 3:
            case 5:
            case 8:
            case 10:
                return 30;
            default:
            {
                return getDiaBissesto( 1900 + ano );
            }
        }

    }

    private static int getDiaBissesto( int ano )
    {
        switch ( ano )
        {
            case 2016:
            case 2020:
            case 2024:
            case 2028:
            case 2032:
                return 29;
            default:
                return 28;
        }

    }

    private String getHora( Date date )
    {

        return getNumeroDoisDigitos( date.getHours() ) + ":"
                + getNumeroDoisDigitos( date.getMinutes() ) + ":"
                + getNumeroDoisDigitos( date.getSeconds() );
    }

    private String getNumeroDoisDigitos( int numero )
    {

        if ( numero < 10 )
        {
            return "0" + numero;
        }
        return String.valueOf( numero );

    }

    private void processar_mapa()
    {

//        BDConexao conexao = BDConexao.getInstancia();
        List<TbStock> list_stock = MetodosUtil.getAllStock( conexao );
        List<MapaExistenciaUtil> lista = new ArrayList<MapaExistenciaUtil>();
        MapaExistenciaUtil object;
        TbPreco preco;
        /*
              object.setDesignacao("Produto Teste");
              object.setExistencia_anterior(12);
              object.setQtd_entrada(4);
              object.setQtd_vendida(12);
              object.setQtd_actual(4);
              object.setPreco_venda(450.0);
         */

        //1º Buscar todos os produtos do stock
        //2º Para cada produto setar os campos: existencia_anterior, ....;
        for ( TbStock stock : list_stock )
        {

            object = new MapaExistenciaUtil();
            preco = precoDao.findTbPreco( MetodosUtil.getUltimoPreco( stock.getCodProdutoCodigo().getCodigo(), conexao ) );
            object.setCod_produto( stock.getCodProdutoCodigo().getCodigo() );
            object.setDesignacao( stock.getCodProdutoCodigo().getDesignacao() );
            object.setExistencia_anterior( MetodosUtil.getTotalQtdAnteiror( conexao, stock.getCodProdutoCodigo().getCodigo(), getDataBefore( dcData.getDate() ) ) );
            object.setQtd_entrada( MetodosUtil.getTotalQtdEntrada( conexao, getCodigoArmazem(), stock.getCodProdutoCodigo().getCodigo(), getData( dcData.getDate() ) ) );
            object.setQtd_vendida( MetodosUtil.getTotalQtdVendida( conexao, getCodigoArmazem(), stock.getCodProdutoCodigo().getCodigo(), getData( dcData.getDate() ) ) );
            object.setDesconto( MetodosUtil.getDescontoByProduto( conexao, getCodigoArmazem(), stock.getCodProdutoCodigo().getCodigo(), getData( dcData.getDate() ) ) );
            object.setQtd_actual( ( int ) MetodosUtil.getQuantidadeProduto( stock.getCodProdutoCodigo().getCodigo(), getCodigoArmazem(), conexao ) );
            object.setPreco_venda( preco.getPrecoVenda().doubleValue() );

            object.setTotal( ( object.getQtd_vendida() * object.getPreco_venda() ) - object.getDesconto() );
            lista.add( object );
        }

        new MapaExistencia( lista, armazemDao.findTbArmazem( getCodigoArmazem() ).getDesignacao(), dcData.getDate() );

    }

}
