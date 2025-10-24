/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

 /*
 * ProvinciaVisao.java
 *
 * Created on 27/12/2013, 12:24:14
 */
package visao;


import java.sql.Connection;
import dao.AnoEconomicoDao;
import dao.DadosInstituicaoDao;
import dao.NotasDao;
import dao.VendaDao;
import entity.Notas;
import entity.NotasItem;
import entity.TbCliente;
import entity.TbDadosInstituicao;
import entity.TbItemVenda;
import entity.TbProduto;
import entity.TbVenda;
import hotel.controller.VendaController;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.*;
import java.math.BigDecimal;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import javax.persistence.EntityManagerFactory;
import javax.swing.JOptionPane;
import static kitanda.util.CfConstantes.YYYY_MM_DD;
import static kitanda.util.CfConstantes.YYYY_MM_DD_T_HH_MM_SS;
import util.BDConexao;
import util.DVML;
import static util.DVML.*;
import util.JPAEntityMannagerFactoryUtil;
import static util.MetodosUtil.*;
import util.MetodosUtilitarios;

/**
 *
 * @author DVML COMERCIAL LDA
 */
public class FicheiroSAFTSistemaVisao extends javax.swing.JFrame
{

    private EntityManagerFactory emf = JPAEntityMannagerFactoryUtil.em;
    private DadosInstituicaoDao dadosInstituicaoDao = new DadosInstituicaoDao( emf );
    private AnoEconomicoDao anoEconomicoDao = new AnoEconomicoDao( emf );
    private VendaDao vendaDao = new VendaDao( emf );
    private NotasDao notasDao = new NotasDao( emf );
    private List<TbCliente> list_cliente_venda = null;
    private List<TbCliente> list_cliente_notas = null;
    private List<TbProduto> list_produto_venda = null;
    private List<TbProduto> list_produto_notas = null;
    private List<TbVenda> list_venda = null;
    private List<TbVenda> list_recibo = null;
    private List<TbVenda> list_proforma = null;
    private List<Notas> list_notas = null;
    private List<TbItemVenda> list_item_proforma = null;
    private List<TbItemVenda> list_item_venda = null;
    private List<NotasItem> list_notas_item = null;
    private BDConexao conexao;
    private int line_number = 0;

    public FicheiroSAFTSistemaVisao( BDConexao conexao )
    {

        initComponents();
        setLocationRelativeTo( null );
        this.conexao = conexao;

        popularComponentes();

        saftDataMinimaJDateChoose.setDate( new Date() );
        saftDataMaximaJDateChooser.setDate( new Date() );
    }

    @SuppressWarnings( "unchecked" )
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        jPanel2 = new javax.swing.JPanel();
        btnCancelar = new javax.swing.JButton();
        btnSalvar = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        saftBannerJLabel = new javax.swing.JLabel();
        saftDataMinimaJLabel = new javax.swing.JLabel();
        saftDataMinimaJDateChoose = new com.toedter.calendar.JDateChooser();
        saftDataMaximaJLabel = new javax.swing.JLabel();
        saftDataMaximaJDateChooser = new com.toedter.calendar.JDateChooser();
        jRadioButton1 = new javax.swing.JRadioButton();
        jPanel7 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Gerar ficheiro SAFT");

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        btnCancelar.setBackground(new java.awt.Color(255, 0, 51));
        btnCancelar.setText("Fechar");
        btnCancelar.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnCancelarActionPerformed(evt);
            }
        });

        btnSalvar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/salvar_16x16.png"))); // NOI18N
        btnSalvar.setText("Gerar");
        btnSalvar.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnSalvarActionPerformed(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        saftBannerJLabel.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 16)); // NOI18N
        saftBannerJLabel.setText("Incluir todos os documentos no seguinte intervalo de datas");

        saftDataMinimaJLabel.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 16)); // NOI18N
        saftDataMinimaJLabel.setText("Até");

        saftDataMinimaJDateChoose.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N

        saftDataMaximaJLabel.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 16)); // NOI18N
        saftDataMaximaJLabel.setText("De:");

        saftDataMaximaJDateChooser.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N

        jRadioButton1.setFont(new java.awt.Font("Lucida Grande", 1, 18)); // NOI18N
        jRadioButton1.setSelected(true);
        jRadioButton1.setText("Facturação");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(101, 101, 101)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(saftDataMinimaJLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(saftDataMaximaJLabel, javax.swing.GroupLayout.Alignment.LEADING))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(saftDataMinimaJDateChoose, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(saftDataMaximaJDateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(saftBannerJLabel))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jRadioButton1)))
                .addContainerGap(41, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jRadioButton1)
                .addGap(32, 32, 32)
                .addComponent(saftBannerJLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(saftDataMaximaJLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(saftDataMinimaJDateChoose, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(saftDataMinimaJLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(saftDataMaximaJDateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(46, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(177, 177, 177)
                .addComponent(btnSalvar, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnCancelar)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnCancelar, javax.swing.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE)
                    .addComponent(btnSalvar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(12, 12, 12))
        );

        jPanel7.setBackground(new java.awt.Color(0, 102, 102));

        jLabel19.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(204, 204, 204));
        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel19.setText("GERAR FICHEIRO SAFT - AO");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalvarActionPerformed

        if ( existe_registro() )
        {
            btnSalvar.setEnabled( false );
            gerarFcheiroSAFT();
        }


    }//GEN-LAST:event_btnSalvarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        dispose();
    }//GEN-LAST:event_btnCancelarActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main( String args[] )
    {
        java.awt.EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                new FicheiroSAFTSistemaVisao( BDConexao.getInstancia() ).setVisible( true );
            }
        } );
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelar;
    public static javax.swing.JButton btnSalvar;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JLabel saftBannerJLabel;
    public static com.toedter.calendar.JDateChooser saftDataMaximaJDateChooser;
    private javax.swing.JLabel saftDataMaximaJLabel;
    public static com.toedter.calendar.JDateChooser saftDataMinimaJDateChoose;
    private javax.swing.JLabel saftDataMinimaJLabel;
    // End of variables declaration//GEN-END:variables

    private void popularComponentes()
    {
//        saftDataMinimaJDateChoose.getDate ();
        saftDataMinimaJDateChoose.addPropertyChangeListener( "date", new PropertyChangeListener()
        {
            @Override
            public void propertyChange( PropertyChangeEvent evt )
            {
                System.err.println( "Data mudou" );
            }
        } );
    }


    private void gerarFcheiroSAFT()
    {
        //#TOP
        int numero_linha;
        List<TbItemVenda> linhas_recibo;
        File directorio = new File( CAMINHO_SAFT );
        File ficheiroSAFT = new File( CAMINHO_SAFT + "/" + FICHEIRO_SAFT );

        list_venda = vendaDao.getAllVendaExceptoFacturaProformaAndReciboAlterado( saftDataMinimaJDateChoose.getDate(), saftDataMaximaJDateChooser.getDate() );
        list_cliente_venda = vendaDao.getAllClienteVenda( saftDataMinimaJDateChoose.getDate(), saftDataMaximaJDateChooser.getDate() );
        list_cliente_notas = notasDao.getAllClienteNotas( saftDataMinimaJDateChoose.getDate(), saftDataMaximaJDateChooser.getDate() );
        list_recibo = vendaDao.getAllRecibo( saftDataMinimaJDateChoose.getDate(), saftDataMaximaJDateChooser.getDate() );
        list_proforma = vendaDao.getAllFacturaProforma( saftDataMinimaJDateChoose.getDate(), saftDataMaximaJDateChooser.getDate() );
        list_produto_venda = vendaDao.getAllProdutosVenda( saftDataMinimaJDateChoose.getDate(), saftDataMaximaJDateChooser.getDate() );
        list_produto_notas = notasDao.getAllProdutosNotas( saftDataMinimaJDateChoose.getDate(), saftDataMaximaJDateChooser.getDate() );
        System.out.println( "TAMANHO NOTA: " + list_produto_notas.size() );
        list_produto_venda = lista_produto_filtrado( list_produto_venda, list_produto_notas );
        list_cliente_venda = lista_cliente_filtrado( list_cliente_venda, list_cliente_notas );
//        list_cliente_venda = lista_cliente_filtrado( list_cliente_venda, list_cliente_notas );

        //CRIAR CAMINHO DO DIRECTORIO DO FICHEIRO SAFT CASO NÃO EXISTAM
        directorio.mkdirs();

        //ELIMINAR O FICHEIRO SAFT CASO EXISTA
        if ( ficheiroSAFT.exists() )
        {
            ficheiroSAFT.delete();
        }

        TbDadosInstituicao dadosDadosInstituicao = dadosInstituicaoDao.findTbDadosInstituicao( 1 );
        /*Prencher as constantes do Header*/
        AGT_SAFT_COMPANY_ID = dadosDadosInstituicao.getNif();
        AGT_SAFT_TAX_REGISTRATION_NUMBER = dadosDadosInstituicao.getNif();
        AGT_SAFT_TAX_ACCOUNTING_BASIS = "F";
        AGT_SAFT_COMPANY_NAME = getStringValida( dadosDadosInstituicao.getNome() );
        AGT_SAFT_BUSINESS_NAME = getStringValida( dadosDadosInstituicao.getNome() );
        AGT_SAFT_ADDRESS_DETAIL = getStringValida( dadosDadosInstituicao.getEnderecos() );
        AGT_SAFT_CITY = getStringValida( "Luanda" );
        AGT_SAFT_COUNTRY = "AO";
        AGT_SAFT_FISCAL_YEAR = anoEconomicoDao.getLastObject().getDesignacao();
        AGT_SAFT_START_DATE = formateDate( YYYY_MM_DD, saftDataMinimaJDateChoose.getDate() );
        AGT_SAFT_END_DATE = formateDate( YYYY_MM_DD, saftDataMaximaJDateChooser.getDate() );
        AGT_SAFT_CURRENCY_CODE = "AOA";
        AGT_SAFT_DATE_CREATED = formateDate( YYYY_MM_DD, new Date() );
        AGT_SAFT_TAX_ENTITY = "Global";
        AGT_SAFT_PRODUCT_COMPANY_TAX_ID = "5417221951";
        AGT_SAFT_SOFTWARE_VALIDATION_NUMBER = "258/AGT/2020";
        AGT_SAFT_PRODUCT_ID = NAME_SOFTWARE + "/" + NAME_COMPANY;
        AGT_SAFT_PRODUCT_VERSION = VERSION_SOFTWARE;
        AGT_SAFT_TELEPHONE = "923409284 940537124";
        AGT_SAFT_FAX = "0000000";
        AGT_SAFT_EMAIL = "dvml.comercial@gmail.com";
        AGT_SAFT_WEBSITE = "";

        StringBuilder buffer = new StringBuilder();
//encoding="utf-8"
        //ADICIONAR LINHAS AO FICHEIRO SAFT
        buffer.append( "<?xml version=\"1.0\" encoding=\"windows-1252\"?>" );
//        buffer.append( "<?xml version=\"1.0\" encoding=\"utf-8\"?>" );
        buffer.append( "\n" );
        buffer.append( "<AuditFile  xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"urn:OECD:StandardAuditFile-Tax:AO_1.01_01 SAFTAO1.01_01.xsd\" xmlns=\"urn:OECD:StandardAuditFile-Tax:AO_1.01_01\">" );
        {
            buffer.append( "\n" );

            //CABEÇALHO
            buffer.append( "<Header>" );
            {
                buffer.append( "\n" );
                buffer.append( "<AuditFileVersion>" ).append( AGT_SAFT_AUDIT_FILE_VERSION ).append( "</AuditFileVersion>" );
                buffer.append( "\n" );
                buffer.append( "<CompanyID>" ).append( AGT_SAFT_COMPANY_ID ).append( "</CompanyID>" );
                buffer.append( "\n" );
                buffer.append( "<TaxRegistrationNumber>" ).append( AGT_SAFT_TAX_REGISTRATION_NUMBER ).append( "</TaxRegistrationNumber>" );
                buffer.append( "\n" );
                buffer.append( "<TaxAccountingBasis>" ).append( AGT_SAFT_TAX_ACCOUNTING_BASIS ).append( "</TaxAccountingBasis>" );
                buffer.append( "\n" );
                buffer.append( "<CompanyName>" ).append( AGT_SAFT_COMPANY_NAME ).append( "</CompanyName>" );
                buffer.append( "\n" );
                buffer.append( "<BusinessName>" ).append( AGT_SAFT_BUSINESS_NAME ).append( "</BusinessName>" );
                buffer.append( "\n" );

                buffer.append( "<CompanyAddress>" );
                {
                    buffer.append( "\n" );
                    buffer.append( "<AddressDetail>" ).append( AGT_SAFT_ADDRESS_DETAIL ).append( "</AddressDetail>" );
                    buffer.append( "\n" );
                    buffer.append( "<City>" ).append( AGT_SAFT_CITY ).append( "</City>" );
                    buffer.append( "\n" );
                    buffer.append( "<Country>" ).append( AGT_SAFT_COUNTRY ).append( "</Country>" );
                    buffer.append( "\n" );
                }
                buffer.append( "</CompanyAddress>" );

                buffer.append( "\n" );
                buffer.append( "<FiscalYear>" ).append( AGT_SAFT_FISCAL_YEAR ).append( "</FiscalYear>" );
                buffer.append( "\n" );
                buffer.append( "<StartDate>" ).append( AGT_SAFT_START_DATE ).append( "</StartDate>" );
                buffer.append( "\n" );
                buffer.append( "<EndDate>" ).append( AGT_SAFT_END_DATE ).append( "</EndDate>" );
                buffer.append( "\n" );
                buffer.append( "<CurrencyCode>" ).append( AGT_SAFT_CURRENCY_CODE ).append( "</CurrencyCode>" );
                buffer.append( "\n" );
                buffer.append( "<DateCreated>" ).append( AGT_SAFT_DATE_CREATED ).append( "</DateCreated>" );
                buffer.append( "\n" );
                buffer.append( "<TaxEntity>" ).append( AGT_SAFT_TAX_ENTITY ).append( "</TaxEntity>" );
                buffer.append( "\n" );
                buffer.append( "<ProductCompanyTaxID>" ).append( AGT_SAFT_PRODUCT_COMPANY_TAX_ID ).append( "</ProductCompanyTaxID>" );
                buffer.append( "\n" );
                buffer.append( "<SoftwareValidationNumber>" ).append( AGT_SAFT_SOFTWARE_VALIDATION_NUMBER ).append( "</SoftwareValidationNumber>" );
                buffer.append( "\n" );
                buffer.append( "<ProductID>" ).append( AGT_SAFT_PRODUCT_ID ).append( "</ProductID>" );
                buffer.append( "\n" );
                buffer.append( "<ProductVersion>" ).append( AGT_SAFT_PRODUCT_VERSION ).append( "</ProductVersion>" );
                buffer.append( "\n" );
                buffer.append( "<Telephone>" ).append( AGT_SAFT_TELEPHONE ).append( "</Telephone>" );
                buffer.append( "\n" );
                buffer.append( "<Fax>" ).append( AGT_SAFT_FAX ).append( "</Fax>" );
                buffer.append( "\n" );
                buffer.append( "<Email>" ).append( AGT_SAFT_EMAIL ).append( "</Email>" );
                buffer.append( "\n" );
                buffer.append( "<Website>" ).append( AGT_SAFT_WEBSITE ).append( "</Website>" );
                buffer.append( "\n" );

            }
            buffer.append( "</Header>" );
            buffer.append( "\n" );

            //REGISTROS RELACIONADOS AS FATURAS
            buffer.append( "<MasterFiles>" );
            {

                {
                    int number_cliente = 1;
                    //INICIO CICLO DOS CLIENTE
                    for ( TbCliente cliente : list_cliente_venda )
                    {

                        buffer.append( "<Customer>" );
                        AGT_SAFT_CUSTOMER_ID = String.valueOf( cliente.getCodigo() );
                        AGT_SAFT_CUSTOMER_ACCOUNT_ID = "Desconhecido";
//                        AGT_SAFT_CUSTOMER_TAX_ID = ( "".equals( cliente.getNif() ) || cliente.getNif() == null ) ? NUMBER_NIF_GENERICO : cliente.getNif();
                        AGT_SAFT_CUSTOMER_TAX_ID = getNifCliente( cliente );
                        AGT_SAFT_CUSTUMER_COMPANY_NAME = getStringValida( cliente.getNome() );
                        String morada_cliente = ( !cliente.getMorada().equals( "" ) ? cliente.getMorada() : "n/a" );
                        AGT_SAFT_CUSTUMER_ADDRESS_DETAIL = getStringValida( morada_cliente );
                        AGT_SAFT_CUSTUMER_CITY = getStringValida( "Luanda" );
                        AGT_SAFT_CUSTUMER_POSTAL_CODE = "*";
                        AGT_SAFT_CUSTUMER_COUNTRY = "AO";
                        AGT_SAFT_SELF_BILLING_INDICATOR = "0";

                        buffer.append( "\n" );
                        buffer.append( "<CustomerID>" ).append( AGT_SAFT_CUSTOMER_ID ).append( "</CustomerID>" );
                        buffer.append( "\n" );
                        buffer.append( "<AccountID>" ).append( AGT_SAFT_CUSTOMER_ACCOUNT_ID ).append( "</AccountID>" );
                        buffer.append( "\n" );
                        buffer.append( "<CustomerTaxID>" ).append( AGT_SAFT_CUSTOMER_TAX_ID ).append( "</CustomerTaxID>" );
                        buffer.append( "\n" );
                        buffer.append( "<CompanyName>" ).append( AGT_SAFT_CUSTUMER_COMPANY_NAME ).append( "</CompanyName>" );

                        buffer.append( "\n" );
                        buffer.append( "<BillingAddress>" );
                        {
                            buffer.append( "\n" );
                            buffer.append( "<AddressDetail>" ).append( AGT_SAFT_CUSTUMER_ADDRESS_DETAIL ).append( "</AddressDetail>" );
                            buffer.append( "\n" );
                            buffer.append( "<City>" ).append( AGT_SAFT_CUSTUMER_CITY ).append( "</City>" );
                            buffer.append( "\n" );
                            buffer.append( "<PostalCode>" ).append( AGT_SAFT_CUSTUMER_POSTAL_CODE ).append( "</PostalCode>" );
                            buffer.append( "\n" );
                            buffer.append( "<Country>" ).append( AGT_SAFT_CUSTUMER_COUNTRY ).append( "</Country>" );
                            buffer.append( "\n" );
                        }
                        buffer.append( "</BillingAddress>" );

                        buffer.append( "\n" );
                        buffer.append( "<SelfBillingIndicator>" ).append( AGT_SAFT_SELF_BILLING_INDICATOR ).append( "</SelfBillingIndicator>" );
                        buffer.append( "\n" );
                        buffer.append( "</Customer>" );
                    }
                    //FIM CICLO DOS CLIENTES

                }

                //PRODUTOS
                {

                    System.err.println( "Tamanho Lista Produto: " + list_produto_venda.size() );

                    //INICIO CICLO DOS PRODUTOS 
                    for ( TbProduto produto : list_produto_venda )
                    {

                        System.err.println( "id produto: " + produto.getCodigo() );
                        System.err.println( "designacao: " + produto.getDesignacao() );
                        buffer.append( "<Product>" );
                        buffer.append( "\n" );
                        AGT_SAFT_PRODUCT_TYPE = getStringValida( "P" );
                        buffer.append( "<ProductType>" ).append( AGT_SAFT_PRODUCT_TYPE ).append( "</ProductType>" );
                        AGT_SAFT_PRODUCT_CODE = String.valueOf( produto.getCodigo() );
                        AGT_SAFT_PRODUCT_GROUP = getStringValida( produto.getCodTipoProduto().getDesignacao() );
                        AGT_SAFT_PRODUCT_DESCRIPTION = getStringValida( produto.getDesignacao() );
                        AGT_SAFT_PRODUCT_NUMBER_CODE = !produto.getCodBarra().equals( "" ) ? produto.getCodBarra() : "999999999";

                        buffer.append( "\n" );
                        buffer.append( "<ProductCode>" ).append( AGT_SAFT_PRODUCT_CODE ).append( "</ProductCode>" );
                        buffer.append( "\n" );
                        buffer.append( "<ProductGroup>" ).append( AGT_SAFT_PRODUCT_GROUP ).append( "</ProductGroup>" );
                        buffer.append( "\n" );
                        buffer.append( "<ProductDescription>" ).append( AGT_SAFT_PRODUCT_DESCRIPTION ).append( "</ProductDescription>" );
                        buffer.append( "\n" );
                        buffer.append( "<ProductNumberCode>" ).append( AGT_SAFT_PRODUCT_NUMBER_CODE ).append( "</ProductNumberCode>" );
                        buffer.append( "\n" );
                        buffer.append( "</Product>" );
                        buffer.append( "\n" );
                    }
                    //FIM CICLO DOS PRODUTOS 

                }
//            /*Vizualizar Taxa do Iva*/
                //TABELAS DE IMPOSTOS - EX: ISENTO DE IMPOSTOS E COM IVA
                buffer.append( "<TaxTable>" );
                {
                    AGT_SAFT_TAX_TYPE = "IVA";
                    AGT_SAFT_TAX_CODE = "NOR";
                    AGT_SAFT_TAX_COUNTRY_REGION = "AO";
                    AGT_SAFT_TAX_DESCRIPTION = getStringValida( "IMPOSTO SOBRE VALOR ACRESCENTADO" );
                    AGT_SAFT_TAX_PERCENTAGE = "14";
                    AGT_SAFT_TAX_AMOUNT = "14";

                    buffer.append( "\n" );
                    buffer.append( "<TaxTableEntry>" );
                    {
                        buffer.append( "\n" );
                        buffer.append( "<TaxType>" ).append( AGT_SAFT_TAX_TYPE ).append( "</TaxType>" );
                        buffer.append( "\n" );
                        buffer.append( "<TaxCountryRegion>" ).append( AGT_SAFT_TAX_COUNTRY_REGION ).append( "</TaxCountryRegion>" );
                        buffer.append( "\n" );
                        buffer.append( "<TaxCode>" ).append( AGT_SAFT_TAX_CODE ).append( "</TaxCode>" );
                        buffer.append( "\n" );
                        buffer.append( "<Description>" ).append( AGT_SAFT_TAX_DESCRIPTION ).append( "</Description>" );
                        buffer.append( "\n" );
                        buffer.append( "<TaxPercentage>" ).append( AGT_SAFT_TAX_PERCENTAGE ).append( "</TaxPercentage>" );
                    }
                    buffer.append( "</TaxTableEntry>" );
                    buffer.append( "\n" );
                }
                buffer.append( "</TaxTable>" );
                buffer.append( "\n" );
            }
            buffer.append( "</MasterFiles>" );
            buffer.append( "\n" );

            buffer.append( "<SourceDocuments>" );
            buffer.append( "\n" );
            {
                //#FT, FR 
                buffer.append( "<SalesInvoices>" );
                buffer.append( "\n" );
                {
                    AGT_SAFT_NUMBER_OF_ENTRIES = String.valueOf( ( !Objects.isNull( list_venda ) ? list_venda.size() : 0 ) + ( !Objects.isNull( list_notas ) ? list_notas.size() : 0 ) );
                    AGT_SAFT_TOTAL_DEBIT = String.valueOf( getValorCasasDecimais( MetodosUtilitarios.getTotalNotasCreditoSemIva( list_notas, DOC_NOTA_CREDITO_NC ), CASAS_DECIMAIS ) );
                    AGT_SAFT_TOTAL_CREDIT = String.valueOf( getValorCasasDecimais(
                            MetodosUtilitarios.getTotalSemIva( list_venda, DOC_FACTURA_RECIBO_FR )
                            + MetodosUtilitarios.getTotalNotasCreditoSemIva( list_notas, DVML.DOC_NOTA_DEBITO_ND ),
                            CASAS_DECIMAIS )
                    );

                    buffer.append( "<NumberOfEntries>" ).append( AGT_SAFT_NUMBER_OF_ENTRIES ).append( "</NumberOfEntries>" );
                    buffer.append( "\n" );
                    buffer.append( "<TotalDebit>" ).append( AGT_SAFT_TOTAL_DEBIT ).append( "</TotalDebit>" );
                    buffer.append( "\n" );
                    buffer.append( "<TotalCredit>" ).append( AGT_SAFT_TOTAL_CREDIT ).append( "</TotalCredit>" );
                    buffer.append( "\n" );
                    //INICIO DO CICLO DAS VENDAS
                    for ( TbVenda venda : list_venda )
                    {

                        AGT_SAFT_INVOICE_NO = venda.getCodFact();
                        AGT_SAFT_INVOICE_STATUS = "N";
                        AGT_SAFT_INVOICE_STATUS_DATE = formateDate( YYYY_MM_DD_T_HH_MM_SS, venda.getDataVenda() );
                        AGT_SAFT_SOURCE_ID = venda.getCodigoUsuario().getNome();
                        AGT_SAFT_SOURCE_BILLING = "P";
                        AGT_SAFT_HASH = venda.getHashCod();
                        AGT_SAFT_HASH_CONTROL = "1.0";
                        AGT_SAFT_PERIOD = String.valueOf( venda.getDataVenda().getMonth() + 1 );
                        AGT_SAFT_INVOICE_DATE = formateDate( YYYY_MM_DD, venda.getDataVenda() );
                        AGT_SAFT_INVOICE_TYPE = venda.getFkDocumento().getAbreviacao();
                        AGT_SAFT_STATUS_SELF_BILLING_INDICATOR = "0";
                        AGT_SAFT_STATUS_CASH_VAT_SCHEME_INDICATOR = "0";
                        AGT_SAFT_STATUS_THIRD_PARTIES_BILLING_INDICATOR = "0";
                        AGT_SAFT_INVOICE_SOURCE_ID = venda.getCodigoUsuario().getNome();
                        AGT_SAFT_INVOICE_SYSTEM_ENTRY_DATE = formateDate( YYYY_MM_DD_T_HH_MM_SS, venda.getDataVenda() );
                        AGT_SAFT_INVOICE_CUSTOMER_ID = String.valueOf( venda.getCodigoCliente().getCodigo() );

                        buffer.append( "<Invoice>" );
                        {

                            System.out.println( "INVOICE NO: " + AGT_SAFT_INVOICE_NO );
                            //FIM DAS VENDAS
                            buffer.append( "\n" );
                            buffer.append( "<InvoiceNo>" ).append( AGT_SAFT_INVOICE_NO ).append( "</InvoiceNo>" );
                            buffer.append( "\n" );
                            buffer.append( "<DocumentStatus>" );
                            {
                                buffer.append( "\n" );
                                buffer.append( "<InvoiceStatus>" ).append( AGT_SAFT_INVOICE_STATUS ).append( "</InvoiceStatus>" );
                                buffer.append( "\n" );
                                buffer.append( "<InvoiceStatusDate>" ).append( AGT_SAFT_INVOICE_STATUS_DATE ).append( "</InvoiceStatusDate>" );
                                buffer.append( "\n" );
                                buffer.append( "<SourceID>" ).append( AGT_SAFT_SOURCE_ID ).append( "</SourceID>" );
                                buffer.append( "\n" );
                                buffer.append( "<SourceBilling>" ).append( AGT_SAFT_SOURCE_BILLING ).append( "</SourceBilling>" );
                                buffer.append( "\n" );

                            }
                            buffer.append( "</DocumentStatus>" );
                            buffer.append( "\n" );
                            buffer.append( "<Hash>" ).append( AGT_SAFT_HASH ).append( "</Hash>" );
                            buffer.append( "\n" );
                            buffer.append( "<HashControl>" ).append( AGT_SAFT_HASH_CONTROL ).append( "</HashControl>" );
                            buffer.append( "\n" );
                            buffer.append( "<Period>" ).append( AGT_SAFT_PERIOD ).append( "</Period>" );
                            buffer.append( "\n" );
                            buffer.append( "<InvoiceDate>" ).append( AGT_SAFT_INVOICE_DATE ).append( "</InvoiceDate>" );
                            buffer.append( "\n" );
                            buffer.append( "<InvoiceType>" ).append( AGT_SAFT_INVOICE_TYPE ).append( "</InvoiceType>" );
                            buffer.append( "\n" );

                            buffer.append( "<SpecialRegimes>" );
                            {
                                buffer.append( "\n" );
                                buffer.append( "<SelfBillingIndicator>" ).append( AGT_SAFT_STATUS_SELF_BILLING_INDICATOR ).append( "</SelfBillingIndicator>" );
                                buffer.append( "\n" );
                                buffer.append( "<CashVATSchemeIndicator>" ).append( AGT_SAFT_STATUS_CASH_VAT_SCHEME_INDICATOR ).append( "</CashVATSchemeIndicator>" );
                                buffer.append( "\n" );
                                buffer.append( "<ThirdPartiesBillingIndicator>" ).append( AGT_SAFT_STATUS_THIRD_PARTIES_BILLING_INDICATOR ).append( "</ThirdPartiesBillingIndicator>" );
                                buffer.append( "\n" );

                            }
                            buffer.append( "</SpecialRegimes>" );
                            buffer.append( "\n" );
                            buffer.append( "<SourceID>" ).append( AGT_SAFT_INVOICE_SOURCE_ID ).append( "</SourceID>" );
                            buffer.append( "\n" );
                            buffer.append( "<SystemEntryDate>" ).append( AGT_SAFT_INVOICE_SYSTEM_ENTRY_DATE ).append( "</SystemEntryDate>" );
                            buffer.append( "\n" );
                            buffer.append( "<CustomerID>" ).append( AGT_SAFT_INVOICE_CUSTOMER_ID ).append( "</CustomerID>" );
                            buffer.append( "\n" );

                            list_item_venda = venda.getTbItemVendaList();
                            line_number = 1;
                            //INICIO LINHA
                            for ( TbItemVenda item_venda : list_item_venda )
                            {

                                AGT_SAFT_LINE_NUMBER = "" + line_number;
                                AGT_SAFT_LINE_PRODUCT_CODE = String.valueOf( item_venda.getCodigoProduto().getCodigo() );
                                AGT_SAFT_LINE_QUANTITY = String.valueOf( item_venda.getQuantidade() );
                                AGT_SAFT_LINE_UNIT_OF_MEASURE = "Un";
                                AGT_SAFT_LINE_UNIT_PRICE = String.valueOf( getValorCasasDecimais( item_venda.getFkPreco().getPrecoVenda().doubleValue(), CASAS_DECIMAIS ) );
                                AGT_SAFT_LINE_TAX_POINT_DATE = formateDate( YYYY_MM_DD, item_venda.getCodigoProduto().getDataEntrada() );
                                AGT_SAFT_LINE_PRODUCT_DESCRIPTION = getStringValida( item_venda.getCodigoProduto().getDesignacao() );
                                AGT_SAFT_LINE_DESCRIPTION = "N/A";
                                AGT_SAFT_LINE_CREDIT_AMOUNT = String.valueOf( getValorCasasDecimais( item_venda.getFkPreco().getPrecoVenda().doubleValue() * item_venda.getQuantidade(), CASAS_DECIMAIS ) );
                                AGT_SAFT_LINE_TAX_TYPE = String.valueOf( getTaxType( item_venda.getCodigoProduto().getCodigo() ) );
                                AGT_SAFT_LINE_TAX_COUNTRY_REGION = "AO";
                                AGT_SAFT_LINE_TAX_CODE = String.valueOf( getTaxCode( item_venda.getCodigoProduto().getCodigo() ) );
                                AGT_SAFT_LINE_TAX_PERCENTAGE = String.valueOf( getTaxaPercantagem( item_venda.getCodigoProduto().getCodigo() ) );
                                AGT_SAFT_LINE_SETTLEMENT_AMOUNT = String.valueOf( getValorCasasDecimais( item_venda.getDesconto(), CASAS_DECIMAIS ) );
                                AGT_SAFT_LINE_TAX_EXEMPTION_REASON = getStringValida( getMotivoIsensao( item_venda.getCodigoProduto().getCodigo() ) );
                                AGT_SAFT_LINE_TAX_TAX_EXEMPTION_CODE = getCodigoRegime( item_venda.getCodigoProduto().getCodigo() );

                                buffer.append( "<Line>" );
                                {
                                    buffer.append( "\n" );
                                    buffer.append( "<LineNumber>" ).append( AGT_SAFT_LINE_NUMBER ).append( "</LineNumber>" );
                                    buffer.append( "\n" );

                                    buffer.append( "<ProductCode>" ).append( AGT_SAFT_LINE_PRODUCT_CODE ).append( "</ProductCode>" );
                                    buffer.append( "\n" );
                                    buffer.append( "<ProductDescription>" ).append( AGT_SAFT_LINE_PRODUCT_DESCRIPTION ).append( "</ProductDescription>" );
                                    buffer.append( "\n" );
                                    buffer.append( "<Quantity>" ).append( AGT_SAFT_LINE_QUANTITY ).append( "</Quantity>" );
                                    buffer.append( "\n" );
                                    buffer.append( "<UnitOfMeasure>" ).append( AGT_SAFT_LINE_UNIT_OF_MEASURE ).append( "</UnitOfMeasure>" );
                                    buffer.append( "\n" );
                                    buffer.append( "<UnitPrice>" ).append( AGT_SAFT_LINE_UNIT_PRICE ).append( "</UnitPrice>" );
                                    buffer.append( "\n" );
                                    buffer.append( "<TaxPointDate>" ).append( AGT_SAFT_LINE_TAX_POINT_DATE ).append( "</TaxPointDate>" );
                                    buffer.append( "\n" );
                                    buffer.append( "<Description>" ).append( AGT_SAFT_LINE_PRODUCT_DESCRIPTION ).append( "</Description>" );
                                    buffer.append( "\n" );
                                    buffer.append( "<CreditAmount>" ).append( AGT_SAFT_LINE_CREDIT_AMOUNT ).append( "</CreditAmount>" );
                                    buffer.append( "\n" );

                                    buffer.append( "<Tax>" );
                                    {
                                        buffer.append( "\n" );
                                        buffer.append( "<TaxType>" ).append( AGT_SAFT_LINE_TAX_TYPE ).append( "</TaxType>" );
                                        buffer.append( "\n" );
                                        buffer.append( "<TaxCountryRegion>" ).append( AGT_SAFT_LINE_TAX_COUNTRY_REGION ).append( "</TaxCountryRegion>" );
                                        buffer.append( "\n" );
                                        buffer.append( "<TaxCode>" ).append( AGT_SAFT_LINE_TAX_CODE ).append( "</TaxCode>" );
                                        buffer.append( "\n" );
                                        buffer.append( "<TaxPercentage>" ).append( AGT_SAFT_LINE_TAX_PERCENTAGE ).append( "</TaxPercentage>" );
                                        buffer.append( "\n" );
                                    }
                                    buffer.append( "</Tax>" );
                                    buffer.append( "\n" );
                                    if ( !AGT_SAFT_LINE_TAX_EXEMPTION_REASON.isEmpty() )
                                    {
                                        buffer.append( "<TaxExemptionReason>" ).append( AGT_SAFT_LINE_TAX_EXEMPTION_REASON ).append( "</TaxExemptionReason>" );
                                        buffer.append( "\n" );
                                        buffer.append( "<TaxExemptionCode>" ).append( AGT_SAFT_LINE_TAX_TAX_EXEMPTION_CODE ).append( "</TaxExemptionCode>" );
                                        buffer.append( "\n" );
                                    }
                                    buffer.append( "<SettlementAmount>" ).append( AGT_SAFT_LINE_SETTLEMENT_AMOUNT ).append( "</SettlementAmount>" );
                                    buffer.append( "\n" );

                                }
                                buffer.append( "</Line>" );
                                line_number++;

                            }
                            //FIM LINHA
                            buffer.append( "\n" );
                            double total_iva_sem_desconto = MetodosUtilitarios.getTotalVendaIVASemIncluirDesconto( venda.getTbItemVendaList() );

                            double net_total = MetodosUtilitarios.getNetTotal( venda.getTbItemVendaList() );
                            double gross_total = ( net_total + total_iva_sem_desconto );
                            System.out.println( "DOC: " + venda.getCodFact() + " - AGT_SAFT_LINE_TAX_PAYABLE = " + total_iva_sem_desconto );
                            AGT_SAFT_LINE_TAX_PAYABLE = String.valueOf( getValorCasasDecimais( total_iva_sem_desconto, CASAS_DECIMAIS ) );
                            AGT_SAFT_LINE_NET_TOTAL = String.valueOf( getValorCasasDecimais( net_total, CASAS_DECIMAIS ) );
                            AGT_SAFT_LINE_GROSS_TOTAL = String.valueOf( getValorCasasDecimais( gross_total, CASAS_DECIMAIS ) );
                            AGT_SAFT_TOTAL_DESCONTO_FACTURA = String.valueOf( getValorCasasDecimais( venda.getDescontoTotal().doubleValue(), CASAS_DECIMAIS ) );
                            buffer.append( "<DocumentTotals>" );
                            {
                                buffer.append( "\n" );
                                buffer.append( "<TaxPayable>" ).append( AGT_SAFT_LINE_TAX_PAYABLE ).append( "</TaxPayable>" );
                                buffer.append( "\n" );
                                buffer.append( "<NetTotal>" ).append( AGT_SAFT_LINE_NET_TOTAL ).append( "</NetTotal>" );
                                buffer.append( "\n" );
                                buffer.append( "<GrossTotal>" ).append( AGT_SAFT_LINE_GROSS_TOTAL ).append( "</GrossTotal>" );
                                buffer.append( "\n" );

                                // NO CASO DE MOEDA ESTRANGEIRA
                                if ( !Objects.equals( venda.getFkCambio().getFkMoeda().getPkMoeda(), DVML.MOEDA_NACIONAL ) )
                                {

                                    AGT_SAFT_MOEDA_ESTRANGEIRA = venda.getFkCambio().getFkMoeda().getAbreviacao();
                                    AGT_SAFT_MOEDA_CAMBIO = getValorCasasDecimais( venda.getFkCambio().getValor(), CASAS_DECIMAIS );

                                    buffer.append( "<Currency>" );
                                    {
                                        buffer.append( "\n" );
                                        buffer.append( "<CurrencyCode>" ).append( AGT_SAFT_MOEDA_ESTRANGEIRA ).append( "</CurrencyCode>" );
                                        buffer.append( "\n" );
                                        buffer.append( "<CurrencyAmount>" ).append( AGT_SAFT_LINE_GROSS_TOTAL ).append( "</CurrencyAmount>" );
                                        buffer.append( "\n" );
                                        buffer.append( "<ExchangeRate>" ).append( AGT_SAFT_MOEDA_CAMBIO ).append( "</ExchangeRate>" );
                                        buffer.append( "\n" );

                                    }
                                    buffer.append( "</Currency>" );

                                }

                                //desconto
                                buffer.append( "<Settlement>" );
                                {
                                    buffer.append( "\n" );
                                    buffer.append( "<SettlementAmount>" ).append( AGT_SAFT_TOTAL_DESCONTO_FACTURA ).append( "</SettlementAmount>" );
                                    buffer.append( "\n" );
                                }
                                buffer.append( "</Settlement>" );
                                buffer.append( "\n" );

                            }
                            buffer.append( "</DocumentTotals>" );
                            buffer.append( "\n" );

                        }

                        buffer.append( "</Invoice>" );

                        buffer.append( "\n" );

                    }//FIM DO CICLO DAS VENDAS

                    /**
                     * INICIO DAS NOTAS DE CRÉDITO E DÉBITO
                     */
                    //#NC ND - NOTA DE DÉBITO E NOTA DE CRÉDITO
                    if ( !Objects.isNull( list_notas ) )
                    {

                        for ( Notas nota : list_notas )
                        {

                            AGT_SAFT_INVOICE_NO = nota.getCodNota();
                            AGT_SAFT_INVOICE_STATUS = nota.getEstado().equalsIgnoreCase( ESTADO_NOTA.ANULADO.toString() ) ? "A" : "N";
                            AGT_SAFT_INVOICE_STATUS_DATE = formateDate( YYYY_MM_DD_T_HH_MM_SS, nota.getDataNota() );
                            AGT_SAFT_SOURCE_ID = nota.getCodigoUsuario().getNome() + " " + nota.getCodigoUsuario().getSobreNome();
                            AGT_SAFT_SOURCE_BILLING = "P";
                            AGT_SAFT_HASH = nota.getHashCod();
                            AGT_SAFT_HASH_CONTROL = "1.0";
                            AGT_SAFT_PERIOD = String.valueOf( nota.getDataNota().getMonth() + 1 );
                            AGT_SAFT_INVOICE_DATE = formateDate( YYYY_MM_DD, nota.getDataNota() );
                            AGT_SAFT_INVOICE_TYPE = nota.getFkDocumento().getAbreviacao();
                            AGT_SAFT_STATUS_SELF_BILLING_INDICATOR = "0";
                            AGT_SAFT_STATUS_CASH_VAT_SCHEME_INDICATOR = "0";
                            AGT_SAFT_STATUS_THIRD_PARTIES_BILLING_INDICATOR = "0";
                            AGT_SAFT_INVOICE_SOURCE_ID = nota.getCodigoUsuario().getNome() + " " + nota.getCodigoUsuario().getSobreNome();
                            AGT_SAFT_INVOICE_SYSTEM_ENTRY_DATE = formateDate( YYYY_MM_DD_T_HH_MM_SS, nota.getDataNota() );
                            AGT_SAFT_INVOICE_CUSTOMER_ID = String.valueOf( nota.getCodigoCliente().getCodigo() );
                            TbVenda documentoOrigem = vendaDao.findByCodFact( nota.getRefCodFact() );
                            buffer.append( "<Invoice>" );
                            {

                                //FIM DAS VENDAS
                                buffer.append( "\n" );
                                buffer.append( "<InvoiceNo>" ).append( AGT_SAFT_INVOICE_NO ).append( "</InvoiceNo>" );
                                buffer.append( "\n" );
                                buffer.append( "<DocumentStatus>" );
                                {
                                    buffer.append( "\n" );
                                    buffer.append( "<InvoiceStatus>" ).append( AGT_SAFT_INVOICE_STATUS ).append( "</InvoiceStatus>" );
                                    buffer.append( "\n" );
                                    buffer.append( "<InvoiceStatusDate>" ).append( AGT_SAFT_INVOICE_STATUS_DATE ).append( "</InvoiceStatusDate>" );
                                    buffer.append( "\n" );
                                    buffer.append( "<SourceID>" ).append( AGT_SAFT_SOURCE_ID ).append( "</SourceID>" );
                                    buffer.append( "\n" );
                                    buffer.append( "<SourceBilling>" ).append( AGT_SAFT_SOURCE_BILLING ).append( "</SourceBilling>" );
                                    buffer.append( "\n" );

                                }
                                buffer.append( "\n" );
                                buffer.append( "</DocumentStatus>" );
                                buffer.append( "\n" );
                                buffer.append( "<Hash>" ).append( AGT_SAFT_HASH ).append( "</Hash>" );
                                buffer.append( "\n" );
                                buffer.append( "<HashControl>" ).append( AGT_SAFT_HASH_CONTROL ).append( "</HashControl>" );
                                buffer.append( "\n" );
                                buffer.append( "<Period>" ).append( AGT_SAFT_PERIOD ).append( "</Period>" );
                                buffer.append( "\n" );
                                buffer.append( "<InvoiceDate>" ).append( AGT_SAFT_INVOICE_DATE ).append( "</InvoiceDate>" );
                                buffer.append( "\n" );
                                buffer.append( "<InvoiceType>" ).append( AGT_SAFT_INVOICE_TYPE ).append( "</InvoiceType>" );
                                buffer.append( "\n" );

                                buffer.append( "\n" );
                                buffer.append( "<SpecialRegimes>" );
                                {
                                    buffer.append( "\n" );
                                    buffer.append( "<SelfBillingIndicator>" ).append( AGT_SAFT_STATUS_SELF_BILLING_INDICATOR ).append( "</SelfBillingIndicator>" );
                                    buffer.append( "\n" );
                                    buffer.append( "<CashVATSchemeIndicator>" ).append( AGT_SAFT_STATUS_CASH_VAT_SCHEME_INDICATOR ).append( "</CashVATSchemeIndicator>" );
                                    buffer.append( "\n" );
                                    buffer.append( "<ThirdPartiesBillingIndicator>" ).append( AGT_SAFT_STATUS_THIRD_PARTIES_BILLING_INDICATOR ).append( "</ThirdPartiesBillingIndicator>" );
                                    buffer.append( "\n" );

                                }
                                buffer.append( "\n" );
                                buffer.append( "</SpecialRegimes>" );
                                buffer.append( "\n" );
                                buffer.append( "<SourceID>" ).append( AGT_SAFT_INVOICE_SOURCE_ID ).append( "</SourceID>" );
                                buffer.append( "\n" );
                                buffer.append( "<SystemEntryDate>" ).append( AGT_SAFT_INVOICE_SYSTEM_ENTRY_DATE ).append( "</SystemEntryDate>" );
                                buffer.append( "\n" );
                                buffer.append( "<CustomerID>" ).append( AGT_SAFT_INVOICE_CUSTOMER_ID ).append( "</CustomerID>" );
                                buffer.append( "\n" );

//                                list_notas_item = nota.getNotasItemList();
                                list_notas_item = nota.getEstado().equalsIgnoreCase( ESTADO_NOTA.ANULADO.toString() ) ? getConvertVendasToNotas( documentoOrigem ) : nota.getNotasItemList();
//                                list_notas_item = null;
                                line_number = 1;

                                //INICIO LINHA
                                for ( NotasItem item_nota : list_notas_item )
                                {

                                    AGT_SAFT_LINE_NUMBER = "" + line_number;
                                    AGT_SAFT_LINE_PRODUCT_CODE = String.valueOf( item_nota.getFkProduto().getCodigo() );
                                    AGT_SAFT_LINE_QUANTITY = String.valueOf( item_nota.getQuantidade() );
                                    AGT_SAFT_LINE_UNIT_OF_MEASURE = "Un";
                                    AGT_SAFT_LINE_UNIT_PRICE = String.valueOf( getValorCasasDecimais( item_nota.getFkPreco().getPrecoVenda().doubleValue(), CASAS_DECIMAIS ) );
                                    AGT_SAFT_LINE_TAX_POINT_DATE = formateDate( YYYY_MM_DD, item_nota.getFkProduto().getDataEntrada() );
                                    AGT_SAFT_LINE_PRODUCT_DESCRIPTION = item_nota.getFkProduto().getDesignacao();
                                    AGT_SAFT_LINE_DESCRIPTION = nota.getObs();
                                    AGT_SAFT_LINE_CREDIT_AMOUNT = String.valueOf( getValorCasasDecimais( item_nota.getFkPreco().getPrecoVenda().doubleValue() * item_nota.getQuantidade(), CASAS_DECIMAIS ) );
                                    AGT_SAFT_LINE_DEBIT_AMOUNT = String.valueOf( getValorCasasDecimais( item_nota.getFkPreco().getPrecoVenda().doubleValue() * item_nota.getQuantidade(), CASAS_DECIMAIS ) );
                                    AGT_SAFT_LINE_TAX_TYPE = String.valueOf( getTaxType( item_nota.getFkProduto().getCodigo() ) );
                                    AGT_SAFT_LINE_TAX_COUNTRY_REGION = "AO";
                                    AGT_SAFT_LINE_TAX_CODE = String.valueOf( getTaxCode( item_nota.getFkProduto().getCodigo() ) );
                                    AGT_SAFT_LINE_TAX_PERCENTAGE = String.valueOf( getTaxaPercantagem( item_nota.getFkProduto().getCodigo() ) );
                                    AGT_SAFT_LINE_SETTLEMENT_AMOUNT = String.valueOf( getValorCasasDecimais( item_nota.getDesconto(), CASAS_DECIMAIS ) );
                                    AGT_SAFT_LINE_TAX_EXEMPTION_REASON = getMotivoIsensao( item_nota.getFkProduto().getCodigo() );
                                    AGT_SAFT_LINE_TAX_TAX_EXEMPTION_CODE = getCodigoRegime( item_nota.getFkProduto().getCodigo() );
                                    AGT_SAFT_LINE_ORIGINATION_ON = nota.getRefCodFact();
                                    AGT_SAFT_LINE_ORDER_DATE = formateDate( YYYY_MM_DD, nota.getRefFactData() );
                                    AGT_LINE_REFERENCES_REFERENCE = nota.getRefCodFact();
                                    AGT_LINE_REFERENCES_REASON = ( nota.getMotivo().length() < 50 ) ? nota.getMotivo() : nota.getMotivo().substring( 0, 49 ); // so e permitido 50 caracteres

                                    buffer.append( "<Line>" );
                                    {
                                        buffer.append( "\n" );
                                        buffer.append( "<LineNumber>" ).append( AGT_SAFT_LINE_NUMBER ).append( "</LineNumber>" );
                                        buffer.append( "\n" );
                                        buffer.append( "<OrderReferences>" );
                                        {
                                            buffer.append( "<OriginatingON>" ).append( AGT_SAFT_LINE_ORIGINATION_ON ).append( "</OriginatingON>" );
                                            buffer.append( "<OrderDate>" ).append( AGT_SAFT_LINE_ORDER_DATE ).append( "</OrderDate>" );
                                        }
                                        buffer.append( "</OrderReferences>" );
                                        buffer.append( "\n" );
                                        buffer.append( "<ProductCode>" ).append( AGT_SAFT_LINE_PRODUCT_CODE ).append( "</ProductCode>" );
                                        buffer.append( "\n" );
                                        buffer.append( "<ProductDescription>" ).append( AGT_SAFT_LINE_PRODUCT_DESCRIPTION ).append( "</ProductDescription>" );
                                        buffer.append( "\n" );
                                        buffer.append( "<Quantity>" ).append( AGT_SAFT_LINE_QUANTITY ).append( "</Quantity>" );
                                        buffer.append( "\n" );
                                        buffer.append( "<UnitOfMeasure>" ).append( AGT_SAFT_LINE_UNIT_OF_MEASURE ).append( "</UnitOfMeasure>" );
                                        buffer.append( "\n" );
                                        buffer.append( "<UnitPrice>" ).append( AGT_SAFT_LINE_UNIT_PRICE ).append( "</UnitPrice>" );
                                        buffer.append( "\n" );
                                        buffer.append( "<TaxPointDate>" ).append( AGT_SAFT_LINE_TAX_POINT_DATE ).append( "</TaxPointDate>" );
                                        buffer.append( "\n" );
                                        //4.1.4.19.10
                                        buffer.append( "<References>" );
                                        {
                                            buffer.append( "<Reference>" ).append( AGT_LINE_REFERENCES_REFERENCE ).append( "</Reference>" );
                                            buffer.append( "\n" );
                                            buffer.append( "<Reason>" ).append( AGT_LINE_REFERENCES_REASON ).append( "</Reason>" );
                                            buffer.append( "\n" );
                                        }
                                        buffer.append( "</References>" );

                                        buffer.append( "<Description>" ).append( AGT_SAFT_LINE_DESCRIPTION ).append( "</Description>" );
                                        // buffer.append("<Description>").append(AGT_SAFT_LINE_PRODUCT_DESCRIPTION).append("</Description>");
                                        buffer.append( "\n" );
                                        //  buffer.append("<CreditAmount>").append(AGT_SAFT_LINE_CREDIT_AMOUNT).append("</CreditAmount>");
                                        if ( nota.getFkDocumento().getPkDocumento() == DVML.DOC_NOTA_CREDITO_NC )
                                        {
                                            buffer.append( "<DebitAmount>" ).append( AGT_SAFT_LINE_DEBIT_AMOUNT ).append( "</DebitAmount>" );
                                            buffer.append( "\n" );
                                        }
                                        else
                                        {
                                            buffer.append( "<CreditAmount>" ).append( AGT_SAFT_LINE_CREDIT_AMOUNT ).append( "</CreditAmount>" );
                                            buffer.append( "\n" );
                                        }

                                        buffer.append( "<Tax>" );
                                        {
                                            buffer.append( "\n" );
                                            buffer.append( "<TaxType>" ).append( AGT_SAFT_LINE_TAX_TYPE ).append( "</TaxType>" );
                                            buffer.append( "\n" );
                                            buffer.append( "<TaxCountryRegion>" ).append( AGT_SAFT_LINE_TAX_COUNTRY_REGION ).append( "</TaxCountryRegion>" );
                                            buffer.append( "\n" );
                                            buffer.append( "<TaxCode>" ).append( AGT_SAFT_LINE_TAX_CODE ).append( "</TaxCode>" );
                                            buffer.append( "\n" );
                                            buffer.append( "<TaxPercentage>" ).append( AGT_SAFT_LINE_TAX_PERCENTAGE ).append( "</TaxPercentage>" );
                                            buffer.append( "\n" );
                                        }
                                        buffer.append( "</Tax>" );
                                        buffer.append( "\n" );
                                        if ( !AGT_SAFT_LINE_TAX_EXEMPTION_REASON.isEmpty() )
                                        {
                                            buffer.append( "<TaxExemptionReason>" ).append( AGT_SAFT_LINE_TAX_EXEMPTION_REASON ).append( "</TaxExemptionReason>" );
                                            buffer.append( "\n" );
                                            buffer.append( "<TaxExemptionCode>" ).append( AGT_SAFT_LINE_TAX_TAX_EXEMPTION_CODE ).append( "</TaxExemptionCode>" );
                                            buffer.append( "\n" );
                                        }
                                        buffer.append( "<SettlementAmount>" ).append( AGT_SAFT_LINE_SETTLEMENT_AMOUNT ).append( "</SettlementAmount>" );
                                        buffer.append( "\n" );

                                    }
                                    buffer.append( "</Line>" );
                                    line_number++;

                                }
                                //FIM LINHA
                                buffer.append( "\n" );

                                double total_iva_sem_desconto = nota.getEstado().equalsIgnoreCase( ESTADO_NOTA.ANULADO.toString() ) ? MetodosUtilitarios.getTotalVendaIVASemIncluirDesconto( documentoOrigem.getTbItemVendaList() ) : MetodosUtilitarios.getTotalNotaIVASemIncluirDesconto( nota.getNotasItemList() );
//                                double total_iva_sem_desconto = 0;
                                AGT_SAFT_LINE_TAX_PAYABLE = String.valueOf( getValorCasasDecimais( total_iva_sem_desconto, CASAS_DECIMAIS ) );
                                AGT_SAFT_LINE_NET_TOTAL = String.valueOf( getValorCasasDecimais( ( nota.getEstado().equalsIgnoreCase( ESTADO_NOTA.ANULADO.toString() ) ? documentoOrigem.getTotalGeral().doubleValue() : nota.getTotalGeral() ), CASAS_DECIMAIS ) );
                                AGT_SAFT_LINE_GROSS_TOTAL = String.valueOf( getValorCasasDecimais( ( nota.getEstado().equalsIgnoreCase( ESTADO_NOTA.ANULADO.toString() ) ? documentoOrigem.getTotalGeral().doubleValue() : nota.getTotalGeral() ) + total_iva_sem_desconto, CASAS_DECIMAIS ) );;

                                buffer.append( "<DocumentTotals>" );
                                {
                                    buffer.append( "\n" );
                                    buffer.append( "<TaxPayable>" ).append( AGT_SAFT_LINE_TAX_PAYABLE ).append( "</TaxPayable>" );
                                    buffer.append( "\n" );
                                    buffer.append( "<NetTotal>" ).append( AGT_SAFT_LINE_NET_TOTAL ).append( "</NetTotal>" );
                                    buffer.append( "\n" );
                                    buffer.append( "<GrossTotal>" ).append( AGT_SAFT_LINE_GROSS_TOTAL ).append( "</GrossTotal>" );
                                    buffer.append( "\n" );
                                }
                                buffer.append( "</DocumentTotals>" );
                                buffer.append( "\n" );
                            }

                            buffer.append( "</Invoice>" );
                            buffer.append( "\n" );

                        }

                    }
                    /**
                     * FIM DAS NOTAS DE CRÉDITO E DÉBITO
                     */
                }
                buffer.append( "</SalesInvoices>" );
                buffer.append( "\n" );

                if ( !list_proforma.isEmpty() )
                {
                    //#pp ===========================================================================================================================================================
                    buffer.append( "<WorkingDocuments>" );
                    buffer.append( "\n" );
                    {
                        AGT_SAFT_NUMBER_OF_ENTRIES = String.valueOf( list_proforma.size() );
                        AGT_SAFT_TOTAL_DEBIT = String.valueOf( getValorCasasDecimais( MetodosUtilitarios.getTotalNotasCreditoSemIva( list_notas, DOC_FACTURA_PROFORMA_PP ), CASAS_DECIMAIS ) );
                        AGT_SAFT_TOTAL_CREDIT = String.valueOf( getValorCasasDecimais(
                                MetodosUtilitarios.getTotalSemIva( list_proforma, DOC_FACTURA_PROFORMA_PP ),
                                CASAS_DECIMAIS )
                        );

                        buffer.append( "\n" );
                        buffer.append( "<NumberOfEntries>" ).append( AGT_SAFT_NUMBER_OF_ENTRIES ).append( "</NumberOfEntries>" );
                        buffer.append( "\n" );
                        buffer.append( "<TotalDebit>" ).append( AGT_SAFT_TOTAL_DEBIT ).append( "</TotalDebit>" );
                        buffer.append( "\n" );
                        buffer.append( "<TotalCredit>" ).append( AGT_SAFT_TOTAL_CREDIT ).append( "</TotalCredit>" );
                        buffer.append( "\n" );
                        //INICIO DO CICLO DAS PROFORMAS
                        for ( TbVenda proforma : list_proforma )
                        {

                            AGT_SAFT_INVOICE_NO = proforma.getCodFact();
                            AGT_SAFT_INVOICE_STATUS = "N";
                            AGT_SAFT_INVOICE_STATUS_DATE = formateDate( YYYY_MM_DD_T_HH_MM_SS, proforma.getDataVenda() );
                            AGT_SAFT_SOURCE_ID = proforma.getCodigoUsuario().getNome() + " " + proforma.getCodigoUsuario().getSobreNome();
                            AGT_SAFT_SOURCE_BILLING = "P";
                            AGT_SAFT_HASH = proforma.getHashCod();

                            System.err.println( "#AGT_SAFT_HASH: " + AGT_SAFT_HASH );

                            AGT_SAFT_HASH_CONTROL = "1.0";
                            AGT_SAFT_PERIOD = String.valueOf( proforma.getDataVenda().getMonth() + 1 );
                            AGT_SAFT_INVOICE_DATE = formateDate( YYYY_MM_DD, proforma.getDataVenda() );
                            AGT_SAFT_INVOICE_TYPE = proforma.getFkDocumento().getAbreviacao();
                            AGT_SAFT_STATUS_SELF_BILLING_INDICATOR = "0";
                            AGT_SAFT_STATUS_CASH_VAT_SCHEME_INDICATOR = "0";
                            AGT_SAFT_STATUS_THIRD_PARTIES_BILLING_INDICATOR = "0";
                            AGT_SAFT_INVOICE_SOURCE_ID = proforma.getCodigoUsuario().getNome() + " " + proforma.getCodigoUsuario().getSobreNome();
                            AGT_SAFT_INVOICE_SYSTEM_ENTRY_DATE = formateDate( YYYY_MM_DD_T_HH_MM_SS, proforma.getDataVenda() );
                            AGT_SAFT_INVOICE_CUSTOMER_ID = String.valueOf( proforma.getCodigoCliente().getCodigo() );

                            buffer.append( "<WorkDocument>" );
                            {
                                //FIM DAS PROFORMAS
                                buffer.append( "\n" );
                                buffer.append( "<DocumentNumber>" ).append( AGT_SAFT_INVOICE_NO ).append( "</DocumentNumber>" );
                                buffer.append( "\n" );
                                buffer.append( "<DocumentStatus>" );
                                {
                                    buffer.append( "\n" );
                                    buffer.append( "<WorkStatus>" ).append( AGT_SAFT_INVOICE_STATUS ).append( "</WorkStatus>" );
                                    buffer.append( "\n" );
                                    buffer.append( "<WorkStatusDate>" ).append( AGT_SAFT_INVOICE_STATUS_DATE ).append( "</WorkStatusDate>" );
                                    buffer.append( "\n" );
                                    buffer.append( "<SourceID>" ).append( AGT_SAFT_SOURCE_ID ).append( "</SourceID>" );
                                    buffer.append( "\n" );
                                    buffer.append( "<SourceBilling>" ).append( AGT_SAFT_SOURCE_BILLING ).append( "</SourceBilling>" );
                                    buffer.append( "\n" );

                                }
                                buffer.append( "\n" );
                                buffer.append( "</DocumentStatus>" );
                                buffer.append( "\n" );
                                buffer.append( "<Hash>" ).append( AGT_SAFT_HASH ).append( "</Hash>" );
                                buffer.append( "\n" );
                                buffer.append( "<HashControl>" ).append( AGT_SAFT_HASH_CONTROL ).append( "</HashControl>" );
                                buffer.append( "\n" );
                                buffer.append( "<Period>" ).append( AGT_SAFT_PERIOD ).append( "</Period>" );
                                buffer.append( "\n" );
                                buffer.append( "<WorkDate>" ).append( AGT_SAFT_INVOICE_DATE ).append( "</WorkDate>" );
                                buffer.append( "\n" );
                                buffer.append( "<WorkType>" ).append( AGT_SAFT_INVOICE_TYPE ).append( "</WorkType>" );
                                buffer.append( "\n" );

                                buffer.append( "\n" );
                                buffer.append( "<SourceID>" ).append( AGT_SAFT_INVOICE_SOURCE_ID ).append( "</SourceID>" );
                                buffer.append( "\n" );
                                buffer.append( "<SystemEntryDate>" ).append( AGT_SAFT_INVOICE_SYSTEM_ENTRY_DATE ).append( "</SystemEntryDate>" );
                                buffer.append( "\n" );
                                buffer.append( "<CustomerID>" ).append( AGT_SAFT_INVOICE_CUSTOMER_ID ).append( "</CustomerID>" );
                                buffer.append( "\n" );

                                list_item_proforma = proforma.getTbItemVendaList();
                                line_number = 1;
                                //INICIO LINHA
                                for ( TbItemVenda item_proforma : list_item_proforma )
                                {

                                    AGT_SAFT_LINE_NUMBER = "" + line_number;
                                    AGT_SAFT_LINE_PRODUCT_CODE = String.valueOf( item_proforma.getCodigoProduto().getCodigo() );
                                    AGT_SAFT_LINE_QUANTITY = String.valueOf( item_proforma.getQuantidade() );
                                    AGT_SAFT_LINE_UNIT_OF_MEASURE = "Un";
                                    AGT_SAFT_LINE_UNIT_PRICE = String.valueOf( getValorCasasDecimais( item_proforma.getFkPreco().getPrecoVenda().doubleValue(), CASAS_DECIMAIS ) );
                                    AGT_SAFT_LINE_TAX_POINT_DATE = formateDate( YYYY_MM_DD, item_proforma.getCodigoProduto().getDataEntrada() );
                                    AGT_SAFT_LINE_PRODUCT_DESCRIPTION = item_proforma.getCodigoProduto().getDesignacao();
                                    AGT_SAFT_LINE_DESCRIPTION = "N/A";
                                    AGT_SAFT_LINE_CREDIT_AMOUNT = String.valueOf( getValorCasasDecimais( item_proforma.getFkPreco().getPrecoVenda().doubleValue() * item_proforma.getQuantidade(), CASAS_DECIMAIS ) );
                                    AGT_SAFT_LINE_TAX_TYPE = String.valueOf( getTaxType( item_proforma.getCodigoProduto().getCodigo() ) );
                                    AGT_SAFT_LINE_TAX_COUNTRY_REGION = "AO";
                                    AGT_SAFT_LINE_TAX_CODE = String.valueOf( getTaxCode( item_proforma.getCodigoProduto().getCodigo() ) );
                                    AGT_SAFT_LINE_TAX_PERCENTAGE = String.valueOf( getTaxaPercantagem( item_proforma.getCodigoProduto().getCodigo() ) );
                                    System.err.println( "DESCONTO PROFORMA: " + item_proforma.getDesconto() );
                                    AGT_SAFT_LINE_SETTLEMENT_AMOUNT = String.valueOf( getValorCasasDecimais( ( item_proforma.getDesconto() * item_proforma.getQuantidade() ), CASAS_DECIMAIS ) );
                                    AGT_SAFT_LINE_TAX_EXEMPTION_REASON = getMotivoIsensao( item_proforma.getCodigoProduto().getCodigo() );
                                    AGT_SAFT_LINE_TAX_TAX_EXEMPTION_CODE = getCodigoRegime( item_proforma.getCodigoProduto().getCodigo() );

                                    buffer.append( "<Line>" );
                                    {
                                        buffer.append( "\n" );
                                        buffer.append( "<LineNumber>" ).append( AGT_SAFT_LINE_NUMBER ).append( "</LineNumber>" );
                                        buffer.append( "\n" );
                                        buffer.append( "<ProductCode>" ).append( AGT_SAFT_LINE_PRODUCT_CODE ).append( "</ProductCode>" );
                                        buffer.append( "\n" );
                                        buffer.append( "<ProductDescription>" ).append( AGT_SAFT_LINE_PRODUCT_DESCRIPTION ).append( "</ProductDescription>" );
                                        buffer.append( "\n" );
                                        buffer.append( "<Quantity>" ).append( AGT_SAFT_LINE_QUANTITY ).append( "</Quantity>" );
                                        buffer.append( "\n" );
                                        buffer.append( "<UnitOfMeasure>" ).append( AGT_SAFT_LINE_UNIT_OF_MEASURE ).append( "</UnitOfMeasure>" );
                                        buffer.append( "\n" );
                                        buffer.append( "<UnitPrice>" ).append( AGT_SAFT_LINE_UNIT_PRICE ).append( "</UnitPrice>" );
                                        buffer.append( "\n" );
                                        buffer.append( "<TaxPointDate>" ).append( AGT_SAFT_LINE_TAX_POINT_DATE ).append( "</TaxPointDate>" );
                                        buffer.append( "\n" );
                                        //buffer.append("<Description>").append(AGT_SAFT_LINE_DESCRIPTION).append("</Description>");
                                        buffer.append( "<Description>" ).append( AGT_SAFT_LINE_PRODUCT_DESCRIPTION ).append( "</Description>" );
                                        buffer.append( "\n" );
                                        buffer.append( "<CreditAmount>" ).append( AGT_SAFT_LINE_CREDIT_AMOUNT ).append( "</CreditAmount>" );
                                        buffer.append( "\n" );

                                        buffer.append( "<Tax>" );
                                        {
                                            buffer.append( "\n" );
                                            buffer.append( "<TaxType>" ).append( AGT_SAFT_LINE_TAX_TYPE ).append( "</TaxType>" );
                                            buffer.append( "\n" );
                                            buffer.append( "<TaxCountryRegion>" ).append( AGT_SAFT_LINE_TAX_COUNTRY_REGION ).append( "</TaxCountryRegion>" );
                                            buffer.append( "\n" );
                                            buffer.append( "<TaxCode>" ).append( AGT_SAFT_LINE_TAX_CODE ).append( "</TaxCode>" );
                                            buffer.append( "\n" );
                                            buffer.append( "<TaxPercentage>" ).append( AGT_SAFT_LINE_TAX_PERCENTAGE ).append( "</TaxPercentage>" );
                                            buffer.append( "\n" );
                                        }
                                        buffer.append( "</Tax>" );
                                        buffer.append( "\n" );
                                        if ( !AGT_SAFT_LINE_TAX_EXEMPTION_REASON.isEmpty() )
                                        {
                                            buffer.append( "<TaxExemptionReason>" ).append( AGT_SAFT_LINE_TAX_EXEMPTION_REASON ).append( "</TaxExemptionReason>" );
                                            buffer.append( "\n" );
                                            buffer.append( "<TaxExemptionCode>" ).append( AGT_SAFT_LINE_TAX_TAX_EXEMPTION_CODE ).append( "</TaxExemptionCode>" );
                                            buffer.append( "\n" );
                                        }
                                        buffer.append( "<SettlementAmount>" ).append( AGT_SAFT_LINE_SETTLEMENT_AMOUNT ).append( "</SettlementAmount>" );
                                        buffer.append( "\n" );

                                    }
                                    buffer.append( "</Line>" );
                                    line_number++;

                                }
                                //FIM LINHA
                                buffer.append( "\n" );

                                double total_iva_sem_desconto = MetodosUtilitarios.getTotalVendaIVASemIncluirDesconto( proforma.getTbItemVendaList() );
                                AGT_SAFT_LINE_TAX_PAYABLE = String.valueOf( getValorCasasDecimais( total_iva_sem_desconto, CASAS_DECIMAIS ) );
                                AGT_SAFT_LINE_NET_TOTAL = String.valueOf( getValorCasasDecimais( proforma.getTotalGeral().doubleValue(), CASAS_DECIMAIS ) );
                                AGT_SAFT_LINE_GROSS_TOTAL = String.valueOf( getValorCasasDecimais( proforma.getTotalGeral().doubleValue() + total_iva_sem_desconto, CASAS_DECIMAIS ) );
                                AGT_SAFT_TOTAL_DESCONTO_FACTURA = String.valueOf( getValorCasasDecimais( proforma.getDescontoTotal().doubleValue(), CASAS_DECIMAIS ) );

                                buffer.append( "<DocumentTotals>" );
                                {
                                    buffer.append( "\n" );
                                    buffer.append( "<TaxPayable>" ).append( AGT_SAFT_LINE_TAX_PAYABLE ).append( "</TaxPayable>" );
                                    buffer.append( "\n" );
                                    buffer.append( "<NetTotal>" ).append( AGT_SAFT_LINE_NET_TOTAL ).append( "</NetTotal>" );
                                    buffer.append( "\n" );
                                    buffer.append( "<GrossTotal>" ).append( AGT_SAFT_LINE_GROSS_TOTAL ).append( "</GrossTotal>" );
                                    buffer.append( "\n" );
                                }
                                buffer.append( "</DocumentTotals>" );
                                buffer.append( "\n" );
                            }
                            buffer.append( "</WorkDocument>" );
                            buffer.append( "\n" );

                            buffer.append( "\n" );

                        }//FIM DO CICLO DAS PROFORMAS

                    }
                    buffer.append( "</WorkingDocuments>" );
                    buffer.append( "\n" );
                }

                if ( !Objects.isNull( list_recibo ) )
                {
                    //#RE  RECIBO
                    buffer.append( "<Payments>" );
                    {
                        AGT_SAFT_NUMBER_OF_ENTRIES = String.valueOf( list_recibo.size() );
                        AGT_SAFT_TOTAL_DEBIT = "0";
                        AGT_SAFT_TOTAL_CREDIT = getValorCasasDecimais( MetodosUtilitarios.getTotalSemIva( list_recibo, DOC_RECIBO_RC ), CASAS_DECIMAIS );

                        buffer.append( "\n" );
                        buffer.append( "<NumberOfEntries>" ).append( AGT_SAFT_NUMBER_OF_ENTRIES ).append( "</NumberOfEntries>" );
                        buffer.append( "\n" );
                        buffer.append( "<TotalDebit>" ).append( AGT_SAFT_TOTAL_DEBIT ).append( "</TotalDebit>" );
                        buffer.append( "\n" );
                        buffer.append( "<TotalCredit>" ).append( AGT_SAFT_TOTAL_CREDIT ).append( "</TotalCredit>" );
                        buffer.append( "\n" );

                        for ( TbVenda recibo : list_recibo )
                        {
//                            AGT_SAFT_PAYMENT_REF_NO = recibo.getCodFact();
                            AGT_SAFT_PAYMENT_REF_NO = recibo.getCodFact().replaceAll( "RE", "RC");
                            AGT_SAFT_PERIOD = String.valueOf( recibo.getDataVenda().getMonth() + 1 );
                            //o campo só é obrigatorio se aplicação for intergrada com um sistema de facturação.
                            AGT_SAFT_TRANSACTION_ID = "Desconhecido";
                            AGT_SAFT_TRANSACTION_DATE = formateDate( YYYY_MM_DD, recibo.getDataVenda() );;
                            AGT_SAFT_PAYMENT_TYPE = "RC";
                            AGT_SAFT_DESCRIPTION = "Sem descrição";
                            AGT_SAFT_SYSTEM_ID = String.valueOf( recibo.getCodigo() );
                            AGT_PAYMENT_STATUS = "N";
                            AGT_PAYMENT_STATUS_DATE = formateDate( YYYY_MM_DD_T_HH_MM_SS, recibo.getDataVenda() );
                            AGT_REASON = "";//NÃO OBRIGATORIO - OMITIDO NO SAFT POR ENQUANTO
                            AGT_PAYMENT_SOURCE_ID = recibo.getCodigoUsuario().getNome() + " " + recibo.getCodigoUsuario().getSobreNome();;
                            AGT_SOURCE_PAYMENT = "P";
                            AGT_SYSTEM_ENTRY_DATE = formateDate( YYYY_MM_DD_T_HH_MM_SS, recibo.getDataVenda() );
                            AGT_CUSTOMER_ID = String.valueOf( recibo.getCodigoCliente().getCodigo() );

                            double total_iva_sem_desconto = MetodosUtilitarios.getTotalVendaIVASemIncluirDesconto( recibo.getTbItemVendaList() );
                            AGT_TAX_PAYABLE = String.valueOf( getValorCasasDecimais( total_iva_sem_desconto, CASAS_DECIMAIS ) );
                           
                            double net_total = MetodosUtilitarios.getNetTotal( recibo.getTbItemVendaList() );
                            double gross_total = ( net_total + total_iva_sem_desconto );
                  
                            AGT_NET_TOTAL = String.valueOf( getValorCasasDecimais( net_total, CASAS_DECIMAIS ) );
                           
//                            AGT_GROSS_TOTAL = String.valueOf( getValorCasasDecimais( recibo.getTotalGeral() + total_iva_sem_desconto, CASAS_DECIMAIS ) );
                            AGT_GROSS_TOTAL = String.valueOf( getValorCasasDecimais( gross_total, CASAS_DECIMAIS ) );

                            AGT_SETTLEMENT_AMOUNT = String.valueOf( recibo.getDescontoTotal() );
                            AGT_SAFT_CURRENCY_CODE = "AOA";
                            AGT_CREDIT_AMOUNT = AGT_GROSS_TOTAL;
                            AGT_EXCHANGE_RATE = "14";
                            AGT_WITH_HOLDING_TAX_TYPE = "";//NÃO OBRIGATORIO - OMITIDO NO SAFT POR ENQUANTO
                            AGT_WITH_HOLDING_TAX_DESCIPTION = "";//NÃO OBRIGATORIO - OMITIDO NO SAFT POR ENQUANTO
                            AGT_WITH_HOLDING_TAX_AMOUNT = "";//NÃO OBRIGATORIO - OMITIDO NO SAFT POR ENQUANTO
                            //------------------------------------------------

                            buffer.append( "<Payment>" );
                            {
                                buffer.append( "\n" );
                                buffer.append( "<PaymentRefNo>" ).append( AGT_SAFT_PAYMENT_REF_NO ).append( "</PaymentRefNo>" );
                                buffer.append( "\n" );
                                buffer.append( "<Period>" ).append( AGT_SAFT_PERIOD ).append( "</Period>" );
                                buffer.append( "\n" );
                                buffer.append( "<TransactionDate>" ).append( AGT_SAFT_TRANSACTION_DATE ).append( "</TransactionDate>" );
                                buffer.append( "\n" );
                                buffer.append( "<PaymentType>" ).append( AGT_SAFT_PAYMENT_TYPE ).append( "</PaymentType>" );
                                buffer.append( "\n" );
                                buffer.append( "<Description>" ).append( AGT_SAFT_DESCRIPTION ).append( "</Description>" );
                                buffer.append( "\n" );
                                buffer.append( "<SystemID>" ).append( AGT_SAFT_SYSTEM_ID ).append( "</SystemID>" );
                                buffer.append( "\n" );
                                buffer.append( "<DocumentStatus>" );
                                {
                                    buffer.append( "\n" );
                                    buffer.append( "<PaymentStatus>" ).append( AGT_PAYMENT_STATUS ).append( "</PaymentStatus>" );
                                    buffer.append( "\n" );
                                    buffer.append( "<PaymentStatusDate>" ).append( AGT_PAYMENT_STATUS_DATE ).append( "</PaymentStatusDate>" );
                                    buffer.append( "\n" );
                                    buffer.append( "<SourceID>" ).append( AGT_PAYMENT_SOURCE_ID ).append( "</SourceID>" );
                                    buffer.append( "\n" );
                                    buffer.append( "<SourcePayment>" ).append( AGT_SOURCE_PAYMENT ).append( "</SourcePayment>" );
                                    buffer.append( "\n" );

                                }
                                buffer.append( "</DocumentStatus>" );
                                buffer.append( "\n" );
                                buffer.append( "<SourceID>" ).append( AGT_PAYMENT_SOURCE_ID ).append( "</SourceID>" );
                                buffer.append( "\n" );
                                buffer.append( "<SystemEntryDate>" ).append( AGT_SYSTEM_ENTRY_DATE ).append( "</SystemEntryDate>" );
                                buffer.append( "\n" );
                                buffer.append( "<CustomerID>" ).append( AGT_CUSTOMER_ID ).append( "</CustomerID>" );
                                linhas_recibo = recibo.getTbItemVendaList();
                                line_number = 0;
                                for ( TbItemVenda linha : linhas_recibo )
                                {

                                    ++line_number;
                                    AGT_LINE_NUMBER = "" + line_number;
                                    AGT_LINE_SOURCE_DOCUMENT_ID_ORIGINATING_ON = recibo.getRefCodFact().toUpperCase();
                                    AGT_LINE_SOURCE_DOCUMENT_INVOICE_DATE = formateDate( YYYY_MM_DD, recibo.getDataVenda() );
                                    AGT_LINE_SOURCE_DOCUMENT_DESCRIPTION = "";//NÃO OBRIGATORIO - OMITIDO NO SAFT POR ENQUANTO
                                    AGT_LINE_SATTLEMENT_AMOUNT = String.valueOf( linha.getDesconto() );
                                    AGT_LINE_DEBIT_AMOUNT = "0.0";
                                    double pu = linha.getFkPreco().getPrecoVenda().doubleValue() * linha.getQuantidade();
                                    AGT_LINE_CREDIT_AMOUNT = String.valueOf( getValorCasasDecimais( pu, CASAS_DECIMAIS ) );
                                    AGT_LINE_TAX_TYPE = "IVA";
                                    AGT_LINE_TAX_COUNTRY_REGION = "AO";
                                    AGT_LINE_TAX_CODE = "NOR";
                                    AGT_LINE_TAX_PERCENTAGE = String.valueOf( linha.getValorIva() );
                                    AGT_LINE_TAX_AMOUNT = "";//NÃO OBRIGATORIO - OMITIDO NO SAFT POR ENQUANTO
                                    AGT_LINE_TAX_EXCEPTION_REASON = MetodosUtilitarios.getMotivoIsensao( linha.getCodigoProduto().getCodigo() );
                                    AGT_LINE_TAX_EXCEPTION_CODE = MetodosUtilitarios.getCodigoRegime( linha.getCodigoProduto().getCodigo() );

                                    buffer.append( "<Line>" );
                                    {
                                        buffer.append( "<LineNumber>" ).append( AGT_LINE_NUMBER ).append( "</LineNumber>" );
                                        buffer.append( "<SourceDocumentID>" );
                                        {
                                            buffer.append( "<OriginatingON>" ).append( AGT_LINE_SOURCE_DOCUMENT_ID_ORIGINATING_ON ).append( "</OriginatingON>" );
                                            buffer.append( "<InvoiceDate>" ).append( AGT_LINE_SOURCE_DOCUMENT_INVOICE_DATE ).append( "</InvoiceDate>" );

                                        }
                                        buffer.append( "</SourceDocumentID>" );
                                        buffer.append( "<SettlementAmount>" ).append( AGT_LINE_SATTLEMENT_AMOUNT ).append( "</SettlementAmount>" );
                                        buffer.append( "<CreditAmount>" ).append( AGT_LINE_CREDIT_AMOUNT ).append( "</CreditAmount>" );

                                        buffer.append( "<Tax>" );
                                        {
                                            buffer.append( "<TaxType>" ).append( AGT_LINE_TAX_TYPE ).append( "</TaxType>" );
                                            buffer.append( "<TaxCountryRegion>" ).append( AGT_LINE_TAX_COUNTRY_REGION ).append( "</TaxCountryRegion>" );
                                            buffer.append( "<TaxCode>" ).append( AGT_LINE_TAX_CODE ).append( "</TaxCode>" );
                                            buffer.append( "<TaxPercentage>" ).append( AGT_LINE_TAX_PERCENTAGE ).append( "</TaxPercentage>" );
                                        }
                                        buffer.append( "</Tax>" );
                                        buffer.append( "\n" );
                                        if ( linha.getValorIva() == 0 )
                                        {
                                            buffer.append( "<TaxExemptionReason>" ).append( AGT_LINE_TAX_EXCEPTION_REASON ).append( "</TaxExemptionReason>" );
                                            buffer.append( "<TaxExemptionCode>" ).append( AGT_LINE_TAX_EXCEPTION_CODE ).append( "</TaxExemptionCode>" );
                                        }
                                        buffer.append( "\n" );
                                    }
                                    buffer.append( "</Line>" );
                                }

                                buffer.append( "\n" );
                                buffer.append( "<DocumentTotals>" );
                                {
                                    buffer.append( "<TaxPayable>" ).append( AGT_TAX_PAYABLE ).append( "</TaxPayable>" );
                                    buffer.append( "\n" );
                                    buffer.append( "<NetTotal>" ).append( AGT_NET_TOTAL ).append( "</NetTotal>" );
                                    buffer.append( "\n" );
                                    buffer.append( "<GrossTotal>" ).append( AGT_GROSS_TOTAL ).append( "</GrossTotal>" );
                                    buffer.append( "\n" );
                                    buffer.append( "<Settlement>" );
                                    {
                                        buffer.append( "\n" );
                                        buffer.append( "<SettlementAmount>" ).append( AGT_SETTLEMENT_AMOUNT ).append( "</SettlementAmount>" );
                                        buffer.append( "\n" );
                                    }
                                    buffer.append( "</Settlement>" );
                                    buffer.append( "\n" );
                                    buffer.append( "<Currency>" );
                                    {
                                        buffer.append( "\n" );
                                        buffer.append( "<CurrencyCode>" ).append( AGT_SAFT_CURRENCY_CODE ).append( "</CurrencyCode>" );
                                        buffer.append( "\n" );
                                        buffer.append( "<CurrencyAmount>" ).append( AGT_CREDIT_AMOUNT ).append( "</CurrencyAmount>" );
                                        buffer.append( "\n" );
                                        buffer.append( "<ExchangeRate>" ).append( AGT_EXCHANGE_RATE ).append( "</ExchangeRate>" );
                                    }
                                    buffer.append( "</Currency>" );
                                    buffer.append( "\n" );

                                }
                                buffer.append( "</DocumentTotals>" );
                            }
                            buffer.append( "</Payment>" );
                        }

                    }
                    buffer.append( "</Payments>" );
                    buffer.append( "\n" );

                }

            }

            buffer.append( "</SourceDocuments>" );
            buffer.append( "\n" );
        }
        buffer.append( "</AuditFile>" );

        escreverNoDocumento( buffer.toString(), ficheiroSAFT );

    }

    private List<NotasItem> getConvertVendasToNotas( TbVenda documentoOrigem )
    {
        System.err.println( "Venda: " + documentoOrigem );
        List<NotasItem> lista_nota = new ArrayList<>();
        List<TbItemVenda> lista_item = documentoOrigem.getTbItemVendaList();
        NotasItem notasItem;
        for ( TbItemVenda linha : lista_item )
        {
            notasItem = new NotasItem();

            notasItem.setFkProduto( linha.getCodigoProduto() );
            notasItem.setFkNota( null );
            notasItem.setQuantidade( ( double ) linha.getQuantidade() );
            notasItem.setDesconto( linha.getDesconto() );
            notasItem.setValorIva( linha.getValorIva() );
            notasItem.setMotivoIsensao( linha.getMotivoIsensao() );
            notasItem.setTotal( new BigDecimal(linha.getTotal().doubleValue() ));
            notasItem.setFkPreco( linha.getFkPreco() );

            lista_nota.add( notasItem );

        }

        return lista_nota;

    }

    private boolean existe_registro()
    {
        Date data_inicio = saftDataMinimaJDateChoose.getDate();
        Date data_fim = saftDataMaximaJDateChooser.getDate();

        if ( saftDataMinimaJDateChoose.getCalendar() == null )
        {
            JOptionPane.showMessageDialog( null, "Por favor seleccione a data do inicio", "Aviso", JOptionPane.WARNING_MESSAGE );
            return false;
        }
        else if ( saftDataMaximaJDateChooser.getCalendar() == null )
        {
            JOptionPane.showMessageDialog( null, "Por favor seleccione a data do fim", "Aviso", JOptionPane.WARNING_MESSAGE );
            return false;
        }
        else if ( vendaDao.getAllVendaExceptoFacturaProformaAndReciboAlterado( data_inicio, data_fim ) == ( null ) )
        {
            JOptionPane.showMessageDialog( null, "Não existe registro de documentos, impossivel gerar o SAFT.", "Erro", JOptionPane.WARNING_MESSAGE );
            return false;
        }
        return true;

    }

    private List<TbProduto> lista_produto_filtrado( List<TbProduto> list_produto_vendas, List<TbProduto> list_produto_notas )
    {

        List<TbProduto> list_produto_local = list_produto_vendas;
        boolean existe;

        for ( TbProduto produto_nota : list_produto_notas )
        {
            System.out.println( "NOTA PRODUTO: " + produto_nota.getDesignacao() );
            existe = false;
            for ( TbProduto produto_venda : list_produto_vendas )
            {

                if ( Objects.equals( produto_nota.getCodigo(), produto_venda.getCodigo() ) )
                {
                    System.out.println( "EXISTE" );
                    existe = true;
                    break;
                }

            }

            if ( !existe )
            {
                list_produto_local.add( produto_nota );
            }

        }

        return list_produto_local;

    }

    private List<TbCliente> lista_cliente_filtrado( List<TbCliente> list_cliente_a, List<TbCliente> list_cliente_b )
    {

        List<TbCliente> list_cliente_local = list_cliente_a;
        boolean existe;

        for ( TbCliente cliente_nota : list_cliente_b )
        {
            System.out.println( "NOTA CLIENTE: " + cliente_nota.getNome() );
            existe = false;
            for ( TbCliente cliente_venda : list_cliente_a )
            {

                if ( Objects.equals( cliente_nota.getCodigo(), cliente_venda.getCodigo() ) )
                {
                    System.out.println( "EXISTE" );
                    existe = true;
                    break;
                }

            }

            if ( !existe )
            {
                list_cliente_local.add( cliente_nota );
            }

        }

        return list_cliente_local;

    }

    private String getNifCliente( TbCliente cliente_local )
    {
        if ( !Objects.isNull( cliente_local ) )
        {
            try
            {
                Long.parseLong( cliente_local.getNif() );
                return cliente_local.getNif();
            }
            catch ( Exception e )
            {
                return DVML.NUMBER_NIF_GENERICO;
            }
        }
        return DVML.NUMBER_NIF_GENERICO;
    }

    private String getStringValida( String valor )
    {
        String valor_valido = valor;
        valor_valido = valor_valido
                .replaceAll( "&", " e " )
                .replaceAll( "/", " " )
                .replaceAll( "-", " " )
                .replaceAll( "-", " " )
                .replaceAll( "ª", " " )
                .replaceAll( "º", " " );
        String nfdNormalizedString = Normalizer.normalize( valor_valido, Normalizer.Form.NFD );
        Pattern pattern = Pattern.compile( "\\p{InCombiningDiacriticalMarks}+" );
        return pattern.matcher( nfdNormalizedString ).replaceAll( "" );

    }

}
