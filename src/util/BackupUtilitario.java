/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;


import java.sql.Connection;
import java.io.File;

public class BackupUtilitario {

    public static File encontrarBackupMaisRecente(File pasta) {
        File[] arquivos = pasta.listFiles();
        if (arquivos == null || arquivos.length == 0) return null;

        File maisRecente = null;

        for (File f : arquivos) {
            if (f.isDirectory()) {
                File subMaisRecente = encontrarBackupMaisRecente(f);
                if (subMaisRecente != null) {
                    if (maisRecente == null || subMaisRecente.lastModified() > maisRecente.lastModified()) {
                        maisRecente = subMaisRecente;
                    }
                }
            } else if (f.getName().endsWith(".enc")) {
                if (maisRecente == null || f.lastModified() > maisRecente.lastModified()) {
                    maisRecente = f;
                }
            }
        }
        return maisRecente;
    }
}

