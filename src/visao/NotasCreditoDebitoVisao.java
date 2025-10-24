/*

 2259 - ImprimirNota
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package visao;


import java.sql.Connection;
import controller.ItemVendaController;
import controller.ProdutoController;
import controller.StockController;
import controller.TipoClienteController;
import controller.VendaController;
import dao.*;
import entity.*;
import exemplos.PermitirNumeros;
import java.awt.Color;
import java.awt.event.*;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import kitanda.util.CfMethods;
import static kitanda.util.CfMethodsSwing.*;
import kitanda.util.CfMethodsSwing;
import lista.ListaNotaDebito;
import modelo.ClienteModelo;
import modelo.ItemVendaModelo;
import modelo.ProdutoModelo;
import modelo.StockModelo;
import modelo.TipoClienteModelo;
import modelo.VendaModelo;
import util.BDConexao;
import static util.DVML.*;
import util.DVML;
import static util.DVML.Abreviacao.*;
import util.DVML.Abreviacao;
import util.DVML.ESTADO_NOTA;
import util.JPAEntityMannagerFactoryUtil;
import util.MetodosUtil;
import static visao.NotasCreditoDebitoVisao.EVENTO_DO_SISTEMA.*;

/**
 *
 * @author Domingos Dala Vunge
 */
public class NotasCreditoDebitoVisao extends javax.swing.JFrame implements Runnable
{

    private static EntityManagerFactory emf = JPAEntityMannagerFactoryUtil.em;
    private VasilhameDao vasilhameDao;
    private static DescontoDao descontoDao;
    private static PrecoDao precoDao;
    private BancoDao bancoDao;
    private NotasItem notasItem;
    private TbStock stock_local;
    private Notas notas;
    public static ProdutoDao produtoDao;
    private static StockDao stockDao;
    private UsuarioDao usuarioDao;
    private DocumentoDao documentoDao;
    private static ClienteDao clienteDao;
    private NotasDao notasDao;
    private static MoedaDao moedaDao;
    private AnoEconomicoDao anoEconomicoDao;
    private static ArmazemDao armazemDao;
    private NotasItemDao notasItemDao;
    private TbVasilhame vasilhame;
    private Moeda moeda;
    private AnoEconomico anoEconomico;
    private static BDConexao conexao;
    private StockModelo stockModelo;
    private TipoClienteController tipoClienteController;
    private VendaController vendaController;
    private ItemVendaController itemVendaController;
    private ItemVendaModelo itemVendaModelo;
    private VendaModelo vendaModelo;
    private ClienteModelo clienteModelo;
    private CambioDao cambioDao;
    private Documento documento;
    private static ProdutoImpostoDao produtoImpostoDao;
    private Cambio cambio;
    private TipoProdutoDao tipoProdutoDao;
    private int cod_usuario;
    private TipoClienteModelo tipoClienteModelo;
    private StockController stockController;
    private int linha = 0, coordenada = 1, doc_prox_cod = 0;
    private double soma_total = 0, total_iva = 0;
    private ProdutoModelo produtoModelo;
    private static ProdutoIsentoDao produtoIsentoDao;
    private static int linha_actual = -1;
    private Abreviacao abreviacao;
    private String motivos_isentos = "";

    private Thread t;
    private String prox_doc;

    private Notas getNotaCreditoModel()
    {
        Date data_documento = dc_data_documento.getDate();
        TbVenda venda = getVenda( cmbFacturaCredito );
        Notas notas_local = new Notas();

        notas_local.setDataNota( data_documento );
        notas_local.setHora( data_documento );
        notas_local.setNomeCliente( venda.getNomeCliente() );
        notas_local.setClienteNif( venda.getClienteNif() );

        //Total Ilíquido
        notas_local.setTotalGeral( getTotalIliquido() );
        //desconto por linha
        notas_local.setDescontoComercial( getDescontoComercial() );
        //imposto
        //calculaTotalIVA();
        notas_local.setTotalIva( getTotalImposto() );
        //desconto global
        notas_local.setDescontoFinaceiro( getDescontoFinanceiro() );
        //Total(AOA) <=> Total Líquido
        notas_local.setTotalVenda( new BigDecimal( getTotalAOALiquido() ));
        notas_local.setValorEntregue( 0.0 );
        notas_local.setTroco( 0.0 );
        //notas_local.setTotalIncidencia( getTotalIncidencia() );
        notas_local.setTotalIncidencia( getTotalIncidencia() );
        notas_local.setTotalIncidenciaIsento( getTotalIncidenciaIsento() );

        /*outros campos*/
        notas_local.setDescontoTotal( getDescontoComercial() + getDescontoFinanceiro() );
        notas_local.setIdBanco( venda.getIdBanco() );
        notas_local.setIdArmazemFK( venda.getIdArmazemFK() );
        notas_local.setCodigoUsuario( usuarioDao.findTbUsuario( cod_usuario ) );
        notas_local.setCodigoCliente( clienteDao.findTbCliente( getIdCliente() ) );
        notas_local.setFkAnoEconomico( this.anoEconomico );

        notas_local.setFkDocumento( this.documento );
        notas_local.setCodNota( this.prox_doc );
//        notas.setHashCod ( MetodosUtil.criptografia_hash ( this.prox_doc ) );
//#HASH_TESTE        notas.setHashCod( MetodosUtil.criptografia_hash( notas.getCodNota() ) ); 

        notas_local.setHashCod( MetodosUtil.criptografia_hash( notas_local, getGrossTotal(), conexao ) );
        notas_local.setTotalPorExtenso( lbValorPorExtenco.getText() );
        //System.out.println( "STATUS:hash cod processado." );
        notas_local.setAssinatura( MetodosUtil.assinatura_doc( notas_local.getHashCod() ) );
        notas_local.setRefFactData( dc_data_factura_credito.getDate() );
        //System.out.println( "STATUS:documento assinado com sucesso." );

        if ( txtCambio.getText().endsWith( DVML.MOEDA_KWANZA ) )
        {
            notas_local.setFkCambio( new Cambio( 1 ) );
        }
        else
        {
            Cambio cambio_nacional = cambioDao.getLastObject( getIdMoeda() );
            notas_local.setFkCambio( cambio_nacional );
        }

        /*status documento*/
        notas_local.setStatusEliminado( "false" );
        notas_local.setPerformance( "false" );
        notas_local.setCredito( "false" );

        notas_local.setMotivo( motivoJTextArea.getText() );

        notas_local.setObs( DVML.RECTIFICACAO );

        notas_local.setCodNota( this.prox_doc );
        notas_local.setRefCodFact( venda.getCodFact() );
        notas_local.setTipoNota( "C" );
        ESTADO_NOTA estado_nota = defininirEstadoNotas( venda );

        notas_local.setEstado( estado_nota.toString() );

        return notas_local;
    }

    private Notas getNotaDebitoModel()
    {
        TbVenda venda = getVenda( cmbFacturaDebito );
        Date data_documento = dc_data_documento.getDate();
        Notas notas_local = new Notas();

        notas_local.setDataNota( data_documento );
        notas_local.setHora( data_documento );
        notas_local.setNomeCliente( venda.getNomeCliente() );
        notas_local.setClienteNif( venda.getClienteNif() );

        //Total Ilíquido
        notas_local.setTotalGeral( venda.getTotalGeral().doubleValue() );

        //desconto por linha
        notas_local.setDescontoComercial( venda.getDescontoComercial().doubleValue() );
        //imposto
        //calculaTotalIVA();
        notas_local.setTotalIva( venda.getTotalIva().doubleValue() );
        //desconto global
        notas_local.setDescontoFinaceiro( venda.getDescontoFinanceiro().doubleValue() );
        //Total(AOA) <=> Total Líquido
        notas_local.setTotalVenda( new BigDecimal(venda.getTotalVenda().doubleValue() ));
        notas_local.setValorEntregue( (double) sp_valor_entregue1.getValue() );
        Double troco = CfMethods.parseMoedaFormatada( txtTroco1.getText() );
        notas_local.setTroco( troco == null ? 0.0 : troco );
//        notas_local.setTotalIncidencia( venda.getTotalIncidencia() );
//        notas_local.setTotalIncidencia( venda.getTotalIncidenciaIsento() );
        notas_local.setTotalIncidencia( getTotalIncidencia() );
        notas_local.setTotalIncidenciaIsento( getTotalIncidenciaIsento() );

        /*outros campos*/
        notas_local.setDescontoTotal( venda.getDescontoTotal().doubleValue() );
        notas_local.setIdBanco( venda.getIdBanco() );
        notas_local.setIdArmazemFK( venda.getIdArmazemFK() );
        notas_local.setCodigoUsuario( usuarioDao.findTbUsuario( cod_usuario ) );
        notas_local.setCodigoCliente( venda.getCodigoCliente() );
        notas_local.setFkAnoEconomico( venda.getFkAnoEconomico() );

        notas_local.setFkDocumento( this.documento );
        notas_local.setCodNota( this.prox_doc );
//        notas.setHashCod ( MetodosUtil.criptografia_hash ( this.prox_doc ) );
//#HASH_TESTE        notas.setHashCod( MetodosUtil.criptografia_hash( notas.getCodNota() ) );
        notas_local.setHashCod( MetodosUtil.criptografia_hash( notas_local, getGrossTotal( venda.getTbItemVendaList() ), conexao ) );
        notas_local.setTotalPorExtenso( lbValorPorExtenco1.getText() );
        //System.out.println( "STATUS:hash cod processado." );
        notas_local.setAssinatura( MetodosUtil.assinatura_doc( notas_local.getHashCod() ) );
        notas_local.setRefFactData( dc_data_factura_debito.getDate() );
        //System.out.println( "STATUS:documento assinado com sucesso." );

        notas_local.setFkCambio( venda.getFkCambio() );


        /*status documento*/
        notas_local.setStatusEliminado( "false" );
        notas_local.setPerformance( "false" );
        notas_local.setCredito( "false" );

        notas_local.setMotivo( motivoJTextArea1.getText() );

        notas_local.setObs( DVML.RECTIFICACAO );

        notas_local.setCodNota( this.prox_doc );
        notas_local.setRefCodFact( venda.getCodFact() );

        notas_local.setTipoNota( "D" );

//#HASH_TESTE        notas.setHashCod( MetodosUtil.criptografia_hash( notas.getCodNota() ) );
        notas_local.setTotalPorExtenso( lbValorPorExtenco1.getText() );
        System.out.println( "STATUS:hash cod processado." );
        notas_local.setAssinatura( MetodosUtil.assinatura_doc( notas_local.getHashCod() ) );
        System.out.println( "STATUS:documento assinado com sucesso." );
        ESTADO_NOTA estado_nota = defininirEstadoNotas( venda );

        notas_local.setEstado( estado_nota.toString() );

        return notas_local;
        /*
        notas.setPerformance( "false" );
        notas.setCredito( "false" );
        notas.setValorEntregue( ( double ) sp_valor_entregue1.getValue() );
        Double troco = CfMethods.parseMoedaFormatada( txtTroco1.getText() );
        notas.setTroco( troco == null ? 0.0 : troco );
        notas.setIdArmazemFK( venda.getIdArmazemFK() );
        notas.setDataNota( new Date() );
        notas.setHora( new Date() );
        notas.setNomeCliente( venda.getNomeCliente() );
        notas.setDescontoTotal( 0.0 );
        notas.setTotalGeral( CfMethods.parseMoedaFormatada( txtTotalApagar1.getText() ) );
        notas.setIdBanco( venda.getIdBanco() );
        notas.setStatusEliminado( "false" );
        notas.setFkAnoEconomico( venda.getFkAnoEconomico() );
        notas.setFkCambio( venda.getFkCambio() );
        notas.setFkDocumento( getDocumento() );
        notas.setRefFactData( venda.getDataVenda() );
        notas.setMotivo( motivoJTextArea1.getText() );

        notas.setObs( DVML.RECTIFICACAO );

        notas.setCodNota( this.prox_doc );
        notas.setRefCodFact( venda.getCodFact() );
        calculaTotalIVA();
        notas.setTotalIva( this.total_iva );

        notas.setTipoNota( "D" );

        notas.setHashCod( MetodosUtil.criptografia_hash( notas.getCodNota() ) );
        notas.setTotalPorExtenso( lbValorPorExtenco1.getText() );
        System.out.println( "STATUS:hash cod processado." );
        notas.setAssinatura( MetodosUtil.assinatura_doc( notas.getHashCod() ) );
        System.out.println( "STATUS:documento assinado com sucesso." );
        ESTADO_NOTA estado_nota = defininirEstadoNotas( venda );

        notas.setEstado( estado_nota.toString() );

        return notas;*/

    }

    private void initNotaCreditoTabe()
    {
        cmbCodigoProduto.setModel( new DefaultComboBoxModel() );

        txtCodigoBarra.setDocument( new PermitirNumeros() );
        lbValorPorExtenco.setText( "" );
        mostrar_nome( lb_usuario );
        cmbArmazem.setModel( new DefaultComboBoxModel( armazemDao.buscaTodos1() ) );
        txtCategoria.setText( "" );
        txtProduto.setText( "" );
        cmbFormaPagamento.setModel( new DefaultComboBoxModel( (Vector) bancoDao.buscaTodos() ) );
        txtMoeda.setText( "" );
        cmbCliente.setModel( new DefaultComboBoxModel( conexao.getElementos( "tb_cliente", "nome", false ) ) );
        cmbCliente.setSelectedItem( "DIVERSOS" );
        dc_data_documento.setDate( new Date() );
        txtQuatindade.setValue( 1 );
        cmbCodigoProduto.requestFocus();
        txtClienteNome.requestFocus();
        //desactivar_campos();
        mostrar_ano_economico_serie( lb_ano_academico );
        lb_proximo_documento.setText( "" );
        txtTotalApagar.setText( CfMethods.formatarComoMoeda( 0.0 ) );
//        sp_valor_entregue.addKeyListener(new TratarEventoValorEntregue());
//        sp_valor_entregue.addKeyListener(new TratarTroco());

        sp_valor_entregue.setModel( CfMethodsSwing.criarSpinnerDoubleModel( 0.0, 10000000000.00, 0.0 ) );

        atualizarCMBVendas( cmbFacturaCredito );
        cmbFacturaCredito.setSelectedIndex( -1 );

        motivoJTextArea.getDocument().addDocumentListener( new DocumentListener()
        {

            @Override
            public void insertUpdate( DocumentEvent e )
            {
                atualizarBotaoSalvar( okJButton );
            }

            @Override
            public void removeUpdate( DocumentEvent e )
            {
                atualizarBotaoSalvar( okJButton );
            }

            @Override
            public void changedUpdate( DocumentEvent e )
            {
            }
        } );

        lbCodigoProduto.setHorizontalAlignment( JLabel.RIGHT );
        operacaoJLabel.setHorizontalAlignment( JLabel.RIGHT );
        lbProduto.setHorizontalAlignment( JLabel.RIGHT );
        lbPreco.setHorizontalAlignment( JLabel.RIGHT );
        lbQuantidade.setHorizontalAlignment( JLabel.RIGHT );
        lbTotalPagar.setHorizontalAlignment( JLabel.RIGHT );
        lbValorEnregue.setHorizontalAlignment( JLabel.RIGHT );
        lbTroco.setHorizontalAlignment( JLabel.RIGHT );
        //lbCliente.setHorizontalAlignment(JLabel.RIGHT);
        lbQuantidadeStock.setHorizontalAlignment( JLabel.RIGHT );

    }

    private void initNotaDebitoTabe()
    {

        lbValorPorExtenco1.setText( "" );
        mostrar_nome( lb_usuario1 );
        cmbArmazem1.setModel( new DefaultComboBoxModel( armazemDao.buscaTodos1() ) );
        cmbFormaPagamento1.setModel( new DefaultComboBoxModel( (Vector) bancoDao.buscaTodos() ) );
        txtMoeda1.setText( "" );
        txtClienteNome1.requestFocus();
        mostrar_ano_economico_serie( lb_ano_academico1 );
        lb_proximo_documento1.setText( "" );
        txtTotalApagar1.setText( CfMethods.formatarComoMoeda( 0.0 ) );

        sp_valor_entregue1.setModel( CfMethodsSwing.criarSpinnerDoubleModel( 0.0, 10000000000.00, 0.0 ) );

        atualizarCMBVendas( cmbFacturaDebito );
        System.err.println( "cmbFactura1: " + cmbFacturaDebito.getModel().getSize() );

        cmbFacturaDebito.setSelectedIndex( -1 );

        motivoJTextArea1.getDocument().addDocumentListener( new DocumentListener()
        {

            @Override
            public void insertUpdate( DocumentEvent e )
            {
                atualizarBotaoSalvar1( okJButton1 );
            }

            @Override
            public void removeUpdate( DocumentEvent e )
            {
                atualizarBotaoSalvar1( okJButton1 );
            }

            @Override
            public void changedUpdate( DocumentEvent e )
            {
            }
        } );

        lbCodigoProduto1.setHorizontalAlignment( JLabel.RIGHT );
        operacaoJLabel1.setHorizontalAlignment( JLabel.RIGHT );
        lbPreco1.setHorizontalAlignment( JLabel.RIGHT );
        lbTotalPagar1.setHorizontalAlignment( JLabel.RIGHT );
        lbValorEnregue1.setHorizontalAlignment( JLabel.RIGHT );
        lbTroco1.setHorizontalAlignment( JLabel.RIGHT );
        txtTroco1.setText( CfMethods.formatarComoMoeda( 0.0 ) );
    }

    private boolean salvarNotaDebitoItemNota( int cod_notas )
    {
        TbVenda venda = getVenda( cmbFacturaDebito );

        boolean efectuada = true;
        this.notas = notasDao.findNotas( cod_notas );

        List<NotasItem> notasItems = getConvertVendasToNotas( venda );

        for ( NotasItem notasItem1 : notasItems )
        {
            notasItem1.setFkNota( notas );
            notasItemDao.create( notasItem1 );
        }

        if ( efectuada )
        {
            JOptionPane.showMessageDialog( null, "Factura efectuada com sucesso!.." );
            try
            {

                limpar();
                remover_all_produto();
                //adicionar_preco_quantidade_anitgo();
            }
            catch ( Exception e )
            {
                return false;
            }

            txtClienteNome.setText( "" );
            txtClienteNome.requestFocus();
            //Chama a factura e imprime directamente para a imprissora que estiver devenidade no sistema operativo

            return true;
            //ListaVenda1 listaVenda2 = new ListaVenda1(cod_notas, false, ck_simplificada.isSelected());

            //Em caso em que a impreensão é dupla
            //ListaVendaDuplicado listaVenda1 = new ListaVendaDuplicado(cod_notas, setPeformance(), ckImpreesao.isSelected());
        }
        return false;

    }

    private void atualizarProdutoAdicionarRemover()
    {
        adicionarJButton.setEnabled( cmbCodigoProduto.getSelectedIndex() > -1 );
        removerProdutoJButton.setEnabled( tabelaItensNotas.getRowCount() > 0 );
    }

    public enum EVENTO_DO_SISTEMA
    {

        SALVAR_REGISTRO,
        ADICIONAR_PRODUTO,
        REMOVER_PRODUTO,
        SELECIONAR_TIPO_DOCUMENTO,
        SELECIONAR_PRODUTO,
        MUDAR_DE_ABA,
        SELECIONAR_FACTURA_DE_REF,
        SELECIONAR_PRODUTO_PELO_COD_BARRA,
        PADRAO,
        ND_SELECIONAR_FACTURA_DE_REF,
        TABELA_ALTERADA
    }

    public NotasCreditoDebitoVisao( int cod_usuario, BDConexao conexao ) throws SQLException
    {

        initComponents();

        setLocationRelativeTo( null );
        setResizable( false );
        this.cod_usuario = cod_usuario;
        this.conexao = conexao;
        setWindowsListener();
        init();
        postInitComponents();
    }

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
        jMenuItem1 = new javax.swing.JMenuItem();
        buttonGroup2 = new javax.swing.ButtonGroup();
        principalJTabbedPane = new javax.swing.JTabbedPane();
        notasCreditoJPanel = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        lbTotalPagar = new javax.swing.JLabel();
        lbValorEnregue = new javax.swing.JLabel();
        lbTroco = new javax.swing.JLabel();
        txtTroco = new javax.swing.JTextField();
        txtTotalApagar = new javax.swing.JTextField();
        sp_valor_entregue = new javax.swing.JSpinner();
        lbValorPorExtenco = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        cmbCliente = new javax.swing.JComboBox();
        lbCliente = new javax.swing.JLabel();
        cmbFormaPagamento = new javax.swing.JComboBox();
        lbFormaPagamento = new javax.swing.JLabel();
        txtClienteNome = new javax.swing.JTextField();
        lb_usuario = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        lbCodigoProduto = new javax.swing.JLabel();
        lbPreco = new javax.swing.JLabel();
        lbProduto = new javax.swing.JLabel();
        lbQuantidade = new javax.swing.JLabel();
        removerProdutoJButton = new javax.swing.JButton();
        adicionarJButton = new javax.swing.JButton();
        lbQuantidadeStock = new javax.swing.JLabel();
        lbCodigoProduto1 = new javax.swing.JLabel();
        txtCodigoBarra = new javax.swing.JTextField();
        txtQuantidaStock = new javax.swing.JTextField();
        jButton4 = new javax.swing.JButton();
        txtPreco = new javax.swing.JTextField();
        cmbCodigoProduto = new javax.swing.JComboBox();
        txtQuatindade = new javax.swing.JSpinner();
        txtCategoria = new javax.swing.JTextField();
        txtProduto = new javax.swing.JTextField();
        lbCategoria1 = new javax.swing.JLabel();
        lbCategoria2 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabelaItensNotas = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        lb_proximo_documento = new javax.swing.JLabel();
        lbPreco1 = new javax.swing.JLabel();
        cmbArmazem = new javax.swing.JComboBox();
        lbPreco5 = new javax.swing.JLabel();
        lb_cambio = new javax.swing.JLabel();
        lbPreco4 = new javax.swing.JLabel();
        cmbFacturaCredito = new javax.swing.JComboBox();
        txtMoeda = new javax.swing.JTextField();
        txtCambio = new javax.swing.JTextField();
        lb_ano_academico = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        okJButton = new javax.swing.JButton();
        fecharJButton = new javax.swing.JButton();
        operacaoJLabel = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        motivoJTextArea = new javax.swing.JTextArea();
        dc_data_documento = new com.toedter.calendar.JDateChooser();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        dc_data_factura_credito = new com.toedter.calendar.JDateChooser();
        notasDebitoJPanel = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        lb_proximo_documento1 = new javax.swing.JLabel();
        lbPreco7 = new javax.swing.JLabel();
        cmbArmazem1 = new javax.swing.JComboBox();
        lbPreco8 = new javax.swing.JLabel();
        lb_cambio1 = new javax.swing.JLabel();
        lbPreco9 = new javax.swing.JLabel();
        cmbFacturaDebito = new javax.swing.JComboBox();
        txtMoeda1 = new javax.swing.JTextField();
        txtCambio1 = new javax.swing.JTextField();
        lbTotalPagar1 = new javax.swing.JLabel();
        txtTotalApagar1 = new javax.swing.JTextField();
        lbValorEnregue1 = new javax.swing.JLabel();
        sp_valor_entregue1 = new javax.swing.JSpinner();
        txtTroco1 = new javax.swing.JTextField();
        lbTroco1 = new javax.swing.JLabel();
        cmbFormaPagamento1 = new javax.swing.JComboBox();
        lbFormaPagamento1 = new javax.swing.JLabel();
        lbCliente1 = new javax.swing.JLabel();
        txtClienteNome1 = new javax.swing.JTextField();
        lbValorPorExtenco1 = new javax.swing.JLabel();
        lb_usuario1 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        motivoJTextArea1 = new javax.swing.JTextArea();
        jPanel16 = new javax.swing.JPanel();
        okJButton1 = new javax.swing.JButton();
        fecharJButton1 = new javax.swing.JButton();
        operacaoJLabel1 = new javax.swing.JLabel();
        lb_ano_academico1 = new javax.swing.JLabel();
        dc_data_factura_debito = new com.toedter.calendar.JDateChooser();
        jLabel3 = new javax.swing.JLabel();

        jMenuItem1.setText("jMenuItem1");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("...:::::  KITANDA - FACTURAÇÃO ::::...");
        getContentPane().setLayout(new java.awt.CardLayout());

        principalJTabbedPane.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        principalJTabbedPane.addChangeListener(new javax.swing.event.ChangeListener()
        {
            public void stateChanged(javax.swing.event.ChangeEvent evt)
            {
                principalJTabbedPaneStateChanged(evt);
            }
        });

        notasCreditoJPanel.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        notasCreditoJPanel.setPreferredSize(new java.awt.Dimension(1149, 754));

        jPanel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jPanel1.setFont(new java.awt.Font("Showcard Gothic", 0, 24)); // NOI18N

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel8.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lbTotalPagar.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbTotalPagar.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbTotalPagar.setText("Total a Pagar :");
        jPanel8.add(lbTotalPagar, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 120, 34));

        lbValorEnregue.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbValorEnregue.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbValorEnregue.setText("Valor Entregue :");
        jPanel8.add(lbValorEnregue, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 120, 30));

        lbTroco.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbTroco.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbTroco.setText("Troco:");
        jPanel8.add(lbTroco, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, 120, 30));

        txtTroco.setEditable(false);
        txtTroco.setBackground(new java.awt.Color(4, 154, 3));
        txtTroco.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        txtTroco.setForeground(new java.awt.Color(255, 255, 255));
        txtTroco.setCaretColor(new java.awt.Color(255, 255, 255));
        jPanel8.add(txtTroco, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 110, 330, 30));

        txtTotalApagar.setEditable(false);
        txtTotalApagar.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        txtTotalApagar.setForeground(new java.awt.Color(255, 0, 0));
        txtTotalApagar.setCaretColor(new java.awt.Color(255, 255, 255));
        txtTotalApagar.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtTotalApagarActionPerformed(evt);
            }
        });
        jPanel8.add(txtTotalApagar, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 20, 330, 40));

        sp_valor_entregue.setEnabled(false);
        sp_valor_entregue.addChangeListener(new javax.swing.event.ChangeListener()
        {
            public void stateChanged(javax.swing.event.ChangeEvent evt)
            {
                sp_valor_entregueStateChanged(evt);
            }
        });
        sp_valor_entregue.addInputMethodListener(new java.awt.event.InputMethodListener()
        {
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt)
            {
                sp_valor_entregueInputMethodTextChanged(evt);
            }
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt)
            {
            }
        });
        sp_valor_entregue.addPropertyChangeListener(new java.beans.PropertyChangeListener()
        {
            public void propertyChange(java.beans.PropertyChangeEvent evt)
            {
                sp_valor_entreguePropertyChange(evt);
            }
        });
        sp_valor_entregue.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyTyped(java.awt.event.KeyEvent evt)
            {
                sp_valor_entregueKeyTyped(evt);
            }
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                sp_valor_entregueKeyPressed(evt);
            }
        });
        jPanel8.add(sp_valor_entregue, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 70, 330, 30));

        lbValorPorExtenco.setFont(new java.awt.Font("Lucida Grande", 1, 18)); // NOI18N
        lbValorPorExtenco.setForeground(new java.awt.Color(204, 0, 0));
        lbValorPorExtenco.setText("VALOR POR EXTENSO");

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        cmbCliente.setBackground(new java.awt.Color(4, 154, 3));
        cmbCliente.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        cmbCliente.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbCliente.setEnabled(false);
        cmbCliente.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbClienteActionPerformed(evt);
            }
        });
        jPanel3.add(cmbCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 50, 230, 30));

        lbCliente.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbCliente.setText("Cliente:");
        jPanel3.add(lbCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, 60, 40));

        cmbFormaPagamento.setBackground(new java.awt.Color(4, 154, 3));
        cmbFormaPagamento.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        cmbFormaPagamento.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbFormaPagamento.setEnabled(false);
        cmbFormaPagamento.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbFormaPagamentoActionPerformed(evt);
            }
        });
        jPanel3.add(cmbFormaPagamento, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 10, 250, 30));

        lbFormaPagamento.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbFormaPagamento.setText("Forma de Pagamento:");
        jPanel3.add(lbFormaPagamento, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 160, 30));

        txtClienteNome.setEditable(false);
        txtClienteNome.setBackground(new java.awt.Color(4, 154, 3));
        txtClienteNome.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txtClienteNome.setForeground(new java.awt.Color(255, 255, 255));
        txtClienteNome.setCaretColor(new java.awt.Color(255, 255, 255));
        txtClienteNome.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtClienteNomeActionPerformed(evt);
            }
        });
        jPanel3.add(txtClienteNome, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, 400, 30));

        lb_usuario.setFont(new java.awt.Font("Lucida Grande", 1, 12)); // NOI18N
        lb_usuario.setText("Conta:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbValorPorExtenco, javax.swing.GroupLayout.PREFERRED_SIZE, 758, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lb_usuario, javax.swing.GroupLayout.PREFERRED_SIZE, 691, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 442, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbValorPorExtenco)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lb_usuario, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Lucida Grande", 0, 13), new java.awt.Color(4, 154, 3))); // NOI18N
        jPanel4.setForeground(new java.awt.Color(102, 153, 0));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lbCodigoProduto.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbCodigoProduto.setText("CodArt:");
        jPanel4.add(lbCodigoProduto, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 10, 60, 25));

        lbPreco.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbPreco.setText("Preco:");
        jPanel4.add(lbPreco, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 100, 50, 25));

        lbProduto.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbProduto.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbProduto.setText("Artigo:");
        jPanel4.add(lbProduto, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 70, 80, 25));

        lbQuantidade.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbQuantidade.setText("Qtd:");
        jPanel4.add(lbQuantidade, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 190, -1, 25));

        removerProdutoJButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/2934_32x32.png"))); // NOI18N
        removerProdutoJButton.setToolTipText("click para remover produtos do carrinho");
        removerProdutoJButton.setEnabled(false);
        removerProdutoJButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                removerProdutoJButtonActionPerformed(evt);
            }
        });
        jPanel4.add(removerProdutoJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 230, 50, 50));

        adicionarJButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/Button-Add-icon.png"))); // NOI18N
        adicionarJButton.setToolTipText("click para adicionar no carrinho");
        adicionarJButton.setEnabled(false);
        adicionarJButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                adicionarJButtonActionPerformed(evt);
            }
        });
        jPanel4.add(adicionarJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 230, 50, 50));

        lbQuantidadeStock.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbQuantidadeStock.setText("Q.Stock:");
        jPanel4.add(lbQuantidadeStock, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 160, -1, 25));

        lbCodigoProduto1.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbCodigoProduto1.setText("CodBarra:");
        jPanel4.add(lbCodigoProduto1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, 70, 25));

        txtCodigoBarra.setEditable(false);
        txtCodigoBarra.setBackground(new java.awt.Color(4, 154, 3));
        txtCodigoBarra.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        txtCodigoBarra.setForeground(new java.awt.Color(255, 255, 255));
        txtCodigoBarra.setCaretColor(new java.awt.Color(255, 255, 255));
        txtCodigoBarra.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtCodigoBarraActionPerformed(evt);
            }
        });
        jPanel4.add(txtCodigoBarra, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 130, 270, 25));

        txtQuantidaStock.setEditable(false);
        txtQuantidaStock.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        txtQuantidaStock.setForeground(new java.awt.Color(255, 255, 255));
        jPanel4.add(txtQuantidaStock, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 160, 270, 25));

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/proucura.png"))); // NOI18N
        jButton4.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel4.add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 10, 40, 25));

        txtPreco.setEditable(false);
        txtPreco.setBackground(new java.awt.Color(4, 154, 3));
        txtPreco.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txtPreco.setForeground(new java.awt.Color(255, 255, 255));
        txtPreco.setCaretColor(new java.awt.Color(255, 255, 255));
        jPanel4.add(txtPreco, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 100, 270, 25));

        cmbCodigoProduto.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        cmbCodigoProduto.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbCodigoProduto.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbCodigoProdutoActionPerformed(evt);
            }
        });
        jPanel4.add(cmbCodigoProduto, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 10, 220, 25));

        txtQuatindade.setEnabled(false);
        jPanel4.add(txtQuatindade, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 190, 270, 25));

        txtCategoria.setEditable(false);
        txtCategoria.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jPanel4.add(txtCategoria, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 40, 270, 25));

        txtProduto.setEditable(false);
        txtProduto.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jPanel4.add(txtProduto, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 70, 270, 25));

        lbCategoria1.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbCategoria1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbCategoria1.setText("Categoria:");
        jPanel4.add(lbCategoria1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 40, 80, 25));

        lbCategoria2.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbCategoria2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbCategoria2.setText("Categoria:");
        jPanel4.add(lbCategoria2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 40, 80, 25));

        jPanel2.setBackground(new java.awt.Color(204, 204, 204));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        tabelaItensNotas.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        tabelaItensNotas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "Cod.Art", "Descrição", "Preço", "Qtd.", "Desconto(%)", "Taxa", "Valor", "Valor(IVA)"
            }
        )
        {
            Class[] types = new Class []
            {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean []
            {
                false, false, false, false, false, false, false, false
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
        tabelaItensNotas.setCellSelectionEnabled(true);
        tabelaItensNotas.setGridColor(new java.awt.Color(51, 153, 0));
        tabelaItensNotas.addPropertyChangeListener(new java.beans.PropertyChangeListener()
        {
            public void propertyChange(java.beans.PropertyChangeEvent evt)
            {
                tabelaItensNotasPropertyChange(evt);
            }
        });
        jScrollPane1.setViewportView(tabelaItensNotas);
        tabelaItensNotas.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        if (tabelaItensNotas.getColumnModel().getColumnCount() > 0)
        {
            tabelaItensNotas.getColumnModel().getColumn(0).setPreferredWidth(10);
            tabelaItensNotas.getColumnModel().getColumn(1).setPreferredWidth(250);
            tabelaItensNotas.getColumnModel().getColumn(2).setPreferredWidth(20);
            tabelaItensNotas.getColumnModel().getColumn(3).setPreferredWidth(5);
            tabelaItensNotas.getColumnModel().getColumn(5).setPreferredWidth(5);
        }

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1122, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 212, Short.MAX_VALUE)
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        lb_proximo_documento.setFont(new java.awt.Font("Lucida Grande", 1, 16)); // NOI18N
        lb_proximo_documento.setText("PRÓXIMO DOC. : XX PP/A1");

        lbPreco1.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbPreco1.setText("Armzém");

        cmbArmazem.setBackground(new java.awt.Color(4, 154, 3));
        cmbArmazem.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 14)); // NOI18N
        cmbArmazem.setEnabled(false);
        cmbArmazem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbArmazemActionPerformed(evt);
            }
        });

        lbPreco5.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbPreco5.setText("Moeda");

        lb_cambio.setFont(new java.awt.Font("Lucida Grande", 1, 16)); // NOI18N
        lb_cambio.setText("Cambio");

        lbPreco4.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbPreco4.setText("Factura");

        cmbFacturaCredito.setBackground(new java.awt.Color(4, 154, 3));
        cmbFacturaCredito.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 14)); // NOI18N
        cmbFacturaCredito.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbFacturaCreditoActionPerformed(evt);
            }
        });

        txtMoeda.setEditable(false);
        txtMoeda.setFont(new java.awt.Font("Tw Cen MT", 3, 14)); // NOI18N

        txtCambio.setEditable(false);
        txtCambio.setFont(new java.awt.Font("Tw Cen MT", 3, 14)); // NOI18N
        txtCambio.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtCambioActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lb_proximo_documento, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbPreco4, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbPreco1, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cmbFacturaCredito, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cmbArmazem, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(lb_cambio, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtCambio, javax.swing.GroupLayout.PREFERRED_SIZE, 327, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(lbPreco5, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtMoeda, javax.swing.GroupLayout.PREFERRED_SIZE, 327, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 10, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbPreco4)
                    .addComponent(cmbFacturaCredito, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbArmazem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbPreco1))
                .addGap(18, 18, 18)
                .addComponent(lb_proximo_documento, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbPreco5)
                    .addComponent(txtMoeda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lb_cambio, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCambio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        lb_ano_academico.setFont(new java.awt.Font("Lucida Grande", 1, 16)); // NOI18N
        lb_ano_academico.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lb_ano_academico.setText("ANO ECONÔMICO");

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Operações", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("SansSerif", 1, 14))); // NOI18N

        okJButton.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        okJButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/impressora1.png"))); // NOI18N
        okJButton.setToolTipText("Efectuar Venda");
        okJButton.setEnabled(false);
        okJButton.setName("Emitir nota"); // NOI18N
        okJButton.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseExited(java.awt.event.MouseEvent evt)
            {
                okJButtonMouseExited(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt)
            {
                okJButtonMouseEntered(evt);
            }
        });
        okJButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                okJButtonActionPerformed(evt);
            }
        });

        fecharJButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/LOGOUT - VERMELHO/Logout 32x32.png"))); // NOI18N
        fecharJButton.setAlignmentX(0.5F);
        fecharJButton.setName("Fechar janela"); // NOI18N
        fecharJButton.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseEntered(java.awt.event.MouseEvent evt)
            {
                fecharJButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt)
            {
                fecharJButtonMouseExited(evt);
            }
        });
        fecharJButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                fecharJButtonActionPerformed(evt);
            }
        });

        operacaoJLabel.setFont(new java.awt.Font("Lucida Grande", 2, 14)); // NOI18N
        operacaoJLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        operacaoJLabel.setText("Categoria:");
        operacaoJLabel.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(okJButton, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(fecharJButton, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(operacaoJLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(operacaoJLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(okJButton, javax.swing.GroupLayout.DEFAULT_SIZE, 58, Short.MAX_VALUE)
                    .addComponent(fecharJButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jScrollPane2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Motivo da Alteração ou Rectificação", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("SansSerif", 1, 14))); // NOI18N

        motivoJTextArea.setColumns(20);
        motivoJTextArea.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        motivoJTextArea.setRows(5);
        jScrollPane2.setViewportView(motivoJTextArea);

        jLabel1.setText("DataDoc.");

        jLabel2.setText("DataVenda.");

        javax.swing.GroupLayout notasCreditoJPanelLayout = new javax.swing.GroupLayout(notasCreditoJPanel);
        notasCreditoJPanel.setLayout(notasCreditoJPanelLayout);
        notasCreditoJPanelLayout.setHorizontalGroup(
            notasCreditoJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(notasCreditoJPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(notasCreditoJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(notasCreditoJPanelLayout.createSequentialGroup()
                        .addGroup(notasCreditoJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(notasCreditoJPanelLayout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(dc_data_documento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(51, 51, 51)
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(dc_data_factura_credito, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 379, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(notasCreditoJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lb_ano_academico, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jScrollPane2)))
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        notasCreditoJPanelLayout.setVerticalGroup(
            notasCreditoJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(notasCreditoJPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(notasCreditoJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(notasCreditoJPanelLayout.createSequentialGroup()
                        .addComponent(lb_ano_academico, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(5, 5, 5)
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(notasCreditoJPanelLayout.createSequentialGroup()
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(notasCreditoJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(dc_data_documento, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(dc_data_factura_credito, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        principalJTabbedPane.addTab("Notas de crédito", notasCreditoJPanel);

        notasDebitoJPanel.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        notasDebitoJPanel.setPreferredSize(new java.awt.Dimension(1149, 754));

        jPanel15.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        lb_proximo_documento1.setFont(new java.awt.Font("Lucida Grande", 1, 16)); // NOI18N
        lb_proximo_documento1.setText("PRÓXIMO DOC. : XX PP/A1");

        lbPreco7.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbPreco7.setText("Armzém");

        cmbArmazem1.setBackground(new java.awt.Color(4, 154, 3));
        cmbArmazem1.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 14)); // NOI18N
        cmbArmazem1.setEnabled(false);
        cmbArmazem1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbArmazem1ActionPerformed(evt);
            }
        });

        lbPreco8.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbPreco8.setText("Moeda");

        lb_cambio1.setFont(new java.awt.Font("Lucida Grande", 1, 16)); // NOI18N
        lb_cambio1.setText("Cambio");

        lbPreco9.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbPreco9.setText("Factura");

        cmbFacturaDebito.setBackground(new java.awt.Color(4, 154, 3));
        cmbFacturaDebito.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 14)); // NOI18N
        cmbFacturaDebito.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbFacturaDebitoActionPerformed(evt);
            }
        });

        txtMoeda1.setEditable(false);
        txtMoeda1.setFont(new java.awt.Font("Tw Cen MT", 3, 14)); // NOI18N

        txtCambio1.setEditable(false);
        txtCambio1.setFont(new java.awt.Font("Tw Cen MT", 3, 14)); // NOI18N
        txtCambio1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtCambio1ActionPerformed(evt);
            }
        });

        lbTotalPagar1.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbTotalPagar1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbTotalPagar1.setText("Valor total da factura");

        txtTotalApagar1.setEditable(false);
        txtTotalApagar1.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        txtTotalApagar1.setForeground(new java.awt.Color(255, 0, 0));
        txtTotalApagar1.setCaretColor(new java.awt.Color(255, 255, 255));
        txtTotalApagar1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtTotalApagar1ActionPerformed(evt);
            }
        });

        lbValorEnregue1.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbValorEnregue1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbValorEnregue1.setText("Valor Entregue ");

        sp_valor_entregue1.addChangeListener(new javax.swing.event.ChangeListener()
        {
            public void stateChanged(javax.swing.event.ChangeEvent evt)
            {
                sp_valor_entregue1StateChanged(evt);
            }
        });
        sp_valor_entregue1.addInputMethodListener(new java.awt.event.InputMethodListener()
        {
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt)
            {
                sp_valor_entregue1InputMethodTextChanged(evt);
            }
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt)
            {
            }
        });
        sp_valor_entregue1.addPropertyChangeListener(new java.beans.PropertyChangeListener()
        {
            public void propertyChange(java.beans.PropertyChangeEvent evt)
            {
                sp_valor_entregue1PropertyChange(evt);
            }
        });
        sp_valor_entregue1.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyTyped(java.awt.event.KeyEvent evt)
            {
                sp_valor_entregue1KeyTyped(evt);
            }
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                sp_valor_entregue1KeyPressed(evt);
            }
        });

        txtTroco1.setEditable(false);
        txtTroco1.setBackground(new java.awt.Color(4, 154, 3));
        txtTroco1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        txtTroco1.setForeground(new java.awt.Color(255, 255, 255));
        txtTroco1.setCaretColor(new java.awt.Color(255, 255, 255));

        lbTroco1.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbTroco1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbTroco1.setText("Troco ");

        cmbFormaPagamento1.setBackground(new java.awt.Color(4, 154, 3));
        cmbFormaPagamento1.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        cmbFormaPagamento1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbFormaPagamento1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbFormaPagamento1ActionPerformed(evt);
            }
        });

        lbFormaPagamento1.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbFormaPagamento1.setText("Forma de Pagamento:");

        lbCliente1.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbCliente1.setText("Cliente:");

        txtClienteNome1.setEditable(false);
        txtClienteNome1.setBackground(new java.awt.Color(4, 154, 3));
        txtClienteNome1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txtClienteNome1.setForeground(new java.awt.Color(255, 255, 255));
        txtClienteNome1.setCaretColor(new java.awt.Color(255, 255, 255));
        txtClienteNome1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtClienteNome1ActionPerformed(evt);
            }
        });

        lbValorPorExtenco1.setFont(new java.awt.Font("Lucida Grande", 1, 18)); // NOI18N
        lbValorPorExtenco1.setForeground(new java.awt.Color(204, 0, 0));
        lbValorPorExtenco1.setText("VALOR POR EXTENSO");

        lb_usuario1.setFont(new java.awt.Font("Lucida Grande", 1, 12)); // NOI18N
        lb_usuario1.setText("Conta:");

        jScrollPane4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Motivo da Alteração ou Rectificação", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("SansSerif", 1, 14))); // NOI18N

        motivoJTextArea1.setColumns(20);
        motivoJTextArea1.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        motivoJTextArea1.setRows(5);
        jScrollPane4.setViewportView(motivoJTextArea1);

        jPanel16.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Operações", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("SansSerif", 1, 14))); // NOI18N

        okJButton1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        okJButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/impressora1.png"))); // NOI18N
        okJButton1.setToolTipText("Efectuar Venda");
        okJButton1.setName("Emitir nota"); // NOI18N
        okJButton1.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseExited(java.awt.event.MouseEvent evt)
            {
                okJButton1MouseExited(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt)
            {
                okJButton1MouseEntered(evt);
            }
        });
        okJButton1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                okJButton1ActionPerformed(evt);
            }
        });

        fecharJButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/LOGOUT - VERMELHO/Logout 32x32.png"))); // NOI18N
        fecharJButton1.setAlignmentX(0.5F);
        fecharJButton1.setName("Fechar janela"); // NOI18N
        fecharJButton1.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseEntered(java.awt.event.MouseEvent evt)
            {
                fecharJButton1MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt)
            {
                fecharJButton1MouseExited(evt);
            }
        });
        fecharJButton1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                fecharJButton1ActionPerformed(evt);
            }
        });

        operacaoJLabel1.setFont(new java.awt.Font("Lucida Grande", 2, 14)); // NOI18N
        operacaoJLabel1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        operacaoJLabel1.setText("Categoria:");
        operacaoJLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addComponent(okJButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 59, Short.MAX_VALUE)
                        .addComponent(fecharJButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(operacaoJLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(operacaoJLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(okJButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(fecharJButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        lb_ano_academico1.setFont(new java.awt.Font("Lucida Grande", 1, 16)); // NOI18N
        lb_ano_academico1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lb_ano_academico1.setText("ANO ECONÔMICO");

        jLabel3.setText("DataVenda.");

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel15Layout.createSequentialGroup()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lb_usuario1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lbValorPorExtenco1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(306, 306, 306))
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel15Layout.createSequentialGroup()
                                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel15Layout.createSequentialGroup()
                                        .addComponent(lbPreco8, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(txtMoeda1, javax.swing.GroupLayout.PREFERRED_SIZE, 327, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel15Layout.createSequentialGroup()
                                        .addComponent(lb_cambio1, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(42, 42, 42)
                                        .addComponent(txtCambio1, javax.swing.GroupLayout.PREFERRED_SIZE, 327, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel15Layout.createSequentialGroup()
                                        .addComponent(lbFormaPagamento1, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(cmbFormaPagamento1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGroup(jPanel15Layout.createSequentialGroup()
                                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addGroup(jPanel15Layout.createSequentialGroup()
                                                .addGap(10, 10, 10)
                                                .addComponent(lbTroco1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                            .addComponent(lbValorEnregue1, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(sp_valor_entregue1)
                                            .addGroup(jPanel15Layout.createSequentialGroup()
                                                .addComponent(txtTroco1, javax.swing.GroupLayout.PREFERRED_SIZE, 327, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(0, 0, Short.MAX_VALUE))))
                                    .addGroup(jPanel15Layout.createSequentialGroup()
                                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(lbTotalPagar1, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING))
                                        .addGap(18, 18, 18)
                                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txtTotalApagar1)
                                            .addGroup(jPanel15Layout.createSequentialGroup()
                                                .addComponent(dc_data_factura_debito, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(0, 0, Short.MAX_VALUE)))))
                                .addGap(0, 100, Short.MAX_VALUE))
                            .addGroup(jPanel15Layout.createSequentialGroup()
                                .addComponent(lbCliente1, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(txtClienteNome1)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 277, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(lb_ano_academico1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addComponent(lb_proximo_documento1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel15Layout.createSequentialGroup()
                                .addComponent(lbPreco7, javax.swing.GroupLayout.DEFAULT_SIZE, 104, Short.MAX_VALUE)
                                .addGap(14, 14, 14))
                            .addGroup(jPanel15Layout.createSequentialGroup()
                                .addComponent(lbPreco9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(cmbFacturaDebito, javax.swing.GroupLayout.Alignment.LEADING, 0, 358, Short.MAX_VALUE)
                            .addComponent(cmbArmazem1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(361, 361, 361))))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lb_ano_academico1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lbPreco9)
                            .addComponent(cmbFacturaDebito, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cmbArmazem1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbPreco7))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lb_proximo_documento1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(14, 14, 14)
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lbPreco8)
                            .addComponent(txtMoeda1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lb_cambio1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtCambio1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbTotalPagar1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtTotalApagar1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dc_data_factura_debito, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel15Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(sp_valor_entregue1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbValorEnregue1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbTroco1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTroco1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbFormaPagamento1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbFormaPagamento1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbCliente1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel15Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(txtClienteNome1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lbValorPorExtenco1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lb_usuario1, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );

        javax.swing.GroupLayout notasDebitoJPanelLayout = new javax.swing.GroupLayout(notasDebitoJPanel);
        notasDebitoJPanel.setLayout(notasDebitoJPanelLayout);
        notasDebitoJPanelLayout.setHorizontalGroup(
            notasDebitoJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, notasDebitoJPanelLayout.createSequentialGroup()
                .addContainerGap(108, Short.MAX_VALUE)
                .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(176, 176, 176))
        );
        notasDebitoJPanelLayout.setVerticalGroup(
            notasDebitoJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(notasDebitoJPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, 577, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(160, Short.MAX_VALUE))
        );

        principalJTabbedPane.addTab("Notas de débito", notasDebitoJPanel);

        getContentPane().add(principalJTabbedPane, "card4");

        getAccessibleContext().setAccessibleName("...:::::  KITANDA - FACTURAÃO ::::...");

        pack();
    }// </editor-fold>//GEN-END:initComponents


    private void removerProdutoJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removerProdutoJButtonActionPerformed

        atualizarFormulario( REMOVER_PRODUTO );
    }//GEN-LAST:event_removerProdutoJButtonActionPerformed

    private void adicionarJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_adicionarJButtonActionPerformed
        atualizarFormulario( ADICIONAR_PRODUTO );
    }//GEN-LAST:event_adicionarJButtonActionPerformed

    private void fecharJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fecharJButtonActionPerformed
        dispose();
    }//GEN-LAST:event_fecharJButtonActionPerformed

    private void cmbArmazemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbArmazemActionPerformed
        // TODO add your handling code here:
        adicionar_preco_quantidade();
    }//GEN-LAST:event_cmbArmazemActionPerformed

    private void txtCodigoBarraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCodigoBarraActionPerformed
        // TODO add your handling code here:

        atualizarFormulario( SELECIONAR_PRODUTO_PELO_COD_BARRA );
    }//GEN-LAST:event_txtCodigoBarraActionPerformed

    private void txtClienteNomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtClienteNomeActionPerformed
        // TODO add your handling code here:
        procedimento_emitir_nota_credito();
        txtQuatindade.requestFocus();

    }//GEN-LAST:event_txtClienteNomeActionPerformed

    private void txtTotalApagarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTotalApagarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTotalApagarActionPerformed

    private void okJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okJButtonActionPerformed

        procedimento_emitir_nota_credito();

    }//GEN-LAST:event_okJButtonActionPerformed

    private void cmbFormaPagamentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbFormaPagamentoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbFormaPagamentoActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed

        try
        {
            new BuscaProdutoVisao( this, rootPaneCheckingEnabled, getVenda( cmbFacturaCredito ).getIdArmazemFK().getCodigo(), DVML.JANELA_VENDA, BDConexao.getInstancia()).setVisible(true);
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }

    }//GEN-LAST:event_jButton4ActionPerformed

    private void sp_valor_entregueStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_sp_valor_entregueStateChanged
        // TODO add your handling code here:
        tratar_troco();

    }//GEN-LAST:event_sp_valor_entregueStateChanged

    private void sp_valor_entreguePropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_sp_valor_entreguePropertyChange
        // TODO add your handling code here:

    }//GEN-LAST:event_sp_valor_entreguePropertyChange

    private void sp_valor_entregueInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_sp_valor_entregueInputMethodTextChanged
        // TODO add your handling code here:

    }//GEN-LAST:event_sp_valor_entregueInputMethodTextChanged

    private void sp_valor_entregueKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_sp_valor_entregueKeyTyped
        // TODO add your handling code here:

    }//GEN-LAST:event_sp_valor_entregueKeyTyped

    private void sp_valor_entregueKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_sp_valor_entregueKeyPressed
        // TODO add your handling code here:

    }//GEN-LAST:event_sp_valor_entregueKeyPressed

    private void cmbFacturaCreditoActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbFacturaCreditoActionPerformed
    {//GEN-HEADEREND:event_cmbFacturaCreditoActionPerformed

        atualizarFormulario( SELECIONAR_FACTURA_DE_REF );
    }//GEN-LAST:event_cmbFacturaCreditoActionPerformed

    private void cmbCodigoProdutoActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbCodigoProdutoActionPerformed
    {//GEN-HEADEREND:event_cmbCodigoProdutoActionPerformed
        atualizarFormulario( SELECIONAR_PRODUTO );
    }//GEN-LAST:event_cmbCodigoProdutoActionPerformed

    private void txtCambioActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtCambioActionPerformed
    {//GEN-HEADEREND:event_txtCambioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCambioActionPerformed

    private void okJButtonMouseEntered(java.awt.event.MouseEvent evt)//GEN-FIRST:event_okJButtonMouseEntered
    {//GEN-HEADEREND:event_okJButtonMouseEntered
        atualizzarLBOperacoes( evt );
    }//GEN-LAST:event_okJButtonMouseEntered

    private void okJButtonMouseExited(java.awt.event.MouseEvent evt)//GEN-FIRST:event_okJButtonMouseExited
    {//GEN-HEADEREND:event_okJButtonMouseExited
        atualizzarLBOperacoes( evt );
    }//GEN-LAST:event_okJButtonMouseExited

    private void fecharJButtonMouseEntered(java.awt.event.MouseEvent evt)//GEN-FIRST:event_fecharJButtonMouseEntered
    {//GEN-HEADEREND:event_fecharJButtonMouseEntered
        atualizzarLBOperacoes( evt );
    }//GEN-LAST:event_fecharJButtonMouseEntered

    private void fecharJButtonMouseExited(java.awt.event.MouseEvent evt)//GEN-FIRST:event_fecharJButtonMouseExited
    {//GEN-HEADEREND:event_fecharJButtonMouseExited
        atualizzarLBOperacoes( evt );
    }//GEN-LAST:event_fecharJButtonMouseExited

    private void txtTotalApagar1ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtTotalApagar1ActionPerformed
    {//GEN-HEADEREND:event_txtTotalApagar1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTotalApagar1ActionPerformed

    private void sp_valor_entregue1StateChanged(javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_sp_valor_entregue1StateChanged
    {//GEN-HEADEREND:event_sp_valor_entregue1StateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_sp_valor_entregue1StateChanged

    private void sp_valor_entregue1InputMethodTextChanged(java.awt.event.InputMethodEvent evt)//GEN-FIRST:event_sp_valor_entregue1InputMethodTextChanged
    {//GEN-HEADEREND:event_sp_valor_entregue1InputMethodTextChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_sp_valor_entregue1InputMethodTextChanged

    private void sp_valor_entregue1PropertyChange(java.beans.PropertyChangeEvent evt)//GEN-FIRST:event_sp_valor_entregue1PropertyChange
    {//GEN-HEADEREND:event_sp_valor_entregue1PropertyChange
        // TODO add your handling code here:
    }//GEN-LAST:event_sp_valor_entregue1PropertyChange

    private void sp_valor_entregue1KeyTyped(java.awt.event.KeyEvent evt)//GEN-FIRST:event_sp_valor_entregue1KeyTyped
    {//GEN-HEADEREND:event_sp_valor_entregue1KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_sp_valor_entregue1KeyTyped

    private void sp_valor_entregue1KeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_sp_valor_entregue1KeyPressed
    {//GEN-HEADEREND:event_sp_valor_entregue1KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_sp_valor_entregue1KeyPressed

    private void txtClienteNome1ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtClienteNome1ActionPerformed
    {//GEN-HEADEREND:event_txtClienteNome1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtClienteNome1ActionPerformed

    private void cmbArmazem1ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbArmazem1ActionPerformed
    {//GEN-HEADEREND:event_cmbArmazem1ActionPerformed
        nota_debito_adicionar_preco_quantidade();
    }//GEN-LAST:event_cmbArmazem1ActionPerformed

    private void cmbFacturaDebitoActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbFacturaDebitoActionPerformed
    {//GEN-HEADEREND:event_cmbFacturaDebitoActionPerformed
        atualizarFormulario( ND_SELECIONAR_FACTURA_DE_REF );
    }//GEN-LAST:event_cmbFacturaDebitoActionPerformed

    private void txtCambio1ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtCambio1ActionPerformed
    {//GEN-HEADEREND:event_txtCambio1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCambio1ActionPerformed

    private void okJButton1MouseEntered(java.awt.event.MouseEvent evt)//GEN-FIRST:event_okJButton1MouseEntered
    {//GEN-HEADEREND:event_okJButton1MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_okJButton1MouseEntered

    private void okJButton1MouseExited(java.awt.event.MouseEvent evt)//GEN-FIRST:event_okJButton1MouseExited
    {//GEN-HEADEREND:event_okJButton1MouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_okJButton1MouseExited

    private void okJButton1ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_okJButton1ActionPerformed
    {//GEN-HEADEREND:event_okJButton1ActionPerformed
        procedimento_emitir_nota_debito();
    }//GEN-LAST:event_okJButton1ActionPerformed

    private void fecharJButton1MouseEntered(java.awt.event.MouseEvent evt)//GEN-FIRST:event_fecharJButton1MouseEntered
    {//GEN-HEADEREND:event_fecharJButton1MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_fecharJButton1MouseEntered

    private void fecharJButton1MouseExited(java.awt.event.MouseEvent evt)//GEN-FIRST:event_fecharJButton1MouseExited
    {//GEN-HEADEREND:event_fecharJButton1MouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_fecharJButton1MouseExited

    private void fecharJButton1ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_fecharJButton1ActionPerformed
    {//GEN-HEADEREND:event_fecharJButton1ActionPerformed
        dispose();
    }//GEN-LAST:event_fecharJButton1ActionPerformed

    private void principalJTabbedPaneStateChanged(javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_principalJTabbedPaneStateChanged
    {//GEN-HEADEREND:event_principalJTabbedPaneStateChanged
        atualizarFormulario( MUDAR_DE_ABA );
    }//GEN-LAST:event_principalJTabbedPaneStateChanged

    private void cmbFormaPagamento1ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbFormaPagamento1ActionPerformed
    {//GEN-HEADEREND:event_cmbFormaPagamento1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbFormaPagamento1ActionPerformed

    private void tabelaItensNotasPropertyChange(java.beans.PropertyChangeEvent evt)//GEN-FIRST:event_tabelaItensNotasPropertyChange
    {//GEN-HEADEREND:event_tabelaItensNotasPropertyChange
        atualizarFormulario( TABELA_ALTERADA );
    }//GEN-LAST:event_tabelaItensNotasPropertyChange

    private void cmbClienteActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbClienteActionPerformed
    {//GEN-HEADEREND:event_cmbClienteActionPerformed
        // TODO add your handling code here:
        accao_cliente();
    }//GEN-LAST:event_cmbClienteActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton adicionarJButton;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    public static javax.swing.JComboBox cmbArmazem;
    public static javax.swing.JComboBox cmbArmazem1;
    private static javax.swing.JComboBox cmbCliente;
    private static javax.swing.JComboBox cmbCodigoProduto;
    public static javax.swing.JComboBox cmbFacturaCredito;
    public static javax.swing.JComboBox cmbFacturaDebito;
    private static javax.swing.JComboBox cmbFormaPagamento;
    private javax.swing.JComboBox cmbFormaPagamento1;
    private com.toedter.calendar.JDateChooser dc_data_documento;
    private com.toedter.calendar.JDateChooser dc_data_factura_credito;
    private com.toedter.calendar.JDateChooser dc_data_factura_debito;
    private javax.swing.JButton fecharJButton;
    private javax.swing.JButton fecharJButton1;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JLabel lbCategoria1;
    private javax.swing.JLabel lbCategoria2;
    private javax.swing.JLabel lbCliente;
    private javax.swing.JLabel lbCliente1;
    private javax.swing.JLabel lbCodigoProduto;
    private javax.swing.JLabel lbCodigoProduto1;
    private javax.swing.JLabel lbFormaPagamento;
    private javax.swing.JLabel lbFormaPagamento1;
    private javax.swing.JLabel lbPreco;
    private javax.swing.JLabel lbPreco1;
    private javax.swing.JLabel lbPreco4;
    private javax.swing.JLabel lbPreco5;
    private javax.swing.JLabel lbPreco7;
    private javax.swing.JLabel lbPreco8;
    private javax.swing.JLabel lbPreco9;
    private javax.swing.JLabel lbProduto;
    private javax.swing.JLabel lbQuantidade;
    private javax.swing.JLabel lbQuantidadeStock;
    private javax.swing.JLabel lbTotalPagar;
    private javax.swing.JLabel lbTotalPagar1;
    private javax.swing.JLabel lbTroco;
    private javax.swing.JLabel lbTroco1;
    private javax.swing.JLabel lbValorEnregue;
    private javax.swing.JLabel lbValorEnregue1;
    public static javax.swing.JLabel lbValorPorExtenco;
    public static javax.swing.JLabel lbValorPorExtenco1;
    private javax.swing.JLabel lb_ano_academico;
    private javax.swing.JLabel lb_ano_academico1;
    private javax.swing.JLabel lb_cambio;
    private javax.swing.JLabel lb_cambio1;
    private javax.swing.JLabel lb_proximo_documento;
    private javax.swing.JLabel lb_proximo_documento1;
    private javax.swing.JLabel lb_usuario;
    private javax.swing.JLabel lb_usuario1;
    private javax.swing.JTextArea motivoJTextArea;
    private javax.swing.JTextArea motivoJTextArea1;
    private javax.swing.JPanel notasCreditoJPanel;
    private javax.swing.JPanel notasDebitoJPanel;
    private javax.swing.JButton okJButton;
    private javax.swing.JButton okJButton1;
    private javax.swing.JLabel operacaoJLabel;
    private javax.swing.JLabel operacaoJLabel1;
    private javax.swing.JTabbedPane principalJTabbedPane;
    private javax.swing.JButton removerProdutoJButton;
    private static javax.swing.JSpinner sp_valor_entregue;
    private static javax.swing.JSpinner sp_valor_entregue1;
    public static javax.swing.JTable tabelaItensNotas;
    private static javax.swing.JTextField txtCambio;
    private static javax.swing.JTextField txtCambio1;
    private static javax.swing.JTextField txtCategoria;
    private javax.swing.JTextField txtClienteNome;
    private javax.swing.JTextField txtClienteNome1;
    public static javax.swing.JTextField txtCodigoBarra;
    private static javax.swing.JTextField txtMoeda;
    private static javax.swing.JTextField txtMoeda1;
    public static javax.swing.JTextField txtPreco;
    private static javax.swing.JTextField txtProduto;
    public static javax.swing.JTextField txtQuantidaStock;
    private static javax.swing.JSpinner txtQuatindade;
    public static javax.swing.JTextField txtTotalApagar;
    public static javax.swing.JTextField txtTotalApagar1;
    public static javax.swing.JTextField txtTroco;
    public static javax.swing.JTextField txtTroco1;
    // End of variables declaration//GEN-END:variables

    //verifica se o produto existe na tabela do formulário visão isto é na jTable
    private static boolean exist_produto_tabela_formulario( int codigo )
    {
        DefaultTableModel modelo = (DefaultTableModel) tabelaItensNotas.getModel();

        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {
            if ( Integer.parseInt( String.valueOf( tabelaItensNotas.getValueAt( i, 0 ) ) ) == codigo )
            {
                linha_actual = i;
                return true;
            }
        }
        return false;

    }

    //actualiza a quantidade na tabela do formulário visão isto é na jTable
    private static void actuazlizar_quantidade_tabela_formulario( String quantidade, double desconto )
    {
        DefaultTableModel modelo = (DefaultTableModel) tabelaItensNotas.getModel();
        double preco_venda = Double.parseDouble( String.valueOf( modelo.getValueAt( linha_actual, 2 ) ) );
        double total_item = preco_venda * Double.parseDouble( quantidade );
        total_item = total_item - desconto;
        modelo.setValueAt( quantidade, linha_actual, 3 );
        modelo.setValueAt( total_item, linha_actual, 5 );
        //a linha_actual recebe o default
        linha_actual = -1;

    }

    private void accao_cliente()
    {
        if ( cmbCliente.getSelectedItem().equals( "DIVERSOS" ) )
        {
            txtClienteNome.setVisible( true );
            txtClienteNome.requestFocus();
        }
        else
        {
            txtClienteNome.setVisible( false );
        }
    }

    public static void procedimento_adicionar()
    {

        try
        {
            if ( !campos_invalidos() )
            {

                if ( isStocavel( produtoDao.findTbProduto( getFkProduto() ).getStocavel() ) )
                {

                    if ( estado_critico() )
                    {
                        JOptionPane.showMessageDialog( null, "O produto: " + produtoDao.findTbProduto( getFkProduto() ).getDesignacao() + " precisa de ser actualizado no stock", DVML.DVML_COMERCIAL, JOptionPane.WARNING_MESSAGE );
                    }

                    adicionar_produto();

                }
                else
                {
                    adicionar_produto();
                }

            }
            else
            {
                JOptionPane.showMessageDialog( null, "Por Favor Digite a Quantidade" );
            }

        }
        catch ( SQLException ex )
        {

        }

    }

    private void accao_codigo_barra_enter()
    {
        try
        {

            String codigo_barra = txtCodigoBarra.getText().trim();

            TbProduto produto = produtoDao.getProdutoByCodigoBarra( codigo_barra );

            txtCategoria.setText( produto.getCodTipoProduto().getDesignacao() );
            //Devo setar a combo dos produtos (Isto porque quando a se faz a busca na cmbCategoria remove todos os produtos exceptos os de categoria actual)
            txtProduto.setText( produto.getDesignacao() );

            adicionar_preco_quantidade_anitgo();
            procedimento_adicionar();

            txtCodigoBarra.setText( "" );
            txtQuatindade.setValue( 1.0 );
            txtCodigoBarra.requestFocus();

        }
        catch ( Exception ex )
        {
            ex.printStackTrace();
            Logger.getLogger( NotasCreditoDebitoVisao.class.getName() ).log( Level.SEVERE, null, ex );
            JOptionPane.showMessageDialog( null, "Não existe produto com este código de barra.", DVML.DVML_COMERCIAL, JOptionPane.ERROR_MESSAGE );
        }

    }

    @Override
    public void run()
    {

    }

    public void actualizar_quantidade( double quantidade, TbStock stock )
    {

        double qtd = ( stock.getQuantidadeExistente() - quantidade );
        stock.setQuantidadeExistente( qtd );
        try
        {
            stockDao.edit( stock );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            System.err.println( "Falha ao actualizar a quantidade do produto no stock" );
        }

    }

    private void mostrar_nome( JLabel lb_usuario )
    {
        TbUsuario usuario = usuarioDao.findTbUsuario( this.cod_usuario );
        //caso masculino
        if ( usuario.getCodigoSexo().getCodigo() == 1 )
        {
            lb_usuario.setText( "Operador: " + usuario.getNome() );
        }
        else
        {
            lb_usuario.setText( "Operadora: " + usuario.getNome() );
        }
    }

    public void adicionar_preco_quantidade()
    {
        try
        {
            Integer codigoArmazem = getVenda( cmbFacturaCredito ).getIdArmazemFK().getCodigo();

            if ( stockDao.get_stock_by_id_produto_and_id_armazem( getFkProduto(), codigoArmazem ).getQuantidadeExistente() <= stockDao.get_stock_by_id_produto_and_id_armazem( getFkProduto(), codigoArmazem ).getQuantCritica() )
            {

                txtQuantidaStock.setBackground( Color.RED );
                txtQuantidaStock.setForeground( Color.BLACK );
            }
            else
            {
                txtQuantidaStock.setBackground( new Color( 51, 153, 0, 255 ) );
            }

            TbProduto produto = produtoDao.findTbProduto( getFkProduto() );

            txtPreco.setText( String.valueOf( precoDao.findTbPreco( precoDao.getUltimoIdPrecoByIdProduto( produto.getCodigo() ) ).getPrecoVenda() ) );
            txtCodigoBarra.setText( String.valueOf( produto.getCodBarra() ) );
            cmbCodigoProduto.setSelectedItem( String.valueOf( produto.getCodigo() ) );
            if ( stockDao.exist_produto_stock( getFkProduto(), codigoArmazem ) )
            {
                txtQuantidaStock.setText( String.valueOf( stockDao.get_stock_by_id_produto_and_id_armazem( getFkProduto(), codigoArmazem ).getQuantidadeExistente() ) );

            }
            else
            {
                txtQuantidaStock.setText( "0" );
            }

        }
        catch ( Exception ex )
        {
            ex.printStackTrace();
            txtQuantidaStock.setText( "0" );
            Logger.getLogger( NotasCreditoDebitoVisao.class.getName() ).log( Level.SEVERE, null, ex );
        }

    }

    public void nota_debito_adicionar_preco_quantidade()
    {
        try
        {
            Integer codigoArmazem = getVenda( cmbFacturaCredito ).getIdArmazemFK().getCodigo();

            if ( stockDao.get_stock_by_id_produto_and_id_armazem( getFkProduto(), codigoArmazem ).getQuantidadeExistente() <= stockDao.get_stock_by_id_produto_and_id_armazem( getFkProduto(), codigoArmazem ).getQuantCritica() )
            {

                txtQuantidaStock.setBackground( Color.RED );
                txtQuantidaStock.setForeground( Color.BLACK );
            }
            else
            {
                txtQuantidaStock.setBackground( new Color( 51, 153, 0, 255 ) );
            }

            TbProduto produto = produtoDao.findTbProduto( getFkProduto() );

            txtPreco.setText( String.valueOf( precoDao.findTbPreco( precoDao.getUltimoIdPrecoByIdProduto( produto.getCodigo() ) ).getPrecoVenda() ) );
            txtCodigoBarra.setText( String.valueOf( produto.getCodBarra() ) );
            cmbCodigoProduto.setSelectedItem( String.valueOf( produto.getCodigo() ) );
            if ( stockDao.exist_produto_stock( getFkProduto(), codigoArmazem ) )
            {
                txtQuantidaStock.setText( String.valueOf( stockDao.get_stock_by_id_produto_and_id_armazem( getFkProduto(), codigoArmazem ).getQuantidadeExistente() ) );

            }
            else
            {
                txtQuantidaStock.setText( "0" );
            }

        }
        catch ( Exception ex )
        {
            ex.printStackTrace();
            txtQuantidaStock.setText( "0" );
            Logger.getLogger( NotasCreditoDebitoVisao.class.getName() ).log( Level.SEVERE, null, ex );
        }

    }

    public static void adicionar_preco_quantidade_anitgo()
    {

        try
        {
            Integer codigoArmazem = getVenda( cmbFacturaCredito ).getCodigo();

            // TODO add your handling code here
            //if(BDConexao.getCodigoByCodigo("tb_stock", "quantidade_existente", "cod_produto_codigo", getFkProduto())<=5)
            //if (conexao.getQtdExistenteStock(getFkProduto(), getCodigoArmazem()) <= conexao.getQtdCriticaStock(getFkProduto(), getCodigoArmazem())) {
            TbStock stock = stockDao.get_stock_by_id_produto_and_id_armazem( getFkProduto(), codigoArmazem );
            if ( stock.getQuantidadeExistente() <= stock.getQuantCritica() )
            {
                txtQuantidaStock.setBackground( Color.RED );
                txtQuantidaStock.setForeground( Color.BLACK );
            } //[51,153,0]
            else
            {
                txtQuantidaStock.setBackground( new Color( 51, 153, 0, 255 ) );
            }

            txtQuantidaStock.setText( String.valueOf( conexao.getQtdExistenteStock( getFkProduto(), codigoArmazem ) ) );

            txtPreco.setText( String.valueOf( MetodosUtil.retirar_dizimas( getPrecoProduto( getFkProduto(), getQuantidade() ).getPrecoVenda().doubleValue() ) ) );

        }
        catch ( Exception ex )
        {
            Logger.getLogger( NotasCreditoDebitoVisao.class.getName() ).log( Level.SEVERE, null, ex );
        }

    }

    public void adicionar_botao()
    {
        try
        {
            System.err.println( "VALIDAR CAMPOS " );
            if ( !campos_invalidos() )
            {
                if ( isStocavel( new ProdutoController( conexao ).getProduto( getFkProduto() ).getStocavel() ) )
                {
                    if ( estado_critico() )
                    {
                        JOptionPane.showMessageDialog( null, "O produto: " + new ProdutoController( conexao ).getProduto( getFkProduto() ).getDesignacao() + " precisa de ser actualizado no stock", "DVML", JOptionPane.WARNING_MESSAGE );
                    }

                }

                System.err.println( "---CAMPOS VALIDOS" );
                adicionar_produto();

            }
            else
            {
                JOptionPane.showMessageDialog( null, "Por Favor Digite a Quantidade" );
            }

        }
        catch ( SQLException ex )
        {

        }

        atualizarBotaoSalvar( okJButton );

    }

    public static boolean validar_zero()
    {
        return (int) txtQuatindade.getValue() == 0;
    }

    public void limpar()
    {

        txtQuatindade.setModel( criarSpinnerNumberModel( 0, 0, 0 ) );;
        txtClienteNome.setText( "DIVERSOS" );
        txtClienteNome1.setText( "DIVERSOS" );
        cmbCodigoProduto.setSelectedIndex( -1 );
        sp_valor_entregue.setValue( 0.0 );
        sp_valor_entregue1.setValue( 0.0 );

        txtTotalApagar.setText( CfMethods.formatarComoMoeda( 0.0 ) );
        txtTotalApagar1.setText( CfMethods.formatarComoMoeda( 0.0 ) );
        //txtDesconto.setText("0");

        txtTroco.setText( CfMethods.formatarComoMoeda( 0.0 ) );
        txtTroco1.setText( CfMethods.formatarComoMoeda( 0.0 ) );
        soma_total = 0;
        txtCodigoBarra.setText( "" );

    }

    public boolean campos_invalido_imprimir()
    {

        if ( txtClienteNome.getText().equals( "" ) || txtClienteNome.getText() == null )
        {
            txtClienteNome.requestFocus();
            JOptionPane.showMessageDialog( null, "Por favor digite o nome do cliente", "AVISO", JOptionPane.WARNING_MESSAGE );
            return true;
        }

        return false;

    }

    public boolean campos_invalido_imprimir1()
    {
        if ( txtClienteNome1.getText().equals( "" ) || txtClienteNome1.getText() == null )
        {
            txtClienteNome1.requestFocus();
            JOptionPane.showMessageDialog( null, "Por favor digite o nome do cliente", "AVISO", JOptionPane.WARNING_MESSAGE );
            return true;
        }

        return false;
    }

    public boolean possivel_quantidade( int cod_produto, double qtd )
    {
        //System.err.println(conexao.getQuantidade_Existente_Publico(getFkProduto(), getCodigoArmazem()));  
        //  TbStock stock =  stockDao.getStockByDescricao(getFkProduto(), getCodigoArmazem() );
        double quant_possivel = conexao.getQuantidade_Existente_Publico( cod_produto, getVenda( cmbFacturaCredito ).getIdArmazemFK().getCodigo() ) - conexao.getQuantidade_minima_publico( cod_produto, getVenda( cmbFacturaCredito ).getIdArmazemFK().getCodigo() );
        //int quant_possivel = stock.getQuantidadeExistente() -  stock.getQuantBaixa();

        return quant_possivel >= qtd;
    }

    private boolean transtorno()
    {

        int cod_produto = 0;
        double qtd = 0d, qtd_aceite = 0d;

        DefaultTableModel modelo = (DefaultTableModel) tabelaItensNotas.getModel();
        boolean transtorno = false;

        for ( int i = 0; i < tabelaItensNotas.getRowCount(); i++ )
        {

            cod_produto = Integer.parseInt( String.valueOf( tabelaItensNotas.getModel().getValueAt( i, 0 ) ) );
            qtd = Double.parseDouble( String.valueOf( tabelaItensNotas.getModel().getValueAt( i, 3 ) ) );

            if ( !possivel_quantidade( cod_produto, qtd ) )
            {

                transtorno = true;
                //qtd_aceite = conexao.getQuantidade_Existente_Publico(cod_produto, getCodigoArmazem());
                qtd_aceite = stockDao.getStockByDescricao( cod_produto, getVenda( cmbFacturaCredito ).getIdArmazemFK().getCodigo() ).getQuantidadeExistente();

                if ( qtd_aceite != 0 )
                {

                    int opcao = JOptionPane.showConfirmDialog( null, "Desculpe pelo transtorno, o produto " + produtoDao.findTbProduto( cod_produto ).getDesignacao() + " só é possivel  a " + qtd_aceite + " quantidade(s)" + ", contrariamente de " + qtd + "\n Deseja actualizar ou remover da tabela ?" );

                    if ( opcao == JOptionPane.YES_OPTION )
                    {

                        modelo.setValueAt( qtd_aceite, i, 3 );
                        double valor_iva = 0, taxa = 0, desconto = 0;
                        taxa = Double.parseDouble( modelo.getValueAt( i, 5 ).toString() );
                        desconto = Double.parseDouble( modelo.getValueAt( i, 4 ).toString() );
                        modelo.setValueAt( ( qtd_aceite * ( getPreco( cod_produto, qtd_aceite ) + getValorIVA( taxa, getPreco( cod_produto, qtd_aceite ) ) ) - desconto ), i, 7 );

                    }
                    else
                    {
                        modelo.removeRow( i );
                    }
                    setTotalPagar();
                    valor_por_extenco();
                    sp_valor_entregue.setValue( 0.0 );
                    txtTroco.setText( CfMethods.formatarComoMoeda( 0.0 ) );
                    adicionar_preco_quantidade_anitgo();

                }
                else
                {
                    modelo.removeRow( i );
                    adicionar_preco_quantidade_anitgo();
                    JOptionPane.showMessageDialog( null, "Desculpe pelo transtorno o produto " + produtoDao.findTbProduto( cod_produto ).getDesignacao() + " já não se encontra disponível no stock", DVML.DVML_COMERCIAL, JOptionPane.WARNING_MESSAGE );
                }

            }

        }
        return transtorno;

    }

    private Documento getDocumento()
    {
        int idDocumento = getAbreviacaoDocumento() == ND ? DOC_NOTA_DEBITO_ND : DOC_NOTA_CREDITO_NC;
        return new DocumentoDao( emf ).findDocumento( idDocumento );
    }

    private void procedimento_emitir_nota_credito()
    {
        TbVenda venda = getVenda( cmbFacturaCredito );
        Date data_actual = dc_data_documento.getDate();
        Date documento_ref_data = venda.getDataVenda();
        if ( documento_ref_data != null )
        {
            if ( MetodosUtil.menor_data_1_data_2( data_actual, documento_ref_data ) )
            {
                JOptionPane.showMessageDialog( null, "Caro usário verifiue a data do sistema", "AVISO", JOptionPane.WARNING_MESSAGE );
            }
            else
            {
                salvarNotaCredito();

            }

        }

    }

    private void procedimento_emitir_nota_debito()
    {
        TbVenda venda = getVenda( cmbFacturaDebito );
        Date data_actual = dc_data_documento.getDate();
        Date documento_ref_data = venda.getDataVenda();
        if ( documento_ref_data != null )
        {
            if ( MetodosUtil.menor_data_1_data_2( data_actual, documento_ref_data ) )
            {
                JOptionPane.showMessageDialog( null, "Caro usário verifiue a data do sistema", "AVISO", JOptionPane.WARNING_MESSAGE );
            }
            else
            {
                salvarNotaDebito();
            }
        }

    }

    public void salvarNotaCredito()
    {
        if ( !campos_invalido_imprimir() )
        {
            if ( !txtClienteNome.getText().equals( "" ) )
            {
                if ( !transtorno() )
                {
                    System.out.println( "STATUS: a processar a factura" );
                    EntityManager em = JPAEntityMannagerFactoryUtil.createEntityManager();
                    em.getTransaction().begin();

                    TbVenda venda = getVenda( cmbFacturaCredito );
                    Notas notas_local = getNotaCreditoModel();

                    try
                    {
                        notasDao.create( notas_local );
                        //actualiza a data para o próximo documento
                        dc_data_documento.setDate( new Date() );
                        this.motivos_isentos = "";
                        System.out.println( "STATUS:factura criada com sucesso." );
                        boolean documentosalvoComSucesso = salvarItemNota( notasDao.getLastNota() );

                        if ( documentosalvoComSucesso )
                        {
                            actualizar_cod_doc();
                            HashMap hashMap = new HashMap();
                            String telefone = venda.getCodigoCliente().getTelefone();
                            String morada = venda.getCodigoCliente().getMorada();
                            String email = venda.getCodigoCliente().getEmail();

//                            List<TbProduto> lista_produto_isentos = new ArrayList<>();
//                            lista_produto_isentos = getProdutosIsentos();
//                            String motivos_isentos = MetodosUtil.getMotivoIsensaoProdutos( lista_produto_isentos );
//                            System.err.println( "MOTIVOS: " + motivos_isentos );
//                    boolean clienteConsumidorFinal = venda.getCodigoCliente ().getNome ().equalsIgnoreCase ( "Consumidor Final" );
                            boolean clienteNaoInformouOTelefone = telefone == null || telefone.isEmpty();
                            boolean clienteNaoInformouAMorada = morada == null || morada.isEmpty();
                            boolean clienteNaoInformouOEmail = email == null || email.isEmpty();
//                    hashMap.put ( "_CLIENTE_NOME", DVML._NAO_INCLUIR );
//                    hashMap.put ( "_CLIENTE_NIF", DVML._NAO_INCLUIR );

                            if ( clienteNaoInformouOEmail )
                            {
                                hashMap.put( "_CLIENTE_EMAIL", DVML._NAO_INCLUIR );
                            }
                            if ( clienteNaoInformouOTelefone )
                            {
                                hashMap.put( "_CLIENTE_TELEFONE", DVML._NAO_INCLUIR );
                            }
                            if ( clienteNaoInformouAMorada )
                            {
                                hashMap.put( "_CLIENTE_MORADA", DVML._NAO_INCLUIR );
                            }

                            //Chama a factura e imprime directamente para a imprissora que estiver devenidade no sistema operativo
                            new ListaNotaDebito( notasDao.getLastNota(), getAbreviacaoDocumento(), false, false, hashMap, this.motivos_isentos );

                        }

                        System.out.println( "STATUS:itens adicionado na facrtura com sucesso." );
                    }
                    catch ( Exception e )
                    {
                        System.err.println( "STATUS: falha ao actualizar a factura" );
                        e.printStackTrace();
                        JOptionPane.showMessageDialog( null, "Falha ao Processar a Factura", "FALHA", JOptionPane.ERROR_MESSAGE );
                    }

                    em.getTransaction().commit();
                    System.out.println( "STATUS: fim do processamento da factura" );
                }
            }
            else
            {
                txtClienteNome.requestFocus();
                JOptionPane.showMessageDialog( null, "Pf. Digite o Nome do Cliente!...", "AVISO", JOptionPane.WARNING_MESSAGE );
            }
        }

        atualizarFormulario( SALVAR_REGISTRO );

    }

    public void salvarNotaDebito()
    {
        if ( !campos_invalido_imprimir1() )
        {

            if ( !transtorno() )
            {
                System.out.println( "STATUS: a processar a factura" );
                EntityManager em = JPAEntityMannagerFactoryUtil.createEntityManager();
                em.getTransaction().begin();

                TbVenda venda = getVenda( cmbFacturaDebito );
                Notas notas_local = getNotaDebitoModel();

                try
                {
                    //#data
                    notasDao.create( notas_local );
                    //actualiza a data para o próximo documento
                    dc_data_documento.setDate( new Date() );
                    System.out.println( "STATUS:factura criada com sucesso." );
                    boolean documentosalvoComSucesso = salvarNotaDebitoItemNota( notasDao.getLastNota() );

                    if ( documentosalvoComSucesso )
                    {

                        actualizar_cod_doc();
                        HashMap hashMap = new HashMap();
                        String telefone = venda.getCodigoCliente().getTelefone();
                        String morada = venda.getCodigoCliente().getMorada();
                        String email = venda.getCodigoCliente().getEmail();

                        List<TbProduto> lista_produto_isentos = new ArrayList<>();
                        lista_produto_isentos = getProdutosIsentos();
                        String motivos_isentos = MetodosUtil.getMotivoIsensaoProdutos( lista_produto_isentos );
                        System.err.println( "MOTIVOS: " + motivos_isentos );

//                    boolean clienteConsumidorFinal = venda.getCodigoCliente ().getNome ().equalsIgnoreCase ( "Consumidor Final" );
                        boolean clienteNaoInformouOTelefone = telefone == null || telefone.isEmpty();
                        boolean clienteNaoInformouAMorada = morada == null || morada.isEmpty();
                        boolean clienteNaoInformouOEmail = email == null || email.isEmpty();
//                    hashMap.put ( "_CLIENTE_NOME", DVML._NAO_INCLUIR );
//                    hashMap.put ( "_CLIENTE_NIF", DVML._NAO_INCLUIR );

                        if ( clienteNaoInformouOEmail )
                        {
                            hashMap.put( "_CLIENTE_EMAIL", DVML._NAO_INCLUIR );
                        }
                        if ( clienteNaoInformouOTelefone )
                        {
                            hashMap.put( "_CLIENTE_TELEFONE", DVML._NAO_INCLUIR );
                        }
                        if ( clienteNaoInformouAMorada )
                        {
                            hashMap.put( "_CLIENTE_MORADA", DVML._NAO_INCLUIR );
                        }

                        //Chama a factura e imprime directamente para a imprissora que estiver devenidade no sistema operativo
                        new ListaNotaDebito( notasDao.getLastNota(), getAbreviacaoDocumento(), false, false, hashMap, motivos_isentos );
                    }

                    System.out.println( "STATUS:itens adicionado na facrtura com sucesso." );
                }
                catch ( Exception e )
                {
                    System.err.println( "STATUS: falha ao actualizar a factura" );
                    e.printStackTrace();
                    JOptionPane.showMessageDialog( null, "Falha ao Processar a Factura", "FALHA", JOptionPane.ERROR_MESSAGE );
                }

                em.getTransaction().commit();
                System.out.println( "STATUS: fim do processamento da factura" );
            }

        }

        atualizarFormulario( SALVAR_REGISTRO );

    }


    /* CRIACAO DO GETS  */
    public static int getQuantidade()
    {
        System.err.println( "QUANTIDADE DIGTADA:" + txtQuatindade.getValue() );
        return (int) txtQuatindade.getValue();
    }

    public static int getIdCliente()
    {
        try
        {
            return clienteDao.getClienteByNome( cmbCliente.getSelectedItem().toString() ).getCodigo();
        }
        catch ( Exception e )
        {
            return 0;
        }

    }

    public int getIdMoeda()
    {
        try
        {
            return moedaDao.getIdByDescricao( txtMoeda.getText() );
        }
        catch ( Exception e )
        {
            return 0;
        }

    }

    public static String getDescricao_Produto()
    {
        return produtoDao.findTbProduto( getFkProduto() ).getDesignacao();
    }

    public static boolean campos_invalidos()
    {
        return txtQuatindade.getValue().equals( "" );
    }

    public void calcularTroco()
    {

        String prefixo = "";
        double troco = 0;

        System.out.println( "VALOR ENTREGUE " + sp_valor_entregue.getValue() );
        System.out.println( "TOTAL A PAGAR " + txtTotalApagar.getText().trim() );

        double valor_entregue = (double) sp_valor_entregue.getValue();
        double total_pagar = Double.parseDouble( txtTotalApagar.getText().trim() );
        troco = valor_entregue - total_pagar;

        System.out.println( "TROCO " + troco );
        txtTroco.setText( String.valueOf( MetodosUtil.retirar_dizimas( troco ) ).trim() );

    }

    private void tratar_troco()
    {
        try
        {
            double troco = 0.0;
            double total_pagar = CfMethods.parseMoedaFormatada( txtTotalApagar.getText() );
            double valor_entregue = (double) sp_valor_entregue.getValue();
            troco = valor_entregue - total_pagar;
            txtTroco.setText( CfMethods.formatarComoMoeda( troco ) );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }

    }

    private void atualizarCMBVendas( JComboBox cmbFactura )
    {
        VendaDao tbVendaDao = new VendaDao( emf );

        Object[] listarVendaDoDocumento = tbVendaDao.listarVendaDoDocumentoValidas();

        if ( listarVendaDoDocumento != null )
        {
            cmbFactura.setModel( new DefaultComboBoxModel( listarVendaDoDocumento ) );
            System.err.println( "Atualizar!" );
        }
        else
        {
            System.err.println( "Nao - Atualizar!" );
        }

    }

    private void atualizarArmazem( JComboBox cmbFactura )
    {
        String facturaSelecionada = (String) cmbFactura.getSelectedItem();
        Object[] buscaDaVenda = armazemDao.buscaDaVenda( facturaSelecionada );

        if ( buscaDaVenda != null )
        {
            cmbArmazem.setModel( new DefaultComboBoxModel( buscaDaVenda ) );
        }

    }

    private void atualizarCodigoProduto()
    {
        String codigoFactura = (String) cmbFacturaCredito.getSelectedItem();
        ProdutoDao dao = new ProdutoDao( emf );
        List<TbProduto> produtosDaVenda = dao.getAllByVenda( codigoFactura );

        if ( produtosDaVenda != null )
        {
            DefaultComboBoxModel defaultComboBoxModel = new DefaultComboBoxModel();

            defaultComboBoxModel.addElement( "--Selecione--" );
            for ( TbProduto tbProduto : produtosDaVenda )
            {
                defaultComboBoxModel.addElement( tbProduto.getCodigo() );
            }

            cmbCodigoProduto.setModel( defaultComboBoxModel );
        }
    }

    private static TbProduto getProdutoSelecionado()
    {
        if ( cmbCodigoProduto.getSelectedIndex() > 0 )
        {
            Integer selectedItem = (Integer) cmbCodigoProduto.getSelectedItem();

            if ( selectedItem != null )
            {
                String codigoProutoSelcionadoText = String.valueOf( selectedItem );

                int codigoProutoSelcionado = Integer.parseInt( codigoProutoSelcionadoText );

                return produtoDao.findTbProduto( codigoProutoSelcionado );
            }
        }
        else
        {
            limparFormulario();
        }

        return null;
    }

    private static TbVenda getFaturaSlecionada()
    {
        String codFact = (String) cmbFacturaCredito.getSelectedItem();
        return new VendaDao( emf ).findByCodFact( codFact );
    }

    private static TbItemVenda getTbItemVendaSelecionado()
    {
        TbProduto produtoSelecionado = getProdutoSelecionado();

        if ( produtoSelecionado != null )
        {
            TbVenda venda = getFaturaSlecionada();
            return new ItemVendaDao( emf ).getlItemVendasByIdVendaAndPrduto( venda.getCodigo(), produtoSelecionado.getCodigo() );
        }

        return null;
    }

    private void atualizarDadosProdutosPelaCombo()
    {
        if ( cmbCodigoProduto.getSelectedIndex() > 0 )
        {
            String codigoProutoSelcionadoText = String.valueOf( cmbCodigoProduto.getSelectedItem() );
            int codigoProutoSelcionado = Integer.parseInt( codigoProutoSelcionadoText );
            String codFact = (String) cmbFacturaCredito.getSelectedItem();
            TbVenda venda = new VendaDao( emf ).findByCodFact( codFact );
            TbProduto produto = produtoDao.findTbProduto( codigoProutoSelcionado );

            ItemVendaDao itemVendaDao = new ItemVendaDao( emf );

            TbItemVenda itenVenda = itemVendaDao.getlItemVendasByIdVendaAndPrduto( venda.getCodigo(), produto.getCodigo() );

            txtMoeda.setText( venda.getFkCambio().getFkMoeda().getDesignacao() );
            txtCategoria.setText( produto.getCodTipoProduto().getDesignacao() );
            txtProduto.setText( produto.getDesignacao() );
            txtQuatindade.setValue( itenVenda.getQuantidade() );
            txtCodigoBarra.setText( String.valueOf( produto.getCodBarra() ) );
            txtPreco.setText( String.valueOf( itenVenda.getFkPreco().getPrecoVenda() ) );
            txtClienteNome.setText( venda.getNomeCliente() );
            cmbCliente.setSelectedItem( venda.getCodigoCliente().getNome() );
            TbStock stock = stockDao.get_stock_by_id_produto_and_id_armazem( getFkProduto(), getVenda( cmbFacturaCredito ).getIdArmazemFK().getCodigo() );

            if ( stock.getQuantidadeExistente() <= stock.getQuantCritica() )
            {
                txtQuantidaStock.setBackground( Color.RED );
                txtQuantidaStock.setForeground( Color.BLACK );
            }
            else
            {
                txtQuantidaStock.setBackground( new Color( 51, 153, 0, 255 ) );
            }

            if ( stockDao.exist_produto_stock( getFkProduto(), getVenda( cmbFacturaCredito ).getIdArmazemFK().getCodigo() ) )
            {
                txtQuantidaStock.setText( String.valueOf( stockDao.get_stock_by_id_produto_and_id_armazem( getFkProduto(), getVenda( cmbFacturaCredito ).getIdArmazemFK().getCodigo() ).getQuantidadeExistente() ) );

            }
            else
            {
                txtQuantidaStock.setText( "0" );
            }
        }
        else
        {
            limparFormulario();
        }
    }

    private void calculaTotalIVA()
    {
        DefaultTableModel modelo = (DefaultTableModel) tabelaItensNotas.getModel();
        double iva = 0, preco = 0;
        int qtd = 0;

        total_iva = 0;
        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {

            preco = Double.parseDouble( modelo.getValueAt( i, 2 ).toString() );
            qtd = Integer.parseInt( modelo.getValueAt( i, 3 ).toString() );
            iva = Double.parseDouble( modelo.getValueAt( i, 5 ).toString() );
            iva = ( iva / 100 );

            total_iva += ( iva * preco ) * qtd;

        }
        System.out.println( "TOTAL IVA: " + total_iva );
    }

    private ESTADO_NOTA defininirEstadoNotas( TbVenda venda )
    {
        List<TbItemVenda> tbItemVendaList = venda.getTbItemVendaList();
        ESTADO_NOTA estado_nota = null;

        if ( tabelaItensNotas.getRowCount() == tbItemVendaList.size() )
        {
            DefaultTableModel model = (DefaultTableModel) tabelaItensNotas.getModel();
            boolean anulado = true;

            for ( int i = 0; i < model.getRowCount(); i++ )
            {
                double valueAt = (double) model.getValueAt( i, 4 );
                anulado = anulado && ( valueAt > 0 );
            }

            if ( anulado )
            {
                estado_nota = ESTADO_NOTA.ANULADO;
            }
            else
            {
                estado_nota = ESTADO_NOTA.TOTALMENTE_RETIFICADO;
            }
        }
        else
        {
            estado_nota = ESTADO_NOTA.RETIFICADO;
        }

        return estado_nota;
    }

    private static void limparFormulario()
    {

    }

    private void atualizarMoeda( JComboBox cmbFactura, JTextField txtMoeda, JTextField txtCambio )
    {
        TbVenda venda = getVenda( cmbFactura );

        if ( venda != null )
        {
            Cambio fkCambio = venda.getFkCambio();
            txtMoeda.setText( fkCambio.getFkMoeda().getDesignacao() );
            txtCambio.setText( CfMethods.formatarComoMoeda( fkCambio.getValor() ) );
        }
    }

    private void atualizzarLBOperacoes( MouseEvent evt )
    {
        JButton source = (JButton) evt.getSource();

        int id = evt.getID();

        switch ( id )
        {
            case MouseEvent.MOUSE_ENTERED:
            {
                int tamanho = 10;
                operacaoJLabel.setText( source.getName() );
                resizeJLableIcon( tamanho, tamanho, getClass().getResource( "/imagens/img32x32/_info.png" ), operacaoJLabel );
            }
            break;
            default:
            {
                operacaoJLabel.setIcon( null );
                operacaoJLabel.setText( "" );
            }
            break;
        }
    }

    private void atualizarFormulario( EVENTO_DO_SISTEMA evento_do_sistema )
    {
        switch ( evento_do_sistema )
        {
            case SELECIONAR_PRODUTO_PELO_COD_BARRA:
            {
                accao_codigo_barra_enter();
            }
            break;
            case MUDAR_DE_ABA:
            {

            }
            break;

            case TABELA_ALTERADA:
            {

            }
            break;

            case SELECIONAR_FACTURA_DE_REF:
            {
                atualizarArmazem( cmbFacturaCredito );
                atualizarCodigoProduto();
                atualizarMoeda( cmbFacturaCredito, txtMoeda, txtCambio );
                TbVenda venda = getVenda( cmbFacturaCredito );
                if ( venda != null )
                {
                    dc_data_factura_credito.setDate( venda.getDataVenda() );
                }
            }
            break;
            case ND_SELECIONAR_FACTURA_DE_REF:
            {
                atualizarArmazem( cmbFacturaDebito );
                atualizarMoeda( cmbFacturaDebito, txtMoeda1, txtCambio1 );
                TbVenda venda = getVenda( cmbFacturaDebito );
                if ( venda != null )
                {
                    txtTotalApagar1.setText( CfMethods.formatarComoMoeda( venda.getTotalVenda() ) );
                    txtClienteNome1.setText( venda.getCodigoCliente().getNome() );
                    dc_data_factura_debito.setDate( venda.getDataVenda() );
                }
            }
            break;
            case ADICIONAR_PRODUTO:
            {
                adicionar_botao();
            }
            break;
            case REMOVER_PRODUTO:
            {

                remover_item_carrinho();
            }
            break;
            case SELECIONAR_PRODUTO:
            {
                atualizarDadosProdutosPelaCombo();
            }
            break;
            case SALVAR_REGISTRO:
            {
                Object[] listarVendaDoDocumento = new VendaDao( emf ).listarVendaDoDocumentoValidas();

                if ( listarVendaDoDocumento != null )
                {
                    cmbFacturaCredito.setModel( new DefaultComboBoxModel( listarVendaDoDocumento ) );
                }

                txtQuatindade.requestFocus();
            }
            break;
            default:
            {

            }
            break;

        }

        atualizarProdutoAdicionarRemover();
        mostrar_proximo_codigo_documento();

        desabilitar_campos();

        String documeto = getDescricaoTipoNota();

        TbItemVenda tbItemVendaSelecionado = getTbItemVendaSelecionado();
        boolean documentoENotaDebito = documeto != null && documeto.equalsIgnoreCase( "Nota de débito" );

        if ( tbItemVendaSelecionado != null )
        {
            if ( documentoENotaDebito )
            {
                System.err.println( "Debito!" );
//                txtQuatindade.setModel( criarSpinnerNumberModel( tbItemVendaSelecionado.getQuantidade(), 10000000, tbItemVendaSelecionado.getQuantidade() ) );
            }
            else
            {
                System.err.println( "Credito!" );
//                txtQuatindade.setModel( criarSpinnerNumberModel( 1, tbItemVendaSelecionado.getQuantidade(), tbItemVendaSelecionado.getQuantidade() ) );
            }
        }

        sp_valor_entregue.setEnabled( documentoENotaDebito );

        atualizarBotaoSalvar( okJButton );
        atualizarBotaoSalvar( okJButton1 );

        boolean algumProdutoSelecionado = cmbCodigoProduto.getSelectedIndex() > 0;

        txtQuatindade.setEnabled( algumProdutoSelecionado );
    }

    private void atualizarBotaoSalvar( JButton okJButton )
    {
        boolean informouOMotivo = !motivoJTextArea.getText().trim().isEmpty();
        boolean pelomenosUmArtigoSelecionado = tabelaItensNotas.getRowCount() > 0;
        okJButton.setEnabled( informouOMotivo && pelomenosUmArtigoSelecionado );
    }

    private void atualizarBotaoSalvar1( JButton okJButton )
    {
        boolean informouOMotivo = !motivoJTextArea1.getText().trim().isEmpty();

        okJButton.setEnabled( informouOMotivo );
    }

    public static double getTotal()
    {
        return getQuantidade() * getPreco();
    }

    public static void adicionar_produto() throws SQLException
    {

        DefaultTableModel modelo = (DefaultTableModel) tabelaItensNotas.getModel();
        double total_deconto = Double.parseDouble( String.valueOf( getPreco() ) ) * Double.parseDouble( String.valueOf( getQuantidade() ) );

        if ( !exist_produto_tabela_formulario( getFkProduto() ) )
        {
            System.err.println( "getQuantidade (): " + getQuantidade() );
            System.err.println( "getTaxaImposto ( getFkProduto () ): " + getTaxaImposto( getFkProduto() ) );
            System.err.println( "getFkProduto (): " + getFkProduto() );
            System.err.println( "getPreco (): " + getPreco() );
            System.err.println( "getTaxaImposto ( getFkProduto () ): " + getTaxaImposto( getFkProduto() ) );
            System.err.println( "getValorIVA ( getTaxaImposto ( getFkProduto () ): " + getValorIVA( getTaxaImposto( getFkProduto() ), getPreco() ) );
            modelo.addRow( new Object[]
            {
                getFkProduto(),
                getDescricao_Produto(),
                getPreco(),
                getQuantidade(),
                //MetodosUtil.retirar_dizimas( getDesconto_produto( total_deconto ) ),
                getDescontoPercentagem(),
                getTaxaImposto( getFkProduto() ),
                //getTotal() - getDesconto_produto( total_deconto ),
                CfMethods.formatarComoMoeda( getTotal() ),
                //CfMethods.formatarComoMoeda( getQuantidade() * getValorIVA( getTaxaImposto( getFkProduto() ), getPreco() ) - getDesconto_produto( total_deconto ) )
                CfMethods.formatarComoMoeda( getValorComIVA(
                getQuantidade(),
                getTaxaImposto( getFkProduto() ),
                getPreco(),
                getDescontoPercentagem()
                )
                )

            } );
        }
        else
        {
            actuazlizar_quantidade_tabela_formulario( String.valueOf( txtQuatindade.getValue() ), getDesconto_produto( total_deconto ) );
        }

        setTotalPagar();
        valor_por_extenco();
        txtQuatindade.setValue( 1 );
        txtCodigoBarra.requestFocus();

    }

    private static void valor_por_extenco()
    {
        Moeda moeda = new MoedaDao( emf ).getByDesignacao( txtMoeda.getText() );
        System.out.println( "Valor XXXXXXX: " + CfMethods.parseMoedaFormatada( txtTotalApagar.getText() ) );
        lbValorPorExtenco.setText( "São: " + MetodosUtil.valorPorExtenso( CfMethods.parseMoedaFormatada( txtTotalApagar.getText() ), moeda.getDesignacao() ) );
    }

    public void remover_item_carrinho()
    {

        DefaultTableModel modelo = (DefaultTableModel) tabelaItensNotas.getModel();
        modelo.removeRow( tabelaItensNotas.getSelectedRow() );
        setTotalPagar();
        //txtDesconto.setText("0");

        valor_por_extenco();
        //calcularTroco();
        atualizarBotaoSalvar( okJButton );

    }

    public boolean salvarItemNota( int cod_notas )
    {

        boolean efectuada = true;
        this.notas = notasDao.findNotas( cod_notas );

        for ( int i = 0; i < tabelaItensNotas.getRowCount(); i++ )
        {
            try
            {
                notasItem = new NotasItem();

                notasItem.setFkProduto( produtoDao.findTbProduto( Integer.parseInt( String.valueOf( tabelaItensNotas.getModel().getValueAt( i, 0 ) ) ) ) );
                notasItem.setFkNota( this.notas );
                notasItem.setQuantidade( Double.parseDouble( String.valueOf( tabelaItensNotas.getModel().getValueAt( i, 3 ) ) ) );
                notasItem.setDesconto( Double.parseDouble( String.valueOf( tabelaItensNotas.getModel().getValueAt( i, 4 ) ) ) );
                notasItem.setValorIva( Double.parseDouble( String.valueOf( tabelaItensNotas.getModel().getValueAt( i, 5 ) ) ) );
                notasItem.setMotivoIsensao( getMotivoIsensao( notasItem.getFkProduto().getCodigo() ) );
                notasItem.setCodigoIsencao( MetodosUtil.getCodigoRegime( notasItem.getFkProduto().getCodigo() ) );

                notasItem.setTotal( new BigDecimal(CfMethods.parseMoedaFormatada( String.valueOf( tabelaItensNotas.getModel().getValueAt( i, 7 ) ) ) ));

                System.err.println( "notasItem.getFkProduto ().getCodigo (): " + notasItem.getFkProduto().getCodigo() );

                //notasItem.setFkPreco( getPrecoProduto( getFkProduto(), notasItem.getQuantidade() ) );
                if ( notasItem.getQuantidade() != 0 )
                {
                    notasItem.setFkPreco( precoDao.findTbPreco( precoDao.getUltimoIdPrecoByIdProduto( notasItem.getFkProduto().getCodigo(), notasItem.getQuantidade() ) ) );
                }
                else
                {
                    notasItem.setFkPreco( precoDao.findTbPreco( precoDao.getUltimoIdPrecoByIdProduto( notasItem.getFkProduto().getCodigo(), 1 ) ) );
                }

                //cria o item notas
                notasItemDao.create( notasItem );
                this.stock_local = stockDao.get_stock_by_id_produto_and_id_armazem( notasItem.getFkProduto().getCodigo(), getVenda( cmbFacturaCredito ).getIdArmazemFK().getCodigo() );

                if ( stock_local.getCodigo() != 0 )
                {
                    double qtd = ( stock_local.getQuantidadeExistente() - notasItem.getQuantidade() );
                    stock_local.setQuantidadeExistente( qtd );
                    try
                    {
                        stockDao.edit( stock_local );
                    }
                    catch ( Exception e )
                    {
                        e.printStackTrace();
                        System.err.println( "Falha ao actualizar a quantidade do produto no stock" );
                    }
                    actualizar_quantidade( notasItem.getQuantidade(), stock_local );
                }

            }
            catch ( Exception e )
            {
                e.printStackTrace();
                efectuada = false;
                JOptionPane.showMessageDialog( null, "Falha ao registrar o produto: " + notasItem.getFkProduto().getCodigo() + " na Factura" );
                break;
            }
        }

        if ( efectuada )
        {
            JOptionPane.showMessageDialog( null, "Nota efectuada com sucesso!.." );
            List<TbProduto> lista_produto_isentos = new ArrayList<>();
            lista_produto_isentos = getProdutosIsentos();
            this.motivos_isentos = MetodosUtil.getMotivoIsensaoProdutos( lista_produto_isentos );
            System.err.println( "MOTIVOS: " + this.motivos_isentos );
            try
            {

                limpar();
                remover_all_produto();
                //adicionar_preco_quantidade_anitgo();

            }
            catch ( Exception e )
            {
                return false;
            }

            txtClienteNome.setText( "" );
            txtClienteNome.requestFocus();
            //Chama a factura e imprime directamente para a imprissora que estiver devenidade no sistema operativo

            return true;
            //ListaVenda1 listaVenda2 = new ListaVenda1(cod_notas, false, ck_simplificada.isSelected());

            //Em caso em que a impreensão é dupla
            //ListaVendaDuplicado listaVenda1 = new ListaVendaDuplicado(cod_notas, setPeformance(), ckImpreesao.isSelected());
        }
        return false;
    }

    public static void setTotalPagar()
    {

        DefaultTableModel modelo = (DefaultTableModel) tabelaItensNotas.getModel();

        double total_pagar = 0;

        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {
            total_pagar += CfMethods.parseMoedaFormatada( String.valueOf( modelo.getValueAt( i, 7 ) ) );

        }

        //txtTotalApagar.setText(String.valueOf(MetodosUtil.retirar_dizimas(total_pagar)));
        txtTotalApagar.setText( CfMethods.formatarComoMoeda( total_pagar ) );

        sp_valor_entregue.setValue( total_pagar );

        txtTroco.setText( CfMethods.formatarComoMoeda( 0.0 ) );
    }

    public void setTotalCompleto()
    {

        tabelaItensNotas.getColumnModel().getColumn( 0 );
        tabelaItensNotas.getColumnModel().getColumn( 1 );
        tabelaItensNotas.getColumnModel().getColumn( 2 );
        tabelaItensNotas.getColumnModel().getColumn( 3 );
        tabelaItensNotas.getColumnModel().getColumn( 4 );
        tabelaItensNotas.getColumnModel().getColumn( 5 );

        DefaultTableModel modelo = (DefaultTableModel) tabelaItensNotas.getModel();

        double total_pagar = 0;

        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {
            total_pagar += Double.parseDouble( String.valueOf( modelo.getValueAt( i, 5 ) ) );

        }

        try
        {

            calcularTroco();

        }
        catch ( Exception e )
        {
            //e.printStackTrace();
        }

    }

    public static double getDesconto_produto( double preco_total_produto ) throws SQLException
    {

        TbDesconto desconto = descontoDao.get_desconto_cliente_produto( getIdCliente(), getFkProduto() );
        Double quantidade = desconto.getQuantidade();
        double percentagem_desconto = desconto.getValor();

        if ( getQuantidade() >= quantidade )
        {
            return preco_total_produto * ( percentagem_desconto / 100 );
        }
        else
        {
            return 0.0;
        }

    }

    public double getDescontoActual() throws SQLException
    {

        ResultSet resultado = conexao.executeQuery( "SELECT valor FROM tb_desconto WHERE idDesconto = 1" );
        double valor = 0;
        if ( resultado.next() )
        {
            valor = resultado.getDouble( "valor" );
        }
        return valor;

    }

    public void remover_all_produto() throws SQLException
    {

        DefaultTableModel modelo = (DefaultTableModel) tabelaItensNotas.getModel();
        for ( int i = modelo.getRowCount() - 1; i >= 0; i-- )
        {
            modelo.removeRow( i );
        }

    }

    public static void main( String[] args ) throws SQLException
    {
        new NotasCreditoDebitoVisao( 15, BDConexao.getInstancia() ).show( true );
    }

    public int getCodigoTipoProduto() throws SQLException
    {
        return conexao.getCodigoPublico( "tb_tipo_produto", String.valueOf( txtCategoria.getText() ) );
    }

    public static int getFkProduto()
    {
        //return conexao.getCodigoPublico("tb_produto", String.valueOf(  cmbProduto.getSelectedItem()));   
        return produtoDao.getProdutoByDescricao( txtProduto.getText() ).getCodigo();

    }

    public static boolean estado_critico() throws SQLException
    {
        TbStock stock = stockDao.getStockByDescricao( getFkProduto(), getVenda( cmbFacturaCredito ).getIdArmazemFK().getCodigo() );

        double qtd_minima = stock.getQuantBaixa(),
                qtd_existente = stock.getQuantidadeExistente(),
                qtd_critica = stock.getQuantCritica();

//           return conexao.getQuantidade_minima_publico(getFkProduto(), getCodigoArmazem() ) < conexao.getQuantidade_Existente_Publico( getFkProduto(), getCodigoArmazem()  ) 
//                   && conexao.getQuantidade_Existente_Publico( getFkProduto(), getCodigoArmazem()  )  <= conexao.getQuantidade_critica_public( getFkProduto(), getCodigoArmazem() );
//   
        return qtd_minima < qtd_existente
                && qtd_existente <= qtd_critica;

    }

    public static boolean isStocavel( String status )
    {
        try
        {
            if ( status.equals( "true" ) )
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        catch ( Exception e )
        {
            return true;
        }

    }

    //devolve o codigo_barra de uma determinada tabela
    public double getPrecoProduto( int codProduto, boolean stocavel )
    {

        String sql = "";

        if ( stocavel )
        {
            sql = "SELECT preco_venda FROM tb_stock WHERE( cod_produto_codigo = " + codProduto + " AND cod_armazem = " + getVenda( cmbFacturaCredito ).getIdArmazemFK().getCodigo() + ")";
        }
        else
        {
            sql = "SELECT preco FROM tb_produto WHERE( codigo = " + codProduto + ")";
            JOptionPane.showMessageDialog( null, "O produto provavelmente nap estocavel!..." );
        }
        ResultSet rs = conexao.executeQuery( sql );

        try
        {
            if ( rs.next() )
            {
                if ( stocavel )
                {
                    return rs.getDouble( "preco_venda" );
                }
                else
                {
                    return rs.getDouble( "preco" );
                }
            }
        }
        catch ( SQLException ex )
        {
            return 0;
        }

        return 0;
    }

    public boolean setCredito()
    {
        //return rbCredito.isSelected();
        return false;
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">    
    private void init()
    {
        operacaoJLabel.setText( "" );
        vasilhameDao = new VasilhameDao( emf );
        descontoDao = new DescontoDao( emf );
        precoDao = new PrecoDao( emf );
        produtoDao = new ProdutoDao( emf );
        stockDao = new StockDao( emf );
        usuarioDao = new UsuarioDao( emf );
        clienteDao = new ClienteDao( emf );
        notasDao = new NotasDao( emf );
        armazemDao = new ArmazemDao( emf );
        notasItemDao = new NotasItemDao( emf );
        tipoProdutoDao = new TipoProdutoDao( emf );
        bancoDao = new BancoDao( emf );
        anoEconomicoDao = new AnoEconomicoDao( emf );
        documentoDao = new DocumentoDao( emf );
        moedaDao = new MoedaDao( emf );
        cambioDao = new CambioDao( emf );
        produtoImpostoDao = new ProdutoImpostoDao( emf );
        produtoIsentoDao = new ProdutoIsentoDao( emf );
        //txtDesconto.setText("0");
//        txtQuatindade.setDocument ( new PermitirNumeros () );

        initNotaCreditoTabe();
        initNotaDebitoTabe();

    }// </editor-fold>   

    private void mostrar_ano_economico_serie( JLabel lb_ano_academico )
    {
        this.anoEconomico = anoEconomicoDao.getLastObject();
        lb_ano_academico.setText( "ANO ECONÔMICO: " + this.anoEconomico.getSerie() );

    }

    private void mostrar_proximo_codigo_documento()
    {
        try
        {
            this.documento = documentoDao.findDocumento( getDocumento().getPkDocumento() );
            this.doc_prox_cod = documento.getCodUltimoDoc() + 1;
            prox_doc = documento.getAbreviacao();
            //FA Série / codigo
            prox_doc += " " + this.anoEconomico.getSerie() + "/" + this.doc_prox_cod;
            lb_proximo_documento.setText( "PRÓXIMO DOC. :" + prox_doc );
        }
        catch ( Exception e )
        {
            this.documento = null;
            lb_proximo_documento.setText( "" );
        }
    }

    private static double getTaxaImposto( int idProduto )
    {
        return produtoImpostoDao.getTaxaByIdProduto( idProduto );
    }

    private static String getMotivoIsensao( int idProduto )
    {
        return produtoIsentoDao.getRegimeIsensaoByIdProduto( idProduto );
    }

    private static double getValorIVA( double taxa, double preco_venda )
    {
        return ( ( ( taxa / 100 ) + 1 ) * preco_venda );
    }

    private void actualizar_cod_doc()
    {
        this.documento.setCodUltimoDoc( this.doc_prox_cod );
        this.documento.setDescricaoUltimoDoc( this.prox_doc );
        this.documento.setUltimaData( new Date() );
        try
        {
            documentoDao.edit( documento );
        }
        catch ( Exception e )
        {
            System.err.println( "Falha ao actualizar o documento" );
        }
    }

    private void setWindowsListener()
    {
        this.addWindowListener( new WindowAdapter()
        {
            @Override
            public void windowActivated( WindowEvent e )
            {
                mostrar_proximo_codigo_documento();
            }
        } );

    }

    private void desabilitar_campos()
    {

//        boolean valor =  ! ( DVML.DOC_FACTURA_PROFORMA_PP == getIdDocumento () );
        boolean valor = true;
        lbValorEnregue.setVisible( valor );
        sp_valor_entregue.setVisible( valor );
        lbTroco.setVisible( valor );
        txtTroco.setVisible( valor );
        lbFormaPagamento.setVisible( valor );
        cmbFormaPagamento.setVisible( valor );

        txtTroco.setText( CfMethods.formatarComoMoeda( 0.0 ) );
    }

//    public static TbPreco getPrecoProduto( int PkProduto )
//    {
//        TbVenda venda = getVenda( cmbFactura );
//
//        List<TbItemVenda> itemVendas = venda.getTbItemVendaList();
//
//        for ( TbItemVenda itemVenda : itemVendas )
//        {
//            if ( itemVenda.getCodigoProduto().getCodigo() == PkProduto )
//            {
//                 //precoDao.findTbPreco( precoDao.getUltimoIdPrecoByIdProduto( PkProduto, itemVenda.getQuantidade() ) )
//                return itemVenda.getFkPreco();
//            }
//        }
//
//        return null;
//    }
    public static TbPreco getPrecoProduto( int PkProduto, int qtd )
    {
        TbVenda venda = getVenda( cmbFacturaCredito );

        List<TbItemVenda> itemVendas = venda.getTbItemVendaList();

        for ( TbItemVenda itemVenda : itemVendas )
        {
            if ( itemVenda.getCodigoProduto().getCodigo() == PkProduto )
            {
                return precoDao.findTbPreco( precoDao.getUltimoIdPrecoByIdProduto( PkProduto, qtd ) );
            }
        }

        return null;
    }

    public static TbVenda getVenda( JComboBox cmbFactura )
    {
        String codFactura = (String) cmbFactura.getSelectedItem();
        return new VendaDao( emf ).findByCodFact( codFactura );
    }

    private void postInitComponents()
    {
        int tamanho = 32;
        resizeJButtonIcon( tamanho, tamanho, getClass().getResource( "/imagens/img32x32/_ok.png" ), okJButton );
        resizeJButtonIcon( tamanho, tamanho, getClass().getResource( "/imagens/img32x32/_cancel.png" ), fecharJButton );
    }

    public Abreviacao getAbreviacaoDocumento()
    {
        int selectedIndex = principalJTabbedPane.getSelectedIndex();

        return selectedIndex == 0 ? Abreviacao.NC : Abreviacao.ND;
    }

    public String getDescricaoTipoNota()
    {
        Abreviacao tipoNota = getAbreviacaoDocumento();

        if ( tipoNota == ND )
        {
            return "Nota de débito";
        }

        return "Nota de crédito";
    }

    private static double getTotalIliquido()
    {
        DefaultTableModel modelo = (DefaultTableModel) tabelaItensNotas.getModel();
        int qtd = 0;
        double total_iliquido = 0, preco_unitario = 0;

        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {
            preco_unitario = Double.parseDouble( modelo.getValueAt( i, 2 ).toString() );
            qtd = Integer.parseInt( modelo.getValueAt( i, 3 ).toString() );
            total_iliquido += ( preco_unitario * qtd );

        }

        return total_iliquido;
    }
//    
//        private static double getDescontoFinanceiro()
//    {
//        double desconto_economico = 0d;
//        desconto_economico = Double.parseDouble( sp_valor_desconto_financeiro.getValue().toString() );
//        return desconto_economico;
//    }

    private static double getTotalAOALiquido()
    {
        double valores = ( getTotalIliquido() + getTotalImposto() );
        double descontos = ( getDescontoComercial() + getDescontoFinanceiro() );
        System.out.println( "TotalIliquido: " + getTotalIliquido() );
        System.out.println( "TotalImposto: " + getTotalImposto() );
        System.out.println( "TotalDescontoComercial: " + getDescontoComercial() );
        System.out.println( "Total Liquido: " + ( valores - descontos ) );
        return ( valores - descontos );
    }

    private static double getDescontoComercial()
    {
        DefaultTableModel modelo = (DefaultTableModel) tabelaItensNotas.getModel();
        int qtd = 0;
        double desconto_comercial = 0d, preco_unitario = 0d, desconto_valor_linha = 0d;

        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {
            preco_unitario = Double.parseDouble( modelo.getValueAt( i, 2 ).toString() );
            qtd = Integer.parseInt( modelo.getValueAt( i, 3 ).toString() );
            double valor_percentagem = Double.parseDouble( modelo.getValueAt( i, 4 ).toString() );
            desconto_valor_linha = ( ( valor_percentagem ) / 100 );
            double valor_unitario = ( preco_unitario * qtd );
            desconto_comercial += ( valor_unitario * desconto_valor_linha );

        }

        return desconto_comercial;
    }

    private static double getTotalIncidencia()
    {
        DefaultTableModel modelo = (DefaultTableModel) tabelaItensNotas.getModel();
        int qtd = 0;
        double incidencia = 0d, preco_unitario = 0d, desconto_valor_linha = 0;

        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {
            preco_unitario = Double.parseDouble( modelo.getValueAt( i, 2 ).toString() );
            qtd = Integer.parseInt( modelo.getValueAt( i, 3 ).toString() );
            double valor_percentagem = Double.parseDouble( modelo.getValueAt( i, 4 ).toString() );
            double taxa = Double.parseDouble( modelo.getValueAt( i, 5 ).toString() );
            // a incidência só é aplicável ao produtos sujeitos a iva 
            if ( taxa != 0 )
            {
                desconto_valor_linha = ( ( valor_percentagem ) / 100 );
                double valor_unitario = ( preco_unitario * qtd );
                incidencia += ( ( valor_unitario ) - ( valor_unitario * desconto_valor_linha ) );

            }

        }

        return incidencia;
    }

    private static double getTotalIncidenciaIsento()
    {
        DefaultTableModel modelo = (DefaultTableModel) tabelaItensNotas.getModel();
        int qtd = 0;
        double incidencia = 0d, preco_unitario = 0d, desconto_valor_linha = 0;

        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {
            //preco_unitario = CfMethods.parseMoedaFormatada( modelo.getValueAt( i, 2 ).toString() );
            preco_unitario = Double.parseDouble( modelo.getValueAt( i, 2 ).toString() );
            qtd = Integer.parseInt( modelo.getValueAt( i, 3 ).toString() );
            double valor_percentagem = Double.parseDouble( modelo.getValueAt( i, 4 ).toString() );
            double taxa = Double.parseDouble( modelo.getValueAt( i, 5 ).toString() );
            // a incidência só é aplicável ao produtos sujeitos a iva 
            if ( taxa == 0 )
            {
                desconto_valor_linha = ( ( valor_percentagem ) / 100 );
                double valor_unitario = ( preco_unitario * qtd );
                incidencia += ( ( valor_unitario ) - ( valor_unitario * desconto_valor_linha ) );

            }

        }

        return incidencia;
    }

    private static double getTotalImposto()
    {
        DefaultTableModel modelo = (DefaultTableModel) tabelaItensNotas.getModel();
        int qtd = 0;
        double imposto = 0d, preco_unitario = 0d, desconto_valor_linha = 0;

        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {
            preco_unitario = Double.parseDouble( modelo.getValueAt( i, 2 ).toString() );
            qtd = Integer.parseInt( modelo.getValueAt( i, 3 ).toString() );
            double valor_percentagem = Double.parseDouble( modelo.getValueAt( i, 4 ).toString() );
            double taxa = Double.parseDouble( modelo.getValueAt( i, 5 ).toString() );
            // a incidência só é aplicável ao produtos sujeitos a iva 
            if ( taxa != 0 )
            {
                double valor_unitario = ( preco_unitario * qtd );
                desconto_valor_linha = valor_unitario * ( ( valor_percentagem ) / 100 );
                imposto += ( ( valor_unitario - desconto_valor_linha ) * ( taxa / 100 ) );

            }

        }

        return imposto;
    }

    private static double getDescontoFinanceiro()
    {
//        double desconto_economico = 0d;
//        desconto_economico = Double.parseDouble( sp_valor_desconto_financeiro.getValue().toString() );
//        return desconto_economico;
        return 0.0;
    }

    private List<TbProduto> getProdutosIsentos()
    {
        DefaultTableModel modelo = (DefaultTableModel) tabelaItensNotas.getModel();
        double taxa = 0.0;
        int codigo_produto = 0;
        List<TbProduto> lista_produtos_isentos = new ArrayList<>();
        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {
            codigo_produto = Integer.parseInt( modelo.getValueAt( i, 0 ).toString() );
            taxa = Double.parseDouble( modelo.getValueAt( i, 5 ).toString() );
            if ( taxa == 0.0 )
            {
                lista_produtos_isentos.add( produtoDao.findTbProduto( codigo_produto ) );
            }
        }

        return lista_produtos_isentos;

    }

    private void actualizar_moeda()
    {
        CfMethods.MOEDA = getMoeda().getAbreviacao();
        mostrar_abreviacao_moeda_cambio();
        refresh_table();
    }

    private static Moeda getMoeda()
    {
        String moedaSelecionada = (String) txtMoeda.getText();

        if ( moedaSelecionada == null )
        {
            return null;
        }

        return new MoedaDao( emf ).getByDescricao( moedaSelecionada );
    }

    private void actualizar_moeda( String moeda )
    {
        CfMethods.MOEDA = moeda;
        mostrar_abreviacao_moeda_cambio();
        refresh_table( 1 );
    }

    private void mostrar_abreviacao_moeda_cambio()
    {
        try
        {
            this.moeda = moedaDao.findMoeda( getIdMoeda() );
            this.cambio = cambioDao.getLastObject( getIdMoeda() );
            lb_cambio.setText( "CAMBIO: " + String.valueOf( this.cambio.getValor() ) + " " + this.moeda.getAbreviacao() );

        }
        catch ( Exception e )
        {
            this.cambio = null;
            e.printStackTrace();
            lb_cambio.setText( "" );
        }
    }

    private void refresh_table()
    {

        DefaultTableModel modelo = (DefaultTableModel) tabelaItensNotas.getModel();

        double preco = 0, desconto_percentagem = 0, sub_total_linha = 0, sub_total_linha_com_iva = 0, taxa = 0;
        int idProduto, qtd;

        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {

            try
            {

                /**
                 * Recupera os valores da tabela
                 */
                idProduto = Integer.parseInt( modelo.getValueAt( i, 0 ).toString() );
                qtd = Integer.parseInt( modelo.getValueAt( i, 3 ).toString() );
                taxa = Double.parseDouble( modelo.getValueAt( i, 3 ).toString() );
                desconto_percentagem = Double.parseDouble( modelo.getValueAt( i, 4 ).toString() );

                //busca o preço em função do câmbio
                preco = getPreco( idProduto, qtd );

                /**
                 * Processa os valores para serem actualizados na tabela
                 */
                //desconto = getDesconto_produto( preco, qtd );
                sub_total_linha = ( preco * qtd ) - desconto_percentagem;
                sub_total_linha_com_iva = getValorComIVA( qtd, getTaxaImposto( idProduto ), preco, desconto_percentagem );

                /**
                 * actualiza os valores na tabela
                 */
                modelo.setValueAt( CfMethods.formatarComoMoeda( preco ), i, 2 );
                modelo.setValueAt( CfMethods.formatarComoMoeda( desconto_percentagem ), i, 4 );
                modelo.setValueAt( CfMethods.formatarComoMoeda( sub_total_linha ), i, 6 );
                modelo.setValueAt( CfMethods.formatarComoMoeda( sub_total_linha_com_iva ), i, 7 );

            }
            catch ( Exception e )
            {
                e.printStackTrace();
            }

        }

        setTotalPagar();
        calculaTotalIVA();
        valor_por_extenco();

    }

    private void refresh_table( int idMoeda )
    {

        Moeda moeda_local = moedaDao.findMoeda( idMoeda );
        DefaultTableModel modelo = (DefaultTableModel) tabelaItensNotas.getModel();

        double preco = 0, desconto = 0, sub_total_linha = 0, sub_total_linha_com_iva = 0, taxa = 0;
        int idProduto, qtd;

        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {

            try
            {

                /**
                 * Recupera os valores da tabela
                 */
                idProduto = Integer.parseInt( modelo.getValueAt( i, 0 ).toString() );
                qtd = Integer.parseInt( modelo.getValueAt( i, 3 ).toString() );
                taxa = Double.parseDouble( modelo.getValueAt( i, 3 ).toString() );

                //busca o preço em função do câmbio em funcção da moeda
                preco = getPreco( idProduto, qtd, idMoeda );

                /**
                 * Processa os valores para serem actualizados na tabela
                 */
                desconto = getDesconto_produto( preco, qtd );
                sub_total_linha = ( preco * qtd ) - desconto;
                sub_total_linha_com_iva = getValorComIVA( qtd, getTaxaImposto( idProduto ), preco, desconto );

                /**
                 * actualiza os valores na tabela
                 */
                modelo.setValueAt( CfMethods.formatarComoMoeda( preco ), i, 2 );
                modelo.setValueAt( CfMethods.formatarComoMoeda( desconto ), i, 4 );
                modelo.setValueAt( CfMethods.formatarComoMoeda( sub_total_linha ), i, 6 );
                modelo.setValueAt( CfMethods.formatarComoMoeda( sub_total_linha_com_iva ), i, 7 );

            }
            catch ( Exception e )
            {
                e.printStackTrace();
            }

        }

        setTotalPagar();
        calculaTotalIVA();
        valor_por_extenco( moeda_local );

    }

//    public static int getCodigoProduto()
//    {
//        //return conexao.getCodigoPublico("tb_produto", String.valueOf(  cmbProduto.getSelectedItem()));   
//        System.err.println ( "#12 cmbProduto.getSelectedItem().toString(): "+cmbProduto.getSelectedItem().toString() );
//        return produtoDao.getProdutoByDescricao( cmbProduto.getSelectedItem().toString() ).getCodigo();
//
//    }
    public static double getDesconto_produto( double preco_total_produto, double qtd )
    {

        TbDesconto desconto = descontoDao.get_desconto_cliente_produto( getIdCliente(), getFkProduto() );
        Double quantidade = desconto.getQuantidade();
        double percentagem_desconto = desconto.getValor();

        if ( qtd >= quantidade )
        {
            return preco_total_produto * ( percentagem_desconto / 100 );
        }
        else
        {
            return 0.0;
        }

    }

    public static Double getPreco()
    {
        Moeda moeda = getMoeda();
        if ( moeda == null )
        {
            return null;
        }

        Cambio lastCambio = new CambioDao( emf ).getLastObject( moeda.getPkMoeda() );
        try
        {
            // return  precoDao.findTbPreco( precoDao.getUltimoIdPrecoByIdProduto(  getCodigoProduto() )  ).getPrecoVenda();
            // return MetodosUtil.retirar_dizimas(precoDao.findTbPreco(precoDao.getUltimoIdPrecoByIdProduto(getCodigoProduto(), Integer.parseInt(txtQuatindade.getText()))).getPrecoVenda());
            Double valorCambio = lastCambio.getValor();

            double precoVenda = precoDao.findTbPreco( precoDao.getUltimoIdPrecoByIdProduto( getFkProduto(), (int) txtQuatindade.getValue() ) ).getPrecoVenda().doubleValue();
            System.err.println( "#precoVenda: " + precoVenda );
            return ( precoVenda / valorCambio );
        }
        catch ( Exception e )
        {
            return 0.0;
        }

    }

    public static Double getPreco( int idProduto, double qtd )
    {

        Moeda moeda = getMoeda();
        if ( moeda == null )
        {
            return null;
        }

        Cambio lastCambio = new CambioDao( emf ).getLastObject( moeda.getPkMoeda() );
        try
        {
            // return  precoDao.findTbPreco( precoDao.getUltimoIdPrecoByIdProduto(  getCodigoProduto() )  ).getPrecoVenda();
            // return MetodosUtil.retirar_dizimas(precoDao.findTbPreco(precoDao.getUltimoIdPrecoByIdProduto(getCodigoProduto(), Integer.parseInt(txtQuatindade.getText()))).getPrecoVenda());
            Double valorCambio = lastCambio.getValor();
            double precoVenda = precoDao.findTbPreco( precoDao.getUltimoIdPrecoByIdProduto( idProduto, qtd ) ).getPrecoVenda().doubleValue();

            System.err.println( "PREÇO VENDA: " + precoVenda );
            if ( moeda.getAbreviacao().equals( DVML.MOEDA_KWANZA ) )
            {
                return precoVenda;
            }

            return ( precoVenda / valorCambio );

        }
        catch ( Exception e )
        {
            e.printStackTrace();
            return null;
        }

    }

    public static Double getPreco( int idProduto, int qtd, int idMoeda )
    {

        Moeda moeda = moedaDao.findMoeda( idMoeda );
        if ( moeda == null )
        {
            return null;
        }

        Cambio lastCambio = new CambioDao( emf ).getLastObject( moeda.getPkMoeda() );
        try
        {
            // return  precoDao.findTbPreco( precoDao.getUltimoIdPrecoByIdProduto(  getCodigoProduto() )  ).getPrecoVenda();
            // return MetodosUtil.retirar_dizimas(precoDao.findTbPreco(precoDao.getUltimoIdPrecoByIdProduto(getCodigoProduto(), Integer.parseInt(txtQuatindade.getText()))).getPrecoVenda());
            Double valorCambio = lastCambio.getValor();
            double precoVenda = precoDao.findTbPreco( precoDao.getUltimoIdPrecoByIdProduto( idProduto, qtd ) ).getPrecoVenda().doubleValue();

            System.err.println( "PREÇO VENDA: " + precoVenda );
            if ( moeda.getAbreviacao().equals( DVML.MOEDA_KWANZA ) )
            {
                return precoVenda;
            }

            return ( precoVenda / valorCambio );

        }
        catch ( Exception e )
        {
            e.printStackTrace();
            return null;
        }

    }

    private static double getValorComIVA( int qtd, double taxa, double preco_venda, double desconto )
    {
        double subtotal_linha = ( preco_venda * qtd );
        double desconto_valor = ( subtotal_linha * ( desconto / 100 ) );
        double valor_iva = 1 + ( taxa / 100 );//
        return ( ( subtotal_linha - desconto_valor ) * valor_iva );

    }

    private static void valor_por_extenco( Moeda moeda )
    {
        System.out.println( "Valor XXXXXXX: " + CfMethods.parseMoedaFormatada( txtTotalApagar.getText() ) );
        lbValorPorExtenco.setText( "São: " + MetodosUtil.valorPorExtenso( CfMethods.parseMoedaFormatada( txtTotalApagar.getText() ), moeda.getDesignacao() ) );
    }

    private static double getDescontoPercentagem()
    {
        TbDesconto desconto = getDesconto();
        return desconto.getValor();
    }

    private static TbDesconto getDesconto()
    {
        System.out.println( "ID cliente: " + getIdCliente() );
        System.out.println( "ID produto: " + getFkProduto() );
        TbDesconto desconto = descontoDao.get_desconto_cliente_produto( getIdCliente(), getFkProduto() );
        return desconto;
    }

    private List<NotasItem> getConvertVendasToNotas( TbVenda documentoOrigem )
    {
        List<NotasItem> lista_nota = new ArrayList<>();
        List<TbItemVenda> lista_item = documentoOrigem.getTbItemVendaList();
        NotasItem notasItem;
        for ( TbItemVenda linha : lista_item )
        {
            notasItem = new NotasItem();

            notasItem.setFkProduto( linha.getCodigoProduto() );
            notasItem.setFkNota( null );
            notasItem.setQuantidade( linha.getQuantidade() );
            notasItem.setDesconto( linha.getDesconto() );
            notasItem.setValorIva( linha.getValorIva() );
            notasItem.setMotivoIsensao( linha.getMotivoIsensao() );
            notasItem.setTotal( new BigDecimal(linha.getTotal().doubleValue() ));
            notasItem.setFkPreco( linha.getFkPreco() );

            lista_nota.add( notasItem );

        }

        return lista_nota;

    }

    private static double getTotalVendaIVASemIncluirDesconto()
    {
        double taxa = 0, total_iva_local = 0, preco_unitario = 0, sub_total_iliquido = 0;
        int qtd = 0;

        DefaultTableModel modelo = (DefaultTableModel) tabelaItensNotas.getModel();
        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {
            preco_unitario = Double.parseDouble( modelo.getValueAt( i, 2 ).toString() );
            qtd = Integer.parseInt( modelo.getValueAt( i, 3 ).toString() );
            sub_total_iliquido = ( preco_unitario * qtd );
            taxa = Double.parseDouble( modelo.getValueAt( i, 5 ).toString() );
            total_iva_local += ( ( ( sub_total_iliquido ) * ( taxa / 100 ) ) );
        }

        return total_iva_local;
    }

    private static double getTotalVendaIVASemIncluirDesconto( List<TbItemVenda> itemVendas )
    {
        double taxa = 0, total_iva_local = 0, preco_unitario = 0, sub_total_iliquido = 0;
        double qtd = 0d;

        DefaultTableModel modelo = (DefaultTableModel) tabelaItensNotas.getModel();
        for ( int i = 0; i < itemVendas.size(); i++ )
        {
            preco_unitario = itemVendas.get( i ).getFkPreco().getPrecoVenda().doubleValue();
            qtd = itemVendas.get( i ).getQuantidade();
            taxa = itemVendas.get( i ).getValorIva();

            sub_total_iliquido = ( preco_unitario * qtd );
            total_iva_local += ( ( ( sub_total_iliquido ) * ( taxa / 100 ) ) );
        }

        return total_iva_local;
    }

    private double getGrossTotal()
    {
        System.out.println( "TOTALILIQUIDO: " + getTotalVendaIVASemIncluirDesconto() );
        System.out.println( "TOTALVENDAIVASEMDESCONTO: " + getTotalVendaIVASemIncluirDesconto() );
        return getTotalIliquido() + getTotalVendaIVASemIncluirDesconto();
    }

    private double getGrossTotal( List<TbItemVenda> itemVendas )
    {
        System.out.println( "TOTALILIQUIDO: " + getTotalVendaIVASemIncluirDesconto( itemVendas ) );
        System.out.println( "TOTALVENDAIVASEMDESCONTO: " + getTotalVendaIVASemIncluirDesconto( itemVendas ) );
        return itemVendas.get( 0 ).getCodigoVenda().getTotalGeral().doubleValue() + getTotalVendaIVASemIncluirDesconto( itemVendas );
    }

}
