/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;


import java.sql.Connection;
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

/**
 *
 * @author lenovo
 */
@Entity
@Table(name = "tb_profissao")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "TbProfissao.findAll", query = "SELECT t FROM TbProfissao t"),
    @NamedQuery(name = "TbProfissao.findByIdProfissao", query = "SELECT t FROM TbProfissao t WHERE t.idProfissao = :idProfissao"),
    @NamedQuery(name = "TbProfissao.findByDescricao", query = "SELECT t FROM TbProfissao t WHERE t.descricao = :descricao")
})
public class TbProfissao implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idProfissao")
    private Integer idProfissao;
    @Column(name = "descricao")
    private Integer descricao;

    public TbProfissao()
    {
    }

    public TbProfissao( Integer idProfissao )
    {
        this.idProfissao = idProfissao;
    }

    public Integer getIdProfissao()
    {
        return idProfissao;
    }

    public void setIdProfissao( Integer idProfissao )
    {
        this.idProfissao = idProfissao;
    }

    public Integer getDescricao()
    {
        return descricao;
    }

    public void setDescricao( Integer descricao )
    {
        this.descricao = descricao;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += ( idProfissao != null ? idProfissao.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if ( !( object instanceof TbProfissao ) )
        {
            return false;
        }
        TbProfissao other = ( TbProfissao ) object;
        if ( ( this.idProfissao == null && other.idProfissao != null ) || ( this.idProfissao != null && !this.idProfissao.equals( other.idProfissao ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.TbProfissao[ idProfissao=" + idProfissao + " ]";
    }
    
}
