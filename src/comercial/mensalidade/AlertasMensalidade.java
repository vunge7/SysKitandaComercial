package comercial.mensalidade;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Window;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.SourceDataLine;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import util.BDConexao;
import util.ui.ComponentesWeb;
import util.ui.EstiloUI;

/**
 * Alertas de renovação das mensalidades (Ginásio, Centro Infantil, Prestação
 * de Serviço). De {@code intervalo} em {@code intervalo} minutos (60 por
 * omissão, em mensalidade_config) procura os contratos cujo próximo mês
 * vence nos próximos {@code dias_alerta} dias:
 * <ul>
 * <li>laranja — "Faltam X dias para a renovação da mensalidade do Cliente Y";</li>
 * <li>vermelho — vence hoje;</li>
 * <li>vermelho — devedores: mensalidades já em atraso (se {@code alertar_atrasados}).</li>
 * </ul>
 * A janela aparece no canto do ecrã sem tirar o foco a quem está a
 * trabalhar, pisca, toca um som e fecha-se ao fim de {@code duracao}
 * segundos (120 por omissão). Com {@code alarme_continuo} o som é um
 * alarme: repete-se até alguém carregar em "Silenciar", mexer no alerta
 * ou o alerta fechar.
 */
public final class AlertasMensalidade {

    private static final Color LARANJA = ComponentesWeb.LARANJA;
    private static final Color VERMELHO = ComponentesWeb.VERMELHO;

    private static Timer temporizador;
    private static Window dono;
    private static JanelaAlerta aberta;

    /** Um aviso: um contrato cujo mês vence dentro de 'dias' dias. */
    public static final class Aviso {

        public final String cliente, servico, telefone;
        public final long dias;
        public final LocalDate vencimento;
        /** Devedor: tem meses já vencidos e por pagar. */
        public final boolean atraso;
        public final long diasAtraso;
        public final int mesesAtraso;

        Aviso(Devedores.Linha l) {
            cliente = l.cliente;
            servico = l.servico;
            telefone = l.telefone;
            atraso = l.situacao == Devedores.Situacao.ATRASO;
            diasAtraso = l.diasAtraso;
            mesesAtraso = l.emAtraso.size();
            dias = atraso ? -l.diasAtraso : l.diasParaVencer;
            vencimento = atraso && !l.vencimentoPorMes.isEmpty() ? l.vencimentoPorMes.get(0) : l.proximoVencimento;
        }

        public boolean hoje() {
            return !atraso && dias == 0;
        }

        /** Vermelho a 1 dia ou menos e nos devedores; laranja até aos dias de alerta. */
        public boolean urgente() {
            return atraso || dias <= 1;
        }

        public String mensagem() {
            if (atraso) {
                return "O Cliente " + cliente + " está em atraso há " + diasAtraso + (diasAtraso == 1 ? " dia" : " dias")
                        + " (" + mesesAtraso + (mesesAtraso == 1 ? " mês" : " meses") + " por pagar)";
            }
            if (dias == 0) {
                return "Hoje é o dia da renovação da mensalidade do Cliente " + cliente;
            }
            return (dias == 1 ? "Falta 1 dia" : "Faltam " + dias + " dias") + " para a renovação da mensalidade do Cliente " + cliente;
        }
    }

    private AlertasMensalidade() {
    }

    /** Liga os alertas (no menu principal, depois do início de sessão). */
    public static synchronized void iniciar(Window janelaPrincipal) {
        // novo início de sessão: o alarme volta a tocar
        setSilenciado(false);
        if (!Mensalidades.activo()) {
            return;
        }
        dono = janelaPrincipal;
        if (temporizador == null) {
            temporizador = new Timer(60 * 60 * 1000, e -> verificarAgora(false));
            temporizador.setRepeats(true);
            // primeira verificação pouco depois de entrar
            temporizador.setInitialDelay(45 * 1000);
        }
        reprogramar();
    }

    /** Aplica o intervalo gravado (depois de mudar a configuração). */
    public static synchronized void reprogramar() {
        if (temporizador == null) {
            return;
        }
        ConfigMensalidade c = ConfigMensalidade.ler();
        temporizador.setDelay(c.intervaloMinutos * 60 * 1000);
        if (!temporizador.isRunning()) {
            temporizador.start();
        }
    }

    public static synchronized void parar() {
        if (temporizador != null) {
            temporizador.stop();
        }
    }

    /** Procura e mostra os alertas; manual = o utilizador pediu (avisa também quando não há nada). */
    public static void verificarAgora(boolean manual) {
        if (!Mensalidades.activo()) {
            return;
        }
        new SwingWorker<List<Aviso>, Void>() {
            ConfigMensalidade config;

            @Override
            protected List<Aviso> doInBackground() throws Exception {
                config = ConfigMensalidade.ler();
                List<Aviso> avisos = new ArrayList<>();
                if (!config.activos && !manual) {
                    return avisos;
                }
                for (Devedores.Linha l : Devedores.carregar(LocalDate.now(), config.diasAlerta)) {
                    boolean aVencer = l.situacao == Devedores.Situacao.VENCE_HOJE || l.situacao == Devedores.Situacao.A_VENCER;
                    boolean devedor = config.alertarAtrasados && l.situacao == Devedores.Situacao.ATRASO;
                    if (!l.inactivo() && (aVencer || devedor)) {
                        avisos.add(new Aviso(l));
                    }
                }
                // devedores primeiro (o atraso mais antigo à cabeça), depois os que vencem mais cedo
                avisos.sort((a, b) -> a.dias != b.dias ? Long.compare(a.dias, b.dias) : a.cliente.compareToIgnoreCase(b.cliente));
                return avisos;
            }

            @Override
            protected void done() {
                try {
                    List<Aviso> avisos = get();
                    if (avisos.isEmpty()) {
                        if (manual) {
                            JOptionPane.showMessageDialog(dono, "Nenhum devedor e nenhuma mensalidade a renovar nos próximos "
                                    + config.diasAlerta + " dias.");
                        }
                        return;
                    }
                    mostrar(avisos, config);
                } catch (Exception e) {
                    System.err.println("[Alertas mensalidade] " + e.getMessage());
                }
            }
        }.execute();
    }

    /** Só para pré-visualizações. */
    static JWindow criarJanela(List<Aviso> avisos, int duracao) {
        return new JanelaAlerta(null, avisos, duracao);
    }

    private static void mostrar(List<Aviso> avisos, ConfigMensalidade config) {
        if (aberta != null) {
            aberta.fechar();
        }
        Window venda = Mensalidades.janelaVendaAberta();
        if (venda != null) {
            // na venda: o estado de todos os clientes, com os cartões em alerta a piscar
            EstadoClientesDialog.abrirPorAlerta(venda, BDConexao.getInstancia());
        } else {
            aberta = new JanelaAlerta(dono, avisos, config.duracaoSegundos);
            aberta.setVisible(true);
        }
        if (config.som && !silenciado) {
            boolean urgente = avisos.stream().anyMatch(Aviso::urgente);
            if (config.alarmeContinuo) {
                iniciarAlarme(urgente, config.duracaoSegundos);
            } else {
                tocar(urgente);
            }
        }
    }

    // =====================================================================
    // ALARME (repete o som até ser silenciado)
    // =====================================================================
    private static volatile Thread alarme;

    /** Toca o alarme em ciclo durante, no máximo, 'segundos' (o tempo do alerta no ecrã). */
    static synchronized void iniciarAlarme(boolean urgente, int segundos) {
        pararAlarme();
        final long fim = System.currentTimeMillis() + Math.max(5, segundos) * 1000L;
        Thread t = new Thread(() -> {
            float taxa = 22050f;
            try (SourceDataLine linha = AudioSystem.getSourceDataLine(new AudioFormat(taxa, 8, 1, true, false))) {
                linha.open();
                linha.start();
                int[] freq = urgente ? new int[]{1175, 880, 1175, 880, 1175, 880} : new int[]{880, 1046, 1318};
                while (alarme == Thread.currentThread() && System.currentTimeMillis() < fim) {
                    for (int f : freq) {
                        tom(linha, taxa, f, urgente ? 170 : 220);
                        silencio(linha, taxa, 70);
                    }
                    linha.drain();
                    // pausa entre toques, interrompida logo ao silenciar
                    for (int i = 0; i < 20 && alarme == Thread.currentThread(); i++) {
                        Thread.sleep(100);
                    }
                }
            } catch (InterruptedException e) {
                // silenciado
            } catch (Exception e) {
                java.awt.Toolkit.getDefaultToolkit().beep();
            } finally {
                synchronized (AlertasMensalidade.class) {
                    if (alarme == Thread.currentThread()) {
                        alarme = null;
                    }
                }
            }
        }, "alarme-mensalidade");
        t.setDaemon(true);
        alarme = t;
        t.start();
    }

    /** Silencia o alarme (botão "Silenciar", mexer no alerta ou fechar). */
    public static synchronized void pararAlarme() {
        Thread t = alarme;
        alarme = null;
        if (t != null) {
            t.interrupt();
        }
    }

    public static boolean alarmeATocar() {
        return alarme != null;
    }

    /**
     * O botão "Mensalidades" da venda acabou de acender vermelho (ou há mais
     * clientes a vermelho): o alarme arranca logo, sem esperar pelo alerta de
     * hora a hora. Respeita "Alertas ligados", "Tocar som" e o "Silenciar alarme".
     */
    public static void alarmePorVermelho(ConfigMensalidade c) {
        if (c == null || !c.activos || !c.som || silenciado || alarmeATocar()) {
            return;
        }
        if (c.alarmeContinuo) {
            iniciarAlarme(true, c.duracaoSegundos);
        } else {
            tocar(true);
        }
    }

    // =====================================================================
    // SILENCIAR NA SESSÃO (checkbox na venda; volta a tocar no próximo login)
    // =====================================================================
    private static volatile boolean silenciado;
    private static final java.beans.PropertyChangeSupport ouvintes =
            new java.beans.PropertyChangeSupport(AlertasMensalidade.class);

    public static boolean isSilenciado() {
        return silenciado;
    }

    /** Silencia (ou volta a ligar) o som dos alertas até ao próximo início de sessão. */
    public static void setSilenciado(boolean valor) {
        boolean antes = silenciado;
        silenciado = valor;
        if (valor) {
            pararAlarme();
        }
        ouvintes.firePropertyChange("silenciado", antes, valor);
    }

    /**
     * Checkbox "Silenciar alarme" para as janelas de venda. Fica igual em
     * todas as janelas abertas; o alerta continua a aparecer, só o som cala.
     */
    public static javax.swing.JCheckBox criarCaixaSilenciar() {
        final javax.swing.JCheckBox caixa = new javax.swing.JCheckBox("Silenciar alarme", silenciado);
        caixa.setOpaque(false);
        caixa.setFocusable(false);
        caixa.setToolTipText("Cala o alarme dos devedores e renovações até ao próximo início de sessão");
        caixa.addActionListener(e -> setSilenciado(caixa.isSelected()));
        final java.beans.PropertyChangeListener ouvinte = ev -> SwingUtilities.invokeLater(
                () -> caixa.setSelected(silenciado));
        caixa.addHierarchyListener(ev -> {
            if ((ev.getChangeFlags() & java.awt.event.HierarchyEvent.DISPLAYABILITY_CHANGED) != 0) {
                if (caixa.isDisplayable()) {
                    ouvintes.addPropertyChangeListener(ouvinte);
                    caixa.setSelected(silenciado);
                } else {
                    ouvintes.removePropertyChangeListener(ouvinte);
                }
            }
        });
        return caixa;
    }

    // =====================================================================
    // SOM (gerado: não depende de ficheiros de áudio)
    // =====================================================================
    /** Três toques; vermelho (vence hoje) mais agudo e repetido. */
    public static void tocar(boolean urgente) {
        Thread t = new Thread(() -> {
            float taxa = 22050f;
            try (SourceDataLine linha = AudioSystem.getSourceDataLine(new AudioFormat(taxa, 8, 1, true, false))) {
                linha.open();
                linha.start();
                int[] freq = urgente ? new int[]{1175, 880, 1175, 880, 1175, 880} : new int[]{880, 1046, 1318};
                for (int f : freq) {
                    tom(linha, taxa, f, urgente ? 170 : 220);
                    silencio(linha, taxa, 70);
                }
                linha.drain();
            } catch (Exception e) {
                java.awt.Toolkit.getDefaultToolkit().beep();
            }
        }, "alerta-mensalidade-som");
        t.setDaemon(true);
        t.start();
    }

    private static void tom(SourceDataLine l, float taxa, int freq, int ms) {
        int n = (int) (taxa * ms / 1000);
        byte[] b = new byte[n];
        for (int i = 0; i < n; i++) {
            double env = Math.min(1.0, Math.min(i, n - i) / (taxa * 0.01)); // sem estalos
            b[i] = (byte) (Math.sin(2 * Math.PI * freq * i / taxa) * 100 * env);
        }
        l.write(b, 0, b.length);
    }

    private static void silencio(SourceDataLine l, float taxa, int ms) {
        byte[] b = new byte[(int) (taxa * ms / 1000)];
        l.write(b, 0, b.length);
    }

    // =====================================================================
    // JANELA
    // =====================================================================
    private static final class JanelaAlerta extends JWindow {

        private final Timer contagem;
        private final Timer pisca;
        private int restante;
        private boolean aceso = true;
        private final Color cor;

        JanelaAlerta(Window dono, List<Aviso> avisos, int duracao) {
            super(dono);
            setAlwaysOnTop(true);
            setFocusableWindowState(false); // não tira o foco a quem está a vender
            boolean algumHoje = avisos.stream().anyMatch(Aviso::urgente);
            cor = algumHoje ? VERMELHO : LARANJA;
            restante = duracao;

            JPanel raiz = new JPanel(new BorderLayout()) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = ComponentesWeb.suave(g);
                    g2.setColor(Color.WHITE);
                    g2.fillRect(0, 0, getWidth(), getHeight());
                    g2.setColor(aceso ? cor : ComponentesWeb.alfa(cor, 90));
                    g2.setStroke(new BasicStroke(4f));
                    g2.drawRect(2, 2, getWidth() - 4, getHeight() - 4);
                    g2.dispose();
                }
            };
            raiz.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

            // cabeçalho com a cor do alerta mais urgente
            JPanel cab = new JPanel(new BorderLayout(8, 0)) {
                @Override
                protected void paintComponent(Graphics g) {
                    g.setColor(aceso ? cor : cor.darker());
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            };
            cab.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));
            boolean haDevedores = avisos.stream().anyMatch(a -> a.atraso);
            JLabel titulo = new JLabel(haDevedores ? "Devedores e renovações" : "Renovação de mensalidades");
            titulo.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(16f));
            titulo.setForeground(Color.WHITE);
            long hoje = avisos.stream().filter(Aviso::urgente).count();
            JLabel resumo = new JLabel((hoje > 0 ? hoje + " urgente(s) · " : "") + avisos.size() + " cliente(s)");
            resumo.setFont(EstiloUI.FONTE.deriveFont(12.5f));
            resumo.setForeground(Color.WHITE);
            cab.add(titulo, BorderLayout.WEST);
            cab.add(resumo, BorderLayout.EAST);

            JPanel lista = new JPanel();
            lista.setLayout(new BoxLayout(lista, BoxLayout.Y_AXIS));
            lista.setBackground(Color.WHITE);
            for (Aviso a : avisos) {
                lista.add(linha(a));
            }
            JScrollPane rol = ComponentesWeb.rolagemVertical(lista);
            rol.setOpaque(true);
            rol.getViewport().setOpaque(true);
            rol.getViewport().setBackground(Color.WHITE);

            JLabel lbTempo = new JLabel();
            lbTempo.setFont(EstiloUI.FONTE.deriveFont(12f));
            lbTempo.setForeground(ComponentesWeb.TEXTO_SUAVE);
            JButton ver = new JButton("Ver estado dos clientes");
            JButton silenciar = new JButton("Silenciar");
            JButton fechar = new JButton("Fechar");
            ComponentesWeb.botao(silenciar, ComponentesWeb.Variante.SECUNDARIO, null, "Pára o alarme (o alerta continua no ecrã)");
            silenciar.addActionListener(e -> {
                pararAlarme();
                silenciar.setEnabled(false);
            });
            ComponentesWeb.botao(ver, ComponentesWeb.Variante.PRIMARIO, null, "Abre o estado das mensalidades de todos os clientes");
            ComponentesWeb.botao(fechar, ComponentesWeb.Variante.SECUNDARIO, null, "Fecha este alerta (volta no próximo intervalo)");
            ver.addActionListener(e -> {
                fechar();
                EstadoClientesDialog.abrir(dono, BDConexao.getInstancia());
            });
            fechar.addActionListener(e -> fechar());
            JPanel rodape = new JPanel(new BorderLayout());
            rodape.setBackground(Color.WHITE);
            rodape.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
            JPanel botoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
            botoes.setOpaque(false);
            botoes.add(ver);
            botoes.add(silenciar);
            botoes.add(fechar);
            rodape.add(lbTempo, BorderLayout.WEST);
            rodape.add(botoes, BorderLayout.EAST);

            raiz.add(cab, BorderLayout.NORTH);
            raiz.add(rol, BorderLayout.CENTER);
            raiz.add(rodape, BorderLayout.SOUTH);
            setContentPane(raiz);

            Rectangle util = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
            int largura = Math.min(500, util.width - 40);
            int altura = Math.min(130 + avisos.size() * 82, (int) (util.height * 0.6));
            setSize(new Dimension(largura, altura));
            setLocation(util.x + util.width - largura - 16, util.y + util.height - altura - 16);

            contagem = new Timer(1000, e -> {
                restante--;
                if (!alarmeATocar()) {
                    silenciar.setEnabled(false);
                }
                lbTempo.setText("Fecha em " + restante / 60 + ":" + String.format("%02d", restante % 60));
                if (restante <= 0) {
                    fechar();
                }
            });
            lbTempo.setText("Fecha em " + restante / 60 + ":" + String.format("%02d", restante % 60));
            contagem.start();
            // pisca nos primeiros 20 s para chamar a atenção (mais tempo se vence hoje)
            final int piscadelas = algumHoje ? 60 : 30;
            pisca = new Timer(650, null);
            pisca.addActionListener(new java.awt.event.ActionListener() {
                int n = 0;

                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    aceso = !aceso;
                    repaint();
                    if (++n >= piscadelas) {
                        aceso = true;
                        repaint();
                        pisca.stop();
                    }
                }
            });
            pisca.start();
        }

        private JPanel linha(Aviso a) {
            Color c = a.urgente() ? VERMELHO : LARANJA;
            JPanel p = new JPanel(new BorderLayout(10, 0)) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = ComponentesWeb.suave(g);
                    g2.setColor(ComponentesWeb.alfa(c, 26));
                    g2.fillRoundRect(4, 3, getWidth() - 8, getHeight() - 6, 10, 10);
                    g2.setColor(c);
                    g2.fillRoundRect(4, 3, 6, getHeight() - 6, 6, 6);
                    g2.dispose();
                }
            };
            p.setOpaque(false);
            p.setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 10));
            JLabel msg = new JLabel("<html>" + a.mensagem() + "</html>");
            msg.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(13f));
            msg.setForeground(a.urgente() ? VERMELHO.darker() : new Color(146, 64, 14));
            String det = a.servico + (a.vencimento == null ? "" : (a.atraso ? " · venceu a " : " · vence a ")
                    + a.vencimento.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM")))
                    + (a.telefone.isEmpty() ? "" : " · Tel. " + a.telefone);
            JLabel detalhe = new JLabel(det);
            detalhe.setFont(EstiloUI.FONTE.deriveFont(Font.PLAIN, 12f));
            detalhe.setForeground(ComponentesWeb.TEXTO_SUAVE);
            JPanel textos = new JPanel(new BorderLayout(0, 2));
            textos.setOpaque(false);
            textos.add(msg, BorderLayout.CENTER);
            textos.add(detalhe, BorderLayout.SOUTH);
            String txtEtiqueta = a.atraso ? "ATRASO" : a.hoje() ? "HOJE" : a.dias + (a.dias == 1 ? " dia" : " dias");
            JLabel etiqueta = new JLabel(txtEtiqueta, JLabel.CENTER);
            etiqueta.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(13f));
            etiqueta.setForeground(Color.WHITE);
            etiqueta.setOpaque(true);
            etiqueta.setBackground(c);
            etiqueta.setPreferredSize(new Dimension(70, 30));
            JPanel e = new JPanel(new java.awt.GridBagLayout());
            e.setOpaque(false);
            e.add(etiqueta);
            p.add(textos, BorderLayout.CENTER);
            p.add(e, BorderLayout.EAST);
            p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 96));
            p.setPreferredSize(new Dimension(440, 80));
            p.setAlignmentX(0f);
            return p;
        }

        void fechar() {
            pararAlarme();
            contagem.stop();
            pisca.stop();
            SwingUtilities.invokeLater(this::dispose);
            if (aberta == this) {
                aberta = null;
            }
        }
    }
}
