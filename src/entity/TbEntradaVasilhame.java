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
@Table(name = "tb_entrada_vasilhame")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "TbEntradaVasilhame.findAll", query = "SELECT t FROM TbEntradaVasilhame t"),
    @NamedQuery(name = "TbEntradaVasilhame.findByPkEntradaVasilhame", query = "SELECT t FROM TbEntradaVasilhame t WHERE t.pkEntradaVasilhame = :pkEntradaVasilhame"),
    @NamedQuery(name = "TbEntradaVasilhame.findByDataEntrada", query = "SELECT t FROM TbEntradaVasilhame t WHERE t.dataEntrada = :dataEntrada"),
    @NamedQuery(name = "TbEntradaVasilhame.findByHoraEntrada", query = "SELECT t FROM TbEntradaVasilhame t WHERE t.horaEntrada = :horaEntrada")
})
public class TbEntradaVasilhame implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pk_entrada_vasilhame")
    private Integer pkEntradaVasilhame;
    @Column(name = "data_entrada")
    @Temporal(TemporalType.DATE)
    private Date dataEntrada;
    @Column(name = "hora_entrada")
    @Temporal(TemporalType.TIME)
    private Date horaEntrada;
    @JoinColumn(name = "fk_amazem", referencedColumnName = "codigo")
    @ManyToOne
    private TbArmazem fkAmazem;
    @JoinColumn(name = "fk_usuario", referencedColumnName = "codigo")
    @ManyToOne
    private TbUsuario fkUsuario;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkEntradaVasilhame")
    private List<TbItemEntradaVasilhame> tbItemEntradaVasilhameList;

    public TbEntradaVasilhame()
    {
    }

    public TbEntradaVasilhame( Integer pkEntradaVasilhame )
    {
        this.pkEntradaVasilhame = pkEntradaVasilhame;
    }

    public Integer getPkEntradaVasilhame()
    {
        return pkEntradaVasilhame;
    }

    public void setPkEntradaVasilhame( Integer pkEntradaVasilhame )
    {
        this.pkEntradaVasilhame = pkEntradaVasilhame;
    }

    public Date getDataEntrada()
    {
        return dataEntrada;
    }

    public void setDataEntrada( Date dataEntrada )
    {
        this.dataEntrada = dataEntrada;
    }

    public Date getHoraEntrada()
    {
        return horaEntrada;
    }

    public void setHoraEntrada( Date horaEntrada )
    {
        this.horaEntrada = horaEntrada;
    }

    public TbArmazem getFkAmazem()
    {
        return fkAmazem;
    }

    public void setFkAmazem( TbArmazem fkAmazem )
    {
        this.fkAmazem = fkAmazem;
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
    public List<TbItemEntradaVasilhame> getTbItemEntradaVasilhameList()
    {
        return tbItemEntradaVasilhameList;
    }

    public void setTbItemEntradaVasilhameList( List<TbItemEntradaVasilhame> tbItemEntradaVasilhameList )
    {
        this.tbItemEntradaVasilhameList = tbItemEntradaVasilhameList;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += ( pkEntradaVasilhame != null ? pkEntradaVasilhame.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if ( !( object instanceof TbEntradaVasilhame ) )
        {
            return false;
        }
        TbEntradaVasilhame other = ( TbEntradaVasilhame ) object;
        if ( ( this.pkEntradaVasilhame == null && other.pkEntradaVasilhame != null ) || ( this.pkEntradaVasilhame != null && !this.pkEntradaVasilhame.equals( other.pkEntradaVasilhame ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.TbEntradaVasilhame[ pkEntradaVasilhame=" + pkEntradaVasilhame + " ]";
    }
    
}
