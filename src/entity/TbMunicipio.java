/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;


import java.sql.Connection;
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
@Table(name = "tb_municipio")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "TbMunicipio.findAll", query = "SELECT t FROM TbMunicipio t"),
    @NamedQuery(name = "TbMunicipio.findByIdMunicipio", query = "SELECT t FROM TbMunicipio t WHERE t.idMunicipio = :idMunicipio"),
    @NamedQuery(name = "TbMunicipio.findByDescrisao", query = "SELECT t FROM TbMunicipio t WHERE t.descrisao = :descrisao")
})
public class TbMunicipio implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idMunicipio")
    private Integer idMunicipio;
    @Column(name = "descrisao")
    private String descrisao;
    @JoinColumn(name = "idProvincia", referencedColumnName = "idProvincia")
    @ManyToOne
    private TbProvincia idProvincia;

    public TbMunicipio()
    {
    }

    public TbMunicipio( Integer idMunicipio )
    {
        this.idMunicipio = idMunicipio;
    }

    public Integer getIdMunicipio()
    {
        return idMunicipio;
    }

    public void setIdMunicipio( Integer idMunicipio )
    {
        this.idMunicipio = idMunicipio;
    }

    public String getDescrisao()
    {
        return descrisao;
    }

    public void setDescrisao( String descrisao )
    {
        this.descrisao = descrisao;
    }

    public TbProvincia getIdProvincia()
    {
        return idProvincia;
    }

    public void setIdProvincia( TbProvincia idProvincia )
    {
        this.idProvincia = idProvincia;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += ( idMunicipio != null ? idMunicipio.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if ( !( object instanceof TbMunicipio ) )
        {
            return false;
        }
        TbMunicipio other = ( TbMunicipio ) object;
        if ( ( this.idMunicipio == null && other.idMunicipio != null ) || ( this.idMunicipio != null && !this.idMunicipio.equals( other.idMunicipio ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.TbMunicipio[ idMunicipio=" + idMunicipio + " ]";
    }
    
}
