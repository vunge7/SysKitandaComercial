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
@Table(name = "tb_especialidade")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "TbEspecialidade.findAll", query = "SELECT t FROM TbEspecialidade t"),
    @NamedQuery(name = "TbEspecialidade.findByPkEspecialidade", query = "SELECT t FROM TbEspecialidade t WHERE t.pkEspecialidade = :pkEspecialidade"),
    @NamedQuery(name = "TbEspecialidade.findByDesignacao", query = "SELECT t FROM TbEspecialidade t WHERE t.designacao = :designacao")
})
public class TbEspecialidade implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pk_especialidade")
    private Integer pkEspecialidade;
    @Basic(optional = false)
    @Column(name = "designacao")
    private String designacao;
    @OneToMany(mappedBy = "fkEspecialidade")
    private List<TbFuncionario> tbFuncionarioList;

    public TbEspecialidade()
    {
    }

    public TbEspecialidade( Integer pkEspecialidade )
    {
        this.pkEspecialidade = pkEspecialidade;
    }

    public TbEspecialidade( Integer pkEspecialidade, String designacao )
    {
        this.pkEspecialidade = pkEspecialidade;
        this.designacao = designacao;
    }

    public Integer getPkEspecialidade()
    {
        return pkEspecialidade;
    }

    public void setPkEspecialidade( Integer pkEspecialidade )
    {
        this.pkEspecialidade = pkEspecialidade;
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
        hash += ( pkEspecialidade != null ? pkEspecialidade.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if ( !( object instanceof TbEspecialidade ) )
        {
            return false;
        }
        TbEspecialidade other = ( TbEspecialidade ) object;
        if ( ( this.pkEspecialidade == null && other.pkEspecialidade != null ) || ( this.pkEspecialidade != null && !this.pkEspecialidade.equals( other.pkEspecialidade ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.TbEspecialidade[ pkEspecialidade=" + pkEspecialidade + " ]";
    }
    
}
