package comercial.whatsapp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Envio automático dos relatórios por WhatsApp SEM o sistema aberto.
 *
 * O agendador normal só corre enquanto alguém tem o menu principal ou a
 * Recepção abertos. Para "enviar sempre", este programa corre no servidor
 * (ou num computador sempre ligado), sem janelas, arrancado pelo Agendador de
 * Tarefas do Windows ao ligar o computador — ver whatsapp_servico.bat.
 *
 * Usa as mesmas tabelas, horários, contactos e configuração (tb_whatsapp_*);
 * a reserva de cada horário na base de dados impede envios repetidos se o
 * sistema também estiver aberto noutro computador.
 *
 * Tem de correr na pasta do programa (lê credencial/file.txt e relatorios/).
 * Registo em anexos/whatsapp/servico.log.
 */
public final class ServicoWhatsApp {

    /** Porta local usada só para garantir uma única cópia por computador. */
    private static final int PORTA_UNICA = 47911;

    private ServicoWhatsApp() {
    }

    public static void main(String[] args) throws Exception {
        System.setProperty("java.awt.headless", "true");
        File pasta = new File("anexos" + File.separator + "whatsapp");
        pasta.mkdirs();
        PrintStream log = new PrintStream(new FileOutputStream(new File(pasta, "servico.log"), true), true, "UTF-8");
        System.setOut(new CarimboHora(log));
        System.setErr(new CarimboHora(log));

        ServerSocket unico;
        try {
            unico = new ServerSocket(PORTA_UNICA, 1, InetAddress.getLoopbackAddress());
        } catch (Exception e) {
            System.out.println("[WhatsApp] o serviço já está a correr neste computador; esta cópia termina.");
            return;
        }
        System.out.println("[WhatsApp] serviço iniciado em " + new File("").getAbsolutePath()
                + " (Java " + System.getProperty("java.version") + ")");
        if (!new File("credencial" + File.separator + "file.txt").exists()) {
            System.out.println("[WhatsApp] ATENÇÃO: credencial\\file.txt não existe nesta pasta — o serviço tem de"
                    + " arrancar na pasta do programa (ver whatsapp_servico.bat).");
        }
        WhatsAppAgendador.marcarComoServico();
        WhatsAppAgendador.iniciar();
        // o mesmo serviço envia também os relatórios por e-mail
        comercial.email.EmailAgendador.marcarComoServico();
        comercial.email.EmailAgendador.iniciar();
        comercial.caixa.FechoGeralAgendador.marcarComoServico();
        comercial.caixa.FechoGeralAgendador.iniciar(); // o fecho geral diário, se nenhum computador o gerar
        Runtime.getRuntime().addShutdownHook(new Thread(() -> System.out.println("[WhatsApp] serviço terminado")));
        // as threads do agendador são "daemon": o programa fica vivo aqui
        synchronized (ServicoWhatsApp.class) {
            while (!unico.isClosed()) {
                ServicoWhatsApp.class.wait(60000);
            }
        }
    }

    /** Cada linha do log começa pela data e hora. */
    private static final class CarimboHora extends PrintStream {

        private final SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        CarimboHora(PrintStream destino) {
            super(destino, true);
        }

        @Override
        public void println(String x) {
            super.println(f.format(new Date()) + "  " + x);
        }

        @Override
        public void println(Object x) {
            println(String.valueOf(x));
        }
    }
}
