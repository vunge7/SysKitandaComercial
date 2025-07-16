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
@Table(name = "tb_item_estorno")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "TbItemEstorno.findAll", query = "SELECT t FROM TbItemEstorno t"),
    @NamedQuery(name = "TbItemEstorno.findByCodigo", query = "SELECT t FROM TbItemEstorno t WHERE t.codigo = :codigo"),
    @NamedQuery(name = "TbItemEstorno.findByQuantidade", query = "SELECT t FROM TbItemEstorno t WHERE t.quantidade = :quantidade")
})
public class TbItemEstorno implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "codigo")
    private Integer codigo;
    @Basic(optional = false)
    @Column(name = "quantidade")
    private int quantidade;
    @JoinColumn(name = "fk_estorno", referencedColumnName = "pk_estorno")
    @ManyToOne(optional = false)
    private TbEstorno fkEstorno;
    @JoinColumn(name = "fk_produtos", referencedColumnName = "codigo")
    @ManyToOne
    private TbProduto fkProdutos;

    public TbItemEstorno()
    {
    }

    public TbItemEstorno( Integer codigo )
    {
        this.codigo = codigo;
    }

    public TbItemEstorno( Integer codigo, int quantidade )
    {
        this.codigo = codigo;
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

    public int getQuantidade()
    {
        return quantidade;
    }

    public void setQuantidade( int quantidade )
    {
        this.quantidade = quantidade;
    }

    public TbEstorno getFkEstorno()
    {
        return fkEstorno;
    }

    public void setFkEstorno( TbEstorno fkEstorno )
    {
        this.fkEstorno = fkEstorno;
    }

    public TbProduto getFkProdutos()
    {
        return fkProdutos;
    }

    public void setFkProdutos( TbProduto fkProdutos )
    {
        this.fkProdutos = fkProdutos;
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
        if ( !( object instanceof TbItemEstorno ) )
        {
            return false;
        }
        TbItemEstorno other = ( TbItemEstorno ) object;
        if ( ( this.codigo == null && other.codigo != null ) || ( this.codigo != null && !this.codigo.equals( other.codigo ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.TbItemEstorno[ codigo=" + codigo + " ]";
    }
    
}
