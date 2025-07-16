/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

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
@Table(name = "recibo_rh")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "ReciboRh.findAll", query = "SELECT r FROM ReciboRh r"),
    @NamedQuery(name = "ReciboRh.findByPkReciboRh", query = "SELECT r FROM ReciboRh r WHERE r.pkReciboRh = :pkReciboRh"),
    @NamedQuery(name = "ReciboRh.findByDataHora", query = "SELECT r FROM ReciboRh r WHERE r.dataHora = :dataHora"),
    @NamedQuery(name = "ReciboRh.findByTotalRemuneracao", query = "SELECT r FROM ReciboRh r WHERE r.totalRemuneracao = :totalRemuneracao"),
    @NamedQuery(name = "ReciboRh.findByTotalDesconto", query = "SELECT r FROM ReciboRh r WHERE r.totalDesconto = :totalDesconto"),
    @NamedQuery(name = "ReciboRh.findByTotalPago", query = "SELECT r FROM ReciboRh r WHERE r.totalPago = :totalPago"),
    @NamedQuery(name = "ReciboRh.findByVencimento", query = "SELECT r FROM ReciboRh r WHERE r.vencimento = :vencimento"),
    @NamedQuery(name = "ReciboRh.findByDiasTrabalhado", query = "SELECT r FROM ReciboRh r WHERE r.diasTrabalhado = :diasTrabalhado"),
    @NamedQuery(name = "ReciboRh.findByPeriodo", query = "SELECT r FROM ReciboRh r WHERE r.periodo = :periodo")
})
public class ReciboRh implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pk_recibo_rh")
    private Integer pkReciboRh;
    @Column(name = "data_hora")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataHora;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "total_remuneracao")
    private Double totalRemuneracao;
    @Column(name = "total_desconto")
    private Double totalDesconto;
    @Column(name = "total_pago")
    private Double totalPago;
    @Column(name = "vencimento")
    private Double vencimento;
    @Column(name = "dias_trabalhado")
    private Double diasTrabalhado;
    @Column(name = "periodo")
    private String periodo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkReciboRh")
    private List<ItemReciboRh> itemReciboRhList;
    @JoinColumn(name = "fk_fecho_periodo", referencedColumnName = "pk_fecho_periodo")
    @ManyToOne
    private FechoPeriodo fkFechoPeriodo;
    @JoinColumn(name = "fk_forma_pagamento", referencedColumnName = "pk_forma_pagamento")
    @ManyToOne(optional = false)
    private FormaPagamento fkFormaPagamento;
    @JoinColumn(name = "fk_funcionario", referencedColumnName = "idFuncionario")
    @ManyToOne(optional = false)
    private TbFuncionario fkFuncionario;

    public ReciboRh()
    {
    }

    public ReciboRh( Integer pkReciboRh )
    {
        this.pkReciboRh = pkReciboRh;
    }

    public Integer getPkReciboRh()
    {
        return pkReciboRh;
    }

    public void setPkReciboRh( Integer pkReciboRh )
    {
        this.pkReciboRh = pkReciboRh;
    }

    public Date getDataHora()
    {
        return dataHora;
    }

    public void setDataHora( Date dataHora )
    {
        this.dataHora = dataHora;
    }

    public Double getTotalRemuneracao()
    {
        return totalRemuneracao;
    }

    public void setTotalRemuneracao( Double totalRemuneracao )
    {
        this.totalRemuneracao = totalRemuneracao;
    }

    public Double getTotalDesconto()
    {
        return totalDesconto;
    }

    public void setTotalDesconto( Double totalDesconto )
    {
        this.totalDesconto = totalDesconto;
    }

    public Double getTotalPago()
    {
        return totalPago;
    }

    public void setTotalPago( Double totalPago )
    {
        this.totalPago = totalPago;
    }

    public Double getVencimento()
    {
        return vencimento;
    }

    public void setVencimento( Double vencimento )
    {
        this.vencimento = vencimento;
    }

    public Double getDiasTrabalhado()
    {
        return diasTrabalhado;
    }

    public void setDiasTrabalhado( Double diasTrabalhado )
    {
        this.diasTrabalhado = diasTrabalhado;
    }

    public String getPeriodo()
    {
        return periodo;
    }

    public void setPeriodo( String periodo )
    {
        this.periodo = periodo;
    }

    @XmlTransient
    public List<ItemReciboRh> getItemReciboRhList()
    {
        return itemReciboRhList;
    }

    public void setItemReciboRhList( List<ItemReciboRh> itemReciboRhList )
    {
        this.itemReciboRhList = itemReciboRhList;
    }

    public FechoPeriodo getFkFechoPeriodo()
    {
        return fkFechoPeriodo;
    }

    public void setFkFechoPeriodo( FechoPeriodo fkFechoPeriodo )
    {
        this.fkFechoPeriodo = fkFechoPeriodo;
    }

    public FormaPagamento getFkFormaPagamento()
    {
        return fkFormaPagamento;
    }

    public void setFkFormaPagamento( FormaPagamento fkFormaPagamento )
    {
        this.fkFormaPagamento = fkFormaPagamento;
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
        hash += ( pkReciboRh != null ? pkReciboRh.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if ( !( object instanceof ReciboRh ) )
        {
            return false;
        }
        ReciboRh other = ( ReciboRh ) object;
        if ( ( this.pkReciboRh == null && other.pkReciboRh != null ) || ( this.pkReciboRh != null && !this.pkReciboRh.equals( other.pkReciboRh ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.ReciboRh[ pkReciboRh=" + pkReciboRh + " ]";
    }
    
}
