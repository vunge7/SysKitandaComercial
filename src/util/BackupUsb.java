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
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.awt.BorderLayout;
import java.awt.Frame;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import util.ui.AvisoBackup;
import util.ui.EstiloUI;
import util.ui.MenuWeb;

public class BackupUsb {

    private static final String SENHA = "MinhaSenhaForte123"; // a mesma de sempre no comercial: os backups antigos continuam a abrir // 🔑 Trocar por senha segura
    private static final String ALGORITMO = "AES/CBC/PKCS5Padding";
    private static final long LIMITE_ESPERA_SAIDA_MS = 15 * 60 * 1000;

    private static Thread threadBackup;
    private static boolean ganchoSaidaInstalado;
    private static long limiteSaida;
    private static final String FICHEIRO_ERROS = "Before\\bd\\backup_erros.log";
    private static volatile List<String> ultimasFalhas = Collections.emptyList();

    public static void main(String[] args) {
        realizarBackup();
    }

    /**
     * Corre o backup sem ocupar a interface. Se já houver um a correr, não
     * começa outro. A aplicação não fecha a meio: ver {@link #aguardarFim()}.
     */
    public static synchronized void iniciarEmSegundoPlano() {
        if (backupEmCurso()) {
            return;
        }
        if (!ganchoSaidaInstalado) {
            // cobre todas as saídas: System.exit, fecho da janela, fim de sessão
            Runtime.getRuntime().addShutdownHook(new Thread(BackupUsb::aguardarFim, "BackupUsb-saida"));
            ganchoSaidaInstalado = true;
        }
        threadBackup = new Thread(BackupUsb::backupComAviso, "BackupUsb");
        threadBackup.setPriority(Thread.MIN_PRIORITY);
        threadBackup.start();
    }

    private static void backupComAviso() {
        List<String> falhas = realizarBackup();
        ultimasFalhas = falhas;
        if (!falhas.isEmpty()) {
            SwingUtilities.invokeLater(() -> AvisoBackup.mostrar(falhas, FICHEIRO_ERROS));
        }
    }

    public static synchronized boolean backupEmCurso() {
        return threadBackup != null && threadBackup.isAlive();
    }

    /**
     * Espera que o backup termine, com um limite para um mysqldump preso não
     * impedir a aplicação de fechar. Passado o limite, o backup anterior continua
     * intacto, porque só é apagado depois de o novo estar pronto.
     */
    private static void aguardarFim() {
        Thread backup;
        long limite;
        synchronized (BackupUsb.class) {
            backup = threadBackup;
            if (limiteSaida == 0) {
                limiteSaida = System.currentTimeMillis() + LIMITE_ESPERA_SAIDA_MS;
            }
            limite = limiteSaida;
        }
        if (backup == null) {
            return;
        }
        try {
            long restante = limite - System.currentTimeMillis();
            if (restante > 0) {
                backup.join(restante);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /** Fecha a aplicação; com um backup a correr, avisa e espera que termine. */
    public static void sairDaAplicacao() {
        if (!backupEmCurso()) {
            System.exit(0);
            return;
        }
        JLabel mensagem = new JLabel("A terminar a cópia de segurança. Aguarde, por favor...");
        mensagem.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(14f));
        JProgressBar barra = new JProgressBar();
        barra.setIndeterminate(true);

        JPanel conteudo = new JPanel(new BorderLayout(0, 12));
        conteudo.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));
        conteudo.add(mensagem, BorderLayout.CENTER);
        conteudo.add(barra, BorderLayout.SOUTH);

        JDialog aviso = new JDialog((Frame) null, "A fechar o sistema", true);
        aviso.setIconImages(MenuWeb.iconesAplicacao());
        aviso.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        aviso.setContentPane(conteudo);
        aviso.pack();
        aviso.setLocationRelativeTo(null);
        aviso.setAlwaysOnTop(true);

        Thread espera = new Thread(() -> {
            aguardarFim();
            String problema = null;
            if (backupEmCurso()) {
                problema = "A cópia de segurança não terminou a tempo e ficou incompleta. "
                        + "Os backups anteriores não foram apagados. Contacte o suporte.";
                registarErro("sistema fechado", "a cópia não terminou dentro do limite de espera");
            } else if (!ultimasFalhas.isEmpty()) {
                problema = AvisoBackup.texto(ultimasFalhas);
            }
            final String mensagemFinal = problema;
            SwingUtilities.invokeLater(() -> {
                aviso.dispose();
                // quem está a sair tem de ver o problema: o aviso do canto fecharia com o sistema
                if (mensagemFinal != null) {
                    JOptionPane painel = new JOptionPane(
                            "<html><body style='width:320px'>" + mensagemFinal + "</body></html>",
                            JOptionPane.WARNING_MESSAGE);
                    JDialog dialogo = painel.createDialog("Cópia de segurança não concluída");
                    dialogo.setIconImages(MenuWeb.iconesAplicacao());
                    dialogo.setAlwaysOnTop(true);
                    dialogo.setVisible(true);
                    dialogo.dispose();
                }
                System.exit(0);
            });
        }, "BackupUsb-espera");
        espera.setDaemon(true);
        espera.start();
        aviso.setVisible(true);
    }

    /** @return os destinos onde a cópia falhou (vazio se correu tudo bem). */
    public static List<String> realizarBackup() {
        List<String> falhas = new ArrayList<>();
        // 📌 Data e Hora
        String data = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String hora = new SimpleDateFormat("HH-mm-ss").format(new Date());

        // ===============================
        // 1️⃣ Backup nas pendrives (todas com pasta BK)
        // ===============================
        for (File root : File.listRoots()) {
            File pastaBK = new File(root, "BK");
            if (pastaBK.exists() && pastaBK.isDirectory()) {
                try {
                    gravarCopia(new File(pastaBK, data + "\\" + hora));
                } catch (Exception e) {
                    // uma pendrive com problemas não impede as outras cópias
                    e.printStackTrace();
                    String destino = "unidade " + root.getPath();
                    falhas.add(destino);
                    registarErro(destino, e.getMessage());
                }
            }
        }

        // ===============================
        // 2️⃣ Backup na pasta do projeto (mantém só o último)
        // ===============================
        try {
            File dirProjeto = new File(System.getProperty("user.dir") + "\\Before\\bd\\bk_automatico");
            File novo = new File(dirProjeto, data + "\\" + hora);
            gravarCopia(novo);
            // 🔹 Só agora: se a cópia nova falhar, a anterior continua disponível
            apagarAnteriores(dirProjeto, novo);
        } catch (Exception e) {
            e.printStackTrace();
            falhas.add("pasta do sistema");
            registarErro("pasta do sistema", e.getMessage());
        }
        return falhas;
    }

    /** Sem consola na instalação, é aqui que o suporte encontra o motivo da falha. */
    private static void registarErro(String destino, String motivo) {
        File ficheiro = new File(System.getProperty("user.dir"), FICHEIRO_ERROS);
        ficheiro.getParentFile().mkdirs();
        String agora = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        try (PrintWriter saida = new PrintWriter(new OutputStreamWriter(
                new FileOutputStream(ficheiro, true), StandardCharsets.UTF_8))) {
            saida.println(agora + " | " + destino + " | " + motivo);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Grava e encripta uma cópia na pasta indicada. Se existir backup.sql.enc, a
     * cópia está completa: o nome final só é dado no fim. Se falhar, não deixa nada.
     */
    private static void gravarCopia(File pasta) throws Exception {
        if (!pasta.isDirectory() && !pasta.mkdirs()) {
            throw new IOException("Não foi possível criar a pasta: " + pasta);
        }
        File sql = new File(pasta, "backup.sql");
        File temporario = new File(pasta, "backup.sql.enc.tmp");
        File encriptado = new File(pasta, "backup.sql.enc");
        try {
            executarBackup(sql.getAbsolutePath());
            // 🔐 Encripta o ficheiro
            encriptarArquivo(sql, temporario);
            if (!temporario.renameTo(encriptado)) {
                throw new IOException("Não foi possível concluir o ficheiro: " + encriptado);
            }
            System.out.println("🔒 Backup encriptado: " + encriptado.getAbsolutePath());
        } finally {
            sql.delete(); // remove original
            temporario.delete();
            if (!encriptado.exists()) {
                apagarDiretorio(pasta);
            }
        }
    }

    /** Apaga tudo em bk_automatico menos a cópia nova (bk_automatico\data\hora). */
    private static void apagarAnteriores(File dirProjeto, File novo) {
        File pastaDataNova = novo.getParentFile();
        File[] datas = dirProjeto.listFiles();
        if (datas == null) {
            return;
        }
        for (File data : datas) {
            if (!data.equals(pastaDataNova)) {
                apagarDiretorio(data);
                continue;
            }
            File[] horas = data.listFiles();
            if (horas != null) {
                for (File hora : horas) {
                    if (!hora.equals(novo)) {
                        apagarDiretorio(hora);
                    }
                }
            }
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
            // cópia consistente sem bloquear as tabelas (são InnoDB): o sistema continua a gravar
            "--single-transaction",
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
        if (retorno != 0) {
            System.out.println("❌ Erro ao criar backup em: " + arquivoBackup);
            throw new IOException("O mysqldump terminou com o código " + retorno + errosDoDump(new File(arquivoBackup)));
        }
        System.out.println("✅ Backup criado em: " + arquivoBackup);
    }

    /**
     * O erro do mysqldump fica misturado com o dump, no fim do ficheiro. Só as
     * linhas de erro seguem para o registo: o resto são dados dos clientes.
     */
    private static String errosDoDump(File dump) throws IOException {
        try (RandomAccessFile ficheiro = new RandomAccessFile(dump, "r")) {
            byte[] fim = new byte[(int) Math.min(ficheiro.length(), 2048)];
            ficheiro.seek(ficheiro.length() - fim.length);
            ficheiro.readFully(fim);
            StringBuilder erros = new StringBuilder();
            for (String linha : new String(fim, StandardCharsets.UTF_8).split("\\r?\\n")) {
                if (linha.startsWith("mysqldump: ")) {
                    erros.append(" - ").append(linha);
                }
            }
            return erros.toString();
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


