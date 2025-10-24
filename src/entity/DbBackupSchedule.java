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
@Table(name = "db_backup_schedule")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "DbBackupSchedule.findAll", query = "SELECT d FROM DbBackupSchedule d"),
    @NamedQuery(name = "DbBackupSchedule.findByPkDbBackupSchedule", query = "SELECT d FROM DbBackupSchedule d WHERE d.pkDbBackupSchedule = :pkDbBackupSchedule"),
    @NamedQuery(name = "DbBackupSchedule.findByAno", query = "SELECT d FROM DbBackupSchedule d WHERE d.ano = :ano"),
    @NamedQuery(name = "DbBackupSchedule.findByMes", query = "SELECT d FROM DbBackupSchedule d WHERE d.mes = :mes"),
    @NamedQuery(name = "DbBackupSchedule.findByDia", query = "SELECT d FROM DbBackupSchedule d WHERE d.dia = :dia"),
    @NamedQuery(name = "DbBackupSchedule.findByHora", query = "SELECT d FROM DbBackupSchedule d WHERE d.hora = :hora"),
    @NamedQuery(name = "DbBackupSchedule.findByMinuto", query = "SELECT d FROM DbBackupSchedule d WHERE d.minuto = :minuto"),
    @NamedQuery(name = "DbBackupSchedule.findBySegundo", query = "SELECT d FROM DbBackupSchedule d WHERE d.segundo = :segundo"),
    @NamedQuery(name = "DbBackupSchedule.findByDataUltimoBackup", query = "SELECT d FROM DbBackupSchedule d WHERE d.dataUltimoBackup = :dataUltimoBackup"),
    @NamedQuery(name = "DbBackupSchedule.findByDataProximoBackup", query = "SELECT d FROM DbBackupSchedule d WHERE d.dataProximoBackup = :dataProximoBackup")
})
public class DbBackupSchedule implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pk_db_backup_schedule")
    private Integer pkDbBackupSchedule;
    @Column(name = "ano")
    private Integer ano;
    @Column(name = "mes")
    private Integer mes;
    @Column(name = "dia")
    private Integer dia;
    @Column(name = "hora")
    private Integer hora;
    @Column(name = "minuto")
    private Integer minuto;
    @Column(name = "segundo")
    private Integer segundo;
    @Basic(optional = false)
    @Column(name = "dataUltimoBackup")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataUltimoBackup;
    @Basic(optional = false)
    @Column(name = "dataProximoBackup")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataProximoBackup;

    public DbBackupSchedule()
    {
    }

    public DbBackupSchedule( Integer pkDbBackupSchedule )
    {
        this.pkDbBackupSchedule = pkDbBackupSchedule;
    }

    public DbBackupSchedule( Integer pkDbBackupSchedule, Date dataUltimoBackup, Date dataProximoBackup )
    {
        this.pkDbBackupSchedule = pkDbBackupSchedule;
        this.dataUltimoBackup = dataUltimoBackup;
        this.dataProximoBackup = dataProximoBackup;
    }

    public Integer getPkDbBackupSchedule()
    {
        return pkDbBackupSchedule;
    }

    public void setPkDbBackupSchedule( Integer pkDbBackupSchedule )
    {
        this.pkDbBackupSchedule = pkDbBackupSchedule;
    }

    public Integer getAno()
    {
        return ano;
    }

    public void setAno( Integer ano )
    {
        this.ano = ano;
    }

    public Integer getMes()
    {
        return mes;
    }

    public void setMes( Integer mes )
    {
        this.mes = mes;
    }

    public Integer getDia()
    {
        return dia;
    }

    public void setDia( Integer dia )
    {
        this.dia = dia;
    }

    public Integer getHora()
    {
        return hora;
    }

    public void setHora( Integer hora )
    {
        this.hora = hora;
    }

    public Integer getMinuto()
    {
        return minuto;
    }

    public void setMinuto( Integer minuto )
    {
        this.minuto = minuto;
    }

    public Integer getSegundo()
    {
        return segundo;
    }

    public void setSegundo( Integer segundo )
    {
        this.segundo = segundo;
    }

    public Date getDataUltimoBackup()
    {
        return dataUltimoBackup;
    }

    public void setDataUltimoBackup( Date dataUltimoBackup )
    {
        this.dataUltimoBackup = dataUltimoBackup;
    }

    public Date getDataProximoBackup()
    {
        return dataProximoBackup;
    }

    public void setDataProximoBackup( Date dataProximoBackup )
    {
        this.dataProximoBackup = dataProximoBackup;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += ( pkDbBackupSchedule != null ? pkDbBackupSchedule.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if ( !( object instanceof DbBackupSchedule ) )
        {
            return false;
        }
        DbBackupSchedule other = ( DbBackupSchedule ) object;
        if ( ( this.pkDbBackupSchedule == null && other.pkDbBackupSchedule != null ) || ( this.pkDbBackupSchedule != null && !this.pkDbBackupSchedule.equals( other.pkDbBackupSchedule ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.DbBackupSchedule[ pkDbBackupSchedule=" + pkDbBackupSchedule + " ]";
    }
    
}
