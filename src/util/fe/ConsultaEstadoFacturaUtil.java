/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util.fe;

/**
 *
 * @author MARTINHO
 */
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import entity.TbVenda;
import comercial.controller.VendasController; // <-- ajusta o import ao teu pacote real
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import util.fe.dto.ConsultaEstadoDTO;
import util.fe.http.HttpClientUtil;
import util.fe.payloads.PayloadFactory;

/**
 * Consulta independente do estado de facturas já submetidas (obterEstado) e
 * actualização local. NÃO tem UI (sem JOptionPane): quem chama decide como
 * mostrar o resultado.
 *
 * Estados locais usados (iguais aos teus): "P" pendente, "V" validado, "I"
 * inválido.
 *
 */
public class ConsultaEstadoFacturaUtil {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private static final String ESTADO_PENDENTE = "P";
    private static final String ESTADO_VALIDO = "V";
    private static final String ESTADO_INVALIDO = "I";

    // =====================================================
    // 1. CONSULTAR NA AGT (não toca na BD)
    // =====================================================
    public static ConsultaEstadoDTO consultar(String nif, String requestID, String codFact) {

        ConsultaEstadoDTO dto = new ConsultaEstadoDTO();
        dto.setRequestID(requestID);
        dto.setCodFact(codFact);

        if (requestID == null || requestID.trim().isEmpty()) {
            dto.setMensagem("A factura não tem requestID. Ainda não foi submetida com sucesso à AGT.");
            return dto;
        }

        try {
            Map<String, Object> payload = PayloadFactory.consultaPayloadFactura(nif, requestID.trim());
            String json = JsonUtil.toJson(payload);
            String auth = BasicAuthUtil.gerarAuthorizationHeader(FEConfig.getUsername(), FEConfig.getPassword());

            String resposta = HttpClientUtil.postJson(FEConfig.getEndpointObterEstado(), json, auth);
            dto.setRespostaBruta(resposta);

            interpretarResposta(resposta, codFact, dto);

        } catch (Exception e) {
            e.printStackTrace();
            dto.setPodeTentarMaisTarde(true);
            dto.setMensagem("Falha de comunicação com a AGT: " + e.getMessage());
        }
        return dto;
    }

    // =====================================================
    // 2. CONSULTAR + ACTUALIZAR A VENDA LOCAL
    // =====================================================
    public static ConsultaEstadoDTO consultarEActualizar(
            TbVenda venda,
            String nif,
            VendasController vendasController,
            Connection connection) {

        ConsultaEstadoDTO dto = consultar(nif, venda.getRequestID(), venda.getCodFact());

        String novoEstado = dto.getEstadoLocal();

        // Só grava quando a AGT trouxe um estado final (V ou I) e ele mudou
        if (novoEstado != null && !novoEstado.equals(venda.getEstado())) {
            venda.setEstado(novoEstado);
            boolean gravado = vendasController.actualizarEstadoFE(venda, connection);
            dto.setEstadoAlterado(gravado);
            if (!gravado) {
                dto.getErros().add("O estado foi obtido na AGT mas não foi possível gravar na base de dados.");
            }
        }
        return dto;
    }

    // =====================================================
    // 3. LOTE: percorre vendas pendentes
    // =====================================================
    public static List<ConsultaEstadoDTO> consultarPendentes(
            List<TbVenda> vendasPendentes,
            String nif,
            VendasController vendasController,
            Connection connection) {

        List<ConsultaEstadoDTO> resultados = new ArrayList<>();

        for (TbVenda venda : vendasPendentes) {
            ConsultaEstadoDTO dto = consultarEActualizar(venda, nif, vendasController, connection);
            resultados.add(dto);

            // E98 = demasiadas solicitações repetidas -> parar já, tentar mais tarde
            if ("E98".equals(dto.getErrorCode())) {
                break;
            }

            // Pausa entre chamadas para não ser bloqueado por excesso de pedidos
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        return resultados;
    }

    // =====================================================
    // INTERPRETAÇÃO DA RESPOSTA
    // =====================================================
    private static void interpretarResposta(String resposta, String codFact, ConsultaEstadoDTO dto) throws Exception {

        JsonNode root = MAPPER.readTree(resposta);

        // ---- Erros HTTP (400 / 422 / 429): body = errorEntry { errorCode, errorDescription }
        // (aceita também os nomes antigos idError / descriptionError que já usas)
        String codErro = texto(root, "errorCode", "idError");
        if (codErro != null && !root.has("resultCode")) {
            String descErro = texto(root, "errorDescription", "descriptionError");
            dto.setErrorCode(codErro);
            dto.setMensagem(codErro + " - " + (descErro == null ? "" : descErro));

            boolean emProcessamento = descErro != null && descErro.toLowerCase().contains("processamento");
            dto.setPodeTentarMaisTarde("E97".equals(codErro) || "E98".equals(codErro)
                    || ("E96".equals(codErro) && emProcessamento));
            return;
        }

        // ---- Resposta 200 (statusResult)
        int resultCode = root.path("resultCode").asInt(-1); // aceita 2 ou "2"
        dto.setResultCode(resultCode);

        // Erros ao nível do pedido (informativo)
        JsonNode requestErrorList = root.path("requestErrorList");
        if (requestErrorList.isArray()) {
            for (JsonNode e : requestErrorList) {
                dto.getErros().add(texto(e, "errorCode", "idError") + " - " + texto(e, "errorDescription", "descriptionError"));
            }
        }

        switch (resultCode) {
            case 7:
                dto.setPodeTentarMaisTarde(true);
                dto.setMensagem("Solicitação prematura ou repetida. Aguarde antes de consultar novamente.");
                return;
            case 8:
                dto.setPodeTentarMaisTarde(true);
                dto.setMensagem("A AGT ainda está a processar o pedido. Estado mantém-se PENDENTE.");
                return;
            case 9:
                dto.setCancelado(true);
                dto.setMensagem("Processamento cancelado pela AGT.");
                return;
            case 0:
            case 1:
            case 2:
                break; // processamento concluído -> analisa documentStatusList
            default:
                dto.setMensagem("resultCode desconhecido: " + resultCode);
                return;
        }

        // ---- Procurar a nossa factura na lista (por documentNo; senão a primeira)
        JsonNode lista = root.path("documentStatusList");
        JsonNode doc = null;
        if (lista.isArray()) {
            for (JsonNode d : lista) {
                if (codFact != null && codFact.equals(d.path("documentNo").asText())) {
                    doc = d;
                    break;
                }
            }
            if (doc == null && lista.size() > 0) {
                doc = lista.get(0);
            }
        }

        if (doc == null) {
            dto.setMensagem("Processamento concluído mas a factura não consta na resposta.");
            return;
        }

        String status = doc.path("documentStatus").asText("");
        dto.setDocumentStatus(status);

        if (ESTADO_VALIDO.equalsIgnoreCase(status)) {
            dto.setEstadoLocal(ESTADO_VALIDO);
            dto.setMensagem("Documento validado com sucesso.");

        } else if (ESTADO_INVALIDO.equalsIgnoreCase(status)) {
            dto.setEstadoLocal(ESTADO_INVALIDO);
            dto.setMensagem("Documento inválido.");
            JsonNode errorList = doc.path("errorList");
            if (errorList.isArray()) {
                for (JsonNode e : errorList) {
                    dto.getErros().add(texto(e, "errorCode", "idError") + " - " + texto(e, "errorDescription", "descriptionError"));
                }
            }
        } else {
            dto.setMensagem("Estado de documento não reconhecido: " + status);
        }
    }

    /**
     * Devolve o primeiro campo existente e não vazio entre os nomes dados.
     */
    private static String texto(JsonNode node, String... campos) {
        for (String c : campos) {
            JsonNode n = node.get(c);
            if (n != null && !n.isNull() && !n.asText().trim().isEmpty()) {
                return n.asText().trim();
            }
        }
        return null;
    }
}

