package visao.cliente;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.text.Normalizer;
import java.util.concurrent.ExecutionException;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import util.MetodosUtil;
import util.ValidadorNifAGT;
import util.ValidadorNifAGT.Resultado;
import util.ui.ComponentesWeb;
import util.ui.EstiloUI;
import util.ui.FormularioWeb;

/**
 * Validação do NIF na AGT para os formulários de cliente com o layout web
 * ({@code util.ui}).
 * <ul>
 * <li>botão «Validar na AGT» ao lado do NIF (e Enter / saída do campo);</li>
 * <li>valida sozinho, em segundo plano, quando o NIF fica com um formato
 * completo (ao escrever ou ao abrir um cliente);</li>
 * <li>linha de estado com o resultado (nome na AGT, tipo, IVA, situação);</li>
 * <li>preenche os campos vazios com os dados da AGT e oferece «Usar dados
 * da AGT» para os substituir;</li>
 * <li>{@link #confirmarAntesDeGravar(Component)}: regra a chamar antes de
 * salvar ou alterar.</li>
 * </ul>
 */
public final class ValidacaoNifWeb {

    private static final int ESPERA_AUTOMATICA_MS = 700;

    private final JTextField txtNif;
    private final JComboBox<?> cmbPais;
    private final JTextField txtNome;
    private final JTextField txtMorada;
    private final JTextField txtTelefone;
    private final JTextField txtEmail;

    private final JButton btnValidar = new JButton("Validar na AGT");
    private final JButton btnUsarDados = new JButton("Usar dados da AGT");
    private final ComponentesWeb.Etiqueta etiqueta = new ComponentesWeb.Etiqueta("NIF por validar", ComponentesWeb.CINZA);
    private final JLabel lbDetalhe = new JLabel();
    private final Timer validacaoAutomatica;

    private Resultado resultado;
    private String chaveValidada;
    private boolean aValidar;

    public ValidacaoNifWeb(JTextField txtNif, JComboBox<?> cmbPais, JTextField txtNome,
            JTextField txtMorada, JTextField txtTelefone, JTextField txtEmail) {
        this.txtNif = txtNif;
        this.cmbPais = cmbPais;
        this.txtNome = txtNome;
        this.txtMorada = txtMorada;
        this.txtTelefone = txtTelefone;
        this.txtEmail = txtEmail;

        lbDetalhe.setFont(EstiloUI.FONTE.deriveFont(12f));
        lbDetalhe.setForeground(ComponentesWeb.TEXTO_SUAVE);
        btnUsarDados.setVisible(false);

        validacaoAutomatica = new Timer(ESPERA_AUTOMATICA_MS, e -> {
            String nif = ValidadorNifAGT.normalizar(txtNif.getText());
            if (!nif.isEmpty() && ValidadorNifAGT.validarFormato(nif, paisIso()) == null) {
                validar(null);
            }
        });
        validacaoAutomatica.setRepeats(false);

        txtNif.setToolTipText("<html>Formatos aceites pela AGT:<br>"
                + "• Empresa / NIF da AGT: 10 dígitos (ex.: 5417033855)<br>"
                + "• Pessoa singular: nº do BI (ex.: 000067259ME013)<br>"
                + "• Consumidor final: 999999999</html>");
        txtNif.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                aoAlterar();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                aoAlterar();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });
        txtNif.addActionListener(e -> validar(null));
        txtNif.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                if (!e.isTemporary() && resultado == null && !aValidar
                        && !ValidadorNifAGT.normalizar(txtNif.getText()).isEmpty()) {
                    validar(null);
                }
            }
        });
        cmbPais.addActionListener(e -> aoAlterar());
        btnValidar.addActionListener(e -> validar(null));
        btnUsarDados.addActionListener(e -> usarDadosAGT(true));
        porValidar();
    }

    // ------------------------------------------------------------------
    // Componentes para o layout
    // ------------------------------------------------------------------
    /** O campo do NIF com o botão «Validar na AGT» à direita. */
    public JPanel campoNif() {
        ComponentesWeb.botao(btnValidar, ComponentesWeb.Variante.PRIMARIO, null,
                "Confirmar o NIF no serviço da AGT (Enter no NIF)");
        btnValidar.setPreferredSize(new Dimension(btnValidar.getPreferredSize().width, FormularioWeb.ALTURA_CAMPO));
        JPanel p = ComponentesWeb.transparente(new BorderLayout(6, 0));
        p.add(txtNif, BorderLayout.CENTER);
        p.add(btnValidar, BorderLayout.EAST);
        return p;
    }

    /** Linha com o estado da validação e o botão «Usar dados da AGT». */
    public JPanel painelEstado() {
        ComponentesWeb.botao(btnUsarDados, ComponentesWeb.Variante.SUCESSO, null,
                "Preencher o nome, o endereço e os contactos com os dados registados na AGT");
        JPanel esquerda = ComponentesWeb.transparente(new FlowLayout(FlowLayout.LEFT, 0, 0));
        esquerda.add(etiqueta);
        JPanel p = ComponentesWeb.transparente(new BorderLayout(10, 0));
        p.add(esquerda, BorderLayout.WEST);
        p.add(lbDetalhe, BorderLayout.CENTER);
        p.add(btnUsarDados, BorderLayout.EAST);
        return p;
    }

    // ------------------------------------------------------------------
    // Validação
    // ------------------------------------------------------------------
    private String paisIso() {
        return ConfirmacaoNif.isoDoPais(cmbPais.getSelectedItem());
    }

    private String chaveActual() {
        return ValidadorNifAGT.normalizar(txtNif.getText()) + "|" + paisIso();
    }

    private void aoAlterar() {
        if (resultado != null && chaveActual().equals(chaveValidada)) {
            return;
        }
        resultado = null;
        chaveValidada = null;
        if (!aValidar) {
            porValidar();
        }
        validacaoAutomatica.restart();
    }

    /** Valida em segundo plano e depois executa {@code depois} (pode ser null). */
    public void validar(final Runnable depois) {
        validacaoAutomatica.stop();
        if (aValidar) {
            return;
        }
        final String nif = ValidadorNifAGT.normalizar(txtNif.getText());
        final String iso = paisIso();
        final String chave = nif + "|" + iso;
        if (resultado != null && chave.equals(chaveValidada)) {
            if (depois != null) {
                depois.run();
            }
            return;
        }
        if (nif.isEmpty()) {
            porValidar();
            return;
        }
        if (ValidadorNifAGT.validarFormato(nif, iso) != null) {
            aplicar(ValidadorNifAGT.validar(nif, iso)); // sem rede
            if (depois != null) {
                depois.run();
            }
            return;
        }

        aValidar = true;
        btnValidar.setEnabled(false);
        mostrar(ComponentesWeb.AZUL_VIVO, "A consultar a AGT…", "a confirmar o registo do contribuinte", false);
        new SwingWorker<Resultado, Void>() {
            @Override
            protected Resultado doInBackground() {
                return ValidadorNifAGT.validar(nif, iso);
            }

            @Override
            protected void done() {
                aValidar = false;
                btnValidar.setEnabled(true);
                if (!chave.equals(chaveActual())) {
                    porValidar();
                    validacaoAutomatica.restart();
                    return;
                }
                try {
                    aplicar(get());
                } catch (InterruptedException | ExecutionException e) {
                    porValidar();
                    return;
                }
                if (depois != null) {
                    depois.run();
                }
            }
        }.execute();
    }

    private void aplicar(Resultado r) {
        if (!r.getNif().isEmpty() && !txtNif.getText().equals(r.getNif())) {
            txtNif.setText(r.getNif()); // NIF normalizado
        }
        resultado = r;
        chaveValidada = chaveActual();
        mostrarResultado(r);
        if (r.isConfirmado() && r.getTipo() != ValidadorNifAGT.Tipo.CONSUMIDOR_FINAL) {
            usarDadosAGT(false);
        }
    }

    /**
     * Regra a chamar antes de salvar ou alterar: garante que o NIF actual
     * foi validado (valida já, se preciso) e aplica a política da AGT.
     *
     * @return true se pode gravar; o campo NIF fica normalizado
     */
    public boolean confirmarAntesDeGravar(Component pai) {
        validacaoAutomatica.stop();
        Resultado r = resultado;
        if (r == null || !chaveActual().equals(chaveValidada)) {
            Cursor anterior = pai == null ? null : pai.getCursor();
            if (pai != null) {
                pai.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            }
            try {
                r = ValidadorNifAGT.validar(txtNif.getText(), paisIso());
            } finally {
                if (pai != null) {
                    pai.setCursor(anterior);
                }
            }
            aplicar(r);
        }
        if (!ConfirmacaoNif.permiteGuardar(pai, r)) {
            txtNif.requestFocusInWindow();
            txtNif.selectAll();
            return false;
        }
        if (r.isConfirmado() && r.getTipo() != ValidadorNifAGT.Tipo.CONSUMIDOR_FINAL && r.getNome() != null
                && !mesmoNome(r.getNome(), txtNome.getText())) {
            int opcao = JOptionPane.showOptionDialog(pai,
                    "O nome do cliente não corresponde ao contribuinte registado na AGT com este NIF:\n\n"
                    + "   No formulário:  " + txtNome.getText().trim() + "\n"
                    + "   Na AGT:            " + r.getNome() + "\n\n"
                    + "Confirme se o NIF pertence a este cliente.",
                    "Nome diferente do registado na AGT", JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.WARNING_MESSAGE, null,
                    new Object[]{"Usar o nome da AGT", "Manter o nome", "Cancelar"}, "Usar o nome da AGT");
            if (opcao == 0) {
                txtNome.setText(MetodosUtil.normalizarCampo(r.getNome()));
            } else if (opcao != 1) {
                return false;
            }
        }
        if (!r.getNif().isEmpty()) {
            txtNif.setText(r.getNif());
        }
        return true;
    }

    // ------------------------------------------------------------------
    // Dados da AGT
    // ------------------------------------------------------------------
    private void usarDadosAGT(boolean substituir) {
        Resultado r = resultado;
        if (r == null || !r.isConfirmado()) {
            return;
        }
        preencher(txtNome, r.getNome() == null ? null : MetodosUtil.normalizarCampo(r.getNome()), substituir);
        preencher(txtMorada, r.getMorada(), substituir);
        preencher(txtTelefone, r.getTelefone(), substituir);
        preencher(txtEmail, r.getEmail(), substituir);
    }

    private static void preencher(JTextField campo, String valor, boolean substituir) {
        if (campo == null || valor == null || valor.trim().isEmpty()) {
            return;
        }
        String actual = campo.getText().trim();
        if (substituir || actual.isEmpty() || "null".equalsIgnoreCase(actual)) {
            campo.setText(valor.trim());
            campo.setCaretPosition(0);
        }
    }

    /** Nomes iguais ignorando maiúsculas, acentos e pontuação (um pode conter o outro). */
    private static boolean mesmoNome(String a, String b) {
        String x = simplificar(a);
        String y = simplificar(b);
        return !x.isEmpty() && !y.isEmpty() && (x.contains(y) || y.contains(x));
    }

    private static String simplificar(String s) {
        return Normalizer.normalize(s == null ? "" : s, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "").toUpperCase().replaceAll("[^A-Z0-9]", "");
    }

    // ------------------------------------------------------------------
    // Estado visual
    // ------------------------------------------------------------------
    private void porValidar() {
        mostrar(ComponentesWeb.CINZA, "NIF por validar",
                "o NIF é confirmado na AGT antes de salvar ou alterar", false);
    }

    private void mostrarResultado(Resultado r) {
        switch (r.getEstado()) {
            case VALIDO:
                if (r.getTipo() == ValidadorNifAGT.Tipo.CONSUMIDOR_FINAL) {
                    mostrar(ComponentesWeb.VERDE, "Consumidor final",
                            "NIF genérico " + ValidadorNifAGT.NIF_CONSUMIDOR_FINAL + " definido pela AGT", false);
                } else {
                    mostrar(r.isActivo() ? ComponentesWeb.VERDE : ComponentesWeb.LARANJA,
                            r.isActivo() ? "NIF válido na AGT" : "NIF INACTIVO na AGT",
                            "<b>" + html(r.getNome() == null ? "—" : r.getNome()) + "</b> · "
                            + r.getTipo().getDescricao()
                            + (r.getRegimeIva() == null ? "" : " · IVA: " + html(regime(r.getRegimeIva())))
                            + " · " + (r.isActivo() ? "Activo" : "<b>Inactivo</b>"),
                            r.getNome() != null);
                }
                break;
            case ESTRANGEIRO:
                mostrar(ComponentesWeb.CIANO, "Contribuinte estrangeiro",
                        "o NIF de outro país não é verificável na AGT", false);
                break;
            case SEM_LIGACAO:
                mostrar(ComponentesWeb.LARANJA, "Sem ligação à AGT",
                        "formato correcto, mas o registo não pôde ser confirmado", false);
                break;
            case NAO_REGISTADO:
                mostrar(ComponentesWeb.VERMELHO, "NIF não registado na AGT",
                        "confirme o número junto do cliente (cartão de contribuinte ou BI)", false);
                break;
            default:
                mostrar(ComponentesWeb.VERMELHO, "NIF inválido", html(r.getMensagem()).replace("\n", "<br>"), false);
                break;
        }
    }

    private void mostrar(Color cor, String titulo, String detalheHtml, boolean usarDados) {
        etiqueta.setText(titulo);
        etiqueta.setCor(cor);
        lbDetalhe.setText("<html>" + detalheHtml + "</html>");
        btnUsarDados.setVisible(usarDados);
        Component raiz = SwingUtilities.getRoot(lbDetalhe);
        if (raiz != null) {
            lbDetalhe.revalidate();
            raiz.repaint();
        }
    }

    private static String regime(String codigo) {
        switch (codigo.toUpperCase()) {
            case "GNAD":
                return "Regime geral";
            case "SIMP":
                return "Regime simplificado";
            case "EXCL":
                return "Regime de exclusão";
            default:
                return codigo;
        }
    }

    private static String html(String s) {
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }
}
