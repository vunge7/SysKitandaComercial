package util.ui;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RadialGradientPaint;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.awt.geom.Path2D;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

/**
 * Mensagem de sucesso com a imagem de OK (o círculo verde com o visto do
 * _ok.png do SysKitandaHotel, desenhado em grande para ficar nítido): título,
 * nº do documento, linhas de valores (total, recebido, troco) e o total por
 * extenso. Enter ou Esc fecham.
 *
 * <pre>
 * MensagemSucesso.mostrar(janela, "Factura efectuada com sucesso!", "FR 2026/3715",
 *         new String[][]{{"Total", "6.800,00 AOA"}, {"Troco", "200,00 AOA"}}, "Seis Mil, Oitocentos Kwanza(s)");
 * </pre>
 */
public final class MensagemSucesso {

    private static final Color VERDE = new Color(22, 163, 74);
    private static final Color VERDE_ESCURO = new Color(21, 128, 61);

    private MensagemSucesso() {
    }

    /** Só o título (sem documento nem valores). */
    public static void mostrar(Component pai, String titulo) {
        mostrar(pai, titulo, null, null, null);
    }

    /**
     * @param documento nº do documento (pode ser null)
     * @param linhas    pares {rótulo, valor}; o último sai em destaque (pode ser null)
     * @param extenso   valor por extenso (pode ser null)
     */
    public static void mostrar(Component pai, String titulo, String documento, String[][] linhas, String extenso) {
        Window dono = pai == null ? null : pai instanceof Window ? (Window) pai : SwingUtilities.getWindowAncestor(pai);
        final JDialog d = new JDialog(dono, "Sucesso", java.awt.Dialog.ModalityType.APPLICATION_MODAL);
        d.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel corpo = new JPanel(new BorderLayout(0, 14));
        corpo.setBackground(Color.WHITE);
        corpo.setBorder(BorderFactory.createEmptyBorder(24, 32, 20, 32));

        // ---- imagem de OK
        JComponent ok = new IconeOk();
        JPanel topo = ComponentesWeb.transparente(new FlowLayout(FlowLayout.CENTER, 0, 0));
        topo.add(ok);

        // ---- textos
        JPanel textos = ComponentesWeb.transparente(new GridLayout(0, 1, 0, 4));
        JLabel lbTitulo = new JLabel(titulo, SwingConstants.CENTER);
        lbTitulo.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(20f));
        lbTitulo.setForeground(VERDE_ESCURO);
        textos.add(lbTitulo);
        if (documento != null && !documento.trim().isEmpty()) {
            JLabel lbDoc = new JLabel(documento.trim(), SwingConstants.CENTER);
            lbDoc.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(14.5f));
            lbDoc.setForeground(ComponentesWeb.TEXTO);
            textos.add(lbDoc);
        }

        JPanel meio = ComponentesWeb.transparente(new BorderLayout(0, 12));
        meio.add(textos, BorderLayout.NORTH);

        // ---- valores (caixa suave) e extenso
        if (linhas != null && linhas.length > 0 || extenso != null && !extenso.trim().isEmpty()) {
            JPanel caixa = new ComponentesWeb.CaixaSuave(new BorderLayout(0, 8), new Color(240, 253, 244), new Color(187, 247, 208));
            caixa.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));
            if (linhas != null && linhas.length > 0) {
                JPanel grelha = ComponentesWeb.transparente(new GridLayout(0, 2, 16, 4));
                for (int i = 0; i < linhas.length; i++) {
                    boolean ultimo = i == linhas.length - 1 && linhas.length > 1;
                    JLabel r = new JLabel(linhas[i][0]);
                    r.setFont(EstiloUI.FONTE.deriveFont(13.5f));
                    r.setForeground(ComponentesWeb.TEXTO_SUAVE);
                    JLabel v = new JLabel(linhas[i][1], SwingConstants.RIGHT);
                    v.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(ultimo ? 17f : 14.5f));
                    v.setForeground(ultimo || i == 0 ? VERDE_ESCURO : ComponentesWeb.TEXTO); // total e último em verde escuro
                    grelha.add(r);
                    grelha.add(v);
                }
                caixa.add(grelha, BorderLayout.CENTER);
            }
            if (extenso != null && !extenso.trim().isEmpty()) {
                ComponentesWeb.TextoQuebrado lbExtenso = new ComponentesWeb.TextoQuebrado();
                lbExtenso.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(java.awt.Font.BOLD | java.awt.Font.ITALIC, 12.5f));
                lbExtenso.setForeground(new Color(220, 38, 38)); // vermelho, como nos visores
                lbExtenso.setText("São: " + extenso.trim());
                caixa.add(lbExtenso, BorderLayout.SOUTH);
            }
            meio.add(caixa, BorderLayout.CENTER);
        }

        // ---- OK
        JButton btOk = new JButton("OK");
        ComponentesWeb.botao(btOk, ComponentesWeb.Variante.SUCESSO, null, "Fechar (Enter)");
        btOk.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(15f));
        btOk.setBorder(BorderFactory.createEmptyBorder(10, 48, 10, 48));
        btOk.addActionListener(e -> d.dispose());
        JPanel baixo = ComponentesWeb.transparente(new FlowLayout(FlowLayout.CENTER, 0, 0));
        baixo.add(btOk);

        corpo.add(topo, BorderLayout.NORTH);
        corpo.add(meio, BorderLayout.CENTER);
        corpo.add(baixo, BorderLayout.SOUTH);

        d.setContentPane(corpo);
        d.getRootPane().setDefaultButton(btOk);
        d.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "fechar");
        d.getRootPane().getActionMap().put("fechar", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                d.dispose();
            }
        });
        d.pack();
        d.setSize(Math.max(420, d.getWidth()), d.getHeight());
        d.setResizable(false);
        d.setLocationRelativeTo(dono);
        SwingUtilities.invokeLater(btOk::requestFocusInWindow);
        d.setVisible(true);
    }

    /** Círculo verde com o visto (como o _ok.png), com um halo suave. */
    static final class IconeOk extends JComponent {

        IconeOk() {
            setPreferredSize(new Dimension(96, 96));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = ComponentesWeb.suave(g);
            int s = Math.min(getWidth(), getHeight());
            float cx = getWidth() / 2f, cy = getHeight() / 2f;
            // halo
            g2.setPaint(new RadialGradientPaint(cx, cy, s / 2f, new float[]{0.6f, 1f},
                    new Color[]{new Color(34, 197, 94, 70), new Color(34, 197, 94, 0)}));
            g2.fillOval(0, 0, s, s);
            // disco
            int d = (int) (s * 0.72);
            int x = (int) (cx - d / 2f), y = (int) (cy - d / 2f);
            g2.setColor(new Color(240, 253, 244));
            g2.fillOval(x, y, d, d);
            g2.setColor(VERDE);
            g2.setStroke(new BasicStroke(d * 0.07f));
            g2.drawOval(x + 2, y + 2, d - 4, d - 4);
            // visto
            Path2D v = new Path2D.Double();
            v.moveTo(x + d * 0.28, y + d * 0.52);
            v.lineTo(x + d * 0.44, y + d * 0.67);
            v.lineTo(x + d * 0.73, y + d * 0.36);
            g2.setStroke(new BasicStroke(d * 0.10f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.draw(v);
            g2.dispose();
        }
    }
}
