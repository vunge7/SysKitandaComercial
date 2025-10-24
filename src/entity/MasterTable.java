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
@Table(name = "master_table")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "MasterTable.findAll", query = "SELECT m FROM MasterTable m"),
    @NamedQuery(name = "MasterTable.findByPkMasterTable", query = "SELECT m FROM MasterTable m WHERE m.pkMasterTable = :pkMasterTable"),
    @NamedQuery(name = "MasterTable.findByDesigncao", query = "SELECT m FROM MasterTable m WHERE m.designcao = :designcao")
})
public class MasterTable implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pk_master_table")
    private Integer pkMasterTable;
    @Column(name = "designcao")
    private String designcao;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkMasterTable")
    private List<ItemSalarioExtra> itemSalarioExtraList;
    @OneToMany(mappedBy = "fkMasterTable")
    private List<MasterTable> masterTableList;
    @JoinColumn(name = "fk_master_table", referencedColumnName = "pk_master_table")
    @ManyToOne
    private MasterTable fkMasterTable;

    public MasterTable()
    {
    }

    public MasterTable( Integer pkMasterTable )
    {
        this.pkMasterTable = pkMasterTable;
    }

    public Integer getPkMasterTable()
    {
        return pkMasterTable;
    }

    public void setPkMasterTable( Integer pkMasterTable )
    {
        this.pkMasterTable = pkMasterTable;
    }

    public String getDesigncao()
    {
        return designcao;
    }

    public void setDesigncao( String designcao )
    {
        this.designcao = designcao;
    }

    @XmlTransient
    public List<ItemSalarioExtra> getItemSalarioExtraList()
    {
        return itemSalarioExtraList;
    }

    public void setItemSalarioExtraList( List<ItemSalarioExtra> itemSalarioExtraList )
    {
        this.itemSalarioExtraList = itemSalarioExtraList;
    }

    @XmlTransient
    public List<MasterTable> getMasterTableList()
    {
        return masterTableList;
    }

    public void setMasterTableList( List<MasterTable> masterTableList )
    {
        this.masterTableList = masterTableList;
    }

    public MasterTable getFkMasterTable()
    {
        return fkMasterTable;
    }

    public void setFkMasterTable( MasterTable fkMasterTable )
    {
        this.fkMasterTable = fkMasterTable;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += ( pkMasterTable != null ? pkMasterTable.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if ( !( object instanceof MasterTable ) )
        {
            return false;
        }
        MasterTable other = ( MasterTable ) object;
        if ( ( this.pkMasterTable == null && other.pkMasterTable != null ) || ( this.pkMasterTable != null && !this.pkMasterTable.equals( other.pkMasterTable ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.MasterTable[ pkMasterTable=" + pkMasterTable + " ]";
    }
    
}
