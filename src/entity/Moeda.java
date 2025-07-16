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
@Table(name = "moeda")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "Moeda.findAll", query = "SELECT m FROM Moeda m"),
    @NamedQuery(name = "Moeda.findByPkMoeda", query = "SELECT m FROM Moeda m WHERE m.pkMoeda = :pkMoeda"),
    @NamedQuery(name = "Moeda.findByDesignacao", query = "SELECT m FROM Moeda m WHERE m.designacao = :designacao"),
    @NamedQuery(name = "Moeda.findByAbreviacao", query = "SELECT m FROM Moeda m WHERE m.abreviacao = :abreviacao")
})
public class Moeda implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pk_moeda")
    private Integer pkMoeda;
    @Column(name = "designacao")
    private String designacao;
    @Column(name = "abreviacao")
    private String abreviacao;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkMoeda")
    private List<Cambio> cambioList;

    public Moeda()
    {
    }

    public Moeda( Integer pkMoeda )
    {
        this.pkMoeda = pkMoeda;
    }

    public Integer getPkMoeda()
    {
        return pkMoeda;
    }

    public void setPkMoeda( Integer pkMoeda )
    {
        this.pkMoeda = pkMoeda;
    }

    public String getDesignacao()
    {
        return designacao;
    }

    public void setDesignacao( String designacao )
    {
        this.designacao = designacao;
    }

    public String getAbreviacao()
    {
        return abreviacao;
    }

    public void setAbreviacao( String abreviacao )
    {
        this.abreviacao = abreviacao;
    }

    @XmlTransient
    public List<Cambio> getCambioList()
    {
        return cambioList;
    }

    public void setCambioList( List<Cambio> cambioList )
    {
        this.cambioList = cambioList;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += ( pkMoeda != null ? pkMoeda.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if ( !( object instanceof Moeda ) )
        {
            return false;
        }
        Moeda other = ( Moeda ) object;
        if ( ( this.pkMoeda == null && other.pkMoeda != null ) || ( this.pkMoeda != null && !this.pkMoeda.equals( other.pkMoeda ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.Moeda[ pkMoeda=" + pkMoeda + " ]";
    }
    
}
