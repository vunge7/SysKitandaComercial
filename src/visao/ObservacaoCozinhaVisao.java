package visao;

import entity.TbProduto;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import util.ui.ComponentesWeb;
import util.ui.EstiloUI;

/**
 * Observação para a cozinha ao lançar um prato na mesa.
 *
 * <p>
 * Sugestões de um clique (sem sal, bem passado, para levar...), texto livre e
 * a quantidade, tudo num só passo. "Enviar" lança o item na mesa e o talão sai
 * logo na cozinha com a observação em destaque; "Sem observação" lança sem
 * nota; "Cancelar" (Esc) não lança nada.
 *
 * <p>
 * Atalhos: Ctrl+Enter envia, Esc cancela. 2026-11-17 (substitui o formulário
 * antigo de uma só caixa de texto).
 */
public class ObservacaoCozinhaVisao extends JDialog {

    /** Notas mais pedidas; ficam em pílulas para não ter de as escrever. */
    static final String[] SUGESTOES = {
        "Sem sal", "Pouco sal", "Sem picante", "Picante", "Sem cebola", "Sem alho",
        "Sem molho", "Molho à parte", "Bem passado", "Ao ponto", "Mal passado",
        "Sem gelo", "Bem quente", "Dose de criança", "Para levar", "Urgente"
    };

    private final TbProduto produtoLocal;
    private final List<JToggleButton> pilulas = new ArrayList<>();
    private final JTextArea txtObservacao = new JTextArea(3, 30);
    private final JTextField txtQtd = new JTextField(3);
    private final JLabel resumo = new JLabel(" ");

    public ObservacaoCozinhaVisao(java.awt.Frame parent, boolean modal, TbProduto produtoParm, String obs) {
        super(parent, modal);
        this.produtoLocal = produtoParm;
        setTitle("Observação para a cozinha");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        montar(obs);
        // as pílulas quebram linha conforme a largura: fixa a largura e mede a altura depois
        setSize(600, 400);
        validate();
        setSize(600, getPreferredSize().height);
        validate();
        setSize(600, getPreferredSize().height);
        setMinimumSize(new Dimension(560, 380));
        setLocationRelativeTo(parent);
    }

    private void montar(String obsInicial) {
        JPanel raiz = new JPanel(new BorderLayout());
        raiz.setBackground(ComponentesWeb.FUNDO_PAGINA);
        setContentPane(raiz);

        // ---- cabeçalho: o prato
        JPanel cab = new JPanel(new BorderLayout(0, 2));
        cab.setBackground(ComponentesWeb.AZUL);
        cab.setBorder(BorderFactory.createEmptyBorder(14, 20, 14, 20));
        JLabel sub = new JLabel("ENVIAR PARA A COZINHA");
        sub.setForeground(new Color(191, 219, 254));
        sub.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(11f));
        JLabel prato = new JLabel(produtoLocal == null ? "Prato" : produtoLocal.getDesignacao());
        prato.setForeground(Color.WHITE);
        prato.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(20f));
        cab.add(sub, BorderLayout.NORTH);
        cab.add(prato, BorderLayout.CENTER);
        raiz.add(cab, BorderLayout.NORTH);

        // ---- corpo
        JPanel corpo = new JPanel();
        corpo.setOpaque(false);
        corpo.setLayout(new BoxLayout(corpo, BoxLayout.Y_AXIS));
        corpo.setBorder(BorderFactory.createEmptyBorder(14, 20, 6, 20));

        corpo.add(titulo("Quantidade"));
        corpo.add(quantidade());
        corpo.add(Box.createVerticalStrut(12));

        corpo.add(titulo("Sugestões rápidas (clique para marcar)"));
        JPanel grelha = new JPanel(new EstiloUI.WrapLayout(FlowLayout.LEFT, 6, 6));
        grelha.setOpaque(false);
        grelha.setAlignmentX(LEFT_ALIGNMENT);
        for (String s : SUGESTOES) {
            JToggleButton t = new JToggleButton(s);
            ComponentesWeb.pilulaCompacta(t);
            t.addActionListener(e -> actualizarResumo());
            pilulas.add(t);
            grelha.add(t);
        }
        corpo.add(grelha);
        corpo.add(Box.createVerticalStrut(10));

        corpo.add(titulo("Outra observação"));
        txtObservacao.setLineWrap(true);
        txtObservacao.setWrapStyleWord(true);
        txtObservacao.setFont(EstiloUI.FONTE.deriveFont(14f));
        txtObservacao.setText(obsInicial == null ? "" : obsInicial.trim());
        txtObservacao.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                actualizarResumo();
            }

            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                actualizarResumo();
            }

            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                actualizarResumo();
            }
        });
        JScrollPane sp = new JScrollPane(txtObservacao);
        sp.setBorder(BorderFactory.createLineBorder(ComponentesWeb.LINHA));
        sp.setAlignmentX(LEFT_ALIGNMENT);
        sp.setPreferredSize(new Dimension(500, 70));
        corpo.add(sp);
        corpo.add(Box.createVerticalStrut(8));

        resumo.setFont(EstiloUI.FONTE.deriveFont(Font.ITALIC, 12.5f));
        resumo.setForeground(ComponentesWeb.TEXTO_SUAVE);
        resumo.setAlignmentX(LEFT_ALIGNMENT);
        corpo.add(resumo);
        raiz.add(corpo, BorderLayout.CENTER);

        // ---- rodapé
        JButton enviar = new JButton("Enviar para a cozinha");
        ComponentesWeb.botao(enviar, ComponentesWeb.Variante.SUCESSO, null, "Lança o prato e imprime o talão na cozinha (Ctrl+Enter)");
        enviar.addActionListener(e -> enviar(observacao()));
        JButton semObs = new JButton("Sem observação");
        ComponentesWeb.botao(semObs, ComponentesWeb.Variante.SECUNDARIO, null, "Lança o prato sem nota para a cozinha");
        semObs.addActionListener(e -> enviar(""));
        JButton cancelar = new JButton("Cancelar");
        ComponentesWeb.botao(cancelar, ComponentesWeb.Variante.PERIGO, null, "Não lança o prato (Esc)");
        cancelar.addActionListener(e -> dispose());

        JPanel rod = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        rod.setOpaque(false);
        rod.setBorder(BorderFactory.createEmptyBorder(8, 20, 16, 20));
        rod.add(cancelar);
        rod.add(semObs);
        rod.add(enviar);
        raiz.add(rod, BorderLayout.SOUTH);
        getRootPane().setDefaultButton(enviar);

        JComponent r = getRootPane();
        r.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "cancelar");
        r.getActionMap().put("cancelar", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        r.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, InputEvent.CTRL_DOWN_MASK), "enviar");
        r.getActionMap().put("enviar", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                enviar.doClick();
            }
        });
        actualizarResumo();
    }

    private JPanel quantidade() {
        String q = "1";
        try {
            String t = NovaGestaoPedidosVisao.txtQuantidade.getText().trim();
            if (Integer.parseInt(t) > 0) {
                q = t;
            }
        } catch (Exception ignorar) {
            // campo vazio ou inválido: 1
        }
        txtQtd.setText(q);
        txtQtd.setHorizontalAlignment(SwingConstants.CENTER);
        txtQtd.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(18f));
        txtQtd.setPreferredSize(new Dimension(64, 38));
        JButton menos = new JButton("−");
        JButton mais = new JButton("+");
        ComponentesWeb.botao(menos, ComponentesWeb.Variante.SECUNDARIO, null, "Menos uma dose");
        ComponentesWeb.botao(mais, ComponentesWeb.Variante.SECUNDARIO, null, "Mais uma dose");
        menos.addActionListener(e -> txtQtd.setText(String.valueOf(Math.max(1, qtd() - 1))));
        mais.addActionListener(e -> txtQtd.setText(String.valueOf(qtd() + 1)));
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        p.setOpaque(false);
        p.setAlignmentX(LEFT_ALIGNMENT);
        p.add(menos);
        p.add(txtQtd);
        p.add(mais);
        return p;
    }

    private int qtd() {
        try {
            return Math.max(1, Integer.parseInt(txtQtd.getText().trim()));
        } catch (Exception e) {
            return 1;
        }
    }

    private static JLabel titulo(String t) {
        JLabel l = new JLabel(t);
        l.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(12.5f));
        l.setForeground(ComponentesWeb.TEXTO);
        l.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
        l.setAlignmentX(LEFT_ALIGNMENT);
        return l;
    }

    /** Sugestões marcadas + texto livre, separados por vírgulas. */
    private String observacao() {
        StringBuilder sb = new StringBuilder();
        for (JToggleButton t : pilulas) {
            if (t.isSelected()) {
                sb.append(sb.length() > 0 ? ", " : "").append(t.getText());
            }
        }
        String livre = txtObservacao.getText().trim().replaceAll("\\s+", " ");
        if (!livre.isEmpty()) {
            sb.append(sb.length() > 0 ? ", " : "").append(livre);
        }
        return sb.toString();
    }

    private void actualizarResumo() {
        String o = observacao();
        resumo.setText(o.isEmpty() ? "Sem observação — o talão sai só com o prato e a quantidade."
                : "Vai no talão: " + o);
    }

    private void enviar(String obs) {
        if (produtoLocal == null) {
            dispose();
            return;
        }
        String qtdAnterior = NovaGestaoPedidosVisao.txtQuantidade.getText();
        try {
            NovaGestaoPedidosVisao.observacaoCozinha = obs;
            NovaGestaoPedidosVisao.txtQuantidade.setText(String.valueOf(qtd()));
            dispose();
            NovaGestaoPedidosVisao.procedimento_salvar_pedidos_iten_pedidos(produtoLocal.getDesignacao());
            NovaGestaoPedidosVisao.status_button_lugar(true);
        } finally {
            // a nota é deste prato: não pode passar para o item seguinte
            NovaGestaoPedidosVisao.observacaoCozinha = "";
            NovaGestaoPedidosVisao.txtQuantidade.setText(qtdAnterior);
        }
    }
}
