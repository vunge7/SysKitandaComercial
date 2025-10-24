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
@Table(name = "notas")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "Notas.findAll", query = "SELECT n FROM Notas n"),
    @NamedQuery(name = "Notas.findByPkNota", query = "SELECT n FROM Notas n WHERE n.pkNota = :pkNota"),
    @NamedQuery(name = "Notas.findByTipoNota", query = "SELECT n FROM Notas n WHERE n.tipoNota = :tipoNota"),
    @NamedQuery(name = "Notas.findByCodNota", query = "SELECT n FROM Notas n WHERE n.codNota = :codNota"),
    @NamedQuery(name = "Notas.findByRefCodNota", query = "SELECT n FROM Notas n WHERE n.refCodNota = :refCodNota"),
    @NamedQuery(name = "Notas.findByDataNota", query = "SELECT n FROM Notas n WHERE n.dataNota = :dataNota"),
    @NamedQuery(name = "Notas.findByTotalVenda", query = "SELECT n FROM Notas n WHERE n.totalVenda = :totalVenda"),
    @NamedQuery(name = "Notas.findByPerformance", query = "SELECT n FROM Notas n WHERE n.performance = :performance"),
    @NamedQuery(name = "Notas.findByCredito", query = "SELECT n FROM Notas n WHERE n.credito = :credito"),
    @NamedQuery(name = "Notas.findByValorEntregue", query = "SELECT n FROM Notas n WHERE n.valorEntregue = :valorEntregue"),
    @NamedQuery(name = "Notas.findByTroco", query = "SELECT n FROM Notas n WHERE n.troco = :troco"),
    @NamedQuery(name = "Notas.findByHora", query = "SELECT n FROM Notas n WHERE n.hora = :hora"),
    @NamedQuery(name = "Notas.findByNomeCliente", query = "SELECT n FROM Notas n WHERE n.nomeCliente = :nomeCliente"),
    @NamedQuery(name = "Notas.findByStatusEliminado", query = "SELECT n FROM Notas n WHERE n.statusEliminado = :statusEliminado"),
    @NamedQuery(name = "Notas.findByDescontoTotal", query = "SELECT n FROM Notas n WHERE n.descontoTotal = :descontoTotal"),
    @NamedQuery(name = "Notas.findByTotalIva", query = "SELECT n FROM Notas n WHERE n.totalIva = :totalIva"),
    @NamedQuery(name = "Notas.findByTotalGeral", query = "SELECT n FROM Notas n WHERE n.totalGeral = :totalGeral"),
    @NamedQuery(name = "Notas.findByCodFact", query = "SELECT n FROM Notas n WHERE n.codFact = :codFact"),
    @NamedQuery(name = "Notas.findByAssinatura", query = "SELECT n FROM Notas n WHERE n.assinatura = :assinatura"),
    @NamedQuery(name = "Notas.findByHashCod", query = "SELECT n FROM Notas n WHERE n.hashCod = :hashCod"),
    @NamedQuery(name = "Notas.findByObs", query = "SELECT n FROM Notas n WHERE n.obs = :obs"),
    @NamedQuery(name = "Notas.findByRefCodFact", query = "SELECT n FROM Notas n WHERE n.refCodFact = :refCodFact"),
    @NamedQuery(name = "Notas.findByTotalPorExtenso", query = "SELECT n FROM Notas n WHERE n.totalPorExtenso = :totalPorExtenso"),
    @NamedQuery(name = "Notas.findByRefFactData", query = "SELECT n FROM Notas n WHERE n.refFactData = :refFactData"),
    @NamedQuery(name = "Notas.findByEstado", query = "SELECT n FROM Notas n WHERE n.estado = :estado"),
    @NamedQuery(name = "Notas.findByDescontoComercial", query = "SELECT n FROM Notas n WHERE n.descontoComercial = :descontoComercial"),
    @NamedQuery(name = "Notas.findByDescontoFinaceiro", query = "SELECT n FROM Notas n WHERE n.descontoFinaceiro = :descontoFinaceiro"),
    @NamedQuery(name = "Notas.findByLocalCarga", query = "SELECT n FROM Notas n WHERE n.localCarga = :localCarga"),
    @NamedQuery(name = "Notas.findByLocalDescarga", query = "SELECT n FROM Notas n WHERE n.localDescarga = :localDescarga"),
    @NamedQuery(name = "Notas.findByMotivo", query = "SELECT n FROM Notas n WHERE n.motivo = :motivo"),
    @NamedQuery(name = "Notas.findByTotalIncidencia", query = "SELECT n FROM Notas n WHERE n.totalIncidencia = :totalIncidencia"),
    @NamedQuery(name = "Notas.findByClienteNif", query = "SELECT n FROM Notas n WHERE n.clienteNif = :clienteNif"),
    @NamedQuery(name = "Notas.findByTotalIncidenciaIsento", query = "SELECT n FROM Notas n WHERE n.totalIncidenciaIsento = :totalIncidenciaIsento"),
    @NamedQuery(name = "Notas.findByRefDataFact", query = "SELECT n FROM Notas n WHERE n.refDataFact = :refDataFact"),
    @NamedQuery(name = "Notas.findByTotalNota", query = "SELECT n FROM Notas n WHERE n.totalNota = :totalNota")
})
public class Notas implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pk_nota")
    private Integer pkNota;
    @Column(name = "tipo_nota")
    private String tipoNota;
    @Column(name = "cod_nota")
    private String codNota;
    @Column(name = "ref_cod_nota")
    private String refCodNota;
    @Column(name = "dataNota")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataNota;
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
    private double valorEntregue;
    @Basic(optional = false)
    @Column(name = "troco")
    private double troco;
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
    private Double descontoTotal;
    @Column(name = "total_iva")
    private Double totalIva;
    @Basic(optional = false)
    @Column(name = "total_geral")
    private double totalGeral;
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
    @Column(name = "ref_fact_data")
    @Temporal(TemporalType.TIMESTAMP)
    private Date refFactData;
    @Column(name = "estado")
    private String estado;
    @Column(name = "desconto_comercial")
    private Double descontoComercial;
    @Column(name = "desconto_finaceiro")
    private Double descontoFinaceiro;
    @Column(name = "local_carga")
    private String localCarga;
    @Column(name = "local_descarga")
    private String localDescarga;
    @Column(name = "motivo")
    private String motivo;
    @Column(name = "total_incidencia")
    private Double totalIncidencia;
    @Column(name = "cliente_nif")
    private String clienteNif;
    @Column(name = "total_incidencia_isento")
    private Double totalIncidenciaIsento;
    @Column(name = "ref_data_fact")
    private String refDataFact;
    @Column(name = "total_nota")
    private BigDecimal totalNota;
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
    @JoinColumn(name = "codigo_cliente", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private TbCliente codigoCliente;
    @JoinColumn(name = "codigo_usuario", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private TbUsuario codigoUsuario;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkNota")
    private List<ItensNota> itensNotaList;
    @OneToMany(mappedBy = "fkNota")
    private List<NotasItem> notasItemList;

    public Notas()
    {
    }

    public Notas( Integer pkNota )
    {
        this.pkNota = pkNota;
    }

    public Notas( Integer pkNota, BigDecimal totalVenda, String performance, String credito, double valorEntregue, double troco, Date hora, String nomeCliente, String statusEliminado, double totalGeral )
    {
        this.pkNota = pkNota;
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

    public Integer getPkNota()
    {
        return pkNota;
    }

    public void setPkNota( Integer pkNota )
    {
        this.pkNota = pkNota;
    }

    public String getTipoNota()
    {
        return tipoNota;
    }

    public void setTipoNota( String tipoNota )
    {
        this.tipoNota = tipoNota;
    }

    public String getCodNota()
    {
        return codNota;
    }

    public void setCodNota( String codNota )
    {
        this.codNota = codNota;
    }

    public String getRefCodNota()
    {
        return refCodNota;
    }

    public void setRefCodNota( String refCodNota )
    {
        this.refCodNota = refCodNota;
    }

    public Date getDataNota()
    {
        return dataNota;
    }

    public void setDataNota( Date dataNota )
    {
        this.dataNota = dataNota;
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

    public double getValorEntregue()
    {
        return valorEntregue;
    }

    public void setValorEntregue( double valorEntregue )
    {
        this.valorEntregue = valorEntregue;
    }

    public double getTroco()
    {
        return troco;
    }

    public void setTroco( double troco )
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

    public Double getDescontoTotal()
    {
        return descontoTotal;
    }

    public void setDescontoTotal( Double descontoTotal )
    {
        this.descontoTotal = descontoTotal;
    }

    public Double getTotalIva()
    {
        return totalIva;
    }

    public void setTotalIva( Double totalIva )
    {
        this.totalIva = totalIva;
    }

    public double getTotalGeral()
    {
        return totalGeral;
    }

    public void setTotalGeral( double totalGeral )
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

    public Date getRefFactData()
    {
        return refFactData;
    }

    public void setRefFactData( Date refFactData )
    {
        this.refFactData = refFactData;
    }

    public String getEstado()
    {
        return estado;
    }

    public void setEstado( String estado )
    {
        this.estado = estado;
    }

    public Double getDescontoComercial()
    {
        return descontoComercial;
    }

    public void setDescontoComercial( Double descontoComercial )
    {
        this.descontoComercial = descontoComercial;
    }

    public Double getDescontoFinaceiro()
    {
        return descontoFinaceiro;
    }

    public void setDescontoFinaceiro( Double descontoFinaceiro )
    {
        this.descontoFinaceiro = descontoFinaceiro;
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

    public String getMotivo()
    {
        return motivo;
    }

    public void setMotivo( String motivo )
    {
        this.motivo = motivo;
    }

    public Double getTotalIncidencia()
    {
        return totalIncidencia;
    }

    public void setTotalIncidencia( Double totalIncidencia )
    {
        this.totalIncidencia = totalIncidencia;
    }

    public String getClienteNif()
    {
        return clienteNif;
    }

    public void setClienteNif( String clienteNif )
    {
        this.clienteNif = clienteNif;
    }

    public Double getTotalIncidenciaIsento()
    {
        return totalIncidenciaIsento;
    }

    public void setTotalIncidenciaIsento( Double totalIncidenciaIsento )
    {
        this.totalIncidenciaIsento = totalIncidenciaIsento;
    }

    public String getRefDataFact()
    {
        return refDataFact;
    }

    public void setRefDataFact( String refDataFact )
    {
        this.refDataFact = refDataFact;
    }

    public BigDecimal getTotalNota()
    {
        return totalNota;
    }

    public void setTotalNota( BigDecimal totalNota )
    {
        this.totalNota = totalNota;
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

    public TbCliente getCodigoCliente()
    {
        return codigoCliente;
    }

    public void setCodigoCliente( TbCliente codigoCliente )
    {
        this.codigoCliente = codigoCliente;
    }

    public TbUsuario getCodigoUsuario()
    {
        return codigoUsuario;
    }

    public void setCodigoUsuario( TbUsuario codigoUsuario )
    {
        this.codigoUsuario = codigoUsuario;
    }

    @XmlTransient
    public List<ItensNota> getItensNotaList()
    {
        return itensNotaList;
    }

    public void setItensNotaList( List<ItensNota> itensNotaList )
    {
        this.itensNotaList = itensNotaList;
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
        hash += ( pkNota != null ? pkNota.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if ( !( object instanceof Notas ) )
        {
            return false;
        }
        Notas other = ( Notas ) object;
        if ( ( this.pkNota == null && other.pkNota != null ) || ( this.pkNota != null && !this.pkNota.equals( other.pkNota ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.Notas[ pkNota=" + pkNota + " ]";
    }
    
}
