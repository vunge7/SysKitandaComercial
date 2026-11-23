package comercial.email;

import comercial.whatsapp.RelatorioWhatsApp;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import util.BDConexao;

/**
 * Envio automático dos relatórios por e-mail nas horas agendadas — a mesma
 * lógica do comercial.whatsapp.WhatsAppAgendador: thread própria e ligação própria,
 * verificação de minuto a minuto, reserva de cada horário na base de dados (sem
 * envios repetidos entre computadores), novas tentativas de 5 em 5 minutos dentro
 * da tolerância só com o que faltou, e "sinal de vida" no formulário.
 *
 * Diferença: cada contacto recebe UM e-mail por horário com todos os relatórios
 * (limite diário do Gmail gratuito).
 */
public final class EmailAgendador {

    static final int MINUTOS_ENTRE_TENTATIVAS = 5;
    private static ScheduledExecutorService executor;
    private static final Map<Integer, Long> proximaTentativa = new ConcurrentHashMap<>();
    private static volatile String origem = computador() + " (sistema aberto)";

    private EmailAgendador() {
    }

    /** Liga o agendador (menus e serviço). Chamadas repetidas não fazem nada. */
    public static synchronized void iniciar() {
        if (executor != null) {
            return;
        }
        executor = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "Email-Agendador");
            t.setDaemon(true);
            t.setPriority(Thread.MIN_PRIORITY);
            return t;
        });
        long atraso = 60 - (System.currentTimeMillis() / 1000) % 60 + 25; // segundo 25 de cada minuto (o WhatsApp usa o 10)
        executor.scheduleWithFixedDelay(EmailAgendador::verificar, atraso, 60, TimeUnit.SECONDS);
        log("agendador iniciado");
    }

    /** Modo serviço (comercial.whatsapp.ServicoWhatsApp): só muda o nome no sinal de vida. */
    public static void marcarComoServico() {
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
            EmailDados.garantirTabelas(cn);
            EmailDados.registarSinal(cn, origem);
            EmailEnvio envio = EmailEnvio.daBase(cn);
            if (!envio.automaticoLigado()) {
                return;
            }
            List<EmailDados.Agendamento> emFalta = EmailDados.agendamentosEmFalta(cn, envio.toleranciaMin());
            Map<RelatorioWhatsApp, RelatorioWhatsApp.Resultado> gerados = new EnumMap<>(RelatorioWhatsApp.class);
            long agora = System.currentTimeMillis();
            for (EmailDados.Agendamento a : emFalta) {
                Long proxima = proximaTentativa.get(a.id);
                if (proxima != null && agora < proxima) {
                    continue;
                }
                if (!EmailDados.reservarEnvio(cn, a.id)) {
                    continue; // outro computador já tratou
                }
                Set<String> entregues = EmailDados.entreguesHoje(cn, a.email, a.hora);
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
                EmailDados.Contacto c = new EmailDados.Contacto();
                c.id = a.contacto;
                c.nome = a.nome;
                c.email = a.email;
                List<EmailDados.Contacto> um = new ArrayList<>();
                um.add(c);
                log("envio das " + a.hora + " para " + a.nome + ": " + RelatorioWhatsApp.titulos(faltam)
                        + (proxima != null ? " (nova tentativa)" : ""));
                int ok = enviar(cn, envio, um, faltam, EmailDados.MODO_AUTOMATICO, null, gerados, null);
                if (ok < 1) {
                    EmailDados.libertarReserva(cn, a.id);
                    proximaTentativa.put(a.id, System.currentTimeMillis() + MINUTOS_ENTRE_TENTATIVAS * 60000L);
                    log("envio das " + a.hora + " para " + a.nome + " falhou; nova tentativa dentro de "
                            + MINUTOS_ENTRE_TENTATIVAS + " min");
                } else {
                    proximaTentativa.remove(a.id);
                }
            }
        } catch (Throwable e) {
            log("falha: " + e.getMessage());
        }
    }

    /**
     * Gera cada relatório uma vez e envia UM e-mail por contacto com todos eles.
     * Regista no histórico uma linha por relatório (para as novas tentativas).
     *
     * @return número de contactos a quem o e-mail foi entregue ao servidor
     */
    public static int enviar(Connection cn, EmailEnvio envio, List<EmailDados.Contacto> contactos,
            List<RelatorioWhatsApp> relatorios, String modo, Integer usuario,
            Map<RelatorioWhatsApp, RelatorioWhatsApp.Resultado> gerados, Consumer<String> progresso) {
        if (gerados == null) {
            gerados = new EnumMap<>(RelatorioWhatsApp.class);
        }
        List<RelatorioWhatsApp.Resultado> prontos = new ArrayList<>();
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
                    for (EmailDados.Contacto c : contactos) {
                        EmailDados.registarEnvio(cn, c.id == 0 ? null : c.id, c.nome, c.email, r.titulo, modo,
                                EmailDados.ESTADO_FALHOU, erro, usuario);
                    }
                    avisar(progresso, "✗ " + r.titulo + ": " + erro);
                    continue;
                }
            }
            prontos.add(res);
        }
        if (prontos.isEmpty()) {
            return 0;
        }
        int ok = 0;
        for (EmailDados.Contacto c : contactos) {
            avisar(progresso, "A enviar e-mail para " + c.nome + " (" + c.email + ")…");
            try {
                String detalhe = envio.enviar(cn, c.email, prontos);
                for (RelatorioWhatsApp.Resultado r : prontos) {
                    EmailDados.registarEnvio(cn, c.id == 0 ? null : c.id, c.nome, c.email, r.titulo, modo,
                            EmailDados.ESTADO_ENVIADO, detalhe, usuario);
                }
                ok++;
                avisar(progresso, "✓ " + c.nome + ": " + prontos.size() + " relatório(s) num e-mail");
            } catch (Throwable e) {
                String erro = e.getMessage() == null ? e.toString() : e.getMessage();
                for (RelatorioWhatsApp.Resultado r : prontos) {
                    EmailDados.registarEnvio(cn, c.id == 0 ? null : c.id, c.nome, c.email, r.titulo, modo,
                            EmailDados.ESTADO_FALHOU, erro, usuario);
                }
                avisar(progresso, "✗ " + c.nome + ": " + erro);
                log("falhou o e-mail para " + c.nome + ": " + erro);
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
        System.out.println("[E-mail] " + m);
    }
}
