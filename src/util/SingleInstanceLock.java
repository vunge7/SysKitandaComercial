/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;


import java.sql.Connection;
import java.io.File;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

public class SingleInstanceLock {

    private static FileLock lock;
    private static FileChannel channel;
    private static File file;

    public static boolean isAlreadyRunning() {
        try {
            file = new File("app.lock");
            channel = new RandomAccessFile(file, "rw").getChannel();

            lock = channel.tryLock();
            if (lock == null) {
                channel.close();
                return true; // Já está rodando
            }

            // Quando fechar, libera o lock e apaga o arquivo
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    lock.release();
                    channel.close();
                    file.delete();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }));

            return false; // Rodando pela primeira vez
        } catch (Exception e) {
            return true;
        }
    }
}

