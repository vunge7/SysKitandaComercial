/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

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
@Table(name = "tb_item_permissao")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "TbItemPermissao.findAll", query = "SELECT t FROM TbItemPermissao t"),
    @NamedQuery(name = "TbItemPermissao.findByIdItemPermissao", query = "SELECT t FROM TbItemPermissao t WHERE t.idItemPermissao = :idItemPermissao"),
    @NamedQuery(name = "TbItemPermissao.findByStatus", query = "SELECT t FROM TbItemPermissao t WHERE t.status = :status"),
    @NamedQuery(name = "TbItemPermissao.findByModulo", query = "SELECT t FROM TbItemPermissao t WHERE t.modulo = :modulo")
})
public class TbItemPermissao implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idItemPermissao")
    private Long idItemPermissao;
    @Basic(optional = false)
    @Column(name = "status")
    private String status;
    @Basic(optional = false)
    @Column(name = "modulo")
    private String modulo;
    @JoinColumn(name = "idUsuario", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private TbUsuario idUsuario;
    @JoinColumn(name = "idPermissao", referencedColumnName = "idPermissao")
    @ManyToOne(optional = false)
    private TbPermissao idPermissao;

    public TbItemPermissao()
    {
    }

    public TbItemPermissao( Long idItemPermissao )
    {
        this.idItemPermissao = idItemPermissao;
    }

    public TbItemPermissao( Long idItemPermissao, String status, String modulo )
    {
        this.idItemPermissao = idItemPermissao;
        this.status = status;
        this.modulo = modulo;
    }

    public Long getIdItemPermissao()
    {
        return idItemPermissao;
    }

    public void setIdItemPermissao( Long idItemPermissao )
    {
        this.idItemPermissao = idItemPermissao;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus( String status )
    {
        this.status = status;
    }

    public String getModulo()
    {
        return modulo;
    }

    public void setModulo( String modulo )
    {
        this.modulo = modulo;
    }

    public TbUsuario getIdUsuario()
    {
        return idUsuario;
    }

    public void setIdUsuario( TbUsuario idUsuario )
    {
        this.idUsuario = idUsuario;
    }

    public TbPermissao getIdPermissao()
    {
        return idPermissao;
    }

    public void setIdPermissao( TbPermissao idPermissao )
    {
        this.idPermissao = idPermissao;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += ( idItemPermissao != null ? idItemPermissao.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if ( !( object instanceof TbItemPermissao ) )
        {
            return false;
        }
        TbItemPermissao other = ( TbItemPermissao ) object;
        if ( ( this.idItemPermissao == null && other.idItemPermissao != null ) || ( this.idItemPermissao != null && !this.idItemPermissao.equals( other.idItemPermissao ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.TbItemPermissao[ idItemPermissao=" + idItemPermissao + " ]";
    }
    
}
