package util.invoiceauto;

import comercial.controller.*;
import entity.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.logging.Logger;
import util.BDConexao;
import static util.DVML.*;

public class FacturaService1
{

    private static final Logger logger = Logger.getLogger(FacturaService1.class.getName() );
    private static final int PRODUTO_AJUSTE_ID = 999;

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

    public FacturaService1( BDConexao conexao )
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

    /**
     * 🎯 Gera faturas até bater o total mensal
     */
    public void gerarFacturasComTotalMensal( BigDecimal totalAlvo )
    {

        BigDecimal acumulado = BigDecimal.ZERO;

        while ( true )
        {

            BigDecimal restante = totalAlvo.subtract( acumulado );

            if ( restante.compareTo( BigDecimal.ZERO ) == 0 )
            {
                logger.info( "✔ Total mensal atingido: " + acumulado );
                break;
            }

            List<Item> itens = generator.gerarItens( 10, 10, 11 );
            BigDecimal totalFactura = calculator.calcularTotalLiquido( itens );

            if ( totalFactura.compareTo( restante ) > 0 )
            {
                criarFacturaDeAjuste( restante );
                acumulado = acumulado.add( restante );
                break;
            }

            criarFactura( itens, totalFactura );
            acumulado = acumulado.add( totalFactura );

            logger.info( "Acumulado: " + acumulado );
        }
    }

    /**
     * 🧾 FACTURA NORMAL COMPLETA
     */
    private void criarFactura( List<Item> itens, BigDecimal totalCalculado )
    {

        try
        {
            DocumentosController.start( conexao );

            Date data = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime( data );
            calendar.add( Calendar.DATE, 15 );

            TbVenda venda = new TbVenda();

            venda.setDataVenda( data );
            venda.setRefDataFact( data );
            venda.setRefCodFact( "" );

            venda.setDataVencimento( calendar.getTime() );
            venda.setHora( data );

            venda.setNomeCliente( _CLIENTE_CONSUMIDOR_FINAL );
            venda.setClienteNif( _CLIENTE_CONSUMIDOR_FINAL );
            venda.setNomeConsumidorFinal( _CLIENTE_CONSUMIDOR_FINAL );

            // ✔ Totais vindos do calculator
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

            // ✔ outros campos obrigatórios
            venda.setDescontoTotal( BigDecimal.ZERO );
            venda.setIdBanco( new TbBanco( 1 ) );
            venda.setIdArmazemFK( new TbArmazem( ARMAZEM_DEFAUTL ) );
            venda.setCodigoUsuario( new TbUsuario( 18 ) );
            venda.setCodigoCliente( new TbCliente( 1 ) );
            venda.setFkAnoEconomico( new AnoEconomico( 4 ) );
            venda.setFkDocumento( new Documento( 1 ) );

            venda.setReferencia( "" );
            venda.setCont( 0 );

            venda.setStatusEliminado( "false" );
            venda.setPerformance( "false" );
            venda.setCredito( "false" );
            venda.setGorjeta( BigDecimal.ZERO );

            // ⚠️ simplificado (mantém tua lógica real se quiseres)
            venda.setCodFact( "AUTO-" + System.currentTimeMillis() );

            boolean salva = vendasController.salvar( venda );

            if ( !salva )
            {
                DocumentosController.rollback( conexao );
                return;
            }

            Integer vendaId = vendasController.getLastVenda().getCodigo();

            salvarItens( vendaId, itens );

            DocumentosController.commit( conexao );

        }
        catch ( Exception e )
        {
            e.printStackTrace();
            DocumentosController.rollback( conexao );
        }
    }

    /**
     * 🧾 FACTURA DE AJUSTE
     */
    private void criarFacturaDeAjuste( BigDecimal valor )
    {

        try
        {
            DocumentosController.start( conexao );

            Date data = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime( data );
            calendar.add( Calendar.DATE, 15 );

            // 🔹 Buscar produto de ajuste
            TbProduto produto = ( TbProduto ) produtosController.findById( PRODUTO_AJUSTE_ID );

            if ( produto == null )
            {
                throw new IllegalStateException( "Produto de ajuste não encontrado!" );
            }

            // 🔹 Taxa de imposto do produto
            double taxa = impostoController.getTaxaByIdProduto( produto.getCodigo() );

            BigDecimal totalIliquido;
            BigDecimal totalIva;

            // ✔ Se tiver IVA → separar base e imposto
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

            // 🔹 Criar venda completa
            TbVenda venda = new TbVenda();

            venda.setDataVenda( data );
            venda.setRefDataFact( data );
            venda.setRefCodFact( "" );

            venda.setDataVencimento( calendar.getTime() );
            venda.setHora( data );

            venda.setNomeCliente( _CLIENTE_CONSUMIDOR_FINAL );
            venda.setClienteNif( _CLIENTE_CONSUMIDOR_FINAL );
            venda.setNomeConsumidorFinal( _CLIENTE_CONSUMIDOR_FINAL );

            venda.setTotalGeral( totalIliquido );
            venda.setTotalIva( totalIva );
            venda.setTotalVenda( valor );

            venda.setDescontoComercial( BigDecimal.ZERO );
            venda.setDescontoFinanceiro( BigDecimal.ZERO );
            venda.setTotalRetencao( BigDecimal.ZERO );

            venda.setValorEntregue( valor );
            venda.setTroco( BigDecimal.ZERO );

            venda.setTotalIncidencia( totalIliquido );
            venda.setTotalIncidenciaIsento( BigDecimal.ZERO );

            venda.setDescontoTotal( BigDecimal.ZERO );
            venda.setIdBanco( new TbBanco( 1 ) );
            venda.setIdArmazemFK( new TbArmazem( ARMAZEM_DEFAUTL ) );
            venda.setCodigoUsuario( new TbUsuario( 18 ) );
            venda.setCodigoCliente( new TbCliente( 1 ) );
            venda.setFkAnoEconomico( new AnoEconomico( 4 ) );
            venda.setFkDocumento( new Documento( 1 ) );

            venda.setReferencia( "AJUSTE MENSAL" );
            venda.setCodFact( "AJUSTE-" + System.currentTimeMillis() );
            venda.setCont( 0 );

            venda.setStatusEliminado( "false" );
            venda.setPerformance( "false" );
            venda.setCredito( "false" );
            venda.setGorjeta( BigDecimal.ZERO );

            boolean salva = vendasController.salvar( venda );

            if ( !salva )
            {
                DocumentosController.rollback( conexao );
                return;
            }

            Integer vendaId = vendasController.getLastVenda().getCodigo();

            // 🔹 Criar item
            TbItemVenda item = new TbItemVenda();

            item.setCodigoVenda( new TbVenda( vendaId ) );
            item.setCodigoProduto( produto );

            item.setQuantidade( 1.0 );
            item.setDesconto( 0d );
            item.setValorIva( taxa );
            item.setValorRetencao( 0d );

            item.setMotivoIsensao( "" );
            item.setCodigoIsensao( "" );

            item.setTotal( valor );

            item.setFkPreco(
                    precosController.getLastIdPrecoByIdProduto( produto.getCodigo(), 1 )
            );

            item.setDataServico( data );

            item.setFkLugares( ( TbLugares ) lugaresController.findById( LUGAR_BALCAO ) );
            item.setFkMesas( ( TbMesas ) mesasController.findById( MESA_BALCAO ) );

            itemController.salvar( item );

            DocumentosController.commit( conexao );

            logger.info( "✔ Factura de ajuste criada com valor: " + valor );

        }
        catch ( Exception e )
        {
            e.printStackTrace();
            DocumentosController.rollback( conexao );
        }
    }

    /**
     * 📦 ITENS COMPLETOS (IGUAL AO TEU SISTEMA)
     */
    private void salvarItens( Integer vendaId, List<Item> itens )
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
                        .multiply( BigDecimal.valueOf( 1 + ( taxa / 100 ) ) );

                TbItemVenda iv = new TbItemVenda();
                iv.setCodigoVenda( new TbVenda( vendaId ) );
                iv.setCodigoProduto( produto );

                iv.setQuantidade( item.getQuantidade().doubleValue() );
                iv.setDesconto( 0d );
                iv.setValorIva( taxa );
                iv.setValorRetencao( 0d );
                iv.setMotivoIsensao( "" );
                iv.setCodigoIsensao( "" );

                iv.setTotal( totalLinha );
                iv.setFkPreco( precosController.getLastIdPrecoByIdProduto( produto.getCodigo(), iv.getQuantidade() ) );

                iv.setDataServico( new Date() );
                iv.setFkLugares( ( TbLugares ) lugaresController.findById( LUGAR_BALCAO ) );
                iv.setFkMesas( ( TbMesas ) mesasController.findById( MESA_BALCAO ) );

                itemController.salvar( iv );

            }
            catch ( Exception e )
            {
                e.printStackTrace();
                DocumentosController.rollback( conexao );
                return;
            }
        }
    }
}
