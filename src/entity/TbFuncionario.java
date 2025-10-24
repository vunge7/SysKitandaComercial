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
import javax.persistence.Lob;
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
@Table(name = "tb_funcionario")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "TbFuncionario.findAll", query = "SELECT t FROM TbFuncionario t"),
    @NamedQuery(name = "TbFuncionario.findByIdFuncionario", query = "SELECT t FROM TbFuncionario t WHERE t.idFuncionario = :idFuncionario"),
    @NamedQuery(name = "TbFuncionario.findByNumeroFuncionario", query = "SELECT t FROM TbFuncionario t WHERE t.numeroFuncionario = :numeroFuncionario"),
    @NamedQuery(name = "TbFuncionario.findByNome", query = "SELECT t FROM TbFuncionario t WHERE t.nome = :nome"),
    @NamedQuery(name = "TbFuncionario.findByTelefone", query = "SELECT t FROM TbFuncionario t WHERE t.telefone = :telefone"),
    @NamedQuery(name = "TbFuncionario.findByMorada", query = "SELECT t FROM TbFuncionario t WHERE t.morada = :morada"),
    @NamedQuery(name = "TbFuncionario.findByUserName", query = "SELECT t FROM TbFuncionario t WHERE t.userName = :userName"),
    @NamedQuery(name = "TbFuncionario.findByPassword", query = "SELECT t FROM TbFuncionario t WHERE t.password = :password"),
    @NamedQuery(name = "TbFuncionario.findByHabilitacaoLiteraria", query = "SELECT t FROM TbFuncionario t WHERE t.habilitacaoLiteraria = :habilitacaoLiteraria"),
    @NamedQuery(name = "TbFuncionario.findByDiasInstituicao", query = "SELECT t FROM TbFuncionario t WHERE t.diasInstituicao = :diasInstituicao"),
    @NamedQuery(name = "TbFuncionario.findByNif", query = "SELECT t FROM TbFuncionario t WHERE t.nif = :nif"),
    @NamedQuery(name = "TbFuncionario.findByDataNascimento", query = "SELECT t FROM TbFuncionario t WHERE t.dataNascimento = :dataNascimento"),
    @NamedQuery(name = "TbFuncionario.findByDocID", query = "SELECT t FROM TbFuncionario t WHERE t.docID = :docID"),
    @NamedQuery(name = "TbFuncionario.findByNdocID", query = "SELECT t FROM TbFuncionario t WHERE t.ndocID = :ndocID"),
    @NamedQuery(name = "TbFuncionario.findByDataemissaodocID", query = "SELECT t FROM TbFuncionario t WHERE t.dataemissaodocID = :dataemissaodocID"),
    @NamedQuery(name = "TbFuncionario.findByDatavalidadedocID", query = "SELECT t FROM TbFuncionario t WHERE t.datavalidadedocID = :datavalidadedocID"),
    @NamedQuery(name = "TbFuncionario.findBySexo", query = "SELECT t FROM TbFuncionario t WHERE t.sexo = :sexo"),
    @NamedQuery(name = "TbFuncionario.findByNSegurancaSocial", query = "SELECT t FROM TbFuncionario t WHERE t.nSegurancaSocial = :nSegurancaSocial"),
    @NamedQuery(name = "TbFuncionario.findByDescontoSegurancaSocial", query = "SELECT t FROM TbFuncionario t WHERE t.descontoSegurancaSocial = :descontoSegurancaSocial"),
    @NamedQuery(name = "TbFuncionario.findByDataInicioContrato", query = "SELECT t FROM TbFuncionario t WHERE t.dataInicioContrato = :dataInicioContrato"),
    @NamedQuery(name = "TbFuncionario.findByDataFimContrato", query = "SELECT t FROM TbFuncionario t WHERE t.dataFimContrato = :dataFimContrato"),
    @NamedQuery(name = "TbFuncionario.findByTelefone1", query = "SELECT t FROM TbFuncionario t WHERE t.telefone1 = :telefone1"),
    @NamedQuery(name = "TbFuncionario.findByTipoContrato", query = "SELECT t FROM TbFuncionario t WHERE t.tipoContrato = :tipoContrato"),
    @NamedQuery(name = "TbFuncionario.findByDuracaoContrato", query = "SELECT t FROM TbFuncionario t WHERE t.duracaoContrato = :duracaoContrato"),
    @NamedQuery(name = "TbFuncionario.findByDataContrato", query = "SELECT t FROM TbFuncionario t WHERE t.dataContrato = :dataContrato"),
    @NamedQuery(name = "TbFuncionario.findByContaFechada", query = "SELECT t FROM TbFuncionario t WHERE t.contaFechada = :contaFechada"),
    @NamedQuery(name = "TbFuncionario.findByMotivoFecho", query = "SELECT t FROM TbFuncionario t WHERE t.motivoFecho = :motivoFecho"),
    @NamedQuery(name = "TbFuncionario.findByTelefone2", query = "SELECT t FROM TbFuncionario t WHERE t.telefone2 = :telefone2"),
    @NamedQuery(name = "TbFuncionario.findByFormaPagamento", query = "SELECT t FROM TbFuncionario t WHERE t.formaPagamento = :formaPagamento"),
    @NamedQuery(name = "TbFuncionario.findByActivo", query = "SELECT t FROM TbFuncionario t WHERE t.activo = :activo"),
    @NamedQuery(name = "TbFuncionario.findByDataValidadeBi", query = "SELECT t FROM TbFuncionario t WHERE t.dataValidadeBi = :dataValidadeBi"),
    @NamedQuery(name = "TbFuncionario.findByNbi", query = "SELECT t FROM TbFuncionario t WHERE t.nbi = :nbi"),
    @NamedQuery(name = "TbFuncionario.findByFkFormaPagamento", query = "SELECT t FROM TbFuncionario t WHERE t.fkFormaPagamento = :fkFormaPagamento")
})
public class TbFuncionario implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idFuncionario")
    private Integer idFuncionario;
    @Column(name = "numero_funcionario")
    private String numeroFuncionario;
    @Column(name = "nome")
    private String nome;
    @Column(name = "telefone")
    private String telefone;
    @Column(name = "morada")
    private String morada;
    @Column(name = "user_name")
    private String userName;
    @Column(name = "password")
    private String password;
    @Column(name = "habilitacao_literaria")
    private String habilitacaoLiteraria;
    @Column(name = "dias_instituicao")
    private String diasInstituicao;
    @Column(name = "nif")
    private String nif;
    @Column(name = "data_nascimento")
    @Temporal(TemporalType.DATE)
    private Date dataNascimento;
    @Column(name = "docID")
    private String docID;
    @Column(name = "ndocID")
    private String ndocID;
    @Column(name = "data_emissao_docID")
    @Temporal(TemporalType.DATE)
    private Date dataemissaodocID;
    @Column(name = "data_validade_docID")
    @Temporal(TemporalType.DATE)
    private Date datavalidadedocID;
    @Column(name = "sexo")
    private String sexo;
    @Column(name = "n_seguranca_social")
    private String nSegurancaSocial;
    @Column(name = "desconto_seguranca_social")
    private String descontoSegurancaSocial;
    @Column(name = "data_inicio_contrato")
    @Temporal(TemporalType.DATE)
    private Date dataInicioContrato;
    @Column(name = "data_fim_contrato")
    @Temporal(TemporalType.DATE)
    private Date dataFimContrato;
    @Column(name = "telefone_1")
    private String telefone1;
    @Column(name = "tipo_contrato")
    private String tipoContrato;
    @Column(name = "duracao_contrato")
    private String duracaoContrato;
    @Column(name = "data_contrato")
    @Temporal(TemporalType.DATE)
    private Date dataContrato;
    @Column(name = "conta_fechada")
    private Short contaFechada;
    @Column(name = "motivo_fecho")
    private String motivoFecho;
    @Lob
    @Column(name = "photo")
    private byte[] photo;
    @Column(name = "telefone_2")
    private String telefone2;
    @Column(name = "forma_pagamento")
    private String formaPagamento;
    @Column(name = "activo")
    private Integer activo;
    @Column(name = "data_validade_bi")
    @Temporal(TemporalType.DATE)
    private Date dataValidadeBi;
    @Column(name = "nbi")
    private String nbi;
    @Column(name = "fk_forma_pagamento")
    private Integer fkFormaPagamento;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkTbFuncionario")
    private List<ItemSalarioExtra> itemSalarioExtraList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkFuncionario")
    private List<PedidoFeria> pedidoFeriaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkFuncionario")
    private List<ReciboRh> reciboRhList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkFuncionario")
    private List<PagamentoSubsidioFeriaNatal> pagamentoSubsidioFeriaNatalList;
    @OneToMany(mappedBy = "idFuncionarioFK")
    private List<TbFalta> tbFaltaList;
    @OneToMany(mappedBy = "idFuncionarioFK")
    private List<TbSalario> tbSalarioList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkFuncionario")
    private List<Anexos> anexosList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkFuncionario")
    private List<FechoContrato> fechoContratoList;
    @OneToMany(mappedBy = "idFuncionarioFK")
    private List<TbItemSubsidiosFuncionario> tbItemSubsidiosFuncionarioList;
    @JoinColumn(name = "fk_departamento", referencedColumnName = "pk_departamento")
    @ManyToOne
    private TbDepartamento fkDepartamento;
    @JoinColumn(name = "fk_especialidade", referencedColumnName = "pk_especialidade")
    @ManyToOne
    private TbEspecialidade fkEspecialidade;
    @JoinColumn(name = "fk_estado_civil", referencedColumnName = "pk_estado_civil")
    @ManyToOne
    private TbEstadoCivil fkEstadoCivil;
    @JoinColumn(name = "fk_funcao", referencedColumnName = "pk_funcao")
    @ManyToOne
    private TbFuncao fkFuncao;
    @JoinColumn(name = "fk_grau_academico", referencedColumnName = "pk_grau_academico")
    @ManyToOne
    private TbGrauAcademico fkGrauAcademico;
    @JoinColumn(name = "idStatusFK", referencedColumnName = "idStatus")
    @ManyToOne
    private TbStatus idStatusFK;
    @JoinColumn(name = "fkUsuario", referencedColumnName = "codigo")
    @ManyToOne
    private TbUsuario fkUsuario;
    @JoinColumn(name = "fk_empresa", referencedColumnName = "pk_empresa")
    @ManyToOne(optional = false)
    private Empresa fkEmpresa;
    @JoinColumn(name = "fk_modalidade", referencedColumnName = "pk_modalidade")
    @ManyToOne(optional = false)
    private Modalidade fkModalidade;
    @OneToMany(mappedBy = "idFuncionarioFK")
    private List<TbAdiantamento> tbAdiantamentoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkFuncionario")
    private List<AgregadoFamiliar> agregadoFamiliarList;
    @OneToMany(mappedBy = "idFuncionarioFK")
    private List<TbConta> tbContaList;
    @OneToMany(mappedBy = "idFuncionarioFK")
    private List<TbTempo> tbTempoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkFuncionario")
    private List<PrevioAviso> previoAvisoList;

    public TbFuncionario()
    {
    }

    public TbFuncionario( Integer idFuncionario )
    {
        this.idFuncionario = idFuncionario;
    }

    public Integer getIdFuncionario()
    {
        return idFuncionario;
    }

    public void setIdFuncionario( Integer idFuncionario )
    {
        this.idFuncionario = idFuncionario;
    }

    public String getNumeroFuncionario()
    {
        return numeroFuncionario;
    }

    public void setNumeroFuncionario( String numeroFuncionario )
    {
        this.numeroFuncionario = numeroFuncionario;
    }

    public String getNome()
    {
        return nome;
    }

    public void setNome( String nome )
    {
        this.nome = nome;
    }

    public String getTelefone()
    {
        return telefone;
    }

    public void setTelefone( String telefone )
    {
        this.telefone = telefone;
    }

    public String getMorada()
    {
        return morada;
    }

    public void setMorada( String morada )
    {
        this.morada = morada;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName( String userName )
    {
        this.userName = userName;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword( String password )
    {
        this.password = password;
    }

    public String getHabilitacaoLiteraria()
    {
        return habilitacaoLiteraria;
    }

    public void setHabilitacaoLiteraria( String habilitacaoLiteraria )
    {
        this.habilitacaoLiteraria = habilitacaoLiteraria;
    }

    public String getDiasInstituicao()
    {
        return diasInstituicao;
    }

    public void setDiasInstituicao( String diasInstituicao )
    {
        this.diasInstituicao = diasInstituicao;
    }

    public String getNif()
    {
        return nif;
    }

    public void setNif( String nif )
    {
        this.nif = nif;
    }

    public Date getDataNascimento()
    {
        return dataNascimento;
    }

    public void setDataNascimento( Date dataNascimento )
    {
        this.dataNascimento = dataNascimento;
    }

    public String getDocID()
    {
        return docID;
    }

    public void setDocID( String docID )
    {
        this.docID = docID;
    }

    public String getNdocID()
    {
        return ndocID;
    }

    public void setNdocID( String ndocID )
    {
        this.ndocID = ndocID;
    }

    public Date getDataemissaodocID()
    {
        return dataemissaodocID;
    }

    public void setDataemissaodocID( Date dataemissaodocID )
    {
        this.dataemissaodocID = dataemissaodocID;
    }

    public Date getDatavalidadedocID()
    {
        return datavalidadedocID;
    }

    public void setDatavalidadedocID( Date datavalidadedocID )
    {
        this.datavalidadedocID = datavalidadedocID;
    }

    public String getSexo()
    {
        return sexo;
    }

    public void setSexo( String sexo )
    {
        this.sexo = sexo;
    }

    public String getNSegurancaSocial()
    {
        return nSegurancaSocial;
    }

    public void setNSegurancaSocial( String nSegurancaSocial )
    {
        this.nSegurancaSocial = nSegurancaSocial;
    }

    public String getDescontoSegurancaSocial()
    {
        return descontoSegurancaSocial;
    }

    public void setDescontoSegurancaSocial( String descontoSegurancaSocial )
    {
        this.descontoSegurancaSocial = descontoSegurancaSocial;
    }

    public Date getDataInicioContrato()
    {
        return dataInicioContrato;
    }

    public void setDataInicioContrato( Date dataInicioContrato )
    {
        this.dataInicioContrato = dataInicioContrato;
    }

    public Date getDataFimContrato()
    {
        return dataFimContrato;
    }

    public void setDataFimContrato( Date dataFimContrato )
    {
        this.dataFimContrato = dataFimContrato;
    }

    public String getTelefone1()
    {
        return telefone1;
    }

    public void setTelefone1( String telefone1 )
    {
        this.telefone1 = telefone1;
    }

    public String getTipoContrato()
    {
        return tipoContrato;
    }

    public void setTipoContrato( String tipoContrato )
    {
        this.tipoContrato = tipoContrato;
    }

    public String getDuracaoContrato()
    {
        return duracaoContrato;
    }

    public void setDuracaoContrato( String duracaoContrato )
    {
        this.duracaoContrato = duracaoContrato;
    }

    public Date getDataContrato()
    {
        return dataContrato;
    }

    public void setDataContrato( Date dataContrato )
    {
        this.dataContrato = dataContrato;
    }

    public Short getContaFechada()
    {
        return contaFechada;
    }

    public void setContaFechada( Short contaFechada )
    {
        this.contaFechada = contaFechada;
    }

    public String getMotivoFecho()
    {
        return motivoFecho;
    }

    public void setMotivoFecho( String motivoFecho )
    {
        this.motivoFecho = motivoFecho;
    }

    public byte[] getPhoto()
    {
        return photo;
    }

    public void setPhoto( byte[] photo )
    {
        this.photo = photo;
    }

    public String getTelefone2()
    {
        return telefone2;
    }

    public void setTelefone2( String telefone2 )
    {
        this.telefone2 = telefone2;
    }

    public String getFormaPagamento()
    {
        return formaPagamento;
    }

    public void setFormaPagamento( String formaPagamento )
    {
        this.formaPagamento = formaPagamento;
    }

    public Integer getActivo()
    {
        return activo;
    }

    public void setActivo( Integer activo )
    {
        this.activo = activo;
    }

    public Date getDataValidadeBi()
    {
        return dataValidadeBi;
    }

    public void setDataValidadeBi( Date dataValidadeBi )
    {
        this.dataValidadeBi = dataValidadeBi;
    }

    public String getNbi()
    {
        return nbi;
    }

    public void setNbi( String nbi )
    {
        this.nbi = nbi;
    }

    public Integer getFkFormaPagamento()
    {
        return fkFormaPagamento;
    }

    public void setFkFormaPagamento( Integer fkFormaPagamento )
    {
        this.fkFormaPagamento = fkFormaPagamento;
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
    public List<PedidoFeria> getPedidoFeriaList()
    {
        return pedidoFeriaList;
    }

    public void setPedidoFeriaList( List<PedidoFeria> pedidoFeriaList )
    {
        this.pedidoFeriaList = pedidoFeriaList;
    }

    @XmlTransient
    public List<ReciboRh> getReciboRhList()
    {
        return reciboRhList;
    }

    public void setReciboRhList( List<ReciboRh> reciboRhList )
    {
        this.reciboRhList = reciboRhList;
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
    public List<TbFalta> getTbFaltaList()
    {
        return tbFaltaList;
    }

    public void setTbFaltaList( List<TbFalta> tbFaltaList )
    {
        this.tbFaltaList = tbFaltaList;
    }

    @XmlTransient
    public List<TbSalario> getTbSalarioList()
    {
        return tbSalarioList;
    }

    public void setTbSalarioList( List<TbSalario> tbSalarioList )
    {
        this.tbSalarioList = tbSalarioList;
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
    public List<TbItemSubsidiosFuncionario> getTbItemSubsidiosFuncionarioList()
    {
        return tbItemSubsidiosFuncionarioList;
    }

    public void setTbItemSubsidiosFuncionarioList( List<TbItemSubsidiosFuncionario> tbItemSubsidiosFuncionarioList )
    {
        this.tbItemSubsidiosFuncionarioList = tbItemSubsidiosFuncionarioList;
    }

    public TbDepartamento getFkDepartamento()
    {
        return fkDepartamento;
    }

    public void setFkDepartamento( TbDepartamento fkDepartamento )
    {
        this.fkDepartamento = fkDepartamento;
    }

    public TbEspecialidade getFkEspecialidade()
    {
        return fkEspecialidade;
    }

    public void setFkEspecialidade( TbEspecialidade fkEspecialidade )
    {
        this.fkEspecialidade = fkEspecialidade;
    }

    public TbEstadoCivil getFkEstadoCivil()
    {
        return fkEstadoCivil;
    }

    public void setFkEstadoCivil( TbEstadoCivil fkEstadoCivil )
    {
        this.fkEstadoCivil = fkEstadoCivil;
    }

    public TbFuncao getFkFuncao()
    {
        return fkFuncao;
    }

    public void setFkFuncao( TbFuncao fkFuncao )
    {
        this.fkFuncao = fkFuncao;
    }

    public TbGrauAcademico getFkGrauAcademico()
    {
        return fkGrauAcademico;
    }

    public void setFkGrauAcademico( TbGrauAcademico fkGrauAcademico )
    {
        this.fkGrauAcademico = fkGrauAcademico;
    }

    public TbStatus getIdStatusFK()
    {
        return idStatusFK;
    }

    public void setIdStatusFK( TbStatus idStatusFK )
    {
        this.idStatusFK = idStatusFK;
    }

    public TbUsuario getFkUsuario()
    {
        return fkUsuario;
    }

    public void setFkUsuario( TbUsuario fkUsuario )
    {
        this.fkUsuario = fkUsuario;
    }

    public Empresa getFkEmpresa()
    {
        return fkEmpresa;
    }

    public void setFkEmpresa( Empresa fkEmpresa )
    {
        this.fkEmpresa = fkEmpresa;
    }

    public Modalidade getFkModalidade()
    {
        return fkModalidade;
    }

    public void setFkModalidade( Modalidade fkModalidade )
    {
        this.fkModalidade = fkModalidade;
    }

    @XmlTransient
    public List<TbAdiantamento> getTbAdiantamentoList()
    {
        return tbAdiantamentoList;
    }

    public void setTbAdiantamentoList( List<TbAdiantamento> tbAdiantamentoList )
    {
        this.tbAdiantamentoList = tbAdiantamentoList;
    }

    @XmlTransient
    public List<AgregadoFamiliar> getAgregadoFamiliarList()
    {
        return agregadoFamiliarList;
    }

    public void setAgregadoFamiliarList( List<AgregadoFamiliar> agregadoFamiliarList )
    {
        this.agregadoFamiliarList = agregadoFamiliarList;
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
    public List<TbTempo> getTbTempoList()
    {
        return tbTempoList;
    }

    public void setTbTempoList( List<TbTempo> tbTempoList )
    {
        this.tbTempoList = tbTempoList;
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

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += ( idFuncionario != null ? idFuncionario.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if ( !( object instanceof TbFuncionario ) )
        {
            return false;
        }
        TbFuncionario other = ( TbFuncionario ) object;
        if ( ( this.idFuncionario == null && other.idFuncionario != null ) || ( this.idFuncionario != null && !this.idFuncionario.equals( other.idFuncionario ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.TbFuncionario[ idFuncionario=" + idFuncionario + " ]";
    }
    
}
