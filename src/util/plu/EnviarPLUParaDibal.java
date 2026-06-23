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

public class EnviarPLUParaDibal {

    public static void main(String[] args) {
        String ipBalanca = "192.168.1.90"; 
        int porta = 3000;

        try (Socket socket = new Socket(ipBalanca, porta)) {
            OutputStream out = socket.getOutputStream();

            // --- EXEMPLO DE PLU EM FORMATO TEXTO (depende do modelo) ---
            String dados = 
                    "PLU:039\r\n" +
                    "DESCRICAO:ARROZ 1KG\r\n" +
                    "PRECO:850.00\r\n" +
                    "DEPT:01\r\n" +
                    "FIM\r\n";

            out.write(dados.getBytes("UTF-8"));
            out.flush();

            System.out.println("PLU enviado com sucesso!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
