package util.fe.payloads;

import java.util.*;
import java.util.stream.Collectors;
import util.fe.DataUtil;
import util.fe.JwsGenerator;
import util.fe.SubmissionUUID;
import util.fe.dto.*;

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
        payload.put( "schemaVersion", "1.2" );
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
    public static Map<String, Object> criarPayloadCriarSerie( String taxRegistrationNumber, String seriesYear, String documentType )
    {

        Map<String, Object> payload = criarBasePayload( taxRegistrationNumber );

        payload.put( "seriesYear", seriesYear );
        payload.put( "documentType", documentType );
        payload.put( "establishmentNumber", "SEDE" );
        payload.put( "seriesContingencyIndicator", "N" );

        Map<String, Object> signatureMap = new LinkedHashMap<>();
        signatureMap.put( "taxRegistrationNumber", taxRegistrationNumber );
        signatureMap.put( "seriesYear", seriesYear );
        signatureMap.put( "documentType", documentType );
        signatureMap.put( "establishmentNumber", "SEDE" );
        signatureMap.put( "seriesContingencyIndicator", "N" );

        payload.put( "jwsSignature", JwsGenerator.gerarJws( SOFTWARE_KEY, signatureMap ) );
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
                .map( PayloadFactory::linhaToMap )
                .collect( Collectors.toList() ) );

        document.put( "documentTotals", totaisToMap( dto ) );
        adicionarRetencoes( document, dto );
    }

    private static Map<String, Object> linhaToMap( LineDTO l )
    {

        Map<String, Object> linha = new LinkedHashMap<>();
        linha.put( "lineNumber", l.getLineNumber() );
        linha.put( "productCode", l.getProductCode() );
        linha.put( "productDescription", l.getProductDescription() );
        linha.put( "quantity", l.getQuantity() );
        linha.put( "unitOfMeasure", l.getUnitOfMeasure() );
        linha.put( "unitPrice", l.getUnitPrice() );
        linha.put( "unitPriceBase", l.getUnitPriceBase() );
        linha.put( "debitAmount", l.getDebitAmount() );
        linha.put( "creditAmount", l.getCreditAmount() );
        linha.put( "settlementAmount", l.getSettlementAmount() );

        linha.put( "taxes", l.getTaxes().stream()
                .map( PayloadFactory::taxToMap )
                .collect( Collectors.toList() ) );

        return linha;
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
    // =====================================================
    private static void montarRecibo( Map<String, Object> document, DocumentDTO dto )
    {

        List<Map<String, Object>> sourceDocs = dto.getSourceDocuments().stream()
                .map( sd ->
                {
                    Map<String, Object> m = new LinkedHashMap<>();
                    m.put( "lineNo", sd.getLineNo() );

                    Map<String, Object> id = new LinkedHashMap<>();
                    id.put( "originatingON", sd.getOriginatingON() );
                    id.put( "documentDate", sd.getDocumentDate() );

                    m.put( "sourceDocumentID", id );
                    m.put( "creditAmount", sd.getCreditAmount() );
                    return m;
                } ).collect( Collectors.toList() );

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
        payload.put( "schemaVersion", "1.2" );
        payload.put( "submissionUUID", SubmissionUUID.gerar() );
        payload.put( "taxRegistrationNumber", taxRegistrationNumber );
        payload.put( "submissionTimeStamp", DataUtil.converter( new Date() ) );
        payload.put( "softwareInfo", softwareInfo );
        payload.put( "jwsSignature", jwsSignature );
        payload.put( "requestID", requestID );

        return payload;
    }

}
