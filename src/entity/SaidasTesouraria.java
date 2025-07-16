/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

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
@Table(name = "saidas_tesouraria")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "SaidasTesouraria.findAll", query = "SELECT s FROM SaidasTesouraria s"),
    @NamedQuery(name = "SaidasTesouraria.findByPkSaidasTesouraria", query = "SELECT s FROM SaidasTesouraria s WHERE s.pkSaidasTesouraria = :pkSaidasTesouraria"),
    @NamedQuery(name = "SaidasTesouraria.findByDescricao", query = "SELECT s FROM SaidasTesouraria s WHERE s.descricao = :descricao"),
    @NamedQuery(name = "SaidasTesouraria.findByValor", query = "SELECT s FROM SaidasTesouraria s WHERE s.valor = :valor"),
    @NamedQuery(name = "SaidasTesouraria.findByDataSaida", query = "SELECT s FROM SaidasTesouraria s WHERE s.dataSaida = :dataSaida"),
    @NamedQuery(name = "SaidasTesouraria.findByHoraSaida", query = "SELECT s FROM SaidasTesouraria s WHERE s.horaSaida = :horaSaida")
})
public class SaidasTesouraria implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pk_saidas_tesouraria")
    private Integer pkSaidasTesouraria;
    @Column(name = "descricao")
    private String descricao;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor")
    private Double valor;
    @Column(name = "data_saida")
    @Temporal(TemporalType.DATE)
    private Date dataSaida;
    @Column(name = "hora_saida")
    @Temporal(TemporalType.TIME)
    private Date horaSaida;
    @JoinColumn(name = "fk_banco", referencedColumnName = "idBanco")
    @ManyToOne
    private TbBanco fkBanco;
    @JoinColumn(name = "fk_usuario", referencedColumnName = "codigo")
    @ManyToOne
    private TbUsuario fkUsuario;

    public SaidasTesouraria()
    {
    }

    public SaidasTesouraria( Integer pkSaidasTesouraria )
    {
        this.pkSaidasTesouraria = pkSaidasTesouraria;
    }

    public Integer getPkSaidasTesouraria()
    {
        return pkSaidasTesouraria;
    }

    public void setPkSaidasTesouraria( Integer pkSaidasTesouraria )
    {
        this.pkSaidasTesouraria = pkSaidasTesouraria;
    }

    public String getDescricao()
    {
        return descricao;
    }

    public void setDescricao( String descricao )
    {
        this.descricao = descricao;
    }

    public Double getValor()
    {
        return valor;
    }

    public void setValor( Double valor )
    {
        this.valor = valor;
    }

    public Date getDataSaida()
    {
        return dataSaida;
    }

    public void setDataSaida( Date dataSaida )
    {
        this.dataSaida = dataSaida;
    }

    public Date getHoraSaida()
    {
        return horaSaida;
    }

    public void setHoraSaida( Date horaSaida )
    {
        this.horaSaida = horaSaida;
    }

    public TbBanco getFkBanco()
    {
        return fkBanco;
    }

    public void setFkBanco( TbBanco fkBanco )
    {
        this.fkBanco = fkBanco;
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
        hash += ( pkSaidasTesouraria != null ? pkSaidasTesouraria.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if ( !( object instanceof SaidasTesouraria ) )
        {
            return false;
        }
        SaidasTesouraria other = ( SaidasTesouraria ) object;
        if ( ( this.pkSaidasTesouraria == null && other.pkSaidasTesouraria != null ) || ( this.pkSaidasTesouraria != null && !this.pkSaidasTesouraria.equals( other.pkSaidasTesouraria ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.SaidasTesouraria[ pkSaidasTesouraria=" + pkSaidasTesouraria + " ]";
    }
    
}
