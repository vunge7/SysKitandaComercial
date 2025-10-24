/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;


import java.sql.Connection;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author lenovo
 */
@Entity
@Table(name = "tb_produto")
@XmlRootElement
@NamedQueries(
        {
            @NamedQuery(name = "TbProduto.findAll", query = "SELECT t FROM TbProduto t"),
            @NamedQuery(name = "TbProduto.findByCodigo", query = "SELECT t FROM TbProduto t WHERE t.codigo = :codigo"),
            @NamedQuery(name = "TbProduto.findByDesignacao", query = "SELECT t FROM TbProduto t WHERE t.designacao = :designacao"),
            @NamedQuery(name = "TbProduto.findByPreco", query = "SELECT t FROM TbProduto t WHERE t.preco = :preco"),
            @NamedQuery(name = "TbProduto.findByDataFabrico", query = "SELECT t FROM TbProduto t WHERE t.dataFabrico = :dataFabrico"),
            @NamedQuery(name = "TbProduto.findByDataExpiracao", query = "SELECT t FROM TbProduto t WHERE t.dataExpiracao = :dataExpiracao"),
            @NamedQuery(name = "TbProduto.findByCodBarra", query = "SELECT t FROM TbProduto t WHERE t.codBarra = :codBarra"),
            @NamedQuery(name = "TbProduto.findByStatus", query = "SELECT t FROM TbProduto t WHERE t.status = :status"),
            @NamedQuery(name = "TbProduto.findByDataEntrada", query = "SELECT t FROM TbProduto t WHERE t.dataEntrada = :dataEntrada"),
            @NamedQuery(name = "TbProduto.findByStocavel", query = "SELECT t FROM TbProduto t WHERE t.stocavel = :stocavel"),
            @NamedQuery(name = "TbProduto.findByPrecoVenda", query = "SELECT t FROM TbProduto t WHERE t.precoVenda = :precoVenda"),
            @NamedQuery(name = "TbProduto.findByPercentagemDesconto", query = "SELECT t FROM TbProduto t WHERE t.percentagemDesconto = :percentagemDesconto"),
            @NamedQuery(name = "TbProduto.findByQuantidadeDesconto", query = "SELECT t FROM TbProduto t WHERE t.quantidadeDesconto = :quantidadeDesconto"),
            @NamedQuery(name = "TbProduto.findByCodigoManual", query = "SELECT t FROM TbProduto t WHERE t.codigoManual = :codigoManual"),
            @NamedQuery(name = "TbProduto.findByStatusIva", query = "SELECT t FROM TbProduto t WHERE t.statusIva = :statusIva")
        })
public class TbProduto implements Serializable
{

    @Lob
    @Column( name = "photo" )
    private byte[] photo;
    @Column( name = "cod_pai" )
    private Integer codPai;
    @OneToMany( cascade = CascadeType.ALL, mappedBy = "idProduto" )
    private List<TbItemEntradas> tbItemEntradasList;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "codigo")
    private Integer codigo;
    @Column(name = "designacao")
    private String designacao;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "preco")
    private BigDecimal preco;
    @Basic(optional = false)
    @Column(name = "data_fabrico")
    @Temporal(TemporalType.DATE)
    private Date dataFabrico;
    @Basic(optional = false)
    @Column(name = "data_expiracao")
    @Temporal(TemporalType.DATE)
    private Date dataExpiracao;
    @Basic(optional = false)
    @Column(name = "codBarra")
    private String codBarra;
    @Basic(optional = false)
    @Column(name = "status")
    private String status;
    @Basic(optional = false)
    @Column(name = "data_entrada")
    @Temporal(TemporalType.DATE)
    private Date dataEntrada;
    @Basic(optional = false)
    @Column(name = "stocavel")
    private String stocavel;
    @Column(name = "preco_venda")
    private Double precoVenda;
    @Column(name = "percentagem_desconto")
    private Double percentagemDesconto;
    @Column(name = "quantidade_desconto")
    private Integer quantidadeDesconto;
    @Column(name = "codigo_manual")
    private String codigoManual;
    @Column(name = "cozinha")
    private String cozinha;
    @Column(name = "status_iva")
    private String statusIva;
    @Column(name = "unidade_compra")
    private Double unidadeCompra;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkProduto")
    private List<ProdutoIsento> produtoIsentoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "codProdutoCodigo")
    private List<TbStockMirrow> tbStockMirrowList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkProduto")
    private List<ProdutoImposto> produtoImpostoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkProdutos")
    private List<TbItemPedidos> tbItemPedidosList;
    @OneToMany(mappedBy = "idProduto")
    private List<TbAcerto> tbAcertoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idProduto")
    private List<TbItemEncomenda> tbItemEncomendaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkProduto")
    private List<ItemCompras> itemComprasList;
    @OneToMany(mappedBy = "fkProduto")
    private List<TbVasilhame> tbVasilhameList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkProdutos")
    private List<TbItemSaidas> tbItemSaidasList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkProduto")
    private List<TbDesconto> tbDescontoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkProduto")
    private List<TbItemProForma> tbItemProFormaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkProduto")
    private List<TbPreco> tbPrecoList;
    @OneToMany(mappedBy = "fkProduto")
    private List<ServicoRetencao> servicoRetencaoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "codigoProduto")
    private List<TbItemVenda> tbItemVendaList;
    @OneToMany(mappedBy = "fkProdutos")
    private List<TbItemEstorno> tbItemEstornoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idProduto")
    private List<TbEntrada> tbEntradaList;
    @JoinColumn(name = "fk_grupo", referencedColumnName = "pk_grupo")
    @ManyToOne
    private Grupo fkGrupo;
    @JoinColumn(name = "fk_modelo", referencedColumnName = "pk_modelo")
    @ManyToOne
    private Modelo fkModelo;
    @JoinColumn(name = "cod_fornecedores", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private TbFornecedor codFornecedores;
    @JoinColumn(name = "cod_local", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private TbLocal codLocal;
    @JoinColumn(name = "cod_Tipo_Produto", referencedColumnName = "codigo")
    @ManyToOne
    private TbTipoProduto codTipoProduto;
    @JoinColumn(name = "cod_unidade", referencedColumnName = "pk_unidade")
    @ManyToOne(optional = false)
    private Unidade codUnidade;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "codProdutoCodigo")
    private List<TbStock> tbStockList;
    @OneToMany(mappedBy = "fkProduto")
    private List<NotasItem> notasItemList;
    @OneToMany(mappedBy = "fkProduto")
    private List<NotasItemCompras> notasItemComprasList;

    public Double getUnidadeCompra()
    {
        return unidadeCompra;
    }

    public void setUnidadeCompra( Double unidadeCompra )
    {
        this.unidadeCompra = unidadeCompra;
    }


    public TbProduto()
    {
    }

    public TbProduto( Integer codigo )
    {
        this.codigo = codigo;
    }

    public TbProduto( Integer codigo, Date dataFabrico, Date dataExpiracao, String codBarra, String status, Date dataEntrada, String stocavel )
    {
        this.codigo = codigo;
        this.dataFabrico = dataFabrico;
        this.dataExpiracao = dataExpiracao;
        this.codBarra = codBarra;
        this.status = status;
        this.dataEntrada = dataEntrada;
        this.stocavel = stocavel;
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

    public int getCodPai()
    {
        return codPai;
    }

    public void setCodPai( int codPai )
    {
        this.codPai = codPai;
    }

    public void setDesignacao( String designacao )
    {
        this.designacao = designacao;
    }

    public BigDecimal getPreco()
    {
        return preco;
    }

    public void setPreco( BigDecimal preco )
    {
        this.preco = preco;
    }

    public Date getDataFabrico()
    {
        return dataFabrico;
    }

    public String getCozinha()
    {
        return cozinha;
    }

    public void setCozinha( String cozinha )
    {
        this.cozinha = cozinha;
    }

    public void setDataFabrico( Date dataFabrico )
    {
        this.dataFabrico = dataFabrico;
    }

    public Date getDataExpiracao()
    {
        return dataExpiracao;
    }

    public void setDataExpiracao( Date dataExpiracao )
    {
        this.dataExpiracao = dataExpiracao;
    }

    public String getCodBarra()
    {
        return codBarra;
    }

    public void setCodBarra( String codBarra )
    {
        this.codBarra = codBarra;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus( String status )
    {
        this.status = status;
    }

    public Date getDataEntrada()
    {
        return dataEntrada;
    }

    public void setDataEntrada( Date dataEntrada )
    {
        this.dataEntrada = dataEntrada;
    }

    public String getStocavel()
    {
        return stocavel;
    }

    public void setStocavel( String stocavel )
    {
        this.stocavel = stocavel;
    }

    public Double getPrecoVenda()
    {
        return precoVenda;
    }

    public void setPrecoVenda( Double precoVenda )
    {
        this.precoVenda = precoVenda;
    }

    public Double getPercentagemDesconto()
    {
        return percentagemDesconto;
    }

    public void setPercentagemDesconto( Double percentagemDesconto )
    {
        this.percentagemDesconto = percentagemDesconto;
    }

    public Integer getQuantidadeDesconto()
    {
        return quantidadeDesconto;
    }

    public void setQuantidadeDesconto( Integer quantidadeDesconto )
    {
        this.quantidadeDesconto = quantidadeDesconto;
    }

    public String getCodigoManual()
    {
        return codigoManual;
    }

    public void setCodigoManual( String codigoManual )
    {
        this.codigoManual = codigoManual;
    }


    public String getStatusIva()
    {
        return statusIva;
    }

    public void setStatusIva( String statusIva )
    {
        this.statusIva = statusIva;
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

    @XmlTransient
    public List<TbStockMirrow> getTbStockMirrowList()
    {
        return tbStockMirrowList;
    }

    public void setTbStockMirrowList( List<TbStockMirrow> tbStockMirrowList )
    {
        this.tbStockMirrowList = tbStockMirrowList;
    }

    @XmlTransient
    public List<ProdutoImposto> getProdutoImpostoList()
    {
        return produtoImpostoList;
    }

    public void setProdutoImpostoList( List<ProdutoImposto> produtoImpostoList )
    {
        this.produtoImpostoList = produtoImpostoList;
    }

    @XmlTransient
    public List<TbItemPedidos> getTbItemPedidosList()
    {
        return tbItemPedidosList;
    }

    public void setTbItemPedidosList( List<TbItemPedidos> tbItemPedidosList )
    {
        this.tbItemPedidosList = tbItemPedidosList;
    }

    @XmlTransient
    public List<TbAcerto> getTbAcertoList()
    {
        return tbAcertoList;
    }

    public void setTbAcertoList( List<TbAcerto> tbAcertoList )
    {
        this.tbAcertoList = tbAcertoList;
    }

    @XmlTransient
    public List<TbItemEncomenda> getTbItemEncomendaList()
    {
        return tbItemEncomendaList;
    }

    public void setTbItemEncomendaList( List<TbItemEncomenda> tbItemEncomendaList )
    {
        this.tbItemEncomendaList = tbItemEncomendaList;
    }

    @XmlTransient
    public List<ItemCompras> getItemComprasList()
    {
        return itemComprasList;
    }

    public void setItemComprasList( List<ItemCompras> itemComprasList )
    {
        this.itemComprasList = itemComprasList;
    }

    @XmlTransient
    public List<TbVasilhame> getTbVasilhameList()
    {
        return tbVasilhameList;
    }

    public void setTbVasilhameList( List<TbVasilhame> tbVasilhameList )
    {
        this.tbVasilhameList = tbVasilhameList;
    }

    @XmlTransient
    public List<TbItemSaidas> getTbItemSaidasList()
    {
        return tbItemSaidasList;
    }

    public void setTbItemSaidasList( List<TbItemSaidas> tbItemSaidasList )
    {
        this.tbItemSaidasList = tbItemSaidasList;
    }

    @XmlTransient
    public List<TbDesconto> getTbDescontoList()
    {
        return tbDescontoList;
    }

    public void setTbDescontoList( List<TbDesconto> tbDescontoList )
    {
        this.tbDescontoList = tbDescontoList;
    }

    @XmlTransient
    public List<TbItemProForma> getTbItemProFormaList()
    {
        return tbItemProFormaList;
    }

    public void setTbItemProFormaList( List<TbItemProForma> tbItemProFormaList )
    {
        this.tbItemProFormaList = tbItemProFormaList;
    }

    @XmlTransient
    public List<TbPreco> getTbPrecoList()
    {
        return tbPrecoList;
    }

    public void setTbPrecoList( List<TbPreco> tbPrecoList )
    {
        this.tbPrecoList = tbPrecoList;
    }

    @XmlTransient
    public List<ServicoRetencao> getServicoRetencaoList()
    {
        return servicoRetencaoList;
    }

    public void setServicoRetencaoList( List<ServicoRetencao> servicoRetencaoList )
    {
        this.servicoRetencaoList = servicoRetencaoList;
    }

    @XmlTransient
    public List<TbItemVenda> getTbItemVendaList()
    {
        return tbItemVendaList;
    }

    public void setTbItemVendaList( List<TbItemVenda> tbItemVendaList )
    {
        this.tbItemVendaList = tbItemVendaList;
    }

    @XmlTransient
    public List<TbItemEstorno> getTbItemEstornoList()
    {
        return tbItemEstornoList;
    }

    public void setTbItemEstornoList( List<TbItemEstorno> tbItemEstornoList )
    {
        this.tbItemEstornoList = tbItemEstornoList;
    }

    @XmlTransient
    public List<TbEntrada> getTbEntradaList()
    {
        return tbEntradaList;
    }

    public void setTbEntradaList( List<TbEntrada> tbEntradaList )
    {
        this.tbEntradaList = tbEntradaList;
    }

    public Grupo getFkGrupo()
    {
        return fkGrupo;
    }

    public void setFkGrupo( Grupo fkGrupo )
    {
        this.fkGrupo = fkGrupo;
    }

    public Modelo getFkModelo()
    {
        return fkModelo;
    }

    public void setFkModelo( Modelo fkModelo )
    {
        this.fkModelo = fkModelo;
    }

    public TbFornecedor getCodFornecedores()
    {
        return codFornecedores;
    }

    public void setCodFornecedores( TbFornecedor codFornecedores )
    {
        this.codFornecedores = codFornecedores;
    }

    public TbLocal getCodLocal()
    {
        return codLocal;
    }

    public void setCodLocal( TbLocal codLocal )
    {
        this.codLocal = codLocal;
    }

    public TbTipoProduto getCodTipoProduto()
    {
        return codTipoProduto;
    }

    public void setCodTipoProduto( TbTipoProduto codTipoProduto )
    {
        this.codTipoProduto = codTipoProduto;
    }

    public Unidade getCodUnidade()
    {
        return codUnidade;
    }

    public void setCodUnidade( Unidade codUnidade )
    {
        this.codUnidade = codUnidade;
    }

    @XmlTransient
    public List<TbStock> getTbStockList()
    {
        return tbStockList;
    }

    public void setTbStockList( List<TbStock> tbStockList )
    {
        this.tbStockList = tbStockList;
    }

    @XmlTransient
    public List<NotasItem> getNotasItemList()
    {
        return notasItemList;
    }

    public void setNotasItemList( List<NotasItem> notasItemList )
    {
        this.notasItemList = notasItemList;
    }

    @XmlTransient
    public List<NotasItemCompras> getNotasItemComprasList()
    {
        return notasItemComprasList;
    }

    public void setNotasItemComprasList( List<NotasItemCompras> notasItemComprasList )
    {
        this.notasItemComprasList = notasItemComprasList;
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
        if ( !( object instanceof TbProduto ) )
        {
            return false;
        }
        TbProduto other = ( TbProduto ) object;
        if ( ( this.codigo == null && other.codigo != null ) || ( this.codigo != null && !this.codigo.equals( other.codigo ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.TbProduto[ codigo=" + codigo + " ]";
    }

    public byte[] getPhoto()
    {
        return photo;
    }

    public void setPhoto( byte[] photo )
    {
        this.photo = photo;
    }

//    public Integer getCodPai()
//    {
//        return codPai;
//    }
//
//    public void setCodPai( Integer codPai )
//    {
//        this.codPai = codPai;
//    }

    @XmlTransient
    public List<TbItemEntradas> getTbItemEntradasList()
    {
        return tbItemEntradasList;
    }

    public void setTbItemEntradasList( List<TbItemEntradas> tbItemEntradasList )
    {
        this.tbItemEntradasList = tbItemEntradasList;
    }

}
