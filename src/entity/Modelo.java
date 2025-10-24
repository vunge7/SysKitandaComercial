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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "modelo")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "Modelo.findAll", query = "SELECT m FROM Modelo m"),
    @NamedQuery(name = "Modelo.findByPkModelo", query = "SELECT m FROM Modelo m WHERE m.pkModelo = :pkModelo"),
    @NamedQuery(name = "Modelo.findByDesignacao", query = "SELECT m FROM Modelo m WHERE m.designacao = :designacao")
})
public class Modelo implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pk_modelo")
    private Integer pkModelo;
    @Column(name = "designacao")
    private String designacao;
    @JoinColumn(name = "fk_marca", referencedColumnName = "pk_marca")
    @ManyToOne(optional = false)
    private Marca fkMarca;
    @OneToMany(mappedBy = "fkModelo")
    private List<TbProduto> tbProdutoList;

    public Modelo()
    {
    }

    public Modelo( Integer pkModelo )
    {
        this.pkModelo = pkModelo;
    }

    public Integer getPkModelo()
    {
        return pkModelo;
    }

    public void setPkModelo( Integer pkModelo )
    {
        this.pkModelo = pkModelo;
    }

    public String getDesignacao()
    {
        return designacao;
    }

    public void setDesignacao( String designacao )
    {
        this.designacao = designacao;
    }

    public Marca getFkMarca()
    {
        return fkMarca;
    }

    public void setFkMarca( Marca fkMarca )
    {
        this.fkMarca = fkMarca;
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
        hash += ( pkModelo != null ? pkModelo.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if ( !( object instanceof Modelo ) )
        {
            return false;
        }
        Modelo other = ( Modelo ) object;
        if ( ( this.pkModelo == null && other.pkModelo != null ) || ( this.pkModelo != null && !this.pkModelo.equals( other.pkModelo ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.Modelo[ pkModelo=" + pkModelo + " ]";
    }
    
}
