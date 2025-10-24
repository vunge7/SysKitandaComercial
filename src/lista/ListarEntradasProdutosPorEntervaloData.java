/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lista;


import java.sql.Connection;
import controller.EntradaController;
import dao.ArmazemDao;
import dao.EntradaDao;
import dao.FornecedorDao;
import entity.TbEntrada;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import modelo.EntradaModelo;
import modelo.ItemVendaModelo;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRXlsAbstractExporterParameter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.view.JasperViewer;
import util.BDConexao;
import util.JPAEntityMannagerFactoryUtil;
import util.MetodosUtil;
import static visao.EntradaProdutoVisao.cmbArmazem;

/**
 *
 * @author Domingos Dala Vunge
 */
public class ListarEntradasProdutosPorEntervaloData extends javax.swing.JFrame
{

    /**
     * Creates new form ListaUsuarioVisao
     */
    private BDConexao conexao;
    private int id_armzem = 0;
    private int idUser = 0;
    private EntityManagerFactory emf = JPAEntityMannagerFactoryUtil.em;

    private EntradaDao entradaDao = new EntradaDao( emf );
    private ArmazemDao armazemDao = new ArmazemDao( emf );
    private FornecedorDao fornecedorDao = new FornecedorDao( emf );

    public ListarEntradasProdutosPorEntervaloData( int idUser ) throws SQLException
    {

        initComponents();
        setResizable( false );
        setLocationRelativeTo( null );
        confLabel();

        dcDataInicio.setDate( new Date() );
        dcDataFim.setDate( new Date() );

        conexao = BDConexao.getInstancia();
        this.idUser = idUser;
        cmbArmazem.setModel( new DefaultComboBoxModel( (Vector) armazemDao.buscaTodos5() ) );
        MetodosUtil.setArmazemByCampoConfigArmazem( cmbArmazem, conexao, idUser );
//        accao_mostar_campo_fornecedor(false);
//        cmbFornecedor.setModel(new DefaultComboBoxModel(fornecedorDao.buscaTodos()));
//        id_armzem = getCodigoArmazem();
//        MetodosUtil.setArmazemByCampoConfigArmazem( cmbArmazem, conexao, idUser );
//        adicionar_tabela( entradaDao.getAllEntradasByIdArmazem( id_armzem, dcDataInicio.getDate(), dcDataFim.getDate() ) );
    }

    //SELECT distinct  SUM(total_venda) as soma FROM tb_venda WHERE codigo_usuario = 4;
    public void confLabel()
    {

        lbData.setHorizontalAlignment( JLabel.RIGHT );

    }

//    public String getDataSelecionadaString()
//    {
//        GregorianCalendar gc = new GregorianCalendar();
//        gc.setTime(dcDataInicio.getDate());
//        
//        String dataSelecionada = gc.get(GregorianCalendar.YEAR) + "-" +
//                        setTransforme( gc.get(GregorianCalendar.MONTH) + 1 ) + "-" +
//                        setTransforme( gc.get(GregorianCalendar.DATE) );
//        return dataSelecionada;
//    }
//    
//    public String setTransforme(int descrisao)
//    {
//            if(descrisao <=9 )
//                    return "0" +descrisao;
//            else return String.valueOf(descrisao);
//    }
//    
    public void mostrar() throws SQLException
    {

        java.sql.Connection connection = conexao.getConnectionAtiva();
        HashMap hashMap = new HashMap();

        System.out.println( "DATA_INICIO" + dcDataInicio.getDate() );

        hashMap.put( "DATA_INICIO", dcDataInicio.getDate() );
        hashMap.put( "DATA_FIM", dcDataFim.getDate() );
        hashMap.put( "ID_ARMAZEM", this.id_armzem );

        String relatorio = "";

        relatorio = "relatorios/EntradaProdutosPorDatas.jasper";

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

    public void mostrarFornecedor() throws SQLException
    {

        java.sql.Connection connection = conexao.getConnectionAtiva();
        HashMap hashMap = new HashMap();

        System.out.println( "DATA_INICIO" + dcDataInicio.getDate() );

        hashMap.put( "DATA_INICIO", dcDataInicio.getDate() );
        hashMap.put( "DATA_FIM", dcDataFim.getDate() );
        hashMap.put( "ID_ARMAZEM", this.id_armzem );
//        hashMap.put("ID_FORNECEDOR", getCodigoFornecedor() );
//        hashMap.put("FORNECEDOR", fornecedorDao.findTbFornecedor(getCodigoFornecedor()).getNome() );

        String relatorio = "";

        relatorio = "relatorios/EntradaProdutosPorDatasFornecedor.jasper";

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

    public int getCodigoArmazem()
    {
        return armazemDao.getArmazemByDescricao( cmbArmazem.getSelectedItem().toString() ).getCodigo();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings( "unchecked" )
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        btnCancelar = new javax.swing.JButton();
        btnCancelar1 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        lbData = new javax.swing.JLabel();
        dcDataInicio = new com.toedter.calendar.JDateChooser();
        lbData1 = new javax.swing.JLabel();
        dcDataFim = new com.toedter.calendar.JDateChooser();
        lbStatus = new javax.swing.JLabel();
        cmbArmazem = new javax.swing.JComboBox();
        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("...:::: DVML - LISTAR ENTRADA DE PRODUTOS:::...");

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

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        lbData.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 16)); // NOI18N
        lbData.setText("De");

        lbData1.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 16)); // NOI18N
        lbData1.setText("à");

        lbStatus.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 16)); // NOI18N
        lbStatus.setText("Armazém");

        cmbArmazem.setBackground(new java.awt.Color(0, 51, 102));
        cmbArmazem.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 14)); // NOI18N
        cmbArmazem.setForeground(new java.awt.Color(255, 255, 255));
        cmbArmazem.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Todos", "Activo", "Desactivo" }));
        cmbArmazem.setOpaque(true);
        cmbArmazem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbArmazemActionPerformed(evt);
            }
        });

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/actualizar_1_32x32_1.png"))); // NOI18N
        jButton1.setText("Pesquisar");
        jButton1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addComponent(lbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbArmazem, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addComponent(lbData, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(dcDataInicio, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lbData1, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dcDataFim, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton1)
                .addContainerGap(21, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(dcDataInicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbData, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbData1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(dcDataFim, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(lbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(cmbArmazem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(87, 87, 87))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(70, 70, 70))))
        );

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "Cod. Entrada", "Data Entrada", "Usuario"
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
        jScrollPane1.setViewportView(jTable1);

        jPanel2.setBackground(new java.awt.Color(0, 0, 102));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jLabel2.setFont(new java.awt.Font("Lucida Grande", 1, 30)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("REGISTRO DE ENTRADAS NO STOCK");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 866, Short.MAX_VALUE)
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
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCancelar1, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jScrollPane1)))
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 262, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCancelar)
                    .addComponent(btnCancelar1))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCancelar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelar1ActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_btnCancelar1ActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        try
        {
//            if (ck_fornecedor.isSelected()) {
//                 mostrarFornecedor();
//            }else 
            mostrar();

        }
        catch ( SQLException ex )
        {
            Logger.getLogger( ListarEntradasProdutosPorEntervaloData.class.getName() ).log( Level.SEVERE, null, ex );
        }

    }//GEN-LAST:event_btnCancelarActionPerformed

    private void cmbArmazemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbArmazemActionPerformed
        // TODO add your handling code here:

        id_armzem = getCodigoArmazem();
//        if (ck_fornecedor.isSelected()) {
//             adicionar_tabela( entradaDao.getAllEntradasByIdArmazem(id_armzem, getCodigoFornecedor(),  dcDataInicio.getDate(), dcDataFim.getDate() )  );
//        }else {
        adicionar_tabela( entradaDao.getAllEntradasByIdArmazem( id_armzem, dcDataInicio.getDate(), dcDataFim.getDate() ) );
//        }

    }//GEN-LAST:event_cmbArmazemActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

//        if ( cmbArmazem.getSelectedIndex() == 0 )
//        {
//            JOptionPane.showMessageDialog( null, "Por favor\nSeleccione um armazém válido!" );
//        }
//        else
//        {

            adicionar_tabela( entradaDao.getAllEntradasByIdArmazem( getIdArmazem(), dcDataInicio.getDate(), dcDataFim.getDate() ) );

//        }

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        // TODO add your handling code here:

//        if(evt.getClickCount() >= 2)
//        {
//            procedimento_eliminar();
//        }
//          

    }//GEN-LAST:event_jTable1MouseClicked

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
            java.util.logging.Logger.getLogger( ListarEntradasProdutosPorEntervaloData.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        catch ( InstantiationException ex )
        {
            java.util.logging.Logger.getLogger( ListarEntradasProdutosPorEntervaloData.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        catch ( IllegalAccessException ex )
        {
            java.util.logging.Logger.getLogger( ListarEntradasProdutosPorEntervaloData.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        catch ( javax.swing.UnsupportedLookAndFeelException ex )
        {
            java.util.logging.Logger.getLogger( ListarEntradasProdutosPorEntervaloData.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater( new Runnable()
        {
            public void run()
            {
                try
                {
                    new ListarEntradasProdutosPorEntervaloData( 15 ).setVisible( true );
                }
                catch ( SQLException ex )
                {
                    Logger.getLogger( ListarEntradasProdutosPorEntervaloData.class.getName() ).log( Level.SEVERE, null, ex );
                }
            }
        } );
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnCancelar1;
    private javax.swing.JComboBox cmbArmazem;
    private com.toedter.calendar.JDateChooser dcDataFim;
    private com.toedter.calendar.JDateChooser dcDataInicio;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel lbData;
    private javax.swing.JLabel lbData1;
    private javax.swing.JLabel lbStatus;
    // End of variables declaration//GEN-END:variables

    public void adicionar_tabela( List<TbEntrada> lista_entrada )
    {

        DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
        modelo.setRowCount( 0 );

        try
        {

            for ( int i = 0; i < lista_entrada.size(); i++ )
            {

                modelo.addRow( new Object[]
                {
                    lista_entrada.get( i ).getIdEntrada(),
                    lista_entrada.get( i ).getDataEntrada().getDate() + "/" + ( lista_entrada.get( i ).getDataEntrada().getMonth() + 1 ) + "/" + ( lista_entrada.get( i ).getDataEntrada().getYear() + 1900 ),
                    lista_entrada.get( i ).getIdUsuario().getNome()

                } );

            }

        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }

    }

    public String getDataInicio()
    {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime( dcDataInicio.getDate() );

        String dataSelecionada = gc.get( GregorianCalendar.YEAR ) + "-"
                + ( gc.get( GregorianCalendar.MONTH ) + 1 ) + "-"
                + gc.get( GregorianCalendar.DATE );
        return dataSelecionada;
    }

    public String getDataFim()
    {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime( dcDataFim.getDate() );

        String dataSelecionada = gc.get( GregorianCalendar.YEAR ) + "-"
                + ( gc.get( GregorianCalendar.MONTH ) + 1 ) + "-"
                + gc.get( GregorianCalendar.DATE );
        return dataSelecionada;
    }

    public void actualizar_quantidade( int cod_entrada )
    {

        EntradaModelo entradaModelo = new EntradaController( conexao ).getEntrada( cod_entrada );
        ItemVendaModelo itemVendaModelo;
        int quantidade = 0;

        quantidade = BDConexao.getQuantidade_Existente( entradaModelo.getIdProduto(), entradaModelo.getIdArmazemFK() ) - entradaModelo.getQuantidade();
        conexao.executeUpdate( "UPDATE tb_stock SET quantidade_existente = " + quantidade + " WHERE cod_produto_codigo = " + entradaModelo.getIdProduto() + " AND cod_armazem = " + entradaModelo.getIdArmazemFK() );

// 
    }

    private void eliminar_entrada( EntradaModelo entradaModelo )
    {

        try
        {
            entradaModelo.setData_entrada( "2015-12-01" );
            new EntradaController( conexao ).operacao( 3, entradaModelo, false );

        }
        catch ( Exception e )
        {
            JOptionPane.showMessageDialog( null, "Erro ao eliminar a entrada", "ERRO", JOptionPane.ERROR_MESSAGE );
        }

    }

    private void procedimento_eliminar()
    {

        try
        {

            DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
            int idEntrada = Integer.parseInt( String.valueOf( modelo.getValueAt( jTable1.getSelectedRow(), 0 ) ) );
            int opcao = JOptionPane.showConfirmDialog( null, "Tens a certeza que pretendes eliminar esta entrada " + idEntrada + " ? " );

            if ( opcao == JOptionPane.YES_OPTION )
            {
                //Actualiza a quantidade existente no stock
                actualizar_quantidade( idEntrada );
                //Elimnada a entrada
                eliminar_entrada( new EntradaController( conexao ).getEntrada( idEntrada ) );
                //Actualiza a tabela
                adicionar_tabela( entradaDao.getAllEntradasByIdArmazem( id_armzem, dcDataInicio.getDate(), dcDataFim.getDate() ) );

                //Emite a mensagemna tela
                JOptionPane.showMessageDialog( null, "Entrada eliminada com sucesso!" );
            }
            else
            {
                JOptionPane.showMessageDialog( null, "Entrada não eliminada ", "AVISO", JOptionPane.WARNING_MESSAGE );
            }

        }
        catch ( Exception e )
        {
            e.printStackTrace();

        }

    }

    private int getIdArmazem()
    {

        return armazemDao.getArmazemByDescricao( cmbArmazem.getSelectedItem().toString() ).getCodigo();

    }

//    private void accao_mostar_campo_fornecedor(boolean estado){
//        lbFornecedor.setVisible(estado);
//        cmbFornecedor.setVisible(estado); 
//    }
//    public int getCodigoFornecedor() {
//        return fornecedorDao.getFornecedorByDescricao(cmbFornecedor.getSelectedItem().toString()).getCodigo();
//    }
}
