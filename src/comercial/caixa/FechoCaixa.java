package comercial.caixa;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Fecho de caixa (vendas, restaurante, POS e também administradores que
 * recebam pagamentos): o resumo da caixa do utilizador e a
 * gravação do fecho. Substitui as contas que estavam no CaixaFechoGoldVisao
 * (verificação de 2026-11-14):
 * - o total vendido era SUM(valor_real) − SUM(troco), mas o valor_real já vem
 *   sem o troco: o troco era tirado duas vezes (ex.: caixa 1278, −44 300 Kz);
 * - o IVA, os descontos e o ilíquido eram calculados mas não eram gravados
 *   (todos os fechos de 2026 com NULL);
 * - o valor contado (declarado) ficava sempre 0.
 *
 * Vendas = Facturas/Recibo (FR, documento 1) do utilizador entre a abertura e o
 * fecho, como antes. Recibos (RC, documento 6) — pagamentos de facturas a crédito
 * recebidos na caixa — entram no fecho (pedido do utilizador em 2026-11-14), à
 * parte, e contam no valor esperado em caixa. O talão lista cada documento.
 */
public final class FechoCaixa {

    static final int DOC_FR = 1;
    static final int DOC_RC = 6;
    static final int DOC_NC = 5;
    /** Linha dos documentos sem forma de pagamento registada. */
    public static final int FORMA_NAO_REGISTADA = 0;

    private FechoCaixa() {
    }

    /** Uma forma de pagamento no fecho. */
    public static final class Linha {

        public final int forma;
        public final String nome;
        public BigDecimal vendas = BigDecimal.ZERO;   // FR
        public BigDecimal recibos = BigDecimal.ZERO;  // RC
        /** Devoluções de saldo da conta do cliente (sempre em numerário): saem da gaveta. */
        public BigDecimal devolucoes = BigDecimal.ZERO;
        /** Cauções recebidas (documento interno CAU): entram na gaveta mas não são vendas. */
        public BigDecimal caucoes = BigDecimal.ZERO;
        /** Contado na gaveta / no extracto; null = não indicado. */
        public BigDecimal contado;

        Linha(int forma, String nome) {
            this.forma = forma;
            this.nome = nome;
        }

        /** O que devia estar em caixa: vendas + recibos + cauções − devoluções. */
        public BigDecimal esperado() {
            return vendas.add(recibos).add(caucoes).subtract(devolucoes);
        }

        /** contado − esperado; null se não foi contado. */
        public BigDecimal diferenca() {
            return contado == null ? null : contado.subtract(esperado());
        }
    }

    /** Um documento de pagamento (FR ou RC) da caixa. */
    public static final class Documento {

        public int codigo;
        public int tipo;          // DOC_FR ou DOC_RC
        public String numero = "";
        public Date data;
        public String cliente = "";
        public String formas = "";
        public BigDecimal valor = BigDecimal.ZERO;
        BigDecimal semForma;
        /** RC: as facturas (FT) que pagou; NC: o documento anulado (FR / RC). */
        public String origem = "";
        /** FR / RC anulado depois por uma nota de crédito: o número da NC. */
        public String anuladaPor = "";
        /** NC: quem emitiu o documento de origem (quando não é o mesmo operador). */
        public String operador = "";
        /** Estado na AGT (V, P, I); I = inválido (o número pode ter sido reutilizado noutro documento). */
        public String estado = "";

        public boolean invalido() {
            return "I".equalsIgnoreCase(estado);
        }

        public String sigla() {
            return tipo == DOC_RC ? "RC" : tipo == DOC_NC ? "NC" : "FR";
        }
    }

    /** O estado da caixa do utilizador. */
    public static final class Resumo {

        public int pkCaixa;
        public int idUsuario;
        public String nomeUtilizador = "";
        public String tipoUtilizador = "";
        public Date abertura;
        public Date fecho;          // preenchido se já estiver fechada
        public Integer numeroFecho; // idem

        /** Número do fecho para mostrar; as caixas fechadas antes da numeração ficam com o nº da caixa. */
        public String numero() {
            return numeroFecho != null ? String.valueOf(numeroFecho) : "s/n (caixa " + pkCaixa + ")";
        }
        public String utilizadorAbertura = "";
        public String utilizadorFecho = "";
        public String area = "";
        public double valorInicial;
        public Date ate;            // fim do período calculado (agora, para a caixa aberta)
        public int numeroVendas, numeroRecibos;
        public BigDecimal totalVendas = BigDecimal.ZERO, totalIva = BigDecimal.ZERO, totalDesconto = BigDecimal.ZERO,
                totalIliquido = BigDecimal.ZERO, totalRecibos = BigDecimal.ZERO;
        public final List<Linha> linhas = new ArrayList<>();
        public final List<Documento> documentos = new ArrayList<>();
        /** Facturas a crédito (FT) emitidas: não são pagamento, não entram no total. */
        public final List<Documento> facturasCredito = new ArrayList<>();
        public BigDecimal totalFacturasCredito = BigDecimal.ZERO;
        /** Notas de crédito (NC) de FR / RC emitidas pelo operador nesta caixa, com o documento de origem. */
        /** FR / RC desta caixa anulados por NC (status_eliminado = 'true'): não contam no total. */
        public final List<Documento> anulados = new ArrayList<>();
        /** FR / RC inválidos na AGT (estado I) no período: não contam no fecho. */
        public int numeroInvalidos;
        public BigDecimal totalInvalidos = BigDecimal.ZERO;
        public final List<Documento> notasCredito = new ArrayList<>();
        public BigDecimal totalNotasCredito = BigDecimal.ZERO;
        /** Consumos lançados na conta dos quartos (Restaurante/Bar): quarto, descrição, qtd, valor, hora. Não entram no total. */
        public final List<String[]> consumosQuartos = new ArrayList<>();
        public BigDecimal totalQuartos = BigDecimal.ZERO;
        /** Produtos facturados (FR + FT): quantidade, designação, total. */
        public final List<String[]> produtos = new ArrayList<>();

        public boolean fechada() {
            return fecho != null;
        }

        public BigDecimal totalEsperado() {
            return totalVendas.add(totalRecibos);
        }

        public BigDecimal totalContado() {
            BigDecimal t = null;
            for (Linha l : linhas) {
                if (l.contado != null) {
                    t = (t == null ? BigDecimal.ZERO : t).add(l.contado);
                }
            }
            return t;
        }

        /** Houve dinheiro a passar por esta caixa. */
        public boolean temMovimento() {
            return numeroVendas > 0 || numeroRecibos > 0;
        }
    }

    // =====================================================================
    // RESUMO
    // =====================================================================
    /**
     * A última caixa aberta pelo utilizador, com os valores calculados até agora
     * (ou até ao fecho, se já estiver fechada). null se o utilizador nunca abriu caixa.
     */
    public static Resumo ler(Connection cn, int idUser) throws SQLException {
        Integer pk = null;
        try (PreparedStatement ps = cn.prepareStatement("SELECT MAX(pk_caixa) FROM caixa WHERE cod_usuario_abertura = ?")) {
            ps.setInt(1, idUser);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int n = rs.getInt(1);
                    pk = rs.wasNull() ? null : n;
                }
            }
        }
        return pk == null ? null : lerPorId(cn, pk);
    }

    /**
     * Uma caixa qualquer (reimpressão, fecho geral): valores calculados entre a
     * abertura e o fecho (ou até agora, se ainda estiver aberta) e os valores
     * contados gravados no fecho. null se não existir.
     */
    public static Resumo lerPorId(Connection cn, int pkCaixa) throws SQLException {
        garantirEstrutura(cn);
        Resumo r = new Resumo();
        try (PreparedStatement ps = cn.prepareStatement("SELECT c.pk_caixa, c.data_abertura, c.data_fecho, c.numero_fecho,"
                + " c.usuario_abertura, c.valor_inicial, c.cod_usuario_abertura, c.usuario_fecho, u.nome, t.descricao"
                + " FROM caixa c LEFT JOIN tb_usuario u ON u.codigo = c.cod_usuario_abertura"
                + " LEFT JOIN tb_tipo_usuario t ON t.idTipoUsuario = u.idTipoUsuario WHERE c.pk_caixa = ?")) {
            ps.setInt(1, pkCaixa);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }
                r.pkCaixa = rs.getInt(1);
                r.abertura = rs.getTimestamp(2);
                r.fecho = rs.getTimestamp(3);
                int n = rs.getInt(4);
                r.numeroFecho = rs.wasNull() ? null : n;
                r.utilizadorAbertura = texto(rs.getString(5));
                r.valorInicial = rs.getDouble(6);
                r.idUsuario = rs.getInt(7);
                r.utilizadorFecho = texto(rs.getString(8));
                r.nomeUtilizador = texto(rs.getString(9));
                if (r.nomeUtilizador.isEmpty()) {
                    r.nomeUtilizador = r.utilizadorAbertura;
                }
                r.tipoUtilizador = texto(rs.getString(10));
            }
        }
        if (r.abertura == null) {
            r.abertura = new Date(0);
        }
        calcular(cn, r, r.idUsuario, r.fecho != null ? r.fecho : new Date());
        detalhar(cn, r);
        if (r.fechada()) {
            lerContados(cn, r);
        }
        return r;
    }

    /**
     * Os valores contados gravados (item_caixa.valor_declarado). Desde 2026-11-14
     * uma forma não contada fica NULL; nos fechos antigos ficava sempre 0 — se
     * todos forem 0 e não houver nenhum NULL, o fecho é antigo e não teve contagem.
     */
    private static void lerContados(Connection cn, Resumo r) throws SQLException {
        Map<Integer, BigDecimal> contados = new LinkedHashMap<>();
        boolean temNulo = false, temValor = false;
        try (PreparedStatement ps = cn.prepareStatement("SELECT fk_forma_pagamento, valor_declarado, area FROM item_caixa"
                + " WHERE fk_caixa = ?")) {
            ps.setInt(1, r.pkCaixa);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    double v = rs.getDouble(2);
                    if (rs.wasNull()) {
                        temNulo = true;
                        continue;
                    }
                    if (v != 0) {
                        temValor = true;
                    }
                    contados.put(rs.getInt(1), BigDecimal.valueOf(v));
                    if (r.area.isEmpty()) {
                        r.area = texto(rs.getString(3));
                    }
                }
            }
        }
        if (!temNulo && !temValor) {
            return; // fecho antigo, sem contagem
        }
        for (Linha l : r.linhas) {
            l.contado = contados.get(l.forma);
        }
    }

    /** Calcula as vendas (FR), os recibos (RC), os totais e os documentos entre a abertura e {@code ate}. */
    static void calcular(Connection cn, Resumo r, int idUser, Date ate) throws SQLException {
        r.ate = ate;
        r.linhas.clear();
        r.documentos.clear();
        r.anulados.clear();
        Map<Integer, Linha> porForma = new LinkedHashMap<>();
        // conta do cliente (fase 4): parte de uma FR paga com o saldo (adiantamento recebido
        // antes, já contado na caixa desse dia) - aparece no documento mas não soma
        final int formaAdiantamento = -1; // o comercial não tem conta do cliente (saldo/adiantamentos)
        try (Statement st = cn.createStatement();
                ResultSet rs = st.executeQuery("SELECT pk_forma_pagamento, designacao FROM forma_pagamento ORDER BY pk_forma_pagamento")) {
            while (rs.next()) {
                if (rs.getInt(1) == formaAdiantamento) {
                    continue; // saldo da conta do cliente: não é dinheiro da caixa
                }
                Linha l = new Linha(rs.getInt(1), texto(rs.getString(2)));
                porForma.put(l.forma, l);
                r.linhas.add(l);
            }
        }
        Timestamp de = new Timestamp(r.abertura.getTime()), a = new Timestamp(ate.getTime());

        // cada documento, com o valor recebido por forma (valor_real já vem sem o troco)
        Map<Integer, Documento> docs = new LinkedHashMap<>();
        try (PreparedStatement ps = cn.prepareStatement("SELECT v.codigo, v.fk_documento, v.cod_fact, v.dataVenda, v.hora,"
                + " COALESCE(NULLIF(TRIM(v.nome_consumidor_final), ''), NULLIF(TRIM(v.nome_cliente), ''), cl.nome, ''),"
                + " i.fk_forma_pagamento, COALESCE(i.valor_real, 0), COALESCE(v.total_venda, 0), ''"
                + " FROM tb_venda v LEFT JOIN tb_cliente cl ON cl.codigo = v.codigo_cliente"
                + " LEFT JOIN forma_pagamento_item i ON i.fk_venda = v.codigo"
                + " WHERE v.fk_documento IN (" + DOC_FR + ", " + DOC_RC + ")"
                + " AND (v.status_eliminado IS NULL OR v.status_eliminado = 'false')"
                + " AND v.codigo_usuario = ? AND v.dataVenda BETWEEN ? AND ? ORDER BY v.dataVenda, v.codigo")) {
            ps.setInt(1, idUser);
            ps.setTimestamp(2, de);
            ps.setTimestamp(3, a);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int codigo = rs.getInt(1);
                    Documento d = docs.get(codigo);
                    if (d == null) {
                        d = new Documento();
                        d.codigo = codigo;
                        d.tipo = rs.getInt(2);
                        d.numero = texto(rs.getString(3));
                        d.data = rs.getTimestamp(4);
                        d.cliente = texto(rs.getString(6));
                        d.semForma = rs.getBigDecimal(9);
                        d.estado = texto(rs.getString(10));
                        docs.put(codigo, d);
                    }
                    int forma = rs.getInt(7);
                    if (rs.wasNull()) {
                        continue;
                    }
                    BigDecimal valor = rs.getBigDecimal(8);
                    if (forma == formaAdiantamento && formaAdiantamento > 0) {
                        String nome = "Adiantamento " + kitanda.util.CfMethods.formatarComoMoeda(valor.doubleValue());
                        d.formas = d.formas.isEmpty() ? nome : d.formas + ", " + nome;
                        d.semForma = BigDecimal.ZERO; // tem forma registada
                        continue;
                    }
                    Linha l = porForma.get(forma);
                    if (l == null) {
                        l = new Linha(forma, "Forma " + forma);
                        porForma.put(forma, l);
                        r.linhas.add(l);
                    }
                    if (d.tipo == DOC_FR) {
                        l.vendas = l.vendas.add(valor);
                    } else {
                        l.recibos = l.recibos.add(valor);
                    }
                    d.valor = d.valor.add(valor);
                    if (!d.formas.contains(l.nome)) {
                        d.formas = d.formas.isEmpty() ? l.nome : d.formas + ", " + l.nome;
                    }
                }
            }
        }
        // documentos sem forma de pagamento registada (falha antiga, sobretudo Julho de 2026):
        // contam pelo total do documento numa linha própria, para o dinheiro não desaparecer do fecho
        for (Documento d : docs.values()) {
            if (!d.formas.isEmpty() || d.semForma == null || d.semForma.signum() == 0) {
                continue;
            }
            Linha l = porForma.get(FORMA_NAO_REGISTADA);
            if (l == null) {
                l = new Linha(FORMA_NAO_REGISTADA, "Forma não registada");
                porForma.put(l.forma, l);
                r.linhas.add(l);
            }
            if (d.tipo == DOC_FR) {
                l.vendas = l.vendas.add(d.semForma);
            } else {
                l.recibos = l.recibos.add(d.semForma);
            }
            d.valor = d.semForma;
            d.formas = l.nome;
        }
        r.documentos.addAll(docs.values());
        origens(cn, r.documentos);
        // FR / RC do período anulados por NC: ficam fora do total (a FR costuma ser reemitida
        // corrigida) mas aparecem no talão com o número da NC
        try (PreparedStatement ps = cn.prepareStatement("SELECT v.codigo, v.fk_documento, v.cod_fact, v.dataVenda,"
                + " COALESCE(NULLIF(TRIM(v.nome_consumidor_final), ''), NULLIF(TRIM(v.nome_cliente), ''), cl.nome, ''),"
                + " COALESCE(v.total_venda, 0), '' FROM tb_venda v LEFT JOIN tb_cliente cl ON cl.codigo = v.codigo_cliente"
                + " WHERE v.fk_documento IN (" + DOC_FR + ", " + DOC_RC + ") AND v.status_eliminado = 'true'"
                + " AND v.codigo_usuario = ? AND v.dataVenda BETWEEN ? AND ? ORDER BY v.dataVenda, v.codigo")) {
            ps.setInt(1, idUser);
            ps.setTimestamp(2, de);
            ps.setTimestamp(3, a);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Documento d = new Documento();
                    d.codigo = rs.getInt(1);
                    d.tipo = rs.getInt(2);
                    d.numero = texto(rs.getString(3));
                    d.data = rs.getTimestamp(4);
                    d.cliente = texto(rs.getString(5));
                    d.valor = rs.getBigDecimal(6);
                    d.estado = texto(rs.getString(7));
                    r.anulados.add(d);
                }
            }
        }
        origens(cn, r.anulados);
        // modo SAFT: não há documentos inválidos na AGT
        r.numeroInvalidos = 0;
        r.totalInvalidos = BigDecimal.ZERO;

        // totais dos documentos (contagens, IVA, descontos, ilíquido)
        String totaisSql = "SELECT v.fk_documento, COUNT(*), COALESCE(SUM(v.total_iva), 0), COALESCE(SUM(v.desconto_total), 0),"
                + " COALESCE(SUM(v.total_geral), 0) FROM tb_venda v"
                + " WHERE v.fk_documento IN (" + DOC_FR + ", " + DOC_RC + ")"
                + " AND (v.status_eliminado IS NULL OR v.status_eliminado = 'false')"
                + " AND v.codigo_usuario = ? AND v.dataVenda BETWEEN ? AND ? GROUP BY v.fk_documento";
        r.numeroVendas = 0;
        r.numeroRecibos = 0;
        r.totalIva = BigDecimal.ZERO;
        r.totalDesconto = BigDecimal.ZERO;
        r.totalIliquido = BigDecimal.ZERO;
        try (PreparedStatement ps = cn.prepareStatement(totaisSql)) {
            ps.setInt(1, idUser);
            ps.setTimestamp(2, de);
            ps.setTimestamp(3, a);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    if (rs.getInt(1) == DOC_FR) {
                        r.numeroVendas = rs.getInt(2);
                        r.totalIva = rs.getBigDecimal(3);
                        r.totalDesconto = rs.getBigDecimal(4);
                        r.totalIliquido = rs.getBigDecimal(5);
                    } else {
                        r.numeroRecibos = rs.getInt(2);
                    }
                }
            }
        }
        devolucoes(cn, r, idUser, de, a);
        caucoes(cn, r, idUser, de, a);
        r.totalVendas = BigDecimal.ZERO;
        r.totalRecibos = BigDecimal.ZERO;
        for (Linha l : r.linhas) {
            r.totalVendas = r.totalVendas.add(l.vendas);
            r.totalRecibos = r.totalRecibos.add(l.recibos);
        }
    }

    /**
     * Cauções recebidas pelo operador no período (2026-11-16): somam-se à
     * forma de pagamento com que entraram (não são vendas nem documentos fiscais).
     */
    static void caucoes(Connection cn, Resumo r, int idUser, Timestamp de, Timestamp a) {
        // o comercial não recebe cauções (só no hotel)
    }

    /**
     * Devoluções de saldo da conta do cliente feitas pelo operador no período
     * (movimentos "Devolução" não estornados): numerário que saiu da gaveta.
     */
    static void devolucoes(Connection cn, Resumo r, int idUser, Timestamp de, Timestamp a) {
        // o comercial não tem conta do cliente: não há devoluções de saldo
    }

    /**
     * O que não é dinheiro mas tem de constar no fecho: as notas de crédito e os
     * produtos facturados.
     */
    static void detalhar(Connection cn, Resumo r) throws SQLException {
        r.facturasCredito.clear();
        r.notasCredito.clear();
        r.totalNotasCredito = BigDecimal.ZERO;
        r.consumosQuartos.clear();
        r.produtos.clear();
        r.totalFacturasCredito = BigDecimal.ZERO;
        r.totalQuartos = BigDecimal.ZERO;
        Timestamp de = new Timestamp(r.abertura.getTime()), a = new Timestamp(r.ate.getTime());
        // as facturas a crédito (FT) não entram no fecho (pedido do utilizador em 2026-11-14)
        r.notasCredito.addAll(notasCredito(cn, r.idUsuario, de, a));
        for (Documento d : r.notasCredito) {
            r.totalNotasCredito = r.totalNotasCredito.add(d.valor);
        }
        try (PreparedStatement ps = cn.prepareStatement("SELECT SUM(it.quantidade), COALESCE(p.designacao, it.designacao_item, ''),"
                + " SUM(it.total) FROM tb_item_venda it JOIN tb_venda v ON v.codigo = it.codigo_venda"
                + " LEFT JOIN tb_produto p ON p.codigo = it.codigo_produto"
                + " WHERE v.fk_documento = " + DOC_FR + " AND (v.status_eliminado IS NULL OR v.status_eliminado = 'false')"
                + " AND v.codigo_usuario = ? AND v.dataVenda BETWEEN ? AND ?"
                + " GROUP BY it.codigo_produto, 2 ORDER BY 2")) {
            ps.setInt(1, r.idUsuario);
            ps.setTimestamp(2, de);
            ps.setTimestamp(3, a);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    r.produtos.add(new String[]{new java.text.DecimalFormat("0.##").format(rs.getDouble(1)),
                        texto(rs.getString(2)), rs.getBigDecimal(3).toPlainString()});
                }
            }
        }
    }

    /**
     * Notas de crédito (NC) que anulam FR ou RC, emitidas entre {@code de} e
     * {@code a} (por um utilizador, ou por todos se idUsuario <= 0), com o
     * documento de origem e quem o emitiu.
     */
    public static List<Documento> notasCredito(Connection cn, int idUsuario, Timestamp de, Timestamp a) throws SQLException {
        List<Documento> l = new ArrayList<>();
        try (PreparedStatement ps = cn.prepareStatement("SELECT n.codigo, n.cod_fact, n.dataVenda, n.ref_cod_fact,"
                + " COALESCE(NULLIF(TRIM(n.nome_consumidor_final), ''), NULLIF(TRIM(n.nome_cliente), ''), cl.nome, ''),"
                + " COALESCE(n.total_venda, 0), un.nome, uo.nome, o.codigo_usuario, n.codigo_usuario"
                + " FROM tb_venda n LEFT JOIN tb_cliente cl ON cl.codigo = n.codigo_cliente"
                + " LEFT JOIN tb_usuario un ON un.codigo = n.codigo_usuario"
                + " LEFT JOIN tb_venda o ON o.cod_fact = TRIM(n.ref_cod_fact) AND o.fk_documento IN (1, 6)"
                + " LEFT JOIN tb_usuario uo ON uo.codigo = o.codigo_usuario"
                + " WHERE n.fk_documento = " + DOC_NC + " AND (n.status_eliminado IS NULL OR n.status_eliminado <> 'true')" // a NC fica com 'ANULADO'
                + " AND (TRIM(n.ref_cod_fact) LIKE 'FR%' OR TRIM(n.ref_cod_fact) LIKE 'RC%')"
                + (idUsuario > 0 ? " AND n.codigo_usuario = ?" : "")
                + " AND n.dataVenda BETWEEN ? AND ? GROUP BY n.codigo ORDER BY n.dataVenda, n.codigo")) {
            int i = 1;
            if (idUsuario > 0) {
                ps.setInt(i++, idUsuario);
            }
            ps.setTimestamp(i++, de);
            ps.setTimestamp(i, a);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Documento d = new Documento();
                    d.codigo = rs.getInt(1);
                    d.tipo = DOC_NC;
                    d.numero = texto(rs.getString(2));
                    d.data = rs.getTimestamp(3);
                    d.origem = texto(rs.getString(4));
                    d.cliente = texto(rs.getString(5));
                    d.valor = rs.getBigDecimal(6);
                    String emissor = texto(rs.getString(7)), operadorOrigem = texto(rs.getString(8));
                    int uo = rs.getInt(9);
                    boolean semOrigem = rs.wasNull();
                    d.operador = idUsuario > 0
                            ? (semOrigem || uo == rs.getInt(10) ? "" : "FR/RC de " + operadorOrigem)
                            : "NC de " + emissor + (semOrigem || uo == rs.getInt(10) || operadorOrigem.isEmpty() ? "" : " · origem de " + operadorOrigem);
                    l.add(d);
                }
            }
        }
        return l;
    }

    /**
     * RC: as facturas (FT) que cada recibo pagou (item_venda_recibos; os RC
     * antigos podem não ter). FR / RC: a nota de crédito que os anulou, se houver.
     */
    static void origens(Connection cn, List<Documento> docs) throws SQLException {
        if (docs.isEmpty()) {
            return;
        }
        Map<Integer, Documento> porCodigo = new LinkedHashMap<>();
        Map<String, Documento> porNumero = new LinkedHashMap<>();
        StringBuilder rcs = new StringBuilder(), numeros = new StringBuilder();
        for (Documento d : docs) {
            porCodigo.put(d.codigo, d);
            if (!d.invalido()) { // o número de um inválido pode pertencer a outro documento
                porNumero.put(d.numero.replace(" ", ""), d);
            }
            if (d.tipo == DOC_RC) {
                rcs.append(rcs.length() == 0 ? "" : ",").append(d.codigo);
            }
        }
        for (int i = 0; i < porNumero.size(); i++) {
            numeros.append(i == 0 ? "?" : ",?"); // um por número distinto (o cod_fact pode repetir-se)
        }
        if (rcs.length() > 0) {
            // a Emissão de Recibos regista cada FT paga em amortizacao_divida
            // (fk_venda = FT, ref_cod_fact = número do RC)
            StringBuilder numerosRc = new StringBuilder();
            List<String> rcNumeros = new ArrayList<>();
            Map<String, Documento> rcPorNumero = new LinkedHashMap<>();
            for (Documento d : docs) {
                if (d.tipo == DOC_RC && d.origem.isEmpty()) {
                    numerosRc.append(numerosRc.length() == 0 ? "?" : ",?");
                    rcNumeros.add(d.numero);
                    rcPorNumero.put(d.numero.replace(" ", ""), d);
                }
            }
            if (!rcNumeros.isEmpty()) {
                try (PreparedStatement ps = cn.prepareStatement("SELECT TRIM(a.ref_cod_fact), GROUP_CONCAT(DISTINCT TRIM(f.cod_fact)"
                        + " ORDER BY f.codigo SEPARATOR ', ') FROM amortizacao_divida a JOIN tb_venda f ON f.codigo = a.fk_venda"
                        + " WHERE TRIM(a.ref_cod_fact) IN (" + numerosRc + ") GROUP BY 1")) {
                    for (int i = 0; i < rcNumeros.size(); i++) {
                        ps.setString(i + 1, rcNumeros.get(i).trim());
                    }
                    try (ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) {
                            Documento d = rcPorNumero.get(texto(rs.getString(1)).replace(" ", ""));
                            if (d != null) {
                                d.origem = texto(rs.getString(2));
                            }
                        }
                    }
                }
            }
        }
        if (porNumero.isEmpty()) {
            return;
        }
        try (PreparedStatement ps = cn.prepareStatement("SELECT REPLACE(ref_cod_fact, ' ', ''), GROUP_CONCAT(TRIM(cod_fact) SEPARATOR ', ')"
                + " FROM tb_venda WHERE fk_documento = " + DOC_NC + " AND (status_eliminado IS NULL OR status_eliminado <> 'true')"
                + " AND REPLACE(ref_cod_fact, ' ', '') IN (" + numeros + ") GROUP BY 1")) {
            int i = 1;
            for (String n : porNumero.keySet()) {
                ps.setString(i++, n);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Documento d = porNumero.get(rs.getString(1));
                    if (d != null) {
                        d.anuladaPor = texto(rs.getString(2));
                    }
                }
            }
        }
    }

    // =====================================================================
    // FECHO
    // =====================================================================
    /**
     * Fecha a caixa numa transacção: volta a calcular até agora (vendas feitas
     * enquanto o formulário esteve aberto), bloqueia a linha da caixa (dois postos
     * não fecham a mesma caixa), dá o número de fecho e grava os totais e uma
     * linha por forma de pagamento (valor apurado e valor contado).
     *
     * @param contados contado por forma de pagamento (pk_forma_pagamento → valor); pode ter nulos
     * @return o resumo final gravado (com número e data de fecho)
     */
    public static Resumo fechar(Connection cn, int idUser, String nomeUtilizador, String area, Map<Integer, BigDecimal> contados)
            throws SQLException {
        garantirEstrutura(cn);
        boolean auto = cn.getAutoCommit();
        cn.setAutoCommit(false);
        try {
            int pk;
            try (PreparedStatement ps = cn.prepareStatement("SELECT pk_caixa, data_fecho"
                    + " FROM caixa WHERE pk_caixa = (SELECT MAX(pk_caixa) FROM caixa WHERE cod_usuario_abertura = ?) FOR UPDATE")) {
                ps.setInt(1, idUser);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        throw new SQLException("Não há caixa aberta para este utilizador.");
                    }
                    if (rs.getTimestamp(2) != null) {
                        throw new SQLException("Esta caixa já foi fechada (" + rs.getTimestamp(2) + ").");
                    }
                    pk = rs.getInt(1);
                }
            }
            Resumo r = lerPorId(cn, pk); // calcula até agora
            Date agora = r.ate;
            r.area = area == null ? "" : area;
            for (Linha l : r.linhas) {
                l.contado = contados == null ? null : contados.get(l.forma);
            }
            int numero;
            try (Statement st = cn.createStatement()) {
                st.executeUpdate("INSERT INTO sequencia_fecho VALUES (NULL)", Statement.RETURN_GENERATED_KEYS);
                try (ResultSet rs = st.getGeneratedKeys()) {
                    if (!rs.next()) {
                        throw new SQLException("Não foi possível gerar o número do fecho.");
                    }
                    numero = rs.getInt(1);
                }
            }
            try (PreparedStatement ps = cn.prepareStatement("UPDATE caixa SET data_fecho = ?, numero_fecho = ?, total_vendas = ?,"
                    + " numero_vendas = ?, usuario_fecho = ?, cod_usuario_fecho = ?, total_desconto = ?, total_iva = ?,"
                    + " total_iliquido = ? WHERE pk_caixa = ?")) {
                ps.setTimestamp(1, new Timestamp(agora.getTime()));
                ps.setInt(2, numero);
                ps.setDouble(3, r.totalVendas.doubleValue());
                ps.setInt(4, r.numeroVendas);
                ps.setString(5, nomeUtilizador);
                ps.setInt(6, idUser);
                ps.setBigDecimal(7, r.totalDesconto);
                ps.setBigDecimal(8, r.totalIva);
                ps.setBigDecimal(9, r.totalIliquido);
                ps.setInt(10, r.pkCaixa);
                ps.executeUpdate();
            }
            // uma linha por forma com movimento ou contada: valor_real = vendas FR (como antes),
            // declarado = contado (NULL = não contado)
            try (PreparedStatement ps = cn.prepareStatement("INSERT INTO item_caixa (valor_declarado, valor_real, fk_caixa,"
                    + " fk_forma_pagamento, area) VALUES (?, ?, ?, ?, ?)")) {
                int n = 0;
                for (Linha l : r.linhas) {
                    if (l.forma == FORMA_NAO_REGISTADA) {
                        continue; // não há forma a gravar
                    }
                    if (l.vendas.signum() == 0 && l.recibos.signum() == 0 && l.contado == null) {
                        continue;
                    }
                    if (l.contado == null) {
                        ps.setNull(1, java.sql.Types.DOUBLE);
                    } else {
                        ps.setDouble(1, l.contado.doubleValue());
                    }
                    ps.setDouble(2, l.vendas.doubleValue());
                    ps.setInt(3, r.pkCaixa);
                    ps.setInt(4, l.forma);
                    ps.setString(5, r.area);
                    ps.addBatch();
                    n++;
                }
                if (n > 0) {
                    ps.executeBatch();
                }
            }
            cn.commit();
            r.fecho = agora;
            r.numeroFecho = numero;
            r.utilizadorFecho = nomeUtilizador;
            return r;
        } catch (SQLException | RuntimeException e) {
            try {
                cn.rollback();
            } catch (SQLException ignorado) {
            }
            throw e;
        } finally {
            cn.setAutoCommit(auto);
        }
    }

    // =====================================================================
    // ABERTURA
    // =====================================================================
    /** pk da caixa aberta do utilizador (a última, sem data de fecho); null se não tiver. */
    public static Integer caixaAberta(Connection cn, int idUser) throws SQLException {
        try (PreparedStatement ps = cn.prepareStatement("SELECT pk_caixa, data_fecho FROM caixa WHERE pk_caixa ="
                + " (SELECT MAX(pk_caixa) FROM caixa WHERE cod_usuario_abertura = ?)")) {
            ps.setInt(1, idUser);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next() && rs.getTimestamp(2) == null) {
                    return rs.getInt(1);
                }
            }
        }
        return null;
    }

    /**
     * Abre uma caixa nova para o utilizador (transacção, com bloqueio para dois
     * cliques/postos não abrirem duas).
     *
     * @return o número (pk) da caixa aberta
     * @throws SQLException se o utilizador já tiver uma caixa aberta
     */
    public static int abrir(Connection cn, int idUser, String nomeUtilizador, double valorInicial) throws SQLException {
        boolean auto = cn.getAutoCommit();
        cn.setAutoCommit(false);
        try {
            try (PreparedStatement ps = cn.prepareStatement("SELECT codigo FROM tb_usuario WHERE codigo = ? FOR UPDATE")) {
                ps.setInt(1, idUser);
                ps.executeQuery().close();
            }
            Integer aberta = caixaAberta(cn, idUser);
            if (aberta != null) {
                throw new SQLException("Já tem a caixa Nº " + aberta + " aberta. Feche-a antes de abrir outra.");
            }
            int pk;
            try (PreparedStatement ps = cn.prepareStatement("INSERT INTO caixa (data_abertura, data_fecho, total_vendas,"
                    + " numero_vendas, valor_inicial, usuario_fecho, usuario_abertura, cod_usuario_abertura, cod_usuario_fecho)"
                    + " VALUES (NOW(), NULL, 0, 0, ?, '', ?, ?, 0)", Statement.RETURN_GENERATED_KEYS)) {
                ps.setDouble(1, valorInicial);
                ps.setString(2, nomeUtilizador);
                ps.setInt(3, idUser);
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    rs.next();
                    pk = rs.getInt(1);
                }
            }
            cn.commit();
            return pk;
        } catch (SQLException | RuntimeException e) {
            try {
                cn.rollback();
            } catch (SQLException ignorado) {
            }
            throw e;
        } finally {
            cn.setAutoCommit(auto);
        }
    }

    /** Nome e tipo (descrição) do utilizador. */
    public static String[] utilizador(Connection cn, int idUser) throws SQLException {
        try (PreparedStatement ps = cn.prepareStatement("SELECT u.nome, t.descricao, u.idTipoUsuario FROM tb_usuario u"
                + " LEFT JOIN tb_tipo_usuario t ON t.idTipoUsuario = u.idTipoUsuario WHERE u.codigo = ?")) {
            ps.setInt(1, idUser);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new String[]{texto(rs.getString(1)), texto(rs.getString(2)), String.valueOf(rs.getInt(3))};
                }
            }
        }
        return new String[]{"Utilizador " + idUser, "", "0"};
    }

    // =====================================================================
    // ESTRUTURA (o comercial não tinha o número do fecho nem a área)
    // =====================================================================
    private static volatile boolean estruturaVerificada;

    /**
     * Cria o que o fecho precisa e o comercial ainda não tinha, uma vez por
     * arranque: a sequência do número do fecho (sequencia_fecho),
     * caixa.numero_fecho e item_caixa.area. Os fechos antigos ficam sem número.
     */
    public static void garantirEstrutura(Connection cn) throws SQLException {
        if (estruturaVerificada) {
            return;
        }
        synchronized (FechoCaixa.class) {
            if (estruturaVerificada) {
                return;
            }
            try (Statement st = cn.createStatement()) {
                st.execute("CREATE TABLE IF NOT EXISTS sequencia_fecho (numero INT NOT NULL AUTO_INCREMENT PRIMARY KEY)"
                        + " ENGINE=InnoDB");
                if (!temColuna(cn, "caixa", "numero_fecho")) {
                    st.execute("ALTER TABLE caixa ADD COLUMN numero_fecho INT NULL");
                }
                if (!temColuna(cn, "item_caixa", "area")) {
                    st.execute("ALTER TABLE item_caixa ADD COLUMN area VARCHAR(60) NULL");
                }
            }
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

    static String texto(String s) {
        return s == null || "null".equalsIgnoreCase(s.trim()) ? "" : s.trim();
    }
}
