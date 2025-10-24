/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lista;


import java.sql.Connection;
import com.toedter.calendar.JDateChooser;
import dao.AccessoArmazemDao;
import dao.ArmazemDao;
import dao.DocumentoDao;
import dao.ItemVendaDao;
import dao.ClienteDao;
import dao.VendaDao;
import entity.TbVenda;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import util.BDConexao;
import util.DVML;
import util.JPAEntityMannagerFactoryUtil;

/**
 *
 * @author Domingos Dala Vunge
 */
public class ListarVendasByClientes extends javax.swing.JFrame
{

    /**
     * Creates new form ListaClienteVisao
     */
    private BDConexao conexao;
    private EntityManagerFactory emf = JPAEntityMannagerFactoryUtil.em;
    private ArmazemDao armazemDao = new ArmazemDao( emf );
    private DocumentoDao documentoDao = new DocumentoDao( emf );
    private ItemVendaDao itemVendaDao = new ItemVendaDao( emf );
    private ClienteDao clienteDao = new ClienteDao( emf );
    private VendaDao vendaDao = new VendaDao( emf );
    private AccessoArmazemDao accessoArmazemDao = new AccessoArmazemDao( emf );
    private List<TbVenda> lista = null;
    private int idUser;

    public ListarVendasByClientes( int idUser, BDConexao conexao )
    {

        initComponents();
        setResizable( false );
        setLocationRelativeTo( null );
        confLabel();

        dc_inicio.setDate( new Date() );
        dc_fim.setDate( new Date() );
        this.idUser = idUser;

        this.conexao = conexao;
//        cmbCliente.setModel( new DefaultComboBoxModel( ( Vector ) clienteDao.getAllCliente() ) );

        cmbCliente.setModel( new DefaultComboBoxModel( ( Vector ) clienteDao.buscaTodosClientesExceptoConsumidorFinal1() ) );
//        cmbArmazem.setModel( new DefaultComboBoxModel( armazemDao.buscaTodos() ) );
        cmbArmazem.setModel( new DefaultComboBoxModel( accessoArmazemDao.getAllArmazemByIdUSuario( idUser ) ) );
//        tabela.setVisible( false );
//        bt_pesquisa.setVisible( false );
    }

    //SELECT distinct  SUM(total_venda) as soma FROM tb_venda WHERE codigo_cliente = 4;
    public void confLabel()
    {

        lbStatus.setHorizontalAlignment( JLabel.RIGHT );
        lbArmazem.setHorizontalAlignment( JLabel.RIGHT );
        lbData.setHorizontalAlignment( JLabel.RIGHT );

    }

    public String getCliente()
    {
        return String.valueOf( cmbCliente.getSelectedItem() );

    }

    public double getTotalValor( int codCliente )
    {

        String sql = "SELECT  SUM(total_venda) as soma FROM tb_venda WHERE codigo_cliente = " + codCliente + "  AND performance = 'false'  AND status_eliminado = 'false'  AND credito = 'false'  AND dataVenda = '" + getDataSelecionadaString() + "'";
        System.out.println( sql );
        ResultSet rs = BDConexao.getInstancia().executeQuery( sql );

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

    public double getTotalValor( int codCliente, int cod_armazem, int cod_documento )
    {

        String sql = "SELECT  SUM(total_venda) as soma FROM tb_venda WHERE DATE(dataVenda) BETWEEN '" + getDataSelecionadaString( dc_inicio ) + "' AND '" + getDataSelecionadaString( dc_fim ) + "' AND  codigo_cliente = " + codCliente + "  AND performance = 'false'  AND fk_documento = " + cod_documento + " AND status_eliminado = 'false'  AND credito = 'false' AND idArmazemFK = " + cod_armazem;
        System.out.println( sql );
        ResultSet rs = BDConexao.getInstancia().executeQuery( sql );

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

        ResultSet rs = BDConexao.getInstancia().executeQuery( sql );

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
        gc.setTime( dc_inicio.getDate() );

        String dataSelecionada = gc.get( GregorianCalendar.YEAR ) + "-"
                + ( gc.get( GregorianCalendar.MONTH ) + 1 ) + "-"
                + gc.get( GregorianCalendar.DATE );
        return dc_inicio.getDate();
    }

    public String getDataSelecionadaString()
    {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime( dc_inicio.getDate() );

        String dataSelecionada = gc.get( GregorianCalendar.YEAR ) + "-"
                + setTransforme( gc.get( GregorianCalendar.MONTH ) + 1 ) + "-"
                + setTransforme( gc.get( GregorianCalendar.DATE ) );
        return dataSelecionada;
    }

    public String getDataSelecionadaString( JDateChooser date )
    {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime( date.getDate() );

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

//    
    public void mostrarClientes() throws SQLException
    {

        java.sql.Connection connection = conexao.getConnectionAtiva();
        HashMap hashMap = new HashMap();

        hashMap.put( "NOME_CLIENTE", getCliente() );
        hashMap.put( "DATA_1", dc_inicio.getDate() );
        hashMap.put( "DATA_2", dc_fim.getDate() );
        hashMap.put( "ID_ARMAZEM", this.getCodigoArmazem() );
        hashMap.put( "ID_CLIENTE", this.getCodigoCliente() );
        hashMap.put( "ID_DOCUMENTO", this.getTipoDocumento() );
        hashMap.put( "ID_DOCUMENTO", this.getTipoDocumento() );
        hashMap.put( "DESIGNACAO_DOCUMENTO", this.documentoDao.findDocumento( getTipoDocumento() ).getDesignacao() );
        hashMap.put( "TOTAL_CLIENTE", getTotalValor( getCodigoCliente(), getCodigoArmazem(), getTipoDocumento() ) );
        hashMap.put( "LUCRO_CLIENTE", itemVendaDao.lucro( dc_inicio.getDate(), dc_fim.getDate(), getCodigoArmazem(), getTipoDocumento(), getCodigoCliente() ) );
        String relatorio = "";
        relatorio = "relatorios/lista_venda_by_cliente.jasper";
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
                JOptionPane.showMessageDialog( null, "Nao existe venda para essa data!..." );
            }
        }
        catch ( JRException jex )
        {
            jex.printStackTrace();
            //System.out.println("aqui");
            JOptionPane.showMessageDialog( null, "Falha ao tentar mostrar o relatório de vendas por clientes!..." );
        }
        catch ( Exception ex )
        {
            ex.printStackTrace();
            JOptionPane.showMessageDialog( null, "Erro ao tentar mostrar o relatório!..." );
        }
    }

//    
//         public static void XLSX(String jasperFilename, Map<String, Object> parameters, java.sql.Connection dataSource) throws JRException, IOException 
//        {
//        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperFilename, parameters, dataSource);
//        int indexOfPonto = jasperFilename.indexOf('.');
//        String file = jasperFilename.substring(0, indexOfPonto) + ".xlsx" ;
//        
//        FileOutputStream output = new FileOutputStream(file);
//  
//        JRXlsxExporter docxExporter = new JRXlsxExporter();
//        
//        docxExporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
//        docxExporter.setParameter(JRXlsAbstractExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
//        docxExporter.setParameter(JRExporterParameter.OUTPUT_STREAM, output);
//        docxExporter.setParameter(JRXlsAbstractExporterParameter.IS_DETECT_CELL_TYPE, Boolean.FALSE);
//        docxExporter.exportReport();        
//        
//        Runtime rt = Runtime.getRuntime();  
//        System.out.println(file);        
//        
//        rt.exec("RunDLL32.EXE shell32.dll,ShellExec_RunDLL " + file);  
//    }
//    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        lbData2 = new javax.swing.JLabel();
        rb_FR = new javax.swing.JRadioButton();
        rb_FT = new javax.swing.JRadioButton();
        rb_PP = new javax.swing.JRadioButton();
        rb_RC = new javax.swing.JRadioButton();
        jPanel2 = new javax.swing.JPanel();
        lbStatus = new javax.swing.JLabel();
        cmbCliente = new javax.swing.JComboBox();
        cmbArmazem = new javax.swing.JComboBox<>();
        lbData = new javax.swing.JLabel();
        dc_inicio = new com.toedter.calendar.JDateChooser();
        lbData1 = new javax.swing.JLabel();
        dc_fim = new com.toedter.calendar.JDateChooser();
        lbArmazem = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        btnCancelar = new javax.swing.JButton();
        btnCancelar1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("...:::: DVML - LISTAGEM DE VENDAS POR USUÁRIO:::...");

        jPanel1.setBackground(new java.awt.Color(0, 51, 102));

        lbData2.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 24)); // NOI18N
        lbData2.setForeground(new java.awt.Color(255, 255, 255));
        lbData2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbData2.setText("RELATÓRIO DE VENDAS POR CLIENTES");

        buttonGroup1.add(rb_FR);
        rb_FR.setForeground(new java.awt.Color(255, 255, 255));
        rb_FR.setSelected(true);
        rb_FR.setText("FR");
        rb_FR.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                rb_FRActionPerformed(evt);
            }
        });

        buttonGroup1.add(rb_FT);
        rb_FT.setForeground(new java.awt.Color(255, 255, 255));
        rb_FT.setText("FT");
        rb_FT.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                rb_FTActionPerformed(evt);
            }
        });

        buttonGroup1.add(rb_PP);
        rb_PP.setForeground(new java.awt.Color(255, 255, 255));
        rb_PP.setText("PP");
        rb_PP.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                rb_PPActionPerformed(evt);
            }
        });

        buttonGroup1.add(rb_RC);
        rb_RC.setForeground(new java.awt.Color(255, 255, 255));
        rb_RC.setText("RC");
        rb_RC.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                rb_RCActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(lbData2, javax.swing.GroupLayout.DEFAULT_SIZE, 964, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(rb_FR)
                .addGap(3, 3, 3)
                .addComponent(rb_FT)
                .addGap(3, 3, 3)
                .addComponent(rb_PP)
                .addGap(14, 14, 14)
                .addComponent(rb_RC)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(lbData2, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(rb_FR, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rb_FT, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rb_PP, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rb_RC, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        lbStatus.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 16)); // NOI18N
        lbStatus.setText("Clientes:");

        cmbCliente.setBackground(new java.awt.Color(51, 153, 0));
        cmbCliente.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 14)); // NOI18N
        cmbCliente.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Todos", "Activo", "Desactivo" }));

        cmbArmazem.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        lbData.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 16)); // NOI18N
        lbData.setText("De:");

        lbData1.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 16)); // NOI18N
        lbData1.setText("à");

        lbArmazem.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 16)); // NOI18N
        lbArmazem.setText("Armazém:");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbArmazem, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cmbCliente, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cmbArmazem, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(lbData)
                        .addGap(127, 127, 127)
                        .addComponent(lbData1))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addComponent(dc_inicio, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dc_fim, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cmbCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(12, 12, 12)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cmbArmazem)
                            .addComponent(lbArmazem, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lbData, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbData1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dc_inicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(dc_fim, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
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
                .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCancelar1, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnCancelar1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed

        try
        {
            // TODO add your handling code here:

            if ( cmbCliente.getSelectedIndex() == 0 )
            {

                procedimento_imprimir_todos();

            }
            else
            {

                mostrarClientes();

            }
        }
        catch ( SQLException ex )
        {
            Logger.getLogger( ListarVendasByClientes.class.getName() ).log( Level.SEVERE, null, ex );
        }


    }//GEN-LAST:event_btnCancelarActionPerformed

    private void procedimento_imprimir_todos()
    {

//        if ( ck_relatorio_normal.isSelected() || ck_mapa_iva.isSelected() )
//        {
//            ResumoVenda resumoVenda = new ResumoVenda( dcDataInicio.getDate(), dcDataFim.getDate(), getCodigoArmazem(), this.lista, getTipoDocumento(), ck_mapa_iva.isSelected() );
//        }
//        if ( ck_ordemFR.isSelected() )
//        {
        ResumoVenda resumoVenda = new ResumoVenda( dc_inicio.getDate(), dc_fim.getDate(), getCodigoArmazem(), this.lista, getTipoDocumento() );
//        }
//        else if ( ck_mapa_iva.isSelected() )
//        {
//            ResumoVenda resumoVenda = new ResumoVenda( dcDataInicio.getDate(), dcDataFim.getDate(), getCodigoArmazem(), this.lista, getTipoDocumento(), true );
//        }
//        else if ( ck_estrato_cliente.isSelected() )
//        {
//            new ExtratoContaReport( getCodigoCliente(), dcDataInicio.getDate(), dcDataFim.getDate() );
//        }

    }


    private void btnCancelar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelar1ActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_btnCancelar1ActionPerformed

    private void rb_FRActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_rb_FRActionPerformed
    {//GEN-HEADEREND:event_rb_FRActionPerformed
        // TODO add your handling code here:
        adicionar_tabela();
    }//GEN-LAST:event_rb_FRActionPerformed

    private void rb_FTActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_rb_FTActionPerformed
    {//GEN-HEADEREND:event_rb_FTActionPerformed
        // TODO add your handling code here:
        adicionar_tabela();
    }//GEN-LAST:event_rb_FTActionPerformed

    private void rb_PPActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_rb_PPActionPerformed
    {//GEN-HEADEREND:event_rb_PPActionPerformed
        // TODO add your handling code here:
        adicionar_tabela();
    }//GEN-LAST:event_rb_PPActionPerformed

    private void rb_RCActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_rb_RCActionPerformed
    {//GEN-HEADEREND:event_rb_RCActionPerformed
        // TODO add your handling code here:
        adicionar_tabela();
    }//GEN-LAST:event_rb_RCActionPerformed

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
            java.util.logging.Logger.getLogger( ListarVendasByClientes.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        catch ( InstantiationException ex )
        {
            java.util.logging.Logger.getLogger( ListarVendasByClientes.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        catch ( IllegalAccessException ex )
        {
            java.util.logging.Logger.getLogger( ListarVendasByClientes.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        catch ( javax.swing.UnsupportedLookAndFeelException ex )
        {
            java.util.logging.Logger.getLogger( ListarVendasByClientes.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater( new Runnable()
        {
            public void run()
            {
                new ListarVendasByClientes( 15, BDConexao.getInstancia() ).setVisible( true );
            }
        } );
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnCancelar1;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox<String> cmbArmazem;
    private javax.swing.JComboBox cmbCliente;
    private com.toedter.calendar.JDateChooser dc_fim;
    private com.toedter.calendar.JDateChooser dc_inicio;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JLabel lbArmazem;
    private javax.swing.JLabel lbData;
    private javax.swing.JLabel lbData1;
    private javax.swing.JLabel lbData2;
    private javax.swing.JLabel lbStatus;
    private javax.swing.JRadioButton rb_FR;
    private javax.swing.JRadioButton rb_FT;
    private javax.swing.JRadioButton rb_PP;
    private javax.swing.JRadioButton rb_RC;
    // End of variables declaration//GEN-END:variables

    private void adicionar_tabela()
    {
//        DefaultTableModel modelo = ( DefaultTableModel ) tabela.getModel();
//        modelo.setRowCount( 0 );
//        
//        try
//        {
//            
//            List<TbVenda> lista = vendaDao.getAllVendaByBetweenDataAndArmazem( dc_inicio.getDate(), dc_fim.getDate(), getCodigoArmazem(), getCodigoCliente(), getTipoDocumento() );
//            
//            for ( TbVenda object : lista )
//            {
//                
//                modelo.addRow( new Object[]
//                {
//                    
//                    object.getCodigo(),
//                    object.getNomeCliente(),
//                    getData( object.getDataVenda() ),
//                    getHora( object.getHora() ),
//                    object.getCodigoCliente().getNome(),
//                    object.getTotalVenda()
//                
//                } );
//                
//            }
//            
//            lb_total.setText( String.valueOf( getTotal() ) );
//            
//        }
//        catch ( Exception e )
//        {
//            e.printStackTrace();
//            lb_total.setText( "" );
//            JOptionPane.showMessageDialog( null, "Não há registro para esse armazém", DVML.DVML_COMERCIAL, JOptionPane.WARNING_MESSAGE );
//        }
//        
    }

    private String getData( Date date )
    {
        try
        {
            return getNumeroDoisDigitos( date.getDate() )
                    + "/" + ( getNumeroDoisDigitos( date.getMonth() + 1 ) )
                    + "/" + ( date.getYear() + 1900 );
        }
        catch ( Exception e )
        {
            return "";
        }

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
            return "";
        }

    }

    private String getNumeroDoisDigitos( int numero )
    {
        if ( numero < 10 )
        {
            return "0" + numero;
        }

        return String.valueOf( numero );

    }

//    private double getTotal()
//    {
//        double total = 0;
//        DefaultTableModel modelo = ( DefaultTableModel ) tabela.getModel();
//        
//        for ( int i = 0; i < modelo.getRowCount(); i++ )
//        {
//            total = total + Double.parseDouble( tabela.getValueAt( i, 5 ).toString() );
//            
//        }
//        
//        return total;
//        
//    }
    public int getCodigoArmazem()
    {
        return armazemDao.getArmazemByDescricao( cmbArmazem.getSelectedItem().toString() ).getCodigo();
    }

    private int getTipoDocumento()
    {
        if ( rb_FR.isSelected() )
        {
            return DVML.DOC_FACTURA_RECIBO_FR;
        }
        else if ( rb_FT.isSelected() )
        {
            return DVML.DOC_FACTURA_FT;
        }
        else if ( rb_PP.isSelected() )
        {
            return DVML.DOC_FACTURA_PROFORMA_PP;
        }
        else
        {
            return DVML.DOC_RECIBO_RC;
        }

    }
}
