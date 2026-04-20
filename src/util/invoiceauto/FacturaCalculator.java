/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util.invoiceauto;

/**
 *
 * @author Engº Domingos Dala Vunge
 * @created 20/abr/2026
 * @lastModified 20/abr/2026
 */
import comercial.controller.PrecosController;
import comercial.controller.ProdutosImpostoController;
import entity.TbPreco;
import java.math.BigDecimal;
import java.util.List;
import util.BDConexao;

public class FacturaCalculator
{

    private final PrecosController precosController;
    private final ProdutosImpostoController impostoController;

    public FacturaCalculator( BDConexao conexao )
    {
        this.precosController = new PrecosController( conexao );
        this.impostoController = new ProdutosImpostoController( conexao );
    }

    public BigDecimal calcularTotalIliquido( List<Item> itens )
    {
        BigDecimal total = BigDecimal.ZERO;

        for ( Item item : itens )
        {
            TbPreco preco = precosController.getLastIdPrecoByIdProduto( item.getProdutoId(), 0 );
            if ( preco == null )
            {
                continue;
            }

            BigDecimal precoUnitario = preco.getPrecoVenda();
            total = total.add( precoUnitario.multiply( item.getQuantidade() ) );
        }

        return total;
    }

    public BigDecimal calcularImposto( List<Item> itens )
    {
        BigDecimal imposto = BigDecimal.ZERO;

        for ( Item item : itens )
        {
            TbPreco preco = precosController.getLastIdPrecoByIdProduto( item.getProdutoId(), 0 );
            if ( preco == null )
            {
                continue;
            }

            BigDecimal precoUnitario = preco.getPrecoVenda();
            BigDecimal subtotal = precoUnitario.multiply( item.getQuantidade() );

            double taxa = impostoController.getTaxaByIdProduto( item.getProdutoId() );

            if ( taxa > 0 )
            {
                BigDecimal taxaBD = BigDecimal.valueOf( taxa ).divide( BigDecimal.valueOf( 100 ) );
                imposto = imposto.add( subtotal.multiply( taxaBD ) );
            }
        }

        return imposto;
    }

    public BigDecimal calcularTotalLiquido( List<Item> itens )
    {
        return calcularTotalIliquido( itens ).add( calcularImposto( itens ) );
    }
}
