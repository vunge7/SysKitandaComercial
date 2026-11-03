package util.fe.dto;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Linha do histórico de facturação de um cliente (consulta individual local ou
 * AGT). Os getters numero/tipo/data/total são usados pelo relatório
 * historico_facturacao_A4.
 */
public class DocumentoHistoricoDTO {

    public static final String FACTURA = "Factura";
    public static final String FACTURA_RECIBO = "Factura Recibo";
    public static final String RECIBO = "Recibo";
    public static final String NOTA_CREDITO = "Nota Crédito";

    public static final String ESTADO_VALIDADO = "Validado";
    public static final String ESTADO_PENDENTE = "Pendente";
    public static final String ESTADO_NAO_ENVIADO = "Não enviado";
    public static final String ESTADO_SO_AGT = "Validado (só na AGT)";

    private Integer codigo;     // código da venda na BD local, ou null se não existir
    private String numero;
    private String tipo;
    private Date data;
    private BigDecimal total;
    private String nomeCliente;
    private String moradaCliente;
    private String nifCliente;
    private String estado;      // Validado / Pendente / Não enviado (os inválidos não entram)

    public DocumentoHistoricoDTO() {
    }

    public DocumentoHistoricoDTO(Integer codigo, String numero, String tipo, Date data, BigDecimal total) {
        this.codigo = codigo;
        this.numero = numero;
        this.tipo = tipo;
        this.data = data;
        this.total = total;
    }

    /**
     * Tipo a partir do documento local (tb_venda.fk_documento); null se não
     * for um dos tipos do histórico.
     */
    public static String tipoPorDocumentoLocal(int fkDocumento) {
        switch (fkDocumento) {
            case 1:
                return FACTURA_RECIBO;
            case 2:
                return FACTURA;
            case 5:
                return NOTA_CREDITO;
            case 6:
            case 15:
                return RECIBO;
            default:
                return null;
        }
    }

    /**
     * Tipo a partir do documentType da AGT (FT, FR, RC, RG, NC); null se não
     * for um dos tipos do histórico.
     */
    public static String tipoPorDocumentoAGT(String documentType) {
        if (documentType == null) {
            return null;
        }
        switch (documentType.trim().toUpperCase()) {
            case "FT":
                return FACTURA;
            case "FR":
                return FACTURA_RECIBO;
            case "RC":
            case "RG":
                return RECIBO;
            case "NC":
                return NOTA_CREDITO;
            default:
                return null;
        }
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    /**
     * Texto do estado local (tb_venda.estado); null se for inválido ("I").
     * Sem requestID o documento nunca foi enviado à AGT.
     */
    public static String textoEstadoLocal(String estado, String requestID) {
        if ("I".equalsIgnoreCase(estado)) {
            return null;
        }
        if ("V".equalsIgnoreCase(estado)) {
            return ESTADO_VALIDADO;
        }
        if (requestID == null || requestID.trim().isEmpty()) {
            return ESTADO_NAO_ENVIADO;
        }
        return ESTADO_PENDENTE;
    }

    public String getNifCliente() {
        return nifCliente;
    }

    public void setNifCliente(String nifCliente) {
        this.nifCliente = nifCliente;
    }

    public String getMoradaCliente() {
        return moradaCliente;
    }

    public void setMoradaCliente(String moradaCliente) {
        this.moradaCliente = moradaCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }
}
