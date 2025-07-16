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
@Table(name = "agregado_familiar")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "AgregadoFamiliar.findAll", query = "SELECT a FROM AgregadoFamiliar a"),
    @NamedQuery(name = "AgregadoFamiliar.findByPkAgregadoFamiliar", query = "SELECT a FROM AgregadoFamiliar a WHERE a.pkAgregadoFamiliar = :pkAgregadoFamiliar"),
    @NamedQuery(name = "AgregadoFamiliar.findByNomeFilho", query = "SELECT a FROM AgregadoFamiliar a WHERE a.nomeFilho = :nomeFilho"),
    @NamedQuery(name = "AgregadoFamiliar.findByDataNascimento", query = "SELECT a FROM AgregadoFamiliar a WHERE a.dataNascimento = :dataNascimento"),
    @NamedQuery(name = "AgregadoFamiliar.findByValor", query = "SELECT a FROM AgregadoFamiliar a WHERE a.valor = :valor")
})
public class AgregadoFamiliar implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pk_agregado_familiar")
    private Integer pkAgregadoFamiliar;
    @Column(name = "nome_filho")
    private String nomeFilho;
    @Column(name = "data_nascimento")
    @Temporal(TemporalType.DATE)
    private Date dataNascimento;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor")
    private Double valor;
    @JoinColumn(name = "fk_funcionario", referencedColumnName = "idFuncionario")
    @ManyToOne(optional = false)
    private TbFuncionario fkFuncionario;

    public AgregadoFamiliar()
    {
    }

    public AgregadoFamiliar( Integer pkAgregadoFamiliar )
    {
        this.pkAgregadoFamiliar = pkAgregadoFamiliar;
    }

    public Integer getPkAgregadoFamiliar()
    {
        return pkAgregadoFamiliar;
    }

    public void setPkAgregadoFamiliar( Integer pkAgregadoFamiliar )
    {
        this.pkAgregadoFamiliar = pkAgregadoFamiliar;
    }

    public String getNomeFilho()
    {
        return nomeFilho;
    }

    public void setNomeFilho( String nomeFilho )
    {
        this.nomeFilho = nomeFilho;
    }

    public Date getDataNascimento()
    {
        return dataNascimento;
    }

    public void setDataNascimento( Date dataNascimento )
    {
        this.dataNascimento = dataNascimento;
    }

    public Double getValor()
    {
        return valor;
    }

    public void setValor( Double valor )
    {
        this.valor = valor;
    }

    public TbFuncionario getFkFuncionario()
    {
        return fkFuncionario;
    }

    public void setFkFuncionario( TbFuncionario fkFuncionario )
    {
        this.fkFuncionario = fkFuncionario;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += ( pkAgregadoFamiliar != null ? pkAgregadoFamiliar.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if ( !( object instanceof AgregadoFamiliar ) )
        {
            return false;
        }
        AgregadoFamiliar other = ( AgregadoFamiliar ) object;
        if ( ( this.pkAgregadoFamiliar == null && other.pkAgregadoFamiliar != null ) || ( this.pkAgregadoFamiliar != null && !this.pkAgregadoFamiliar.equals( other.pkAgregadoFamiliar ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.AgregadoFamiliar[ pkAgregadoFamiliar=" + pkAgregadoFamiliar + " ]";
    }
    
}
