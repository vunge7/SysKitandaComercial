package rh.web;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import util.BDConexao;

/**
 * Dados do RH novo (JDBC, ligação própria por operação): funcionários,
 * faltas, férias, processos disciplinares, feriados e alertas.
 *
 * Só acrescenta tabelas e colunas (criadas sozinhas na primeira utilização);
 * as tabelas antigas continuam a servir os formulários antigos e o
 * processamento de salários (tb_falta em horas, pedido_feria).
 */
public final class DadosRH {

    private DadosRH() {
    }

    // =====================================================================
    // LIGAÇÕES (reaproveitadas: abrir uma ligação nova custa a leitura das
    // credenciais e o aperto de mão do MySQL, e os ecrãs abrem muitas)
    // =====================================================================
    private static final int MAX_LIVRES = 3;
    private static final long VALIDADE_MS = 5 * 60_000; // abaixo do wait_timeout habitual
    private static final java.util.ArrayDeque<Object[]> livres = new java.util.ArrayDeque<>();

    /**
     * Ligação para uma operação, com try-with-resources: o close() devolve-a
     * (em autocommit) para a próxima; as paradas há muito tempo ou inválidas
     * fecham-se de verdade.
     */
    public static Connection ligacao() throws SQLException {
        Connection real = null;
        synchronized (livres) {
            while (!livres.isEmpty()) {
                Object[] l = livres.pop();
                Connection c = (Connection) l[0];
                if (System.currentTimeMillis() - (Long) l[1] < VALIDADE_MS && valida(c)) {
                    real = c;
                    break;
                }
                fecharSemErro(c);
            }
        }
        if (real == null) {
            real = BDConexao.novaLigacao();
        }
        garantirEstrutura(real);
        return devolvivel(real);
    }

    private static boolean valida(Connection c) {
        try {
            return !c.isClosed() && c.isValid(1);
        } catch (SQLException e) {
            return false;
        }
    }

    private static void fecharSemErro(Connection c) {
        try {
            c.close();
        } catch (SQLException e) {
            // já estava fechada
        }
    }

    /** Embrulho cujo close() devolve a ligação em vez de a fechar. */
    private static Connection devolvivel(Connection real) {
        boolean[] fechada = {false};
        return (Connection) java.lang.reflect.Proxy.newProxyInstance(DadosRH.class.getClassLoader(), new Class<?>[]{Connection.class},
                (proxy, m, args) -> {
                    switch (m.getName()) {
                        case "close":
                            if (!fechada[0]) {
                                fechada[0] = true;
                                devolver(real);
                            }
                            return null;
                        case "isClosed":
                            return fechada[0] || real.isClosed();
                        case "equals":
                            return proxy == args[0];
                        case "hashCode":
                            return System.identityHashCode(proxy);
                        default:
                            if (fechada[0]) {
                                throw new SQLException("Ligação já fechada");
                            }
                            try {
                                return m.invoke(real, args);
                            } catch (java.lang.reflect.InvocationTargetException e) {
                                throw e.getCause();
                            }
                    }
                });
    }

    private static void devolver(Connection c) {
        try {
            if (c.isClosed()) {
                return;
            }
            if (!c.getAutoCommit()) {
                c.rollback(); // transacção deixada a meio: não passa para a operação seguinte
                c.setAutoCommit(true);
            }
        } catch (SQLException e) {
            fecharSemErro(c);
            return;
        }
        synchronized (livres) {
            if (livres.size() < MAX_LIVRES) {
                livres.push(new Object[]{c, System.currentTimeMillis()});
                return;
            }
        }
        fecharSemErro(c);
    }

    // =====================================================================
    // ESTRUTURA
    // =====================================================================
    private static volatile boolean estruturaVerificada;

    public static void garantirEstrutura(Connection cn) throws SQLException {
        if (estruturaVerificada) {
            return;
        }
        synchronized (DadosRH.class) {
            if (estruturaVerificada) {
                return;
            }
            try (Statement st = cn.createStatement()) {
                st.execute("CREATE TABLE IF NOT EXISTS rh_feriado (data DATE NOT NULL PRIMARY KEY,"
                        + " designacao VARCHAR(120) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=utf8");
                st.execute("CREATE TABLE IF NOT EXISTS rh_tipo_falta (pk INT NOT NULL AUTO_INCREMENT PRIMARY KEY,"
                        + " codigo VARCHAR(10) NOT NULL, designacao VARCHAR(120) NOT NULL,"
                        + " justificada TINYINT(1) NOT NULL DEFAULT 0, remunerada TINYINT(1) NOT NULL DEFAULT 0,"
                        + " activo TINYINT(1) NOT NULL DEFAULT 1) ENGINE=InnoDB DEFAULT CHARSET=utf8");
                st.execute("CREATE TABLE IF NOT EXISTS rh_ferias_direito (fk_funcionario INT NOT NULL, ano INT NOT NULL,"
                        + " dias INT NOT NULL, PRIMARY KEY (fk_funcionario, ano)) ENGINE=InnoDB DEFAULT CHARSET=utf8");
                st.execute("CREATE TABLE IF NOT EXISTS rh_processo_disciplinar (pk INT NOT NULL AUTO_INCREMENT PRIMARY KEY,"
                        + " numero VARCHAR(20) NOT NULL, fk_funcionario INT NOT NULL, data_ocorrencia DATE NULL,"
                        + " data_conhecimento DATE NULL, data_abertura DATETIME NOT NULL, factos TEXT NULL,"
                        + " testemunhas VARCHAR(500) NULL, fase VARCHAR(30) NOT NULL, data_nota_culpa DATE NULL,"
                        + " data_entrevista DATETIME NULL, defesa TEXT NULL, decisao TEXT NULL, sancao VARCHAR(60) NULL,"
                        + " dias_sancao INT NULL, data_decisao DATE NULL, estado VARCHAR(20) NOT NULL,"
                        + " fk_usuario INT NULL, KEY (fk_funcionario)) ENGINE=InnoDB DEFAULT CHARSET=utf8");
                st.execute("CREATE TABLE IF NOT EXISTS rh_processo_evento (pk INT NOT NULL AUTO_INCREMENT PRIMARY KEY,"
                        + " fk_processo INT NOT NULL, data_hora DATETIME NOT NULL, tipo VARCHAR(40) NOT NULL,"
                        + " descricao TEXT NULL, fk_usuario INT NULL, KEY (fk_processo)) ENGINE=InnoDB DEFAULT CHARSET=utf8");
                st.execute("CREATE TABLE IF NOT EXISTS rh_config (chave VARCHAR(60) NOT NULL PRIMARY KEY,"
                        + " valor VARCHAR(500) NULL) ENGINE=InnoDB DEFAULT CHARSET=utf8");
                // férias: estado e aprovação (os pedidos antigos, sem estado, contam como aprovados)
                for (String[] c : new String[][]{
                    {"estado", "VARCHAR(20) NULL"}, {"ano_referencia", "INT NULL"}, {"observacao", "VARCHAR(500) NULL"},
                    {"fk_aprovador", "INT NULL"}, {"data_aprovacao", "DATETIME NULL"}}) {
                    if (!temColuna(cn, "pedido_feria", c[0])) {
                        st.execute("ALTER TABLE pedido_feria ADD COLUMN " + c[0] + " " + c[1]);
                    }
                }
                // faltas: o tipo (código), como no Primavera; o salário continua a ler n_falta e justificada
                if (!temColuna(cn, "tb_falta", "fk_tipo_falta")) {
                    st.execute("ALTER TABLE tb_falta ADD COLUMN fk_tipo_falta INT NULL");
                }
            }
            tiposFaltaIniciais(cn);
            estruturaVerificada = true;
        }
    }

    private static boolean temColuna(Connection cn, String tabela, String coluna) throws SQLException {
        try (PreparedStatement ps = cn.prepareStatement("SELECT COUNT(*) FROM information_schema.COLUMNS"
                + " WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = ? AND COLUMN_NAME = ?")) {
            ps.setString(1, tabela);
            ps.setString(2, coluna);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    /** Tipos de falta por omissão (editáveis em Tabelas): ficam só se a tabela estiver vazia. */
    private static void tiposFaltaIniciais(Connection cn) throws SQLException {
        try (Statement st = cn.createStatement(); ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM rh_tipo_falta")) {
            if (rs.next() && rs.getInt(1) > 0) {
                return;
            }
        }
        Object[][] tipos = {
            {"FI", "Falta injustificada", 0, 0},
            {"DO", "Doença (com atestado)", 1, 0},
            {"AF", "Assistência à família", 1, 0},
            {"NJ", "Nojo (falecimento de familiar)", 1, 1},
            {"CA", "Casamento", 1, 1},
            {"PA", "Parto do cônjuge", 1, 1},
            {"EX", "Prestação de provas / exames", 1, 1},
            {"OL", "Obrigação legal (tribunal, eleições...)", 1, 1},
            {"AU", "Autorizada pela entidade empregadora", 1, 1},
            {"AT", "Atraso", 0, 0}};
        try (PreparedStatement ps = cn.prepareStatement(
                "INSERT INTO rh_tipo_falta (codigo, designacao, justificada, remunerada, activo) VALUES (?, ?, ?, ?, 1)")) {
            for (Object[] t : tipos) {
                ps.setString(1, (String) t[0]);
                ps.setString(2, (String) t[1]);
                ps.setInt(3, (Integer) t[2]);
                ps.setInt(4, (Integer) t[3]);
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    // =====================================================================
    // CONFIGURAÇÃO (prazos, dias de férias)
    // =====================================================================
    public static String config(Connection cn, String chave, String omissao) {
        try (PreparedStatement ps = cn.prepareStatement("SELECT valor FROM rh_config WHERE chave = ?")) {
            ps.setString(1, chave);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next() && rs.getString(1) != null && !rs.getString(1).trim().isEmpty()) {
                    return rs.getString(1).trim();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return omissao;
    }

    public static int configInt(Connection cn, String chave, int omissao) {
        try {
            return Integer.parseInt(config(cn, chave, String.valueOf(omissao)));
        } catch (NumberFormatException e) {
            return omissao;
        }
    }

    public static void gravarConfig(Connection cn, String chave, String valor) throws SQLException {
        try (PreparedStatement ps = cn.prepareStatement("REPLACE INTO rh_config (chave, valor) VALUES (?, ?)")) {
            ps.setString(1, chave);
            ps.setString(2, valor);
            ps.executeUpdate();
        }
    }

    public static final String C_DIAS_FERIAS = "ferias.dias_ano";
    public static final String C_ANTECEDENCIA_ENTREVISTA = "disciplinar.antecedencia_entrevista_dias_uteis";
    public static final String C_PRAZO_DECISAO = "disciplinar.prazo_decisao_dias";
    public static final String C_PRAZO_INSTAURACAO = "disciplinar.prazo_instauracao_dias";

    // =====================================================================
    // FUNCIONÁRIOS
    // =====================================================================
    /** Um funcionário, com o que as listas e os alertas precisam. */
    public static final class Funcionario {

        public int id;
        public String numero = "", nome = "", funcao = "", departamento = "", telefone = "", sexo = "", tipoContrato = "";
        public LocalDate nascimento, admissao, fimContrato, validadeDoc;
        public boolean activo = true;
        public double salario;
        public int diasUteisMes = 22;

        public String rotulo() {
            return (numero.isEmpty() ? "" : numero + " · ") + nome;
        }

        @Override
        public String toString() {
            return rotulo();
        }
    }

    private static final String SQL_FUNCIONARIOS = "SELECT f.idFuncionario, f.numero_funcionario, f.nome, COALESCE(fn.designacao, ''),"
            + " COALESCE(d.designacao, ''), COALESCE(NULLIF(f.telefone, ''), f.telefone_1, ''), f.sexo, f.tipo_contrato,"
            + " f.data_nascimento, f.data_inicio_contrato, f.data_fim_contrato,"
            + " COALESCE(f.data_validade_docID, f.data_validade_bi), f.activo, f.idStatusFK, f.conta_fechada,"
            + " (SELECT s.valor_tempo FROM tb_salario s WHERE s.idFuncionarioFK = f.idFuncionario ORDER BY s.idSalarioFK DESC LIMIT 1),"
            + " COALESCE(m.dias_uteis_trabalho, 22)"
            + " FROM tb_funcionario f LEFT JOIN tb_funcao fn ON fn.pk_funcao = f.fk_funcao"
            + " LEFT JOIN tb_departamento d ON d.pk_departamento = f.fk_departamento"
            + " LEFT JOIN modalidade m ON m.pk_modalidade = f.fk_modalidade";

    /** Todos os funcionários (activos primeiro), ordenados pelo nome. */
    public static List<Funcionario> funcionarios(Connection cn, boolean soActivos) throws SQLException {
        List<Funcionario> l = new ArrayList<>();
        try (Statement st = cn.createStatement(); ResultSet rs = st.executeQuery(SQL_FUNCIONARIOS + " ORDER BY f.nome")) {
            while (rs.next()) {
                Funcionario f = new Funcionario();
                f.id = rs.getInt(1);
                f.numero = t(rs.getString(2));
                f.nome = t(rs.getString(3));
                f.funcao = t(rs.getString(4));
                f.departamento = t(rs.getString(5));
                f.telefone = t(rs.getString(6));
                f.sexo = t(rs.getString(7));
                f.tipoContrato = t(rs.getString(8));
                f.nascimento = data(rs.getDate(9));
                f.admissao = data(rs.getDate(10));
                f.fimContrato = data(rs.getDate(11));
                f.validadeDoc = data(rs.getDate(12));
                int activo = rs.getInt(13);
                boolean semActivo = rs.wasNull();
                int status = rs.getInt(14);
                int fechada = rs.getInt(15);
                f.activo = (semActivo || activo == 1) && status != 2 && fechada != 1;
                f.salario = rs.getDouble(16);
                f.diasUteisMes = rs.getInt(17);
                if (!soActivos || f.activo) {
                    l.add(f);
                }
            }
        }
        return l;
    }

    // =====================================================================
    // FALTAS (tb_falta: uma linha por dia, em horas, como o salário as lê)
    // =====================================================================
    public static final class TipoFalta {

        public int id;
        public String codigo, designacao;
        public boolean justificada, remunerada, activo = true;

        @Override
        public String toString() {
            return codigo + " · " + designacao;
        }
    }

    public static List<TipoFalta> tiposFalta(Connection cn, boolean soActivos) throws SQLException {
        List<TipoFalta> l = new ArrayList<>();
        try (Statement st = cn.createStatement(); ResultSet rs = st.executeQuery(
                "SELECT pk, codigo, designacao, justificada, remunerada, activo FROM rh_tipo_falta"
                + (soActivos ? " WHERE activo = 1" : "") + " ORDER BY codigo")) {
            while (rs.next()) {
                TipoFalta t = new TipoFalta();
                t.id = rs.getInt(1);
                t.codigo = rs.getString(2);
                t.designacao = rs.getString(3);
                t.justificada = rs.getInt(4) == 1;
                t.remunerada = rs.getInt(5) == 1;
                t.activo = rs.getInt(6) == 1;
                l.add(t);
            }
        }
        return l;
    }

    public static void gravarTipoFalta(Connection cn, TipoFalta t) throws SQLException {
        String sql = t.id > 0
                ? "UPDATE rh_tipo_falta SET codigo = ?, designacao = ?, justificada = ?, remunerada = ?, activo = ? WHERE pk = ?"
                : "INSERT INTO rh_tipo_falta (codigo, designacao, justificada, remunerada, activo) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, t.codigo);
            ps.setString(2, t.designacao);
            ps.setInt(3, t.justificada ? 1 : 0);
            ps.setInt(4, t.remunerada ? 1 : 0);
            ps.setInt(5, t.activo ? 1 : 0);
            if (t.id > 0) {
                ps.setInt(6, t.id);
            }
            ps.executeUpdate();
        }
    }

    /** Uma falta registada (um dia). */
    public static final class Falta {

        public int id, idFuncionario, idTipo;
        public String funcionario = "", numero = "", departamento = "", tipo = "", descricao = "", justificacao = "";
        public LocalDate data;
        public double horas;
        public boolean justificada;
    }

    public static List<Falta> faltas(Connection cn, LocalDate de, LocalDate ate, Integer idFuncionario) throws SQLException {
        List<Falta> l = new ArrayList<>();
        String sql = "SELECT x.idFaltaPK, x.idFuncionarioFK, f.nome, f.numero_funcionario, COALESCE(d.designacao, ''),"
                + " x.data, x.n_falta, x.justificada_injustificada, COALESCE(x.descricao_falta, ''),"
                + " COALESCE(x.descricao_justificativa, ''), COALESCE(x.fk_tipo_falta, 0),"
                + " COALESCE(CONCAT(t.codigo, ' · ', t.designacao), IF(x.justificada_injustificada = 1, 'Justificada', 'Injustificada'))"
                + " FROM tb_falta x JOIN tb_funcionario f ON f.idFuncionario = x.idFuncionarioFK"
                + " LEFT JOIN tb_departamento d ON d.pk_departamento = f.fk_departamento"
                + " LEFT JOIN rh_tipo_falta t ON t.pk = x.fk_tipo_falta"
                + " WHERE x.data BETWEEN ? AND ?" + (idFuncionario != null ? " AND x.idFuncionarioFK = ?" : "")
                + " ORDER BY x.data DESC, f.nome";
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(de));
            ps.setDate(2, Date.valueOf(ate));
            if (idFuncionario != null) {
                ps.setInt(3, idFuncionario);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Falta f = new Falta();
                    f.id = rs.getInt(1);
                    f.idFuncionario = rs.getInt(2);
                    f.funcionario = t(rs.getString(3));
                    f.numero = t(rs.getString(4));
                    f.departamento = t(rs.getString(5));
                    f.data = data(rs.getDate(6));
                    f.horas = rs.getDouble(7);
                    f.justificada = rs.getInt(8) == 1;
                    f.descricao = t(rs.getString(9));
                    f.justificacao = t(rs.getString(10));
                    f.idTipo = rs.getInt(11);
                    f.tipo = t(rs.getString(12));
                    l.add(f);
                }
            }
        }
        return l;
    }

    /**
     * Regista faltas: uma linha por funcionário e por dia útil do intervalo
     * (como o formulário antigo), numa transacção. Devolve quantas ficaram.
     * Os dias que o funcionário já tem falta registada são saltados.
     */
    public static int registarFaltas(Connection cn, List<Integer> funcionarios, List<LocalDate> dias, double horasPorDia,
            TipoFalta tipo, String descricao) throws SQLException {
        boolean auto = cn.getAutoCommit();
        cn.setAutoCommit(false);
        int n = 0;
        // as faltas que já existem no intervalo, numa só leitura (em vez de uma por funcionário e dia)
        java.util.Set<String> existentes = new java.util.HashSet<>();
        if (!funcionarios.isEmpty() && !dias.isEmpty()) {
            StringBuilder ids = new StringBuilder();
            for (int idF : funcionarios) {
                ids.append(ids.length() == 0 ? "" : ",").append(idF);
            }
            try (PreparedStatement ps = cn.prepareStatement("SELECT idFuncionarioFK, data FROM tb_falta WHERE data BETWEEN ? AND ?"
                    + " AND idFuncionarioFK IN (" + ids + ")")) {
                ps.setDate(1, Date.valueOf(java.util.Collections.min(dias)));
                ps.setDate(2, Date.valueOf(java.util.Collections.max(dias)));
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        existentes.add(rs.getInt(1) + "|" + rs.getDate(2).toLocalDate());
                    }
                }
            }
        }
        try (PreparedStatement ins = cn.prepareStatement("INSERT INTO tb_falta (n_falta, data, hora, descricao_falta,"
                        + " idFuncionarioFK, justificada_injustificada, descricao_justificativa, data_justificativa, fk_tipo_falta)"
                        + " VALUES (?, ?, '00:00:00', ?, ?, ?, ?, ?, ?)")) {
            for (int idF : funcionarios) {
                for (LocalDate d : dias) {
                    if (!existentes.add(idF + "|" + d)) {
                        continue;
                    }
                    ins.setDouble(1, horasPorDia);
                    ins.setDate(2, Date.valueOf(d));
                    ins.setString(3, descricao == null ? "" : descricao);
                    ins.setInt(4, idF);
                    ins.setInt(5, tipo.justificada ? 1 : 0);
                    ins.setString(6, tipo.justificada ? tipo.designacao : null);
                    if (tipo.justificada) {
                        ins.setDate(7, Date.valueOf(LocalDate.now()));
                    } else {
                        ins.setNull(7, java.sql.Types.DATE);
                    }
                    ins.setInt(8, tipo.id);
                    ins.executeUpdate();
                    n++;
                }
            }
            cn.commit();
            return n;
        } catch (SQLException | RuntimeException e) {
            cn.rollback();
            throw e;
        } finally {
            cn.setAutoCommit(auto);
        }
    }

    /** Justifica (ou volta a injustificar) faltas já registadas, com o tipo escolhido. */
    public static void justificar(Connection cn, List<Integer> faltas, TipoFalta tipo, String motivo) throws SQLException {
        try (PreparedStatement ps = cn.prepareStatement("UPDATE tb_falta SET justificada_injustificada = ?,"
                + " descricao_justificativa = ?, data_justificativa = ?, fk_tipo_falta = ? WHERE idFaltaPK = ?")) {
            for (int id : faltas) {
                ps.setInt(1, tipo.justificada ? 1 : 0);
                ps.setString(2, tipo.justificada ? (motivo == null || motivo.trim().isEmpty() ? tipo.designacao : motivo.trim()) : null);
                if (tipo.justificada) {
                    ps.setDate(3, Date.valueOf(LocalDate.now()));
                } else {
                    ps.setNull(3, java.sql.Types.DATE);
                }
                ps.setInt(4, tipo.id);
                ps.setInt(5, id);
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    public static void eliminarFaltas(Connection cn, List<Integer> faltas) throws SQLException {
        try (PreparedStatement ps = cn.prepareStatement("DELETE FROM tb_falta WHERE idFaltaPK = ?")) {
            for (int id : faltas) {
                ps.setInt(1, id);
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    // =====================================================================
    // FÉRIAS (pedido_feria)
    // =====================================================================
    public static final String PEDIDO = "Pedido", APROVADO = "Aprovado", RECUSADO = "Recusado", CANCELADO = "Cancelado";

    public static final class Ferias {

        public int id, idFuncionario, dias, ano;
        public String funcionario = "", numero = "", departamento = "", estado = APROVADO, observacao = "";
        public LocalDate inicio, fim;

        /** Conta para o saldo (pedidos e aprovados; recusados e cancelados não). */
        public boolean conta() {
            return !RECUSADO.equals(estado) && !CANCELADO.equals(estado);
        }

        public String situacao() {
            LocalDate hoje = LocalDate.now();
            if (!APROVADO.equals(estado)) {
                return estado;
            }
            if (inicio != null && fim != null && !hoje.isBefore(inicio) && !hoje.isAfter(fim)) {
                return "Em gozo";
            }
            return fim != null && fim.isBefore(hoje) ? "Gozadas" : "Marcadas";
        }
    }

    public static List<Ferias> ferias(Connection cn, int ano, Integer idFuncionario) throws SQLException {
        List<Ferias> l = new ArrayList<>();
        String sql = "SELECT p.pk_pedido_feria, p.fk_funcionario, f.nome, f.numero_funcionario, COALESCE(d.designacao, ''),"
                + " p.data_inicio, p.data_fim, COALESCE(p.dias_ferias, 0), COALESCE(p.estado, 'Aprovado'),"
                + " COALESCE(p.ano_referencia, YEAR(p.data_inicio)), COALESCE(p.observacao, '')"
                + " FROM pedido_feria p JOIN tb_funcionario f ON f.idFuncionario = p.fk_funcionario"
                + " LEFT JOIN tb_departamento d ON d.pk_departamento = f.fk_departamento"
                + " WHERE COALESCE(p.ano_referencia, YEAR(p.data_inicio)) = ?"
                + (idFuncionario != null ? " AND p.fk_funcionario = ?" : "") + " ORDER BY p.data_inicio, f.nome";
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, ano);
            if (idFuncionario != null) {
                ps.setInt(2, idFuncionario);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Ferias f = new Ferias();
                    f.id = rs.getInt(1);
                    f.idFuncionario = rs.getInt(2);
                    f.funcionario = t(rs.getString(3));
                    f.numero = t(rs.getString(4));
                    f.departamento = t(rs.getString(5));
                    f.inicio = data(rs.getDate(6));
                    f.fim = data(rs.getDate(7));
                    f.dias = rs.getInt(8);
                    f.estado = t(rs.getString(9));
                    f.ano = rs.getInt(10);
                    f.observacao = t(rs.getString(11));
                    l.add(f);
                }
            }
        }
        return l;
    }

    /**
     * Dias de férias a que o funcionário tem direito no ano: o valor fixado à
     * mão (rh_ferias_direito) ou a regra por omissão — 22 dias úteis; no ano de
     * admissão, 2 dias úteis por mês completo de trabalho, até ao máximo.
     */
    public static int direito(Connection cn, Funcionario f, int ano) throws SQLException {
        try (PreparedStatement ps = cn.prepareStatement("SELECT dias FROM rh_ferias_direito WHERE fk_funcionario = ? AND ano = ?")) {
            ps.setInt(1, f.id);
            ps.setInt(2, ano);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return direitoPorOmissao(f, ano, configInt(cn, C_DIAS_FERIAS, 22));
    }

    /** O direito de vários funcionários num ano, com uma só leitura dos valores fixados à mão. */
    public static Map<Integer, Integer> direitos(Connection cn, List<Funcionario> fs, int ano) throws SQLException {
        Map<Integer, Integer> fixados = new java.util.HashMap<>();
        try (PreparedStatement ps = cn.prepareStatement("SELECT fk_funcionario, dias FROM rh_ferias_direito WHERE ano = ?")) {
            ps.setInt(1, ano);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    fixados.put(rs.getInt(1), rs.getInt(2));
                }
            }
        }
        int maximo = configInt(cn, C_DIAS_FERIAS, 22);
        Map<Integer, Integer> m = new java.util.HashMap<>();
        for (Funcionario f : fs) {
            m.put(f.id, fixados.containsKey(f.id) ? fixados.get(f.id) : direitoPorOmissao(f, ano, maximo));
        }
        return m;
    }

    private static int direitoPorOmissao(Funcionario f, int ano, int maximo) {
        if (f.admissao == null || f.admissao.getYear() < ano) {
            return maximo;
        }
        if (f.admissao.getYear() > ano) {
            return 0;
        }
        LocalDate inicioContagem = f.admissao.getDayOfMonth() == 1 ? f.admissao : f.admissao.withDayOfMonth(1).plusMonths(1);
        int meses = Math.max(0, 12 - inicioContagem.getMonthValue() + 1);
        return Math.min(maximo, meses * 2);
    }

    public static void gravarDireito(Connection cn, int idFuncionario, int ano, int dias) throws SQLException {
        try (PreparedStatement ps = cn.prepareStatement("REPLACE INTO rh_ferias_direito (fk_funcionario, ano, dias) VALUES (?, ?, ?)")) {
            ps.setInt(1, idFuncionario);
            ps.setInt(2, ano);
            ps.setInt(3, dias);
            ps.executeUpdate();
        }
    }

    public static int gravarFerias(Connection cn, Ferias f, int idUsuario) throws SQLException {
        if (f.id > 0) {
            try (PreparedStatement ps = cn.prepareStatement("UPDATE pedido_feria SET data_inicio = ?, data_fim = ?, dias_ferias = ?,"
                    + " estado = ?, ano_referencia = ?, observacao = ? WHERE pk_pedido_feria = ?")) {
                ps.setDate(1, Date.valueOf(f.inicio));
                ps.setDate(2, Date.valueOf(f.fim));
                ps.setInt(3, f.dias);
                ps.setString(4, f.estado);
                ps.setInt(5, f.ano);
                ps.setString(6, f.observacao);
                ps.setInt(7, f.id);
                ps.executeUpdate();
                return f.id;
            }
        }
        try (PreparedStatement ps = cn.prepareStatement("INSERT INTO pedido_feria (data_inicio, data_fim, dias_ferias, data_registro,"
                + " fk_funcionario, fk_usuario, estado, ano_referencia, observacao) VALUES (?, ?, ?, NOW(), ?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS)) {
            ps.setDate(1, Date.valueOf(f.inicio));
            ps.setDate(2, Date.valueOf(f.fim));
            ps.setInt(3, f.dias);
            ps.setInt(4, f.idFuncionario);
            ps.setInt(5, idUsuario);
            ps.setString(6, f.estado);
            ps.setInt(7, f.ano);
            ps.setString(8, f.observacao);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    public static void mudarEstadoFerias(Connection cn, int id, String estado, int idUsuario) throws SQLException {
        try (PreparedStatement ps = cn.prepareStatement("UPDATE pedido_feria SET estado = ?, fk_aprovador = ?, data_aprovacao = NOW()"
                + " WHERE pk_pedido_feria = ?")) {
            ps.setString(1, estado);
            ps.setInt(2, idUsuario);
            ps.setInt(3, id);
            ps.executeUpdate();
        }
    }

    public static void eliminarFerias(Connection cn, int id) throws SQLException {
        try (PreparedStatement ps = cn.prepareStatement("DELETE FROM pedido_feria WHERE pk_pedido_feria = ?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    // =====================================================================
    // PROCESSOS DISCIPLINARES
    // =====================================================================
    public static final String FASE_INSTAURADO = "Instaurado", FASE_CONVOCADO = "Nota de culpa enviada",
            FASE_DEFESA = "Defesa recebida", FASE_DECIDIDO = "Decidido";
    public static final String ABERTO = "Aberto", CONCLUIDO = "Concluído", ARQUIVADO = "Arquivado";

    public static final String[] SANCOES = {
        "Admoestação simples", "Admoestação registada", "Despromoção temporária de categoria",
        "Redução temporária do salário", "Despedimento disciplinar", "Sem sanção (arquivamento)"};

    public static final class Processo {

        public int id, idFuncionario;
        public String numero = "", funcionario = "", numeroFuncionario = "", departamento = "", funcao = "";
        public LocalDate ocorrencia, conhecimento, notaCulpa, decisaoData;
        public java.time.LocalDateTime abertura, entrevista;
        public String factos = "", testemunhas = "", fase = FASE_INSTAURADO, defesa = "", decisao = "", sancao = "", estado = ABERTO;
        public Integer diasSancao;
    }

    public static final class Evento {

        public java.time.LocalDateTime quando;
        public String tipo = "", descricao = "", utilizador = "";
    }

    public static List<Processo> processos(Connection cn, boolean soAbertos) throws SQLException {
        List<Processo> l = new ArrayList<>();
        try (Statement st = cn.createStatement(); ResultSet rs = st.executeQuery(
                "SELECT p.pk, p.numero, p.fk_funcionario, f.nome, f.numero_funcionario, COALESCE(d.designacao, ''), COALESCE(fn.designacao, ''),"
                + " p.data_ocorrencia, p.data_conhecimento, p.data_abertura, p.factos, p.testemunhas, p.fase, p.data_nota_culpa,"
                + " p.data_entrevista, p.defesa, p.decisao, p.sancao, p.dias_sancao, p.data_decisao, p.estado"
                + " FROM rh_processo_disciplinar p JOIN tb_funcionario f ON f.idFuncionario = p.fk_funcionario"
                + " LEFT JOIN tb_departamento d ON d.pk_departamento = f.fk_departamento"
                + " LEFT JOIN tb_funcao fn ON fn.pk_funcao = f.fk_funcao"
                + (soAbertos ? " WHERE p.estado = 'Aberto'" : "") + " ORDER BY p.data_abertura DESC")) {
            while (rs.next()) {
                Processo p = new Processo();
                p.id = rs.getInt(1);
                p.numero = t(rs.getString(2));
                p.idFuncionario = rs.getInt(3);
                p.funcionario = t(rs.getString(4));
                p.numeroFuncionario = t(rs.getString(5));
                p.departamento = t(rs.getString(6));
                p.funcao = t(rs.getString(7));
                p.ocorrencia = data(rs.getDate(8));
                p.conhecimento = data(rs.getDate(9));
                p.abertura = dataHora(rs.getTimestamp(10));
                p.factos = t(rs.getString(11));
                p.testemunhas = t(rs.getString(12));
                p.fase = t(rs.getString(13));
                p.notaCulpa = data(rs.getDate(14));
                p.entrevista = dataHora(rs.getTimestamp(15));
                p.defesa = t(rs.getString(16));
                p.decisao = t(rs.getString(17));
                p.sancao = t(rs.getString(18));
                int dias = rs.getInt(19);
                p.diasSancao = rs.wasNull() ? null : dias;
                p.decisaoData = data(rs.getDate(20));
                p.estado = t(rs.getString(21));
                l.add(p);
            }
        }
        return l;
    }

    /** Número do processo: PD/ano/sequência do ano. */
    private static String proximoNumero(Connection cn) throws SQLException {
        int ano = LocalDate.now().getYear();
        try (PreparedStatement ps = cn.prepareStatement("SELECT COUNT(*) FROM rh_processo_disciplinar WHERE YEAR(data_abertura) = ?")) {
            ps.setInt(1, ano);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return "PD " + ano + "/" + (rs.getInt(1) + 1);
            }
        }
    }

    public static Processo gravarProcesso(Connection cn, Processo p, int idUsuario, String evento) throws SQLException {
        boolean auto = cn.getAutoCommit();
        cn.setAutoCommit(false);
        try {
            if (p.id == 0) {
                p.numero = proximoNumero(cn);
                p.abertura = java.time.LocalDateTime.now();
                try (PreparedStatement ps = cn.prepareStatement("INSERT INTO rh_processo_disciplinar (numero, fk_funcionario,"
                        + " data_abertura, fase, estado, fk_usuario) VALUES (?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
                    ps.setString(1, p.numero);
                    ps.setInt(2, p.idFuncionario);
                    ps.setTimestamp(3, Timestamp.valueOf(p.abertura));
                    ps.setString(4, p.fase);
                    ps.setString(5, p.estado);
                    ps.setInt(6, idUsuario);
                    ps.executeUpdate();
                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        rs.next();
                        p.id = rs.getInt(1);
                    }
                }
            }
            try (PreparedStatement ps = cn.prepareStatement("UPDATE rh_processo_disciplinar SET fk_funcionario = ?, data_ocorrencia = ?,"
                    + " data_conhecimento = ?, factos = ?, testemunhas = ?, fase = ?, data_nota_culpa = ?, data_entrevista = ?,"
                    + " defesa = ?, decisao = ?, sancao = ?, dias_sancao = ?, data_decisao = ?, estado = ? WHERE pk = ?")) {
                ps.setInt(1, p.idFuncionario);
                setData(ps, 2, p.ocorrencia);
                setData(ps, 3, p.conhecimento);
                ps.setString(4, p.factos);
                ps.setString(5, p.testemunhas);
                ps.setString(6, p.fase);
                setData(ps, 7, p.notaCulpa);
                if (p.entrevista == null) {
                    ps.setNull(8, java.sql.Types.TIMESTAMP);
                } else {
                    ps.setTimestamp(8, Timestamp.valueOf(p.entrevista));
                }
                ps.setString(9, p.defesa);
                ps.setString(10, p.decisao);
                ps.setString(11, p.sancao);
                if (p.diasSancao == null) {
                    ps.setNull(12, java.sql.Types.INTEGER);
                } else {
                    ps.setInt(12, p.diasSancao);
                }
                setData(ps, 13, p.decisaoData);
                ps.setString(14, p.estado);
                ps.setInt(15, p.id);
                ps.executeUpdate();
            }
            if (evento != null) {
                registarEvento(cn, p.id, evento.split("\\|", 2)[0], evento.contains("|") ? evento.split("\\|", 2)[1] : "", idUsuario);
            }
            cn.commit();
            return p;
        } catch (SQLException | RuntimeException e) {
            cn.rollback();
            throw e;
        } finally {
            cn.setAutoCommit(auto);
        }
    }

    public static void registarEvento(Connection cn, int idProcesso, String tipo, String descricao, int idUsuario) throws SQLException {
        try (PreparedStatement ps = cn.prepareStatement("INSERT INTO rh_processo_evento (fk_processo, data_hora, tipo, descricao, fk_usuario)"
                + " VALUES (?, NOW(), ?, ?, ?)")) {
            ps.setInt(1, idProcesso);
            ps.setString(2, tipo);
            ps.setString(3, descricao);
            ps.setInt(4, idUsuario);
            ps.executeUpdate();
        }
    }

    public static List<Evento> eventos(Connection cn, int idProcesso) throws SQLException {
        List<Evento> l = new ArrayList<>();
        try (PreparedStatement ps = cn.prepareStatement("SELECT e.data_hora, e.tipo, COALESCE(e.descricao, ''), COALESCE(u.nome, '')"
                + " FROM rh_processo_evento e LEFT JOIN tb_usuario u ON u.codigo = e.fk_usuario WHERE e.fk_processo = ? ORDER BY e.data_hora, e.pk")) {
            ps.setInt(1, idProcesso);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Evento e = new Evento();
                    e.quando = dataHora(rs.getTimestamp(1));
                    e.tipo = t(rs.getString(2));
                    e.descricao = t(rs.getString(3));
                    e.utilizador = t(rs.getString(4));
                    l.add(e);
                }
            }
        }
        return l;
    }

    // =====================================================================
    // HISTÓRICO DO FUNCIONÁRIO (linha do tempo)
    // =====================================================================
    /** Data, tipo, descrição — admissão, contrato, faltas, férias, processos, recibos. */
    public static List<String[]> historico(Connection cn, Funcionario f) throws SQLException {
        List<String[]> l = new ArrayList<>();
        if (f.admissao != null) {
            l.add(new String[]{f.admissao.toString(), "Admissão", "Contrato " + f.tipoContrato.toLowerCase()});
        }
        if (f.fimContrato != null) {
            l.add(new String[]{f.fimContrato.toString(), "Contrato", "Fim do contrato"});
        }
        try (PreparedStatement ps = cn.prepareStatement("SELECT x.data, x.n_falta, x.justificada_injustificada, COALESCE(t.designacao, x.descricao_justificativa, '')"
                + " FROM tb_falta x LEFT JOIN rh_tipo_falta t ON t.pk = x.fk_tipo_falta WHERE x.idFuncionarioFK = ?")) {
            ps.setInt(1, f.id);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    l.add(new String[]{String.valueOf(rs.getDate(1)), rs.getInt(3) == 1 ? "Falta justificada" : "Falta injustificada",
                        UiRH.horas(rs.getDouble(2)) + (t(rs.getString(4)).isEmpty() ? "" : " · " + t(rs.getString(4)))});
                }
            }
        }
        try (PreparedStatement ps = cn.prepareStatement("SELECT data_inicio, data_fim, dias_ferias, COALESCE(estado, 'Aprovado')"
                + " FROM pedido_feria WHERE fk_funcionario = ?")) {
            ps.setInt(1, f.id);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    l.add(new String[]{String.valueOf(rs.getDate(1)), "Férias (" + rs.getString(4).toLowerCase() + ")",
                        rs.getInt(3) + " dias úteis, até " + UiRH.data(data(rs.getDate(2)))});
                }
            }
        }
        try (PreparedStatement ps = cn.prepareStatement("SELECT DATE(data_abertura), numero, fase, COALESCE(sancao, ''), estado"
                + " FROM rh_processo_disciplinar WHERE fk_funcionario = ?")) {
            ps.setInt(1, f.id);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    l.add(new String[]{String.valueOf(rs.getDate(1)), "Processo disciplinar",
                        rs.getString(2) + " · " + rs.getString(3) + (t(rs.getString(4)).isEmpty() ? "" : " · " + rs.getString(4))
                        + " · " + rs.getString(5)});
                }
            }
        }
        try (PreparedStatement ps = cn.prepareStatement("SELECT DATE(data_hora), periodo, total_pago FROM recibo_rh WHERE fk_funcionario = ?")) {
            ps.setInt(1, f.id);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    l.add(new String[]{String.valueOf(rs.getDate(1)), "Recibo de salário",
                        t(rs.getString(2)) + " · " + UiRH.moeda(rs.getDouble(3)) + " Kz"});
                }
            }
        } catch (SQLException e) {
            // sem recibos nesta base
        }
        try (PreparedStatement ps = cn.prepareStatement("SELECT data_previo, numero_dias_aviso FROM previo_aviso WHERE fk_funcionario = ?")) {
            ps.setInt(1, f.id);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    l.add(new String[]{String.valueOf(rs.getDate(1)), "Pré-aviso", rs.getInt(2) + " dias"});
                }
            }
        } catch (SQLException e) {
            // tabela antiga ausente
        }
        l.sort((a, b) -> b[0].compareTo(a[0]));
        return l;
    }

    // =====================================================================
    // INDICADORES E ALERTAS (painel do RH)
    // =====================================================================
    public static final class Indicadores {

        public int activos, homens, mulheres, contratosATerminar, documentosAExpirar, emFerias, feriasProximas,
                faltasMes, processosAbertos, aniversariantesMes, pedidosFeriasPendentes;
        public double massaSalarial;
    }

    /** Alerta do painel: gravidade (0 informação, 1 atenção, 2 urgente), texto, área (para abrir o ecrã certo). */
    public static final class Alerta {

        public final int gravidade;
        public final String texto, area;

        Alerta(int gravidade, String texto, String area) {
            this.gravidade = gravidade;
            this.texto = texto;
            this.area = area;
        }
    }

    public static Indicadores indicadores(Connection cn, List<Alerta> alertas) throws SQLException {
        Indicadores ind = new Indicadores();
        LocalDate hoje = LocalDate.now();
        List<Funcionario> fs = funcionarios(cn, true);
        ind.activos = fs.size();
        for (Funcionario f : fs) {
            if ("Masculino".equalsIgnoreCase(f.sexo)) {
                ind.homens++;
            } else if (f.sexo.toLowerCase().startsWith("f")) {
                ind.mulheres++;
            }
            ind.massaSalarial += f.salario;
            if (f.fimContrato != null && "Determinado".equalsIgnoreCase(f.tipoContrato)) {
                long dias = java.time.temporal.ChronoUnit.DAYS.between(hoje, f.fimContrato);
                if (dias < 0) {
                    ind.contratosATerminar++;
                    alertas.add(new Alerta(2, "Contrato de " + f.nome + " terminou em " + UiRH.data(f.fimContrato)
                            + " e o funcionário continua activo", "contratos"));
                } else if (dias <= 30) {
                    ind.contratosATerminar++;
                    alertas.add(new Alerta(dias <= 7 ? 2 : 1, "Contrato de " + f.nome + " termina "
                            + (dias == 0 ? "hoje" : "em " + dias + (dias == 1 ? " dia" : " dias")) + " (" + UiRH.data(f.fimContrato) + ")", "contratos"));
                }
            }
            if (f.validadeDoc != null) {
                long dias = java.time.temporal.ChronoUnit.DAYS.between(hoje, f.validadeDoc);
                if (dias <= 60) {
                    ind.documentosAExpirar++;
                    alertas.add(new Alerta(dias < 0 ? 2 : 1, "Documento de identificação de " + f.nome
                            + (dias < 0 ? " caducou em " : " caduca em ") + UiRH.data(f.validadeDoc), "funcionarios"));
                }
            }
            if (f.nascimento != null && f.nascimento.getMonthValue() == hoje.getMonthValue()) {
                ind.aniversariantesMes++;
                if (f.nascimento.getDayOfMonth() == hoje.getDayOfMonth()) {
                    alertas.add(new Alerta(0, "Hoje é o aniversário de " + f.nome, "funcionarios"));
                }
            }
            if (f.salario <= 0) {
                alertas.add(new Alerta(1, f.nome + " não tem salário base definido", "funcionarios"));
            }
        }
        try (PreparedStatement ps = cn.prepareStatement("SELECT COUNT(DISTINCT fk_funcionario) FROM pedido_feria"
                + " WHERE COALESCE(estado, 'Aprovado') = 'Aprovado' AND ? BETWEEN data_inicio AND data_fim")) {
            ps.setDate(1, Date.valueOf(hoje));
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                ind.emFerias = rs.getInt(1);
            }
        }
        try (PreparedStatement ps = cn.prepareStatement("SELECT f.nome, p.data_inicio FROM pedido_feria p"
                + " JOIN tb_funcionario f ON f.idFuncionario = p.fk_funcionario"
                + " WHERE COALESCE(p.estado, 'Aprovado') = 'Aprovado' AND p.data_inicio BETWEEN ? AND ? ORDER BY p.data_inicio")) {
            ps.setDate(1, Date.valueOf(hoje.plusDays(1)));
            ps.setDate(2, Date.valueOf(hoje.plusDays(14)));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ind.feriasProximas++;
                    alertas.add(new Alerta(0, rs.getString(1) + " entra de férias a " + UiRH.data(data(rs.getDate(2))), "ferias"));
                }
            }
        }
        try (Statement st = cn.createStatement(); ResultSet rs = st.executeQuery(
                "SELECT COUNT(*) FROM pedido_feria WHERE estado = 'Pedido'")) {
            rs.next();
            ind.pedidosFeriasPendentes = rs.getInt(1);
            if (ind.pedidosFeriasPendentes > 0) {
                alertas.add(new Alerta(1, ind.pedidosFeriasPendentes + (ind.pedidosFeriasPendentes == 1
                        ? " pedido de férias à espera de aprovação" : " pedidos de férias à espera de aprovação"), "ferias"));
            }
        }
        try (PreparedStatement ps = cn.prepareStatement("SELECT COUNT(DISTINCT idFuncionarioFK, data) FROM tb_falta WHERE data BETWEEN ? AND ?")) {
            ps.setDate(1, Date.valueOf(hoje.withDayOfMonth(1)));
            ps.setDate(2, Date.valueOf(hoje.withDayOfMonth(hoje.lengthOfMonth())));
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                ind.faltasMes = rs.getInt(1);
            }
        }
        int antecedencia = configInt(cn, C_PRAZO_DECISAO, 30);
        for (Processo p : processos(cn, true)) {
            ind.processosAbertos++;
            if (p.entrevista != null && p.entrevista.toLocalDate().equals(hoje)) {
                alertas.add(new Alerta(2, "Entrevista do processo " + p.numero + " (" + p.funcionario + ") é hoje às "
                        + p.entrevista.toLocalTime().toString().substring(0, 5), "disciplinar"));
            }
            if (p.entrevista != null && FASE_DEFESA.equals(p.fase)) {
                long passados = java.time.temporal.ChronoUnit.DAYS.between(p.entrevista.toLocalDate(), hoje);
                if (passados > antecedencia) {
                    alertas.add(new Alerta(2, "Processo " + p.numero + " (" + p.funcionario + "): passaram " + passados
                            + " dias desde a entrevista e ainda não há decisão", "disciplinar"));
                }
            }
        }
        alertas.sort((a, b) -> b.gravidade - a.gravidade);
        return ind;
    }

    // =====================================================================
    // FERIADOS
    // =====================================================================
    /** Feriados de um ano: os gravados em rh_feriado; sem nenhum, os nacionais de Angola. */
    private static final Map<Integer, Map<LocalDate, String>> cacheFeriados = new java.util.concurrent.ConcurrentHashMap<>();

    public static Map<LocalDate, String> feriados(Connection cn, int ano) throws SQLException {
        Map<LocalDate, String> c = cacheFeriados.get(ano);
        if (c == null) {
            c = lerFeriados(cn, ano);
            cacheFeriados.put(ano, c);
        }
        return new LinkedHashMap<>(c);
    }

    /** Feriados de um ano sem pedir ligação (só vai à base se ainda não estiverem em memória). */
    public static Map<LocalDate, String> feriados(int ano) {
        Map<LocalDate, String> c = cacheFeriados.get(ano);
        if (c != null) {
            return new LinkedHashMap<>(c);
        }
        try (Connection cn = ligacao()) {
            return feriados(cn, ano);
        } catch (SQLException e) {
            e.printStackTrace();
            return CalendarioRH.feriadosNacionais(ano);
        }
    }

    private static Map<LocalDate, String> lerFeriados(Connection cn, int ano) throws SQLException {
        Map<LocalDate, String> m = new LinkedHashMap<>();
        try (PreparedStatement ps = cn.prepareStatement("SELECT data, designacao FROM rh_feriado WHERE YEAR(data) = ? ORDER BY data")) {
            ps.setInt(1, ano);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    m.put(rs.getDate(1).toLocalDate(), rs.getString(2));
                }
            }
        }
        if (m.isEmpty()) {
            m.putAll(CalendarioRH.feriadosNacionais(ano));
        }
        return m;
    }

    public static void gravarFeriados(Connection cn, int ano, Map<LocalDate, String> feriados) throws SQLException {
        boolean auto = cn.getAutoCommit();
        cn.setAutoCommit(false);
        try (PreparedStatement del = cn.prepareStatement("DELETE FROM rh_feriado WHERE YEAR(data) = ?");
                PreparedStatement ins = cn.prepareStatement("INSERT INTO rh_feriado (data, designacao) VALUES (?, ?)")) {
            del.setInt(1, ano);
            del.executeUpdate();
            for (Map.Entry<LocalDate, String> e : feriados.entrySet()) {
                ins.setDate(1, Date.valueOf(e.getKey()));
                ins.setString(2, e.getValue());
                ins.addBatch();
            }
            ins.executeBatch();
            cn.commit();
            cacheFeriados.remove(ano);
        } catch (SQLException | RuntimeException e) {
            cn.rollback();
            throw e;
        } finally {
            cn.setAutoCommit(auto);
        }
    }

    // =====================================================================
    static String t(String s) {
        return s == null || "null".equalsIgnoreCase(s.trim()) ? "" : s.trim();
    }

    static LocalDate data(java.sql.Date d) {
        return d == null ? null : d.toLocalDate();
    }

    static java.time.LocalDateTime dataHora(Timestamp t) {
        return t == null ? null : t.toLocalDateTime();
    }

    private static void setData(PreparedStatement ps, int i, LocalDate d) throws SQLException {
        if (d == null) {
            ps.setNull(i, java.sql.Types.DATE);
        } else {
            ps.setDate(i, Date.valueOf(d));
        }
    }
}
