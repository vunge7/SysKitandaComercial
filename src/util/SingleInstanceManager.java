/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Garante que apenas UMA instância do sistema seja executada por computador.
 * Permite múltiplos computadores na rede usarem o sistema sem interferência.
 *
 * Funcionalidades:
 * - Evita que o sistema abra duas vezes no mesmo PC.
 * - Fecha automaticamente a instância antiga caso o utilizador tente abrir outra.
 * - Nunca fica preso no Gestor de Tarefas (sem lock file).
 * - Usa socket local (localhost), não interfere com a rede.
 */
public class SingleInstanceManager {

    private static final int PORT = 44567; // Porta local de bloqueio
    private static ServerSocket serverSocket;

    /**
     * Deve ser chamado no início do programa (logo após o login).
     * @return true se o sistema já está aberto e deve encerrar esta nova instância.
     */
    public static boolean isInstanceRunning() {
        try {
            // Tenta abrir porta local
            serverSocket = new ServerSocket(PORT);

            // Criar thread para ouvir pedidos de nova instância
            listenForInstanceRequests();

            return false; // Primeira instância → pode abrir o sistema

        } catch (IOException ex) {
            // A porta já está em uso → existe outra instância
            notifyExistingInstanceToClose();
            return true;
        }
    }

    /**
     * Thread que fica à espera de um pedido da segunda instância para encerrar.
     */
    private static void listenForInstanceRequests() {
        new Thread(() -> {
            while (true) {
                try (Socket socket = serverSocket.accept()) {
                    // Pedido de nova instância → encerra a aplicação antiga
                    System.exit(0);
                } catch (Exception ignored) {}
            }
        }).start();
    }

    /**
     * A nova instância pede à instância antiga para fechar.
     */
    private static void notifyExistingInstanceToClose() {
        try {
            new Socket("127.0.0.1", PORT).close();
            Thread.sleep(1000); // espera a instância velha fechar
        } catch (Exception ignored) {}
    }
}
