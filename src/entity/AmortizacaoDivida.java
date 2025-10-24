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
 * @author lenovo
 */
@Entity
@Table(name = "amortizacao_divida")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "AmortizacaoDivida.findAll", query = "SELECT a FROM AmortizacaoDivida a"),
    @NamedQuery(name = "AmortizacaoDivida.findByPkAmortizacaoDivida", query = "SELECT a FROM AmortizacaoDivida a WHERE a.pkAmortizacaoDivida = :pkAmortizacaoDivida"),
    @NamedQuery(name = "AmortizacaoDivida.findByData", query = "SELECT a FROM AmortizacaoDivida a WHERE a.data = :data"),
    @NamedQuery(name = "AmortizacaoDivida.findByValorPendente", query = "SELECT a FROM AmortizacaoDivida a WHERE a.valorPendente = :valorPendente"),
    @NamedQuery(name = "AmortizacaoDivida.findByValorEntregue", query = "SELECT a FROM AmortizacaoDivida a WHERE a.valorEntregue = :valorEntregue"),
    @NamedQuery(name = "AmortizacaoDivida.findByTroco", query = "SELECT a FROM AmortizacaoDivida a WHERE a.troco = :troco"),
    @NamedQuery(name = "AmortizacaoDivida.findByObs", query = "SELECT a FROM AmortizacaoDivida a WHERE a.obs = :obs"),
    @NamedQuery(name = "AmortizacaoDivida.findByDesconto", query = "SELECT a FROM AmortizacaoDivida a WHERE a.desconto = :desconto")
})
public class AmortizacaoDivida implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pk_amortizacao_divida")
    private Integer pkAmortizacaoDivida;
    @Column(name = "data")
    @Temporal(TemporalType.TIMESTAMP)
    private Date data;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor_pendente")
    private Double valorPendente;
    @Column(name = "valor_entregue")
    private Double valorEntregue;
    @Column(name = "troco")
    private Double troco;
    @Column(name = "obs")
    private String obs;
    @Column(name = "desconto")
    private Double desconto;
        @Column(name = "ref_cod_fact")
    private String refCodFact;
    @Column(name = "total_venda_fact")
    private Double totalVendaFact;
    @Basic(optional = false)
    @Column(name = "valor_pago")
    private BigDecimal valorPago;
    @Basic(optional = false)
    @Column(name = "net_total")
    private BigDecimal netTotal;
    @Basic(optional = false)
    @Column(name = "tax")
    private BigDecimal tax;
    @JoinColumn(name = "fk_usuario", referencedColumnName = "codigo")
    @ManyToOne
    private TbUsuario fkUsuario;
    @JoinColumn(name = "fk_venda", referencedColumnName = "codigo")
    @ManyToOne
    private TbVenda fkVenda;

    public AmortizacaoDivida()
    {
    }

    public String getRefCodFact()
    {
        return refCodFact;
    }

    public void setRefCodFact( String refCodFact )
    {
        this.refCodFact = refCodFact;
    }

    public Double getTotalVendaFact()
    {
        return totalVendaFact;
    }

    public void setTotalVendaFact( Double totalVendaFact )
    {
        this.totalVendaFact = totalVendaFact;
    }

    public BigDecimal getValorPago()
    {
        return valorPago;
    }

    public void setValorPago( BigDecimal valorPago )
    {
        this.valorPago = valorPago;
    }

    public BigDecimal getNetTotal()
    {
        return netTotal;
    }

    public void setNetTotal( BigDecimal netTotal )
    {
        this.netTotal = netTotal;
    }

    public BigDecimal getTax()
    {
        return tax;
    }

    public void setTax( BigDecimal tax )
    {
        this.tax = tax;
    }

    public AmortizacaoDivida( Integer pkAmortizacaoDivida )
    {
        this.pkAmortizacaoDivida = pkAmortizacaoDivida;
    }

    public Integer getPkAmortizacaoDivida()
    {
        return pkAmortizacaoDivida;
    }

    public void setPkAmortizacaoDivida( Integer pkAmortizacaoDivida )
    {
        this.pkAmortizacaoDivida = pkAmortizacaoDivida;
    }

    public Date getData()
    {
        return data;
    }

    public void setData( Date data )
    {
        this.data = data;
    }

    public Double getValorPendente()
    {
        return valorPendente;
    }

    public void setValorPendente( Double valorPendente )
    {
        this.valorPendente = valorPendente;
    }

    public Double getValorEntregue()
    {
        return valorEntregue;
    }

    public void setValorEntregue( Double valorEntregue )
    {
        this.valorEntregue = valorEntregue;
    }

    public Double getTroco()
    {
        return troco;
    }

    public void setTroco( Double troco )
    {
        this.troco = troco;
    }

    public String getObs()
    {
        return obs;
    }

    public void setObs( String obs )
    {
        this.obs = obs;
    }

    public Double getDesconto()
    {
        return desconto;
    }

    public void setDesconto( Double desconto )
    {
        this.desconto = desconto;
    }

    public TbUsuario getFkUsuario()
    {
        return fkUsuario;
    }

    public void setFkUsuario( TbUsuario fkUsuario )
    {
        this.fkUsuario = fkUsuario;
    }

    public TbVenda getFkVenda()
    {
        return fkVenda;
    }

    public void setFkVenda( TbVenda fkVenda )
    {
        this.fkVenda = fkVenda;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += ( pkAmortizacaoDivida != null ? pkAmortizacaoDivida.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if ( !( object instanceof AmortizacaoDivida ) )
        {
            return false;
        }
        AmortizacaoDivida other = ( AmortizacaoDivida ) object;
        if ( ( this.pkAmortizacaoDivida == null && other.pkAmortizacaoDivida != null ) || ( this.pkAmortizacaoDivida != null && !this.pkAmortizacaoDivida.equals( other.pkAmortizacaoDivida ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.AmortizacaoDivida[ pkAmortizacaoDivida=" + pkAmortizacaoDivida + " ]";
    }
    
}
