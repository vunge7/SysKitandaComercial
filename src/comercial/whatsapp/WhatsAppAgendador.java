package comercial.whatsapp;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import util.BDConexao;

/**
 * Envio automático dos relatórios por WhatsApp nas horas agendadas.
 *
 * Corre numa thread própria, com uma ligação à base de dados só sua (os
 * relatórios e o envio podem demorar e não podem bloquear nem misturar-se
 * com as transacções dos formulários). Verifica de minuto a minuto.
 *
 * Vários computadores com o sistema aberto não repetem o envio: cada horário
 * é "reservado" na base de dados antes de enviar (WhatsAppDados.reservarEnvio).
 * Se nenhum computador estiver ligado à hora, o envio sai quando um abrir,
 * até à tolerância configurada (por omissão 2 horas); depois disso salta.
 */
public final class WhatsAppAgendador {

    private static ScheduledExecutorService executor;

    private WhatsAppAgendador() {
    }

    /** Liga o agendador (menu principal e Recepção). Chamadas repetidas não fazem nada. */
    public static synchronized void iniciar() {
        if (executor != null) {
            return;
        }
        executor = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "WhatsApp-Agendador");
            t.setDaemon(true);
            t.setPriority(Thread.MIN_PRIORITY);
            return t;
        });
        long atraso = 60 - (System.currentTimeMillis() / 1000) % 60 + 10; // segundo 10 de cada minuto
        executor.scheduleWithFixedDelay(WhatsAppAgendador::verificar, atraso, 60, TimeUnit.SECONDS);
        log("agendador iniciado");
    }

    /** Minutos entre novas tentativas de um horário com envios falhados. */
    static final int MINUTOS_ENTRE_TENTATIVAS = 5;
    /** Próxima tentativa permitida por horário (neste computador). */
    private static final Map<Integer, Long> proximaTentativa = new java.util.concurrent.ConcurrentHashMap<>();
    /** Quem está a verificar: "SERVIDOR (serviço)" ou "RECEPCAO-1 (sistema aberto)". */
    private static volatile String origem = computador() + " (sistema aberto)";

    /** Modo serviço (ServicoWhatsApp): só muda o nome que aparece no sinal de vida. */
    static void marcarComoServico() {
        origem = computador() + " (serviço)";
    }

    private static String computador() {
        try {
            return java.net.InetAddress.getLocalHost().getHostName();
        } catch (Exception e) {
            String n = System.getenv("COMPUTERNAME");
            return n == null ? "computador" : n;
        }
    }

    static void verificar() {
        try (Connection cn = BDConexao.novaLigacao()) {
            WhatsAppDados.garantirTabelas(cn);
            WhatsAppDados.registarSinal(cn, origem);
            WhatsAppEnvio envio = WhatsAppEnvio.daBase(cn);
            if (!envio.automaticoLigado()) {
                return;
            }
            List<WhatsAppDados.Agendamento> emFalta = WhatsAppDados.agendamentosEmFalta(cn, envio.toleranciaMin());
            Map<RelatorioWhatsApp, RelatorioWhatsApp.Resultado> gerados = new EnumMap<>(RelatorioWhatsApp.class);
            long agora = System.currentTimeMillis();
            for (WhatsAppDados.Agendamento a : emFalta) {
                Long proxima = proximaTentativa.get(a.id);
                if (proxima != null && agora < proxima) {
                    continue; // falhou há pouco: espera pela próxima tentativa
                }
                if (!WhatsAppDados.reservarEnvio(cn, a.id)) {
                    continue; // outro computador já tratou
                }
                // nova tentativa: só o que ainda não chegou a este contacto
                java.util.Set<String> entregues = WhatsAppDados.entreguesHoje(cn, a.numero, a.hora);
                List<RelatorioWhatsApp> faltam = new ArrayList<>();
                for (RelatorioWhatsApp r : a.relatorios) {
                    if (!entregues.contains(r.titulo)) {
                        faltam.add(r);
                    }
                }
                if (faltam.isEmpty()) {
                    proximaTentativa.remove(a.id);
                    continue;
                }
                WhatsAppDados.Contacto c = new WhatsAppDados.Contacto();
                c.id = a.contacto;
                c.nome = a.nome;
                c.numero = a.numero;
                log("envio das " + a.hora + " para " + a.nome + ": " + RelatorioWhatsApp.titulos(faltam)
                        + (proxima != null ? " (nova tentativa)" : ""));
                List<WhatsAppDados.Contacto> um = new ArrayList<>();
                um.add(c);
                int ok = enviar(cn, envio, um, faltam, WhatsAppDados.MODO_AUTOMATICO, null, gerados, null);
                if (ok < faltam.size()) {
                    // sem internet, fornecedor em baixo...: volta a tentar, dentro da tolerância
                    WhatsAppDados.libertarReserva(cn, a.id);
                    proximaTentativa.put(a.id, System.currentTimeMillis() + MINUTOS_ENTRE_TENTATIVAS * 60000L);
                    log("envio das " + a.hora + " para " + a.nome + ": " + (faltam.size() - ok)
                            + " falhado(s); nova tentativa dentro de " + MINUTOS_ENTRE_TENTATIVAS + " min");
                } else {
                    proximaTentativa.remove(a.id);
                }
            }
        } catch (Throwable e) {
            log("falha: " + e.getMessage());
        }
    }

    /**
     * Gera cada relatório uma vez e envia-o a cada contacto, registando tudo
     * no histórico. Usado pelo agendador e pelo envio manual.
     *
     * @param gerados relatórios já gerados nesta ronda (reaproveitados); pode ser null
     * @param progresso recebe uma linha por passo; pode ser null
     * @return número de envios com sucesso
     */
    public static int enviar(Connection cn, WhatsAppEnvio envio, List<WhatsAppDados.Contacto> contactos,
            List<RelatorioWhatsApp> relatorios, String modo, Integer usuario,
            Map<RelatorioWhatsApp, RelatorioWhatsApp.Resultado> gerados, Consumer<String> progresso) {
        if (gerados == null) {
            gerados = new EnumMap<>(RelatorioWhatsApp.class);
        }
        int ok = 0;
        for (RelatorioWhatsApp r : relatorios) {
            RelatorioWhatsApp.Resultado res = gerados.get(r);
            if (res == null) {
                avisar(progresso, "A gerar \"" + r.titulo + "\"…");
                try {
                    res = r.gerar(cn);
                    gerados.put(r, res);
                } catch (Throwable e) {
                    e.printStackTrace();
                    String erro = "não foi possível gerar o relatório: " + e;
                    for (WhatsAppDados.Contacto c : contactos) {
                        WhatsAppDados.registarEnvio(cn, c.id == 0 ? null : c.id, c.nome, c.numero, r.titulo, modo,
                                WhatsAppDados.ESTADO_FALHOU, erro, usuario);
                    }
                    avisar(progresso, "✗ " + r.titulo + ": " + erro);
                    continue;
                }
            }
            for (WhatsAppDados.Contacto c : contactos) {
                avisar(progresso, "A enviar \"" + r.titulo + "\" para " + c.nome + "…");
                try {
                    String detalhe = envio.enviar(c.numero, res);
                    WhatsAppDados.registarEnvio(cn, c.id == 0 ? null : c.id, c.nome, c.numero, r.titulo, modo,
                            envio.simulacao() ? WhatsAppDados.ESTADO_SIMULADO : WhatsAppDados.ESTADO_ENVIADO, detalhe, usuario);
                    ok++;
                    avisar(progresso, "✓ " + r.titulo + " → " + c.nome + (envio.simulacao() ? " (simulação)" : ""));
                } catch (Throwable e) {
                    String erro = e.getMessage() == null ? e.toString() : e.getMessage();
                    WhatsAppDados.registarEnvio(cn, c.id == 0 ? null : c.id, c.nome, c.numero, r.titulo, modo,
                            WhatsAppDados.ESTADO_FALHOU, erro, usuario);
                    avisar(progresso, "✗ " + r.titulo + " → " + c.nome + ": " + erro);
                    log("falhou " + r.titulo + " para " + c.nome + ": " + erro);
                }
            }
        }
        return ok;
    }

    private static void avisar(Consumer<String> progresso, String s) {
        if (progresso != null) {
            progresso.accept(s);
        }
    }

    private static void log(String m) {
        System.out.println("[WhatsApp] " + m);
    }
}
