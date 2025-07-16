/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
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
@Table(name = "notas_item")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "NotasItem.findAll", query = "SELECT n FROM NotasItem n"),
    @NamedQuery(name = "NotasItem.findByPkNotasItem", query = "SELECT n FROM NotasItem n WHERE n.pkNotasItem = :pkNotasItem"),
    @NamedQuery(name = "NotasItem.findByQuantidade", query = "SELECT n FROM NotasItem n WHERE n.quantidade = :quantidade"),
    @NamedQuery(name = "NotasItem.findByValorIva", query = "SELECT n FROM NotasItem n WHERE n.valorIva = :valorIva"),
    @NamedQuery(name = "NotasItem.findByMotivoIsensao", query = "SELECT n FROM NotasItem n WHERE n.motivoIsensao = :motivoIsensao"),
    @NamedQuery(name = "NotasItem.findByDesconto", query = "SELECT n FROM NotasItem n WHERE n.desconto = :desconto"),
    @NamedQuery(name = "NotasItem.findByTotal", query = "SELECT n FROM NotasItem n WHERE n.total = :total"),
    @NamedQuery(name = "NotasItem.findByCodigoIsencao", query = "SELECT n FROM NotasItem n WHERE n.codigoIsencao = :codigoIsencao"),
    @NamedQuery(name = "NotasItem.findByTotalImposto", query = "SELECT n FROM NotasItem n WHERE n.totalImposto = :totalImposto")
})
public class NotasItem implements Serializable
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
    private BigDecimal total;
    @Column(name = "codigo_isencao")
    private String codigoIsencao;
    @Column(name = "total_imposto")
    private BigDecimal totalImposto;
    @JoinColumn(name = "fk_nota", referencedColumnName = "pk_nota")
    @ManyToOne
    private Notas fkNota;
    @JoinColumn(name = "fk_preco", referencedColumnName = "pk_preco")
    @ManyToOne
    private TbPreco fkPreco;
    @JoinColumn(name = "fk_produto", referencedColumnName = "codigo")
    @ManyToOne
    private TbProduto fkProduto;
    @JoinColumn(name = "fk_venda", referencedColumnName = "codigo")
    @ManyToOne
    private TbVenda fkVenda;

    public NotasItem()
    {
    }

    public NotasItem( Integer pkNotasItem )
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

    public BigDecimal getTotal()
    {
        return total;
    }

    public void setTotal( BigDecimal total )
    {
        this.total = total;
    }

    public String getCodigoIsencao()
    {
        return codigoIsencao;
    }

    public void setCodigoIsencao( String codigoIsencao )
    {
        this.codigoIsencao = codigoIsencao;
    }

    public BigDecimal getTotalImposto()
    {
        return totalImposto;
    }

    public void setTotalImposto( BigDecimal totalImposto )
    {
        this.totalImposto = totalImposto;
    }

    public Notas getFkNota()
    {
        return fkNota;
    }

    public void setFkNota( Notas fkNota )
    {
        this.fkNota = fkNota;
    }

    public TbPreco getFkPreco()
    {
        return fkPreco;
    }

    public void setFkPreco( TbPreco fkPreco )
    {
        this.fkPreco = fkPreco;
    }

    public TbProduto getFkProduto()
    {
        return fkProduto;
    }

    public void setFkProduto( TbProduto fkProduto )
    {
        this.fkProduto = fkProduto;
    }

    public TbVenda getFkVenda()
    {
        return fkVenda;
    }

    public void setFkVenda( TbVenda fkVenda )
    {
        this.fkVenda = fkVenda;
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
        if ( !( object instanceof NotasItem ) )
        {
            return false;
        }
        NotasItem other = ( NotasItem ) object;
        if ( ( this.pkNotasItem == null && other.pkNotasItem != null ) || ( this.pkNotasItem != null && !this.pkNotasItem.equals( other.pkNotasItem ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.NotasItem[ pkNotasItem=" + pkNotasItem + " ]";
    }
    
}
