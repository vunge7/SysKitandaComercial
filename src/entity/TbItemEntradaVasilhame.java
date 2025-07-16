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
@Table(name = "tb_item_entrada_vasilhame")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "TbItemEntradaVasilhame.findAll", query = "SELECT t FROM TbItemEntradaVasilhame t"),
    @NamedQuery(name = "TbItemEntradaVasilhame.findByPkItemEntradaVasilhame", query = "SELECT t FROM TbItemEntradaVasilhame t WHERE t.pkItemEntradaVasilhame = :pkItemEntradaVasilhame"),
    @NamedQuery(name = "TbItemEntradaVasilhame.findByQtd", query = "SELECT t FROM TbItemEntradaVasilhame t WHERE t.qtd = :qtd")
})
public class TbItemEntradaVasilhame implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pk_item_entrada_vasilhame")
    private Integer pkItemEntradaVasilhame;
    @Basic(optional = false)
    @Column(name = "qtd")
    private int qtd;
    @JoinColumn(name = "fk_entrada_vasilhame", referencedColumnName = "pk_entrada_vasilhame")
    @ManyToOne(optional = false)
    private TbEntradaVasilhame fkEntradaVasilhame;
    @JoinColumn(name = "fk_vasilhame", referencedColumnName = "pk_vasilhame")
    @ManyToOne(optional = false)
    private TbVasilhame fkVasilhame;

    public TbItemEntradaVasilhame()
    {
    }

    public TbItemEntradaVasilhame( Integer pkItemEntradaVasilhame )
    {
        this.pkItemEntradaVasilhame = pkItemEntradaVasilhame;
    }

    public TbItemEntradaVasilhame( Integer pkItemEntradaVasilhame, int qtd )
    {
        this.pkItemEntradaVasilhame = pkItemEntradaVasilhame;
        this.qtd = qtd;
    }

    public Integer getPkItemEntradaVasilhame()
    {
        return pkItemEntradaVasilhame;
    }

    public void setPkItemEntradaVasilhame( Integer pkItemEntradaVasilhame )
    {
        this.pkItemEntradaVasilhame = pkItemEntradaVasilhame;
    }

    public int getQtd()
    {
        return qtd;
    }

    public void setQtd( int qtd )
    {
        this.qtd = qtd;
    }

    public TbEntradaVasilhame getFkEntradaVasilhame()
    {
        return fkEntradaVasilhame;
    }

    public void setFkEntradaVasilhame( TbEntradaVasilhame fkEntradaVasilhame )
    {
        this.fkEntradaVasilhame = fkEntradaVasilhame;
    }

    public TbVasilhame getFkVasilhame()
    {
        return fkVasilhame;
    }

    public void setFkVasilhame( TbVasilhame fkVasilhame )
    {
        this.fkVasilhame = fkVasilhame;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += ( pkItemEntradaVasilhame != null ? pkItemEntradaVasilhame.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if ( !( object instanceof TbItemEntradaVasilhame ) )
        {
            return false;
        }
        TbItemEntradaVasilhame other = ( TbItemEntradaVasilhame ) object;
        if ( ( this.pkItemEntradaVasilhame == null && other.pkItemEntradaVasilhame != null ) || ( this.pkItemEntradaVasilhame != null && !this.pkItemEntradaVasilhame.equals( other.pkItemEntradaVasilhame ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.TbItemEntradaVasilhame[ pkItemEntradaVasilhame=" + pkItemEntradaVasilhame + " ]";
    }
    
}
