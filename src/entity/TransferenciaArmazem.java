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
@Table(name = "transferencia_armazem")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "TransferenciaArmazem.findAll", query = "SELECT t FROM TransferenciaArmazem t"),
    @NamedQuery(name = "TransferenciaArmazem.findByPkTransferenciaArmazem", query = "SELECT t FROM TransferenciaArmazem t WHERE t.pkTransferenciaArmazem = :pkTransferenciaArmazem"),
    @NamedQuery(name = "TransferenciaArmazem.findByDataHora", query = "SELECT t FROM TransferenciaArmazem t WHERE t.dataHora = :dataHora"),
    @NamedQuery(name = "TransferenciaArmazem.findByFkUsuario", query = "SELECT t FROM TransferenciaArmazem t WHERE t.fkUsuario = :fkUsuario"),
    @NamedQuery(name = "TransferenciaArmazem.findByNomeUsuario", query = "SELECT t FROM TransferenciaArmazem t WHERE t.nomeUsuario = :nomeUsuario")
})
public class TransferenciaArmazem implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pk_transferencia_armazem")
    private Integer pkTransferenciaArmazem;
    @Column(name = "data_hora")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataHora;
    @Column(name = "fk_usuario")
    private Integer fkUsuario;
    @Column(name = "nome_usuario")
    private String nomeUsuario;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkTransferenciaArmazem")
    private List<LinhaTransferencia> linhaTransferenciaList;

    public TransferenciaArmazem()
    {
    }

    public TransferenciaArmazem( Integer pkTransferenciaArmazem )
    {
        this.pkTransferenciaArmazem = pkTransferenciaArmazem;
    }

    public Integer getPkTransferenciaArmazem()
    {
        return pkTransferenciaArmazem;
    }

    public void setPkTransferenciaArmazem( Integer pkTransferenciaArmazem )
    {
        this.pkTransferenciaArmazem = pkTransferenciaArmazem;
    }

    public Date getDataHora()
    {
        return dataHora;
    }

    public void setDataHora( Date dataHora )
    {
        this.dataHora = dataHora;
    }

    public Integer getFkUsuario()
    {
        return fkUsuario;
    }

    public void setFkUsuario( Integer fkUsuario )
    {
        this.fkUsuario = fkUsuario;
    }

    public String getNomeUsuario()
    {
        return nomeUsuario;
    }

    public void setNomeUsuario( String nomeUsuario )
    {
        this.nomeUsuario = nomeUsuario;
    }

    @XmlTransient
    public List<LinhaTransferencia> getLinhaTransferenciaList()
    {
        return linhaTransferenciaList;
    }

    public void setLinhaTransferenciaList( List<LinhaTransferencia> linhaTransferenciaList )
    {
        this.linhaTransferenciaList = linhaTransferenciaList;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += ( pkTransferenciaArmazem != null ? pkTransferenciaArmazem.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if ( !( object instanceof TransferenciaArmazem ) )
        {
            return false;
        }
        TransferenciaArmazem other = ( TransferenciaArmazem ) object;
        if ( ( this.pkTransferenciaArmazem == null && other.pkTransferenciaArmazem != null ) || ( this.pkTransferenciaArmazem != null && !this.pkTransferenciaArmazem.equals( other.pkTransferenciaArmazem ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.TransferenciaArmazem[ pkTransferenciaArmazem=" + pkTransferenciaArmazem + " ]";
    }
    
}
