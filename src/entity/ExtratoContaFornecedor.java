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
@Table(name = "extrato_conta_fornecedor")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "ExtratoContaFornecedor.findAll", query = "SELECT e FROM ExtratoContaFornecedor e"),
    @NamedQuery(name = "ExtratoContaFornecedor.findByPkExtratoContaFornecedor", query = "SELECT e FROM ExtratoContaFornecedor e WHERE e.pkExtratoContaFornecedor = :pkExtratoContaFornecedor"),
    @NamedQuery(name = "ExtratoContaFornecedor.findByDocumento", query = "SELECT e FROM ExtratoContaFornecedor e WHERE e.documento = :documento"),
    @NamedQuery(name = "ExtratoContaFornecedor.findByReferencia", query = "SELECT e FROM ExtratoContaFornecedor e WHERE e.referencia = :referencia"),
    @NamedQuery(name = "ExtratoContaFornecedor.findByDataHora", query = "SELECT e FROM ExtratoContaFornecedor e WHERE e.dataHora = :dataHora"),
    @NamedQuery(name = "ExtratoContaFornecedor.findBySaldoAnterior", query = "SELECT e FROM ExtratoContaFornecedor e WHERE e.saldoAnterior = :saldoAnterior"),
    @NamedQuery(name = "ExtratoContaFornecedor.findByDebito", query = "SELECT e FROM ExtratoContaFornecedor e WHERE e.debito = :debito"),
    @NamedQuery(name = "ExtratoContaFornecedor.findByCredito", query = "SELECT e FROM ExtratoContaFornecedor e WHERE e.credito = :credito"),
    @NamedQuery(name = "ExtratoContaFornecedor.findBySaldoActual", query = "SELECT e FROM ExtratoContaFornecedor e WHERE e.saldoActual = :saldoActual"),
    @NamedQuery(name = "ExtratoContaFornecedor.findByTipoExtrato", query = "SELECT e FROM ExtratoContaFornecedor e WHERE e.tipoExtrato = :tipoExtrato"),
    @NamedQuery(name = "ExtratoContaFornecedor.findByFornecedorId", query = "SELECT e FROM ExtratoContaFornecedor e WHERE e.fornecedorId = :fornecedorId"),
    @NamedQuery(name = "ExtratoContaFornecedor.findByFonecedorNome", query = "SELECT e FROM ExtratoContaFornecedor e WHERE e.fonecedorNome = :fonecedorNome"),
    @NamedQuery(name = "ExtratoContaFornecedor.findByUserId", query = "SELECT e FROM ExtratoContaFornecedor e WHERE e.userId = :userId"),
    @NamedQuery(name = "ExtratoContaFornecedor.findByUserName", query = "SELECT e FROM ExtratoContaFornecedor e WHERE e.userName = :userName"),
    @NamedQuery(name = "ExtratoContaFornecedor.findByDescricao", query = "SELECT e FROM ExtratoContaFornecedor e WHERE e.descricao = :descricao")
})
public class ExtratoContaFornecedor implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pk_extrato_conta_fornecedor")
    private Long pkExtratoContaFornecedor;
    @Column(name = "documento")
    private String documento;
    @Column(name = "referencia")
    private String referencia;
    @Column(name = "data_hora")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataHora;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "saldo_anterior")
    private BigDecimal saldoAnterior;
    @Column(name = "debito")
    private BigDecimal debito;
    @Column(name = "credito")
    private BigDecimal credito;
    @Column(name = "saldo_actual")
    private BigDecimal saldoActual;
    @Column(name = "tipo_extrato")
    private String tipoExtrato;
    @Column(name = "fornecedor_id")
    private Integer fornecedorId;
    @Column(name = "fonecedor_nome")
    private String fonecedorNome;
    @Column(name = "user_id")
    private Integer userId;
    @Column(name = "user_name")
    private String userName;
    @Column(name = "descricao")
    private String descricao;

    public ExtratoContaFornecedor()
    {
    }

    public ExtratoContaFornecedor( Long pkExtratoContaFornecedor )
    {
        this.pkExtratoContaFornecedor = pkExtratoContaFornecedor;
    }

    public Long getPkExtratoContaFornecedor()
    {
        return pkExtratoContaFornecedor;
    }

    public void setPkExtratoContaFornecedor( Long pkExtratoContaFornecedor )
    {
        this.pkExtratoContaFornecedor = pkExtratoContaFornecedor;
    }

    public String getDocumento()
    {
        return documento;
    }

    public void setDocumento( String documento )
    {
        this.documento = documento;
    }

    public String getReferencia()
    {
        return referencia;
    }

    public void setReferencia( String referencia )
    {
        this.referencia = referencia;
    }

    public Date getDataHora()
    {
        return dataHora;
    }

    public void setDataHora( Date dataHora )
    {
        this.dataHora = dataHora;
    }

    public BigDecimal getSaldoAnterior()
    {
        return saldoAnterior;
    }

    public void setSaldoAnterior( BigDecimal saldoAnterior )
    {
        this.saldoAnterior = saldoAnterior;
    }

    public BigDecimal getDebito()
    {
        return debito;
    }

    public void setDebito( BigDecimal debito )
    {
        this.debito = debito;
    }

    public BigDecimal getCredito()
    {
        return credito;
    }

    public void setCredito( BigDecimal credito )
    {
        this.credito = credito;
    }

    public BigDecimal getSaldoActual()
    {
        return saldoActual;
    }

    public void setSaldoActual( BigDecimal saldoActual )
    {
        this.saldoActual = saldoActual;
    }

    public String getTipoExtrato()
    {
        return tipoExtrato;
    }

    public void setTipoExtrato( String tipoExtrato )
    {
        this.tipoExtrato = tipoExtrato;
    }

    public Integer getFornecedorId()
    {
        return fornecedorId;
    }

    public void setFornecedorId( Integer fornecedorId )
    {
        this.fornecedorId = fornecedorId;
    }

    public String getFonecedorNome()
    {
        return fonecedorNome;
    }

    public void setFonecedorNome( String fonecedorNome )
    {
        this.fonecedorNome = fonecedorNome;
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

    public String getDescricao()
    {
        return descricao;
    }

    public void setDescricao( String descricao )
    {
        this.descricao = descricao;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += ( pkExtratoContaFornecedor != null ? pkExtratoContaFornecedor.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if ( !( object instanceof ExtratoContaFornecedor ) )
        {
            return false;
        }
        ExtratoContaFornecedor other = ( ExtratoContaFornecedor ) object;
        if ( ( this.pkExtratoContaFornecedor == null && other.pkExtratoContaFornecedor != null ) || ( this.pkExtratoContaFornecedor != null && !this.pkExtratoContaFornecedor.equals( other.pkExtratoContaFornecedor ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.ExtratoContaFornecedor[ pkExtratoContaFornecedor=" + pkExtratoContaFornecedor + " ]";
    }
    
}
