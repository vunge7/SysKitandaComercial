/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lista;

import com.mysql.jdbc.Connection;
import controller.EntradaController;
import controller.ProdutoController;
import controller.UsuarioController;
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

/**
 *
 * @author Domingos Dala Vunge
 */
public class ListarRegistroDeEntradasPorEntervaloData extends javax.swing.JFrame {

    /**
     * Creates new form ListaUsuarioVisao
     */
    
    private BDConexao conexao;
    private int id_armzem = 0;
    
    public ListarRegistroDeEntradasPorEntervaloData() throws SQLException {
        
        initComponents();
        setResizable( false );
        setLocationRelativeTo( null );
        confLabel();        
        dcDataInicio.setDate( new Date() );
        dcDataFim.setDate( new Date() );  
        conexao = new BDConexao();
        cmbArmazem.setModel( new DefaultComboBoxModel( conexao.getElementos("tb_armazem", "designacao", false)   ) );       
        this.id_armzem = getCodigoArmazem();
        
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
        
        Connection connection = (Connection) conexao.conectar();
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
        Connection connection = (Connection) conexao.conectar();
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

     
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    
    
  
 
    
   
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnCancelar = new javax.swing.JButton();
        btnCancelar1 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        lbData = new javax.swing.JLabel();
        dcDataInicio = new com.toedter.calendar.JDateChooser();
        lbData1 = new javax.swing.JLabel();
        dcDataFim = new com.toedter.calendar.JDateChooser();
        lbStatus = new javax.swing.JLabel();
        cmbArmazem = new javax.swing.JComboBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("...:::: DVML - LISTAR VASILHAME :::...");

        btnCancelar.setText("IMPRIMIR");
        btnCancelar.setAlignmentX(0.5F);
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        btnCancelar1.setText("CANCELAR");
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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(82, Short.MAX_VALUE)
                .addComponent(lbData, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(dcDataInicio, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lbData1, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dcDataFim, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(83, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(112, 112, 112)
                .addComponent(lbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(cmbArmazem, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbArmazem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(dcDataInicio, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbData, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbData1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dcDataFim, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Cod. Vasilhame", "Descrição", "Produto", "Qtd Actual", "Usuario"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jButton1.setText("Pesquisar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCancelar1, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jScrollPane1))))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addGap(21, 21, 21))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
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
        try {
            mostrar();
        } catch (SQLException ex) {
            Logger.getLogger(ListarRegistroDeEntradasPorEntervaloData.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_btnCancelarActionPerformed

    private void cmbArmazemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbArmazemActionPerformed
        // TODO add your handling code here:

        id_armzem = getCodigoArmazem();


    }//GEN-LAST:event_cmbArmazemActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        
        
        adicionar_tabela(        conexao.getEntradaByIntervaloDeData(id_armzem, getDataInicio(), getDataFim() )          );
        
        
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        // TODO add your handling code here:
        
        if(evt.getClickCount() >= 2)
        {
            procedimento_eliminar();
        }
          
        
        
    }//GEN-LAST:event_jTable1MouseClicked

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
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ListarRegistroDeEntradasPorEntervaloData.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ListarRegistroDeEntradasPorEntervaloData.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ListarRegistroDeEntradasPorEntervaloData.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ListarRegistroDeEntradasPorEntervaloData.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new ListarRegistroDeEntradasPorEntervaloData().setVisible(true);
                } catch (SQLException ex) {
                    Logger.getLogger(ListarRegistroDeEntradasPorEntervaloData.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnCancelar1;
    private javax.swing.JComboBox cmbArmazem;
    private com.toedter.calendar.JDateChooser dcDataFim;
    private com.toedter.calendar.JDateChooser dcDataInicio;
    private javax.swing.JButton jButton1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel lbData;
    private javax.swing.JLabel lbData1;
    private javax.swing.JLabel lbStatus;
    // End of variables declaration//GEN-END:variables





    public void adicionar_tabela(Vector<EntradaModelo> entrada_modelos) {

        DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
        modelo.setRowCount(0);
        
        try {
            
             for (int i = 0; i < entrada_modelos.size(); i++) {

                modelo.addRow(new Object[]{
                    entrada_modelos.get(i).getIdEntrada(),
                    new ProdutoController(conexao).getProduto( entrada_modelos.get(i).getIdProduto() ).getDesignacao()    ,
                    entrada_modelos.get(i).getQuantidade(),
                    entrada_modelos.get(i).getData_entrada(),
                    new UsuarioController(conexao).getUsuario(entrada_modelos.get(i).getIdUsuario()).getNome()

                });

            }

            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
     
        
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
    
             EntradaModelo entradaModelo = new EntradaController(conexao).getEntrada(cod_entrada);
             ItemVendaModelo itemVendaModelo;
             int quantidade = 0;
          
             quantidade =   BDConexao.getQuantidade_Existente(  entradaModelo.getIdProduto() , entradaModelo.getIdArmazemFK() ) -  entradaModelo.getQuantidade();   
             conexao.executeUpdate("UPDATE tb_stock SET quantidade_existente = " + quantidade +" WHERE cod_produto_codigo = " +entradaModelo.getIdProduto()  + " AND cod_armazem = " +entradaModelo.getIdArmazemFK() );
       
            
// 
    }
    

   
   private void eliminar_entrada(EntradaModelo entradaModelo)
   {
   
      
       try {
            entradaModelo.setData_entrada("2015-12-01");
            new EntradaController(conexao).operacao(3, entradaModelo, false);
            
           
       } catch (Exception e) {
           JOptionPane.showMessageDialog(null, "Erro ao eliminar a entrada", "ERRO", JOptionPane.ERROR_MESSAGE);
       }
   
   
   }
   
   

   
   
   private void procedimento_eliminar()
   {
       
           try {
                 
                 DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
                 int idEntrada  =   Integer.parseInt(     String.valueOf(      modelo.getValueAt(   jTable1.getSelectedRow()  , 0)  )  ); 
                 int opcao = JOptionPane.showConfirmDialog(null, "Tens a certeza que pretendes eliminar esta entrada " +idEntrada  +" ? ");
                 
                if (opcao == JOptionPane.YES_OPTION )
                {
                      //Actualiza a quantidade existente no stock
                       actualizar_quantidade(idEntrada);
                       //Elimnada a entrada
                       eliminar_entrada(  new EntradaController(conexao).getEntrada( idEntrada )      );
                       //Actualiza a tabela
                       adicionar_tabela(        conexao.getEntradaByIntervaloDeData(id_armzem, getDataInicio(), getDataFim() ) );
                       //Emite a mensagemna tela
                       JOptionPane.showMessageDialog(null, "Entrada eliminada com sucesso!");
                }
                else JOptionPane.showMessageDialog(null, "Entrada não eliminada ", "AVISO", JOptionPane.WARNING_MESSAGE);
                 
                 
                 
                 
            } catch (Exception e) {
                e.printStackTrace();
                
            }
             
        }
        
       
       
   }





