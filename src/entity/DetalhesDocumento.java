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
 * @author Domingos Dala Vunge
 */
@Entity
@Table(name = "detalhes_documento")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "DetalhesDocumento.findAll", query = "SELECT d FROM DetalhesDocumento d"),
    @NamedQuery(name = "DetalhesDocumento.findByPkDetalhesDocumento", query = "SELECT d FROM DetalhesDocumento d WHERE d.pkDetalhesDocumento = :pkDetalhesDocumento"),
    @NamedQuery(name = "DetalhesDocumento.findByArCondicionado", query = "SELECT d FROM DetalhesDocumento d WHERE d.arCondicionado = :arCondicionado"),
    @NamedQuery(name = "DetalhesDocumento.findByBateria", query = "SELECT d FROM DetalhesDocumento d WHERE d.bateria = :bateria"),
    @NamedQuery(name = "DetalhesDocumento.findByCamara", query = "SELECT d FROM DetalhesDocumento d WHERE d.camara = :camara"),
    @NamedQuery(name = "DetalhesDocumento.findByCanicos", query = "SELECT d FROM DetalhesDocumento d WHERE d.canicos = :canicos"),
    @NamedQuery(name = "DetalhesDocumento.findByChaveRoda", query = "SELECT d FROM DetalhesDocumento d WHERE d.chaveRoda = :chaveRoda"),
    @NamedQuery(name = "DetalhesDocumento.findByCintoSeguranca", query = "SELECT d FROM DetalhesDocumento d WHERE d.cintoSeguranca = :cintoSeguranca"),
    @NamedQuery(name = "DetalhesDocumento.findByColete", query = "SELECT d FROM DetalhesDocumento d WHERE d.colete = :colete"),
    @NamedQuery(name = "DetalhesDocumento.findByElevador", query = "SELECT d FROM DetalhesDocumento d WHERE d.elevador = :elevador"),
    @NamedQuery(name = "DetalhesDocumento.findByExtintor", query = "SELECT d FROM DetalhesDocumento d WHERE d.extintor = :extintor"),
    @NamedQuery(name = "DetalhesDocumento.findByFarois", query = "SELECT d FROM DetalhesDocumento d WHERE d.farois = :farois"),
    @NamedQuery(name = "DetalhesDocumento.findByGuincho", query = "SELECT d FROM DetalhesDocumento d WHERE d.guincho = :guincho"),
    @NamedQuery(name = "DetalhesDocumento.findByIsqueiro", query = "SELECT d FROM DetalhesDocumento d WHERE d.isqueiro = :isqueiro"),
    @NamedQuery(name = "DetalhesDocumento.findByLimpaParabrisa", query = "SELECT d FROM DetalhesDocumento d WHERE d.limpaParabrisa = :limpaParabrisa"),
    @NamedQuery(name = "DetalhesDocumento.findByLivrete", query = "SELECT d FROM DetalhesDocumento d WHERE d.livrete = :livrete"),
    @NamedQuery(name = "DetalhesDocumento.findByTitulo", query = "SELECT d FROM DetalhesDocumento d WHERE d.titulo = :titulo"),
    @NamedQuery(name = "DetalhesDocumento.findByLongos", query = "SELECT d FROM DetalhesDocumento d WHERE d.longos = :longos"),
    @NamedQuery(name = "DetalhesDocumento.findByMacaco", query = "SELECT d FROM DetalhesDocumento d WHERE d.macaco = :macaco"),
    @NamedQuery(name = "DetalhesDocumento.findByManipuloExt", query = "SELECT d FROM DetalhesDocumento d WHERE d.manipuloExt = :manipuloExt"),
    @NamedQuery(name = "DetalhesDocumento.findByManipuloInt", query = "SELECT d FROM DetalhesDocumento d WHERE d.manipuloInt = :manipuloInt"),
    @NamedQuery(name = "DetalhesDocumento.findByPainel", query = "SELECT d FROM DetalhesDocumento d WHERE d.painel = :painel"),
    @NamedQuery(name = "DetalhesDocumento.findByPiscas", query = "SELECT d FROM DetalhesDocumento d WHERE d.piscas = :piscas"),
    @NamedQuery(name = "DetalhesDocumento.findByPorcasJantes", query = "SELECT d FROM DetalhesDocumento d WHERE d.porcasJantes = :porcasJantes"),
    @NamedQuery(name = "DetalhesDocumento.findByPresenca", query = "SELECT d FROM DetalhesDocumento d WHERE d.presenca = :presenca"),
    @NamedQuery(name = "DetalhesDocumento.findByRadio", query = "SELECT d FROM DetalhesDocumento d WHERE d.radio = :radio"),
    @NamedQuery(name = "DetalhesDocumento.findByReflectores", query = "SELECT d FROM DetalhesDocumento d WHERE d.reflectores = :reflectores"),
    @NamedQuery(name = "DetalhesDocumento.findByRetrovisores", query = "SELECT d FROM DetalhesDocumento d WHERE d.retrovisores = :retrovisores"),
    @NamedQuery(name = "DetalhesDocumento.findBySensor", query = "SELECT d FROM DetalhesDocumento d WHERE d.sensor = :sensor"),
    @NamedQuery(name = "DetalhesDocumento.findBySobresalente", query = "SELECT d FROM DetalhesDocumento d WHERE d.sobresalente = :sobresalente"),
    @NamedQuery(name = "DetalhesDocumento.findByStop", query = "SELECT d FROM DetalhesDocumento d WHERE d.stop = :stop"),
    @NamedQuery(name = "DetalhesDocumento.findByTampasJantes", query = "SELECT d FROM DetalhesDocumento d WHERE d.tampasJantes = :tampasJantes"),
    @NamedQuery(name = "DetalhesDocumento.findByTampoes", query = "SELECT d FROM DetalhesDocumento d WHERE d.tampoes = :tampoes"),
    @NamedQuery(name = "DetalhesDocumento.findByTapetes", query = "SELECT d FROM DetalhesDocumento d WHERE d.tapetes = :tapetes"),
    @NamedQuery(name = "DetalhesDocumento.findByTejadilho", query = "SELECT d FROM DetalhesDocumento d WHERE d.tejadilho = :tejadilho"),
    @NamedQuery(name = "DetalhesDocumento.findByTriangulo", query = "SELECT d FROM DetalhesDocumento d WHERE d.triangulo = :triangulo"),
    @NamedQuery(name = "DetalhesDocumento.findByVidros", query = "SELECT d FROM DetalhesDocumento d WHERE d.vidros = :vidros"),
    @NamedQuery(name = "DetalhesDocumento.findByCodVenda", query = "SELECT d FROM DetalhesDocumento d WHERE d.codVenda = :codVenda"),
    @NamedQuery(name = "DetalhesDocumento.findByDataEntrada", query = "SELECT d FROM DetalhesDocumento d WHERE d.dataEntrada = :dataEntrada"),
    @NamedQuery(name = "DetalhesDocumento.findByNotas", query = "SELECT d FROM DetalhesDocumento d WHERE d.notas = :notas")
})
public class DetalhesDocumento implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pk_detalhes_documento")
    private Integer pkDetalhesDocumento;
    @Column(name = "ar_condicionado")
    private String arCondicionado;
    @Column(name = "bateria")
    private String bateria;
    @Column(name = "camara")
    private String camara;
    @Column(name = "canicos")
    private String canicos;
    @Column(name = "chave_roda")
    private String chaveRoda;
    @Column(name = "cinto_seguranca")
    private String cintoSeguranca;
    @Column(name = "colete")
    private String colete;
    @Column(name = "elevador")
    private String elevador;
    @Column(name = "extintor")
    private String extintor;
    @Column(name = "farois")
    private String farois;
    @Column(name = "guincho")
    private String guincho;
    @Column(name = "isqueiro")
    private String isqueiro;
    @Column(name = "limpa_parabrisa")
    private String limpaParabrisa;
    @Column(name = "livrete")
    private String livrete;
    @Column(name = "titulo")
    private String titulo;
    @Column(name = "longos")
    private String longos;
    @Column(name = "macaco")
    private String macaco;
    @Column(name = "manipulo_ext")
    private String manipuloExt;
    @Column(name = "manipulo_int")
    private String manipuloInt;
    @Column(name = "painel")
    private String painel;
    @Column(name = "piscas")
    private String piscas;
    @Column(name = "porcas_jantes")
    private String porcasJantes;
    @Column(name = "presenca")
    private String presenca;
    @Column(name = "radio")
    private String radio;
    @Column(name = "reflectores")
    private String reflectores;
    @Column(name = "retrovisores")
    private String retrovisores;
    @Column(name = "sensor")
    private String sensor;
    @Column(name = "sobresalente")
    private String sobresalente;
    @Column(name = "stop")
    private String stop;
    @Column(name = "tampas_jantes")
    private String tampasJantes;
    @Column(name = "tampoes")
    private String tampoes;
    @Column(name = "tapetes")
    private String tapetes;
    @Column(name = "tejadilho")
    private String tejadilho;
    @Column(name = "triangulo")
    private String triangulo;
    @Column(name = "vidros")
    private String vidros;
    @Column(name = "cod_venda")
    private Integer codVenda;
    @Column(name = "data_entrada")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataEntrada;
    @Column(name = "notas")
    private String notas;

    public DetalhesDocumento()
    {
    }

    public DetalhesDocumento( Integer pkDetalhesDocumento )
    {
        this.pkDetalhesDocumento = pkDetalhesDocumento;
    }

    public Integer getPkDetalhesDocumento()
    {
        return pkDetalhesDocumento;
    }

    public void setPkDetalhesDocumento( Integer pkDetalhesDocumento )
    {
        this.pkDetalhesDocumento = pkDetalhesDocumento;
    }

    public String getArCondicionado()
    {
        return arCondicionado;
    }

    public void setArCondicionado( String arCondicionado )
    {
        this.arCondicionado = arCondicionado;
    }

    public String getBateria()
    {
        return bateria;
    }

    public void setBateria( String bateria )
    {
        this.bateria = bateria;
    }

    public String getCamara()
    {
        return camara;
    }

    public void setCamara( String camara )
    {
        this.camara = camara;
    }

    public String getCanicos()
    {
        return canicos;
    }

    public void setCanicos( String canicos )
    {
        this.canicos = canicos;
    }

    public String getChaveRoda()
    {
        return chaveRoda;
    }

    public void setChaveRoda( String chaveRoda )
    {
        this.chaveRoda = chaveRoda;
    }

    public String getCintoSeguranca()
    {
        return cintoSeguranca;
    }

    public void setCintoSeguranca( String cintoSeguranca )
    {
        this.cintoSeguranca = cintoSeguranca;
    }

    public String getColete()
    {
        return colete;
    }

    public void setColete( String colete )
    {
        this.colete = colete;
    }

    public String getElevador()
    {
        return elevador;
    }

    public void setElevador( String elevador )
    {
        this.elevador = elevador;
    }

    public String getExtintor()
    {
        return extintor;
    }

    public void setExtintor( String extintor )
    {
        this.extintor = extintor;
    }

    public String getFarois()
    {
        return farois;
    }

    public void setFarois( String farois )
    {
        this.farois = farois;
    }

    public String getGuincho()
    {
        return guincho;
    }

    public void setGuincho( String guincho )
    {
        this.guincho = guincho;
    }

    public String getIsqueiro()
    {
        return isqueiro;
    }

    public void setIsqueiro( String isqueiro )
    {
        this.isqueiro = isqueiro;
    }

    public String getLimpaParabrisa()
    {
        return limpaParabrisa;
    }

    public void setLimpaParabrisa( String limpaParabrisa )
    {
        this.limpaParabrisa = limpaParabrisa;
    }

    public String getLivrete()
    {
        return livrete;
    }

    public void setLivrete( String livrete )
    {
        this.livrete = livrete;
    }

    public String getTitulo()
    {
        return titulo;
    }

    public void setTitulo( String titulo )
    {
        this.titulo = titulo;
    }

    public String getLongos()
    {
        return longos;
    }

    public void setLongos( String longos )
    {
        this.longos = longos;
    }

    public String getMacaco()
    {
        return macaco;
    }

    public void setMacaco( String macaco )
    {
        this.macaco = macaco;
    }

    public String getManipuloExt()
    {
        return manipuloExt;
    }

    public void setManipuloExt( String manipuloExt )
    {
        this.manipuloExt = manipuloExt;
    }

    public String getManipuloInt()
    {
        return manipuloInt;
    }

    public void setManipuloInt( String manipuloInt )
    {
        this.manipuloInt = manipuloInt;
    }

    public String getPainel()
    {
        return painel;
    }

    public void setPainel( String painel )
    {
        this.painel = painel;
    }

    public String getPiscas()
    {
        return piscas;
    }

    public void setPiscas( String piscas )
    {
        this.piscas = piscas;
    }

    public String getPorcasJantes()
    {
        return porcasJantes;
    }

    public void setPorcasJantes( String porcasJantes )
    {
        this.porcasJantes = porcasJantes;
    }

    public String getPresenca()
    {
        return presenca;
    }

    public void setPresenca( String presenca )
    {
        this.presenca = presenca;
    }

    public String getRadio()
    {
        return radio;
    }

    public void setRadio( String radio )
    {
        this.radio = radio;
    }

    public String getReflectores()
    {
        return reflectores;
    }

    public void setReflectores( String reflectores )
    {
        this.reflectores = reflectores;
    }

    public String getRetrovisores()
    {
        return retrovisores;
    }

    public void setRetrovisores( String retrovisores )
    {
        this.retrovisores = retrovisores;
    }

    public String getSensor()
    {
        return sensor;
    }

    public void setSensor( String sensor )
    {
        this.sensor = sensor;
    }

    public String getSobresalente()
    {
        return sobresalente;
    }

    public void setSobresalente( String sobresalente )
    {
        this.sobresalente = sobresalente;
    }

    public String getStop()
    {
        return stop;
    }

    public void setStop( String stop )
    {
        this.stop = stop;
    }

    public String getTampasJantes()
    {
        return tampasJantes;
    }

    public void setTampasJantes( String tampasJantes )
    {
        this.tampasJantes = tampasJantes;
    }

    public String getTampoes()
    {
        return tampoes;
    }

    public void setTampoes( String tampoes )
    {
        this.tampoes = tampoes;
    }

    public String getTapetes()
    {
        return tapetes;
    }

    public void setTapetes( String tapetes )
    {
        this.tapetes = tapetes;
    }

    public String getTejadilho()
    {
        return tejadilho;
    }

    public void setTejadilho( String tejadilho )
    {
        this.tejadilho = tejadilho;
    }

    public String getTriangulo()
    {
        return triangulo;
    }

    public void setTriangulo( String triangulo )
    {
        this.triangulo = triangulo;
    }

    public String getVidros()
    {
        return vidros;
    }

    public void setVidros( String vidros )
    {
        this.vidros = vidros;
    }

    public Integer getCodVenda()
    {
        return codVenda;
    }

    public void setCodVenda( Integer codVenda )
    {
        this.codVenda = codVenda;
    }

    public Date getDataEntrada()
    {
        return dataEntrada;
    }

    public void setDataEntrada( Date dataEntrada )
    {
        this.dataEntrada = dataEntrada;
    }

    public String getNotas()
    {
        return notas;
    }

    public void setNotas( String notas )
    {
        this.notas = notas;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += ( pkDetalhesDocumento != null ? pkDetalhesDocumento.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals( Object object )
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if ( !( object instanceof DetalhesDocumento ) )
        {
            return false;
        }
        DetalhesDocumento other = ( DetalhesDocumento ) object;
        if ( ( this.pkDetalhesDocumento == null && other.pkDetalhesDocumento != null ) || ( this.pkDetalhesDocumento != null && !this.pkDetalhesDocumento.equals( other.pkDetalhesDocumento ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.DetalhesDocumento[ pkDetalhesDocumento=" + pkDetalhesDocumento + " ]";
    }
    
}
