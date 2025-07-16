/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "tb_banco")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "TbBanco.findAll", query = "SELECT t FROM TbBanco t"),
    @NamedQuery(name = "TbBanco.findByIdBanco", query = "SELECT t FROM TbBanco t WHERE t.idBanco = :idBanco"),
    @NamedQuery(name = "TbBanco.findByDescrisao", query = "SELECT t FROM TbBanco t WHERE t.descrisao = :descrisao"),
    @NamedQuery(name = "TbBanco.findByNumero", query = "SELECT t FROM TbBanco t WHERE t.numero = :numero"),
    @NamedQuery(name = "TbBanco.findBySaldoBanco", query = "SELECT t FROM TbBanco t WHERE t.saldoBanco = :saldoBanco")
})
public class TbBanco implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idBanco")
    private Integer idBanco;
    @Column(name = "descrisao")
    private String descrisao;
    @Column(name = "numero")
    private String numero;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "saldo_banco")
    private Double saldoBanco;
    @OneToMany(mappedBy = "idBanco")
    private List<NotasCompras> notasComprasList;
    @OneToMany(mappedBy = "idBanco")
    private List<Notas> notasList;
    @OneToMany(mappedBy = "fkBancoDestino")
    private List<TransferenciaBancaria> transferenciaBancariaList;
    @OneToMany(mappedBy = "fkBancoOrigem")
    private List<TransferenciaBancaria> transferenciaBancariaList1;
    @OneToMany(mappedBy = "fkBanco")
    private List<LevantamentoBancario> levantamentoBancarioList;
    @OneToMany(mappedBy = "fkBanco")
    private List<SaidasTesouraria> saidasTesourariaList;
    @OneToMany(mappedBy = "fkBanco")
    private List<DepositoBancario> depositoBancarioList;
    @OneToMany(mappedBy = "idBancoFK")
    private List<TbConta> tbContaList;
    @OneToMany(mappedBy = "idBanco")
    private List<TbVenda> tbVendaList;
    @OneToMany(mappedBy = "fkBanco")
    private List<EntradaTesouraria> entradaTesourariaList;

    public TbBanco()
    {
    }

    public TbBanco( Integer idBanco )
    {
        this.idBanco = idBanco;
    }

    public Integer getIdBanco()
    {
        return idBanco;
    }

    public void setIdBanco( Integer idBanco )
    {
        this.idBanco = idBanco;
    }

    public String getDescrisao()
    {
        return descrisao;
    }

    public void setDescrisao( String descrisao )
    {
        this.descrisao = descrisao;
    }

    public String getNumero()
    {
        return numero;
    }

    public void setNumero( String numero )
    {
        this.numero = numero;
    }

    public Double getSaldoBanco()
    {
        return saldoBanco;
    }

    public void setSaldoBanco( Double saldoBanco )
    {
        this.saldoBanco = saldoBanco;
    }

    @XmlTransient
    public List<NotasCompras> getNotasComprasList()
    {
        return notasComprasList;
    }

    public void setNotasComprasList( List<NotasCompras> notasComprasList )
    {
        this.notasComprasList = notasComprasList;
    }

    @XmlTransient
    public List<Notas> getNotasList()
    {
        return notasList;
    }

    public void setNotasList( List<Notas> notasList )
    {
        this.notasList = notasList;
    }

    @XmlTransient
    public List<TransferenciaBancaria> getTransferenciaBancariaList()
    {
        return transferenciaBancariaList;
    }

    public void setTransferenciaBancariaList( List<TransferenciaBancaria> transferenciaBancariaList )
    {
        this.transferenciaBancariaList = transferenciaBancariaList;
    }

    @XmlTransient
    public List<TransferenciaBancaria> getTransferenciaBancariaList1()
    {
        return transferenciaBancariaList1;
    }

    public void setTransferenciaBancariaList1( List<TransferenciaBancaria> transferenciaBancariaList1 )
    {
        this.transferenciaBancariaList1 = transferenciaBancariaList1;
    }

    @XmlTransient
    public List<LevantamentoBancario> getLevantamentoBancarioList()
    {
        return levantamentoBancarioList;
    }

    public void setLevantamentoBancarioList( List<LevantamentoBancario> levantamentoBancarioList )
    {
        this.levantamentoBancarioList = levantamentoBancarioList;
    }

    @XmlTransient
    public List<SaidasTesouraria> getSaidasTesourariaList()
    {
        return saidasTesourariaList;
    }

    public void setSaidasTesourariaList( List<SaidasTesouraria> saidasTesourariaList )
    {
        this.saidasTesourariaList = saidasTesourariaList;
    }

    @XmlTransient
    public List<DepositoBancario> getDepositoBancarioList()
    {
        return depositoBancarioList;
    }

    public void setDepositoBancarioList( List<DepositoBancario> depositoBancarioList )
    {
        this.depositoBancarioList = depositoBancarioList;
    }

    @XmlTransient
    public List<TbConta> getTbContaList()
    {
        return tbContaList;
    }

    public void setTbContaList( List<TbConta> tbContaList )
    {
        this.tbContaList = tbContaList;
    }

    @XmlTransient
    public List<TbVenda> getTbVendaList()
    {
        return tbVendaList;
    }

    public void setTbVendaList( List<TbVenda> tbVendaList )
    {
        this.tbVendaList = tbVendaList;
    }

    @XmlTransient
    public List<EntradaTesouraria> getEntradaTesourariaList()
    {
        return entradaTesourariaList;
    }

    public void setEntradaTesourariaList( List<EntradaTesouraria> entradaTesourariaList )
    {
        this.entradaTesourariaList = entradaTesourariaList;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += ( idBanco != null ? idBanco.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if ( !( object instanceof TbBanco ) )
        {
            return false;
        }
        TbBanco other = ( TbBanco ) object;
        if ( ( this.idBanco == null && other.idBanco != null ) || ( this.idBanco != null && !this.idBanco.equals( other.idBanco ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.TbBanco[ idBanco=" + idBanco + " ]";
    }
    
}
