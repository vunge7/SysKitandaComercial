/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author lenovo
 */
@Entity
@Table(name = "tb_conta")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "TbConta.findAll", query = "SELECT t FROM TbConta t"),
    @NamedQuery(name = "TbConta.findByIdContaPK", query = "SELECT t FROM TbConta t WHERE t.idContaPK = :idContaPK"),
    @NamedQuery(name = "TbConta.findByNumeroConta", query = "SELECT t FROM TbConta t WHERE t.numeroConta = :numeroConta"),
    @NamedQuery(name = "TbConta.findBySaldoConta", query = "SELECT t FROM TbConta t WHERE t.saldoConta = :saldoConta"),
    @NamedQuery(name = "TbConta.findByIban", query = "SELECT t FROM TbConta t WHERE t.iban = :iban")
})
public class TbConta implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idContaPK")
    private Integer idContaPK;
    @Column(name = "numero_conta")
    private String numeroConta;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "saldo_conta")
    private Double saldoConta;
    @Column(name = "iban")
    private String iban;
    @JoinColumn(name = "idBancoFK", referencedColumnName = "idBanco")
    @ManyToOne
    private TbBanco idBancoFK;
    @JoinColumn(name = "idFuncionarioFK", referencedColumnName = "idFuncionario")
    @ManyToOne
    private TbFuncionario idFuncionarioFK;

    public TbConta()
    {
    }

    public TbConta( Integer idContaPK )
    {
        this.idContaPK = idContaPK;
    }

    public Integer getIdContaPK()
    {
        return idContaPK;
    }

    public void setIdContaPK( Integer idContaPK )
    {
        this.idContaPK = idContaPK;
    }

    public String getNumeroConta()
    {
        return numeroConta;
    }

    public void setNumeroConta( String numeroConta )
    {
        this.numeroConta = numeroConta;
    }

    public Double getSaldoConta()
    {
        return saldoConta;
    }

    public void setSaldoConta( Double saldoConta )
    {
        this.saldoConta = saldoConta;
    }

    public String getIban()
    {
        return iban;
    }

    public void setIban( String iban )
    {
        this.iban = iban;
    }

    public TbBanco getIdBancoFK()
    {
        return idBancoFK;
    }

    public void setIdBancoFK( TbBanco idBancoFK )
    {
        this.idBancoFK = idBancoFK;
    }

    public TbFuncionario getIdFuncionarioFK()
    {
        return idFuncionarioFK;
    }

    public void setIdFuncionarioFK( TbFuncionario idFuncionarioFK )
    {
        this.idFuncionarioFK = idFuncionarioFK;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += ( idContaPK != null ? idContaPK.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if ( !( object instanceof TbConta ) )
        {
            return false;
        }
        TbConta other = ( TbConta ) object;
        if ( ( this.idContaPK == null && other.idContaPK != null ) || ( this.idContaPK != null && !this.idContaPK.equals( other.idContaPK ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.TbConta[ idContaPK=" + idContaPK + " ]";
    }
    
}
