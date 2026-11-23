package util.ui;

import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;

/**
 * Campo de impressora (Configurações): ao clicar abre a lista das impressoras
 * instaladas neste computador, para não se escrever o nome à mão (um erro no
 * nome e o talão da cozinha/bar não sai). O campo continua editável.
 * 2026-11-17.
 */
public final class EscolhaImpressora {

    private EscolhaImpressora() {
    }

    public static void ligar(JTextField... campos) {
        for (JTextField c : campos) {
            if (c == null) {
                continue;
            }
            c.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            c.setToolTipText("Clique para escolher uma impressora instalada neste computador");
            c.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    mostrar(c);
                }
            });
        }
    }

    static void mostrar(JTextField campo) {
        JPopupMenu m = new JPopupMenu();
        PrintService pred = PrintServiceLookup.lookupDefaultPrintService();
        PrintService[] todas = PrintServiceLookup.lookupPrintServices(null, null);
        if (todas.length == 0) {
            JMenuItem v = new JMenuItem("Nenhuma impressora instalada");
            v.setEnabled(false);
            m.add(v);
        }
        for (PrintService s : todas) {
            String nome = s.getName();
            boolean ePred = pred != null && pred.getName().equals(nome);
            boolean actual = nome.equalsIgnoreCase(campo.getText().trim());
            JMenuItem i = new JMenuItem((actual ? "✔ " : "    ") + nome + (ePred ? "   (predefinida)" : ""));
            i.addActionListener(e -> campo.setText(nome));
            m.add(i);
        }
        m.addSeparator();
        JMenuItem limpar = new JMenuItem("Usar a impressora predefinida do Windows");
        limpar.addActionListener(e -> campo.setText(""));
        m.add(limpar);
        m.show(campo, 0, campo.getHeight());
    }
}
