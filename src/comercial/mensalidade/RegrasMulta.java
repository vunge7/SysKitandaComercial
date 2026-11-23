package comercial.mensalidade;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Regras das multas das mensalidades (tabela {@code multa_servico}): por
 * serviço, faixas de dias de atraso com uma multa em <b>percentagem</b> da
 * mensalidade ou um <b>valor fixo</b> em Kz.
 * <p>
 * Os dias contam-se, conforme a configuração ({@code mensalidade_config.multa_base}):
 * <ul>
 * <li>MES — desde o dia 1 do mês da mensalidade (o dia 1 conta como 1);</li>
 * <li>VENCIMENTO — desde o dia de pagamento do contrato (o dia seguinte conta como 1).</li>
 * </ul>
 * Passada a última faixa aplica-se a faixa mais alta. Meses pagos adiantados não têm multa.
 */
public final class RegrasMulta {

    public static final String PERCENTAGEM = "PERCENTAGEM", VALOR = "VALOR";
    public static final String BASE_MES = "MES", BASE_VENCIMENTO = "VENCIMENTO";

    /** Uma faixa de dias. */
    public static final class Faixa {

        public int id, de, ate;
        public String tipo = PERCENTAGEM;
        public BigDecimal valor = BigDecimal.ZERO;

        public boolean percentagem() {
            return !VALOR.equalsIgnoreCase(tipo);
        }

        public String texto() {
            return percentagem() ? valor.stripTrailingZeros().toPlainString() + "%"
                    : kitanda.util.CfMethods.formatarComoMoeda(valor.doubleValue());
        }
    }

    /** O resultado do cálculo para um mês. */
    public static final class Calculo {

        public static final Calculo ZERO = new Calculo(0, null, BigDecimal.ZERO);
        public final long dias;
        public final Faixa faixa;
        public final BigDecimal valor;

        Calculo(long dias, Faixa faixa, BigDecimal valor) {
            this.dias = dias;
            this.faixa = faixa;
            this.valor = valor;
        }

        /** "25 dias de atraso · 10%" */
        public String descricao() {
            if (faixa == null) {
                return "";
            }
            return dias + (dias == 1 ? " dia" : " dias") + " · " + faixa.texto();
        }
    }

    private RegrasMulta() {
    }

    /** As faixas de todos os serviços (relatórios: uma consulta só). */
    public static Map<Integer, List<Faixa>> todas(Connection cn) {
        Map<Integer, List<Faixa>> m = new HashMap<>();
        for (Object[] r : ler(cn, null)) {
            m.computeIfAbsent((Integer) r[0], k -> new ArrayList<>()).add((Faixa) r[1]);
        }
        return m;
    }

    public static List<Faixa> doServico(Connection cn, int produtoId) {
        List<Faixa> l = new ArrayList<>();
        for (Object[] r : ler(cn, produtoId)) {
            l.add((Faixa) r[1]);
        }
        return l;
    }

    private static List<Object[]> ler(Connection cn, Integer produtoId) {
        List<Object[]> r = new ArrayList<>();
        boolean comTipo;
        try {
            comTipo = Mensalidades.existeColuna(cn, "multa_servico", "tipo");
        } catch (SQLException e) {
            comTipo = false;
        }
        String sql = "SELECT id, produto_id, day_start, day_end, valor" + (comTipo ? ", tipo" : "")
                + " FROM multa_servico" + (produtoId == null ? "" : " WHERE produto_id = ?") + " ORDER BY day_start";
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            if (produtoId != null) {
                ps.setInt(1, produtoId);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Faixa f = new Faixa();
                    f.id = rs.getInt("id");
                    f.de = rs.getInt("day_start");
                    f.ate = rs.getInt("day_end");
                    f.valor = rs.getBigDecimal("valor") == null ? BigDecimal.ZERO : rs.getBigDecimal("valor");
                    f.tipo = comTipo && rs.getString("tipo") != null ? rs.getString("tipo") : PERCENTAGEM;
                    r.add(new Object[]{rs.getInt("produto_id"), f});
                }
            }
        } catch (SQLException e) {
            System.err.println("[Multas] " + e.getMessage());
        }
        return r;
    }

    /**
     * A multa de um mês.
     *
     * @param preco       mensalidade (a base da percentagem)
     * @param vencimento  dia em que o mês venceu (base VENCIMENTO)
     * @param pagamento   data do pagamento
     */
    public static Calculo calcular(List<Faixa> faixas, BigDecimal preco, Periodo p, LocalDate vencimento,
            LocalDate pagamento, String base) {
        if (faixas == null || faixas.isEmpty() || pagamento == null || p == null) {
            return Calculo.ZERO;
        }
        if (p.compareTo(new Periodo(pagamento.getYear(), pagamento.getMonthValue())) > 0) {
            return Calculo.ZERO; // pago adiantado
        }
        long dias = BASE_VENCIMENTO.equalsIgnoreCase(base) && vencimento != null
                ? ChronoUnit.DAYS.between(vencimento, pagamento)
                : ChronoUnit.DAYS.between(LocalDate.of(p.getAno(), p.getMes(), 1), pagamento) + 1;
        if (dias <= 0) {
            return Calculo.ZERO;
        }
        Faixa escolhida = null, maisAlta = null;
        for (Faixa f : faixas) {
            if (escolhida == null && dias >= f.de && dias <= f.ate) {
                escolhida = f;
            }
            if (maisAlta == null || f.ate > maisAlta.ate) {
                maisAlta = f;
            }
        }
        if (escolhida == null && maisAlta != null && dias > maisAlta.ate) {
            escolhida = maisAlta;
        }
        if (escolhida == null || escolhida.valor.signum() <= 0) {
            return new Calculo(dias, null, BigDecimal.ZERO);
        }
        BigDecimal valor = escolhida.percentagem()
                ? (preco == null ? BigDecimal.ZERO : preco).multiply(escolhida.valor).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP)
                : escolhida.valor.setScale(2, RoundingMode.HALF_UP);
        return new Calculo(dias, escolhida, valor);
    }
}
