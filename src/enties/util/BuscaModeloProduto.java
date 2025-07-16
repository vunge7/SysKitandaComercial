/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enties.util;

/**
 *
 * @author Domingos Dala Vunge
 */
public class BuscaModeloProduto
{
    
    private int codigo;
    private String designacao;
    private String categoria;
    private String qtd;
    private double precoVenda;
    private String estadoCritico;

    public BuscaModeloProduto()
    {
    }

    public int getCodigo()
    {
        return codigo;
    }

    public void setCodigo( int codigo )
    {
        this.codigo = codigo;
    }

    public String getDesignacao()
    {
        return designacao;
    }

    public void setDesignacao( String designacao )
    {
        this.designacao = designacao;
    }

    public String getCategoria()
    {
        return categoria;
    }

    public void setCategoria( String categoria )
    {
        this.categoria = categoria;
    }

    public String getQtd()
    {
        return qtd;
    }

    public void setQtd( String qtd )
    {
        this.qtd = qtd;
    }

    public double getPrecoVenda()
    {
        return precoVenda;
    }

    public void setPrecoVenda( double precoVenda )
    {
        this.precoVenda = precoVenda;
    }

    public String getEstadoCritico()
    {
        return estadoCritico;
    }

    public void setEstadoCritico( String estadoCritico )
    {
        this.estadoCritico = estadoCritico;
    }
    
    
    
    
    
    
}
