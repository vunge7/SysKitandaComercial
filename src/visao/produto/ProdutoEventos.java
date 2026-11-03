package visao.produto;

/**
 * Liga os eventos dos componentes aos métodos de tratamento da janela de
 * produtos.
 */
final class ProdutoEventos {

    private final ProdutosModernoVisao j;

    ProdutoEventos(ProdutosModernoVisao janela) {
        this.j = janela;
    }

    void ligar() {
        // Pesquisa e armazéns
        j.txtCodigoProduto.addActionListener(j::txtCodigoProdutoActionPerformed);
        j.txtCodigoBarraProcura.addActionListener(j::txtCodigoBarraProcuraActionPerformed);
        j.txtCodigoManualProcura.addActionListener(j::txtCodigoManualProcuraActionPerformed);
        j.jButton4.addActionListener(j::jButton4ActionPerformed);
        j.btLupaEconomato.addActionListener(j::btLupaEconomatoActionPerformed);
        j.rbJanelaServico.addActionListener(j::rbJanelaServicoActionPerformed);
        j.txtArmazen4.addActionListener(j::txtArmazen4ActionPerformed);
        j.jButtonCompras.addActionListener(j::jButtonComprasActionPerformed);

        // Identificação e classificação
        j.ck_produto.addActionListener(j::ck_produtoActionPerformed);
        j.ck_servico.addActionListener(j::ck_servicoActionPerformed);
        // ItemListener também dispara quando o código muda a selecção (ex.: ao
        // carregar um produto pesquisado), não só com o clique do utilizador.
        j.ck_produto.addItemListener(e -> ProdutosModernoVisao.actualizarBotaoCompras());
        j.ck_servico.addItemListener(e -> ProdutosModernoVisao.actualizarBotaoCompras());
        j.txtCodigoBarra.addActionListener(j::txtCodigoBarraActionPerformed);
        j.btnCarregar.addActionListener(j::btnCarregarActionPerformed);
        j.cmbMarca.addActionListener(j::cmbMarcaActionPerformed);
        j.cmbModelo.addActionListener(j::cmbModeloActionPerformed);
        j.cmbFamilia.addActionListener(j::cmbFamiliaActionPerformed);
        j.cmbTipoProduto.addActionListener(j::cmbTipoProdutoActionPerformed);
        j.jButton1.addActionListener(j::jButton1ActionPerformed);

        // Unidades e custo
        j.txtUnidadeCompra.addActionListener(j::txtUnidadeCompraActionPerformed);
        j.txtFactorConversao.addActionListener(j::txtFactorConversaoActionPerformed);
        j.cmbUnidade.addActionListener(j::cmbUnidadeActionPerformed);
        j.txtPrecoCompra.addActionListener(j::txtPrecoCompraActionPerformed);
        j.btnAssociar.addActionListener(j::btnAssociarActionPerformed);

        // Impostos
        j.ivaAplicarJRadioButton.addActionListener(j::ivaAplicarJRadioButtonActionPerformed);
        j.ivaNaoAplicarJRadioButton.addActionListener(j::ivaNaoAplicarJRadioButtonActionPerformed);
        j.cmbImposto.addActionListener(j::cmbImpostoActionPerformed);
        j.ivaMotivoJComboBox.addItemListener(j::ivaMotivoJComboBoxItemStateChanged);
        j.ivaMotivoJComboBox.addActionListener(j::ivaMotivoJComboBoxActionPerformed);
        j.retencaoAplicarJRadioButton.addActionListener(j::retencaoAplicarJRadioButtonActionPerformed);
        j.retencaoNaoAplicarJRadioButton.addActionListener(j::retencaoNaoAplicarJRadioButtonActionPerformed);
        j.retencaoTaxaJTextField.addActionListener(j::retencaoTaxaJTextFieldActionPerformed);
        j.retencaoZeroTaxaJTextField.addActionListener(j::retencaoZeroTaxaJTextFieldActionPerformed);

        // Preços e stock
        j.jButton5.addActionListener(j::jButton5ActionPerformed);
        j.txtCodigoManual.addActionListener(j::txtCodigoManualActionPerformed);
        j.txtPercentagemGanhoRetalho.addActionListener(j::txtPercentagemGanhoRetalhoActionPerformed);
        j.txtPrecoVendaRetalho.addActionListener(j::txtPrecoVendaRetalhoActionPerformed);
        j.txtQtdGrosso.addActionListener(j::txtQtdGrossoActionPerformed);
        j.txtPrecoVendaGrosso.addActionListener(j::txtPrecoVendaGrossoActionPerformed);

        // Acções do rodapé
        j.jButton2.addActionListener(j::jButton2ActionPerformed);
        j.btnLimpar.addActionListener(j::btnLimparActionPerformed);
        j.jButton3.addActionListener(j::jButton3ActionPerformed);
        j.btnAlterar2.addActionListener(j::btnAlterar2ActionPerformed);
        j.btnSalvar.addActionListener(j::btnSalvarActionPerformed);
        j.btnCancelar.addActionListener(j::btnCancelarActionPerformed);
    }
}
