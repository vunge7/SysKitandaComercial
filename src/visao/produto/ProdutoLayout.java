package visao.produto;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;
import visao.design.Cabecalho;
import visao.design.Caixa;
import visao.design.Cartao;
import visao.design.ControloSegmentado;
import visao.design.FundoJanela;
import visao.design.PainelResponsivo;
import visao.design.PainelRolavel;
import visao.design.RolagemModerna;
import visao.design.Rodape;
import visao.design.Tema;
import static visao.design.Formularios.adicionarCampo;
import static visao.design.Formularios.coluna;
import static visao.design.Formularios.dimensionar;
import static visao.design.Formularios.fecharGrade;
import static visao.design.Formularios.gbc;
import static visao.design.Formularios.nota;
import static visao.design.Formularios.rotulo;
import static visao.design.Formularios.transparente;

/**
 * Organiza os componentes da janela de produtos em cartões e adapta a
 * disposição à largura da janela:
 * <ul>
 * <li>larga (&ge; 1280px): três colunas;</li>
 * <li>média (&ge; 900px): formulário à esquerda, preços à direita;</li>
 * <li>estreita: tudo numa coluna.</li>
 * </ul>
 */
final class ProdutoLayout {

    private static final int QUEBRA_MEDIA = 900;
    private static final int QUEBRA_LARGA = 1280;
    private static final int QUEBRA_TOPO = 1100;
    private static final int QUEBRA_RODAPE = 820;
    private static final int LARGURA_MINIMA = 640;

    private static final int MODO_ESTREITO = 0;
    private static final int MODO_MEDIO = 1;

    private final ProdutosModernoVisao j;

    ProdutoLayout(ProdutosModernoVisao janela) {
        this.j = janela;
    }

    void montar() {
        FundoJanela raiz = new FundoJanela();
        raiz.add(criarCabecalho(), BorderLayout.NORTH);
        raiz.add(criarAreaConteudo(), BorderLayout.CENTER);
        raiz.add(criarRodape(), BorderLayout.SOUTH);
        j.setContentPane(raiz);

        Rectangle ecra = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        j.setSize(Math.min(1400, ecra.width - 40), Math.min(900, ecra.height - 40));
        j.setMinimumSize(new Dimension(Math.min(720, ecra.width), Math.min(560, ecra.height)));
    }

    // ------------------------------------------------------------------
    // Cabeçalho
    // ------------------------------------------------------------------
    private JPanel criarCabecalho() {
        Cabecalho cabecalho = new Cabecalho("P", "Cadastro de Produtos e Serviços",
                "Registo, classificação, preços, impostos e stock");
        cabecalho.adicionarDireita(j.jLabelCodProduto);
        cabecalho.adicionarDireita(j.jLabel2);
        return cabecalho;
    }

    // ------------------------------------------------------------------
    // Conteúdo (com rolagem)
    // ------------------------------------------------------------------
    private JComponent criarAreaConteudo() {
        final JPanel pesquisa = montarPesquisa();
        final JPanel armazens = criarCartaoArmazens();
        PainelResponsivo topo = new PainelResponsivo(new int[]{QUEBRA_TOPO}, (p, modo) -> {
            if (modo == MODO_ESTREITO) {
                p.add(coluna(pesquisa, armazens), gbc(0, 0, 1, 1, 1, 1, GridBagConstraints.BOTH, semMargem()));
            } else {
                p.add(pesquisa, gbc(0, 0, 1, 1, 0.5, 1, GridBagConstraints.BOTH, new Insets(0, 0, 0, Tema.ESPACO)));
                p.add(armazens, gbc(1, 0, 1, 1, 0.5, 1, GridBagConstraints.BOTH, semMargem()));
            }
        });

        final JPanel identificacao = criarCartaoIdentificacao();
        final JPanel classificacao = criarCartaoClassificacao();
        final JPanel unidades = criarCartaoUnidades();
        final JPanel impostos = montarImpostos();
        final JPanel stock = montarPainelStock();
        PainelResponsivo corpo = new PainelResponsivo(new int[]{QUEBRA_MEDIA, QUEBRA_LARGA}, (p, modo) -> {
            Insets entreColunas = new Insets(0, 0, 0, Tema.ESPACO);
            if (modo == MODO_ESTREITO) {
                p.add(coluna(identificacao, classificacao, unidades, impostos, stock),
                        gbc(0, 0, 1, 1, 1, 1, GridBagConstraints.BOTH, semMargem()));
            } else if (modo == MODO_MEDIO) {
                p.add(coluna(identificacao, classificacao, unidades, impostos),
                        gbc(0, 0, 1, 1, 1, 1, GridBagConstraints.BOTH, entreColunas));
                p.add(stock, gbc(1, 0, 1, 1, 0, 1, GridBagConstraints.BOTH, semMargem()));
            } else {
                p.add(coluna(identificacao, classificacao),
                        gbc(0, 0, 1, 1, 1, 1, GridBagConstraints.BOTH, entreColunas));
                p.add(coluna(unidades, impostos),
                        gbc(1, 0, 1, 1, 1, 1, GridBagConstraints.BOTH, entreColunas));
                p.add(stock, gbc(2, 0, 1, 1, 0, 1, GridBagConstraints.BOTH, semMargem()));
            }
        });
        j.jPanel5 = corpo;

        PainelRolavel conteudo = new PainelRolavel(new GridBagLayout(), LARGURA_MINIMA);
        conteudo.setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));
        conteudo.add(topo, gbc(0, 0, 1, 1, 1, 0, GridBagConstraints.BOTH, new Insets(0, 0, Tema.ESPACO, 0)));
        conteudo.add(corpo, gbc(0, 1, 1, 1, 1, 1, GridBagConstraints.BOTH, semMargem()));

        return new RolagemModerna(conteudo);
    }

    private JPanel montarPesquisa() {
        JPanel p = j.jPanel3;
        p.setLayout(new GridBagLayout());
        Insets rot = new Insets(0, 0, 4, 10);
        Insets cmp = new Insets(0, 0, 0, 10);
        dimensionar(j.txtCodigoProduto, 96, Tema.ALTURA_CAMPO);
        dimensionar(j.jButton4, 40, Tema.ALTURA_CAMPO);
        dimensionar(j.txtCodigoBarraProcura, 110, Tema.ALTURA_CAMPO);
        dimensionar(j.txtCodigoManualProcura, 110, Tema.ALTURA_CAMPO);
        dimensionar(j.btLupaEconomato, j.btLupaEconomato.getPreferredSize().width, Tema.ALTURA_CAMPO);

        p.add(j.lbProdutos2, gbc(0, 0, 2, 1, 0, 0, GridBagConstraints.NONE, rot));
        p.add(j.lbProdutos3, gbc(2, 0, 1, 1, 0, 0, GridBagConstraints.NONE, rot));
        p.add(j.lbProdutos4, gbc(3, 0, 1, 1, 0, 0, GridBagConstraints.NONE, rot));

        p.add(j.txtCodigoProduto, gbc(0, 1, 1, 1, 0, 0, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 4)));
        p.add(j.jButton4, gbc(1, 1, 1, 1, 0, 0, GridBagConstraints.NONE, cmp));
        p.add(j.txtCodigoBarraProcura, gbc(2, 1, 1, 1, 1, 0, GridBagConstraints.HORIZONTAL, cmp));
        p.add(j.txtCodigoManualProcura, gbc(3, 1, 1, 1, 1, 0, GridBagConstraints.HORIZONTAL, cmp));
        p.add(j.btLupaEconomato, gbc(4, 1, 1, 1, 0, 0, GridBagConstraints.NONE, semMargem()));
        p.add(j.rbJanelaServico, gbc(5, 1, 1, 1, 0, 0, GridBagConstraints.NONE, semMargem()));
        fecharGrade(p, 2, 6);
        return p;
    }

    private JPanel criarCartaoArmazens() {
        Cartao cartao = new Cartao("Stock por Armazém", "quantidades actuais", Tema.INFO);
        cartao.setLayout(new GridBagLayout());
        JComboBox[] combos = {j.cmbarmazem1, j.cmbarmazem2, j.cmbarmazem3, j.cmbarmazem4};
        JTextField[] qtds = {j.txtArmazen1, j.txtArmazen2, j.txtArmazen3, j.txtArmazen4};
        for (int i = 0; i < combos.length; i++) {
            JPanel chip = transparente(new BorderLayout(6, 0));
            chip.add(combos[i], BorderLayout.CENTER);
            chip.add(qtds[i], BorderLayout.EAST);
            int col = i % 2;
            int linha = i / 2;
            cartao.add(chip, gbc(col, linha, 1, 1, 1, 0, GridBagConstraints.HORIZONTAL,
                    new Insets(0, 0, linha == 0 ? 8 : 0, 12)));
        }
        cartao.add(j.jButtonCompras, gbc(2, 0, 1, 2, 0, 0, GridBagConstraints.BOTH, semMargem()));
        fecharGrade(cartao, 2, 3);
        return cartao;
    }

    // ------------------------------------------------------------------
    // Cartões do corpo
    // ------------------------------------------------------------------
    private JPanel criarCartaoIdentificacao() {
        Cartao cartao = new Cartao("Identificação", "nome, códigos e fotografia", Tema.PRIMARIA);
        cartao.setLayout(new GridBagLayout());

        ControloSegmentado segmentado = new ControloSegmentado(j.ck_produto, j.ck_servico);
        JPanel tipo = transparente(new BorderLayout());
        tipo.add(segmentado, BorderLayout.WEST);
        dimensionar(tipo, segmentado.getPreferredSize().width, Tema.ALTURA_CAMPO);

        adicionarCampo(cartao, 0, 0, 1, rotulo("Tipo"), tipo);
        adicionarCampo(cartao, 1, 0, 1, j.lbBarra, j.txtCodigoBarra);
        adicionarCampo(cartao, 0, 1, 2, j.lbProduto, j.jScrollPane1);

        cartao.add(j.jPanel4, gbc(2, 0, 1, 4, 0, 0, GridBagConstraints.BOTH, new Insets(0, Tema.ESPACO, 8, 0)));
        cartao.add(j.btnCarregar, gbc(2, 4, 1, 1, 0, 0, GridBagConstraints.HORIZONTAL, new Insets(0, Tema.ESPACO, 0, 0)));
        fecharGrade(cartao, 5, 3);
        return cartao;
    }

    private JPanel criarCartaoClassificacao() {
        Cartao cartao = new Cartao("Classificação", "marca, família e grupo", Tema.PRIMARIA);
        cartao.setLayout(new GridBagLayout());

        JPanel subFamilia = transparente(new BorderLayout(6, 0));
        subFamilia.add(j.cmbTipoProduto, BorderLayout.CENTER);
        subFamilia.add(j.jButton1, BorderLayout.EAST);

        adicionarCampo(cartao, 0, 0, 1, j.lbTipoProduto2, j.cmbMarca);
        adicionarCampo(cartao, 1, 0, 1, j.lbTipoProduto3, j.cmbModelo);
        adicionarCampo(cartao, 0, 1, 1, j.lbTipoProduto4, j.cmbFamilia);
        adicionarCampo(cartao, 1, 1, 1, j.lbTipoProduto, subFamilia);
        adicionarCampo(cartao, 0, 2, 1, j.lbTipoProduto5, j.cmbGrupo);
        fecharGrade(cartao, 6, 2);
        return cartao;
    }

    private JPanel criarCartaoUnidades() {
        Cartao cartao = new Cartao("Unidades e Custo", "compra e conversão", Tema.PRIMARIA);
        cartao.setLayout(new GridBagLayout());

        adicionarCampo(cartao, 0, 0, 1, j.label_qtd_bruta, j.txtUnidadeCompra);
        adicionarCampo(cartao, 1, 0, 1, j.label_factor_conversao, j.txtFactorConversao);
        adicionarCampo(cartao, 0, 1, 1, j.lbUnidade, j.cmbUnidade);
        adicionarCampo(cartao, 1, 1, 1, j.lbCusto, j.txtPrecoCompra);

        JPanel opcoes = transparente(new FlowLayout(FlowLayout.LEFT, 0, 0));
        opcoes.add(j.rbNaoEnviarCozinha);
        opcoes.add(Box.createHorizontalStrut(10));
        opcoes.add(j.rbEnviarCozinha);
        opcoes.add(Box.createHorizontalStrut(10));
        opcoes.add(j.rbEnviarSala);
        JPanel tickets = transparente(new BorderLayout(8, 0));
        tickets.add(opcoes, BorderLayout.CENTER);
        tickets.add(j.btnAssociar, BorderLayout.EAST);
        adicionarCampo(cartao, 0, 2, 2, j.lbTicket, tickets);
        fecharGrade(cartao, 6, 2);
        return cartao;
    }

    private JPanel montarImpostos() {
        // Caixa "Aplicar IVA"
        JPanel aplicar = j.pnPainelAplicar;
        aplicar.setLayout(new GridBagLayout());
        aplicar.add(j.ivaAplicarJRadioButton, gbc(0, 0, 2, 1, 0, 0, GridBagConstraints.NONE, new Insets(0, 0, 8, 0)));
        aplicar.add(j.ivaTaxaJLabel, gbc(0, 1, 1, 1, 0, 0, GridBagConstraints.NONE, new Insets(0, 0, 4, 10)));
        aplicar.add(j.TotalIvaLabel, gbc(1, 1, 1, 1, 0, 0, GridBagConstraints.NONE, new Insets(0, 0, 4, 0)));
        aplicar.add(j.cmbImposto, gbc(0, 2, 1, 1, 1, 0, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 10)));
        aplicar.add(j.txtPrecoDeVendaComIva, gbc(1, 2, 1, 1, 1, 0, GridBagConstraints.HORIZONTAL, semMargem()));
        fecharGrade(aplicar, 3, 2);

        // Caixa "Isento de IVA"
        JPanel isento = j.jPanel6;
        isento.setLayout(new GridBagLayout());
        isento.add(j.ivaNaoAplicarJRadioButton, gbc(0, 0, 1, 1, 0, 0, GridBagConstraints.NONE, new Insets(0, 0, 8, 0)));
        isento.add(j.ivaMotivoJLabel, gbc(0, 1, 1, 1, 0, 0, GridBagConstraints.NONE, new Insets(0, 0, 4, 0)));
        isento.add(j.ivaMotivoJComboBox, gbc(0, 2, 1, 1, 1, 0, GridBagConstraints.HORIZONTAL, semMargem()));
        fecharGrade(isento, 3, 1);

        j.ivaJPanel.add(aplicar, gbc(0, 0, 1, 1, 0.5, 1, GridBagConstraints.BOTH, new Insets(0, 0, 0, 10)));
        j.ivaJPanel.add(isento, gbc(1, 0, 1, 1, 0.5, 1, GridBagConstraints.BOTH, semMargem()));

        // Caixa "Retenção na fonte"
        JPanel retencao = j.jPanel_retencao;
        retencao.setLayout(new GridBagLayout());
        retencao.add(j.retencaoAplicarJRadioButton, gbc(0, 0, 1, 1, 0, 0, GridBagConstraints.NONE, new Insets(0, 0, 6, 12)));
        retencao.add(j.retencaoTaxaJLabel, gbc(1, 0, 1, 1, 0, 0, GridBagConstraints.NONE, new Insets(0, 0, 6, 8)));
        retencao.add(j.retencaoTaxaJTextField, gbc(2, 0, 1, 1, 0, 0, GridBagConstraints.NONE, new Insets(0, 0, 6, 0)));
        retencao.add(j.retencaoNaoAplicarJRadioButton, gbc(0, 1, 1, 1, 0, 0, GridBagConstraints.NONE, new Insets(0, 0, 0, 12)));
        retencao.add(j.retencaoZeroTaxaJTextField, gbc(2, 1, 1, 1, 0, 0, GridBagConstraints.NONE, semMargem()));
        fecharGrade(retencao, 2, 4);

        JPanel cartao = j.jPanel1;
        cartao.setLayout(new GridBagLayout());
        cartao.add(j.ivaJPanel, gbc(0, 0, 1, 1, 1, 0, GridBagConstraints.BOTH, new Insets(0, 0, 10, 0)));
        cartao.add(retencao, gbc(0, 1, 1, 1, 1, 0, GridBagConstraints.BOTH, semMargem()));
        fecharGrade(cartao, 2, 1);
        return cartao;
    }

    private JPanel montarPainelStock() {
        JPanel p = j.painel_stock;
        p.setLayout(new GridBagLayout());

        p.add(j.jButton5, gbc(0, 0, 2, 1, 1, 0, GridBagConstraints.HORIZONTAL, new Insets(0, 0, Tema.ESPACO, 0)));

        JPanel lotes = transparente(new FlowLayout(FlowLayout.LEFT, 0, 0));
        lotes.add(j.jLabel3);
        lotes.add(Box.createHorizontalStrut(14));
        lotes.add(j.jRadioButtonComLotes);
        lotes.add(Box.createHorizontalStrut(8));
        lotes.add(j.jRadioButtonSemLotes);
        p.add(lotes, gbc(0, 1, 2, 1, 1, 0, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 12, 0)));

        // adicionarCampo usa duas linhas da grelha por "linha" lógica: 1 -> 2,3; 2 -> 4,5
        adicionarCampo(p, 0, 1, 2, j.lbCategoria3, j.cmbLocal);
        adicionarCampo(p, 0, 2, 1, j.lbBarra1, j.txtCodigoManual);
        adicionarCampo(p, 1, 2, 1, j.lbPreco, j.txtPercentagemGanhoRetalho);

        Caixa destaque = new Caixa(null, Tema.SUCESSO_FUNDO, Tema.SUCESSO_BORDA);
        destaque.setLayout(new BorderLayout(0, 6));
        destaque.add(j.lbPrecoVenda, BorderLayout.NORTH);
        destaque.add(j.txtPrecoVendaRetalho, BorderLayout.CENTER);
        p.add(destaque, gbc(0, 6, 2, 1, 1, 0, GridBagConstraints.HORIZONTAL, new Insets(0, 0, Tema.ESPACO, 0)));

        adicionarCampo(p, 0, 4, 1, j.jLabel1, j.txtQtdGrosso);
        adicionarCampo(p, 1, 4, 1, j.lbPrecoVenda1, j.txtPrecoVendaGrosso);
        fecharGrade(p, 10, 2);
        return p;
    }

    // ------------------------------------------------------------------
    // Rodapé
    // ------------------------------------------------------------------
    private JPanel criarRodape() {
        JPanel esquerda = transparente(new FlowLayout(FlowLayout.LEFT, 10, 0));
        esquerda.add(j.jButton2);
        esquerda.add(j.btnLimpar);
        esquerda.add(Box.createHorizontalStrut(6));
        esquerda.add(nota("* campo obrigatório"));

        JPanel direita = transparente(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        direita.add(j.jButton3);
        direita.add(j.btnAlterar2);
        direita.add(j.btnSalvar);
        direita.add(j.btnCancelar);

        return new Rodape(esquerda, direita, QUEBRA_RODAPE);
    }

    private static Insets semMargem() {
        return new Insets(0, 0, 0, 0);
    }
}
