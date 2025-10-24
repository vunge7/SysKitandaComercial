/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;


import java.sql.Connection;
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
@Table(name = "conta_operacoes")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "ContaOperacoes.findAll", query = "SELECT c FROM ContaOperacoes c"),
    @NamedQuery(name = "ContaOperacoes.findByPkContaOperacao", query = "SELECT c FROM ContaOperacoes c WHERE c.pkContaOperacao = :pkContaOperacao"),
    @NamedQuery(name = "ContaOperacoes.findByDataHora", query = "SELECT c FROM ContaOperacoes c WHERE c.dataHora = :dataHora"),
    @NamedQuery(name = "ContaOperacoes.findByTipo", query = "SELECT c FROM ContaOperacoes c WHERE c.tipo = :tipo"),
    @NamedQuery(name = "ContaOperacoes.findByValor", query = "SELECT c FROM ContaOperacoes c WHERE c.valor = :valor"),
    @NamedQuery(name = "ContaOperacoes.findByUserId", query = "SELECT c FROM ContaOperacoes c WHERE c.userId = :userId"),
    @NamedQuery(name = "ContaOperacoes.findByUserName", query = "SELECT c FROM ContaOperacoes c WHERE c.userName = :userName"),
    @NamedQuery(name = "ContaOperacoes.findByBeneficiario", query = "SELECT c FROM ContaOperacoes c WHERE c.beneficiario = :beneficiario")
})
public class ContaOperacoes implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pk_conta_operacao")
    private Long pkContaOperacao;
    @Column(name = "data_hora")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataHora;
    @Basic(optional = false)
    @Column(name = "tipo")
    private String tipo;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @Column(name = "valor")
    private BigDecimal valor;
    @Column(name = "user_id")
    private Integer userId;
    @Column(name = "user_name")
    private String userName;
    @Column(name = "beneficiario")
    private String beneficiario;

    public ContaOperacoes()
    {
    }

    public ContaOperacoes( Long pkContaOperacao )
    {
        this.pkContaOperacao = pkContaOperacao;
    }

    public ContaOperacoes( Long pkContaOperacao, String tipo, BigDecimal valor )
    {
        this.pkContaOperacao = pkContaOperacao;
        this.tipo = tipo;
        this.valor = valor;
    }

    public Long getPkContaOperacao()
    {
        return pkContaOperacao;
    }

    public void setPkContaOperacao( Long pkContaOperacao )
    {
        this.pkContaOperacao = pkContaOperacao;
    }

    public Date getDataHora()
    {
        return dataHora;
    }

    public void setDataHora( Date dataHora )
    {
        this.dataHora = dataHora;
    }

    public String getTipo()
    {
        return tipo;
    }

    public void setTipo( String tipo )
    {
        this.tipo = tipo;
    }

    public BigDecimal getValor()
    {
        return valor;
    }

    public void setValor( BigDecimal valor )
    {
        this.valor = valor;
    }

    public Integer getUserId()
    {
        return userId;
    }

    public void setUserId( Integer userId )
    {
        this.userId = userId;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName( String userName )
    {
        this.userName = userName;
    }

    public String getBeneficiario()
    {
        return beneficiario;
    }

    public void setBeneficiario( String beneficiario )
    {
        this.beneficiario = beneficiario;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += ( pkContaOperacao != null ? pkContaOperacao.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if ( !( object instanceof ContaOperacoes ) )
        {
            return false;
        }
        ContaOperacoes other = ( ContaOperacoes ) object;
        if ( ( this.pkContaOperacao == null && other.pkContaOperacao != null ) || ( this.pkContaOperacao != null && !this.pkContaOperacao.equals( other.pkContaOperacao ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.ContaOperacoes[ pkContaOperacao=" + pkContaOperacao + " ]";
    }
    
}
