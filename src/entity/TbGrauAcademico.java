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
@Table(name = "tb_grau_academico")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "TbGrauAcademico.findAll", query = "SELECT t FROM TbGrauAcademico t"),
    @NamedQuery(name = "TbGrauAcademico.findByPkGrauAcademico", query = "SELECT t FROM TbGrauAcademico t WHERE t.pkGrauAcademico = :pkGrauAcademico"),
    @NamedQuery(name = "TbGrauAcademico.findByDesignacao", query = "SELECT t FROM TbGrauAcademico t WHERE t.designacao = :designacao")
})
public class TbGrauAcademico implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pk_grau_academico")
    private Integer pkGrauAcademico;
    @Column(name = "designacao")
    private String designacao;
    @OneToMany(mappedBy = "fkGrauAcademico")
    private List<TbFuncionario> tbFuncionarioList;

    public TbGrauAcademico()
    {
    }

    public TbGrauAcademico( Integer pkGrauAcademico )
    {
        this.pkGrauAcademico = pkGrauAcademico;
    }

    public Integer getPkGrauAcademico()
    {
        return pkGrauAcademico;
    }

    public void setPkGrauAcademico( Integer pkGrauAcademico )
    {
        this.pkGrauAcademico = pkGrauAcademico;
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
        hash += ( pkGrauAcademico != null ? pkGrauAcademico.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if ( !( object instanceof TbGrauAcademico ) )
        {
            return false;
        }
        TbGrauAcademico other = ( TbGrauAcademico ) object;
        if ( ( this.pkGrauAcademico == null && other.pkGrauAcademico != null ) || ( this.pkGrauAcademico != null && !this.pkGrauAcademico.equals( other.pkGrauAcademico ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.TbGrauAcademico[ pkGrauAcademico=" + pkGrauAcademico + " ]";
    }
    
}
