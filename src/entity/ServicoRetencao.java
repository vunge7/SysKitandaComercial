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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author lenovo
 */
@Entity
@Table(name = "servico_retencao")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "ServicoRetencao.findAll", query = "SELECT s FROM ServicoRetencao s"),
    @NamedQuery(name = "ServicoRetencao.findByPkServicoRetencao", query = "SELECT s FROM ServicoRetencao s WHERE s.pkServicoRetencao = :pkServicoRetencao")
})
public class ServicoRetencao implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pk_servico_retencao")
    private Integer pkServicoRetencao;
    @JoinColumn(name = "fk_produto", referencedColumnName = "codigo")
    @ManyToOne
    private TbProduto fkProduto;
    @JoinColumn(name = "fk_retencao", referencedColumnName = "pk_retencao")
    @ManyToOne
    private Retencao fkRetencao;

    public ServicoRetencao()
    {
    }

    public ServicoRetencao( Integer pkServicoRetencao )
    {
        this.pkServicoRetencao = pkServicoRetencao;
    }

    public Integer getPkServicoRetencao()
    {
        return pkServicoRetencao;
    }

    public void setPkServicoRetencao( Integer pkServicoRetencao )
    {
        this.pkServicoRetencao = pkServicoRetencao;
    }

    public TbProduto getFkProduto()
    {
        return fkProduto;
    }

    public void setFkProduto( TbProduto fkProduto )
    {
        this.fkProduto = fkProduto;
    }

    public Retencao getFkRetencao()
    {
        return fkRetencao;
    }

    public void setFkRetencao( Retencao fkRetencao )
    {
        this.fkRetencao = fkRetencao;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += ( pkServicoRetencao != null ? pkServicoRetencao.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if ( !( object instanceof ServicoRetencao ) )
        {
            return false;
        }
        ServicoRetencao other = ( ServicoRetencao ) object;
        if ( ( this.pkServicoRetencao == null && other.pkServicoRetencao != null ) || ( this.pkServicoRetencao != null && !this.pkServicoRetencao.equals( other.pkServicoRetencao ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.ServicoRetencao[ pkServicoRetencao=" + pkServicoRetencao + " ]";
    }
    
}
