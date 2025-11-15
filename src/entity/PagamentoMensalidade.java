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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Engº Domingos Dala Vunge
 * @created 4/nov/2025
 * @lastModified 4/nov/2025
 */
@Entity
@Table( name = "pagamento_mensalidade" )
@XmlRootElement
@NamedQueries( 
{
    @NamedQuery( name = "PagamentoMensalidade.findAll", query = "SELECT p FROM PagamentoMensalidade p" ),
    @NamedQuery( name = "PagamentoMensalidade.findById", query = "SELECT p FROM PagamentoMensalidade p WHERE p.id = :id" ),
    @NamedQuery( name = "PagamentoMensalidade.findByProdutoId", query = "SELECT p FROM PagamentoMensalidade p WHERE p.produtoId = :produtoId" ),
    @NamedQuery( name = "PagamentoMensalidade.findByClienteId", query = "SELECT p FROM PagamentoMensalidade p WHERE p.clienteId = :clienteId" ),
    @NamedQuery( name = "PagamentoMensalidade.findByMesId", query = "SELECT p FROM PagamentoMensalidade p WHERE p.mesId = :mesId" ),
    @NamedQuery( name = "PagamentoMensalidade.findByVendaId", query = "SELECT p FROM PagamentoMensalidade p WHERE p.vendaId = :vendaId" ),
    @NamedQuery( name = "PagamentoMensalidade.findByDataCadastro", query = "SELECT p FROM PagamentoMensalidade p WHERE p.dataCadastro = :dataCadastro" )
} )
public class PagamentoMensalidade implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Basic( optional = false )
    @Column( name = "id" )
    private Integer id;
    @Basic( optional = false )
    @Column( name = "produto_id" )
    private int produtoId;
    @Basic( optional = false )
    @Column( name = "cliente_id" )
    private int clienteId;
    @Basic( optional = false )
    @Column( name = "mes_id" )
    private int mesId;
    @Basic( optional = false )
    @Column( name = "venda_id" )
    private int vendaId;
    @Basic( optional = false )
    @Column( name = "data_cadastro" )
    @Temporal( TemporalType.TIMESTAMP )
    private Date dataCadastro;

    public PagamentoMensalidade()
    {
    }

    public PagamentoMensalidade( Integer id )
    {
        this.id = id;
    }

    public PagamentoMensalidade( Integer id, int produtoId, int clienteId, short mesId, int vendaId, Date dataCadastro )
    {
        this.id = id;
        this.produtoId = produtoId;
        this.clienteId = clienteId;
        this.mesId = mesId;
        this.vendaId = vendaId;
        this.dataCadastro = dataCadastro;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId( Integer id )
    {
        this.id = id;
    }

    public int getProdutoId()
    {
        return produtoId;
    }

    public void setProdutoId( int produtoId )
    {
        this.produtoId = produtoId;
    }

    public int getClienteId()
    {
        return clienteId;
    }

    public void setClienteId( int clienteId )
    {
        this.clienteId = clienteId;
    }

    public int getMesId()
    {
        return mesId;
    }

    public void setMesId( int mesId )
    {
        this.mesId = mesId;
    }

    public int getVendaId()
    {
        return vendaId;
    }

    public void setVendaId( int vendaId )
    {
        this.vendaId = vendaId;
    }

    public Date getDataCadastro()
    {
        return dataCadastro;
    }

    public void setDataCadastro( Date dataCadastro )
    {
        this.dataCadastro = dataCadastro;
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
        // TODO: Warning - this method won't work in the case the id fields are not set
        if ( !( object instanceof PagamentoMensalidade ) )
        {
            return false;
        }
        PagamentoMensalidade other = (PagamentoMensalidade) object;
        if ( ( this.id == null && other.id != null ) || ( this.id != null && !this.id.equals( other.id ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.PagamentoMensalidade[ id=" + id + " ]";
    }

}
