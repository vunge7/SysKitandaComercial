package util;

/**
 * Tela de vendas a abrir, configurada em tb_dados_instituicao.tela_venda.
 * NORMAL = FormVendaResponsivaVisaoTop, LATEST = VendaModernaVisao.
 */
public enum TelaVenda {
    NORMAL,
    LATEST;

    /** Valor nulo ou desconhecido devolve NORMAL (fallback). */
    public static TelaVenda fromString(String valor) {
        if (valor != null) {
            for (TelaVenda t : values()) {
                if (t.name().equalsIgnoreCase(valor.trim())) {
                    return t;
                }
            }
        }
        return NORMAL;
    }
}
