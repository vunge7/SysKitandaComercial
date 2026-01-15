/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util.fe;

/**
 *
 * @author Engº Domingos Dala Vunge
 * @created 23/dez/2025
 * @lastModified 23/dez/2025
 */
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class JsonUtil
{

    private static final ObjectMapper mapper
            = new ObjectMapper().enable( SerializationFeature.INDENT_OUTPUT );

    public static String toJson( Object obj )
    {
        try
        {
            return mapper.writeValueAsString( obj );
        }
        catch ( Exception e )
        {
            throw new RuntimeException( "Erro ao converter para JSON", e );
        }
    }

    public static void print( String resposta )
    {
        try
        {
            String json = resposta;
            ObjectMapper mapper = new ObjectMapper();
            Object jsonObj = mapper.readValue( json, Object.class );

            String pretty = mapper
                    .writerWithDefaultPrettyPrinter()
                    .writeValueAsString( jsonObj );

            System.out.println( pretty );
        }
        catch ( Exception e )
        {
        }

    }
}
