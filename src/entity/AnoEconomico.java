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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author lenovo
 */
@Entity
@Table(name = "ano_economico")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "AnoEconomico.findAll", query = "SELECT a FROM AnoEconomico a"),
    @NamedQuery(name = "AnoEconomico.findByPkAnoEconomico", query = "SELECT a FROM AnoEconomico a WHERE a.pkAnoEconomico = :pkAnoEconomico"),
    @NamedQuery(name = "AnoEconomico.findByDesignacao", query = "SELECT a FROM AnoEconomico a WHERE a.designacao = :designacao"),
    @NamedQuery(name = "AnoEconomico.findBySerie", query = "SELECT a FROM AnoEconomico a WHERE a.serie = :serie"),
    @NamedQuery(name = "AnoEconomico.findByDataInicio", query = "SELECT a FROM AnoEconomico a WHERE a.dataInicio = :dataInicio"),
    @NamedQuery(name = "AnoEconomico.findByDataFim", query = "SELECT a FROM AnoEconomico a WHERE a.dataFim = :dataFim")
})
public class AnoEconomico implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pk_ano_economico")
    private Integer pkAnoEconomico;
    @Column(name = "designacao")
    private String designacao;
    @Column(name = "serie")
    private String serie;
    @Column(name = "data_inicio")
    @Temporal(TemporalType.DATE)
    private Date dataInicio;
    @Column(name = "data_fim")
    @Temporal(TemporalType.DATE)
    private Date dataFim;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkAnoEconomico")
    private List<NotasCompras> notasComprasList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkAnoEconomico")
    private List<Notas> notasList;
    @OneToMany(mappedBy = "fkAnoEconomico")
    private List<Compras> comprasList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkAnoEconomico")
    private List<TbVenda> tbVendaList;

    public AnoEconomico()
    {
    }

    public AnoEconomico( Integer pkAnoEconomico )
    {
        this.pkAnoEconomico = pkAnoEconomico;
    }

    public Integer getPkAnoEconomico()
    {
        return pkAnoEconomico;
    }

    public void setPkAnoEconomico( Integer pkAnoEconomico )
    {
        this.pkAnoEconomico = pkAnoEconomico;
    }

    public String getDesignacao()
    {
        return designacao;
    }

    public void setDesignacao( String designacao )
    {
        this.designacao = designacao;
    }

    public String getSerie()
    {
        return serie;
    }

    public void setSerie( String serie )
    {
        this.serie = serie;
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

    @XmlTransient
    public List<NotasCompras> getNotasComprasList()
    {
        return notasComprasList;
    }

    public void setNotasComprasList( List<NotasCompras> notasComprasList )
    {
        this.notasComprasList = notasComprasList;
    }

    @XmlTransient
    public List<Notas> getNotasList()
    {
        return notasList;
    }

    public void setNotasList( List<Notas> notasList )
    {
        this.notasList = notasList;
    }

    @XmlTransient
    public List<Compras> getComprasList()
    {
        return comprasList;
    }

    public void setComprasList( List<Compras> comprasList )
    {
        this.comprasList = comprasList;
    }

    @XmlTransient
    public List<TbVenda> getTbVendaList()
    {
        return tbVendaList;
    }

    public void setTbVendaList( List<TbVenda> tbVendaList )
    {
        this.tbVendaList = tbVendaList;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += ( pkAnoEconomico != null ? pkAnoEconomico.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if ( !( object instanceof AnoEconomico ) )
        {
            return false;
        }
        AnoEconomico other = ( AnoEconomico ) object;
        if ( ( this.pkAnoEconomico == null && other.pkAnoEconomico != null ) || ( this.pkAnoEconomico != null && !this.pkAnoEconomico.equals( other.pkAnoEconomico ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.AnoEconomico[ pkAnoEconomico=" + pkAnoEconomico + " ]";
    }
    
}
