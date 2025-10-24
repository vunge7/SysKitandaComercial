/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;


import java.sql.Connection;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author marti
 */
@Entity
@Table( name = "ficha_tecnica" )
@XmlRootElement
@NamedQueries( 
{
    @NamedQuery( name = "FichaTecnica.findAll", query = "SELECT f FROM FichaTecnica f" ),
    @NamedQuery( name = "FichaTecnica.findById", query = "SELECT f FROM FichaTecnica f WHERE f.id = :id" ),
    @NamedQuery( name = "FichaTecnica.findByProdutoId", query = "SELECT f FROM FichaTecnica f WHERE f.produtoId = :produtoId" ),
    @NamedQuery( name = "FichaTecnica.findByDataCriacao", query = "SELECT f FROM FichaTecnica f WHERE f.dataCriacao = :dataCriacao" ),
    @NamedQuery( name = "FichaTecnica.findByUsuarioIdCriacao", query = "SELECT f FROM FichaTecnica f WHERE f.usuarioIdCriacao = :usuarioIdCriacao" ),
    @NamedQuery( name = "FichaTecnica.findByDataActualizacao", query = "SELECT f FROM FichaTecnica f WHERE f.dataActualizacao = :dataActualizacao" ),
    @NamedQuery( name = "FichaTecnica.findByUsuarioIdActualizacao", query = "SELECT f FROM FichaTecnica f WHERE f.usuarioIdActualizacao = :usuarioIdActualizacao" ),
    @NamedQuery( name = "FichaTecnica.findByCustoProduto", query = "SELECT f FROM FichaTecnica f WHERE f.custoProduto = :custoProduto" ),
    @NamedQuery( name = "FichaTecnica.findByPercentagemGanho", query = "SELECT f FROM FichaTecnica f WHERE f.percentagemGanho = :percentagemGanho" ),
    @NamedQuery( name = "FichaTecnica.findByCustoVenda", query = "SELECT f FROM FichaTecnica f WHERE f.custoVenda = :custoVenda" )
} )
public class FichaTecnica implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Basic( optional = false )
    @Column( name = "id" )
    private Integer id;
    @Column( name = "produto_id" )
    private Integer produtoId;
    @Column( name = "prato" )
    private String prato;
    @Column( name = "data_criacao" )
    @Temporal( TemporalType.TIMESTAMP )
    private Date dataCriacao;
    @Column( name = "usuario_id_criacao" )
    private Integer usuarioIdCriacao;
    @Column( name = "data_actualizacao" )
    @Temporal( TemporalType.TIMESTAMP )
    private Date dataActualizacao;
    @Column( name = "usuario_id_actualizacao" )
    private Integer usuarioIdActualizacao;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column( name = "custo_produto" )
    private BigDecimal custoProduto;
    @Column( name = "percentagem_ganho" )
    private BigDecimal percentagemGanho;
    @Column( name = "custo_venda" )
    private BigDecimal custoVenda;
    @Column( name = "qtd_composto" )
    private double qtdComposto;
    @Lob
    @Column( name = "photo" )
    private byte[] photo;

    public FichaTecnica()
    {
    }

    public String getPrato()
    {
        return prato;
    }

    public void setPrato( String prato )
    {
        this.prato = prato;
    }

    public double getQtdComposto()
    {
        return qtdComposto;
    }

    public void setQtdComposto( double qtdComposto )
    {
        this.qtdComposto = qtdComposto;
    }

    public FichaTecnica( Integer id )
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

    public Integer getProdutoId()
    {
        return produtoId;
    }

    public void setProdutoId( Integer produtoId )
    {
        this.produtoId = produtoId;
    }

    public Date getDataCriacao()
    {
        return dataCriacao;
    }

    public void setDataCriacao( Date dataCriacao )
    {
        this.dataCriacao = dataCriacao;
    }

    public Integer getUsuarioIdCriacao()
    {
        return usuarioIdCriacao;
    }

    public void setUsuarioIdCriacao( Integer usuarioIdCriacao )
    {
        this.usuarioIdCriacao = usuarioIdCriacao;
    }

    public Date getDataActualizacao()
    {
        return dataActualizacao;
    }

    public void setDataActualizacao( Date dataActualizacao )
    {
        this.dataActualizacao = dataActualizacao;
    }

    public Integer getUsuarioIdActualizacao()
    {
        return usuarioIdActualizacao;
    }

    public void setUsuarioIdActualizacao( Integer usuarioIdActualizacao )
    {
        this.usuarioIdActualizacao = usuarioIdActualizacao;
    }

    public BigDecimal getCustoProduto()
    {
        return custoProduto;
    }

    public void setCustoProduto( BigDecimal custoProduto )
    {
        this.custoProduto = custoProduto;
    }

    public BigDecimal getPercentagemGanho()
    {
        return percentagemGanho;
    }

    public void setPercentagemGanho( BigDecimal percentagemGanho )
    {
        this.percentagemGanho = percentagemGanho;
    }

    public BigDecimal getCustoVenda()
    {
        return custoVenda;
    }

    public void setCustoVenda( BigDecimal custoVenda )
    {
        this.custoVenda = custoVenda;
    }

    public byte[] getPhoto()
    {
        return photo;
    }

    public void setPhoto( byte[] photo )
    {
        this.photo = photo;
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
        if ( !( object instanceof FichaTecnica ) )
        {
            return false;
        }
        FichaTecnica other = (FichaTecnica) object;
        if ( ( this.id == null && other.id != null ) || ( this.id != null && !this.id.equals( other.id ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.FichaTecnica[ id=" + id + " ]";
    }
    
}
