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
@Table(name = "tb_validade")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "TbValidade.findAll", query = "SELECT t FROM TbValidade t"),
    @NamedQuery(name = "TbValidade.findByPkValidade", query = "SELECT t FROM TbValidade t WHERE t.pkValidade = :pkValidade"),
    @NamedQuery(name = "TbValidade.findByDataInicio", query = "SELECT t FROM TbValidade t WHERE t.dataInicio = :dataInicio"),
    @NamedQuery(name = "TbValidade.findByDataFim", query = "SELECT t FROM TbValidade t WHERE t.dataFim = :dataFim")
})
public class TbValidade implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pk_validade")
    private Integer pkValidade;
    @Basic(optional = false)
    @Column(name = "data_inicio")
    @Temporal(TemporalType.DATE)
    private Date dataInicio;
    @Basic(optional = false)
    @Column(name = "data_fim")
    @Temporal(TemporalType.DATE)
    private Date dataFim;

    public TbValidade()
    {
    }

    public TbValidade( Integer pkValidade )
    {
        this.pkValidade = pkValidade;
    }

    public TbValidade( Integer pkValidade, Date dataInicio, Date dataFim )
    {
        this.pkValidade = pkValidade;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
    }

    public Integer getPkValidade()
    {
        return pkValidade;
    }

    public void setPkValidade( Integer pkValidade )
    {
        this.pkValidade = pkValidade;
    }

    public Date getDataInicio()
    {
        return dataInicio;
    }

    public void setDataInicio( Date dataInicio )
    {
        this.dataInicio = dataInicio;
    }

    public Date getDataFim()
    {
        return dataFim;
    }

    public void setDataFim( Date dataFim )
    {
        this.dataFim = dataFim;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += ( pkValidade != null ? pkValidade.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if ( !( object instanceof TbValidade ) )
        {
            return false;
        }
        TbValidade other = ( TbValidade ) object;
        if ( ( this.pkValidade == null && other.pkValidade != null ) || ( this.pkValidade != null && !this.pkValidade.equals( other.pkValidade ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.TbValidade[ pkValidade=" + pkValidade + " ]";
    }
    
}
