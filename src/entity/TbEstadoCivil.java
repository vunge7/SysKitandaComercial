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
@Table(name = "tb_estado_civil")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "TbEstadoCivil.findAll", query = "SELECT t FROM TbEstadoCivil t"),
    @NamedQuery(name = "TbEstadoCivil.findByPkEstadoCivil", query = "SELECT t FROM TbEstadoCivil t WHERE t.pkEstadoCivil = :pkEstadoCivil"),
    @NamedQuery(name = "TbEstadoCivil.findByDesignacao", query = "SELECT t FROM TbEstadoCivil t WHERE t.designacao = :designacao")
})
public class TbEstadoCivil implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pk_estado_civil")
    private Integer pkEstadoCivil;
    @Column(name = "designacao")
    private String designacao;
    @OneToMany(mappedBy = "fkEstadoCivil")
    private List<TbFuncionario> tbFuncionarioList;

    public TbEstadoCivil()
    {
    }

    public TbEstadoCivil( Integer pkEstadoCivil )
    {
        this.pkEstadoCivil = pkEstadoCivil;
    }

    public Integer getPkEstadoCivil()
    {
        return pkEstadoCivil;
    }

    public void setPkEstadoCivil( Integer pkEstadoCivil )
    {
        this.pkEstadoCivil = pkEstadoCivil;
    }

    public String getDesignacao()
    {
        return designacao;
    }

    public void setDesignacao( String designacao )
    {
        this.designacao = designacao;
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
        hash += ( pkEstadoCivil != null ? pkEstadoCivil.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if ( !( object instanceof TbEstadoCivil ) )
        {
            return false;
        }
        TbEstadoCivil other = ( TbEstadoCivil ) object;
        if ( ( this.pkEstadoCivil == null && other.pkEstadoCivil != null ) || ( this.pkEstadoCivil != null && !this.pkEstadoCivil.equals( other.pkEstadoCivil ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.TbEstadoCivil[ pkEstadoCivil=" + pkEstadoCivil + " ]";
    }
    
}
