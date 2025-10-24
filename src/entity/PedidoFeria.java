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
@Table(name = "pedido_feria")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "PedidoFeria.findAll", query = "SELECT p FROM PedidoFeria p"),
    @NamedQuery(name = "PedidoFeria.findByPkPedidoFeria", query = "SELECT p FROM PedidoFeria p WHERE p.pkPedidoFeria = :pkPedidoFeria"),
    @NamedQuery(name = "PedidoFeria.findByDataInicio", query = "SELECT p FROM PedidoFeria p WHERE p.dataInicio = :dataInicio"),
    @NamedQuery(name = "PedidoFeria.findByDataFim", query = "SELECT p FROM PedidoFeria p WHERE p.dataFim = :dataFim"),
    @NamedQuery(name = "PedidoFeria.findByDiasFerias", query = "SELECT p FROM PedidoFeria p WHERE p.diasFerias = :diasFerias"),
    @NamedQuery(name = "PedidoFeria.findByDataRegistro", query = "SELECT p FROM PedidoFeria p WHERE p.dataRegistro = :dataRegistro")
})
public class PedidoFeria implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pk_pedido_feria")
    private Integer pkPedidoFeria;
    @Column(name = "data_inicio")
    @Temporal(TemporalType.DATE)
    private Date dataInicio;
    @Column(name = "data_fim")
    @Temporal(TemporalType.DATE)
    private Date dataFim;
    @Column(name = "dias_ferias")
    private Integer diasFerias;
    @Column(name = "data_registro")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataRegistro;
    @JoinColumn(name = "fk_funcionario", referencedColumnName = "idFuncionario")
    @ManyToOne(optional = false)
    private TbFuncionario fkFuncionario;
    @JoinColumn(name = "fk_usuario", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private TbUsuario fkUsuario;

    public PedidoFeria()
    {
    }

    public PedidoFeria( Integer pkPedidoFeria )
    {
        this.pkPedidoFeria = pkPedidoFeria;
    }

    public Integer getPkPedidoFeria()
    {
        return pkPedidoFeria;
    }

    public void setPkPedidoFeria( Integer pkPedidoFeria )
    {
        this.pkPedidoFeria = pkPedidoFeria;
    }

    public Date getDataInicio()
    {
        return dataInicio;
    }

    public void setDataInicio( Date dataInicio )
    {
        this.dataInicio = dataInicio;
    }

    public Date getDataFim()
    {
        return dataFim;
    }

    public void setDataFim( Date dataFim )
    {
        this.dataFim = dataFim;
    }

    public Integer getDiasFerias()
    {
        return diasFerias;
    }

    public void setDiasFerias( Integer diasFerias )
    {
        this.diasFerias = diasFerias;
    }

    public Date getDataRegistro()
    {
        return dataRegistro;
    }

    public void setDataRegistro( Date dataRegistro )
    {
        this.dataRegistro = dataRegistro;
    }

    public TbFuncionario getFkFuncionario()
    {
        return fkFuncionario;
    }

    public void setFkFuncionario( TbFuncionario fkFuncionario )
    {
        this.fkFuncionario = fkFuncionario;
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
        hash += ( pkPedidoFeria != null ? pkPedidoFeria.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if ( !( object instanceof PedidoFeria ) )
        {
            return false;
        }
        PedidoFeria other = ( PedidoFeria ) object;
        if ( ( this.pkPedidoFeria == null && other.pkPedidoFeria != null ) || ( this.pkPedidoFeria != null && !this.pkPedidoFeria.equals( other.pkPedidoFeria ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.PedidoFeria[ pkPedidoFeria=" + pkPedidoFeria + " ]";
    }
    
}
