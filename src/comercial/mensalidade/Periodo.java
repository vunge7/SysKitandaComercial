package comercial.mensalidade;

import java.text.Normalizer;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

/**
 * Um mês de um ano (ex.: Março/2026). As mensalidades passam a ser contadas
 * por período e não só pelo mês: antes, o Março pago em 2025 ficava "pago"
 * para sempre e os contratos que passavam de Dezembro baralhavam-se.
 *
 * Na linha da venda o período vai no fim da designação, depois do '#':
 * "Pgt. Ref. de Propina de #Março/2026". As linhas antigas, só com o mês
 * ("#Março"), continuam a ser lidas (ano corrente).
 */
public final class Periodo implements Comparable<Periodo> {

    public static final String[] MESES = {
        "Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho",
        "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"
    };
    public static final String[] MESES_CURTOS = {
        "Jan", "Fev", "Mar", "Abr", "Mai", "Jun", "Jul", "Ago", "Set", "Out", "Nov", "Dez"
    };

    private final int ano;
    private final int mes; // 1..12

    public Periodo(int ano, int mes) {
        if (mes < 1 || mes > 12) {
            throw new IllegalArgumentException("Mês inválido: " + mes);
        }
        this.ano = ano;
        this.mes = mes;
    }

    public static Periodo actual() {
        return de(new Date());
    }

    public static Periodo de(Date data) {
        Calendar c = Calendar.getInstance();
        c.setTime(data);
        return new Periodo(c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1);
    }

    public int getAno() {
        return ano;
    }

    public int getMes() {
        return mes;
    }

    /** Número sequencial (para comparar e somar meses). */
    public int indice() {
        return ano * 12 + (mes - 1);
    }

    public Periodo mais(int meses) {
        int i = indice() + meses;
        return new Periodo(Math.floorDiv(i, 12), Math.floorMod(i, 12) + 1);
    }

    public String nomeMes() {
        return MESES[mes - 1];
    }

    /** "Março/2026": é o texto que vai para a designação do item da venda. */
    public String texto() {
        return nomeMes() + "/" + ano;
    }

    /** "Mar 26" — para cartões pequenos. */
    public String curto() {
        return MESES_CURTOS[mes - 1] + " " + String.format("%02d", ano % 100);
    }

    @Override
    public String toString() {
        return texto();
    }

    // ------------------------------------------------------------------
    // LEITURA
    // ------------------------------------------------------------------
    /** 1..12 a partir do nome (com ou sem acentos); -1 se não for um mês. */
    public static int mesDoNome(String nome) {
        String n = normalizar(nome);
        for (int i = 0; i < MESES.length; i++) {
            if (normalizar(MESES[i]).equals(n)) {
                return i + 1;
            }
        }
        return -1;
    }

    /**
     * Lê "Março/2026", "Março 2026" ou só "Março" (ano por omissão).
     * Devolve null se o texto não for um período.
     */
    public static Periodo ler(String texto, int anoPorOmissao) {
        if (texto == null) {
            return null;
        }
        String t = texto.trim();
        String parteMes = t;
        int ano = anoPorOmissao;
        int sep = Math.max(t.lastIndexOf('/'), t.lastIndexOf(' '));
        if (sep > 0) {
            String fim = t.substring(sep + 1).trim();
            if (fim.matches("\\d{4}")) {
                ano = Integer.parseInt(fim);
                parteMes = t.substring(0, sep).trim();
            }
        }
        int m = mesDoNome(parteMes);
        return m < 0 ? null : new Periodo(ano, m);
    }

    /** O período de uma designação de item ("... #Março/2026"); null se não for mensalidade. */
    public static Periodo daDesignacao(String designacao) {
        if (designacao == null) {
            return null;
        }
        int i = designacao.lastIndexOf('#');
        if (i < 0) {
            return null;
        }
        return ler(designacao.substring(i + 1), actual().getAno());
    }

    /** O que está antes do '#' (para agrupar as linhas do mesmo serviço). */
    public static String servicoDaDesignacao(String designacao) {
        if (designacao == null) {
            return "";
        }
        int i = designacao.lastIndexOf('#');
        return normalizar(i < 0 ? designacao : designacao.substring(0, i));
    }

    static String normalizar(String s) {
        if (s == null) {
            return "";
        }
        return Normalizer.normalize(s, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .replaceAll("\\s+", " ")
                .trim()
                .toLowerCase();
    }

    @Override
    public int compareTo(Periodo o) {
        return Integer.compare(indice(), o.indice());
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Periodo && ((Periodo) o).indice() == indice();
    }

    @Override
    public int hashCode() {
        return Objects.hash(ano, mes);
    }
}
