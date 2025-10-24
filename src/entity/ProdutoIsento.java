/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;


import java.sql.Connection;
import java.io.Serializable;
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
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author lenovo
 */
@Entity
@Table(name = "produto_isento")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "ProdutoIsento.findAll", query = "SELECT p FROM ProdutoIsento p"),
    @NamedQuery(name = "ProdutoIsento.findByPkProdutoIsento", query = "SELECT p FROM ProdutoIsento p WHERE p.pkProdutoIsento = :pkProdutoIsento")
})
public class ProdutoIsento implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pk_produto_isento")
    private Integer pkProdutoIsento;
    @JoinColumn(name = "fk_produtos_motivos_isensao", referencedColumnName = "pk_produtos_motivos_isensao")
    @ManyToOne(optional = false)
    private ProdutosMotivosIsensao fkProdutosMotivosIsensao;
    @JoinColumn(name = "fk_produto", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private TbProduto fkProduto;

    public ProdutoIsento()
    {
    }

    public ProdutoIsento( Integer pkProdutoIsento )
    {
        this.pkProdutoIsento = pkProdutoIsento;
    }

    public Integer getPkProdutoIsento()
    {
        return pkProdutoIsento;
    }

    public void setPkProdutoIsento( Integer pkProdutoIsento )
    {
        this.pkProdutoIsento = pkProdutoIsento;
    }

    public ProdutosMotivosIsensao getFkProdutosMotivosIsensao()
    {
        return fkProdutosMotivosIsensao;
    }

    public void setFkProdutosMotivosIsensao( ProdutosMotivosIsensao fkProdutosMotivosIsensao )
    {
        this.fkProdutosMotivosIsensao = fkProdutosMotivosIsensao;
    }

    public TbProduto getFkProduto()
    {
        return fkProduto;
    }

    public void setFkProduto( TbProduto fkProduto )
    {
        this.fkProduto = fkProduto;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += ( pkProdutoIsento != null ? pkProdutoIsento.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if ( !( object instanceof ProdutoIsento ) )
        {
            return false;
        }
        ProdutoIsento other = ( ProdutoIsento ) object;
        if ( ( this.pkProdutoIsento == null && other.pkProdutoIsento != null ) || ( this.pkProdutoIsento != null && !this.pkProdutoIsento.equals( other.pkProdutoIsento ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.ProdutoIsento[ pkProdutoIsento=" + pkProdutoIsento + " ]";
    }
    
}
