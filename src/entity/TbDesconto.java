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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "tb_desconto")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "TbDesconto.findAll", query = "SELECT t FROM TbDesconto t"),
    @NamedQuery(name = "TbDesconto.findByIdDesconto", query = "SELECT t FROM TbDesconto t WHERE t.idDesconto = :idDesconto"),
    @NamedQuery(name = "TbDesconto.findByData", query = "SELECT t FROM TbDesconto t WHERE t.data = :data"),
    @NamedQuery(name = "TbDesconto.findByHora", query = "SELECT t FROM TbDesconto t WHERE t.hora = :hora"),
    @NamedQuery(name = "TbDesconto.findByQuantidade", query = "SELECT t FROM TbDesconto t WHERE t.quantidade = :quantidade"),
    @NamedQuery(name = "TbDesconto.findByValor", query = "SELECT t FROM TbDesconto t WHERE t.valor = :valor")
})
public class TbDesconto implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idDesconto")
    private Integer idDesconto;
    @Column(name = "data")
    @Temporal(TemporalType.DATE)
    private Date data;
    @Column(name = "hora")
    @Temporal(TemporalType.TIME)
    private Date hora;
    @Basic(optional = false)
    @Column(name = "quantidade")
    private double quantidade;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor")
    private Double valor;
    @JoinColumn(name = "fk_cliente", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private TbCliente fkCliente;
    @JoinColumn(name = "fk_produto", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private TbProduto fkProduto;
    @JoinColumn(name = "fk_usuario", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private TbUsuario fkUsuario;

    public TbDesconto()
    {
    }

    public TbDesconto( Integer idDesconto )
    {
        this.idDesconto = idDesconto;
    }

    public TbDesconto( Integer idDesconto, double quantidade )
    {
        this.idDesconto = idDesconto;
        this.quantidade = quantidade;
    }

    public Integer getIdDesconto()
    {
        return idDesconto;
    }

    public void setIdDesconto( Integer idDesconto )
    {
        this.idDesconto = idDesconto;
    }

    public Date getData()
    {
        return data;
    }

    public void setData( Date data )
    {
        this.data = data;
    }

    public Date getHora()
    {
        return hora;
    }

    public void setHora( Date hora )
    {
        this.hora = hora;
    }

    public double getQuantidade()
    {
        return quantidade;
    }

    public void setQuantidade( double quantidade )
    {
        this.quantidade = quantidade;
    }

    public Double getValor()
    {
        return valor;
    }

    public void setValor( Double valor )
    {
        this.valor = valor;
    }

    public TbCliente getFkCliente()
    {
        return fkCliente;
    }

    public void setFkCliente( TbCliente fkCliente )
    {
        this.fkCliente = fkCliente;
    }

    public TbProduto getFkProduto()
    {
        return fkProduto;
    }

    public void setFkProduto( TbProduto fkProduto )
    {
        this.fkProduto = fkProduto;
    }

    public TbUsuario getFkUsuario()
    {
        return fkUsuario;
    }

    public void setFkUsuario( TbUsuario fkUsuario )
    {
        this.fkUsuario = fkUsuario;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += ( idDesconto != null ? idDesconto.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if ( !( object instanceof TbDesconto ) )
        {
            return false;
        }
        TbDesconto other = ( TbDesconto ) object;
        if ( ( this.idDesconto == null && other.idDesconto != null ) || ( this.idDesconto != null && !this.idDesconto.equals( other.idDesconto ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.TbDesconto[ idDesconto=" + idDesconto + " ]";
    }
    
}
