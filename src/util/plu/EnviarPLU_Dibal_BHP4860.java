/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util.plu;

/**
 *
 * @author Engº Domingos Dala Vunge
 * @created 8/dez/2025
 * @lastModified 8/dez/2025
 */
import java.io.OutputStream;
import java.net.Socket;

public class EnviarPLU_Dibal_BHP4860
{

    public static void main( String[] args )
    {
        String ip = "192.168.1.90";
        int porta = 3000;

        try ( Socket socket = new Socket( ip, porta ) )
        {

            OutputStream out = socket.getOutputStream();

            // Dados do PLU
            String plu = "0039";                   // Código PLU (4 dígitos)
            String descricao = "ARROZ 1KG       "; // 20 caracteres fixos
            String preco = "000085000";           // 850.00 Kz → 000085000
            String departamento = "01";           // 2 dígitos

            // Montagem do pacote DIBAL BH-P4860
            char STX = 0x02;
            char ETX = 0x03;

            String pacote
                    = STX
                    + "05"
                    + // Tipo de comando: 05 = gravar PLU
                    plu
                    + descricao
                    + preco
                    + departamento
                    + ETX;

            out.write( pacote.getBytes( "UTF-8" ) );
            out.flush();

            System.out.println( "PLU enviado com sucesso para a DIBAL BH-P4860!" );

        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }
}
