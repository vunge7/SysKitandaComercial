package util;



import java.sql.Connection;
import javax.swing.*;
import java.io.File;

public class RestaurarBackupBotao {

    // Método chamado pelo botão
    public void restaurarBackup() {
        try {
            File pendrive = BackupPaths.getPendriveBK(); // retorna F:\BK
            File arquivoEnc = getUltimoBackup(pendrive); // busca o .enc mais recente

            if (arquivoEnc == null) {
                JOptionPane.showMessageDialog(null, "Nenhum backup encontrado!");
                return;
            }

            File restaurado = new File(arquivoEnc.getParentFile(), "backup_restaurado.sql");
            BackupCryptoUtil.desencriptarArquivo(arquivoEnc, restaurado);

            JOptionPane.showMessageDialog(null, "Backup restaurado com sucesso!\n" + restaurado.getAbsolutePath());

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao restaurar backup:\n" + ex.getMessage());
        }
    }

    // -------------------------------
    // Utilitário: busca recursiva do .enc mais recente
    // -------------------------------
    private static File getUltimoBackup(File pasta) {
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
