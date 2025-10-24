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
@Table(name = "amortizacao")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "Amortizacao.findAll", query = "SELECT a FROM Amortizacao a"),
    @NamedQuery(name = "Amortizacao.findByPkAmortizacao", query = "SELECT a FROM Amortizacao a WHERE a.pkAmortizacao = :pkAmortizacao"),
    @NamedQuery(name = "Amortizacao.findByData", query = "SELECT a FROM Amortizacao a WHERE a.data = :data"),
    @NamedQuery(name = "Amortizacao.findByHora", query = "SELECT a FROM Amortizacao a WHERE a.hora = :hora"),
    @NamedQuery(name = "Amortizacao.findByValor", query = "SELECT a FROM Amortizacao a WHERE a.valor = :valor"),
    @NamedQuery(name = "Amortizacao.findByCodFact", query = "SELECT a FROM Amortizacao a WHERE a.codFact = :codFact"),
    @NamedQuery(name = "Amortizacao.findByRefCodFact", query = "SELECT a FROM Amortizacao a WHERE a.refCodFact = :refCodFact")
})
public class Amortizacao implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pk_amortizacao")
    private Integer pkAmortizacao;
    @Column(name = "data")
    @Temporal(TemporalType.DATE)
    private Date data;
    @Column(name = "hora")
    @Temporal(TemporalType.TIME)
    private Date hora;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor")
    private Double valor;
    @Column(name = "cod_fact")
    private String codFact;
    @Column(name = "ref_cod_fact")
    private String refCodFact;
    @JoinColumn(name = "fk_usuario", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private TbUsuario fkUsuario;
    @JoinColumn(name = "fk_venda", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private TbVenda fkVenda;

    public Amortizacao()
    {
    }

    public Amortizacao( Integer pkAmortizacao )
    {
        this.pkAmortizacao = pkAmortizacao;
    }

    public Integer getPkAmortizacao()
    {
        return pkAmortizacao;
    }

    public void setPkAmortizacao( Integer pkAmortizacao )
    {
        this.pkAmortizacao = pkAmortizacao;
    }

    public Date getData()
    {
        return data;
    }

    public void setData( Date data )
    {
        this.data = data;
    }

    public Date getHora()
    {
        return hora;
    }

    public void setHora( Date hora )
    {
        this.hora = hora;
    }

    public Double getValor()
    {
        return valor;
    }

    public void setValor( Double valor )
    {
        this.valor = valor;
    }

    public String getCodFact()
    {
        return codFact;
    }

    public void setCodFact( String codFact )
    {
        this.codFact = codFact;
    }

    public String getRefCodFact()
    {
        return refCodFact;
    }

    public void setRefCodFact( String refCodFact )
    {
        this.refCodFact = refCodFact;
    }

    public TbUsuario getFkUsuario()
    {
        return fkUsuario;
    }

    public void setFkUsuario( TbUsuario fkUsuario )
    {
        this.fkUsuario = fkUsuario;
    }

    public TbVenda getFkVenda()
    {
        return fkVenda;
    }

    public void setFkVenda( TbVenda fkVenda )
    {
        this.fkVenda = fkVenda;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += ( pkAmortizacao != null ? pkAmortizacao.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if ( !( object instanceof Amortizacao ) )
        {
            return false;
        }
        Amortizacao other = ( Amortizacao ) object;
        if ( ( this.pkAmortizacao == null && other.pkAmortizacao != null ) || ( this.pkAmortizacao != null && !this.pkAmortizacao.equals( other.pkAmortizacao ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.Amortizacao[ pkAmortizacao=" + pkAmortizacao + " ]";
    }
    
}
