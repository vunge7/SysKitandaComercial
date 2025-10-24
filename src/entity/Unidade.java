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
@Table(name = "unidade")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "Unidade.findAll", query = "SELECT u FROM Unidade u"),
    @NamedQuery(name = "Unidade.findByPkUnidade", query = "SELECT u FROM Unidade u WHERE u.pkUnidade = :pkUnidade"),
    @NamedQuery(name = "Unidade.findByDescricao", query = "SELECT u FROM Unidade u WHERE u.descricao = :descricao"),
    @NamedQuery(name = "Unidade.findByAbreviacao", query = "SELECT u FROM Unidade u WHERE u.abreviacao = :abreviacao")
})
public class Unidade implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pk_unidade")
    private Integer pkUnidade;
    @Column(name = "descricao")
    private String descricao;
    @Column(name = "abreviacao")
    private String abreviacao;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "codUnidade")
    private List<TbProduto> tbProdutoList;

    public Unidade()
    {
    }

    public Unidade( Integer pkUnidade )
    {
        this.pkUnidade = pkUnidade;
    }

    public Integer getPkUnidade()
    {
        return pkUnidade;
    }

    public void setPkUnidade( Integer pkUnidade )
    {
        this.pkUnidade = pkUnidade;
    }

    public String getDescricao()
    {
        return descricao;
    }

    public void setDescricao( String descricao )
    {
        this.descricao = descricao;
    }

    public String getAbreviacao()
    {
        return abreviacao;
    }

    public void setAbreviacao( String abreviacao )
    {
        this.abreviacao = abreviacao;
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
        hash += ( pkUnidade != null ? pkUnidade.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if ( !( object instanceof Unidade ) )
        {
            return false;
        }
        Unidade other = ( Unidade ) object;
        if ( ( this.pkUnidade == null && other.pkUnidade != null ) || ( this.pkUnidade != null && !this.pkUnidade.equals( other.pkUnidade ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.Unidade[ pkUnidade=" + pkUnidade + " ]";
    }
    
}
