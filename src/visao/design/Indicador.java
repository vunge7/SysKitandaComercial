package visao.design;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Pílula de legenda/contagem: ponto de cor, descrição e valor
 * (ex.: "● Stock crítico 12").
 */
public class Indicador extends JPanel {

    /**
     * @param ponto painel cuja cor de fundo (getBackground) é a cor do ponto;
     * pode ser alterada depois pela lógica da janela
     * @param valor etiqueta com o número, actualizada pela lógica da janela
     */
    public Indicador(JPanel ponto, String descricao, JLabel valor) {
        super(new FlowLayout(FlowLayout.LEFT, 7, 0));
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 12));

        JLabel lbDescricao = new JLabel(descricao);
        lbDescricao.setFont(Tema.SUBTITULO_CARTAO);
        lbDescricao.setForeground(Tema.ROTULO);
        valor.setFont(Tema.OPCAO_FORTE);
        valor.setForeground(Tema.TEXTO);

        add(ponto);
        add(lbDescricao);
        add(valor);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = Formularios.suavizar(g);
        int h = getHeight();
        g2.setColor(Color.WHITE);
        g2.fillRoundRect(0, 0, getWidth() - 1, h - 1, h, h);
        g2.setColor(Tema.BORDA);
        g2.drawRoundRect(0, 0, getWidth() - 1, h - 1, h, h);
        g2.dispose();
    }

    /** Ponto redondo que usa a cor de fundo do próprio painel. */
    public static JPanel ponto() {
        JPanel ponto = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = Formularios.suavizar(g);
                int d = Math.min(getWidth(), getHeight()) - 1;
                int y = (getHeight() - d) / 2;
                g2.setColor(getBackground());
                g2.fillOval(0, y, d, d);
                g2.setColor(Tema.escurecer(getBackground(), 0.25f));
                g2.drawOval(0, y, d, d);
                g2.dispose();
            }
        };
        ponto.setOpaque(false);
        ponto.setPreferredSize(new Dimension(12, 12));
        return ponto;
    }
}
