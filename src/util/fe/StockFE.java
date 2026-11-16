package util.fe;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import util.DVML;

/**
 * Controlo do stock com a Facturação Electrónica.
 *
 * Regra: um documento de venda inválido na AGT ("I") não desconta stock.
 * <ul>
 * <li>Na emissão com ligação à AGT, quem factura só abate o stock depois da
 * resposta (se vier "I" não abate).</li>
 * <li>Na emissão OFFLINE a venda fica pendente ("P") e o stock é abatido logo
 * (a venda aconteceu). Quando a comunicação automática a envia mais tarde e a
 * AGT a dá como inválida, o stock é devolvido aqui, uma só vez: a passagem
 * para "I" só acontece uma vez por documento (depois disso já não está nas
 * listas de pendentes).</li>
 * </ul>
 */
public final class StockFE {

    private StockFE() {
    }

    /**
     * true quando o estado passa agora a inválido, vindo de pendente/sem
     * estado: é o único caso em que o stock (já abatido) tem de voltar.
     */
    public static boolean passouAInvalida(String estadoAnterior, String estadoNovo) {
        return DVML.ESTADO_INVALIDO.equalsIgnoreCase(estadoNovo)
                && !DVML.ESTADO_INVALIDO.equalsIgnoreCase(estadoAnterior);
    }

    /**
     * Devolve ao armazém da venda (tb_venda.idArmazemFK) as quantidades dos
     * produtos stocáveis de uma factura FR/FT. Usa a ligação (e transacção) de
     * quem chama.
     *
     * @return número de produtos com stock devolvido
     */
    public static int devolverStockDaVenda(Connection conn, int codigoVenda) throws SQLException {
        String sql = "UPDATE tb_stock s"
                + " JOIN tb_venda v ON v.codigo = ? AND s.cod_armazem = v.idArmazemFK"
                + " JOIN (SELECT iv.codigo_produto, SUM(iv.quantidade) AS q"
                + "       FROM tb_item_venda iv JOIN tb_produto p ON p.codigo = iv.codigo_produto"
                + "       WHERE iv.codigo_venda = ? AND p.stocavel = 'true'"
                + "       GROUP BY iv.codigo_produto) x ON x.codigo_produto = s.cod_produto_codigo"
                + " SET s.quantidade_existente = s.quantidade_existente + x.q"
                + " WHERE v.fk_documento IN (" + DVML.DOC_FACTURA_RECIBO_FR + ", " + DVML.DOC_FACTURA_FT + ")";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, codigoVenda);
            ps.setInt(2, codigoVenda);
            int n = ps.executeUpdate();
            System.err.println("[FE] Venda " + codigoVenda + " inválida na AGT: stock devolvido em " + n + " produto(s).");
            return n;
        }
    }

    /** Se o estado acabou de passar a "I", devolve o stock; erros só ficam no registo (o estado já foi gravado). */
    public static void devolverSeFicouInvalida(Connection conn, int codigoVenda, String estadoAnterior, String estadoNovo) {
        if (!passouAInvalida(estadoAnterior, estadoNovo)) {
            return;
        }
        try {
            devolverStockDaVenda(conn, codigoVenda);
        } catch (SQLException e) {
            System.err.println("[FE] ATENÇÃO: não foi possível devolver o stock da venda " + codigoVenda + ": " + e.getMessage());
            e.printStackTrace();
        }
    }
}
