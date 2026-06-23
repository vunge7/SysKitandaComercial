/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util.fe.dto;

import java.math.BigDecimal;

/**
 *
 * @author Engº Domingos Dala Vunge
 * @created 1/fev/2026
 * @lastModified 1/fev/2026
 */
public class SourceDocumentDTO
{

    private int lineNo;
    private String originatingON;
    private String documentDate;
    private BigDecimal creditAmount;

    public SourceDocumentDTO()
    {
    }

    public int getLineNo()
    {
        return lineNo;
    }

    public void setLineNo( int lineNo )
    {
        this.lineNo = lineNo;
    }

    public String getOriginatingON()
    {
        return originatingON;
    }

    public void setOriginatingON( String originatingON )
    {
        this.originatingON = originatingON;
    }

    public String getDocumentDate()
    {
        return documentDate;
    }

    public void setDocumentDate( String documentDate )
    {
        this.documentDate = documentDate;
    }

    public BigDecimal getCreditAmount()
    {
        return creditAmount;
    }

    public void setCreditAmount( BigDecimal creditAmount )
    {
        this.creditAmount = creditAmount;
    }
    
    
}
