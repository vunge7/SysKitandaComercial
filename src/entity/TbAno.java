/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author lenovo
 */
@Entity
@Table(name = "tb_ano")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "TbAno.findAll", query = "SELECT t FROM TbAno t"),
    @NamedQuery(name = "TbAno.findByIdAno", query = "SELECT t FROM TbAno t WHERE t.idAno = :idAno"),
    @NamedQuery(name = "TbAno.findByDescrisao", query = "SELECT t FROM TbAno t WHERE t.descrisao = :descrisao")
})
public class TbAno implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idAno")
    private Integer idAno;
    @Column(name = "descrisao")
    private String descrisao;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkAno")
    private List<FechoPeriodo> fechoPeriodoList;

    public TbAno()
    {
    }

    public TbAno( Integer idAno )
    {
        this.idAno = idAno;
    }

    public Integer getIdAno()
    {
        return idAno;
    }

    public void setIdAno( Integer idAno )
    {
        this.idAno = idAno;
    }

    public String getDescrisao()
    {
        return descrisao;
    }

    public void setDescrisao( String descrisao )
    {
        this.descrisao = descrisao;
    }

    @XmlTransient
    public List<FechoPeriodo> getFechoPeriodoList()
    {
        return fechoPeriodoList;
    }

    public void setFechoPeriodoList( List<FechoPeriodo> fechoPeriodoList )
    {
        this.fechoPeriodoList = fechoPeriodoList;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += ( idAno != null ? idAno.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if ( !( object instanceof TbAno ) )
        {
            return false;
        }
        TbAno other = ( TbAno ) object;
        if ( ( this.idAno == null && other.idAno != null ) || ( this.idAno != null && !this.idAno.equals( other.idAno ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.TbAno[ idAno=" + idAno + " ]";
    }
    
}
