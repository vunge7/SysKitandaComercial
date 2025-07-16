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
 * @author Domingos Dala Vunge
 */
@Entity
@Table(name = "tipo_conta")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "TipoConta.findAll", query = "SELECT t FROM TipoConta t"),
    @NamedQuery(name = "TipoConta.findByPkTipoConta", query = "SELECT t FROM TipoConta t WHERE t.pkTipoConta = :pkTipoConta"),
    @NamedQuery(name = "TipoConta.findByDesignacao", query = "SELECT t FROM TipoConta t WHERE t.designacao = :designacao")
})
public class TipoConta implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pk_tipo_conta")
    private Integer pkTipoConta;
    @Basic(optional = false)
    @Column(name = "designacao")
    private String designacao;

    public TipoConta()
    {
    }

    public TipoConta( Integer pkTipoConta )
    {
        this.pkTipoConta = pkTipoConta;
    }

    public TipoConta( Integer pkTipoConta, String designacao )
    {
        this.pkTipoConta = pkTipoConta;
        this.designacao = designacao;
    }

    public Integer getPkTipoConta()
    {
        return pkTipoConta;
    }

    public void setPkTipoConta( Integer pkTipoConta )
    {
        this.pkTipoConta = pkTipoConta;
    }

    public String getDesignacao()
    {
        return designacao;
    }

    public void setDesignacao( String designacao )
    {
        this.designacao = designacao;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += ( pkTipoConta != null ? pkTipoConta.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if ( !( object instanceof TipoConta ) )
        {
            return false;
        }
        TipoConta other = ( TipoConta ) object;
        if ( ( this.pkTipoConta == null && other.pkTipoConta != null ) || ( this.pkTipoConta != null && !this.pkTipoConta.equals( other.pkTipoConta ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.TipoConta[ pkTipoConta=" + pkTipoConta + " ]";
    }
    
}
