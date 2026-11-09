package util.ui;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.Container;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
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

    private RelatoriosWeb() {
    }

    /** Uma vez, no arranque. */
    public static synchronized void instalar() {
        if (instalado) {
            return;
        }
        instalado = true;
        Toolkit.getDefaultToolkit().addAWTEventListener(evento -> {
            if (evento.getID() == WindowEvent.WINDOW_OPENED && evento.getSource() instanceof Window) {
                Window w = (Window) evento.getSource();
                if (eVisualizador(w)) {
                    SwingUtilities.invokeLater(() -> preparar(w));
                }
            }
        }, AWTEvent.WINDOW_EVENT_MASK);
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
