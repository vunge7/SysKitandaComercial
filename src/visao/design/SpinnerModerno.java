package visao.design;

import java.awt.BasicStroke;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SwingConstants;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.plaf.basic.BasicFormattedTextFieldUI;
import javax.swing.plaf.basic.BasicSpinnerUI;

/**
 * Spinner com o aspecto dos campos do kit: fundo suave do negócio, cantos
 * arredondados, anel de foco e setas minimalistas. Continua a ser um
 * JSpinner normal (modelo, valor e eventos não mudam).
 */
public class SpinnerModerno extends JSpinner {

    private final FocusAdapter repintar = new FocusAdapter() {
        @Override
        public void focusGained(FocusEvent e) {
            repaint();
        }

        @Override
        public void focusLost(FocusEvent e) {
            repaint();
        }
    };

    public SpinnerModerno() {
        super();
        setOpaque(false);
        EfeitoHover.instalar(this);
        estilizarEditor(getEditor());
    }

    @Override
    public void updateUI() {
        setUI(new UI());
    }

    /** O editor é recriado sempre que o modelo muda (setModel). */
    @Override
    protected JComponent createEditor(SpinnerModel modelo) {
        JComponent editor = super.createEditor(modelo);
        estilizarEditor(editor);
        return editor;
    }

    public JFormattedTextField getCampo() {
        JComponent editor = getEditor();
        return editor instanceof DefaultEditor ? ((DefaultEditor) editor).getTextField() : null;
    }

    private void estilizarEditor(JComponent editor) {
        if (!(editor instanceof DefaultEditor)) {
            return;
        }
        editor.setOpaque(false);
        editor.setBorder(BorderFactory.createEmptyBorder());
        JFormattedTextField campo = ((DefaultEditor) editor).getTextField();
        campo.setUI(new BasicFormattedTextFieldUI());
        campo.setOpaque(false);
        campo.setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 2));
        campo.setFont(Tema.CAMPO);
        campo.setForeground(Tema.TEXTO);
        campo.setCaretColor(Tema.TEXTO);
        campo.setSelectionColor(Tema.selecaoTexto());
        campo.setSelectedTextColor(Tema.TEXTO);
        campo.setDisabledTextColor(Tema.TEXTO_SUAVE);
        campo.setHorizontalAlignment(SwingConstants.RIGHT);
        campo.removeFocusListener(repintar);
        campo.addFocusListener(repintar);
        // o anel de foco acende quando o foco está no campo de texto interno
        setBorder(new BordaCampo(2, 8, 2, campo));
    }

    @Override
    protected void paintComponent(Graphics g) {
        BordaCampo.pintarFundo(g, this, isEnabled() ? Tema.fundoCampo() : Tema.CAMPO_INACTIVO);
    }

    private static class UI extends BasicSpinnerUI {

        @Override
        protected Component createNextButton() {
            Component c = seta(true);
            installNextButtonListeners(c);
            return c;
        }

        @Override
        protected Component createPreviousButton() {
            Component c = seta(false);
            installPreviousButtonListeners(c);
            return c;
        }

        private static JButton seta(final boolean cima) {
            JButton botao = new JButton() {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = Formularios.suavizar(g);
                    boolean realce = isEnabled() && getModel().isRollover();
                    g2.setColor(!isEnabled() ? Tema.BORDA_CAMPO : realce ? Tema.PRIMARIA : Tema.ROTULO);
                    g2.setStroke(new BasicStroke(1.6f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    int cx = getWidth() / 2 - 2;
                    int cy = getHeight() / 2 + (cima ? 1 : -1);
                    int d = cima ? -2 : 2;
                    g2.drawLine(cx - 3, cy - d, cx, cy + d);
                    g2.drawLine(cx, cy + d, cx + 3, cy - d);
                    g2.dispose();
                }
            };
            botao.setUI(new BasicButtonUI());
            botao.setOpaque(false);
            botao.setContentAreaFilled(false);
            botao.setBorder(BorderFactory.createEmptyBorder());
            botao.setFocusable(false);
            botao.setRolloverEnabled(true);
            botao.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            botao.setPreferredSize(new java.awt.Dimension(20, 12));
            botao.setName(cima ? "Spinner.nextButton" : "Spinner.previousButton");
            return botao;
        }
    }
}
