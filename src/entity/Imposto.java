/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
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
@Table(name = "imposto")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "Imposto.findAll", query = "SELECT i FROM Imposto i"),
    @NamedQuery(name = "Imposto.findByPkImposto", query = "SELECT i FROM Imposto i WHERE i.pkImposto = :pkImposto"),
    @NamedQuery(name = "Imposto.findByDescricao", query = "SELECT i FROM Imposto i WHERE i.descricao = :descricao"),
    @NamedQuery(name = "Imposto.findByTaxa", query = "SELECT i FROM Imposto i WHERE i.taxa = :taxa"),
    @NamedQuery(name = "Imposto.findByDataHora", query = "SELECT i FROM Imposto i WHERE i.dataHora = :dataHora")
})
public class Imposto implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pk_imposto")
    private Integer pkImposto;
    @Column(name = "descricao")
    private String descricao;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "taxa")
    private Double taxa;
    @Column(name = "data_hora")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataHora;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkImposto")
    private List<ProdutoImposto> produtoImpostoList;

    public Imposto()
    {
    }

    public Imposto( Integer pkImposto )
    {
        this.pkImposto = pkImposto;
    }

    public Integer getPkImposto()
    {
        return pkImposto;
    }

    public void setPkImposto( Integer pkImposto )
    {
        this.pkImposto = pkImposto;
    }

    public String getDescricao()
    {
        return descricao;
    }

    public void setDescricao( String descricao )
    {
        this.descricao = descricao;
    }

    public Double getTaxa()
    {
        return taxa;
    }

    public void setTaxa( Double taxa )
    {
        this.taxa = taxa;
    }

    public Date getDataHora()
    {
        return dataHora;
    }

    public void setDataHora( Date dataHora )
    {
        this.dataHora = dataHora;
    }

    @XmlTransient
    public List<ProdutoImposto> getProdutoImpostoList()
    {
        return produtoImpostoList;
    }

    public void setProdutoImpostoList( List<ProdutoImposto> produtoImpostoList )
    {
        this.produtoImpostoList = produtoImpostoList;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += ( pkImposto != null ? pkImposto.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if ( !( object instanceof Imposto ) )
        {
            return false;
        }
        Imposto other = ( Imposto ) object;
        if ( ( this.pkImposto == null && other.pkImposto != null ) || ( this.pkImposto != null && !this.pkImposto.equals( other.pkImposto ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.Imposto[ pkImposto=" + pkImposto + " ]";
    }
    
}
