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
public class WithholdingTaxDTO
{

    private String withholdingTaxType;
    private String withholdingTaxDescription;
    private double withholdingTaxAmount;

    // getters & setters
    public String getWithholdingTaxType()
    {
        return withholdingTaxType;
    }

    public void setWithholdingTaxType( String withholdingTaxType )
    {
        this.withholdingTaxType = withholdingTaxType;
    }

    public String getWithholdingTaxDescription()
    {
        return withholdingTaxDescription;
    }

    public void setWithholdingTaxDescription( String withholdingTaxDescription )
    {
        this.withholdingTaxDescription = withholdingTaxDescription;
    }

    public double getWithholdingTaxAmount()
    {
        return withholdingTaxAmount;
    }

    public void setWithholdingTaxAmount( double withholdingTaxAmount )
    {
        this.withholdingTaxAmount = withholdingTaxAmount;
    }

}
