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
@Table(name = "tb_tipo_usuario")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "TbTipoUsuario.findAll", query = "SELECT t FROM TbTipoUsuario t"),
    @NamedQuery(name = "TbTipoUsuario.findByIdTipoUsuario", query = "SELECT t FROM TbTipoUsuario t WHERE t.idTipoUsuario = :idTipoUsuario"),
    @NamedQuery(name = "TbTipoUsuario.findByDescricao", query = "SELECT t FROM TbTipoUsuario t WHERE t.descricao = :descricao")
})
public class TbTipoUsuario implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idTipoUsuario")
    private Integer idTipoUsuario;
    @Basic(optional = false)
    @Column(name = "descricao")
    private String descricao;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idTipoUsuario")
    private List<TbUsuario> tbUsuarioList;

    public TbTipoUsuario()
    {
    }

    public TbTipoUsuario( Integer idTipoUsuario )
    {
        this.idTipoUsuario = idTipoUsuario;
    }

    public TbTipoUsuario( Integer idTipoUsuario, String descricao )
    {
        this.idTipoUsuario = idTipoUsuario;
        this.descricao = descricao;
    }

    public Integer getIdTipoUsuario()
    {
        return idTipoUsuario;
    }

    public void setIdTipoUsuario( Integer idTipoUsuario )
    {
        this.idTipoUsuario = idTipoUsuario;
    }

    public String getDescricao()
    {
        return descricao;
    }

    public void setDescricao( String descricao )
    {
        this.descricao = descricao;
    }

    @XmlTransient
    public List<TbUsuario> getTbUsuarioList()
    {
        return tbUsuarioList;
    }

    public void setTbUsuarioList( List<TbUsuario> tbUsuarioList )
    {
        this.tbUsuarioList = tbUsuarioList;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += ( idTipoUsuario != null ? idTipoUsuario.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if ( !( object instanceof TbTipoUsuario ) )
        {
            return false;
        }
        TbTipoUsuario other = ( TbTipoUsuario ) object;
        if ( ( this.idTipoUsuario == null && other.idTipoUsuario != null ) || ( this.idTipoUsuario != null && !this.idTipoUsuario.equals( other.idTipoUsuario ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.TbTipoUsuario[ idTipoUsuario=" + idTipoUsuario + " ]";
    }
    
}
