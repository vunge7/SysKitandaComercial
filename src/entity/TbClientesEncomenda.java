/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;


import java.sql.Connection;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author lenovo
 */
@Entity
@Table(name = "tb_clientes_encomenda")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "TbClientesEncomenda.findAll", query = "SELECT t FROM TbClientesEncomenda t"),
    @NamedQuery(name = "TbClientesEncomenda.findByIdCliente", query = "SELECT t FROM TbClientesEncomenda t WHERE t.idCliente = :idCliente"),
    @NamedQuery(name = "TbClientesEncomenda.findByNomeCliente", query = "SELECT t FROM TbClientesEncomenda t WHERE t.nomeCliente = :nomeCliente"),
    @NamedQuery(name = "TbClientesEncomenda.findByTelefone", query = "SELECT t FROM TbClientesEncomenda t WHERE t.telefone = :telefone"),
    @NamedQuery(name = "TbClientesEncomenda.findByEndereco", query = "SELECT t FROM TbClientesEncomenda t WHERE t.endereco = :endereco"),
    @NamedQuery(name = "TbClientesEncomenda.findBySaldo", query = "SELECT t FROM TbClientesEncomenda t WHERE t.saldo = :saldo"),
    @NamedQuery(name = "TbClientesEncomenda.findByEmail", query = "SELECT t FROM TbClientesEncomenda t WHERE t.email = :email"),
    @NamedQuery(name = "TbClientesEncomenda.findByCredito", query = "SELECT t FROM TbClientesEncomenda t WHERE t.credito = :credito")
})
public class TbClientesEncomenda implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idCliente")
    private Long idCliente;
    @Basic(optional = false)
    @Column(name = "nome_cliente")
    private String nomeCliente;
    @Basic(optional = false)
    @Column(name = "telefone")
    private String telefone;
    @Basic(optional = false)
    @Column(name = "endereco")
    private String endereco;
    @Basic(optional = false)
    @Column(name = "saldo")
    private double saldo;
    @Basic(optional = false)
    @Column(name = "email")
    private String email;
    @Basic(optional = false)
    @Column(name = "credito")
    private double credito;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idCliente")
    private List<TbEncomenda> tbEncomendaList;

    public TbClientesEncomenda()
    {
    }

    public TbClientesEncomenda( Long idCliente )
    {
        this.idCliente = idCliente;
    }

    public TbClientesEncomenda( Long idCliente, String nomeCliente, String telefone, String endereco, double saldo, String email, double credito )
    {
        this.idCliente = idCliente;
        this.nomeCliente = nomeCliente;
        this.telefone = telefone;
        this.endereco = endereco;
        this.saldo = saldo;
        this.email = email;
        this.credito = credito;
    }

    public Long getIdCliente()
    {
        return idCliente;
    }

    public void setIdCliente( Long idCliente )
    {
        this.idCliente = idCliente;
    }

    public String getNomeCliente()
    {
        return nomeCliente;
    }

    public void setNomeCliente( String nomeCliente )
    {
        this.nomeCliente = nomeCliente;
    }

    public String getTelefone()
    {
        return telefone;
    }

    public void setTelefone( String telefone )
    {
        this.telefone = telefone;
    }

    public String getEndereco()
    {
        return endereco;
    }

    public void setEndereco( String endereco )
    {
        this.endereco = endereco;
    }

    public double getSaldo()
    {
        return saldo;
    }

    public void setSaldo( double saldo )
    {
        this.saldo = saldo;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail( String email )
    {
        this.email = email;
    }

    public double getCredito()
    {
        return credito;
    }

    public void setCredito( double credito )
    {
        this.credito = credito;
    }

    @XmlTransient
    public List<TbEncomenda> getTbEncomendaList()
    {
        return tbEncomendaList;
    }

    public void setTbEncomendaList( List<TbEncomenda> tbEncomendaList )
    {
        this.tbEncomendaList = tbEncomendaList;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += ( idCliente != null ? idCliente.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if ( !( object instanceof TbClientesEncomenda ) )
        {
            return false;
        }
        TbClientesEncomenda other = ( TbClientesEncomenda ) object;
        if ( ( this.idCliente == null && other.idCliente != null ) || ( this.idCliente != null && !this.idCliente.equals( other.idCliente ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.TbClientesEncomenda[ idCliente=" + idCliente + " ]";
    }
    
}
