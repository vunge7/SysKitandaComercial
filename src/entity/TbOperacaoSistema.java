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
@Table(name = "tb_operacao_sistema")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "TbOperacaoSistema.findAll", query = "SELECT t FROM TbOperacaoSistema t"),
    @NamedQuery(name = "TbOperacaoSistema.findByPkOperacaoSistema", query = "SELECT t FROM TbOperacaoSistema t WHERE t.pkOperacaoSistema = :pkOperacaoSistema"),
    @NamedQuery(name = "TbOperacaoSistema.findByDataAbertura", query = "SELECT t FROM TbOperacaoSistema t WHERE t.dataAbertura = :dataAbertura"),
    @NamedQuery(name = "TbOperacaoSistema.findByHoraAbertura", query = "SELECT t FROM TbOperacaoSistema t WHERE t.horaAbertura = :horaAbertura"),
    @NamedQuery(name = "TbOperacaoSistema.findByDataFeicho", query = "SELECT t FROM TbOperacaoSistema t WHERE t.dataFeicho = :dataFeicho"),
    @NamedQuery(name = "TbOperacaoSistema.findByHoraFeicho", query = "SELECT t FROM TbOperacaoSistema t WHERE t.horaFeicho = :horaFeicho")
})
public class TbOperacaoSistema implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pk_operacao_sistema")
    private Long pkOperacaoSistema;
    @Column(name = "data_abertura")
    @Temporal(TemporalType.DATE)
    private Date dataAbertura;
    @Column(name = "hora_abertura")
    @Temporal(TemporalType.TIME)
    private Date horaAbertura;
    @Column(name = "data_feicho")
    @Temporal(TemporalType.DATE)
    private Date dataFeicho;
    @Column(name = "hora_feicho")
    @Temporal(TemporalType.TIME)
    private Date horaFeicho;
    @JoinColumn(name = "fk_usuario", referencedColumnName = "codigo")
    @ManyToOne
    private TbUsuario fkUsuario;

    public TbOperacaoSistema()
    {
    }

    public TbOperacaoSistema( Long pkOperacaoSistema )
    {
        this.pkOperacaoSistema = pkOperacaoSistema;
    }

    public Long getPkOperacaoSistema()
    {
        return pkOperacaoSistema;
    }

    public void setPkOperacaoSistema( Long pkOperacaoSistema )
    {
        this.pkOperacaoSistema = pkOperacaoSistema;
    }

    public Date getDataAbertura()
    {
        return dataAbertura;
    }

    public void setDataAbertura( Date dataAbertura )
    {
        this.dataAbertura = dataAbertura;
    }

    public Date getHoraAbertura()
    {
        return horaAbertura;
    }

    public void setHoraAbertura( Date horaAbertura )
    {
        this.horaAbertura = horaAbertura;
    }

    public Date getDataFeicho()
    {
        return dataFeicho;
    }

    public void setDataFeicho( Date dataFeicho )
    {
        this.dataFeicho = dataFeicho;
    }

    public Date getHoraFeicho()
    {
        return horaFeicho;
    }

    public void setHoraFeicho( Date horaFeicho )
    {
        this.horaFeicho = horaFeicho;
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
        hash += ( pkOperacaoSistema != null ? pkOperacaoSistema.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if ( !( object instanceof TbOperacaoSistema ) )
        {
            return false;
        }
        TbOperacaoSistema other = ( TbOperacaoSistema ) object;
        if ( ( this.pkOperacaoSistema == null && other.pkOperacaoSistema != null ) || ( this.pkOperacaoSistema != null && !this.pkOperacaoSistema.equals( other.pkOperacaoSistema ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.TbOperacaoSistema[ pkOperacaoSistema=" + pkOperacaoSistema + " ]";
    }
    
}
