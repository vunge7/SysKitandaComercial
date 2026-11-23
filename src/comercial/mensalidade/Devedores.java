package comercial.mensalidade;

import entity.ConfiguracaoMesComeco;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import util.BDConexao;

/**
 * Situação de pagamento de todos os contratos de mensalidade, numa data de
 * referência. Lê tudo de uma vez (contratos, pagamentos, preços e multas: 4
 * consultas) e calcula em memória — o relatório filtra sem voltar à BD.
 */
public final class Devedores {

    public enum Situacao {
        ATRASO("Em atraso"), VENCE_HOJE("Vence hoje"), A_VENCER("A vencer"), EM_DIA("Em dia"), CONCLUIDO("Concluído"),
        INACTIVO("Inactivo");

        private final String texto;

        Situacao(String texto) {
            this.texto = texto;
        }

        public String texto() {
            return texto;
        }
    }

    /** Um contrato (cliente + serviço) e a sua situação. */
    public static final class Linha {

        public int clienteId, produtoId;
        public String cliente = "", telefone = "", nif = "", servico = "";
        public ConfiguracaoMesComeco contrato;
        public BigDecimal preco = BigDecimal.ZERO;
        public final List<Periodo> emAtraso = new ArrayList<>();
        /** Dias de atraso de cada mês em atraso (mesma ordem de emAtraso). */
        public final List<Long> diasPorMes = new ArrayList<>();
        /** Multa de cada mês em atraso (mesma ordem de emAtraso). */
        public final List<BigDecimal> multaPorMes = new ArrayList<>();
        /** Vencimento de cada mês em atraso (mesma ordem de emAtraso). */
        public final List<LocalDate> vencimentoPorMes = new ArrayList<>();
        /** Estado da inscrição do cliente (activo, desistente, desactivado). */
        public EstadoCliente.Registo estadoCliente = EstadoCliente.ACTIVO_SEM_REGISTO;

        public boolean inactivo() {
            return estadoCliente.inactivo();
        }
        public BigDecimal divida = BigDecimal.ZERO, multa = BigDecimal.ZERO;
        public long diasAtraso;            // do mês em atraso mais antigo
        public LocalDate proximoVencimento; // do primeiro mês por pagar que ainda não venceu
        public long diasParaVencer = Long.MAX_VALUE;
        public Periodo proximoPeriodo;
        public LocalDate ultimoPagamento;
        public Situacao situacao = Situacao.EM_DIA;

        public BigDecimal total(boolean comMulta) {
            return comMulta ? divida.add(multa) : divida;
        }

        public Periodo desde() {
            return emAtraso.isEmpty() ? null : emAtraso.get(0);
        }
    }

    private Devedores() {
    }

    /** Carrega a situação de todos os contratos na data de referência (pagamentos registados até essa data). */
    public static List<Linha> carregar(LocalDate dataRef, int diasAlerta) throws SQLException {
        Mensalidades.garantirEstrutura();
        List<Linha> linhas = new ArrayList<>();
        try (Connection cn = BDConexao.novaLigacao()) {
            Map<String, Map<Periodo, Integer>> pagos = new HashMap<>();
            Map<String, LocalDate> ultimos = new HashMap<>();
            lerPagamentos(cn, dataRef, pagos, ultimos);
            Map<Integer, List<RegrasMulta.Faixa>> faixas = RegrasMulta.todas(cn);
            String base = ConfigMensalidade.lerEmCache().multaBase;
            Map<Integer, BigDecimal> precos = lerPrecos(cn);
            Map<Integer, EstadoCliente.Registo> estados = EstadoCliente.todos(cn);

            String sql = "SELECT c.*, cl.nome, cl.telefone, cl.nif, p.designacao"
                    + " FROM configuracao_mes_comeco c"
                    + " JOIN tb_cliente cl ON cl.codigo = c.cliente_id"
                    + " LEFT JOIN tb_produto p ON p.codigo = c.produto_id";
            try (PreparedStatement ps = cn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ConfiguracaoMesComeco c = new ConfiguracaoMesComeco();
                    c.setId(rs.getInt("id"));
                    c.setMesId(rs.getInt("mes_id"));
                    c.setProdutoId(rs.getInt("produto_id"));
                    c.setClienteId(rs.getInt("cliente_id"));
                    c.setDuracao(rs.getInt("duracao"));
                    c.setAnoInicio(rs.getInt("ano_inicio"));
                    c.setDiaPagamento(rs.getInt("dia_pagamento"));
                    Timestamp dc = rs.getTimestamp("data_cadastro");
                    c.setDataCadastro(dc);
                    if (c.getAnoInicio() <= 0 && dc != null) {
                        c.setAnoInicio(Periodo.de(dc).getAno());
                    }

                    Linha l = new Linha();
                    l.contrato = c;
                    l.clienteId = c.getClienteId();
                    l.produtoId = c.getProdutoId();
                    l.cliente = nvl(rs.getString("nome"));
                    l.telefone = nvl(rs.getString("telefone"));
                    l.nif = nvl(rs.getString("nif"));
                    l.servico = rs.getString("designacao") == null ? "Serviço " + c.getProdutoId() : rs.getString("designacao");
                    l.preco = precos.getOrDefault(c.getProdutoId(), BigDecimal.ZERO);
                    String chave = chave(l.clienteId, l.produtoId);
                    l.ultimoPagamento = ultimos.get(chave);
                    l.estadoCliente = estados.getOrDefault(l.clienteId, EstadoCliente.ACTIVO_SEM_REGISTO);
                    calcular(l, pagos.getOrDefault(chave, new HashMap<>()), dataRef, diasAlerta,
                            faixas.get(l.produtoId), base);
                    linhas.add(l);
                }
            }
        }
        return linhas;
    }

    static void calcular(Linha l, Map<Periodo, Integer> pagos, LocalDate ref, int diasAlerta,
            List<RegrasMulta.Faixa> faixas, String base) {
        boolean algumPorPagar = false;
        for (Mensalidades.Mes m : Mensalidades.meses(l.contrato, pagos, ref, l.estadoCliente.limiteDivida())) {
            if (m.pago()) {
                continue;
            }
            algumPorPagar = true;
            if (m.estado == Mensalidades.Estado.ATRASO) {
                long dias = ChronoUnit.DAYS.between(m.vencimento, ref);
                l.emAtraso.add(m.periodo);
                l.diasPorMes.add(dias);
                l.divida = l.divida.add(l.preco);
                BigDecimal mm = RegrasMulta.calcular(faixas, l.preco, m.periodo, m.vencimento, ref, base).valor;
                l.multa = l.multa.add(mm);
                l.multaPorMes.add(mm);
                l.vencimentoPorMes.add(m.vencimento);
            } else if (l.proximoVencimento == null) {
                l.proximoVencimento = m.vencimento;
                l.proximoPeriodo = m.periodo;
                l.diasParaVencer = ChronoUnit.DAYS.between(ref, m.vencimento);
            }
        }
        if (!l.emAtraso.isEmpty()) {
            l.diasAtraso = l.diasPorMes.get(0);
            l.situacao = Situacao.ATRASO;
        } else if (l.inactivo()) {
            l.situacao = Situacao.INACTIVO;
            l.proximoVencimento = null;
            l.proximoPeriodo = null;
            l.diasParaVencer = Long.MAX_VALUE;
        } else if (!algumPorPagar) {
            l.situacao = Situacao.CONCLUIDO;
        } else if (l.diasParaVencer == 0) {
            l.situacao = Situacao.VENCE_HOJE;
            l.divida = l.preco; // o que vence hoje já é para cobrar
        } else if (l.diasParaVencer <= diasAlerta) {
            l.situacao = Situacao.A_VENCER;
        } else {
            l.situacao = Situacao.EM_DIA;
        }
    }

    // =====================================================================
    // LEITURAS EM BLOCO
    // =====================================================================
    private static void lerPagamentos(Connection cn, LocalDate ref, Map<String, Map<Periodo, Integer>> pagos,
            Map<String, LocalDate> ultimos) throws SQLException {
        String sql = "SELECT cliente_id, produto_id, mes_id, ano, venda_id, data_cadastro FROM pagamento_mensalidade"
                + " WHERE data_cadastro < ?";
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(ref.plusDays(1).atStartOfDay()));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int mes = rs.getInt("mes_id");
                    if (mes < 1 || mes > 12) {
                        continue;
                    }
                    Timestamp t = rs.getTimestamp("data_cadastro");
                    int ano = rs.getInt("ano");
                    if (ano <= 0) {
                        ano = t != null ? Periodo.de(t).getAno() : ref.getYear();
                    }
                    String k = chave(rs.getInt("cliente_id"), rs.getInt("produto_id"));
                    pagos.computeIfAbsent(k, x -> new HashMap<>()).put(new Periodo(ano, mes), rs.getInt("venda_id"));
                    if (t != null) {
                        LocalDate d = t.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                        LocalDate u = ultimos.get(k);
                        if (u == null || d.isAfter(u)) {
                            ultimos.put(k, d);
                        }
                    }
                }
            }
        }
    }

    /** Último preço (qtd 1) de cada serviço com contrato. */
    private static Map<Integer, BigDecimal> lerPrecos(Connection cn) throws SQLException {
        Map<Integer, BigDecimal> precos = new HashMap<>();
        String sql = "SELECT pr.fk_produto, pr.preco_venda FROM tb_preco pr"
                + " JOIN (SELECT fk_produto, MAX(pk_preco) AS ultimo FROM tb_preco"
                + "       WHERE 1 BETWEEN qtd_baixo AND qtd_alto AND fk_produto IN (SELECT DISTINCT produto_id FROM configuracao_mes_comeco)"
                + "       GROUP BY fk_produto) u ON u.ultimo = pr.pk_preco";
        try (PreparedStatement ps = cn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                BigDecimal v = rs.getBigDecimal("preco_venda");
                precos.put(rs.getInt("fk_produto"), v == null ? BigDecimal.ZERO : v);
            }
        }
        return precos;
    }

    private static String chave(int cliente, int produto) {
        return cliente + ":" + produto;
    }

    private static String nvl(String s) {
        return s == null ? "" : s;
    }
}
