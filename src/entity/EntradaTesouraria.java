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
@Table(name = "entrada_tesouraria")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "EntradaTesouraria.findAll", query = "SELECT e FROM EntradaTesouraria e"),
    @NamedQuery(name = "EntradaTesouraria.findByPkEntradaTesouraria", query = "SELECT e FROM EntradaTesouraria e WHERE e.pkEntradaTesouraria = :pkEntradaTesouraria"),
    @NamedQuery(name = "EntradaTesouraria.findByDescricao", query = "SELECT e FROM EntradaTesouraria e WHERE e.descricao = :descricao"),
    @NamedQuery(name = "EntradaTesouraria.findByValor", query = "SELECT e FROM EntradaTesouraria e WHERE e.valor = :valor"),
    @NamedQuery(name = "EntradaTesouraria.findByDataEntrada", query = "SELECT e FROM EntradaTesouraria e WHERE e.dataEntrada = :dataEntrada"),
    @NamedQuery(name = "EntradaTesouraria.findByHoraEntrada", query = "SELECT e FROM EntradaTesouraria e WHERE e.horaEntrada = :horaEntrada")
})
public class EntradaTesouraria implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pk_entrada_tesouraria")
    private Integer pkEntradaTesouraria;
    @Column(name = "descricao")
    private String descricao;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor")
    private Double valor;
    @Column(name = "data_entrada")
    @Temporal(TemporalType.DATE)
    private Date dataEntrada;
    @Column(name = "hora_entrada")
    @Temporal(TemporalType.TIME)
    private Date horaEntrada;
    @JoinColumn(name = "fk_usuario", referencedColumnName = "codigo")
    @ManyToOne
    private TbUsuario fkUsuario;
    @JoinColumn(name = "fk_banco", referencedColumnName = "idBanco")
    @ManyToOne
    private TbBanco fkBanco;

    public EntradaTesouraria()
    {
    }

    public EntradaTesouraria( Integer pkEntradaTesouraria )
    {
        this.pkEntradaTesouraria = pkEntradaTesouraria;
    }

    public Integer getPkEntradaTesouraria()
    {
        return pkEntradaTesouraria;
    }

    public void setPkEntradaTesouraria( Integer pkEntradaTesouraria )
    {
        this.pkEntradaTesouraria = pkEntradaTesouraria;
    }

    public String getDescricao()
    {
        return descricao;
    }

    public void setDescricao( String descricao )
    {
        this.descricao = descricao;
    }

    public Double getValor()
    {
        return valor;
    }

    public void setValor( Double valor )
    {
        this.valor = valor;
    }

    public Date getDataEntrada()
    {
        return dataEntrada;
    }

    public void setDataEntrada( Date dataEntrada )
    {
        this.dataEntrada = dataEntrada;
    }

    public Date getHoraEntrada()
    {
        return horaEntrada;
    }

    public void setHoraEntrada( Date horaEntrada )
    {
        this.horaEntrada = horaEntrada;
    }

    public TbUsuario getFkUsuario()
    {
        return fkUsuario;
    }

    public void setFkUsuario( TbUsuario fkUsuario )
    {
        this.fkUsuario = fkUsuario;
    }

    public TbBanco getFkBanco()
    {
        return fkBanco;
    }

    public void setFkBanco( TbBanco fkBanco )
    {
        this.fkBanco = fkBanco;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += ( pkEntradaTesouraria != null ? pkEntradaTesouraria.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if ( !( object instanceof EntradaTesouraria ) )
        {
            return false;
        }
        EntradaTesouraria other = ( EntradaTesouraria ) object;
        if ( ( this.pkEntradaTesouraria == null && other.pkEntradaTesouraria != null ) || ( this.pkEntradaTesouraria != null && !this.pkEntradaTesouraria.equals( other.pkEntradaTesouraria ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.EntradaTesouraria[ pkEntradaTesouraria=" + pkEntradaTesouraria + " ]";
    }
    
}
