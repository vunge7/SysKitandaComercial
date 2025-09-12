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
@Table( name = "tb_item_saidas" )
@XmlRootElement
@NamedQueries(
                {
            @NamedQuery( name = "TbItemSaidas.findAll", query = "SELECT t FROM TbItemSaidas t" ),
            @NamedQuery( name = "TbItemSaidas.findByCodigo", query = "SELECT t FROM TbItemSaidas t WHERE t.codigo = :codigo" ),
            @NamedQuery( name = "TbItemSaidas.findByQuantidade", query = "SELECT t FROM TbItemSaidas t WHERE t.quantidade = :quantidade" )
        } )
public class TbItemSaidas implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Basic( optional = false )
    @Column( name = "codigo" )
    private Integer codigo;
    @Basic( optional = false )
    @Column( name = "quantidade" )
    private double quantidade;
    @Basic( optional = false )
    @Column( name = "preco_compra" )
    private BigDecimal precoCompra;
    @JoinColumn( name = "fk_produtos", referencedColumnName = "codigo" )
    @ManyToOne( optional = false )
    private TbProduto fkProdutos;
    @JoinColumn( name = "fk_saidas_produtos", referencedColumnName = "pk_saidas_produtos" )
    @ManyToOne( optional = false )
    private TbSaidasProdutos fkSaidasProdutos;

    public TbItemSaidas()
    {
    }

    public TbItemSaidas( Integer codigo )
    {
        this.codigo = codigo;
    }

    public TbItemSaidas( Integer codigo, double quantidade )
    {
        this.codigo = codigo;
        this.quantidade = quantidade;
    }

    public Integer getCodigo()
    {
        return codigo;
    }

    public void setCodigo( Integer codigo )
    {
        this.codigo = codigo;
    }

    public double getQuantidade()
    {
        return quantidade;
    }

    public void setQuantidade( double quantidade )
    {
        this.quantidade = quantidade;
    }

    public TbProduto getFkProdutos()
    {
        return fkProdutos;
    }

    public void setFkProdutos( TbProduto fkProdutos )
    {
        this.fkProdutos = fkProdutos;
    }

    public TbSaidasProdutos getFkSaidasProdutos()
    {
        return fkSaidasProdutos;
    }

    public void setFkSaidasProdutos( TbSaidasProdutos fkSaidasProdutos )
    {
        this.fkSaidasProdutos = fkSaidasProdutos;
    }

    public BigDecimal getPrecoCompra()
    {
        return precoCompra;
    }

    public void setPrecoCompra( BigDecimal precoCompra )
    {
        this.precoCompra = precoCompra;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += ( codigo != null ? codigo.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if ( !( object instanceof TbItemSaidas ) )
        {
            return false;
        }
        TbItemSaidas other = (TbItemSaidas) object;
        if ( ( this.codigo == null && other.codigo != null ) || ( this.codigo != null && !this.codigo.equals( other.codigo ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.TbItemSaidas[ codigo=" + codigo + " ]";
    }

}
