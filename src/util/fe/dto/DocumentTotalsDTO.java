/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util.fe.dto;

import java.math.BigDecimal;

/**
 *
 * @author Engº Domingos Dala Vunge
 * @created 13/jan/2026
 * @lastModified 13/jan/2026
 */
public class DocumentTotalsDTO
{

    private BigDecimal taxPayable;
    private BigDecimal netTotal;
    private BigDecimal grossTotal;

    // getters & setters

    public BigDecimal getTaxPayable()
    {
        return taxPayable;
    }

    public void setTaxPayable( BigDecimal taxPayable )
    {
        this.taxPayable = taxPayable;
    }

    public BigDecimal getNetTotal()
    {
        return netTotal;
    }

    public void setNetTotal( BigDecimal netTotal )
    {
        this.netTotal = netTotal;
    }

    public BigDecimal getGrossTotal()
    {
        return grossTotal;
    }

    public void setGrossTotal( BigDecimal grossTotal )
    {
        this.grossTotal = grossTotal;
    }
    
}
