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
@Table(name = "anexos")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "Anexos.findAll", query = "SELECT a FROM Anexos a"),
    @NamedQuery(name = "Anexos.findByPkAnexos", query = "SELECT a FROM Anexos a WHERE a.pkAnexos = :pkAnexos"),
    @NamedQuery(name = "Anexos.findByDataHora", query = "SELECT a FROM Anexos a WHERE a.dataHora = :dataHora"),
    @NamedQuery(name = "Anexos.findByNomeFicheiro", query = "SELECT a FROM Anexos a WHERE a.nomeFicheiro = :nomeFicheiro"),
    @NamedQuery(name = "Anexos.findByDescricao", query = "SELECT a FROM Anexos a WHERE a.descricao = :descricao"),
    @NamedQuery(name = "Anexos.findByCaminho", query = "SELECT a FROM Anexos a WHERE a.caminho = :caminho"),
    @NamedQuery(name = "Anexos.findByCaminhoFicheiro", query = "SELECT a FROM Anexos a WHERE a.caminhoFicheiro = :caminhoFicheiro")
})
public class Anexos implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pk_anexos")
    private Integer pkAnexos;
    @Column(name = "data_hora")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataHora;
    @Column(name = "nome_ficheiro")
    private String nomeFicheiro;
    @Column(name = "descricao")
    private String descricao;
    @Column(name = "caminho")
    private String caminho;
    @Column(name = "caminho_ficheiro")
    private String caminhoFicheiro;
    @JoinColumn(name = "fk_funcionario", referencedColumnName = "idFuncionario")
    @ManyToOne(optional = false)
    private TbFuncionario fkFuncionario;
    @JoinColumn(name = "fk_usuario", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private TbUsuario fkUsuario;

    public Anexos()
    {
    }

    public Anexos( Integer pkAnexos )
    {
        this.pkAnexos = pkAnexos;
    }

    public Integer getPkAnexos()
    {
        return pkAnexos;
    }

    public void setPkAnexos( Integer pkAnexos )
    {
        this.pkAnexos = pkAnexos;
    }

    public Date getDataHora()
    {
        return dataHora;
    }

    public void setDataHora( Date dataHora )
    {
        this.dataHora = dataHora;
    }

    public String getNomeFicheiro()
    {
        return nomeFicheiro;
    }

    public void setNomeFicheiro( String nomeFicheiro )
    {
        this.nomeFicheiro = nomeFicheiro;
    }

    public String getDescricao()
    {
        return descricao;
    }

    public void setDescricao( String descricao )
    {
        this.descricao = descricao;
    }

    public String getCaminho()
    {
        return caminho;
    }

    public void setCaminho( String caminho )
    {
        this.caminho = caminho;
    }

    public String getCaminhoFicheiro()
    {
        return caminhoFicheiro;
    }

    public void setCaminhoFicheiro( String caminhoFicheiro )
    {
        this.caminhoFicheiro = caminhoFicheiro;
    }

    public TbFuncionario getFkFuncionario()
    {
        return fkFuncionario;
    }

    public void setFkFuncionario( TbFuncionario fkFuncionario )
    {
        this.fkFuncionario = fkFuncionario;
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
        hash += ( pkAnexos != null ? pkAnexos.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if ( !( object instanceof Anexos ) )
        {
            return false;
        }
        Anexos other = ( Anexos ) object;
        if ( ( this.pkAnexos == null && other.pkAnexos != null ) || ( this.pkAnexos != null && !this.pkAnexos.equals( other.pkAnexos ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.Anexos[ pkAnexos=" + pkAnexos + " ]";
    }
    
}
