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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author marti
 */
@Entity
@Table( name = "linha_ficha_tecnica" )
@XmlRootElement
@NamedQueries( 
{
    @NamedQuery( name = "LinhaFichaTecnica.findAll", query = "SELECT l FROM LinhaFichaTecnica l" ),
    @NamedQuery( name = "LinhaFichaTecnica.findById", query = "SELECT l FROM LinhaFichaTecnica l WHERE l.id = :id" ),
    @NamedQuery( name = "LinhaFichaTecnica.findByIgrendienteId", query = "SELECT l FROM LinhaFichaTecnica l WHERE l.igrendienteId = :igrendienteId" ),
    @NamedQuery( name = "LinhaFichaTecnica.findByIgrendienteDesignacao", query = "SELECT l FROM LinhaFichaTecnica l WHERE l.igrendienteDesignacao = :igrendienteDesignacao" ),
    @NamedQuery( name = "LinhaFichaTecnica.findByUnidade", query = "SELECT l FROM LinhaFichaTecnica l WHERE l.unidade = :unidade" ),
    @NamedQuery( name = "LinhaFichaTecnica.findByPrecoUnitario", query = "SELECT l FROM LinhaFichaTecnica l WHERE l.precoUnitario = :precoUnitario" ),
    @NamedQuery( name = "LinhaFichaTecnica.findByQtdBruto", query = "SELECT l FROM LinhaFichaTecnica l WHERE l.qtdBruto = :qtdBruto" ),
    @NamedQuery( name = "LinhaFichaTecnica.findByQtdLiquido", query = "SELECT l FROM LinhaFichaTecnica l WHERE l.qtdLiquido = :qtdLiquido" ),
    @NamedQuery( name = "LinhaFichaTecnica.findByFactorCorrecao", query = "SELECT l FROM LinhaFichaTecnica l WHERE l.factorCorrecao = :factorCorrecao" ),
    @NamedQuery( name = "LinhaFichaTecnica.findByCustoTotal", query = "SELECT l FROM LinhaFichaTecnica l WHERE l.custoTotal = :custoTotal" ),
    @NamedQuery( name = "LinhaFichaTecnica.findByFichaTecnicaId", query = "SELECT l FROM LinhaFichaTecnica l WHERE l.fichaTecnicaId = :fichaTecnicaId" )
} )
public class LinhaFichaTecnica implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Basic( optional = false )
    @Column( name = "id" )
    private Integer id;
    @Column( name = "igrendiente_id" )
    private Integer igrendienteId;
    @Column( name = "igrendiente_designacao" )
    private String igrendienteDesignacao;
    @Column( name = "unidade" )
    private String unidade;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column( name = "preco_unitario" )
    private BigDecimal precoUnitario;
    @Column( name = "qtd_bruto" )
    private Double qtdBruto;
    @Column( name = "qtd_liquido" )
    private Double qtdLiquido;
    @Column( name = "factor_correcao" )
    private Double factorCorrecao;
    @Column( name = "unidade_compra" )
    private Double unidade_compra;
    @Column( name = "custo_total" )
    private BigDecimal custoTotal;
    @Column( name = "ficha_tecnica_id" )
    private Integer fichaTecnicaId;

    public Double getUnidade_compra()
    {
        return unidade_compra;
    }

    public void setUnidade_compra( Double unidade_compra )
    {
        this.unidade_compra = unidade_compra;
    }

    public LinhaFichaTecnica()
    {
    }

    public LinhaFichaTecnica( Integer id )
    {
        this.id = id;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId( Integer id )
    {
        this.id = id;
    }

    public Integer getIgrendienteId()
    {
        return igrendienteId;
    }

    public void setIgrendienteId( Integer igrendienteId )
    {
        this.igrendienteId = igrendienteId;
    }

    public String getIgrendienteDesignacao()
    {
        return igrendienteDesignacao;
    }

    public void setIgrendienteDesignacao( String igrendienteDesignacao )
    {
        this.igrendienteDesignacao = igrendienteDesignacao;
    }

    public String getUnidade()
    {
        return unidade;
    }

    public void setUnidade( String unidade )
    {
        this.unidade = unidade;
    }

    public BigDecimal getPrecoUnitario()
    {
        return precoUnitario;
    }

    public void setPrecoUnitario( BigDecimal precoUnitario )
    {
        this.precoUnitario = precoUnitario;
    }

    public Double getQtdBruto()
    {
        return qtdBruto;
    }

    public void setQtdBruto( Double qtdBruto )
    {
        this.qtdBruto = qtdBruto;
    }

    public Double getQtdLiquido()
    {
        return qtdLiquido;
    }

    public void setQtdLiquido( Double qtdLiquido )
    {
        this.qtdLiquido = qtdLiquido;
    }

    public Double getFactorCorrecao()
    {
        return factorCorrecao;
    }

    public void setFactorCorrecao( Double factorCorrecao )
    {
        this.factorCorrecao = factorCorrecao;
    }

    public BigDecimal getCustoTotal()
    {
        return custoTotal;
    }

    public void setCustoTotal( BigDecimal custoTotal )
    {
        this.custoTotal = custoTotal;
    }

    public Integer getFichaTecnicaId()
    {
        return fichaTecnicaId;
    }

    public void setFichaTecnicaId( Integer fichaTecnicaId )
    {
        this.fichaTecnicaId = fichaTecnicaId;
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
        if ( !( object instanceof LinhaFichaTecnica ) )
        {
            return false;
        }
        LinhaFichaTecnica other = (LinhaFichaTecnica) object;
        if ( ( this.id == null && other.id != null ) || ( this.id != null && !this.id.equals( other.id ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.LinhaFichaTecnica[ id=" + id + " ]";
    }
    
}
