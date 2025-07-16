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
@Table(name = "tb_pedido")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "TbPedido.findAll", query = "SELECT t FROM TbPedido t"),
    @NamedQuery(name = "TbPedido.findByPkPedido", query = "SELECT t FROM TbPedido t WHERE t.pkPedido = :pkPedido"),
    @NamedQuery(name = "TbPedido.findByDataPedido", query = "SELECT t FROM TbPedido t WHERE t.dataPedido = :dataPedido"),
    @NamedQuery(name = "TbPedido.findByHoraPedido", query = "SELECT t FROM TbPedido t WHERE t.horaPedido = :horaPedido"),
    @NamedQuery(name = "TbPedido.findByStatusPedido", query = "SELECT t FROM TbPedido t WHERE t.statusPedido = :statusPedido"),
    @NamedQuery(name = "TbPedido.findByFacturado", query = "SELECT t FROM TbPedido t WHERE t.facturado = :facturado"),
    @NamedQuery(name = "TbPedido.findByDeposito", query = "SELECT t FROM TbPedido t WHERE t.deposito = :deposito"),
    @NamedQuery(name = "TbPedido.findByValorEmFalta", query = "SELECT t FROM TbPedido t WHERE t.valorEmFalta = :valorEmFalta")
})
public class TbPedido implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pk_pedido")
    private Integer pkPedido;
    @Basic(optional = false)
    @Column(name = "data_pedido")
    @Temporal(TemporalType.DATE)
    private Date dataPedido;
    @Basic(optional = false)
    @Column(name = "hora_pedido")
    @Temporal(TemporalType.TIME)
    private Date horaPedido;
    @Basic(optional = false)
    @Column(name = "status_pedido")
    private boolean statusPedido;
    @Column(name = "facturado")
    private String facturado;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "deposito")
    private Double deposito;
    @Column(name = "valor_em_falta")
    private Double valorEmFalta;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkPedidos")
    private List<TbItemPedidos> tbItemPedidosList;
    @JoinColumn(name = "fk_mesas", referencedColumnName = "pk_mesas")
    @ManyToOne
    private TbMesas fkMesas;

    public TbPedido()
    {
    }

    public TbPedido( Integer pkPedido )
    {
        this.pkPedido = pkPedido;
    }

    public TbPedido( Integer pkPedido, Date dataPedido, Date horaPedido, boolean statusPedido )
    {
        this.pkPedido = pkPedido;
        this.dataPedido = dataPedido;
        this.horaPedido = horaPedido;
        this.statusPedido = statusPedido;
    }

    public Integer getPkPedido()
    {
        return pkPedido;
    }

    public void setPkPedido( Integer pkPedido )
    {
        this.pkPedido = pkPedido;
    }

    public Date getDataPedido()
    {
        return dataPedido;
    }

    public void setDataPedido( Date dataPedido )
    {
        this.dataPedido = dataPedido;
    }

    public Date getHoraPedido()
    {
        return horaPedido;
    }

    public void setHoraPedido( Date horaPedido )
    {
        this.horaPedido = horaPedido;
    }

    public boolean getStatusPedido()
    {
        return statusPedido;
    }

    public void setStatusPedido( boolean statusPedido )
    {
        this.statusPedido = statusPedido;
    }

    public String getFacturado()
    {
        return facturado;
    }

    public void setFacturado( String facturado )
    {
        this.facturado = facturado;
    }

    public Double getDeposito()
    {
        return deposito;
    }

    public void setDeposito( Double deposito )
    {
        this.deposito = deposito;
    }

    public Double getValorEmFalta()
    {
        return valorEmFalta;
    }

    public void setValorEmFalta( Double valorEmFalta )
    {
        this.valorEmFalta = valorEmFalta;
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

    public TbMesas getFkMesas()
    {
        return fkMesas;
    }

    public void setFkMesas( TbMesas fkMesas )
    {
        this.fkMesas = fkMesas;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += ( pkPedido != null ? pkPedido.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if ( !( object instanceof TbPedido ) )
        {
            return false;
        }
        TbPedido other = ( TbPedido ) object;
        if ( ( this.pkPedido == null && other.pkPedido != null ) || ( this.pkPedido != null && !this.pkPedido.equals( other.pkPedido ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.TbPedido[ pkPedido=" + pkPedido + " ]";
    }
    
}
