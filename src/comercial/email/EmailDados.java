package comercial.email;

import comercial.whatsapp.RelatorioWhatsApp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Dados do envio de relatórios por E-MAIL (pedido do utilizador, 2026-11-14):
 * a mesma estrutura do WhatsApp (comercial.whatsapp.WhatsAppDados), com tabelas próprias:
 * - tb_email_contacto: responsáveis (nome, cargo, endereço de e-mail);
 * - tb_email_agendamento: "a este contacto, às HH:mm, nestes dias, enviar estes relatórios";
 * - tb_email_envio: histórico de tudo o que foi enviado (ou falhou);
 * - tb_email_config: conta de e-mail (SMTP) que envia.
 * Os relatórios são os mesmos do WhatsApp (RelatorioWhatsApp). Tabelas criadas sozinhas.
 */
public final class EmailDados {

    public static final String MODO_AUTOMATICO = "AUTO";
    public static final String MODO_MANUAL = "MANUAL";

    public static final String ESTADO_ENVIADO = "ENVIADO";
    public static final String ESTADO_FALHOU = "FALHOU";
    public static final String ESTADO_SIMULADO = "SIMULADO";

    /** Dias da semana pela ordem do WEEKDAY() do MySQL (0 = segunda). */
    public static final String[] DIAS_CURTOS = {"Seg", "Ter", "Qua", "Qui", "Sex", "Sáb", "Dom"};
    public static final String TODOS_OS_DIAS = "1111111";

    private static volatile boolean tabelasProntas = false;

    private EmailDados() {
    }

    // =====================================================================
    // TABELAS
    // =====================================================================
    public static void garantirTabelas(Connection cn) {
        if (tabelasProntas) {
            return;
        }
        String[] ddl = {
            "CREATE TABLE IF NOT EXISTS tb_email_contacto ("
            + " pk_contacto INT NOT NULL AUTO_INCREMENT,"
            + " nome VARCHAR(120) NOT NULL,"
            + " cargo VARCHAR(80) NULL,"
            + " email VARCHAR(150) NOT NULL,"
            + " activo TINYINT(1) NOT NULL DEFAULT 1,"
            + " data_registo DATETIME NULL,"
            + " PRIMARY KEY (pk_contacto),"
            + " UNIQUE KEY uk_email_endereco (email)"
            + ") ENGINE=InnoDB DEFAULT CHARSET=utf8",
            "CREATE TABLE IF NOT EXISTS tb_email_agendamento ("
            + " pk_agendamento INT NOT NULL AUTO_INCREMENT,"
            + " fk_contacto INT NOT NULL,"
            + " hora CHAR(5) NOT NULL,"
            + " dias CHAR(7) NOT NULL DEFAULT '1111111',"
            + " relatorios VARCHAR(255) NOT NULL,"
            + " activo TINYINT(1) NOT NULL DEFAULT 1,"
            + " ultimo_envio DATETIME NULL,"
            + " PRIMARY KEY (pk_agendamento),"
            + " KEY idx_email_ag_contacto (fk_contacto)"
            + ") ENGINE=InnoDB DEFAULT CHARSET=utf8",
            "CREATE TABLE IF NOT EXISTS tb_email_envio ("
            + " pk_envio INT NOT NULL AUTO_INCREMENT,"
            + " data_envio DATETIME NOT NULL,"
            + " fk_contacto INT NULL,"
            + " nome VARCHAR(120) NULL,"
            + " email VARCHAR(150) NULL,"
            + " relatorio VARCHAR(80) NULL,"
            + " modo VARCHAR(10) NOT NULL,"
            + " estado VARCHAR(10) NOT NULL,"
            + " detalhe VARCHAR(500) NULL,"
            + " fk_usuario INT NULL,"
            + " PRIMARY KEY (pk_envio),"
            + " KEY idx_email_envio_data (data_envio)"
            + ") ENGINE=InnoDB DEFAULT CHARSET=utf8",
            "CREATE TABLE IF NOT EXISTS tb_email_config ("
            + " chave VARCHAR(60) NOT NULL,"
            + " valor TEXT NULL,"
            + " PRIMARY KEY (chave)"
            + ") ENGINE=InnoDB DEFAULT CHARSET=utf8"
        };
        try (Statement st = cn.createStatement()) {
            for (String s : ddl) {
                st.execute(s);
            }
            // 2026-11-14: o contacto recebe os fechos de caixa (por omissão sim)
            try (ResultSet rc = st.executeQuery("SELECT COUNT(*) FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = DATABASE()"
                    + " AND TABLE_NAME = 'tb_email_contacto' AND COLUMN_NAME = 'recebe_fecho'")) {
                if (rc.next() && rc.getInt(1) == 0) {
                    st.execute("ALTER TABLE tb_email_contacto ADD COLUMN recebe_fecho TINYINT(1) NOT NULL DEFAULT 1");
                }
            }
            tabelasProntas = true;
        } catch (SQLException e) {
            System.err.println("[E-mail] não foi possível criar as tabelas: " + e.getMessage());
        }
    }

    // =====================================================================
    // CONFIGURAÇÃO
    // =====================================================================
    public static Map<String, String> lerConfig(Connection cn) {
        garantirTabelas(cn);
        Map<String, String> m = new LinkedHashMap<>();
        try (Statement st = cn.createStatement(); ResultSet rs = st.executeQuery("SELECT chave, valor FROM tb_email_config")) {
            while (rs.next()) {
                m.put(rs.getString(1), rs.getString(2) == null ? "" : rs.getString(2));
            }
        } catch (SQLException e) {
            System.err.println("[E-mail] configuração: " + e.getMessage());
        }
        return m;
    }

    public static void gravarConfig(Connection cn, Map<String, String> valores) throws SQLException {
        garantirTabelas(cn);
        try (PreparedStatement ps = cn.prepareStatement("INSERT INTO tb_email_config (chave, valor) VALUES (?, ?)"
                + " ON DUPLICATE KEY UPDATE valor = VALUES(valor)")) {
            for (Map.Entry<String, String> e : valores.entrySet()) {
                ps.setString(1, e.getKey());
                ps.setString(2, e.getValue() == null ? "" : e.getValue().trim());
                ps.executeUpdate();
            }
        }
    }

    // =====================================================================
    // ENDEREÇOS
    // =====================================================================
    /**
     * Endereço de e-mail em minúsculas, sem espaços; null se não parecer um
     * endereço válido (nome@dominio.ext).
     */
    public static String normalizarEmail(String endereco) {
        if (endereco == null) {
            return null;
        }
        String e = endereco.trim().toLowerCase(java.util.Locale.ROOT);
        return e.matches("[a-z0-9._%+-]+@[a-z0-9.-]+[.][a-z]{2,}") && e.length() <= 150 ? e : null;
    }

    /** O endereço como se mostra (já vem normalizado). */
    public static String legivel(String e) {
        return e == null ? "" : e;
    }

    // =====================================================================
    // CONTACTOS
    // =====================================================================
    public static class Contacto {

        public int id;
        public String nome = "", cargo = "", email = "";
        /** Recebe o PDF de cada fecho de caixa (Recepção, Restaurante, Bar), no momento do fecho. */
        public boolean recebeFecho = true;
        public boolean activo = true;

        @Override
        public String toString() {
            return nome + (cargo.isEmpty() ? "" : " (" + cargo + ")");
        }
    }

    public static List<Contacto> contactos(Connection cn, boolean soActivos) throws SQLException {
        garantirTabelas(cn);
        List<Contacto> l = new ArrayList<>();
        String sql = "SELECT pk_contacto, nome, cargo, email, activo, recebe_fecho FROM tb_email_contacto"
                + (soActivos ? " WHERE activo = 1" : "") + " ORDER BY activo DESC, nome";
        try (Statement st = cn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Contacto c = new Contacto();
                c.id = rs.getInt(1);
                c.nome = texto(rs.getString(2));
                c.cargo = texto(rs.getString(3));
                c.email = texto(rs.getString(4));
                c.activo = rs.getInt(5) == 1;
                c.recebeFecho = rs.getInt(6) == 1;
                l.add(c);
            }
        }
        return l;
    }

    public static void gravarContacto(Connection cn, Contacto c) throws SQLException {
        garantirTabelas(cn);
        String sql = c.id == 0
                ? "INSERT INTO tb_email_contacto (nome, cargo, email, activo, recebe_fecho, data_registo) VALUES (?, ?, ?, ?, ?, NOW())"
                : "UPDATE tb_email_contacto SET nome = ?, cargo = ?, email = ?, activo = ?, recebe_fecho = ? WHERE pk_contacto = ?";
        try (PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, c.nome);
            ps.setString(2, c.cargo);
            ps.setString(3, c.email);
            ps.setInt(4, c.activo ? 1 : 0);
            ps.setInt(5, c.recebeFecho ? 1 : 0);
            if (c.id != 0) {
                ps.setInt(6, c.id);
            }
            ps.executeUpdate();
            if (c.id == 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        c.id = rs.getInt(1);
                    }
                }
            }
        }
    }

    /** Contactos activos que recebem os fechos de caixa. */
    public static List<Contacto> contactosFecho(Connection cn) throws SQLException {
        List<Contacto> l = new ArrayList<>();
        for (Contacto c : contactos(cn, true)) {
            if (c.recebeFecho) {
                l.add(c);
            }
        }
        return l;
    }

    /** Remove o contacto e os horários dele (o histórico fica). */
    public static void removerContacto(Connection cn, int id) throws SQLException {
        try (Statement st = cn.createStatement()) {
            st.executeUpdate("DELETE FROM tb_email_agendamento WHERE fk_contacto = " + id);
            st.executeUpdate("DELETE FROM tb_email_contacto WHERE pk_contacto = " + id);
        }
    }

    // =====================================================================
    // AGENDAMENTOS
    // =====================================================================
    public static class Agendamento {

        public int id, contacto;
        public String hora = "07:00", dias = TODOS_OS_DIAS;
        public List<RelatorioWhatsApp> relatorios = new ArrayList<>();
        public boolean activo = true;
        public Timestamp ultimoEnvio;
        // só na leitura para o agendador
        public String nome, email;

        public String textoDias() {
            return EmailDados.textoDias(dias);
        }
    }

    /** "1111111" → "Todos os dias"; "1111100" → "Seg a Sex"; "1010100" → "Seg, Qua, Sex". */
    public static String textoDias(String dias) {
        if (dias == null || dias.length() != 7 || dias.indexOf('1') < 0) {
            return "Nenhum dia";
        }
        if (TODOS_OS_DIAS.equals(dias)) {
            return "Todos os dias";
        }
        int primeiro = dias.indexOf('1'), ultimo = dias.lastIndexOf('1');
        if (ultimo - primeiro >= 2 && dias.substring(primeiro, ultimo + 1).indexOf('0') < 0) {
            return DIAS_CURTOS[primeiro] + " a " + DIAS_CURTOS[ultimo];
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 7; i++) {
            if (dias.charAt(i) == '1') {
                sb.append(sb.length() == 0 ? "" : ", ").append(DIAS_CURTOS[i]);
            }
        }
        return sb.toString();
    }

    public static List<Agendamento> agendamentos(Connection cn, int contacto) throws SQLException {
        garantirTabelas(cn);
        List<Agendamento> l = new ArrayList<>();
        String sql = "SELECT pk_agendamento, fk_contacto, hora, dias, relatorios, activo, ultimo_envio"
                + " FROM tb_email_agendamento WHERE fk_contacto = ? ORDER BY hora";
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, contacto);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    l.add(lerAgendamento(rs));
                }
            }
        }
        return l;
    }

    private static Agendamento lerAgendamento(ResultSet rs) throws SQLException {
        Agendamento a = new Agendamento();
        a.id = rs.getInt("pk_agendamento");
        a.contacto = rs.getInt("fk_contacto");
        a.hora = rs.getString("hora");
        a.dias = rs.getString("dias");
        a.relatorios = RelatorioWhatsApp.daLista(rs.getString("relatorios"));
        a.activo = rs.getInt("activo") == 1;
        a.ultimoEnvio = rs.getTimestamp("ultimo_envio");
        return a;
    }

    public static void gravarAgendamento(Connection cn, Agendamento a) throws SQLException {
        String sql = a.id == 0
                ? "INSERT INTO tb_email_agendamento (hora, dias, relatorios, activo, fk_contacto) VALUES (?, ?, ?, ?, ?)"
                : "UPDATE tb_email_agendamento SET hora = ?, dias = ?, relatorios = ?, activo = ? WHERE pk_agendamento = ?";
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, a.hora);
            ps.setString(2, a.dias);
            ps.setString(3, RelatorioWhatsApp.paraLista(a.relatorios));
            ps.setInt(4, a.activo ? 1 : 0);
            ps.setInt(5, a.id == 0 ? a.contacto : a.id);
            ps.executeUpdate();
        }
    }

    public static void removerAgendamento(Connection cn, int id) throws SQLException {
        try (Statement st = cn.createStatement()) {
            st.executeUpdate("DELETE FROM tb_email_agendamento WHERE pk_agendamento = " + id);
        }
    }

    /**
     * Horários que já deviam ter sido enviados hoje e ainda não foram (hora
     * do servidor MySQL, a mesma para todos os computadores), até
     * {@code toleranciaMin} minutos de atraso (computador desligado à hora).
     */
    public static List<Agendamento> agendamentosEmFalta(Connection cn, int toleranciaMin) throws SQLException {
        garantirTabelas(cn);
        String momento = "TIMESTAMP(CURDATE(), CONCAT(a.hora, ':00'))";
        String sql = "SELECT a.*, c.nome, c.email FROM tb_email_agendamento a"
                + " JOIN tb_email_contacto c ON c.pk_contacto = a.fk_contacto"
                + " WHERE a.activo = 1 AND c.activo = 1"
                + " AND SUBSTRING(a.dias, WEEKDAY(CURDATE()) + 1, 1) = '1'"
                + " AND " + momento + " <= NOW()"
                + " AND NOW() <= " + momento + " + INTERVAL " + Math.max(1, toleranciaMin) + " MINUTE"
                + " AND (a.ultimo_envio IS NULL OR a.ultimo_envio < " + momento + ")"
                + " ORDER BY a.hora, a.pk_agendamento";
        List<Agendamento> l = new ArrayList<>();
        try (Statement st = cn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Agendamento a = lerAgendamento(rs);
                a.nome = texto(rs.getString("nome"));
                a.email = texto(rs.getString("email"));
                l.add(a);
            }
        }
        return l;
    }

    /**
     * Reserva o envio de hoje deste horário. Só um computador consegue (o
     * UPDATE é atómico): os outros que tenham o sistema aberto não repetem.
     */
    public static boolean reservarEnvio(Connection cn, int agendamento) throws SQLException {
        String momento = "TIMESTAMP(CURDATE(), CONCAT(hora, ':00'))";
        try (Statement st = cn.createStatement()) {
            return st.executeUpdate("UPDATE tb_email_agendamento SET ultimo_envio = NOW()"
                    + " WHERE pk_agendamento = " + agendamento
                    + " AND (ultimo_envio IS NULL OR ultimo_envio < " + momento + ")") == 1;
        }
    }

    /**
     * Relatórios (título) já entregues hoje a este número por este horário: envios
     * automáticos ENVIADO/SIMULADO desde a hora agendada. Numa nova tentativa só
     * se reenvia o que falhou (o contacto não recebe o mesmo relatório duas vezes).
     */
    public static java.util.Set<String> entreguesHoje(Connection cn, String email, String hora) throws SQLException {
        java.util.Set<String> s = new java.util.HashSet<>();
        try (PreparedStatement ps = cn.prepareStatement("SELECT DISTINCT relatorio FROM tb_email_envio"
                + " WHERE email = ? AND modo = ? AND estado IN (?, ?)"
                + " AND data_envio >= TIMESTAMP(CURDATE(), CONCAT(?, ':00'))")) {
            ps.setString(1, email);
            ps.setString(2, MODO_AUTOMATICO);
            ps.setString(3, ESTADO_ENVIADO);
            ps.setString(4, ESTADO_SIMULADO);
            ps.setString(5, hora);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    s.add(texto(rs.getString(1)));
                }
            }
        }
        return s;
    }

    /**
     * Desfaz a reserva de hoje (algum relatório falhou: sem internet, fornecedor em
     * baixo...): o horário volta a estar "em falta" e é tentado de novo dentro da
     * tolerância. ultimo_envio fica 1 segundo antes da hora agendada.
     */
    public static void libertarReserva(Connection cn, int agendamento) throws SQLException {
        try (Statement st = cn.createStatement()) {
            st.executeUpdate("UPDATE tb_email_agendamento"
                    + " SET ultimo_envio = TIMESTAMP(CURDATE(), CONCAT(hora, ':00')) - INTERVAL 1 SECOND"
                    + " WHERE pk_agendamento = " + agendamento);
        }
    }

    /** Chave do "sinal de vida" de quem verifica os envios (computador e hora). */
    public static final String C_SINAL = "agendador.sinal";

    /** Regista que este computador está a verificar os envios agora (hora do MySQL). */
    public static void registarSinal(Connection cn, String origem) {
        try (PreparedStatement ps = cn.prepareStatement("REPLACE INTO tb_email_config (chave, valor)"
                + " VALUES (?, CONCAT(DATE_FORMAT(NOW(), '%Y-%m-%d %H:%i:%s'), '|', ?))")) {
            ps.setString(1, C_SINAL);
            ps.setString(2, cortar(origem, 200));
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("[E-mail] sinal: " + e.getMessage());
        }
    }

    /**
     * Texto do último sinal: "Verificado há 1 min por SERVIDOR (serviço)"; ou aviso
     * se ninguém verifica há mais de 5 minutos (os envios automáticos estão parados).
     */
    public static String estadoSinal(Connection cn) {
        try (PreparedStatement ps = cn.prepareStatement("SELECT valor, TIMESTAMPDIFF(MINUTE,"
                + " STR_TO_DATE(SUBSTRING_INDEX(valor, '|', 1), '%Y-%m-%d %H:%i:%s'), NOW()) FROM tb_email_config WHERE chave = ?")) {
            ps.setString(1, C_SINAL);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next() || rs.getString(1) == null) {
                    return "Envio automático: ainda nenhum computador verificou os horários.";
                }
                String[] p = rs.getString(1).split("\\|", 2);
                int min = rs.getInt(2);
                String quem = p.length > 1 ? p[1] : "?";
                return min <= 5 ? "Envio automático activo — verificado há " + min + " min por " + quem + "."
                        : "ATENÇÃO: envio automático parado — última verificação há " + min + " min (" + quem
                        + "). Ligue o serviço de envio no servidor ou abra o sistema.";
            }
        } catch (SQLException e) {
            return "";
        }
    }

    // =====================================================================
    // HISTÓRICO
    // =====================================================================
    public static void registarEnvio(Connection cn, Integer contacto, String nome, String email, String relatorio,
            String modo, String estado, String detalhe, Integer usuario) {
        try (PreparedStatement ps = cn.prepareStatement("INSERT INTO tb_email_envio"
                + " (data_envio, fk_contacto, nome, email, relatorio, modo, estado, detalhe, fk_usuario)"
                + " VALUES (NOW(), ?, ?, ?, ?, ?, ?, ?, ?)")) {
            ps.setObject(1, contacto);
            ps.setString(2, cortar(nome, 120));
            ps.setString(3, cortar(email, 150));
            ps.setString(4, cortar(relatorio, 80));
            ps.setString(5, modo);
            ps.setString(6, estado);
            ps.setString(7, cortar(detalhe, 500));
            ps.setObject(8, usuario);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("[E-mail] histórico: " + e.getMessage());
        }
    }

    /** Linhas: data, nome, número, relatório, modo, estado, detalhe. */
    public static List<Object[]> historico(Connection cn, int dias) throws SQLException {
        garantirTabelas(cn);
        List<Object[]> l = new ArrayList<>();
        String sql = "SELECT data_envio, nome, email, relatorio, modo, estado, detalhe FROM tb_email_envio"
                + " WHERE data_envio >= CURDATE() - INTERVAL " + dias + " DAY ORDER BY data_envio DESC, pk_envio DESC LIMIT 1000";
        try (Statement st = cn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                l.add(new Object[]{rs.getTimestamp(1), texto(rs.getString(2)), legivel(rs.getString(3)),
                    texto(rs.getString(4)), texto(rs.getString(5)), texto(rs.getString(6)), texto(rs.getString(7))});
            }
        }
        return l;
    }

    static String texto(String s) {
        return s == null ? "" : s.trim();
    }

    static String cortar(String s, int max) {
        return s == null ? null : s.length() <= max ? s : s.substring(0, max - 1) + "…";
    }
}
