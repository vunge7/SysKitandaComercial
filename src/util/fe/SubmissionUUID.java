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
import java.util.UUID;

public final class SubmissionUUID
{

    private SubmissionUUID()
    {
        // evita instanciação
    }

    /**
     * Gera um identificador único por submissão Formato RFC 4122 UUID v4:
     * xxxxxxxx-xxxx-4xxx-[8|9|a|b]xxx-xxxxxxxxxxxx
     */
    public static String gerar()
    {
        UUID uuid = UUID.randomUUID();

        // Garantia adicional de conformidade (opcional)
        if ( uuid.version() != 4 || uuid.variant() != 2 )
        {
            throw new IllegalStateException( "UUID gerado não está conforme RFC 4122" );
        }

        return uuid.toString();
    }

    // Teste
    public static void main( String[] args )
    {
        String submissionUUID = gerar();
        System.out.println( "\"submissionUUID\": \"" + submissionUUID + "\"" );
    }
}
