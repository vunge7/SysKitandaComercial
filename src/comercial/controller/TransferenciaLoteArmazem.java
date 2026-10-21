package comercial.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Transferência de LOTES entre armazéns (Depósito -> Farmácia).
 *
 * Tem de ser chamada com a MESMA ligação da transacção da transferência, para
 * que lote, stock e linha de transferência façam commit/rollback juntos.
 *
 * Regras:
 *  - sai dos lotes do armazém de origem por ordem FEFO (validade mais curta
 *    primeiro), só lotes ACTIVOS e dentro da validade (igual à venda);
 *  - no destino o lote é criado (ou somado, se já existir o mesmo nº de lote
 *    com a mesma validade) com a quantidade já convertida (factor de conversão);
 *  - cada lote tocado gera um movimento TRANSF_SAIDA / TRANSF_ENTRADA em
 *    lote_movimento, com fk_operacao = pk da transferência;
 *  - produto sem lotes no armazém de origem (stock antigo) -> só stock, nada a fazer.
 */
public final class TransferenciaLoteArmazem {

    public static final String TIPO_SAIDA = "TRANSF_SAIDA";
    public static final String TIPO_ENTRADA = "TRANSF_ENTRADA";

    private static final double EPS = 0.0005;

    private TransferenciaLoteArmazem() {
    }

    /** O produto tem lotes com quantidade no armazém? */
    public static boolean temLotes(Connection cn, int codProduto, int idArmazem) throws SQLException {
        String sql = "SELECT 1 FROM lote WHERE fk_produto = ? AND fk_armazem = ? AND qtd > 0 LIMIT 1";
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, codProduto);
            ps.setInt(2, idArmazem);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    /** Quantidade transferível: lotes activos e dentro da validade. */
    public static double disponivel(Connection cn, int codProduto, int idArmazem) throws SQLException {
        String sql = "SELECT COALESCE(SUM(qtd), 0) FROM lote "
                + "WHERE fk_produto = ? AND fk_armazem = ? AND qtd > 0 "
                + "  AND estado = 'ACTIVO' AND data_validade >= CURDATE()";
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, codProduto);
            ps.setInt(2, idArmazem);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getDouble(1) : 0;
            }
        }
    }

    private static class LoteOrigem {

        int id;
        double qtd;
    }

    /**
     * @param qtdSaida quantidade que sai da origem (unidade da origem)
     * @param qtdEntrada quantidade que entra no destino (qtdSaida x factor de conversão)
     * @param idTransferencia pk da transferência (vai para lote_movimento.fk_operacao)
     * @return true se movimentou lotes; false se o produto não tem lotes na origem
     * @throws IllegalStateException se os lotes não chegam (o chamador faz rollback)
     */
    public static boolean transferir(Connection cn, int codProduto, int idOrigem, int idDestino,
            double qtdSaida, double qtdEntrada, int idTransferencia) throws SQLException {

        System.err.println("[LOTE] transferir: produto=" + codProduto + " origem=" + idOrigem
                + " destino=" + idDestino + " saida=" + qtdSaida + " entrada=" + qtdEntrada
                + " transferencia=" + idTransferencia);
        if (qtdSaida <= 0 || !temLotes(cn, codProduto, idOrigem)) {
            System.err.println("[LOTE] sem lotes com quantidade no armazém " + idOrigem
                    + " para o produto " + codProduto + " -> só o stock foi movido");
            return false; // sem lotes na origem: comportamento antigo (só stock)
        }

        // 1) lotes FEFO da origem, bloqueados até ao fim da transacção
        List<LoteOrigem> lotes = new ArrayList<>();
        String sel = "SELECT id, qtd FROM lote "
                + "WHERE fk_produto = ? AND fk_armazem = ? AND qtd > 0 "
                + "  AND estado = 'ACTIVO' AND data_validade >= CURDATE() "
                + "ORDER BY data_validade ASC, id ASC FOR UPDATE";
        double total = 0;
        try (PreparedStatement ps = cn.prepareStatement(sel)) {
            ps.setInt(1, codProduto);
            ps.setInt(2, idOrigem);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    LoteOrigem l = new LoteOrigem();
                    l.id = rs.getInt("id");
                    l.qtd = rs.getDouble("qtd");
                    total += l.qtd;
                    lotes.add(l);
                }
            }
        }

        if (total + EPS < qtdSaida) {
            throw new IllegalStateException("Lotes insuficientes no armazém de origem para o produto "
                    + codProduto + ": disponível " + arredondar(total) + " (apenas lotes activos e dentro "
                    + "da validade), pedido " + arredondar(qtdSaida) + ".");
        }

        // 2) reparte a quantidade pelos lotes (FEFO)
        double restante = qtdSaida;
        double entradaAcum = 0;
        for (LoteOrigem l : lotes) {
            if (restante <= EPS) {
                break;
            }
            double tirar = Math.min(restante, l.qtd);
            restante -= tirar;

            // o último lote absorve os arredondamentos
            double entrar = (restante <= EPS)
                    ? arredondar(qtdEntrada - entradaAcum)
                    : arredondar(tirar * qtdEntrada / qtdSaida);
            entradaAcum += entrar;

            // sai da origem
            try (PreparedStatement ps = cn.prepareStatement(
                    "UPDATE lote SET qtd = GREATEST(qtd - ?, 0) WHERE id = ?")) {
                ps.setDouble(1, tirar);
                ps.setInt(2, l.id);
                ps.executeUpdate();
            }
            movimento(cn, l.id, idTransferencia, TIPO_SAIDA, tirar);

            // entra no destino
            int idLoteDestino = entrarNoDestino(cn, l.id, idDestino, entrar);
            movimento(cn, idLoteDestino, idTransferencia, TIPO_ENTRADA, entrar);
            System.err.println("[LOTE] lote origem " + l.id + " (-" + tirar + ") -> lote destino "
                    + idLoteDestino + " (+" + entrar + ")");
        }
        return true;
    }

    /** Soma ao lote equivalente do destino (mesmo nº e validade) ou cria-o. */
    private static int entrarNoDestino(Connection cn, int idLoteOrigem, int idDestino, double qtd) throws SQLException {

        String find = "SELECT d.id FROM lote o "
                + "INNER JOIN lote d ON d.fk_produto = o.fk_produto "
                + "  AND d.numero_lote = o.numero_lote "
                + "  AND d.data_validade = o.data_validade "
                + "  AND d.fk_armazem = ? "
                + "WHERE o.id = ? LIMIT 1 FOR UPDATE";
        try (PreparedStatement ps = cn.prepareStatement(find)) {
            ps.setInt(1, idDestino);
            ps.setInt(2, idLoteOrigem);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int idExistente = rs.getInt(1);
                    // se o lote estava a zero, volta a ficar ACTIVO (a ordem dos SET importa no MySQL)
                    try (PreparedStatement up = cn.prepareStatement(
                            "UPDATE lote SET "
                            + "estado = CASE WHEN qtd <= 0 AND data_validade >= CURDATE() THEN 'ACTIVO' ELSE estado END, "
                            + "qtd = qtd + ? WHERE id = ?")) {
                        up.setDouble(1, qtd);
                        up.setInt(2, idExistente);
                        up.executeUpdate();
                    }
                    return idExistente;
                }
            }
        }

        String ins = "INSERT INTO lote (fk_produto, numero_lote, data_entrada, data_fabrica, data_validade, qtd, estado, fk_armazem) "
                + "SELECT fk_produto, numero_lote, data_entrada, data_fabrica, data_validade, ?, 'ACTIVO', ? "
                + "FROM lote WHERE id = ?";
        try (PreparedStatement ps = cn.prepareStatement(ins, Statement.RETURN_GENERATED_KEYS)) {
            ps.setDouble(1, qtd);
            ps.setInt(2, idDestino);
            ps.setInt(3, idLoteOrigem);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        throw new SQLException("Não foi possível criar o lote no armazém de destino.");
    }

    private static void movimento(Connection cn, int idLote, int idOperacao, String tipo, double qtd) throws SQLException {
        String sql = "INSERT INTO lote_movimento (fk_lote, fk_operacao, tipo, qtd, qtd_devolvida, data_mov) "
                + "VALUES (?, ?, ?, ?, 0, NOW())";
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, idLote);
            ps.setInt(2, idOperacao);
            ps.setString(3, tipo);
            ps.setDouble(4, qtd);
            ps.executeUpdate();
        }
    }

    private static double arredondar(double v) {
        return Math.round(v * 1000.0) / 1000.0;
    }
}
