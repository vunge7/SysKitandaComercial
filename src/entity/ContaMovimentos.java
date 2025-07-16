/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
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
@Table(name = "conta_movimentos")
@XmlRootElement
@NamedQueries(
        {
            @NamedQuery(name = "ContaMovimentos.findAll", query = "SELECT c FROM ContaMovimentos c"),
            @NamedQuery(name = "ContaMovimentos.findByPkContaMovimento", query = "SELECT c FROM ContaMovimentos c WHERE c.pkContaMovimento = :pkContaMovimento"),
            @NamedQuery(name = "ContaMovimentos.findByDataHora", query = "SELECT c FROM ContaMovimentos c WHERE c.dataHora = :dataHora"),
            @NamedQuery(name = "ContaMovimentos.findByContaId", query = "SELECT c FROM ContaMovimentos c WHERE c.contaId = :contaId"),
            @NamedQuery(name = "ContaMovimentos.findByContaDesignacao", query = "SELECT c FROM ContaMovimentos c WHERE c.contaDesignacao = :contaDesignacao"),
            @NamedQuery(name = "ContaMovimentos.findBySaldoAnterior", query = "SELECT c FROM ContaMovimentos c WHERE c.saldoAnterior = :saldoAnterior"),
            @NamedQuery(name = "ContaMovimentos.findByValorEntrada", query = "SELECT c FROM ContaMovimentos c WHERE c.valorEntrada = :valorEntrada"),
            @NamedQuery(name = "ContaMovimentos.findByValorSaida", query = "SELECT c FROM ContaMovimentos c WHERE c.valorSaida = :valorSaida"),
            @NamedQuery(name = "ContaMovimentos.findBySaldoFinal", query = "SELECT c FROM ContaMovimentos c WHERE c.saldoFinal = :saldoFinal"),
            @NamedQuery(name = "ContaMovimentos.findByTipo", query = "SELECT c FROM ContaMovimentos c WHERE c.tipo = :tipo"),
            @NamedQuery(name = "ContaMovimentos.findByDescricao", query = "SELECT c FROM ContaMovimentos c WHERE c.descricao = :descricao"),
            @NamedQuery(name = "ContaMovimentos.findByDocumento", query = "SELECT c FROM ContaMovimentos c WHERE c.documento = :documento"),
            @NamedQuery(name = "ContaMovimentos.findByCodOperacao", query = "SELECT c FROM ContaMovimentos c WHERE c.codOperacao = :codOperacao")
        })
public class ContaMovimentos implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pk_conta_movimento")
    private Long pkContaMovimento;
    @Column(name = "data_hora")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataHora;
    @Column(name = "conta_id")
    private Integer contaId;
    @Column(name = "conta_designacao")
    private String contaDesignacao;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "saldo_anterior")
    private BigDecimal saldoAnterior;
    @Column(name = "valor_entrada")
    private BigDecimal valorEntrada;
    @Column(name = "valor_saida")
    private BigDecimal valorSaida;
    @Column(name = "saldo_final")
    private BigDecimal saldoFinal;
    @Column(name = "tipo")
    private String tipo;
    @Column(name = "descricao")
    private String descricao;
    @Column(name = "documento")
    private String documento;
    @Column(name = "cod_operacao")
    private long codOperacao;

    public ContaMovimentos()
    {
    }

    public ContaMovimentos( Long pkContaMovimento )
    {
        this.pkContaMovimento = pkContaMovimento;
    }

    public Long getPkContaMovimento()
    {
        return pkContaMovimento;
    }

    public void setPkContaMovimento( Long pkContaMovimento )
    {
        this.pkContaMovimento = pkContaMovimento;
    }

    public Date getDataHora()
    {
        return dataHora;
    }

    public void setDataHora( Date dataHora )
    {
        this.dataHora = dataHora;
    }

    public Integer getContaId()
    {
        return contaId;
    }

    public void setContaId( Integer contaId )
    {
        this.contaId = contaId;
    }

    public String getContaDesignacao()
    {
        return contaDesignacao;
    }

    public void setContaDesignacao( String contaDesignacao )
    {
        this.contaDesignacao = contaDesignacao;
    }

    public BigDecimal getSaldoAnterior()
    {
        return saldoAnterior;
    }

    public void setSaldoAnterior( BigDecimal saldoAnterior )
    {
        this.saldoAnterior = saldoAnterior;
    }

    public BigDecimal getValorEntrada()
    {
        return valorEntrada;
    }

    public void setValorEntrada( BigDecimal valorEntrada )
    {
        this.valorEntrada = valorEntrada;
    }

    public BigDecimal getValorSaida()
    {
        return valorSaida;
    }

    public void setValorSaida( BigDecimal valorSaida )
    {
        this.valorSaida = valorSaida;
    }

    public BigDecimal getSaldoFinal()
    {
        return saldoFinal;
    }

    public void setSaldoFinal( BigDecimal saldoFinal )
    {
        this.saldoFinal = saldoFinal;
    }

    public String getTipo()
    {
        return tipo;
    }

    public void setTipo( String tipo )
    {
        this.tipo = tipo;
    }

    public String getDescricao()
    {
        return descricao;
    }

    public void setDescricao( String descricao )
    {
        this.descricao = descricao;
    }

    public String getDocumento()
    {
        return documento;
    }

    public void setDocumento( String documento )
    {
        this.documento = documento;
    }

    public long getCodOperacao()
    {
        return codOperacao;
    }

    public void setCodOperacao( long codOperacao )
    {
        this.codOperacao = codOperacao;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += ( pkContaMovimento != null ? pkContaMovimento.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if ( !( object instanceof ContaMovimentos ) )
        {
            return false;
        }
        ContaMovimentos other = ( ContaMovimentos ) object;
        if ( ( this.pkContaMovimento == null && other.pkContaMovimento != null ) || ( this.pkContaMovimento != null && !this.pkContaMovimento.equals( other.pkContaMovimento ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.ContaMovimentos[ pkContaMovimento=" + pkContaMovimento + " ]";
    }

}
