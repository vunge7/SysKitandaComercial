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
@Table(name = "tb_adiantamento")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "TbAdiantamento.findAll", query = "SELECT t FROM TbAdiantamento t"),
    @NamedQuery(name = "TbAdiantamento.findByIdAdiantamentoFK", query = "SELECT t FROM TbAdiantamento t WHERE t.idAdiantamentoFK = :idAdiantamentoFK"),
    @NamedQuery(name = "TbAdiantamento.findByValorAdiantado", query = "SELECT t FROM TbAdiantamento t WHERE t.valorAdiantado = :valorAdiantado"),
    @NamedQuery(name = "TbAdiantamento.findByDescricao", query = "SELECT t FROM TbAdiantamento t WHERE t.descricao = :descricao"),
    @NamedQuery(name = "TbAdiantamento.findByData", query = "SELECT t FROM TbAdiantamento t WHERE t.data = :data"),
    @NamedQuery(name = "TbAdiantamento.findByHora", query = "SELECT t FROM TbAdiantamento t WHERE t.hora = :hora")
})
public class TbAdiantamento implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idAdiantamentoFK")
    private Integer idAdiantamentoFK;
    @Basic(optional = false)
    @Column(name = "valor_adiantado")
    private double valorAdiantado;
    @Basic(optional = false)
    @Column(name = "descricao")
    private String descricao;
    @Basic(optional = false)
    @Column(name = "data")
    @Temporal(TemporalType.DATE)
    private Date data;
    @Basic(optional = false)
    @Column(name = "hora")
    @Temporal(TemporalType.TIME)
    private Date hora;
    @JoinColumn(name = "idFuncionarioFK", referencedColumnName = "idFuncionario")
    @ManyToOne
    private TbFuncionario idFuncionarioFK;

    public TbAdiantamento()
    {
    }

    public TbAdiantamento( Integer idAdiantamentoFK )
    {
        this.idAdiantamentoFK = idAdiantamentoFK;
    }

    public TbAdiantamento( Integer idAdiantamentoFK, double valorAdiantado, String descricao, Date data, Date hora )
    {
        this.idAdiantamentoFK = idAdiantamentoFK;
        this.valorAdiantado = valorAdiantado;
        this.descricao = descricao;
        this.data = data;
        this.hora = hora;
    }

    public Integer getIdAdiantamentoFK()
    {
        return idAdiantamentoFK;
    }

    public void setIdAdiantamentoFK( Integer idAdiantamentoFK )
    {
        this.idAdiantamentoFK = idAdiantamentoFK;
    }

    public double getValorAdiantado()
    {
        return valorAdiantado;
    }

    public void setValorAdiantado( double valorAdiantado )
    {
        this.valorAdiantado = valorAdiantado;
    }

    public String getDescricao()
    {
        return descricao;
    }

    public void setDescricao( String descricao )
    {
        this.descricao = descricao;
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

    public TbFuncionario getIdFuncionarioFK()
    {
        return idFuncionarioFK;
    }

    public void setIdFuncionarioFK( TbFuncionario idFuncionarioFK )
    {
        this.idFuncionarioFK = idFuncionarioFK;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += ( idAdiantamentoFK != null ? idAdiantamentoFK.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if ( !( object instanceof TbAdiantamento ) )
        {
            return false;
        }
        TbAdiantamento other = ( TbAdiantamento ) object;
        if ( ( this.idAdiantamentoFK == null && other.idAdiantamentoFK != null ) || ( this.idAdiantamentoFK != null && !this.idAdiantamentoFK.equals( other.idAdiantamentoFK ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.TbAdiantamento[ idAdiantamentoFK=" + idAdiantamentoFK + " ]";
    }
    
}
