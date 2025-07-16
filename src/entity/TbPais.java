/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
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
@Table(name = "tb_pais")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "TbPais.findAll", query = "SELECT t FROM TbPais t"),
    @NamedQuery(name = "TbPais.findByIdPais", query = "SELECT t FROM TbPais t WHERE t.idPais = :idPais"),
    @NamedQuery(name = "TbPais.findByDescrisao", query = "SELECT t FROM TbPais t WHERE t.descrisao = :descrisao")
})
public class TbPais implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idPais")
    private Integer idPais;
    @Column(name = "descrisao")
    private String descrisao;
    @OneToMany(mappedBy = "idPais")
    private List<TbProvincia> tbProvinciaList;

    public TbPais()
    {
    }

    public TbPais( Integer idPais )
    {
        this.idPais = idPais;
    }

    public Integer getIdPais()
    {
        return idPais;
    }

    public void setIdPais( Integer idPais )
    {
        this.idPais = idPais;
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
    public List<TbProvincia> getTbProvinciaList()
    {
        return tbProvinciaList;
    }

    public void setTbProvinciaList( List<TbProvincia> tbProvinciaList )
    {
        this.tbProvinciaList = tbProvinciaList;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += ( idPais != null ? idPais.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if ( !( object instanceof TbPais ) )
        {
            return false;
        }
        TbPais other = ( TbPais ) object;
        if ( ( this.idPais == null && other.idPais != null ) || ( this.idPais != null && !this.idPais.equals( other.idPais ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.TbPais[ idPais=" + idPais + " ]";
    }
    
}
