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
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
@Table(name = "lote")
@XmlRootElement
@NamedQueries(
        {
            @NamedQuery(name = "Lote.findAll", query = "SELECT l FROM Lote l"),
            @NamedQuery(name = "Lote.findById", query = "SELECT l FROM Lote l WHERE l.id = :id"),
            @NamedQuery(name = "Lote.findByFkProduto", query = "SELECT l FROM Lote l WHERE l.fkProduto = :fkProduto"),
            @NamedQuery(name = "Lote.findByNumeroLote", query = "SELECT l FROM Lote l WHERE l.fkProduto = :fkProduto AND l.numeroLote = :numeroLote"),
            @NamedQuery(name = "Lote.findByEstado", query = "SELECT l FROM Lote l WHERE l.estado = :estado"),
            @NamedQuery(name = "Lote.findParaBaixa", query = "SELECT l FROM Lote l WHERE l.fkProduto = :fkProduto AND l.estado = :estado AND l.qtd > 0 AND l.dataValidade >= CURRENT_DATE ORDER BY l.dataValidade ASC"),
            @NamedQuery(name = "Lote.findAVencer", query = "SELECT l FROM Lote l WHERE l.estado = :estado AND l.qtd > 0 AND l.dataValidade BETWEEN :inicio AND :fim ORDER BY l.dataValidade ASC")
        })
public class Lote implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "fk_produto")
    private Integer fkProduto;
    @Basic(optional = false)
    @Column(name = "numero_lote")
    private String numeroLote;
    @Basic(optional = false)
    @Column(name = "data_entrada")
    @Temporal(TemporalType.DATE)
    private Date dataEntrada;
    @Column(name = "data_fabrica")
    @Temporal(TemporalType.DATE)
    private Date dataFabrica;
    @Basic(optional = false)
    @Column(name = "data_validade")
    @Temporal(TemporalType.DATE)
    private Date dataValidade;
    @Basic(optional = false)
    @Column(name = "qtd")
    private int qtd;
    @Basic(optional = false)
    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private EstadoLote estado = EstadoLote.ACTIVO;

    public Lote()
    {
    }

    public Lote( Integer id )
    {
        this.id = id;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId( Integer id )
    {
        this.id = id;
    }

    public Integer getFkProduto()
    {
        return fkProduto;
    }

    public void setFkProduto( Integer fkProduto )
    {
        this.fkProduto = fkProduto;
    }

    public String getNumeroLote()
    {
        return numeroLote;
    }

    public void setNumeroLote( String numeroLote )
    {
        this.numeroLote = numeroLote;
    }

    public Date getDataEntrada()
    {
        return dataEntrada;
    }

    public void setDataEntrada( Date dataEntrada )
    {
        this.dataEntrada = dataEntrada;
    }

    public Date getDataFabrica()
    {
        return dataFabrica;
    }

    public void setDataFabrica( Date dataFabrica )
    {
        this.dataFabrica = dataFabrica;
    }

    public Date getDataValidade()
    {
        return dataValidade;
    }

    public void setDataValidade( Date dataValidade )
    {
        this.dataValidade = dataValidade;
    }

    public int getQtd()
    {
        return qtd;
    }

    public void setQtd( int qtd )
    {
        this.qtd = qtd;
    }

    public EstadoLote getEstado()
    {
        return estado;
    }

    public void setEstado( EstadoLote estado )
    {
        this.estado = estado;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += ( id != null ? id.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if ( !( object instanceof Lote ) )
        {
            return false;
        }
        Lote other = ( Lote ) object;
        if ( ( this.id == null && other.id != null ) || ( this.id != null && !this.id.equals( other.id ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.Lote[ id=" + id + " ]";
    }

}