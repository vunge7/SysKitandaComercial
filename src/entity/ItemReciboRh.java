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
@Table(name = "item_recibo_rh")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "ItemReciboRh.findAll", query = "SELECT i FROM ItemReciboRh i"),
    @NamedQuery(name = "ItemReciboRh.findByPkItemReciboRh", query = "SELECT i FROM ItemReciboRh i WHERE i.pkItemReciboRh = :pkItemReciboRh"),
    @NamedQuery(name = "ItemReciboRh.findByDescricao", query = "SELECT i FROM ItemReciboRh i WHERE i.descricao = :descricao"),
    @NamedQuery(name = "ItemReciboRh.findByRemuneracao", query = "SELECT i FROM ItemReciboRh i WHERE i.remuneracao = :remuneracao"),
    @NamedQuery(name = "ItemReciboRh.findByDesconto", query = "SELECT i FROM ItemReciboRh i WHERE i.desconto = :desconto")
})
public class ItemReciboRh implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pk_item_recibo_rh")
    private Integer pkItemReciboRh;
    @Column(name = "descricao")
    private String descricao;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "remuneracao")
    private Double remuneracao;
    @Column(name = "desconto")
    private Double desconto;
    @JoinColumn(name = "fk_recibo_rh", referencedColumnName = "pk_recibo_rh")
    @ManyToOne(optional = false)
    private ReciboRh fkReciboRh;

    public ItemReciboRh()
    {
    }

    public ItemReciboRh( Integer pkItemReciboRh )
    {
        this.pkItemReciboRh = pkItemReciboRh;
    }

    public Integer getPkItemReciboRh()
    {
        return pkItemReciboRh;
    }

    public void setPkItemReciboRh( Integer pkItemReciboRh )
    {
        this.pkItemReciboRh = pkItemReciboRh;
    }

    public String getDescricao()
    {
        return descricao;
    }

    public void setDescricao( String descricao )
    {
        this.descricao = descricao;
    }

    public Double getRemuneracao()
    {
        return remuneracao;
    }

    public void setRemuneracao( Double remuneracao )
    {
        this.remuneracao = remuneracao;
    }

    public Double getDesconto()
    {
        return desconto;
    }

    public void setDesconto( Double desconto )
    {
        this.desconto = desconto;
    }

    public ReciboRh getFkReciboRh()
    {
        return fkReciboRh;
    }

    public void setFkReciboRh( ReciboRh fkReciboRh )
    {
        this.fkReciboRh = fkReciboRh;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += ( pkItemReciboRh != null ? pkItemReciboRh.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if ( !( object instanceof ItemReciboRh ) )
        {
            return false;
        }
        ItemReciboRh other = ( ItemReciboRh ) object;
        if ( ( this.pkItemReciboRh == null && other.pkItemReciboRh != null ) || ( this.pkItemReciboRh != null && !this.pkItemReciboRh.equals( other.pkItemReciboRh ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.ItemReciboRh[ pkItemReciboRh=" + pkItemReciboRh + " ]";
    }
    
}
