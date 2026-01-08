/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util.fe.http;

/**
 *
 * @author Engº Domingos Dala Vunge
 * @created 23/dez/2025
 * @lastModified 23/dez/2025
 */
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpGetClient
{

    public static void main( String[] args )
    {
        try
        {
            URL url = new URL( "http://localhost:8080/status" );
            HttpURLConnection conn = ( HttpURLConnection ) url.openConnection();

            conn.setRequestMethod( "GET" );
            conn.setRequestProperty( "Accept", "application/json" );

            int status = conn.getResponseCode();
            System.out.println( "HTTP Status: " + status );

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader( conn.getInputStream() )
            );

            String linha;
            StringBuilder resposta = new StringBuilder();
            while ( ( linha = reader.readLine() ) != null )
            {
                resposta.append( linha );
            }

            reader.close();
            conn.disconnect();

            System.out.println( "Resposta:" );
            System.out.println( resposta );

        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }
}
