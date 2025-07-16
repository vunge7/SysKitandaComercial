/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author lenovo
 */
@Entity
@Table(name = "turno")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "Turno.findAll", query = "SELECT t FROM Turno t"),
    @NamedQuery(name = "Turno.findByPkTurno", query = "SELECT t FROM Turno t WHERE t.pkTurno = :pkTurno"),
    @NamedQuery(name = "Turno.findByData", query = "SELECT t FROM Turno t WHERE t.data = :data"),
    @NamedQuery(name = "Turno.findByHoraAbertura", query = "SELECT t FROM Turno t WHERE t.horaAbertura = :horaAbertura"),
    @NamedQuery(name = "Turno.findByHoraFecho", query = "SELECT t FROM Turno t WHERE t.horaFecho = :horaFecho"),
    @NamedQuery(name = "Turno.findByTurno", query = "SELECT t FROM Turno t WHERE t.turno = :turno")
})
public class Turno implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pk_turno")
    private Integer pkTurno;
    @Column(name = "data")
    @Temporal(TemporalType.DATE)
    private Date data;
    @Column(name = "hora_abertura")
    @Temporal(TemporalType.TIME)
    private Date horaAbertura;
    @Column(name = "hora_fecho")
    @Temporal(TemporalType.TIME)
    private Date horaFecho;
    @Column(name = "turno")
    private String turno;
    @JoinColumn(name = "fk_usuario", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private TbUsuario fkUsuario;

    public Turno()
    {
    }

    public Turno( Integer pkTurno )
    {
        this.pkTurno = pkTurno;
    }

    public Integer getPkTurno()
    {
        return pkTurno;
    }

    public void setPkTurno( Integer pkTurno )
    {
        this.pkTurno = pkTurno;
    }

    public Date getData()
    {
        return data;
    }

    public void setData( Date data )
    {
        this.data = data;
    }

    public Date getHoraAbertura()
    {
        return horaAbertura;
    }

    public void setHoraAbertura( Date horaAbertura )
    {
        this.horaAbertura = horaAbertura;
    }

    public Date getHoraFecho()
    {
        return horaFecho;
    }

    public void setHoraFecho( Date horaFecho )
    {
        this.horaFecho = horaFecho;
    }

    public String getTurno()
    {
        return turno;
    }

    public void setTurno( String turno )
    {
        this.turno = turno;
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
        hash += ( pkTurno != null ? pkTurno.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if ( !( object instanceof Turno ) )
        {
            return false;
        }
        Turno other = ( Turno ) object;
        if ( ( this.pkTurno == null && other.pkTurno != null ) || ( this.pkTurno != null && !this.pkTurno.equals( other.pkTurno ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.Turno[ pkTurno=" + pkTurno + " ]";
    }
    
}
