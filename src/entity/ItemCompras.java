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
@Table(name = "item_compras")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "ItemCompras.findAll", query = "SELECT i FROM ItemCompras i"),
    @NamedQuery(name = "ItemCompras.findByPkItmCompras", query = "SELECT i FROM ItemCompras i WHERE i.pkItmCompras = :pkItmCompras"),
    @NamedQuery(name = "ItemCompras.findByPrecoCompra", query = "SELECT i FROM ItemCompras i WHERE i.precoCompra = :precoCompra"),
    @NamedQuery(name = "ItemCompras.findByQuantidade", query = "SELECT i FROM ItemCompras i WHERE i.quantidade = :quantidade"),
    @NamedQuery(name = "ItemCompras.findByValorIva", query = "SELECT i FROM ItemCompras i WHERE i.valorIva = :valorIva"),
    @NamedQuery(name = "ItemCompras.findByMotivoIsensao", query = "SELECT i FROM ItemCompras i WHERE i.motivoIsensao = :motivoIsensao"),
    @NamedQuery(name = "ItemCompras.findByDesconto", query = "SELECT i FROM ItemCompras i WHERE i.desconto = :desconto"),
    @NamedQuery(name = "ItemCompras.findByTotal", query = "SELECT i FROM ItemCompras i WHERE i.total = :total"),
    @NamedQuery(name = "ItemCompras.findByCodigoIsensao", query = "SELECT i FROM ItemCompras i WHERE i.codigoIsensao = :codigoIsensao")
})
public class ItemCompras implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pk_itm_compras")
    private Integer pkItmCompras;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "preco_compra")
    private Double precoCompra;
    @Column(name = "quantidade")
    private Double quantidade;
    @Column(name = "valor_iva")
    private Double valorIva;
    @Column(name = "motivo_isensao")
    private String motivoIsensao;
    @Column(name = "desconto")
    private Double desconto;
    @Column(name = "total")
    private Double total;
    @Column(name = "codigo_isensao")
    private String codigoIsensao;
    @JoinColumn(name = "fk_compra", referencedColumnName = "pk_compra")
    @ManyToOne(optional = false)
    private Compras fkCompra;
    @JoinColumn(name = "fk_produto", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private TbProduto fkProduto;

    public ItemCompras()
    {
    }

    public ItemCompras( Integer pkItmCompras )
    {
        this.pkItmCompras = pkItmCompras;
    }

    public Integer getPkItmCompras()
    {
        return pkItmCompras;
    }

    public void setPkItmCompras( Integer pkItmCompras )
    {
        this.pkItmCompras = pkItmCompras;
    }

    public Double getPrecoCompra()
    {
        return precoCompra;
    }

    public void setPrecoCompra( Double precoCompra )
    {
        this.precoCompra = precoCompra;
    }

    public Double getQuantidade()
    {
        return quantidade;
    }

    public void setQuantidade( Double quantidade )
    {
        this.quantidade = quantidade;
    }

    public Double getValorIva()
    {
        return valorIva;
    }

    public void setValorIva( Double valorIva )
    {
        this.valorIva = valorIva;
    }

    public String getMotivoIsensao()
    {
        return motivoIsensao;
    }

    public void setMotivoIsensao( String motivoIsensao )
    {
        this.motivoIsensao = motivoIsensao;
    }

    public Double getDesconto()
    {
        return desconto;
    }

    public void setDesconto( Double desconto )
    {
        this.desconto = desconto;
    }

    public Double getTotal()
    {
        return total;
    }

    public void setTotal( Double total )
    {
        this.total = total;
    }

    public String getCodigoIsensao()
    {
        return codigoIsensao;
    }

    public void setCodigoIsensao( String codigoIsensao )
    {
        this.codigoIsensao = codigoIsensao;
    }

    public Compras getFkCompra()
    {
        return fkCompra;
    }

    public void setFkCompra( Compras fkCompra )
    {
        this.fkCompra = fkCompra;
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
        hash += ( pkItmCompras != null ? pkItmCompras.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if ( !( object instanceof ItemCompras ) )
        {
            return false;
        }
        ItemCompras other = ( ItemCompras ) object;
        if ( ( this.pkItmCompras == null && other.pkItmCompras != null ) || ( this.pkItmCompras != null && !this.pkItmCompras.equals( other.pkItmCompras ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.ItemCompras[ pkItmCompras=" + pkItmCompras + " ]";
    }
    
}
