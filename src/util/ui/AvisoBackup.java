package util.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dialog;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JWindow;

/**
 * Aviso de que a cópia de segurança falhou: fica no canto do ecrã até alguém o
 * fechar, para não passar despercebido, mas não tira o foco nem bloqueia o trabalho.
 */
public final class AvisoBackup {

    private static JWindow actual;

    private AvisoBackup() {
    }

    /** Texto para o utilizador; destinos são, por exemplo, "pasta do sistema" ou "unidade E:\". */
    public static String texto(List<String> destinos) {
        return "Não foi possível guardar em: " + String.join(", ", destinos) + ". "
                + "Os backups anteriores não foram apagados. Contacte o suporte.";
    }

    /** Chamar na thread da interface. */
    public static void mostrar(List<String> destinos, String ficheiroDetalhes) {
        if (actual != null) {
            actual.dispose();
        }
        JLabel titulo = new JLabel("Cópia de segurança não concluída");
        titulo.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(14f));
        titulo.setForeground(ComponentesWeb.VERMELHO);

        JLabel corpo = new JLabel("<html><body style='width:300px'>" + texto(destinos)
                + "<br><br>Detalhes: " + ficheiroDetalhes + "</body></html>");
        corpo.setFont(EstiloUI.FONTE.deriveFont(13f));
        corpo.setForeground(ComponentesWeb.TEXTO);

        final JWindow janela = new JWindow();
        JButton fechar = new JButton("Fechar");
        fechar.setFont(EstiloUI.FONTE.deriveFont(13f));
        fechar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        fechar.addActionListener(e -> {
            janela.dispose();
            if (actual == janela) {
                actual = null;
            }
        });
        JPanel rodape = new JPanel(new BorderLayout());
        rodape.setOpaque(false);
        rodape.add(fechar, BorderLayout.EAST);

        JPanel conteudo = new JPanel(new BorderLayout(0, 8));
        conteudo.setBackground(Color.WHITE);
        conteudo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 5, 1, 1, ComponentesWeb.VERMELHO),
                BorderFactory.createEmptyBorder(12, 14, 12, 14)));
        conteudo.add(titulo, BorderLayout.NORTH);
        conteudo.add(corpo, BorderLayout.CENTER);
        conteudo.add(rodape, BorderLayout.SOUTH);

        janela.setContentPane(conteudo);
        janela.pack();
        Rectangle area = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        janela.setLocation(area.x + area.width - janela.getWidth() - 16,
                area.y + area.height - janela.getHeight() - 16);
        janela.setFocusableWindowState(false);
        janela.setAlwaysOnTop(true);
        // o botão Fechar tem de responder mesmo com um diálogo modal aberto
        janela.setModalExclusionType(Dialog.ModalExclusionType.APPLICATION_EXCLUDE);
        janela.setVisible(true);
        actual = janela;
    }
}
