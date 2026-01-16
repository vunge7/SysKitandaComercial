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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import util.fe.DataUtil;
import util.fe.JwsGenerator;
import util.fe.SubmissionUUID;

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
