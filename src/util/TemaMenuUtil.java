/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

/**
 *
 * @author MARTINHO
 */
import java.awt.Color;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFrame;
import visao.SimboloFarmaciaPanel;

public final class TemaMenuUtil {

    public static final Color VERDE_FUNDO = new Color(30, 77, 43);     // #1E4D2B
    public static final Color VERDE_PAINEL = new Color(140, 198, 63);  // #8CC63F

    private TemaMenuUtil() { }

    /**
     * Aplica o tema de farmácia ao menu. Se não for farmácia, não altera nada.
     *
     * @param janela        janela do menu
     * @param farmacia      true se o negócio for farmácia
     * @param fundo         painel de fundo que cobre a janela (ex.: fundo_principal)
     * @param painelModulos painel azul-claro dos módulos (pode ser null)
     * @param recursoImagem caminho do PNG no classpath
     */
    public static void aplicarTemaMenu(JFrame janela, boolean farmacia,
        final JComponent fundo, JComponent painelModulos) {
    if (!farmacia || fundo == null) {
        return;
    }

    janela.getContentPane().setBackground(VERDE_FUNDO);
    fundo.setOpaque(true);
    fundo.setBackground(VERDE_FUNDO);

    if (painelModulos != null) {
        painelModulos.setOpaque(true);
        painelModulos.setBackground(VERDE_PAINEL);
    }

    final SimboloFarmaciaPanel marca = new SimboloFarmaciaPanel(0.18f);
    marca.setBounds(0, 0, fundo.getWidth(), fundo.getHeight());
    fundo.add(marca);
    fundo.setComponentZOrder(marca, fundo.getComponentCount() - 1);

    fundo.addComponentListener(new ComponentAdapter() {
        @Override
        public void componentResized(ComponentEvent e) {
            marca.setBounds(0, 0, fundo.getWidth(), fundo.getHeight());
        }
    });

    fundo.revalidate();
    fundo.repaint();
}
}