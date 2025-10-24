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
 * @author Domingos Dala Vunge
 */
@Entity
@Table(name = "conta_permissoes")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "ContaPermissoes.findAll", query = "SELECT c FROM ContaPermissoes c"),
    @NamedQuery(name = "ContaPermissoes.findByPkContaPermissao", query = "SELECT c FROM ContaPermissoes c WHERE c.pkContaPermissao = :pkContaPermissao"),
    @NamedQuery(name = "ContaPermissoes.findByCodUsuario", query = "SELECT c FROM ContaPermissoes c WHERE c.codUsuario = :codUsuario"),
    @NamedQuery(name = "ContaPermissoes.findByCodConta", query = "SELECT c FROM ContaPermissoes c WHERE c.codConta = :codConta"),
    @NamedQuery(name = "ContaPermissoes.findByEntrada", query = "SELECT c FROM ContaPermissoes c WHERE c.entrada = :entrada"),
    @NamedQuery(name = "ContaPermissoes.findBySaida", query = "SELECT c FROM ContaPermissoes c WHERE c.saida = :saida"),
    @NamedQuery(name = "ContaPermissoes.findByTransferencia", query = "SELECT c FROM ContaPermissoes c WHERE c.transferencia = :transferencia"),
    @NamedQuery(name = "ContaPermissoes.findByVisEntrato", query = "SELECT c FROM ContaPermissoes c WHERE c.visEntrato = :visEntrato"),
    @NamedQuery(name = "ContaPermissoes.findByAnulacao", query = "SELECT c FROM ContaPermissoes c WHERE c.anulacao = :anulacao")
})
public class ContaPermissoes implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pk_conta_permissao")
    private Long pkContaPermissao;
    @Column(name = "cod_usuario")
    private Integer codUsuario;
    @Column(name = "cod_conta")
    private Integer codConta;
    @Column(name = "entrada")
    private Boolean entrada;
    @Column(name = "saida")
    private Boolean saida;
    @Column(name = "transferencia")
    private Boolean transferencia;
    @Column(name = "vis_entrato")
    private Boolean visEntrato;
    @Column(name = "anulacao")
    private Boolean anulacao;

    public ContaPermissoes()
    {
    }

    public ContaPermissoes( Long pkContaPermissao )
    {
        this.pkContaPermissao = pkContaPermissao;
    }

    public Long getPkContaPermissao()
    {
        return pkContaPermissao;
    }

    public void setPkContaPermissao( Long pkContaPermissao )
    {
        this.pkContaPermissao = pkContaPermissao;
    }

    public Integer getCodUsuario()
    {
        return codUsuario;
    }

    public void setCodUsuario( Integer codUsuario )
    {
        this.codUsuario = codUsuario;
    }

    public Integer getCodConta()
    {
        return codConta;
    }

    public void setCodConta( Integer codConta )
    {
        this.codConta = codConta;
    }

    public Boolean getEntrada()
    {
        return entrada;
    }

    public void setEntrada( Boolean entrada )
    {
        this.entrada = entrada;
    }

    public Boolean getSaida()
    {
        return saida;
    }

    public void setSaida( Boolean saida )
    {
        this.saida = saida;
    }

    public Boolean getTransferencia()
    {
        return transferencia;
    }

    public void setTransferencia( Boolean transferencia )
    {
        this.transferencia = transferencia;
    }

    public Boolean getVisEntrato()
    {
        return visEntrato;
    }

    public void setVisEntrato( Boolean visEntrato )
    {
        this.visEntrato = visEntrato;
    }

    public Boolean getAnulacao()
    {
        return anulacao;
    }

    public void setAnulacao( Boolean anulacao )
    {
        this.anulacao = anulacao;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += ( pkContaPermissao != null ? pkContaPermissao.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if ( !( object instanceof ContaPermissoes ) )
        {
            return false;
        }
        ContaPermissoes other = ( ContaPermissoes ) object;
        if ( ( this.pkContaPermissao == null && other.pkContaPermissao != null ) || ( this.pkContaPermissao != null && !this.pkContaPermissao.equals( other.pkContaPermissao ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.ContaPermissoes[ pkContaPermissao=" + pkContaPermissao + " ]";
    }
    
}
