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
@Table(name = "tb_sexo")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "TbSexo.findAll", query = "SELECT t FROM TbSexo t"),
    @NamedQuery(name = "TbSexo.findByCodigo", query = "SELECT t FROM TbSexo t WHERE t.codigo = :codigo"),
    @NamedQuery(name = "TbSexo.findByDescrisao", query = "SELECT t FROM TbSexo t WHERE t.descrisao = :descrisao"),
    @NamedQuery(name = "TbSexo.findByDesignacao", query = "SELECT t FROM TbSexo t WHERE t.designacao = :designacao")
})
public class TbSexo implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "codigo")
    private Integer codigo;
    @Column(name = "descrisao")
    private String descrisao;
    @Column(name = "designacao")
    private String designacao;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "codigoSexo")
    private List<TbUsuario> tbUsuarioList;

    public TbSexo()
    {
    }

    public TbSexo( Integer codigo )
    {
        this.codigo = codigo;
    }

    public Integer getCodigo()
    {
        return codigo;
    }

    public void setCodigo( Integer codigo )
    {
        this.codigo = codigo;
    }

    public String getDescrisao()
    {
        return descrisao;
    }

    public void setDescrisao( String descrisao )
    {
        this.descrisao = descrisao;
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
    public List<TbUsuario> getTbUsuarioList()
    {
        return tbUsuarioList;
    }

    public void setTbUsuarioList( List<TbUsuario> tbUsuarioList )
    {
        this.tbUsuarioList = tbUsuarioList;
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
        if ( !( object instanceof TbSexo ) )
        {
            return false;
        }
        TbSexo other = ( TbSexo ) object;
        if ( ( this.codigo == null && other.codigo != null ) || ( this.codigo != null && !this.codigo.equals( other.codigo ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.TbSexo[ codigo=" + codigo + " ]";
    }
    
}
