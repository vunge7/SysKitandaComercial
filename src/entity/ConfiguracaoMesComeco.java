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
@Table( name = "configuracao_mes_comeco" )
@XmlRootElement
@NamedQueries(
                {
            @NamedQuery( name = "ConfiguracaoMesComeco.findAll", query = "SELECT c FROM ConfiguracaoMesComeco c" ),
            @NamedQuery( name = "ConfiguracaoMesComeco.findById", query = "SELECT c FROM ConfiguracaoMesComeco c WHERE c.id = :id" ),
            @NamedQuery( name = "ConfiguracaoMesComeco.findByDataCadastro", query = "SELECT c FROM ConfiguracaoMesComeco c WHERE c.dataCadastro = :dataCadastro" ),
            @NamedQuery( name = "ConfiguracaoMesComeco.findByMesId", query = "SELECT c FROM ConfiguracaoMesComeco c WHERE c.mesId = :mesId" ),
            @NamedQuery( name = "ConfiguracaoMesComeco.findByProdutoId", query = "SELECT c FROM ConfiguracaoMesComeco c WHERE c.produtoId = :produtoId" ),
            @NamedQuery( name = "ConfiguracaoMesComeco.findByUsuarioId", query = "SELECT c FROM ConfiguracaoMesComeco c WHERE c.usuarioId = :usuarioId" )
        } )
public class ConfiguracaoMesComeco implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Basic( optional = false )
    @Column( name = "id" )
    private Integer id;
    @Basic( optional = false )
    @Column( name = "data_cadastro" )
    @Temporal( TemporalType.TIMESTAMP )
    private Date dataCadastro;
    @Basic( optional = false )
    @Column( name = "mes_id" )
    private int mesId;
    @Basic( optional = false )
    @Column( name = "produto_id" )
    private int produtoId;
    @Basic( optional = false )
    @Column( name = "usuario_id" )
    private int usuarioId;
    @Basic( optional = false )
    @Column( name = "cliente_id" )
    private int clienteId;
    @Basic( optional = false )
    @Column( name = "duracao" )
    private int duracao;

    public ConfiguracaoMesComeco()
    {
    }

    public ConfiguracaoMesComeco( Integer id )
    {
        this.id = id;
    }

    public ConfiguracaoMesComeco( Integer id, Date dataCadastro, short mesId, int produtoId, int usuarioId )
    {
        this.id = id;
        this.dataCadastro = dataCadastro;
        this.mesId = mesId;
        this.produtoId = produtoId;
        this.usuarioId = usuarioId;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId( Integer id )
    {
        this.id = id;
    }

    public Date getDataCadastro()
    {
        return dataCadastro;
    }

    public void setDataCadastro( Date dataCadastro )
    {
        this.dataCadastro = dataCadastro;
    }

    public int getMesId()
    {
        return mesId;
    }

    public void setMesId( int mesId )
    {
        this.mesId = mesId;
    }

    public int getProdutoId()
    {
        return produtoId;
    }

    public void setProdutoId( int produtoId )
    {
        this.produtoId = produtoId;
    }

    public int getUsuarioId()
    {
        return usuarioId;
    }

    public void setUsuarioId( int usuarioId )
    {
        this.usuarioId = usuarioId;
    }

    public int getDuracao()
    {
        return duracao;
    }

    public void setDuracao( int duracao )
    {
        this.duracao = duracao;
    }

    public int getClienteId()
    {
        return clienteId;
    }

    public void setClienteId( int clienteId )
    {
        this.clienteId = clienteId;
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
        if ( !( object instanceof ConfiguracaoMesComeco ) )
        {
            return false;
        }
        ConfiguracaoMesComeco other = (ConfiguracaoMesComeco) object;
        if ( ( this.id == null && other.id != null ) || ( this.id != null && !this.id.equals( other.id ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.ConfiguracaoMesComeco[ id=" + id + " ]";
    }

}
