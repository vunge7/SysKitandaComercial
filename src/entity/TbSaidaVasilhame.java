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
@Table(name = "tb_saida_vasilhame")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "TbSaidaVasilhame.findAll", query = "SELECT t FROM TbSaidaVasilhame t"),
    @NamedQuery(name = "TbSaidaVasilhame.findByPkSaidaVasilhame", query = "SELECT t FROM TbSaidaVasilhame t WHERE t.pkSaidaVasilhame = :pkSaidaVasilhame"),
    @NamedQuery(name = "TbSaidaVasilhame.findByDataSaida", query = "SELECT t FROM TbSaidaVasilhame t WHERE t.dataSaida = :dataSaida"),
    @NamedQuery(name = "TbSaidaVasilhame.findByHoraSaida", query = "SELECT t FROM TbSaidaVasilhame t WHERE t.horaSaida = :horaSaida"),
    @NamedQuery(name = "TbSaidaVasilhame.findByNomeFuncionario", query = "SELECT t FROM TbSaidaVasilhame t WHERE t.nomeFuncionario = :nomeFuncionario")
})
public class TbSaidaVasilhame implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pk_saida_vasilhame")
    private Integer pkSaidaVasilhame;
    @Basic(optional = false)
    @Column(name = "data_saida")
    @Temporal(TemporalType.DATE)
    private Date dataSaida;
    @Basic(optional = false)
    @Column(name = "hora_saida")
    @Temporal(TemporalType.TIME)
    private Date horaSaida;
    @Basic(optional = false)
    @Column(name = "nome_funcionario")
    private String nomeFuncionario;
    @JoinColumn(name = "fk_armazem", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private TbArmazem fkArmazem;
    @JoinColumn(name = "fk_usuario", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private TbUsuario fkUsuario;

    public TbSaidaVasilhame()
    {
    }

    public TbSaidaVasilhame( Integer pkSaidaVasilhame )
    {
        this.pkSaidaVasilhame = pkSaidaVasilhame;
    }

    public TbSaidaVasilhame( Integer pkSaidaVasilhame, Date dataSaida, Date horaSaida, String nomeFuncionario )
    {
        this.pkSaidaVasilhame = pkSaidaVasilhame;
        this.dataSaida = dataSaida;
        this.horaSaida = horaSaida;
        this.nomeFuncionario = nomeFuncionario;
    }

    public Integer getPkSaidaVasilhame()
    {
        return pkSaidaVasilhame;
    }

    public void setPkSaidaVasilhame( Integer pkSaidaVasilhame )
    {
        this.pkSaidaVasilhame = pkSaidaVasilhame;
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

    public String getNomeFuncionario()
    {
        return nomeFuncionario;
    }

    public void setNomeFuncionario( String nomeFuncionario )
    {
        this.nomeFuncionario = nomeFuncionario;
    }

    public TbArmazem getFkArmazem()
    {
        return fkArmazem;
    }

    public void setFkArmazem( TbArmazem fkArmazem )
    {
        this.fkArmazem = fkArmazem;
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
        hash += ( pkSaidaVasilhame != null ? pkSaidaVasilhame.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if ( !( object instanceof TbSaidaVasilhame ) )
        {
            return false;
        }
        TbSaidaVasilhame other = ( TbSaidaVasilhame ) object;
        if ( ( this.pkSaidaVasilhame == null && other.pkSaidaVasilhame != null ) || ( this.pkSaidaVasilhame != null && !this.pkSaidaVasilhame.equals( other.pkSaidaVasilhame ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.TbSaidaVasilhame[ pkSaidaVasilhame=" + pkSaidaVasilhame + " ]";
    }
    
}
