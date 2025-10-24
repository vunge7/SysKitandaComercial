/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;


import java.sql.Connection;
import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author lenovo
 */
@Entity
@Table(name = "tb_usuario")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "TbUsuario.findAll", query = "SELECT t FROM TbUsuario t"),
    @NamedQuery(name = "TbUsuario.findByCodigo", query = "SELECT t FROM TbUsuario t WHERE t.codigo = :codigo"),
    @NamedQuery(name = "TbUsuario.findByNome", query = "SELECT t FROM TbUsuario t WHERE t.nome = :nome"),
    @NamedQuery(name = "TbUsuario.findBySenha", query = "SELECT t FROM TbUsuario t WHERE t.senha = :senha"),
    @NamedQuery(name = "TbUsuario.findByStatus", query = "SELECT t FROM TbUsuario t WHERE t.status = :status"),
    @NamedQuery(name = "TbUsuario.findByDataNascimento", query = "SELECT t FROM TbUsuario t WHERE t.dataNascimento = :dataNascimento"),
    @NamedQuery(name = "TbUsuario.findByTelefone", query = "SELECT t FROM TbUsuario t WHERE t.telefone = :telefone"),
    @NamedQuery(name = "TbUsuario.findByEmail", query = "SELECT t FROM TbUsuario t WHERE t.email = :email"),
    @NamedQuery(name = "TbUsuario.findByEndereco", query = "SELECT t FROM TbUsuario t WHERE t.endereco = :endereco"),
    @NamedQuery(name = "TbUsuario.findByUserName", query = "SELECT t FROM TbUsuario t WHERE t.userName = :userName"),
    @NamedQuery(name = "TbUsuario.findBySobreNome", query = "SELECT t FROM TbUsuario t WHERE t.sobreNome = :sobreNome"),
    @NamedQuery(name = "TbUsuario.findByDataUltimoAcesso", query = "SELECT t FROM TbUsuario t WHERE t.dataUltimoAcesso = :dataUltimoAcesso")
})
public class TbUsuario implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "codigo")
    private Integer codigo;
    @Column(name = "nome")
    private String nome;
    @Column(name = "senha")
    private String senha;
    @Column(name = "status")
    private String status;
    @Column(name = "dataNascimento")
    @Temporal(TemporalType.DATE)
    private Date dataNascimento;
    @Column(name = "telefone")
    private String telefone;
    @Column(name = "email")
    private String email;
    @Basic(optional = false)
    @Column(name = "endereco")
    private String endereco;
    @Column(name = "userName")
    private String userName;
    @Column(name = "sobreNome")
    private String sobreNome;
    @Column(name = "dataUltimoAcesso")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataUltimoAcesso;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkTbUsuario")
    private List<ItemSalarioExtra> itemSalarioExtraList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "codigoUsuario")
    private List<NotasCompras> notasComprasList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "codigoUsuario")
    private List<Notas> notasList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkUsuario")
    private List<Turno> turnoList;
    @OneToMany(mappedBy = "codigoUsuario")
    private List<Compras> comprasList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkUsuario")
    private List<PedidoFeria> pedidoFeriaList;
    @OneToMany(mappedBy = "fkUsuario")
    private List<TransferenciaBancaria> transferenciaBancariaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idUsuario")
    private List<TbEncomenda> tbEncomendaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkUsuario")
    private List<TbEstorno> tbEstornoList;
    @OneToMany(mappedBy = "fkUsuario")
    private List<LevantamentoBancario> levantamentoBancarioList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkUsuario")
    private List<PagamentoSubsidioFeriaNatal> pagamentoSubsidioFeriaNatalList;
    @OneToMany(mappedBy = "fkUsuario")
    private List<SaidasTesouraria> saidasTesourariaList;
    @OneToMany(mappedBy = "idUsuario")
    private List<TbAcerto> tbAcertoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkUsuario")
    private List<TbProForma> tbProFormaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkUsuario")
    private List<FechoPeriodo> fechoPeriodoList;
    @JoinColumn(name = "idStatus", referencedColumnName = "idStatus")
    @ManyToOne
    private TbStatus idStatus;
    @JoinColumn(name = "idTipoUsuario", referencedColumnName = "idTipoUsuario")
    @ManyToOne(optional = false)
    private TbTipoUsuario idTipoUsuario;
    @JoinColumn(name = "codigo_sexo", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private TbSexo codigoSexo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkUsuario")
    private List<Amortizacao> amortizacaoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkUsuario")
    private List<Anexos> anexosList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkUsuario")
    private List<FechoContrato> fechoContratoList;
    @OneToMany(mappedBy = "fkUsuario")
    private List<DepositoBancario> depositoBancarioList;
    @OneToMany(mappedBy = "fkUsuario")
    private List<TbFuncionario> tbFuncionarioList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkUsuario")
    private List<TbDesconto> tbDescontoList;
    @OneToMany(mappedBy = "fkUsuario")
    private List<TbOperacaoSistema> tbOperacaoSistemaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkUsuario")
    private List<TbSaidasProdutos> tbSaidasProdutosList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkUsuario")
    private List<TbPreco> tbPrecoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idUsuario")
    private List<TbEntrada> tbEntradaList;
    @OneToMany(mappedBy = "fkUsuario")
    private List<TbEntradaVasilhame> tbEntradaVasilhameList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "codigoUsuario")
    private List<TbVenda> tbVendaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkUsuario")
    private List<Promocao> promocaoList;
    @OneToMany(mappedBy = "fkUsuario")
    private List<EntradaTesouraria> entradaTesourariaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idUsuario")
    private List<TbItemPermissao> tbItemPermissaoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkUsuario")
    private List<AccessoArmazem> accessoArmazemList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkUsuario")
    private List<PrevioAviso> previoAvisoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkUsuario")
    private List<TbSaidaVasilhame> tbSaidaVasilhameList;
    @OneToMany(mappedBy = "fkUsuario")
    private List<AmortizacaoDivida> amortizacaoDividaList;

    public TbUsuario()
    {
    }

    public TbUsuario( Integer codigo )
    {
        this.codigo = codigo;
    }

    public TbUsuario( Integer codigo, String endereco )
    {
        this.codigo = codigo;
        this.endereco = endereco;
    }

    public Integer getCodigo()
    {
        return codigo;
    }

    public void setCodigo( Integer codigo )
    {
        this.codigo = codigo;
    }

    public String getNome()
    {
        return nome;
    }

    public void setNome( String nome )
    {
        this.nome = nome;
    }

    public String getSenha()
    {
        return senha;
    }

    public void setSenha( String senha )
    {
        this.senha = senha;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus( String status )
    {
        this.status = status;
    }

    public Date getDataNascimento()
    {
        return dataNascimento;
    }

    public void setDataNascimento( Date dataNascimento )
    {
        this.dataNascimento = dataNascimento;
    }

    public String getTelefone()
    {
        return telefone;
    }

    public void setTelefone( String telefone )
    {
        this.telefone = telefone;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail( String email )
    {
        this.email = email;
    }

    public String getEndereco()
    {
        return endereco;
    }

    public void setEndereco( String endereco )
    {
        this.endereco = endereco;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName( String userName )
    {
        this.userName = userName;
    }

    public String getSobreNome()
    {
        return sobreNome;
    }

    public void setSobreNome( String sobreNome )
    {
        this.sobreNome = sobreNome;
    }

    public Date getDataUltimoAcesso()
    {
        return dataUltimoAcesso;
    }

    public void setDataUltimoAcesso( Date dataUltimoAcesso )
    {
        this.dataUltimoAcesso = dataUltimoAcesso;
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
    public List<Turno> getTurnoList()
    {
        return turnoList;
    }

    public void setTurnoList( List<Turno> turnoList )
    {
        this.turnoList = turnoList;
    }

    @XmlTransient
    public List<Compras> getComprasList()
    {
        return comprasList;
    }

    public void setComprasList( List<Compras> comprasList )
    {
        this.comprasList = comprasList;
    }

    @XmlTransient
    public List<PedidoFeria> getPedidoFeriaList()
    {
        return pedidoFeriaList;
    }

    public void setPedidoFeriaList( List<PedidoFeria> pedidoFeriaList )
    {
        this.pedidoFeriaList = pedidoFeriaList;
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
    public List<TbEncomenda> getTbEncomendaList()
    {
        return tbEncomendaList;
    }

    public void setTbEncomendaList( List<TbEncomenda> tbEncomendaList )
    {
        this.tbEncomendaList = tbEncomendaList;
    }

    @XmlTransient
    public List<TbEstorno> getTbEstornoList()
    {
        return tbEstornoList;
    }

    public void setTbEstornoList( List<TbEstorno> tbEstornoList )
    {
        this.tbEstornoList = tbEstornoList;
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
    public List<PagamentoSubsidioFeriaNatal> getPagamentoSubsidioFeriaNatalList()
    {
        return pagamentoSubsidioFeriaNatalList;
    }

    public void setPagamentoSubsidioFeriaNatalList( List<PagamentoSubsidioFeriaNatal> pagamentoSubsidioFeriaNatalList )
    {
        this.pagamentoSubsidioFeriaNatalList = pagamentoSubsidioFeriaNatalList;
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
    public List<TbAcerto> getTbAcertoList()
    {
        return tbAcertoList;
    }

    public void setTbAcertoList( List<TbAcerto> tbAcertoList )
    {
        this.tbAcertoList = tbAcertoList;
    }

    @XmlTransient
    public List<TbProForma> getTbProFormaList()
    {
        return tbProFormaList;
    }

    public void setTbProFormaList( List<TbProForma> tbProFormaList )
    {
        this.tbProFormaList = tbProFormaList;
    }

    @XmlTransient
    public List<FechoPeriodo> getFechoPeriodoList()
    {
        return fechoPeriodoList;
    }

    public void setFechoPeriodoList( List<FechoPeriodo> fechoPeriodoList )
    {
        this.fechoPeriodoList = fechoPeriodoList;
    }

    public TbStatus getIdStatus()
    {
        return idStatus;
    }

    public void setIdStatus( TbStatus idStatus )
    {
        this.idStatus = idStatus;
    }

    public TbTipoUsuario getIdTipoUsuario()
    {
        return idTipoUsuario;
    }

    public void setIdTipoUsuario( TbTipoUsuario idTipoUsuario )
    {
        this.idTipoUsuario = idTipoUsuario;
    }

    public TbSexo getCodigoSexo()
    {
        return codigoSexo;
    }

    public void setCodigoSexo( TbSexo codigoSexo )
    {
        this.codigoSexo = codigoSexo;
    }

    @XmlTransient
    public List<Amortizacao> getAmortizacaoList()
    {
        return amortizacaoList;
    }

    public void setAmortizacaoList( List<Amortizacao> amortizacaoList )
    {
        this.amortizacaoList = amortizacaoList;
    }

    @XmlTransient
    public List<Anexos> getAnexosList()
    {
        return anexosList;
    }

    public void setAnexosList( List<Anexos> anexosList )
    {
        this.anexosList = anexosList;
    }

    @XmlTransient
    public List<FechoContrato> getFechoContratoList()
    {
        return fechoContratoList;
    }

    public void setFechoContratoList( List<FechoContrato> fechoContratoList )
    {
        this.fechoContratoList = fechoContratoList;
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
    public List<TbFuncionario> getTbFuncionarioList()
    {
        return tbFuncionarioList;
    }

    public void setTbFuncionarioList( List<TbFuncionario> tbFuncionarioList )
    {
        this.tbFuncionarioList = tbFuncionarioList;
    }

    @XmlTransient
    public List<TbDesconto> getTbDescontoList()
    {
        return tbDescontoList;
    }

    public void setTbDescontoList( List<TbDesconto> tbDescontoList )
    {
        this.tbDescontoList = tbDescontoList;
    }

    @XmlTransient
    public List<TbOperacaoSistema> getTbOperacaoSistemaList()
    {
        return tbOperacaoSistemaList;
    }

    public void setTbOperacaoSistemaList( List<TbOperacaoSistema> tbOperacaoSistemaList )
    {
        this.tbOperacaoSistemaList = tbOperacaoSistemaList;
    }

    @XmlTransient
    public List<TbSaidasProdutos> getTbSaidasProdutosList()
    {
        return tbSaidasProdutosList;
    }

    public void setTbSaidasProdutosList( List<TbSaidasProdutos> tbSaidasProdutosList )
    {
        this.tbSaidasProdutosList = tbSaidasProdutosList;
    }

    @XmlTransient
    public List<TbPreco> getTbPrecoList()
    {
        return tbPrecoList;
    }

    public void setTbPrecoList( List<TbPreco> tbPrecoList )
    {
        this.tbPrecoList = tbPrecoList;
    }

    @XmlTransient
    public List<TbEntrada> getTbEntradaList()
    {
        return tbEntradaList;
    }

    public void setTbEntradaList( List<TbEntrada> tbEntradaList )
    {
        this.tbEntradaList = tbEntradaList;
    }

    @XmlTransient
    public List<TbEntradaVasilhame> getTbEntradaVasilhameList()
    {
        return tbEntradaVasilhameList;
    }

    public void setTbEntradaVasilhameList( List<TbEntradaVasilhame> tbEntradaVasilhameList )
    {
        this.tbEntradaVasilhameList = tbEntradaVasilhameList;
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
    public List<Promocao> getPromocaoList()
    {
        return promocaoList;
    }

    public void setPromocaoList( List<Promocao> promocaoList )
    {
        this.promocaoList = promocaoList;
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

    @XmlTransient
    public List<TbItemPermissao> getTbItemPermissaoList()
    {
        return tbItemPermissaoList;
    }

    public void setTbItemPermissaoList( List<TbItemPermissao> tbItemPermissaoList )
    {
        this.tbItemPermissaoList = tbItemPermissaoList;
    }

    @XmlTransient
    public List<AccessoArmazem> getAccessoArmazemList()
    {
        return accessoArmazemList;
    }

    public void setAccessoArmazemList( List<AccessoArmazem> accessoArmazemList )
    {
        this.accessoArmazemList = accessoArmazemList;
    }

    @XmlTransient
    public List<PrevioAviso> getPrevioAvisoList()
    {
        return previoAvisoList;
    }

    public void setPrevioAvisoList( List<PrevioAviso> previoAvisoList )
    {
        this.previoAvisoList = previoAvisoList;
    }

    @XmlTransient
    public List<TbSaidaVasilhame> getTbSaidaVasilhameList()
    {
        return tbSaidaVasilhameList;
    }

    public void setTbSaidaVasilhameList( List<TbSaidaVasilhame> tbSaidaVasilhameList )
    {
        this.tbSaidaVasilhameList = tbSaidaVasilhameList;
    }

    @XmlTransient
    public List<AmortizacaoDivida> getAmortizacaoDividaList()
    {
        return amortizacaoDividaList;
    }

    public void setAmortizacaoDividaList( List<AmortizacaoDivida> amortizacaoDividaList )
    {
        this.amortizacaoDividaList = amortizacaoDividaList;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += ( codigo != null ? codigo.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if ( !( object instanceof TbUsuario ) )
        {
            return false;
        }
        TbUsuario other = ( TbUsuario ) object;
        if ( ( this.codigo == null && other.codigo != null ) || ( this.codigo != null && !this.codigo.equals( other.codigo ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.TbUsuario[ codigo=" + codigo + " ]";
    }
    
}
