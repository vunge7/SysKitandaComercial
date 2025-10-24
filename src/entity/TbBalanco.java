/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;


import java.sql.Connection;
import java.io.Serializable;
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
@Table(name = "tb_balanco")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "TbBalanco.findAll", query = "SELECT t FROM TbBalanco t"),
    @NamedQuery(name = "TbBalanco.findByPkBalanco", query = "SELECT t FROM TbBalanco t WHERE t.pkBalanco = :pkBalanco"),
    @NamedQuery(name = "TbBalanco.findByDataBalanco", query = "SELECT t FROM TbBalanco t WHERE t.dataBalanco = :dataBalanco"),
    @NamedQuery(name = "TbBalanco.findByValorBancoOutros", query = "SELECT t FROM TbBalanco t WHERE t.valorBancoOutros = :valorBancoOutros"),
    @NamedQuery(name = "TbBalanco.findByValorBancoCaixa", query = "SELECT t FROM TbBalanco t WHERE t.valorBancoCaixa = :valorBancoCaixa"),
    @NamedQuery(name = "TbBalanco.findByValorBancoStock", query = "SELECT t FROM TbBalanco t WHERE t.valorBancoStock = :valorBancoStock")
})
public class TbBalanco implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pk_balanco")
    private Long pkBalanco;
    @Column(name = "data_balanco")
    @Temporal(TemporalType.DATE)
    private Date dataBalanco;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor_banco_outros")
    private Double valorBancoOutros;
    @Column(name = "valor_banco_caixa")
    private Double valorBancoCaixa;
    @Column(name = "valor_banco_stock")
    private Double valorBancoStock;

    public TbBalanco()
    {
    }

    public TbBalanco( Long pkBalanco )
    {
        this.pkBalanco = pkBalanco;
    }

    public Long getPkBalanco()
    {
        return pkBalanco;
    }

    public void setPkBalanco( Long pkBalanco )
    {
        this.pkBalanco = pkBalanco;
    }

    public Date getDataBalanco()
    {
        return dataBalanco;
    }

    public void setDataBalanco( Date dataBalanco )
    {
        this.dataBalanco = dataBalanco;
    }

    public Double getValorBancoOutros()
    {
        return valorBancoOutros;
    }

    public void setValorBancoOutros( Double valorBancoOutros )
    {
        this.valorBancoOutros = valorBancoOutros;
    }

    public Double getValorBancoCaixa()
    {
        return valorBancoCaixa;
    }

    public void setValorBancoCaixa( Double valorBancoCaixa )
    {
        this.valorBancoCaixa = valorBancoCaixa;
    }

    public Double getValorBancoStock()
    {
        return valorBancoStock;
    }

    public void setValorBancoStock( Double valorBancoStock )
    {
        this.valorBancoStock = valorBancoStock;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += ( pkBalanco != null ? pkBalanco.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if ( !( object instanceof TbBalanco ) )
        {
            return false;
        }
        TbBalanco other = ( TbBalanco ) object;
        if ( ( this.pkBalanco == null && other.pkBalanco != null ) || ( this.pkBalanco != null && !this.pkBalanco.equals( other.pkBalanco ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.TbBalanco[ pkBalanco=" + pkBalanco + " ]";
    }
    
}
