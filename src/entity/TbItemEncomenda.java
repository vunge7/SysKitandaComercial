/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author lenovo
 */
@Entity
@Table(name = "tb_item_encomenda")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "TbItemEncomenda.findAll", query = "SELECT t FROM TbItemEncomenda t"),
    @NamedQuery(name = "TbItemEncomenda.findByTotal", query = "SELECT t FROM TbItemEncomenda t WHERE t.total = :total"),
    @NamedQuery(name = "TbItemEncomenda.findByQuantidade", query = "SELECT t FROM TbItemEncomenda t WHERE t.quantidade = :quantidade"),
    @NamedQuery(name = "TbItemEncomenda.findByCodigo", query = "SELECT t FROM TbItemEncomenda t WHERE t.codigo = :codigo")
})
public class TbItemEncomenda implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @Column(name = "total")
    private double total;
    @Basic(optional = false)
    @Column(name = "quantidade")
    private int quantidade;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "codigo")
    private Integer codigo;
    @JoinColumn(name = "idEncomenda", referencedColumnName = "idEncomenda")
    @ManyToOne(optional = false)
    private TbEncomenda idEncomenda;
    @JoinColumn(name = "idProduto", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private TbProduto idProduto;

    public TbItemEncomenda()
    {
    }

    public TbItemEncomenda( Integer codigo )
    {
        this.codigo = codigo;
    }

    public TbItemEncomenda( Integer codigo, double total, int quantidade )
    {
        this.codigo = codigo;
        this.total = total;
        this.quantidade = quantidade;
    }

    public double getTotal()
    {
        return total;
    }

    public void setTotal( double total )
    {
        this.total = total;
    }

    public int getQuantidade()
    {
        return quantidade;
    }

    public void setQuantidade( int quantidade )
    {
        this.quantidade = quantidade;
    }

    public Integer getCodigo()
    {
        return codigo;
    }

    public void setCodigo( Integer codigo )
    {
        this.codigo = codigo;
    }

    public TbEncomenda getIdEncomenda()
    {
        return idEncomenda;
    }

    public void setIdEncomenda( TbEncomenda idEncomenda )
    {
        this.idEncomenda = idEncomenda;
    }

    public TbProduto getIdProduto()
    {
        return idProduto;
    }

    public void setIdProduto( TbProduto idProduto )
    {
        this.idProduto = idProduto;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += ( codigo != null ? codigo.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if ( !( object instanceof TbItemEncomenda ) )
        {
            return false;
        }
        TbItemEncomenda other = ( TbItemEncomenda ) object;
        if ( ( this.codigo == null && other.codigo != null ) || ( this.codigo != null && !this.codigo.equals( other.codigo ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.TbItemEncomenda[ codigo=" + codigo + " ]";
    }
    
}
