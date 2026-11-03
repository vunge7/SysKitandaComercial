package visao.log;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import visao.design.Cabecalho;
import visao.design.Caixa;
import visao.design.Cartao;
import visao.design.ControloSegmentado;
import visao.design.FundoJanela;
import visao.design.PainelResponsivo;
import visao.design.PainelRolavel;
import visao.design.PainelTabela;
import visao.design.RolagemModerna;
import visao.design.Rodape;
import visao.design.Tema;
import static visao.design.Formularios.adicionarCampo;
import static visao.design.Formularios.dimensionar;
import static visao.design.Formularios.gbc;
import static visao.design.Formularios.rotulo;
import static visao.design.Formularios.transparente;

/**
 * Organiza a janela de registo de actividades e adapta-a à largura:
 * <ul>
 * <li>larga (&ge; 1240px): tabela à esquerda, detalhe numa coluna à direita;</li>
 * <li>estreita: detalhe por baixo da tabela.</li>
 * </ul>
 * Os filtros passam de duas para três linhas abaixo de 1180px.
 */
final class LogLayout {

    private static final int QUEBRA_CORPO = 1240;
    // a grelha larga dos filtros precisa de ~1150px; abaixo disso o GridBagLayout cortaria os rótulos
    private static final int QUEBRA_FILTROS = 1200;
    private static final int QUEBRA_DETALHE = 720;
    private static final int QUEBRA_RODAPE = 1000;
    private static final int LARGURA_DETALHE = 440;
    private static final int LARGURA_MINIMA = 760;

    private static final int MODO_ESTREITO = 0;

    private final LogActividadeVisao j;

    LogLayout(LogActividadeVisao janela) {
        this.j = janela;
    }

    void montar() {
        FundoJanela raiz = new FundoJanela();
        raiz.add(criarCabecalho(), BorderLayout.NORTH);
        raiz.add(criarConteudo(), BorderLayout.CENTER);
        raiz.add(criarRodape(), BorderLayout.SOUTH);
        j.setContentPane(raiz);

        Rectangle ecra = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        j.setSize(Math.min(1440, ecra.width - 40), Math.min(900, ecra.height - 40));
        j.setMinimumSize(new Dimension(Math.min(820, ecra.width), Math.min(600, ecra.height)));
    }

    // ------------------------------------------------------------------
    // Cabeçalho
    // ------------------------------------------------------------------
    private JPanel criarCabecalho() {
        Cabecalho cabecalho = new Cabecalho("L", "Registo de Actividades",
                "Auditoria: quem fez o quê, quando e em que posto");
        cabecalho.adicionarDireita(j.lbTotal);
        return cabecalho;
    }

    // ------------------------------------------------------------------
    // Conteúdo: filtros em cima, registos + detalhe a ocupar o resto
    // ------------------------------------------------------------------
    private JComponent criarConteudo() {
        final JPanel registos = criarCartaoRegistos();
        final JPanel detalhe = criarCartaoDetalhe();
        PainelResponsivo corpo = new PainelResponsivo(new int[]{QUEBRA_CORPO}, (p, modo) -> {
            if (modo == MODO_ESTREITO) {
                p.add(registos, gbc(0, 0, 1, 1, 1, 0.62, GridBagConstraints.BOTH, new Insets(0, 0, Tema.ESPACO, 0)));
                p.add(detalhe, gbc(0, 1, 1, 1, 1, 0.38, GridBagConstraints.BOTH, semMargem()));
            } else {
                p.add(registos, gbc(0, 0, 1, 1, 1, 1, GridBagConstraints.BOTH, new Insets(0, 0, 0, Tema.ESPACO)));
                p.add(detalhe, gbc(1, 0, 1, 1, 0, 1, GridBagConstraints.BOTH, semMargem()));
            }
        });

        // Como em ProdutoLayout: em ecrãs baixos o conteúdo rola em vez de esmagar a tabela
        PainelRolavel conteudo = new PainelRolavel(new GridBagLayout(), LARGURA_MINIMA);
        conteudo.setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));
        conteudo.add(criarCartaoFiltros(), gbc(0, 0, 1, 1, 1, 0, GridBagConstraints.BOTH, new Insets(0, 0, Tema.ESPACO, 0)));
        conteudo.add(corpo, gbc(0, 1, 1, 1, 1, 1, GridBagConstraints.BOTH, semMargem()));
        return new RolagemModerna(conteudo);
    }

    private JPanel criarCartaoFiltros() {
        Cartao cartao = new Cartao("Filtros", "período, utilizador, módulo e acção", Tema.PRIMARIA);
        cartao.setLayout(new BorderLayout());

        ControloSegmentado periodo = new ControloSegmentado(j.tgHoje, j.tg7Dias, j.tg30Dias, j.tgEsteMes);
        final JPanel atalhos = transparente(new BorderLayout());
        atalhos.add(periodo, BorderLayout.WEST);
        dimensionar(atalhos, periodo.getPreferredSize().width, Tema.ALTURA_CAMPO);

        final JPanel botoes = transparente(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        botoes.add(j.btLimpar);
        botoes.add(j.btPesquisar);

        final JPanel opcoes = transparente(new BorderLayout(Tema.ESPACO, 0));
        opcoes.add(j.ckAutomaticos, BorderLayout.WEST);
        opcoes.add(botoes, BorderLayout.EAST);

        PainelResponsivo grelha = new PainelResponsivo(new int[]{QUEBRA_FILTROS}, (p, modo) -> {
            if (modo == MODO_ESTREITO) {
                adicionarCampo(p, 0, 0, 1, rotulo("De"), j.dcInicio);
                adicionarCampo(p, 1, 0, 1, rotulo("Até"), j.dcFim);
                adicionarCampo(p, 2, 0, 1, rotulo("Período rápido"), atalhos);
                adicionarCampo(p, 0, 1, 1, rotulo("Utilizador"), j.cbUsuario);
                adicionarCampo(p, 1, 1, 1, rotulo("Módulo"), j.cbModulo);
                adicionarCampo(p, 2, 1, 1, rotulo("Acção"), j.cbAccao);
                adicionarCampo(p, 0, 2, 3, rotulo("Pesquisar"), j.txtPesquisa);
                p.add(opcoes, gbc(0, 6, 3, 1, 1, 0, GridBagConstraints.HORIZONTAL, semMargem()));
            } else {
                adicionarCampo(p, 0, 0, 1, rotulo("De"), j.dcInicio);
                adicionarCampo(p, 1, 0, 1, rotulo("Até"), j.dcFim);
                adicionarCampo(p, 2, 0, 1, rotulo("Período rápido"), atalhos);
                adicionarCampo(p, 3, 0, 1, rotulo("Utilizador"), j.cbUsuario);
                adicionarCampo(p, 4, 0, 1, rotulo("Módulo"), j.cbModulo);
                adicionarCampo(p, 5, 0, 1, rotulo("Acção"), j.cbAccao);
                adicionarCampo(p, 0, 1, 3, rotulo("Pesquisar"), j.txtPesquisa);
                // alinhado com o campo de pesquisa (segunda linha da grelha do par)
                p.add(opcoes, gbc(3, 3, 3, 1, 1, 0, GridBagConstraints.HORIZONTAL, new Insets(0, 12, 12, 0)));
            }
        });
        cartao.add(grelha, BorderLayout.CENTER);
        return cartao;
    }

    private JPanel criarCartaoRegistos() {
        Cartao cartao = new Cartao("Registos", "seleccione uma linha para ver o detalhe", Tema.INFO);
        cartao.setLayout(new BorderLayout());
        cartao.add(new PainelTabela(new RolagemModerna(j.tabela)), BorderLayout.CENTER);
        // altura mínima garantida (cerca de 5 linhas): abaixo disso a janela rola
        cartao.setPreferredSize(new Dimension(600, 300));
        cartao.setMinimumSize(new Dimension(300, 280));
        return cartao;
    }

    private JPanel criarCartaoDetalhe() {
        Cartao cartao = new Cartao("Detalhe do registo", Tema.AVISO);
        cartao.setLayout(new BorderLayout());

        // Informação do registo
        JPanel info = transparente(new GridLayout(2, 2, 14, 10));
        info.add(blocoInfo("Utilizador", j.lbDetUtilizador));
        info.add(blocoInfo("Máquina / IP", j.lbDetMaquina));
        info.add(blocoInfo("Ecrã de origem", j.lbDetEcra));
        info.add(blocoInfo("Tabela / Nº registo", j.lbDetEntidade));

        Caixa descricao = new Caixa(null);
        descricao.setLayout(new BorderLayout());
        descricao.add(j.txtDetDescricao, BorderLayout.CENTER);

        final JPanel resumo = transparente(new BorderLayout(0, 10));
        resumo.add(info, BorderLayout.NORTH);
        resumo.add(descricao, BorderLayout.CENTER);

        // Antes / Depois
        final JPanel antes = caixaValores("ANTES", Tema.PERIGO, j.txtAntes);
        final JPanel depois = caixaValores("DEPOIS", Tema.SUCESSO, j.txtDepois);

        cartao.add(new PainelResponsivo(new int[]{QUEBRA_DETALHE}, (p, modo) -> {
            if (modo == MODO_ESTREITO) {
                // coluna lateral: resumo em cima, Antes | Depois lado a lado com o resto da altura
                p.add(resumo, gbc(0, 0, 2, 1, 1, 0, GridBagConstraints.BOTH, new Insets(0, 0, 10, 0)));
                p.add(antes, gbc(0, 1, 1, 1, 0.5, 1, GridBagConstraints.BOTH, new Insets(0, 0, 0, 8)));
                p.add(depois, gbc(1, 1, 1, 1, 0.5, 1, GridBagConstraints.BOTH, semMargem()));
            } else {
                // por baixo da tabela: resumo | antes | depois lado a lado
                p.add(resumo, gbc(0, 0, 1, 1, 0.4, 1, GridBagConstraints.BOTH, new Insets(0, 0, 0, Tema.ESPACO)));
                p.add(antes, gbc(1, 0, 1, 1, 0.3, 1, GridBagConstraints.BOTH, new Insets(0, 0, 0, 10)));
                p.add(depois, gbc(2, 0, 1, 1, 0.3, 1, GridBagConstraints.BOTH, semMargem()));
            }
        }), BorderLayout.CENTER);

        cartao.fixarLargura(LARGURA_DETALHE);
        cartao.setMinimumSize(new Dimension(LARGURA_DETALHE, 280));
        return cartao;
    }

    private static JPanel blocoInfo(String titulo, JLabel valor) {
        JPanel bloco = transparente(new BorderLayout(0, 3));
        JLabel rotulo = new JLabel(titulo.toUpperCase());
        rotulo.setFont(Tema.TITULO_CAIXA);
        rotulo.setForeground(Tema.TEXTO_SUAVE);
        bloco.add(rotulo, BorderLayout.NORTH);
        bloco.add(valor, BorderLayout.CENTER);
        return bloco;
    }

    // caixa com fundo suave da cor indicada e texto com rolagem
    private static JPanel caixaValores(String titulo, Color cor, JTextArea area) {
        Caixa caixa = new Caixa(titulo, Tema.misturarComBranco(cor, 0.06f), Tema.misturarComBranco(cor, 0.30f));
        caixa.setLayout(new BorderLayout());
        RolagemModerna rolagem = new RolagemModerna(area);
        rolagem.setPreferredSize(new Dimension(100, 90));
        rolagem.setMinimumSize(new Dimension(80, 70));
        caixa.add(rolagem, BorderLayout.CENTER);
        return caixa;
    }

    // ------------------------------------------------------------------
    // Rodapé
    // ------------------------------------------------------------------
    private JPanel criarRodape() {
        JPanel paginacao = transparente(new FlowLayout(FlowLayout.LEFT, 8, 0));
        paginacao.add(j.btPrimeira);
        paginacao.add(j.btAnterior);
        paginacao.add(Box.createHorizontalStrut(4));
        paginacao.add(j.lbPagina);
        paginacao.add(Box.createHorizontalStrut(4));
        paginacao.add(j.btSeguinte);
        paginacao.add(j.btUltima);

        JPanel esquerda = transparente(new BorderLayout(Tema.ESPACO, 0));
        esquerda.add(paginacao, BorderLayout.WEST);
        esquerda.add(j.lbEstado, BorderLayout.CENTER);

        JPanel direita = transparente(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        direita.add(j.ckValoresRelatorio);
        direita.add(j.btImprimir);
        direita.add(j.btFechar);

        return new Rodape(esquerda, direita, QUEBRA_RODAPE);
    }

    private static Insets semMargem() {
        return new Insets(0, 0, 0, 0);
    }
}
