/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;


import java.sql.Connection;
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
@Table(name = "tb_stock_mirrow")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "TbStockMirrow.findAll", query = "SELECT t FROM TbStockMirrow t"),
    @NamedQuery(name = "TbStockMirrow.findByCodigo", query = "SELECT t FROM TbStockMirrow t WHERE t.codigo = :codigo"),
    @NamedQuery(name = "TbStockMirrow.findByDataEntrada", query = "SELECT t FROM TbStockMirrow t WHERE t.dataEntrada = :dataEntrada"),
    @NamedQuery(name = "TbStockMirrow.findByQuantidadeExistente", query = "SELECT t FROM TbStockMirrow t WHERE t.quantidadeExistente = :quantidadeExistente"),
    @NamedQuery(name = "TbStockMirrow.findByStatus", query = "SELECT t FROM TbStockMirrow t WHERE t.status = :status"),
    @NamedQuery(name = "TbStockMirrow.findByPrecoVenda", query = "SELECT t FROM TbStockMirrow t WHERE t.precoVenda = :precoVenda"),
    @NamedQuery(name = "TbStockMirrow.findByQuantCritica", query = "SELECT t FROM TbStockMirrow t WHERE t.quantCritica = :quantCritica"),
    @NamedQuery(name = "TbStockMirrow.findByQuantBaixa", query = "SELECT t FROM TbStockMirrow t WHERE t.quantBaixa = :quantBaixa"),
    @NamedQuery(name = "TbStockMirrow.findByQuantidadeAntiga", query = "SELECT t FROM TbStockMirrow t WHERE t.quantidadeAntiga = :quantidadeAntiga"),
    @NamedQuery(name = "TbStockMirrow.findByPrecoVendaGrosso", query = "SELECT t FROM TbStockMirrow t WHERE t.precoVendaGrosso = :precoVendaGrosso"),
    @NamedQuery(name = "TbStockMirrow.findByQtdGrosso", query = "SELECT t FROM TbStockMirrow t WHERE t.qtdGrosso = :qtdGrosso"),
    @NamedQuery(name = "TbStockMirrow.findByPrecoVendaFabrica", query = "SELECT t FROM TbStockMirrow t WHERE t.precoVendaFabrica = :precoVendaFabrica"),
    @NamedQuery(name = "TbStockMirrow.findByDataFeicho", query = "SELECT t FROM TbStockMirrow t WHERE t.dataFeicho = :dataFeicho")
})
public class TbStockMirrow implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "codigo")
    private Integer codigo;
    @Column(name = "dataEntrada")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataEntrada;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "quantidade_existente")
    private Double quantidadeExistente;
    @Column(name = "status")
    private String status;
    @Basic(optional = false)
    @Column(name = "preco_venda")
    private BigDecimal precoVenda;
    @Basic(optional = false)
    @Column(name = "quant_critica")
    private int quantCritica;
    @Basic(optional = false)
    @Column(name = "quant_baixa")
    private int quantBaixa;
    @Basic(optional = false)
    @Column(name = "quantidade_antiga")
    private int quantidadeAntiga;
    @Basic(optional = false)
    @Column(name = "preco_venda_grosso")
    private BigDecimal precoVendaGrosso;
    @Basic(optional = false)
    @Column(name = "qtd_grosso")
    private int qtdGrosso;
    @Column(name = "preco_venda_fabrica")
    private BigDecimal precoVendaFabrica;
    @Column(name = "data_feicho")
    @Temporal(TemporalType.DATE)
    private Date dataFeicho;
    @JoinColumn(name = "cod_armazem", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private TbArmazem codArmazem;
    @JoinColumn(name = "cod_produto_codigo", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private TbProduto codProdutoCodigo;

    public TbStockMirrow()
    {
    }

    public TbStockMirrow( Integer codigo )
    {
        this.codigo = codigo;
    }

    public TbStockMirrow( Integer codigo, BigDecimal precoVenda, int quantCritica, int quantBaixa, int quantidadeAntiga, BigDecimal precoVendaGrosso, int qtdGrosso )
    {
        this.codigo = codigo;
        this.precoVenda = precoVenda;
        this.quantCritica = quantCritica;
        this.quantBaixa = quantBaixa;
        this.quantidadeAntiga = quantidadeAntiga;
        this.precoVendaGrosso = precoVendaGrosso;
        this.qtdGrosso = qtdGrosso;
    }

    public Integer getCodigo()
    {
        return codigo;
    }

    public void setCodigo( Integer codigo )
    {
        this.codigo = codigo;
    }

    public Date getDataEntrada()
    {
        return dataEntrada;
    }

    public void setDataEntrada( Date dataEntrada )
    {
        this.dataEntrada = dataEntrada;
    }

    public Double getQuantidadeExistente()
    {
        return quantidadeExistente;
    }

    public void setQuantidadeExistente( Double quantidadeExistente )
    {
        this.quantidadeExistente = quantidadeExistente;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus( String status )
    {
        this.status = status;
    }

    public BigDecimal getPrecoVenda()
    {
        return precoVenda;
    }

    public void setPrecoVenda( BigDecimal precoVenda )
    {
        this.precoVenda = precoVenda;
    }

    public int getQuantCritica()
    {
        return quantCritica;
    }

    public void setQuantCritica( int quantCritica )
    {
        this.quantCritica = quantCritica;
    }

    public int getQuantBaixa()
    {
        return quantBaixa;
    }

    public void setQuantBaixa( int quantBaixa )
    {
        this.quantBaixa = quantBaixa;
    }

    public int getQuantidadeAntiga()
    {
        return quantidadeAntiga;
    }

    public void setQuantidadeAntiga( int quantidadeAntiga )
    {
        this.quantidadeAntiga = quantidadeAntiga;
    }

    public BigDecimal getPrecoVendaGrosso()
    {
        return precoVendaGrosso;
    }

    public void setPrecoVendaGrosso( BigDecimal precoVendaGrosso )
    {
        this.precoVendaGrosso = precoVendaGrosso;
    }

    public int getQtdGrosso()
    {
        return qtdGrosso;
    }

    public void setQtdGrosso( int qtdGrosso )
    {
        this.qtdGrosso = qtdGrosso;
    }

    public BigDecimal getPrecoVendaFabrica()
    {
        return precoVendaFabrica;
    }

    public void setPrecoVendaFabrica( BigDecimal precoVendaFabrica )
    {
        this.precoVendaFabrica = precoVendaFabrica;
    }

    public Date getDataFeicho()
    {
        return dataFeicho;
    }

    public void setDataFeicho( Date dataFeicho )
    {
        this.dataFeicho = dataFeicho;
    }

    public TbArmazem getCodArmazem()
    {
        return codArmazem;
    }

    public void setCodArmazem( TbArmazem codArmazem )
    {
        this.codArmazem = codArmazem;
    }

    public TbProduto getCodProdutoCodigo()
    {
        return codProdutoCodigo;
    }

    public void setCodProdutoCodigo( TbProduto codProdutoCodigo )
    {
        this.codProdutoCodigo = codProdutoCodigo;
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
        if ( !( object instanceof TbStockMirrow ) )
        {
            return false;
        }
        TbStockMirrow other = ( TbStockMirrow ) object;
        if ( ( this.codigo == null && other.codigo != null ) || ( this.codigo != null && !this.codigo.equals( other.codigo ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.TbStockMirrow[ codigo=" + codigo + " ]";
    }
    
}
