/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author lenovo
 */
@Entity
@Table(name = "tb_salario")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "TbSalario.findAll", query = "SELECT t FROM TbSalario t"),
    @NamedQuery(name = "TbSalario.findByIdSalarioFK", query = "SELECT t FROM TbSalario t WHERE t.idSalarioFK = :idSalarioFK"),
    @NamedQuery(name = "TbSalario.findByValorTempo", query = "SELECT t FROM TbSalario t WHERE t.valorTempo = :valorTempo")
})
public class TbSalario implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idSalarioFK")
    private Integer idSalarioFK;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor_tempo")
    private Double valorTempo;
    @JoinColumn(name = "idFuncionarioFK", referencedColumnName = "idFuncionario")
    @ManyToOne
    private TbFuncionario idFuncionarioFK;

    public TbSalario()
    {
    }

    public TbSalario( Integer idSalarioFK )
    {
        this.idSalarioFK = idSalarioFK;
    }

    public Integer getIdSalarioFK()
    {
        return idSalarioFK;
    }

    public void setIdSalarioFK( Integer idSalarioFK )
    {
        this.idSalarioFK = idSalarioFK;
    }

    public Double getValorTempo()
    {
        return valorTempo;
    }

    public void setValorTempo( Double valorTempo )
    {
        this.valorTempo = valorTempo;
    }

    public TbFuncionario getIdFuncionarioFK()
    {
        return idFuncionarioFK;
    }

    public void setIdFuncionarioFK( TbFuncionario idFuncionarioFK )
    {
        this.idFuncionarioFK = idFuncionarioFK;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += ( idSalarioFK != null ? idSalarioFK.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if ( !( object instanceof TbSalario ) )
        {
            return false;
        }
        TbSalario other = ( TbSalario ) object;
        if ( ( this.idSalarioFK == null && other.idSalarioFK != null ) || ( this.idSalarioFK != null && !this.idSalarioFK.equals( other.idSalarioFK ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.TbSalario[ idSalarioFK=" + idSalarioFK + " ]";
    }
    
}
