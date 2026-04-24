/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util.invoiceauto;

import java.math.BigDecimal;

/**
 *
 * @author Engº Domingos Dala Vunge
 * @created 20/abr/2026
 * @lastModified 20/abr/2026
 */
public class Item
{

    private final int produtoId;
    private final BigDecimal quantidade;

    public Item( int produtoId, BigDecimal quantidade )
    {
        this.produtoId = produtoId;
        this.quantidade = quantidade;
    }

    public int getProdutoId()
    {
        return produtoId;
    }

    public BigDecimal getQuantidade()
    {
        return quantidade;
    }
}
