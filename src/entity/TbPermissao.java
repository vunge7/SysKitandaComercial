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
@Table(name = "tb_permissao")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "TbPermissao.findAll", query = "SELECT t FROM TbPermissao t"),
    @NamedQuery(name = "TbPermissao.findByIdPermissao", query = "SELECT t FROM TbPermissao t WHERE t.idPermissao = :idPermissao"),
    @NamedQuery(name = "TbPermissao.findByDescricao", query = "SELECT t FROM TbPermissao t WHERE t.descricao = :descricao")
})
public class TbPermissao implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idPermissao")
    private Long idPermissao;
    @Basic(optional = false)
    @Column(name = "descricao")
    private String descricao;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idPermissao")
    private List<TbItemPermissao> tbItemPermissaoList;

    public TbPermissao()
    {
    }

    public TbPermissao( Long idPermissao )
    {
        this.idPermissao = idPermissao;
    }

    public TbPermissao( Long idPermissao, String descricao )
    {
        this.idPermissao = idPermissao;
        this.descricao = descricao;
    }

    public Long getIdPermissao()
    {
        return idPermissao;
    }

    public void setIdPermissao( Long idPermissao )
    {
        this.idPermissao = idPermissao;
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
    public List<TbItemPermissao> getTbItemPermissaoList()
    {
        return tbItemPermissaoList;
    }

    public void setTbItemPermissaoList( List<TbItemPermissao> tbItemPermissaoList )
    {
        this.tbItemPermissaoList = tbItemPermissaoList;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += ( idPermissao != null ? idPermissao.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if ( !( object instanceof TbPermissao ) )
        {
            return false;
        }
        TbPermissao other = ( TbPermissao ) object;
        if ( ( this.idPermissao == null && other.idPermissao != null ) || ( this.idPermissao != null && !this.idPermissao.equals( other.idPermissao ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.TbPermissao[ idPermissao=" + idPermissao + " ]";
    }
    
}
