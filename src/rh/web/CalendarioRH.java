package rh.web;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Calendário do RH: feriados nacionais de Angola e contagem de dias úteis
 * (segunda a sexta, sem feriados), para as férias e as faltas.
 *
 * A lista é a habitual; as pontes e tolerâncias decretadas em cada ano
 * acrescentam-se em "Feriados" (ficam gravadas em rh_feriado e passam a valer
 * em vez desta).
 */
public final class CalendarioRH {

    private CalendarioRH() {
    }

    /** Feriados nacionais de Angola num ano (fixos + Carnaval e Sexta-feira Santa, que dependem da Páscoa). */
    public static Map<LocalDate, String> feriadosNacionais(int ano) {
        Map<LocalDate, String> m = new TreeMap<>();
        m.put(LocalDate.of(ano, 1, 1), "Dia do Ano Novo");
        m.put(LocalDate.of(ano, 2, 4), "Dia do Início da Luta Armada de Libertação Nacional");
        m.put(LocalDate.of(ano, 3, 8), "Dia Internacional da Mulher");
        m.put(LocalDate.of(ano, 3, 23), "Dia da Libertação da África Austral");
        m.put(LocalDate.of(ano, 4, 4), "Dia da Paz e da Reconciliação Nacional");
        m.put(LocalDate.of(ano, 5, 1), "Dia Internacional do Trabalhador");
        m.put(LocalDate.of(ano, 9, 17), "Dia do Fundador da Nação e do Herói Nacional");
        m.put(LocalDate.of(ano, 11, 2), "Dia dos Finados");
        m.put(LocalDate.of(ano, 11, 11), "Dia da Independência Nacional");
        m.put(LocalDate.of(ano, 12, 25), "Dia de Natal e da Família");
        LocalDate pascoa = pascoa(ano);
        m.put(pascoa.minusDays(47), "Carnaval");
        m.put(pascoa.minusDays(2), "Sexta-feira Santa");
        return new LinkedHashMap<>(m);
    }

    /** Domingo de Páscoa (algoritmo de Meeus/Jones/Butcher, calendário gregoriano). */
    static LocalDate pascoa(int ano) {
        int a = ano % 19, b = ano / 100, c = ano % 100, d = b / 4, e = b % 4, f = (b + 8) / 25, g = (b - f + 1) / 3;
        int h = (19 * a + b - d - g + 15) % 30, i = c / 4, k = c % 4, l = (32 + 2 * e + 2 * i - h - k) % 7;
        int m = (a + 11 * h + 22 * l) / 451;
        int mes = (h + l - 7 * m + 114) / 31, dia = ((h + l - 7 * m + 114) % 31) + 1;
        return LocalDate.of(ano, mes, dia);
    }

    public static boolean fimDeSemana(LocalDate d) {
        return d.getDayOfWeek() == DayOfWeek.SATURDAY || d.getDayOfWeek() == DayOfWeek.SUNDAY;
    }

    /** Os dias úteis entre duas datas (inclusive). */
    public static List<LocalDate> diasUteis(LocalDate de, LocalDate ate, Map<LocalDate, String> feriados) {
        List<LocalDate> l = new ArrayList<>();
        if (de == null || ate == null) {
            return l;
        }
        for (LocalDate d = de; !d.isAfter(ate); d = d.plusDays(1)) {
            if (!fimDeSemana(d) && (feriados == null || !feriados.containsKey(d))) {
                l.add(d);
            }
        }
        return l;
    }

    /** A data em que acabam n dias úteis a contar de "inicio" (inclusive). */
    public static LocalDate somarDiasUteis(LocalDate inicio, int n, Map<LocalDate, String> feriados) {
        LocalDate d = inicio;
        int contados = 0;
        while (true) {
            if (!fimDeSemana(d) && (feriados == null || !feriados.containsKey(d))) {
                contados++;
                if (contados >= n) {
                    return d;
                }
            }
            d = d.plusDays(1);
        }
    }
}
