package util.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JWindow;
import javax.swing.SwingConstants;

/**
 * Ecrã de arranque com a marca Kitanda: aparece logo ao abrir o sistema,
 * enquanto a base de dados e o JPA (EclipseLink, ~3 a 4 segundos) se preparam
 * numa thread própria. Antes, nada aparecia durante esse tempo e parecia que o
 * sistema não tinha aberto (e o utilizador clicava outra vez).
 */
public final class EcraArranque {

    private static JWindow janela;
    private static JLabel lbEstado;

    private EcraArranque() {
    }

    /** Mostra o ecrã (chamar na thread da interface). */
    public static void mostrar(String versao) {
        if (janela != null) {
            return;
        }
        JPanel fundo = new JPanel(new BorderLayout(0, 14)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = ComponentesWeb.suave(g);
                g2.setPaint(new GradientPaint(0, 0, MenuWeb.AZUL_NOITE, getWidth(), getHeight(), MenuWeb.AZUL_MARINHO));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(MenuWeb.DOURADO);
                g2.fillRect(0, getHeight() - 4, getWidth(), 4);
                g2.dispose();
            }
        };
        fundo.setBorder(BorderFactory.createEmptyBorder(28, 32, 22, 32));

        JPanel topo = ComponentesWeb.transparente(new FlowLayout(FlowLayout.CENTER, 18, 0));
        topo.add(new MenuWeb.Logotipo(88));
        topo.add(new MenuWeb.MarcaSoftware("KITANDA", versao == null ? "" : versao, 52f,
                new Color(250, 232, 190), MenuWeb.DOURADO, MenuWeb.DOURADO_CLARO, true));

        lbEstado = new JLabel("A iniciar o sistema…", SwingConstants.CENTER);
        lbEstado.setFont(EstiloUI.FONTE.deriveFont(13.5f));
        lbEstado.setForeground(new Color(214, 226, 243));

        JProgressBar barra = new JProgressBar();
        barra.setIndeterminate(true);
        barra.setPreferredSize(new Dimension(10, 6));
        barra.setBorderPainted(false);
        barra.setForeground(MenuWeb.DOURADO);
        barra.setBackground(new Color(255, 255, 255, 40));

        JPanel baixo = ComponentesWeb.transparente(new BorderLayout(0, 10));
        baixo.add(lbEstado, BorderLayout.NORTH);
        baixo.add(barra, BorderLayout.SOUTH);

        fundo.add(topo, BorderLayout.CENTER);
        fundo.add(baixo, BorderLayout.SOUTH);

        janela = new JWindow();
        janela.setIconImages(MenuWeb.iconesAplicacao());
        janela.setContentPane(fundo);
        janela.setSize(560, 250);
        janela.setLocationRelativeTo(null);
        janela.setAlwaysOnTop(true);
        janela.setVisible(true);
        // pinta já: a seguir a interface fica ocupada com o look and feel e o pedido de pintura esperaria
        fundo.paintImmediately(0, 0, janela.getWidth(), janela.getHeight());
    }

    /** Texto por baixo do logótipo (ex.: "A ligar à base de dados…"). Qualquer thread. */
    public static void estado(String texto) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            if (lbEstado != null) {
                lbEstado.setText(texto);
            }
        });
    }

    /** Fecha o ecrã (chamar na thread da interface). */
    public static void fechar() {
        if (janela != null) {
            janela.dispose();
            janela = null;
            lbEstado = null;
        }
    }
}
