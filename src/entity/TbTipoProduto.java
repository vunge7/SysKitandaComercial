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
@Table(name = "tb_tipo_produto")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "TbTipoProduto.findAll", query = "SELECT t FROM TbTipoProduto t"),
    @NamedQuery(name = "TbTipoProduto.findByCodigo", query = "SELECT t FROM TbTipoProduto t WHERE t.codigo = :codigo"),
    @NamedQuery(name = "TbTipoProduto.findByDesignacao", query = "SELECT t FROM TbTipoProduto t WHERE t.designacao = :designacao")
})
public class TbTipoProduto implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "codigo")
    private Integer codigo;
    @Column(name = "designacao")
    private String designacao;
    @JoinColumn(name = "fk_familia", referencedColumnName = "pk_familia")
    @ManyToOne(optional = false)
    private Familia fkFamilia;
    @OneToMany(mappedBy = "codTipoProduto")
    private List<TbProduto> tbProdutoList;

    public TbTipoProduto()
    {
    }

    public TbTipoProduto( Integer codigo )
    {
        this.codigo = codigo;
    }

    public Integer getCodigo()
    {
        return codigo;
    }

    public void setCodigo( Integer codigo )
    {
        this.codigo = codigo;
    }

    public String getDesignacao()
    {
        return designacao;
    }

    public void setDesignacao( String designacao )
    {
        this.designacao = designacao;
    }

    public Familia getFkFamilia()
    {
        return fkFamilia;
    }

    public void setFkFamilia( Familia fkFamilia )
    {
        this.fkFamilia = fkFamilia;
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
        hash += ( codigo != null ? codigo.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if ( !( object instanceof TbTipoProduto ) )
        {
            return false;
        }
        TbTipoProduto other = ( TbTipoProduto ) object;
        if ( ( this.codigo == null && other.codigo != null ) || ( this.codigo != null && !this.codigo.equals( other.codigo ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.TbTipoProduto[ codigo=" + codigo + " ]";
    }
    
}
