package comercial.caixa;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Window;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import util.BDConexao;
import util.ui.ComponentesWeb;
import util.ui.EstiloUI;
import util.ui.FormularioWeb;

/**
 * Abertura de caixa (substitui o CaixaAberturaVisao nos menus): modal, para o
 * pagamento que a pediu continuar logo a seguir; mostra o último fecho do
 * utilizador; teclado no ecrã nos postos tácteis. A gravação é feita numa
 * transacção com bloqueio (FechoCaixa.abrir): dois cliques não abrem duas caixas.
 * 2026-11-14.
 */
public final class AberturaCaixaWeb extends JDialog {

    private static final SimpleDateFormat DH = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    private final int idUser;
    private final boolean touch;
    private final JTextField txtFundo = new JTextField("0,00");
    private boolean aberta;
    private String nome = "", perfil = "";

    private AberturaCaixaWeb(Window dono, int idUser, boolean touch) {
        super(dono, "Abertura de caixa", Dialog.ModalityType.APPLICATION_MODAL);
        this.idUser = idUser;
        this.touch = touch;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        montar();
    }

    /**
     * Mostra o formulário de abertura.
     *
     * @return true se ficou uma caixa aberta (aberta agora ou já estava)
     */
    public static boolean abrir(Component pai, int idUser, boolean touch) {
        Window dono = pai == null ? null : (pai instanceof Window ? (Window) pai : SwingUtilities.getWindowAncestor(pai));
        try (Connection cn = BDConexao.novaLigacao()) {
            Integer jaAberta = FechoCaixa.caixaAberta(cn, idUser);
            if (jaAberta != null) {
                JOptionPane.showMessageDialog(dono, "Já tem a caixa Nº " + jaAberta + " aberta.\n"
                        + "Pode continuar a trabalhar; no fim do turno faça o fecho.", "Abertura de caixa",
                        JOptionPane.INFORMATION_MESSAGE, new IconeCaixa(IconeCaixa.Tipo.ABERTURA, 48));
                return true;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(dono, "Não foi possível verificar a caixa: " + e.getMessage(), "Abertura de caixa",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        AberturaCaixaWeb d = new AberturaCaixaWeb(dono, idUser, touch);
        d.setVisible(true);
        return d.aberta;
    }

    private void montar() {
        String ultimo = "Ainda não fez nenhum fecho de caixa.";
        try (Connection cn = BDConexao.novaLigacao()) {
            String[] u = FechoCaixa.utilizador(cn, idUser);
            nome = u[0];
            perfil = u[1];
            try (PreparedStatement ps = cn.prepareStatement("SELECT numero_fecho, data_fecho, total_vendas FROM caixa"
                    + " WHERE cod_usuario_abertura = ? AND data_fecho IS NOT NULL ORDER BY pk_caixa DESC LIMIT 1")) {
                ps.setInt(1, idUser);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        ultimo = "Último fecho: Nº " + rs.getInt(1) + " em " + DH.format(rs.getTimestamp(2));
                    }
                }
            }
        } catch (Exception e) {
            nome = "Utilizador " + idUser;
        }

        JPanel cab = FormularioWeb.cabecalho("Abertura de caixa", nome + (perfil.isEmpty() ? "" : " · " + perfil), null);
        IconeCaixa.noCabecalho(cab, IconeCaixa.Tipo.ABERTURA);

        txtFundo.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(22f));
        txtFundo.setHorizontalAlignment(JTextField.RIGHT);
        FormularioWeb.campoTexto(txtFundo);
        txtFundo.setPreferredSize(new java.awt.Dimension(260, 46));
        txtFundo.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                SwingUtilities.invokeLater(txtFundo::selectAll);
            }
        });
        txtFundo.addActionListener(e -> confirmar());
        JButton teclado = new JButton("Teclado");
        ComponentesWeb.botao(teclado, ComponentesWeb.Variante.SECUNDARIO, null, "Teclado no ecrã");
        teclado.addActionListener(e -> usarTeclado());
        if (touch) {
            txtFundo.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    usarTeclado();
                }
            });
        }
        JPanel linhaFundo = ComponentesWeb.transparente(new BorderLayout(8, 0));
        linhaFundo.add(txtFundo, BorderLayout.CENTER);
        linhaFundo.add(teclado, BorderLayout.EAST);

        JLabel info = new JLabel("<html><b>A abertura é obrigatória</b> para receber pagamentos (Factura/Recibo e Recibos)."
                + "<br>No fim do turno, antes de sair, faça o <b>fecho de caixa</b>: é impresso e enviado aos responsáveis."
                + "<br><br>" + ultimo + "</html>");
        info.setFont(EstiloUI.FONTE.deriveFont(13f));
        info.setForeground(ComponentesWeb.TEXTO);

        JPanel corpo = ComponentesWeb.transparente(new BorderLayout(0, 14));
        corpo.add(ComponentesWeb.campo("Fundo de caixa — numerário com que começa (Kz)", linhaFundo), BorderLayout.NORTH);
        corpo.add(info, BorderLayout.CENTER);
        ComponentesWeb.Cartao cartao = FormularioWeb.cartao("Nova caixa", ComponentesWeb.VERDE, corpo);

        JButton abrir = new JButton("Abrir caixa");
        ComponentesWeb.botao(abrir, ComponentesWeb.Variante.SUCESSO, null, "Abre a caixa e começa o turno");
        abrir.setIcon(new IconeCaixa(IconeCaixa.Tipo.ABERTURA, 22));
        abrir.addActionListener(e -> confirmar());
        JButton sair = new JButton("Sair");
        FormularioWeb.botaoSair(sair, "Fechar sem abrir a caixa");
        sair.addActionListener(e -> dispose());
        JComponent accoes = FormularioWeb.barraAccoes(FormularioWeb.dica("Enter abre a caixa"), abrir, sair);

        JPanel conteudo = ComponentesWeb.transparente(new BorderLayout());
        conteudo.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
        conteudo.add(cartao, BorderLayout.NORTH);
        FormularioWeb.montar(this, cab, conteudo, accoes, "Abertura de caixa · " + nome);
        getRootPane().setDefaultButton(abrir);
        FormularioWeb.ajustar(this, 640, 470, 520, 420);
    }

    private void usarTeclado() {
        String v = UiCaixa.teclado(this, "Fundo de caixa", "");
        if (v != null) {
            txtFundo.setText(v);
        }
    }

    private void confirmar() {
        java.math.BigDecimal fundo;
        try {
            fundo = UiCaixa.valor(txtFundo.getText());
        } catch (java.text.ParseException e) {
            JOptionPane.showMessageDialog(this, "Valor inválido: " + txtFundo.getText(), "Abertura de caixa", JOptionPane.WARNING_MESSAGE);
            txtFundo.requestFocus();
            return;
        }
        if (fundo == null) {
            fundo = java.math.BigDecimal.ZERO;
        }
        // barra de progresso (pintada numa thread própria) enquanto a abertura é gravada
        util.ui.ProgressoEmissao.iniciar("Abertura de caixa", "A abrir a caixa de " + nome + "...", new String[]{
            "A registar a abertura e o fundo de caixa.",
            "Por favor aguarde, não feche o sistema."});
        try (Connection cn = BDConexao.novaLigacao()) {
            int pk = FechoCaixa.abrir(cn, idUser, nome, fundo.doubleValue());
            aberta = true;
            util.ui.ProgressoEmissao.concluirAgora();
            JOptionPane.showMessageDialog(this, "Caixa Nº " + pk + " aberta com sucesso.\nFundo de caixa: "
                    + UiCaixa.moeda(fundo) + " Kz", "Abertura de caixa", JOptionPane.INFORMATION_MESSAGE, new IconeCaixa(IconeCaixa.Tipo.ABERTURA, 48));
            dispose();
        } catch (Exception e) {
            util.ui.ProgressoEmissao.cancelar();
            JOptionPane.showMessageDialog(this, e.getMessage(), "Abertura de caixa", JOptionPane.WARNING_MESSAGE);
        }
    }

    static Color corPerfil(String perfil) {
        return perfil != null && perfil.toLowerCase().startsWith("admin") ? ComponentesWeb.ROXO : ComponentesWeb.AZUL_VIVO;
    }
}
