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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "movimento_conta")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "MovimentoConta.findAll", query = "SELECT m FROM MovimentoConta m"),
    @NamedQuery(name = "MovimentoConta.findByPkMovimentoConta", query = "SELECT m FROM MovimentoConta m WHERE m.pkMovimentoConta = :pkMovimentoConta"),
    @NamedQuery(name = "MovimentoConta.findByDataHora", query = "SELECT m FROM MovimentoConta m WHERE m.dataHora = :dataHora"),
    @NamedQuery(name = "MovimentoConta.findByValorOperacao", query = "SELECT m FROM MovimentoConta m WHERE m.valorOperacao = :valorOperacao"),
    @NamedQuery(name = "MovimentoConta.findByTipoOperacao", query = "SELECT m FROM MovimentoConta m WHERE m.tipoOperacao = :tipoOperacao"),
    @NamedQuery(name = "MovimentoConta.findByNomeUsuario", query = "SELECT m FROM MovimentoConta m WHERE m.nomeUsuario = :nomeUsuario"),
    @NamedQuery(name = "MovimentoConta.findByValorAntesOperacao", query = "SELECT m FROM MovimentoConta m WHERE m.valorAntesOperacao = :valorAntesOperacao"),
    @NamedQuery(name = "MovimentoConta.findByValorDepoisOperacao", query = "SELECT m FROM MovimentoConta m WHERE m.valorDepoisOperacao = :valorDepoisOperacao")
})
public class MovimentoConta implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pk_movimento_conta")
    private Integer pkMovimentoConta;
    @Column(name = "data_hora")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataHora;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor_operacao")
    private BigDecimal valorOperacao;
    @Column(name = "tipo_operacao")
    private String tipoOperacao;
    @Column(name = "nome_usuario")
    private String nomeUsuario;
    @Column(name = "valor_antes_operacao")
    private BigDecimal valorAntesOperacao;
    @Column(name = "valor_depois_operacao")
    private BigDecimal valorDepoisOperacao;
    @JoinColumn(name = "fk_conta", referencedColumnName = "idConta")
    @ManyToOne(optional = false)
    private Conta fkConta;

    public MovimentoConta()
    {
    }

    public MovimentoConta( Integer pkMovimentoConta )
    {
        this.pkMovimentoConta = pkMovimentoConta;
    }

    public Integer getPkMovimentoConta()
    {
        return pkMovimentoConta;
    }

    public void setPkMovimentoConta( Integer pkMovimentoConta )
    {
        this.pkMovimentoConta = pkMovimentoConta;
    }

    public Date getDataHora()
    {
        return dataHora;
    }

    public void setDataHora( Date dataHora )
    {
        this.dataHora = dataHora;
    }

    public BigDecimal getValorOperacao()
    {
        return valorOperacao;
    }

    public void setValorOperacao( BigDecimal valorOperacao )
    {
        this.valorOperacao = valorOperacao;
    }

    public String getTipoOperacao()
    {
        return tipoOperacao;
    }

    public void setTipoOperacao( String tipoOperacao )
    {
        this.tipoOperacao = tipoOperacao;
    }

    public String getNomeUsuario()
    {
        return nomeUsuario;
    }

    public void setNomeUsuario( String nomeUsuario )
    {
        this.nomeUsuario = nomeUsuario;
    }

    public BigDecimal getValorAntesOperacao()
    {
        return valorAntesOperacao;
    }

    public void setValorAntesOperacao( BigDecimal valorAntesOperacao )
    {
        this.valorAntesOperacao = valorAntesOperacao;
    }

    public BigDecimal getValorDepoisOperacao()
    {
        return valorDepoisOperacao;
    }

    public void setValorDepoisOperacao( BigDecimal valorDepoisOperacao )
    {
        this.valorDepoisOperacao = valorDepoisOperacao;
    }

    public Conta getFkConta()
    {
        return fkConta;
    }

    public void setFkConta( Conta fkConta )
    {
        this.fkConta = fkConta;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += ( pkMovimentoConta != null ? pkMovimentoConta.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if ( !( object instanceof MovimentoConta ) )
        {
            return false;
        }
        MovimentoConta other = ( MovimentoConta ) object;
        if ( ( this.pkMovimentoConta == null && other.pkMovimentoConta != null ) || ( this.pkMovimentoConta != null && !this.pkMovimentoConta.equals( other.pkMovimentoConta ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.MovimentoConta[ pkMovimentoConta=" + pkMovimentoConta + " ]";
    }
    
}
