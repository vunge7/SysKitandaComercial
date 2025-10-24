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
@Table(name = "linha_transferencia")
@XmlRootElement
@NamedQueries(
        {
            @NamedQuery(name = "LinhaTransferencia.findAll", query = "SELECT l FROM LinhaTransferencia l"),
            @NamedQuery(name = "LinhaTransferencia.findByPkLinhaTransferencia", query = "SELECT l FROM LinhaTransferencia l WHERE l.pkLinhaTransferencia = :pkLinhaTransferencia"),
            @NamedQuery(name = "LinhaTransferencia.findByQtdBefore", query = "SELECT l FROM LinhaTransferencia l WHERE l.qtdBefore = :qtdBefore"),
            @NamedQuery(name = "LinhaTransferencia.findByQtd", query = "SELECT l FROM LinhaTransferencia l WHERE l.qtd = :qtd"),
            @NamedQuery(name = "LinhaTransferencia.findByQtdAfter", query = "SELECT l FROM LinhaTransferencia l WHERE l.qtdAfter = :qtdAfter"),
            @NamedQuery(name = "LinhaTransferencia.findByFkArmazemOrigem", query = "SELECT l FROM LinhaTransferencia l WHERE l.fkArmazemOrigem = :fkArmazemOrigem"),
            @NamedQuery(name = "LinhaTransferencia.findByArmazemOrigem", query = "SELECT l FROM LinhaTransferencia l WHERE l.armazemOrigem = :armazemOrigem"),
            @NamedQuery(name = "LinhaTransferencia.findByFkArmazemDestino", query = "SELECT l FROM LinhaTransferencia l WHERE l.fkArmazemDestino = :fkArmazemDestino"),
            @NamedQuery(name = "LinhaTransferencia.findByArmazemDestino", query = "SELECT l FROM LinhaTransferencia l WHERE l.armazemDestino = :armazemDestino")
        })
public class LinhaTransferencia implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pk_linha_transferencia")
    private Integer pkLinhaTransferencia;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "qtd_before")
    private Double qtdBefore;
    @Column(name = "qtd")
    private Double qtd;
    @Column(name = "qtd_after")
    private Double qtdAfter;
    @Column(name = "fk_armazem_origem")
    private Integer fkArmazemOrigem;
    @Column(name = "armazem_origem")
    private String armazemOrigem;
    @Column(name = "fk_armazem_destino")
    private Integer fkArmazemDestino;
    @Column(name = "armazem_destino")
    private String armazemDestino;
    @Column(name = "fk_produto")
    private int fkProduto;
    @Column(name = "produto")
    private String produto;
    @JoinColumn(name = "fk_transferencia_armazem", referencedColumnName = "pk_transferencia_armazem")
    @ManyToOne(optional = false)
    private TransferenciaArmazem fkTransferenciaArmazem;

    public LinhaTransferencia()
    {
    }

    public int getFkProduto()
    {
        return fkProduto;
    }

    public void setFkProduto( int fkProduto )
    {
        this.fkProduto = fkProduto;
    }

    public String getProduto()
    {
        return produto;
    }

    public void setProduto( String produto )
    {
        this.produto = produto;
    }

    public LinhaTransferencia( Integer pkLinhaTransferencia )
    {
        this.pkLinhaTransferencia = pkLinhaTransferencia;
    }

    public Integer getPkLinhaTransferencia()
    {
        return pkLinhaTransferencia;
    }

    public void setPkLinhaTransferencia( Integer pkLinhaTransferencia )
    {
        this.pkLinhaTransferencia = pkLinhaTransferencia;
    }

    public Double getQtdBefore()
    {
        return qtdBefore;
    }

    public void setQtdBefore( Double qtdBefore )
    {
        this.qtdBefore = qtdBefore;
    }

    public Double getQtd()
    {
        return qtd;
    }

    public void setQtd( Double qtd )
    {
        this.qtd = qtd;
    }

    public Double getQtdAfter()
    {
        return qtdAfter;
    }

    public void setQtdAfter( Double qtdAfter )
    {
        this.qtdAfter = qtdAfter;
    }

    public Integer getFkArmazemOrigem()
    {
        return fkArmazemOrigem;
    }

    public void setFkArmazemOrigem( Integer fkArmazemOrigem )
    {
        this.fkArmazemOrigem = fkArmazemOrigem;
    }

    public String getArmazemOrigem()
    {
        return armazemOrigem;
    }

    public void setArmazemOrigem( String armazemOrigem )
    {
        this.armazemOrigem = armazemOrigem;
    }

    public Integer getFkArmazemDestino()
    {
        return fkArmazemDestino;
    }

    public void setFkArmazemDestino( Integer fkArmazemDestino )
    {
        this.fkArmazemDestino = fkArmazemDestino;
    }

    public String getArmazemDestino()
    {
        return armazemDestino;
    }

    public void setArmazemDestino( String armazemDestino )
    {
        this.armazemDestino = armazemDestino;
    }

    public TransferenciaArmazem getFkTransferenciaArmazem()
    {
        return fkTransferenciaArmazem;
    }

    public void setFkTransferenciaArmazem( TransferenciaArmazem fkTransferenciaArmazem )
    {
        this.fkTransferenciaArmazem = fkTransferenciaArmazem;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += ( pkLinhaTransferencia != null ? pkLinhaTransferencia.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if ( !( object instanceof LinhaTransferencia ) )
        {
            return false;
        }
        LinhaTransferencia other = ( LinhaTransferencia ) object;
        if ( ( this.pkLinhaTransferencia == null && other.pkLinhaTransferencia != null ) || ( this.pkLinhaTransferencia != null && !this.pkLinhaTransferencia.equals( other.pkLinhaTransferencia ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.LinhaTransferencia[ pkLinhaTransferencia=" + pkLinhaTransferencia + " ]";
    }

}
