package visao.design;

import java.awt.Color;
import java.awt.Font;

/**
 * Paleta de cores, fontes e medidas partilhadas pelas janelas com o novo
 * design.
 * <p>
 * A cor do negócio (ciano genérico, verde nas farmácias...) é definida uma
 * vez com {@link #definirCorNegocio(Color)}; os campos calculam a partir
 * dela o tom suave de fundo e o anel de foco.
 */
public final class Tema {

    // Superfícies
    public static final Color FUNDO = new Color(241, 245, 249);
    public static final Color FUNDO_FIM = new Color(232, 238, 246);
    public static final Color CAIXA = new Color(248, 250, 252);
    public static final Color BORDA = new Color(226, 232, 240);
    public static final Color DIVISOR = new Color(241, 245, 249);
    public static final Color SOMBRA = new Color(15, 23, 42);

    // Cabeçalho
    public static final Color CABECALHO_INICIO = new Color(15, 23, 72);
    public static final Color CABECALHO_FIM = new Color(29, 78, 216);
    public static final Color CABECALHO_SUBTITULO = new Color(191, 219, 254);

    // Cores de acção
    public static final Color PRIMARIA = new Color(37, 99, 235);
    public static final Color SUCESSO = new Color(22, 163, 74);
    public static final Color SUCESSO_TEXTO = new Color(21, 128, 61);
    public static final Color SUCESSO_FUNDO = new Color(240, 253, 244);
    public static final Color SUCESSO_BORDA = new Color(187, 247, 208);
    public static final Color AVISO = new Color(217, 119, 6);
    public static final Color PERIGO = new Color(220, 38, 38);
    public static final Color NEUTRA = new Color(71, 85, 105);
    public static final Color INFO = new Color(8, 145, 178);

    // Texto
    public static final Color TEXTO = new Color(15, 23, 42);
    public static final Color ROTULO = new Color(71, 85, 105);
    public static final Color TEXTO_SUAVE = new Color(100, 116, 139);

    // Campos
    public static final Color BORDA_CAMPO = new Color(203, 213, 225);
    public static final Color BORDA_CAMPO_HOVER = new Color(148, 163, 184);
    public static final Color CAMPO_INACTIVO = new Color(241, 245, 249);

    // Foto
    public static final Color MOLDURA_FOTO = new Color(30, 41, 59);
    public static final Color MOLDURA_FOTO_TEXTO = new Color(148, 163, 184);

    public static final Font TITULO_JANELA = new Font("Segoe UI", Font.BOLD, 21);
    public static final Font SUBTITULO_JANELA = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font TITULO_CARTAO = new Font("Segoe UI", Font.BOLD, 15);
    public static final Font SUBTITULO_CARTAO = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font TITULO_CAIXA = new Font("Segoe UI", Font.BOLD, 11);
    public static final Font ROTULO_FONTE = new Font("Segoe UI", Font.BOLD, 12);
    public static final Font NOTA = new Font("Segoe UI", Font.ITALIC, 11);
    public static final Font CAMPO = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font VALOR = new Font("Segoe UI", Font.BOLD, 16);
    public static final Font DESTAQUE = new Font("Segoe UI", Font.BOLD, 24);
    public static final Font COMBO = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font OPCAO = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font OPCAO_FORTE = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font BOTAO = new Font("Segoe UI", Font.BOLD, 13);

    public static final int ALTURA_CAMPO = 36;
    public static final int ESPACO = 14;

    private static Color corNegocio = new Color(0, 255, 255);

    private Tema() {
    }

    /**
     * Define a cor do negócio usada nos campos (fundo suave e foco). Deve ser
     * chamada ao abrir a janela, com a cor de
     * {@code util.TemaNegocioUtil.corDoNegocio(...)}.
     */
    public static void definirCorNegocio(Color cor) {
        if (cor != null) {
            corNegocio = cor;
        }
    }

    public static Color corNegocio() {
        return corNegocio;
    }

    /** Fundo dos campos editáveis: tom muito claro da cor do negócio. */
    public static Color fundoCampo() {
        return misturarComBranco(corNegocio, 0.12f);
    }

    /** Borda do campo em foco: cor do negócio ligeiramente mais escura. */
    public static Color bordaFoco() {
        return escurecer(corNegocio, 0.22f);
    }

    /** Brilho difuso à volta do campo em foco. */
    public static Color brilhoFoco() {
        return comAlfa(corNegocio, 80);
    }

    /** Fundo do texto seleccionado dentro dos campos. */
    public static Color selecaoTexto() {
        return misturarComBranco(corNegocio, 0.40f);
    }

    /** Fundo do item seleccionado nas listas dos combos. */
    public static Color selecaoLista() {
        return misturarComBranco(PRIMARIA, 0.12f);
    }

    public static Color misturarComBranco(Color cor, float intensidade) {
        return new Color(
                Math.round(255 + (cor.getRed() - 255) * intensidade),
                Math.round(255 + (cor.getGreen() - 255) * intensidade),
                Math.round(255 + (cor.getBlue() - 255) * intensidade));
    }

    public static Color escurecer(Color cor, float quantidade) {
        float f = 1f - quantidade;
        return new Color(Math.round(cor.getRed() * f), Math.round(cor.getGreen() * f),
                Math.round(cor.getBlue() * f));
    }

    public static Color comAlfa(Color cor, int alfa) {
        return new Color(cor.getRed(), cor.getGreen(), cor.getBlue(), alfa);
    }
}
