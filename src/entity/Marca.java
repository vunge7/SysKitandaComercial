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
@Table(name = "marca")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "Marca.findAll", query = "SELECT m FROM Marca m"),
    @NamedQuery(name = "Marca.findByPkMarca", query = "SELECT m FROM Marca m WHERE m.pkMarca = :pkMarca"),
    @NamedQuery(name = "Marca.findByDesignacao", query = "SELECT m FROM Marca m WHERE m.designacao = :designacao")
})
public class Marca implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pk_marca")
    private Integer pkMarca;
    @Column(name = "designacao")
    private String designacao;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkMarca")
    private List<Modelo> modeloList;

    public Marca()
    {
    }

    public Marca( Integer pkMarca )
    {
        this.pkMarca = pkMarca;
    }

    public Integer getPkMarca()
    {
        return pkMarca;
    }

    public void setPkMarca( Integer pkMarca )
    {
        this.pkMarca = pkMarca;
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
    public List<Modelo> getModeloList()
    {
        return modeloList;
    }

    public void setModeloList( List<Modelo> modeloList )
    {
        this.modeloList = modeloList;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += ( pkMarca != null ? pkMarca.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if ( !( object instanceof Marca ) )
        {
            return false;
        }
        Marca other = ( Marca ) object;
        if ( ( this.pkMarca == null && other.pkMarca != null ) || ( this.pkMarca != null && !this.pkMarca.equals( other.pkMarca ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.Marca[ pkMarca=" + pkMarca + " ]";
    }
    
}
