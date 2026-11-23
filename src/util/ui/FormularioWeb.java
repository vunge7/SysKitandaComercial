package util.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.RootPaneContainer;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

/**
 * Peças comuns dos formulários de cadastro com aspecto web (mesmo estilo e
 * cores da Emissão de Recibos): cabeçalho em gradiente, cartões, campos com
 * rótulo por cima, grelhas responsivas e barra de acções sempre visível.
 * O initComponents continua a criar os componentes e os eventos; estas peças
 * só os arrumam.
 */
public final class FormularioWeb {

    public static final int ALTURA_CAMPO = 30;
    private static final int ESPACO = 10;
    /** Texto claro do cabeçalho (o tema do negócio muda-o). */
    static Color TEXTO_CABECALHO = new Color(196, 218, 245);

    private FormularioWeb() {
    }

    // =====================================================================
    // JANELA
    // =====================================================================
    /**
     * Substitui o conteúdo da janela: cabeçalho em cima, conteúdo deslizante
     * no meio e, sempre visíveis em baixo, as acções e a barra de estado.
     */
    public static <J extends Window & RootPaneContainer> void montar(J janela, JComponent cabecalho,
            JComponent conteudo, JComponent accoes, String textoEstado) {
        Color fundo = ComponentesWeb.FUNDO_PAGINA;

        // já na construção: a chávena do Java não chega a aparecer, venha de onde vier a janela
        janela.setIconImages(MenuWeb.iconesAplicacao());

        JScrollPane rolagem = EstiloUI.adaptavel(conteudo);
        rolagem.setOpaque(false);
        rolagem.getViewport().setBackground(fundo);
        rolagem.getViewport().getView().setBackground(fundo);

        JPanel centro = new JPanel(new BorderLayout());
        centro.setBackground(fundo);
        centro.setBorder(BorderFactory.createEmptyBorder(8, 8, 4, 8));
        centro.add(rolagem, BorderLayout.CENTER);

        JPanel rodape = new JPanel(new BorderLayout());
        rodape.setBackground(fundo);
        rodape.add(accoes, BorderLayout.NORTH);
        rodape.add(EstiloUI.barraEstadoComRelogio(janela, textoEstado, null), BorderLayout.SOUTH);

        Container conteudoJanela = janela.getContentPane();
        conteudoJanela.removeAll();
        conteudoJanela.setLayout(new BorderLayout());
        conteudoJanela.setBackground(fundo);
        conteudoJanela.add(cabecalho, BorderLayout.NORTH);
        conteudoJanela.add(centro, BorderLayout.CENTER);
        conteudoJanela.add(rodape, BorderLayout.SOUTH);
    }

    /**
     * Tamanho próprio de um formulário de cadastro (sem maximizar), limitado à
     * área útil do ecrã, redimensionável e ao centro.
     */
    public static void ajustar(Window janela, int largura, int altura, int larguraMinima, int alturaMinima) {
        Rectangle util = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        if (janela instanceof Frame) {
            ((Frame) janela).setResizable(true);
        } else if (janela instanceof Dialog) {
            ((Dialog) janela).setResizable(true);
        }
        janela.setMinimumSize(new Dimension(Math.min(larguraMinima, util.width), Math.min(alturaMinima, util.height)));
        janela.setSize(Math.min(largura, (int) (util.width * 0.96)), Math.min(altura, (int) (util.height * 0.96)));
        janela.setLocationRelativeTo(null);
    }

    // =====================================================================
    // CABEÇALHO
    // =====================================================================
    /** direita pode ser null. */
    public static JPanel cabecalho(String titulo, String subtitulo, JComponent direita) {
        JPanel cabecalho = new ComponentesWeb.PainelGradiente(new BorderLayout(16, 0),
                EstiloUI.AZUL_ESCURO, Tema.gradienteFim());
        cabecalho.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));

        JLabel lbTitulo = new JLabel(titulo);
        lbTitulo.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(23f));
        lbTitulo.setForeground(Color.WHITE);
        JLabel lbSubtitulo = new JLabel(subtitulo);
        lbSubtitulo.setFont(EstiloUI.FONTE.deriveFont(13f));
        lbSubtitulo.setForeground(TEXTO_CABECALHO);

        JPanel titulos = ComponentesWeb.transparente(new java.awt.GridLayout(2, 1, 0, 0));
        titulos.add(lbTitulo);
        titulos.add(lbSubtitulo);
        JPanel marca = ComponentesWeb.transparente(new BorderLayout(12, 0));
        marca.add(new MenuWeb.Logotipo(44), BorderLayout.WEST);
        marca.add(titulos, BorderLayout.CENTER);
        JPanel marcaTopo = ComponentesWeb.transparente(new BorderLayout());
        marcaTopo.add(marca, BorderLayout.NORTH);

        cabecalho.add(marcaTopo, BorderLayout.WEST);
        if (direita != null) {
            cabecalho.add(direita, BorderLayout.CENTER);
        }
        return cabecalho;
    }

    /** Campo do cabeçalho: rótulo claro por cima do componente. */
    public static JPanel campoCabecalho(JLabel rotulo, Component componente) {
        rotulo.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(12.5f));
        rotulo.setForeground(TEXTO_CABECALHO);
        JPanel p = ComponentesWeb.transparente(new BorderLayout(0, 3));
        p.add(rotulo, BorderLayout.NORTH);
        p.add(componente, BorderLayout.CENTER);
        return p;
    }

    // =====================================================================
    // ARRUMAÇÃO
    // =====================================================================
    /** Cartão com o conteúdo já colocado e os painéis interiores transparentes. */
    public static ComponentesWeb.Cartao cartao(String titulo, Color destaque, Component conteudo) {
        ComponentesWeb.Cartao cartao = new ComponentesWeb.Cartao(titulo, destaque, new BorderLayout());
        cartao.getCorpo().add(conteudo, BorderLayout.CENTER);
        ComponentesWeb.transparentar(cartao.getCorpo());
        return cartao;
    }

    /**
     * Componentes empilhados, cada um com a sua altura preferida e a largura
     * toda; a altura que sobrar fica em baixo, sem esticar os campos.
     */
    public static JPanel pilha(int espaco, Component... componentes) {
        JPanel resto = null;
        for (int i = componentes.length - 1; i >= 0; i--) {
            JPanel p = ComponentesWeb.transparente(new BorderLayout(0, espaco));
            p.add(componentes[i], BorderLayout.NORTH);
            if (resto != null) {
                p.add(resto, BorderLayout.CENTER);
            }
            resto = p;
        }
        return resto;
    }

    /** Tantas colunas quantas couberem (até maxColunas), reorganiza-se ao mudar a largura. */
    public static JPanel grelha(int larguraMinima, int maxColunas, Component... componentes) {
        JPanel p = ComponentesWeb.transparente(new ComponentesWeb.GrelhaResponsiva(larguraMinima, ESPACO, maxColunas));
        for (Component c : componentes) {
            p.add(c);
        }
        // a altura preferida depende da largura, que só se conhece depois do
        // layout: sem isto, a linha nova ficava cortada ao estreitar a janela
        p.addComponentListener(new ComponentAdapter() {
            private int larguraAnterior = -1;

            @Override
            public void componentResized(ComponentEvent e) {
                if (p.getWidth() != larguraAnterior) {
                    larguraAnterior = p.getWidth();
                    SwingUtilities.invokeLater(() -> {
                        p.invalidate();
                        p.revalidate();
                    });
                }
            }
        });
        return p;
    }

    /** Lista à esquerda e formulário à direita (empilham-se em janelas estreitas). */
    public static JPanel listaEFormulario(Component lista, Component formulario) {
        JPanel colunas = ComponentesWeb.transparente(new ComponentesWeb.ColunasResponsivas(
                new double[]{0.28, 0.72}, new int[]{250, 520}, ESPACO));
        colunas.add(lista);
        colunas.add(formulario);
        return colunas;
    }

    /** Cartão de registos: pesquisa pelas iniciais em cima e lista a ocupar o resto; rodape pode ser null. */
    public static ComponentesWeb.Cartao cartaoLista(String titulo, JTextField pesquisa, JList<?> lista,
            JScrollPane rolagemLista, Component rodape) {
        lista.setBorder(null);
        lista.setFixedCellHeight(30);
        lista.setCellRenderer(new ComponentesWeb.RendererLista(null));
        rolagemLista.setPreferredSize(new Dimension(240, 240));
        rolagemLista.setMinimumSize(new Dimension(200, 120));

        JPanel corpo = ComponentesWeb.transparente(new BorderLayout(0, 8));
        corpo.add(ComponentesWeb.campo("Pesquisar pelas iniciais do nome", pesquisa), BorderLayout.NORTH);
        corpo.add(rolagemLista, BorderLayout.CENTER);
        if (rodape != null) {
            corpo.add(rodape, BorderLayout.SOUTH);
        }
        return cartao(titulo, ComponentesWeb.AZUL_VIVO, corpo);
    }

    /** Campo com um botão pequeno à direita (ex.: "+" para cadastrar uma opção nova). */
    public static JPanel comBotao(Component campo, JButton botao, String dica) {
        ComponentesWeb.botao(botao, ComponentesWeb.Variante.SECUNDARIO, null, dica);
        botao.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        botao.setPreferredSize(new Dimension(34, ALTURA_CAMPO));
        JPanel p = ComponentesWeb.transparente(new BorderLayout(6, 0));
        p.add(campo, BorderLayout.CENTER);
        p.add(botao, BorderLayout.EAST);
        return p;
    }

    /** Botões de opção como pílulas, lado a lado. */
    public static JPanel pilulas(JToggleButton... botoes) {
        JPanel p = ComponentesWeb.transparente(new FlowLayout(FlowLayout.LEFT, 6, 2));
        for (JToggleButton b : botoes) {
            ComponentesWeb.pilulaCompacta(b);
            p.add(b);
        }
        return p;
    }

    /** Barra de acções, sempre visível: dica à esquerda (pode ser null) e botões à direita. */
    public static JPanel barraAccoes(JLabel dica, AbstractButton... botoes) {
        JPanel barra = new JPanel(new BorderLayout(16, 0));
        barra.setBackground(Color.WHITE);
        barra.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, ComponentesWeb.LINHA),
                BorderFactory.createEmptyBorder(8, 16, 8, 16)));
        if (dica != null) {
            barra.add(dica, BorderLayout.WEST);
        }
        JPanel direita = ComponentesWeb.transparente(new EstiloUI.WrapLayout(FlowLayout.RIGHT, 8, 0));
        for (AbstractButton b : botoes) {
            direita.add(b);
        }
        barra.add(direita, BorderLayout.CENTER);
        return barra;
    }

    public static JLabel dica(String texto) {
        JLabel l = new JLabel(texto);
        l.setFont(EstiloUI.FONTE.deriveFont(12f));
        l.setForeground(ComponentesWeb.TEXTO_SUAVE);
        return l;
    }

    // =====================================================================
    // ESTILO DOS CAMPOS E BOTÕES
    // =====================================================================
    /**
     * Fundo igual ao do sistema (é o que deixa os campos desactivados a
     * cinzento); os destaques a amarelo da validação continuam a funcionar.
     */
    public static void campoTexto(JTextField... campos) {
        for (JTextField t : campos) {
            t.setFont(EstiloUI.FONTE.deriveFont(13.5f));
            t.setBackground(UIManager.getColor("TextField.background"));
            t.setForeground(ComponentesWeb.TEXTO);
            t.setCaretColor(ComponentesWeb.TEXTO);
            t.setDisabledTextColor(ComponentesWeb.TEXTO_SUAVE);
            fixarAltura(t);
            t.setBorder(bordaCampo(ComponentesWeb.LINHA));
            t.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    t.setBorder(bordaCampo(ComponentesWeb.AZUL_VIVO));
                }

                @Override
                public void focusLost(FocusEvent e) {
                    t.setBorder(bordaCampo(ComponentesWeb.LINHA));
                }
            });
        }
    }

    public static void combo(JComboBox<?>... combos) {
        for (JComboBox<?> c : combos) {
            c.setBackground(Color.WHITE);
            c.setFont(EstiloUI.FONTE);
            fixarAltura(c);
        }
    }

    public static void data(com.toedter.calendar.JDateChooser data) {
        data.setFont(EstiloUI.FONTE);
        fixarAltura(data);
        Component editor = data.getDateEditor().getUiComponent();
        if (editor instanceof JComponent) {
            ((JComponent) editor).setBorder(bordaCampo(ComponentesWeb.LINHA));
        }
    }

    public static void spinner(JSpinner spinner) {
        spinner.setFont(EstiloUI.FONTE);
        fixarAltura(spinner);
        spinner.setBorder(new LineBorder(ComponentesWeb.LINHA, 1, true));
    }

    /** Largura decidida pela grelha (não pelo item mais comprido) e altura uniforme. */
    private static void fixarAltura(JComponent c) {
        c.setPreferredSize(new Dimension(120, ALTURA_CAMPO));
        c.setMinimumSize(new Dimension(60, ALTURA_CAMPO));
    }

    private static Border bordaCampo(Color cor) {
        return BorderFactory.createCompoundBorder(
                new LineBorder(cor, 1, true),
                BorderFactory.createEmptyBorder(3, 8, 3, 8));
    }

    public static void botaoSalvar(AbstractButton b, String dica) {
        ComponentesWeb.botao(b, ComponentesWeb.Variante.SUCESSO, "/imagens/salvar_16x16.png", dica);
    }

    public static void botaoAlterar(AbstractButton b, String dica) {
        ComponentesWeb.botao(b, ComponentesWeb.Variante.PRIMARIO, "/imagens/alterar_16x16.png", dica);
    }

    public static void botaoEliminar(AbstractButton b, String dica) {
        ComponentesWeb.botao(b, ComponentesWeb.Variante.AVISO, "/imagens/eliminar_16x16.png", dica);
    }

    public static void botaoSair(AbstractButton b, String dica) {
        ComponentesWeb.botao(b, ComponentesWeb.Variante.PERIGO, null, dica);
        b.setIcon(new ComponentesWeb.IconeSair(16, Color.WHITE));
    }
}
