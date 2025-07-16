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
@Table(name = "tb_pro_forma")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "TbProForma.findAll", query = "SELECT t FROM TbProForma t"),
    @NamedQuery(name = "TbProForma.findByPkProForma", query = "SELECT t FROM TbProForma t WHERE t.pkProForma = :pkProForma"),
    @NamedQuery(name = "TbProForma.findByData", query = "SELECT t FROM TbProForma t WHERE t.data = :data"),
    @NamedQuery(name = "TbProForma.findByHora", query = "SELECT t FROM TbProForma t WHERE t.hora = :hora"),
    @NamedQuery(name = "TbProForma.findByTotal", query = "SELECT t FROM TbProForma t WHERE t.total = :total")
})
public class TbProForma implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pk_pro_forma")
    private Integer pkProForma;
    @Column(name = "data")
    @Temporal(TemporalType.DATE)
    private Date data;
    @Column(name = "hora")
    @Temporal(TemporalType.TIME)
    private Date hora;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "total")
    private Double total;
    @JoinColumn(name = "fk_cliente", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private TbCliente fkCliente;
    @JoinColumn(name = "fk_usuario", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private TbUsuario fkUsuario;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkProForma")
    private List<TbItemProForma> tbItemProFormaList;

    public TbProForma()
    {
    }

    public TbProForma( Integer pkProForma )
    {
        this.pkProForma = pkProForma;
    }

    public Integer getPkProForma()
    {
        return pkProForma;
    }

    public void setPkProForma( Integer pkProForma )
    {
        this.pkProForma = pkProForma;
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

    public Double getTotal()
    {
        return total;
    }

    public void setTotal( Double total )
    {
        this.total = total;
    }

    public TbCliente getFkCliente()
    {
        return fkCliente;
    }

    public void setFkCliente( TbCliente fkCliente )
    {
        this.fkCliente = fkCliente;
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
    public List<TbItemProForma> getTbItemProFormaList()
    {
        return tbItemProFormaList;
    }

    public void setTbItemProFormaList( List<TbItemProForma> tbItemProFormaList )
    {
        this.tbItemProFormaList = tbItemProFormaList;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += ( pkProForma != null ? pkProForma.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if ( !( object instanceof TbProForma ) )
        {
            return false;
        }
        TbProForma other = ( TbProForma ) object;
        if ( ( this.pkProForma == null && other.pkProForma != null ) || ( this.pkProForma != null && !this.pkProForma.equals( other.pkProForma ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.TbProForma[ pkProForma=" + pkProForma + " ]";
    }
    
}
