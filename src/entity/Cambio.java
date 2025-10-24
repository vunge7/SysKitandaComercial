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
@Table(name = "cambio")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "Cambio.findAll", query = "SELECT c FROM Cambio c"),
    @NamedQuery(name = "Cambio.findByPkCambio", query = "SELECT c FROM Cambio c WHERE c.pkCambio = :pkCambio"),
    @NamedQuery(name = "Cambio.findByValor", query = "SELECT c FROM Cambio c WHERE c.valor = :valor"),
    @NamedQuery(name = "Cambio.findByDataHora", query = "SELECT c FROM Cambio c WHERE c.dataHora = :dataHora")
})
public class Cambio implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pk_cambio")
    private Integer pkCambio;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor")
    private Double valor;
    @Column(name = "data_hora")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataHora;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkCambio")
    private List<Notas> notasList;
    @JoinColumn(name = "fk_moeda", referencedColumnName = "pk_moeda")
    @ManyToOne(optional = false)
    private Moeda fkMoeda;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkCambio")
    private List<TbVenda> tbVendaList;

    public Cambio()
    {
    }

    public Cambio( Integer pkCambio )
    {
        this.pkCambio = pkCambio;
    }

    public Integer getPkCambio()
    {
        return pkCambio;
    }

    public void setPkCambio( Integer pkCambio )
    {
        this.pkCambio = pkCambio;
    }

    public Double getValor()
    {
        return valor;
    }

    public void setValor( Double valor )
    {
        this.valor = valor;
    }

    public Date getDataHora()
    {
        return dataHora;
    }

    public void setDataHora( Date dataHora )
    {
        this.dataHora = dataHora;
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

    public Moeda getFkMoeda()
    {
        return fkMoeda;
    }

    public void setFkMoeda( Moeda fkMoeda )
    {
        this.fkMoeda = fkMoeda;
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
        hash += ( pkCambio != null ? pkCambio.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if ( !( object instanceof Cambio ) )
        {
            return false;
        }
        Cambio other = ( Cambio ) object;
        if ( ( this.pkCambio == null && other.pkCambio != null ) || ( this.pkCambio != null && !this.pkCambio.equals( other.pkCambio ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.Cambio[ pkCambio=" + pkCambio + " ]";
    }
    
}
