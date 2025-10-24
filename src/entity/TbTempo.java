/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;


import java.sql.Connection;
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
@Table(name = "tb_tempo")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "TbTempo.findAll", query = "SELECT t FROM TbTempo t"),
    @NamedQuery(name = "TbTempo.findByIdTempoPK", query = "SELECT t FROM TbTempo t WHERE t.idTempoPK = :idTempoPK"),
    @NamedQuery(name = "TbTempo.findByTempoDado", query = "SELECT t FROM TbTempo t WHERE t.tempoDado = :tempoDado"),
    @NamedQuery(name = "TbTempo.findByData", query = "SELECT t FROM TbTempo t WHERE t.data = :data"),
    @NamedQuery(name = "TbTempo.findByHora", query = "SELECT t FROM TbTempo t WHERE t.hora = :hora")
})
public class TbTempo implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idTempoPK")
    private Integer idTempoPK;
    @Column(name = "tempo_dado")
    private Integer tempoDado;
    @Column(name = "data")
    @Temporal(TemporalType.DATE)
    private Date data;
    @Column(name = "hora")
    @Temporal(TemporalType.TIME)
    private Date hora;
    @JoinColumn(name = "idFuncionarioFK", referencedColumnName = "idFuncionario")
    @ManyToOne
    private TbFuncionario idFuncionarioFK;

    public TbTempo()
    {
    }

    public TbTempo( Integer idTempoPK )
    {
        this.idTempoPK = idTempoPK;
    }

    public Integer getIdTempoPK()
    {
        return idTempoPK;
    }

    public void setIdTempoPK( Integer idTempoPK )
    {
        this.idTempoPK = idTempoPK;
    }

    public Integer getTempoDado()
    {
        return tempoDado;
    }

    public void setTempoDado( Integer tempoDado )
    {
        this.tempoDado = tempoDado;
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

    public TbFuncionario getIdFuncionarioFK()
    {
        return idFuncionarioFK;
    }

    public void setIdFuncionarioFK( TbFuncionario idFuncionarioFK )
    {
        this.idFuncionarioFK = idFuncionarioFK;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += ( idTempoPK != null ? idTempoPK.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if ( !( object instanceof TbTempo ) )
        {
            return false;
        }
        TbTempo other = ( TbTempo ) object;
        if ( ( this.idTempoPK == null && other.idTempoPK != null ) || ( this.idTempoPK != null && !this.idTempoPK.equals( other.idTempoPK ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.TbTempo[ idTempoPK=" + idTempoPK + " ]";
    }
    
}
