package util.ui;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import java.awt.AWTEvent;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.HierarchyEvent;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import javax.swing.SwingUtilities;

/**
 * Uma só entrada na barra de tarefas, como no Primavera, WinRest ou XD (pedido
 * do utilizador em 2026-11-14).
 *
 * Os formulários do sistema são JFrame (janelas principais): o Windows dava a
 * cada um o seu botão na barra de tarefas. Aqui, cada JFrame que abre passa a
 * ser "filho" (owned window, GWLP_HWNDPARENT) da janela principal do momento —
 * o Login e, depois de entrar, o menu do utilizador:
 * <ul>
 * <li>sai da barra de tarefas; mantém a barra de título normal (maximizar...);</li>
 * <li>fica sempre à frente da janela principal;</li>
 * <li>minimizar um formulário minimiza o sistema todo (e volta tudo junto);</li>
 * <li>antes de a janela principal fechar (terminar sessão), os filhos são
 *     soltos, para o Windows não os destruir por arrasto.</li>
 * </ul>
 * Só no Windows e só com a JNA (lib/jna-5.13.0.jar); sem ela fica tudo como antes.
 */
public final class JanelasFilhas {

    private static final int GWLP_HWNDPARENT = -8;
    private static volatile boolean instalado;
    private static Window principal;
    /** filhos actuais (fracos: uma janela fechada desaparece sozinha). */
    private static final Map<Window, Boolean> FILHOS = new WeakHashMap<>();
    private static User32 user32;

    private JanelasFilhas() {
    }

    interface User32 extends Library {

        Pointer SetWindowLongPtrW(Pointer hwnd, int indice, Pointer valor); // 64 bits

        int SetWindowLongW(Pointer hwnd, int indice, int valor);             // 32 bits
    }

    /** Liga o mecanismo (uma vez, no arranque). Nunca lança excepção. */
    public static synchronized void instalar() {
        if (instalado) {
            return;
        }
        instalado = true;
        if (!System.getProperty("os.name", "").toLowerCase().contains("windows")) {
            return;
        }
        try {
            user32 = Native.load("user32", User32.class);
        } catch (Throwable e) {
            System.err.println("[Janelas] JNA indisponível, as janelas ficam como antes: " + e);
            return;
        }
        Toolkit.getDefaultToolkit().addAWTEventListener(e -> {
            try {
                if (e instanceof WindowEvent) {
                    tratar((WindowEvent) e);
                } else if (e instanceof HierarchyEvent) {
                    HierarchyEvent h = (HierarchyEvent) e;
                    // a janela principal vai deixar de aparecer (dispose / setVisible(false)):
                    // este evento chega antes de o Windows a destruir
                    if (h.getComponent() == principal && (h.getChangeFlags() & HierarchyEvent.SHOWING_CHANGED) != 0
                            && !principal.isShowing()) {
                        soltarFilhos();
                        principal = null;
                        escolherNovoPrincipal();
                    }
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }, AWTEvent.WINDOW_EVENT_MASK | AWTEvent.WINDOW_STATE_EVENT_MASK | AWTEvent.HIERARCHY_EVENT_MASK);
    }

    private static void tratar(WindowEvent e) {
        Window w = e.getWindow();
        if (!(w instanceof Frame)) {
            return; // os JDialog já têm dono pelo Java
        }
        switch (e.getID()) {
            case WindowEvent.WINDOW_OPENED:
                if (w == principal) {
                    break;
                }
                // o menu substitui o Login (em alguns perfis o menu abre antes de o Login fechar)
                // - e também um formulário que tenha aberto antes do menu
                if (principal == null || !principal.isDisplayable() || !principal.isShowing()
                        || (eEntrada(w) && (eLogin(principal) || !eEntrada(principal)))) {
                    tornarPrincipal(w);
                } else {
                    adoptar(w);
                }
                break;
            case WindowEvent.WINDOW_STATE_CHANGED:
                // minimizar um formulário minimiza o sistema todo (um filho não tem botão na barra)
                if (FILHOS.containsKey(w) && (((Frame) w).getExtendedState() & Frame.ICONIFIED) != 0) {
                    Frame f = (Frame) w;
                    SwingUtilities.invokeLater(() -> {
                        f.setExtendedState(f.getExtendedState() & ~Frame.ICONIFIED);
                        if (principal instanceof Frame) {
                            ((Frame) principal).setExtendedState(((Frame) principal).getExtendedState() | Frame.ICONIFIED);
                        }
                    });
                }
                break;
            case WindowEvent.WINDOW_CLOSED:
                FILHOS.remove(w);
                if (w == principal) {
                    principal = null;
                    escolherNovoPrincipal();
                }
                break;
            default:
                break;
        }
    }

    /**
     * Ecrãs que o Login abre conforme o tipo de utilizador (comercial): o menu
     * de raiz, o Front Office, a venda do caixa e o Front Office da lavandaria.
     */
    private static final java.util.Set<String> ENTRADAS = new java.util.HashSet<>(java.util.Arrays.asList(
            "visao.RootVisao", "visao.FrontOfficeVisao", "visao.VendaUsuarioVisao",
            "visao.JanelaFrontOfficeLavandariaVisao"));

    /** Janelas que podem ser a principal: o Login, os menus do pacote visao e os ecrãs de entrada. */
    private static boolean eEntrada(Window w) {
        String n = w.getClass().getName();
        return eLogin(w) || ENTRADAS.contains(n) || n.startsWith("visao.Menu") && n.indexOf('.', 6) == -1;
    }

    private static boolean eLogin(Window w) {
        return w != null && "visao.LoginVisao".equals(w.getClass().getName());
    }

    private static void tornarPrincipal(Window w) {
        Window anterior = principal;
        if (FILHOS.containsKey(w)) {
            definirDono(w, null); // era filho (ex.: do Login): passa a ter o botão na barra
            FILHOS.remove(w);
        }
        principal = w;
        if (anterior != null && anterior != w && anterior.isShowing()) {
            adoptar(anterior); // o Login que ainda não fechou fica atrás do menu, sem botão
        }
    }

    /** Fechou a janela principal: um menu ainda aberto passa a principal. */
    private static void escolherNovoPrincipal() {
        for (Window w : Window.getWindows()) {
            if (w instanceof Frame && w.isShowing() && eEntrada(w)) {
                tornarPrincipal(w);
                return;
            }
        }
    }

    private static void adoptar(Window filho) {
        if (definirDono(filho, principal)) {
            FILHOS.put(filho, Boolean.TRUE);
        }
    }

    private static void soltarFilhos() {
        List<Window> l = new ArrayList<>(FILHOS.keySet());
        for (Window f : l) {
            if (f.isDisplayable()) {
                definirDono(f, null);
            }
        }
        FILHOS.clear();
    }

    /** dono = null solta a janela (volta a ter botão próprio). */
    private static boolean definirDono(Window janela, Window dono) {
        try {
            Pointer hwnd = Native.getWindowPointer(janela);
            if (hwnd == null) {
                return false;
            }
            Pointer hwndDono = dono == null ? null : Native.getWindowPointer(dono);
            if (dono != null && hwndDono == null) {
                return false;
            }
            if (Native.POINTER_SIZE == 8) {
                user32.SetWindowLongPtrW(hwnd, GWLP_HWNDPARENT, hwndDono);
            } else {
                user32.SetWindowLongW(hwnd, GWLP_HWNDPARENT, hwndDono == null ? 0 : (int) Pointer.nativeValue(hwndDono));
            }
            return true;
        } catch (Throwable t) {
            System.err.println("[Janelas] não foi possível agrupar " + janela.getClass().getSimpleName() + ": " + t);
            return false;
        }
    }
}
