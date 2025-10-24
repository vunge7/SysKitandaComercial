/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;


import java.sql.Connection;
import java.io.Serializable;
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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author lenovo
 */
@Entity
@Table(name = "tb_lugares")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "TbLugares.findAll", query = "SELECT t FROM TbLugares t"),
    @NamedQuery(name = "TbLugares.findByPkLugares", query = "SELECT t FROM TbLugares t WHERE t.pkLugares = :pkLugares"),
    @NamedQuery(name = "TbLugares.findByDesignacao", query = "SELECT t FROM TbLugares t WHERE t.designacao = :designacao")
})
public class TbLugares implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pk_lugares")
    private Integer pkLugares;
    @Basic(optional = false)
    @Column(name = "designacao")
    private String designacao;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkLugares")
    private List<TbItemPedidos> tbItemPedidosList;
    @OneToMany(mappedBy = "fkLugares")
    private List<TbItemVenda> tbItemVendaList;

    public TbLugares()
    {
    }

    public TbLugares( Integer pkLugares )
    {
        this.pkLugares = pkLugares;
    }

    public TbLugares( Integer pkLugares, String designacao )
    {
        this.pkLugares = pkLugares;
        this.designacao = designacao;
    }

    public Integer getPkLugares()
    {
        return pkLugares;
    }

    public void setPkLugares( Integer pkLugares )
    {
        this.pkLugares = pkLugares;
    }

    public String getDesignacao()
    {
        return designacao;
    }

    public void setDesignacao( String designacao )
    {
        this.designacao = designacao;
    }

    @XmlTransient
    public List<TbItemPedidos> getTbItemPedidosList()
    {
        return tbItemPedidosList;
    }

    public void setTbItemPedidosList( List<TbItemPedidos> tbItemPedidosList )
    {
        this.tbItemPedidosList = tbItemPedidosList;
    }

    @XmlTransient
    public List<TbItemVenda> getTbItemVendaList()
    {
        return tbItemVendaList;
    }

    public void setTbItemVendaList( List<TbItemVenda> tbItemVendaList )
    {
        this.tbItemVendaList = tbItemVendaList;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += ( pkLugares != null ? pkLugares.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if ( !( object instanceof TbLugares ) )
        {
            return false;
        }
        TbLugares other = ( TbLugares ) object;
        if ( ( this.pkLugares == null && other.pkLugares != null ) || ( this.pkLugares != null && !this.pkLugares.equals( other.pkLugares ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.TbLugares[ pkLugares=" + pkLugares + " ]";
    }
    
}
