package util.fe;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import util.fe.dto.FacturaRemotaDTO;
import util.fe.http.HttpClientUtil;
import util.fe.payloads.PayloadFactory;

/**
 * Lista as facturas que estão no portal da AGT (serviço listarFacturas) para
 * um intervalo de datas. NÃO tem UI e NÃO toca na BD.
 *
 * A AGT devolve no máximo 10 facturas por resposta: este utilitário pede as
 * páginas seguintes até ter todas (documentResultCount).
 */
public class ListarFacturasAGTUtil {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private static final int MAX_PAGINAS = 200;       // limite de segurança (2000 facturas)
    private static final long PAUSA_ENTRE_PAGINAS = 1000; // evita o E98 (demasiados pedidos)

    /**
     * Resultado da listagem. Se "mensagemErro" vier preenchida, a lista pode
     * estar incompleta (ex.: E98 a meio da paginação).
     */
    public static class Resultado {

        private final List<FacturaRemotaDTO> facturas = new ArrayList<>();
        private int totalNaAGT = -1;
        private String errorCode;
        private String mensagemErro;
        private String respostaBruta;

        public List<FacturaRemotaDTO> getFacturas() {
            return facturas;
        }

        public int getTotalNaAGT() {
            return totalNaAGT;
        }

        public String getErrorCode() {
            return errorCode;
        }

        public String getMensagemErro() {
            return mensagemErro;
        }

        public String getRespostaBruta() {
            return respostaBruta;
        }

        public boolean isCompleto() {
            return mensagemErro == null && facturas.size() >= totalNaAGT;
        }
    }

    /**
     * @param inicio data inicial (inclusiva, só conta o dia)
     * @param fim data final (inclusiva, só conta o dia)
     */
    public static Resultado listar(String nif, Date inicio, Date fim) {

        Resultado res = new Resultado();

        // Datas no fuso local: o DataUtil.converterNormal usa UTC e, em Angola
        // (UTC+1), a meia-noite local passaria para o dia anterior.
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        String dataInicio = fmt.format(inicio);
        String dataFim = fmt.format(fim);

        try {
            String auth = BasicAuthUtil.gerarAuthorizationHeader(FEConfig.getUsername(), FEConfig.getPassword());

            for (int pagina = 0; pagina < MAX_PAGINAS; pagina++) {

                if (pagina > 0) {
                    Thread.sleep(PAUSA_ENTRE_PAGINAS);
                }

                // 1ª página sem o campo "page" (igual ao exemplo da AGT)
                Map<String, Object> payload = PayloadFactory.criarPayloadListarFacturas(
                        nif, dataInicio, dataFim, pagina == 0 ? null : pagina);

                String json = JsonUtil.toJson(payload);
                System.out.println("[listarFacturas] " + FEConfig.getEndpointListarFacturas() + "\n" + json);

                String resposta = HttpClientUtil.postJson(FEConfig.getEndpointListarFacturas(), json, auth);
                res.respostaBruta = resposta;
                System.out.println("[listarFacturas] resposta: " + resposta);

                // Resposta que não é JSON (ex.: SOAP Fault em XML ou página HTML de erro)
                String r = resposta == null ? "" : resposta.trim();
                if (!r.startsWith("{") && !r.startsWith("[")) {
                    res.mensagemErro = "A AGT devolveu uma resposta que não é JSON"
                            + " (endpoint: " + FEConfig.getEndpointListarFacturas() + ").\n"
                            + resumoRespostaNaoJson(r);
                    return res;
                }

                JsonNode root = MAPPER.readTree(resposta);

                // A AGT responde { "statusResult": {...}, "paginator": {...} };
                // a documentação mostra os campos directamente na raiz: aceita os dois.
                JsonNode corpo = root.has("statusResult") ? root.get("statusResult") : root;

                // Em produção a lista vem em "resultEntryList" (itens com
                // "documentEntryResult"); a documentação usa "documentResultList".
                String campoLista = corpo.has("resultEntryList") ? "resultEntryList" : "documentResultList";

                // Erros (400 E96 / 422 E94 / 429 E98 / errorList de validação)
                if (!corpo.has(campoLista) && !corpo.has("documentResultCount")) {
                    preencherErro(root, res);
                    return res;
                }

                // requestErrorList vem como [ "" ] quando não há erros
                String errosPedido = errosDoPedido(corpo.path("requestErrorList"));
                if (errosPedido != null && !corpo.has(campoLista)) {
                    res.mensagemErro = errosPedido;
                    return res;
                }

                res.totalNaAGT = corpo.path("documentResultCount").asInt(0);

                JsonNode lista = corpo.path(campoLista);
                int recebidas = 0;
                for (JsonNode item : lista) {
                    res.facturas.add(converter(item));
                    recebidas++;
                }

                // paginator.totalPaginas vem como texto ("1")
                int totalPaginas = root.path("paginator").path("totalPaginas").asInt(-1);

                if (recebidas == 0
                        || res.facturas.size() >= res.totalNaAGT
                        || (totalPaginas > 0 && pagina + 1 >= totalPaginas)) {
                    break; // já temos tudo
                }
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            res.mensagemErro = "Listagem cancelada.";
        } catch (Exception e) {
            e.printStackTrace();
            res.mensagemErro = "Falha de comunicação com a AGT: " + e.getMessage();
        }
        return res;
    }

    /**
     * Junta os erros de requestErrorList, ignorando entradas vazias. Aceita
     * texto simples ou objectos {idError, descriptionError}. Devolve null se
     * não houver erros.
     */
    private static String errosDoPedido(JsonNode lista) {
        if (!lista.isArray()) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (JsonNode e : lista) {
            String msg;
            if (e.isObject()) {
                String cod = texto(e, "errorCode", "idError");
                String desc = texto(e, "errorDescription", "descriptionError");
                msg = (cod == null ? "" : cod + " - ") + (desc == null ? "" : desc);
            } else {
                msg = e.asText("");
            }
            if (!msg.trim().isEmpty()) {
                sb.append(sb.length() == 0 ? "" : "\n").append("• ").append(msg.trim());
            }
        }
        return sb.length() == 0 ? null : sb.toString();
    }

    private static void preencherErro(JsonNode root, Resultado res) {
        String cod = texto(root, "errorCode", "idError");
        String desc = texto(root, "errorDescription", "descriptionError");

        // errorList [ {idError, descriptionError} ]
        JsonNode errorList = root.path("errorList");
        if (cod == null && errorList.isArray() && errorList.size() > 0) {
            StringBuilder sb = new StringBuilder();
            for (JsonNode e : errorList) {
                if (cod == null) {
                    cod = texto(e, "errorCode", "idError");
                }
                sb.append("\n• ").append(texto(e, "errorCode", "idError"))
                        .append(" - ").append(texto(e, "errorDescription", "descriptionError"));
            }
            desc = sb.toString();
        }

        res.errorCode = cod;
        if (cod == null && desc == null) {
            res.mensagemErro = "Resposta inesperada da AGT: " + res.respostaBruta;
        } else {
            res.mensagemErro = (cod == null ? "" : cod + " - ") + (desc == null ? "" : desc);
        }
        if ("E98".equals(cod)) {
            res.mensagemErro += "\nDemasiados pedidos seguidos. Aguarde um pouco e tente de novo.";
        }
    }

    /**
     * Extrai o &lt;faultstring&gt; de um SOAP Fault; senão devolve o início da
     * resposta.
     */
    private static String resumoRespostaNaoJson(String r) {
        if (r.isEmpty()) {
            return "Resposta vazia.";
        }
        int i = r.indexOf("<faultstring>");
        int j = r.indexOf("</faultstring>");
        if (i >= 0 && j > i) {
            return r.substring(i + "<faultstring>".length(), j);
        }
        return r.length() > 300 ? r.substring(0, 300) + "..." : r;
    }

    private static FacturaRemotaDTO converter(JsonNode item) {
        FacturaRemotaDTO f = new FacturaRemotaDTO();
        // Produção: { "documentEntryResult": {...campos planos...} }
        // Documentação: { "documentInfo": {..., "documentTotals": {...}} }
        JsonNode info = item.has("documentEntryResult")
                ? item.get("documentEntryResult")
                : item.path("documentInfo");

        f.setDocumentNo(texto(info, "documentNo"));
        f.setDocumentType(texto(info, "documentType"));
        f.setDocumentDate(texto(info, "documentDate"));
        f.setSystemEntryDate(texto(info, "systemEntryDate"));
        f.setDocumentStatus(texto(info, "documentStatus"));
        f.setRejectedDocumentNo(texto(info, "rejectedDocumentNo"));
        f.setSupplierTaxRegistrationNumber(texto(info, "supplierTaxRegistrationNumber", "taxRegistrationNumber"));
        // Produção devolve "customerTaxId" (d minúsculo)
        f.setCustomerTaxID(texto(info, "customerTaxId", "customerTaxID", "customerTaxRegistrationNumber"));
        f.setCustomerName(texto(info, "customerName"));
        String aceitacao = texto(item, "customerAcceptance");
        f.setCustomerAcceptance(aceitacao != null ? aceitacao : texto(info, "documentConfirmStatus"));

        JsonNode totais = info.has("documentTotals") ? info.get("documentTotals") : info;
        f.setNetTotal(texto(totais, "netTotal"));
        f.setTaxPayable(texto(totais, "taxPayable"));
        f.setGrossTotal(texto(totais, "grossTotal"));

        f.setNumReferencias(item.path("referenceList").size());
        f.setNumPagamentos(item.path("paymentList").size());

        try {
            f.setJsonBruto(MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(item));
        } catch (Exception e) {
            f.setJsonBruto(item.toString());
        }
        return f;
    }

    /**
     * Devolve o primeiro campo existente e não vazio entre os nomes dados.
     */
    private static String texto(JsonNode node, String... campos) {
        if (node == null || node.isMissingNode()) {
            return null;
        }
        for (String c : campos) {
            JsonNode n = node.get(c);
            if (n != null && !n.isNull() && !n.asText().trim().isEmpty()) {
                return n.asText().trim();
            }
        }
        return null;
    }
}
