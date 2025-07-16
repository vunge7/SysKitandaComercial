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

import comercial.controller.ComprasController;
import comercial.controller.DocumentosController;
import comercial.controller.FornecedoresController;
import comercial.controller.ItemComprasController;
import comercial.controller.UsuariosController;
import dao.AnoEconomicoDao;
import dao.DadosInstituicaoDao;
import entity.Compras;
import entity.Documento;
import entity.ItemCompras;
import entity.NotasItem;
import entity.TbCliente;
import entity.TbDadosInstituicao;
import entity.TbFornecedor;
import entity.TbItemVenda;
import entity.TbProduto;
import entity.TbUsuario;
import entity.TbVenda;
import hotel.controller.NotaController;
import java.awt.Desktop;
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
import kitanda.util.CfConstantes;
import static kitanda.util.CfConstantes.YYYY_MM_DD;
import kitanda.util.CfMethods;
import util.BDConexao;
import util.DVML;
import static util.DVML.*;
import util.JPAEntityMannagerFactoryUtil;
import util.MetodosUtil;
import static util.MetodosUtil.*;

/**
 *
 * @author Domingos Dala Vunge
 */
public class FicheiroSAFTCompraVisao extends javax.swing.JFrame
{

    /**
     * Creates new form ProvinciaVisao
     */
    private EntityManagerFactory emf = JPAEntityMannagerFactoryUtil.em;
    private DadosInstituicaoDao dadosInstituicaoDao = new DadosInstituicaoDao( emf );
    private AnoEconomicoDao anoEconomicoDao = new AnoEconomicoDao( emf );
    private FornecedoresController fornecedoresController;
    private DocumentosController documentosController;
    private ComprasController comprasController;
    private UsuariosController usuariosController;
    private ItemComprasController itemComprasController;
    private List<TbFornecedor> list_fornecedores = null;
    private List<Compras> list_compras = null;
    private List<ItemCompras> lista_itens_compra = null;

    private BDConexao conexao;
    private int line_number = 0;

    public FicheiroSAFTCompraVisao( BDConexao conexao )
    {

        initComponents();
        setLocationRelativeTo( null );
        this.conexao = conexao;

        popularComponentes();
        fornecedoresController = new FornecedoresController( conexao );
        comprasController = new ComprasController( conexao );
        usuariosController = new UsuariosController( conexao );
        documentosController = new DocumentosController( conexao );
        itemComprasController = new ItemComprasController( conexao );

        saftDataMinimaJDateChoose.setDate( new Date() );
        saftDataMaximaJDateChooser.setDate( new Date() );
    }

    @SuppressWarnings("unchecked")
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
        jRadioButton1.setText("Compra");

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
                .addContainerGap(151, Short.MAX_VALUE))
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
                .addGap(126, 126, 126)
                .addComponent(btnSalvar, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                    .addComponent(btnCancelar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnSalvar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(12, 12, 12))
        );

        jPanel7.setBackground(new java.awt.Color(0, 102, 102));

        jLabel19.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(204, 204, 204));
        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel19.setText("GERAR FICHEIRO SAFT DE COMPRAS - AO");

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

        if ( true )
        {
//            btnSalvar.setEnabled( false );
            actualizar_hash();
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
        java.awt.EventQueue.invokeLater( new Runnable()
        {
            public void run()
            {
                new FicheiroSAFTCompraVisao( new BDConexao() ).setVisible( true );
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

    public void actualizar_hash()
    {
        list_compras = comprasController.listComprasByDates( saftDataMinimaJDateChoose.getDate(), saftDataMaximaJDateChooser.getDate() );
        comprasController.actualizar_hash( list_compras, itemComprasController, conexao );
    }

    private void gerarFcheiroSAFT()
    {
        //#TOP
        int numero_linha;
        List<TbItemVenda> linhas_recibo;
        File directorio = new File( CAMINHO_SAFT );
        File ficheiroSAFT = new File( CAMINHO_SAFT + "/" + FICHEIRO_SAFT_COMPRA );

        list_fornecedores = fornecedoresController.listarTodos();
        list_compras = comprasController.listComprasByDates( saftDataMinimaJDateChoose.getDate(), saftDataMaximaJDateChooser.getDate() );

        //CRIAR CAMINHO DO DIRECTORIO DO FICHEIRO SAFT CASO NÃO EXISTAM
        directorio.mkdirs();

        //ELIMINAR O FICHEIRO SAFT CASO EXISTA
        if ( ficheiroSAFT.exists() )
        {
            ficheiroSAFT.delete();
        }

        StringBuilder buffer = new StringBuilder();
        //ADICIONAR LINHAS AO FICHEIRO SAFT
        buffer.append( "<?xml version=\"1.0\" encoding=\"windows-1252\"?>" );
        buffer.append( "\n" );
        buffer.append( "<AuditFile  xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"urn:OECD:StandardAuditFile-Tax:AO_1.01_01 SAFTAO1.01_01.xsd\" xmlns=\"urn:OECD:StandardAuditFile-Tax:AO_1.01_01\">" );
        {
            buffer.append( "\n" );
            //CABEÇALHO
            buffer.append( criar_header() );
            buffer.append( criar_master_files() );
            buffer.append( criar_source_documents() ); //4.5  Documentos Comerciais do Fornecedor

        }
        buffer.append( "</AuditFile>" );

        escreverNoDocumento( buffer.toString(), ficheiroSAFT );

        try
        {
            Desktop.getDesktop().open( new File( CAMINHO_SAFT ) );
        }
        catch ( IOException e )
        {
        }

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
            notasItem.setTotal( new BigDecimal( linha.getTotal().doubleValue() ) );
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
//        else if ( vendaDao.getAllVendaExceptoFacturaProformaAndReciboAlterado( data_inicio, data_fim ) == ( null ) )
//        {
//            JOptionPane.showMessageDialog( null, "Não existe registro de documentos, impossivel gerar o SAFT.", "Erro", JOptionPane.WARNING_MESSAGE );
//            return false;
//        }
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

    private String getNifFornecedor( TbFornecedor fornecedor_local )
    {
        if ( !Objects.isNull( fornecedor_local ) )
        {
            try
            {
                Long.parseLong( fornecedor_local.getNif() );
                return fornecedor_local.getNif();
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

    private StringBuilder criar_header()
    {
        StringBuilder buffer = new StringBuilder();

        TbDadosInstituicao dadosDadosInstituicao = dadosInstituicaoDao.findTbDadosInstituicao( 1 );

        /*Prencher as constantes do Header*/
        AGT_SAFT_COMPANY_ID = dadosDadosInstituicao.getNif();
        AGT_SAFT_TAX_REGISTRATION_NUMBER = dadosDadosInstituicao.getNif();
        AGT_SAFT_TAX_ACCOUNTING_BASIS = "A"; //Aquisição de Bens e Serviços.
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

        return buffer;
    }

    private StringBuilder criar_master_files()
    {

        StringBuilder buffer = new StringBuilder();

        //REGISTROS RELACIONADOS AS FATURAS
        buffer.append( "<MasterFiles>" ); // 2. Tabela Mestre
        {
            buffer.append( criar_supplier() ); // 2.3 fornecedores
            buffer.append( criar_tax_table() ); //2.5  impostos

        }
        buffer.append( "</MasterFiles>" );
        buffer.append( "\n" );

        return buffer;

    }

    private StringBuilder criar_supplier()
    {
        StringBuilder buffer = new StringBuilder();

        //INICIO CICLO DOS FORNECEDORES
        for ( TbFornecedor fornecedor : list_fornecedores )
        {

            buffer.append( "<Supplier>" );
            {
                AGT_SAFT_SUPPLIER_ID = String.valueOf( fornecedor.getCodigo() );
                AGT_SAFT_SUPPLIER_ACCOUNT_ID = "Desconhecido";
                AGT_SAFT_SUPPLIER_TAX_ID = getNifFornecedor( fornecedor );
                AGT_SAFT_SUPPLIER_COMPANY_NAME = getStringValida( fornecedor.getNome() );
                AGT_SAFT_SUPPLIER_CONTACT = getStringValida( fornecedor.getTelefone() );
                AGT_SAFT_SUPPLIER_BULLING_ADDRESS_NUMBER = "";
                AGT_SAFT_SUPPLIER_BULLING_ADDRESS_STREET_NAME = "";

                String morada_cliente = ( !fornecedor.getEndereco().equals( "" ) ? fornecedor.getEndereco() : "n/a" );
                AGT_SAFT_SUPPLIER_BULLING_ADDRESS_DETAIL = getStringValida( morada_cliente );
                AGT_SAFT_SUPPLIER_BULLING_ADDRESS_CITY = getStringValida( "Luanda" );
                AGT_SAFT_SUPPLIER_BULLING_ADDRESS_POSTAL_CODE = "";
                AGT_SAFT_SUPPLIER_BULLING_ADDRESS_PROVINCE = "";
                AGT_SAFT_SUPPLIER_BULLING_ADDRESS_COUNTRY = "AO";
                AGT_SAFT_SUPPLIER_TELEPHONE = "";
                AGT_SAFT_SUPPLIER_FAX = "";
                AGT_SAFT_SUPPLIER_EMAIL = "";
                AGT_SAFT_SUPPLIER_SELFBELLIN_INDICATOR = "0";

                buffer.append( "\n" );
                buffer.append( "<SupplierID>" ).append( AGT_SAFT_SUPPLIER_ID ).append( "</SupplierID>" );
                buffer.append( "\n" );
                buffer.append( "<AccountID>" ).append( AGT_SAFT_SUPPLIER_ACCOUNT_ID ).append( "</AccountID>" );
                buffer.append( "\n" );
                buffer.append( "<SupplierTaxID>" ).append( AGT_SAFT_SUPPLIER_TAX_ID ).append( "</SupplierTaxID>" );
                buffer.append( "\n" );
                buffer.append( "<CompanyName>" ).append( AGT_SAFT_SUPPLIER_COMPANY_NAME ).append( "</CompanyName>" );
                buffer.append( "<Contact>" ).append( AGT_SAFT_SUPPLIER_CONTACT ).append( "</Contact>" );

                buffer.append( "\n" );
                buffer.append( "<BillingAddress>" );
                {
                    buffer.append( "\n" );
                    buffer.append( "<BuildingNumber>" ).append( AGT_SAFT_SUPPLIER_BULLING_ADDRESS_NUMBER ).append( "</BuildingNumber>" );
                    buffer.append( "\n" );
                    buffer.append( "<StreetName>" ).append( AGT_SAFT_SUPPLIER_BULLING_ADDRESS_STREET_NAME ).append( "</StreetName>" );
                    buffer.append( "\n" );
                    buffer.append( "<AddressDetail>" ).append( AGT_SAFT_SUPPLIER_BULLING_ADDRESS_DETAIL ).append( "</AddressDetail>" );
                    buffer.append( "\n" );
                    buffer.append( "<City>" ).append( AGT_SAFT_SUPPLIER_BULLING_ADDRESS_CITY ).append( "</City>" );
                    buffer.append( "\n" );
                    buffer.append( "<Province>" ).append( AGT_SAFT_SUPPLIER_BULLING_ADDRESS_PROVINCE ).append( "</Province>" );
                    buffer.append( "\n" );
//                    buffer.append( "<PostalCode>" ).append( AGT_SAFT_SUPPLIER_BULLING_ADDRESS_POSTAL_CODE ).append( "</PostalCode>" );
//                    buffer.append( "\n" );
                    buffer.append( "<Country>" ).append( AGT_SAFT_SUPPLIER_BULLING_ADDRESS_COUNTRY ).append( "</Country>" );
                    buffer.append( "\n" );
                }
                buffer.append( "</BillingAddress>" );

                buffer.append( "\n" );
                buffer.append( "<Telephone>" ).append( AGT_SAFT_SUPPLIER_TELEPHONE ).append( "</Telephone>" );
                buffer.append( "\n" );
                buffer.append( "<Fax>" ).append( AGT_SAFT_SUPPLIER_FAX ).append( "</Fax>" );
                buffer.append( "\n" );
                buffer.append( "<Email>" ).append( AGT_SAFT_SUPPLIER_EMAIL ).append( "</Email>" );
                buffer.append( "\n" );
                buffer.append( "<SelfBillingIndicator>" ).append( AGT_SAFT_SELF_BILLING_INDICATOR ).append( "</SelfBillingIndicator>" );
                buffer.append( "\n" );
            }
            buffer.append( "</Supplier>" );

        }
        //FIM CICLO DOS FORNECEDORES

        return buffer;
    }

    private StringBuilder criar_tax_table()
    {
        StringBuilder buffer = new StringBuilder();
        //TABELAS DE IMPOSTOS - EX: ISENTO DE IMPOSTOS E COM IVA
        buffer.append( "<TaxTable>" );
        {
            AGT_SAFT_TAX_TYPE = "IVA";
            AGT_SAFT_TAX_CODE = "NOR";
            AGT_SAFT_TAX_COUNTRY_REGION = "AO";
            AGT_SAFT_TAX_DESCRIPTION = getStringValida( "IMPOSTO SOBRE VALOR ACRESCENTADO" );
//                    AGT_SAFT_TAX_PERCENTAGE = "14";
//                    AGT_SAFT_TAX_AMOUNT = "14";
            AGT_SAFT_TAX_PERCENTAGE = "7";
            AGT_SAFT_TAX_AMOUNT = "7";

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
//                        buffer.append("\n");
//                        buffer.append("<TaxAmount>").append(AGT_SAFT_TAX_AMOUNT).append("</TaxAmount>");
//                        buffer.append("\n");
            }
            buffer.append( "</TaxTableEntry>" );
            buffer.append( "\n" );
        }
        buffer.append( "</TaxTable>" );
        buffer.append( "\n" );

        return buffer;
    }

    private StringBuilder criar_source_documents()
    {
        StringBuilder buffer = new StringBuilder();

        //REGISTROS RELACIONADOS AS FATURAS
        buffer.append( "<SourceDocuments>" ); // 1. Tabela Mestre
        {
            buffer.append( criar_purchase_invoices() ); // 2.3. fornecedores
        }
        buffer.append( "</SourceDocuments>" );
        buffer.append( "\n" );

        return buffer;
    }

    private StringBuilder criar_purchase_invoices()
    {
        StringBuilder buffer = new StringBuilder();

        //REGISTROS RELACIONADOS AS FATURAS
        buffer.append( "<PurchaseInvoices>" ); // 4.5. Documentos Comerciais do Fornecedores
        {

            AGT_SAFT_PURCHASE_INVOICES_NUMBER_OF_ENTRIES = String.valueOf( list_compras.size() );

            buffer.append( "\n" );//4.5.1.
            buffer.append( "<NumberOfEntries>" ).append( AGT_SAFT_PURCHASE_INVOICES_NUMBER_OF_ENTRIES ).append( "</NumberOfEntries>" );
            buffer.append( "\n" );

            for ( Compras compra : list_compras )
            {

                int idUsuario = compra.getCodigoUsuario().getCodigo();
                TbUsuario usuario = ( TbUsuario ) usuariosController.findById( idUsuario );
                int idDocumento = compra.getFkDocumento().getPkDocumento();
                Documento documento = ( Documento ) documentosController.findById( idDocumento );
                int idFornecedor = compra.getFkFornecedor().getCodigo();
                TbFornecedor fornecedor = ( TbFornecedor ) fornecedoresController.findById( idFornecedor );

                lista_itens_compra = itemComprasController.listarTodosByIdCompra( compra.getPkCompra() );

                BigDecimal tax_payble = MetodosUtil.getTotalVendaIVASemIncluirDescontoCompra( lista_itens_compra );
                BigDecimal net_total = MetodosUtil.getNetTotalCompra( lista_itens_compra );
                BigDecimal gross_total = MetodosUtil.getGrossTotalCompra( lista_itens_compra );
                BigDecimal tax_base = net_total;
                BigDecimal deditable_amount = new BigDecimal( 0 );
                double deditbale_percentagem = 0;
                String dedutible_tax_type = "OBC";
                String witholding_tax_type = "IVA";
                String witholding_tax_description = "0";
                String witholding_tax_amount = String.valueOf( deditable_amount );

                buffer.append( "\n" );//
                AGT_SAFT_PURCHASE_INVOICES_INVOICE_NO = String.valueOf( compra.getCodFact().replaceAll( "CO", "FT" ) );
                AGT_SAFT_PURCHASE_INVOICES_INVOICE_HASH = String.valueOf( compra.getHashCod() );
                AGT_SAFT_PURCHASE_INVOICES_INVOICE_SOURCE_ID = String.valueOf( usuario.getNome() );
                AGT_SAFT_PURCHASE_INVOICES_INVOICE_PERIOD = String.valueOf( compra.getDataCompra().getMonth() + 1 );
                AGT_SAFT_PURCHASE_INVOICES_INVOICE_DATE = String.valueOf( formateDate( YYYY_MM_DD, compra.getDataCompra() ) );
                AGT_SAFT_PURCHASE_INVOICES_INVOICE_PURCHASE_TYPE = "FT";
                AGT_SAFT_PURCHASE_INVOICES_INVOICE_SUPPLIER_ID = fornecedor.getNif();
                AGT_SAFT_PURCHASE_INVOICES_INVOICE_DOCUMENTS_TOTAL_TAX_PAYBLE = String.valueOf( tax_payble );
                AGT_SAFT_PURCHASE_INVOICES_INVOICE_DOCUMENTS_TOTAL_NET_TOTAL = String.valueOf( net_total );
                AGT_SAFT_PURCHASE_INVOICES_INVOICE_DOCUMENTS_TOTAL_GROSS_TOTAL = String.valueOf( gross_total );
                AGT_SAFT_PURCHASE_INVOICES_INVOICE_DOCUMENTS_TOTAL_DEDUCTIBLE_TAX_BASE = String.valueOf( tax_base );
                AGT_SAFT_PURCHASE_INVOICES_INVOICE_DOCUMENTS_TOTAL_DEDUCTIBLE_DEDUTABLE_AMOUNT = String.valueOf( deditable_amount );
                AGT_SAFT_PURCHASE_INVOICES_INVOICE_DOCUMENTS_TOTAL_DEDUCTIBLE_DEDUTABLE_PERCENTAGE = String.valueOf( deditbale_percentagem );
                AGT_SAFT_PURCHASE_INVOICES_INVOICE_DOCUMENTS_TOTAL_DEDUCTIBLE_DEDUTABLE_TAX_TYPE = dedutible_tax_type;
                AGT_SAFT_PURCHASE_INVOICES_INVOICE_DOCUMENTS_TOTAL_MOEDA_CURRENCY_CODDE = "";
                AGT_SAFT_PURCHASE_INVOICES_INVOICE_DOCUMENTS_TOTAL_MOEDA_CURRENCY_AMOUNT = "";
                AGT_SAFT_PURCHASE_INVOICES_INVOICE_DOCUMENTS_TOTAL_MOEDA_EXCHANCE_RATE = "";
                AGT_SAFT_PURCHASE_INVOICES_INVOICE_WITHOLDING_TAX_WITHOLDING_TAX_TYPE = witholding_tax_type;
                AGT_SAFT_PURCHASE_INVOICES_INVOICE_WITHOLDING_TAX_WITHOLDING_TAX_DESCRIPTION = witholding_tax_description;
                AGT_SAFT_PURCHASE_INVOICES_INVOICE_WITHOLDING_TAX_WITHOLDING_TAX_AMOUNT = witholding_tax_amount;

                buffer.append( "<Invoice>" );
                {
                    buffer.append( "\n" );//5.4.2.1.
                    buffer.append( "<InvoiceNo>" ).append( AGT_SAFT_PURCHASE_INVOICES_INVOICE_NO ).append( "</InvoiceNo>" );
                    buffer.append( "\n" );//4.5.2.3.
                    buffer.append( "<Hash>" ).append( AGT_SAFT_PURCHASE_INVOICES_INVOICE_HASH ).append( "</Hash>" );
                    buffer.append( "\n" );//4.5.2.4.
                    buffer.append( "<SourceID>" ).append( AGT_SAFT_PURCHASE_INVOICES_INVOICE_SOURCE_ID ).append( "</SourceID>" );
                    buffer.append( "\n" );//4.5.2.5.
                    buffer.append( "<Period>" ).append( AGT_SAFT_PURCHASE_INVOICES_INVOICE_PERIOD ).append( "</Period>" );
                    buffer.append( "\n" );//4.5.2.6.
                    buffer.append( "<InvoiceDate>" ).append( AGT_SAFT_PURCHASE_INVOICES_INVOICE_DATE ).append( "</InvoiceDate>" );
                    buffer.append( "\n" );//4.5.2.7.
                    buffer.append( "<PurchaseType>" ).append( AGT_SAFT_PURCHASE_INVOICES_INVOICE_PURCHASE_TYPE ).append( "</PurchaseType>" );
                    buffer.append( "\n" );//4.5.2.8.
                    buffer.append( "<SupplierID>" ).append( AGT_SAFT_PURCHASE_INVOICES_INVOICE_SUPPLIER_ID ).append( "</SupplierID>" );
                    buffer.append( "\n" );//4.5.2.9.
                    buffer.append( "<DocumentTotals>" );
                    {
                        buffer.append( "\n" );//4.5.2.9.1
                        buffer.append( "<TaxPayable>" ).append( AGT_SAFT_PURCHASE_INVOICES_INVOICE_DOCUMENTS_TOTAL_TAX_PAYBLE ).append( "</TaxPayable>" );
                        buffer.append( "\n" );//4.5.2.9.2
                        buffer.append( "<NetTotal>" ).append( AGT_SAFT_PURCHASE_INVOICES_INVOICE_DOCUMENTS_TOTAL_NET_TOTAL ).append( "</NetTotal>" );
                        buffer.append( "\n" );//4.5.2.9.3
                        buffer.append( "<GrossTotal>" ).append( AGT_SAFT_PURCHASE_INVOICES_INVOICE_DOCUMENTS_TOTAL_GROSS_TOTAL ).append( "</GrossTotal>" );

                        buffer.append( "\n" );//4.5.2.9.4
                        buffer.append( "<Deductible>" );
                        {
                            buffer.append( "\n" );//4.5.2.9.4.1
                            buffer.append( "<TaxBase>" ).append( AGT_SAFT_PURCHASE_INVOICES_INVOICE_DOCUMENTS_TOTAL_DEDUCTIBLE_TAX_BASE ).append( "</TaxBase>" );
                            buffer.append( "\n" );//4.5.2.9.4.2
                            buffer.append( "<DeductibleAmount>" ).append( AGT_SAFT_PURCHASE_INVOICES_INVOICE_DOCUMENTS_TOTAL_DEDUCTIBLE_DEDUTABLE_AMOUNT ).append( "</DeductibleAmount>" );
                            buffer.append( "\n" );//4.5.2.9.4.3
                            buffer.append( "<DeductiblePercentage>" ).append( AGT_SAFT_PURCHASE_INVOICES_INVOICE_DOCUMENTS_TOTAL_DEDUCTIBLE_DEDUTABLE_PERCENTAGE ).append( "</DeductiblePercentage>" );
                            buffer.append( "\n" );//4.5.2.9.4.4
                            buffer.append( "<DeductibleTaxType>" ).append( AGT_SAFT_PURCHASE_INVOICES_INVOICE_DOCUMENTS_TOTAL_DEDUCTIBLE_DEDUTABLE_TAX_TYPE ).append( "</DeductibleTaxType>" );
                        }
                        buffer.append( "</Deductible>" );

//                        buffer.append( "\n" );//4.5.2.9.5
//                        buffer.append( "<Moeda>" );
//                        {
//                            buffer.append( "\n" );//4.5.2.9.5.1
//                            buffer.append( "<CurrencyCode>" ).append( AGT_SAFT_PURCHASE_INVOICES_INVOICE_DOCUMENTS_TOTAL_MOEDA_CURRENCY_CODDE ).append( "</CurrencyCode>" );
//                            buffer.append( "\n" );//4.5.2.9.5.2
//                            buffer.append( "<CurrencyAmount>" ).append( AGT_SAFT_PURCHASE_INVOICES_INVOICE_DOCUMENTS_TOTAL_MOEDA_CURRENCY_AMOUNT ).append( "</CurrencyAmount>" );
//                            buffer.append( "\n" );//4.5.2.9.5.3
//                            buffer.append( "<ExchangeRate>" ).append( AGT_SAFT_PURCHASE_INVOICES_INVOICE_DOCUMENTS_TOTAL_MOEDA_EXCHANCE_RATE ).append( "</ExchangeRate>" );
//                        }
//                        buffer.append( "</Moeda>" );
//                        buffer.append( "\n" );
                    }
                    buffer.append( "</DocumentTotals>" );

                    buffer.append( "\n" );//4.5.2.10
                    buffer.append( "<WithholdingTax>" );
                    {
                        buffer.append( "\n" );//4.5.2.10.1
                        buffer.append( "<WithholdingTaxType>" ).append( AGT_SAFT_PURCHASE_INVOICES_INVOICE_WITHOLDING_TAX_WITHOLDING_TAX_TYPE ).append( "</WithholdingTaxType>" );
                        buffer.append( "\n" );//4.5.2.10.2
                        buffer.append( "<WithholdingTaxDescription>" ).append( AGT_SAFT_PURCHASE_INVOICES_INVOICE_WITHOLDING_TAX_WITHOLDING_TAX_DESCRIPTION ).append( "</WithholdingTaxDescription>" );
                        buffer.append( "\n" );//4.5.2.10.3
                        buffer.append( "<WithholdingTaxAmount>" ).append( AGT_SAFT_PURCHASE_INVOICES_INVOICE_WITHOLDING_TAX_WITHOLDING_TAX_AMOUNT ).append( "</WithholdingTaxAmount>" );
                    }
                    buffer.append( "</WithholdingTax>" );
                    buffer.append( "\n" );

                }
                buffer.append( "</Invoice>" );
                buffer.append( "\n" );

            }

        }
        buffer.append( "</PurchaseInvoices>" );
        buffer.append( "\n" );

        return buffer;
    }

}
