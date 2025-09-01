package util;

import javax.swing.*;
import java.io.File;

/**
 * Serviço completo de backup e restauração encriptada
 */
public class BackupService {

    private static final String SENHA_PADRAO = "MinhaSenhaForte123"; // senha de encriptação padrão

    // Caminho do projeto
    public static final String CAMINHO_PROJETO = System.getProperty("user.dir") + "\\Before\\bd\\bk_automatico";

    /**
     * Cria backup em pendrive e projeto
     */
    public void fazerBackup(String usuarioDB, String senhaDB, String banco) {
        try {
            String data = new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date());
            String hora = new java.text.SimpleDateFormat("HH-mm-ss").format(new java.util.Date());

            // 1️⃣ Backup nas pendrives
            File[] roots = File.listRoots();
            for (File root : roots) {
                File pastaBK = new File(root, "BK");
                if (pastaBK.exists() && pastaBK.isDirectory()) {
                    File pastaHora = new File(pastaBK, data + "\\" + hora);
                    pastaHora.mkdirs();
                    String caminhoBackup = pastaHora.getAbsolutePath() + "\\backup.sql";
                    BackupCryptoUtil.criarBackup(caminhoBackup, usuarioDB, senhaDB, banco, SENHA_PADRAO);
                }
            }

            // 2️⃣ Backup na pasta do projeto (mantém só o último)
            File dirProjeto = new File(CAMINHO_PROJETO);
            if (dirProjeto.exists()) apagarDiretorio(dirProjeto);

            File pastaHoraProjeto = new File(CAMINHO_PROJETO + "\\" + data + "\\" + hora);
            pastaHoraProjeto.mkdirs();
            String caminhoBackupProjeto = pastaHoraProjeto.getAbsolutePath() + "\\backup.sql";
            BackupCryptoUtil.criarBackup(caminhoBackupProjeto, usuarioDB, senhaDB, banco, SENHA_PADRAO);

            JOptionPane.showMessageDialog(null, "✅ Backup realizado com sucesso!");

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "❌ Erro ao criar backup:\n" + e.getMessage());
        }
    }

    /**
     * Restaura backup (pendrive ou projeto) pedindo senha
     */
    public void restaurarBackup() {
        try {
            String[] opcoes = {"Pendrive", "Projeto"};
            int escolha = JOptionPane.showOptionDialog(
                    null,
                    "Deseja restaurar o backup a partir de onde?",
                    "Restaurar Backup",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    opcoes,
                    opcoes[0]
            );

            File arquivoEnc = null;
            if (escolha == 0) { // Pendrive
                File pendrive = BackupPaths.getPendriveBK();
                if (pendrive == null) {
                    JOptionPane.showMessageDialog(null, "Nenhuma pendrive encontrada com pasta BK!");
                    return;
                }
                arquivoEnc = BackupCryptoUtil.getUltimoBackup(pendrive);
            } else { // Projeto
                arquivoEnc = BackupCryptoUtil.getUltimoBackup(new File(CAMINHO_PROJETO));
            }

            if (arquivoEnc == null || !arquivoEnc.exists()) {
                JOptionPane.showMessageDialog(null, "Arquivo de backup não encontrado!");
                return;
            }

            File restaurado = new File(arquivoEnc.getParentFile(), "backup_restaurado.sql");
            BackupCryptoUtil.desencriptarArquivo(arquivoEnc, restaurado);

            JOptionPane.showMessageDialog(null, "✅ Backup restaurado em:\n" + restaurado.getAbsolutePath());

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "❌ Erro ao restaurar backup:\n" + e.getMessage());
        }
    }

    /**
     * Limpa diretórios recursivamente
     */
    private void apagarDiretorio(File dir) {
        if (dir.isDirectory()) {
            for (File f : dir.listFiles()) {
                apagarDiretorio(f);
            }
        }
        dir.delete();
    }
}
