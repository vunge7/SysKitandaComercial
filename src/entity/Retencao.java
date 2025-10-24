/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;


import java.sql.Connection;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author lenovo
 */
@Entity
@Table(name = "retencao")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "Retencao.findAll", query = "SELECT r FROM Retencao r"),
    @NamedQuery(name = "Retencao.findByPkRetencao", query = "SELECT r FROM Retencao r WHERE r.pkRetencao = :pkRetencao"),
    @NamedQuery(name = "Retencao.findByDescricao", query = "SELECT r FROM Retencao r WHERE r.descricao = :descricao"),
    @NamedQuery(name = "Retencao.findByTaxa", query = "SELECT r FROM Retencao r WHERE r.taxa = :taxa"),
    @NamedQuery(name = "Retencao.findByDataHora", query = "SELECT r FROM Retencao r WHERE r.dataHora = :dataHora")
})
public class Retencao implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pk_retencao")
    private Integer pkRetencao;
    @Column(name = "descricao")
    private String descricao;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "taxa")
    private Double taxa;
    @Column(name = "data_hora")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataHora;
    @OneToMany(mappedBy = "fkRetencao")
    private List<ServicoRetencao> servicoRetencaoList;

    public Retencao()
    {
    }

    public Retencao( Integer pkRetencao )
    {
        this.pkRetencao = pkRetencao;
    }

    public Integer getPkRetencao()
    {
        return pkRetencao;
    }

    public void setPkRetencao( Integer pkRetencao )
    {
        this.pkRetencao = pkRetencao;
    }

    public String getDescricao()
    {
        return descricao;
    }

    public void setDescricao( String descricao )
    {
        this.descricao = descricao;
    }

    public Double getTaxa()
    {
        return taxa;
    }

    public void setTaxa( Double taxa )
    {
        this.taxa = taxa;
    }

    public Date getDataHora()
    {
        return dataHora;
    }

    public void setDataHora( Date dataHora )
    {
        this.dataHora = dataHora;
    }

    @XmlTransient
    public List<ServicoRetencao> getServicoRetencaoList()
    {
        return servicoRetencaoList;
    }

    public void setServicoRetencaoList( List<ServicoRetencao> servicoRetencaoList )
    {
        this.servicoRetencaoList = servicoRetencaoList;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += ( pkRetencao != null ? pkRetencao.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if ( !( object instanceof Retencao ) )
        {
            return false;
        }
        Retencao other = ( Retencao ) object;
        if ( ( this.pkRetencao == null && other.pkRetencao != null ) || ( this.pkRetencao != null && !this.pkRetencao.equals( other.pkRetencao ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.Retencao[ pkRetencao=" + pkRetencao + " ]";
    }
    
}
