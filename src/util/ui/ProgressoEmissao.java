package util.ui;

import java.awt.AWTEvent;
import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.AWTEventListener;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import javax.swing.JWindow;
import javax.swing.Timer;

/**
 * Barra de progresso da emissão de um documento. A emissão corre na thread da
 * interface (gravação, impressão), que fica ocupada e não pinta nada; por
 * isso esta janela é pintada por uma thread própria, directamente na tela.
 *
 * A duração real não se conhece: a barra avança depressa no início, abranda e
 * nunca chega ao fim sozinha; só enche quando a interface fica livre, ou seja,
 * quando a emissão terminou. Não afirma sucesso: o resultado é dito pelas
 * mensagens do próprio processo.
 */
public final class ProgressoEmissao {

    private static final int LARGURA = 460;
    private static final int ALTURA = 136;
    private static final long NOTA_MS = 5000;
    private static final long DEMORADO_MS = 30000;
    private static final String[] NOTAS = {
        "Por favor aguarde, não feche o sistema.",
        "A gravação e a impressão podem demorar alguns segundos.",
        "O documento aparece assim que o processo terminar."
    };
    private static final double LIMITE = 0.93;
    private static final double CONSTANTE_MS = 7000;
    private static final long CONCLUSAO_MS = 280;

    private static volatile Janela actual;

    private ProgressoEmissao() {
    }

    /** Mostra a barra. Chamar na thread da interface. */
    public static void iniciar(String texto) {
        iniciar("A processar o documento", texto, NOTAS);
    }

    /** Barra com título e notas próprios (ex.: alojamento sem documento). */
    public static void iniciar(String titulo, String texto, String[] notas) {
        if (actual != null) {
            actual.texto = texto;
            return;
        }
        actual = new Janela(texto);
        actual.titulo = titulo;
        actual.notas = notas == null || notas.length == 0 ? NOTAS : notas;
        actual.abrir();
    }

    /**
     * Enche e fecha já, antes de a confirmação do processo aparecer. Espera o
     * tempo da animação (a barra é pintada por outra thread), por isso a
     * confirmação surge logo a seguir à barra cheia.
     */
    public static void concluirAgora() {
        Janela j = actual;
        if (j == null) {
            return;
        }
        actual = null;
        j.fechar(true);
        try {
            Thread.sleep(CONCLUSAO_MS + 160);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        j.janela.dispose();
    }

    /** Etapa real em curso. Sem barra aberta não faz nada: pode ser chamado de qualquer fluxo. */
    public static void texto(String texto) {
        Janela j = actual;
        if (j != null) {
            j.texto = texto;
        }
    }

    /** Fecha já, sem encher (ex.: faltam dados e o processo não começou). */
    public static void cancelar() {
        if (actual != null) {
            actual.fechar(false);
            actual = null;
        }
    }

    /**
     * Enche e fecha assim que a interface ficar livre, venha de onde vier o
     * trabalho que falta (ex.: o alojamento grava depois de o diálogo fechar).
     * Enquanto houver uma mensagem aberta o processo ainda não acabou.
     */
    public static void fecharQuandoTerminar() {
        final Janela alvo = actual;
        if (alvo == null) {
            return;
        }
        EventQueue.invokeLater(() -> tentarConcluir(alvo));
    }

    private static void tentarConcluir(Janela alvo) {
        if (alvo != actual) {
            return;
        }
        if (haMensagemAberta()) {
            Timer espera = new Timer(200, e -> tentarConcluir(alvo));
            espera.setRepeats(false);
            espera.start();
            return;
        }
        alvo.fechar(true);
        actual = null;
    }

    private static boolean haMensagemAberta() {
        for (Window w : Window.getWindows()) {
            if (w instanceof Dialog && w.isShowing() && ((Dialog) w).isModal()) {
                return true;
            }
        }
        return false;
    }

    private static final class Janela implements AWTEventListener {

        private final JWindow janela = new JWindow();
        private final Canvas tela = new Canvas();
        private final BufferedImage imagem = new BufferedImage(LARGURA, ALTURA, BufferedImage.TYPE_INT_RGB);
        private final long inicio = System.currentTimeMillis();
        private volatile String texto;
        private volatile String titulo = "A processar o documento";
        private volatile String[] notas = NOTAS;
        private volatile boolean aCorrer = true;
        private volatile long concluidoEm = 0;
        private double progressoAoConcluir;

        Janela(String texto) {
            this.texto = texto;
        }

        void abrir() {
            tela.setBackground(MenuWeb.AZUL_NOITE);
            tela.setIgnoreRepaint(true);
            janela.setIconImages(MenuWeb.iconesAplicacao());
            janela.setAlwaysOnTop(true);
            janela.getContentPane().add(tela);
            janela.setSize(LARGURA, ALTURA);
            janela.setLocationRelativeTo(null);
            Toolkit.getDefaultToolkit().addAWTEventListener(this, AWTEvent.WINDOW_EVENT_MASK);
            janela.setVisible(true);

            Thread pintura = new Thread(this::animar, "ProgressoEmissao");
            pintura.setDaemon(true);
            pintura.start();
        }

        void fechar(boolean encher) {
            Toolkit.getDefaultToolkit().removeAWTEventListener(this);
            if (encher) {
                progressoAoConcluir = progresso(System.currentTimeMillis());
                concluidoEm = System.currentTimeMillis();
            } else {
                aCorrer = false;
                janela.dispose();
            }
        }

        /** Uma mensagem do processo tem de ficar visível: a barra sai da frente. */
        @Override
        public void eventDispatched(AWTEvent e) {
            Window w = ((WindowEvent) e).getWindow();
            if (!(w instanceof Dialog) || !((Dialog) w).isModal()) {
                return;
            }
            if (e.getID() == WindowEvent.WINDOW_OPENED) {
                janela.setVisible(false);
            } else if (e.getID() == WindowEvent.WINDOW_CLOSED && aCorrer && !janela.isVisible()) {
                janela.setVisible(true);
            }
        }

        private double progresso(long agora) {
            return LIMITE * (1 - Math.exp(-(agora - inicio) / CONSTANTE_MS));
        }

        private void animar() {
            while (aCorrer) {
                long agora = System.currentTimeMillis();
                long fim = concluidoEm;
                double p;
                if (fim == 0) {
                    p = progresso(agora);
                } else {
                    p = Math.min(1, progressoAoConcluir + (1 - progressoAoConcluir) * (agora - fim) / (double) CONCLUSAO_MS);
                }
                pintar(p, agora);
                if (p >= 1) {
                    aCorrer = false;
                    dormir(120);
                    EventQueue.invokeLater(janela::dispose);
                    return;
                }
                dormir(33);
            }
        }

        private static void dormir(long ms) {
            try {
                Thread.sleep(ms);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        private static String cortar(Graphics2D g, String s, int largura) {
            FontMetrics fm = g.getFontMetrics();
            String r = s;
            while (fm.stringWidth(r) > largura && r.length() > 1) {
                r = r.substring(0, r.length() - 1);
            }
            return r;
        }

        private void pintar(double p, long agora) {
            Graphics destino = tela.getGraphics();
            if (destino == null) {
                return; // ainda a abrir, ou escondida por uma mensagem
            }
            Graphics2D g = ComponentesWeb.suave(imagem.getGraphics());
            g.setPaint(new GradientPaint(0, 0, MenuWeb.AZUL_NOITE, LARGURA, ALTURA, MenuWeb.AZUL_MARINHO));
            g.fillRect(0, 0, LARGURA, ALTURA);
            g.setColor(MenuWeb.DOURADO);
            g.setStroke(new BasicStroke(1.5f));
            g.drawRect(0, 0, LARGURA - 1, ALTURA - 1);

            g.setColor(Color.WHITE);
            g.setFont(new Font("Georgia", Font.BOLD, 17));
            g.drawString(titulo, 22, 36);

            g.setColor(MenuWeb.DOURADO_CLARO);
            g.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(12.5f));
            g.drawString(cortar(g, texto, LARGURA - 44), 22, 58);

            // notas de paciência, a rodar, e tempo decorrido
            long decorrido = agora - inicio;
            String nota = decorrido >= DEMORADO_MS
                    ? "Está a demorar mais do que o habitual. Continue a aguardar."
                    : notas[(int) (decorrido / NOTA_MS % notas.length)];
            String tempo = decorrido / 1000 + " s";
            g.setFont(EstiloUI.FONTE.deriveFont(12f));
            FontMetrics fmNota = g.getFontMetrics();
            g.setColor(new Color(190, 205, 225));
            g.drawString(cortar(g, nota, LARGURA - 44 - fmNota.stringWidth(tempo) - 10), 22, 112);
            g.drawString(tempo, LARGURA - 22 - fmNota.stringWidth(tempo), 112);

            int x = 22, y = 72, w = LARGURA - 44, h = 12;
            g.setColor(new Color(255, 255, 255, 40));
            g.fillRoundRect(x, y, w, h, h, h);
            int cheio = Math.max(h, (int) (w * p));
            g.setPaint(new GradientPaint(x, 0, MenuWeb.DOURADO, x + w, 0, MenuWeb.DOURADO_CLARO));
            g.fillRoundRect(x, y, cheio, h, h, h);
            // brilho a correr: mostra que continua a trabalhar quando a barra abranda
            int brilho = (int) ((agora - inicio) / 6 % (cheio + 60)) - 30;
            java.awt.Shape recorte = g.getClip();
            g.setClip(new java.awt.geom.RoundRectangle2D.Float(x, y, cheio, h, h, h));
            g.setPaint(new GradientPaint(x + brilho - 30, 0, new Color(255, 255, 255, 0),
                    x + brilho, 0, new Color(255, 255, 255, 150), true));
            g.fillRect(x + brilho - 30, y, 60, h);
            g.setClip(recorte);
            g.dispose();

            destino.drawImage(imagem, 0, 0, null);
            destino.dispose();
            Toolkit.getDefaultToolkit().sync();
        }
    }
}
