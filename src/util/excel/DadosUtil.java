/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util.excel;

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

    public DadosUtil()
    {
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
    
    
    

}
