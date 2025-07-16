/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author lenovo
 */
@Entity
@Table(name = "itens_nota")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "ItensNota.findAll", query = "SELECT i FROM ItensNota i"),
    @NamedQuery(name = "ItensNota.findByPkItensNota", query = "SELECT i FROM ItensNota i WHERE i.pkItensNota = :pkItensNota"),
    @NamedQuery(name = "ItensNota.findByDescricao", query = "SELECT i FROM ItensNota i WHERE i.descricao = :descricao"),
    @NamedQuery(name = "ItensNota.findByValor", query = "SELECT i FROM ItensNota i WHERE i.valor = :valor")
})
public class ItensNota implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pk_itens_nota")
    private Integer pkItensNota;
    @Column(name = "descricao")
    private String descricao;
    @Column(name = "valor")
    private String valor;
    @JoinColumn(name = "fk_nota", referencedColumnName = "pk_nota")
    @ManyToOne(optional = false)
    private Notas fkNota;

    public ItensNota()
    {
    }

    public ItensNota( Integer pkItensNota )
    {
        this.pkItensNota = pkItensNota;
    }

    public Integer getPkItensNota()
    {
        return pkItensNota;
    }

    public void setPkItensNota( Integer pkItensNota )
    {
        this.pkItensNota = pkItensNota;
    }

    public String getDescricao()
    {
        return descricao;
    }

    public void setDescricao( String descricao )
    {
        this.descricao = descricao;
    }

    public String getValor()
    {
        return valor;
    }

    public void setValor( String valor )
    {
        this.valor = valor;
    }

    public Notas getFkNota()
    {
        return fkNota;
    }

    public void setFkNota( Notas fkNota )
    {
        this.fkNota = fkNota;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += ( pkItensNota != null ? pkItensNota.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if ( !( object instanceof ItensNota ) )
        {
            return false;
        }
        ItensNota other = ( ItensNota ) object;
        if ( ( this.pkItensNota == null && other.pkItensNota != null ) || ( this.pkItensNota != null && !this.pkItensNota.equals( other.pkItensNota ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.ItensNota[ pkItensNota=" + pkItensNota + " ]";
    }
    
}
