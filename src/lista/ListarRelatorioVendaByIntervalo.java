/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lista;

import comercial.controller.ArmazensController;
import comercial.controller.ItemVendasController;
import comercial.controller.PrecosController;
import comercial.controller.ProdutosController;
import comercial.controller.StoksController;
import comercial.controller.TipoProdutosController;
import comercial.controller.UsuariosController;
import comercial.controller.VendasController;
import entity.Compras;
import entity.TbArmazem;
import entity.TbDadosInstituicao;
import entity.TbPreco;
import entity.TbProduto;
import entity.TbStock;
import entity.TbTipoProduto;
import entity.TbUsuario;
import entity.TbVenda;
import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import util.BDConexao;
import util.DVML;
import util.MetodosUtil;
import static visao.VendaUsuarioVisao.formatarComoMoeda;
import util.DVML.Abreviacao;

/**
 *
 * @author Martinho Luis
 */
public class ListarRelatorioVendaByIntervalo extends javax.swing.JFrame
{

    /**
     * Creates new form ListaUsuarioVisao
     */
    private static PrecosController precosController;
    private static ProdutosController produtosController;
    private static TipoProdutosController tipoProdutosController;
    private static StoksController stocksController;
    private static ArmazensController armazensController;
    private static UsuariosController usuariosController;
    private static ItemVendasController itemVendasController;
    private static VendasController vendasController;
    private static TbArmazem armazem;
    private static TbStock stock_local;
    private static Compras compra;
    private static TbPreco preco;
    private static TbProduto produto;
    private static TbUsuario usuario;
    public static TbDadosInstituicao dadosInstituicao;
    private static BDConexao conexao;
    private static BDConexao conexaoTransaction;
    private static TbStock stock;
    private TbTipoProduto tipoProduto;
    private List<TbVenda> lista = null;
    private static Abreviacao abreviacao;

    public ListarRelatorioVendaByIntervalo( BDConexao conexao )
    {

        initComponents();
        setResizable( false );
        setLocationRelativeTo( null );
        this.conexao = conexao;
        precosController = new PrecosController( conexao );
        produtosController = new ProdutosController( conexao );
        tipoProdutosController = new TipoProdutosController( conexao );
        stocksController = new StoksController( conexao );
        armazensController = new ArmazensController( conexao );
        usuariosController = new UsuariosController( conexao );
        itemVendasController = new ItemVendasController( conexao );
        vendasController = new VendasController( conexao );
        dcDataInicio.setDate( new Date() );
        dcDataFim.setDate( new Date() );

        init();

    }

    private void init()
    {
        cmbUsuario.setModel( new DefaultComboBoxModel<>( usuariosController.getVector() ) );

    }

    @SuppressWarnings( "unchecked" )
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        lbData = new javax.swing.JLabel();
        dcDataInicio = new com.toedter.calendar.JDateChooser();
        lbData1 = new javax.swing.JLabel();
        dcDataFim = new com.toedter.calendar.JDateChooser();
        jButton1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        cmbUsuario = new javax.swing.JComboBox<>();
        lb_total = new javax.swing.JLabel();
        lbData2 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        btnSalvar = new javax.swing.JButton();
        btnCancelar1 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("...:::: DVML - RELATORIO DIARIO::...");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        lbData.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 16)); // NOI18N
        lbData.setText("De");

        lbData1.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 16)); // NOI18N
        lbData1.setText("à");

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/actualizar_1_32x32.png"))); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel1.setText("Usuário:");

        lb_total.setText(".");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 283, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lbData, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(dcDataInicio, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lbData1, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dcDataFim, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lb_total, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jButton1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(dcDataInicio, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lbData1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dcDataFim, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lbData, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel1)
                        .addComponent(cmbUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lb_total)
                .addContainerGap())
        );

        lbData2.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 24)); // NOI18N
        lbData2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbData2.setText("RELATÓRIO DE VENDAS DETALHADAS POR USUÁRIO E POR INTERVALO DE DATAS");

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        btnSalvar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/impressora1.png"))); // NOI18N
        btnSalvar.setText("Imprimir");
        btnSalvar.setAlignmentX(0.5F);
        btnSalvar.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnSalvarActionPerformed(evt);
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

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnSalvar, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCancelar1)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSalvar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnCancelar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "Doc. Ref Nº", "Cliente ", "Data", "Hora", "Total"
            }
        )
        {
            Class[] types = new Class []
            {
                java.lang.Object.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean []
            {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex)
            {
                return types [columnIndex];
            }

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

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1128, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 468, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lbData2, javax.swing.GroupLayout.DEFAULT_SIZE, 1136, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(lbData2, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCancelar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelar1ActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_btnCancelar1ActionPerformed

    private void btnSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalvarActionPerformed

            new ResumoVendasByUsuarioByIntervalo( dcDataInicio.getDate(), dcDataFim.getDate() );

    }//GEN-LAST:event_btnSalvarActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        adicionar_tabela();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_jTable1MouseClicked
    {//GEN-HEADEREND:event_jTable1MouseClicked
        if ( evt.getClickCount() > 1 )
        {

            reimprimir_FR();

        }
    }//GEN-LAST:event_jTable1MouseClicked

//    private void reimprimir_FR()
//    {
//        DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
//        int selectedRow = jTable1.getSelectedRow();
//        String codRef = modelo.getValueAt( selectedRow, 0 ).toString();
//
//        procedimento_reimprimir_FR( codRef );
//    }
    private void reimprimir_FR()
    {
        DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
        int selectedRow = jTable1.getSelectedRow();
        String codRef = modelo.getValueAt( selectedRow, 0 ).toString();

        procedimento_reimprimir_FR( codRef );
    }

    private void procedimento_reimprimir_FR( String ref_doc )
    {

        HashMap hashMap = new HashMap();
        TbVenda venda = vendasController.findByCodFactReemprensao( ref_doc );

        if ( venda != null )
        {
            List<TbProduto> lista_produto_isentos = new ArrayList<>();
            lista_produto_isentos = MetodosUtil.getProdutosIsentos( venda.getTbItemVendaList() );
            String motivos_isentos = MetodosUtil.getMotivoIsensaoProdutos( lista_produto_isentos );
            ListaVenda2 listaVenda2 = new ListaVenda2( venda.getCodigo(), abreviacao, false, false, DVML.SEGUNDA_VIA_CONFORMIDADE_COM_ORIGINAL, motivos_isentos );
        }
        else
        {
            JOptionPane.showMessageDialog( null, "Atenção\nO Documento não existe na base de dados. \nObs: Verifique a referência. " );
        }

    }

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
            java.util.logging.Logger.getLogger( ListarRelatorioVendaByIntervalo.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        catch ( InstantiationException ex )
        {
            java.util.logging.Logger.getLogger( ListarRelatorioVendaByIntervalo.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        catch ( IllegalAccessException ex )
        {
            java.util.logging.Logger.getLogger( ListarRelatorioVendaByIntervalo.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        catch ( javax.swing.UnsupportedLookAndFeelException ex )
        {
            java.util.logging.Logger.getLogger( ListarRelatorioVendaByIntervalo.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
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
                    new ListarRelatorioVendaByIntervalo( new BDConexao() ).setVisible( true );
                }
                catch ( Exception ex )
                {
                    Logger.getLogger( ListarRelatorioVendaByIntervalo.class.getName() ).log( Level.SEVERE, null, ex );
                }
            }
        } );
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelar1;
    private javax.swing.JButton btnSalvar;
    private javax.swing.ButtonGroup buttonGroup1;
    private static javax.swing.JComboBox<String> cmbUsuario;
    private com.toedter.calendar.JDateChooser dcDataFim;
    private com.toedter.calendar.JDateChooser dcDataInicio;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel lbData;
    private javax.swing.JLabel lbData1;
    private javax.swing.JLabel lbData2;
    private javax.swing.JLabel lb_total;
    // End of variables declaration//GEN-END:variables

    private void adicionar_tabela()
    {
        DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
        modelo.setRowCount( 0 );

        try
        {

            lista = vendasController.getAllFRVendaByBetweenDataAndArmazemAndDocumento( dcDataInicio.getDate(), dcDataFim.getDate(), getCodigoUsuario() );

            if ( lista != null )
            {
                for ( TbVenda object : lista )
                {

                    modelo.addRow( new Object[]
                    {
                        object.getCodFact(),
                        object.getCodigoCliente().getNome(),
                        getData( object.getDataVenda() ),
                        getHora( object.getHora() ),
                        object.getTotalVenda(),

                    } );

                }
                lb_total.setText( formatarComoMoeda( getTotal( jTable1 ) ) );
            }

        }
        catch ( Exception e )
        {
            e.printStackTrace();
//            JOptionPane.showMessageDialog( null, "Não há registro para este intervalo de datas", DVML.DVML_COMERCIAL, JOptionPane.WARNING_MESSAGE );
        }

    }

//    public int getCodigoArmazem() {
//        return armazemDao.getArmazemByDescricao(cmbArmazem.getSelectedItem().toString()).getCodigo();
//    }
    private String getData( Date date )
    {
        return getNumeroDoisDigitos( date.getDate() )
                + "/" + ( getNumeroDoisDigitos( date.getMonth() + 1 ) )
                + "/" + ( date.getYear() + 1900 );

    }

    private String getHora( Date date )
    {
        try
        {
            return getNumeroDoisDigitos( date.getHours() ) + ":"
                    + getNumeroDoisDigitos( date.getMinutes() ) + ":"
                    + getNumeroDoisDigitos( date.getSeconds() );
        }
        catch ( Exception e )
        {
        }
        return "";

    }

    private String getNumeroDoisDigitos( int numero )
    {
        if ( numero < 10 )
        {
            return "0" + numero;
        }

        return String.valueOf( numero );

    }

    private BigDecimal getTotal( JTable tabela )
    {
        BigDecimal total = new BigDecimal( 0.0 );
        DefaultTableModel modelo = (DefaultTableModel) tabela.getModel();

        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {
            total = total.add( new BigDecimal( tabela.getValueAt( i, 5 ).toString() ) ).setScale( 2, RoundingMode.CEILING );

        }
        System.out.println( "Total: " + total.doubleValue() );

        return total;

    }
    
//    
//
//    public void mostrarUsuarios() throws SQLException
//    {
//
//        Connection connection = (Connection) conexao.conectar();
//        HashMap hashMap = new HashMap();
//
//        hashMap.put( "NOME_USUARIO", getUsuario() );
//        hashMap.put( "DATA", dcDataInicio.getDate() );
//        hashMap.put( "DATA_VENDA", dcDataInicio.getDate() );
//        hashMap.put( "TOTAL_USUARIO", getTotal(getUsuario ) );
////        hashMap.put( "TOTAL_USUARIO", getTotal(getUsuario ) );
//
//        String relatorio = getCaminho();
//
//        File file = new File( relatorio ).getAbsoluteFile();
//
//        String obterCaminho = file.getAbsolutePath();
//
//        try
//        {
//
//            JasperFillManager.fillReport( obterCaminho, hashMap, connection );
//
//            JasperPrint jasperPrint = JasperFillManager.fillReport( obterCaminho, hashMap, connection );
//
//            if ( jasperPrint.getPages().size() >= 1 )
//            {
//
//                JasperViewer jasperViewer = new JasperViewer( jasperPrint, false );
//                jasperViewer.setVisible( true );
//
//            }
//            else
//            {
//                JOptionPane.showMessageDialog( null, "Atenção: Não existem vendas deste usuário(a) para esta data!..." );
//            }
//        }
//        catch ( JRException jex )
//        {
//            jex.printStackTrace();
//            JOptionPane.showMessageDialog( null, "FALHA AO TENTAR MOSTRAR OS USUARIOS!..." );
//        }
//        catch ( Exception ex )
//        {
//            ex.printStackTrace();
//            JOptionPane.showMessageDialog( null, "ERRO AO TENTAR OS USUARIOS!..." );
//        }
//    }
//    
//        public double getTotalValor( int codUtilizdor )
//    {
//        // SELECT distinct  SUM(total_venda) as soma FROM tb_venda WHERE codigo_usuario = 15 AND dataVenda LIKE '2013-04-05%'
//        //String sql = "SELECT  SUM(total_venda) as soma FROM tb_venda WHERE codigo_usuario = "  +codUtilizdor+" AND dataVenda LIKE '" +getDataSelecionadaString() +"%'";
//        String sql = "SELECT  SUM(total_venda) as soma FROM tb_venda WHERE codigo_usuario = " + codUtilizdor + "  AND performance = 'false'  AND status_eliminado = 'false'  AND dataVenda = '" + getDataSelecionadaString() + "'";
//        System.out.println( sql );
//        ResultSet rs = conexao.executeQuery( sql );
//
//        try
//        {
//            if ( rs.next() )
//            {
//                return rs.getDouble( "soma" );
//            }
//        }
//        catch ( SQLException ex )
//        {
//            return 0;
//        }
//
//        return 0;
//
//    }
        
        
        public String getUsuario()
    {
        return String.valueOf( cmbUsuario.getSelectedItem() );

    }
    

    public String getCaminho()
    {

        return "relatorios/report8.jasper";

    }

    public static int getCodigoUsuario()
    {
        return usuariosController.getClienteByNome( cmbUsuario.getSelectedItem().toString() ).getCodigo();
    }

}
