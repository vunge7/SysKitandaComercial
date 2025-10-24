/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lista;


import java.sql.Connection;
import dao.SaidaVasilhameDao;
import dao.ItemSaidaVasilhameDao;
import dao.VasilhameDao;
import entity.TbEntradaVasilhame;
import entity.TbItemSaidaVasilhame;
import entity.TbProduto;
import entity.TbSaidaVasilhame;
import entity.TbVasilhame;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import util.BDConexao;
import util.JPAEntityMannagerFactoryUtil;

/**
 *
 * @author Domingos Dala Vunge
 */
public class ListarSaidasVasilhamesPorEntervaloData extends javax.swing.JFrame {

      
    private EntityManagerFactory emf = JPAEntityMannagerFactoryUtil.em;
    private TbProduto produto;
    private VasilhameDao vasilhameDao = new VasilhameDao(emf);
    private TbVasilhame vasilhame;
    private SaidaVasilhameDao saidaVasilhameDao = new SaidaVasilhameDao(emf);
    private TbEntradaVasilhame entradaVasilhame;
    private BDConexao conexao;
    private int id_armzem = 0;
    
    
    
    public ListarSaidasVasilhamesPorEntervaloData() throws SQLException {
        
        initComponents();
        setResizable(false);
        setLocationRelativeTo(null);
        confLabel();
        
        dcDataInicio.setDate( new Date() );
        dcDataFim.setDate( new Date() );
  
        conexao = BDConexao.getInstancia();
        cmbArmazem.setModel( new DefaultComboBoxModel( conexao.getElementos("tb_armazem", "designacao", false)   ) );
        
        id_armzem = getCodigoArmazem();
        
        
    }

    //SELECT distinct  SUM(total_venda) as soma FROM tb_venda WHERE codigo_usuario = 4;
    public void confLabel()
    {
            
            lbData.setHorizontalAlignment(JLabel.RIGHT);
    
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
    
    
    public void mostrar() throws SQLException {
        
        java.sql.Connection connection = conexao.getConnectionAtiva();
        HashMap hashMap = new HashMap();

        System.out.println("DATA_INICIO" +dcDataInicio.getDate() );
      
        hashMap.put("DATA_INICIO", dcDataInicio.getDate() );
        hashMap.put("DATA_FIM", dcDataFim.getDate() );
        hashMap.put("ID_ARMAZEM", this.id_armzem );

        String relatorio = "";

        relatorio = "relatorios/EntradaProdutosPorDatas.jasper";
       
        File file = new File(relatorio).getAbsoluteFile();
        
        String obterCaminho = file.getAbsolutePath();

        try {

            JasperFillManager.fillReport(obterCaminho, hashMap, connection);

            JasperPrint jasperPrint = JasperFillManager.fillReport(obterCaminho, hashMap, connection);

            if (jasperPrint.getPages().size() >= 1) 
            {
                
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
        
        
     
        hashMap.put("DATA_INICIO", dcDataInicio.getDate() );
        hashMap.put("DATA_FIM", "2014-04-16");
        
        relatorio = "relatorios/EntradaProdutosPorDatas.jasper";
      
       
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
 
    
    
//    
    
    
     public static void XLSX(String jasperFilename, Map<String, Object> parameters, java.sql.Connection dataSource) throws JRException, IOException 
     {
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperFilename, parameters, dataSource);
        int indexOfPonto = jasperFilename.indexOf('.');
        String file = jasperFilename.substring(0, indexOfPonto) + ".xlsx" ;
        
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
    
    
      
    public int getCodigoArmazem() 
    {
           return conexao.getCodigoPublico("tb_armazem", String.valueOf(  cmbArmazem.getSelectedItem() ) );   
    }

   
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabela_detalhes = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        tabela_saida = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        lbData = new javax.swing.JLabel();
        dcDataInicio = new com.toedter.calendar.JDateChooser();
        lbData1 = new javax.swing.JLabel();
        dcDataFim = new com.toedter.calendar.JDateChooser();
        lbStatus = new javax.swing.JLabel();
        cmbArmazem = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("...:::: DVML - LISTAR ENTRADA DE PRODUTOS::...");

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        tabela_detalhes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Cod. Vasilhame", "Vasilhame", "Produto", "Qtd"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tabela_detalhes);

        tabela_saida.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Cod. Saida", "Data", "Hora", "Armazém", "Funciónario", "Usuario"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tabela_saida.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabela_saidaMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tabela_saida);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 431, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 411, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        lbData.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 16)); // NOI18N
        lbData.setText("De");

        lbData1.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 16)); // NOI18N
        lbData1.setText("à");

        lbStatus.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 16)); // NOI18N
        lbStatus.setText("Armazém");

        cmbArmazem.setBackground(new java.awt.Color(51, 153, 0));
        cmbArmazem.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 14)); // NOI18N
        cmbArmazem.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Todos", "Activo", "Desactivo" }));
        cmbArmazem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbArmazemActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        jLabel1.setText("REGISTRO DE SAIDAS DE VASILHAMES");

        jButton3.setText("PESQUISAR");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 76, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(lbData, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(dcDataInicio, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(lbData1, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(lbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(cmbArmazem, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(dcDataFim, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap(26, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lbData, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(dcDataFim, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(lbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cmbArmazem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(dcDataInicio, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lbData1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jButton1.setText("IMPRIMIR");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("CANCELAR");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cmbArmazemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbArmazemActionPerformed
        // TODO add your handling code here:

        id_armzem = getCodigoArmazem();


    }//GEN-LAST:event_cmbArmazemActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        
        int opcao = JOptionPane.showConfirmDialog(null, "Deseja imprimir o relatorio detalhe?");
        

        if( opcao == JOptionPane.YES_OPTION   )
        {
                new RelatorioDetalheVasilhameSaida( getCodigoArmazem(),  dcDataInicio.getDate() , dcDataFim.getDate() );
        
        }else   new RelatorioCompletoVasilhameSaida( getCodigoArmazem(),  dcDataInicio.getDate() , dcDataFim.getDate() );
        
        
        
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        
        id_armzem = getCodigoArmazem();
        
        try {
            esvaziar_tabela_detalhe();
        } catch (Exception e) {
        }
        
        
        adicionar_tabela_saida_vasilhame(  saidaVasilhameDao.getAllSaidaVasilhameByIdArmazem(id_armzem, dcDataInicio.getDate(), dcDataFim.getDate()
        )      );
        
        
        
        
        
        
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void tabela_saidaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabela_saidaMouseClicked
        // TODO add your handling code here:
        
        
        if(evt.getClickCount() >= 1)
        {
            procedimento_detalhes_saida();
        }
          
        
        
        
    }//GEN-LAST:event_tabela_saidaMouseClicked

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
            java.util.logging.Logger.getLogger(ListarSaidasVasilhamesPorEntervaloData.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ListarSaidasVasilhamesPorEntervaloData.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ListarSaidasVasilhamesPorEntervaloData.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ListarSaidasVasilhamesPorEntervaloData.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new ListarSaidasVasilhamesPorEntervaloData().setVisible(true);
                } catch (SQLException ex) {
                    Logger.getLogger(ListarSaidasVasilhamesPorEntervaloData.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox cmbArmazem;
    private com.toedter.calendar.JDateChooser dcDataFim;
    private com.toedter.calendar.JDateChooser dcDataInicio;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lbData;
    private javax.swing.JLabel lbData1;
    private javax.swing.JLabel lbStatus;
    private javax.swing.JTable tabela_detalhes;
    private javax.swing.JTable tabela_saida;
    // End of variables declaration//GEN-END:variables



    public void adicionar_tabela_saida_vasilhame(List<TbSaidaVasilhame> saida_vasilhames) {

        DefaultTableModel modelo = (DefaultTableModel) tabela_saida.getModel();
        modelo.setRowCount(0);
        
        try {
            
             for (int i = 0; i < saida_vasilhames.size(); i++) {

                modelo.addRow(new Object[]{
                    saida_vasilhames.get(i).getPkSaidaVasilhame(),
                    getData (     saida_vasilhames.get(i).getDataSaida() ),
                    getHora( saida_vasilhames.get(i).getHoraSaida() ),
                    saida_vasilhames.get(i).getFkArmazem().getDesignacao(),
                    saida_vasilhames.get(i).getNomeFuncionario(),
                    saida_vasilhames.get(i).getFkUsuario().getNome()
                   
                });

            }

            
        } catch (Exception e) {
            System.out.println("CHEGUEI DALLAS");
           
            e.printStackTrace();
        }
        
     
        
    }
    

    
    
    private void esvaziar_tabela_detalhe()
    {
         adicionar_tabela_item_saida_vasilhame(null);
    }
    
    
     public void adicionar_tabela_item_saida_vasilhame(List<TbItemSaidaVasilhame> item_saida_vasilhames) {

        DefaultTableModel modelo = (DefaultTableModel) tabela_detalhes.getModel();
        modelo.setRowCount(0);
        
//        try {
//            
////             for (int i = 0; i < item_saida_vasilhames.size(); i++) {
////
////                modelo.addRow(new Object[]{
////                    item_saida_vasilhames.get(i).getFkVasihame().getPkVasilhame(),
////                    item_saida_vasilhames.get(i).getFkVasihame().getDescricao(),
////                    item_saida_vasilhames.get(i).getFkVasihame().getFkProduto().getDesignacao(),
////                    item_saida_vasilhames.get(i).getQtd()
////                    
////                });
//
//            }
//
//            
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        
     
        
    }
    






  public String getDataInicio()
    {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(dcDataInicio.getDate());
        
        String dataSelecionada = gc.get(GregorianCalendar.YEAR) + "-" +
                        (gc.get(GregorianCalendar.MONTH) + 1) + "-" +
                        gc.get(GregorianCalendar.DATE);
        return dataSelecionada;
    }
    
  
    public String getData(Date dcData)
    {
        return dcData.getDate()  +" - " +(dcData.getMonth() + 1) + " - " +dcData.getYear();
    }
 
  public String getHora(Date hora)
    {
        return hora.getHours()  +":" +hora.getMinutes()  + ":" +hora.getSeconds();
    }
  
  
 public String getDataFim()
    {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(dcDataFim.getDate());
        
        String dataSelecionada = gc.get(GregorianCalendar.YEAR) + "-" +
                        (gc.get(GregorianCalendar.MONTH) + 1) + "-" +
                        gc.get(GregorianCalendar.DATE);
        return dataSelecionada;
    }
    


 
 
 
   public void actualizar_quantidade(int cod_entrada){
    
//             TbEntradaVasilhame entradaModelo = new EntradaController(conexao).getEntrada(cod_entrada);
//             ItemVendaModelo itemVendaModelo;
//             int quantidade = 0;
//          
//             quantidade =   BDConexao.getQuantidade_Existente(  entradaModelo.getIdProduto() , entradaModelo.getIdArmazemFK() ) -  entradaModelo.getQuantidade();   
//             conexao.executeUpdate("UPDATE tb_stock SET quantidade_existente = " + quantidade +" WHERE cod_produto_codigo = " +entradaModelo.getIdProduto()  + " AND cod_armazem = " +entradaModelo.getIdArmazemFK() );
//       
            
// 
    }
    

   
   private void eliminar_entrada(TbEntradaVasilhame entradaModelo)
   {
   
      
//       try {
//            entradaModelo.setData_entrada("2015-12-01");
//            new EntradaController(conexao).operacao(3, entradaModelo, false);
//            
//           
//       } catch (Exception e) {
//           JOptionPane.showMessageDialog(null, "Erro ao eliminar a entrada", "ERRO", JOptionPane.ERROR_MESSAGE);
//       }
//   
   
   }
   
   
   private void procedimento_detalhes_saida()
   {
       
           try {
                 
                 DefaultTableModel modelo = (DefaultTableModel) tabela_saida.getModel();
                 int idSaidaVasilhame  =   Integer.parseInt(     String.valueOf(      modelo.getValueAt(   tabela_saida.getSelectedRow()  , 0)  )  ); 
                 adicionar_tabela_item_saida_vasilhame(    new ItemSaidaVasilhameDao(emf).getAllSaidaVasilhameByIdArmazem(idSaidaVasilhame) );
                 
            } catch (Exception e) {
                e.printStackTrace();
                
            }      
    }
       
}





