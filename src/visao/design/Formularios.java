package visao.design;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.Image;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.RenderingHints;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.AbstractButton;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.plaf.basic.BasicCheckBoxUI;
import javax.swing.plaf.basic.BasicRadioButtonUI;

/**
 * Métodos utilitários para criar e organizar campos de formulário com o
 * novo design.
 */
public final class Formularios {

    private static final int TAMANHO_ICONE = 18;

    private Formularios() {
    }

    public static Graphics2D suavizar(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        return g2;
    }

    public static JLabel rotulo(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(Tema.ROTULO_FONTE);
        label.setForeground(Tema.ROTULO);
        return label;
    }

    /** Rótulo de campo obrigatório (com asterisco vermelho). */
    public static JLabel rotuloObrigatorio(String texto) {
        JLabel label = rotulo("<html>" + texto + " <span style='color:#DC2626'>*</span></html>");
        return label;
    }

    /** Texto auxiliar pequeno, em itálico. */
    public static JLabel nota(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(Tema.NOTA);
        label.setForeground(Tema.TEXTO_SUAVE);
        return label;
    }

    public static JTextField campoTexto(Font fonte) {
        JTextField campo = new CampoModerno();
        campo.setFont(fonte);
        dimensionar(campo, 80, Tema.ALTURA_CAMPO);
        return campo;
    }

    public static <E> ComboModerno<E> combo() {
        ComboModerno<E> combo = new ComboModerno<>();
        estiloCombo(combo);
        return combo;
    }

    public static void estiloCombo(JComboBox combo) {
        combo.setFont(Tema.COMBO);
        dimensionar(combo, 80, Tema.ALTURA_CAMPO);
    }

    /**
     * Aplica o aspecto do kit a um rádio ou checkbox: ícone desenhado,
     * realce ao passar o rato e indicação de foco.
     */
    public static void estiloOpcao(final AbstractButton opcao) {
        if (opcao instanceof JRadioButton) {
            opcao.setUI(new BasicRadioButtonUI());
        } else if (opcao instanceof JCheckBox) {
            opcao.setUI(new BasicCheckBoxUI());
        }
        Icon icone = new IconeOpcao(opcao instanceof JRadioButton);
        opcao.setIcon(icone);
        opcao.setSelectedIcon(icone);
        opcao.setRolloverIcon(icone);
        opcao.setRolloverSelectedIcon(icone);
        opcao.setPressedIcon(icone);
        opcao.setDisabledIcon(icone);
        opcao.setDisabledSelectedIcon(icone);
        opcao.setIconTextGap(8);
        opcao.setRolloverEnabled(true);
        opcao.setFont(Tema.OPCAO);
        opcao.setForeground(Tema.TEXTO);
        opcao.setOpaque(false);
        opcao.setFocusPainted(false);
        opcao.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        opcao.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                opcao.repaint();
            }

            @Override
            public void focusLost(FocusEvent e) {
                opcao.repaint();
            }
        });
    }

    public static void dimensionar(JComponent componente, int largura, int altura) {
        Dimension dimensao = new Dimension(largura, altura);
        componente.setPreferredSize(dimensao);
        componente.setMinimumSize(dimensao);
    }

    public static JPanel transparente(LayoutManager layout) {
        JPanel painel = new JPanel(layout);
        painel.setOpaque(false);
        return painel;
    }

    /**
     * Carrega um ícone dos recursos e redimensiona-o para o tamanho padrão,
     * para que todos os botões tenham ícones uniformes.
     */
    public static Icon icone(String caminho) {
        java.net.URL url = Formularios.class.getResource(caminho);
        if (url == null) {
            return null;
        }
        Image imagem = new ImageIcon(url).getImage()
                .getScaledInstance(TAMANHO_ICONE, TAMANHO_ICONE, Image.SCALE_SMOOTH);
        return new ImageIcon(imagem);
    }

    public static GridBagConstraints gbc(int x, int y, int largura, int altura,
            double pesoX, double pesoY, int preenchimento, Insets margens) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = x;
        c.gridy = y;
        c.gridwidth = largura;
        c.gridheight = altura;
        c.weightx = pesoX;
        c.weighty = pesoY;
        c.fill = preenchimento;
        c.anchor = GridBagConstraints.NORTHWEST;
        c.insets = margens;
        return c;
    }

    /**
     * Adiciona um par rótulo/campo empilhado. Cada "linha" lógica ocupa duas
     * linhas da grelha (rótulo em cima, campo em baixo).
     */
    public static void adicionarCampo(JPanel painel, int coluna, int linha, int span,
            JComponent rotulo, JComponent campo) {
        int esquerda = coluna > 0 ? 12 : 0;
        painel.add(rotulo, gbc(coluna, linha * 2, span, 1, 0, 0,
                GridBagConstraints.NONE, new Insets(0, esquerda, 5, 0)));
        painel.add(campo, gbc(coluna, linha * 2 + 1, span, 1, 1, 0,
                GridBagConstraints.HORIZONTAL, new Insets(0, esquerda, 12, 0)));
    }

    /**
     * Empurra o conteúdo para o canto superior esquerdo (evita que a grelha
     * fique centrada quando há campos ocultos).
     */
    public static void fecharGrade(JPanel painel, int linha, int colunas) {
        painel.add(transparente(new BorderLayout()), gbc(0, linha, colunas, 1, 1, 1,
                GridBagConstraints.BOTH, new Insets(0, 0, 0, 0)));
    }

    /**
     * Associa uma tecla (ex.: "ESCAPE", "F4") a uma acção, activa em qualquer
     * ponto da janela.
     */
    public static void atalho(JComponent raiz, String tecla, final Runnable accao) {
        String chave = "kitanda.atalho." + tecla;
        raiz.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(javax.swing.KeyStroke.getKeyStroke(tecla), chave);
        raiz.getActionMap().put(chave, new javax.swing.AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                accao.run();
            }
        });
    }

    /** Propriedade que marca um campo com erro de validação. */
    public static final String ERRO = "kitanda.erro";

    /**
     * Mostra (ou retira) o estado de erro de um campo: borda e brilho
     * vermelhos em vez das cores normais. Aceita também um {@link CampoData}.
     */
    public static void marcarErro(JComponent campo, boolean erro) {
        JComponent alvo = campo instanceof CampoData ? ((CampoData) campo).getCampo() : campo;
        alvo.putClientProperty(ERRO, erro ? Boolean.TRUE : null);
        alvo.repaint();
    }

    public static boolean temErro(java.awt.Component c) {
        return c instanceof JComponent && Boolean.TRUE.equals(((JComponent) c).getClientProperty(ERRO));
    }

    /**
     * Os seguidores (ex.: o rótulo ou o grupo rótulo+campo) ficam visíveis
     * só quando o alvo está visível. Útil quando a lógica da janela esconde
     * apenas o campo.
     */
    public static void seguirVisibilidade(final JComponent alvo, final JComponent... seguidores) {
        alvo.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentShown(java.awt.event.ComponentEvent e) {
                aplicar(true);
            }

            @Override
            public void componentHidden(java.awt.event.ComponentEvent e) {
                aplicar(false);
            }

            private void aplicar(boolean visivel) {
                for (JComponent s : seguidores) {
                    s.setVisible(visivel);
                }
            }
        });
        for (JComponent s : seguidores) {
            s.setVisible(alvo.isVisible());
        }
    }

    /**
     * Descreve um botão para quem o usa: dica (com o atalho, se houver) e
     * nome acessível, essencial nos botões só com ícone.
     */
    public static void descrever(AbstractButton botao, String nome, String atalho) {
        String dica = atalho == null ? nome : nome + "  (" + atalho + ")";
        botao.setToolTipText(dica);
        botao.getAccessibleContext().setAccessibleName(nome);
        botao.getAccessibleContext().setAccessibleDescription(dica);
    }

    /**
     * Empilha cartões numa coluna. O último estica para que todas as colunas
     * terminem à mesma altura.
     */
    public static JPanel coluna(JComponent... cartoes) {
        JPanel coluna = transparente(new java.awt.GridBagLayout());
        for (int i = 0; i < cartoes.length; i++) {
            boolean ultimo = i == cartoes.length - 1;
            coluna.add(cartoes[i], gbc(0, i, 1, 1, 1, ultimo ? 1 : 0, GridBagConstraints.BOTH,
                    new Insets(0, 0, ultimo ? 0 : Tema.ESPACO, 0)));
        }
        return coluna;
    }
}
