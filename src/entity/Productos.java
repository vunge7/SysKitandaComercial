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
@Table(name = "productos")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "Productos.findAll", query = "SELECT p FROM Productos p"),
    @NamedQuery(name = "Productos.findByCodigoProduto", query = "SELECT p FROM Productos p WHERE p.codigoProduto = :codigoProduto"),
    @NamedQuery(name = "Productos.findByConsumo", query = "SELECT p FROM Productos p WHERE p.consumo = :consumo"),
    @NamedQuery(name = "Productos.findByPreco", query = "SELECT p FROM Productos p WHERE p.preco = :preco"),
    @NamedQuery(name = "Productos.findByLugar", query = "SELECT p FROM Productos p WHERE p.lugar = :lugar"),
    @NamedQuery(name = "Productos.findByQtd", query = "SELECT p FROM Productos p WHERE p.qtd = :qtd"),
    @NamedQuery(name = "Productos.findByMesa", query = "SELECT p FROM Productos p WHERE p.mesa = :mesa"),
    @NamedQuery(name = "Productos.findByUsuario", query = "SELECT p FROM Productos p WHERE p.usuario = :usuario"),
    @NamedQuery(name = "Productos.findByDataHora", query = "SELECT p FROM Productos p WHERE p.dataHora = :dataHora")
})
public class Productos implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "codigo_produto")
    private Integer codigoProduto;
    @Column(name = "consumo")
    private String consumo;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "preco")
    private Double preco;
    @Column(name = "lugar")
    private String lugar;
    @Column(name = "qtd")
    private Double qtd;
    @Column(name = "mesa")
    private String mesa;
    @Column(name = "usuario")
    private String usuario;
    @Column(name = "data_hora")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataHora;

    public Productos()
    {
    }

    public Productos( Integer codigoProduto )
    {
        this.codigoProduto = codigoProduto;
    }

    public Integer getCodigoProduto()
    {
        return codigoProduto;
    }

    public void setCodigoProduto( Integer codigoProduto )
    {
        this.codigoProduto = codigoProduto;
    }

    public String getConsumo()
    {
        return consumo;
    }

    public void setConsumo( String consumo )
    {
        this.consumo = consumo;
    }

    public Double getPreco()
    {
        return preco;
    }

    public void setPreco( Double preco )
    {
        this.preco = preco;
    }

    public String getLugar()
    {
        return lugar;
    }

    public void setLugar( String lugar )
    {
        this.lugar = lugar;
    }

    public Double getQtd()
    {
        return qtd;
    }

    public void setQtd( Double qtd )
    {
        this.qtd = qtd;
    }

    public String getMesa()
    {
        return mesa;
    }

    public void setMesa( String mesa )
    {
        this.mesa = mesa;
    }

    public String getUsuario()
    {
        return usuario;
    }

    public void setUsuario( String usuario )
    {
        this.usuario = usuario;
    }

    public Date getDataHora()
    {
        return dataHora;
    }

    public void setDataHora( Date dataHora )
    {
        this.dataHora = dataHora;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += ( codigoProduto != null ? codigoProduto.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if ( !( object instanceof Productos ) )
        {
            return false;
        }
        Productos other = ( Productos ) object;
        if ( ( this.codigoProduto == null && other.codigoProduto != null ) || ( this.codigoProduto != null && !this.codigoProduto.equals( other.codigoProduto ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.Productos[ codigoProduto=" + codigoProduto + " ]";
    }
    
}
