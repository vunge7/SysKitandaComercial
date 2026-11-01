package visao.produto;

import java.awt.Color;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import visao.design.AreaModerna;
import visao.design.BotaoModerno;
import visao.design.Caixa;
import visao.design.Cartao;
import visao.design.ComboModerno;
import visao.design.MolduraFoto;
import visao.design.Selo;
import visao.design.Tema;
import static visao.design.Formularios.campoTexto;
import static visao.design.Formularios.dimensionar;
import static visao.design.Formularios.estiloCombo;
import static visao.design.Formularios.estiloOpcao;
import static visao.design.Formularios.rotulo;
import static visao.design.Formularios.rotuloObrigatorio;

/**
 * Cria e configura todos os componentes da janela de produtos (textos,
 * fontes, estados iniciais e grupos de opções). A disposição no ecrã fica a
 * cargo de {@link ProdutoLayout}.
 */
final class ProdutoComponentes {

    private final ProdutosModernoVisao j;

    ProdutoComponentes(ProdutosModernoVisao janela) {
        this.j = janela;
    }

    void criar() {
        criarGrupos();
        criarPesquisa();
        criarCabecalho();
        criarArmazens();
        criarIdentificacao();
        criarClassificacao();
        criarUnidades();
        criarImpostos();
        criarPrecosStock();
        criarAccoes();
        criarPaineis();
    }

    private void criarGrupos() {
        j.buttonGroup1 = new ButtonGroup();
        j.buttonGroup2 = new ButtonGroup();
        j.buttonGroup3 = new ButtonGroup();
        j.buttonGroup4 = new ButtonGroup();
        j.buttonGroup5 = new ButtonGroup();
        j.buttonGroup6 = new ButtonGroup();
    }

    private void criarPesquisa() {
        j.lbProdutos2 = rotulo("Cód. Interno");
        j.lbProdutos3 = rotulo("Cód. Barra");
        j.lbProdutos4 = rotulo("Cód. Manual");
        j.txtCodigoProduto = campoTexto(Tema.CAMPO);
        j.txtCodigoBarraProcura = campoTexto(Tema.CAMPO);
        j.txtCodigoManualProcura = campoTexto(Tema.CAMPO);
        j.jButton4 = new BotaoModerno("", "/imagens/proucura.png", Tema.PRIMARIA, false);
        j.jButton4.setToolTipText("Lupa de Pesquisa de Produtos/Serviços");
        j.btLupaEconomato = new BotaoModerno("Economato", "/imagens/proucura.png", Tema.PRIMARIA, true);
        j.btLupaEconomato.setToolTipText("Lupa de Pesquisas de Produtos no Economato ou Depósito");
        j.rbJanelaServico = new JRadioButton();
        j.rbJanelaServico.setSelected(true);
        j.rbJanelaServico.setEnabled(false);
        j.rbJanelaServico.setOpaque(false);
    }

    private void criarCabecalho() {
        j.jLabel2 = new JLabel("KITANDA 1.2");
        j.jLabel2.setFont(new Font("Segoe UI", Font.BOLD, 18));
        j.jLabel2.setForeground(Color.WHITE);
        j.jLabelCodProduto = new Selo("Cód. Prod:");
    }

    private void criarArmazens() {
        j.cmbarmazem1 = new ComboModerno<>();
        j.cmbarmazem2 = new ComboModerno<>();
        j.cmbarmazem3 = new ComboModerno<>();
        j.cmbarmazem4 = new ComboModerno<>();
        j.txtArmazen1 = campoTexto(Tema.VALOR);
        j.txtArmazen2 = campoTexto(Tema.VALOR);
        j.txtArmazen3 = campoTexto(Tema.VALOR);
        j.txtArmazen4 = campoTexto(Tema.VALOR);
        for (JComboBox c : new JComboBox[]{j.cmbarmazem1, j.cmbarmazem2, j.cmbarmazem3, j.cmbarmazem4}) {
            estiloCombo(c);
            dimensionar(c, 100, Tema.ALTURA_CAMPO);
            c.setEnabled(false);
        }
        for (JTextField t : new JTextField[]{j.txtArmazen1, j.txtArmazen2, j.txtArmazen3, j.txtArmazen4}) {
            t.setEditable(false);
            t.setHorizontalAlignment(JTextField.CENTER);
            dimensionar(t, 76, Tema.ALTURA_CAMPO);
        }
        // O texto "Compras" é comparado com a permissão do utilizador: não alterar.
        j.jButtonCompras = new BotaoModerno("Compras", "/imagem/caixa_32x_32.png", Tema.PRIMARIA, false);
    }

    private void criarIdentificacao() {
        // Estilizados como controlo segmentado em ProdutoLayout.
        j.ck_produto = new JCheckBox("Produto");
        j.ck_servico = new JCheckBox("Serviço");
        j.buttonGroup2.add(j.ck_produto);
        j.buttonGroup2.add(j.ck_servico);
        j.lbBarra = rotulo("Código de Barras");
        j.txtCodigoBarra = campoTexto(Tema.CAMPO);
        j.txtCodigoBarra.setText("0");
        j.lbProduto = rotuloObrigatorio("Designação");
        j.txtDesignacao = new JTextArea(3, 20);
        j.txtDesignacao.setFont(new Font("Segoe UI", Font.BOLD, 15));
        j.jScrollPane1 = new AreaModerna(j.txtDesignacao);
        dimensionar(j.jScrollPane1, 120, 98);

        j.lbPhoto = new JLabel();
        j.lbPhoto.setFont(new Font("Segoe UI", Font.BOLD, 12));
        j.lbPhoto.setForeground(Color.WHITE);
        j.jPanel4 = new MolduraFoto(j.lbPhoto);
        dimensionar(j.jPanel4, 170, 120);
        j.btnCarregar = new BotaoModerno("Carregar Foto", "/imagens/proucura.png", Tema.PRIMARIA, true);
        j.btnCarregar.setToolTipText("Carregue aqui a Foto do Produto");
    }

    private void criarClassificacao() {
        j.lbTipoProduto2 = rotulo("Marca");
        j.lbTipoProduto3 = rotulo("Modelo");
        j.lbTipoProduto4 = rotulo("Família");
        j.lbTipoProduto = rotulo("Sub Família");
        j.lbTipoProduto5 = rotulo("Grupo");
        j.cmbMarca = new ComboModerno<>();
        j.cmbModelo = new ComboModerno<>();
        j.cmbFamilia = new ComboModerno<>();
        j.cmbTipoProduto = new ComboModerno<>();
        j.cmbGrupo = new ComboModerno<>();
        for (JComboBox c : new JComboBox[]{j.cmbMarca, j.cmbModelo, j.cmbFamilia, j.cmbTipoProduto, j.cmbGrupo}) {
            estiloCombo(c);
        }
        j.jButton1 = new BotaoModerno("+", Tema.PRIMARIA, false);
        j.jButton1.setToolTipText("Adicionar Sub Família");
        j.jButton1.setFont(new Font("Segoe UI", Font.BOLD, 16));
        j.jButton1.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));
    }

    private void criarUnidades() {
        j.label_qtd_bruta = rotulo("Unid. Compra");
        j.label_factor_conversao = rotulo("Factor Conversão");
        j.lbUnidade = rotulo("Unidade");
        j.lbCusto = rotulo("Preço Compra (Retalho)");
        j.txtUnidadeCompra = campoTexto(Tema.CAMPO);
        j.txtUnidadeCompra.setText("1");
        j.txtFactorConversao = campoTexto(Tema.CAMPO);
        j.txtFactorConversao.setText("1");
        j.cmbUnidade = new ComboModerno<>();
        estiloCombo(j.cmbUnidade);
        j.txtPrecoCompra = campoTexto(Tema.VALOR);

        j.lbTicket = rotulo("Impressão de Tickets");
        j.rbNaoEnviarCozinha = new JRadioButton("Não Enviar Ticket");
        j.rbEnviarCozinha = new JRadioButton("Enviar Ticket (Cozinha)");
        j.rbEnviarSala = new JRadioButton("Enviar Sala");
        for (JRadioButton r : new JRadioButton[]{j.rbNaoEnviarCozinha, j.rbEnviarCozinha, j.rbEnviarSala}) {
            estiloOpcao(r);
            j.buttonGroup5.add(r);
        }
        j.rbNaoEnviarCozinha.setSelected(true);
        j.btnAssociar = new BotaoModerno("Associação", "/imagens32x32/actualizar_32x32.png", Tema.PRIMARIA, true);
    }

    private void criarImpostos() {
        j.ivaAplicarJRadioButton = new JRadioButton("Sim (Aplicar IVA)");
        j.ivaNaoAplicarJRadioButton = new JRadioButton("Não (Não aplicar IVA)");
        estiloOpcao(j.ivaAplicarJRadioButton);
        estiloOpcao(j.ivaNaoAplicarJRadioButton);
        j.ivaAplicarJRadioButton.setFont(Tema.OPCAO_FORTE);
        j.ivaNaoAplicarJRadioButton.setFont(Tema.OPCAO_FORTE);
        j.buttonGroup1.add(j.ivaAplicarJRadioButton);
        j.buttonGroup1.add(j.ivaNaoAplicarJRadioButton);
        j.ivaNaoAplicarJRadioButton.setSelected(true);
        j.ivaTaxaJLabel = rotulo("Taxa IVA (%)");
        j.TotalIvaLabel = rotulo("P. Venda c/ IVA");
        j.ivaMotivoJLabel = rotulo("Motivo de Isenção");
        j.cmbImposto = new ComboModerno<>(new DefaultComboBoxModel<>(new String[]{"Item 1", "Item 2", "Item 3", "Item 4"}));
        estiloCombo(j.cmbImposto);
        j.cmbImposto.setFont(new Font("Segoe UI", Font.BOLD, 14));
        dimensionar(j.cmbImposto, 90, Tema.ALTURA_CAMPO);
        j.txtPrecoDeVendaComIva = campoTexto(Tema.VALOR);
        j.txtPrecoDeVendaComIva.setEditable(false);
        dimensionar(j.txtPrecoDeVendaComIva, 90, Tema.ALTURA_CAMPO);
        j.ivaMotivoJComboBox = new ComboModerno<>();
        estiloCombo(j.ivaMotivoJComboBox);
        dimensionar(j.ivaMotivoJComboBox, 120, Tema.ALTURA_CAMPO);

        j.retencaoAplicarJRadioButton = new JRadioButton("Sim (Aplicar Retenção)");
        j.retencaoNaoAplicarJRadioButton = new JRadioButton("Não (Não aplicar Retenção)");
        estiloOpcao(j.retencaoAplicarJRadioButton);
        estiloOpcao(j.retencaoNaoAplicarJRadioButton);
        j.buttonGroup4.add(j.retencaoAplicarJRadioButton);
        j.buttonGroup4.add(j.retencaoNaoAplicarJRadioButton);
        j.retencaoNaoAplicarJRadioButton.setSelected(true);
        j.retencaoTaxaJLabel = rotulo("Taxa");
        j.retencaoTaxaJTextField = campoTexto(Tema.CAMPO);
        j.retencaoTaxaJTextField.setEditable(false);
        j.retencaoZeroTaxaJTextField = campoTexto(Tema.CAMPO);
        j.retencaoZeroTaxaJTextField.setEditable(false);
        dimensionar(j.retencaoTaxaJTextField, 80, Tema.ALTURA_CAMPO);
        dimensionar(j.retencaoZeroTaxaJTextField, 80, Tema.ALTURA_CAMPO);
    }

    private void criarPrecosStock() {
        j.jButton5 = new BotaoModerno("Referências", "/imagens/alterar_32x32.png", Tema.PRIMARIA, true);
        dimensionar(j.jButton5, 120, 40);
        j.jLabel3 = rotulo("Controla Lote?");
        j.jRadioButtonComLotes = new JRadioButton("SIM");
        j.jRadioButtonSemLotes = new JRadioButton("NÃO");
        estiloOpcao(j.jRadioButtonComLotes);
        estiloOpcao(j.jRadioButtonSemLotes);
        j.buttonGroup6.add(j.jRadioButtonComLotes);
        j.buttonGroup6.add(j.jRadioButtonSemLotes);
        j.jRadioButtonComLotes.setSelected(true);
        j.lbCategoria3 = rotulo("Local");
        j.cmbLocal = new ComboModerno<>();
        estiloCombo(j.cmbLocal);
        j.lbBarra1 = rotulo("Cód. Manual");
        j.txtCodigoManual = campoTexto(Tema.CAMPO);
        j.txtCodigoManual.setText("0");
        j.lbPreco = rotulo("Margem de Ganho (%)");
        j.txtPercentagemGanhoRetalho = campoTexto(Tema.VALOR);
        j.txtPercentagemGanhoRetalho.setText("0");
        j.lbPrecoVenda = rotulo("PREÇO DE VENDA S/ IVA (RETALHO)");
        j.lbPrecoVenda.setForeground(Tema.SUCESSO_TEXTO);
        j.txtPrecoVendaRetalho = campoTexto(Tema.DESTAQUE);
        dimensionar(j.txtPrecoVendaRetalho, 120, 46);
        j.jLabel1 = rotulo("Qtd. Grosso");
        j.txtQtdGrosso = campoTexto(Tema.VALOR);
        j.lbPrecoVenda1 = rotulo("Preço Grosso");
        j.txtPrecoVendaGrosso = campoTexto(Tema.VALOR);
    }

    private void criarAccoes() {
        j.jButton2 = new BotaoModerno("Novo", "/imagens/img32x32/_ok.png", Tema.PRIMARIA, true);
        j.btnLimpar = new BotaoModerno("Limpar", "/imagens/lixo.png", Tema.NEUTRA, true);
        j.jButton3 = new BotaoModerno("Desactivar", "/imagem/eliminar/destroy_32x32.png", Tema.PERIGO, true);
        j.btnAlterar2 = new BotaoModerno("Alterar", "/imagens/alterar_16x16.png", Tema.AVISO, false);
        j.btnAlterar2.setEnabled(false);
        j.btnSalvar = new BotaoModerno("Salvar", "/imagens/salvar_16x16.png", Tema.SUCESSO, false);
        j.btnCancelar = new BotaoModerno("Sair", "/imagens/LOGOUT - VERMELHO/Logout 32x32.png", Tema.NEUTRA, false);
        for (JButton b : new JButton[]{j.jButton2, j.btnLimpar, j.jButton3, j.btnAlterar2, j.btnSalvar, j.btnCancelar}) {
            dimensionar(b, Math.max(118, b.getPreferredSize().width), 40);
        }
    }

    private void criarPaineis() {
        j.jPanel3 = new Cartao("Pesquisar Produto / Serviço", Tema.PRIMARIA);
        j.jPanel1 = new Cartao("Impostos", "IVA e retenção", Tema.AVISO);
        j.ivaJPanel = new JPanel(new java.awt.GridBagLayout());
        j.ivaJPanel.setOpaque(false);
        j.pnPainelAplicar = new Caixa(null);
        j.jPanel6 = new Caixa(null);
        j.jPanel_retencao = new Caixa("RETENÇÃO NA FONTE (SERVIÇOS)");
        Cartao cartaoStock = new Cartao("Preços e Stock", "venda e margem", Tema.SUCESSO);
        cartaoStock.fixarLargura(340);
        j.painel_stock = cartaoStock;
        // jPanel5 (corpo responsivo) é criado em ProdutoLayout.
    }
}
