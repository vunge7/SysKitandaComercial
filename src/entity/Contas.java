/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Domingos Dala Vunge
 */
@Entity
@Table(name = "contas")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "Contas.findAll", query = "SELECT c FROM Contas c"),
    @NamedQuery(name = "Contas.findByPkContas", query = "SELECT c FROM Contas c WHERE c.pkContas = :pkContas"),
    @NamedQuery(name = "Contas.findByDesignacao", query = "SELECT c FROM Contas c WHERE c.designacao = :designacao"),
    @NamedQuery(name = "Contas.findByNumero", query = "SELECT c FROM Contas c WHERE c.numero = :numero"),
    @NamedQuery(name = "Contas.findByIban", query = "SELECT c FROM Contas c WHERE c.iban = :iban"),
    @NamedQuery(name = "Contas.findByTitular1", query = "SELECT c FROM Contas c WHERE c.titular1 = :titular1"),
    @NamedQuery(name = "Contas.findByTitular2", query = "SELECT c FROM Contas c WHERE c.titular2 = :titular2"),
    @NamedQuery(name = "Contas.findBySaldo", query = "SELECT c FROM Contas c WHERE c.saldo = :saldo"),
    @NamedQuery(name = "Contas.findByObjecto", query = "SELECT c FROM Contas c WHERE c.objecto = :objecto"),
    @NamedQuery(name = "Contas.findByDataCriacao", query = "SELECT c FROM Contas c WHERE c.dataCriacao = :dataCriacao"),
    @NamedQuery(name = "Contas.findByUserId", query = "SELECT c FROM Contas c WHERE c.userId = :userId"),
    @NamedQuery(name = "Contas.findByTipoContaId", query = "SELECT c FROM Contas c WHERE c.tipoContaId = :tipoContaId")
})
public class Contas implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pk_contas")
    private Integer pkContas;
    @Column(name = "designacao")
    private String designacao;
    @Column(name = "numero")
    private String numero;
    @Column(name = "iban")
    private String iban;
    @Column(name = "titular_1")
    private String titular1;
    @Column(name = "titular_2")
    private String titular2;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "saldo")
    private BigDecimal saldo;
    @Column(name = "objecto")
    private String objecto;
    @Column(name = "data_criacao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataCriacao;
    @Column(name = "user_id")
    private Integer userId;
    @Column(name = "tipo_conta_id")
    private Integer tipoContaId;

    public Contas()
    {
    }

    public Contas( Integer pkContas )
    {
        this.pkContas = pkContas;
    }

    public Integer getPkContas()
    {
        return pkContas;
    }

    public void setPkContas( Integer pkContas )
    {
        this.pkContas = pkContas;
    }

    public String getDesignacao()
    {
        return designacao;
    }

    public void setDesignacao( String designacao )
    {
        this.designacao = designacao;
    }

    public String getNumero()
    {
        return numero;
    }

    public void setNumero( String numero )
    {
        this.numero = numero;
    }

    public String getIban()
    {
        return iban;
    }

    public void setIban( String iban )
    {
        this.iban = iban;
    }

    public String getTitular1()
    {
        return titular1;
    }

    public void setTitular1( String titular1 )
    {
        this.titular1 = titular1;
    }

    public String getTitular2()
    {
        return titular2;
    }

    public void setTitular2( String titular2 )
    {
        this.titular2 = titular2;
    }

    public BigDecimal getSaldo()
    {
        return saldo;
    }

    public void setSaldo( BigDecimal saldo )
    {
        this.saldo = saldo;
    }

    public String getObjecto()
    {
        return objecto;
    }

    public void setObjecto( String objecto )
    {
        this.objecto = objecto;
    }

    public Date getDataCriacao()
    {
        return dataCriacao;
    }

    public void setDataCriacao( Date dataCriacao )
    {
        this.dataCriacao = dataCriacao;
    }

    public Integer getUserId()
    {
        return userId;
    }

    public void setUserId( Integer userId )
    {
        this.userId = userId;
    }

    public Integer getTipoContaId()
    {
        return tipoContaId;
    }

    public void setTipoContaId( Integer tipoContaId )
    {
        this.tipoContaId = tipoContaId;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += ( pkContas != null ? pkContas.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if ( !( object instanceof Contas ) )
        {
            return false;
        }
        Contas other = ( Contas ) object;
        if ( ( this.pkContas == null && other.pkContas != null ) || ( this.pkContas != null && !this.pkContas.equals( other.pkContas ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.Contas[ pkContas=" + pkContas + " ]";
    }
    
}
