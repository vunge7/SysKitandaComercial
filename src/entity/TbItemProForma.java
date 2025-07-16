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
@Table(name = "tb_item_pro_forma")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "TbItemProForma.findAll", query = "SELECT t FROM TbItemProForma t"),
    @NamedQuery(name = "TbItemProForma.findByPkItemProForma", query = "SELECT t FROM TbItemProForma t WHERE t.pkItemProForma = :pkItemProForma"),
    @NamedQuery(name = "TbItemProForma.findByQtd", query = "SELECT t FROM TbItemProForma t WHERE t.qtd = :qtd")
})
public class TbItemProForma implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pk_item_pro_forma")
    private Integer pkItemProForma;
    @Column(name = "qtd")
    private Integer qtd;
    @JoinColumn(name = "fk_preco", referencedColumnName = "pk_preco")
    @ManyToOne(optional = false)
    private TbPreco fkPreco;
    @JoinColumn(name = "fk_produto", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private TbProduto fkProduto;
    @JoinColumn(name = "fk_pro_forma", referencedColumnName = "pk_pro_forma")
    @ManyToOne(optional = false)
    private TbProForma fkProForma;

    public TbItemProForma()
    {
    }

    public TbItemProForma( Integer pkItemProForma )
    {
        this.pkItemProForma = pkItemProForma;
    }

    public Integer getPkItemProForma()
    {
        return pkItemProForma;
    }

    public void setPkItemProForma( Integer pkItemProForma )
    {
        this.pkItemProForma = pkItemProForma;
    }

    public Integer getQtd()
    {
        return qtd;
    }

    public void setQtd( Integer qtd )
    {
        this.qtd = qtd;
    }

    public TbPreco getFkPreco()
    {
        return fkPreco;
    }

    public void setFkPreco( TbPreco fkPreco )
    {
        this.fkPreco = fkPreco;
    }

    public TbProduto getFkProduto()
    {
        return fkProduto;
    }

    public void setFkProduto( TbProduto fkProduto )
    {
        this.fkProduto = fkProduto;
    }

    public TbProForma getFkProForma()
    {
        return fkProForma;
    }

    public void setFkProForma( TbProForma fkProForma )
    {
        this.fkProForma = fkProForma;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += ( pkItemProForma != null ? pkItemProForma.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if ( !( object instanceof TbItemProForma ) )
        {
            return false;
        }
        TbItemProForma other = ( TbItemProForma ) object;
        if ( ( this.pkItemProForma == null && other.pkItemProForma != null ) || ( this.pkItemProForma != null && !this.pkItemProForma.equals( other.pkItemProForma ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.TbItemProForma[ pkItemProForma=" + pkItemProForma + " ]";
    }
    
}
