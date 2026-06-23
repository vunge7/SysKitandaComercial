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
import java.math.BigDecimal;
import java.util.List;

public class LineDTO
{

    private int lineNumber;
    private String productCode;
    private String productDescription;
    private String quantity;
    private String unitOfMeasure;
    private BigDecimal unitPrice;
    private BigDecimal unitPriceBase;
    private List<ReferenceInfoDTO> referenceInfoDTOs;
    private BigDecimal debitAmount;
    private BigDecimal creditAmount;
    private BigDecimal settlementAmount;

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

    public BigDecimal getUnitPrice()
    {
        return unitPrice;
    }

    public void setUnitPrice( BigDecimal unitPrice )
    {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getUnitPriceBase()
    {
        return unitPriceBase;
    }

    public void setUnitPriceBase( BigDecimal unitPriceBase )
    {
        this.unitPriceBase = unitPriceBase;
    }

    public BigDecimal getDebitAmount()
    {
        return debitAmount;
    }

    public void setDebitAmount( BigDecimal debitAmount )
    {
        this.debitAmount = debitAmount;
    }

    public BigDecimal getCreditAmount()
    {
        return creditAmount;
    }

    public void setCreditAmount( BigDecimal creditAmount )
    {
        this.creditAmount = creditAmount;
    }

    public BigDecimal getSettlementAmount()
    {
        return settlementAmount;
    }

    public void setSettlementAmount( BigDecimal settlementAmount )
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

    public List<ReferenceInfoDTO> getReferenceInfoDTOs()
    {
        return referenceInfoDTOs;
    }

    public void setReferenceInfoDTOs( List<ReferenceInfoDTO> referenceInfoDTOs )
    {
        this.referenceInfoDTOs = referenceInfoDTOs;
    }

}
