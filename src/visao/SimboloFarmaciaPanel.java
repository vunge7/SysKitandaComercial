/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package visao;

/**
 *
 * @author MARTINHO
 */

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Arc2D;
import java.awt.geom.Path2D;
import javax.swing.JPanel;

/** Desenha o símbolo da farmácia (taça de Higeia) em branco semitransparente. */
public class SimboloFarmaciaPanel extends JPanel {

    private final float opacidade;

    public SimboloFarmaciaPanel(float opacidade) {
        this.opacidade = opacidade;
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g2.setComposite(AlphaComposite.SrcOver.derive(opacidade));
        g2.setColor(java.awt.Color.WHITE);

        // Área do símbolo: 60% da altura, proporção 0.9:1, encostado à direita
        double altura = getHeight() * 0.60;
        double largura = altura * 0.9;
        double x0 = getWidth() - largura - 40;
        double y0 = (getHeight() - altura) / 2.0;

        g2.translate(x0, y0);
        g2.scale(largura / 100.0, altura / 111.0);   // desenho feito em 100 x 111

        // Taça
        Path2D taca = new Path2D.Double();
        taca.moveTo(0, 38);
        taca.curveTo(15, 42, 25, 52, 50, 52);
        taca.curveTo(75, 52, 85, 42, 100, 38);
        taca.curveTo(90, 36, 80, 36, 72, 36);
        taca.lineTo(28, 36);
        taca.curveTo(20, 36, 10, 36, 0, 38);
        taca.closePath();
        g2.fill(taca);

        // Haste e base
        g2.fillRect(46, 50, 8, 52);
        Path2D base = new Path2D.Double();
        base.moveTo(26, 111);
        base.curveTo(34, 102, 44, 101, 50, 101);
        base.curveTo(56, 101, 66, 102, 74, 111);
        base.closePath();
        g2.fill(base);

        // Serpente: corpo em S sobre a haste
        g2.setStroke(new BasicStroke(9f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        Path2D cobra = new Path2D.Double();
        cobra.moveTo(63, 8);
        cobra.curveTo(55, -2, 28, -2, 28, 16);
        cobra.curveTo(28, 30, 70, 30, 70, 48);
        cobra.curveTo(70, 66, 30, 62, 30, 78);
        cobra.curveTo(30, 94, 66, 90, 66, 100);
        g2.draw(cobra);

        // Cabeça da serpente
        g2.fill(new Arc2D.Double(58, 4, 14, 14, 0, 360, Arc2D.OPEN));

        g2.dispose();
    }
}