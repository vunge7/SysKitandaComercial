/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;


import java.sql.Connection;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
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
@Table(name = "notas_compras")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "NotasCompras.findAll", query = "SELECT n FROM NotasCompras n"),
    @NamedQuery(name = "NotasCompras.findByPkNotaCompras", query = "SELECT n FROM NotasCompras n WHERE n.pkNotaCompras = :pkNotaCompras"),
    @NamedQuery(name = "NotasCompras.findByCodNota", query = "SELECT n FROM NotasCompras n WHERE n.codNota = :codNota"),
    @NamedQuery(name = "NotasCompras.findByRefCodNota", query = "SELECT n FROM NotasCompras n WHERE n.refCodNota = :refCodNota"),
    @NamedQuery(name = "NotasCompras.findByDataNota", query = "SELECT n FROM NotasCompras n WHERE n.dataNota = :dataNota"),
    @NamedQuery(name = "NotasCompras.findByTotalCompra", query = "SELECT n FROM NotasCompras n WHERE n.totalCompra = :totalCompra"),
    @NamedQuery(name = "NotasCompras.findByPerformance", query = "SELECT n FROM NotasCompras n WHERE n.performance = :performance"),
    @NamedQuery(name = "NotasCompras.findByCredito", query = "SELECT n FROM NotasCompras n WHERE n.credito = :credito"),
    @NamedQuery(name = "NotasCompras.findByHora", query = "SELECT n FROM NotasCompras n WHERE n.hora = :hora"),
    @NamedQuery(name = "NotasCompras.findByStatusEliminado", query = "SELECT n FROM NotasCompras n WHERE n.statusEliminado = :statusEliminado"),
    @NamedQuery(name = "NotasCompras.findByDescontoTotal", query = "SELECT n FROM NotasCompras n WHERE n.descontoTotal = :descontoTotal"),
    @NamedQuery(name = "NotasCompras.findByTotalIva", query = "SELECT n FROM NotasCompras n WHERE n.totalIva = :totalIva"),
    @NamedQuery(name = "NotasCompras.findByTotalGeral", query = "SELECT n FROM NotasCompras n WHERE n.totalGeral = :totalGeral"),
    @NamedQuery(name = "NotasCompras.findByCodFact", query = "SELECT n FROM NotasCompras n WHERE n.codFact = :codFact"),
    @NamedQuery(name = "NotasCompras.findByAssinatura", query = "SELECT n FROM NotasCompras n WHERE n.assinatura = :assinatura"),
    @NamedQuery(name = "NotasCompras.findByHashCod", query = "SELECT n FROM NotasCompras n WHERE n.hashCod = :hashCod"),
    @NamedQuery(name = "NotasCompras.findByObs", query = "SELECT n FROM NotasCompras n WHERE n.obs = :obs"),
    @NamedQuery(name = "NotasCompras.findByRefCodFact", query = "SELECT n FROM NotasCompras n WHERE n.refCodFact = :refCodFact"),
    @NamedQuery(name = "NotasCompras.findByTotalPorExtenso", query = "SELECT n FROM NotasCompras n WHERE n.totalPorExtenso = :totalPorExtenso"),
    @NamedQuery(name = "NotasCompras.findByRefFactData", query = "SELECT n FROM NotasCompras n WHERE n.refFactData = :refFactData"),
    @NamedQuery(name = "NotasCompras.findByEstado", query = "SELECT n FROM NotasCompras n WHERE n.estado = :estado"),
    @NamedQuery(name = "NotasCompras.findByDescontoComercial", query = "SELECT n FROM NotasCompras n WHERE n.descontoComercial = :descontoComercial"),
    @NamedQuery(name = "NotasCompras.findByDescontoFinanceiro", query = "SELECT n FROM NotasCompras n WHERE n.descontoFinanceiro = :descontoFinanceiro"),
    @NamedQuery(name = "NotasCompras.findByMotivo", query = "SELECT n FROM NotasCompras n WHERE n.motivo = :motivo"),
    @NamedQuery(name = "NotasCompras.findByTotalIncidencia", query = "SELECT n FROM NotasCompras n WHERE n.totalIncidencia = :totalIncidencia"),
    @NamedQuery(name = "NotasCompras.findByTotalIncidenciaIsento", query = "SELECT n FROM NotasCompras n WHERE n.totalIncidenciaIsento = :totalIncidenciaIsento"),
    @NamedQuery(name = "NotasCompras.findByRefDataFact", query = "SELECT n FROM NotasCompras n WHERE n.refDataFact = :refDataFact")
})
public class NotasCompras implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pk_nota_compras")
    private Integer pkNotaCompras;
    @Column(name = "cod_nota")
    private String codNota;
    @Column(name = "ref_cod_nota")
    private String refCodNota;
    @Column(name = "dataNota")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataNota;
    @Basic(optional = false)
    @Column(name = "total_compra")
    private float totalCompra;
    @Basic(optional = false)
    @Column(name = "performance")
    private String performance;
    @Basic(optional = false)
    @Column(name = "credito")
    private String credito;
    @Basic(optional = false)
    @Column(name = "hora")
    @Temporal(TemporalType.TIME)
    private Date hora;
    @Basic(optional = false)
    @Column(name = "status_eliminado")
    private String statusEliminado;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "desconto_total")
    private Double descontoTotal;
    @Column(name = "total_iva")
    private Double totalIva;
    @Column(name = "total_geral")
    private Double totalGeral;
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
    @Column(name = "desconto_financeiro")
    private Double descontoFinanceiro;
    @Column(name = "motivo")
    private String motivo;
    @Column(name = "total_incidencia")
    private Double totalIncidencia;
    @Column(name = "total_incidencia_isento")
    private Double totalIncidenciaIsento;
    @Column(name = "ref_data_fact")
    private String refDataFact;
    @JoinColumn(name = "fk_ano_economico", referencedColumnName = "pk_ano_economico")
    @ManyToOne(optional = false)
    private AnoEconomico fkAnoEconomico;
    @JoinColumn(name = "idArmazemFK", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private TbArmazem idArmazemFK;
    @JoinColumn(name = "fk_documento", referencedColumnName = "pk_documento")
    @ManyToOne
    private Documento fkDocumento;
    @JoinColumn(name = "codigo_usuario", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private TbUsuario codigoUsuario;
    @JoinColumn(name = "idBanco", referencedColumnName = "idBanco")
    @ManyToOne
    private TbBanco idBanco;
    @JoinColumn(name = "fk_fornecedor", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private TbFornecedor fkFornecedor;
    @OneToMany(mappedBy = "fkNotaCompras")
    private List<NotasItemCompras> notasItemComprasList;

    public NotasCompras()
    {
    }

    public NotasCompras( Integer pkNotaCompras )
    {
        this.pkNotaCompras = pkNotaCompras;
    }

    public NotasCompras( Integer pkNotaCompras, float totalCompra, String performance, String credito, Date hora, String statusEliminado )
    {
        this.pkNotaCompras = pkNotaCompras;
        this.totalCompra = totalCompra;
        this.performance = performance;
        this.credito = credito;
        this.hora = hora;
        this.statusEliminado = statusEliminado;
    }

    public Integer getPkNotaCompras()
    {
        return pkNotaCompras;
    }

    public void setPkNotaCompras( Integer pkNotaCompras )
    {
        this.pkNotaCompras = pkNotaCompras;
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

    public float getTotalCompra()
    {
        return totalCompra;
    }

    public void setTotalCompra( float totalCompra )
    {
        this.totalCompra = totalCompra;
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

    public Date getHora()
    {
        return hora;
    }

    public void setHora( Date hora )
    {
        this.hora = hora;
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

    public Double getTotalGeral()
    {
        return totalGeral;
    }

    public void setTotalGeral( Double totalGeral )
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

    public Double getDescontoFinanceiro()
    {
        return descontoFinanceiro;
    }

    public void setDescontoFinanceiro( Double descontoFinanceiro )
    {
        this.descontoFinanceiro = descontoFinanceiro;
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

    public AnoEconomico getFkAnoEconomico()
    {
        return fkAnoEconomico;
    }

    public void setFkAnoEconomico( AnoEconomico fkAnoEconomico )
    {
        this.fkAnoEconomico = fkAnoEconomico;
    }

    public TbArmazem getIdArmazemFK()
    {
        return idArmazemFK;
    }

    public void setIdArmazemFK( TbArmazem idArmazemFK )
    {
        this.idArmazemFK = idArmazemFK;
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

    public TbBanco getIdBanco()
    {
        return idBanco;
    }

    public void setIdBanco( TbBanco idBanco )
    {
        this.idBanco = idBanco;
    }

    public TbFornecedor getFkFornecedor()
    {
        return fkFornecedor;
    }

    public void setFkFornecedor( TbFornecedor fkFornecedor )
    {
        this.fkFornecedor = fkFornecedor;
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
        hash += ( pkNotaCompras != null ? pkNotaCompras.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if ( !( object instanceof NotasCompras ) )
        {
            return false;
        }
        NotasCompras other = ( NotasCompras ) object;
        if ( ( this.pkNotaCompras == null && other.pkNotaCompras != null ) || ( this.pkNotaCompras != null && !this.pkNotaCompras.equals( other.pkNotaCompras ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.NotasCompras[ pkNotaCompras=" + pkNotaCompras + " ]";
    }
    
}
