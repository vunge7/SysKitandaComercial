/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;


import java.sql.Connection;
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
@Table(name = "tb_mes_rh")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "TbMesRh.findAll", query = "SELECT t FROM TbMesRh t"),
    @NamedQuery(name = "TbMesRh.findByPkMesRh", query = "SELECT t FROM TbMesRh t WHERE t.pkMesRh = :pkMesRh"),
    @NamedQuery(name = "TbMesRh.findByDescricao", query = "SELECT t FROM TbMesRh t WHERE t.descricao = :descricao")
})
public class TbMesRh implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pk_mes_rh")
    private Integer pkMesRh;
    @Column(name = "descricao")
    private String descricao;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkPeriodo")
    private List<FechoPeriodo> fechoPeriodoList;

    public TbMesRh()
    {
    }

    public TbMesRh( Integer pkMesRh )
    {
        this.pkMesRh = pkMesRh;
    }

    public Integer getPkMesRh()
    {
        return pkMesRh;
    }

    public void setPkMesRh( Integer pkMesRh )
    {
        this.pkMesRh = pkMesRh;
    }

    public String getDescricao()
    {
        return descricao;
    }

    public void setDescricao( String descricao )
    {
        this.descricao = descricao;
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
        hash += ( pkMesRh != null ? pkMesRh.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if ( !( object instanceof TbMesRh ) )
        {
            return false;
        }
        TbMesRh other = ( TbMesRh ) object;
        if ( ( this.pkMesRh == null && other.pkMesRh != null ) || ( this.pkMesRh != null && !this.pkMesRh.equals( other.pkMesRh ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.TbMesRh[ pkMesRh=" + pkMesRh + " ]";
    }
    
}
