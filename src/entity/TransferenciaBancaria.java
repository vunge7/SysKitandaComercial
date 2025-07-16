/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

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
@Table(name = "transferencia_bancaria")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "TransferenciaBancaria.findAll", query = "SELECT t FROM TransferenciaBancaria t"),
    @NamedQuery(name = "TransferenciaBancaria.findByPkTransferencia", query = "SELECT t FROM TransferenciaBancaria t WHERE t.pkTransferencia = :pkTransferencia"),
    @NamedQuery(name = "TransferenciaBancaria.findByData", query = "SELECT t FROM TransferenciaBancaria t WHERE t.data = :data"),
    @NamedQuery(name = "TransferenciaBancaria.findByHora", query = "SELECT t FROM TransferenciaBancaria t WHERE t.hora = :hora"),
    @NamedQuery(name = "TransferenciaBancaria.findByValor", query = "SELECT t FROM TransferenciaBancaria t WHERE t.valor = :valor"),
    @NamedQuery(name = "TransferenciaBancaria.findByDescricao", query = "SELECT t FROM TransferenciaBancaria t WHERE t.descricao = :descricao"),
    @NamedQuery(name = "TransferenciaBancaria.findByNborderaux", query = "SELECT t FROM TransferenciaBancaria t WHERE t.nborderaux = :nborderaux")
})
public class TransferenciaBancaria implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pk_transferencia")
    private Integer pkTransferencia;
    @Column(name = "data")
    @Temporal(TemporalType.DATE)
    private Date data;
    @Column(name = "hora")
    @Temporal(TemporalType.TIME)
    private Date hora;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor")
    private Double valor;
    @Column(name = "descricao")
    private String descricao;
    @Column(name = "nborderaux")
    private String nborderaux;
    @JoinColumn(name = "fk_banco_destino", referencedColumnName = "idBanco")
    @ManyToOne
    private TbBanco fkBancoDestino;
    @JoinColumn(name = "fk_banco_origem", referencedColumnName = "idBanco")
    @ManyToOne
    private TbBanco fkBancoOrigem;
    @JoinColumn(name = "fk_usuario", referencedColumnName = "codigo")
    @ManyToOne
    private TbUsuario fkUsuario;

    public TransferenciaBancaria()
    {
    }

    public TransferenciaBancaria( Integer pkTransferencia )
    {
        this.pkTransferencia = pkTransferencia;
    }

    public Integer getPkTransferencia()
    {
        return pkTransferencia;
    }

    public void setPkTransferencia( Integer pkTransferencia )
    {
        this.pkTransferencia = pkTransferencia;
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

    public Double getValor()
    {
        return valor;
    }

    public void setValor( Double valor )
    {
        this.valor = valor;
    }

    public String getDescricao()
    {
        return descricao;
    }

    public void setDescricao( String descricao )
    {
        this.descricao = descricao;
    }

    public String getNborderaux()
    {
        return nborderaux;
    }

    public void setNborderaux( String nborderaux )
    {
        this.nborderaux = nborderaux;
    }

    public TbBanco getFkBancoDestino()
    {
        return fkBancoDestino;
    }

    public void setFkBancoDestino( TbBanco fkBancoDestino )
    {
        this.fkBancoDestino = fkBancoDestino;
    }

    public TbBanco getFkBancoOrigem()
    {
        return fkBancoOrigem;
    }

    public void setFkBancoOrigem( TbBanco fkBancoOrigem )
    {
        this.fkBancoOrigem = fkBancoOrigem;
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
        hash += ( pkTransferencia != null ? pkTransferencia.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if ( !( object instanceof TransferenciaBancaria ) )
        {
            return false;
        }
        TransferenciaBancaria other = ( TransferenciaBancaria ) object;
        if ( ( this.pkTransferencia == null && other.pkTransferencia != null ) || ( this.pkTransferencia != null && !this.pkTransferencia.equals( other.pkTransferencia ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.TransferenciaBancaria[ pkTransferencia=" + pkTransferencia + " ]";
    }
    
}
