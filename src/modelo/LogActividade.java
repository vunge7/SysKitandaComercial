package modelo;

import java.util.Date;

/**
 * Registo da tabela log_actividade (auditoria das actividades do sistema).
 *
 * @see util.LogActividadeUtil
 */
public class LogActividade
{

    private long pkLogActividade;
    private Date dataHora;
    private Integer codUsuario;
    private String nomeUsuario;
    private String modulo;
    private String formulario;
    private String accao;
    private String entidade;
    private String idRegisto;
    private String descricao;
    private String valorAnterior;
    private String valorNovo;
    private String nomeMaquina;
    private String ipMaquina;

    public long getPkLogActividade()
    {
        return pkLogActividade;
    }

    public void setPkLogActividade( long pkLogActividade )
    {
        this.pkLogActividade = pkLogActividade;
    }

    public Date getDataHora()
    {
        return dataHora;
    }

    public void setDataHora( Date dataHora )
    {
        this.dataHora = dataHora;
    }

    public Integer getCodUsuario()
    {
        return codUsuario;
    }

    public void setCodUsuario( Integer codUsuario )
    {
        this.codUsuario = codUsuario;
    }

    public String getNomeUsuario()
    {
        return nomeUsuario;
    }

    public void setNomeUsuario( String nomeUsuario )
    {
        this.nomeUsuario = nomeUsuario;
    }

    public String getModulo()
    {
        return modulo;
    }

    public void setModulo( String modulo )
    {
        this.modulo = modulo;
    }

    public String getFormulario()
    {
        return formulario;
    }

    public void setFormulario( String formulario )
    {
        this.formulario = formulario;
    }

    public String getAccao()
    {
        return accao;
    }

    public void setAccao( String accao )
    {
        this.accao = accao;
    }

    public String getEntidade()
    {
        return entidade;
    }

    public void setEntidade( String entidade )
    {
        this.entidade = entidade;
    }

    public String getIdRegisto()
    {
        return idRegisto;
    }

    public void setIdRegisto( String idRegisto )
    {
        this.idRegisto = idRegisto;
    }

    public String getDescricao()
    {
        return descricao;
    }

    public void setDescricao( String descricao )
    {
        this.descricao = descricao;
    }

    public String getValorAnterior()
    {
        return valorAnterior;
    }

    public void setValorAnterior( String valorAnterior )
    {
        this.valorAnterior = valorAnterior;
    }

    public String getValorNovo()
    {
        return valorNovo;
    }

    public void setValorNovo( String valorNovo )
    {
        this.valorNovo = valorNovo;
    }

    public String getNomeMaquina()
    {
        return nomeMaquina;
    }

    public void setNomeMaquina( String nomeMaquina )
    {
        this.nomeMaquina = nomeMaquina;
    }

    public String getIpMaquina()
    {
        return ipMaquina;
    }

    public void setIpMaquina( String ipMaquina )
    {
        this.ipMaquina = ipMaquina;
    }

}
