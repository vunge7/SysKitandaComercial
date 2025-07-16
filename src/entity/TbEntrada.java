/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "tb_entrada")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "TbEntrada.findAll", query = "SELECT t FROM TbEntrada t"),
    @NamedQuery(name = "TbEntrada.findByIdEntrada", query = "SELECT t FROM TbEntrada t WHERE t.idEntrada = :idEntrada"),
    @NamedQuery(name = "TbEntrada.findByDataEntrada", query = "SELECT t FROM TbEntrada t WHERE t.dataEntrada = :dataEntrada"),
    @NamedQuery(name = "TbEntrada.findByQuantidade", query = "SELECT t FROM TbEntrada t WHERE t.quantidade = :quantidade")
})
public class TbEntrada implements Serializable
{

    @Column( name = "status_eliminado" )
    private String statusEliminado;
    @OneToMany( cascade = CascadeType.ALL, mappedBy = "fkEntradas" )
    private List<TbItemEntradas> tbItemEntradasList;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idEntrada")
    private Integer idEntrada;
    @Basic(optional = false)
    @Column(name = "data_entrada")
    @Temporal(TemporalType.DATE)
    private Date dataEntrada;
    @Basic(optional = false)
    @Column(name = "quantidade")
    private int quantidade;
    @JoinColumn(name = "idUsuario", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private TbUsuario idUsuario;
    @JoinColumn(name = "idProduto", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private TbProduto idProduto;
    @JoinColumn(name = "idArmazemFK", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private TbArmazem idArmazemFK;

    public TbEntrada()
    {
    }

    public TbEntrada( Integer idEntrada )
    {
        this.idEntrada = idEntrada;
    }

    public TbEntrada( Integer idEntrada, Date dataEntrada, int quantidade )
    {
        this.idEntrada = idEntrada;
        this.dataEntrada = dataEntrada;
        this.quantidade = quantidade;
    }

    public Integer getIdEntrada()
    {
        return idEntrada;
    }

    public void setIdEntrada( Integer idEntrada )
    {
        this.idEntrada = idEntrada;
    }

    public Date getDataEntrada()
    {
        return dataEntrada;
    }

    public void setDataEntrada( Date dataEntrada )
    {
        this.dataEntrada = dataEntrada;
    }

    public int getQuantidade()
    {
        return quantidade;
    }

    public void setQuantidade( int quantidade )
    {
        this.quantidade = quantidade;
    }

    public TbUsuario getIdUsuario()
    {
        return idUsuario;
    }

    public void setIdUsuario( TbUsuario idUsuario )
    {
        this.idUsuario = idUsuario;
    }

    public TbProduto getIdProduto()
    {
        return idProduto;
    }

    public void setIdProduto( TbProduto idProduto )
    {
        this.idProduto = idProduto;
    }

    public TbArmazem getIdArmazemFK()
    {
        return idArmazemFK;
    }

    public void setIdArmazemFK( TbArmazem idArmazemFK )
    {
        this.idArmazemFK = idArmazemFK;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += ( idEntrada != null ? idEntrada.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if ( !( object instanceof TbEntrada ) )
        {
            return false;
        }
        TbEntrada other = ( TbEntrada ) object;
        if ( ( this.idEntrada == null && other.idEntrada != null ) || ( this.idEntrada != null && !this.idEntrada.equals( other.idEntrada ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.TbEntrada[ idEntrada=" + idEntrada + " ]";
    }

    public String getStatusEliminado()
    {
        return statusEliminado;
    }

    public void setStatusEliminado( String statusEliminado )
    {
        this.statusEliminado = statusEliminado;
    }

    @XmlTransient
    public List<TbItemEntradas> getTbItemEntradasList()
    {
        return tbItemEntradasList;
    }

    public void setTbItemEntradasList( List<TbItemEntradas> tbItemEntradasList )
    {
        this.tbItemEntradasList = tbItemEntradasList;
    }
    
}
