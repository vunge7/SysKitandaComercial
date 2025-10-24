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
@Table(name = "fecho_periodo")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "FechoPeriodo.findAll", query = "SELECT f FROM FechoPeriodo f"),
    @NamedQuery(name = "FechoPeriodo.findByPkFechoPeriodo", query = "SELECT f FROM FechoPeriodo f WHERE f.pkFechoPeriodo = :pkFechoPeriodo"),
    @NamedQuery(name = "FechoPeriodo.findByDataAbertura", query = "SELECT f FROM FechoPeriodo f WHERE f.dataAbertura = :dataAbertura"),
    @NamedQuery(name = "FechoPeriodo.findByDataFecho", query = "SELECT f FROM FechoPeriodo f WHERE f.dataFecho = :dataFecho")
})
public class FechoPeriodo implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pk_fecho_periodo")
    private Integer pkFechoPeriodo;
    @Column(name = "data_abertura")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataAbertura;
    @Column(name = "data_fecho")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataFecho;
    @OneToMany(mappedBy = "fkFechoPeriodo")
    private List<ReciboRh> reciboRhList;
    @JoinColumn(name = "fk_ano", referencedColumnName = "idAno")
    @ManyToOne(optional = false)
    private TbAno fkAno;
    @JoinColumn(name = "fk_periodo", referencedColumnName = "pk_mes_rh")
    @ManyToOne(optional = false)
    private TbMesRh fkPeriodo;
    @JoinColumn(name = "fk_usuario", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private TbUsuario fkUsuario;

    public FechoPeriodo()
    {
    }

    public FechoPeriodo( Integer pkFechoPeriodo )
    {
        this.pkFechoPeriodo = pkFechoPeriodo;
    }

    public Integer getPkFechoPeriodo()
    {
        return pkFechoPeriodo;
    }

    public void setPkFechoPeriodo( Integer pkFechoPeriodo )
    {
        this.pkFechoPeriodo = pkFechoPeriodo;
    }

    public Date getDataAbertura()
    {
        return dataAbertura;
    }

    public void setDataAbertura( Date dataAbertura )
    {
        this.dataAbertura = dataAbertura;
    }

    public Date getDataFecho()
    {
        return dataFecho;
    }

    public void setDataFecho( Date dataFecho )
    {
        this.dataFecho = dataFecho;
    }

    @XmlTransient
    public List<ReciboRh> getReciboRhList()
    {
        return reciboRhList;
    }

    public void setReciboRhList( List<ReciboRh> reciboRhList )
    {
        this.reciboRhList = reciboRhList;
    }

    public TbAno getFkAno()
    {
        return fkAno;
    }

    public void setFkAno( TbAno fkAno )
    {
        this.fkAno = fkAno;
    }

    public TbMesRh getFkPeriodo()
    {
        return fkPeriodo;
    }

    public void setFkPeriodo( TbMesRh fkPeriodo )
    {
        this.fkPeriodo = fkPeriodo;
    }

    public TbUsuario getFkUsuario()
    {
        return fkUsuario;
    }

    public void setFkUsuario( TbUsuario fkUsuario )
    {
        this.fkUsuario = fkUsuario;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += ( pkFechoPeriodo != null ? pkFechoPeriodo.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if ( !( object instanceof FechoPeriodo ) )
        {
            return false;
        }
        FechoPeriodo other = ( FechoPeriodo ) object;
        if ( ( this.pkFechoPeriodo == null && other.pkFechoPeriodo != null ) || ( this.pkFechoPeriodo != null && !this.pkFechoPeriodo.equals( other.pkFechoPeriodo ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.FechoPeriodo[ pkFechoPeriodo=" + pkFechoPeriodo + " ]";
    }
    
}
