/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;


import java.sql.Connection;
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
@Table(name = "documento")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "Documento.findAll", query = "SELECT d FROM Documento d"),
    @NamedQuery(name = "Documento.findByPkDocumento", query = "SELECT d FROM Documento d WHERE d.pkDocumento = :pkDocumento"),
    @NamedQuery(name = "Documento.findByDesignacao", query = "SELECT d FROM Documento d WHERE d.designacao = :designacao"),
    @NamedQuery(name = "Documento.findByAbreviacao", query = "SELECT d FROM Documento d WHERE d.abreviacao = :abreviacao"),
    @NamedQuery(name = "Documento.findByCodUltimoDoc", query = "SELECT d FROM Documento d WHERE d.codUltimoDoc = :codUltimoDoc"),
    @NamedQuery(name = "Documento.findByDescricaoUltimoDoc", query = "SELECT d FROM Documento d WHERE d.descricaoUltimoDoc = :descricaoUltimoDoc"),
    @NamedQuery(name = "Documento.findByUltimaData", query = "SELECT d FROM Documento d WHERE d.ultimaData = :ultimaData")
})
public class Documento implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pk_documento")
    private Integer pkDocumento;
    @Column(name = "designacao")
    private String designacao;
    @Column(name = "abreviacao")
    private String abreviacao;
    @Column(name = "cod_ultimo_doc")
    private Integer codUltimoDoc;
    @Column(name = "descricao_ultimo_doc")
    private String descricaoUltimoDoc;
    @Column(name = "ultima_data")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ultimaData;
    @OneToMany(mappedBy = "fkDocumento")
    private List<NotasCompras> notasComprasList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkDocumento")
    private List<Notas> notasList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkDocumento")
    private List<Compras> comprasList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkDocumento")
    private List<TbVenda> tbVendaList;

    public Documento()
    {
    }

    public Documento( Integer pkDocumento )
    {
        this.pkDocumento = pkDocumento;
    }

    public Integer getPkDocumento()
    {
        return pkDocumento;
    }

    public void setPkDocumento( Integer pkDocumento )
    {
        this.pkDocumento = pkDocumento;
    }

    public String getDesignacao()
    {
        return designacao;
    }

    public void setDesignacao( String designacao )
    {
        this.designacao = designacao;
    }

    public String getAbreviacao()
    {
        return abreviacao;
    }

    public void setAbreviacao( String abreviacao )
    {
        this.abreviacao = abreviacao;
    }

    public Integer getCodUltimoDoc()
    {
        return codUltimoDoc;
    }

    public void setCodUltimoDoc( Integer codUltimoDoc )
    {
        this.codUltimoDoc = codUltimoDoc;
    }

    public String getDescricaoUltimoDoc()
    {
        return descricaoUltimoDoc;
    }

    public void setDescricaoUltimoDoc( String descricaoUltimoDoc )
    {
        this.descricaoUltimoDoc = descricaoUltimoDoc;
    }

    public Date getUltimaData()
    {
        return ultimaData;
    }

    public void setUltimaData( Date ultimaData )
    {
        this.ultimaData = ultimaData;
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
    public List<Notas> getNotasList()
    {
        return notasList;
    }

    public void setNotasList( List<Notas> notasList )
    {
        this.notasList = notasList;
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
        hash += ( pkDocumento != null ? pkDocumento.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if ( !( object instanceof Documento ) )
        {
            return false;
        }
        Documento other = ( Documento ) object;
        if ( ( this.pkDocumento == null && other.pkDocumento != null ) || ( this.pkDocumento != null && !this.pkDocumento.equals( other.pkDocumento ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.Documento[ pkDocumento=" + pkDocumento + " ]";
    }
    
}
