/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;


import java.sql.Connection;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BackupUtil {

    public static void fazerBackupAgora() {
        try {
            // Data (pasta do dia)
            String dataPasta = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

            // Hora (pasta da hora)
            String horaPasta = new SimpleDateFormat("HH-mm-ss").format(new Date());

            // Data + hora (para o nome do arquivo)
            String dataArquivo = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

            // Caminho da pasta final com subpastas de data e hora
            String pastaBackup = System.getProperty("user.dir") + "\\Before\\bd\\bk_automatico\\" + dataPasta + "\\" + horaPasta;

            // garante que a subpasta existe
            new File(pastaBackup).mkdirs();

            // Caminho do arquivo de backup
            String caminhoBackup = pastaBackup + "\\_database_backup_" + dataArquivo + ".sql";

            // Comando mysqldump
            String rodar_comando = "cmd /c mysqldump --single-transaction -uroot -pDoV90x?# "
                    + "--dump-date --triggers --add-drop-database --routines "
                    + "--skip-quote-names --skip-set-charset --add-locks --disable-keys "
                    + "--databases kitanda_db > \"" + caminhoBackup + "\"";

            // Executa o comando
            Process rodarComandoWindows = rodarComandoWindows(rodar_comando, true);

            System.out.println("✅ Backup realizado com sucesso em: " + caminhoBackup);

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("❌ Erro ao realizar backup: " + e.getMessage());
        }
    }

    private static Process rodarComandoWindows(String comando, boolean esperar) throws Exception {
        Process processo = Runtime.getRuntime().exec(comando);
        if (esperar) {
            processo.waitFor();
        }
        return processo;
    }
}

//}

