/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;


import java.sql.Connection;
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
@Table(name = "modalidade")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "Modalidade.findAll", query = "SELECT m FROM Modalidade m"),
    @NamedQuery(name = "Modalidade.findByPkModalidade", query = "SELECT m FROM Modalidade m WHERE m.pkModalidade = :pkModalidade"),
    @NamedQuery(name = "Modalidade.findByDesignacao", query = "SELECT m FROM Modalidade m WHERE m.designacao = :designacao"),
    @NamedQuery(name = "Modalidade.findByDiasUteisTrabalho", query = "SELECT m FROM Modalidade m WHERE m.diasUteisTrabalho = :diasUteisTrabalho")
})
public class Modalidade implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pk_modalidade")
    private Integer pkModalidade;
    @Column(name = "designacao")
    private String designacao;
    @Column(name = "dias_uteis_trabalho")
    private Integer diasUteisTrabalho;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkModalidade")
    private List<TbFuncionario> tbFuncionarioList;

    public Modalidade()
    {
    }

    public Modalidade( Integer pkModalidade )
    {
        this.pkModalidade = pkModalidade;
    }

    public Integer getPkModalidade()
    {
        return pkModalidade;
    }

    public void setPkModalidade( Integer pkModalidade )
    {
        this.pkModalidade = pkModalidade;
    }

    public String getDesignacao()
    {
        return designacao;
    }

    public void setDesignacao( String designacao )
    {
        this.designacao = designacao;
    }

    public Integer getDiasUteisTrabalho()
    {
        return diasUteisTrabalho;
    }

    public void setDiasUteisTrabalho( Integer diasUteisTrabalho )
    {
        this.diasUteisTrabalho = diasUteisTrabalho;
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
        hash += ( pkModalidade != null ? pkModalidade.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if ( !( object instanceof Modalidade ) )
        {
            return false;
        }
        Modalidade other = ( Modalidade ) object;
        if ( ( this.pkModalidade == null && other.pkModalidade != null ) || ( this.pkModalidade != null && !this.pkModalidade.equals( other.pkModalidade ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.Modalidade[ pkModalidade=" + pkModalidade + " ]";
    }
    
}
