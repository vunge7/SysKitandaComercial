package util.fe.payloads;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import entity.TbVenda;
import java.util.*;
import java.util.stream.Collectors;
import javax.swing.JOptionPane;
import util.fe.BasicAuthUtil;
import util.fe.DataUtil;
import util.fe.FEConfig;
import util.fe.JsonUtil;
import util.fe.JwsGenerator;
import util.fe.SubmissionUUID;
import util.fe.dto.*;
import util.fe.http.HttpClientUtil;

public class PayloadFactory
{

    private static final String SOFTWARE_KEY = "Chaves/ChavePrivada_2048_PKCS8.pem";
    private static final String CLIENT_KEY = "Chaves/chave_cliente/ChavePrivada2048Cliente.pem";

    // =====================================================
    // BASE PAYLOAD
    // =====================================================
    private static Map<String, Object> criarBasePayload( String taxRegistrationNumber )
    {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put( "schemaVersion", "1.0" );
        payload.put( "submissionUUID", SubmissionUUID.gerar() );
        payload.put( "taxRegistrationNumber", taxRegistrationNumber );
        payload.put( "submissionTimeStamp", DataUtil.converter( new Date() ) );
        payload.put( "softwareInfo", criarSoftwareInfo() );
        return payload;
    }

    private static Map<String, Object> criarSoftwareInfo()
    {
        Map<String, Object> softwareInfoDetail = JwsGenerator.softwareInfoDetail();
        String jwsSoftwareSignature = JwsGenerator.gerarJws( SOFTWARE_KEY, softwareInfoDetail );

        Map<String, Object> softwareInfo = new LinkedHashMap<>();
        softwareInfo.put( "softwareInfoDetail", softwareInfoDetail );
        softwareInfo.put( "jwsSoftwareSignature", jwsSoftwareSignature );
        return softwareInfo;
    }

    // =====================================================
    // CRIAR SÉRIE
    // =====================================================
    public static Map<String, Object> criarPayloadCriarSerie(
            String taxRegistrationNumber,
            String seriesYear,
            String documentType
    )
    {

        Map<String, Object> payload = criarBasePayload( taxRegistrationNumber );

//        System.out.println( "#taxRegistrationNumber: " + taxRegistrationNumber );
//        System.out.println( "#seriesYear: " + seriesYear );
//        System.out.println( "#documentType: " + documentType );
//
        String submissionUUID = payload.get( "submissionUUID" ).toString();
//        System.out.println( "submissionUUID" + submissionUUID );

        payload.put( "seriesYear", seriesYear );
        payload.put( "documentType", documentType );
        payload.put( "establishmentNumber", "I" );

        Map<String, Object> signatureMap = new LinkedHashMap<>();
        signatureMap.put( "taxRegistrationNumber", taxRegistrationNumber );
        signatureMap.put( "submissionUUID", submissionUUID );
        signatureMap.put( "seriesYear", seriesYear );
        signatureMap.put( "documentType", documentType );
        signatureMap.put( "establishmentNumber", "I" );
//        signatureMap.put( "seriesContingencyIndicator", "N" );

//        payload.put( "jwsSignature", JwsGenerator.gerarJws( SOFTWARE_KEY, signatureMap ) );
        payload.put( "jwsSignature", JwsGenerator.gerarJws( CLIENT_KEY, signatureMap ) );
        payload.put( "seriesContingencyIndicator", "N" );
        return payload;
    }

    // =====================================================
    // LISTAR FATURAS
    // =====================================================
    public static Map<String, Object> criarPayloadListarFacturas( String taxRegistrationNumber, String queryStartDate, String queryEndDate )
    {

        Map<String, Object> payload = criarBasePayload( taxRegistrationNumber );

        Map<String, Object> signatureMap = new LinkedHashMap<>();
        signatureMap.put( "taxRegistrationNumber", taxRegistrationNumber );
        signatureMap.put( "queryStartDate", queryStartDate );
        signatureMap.put( "queryEndDate", queryEndDate );

        payload.put( "jwsSignature", JwsGenerator.gerarJws( CLIENT_KEY, signatureMap ) );
        payload.put( "queryStartDate", queryStartDate );
        payload.put( "queryEndDate", queryEndDate );

        return payload;
    }

    // =====================================================
    // CRIAR DOCUMENTOS
    // =====================================================
    public static Map<String, Object> criarPayloadCriarDocumento( String taxRegistrationNumber, List<DocumentDTO> docs )
    {

        Map<String, Object> payload = criarBasePayload( taxRegistrationNumber );

        payload.put( "numberOfEntries", docs.size() );
        payload.put( "documents", montarDocumentos( taxRegistrationNumber, docs ) );

        // 🔐 Assinatura do payload principal
        Map<String, Object> signatureMap = new LinkedHashMap<>();
        signatureMap.put( "taxRegistrationNumber", taxRegistrationNumber );
        signatureMap.put( "numberOfEntries", docs.size() );

        payload.put( "jwsSignature", JwsGenerator.gerarJws( CLIENT_KEY, signatureMap ) );

        return payload;
    }

    private static List<Map<String, Object>> montarDocumentos( String taxRegistrationNumber, List<DocumentDTO> docs )
    {

        return docs.stream().map( dto ->
        {
            Map<String, Object> document = montarCamposBaseDocumento( taxRegistrationNumber, dto );

            String tipo = dto.getDocumentType();

            switch (tipo)
            {
                case "FT":
                case "FR":
                case "NC":
                    montarFatura( document, dto );
                    break;
                case "RC":
                    montarRecibo( document, dto );
                    break;
                default:
                    throw new AssertionError();
            }

            return document;
        } ).collect( Collectors.toList() );
    }

    private static Map<String, Object> montarCamposBaseDocumento( String taxRegistrationNumber, DocumentDTO dto )
    {

        Map<String, Object> document = new LinkedHashMap<>();

        Map<String, Object> signaturePayload = jwsDocumentSignature( taxRegistrationNumber, dto );
        document.put( "jwsDocumentSignature", JwsGenerator.gerarJws( CLIENT_KEY, signaturePayload ) );

        document.put( "documentNo", dto.getDocumentNo() );
        document.put( "documentStatus", dto.getDocumentStatus() );
        document.put( "documentDate", dto.getDocumentDate() );
        document.put( "documentType", dto.getDocumentType() );
        document.put( "eacCode", dto.getEacCode() );
        document.put( "systemEntryDate", dto.getSystemEntryDate() );
        document.put( "customerTaxID", dto.getCustomerTaxID() );
        document.put( "customerCountry", dto.getCustomerCountry() );
        document.put( "companyName", dto.getCompanyName() );

        return document;
    }

    // =====================================================
    // FATURA
    // =====================================================
    private static void montarFatura( Map<String, Object> document, DocumentDTO dto )
    {

        document.put( "lines", dto.getLines().stream()
                .map( e -> linhaToMap( e, dto.getDocumentType() ) )
                .collect( Collectors.toList() ) );

        document.put( "documentTotals", totaisToMap( dto ) );
        adicionarRetencoes( document, dto );
    }

    private static Map<String, Object> linhaToMap( LineDTO l, String documentType )
    {

        Map<String, Object> linha = new LinkedHashMap<>();
        linha.put( "lineNumber", l.getLineNumber() );
        linha.put( "productCode", l.getProductCode() );
        linha.put( "productDescription", l.getProductDescription() );
        linha.put( "quantity", l.getQuantity() );
        linha.put( "unitOfMeasure", l.getUnitOfMeasure() );
        linha.put( "unitPrice", l.getUnitPrice() );
        linha.put( "unitPriceBase", l.getUnitPriceBase() );
        if ( documentType.endsWith( "NC" ) )
        {
            linha.put( "referenceInfo", l.getReferenceInfoDTOs()
                    .stream()
                    .map( PayloadFactory::referenceInfo )
                    .collect( Collectors.toList() )
            );
        }
        linha.put( "debitAmount", l.getDebitAmount() );
        linha.put( "creditAmount", l.getCreditAmount() );
        linha.put( "settlementAmount", l.getSettlementAmount() );

        if ( Objects.nonNull( l.getTaxes() ) )
        {
            linha.put( "taxes", l.getTaxes().stream()
                    .map( PayloadFactory::taxToMap )
                    .collect( Collectors.toList() ) );
        }

        return linha;
    }

    private static Map<String, Object> referenceInfo( ReferenceInfoDTO r )
    {
        Map<String, Object> referenceInfo = new LinkedHashMap<>();
        referenceInfo.put( "referenceItemLineNo", r.getReferenceItemLineNo() );
        referenceInfo.put( "reference", r.getReference() );
        referenceInfo.put( "reason", r.getReason() );
        return referenceInfo;
    }

    private static Map<String, Object> taxToMap( TaxDTO t )
    {

        Map<String, Object> tax = new LinkedHashMap<>();
        tax.put( "taxType", t.getTaxType() );
        tax.put( "taxCountryRegion", t.getTaxCountryRegion() );
        tax.put( "taxCode", t.getTaxCode() );
        tax.put( "taxPercentage", t.getTaxPercentage() );
        tax.put( "taxContribution", t.getTaxContribution() );
        return tax;
    }

    // =====================================================
    // RECIBO
//    // =====================================================
//    private static void montarRecibo( Map<String, Object> document, DocumentDTO dto )
//    {
//
//        List<Map<String, Object>> sourceDocs = dto.getSourceDocuments().stream()
//                .map( sd ->
//                {
//                    Map<String, Object> m = new LinkedHashMap<>();
//                    m.put( "lineNo", sd.getLineNo() );
//
//                    Map<String, Object> id = new LinkedHashMap<>();
//                    id.put( "originatingON", sd.getOriginatingON() );
//                    id.put( "documentDate", sd.getDocumentDate() );
//
//                    m.put( "sourceDocumentID", id );
//                    m.put( "creditAmount", sd.getCreditAmount() );
//                    return m;
//                } ).collect( Collectors.toList() );
//
//        Map<String, Object> paymentReceipt = new LinkedHashMap<>();
//        paymentReceipt.put( "sourceDocuments", sourceDocs );
//
//        document.put( "paymentReceipt", paymentReceipt );
//        document.put( "documentTotals", totaisToMap( dto ) );
//    }
    // =====================================================
// RECIBO
// =====================================================
    private static void montarRecibo( Map<String, Object> document, DocumentDTO dto )
    {
        List<Map<String, Object>> sourceDocs = new ArrayList<>();

        for ( SourceDocumentDTO sd : dto.getSourceDocuments() )
        {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put( "lineNo", sd.getLineNo() );

            Map<String, Object> id = new LinkedHashMap<>();
            id.put( "originatingON", sd.getOriginatingON() );
            id.put( "documentDate", sd.getDocumentDate() );

            m.put( "sourceDocumentID", id );
            m.put( "creditAmount", sd.getCreditAmount() );

            sourceDocs.add( m );
        }

        Map<String, Object> paymentReceipt = new LinkedHashMap<>();
        paymentReceipt.put( "sourceDocuments", sourceDocs );

        document.put( "paymentReceipt", paymentReceipt );
        document.put( "documentTotals", totaisToMap( dto ) );
    }

    private static Map<String, Object> totaisToMap( DocumentDTO dto )
    {

        Map<String, Object> totals = new LinkedHashMap<>();
        totals.put( "taxPayable", dto.getDocumentTotals().getTaxPayable() );
        totals.put( "netTotal", dto.getDocumentTotals().getNetTotal() );
        totals.put( "grossTotal", dto.getDocumentTotals().getGrossTotal() );
        return totals;
    }

    private static void adicionarRetencoes( Map<String, Object> document, DocumentDTO dto )
    {

        if ( dto.getWithholdingTaxList() == null )
        {
            return;
        }

        List<Map<String, Object>> list = dto.getWithholdingTaxList().stream()
                .map( w ->
                {
                    Map<String, Object> m = new LinkedHashMap<>();
                    m.put( "withholdingTaxType", w.getWithholdingTaxType() );
                    m.put( "withholdingTaxDescription", w.getWithholdingTaxDescription() );
                    m.put( "withholdingTaxAmount", w.getWithholdingTaxAmount() );
                    return m;
                } ).collect( Collectors.toList() );

        document.put( "withholdingTaxList", list );
    }

    // =====================================================
    // ASSINATURA DOCUMENTO
    // =====================================================
    public static Map<String, Object> jwsDocumentSignature( String taxRegistrationNumber, DocumentDTO dto )
    {

        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put( "documentNo", dto.getDocumentNo() );
        payload.put( "taxRegistrationNumber", taxRegistrationNumber );
        payload.put( "documentType", dto.getDocumentType() );
        payload.put( "documentDate", dto.getDocumentDate() );
        payload.put( "customerTaxID", dto.getCustomerTaxID() );
        payload.put( "customerCountry", dto.getCustomerCountry() );
        payload.put( "companyName", dto.getCompanyName() );
        payload.put( "documentTotals", totaisToMap( dto ) );

        return payload;
    }

    public static Map<String, Object> consultaPayloadFactura(
            String taxRegistrationNumber,
            String requestID )
    {

        Map<String, Object> softwareInfoDetail = JwsGenerator.softwareInfoDetail();
        Map<String, Object> jsonJWSignature = JwsGenerator.jwsConsutlarFactura( taxRegistrationNumber, requestID );

        String jwsSoftwareSignature = JwsGenerator.gerarJws( "Chaves/ChavePrivada_2048_PKCS8.pem", softwareInfoDetail );
        String jwsSignature = JwsGenerator.gerarJws( "Chaves/chave_cliente/ChavePrivada2048Cliente.pem", jsonJWSignature );

        // softwareInfo
        Map<String, Object> softwareInfo = new LinkedHashMap<>();
        softwareInfo.put( "softwareInfoDetail", softwareInfoDetail );
        softwareInfo.put( "jwsSoftwareSignature", jwsSoftwareSignature );

        // payload principal
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put( "schemaVersion", "1.0" );
        payload.put( "submissionUUID", SubmissionUUID.gerar() );
        payload.put( "taxRegistrationNumber", taxRegistrationNumber );
        payload.put( "submissionTimeStamp", DataUtil.converter( new Date() ) );
        payload.put( "softwareInfo", softwareInfo );
        payload.put( "jwsSignature", jwsSignature );
        payload.put( "requestID", requestID );

        return payload;
    }

    public static boolean obterEstadoFactura(
            String taxRegistrationNumber,
            String resposta,
            TbVenda venda
    ) throws JsonProcessingException
    {
        ObjectMapper mapper = new ObjectMapper();

        // 1️⃣ Extrair requestID
        JsonNode rootNode = mapper.readTree( resposta );
        String requestID = rootNode.get( "requestID" ).asText();
        venda.setRequestID( requestID );

        System.out.println( "Request ID: " + requestID );

        // 2️⃣ Criar payload de consulta
        Map<String, Object> jsonPayload = PayloadFactory.consultaPayloadFactura(
                taxRegistrationNumber,
                requestID
        );

        String payload = JsonUtil.toJson( jsonPayload );
        System.out.println( payload );

        String basicAuth = BasicAuthUtil.gerarAuthorizationHeader(
                FEConfig.getUsername(),
                FEConfig.getPassword()
        );

        try
        {
            // 3️⃣ Chamada à FE
            String r = HttpClientUtil.postJson(
                    FEConfig.getEndpointObterEstado(),
                    payload,
                    basicAuth
            );

            JsonUtil.print( r );

            JsonNode estadoRoot = mapper.readTree( r );

            JsonNode documentStatusList = estadoRoot.get( "documentStatusList" );
            JsonNode requestErrorList = estadoRoot.get( "requestErrorList" );

            // =====================================================
            // 🔎 1º VERIFICAR ERROS DO PEDIDO (ANTES DO DOCUMENTO)
            // =====================================================
            if ( requestErrorList != null && requestErrorList.isArray() && requestErrorList.size() > 0 )
            {
                for ( JsonNode erro : requestErrorList )
                {

                    if ( Objects.nonNull( erro.get( "idError" ) ) )
                    {
                        String idErro = erro.get( "idError" ).asText();
                        venda.setEstado( "P" );

                        // ✅ REGRA DE NEGÓCIO
                        if ( "E94".equalsIgnoreCase( idErro ) )
                        {
                            JOptionPane.showMessageDialog( null, "Documeto processado no estado PENDENTE.\nAguardando a resposta da AGT." );
                            System.out.println( "E94 - Solicitação não encontrada. Considerando TRUE." );
                            return true;
                        }

                        // Outros erros reais
                        JOptionPane.showMessageDialog(
                                null,
                                "Erro da FE: " + erro.get( "descriptionError" ).asText(),
                                "Erro na Consulta",
                                JOptionPane.ERROR_MESSAGE
                        );
                        return false;
                    }

                }
            }

            // =====================================================
            // 🔎 2º VERIFICAR STATUS DA FACTURA
            // =====================================================
            if ( documentStatusList != null && documentStatusList.isArray() && documentStatusList.size() > 0 )
            {
                JsonNode doc = documentStatusList.get( 0 );
                String documentStatus = doc.get( "documentStatus" ).asText();

                // ✅ FACTURA VÁLIDA
                if ( "V".equalsIgnoreCase( documentStatus ) )
                {
                    JOptionPane.showMessageDialog(
                            null,
                            "Documento validado com sucesso ✅",
                            "Factura Válida",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                    venda.setEstado( "V" );
                    return true;
                }
                // ❌ FACTURA INVÁLIDA
                else if ( "I".equalsIgnoreCase( documentStatus ) )
                {
                    venda.setEstado( "I" );
                    StringBuilder mensagens = new StringBuilder( "Documento inválido ❌\n\n" );

                    JsonNode errorList = doc.get( "errorList" );

                    if ( errorList != null && errorList.isArray() )
                    {
                        for ( JsonNode erro : errorList )
                        {
                            mensagens.append( "• " )
                                    .append( erro.get( "descriptionError" ).asText() )
                                    .append( "\n" );
                        }
                    }

                    JOptionPane.showMessageDialog(
                            null,
                            mensagens.toString(),
                            "Erro de Validação",
                            JOptionPane.ERROR_MESSAGE
                    );

                    return false;
                }
            }

        }
        catch ( Exception e )
        {
            e.printStackTrace();
            return false;
        }

        // Caso inesperado
        return false;
    }

}
