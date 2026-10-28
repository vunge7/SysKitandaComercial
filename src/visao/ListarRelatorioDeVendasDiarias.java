/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package visao;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import util.BDConexao;

/**
 *
 * @author Domingos Dala Vunge
 */
public class ListarRelatorioDeVendasDiarias extends javax.swing.JFrame {

    // Ficheiros dos relatórios (compilados no iReport)
    private static final String REL_DIARIO_HORIZONTAL = "relatorios/relatorio_vendas_diarias_horizontal.jasper";
    private static final String REL_DIARIO_VERTICAL = "relatorios/relatorio_vendas_diarias.jasper";
    private static final String REL_ANUAL = "relatorios/relatorio_vendas_anual.jasper";

    // Nome do parâmetro do ano no relatório anual (Integer)
    private static final String PARM_ANO = "PARM_ANO";

    private BDConexao conexao;


    public ListarRelatorioDeVendasDiarias(int idUser, BDConexao conexao) throws SQLException {

        initComponents();
        setResizable(false);
        setLocationRelativeTo(null);
        confLabel();

        dcDataInicio.setDate(new Date());
        dcDataFim.setDate(new Date());

        this.conexao = conexao;

        carregarAnos();
        configurarOpcoes();

    }

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

   
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        btnImprimir = new javax.swing.JButton();
        btnCancelar1 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        lbData = new javax.swing.JLabel();
        dcDataInicio = new com.toedter.calendar.JDateChooser();
        lbData1 = new javax.swing.JLabel();
        dcDataFim = new com.toedter.calendar.JDateChooser();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jCheckBox1 = new javax.swing.JCheckBox();
        jCheckBox2 = new javax.swing.JCheckBox();
        jCheckBox3 = new javax.swing.JCheckBox();
        lbAno = new javax.swing.JLabel();
        cbAno = new javax.swing.JComboBox();

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

        buttonGroup2.add(jCheckBox1);
        jCheckBox1.setSelected(true);
        jCheckBox1.setText("Opção na Horizontal");

        buttonGroup2.add(jCheckBox2);
        jCheckBox2.setText("Opção na Vertical");

        buttonGroup2.add(jCheckBox3);
        jCheckBox3.setText("Relatório Anual");

        lbAno.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 16)); // NOI18N
        lbAno.setText("Ano");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jCheckBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnImprimir, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCancelar1, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jCheckBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jCheckBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbAno, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbAno, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jCheckBox1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jCheckBox2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox3)
                    .addComponent(lbAno)
                    .addComponent(cbAno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnImprimir)
                    .addComponent(btnCancelar1))
                .addGap(26, 26, 26))
        );

        pack();
    }// </editor-fold>                        

    private void btnCancelar1ActionPerformed(java.awt.event.ActionEvent evt) {                                             
        // TODO add your handling code here:
        dispose();
    }                                            

    private void btnImprimirActionPerformed(java.awt.event.ActionEvent evt) {                                            

        procedimento_imprimir();

    }                                           

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
            java.util.logging.Logger.getLogger(ListarRelatorioDeVendasDiarias.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ListarRelatorioDeVendasDiarias.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ListarRelatorioDeVendasDiarias.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ListarRelatorioDeVendasDiarias.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new ListarRelatorioDeVendasDiarias(15, BDConexao.getInstancia()).setVisible(true);
                } catch (SQLException ex) {
                    Logger.getLogger(ListarRelatorioDeVendasDiarias.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    // Variables declaration - do not modify                     
    private javax.swing.JButton btnCancelar1;
    private javax.swing.JButton btnImprimir;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JComboBox cbAno;
    private com.toedter.calendar.JDateChooser dcDataFim;
    private com.toedter.calendar.JDateChooser dcDataInicio;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JCheckBox jCheckBox3;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel lbAno;
    private javax.swing.JLabel lbData;
    private javax.swing.JLabel lbData1;
    // End of variables declaration                   


    // =====================================================================
    //  ANOS (ComboBox) E ESTADO DOS CONTROLOS
    // =====================================================================

    /**
     * Carrega na combobox os anos existentes em tb_venda (mais recente primeiro)
     * e selecciona automaticamente o ano actual.
     * Se o ano actual ainda não tiver vendas, é acrescentado na mesma.
     */
    @SuppressWarnings("unchecked")
    private void carregarAnos() {

        DefaultComboBoxModel model = new DefaultComboBoxModel();
        int anoActual = new GregorianCalendar().get(GregorianCalendar.YEAR);

        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = BDConexao.getConnection();
            ps = connection.prepareStatement(
                    "SELECT DISTINCT YEAR(dataVenda) AS ano FROM tb_venda "
                    + "WHERE dataVenda IS NOT NULL ORDER BY ano DESC");
            rs = ps.executeQuery();
            while (rs.next()) {
                model.addElement(Integer.valueOf(rs.getInt("ano")));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            fecharLigacao(connection);
        }

        // Garante que o ano actual existe na lista (mesmo sem vendas)
        Integer actual = Integer.valueOf(anoActual);
        if (model.getIndexOf(actual) < 0) {
            model.insertElementAt(actual, 0);
        }

        cbAno.setModel(model);
        cbAno.setSelectedItem(actual);
    }

    /**
     * Liga os checkboxes ao estado dos campos: no "Relatório Anual" usa-se o
     * Ano; nas outras opções usam-se as datas.
     */
    private void configurarOpcoes() {

        java.awt.event.ActionListener ouvinte = new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                actualizarControlos();
            }
        };

        jCheckBox1.addActionListener(ouvinte);
        jCheckBox2.addActionListener(ouvinte);
        jCheckBox3.addActionListener(ouvinte);

        actualizarControlos();
    }

    private void actualizarControlos() {

        boolean anual = jCheckBox3.isSelected();

        lbData.setEnabled(!anual);
        lbData1.setEnabled(!anual);
        dcDataInicio.setEnabled(!anual);
        dcDataFim.setEnabled(!anual);

        lbAno.setEnabled(anual);
        cbAno.setEnabled(anual);
    }

    // =====================================================================
    //  IMPRESSÃO
    // =====================================================================

    /**
     * Escolhe o relatório conforme o checkbox seleccionado:
     *  - Horizontal  -> relatorio_vendas_diarias.jasper (intervalo de datas)
     *  - Vertical    -> relatorio_vendas_diarias_vertical.jasper (intervalo de datas)
     *  - Anual       -> relatorio_vendas_anual.jasper (ano da combobox)
     */
    private void procedimento_imprimir() {

        HashMap<String, Object> parametros = new HashMap<String, Object>();

        // ---- Relatório anual: usa o ano seleccionado ----
        if (jCheckBox3.isSelected()) {

            Object seleccionado = cbAno.getSelectedItem();
            if (seleccionado == null) {
                JOptionPane.showMessageDialog(this,
                        "Seleccione o ano!...",
                        "DVML-COMERCIAL, LDA", JOptionPane.WARNING_MESSAGE);
                return;
            }

            parametros.put(PARM_ANO, (Integer) seleccionado);
            mostrarRelatorio(REL_ANUAL, parametros,
                    "Não existe registo para o ano " + seleccionado + "!...");
            return;
        }

        // ---- Relatórios diários: usam o intervalo de datas ----
        Date dataInicio = dcDataInicio.getDate();
        Date dataFim = dcDataFim.getDate();

        if (dataInicio == null || dataFim == null) {
            JOptionPane.showMessageDialog(this,
                    "Seleccione a data inicial e a data final!...",
                    "DVML-COMERCIAL, LDA", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (semHora(dataInicio).after(semHora(dataFim))) {
            JOptionPane.showMessageDialog(this,
                    "A data inicial não pode ser posterior à data final!...",
                    "DVML-COMERCIAL, LDA", JOptionPane.WARNING_MESSAGE);
            return;
        }

        parametros.put("PARM_DATA_1", dataInicio);
        parametros.put("PARM_DATA_2", dataFim);

        String ficheiro = jCheckBox2.isSelected() ? REL_DIARIO_VERTICAL : REL_DIARIO_HORIZONTAL;
        mostrarRelatorio(ficheiro, parametros,
                "Não existe registo para esse intervalo de datas!...");
    }

    /**
     * Devolve a mesma data com a hora zerada, para comparar apenas o dia.
     */
    private Date semHora(Date data) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(data);
        gc.set(GregorianCalendar.HOUR_OF_DAY, 0);
        gc.set(GregorianCalendar.MINUTE, 0);
        gc.set(GregorianCalendar.SECOND, 0);
        gc.set(GregorianCalendar.MILLISECOND, 0);
        return gc.getTime();
    }

    /**
     * Preenche e mostra um relatório do JasperReports.
     *
     * @param relatorio caminho do ficheiro .jasper
     * @param parametros parâmetros a enviar ao relatório
     * @param msgSemDados mensagem quando o relatório não tem registos
     */
    private void mostrarRelatorio(String relatorio, HashMap<String, Object> parametros, String msgSemDados) {

        File ficheiro = new File(relatorio).getAbsoluteFile();

        if (!ficheiro.exists()) {
            JOptionPane.showMessageDialog(this,
                    "Relatório não encontrado:\n" + ficheiro.getAbsolutePath(),
                    "DVML-COMERCIAL, LDA", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Connection connection = null;
        try {
            connection = BDConexao.getConnection();

            // Preenche o relatório uma única vez
            JasperPrint jasperPrint = JasperFillManager.fillReport(ficheiro.getAbsolutePath(), parametros, connection);

            if (jasperPrint.getPages().size() >= 1) {
                JasperViewer jasperViewer = new JasperViewer(jasperPrint, false);
                jasperViewer.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, msgSemDados,
                        "ERROR", JOptionPane.ERROR_MESSAGE);
            }

        } catch (JRException jex) {
            jex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Falha ao mostrar o relatório",
                    "DVML-COMERCIAL, LDA", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Erro ao tentar mostrar o relatório",
                    "DVML-COMERCIAL, LDA", JOptionPane.ERROR_MESSAGE);
        } finally {
            fecharLigacao(connection);
        }
    }

    /**
     * Fecha a ligação à base de dados.
     * Se BDConexao.getConnection() devolver sempre a MESMA ligação partilhada,
     * deixe este método vazio para não fechar a ligação usada pela aplicação.
     */
    private void fecharLigacao(Connection connection) {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
