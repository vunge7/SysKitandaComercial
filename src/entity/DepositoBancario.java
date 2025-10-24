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
@Table(name = "deposito_bancario")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "DepositoBancario.findAll", query = "SELECT d FROM DepositoBancario d"),
    @NamedQuery(name = "DepositoBancario.findByPkDeposito", query = "SELECT d FROM DepositoBancario d WHERE d.pkDeposito = :pkDeposito"),
    @NamedQuery(name = "DepositoBancario.findByData", query = "SELECT d FROM DepositoBancario d WHERE d.data = :data"),
    @NamedQuery(name = "DepositoBancario.findByHora", query = "SELECT d FROM DepositoBancario d WHERE d.hora = :hora"),
    @NamedQuery(name = "DepositoBancario.findByNborderaux", query = "SELECT d FROM DepositoBancario d WHERE d.nborderaux = :nborderaux"),
    @NamedQuery(name = "DepositoBancario.findByValor", query = "SELECT d FROM DepositoBancario d WHERE d.valor = :valor"),
    @NamedQuery(name = "DepositoBancario.findByDescricao", query = "SELECT d FROM DepositoBancario d WHERE d.descricao = :descricao")
})
public class DepositoBancario implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pk_deposito")
    private Integer pkDeposito;
    @Column(name = "data")
    @Temporal(TemporalType.DATE)
    private Date data;
    @Column(name = "hora")
    @Temporal(TemporalType.TIME)
    private Date hora;
    @Column(name = "nborderaux")
    private String nborderaux;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor")
    private Double valor;
    @Column(name = "descricao")
    private String descricao;
    @JoinColumn(name = "fk_banco", referencedColumnName = "idBanco")
    @ManyToOne
    private TbBanco fkBanco;
    @JoinColumn(name = "fk_usuario", referencedColumnName = "codigo")
    @ManyToOne
    private TbUsuario fkUsuario;

    public DepositoBancario()
    {
    }

    public DepositoBancario( Integer pkDeposito )
    {
        this.pkDeposito = pkDeposito;
    }

    public Integer getPkDeposito()
    {
        return pkDeposito;
    }

    public void setPkDeposito( Integer pkDeposito )
    {
        this.pkDeposito = pkDeposito;
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

    public String getNborderaux()
    {
        return nborderaux;
    }

    public void setNborderaux( String nborderaux )
    {
        this.nborderaux = nborderaux;
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

    public TbBanco getFkBanco()
    {
        return fkBanco;
    }

    public void setFkBanco( TbBanco fkBanco )
    {
        this.fkBanco = fkBanco;
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
        hash += ( pkDeposito != null ? pkDeposito.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if ( !( object instanceof DepositoBancario ) )
        {
            return false;
        }
        DepositoBancario other = ( DepositoBancario ) object;
        if ( ( this.pkDeposito == null && other.pkDeposito != null ) || ( this.pkDeposito != null && !this.pkDeposito.equals( other.pkDeposito ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.DepositoBancario[ pkDeposito=" + pkDeposito + " ]";
    }
    
}
