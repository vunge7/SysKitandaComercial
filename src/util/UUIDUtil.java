/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;


import java.sql.Connection;
/**
 *
 * @author Engº Domingos Dala Vunge
 * @created 9/set/2025
 * @lastModified 9/set/2025
 */
import java.util.UUID;

public class UUIDUtil
{

    /**
     * Gera um UUID v4 único para submissão de faturas
     *
     * @return String UUID v4 no formato padrão
     * (xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx)
     */
    public static String generateUUIDv4()
    {
        return UUID.randomUUID().toString();
    }

    // Exemplo de uso
    public static void main( String[] args )
    {
        String submissionGUID = UUIDUtil.generateUUIDv4();
        System.out.println( "UUIDv4 gerado: " + submissionGUID );
    }
}
