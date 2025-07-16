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
@Table(name = "tb_forma_pagamento")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "TbFormaPagamento.findAll", query = "SELECT t FROM TbFormaPagamento t"),
    @NamedQuery(name = "TbFormaPagamento.findByPkFormaPagamento", query = "SELECT t FROM TbFormaPagamento t WHERE t.pkFormaPagamento = :pkFormaPagamento"),
    @NamedQuery(name = "TbFormaPagamento.findByDescricao", query = "SELECT t FROM TbFormaPagamento t WHERE t.descricao = :descricao")
})
public class TbFormaPagamento implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pk_forma_pagamento")
    private Integer pkFormaPagamento;
    @Basic(optional = false)
    @Column(name = "descricao")
    private String descricao;

    public TbFormaPagamento()
    {
    }

    public TbFormaPagamento( Integer pkFormaPagamento )
    {
        this.pkFormaPagamento = pkFormaPagamento;
    }

    public TbFormaPagamento( Integer pkFormaPagamento, String descricao )
    {
        this.pkFormaPagamento = pkFormaPagamento;
        this.descricao = descricao;
    }

    public Integer getPkFormaPagamento()
    {
        return pkFormaPagamento;
    }

    public void setPkFormaPagamento( Integer pkFormaPagamento )
    {
        this.pkFormaPagamento = pkFormaPagamento;
    }

    public String getDescricao()
    {
        return descricao;
    }

    public void setDescricao( String descricao )
    {
        this.descricao = descricao;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += ( pkFormaPagamento != null ? pkFormaPagamento.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if ( !( object instanceof TbFormaPagamento ) )
        {
            return false;
        }
        TbFormaPagamento other = ( TbFormaPagamento ) object;
        if ( ( this.pkFormaPagamento == null && other.pkFormaPagamento != null ) || ( this.pkFormaPagamento != null && !this.pkFormaPagamento.equals( other.pkFormaPagamento ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.TbFormaPagamento[ pkFormaPagamento=" + pkFormaPagamento + " ]";
    }
    
}
