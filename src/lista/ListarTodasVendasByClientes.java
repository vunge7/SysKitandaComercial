/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lista;


import java.sql.Connection;
import dao.ArmazemDao;
import dao.DocumentoDao;
import dao.ItemVendaDao;
import dao.ClienteDao;
import dao.DadosInstituicaoDao;
import dao.VendaDao;
import entity.TbVenda;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;
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
public class ListarTodasVendasByClientes extends javax.swing.JFrame
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
    private DadosInstituicaoDao dadosInstituicaoDao = new DadosInstituicaoDao( emf );

    public ListarTodasVendasByClientes( BDConexao conexao )
    {

        initComponents();
        setResizable( false );
        setLocationRelativeTo( null );
        confLabel();
        this.conexao = conexao;
        cmbCliente.setModel( new DefaultComboBoxModel( (Vector) clienteDao.buscaTodosClientesExceptoConsumidorFinal() ) );

    }

    //SELECT distinct  SUM(total_venda) as soma FROM tb_venda WHERE codigo_cliente = 4;
    public void confLabel()
    {

        lbStatus.setHorizontalAlignment( JLabel.RIGHT );

    }

    public String getCliente()
    {
        return String.valueOf( cmbCliente.getSelectedItem() );

    }

//    
    public void mostrarContasPendentesResumidaClientes() throws SQLException
    {

        java.sql.Connection connection = conexao.getConnectionAtiva();
        HashMap hashMap = new HashMap();

        hashMap.put( "NOME_CLIENTE", getCliente() );
        hashMap.put( "ID_CLIENTE", this.getCodigoCliente() );
        hashMap.put( "MORADA", this.getMoradaClienteString() );
        hashMap.put( "NIF_CLIENTE", this.getNIFClienteString() );
        hashMap.put( "TELEFONE_CLIENTE", this.getTelefoneClienteString() );
        hashMap.put( "EMAIL_CLIENTE", this.getEmailClienteString() );

        hashMap.put( "DADOS_INSTITUICAO", dadosInstituicaoDao.findTbDadosInstituicao( 1 ).getNome() );
        hashMap.put( "ENDERECO", dadosInstituicaoDao.findTbDadosInstituicao( 1 ).getEnderecos() );
        hashMap.put( "NIF", dadosInstituicaoDao.findTbDadosInstituicao( 1 ).getNif() );
        hashMap.put( "TELEFONE", dadosInstituicaoDao.findTbDadosInstituicao( 1 ).getTelefone() );
        hashMap.put( "EMAIL", dadosInstituicaoDao.findTbDadosInstituicao( 1 ).getEmail() );
        hashMap.put( "CONTA_BANCARIA_1", dadosInstituicaoDao.findTbDadosInstituicao( 1 ).getContaBancaria1() );
        hashMap.put( "CONTA_BANCARIA_2", dadosInstituicaoDao.findTbDadosInstituicao( 1 ).getContaBancaria2() );
        hashMap.put( "CONTA_BANCARIA_3", dadosInstituicaoDao.findTbDadosInstituicao( 1 ).getContaBancaria3() );
        hashMap.put( "CONTA_BANCARIA_4", dadosInstituicaoDao.findTbDadosInstituicao( 1 ).getContaBancaria4() );
        hashMap.put( "CONTA_BANCARIA_5", dadosInstituicaoDao.findTbDadosInstituicao( 1 ).getContaBancaria5() );
        hashMap.put( "CONTA_BANCARIA_6", dadosInstituicaoDao.findTbDadosInstituicao( 1 ).getContaBancaria6() );
        String relatorio = "";
//        relatorio = "relatorios/lista_venda_by_cliente.jasper";
        relatorio = "relatorios/lista_de_vendas_clientes_pendentes.jasper";
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
                JOptionPane.showMessageDialog( null, "Nao existem vendas pendentes para este cliente!..." );
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

    public void mostrarContasPendentesDetalhadaClientes() throws SQLException
    {

        java.sql.Connection connection = conexao.getConnectionAtiva();
        HashMap hashMap = new HashMap();

        hashMap.put( "NOME_CLIENTE", getCliente() );
        hashMap.put( "ID_CLIENTE", this.getCodigoCliente() );
        hashMap.put( "MORADA", this.getMoradaClienteString() );
        hashMap.put( "NIF_CLIENTE", this.getNIFClienteString() );
        hashMap.put( "TELEFONE_CLIENTE", this.getTelefoneClienteString() );
        hashMap.put( "EMAIL_CLIENTE", this.getEmailClienteString() );

        hashMap.put( "DADOS_INSTITUICAO", dadosInstituicaoDao.findTbDadosInstituicao( 1 ).getNome() );
        hashMap.put( "ENDERECO", dadosInstituicaoDao.findTbDadosInstituicao( 1 ).getEnderecos() );
        hashMap.put( "NIF", dadosInstituicaoDao.findTbDadosInstituicao( 1 ).getNif() );
        hashMap.put( "TELEFONE", dadosInstituicaoDao.findTbDadosInstituicao( 1 ).getTelefone() );
        hashMap.put( "EMAIL", dadosInstituicaoDao.findTbDadosInstituicao( 1 ).getEmail() );
        hashMap.put( "CONTA_BANCARIA_1", dadosInstituicaoDao.findTbDadosInstituicao( 1 ).getContaBancaria1() );
        hashMap.put( "CONTA_BANCARIA_2", dadosInstituicaoDao.findTbDadosInstituicao( 1 ).getContaBancaria2() );
        hashMap.put( "CONTA_BANCARIA_3", dadosInstituicaoDao.findTbDadosInstituicao( 1 ).getContaBancaria3() );
        hashMap.put( "CONTA_BANCARIA_4", dadosInstituicaoDao.findTbDadosInstituicao( 1 ).getContaBancaria4() );
        hashMap.put( "CONTA_BANCARIA_5", dadosInstituicaoDao.findTbDadosInstituicao( 1 ).getContaBancaria5() );
        hashMap.put( "CONTA_BANCARIA_6", dadosInstituicaoDao.findTbDadosInstituicao( 1 ).getContaBancaria6() );
        String relatorio = "";
//        relatorio = "relatorios/lista_venda_by_cliente.jasper";
        relatorio = "relatorios/lista_de_vendas_clientes_pendentes_detalhado.jasper";
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
                JOptionPane.showMessageDialog( null, "Nao existem vendas pendentes para este cliente!..." );
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

    public void mostrarClientesPagos() throws SQLException
    {

        java.sql.Connection connection = conexao.getConnectionAtiva();
        HashMap hashMap = new HashMap();

        hashMap.put( "NOME_CLIENTE", getCliente() );
        hashMap.put( "ID_CLIENTE", this.getCodigoCliente() );
        hashMap.put( "MORADA", this.getMoradaClienteString() );
        hashMap.put( "NIF_CLIENTE", this.getNIFClienteString() );
        hashMap.put( "TELEFONE_CLIENTE", this.getTelefoneClienteString() );
        hashMap.put( "EMAIL_CLIENTE", this.getEmailClienteString() );

        hashMap.put( "DADOS_INSTITUICAO", dadosInstituicaoDao.findTbDadosInstituicao( 1 ).getNome() );
        hashMap.put( "ENDERECO", dadosInstituicaoDao.findTbDadosInstituicao( 1 ).getEnderecos() );
        hashMap.put( "NIF", dadosInstituicaoDao.findTbDadosInstituicao( 1 ).getNif() );
        hashMap.put( "TELEFONE", dadosInstituicaoDao.findTbDadosInstituicao( 1 ).getTelefone() );
        hashMap.put( "EMAIL", dadosInstituicaoDao.findTbDadosInstituicao( 1 ).getEmail() );
        hashMap.put( "CONTA_BANCARIA_1", dadosInstituicaoDao.findTbDadosInstituicao( 1 ).getContaBancaria1() );
        hashMap.put( "CONTA_BANCARIA_2", dadosInstituicaoDao.findTbDadosInstituicao( 1 ).getContaBancaria2() );
        hashMap.put( "CONTA_BANCARIA_3", dadosInstituicaoDao.findTbDadosInstituicao( 1 ).getContaBancaria3() );
        hashMap.put( "CONTA_BANCARIA_4", dadosInstituicaoDao.findTbDadosInstituicao( 1 ).getContaBancaria4() );
        hashMap.put( "CONTA_BANCARIA_5", dadosInstituicaoDao.findTbDadosInstituicao( 1 ).getContaBancaria5() );
        hashMap.put( "CONTA_BANCARIA_6", dadosInstituicaoDao.findTbDadosInstituicao( 1 ).getContaBancaria6() );
//        hashMap.put("ENDERECO",  dadosInstituicaoDao.findTbDadosInstituicao(1).getEnderecos()  );
        String relatorio = "";
//        relatorio = "relatorios/lista_venda_by_cliente.jasper";
        relatorio = "relatorios/lista_de_vendas_clientes_pagos.jasper";
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
                JOptionPane.showMessageDialog( null, "Nao existem vendas pagas para este cliente!..." );
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
        buttonGroup2 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        lbData2 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        lbStatus = new javax.swing.JLabel();
        cmbCliente = new javax.swing.JComboBox();
        ck_contas_pagas = new javax.swing.JCheckBox();
        ck_contas_pendentes = new javax.swing.JCheckBox();
        ck_contas_pendentes_detalhada = new javax.swing.JCheckBox();
        jPanel3 = new javax.swing.JPanel();
        btnCancelar = new javax.swing.JButton();
        btnCancelar1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("...:::: DVML - LISTAGEM DE VENDAS POR USUÁRIO:::...");

        jPanel1.setBackground(new java.awt.Color(0, 51, 102));

        lbData2.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 24)); // NOI18N
        lbData2.setForeground(new java.awt.Color(255, 255, 255));
        lbData2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbData2.setText("EXTRATO DE CONTAS DE CLIENTES");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(lbData2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(lbData2, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(7, Short.MAX_VALUE))
        );

        lbStatus.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 16)); // NOI18N
        lbStatus.setText("Clientes:");

        cmbCliente.setBackground(new java.awt.Color(51, 153, 0));
        cmbCliente.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 14)); // NOI18N
        cmbCliente.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Todos", "Activo", "Desactivo" }));

        buttonGroup1.add(ck_contas_pagas);
        ck_contas_pagas.setFont(new java.awt.Font("Lucida Grande", 1, 12)); // NOI18N
        ck_contas_pagas.setText("Contas Pagas");
        ck_contas_pagas.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ck_contas_pagasActionPerformed(evt);
            }
        });

        buttonGroup1.add(ck_contas_pendentes);
        ck_contas_pendentes.setFont(new java.awt.Font("Lucida Grande", 1, 12)); // NOI18N
        ck_contas_pendentes.setText("Contas Pendentes");
        ck_contas_pendentes.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ck_contas_pendentesActionPerformed(evt);
            }
        });

        buttonGroup1.add(ck_contas_pendentes_detalhada);
        ck_contas_pendentes_detalhada.setFont(new java.awt.Font("Lucida Grande", 1, 12)); // NOI18N
        ck_contas_pendentes_detalhada.setText("Contas Pendentes Detalhada");
        ck_contas_pendentes_detalhada.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ck_contas_pendentes_detalhadaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(lbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(cmbCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(ck_contas_pendentes, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ck_contas_pagas, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(ck_contas_pendentes_detalhada, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(24, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ck_contas_pendentes)
                    .addComponent(ck_contas_pagas))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(ck_contas_pendentes_detalhada)
                .addGap(22, 22, 22))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        btnCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/proucura.png"))); // NOI18N
        btnCancelar.setText("Visualizar");
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
                .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCancelar1, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnCancelar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnCancelar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed

        procedimento_chamar_extrato();
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnCancelar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelar1ActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_btnCancelar1ActionPerformed

    private void ck_contas_pagasActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_ck_contas_pagasActionPerformed
    {//GEN-HEADEREND:event_ck_contas_pagasActionPerformed


    }//GEN-LAST:event_ck_contas_pagasActionPerformed

    private void ck_contas_pendentesActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_ck_contas_pendentesActionPerformed
    {//GEN-HEADEREND:event_ck_contas_pendentesActionPerformed


    }//GEN-LAST:event_ck_contas_pendentesActionPerformed

    private void ck_contas_pendentes_detalhadaActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_ck_contas_pendentes_detalhadaActionPerformed
    {//GEN-HEADEREND:event_ck_contas_pendentes_detalhadaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ck_contas_pendentes_detalhadaActionPerformed

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
            java.util.logging.Logger.getLogger( ListarTodasVendasByClientes.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        catch ( InstantiationException ex )
        {
            java.util.logging.Logger.getLogger( ListarTodasVendasByClientes.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        catch ( IllegalAccessException ex )
        {
            java.util.logging.Logger.getLogger( ListarTodasVendasByClientes.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        catch ( javax.swing.UnsupportedLookAndFeelException ex )
        {
            java.util.logging.Logger.getLogger( ListarTodasVendasByClientes.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater( new Runnable()
        {
            public void run()
            {
                new ListarTodasVendasByClientes( BDConexao.getInstancia() ).setVisible( true );
            }
        } );
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnCancelar1;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    public static javax.swing.JCheckBox ck_contas_pagas;
    private javax.swing.JCheckBox ck_contas_pendentes;
    private javax.swing.JCheckBox ck_contas_pendentes_detalhada;
    private javax.swing.JComboBox cmbCliente;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JLabel lbData2;
    private javax.swing.JLabel lbStatus;
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

    private String setNif( TbVenda venda )
    {

        return venda.getClienteNif();
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

    //devolve o campo de uma determinada tabela em funcao do codigo
    public String getMoradaClienteString()
    {

        String sql = "SELECT morada FROM  tb_cliente WHERE( nome  = '" + getCliente() + "')";

        ResultSet rs = conexao.executeQuery( sql );

        try
        {
            if ( rs.next() )
            {
                return rs.getString( "morada" );
            }
        }
        catch ( SQLException ex )
        {
            return "";
        }
        return "";
    }

    //devolve o campo de uma determinada tabela em funcao do codigo
    public String getNIFClienteString()
    {

        String sql = "SELECT nif FROM  tb_cliente WHERE( nome  = '" + getCliente() + "')";

        ResultSet rs = conexao.executeQuery( sql );

        try
        {
            if ( rs.next() )
            {
                return rs.getString( "nif" );
            }
        }
        catch ( SQLException ex )
        {
            return "";
        }
        return "";
    }

    //devolve o campo de uma determinada tabela em funcao do codigo
    public String getTelefoneClienteString()
    {

        String sql = "SELECT telefone FROM  tb_cliente WHERE( nome  = '" + getCliente() + "')";

        ResultSet rs = conexao.executeQuery( sql );

        try
        {
            if ( rs.next() )
            {
                return rs.getString( "telefone" );
            }
        }
        catch ( SQLException ex )
        {
            return "";
        }
        return "";
    }

    //devolve o campo de uma determinada tabela em funcao do codigo
    public String getEmailClienteString()
    {

        String sql = "SELECT email FROM  tb_cliente WHERE( nome  = '" + getCliente() + "')";

        ResultSet rs = conexao.executeQuery( sql );

        try
        {
            if ( rs.next() )
            {
                return rs.getString( "email" );
            }
        }
        catch ( SQLException ex )
        {
            return "";
        }
        return "";
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

    private void procedimento_chamar_extrato()
    {
        try
        {
            if ( ck_contas_pendentes.isSelected() )
            {
                mostrarContasPendentesResumidaClientes();
            }
            else if ( ck_contas_pendentes_detalhada.isSelected() )
            {
                mostrarContasPendentesDetalhadaClientes();
            }
            else
            {
                mostrarClientesPagos();
            }

        }
        catch ( SQLException ex )
        {
            ex.printStackTrace();
        }
    }
}
