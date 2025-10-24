/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rh.visao;


import java.sql.Connection;
import lista.*;
import dao.AccessoArmazemDao;
import dao.ArmazemDao;
import dao.FornecedorDao;
import dao.ItemVendaDao;
import dao.MesRhDao;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRXlsAbstractExporterParameter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.view.JasperViewer;
import static org.apache.poi.hssf.usermodel.HeaderFooter.date;
import util.BDConexao;
import util.JPAEntityMannagerFactoryUtil;
import util.MetodosUtil;

/**
 *
 * @author Domingos Dala Vunge
 */
public class ListarMapaSalario extends javax.swing.JFrame
{

    private EntityManagerFactory emf = JPAEntityMannagerFactoryUtil.em;
    private ItemVendaDao itemVendaDao = new ItemVendaDao( emf );
    private MesRhDao mesRhDao = new MesRhDao( emf );
    private ArmazemDao armazemDao = new ArmazemDao( emf );
    private FornecedorDao fornecedorDao = new FornecedorDao( emf );
    private BDConexao conexao;
    private AccessoArmazemDao accessoArmazemDao = new AccessoArmazemDao( emf );
    private String periodo = "", ano = "";
    private Date data;

    public ListarMapaSalario( int idUser, BDConexao conexao ) throws SQLException
    {

        initComponents();
        this.conexao = conexao;

        setResizable( false );
        setLocationRelativeTo( null );
        confLabel();
//        cmbPeriodo.setModel( new DefaultComboBoxModel( (Vector) mesRhDao.buscaTodos() ) );
////        String ano = String.valueOf( 1900 + date.getYear());
//        String mes_actual = String.valueOf( new Date().getMonth() );
//        cmbPeriodo.setSelectedItem( mes_actual );


        cmbPeriodo.setModel( new DefaultComboBoxModel( ( Vector ) mesRhDao.buscaTodos() ) );

        cmbPeriodo.setSelectedItem( MetodosUtil.getDescricaoMes( new Date().getMonth() ) );
//        dcDataInicio.setDate(new Date());com
//        dcDataFim.setDate(new Date());
//        cmbArmazem.setModel(new DefaultComboBoxModel(armazemDao.buscaTodos()));
//        cmbArmazem.setModel(new DefaultComboBoxModel(accessoArmazemDao.getAllArmazemByIdUSuario(idUser)));
//        accao_mostar_campo_fornecedor(false);
//        cmbFornecedor.setModel(new DefaultComboBoxModel(fornecedorDao.buscaTodos()));
    }

    //SELECT distinct  SUM(total_venda) as soma FROM tb_venda WHERE codigo_usuario = 4;
    public void confLabel()
    {

//        lbData.setHorizontalAlignment(JLabel.RIGHT);
    }

    public void mostrar() throws SQLException
    {

        java.sql.Connection connection = conexao.getConnectionAtiva();
        HashMap hashMap = new HashMap();

//        hashMap.put("DATA_INICIO", dcDataInicio.getDate());
//        hashMap.put("DATA_FIM", dcDataFim.getDate());
        String relatorio = "";

        relatorio = "relatorios/RelatorioDiario.jasper";

        File file = new File( relatorio ).getAbsoluteFile();

        String obterCaminho = file.getAbsolutePath();

        try
        {

            JasperFillManager.fillReport( obterCaminho, hashMap, connection );

            JasperPrint jasperPrint = JasperFillManager.fillReport( obterCaminho, hashMap, connection );

            if ( jasperPrint.getPages().size() >= 1 )
            {

                //int opcao = JOptionPane.showConfirmDialog(null, "Pretende Ver Os Dados Em Excel?");
//                if (opcao == JOptionPane.YES_OPTION)
//                    XLSX(obterCaminho, hashMap, connection);
//                else
//                {
                JasperViewer jasperViewer = new JasperViewer( jasperPrint, false );
                jasperViewer.setVisible( true );

                //}
            }
            else
            {
                JOptionPane.showMessageDialog( null, "Nao existe registro para o intervalo de data!..." );
            }
        }
        catch ( JRException jex )
        {
            jex.printStackTrace();
            //System.out.println("aqui");
            JOptionPane.showMessageDialog( null, "O Produto pode nao estar no stock \n Pf Verifique!..." );
        }
        catch ( Exception ex )
        {
            ex.printStackTrace();
            JOptionPane.showMessageDialog( null, "O Produto pode nao estar no stock \n Pf Verifique!..." );
        }

    }

    public static void XLSX( String jasperFilename, Map<String, Object> parameters, java.sql.Connection dataSource ) throws JRException, IOException
    {
        JasperPrint jasperPrint = JasperFillManager.fillReport( jasperFilename, parameters, dataSource );
        int indexOfPonto = jasperFilename.indexOf( '.' );
        String file = jasperFilename.substring( 0, indexOfPonto ) + ".xlsx";

        FileOutputStream output = new FileOutputStream( file );

        JRXlsxExporter docxExporter = new JRXlsxExporter();

        docxExporter.setParameter( JRExporterParameter.JASPER_PRINT, jasperPrint );
        docxExporter.setParameter( JRXlsAbstractExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE );
        docxExporter.setParameter( JRExporterParameter.OUTPUT_STREAM, output );
        docxExporter.setParameter( JRXlsAbstractExporterParameter.IS_DETECT_CELL_TYPE, Boolean.FALSE );
        docxExporter.exportReport();

        Runtime rt = Runtime.getRuntime();
        System.out.println( file );

        rt.exec( "RunDLL32.EXE shell32.dll,ShellExec_RunDLL " + file );
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        buttonGroup1 = new javax.swing.ButtonGroup();
        btnImprimir = new javax.swing.JButton();
        btnCancelar1 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        rbSalarioCompleto = new javax.swing.JRadioButton();
        rbINSS = new javax.swing.JRadioButton();
        jLabel3 = new javax.swing.JLabel();
        cmbPeriodo = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        rbIRT = new javax.swing.JRadioButton();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        rbSalarioBanco = new javax.swing.JRadioButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("...:::: DVML - MAPA SALARIOS:...");

        btnImprimir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/impressora1.png"))); // NOI18N
        btnImprimir.setText("Imprimir");
        btnImprimir.setAlignmentX(0.5F);
        btnImprimir.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnImprimirActionPerformed(evt);
            }
        });

        btnCancelar1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/LOGOUT - VERMELHO/Logout 32x32.png"))); // NOI18N
        btnCancelar1.setAlignmentX(0.5F);
        btnCancelar1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnCancelar1ActionPerformed(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        buttonGroup1.add(rbSalarioCompleto);
        rbSalarioCompleto.setFont(new java.awt.Font("Lucida Sans Typewriter", 0, 12)); // NOI18N
        rbSalarioCompleto.setSelected(true);

        buttonGroup1.add(rbINSS);
        rbINSS.setFont(new java.awt.Font("Lucida Sans Typewriter", 0, 12)); // NOI18N

        jLabel3.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        jLabel3.setText("Mapa Completo");

        jLabel4.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        jLabel4.setText("Mapa INSS");

        buttonGroup1.add(rbIRT);
        rbIRT.setFont(new java.awt.Font("Lucida Sans Typewriter", 0, 12)); // NOI18N

        jLabel5.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        jLabel5.setText("Mapa IRT");

        jLabel6.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        jLabel6.setText("Mapa Banco");

        buttonGroup1.add(rbSalarioBanco);
        rbSalarioBanco.setFont(new java.awt.Font("Lucida Sans Typewriter", 0, 12)); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(cmbPeriodo, javax.swing.GroupLayout.PREFERRED_SIZE, 289, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rbSalarioCompleto)
                .addGap(39, 39, 39)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rbSalarioBanco)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 38, Short.MAX_VALUE)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rbINSS)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rbIRT)
                .addGap(32, 32, 32))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(rbSalarioCompleto)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(rbINSS, javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(rbIRT)
                        .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(rbSalarioBanco)
                            .addComponent(jLabel6)
                            .addComponent(jLabel4))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(cmbPeriodo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(73, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(0, 0, 102));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jLabel2.setFont(new java.awt.Font("Lucida Grande", 1, 30)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("MAPA SALARIOS / IMPOSTOS");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 79, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(btnImprimir, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnCancelar1, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnImprimir)
                    .addComponent(btnCancelar1))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCancelar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelar1ActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_btnCancelar1ActionPerformed

    private void btnImprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImprimirActionPerformed

        if ( rbSalarioCompleto.isSelected() )
        {

            procedimento_imprimir_Mapa_Completo();

        }
        else if ( rbSalarioBanco.isSelected() )
        {

            procedimento_imprimir_Mapa_BANCo();

        }
        else if ( rbINSS.isSelected() )
        {

            procedimento_imprimir_Mapa_INSS();

        }
        else
        {

            procedimento_imprimir_Mapa_IRT();

        }

//        if (ck_fornecedor.isSelected()) {
//            new ResumoVendasQTD(dcDataInicio.getDate(), dcDataFim.getDate(), getCodigoArmazem(), getCodigoFornecedor());
//        } else {
//            new ResumoVendasQTD(dcDataInicio.getDate(), dcDataFim.getDate(), getCodigoArmazem());
//        }

    }//GEN-LAST:event_btnImprimirActionPerformed

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
            java.util.logging.Logger.getLogger( ListarMapaSalario.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        catch ( InstantiationException ex )
        {
            java.util.logging.Logger.getLogger( ListarMapaSalario.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        catch ( IllegalAccessException ex )
        {
            java.util.logging.Logger.getLogger( ListarMapaSalario.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        catch ( javax.swing.UnsupportedLookAndFeelException ex )
        {
            java.util.logging.Logger.getLogger( ListarMapaSalario.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                try
                {
                    new ListarMapaSalario( 15, BDConexao.getInstancia() ).setVisible( true );
                }
                catch ( SQLException ex )
                {
                    Logger.getLogger( ListarMapaSalario.class.getName() ).log( Level.SEVERE, null, ex );
                }
            }
        } );
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelar1;
    private javax.swing.JButton btnImprimir;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox<String> cmbPeriodo;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    public static javax.swing.JRadioButton rbINSS;
    public static javax.swing.JRadioButton rbIRT;
    public static javax.swing.JRadioButton rbSalarioBanco;
    public static javax.swing.JRadioButton rbSalarioCompleto;
    // End of variables declaration//GEN-END:variables

//    public int getCodigoArmazem() {
//        return armazemDao.getArmazemByDescricao(cmbArmazem.getSelectedItem().toString()).getCodigo();
//    }
    private void adicionar_tabela()
    {
//            DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
//            modelo.setRowCount(0);
//             List<RelatorioDiarioUtil>   lista = null;
//           
//             try {
//                 
//                 if (ck_fornecedor.isSelected()) {
//                     lista  = itemVendaDao.getRelatorioDiaio( dcDataInicio.getDate(), dcDataFim.getDate(), getCodigoArmazem(), getCodigoFornecedor() );
//                 }else lista  = itemVendaDao.getRelatorioDiaio( dcDataInicio.getDate(), dcDataFim.getDate(), getCodigoArmazem() );
//                   
//    
//                 
//                 for (RelatorioDiarioUtil objecto : lista) {
//            
//                            modelo.addRow( new Object[]{
//                                        
//                                objecto.getCodigo_produto(),
//                                objecto.getDesigancao(),
//                                 armazemDao.findTbArmazem( getCodigoArmazem()).getDesignacao(),
//                                objecto.getQuantidade()
//                                    
//                            
//                            });
//                            
//            
//                 }
//             
//            
//        } catch (Exception e) {
//            e.printStackTrace();
//            
//            JOptionPane.showMessageDialog(null, "Não há registro de vendas para esse armazém", DVML.DVML_COMERCIAL, JOptionPane.WARNING_MESSAGE);
//        }

    }

    private void procedimento_imprimir_Mapa_Completo()
    {

        new ResumoMapa(String.valueOf( cmbPeriodo.getSelectedItem() ) );

    }
    
    private void procedimento_imprimir_Mapa_BANCo()
    {

        new ResumoMapaBanco(String.valueOf( cmbPeriodo.getSelectedItem() ) );

    }
    
    private void procedimento_imprimir_Mapa_INSS()
    {

        new ResumoINSS(String.valueOf( cmbPeriodo.getSelectedItem() ) );

    }
    
    private void procedimento_imprimir_Mapa_IRT()
    {

        new ResumoIRT(String.valueOf( cmbPeriodo.getSelectedItem() ) );

    }

//    public int getCodigoFornecedor() {
//        return fornecedorDao.getFornecedorByDescricao(cmbFornecedor.getSelectedItem().toString()).getCodigo();
//    }
//
//    private void accao_mostar_campo_fornecedor(boolean estado) {
//        lbFornecedor.setVisible(estado);
//        cmbFornecedor.setVisible(estado);
//    }
}
