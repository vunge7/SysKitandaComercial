/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
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
@Table(name = "tb_mesas")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "TbMesas.findAll", query = "SELECT t FROM TbMesas t"),
    @NamedQuery(name = "TbMesas.findByPkMesas", query = "SELECT t FROM TbMesas t WHERE t.pkMesas = :pkMesas"),
    @NamedQuery(name = "TbMesas.findByDesignacao", query = "SELECT t FROM TbMesas t WHERE t.designacao = :designacao")
})
public class TbMesas implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pk_mesas")
    private Integer pkMesas;
    @Basic(optional = false)
    @Column(name = "designacao")
    private String designacao;
    @OneToMany(mappedBy = "fkMesas")
    private List<TbItemVenda> tbItemVendaList;
    @OneToMany(mappedBy = "fkMesas")
    private List<TbPedido> tbPedidoList;

    public TbMesas()
    {
    }

    public TbMesas( Integer pkMesas )
    {
        this.pkMesas = pkMesas;
    }

    public TbMesas( Integer pkMesas, String designacao )
    {
        this.pkMesas = pkMesas;
        this.designacao = designacao;
    }

    public Integer getPkMesas()
    {
        return pkMesas;
    }

    public void setPkMesas( Integer pkMesas )
    {
        this.pkMesas = pkMesas;
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
    public List<TbItemVenda> getTbItemVendaList()
    {
        return tbItemVendaList;
    }

    public void setTbItemVendaList( List<TbItemVenda> tbItemVendaList )
    {
        this.tbItemVendaList = tbItemVendaList;
    }

    @XmlTransient
    public List<TbPedido> getTbPedidoList()
    {
        return tbPedidoList;
    }

    public void setTbPedidoList( List<TbPedido> tbPedidoList )
    {
        this.tbPedidoList = tbPedidoList;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += ( pkMesas != null ? pkMesas.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if ( !( object instanceof TbMesas ) )
        {
            return false;
        }
        TbMesas other = ( TbMesas ) object;
        if ( ( this.pkMesas == null && other.pkMesas != null ) || ( this.pkMesas != null && !this.pkMesas.equals( other.pkMesas ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.TbMesas[ pkMesas=" + pkMesas + " ]";
    }
    
}
