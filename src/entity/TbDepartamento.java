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
@Table(name = "tb_departamento")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "TbDepartamento.findAll", query = "SELECT t FROM TbDepartamento t"),
    @NamedQuery(name = "TbDepartamento.findByPkDepartamento", query = "SELECT t FROM TbDepartamento t WHERE t.pkDepartamento = :pkDepartamento"),
    @NamedQuery(name = "TbDepartamento.findByDesignacao", query = "SELECT t FROM TbDepartamento t WHERE t.designacao = :designacao")
})
public class TbDepartamento implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pk_departamento")
    private Integer pkDepartamento;
    @Column(name = "designacao")
    private String designacao;
    @OneToMany(mappedBy = "fkDepartamento")
    private List<TbFuncionario> tbFuncionarioList;

    public TbDepartamento()
    {
    }

    public TbDepartamento( Integer pkDepartamento )
    {
        this.pkDepartamento = pkDepartamento;
    }

    public Integer getPkDepartamento()
    {
        return pkDepartamento;
    }

    public void setPkDepartamento( Integer pkDepartamento )
    {
        this.pkDepartamento = pkDepartamento;
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
        hash += ( pkDepartamento != null ? pkDepartamento.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if ( !( object instanceof TbDepartamento ) )
        {
            return false;
        }
        TbDepartamento other = ( TbDepartamento ) object;
        if ( ( this.pkDepartamento == null && other.pkDepartamento != null ) || ( this.pkDepartamento != null && !this.pkDepartamento.equals( other.pkDepartamento ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.TbDepartamento[ pkDepartamento=" + pkDepartamento + " ]";
    }
    
}
