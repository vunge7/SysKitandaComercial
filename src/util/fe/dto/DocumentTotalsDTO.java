/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util.fe.dto;

/**
 *
 * @author Engº Domingos Dala Vunge
 * @created 13/jan/2026
 * @lastModified 13/jan/2026
 */
public class DocumentTotalsDTO
{

    private double taxPayable;
    private double netTotal;
    private double grossTotal;

    // getters & setters

    public double getTaxPayable()
    {
        return taxPayable;
    }

    public void setTaxPayable( double taxPayable )
    {
        this.taxPayable = taxPayable;
    }

    public double getNetTotal()
    {
        return netTotal;
    }

    public void setNetTotal( double netTotal )
    {
        this.netTotal = netTotal;
    }

    public double getGrossTotal()
    {
        return grossTotal;
    }

    public void setGrossTotal( double grossTotal )
    {
        this.grossTotal = grossTotal;
    }
    
}
