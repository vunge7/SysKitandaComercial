/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util.excel;


import java.sql.Connection;
import java.math.BigDecimal;

/**
 *
 * @author Domingos Dala Vunge
 */
public class DadosUtil
{

    private String codManual;
    private String categoria;
    private String designacao;
    private BigDecimal precoVenda;
    private String codBarra;
    private double stock_actual;
    private BigDecimal precoCompra;

    public DadosUtil()
    {
    }
    
        public double getStock_actual()
    {
        return stock_actual;
    }

    public void setStock_actual( double stock_actual )
    {
        this.stock_actual = stock_actual;
    }

        public BigDecimal getPrecoCompra()
    {
        return precoCompra;
    }

    public void setPrecoCompra( BigDecimal precoCompra )
    {
        this.precoCompra = precoCompra;
    }
    

    public String getCodManual()
    {
        return codManual;
    }

    public void setCodManual( String codManual )
    {
        this.codManual = codManual;
    }

    public String getCategoria()
    {
        return categoria;
    }

    public void setCategoria( String categoria )
    {
        this.categoria = categoria;
    }

    public String getDesignacao()
    {
        return designacao;
    }

    public void setDesignacao( String designacao )
    {
        this.designacao = designacao;
    }

    public BigDecimal getPrecoVenda()
    {
        return precoVenda;
    }

    public void setPrecoVenda( BigDecimal precoVenda )
    {
        this.precoVenda = precoVenda;
    }
    
        
    public String getCodBarra()
    {
        return codBarra;
    }

    public void setCodBarra( String codBarra )
    {
        this.codBarra = codBarra;
    }
    
    
    

}
