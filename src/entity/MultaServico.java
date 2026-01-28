/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
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

@Entity
@Table( name = "multa_servico" )
@XmlRootElement
@NamedQueries( 
{
    @NamedQuery( name = "MultaServico.findAll", query = "SELECT m FROM MultaServico m" ),
    @NamedQuery( name = "MultaServico.findById", query = "SELECT m FROM MultaServico m WHERE m.id = :id" ),
    @NamedQuery( name = "MultaServico.findByDayStart", query = "SELECT m FROM MultaServico m WHERE m.dayStart = :dayStart" ),
    @NamedQuery( name = "MultaServico.findByDayEnd", query = "SELECT m FROM MultaServico m WHERE m.dayEnd = :dayEnd" ),
    @NamedQuery( name = "MultaServico.findByValor", query = "SELECT m FROM MultaServico m WHERE m.valor = :valor" ),
    @NamedQuery( name = "MultaServico.findByDataRegistro", query = "SELECT m FROM MultaServico m WHERE m.dataRegistro = :dataRegistro" )
} )
public class MultaServico implements Serializable
{

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Basic( optional = false )
    @Column( name = "id" )
    private Integer id;

    @Basic( optional = false )
    @Column( name = "day_start" )
    private Integer dayStart;

    @Basic( optional = false )
    @Column( name = "day_end" )
    private Integer dayEnd;

    @Basic( optional = false )
    @Column( name = "valor" )
    private BigDecimal valor;

    @Basic( optional = false )
    @Column( name = "data_registro" )
    @Temporal( TemporalType.TIMESTAMP )
    private Date dataRegistro;

    // 🔗 Relacionamento com usuário
    @JoinColumn( name = "usuario_id", referencedColumnName = "codigo" )
    @ManyToOne( optional = false )
    private TbUsuario usuario;

    public MultaServico()
    {
    }

    public MultaServico( Integer id )
    {
        this.id = id;
    }

    public MultaServico( Integer id, Integer dayStart, Integer dayEnd, BigDecimal valor, Date dataRegistro )
    {
        this.id = id;
        this.dayStart = dayStart;
        this.dayEnd = dayEnd;
        this.valor = valor;
        this.dataRegistro = dataRegistro;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId( Integer id )
    {
        this.id = id;
    }

    public Integer getDayStart()
    {
        return dayStart;
    }

    public void setDayStart( Integer dayStart )
    {
        this.dayStart = dayStart;
    }

    public Integer getDayEnd()
    {
        return dayEnd;
    }

    public void setDayEnd( Integer dayEnd )
    {
        this.dayEnd = dayEnd;
    }

    public BigDecimal getValor()
    {
        return valor;
    }

    public void setValor( BigDecimal valor )
    {
        this.valor = valor;
    }

    public Date getDataRegistro()
    {
        return dataRegistro;
    }

    public void setDataRegistro( Date dataRegistro )
    {
        this.dataRegistro = dataRegistro;
    }

    public TbUsuario getUsuario()
    {
        return usuario;
    }

    public void setUsuario( TbUsuario usuario )
    {
        this.usuario = usuario;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += ( id != null ? id.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        if ( !( object instanceof MultaServico ) )
        {
            return false;
        }
        MultaServico other = ( MultaServico ) object;
        return !( ( this.id == null && other.id != null ) || ( this.id != null && !this.id.equals( other.id ) ) );
    }

    @Override
    public String toString()
    {
        return "entity.MultaServico[ id=" + id + " ]";
    }
}
