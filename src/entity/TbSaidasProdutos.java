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
@Table(name = "tb_saidas_produtos")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "TbSaidasProdutos.findAll", query = "SELECT t FROM TbSaidasProdutos t"),
    @NamedQuery(name = "TbSaidasProdutos.findByPkSaidasProdutos", query = "SELECT t FROM TbSaidasProdutos t WHERE t.pkSaidasProdutos = :pkSaidasProdutos"),
    @NamedQuery(name = "TbSaidasProdutos.findByDataSaida", query = "SELECT t FROM TbSaidasProdutos t WHERE t.dataSaida = :dataSaida"),
    @NamedQuery(name = "TbSaidasProdutos.findByHoraSaida", query = "SELECT t FROM TbSaidasProdutos t WHERE t.horaSaida = :horaSaida"),
    @NamedQuery(name = "TbSaidasProdutos.findByStatusEliminado", query = "SELECT t FROM TbSaidasProdutos t WHERE t.statusEliminado = :statusEliminado"),
    @NamedQuery(name = "TbSaidasProdutos.findByObs", query = "SELECT t FROM TbSaidasProdutos t WHERE t.obs = :obs"),
    @NamedQuery(name = "TbSaidasProdutos.findByNomeFuncionario", query = "SELECT t FROM TbSaidasProdutos t WHERE t.nomeFuncionario = :nomeFuncionario")
})
public class TbSaidasProdutos implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pk_saidas_produtos")
    private Integer pkSaidasProdutos;
    @Basic(optional = false)
    @Column(name = "data_saida")
    @Temporal(TemporalType.DATE)
    private Date dataSaida;
    @Basic(optional = false)
    @Column(name = "hora_saida")
    @Temporal(TemporalType.TIME)
    private Date horaSaida;
    @Basic(optional = false)
    @Column(name = "status_eliminado")
    private String statusEliminado;
    @Basic(optional = false)
    @Column(name = "obs")
    private String obs;
    @Column(name = "documento")
    private String documento;
    @Basic(optional = false)
    @Column(name = "nome_funcionario")
    private String nomeFuncionario;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkSaidasProdutos")
    private List<TbItemSaidas> tbItemSaidasList;
    @JoinColumn(name = "idArmazemFK", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private TbArmazem idArmazemFK;
    @JoinColumn(name = "fk_usuario", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private TbUsuario fkUsuario;

    public TbSaidasProdutos()
    {
    }

    public TbSaidasProdutos( Integer pkSaidasProdutos )
    {
        this.pkSaidasProdutos = pkSaidasProdutos;
    }

    public TbSaidasProdutos( Integer pkSaidasProdutos, Date dataSaida, Date horaSaida, String statusEliminado, String obs, String nomeFuncionario )
    {
        this.pkSaidasProdutos = pkSaidasProdutos;
        this.dataSaida = dataSaida;
        this.horaSaida = horaSaida;
        this.statusEliminado = statusEliminado;
        this.obs = obs;
        this.nomeFuncionario = nomeFuncionario;
    }

    public Integer getPkSaidasProdutos()
    {
        return pkSaidasProdutos;
    }

    public void setPkSaidasProdutos( Integer pkSaidasProdutos )
    {
        this.pkSaidasProdutos = pkSaidasProdutos;
    }

    public String getDocumento()
    {
        return documento;
    }

    public void setDocumento( String documento )
    {
        this.documento = documento;
    }

    public Date getDataSaida()
    {
        return dataSaida;
    }

    public void setDataSaida( Date dataSaida )
    {
        this.dataSaida = dataSaida;
    }

    public Date getHoraSaida()
    {
        return horaSaida;
    }

    public void setHoraSaida( Date horaSaida )
    {
        this.horaSaida = horaSaida;
    }

    public String getStatusEliminado()
    {
        return statusEliminado;
    }

    public void setStatusEliminado( String statusEliminado )
    {
        this.statusEliminado = statusEliminado;
    }

    public String getObs()
    {
        return obs;
    }

    public void setObs( String obs )
    {
        this.obs = obs;
    }

    public String getNomeFuncionario()
    {
        return nomeFuncionario;
    }

    public void setNomeFuncionario( String nomeFuncionario )
    {
        this.nomeFuncionario = nomeFuncionario;
    }

    @XmlTransient
    public List<TbItemSaidas> getTbItemSaidasList()
    {
        return tbItemSaidasList;
    }

    public void setTbItemSaidasList( List<TbItemSaidas> tbItemSaidasList )
    {
        this.tbItemSaidasList = tbItemSaidasList;
    }

    public TbArmazem getIdArmazemFK()
    {
        return idArmazemFK;
    }

    public void setIdArmazemFK( TbArmazem idArmazemFK )
    {
        this.idArmazemFK = idArmazemFK;
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
        hash += ( pkSaidasProdutos != null ? pkSaidasProdutos.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if ( !( object instanceof TbSaidasProdutos ) )
        {
            return false;
        }
        TbSaidasProdutos other = ( TbSaidasProdutos ) object;
        if ( ( this.pkSaidasProdutos == null && other.pkSaidasProdutos != null ) || ( this.pkSaidasProdutos != null && !this.pkSaidasProdutos.equals( other.pkSaidasProdutos ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.TbSaidasProdutos[ pkSaidasProdutos=" + pkSaidasProdutos + " ]";
    }
    
}
