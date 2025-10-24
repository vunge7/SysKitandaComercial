/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;


import java.sql.Connection;
import java.io.Serializable;
import java.util.List;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author lenovo
 */
@Entity
@Table(name = "tb_provincia")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "TbProvincia.findAll", query = "SELECT t FROM TbProvincia t"),
    @NamedQuery(name = "TbProvincia.findByIdProvincia", query = "SELECT t FROM TbProvincia t WHERE t.idProvincia = :idProvincia"),
    @NamedQuery(name = "TbProvincia.findByDescrisao", query = "SELECT t FROM TbProvincia t WHERE t.descrisao = :descrisao")
})
public class TbProvincia implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idProvincia")
    private Integer idProvincia;
    @Column(name = "descrisao")
    private String descrisao;
    @OneToMany(mappedBy = "idProvincia")
    private List<TbMunicipio> tbMunicipioList;
    @JoinColumn(name = "idPais", referencedColumnName = "idPais")
    @ManyToOne
    private TbPais idPais;

    public TbProvincia()
    {
    }

    public TbProvincia( Integer idProvincia )
    {
        this.idProvincia = idProvincia;
    }

    public Integer getIdProvincia()
    {
        return idProvincia;
    }

    public void setIdProvincia( Integer idProvincia )
    {
        this.idProvincia = idProvincia;
    }

    public String getDescrisao()
    {
        return descrisao;
    }

    public void setDescrisao( String descrisao )
    {
        this.descrisao = descrisao;
    }

    @XmlTransient
    public List<TbMunicipio> getTbMunicipioList()
    {
        return tbMunicipioList;
    }

    public void setTbMunicipioList( List<TbMunicipio> tbMunicipioList )
    {
        this.tbMunicipioList = tbMunicipioList;
    }

    public TbPais getIdPais()
    {
        return idPais;
    }

    public void setIdPais( TbPais idPais )
    {
        this.idPais = idPais;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += ( idProvincia != null ? idProvincia.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if ( !( object instanceof TbProvincia ) )
        {
            return false;
        }
        TbProvincia other = ( TbProvincia ) object;
        if ( ( this.idProvincia == null && other.idProvincia != null ) || ( this.idProvincia != null && !this.idProvincia.equals( other.idProvincia ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.TbProvincia[ idProvincia=" + idProvincia + " ]";
    }
    
}
