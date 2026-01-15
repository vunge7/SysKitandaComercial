/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package enties.util;

/**
 *
 * @author Engº Domingos Dala Vunge
 * @created 15/jan/2026
 * @lastModified 15/jan/2026
 */
public class CozinhaPedido
{

    private int pedido;
    private int pkItemPedidos;
    private String mesa;
    private String lugar;
    private String produto;
    private double quantidade;

    // getters e setters
    public int getPedido()
    {
        return pedido;
    }

    public void setPedido( int pedido )
    {
        this.pedido = pedido;
    }

    public String getMesa()
    {
        return mesa;
    }

    public void setMesa( String mesa )
    {
        this.mesa = mesa;
    }

    public String getLugar()
    {
        return lugar;
    }

    public void setLugar( String lugar )
    {
        this.lugar = lugar;
    }

    public String getProduto()
    {
        return produto;
    }

    public void setProduto( String produto )
    {
        this.produto = produto;
    }

    public double getQuantidade()
    {
        return quantidade;
    }

    public void setQuantidade( double quantidade )
    {
        this.quantidade = quantidade;
    }

    public int getPkItemPedidos()
    {
        return pkItemPedidos;
    }

    public void setPkItemPedidos( int pkItemPedidos )
    {
        this.pkItemPedidos = pkItemPedidos;
    }

}
