package comercial.controller;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import util.BDConexao;

/**
 * Leituras da gestão de fornecedores (dívidas, antiguidade, pagamentos), em
 * consultas directas e agregadas para o ecrã não esperar.
 *
 * <p>
 * Dívida de uma compra = compras a crédito (V/Factura) ainda não liquidadas:
 * valor_por_pagar, ou (quando ficou vazio) total_compra - valor_pago. Os
 * pagamentos continuam a ser feitos pela Saída de tesouraria (SaidaVisao), que
 * debita o extrato do fornecedor e actualiza a compra.
 */
public class GestaoFornecedoresController {

    private final BDConexao conexao;

    /** Compras não eliminadas, a crédito, com valor em falta. */
    private static final String RESTANTE
            = "COALESCE(c.valor_por_pagar, c.total_compra - COALESCE(c.valor_pago, 0))";
    private static final String FILTRO_DIVIDA
            = " (c.status_eliminado IS NULL OR c.status_eliminado <> 'true')"
            + " AND c.doc_vosso = 'V/Factura'"
            + " AND " + RESTANTE + " > 0.005";

    public GestaoFornecedoresController(BDConexao conexao) {
        this.conexao = conexao;
    }

    /** Uma compra em dívida. */
    public static final class Divida {

        public int pkCompra;
        public String codFact;
        public String documento;
        public Date data;
        public Date vencimento;
        public double total;
        public double pago;
        public double restante;
        public int fornecedorId;
        public String fornecedor;

        /** Dias em atraso (negativo = ainda não venceu; sem vencimento conta a data da compra). */
        public long diasAtraso() {
            Date ref = vencimento != null ? vencimento : data;
            if (ref == null) {
                return 0;
            }
            return (inicioDoDia(new Date()).getTime() - inicioDoDia(ref).getTime()) / 86_400_000L;
        }

        public boolean vencida() {
            return vencimento != null && diasAtraso() > 0;
        }
    }

    /** Resumo do ecrã. */
    public static final class Resumo {

        public int fornecedores;
        public int fornecedoresComDivida;
        public double emDivida;
        public double vencido;
        public double pago30Dias;
        public int documentosAbertos;
    }

    /** Dívida por fornecedor (ranking dos maiores credores). */
    public static final class DividaFornecedor {

        public int fornecedorId;
        public String fornecedor;
        public int documentos;
        public double emDivida;
        public double vencido;
    }

    /** Compras em dívida; idFornecedor 0 = todos. Das mais antigas para as mais recentes. */
    public List<Divida> dividas(int idFornecedor) throws SQLException {
        String sql = "SELECT c.pk_compra, c.cod_fact, c.doc_vosso, c.doc_vosso_numero, c.doc_vosso_data,"
                + " c.doc_vosso_data_vencimento, c.data_compra, c.total_compra, COALESCE(c.valor_pago, 0),"
                + " " + RESTANTE + ", c.fk_fornecedor, COALESCE(f.nome, c.nome_fornecedor)"
                + " FROM compras c LEFT JOIN tb_fornecedor f ON f.codigo = c.fk_fornecedor"
                + " WHERE" + FILTRO_DIVIDA + (idFornecedor > 0 ? " AND c.fk_fornecedor = ?" : "")
                + " ORDER BY COALESCE(c.doc_vosso_data_vencimento, c.doc_vosso_data, c.data_compra), c.pk_compra";
        List<Divida> lista = new ArrayList<>();
        try (PreparedStatement ps = conexao.getPreparedStatement(sql)) {
            if (idFornecedor > 0) {
                ps.setInt(1, idFornecedor);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Divida d = new Divida();
                    d.pkCompra = rs.getInt(1);
                    d.codFact = rs.getString(2);
                    String doc = rs.getString(3), num = rs.getString(4);
                    d.documento = ((doc == null ? "" : doc) + " " + (num == null ? "" : num)).trim();
                    d.data = data(rs, 5);
                    if (d.data == null) {
                        d.data = data(rs, 7);
                    }
                    d.vencimento = data(rs, 6);
                    d.total = rs.getDouble(8);
                    d.pago = rs.getDouble(9);
                    d.restante = Math.round(rs.getDouble(10) * 100) / 100.0;
                    d.fornecedorId = rs.getInt(11);
                    d.fornecedor = rs.getString(12);
                    lista.add(d);
                }
            }
        }
        return lista;
    }

    /** Totais do topo do ecrã. */
    public Resumo resumo() throws SQLException {
        Resumo r = new Resumo();
        try (PreparedStatement ps = conexao.getPreparedStatement("SELECT COUNT(*) FROM tb_fornecedor");
                ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                r.fornecedores = rs.getInt(1);
            }
        }
        java.util.Set<Integer> comDivida = new java.util.HashSet<>();
        for (Divida d : dividas(0)) {
            r.emDivida += d.restante;
            r.documentosAbertos++;
            if (d.vencida()) {
                r.vencido += d.restante;
            }
            comDivida.add(d.fornecedorId);
        }
        r.fornecedoresComDivida = comDivida.size();
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, -30);
        try (PreparedStatement ps = conexao.getPreparedStatement(
                "SELECT COALESCE(SUM(debito), 0) FROM extrato_conta_fornecedor WHERE tipo_extrato = 'DB' AND data_hora >= ?")) {
            ps.setTimestamp(1, new java.sql.Timestamp(inicioDoDia(c.getTime()).getTime()));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    r.pago30Dias = rs.getDouble(1);
                }
            }
        }
        return r;
    }

    /** Maiores credores (por valor em dívida). */
    public List<DividaFornecedor> rankingDividas() throws SQLException {
        java.util.Map<Integer, DividaFornecedor> m = new java.util.LinkedHashMap<>();
        for (Divida d : dividas(0)) {
            DividaFornecedor f = m.get(d.fornecedorId);
            if (f == null) {
                f = new DividaFornecedor();
                f.fornecedorId = d.fornecedorId;
                f.fornecedor = d.fornecedor;
                m.put(d.fornecedorId, f);
            }
            f.documentos++;
            f.emDivida += d.restante;
            if (d.vencida()) {
                f.vencido += d.restante;
            }
        }
        List<DividaFornecedor> l = new ArrayList<>(m.values());
        l.sort((a, b) -> Double.compare(b.emDivida, a.emDivida));
        return l;
    }

    /** Antiguidade da dívida: [a vencer, 0-30, 31-60, 61-90, +90 dias]. */
    public static double[] antiguidade(List<Divida> dividas) {
        double[] f = new double[5];
        for (Divida d : dividas) {
            long dias = d.diasAtraso();
            int i = dias <= 0 ? 0 : dias <= 30 ? 1 : dias <= 60 ? 2 : dias <= 90 ? 3 : 4;
            f[i] += d.restante;
        }
        return f;
    }

    /** Última compra e número de compras do fornecedor: {Date, Integer}. */
    public Object[] actividade(int idFornecedor) throws SQLException {
        try (PreparedStatement ps = conexao.getPreparedStatement(
                "SELECT MAX(COALESCE(doc_vosso_data, data_compra)), COUNT(*) FROM compras"
                + " WHERE fk_fornecedor = ? AND (status_eliminado IS NULL OR status_eliminado <> 'true')")) {
            ps.setInt(1, idFornecedor);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Object[]{data(rs, 1), rs.getInt(2)};
                }
            }
        }
        return new Object[]{null, 0};
    }

    /**
     * Antes de pagar: os campos de pagamento da compra deixam de estar vazios
     * (a Saída de tesouraria soma ao valor pago e subtrai ao valor por pagar).
     */
    public void prepararParaPagamento(int pkCompra) throws SQLException {
        try (PreparedStatement ps = conexao.getPreparedStatement(
                "UPDATE compras SET valor_pago = COALESCE(valor_pago, 0),"
                + " valor_por_pagar = COALESCE(valor_por_pagar, total_compra - COALESCE(valor_pago, 0))"
                + " WHERE pk_compra = ? AND (valor_pago IS NULL OR valor_por_pagar IS NULL)")) {
            ps.setInt(1, pkCompra);
            ps.executeUpdate();
        }
    }

    private static Date data(ResultSet rs, int col) {
        try {
            java.sql.Timestamp t = rs.getTimestamp(col);
            return t == null ? null : new Date(t.getTime());
        } catch (SQLException e) {
            return null; // data inválida gravada (0000-00-00): fica sem data
        }
    }

    static Date inicioDoDia(Date d) {
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }
}
