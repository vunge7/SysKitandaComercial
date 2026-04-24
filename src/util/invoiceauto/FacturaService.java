package util.invoiceauto;

import comercial.controller.*;
import entity.*;
import util.BDConexao;
import java.math.BigDecimal;
import java.util.*;
import java.util.logging.Logger;
import static util.DVML.*;

public class FacturaService
{

    private static final Logger logger = Logger.getLogger( FacturaService.class.getName() );
    private static final int PRODUTO_AJUSTE_ID = 424;

    private final BDConexao conexao;
    private final FacturaGenerator generator;
    private final FacturaCalculator calculator;

    private final VendasController vendasController;
    private final ItemVendasController itemController;
    private final ProdutosController produtosController;
    private final PrecosController precosController;
    private final ProdutosImpostoController impostoController;
    private final LugaresController lugaresController;
    private final MesasController mesasController;

    public FacturaService( BDConexao conexao )
    {
        this.conexao = conexao;

        this.generator = new FacturaGenerator();
        this.calculator = new FacturaCalculator( conexao );

        this.vendasController = new VendasController( conexao );
        this.itemController = new ItemVendasController( conexao );
        this.produtosController = new ProdutosController( conexao );
        this.precosController = new PrecosController( conexao );
        this.impostoController = new ProdutosImpostoController( conexao );
        this.lugaresController = new LugaresController( conexao );
        this.mesasController = new MesasController( conexao );
    }

    // =========================================================
    // 🎯 ENTRY POINT
    // =========================================================
    public void gerarFacturasComTotalMensal( int ano, int mes, BigDecimal totalAlvo )
    {

        BigDecimal acumulado = BigDecimal.ZERO;

        logger.info( "▶ Início geração: " + ano + "-" + mes + " Total: " + totalAlvo );

        while ( acumulado.compareTo( totalAlvo ) < 0 )
        {

            BigDecimal restante = totalAlvo.subtract( acumulado );

            List<Item> itens = generator.gerarItens( 10, 10, 11 );

            ajustarDataItens( itens, ano, mes );

            BigDecimal totalFactura = calculator.calcularTotalLiquido( itens );

            if ( totalFactura.compareTo( restante ) > 0 )
            {

                logger.info( "⚠ Ajuste necessário para fechar total mensal" );

                criarFacturaDeAjuste( restante, ano, mes );

                acumulado = acumulado.add( restante );
                break;
            }

            criarFactura( itens, totalFactura, ano, mes );

            acumulado = acumulado.add( totalFactura );

            logger.info( "✔ Acumulado: " + acumulado );
        }

        logger.info( "✔ FIM - Total final: " + acumulado );
    }

    // =========================================================
    // 🧾 FACTURA NORMAL
    // =========================================================
    private void criarFactura( List<Item> itens, BigDecimal totalCalculado, int ano, int mes )
    {

        try
        {
            DocumentosController.start( conexao );

            Date data = gerarDataAleatoriaNoMes( ano, mes );

            TbVenda venda = new TbVenda();

            venda.setDataVenda( data );
            venda.setRefDataFact( data );
            venda.setHora( data );

            venda.setNomeCliente( _CLIENTE_CONSUMIDOR_FINAL );
            venda.setClienteNif( _CLIENTE_CONSUMIDOR_FINAL );
            venda.setNomeConsumidorFinal( _CLIENTE_CONSUMIDOR_FINAL );

            venda.setTotalGeral( calculator.calcularTotalIliquido( itens ) );
            venda.setTotalIva( calculator.calcularImposto( itens ) );
            venda.setTotalVenda( totalCalculado );

            venda.setDescontoComercial( BigDecimal.ZERO );
            venda.setDescontoFinanceiro( BigDecimal.ZERO );
            venda.setTotalRetencao( BigDecimal.ZERO );

            venda.setValorEntregue( totalCalculado );
            venda.setTroco( BigDecimal.ZERO );

            venda.setTotalIncidencia( venda.getTotalGeral() );
            venda.setTotalIncidenciaIsento( BigDecimal.ZERO );

            venda.setIdBanco( new TbBanco( 1 ) );
            venda.setIdArmazemFK( new TbArmazem( ARMAZEM_DEFAUTL ) );
            venda.setCodigoUsuario( new TbUsuario( 18 ) );
            venda.setCodigoCliente( new TbCliente( 1 ) );
            venda.setFkAnoEconomico( new AnoEconomico( ano ) );
            venda.setFkDocumento( new Documento( 1 ) );

            venda.setCodFact( "AUTO-" + System.currentTimeMillis() );
            venda.setStatusEliminado( "false" );
            venda.setPerformance( "false" );
            venda.setCredito( "false" );
            venda.setGorjeta( BigDecimal.ZERO );

            if ( !vendasController.salvar( venda ) )
            {
                DocumentosController.rollback( conexao );
                return;
            }

            Integer vendaId = vendasController.getLastVenda().getCodigo();

            salvarItens( vendaId, itens, data );

            DocumentosController.commit( conexao );

        }
        catch ( Exception e )
        {
            e.printStackTrace();
            DocumentosController.rollback( conexao );
        }
    }

    // =========================================================
    // 🧾 FACTURA AJUSTE
    // =========================================================
    private void criarFacturaDeAjuste( BigDecimal valor, int ano, int mes )
    {

        try
        {
            DocumentosController.start( conexao );

            Date data = gerarDataAleatoriaNoMes( ano, mes );

            System.out.println( "PRODUTO CONTROLLER " + Objects.isNull( produtosController ) );
            TbProduto produto = ( TbProduto ) produtosController.findById( PRODUTO_AJUSTE_ID );

            System.err.println( "CODIGO: " + produto.getCodigo() );;
            if ( Objects.isNull( produto ) )
            {
                throw new IllegalStateException( "Produto de ajuste não encontrado!" );
            }

            double taxa = impostoController.getTaxaByIdProduto( produto.getCodigo() );

            BigDecimal totalIliquido;
            BigDecimal totalIva;

            if ( taxa > 0 )
            {
                BigDecimal divisor = BigDecimal.valueOf( 1 + ( taxa / 100 ) );
                totalIliquido = valor.divide( divisor, 2, BigDecimal.ROUND_HALF_UP );
                totalIva = valor.subtract( totalIliquido );
            }
            else
            {
                totalIliquido = valor;
                totalIva = BigDecimal.ZERO;
            }

            TbVenda venda = new TbVenda();

            venda.setDataVenda( data );
            venda.setRefDataFact( data );
            venda.setHora( data );

            venda.setNomeCliente( _CLIENTE_CONSUMIDOR_FINAL );
            venda.setClienteNif( _CLIENTE_CONSUMIDOR_FINAL );
            venda.setNomeConsumidorFinal( _CLIENTE_CONSUMIDOR_FINAL );

            venda.setTotalGeral( totalIliquido );
            venda.setTotalIva( totalIva );
            venda.setTotalVenda( valor );

            venda.setIdBanco( new TbBanco( 1 ) );
            venda.setIdArmazemFK( new TbArmazem( ARMAZEM_DEFAUTL ) );
            venda.setCodigoUsuario( new TbUsuario( 18 ) );
            venda.setCodigoCliente( new TbCliente( 1 ) );
            venda.setFkAnoEconomico( new AnoEconomico( ano ) );
            venda.setFkDocumento( new Documento( 1 ) );

            venda.setCodFact( "AJUSTE-" + System.currentTimeMillis() );

            if ( !vendasController.salvar( venda ) )
            {
                DocumentosController.rollback( conexao );
                return;
            }

            Integer vendaId = vendasController.getLastVenda().getCodigo();

            TbItemVenda item = new TbItemVenda();
            item.setCodigoVenda( new TbVenda( vendaId ) );
            item.setCodigoProduto( produto );
            item.setQuantidade( 1.0 );
            item.setTotal( valor );
            item.setValorIva( taxa );
            item.setDataServico( data );

            itemController.salvar( item );

            DocumentosController.commit( conexao );

        }
        catch ( Exception e )
        {
            e.printStackTrace();
            DocumentosController.rollback( conexao );
        }
    }

    // =========================================================
    // 📦 ITENS
    // =========================================================
    private void salvarItens( Integer vendaId, List<Item> itens, Date data )
    {

        for ( Item item : itens )
        {

            try
            {
                TbProduto produto = ( TbProduto ) produtosController.findById( item.getProdutoId() );

                if ( produto == null )
                {
                    continue;
                }

                double taxa = impostoController.getTaxaByIdProduto( item.getProdutoId() );

                BigDecimal preco = precosController
                        .getLastIdPrecoByIdProduto( produto.getCodigo(), 0 )
                        .getPrecoVenda();

                BigDecimal totalLinha = preco
                        .multiply( item.getQuantidade() )
                        .multiply( BigDecimal.valueOf( 1 + taxa / 100 ) );

                TbItemVenda iv = new TbItemVenda();
                iv.setCodigoVenda( new TbVenda( vendaId ) );
                iv.setCodigoProduto( produto );
                iv.setQuantidade( item.getQuantidade().doubleValue() );
                iv.setValorIva( taxa );
                iv.setTotal( totalLinha );
                iv.setDataServico( data );

                itemController.salvar( iv );

            }
            catch ( Exception e )
            {
                e.printStackTrace();
            }
        }
    }

    // =========================================================
    // 📅 DATA ALEATÓRIA NO MÊS
    // =========================================================
    private Date gerarDataAleatoriaNoMes( int ano, int mes )
    {

        Calendar cal = Calendar.getInstance();

        cal.set( Calendar.YEAR, ano );
        cal.set( Calendar.MONTH, mes - 1 );

        int max = cal.getActualMaximum( Calendar.DAY_OF_MONTH );

        cal.set( Calendar.DAY_OF_MONTH, new Random().nextInt( max ) + 1 );
        cal.set( Calendar.HOUR_OF_DAY, new Random().nextInt( 23 ) );
        cal.set( Calendar.MINUTE, new Random().nextInt( 59 ) );

        return cal.getTime();
    }

    private void ajustarDataItens( List<Item> itens, int ano, int mes )
    {
        // reservado para futuras regras (opcional)
    }
}
