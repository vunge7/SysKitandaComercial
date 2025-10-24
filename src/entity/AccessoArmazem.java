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
@Table(name = "accesso_armazem")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "AccessoArmazem.findAll", query = "SELECT a FROM AccessoArmazem a"),
    @NamedQuery(name = "AccessoArmazem.findByPkAccessoArmazem", query = "SELECT a FROM AccessoArmazem a WHERE a.pkAccessoArmazem = :pkAccessoArmazem"),
    @NamedQuery(name = "AccessoArmazem.findByDataTime", query = "SELECT a FROM AccessoArmazem a WHERE a.dataTime = :dataTime")
})
public class AccessoArmazem implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pk_accesso_armazem")
    private Integer pkAccessoArmazem;
    @Column(name = "data_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataTime;
    @JoinColumn(name = "fk_armazem", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private TbArmazem fkArmazem;
    @JoinColumn(name = "fk_usuario", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private TbUsuario fkUsuario;

    public AccessoArmazem()
    {
    }

    public AccessoArmazem( Integer pkAccessoArmazem )
    {
        this.pkAccessoArmazem = pkAccessoArmazem;
    }

    public Integer getPkAccessoArmazem()
    {
        return pkAccessoArmazem;
    }

    public void setPkAccessoArmazem( Integer pkAccessoArmazem )
    {
        this.pkAccessoArmazem = pkAccessoArmazem;
    }

    public Date getDataTime()
    {
        return dataTime;
    }

    public void setDataTime( Date dataTime )
    {
        this.dataTime = dataTime;
    }

    public TbArmazem getFkArmazem()
    {
        return fkArmazem;
    }

    public void setFkArmazem( TbArmazem fkArmazem )
    {
        this.fkArmazem = fkArmazem;
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
        hash += ( pkAccessoArmazem != null ? pkAccessoArmazem.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if ( !( object instanceof AccessoArmazem ) )
        {
            return false;
        }
        AccessoArmazem other = ( AccessoArmazem ) object;
        if ( ( this.pkAccessoArmazem == null && other.pkAccessoArmazem != null ) || ( this.pkAccessoArmazem != null && !this.pkAccessoArmazem.equals( other.pkAccessoArmazem ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.AccessoArmazem[ pkAccessoArmazem=" + pkAccessoArmazem + " ]";
    }
    
}
