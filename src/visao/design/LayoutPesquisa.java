package visao.design;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

/**
 * Monta o layout padrão dos formulários de pesquisa (lupas):
 * <pre>
 * ┌ Cabeçalho (sigla, título, subtítulo, total, ×) ──────────────┐
 * │ ┌ Cartão ─────────────────────────────────────────────────┐ │
 * │ │ [🔍 campo de pesquisa]              [legenda/indicadores] │ │
 * │ │ ┌ tabela ─────────────────────────────────────────────┐ │ │
 * │ └─────────────────────────────────────────────────────────┘ │
 * └ Rodapé: atalhos ............................ [Fechar] [Seleccionar] ┘
 * </pre>
 * Uso:
 * <pre>
 * LayoutPesquisa.para(this)
 *         .titulo("C", "Pesquisa de Clientes", "Escreva o nome...")
 *         .campo(txtNome)
 *         .tabela(jScrollPane1)
 *         .fechar(jButton1)
 *         .aoSeleccionar(() -&gt; ...)
 *         .montar();
 * </pre>
 */
public final class LayoutPesquisa {

    private static final int QUEBRA_BARRA = 980;
    private static final int QUEBRA_RODAPE = 760;

    private final JDialog janela;
    private String sigla = "?";
    private String titulo = "Pesquisa";
    private String subtitulo = "";
    private String atalhos = "↑ ↓ navegar     •     Enter ou duplo clique: seleccionar     •     Esc: fechar";
    private JTextField campo;
    private JScrollPane rolagemTabela;
    private JButton fechar;
    private JComponent legenda;
    private JLabel rotuloTotal;
    private JLabel total;
    private Runnable seleccionar;
    private int largura = 1120;
    private int altura = 740;

    private LayoutPesquisa(JDialog janela) {
        this.janela = janela;
    }

    public static LayoutPesquisa para(JDialog janela) {
        return new LayoutPesquisa(janela);
    }

    public LayoutPesquisa titulo(String sigla, String titulo, String subtitulo) {
        this.sigla = sigla;
        this.titulo = titulo;
        this.subtitulo = subtitulo;
        return this;
    }

    /** Campo de pesquisa (de preferência um {@link CampoPesquisa}). */
    public LayoutPesquisa campo(JTextField campo) {
        this.campo = campo;
        return this;
    }

    /** Rolagem que contém a tabela (de preferência {@link RolagemModerna} com {@link TabelaModerna}). */
    public LayoutPesquisa tabela(JScrollPane rolagemTabela) {
        this.rolagemTabela = rolagemTabela;
        return this;
    }

    /** Botão de fechar do cabeçalho (de preferência um {@link BotaoFechar}). */
    public LayoutPesquisa fechar(JButton fechar) {
        this.fechar = fechar;
        return this;
    }

    /** Componente mostrado ao lado do campo (ex.: indicadores de estado). */
    public LayoutPesquisa legenda(JComponent legenda) {
        this.legenda = legenda;
        return this;
    }

    /** Contador de resultados mostrado no cabeçalho. */
    public LayoutPesquisa total(JLabel rotulo, JLabel total) {
        this.rotuloTotal = rotulo;
        this.total = total;
        return this;
    }

    /** Acção do botão "Seleccionar" (sem acção, o botão não aparece). */
    public LayoutPesquisa aoSeleccionar(Runnable seleccionar) {
        this.seleccionar = seleccionar;
        return this;
    }

    /** Texto de ajuda com os atalhos, mostrado no rodapé. */
    public LayoutPesquisa atalhos(String atalhos) {
        this.atalhos = atalhos;
        return this;
    }

    public LayoutPesquisa tamanho(int largura, int altura) {
        this.largura = largura;
        this.altura = altura;
        return this;
    }

    public void montar() {
        janela.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        FundoJanela raiz = new FundoJanela();
        raiz.setBorder(BorderFactory.createLineBorder(Tema.CABECALHO_INICIO));
        raiz.add(criarCabecalho(), BorderLayout.NORTH);
        raiz.add(criarConteudo(), BorderLayout.CENTER);
        raiz.add(criarRodape(), BorderLayout.SOUTH);
        janela.setContentPane(raiz);
        Formularios.atalho(raiz, "ESCAPE", janela::dispose);

        Rectangle ecra = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        janela.setSize(Math.min(largura, ecra.width - 60), Math.min(altura, ecra.height - 60));
    }

    private Cabecalho criarCabecalho() {
        Cabecalho cabecalho = new Cabecalho(sigla, titulo, subtitulo);
        if (total != null) {
            if (rotuloTotal != null) {
                rotuloTotal.setFont(Tema.TITULO_CAIXA);
                rotuloTotal.setForeground(Tema.CABECALHO_SUBTITULO);
                cabecalho.adicionarDireita(rotuloTotal);
            }
            cabecalho.adicionarDireita(total);
        }
        if (fechar != null) {
            cabecalho.adicionarDireita(fechar);
        }
        if (janela.isUndecorated()) {
            cabecalho.permitirArrastar(janela);
        }
        return cabecalho;
    }

    private JPanel criarConteudo() {
        Cartao cartao = new Cartao(null, Tema.PRIMARIA);
        cartao.setLayout(new GridBagLayout());

        if (campo != null) {
            Formularios.dimensionar(campo, 260, 42);
            final JComponent lado = legenda;
            PainelResponsivo barra = new PainelResponsivo(new int[]{QUEBRA_BARRA}, (p, modo) -> {
                if (lado == null) {
                    p.add(campo, Formularios.gbc(0, 0, 1, 1, 1, 0, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0)));
                } else if (modo == 0) {
                    p.add(campo, Formularios.gbc(0, 0, 1, 1, 1, 0, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 10, 0)));
                    p.add(lado, Formularios.gbc(0, 1, 1, 1, 1, 0, GridBagConstraints.NONE, new Insets(0, 0, 0, 0)));
                } else {
                    p.add(campo, Formularios.gbc(0, 0, 1, 1, 1, 0, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 14)));
                    GridBagConstraints c = Formularios.gbc(1, 0, 1, 1, 0, 0, GridBagConstraints.NONE, new Insets(0, 0, 0, 0));
                    c.anchor = GridBagConstraints.EAST;
                    p.add(lado, c);
                }
            });
            cartao.add(barra, Formularios.gbc(0, 0, 1, 1, 1, 0, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 14, 0)));
        }
        cartao.add(new PainelTabela(rolagemTabela), Formularios.gbc(0, 1, 1, 1, 1, 1, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0)));

        JPanel conteudo = Formularios.transparente(new BorderLayout());
        conteudo.setBorder(BorderFactory.createEmptyBorder(16, 20, 12, 20));
        conteudo.add(cartao, BorderLayout.CENTER);
        return conteudo;
    }

    private Rodape criarRodape() {
        JPanel esquerda = Formularios.transparente(new FlowLayout(FlowLayout.LEFT, 0, 0));
        esquerda.add(Formularios.nota(atalhos));

        JPanel direita = Formularios.transparente(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        BotaoModerno btFechar = new BotaoModerno("Fechar", Tema.NEUTRA, true);
        btFechar.addActionListener(evt -> janela.dispose());
        Formularios.dimensionar(btFechar, 120, 40);
        direita.add(btFechar);
        if (seleccionar != null) {
            BotaoModerno btSeleccionar = new BotaoModerno("Seleccionar", Tema.PRIMARIA, false);
            btSeleccionar.addActionListener(evt -> seleccionar.run());
            Formularios.dimensionar(btSeleccionar, 140, 40);
            direita.add(btSeleccionar);
        }
        return new Rodape(esquerda, direita, QUEBRA_RODAPE);
    }
}
