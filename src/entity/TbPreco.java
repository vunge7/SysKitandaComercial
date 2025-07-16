/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

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
@Table(name = "tb_preco")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "TbPreco.findAll", query = "SELECT t FROM TbPreco t"),
    @NamedQuery(name = "TbPreco.findByPkPreco", query = "SELECT t FROM TbPreco t WHERE t.pkPreco = :pkPreco"),
    @NamedQuery(name = "TbPreco.findByData", query = "SELECT t FROM TbPreco t WHERE t.data = :data"),
    @NamedQuery(name = "TbPreco.findByHora", query = "SELECT t FROM TbPreco t WHERE t.hora = :hora"),
    @NamedQuery(name = "TbPreco.findByPrecoCompra", query = "SELECT t FROM TbPreco t WHERE t.precoCompra = :precoCompra"),
    @NamedQuery(name = "TbPreco.findByPercentagemGanho", query = "SELECT t FROM TbPreco t WHERE t.percentagemGanho = :percentagemGanho"),
    @NamedQuery(name = "TbPreco.findByPrecoVenda", query = "SELECT t FROM TbPreco t WHERE t.precoVenda = :precoVenda"),
    @NamedQuery(name = "TbPreco.findByQtdBaixo", query = "SELECT t FROM TbPreco t WHERE t.qtdBaixo = :qtdBaixo"),
    @NamedQuery(name = "TbPreco.findByQtdAlto", query = "SELECT t FROM TbPreco t WHERE t.qtdAlto = :qtdAlto"),
    @NamedQuery(name = "TbPreco.findByPrecoAnterior", query = "SELECT t FROM TbPreco t WHERE t.precoAnterior = :precoAnterior"),
    @NamedQuery(name = "TbPreco.findByRetalho", query = "SELECT t FROM TbPreco t WHERE t.retalho = :retalho")
})
public class TbPreco implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pk_preco")
    private Integer pkPreco;
    @Basic(optional = false)
    @Column(name = "data")
    @Temporal(TemporalType.DATE)
    private Date data;
    @Column(name = "hora")
    @Temporal(TemporalType.TIME)
    private Date hora;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @Column(name = "preco_compra")
    private BigDecimal precoCompra;
    @Basic(optional = false)
    @Column(name = "percentagem_ganho")
    private BigDecimal percentagemGanho;
    @Basic(optional = false)
    @Column(name = "preco_venda")
    private BigDecimal precoVenda;
    @Basic(optional = false)
    @Column(name = "qtd_baixo")
    private int qtdBaixo;
    @Basic(optional = false)
    @Column(name = "qtd_alto")
    private int qtdAlto;
    @Column(name = "preco_anterior")
    private Double precoAnterior;
    @Column(name = "retalho")
    private Boolean retalho;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkPreco")
    private List<TbItemProForma> tbItemProFormaList;
    @JoinColumn(name = "fk_produto", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private TbProduto fkProduto;
    @JoinColumn(name = "fk_usuario", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private TbUsuario fkUsuario;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkPreco")
    private List<TbItemVenda> tbItemVendaList;
    @OneToMany(mappedBy = "fkPreco")
    private List<NotasItem> notasItemList;

    public TbPreco()
    {
    }

    public TbPreco( Integer pkPreco )
    {
        this.pkPreco = pkPreco;
    }

    public TbPreco( Integer pkPreco, Date data, BigDecimal precoCompra, BigDecimal percentagemGanho, BigDecimal precoVenda, int qtdBaixo, int qtdAlto )
    {
        this.pkPreco = pkPreco;
        this.data = data;
        this.precoCompra = precoCompra;
        this.percentagemGanho = percentagemGanho;
        this.precoVenda = precoVenda;
        this.qtdBaixo = qtdBaixo;
        this.qtdAlto = qtdAlto;
    }

    public Integer getPkPreco()
    {
        return pkPreco;
    }

    public void setPkPreco( Integer pkPreco )
    {
        this.pkPreco = pkPreco;
    }

    public Date getData()
    {
        return data;
    }

    public void setData( Date data )
    {
        this.data = data;
    }

    public Date getHora()
    {
        return hora;
    }

    public void setHora( Date hora )
    {
        this.hora = hora;
    }

    public BigDecimal getPrecoCompra()
    {
        return precoCompra;
    }

    public void setPrecoCompra( BigDecimal precoCompra )
    {
        this.precoCompra = precoCompra;
    }

    public BigDecimal getPercentagemGanho()
    {
        return percentagemGanho;
    }

    public void setPercentagemGanho( BigDecimal percentagemGanho )
    {
        this.percentagemGanho = percentagemGanho;
    }

    public BigDecimal getPrecoVenda()
    {
        return precoVenda;
    }

    public void setPrecoVenda( BigDecimal precoVenda )
    {
        this.precoVenda = precoVenda;
    }

    public int getQtdBaixo()
    {
        return qtdBaixo;
    }

    public void setQtdBaixo( int qtdBaixo )
    {
        this.qtdBaixo = qtdBaixo;
    }

    public int getQtdAlto()
    {
        return qtdAlto;
    }

    public void setQtdAlto( int qtdAlto )
    {
        this.qtdAlto = qtdAlto;
    }

    public Double getPrecoAnterior()
    {
        return precoAnterior;
    }

    public void setPrecoAnterior( Double precoAnterior )
    {
        this.precoAnterior = precoAnterior;
    }

    public Boolean getRetalho()
    {
        return retalho;
    }

    public void setRetalho( Boolean retalho )
    {
        this.retalho = retalho;
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

    public TbProduto getFkProduto()
    {
        return fkProduto;
    }

    public void setFkProduto( TbProduto fkProduto )
    {
        this.fkProduto = fkProduto;
    }

    public TbUsuario getFkUsuario()
    {
        return fkUsuario;
    }

    public void setFkUsuario( TbUsuario fkUsuario )
    {
        this.fkUsuario = fkUsuario;
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
    public List<NotasItem> getNotasItemList()
    {
        return notasItemList;
    }

    public void setNotasItemList( List<NotasItem> notasItemList )
    {
        this.notasItemList = notasItemList;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += ( pkPreco != null ? pkPreco.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if ( !( object instanceof TbPreco ) )
        {
            return false;
        }
        TbPreco other = ( TbPreco ) object;
        if ( ( this.pkPreco == null && other.pkPreco != null ) || ( this.pkPreco != null && !this.pkPreco.equals( other.pkPreco ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.TbPreco[ pkPreco=" + pkPreco + " ]";
    }
    
}
