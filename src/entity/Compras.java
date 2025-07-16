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
@Table(name = "compras")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "Compras.findAll", query = "SELECT c FROM Compras c"),
    @NamedQuery(name = "Compras.findByPkCompra", query = "SELECT c FROM Compras c WHERE c.pkCompra = :pkCompra"),
    @NamedQuery(name = "Compras.findByDataCompra", query = "SELECT c FROM Compras c WHERE c.dataCompra = :dataCompra"),
    @NamedQuery(name = "Compras.findByTotalCompra", query = "SELECT c FROM Compras c WHERE c.totalCompra = :totalCompra"),
    @NamedQuery(name = "Compras.findByNomeFornecedor", query = "SELECT c FROM Compras c WHERE c.nomeFornecedor = :nomeFornecedor"),
    @NamedQuery(name = "Compras.findByHashCod", query = "SELECT c FROM Compras c WHERE c.hashCod = :hashCod"),
    @NamedQuery(name = "Compras.findByCodFact", query = "SELECT c FROM Compras c WHERE c.codFact = :codFact"),
    @NamedQuery(name = "Compras.findByRefCodFact", query = "SELECT c FROM Compras c WHERE c.refCodFact = :refCodFact"),
    @NamedQuery(name = "Compras.findByTotalIva", query = "SELECT c FROM Compras c WHERE c.totalIva = :totalIva"),
    @NamedQuery(name = "Compras.findByAssinatura", query = "SELECT c FROM Compras c WHERE c.assinatura = :assinatura"),
    @NamedQuery(name = "Compras.findByTotalPorExtenso", query = "SELECT c FROM Compras c WHERE c.totalPorExtenso = :totalPorExtenso"),
    @NamedQuery(name = "Compras.findByDescontoComercial", query = "SELECT c FROM Compras c WHERE c.descontoComercial = :descontoComercial"),
    @NamedQuery(name = "Compras.findByDescontoFinanceiro", query = "SELECT c FROM Compras c WHERE c.descontoFinanceiro = :descontoFinanceiro"),
    @NamedQuery(name = "Compras.findByDescontoTotal", query = "SELECT c FROM Compras c WHERE c.descontoTotal = :descontoTotal"),
    @NamedQuery(name = "Compras.findByTotalIncidencia", query = "SELECT c FROM Compras c WHERE c.totalIncidencia = :totalIncidencia"),
    @NamedQuery(name = "Compras.findByObs", query = "SELECT c FROM Compras c WHERE c.obs = :obs"),
    @NamedQuery(name = "Compras.findByTotalGeral", query = "SELECT c FROM Compras c WHERE c.totalGeral = :totalGeral"),
    @NamedQuery(name = "Compras.findByValorEntregue", query = "SELECT c FROM Compras c WHERE c.valorEntregue = :valorEntregue"),
    @NamedQuery(name = "Compras.findByTroco", query = "SELECT c FROM Compras c WHERE c.troco = :troco"),
    @NamedQuery(name = "Compras.findByTotalIncidenciaIsento", query = "SELECT c FROM Compras c WHERE c.totalIncidenciaIsento = :totalIncidenciaIsento"),
    @NamedQuery(name = "Compras.findByDataLimiteLevantamento", query = "SELECT c FROM Compras c WHERE c.dataLimiteLevantamento = :dataLimiteLevantamento"),
    @NamedQuery(name = "Compras.findByFornecedorNif", query = "SELECT c FROM Compras c WHERE c.fornecedorNif = :fornecedorNif"),
    @NamedQuery(name = "Compras.findByAutorizado", query = "SELECT c FROM Compras c WHERE c.autorizado = :autorizado"),
    @NamedQuery(name = "Compras.findByDespachoEncomenda", query = "SELECT c FROM Compras c WHERE c.despachoEncomenda = :despachoEncomenda"),
    @NamedQuery(name = "Compras.findByEncomendado", query = "SELECT c FROM Compras c WHERE c.encomendado = :encomendado"),
    @NamedQuery(name = "Compras.findByStatusEliminado", query = "SELECT c FROM Compras c WHERE c.statusEliminado = :statusEliminado"),
    @NamedQuery(name = "Compras.findByStatusRecibo", query = "SELECT c FROM Compras c WHERE c.statusRecibo = :statusRecibo")
})
public class Compras implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pk_compra")
    private Integer pkCompra;
    @Column(name = "data_compra")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataCompra;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "total_compra")
    private Double totalCompra;
    @Column(name = "nome_fornecedor")
    private String nomeFornecedor;
    @Column(name = "hash_cod")
    private String hashCod;
    @Column(name = "cod_fact")
    private String codFact;
    @Column(name = "ref_cod_fact")
    private String refCodFact;
    @Column(name = "total_iva")
    private Double totalIva;
    @Column(name = "assinatura")
    private String assinatura;
    @Column(name = "total_por_extenso")
    private String totalPorExtenso;
    @Column(name = "desconto_comercial")
    private Double descontoComercial;
    @Column(name = "desconto_financeiro")
    private Double descontoFinanceiro;
    @Column(name = "desconto_total")
    private Double descontoTotal;
    @Column(name = "total_incidencia")
    private Double totalIncidencia;
    @Column(name = "obs")
    private String obs;
    @Column(name = "total_geral")
    private Double totalGeral;
    @Column(name = "valor_entregue")
    private Double valorEntregue;
    @Column(name = "troco")
    private Double troco;
    @Column(name = "total_incidencia_isento")
    private Double totalIncidenciaIsento;
    @Column(name = "data_limite_levantamento")
    @Temporal(TemporalType.DATE)
    private Date dataLimiteLevantamento;
    @Column(name = "fornecedor_nif")
    private String fornecedorNif;
    @Column(name = "autorizado")
    private Boolean autorizado;
    @Column(name = "despacho_encomenda")
    private Boolean despachoEncomenda;
    @Column(name = "encomendado")
    private Boolean encomendado;
    @Column(name = "status_eliminado")
    private String statusEliminado;
    @Column(name = "status_recibo")
    private Boolean statusRecibo;
    @Column(name = "valor_por_pagar")
    private BigDecimal valorPorPagar;
    @Column(name = "valor_pago")
    private BigDecimal valorPago;
    @Column(name = "doc_vosso")
    private String docVosso;
    @Column(name = "doc_vosso_numero")
    private String docVossoNumero;
    @Column(name = "doc_vosso_data")
    @Temporal(TemporalType.DATE)
    private Date docVossoData;
    @Column(name = "doc_vosso_data_vencimento")
    @Temporal(TemporalType.DATE)
    private Date docVossoDataVencimento;
    @JoinColumn(name = "fk_documento", referencedColumnName = "pk_documento")
    @ManyToOne(optional = false)
    private Documento fkDocumento;
    @JoinColumn(name = "fk_fornecedor", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private TbFornecedor fkFornecedor;
    @JoinColumn(name = "fk_ano_economico", referencedColumnName = "pk_ano_economico")
    @ManyToOne
    private AnoEconomico fkAnoEconomico;
    @JoinColumn(name = "codigo_usuario", referencedColumnName = "codigo")
    @ManyToOne
    private TbUsuario codigoUsuario;
    @JoinColumn(name = "idArmazemFK", referencedColumnName = "codigo")
    @ManyToOne
    private TbArmazem idArmazemFK;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkCompra")
    private List<ItemCompras> itemComprasList;
    @OneToMany(mappedBy = "fkCompras")
    private List<NotasItemCompras> notasItemComprasList;

    public Compras()
    {
    }

    public Compras( Integer pkCompra )
    {
        this.pkCompra = pkCompra;
    }

    public Integer getPkCompra()
    {
        return pkCompra;
    }

    public void setPkCompra( Integer pkCompra )
    {
        this.pkCompra = pkCompra;
    }

    public BigDecimal getValorPorPagar()
    {
        return valorPorPagar;
    }

    public void setValorPorPagar( BigDecimal valorPorPagar )
    {
        this.valorPorPagar = valorPorPagar;
    }

    public BigDecimal getValorPago()
    {
        return valorPago;
    }

    public void setValorPago( BigDecimal valorPago )
    {
        this.valorPago = valorPago;
    }

    public String getDocVosso()
    {
        return docVosso;
    }

    public void setDocVosso( String docVosso )
    {
        this.docVosso = docVosso;
    }

    public String getDocVossoNumero()
    {
        return docVossoNumero;
    }

    public void setDocVossoNumero( String docVossoNumero )
    {
        this.docVossoNumero = docVossoNumero;
    }

    public Date getDocVossoData()
    {
        return docVossoData;
    }

    public void setDocVossoData( Date docVossoData )
    {
        this.docVossoData = docVossoData;
    }

    public Date getDocVossoDataVencimento()
    {
        return docVossoDataVencimento;
    }

    public void setDocVossoDataVencimento( Date docVossoDataVencimento )
    {
        this.docVossoDataVencimento = docVossoDataVencimento;
    }

    public Date getDataCompra()
    {
        return dataCompra;
    }

    public void setDataCompra( Date dataCompra )
    {
        this.dataCompra = dataCompra;
    }

    public Double getTotalCompra()
    {
        return totalCompra;
    }

    public void setTotalCompra( Double totalCompra )
    {
        this.totalCompra = totalCompra;
    }

    public String getNomeFornecedor()
    {
        return nomeFornecedor;
    }

    public void setNomeFornecedor( String nomeFornecedor )
    {
        this.nomeFornecedor = nomeFornecedor;
    }

    public String getHashCod()
    {
        return hashCod;
    }

    public void setHashCod( String hashCod )
    {
        this.hashCod = hashCod;
    }

    public String getCodFact()
    {
        return codFact;
    }

    public void setCodFact( String codFact )
    {
        this.codFact = codFact;
    }

    public String getRefCodFact()
    {
        return refCodFact;
    }

    public void setRefCodFact( String refCodFact )
    {
        this.refCodFact = refCodFact;
    }

    public Double getTotalIva()
    {
        return totalIva;
    }

    public void setTotalIva( Double totalIva )
    {
        this.totalIva = totalIva;
    }

    public String getAssinatura()
    {
        return assinatura;
    }

    public void setAssinatura( String assinatura )
    {
        this.assinatura = assinatura;
    }

    public String getTotalPorExtenso()
    {
        return totalPorExtenso;
    }

    public void setTotalPorExtenso( String totalPorExtenso )
    {
        this.totalPorExtenso = totalPorExtenso;
    }

    public Double getDescontoComercial()
    {
        return descontoComercial;
    }

    public void setDescontoComercial( Double descontoComercial )
    {
        this.descontoComercial = descontoComercial;
    }

    public Double getDescontoFinanceiro()
    {
        return descontoFinanceiro;
    }

    public void setDescontoFinanceiro( Double descontoFinanceiro )
    {
        this.descontoFinanceiro = descontoFinanceiro;
    }

    public Double getDescontoTotal()
    {
        return descontoTotal;
    }

    public void setDescontoTotal( Double descontoTotal )
    {
        this.descontoTotal = descontoTotal;
    }

    public Double getTotalIncidencia()
    {
        return totalIncidencia;
    }

    public void setTotalIncidencia( Double totalIncidencia )
    {
        this.totalIncidencia = totalIncidencia;
    }

    public String getObs()
    {
        return obs;
    }

    public void setObs( String obs )
    {
        this.obs = obs;
    }

    public Double getTotalGeral()
    {
        return totalGeral;
    }

    public void setTotalGeral( Double totalGeral )
    {
        this.totalGeral = totalGeral;
    }

    public Double getValorEntregue()
    {
        return valorEntregue;
    }

    public void setValorEntregue( Double valorEntregue )
    {
        this.valorEntregue = valorEntregue;
    }

    public Double getTroco()
    {
        return troco;
    }

    public void setTroco( Double troco )
    {
        this.troco = troco;
    }

    public Double getTotalIncidenciaIsento()
    {
        return totalIncidenciaIsento;
    }

    public void setTotalIncidenciaIsento( Double totalIncidenciaIsento )
    {
        this.totalIncidenciaIsento = totalIncidenciaIsento;
    }

    public Date getDataLimiteLevantamento()
    {
        return dataLimiteLevantamento;
    }

    public void setDataLimiteLevantamento( Date dataLimiteLevantamento )
    {
        this.dataLimiteLevantamento = dataLimiteLevantamento;
    }

    public String getFornecedorNif()
    {
        return fornecedorNif;
    }

    public void setFornecedorNif( String fornecedorNif )
    {
        this.fornecedorNif = fornecedorNif;
    }

    public Boolean getAutorizado()
    {
        return autorizado;
    }

    public void setAutorizado( Boolean autorizado )
    {
        this.autorizado = autorizado;
    }

    public Boolean getDespachoEncomenda()
    {
        return despachoEncomenda;
    }

    public void setDespachoEncomenda( Boolean despachoEncomenda )
    {
        this.despachoEncomenda = despachoEncomenda;
    }

    public Boolean getEncomendado()
    {
        return encomendado;
    }

    public void setEncomendado( Boolean encomendado )
    {
        this.encomendado = encomendado;
    }

    public String getStatusEliminado()
    {
        return statusEliminado;
    }

    public void setStatusEliminado( String statusEliminado )
    {
        this.statusEliminado = statusEliminado;
    }

    public Boolean getStatusRecibo()
    {
        return statusRecibo;
    }

    public void setStatusRecibo( Boolean statusRecibo )
    {
        this.statusRecibo = statusRecibo;
    }

    public Documento getFkDocumento()
    {
        return fkDocumento;
    }

    public void setFkDocumento( Documento fkDocumento )
    {
        this.fkDocumento = fkDocumento;
    }

    public TbFornecedor getFkFornecedor()
    {
        return fkFornecedor;
    }

    public void setFkFornecedor( TbFornecedor fkFornecedor )
    {
        this.fkFornecedor = fkFornecedor;
    }

    public AnoEconomico getFkAnoEconomico()
    {
        return fkAnoEconomico;
    }

    public void setFkAnoEconomico( AnoEconomico fkAnoEconomico )
    {
        this.fkAnoEconomico = fkAnoEconomico;
    }

    public TbUsuario getCodigoUsuario()
    {
        return codigoUsuario;
    }

    public void setCodigoUsuario( TbUsuario codigoUsuario )
    {
        this.codigoUsuario = codigoUsuario;
    }

    public TbArmazem getIdArmazemFK()
    {
        return idArmazemFK;
    }

    public void setIdArmazemFK( TbArmazem idArmazemFK )
    {
        this.idArmazemFK = idArmazemFK;
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
        hash += ( pkCompra != null ? pkCompra.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if ( !( object instanceof Compras ) )
        {
            return false;
        }
        Compras other = ( Compras ) object;
        if ( ( this.pkCompra == null && other.pkCompra != null ) || ( this.pkCompra != null && !this.pkCompra.equals( other.pkCompra ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.Compras[ pkCompra=" + pkCompra + " ]";
    }
    
}
