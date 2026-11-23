package comercial.caixa;

import java.awt.Window;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import util.BDConexao;

/**
 * Fechos de caixa e envio de relatórios por e-mail e WhatsApp nos menus do
 * comercial (como na gestão hoteleira): Gestão de Fechos de Caixa, Relatórios
 * por E-mail e Relatórios por WhatsApp. Os dois últimos só abrem para
 * administradores (util.ui.SoAdministrador).
 *
 * Também arranca os agendadores (envios nas horas marcadas e fecho geral do
 * dia), cada um na sua thread e com a sua ligação: não atrasam a interface.
 */
public final class MenuFechos {

    public static final String TITULO = "Fechos e Envios";

    private MenuFechos() {
    }

    /** Menu "Fechos e Envios" no fim da barra (nos menus web vira um grupo da barra lateral). */
    public static JMenu adicionarABarra(JMenuBar barra, Window dono, int usuario) {
        JMenu menu = new JMenu(TITULO);
        menu.add(item("Gestão de Fechos de Caixa", () -> abrirFechos()));
        menu.add(item("Relatórios por E-mail", () -> comercial.email.EnvioRelatoriosEmailVisao.abrir(dono, BDConexao.getInstancia(), usuario)));
        menu.add(item("Relatórios por WhatsApp", () -> comercial.whatsapp.EnvioRelatoriosWhatsAppVisao.abrir(dono, BDConexao.getInstancia(), usuario)));
        if (barra != null) {
            barra.add(menu);
        }
        return menu;
    }

    /** O mesmo, como grupo de uma página de menu web (ex.: o ERP Kitanda do administrador). */
    public static void adicionarAPagina(util.ui.PaginaMenuWeb pagina, Window dono, int usuario) {
        pagina.grupo(TITULO);
        pagina.item(botao("Gestão de Fechos de Caixa", () -> abrirFechos()), "Gestão de Fechos de Caixa");
        pagina.item(botao("Relatórios por E-mail",
                () -> comercial.email.EnvioRelatoriosEmailVisao.abrir(dono, BDConexao.getInstancia(), usuario)), "Relatórios por E-mail");
        pagina.item(botao("Relatórios por WhatsApp",
                () -> comercial.whatsapp.EnvioRelatoriosWhatsAppVisao.abrir(dono, BDConexao.getInstancia(), usuario)), "Relatórios por WhatsApp");
    }

    private static void abrirFechos() {
        new FechosCaixaVisao().setVisible(true);
    }

    private static JMenuItem item(String texto, Runnable accao) {
        JMenuItem i = new JMenuItem(texto);
        i.addActionListener(e -> executar(accao, texto));
        return i;
    }

    private static JButton botao(String texto, Runnable accao) {
        JButton b = new JButton(texto);
        b.addActionListener(e -> executar(accao, texto));
        return b;
    }

    private static void executar(Runnable accao, String texto) {
        try {
            accao.run();
        } catch (RuntimeException | LinkageError e) {
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(null, "Não foi possível abrir \"" + texto + "\":\n" + e.getMessage(),
                    texto, javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private static volatile boolean agendadoresIniciados;

    /**
     * Envios automáticos (WhatsApp e e-mail nas horas marcadas) e o fecho geral
     * do dia. Uma vez por arranque; arranca numa thread de baixa prioridade,
     * alguns segundos depois de o menu abrir, para não pesar na entrada.
     */
    public static synchronized void iniciarAgendadores() {
        if (agendadoresIniciados) {
            return;
        }
        agendadoresIniciados = true;
        Thread t = new Thread(() -> {
            try {
                Thread.sleep(8000);
                comercial.whatsapp.WhatsAppAgendador.iniciar();
                comercial.email.EmailAgendador.iniciar();
                FechoGeralAgendador.iniciar();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (Throwable e) {
                System.err.println("[Fechos] agendadores não iniciados: " + e);
            }
        }, "kitanda-agendadores");
        t.setDaemon(true);
        t.setPriority(Thread.MIN_PRIORITY);
        t.start();
    }
}
