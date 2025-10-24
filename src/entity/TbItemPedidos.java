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
@Table(name = "tb_item_pedidos")
@XmlRootElement
@NamedQueries(
        {
            @NamedQuery(name = "TbItemPedidos.findAll", query = "SELECT t FROM TbItemPedidos t"),
            @NamedQuery(name = "TbItemPedidos.findByPkItemPedidos", query = "SELECT t FROM TbItemPedidos t WHERE t.pkItemPedidos = :pkItemPedidos"),
            @NamedQuery(name = "TbItemPedidos.findByQtd", query = "SELECT t FROM TbItemPedidos t WHERE t.qtd = :qtd"),
            @NamedQuery(name = "TbItemPedidos.findByStatusConvertido", query = "SELECT t FROM TbItemPedidos t WHERE t.statusConvertido = :statusConvertido"),
            @NamedQuery(name = "TbItemPedidos.findByTotalItem", query = "SELECT t FROM TbItemPedidos t WHERE t.totalItem = :totalItem"),
            @NamedQuery(name = "TbItemPedidos.findByStatusEnviado", query = "SELECT t FROM TbItemPedidos t WHERE t.statusEnviado = :statusEnviado"),
            @NamedQuery(name = "TbItemPedidos.findByStatusEfectuado", query = "SELECT t FROM TbItemPedidos t WHERE t.statusEfectuado = :statusEfectuado")
        })
public class TbItemPedidos implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pk_item_pedidos")
    private Integer pkItemPedidos;
    @Basic(optional = false)
    @Column(name = "qtd")
    private double qtd;
    @Column(name = "status_convertido")
    private Boolean statusConvertido;
    @Basic(optional = false)
    @Column(name = "total_item")
    private double totalItem;
    @Column(name = "status_enviado")
    private Boolean statusEnviado;
    @Column(name = "status_efectuado")
    private Boolean statusEfectuado;
    @Column(name = "data_entrega")
    @Temporal(TemporalType.DATE)
    private Date dataEntrega;
    @Column(name = "obs")
    private String obs;
    @Column(name = "preco")
    private double preco;
    @JoinColumn(name = "fk_lugares", referencedColumnName = "pk_lugares")
    @ManyToOne(optional = false)
    private TbLugares fkLugares;
    @JoinColumn(name = "fk_produtos", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private TbProduto fkProdutos;
    @JoinColumn(name = "fk_pedidos", referencedColumnName = "pk_pedido")
    @ManyToOne(optional = false)
    private TbPedido fkPedidos;

    public TbItemPedidos()
    {
    }

    public TbItemPedidos( Integer pkItemPedidos )
    {
        this.pkItemPedidos = pkItemPedidos;
    }

    public TbItemPedidos( Integer pkItemPedidos, int qtd, double totalItem )
    {
        this.pkItemPedidos = pkItemPedidos;
        this.qtd = qtd;
        this.totalItem = totalItem;
    }

    public Integer getPkItemPedidos()
    {
        return pkItemPedidos;
    }

    public void setPkItemPedidos( Integer pkItemPedidos )
    {
        this.pkItemPedidos = pkItemPedidos;
    }

    public double getQtd()
    {
        return qtd;
    }

    public void setQtd( double qtd )
    {
        this.qtd = qtd;
    }

    public Boolean getStatusConvertido()
    {
        return statusConvertido;
    }

    public void setStatusConvertido( Boolean statusConvertido )
    {
        this.statusConvertido = statusConvertido;
    }

    public double getTotalItem()
    {
        return totalItem;
    }

    public void setTotalItem( double totalItem )
    {
        this.totalItem = totalItem;
    }

    public Boolean getStatusEnviado()
    {
        return statusEnviado;
    }

    public void setStatusEnviado( Boolean statusEnviado )
    {
        this.statusEnviado = statusEnviado;
    }

    public Boolean getStatusEfectuado()
    {
        return statusEfectuado;
    }

    public void setStatusEfectuado( Boolean statusEfectuado )
    {
        this.statusEfectuado = statusEfectuado;
    }

    public TbLugares getFkLugares()
    {
        return fkLugares;
    }

    public void setFkLugares( TbLugares fkLugares )
    {
        this.fkLugares = fkLugares;
    }

    public TbProduto getFkProdutos()
    {
        return fkProdutos;
    }

    public void setFkProdutos( TbProduto fkProdutos )
    {
        this.fkProdutos = fkProdutos;
    }

    public TbPedido getFkPedidos()
    {
        return fkPedidos;
    }

    public void setFkPedidos( TbPedido fkPedidos )
    {
        this.fkPedidos = fkPedidos;
    }

    public Date getDataEntrega()
    {
        return dataEntrega;
    }

    public void setDataEntrega( Date dataEntrega )
    {
        this.dataEntrega = dataEntrega;
    }

    public String getObs()
    {
        return obs;
    }

    public double getPreco()
    {
        return preco;
    }

    public void setPreco( double preco )
    {
        this.preco = preco;
    }
    
    

    public void setObs( String obs )
    {
        this.obs = obs;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += ( pkItemPedidos != null ? pkItemPedidos.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if ( !( object instanceof TbItemPedidos ) )
        {
            return false;
        }
        TbItemPedidos other = ( TbItemPedidos ) object;
        if ( ( this.pkItemPedidos == null && other.pkItemPedidos != null ) || ( this.pkItemPedidos != null && !this.pkItemPedidos.equals( other.pkItemPedidos ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.TbItemPedidos[ pkItemPedidos=" + pkItemPedidos + " ]";
    }

}
