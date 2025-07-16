/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author lenovo
 */
@Entity
@Table(name = "forma_pagamento")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "FormaPagamento.findAll", query = "SELECT f FROM FormaPagamento f"),
    @NamedQuery(name = "FormaPagamento.findByPkFormaPagamento", query = "SELECT f FROM FormaPagamento f WHERE f.pkFormaPagamento = :pkFormaPagamento"),
    @NamedQuery(name = "FormaPagamento.findByDesignacao", query = "SELECT f FROM FormaPagamento f WHERE f.designacao = :designacao")
})
public class FormaPagamento implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pk_forma_pagamento")
    private Integer pkFormaPagamento;
    @Column(name = "designacao")
    private String designacao;
    @Column(name = "fk_conta_associada")
    private int fkContaAssociada;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkFormaPagamento")
    private List<ReciboRh> reciboRhList;
    @OneToMany(mappedBy = "fkFormaPagamento")
    private List<ItemCaixa> itemCaixaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkFormaPagamento")
    private List<FormaPagamentoItem> formaPagamentoItemList;

    public FormaPagamento()
    {
    }

    public FormaPagamento( Integer pkFormaPagamento )
    {
        this.pkFormaPagamento = pkFormaPagamento;
    }

    public Integer getPkFormaPagamento()
    {
        return pkFormaPagamento;
    }

    public int getFkContaAssociada()
    {
        return fkContaAssociada;
    }

    public void setFkContaAssociada( int fkContaAssociada )
    {
        this.fkContaAssociada = fkContaAssociada;
    }

    public void setPkFormaPagamento( Integer pkFormaPagamento )
    {
        this.pkFormaPagamento = pkFormaPagamento;
    }

    public String getDesignacao()
    {
        return designacao;
    }

    public void setDesignacao( String designacao )
    {
        this.designacao = designacao;
    }

    @XmlTransient
    public List<ReciboRh> getReciboRhList()
    {
        return reciboRhList;
    }

    public void setReciboRhList( List<ReciboRh> reciboRhList )
    {
        this.reciboRhList = reciboRhList;
    }

    @XmlTransient
    public List<ItemCaixa> getItemCaixaList()
    {
        return itemCaixaList;
    }

    public void setItemCaixaList( List<ItemCaixa> itemCaixaList )
    {
        this.itemCaixaList = itemCaixaList;
    }

    @XmlTransient
    public List<FormaPagamentoItem> getFormaPagamentoItemList()
    {
        return formaPagamentoItemList;
    }

    public void setFormaPagamentoItemList( List<FormaPagamentoItem> formaPagamentoItemList )
    {
        this.formaPagamentoItemList = formaPagamentoItemList;
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
        if ( !( object instanceof FormaPagamento ) )
        {
            return false;
        }
        FormaPagamento other = ( FormaPagamento ) object;
        if ( ( this.pkFormaPagamento == null && other.pkFormaPagamento != null ) || ( this.pkFormaPagamento != null && !this.pkFormaPagamento.equals( other.pkFormaPagamento ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.FormaPagamento[ pkFormaPagamento=" + pkFormaPagamento + " ]";
    }
    
}
