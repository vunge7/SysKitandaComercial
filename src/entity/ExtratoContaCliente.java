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
 * @author lenovo
 */
@Entity
@Table(name = "extrato_conta_cliente")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "ExtratoContaCliente.findAll", query = "SELECT e FROM ExtratoContaCliente e"),
    @NamedQuery(name = "ExtratoContaCliente.findByPkExtratoContaCliente", query = "SELECT e FROM ExtratoContaCliente e WHERE e.pkExtratoContaCliente = :pkExtratoContaCliente"),
    @NamedQuery(name = "ExtratoContaCliente.findByDocumento", query = "SELECT e FROM ExtratoContaCliente e WHERE e.documento = :documento"),
    @NamedQuery(name = "ExtratoContaCliente.findByReferencia", query = "SELECT e FROM ExtratoContaCliente e WHERE e.referencia = :referencia"),
    @NamedQuery(name = "ExtratoContaCliente.findByDescricao", query = "SELECT e FROM ExtratoContaCliente e WHERE e.descricao = :descricao"),
    @NamedQuery(name = "ExtratoContaCliente.findByDataHora", query = "SELECT e FROM ExtratoContaCliente e WHERE e.dataHora = :dataHora"),
    @NamedQuery(name = "ExtratoContaCliente.findBySaldoAnterior", query = "SELECT e FROM ExtratoContaCliente e WHERE e.saldoAnterior = :saldoAnterior"),
    @NamedQuery(name = "ExtratoContaCliente.findByDebito", query = "SELECT e FROM ExtratoContaCliente e WHERE e.debito = :debito"),
    @NamedQuery(name = "ExtratoContaCliente.findByCredito", query = "SELECT e FROM ExtratoContaCliente e WHERE e.credito = :credito"),
    @NamedQuery(name = "ExtratoContaCliente.findBySaldoActual", query = "SELECT e FROM ExtratoContaCliente e WHERE e.saldoActual = :saldoActual"),
    @NamedQuery(name = "ExtratoContaCliente.findByTipoExtrato", query = "SELECT e FROM ExtratoContaCliente e WHERE e.tipoExtrato = :tipoExtrato"),
    @NamedQuery(name = "ExtratoContaCliente.findByClienteNome", query = "SELECT e FROM ExtratoContaCliente e WHERE e.clienteNome = :clienteNome"),
    @NamedQuery(name = "ExtratoContaCliente.findByClienteId", query = "SELECT e FROM ExtratoContaCliente e WHERE e.clienteId = :clienteId"),
    @NamedQuery(name = "ExtratoContaCliente.findByUserName", query = "SELECT e FROM ExtratoContaCliente e WHERE e.userName = :userName"),
    @NamedQuery(name = "ExtratoContaCliente.findByUsuarioId", query = "SELECT e FROM ExtratoContaCliente e WHERE e.usuarioId = :usuarioId")
})
public class ExtratoContaCliente implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pk_extrato_conta_cliente")
    private Long pkExtratoContaCliente;
    @Column(name = "documento")
    private String documento;
    @Column(name = "referencia")
    private String referencia;
    @Column(name = "descricao")
    private String descricao;
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
    @Column(name = "cliente_nome")
    private String clienteNome;
    @Column(name = "cliente_id")
    private Integer clienteId;
    @Column(name = "user_name")
    private String userName;
    @Column(name = "usuario_id")
    private Integer usuarioId;

    public ExtratoContaCliente()
    {
    }

    public ExtratoContaCliente( Long pkExtratoContaCliente )
    {
        this.pkExtratoContaCliente = pkExtratoContaCliente;
    }

    public Long getPkExtratoContaCliente()
    {
        return pkExtratoContaCliente;
    }

    public void setPkExtratoContaCliente( Long pkExtratoContaCliente )
    {
        this.pkExtratoContaCliente = pkExtratoContaCliente;
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

    public String getDescricao()
    {
        return descricao;
    }

    public void setDescricao( String descricao )
    {
        this.descricao = descricao;
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

    public String getClienteNome()
    {
        return clienteNome;
    }

    public void setClienteNome( String clienteNome )
    {
        this.clienteNome = clienteNome;
    }

    public Integer getClienteId()
    {
        return clienteId;
    }

    public void setClienteId( Integer clienteId )
    {
        this.clienteId = clienteId;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName( String userName )
    {
        this.userName = userName;
    }

    public Integer getUsuarioId()
    {
        return usuarioId;
    }

    public void setUsuarioId( Integer usuarioId )
    {
        this.usuarioId = usuarioId;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += ( pkExtratoContaCliente != null ? pkExtratoContaCliente.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if ( !( object instanceof ExtratoContaCliente ) )
        {
            return false;
        }
        ExtratoContaCliente other = ( ExtratoContaCliente ) object;
        if ( ( this.pkExtratoContaCliente == null && other.pkExtratoContaCliente != null ) || ( this.pkExtratoContaCliente != null && !this.pkExtratoContaCliente.equals( other.pkExtratoContaCliente ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.ExtratoContaCliente[ pkExtratoContaCliente=" + pkExtratoContaCliente + " ]";
    }
    
}
