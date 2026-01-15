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

public class DocumentDTO
{

    private String documentNo;
    private String documentStatus;
    private String jwsDocumentSignature;
    private String documentDate;
    private String documentType;
    private String eacCode;
    private String systemEntryDate;
    private String customerTaxID;
    private String customerCountry;
    private String companyName;

    private List<LineDTO> lines;
    private DocumentTotalsDTO documentTotals;
    private List<WithholdingTaxDTO> withholdingTaxList;

    // getters & setters
    public String getDocumentNo()
    {
        return documentNo;
    }

    public void setDocumentNo( String documentNo )
    {
        this.documentNo = documentNo;
    }

    public String getDocumentStatus()
    {
        return documentStatus;
    }

    public void setDocumentStatus( String documentStatus )
    {
        this.documentStatus = documentStatus;
    }

    public String getJwsDocumentSignature()
    {
        return jwsDocumentSignature;
    }

    public void setJwsDocumentSignature( String jwsDocumentSignature )
    {
        this.jwsDocumentSignature = jwsDocumentSignature;
    }

    public String getDocumentDate()
    {
        return documentDate;
    }

    public void setDocumentDate( String documentDate )
    {
        this.documentDate = documentDate;
    }

    public String getDocumentType()
    {
        return documentType;
    }

    public void setDocumentType( String documentType )
    {
        this.documentType = documentType;
    }

    public String getEacCode()
    {
        return eacCode;
    }

    public void setEacCode( String eacCode )
    {
        this.eacCode = eacCode;
    }

    public String getSystemEntryDate()
    {
        return systemEntryDate;
    }

    public void setSystemEntryDate( String systemEntryDate )
    {
        this.systemEntryDate = systemEntryDate;
    }

    public String getCustomerTaxID()
    {
        return customerTaxID;
    }

    public void setCustomerTaxID( String customerTaxID )
    {
        this.customerTaxID = customerTaxID;
    }

    public String getCustomerCountry()
    {
        return customerCountry;
    }

    public void setCustomerCountry( String customerCountry )
    {
        this.customerCountry = customerCountry;
    }

    public String getCompanyName()
    {
        return companyName;
    }

    public void setCompanyName( String companyName )
    {
        this.companyName = companyName;
    }

    public List<LineDTO> getLines()
    {
        return lines;
    }

    public void setLines( List<LineDTO> lines )
    {
        this.lines = lines;
    }

    public DocumentTotalsDTO getDocumentTotals()
    {
        return documentTotals;
    }

    public void setDocumentTotals( DocumentTotalsDTO documentTotals )
    {
        this.documentTotals = documentTotals;
    }

    public List<WithholdingTaxDTO> getWithholdingTaxList()
    {
        return withholdingTaxList;
    }

    public void setWithholdingTaxList( List<WithholdingTaxDTO> withholdingTaxList )
    {
        this.withholdingTaxList = withholdingTaxList;
    }

}
