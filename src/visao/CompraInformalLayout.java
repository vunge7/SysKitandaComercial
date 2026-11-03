package visao;

import java.awt.BorderLayout;
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
import visao.design.Cabecalho;
import visao.design.Caixa;
import visao.design.Cartao;
import visao.design.FundoJanela;
import visao.design.PainelResponsivo;
import visao.design.PainelRolavel;
import visao.design.PainelTabela;
import visao.design.RolagemModerna;
import visao.design.Rodape;
import visao.design.Tema;
import static visao.design.Formularios.adicionarCampo;
import static visao.design.Formularios.coluna;
import static visao.design.Formularios.dimensionar;
import static visao.design.Formularios.fecharGrade;
import static visao.design.Formularios.gbc;
import static visao.design.Formularios.nota;
import static visao.design.Formularios.seguirVisibilidade;
import static visao.design.Formularios.transparente;

/**
 * Organiza a janela de compras em cartões e adapta-a à largura:
 * <ul>
 * <li>larga (&ge; 1180px): Documento | Produto em cima, Itens | Totais em baixo;</li>
 * <li>estreita: tudo numa coluna, pela ordem de preenchimento.</li>
 * </ul>
 * O formulário do produto passa de 4 para 2 colunas abaixo de 760px.
 */
final class CompraInformalLayout {

    private static final int QUEBRA_LARGA = 1180;
    private static final int QUEBRA_PRODUTO = 760;
    private static final int QUEBRA_RODAPE = 900;
    private static final int LARGURA_MINIMA = 640;
    private static final int LARGURA_DOCUMENTO = 380;
    private static final int LARGURA_TOTAIS = 360;

    private static final int MODO_ESTREITO = 0;

    private final CompraInformalVisao j;

    CompraInformalLayout(CompraInformalVisao janela) {
        this.j = janela;
    }

    void montar() {
        FundoJanela raiz = new FundoJanela();
        raiz.add(criarCabecalho(), BorderLayout.NORTH);
        raiz.add(criarConteudo(), BorderLayout.CENTER);
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
        Cabecalho cabecalho = new Cabecalho("C", "Registo de Compras",
                "Entrada de mercadoria: fornecedor, produtos, lotes, preços e stock");
        cabecalho.adicionarDireita(j.lb_proximo_documento);
        j.jPanel6 = cabecalho;
        return cabecalho;
    }

    // ------------------------------------------------------------------
    // Conteúdo (com rolagem em ecrãs baixos)
    // ------------------------------------------------------------------
    private JComponent criarConteudo() {
        final JPanel documento = criarCartaoDocumento();
        final JPanel produto = criarCartaoProduto();
        PainelResponsivo topo = new PainelResponsivo(new int[]{QUEBRA_LARGA}, (p, modo) -> {
            if (modo == MODO_ESTREITO) {
                p.add(coluna(documento, produto), gbc(0, 0, 1, 1, 1, 1, GridBagConstraints.BOTH, semMargem()));
            } else {
                p.add(documento, gbc(0, 0, 1, 1, 0, 1, GridBagConstraints.BOTH, new Insets(0, 0, 0, Tema.ESPACO)));
                p.add(produto, gbc(1, 0, 1, 1, 1, 1, GridBagConstraints.BOTH, semMargem()));
            }
        });

        final JPanel itens = criarCartaoItens();
        final JPanel totais = criarCartaoTotais();
        PainelResponsivo baixo = new PainelResponsivo(new int[]{QUEBRA_LARGA}, (p, modo) -> {
            if (modo == MODO_ESTREITO) {
                p.add(itens, gbc(0, 0, 1, 1, 1, 1, GridBagConstraints.BOTH, new Insets(0, 0, Tema.ESPACO, 0)));
                p.add(totais, gbc(0, 1, 1, 1, 1, 0, GridBagConstraints.BOTH, semMargem()));
            } else {
                p.add(itens, gbc(0, 0, 1, 1, 1, 1, GridBagConstraints.BOTH, new Insets(0, 0, 0, Tema.ESPACO)));
                p.add(totais, gbc(1, 0, 1, 1, 0, 1, GridBagConstraints.BOTH, semMargem()));
            }
        });

        PainelRolavel conteudo = new PainelRolavel(new GridBagLayout(), LARGURA_MINIMA);
        conteudo.setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));
        conteudo.add(topo, gbc(0, 0, 1, 1, 1, 0, GridBagConstraints.BOTH, new Insets(0, 0, Tema.ESPACO, 0)));
        conteudo.add(baixo, gbc(0, 1, 1, 1, 1, 1, GridBagConstraints.BOTH, semMargem()));
        return new RolagemModerna(conteudo);
    }

    private JPanel criarCartaoDocumento() {
        Cartao cartao = new Cartao("Documento", "fornecedor e descarga", Tema.PRIMARIA);
        cartao.setLayout(new GridBagLayout());
        cartao.fixarLargura(LARGURA_DOCUMENTO);

        adicionarCampo(cartao, 0, 0, 1, j.lbPreco1, j.cmbArmazem);
        adicionarCampo(cartao, 1, 0, 1, j.lb_ano_encomico, j.cmbAnoEconomico);
        adicionarCampo(cartao, 0, 1, 2, j.lb_fornecedor_nome1, j.cmbFornecedor);

        Caixa contactos = new Caixa("CONTACTOS DO FORNECEDOR");
        contactos.setLayout(new GridLayout(3, 1, 0, 4));
        contactos.add(j.lb_fornecedor_endereco);
        contactos.add(j.lb_fornecedor_telefone);
        contactos.add(j.lb_fornecedor_email);
        cartao.add(contactos, gbc(0, 4, 2, 1, 1, 0, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0)));

        // cmbTipoDocumento fica oculto pela lógica; mantido para não alterar o comportamento
        cartao.add(j.cmbTipoDocumento, gbc(0, 5, 2, 1, 0, 0, GridBagConstraints.NONE, semMargem()));
        fecharGrade(cartao, 6, 2);
        return cartao;
    }

    private JPanel criarCartaoProduto() {
        final Cartao cartao = new Cartao("Produto", "pesquisa, preços, quantidades e lote", Tema.INFO);
        cartao.setLayout(new BorderLayout());

        final JPanel codigo = transparente(new BorderLayout(6, 0));
        codigo.add(j.txtCodigoInterno, BorderLayout.CENTER);
        codigo.add(j.jButtonLupa, BorderLayout.EAST);
        dimensionar(j.txtCodigoInterno, 80, Tema.ALTURA_CAMPO);

        // Produto escolhido, em destaque
        final Caixa seleccionado = new Caixa(null, Tema.SUCESSO_FUNDO, Tema.SUCESSO_BORDA);
        seleccionado.setLayout(new BorderLayout(0, 2));
        JLabel tituloSeleccionado = new JLabel("PRODUTO SELECCIONADO");
        tituloSeleccionado.setFont(Tema.TITULO_CAIXA);
        tituloSeleccionado.setForeground(Tema.SUCESSO_TEXTO);
        seleccionado.add(tituloSeleccionado, BorderLayout.NORTH);
        seleccionado.add(j.lb_produto, BorderLayout.CENTER);

        // Lote (escondido inteiro quando a lógica esconde o número do lote)
        final Caixa lote = new Caixa("LOTE");
        lote.setLayout(new GridBagLayout());
        adicionarCampo(lote, 0, 0, 1, j.llbDataFabrico, j.txtNumeroLote);
        adicionarCampo(lote, 1, 0, 1, j.llbDataFabrico1, j.jcDataFabrico);
        adicionarCampo(lote, 2, 0, 1, j.lbDataExpiracao, j.jcDataValidade);
        seguirVisibilidade(j.txtNumeroLote, lote);

        final JPanel botoes = transparente(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        botoes.add(j.jButton_eliminar);
        botoes.add(j.jButton_inserir);

        cartao.add(new PainelResponsivo(new int[]{QUEBRA_PRODUTO}, (p, modo) -> {
            if (modo == MODO_ESTREITO) {
                adicionarCampo(p, 0, 0, 1, j.lbProdutos2, codigo);
                adicionarCampo(p, 1, 0, 1, j.lb_data_apuramento5, j.txtCodBarra);
                p.add(seleccionado, gbc(0, 2, 2, 1, 1, 0, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 12, 0)));
                adicionarCampo(p, 0, 2, 1, j.lb_data_apuramento1, j.txtQtdExistente);
                adicionarCampo(p, 1, 2, 1, j.lb_data_apuramento3, j.txtFactorConversao);
                adicionarCampo(p, 0, 3, 1, j.lb_data_apuramento10, j.spPrecoCompra);
                adicionarCampo(p, 1, 3, 1, j.lb_data_apuramento9, j.txtPercentagemGanho);
                adicionarCampo(p, 0, 4, 1, j.lb_data_apuramento7, j.txtPrecoVenda);
                adicionarCampo(p, 1, 4, 1, j.lb_data_apuramento2, j.txtQtdEntrar);
                adicionarCampo(p, 0, 5, 1, j.lb_data_apuramento8, j.txtQtdCritica);
                adicionarCampo(p, 1, 5, 1, j.lb_data_apuramento6, j.txtQtdBaixa);
                p.add(lote, gbc(0, 12, 2, 1, 1, 0, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 12, 0)));
                p.add(botoes, gbc(0, 13, 2, 1, 1, 0, GridBagConstraints.HORIZONTAL, semMargem()));
            } else {
                adicionarCampo(p, 0, 0, 1, j.lbProdutos2, codigo);
                adicionarCampo(p, 1, 0, 1, j.lb_data_apuramento5, j.txtCodBarra);
                adicionarCampo(p, 2, 0, 1, j.lb_data_apuramento1, j.txtQtdExistente);
                adicionarCampo(p, 3, 0, 1, j.lb_data_apuramento3, j.txtFactorConversao);
                p.add(seleccionado, gbc(0, 2, 4, 1, 1, 0, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 12, 0)));
                adicionarCampo(p, 0, 2, 1, j.lb_data_apuramento10, j.spPrecoCompra);
                adicionarCampo(p, 1, 2, 1, j.lb_data_apuramento9, j.txtPercentagemGanho);
                adicionarCampo(p, 2, 2, 1, j.lb_data_apuramento7, j.txtPrecoVenda);
                adicionarCampo(p, 3, 2, 1, j.lb_data_apuramento2, j.txtQtdEntrar);
                adicionarCampo(p, 0, 3, 1, j.lb_data_apuramento8, j.txtQtdCritica);
                adicionarCampo(p, 1, 3, 1, j.lb_data_apuramento6, j.txtQtdBaixa);
                p.add(lote, gbc(0, 8, 4, 1, 1, 0, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 12, 0)));
                p.add(botoes, gbc(0, 9, 4, 1, 1, 0, GridBagConstraints.HORIZONTAL, semMargem()));
            }
        }), BorderLayout.CENTER);
        j.jPanel3 = cartao;
        return cartao;
    }

    private JPanel criarCartaoItens() {
        Cartao cartao = new Cartao("Itens da Compra", "duplo clique para editar preço, quantidade, desconto ou taxa", Tema.PRIMARIA);
        cartao.setLayout(new BorderLayout());
        cartao.add(new PainelTabela(j.jScrollPane1), BorderLayout.CENTER);
        // altura mínima (cerca de 6 linhas): abaixo disso a janela rola
        cartao.setPreferredSize(new Dimension(600, 330));
        cartao.setMinimumSize(new Dimension(300, 300));
        j.jPanel2 = cartao;
        return cartao;
    }

    private JPanel criarCartaoTotais() {
        Cartao cartao = new Cartao("Totais", Tema.SUCESSO);
        cartao.setLayout(new GridBagLayout());
        cartao.fixarLargura(LARGURA_TOTAIS);

        Caixa parciais = new Caixa(null);
        parciais.setLayout(new GridBagLayout());
        linhaTotal(parciais, 0, j.lbTotalPagar2, j.txtTotal_AOA_Iliquido);
        linhaTotal(parciais, 1, j.lbTotalPagar, j.txtTotal_AOA_Desconto);
        linhaTotal(parciais, 2, j.lbTotalPagar3, j.txtTotal_AOA_IVA);
        cartao.add(parciais, gbc(0, 0, 1, 1, 1, 0, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 12, 0)));

        Caixa total = new Caixa(null, Tema.SUCESSO_FUNDO, Tema.SUCESSO_BORDA);
        total.setLayout(new BorderLayout(0, 4));
        total.add(j.lbTotalPagar1, BorderLayout.NORTH);
        total.add(j.txtTotal_AOA_liquido, BorderLayout.CENTER);
        cartao.add(total, gbc(0, 1, 1, 1, 1, 0, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 10, 0)));

        // valor por extenso (ex.: "Kwanzas: cento e vinte mil...")
        JPanel extenso = transparente(new BorderLayout(0, 2));
        extenso.add(j.lbLegenda, BorderLayout.NORTH);
        extenso.add(j.lbValorPorExtenco, BorderLayout.CENTER);
        cartao.add(extenso, gbc(0, 2, 1, 1, 1, 0, GridBagConstraints.HORIZONTAL, semMargem()));
        fecharGrade(cartao, 3, 1);
        j.jPanel8 = cartao;
        return cartao;
    }

    private static void linhaTotal(JPanel painel, int linha, JLabel rotulo, JComponent valor) {
        painel.add(rotulo, gbc(0, linha, 1, 1, 0, 0, GridBagConstraints.NONE, new Insets(0, 0, linha < 2 ? 8 : 0, 12)));
        painel.add(valor, gbc(1, linha, 1, 1, 1, 0, GridBagConstraints.HORIZONTAL, new Insets(0, 0, linha < 2 ? 8 : 0, 0)));
    }

    // ------------------------------------------------------------------
    // Rodapé
    // ------------------------------------------------------------------
    private JPanel criarRodape() {
        JPanel esquerda = transparente(new FlowLayout(FlowLayout.LEFT, 10, 0));
        esquerda.add(j.btTransferenciaArmazem);
        esquerda.add(Box.createHorizontalStrut(6));
        esquerda.add(nota("F1: pesquisar produto   •   Enter na quantidade: adicionar   •   * obrigatório"));

        JPanel direita = transparente(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        direita.add(j.btnCancelar);
        direita.add(j.jButton4);

        return new Rodape(esquerda, direita, QUEBRA_RODAPE);
    }

    private static Insets semMargem() {
        return new Insets(0, 0, 0, 0);
    }
}
