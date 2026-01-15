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
public class TaxDTO
{

    private String taxType;
    private String taxCountryRegion;
    private String taxCode;
    private String taxPercentage;
    private double taxContribution;

    // getters & setters
    public String getTaxType()
    {
        return taxType;
    }

    public void setTaxType( String taxType )
    {
        this.taxType = taxType;
    }

    public String getTaxCountryRegion()
    {
        return taxCountryRegion;
    }

    public void setTaxCountryRegion( String taxCountryRegion )
    {
        this.taxCountryRegion = taxCountryRegion;
    }

    public String getTaxCode()
    {
        return taxCode;
    }

    public void setTaxCode( String taxCode )
    {
        this.taxCode = taxCode;
    }

    public String getTaxPercentage()
    {
        return taxPercentage;
    }

    public void setTaxPercentage( String taxPercentage )
    {
        this.taxPercentage = taxPercentage;
    }

    public double getTaxContribution()
    {
        return taxContribution;
    }

    public void setTaxContribution( double taxContribution )
    {
        this.taxContribution = taxContribution;
    }

}
