package util.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.RoundRectangle2D;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

/**
 * Confirmação de que o documento ficou bem: polegar verde, cabeçalho a verde e
 * o texto. Chamar só com o sucesso confirmado (AGT respondeu "V" ou commit feito).
 */
public final class ConfirmacaoPositiva {

    private static final Color VERDE = new Color(22, 163, 74);
    private static final Color VERDE_TEXTO = new Color(21, 128, 61);
    private static final Color VERDE_CLARO = new Color(220, 252, 231);

    private ConfirmacaoPositiva() {
    }

    public static void mostrar(String titulo, String cabecalho, String mensagem) {
        String html = "<html><body style='width:280px'>"
                + "<div style='font-size:13px;font-weight:bold;color:" + hex(VERDE_TEXTO) + "'>" + escapar(cabecalho) + "</div>"
                + "<div style='margin-top:6px'>" + escapar(mensagem) + "</div>"
                + "</body></html>";
        JOptionPane painel = new JOptionPane(html, JOptionPane.INFORMATION_MESSAGE,
                JOptionPane.DEFAULT_OPTION, new PolegarVerde(64));
        JDialog dialogo = painel.createDialog(titulo);
        dialogo.setIconImages(MenuWeb.iconesAplicacao());
        dialogo.setAlwaysOnTop(true);
        dialogo.setVisible(true);
        dialogo.dispose();
    }

    private static String hex(Color c) {
        return String.format("#%02x%02x%02x", c.getRed(), c.getGreen(), c.getBlue());
    }

    /** O número do documento pode ter "/" e outros caracteres; o HTML não pode ser partido. */
    private static String escapar(String s) {
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }

    /** Desenhado em vector para ficar nítido em qualquer escala do Windows. */
    private static final class PolegarVerde implements Icon {

        private final int tamanho;

        PolegarVerde(int tamanho) {
            this.tamanho = tamanho;
        }

        @Override
        public int getIconWidth() {
            return tamanho;
        }

        @Override
        public int getIconHeight() {
            return tamanho;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = ComponentesWeb.suave(g);
            g2.translate(x, y);
            g2.setColor(VERDE_CLARO);
            g2.fill(new Ellipse2D.Double(0, 0, tamanho, tamanho));

            // grelha de 24 unidades, centrada no círculo
            double escala = tamanho * 0.62 / 24;
            g2.translate(tamanho * 0.19, tamanho * 0.17);
            g2.scale(escala, escala);
            g2.setColor(VERDE);
            g2.fill(new RoundRectangle2D.Double(1.5, 10, 4.5, 11.5, 1.6, 1.6));

            Path2D.Double mao = new Path2D.Double();
            mao.moveTo(8, 10.5);
            mao.lineTo(12, 3);
            mao.curveTo(12.6, 1.9, 15.6, 2, 15.6, 4.6);
            mao.lineTo(14.8, 9);
            mao.lineTo(20, 9);
            mao.curveTo(21.6, 9, 22.4, 10.4, 22, 11.8);
            mao.lineTo(19.8, 20);
            mao.curveTo(19.5, 21, 18.7, 21.5, 17.8, 21.5);
            mao.lineTo(9.5, 21.5);
            mao.curveTo(8.6, 21.5, 8, 20.9, 8, 20);
            mao.closePath();
            g2.fill(mao);
            g2.dispose();
        }
    }
}
