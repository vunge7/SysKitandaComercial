/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author marti
 */
@Entity
@Table( name = "referencias" )
@XmlRootElement
@NamedQueries( 
{
    @NamedQuery( name = "Referencias.findAll", query = "SELECT r FROM Referencias r" ),
    @NamedQuery( name = "Referencias.findById", query = "SELECT r FROM Referencias r WHERE r.id = :id" ),
    @NamedQuery( name = "Referencias.findByCodBarra", query = "SELECT r FROM Referencias r WHERE r.codBarra = :codBarra" ),
    @NamedQuery( name = "Referencias.findByProdutoId", query = "SELECT r FROM Referencias r WHERE r.produtoId = :produtoId" ),
    @NamedQuery( name = "Referencias.findByObs", query = "SELECT r FROM Referencias r WHERE r.obs = :obs" ),
    @NamedQuery( name = "Referencias.findByDataReferencia", query = "SELECT r FROM Referencias r WHERE r.dataReferencia = :dataReferencia" ),
    @NamedQuery( name = "Referencias.findByUsuarioId", query = "SELECT r FROM Referencias r WHERE r.usuarioId = :usuarioId" )
} )
public class Referencias implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Basic( optional = false )
    @Column( name = "id" )
    private Integer id;
    @Basic( optional = false )
    @Column( name = "cod_barra" )
    private String codBarra;
    @Column( name = "produto_id" )
    private Integer produtoId;
    @Column( name = "obs" )
    private String obs;
    @Column( name = "data_referencia" )
    @Temporal( TemporalType.DATE )
    private Date dataReferencia;
    @Column( name = "usuario_id" )
    private Integer usuarioId;

    public Referencias()
    {
    }

    public Referencias( Integer id )
    {
        this.id = id;
    }

    public Referencias( Integer id, String codBarra )
    {
        this.id = id;
        this.codBarra = codBarra;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId( Integer id )
    {
        this.id = id;
    }

    public String getCodBarra()
    {
        return codBarra;
    }

    public void setCodBarra( String codBarra )
    {
        this.codBarra = codBarra;
    }

    public Integer getProdutoId()
    {
        return produtoId;
    }

    public void setProdutoId( Integer produtoId )
    {
        this.produtoId = produtoId;
    }

    public String getObs()
    {
        return obs;
    }

    public void setObs( String obs )
    {
        this.obs = obs;
    }

    public Date getDataReferencia()
    {
        return dataReferencia;
    }

    public void setDataReferencia( Date dataReferencia )
    {
        this.dataReferencia = dataReferencia;
    }

    public Integer getUsuarioId()
    {
        return usuarioId;
    }

    public void setUsuarioId( Integer usuarioId )
    {
        this.usuarioId = usuarioId;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += ( id != null ? id.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if ( !( object instanceof Referencias ) )
        {
            return false;
        }
        Referencias other = (Referencias) object;
        if ( ( this.id == null && other.id != null ) || ( this.id != null && !this.id.equals( other.id ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.Referencias[ id=" + id + " ]";
    }
    
}
