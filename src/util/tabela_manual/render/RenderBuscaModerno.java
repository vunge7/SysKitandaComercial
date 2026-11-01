package util.tabela_manual.render;

import java.awt.Color;
import javax.swing.JTable;
import visao.design.RenderizadorModerno;
import visao.design.Tema;
import static util.Definicoes.PRODUTO;
import static util.Definicoes.PRODUTO_CRITICO;
import static util.Definicoes.SERVICOS;
import static util.Definicoes.STOCK_VAZIO;

/**
 * Versão moderna do {@link RenderBusca}: as mesmas regras de estado
 * (crítico, stock vazio, serviço, normal) com as cores de
 * {@link util.Definicoes}, mas em tom suave na linha e com uma barra de cor
 * forte à esquerda.
 * <p>
 * Espera a quantidade na coluna 3 e o estado crítico ("true"/"false") na
 * coluna 5, como as tabelas de busca de produtos.
 */
public class RenderBuscaModerno extends RenderizadorModerno {

    private static final int COLUNA_QTD = 3;
    private static final int COLUNA_CRITICO = 5;

    @Override
    protected Color corDaLinha(JTable tabela, int linha) {
        Color estado = corDoEstado(tabela, linha);
        return estado == null ? null : Tema.misturarComBranco(estado, 0.30f);
    }

    @Override
    protected Color corDoIndicador(JTable tabela, int linha) {
        Color estado = corDoEstado(tabela, linha);
        return estado == null ? null : Tema.escurecer(estado, 0.22f);
    }

    private static Color corDoEstado(JTable tabela, int linha) {
        if (tabela.getColumnCount() <= COLUNA_CRITICO) {
            return null;
        }
        Object critico = tabela.getValueAt(linha, COLUNA_CRITICO);
        Object qtd = tabela.getValueAt(linha, COLUNA_QTD);
        if (critico != null && "true".equals(critico.toString())) {
            return cor(PRODUTO_CRITICO);
        }
        String quantidade = qtd == null ? "" : qtd.toString();
        if ("0".equals(quantidade)) {
            return cor(STOCK_VAZIO);
        }
        if ("-".equals(quantidade)) {
            return cor(SERVICOS);
        }
        return cor(PRODUTO);
    }

    private static Color cor(int[] rgb) {
        return new Color(rgb[0], rgb[1], rgb[2]);
    }
}
