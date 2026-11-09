package visao.design;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.RoundRectangle2D;
import javax.swing.Icon;

/**
 * Família única de ícones de linha, desenhados em vector (sem imagens): todos
 * com o mesmo tamanho, traço e cantos arredondados, como a lupa do
 * {@link CampoPesquisa} e a seta do {@link ComboModerno}.
 * <p>
 * A cor acompanha o componente: usa o {@code getForeground()} do botão
 * (branco nos botões preenchidos, a cor de acção nos de contorno) e fica
 * cinza quando o botão está desactivado. Assim os estados de hover, premido,
 * foco e desactivado ficam a cargo do {@link BotaoModerno}.
 */
public class IconeLinha implements Icon {

    /** Acções com ícone próprio. */
    public enum Tipo {
        ADICIONAR, REMOVER, PESQUISAR, PAGAMENTO, FINALIZAR, PENDURAR, PENDENTES,
        GAVETA, IMPORTAR, CLIENTE_NOVO, SAIR, CAIXA_ABRIR, CAIXA_FECHAR, TRANSFERIR
    }

    public static final int TAMANHO = 18;
    private static final float TRACO = 1.8f;

    private final Tipo tipo;
    private final int tamanho;

    public IconeLinha(Tipo tipo) {
        this(tipo, TAMANHO);
    }

    public IconeLinha(Tipo tipo, int tamanho) {
        this.tipo = tipo;
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
        Graphics2D g2 = Formularios.suavizar(g);
        g2.translate(x, y);
        float escala = tamanho / (float) TAMANHO;
        g2.scale(escala, escala);
        Color cor = c == null || c.isEnabled()
                ? (c != null ? c.getForeground() : Tema.ROTULO)
                : Tema.BORDA_CAMPO_HOVER;
        g2.setColor(cor);
        g2.setStroke(new BasicStroke(TRACO, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        desenhar(g2);
        g2.dispose();
    }

    private void desenhar(Graphics2D g) {
        switch (tipo) {
            case ADICIONAR:
                g.draw(new Ellipse2D.Float(2, 2, 14, 14));
                linha(g, 9, 5.5f, 9, 12.5f);
                linha(g, 5.5f, 9, 12.5f, 9);
                break;
            case REMOVER: {
                linha(g, 2.5f, 5, 15.5f, 5);
                Path2D pega = new Path2D.Float();
                pega.moveTo(6.5f, 5);
                pega.lineTo(7, 2.5f);
                pega.lineTo(11, 2.5f);
                pega.lineTo(11.5f, 5);
                g.draw(pega);
                Path2D corpo = new Path2D.Float();
                corpo.moveTo(4, 5);
                corpo.lineTo(5, 15.5f);
                corpo.lineTo(13, 15.5f);
                corpo.lineTo(14, 5);
                g.draw(corpo);
                linha(g, 7.5f, 8, 7.5f, 12.5f);
                linha(g, 10.5f, 8, 10.5f, 12.5f);
                break;
            }
            case PESQUISAR:
                g.draw(new Ellipse2D.Float(2.5f, 2.5f, 10, 10));
                linha(g, 11, 11, 15.5f, 15.5f);
                break;
            case PAGAMENTO:
                g.draw(new RoundRectangle2D.Float(1.5f, 3.5f, 15, 11, 3, 3));
                linha(g, 1.5f, 7, 16.5f, 7);
                linha(g, 4.5f, 11, 8, 11);
                break;
            case FINALIZAR: {
                Path2D folha = new Path2D.Float();
                folha.moveTo(10.5f, 1.5f);
                folha.lineTo(4, 1.5f);
                folha.lineTo(4, 16.5f);
                folha.lineTo(14, 16.5f);
                folha.lineTo(14, 5);
                folha.closePath();
                g.draw(folha);
                Path2D visto = new Path2D.Float();
                visto.moveTo(6.5f, 10.5f);
                visto.lineTo(8.5f, 12.5f);
                visto.lineTo(11.8f, 8.5f);
                g.draw(visto);
                break;
            }
            case PENDURAR:
                g.draw(new Ellipse2D.Float(2, 2, 14, 14));
                linha(g, 7.3f, 6.5f, 7.3f, 11.5f);
                linha(g, 10.7f, 6.5f, 10.7f, 11.5f);
                break;
            case PENDENTES:
                for (float yy : new float[]{4.5f, 9, 13.5f}) {
                    g.fill(new Ellipse2D.Float(2, yy - 1.2f, 2.4f, 2.4f));
                    linha(g, 6.5f, yy, 16, yy);
                }
                break;
            case GAVETA: {
                Path2D topo = new Path2D.Float();
                topo.moveTo(2, 8);
                topo.lineTo(4.5f, 3);
                topo.lineTo(13.5f, 3);
                topo.lineTo(16, 8);
                g.draw(topo);
                g.draw(new RoundRectangle2D.Float(2, 8, 14, 7.5f, 2.5f, 2.5f));
                linha(g, 7, 11.8f, 11, 11.8f);
                break;
            }
            case IMPORTAR: {
                linha(g, 9, 2, 9, 10.5f);
                Path2D seta = new Path2D.Float();
                seta.moveTo(5.5f, 7);
                seta.lineTo(9, 10.5f);
                seta.lineTo(12.5f, 7);
                g.draw(seta);
                Path2D tabuleiro = new Path2D.Float();
                tabuleiro.moveTo(2, 11);
                tabuleiro.lineTo(2, 15.5f);
                tabuleiro.lineTo(16, 15.5f);
                tabuleiro.lineTo(16, 11);
                g.draw(tabuleiro);
                break;
            }
            case CLIENTE_NOVO:
                g.draw(new Ellipse2D.Float(3.5f, 2, 6, 6));
                g.draw(new Arc2D.Float(1, 10, 11, 10, 0, 180, Arc2D.OPEN));
                linha(g, 14.5f, 6, 14.5f, 12);
                linha(g, 11.5f, 9, 17.5f, 9);
                break;
            case SAIR: {
                Path2D porta = new Path2D.Float();
                porta.moveTo(9.5f, 2.5f);
                porta.lineTo(3, 2.5f);
                porta.lineTo(3, 15.5f);
                porta.lineTo(9.5f, 15.5f);
                g.draw(porta);
                linha(g, 7.5f, 9, 16, 9);
                Path2D seta = new Path2D.Float();
                seta.moveTo(12.5f, 5.5f);
                seta.lineTo(16, 9);
                seta.lineTo(12.5f, 12.5f);
                g.draw(seta);
                break;
            }
            case CAIXA_ABRIR:
                cadeado(g, true);
                break;
            case CAIXA_FECHAR:
                cadeado(g, false);
                break;
            case TRANSFERIR: {
                // seta para a direita em cima, para a esquerda em baixo (entre armazéns)
                linha(g, 2.5f, 6, 15, 6);
                Path2D direita = new Path2D.Float();
                direita.moveTo(12, 3);
                direita.lineTo(15, 6);
                direita.lineTo(12, 9);
                g.draw(direita);
                linha(g, 3, 12, 15.5f, 12);
                Path2D esquerda = new Path2D.Float();
                esquerda.moveTo(6, 9);
                esquerda.lineTo(3, 12);
                esquerda.lineTo(6, 15);
                g.draw(esquerda);
                break;
            }
            default:
                break;
        }
    }

    private static void cadeado(Graphics2D g, boolean aberto) {
        g.draw(new RoundRectangle2D.Float(3, 8, 12, 8.5f, 3, 3));
        g.draw(new Arc2D.Float(5.5f, 2, 7, 7, 0, 180, Arc2D.OPEN));
        linha(g, 5.5f, 5.5f, 5.5f, 8);
        if (!aberto) {
            linha(g, 12.5f, 5.5f, 12.5f, 8);
        }
        linha(g, 9, 11.2f, 9, 13.2f);
    }

    private static void linha(Graphics2D g, float x1, float y1, float x2, float y2) {
        g.draw(new Line2D.Float(x1, y1, x2, y2));
    }
}
