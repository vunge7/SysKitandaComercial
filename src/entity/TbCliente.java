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
@Table(name = "tb_cliente")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "TbCliente.findAll", query = "SELECT t FROM TbCliente t"),
    @NamedQuery(name = "TbCliente.findByCodigo", query = "SELECT t FROM TbCliente t WHERE t.codigo = :codigo"),
    @NamedQuery(name = "TbCliente.findByNome", query = "SELECT t FROM TbCliente t WHERE t.nome = :nome"),
    @NamedQuery(name = "TbCliente.findByMorada", query = "SELECT t FROM TbCliente t WHERE t.morada = :morada"),
    @NamedQuery(name = "TbCliente.findByTelefone", query = "SELECT t FROM TbCliente t WHERE t.telefone = :telefone"),
    @NamedQuery(name = "TbCliente.findByNif", query = "SELECT t FROM TbCliente t WHERE t.nif = :nif"),
    @NamedQuery(name = "TbCliente.findByEmail", query = "SELECT t FROM TbCliente t WHERE t.email = :email")
})
public class TbCliente implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "codigo")
    private Integer codigo;
    @Column(name = "nome")
    private String nome;
    @Column(name = "morada")
    private String morada;
    @Column(name = "telefone")
    private String telefone;
    @Column(name = "nif")
    private String nif;
    @Column(name = "email")
    private String email;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "codigoCliente")
    private List<Notas> notasList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkCliente")
    private List<TbProForma> tbProFormaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkCliente")
    private List<TbDesconto> tbDescontoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "codigoCliente")
    private List<TbVenda> tbVendaList;

    public TbCliente()
    {
    }

    public TbCliente( Integer codigo )
    {
        this.codigo = codigo;
    }

    public Integer getCodigo()
    {
        return codigo;
    }

    public void setCodigo( Integer codigo )
    {
        this.codigo = codigo;
    }

    public String getNome()
    {
        return nome;
    }

    public void setNome( String nome )
    {
        this.nome = nome;
    }

    public String getMorada()
    {
        return morada;
    }

    public void setMorada( String morada )
    {
        this.morada = morada;
    }

    public String getTelefone()
    {
        return telefone;
    }

    public void setTelefone( String telefone )
    {
        this.telefone = telefone;
    }

    public String getNif()
    {
        return nif;
    }

    public void setNif( String nif )
    {
        this.nif = nif;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail( String email )
    {
        this.email = email;
    }

    @XmlTransient
    public List<Notas> getNotasList()
    {
        return notasList;
    }

    public void setNotasList( List<Notas> notasList )
    {
        this.notasList = notasList;
    }

    @XmlTransient
    public List<TbProForma> getTbProFormaList()
    {
        return tbProFormaList;
    }

    public void setTbProFormaList( List<TbProForma> tbProFormaList )
    {
        this.tbProFormaList = tbProFormaList;
    }

    @XmlTransient
    public List<TbDesconto> getTbDescontoList()
    {
        return tbDescontoList;
    }

    public void setTbDescontoList( List<TbDesconto> tbDescontoList )
    {
        this.tbDescontoList = tbDescontoList;
    }

    @XmlTransient
    public List<TbVenda> getTbVendaList()
    {
        return tbVendaList;
    }

    public void setTbVendaList( List<TbVenda> tbVendaList )
    {
        this.tbVendaList = tbVendaList;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += ( codigo != null ? codigo.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if ( !( object instanceof TbCliente ) )
        {
            return false;
        }
        TbCliente other = ( TbCliente ) object;
        if ( ( this.codigo == null && other.codigo != null ) || ( this.codigo != null && !this.codigo.equals( other.codigo ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.TbCliente[ codigo=" + codigo + " ]";
    }
    
}
