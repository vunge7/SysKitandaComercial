package util.ui;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.Container;
import java.awt.Frame;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.HierarchyEvent;
import java.awt.event.WindowEvent;
import java.lang.reflect.Method;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

/**
 * Visão dos relatórios (iReport / JasperReports) mais confortável e
 * responsiva, para TODOS os relatórios do sistema de uma só vez: as janelas do
 * JasperViewer abrem maximizadas, com o zoom "ajustar à largura" (que volta a
 * ajustar quando a janela muda de tamanho), e com o ícone e o título do
 * negócio. Não mexe nos relatórios nem em quem os abre.
 *
 * <p>
 * Funciona com as duas versões do JasperReports que estão no projecto: o
 * visualizador é encontrado pela classe e o zoom é pedido por reflexão.
 */
public final class RelatoriosWeb {

    private static volatile boolean instalado = false;
    /** a meio de um ajuste de tamanho (evita reentrar no próprio setBounds). */
    private static boolean ajustando;

    private RelatoriosWeb() {
    }

    /** Uma vez, no arranque. */
    public static synchronized void instalar() {
        if (instalado) {
            return;
        }
        instalado = true;
        // maximizada ANTES de aparecer. O JasperViewer faz pack(), setSize(...) e setLocation(...) no
        // construtor e só depois setVisible: maximizar no WINDOW_OPENED fazia o documento abrir pequeno
        // e saltar para o ecrã todo. Quando a janela nativa é criada (no pack) junta-se-lhe um gancho
        // invisível; o setVisible valida a janela ANTES de a mostrar e é nessa validação que o gancho
        // lhe dá a área útil do ecrã e a maximiza, já depois do setSize/setLocation do construtor.
        Toolkit.getDefaultToolkit().addAWTEventListener(evento -> {
            HierarchyEvent h = (HierarchyEvent) evento;
            if ((h.getChangeFlags() & HierarchyEvent.DISPLAYABILITY_CHANGED) != 0 && h.getComponent() == h.getChanged()
                    && h.getComponent() instanceof javax.swing.JFrame && eVisualizador((Window) h.getComponent())) {
                javax.swing.JFrame f = (javax.swing.JFrame) h.getComponent();
                if (f.isDisplayable() && f.getRootPane().getClientProperty(Gancho.class) == null) {
                    f.getRootPane().putClientProperty(Gancho.class, Boolean.TRUE);
                    f.getLayeredPane().add(new Gancho(f), javax.swing.JLayeredPane.DEFAULT_LAYER);
                }
            }
        }, AWTEvent.HIERARCHY_EVENT_MASK);
        Toolkit.getDefaultToolkit().addAWTEventListener(evento -> {
            if (evento.getID() == WindowEvent.WINDOW_OPENED && evento.getSource() instanceof Window) {
                Window w = (Window) evento.getSource();
                if (eVisualizador(w)) {
                    SwingUtilities.invokeLater(() -> preparar(w));
                }
            }
        }, AWTEvent.WINDOW_EVENT_MASK);
    }

    /**
     * Componente invisível dentro do visualizador: fica "por validar" enquanto a
     * janela não aparece, para o setVisible lhe chamar o doLayout() mesmo antes
     * de a mostrar; aí a janela recebe a área útil do ecrã e fica maximizada.
     */
    private static final class Gancho extends javax.swing.JComponent {

        private final Frame janela;

        Gancho(Frame janela) {
            this.janela = janela;
            setBounds(0, 0, 0, 0);
        }

        @Override
        public boolean isValid() {
            // null durante o construtor do JComponent
            return (janela == null || janela.isShowing()) && super.isValid();
        }

        @Override
        public void doLayout() {
            if (janela.isShowing() || ajustando) {
                return; // já visível: o utilizador pode mudar o tamanho à vontade
            }
            ajustando = true;
            try {
                Rectangle util = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
                if (!util.equals(janela.getBounds())) {
                    janela.setBounds(util);
                }
                janela.setExtendedState(janela.getExtendedState() | Frame.MAXIMIZED_BOTH);
            } catch (RuntimeException e) {
                e.printStackTrace(); // nunca impede de ver o relatório
            } finally {
                ajustando = false;
            }
        }
    }

    private static boolean eVisualizador(Window w) {
        return w.getClass().getName().startsWith("net.sf.jasperreports");
    }

    private static void preparar(Window w) {
        try {
            w.setIconImages(MenuWeb.iconesAplicacao());
            if (w instanceof Frame) {
                Frame f = (Frame) w;
                String t = f.getTitle();
                if (t == null || t.trim().isEmpty() || t.toLowerCase().contains("jasperviewer")) {
                    f.setTitle("Kitanda  ·  " + Tema.area() + "  ·  Relatório");
                }
                f.setExtendedState(f.getExtendedState() | Frame.MAXIMIZED_BOTH);
            }
            final Component viewer = procurarViewer(w);
            if (viewer == null) {
                return;
            }
            // depois de maximizar, quando o tamanho já é o final
            Timer ajustar = new Timer(250, e -> ajustarLargura(viewer));
            ajustar.setRepeats(false);
            ajustar.start();
            viewer.addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    ajustar.restart();
                }
            });
        } catch (RuntimeException e) {
            e.printStackTrace(); // nunca impede de ver o relatório
        }
    }

    /** O componente do visualizador (o que tem setFitWidthZoomRatio). */
    private static Component procurarViewer(Component c) {
        if (metodo(c, "setFitWidthZoomRatio") != null) {
            return c;
        }
        if (c instanceof Container) {
            for (Component f : ((Container) c).getComponents()) {
                Component r = procurarViewer(f);
                if (r != null) {
                    return r;
                }
            }
        }
        return null;
    }

    private static void ajustarLargura(Component viewer) {
        Method m = metodo(viewer, "setFitWidthZoomRatio");
        if (m != null) {
            try {
                m.invoke(viewer);
            } catch (ReflectiveOperationException | RuntimeException e) {
                // versão sem este zoom: fica o zoom por omissão
            }
        }
    }

    private static Method metodo(Component c, String nome) {
        try {
            return c.getClass().getMethod(nome);
        } catch (NoSuchMethodException | SecurityException e) {
            return null;
        }
    }
}
