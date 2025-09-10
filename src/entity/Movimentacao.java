/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Engº Domingos Dala Vunge
 * @created 10/set/2025
 * @lastModified 10/set/2025
 */
@Entity
@Table( name = "movimentacao" )
@XmlRootElement
@NamedQueries(
                {
            @NamedQuery( name = "Movimentacao.findAll", query = "SELECT m FROM Movimentacao m" ),
            @NamedQuery( name = "Movimentacao.findById", query = "SELECT m FROM Movimentacao m WHERE m.id = :id" ),
            @NamedQuery( name = "Movimentacao.findByProdutoId", query = "SELECT m FROM Movimentacao m WHERE m.produtoId = :produtoId" ),
            @NamedQuery( name = "Movimentacao.findByDataMov", query = "SELECT m FROM Movimentacao m WHERE m.dataMov = :dataMov" ),
            @NamedQuery( name = "Movimentacao.findByTipo", query = "SELECT m FROM Movimentacao m WHERE m.tipo = :tipo" ),
            @NamedQuery( name = "Movimentacao.findByDocumento", query = "SELECT m FROM Movimentacao m WHERE m.documento = :documento" ),
            @NamedQuery( name = "Movimentacao.findByQuantidadeAnterior", query = "SELECT m FROM Movimentacao m WHERE m.quantidadeAnterior = :quantidadeAnterior" ),
            @NamedQuery( name = "Movimentacao.findByQuantidade", query = "SELECT m FROM Movimentacao m WHERE m.quantidade = :quantidade" ),
            @NamedQuery( name = "Movimentacao.findByQuantidadeActual", query = "SELECT m FROM Movimentacao m WHERE m.quantidadeActual = :quantidadeActual" ),
            @NamedQuery( name = "Movimentacao.findByValorUnitario", query = "SELECT m FROM Movimentacao m WHERE m.valorUnitario = :valorUnitario" ),
            @NamedQuery( name = "Movimentacao.findByValorTotal", query = "SELECT m FROM Movimentacao m WHERE m.valorTotal = :valorTotal" ),
            @NamedQuery( name = "Movimentacao.findByCustoMedio", query = "SELECT m FROM Movimentacao m WHERE m.custoMedio = :custoMedio" ),
            @NamedQuery( name = "Movimentacao.findByUsuarioId", query = "SELECT m FROM Movimentacao m WHERE m.usuarioId = :usuarioId" )
        } )
public class Movimentacao implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Basic( optional = false )
    @Column( name = "id" )
    private Integer id;
    @Basic( optional = false )
    @Column( name = "produto_id" )
    private int produtoId;
    @Basic( optional = false )
    @Column( name = "data_mov" )
    @Temporal( TemporalType.TIMESTAMP )
    private Date dataMov;
    @Basic( optional = false )
    @Column( name = "tipo" )
    private String tipo;
    @Column( name = "documento" )
    private String documento;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic( optional = false )
    @Column( name = "quantidade_anterior" )
    private BigDecimal quantidadeAnterior;
    @Basic( optional = false )
    @Column( name = "quantidade" )
    private BigDecimal quantidade;
    @Basic( optional = false )
    @Column( name = "quantidade_actual" )
    private BigDecimal quantidadeActual;
    @Basic( optional = false )
    @Column( name = "valor_unitario" )
    private BigDecimal valorUnitario;
    @Column( name = "valor_total" )
    private BigDecimal valorTotal;
    @Column( name = "custo_medio" )
    private BigDecimal custoMedio;
    @Basic( optional = false )
    @Column( name = "usuario_id" )
    private int usuarioId;
    @Basic( optional = false )
    @Column( name = "armazem_id" )
    private int armazemId;

    public Movimentacao()
    {
    }

    public Movimentacao( Integer id )
    {
        this.id = id;
    }

    public Movimentacao( Integer id, int produtoId, Date dataMov, String tipo, BigDecimal quantidadeAnterior, BigDecimal quantidade, BigDecimal quantidadeActual, BigDecimal valorUnitario, int usuarioId )
    {
        this.id = id;
        this.produtoId = produtoId;
        this.dataMov = dataMov;
        this.tipo = tipo;
        this.quantidadeAnterior = quantidadeAnterior;
        this.quantidade = quantidade;
        this.quantidadeActual = quantidadeActual;
        this.valorUnitario = valorUnitario;
        this.usuarioId = usuarioId;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId( Integer id )
    {
        this.id = id;
    }

    public int getProdutoId()
    {
        return produtoId;
    }

    public void setProdutoId( int produtoId )
    {
        this.produtoId = produtoId;
    }

    public Date getDataMov()
    {
        return dataMov;
    }

    public void setDataMov( Date dataMov )
    {
        this.dataMov = dataMov;
    }

    public String getTipo()
    {
        return tipo;
    }

    public void setTipo( String tipo )
    {
        this.tipo = tipo;
    }

    public String getDocumento()
    {
        return documento;
    }

    public void setDocumento( String documento )
    {
        this.documento = documento;
    }

    public BigDecimal getQuantidadeAnterior()
    {
        return quantidadeAnterior;
    }

    public void setQuantidadeAnterior( BigDecimal quantidadeAnterior )
    {
        this.quantidadeAnterior = quantidadeAnterior;
    }

    public BigDecimal getQuantidade()
    {
        return quantidade;
    }

    public void setQuantidade( BigDecimal quantidade )
    {
        this.quantidade = quantidade;
    }

    public BigDecimal getQuantidadeActual()
    {
        return quantidadeActual;
    }

    public void setQuantidadeActual( BigDecimal quantidadeActual )
    {
        this.quantidadeActual = quantidadeActual;
    }

    public BigDecimal getValorUnitario()
    {
        return valorUnitario;
    }

    public void setValorUnitario( BigDecimal valorUnitario )
    {
        this.valorUnitario = valorUnitario;
    }

    public BigDecimal getValorTotal()
    {
        return valorTotal;
    }

    public void setValorTotal( BigDecimal valorTotal )
    {
        this.valorTotal = valorTotal;
    }

    public BigDecimal getCustoMedio()
    {
        return custoMedio;
    }

    public void setCustoMedio( BigDecimal custoMedio )
    {
        this.custoMedio = custoMedio;
    }

    public int getUsuarioId()
    {
        return usuarioId;
    }

    public void setUsuarioId( int usuarioId )
    {
        this.usuarioId = usuarioId;
    }

    public int getArmazemId()
    {
        return armazemId;
    }

    public void setArmazemId( int armazemId )
    {
        this.armazemId = armazemId;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += ( id != null ? id.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if ( !( object instanceof Movimentacao ) )
        {
            return false;
        }
        Movimentacao other = (Movimentacao) object;
        if ( ( this.id == null && other.id != null ) || ( this.id != null && !this.id.equals( other.id ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.Movimentacao[ id=" + id + " ]";
    }

}
