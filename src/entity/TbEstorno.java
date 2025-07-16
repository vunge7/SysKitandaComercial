/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

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
@Table(name = "tb_estorno")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "TbEstorno.findAll", query = "SELECT t FROM TbEstorno t"),
    @NamedQuery(name = "TbEstorno.findByPkEstorno", query = "SELECT t FROM TbEstorno t WHERE t.pkEstorno = :pkEstorno"),
    @NamedQuery(name = "TbEstorno.findByDataEstorno", query = "SELECT t FROM TbEstorno t WHERE t.dataEstorno = :dataEstorno"),
    @NamedQuery(name = "TbEstorno.findByHoraEstorno", query = "SELECT t FROM TbEstorno t WHERE t.horaEstorno = :horaEstorno"),
    @NamedQuery(name = "TbEstorno.findByStatusEliminado", query = "SELECT t FROM TbEstorno t WHERE t.statusEliminado = :statusEliminado"),
    @NamedQuery(name = "TbEstorno.findByObs", query = "SELECT t FROM TbEstorno t WHERE t.obs = :obs")
})
public class TbEstorno implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pk_estorno")
    private Integer pkEstorno;
    @Basic(optional = false)
    @Column(name = "data_estorno")
    @Temporal(TemporalType.DATE)
    private Date dataEstorno;
    @Basic(optional = false)
    @Column(name = "hora_estorno")
    @Temporal(TemporalType.TIME)
    private Date horaEstorno;
    @Basic(optional = false)
    @Column(name = "status_eliminado")
    private String statusEliminado;
    @Basic(optional = false)
    @Column(name = "obs")
    private String obs;
    @JoinColumn(name = "idArmazemFK", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private TbArmazem idArmazemFK;
    @JoinColumn(name = "fk_usuario", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private TbUsuario fkUsuario;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkEstorno")
    private List<TbItemEstorno> tbItemEstornoList;

    public TbEstorno()
    {
    }

    public TbEstorno( Integer pkEstorno )
    {
        this.pkEstorno = pkEstorno;
    }

    public TbEstorno( Integer pkEstorno, Date dataEstorno, Date horaEstorno, String statusEliminado, String obs )
    {
        this.pkEstorno = pkEstorno;
        this.dataEstorno = dataEstorno;
        this.horaEstorno = horaEstorno;
        this.statusEliminado = statusEliminado;
        this.obs = obs;
    }

    public Integer getPkEstorno()
    {
        return pkEstorno;
    }

    public void setPkEstorno( Integer pkEstorno )
    {
        this.pkEstorno = pkEstorno;
    }

    public Date getDataEstorno()
    {
        return dataEstorno;
    }

    public void setDataEstorno( Date dataEstorno )
    {
        this.dataEstorno = dataEstorno;
    }

    public Date getHoraEstorno()
    {
        return horaEstorno;
    }

    public void setHoraEstorno( Date horaEstorno )
    {
        this.horaEstorno = horaEstorno;
    }

    public String getStatusEliminado()
    {
        return statusEliminado;
    }

    public void setStatusEliminado( String statusEliminado )
    {
        this.statusEliminado = statusEliminado;
    }

    public String getObs()
    {
        return obs;
    }

    public void setObs( String obs )
    {
        this.obs = obs;
    }

    public TbArmazem getIdArmazemFK()
    {
        return idArmazemFK;
    }

    public void setIdArmazemFK( TbArmazem idArmazemFK )
    {
        this.idArmazemFK = idArmazemFK;
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
    public List<TbItemEstorno> getTbItemEstornoList()
    {
        return tbItemEstornoList;
    }

    public void setTbItemEstornoList( List<TbItemEstorno> tbItemEstornoList )
    {
        this.tbItemEstornoList = tbItemEstornoList;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += ( pkEstorno != null ? pkEstorno.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if ( !( object instanceof TbEstorno ) )
        {
            return false;
        }
        TbEstorno other = ( TbEstorno ) object;
        if ( ( this.pkEstorno == null && other.pkEstorno != null ) || ( this.pkEstorno != null && !this.pkEstorno.equals( other.pkEstorno ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.TbEstorno[ pkEstorno=" + pkEstorno + " ]";
    }
    
}
