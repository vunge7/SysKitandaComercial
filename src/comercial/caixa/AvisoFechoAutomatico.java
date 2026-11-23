package comercial.caixa;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import util.BDConexao;
import util.ui.FormularioWeb;

/**
 * Aviso "Faltam x minutos para o fecho automático", mostrado na última hora antes
 * do fecho (60, 45, 30 e 15 minutos antes) por FechoGeralAgendador. Fica 5 minutos
 * no ecrã e fecha sozinho.
 *
 * OK: fecha o aviso. Reagendar Hora Fecho: muda a hora do fecho só para hoje
 * (ex.: o restaurante ainda tem muitos clientes); amanhã volta à hora configurada
 * em hora_fecho_geral. Pedido do utilizador em 2026-11-19.
 */
final class AvisoFechoAutomatico {

    private static final int DURACAO_MS = 5 * 60000;
    private static final SimpleDateFormat HM = new SimpleDateFormat("HH:mm");
    private static JDialog aberto;

    private AvisoFechoAutomatico() {
    }

    static void mostrar(Date fim) {
        SwingUtilities.invokeLater(() -> criar(fim));
    }

    private static void criar(Date fim) {
        if (aberto != null) {
            aberto.dispose(); // fica só o aviso mais recente
        }
        JDialog d = new JDialog((java.awt.Frame) null, "Fecho automático", false);
        d.setAlwaysOnTop(true);
        d.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JLabel titulo = new JLabel();
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 18f));
        titulo.setForeground(new Color(0xB4, 0x3C, 0x00));
        JLabel hora = new JLabel("Hora do fecho automático: " + HM.format(fim));
        Runnable actualizar = () -> {
            int faltam = (int) Math.max(0, Math.ceil((fim.getTime() - System.currentTimeMillis()) / 60000.0));
            titulo.setText("Faltam " + faltam + (faltam == 1 ? " minuto" : " minutos") + " para o fecho automático");
        };
        actualizar.run();

        JPanel texto = new JPanel(new BorderLayout(0, 6));
        texto.setOpaque(false);
        texto.add(titulo, BorderLayout.NORTH);
        texto.add(hora, BorderLayout.CENTER);

        // reagendar: nova hora para hoje
        JSpinner spHora = new JSpinner(new SpinnerDateModel());
        spHora.setEditor(new JSpinner.DateEditor(spHora, "HH:mm"));
        FormularioWeb.spinner(spHora);
        Calendar k = Calendar.getInstance();
        k.setTime(fim);
        k.add(Calendar.HOUR_OF_DAY, 1); // sugestão: uma hora depois
        spHora.setValue(k.getTime());

        JButton ok = new JButton("OK");
        FormularioWeb.botaoSalvar(ok, "Fecha este aviso");
        ok.addActionListener(e -> d.dispose());

        JButton reagendar = new JButton("Reagendar Hora Fecho");
        FormularioWeb.botaoAlterar(reagendar, "Muda a hora do fecho automático só para hoje");
        reagendar.addActionListener(e -> {
            Calendar n = Calendar.getInstance();
            n.setTime((Date) spHora.getValue());
            int h = n.get(Calendar.HOUR_OF_DAY), m = n.get(Calendar.MINUTE);
            Calendar novo = Calendar.getInstance();
            novo.set(Calendar.HOUR_OF_DAY, h);
            novo.set(Calendar.MINUTE, m);
            novo.set(Calendar.SECOND, 0);
            novo.set(Calendar.MILLISECOND, 0);
            if (!novo.getTime().after(new Date())) {
                JOptionPane.showMessageDialog(d, "Escolha uma hora de hoje, depois de agora.", "Reagendar Hora Fecho",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            try (Connection cn = BDConexao.novaLigacao()) {
                FechoGeral.reagendarHoje(cn, h, m);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(d, "Falha ao reagendar: " + ex.getMessage(), "Reagendar Hora Fecho",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            System.out.println("[FechoGeral] fecho automático de hoje reagendado para " + HM.format(novo.getTime()));
            d.dispose();
            JOptionPane.showMessageDialog(null, "Fecho automático de hoje reagendado para " + HM.format(novo.getTime()) + ".",
                    "Fecho automático", JOptionPane.INFORMATION_MESSAGE);
        });

        JPanel linhaReagendar = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        linhaReagendar.setOpaque(false);
        linhaReagendar.add(new JLabel("Nova hora:"));
        linhaReagendar.add(spHora);
        linhaReagendar.add(reagendar);

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        botoes.setOpaque(false);
        botoes.add(ok);

        JPanel sul = new JPanel(new BorderLayout(0, 10));
        sul.setOpaque(false);
        sul.add(linhaReagendar, BorderLayout.NORTH);
        sul.add(botoes, BorderLayout.SOUTH);

        JPanel conteudo = new JPanel(new BorderLayout(0, 14));
        conteudo.setBackground(Color.WHITE);
        conteudo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 6, 0, 0, new Color(0xE0, 0x7A, 0x00)),
                BorderFactory.createEmptyBorder(16, 18, 16, 18)));
        conteudo.add(texto, BorderLayout.CENTER);
        conteudo.add(sul, BorderLayout.SOUTH);
        d.setContentPane(conteudo);

        // actualiza a contagem e fecha sozinho ao fim de 5 minutos
        Timer contagem = new Timer(15000, e -> actualizar.run());
        Timer fechar = new Timer(DURACAO_MS, e -> d.dispose());
        fechar.setRepeats(false);
        d.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                contagem.stop();
                fechar.stop();
                if (aberto == d) {
                    aberto = null;
                }
            }
        });

        d.pack();
        d.setLocationRelativeTo(null);
        aberto = d;
        d.setVisible(true);
        java.awt.Toolkit.getDefaultToolkit().beep();
        contagem.start();
        fechar.start();
    }
}
