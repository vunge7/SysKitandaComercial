/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

/**
 *
 * @author Engº Domingos Dala Vunge
 * @created 14/jan/2026
 * @lastModified 14/jan/2026
 */
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table( name = "series" )
@XmlRootElement
@NamedQueries(
                {
            @NamedQuery( name = "Series.findAll", query = "SELECT s FROM Series s" ),
            @NamedQuery( name = "Series.findById", query = "SELECT s FROM Series s WHERE s.id = :id" ),
            @NamedQuery( name = "Series.findByDesignacao", query = "SELECT s FROM Series s WHERE s.designacao = :designacao" )
        } )
public class Series implements Serializable
{

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Basic( optional = false )
    @Column( name = "id" )
    private Integer id;

    @Column( name = "designacao" )
    private String designacao;

    @Column( name = "fk_documento" )
    private int fkDocumento;

    @Column( name = "fk_ano_economico" )
    private int fkAnoEconomico;

    public Series()
    {
    }

    public Series( Integer id )
    {
        this.id = id;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId( Integer id )
    {
        this.id = id;
    }

    public String getDesignacao()
    {
        return designacao;
    }

    public void setDesignacao( String designacao )
    {
        this.designacao = designacao;
    }

    public int getFkDocumento()
    {
        return fkDocumento;
    }

    public void setFkDocumento( int fkDocumento )
    {
        this.fkDocumento = fkDocumento;
    }

    public int getFkAnoEconomico()
    {
        return fkAnoEconomico;
    }

    public void setFkAnoEconomico( int fkAnoEconomico )
    {
        this.fkAnoEconomico = fkAnoEconomico;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += ( id != null ? id.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        if ( !( object instanceof Series ) )
        {
            return false;
        }
        Series other = ( Series ) object;
        if ( ( this.id == null && other.id != null )
                || ( this.id != null && !this.id.equals( other.id ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.Series[ id=" + id + " ]";
    }
}
