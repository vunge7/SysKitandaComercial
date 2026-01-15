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
import util.fe.DataUtil;
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
        Map<String, Object> softwareInfo = new HashMap<>();
        softwareInfo.put( "softwareInfoDetail", softwareInfoDetail );
        softwareInfo.put( "jwsSoftwareSignature", jwsSoftwareSignature );

        // payload principal
        Map<String, Object> payload = new HashMap<>();
        payload.put( "schemaVersion", "1.2" );
        payload.put( "submissionUUID", SubmissionUUID.gerar() );
        payload.put( "taxRegistrationNumber", taxRegistrationNumber );
        payload.put( "submissionTimeStamp", DataUtil.converter( new Date() ) );
        payload.put( "softwareInfo", softwareInfo );
        payload.put( "seriesYear", seriesYear );
        payload.put( "documentType", documentType );
        payload.put( "establishmentNumber", "10" );
        Map<String, Object> HashJwsSignature = getMapJwsSeriesSignature( taxRegistrationNumber, seriesYear, documentType );
        String jwsSignature = JwsGenerator.gerarJws( "Chaves/ChavePrivada_2048_PKCS8.pem", HashJwsSignature );
        payload.put( "jwsSignature", jwsSignature );
        payload.put( "seriesContingencyIndicator", "N" );

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
        payload.put( "documents", getDocumentos( documentDTOs ) );

        return payload;
    }

    private static List<Map<String, Object>> getDocumentos( List<DocumentDTO> documentDTOs )
    {
        List<Map<String, Object>> documents = new ArrayList<>();

        for ( DocumentDTO documentDTO : documentDTOs )
        {
            Map<String, Object> document = new LinkedHashMap<>();

            document.put( "documentNo", documentDTO.getDocumentNo() );
            document.put( "documentStatus", documentDTO.getDocumentStatus() );
            document.put( "jwsDocumentSignature", documentDTO.getJwsDocumentSignature() );
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
        payload.put( "establishmentNumber", "10" );
        payload.put( "seriesContingencyIndicator", "N" );

        return payload;

    }

}
