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
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class HttpPostClient
{

    public static void main( String[] args )
    {
        try
        {
            URL url = new URL( "http://localhost:8080/api/documentos" );
            HttpURLConnection conn = ( HttpURLConnection ) url.openConnection();

            conn.setRequestMethod( "POST" );
            conn.setRequestProperty( "Content-Type", "application/json" );
            conn.setDoOutput( true );

            String json = "{ \"nif\": \"5000413178\", \"valor\": 15000 }";

            try ( OutputStream os = conn.getOutputStream() )
            {
                os.write( json.getBytes( StandardCharsets.UTF_8 ) );
            }

            System.out.println( "Status: " + conn.getResponseCode() );

            conn.disconnect();

        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }
}
