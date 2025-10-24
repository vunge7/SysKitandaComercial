/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;


import java.sql.Connection;
import java.io.Serializable;
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
 * @author lenovo
 */
@Entity
@Table(name = "acerto_stock")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "AcertoStock.findAll", query = "SELECT a FROM AcertoStock a"),
    @NamedQuery(name = "AcertoStock.findByPkAcertoStock", query = "SELECT a FROM AcertoStock a WHERE a.pkAcertoStock = :pkAcertoStock"),
    @NamedQuery(name = "AcertoStock.findByDataHora", query = "SELECT a FROM AcertoStock a WHERE a.dataHora = :dataHora"),
    @NamedQuery(name = "AcertoStock.findByCodProduto", query = "SELECT a FROM AcertoStock a WHERE a.codProduto = :codProduto"),
    @NamedQuery(name = "AcertoStock.findByDesignacaoProduto", query = "SELECT a FROM AcertoStock a WHERE a.designacaoProduto = :designacaoProduto"),
    @NamedQuery(name = "AcertoStock.findByCodUsuario", query = "SELECT a FROM AcertoStock a WHERE a.codUsuario = :codUsuario"),
    @NamedQuery(name = "AcertoStock.findByNomeUsuario", query = "SELECT a FROM AcertoStock a WHERE a.nomeUsuario = :nomeUsuario"),
    @NamedQuery(name = "AcertoStock.findByQtdAnterior", query = "SELECT a FROM AcertoStock a WHERE a.qtdAnterior = :qtdAnterior"),
    @NamedQuery(name = "AcertoStock.findByQtdAcerto", query = "SELECT a FROM AcertoStock a WHERE a.qtdAcerto = :qtdAcerto"),
    @NamedQuery(name = "AcertoStock.findByQtdDepois", query = "SELECT a FROM AcertoStock a WHERE a.qtdDepois = :qtdDepois"),
    @NamedQuery(name = "AcertoStock.findByCodArmazem", query = "SELECT a FROM AcertoStock a WHERE a.codArmazem = :codArmazem"),
    @NamedQuery(name = "AcertoStock.findByDesigncaoArmazem", query = "SELECT a FROM AcertoStock a WHERE a.designcaoArmazem = :designcaoArmazem"),
    @NamedQuery(name = "AcertoStock.findByMotivoAcerto", query = "SELECT a FROM AcertoStock a WHERE a.motivoAcerto = :motivoAcerto")
})
public class AcertoStock implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pk_acerto_stock")
    private Long pkAcertoStock;
    @Basic(optional = false)
    @Column(name = "data_hora")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataHora;
    @Basic(optional = false)
    @Column(name = "cod_produto")
    private int codProduto;
    @Basic(optional = false)
    @Column(name = "designacao_produto")
    private String designacaoProduto;
    @Basic(optional = false)
    @Column(name = "cod_usuario")
    private int codUsuario;
    @Basic(optional = false)
    @Column(name = "nome_usuario")
    private String nomeUsuario;
    @Basic(optional = false)
    @Column(name = "qtd_anterior")
    private double qtdAnterior;
    @Basic(optional = false)
    @Column(name = "qtd_acerto")
    private double qtdAcerto;
    @Basic(optional = false)
    @Column(name = "qtd_depois")
    private double qtdDepois;
    @Basic(optional = false)
    @Column(name = "cod_armazem")
    private int codArmazem;
    @Basic(optional = false)
    @Column(name = "designcao_armazem")
    private String designcaoArmazem;
    @Column(name = "motivo_acerto")
    private String motivoAcerto;

    public AcertoStock()
    {
    }

    public AcertoStock( Long pkAcertoStock )
    {
        this.pkAcertoStock = pkAcertoStock;
    }

    public AcertoStock( Long pkAcertoStock, Date dataHora, int codProduto, String designacaoProduto, int codUsuario, String nomeUsuario, double qtdAnterior, double qtdAcerto, double qtdDepois, int codArmazem, String designcaoArmazem )
    {
        this.pkAcertoStock = pkAcertoStock;
        this.dataHora = dataHora;
        this.codProduto = codProduto;
        this.designacaoProduto = designacaoProduto;
        this.codUsuario = codUsuario;
        this.nomeUsuario = nomeUsuario;
        this.qtdAnterior = qtdAnterior;
        this.qtdAcerto = qtdAcerto;
        this.qtdDepois = qtdDepois;
        this.codArmazem = codArmazem;
        this.designcaoArmazem = designcaoArmazem;
    }

    public Long getPkAcertoStock()
    {
        return pkAcertoStock;
    }

    public void setPkAcertoStock( Long pkAcertoStock )
    {
        this.pkAcertoStock = pkAcertoStock;
    }

    public Date getDataHora()
    {
        return dataHora;
    }

    public void setDataHora( Date dataHora )
    {
        this.dataHora = dataHora;
    }

    public int getCodProduto()
    {
        return codProduto;
    }

    public void setCodProduto( int codProduto )
    {
        this.codProduto = codProduto;
    }

    public String getDesignacaoProduto()
    {
        return designacaoProduto;
    }

    public void setDesignacaoProduto( String designacaoProduto )
    {
        this.designacaoProduto = designacaoProduto;
    }

    public int getCodUsuario()
    {
        return codUsuario;
    }

    public void setCodUsuario( int codUsuario )
    {
        this.codUsuario = codUsuario;
    }

    public String getNomeUsuario()
    {
        return nomeUsuario;
    }

    public void setNomeUsuario( String nomeUsuario )
    {
        this.nomeUsuario = nomeUsuario;
    }

    public double getQtdAnterior()
    {
        return qtdAnterior;
    }

    public void setQtdAnterior( double qtdAnterior )
    {
        this.qtdAnterior = qtdAnterior;
    }

    public double getQtdAcerto()
    {
        return qtdAcerto;
    }

    public void setQtdAcerto( double qtdAcerto )
    {
        this.qtdAcerto = qtdAcerto;
    }

    public double getQtdDepois()
    {
        return qtdDepois;
    }

    public void setQtdDepois( double qtdDepois )
    {
        this.qtdDepois = qtdDepois;
    }

    public int getCodArmazem()
    {
        return codArmazem;
    }

    public void setCodArmazem( int codArmazem )
    {
        this.codArmazem = codArmazem;
    }

    public String getDesigncaoArmazem()
    {
        return designcaoArmazem;
    }

    public void setDesigncaoArmazem( String designcaoArmazem )
    {
        this.designcaoArmazem = designcaoArmazem;
    }

    public String getMotivoAcerto()
    {
        return motivoAcerto;
    }

    public void setMotivoAcerto( String motivoAcerto )
    {
        this.motivoAcerto = motivoAcerto;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += ( pkAcertoStock != null ? pkAcertoStock.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if ( !( object instanceof AcertoStock ) )
        {
            return false;
        }
        AcertoStock other = ( AcertoStock ) object;
        if ( ( this.pkAcertoStock == null && other.pkAcertoStock != null ) || ( this.pkAcertoStock != null && !this.pkAcertoStock.equals( other.pkAcertoStock ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.AcertoStock[ pkAcertoStock=" + pkAcertoStock + " ]";
    }
    
}
