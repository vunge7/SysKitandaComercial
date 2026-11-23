package comercial.relatorios;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import util.BDConexao;

/**
 * Vendas de um intervalo de datas (uma consulta para os documentos e, só se
 * for pedida a vista por artigo, outra para as linhas). Os filtros de cliente,
 * pesquisa e anuladas aplicam-se em memória no ecrã.
 */
public final class VendasClientesDados {

    /** Tipos de documento, como em {@code documento.pk_documento}. */
    public static final int FR = 1, FT = 2, PP = 3, ND = 4, NC = 5;

    /** Um documento de venda. Notas de crédito com valores negativos (abatem às vendas). */
    public static final class Venda {

        public int codigo, clienteId, documento, itens;
        public LocalDateTime data;
        public String numero = "", cliente = "", nif = "", utilizador = "", abreviacao = "";
        public BigDecimal total = BigDecimal.ZERO, iva = BigDecimal.ZERO, desconto = BigDecimal.ZERO;
        public boolean anulada;
    }

    /** Uma linha (artigo) de um documento. */
    public static final class Linha {

        public int venda;
        public String artigo = "";
        public double quantidade, ivaPercentagem;
        public BigDecimal preco = BigDecimal.ZERO, desconto = BigDecimal.ZERO, total = BigDecimal.ZERO;
    }

    private VendasClientesDados() {
    }

    public static List<Venda> vendas(LocalDate de, LocalDate ate, int[] documentos) throws SQLException {
        List<Venda> r = new ArrayList<>();
        String sql = "SELECT v.codigo, v.dataVenda, v.cod_fact, v.codigo_cliente,"
                + " COALESCE(c.nome, v.nome_cliente) AS nome, COALESCE(NULLIF(c.nif, ''), v.cliente_nif) AS nif,"
                + " u.nome AS utilizador, v.fk_documento, d.abreviacao, v.total_venda, v.total_iva, v.desconto_total,"
                + " v.status_eliminado, COUNT(i.codigo) AS itens"
                + " FROM tb_venda v"
                + " LEFT JOIN tb_cliente c ON c.codigo = v.codigo_cliente"
                + " LEFT JOIN tb_usuario u ON u.codigo = v.codigo_usuario"
                + " LEFT JOIN documento d ON d.pk_documento = v.fk_documento"
                + " LEFT JOIN tb_item_venda i ON i.codigo_venda = v.codigo"
                + " WHERE v.dataVenda >= ? AND v.dataVenda < ? AND v.fk_documento IN (" + lista(documentos) + ")"
                + " GROUP BY v.codigo ORDER BY v.dataVenda, v.codigo";
        try (Connection cn = BDConexao.novaLigacao(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(de.atStartOfDay()));
            ps.setTimestamp(2, Timestamp.valueOf(ate.plusDays(1).atStartOfDay()));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Venda v = new Venda();
                    v.codigo = rs.getInt("codigo");
                    Timestamp t = rs.getTimestamp("dataVenda");
                    v.data = t == null ? null : t.toLocalDateTime();
                    v.numero = nvl(rs.getString("cod_fact"));
                    v.clienteId = rs.getInt("codigo_cliente");
                    v.cliente = nvl(rs.getString("nome"));
                    v.nif = nvl(rs.getString("nif"));
                    v.utilizador = nvl(rs.getString("utilizador"));
                    v.documento = rs.getInt("fk_documento");
                    v.abreviacao = nvl(rs.getString("abreviacao"));
                    int sinal = v.documento == NC ? -1 : 1;
                    v.total = dinheiro(rs.getBigDecimal("total_venda")).multiply(BigDecimal.valueOf(sinal));
                    v.iva = dinheiro(rs.getBigDecimal("total_iva")).multiply(BigDecimal.valueOf(sinal));
                    v.desconto = dinheiro(rs.getBigDecimal("desconto_total"));
                    v.anulada = "true".equalsIgnoreCase(nvl(rs.getString("status_eliminado")).trim());
                    v.itens = rs.getInt("itens");
                    r.add(v);
                }
            }
        }
        return r;
    }

    /** Linhas (artigos) dos documentos do intervalo. */
    public static List<Linha> linhas(LocalDate de, LocalDate ate, int[] documentos) throws SQLException {
        List<Linha> r = new ArrayList<>();
        String sql = "SELECT i.codigo_venda, COALESCE(NULLIF(i.designacao_item, ''), p.designacao) AS artigo,"
                + " i.quantidade, i.desconto, i.valor_iva, i.total, pr.preco_venda, v.fk_documento"
                + " FROM tb_item_venda i"
                + " JOIN tb_venda v ON v.codigo = i.codigo_venda"
                + " LEFT JOIN tb_produto p ON p.codigo = i.codigo_produto"
                + " LEFT JOIN tb_preco pr ON pr.pk_preco = i.fk_preco"
                + " WHERE v.dataVenda >= ? AND v.dataVenda < ? AND v.fk_documento IN (" + lista(documentos) + ")"
                + " ORDER BY v.dataVenda, v.codigo, i.codigo";
        try (Connection cn = BDConexao.novaLigacao(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(de.atStartOfDay()));
            ps.setTimestamp(2, Timestamp.valueOf(ate.plusDays(1).atStartOfDay()));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Linha l = new Linha();
                    l.venda = rs.getInt("codigo_venda");
                    l.artigo = nvl(rs.getString("artigo"));
                    l.quantidade = rs.getDouble("quantidade");
                    l.ivaPercentagem = rs.getDouble("valor_iva");
                    int sinal = rs.getInt("fk_documento") == NC ? -1 : 1;
                    l.preco = dinheiro(rs.getBigDecimal("preco_venda"));
                    l.desconto = dinheiro(rs.getBigDecimal("desconto"));
                    l.total = dinheiro(rs.getBigDecimal("total")).multiply(BigDecimal.valueOf(sinal));
                    r.add(l);
                }
            }
        }
        return r;
    }

    private static String lista(int[] docs) {
        StringBuilder s = new StringBuilder();
        for (int d : docs) {
            s.append(s.length() > 0 ? "," : "").append(d);
        }
        return s.length() == 0 ? "0" : s.toString();
    }

    private static BigDecimal dinheiro(BigDecimal b) {
        return b == null ? BigDecimal.ZERO : b;
    }

    private static String nvl(String s) {
        return s == null ? "" : s;
    }
}
