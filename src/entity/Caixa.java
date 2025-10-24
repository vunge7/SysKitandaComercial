/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;


import java.sql.Connection;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author lenovo
 */
@Entity
@Table(name = "caixa")
@XmlRootElement
@NamedQueries(
        {
            @NamedQuery(name = "Caixa.findAll", query = "SELECT c FROM Caixa c"),
            @NamedQuery(name = "Caixa.findByPkCaixa", query = "SELECT c FROM Caixa c WHERE c.pkCaixa = :pkCaixa"),
            @NamedQuery(name = "Caixa.findByDataAbertura", query = "SELECT c FROM Caixa c WHERE c.dataAbertura = :dataAbertura"),
            @NamedQuery(name = "Caixa.findByDataFecho", query = "SELECT c FROM Caixa c WHERE c.dataFecho = :dataFecho"),
            @NamedQuery(name = "Caixa.findByTotalVendas", query = "SELECT c FROM Caixa c WHERE c.totalVendas = :totalVendas"),
            @NamedQuery(name = "Caixa.findByNumeroVendas", query = "SELECT c FROM Caixa c WHERE c.numeroVendas = :numeroVendas"),
            @NamedQuery(name = "Caixa.findByValorInicial", query = "SELECT c FROM Caixa c WHERE c.valorInicial = :valorInicial"),
            @NamedQuery(name = "Caixa.findByUsuarioFecho", query = "SELECT c FROM Caixa c WHERE c.usuarioFecho = :usuarioFecho"),
            @NamedQuery(name = "Caixa.findByUsuarioAbertura", query = "SELECT c FROM Caixa c WHERE c.usuarioAbertura = :usuarioAbertura")
        })
public class Caixa implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pk_caixa")
    private Integer pkCaixa;
    @Column(name = "data_abertura")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataAbertura;
    @Column(name = "data_fecho")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataFecho;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "total_vendas")
    private Double totalVendas;
    @Column(name = "numero_vendas")
    private Integer numeroVendas;
    @Column(name = "valor_inicial")
    private Double valorInicial;
    @Column(name = "usuario_fecho")
    private String usuarioFecho;
    @Column(name = "usuario_abertura")
    private String usuarioAbertura;
    @Column(name = "cod_usuario_abertura")
    private int codUsuarioAbertura;
    @Column(name = "cod_usuario_fecho")
    private int codUsuarioFecho;
    @Column(name = "total_desconto")
    private BigDecimal totalDesconto;
    @Column(name = "total_iva")
    private BigDecimal totalIva;
    @Column(name = "total_iliquido")
    private BigDecimal totalIliquido;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkCaixa")
    private List<ItemCaixa> itemCaixaList;

    public Caixa()
    {
    }

    public Caixa( Integer pkCaixa )
    {
        this.pkCaixa = pkCaixa;
    }

    public Integer getPkCaixa()
    {
        return pkCaixa;
    }

    public void setPkCaixa( Integer pkCaixa )
    {
        this.pkCaixa = pkCaixa;
    }

    public Date getDataAbertura()
    {
        return dataAbertura;
    }

    public void setDataAbertura( Date dataAbertura )
    {
        this.dataAbertura = dataAbertura;
    }

    public Date getDataFecho()
    {
        return dataFecho;
    }

    public void setDataFecho( Date dataFecho )
    {
        this.dataFecho = dataFecho;
    }

    public Double getTotalVendas()
    {
        return totalVendas;
    }

    public void setTotalVendas( Double totalVendas )
    {
        this.totalVendas = totalVendas;
    }

    public Integer getNumeroVendas()
    {
        return numeroVendas;
    }

    public void setNumeroVendas( Integer numeroVendas )
    {
        this.numeroVendas = numeroVendas;
    }

    public Double getValorInicial()
    {
        return valorInicial;
    }

    public void setValorInicial( Double valorInicial )
    {
        this.valorInicial = valorInicial;
    }

    public String getUsuarioFecho()
    {
        return usuarioFecho;
    }

    public void setUsuarioFecho( String usuarioFecho )
    {
        this.usuarioFecho = usuarioFecho;
    }

    public String getUsuarioAbertura()
    {
        return usuarioAbertura;
    }

    public void setUsuarioAbertura( String usuarioAbertura )
    {
        this.usuarioAbertura = usuarioAbertura;
    }

    public int getCodUsuarioAbertura()
    {
        return codUsuarioAbertura;
    }

    public void setCodUsuarioAbertura( int codUsuarioAbertura )
    {
        this.codUsuarioAbertura = codUsuarioAbertura;
    }

    public int getCodUsuarioFecho()
    {
        return codUsuarioFecho;
    }

    public void setCodUsuarioFecho( int codUsuarioFecho )
    {
        this.codUsuarioFecho = codUsuarioFecho;
    }

    public BigDecimal getTotalDesconto()
    {
        return totalDesconto;
    }

    public void setTotalDesconto( BigDecimal totalDesconto )
    {
        this.totalDesconto = totalDesconto;
    }

    public BigDecimal getTotalIva()
    {
        return totalIva;
    }

    public void setTotalIva( BigDecimal totalIva )
    {
        this.totalIva = totalIva;
    }

    public BigDecimal getTotaIIliquido()
    {
        return totalIliquido;
    }

    public void setTotaIIliquido( BigDecimal totalIliquido )
    {
        this.totalIliquido = totalIliquido;
    }
    
    

    @XmlTransient
    public List<ItemCaixa> getItemCaixaList()
    {
        return itemCaixaList;
    }

    public void setItemCaixaList( List<ItemCaixa> itemCaixaList )
    {
        this.itemCaixaList = itemCaixaList;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += ( pkCaixa != null ? pkCaixa.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if ( !( object instanceof Caixa ) )
        {
            return false;
        }
        Caixa other = ( Caixa ) object;
        if ( ( this.pkCaixa == null && other.pkCaixa != null ) || ( this.pkCaixa != null && !this.pkCaixa.equals( other.pkCaixa ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.Caixa[ pkCaixa=" + pkCaixa + " ]";
    }

}
