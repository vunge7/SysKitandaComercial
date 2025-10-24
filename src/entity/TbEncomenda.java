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
@Table(name = "tb_encomenda")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "TbEncomenda.findAll", query = "SELECT t FROM TbEncomenda t"),
    @NamedQuery(name = "TbEncomenda.findByIdEncomenda", query = "SELECT t FROM TbEncomenda t WHERE t.idEncomenda = :idEncomenda"),
    @NamedQuery(name = "TbEncomenda.findByDataEncomenda", query = "SELECT t FROM TbEncomenda t WHERE t.dataEncomenda = :dataEncomenda"),
    @NamedQuery(name = "TbEncomenda.findByDataEntregaPrevista", query = "SELECT t FROM TbEncomenda t WHERE t.dataEntregaPrevista = :dataEntregaPrevista"),
    @NamedQuery(name = "TbEncomenda.findByTotalEncomenda", query = "SELECT t FROM TbEncomenda t WHERE t.totalEncomenda = :totalEncomenda"),
    @NamedQuery(name = "TbEncomenda.findByStatusEntrega", query = "SELECT t FROM TbEncomenda t WHERE t.statusEntrega = :statusEntrega"),
    @NamedQuery(name = "TbEncomenda.findByObs", query = "SELECT t FROM TbEncomenda t WHERE t.obs = :obs")
})
public class TbEncomenda implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idEncomenda")
    private Long idEncomenda;
    @Basic(optional = false)
    @Column(name = "data_encomenda")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataEncomenda;
    @Basic(optional = false)
    @Column(name = "data_entrega_prevista")
    @Temporal(TemporalType.DATE)
    private Date dataEntregaPrevista;
    @Basic(optional = false)
    @Column(name = "total_encomenda")
    private String totalEncomenda;
    @Column(name = "status_entrega")
    private Boolean statusEntrega;
    @Basic(optional = false)
    @Column(name = "obs")
    private String obs;
    @JoinColumn(name = "idCliente", referencedColumnName = "idCliente")
    @ManyToOne(optional = false)
    private TbClientesEncomenda idCliente;
    @JoinColumn(name = "idUsuario", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private TbUsuario idUsuario;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idEncomenda")
    private List<TbItemEncomenda> tbItemEncomendaList;

    public TbEncomenda()
    {
    }

    public TbEncomenda( Long idEncomenda )
    {
        this.idEncomenda = idEncomenda;
    }

    public TbEncomenda( Long idEncomenda, Date dataEncomenda, Date dataEntregaPrevista, String totalEncomenda, String obs )
    {
        this.idEncomenda = idEncomenda;
        this.dataEncomenda = dataEncomenda;
        this.dataEntregaPrevista = dataEntregaPrevista;
        this.totalEncomenda = totalEncomenda;
        this.obs = obs;
    }

    public Long getIdEncomenda()
    {
        return idEncomenda;
    }

    public void setIdEncomenda( Long idEncomenda )
    {
        this.idEncomenda = idEncomenda;
    }

    public Date getDataEncomenda()
    {
        return dataEncomenda;
    }

    public void setDataEncomenda( Date dataEncomenda )
    {
        this.dataEncomenda = dataEncomenda;
    }

    public Date getDataEntregaPrevista()
    {
        return dataEntregaPrevista;
    }

    public void setDataEntregaPrevista( Date dataEntregaPrevista )
    {
        this.dataEntregaPrevista = dataEntregaPrevista;
    }

    public String getTotalEncomenda()
    {
        return totalEncomenda;
    }

    public void setTotalEncomenda( String totalEncomenda )
    {
        this.totalEncomenda = totalEncomenda;
    }

    public Boolean getStatusEntrega()
    {
        return statusEntrega;
    }

    public void setStatusEntrega( Boolean statusEntrega )
    {
        this.statusEntrega = statusEntrega;
    }

    public String getObs()
    {
        return obs;
    }

    public void setObs( String obs )
    {
        this.obs = obs;
    }

    public TbClientesEncomenda getIdCliente()
    {
        return idCliente;
    }

    public void setIdCliente( TbClientesEncomenda idCliente )
    {
        this.idCliente = idCliente;
    }

    public TbUsuario getIdUsuario()
    {
        return idUsuario;
    }

    public void setIdUsuario( TbUsuario idUsuario )
    {
        this.idUsuario = idUsuario;
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

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += ( idEncomenda != null ? idEncomenda.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if ( !( object instanceof TbEncomenda ) )
        {
            return false;
        }
        TbEncomenda other = ( TbEncomenda ) object;
        if ( ( this.idEncomenda == null && other.idEncomenda != null ) || ( this.idEncomenda != null && !this.idEncomenda.equals( other.idEncomenda ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.TbEncomenda[ idEncomenda=" + idEncomenda + " ]";
    }
    
}
