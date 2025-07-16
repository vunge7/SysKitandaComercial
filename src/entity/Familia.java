/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

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
@Table(name = "familia")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "Familia.findAll", query = "SELECT f FROM Familia f"),
    @NamedQuery(name = "Familia.findByPkFamilia", query = "SELECT f FROM Familia f WHERE f.pkFamilia = :pkFamilia"),
    @NamedQuery(name = "Familia.findByDesignacao", query = "SELECT f FROM Familia f WHERE f.designacao = :designacao")
})
public class Familia implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pk_familia")
    private Integer pkFamilia;
    @Basic(optional = false)
    @Column(name = "designacao")
    private String designacao;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkFamilia")
    private List<TbTipoProduto> tbTipoProdutoList;

    public Familia()
    {
    }

    public Familia( Integer pkFamilia )
    {
        this.pkFamilia = pkFamilia;
    }

    public Familia( Integer pkFamilia, String designacao )
    {
        this.pkFamilia = pkFamilia;
        this.designacao = designacao;
    }

    public Integer getPkFamilia()
    {
        return pkFamilia;
    }

    public void setPkFamilia( Integer pkFamilia )
    {
        this.pkFamilia = pkFamilia;
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
    public List<TbTipoProduto> getTbTipoProdutoList()
    {
        return tbTipoProdutoList;
    }

    public void setTbTipoProdutoList( List<TbTipoProduto> tbTipoProdutoList )
    {
        this.tbTipoProdutoList = tbTipoProdutoList;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += ( pkFamilia != null ? pkFamilia.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if ( !( object instanceof Familia ) )
        {
            return false;
        }
        Familia other = ( Familia ) object;
        if ( ( this.pkFamilia == null && other.pkFamilia != null ) || ( this.pkFamilia != null && !this.pkFamilia.equals( other.pkFamilia ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.Familia[ pkFamilia=" + pkFamilia + " ]";
    }
    
}
