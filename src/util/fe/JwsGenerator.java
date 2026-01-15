package util.fe;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Utilitário para geração de JWS (RS256)
 *
 * - Java 8 puro - Sem Maven - Compatível com Facturação Electrónica / AGT
 *
 * @author Engº Domingos
 */
public final class JwsGenerator
{

    private JwsGenerator()
    {
        // evita instanciação
    }

    /**
     * Gera um JWS RS256 a partir de um Map de claims
     *
     * @param caminhoPrivateKeyPem caminho do ficheiro PEM (PKCS#8)
     * @param claims payload completo a ser assinado (inclui hash, UUID, etc.)
     * @return JWS assinado (Compact Serialization)
     */
    public static String gerarJws(
            String caminhoPrivateKeyPem,
            Map<String, Object> claims
    )
    {
        if ( claims == null || claims.isEmpty() )
        {
            throw new IllegalArgumentException(
                    "O payload (claims) não pode ser nulo ou vazio"
            );
        }

        PrivateKey privateKey = carregarPrivateKeyDoFicheiro( caminhoPrivateKeyPem );

        return Jwts.builder()
                .setClaims( claims )
                .signWith( privateKey, SignatureAlgorithm.RS256 )
                .compact();
    }

    /**
     * Lê o ficheiro PEM e converte para PrivateKey
     */
    private static PrivateKey carregarPrivateKeyDoFicheiro( String caminho )
    {
        try
        {
            String pem = new String(
                    Files.readAllBytes( Paths.get( caminho ) ),
                    StandardCharsets.UTF_8
            );

            return converterPemParaPrivateKey( pem );
        }
        catch ( IOException e )
        {
            throw new RuntimeException(
                    "Não foi possível ler o ficheiro da chave privada",
                    e
            );
        }
    }

    /**
     * Converte texto PEM (PKCS#8) em PrivateKey RSA
     */
    private static PrivateKey converterPemParaPrivateKey( String pem )
    {
        try
        {
            if ( !pem.contains( "BEGIN PRIVATE KEY" ) )
            {
                throw new IllegalArgumentException(
                        "Chave inválida. Use uma chave PKCS#8 (BEGIN PRIVATE KEY)"
                );
            }

            String conteudo = pem
                    .replace( "-----BEGIN PRIVATE KEY-----", "" )
                    .replace( "-----END PRIVATE KEY-----", "" )
                    .replaceAll( "\\s+", "" );

            byte[] decoded = Base64.getDecoder().decode( conteudo );

            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec( decoded );
            KeyFactory keyFactory = KeyFactory.getInstance( "RSA" );

            return keyFactory.generatePrivate( spec );
        }
        catch ( Exception e )
        {
            throw new RuntimeException(
                    "Erro ao converter chave privada PEM",
                    e
            );
        }
    }

    public static Map softwareInfoDetail()
    {

        Map<String, Object> payload = new java.util.HashMap<>();

        payload.put( "productId", FEConfig.getSofwareName() );
        payload.put( "productVersion", FEConfig.getProductionVersion() );
        payload.put( "softwareValidationNumber", FEConfig.getSoftwareValidationNumber() );

        return payload;

    }

    public static Map jwsConsutlarFactura()
    {

        Map<String, Object> payload = new java.util.HashMap<>();

        payload.put( "taxRegistrationNumber", "5000413178" );
        payload.put( "documentNo", "FT FT7825S1917N/1" );

        return payload;

    }

    public static Map jwsSeriesSignature()
    {

        Map<String, Object> payload = new java.util.HashMap<>();

        payload.put( "taxRegistrationNumber", "5000413178" );
        payload.put( "seriesYear", "2026" );
        payload.put( "documentType", "AF" );
        payload.put( "establishmentNumber", "10" );
        payload.put( "seriesContingencyIndicator", "N" );

        return payload;

    }

    public static Map jwsDocumentSignature()
    {

        Map<String, Object> documentTotals = new LinkedHashMap<>();

        documentTotals.put( "taxPayable", 70 );
        documentTotals.put( "netTotal", 500 );
        documentTotals.put( "grossTotal", 570 );

        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put( "documentNo", "RG RG7825S17N/2" );
        payload.put( "taxRegistrationNumber", "5000413178" );
        payload.put( "documentType", "RG" );
        payload.put( "documentDate", "2025-12-30" );
        payload.put( "customerTaxID", "PT987654321" );
        payload.put( "customerCountry", "PT" );
        payload.put( "companyName", "Cliente Exemplo Lda" );
        payload.put( "documentTotals", documentTotals );

        return payload;

    }

    /**
     * Teste rápido
     */
    public static void main( String[] args )
    {
        Map<String, Object> payload = softwareInfoDetail();
//        Map<String, Object> payload = jwsConsutlarFactura();
//        Map<String, Object> payload = jwsSeriesSignature();
//        Map<String, Object> payload = jwsDocumentSignature();

        String jws = gerarJws(
                "Chaves/ChavePrivada_2048_PKCS8.pem",
                payload
        );

        System.out.println( "JWS gerado:" );
        System.out.println( jws );
    }
}
