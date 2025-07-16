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
@Table(name = "tb_venda")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "TbVenda.findAll", query = "SELECT t FROM TbVenda t"),
    @NamedQuery(name = "TbVenda.findByCodigo", query = "SELECT t FROM TbVenda t WHERE t.codigo = :codigo"),
    @NamedQuery(name = "TbVenda.findByDataVenda", query = "SELECT t FROM TbVenda t WHERE t.dataVenda = :dataVenda"),
    @NamedQuery(name = "TbVenda.findByTotalVenda", query = "SELECT t FROM TbVenda t WHERE t.totalVenda = :totalVenda"),
    @NamedQuery(name = "TbVenda.findByPerformance", query = "SELECT t FROM TbVenda t WHERE t.performance = :performance"),
    @NamedQuery(name = "TbVenda.findByCredito", query = "SELECT t FROM TbVenda t WHERE t.credito = :credito"),
    @NamedQuery(name = "TbVenda.findByValorEntregue", query = "SELECT t FROM TbVenda t WHERE t.valorEntregue = :valorEntregue"),
    @NamedQuery(name = "TbVenda.findByTroco", query = "SELECT t FROM TbVenda t WHERE t.troco = :troco"),
    @NamedQuery(name = "TbVenda.findByHora", query = "SELECT t FROM TbVenda t WHERE t.hora = :hora"),
    @NamedQuery(name = "TbVenda.findByNomeCliente", query = "SELECT t FROM TbVenda t WHERE t.nomeCliente = :nomeCliente"),
    @NamedQuery(name = "TbVenda.findByStatusEliminado", query = "SELECT t FROM TbVenda t WHERE t.statusEliminado = :statusEliminado"),
    @NamedQuery(name = "TbVenda.findByDescontoTotal", query = "SELECT t FROM TbVenda t WHERE t.descontoTotal = :descontoTotal"),
    @NamedQuery(name = "TbVenda.findByTotalIva", query = "SELECT t FROM TbVenda t WHERE t.totalIva = :totalIva"),
    @NamedQuery(name = "TbVenda.findByTotalGeral", query = "SELECT t FROM TbVenda t WHERE t.totalGeral = :totalGeral"),
    @NamedQuery(name = "TbVenda.findByCodFact", query = "SELECT t FROM TbVenda t WHERE t.codFact = :codFact"),
    @NamedQuery(name = "TbVenda.findByAssinatura", query = "SELECT t FROM TbVenda t WHERE t.assinatura = :assinatura"),
    @NamedQuery(name = "TbVenda.findByHashCod", query = "SELECT t FROM TbVenda t WHERE t.hashCod = :hashCod"),
    @NamedQuery(name = "TbVenda.findByObs", query = "SELECT t FROM TbVenda t WHERE t.obs = :obs"),
    @NamedQuery(name = "TbVenda.findByRefCodFact", query = "SELECT t FROM TbVenda t WHERE t.refCodFact = :refCodFact"),
    @NamedQuery(name = "TbVenda.findByTotalPorExtenso", query = "SELECT t FROM TbVenda t WHERE t.totalPorExtenso = :totalPorExtenso"),
    @NamedQuery(name = "TbVenda.findByStatusRecibo", query = "SELECT t FROM TbVenda t WHERE t.statusRecibo = :statusRecibo"),
    @NamedQuery(name = "TbVenda.findByDescontoComercial", query = "SELECT t FROM TbVenda t WHERE t.descontoComercial = :descontoComercial"),
    @NamedQuery(name = "TbVenda.findByDescontoFinanceiro", query = "SELECT t FROM TbVenda t WHERE t.descontoFinanceiro = :descontoFinanceiro"),
    @NamedQuery(name = "TbVenda.findByTotalIncidencia", query = "SELECT t FROM TbVenda t WHERE t.totalIncidencia = :totalIncidencia"),
    @NamedQuery(name = "TbVenda.findByLocalCarga", query = "SELECT t FROM TbVenda t WHERE t.localCarga = :localCarga"),
    @NamedQuery(name = "TbVenda.findByLocalDescarga", query = "SELECT t FROM TbVenda t WHERE t.localDescarga = :localDescarga"),
    @NamedQuery(name = "TbVenda.findByDataVencimento", query = "SELECT t FROM TbVenda t WHERE t.dataVencimento = :dataVencimento"),
    @NamedQuery(name = "TbVenda.findByClienteNif", query = "SELECT t FROM TbVenda t WHERE t.clienteNif = :clienteNif"),
    @NamedQuery(name = "TbVenda.findByTotalIncidenciaIsento", query = "SELECT t FROM TbVenda t WHERE t.totalIncidenciaIsento = :totalIncidenciaIsento"),
    @NamedQuery(name = "TbVenda.findByRefDataFact", query = "SELECT t FROM TbVenda t WHERE t.refDataFact = :refDataFact"),
    @NamedQuery(name = "TbVenda.findByCont", query = "SELECT t FROM TbVenda t WHERE t.cont = :cont"),
    @NamedQuery(name = "TbVenda.findByNomeConsumidorFinal", query = "SELECT t FROM TbVenda t WHERE t.nomeConsumidorFinal = :nomeConsumidorFinal"),
    @NamedQuery(name = "TbVenda.findByReferencia", query = "SELECT t FROM TbVenda t WHERE t.referencia = :referencia"),
    @NamedQuery(name = "TbVenda.findByMatricula", query = "SELECT t FROM TbVenda t WHERE t.matricula = :matricula"),
    @NamedQuery(name = "TbVenda.findByModelo", query = "SELECT t FROM TbVenda t WHERE t.modelo = :modelo"),
    @NamedQuery(name = "TbVenda.findByNumChassi", query = "SELECT t FROM TbVenda t WHERE t.numChassi = :numChassi"),
    @NamedQuery(name = "TbVenda.findByNumMotor", query = "SELECT t FROM TbVenda t WHERE t.numMotor = :numMotor"),
    @NamedQuery(name = "TbVenda.findByKilometro", query = "SELECT t FROM TbVenda t WHERE t.kilometro = :kilometro"),
    @NamedQuery(name = "TbVenda.findByNomeMotorista", query = "SELECT t FROM TbVenda t WHERE t.nomeMotorista = :nomeMotorista"),
    @NamedQuery(name = "TbVenda.findByMarcaCarro", query = "SELECT t FROM TbVenda t WHERE t.marcaCarro = :marcaCarro"),
    @NamedQuery(name = "TbVenda.findByCorCarro", query = "SELECT t FROM TbVenda t WHERE t.corCarro = :corCarro"),
    @NamedQuery(name = "TbVenda.findByNDocMotorista", query = "SELECT t FROM TbVenda t WHERE t.nDocMotorista = :nDocMotorista"),
    @NamedQuery(name = "TbVenda.findByTotalRetencao", query = "SELECT t FROM TbVenda t WHERE t.totalRetencao = :totalRetencao"),
    @NamedQuery(name = "TbVenda.findByGorjeta", query = "SELECT t FROM TbVenda t WHERE t.gorjeta = :gorjeta")
})
public class TbVenda implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "codigo")
    private Integer codigo;
    @Column(name = "dataVenda")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataVenda;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @Column(name = "total_venda")
    private BigDecimal totalVenda;
    @Basic(optional = false)
    @Column(name = "performance")
    private String performance;
    @Basic(optional = false)
    @Column(name = "credito")
    private String credito;
    @Basic(optional = false)
    @Column(name = "valor_entregue")
    private BigDecimal valorEntregue;
    @Basic(optional = false)
    @Column(name = "troco")
    private BigDecimal troco;
    @Basic(optional = false)
    @Column(name = "hora")
    @Temporal(TemporalType.TIME)
    private Date hora;
    @Basic(optional = false)
    @Column(name = "nome_cliente")
    private String nomeCliente;
    @Basic(optional = false)
    @Column(name = "status_eliminado")
    private String statusEliminado;
    @Column(name = "desconto_total")
    private BigDecimal descontoTotal;
    @Column(name = "total_iva")
    private BigDecimal totalIva;
    @Basic(optional = false)
    @Column(name = "total_geral")
    private BigDecimal totalGeral;
    @Column(name = "cod_fact")
    private String codFact;
    @Column(name = "assinatura")
    private String assinatura;
    @Column(name = "hash_cod")
    private String hashCod;
    @Column(name = "obs")
    private String obs;
    @Column(name = "ref_cod_fact")
    private String refCodFact;
    @Column(name = "total_por_extenso")
    private String totalPorExtenso;
    @Column(name = "status_recibo")
    private Boolean statusRecibo;
    @Column(name = "desconto_comercial")
    private BigDecimal descontoComercial;
    @Column(name = "desconto_financeiro")
    private BigDecimal descontoFinanceiro;
    @Column(name = "total_incidencia")
    private BigDecimal totalIncidencia;
    @Column(name = "local_carga")
    private String localCarga;
    @Column(name = "local_descarga")
    private String localDescarga;
    @Column(name = "dataVencimento")
    @Temporal(TemporalType.DATE)
    private Date dataVencimento;
    @Column(name = "cliente_nif")
    private String clienteNif;
    @Column(name = "total_incidencia_isento")
    private BigDecimal totalIncidenciaIsento;
    @Column(name = "ref_data_fact")
    @Temporal(TemporalType.TIMESTAMP)
    private Date refDataFact;
    @Column(name = "cont")
    private Integer cont;
    @Column(name = "nome_consumidor_final")
    private String nomeConsumidorFinal;
    @Column(name = "referencia")
    private String referencia;
    @Column(name = "matricula")
    private String matricula;
    @Column(name = "modelo")
    private String modelo;
    @Column(name = "num_chassi")
    private String numChassi;
    @Column(name = "num_motor")
    private String numMotor;
    @Column(name = "kilometro")
    private String kilometro;
    @Column(name = "nome_motorista")
    private String nomeMotorista;
    @Column(name = "marca_carro")
    private String marcaCarro;
    @Column(name = "cor_carro")
    private String corCarro;
    @Column(name = "n_doc_motorista")
    private String nDocMotorista;
    @Column(name = "total_retencao")
    private BigDecimal totalRetencao;
    @Column(name = "gorjeta")
    private BigDecimal gorjeta;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkVenda")
    private List<Amortizacao> amortizacaoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "codigoVenda")
    private List<TbPagamentoCredito> tbPagamentoCreditoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "codigoVenda")
    private List<TbItemVenda> tbItemVendaList;
    @OneToMany(mappedBy = "fkVenda")
    private List<FormaPagamentoItem> formaPagamentoItemList;
    @JoinColumn(name = "idBanco", referencedColumnName = "idBanco")
    @ManyToOne
    private TbBanco idBanco;
    @JoinColumn(name = "idArmazemFK", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private TbArmazem idArmazemFK;
    @JoinColumn(name = "fk_ano_economico", referencedColumnName = "pk_ano_economico")
    @ManyToOne(optional = false)
    private AnoEconomico fkAnoEconomico;
    @JoinColumn(name = "fk_cambio", referencedColumnName = "pk_cambio")
    @ManyToOne(optional = false)
    private Cambio fkCambio;
    @JoinColumn(name = "fk_documento", referencedColumnName = "pk_documento")
    @ManyToOne(optional = false)
    private Documento fkDocumento;
    @JoinColumn(name = "codigo_usuario", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private TbUsuario codigoUsuario;
    @JoinColumn(name = "codigo_cliente", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private TbCliente codigoCliente;
    @OneToMany(mappedBy = "fkVenda")
    private List<NotasItem> notasItemList;
    @OneToMany(mappedBy = "fkVenda")
    private List<AmortizacaoDivida> amortizacaoDividaList;

    public TbVenda()
    {
    }

    public TbVenda( Integer codigo )
    {
        this.codigo = codigo;
    }

    public TbVenda( Integer codigo, BigDecimal totalVenda, String performance, String credito, BigDecimal valorEntregue, BigDecimal troco, Date hora, String nomeCliente, String statusEliminado, BigDecimal totalGeral )
    {
        this.codigo = codigo;
        this.totalVenda = totalVenda;
        this.performance = performance;
        this.credito = credito;
        this.valorEntregue = valorEntregue;
        this.troco = troco;
        this.hora = hora;
        this.nomeCliente = nomeCliente;
        this.statusEliminado = statusEliminado;
        this.totalGeral = totalGeral;
    }

    public Integer getCodigo()
    {
        return codigo;
    }

    public void setCodigo( Integer codigo )
    {
        this.codigo = codigo;
    }

    public Date getDataVenda()
    {
        return dataVenda;
    }

    public void setDataVenda( Date dataVenda )
    {
        this.dataVenda = dataVenda;
    }

    public BigDecimal getTotalVenda()
    {
        return totalVenda;
    }

    public void setTotalVenda( BigDecimal totalVenda )
    {
        this.totalVenda = totalVenda;
    }

    public String getPerformance()
    {
        return performance;
    }

    public void setPerformance( String performance )
    {
        this.performance = performance;
    }

    public String getCredito()
    {
        return credito;
    }

    public void setCredito( String credito )
    {
        this.credito = credito;
    }

    public BigDecimal getValorEntregue()
    {
        return valorEntregue;
    }

    public void setValorEntregue( BigDecimal valorEntregue )
    {
        this.valorEntregue = valorEntregue;
    }

    public BigDecimal getTroco()
    {
        return troco;
    }

    public void setTroco( BigDecimal troco )
    {
        this.troco = troco;
    }

    public Date getHora()
    {
        return hora;
    }

    public void setHora( Date hora )
    {
        this.hora = hora;
    }

    public String getNomeCliente()
    {
        return nomeCliente;
    }

    public void setNomeCliente( String nomeCliente )
    {
        this.nomeCliente = nomeCliente;
    }

    public String getStatusEliminado()
    {
        return statusEliminado;
    }

    public void setStatusEliminado( String statusEliminado )
    {
        this.statusEliminado = statusEliminado;
    }

    public BigDecimal getDescontoTotal()
    {
        return descontoTotal;
    }

    public void setDescontoTotal( BigDecimal descontoTotal )
    {
        this.descontoTotal = descontoTotal;
    }

    public BigDecimal getTotalIva()
    {
        return totalIva;
    }

    public void setTotalIva( BigDecimal totalIva )
    {
        this.totalIva = totalIva;
    }

    public BigDecimal getTotalGeral()
    {
        return totalGeral;
    }

    public void setTotalGeral( BigDecimal totalGeral )
    {
        this.totalGeral = totalGeral;
    }

    public String getCodFact()
    {
        return codFact;
    }

    public void setCodFact( String codFact )
    {
        this.codFact = codFact;
    }

    public String getAssinatura()
    {
        return assinatura;
    }

    public void setAssinatura( String assinatura )
    {
        this.assinatura = assinatura;
    }

    public String getHashCod()
    {
        return hashCod;
    }

    public void setHashCod( String hashCod )
    {
        this.hashCod = hashCod;
    }

    public String getObs()
    {
        return obs;
    }

    public void setObs( String obs )
    {
        this.obs = obs;
    }

    public String getRefCodFact()
    {
        return refCodFact;
    }

    public void setRefCodFact( String refCodFact )
    {
        this.refCodFact = refCodFact;
    }

    public String getTotalPorExtenso()
    {
        return totalPorExtenso;
    }

    public void setTotalPorExtenso( String totalPorExtenso )
    {
        this.totalPorExtenso = totalPorExtenso;
    }

    public Boolean getStatusRecibo()
    {
        return statusRecibo;
    }

    public void setStatusRecibo( Boolean statusRecibo )
    {
        this.statusRecibo = statusRecibo;
    }

    public BigDecimal getDescontoComercial()
    {
        return descontoComercial;
    }

    public void setDescontoComercial( BigDecimal descontoComercial )
    {
        this.descontoComercial = descontoComercial;
    }

    public BigDecimal getDescontoFinanceiro()
    {
        return descontoFinanceiro;
    }

    public void setDescontoFinanceiro( BigDecimal descontoFinanceiro )
    {
        this.descontoFinanceiro = descontoFinanceiro;
    }

    public BigDecimal getTotalIncidencia()
    {
        return totalIncidencia;
    }

    public void setTotalIncidencia( BigDecimal totalIncidencia )
    {
        this.totalIncidencia = totalIncidencia;
    }

    public String getLocalCarga()
    {
        return localCarga;
    }

    public void setLocalCarga( String localCarga )
    {
        this.localCarga = localCarga;
    }

    public String getLocalDescarga()
    {
        return localDescarga;
    }

    public void setLocalDescarga( String localDescarga )
    {
        this.localDescarga = localDescarga;
    }

    public Date getDataVencimento()
    {
        return dataVencimento;
    }

    public void setDataVencimento( Date dataVencimento )
    {
        this.dataVencimento = dataVencimento;
    }

    public String getClienteNif()
    {
        return clienteNif;
    }

    public void setClienteNif( String clienteNif )
    {
        this.clienteNif = clienteNif;
    }

    public BigDecimal getTotalIncidenciaIsento()
    {
        return totalIncidenciaIsento;
    }

    public void setTotalIncidenciaIsento( BigDecimal totalIncidenciaIsento )
    {
        this.totalIncidenciaIsento = totalIncidenciaIsento;
    }

    public Date getRefDataFact()
    {
        return refDataFact;
    }

    public void setRefDataFact( Date refDataFact )
    {
        this.refDataFact = refDataFact;
    }

    public Integer getCont()
    {
        return cont;
    }

    public void setCont( Integer cont )
    {
        this.cont = cont;
    }

    public String getNomeConsumidorFinal()
    {
        return nomeConsumidorFinal;
    }

    public void setNomeConsumidorFinal( String nomeConsumidorFinal )
    {
        this.nomeConsumidorFinal = nomeConsumidorFinal;
    }

    public String getReferencia()
    {
        return referencia;
    }

    public void setReferencia( String referencia )
    {
        this.referencia = referencia;
    }

    public String getMatricula()
    {
        return matricula;
    }

    public void setMatricula( String matricula )
    {
        this.matricula = matricula;
    }

    public String getModelo()
    {
        return modelo;
    }

    public void setModelo( String modelo )
    {
        this.modelo = modelo;
    }

    public String getNumChassi()
    {
        return numChassi;
    }

    public void setNumChassi( String numChassi )
    {
        this.numChassi = numChassi;
    }

    public String getNumMotor()
    {
        return numMotor;
    }

    public void setNumMotor( String numMotor )
    {
        this.numMotor = numMotor;
    }

    public String getKilometro()
    {
        return kilometro;
    }

    public void setKilometro( String kilometro )
    {
        this.kilometro = kilometro;
    }

    public String getNomeMotorista()
    {
        return nomeMotorista;
    }

    public void setNomeMotorista( String nomeMotorista )
    {
        this.nomeMotorista = nomeMotorista;
    }

    public String getMarcaCarro()
    {
        return marcaCarro;
    }

    public void setMarcaCarro( String marcaCarro )
    {
        this.marcaCarro = marcaCarro;
    }

    public String getCorCarro()
    {
        return corCarro;
    }

    public void setCorCarro( String corCarro )
    {
        this.corCarro = corCarro;
    }

    public String getNDocMotorista()
    {
        return nDocMotorista;
    }

    public void setNDocMotorista( String nDocMotorista )
    {
        this.nDocMotorista = nDocMotorista;
    }

    public BigDecimal getTotalRetencao()
    {
        return totalRetencao;
    }

    public void setTotalRetencao( BigDecimal totalRetencao )
    {
        this.totalRetencao = totalRetencao;
    }

    public BigDecimal getGorjeta()
    {
        return gorjeta;
    }

    public void setGorjeta( BigDecimal gorjeta )
    {
        this.gorjeta = gorjeta;
    }

    @XmlTransient
    public List<Amortizacao> getAmortizacaoList()
    {
        return amortizacaoList;
    }

    public void setAmortizacaoList( List<Amortizacao> amortizacaoList )
    {
        this.amortizacaoList = amortizacaoList;
    }

    @XmlTransient
    public List<TbPagamentoCredito> getTbPagamentoCreditoList()
    {
        return tbPagamentoCreditoList;
    }

    public void setTbPagamentoCreditoList( List<TbPagamentoCredito> tbPagamentoCreditoList )
    {
        this.tbPagamentoCreditoList = tbPagamentoCreditoList;
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
    public List<FormaPagamentoItem> getFormaPagamentoItemList()
    {
        return formaPagamentoItemList;
    }

    public void setFormaPagamentoItemList( List<FormaPagamentoItem> formaPagamentoItemList )
    {
        this.formaPagamentoItemList = formaPagamentoItemList;
    }

    public TbBanco getIdBanco()
    {
        return idBanco;
    }

    public void setIdBanco( TbBanco idBanco )
    {
        this.idBanco = idBanco;
    }

    public TbArmazem getIdArmazemFK()
    {
        return idArmazemFK;
    }

    public void setIdArmazemFK( TbArmazem idArmazemFK )
    {
        this.idArmazemFK = idArmazemFK;
    }

    public AnoEconomico getFkAnoEconomico()
    {
        return fkAnoEconomico;
    }

    public void setFkAnoEconomico( AnoEconomico fkAnoEconomico )
    {
        this.fkAnoEconomico = fkAnoEconomico;
    }

    public Cambio getFkCambio()
    {
        return fkCambio;
    }

    public void setFkCambio( Cambio fkCambio )
    {
        this.fkCambio = fkCambio;
    }

    public Documento getFkDocumento()
    {
        return fkDocumento;
    }

    public void setFkDocumento( Documento fkDocumento )
    {
        this.fkDocumento = fkDocumento;
    }

    public TbUsuario getCodigoUsuario()
    {
        return codigoUsuario;
    }

    public void setCodigoUsuario( TbUsuario codigoUsuario )
    {
        this.codigoUsuario = codigoUsuario;
    }

    public TbCliente getCodigoCliente()
    {
        return codigoCliente;
    }

    public void setCodigoCliente( TbCliente codigoCliente )
    {
        this.codigoCliente = codigoCliente;
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
    public List<AmortizacaoDivida> getAmortizacaoDividaList()
    {
        return amortizacaoDividaList;
    }

    public void setAmortizacaoDividaList( List<AmortizacaoDivida> amortizacaoDividaList )
    {
        this.amortizacaoDividaList = amortizacaoDividaList;
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
        if ( !( object instanceof TbVenda ) )
        {
            return false;
        }
        TbVenda other = ( TbVenda ) object;
        if ( ( this.codigo == null && other.codigo != null ) || ( this.codigo != null && !this.codigo.equals( other.codigo ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.TbVenda[ codigo=" + codigo + " ]";
    }
    
}
