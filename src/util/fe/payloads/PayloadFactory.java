/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util.fe.payloads;

/**
 *
 * @author Engº Domingos Dala Vunge
 * @created 23/dez/2025
 * @lastModified 23/dez/2025
 */
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import util.MetodosUtil;
import util.fe.DataUtil;
import util.fe.JsonUtil;
import util.fe.JwsGenerator;
import util.fe.SubmissionUUID;
import util.fe.dto.DocumentDTO;
import util.fe.dto.LineDTO;
import util.fe.dto.TaxDTO;
import util.fe.dto.WithholdingTaxDTO;

public class PayloadFactory
{

    public static Map<String, Object> criarPayloadCriarSerie(
            String taxRegistrationNumber,
            String seriesYear,
            String documentType )
    {

        // softwareInfoDetail
        Map<String, Object> softwareInfoDetail = JwsGenerator.softwareInfoDetail();
        String jwsSoftwareSignature = JwsGenerator.gerarJws( "Chaves/ChavePrivada_2048_PKCS8.pem", softwareInfoDetail );

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
        payload.put( "seriesYear", seriesYear );
        payload.put( "documentType", documentType );
        payload.put( "establishmentNumber", "SEDE" );
        Map<String, Object> HashJwsSignature = getMapJwsSeriesSignature( taxRegistrationNumber, seriesYear, documentType );
        String jwsSignature = JwsGenerator.gerarJws( "Chaves/ChavePrivada_2048_PKCS8.pem", HashJwsSignature );
        payload.put( "jwsSignature", jwsSignature );
        payload.put( "seriesContingencyIndicator", "N" );

        return payload;
    }

    public static Map<String, Object> criarPayloadListarFacturas(
            String taxRegistrationNumber,
            String seriesYear,
            String documentType,
            String queryStartDate,
            String queryEndDate
    )
    {

        // softwareInfoDetail
        Map<String, Object> softwareInfoDetail = JwsGenerator.softwareInfoDetail();
        String jwsSoftwareSignature = JwsGenerator.gerarJws( "Chaves/ChavePrivada_2048_PKCS8.pem", softwareInfoDetail );

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
        Map<String, Object> HashJwsSignature = getMapJwsListarFacturasSignature( taxRegistrationNumber, seriesYear, documentType );
        String jwsSignature = JwsGenerator.gerarJws( "Chaves/chave_cliente/ChavePrivada2048Cliente.pem", HashJwsSignature );
        payload.put( "jwsSignature", jwsSignature );
        payload.put( "queryStartDate", queryStartDate );
        payload.put( "queryEndDate",  queryEndDate ) ;

        return payload;
    }

    public static Map<String, Object> criarPayloadCriarDocumento(
            String taxRegistrationNumber,
            String seriesYear,
            String documentType, List<DocumentDTO> documentDTOs )
    {

        // softwareInfoDetail
        Map<String, Object> softwareInfoDetail = JwsGenerator.softwareInfoDetail();
        String jwsSoftwareSignature = JwsGenerator.gerarJws( "Chaves/ChavePrivada_2048_PKCS8.pem", softwareInfoDetail );

//        String jwsSoftwareSignature = JwsGenerator.gerarJws( "Chaves/ChavePrivada_2048_PKCS8.pem", softwareInfoDetail );
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
        payload.put( "numberOfEntries", documentDTOs.size() );
        payload.put( "documents", getDocumentos( taxRegistrationNumber, documentDTOs ) );

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

    private static List<Map<String, Object>> getDocumentos( String taxRegistrationNumber, List<DocumentDTO> documentDTOs )
    {
        List<Map<String, Object>> documents = new ArrayList<>();

        for ( DocumentDTO documentDTO : documentDTOs )
        {
            Map<String, Object> document = new LinkedHashMap<>();

            Map<String, Object> docSignature = jwsDocumentSignature( taxRegistrationNumber, documentDTO );
            String jwsDocumentSignature = JwsGenerator.gerarJws( "Chaves/chave_cliente/ChavePrivada2048Cliente.pem", docSignature );

            document.put( "documentNo", documentDTO.getDocumentNo() );
            document.put( "documentStatus", documentDTO.getDocumentStatus() );
            document.put( "jwsDocumentSignature", jwsDocumentSignature );
            document.put( "documentDate", documentDTO.getDocumentDate() );
            document.put( "documentType", documentDTO.getDocumentType() );
            document.put( "eacCode", documentDTO.getEacCode() );
            document.put( "systemEntryDate", documentDTO.getSystemEntryDate() );
            document.put( "customerTaxID", documentDTO.getCustomerTaxID() );
            document.put( "customerCountry", documentDTO.getCustomerCountry() );
            document.put( "companyName", documentDTO.getCompanyName() );

            // linhas
            List<Map<String, Object>> linhas = new ArrayList<>();
            for ( LineDTO line : documentDTO.getLines() )
            {
                Map<String, Object> linha = new LinkedHashMap<>();
                linha.put( "lineNumber", line.getLineNumber() );
                linha.put( "productCode", line.getProductCode() );
                linha.put( "productDescription", line.getProductDescription() );
                linha.put( "quantity", line.getQuantity() ); // força inteiro
                linha.put( "unitOfMeasure", line.getUnitOfMeasure() );
                linha.put( "unitPrice", line.getUnitPrice() );
                linha.put( "unitPriceBase", line.getUnitPriceBase() );
                linha.put( "debitAmount", line.getDebitAmount() );
                linha.put( "creditAmount", line.getCreditAmount() );
                linha.put( "settlementAmount", line.getSettlementAmount() );

                List<Map<String, Object>> taxes = new ArrayList<>();
                for ( TaxDTO tax : line.getTaxes() )
                {
                    Map<String, Object> taxMap = new LinkedHashMap<>();
                    taxMap.put( "taxType", tax.getTaxType() );
                    taxMap.put( "taxCountryRegion", tax.getTaxCountryRegion() );
                    taxMap.put( "taxCode", tax.getTaxCode() );
                    taxMap.put( "taxPercentage", tax.getTaxPercentage() );
                    taxMap.put( "taxContribution", tax.getTaxContribution() );
                    taxes.add( taxMap );
                }

                linha.put( "taxes", taxes );
                linhas.add( linha );
            }
            document.put( "lines", linhas );

            // totals
            Map<String, Object> totals = new LinkedHashMap<>();
            totals.put( "taxPayable", documentDTO.getDocumentTotals().getTaxPayable() );
            totals.put( "netTotal", documentDTO.getDocumentTotals().getNetTotal() );
            totals.put( "grossTotal", documentDTO.getDocumentTotals().getGrossTotal() );
            document.put( "documentTotals", totals );

            // retenções
            List<Map<String, Object>> retList = new ArrayList<>();
            List<WithholdingTaxDTO> withholdingTaxList = documentDTO.getWithholdingTaxList();

            if ( Objects.nonNull( withholdingTaxList ) )
            {
                for ( WithholdingTaxDTO w : withholdingTaxList )
                {
                    Map<String, Object> wMap = new LinkedHashMap<>();
                    wMap.put( "withholdingTaxType", w.getWithholdingTaxType() );
                    wMap.put( "withholdingTaxDescription", w.getWithholdingTaxDescription() );
                    wMap.put( "withholdingTaxAmount", w.getWithholdingTaxAmount() ); // valor correto
                    retList.add( wMap );

                }
                document.put( "withholdingTaxList", retList );

            }

            documents.add( document );
        }

        return documents;
    }

    public static Map getMapJwsSeriesSignature(
            String taxRegistrationNumber,
            String seriesYear,
            String documentType )
    {

        Map<String, Object> payload = new java.util.HashMap<>();
        payload.put( "taxRegistrationNumber", taxRegistrationNumber );
        payload.put( "seriesYear", seriesYear );
        payload.put( "documentType", documentType );
        payload.put( "establishmentNumber", "SEDE" );
        payload.put( "seriesContingencyIndicator", "N" );

        return payload;

    }
    public static Map getMapJwsListarFacturasSignature(
            String taxRegistrationNumber,
            String queryStartDate,
            String queryEndDate )
    {

        Map<String, Object> payload = new java.util.HashMap<>();
        payload.put( "taxRegistrationNumber", taxRegistrationNumber );
        payload.put( "queryStartDate", queryStartDate );
        payload.put( "querySEndDate", queryEndDate );

        return payload;

    }

    public static Map jwsDocumentSignature( String taxRegistrationNumber, DocumentDTO documentDTO )
    {

        Map<String, Object> documentTotals = new LinkedHashMap<>();

        documentTotals.put( "taxPayable", documentDTO.getDocumentTotals().getTaxPayable() );
        documentTotals.put( "netTotal", documentDTO.getDocumentTotals().getNetTotal() );
        documentTotals.put( "grossTotal", documentDTO.getDocumentTotals().getGrossTotal() );

        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put( "documentNo", documentDTO.getDocumentNo() );
        payload.put( "taxRegistrationNumber", taxRegistrationNumber );
        payload.put( "documentType", documentDTO.getDocumentType() );
        payload.put( "documentDate", documentDTO.getDocumentDate() );
        payload.put( "customerTaxID", documentDTO.getCustomerTaxID() );
        payload.put( "customerCountry", documentDTO.getCustomerCountry() );
        payload.put( "companyName", documentDTO.getCompanyName() );
        payload.put( "documentTotals", documentTotals );

        return payload;

    }

    public static void main( String[] args )
    {
        Map<String, Object> payload = PayloadFactory.criarPayloadListarFacturas( 
                "5000537039", 
                "2026", "FR",
                "2026-01-02",     "2026-01-26" );
//        Map<String, Object> payload = PayloadFactory.criarPayloadCriarSerie("5000413178", "2026" , "FR");
//        Map<String, Object> consultaPayloadFactura = PayloadFactory.consultaPayloadFactura( "5000413178", "202600000138171" );

        String json = JsonUtil.toJson( payload );

        System.out.println( json );
    }

}
