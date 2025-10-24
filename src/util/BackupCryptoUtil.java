package util;


import java.sql.Connection;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.swing.*;
import java.io.*;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BackupCryptoUtil {

    private static final String ALGORITMO = "AES/CBC/PKCS5Padding";

    /**
     * Cria backup do banco de dados (usando mysqldump) e encripta
     */
    public static void criarBackup(String caminhoBackup, String usuario, String senhaDB, String banco, String senhaCripto) throws Exception {
        // 1️⃣ Cria backup SQL
        File backupSQL = new File(caminhoBackup);
        backupSQL.getParentFile().mkdirs(); // garante diretórios

        executarDumpMySql(backupSQL, usuario, senhaDB, banco);

        // 2️⃣ Encripta backup
        File backupEnc = new File(caminhoBackup + ".enc");
        encriptarArquivo(backupSQL, backupEnc, senhaCripto);

        // 3️⃣ Remove arquivo SQL original
        backupSQL.delete();
    }

    /**
     * Encripta arquivo usando AES com senha
     */
    public static void encriptarArquivo(File inputFile, File outputFile, String senha) throws Exception {
        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);

        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);

        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(senha.toCharArray(), salt, 65536, 128);
        SecretKeySpec chave = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");

        Cipher cipher = Cipher.getInstance(ALGORITMO);
        cipher.init(Cipher.ENCRYPT_MODE, chave, new IvParameterSpec(iv));

        try (FileInputStream fis = new FileInputStream(inputFile);
             FileOutputStream fos = new FileOutputStream(outputFile)) {

            // grava salt e IV no início do arquivo
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

        System.out.println("Backup encriptado: " + outputFile.getAbsolutePath());
    }

    /**
     * Desencripta arquivo pedindo senha ao usuário
     */
    public static void desencriptarArquivo(File inputFile, File outputFile) throws Exception {
        String senha = JOptionPane.showInputDialog(null, "Digite a senha para restaurar o backup:");
        if (senha == null || senha.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Senha não informada. Operação cancelada!");
            return;
        }

        try (FileInputStream fis = new FileInputStream(inputFile)) {
            byte[] salt = new byte[16];
            fis.read(salt);
            byte[] iv = new byte[16];
            fis.read(iv);

            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(senha.toCharArray(), salt, 65536, 128);
            SecretKeySpec chave = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");

            Cipher cipher = Cipher.getInstance(ALGORITMO);
            cipher.init(Cipher.DECRYPT_MODE, chave, new IvParameterSpec(iv));

            try (CipherInputStream cis = new CipherInputStream(fis, cipher);
                 FileOutputStream fos = new FileOutputStream(outputFile)) {

                byte[] buffer = new byte[8192];
                int bytesRead;
                while ((bytesRead = cis.read(buffer)) != -1) {
                    fos.write(buffer, 0, bytesRead);
                }
            }
        }

        System.out.println("Backup restaurado: " + outputFile.getAbsolutePath());
    }

    /**
     * Executa mysqldump para criar backup SQL
     */
    private static void executarDumpMySql(File arquivoBackup, String usuario, String senhaDB, String banco) throws IOException, InterruptedException {
        String mysqldump = "C:\\Program Files\\MySQL\\MySQL Server 5.1\\bin\\mysqldump.exe";

        ProcessBuilder pb = new ProcessBuilder(
                mysqldump,
                "-u", usuario,
                "-p" + senhaDB,
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
        }

        int retorno = processo.waitFor();
        if (retorno == 0) {
            System.out.println("Backup SQL criado em: " + arquivoBackup.getAbsolutePath());
        } else {
            throw new IOException("Erro ao criar backup SQL. Código de retorno: " + retorno);
        }
    }

    /**
     * Busca recursiva do arquivo .enc mais recente
     */
    public static File getUltimoBackup(File pasta) {
        if (pasta == null || !pasta.exists()) return null;

        File[] arquivos = pasta.listFiles();
        if (arquivos == null || arquivos.length == 0) return null;

        File maisRecente = null;

        for (File f : arquivos) {
            if (f.isDirectory()) {
                File temp = getUltimoBackup(f);
                if (temp != null && (maisRecente == null || temp.lastModified() > maisRecente.lastModified())) {
                    maisRecente = temp;
                }
            } else if (f.isFile() && f.getName().endsWith(".enc")) {
                if (maisRecente == null || f.lastModified() > maisRecente.lastModified()) {
                    maisRecente = f;
                }
            }
        }
        return maisRecente;
    }
}
