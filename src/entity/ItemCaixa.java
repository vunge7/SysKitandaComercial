/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;


import java.sql.Connection;
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
@Table(name = "item_caixa")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "ItemCaixa.findAll", query = "SELECT i FROM ItemCaixa i"),
    @NamedQuery(name = "ItemCaixa.findByPkItemCaixa", query = "SELECT i FROM ItemCaixa i WHERE i.pkItemCaixa = :pkItemCaixa"),
    @NamedQuery(name = "ItemCaixa.findByValorDeclarado", query = "SELECT i FROM ItemCaixa i WHERE i.valorDeclarado = :valorDeclarado"),
    @NamedQuery(name = "ItemCaixa.findByValorReal", query = "SELECT i FROM ItemCaixa i WHERE i.valorReal = :valorReal")
})
public class ItemCaixa implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pk_item_caixa")
    private Integer pkItemCaixa;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor_declarado")
    private Double valorDeclarado;
    @Column(name = "valor_real")
    private Double valorReal;
    @JoinColumn(name = "fk_caixa", referencedColumnName = "pk_caixa")
    @ManyToOne(optional = false)
    private Caixa fkCaixa;
    @JoinColumn(name = "fk_forma_pagamento", referencedColumnName = "pk_forma_pagamento")
    @ManyToOne
    private FormaPagamento fkFormaPagamento;

    public ItemCaixa()
    {
    }

    public ItemCaixa( Integer pkItemCaixa )
    {
        this.pkItemCaixa = pkItemCaixa;
    }

    public Integer getPkItemCaixa()
    {
        return pkItemCaixa;
    }

    public void setPkItemCaixa( Integer pkItemCaixa )
    {
        this.pkItemCaixa = pkItemCaixa;
    }

    public Double getValorDeclarado()
    {
        return valorDeclarado;
    }

    public void setValorDeclarado( Double valorDeclarado )
    {
        this.valorDeclarado = valorDeclarado;
    }

    public Double getValorReal()
    {
        return valorReal;
    }

    public void setValorReal( Double valorReal )
    {
        this.valorReal = valorReal;
    }

    public Caixa getFkCaixa()
    {
        return fkCaixa;
    }

    public void setFkCaixa( Caixa fkCaixa )
    {
        this.fkCaixa = fkCaixa;
    }

    public FormaPagamento getFkFormaPagamento()
    {
        return fkFormaPagamento;
    }

    public void setFkFormaPagamento( FormaPagamento fkFormaPagamento )
    {
        this.fkFormaPagamento = fkFormaPagamento;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += ( pkItemCaixa != null ? pkItemCaixa.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if ( !( object instanceof ItemCaixa ) )
        {
            return false;
        }
        ItemCaixa other = ( ItemCaixa ) object;
        if ( ( this.pkItemCaixa == null && other.pkItemCaixa != null ) || ( this.pkItemCaixa != null && !this.pkItemCaixa.equals( other.pkItemCaixa ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.ItemCaixa[ pkItemCaixa=" + pkItemCaixa + " ]";
    }
    
}
