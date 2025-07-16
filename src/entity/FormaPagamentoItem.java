/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
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
@Table(name = "forma_pagamento_item")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "FormaPagamentoItem.findAll", query = "SELECT f FROM FormaPagamentoItem f"),
    @NamedQuery(name = "FormaPagamentoItem.findByPkFormaPagamentoItem", query = "SELECT f FROM FormaPagamentoItem f WHERE f.pkFormaPagamentoItem = :pkFormaPagamentoItem"),
    @NamedQuery(name = "FormaPagamentoItem.findByValor", query = "SELECT f FROM FormaPagamentoItem f WHERE f.valor = :valor"),
    @NamedQuery(name = "FormaPagamentoItem.findByReferencia", query = "SELECT f FROM FormaPagamentoItem f WHERE f.referencia = :referencia")
})
public class FormaPagamentoItem implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pk_forma_pagamento_item")
    private Integer pkFormaPagamentoItem;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor")
    private BigDecimal valor;
    @Column(name = "referencia")
    private String referencia;
    @Column(name = "troco")
    private BigDecimal troco;
    @Column(name = "valor_real")
    private BigDecimal valor_real;
    @JoinColumn(name = "fk_forma_pagamento", referencedColumnName = "pk_forma_pagamento")
    @ManyToOne(optional = false)
    private FormaPagamento fkFormaPagamento;
    @JoinColumn(name = "fk_venda", referencedColumnName = "codigo")
    @ManyToOne
    private TbVenda fkVenda;

    public FormaPagamentoItem()
    {
    }

    public BigDecimal getTroco()
    {
        return troco;
    }

    public void setTroco( BigDecimal troco )
    {
        this.troco = troco;
    }

    public BigDecimal getValor_real()
    {
        return valor_real;
    }

    public void setValor_real( BigDecimal valor_real )
    {
        this.valor_real = valor_real;
    }

    public FormaPagamentoItem( Integer pkFormaPagamentoItem )
    {
        this.pkFormaPagamentoItem = pkFormaPagamentoItem;
    }

    public Integer getPkFormaPagamentoItem()
    {
        return pkFormaPagamentoItem;
    }

    public void setPkFormaPagamentoItem( Integer pkFormaPagamentoItem )
    {
        this.pkFormaPagamentoItem = pkFormaPagamentoItem;
    }

    public BigDecimal getValor()
    {
        return valor;
    }

    public void setValor( BigDecimal valor )
    {
        this.valor = valor;
    }

    public String getReferencia()
    {
        return referencia;
    }

    public void setReferencia( String referencia )
    {
        this.referencia = referencia;
    }

    public FormaPagamento getFkFormaPagamento()
    {
        return fkFormaPagamento;
    }

    public void setFkFormaPagamento( FormaPagamento fkFormaPagamento )
    {
        this.fkFormaPagamento = fkFormaPagamento;
    }

    public TbVenda getFkVenda()
    {
        return fkVenda;
    }

    public void setFkVenda( TbVenda fkVenda )
    {
        this.fkVenda = fkVenda;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += ( pkFormaPagamentoItem != null ? pkFormaPagamentoItem.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if ( !( object instanceof FormaPagamentoItem ) )
        {
            return false;
        }
        FormaPagamentoItem other = ( FormaPagamentoItem ) object;
        if ( ( this.pkFormaPagamentoItem == null && other.pkFormaPagamentoItem != null ) || ( this.pkFormaPagamentoItem != null && !this.pkFormaPagamentoItem.equals( other.pkFormaPagamentoItem ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.FormaPagamentoItem[ pkFormaPagamentoItem=" + pkFormaPagamentoItem + " ]";
    }
    
}
