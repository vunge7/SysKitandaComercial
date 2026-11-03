package visao.design;

import com.toedter.calendar.JCalendar;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * Campo de data (dd/MM/aaaa) com o aspecto dos restantes campos e um botão
 * que abre um calendário. Aceita escrever a data directamente.
 */
public class CampoData extends JPanel {

    private static final String FORMATO = "dd/MM/yyyy";

    private final CampoModerno campo = new CampoModerno();
    private final BotaoModerno botao = new BotaoModerno("", Tema.PRIMARIA, true);
    private final List<Runnable> ouvintes = new ArrayList<>();
    private boolean aDefinir = false;

    public CampoData() {
        super(new BorderLayout(6, 0));
        setOpaque(false);
        campo.setFont(Tema.CAMPO);
        campo.setToolTipText("Data no formato dd/mm/aaaa");
        Formularios.dimensionar(campo, 110, Tema.ALTURA_CAMPO);

        botao.setIcon(new IconeCalendario());
        botao.setToolTipText("Escolher no calendário");
        botao.setBorder(BorderFactory.createEmptyBorder(0, 10, 2, 10));
        Formularios.dimensionar(botao, 40, Tema.ALTURA_CAMPO);
        botao.addActionListener(e -> abrirCalendario());

        campo.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                avisar();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                avisar();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                avisar();
            }
        });

        add(campo, BorderLayout.CENTER);
        add(botao, BorderLayout.EAST);
    }

    /** Data escrita no campo, ou null se estiver vazio ou inválido. */
    public Date getDate() {
        String texto = campo.getText().trim();
        if (texto.isEmpty()) {
            return null;
        }
        SimpleDateFormat formato = new SimpleDateFormat(FORMATO);
        formato.setLenient(false);
        try {
            return formato.parse(texto);
        } catch (ParseException e) {
            return null;
        }
    }

    /** true se o campo tem texto que não é uma data válida. */
    public boolean isInvalida() {
        return !campo.getText().trim().isEmpty() && getDate() == null;
    }

    public void setDate(Date data) {
        aDefinir = true;
        try {
            campo.setText(data == null ? "" : new SimpleDateFormat(FORMATO).format(data));
        } finally {
            aDefinir = false;
        }
    }

    /**
     * Chamado quando o utilizador muda a data (a escrever ou no calendário),
     * mas não quando é alterada por {@link #setDate(Date)}.
     */
    public void aoAlterarPeloUtilizador(Runnable ouvinte) {
        ouvintes.add(ouvinte);
    }

    public CampoModerno getCampo() {
        return campo;
    }

    /** O foco vai para o texto da data (ex.: ao validar um campo em falta). */
    @Override
    public void requestFocus() {
        campo.requestFocus();
    }

    @Override
    public boolean requestFocusInWindow() {
        return campo.requestFocusInWindow();
    }

    @Override
    public void setEnabled(boolean activo) {
        super.setEnabled(activo);
        campo.setEnabled(activo);
        botao.setEnabled(activo);
    }

    private void avisar() {
        if (!aDefinir) {
            for (Runnable ouvinte : ouvintes) {
                ouvinte.run();
            }
        }
    }

    private void abrirCalendario() {
        final JPopupMenu popup = new JPopupMenu();
        popup.setBorder(BorderFactory.createLineBorder(Tema.BORDA_CAMPO));
        final JCalendar calendario = new JCalendar(getDate() != null ? getDate() : new Date());
        calendario.setWeekOfYearVisible(false);
        // "day" só é disparado ao clicar num dia (como no JDateChooser)
        calendario.getDayChooser().addPropertyChangeListener("day", e -> {
            if (popup.isVisible()) {
                popup.setVisible(false);
                setDate(calendario.getDate());
                for (Runnable ouvinte : ouvintes) {
                    ouvinte.run();
                }
                campo.requestFocusInWindow();
            }
        });
        popup.add(calendario);
        popup.show(this, 0, getHeight());
    }

    /** Pequeno calendário desenhado (independente de imagens). */
    private static class IconeCalendario implements Icon {

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = Formularios.suavizar(g);
            g2.setColor(c.isEnabled() ? Tema.PRIMARIA : Tema.BORDA_CAMPO);
            g2.setStroke(new BasicStroke(1.6f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.drawRoundRect(x + 1, y + 3, 14, 12, 3, 3);
            g2.drawLine(x + 1, y + 7, x + 15, y + 7);
            g2.drawLine(x + 5, y + 1, x + 5, y + 4);
            g2.drawLine(x + 11, y + 1, x + 11, y + 4);
            g2.fillRect(x + 4, y + 10, 2, 2);
            g2.fillRect(x + 8, y + 10, 2, 2);
            g2.dispose();
        }

        @Override
        public int getIconWidth() {
            return 17;
        }

        @Override
        public int getIconHeight() {
            return 17;
        }
    }
}
