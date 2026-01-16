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
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DataUtil
{

    private static final DateTimeFormatter FORMATTER
            = DateTimeFormatter.ofPattern( "yyyy-MM-dd'T'HH:mm:ss'Z'" )
                    .withZone( ZoneOffset.UTC );

    public static String converter( Date data )
    {
        return FORMATTER.format( data.toInstant() );
    }

    public static void main( String[] args )
    {
        System.out.println( converter( new Date() ) );
    }
}
