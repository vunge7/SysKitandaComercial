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
import java.util.List;

public class LineDTO
{

    private int lineNumber;
    private String productCode;
    private String productDescription;
    private String quantity;
    private String unitOfMeasure;
    private double unitPrice;
    private double unitPriceBase;
    private double debitAmount;
    private double creditAmount;
    private double settlementAmount;

    private List<TaxDTO> taxes;

    // getters & setters
    public int getLineNumber()
    {
        return lineNumber;
    }

    public void setLineNumber( int lineNumber )
    {
        this.lineNumber = lineNumber;
    }

    public String getProductCode()
    {
        return productCode;
    }

    public void setProductCode( String productCode )
    {
        this.productCode = productCode;
    }

    public String getProductDescription()
    {
        return productDescription;
    }

    public void setProductDescription( String productDescription )
    {
        this.productDescription = productDescription;
    }

    public String getQuantity()
    {
        return quantity;
    }

    public void setQuantity( String quantity )
    {
        this.quantity = quantity;
    }

    public String getUnitOfMeasure()
    {
        return unitOfMeasure;
    }

    public void setUnitOfMeasure( String unitOfMeasure )
    {
        this.unitOfMeasure = unitOfMeasure;
    }

    public double getUnitPrice()
    {
        return unitPrice;
    }

    public void setUnitPrice( double unitPrice )
    {
        this.unitPrice = unitPrice;
    }

    public double getUnitPriceBase()
    {
        return unitPriceBase;
    }

    public void setUnitPriceBase( double unitPriceBase )
    {
        this.unitPriceBase = unitPriceBase;
    }

    public double getDebitAmount()
    {
        return debitAmount;
    }

    public void setDebitAmount( double debitAmount )
    {
        this.debitAmount = debitAmount;
    }

    public double getCreditAmount()
    {
        return creditAmount;
    }

    public void setCreditAmount( double creditAmount )
    {
        this.creditAmount = creditAmount;
    }

    public double getSettlementAmount()
    {
        return settlementAmount;
    }

    public void setSettlementAmount( double settlementAmount )
    {
        this.settlementAmount = settlementAmount;
    }

    public List<TaxDTO> getTaxes()
    {
        return taxes;
    }

    public void setTaxes( List<TaxDTO> taxes )
    {
        this.taxes = taxes;
    }

}
