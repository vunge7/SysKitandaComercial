/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

/**
 *
 * @author DELL
 */

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
@Table(name = "lote_movimento")
@XmlRootElement
@NamedQueries(
        {
            @NamedQuery(name = "LoteMovimento.findAll", query = "SELECT m FROM LoteMovimento m"),
            @NamedQuery(name = "LoteMovimento.findById", query = "SELECT m FROM LoteMovimento m WHERE m.id = :id"),
            @NamedQuery(name = "LoteMovimento.findByFkLote", query = "SELECT m FROM LoteMovimento m WHERE m.fkLote = :fkLote"),
            @NamedQuery(name = "LoteMovimento.findByFkVenda", query = "SELECT m FROM LoteMovimento m WHERE m.fkVenda = :fkVenda"),
            @NamedQuery(name = "LoteMovimento.findByTipo", query = "SELECT m FROM LoteMovimento m WHERE m.tipo = :tipo")
        })
public class LoteMovimento implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "fk_lote")
    private Integer fkLote;
    @Basic(optional = false)
    @Column(name = "fk_venda")
    private Integer fkVenda;
    @Basic(optional = false)
    @Column(name = "tipo")
    private String tipo; // VENDA | DEVOLUCAO
    @Basic(optional = false)
    @Column(name = "qtd")
    private int qtd;
    @Column(name = "qtd_devolvida")
    private int qtdDevolvida;
    @Column(name = "data_mov")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataMov;

    public LoteMovimento()
    {
    }

    public LoteMovimento( Integer id )
    {
        this.id = id;
    }

    public LoteMovimento( Integer fkLote, Integer fkVenda, String tipo, int qtd )
    {
        this.fkLote = fkLote;
        this.fkVenda = fkVenda;
        this.tipo = tipo;
        this.qtd = qtd;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId( Integer id )
    {
        this.id = id;
    }

    public Integer getFkLote()
    {
        return fkLote;
    }

    public void setFkLote( Integer fkLote )
    {
        this.fkLote = fkLote;
    }

    public Integer getFkVenda()
    {
        return fkVenda;
    }

    public void setFkVenda( Integer fkVenda )
    {
        this.fkVenda = fkVenda;
    }

    public String getTipo()
    {
        return tipo;
    }

    public void setTipo( String tipo )
    {
        this.tipo = tipo;
    }

    public int getQtd()
    {
        return qtd;
    }

    public void setQtd( int qtd )
    {
        this.qtd = qtd;
    }

    public int getQtdDevolvida()
    {
        return qtdDevolvida;
    }

    public void setQtdDevolvida( int qtdDevolvida )
    {
        this.qtdDevolvida = qtdDevolvida;
    }

    public Date getDataMov()
    {
        return dataMov;
    }

    public void setDataMov( Date dataMov )
    {
        this.dataMov = dataMov;
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
        if ( !( object instanceof LoteMovimento ) )
        {
            return false;
        }
        LoteMovimento other = ( LoteMovimento ) object;
        if ( ( this.id == null && other.id != null ) || ( this.id != null && !this.id.equals( other.id ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.LoteMovimento[ id=" + id + " ]";
    }

}