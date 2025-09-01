package util;

import java.io.File;

public class BackupPaths {

    // Caminho base do projeto
    public static final String CAMINHO_PROJETO = System.getProperty("user.dir") + "\\Before\\bd\\bk_automatico\\";

    // Retorna a primeira pendrive encontrada com pasta BK
    public static File getPendriveBK() {
        File[] roots = File.listRoots();
        for (File root : roots) {
            File pastaBK = new File(root, "BK");
            if (pastaBK.exists() && pastaBK.isDirectory()) {
                return pastaBK;
            }
        }
        return null; // nenhuma pendrive encontrada
    }
}
