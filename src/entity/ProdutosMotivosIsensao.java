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
@Table(name = "produtos_motivos_isensao")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "ProdutosMotivosIsensao.findAll", query = "SELECT p FROM ProdutosMotivosIsensao p"),
    @NamedQuery(name = "ProdutosMotivosIsensao.findByPkProdutosMotivosIsensao", query = "SELECT p FROM ProdutosMotivosIsensao p WHERE p.pkProdutosMotivosIsensao = :pkProdutosMotivosIsensao"),
    @NamedQuery(name = "ProdutosMotivosIsensao.findByRegime", query = "SELECT p FROM ProdutosMotivosIsensao p WHERE p.regime = :regime"),
    @NamedQuery(name = "ProdutosMotivosIsensao.findByDescricao", query = "SELECT p FROM ProdutosMotivosIsensao p WHERE p.descricao = :descricao"),
    @NamedQuery(name = "ProdutosMotivosIsensao.findByCodigoRegime", query = "SELECT p FROM ProdutosMotivosIsensao p WHERE p.codigoRegime = :codigoRegime")
})
public class ProdutosMotivosIsensao implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pk_produtos_motivos_isensao")
    private Integer pkProdutosMotivosIsensao;
    @Column(name = "regime")
    private String regime;
    @Column(name = "descricao")
    private String descricao;
    @Column(name = "codigo_regime")
    private String codigoRegime;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkProdutosMotivosIsensao")
    private List<ProdutoIsento> produtoIsentoList;

    public ProdutosMotivosIsensao()
    {
    }

    public ProdutosMotivosIsensao( Integer pkProdutosMotivosIsensao )
    {
        this.pkProdutosMotivosIsensao = pkProdutosMotivosIsensao;
    }

    public Integer getPkProdutosMotivosIsensao()
    {
        return pkProdutosMotivosIsensao;
    }

    public void setPkProdutosMotivosIsensao( Integer pkProdutosMotivosIsensao )
    {
        this.pkProdutosMotivosIsensao = pkProdutosMotivosIsensao;
    }

    public String getRegime()
    {
        return regime;
    }

    public void setRegime( String regime )
    {
        this.regime = regime;
    }

    public String getDescricao()
    {
        return descricao;
    }

    public void setDescricao( String descricao )
    {
        this.descricao = descricao;
    }

    public String getCodigoRegime()
    {
        return codigoRegime;
    }

    public void setCodigoRegime( String codigoRegime )
    {
        this.codigoRegime = codigoRegime;
    }

    @XmlTransient
    public List<ProdutoIsento> getProdutoIsentoList()
    {
        return produtoIsentoList;
    }

    public void setProdutoIsentoList( List<ProdutoIsento> produtoIsentoList )
    {
        this.produtoIsentoList = produtoIsentoList;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += ( pkProdutosMotivosIsensao != null ? pkProdutosMotivosIsensao.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if ( !( object instanceof ProdutosMotivosIsensao ) )
        {
            return false;
        }
        ProdutosMotivosIsensao other = ( ProdutosMotivosIsensao ) object;
        if ( ( this.pkProdutosMotivosIsensao == null && other.pkProdutosMotivosIsensao != null ) || ( this.pkProdutosMotivosIsensao != null && !this.pkProdutosMotivosIsensao.equals( other.pkProdutosMotivosIsensao ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.ProdutosMotivosIsensao[ pkProdutosMotivosIsensao=" + pkProdutosMotivosIsensao + " ]";
    }
    
}
