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
@Table(name = "item_salario_extra")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "ItemSalarioExtra.findAll", query = "SELECT i FROM ItemSalarioExtra i"),
    @NamedQuery(name = "ItemSalarioExtra.findByPkItemSalarioExtra", query = "SELECT i FROM ItemSalarioExtra i WHERE i.pkItemSalarioExtra = :pkItemSalarioExtra"),
    @NamedQuery(name = "ItemSalarioExtra.findByValor", query = "SELECT i FROM ItemSalarioExtra i WHERE i.valor = :valor"),
    @NamedQuery(name = "ItemSalarioExtra.findByDataHora", query = "SELECT i FROM ItemSalarioExtra i WHERE i.dataHora = :dataHora")
})
public class ItemSalarioExtra implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pk_item_salario_extra")
    private Integer pkItemSalarioExtra;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor")
    private Double valor;
    @Column(name = "data_hora")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataHora;
    @JoinColumn(name = "fk_master_table", referencedColumnName = "pk_master_table")
    @ManyToOne(optional = false)
    private MasterTable fkMasterTable;
    @JoinColumn(name = "fk_tb_funcionario", referencedColumnName = "idFuncionario")
    @ManyToOne(optional = false)
    private TbFuncionario fkTbFuncionario;
    @JoinColumn(name = "fk_tb_usuario", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private TbUsuario fkTbUsuario;

    public ItemSalarioExtra()
    {
    }

    public ItemSalarioExtra( Integer pkItemSalarioExtra )
    {
        this.pkItemSalarioExtra = pkItemSalarioExtra;
    }

    public Integer getPkItemSalarioExtra()
    {
        return pkItemSalarioExtra;
    }

    public void setPkItemSalarioExtra( Integer pkItemSalarioExtra )
    {
        this.pkItemSalarioExtra = pkItemSalarioExtra;
    }

    public Double getValor()
    {
        return valor;
    }

    public void setValor( Double valor )
    {
        this.valor = valor;
    }

    public Date getDataHora()
    {
        return dataHora;
    }

    public void setDataHora( Date dataHora )
    {
        this.dataHora = dataHora;
    }

    public MasterTable getFkMasterTable()
    {
        return fkMasterTable;
    }

    public void setFkMasterTable( MasterTable fkMasterTable )
    {
        this.fkMasterTable = fkMasterTable;
    }

    public TbFuncionario getFkTbFuncionario()
    {
        return fkTbFuncionario;
    }

    public void setFkTbFuncionario( TbFuncionario fkTbFuncionario )
    {
        this.fkTbFuncionario = fkTbFuncionario;
    }

    public TbUsuario getFkTbUsuario()
    {
        return fkTbUsuario;
    }

    public void setFkTbUsuario( TbUsuario fkTbUsuario )
    {
        this.fkTbUsuario = fkTbUsuario;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += ( pkItemSalarioExtra != null ? pkItemSalarioExtra.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if ( !( object instanceof ItemSalarioExtra ) )
        {
            return false;
        }
        ItemSalarioExtra other = ( ItemSalarioExtra ) object;
        if ( ( this.pkItemSalarioExtra == null && other.pkItemSalarioExtra != null ) || ( this.pkItemSalarioExtra != null && !this.pkItemSalarioExtra.equals( other.pkItemSalarioExtra ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.ItemSalarioExtra[ pkItemSalarioExtra=" + pkItemSalarioExtra + " ]";
    }
    
}
