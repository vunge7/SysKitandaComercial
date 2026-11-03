package visao.venda;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import visao.design.BotaoModerno;
import visao.design.CampoData;
import visao.design.CampoIndicador;
import visao.design.ComboModerno;
import visao.design.IconeLinha;
import visao.design.Mostrador;
import visao.design.RenderizadorModerno;
import visao.design.RolagemModerna;
import visao.design.Selo;
import visao.design.SpinnerModerno;
import visao.design.TabelaModerna;
import visao.design.Tema;
import static visao.design.Formularios.campoTexto;
import static visao.design.Formularios.descrever;
import static visao.design.Formularios.dimensionar;
import static visao.design.Formularios.estiloCombo;
import static visao.design.Formularios.nota;
import static visao.design.Formularios.rotulo;
import static visao.design.Formularios.rotuloObrigatorio;

/**
 * Cria e configura os componentes da janela de vendas (textos, fontes,
 * ícones, dicas e estados iniciais). Os nomes são os mesmos do
 * FormVendaResponsivaVisaoTop. A disposição fica a cargo de
 * {@link VendaLayout} e os eventos de {@link VendaEventos}.
 */
final class VendaComponentes {

    /** Altura dos botões de acção principais (pagar / processar). */
    static final int ALTURA_ACCAO = 48;
    /** Lado dos botões só com ícone (área de clique confortável). */
    static final int LADO_ICONE = 40;

    private final VendaModernaVisao j;

    VendaComponentes(VendaModernaVisao janela) {
        this.j = janela;
    }

    void criar() {
        criarCliente();
        criarDocumento();
        criarImpressao();
        criarProduto();
        criarTabela();
        criarTotais();
        criarAccoes();
        criarOcultos();
    }

    // ------------------------------------------------------------------
    // Cliente
    // ------------------------------------------------------------------
    private void criarCliente() {
        j.lbCliente = rotulo("Iniciais");
        j.lbCliente1 = rotulo("Nome (consumidor final)");
        j.lbClienteConsumidorFinal = rotulo("NIF");
        j.jLabel2 = rotulo("Cód.");
        j.txtIniciaisCliente = campoTexto(Tema.CAMPO);
        j.txtIniciaisCliente.setToolTipText("Escreva as iniciais para filtrar a lista de clientes");
        j.txtNomeConsumidorFinal = campoTexto(Tema.CAMPO);
        j.txtNomeConsumidorFinal.setToolTipText("Nome a imprimir no documento do consumidor final");
        j.txtNifClientePesquisa = campoTexto(Tema.CAMPO);
        j.txtNifClientePesquisa.setToolTipText("Pesquisar cliente pelo NIF (Enter)");
        j.txtCodClientePesquisa = campoTexto(Tema.CAMPO);
        j.txtCodClientePesquisa.setToolTipText("Pesquisar cliente pelo código (Enter)");
        dimensionar(j.txtCodClientePesquisa, 64, Tema.ALTURA_CAMPO);

        j.cmbCliente = combo(new String[]{"Item 1", "Item 2", "Item 3", "Item 4"});
        j.cmbCliente.setFont(Tema.OPCAO_FORTE);

        j.jButton5 = botaoIcone(IconeLinha.Tipo.CLIENTE_NOVO, Tema.PRIMARIA, true, "Registar / gerir clientes", null);
        j.jButton3 = botaoIcone(IconeLinha.Tipo.IMPORTAR, Tema.PRIMARIA, true, "Importar consultas / mesas do cliente", null);
    }

    // ------------------------------------------------------------------
    // Documento
    // ------------------------------------------------------------------
    private void criarDocumento() {
        j.cmbTipoDocumento = combo(null);
        j.cmbTipoDocumento.setFont(Tema.OPCAO_FORTE);
        j.cmbTipoDocumento.setToolTipText("Tipo de documento a emitir");

        j.dc_data_documento = new CampoData();
        j.dc_data_documento.setEnabled(false);
        j.jLabel9 = rotulo("Vencimento");
        j.dc_data_vencimento = new CampoData();
        j.dc_data_vencimento.setEnabled(false);

        j.cmbAnoEconomico = new ComboModerno<>(new DefaultComboBoxModel<>(new String[]{"Item 1", "Item 2", "Item 3", "Item 4"}));
        estiloCombo(j.cmbAnoEconomico);
        j.cmbAnoEconomico.setEnabled(false);

        j.spnCopia = new SpinnerModerno();
        j.spnCopia.setToolTipText("Número de vias a imprimir");
        dimensionar(j.spnCopia, 84, Tema.ALTURA_CAMPO);

        j.lb_nome_usuario = new Selo("Operador");
        j.jlEmpresa = new JLabel("Empresa");
        j.jlEmpresa.setFont(new Font("Segoe UI", Font.BOLD, 14));
        j.jlEmpresa.setForeground(Color.WHITE);
    }

    /** Formatos de impressão: mantêm os textos (são comparados na lógica). */
    private void criarImpressao() {
        j.buttonGroup1 = new ButtonGroup();
        j.ck_A4 = opcaoFolha("A4", "Folha A4");
        j.ck_Duplicada = opcaoFolha("A5", "A4 em duplicado (A5)");
        j.ck_simplificada = opcaoFolha("A6", "Talão A6");
        j.ck_simplificada_A7 = opcaoFolha("A7", "Talão A7 (simplificado)");
        j.ck_ComVirgula = opcaoFolha("A6V", "Talão A6 com casas decimais");
        j.ck_S_A6 = opcaoFolha("S_A6", "Talão A6 simplificado");
        j.ck_simplificada_O = opcaoFolha("A6_O", "Talão A6 (outro modelo)");
        j.ck_simplificada_O_S = opcaoFolha("S_A6_O", "Talão A6 simplificado (outro modelo)");
        j.ck_simplificada.setSelected(true);
    }

    private JCheckBox opcaoFolha(String texto, String descricao) {
        JCheckBox ck = new JCheckBox(texto);
        ck.setToolTipText(descricao);
        ck.getAccessibleContext().setAccessibleName("Formato de impressão " + descricao);
        j.buttonGroup1.add(ck);
        return ck;
    }

    // ------------------------------------------------------------------
    // Produto
    // ------------------------------------------------------------------
    private void criarProduto() {
        j.lbCodigoProduto4 = rotulo("Código de barras");
        j.txtCodigoBarra = campoTexto(Tema.VALOR);
        j.txtCodigoBarra.setToolTipText("Leia ou escreva o código de barras e prima Enter");

        j.lbCodigoProduto = rotulo("Cód. interno");
        j.txtCodigoProduto = campoTexto(Tema.VALOR);
        j.txtCodigoProduto.setToolTipText("Código interno do produto (Enter)");

        j.lbCodigoProduto2 = rotulo("Cód. manual");
        j.txtCodigoManual = campoTexto(Tema.VALOR);
        j.txtCodigoManual.setToolTipText("Código manual do produto (Enter)");

        j.jLabel1 = rotulo("Referência");
        j.txtBuscaRef = campoTexto(Tema.VALOR);
        j.txtBuscaRef.setToolTipText("Pesquisar pela referência (Enter)");

        j.jButton4 = botaoIcone(IconeLinha.Tipo.PESQUISAR, Tema.PRIMARIA, false, "Pesquisar produto", "F4");

        j.cmbSubFamilia = combo(new String[]{"Item 1", "Item 2", "Item 3", "Item 4"});
        j.cmbProduto = combo(new String[]{"Item 1", "Item 2", "Item 3", "Item 4"});
        j.cmbProduto.setFont(Tema.OPCAO_FORTE);
        j.cmbArmazem = combo(null);
        j.cmbArmazem.setToolTipText("Armazém de onde sai o stock");

        j.lbQuantidadeStock = rotulo("Stock");
        j.txtQuantidadeStock = new CampoIndicador();
        j.txtQuantidadeStock.setToolTipText("Stock disponível no armazém (vermelho = crítico)");
        dimensionar(j.txtQuantidadeStock, 90, Tema.ALTURA_CAMPO);

        j.lbQuantidade = rotuloObrigatorio("Qtd.");
        j.txtQuatindade = campoTexto(Tema.VALOR);
        j.txtQuatindade.setText("1");
        j.txtQuatindade.setHorizontalAlignment(JTextField.RIGHT);
        j.txtQuatindade.setToolTipText("Quantidade a adicionar (Enter adiciona)");
        dimensionar(j.txtQuatindade, 80, Tema.ALTURA_CAMPO);

        j.lbCodigoProduto3 = rotulo("Preço");
        j.txtPreco = campoTexto(Tema.VALOR);
        j.txtPreco.setEditable(false);
        j.txtPreco.setFocusable(false);
        j.txtPreco.setHorizontalAlignment(JTextField.RIGHT);
        dimensionar(j.txtPreco, 120, Tema.ALTURA_CAMPO);

        j.btn_adicionar = new BotaoModerno("Adicionar", Tema.PRIMARIA, false);
        j.btn_adicionar.setIcon(new IconeLinha(IconeLinha.Tipo.ADICIONAR));
        descrever(j.btn_adicionar, "Adicionar produto à venda", "Enter na quantidade");
        dimensionar(j.btn_adicionar, 130, Tema.ALTURA_CAMPO + 2);

        j.btn_remover = new BotaoModerno("Remover", Tema.PERIGO, true);
        j.btn_remover.setIcon(new IconeLinha(IconeLinha.Tipo.REMOVER));
        descrever(j.btn_remover, "Remover a linha seleccionada da venda", null);
        dimensionar(j.btn_remover, 124, Tema.ALTURA_CAMPO + 2);
    }

    // ------------------------------------------------------------------
    // Tabela da venda
    // ------------------------------------------------------------------
    private void criarTabela() {
        j.table = new TabelaModerna(new DefaultTableModel(
                new Object[][]{
                    {null, null, null, null, null, null, null, null, null, null, null}
                },
                new String[]{
                    "Codigo", "Descrição", "Unidade", "Preço", "Quantidade", "Desconto (%)", "Taxa",
                    "Retenção", "Valor Retenção", "Valor", "Valor C/ Imposto"
                }) {
            boolean[] canEdit = new boolean[]{
                true, false, false, true, true, true, false, false, false, true, false
            };

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        RenderizadorModerno renderizador = new RenderizadorVenda();
        j.table.setDefaultRenderer(Object.class, renderizador);
        j.table.setDefaultRenderer(Number.class, renderizador);
        j.table.setDefaultRenderer(Double.class, renderizador);
        j.table.setDefaultRenderer(Integer.class, renderizador);
        j.table.setSurrendersFocusOnKeystroke(true);
        // altura base da área da tabela; o cartão estica com o espaço livre
        j.table.setPreferredScrollableViewportSize(new java.awt.Dimension(600, 220));
        j.table.getAccessibleContext().setAccessibleName("Itens da venda");
        ajustarColunas(j.table);
        // A lógica troca o modelo da tabela (setModel) e o Swing recria as
        // colunas: voltar a aplicar as larguras sempre que isso acontece.
        j.table.addPropertyChangeListener("model", e -> ajustarColunas(j.table));

        j.jScrollPane1 = new RolagemModerna(j.table);
        j.jScrollPane1.setAutoscrolls(true);
    }

    static void ajustarColunas(JTable tabela) {
        int[] larguras = {80, 300, 80, 100, 90, 90, 60, 80, 110, 110, 130};
        TableColumnModel colunas = tabela.getColumnModel();
        for (int i = 0; i < larguras.length && i < colunas.getColumnCount(); i++) {
            colunas.getColumn(i).setPreferredWidth(larguras[i]);
        }
    }

    /** Valores (colunas a partir do preço) alinhados à direita. */
    private static class RenderizadorVenda extends RenderizadorModerno {

        @Override
        public Component getTableCellRendererComponent(JTable tabela, Object valor, boolean seleccionado,
                boolean foco, int linha, int coluna) {
            super.getTableCellRendererComponent(tabela, valor, seleccionado, foco, linha, coluna);
            int modelo = tabela.convertColumnIndexToModel(coluna);
            if (modelo >= 3) {
                setHorizontalAlignment(SwingConstants.RIGHT);
            } else if (modelo == 0 || modelo == 2) {
                setHorizontalAlignment(SwingConstants.CENTER);
            }
            if (modelo == 10 && !seleccionado) {
                setFont(tabela.getFont().deriveFont(Font.BOLD));
            }
            return this;
        }
    }

    // ------------------------------------------------------------------
    // Totais
    // ------------------------------------------------------------------
    private void criarTotais() {
        j.txtTotalPagar = new Mostrador(new Font("Segoe UI", Font.BOLD, 30), Tema.SUCESSO_TEXTO);
        j.txtTotalPagar.getAccessibleContext().setAccessibleName("Total a pagar");
        j.lbCodigoProduto5 = rotulo("Total geral");
        j.txtTotalPagarGeral = new Mostrador(Tema.VALOR, Tema.TEXTO);
        j.txtTotalPagarGeral.getAccessibleContext().setAccessibleName("Total geral");

        j.lbValorPorExtenco = new JLabel() {
            @Override
            public void setText(String texto) {
                // o valor por extenso pode ser longo: deixar quebrar a linha
                super.setText(texto == null || texto.isEmpty() || texto.startsWith("<html>")
                        ? texto
                        : "<html><div style='width:250px'>" + texto + "</div></html>");
            }
        };
        j.lbValorPorExtenco.setFont(Tema.NOTA);
        j.lbValorPorExtenco.setForeground(Tema.SUCESSO_TEXTO);
        j.lbValorPorExtenco.setVerticalAlignment(SwingConstants.TOP);

        j.lbDescontoFinanceiro = rotulo("Desconto financeiro (%)");
        j.sp_desconto_financeiro = new SpinnerModerno();
        j.sp_desconto_financeiro.setToolTipText("Desconto financeiro sobre o total da venda");
        dimensionar(j.sp_desconto_financeiro, 110, Tema.ALTURA_CAMPO);
    }

    // ------------------------------------------------------------------
    // Acções
    // ------------------------------------------------------------------
    private void criarAccoes() {
        j.btnFormaPagamento = new BotaoModerno("Pagamento", Tema.SUCESSO, false);
        j.btnFormaPagamento.setIcon(new IconeLinha(IconeLinha.Tipo.PAGAMENTO));
        j.btnFormaPagamento.setFont(new Font("Segoe UI", Font.BOLD, 15));
        descrever(j.btnFormaPagamento, "Escolher forma de pagamento e finalizar a venda", "F5");
        dimensionar(j.btnFormaPagamento, 200, ALTURA_ACCAO);

        j.btnProcessar = new BotaoModerno("Processar documento", Tema.PRIMARIA, false);
        j.btnProcessar.setIcon(new IconeLinha(IconeLinha.Tipo.FINALIZAR));
        j.btnProcessar.setFont(new Font("Segoe UI", Font.BOLD, 15));
        descrever(j.btnProcessar, "Emitir o documento (factura, proforma, guia...)", null);
        dimensionar(j.btnProcessar, 200, ALTURA_ACCAO);

        j.jButton1 = new BotaoModerno("Pendurar", Tema.AVISO, true);
        j.jButton1.setIcon(new IconeLinha(IconeLinha.Tipo.PENDURAR));
        descrever(j.jButton1, "Pendurar a factura para continuar mais tarde", null);

        j.jButton2 = new BotaoModerno("Pendentes", Tema.PRIMARIA, true);
        j.jButton2.setIcon(new IconeLinha(IconeLinha.Tipo.PENDENTES));
        descrever(j.jButton2, "Ver facturas pendentes", "F7");

        j.jButton6 = new BotaoModerno("Gaveta", Tema.NEUTRA, true);
        j.jButton6.setIcon(new IconeLinha(IconeLinha.Tipo.GAVETA));
        descrever(j.jButton6, "Abrir a gaveta de dinheiro", null);

        // Os textos "Abertura de Caixa" e "Fecho" são comparados com as
        // permissões do utilizador: não alterar.
        j.btn_abertura_dia_venda = new BotaoModerno("Abertura de Caixa", Tema.PRIMARIA, true);
        j.btn_abertura_dia_venda.setIcon(new IconeLinha(IconeLinha.Tipo.CAIXA_ABRIR));
        descrever(j.btn_abertura_dia_venda, "Abrir o caixa do dia", null);
        j.btn_fecho_dia_venda = new BotaoModerno("Fecho", Tema.PERIGO, true);
        j.btn_fecho_dia_venda.setIcon(new IconeLinha(IconeLinha.Tipo.CAIXA_FECHAR));
        descrever(j.btn_fecho_dia_venda, "Fechar o caixa do dia", null);

        j.btnCancelar = new BotaoModerno("Sair", Tema.NEUTRA, false);
        j.btnCancelar.setIcon(new IconeLinha(IconeLinha.Tipo.SAIR));
        descrever(j.btnCancelar, "Fechar a janela de vendas", null);

        for (AbstractButton b : new AbstractButton[]{j.jButton1, j.jButton2, j.jButton6,
            j.btn_abertura_dia_venda, j.btn_fecho_dia_venda, j.btnCancelar}) {
            dimensionar(b, Math.max(118, b.getPreferredSize().width), 40);
        }
    }

    /**
     * Componentes que a lógica usa mas que estão sempre ocultos no ecrã de
     * vendas (moeda, transtorno, armazém, etc.). Não entram no layout.
     */
    private void criarOcultos() {
        j.cmbMoeda = combo(null);
        j.btnSemFormaPagamento = new BotaoModerno("Finalizar", Tema.SUCESSO, false);
        j.btnSemFormaPagamento.setEnabled(false);
        j.rbTranstorno = new JRadioButton();
        j.rbTranstorno.setSelected(true);
        j.rbTranstorno.setEnabled(false);
        j.rbMostrar = new JRadioButton();
        j.rbMostrar.setSelected(true);
        j.rbMostrar.setEnabled(false);
        j.rbArmazem1 = new JRadioButton();
        j.rbArmazem1.setEnabled(false);
        j.rbArmazem = new JRadioButton();
        j.rbArmazem.setSelected(true);
        j.rbArmazem.setEnabled(false);
        j.jlStockNegativo = new JLabel("Stock Negativo");
        j.lb_proximo_documento = nota("");
        j.txtLocal = new JTextField();
        j.txtTotalPagarRetencao = new JLabel("Retencao");
    }

    // ------------------------------------------------------------------
    // Fábricas
    // ------------------------------------------------------------------
    @SuppressWarnings("unchecked")
    private static JComboBox combo(String[] itens) {
        ComboModerno combo = itens == null ? new ComboModerno() : new ComboModerno(new DefaultComboBoxModel(itens));
        estiloCombo(combo);
        return combo;
    }

    /** Botão só com ícone: 40x40, dica e nome acessível. */
    static JButton botaoIcone(IconeLinha.Tipo tipo, Color cor, boolean contorno, String nome, String atalho) {
        BotaoModerno b = new BotaoModerno("", cor, contorno);
        b.setIcon(new IconeLinha(tipo));
        b.setBorder(BorderFactory.createEmptyBorder(0, 0, 2, 0));
        descrever(b, nome, atalho);
        dimensionar(b, LADO_ICONE, Tema.ALTURA_CAMPO);
        return b;
    }
}
