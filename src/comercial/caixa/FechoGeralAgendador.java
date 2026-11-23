package comercial.caixa;

import java.awt.GraphicsEnvironment;
import java.io.File;
import java.sql.Connection;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import util.BDConexao;

/**
 * (Modo SAFT: sem acerto de stock de documentos inválidos na AGT.)
 *
 *
 * Gera o fecho geral automaticamente todos os dias à hora configurada (08:00 por
 * omissão), uma só vez (reserva em tb_fecho_geral): imprime-o no computador que o
 * gerou e envia-o por e-mail e WhatsApp.
 *
 * Corre nos menus (Principal, Recepção, Restaurante, Bar, Hospedaria...) e no
 * serviço do WhatsApp. O serviço não tem impressora à vista: só o gera se 30
 * minutos depois da hora ainda nenhum computador com o sistema aberto o fez.
 * Se o sistema só for aberto mais tarde (ex.: às 10:00), é gerado nessa altura.
 */
public final class FechoGeralAgendador {

    private static final int TOLERANCIA_SERVICO_MIN = 30;
    private static ScheduledExecutorService executor;
    private static volatile boolean servico;
    private static volatile long proximaTentativa;
    /** Data (00:00) do último fecho geral já tratado neste computador. */
    private static volatile long ultimaReferencia;

    private FechoGeralAgendador() {
    }

    public static synchronized void iniciar() {
        if (executor != null) {
            return;
        }
        executor = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "FechoGeral-Agendador");
            t.setDaemon(true);
            t.setPriority(Thread.MIN_PRIORITY);
            return t;
        });
        long atraso = 60 - (System.currentTimeMillis() / 1000) % 60 + 40; // segundo 40 de cada minuto
        executor.scheduleWithFixedDelay(FechoGeralAgendador::verificar, atraso, 60, TimeUnit.SECONDS);
        log("agendador iniciado");
    }

    public static void marcarComoServico() {
        servico = true;
    }

    static void verificar() {
        if (System.currentTimeMillis() < proximaTentativa) {
            return;
        }
        try (Connection cn = BDConexao.novaLigacao()) {
            FechoGeral.Config c = FechoGeral.config(cn);
            if (!c.activo) {
                return;
            }
            Date agora = new Date();
            Date fim = c.fimHoje(); // hora de hoje: a reagendada no aviso, ou hora_fecho_geral
            Date referencia = hoje();
            if (!servico && agora.before(fim) && referencia.getTime() != ultimaReferencia) {
                avisar(agora, fim);
            }
            long espera = servico ? TOLERANCIA_SERVICO_MIN * 60000L : 0;
            if (agora.getTime() < fim.getTime() + espera) {
                return; // ainda não chegou a hora
            }
            if (referencia.getTime() == ultimaReferencia) {
                return; // já tratado hoje neste computador
            }
            Date[] periodo = FechoGeral.periodoHoje(cn, c);
            if (FechoGeral.existe(cn, referencia)) {
                ultimaReferencia = referencia.getTime();
                return; // já foi gerado (ou está a ser gerado noutro computador)
            }
            String origem = computador() + (servico ? " (serviço)" : " (sistema aberto)");
            if (!FechoGeral.reservar(cn, referencia, periodo, FechoGeral.MODO_AUTOMATICO, origem, null)) {
                ultimaReferencia = referencia.getTime();
                return; // já foi gerado (ou está a ser gerado noutro computador)
            }
            // a partir daqui este computador não volta a gerar o de hoje, mesmo que algo falhe a meio
            ultimaReferencia = referencia.getTime();
            log("a gerar o fecho geral de " + referencia);
            FechoGeral.Resultado g;
            File pdf;
            JasperPrint jp;
            try {
                g = FechoGeral.calcular(cn, periodo[0], periodo[1]);
                jp = FechoGeral.gerar(cn, g);
                pdf = FechoGeral.guardarPdf(jp, g);
            } catch (Exception e) {
                FechoGeral.libertar(cn, referencia);
                ultimaReferencia = 0; // falhou antes de gerar: pode tentar de novo
                proximaTentativa = System.currentTimeMillis() + 5 * 60000L;
                log("falha ao gerar (nova tentativa dentro de 5 min): " + e);
                return;
            }
            StringBuilder detalhe = new StringBuilder("PDF: " + pdf.getName());
            if (c.imprimir && !servico && !GraphicsEnvironment.isHeadless()) {
                try {
                    JasperPrintManager.printReport(jp, false);
                    detalhe.append(" · impresso");
                } catch (Exception e) {
                    detalhe.append(" · impressão falhou: ").append(e.getMessage());
                }
            }
            if (c.enviar) {
                int n = FechoGeral.enviar(cn, g, pdf, null);
                detalhe.append(" · ").append(n).append(" envio(s)");
            }
            FechoGeral.concluir(cn, referencia, g, "Concluído", detalhe.toString());
            log("fecho geral de " + referencia + ": " + detalhe);
        } catch (Throwable e) {
            log("falha: " + e);
        }
    }

    /** Hora (ms) do fecho a que se referem os avisos já mostrados, e o último aviso (minutos). */
    private static volatile long avisoAlvo;
    private static volatile int avisoUltimo = Integer.MAX_VALUE;

    /**
     * "Faltam x minutos para o fecho automático": na última hora, de 15 em 15
     * minutos (60, 45, 30, 15). Se a hora for reagendada, os avisos recomeçam.
     */
    private static void avisar(Date agora, Date fim) {
        if (GraphicsEnvironment.isHeadless()) {
            return;
        }
        int faltam = (int) Math.ceil((fim.getTime() - agora.getTime()) / 60000.0);
        if (faltam > 60 || faltam <= 0) {
            return;
        }
        if (avisoAlvo != fim.getTime()) {
            avisoAlvo = fim.getTime();
            avisoUltimo = Integer.MAX_VALUE;
        }
        int marco = ((faltam + 14) / 15) * 15; // 60, 45, 30 ou 15
        if (marco < avisoUltimo) {
            avisoUltimo = marco;
            AvisoFechoAutomatico.mostrar(fim);
        }
    }

    static Date hoje() {
        Calendar k = Calendar.getInstance();
        k.set(Calendar.HOUR_OF_DAY, 0);
        k.set(Calendar.MINUTE, 0);
        k.set(Calendar.SECOND, 0);
        k.set(Calendar.MILLISECOND, 0);
        return k.getTime();
    }

    private static String computador() {
        try {
            return java.net.InetAddress.getLocalHost().getHostName();
        } catch (Exception e) {
            String n = System.getenv("COMPUTERNAME");
            return n == null ? "computador" : n;
        }
    }

    private static void log(String m) {
        System.out.println("[FechoGeral] " + m);
    }
}
