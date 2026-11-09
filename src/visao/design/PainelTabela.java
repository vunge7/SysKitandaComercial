package visao.design;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Area;
import java.awt.geom.RoundRectangle2D;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 * Moldura branca de cantos arredondados para tabelas: arredonda os cantos
 * do conteúdo (cabeçalho e linhas) e desenha a borda por cima.
 */
public class PainelTabela extends JPanel {

    private static final int RAIO = 12;

    private Color corMascara = Color.WHITE;

    public PainelTabela(JComponent conteudo) {
        super(new BorderLayout());
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        add(conteudo, BorderLayout.CENTER);
    }

    private RoundRectangle2D forma() {
        return new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, RAIO, RAIO);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = Formularios.suavizar(g);
        g2.setColor(Color.WHITE);
        g2.fill(forma());
        g2.dispose();
    }

    /**
     * Pinta os filhos normalmente e depois tapa os cantos com a cor de
     * fundo do cartão. (Recortar com uma forma arredondada faria o Java2D
     * mudar o antialiasing do texto nas células dos cantos.)
     */
    @Override
    protected void paintChildren(Graphics g) {
        super.paintChildren(g);
        Graphics2D g2 = Formularios.suavizar(g);
        Area cantos = new Area(new Rectangle(0, 0, getWidth(), getHeight()));
        cantos.subtract(new Area(forma()));
        g2.setColor(corMascara);
        g2.fill(cantos);
        g2.setColor(Tema.BORDA_CAMPO);
        g2.draw(forma());
        g2.dispose();
    }

    /** Cor da superfície onde a tabela está (por omissão, cartão branco). */
    public void setCorMascara(Color cor) {
        this.corMascara = cor;
    }
}
