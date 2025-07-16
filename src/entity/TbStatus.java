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
@Table(name = "tb_status")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "TbStatus.findAll", query = "SELECT t FROM TbStatus t"),
    @NamedQuery(name = "TbStatus.findByIdStatus", query = "SELECT t FROM TbStatus t WHERE t.idStatus = :idStatus"),
    @NamedQuery(name = "TbStatus.findByDescrisao", query = "SELECT t FROM TbStatus t WHERE t.descrisao = :descrisao")
})
public class TbStatus implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idStatus")
    private Integer idStatus;
    @Column(name = "descrisao")
    private String descrisao;
    @OneToMany(mappedBy = "idStatus")
    private List<TbUsuario> tbUsuarioList;
    @OneToMany(mappedBy = "idStatusFK")
    private List<TbFuncionario> tbFuncionarioList;

    public TbStatus()
    {
    }

    public TbStatus( Integer idStatus )
    {
        this.idStatus = idStatus;
    }

    public Integer getIdStatus()
    {
        return idStatus;
    }

    public void setIdStatus( Integer idStatus )
    {
        this.idStatus = idStatus;
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
    public List<TbUsuario> getTbUsuarioList()
    {
        return tbUsuarioList;
    }

    public void setTbUsuarioList( List<TbUsuario> tbUsuarioList )
    {
        this.tbUsuarioList = tbUsuarioList;
    }

    @XmlTransient
    public List<TbFuncionario> getTbFuncionarioList()
    {
        return tbFuncionarioList;
    }

    public void setTbFuncionarioList( List<TbFuncionario> tbFuncionarioList )
    {
        this.tbFuncionarioList = tbFuncionarioList;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += ( idStatus != null ? idStatus.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if ( !( object instanceof TbStatus ) )
        {
            return false;
        }
        TbStatus other = ( TbStatus ) object;
        if ( ( this.idStatus == null && other.idStatus != null ) || ( this.idStatus != null && !this.idStatus.equals( other.idStatus ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.TbStatus[ idStatus=" + idStatus + " ]";
    }
    
}
