/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lista;


import java.sql.Connection;
import dao.AccessoArmazemDao;
import dao.ArmazemDao;
import dao.FornecedorDao;
import dao.ItemVendaDao;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRXlsAbstractExporterParameter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.view.JasperViewer;
import util.BDConexao;
import util.JPAEntityMannagerFactoryUtil;

/**
 *
 * @author Domingos Dala Vunge
 */
public class ListarRelatorioVendasDiarias extends javax.swing.JFrame {

    private EntityManagerFactory emf = JPAEntityMannagerFactoryUtil.em;
    private ItemVendaDao itemVendaDao = new ItemVendaDao(emf);
    private ArmazemDao armazemDao = new ArmazemDao(emf);
    private FornecedorDao fornecedorDao = new FornecedorDao(emf);
    private BDConexao conexao;
    private AccessoArmazemDao accessoArmazemDao = new AccessoArmazemDao(emf);

    public ListarRelatorioVendasDiarias(int idUser, BDConexao conexao) throws SQLException {

        initComponents();
        setResizable(false);
        setLocationRelativeTo(null);
        confLabel();

        dcDataInicio.setDate(new Date());
        dcDataFim.setDate(new Date());


        this.conexao = conexao;

    }

    //SELECT distinct  SUM(total_venda) as soma FROM tb_venda WHERE codigo_usuario = 4;
    public void confLabel() {

        lbData.setHorizontalAlignment(JLabel.RIGHT);

    }

    public String getDataInicio() {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(dcDataInicio.getDate());

        String dataSelecionada = gc.get(GregorianCalendar.YEAR) + "-"
                + (gc.get(GregorianCalendar.MONTH) + 1) + "-"
                + gc.get(GregorianCalendar.DATE);
        return dataSelecionada;
    }

    public String getDataFim() {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(dcDataFim.getDate());

        String dataSelecionada = gc.get(GregorianCalendar.YEAR) + "-"
                + (gc.get(GregorianCalendar.MONTH) + 1) + "-"
                + gc.get(GregorianCalendar.DATE);
        return dataSelecionada;
    }

    public void mostrar() throws SQLException {

        java.sql.Connection connection = conexao.getConnectionAtiva();
        HashMap hashMap = new HashMap();

        hashMap.put("DATA_INICIO", dcDataInicio.getDate());
        hashMap.put("DATA_FIM", dcDataFim.getDate());

        String relatorio = "";

        relatorio = "relatorios/RelatorioDiario.jasper";

        File file = new File(relatorio).getAbsoluteFile();

        String obterCaminho = file.getAbsolutePath();

        try {

            JasperFillManager.fillReport(obterCaminho, hashMap, connection);

            JasperPrint jasperPrint = JasperFillManager.fillReport(obterCaminho, hashMap, connection);

            if (jasperPrint.getPages().size() >= 1) {

                //int opcao = JOptionPane.showConfirmDialog(null, "Pretende Ver Os Dados Em Excel?");
//                if (opcao == JOptionPane.YES_OPTION)
//                    XLSX(obterCaminho, hashMap, connection);
//                else
//                {
                JasperViewer jasperViewer = new JasperViewer(jasperPrint, false);
                jasperViewer.setVisible(true);

                //}
            } else {
                JOptionPane.showMessageDialog(null, "Nao existe registro para o intervalo de data!...");
            }
        } catch (JRException jex) {
            jex.printStackTrace();
            //System.out.println("aqui");
            JOptionPane.showMessageDialog(null, "O Produto pode nao estar no stock \n Pf Verifique!...");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "O Produto pode nao estar no stock \n Pf Verifique!...");
        }

    }

    public void mostrar2() throws SQLException {
        java.sql.Connection connection = conexao.getConnectionAtiva();
        HashMap hashMap = new HashMap();
        String relatorio = "";

        hashMap.put("DATA_INICIO", getDataInicio());
        hashMap.put("DATA_FIM", "2014-04-16");

        relatorio = "relatorios/EntradaProdutos.jasper";

        File file = new File(relatorio).getAbsoluteFile();
        String obterCaminho = file.getAbsolutePath();

        try {

            JasperFillManager.fillReport(obterCaminho, hashMap, connection);

            JasperPrint jasperPrint = JasperFillManager.fillReport(obterCaminho, hashMap, connection);

            if (jasperPrint.getPages().size() >= 1) {
                JasperViewer jasperViewer = new JasperViewer(jasperPrint, false);

                jasperViewer.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(null, "Nao Existem Usuarios Para essa Listagen!...");
            }
        } catch (JRException jex) {
            jex.printStackTrace();
            //System.out.println("aqui");
            JOptionPane.showMessageDialog(null, "FALHA AO TENTAR MOSTRAR OS USUARIOS!...");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "ERRO AO TENTAR OS USUARIOS!...");
        }
    }

    public static void XLSX(String jasperFilename, Map<String, Object> parameters, java.sql.Connection dataSource) throws JRException, IOException {
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperFilename, parameters, dataSource);
        int indexOfPonto = jasperFilename.indexOf('.');
        String file = jasperFilename.substring(0, indexOfPonto) + ".xlsx";

        FileOutputStream output = new FileOutputStream(file);

        JRXlsxExporter docxExporter = new JRXlsxExporter();

        docxExporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        docxExporter.setParameter(JRXlsAbstractExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
        docxExporter.setParameter(JRExporterParameter.OUTPUT_STREAM, output);
        docxExporter.setParameter(JRXlsAbstractExporterParameter.IS_DETECT_CELL_TYPE, Boolean.FALSE);
        docxExporter.exportReport();

        Runtime rt = Runtime.getRuntime();
        System.out.println(file);

        rt.exec("RunDLL32.EXE shell32.dll,ShellExec_RunDLL " + file);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        btnImprimir = new javax.swing.JButton();
        btnCancelar1 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        lbData = new javax.swing.JLabel();
        dcDataInicio = new com.toedter.calendar.JDateChooser();
        lbData1 = new javax.swing.JLabel();
        dcDataFim = new com.toedter.calendar.JDateChooser();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("...:::: DVML - RELATORIO VENDAS::...");

        btnImprimir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/impressora1.png"))); // NOI18N
        btnImprimir.setText("Imprimir");
        btnImprimir.setAlignmentX(0.5F);
        btnImprimir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImprimirActionPerformed(evt);
            }
        });

        btnCancelar1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/LOGOUT - VERMELHO/Logout 32x32.png"))); // NOI18N
        btnCancelar1.setAlignmentX(0.5F);
        btnCancelar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelar1ActionPerformed(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        lbData.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 16)); // NOI18N
        lbData.setText("De");

        lbData1.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 16)); // NOI18N
        lbData1.setText("à");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbData, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(dcDataInicio, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lbData1, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dcDataFim, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(dcDataInicio, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 27, Short.MAX_VALUE)
                    .addComponent(dcDataFim, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lbData, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lbData1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(0, 0, 102));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jLabel2.setFont(new java.awt.Font("Lucida Grande", 1, 30)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("RELATÓRIO DE VENDAS RESUMIDAS");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 558, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(81, 81, 81)
                        .addComponent(btnImprimir, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnCancelar1, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnImprimir)
                    .addComponent(btnCancelar1))
                .addGap(39, 39, 39))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCancelar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelar1ActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_btnCancelar1ActionPerformed

    private void btnImprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImprimirActionPerformed


            procedimento_imprimir();



    }//GEN-LAST:event_btnImprimirActionPerformed

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
            java.util.logging.Logger.getLogger(ListarRelatorioVendasDiarias.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ListarRelatorioVendasDiarias.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ListarRelatorioVendasDiarias.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ListarRelatorioVendasDiarias.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new ListarRelatorioVendasDiarias(15, BDConexao.getInstancia()).setVisible(true);
                } catch (SQLException ex) {
                    Logger.getLogger(ListarRelatorioVendasDiarias.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelar1;
    private javax.swing.JButton btnImprimir;
    private javax.swing.ButtonGroup buttonGroup1;
    private com.toedter.calendar.JDateChooser dcDataFim;
    private com.toedter.calendar.JDateChooser dcDataInicio;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel lbData;
    private javax.swing.JLabel lbData1;
    // End of variables declaration//GEN-END:variables


    private void procedimento_imprimir() {

            new ResumoVendasDiario(dcDataInicio.getDate(), dcDataFim.getDate());

    }


}
