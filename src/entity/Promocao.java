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
@Table(name = "promocao")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "Promocao.findAll", query = "SELECT p FROM Promocao p"),
    @NamedQuery(name = "Promocao.findByPkPromocao", query = "SELECT p FROM Promocao p WHERE p.pkPromocao = :pkPromocao"),
    @NamedQuery(name = "Promocao.findByPercentagem", query = "SELECT p FROM Promocao p WHERE p.percentagem = :percentagem"),
    @NamedQuery(name = "Promocao.findByData", query = "SELECT p FROM Promocao p WHERE p.data = :data"),
    @NamedQuery(name = "Promocao.findByHora", query = "SELECT p FROM Promocao p WHERE p.hora = :hora"),
    @NamedQuery(name = "Promocao.findByRetalhoGroso", query = "SELECT p FROM Promocao p WHERE p.retalhoGroso = :retalhoGroso")
})
public class Promocao implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pk_promocao")
    private Integer pkPromocao;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "percentagem")
    private Double percentagem;
    @Column(name = "data")
    @Temporal(TemporalType.DATE)
    private Date data;
    @Column(name = "hora")
    @Temporal(TemporalType.TIME)
    private Date hora;
    @Column(name = "retalho_groso")
    private Boolean retalhoGroso;
    @JoinColumn(name = "fk_usuario", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private TbUsuario fkUsuario;

    public Promocao()
    {
    }

    public Promocao( Integer pkPromocao )
    {
        this.pkPromocao = pkPromocao;
    }

    public Integer getPkPromocao()
    {
        return pkPromocao;
    }

    public void setPkPromocao( Integer pkPromocao )
    {
        this.pkPromocao = pkPromocao;
    }

    public Double getPercentagem()
    {
        return percentagem;
    }

    public void setPercentagem( Double percentagem )
    {
        this.percentagem = percentagem;
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

    public Boolean getRetalhoGroso()
    {
        return retalhoGroso;
    }

    public void setRetalhoGroso( Boolean retalhoGroso )
    {
        this.retalhoGroso = retalhoGroso;
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
        hash += ( pkPromocao != null ? pkPromocao.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if ( !( object instanceof Promocao ) )
        {
            return false;
        }
        Promocao other = ( Promocao ) object;
        if ( ( this.pkPromocao == null && other.pkPromocao != null ) || ( this.pkPromocao != null && !this.pkPromocao.equals( other.pkPromocao ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.Promocao[ pkPromocao=" + pkPromocao + " ]";
    }
    
}
