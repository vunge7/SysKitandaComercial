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
@Table(name = "tb_funcao")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "TbFuncao.findAll", query = "SELECT t FROM TbFuncao t"),
    @NamedQuery(name = "TbFuncao.findByPkFuncao", query = "SELECT t FROM TbFuncao t WHERE t.pkFuncao = :pkFuncao"),
    @NamedQuery(name = "TbFuncao.findByDesignacao", query = "SELECT t FROM TbFuncao t WHERE t.designacao = :designacao")
})
public class TbFuncao implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pk_funcao")
    private Integer pkFuncao;
    @Column(name = "designacao")
    private String designacao;
    @OneToMany(mappedBy = "fkFuncao")
    private List<TbFuncionario> tbFuncionarioList;

    public TbFuncao()
    {
    }

    public TbFuncao( Integer pkFuncao )
    {
        this.pkFuncao = pkFuncao;
    }

    public Integer getPkFuncao()
    {
        return pkFuncao;
    }

    public void setPkFuncao( Integer pkFuncao )
    {
        this.pkFuncao = pkFuncao;
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
        hash += ( pkFuncao != null ? pkFuncao.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if ( !( object instanceof TbFuncao ) )
        {
            return false;
        }
        TbFuncao other = ( TbFuncao ) object;
        if ( ( this.pkFuncao == null && other.pkFuncao != null ) || ( this.pkFuncao != null && !this.pkFuncao.equals( other.pkFuncao ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.TbFuncao[ pkFuncao=" + pkFuncao + " ]";
    }
    
}
