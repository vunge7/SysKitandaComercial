package visao;

import java.awt.BorderLayout;
import java.awt.Font;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import visao.design.BotaoModerno;
import visao.design.CampoData;
import visao.design.CampoIndicador;
import visao.design.CampoPesquisa;
import visao.design.ComboModerno;
import visao.design.Formularios;
import visao.design.IconeLinha;
import visao.design.Mostrador;
import visao.design.RolagemModerna;
import visao.design.Selo;
import visao.design.SpinnerModerno;
import visao.design.TabelaModerna;
import visao.design.Tema;
import static visao.design.Formularios.campoTexto;
import static visao.design.Formularios.descrever;
import static visao.design.Formularios.dimensionar;
import static visao.design.Formularios.estiloCombo;
import static visao.design.Formularios.rotulo;
import static visao.design.Formularios.rotuloObrigatorio;

/**
 * Cria e configura os componentes da janela de compras (textos, fontes,
 * ícones e estados iniciais). A disposição no ecrã fica a cargo de
 * {@link CompraInformalLayout}.
 * <p>
 * Os nomes dos componentes são os do antigo .form, porque a lógica da
 * compra (CompraInformalVisao) e outras janelas usam-nos directamente.
 */
final class CompraInformalComponentes {

    private final CompraInformalVisao j;

    CompraInformalComponentes(CompraInformalVisao janela) {
        this.j = janela;
    }

    void criar() {
        criarDiversos();
        criarCabecalho();
        criarDocumento();
        criarProduto();
        criarLote();
        criarTabela();
        criarTotais();
        criarAccoes();
        criarPesquisaAntiga();
    }

    private void criarDiversos() {
        j.jMenuItem1 = new JMenuItem("jMenuItem1");
        j.buttonGroup3 = new ButtonGroup();
        j.jSeparator1 = new JSeparator(SwingConstants.VERTICAL);
    }

    private void criarCabecalho() {
        j.jLabel2 = new JLabel("REGISTO DE COMPRAS");
        // "PRÓX.DOC. : FT A/12" — preenchido pela lógica ao activar a janela
        j.lb_proximo_documento = new Selo("");
        j.lb_proximo_documento.setToolTipText("Número do próximo documento de compra");
    }

    private void criarDocumento() {
        j.lbPreco1 = rotulo("Local de Descarga");
        j.cmbArmazem = new ComboModerno();
        estiloCombo(j.cmbArmazem);
        j.cmbArmazem.setToolTipText("Armazém onde a mercadoria dá entrada");

        j.lb_ano_encomico = rotulo("Ano Económico");
        j.cmbAnoEconomico = new ComboModerno<>(new DefaultComboBoxModel<>(new String[]{"Item 1", "Item 2", "Item 3", "Item 4"}));
        estiloCombo(j.cmbAnoEconomico);
        j.cmbAnoEconomico.setEnabled(false);

        j.cmbTipoDocumento = new ComboModerno<>(new DefaultComboBoxModel<>(new String[]{"Item 1", "Item 2", "Item 3", "Item 4"}));
        estiloCombo(j.cmbTipoDocumento);

        j.lb_fornecedor_nome1 = rotuloObrigatorio("Fornecedor");
        j.cmbFornecedor = new ComboModerno<>(new DefaultComboBoxModel<>(new String[]{"Item 1", "Item 2", "Item 3", "Item 4"}));
        estiloCombo(j.cmbFornecedor);

        // Contactos do fornecedor (texto definido pela lógica)
        j.lb_fornecedor_endereco = textoInfo();
        j.lb_fornecedor_telefone = textoInfo();
        j.lb_fornecedor_email = textoInfo();

        // Rótulos herdados que a lógica só limpa; ficam disponíveis mas discretos
        j.lb_data_apuramento = textoInfo();
        j.lb_data_solicitacao = textoInfo();
        j.lb_numero_artigos = textoInfo();
        j.lb_usuario = textoInfo();
        j.lb_usuario.setText("Conta:");
    }

    private void criarProduto() {
        j.lbProdutos2 = rotulo("Cód. Interno");
        j.txtCodigoInterno = campoTexto(Tema.CAMPO);
        j.txtCodigoInterno.setToolTipText("Código interno do produto (Enter para procurar)");
        j.jButtonLupa = botaoIcone(IconeLinha.Tipo.PESQUISAR, Tema.PRIMARIA, false);
        descrever(j.jButtonLupa, "Pesquisar produto", "F1");

        j.lb_data_apuramento5 = rotulo("Cód. Barra");
        j.txtCodBarra = campoTexto(Tema.CAMPO);
        j.txtCodBarra.setToolTipText("Leia ou escreva o código de barras (Enter para procurar)");

        j.lb_data_apuramento1 = rotulo("Qtd. Existente");
        j.txtQtdExistente = new CampoIndicador();
        j.txtQtdExistente.setToolTipText("Quantidade actual em stock no armazém de descarga");
        dimensionar(j.txtQtdExistente, 90, Tema.ALTURA_CAMPO);

        j.lb_data_apuramento3 = rotulo("Factor Conversão");
        j.txtFactorConversao = campoTexto(Tema.CAMPO);
        j.txtFactorConversao.setEditable(false);
        j.txtFactorConversao.setHorizontalAlignment(JTextField.RIGHT);

        j.lb_produto = new JLabel(" ");
        j.lb_produto.setFont(new Font("Segoe UI", Font.BOLD, 16));
        j.lb_produto.setForeground(Tema.SUCESSO_TEXTO);

        j.lb_data_apuramento10 = rotulo("Preço Compra / Médio");
        j.spPrecoCompra = new SpinnerModerno();
        j.spPrecoCompra.setToolTipText("Preço de compra unitário (o sistema calcula o preço médio)");
        dimensionar(j.spPrecoCompra, 120, Tema.ALTURA_CAMPO);

        j.lb_data_apuramento9 = rotulo("Margem de Ganho (%)");
        j.txtPercentagemGanho = numero(Tema.CAMPO);

        j.lb_data_apuramento7 = rotulo("Preço Venda");
        j.txtPrecoVenda = numero(Tema.VALOR);
        j.txtPrecoVenda.setForeground(Tema.SUCESSO_TEXTO);

        j.lb_data_apuramento2 = rotuloObrigatorio("Qtd. a Entrar");
        j.txtQtdEntrar = numero(Tema.VALOR);
        j.txtQtdEntrar.setToolTipText("Quantidade a dar entrada (Enter adiciona à compra)");

        j.lb_data_apuramento8 = rotulo("Qtd. Crítica");
        j.txtQtdCritica = numero(Tema.CAMPO);
        j.txtQtdCritica.setText("10");

        j.lb_data_apuramento6 = rotulo("Qtd. Baixa");
        j.txtQtdBaixa = numero(Tema.CAMPO);
    }

    private void criarLote() {
        // Os textos contêm "numero lote", "fabrico" e "validade": a lógica
        // (configurarModoLotes) esconde-os pelo texto nos negócios sem lotes.
        j.llbDataFabrico = rotulo("Número Lote");
        j.txtNumeroLote = campoTexto(Tema.CAMPO);
        j.txtNumeroLote.setToolTipText("Número do lote impresso na embalagem");
        j.llbDataFabrico1 = rotulo("Data Fabrico");
        j.jcDataFabrico = new CampoData();
        j.lbDataExpiracao = rotulo("Data Validade");
        j.jcDataValidade = new CampoData();
    }

    private void criarTabela() {
        j.tableCompra = new TabelaModerna(new DefaultTableModel(
                new Object[][]{},
                new String[]{
                    "Cod.Art", "Descrição", "Preço", "Qtd.", "Unidade", "Desconto(%)", "Taxa ", "Valor",
                    "Valor C/ Imposto", "Critica", "Baixa", "Preço Venda"
                })) {
            // colunas com a largura preferida; se não couberem, aparece rolagem horizontal
            @Override
            public boolean getScrollableTracksViewportWidth() {
                return getParent() == null || getPreferredSize().width <= getParent().getWidth();
            }
        };
        j.tableCompra.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        j.tableCompra.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        j.tableCompra.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
        j.tableCompra.setSurrendersFocusOnKeystroke(true);
        j.tableCompra.getAccessibleContext().setAccessibleName("Itens da compra");
        j.jScrollPane1 = new RolagemModerna(j.tableCompra);
    }

    private void criarTotais() {
        j.lbTotalPagar2 = rotulo("Total Ilíquido");
        j.lbTotalPagar = rotulo("Desconto");
        j.lbTotalPagar3 = rotulo("IVA");
        j.lbTotalPagar1 = rotulo("TOTAL LÍQUIDO");
        j.lbTotalPagar1.setForeground(Tema.SUCESSO_TEXTO);

        Font valor = new Font("Segoe UI", Font.BOLD, 16);
        j.txtTotal_AOA_Iliquido = new Mostrador(valor, Tema.TEXTO);
        j.txtTotal_AOA_Desconto = new Mostrador(valor, Tema.PERIGO);
        j.txtTotal_AOA_IVA = new Mostrador(valor, Tema.TEXTO);
        j.txtTotal_AOA_liquido = new Mostrador(new Font("Segoe UI", Font.BOLD, 26), Tema.SUCESSO_TEXTO);

        j.lbLegenda = new JLabel(" ");
        j.lbLegenda.setFont(Tema.TITULO_CAIXA);
        j.lbLegenda.setForeground(Tema.TEXTO_SUAVE);
        j.lbValorPorExtenco = new JLabel(" ");
        j.lbValorPorExtenco.setFont(Tema.NOTA);
        j.lbValorPorExtenco.setForeground(Tema.ROTULO);
    }

    private void criarAccoes() {
        j.jButton_inserir = new BotaoModerno("Adicionar", Tema.PRIMARIA, false);
        j.jButton_inserir.setIcon(new IconeLinha(IconeLinha.Tipo.ADICIONAR));
        descrever(j.jButton_inserir, "Adicionar produto à compra", "Enter na quantidade");

        j.jButton_eliminar = new BotaoModerno("Remover", Tema.PERIGO, true);
        j.jButton_eliminar.setIcon(new IconeLinha(IconeLinha.Tipo.REMOVER));
        descrever(j.jButton_eliminar, "Remover o item seleccionado da compra", null);

        // O texto "Transferencia de Armazem" é comparado com a permissão do utilizador: não alterar.
        j.btTransferenciaArmazem = new BotaoModerno("Transferencia de Armazem", Tema.PRIMARIA, true);
        j.btTransferenciaArmazem.setIcon(new IconeLinha(IconeLinha.Tipo.TRANSFERIR));
        descrever(j.btTransferenciaArmazem, "Transferir mercadoria entre armazéns", null);

        j.btnCancelar = new BotaoModerno("Sair", Tema.NEUTRA, false);
        j.btnCancelar.setIcon(new IconeLinha(IconeLinha.Tipo.SAIR));
        descrever(j.btnCancelar, "Fechar a janela de compras", null);

        j.jButton4 = new BotaoModerno("Registar Compra", Tema.SUCESSO, false);
        j.jButton4.setIcon(new IconeLinha(IconeLinha.Tipo.FINALIZAR));
        descrever(j.jButton4, "Registar a compra e imprimir o documento", null);

        for (JButton b : new JButton[]{j.jButton_inserir, j.jButton_eliminar, j.btTransferenciaArmazem,
            j.btnCancelar, j.jButton4}) {
            dimensionar(b, Math.max(118, b.getPreferredSize().width), 40);
        }
    }

    /**
     * Diálogo de pesquisa antigo (substituído por BuscaProdutoComprasVisao),
     * mantido porque a lógica ainda o referencia.
     */
    private void criarPesquisaAntiga() {
        j.produtoPesquisaJDialog = new JDialog();
        j.txtDesignacao = new CampoPesquisa("Pesquisar produto pela designação...");
        j.tabela_busca = new TabelaModerna(new DefaultTableModel(
                new Object[][]{},
                new String[]{"Cod.", "Designacao", "Categoria", "Qtd"}) {
            Class[] types = new Class[]{Object.class, Object.class, Object.class, Integer.class};

            @Override
            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }
        });
        j.jScrollPane2 = new RolagemModerna(j.tabela_busca);
        j.jButton6 = new BotaoModerno("Sair", Tema.NEUTRA, true);
        j.jButton6.setIcon(new IconeLinha(IconeLinha.Tipo.SAIR));
        j.jLabel1 = rotulo("BUSCA DE PRODUTO");
        j.jPanel4 = Formularios.transparente(new BorderLayout(0, 10));
        j.jPanel4.add(j.txtDesignacao, BorderLayout.NORTH);
        j.jPanel4.add(j.jScrollPane2, BorderLayout.CENTER);
        JPanel topo = Formularios.transparente(new BorderLayout());
        topo.add(j.jLabel1, BorderLayout.CENTER);
        topo.add(j.jButton6, BorderLayout.EAST);
        j.produtoPesquisaJDialog.getContentPane().setLayout(new BorderLayout(0, 10));
        j.produtoPesquisaJDialog.getContentPane().add(topo, BorderLayout.NORTH);
        j.produtoPesquisaJDialog.getContentPane().add(j.jPanel4, BorderLayout.CENTER);
        j.produtoPesquisaJDialog.setSize(900, 520);
    }

    // ------------------------------------------------------------------
    private static JLabel textoInfo() {
        JLabel label = new JLabel(" ");
        label.setFont(Tema.SUBTITULO_CARTAO);
        label.setForeground(Tema.ROTULO);
        return label;
    }

    /** Campo numérico: alinhado à direita (o feedback de validação está em CompraInformalEventos). */
    private static JTextField numero(Font fonte) {
        JTextField campo = campoTexto(fonte);
        campo.setHorizontalAlignment(JTextField.RIGHT);
        return campo;
    }

    /** Botão quadrado só com ícone (área de clique de 40x36). */
    private static BotaoModerno botaoIcone(IconeLinha.Tipo tipo, java.awt.Color cor, boolean contorno) {
        BotaoModerno botao = new BotaoModerno("", cor, contorno);
        botao.setIcon(new IconeLinha(tipo));
        botao.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 10, 2, 10));
        dimensionar(botao, 40, Tema.ALTURA_CAMPO);
        return botao;
    }
}
