/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;


import java.sql.Connection;
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
@Table( name = "tb_armazem" )
@XmlRootElement
@NamedQueries(
                {
            @NamedQuery( name = "TbArmazem.findAll", query = "SELECT t FROM TbArmazem t" ),
            @NamedQuery( name = "TbArmazem.findByCodigo", query = "SELECT t FROM TbArmazem t WHERE t.codigo = :codigo" ),
            @NamedQuery( name = "TbArmazem.findByDesignacao", query = "SELECT t FROM TbArmazem t WHERE t.designacao = :designacao" )
        } )
public class TbArmazem implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Basic( optional = false )
    @Column( name = "codigo" )
    private Integer codigo;
    @Basic( optional = false )
    @Column( name = "designacao" )
    private String designacao;
    @OneToMany( cascade = CascadeType.ALL, mappedBy = "codArmazem" )
    private List<TbStockMirrow> tbStockMirrowList;
    @OneToMany( cascade = CascadeType.ALL, mappedBy = "idArmazemFK" )
    private List<NotasCompras> notasComprasList;
    @OneToMany( cascade = CascadeType.ALL, mappedBy = "idArmazemFK" )
    private List<Notas> notasList;
    @OneToMany( mappedBy = "idArmazemFK" )
    private List<Compras> comprasList;
    @OneToMany( cascade = CascadeType.ALL, mappedBy = "idArmazemFK" )
    private List<TbEstorno> tbEstornoList;
    @OneToMany( mappedBy = "idArmazemFK" )
    private List<TbAcerto> tbAcertoList;
    @OneToMany( mappedBy = "fkArmazem" )
    private List<TbVasilhame> tbVasilhameList;
    @OneToMany( cascade = CascadeType.ALL, mappedBy = "idArmazemFK" )
    private List<TbSaidasProdutos> tbSaidasProdutosList;
    @OneToMany( cascade = CascadeType.ALL, mappedBy = "idArmazemFK" )
    private List<TbEntrada> tbEntradaList;
    @OneToMany( mappedBy = "fkAmazem" )
    private List<TbEntradaVasilhame> tbEntradaVasilhameList;
    @OneToMany( cascade = CascadeType.ALL, mappedBy = "idArmazemFK" )
    private List<TbVenda> tbVendaList;
    @OneToMany( cascade = CascadeType.ALL, mappedBy = "codArmazem" )
    private List<TbStock> tbStockList;
    @OneToMany( cascade = CascadeType.ALL, mappedBy = "fkArmazem" )
    private List<AccessoArmazem> accessoArmazemList;
    @OneToMany( cascade = CascadeType.ALL, mappedBy = "fkArmazem" )
    private List<TbSaidaVasilhame> tbSaidaVasilhameList;

    public TbArmazem()
    {
    }

    public TbArmazem( Integer codigo )
    {
        this.codigo = codigo;
    }

    public TbArmazem( Integer codigo, String designacao )
    {
        this.codigo = codigo;
        this.designacao = designacao;
    }

    public String getDisplayName()
    {
        return this.designacao; // ou getNome()
    }

    public Integer getCodigo()
    {
        return codigo;
    }

    public void setCodigo( Integer codigo )
    {
        this.codigo = codigo;
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
    public List<TbStockMirrow> getTbStockMirrowList()
    {
        return tbStockMirrowList;
    }

    public void setTbStockMirrowList( List<TbStockMirrow> tbStockMirrowList )
    {
        this.tbStockMirrowList = tbStockMirrowList;
    }

    @XmlTransient
    public List<NotasCompras> getNotasComprasList()
    {
        return notasComprasList;
    }

    public void setNotasComprasList( List<NotasCompras> notasComprasList )
    {
        this.notasComprasList = notasComprasList;
    }

    @XmlTransient
    public List<Notas> getNotasList()
    {
        return notasList;
    }

    public void setNotasList( List<Notas> notasList )
    {
        this.notasList = notasList;
    }

    @XmlTransient
    public List<Compras> getComprasList()
    {
        return comprasList;
    }

    public void setComprasList( List<Compras> comprasList )
    {
        this.comprasList = comprasList;
    }

    @XmlTransient
    public List<TbEstorno> getTbEstornoList()
    {
        return tbEstornoList;
    }

    public void setTbEstornoList( List<TbEstorno> tbEstornoList )
    {
        this.tbEstornoList = tbEstornoList;
    }

    @XmlTransient
    public List<TbAcerto> getTbAcertoList()
    {
        return tbAcertoList;
    }

    public void setTbAcertoList( List<TbAcerto> tbAcertoList )
    {
        this.tbAcertoList = tbAcertoList;
    }

    @XmlTransient
    public List<TbVasilhame> getTbVasilhameList()
    {
        return tbVasilhameList;
    }

    public void setTbVasilhameList( List<TbVasilhame> tbVasilhameList )
    {
        this.tbVasilhameList = tbVasilhameList;
    }

    @XmlTransient
    public List<TbSaidasProdutos> getTbSaidasProdutosList()
    {
        return tbSaidasProdutosList;
    }

    public void setTbSaidasProdutosList( List<TbSaidasProdutos> tbSaidasProdutosList )
    {
        this.tbSaidasProdutosList = tbSaidasProdutosList;
    }

    @XmlTransient
    public List<TbEntrada> getTbEntradaList()
    {
        return tbEntradaList;
    }

    public void setTbEntradaList( List<TbEntrada> tbEntradaList )
    {
        this.tbEntradaList = tbEntradaList;
    }

    @XmlTransient
    public List<TbEntradaVasilhame> getTbEntradaVasilhameList()
    {
        return tbEntradaVasilhameList;
    }

    public void setTbEntradaVasilhameList( List<TbEntradaVasilhame> tbEntradaVasilhameList )
    {
        this.tbEntradaVasilhameList = tbEntradaVasilhameList;
    }

    @XmlTransient
    public List<TbVenda> getTbVendaList()
    {
        return tbVendaList;
    }

    public void setTbVendaList( List<TbVenda> tbVendaList )
    {
        this.tbVendaList = tbVendaList;
    }

    @XmlTransient
    public List<TbStock> getTbStockList()
    {
        return tbStockList;
    }

    public void setTbStockList( List<TbStock> tbStockList )
    {
        this.tbStockList = tbStockList;
    }

    @XmlTransient
    public List<AccessoArmazem> getAccessoArmazemList()
    {
        return accessoArmazemList;
    }

    public void setAccessoArmazemList( List<AccessoArmazem> accessoArmazemList )
    {
        this.accessoArmazemList = accessoArmazemList;
    }

    @XmlTransient
    public List<TbSaidaVasilhame> getTbSaidaVasilhameList()
    {
        return tbSaidaVasilhameList;
    }

    public void setTbSaidaVasilhameList( List<TbSaidaVasilhame> tbSaidaVasilhameList )
    {
        this.tbSaidaVasilhameList = tbSaidaVasilhameList;
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
        if ( !( object instanceof TbArmazem ) )
        {
            return false;
        }
        TbArmazem other = (TbArmazem) object;
        if ( ( this.codigo == null && other.codigo != null ) || ( this.codigo != null && !this.codigo.equals( other.codigo ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.TbArmazem[ codigo=" + codigo + " ]";
    }

}
