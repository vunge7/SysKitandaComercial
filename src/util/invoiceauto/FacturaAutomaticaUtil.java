/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util.invoiceauto;

//import java.sql.Connection;
import util.*;
import comercial.controller.AnoEconomicoController;
import comercial.controller.DadosInstituicaoController;
import comercial.controller.DocumentosController;
import comercial.controller.ItemVendasController;
import comercial.controller.LugaresController;
import comercial.controller.MesasController;
import comercial.controller.PrecosController;
import comercial.controller.ProdutosController;
import comercial.controller.ProdutosImpostoController;
import comercial.controller.VendasController;
import entity.AnoEconomico;
import entity.Cambio;
import entity.Documento;
import entity.TbArmazem;
import entity.TbBanco;
import entity.TbCliente;
import entity.TbItemVenda;
import entity.TbLugares;
import entity.TbMesas;
import entity.TbPreco;
import entity.TbProduto;
import entity.TbUsuario;
import entity.TbVenda;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Vector;
import java.util.stream.Collectors;
import javax.swing.JOptionPane;
import static util.DVML.ARMAZEM_DEFAUTL;
import static util.DVML._CLIENTE_CONSUMIDOR_FINAL;

/**
 *
 * @author Domingos Dala Vunge
 */
public class FacturaAutomaticaUtil
{

    private BDConexao conexao;
    private PrecosController precosController;
    private ProdutosController produtosController;
    private ProdutosImpostoController produtosImpostoController;
    private LugaresController lugaresController;
    private MesasController mesasController;
    private ItemVendasController itemVendasController;
    private BDConexao conexaoTransaction;

    private AutoInvoiceConfiguration configuration;

    public FacturaAutomaticaUtil(
            AutoInvoiceConfiguration configuration,
            BDConexao conexao )
    {

        this.configuration = configuration;

        this.conexao = conexao;
        precosController = new PrecosController( conexao );
        produtosController = new ProdutosController( conexao );
        produtosImpostoController = new ProdutosImpostoController( conexao );
        lugaresController = new LugaresController( conexao );
        mesasController = new MesasController( conexao );
        itemVendasController = new ItemVendasController( conexao );

    }

    /**
     * @1. preencher a lista dos itens do produto.
     * @2. criar a funcao getTotalIliquido
     * @3. criar a funcao getTotalImposto
     * @4. criar a funcao getTotalRetencaoLiquido
     * @5. criar a funcao getTotalAOALiquido
     * @6. criar a funcao getValor_entregue
     * @7. criar a funcao getTotalIncidenciaIsento
     */
    public static void main( String[] args )
    {

        int MAX_INVOICE_NUMBER = 90;
        int MAX_INVOICE_LINE_NUMBER = 4;
        int MAX_INVOICE_LINE_QUANTITY = 1;
//        double MONTHLY_GENERAL_INVOICE_LIMIT = 34460725.41; //#
        double MONTHLY_GENERAL_INVOICE_LIMIT = 9274642.86; //#
//        double MONTHLY_GENERAL_INVOICE_LIMIT = 28947009.34; //#
        int MONTH_START_DAY = 1;
        int MONTH_END_DAY = 31;//#
//        double INITIAL_TOTAL_MONTH = 28947009.34;
        double INITIAL_TOTAL_MONTH = 0;
        int ECONOMIC_YEAR = 2025;
        int ECONOMIC_YEAR_ID = 6;//#
//        int ECONOMIC_YEAR_ID = 5;//#
        int DOCUMENT_ID = 1;
        int MONTH_ID = 1;//#
        int USER_ID = 15;
        int CLIENT_ID = 1;
        int WAREHOUSE_ID = 2;

        BDConexao conexao = new BDConexao();
        ProdutosController produtosController = new ProdutosController( conexao );

        /**
         * 1 - MATRICULAS
         * 2 - PROPINAS
         * 3 - TRASNPORTES
         * 4 - UNIFORMES
         */
        List<Integer> ids = Arrays.asList( 3,4);
 
        Vector<TbProduto> produtosByTipoProduto = produtosController.getProdutosByTipoProduto( ids );
        Vector<Integer> vectorProdutos = produtosByTipoProduto.stream()
                .map( TbProduto::getCodigo )
                .collect( Collectors.toCollection( Vector::new ) );
        

        AutoInvoiceConfiguration configuration = new AutoInvoiceConfiguration();
        configuration.setNumeroMaximoFactura( MAX_INVOICE_NUMBER );
        configuration.setListaProdutoVenda( vectorProdutos );
        configuration.setNumeroMaximoLinha( MAX_INVOICE_LINE_NUMBER );
        configuration.setNumeroMaximoQtd( MAX_INVOICE_LINE_QUANTITY);
        configuration.setLimiteFacturacaoGeralMes( MONTHLY_GENERAL_INVOICE_LIMIT );
        configuration.setDiaComeco( MONTH_START_DAY );
        configuration.setLimiteDiaMes( MONTH_END_DAY );
        configuration.setContTotalGeralMes( INITIAL_TOTAL_MONTH);
        configuration.setAnoEconomico( ECONOMIC_YEAR );
        configuration.setAnoEconomicoId( ECONOMIC_YEAR_ID );
        configuration.setDocumentoId( DOCUMENT_ID );
        configuration.setMesId( MONTH_ID );
        configuration.setUserId( USER_ID);
        configuration.setClienteId( CLIENT_ID );
        configuration.setArmazemId( WAREHOUSE_ID );

        FacturaAutomaticaUtil facturaAutomaticaUtil = new FacturaAutomaticaUtil( configuration, conexao );
        facturaAutomaticaUtil.procedimentoGerar();
//        facturaAutomaticaUtil.mostrarItens();
    }

    private void mostrarItens()
    {
        int linha = 1;
        double precoVenda = 0d;
        double totalLinha = 0d;
        for ( ItemUtil item : listaIntes )
        {
            TbPreco preco = precosController.getLastIdPrecoByIdProduto( item.getIdProduto(), 0 );
            if ( Objects.nonNull( preco ) )
            {
                precoVenda = preco.getPrecoVenda().doubleValue();
            }
            else
            {
                precoVenda = 0d;
            }
            totalLinha = precoVenda * item.getQtd();

            System.out.println( "+++++++++++++++++++++++++++++++++++++++++++++++++++++++ " + linha );
            System.out.println( linha + " - Item: " + item.getIdProduto() + "  qtd: " + item.getQtd() + " Preço:  " + precoVenda + "Total:  " + totalLinha );
            System.out.println( "+++++++++++++++++++++++++++++++++++++++++++++++++++++++" );
            linha++;
        }

        System.out.println( "TOTAL ILIQUIDO: " + getTotalIliquido() );
        System.out.println( "TOTAL IMPOSTO: " + getTotalImposto() );
        System.out.println( "TOTAL INSCIDÊNCIA: " + getTotalIncidencia() );
        System.out.println( "TOTAL LÍQUIDO: " + getTotalAOALiquido() );
    }

    private double getPrecoItem( int idProduto )
    {
        double precoVenda = 0d;
        TbPreco preco = precosController.getLastIdPrecoByIdProduto( idProduto, 0 );
        if ( Objects.nonNull( preco ) )
        {
            precoVenda = preco.getPrecoVenda().doubleValue();
        }
        else
        {
            precoVenda = 0d;
        }
        return precoVenda;
    }

    private boolean existeItem( int codProduto )
    {
        for ( ItemUtil item : listaIntes )
        {
            if ( item.getIdProduto() == codProduto )
            {
                return true;
            }
        }

        return false;

    }

    private List<ItemUtil> listaIntes = new ArrayList<>();

    private int getIdProduto( int index )
    {

        Vector<Integer> listaProdutoVenda = configuration.getListaProdutoVenda();
        return listaProdutoVenda.get( index );
    }

    private void actualizarListItens()
    {
        Random random = new Random();
        int tamanho = random.nextInt( configuration.getNumeroMaximoLinha() ) + 1;
        for ( int i = 0; i < tamanho; i++ )
        {

            Vector<Integer> listaProdutoVenda = configuration.getListaProdutoVenda();
            int size = listaProdutoVenda.size();
            int index = random.nextInt( size ); // pega o codigo do produto aleatoriamente no vector
            int idProduto = getIdProduto( index );
            if ( !existeItem( idProduto ) )
            {
                int qtd = random.nextInt( configuration.getNumeroMaximoQtd() ) + 1;
                ItemUtil itemUtil = new ItemUtil();
                itemUtil.setIdProduto( idProduto );
                itemUtil.setQtd( qtd );
                listaIntes.add( itemUtil );
            }
        }
    }

    private List<Date> getDates()
    {
        List<Date> lista = new ArrayList<>();
        for ( int i = configuration.getDiaComeco(); i <= configuration.getLimiteDiaMes(); i++ )
        {
            Date date = new Date();
            date.setYear( configuration.getAnoEconomico() - 1900 );
            date.setMonth( configuration.getMesId() - 1 );
            date.setDate( i );
            lista.add( date );
        }
        return lista;
    }

    private void procedimentoGerar()
    {

        Random random = new Random();
        List<Date> listDate = getDates();

        for ( int i = 0; i < listDate.size(); i++ )
        {
            int numeroFactura = random.nextInt( configuration.getNumeroMaximoFactura() ) + 1; // determina o numero de factura para cada dia
            Date dataActual = listDate.get( i );
            dataActual.setMinutes( 8 );
            dataActual.setSeconds( 0 );
            System.out.println( MetodosUtil.getDataBancoFull( dataActual ) );

            for ( int j = 1; j <= numeroFactura; j++ )
            {
                dataActual.setMinutes( dataActual.getMinutes() + j );
                System.out.println( "DATA: " + MetodosUtil.getDataBanco( dataActual ) );
                //detrermina o número de linhas da factura automáticamente
                actualizarListItens();
                configuration.setContTotalGeralMes( configuration.getContTotalGeralMes() + getTotalAOALiquido() );
//                System.out.println( "CON TOTAL GERAL: " + contTotalGeralMes );
                if ( configuration.getContTotalGeralMes() <= configuration.getLimiteFacturacaoGeralMes() )
                {
                    salvar_venda_comercial_automatico( dataActual );
                }

                else
                {
                    break;
                }

            }
        }
    }

    private void salvar_venda_comercial_automatico( Date dataDocumento )
    {
//        mostrar_proximo_codigo_documento();
        conexaoTransaction = BDConexao.getInstancia();
        DadosInstituicaoController dadosInstituicaoController = new DadosInstituicaoController( conexaoTransaction );
        VendasController vendasController = new VendasController( conexaoTransaction );
        DocumentosController documentosController = new DocumentosController( conexaoTransaction );
        AnoEconomicoController anoEconomicoController = new AnoEconomicoController( conexaoTransaction );
        itemVendasController = new ItemVendasController( conexaoTransaction );
        DocumentosController.start( conexaoTransaction );
        int prazo_proforma = dadosInstituicaoController.findByCodigo( 1 ).getPrazoProforma();
        Date data_documento = dataDocumento;

        TbVenda venda_local = new TbVenda();
        venda_local.setDataVenda( data_documento );
        venda_local.setRefDataFact( data_documento );
        venda_local.setRefCodFact( "" );

        Calendar calendar = Calendar.getInstance();
        calendar.setTime( data_documento );
        //adicionar 15 dias na data do documento.

        calendar.add( Calendar.DATE, prazo_proforma );
        venda_local.setDataVencimento( calendar.getTime() );
        venda_local.setHora( data_documento );
        venda_local.setNomeCliente( _CLIENTE_CONSUMIDOR_FINAL );
        venda_local.setClienteNif( _CLIENTE_CONSUMIDOR_FINAL );

        //Total Ilíquido
        venda_local.setTotalGeral( new BigDecimal( getTotalIliquido() ) );
        //desconto por linha
        venda_local.setDescontoComercial( new BigDecimal( getDescontoComercial() ) );
        //imposto
        //calculaTotalIVA();
        venda_local.setTotalIva( new BigDecimal( getTotalImposto() ) );
        //calculaTotalRetencao();
        venda_local.setTotalRetencao( new BigDecimal( getTotalRetencaoLiquido() ) );
        //desconto global
        venda_local.setDescontoFinanceiro( new BigDecimal( getDescontoFinanceiro() ) );
        //Total(AOA) <=> Total Líquido
        venda_local.setTotalVenda( new BigDecimal( getTotalAOALiquido() ) );
        venda_local.setValorEntregue( new BigDecimal( getValor_entregue() ) );
        venda_local.setTroco( new BigDecimal( getTroco() ) );
        venda_local.setTotalIncidencia( new BigDecimal( getTotalIncidencia() ) );
        venda_local.setTotalIncidenciaIsento( new BigDecimal( getTotalIncidenciaIsento() ) );

        /*outros campos*/
        venda_local.setDescontoTotal( new BigDecimal( getDescontoComercial() + getDescontoFinanceiro() ) );
        venda_local.setIdBanco( new TbBanco( 1 ) );
        venda_local.setIdArmazemFK( new TbArmazem( configuration.getArmazemId() ) );

        venda_local.setCodigoUsuario( new TbUsuario( configuration.getUserId() ) );
        venda_local.setCodigoCliente( new TbCliente( configuration.getClienteId() ) );
        venda_local.setFkAnoEconomico( new AnoEconomico( configuration.getAnoEconomicoId() ) );
        venda_local.setReferencia( "" );
        venda_local.setNomeConsumidorFinal( _CLIENTE_CONSUMIDOR_FINAL );
        venda_local.setFkDocumento( new Documento( configuration.getDocumentoId() ) );

        String prox_doc = getCodDocActualizador( documentosController, anoEconomicoController, vendasController );

        System.err.println( "PROXDOC" + prox_doc );
        venda_local.setCodFact( prox_doc );
        venda_local.setCont( 0 );

        venda_local.setHashCod( MetodosUtil.criptografia_hash(
                venda_local,
                getGrossTotal(),
                conexaoTransaction )
        );

        venda_local.setTotalPorExtenso( "" );
        venda_local.setModelo( "" );
        venda_local.setNumMotor( "" );

        venda_local.setNomeMotorista( "" );
        venda_local.setMatricula( "" );
        venda_local.setMarcaCarro( "" );
        venda_local.setKilometro( "" );
        venda_local.setNumChassi( "" );
        venda_local.setObs( "" );
        venda_local.setCorCarro( "" );
        venda_local.setNDocMotorista( "" );
        System.out.println( "STATUS:hash cod processado." );

        // venda_local.setAssinatura( this.prox_doc );
        venda_local.setAssinatura( MetodosUtil.assinatura_doc( venda_local.getHashCod() ) );
//        venda_local.setRefDataFact( CfMethods.fullDateToText( venda_local.getDataVenda() ) );

        //System.out.println( "STATUS:documento assinado com sucesso." );\\
        venda_local.setFkCambio( new Cambio( 1 ) );

        /*status documento*/
        venda_local.setStatusEliminado( "false" );
        venda_local.setPerformance( "false" );
        venda_local.setCredito( "false" );
        venda_local.setGorjeta( new BigDecimal( 0d ) );

        try
        {

            if ( vendasController.salvar( venda_local ) )
            {
                System.out.println( "venda criada com sucesso!" );
                Integer last_venda = vendasController.getLastVenda().getCodigo();
                if ( Objects.isNull( last_venda ) || last_venda == 0 )
                {
                    DocumentosController.rollback( conexaoTransaction );
                    conexaoTransaction.close();
                    return;
                }
                System.err.println( "last_venda: " + last_venda );
//                System.out.println( "STATUS:factura criada com sucesso." );

                if ( last_venda > 0 )
                {
                    //DocumentosController.commitTransaction( conexaoTransaction );
                    salvar_item_venda_comercial( last_venda );
                }
            }
            else
            {
                System.out.println( "ERROR: Já existe venda relacionada." );
            }

        }
        catch ( Exception e )
        {
            System.err.println( "STATUS: falha ao actualizar a factura" );
            e.printStackTrace();
            JOptionPane.showMessageDialog( null, "Falha ao Processar a Factura", "FALHA", JOptionPane.ERROR_MESSAGE );

            DocumentosController.rollback( conexaoTransaction );

            conexaoTransaction.close();
        }

    }

    public void salvar_item_venda_comercial( Integer cod_venda )
    {
        TbItemVenda itemVenda = null;

        boolean efectuada = true;
        for ( int i = 0; i < listaIntes.size(); i++ )
        {
            try
            {
                ItemUtil item = listaIntes.get( i );
                double taxa = getTaxaImpostoIva( item.getIdProduto(), produtosImpostoController );
                TbProduto produto_local = ( TbProduto ) produtosController.findById( item.getIdProduto() );
                double totalLinha = getValorComImposto( item.getQtd(), taxa, getPrecoItem( item.getIdProduto() ), 0 );

                itemVenda = new TbItemVenda();
                itemVenda.setCodigoProduto( produto_local );
                itemVenda.setCodigoVenda( new TbVenda( cod_venda ) );
                itemVenda.setQuantidade( item.getQtd() );
                itemVenda.setDesconto( 0d );
                itemVenda.setValorIva( taxa );
                itemVenda.setValorRetencao( 0d );
                itemVenda.setMotivoIsensao( "" );
                itemVenda.setCodigoIsensao( "" );
                itemVenda.setTotal( new BigDecimal( totalLinha ) );
                itemVenda.setFkPreco( precosController.getLastIdPrecoByIdProduto( itemVenda.getCodigoProduto().getCodigo(), itemVenda.getQuantidade() ) );
                itemVenda.setDataServico( new Date() );
                /*setando a mesa e lugar para cunprir a formalidade só aplica-se somente para resstauração*/
                itemVenda.setFkLugares( ( TbLugares ) lugaresController.findById( DVML.LUGAR_BALCAO ) );
                itemVenda.setFkMesas( ( TbMesas ) mesasController.findById( DVML.MESA_BALCAO ) );

                System.out.println( itemVenda );
                //cria o item venda
//                int last_id_item_venda = itemVendaDao.criarComProcedimentos( itemVenda, conexao );
                if ( !itemVendasController.salvar( itemVenda ) )
                {
                    DocumentosController.rollback( conexaoTransaction );
                    conexaoTransaction.close();
                    return;
                }

                boolean isStocavel = produto_local.getStocavel().equals( "true" );

            }
            catch ( Exception e )
            {

                e.printStackTrace();
                efectuada = false;
                JOptionPane.showMessageDialog( null, "Falha ao registrar o produto: " + itemVenda.getCodigoProduto().getCodigo() + " na Factura" );
                DocumentosController.rollback( conexaoTransaction );
                conexaoTransaction.close();
                return;
            }

        }

        if ( efectuada )
        {
            System.out.println( "Factura efectuada com sucesso!.." );
            DocumentosController.commit( conexaoTransaction );
            conexaoTransaction.close();
        }

        listaIntes.clear();

    }

    public double getTotalIliquido()
    {
        double qtd = 0d;
        double total_iliquido = 0d, preco_unitario = 0d;
        for ( int i = 0; i < listaIntes.size(); i++ )
        {
            ItemUtil item = listaIntes.get( i );
            System.out.println( "PRODUTO ID: " + item.idProduto );
            TbPreco preco = precosController.getLastIdPrecoByIdProduto( item.getIdProduto(), 1 );
            preco_unitario = preco.getPrecoVenda().doubleValue();
            qtd = item.getQtd();
            total_iliquido += ( preco_unitario * qtd );
        }

        return total_iliquido;
    }

    public double getDescontoComercial()
    {
        return 0d;
    }

    public double getTotalImposto()
    {
        double qtd = 0d;
        double imposto = 0d, preco_unitario = 0d, desconto_valor_linha = 0d;
        BDConexao conexaoLocal = BDConexao.getInstancia();
        ProdutosImpostoController produtosImpostoController = new ProdutosImpostoController( conexaoLocal );
        for ( int i = 0; i < listaIntes.size(); i++ )
        {
            ItemUtil item = listaIntes.get( i );
            TbPreco preco = precosController.getLastIdPrecoByIdProduto( item.getIdProduto(), 0 );
            preco_unitario = preco.getPrecoVenda().doubleValue();

            qtd = item.getQtd();
            double taxa = getTaxaImpostoIva( item.getIdProduto(), produtosImpostoController );
            // a incidência só é aplicável ao produtos sujeitos a iva 
            if ( taxa != 0 )
            {
                double valor_unitario = (preco_unitario * qtd);
                desconto_valor_linha = 0;
                imposto += ( ( valor_unitario - desconto_valor_linha ) * ( taxa / 100 ) );
            }
        }
        conexaoLocal.close();
        return imposto;
    }

    public double getTotalRetencaoLiquido()
    {
        return 0d;
    }

    public double getDescontoFinanceiro()
    {
        return 0d;
    }

    public double getTotalAOALiquido()
    {
        double valores = (getTotalIliquido() + getTotalImposto());
        double descontos = (getDescontoComercial() + getDescontoFinanceiro());
//        System.out.println( "TotalIliquido: " + getTotalIliquido() );
//        System.out.println( "TotalImposto: " + getTotalImposto() );
//        System.out.println( "TotalDescontoComercial: " + getDescontoComercial() );
//        System.out.println( "TotalDescontoFinanceiro: " + getDescontoFinanceiro() );
//        System.out.println( "Total Liquido: " + ( valores - descontos ) );

        return ( valores - descontos );
    }

    public double getValor_entregue()
    {
        return getTotalAOALiquido();
    }

    public double getTroco()
    {
        return 0d;
    }

    public double getTotalIncidencia()
    {

        double qtd = 0;
        double incidencia = 0d, preco_unitario = 0d, desconto_valor_linha = 0;
        BDConexao conexaoLocal = BDConexao.getInstancia();
        ProdutosImpostoController produtosImpostoController = new ProdutosImpostoController( conexaoLocal );

        for ( int i = 0; i < listaIntes.size(); i++ )
        {
            ItemUtil item = listaIntes.get( i );
            TbPreco preco = precosController.getLastIdPrecoByIdProduto( item.getIdProduto(), 1 );
            preco_unitario = preco.getPrecoVenda().doubleValue();
            qtd = item.getQtd();
            double taxa = getTaxaImpostoIva( item.getIdProduto(), produtosImpostoController );
            if ( taxa != 0 )
            {
                desconto_valor_linha = 0;
                double valor_unitario = (preco_unitario * qtd);
                incidencia += ( ( valor_unitario ) - ( valor_unitario * desconto_valor_linha ) );

            }

        }
        conexaoLocal.close();

        return incidencia;
    }

    public double getTotalIncidenciaIsento()
    {
        return 0d;
    }

    private String getCodDocActualizador(
            DocumentosController documentosController,
            AnoEconomicoController anoEconomicoController,
            VendasController vendasController )
    {
        try
        {
            Documento documento = ( Documento ) documentosController.findById(
                    configuration.getDocumentoId()
            );

            AnoEconomico anoEconomico = ( AnoEconomico ) anoEconomicoController.findById(
                    configuration.getAnoEconomicoId()
            );

            // this.doc_prox_cod = documento.getCodUltimoDoc() + 1;
            int doc_prox_cod = vendasController.getUltimaContagemByIdDocumentoAndAnoEconomico(
                    configuration.getDocumentoId(),
                    configuration.getAnoEconomicoId() ) + 1;

            String prox_doc = documento.getAbreviacao();
            //FA Série / 4codigo
            prox_doc += " " + anoEconomico.getSerie() + "/" + doc_prox_cod;
            return prox_doc;
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            return "";
        }
    }

    private double getTotalVendaIVASemIncluirDesconto()
    {
        double taxa = 0, total_iva_local = 0, preco_unitario = 0, sub_total_iliquido = 0;
        double qtd = 0d;

//        DefaultTableModel modelo = ( DefaultTableModel ) table.getModel();
//        for ( int i = 0; i < modelo.getRowCount(); i++ )
//        {
//            preco_unitario = CfMethods.parseMoedaFormatada( modelo.getValueAt( i, 3 ).toString() );
//            qtd = Double.parseDouble( modelo.getValueAt( i, 4 ).toString() );
//            sub_total_iliquido = ( preco_unitario * qtd );
//            taxa = Double.parseDouble( modelo.getValueAt( i, 6 ).toString() );
//            total_iva_local += ( ( ( sub_total_iliquido ) * ( taxa / 100 ) ) );
//        }
        return total_iva_local;
    }

    private double getTaxaImpostoIva( int idProduto, ProdutosImpostoController produtosImpostoController )
    {
        try
        {
            return produtosImpostoController.getTaxaByIdProduto( idProduto );
        }
        catch ( Exception e )
        {
        }

        return 0;

    }

    private static double getValorComImposto( double qtd, double taxa, double preco_venda, double desconto )
    {
        double subtotal_linha = (preco_venda * qtd);
        double desconto_valor = (subtotal_linha * ( desconto / 100 ));
        double valor_iva = 1 + ( taxa / 100 );//
        return ( ( subtotal_linha - desconto_valor ) * valor_iva );

    }

    private double getGrossTotal()
    {
        System.out.println( "TOTALILIQUIDO: " + getTotalVendaIVASemIncluirDesconto() );
        System.out.println( "TOTALVENDAIVASEMDESCONTO: " + getTotalVendaIVASemIncluirDesconto() );
        return getTotalIliquido() + getTotalVendaIVASemIncluirDesconto();
    }

    class ItemUtil
    {

        private int idProduto;
        private double qtd;

        public ItemUtil()
        {
        }

        public int getIdProduto()
        {
            return idProduto;
        }

        public void setIdProduto( int idProduto )
        {
            this.idProduto = idProduto;
        }

        public double getQtd()
        {
            return qtd;
        }

        public void setQtd( double qtd )
        {
            this.qtd = qtd;
        }

    }

}
