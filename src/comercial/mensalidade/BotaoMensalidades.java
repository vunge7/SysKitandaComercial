package comercial.mensalidade;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Window;
import java.time.LocalDate;
import java.util.List;
import javax.swing.JButton;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import util.BDConexao;
import util.ui.ComponentesWeb;
import util.ui.EstiloUI;

/**
 * Botão "Mensalidades" da janela de vendas: abre o estado dos clientes (F9).
 * Mostra quantos clientes estão em alerta e toma a cor do mais urgente
 * (vermelho / laranja / verde). Actualiza-se sozinho de 5 em 5 minutos.
 * Quando acende vermelho (ou aumentam os clientes a vermelho) o alarme
 * arranca logo e o botão pisca enquanto toca; clicar nele silencia-o.
 */
public class BotaoMensalidades extends JButton {

    private int vermelhos, laranjas;
    private boolean lido;
    private final Timer temporizador;
    /** Pisca enquanto o alarme toca. */
    private final Timer pisca;
    private boolean aceso = true;

    public BotaoMensalidades() {
        super("Mensalidades");
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setOpaque(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setToolTipText("Estado das mensalidades dos clientes (F9)");
        addActionListener(e -> {
            // quem clica está a tratar dos devedores: o alarme cala-se
            AlertasMensalidade.pararAlarme();
            EstadoClientesDialog.abrir(SwingUtilities.getWindowAncestor(this), BDConexao.getInstancia());
        });
        temporizador = new Timer(5 * 60 * 1000, e -> actualizar());
        temporizador.setInitialDelay(3000);
        temporizador.start();
        pisca = new Timer(500, e -> {
            boolean aTocar = AlertasMensalidade.alarmeATocar();
            aceso = !aTocar || !aceso;
            repaint();
            if (!aTocar) {
                ((Timer) e.getSource()).stop();
            }
        });
    }

    @Override
    public void removeNotify() {
        temporizador.stop();
        pisca.stop();
        super.removeNotify();
    }

    @Override
    public void addNotify() {
        super.addNotify();
        if (!temporizador.isRunning()) {
            temporizador.start();
        }
    }

    public void actualizar() {
        new SwingWorker<int[], Void>() {
            ConfigMensalidade config;

            @Override
            protected int[] doInBackground() throws Exception {
                config = ConfigMensalidade.ler();
                int dias = config.diasAlerta;
                int v = 0, l = 0;
                java.util.Map<Integer, Integer> nivel = new java.util.HashMap<>(); // 2 = vermelho, 1 = laranja
                List<Devedores.Linha> linhas = Devedores.carregar(LocalDate.now(), dias);
                for (Devedores.Linha x : linhas) {
                    if (x.inactivo()) {
                        continue;
                    }
                    int n = !x.emAtraso.isEmpty() || x.diasParaVencer <= 1 ? 2 : x.diasParaVencer <= dias ? 1 : 0;
                    nivel.merge(x.clienteId, n, Math::max);
                }
                for (int n : nivel.values()) {
                    if (n == 2) {
                        v++;
                    } else if (n == 1) {
                        l++;
                    }
                }
                return new int[]{v, l};
            }

            @Override
            protected void done() {
                try {
                    int[] r = get();
                    int antes = vermelhos;
                    vermelhos = r[0];
                    laranjas = r[1];
                    lido = true;
                    setToolTipText("<html><b>Mensalidades (F9)</b><br>" + vermelhos + " cliente(s) urgente(s) · "
                            + laranjas + " a vencer em breve</html>");
                    repaint();
                    // acendeu vermelho (ou mais clientes a vermelho): alarme já
                    if (vermelhos > antes) {
                        AlertasMensalidade.alarmePorVermelho(config);
                        if (AlertasMensalidade.alarmeATocar() && !pisca.isRunning()) {
                            pisca.start();
                        }
                    }
                } catch (Exception ignorar) {
                    // sem ligação: fica como estava
                }
            }
        }.execute();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = ComponentesWeb.suave(g);
        int w = getWidth(), h = getHeight();
        Color cor = !lido ? ComponentesWeb.AZUL_MEDIO : vermelhos > 0 ? ComponentesWeb.VERMELHO
                : laranjas > 0 ? ComponentesWeb.LARANJA : ComponentesWeb.VERDE;
        if (!aceso) {
            cor = cor.darker().darker();
        }
        g2.setColor(getModel().isRollover() ? cor.darker() : cor);
        g2.fillRoundRect(0, 0, w - 1, h - 1, 14, 14);
        g2.setColor(Color.WHITE);
        g2.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(13f));
        FontMetrics fm = g2.getFontMetrics();
        String t = "Mensalidades";
        int total = vermelhos + laranjas;
        String sub = !lido ? "F9" : total == 0 ? "tudo em dia" : total + " em alerta";
        int y = h / 2 - 2;
        g2.drawString(t, (w - fm.stringWidth(t)) / 2, y);
        g2.setFont(EstiloUI.FONTE.deriveFont(11.5f));
        fm = g2.getFontMetrics();
        g2.drawString(sub, (w - fm.stringWidth(sub)) / 2, y + fm.getHeight());
        g2.dispose();
    }

    /** F9 na janela de vendas (as duas usam isto). */
    public static void instalarTecla(javax.swing.JRootPane raiz) {
        raiz.getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(javax.swing.KeyStroke.getKeyStroke("F9"), "estadoMensalidades");
        raiz.getActionMap().put("estadoMensalidades", new javax.swing.AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                Window w = SwingUtilities.getWindowAncestor(raiz);
                EstadoClientesDialog.abrir(w, BDConexao.getInstancia());
            }
        });
    }
}
