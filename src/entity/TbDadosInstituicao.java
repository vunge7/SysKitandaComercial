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
@Table( name = "tb_dados_instituicao" )
@XmlRootElement
@NamedQueries(
                {
            @NamedQuery( name = "TbDadosInstituicao.findAll", query = "SELECT t FROM TbDadosInstituicao t" ),
            @NamedQuery( name = "TbDadosInstituicao.findByIdDadosInsitiuicao", query = "SELECT t FROM TbDadosInstituicao t WHERE t.idDadosInsitiuicao = :idDadosInsitiuicao" ),
            @NamedQuery( name = "TbDadosInstituicao.findByNome", query = "SELECT t FROM TbDadosInstituicao t WHERE t.nome = :nome" ),
            @NamedQuery( name = "TbDadosInstituicao.findByTelefone", query = "SELECT t FROM TbDadosInstituicao t WHERE t.telefone = :telefone" ),
            @NamedQuery( name = "TbDadosInstituicao.findByEnderecos", query = "SELECT t FROM TbDadosInstituicao t WHERE t.enderecos = :enderecos" ),
            @NamedQuery( name = "TbDadosInstituicao.findByEmail", query = "SELECT t FROM TbDadosInstituicao t WHERE t.email = :email" ),
            @NamedQuery( name = "TbDadosInstituicao.findByNif", query = "SELECT t FROM TbDadosInstituicao t WHERE t.nif = :nif" ),
            @NamedQuery( name = "TbDadosInstituicao.findByCont", query = "SELECT t FROM TbDadosInstituicao t WHERE t.cont = :cont" ),
            @NamedQuery( name = "TbDadosInstituicao.findByContaBancaria1", query = "SELECT t FROM TbDadosInstituicao t WHERE t.contaBancaria1 = :contaBancaria1" ),
            @NamedQuery( name = "TbDadosInstituicao.findByContaBancaria2", query = "SELECT t FROM TbDadosInstituicao t WHERE t.contaBancaria2 = :contaBancaria2" ),
            @NamedQuery( name = "TbDadosInstituicao.findByContaBancaria3", query = "SELECT t FROM TbDadosInstituicao t WHERE t.contaBancaria3 = :contaBancaria3" ),
            @NamedQuery( name = "TbDadosInstituicao.findByContaBancaria4", query = "SELECT t FROM TbDadosInstituicao t WHERE t.contaBancaria4 = :contaBancaria4" ),
            @NamedQuery( name = "TbDadosInstituicao.findByContaBancaria5", query = "SELECT t FROM TbDadosInstituicao t WHERE t.contaBancaria5 = :contaBancaria5" ),
            @NamedQuery( name = "TbDadosInstituicao.findByContaBancaria6", query = "SELECT t FROM TbDadosInstituicao t WHERE t.contaBancaria6 = :contaBancaria6" ),
            @NamedQuery( name = "TbDadosInstituicao.findByDirectorGeral", query = "SELECT t FROM TbDadosInstituicao t WHERE t.directorGeral = :directorGeral" ),
            @NamedQuery( name = "TbDadosInstituicao.findByNumeroVias", query = "SELECT t FROM TbDadosInstituicao t WHERE t.numeroVias = :numeroVias" ),
            @NamedQuery( name = "TbDadosInstituicao.findByImpressora", query = "SELECT t FROM TbDadosInstituicao t WHERE t.impressora = :impressora" ),
            @NamedQuery( name = "TbDadosInstituicao.findByFoco", query = "SELECT t FROM TbDadosInstituicao t WHERE t.foco = :foco" ),
            @NamedQuery( name = "TbDadosInstituicao.findByDocpadrao", query = "SELECT t FROM TbDadosInstituicao t WHERE t.docpadrao = :docpadrao" ),
            @NamedQuery( name = "TbDadosInstituicao.findByDesactivarvias", query = "SELECT t FROM TbDadosInstituicao t WHERE t.desactivarvias = :desactivarvias" ),
            @NamedQuery( name = "TbDadosInstituicao.findByDescontoFinanceiro", query = "SELECT t FROM TbDadosInstituicao t WHERE t.descontoFinanceiro = :descontoFinanceiro" ),
            @NamedQuery( name = "TbDadosInstituicao.findByAnoEconomico", query = "SELECT t FROM TbDadosInstituicao t WHERE t.anoEconomico = :anoEconomico" ),
            @NamedQuery( name = "TbDadosInstituicao.findByVizualisarStock", query = "SELECT t FROM TbDadosInstituicao t WHERE t.vizualisarStock = :vizualisarStock" ),
            @NamedQuery( name = "TbDadosInstituicao.findByTranstorno", query = "SELECT t FROM TbDadosInstituicao t WHERE t.transtorno = :transtorno" ),
            @NamedQuery( name = "TbDadosInstituicao.findByCorreioCaixa", query = "SELECT t FROM TbDadosInstituicao t WHERE t.correioCaixa = :correioCaixa" ),
            @NamedQuery( name = "TbDadosInstituicao.findByNegocio", query = "SELECT t FROM TbDadosInstituicao t WHERE t.negocio = :negocio" ),
            @NamedQuery( name = "TbDadosInstituicao.findByObsFt", query = "SELECT t FROM TbDadosInstituicao t WHERE t.obsFt = :obsFt" ),
            @NamedQuery( name = "TbDadosInstituicao.findByPrazoFt", query = "SELECT t FROM TbDadosInstituicao t WHERE t.prazoFt = :prazoFt" ),
            @NamedQuery( name = "TbDadosInstituicao.findByLocalCarregamento", query = "SELECT t FROM TbDadosInstituicao t WHERE t.localCarregamento = :localCarregamento" ),
            @NamedQuery( name = "TbDadosInstituicao.findByContaBancaria7", query = "SELECT t FROM TbDadosInstituicao t WHERE t.contaBancaria7 = :contaBancaria7" ),
            @NamedQuery( name = "TbDadosInstituicao.findByContaBancaria8", query = "SELECT t FROM TbDadosInstituicao t WHERE t.contaBancaria8 = :contaBancaria8" ),
            @NamedQuery( name = "TbDadosInstituicao.findBySlogan", query = "SELECT t FROM TbDadosInstituicao t WHERE t.slogan = :slogan" ),
            @NamedQuery( name = "TbDadosInstituicao.findByObsDevolucao", query = "SELECT t FROM TbDadosInstituicao t WHERE t.obsDevolucao = :obsDevolucao" ),
            @NamedQuery( name = "TbDadosInstituicao.findByTeclado", query = "SELECT t FROM TbDadosInstituicao t WHERE t.teclado = :teclado" ),
            @NamedQuery( name = "TbDadosInstituicao.findByDataLicenca", query = "SELECT t FROM TbDadosInstituicao t WHERE t.dataLicenca = :dataLicenca" ),
            @NamedQuery( name = "TbDadosInstituicao.findByRegime", query = "SELECT t FROM TbDadosInstituicao t WHERE t.regime = :regime" ),
            @NamedQuery( name = "TbDadosInstituicao.findByRegimeContrato", query = "SELECT t FROM TbDadosInstituicao t WHERE t.regimeContrato = :regimeContrato" ),
            @NamedQuery( name = "TbDadosInstituicao.findByConfigArmazens", query = "SELECT t FROM TbDadosInstituicao t WHERE t.configArmazens = :configArmazens" ),
            @NamedQuery( name = "TbDadosInstituicao.findByUsarDoisPrecos", query = "SELECT t FROM TbDadosInstituicao t WHERE t.usarDoisPrecos = :usarDoisPrecos" ),
            @NamedQuery( name = "TbDadosInstituicao.findByImpressoraCozinha", query = "SELECT t FROM TbDadosInstituicao t WHERE t.impressoraCozinha = :impressoraCozinha" ),
            @NamedQuery( name = "TbDadosInstituicao.findByChaveMestre", query = "SELECT t FROM TbDadosInstituicao t WHERE t.chaveMestre = :chaveMestre" ),
            @NamedQuery( name = "TbDadosInstituicao.findByDataFecho", query = "SELECT t FROM TbDadosInstituicao t WHERE t.dataFecho = :dataFecho" ),
            @NamedQuery( name = "TbDadosInstituicao.findByTesouraria", query = "SELECT t FROM TbDadosInstituicao t WHERE t.tesouraria = :tesouraria" ),
            @NamedQuery( name = "TbDadosInstituicao.findByRh", query = "SELECT t FROM TbDadosInstituicao t WHERE t.rh = :rh" ),
            @NamedQuery( name = "TbDadosInstituicao.findByComercial", query = "SELECT t FROM TbDadosInstituicao t WHERE t.comercial = :comercial" ),
            @NamedQuery( name = "TbDadosInstituicao.findByJanelaServico", query = "SELECT t FROM TbDadosInstituicao t WHERE t.janelaServico = :janelaServico" )
        } )
public class TbDadosInstituicao implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Basic( optional = false )
    @Column( name = "idDadosInsitiuicao" )
    private Integer idDadosInsitiuicao;
    @Basic( optional = false )
    @Column( name = "nome" )
    private String nome;
    @Basic( optional = false )
    @Column( name = "telefone" )
    private String telefone;
    @Basic( optional = false )
    @Column( name = "enderecos" )
    private String enderecos;
    @Basic( optional = false )
    @Column( name = "email" )
    private String email;
    @Basic( optional = false )
    @Column( name = "nif" )
    private String nif;
    @Column( name = "cont" )
    private Integer cont;
    @Column( name = "conta_bancaria1" )
    private String contaBancaria1;
    @Column( name = "conta_bancaria2" )
    private String contaBancaria2;
    @Column( name = "conta_bancaria3" )
    private String contaBancaria3;
    @Column( name = "conta_bancaria4" )
    private String contaBancaria4;
    @Column( name = "conta_bancaria5" )
    private String contaBancaria5;
    @Column( name = "conta_bancaria6" )
    private String contaBancaria6;
    @Column( name = "director_geral" )
    private String directorGeral;
    @Column( name = "numero_vias" )
    private Integer numeroVias;
    @Column( name = "impressora" )
    private String impressora;
    @Column( name = "foco" )
    private String foco;
    @Column( name = "docpadrao" )
    private String docpadrao;
    @Column( name = "desactivarvias" )
    private String desactivarvias;
    @Column( name = "desconto_financeiro" )
    private String descontoFinanceiro;
    @Column( name = "ano_economico" )
    private String anoEconomico;
    @Column( name = "vizualisar_stock" )
    private String vizualisarStock;
    @Column( name = "transtorno" )
    private String transtorno;
    @Column( name = "correio_caixa" )
    private String correioCaixa;
    @Column( name = "negocio" )
    private String negocio;
    @Column( name = "obs_ft" )
    private String obsFt;
    @Column( name = "prazo_ft" )
    private String prazoFt;
    @Column( name = "local_carregamento" )
    private String localCarregamento;
    @Column( name = "conta_bancaria7" )
    private String contaBancaria7;
    @Column( name = "conta_bancaria8" )
    private String contaBancaria8;
    @Column( name = "slogan" )
    private String slogan;
    @Column( name = "obs_devolucao" )
    private String obsDevolucao;
    @Column( name = "teclado" )
    private String teclado;
    @Column( name = "data_licenca" )
    @Temporal( TemporalType.DATE )
    private Date dataLicenca;
    @Column( name = "regime" )
    private String regime;
    @Column( name = "regime_contrato" )
    private String regimeContrato;
    @Column( name = "config_armazens" )
    private String configArmazens;
    @Column( name = "usar_dois_precos" )
    private String usarDoisPrecos;
    @Column( name = "impressora_cozinha" )
    private String impressoraCozinha;
    @Column( name = "chave_mestre" )
    private String chaveMestre;
    @Column( name = "data_fecho" )
    @Temporal( TemporalType.DATE )
    private Date dataFecho;
    @Column( name = "tesouraria" )
    private String tesouraria;
    @Column( name = "rh" )
    private String rh;
    @Column( name = "comercial" )
    private String comercial;
    @Column( name = "janela_servico" )
    private String janelaServico;
    @Column( name = "impressora_sala" )
    private String impressoraSala;
    @Column( name = "prazo_proforma" )
    private Integer prazoProforma;
    @Column( name = "desactivar_lugares" )
    private String desactivarLugares;
    @Column( name = "tipo_fecho_caixa" )
    private String tipoFechoCaixa;
    @Column( name = "enviar_email" )
    private String enviarEmail;
    @Column( name = "tipo_ficha_tecnica" )
    private String tipoFichaTecnica;
    @Basic( optional = false )
    @Column( name = "hora_comeco_venda" )
    @Temporal( TemporalType.TIME )
    private Date horaComecoVenda;
    @Column( name = "hora_termino_venda" )
    @Temporal( TemporalType.TIME )
    private Date horaTerminoVenda;
//    @Column(name = "editar_preco")
//    private boolean editarPreco;

    public TbDadosInstituicao()
    {
    }

    public TbDadosInstituicao( Integer idDadosInsitiuicao )
    {
        this.idDadosInsitiuicao = idDadosInsitiuicao;
    }

    public TbDadosInstituicao( Integer idDadosInsitiuicao, String nome, String telefone, String enderecos, String email, String nif )
    {
        this.idDadosInsitiuicao = idDadosInsitiuicao;
        this.nome = nome;
        this.telefone = telefone;
        this.enderecos = enderecos;
        this.email = email;
        this.nif = nif;
    }

    public String getEnviarEmail()
    {
        return enviarEmail;
    }

    public void setEnviarEmail( String enviarEmail )
    {
        this.enviarEmail = enviarEmail;
    }

    public String getDesactivarLugares()
    {
        return desactivarLugares;
    }

    public void setDesactivarLugares( String desactivarLugares )
    {
        this.desactivarLugares = desactivarLugares;
    }

    public Integer getPrazoProforma()
    {
        return prazoProforma;
    }

    public void setPrazoProforma( Integer prazoProforma )
    {
        this.prazoProforma = prazoProforma;
    }

    public Integer getIdDadosInsitiuicao()
    {
        return idDadosInsitiuicao;
    }

    public void setIdDadosInsitiuicao( Integer idDadosInsitiuicao )
    {
        this.idDadosInsitiuicao = idDadosInsitiuicao;
    }

    public String getNome()
    {
        return nome;
    }

//    public boolean getEditarPreco()
//    {
//        return editarPreco;
//    }
//
//    public void setEditarPreco( boolean editarPreco )
//    {
//        this.editarPreco = editarPreco;
//    }
    public void setNome( String nome )
    {
        this.nome = nome;
    }

    public String getImpressoraSala()
    {
        return impressoraSala;
    }

    public void setImpressoraSala( String impressoraSala )
    {
        this.impressoraSala = impressoraSala;
    }

    public String getTelefone()
    {
        return telefone;
    }

    public void setTelefone( String telefone )
    {
        this.telefone = telefone;
    }

    public String getEnderecos()
    {
        return enderecos;
    }

    public void setEnderecos( String enderecos )
    {
        this.enderecos = enderecos;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail( String email )
    {
        this.email = email;
    }

    public String getNif()
    {
        return nif;
    }

    public void setNif( String nif )
    {
        this.nif = nif;
    }

    public Integer getCont()
    {
        return cont;
    }

    public void setCont( Integer cont )
    {
        this.cont = cont;
    }

    public String getContaBancaria1()
    {
        return contaBancaria1;
    }

    public void setContaBancaria1( String contaBancaria1 )
    {
        this.contaBancaria1 = contaBancaria1;
    }

    public String getContaBancaria2()
    {
        return contaBancaria2;
    }

    public void setContaBancaria2( String contaBancaria2 )
    {
        this.contaBancaria2 = contaBancaria2;
    }

    public String getContaBancaria3()
    {
        return contaBancaria3;
    }

    public void setContaBancaria3( String contaBancaria3 )
    {
        this.contaBancaria3 = contaBancaria3;
    }

    public String getContaBancaria4()
    {
        return contaBancaria4;
    }

    public void setContaBancaria4( String contaBancaria4 )
    {
        this.contaBancaria4 = contaBancaria4;
    }

    public String getContaBancaria5()
    {
        return contaBancaria5;
    }

    public void setContaBancaria5( String contaBancaria5 )
    {
        this.contaBancaria5 = contaBancaria5;
    }

    public String getContaBancaria6()
    {
        return contaBancaria6;
    }

    public void setContaBancaria6( String contaBancaria6 )
    {
        this.contaBancaria6 = contaBancaria6;
    }

    public String getDirectorGeral()
    {
        return directorGeral;
    }

    public void setDirectorGeral( String directorGeral )
    {
        this.directorGeral = directorGeral;
    }

    public Integer getNumeroVias()
    {
        return numeroVias;
    }

    public void setNumeroVias( Integer numeroVias )
    {
        this.numeroVias = numeroVias;
    }

    public String getImpressora()
    {
        return impressora;
    }

    public void setImpressora( String impressora )
    {
        this.impressora = impressora;
    }

    public String getFoco()
    {
        return foco;
    }

    public void setFoco( String foco )
    {
        this.foco = foco;
    }

    public String getDocpadrao()
    {
        return docpadrao;
    }

    public void setDocpadrao( String docpadrao )
    {
        this.docpadrao = docpadrao;
    }

    public String getDesactivarvias()
    {
        return desactivarvias;
    }

    public void setDesactivarvias( String desactivarvias )
    {
        this.desactivarvias = desactivarvias;
    }

    public String getDescontoFinanceiro()
    {
        return descontoFinanceiro;
    }

    public void setDescontoFinanceiro( String descontoFinanceiro )
    {
        this.descontoFinanceiro = descontoFinanceiro;
    }

    public String getAnoEconomico()
    {
        return anoEconomico;
    }

    public void setAnoEconomico( String anoEconomico )
    {
        this.anoEconomico = anoEconomico;
    }

    public String getVizualisarStock()
    {
        return vizualisarStock;
    }

    public void setVizualisarStock( String vizualisarStock )
    {
        this.vizualisarStock = vizualisarStock;
    }

    public String getTranstorno()
    {
        return transtorno;
    }

    public void setTranstorno( String transtorno )
    {
        this.transtorno = transtorno;
    }

    public String getCorreioCaixa()
    {
        return correioCaixa;
    }

    public void setCorreioCaixa( String correioCaixa )
    {
        this.correioCaixa = correioCaixa;
    }

    public String getNegocio()
    {
        return negocio;
    }

    public void setNegocio( String negocio )
    {
        this.negocio = negocio;
    }

    public String getObsFt()
    {
        return obsFt;
    }

    public void setObsFt( String obsFt )
    {
        this.obsFt = obsFt;
    }

    public String getPrazoFt()
    {
        return prazoFt;
    }

    public void setPrazoFt( String prazoFt )
    {
        this.prazoFt = prazoFt;
    }

    public String getLocalCarregamento()
    {
        return localCarregamento;
    }

    public void setLocalCarregamento( String localCarregamento )
    {
        this.localCarregamento = localCarregamento;
    }

    public String getContaBancaria7()
    {
        return contaBancaria7;
    }

    public void setContaBancaria7( String contaBancaria7 )
    {
        this.contaBancaria7 = contaBancaria7;
    }

    public String getContaBancaria8()
    {
        return contaBancaria8;
    }

    public void setContaBancaria8( String contaBancaria8 )
    {
        this.contaBancaria8 = contaBancaria8;
    }

    public String getSlogan()
    {
        return slogan;
    }

    public void setSlogan( String slogan )
    {
        this.slogan = slogan;
    }

    public String getObsDevolucao()
    {
        return obsDevolucao;
    }

    public void setObsDevolucao( String obsDevolucao )
    {
        this.obsDevolucao = obsDevolucao;
    }

    public String getTeclado()
    {
        return teclado;
    }

    public void setTeclado( String teclado )
    {
        this.teclado = teclado;
    }

    public Date getDataLicenca()
    {
        return dataLicenca;
    }

    public void setDataLicenca( Date dataLicenca )
    {
        this.dataLicenca = dataLicenca;
    }

    public String getRegime()
    {
        return regime;
    }

    public void setRegime( String regime )
    {
        this.regime = regime;
    }

    public String getRegimeContrato()
    {
        return regimeContrato;
    }

    public void setRegimeContrato( String regimeContrato )
    {
        this.regimeContrato = regimeContrato;
    }

    public String getConfigArmazens()
    {
        return configArmazens;
    }

    public void setConfigArmazens( String configArmazens )
    {
        this.configArmazens = configArmazens;
    }

    public String getUsarDoisPrecos()
    {
        return usarDoisPrecos;
    }

    public void setUsarDoisPrecos( String usarDoisPrecos )
    {
        this.usarDoisPrecos = usarDoisPrecos;
    }

    public String getImpressoraCozinha()
    {
        return impressoraCozinha;
    }

    public void setImpressoraCozinha( String impressoraCozinha )
    {
        this.impressoraCozinha = impressoraCozinha;
    }

    public String getChaveMestre()
    {
        return chaveMestre;
    }

    public void setChaveMestre( String chaveMestre )
    {
        this.chaveMestre = chaveMestre;
    }

    public Date getDataFecho()
    {
        return dataFecho;
    }

    public void setDataFecho( Date dataFecho )
    {
        this.dataFecho = dataFecho;
    }

    public String getTesouraria()
    {
        return tesouraria;
    }

    public void setTesouraria( String tesouraria )
    {
        this.tesouraria = tesouraria;
    }

    public String getRh()
    {
        return rh;
    }

    public void setRh( String rh )
    {
        this.rh = rh;
    }

    public String getComercial()
    {
        return comercial;
    }

    public void setComercial( String comercial )
    {
        this.comercial = comercial;
    }

    public String getJanelaServico()
    {
        return janelaServico;
    }

    public void setJanelaServico( String janelaServico )
    {
        this.janelaServico = janelaServico;
    }

    public Date getHoraComecoVenda()
    {
        return horaComecoVenda;
    }

    public void setHoraComecoVenda( Date horaComecoVenda )
    {
        this.horaComecoVenda = horaComecoVenda;
    }

    public Date getHoraTerminoVenda()
    {
        return horaTerminoVenda;
    }

    public void setHoraTerminoVenda( Date horaTerminoVenda )
    {
        this.horaTerminoVenda = horaTerminoVenda;
    }

    public String getTipoFechoCaixa()
    {
        return tipoFechoCaixa;
    }

    public void setTipoFechoCaixa( String tipoFechoCaixa )
    {
        this.tipoFechoCaixa = tipoFechoCaixa;
    }

    public String getTipoFichaTecnica()
    {
        return tipoFichaTecnica;
    }

    public void setTipoFichaTecnica( String tipoFichaTecnica )
    {
        this.tipoFichaTecnica = tipoFichaTecnica;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += ( idDadosInsitiuicao != null ? idDadosInsitiuicao.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if ( !( object instanceof TbDadosInstituicao ) )
        {
            return false;
        }
        TbDadosInstituicao other = (TbDadosInstituicao) object;
        if ( ( this.idDadosInsitiuicao == null && other.idDadosInsitiuicao != null ) || ( this.idDadosInsitiuicao != null && !this.idDadosInsitiuicao.equals( other.idDadosInsitiuicao ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.TbDadosInstituicao[ idDadosInsitiuicao=" + idDadosInsitiuicao + " ]";
    }

}
