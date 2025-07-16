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
@Table(name = "produto_imposto")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "ProdutoImposto.findAll", query = "SELECT p FROM ProdutoImposto p"),
    @NamedQuery(name = "ProdutoImposto.findByPkProdutoImposto", query = "SELECT p FROM ProdutoImposto p WHERE p.pkProdutoImposto = :pkProdutoImposto")
})
public class ProdutoImposto implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pk_produto_imposto")
    private Integer pkProdutoImposto;
    @JoinColumn(name = "fk_imposto", referencedColumnName = "pk_imposto")
    @ManyToOne(optional = false)
    private Imposto fkImposto;
    @JoinColumn(name = "fk_produto", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private TbProduto fkProduto;

    public ProdutoImposto()
    {
    }

    public ProdutoImposto( Integer pkProdutoImposto )
    {
        this.pkProdutoImposto = pkProdutoImposto;
    }

    public Integer getPkProdutoImposto()
    {
        return pkProdutoImposto;
    }

    public void setPkProdutoImposto( Integer pkProdutoImposto )
    {
        this.pkProdutoImposto = pkProdutoImposto;
    }

    public Imposto getFkImposto()
    {
        return fkImposto;
    }

    public void setFkImposto( Imposto fkImposto )
    {
        this.fkImposto = fkImposto;
    }

    public TbProduto getFkProduto()
    {
        return fkProduto;
    }

    public void setFkProduto( TbProduto fkProduto )
    {
        this.fkProduto = fkProduto;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += ( pkProdutoImposto != null ? pkProdutoImposto.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if ( !( object instanceof ProdutoImposto ) )
        {
            return false;
        }
        ProdutoImposto other = ( ProdutoImposto ) object;
        if ( ( this.pkProdutoImposto == null && other.pkProdutoImposto != null ) || ( this.pkProdutoImposto != null && !this.pkProdutoImposto.equals( other.pkProdutoImposto ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.ProdutoImposto[ pkProdutoImposto=" + pkProdutoImposto + " ]";
    }
    
}
