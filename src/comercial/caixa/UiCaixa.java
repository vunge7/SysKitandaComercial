package comercial.caixa;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Window;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import util.ui.ComponentesWeb;
import util.ui.EstiloUI;

/** Utilitários de interface dos formulários de caixa (abertura, fecho, reimpressão). */
public final class UiCaixa {

    private static final Locale PT = new Locale("pt", "PT");

    private UiCaixa() {
    }

    public static String moeda(BigDecimal v) {
        return EnvioFechoCaixa.moeda(v);
    }

    /** Lê "12.500,50", "12500.5", "12 500" ... null se vazio; ParseException se inválido. */
    public static BigDecimal valor(String texto) throws ParseException {
        if (texto == null) {
            return null;
        }
        String s = texto.trim().replace(" ", "").replace(" ", "").replace("Kz", "").replace("kz", "");
        if (s.isEmpty()) {
            return null;
        }
        if (s.contains(",")) {
            s = s.replace(".", "").replace(',', '.');
        } else if (s.indexOf('.') != s.lastIndexOf('.')) {
            s = s.replace(".", "");
        }
        try {
            BigDecimal v = new BigDecimal(s).setScale(2, BigDecimal.ROUND_HALF_UP);
            if (v.signum() < 0) {
                throw new ParseException(texto, 0);
            }
            return v;
        } catch (NumberFormatException e) {
            throw new ParseException(texto, 0);
        }
    }

    public static void tabela(JTable t, int[] larguras, Color cabecalho) {
        EstiloUI.estilizarTabela(t, true);
        t.setRowHeight(32);
        DefaultTableCellRenderer rc = new ComponentesWeb.RendererCabecalho();
        rc.setBackground(cabecalho);
        t.getTableHeader().setDefaultRenderer(rc);
        t.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        for (int i = 0; i < larguras.length && i < t.getColumnCount(); i++) {
            t.getColumnModel().getColumn(i).setPreferredWidth(larguras[i]);
        }
    }

    /** Renderer de valores à direita, com cor opcional por valor. */
    public static class RendererValor extends DefaultTableCellRenderer {

        private final boolean colorir;

        public RendererValor(boolean colorirSinal) {
            this.colorir = colorirSinal;
            setHorizontalAlignment(SwingConstants.RIGHT);
        }

        @Override
        public Component getTableCellRendererComponent(JTable t, Object v, boolean sel, boolean foco, int l, int c) {
            String texto = v instanceof BigDecimal ? moeda((BigDecimal) v) : v == null ? "" : v.toString();
            if (colorir && v instanceof BigDecimal && ((BigDecimal) v).signum() > 0) {
                texto = "+" + texto;
            }
            super.getTableCellRendererComponent(t, texto, sel, foco, l, c);
            setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 10));
            setFont(EstiloUI.FONTE.deriveFont(13.5f));
            if (!sel) {
                setBackground(l % 2 == 0 ? Color.WHITE : EstiloUI.LINHA_IMPAR);
                Color cor = ComponentesWeb.TEXTO;
                if (colorir && v instanceof BigDecimal) {
                    int s = ((BigDecimal) v).signum();
                    cor = s == 0 ? ComponentesWeb.VERDE : s > 0 ? ComponentesWeb.AZUL_VIVO : ComponentesWeb.VERMELHO;
                    setFont(EstiloUI.FONTE_NEGRITO.deriveFont(13.5f));
                } else if (v instanceof BigDecimal && ((BigDecimal) v).signum() == 0) {
                    cor = ComponentesWeb.CINZA;
                }
                setForeground(cor);
            }
            return this;
        }
    }

    // =====================================================================
    // TECLADO NUMÉRICO (ecrãs tácteis)
    // =====================================================================
    /**
     * Pede um valor com teclado no ecrã.
     *
     * @return o valor, null se cancelar; BigDecimal.ZERO negativo nunca
     */
    public static String teclado(Component pai, String titulo, String inicial) {
        Window dono = pai == null ? null : (pai instanceof Window ? (Window) pai : SwingUtilities.getWindowAncestor(pai));
        JDialog d = new JDialog(dono, titulo, java.awt.Dialog.ModalityType.APPLICATION_MODAL);
        JTextField visor = new JTextField(inicial == null ? "" : inicial);
        visor.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(26f));
        visor.setHorizontalAlignment(SwingConstants.RIGHT);
        visor.setPreferredSize(new Dimension(300, 54));
        final String[] resultado = {null};
        JPanel teclas = new JPanel(new GridLayout(5, 3, 8, 8));
        teclas.setOpaque(false);
        String[] rotulos = {"7", "8", "9", "4", "5", "6", "1", "2", "3", ",", "0", "⌫", "Limpar", "Cancelar", "OK"};
        for (String r : rotulos) {
            JButton b = new JButton(r);
            ComponentesWeb.Variante v = "OK".equals(r) ? ComponentesWeb.Variante.SUCESSO
                    : "Cancelar".equals(r) ? ComponentesWeb.Variante.PERIGO
                    : "Limpar".equals(r) ? ComponentesWeb.Variante.AVISO : ComponentesWeb.Variante.SECUNDARIO;
            ComponentesWeb.botao(b, v, null, null);
            b.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(r.length() == 1 ? 24f : 15f));
            b.setPreferredSize(new Dimension(96, 62));
            b.addActionListener(e -> {
                switch (r) {
                    case "OK":
                        resultado[0] = visor.getText();
                        d.dispose();
                        break;
                    case "Cancelar":
                        d.dispose();
                        break;
                    case "Limpar":
                        visor.setText("");
                        break;
                    case "⌫":
                        String s = visor.getText();
                        visor.setText(s.isEmpty() ? s : s.substring(0, s.length() - 1));
                        break;
                    case ",":
                        if (!visor.getText().contains(",")) {
                            visor.setText(visor.getText() + ",");
                        }
                        break;
                    default:
                        visor.setText(visor.getText() + r);
                }
            });
            teclas.add(b);
        }
        visor.addActionListener(e -> {
            resultado[0] = visor.getText();
            d.dispose();
        });
        JPanel p = new JPanel(new BorderLayout(0, 12));
        p.setBackground(ComponentesWeb.FUNDO_PAGINA);
        p.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));
        p.add(visor, BorderLayout.NORTH);
        p.add(teclas, BorderLayout.CENTER);
        d.setContentPane(p);
        d.pack();
        d.setLocationRelativeTo(dono);
        d.setVisible(true);
        return resultado[0];
    }

    static DecimalFormat formatoSimples() {
        return new DecimalFormat("0.##", DecimalFormatSymbols.getInstance(PT));
    }
}
