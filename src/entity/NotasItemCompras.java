/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

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
@Table(name = "notas_item_compras")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "NotasItemCompras.findAll", query = "SELECT n FROM NotasItemCompras n"),
    @NamedQuery(name = "NotasItemCompras.findByPkNotasItem", query = "SELECT n FROM NotasItemCompras n WHERE n.pkNotasItem = :pkNotasItem"),
    @NamedQuery(name = "NotasItemCompras.findByQuantidade", query = "SELECT n FROM NotasItemCompras n WHERE n.quantidade = :quantidade"),
    @NamedQuery(name = "NotasItemCompras.findByValorIva", query = "SELECT n FROM NotasItemCompras n WHERE n.valorIva = :valorIva"),
    @NamedQuery(name = "NotasItemCompras.findByMotivoIsensao", query = "SELECT n FROM NotasItemCompras n WHERE n.motivoIsensao = :motivoIsensao"),
    @NamedQuery(name = "NotasItemCompras.findByDesconto", query = "SELECT n FROM NotasItemCompras n WHERE n.desconto = :desconto"),
    @NamedQuery(name = "NotasItemCompras.findByTotal", query = "SELECT n FROM NotasItemCompras n WHERE n.total = :total"),
    @NamedQuery(name = "NotasItemCompras.findByFkPreco", query = "SELECT n FROM NotasItemCompras n WHERE n.fkPreco = :fkPreco"),
    @NamedQuery(name = "NotasItemCompras.findByCodigoIsencao", query = "SELECT n FROM NotasItemCompras n WHERE n.codigoIsencao = :codigoIsencao")
})
public class NotasItemCompras implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pk_notas_item")
    private Integer pkNotasItem;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
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
    @Column(name = "fk_preco")
    private Integer fkPreco;
    @Column(name = "codigo_isencao")
    private String codigoIsencao;
    @JoinColumn(name = "fk_compras", referencedColumnName = "pk_compra")
    @ManyToOne
    private Compras fkCompras;
    @JoinColumn(name = "fk_nota_compras", referencedColumnName = "pk_nota_compras")
    @ManyToOne
    private NotasCompras fkNotaCompras;
    @JoinColumn(name = "fk_produto", referencedColumnName = "codigo")
    @ManyToOne
    private TbProduto fkProduto;

    public NotasItemCompras()
    {
    }

    public NotasItemCompras( Integer pkNotasItem )
    {
        this.pkNotasItem = pkNotasItem;
    }

    public Integer getPkNotasItem()
    {
        return pkNotasItem;
    }

    public void setPkNotasItem( Integer pkNotasItem )
    {
        this.pkNotasItem = pkNotasItem;
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

    public Integer getFkPreco()
    {
        return fkPreco;
    }

    public void setFkPreco( Integer fkPreco )
    {
        this.fkPreco = fkPreco;
    }

    public String getCodigoIsencao()
    {
        return codigoIsencao;
    }

    public void setCodigoIsencao( String codigoIsencao )
    {
        this.codigoIsencao = codigoIsencao;
    }

    public Compras getFkCompras()
    {
        return fkCompras;
    }

    public void setFkCompras( Compras fkCompras )
    {
        this.fkCompras = fkCompras;
    }

    public NotasCompras getFkNotaCompras()
    {
        return fkNotaCompras;
    }

    public void setFkNotaCompras( NotasCompras fkNotaCompras )
    {
        this.fkNotaCompras = fkNotaCompras;
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
        hash += ( pkNotasItem != null ? pkNotasItem.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if ( !( object instanceof NotasItemCompras ) )
        {
            return false;
        }
        NotasItemCompras other = ( NotasItemCompras ) object;
        if ( ( this.pkNotasItem == null && other.pkNotasItem != null ) || ( this.pkNotasItem != null && !this.pkNotasItem.equals( other.pkNotasItem ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.NotasItemCompras[ pkNotasItem=" + pkNotasItem + " ]";
    }
    
}
