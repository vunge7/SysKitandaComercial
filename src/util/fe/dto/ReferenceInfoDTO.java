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
public class ReferenceInfoDTO
{

    private String referenceItemLineNo;
    private String reference;
    private String reason;

    public ReferenceInfoDTO()
    {
    }

    public String getReferenceItemLineNo()
    {
        return referenceItemLineNo;
    }

    public void setReferenceItemLineNo( String referenceItemLineNo )
    {
        this.referenceItemLineNo = referenceItemLineNo;
    }

    public String getReference()
    {
        return reference;
    }

    public void setReference( String reference )
    {
        this.reference = reference;
    }

    public String getReason()
    {
        return reason;
    }

    public void setReason( String reason )
    {
        this.reason = reason;
    }

}
