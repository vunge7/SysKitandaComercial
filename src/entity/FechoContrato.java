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
@Table(name = "fecho_contrato")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "FechoContrato.findAll", query = "SELECT f FROM FechoContrato f"),
    @NamedQuery(name = "FechoContrato.findByPkFechoContrato", query = "SELECT f FROM FechoContrato f WHERE f.pkFechoContrato = :pkFechoContrato"),
    @NamedQuery(name = "FechoContrato.findByDataHora", query = "SELECT f FROM FechoContrato f WHERE f.dataHora = :dataHora"),
    @NamedQuery(name = "FechoContrato.findBySalarioBase", query = "SELECT f FROM FechoContrato f WHERE f.salarioBase = :salarioBase"),
    @NamedQuery(name = "FechoContrato.findByPercentagemFeria", query = "SELECT f FROM FechoContrato f WHERE f.percentagemFeria = :percentagemFeria"),
    @NamedQuery(name = "FechoContrato.findByValorFeria", query = "SELECT f FROM FechoContrato f WHERE f.valorFeria = :valorFeria"),
    @NamedQuery(name = "FechoContrato.findByPercentagemNatal", query = "SELECT f FROM FechoContrato f WHERE f.percentagemNatal = :percentagemNatal"),
    @NamedQuery(name = "FechoContrato.findByValorNatal", query = "SELECT f FROM FechoContrato f WHERE f.valorNatal = :valorNatal")
})
public class FechoContrato implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pk_fecho_contrato")
    private Integer pkFechoContrato;
    @Column(name = "data_hora")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataHora;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "salario_base")
    private Double salarioBase;
    @Column(name = "percentagem_feria")
    private Double percentagemFeria;
    @Column(name = "valor_feria")
    private Double valorFeria;
    @Column(name = "percentagem_natal")
    private Double percentagemNatal;
    @Column(name = "valor_natal")
    private Double valorNatal;
    @JoinColumn(name = "fk_funcionario", referencedColumnName = "idFuncionario")
    @ManyToOne(optional = false)
    private TbFuncionario fkFuncionario;
    @JoinColumn(name = "fk_usuario", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private TbUsuario fkUsuario;

    public FechoContrato()
    {
    }

    public FechoContrato( Integer pkFechoContrato )
    {
        this.pkFechoContrato = pkFechoContrato;
    }

    public Integer getPkFechoContrato()
    {
        return pkFechoContrato;
    }

    public void setPkFechoContrato( Integer pkFechoContrato )
    {
        this.pkFechoContrato = pkFechoContrato;
    }

    public Date getDataHora()
    {
        return dataHora;
    }

    public void setDataHora( Date dataHora )
    {
        this.dataHora = dataHora;
    }

    public Double getSalarioBase()
    {
        return salarioBase;
    }

    public void setSalarioBase( Double salarioBase )
    {
        this.salarioBase = salarioBase;
    }

    public Double getPercentagemFeria()
    {
        return percentagemFeria;
    }

    public void setPercentagemFeria( Double percentagemFeria )
    {
        this.percentagemFeria = percentagemFeria;
    }

    public Double getValorFeria()
    {
        return valorFeria;
    }

    public void setValorFeria( Double valorFeria )
    {
        this.valorFeria = valorFeria;
    }

    public Double getPercentagemNatal()
    {
        return percentagemNatal;
    }

    public void setPercentagemNatal( Double percentagemNatal )
    {
        this.percentagemNatal = percentagemNatal;
    }

    public Double getValorNatal()
    {
        return valorNatal;
    }

    public void setValorNatal( Double valorNatal )
    {
        this.valorNatal = valorNatal;
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
        hash += ( pkFechoContrato != null ? pkFechoContrato.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if ( !( object instanceof FechoContrato ) )
        {
            return false;
        }
        FechoContrato other = ( FechoContrato ) object;
        if ( ( this.pkFechoContrato == null && other.pkFechoContrato != null ) || ( this.pkFechoContrato != null && !this.pkFechoContrato.equals( other.pkFechoContrato ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.FechoContrato[ pkFechoContrato=" + pkFechoContrato + " ]";
    }
    
}
