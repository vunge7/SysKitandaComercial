package comercial.mensalidade;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import util.BDConexao;

/**
 * Parâmetros dos alertas de mensalidade, guardados na tabela
 * {@code mensalidade_config} (uma linha, id = 1):
 * <ul>
 * <li>dias_alerta — quantos dias antes do vencimento começa o alerta laranja (5);</li>
 * <li>intervalo_alerta_min — de quanto em quanto tempo o alerta volta (60 min);</li>
 * <li>duracao_alerta_seg — quanto tempo fica no ecrã (120 s);</li>
 * <li>som_alerta — se toca o som (1);</li>
 * <li>alertas_activos — se os alertas estão ligados (1);</li>
 * <li>alarme_continuo — o som repete-se até alguém silenciar/mexer no alerta (1);</li>
 * <li>alertar_atrasados — o alerta inclui os devedores em atraso, não só os que vão vencer (1).</li>
 * </ul>
 */
public final class ConfigMensalidade {

    public int diasAlerta = 5;
    public int intervaloMinutos = 60;
    public int duracaoSegundos = 120;
    public boolean som = true;
    public boolean activos = true;
    /** Alarme: o som repete-se enquanto o alerta estiver no ecrã, até alguém o silenciar. */
    public boolean alarmeContinuo = true;
    /** O alerta inclui os devedores (mensalidades em atraso), não só as que vão vencer. */
    public boolean alertarAtrasados = true;
    /** Serviço cobrado como taxa de reactivação (0 = nenhum). */
    public int produtoReactivacao = 0;
    /** Serviço das linhas de multa na factura (0 = cria-se "Multa por atraso" quando for preciso). */
    public int produtoMulta = 0;
    /** De quando se contam os dias da multa: MES (dia 1 do mês) ou VENCIMENTO (dia de pagamento do contrato). */
    public String multaBase = RegrasMulta.BASE_MES;

    private static volatile ConfigMensalidade cache;
    private static volatile long cacheEm;

    /** A configuração lida há menos de 30 s (para os cálculos repetidos das janelas). */
    public static ConfigMensalidade lerEmCache() {
        ConfigMensalidade c = cache;
        if (c == null || System.currentTimeMillis() - cacheEm > 30_000) {
            c = ler();
        }
        return c;
    }

    static void garantirTabela(Statement st) throws SQLException {
        st.executeUpdate("CREATE TABLE IF NOT EXISTS mensalidade_config ("
                + " id INT NOT NULL PRIMARY KEY,"
                + " dias_alerta INT NOT NULL DEFAULT 5,"
                + " intervalo_alerta_min INT NOT NULL DEFAULT 60,"
                + " duracao_alerta_seg INT NOT NULL DEFAULT 120,"
                + " som_alerta TINYINT(1) NOT NULL DEFAULT 1,"
                + " alertas_activos TINYINT(1) NOT NULL DEFAULT 1"
                + ") ENGINE=InnoDB DEFAULT CHARSET=latin1");
        st.executeUpdate("INSERT IGNORE INTO mensalidade_config (id) VALUES (1)");
        if (!Mensalidades.existeColuna(st.getConnection(), "mensalidade_config", "produto_reactivacao_id")) {
            st.executeUpdate("ALTER TABLE mensalidade_config ADD COLUMN produto_reactivacao_id INT NOT NULL DEFAULT 0");
        }
        if (!Mensalidades.existeColuna(st.getConnection(), "mensalidade_config", "produto_multa_id")) {
            st.executeUpdate("ALTER TABLE mensalidade_config ADD COLUMN produto_multa_id INT NOT NULL DEFAULT 0");
        }
        if (!Mensalidades.existeColuna(st.getConnection(), "mensalidade_config", "multa_base")) {
            st.executeUpdate("ALTER TABLE mensalidade_config ADD COLUMN multa_base VARCHAR(12) NOT NULL DEFAULT 'MES'");
        }
        if (!Mensalidades.existeColuna(st.getConnection(), "mensalidade_config", "alarme_continuo")) {
            st.executeUpdate("ALTER TABLE mensalidade_config ADD COLUMN alarme_continuo TINYINT(1) NOT NULL DEFAULT 1");
        }
        if (!Mensalidades.existeColuna(st.getConnection(), "mensalidade_config", "alertar_atrasados")) {
            st.executeUpdate("ALTER TABLE mensalidade_config ADD COLUMN alertar_atrasados TINYINT(1) NOT NULL DEFAULT 1");
        }
    }

    /** Lê a configuração (valores por omissão se a tabela ainda não existir). */
    public static ConfigMensalidade ler() {
        ConfigMensalidade c = new ConfigMensalidade();
        Mensalidades.garantirEstrutura();
        try (Connection cn = BDConexao.novaLigacao();
                PreparedStatement ps = cn.prepareStatement("SELECT * FROM mensalidade_config WHERE id = 1");
                ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                c.diasAlerta = Math.max(0, rs.getInt("dias_alerta"));
                c.intervaloMinutos = Math.max(1, rs.getInt("intervalo_alerta_min"));
                c.duracaoSegundos = Math.max(5, rs.getInt("duracao_alerta_seg"));
                c.som = rs.getInt("som_alerta") != 0;
                c.activos = rs.getInt("alertas_activos") != 0;
                try {
                    c.produtoReactivacao = rs.getInt("produto_reactivacao_id");
                    c.produtoMulta = rs.getInt("produto_multa_id");
                    c.multaBase = rs.getString("multa_base") == null ? RegrasMulta.BASE_MES : rs.getString("multa_base");
                    c.alarmeContinuo = rs.getInt("alarme_continuo") != 0;
                    c.alertarAtrasados = rs.getInt("alertar_atrasados") != 0;
                } catch (SQLException semColuna) {
                    // colunas ainda por criar: valores por omissão
                }
            }
        } catch (SQLException e) {
            System.err.println("[Mensalidades] configuração dos alertas: " + e.getMessage());
        }
        cache = c;
        cacheEm = System.currentTimeMillis();
        return c;
    }

    public boolean gravar() {
        Mensalidades.garantirEstrutura();
        String sql = "UPDATE mensalidade_config SET dias_alerta = ?, intervalo_alerta_min = ?,"
                + " duracao_alerta_seg = ?, som_alerta = ?, alertas_activos = ?, produto_reactivacao_id = ?,"
                + " produto_multa_id = ?, multa_base = ?, alarme_continuo = ?, alertar_atrasados = ? WHERE id = 1";
        try (Connection cn = BDConexao.novaLigacao(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, Math.max(0, diasAlerta));
            ps.setInt(2, Math.max(1, intervaloMinutos));
            ps.setInt(3, Math.max(5, duracaoSegundos));
            ps.setInt(4, som ? 1 : 0);
            ps.setInt(5, activos ? 1 : 0);
            ps.setInt(6, Math.max(0, produtoReactivacao));
            ps.setInt(7, Math.max(0, produtoMulta));
            ps.setString(8, RegrasMulta.BASE_VENCIMENTO.equalsIgnoreCase(multaBase) ? RegrasMulta.BASE_VENCIMENTO : RegrasMulta.BASE_MES);
            ps.setInt(9, alarmeContinuo ? 1 : 0);
            ps.setInt(10, alertarAtrasados ? 1 : 0);
            boolean ok = ps.executeUpdate() > 0;
            cache = null;
            return ok;
        } catch (SQLException e) {
            System.err.println("[Mensalidades] gravar configuração dos alertas: " + e.getMessage());
            return false;
        }
    }
}
