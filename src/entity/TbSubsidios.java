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
@Table(name = "tb_subsidios")
@XmlRootElement
@NamedQueries(
        {
            @NamedQuery(name = "TbSubsidios.findAll", query = "SELECT t FROM TbSubsidios t"),
            @NamedQuery(name = "TbSubsidios.findByIdSubsidios", query = "SELECT t FROM TbSubsidios t WHERE t.idSubsidios = :idSubsidios"),
            @NamedQuery(name = "TbSubsidios.findByDescricao", query = "SELECT t FROM TbSubsidios t WHERE t.descricao = :descricao"),
            @NamedQuery(name = "TbSubsidios.findByIncidencia", query = "SELECT t FROM TbSubsidios t WHERE t.incidencia = :incidencia")
        })
public class TbSubsidios implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idSubsidios")
    private Integer idSubsidios;
    @Column(name = "descricao")
    private String descricao;
    @Column(name = "incidencia")
    private Boolean incidencia;
    @OneToMany(mappedBy = "idSubsidioFK")
    private List<TbItemSubsidiosFuncionario> tbItemSubsidiosFuncionarioList;

    public TbSubsidios()
    {
    }

    public Boolean getIncidencia_inss()
    {
        return incidencia;
    }

    public TbSubsidios( Integer idSubsidios )
    {
        this.idSubsidios = idSubsidios;
    }

    public Integer getIdSubsidios()
    {
        return idSubsidios;
    }

    public void setIdSubsidios( Integer idSubsidios )
    {
        this.idSubsidios = idSubsidios;
    }

    public String getDescricao()
    {
        return descricao;
    }

    public void setDescricao( String descricao )
    {
        this.descricao = descricao;
    }

    public Boolean getIncidencia()
    {
        return incidencia;
    }

    public void setIncidencia( Boolean incidencia )
    {
        this.incidencia = incidencia;
    }

    @XmlTransient
    public List<TbItemSubsidiosFuncionario> getTbItemSubsidiosFuncionarioList()
    {
        return tbItemSubsidiosFuncionarioList;
    }

    public void setTbItemSubsidiosFuncionarioList( List<TbItemSubsidiosFuncionario> tbItemSubsidiosFuncionarioList )
    {
        this.tbItemSubsidiosFuncionarioList = tbItemSubsidiosFuncionarioList;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += ( idSubsidios != null ? idSubsidios.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if ( !( object instanceof TbSubsidios ) )
        {
            return false;
        }
        TbSubsidios other = ( TbSubsidios ) object;
        if ( ( this.idSubsidios == null && other.idSubsidios != null ) || ( this.idSubsidios != null && !this.idSubsidios.equals( other.idSubsidios ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.TbSubsidios[ idSubsidios=" + idSubsidios + " ]";
    }

}
