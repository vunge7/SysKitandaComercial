/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;


import java.sql.Connection;
import java.math.BigDecimal;
import java.math.RoundingMode;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import kitanda.util.CfMethods;

/**
 *
 * @author Domingos Dala Vunge
 */
public class FinanceUtils
{

    public static BigDecimal getTotalIliquidoTable( int indexColunaPreco, int indexColunaQtd, JTable table )
    {
        DefaultTableModel modelo = (DefaultTableModel) table.getModel();
        BigDecimal totalIliquido = BigDecimal.ZERO;

        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {
            // Coluna 3: Preço Unitário, Coluna 4: Quantidade
            BigDecimal precoUnitario = BigDecimal.valueOf(
                    CfMethods.parseMoedaFormatada(
                            modelo.getValueAt( i, indexColunaPreco ).toString() ) );
            BigDecimal quantidade = new BigDecimal( modelo.getValueAt( i, indexColunaQtd ).toString() );

            BigDecimal subtotal = precoUnitario.multiply( quantidade );
            totalIliquido = totalIliquido.add( subtotal );
        }
        return totalIliquido.setScale( 2, RoundingMode.HALF_UP );
    }

    public static double getTotalImpostoTable(
            int indexColPreco,
            int indexColQtd,
            int indexColDesconto,
            int indexColTaxaIva,
            JTable table )
    {
        DefaultTableModel modelo = (DefaultTableModel) table.getModel();
        double qtd = 0d;
        double imposto = 0d, preco_unitario = 0d, desconto_valor_linha = 0d;

        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {
            preco_unitario = CfMethods.parseMoedaFormatada( modelo.getValueAt( i, indexColPreco ).toString() );
            qtd = Double.parseDouble( modelo.getValueAt( i, indexColQtd ).toString() );
            double desconto_percentagem = Double.parseDouble( modelo.getValueAt( i, indexColDesconto ).toString() );
            double taxaIva = Double.parseDouble( modelo.getValueAt( i, indexColTaxaIva ).toString() );
            // a incidência só é aplicável ao produtos sujeitos a iva 
            if ( taxaIva != 0 )
            {
                double valor_unitario = ( preco_unitario * qtd );
                desconto_valor_linha = valor_unitario * ( ( desconto_percentagem ) / 100 );
                imposto += ( ( valor_unitario - desconto_valor_linha ) * ( taxaIva / 100 ) );

            }

        }

        return imposto;
    }

    public static double getTotalIncidenciaTable(
            int indexColPreco,
            int indexColQtd,
            int indexColDesconto,
            int indexColTaxaIva,
            JTable table )
    {
        DefaultTableModel modelo = (DefaultTableModel) table.getModel();
        double qtd = 0;
        double incidencia = 0d, preco_unitario = 0d, desconto_valor_linha = 0;

        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {
            preco_unitario = CfMethods.parseMoedaFormatada( modelo.getValueAt( i, indexColPreco ).toString() );
            qtd = Double.parseDouble( modelo.getValueAt( i, indexColQtd ).toString() );
            double desconto_percentagem = Double.parseDouble( modelo.getValueAt( i, indexColDesconto ).toString() );
            double taxaIva = Double.parseDouble( modelo.getValueAt( i, indexColTaxaIva ).toString() );
            // a incidência só é aplicável ao produtos sujeitos a iva 
            if ( taxaIva != 0 )
            {
                desconto_valor_linha = ( ( desconto_percentagem ) / 100 );
                double valor_unitario = ( preco_unitario * qtd );
                incidencia += ( ( valor_unitario ) - ( valor_unitario * desconto_valor_linha ) );
            }
        }

        return incidencia;
    }

    public static BigDecimal getDescontoComercial(
            int indexColPreco,
            int indexColQtd,
            int indexColDesconto,
            JTable table )
    {
        DefaultTableModel modelo = (DefaultTableModel) table.getModel();

        BigDecimal descontoComercial = BigDecimal.ZERO;

        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {
            // Preço unitário convertido com BigDecimal
            BigDecimal precoUnitario = BigDecimal.valueOf(
                    CfMethods.parseMoedaFormatada( modelo.getValueAt( i, indexColPreco ).toString() )
            );
            // Quantidade
            BigDecimal qtd = new BigDecimal( modelo.getValueAt( i, indexColQtd ).toString() );
            // Percentagem do desconto
            BigDecimal valorPercentagem = new BigDecimal( modelo.getValueAt( i, indexColDesconto ).toString() );
            // Converte percentagem para fração (ex: 10 -> 0.10)
            BigDecimal descontoPercentual = valorPercentagem.divide( BigDecimal.valueOf( 100 ), 6, RoundingMode.HALF_UP );
            // valor_unitario = preco_unitario * qtd
            BigDecimal valorUnitario = precoUnitario.multiply( qtd );
            // desconto_comercial += valor_unitario * descontoPercentual
            descontoComercial = descontoComercial.add( valorUnitario.multiply( descontoPercentual ) );
        }

        // Retorna com 2 casas decimais, arredondado
        return descontoComercial.setScale( 2, RoundingMode.HALF_UP );
    }

    public static BigDecimal getTotalIncidenciaIsento(
            int indexColPreco,
            int indexColQtd,
            int indexColDesconto,
            int indexColTaxaIva,
            JTable table )
    {

        DefaultTableModel modelo = (DefaultTableModel) table.getModel();
        BigDecimal incidenciaIsento = BigDecimal.ZERO;

        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {
            // Preço unitário
            BigDecimal precoUnitario = BigDecimal.valueOf(
                    CfMethods.parseMoedaFormatada( modelo.getValueAt( i, indexColPreco ).toString() )
            );

            // Quantidade
            BigDecimal qtd = new BigDecimal( modelo.getValueAt( i, indexColQtd ).toString() );

            // Percentagem de desconto
            BigDecimal descontoPercentagem = new BigDecimal( modelo.getValueAt( i, indexColDesconto ).toString() );

            // Taxa de IVA
            BigDecimal taxaIva = new BigDecimal( modelo.getValueAt( i, indexColTaxaIva ).toString() );

            // Verifica se é isento (taxa de IVA == 0)
            if ( taxaIva.compareTo( BigDecimal.ZERO ) == 0 )
            {
                // Converte percentagem para fração (ex: 10 -> 0.10)
                BigDecimal descontoFator = descontoPercentagem.divide( BigDecimal.valueOf( 100 ), 6, RoundingMode.HALF_UP );

                // valor_unitario = preco_unitario * qtd
                BigDecimal valorUnitario = precoUnitario.multiply( qtd );

                // aplica desconto: valor_unitario - (valor_unitario * descontoFator)
                BigDecimal valorComDesconto = valorUnitario.subtract( valorUnitario.multiply( descontoFator ) );

                // soma ao total
                incidenciaIsento = incidenciaIsento.add( valorComDesconto );
            }
        }

        // Retorna arredondado a 2 casas decimais
        return incidenciaIsento.setScale( 2, RoundingMode.HALF_UP );
    }

    public static double getValorComIVA( double qtd, double taxa, double preco, double desconto )
    {
        BigDecimal precoBD = BigDecimal.valueOf( preco );
        BigDecimal qtdBD = BigDecimal.valueOf( qtd );
        BigDecimal subtotal = precoBD.multiply( qtdBD );
        BigDecimal descontoBD = subtotal.multiply( BigDecimal.valueOf( desconto ).divide( BigDecimal.valueOf( 100 ) ) );
        BigDecimal base = subtotal.subtract( descontoBD );
        BigDecimal iva = BigDecimal.ONE.add( BigDecimal.valueOf( taxa ).divide( BigDecimal.valueOf( 100 ) ) );
        BigDecimal total = base.multiply( iva );
        return total.setScale( 2, RoundingMode.HALF_UP ).doubleValue();
    }

    public static BigDecimal getValorIliquido( BigDecimal qtd, BigDecimal precoVenda, BigDecimal desconto )
    {
        // subtotal = preço × quantidade
        BigDecimal subtotal = precoVenda.multiply( qtd );

        // desconto = subtotal × (desconto / 100)
        BigDecimal descontoValor = subtotal.multiply( desconto )
                .divide( BigDecimal.valueOf( 100 ), 2, RoundingMode.HALF_UP );

        // valor ilíquido = subtotal - desconto
        BigDecimal valorIliquido = subtotal.subtract( descontoValor );
        // retorna com 2 casas decimais
        return valorIliquido.setScale( 2, RoundingMode.HALF_UP );
    }
}
