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
@Table(name = "grupo")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "Grupo.findAll", query = "SELECT g FROM Grupo g"),
    @NamedQuery(name = "Grupo.findByPkGrupo", query = "SELECT g FROM Grupo g WHERE g.pkGrupo = :pkGrupo"),
    @NamedQuery(name = "Grupo.findByDesignacao", query = "SELECT g FROM Grupo g WHERE g.designacao = :designacao")
})
public class Grupo implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pk_grupo")
    private Integer pkGrupo;
    @Column(name = "designacao")
    private String designacao;
    @OneToMany(mappedBy = "fkGrupo")
    private List<TbProduto> tbProdutoList;

    public Grupo()
    {
    }

    public Grupo( Integer pkGrupo )
    {
        this.pkGrupo = pkGrupo;
    }

    public Integer getPkGrupo()
    {
        return pkGrupo;
    }

    public void setPkGrupo( Integer pkGrupo )
    {
        this.pkGrupo = pkGrupo;
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
    public List<TbProduto> getTbProdutoList()
    {
        return tbProdutoList;
    }

    public void setTbProdutoList( List<TbProduto> tbProdutoList )
    {
        this.tbProdutoList = tbProdutoList;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += ( pkGrupo != null ? pkGrupo.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if ( !( object instanceof Grupo ) )
        {
            return false;
        }
        Grupo other = ( Grupo ) object;
        if ( ( this.pkGrupo == null && other.pkGrupo != null ) || ( this.pkGrupo != null && !this.pkGrupo.equals( other.pkGrupo ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.Grupo[ pkGrupo=" + pkGrupo + " ]";
    }
    
}
