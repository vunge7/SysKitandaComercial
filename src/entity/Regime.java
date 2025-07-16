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
@Table(name = "regime")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "Regime.findAll", query = "SELECT r FROM Regime r"),
    @NamedQuery(name = "Regime.findByPkRegime", query = "SELECT r FROM Regime r WHERE r.pkRegime = :pkRegime"),
    @NamedQuery(name = "Regime.findByDesignacao", query = "SELECT r FROM Regime r WHERE r.designacao = :designacao"),
    @NamedQuery(name = "Regime.findByValor", query = "SELECT r FROM Regime r WHERE r.valor = :valor")
})
public class Regime implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pk_regime")
    private Integer pkRegime;
    @Column(name = "designacao")
    private String designacao;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor")
    private Double valor;
    @OneToMany(mappedBy = "fkRegime")
    private List<TbFornecedor> tbFornecedorList;

    public Regime()
    {
    }

    public Regime( Integer pkRegime )
    {
        this.pkRegime = pkRegime;
    }

    public Integer getPkRegime()
    {
        return pkRegime;
    }

    public void setPkRegime( Integer pkRegime )
    {
        this.pkRegime = pkRegime;
    }

    public String getDesignacao()
    {
        return designacao;
    }

    public void setDesignacao( String designacao )
    {
        this.designacao = designacao;
    }

    public Double getValor()
    {
        return valor;
    }

    public void setValor( Double valor )
    {
        this.valor = valor;
    }

    @XmlTransient
    public List<TbFornecedor> getTbFornecedorList()
    {
        return tbFornecedorList;
    }

    public void setTbFornecedorList( List<TbFornecedor> tbFornecedorList )
    {
        this.tbFornecedorList = tbFornecedorList;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += ( pkRegime != null ? pkRegime.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if ( !( object instanceof Regime ) )
        {
            return false;
        }
        Regime other = ( Regime ) object;
        if ( ( this.pkRegime == null && other.pkRegime != null ) || ( this.pkRegime != null && !this.pkRegime.equals( other.pkRegime ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.Regime[ pkRegime=" + pkRegime + " ]";
    }
    
}
