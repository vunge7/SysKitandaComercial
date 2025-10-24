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
@Table(name = "pagamento_subsidio_feria_natal")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "PagamentoSubsidioFeriaNatal.findAll", query = "SELECT p FROM PagamentoSubsidioFeriaNatal p"),
    @NamedQuery(name = "PagamentoSubsidioFeriaNatal.findByPkPagamentoSubsidioFeriaNatal", query = "SELECT p FROM PagamentoSubsidioFeriaNatal p WHERE p.pkPagamentoSubsidioFeriaNatal = :pkPagamentoSubsidioFeriaNatal"),
    @NamedQuery(name = "PagamentoSubsidioFeriaNatal.findByDataHora", query = "SELECT p FROM PagamentoSubsidioFeriaNatal p WHERE p.dataHora = :dataHora"),
    @NamedQuery(name = "PagamentoSubsidioFeriaNatal.findByPercentagem", query = "SELECT p FROM PagamentoSubsidioFeriaNatal p WHERE p.percentagem = :percentagem"),
    @NamedQuery(name = "PagamentoSubsidioFeriaNatal.findByValor", query = "SELECT p FROM PagamentoSubsidioFeriaNatal p WHERE p.valor = :valor"),
    @NamedQuery(name = "PagamentoSubsidioFeriaNatal.findBySalarioBase", query = "SELECT p FROM PagamentoSubsidioFeriaNatal p WHERE p.salarioBase = :salarioBase"),
    @NamedQuery(name = "PagamentoSubsidioFeriaNatal.findByTipoSubsideo", query = "SELECT p FROM PagamentoSubsidioFeriaNatal p WHERE p.tipoSubsideo = :tipoSubsideo"),
    @NamedQuery(name = "PagamentoSubsidioFeriaNatal.findByFormaPagamento", query = "SELECT p FROM PagamentoSubsidioFeriaNatal p WHERE p.formaPagamento = :formaPagamento"),
    @NamedQuery(name = "PagamentoSubsidioFeriaNatal.findByDescontoIrt", query = "SELECT p FROM PagamentoSubsidioFeriaNatal p WHERE p.descontoIrt = :descontoIrt")
})
public class PagamentoSubsidioFeriaNatal implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pk_pagamento_subsidio_feria_natal")
    private Integer pkPagamentoSubsidioFeriaNatal;
    @Column(name = "data_hora")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataHora;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "percentagem")
    private Double percentagem;
    @Column(name = "valor")
    private Double valor;
    @Column(name = "salario_base")
    private Double salarioBase;
    @Column(name = "tipo_subsideo")
    private String tipoSubsideo;
    @Column(name = "forma_pagamento")
    private String formaPagamento;
    @Column(name = "desconto_irt")
    private Double descontoIrt;
    @JoinColumn(name = "fk_funcionario", referencedColumnName = "idFuncionario")
    @ManyToOne(optional = false)
    private TbFuncionario fkFuncionario;
    @JoinColumn(name = "fk_usuario", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private TbUsuario fkUsuario;

    public PagamentoSubsidioFeriaNatal()
    {
    }

    public PagamentoSubsidioFeriaNatal( Integer pkPagamentoSubsidioFeriaNatal )
    {
        this.pkPagamentoSubsidioFeriaNatal = pkPagamentoSubsidioFeriaNatal;
    }

    public Integer getPkPagamentoSubsidioFeriaNatal()
    {
        return pkPagamentoSubsidioFeriaNatal;
    }

    public void setPkPagamentoSubsidioFeriaNatal( Integer pkPagamentoSubsidioFeriaNatal )
    {
        this.pkPagamentoSubsidioFeriaNatal = pkPagamentoSubsidioFeriaNatal;
    }

    public Date getDataHora()
    {
        return dataHora;
    }

    public void setDataHora( Date dataHora )
    {
        this.dataHora = dataHora;
    }

    public Double getPercentagem()
    {
        return percentagem;
    }

    public void setPercentagem( Double percentagem )
    {
        this.percentagem = percentagem;
    }

    public Double getValor()
    {
        return valor;
    }

    public void setValor( Double valor )
    {
        this.valor = valor;
    }

    public Double getSalarioBase()
    {
        return salarioBase;
    }

    public void setSalarioBase( Double salarioBase )
    {
        this.salarioBase = salarioBase;
    }

    public String getTipoSubsideo()
    {
        return tipoSubsideo;
    }

    public void setTipoSubsideo( String tipoSubsideo )
    {
        this.tipoSubsideo = tipoSubsideo;
    }

    public String getFormaPagamento()
    {
        return formaPagamento;
    }

    public void setFormaPagamento( String formaPagamento )
    {
        this.formaPagamento = formaPagamento;
    }

    public Double getDescontoIrt()
    {
        return descontoIrt;
    }

    public void setDescontoIrt( Double descontoIrt )
    {
        this.descontoIrt = descontoIrt;
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
        hash += ( pkPagamentoSubsidioFeriaNatal != null ? pkPagamentoSubsidioFeriaNatal.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if ( !( object instanceof PagamentoSubsidioFeriaNatal ) )
        {
            return false;
        }
        PagamentoSubsidioFeriaNatal other = ( PagamentoSubsidioFeriaNatal ) object;
        if ( ( this.pkPagamentoSubsidioFeriaNatal == null && other.pkPagamentoSubsidioFeriaNatal != null ) || ( this.pkPagamentoSubsidioFeriaNatal != null && !this.pkPagamentoSubsidioFeriaNatal.equals( other.pkPagamentoSubsidioFeriaNatal ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.PagamentoSubsidioFeriaNatal[ pkPagamentoSubsidioFeriaNatal=" + pkPagamentoSubsidioFeriaNatal + " ]";
    }
    
}
