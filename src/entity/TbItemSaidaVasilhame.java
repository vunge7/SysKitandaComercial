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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author lenovo
 */
@Entity
@Table(name = "tb_item_saida_vasilhame")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "TbItemSaidaVasilhame.findAll", query = "SELECT t FROM TbItemSaidaVasilhame t"),
    @NamedQuery(name = "TbItemSaidaVasilhame.findByPkItemSaidaVasilhame", query = "SELECT t FROM TbItemSaidaVasilhame t WHERE t.pkItemSaidaVasilhame = :pkItemSaidaVasilhame"),
    @NamedQuery(name = "TbItemSaidaVasilhame.findByFkVasihame", query = "SELECT t FROM TbItemSaidaVasilhame t WHERE t.fkVasihame = :fkVasihame"),
    @NamedQuery(name = "TbItemSaidaVasilhame.findByFkSaidaVazilhame", query = "SELECT t FROM TbItemSaidaVasilhame t WHERE t.fkSaidaVazilhame = :fkSaidaVazilhame"),
    @NamedQuery(name = "TbItemSaidaVasilhame.findByQtd", query = "SELECT t FROM TbItemSaidaVasilhame t WHERE t.qtd = :qtd")
})
public class TbItemSaidaVasilhame implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pk_item_saida_vasilhame")
    private Integer pkItemSaidaVasilhame;
    @Column(name = "fk_vasihame")
    private Integer fkVasihame;
    @Column(name = "fk_saida_vazilhame")
    private Integer fkSaidaVazilhame;
    @Column(name = "qtd")
    private Integer qtd;

    public TbItemSaidaVasilhame()
    {
    }

    public TbItemSaidaVasilhame( Integer pkItemSaidaVasilhame )
    {
        this.pkItemSaidaVasilhame = pkItemSaidaVasilhame;
    }

    public Integer getPkItemSaidaVasilhame()
    {
        return pkItemSaidaVasilhame;
    }

    public void setPkItemSaidaVasilhame( Integer pkItemSaidaVasilhame )
    {
        this.pkItemSaidaVasilhame = pkItemSaidaVasilhame;
    }

    public Integer getFkVasihame()
    {
        return fkVasihame;
    }

    public void setFkVasihame( Integer fkVasihame )
    {
        this.fkVasihame = fkVasihame;
    }

    public Integer getFkSaidaVazilhame()
    {
        return fkSaidaVazilhame;
    }

    public void setFkSaidaVazilhame( Integer fkSaidaVazilhame )
    {
        this.fkSaidaVazilhame = fkSaidaVazilhame;
    }

    public Integer getQtd()
    {
        return qtd;
    }

    public void setQtd( Integer qtd )
    {
        this.qtd = qtd;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += ( pkItemSaidaVasilhame != null ? pkItemSaidaVasilhame.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if ( !( object instanceof TbItemSaidaVasilhame ) )
        {
            return false;
        }
        TbItemSaidaVasilhame other = ( TbItemSaidaVasilhame ) object;
        if ( ( this.pkItemSaidaVasilhame == null && other.pkItemSaidaVasilhame != null ) || ( this.pkItemSaidaVasilhame != null && !this.pkItemSaidaVasilhame.equals( other.pkItemSaidaVasilhame ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.TbItemSaidaVasilhame[ pkItemSaidaVasilhame=" + pkItemSaidaVasilhame + " ]";
    }
    
}
