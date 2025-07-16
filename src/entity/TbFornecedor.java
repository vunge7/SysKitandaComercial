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
 * @author lenovo
 */
@Entity
@Table(name = "tb_fornecedor")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "TbFornecedor.findAll", query = "SELECT t FROM TbFornecedor t"),
    @NamedQuery(name = "TbFornecedor.findByCodigo", query = "SELECT t FROM TbFornecedor t WHERE t.codigo = :codigo"),
    @NamedQuery(name = "TbFornecedor.findByNome", query = "SELECT t FROM TbFornecedor t WHERE t.nome = :nome"),
    @NamedQuery(name = "TbFornecedor.findByTelefone", query = "SELECT t FROM TbFornecedor t WHERE t.telefone = :telefone"),
    @NamedQuery(name = "TbFornecedor.findByEmail", query = "SELECT t FROM TbFornecedor t WHERE t.email = :email"),
    @NamedQuery(name = "TbFornecedor.findBySite", query = "SELECT t FROM TbFornecedor t WHERE t.site = :site"),
    @NamedQuery(name = "TbFornecedor.findByEndereco", query = "SELECT t FROM TbFornecedor t WHERE t.endereco = :endereco"),
    @NamedQuery(name = "TbFornecedor.findByNif", query = "SELECT t FROM TbFornecedor t WHERE t.nif = :nif")
})
public class TbFornecedor implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "codigo")
    private Integer codigo;
    @Column(name = "nome")
    private String nome;
    @Column(name = "telefone")
    private String telefone;
    @Column(name = "email")
    private String email;
    @Column(name = "site")
    private String site;
    @Column(name = "endereco")
    private String endereco;
    @Column(name = "nif")
    private String nif;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkFornecedor")
    private List<NotasCompras> notasComprasList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkFornecedor")
    private List<Compras> comprasList;
    @JoinColumn(name = "fk_regime", referencedColumnName = "pk_regime")
    @ManyToOne
    private Regime fkRegime;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "codFornecedores")
    private List<TbProduto> tbProdutoList;

    public TbFornecedor()
    {
    }

    public TbFornecedor( Integer codigo )
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

    public String getTelefone()
    {
        return telefone;
    }

    public void setTelefone( String telefone )
    {
        this.telefone = telefone;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail( String email )
    {
        this.email = email;
    }

    public String getSite()
    {
        return site;
    }

    public void setSite( String site )
    {
        this.site = site;
    }

    public String getEndereco()
    {
        return endereco;
    }

    public void setEndereco( String endereco )
    {
        this.endereco = endereco;
    }

    public String getNif()
    {
        return nif;
    }

    public void setNif( String nif )
    {
        this.nif = nif;
    }

    @XmlTransient
    public List<NotasCompras> getNotasComprasList()
    {
        return notasComprasList;
    }

    public void setNotasComprasList( List<NotasCompras> notasComprasList )
    {
        this.notasComprasList = notasComprasList;
    }

    @XmlTransient
    public List<Compras> getComprasList()
    {
        return comprasList;
    }

    public void setComprasList( List<Compras> comprasList )
    {
        this.comprasList = comprasList;
    }

    public Regime getFkRegime()
    {
        return fkRegime;
    }

    public void setFkRegime( Regime fkRegime )
    {
        this.fkRegime = fkRegime;
    }

    @XmlTransient
    public List<TbProduto> getTbProdutoList()
    {
        return tbProdutoList;
    }

    public void setTbProdutoList( List<TbProduto> tbProdutoList )
    {
        this.tbProdutoList = tbProdutoList;
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
        if ( !( object instanceof TbFornecedor ) )
        {
            return false;
        }
        TbFornecedor other = ( TbFornecedor ) object;
        if ( ( this.codigo == null && other.codigo != null ) || ( this.codigo != null && !this.codigo.equals( other.codigo ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.TbFornecedor[ codigo=" + codigo + " ]";
    }
    
}
