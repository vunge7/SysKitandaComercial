package comercial.caixa;

import java.io.File;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;

/**
 * Fecho geral do dia: junta os fechos de todos os utilizadores (Recepção,
 * Restaurante, Bar, Hospedaria, administradores...) feitos entre as 08:00 do dia
 * anterior e as 08:00 do dia de referência (hora configurável). Mostra também,
 * para controlo, as caixas que ficaram por fechar e os pagamentos (FR/RC) feitos
 * fora de qualquer caixa. É impresso e enviado por e-mail e WhatsApp a quem
 * recebe os fechos; pode ser reimpresso por data.
 * Pedido do utilizador em 2026-11-14.
 *
 * tb_fecho_geral: um registo por data (chave única) — é a reserva que impede dois
 * computadores (ou o serviço e a recepção) de o gerarem duas vezes.
 */
public final class FechoGeral {

    private static final SimpleDateFormat DH = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    private static final SimpleDateFormat D = new SimpleDateFormat("dd/MM/yyyy");
    private static final SimpleDateFormat ISO = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public static final String MODO_AUTOMATICO = "Automático";
    public static final String MODO_MANUAL = "Manual";

    private FechoGeral() {
    }

    // =====================================================================
    // TABELAS E CONFIGURAÇÃO
    // =====================================================================
    private static volatile boolean tabelasCriadas;

    /** Cria as tabelas uma vez por sessão (o DDL faz commit implícito no MySQL). */
    public static void garantirTabelas(Connection cn) throws SQLException {
        if (tabelasCriadas) {
            return;
        }
        try (Statement st = cn.createStatement()) {
            st.execute("CREATE TABLE IF NOT EXISTS tb_fecho_geral_config ("
                    + " id INT NOT NULL PRIMARY KEY,"
                    + " hora TIME NOT NULL DEFAULT '08:00:00',"
                    + " activo TINYINT(1) NOT NULL DEFAULT 1,"
                    + " imprimir TINYINT(1) NOT NULL DEFAULT 1,"
                    + " enviar TINYINT(1) NOT NULL DEFAULT 1"
                    + ") ENGINE=InnoDB DEFAULT CHARSET=utf8");
            st.execute("INSERT IGNORE INTO tb_fecho_geral_config (id) VALUES (1)");
            st.execute("CREATE TABLE IF NOT EXISTS tb_fecho_geral ("
                    + " pk_fecho_geral INT NOT NULL AUTO_INCREMENT PRIMARY KEY,"
                    + " data_referencia DATE NOT NULL,"
                    + " inicio DATETIME NOT NULL,"
                    + " fim DATETIME NOT NULL,"
                    + " gerado_em DATETIME NOT NULL,"
                    + " modo VARCHAR(20) NOT NULL,"
                    + " origem VARCHAR(120) NULL,"
                    + " fk_usuario INT NULL,"
                    + " numero_fechos INT NOT NULL DEFAULT 0,"
                    + " total DECIMAL(30,2) NOT NULL DEFAULT 0,"
                    + " estado VARCHAR(20) NOT NULL,"
                    + " detalhe VARCHAR(1000) NULL,"
                    + " UNIQUE KEY uk_fecho_geral_data (data_referencia)"
                    + ") ENGINE=InnoDB DEFAULT CHARSET=utf8");
            garantirChaveUnica(cn, st);
            garantirColunaHora(cn, st);
            garantirColunasDoDia(cn, st);
        }
        tabelasCriadas = true;
    }

    /**
     * Reagendamento só para o dia (feito no aviso "Faltam x minutos para o fecho
     * automático"): hora_hoje vale apenas na data data_hora_hoje. Fica na BD para
     * todos os computadores verem a mesma hora.
     */
    private static void garantirColunasDoDia(Connection cn, Statement st) throws SQLException {
        String[][] colunas = {
            {"hora_hoje", "TIME NULL"},
            {"data_hora_hoje", "DATE NULL"}};
        for (String[] c : colunas) {
            boolean tem;
            try (ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = DATABASE()"
                    + " AND TABLE_NAME = 'tb_fecho_geral_config' AND COLUMN_NAME = '" + c[0] + "'")) {
                tem = rs.next() && rs.getInt(1) > 0;
            }
            if (!tem) {
                st.execute("ALTER TABLE tb_fecho_geral_config ADD COLUMN " + c[0] + " " + c[1]);
            }
        }
    }

    /**
     * A chave única por data é o que impede o fecho geral de sair mais de uma vez
     * por dia. Uma tb_fecho_geral criada sem ela (versão anterior) deixava o
     * INSERT IGNORE passar sempre e o fecho saía em cada verificação (~5 min,
     * o tempo do envio). Remove os duplicados (fica o primeiro) e cria a chave.
     */
    private static void garantirChaveUnica(Connection cn, Statement st) throws SQLException {
        boolean tem = false;
        try (ResultSet rs = st.executeQuery("SHOW INDEX FROM tb_fecho_geral WHERE Non_unique = 0 AND Column_name = 'data_referencia'")) {
            tem = rs.next();
        }
        if (!tem) {
            st.executeUpdate("DELETE a FROM tb_fecho_geral a JOIN tb_fecho_geral b"
                    + " ON a.data_referencia = b.data_referencia AND a.pk_fecho_geral > b.pk_fecho_geral");
            st.execute("ALTER TABLE tb_fecho_geral ADD UNIQUE KEY uk_fecho_geral_data (data_referencia)");
            System.out.println("[FechoGeral] chave única por data criada em tb_fecho_geral");
        }
    }

    /** tb_dados_instituicao.hora_fecho_geral: a hora do fecho geral automático (Configurações). */
    private static void garantirColunaHora(Connection cn, Statement st) throws SQLException {
        boolean tem;
        try (ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = DATABASE()"
                + " AND TABLE_NAME = 'tb_dados_instituicao' AND COLUMN_NAME = 'hora_fecho_geral'")) {
            tem = rs.next() && rs.getInt(1) > 0;
        }
        if (!tem) {
            st.execute("ALTER TABLE tb_dados_instituicao ADD COLUMN hora_fecho_geral TIME NULL DEFAULT '08:00:00'");
            // herda a hora que já estava configurada na Gestão de Fechos
            st.executeUpdate("UPDATE tb_dados_instituicao SET hora_fecho_geral ="
                    + " COALESCE((SELECT hora FROM tb_fecho_geral_config WHERE id = 1), '08:00:00')");
        }
    }

    /** Configuração (tb_fecho_geral_config). */
    public static final class Config {

        /** Hora configurada (tb_dados_instituicao.hora_fecho_geral). */
        public int hora = 8, minuto = 0;
        public boolean activo = true, imprimir = true, enviar = true;
        /** Hora reagendada só para hoje (null = a configurada). */
        public Integer horaHoje, minutoHoje;

        public String horaTexto() {
            return String.format("%02d:%02d", hora, minuto);
        }

        /** Hora do fecho automático de hoje: a reagendada, ou a configurada. */
        public int horaEfectiva() {
            return horaHoje != null ? horaHoje : hora;
        }

        public int minutoEfectivo() {
            return minutoHoje != null ? minutoHoje : minuto;
        }

        /** Data/hora do fecho automático de hoje. */
        public Date fimHoje() {
            Calendar k = Calendar.getInstance();
            k.set(Calendar.HOUR_OF_DAY, horaEfectiva());
            k.set(Calendar.MINUTE, minutoEfectivo());
            k.set(Calendar.SECOND, 0);
            k.set(Calendar.MILLISECOND, 0);
            return k.getTime();
        }
    }

    public static Config config(Connection cn) throws SQLException {
        garantirTabelas(cn);
        Config c = new Config();
        try (Statement st = cn.createStatement();
                ResultSet rs = st.executeQuery("SELECT HOUR(hora), MINUTE(hora), activo, imprimir, enviar FROM tb_fecho_geral_config WHERE id = 1")) {
            if (rs.next()) {
                c.hora = rs.getInt(1);
                c.minuto = rs.getInt(2);
                c.activo = rs.getInt(3) == 1;
                c.imprimir = rs.getInt(4) == 1;
                c.enviar = rs.getInt(5) == 1;
            }
        }
        // a hora vem dos dados da instituição (Configurações); a tabela de config é o recurso
        try (Statement st = cn.createStatement();
                ResultSet rs = st.executeQuery("SELECT HOUR(hora_fecho_geral), MINUTE(hora_fecho_geral) FROM tb_dados_instituicao"
                        + " WHERE hora_fecho_geral IS NOT NULL LIMIT 1")) {
            if (rs.next()) {
                c.hora = rs.getInt(1);
                c.minuto = rs.getInt(2);
            }
        }
        // ajustes feitos para hoje no aviso do fecho automático
        try (Statement st = cn.createStatement();
                ResultSet rs = st.executeQuery("SELECT HOUR(hora_hoje), MINUTE(hora_hoje),"
                        + " (hora_hoje IS NOT NULL AND data_hora_hoje = CURDATE())"
                        + " FROM tb_fecho_geral_config WHERE id = 1")) {
            if (rs.next() && rs.getInt(3) == 1) {
                c.horaHoje = rs.getInt(1);
                c.minutoHoje = rs.getInt(2);
            }
        }
        return c;
    }

    /** Reagenda o fecho automático só para hoje (o de amanhã volta à hora configurada). */
    public static void reagendarHoje(Connection cn, int hora, int minuto) throws SQLException {
        garantirTabelas(cn);
        try (PreparedStatement ps = cn.prepareStatement("UPDATE tb_fecho_geral_config SET hora_hoje = ?,"
                + " data_hora_hoje = CURDATE() WHERE id = 1")) {
            ps.setString(1, String.format("%02d:%02d:00", hora, minuto));
            ps.executeUpdate();
        }
    }

    public static void gravarConfig(Connection cn, Config c) throws SQLException {
        garantirTabelas(cn);
        try (PreparedStatement ps = cn.prepareStatement("UPDATE tb_fecho_geral_config SET hora = ?, activo = ?, imprimir = ?,"
                + " enviar = ? WHERE id = 1")) {
            ps.setString(1, c.horaTexto() + ":00");
            ps.setInt(2, c.activo ? 1 : 0);
            ps.setInt(3, c.imprimir ? 1 : 0);
            ps.setInt(4, c.enviar ? 1 : 0);
            ps.executeUpdate();
        }
        gravarHora(cn, c.hora, c.minuto);
    }

    /** Grava só a hora do fecho geral (tb_dados_instituicao.hora_fecho_geral e a config). */
    public static void gravarHora(Connection cn, int hora, int minuto) throws SQLException {
        garantirTabelas(cn);
        String h = String.format("%02d:%02d:00", hora, minuto);
        try (PreparedStatement ps = cn.prepareStatement("UPDATE tb_dados_instituicao SET hora_fecho_geral = ?")) {
            ps.setString(1, h);
            ps.executeUpdate();
        }
        // a nova hora configurada vale já para hoje: anula o reagendamento do dia
        try (PreparedStatement ps = cn.prepareStatement("UPDATE tb_fecho_geral_config SET hora = ?, hora_hoje = NULL,"
                + " data_hora_hoje = NULL WHERE id = 1")) {
            ps.setString(1, h);
            ps.executeUpdate();
        }
    }

    /** Já existe o fecho geral desta data (gerado ou a gerar)? */
    public static boolean existe(Connection cn, Date referencia) throws SQLException {
        garantirTabelas(cn);
        try (PreparedStatement ps = cn.prepareStatement("SELECT 1 FROM tb_fecho_geral WHERE data_referencia = ? LIMIT 1")) {
            ps.setDate(1, new java.sql.Date(referencia.getTime()));
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    /** [início, fim] do fecho geral do dia de referência: hora de ontem até hora de hoje. */
    public static Date[] periodo(Date referencia, Config c) {
        Calendar fim = Calendar.getInstance();
        fim.setTime(referencia);
        fim.set(Calendar.HOUR_OF_DAY, c.hora);
        fim.set(Calendar.MINUTE, c.minuto);
        fim.set(Calendar.SECOND, 0);
        fim.set(Calendar.MILLISECOND, 0);
        Calendar ini = (Calendar) fim.clone();
        ini.add(Calendar.DAY_OF_MONTH, -1);
        return new Date[]{ini.getTime(), fim.getTime()};
    }

    /**
     * Período do fecho geral automático de hoje: termina à hora efectiva de hoje
     * (reagendada ou configurada) e começa onde terminou o último fecho geral
     * (se foi nos últimos 3 dias), para não haver horas contadas duas vezes nem
     * perdidas quando um dia foi reagendado ou cancelado.
     */
    public static Date[] periodoHoje(Connection cn, Config c) throws SQLException {
        Date fim = c.fimHoje();
        // sem fecho anterior: das HH:mm configuradas de ontem até à hora de hoje
        Date ini = periodo(fim, c)[0];
        try (PreparedStatement ps = cn.prepareStatement("SELECT MAX(fim) FROM tb_fecho_geral WHERE data_referencia < CURDATE()")) {
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next() && rs.getTimestamp(1) != null) {
                    Date ultimo = new Date(rs.getTimestamp(1).getTime());
                    if (ultimo.before(fim) && fim.getTime() - ultimo.getTime() <= 3L * 24 * 3600000) {
                        ini = ultimo;
                    }
                }
            }
        }
        return new Date[]{ini, fim};
    }

    /** Período de um fecho geral já gerado (o gravado), ou o da hora configurada. */
    public static Date[] periodoDe(Connection cn, Date referencia, Config c) throws SQLException {
        garantirTabelas(cn);
        try (PreparedStatement ps = cn.prepareStatement("SELECT inicio, fim FROM tb_fecho_geral WHERE data_referencia = ? LIMIT 1")) {
            ps.setDate(1, new java.sql.Date(referencia.getTime()));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Date[]{new Date(rs.getTimestamp(1).getTime()), new Date(rs.getTimestamp(2).getTime())};
                }
            }
        }
        return periodo(referencia, c);
    }

    // =====================================================================
    // CÁLCULO
    // =====================================================================
    /** Pagamentos de um utilizador fora de qualquer caixa. */
    public static final class SemCaixa {

        public int idUsuario;
        public String nome = "", tipo = "";
        public int fr, rc;
        public BigDecimal valor = BigDecimal.ZERO;
    }

    public static final class Resultado {

        public Date inicio, fim;
        public final List<FechoCaixa.Resumo> fechos = new ArrayList<>();
        public final List<FechoCaixa.Resumo> abertas = new ArrayList<>();
        public final List<SemCaixa> semCaixa = new ArrayList<>();
        /** NC de FR / RC emitidas no período (todos os utilizadores), com o documento de origem. */
        public final List<FechoCaixa.Documento> notasCredito = new ArrayList<>();
        public BigDecimal totalNotasCredito = BigDecimal.ZERO;
        public final Map<String, BigDecimal[]> porForma = new LinkedHashMap<>(); // nome → {esperado, contado|null}
        public int numeroVendas, numeroRecibos;
        public BigDecimal totalVendas = BigDecimal.ZERO, totalRecibos = BigDecimal.ZERO, totalQuartos = BigDecimal.ZERO,
                totalContado, totalSemCaixa = BigDecimal.ZERO;

        public BigDecimal total() {
            return totalVendas.add(totalRecibos);
        }
    }

    public static Resultado calcular(Connection cn, Date inicio, Date fim) throws SQLException {
        Resultado g = new Resultado();
        g.inicio = inicio;
        g.fim = fim;
        Timestamp de = new Timestamp(inicio.getTime()), a = new Timestamp(fim.getTime());

        // fechos feitos no período
        List<Integer> pks = new ArrayList<>();
        try (PreparedStatement ps = cn.prepareStatement("SELECT pk_caixa FROM caixa WHERE data_fecho >= ? AND data_fecho < ?"
                + " ORDER BY data_fecho")) {
            ps.setTimestamp(1, de);
            ps.setTimestamp(2, a);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    pks.add(rs.getInt(1));
                }
            }
        }
        for (int pk : pks) {
            FechoCaixa.Resumo r = FechoCaixa.lerPorId(cn, pk);
            if (r == null) {
                continue;
            }
            g.fechos.add(r);
            g.numeroVendas += r.numeroVendas;
            g.numeroRecibos += r.numeroRecibos;
            g.totalVendas = g.totalVendas.add(r.totalVendas);
            g.totalRecibos = g.totalRecibos.add(r.totalRecibos);
            g.totalQuartos = g.totalQuartos.add(r.totalQuartos);
            for (FechoCaixa.Linha l : r.linhas) {
                if (l.esperado().signum() == 0 && l.contado == null) {
                    continue;
                }
                BigDecimal[] v = g.porForma.get(l.nome);
                if (v == null) {
                    v = new BigDecimal[]{BigDecimal.ZERO, null};
                    g.porForma.put(l.nome, v);
                }
                v[0] = v[0].add(l.esperado());
                if (l.contado != null) {
                    v[1] = (v[1] == null ? BigDecimal.ZERO : v[1]).add(l.contado);
                }
            }
            BigDecimal c = r.totalContado();
            if (c != null) {
                g.totalContado = (g.totalContado == null ? BigDecimal.ZERO : g.totalContado).add(c);
            }
        }

        // caixas ainda abertas com pagamentos no período
        List<Integer> abertas = new ArrayList<>();
        try (PreparedStatement ps = cn.prepareStatement("SELECT c.pk_caixa FROM caixa c WHERE c.data_fecho IS NULL"
                + " AND c.pk_caixa = (SELECT MAX(c2.pk_caixa) FROM caixa c2 WHERE c2.cod_usuario_abertura = c.cod_usuario_abertura)"
                + " AND EXISTS (SELECT 1 FROM tb_venda v WHERE v.codigo_usuario = c.cod_usuario_abertura"
                + " AND v.fk_documento IN (1, 6) AND (v.status_eliminado IS NULL OR v.status_eliminado = 'false')"
                + " AND v.dataVenda >= GREATEST(c.data_abertura, ?) AND v.dataVenda < ?) ORDER BY c.data_abertura")) {
            ps.setTimestamp(1, de);
            ps.setTimestamp(2, a);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    abertas.add(rs.getInt(1));
                }
            }
        }
        for (int pk : abertas) {
            FechoCaixa.Resumo r = FechoCaixa.lerPorId(cn, pk);
            if (r != null) {
                // só os pagamentos deste período (há caixas esquecidas abertas há meses)
                Date desde = r.abertura;
                r.abertura = desde.after(inicio) ? desde : inicio;
                Date ate = new Date();
                FechoCaixa.calcular(cn, r, r.idUsuario, ate.before(fim) ? ate : fim);
                FechoCaixa.detalhar(cn, r);
                r.abertura = desde;
                g.abertas.add(r);
            }
        }

        // notas de crédito de FR / RC emitidas no período
        g.notasCredito.addAll(FechoCaixa.notasCredito(cn, 0, de, new Timestamp(a.getTime() - 1)));
        for (FechoCaixa.Documento d : g.notasCredito) {
            g.totalNotasCredito = g.totalNotasCredito.add(d.valor);
        }

        // pagamentos fora de qualquer caixa
        try (PreparedStatement ps = cn.prepareStatement("SELECT v.codigo_usuario, u.nome, t.descricao,"
                + " SUM(v.fk_documento = 1), SUM(v.fk_documento = 6),"
                + " COALESCE(SUM((SELECT SUM(i.valor_real) FROM forma_pagamento_item i JOIN forma_pagamento fp ON fp.pk_forma_pagamento = i.fk_forma_pagamento"
                + " WHERE i.fk_venda = v.codigo)), 0)"
                + " FROM tb_venda v LEFT JOIN tb_usuario u ON u.codigo = v.codigo_usuario"
                + " LEFT JOIN tb_tipo_usuario t ON t.idTipoUsuario = u.idTipoUsuario"
                + " WHERE v.fk_documento IN (1, 6) AND (v.status_eliminado IS NULL OR v.status_eliminado = 'false')"
                + " AND v.dataVenda >= ? AND v.dataVenda < ?"
                + " AND NOT EXISTS (SELECT 1 FROM caixa c WHERE c.cod_usuario_abertura = v.codigo_usuario"
                + " AND v.dataVenda >= c.data_abertura AND (c.data_fecho IS NULL OR v.dataVenda <= c.data_fecho))"
                + " GROUP BY v.codigo_usuario, u.nome, t.descricao ORDER BY u.nome")) {
            ps.setTimestamp(1, de);
            ps.setTimestamp(2, a);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    SemCaixa s = new SemCaixa();
                    s.idUsuario = rs.getInt(1);
                    s.nome = FechoCaixa.texto(rs.getString(2));
                    s.tipo = FechoCaixa.texto(rs.getString(3));
                    s.fr = rs.getInt(4);
                    s.rc = rs.getInt(5);
                    s.valor = rs.getBigDecimal(6);
                    g.semCaixa.add(s);
                    g.totalSemCaixa = g.totalSemCaixa.add(s.valor);
                }
            }
        }
        return g;
    }

    // =====================================================================
    // TALÃO
    // =====================================================================
    public static TalaoFecho.Linhas linhas(Connection cn, Resultado g) {
        TalaoFecho.Linhas t = new TalaoFecho.Linhas();
        TalaoFecho.cabecalho(cn, t);
        t.titulo("FECHO GERAL");
        t.centro("De " + DH.format(g.inicio) + " a " + DH.format(g.fim));

        t.seccao("RESUMO", null);
        t.linha("Fechos de caixa", String.valueOf(g.fechos.size()));
        t.linha("Vendas pagas (FR): " + g.numeroVendas, TalaoFecho.m(g.totalVendas));
        t.linha("Recibos emitidos (RC): " + g.numeroRecibos, TalaoFecho.m(g.totalRecibos));
        t.destaque("TOTAL GERAL", TalaoFecho.m(g.total()));
        if (g.totalContado != null) {
            BigDecimal d = g.totalContado.subtract(g.total());
            t.negrito("Contado pelos operadores", TalaoFecho.m(g.totalContado));
            t.negrito(d.signum() == 0 ? "Diferença (certo)" : d.signum() > 0 ? "Sobra" : "Falta", TalaoFecho.sinal(d));
        }
        if (g.totalQuartos.signum() != 0) {
            t.linha("Lançado nos quartos (não entra)", TalaoFecho.m(g.totalQuartos));
        }

        if (!g.porForma.isEmpty()) {
            t.seccao("FORMAS DE PAGAMENTO", null);
            for (Map.Entry<String, BigDecimal[]> e : g.porForma.entrySet()) {
                t.negrito(e.getKey(), TalaoFecho.m(e.getValue()[0]));
                if (e.getValue()[1] != null) {
                    t.linha("  contado", TalaoFecho.m(e.getValue()[1]));
                }
            }
        }

        t.seccao("FECHOS POR UTILIZADOR", String.valueOf(g.fechos.size()));
        if (g.fechos.isEmpty()) {
            t.nota("Nenhum fecho de caixa neste período.");
        }
        for (FechoCaixa.Resumo r : g.fechos) {
            t.negrito(r.nomeUtilizador, TalaoFecho.m(r.totalEsperado()));
            t.doc((r.tipoUtilizador.isEmpty() ? "" : r.tipoUtilizador + " · ") + "Fecho Nº " + r.numero()
                    + " · Caixa " + r.pkCaixa + "\n" + DH.format(r.abertura) + " a " + DH.format(r.fecho), null);
            t.doc("Vendas FR: " + r.numeroVendas, TalaoFecho.m(r.totalVendas));
            if (r.numeroRecibos > 0) {
                t.doc("Recibos RC: " + r.numeroRecibos, TalaoFecho.m(r.totalRecibos));
            }
            for (FechoCaixa.Linha l : r.linhas) {
                if (l.esperado().signum() != 0) {
                    t.doc("  " + l.nome, TalaoFecho.m(l.esperado()));
                }
            }
            BigDecimal c = r.totalContado();
            if (c != null) {
                BigDecimal d = c.subtract(r.totalEsperado());
                t.doc("Contado " + TalaoFecho.m(c), d.signum() == 0 ? "certo" : (d.signum() > 0 ? "sobra " : "falta ") + TalaoFecho.m(d.abs()));
            }
            if (r.totalQuartos.signum() != 0) {
                t.doc("Lançado nos quartos (não entra)", TalaoFecho.m(r.totalQuartos));
            }
            t.separador();
        }

        if (!g.notasCredito.isEmpty()) {
            t.seccao("NOTAS DE CRÉDITO (NC)", String.valueOf(g.notasCredito.size()));
            t.nota("Notas de crédito emitidas no período: anulam a FR / RC de origem (que deixa de contar no total do seu fecho) e devolvem o stock.");
            for (FechoCaixa.Documento d : g.notasCredito) {
                t.doc(d.numero + "  " + new SimpleDateFormat("dd/MM HH:mm").format(d.data) + "\nAnula: " + d.origem
                        + "\n" + d.operador, TalaoFecho.m(d.valor));
            }
            t.negrito("Total das notas de crédito", TalaoFecho.m(g.totalNotasCredito));
        }

        if (!g.abertas.isEmpty()) {
            t.seccao("ATENÇÃO: CAIXAS POR FECHAR", String.valueOf(g.abertas.size()));
            t.nota("Utilizadores que receberam pagamentos neste período e ainda não fecharam a caixa (valores do período, fora do total).");
            for (FechoCaixa.Resumo r : g.abertas) {
                t.doc(r.nomeUtilizador + (r.tipoUtilizador.isEmpty() ? "" : " (" + r.tipoUtilizador + ")")
                        + "\nCaixa " + r.pkCaixa + " aberta desde " + DH.format(r.abertura), TalaoFecho.m(r.totalEsperado()));
            }
        }

        if (!g.semCaixa.isEmpty()) {
            t.seccao("PAGAMENTOS SEM CAIXA", null);
            t.nota("Pagamentos emitidos sem caixa aberta (fora do total).");
            for (SemCaixa s : g.semCaixa) {
                t.doc(s.nome + (s.tipo.isEmpty() ? "" : " (" + s.tipo + ")") + "\nFR " + s.fr + " · RC " + s.rc, TalaoFecho.m(s.valor));
            }
            t.negrito("Total sem caixa", TalaoFecho.m(g.totalSemCaixa));
        }

        t.separador();
        t.linha("Responsável:", null);
        t.linha(" ", null);
        t.linha("____________________", null);
        return t;
    }

    public static String qr(Connection cn, Resultado g) {
        StringBuilder s = new StringBuilder("KITANDA-FECHO-GERAL");
        s.append("|NIF=").append(TalaoFecho.nif(cn));
        s.append("|DE=").append(ISO.format(g.inicio)).append("|ATE=").append(ISO.format(g.fim));
        s.append("|FECHOS=").append(g.fechos.size());
        s.append("|FR=").append(g.numeroVendas).append(':').append(g.totalVendas.toPlainString());
        s.append("|RC=").append(g.numeroRecibos).append(':').append(g.totalRecibos.toPlainString());
        s.append("|TOTAL=").append(g.total().toPlainString());
        if (g.totalContado != null) {
            s.append("|CONTADO=").append(g.totalContado.toPlainString());
        }
        if (!g.abertas.isEmpty()) {
            s.append("|ABERTAS=").append(g.abertas.size());
        }
        if (!g.notasCredito.isEmpty()) {
            s.append("|NC=").append(g.notasCredito.size()).append(':').append(g.totalNotasCredito.toPlainString());
        }
        if (g.totalSemCaixa.signum() != 0) {
            s.append("|SEMCAIXA=").append(g.totalSemCaixa.toPlainString());
        }
        return TalaoFecho.comControlo(s.toString());
    }

    public static JasperPrint gerar(Connection cn, Resultado g) throws Exception {
        return TalaoFecho.gerar(cn, linhas(cn, g), qr(cn, g), "QR de controlo do fecho geral de " + D.format(g.fim));
    }

    /** Texto para o e-mail / WhatsApp. */
    public static String texto(Resultado g, String hotel) {
        StringBuilder s = new StringBuilder();
        s.append("*").append(hotel).append("* · Fecho geral\n");
        s.append("De ").append(DH.format(g.inicio)).append(" a ").append(DH.format(g.fim)).append("\n");
        s.append("Fechos de caixa: ").append(g.fechos.size()).append("\n");
        s.append("Vendas (FR): ").append(g.numeroVendas).append(" · ").append(TalaoFecho.m(g.totalVendas)).append(" Kz\n");
        s.append("Recibos (RC): ").append(g.numeroRecibos).append(" · ").append(TalaoFecho.m(g.totalRecibos)).append(" Kz\n");
        for (Map.Entry<String, BigDecimal[]> e : g.porForma.entrySet()) {
            s.append("· ").append(e.getKey()).append(": ").append(TalaoFecho.m(e.getValue()[0])).append("\n");
        }
        s.append("TOTAL GERAL: ").append(TalaoFecho.m(g.total())).append(" Kz\n");
        for (FechoCaixa.Resumo r : g.fechos) {
            s.append("- ").append(r.nomeUtilizador).append(r.tipoUtilizador.isEmpty() ? "" : " (" + r.tipoUtilizador + ")")
                    .append(", fecho Nº ").append(r.numero()).append(": ").append(TalaoFecho.m(r.totalEsperado())).append(" Kz\n");
        }
        if (g.totalQuartos.signum() != 0) {
            s.append("Lançado nos quartos (não entra no total): ").append(TalaoFecho.m(g.totalQuartos)).append(" Kz\n");
        }
        if (!g.notasCredito.isEmpty()) {
            s.append("Notas de crédito (NC): ").append(g.notasCredito.size()).append(" · ")
                    .append(TalaoFecho.m(g.totalNotasCredito)).append(" Kz");
            for (FechoCaixa.Documento d : g.notasCredito) {
                s.append("\n  ").append(d.numero).append(" anula ").append(d.origem);
            }
            s.append("\n");
        }
        if (!g.abertas.isEmpty()) {
            s.append("ATENÇÃO: ").append(g.abertas.size()).append(" caixa(s) por fechar:");
            for (FechoCaixa.Resumo r : g.abertas) {
                s.append(" ").append(r.nomeUtilizador).append(";");
            }
            s.append("\n");
        }
        if (g.totalSemCaixa.signum() != 0) {
            s.append("ATENÇÃO: pagamentos sem caixa aberta: ").append(TalaoFecho.m(g.totalSemCaixa)).append(" Kz\n");
        }
        return s.toString();
    }

    // =====================================================================
    // EXECUÇÃO (automática ou pedida)
    // =====================================================================
    /**
     * Reserva o fecho geral do dia (tb_fecho_geral, data única).
     *
     * @return true se este computador ficou com ele; false se já existe
     */
    public static boolean reservar(Connection cn, Date referencia, Date[] periodo, String modo, String origem, Integer usuario)
            throws SQLException {
        garantirTabelas(cn);
        try (PreparedStatement ps = cn.prepareStatement("INSERT IGNORE INTO tb_fecho_geral (data_referencia, inicio, fim,"
                + " gerado_em, modo, origem, fk_usuario, estado) VALUES (?, ?, ?, NOW(), ?, ?, ?, 'A gerar')")) {
            ps.setDate(1, new java.sql.Date(referencia.getTime()));
            ps.setTimestamp(2, new Timestamp(periodo[0].getTime()));
            ps.setTimestamp(3, new Timestamp(periodo[1].getTime()));
            ps.setString(4, modo);
            ps.setString(5, origem);
            if (usuario == null) {
                ps.setNull(6, java.sql.Types.INTEGER);
            } else {
                ps.setInt(6, usuario);
            }
            return ps.executeUpdate() == 1;
        }
    }

    public static void concluir(Connection cn, Date referencia, Resultado g, String estado, String detalhe) throws SQLException {
        try (PreparedStatement ps = cn.prepareStatement("UPDATE tb_fecho_geral SET estado = ?, detalhe = ?, numero_fechos = ?,"
                + " total = ? WHERE data_referencia = ?")) {
            ps.setString(1, estado);
            ps.setString(2, detalhe == null ? null : detalhe.length() > 1000 ? detalhe.substring(0, 1000) : detalhe);
            ps.setInt(3, g == null ? 0 : g.fechos.size());
            ps.setBigDecimal(4, g == null ? BigDecimal.ZERO : g.total());
            ps.setDate(5, new java.sql.Date(referencia.getTime()));
            ps.executeUpdate();
        }
    }

    /** Liberta a reserva (falha antes de gerar): outro computador pode tentar. */
    public static void libertar(Connection cn, Date referencia) {
        try (PreparedStatement ps = cn.prepareStatement("DELETE FROM tb_fecho_geral WHERE data_referencia = ? AND estado = 'A gerar'")) {
            ps.setDate(1, new java.sql.Date(referencia.getTime()));
            ps.executeUpdate();
        } catch (SQLException e) {
            // fica para a próxima verificação
        }
    }

    /** Guarda o PDF em anexos/fechos/Fecho_Geral_aaaa-mm-dd.pdf. */
    public static File guardarPdf(JasperPrint jp, Resultado g) throws Exception {
        EnvioFechoCaixa.PASTA.mkdirs();
        File f = new File(EnvioFechoCaixa.PASTA, "Fecho_Geral_" + new SimpleDateFormat("yyyy-MM-dd").format(g.fim) + ".pdf");
        JasperExportManager.exportReportToPdfFile(jp, f.getPath());
        return f;
    }

    /** Envia o PDF do fecho geral a quem recebe os fechos. @return envios entregues */
    public static int enviar(Connection cn, Resultado g, File pdf, Integer usuario) {
        String titulo = "Fecho geral de " + D.format(g.fim);
        return EnvioFechoCaixa.enviarPdf(cn, titulo, texto(g, EnvioFechoCaixa.nomeHotel(cn)), pdf, null, usuario);
    }
}
