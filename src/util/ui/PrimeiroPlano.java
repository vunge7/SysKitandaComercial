package util.ui;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import java.awt.Frame;
import java.awt.Window;

/**
 * Põe uma janela em primeiro plano no Windows a sério, com o teclado.
 *
 * <p>
 * O toFront() do Java não chega quando o sistema arranca: o Windows não deixa
 * uma aplicação roubar o primeiro plano (só faz piscar o botão na barra). O
 * Java dá na mesma o foco ao campo e o cursor pisca, mas as teclas vão para a
 * janela que o Windows tem à frente: era preciso clicar no campo para escrever.
 * Aqui a entrada de teclado liga-se por momentos à janela que está à frente
 * (AttachThreadInput), o que torna o SetForegroundWindow permitido.
 *
 * <p>
 * Só no Windows e só com a JNA (lib/jna-5.13.0.jar); sem ela fica o toFront().
 */
public final class PrimeiroPlano {

    private static final int SW_RESTORE = 9;
    private static final int SW_SHOW = 5;

    private PrimeiroPlano() {
    }

    interface User32 extends Library {

        Pointer GetForegroundWindow();

        int GetWindowThreadProcessId(Pointer hwnd, Pointer pid);

        boolean AttachThreadInput(int de, int para, boolean ligar);

        boolean SetForegroundWindow(Pointer hwnd);

        boolean BringWindowToTop(Pointer hwnd);

        boolean ShowWindow(Pointer hwnd, int comando);
    }

    interface Kernel32 extends Library {

        int GetCurrentThreadId();
    }

    private static User32 user32;
    private static Kernel32 kernel32;
    private static boolean indisponivel;

    /** true se a janela ficou em primeiro plano no Windows (ou se não é Windows). Nunca lança excepção. */
    public static synchronized boolean trazer(Window janela) {
        if (janela == null || !janela.isShowing()) {
            return false;
        }
        janela.toFront();
        if (indisponivel || !System.getProperty("os.name", "").toLowerCase().contains("windows")) {
            return true;
        }
        try {
            if (user32 == null) {
                user32 = Native.load("user32", User32.class);
                kernel32 = Native.load("kernel32", Kernel32.class);
            }
            Pointer alvo = Native.getWindowPointer(janela);
            if (alvo == null) {
                return false;
            }
            Pointer frente = user32.GetForegroundWindow();
            if (alvo.equals(frente)) {
                return true;
            }
            int eu = kernel32.GetCurrentThreadId();
            int deFrente = frente == null ? 0 : user32.GetWindowThreadProcessId(frente, null);
            boolean ligado = deFrente != 0 && deFrente != eu && user32.AttachThreadInput(deFrente, eu, true);
            try {
                boolean minimizada = janela instanceof Frame && (((Frame) janela).getExtendedState() & Frame.ICONIFIED) != 0;
                user32.ShowWindow(alvo, minimizada ? SW_RESTORE : SW_SHOW);
                user32.BringWindowToTop(alvo);
                user32.SetForegroundWindow(alvo);
            } finally {
                if (ligado) {
                    user32.AttachThreadInput(deFrente, eu, false);
                }
            }
            return alvo.equals(user32.GetForegroundWindow());
        } catch (Throwable e) {
            indisponivel = true;
            System.err.println("[PrimeiroPlano] JNA indisponível, fica o toFront(): " + e);
            return false;
        }
    }
}
