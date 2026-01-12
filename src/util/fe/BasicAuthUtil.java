/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util.fe;

/**
 *
 * @author Engº Domingos Dala Vunge
 * @created 22/dez/2025
 * @lastModified 22/dez/2025
 */
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Classe utilitária para gerar Authorization Basic (Base64)
 *
 * Exemplo equivalente ao JavaScript: btoa("cliente123:s3nh@F0rte!")
 *
 * @author Domingos
 */
public class BasicAuthUtil
{

//    public static String username = "ws.kitanda";
    public static String username = "ws.hml.kitanda";
    public static String password = "mfn27942026";
//    public static String password = "mfn538052025";
    
    
    




    /**
     * Gera a string Base64 para autenticação Basic
     *
     * @param username usuário
     * @param password senha
     * @return string Base64 (sem o prefixo "Basic ")
     */
    public static String gerarBase64( String username, String password )
    {
        String credentials = username + ":" + password;

        return Base64.getEncoder()
                .encodeToString( credentials.getBytes( StandardCharsets.UTF_8 ) );
    }

    /**
     * Gera o header Authorization completo
     *
     * @param username usuário
     * @param password senha
     * @return Authorization header (ex: "Basic xxx")
     */
    public static String gerarAuthorizationHeader( String username, String password )
    {
        return "Basic " + gerarBase64( username, password );
    }

    // Teste rápido
    public static void main( String[] args )
    {

        String base64 = gerarBase64( username, password );
        String header = gerarAuthorizationHeader( username, password );

        System.out.println( "Base64:" );
        System.out.println( base64 );

        System.out.println( "\nAuthorization Header:" );
        System.out.println( header );
    }
}
