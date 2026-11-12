package util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Espelho da base principal noutra base do mesmo servidor MySQL, por triggers:
 * tudo o que for inserido/alterado/eliminado em ORIGEM é repetido em DESTINO,
 * na mesma transacção. Se a escrita no espelho falhar, a operação original
 * também falha, pelo que as duas bases nunca divergem.
 * <p>
 * {@link #actualizar} faz o processo completo, sem tocar nos dados da ORIGEM:
 * <ol>
 * <li>guarda os triggers actuais num ficheiro .sql;</li>
 * <li>(opcional) copia o DESTINO para DESTINO_bk_aaaaMMdd_HHmm;</li>
 * <li>remove os triggers do DESTINO (o espelho é passivo);</li>
 * <li>cria o DESTINO e as tabelas em falta / com estrutura diferente;</li>
 * <li>copia os dados das tabelas diferentes (ORIGEM &rarr; DESTINO);</li>
 * <li>cria os triggers esp_* na ORIGEM (substitui os AFTER antigos; os
 * BEFORE, ex. trg_venda_numero_diario, mantêm-se);</li>
 * <li>verifica de novo e volta a copiar o que tiver mudado entretanto.</li>
 * </ol>
 * A lógica do stock_diario (antigo trg_tb_stock_update_rev) fica no trigger de
 * UPDATE de tb_stock.
 * <p>
 * Usado pela janela {@code visao.espelho.EspelhoBDVisao} (Configurações &rarr;
 * Espelho da Base de Dados) e pela linha de comandos:
 * <pre>
 *   java -cp ... util.EspelhoBaseDados ORIGEM DESTINO [--actualizar] [--sem-backup] [--sql ficheiro.sql]
 * </pre>
 * Na linha de comandos: -Dbd.host, -Dbd.porta, -Dbd.user (localhost/3306/root)
 * e a palavra-passe na variável de ambiente KITANDA_DB_PASSWORD.
 * <p>
 * Use sempre uma ligação dedicada (sem o proxy de auditoria), por exemplo
 * {@link BDConexao#novaLigacaoManutencao()}.
 */
public class EspelhoBaseDados {

    public static final String PREFIXO = "esp_";
    private static final int TENTATIVAS_VERIFICACAO = 2;

    private final Connection conn;
    private final String origem;
    private final String destino;
    private Consumer<String> registo = System.out::println;

    public EspelhoBaseDados(Connection conn, String origem, String destino) {
        if (origem.equalsIgnoreCase(destino)) {
            throw new IllegalArgumentException("A base de destino tem de ser diferente da origem.");
        }
        this.conn = conn;
        this.origem = origem;
        this.destino = destino;
    }

    public void setRegisto(Consumer<String> registo) {
        this.registo = registo != null ? registo : s -> {
        };
    }

    private void log(String mensagem) {
        registo.accept(mensagem);
    }

    // ==================================================================
    // Análise
    // ==================================================================
    /** Estado do espelho, sem alterar nada. */
    public static class Analise {

        public boolean destinoExiste;
        public int tabelasOrigem;
        public final List<String> semChavePrimaria = new ArrayList<>();
        public final List<String> emFaltaNoDestino = new ArrayList<>();
        public final List<String> estruturaDiferente = new ArrayList<>();
        /** null se a comparação de dados não foi pedida. */
        public List<String> dadosDiferentes;
        public int triggersEspelho;
        public int triggersAfterAntigos;
        public int triggersNoDestino;

        public boolean espelhoActivo() {
            return destinoExiste && triggersEspelho == tabelasOrigem * 3 && triggersAfterAntigos == 0
                    && triggersNoDestino == 0 && emFaltaNoDestino.isEmpty() && estruturaDiferente.isEmpty()
                    && (dadosDiferentes == null || dadosDiferentes.isEmpty());
        }
    }

    public Analise analisar(boolean compararDados) throws SQLException {
        Analise a = new Analise();
        a.destinoExiste = existeBase(destino);
        List<String> tabelasOrigem = tabelas(origem);
        List<String> tabelasDestino = a.destinoExiste ? tabelas(destino) : new ArrayList<>();
        a.tabelasOrigem = tabelasOrigem.size();

        for (String t : tabelasOrigem) {
            if (chavePrimaria(t).isEmpty()) {
                a.semChavePrimaria.add(t);
            }
            if (!tabelasDestino.contains(t)) {
                a.emFaltaNoDestino.add(t);
            } else if (!assinaturaColunas(origem, t).equals(assinaturaColunas(destino, t))) {
                a.estruturaDiferente.add(t);
            }
        }

        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT trigger_schema, trigger_name, action_timing FROM information_schema.triggers WHERE trigger_schema IN (?, ?)")) {
            ps.setString(1, origem);
            ps.setString(2, destino);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    boolean naOrigem = origem.equals(rs.getString(1));
                    if (!naOrigem) {
                        a.triggersNoDestino++;
                    } else if (rs.getString(2).startsWith(PREFIXO)) {
                        a.triggersEspelho++;
                    } else if ("AFTER".equalsIgnoreCase(rs.getString(3))) {
                        a.triggersAfterAntigos++;
                    }
                }
            }
        }

        if (compararDados && a.destinoExiste) {
            List<String> comparaveis = new ArrayList<>(tabelasOrigem);
            comparaveis.removeAll(a.emFaltaNoDestino);
            comparaveis.removeAll(a.estruturaDiferente);
            a.dadosDiferentes = tabelasComDadosDiferentes(comparaveis);
        }
        return a;
    }

    // ==================================================================
    // Processo completo
    // ==================================================================
    /**
     * Prepara o destino, sincroniza os dados e liga o espelho. Pode ser
     * repetido as vezes que for preciso (é idempotente).
     *
     * @param fazerBackup copia o destino actual antes de o sobrepor
     * @param pasta onde guardar os ficheiros .sql (triggers antigos e novos)
     */
    public void actualizar(boolean fazerBackup, File pasta) throws Exception {
        String carimbo = new SimpleDateFormat("yyyyMMdd_HHmm").format(new Date());
        pasta.mkdirs();

        Analise a = analisar(false);
        if (!a.semChavePrimaria.isEmpty()) {
            throw new IllegalStateException("Estas tabelas não têm chave primária e não podem ser espelhadas: " + a.semChavePrimaria);
        }

        File ficheiroTriggers = new File(pasta, "triggers_antes_" + origem + "_" + carimbo + ".sql");
        guardarTriggersActuais(ficheiroTriggers);
        log("Triggers actuais guardados em " + ficheiroTriggers.getAbsolutePath());

        if (a.destinoExiste && fazerBackup) {
            String copia = destino + "_bk_" + carimbo;
            log("A copiar " + destino + " para " + copia + " (cópia de segurança)...");
            copiarBase(destino, copia);
            log("Cópia de segurança concluída: " + copia);
        }

        removerTriggers(destino, false);
        prepararDestino(a);

        List<String> diferentes = tabelasComDadosDiferentes(tabelas(origem));
        log(diferentes.isEmpty() ? "Dados já iguais em todas as tabelas."
                : diferentes.size() + " tabela(s) com dados diferentes: a sincronizar...");
        sincronizar(diferentes);

        List<String> sql = gerarSql();
        File ficheiroEspelho = new File(pasta, "espelho_" + origem + "_" + destino + "_" + carimbo + ".sql");
        escreverSql(sql, ficheiroEspelho);
        log("A ligar o espelho (" + (sql.size()) + " comandos)...");
        executar(sql);
        log("Triggers do espelho criados. SQL guardado em " + ficheiroEspelho.getAbsolutePath());

        // Se alguém gravou na origem enquanto copiávamos, a diferença aparece aqui;
        // com o espelho já ligado, basta copiar de novo essas tabelas.
        for (int i = 1; i <= TENTATIVAS_VERIFICACAO; i++) {
            diferentes = tabelasComDadosDiferentes(tabelas(origem));
            if (diferentes.isEmpty()) {
                log("Verificação final: " + origem + " e " + destino + " estão iguais. Espelho activo.");
                return;
            }
            log("Verificação " + i + ": " + diferentes + " mudaram durante o processo; a copiar de novo...");
            sincronizar(diferentes);
        }
        throw new IllegalStateException("As tabelas " + diferentes + " continuam diferentes. Feche todos os terminais e repita.");
    }

    /** Cria a base destino e as tabelas em falta ou com estrutura diferente. */
    private void prepararDestino(Analise a) throws SQLException {
        try (Statement st = conn.createStatement()) {
            if (!a.destinoExiste) {
                String[] charset = charsetBase(origem);
                st.execute("CREATE DATABASE " + q(destino) + " DEFAULT CHARACTER SET " + charset[0] + " COLLATE " + charset[1]);
                log("Base " + destino + " criada.");
            }
            List<String> recriar = new ArrayList<>(a.emFaltaNoDestino);
            recriar.addAll(a.estruturaDiferente);
            if (recriar.isEmpty()) {
                return;
            }
            st.execute("SET FOREIGN_KEY_CHECKS = 0");
            try {
                // o CREATE TABLE vem sem nome de base: executado no destino, as FKs apontam para o destino.
                // USE explícito: o setCatalog do Connector/J 5.1 não muda a base activa.
                st.execute("USE " + q(destino));
                garantirBaseActiva(st, destino);
                for (String t : recriar) {
                    String create = showCreateTable(origem, t);
                    st.execute("DROP TABLE IF EXISTS " + q(destino) + "." + q(t));
                    st.execute(create);
                    log((a.emFaltaNoDestino.contains(t) ? "Tabela criada: " : "Tabela recriada (estrutura diferente): ") + t);
                }
            } finally {
                st.execute("USE " + q(origem));
                st.execute("SET FOREIGN_KEY_CHECKS = 1");
            }
        }
    }

    /** Segurança: nunca executar um CREATE TABLE sem nome de base fora da base pretendida. */
    private static void garantirBaseActiva(Statement st, String esperada) throws SQLException {
        try (ResultSet rs = st.executeQuery("SELECT DATABASE()")) {
            String actual = rs.next() ? rs.getString(1) : null;
            if (!esperada.equalsIgnoreCase(actual)) {
                throw new SQLException("Base activa inesperada: " + actual + " (esperada " + esperada + "). Operação cancelada.");
            }
        }
    }

    /**
     * Copia as tabelas indicadas da origem para o destino: apaga do destino o
     * que já não existe na origem e insere/actualiza o resto.
     */
    public void sincronizar(List<String> lista) throws SQLException {
        if (lista.isEmpty()) {
            return;
        }
        try (Statement st = conn.createStatement()) {
            st.execute("SET FOREIGN_KEY_CHECKS = 0");
            try {
                for (String t : lista) {
                    List<String> cols = colunas(origem, t);
                    List<String> pk = chavePrimaria(t);
                    // NOT EXISTS em vez de DELETE multi-tabela com alias: no MySQL 5.1 este
                    // último falha ("Unknown table in MULTI DELETE") entre bases diferentes.
                    String alvo = q(destino) + "." + q(t);
                    StringBuilder juncao = new StringBuilder();
                    for (int i = 0; i < pk.size(); i++) {
                        juncao.append(i > 0 ? " AND " : "").append("o.").append(q(pk.get(i))).append(" = ").append(alvo).append(".").append(q(pk.get(i)));
                    }
                    int apagadas = st.executeUpdate("DELETE FROM " + alvo + " WHERE NOT EXISTS (SELECT 1 FROM "
                            + q(origem) + "." + q(t) + " o WHERE " + juncao + ")");
                    String lista_cols = listaColunas(cols, "");
                    try {
                        st.executeUpdate("INSERT INTO " + q(destino) + "." + q(t) + " (" + lista_cols + ") SELECT "
                                + lista_cols + " FROM " + q(origem) + "." + q(t) + " ON DUPLICATE KEY UPDATE " + actualizacoes(cols));
                    } catch (SQLException e) {
                        // conflito numa chave única com linhas antigas: copia a tabela inteira
                        log("  " + t + ": " + e.getMessage() + " -> a copiar a tabela inteira");
                        st.executeUpdate("DELETE FROM " + q(destino) + "." + q(t));
                        st.executeUpdate("INSERT INTO " + q(destino) + "." + q(t) + " (" + lista_cols + ") SELECT "
                                + lista_cols + " FROM " + q(origem) + "." + q(t));
                    }
                    log("  " + t + " sincronizada" + (apagadas > 0 ? " (" + apagadas + " linha(s) removida(s) do espelho)" : ""));
                }
            } finally {
                st.execute("SET FOREIGN_KEY_CHECKS = 1");
            }
        }
    }

    // ==================================================================
    // Leitura da estrutura
    // ==================================================================
    private boolean existeBase(String schema) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("SELECT 1 FROM information_schema.schemata WHERE schema_name = ?")) {
            ps.setString(1, schema);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    private String[] charsetBase(String schema) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT default_character_set_name, default_collation_name FROM information_schema.schemata WHERE schema_name = ?")) {
            ps.setString(1, schema);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return new String[]{rs.getString(1), rs.getString(2)};
            }
        }
    }

    private List<String> tabelas(String schema) throws SQLException {
        List<String> lista = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT table_name FROM information_schema.tables WHERE table_schema = ? AND table_type = 'BASE TABLE' ORDER BY table_name")) {
            ps.setString(1, schema);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(rs.getString(1));
                }
            }
        }
        return lista;
    }

    private List<String> colunas(String schema, String tabela) throws SQLException {
        List<String> lista = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT column_name FROM information_schema.columns WHERE table_schema = ? AND table_name = ? ORDER BY ordinal_position")) {
            ps.setString(1, schema);
            ps.setString(2, tabela);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(rs.getString(1));
                }
            }
        }
        return lista;
    }

    private String assinaturaColunas(String schema, String tabela) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT GROUP_CONCAT(CONCAT(column_name, ':', column_type) ORDER BY ordinal_position SEPARATOR ',') "
                + "FROM information_schema.columns WHERE table_schema = ? AND table_name = ?")) {
            ps.setString(1, schema);
            ps.setString(2, tabela);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getString(1) != null ? rs.getString(1) : "";
            }
        }
    }

    private List<String> chavePrimaria(String tabela) throws SQLException {
        List<String> lista = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT column_name FROM information_schema.key_column_usage "
                + "WHERE table_schema = ? AND table_name = ? AND constraint_name = 'PRIMARY' ORDER BY ordinal_position")) {
            ps.setString(1, origem);
            ps.setString(2, tabela);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(rs.getString(1));
                }
            }
        }
        return lista;
    }

    private String showCreateTable(String schema, String tabela) throws SQLException {
        try (Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery("SHOW CREATE TABLE " + q(schema) + "." + q(tabela))) {
            rs.next();
            return rs.getString(2);
        }
    }

    public List<String> tabelasComDadosDiferentes(List<String> lista) throws SQLException {
        List<String> diferentes = new ArrayList<>();
        try (Statement st = conn.createStatement()) {
            for (String t : lista) {
                try (ResultSet rs = st.executeQuery("CHECKSUM TABLE " + q(origem) + "." + q(t) + ", " + q(destino) + "." + q(t))) {
                    rs.next();
                    long a = rs.getLong(2);
                    boolean nulo = rs.wasNull();
                    rs.next();
                    long b = rs.getLong(2);
                    if (nulo || rs.wasNull() || a != b) {
                        diferentes.add(t);
                    }
                }
            }
        }
        return diferentes;
    }

    // ==================================================================
    // Triggers
    // ==================================================================
    private void removerTriggers(String schema, boolean soAfter) throws SQLException {
        List<String> remover = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT trigger_name FROM information_schema.triggers WHERE trigger_schema = ?"
                + (soAfter ? " AND action_timing = 'AFTER'" : ""))) {
            ps.setString(1, schema);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    remover.add(rs.getString(1));
                }
            }
        }
        try (Statement st = conn.createStatement()) {
            for (String n : remover) {
                st.execute("DROP TRIGGER IF EXISTS " + q(schema) + "." + q(n));
            }
        }
        if (!remover.isEmpty()) {
            log(remover.size() + " trigger(s) removido(s) de " + schema + ".");
        }
    }

    public List<String> gerarSql() throws SQLException {
        List<String> sql = new ArrayList<>();

        // 1) todos os triggers do destino (espelho passivo)
        // 2) os triggers AFTER da origem (serão substituídos; MySQL 5.1 só permite um por evento)
        Map<String, String> remover = new LinkedHashMap<>();
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT trigger_schema, trigger_name FROM information_schema.triggers "
                + "WHERE trigger_schema = ? OR (trigger_schema = ? AND action_timing = 'AFTER') ORDER BY 1, 2")) {
            ps.setString(1, destino);
            ps.setString(2, origem);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    remover.put(rs.getString(1) + "." + rs.getString(2), "DROP TRIGGER IF EXISTS " + q(rs.getString(1)) + "." + q(rs.getString(2)));
                }
            }
        }
        sql.addAll(remover.values());

        // 3) triggers de espelho em cada tabela da origem
        List<String> tabelasOrigem = tabelas(origem);
        boolean temStockDiario = tabelasOrigem.contains("stock_diario");
        for (String t : tabelasOrigem) {
            List<String> cols = colunas(origem, t);
            List<String> pk = chavePrimaria(t);
            String upsert = upsert(t, cols);

            sql.add("CREATE TRIGGER " + q(origem) + "." + q(nome(t, "ai")) + " AFTER INSERT ON " + q(origem) + "." + q(t)
                    + " FOR EACH ROW BEGIN\n  " + upsert + ";\nEND");

            StringBuilder au = new StringBuilder();
            au.append("CREATE TRIGGER ").append(q(origem)).append(".").append(q(nome(t, "au")))
                    .append(" AFTER UPDATE ON ").append(q(origem)).append(".").append(q(t)).append(" FOR EACH ROW BEGIN\n");
            if (temStockDiario && "tb_stock".equals(t)) {
                au.append(blocoStockDiario());
            }
            // UPDATE pela chave antiga (respeita ON UPDATE CASCADE no destino);
            // se a linha não existir no destino, insere-a.
            au.append("  UPDATE ").append(q(destino)).append(".").append(q(t)).append(" SET ");
            for (int i = 0; i < cols.size(); i++) {
                au.append(i > 0 ? ", " : "").append(q(cols.get(i))).append(" = NEW.").append(q(cols.get(i)));
            }
            au.append(" WHERE ").append(condicaoPk(pk, "OLD")).append(";\n");
            au.append("  IF ROW_COUNT() = 0 THEN\n    ").append(upsert).append(";\n  END IF;\nEND");
            sql.add(au.toString());

            sql.add("CREATE TRIGGER " + q(origem) + "." + q(nome(t, "ad")) + " AFTER DELETE ON " + q(origem) + "." + q(t)
                    + " FOR EACH ROW BEGIN\n  DELETE FROM " + q(destino) + "." + q(t) + " WHERE " + condicaoPk(pk, "OLD") + ";\nEND");
        }
        return sql;
    }

    private String upsert(String tabela, List<String> cols) {
        return "INSERT INTO " + q(destino) + "." + q(tabela) + " (" + listaColunas(cols, "") + ") VALUES ("
                + listaColunas(cols, "NEW.") + ") ON DUPLICATE KEY UPDATE " + actualizacoes(cols);
    }

    private static String listaColunas(List<String> cols, String prefixo) {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < cols.size(); i++) {
            s.append(i > 0 ? ", " : "").append(prefixo).append(q(cols.get(i)));
        }
        return s.toString();
    }

    private static String actualizacoes(List<String> cols) {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < cols.size(); i++) {
            s.append(i > 0 ? ", " : "").append(q(cols.get(i))).append(" = VALUES(").append(q(cols.get(i))).append(")");
        }
        return s.toString();
    }

    /** Mesma regra do antigo trg_tb_stock_update_rev, agora na própria base (o espelho trata do destino). */
    private String blocoStockDiario() {
        String filtro = "produto_id = NEW.cod_produto_codigo AND armazem_id = NEW.cod_armazem AND data_registro = CURDATE()";
        return "  IF EXISTS (SELECT 1 FROM " + q(origem) + ".stock_diario WHERE " + filtro + ") THEN\n"
                + "    UPDATE " + q(origem) + ".stock_diario SET quantidade = NEW.quantidade_existente WHERE " + filtro + ";\n"
                + "  ELSE\n"
                + "    INSERT INTO " + q(origem) + ".stock_diario (produto_id, quantidade, data_registro, armazem_id) "
                + "VALUES (NEW.cod_produto_codigo, NEW.quantidade_existente, CURDATE(), NEW.cod_armazem);\n"
                + "  END IF;\n";
    }

    private String condicaoPk(List<String> pk, String alias) {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < pk.size(); i++) {
            s.append(i > 0 ? " AND " : "").append(q(pk.get(i))).append(" = ").append(alias).append(".").append(q(pk.get(i)));
        }
        return s.toString();
    }

    private static String nome(String tabela, String sufixo) {
        String n = PREFIXO + tabela + "_" + sufixo;
        return n.length() <= 64 ? n : n.substring(0, 64);
    }

    private static String q(String identificador) {
        return "`" + identificador.replace("`", "``") + "`";
    }

    // ==================================================================
    // Cópia de segurança e ficheiros
    // ==================================================================
    /** Copia todas as tabelas (estrutura + dados) de uma base para uma nova. */
    private void copiarBase(String de, String para) throws SQLException {
        if (existeBase(para)) {
            throw new IllegalStateException("Já existe a base " + para + ".");
        }
        String[] charset = charsetBase(de);
        try (Statement st = conn.createStatement()) {
            st.execute("CREATE DATABASE " + q(para) + " DEFAULT CHARACTER SET " + charset[0] + " COLLATE " + charset[1]);
            st.execute("SET FOREIGN_KEY_CHECKS = 0");
            try {
                st.execute("USE " + q(para));
                garantirBaseActiva(st, para);
                for (String t : tabelas(de)) {
                    st.execute(showCreateTable(de, t));
                    st.executeUpdate("INSERT INTO " + q(para) + "." + q(t) + " SELECT * FROM " + q(de) + "." + q(t));
                }
            } finally {
                st.execute("USE " + q(origem));
                st.execute("SET FOREIGN_KEY_CHECKS = 1");
            }
        }
    }

    private void guardarTriggersActuais(File ficheiro) throws Exception {
        try (PrintWriter w = new PrintWriter(new OutputStreamWriter(new FileOutputStream(ficheiro), StandardCharsets.UTF_8));
                PreparedStatement ps = conn.prepareStatement(
                        "SELECT trigger_schema, trigger_name, action_timing, event_manipulation, event_object_table, action_statement "
                        + "FROM information_schema.triggers WHERE trigger_schema IN (?, ?) ORDER BY 1, 2")) {
            ps.setString(1, origem);
            ps.setString(2, destino);
            w.println("-- Triggers de " + origem + " e " + destino + " antes da actualização do espelho");
            w.println("DELIMITER $$");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    w.println("DROP TRIGGER IF EXISTS " + q(rs.getString(1)) + "." + q(rs.getString(2)) + "$$");
                    w.println("CREATE TRIGGER " + q(rs.getString(1)) + "." + q(rs.getString(2)) + " " + rs.getString(3) + " "
                            + rs.getString(4) + " ON " + q(rs.getString(1)) + "." + q(rs.getString(5)) + " FOR EACH ROW\n"
                            + rs.getString(6) + "$$\n");
                }
            }
            w.println("DELIMITER ;");
        }
    }

    public void escreverSql(List<String> sql, File ficheiro) throws Exception {
        try (PrintWriter w = new PrintWriter(new OutputStreamWriter(new FileOutputStream(ficheiro), StandardCharsets.UTF_8))) {
            w.println("-- Espelho " + origem + " -> " + destino + " gerado por util.EspelhoBaseDados");
            w.println("DELIMITER $$");
            for (String s : sql) {
                w.println(s + "$$");
            }
            w.println("DELIMITER ;");
        }
    }

    public void executar(List<String> sql) throws SQLException {
        try (Statement st = conn.createStatement()) {
            for (String s : sql) {
                st.execute(s);
            }
        }
    }

    // ==================================================================
    // Linha de comandos
    // ==================================================================
    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            System.out.println("Uso: util.EspelhoBaseDados ORIGEM DESTINO [--actualizar] [--sem-backup] [--sql ficheiro.sql]");
            return;
        }
        String origem = args[0], destino = args[1], ficheiroSql = null;
        boolean actualizar = false, backup = true;
        for (int i = 2; i < args.length; i++) {
            if ("--actualizar".equals(args[i])) {
                actualizar = true;
            } else if ("--sem-backup".equals(args[i])) {
                backup = false;
            } else if ("--sql".equals(args[i]) && i + 1 < args.length) {
                ficheiroSql = args[++i];
            }
        }
        String senha = System.getenv("KITANDA_DB_PASSWORD");
        if (senha == null) {
            throw new IllegalStateException("Defina a variável de ambiente KITANDA_DB_PASSWORD.");
        }
        String url = "jdbc:mysql://" + System.getProperty("bd.host", "localhost") + ":"
                + System.getProperty("bd.porta", "3306") + "/" + origem + "?characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull";
        try (Connection c = DriverManager.getConnection(url, System.getProperty("bd.user", "root"), senha)) {
            EspelhoBaseDados espelho = new EspelhoBaseDados(c, origem, destino);
            if (actualizar) {
                espelho.actualizar(backup, new File(DVML.CAMINHO_BK, "espelho"));
            } else {
                Analise a = espelho.analisar(true);
                System.out.println("Destino existe: " + a.destinoExiste + " | tabelas: " + a.tabelasOrigem
                        + " | em falta: " + a.emFaltaNoDestino + " | estrutura diferente: " + a.estruturaDiferente
                        + " | sem PK: " + a.semChavePrimaria + " | dados diferentes: " + a.dadosDiferentes
                        + " | triggers esp_: " + a.triggersEspelho + " | AFTER antigos: " + a.triggersAfterAntigos
                        + " | no destino: " + a.triggersNoDestino + " | activo: " + a.espelhoActivo());
                if (ficheiroSql != null) {
                    espelho.escreverSql(espelho.gerarSql(), new File(ficheiroSql));
                    System.out.println("SQL escrito em " + ficheiroSql);
                }
            }
        }
    }
}
