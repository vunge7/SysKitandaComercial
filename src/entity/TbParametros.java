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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author lenovo
 */
@Entity
@Table(name = "tb_parametros")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "TbParametros.findAll", query = "SELECT t FROM TbParametros t"),
    @NamedQuery(name = "TbParametros.findByCodigo", query = "SELECT t FROM TbParametros t WHERE t.codigo = :codigo"),
    @NamedQuery(name = "TbParametros.findByDesignacao", query = "SELECT t FROM TbParametros t WHERE t.designacao = :designacao")
})
public class TbParametros implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "codigo")
    private Integer codigo;
    @Basic(optional = false)
    @Column(name = "Designacao")
    private String designacao;

    public TbParametros()
    {
    }

    public TbParametros( Integer codigo )
    {
        this.codigo = codigo;
    }

    public TbParametros( Integer codigo, String designacao )
    {
        this.codigo = codigo;
        this.designacao = designacao;
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
        if ( !( object instanceof TbParametros ) )
        {
            return false;
        }
        TbParametros other = ( TbParametros ) object;
        if ( ( this.codigo == null && other.codigo != null ) || ( this.codigo != null && !this.codigo.equals( other.codigo ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.TbParametros[ codigo=" + codigo + " ]";
    }
    
}
