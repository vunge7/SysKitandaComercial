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
@Table(name = "tb_acerto")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "TbAcerto.findAll", query = "SELECT t FROM TbAcerto t"),
    @NamedQuery(name = "TbAcerto.findByIdAcerto", query = "SELECT t FROM TbAcerto t WHERE t.idAcerto = :idAcerto"),
    @NamedQuery(name = "TbAcerto.findByDataAcerto", query = "SELECT t FROM TbAcerto t WHERE t.dataAcerto = :dataAcerto"),
    @NamedQuery(name = "TbAcerto.findByQuantidade", query = "SELECT t FROM TbAcerto t WHERE t.quantidade = :quantidade")
})
public class TbAcerto implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idAcerto")
    private Integer idAcerto;
    @Column(name = "data_acerto")
    @Temporal(TemporalType.DATE)
    private Date dataAcerto;
    @Column(name = "quantidade")
    private Integer quantidade;
    @JoinColumn(name = "idArmazemFK", referencedColumnName = "codigo")
    @ManyToOne
    private TbArmazem idArmazemFK;
    @JoinColumn(name = "idProduto", referencedColumnName = "codigo")
    @ManyToOne
    private TbProduto idProduto;
    @JoinColumn(name = "idUsuario", referencedColumnName = "codigo")
    @ManyToOne
    private TbUsuario idUsuario;

    public TbAcerto()
    {
    }

    public TbAcerto( Integer idAcerto )
    {
        this.idAcerto = idAcerto;
    }

    public Integer getIdAcerto()
    {
        return idAcerto;
    }

    public void setIdAcerto( Integer idAcerto )
    {
        this.idAcerto = idAcerto;
    }

    public Date getDataAcerto()
    {
        return dataAcerto;
    }

    public void setDataAcerto( Date dataAcerto )
    {
        this.dataAcerto = dataAcerto;
    }

    public Integer getQuantidade()
    {
        return quantidade;
    }

    public void setQuantidade( Integer quantidade )
    {
        this.quantidade = quantidade;
    }

    public TbArmazem getIdArmazemFK()
    {
        return idArmazemFK;
    }

    public void setIdArmazemFK( TbArmazem idArmazemFK )
    {
        this.idArmazemFK = idArmazemFK;
    }

    public TbProduto getIdProduto()
    {
        return idProduto;
    }

    public void setIdProduto( TbProduto idProduto )
    {
        this.idProduto = idProduto;
    }

    public TbUsuario getIdUsuario()
    {
        return idUsuario;
    }

    public void setIdUsuario( TbUsuario idUsuario )
    {
        this.idUsuario = idUsuario;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += ( idAcerto != null ? idAcerto.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if ( !( object instanceof TbAcerto ) )
        {
            return false;
        }
        TbAcerto other = ( TbAcerto ) object;
        if ( ( this.idAcerto == null && other.idAcerto != null ) || ( this.idAcerto != null && !this.idAcerto.equals( other.idAcerto ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.TbAcerto[ idAcerto=" + idAcerto + " ]";
    }
    
}
