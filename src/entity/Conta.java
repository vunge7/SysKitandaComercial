/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Domingos Dala Vunge
 */
@Entity
@Table(name = "conta")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "Conta.findAll", query = "SELECT c FROM Conta c"),
    @NamedQuery(name = "Conta.findByIdConta", query = "SELECT c FROM Conta c WHERE c.idConta = :idConta"),
    @NamedQuery(name = "Conta.findByDescricao", query = "SELECT c FROM Conta c WHERE c.descricao = :descricao"),
    @NamedQuery(name = "Conta.findBySaldo", query = "SELECT c FROM Conta c WHERE c.saldo = :saldo")
})
public class Conta implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idConta")
    private Integer idConta;
    @Column(name = "descricao")
    private String descricao;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "saldo")
    private Double saldo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkConta")
    private List<MovimentoConta> movimentoContaList;
    @JoinColumn(name = "fk_cliente", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private TbCliente fkCliente;

    public Conta()
    {
    }

    public Conta( Integer idConta )
    {
        this.idConta = idConta;
    }

    public Integer getIdConta()
    {
        return idConta;
    }

    public void setIdConta( Integer idConta )
    {
        this.idConta = idConta;
    }

    public String getDescricao()
    {
        return descricao;
    }

    public void setDescricao( String descricao )
    {
        this.descricao = descricao;
    }

    public Double getSaldo()
    {
        return saldo;
    }

    public void setSaldo( Double saldo )
    {
        this.saldo = saldo;
    }

    @XmlTransient
    public List<MovimentoConta> getMovimentoContaList()
    {
        return movimentoContaList;
    }

    public void setMovimentoContaList( List<MovimentoConta> movimentoContaList )
    {
        this.movimentoContaList = movimentoContaList;
    }

    public TbCliente getFkCliente()
    {
        return fkCliente;
    }

    public void setFkCliente( TbCliente fkCliente )
    {
        this.fkCliente = fkCliente;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += ( idConta != null ? idConta.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if ( !( object instanceof Conta ) )
        {
            return false;
        }
        Conta other = ( Conta ) object;
        if ( ( this.idConta == null && other.idConta != null ) || ( this.idConta != null && !this.idConta.equals( other.idConta ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.Conta[ idConta=" + idConta + " ]";
    }
    
}
