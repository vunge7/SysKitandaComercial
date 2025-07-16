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
@Table(name = "tb_item_subsidios_funcionario")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "TbItemSubsidiosFuncionario.findAll", query = "SELECT t FROM TbItemSubsidiosFuncionario t"),
    @NamedQuery(name = "TbItemSubsidiosFuncionario.findByIdItemSubsidiosFuncionario", query = "SELECT t FROM TbItemSubsidiosFuncionario t WHERE t.idItemSubsidiosFuncionario = :idItemSubsidiosFuncionario"),
    @NamedQuery(name = "TbItemSubsidiosFuncionario.findByValorSubsidio", query = "SELECT t FROM TbItemSubsidiosFuncionario t WHERE t.valorSubsidio = :valorSubsidio")
})
public class TbItemSubsidiosFuncionario implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idItemSubsidiosFuncionario")
    private Integer idItemSubsidiosFuncionario;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor_subsidio")
    private Double valorSubsidio;
    @JoinColumn(name = "idFuncionarioFK", referencedColumnName = "idFuncionario")
    @ManyToOne
    private TbFuncionario idFuncionarioFK;
    @JoinColumn(name = "idSubsidioFK", referencedColumnName = "idSubsidios")
    @ManyToOne
    private TbSubsidios idSubsidioFK;

    public TbItemSubsidiosFuncionario()
    {
    }

    public TbItemSubsidiosFuncionario( Integer idItemSubsidiosFuncionario )
    {
        this.idItemSubsidiosFuncionario = idItemSubsidiosFuncionario;
    }

    public Integer getIdItemSubsidiosFuncionario()
    {
        return idItemSubsidiosFuncionario;
    }

    public void setIdItemSubsidiosFuncionario( Integer idItemSubsidiosFuncionario )
    {
        this.idItemSubsidiosFuncionario = idItemSubsidiosFuncionario;
    }

    public Double getValorSubsidio()
    {
        return valorSubsidio;
    }

    public void setValorSubsidio( Double valorSubsidio )
    {
        this.valorSubsidio = valorSubsidio;
    }

    public TbFuncionario getIdFuncionarioFK()
    {
        return idFuncionarioFK;
    }

    public void setIdFuncionarioFK( TbFuncionario idFuncionarioFK )
    {
        this.idFuncionarioFK = idFuncionarioFK;
    }

    public TbSubsidios getIdSubsidioFK()
    {
        return idSubsidioFK;
    }

    public void setIdSubsidioFK( TbSubsidios idSubsidioFK )
    {
        this.idSubsidioFK = idSubsidioFK;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += ( idItemSubsidiosFuncionario != null ? idItemSubsidiosFuncionario.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if ( !( object instanceof TbItemSubsidiosFuncionario ) )
        {
            return false;
        }
        TbItemSubsidiosFuncionario other = ( TbItemSubsidiosFuncionario ) object;
        if ( ( this.idItemSubsidiosFuncionario == null && other.idItemSubsidiosFuncionario != null ) || ( this.idItemSubsidiosFuncionario != null && !this.idItemSubsidiosFuncionario.equals( other.idItemSubsidiosFuncionario ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.TbItemSubsidiosFuncionario[ idItemSubsidiosFuncionario=" + idItemSubsidiosFuncionario + " ]";
    }
    
}
