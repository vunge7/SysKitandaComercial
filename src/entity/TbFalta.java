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
@Table(name = "tb_falta")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "TbFalta.findAll", query = "SELECT t FROM TbFalta t"),
    @NamedQuery(name = "TbFalta.findByIdFaltaPK", query = "SELECT t FROM TbFalta t WHERE t.idFaltaPK = :idFaltaPK"),
    @NamedQuery(name = "TbFalta.findByNFalta", query = "SELECT t FROM TbFalta t WHERE t.nFalta = :nFalta"),
    @NamedQuery(name = "TbFalta.findByData", query = "SELECT t FROM TbFalta t WHERE t.data = :data"),
    @NamedQuery(name = "TbFalta.findByHora", query = "SELECT t FROM TbFalta t WHERE t.hora = :hora"),
    @NamedQuery(name = "TbFalta.findByDescricaoFalta", query = "SELECT t FROM TbFalta t WHERE t.descricaoFalta = :descricaoFalta"),
    @NamedQuery(name = "TbFalta.findByJustificadaInjustificada", query = "SELECT t FROM TbFalta t WHERE t.justificadaInjustificada = :justificadaInjustificada"),
    @NamedQuery(name = "TbFalta.findByDescricaoJustificativa", query = "SELECT t FROM TbFalta t WHERE t.descricaoJustificativa = :descricaoJustificativa"),
    @NamedQuery(name = "TbFalta.findByDataJustificativa", query = "SELECT t FROM TbFalta t WHERE t.dataJustificativa = :dataJustificativa")
})
public class TbFalta implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idFaltaPK")
    private Integer idFaltaPK;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "n_falta")
    private Double nFalta;
    @Column(name = "data")
    @Temporal(TemporalType.DATE)
    private Date data;
    @Column(name = "hora")
    @Temporal(TemporalType.TIME)
    private Date hora;
    @Column(name = "descricao_falta")
    private String descricaoFalta;
    @Column(name = "justificada_injustificada")
    private Boolean justificadaInjustificada;
    @Column(name = "descricao_justificativa")
    private String descricaoJustificativa;
    @Column(name = "data_justificativa")
    @Temporal(TemporalType.DATE)
    private Date dataJustificativa;
    @JoinColumn(name = "idFuncionarioFK", referencedColumnName = "idFuncionario")
    @ManyToOne
    private TbFuncionario idFuncionarioFK;

    public TbFalta()
    {
    }

    public TbFalta( Integer idFaltaPK )
    {
        this.idFaltaPK = idFaltaPK;
    }

    public Integer getIdFaltaPK()
    {
        return idFaltaPK;
    }

    public void setIdFaltaPK( Integer idFaltaPK )
    {
        this.idFaltaPK = idFaltaPK;
    }

    public Double getNFalta()
    {
        return nFalta;
    }

    public void setNFalta( Double nFalta )
    {
        this.nFalta = nFalta;
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

    public String getDescricaoFalta()
    {
        return descricaoFalta;
    }

    public void setDescricaoFalta( String descricaoFalta )
    {
        this.descricaoFalta = descricaoFalta;
    }

    public Boolean getJustificadaInjustificada()
    {
        return justificadaInjustificada;
    }

    public void setJustificadaInjustificada( Boolean justificadaInjustificada )
    {
        this.justificadaInjustificada = justificadaInjustificada;
    }

    public String getDescricaoJustificativa()
    {
        return descricaoJustificativa;
    }

    public void setDescricaoJustificativa( String descricaoJustificativa )
    {
        this.descricaoJustificativa = descricaoJustificativa;
    }

    public Date getDataJustificativa()
    {
        return dataJustificativa;
    }

    public void setDataJustificativa( Date dataJustificativa )
    {
        this.dataJustificativa = dataJustificativa;
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
        hash += ( idFaltaPK != null ? idFaltaPK.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if ( !( object instanceof TbFalta ) )
        {
            return false;
        }
        TbFalta other = ( TbFalta ) object;
        if ( ( this.idFaltaPK == null && other.idFaltaPK != null ) || ( this.idFaltaPK != null && !this.idFaltaPK.equals( other.idFaltaPK ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.TbFalta[ idFaltaPK=" + idFaltaPK + " ]";
    }
    
}
