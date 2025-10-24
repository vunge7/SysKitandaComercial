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
@Table(name = "tb_pagamento_credito")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "TbPagamentoCredito.findAll", query = "SELECT t FROM TbPagamentoCredito t"),
    @NamedQuery(name = "TbPagamentoCredito.findByCodigo", query = "SELECT t FROM TbPagamentoCredito t WHERE t.codigo = :codigo"),
    @NamedQuery(name = "TbPagamentoCredito.findByDataPagamento", query = "SELECT t FROM TbPagamentoCredito t WHERE t.dataPagamento = :dataPagamento"),
    @NamedQuery(name = "TbPagamentoCredito.findByValor", query = "SELECT t FROM TbPagamentoCredito t WHERE t.valor = :valor")
})
public class TbPagamentoCredito implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "codigo")
    private Integer codigo;
    @Basic(optional = false)
    @Column(name = "data_pagamento")
    private double dataPagamento;
    @Basic(optional = false)
    @Column(name = "valor")
    private double valor;
    @JoinColumn(name = "codigo_venda", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private TbVenda codigoVenda;

    public TbPagamentoCredito()
    {
    }

    public TbPagamentoCredito( Integer codigo )
    {
        this.codigo = codigo;
    }

    public TbPagamentoCredito( Integer codigo, double dataPagamento, double valor )
    {
        this.codigo = codigo;
        this.dataPagamento = dataPagamento;
        this.valor = valor;
    }

    public Integer getCodigo()
    {
        return codigo;
    }

    public void setCodigo( Integer codigo )
    {
        this.codigo = codigo;
    }

    public double getDataPagamento()
    {
        return dataPagamento;
    }

    public void setDataPagamento( double dataPagamento )
    {
        this.dataPagamento = dataPagamento;
    }

    public double getValor()
    {
        return valor;
    }

    public void setValor( double valor )
    {
        this.valor = valor;
    }

    public TbVenda getCodigoVenda()
    {
        return codigoVenda;
    }

    public void setCodigoVenda( TbVenda codigoVenda )
    {
        this.codigoVenda = codigoVenda;
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
        if ( !( object instanceof TbPagamentoCredito ) )
        {
            return false;
        }
        TbPagamentoCredito other = ( TbPagamentoCredito ) object;
        if ( ( this.codigo == null && other.codigo != null ) || ( this.codigo != null && !this.codigo.equals( other.codigo ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.TbPagamentoCredito[ codigo=" + codigo + " ]";
    }
    
}
