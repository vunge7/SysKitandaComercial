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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "tb_vasilhame")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "TbVasilhame.findAll", query = "SELECT t FROM TbVasilhame t"),
    @NamedQuery(name = "TbVasilhame.findByPkVasilhame", query = "SELECT t FROM TbVasilhame t WHERE t.pkVasilhame = :pkVasilhame"),
    @NamedQuery(name = "TbVasilhame.findByDescricao", query = "SELECT t FROM TbVasilhame t WHERE t.descricao = :descricao"),
    @NamedQuery(name = "TbVasilhame.findByQtdExistente", query = "SELECT t FROM TbVasilhame t WHERE t.qtdExistente = :qtdExistente")
})
public class TbVasilhame implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pk_vasilhame")
    private Integer pkVasilhame;
    @Column(name = "descricao")
    private String descricao;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "qtd_existente")
    private Double qtdExistente;
    @JoinColumn(name = "fk_armazem", referencedColumnName = "codigo")
    @ManyToOne
    private TbArmazem fkArmazem;
    @JoinColumn(name = "fk_produto", referencedColumnName = "codigo")
    @ManyToOne
    private TbProduto fkProduto;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkVasilhame")
    private List<TbItemEntradaVasilhame> tbItemEntradaVasilhameList;

    public TbVasilhame()
    {
    }

    public TbVasilhame( Integer pkVasilhame )
    {
        this.pkVasilhame = pkVasilhame;
    }

    public Integer getPkVasilhame()
    {
        return pkVasilhame;
    }

    public void setPkVasilhame( Integer pkVasilhame )
    {
        this.pkVasilhame = pkVasilhame;
    }

    public String getDescricao()
    {
        return descricao;
    }

    public void setDescricao( String descricao )
    {
        this.descricao = descricao;
    }

    public Double getQtdExistente()
    {
        return qtdExistente;
    }

    public void setQtdExistente( Double qtdExistente )
    {
        this.qtdExistente = qtdExistente;
    }

    public TbArmazem getFkArmazem()
    {
        return fkArmazem;
    }

    public void setFkArmazem( TbArmazem fkArmazem )
    {
        this.fkArmazem = fkArmazem;
    }

    public TbProduto getFkProduto()
    {
        return fkProduto;
    }

    public void setFkProduto( TbProduto fkProduto )
    {
        this.fkProduto = fkProduto;
    }

    @XmlTransient
    public List<TbItemEntradaVasilhame> getTbItemEntradaVasilhameList()
    {
        return tbItemEntradaVasilhameList;
    }

    public void setTbItemEntradaVasilhameList( List<TbItemEntradaVasilhame> tbItemEntradaVasilhameList )
    {
        this.tbItemEntradaVasilhameList = tbItemEntradaVasilhameList;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += ( pkVasilhame != null ? pkVasilhame.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if ( !( object instanceof TbVasilhame ) )
        {
            return false;
        }
        TbVasilhame other = ( TbVasilhame ) object;
        if ( ( this.pkVasilhame == null && other.pkVasilhame != null ) || ( this.pkVasilhame != null && !this.pkVasilhame.equals( other.pkVasilhame ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.TbVasilhame[ pkVasilhame=" + pkVasilhame + " ]";
    }
    
}
