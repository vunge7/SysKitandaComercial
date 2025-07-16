/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

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
 * @author marti
 */
@Entity
@Table( name = "tb_item_entradas" )
@XmlRootElement
@NamedQueries( 
{
    @NamedQuery( name = "TbItemEntradas.findAll", query = "SELECT t FROM TbItemEntradas t" ),
    @NamedQuery( name = "TbItemEntradas.findByIdItemEntradas", query = "SELECT t FROM TbItemEntradas t WHERE t.idItemEntradas = :idItemEntradas" ),
    @NamedQuery( name = "TbItemEntradas.findByQuantidade", query = "SELECT t FROM TbItemEntradas t WHERE t.quantidade = :quantidade" )
} )
public class TbItemEntradas implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Basic( optional = false )
    @Column( name = "id_item_entradas" )
    private Integer idItemEntradas;
    @Basic( optional = false )
    @Column( name = "quantidade" )
    private double quantidade;
    @JoinColumn( name = "fk_entradas", referencedColumnName = "idEntrada" )
    @ManyToOne( optional = false )
    private TbEntrada fkEntradas;
    @JoinColumn( name = "id_produto", referencedColumnName = "codigo" )
    @ManyToOne( optional = false )
    private TbProduto idProduto;

    public TbItemEntradas()
    {
    }

    public TbItemEntradas( Integer idItemEntradas )
    {
        this.idItemEntradas = idItemEntradas;
    }

    public TbItemEntradas( Integer idItemEntradas, double quantidade )
    {
        this.idItemEntradas = idItemEntradas;
        this.quantidade = quantidade;
    }

    public Integer getIdItemEntradas()
    {
        return idItemEntradas;
    }

    public void setIdItemEntradas( Integer idItemEntradas )
    {
        this.idItemEntradas = idItemEntradas;
    }

    public double getQuantidade()
    {
        return quantidade;
    }

    public void setQuantidade( double quantidade )
    {
        this.quantidade = quantidade;
    }

    public TbEntrada getFkEntradas()
    {
        return fkEntradas;
    }

    public void setFkEntradas( TbEntrada fkEntradas )
    {
        this.fkEntradas = fkEntradas;
    }

    public TbProduto getIdProduto()
    {
        return idProduto;
    }

    public void setIdProduto( TbProduto idProduto )
    {
        this.idProduto = idProduto;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += ( idItemEntradas != null ? idItemEntradas.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if ( !( object instanceof TbItemEntradas ) )
        {
            return false;
        }
        TbItemEntradas other = (TbItemEntradas) object;
        if ( ( this.idItemEntradas == null && other.idItemEntradas != null ) || ( this.idItemEntradas != null && !this.idItemEntradas.equals( other.idItemEntradas ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.TbItemEntradas[ idItemEntradas=" + idItemEntradas + " ]";
    }
    
}
