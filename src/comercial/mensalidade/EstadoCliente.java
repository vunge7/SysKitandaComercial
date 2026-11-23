package comercial.mensalidade;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import util.BDConexao;

/**
 * Estado da inscrição de cada cliente nas mensalidades:
 * <ul>
 * <li>ACTIVO — paga e recebe alertas (é o estado de quem não tem registo);</li>
 * <li>DESISTENTE — deixou de vir por vontade própria;</li>
 * <li>DESACTIVADO — suspenso pela casa (falta de pagamento, lesão, viagem...).</li>
 * </ul>
 * Um cliente inactivo deixa de gerar dívida a partir da data em que saiu e
 * não aparece nos alertas. Quando volta, reactiva-se (com taxa, se a casa a
 * cobrar) — não é preciso voltar a inscrevê-lo.
 * <p>
 * Tabelas: {@code mensalidade_cliente_estado} (estado actual, uma linha por
 * cliente) e {@code mensalidade_cliente_historico} (todas as mudanças).
 */
public final class EstadoCliente {

    public enum Estado {
        ACTIVO("Activo"), DESISTENTE("Desistente"), DESACTIVADO("Desactivado");

        private final String texto;

        Estado(String texto) {
            this.texto = texto;
        }

        public String texto() {
            return texto;
        }

        @Override
        public String toString() {
            return texto;
        }

        static Estado de(String s) {
            try {
                return s == null ? ACTIVO : valueOf(s.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                return ACTIVO;
            }
        }
    }

    /** O estado de um cliente (desde quando e porquê). */
    public static final class Registo {

        public final Estado estado;
        public final LocalDateTime data;
        public final String motivo;

        Registo(Estado estado, LocalDateTime data, String motivo) {
            this.estado = estado;
            this.data = data;
            this.motivo = motivo == null ? "" : motivo;
        }

        public boolean inactivo() {
            return estado != Estado.ACTIVO;
        }

        /** Último dia que conta para a dívida (o dia em que saiu); null se activo. */
        public LocalDate limiteDivida() {
            return inactivo() && data != null ? data.toLocalDate() : null;
        }
    }

    /** Uma linha do histórico. */
    public static final class Movimento {

        public Estado estado;
        public LocalDateTime data;
        public String motivo;
        public BigDecimal valor = BigDecimal.ZERO;
    }

    public static final Registo ACTIVO_SEM_REGISTO = new Registo(Estado.ACTIVO, null, "");

    private EstadoCliente() {
    }

    static void garantirTabelas(Statement st) throws SQLException {
        st.executeUpdate("CREATE TABLE IF NOT EXISTS mensalidade_cliente_estado ("
                + " cliente_id INT NOT NULL PRIMARY KEY,"
                + " estado VARCHAR(20) NOT NULL DEFAULT 'ACTIVO',"
                + " data_estado DATETIME NULL,"
                + " motivo VARCHAR(255) NULL,"
                + " usuario_id INT NOT NULL DEFAULT 0"
                + ") ENGINE=InnoDB DEFAULT CHARSET=utf8");
        st.executeUpdate("CREATE TABLE IF NOT EXISTS mensalidade_cliente_historico ("
                + " id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,"
                + " cliente_id INT NOT NULL,"
                + " estado VARCHAR(20) NOT NULL,"
                + " data DATETIME NOT NULL,"
                + " motivo VARCHAR(255) NULL,"
                + " valor DECIMAL(14,2) NOT NULL DEFAULT 0,"
                + " usuario_id INT NOT NULL DEFAULT 0,"
                + " KEY idx_cliente (cliente_id)"
                + ") ENGINE=InnoDB DEFAULT CHARSET=utf8");
    }

    /** Estado de todos os clientes que têm registo (os outros são activos). */
    public static Map<Integer, Registo> todos(Connection cn) {
        Map<Integer, Registo> m = new HashMap<>();
        try (PreparedStatement ps = cn.prepareStatement(
                "SELECT cliente_id, estado, data_estado, motivo FROM mensalidade_cliente_estado");
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Timestamp t = rs.getTimestamp("data_estado");
                m.put(rs.getInt("cliente_id"), new Registo(Estado.de(rs.getString("estado")),
                        t == null ? null : t.toLocalDateTime(), rs.getString("motivo")));
            }
        } catch (SQLException e) {
            // tabela ainda por criar (negócio sem mensalidades): todos activos
        }
        return m;
    }

    public static Registo de(int clienteId) {
        Mensalidades.garantirEstrutura();
        try (Connection cn = BDConexao.novaLigacao()) {
            return de(cn, clienteId);
        } catch (SQLException e) {
            return ACTIVO_SEM_REGISTO;
        }
    }

    public static Registo de(Connection cn, int clienteId) {
        try (PreparedStatement ps = cn.prepareStatement(
                "SELECT estado, data_estado, motivo FROM mensalidade_cliente_estado WHERE cliente_id = ?")) {
            ps.setInt(1, clienteId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Timestamp t = rs.getTimestamp("data_estado");
                    return new Registo(Estado.de(rs.getString("estado")), t == null ? null : t.toLocalDateTime(),
                            rs.getString("motivo"));
                }
            }
        } catch (SQLException e) {
            // sem tabela: activo
        }
        return ACTIVO_SEM_REGISTO;
    }

    /** Muda o estado e regista no histórico (valor = taxa de reactivação cobrada, se houver). */
    public static boolean alterar(int clienteId, Estado estado, String motivo, BigDecimal valor) {
        Mensalidades.garantirEstrutura();
        Timestamp agora = Timestamp.valueOf(LocalDateTime.now());
        try (Connection cn = BDConexao.novaLigacao()) {
            try (PreparedStatement ps = cn.prepareStatement(
                    "INSERT INTO mensalidade_cliente_estado (cliente_id, estado, data_estado, motivo) VALUES (?, ?, ?, ?)"
                    + " ON DUPLICATE KEY UPDATE estado = VALUES(estado), data_estado = VALUES(data_estado), motivo = VALUES(motivo)")) {
                ps.setInt(1, clienteId);
                ps.setString(2, estado.name());
                ps.setTimestamp(3, agora);
                ps.setString(4, cortar(motivo));
                ps.executeUpdate();
            }
            try (PreparedStatement ps = cn.prepareStatement(
                    "INSERT INTO mensalidade_cliente_historico (cliente_id, estado, data, motivo, valor) VALUES (?, ?, ?, ?, ?)")) {
                ps.setInt(1, clienteId);
                ps.setString(2, estado.name());
                ps.setTimestamp(3, agora);
                ps.setString(4, cortar(motivo));
                ps.setBigDecimal(5, valor == null ? BigDecimal.ZERO : valor);
                ps.executeUpdate();
            }
            return true;
        } catch (SQLException e) {
            System.err.println("[EstadoCliente] " + e.getMessage());
            return false;
        }
    }

    public static List<Movimento> historico(int clienteId) {
        List<Movimento> l = new ArrayList<>();
        Mensalidades.garantirEstrutura();
        try (Connection cn = BDConexao.novaLigacao();
                PreparedStatement ps = cn.prepareStatement(
                        "SELECT estado, data, motivo, valor FROM mensalidade_cliente_historico WHERE cliente_id = ? ORDER BY data DESC, id DESC")) {
            ps.setInt(1, clienteId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Movimento m = new Movimento();
                    m.estado = Estado.de(rs.getString("estado"));
                    Timestamp t = rs.getTimestamp("data");
                    m.data = t == null ? null : t.toLocalDateTime();
                    m.motivo = rs.getString("motivo") == null ? "" : rs.getString("motivo");
                    m.valor = rs.getBigDecimal("valor") == null ? BigDecimal.ZERO : rs.getBigDecimal("valor");
                    l.add(m);
                }
            }
        } catch (SQLException e) {
            System.err.println("[EstadoCliente] histórico: " + e.getMessage());
        }
        return l;
    }

    private static String cortar(String s) {
        if (s == null) {
            return "";
        }
        return s.length() > 250 ? s.substring(0, 250) : s;
    }
}
