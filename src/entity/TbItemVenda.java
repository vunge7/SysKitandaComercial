/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author lenovo
 */
@Entity
@Table(name = "tb_item_venda")
@XmlRootElement
@NamedQueries(
        {
            @NamedQuery(name = "TbItemVenda.findAll", query = "SELECT t FROM TbItemVenda t"),
            @NamedQuery(name = "TbItemVenda.findByCodigo", query = "SELECT t FROM TbItemVenda t WHERE t.codigo = :codigo"),
            @NamedQuery(name = "TbItemVenda.findByQuantidade", query = "SELECT t FROM TbItemVenda t WHERE t.quantidade = :quantidade"),
            @NamedQuery(name = "TbItemVenda.findByValorIva", query = "SELECT t FROM TbItemVenda t WHERE t.valorIva = :valorIva"),
            @NamedQuery(name = "TbItemVenda.findByMotivoIsensao", query = "SELECT t FROM TbItemVenda t WHERE t.motivoIsensao = :motivoIsensao"),
            @NamedQuery(name = "TbItemVenda.findByDesconto", query = "SELECT t FROM TbItemVenda t WHERE t.desconto = :desconto"),
            @NamedQuery(name = "TbItemVenda.findByTotal", query = "SELECT t FROM TbItemVenda t WHERE t.total = :total"),
            @NamedQuery(name = "TbItemVenda.findByCodigoIsensao", query = "SELECT t FROM TbItemVenda t WHERE t.codigoIsensao = :codigoIsensao"),
            @NamedQuery(name = "TbItemVenda.findByDataServico", query = "SELECT t FROM TbItemVenda t WHERE t.dataServico = :dataServico"),
            @NamedQuery(name = "TbItemVenda.findByValorRetencao", query = "SELECT t FROM TbItemVenda t WHERE t.valorRetencao = :valorRetencao")
        })
public class TbItemVenda implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "codigo")
    private Integer codigo;
    @Basic(optional = false)
    @Column(name = "quantidade")
    private double quantidade;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor_iva")
    private Double valorIva;
    @Column(name = "motivo_isensao")
    private String motivoIsensao;
    @Column(name = "desconto")
    private Double desconto;
    @Basic(optional = false)
    @Column(name = "total")
    private BigDecimal total;
    @Column(name = "codigo_isensao")
    private String codigoIsensao;
    @Column(name = "data_servico")
    @Temporal(TemporalType.DATE)
    private Date dataServico;
    @Column(name = "data_entrega")
    @Temporal(TemporalType.DATE)
    private Date dataEntrega;
    @Column(name = "obs")
    private String obs;
    @Column(name = "valor_retencao")
    private Double valorRetencao;
    @Column(name = "status_entrega")
    private Boolean statusEntrega;
    @Column(name = "posicao")
    private int posicao;
    @JoinColumn(name = "fk_lugares", referencedColumnName = "pk_lugares")
    @ManyToOne
    private TbLugares fkLugares;
    @JoinColumn(name = "fk_mesas", referencedColumnName = "pk_mesas")
    @ManyToOne
    private TbMesas fkMesas;
    @JoinColumn(name = "fk_preco", referencedColumnName = "pk_preco")
    @ManyToOne(optional = false)
    private TbPreco fkPreco;
    @JoinColumn(name = "codigo_produto", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private TbProduto codigoProduto;
    @JoinColumn(name = "codigo_venda", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private TbVenda codigoVenda;

    public TbItemVenda()
    {
    }

    public TbItemVenda( Integer codigo )
    {
        this.codigo = codigo;
    }

    public TbItemVenda( Integer codigo, double quantidade, BigDecimal total )
    {
        this.codigo = codigo;
        this.quantidade = quantidade;
        this.total = total;
    }

    public Integer getCodigo()
    {
        return codigo;
    }

    public void setCodigo( Integer codigo )
    {
        this.codigo = codigo;
    }

    public double getQuantidade()
    {
        return quantidade;
    }

    public void setQuantidade( double quantidade )
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

    public String getCodigoIsensao()
    {
        return codigoIsensao;
    }

    public void setCodigoIsensao( String codigoIsensao )
    {
        this.codigoIsensao = codigoIsensao;
    }

    public Date getDataServico()
    {
        return dataServico;
    }

    public void setDataServico( Date dataServico )
    {
        this.dataServico = dataServico;
    }

    public Double getValorRetencao()
    {
        return valorRetencao;
    }

    public void setValorRetencao( Double valorRetencao )
    {
        this.valorRetencao = valorRetencao;
    }

    public TbLugares getFkLugares()
    {
        return fkLugares;
    }

    public void setFkLugares( TbLugares fkLugares )
    {
        this.fkLugares = fkLugares;
    }

    public TbMesas getFkMesas()
    {
        return fkMesas;
    }

    public void setFkMesas( TbMesas fkMesas )
    {
        this.fkMesas = fkMesas;
    }

    public TbPreco getFkPreco()
    {
        return fkPreco;
    }

    public void setFkPreco( TbPreco fkPreco )
    {
        this.fkPreco = fkPreco;
    }

    public TbProduto getCodigoProduto()
    {
        return codigoProduto;
    }

    public void setCodigoProduto( TbProduto codigoProduto )
    {
        this.codigoProduto = codigoProduto;
    }

    public TbVenda getCodigoVenda()
    {
        return codigoVenda;
    }

    public void setCodigoVenda( TbVenda codigoVenda )
    {
        this.codigoVenda = codigoVenda;
    }

    public Date getDataEntrega()
    {
        return dataEntrega;
    }

    public void setDataEntrega( Date dataEntrega )
    {
        this.dataEntrega = dataEntrega;
    }

    public String getObs()
    {
        return obs;
    }

    public void setObs( String obs )
    {
        this.obs = obs;
    }

    public Boolean getStatusEntrega()
    {
        return statusEntrega;
    }

    public void setStatusEntrega( Boolean statusEntrega )
    {
        this.statusEntrega = statusEntrega;
    }

    public int getPosicao()
    {
        return posicao;
    }

    public void setPosicao( int posicao )
    {
        this.posicao = posicao;
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
        if ( !( object instanceof TbItemVenda ) )
        {
            return false;
        }
        TbItemVenda other = ( TbItemVenda ) object;
        if ( ( this.codigo == null && other.codigo != null ) || ( this.codigo != null && !this.codigo.equals( other.codigo ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.TbItemVenda[ codigo=" + codigo + " ]";
    }

}
