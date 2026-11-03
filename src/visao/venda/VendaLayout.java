package visao.venda;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import visao.PainelLotesVenda;
import visao.design.Cabecalho;
import visao.design.Caixa;
import visao.design.Cartao;
import visao.design.ControloSegmentado;
import visao.design.FundoJanela;
import visao.design.OrdemTab;
import visao.design.PainelResponsivo;
import visao.design.PainelRolavel;
import visao.design.PainelTabela;
import visao.design.RolagemModerna;
import visao.design.Rodape;
import visao.design.Tema;
import static visao.design.Formularios.dimensionar;
import static visao.design.Formularios.gbc;
import static visao.design.Formularios.nota;
import static visao.design.Formularios.rotulo;
import static visao.design.Formularios.rotuloObrigatorio;
import static visao.design.Formularios.seguirVisibilidade;
import static visao.design.Formularios.transparente;

/**
 * Organiza a janela de vendas em cartões e adapta a disposição à largura:
 * <ul>
 * <li>a partir de 980px: produto + itens da venda ocupam a largura livre
 * (a tabela estica até ao fundo); total, cliente e documento ficam numa
 * coluna lateral com rolagem própria;</li>
 * <li>estreita: tudo numa coluna, pela ordem de preenchimento.</li>
 * </ul>
 * O Tab segue sempre a ordem lógica (ver {@link #instalarOrdemTab()}),
 * qualquer que seja a disposição.
 */
final class VendaLayout {

    private static final int QUEBRA_MEDIA = 980;
    private static final int QUEBRA_PRODUTO = 600;
    private static final int QUEBRA_RODAPE = 900;
    private static final int LARGURA_MINIMA = 680;
    private static final int LARGURA_LATERAL = 380;
    private static final int ALTURA_MIN_TABELA = 320;

    private static final int MODO_ESTREITO = 0;

    private final VendaModernaVisao j;

    private JPanel cliente;
    private JPanel documento;
    private JPanel produto;
    private JPanel itens;
    private JPanel total;
    private Cartao lotes;
    private JPanel suporteCorpo;

    VendaLayout(VendaModernaVisao janela) {
        this.j = janela;
    }

    void montar() {
        cliente = criarCartaoCliente();
        documento = criarCartaoDocumento();
        produto = criarCartaoProduto();
        itens = criarCartaoItens();
        total = criarCartaoTotal();

        FundoJanela raiz = new FundoJanela();
        raiz.add(criarCabecalho(), BorderLayout.NORTH);
        raiz.add(criarAreaConteudo(), BorderLayout.CENTER);
        raiz.add(criarRodape(), BorderLayout.SOUTH);
        j.setContentPane(raiz);
        instalarOrdemTab();
        j.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowOpened(java.awt.event.WindowEvent e) {
                // venda começa sempre pela leitura do código de barras
                j.txtCodigoBarra.requestFocusInWindow();
            }
        });

        Rectangle ecra = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        j.setSize(Math.min(1440, ecra.width - 40), Math.min(900, ecra.height - 40));
        j.setMinimumSize(new Dimension(Math.min(760, ecra.width), Math.min(560, ecra.height)));
        j.setLocationRelativeTo(null);
    }

    /**
     * Painel de lotes (só farmácia): entra como cartão na coluna do total.
     */
    void mostrarLotes(PainelLotesVenda painel) {
        painel.setBorder(BorderFactory.createEmptyBorder());
        lotes = new Cartao(null, Tema.AVISO);
        lotes.setLayout(new BorderLayout());
        lotes.add(painel, BorderLayout.CENTER);
        dimensionar(lotes, LARGURA_LATERAL, 300);
        reconstruirCorpo();
    }

    // ------------------------------------------------------------------
    // Cabeçalho
    // ------------------------------------------------------------------
    private JPanel criarCabecalho() {
        Cabecalho cabecalho = new Cabecalho("V", "Ponto de Venda",
                "F4 pesquisar produto  ·  F5 pagamento  ·  F7 facturas pendentes");
        cabecalho.adicionarDireita(j.lb_nome_usuario);
        cabecalho.adicionarDireita(j.jlEmpresa);
        return cabecalho;
    }

    // ------------------------------------------------------------------
    // Conteúdo
    // ------------------------------------------------------------------
    private JComponent criarAreaConteudo() {
        suporteCorpo = transparente(new BorderLayout());
        reconstruirCorpo();

        PainelRolavel conteudo = new PainelRolavel(new BorderLayout(), LARGURA_MINIMA);
        conteudo.setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));
        conteudo.add(suporteCorpo, BorderLayout.CENTER);
        return new RolagemModerna(conteudo);
    }

    private void reconstruirCorpo() {
        suporteCorpo.removeAll();
        suporteCorpo.add(new PainelResponsivo(new int[]{QUEBRA_MEDIA}, (p, modo) -> {
            if (modo == MODO_ESTREITO) {
                p.add(pilha(cliente, documento, produto, itens, total, lotes),
                        gbc(0, 0, 1, 1, 1, 1, GridBagConstraints.BOTH, semMargem()));
            } else {
                // A tabela é o centro da venda: a coluna principal fica com
                // toda a largura livre e a tabela estica até ao fundo; o resto
                // vai para uma coluna lateral com rolagem própria.
                p.add(pilha(produto, itens),
                        gbc(0, 0, 1, 1, 1, 1, GridBagConstraints.BOTH, new Insets(0, 0, 0, Tema.ESPACO)));
                p.add(lateral(pilha(total, cliente, documento, lotes)),
                        gbc(1, 0, 1, 1, 0, 1, GridBagConstraints.BOTH, semMargem()));
            }
        }), BorderLayout.CENTER);
        suporteCorpo.revalidate();
        suporteCorpo.repaint();
    }

    /**
     * Coluna lateral de largura fixa com rolagem própria: assim a sua altura
     * não obriga a janela a rolar e a tabela pode ocupar a altura toda.
     */
    private static JComponent lateral(JPanel coluna) {
        PainelRolavel conteudo = new PainelRolavel(new BorderLayout(), 0);
        conteudo.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 4));
        conteudo.add(coluna, BorderLayout.CENTER);
        RolagemModerna rolagem = new RolagemModerna(conteudo) {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(LARGURA_LATERAL, 200);
            }

            @Override
            public Dimension getMinimumSize() {
                return new Dimension(LARGURA_LATERAL, 120);
            }
        };
        rolagem.setHorizontalScrollBarPolicy(RolagemModerna.HORIZONTAL_SCROLLBAR_NEVER);
        rolagem.getVerticalScrollBar().setUnitIncrement(16);
        return rolagem;
    }

    /**
     * Empilha cartões (ignora os nulos); a tabela de itens, se estiver na
     * pilha, fica com o espaço que sobra; senão estica o último cartão.
     */
    private JPanel pilha(JPanel... cartoes) {
        List<JPanel> lista = new ArrayList<>();
        for (JPanel c : cartoes) {
            if (c != null) {
                lista.add(c);
            }
        }
        JPanel estica = lista.contains(itens) ? itens : lista.get(lista.size() - 1);
        JPanel coluna = transparente(new GridBagLayout());
        for (int i = 0; i < lista.size(); i++) {
            boolean ultimo = i == lista.size() - 1;
            JPanel c = lista.get(i);
            coluna.add(c, gbc(0, i, 1, 1, 1, c == estica ? 1 : 0, GridBagConstraints.BOTH,
                    new Insets(0, 0, ultimo ? 0 : Tema.ESPACO, 0)));
        }
        return coluna;
    }

    // ------------------------------------------------------------------
    // Cartões
    // ------------------------------------------------------------------
    private JPanel criarCartaoCliente() {
        Cartao cartao = new Cartao("Cliente", "quem compra", Tema.PRIMARIA);
        cartao.setLayout(new GridBagLayout());

        JComponent nome = celula(j.lbCliente1, j.txtNomeConsumidorFinal);
        seguirVisibilidade(j.txtNomeConsumidorFinal, nome);

        adicionarLinha(cartao, 0, linha(
                celula(j.lbCliente, j.txtIniciaisCliente), 1,
                nome, 1.6));
        adicionarLinha(cartao, 1, linha(
                celula(j.lbClienteConsumidorFinal, j.txtNifClientePesquisa), 1,
                celula(j.jLabel2, j.txtCodClientePesquisa), 0,
                celula(null, j.jButton5), 0,
                celula(null, j.jButton3), 0));
        adicionarLinha(cartao, 2, linha(celula(rotuloObrigatorio("Cliente"), j.cmbCliente), 1));
        fechar(cartao, 3);
        return cartao;
    }

    private JPanel criarCartaoDocumento() {
        Cartao cartao = new Cartao("Documento", "tipo, datas e impressão", Tema.INFO);
        cartao.setLayout(new GridBagLayout());

        adicionarLinha(cartao, 0, linha(celula(rotulo("Tipo de documento"), j.cmbTipoDocumento), 1));
        adicionarLinha(cartao, 1, linha(
                celula(rotulo("Data"), j.dc_data_documento), 1,
                celula(j.jLabel9, j.dc_data_vencimento), 1));

        JComponent ano = celula(rotulo("Ano económico"), j.cmbAnoEconomico);
        seguirVisibilidade(j.cmbAnoEconomico, ano);
        JComponent vias = celula(rotulo("Vias"), j.spnCopia);
        seguirVisibilidade(j.spnCopia, vias);
        JPanel anoVias = linha(ano, 1, vias, 0);
        esconderSeVazia(anoVias, ano, vias);
        adicionarLinha(cartao, 2, anoVias);

        JPanel formatos = transparente(new GridLayout(2, 1, 0, 6));
        formatos.add(new ControloSegmentado(j.ck_A4, j.ck_Duplicada, j.ck_simplificada, j.ck_simplificada_A7));
        formatos.add(new ControloSegmentado(j.ck_ComVirgula, j.ck_S_A6, j.ck_simplificada_O, j.ck_simplificada_O_S));
        adicionarLinha(cartao, 3, linha(celula(rotulo("Formato de impressão"), formatos), 1));
        fechar(cartao, 4);
        return cartao;
    }

    private JPanel criarCartaoProduto() {
        final Cartao cartao = new Cartao("Adicionar produto", "ler o código ou pesquisar", Tema.PRIMARIA);
        cartao.setLayout(new BorderLayout());

        final JComponent barra = celula(j.lbCodigoProduto4, j.txtCodigoBarra);
        final JComponent interno = celula(j.lbCodigoProduto, j.txtCodigoProduto);
        final JComponent manual = celula(j.lbCodigoProduto2, j.txtCodigoManual);
        final JComponent ref = celula(j.jLabel1, j.txtBuscaRef);
        final JComponent lupa = celula(null, j.jButton4);
        final JComponent subFamilia = celula(rotulo("Sub-família"), j.cmbSubFamilia);
        final JComponent artigo = celula(rotulo("Produto"), j.cmbProduto);
        final JComponent armazem = celula(rotulo("Armazém"), j.cmbArmazem);
        final JComponent stock = celula(j.lbQuantidadeStock, j.txtQuantidadeStock);
        seguirVisibilidade(j.txtQuantidadeStock, stock);
        final JComponent qtd = celula(j.lbQuantidade, j.txtQuatindade);
        final JComponent preco = celula(j.lbCodigoProduto3, j.txtPreco);
        final JComponent accoes = celula(null, botoesLinha());

        cartao.add(new PainelResponsivo(new int[]{QUEBRA_PRODUTO}, (p, modo) -> {
            if (modo == MODO_ESTREITO) {
                adicionarLinha(p, 0, linha(barra, 1, lupa, 0));
                adicionarLinha(p, 1, linha(interno, 1, manual, 1));
                adicionarLinha(p, 2, linha(ref, 1, armazem, 1));
                adicionarLinha(p, 3, linha(subFamilia, 1));
                adicionarLinha(p, 4, linha(artigo, 1));
                adicionarLinha(p, 5, linha(qtd, 0, preco, 1, stock, 0));
                adicionarLinha(p, 6, linha(accoes, 1));
            } else {
                adicionarLinha(p, 0, linha(barra, 2, interno, 1, manual, 1, ref, 1, lupa, 0));
                adicionarLinha(p, 1, linha(subFamilia, 1, artigo, 2.2, armazem, 1, stock, 0));
                adicionarLinha(p, 2, linha(qtd, 0, preco, 1, accoes, 0));
            }
        }), BorderLayout.NORTH);
        return cartao;
    }

    private JPanel botoesLinha() {
        JPanel p = transparente(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        p.add(j.btn_adicionar);
        p.add(j.btn_remover);
        return p;
    }

    private JPanel criarCartaoItens() {
        Cartao cartao = new Cartao("Itens da venda", "produtos adicionados", Tema.PRIMARIA);
        cartao.setLayout(new BorderLayout());
        PainelTabela moldura = new PainelTabela(j.jScrollPane1);
        dimensionar(moldura, 200, ALTURA_MIN_TABELA);
        cartao.add(moldura, BorderLayout.CENTER);
        return cartao;
    }

    private JPanel criarCartaoTotal() {
        Cartao cartao = new Cartao("Total", "pagamento", Tema.SUCESSO);
        cartao.setLayout(new GridBagLayout());

        Caixa destaque = new Caixa(null, Tema.SUCESSO_FUNDO, Tema.SUCESSO_BORDA);
        destaque.setLayout(new BorderLayout(0, 4));
        JLabel rotuloTotal = rotulo("TOTAL A PAGAR");
        rotuloTotal.setForeground(Tema.SUCESSO_TEXTO);
        destaque.add(rotuloTotal, BorderLayout.NORTH);
        destaque.add(j.txtTotalPagar, BorderLayout.CENTER);
        destaque.add(j.lbValorPorExtenco, BorderLayout.SOUTH);

        Caixa geral = new Caixa(null);
        geral.setLayout(new BorderLayout(10, 0));
        geral.add(j.lbCodigoProduto5, BorderLayout.WEST);
        geral.add(j.txtTotalPagarGeral, BorderLayout.CENTER);

        JComponent desconto = celula(j.lbDescontoFinanceiro, j.sp_desconto_financeiro);
        seguirVisibilidade(j.sp_desconto_financeiro, desconto);

        JPanel secundarias = transparente(new GridLayout(1, 2, 10, 0));
        secundarias.add(j.jButton1);
        secundarias.add(j.jButton2);

        int y = 0;
        cartao.add(destaque, gbc(0, y++, 1, 1, 1, 0, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 10, 0)));
        cartao.add(geral, gbc(0, y++, 1, 1, 1, 0, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 12, 0)));
        cartao.add(desconto, gbc(0, y++, 1, 1, 1, 0, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 14, 0)));
        cartao.add(j.btnFormaPagamento, gbc(0, y++, 1, 1, 1, 0, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 8, 0)));
        cartao.add(j.btnProcessar, gbc(0, y++, 1, 1, 1, 0, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 8, 0)));
        cartao.add(secundarias, gbc(0, y++, 1, 1, 1, 0, GridBagConstraints.HORIZONTAL, semMargem()));
        fechar(cartao, y);
        return cartao;
    }

    // ------------------------------------------------------------------
    // Rodapé
    // ------------------------------------------------------------------
    private JPanel criarRodape() {
        JPanel esquerda = transparente(new FlowLayout(FlowLayout.LEFT, 10, 0));
        esquerda.add(j.btn_abertura_dia_venda);
        esquerda.add(j.btn_fecho_dia_venda);
        esquerda.add(j.jButton6);

        JPanel direita = transparente(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        direita.add(nota("* obrigatório"));
        direita.add(j.btnCancelar);

        return new Rodape(esquerda, direita, QUEBRA_RODAPE);
    }

    // ------------------------------------------------------------------
    // Ordem do Tab (independente da disposição)
    // ------------------------------------------------------------------
    private void instalarOrdemTab() {
        OrdemTab ordem = new OrdemTab(
                // cliente
                j.txtIniciaisCliente, j.txtNomeConsumidorFinal, j.txtNifClientePesquisa,
                j.txtCodClientePesquisa, j.jButton5, j.jButton3, j.cmbCliente,
                // documento
                j.cmbTipoDocumento, j.dc_data_documento, j.dc_data_vencimento, j.cmbAnoEconomico, j.spnCopia,
                j.ck_A4, j.ck_Duplicada, j.ck_simplificada, j.ck_simplificada_A7,
                j.ck_ComVirgula, j.ck_S_A6, j.ck_simplificada_O, j.ck_simplificada_O_S,
                // produto
                j.txtCodigoBarra, j.txtCodigoProduto, j.txtCodigoManual, j.txtBuscaRef, j.jButton4,
                j.cmbSubFamilia, j.cmbProduto, j.cmbArmazem, j.txtQuatindade,
                j.btn_adicionar, j.btn_remover, j.table,
                // pagamento
                j.sp_desconto_financeiro, j.btnFormaPagamento, j.btnProcessar, j.jButton1, j.jButton2,
                // rodapé
                j.btn_abertura_dia_venda, j.btn_fecho_dia_venda, j.jButton6, j.btnCancelar);
        // ao abrir, o foco vai para o código de barras (leitura imediata)
        ordem.definirInicial(j.txtCodigoBarra);
        j.setFocusTraversalPolicy(ordem);
    }

    // ------------------------------------------------------------------
    // Auxiliares de grelha
    // ------------------------------------------------------------------
    /** Rótulo em cima, campo em baixo. Sem rótulo, reserva a mesma altura. */
    private static JComponent celula(JComponent rotulo, JComponent campo) {
        JPanel p = transparente(new BorderLayout(0, 5));
        p.add(rotulo != null ? rotulo : rotulo(" "), BorderLayout.NORTH);
        p.add(campo, BorderLayout.CENTER);
        return p;
    }

    /**
     * Linha de células lado a lado. Argumentos em pares (célula, peso): peso
     * 0 mantém a largura preferida, os restantes repartem o espaço.
     */
    private static JPanel linha(Object... pares) {
        JPanel p = transparente(new GridBagLayout());
        for (int i = 0; i < pares.length; i += 2) {
            JComponent c = (JComponent) pares[i];
            double peso = ((Number) pares[i + 1]).doubleValue();
            int esquerda = i == 0 ? 0 : 12;
            p.add(c, gbc(i / 2, 0, 1, 1, peso, 0,
                    peso > 0 ? GridBagConstraints.HORIZONTAL : GridBagConstraints.NONE,
                    new Insets(0, esquerda, 0, 0)));
        }
        return p;
    }

    private static void adicionarLinha(JPanel painel, int y, JComponent linha) {
        painel.add(linha, gbc(0, y, 1, 1, 1, 0, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 12, 0)));
    }

    /** Empurra o conteúdo para cima. */
    private static void fechar(JPanel painel, int y) {
        painel.add(transparente(new BorderLayout()), gbc(0, y, 1, 1, 1, 1, GridBagConstraints.BOTH, semMargem()));
    }

    /** A linha some quando todas as suas células estão ocultas. */
    private static void esconderSeVazia(final JComponent linha, final JComponent... celulas) {
        ComponentAdapter ouvinte = new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                actualizar();
            }

            @Override
            public void componentHidden(ComponentEvent e) {
                actualizar();
            }

            private void actualizar() {
                boolean alguma = false;
                for (Component c : celulas) {
                    alguma |= c.isVisible();
                }
                linha.setVisible(alguma);
            }
        };
        for (JComponent c : celulas) {
            c.addComponentListener(ouvinte);
        }
    }

    private static Insets semMargem() {
        return new Insets(0, 0, 0, 0);
    }
}
