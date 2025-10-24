/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;


import java.sql.Connection;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.io.*;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BackupUsb {

    private static final String SENHA = "MinhaSenhaForte123"; // 🔑 Trocar por senha segura
    private static final String ALGORITMO = "AES/CBC/PKCS5Padding";

    public static void main(String[] args) {
        realizarBackup();
    }

    public static void realizarBackup() {
        try {
            // 📌 Data e Hora
            String data = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            String hora = new SimpleDateFormat("HH-mm-ss").format(new Date());

            // ===============================
            // 1️⃣ Backup nas pendrives (todas com pasta BK)
            // ===============================
            File[] roots = File.listRoots();
            for (File root : roots) {
                File pastaBK = new File(root, "BK");
                if (pastaBK.exists() && pastaBK.isDirectory()) {
                    File pastaHora = new File(pastaBK, data + "\\" + hora);
                    pastaHora.mkdirs();

                    String arquivoBackup = pastaHora.getAbsolutePath() + "\\backup.sql";
                    executarBackup(arquivoBackup);

                    // 🔐 Encripta o ficheiro
                    encriptarArquivo(new File(arquivoBackup), new File(arquivoBackup + ".enc"));
                    new File(arquivoBackup).delete(); // remove original
                }
            }

            // ===============================
            // 2️⃣ Backup na pasta do projeto (mantém só o último)
            // ===============================
            String pastaProjeto = System.getProperty("user.dir") + "\\Before\\bd\\bk_automatico";

            // 🔹 Limpa todos os backups anteriores
            File dirProjeto = new File(pastaProjeto);
            if (dirProjeto.exists()) {
                apagarDiretorio(dirProjeto);
            }

            // 🔹 Cria estrutura nova
            File pastaHoraProjeto = new File(pastaProjeto + "\\" + data + "\\" + hora);
            pastaHoraProjeto.mkdirs();

            String arquivoBackupProjeto = pastaHoraProjeto.getAbsolutePath() + "\\backup.sql";
            executarBackup(arquivoBackupProjeto);

            // 🔐 Encripta o ficheiro
            encriptarArquivo(new File(arquivoBackupProjeto), new File(arquivoBackupProjeto + ".enc"));
            new File(arquivoBackupProjeto).delete(); // remove original

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ===============================
    // Método que executa o mysqldump
    // ===============================
    private static void executarBackup(String arquivoBackup) throws IOException, InterruptedException {
        String mysqldump = "C:\\Program Files (x86)\\MySQL\\MySQL Server 5.1\\bin\\mysqldump.exe";

        String usuario = "root";
        String senha = "DoV90x?#";
        String banco = "kitanda_db";

        ProcessBuilder pb = new ProcessBuilder(
            mysqldump,
            "-u", usuario,
            "-p" + senha,
            "--routines",
            "--events",
            "--triggers",
            "--databases", banco
        );

        pb.redirectErrorStream(true);
        Process processo = pb.start();

        try (InputStream is = processo.getInputStream();
             FileOutputStream fos = new FileOutputStream(arquivoBackup)) {

            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }
            fos.flush();
        }

        int retorno = processo.waitFor();
        if (retorno == 0) {
            System.out.println("✅ Backup criado em: " + arquivoBackup);
        } else {
            System.out.println("❌ Erro ao criar backup em: " + arquivoBackup);
        }
    }

    // ===============================
    // 🔐 Métodos de Encriptação AES
    // ===============================
    private static void encriptarArquivo(File inputFile, File outputFile) throws Exception {
        // Gera chave AES a partir da senha
        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);

        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(SENHA.toCharArray(), salt, 65536, 128);
        SecretKeySpec chave = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");

        // Inicializa Cipher
        Cipher cipher = Cipher.getInstance(ALGORITMO);
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        IvParameterSpec ivspec = new IvParameterSpec(iv);
        cipher.init(Cipher.ENCRYPT_MODE, chave, ivspec);

        try (FileInputStream fis = new FileInputStream(inputFile);
             FileOutputStream fos = new FileOutputStream(outputFile)) {

            // Guarda salt e IV no início do ficheiro (necessários para desencriptar depois)
            fos.write(salt);
            fos.write(iv);

            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                byte[] output = cipher.update(buffer, 0, bytesRead);
                if (output != null) fos.write(output);
            }
            byte[] outputBytes = cipher.doFinal();
            if (outputBytes != null) fos.write(outputBytes);
        }

        System.out.println("🔒 Backup encriptado: " + outputFile.getAbsolutePath());
    }

    // ===============================
    // Método utilitário para apagar diretórios
    // ===============================
    private static void apagarDiretorio(File dir) {
        if (dir.isDirectory()) {
            for (File f : dir.listFiles()) {
                apagarDiretorio(f);
            }
        }
        dir.delete();
    }
}


