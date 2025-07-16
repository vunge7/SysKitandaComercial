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
@Table(name = "levantamento_bancario")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "LevantamentoBancario.findAll", query = "SELECT l FROM LevantamentoBancario l"),
    @NamedQuery(name = "LevantamentoBancario.findByPkLevantamento", query = "SELECT l FROM LevantamentoBancario l WHERE l.pkLevantamento = :pkLevantamento"),
    @NamedQuery(name = "LevantamentoBancario.findByData", query = "SELECT l FROM LevantamentoBancario l WHERE l.data = :data"),
    @NamedQuery(name = "LevantamentoBancario.findByHora", query = "SELECT l FROM LevantamentoBancario l WHERE l.hora = :hora"),
    @NamedQuery(name = "LevantamentoBancario.findByNborderaux", query = "SELECT l FROM LevantamentoBancario l WHERE l.nborderaux = :nborderaux"),
    @NamedQuery(name = "LevantamentoBancario.findByValor", query = "SELECT l FROM LevantamentoBancario l WHERE l.valor = :valor"),
    @NamedQuery(name = "LevantamentoBancario.findByObs", query = "SELECT l FROM LevantamentoBancario l WHERE l.obs = :obs")
})
public class LevantamentoBancario implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pk_levantamento")
    private Integer pkLevantamento;
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
    @Column(name = "obs")
    private String obs;
    @JoinColumn(name = "fk_banco", referencedColumnName = "idBanco")
    @ManyToOne
    private TbBanco fkBanco;
    @JoinColumn(name = "fk_usuario", referencedColumnName = "codigo")
    @ManyToOne
    private TbUsuario fkUsuario;

    public LevantamentoBancario()
    {
    }

    public LevantamentoBancario( Integer pkLevantamento )
    {
        this.pkLevantamento = pkLevantamento;
    }

    public Integer getPkLevantamento()
    {
        return pkLevantamento;
    }

    public void setPkLevantamento( Integer pkLevantamento )
    {
        this.pkLevantamento = pkLevantamento;
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

    public String getObs()
    {
        return obs;
    }

    public void setObs( String obs )
    {
        this.obs = obs;
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
        hash += ( pkLevantamento != null ? pkLevantamento.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if ( !( object instanceof LevantamentoBancario ) )
        {
            return false;
        }
        LevantamentoBancario other = ( LevantamentoBancario ) object;
        if ( ( this.pkLevantamento == null && other.pkLevantamento != null ) || ( this.pkLevantamento != null && !this.pkLevantamento.equals( other.pkLevantamento ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.LevantamentoBancario[ pkLevantamento=" + pkLevantamento + " ]";
    }
    
}
