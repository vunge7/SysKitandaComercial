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
@Table(name = "previo_aviso")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "PrevioAviso.findAll", query = "SELECT p FROM PrevioAviso p"),
    @NamedQuery(name = "PrevioAviso.findByPkPrevioAviso", query = "SELECT p FROM PrevioAviso p WHERE p.pkPrevioAviso = :pkPrevioAviso"),
    @NamedQuery(name = "PrevioAviso.findByDataPrevio", query = "SELECT p FROM PrevioAviso p WHERE p.dataPrevio = :dataPrevio"),
    @NamedQuery(name = "PrevioAviso.findByDescricao", query = "SELECT p FROM PrevioAviso p WHERE p.descricao = :descricao"),
    @NamedQuery(name = "PrevioAviso.findByNumeroDiasAviso", query = "SELECT p FROM PrevioAviso p WHERE p.numeroDiasAviso = :numeroDiasAviso")
})
public class PrevioAviso implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pk_previo_aviso")
    private Integer pkPrevioAviso;
    @Column(name = "data_previo")
    @Temporal(TemporalType.DATE)
    private Date dataPrevio;
    @Column(name = "descricao")
    private String descricao;
    @Column(name = "numero_dias_aviso")
    private Integer numeroDiasAviso;
    @JoinColumn(name = "fk_funcionario", referencedColumnName = "idFuncionario")
    @ManyToOne(optional = false)
    private TbFuncionario fkFuncionario;
    @JoinColumn(name = "fk_usuario", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private TbUsuario fkUsuario;

    public PrevioAviso()
    {
    }

    public PrevioAviso( Integer pkPrevioAviso )
    {
        this.pkPrevioAviso = pkPrevioAviso;
    }

    public Integer getPkPrevioAviso()
    {
        return pkPrevioAviso;
    }

    public void setPkPrevioAviso( Integer pkPrevioAviso )
    {
        this.pkPrevioAviso = pkPrevioAviso;
    }

    public Date getDataPrevio()
    {
        return dataPrevio;
    }

    public void setDataPrevio( Date dataPrevio )
    {
        this.dataPrevio = dataPrevio;
    }

    public String getDescricao()
    {
        return descricao;
    }

    public void setDescricao( String descricao )
    {
        this.descricao = descricao;
    }

    public Integer getNumeroDiasAviso()
    {
        return numeroDiasAviso;
    }

    public void setNumeroDiasAviso( Integer numeroDiasAviso )
    {
        this.numeroDiasAviso = numeroDiasAviso;
    }

    public TbFuncionario getFkFuncionario()
    {
        return fkFuncionario;
    }

    public void setFkFuncionario( TbFuncionario fkFuncionario )
    {
        this.fkFuncionario = fkFuncionario;
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
        hash += ( pkPrevioAviso != null ? pkPrevioAviso.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if ( !( object instanceof PrevioAviso ) )
        {
            return false;
        }
        PrevioAviso other = ( PrevioAviso ) object;
        if ( ( this.pkPrevioAviso == null && other.pkPrevioAviso != null ) || ( this.pkPrevioAviso != null && !this.pkPrevioAviso.equals( other.pkPrevioAviso ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.PrevioAviso[ pkPrevioAviso=" + pkPrevioAviso + " ]";
    }
    
}
