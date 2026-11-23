package util.ui;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Aplica o design web (montarLayoutWeb / montarLayoutResponsivo) de um ecrã sem
 * pôr em risco o funcionamento: se a montagem falhar (imagem em falta, fonte,
 * componente inesperado...), o erro fica registado e a janela continua a abrir
 * com a lógica intacta, em vez de o construtor rebentar e o ecrã não aparecer.
 * Também mede o tempo da montagem e avisa no registo se passar do limite, para
 * se encontrar depressa um ecrã lento.
 */
public final class LayoutSeguro {

    private static final Logger LOG = Logger.getLogger(LayoutSeguro.class.getName());

    /** Acima disto (ms) a montagem fica registada como lenta. */
    private static final long LIMITE_MS = 400;

    private LayoutSeguro() {
    }

    public static void aplicar(java.awt.Component janela, String ecra, Runnable montagem) {
        long inicio = System.nanoTime();
        try {
            montagem.run();
        } catch (RuntimeException | LinkageError e) {
            LOG.log(Level.SEVERE, "Design web de \"" + ecra + "\" não aplicado; o ecrã abre na mesma.", e);
            if (janela != null) {
                // o que já tiver sido montado fica arrumado e visível
                janela.invalidate();
                janela.validate();
                janela.repaint();
            }
        } finally {
            long ms = (System.nanoTime() - inicio) / 1_000_000L;
            if (ms > LIMITE_MS) {
                LOG.log(Level.WARNING, "Design web de \"{0}\" demorou {1} ms a montar.", new Object[]{ecra, ms});
            }
        }
    }
}
