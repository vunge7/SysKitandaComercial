/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util.invoiceauto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Engº Domingos Dala Vunge
 * @created 20/abr/2026
 * @lastModified 20/abr/2026
 */
public class FacturaGenerator
{

    private final Random random = new Random();
    public List<Item> gerarItens( int maxLinhas, int maxQtd, int maxProduto )
    {
        List<Item> itens = new ArrayList<>();
        int tamanho = random.nextInt( maxLinhas ) + 1;

        while ( itens.size() < tamanho )
        {
            int produtoId = random.nextInt( maxProduto ) + 1;

            boolean existe = itens.stream()
                    .anyMatch( i -> i.getProdutoId() == produtoId );

            if ( !existe )
            {
                BigDecimal qtd = BigDecimal.valueOf( random.nextInt( maxQtd ) + 1 );
                itens.add( new Item( produtoId, qtd ) );
            }
        }

        return itens;
    }
}
