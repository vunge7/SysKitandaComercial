package visao.venda;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputMethodEvent;
import java.awt.event.InputMethodListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Liga os eventos dos componentes aos métodos de tratamento da janela de
 * vendas. São exactamente os mesmos eventos do FormVendaResponsivaVisaoTop.
 */
final class VendaEventos {

    private final VendaModernaVisao j;

    VendaEventos(VendaModernaVisao janela) {
        this.j = janela;
    }

    void ligar() {
        // Cliente
        j.txtIniciaisCliente.addActionListener(j::txtIniciaisClienteActionPerformed);
        j.txtNomeConsumidorFinal.addActionListener(j::txtNomeConsumidorFinalActionPerformed);
        j.txtNomeConsumidorFinal.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent evt) {
                j.txtNomeConsumidorFinalKeyPressed(evt);
            }
        });
        j.txtNifClientePesquisa.addActionListener(j::txtNifClientePesquisaActionPerformed);
        j.txtCodClientePesquisa.addActionListener(j::txtCodClientePesquisaActionPerformed);
        j.cmbCliente.addActionListener(j::cmbClienteActionPerformed);
        j.jButton5.addActionListener(j::jButton5ActionPerformed);
        j.jButton3.addActionListener(j::jButton3ActionPerformed);

        // Documento
        j.cmbTipoDocumento.addActionListener(j::cmbTipoDocumentoActionPerformed);
        j.cmbAnoEconomico.addActionListener(j::cmbAnoEconomicoActionPerformed);
        j.ck_A4.addActionListener(j::ck_A4ActionPerformed);
        j.ck_simplificada_A7.addActionListener(j::ck_simplificada_A7ActionPerformed);
        j.ck_S_A6.addActionListener(j::ck_S_A6ActionPerformed);
        j.ck_ComVirgula.addActionListener(j::ck_ComVirgulaActionPerformed);
        j.ck_simplificada_O_S.addActionListener(j::ck_simplificada_O_SActionPerformed);
        j.ck_simplificada_O.addActionListener(j::ck_simplificada_OActionPerformed);
        j.ck_simplificada.addActionListener(j::ck_simplificadaActionPerformed);
        j.ck_Duplicada.addActionListener(j::ck_DuplicadaActionPerformed);

        // Produto
        j.txtCodigoBarra.addActionListener(j::txtCodigoBarraActionPerformed);
        j.txtCodigoProduto.addActionListener(j::txtCodigoProdutoActionPerformed);
        j.txtCodigoManual.addActionListener(j::txtCodigoManualActionPerformed);
        j.txtBuscaRef.addActionListener(j::txtBuscaRefActionPerformed);
        j.jButton4.addActionListener(j::jButton4ActionPerformed);
        j.cmbSubFamilia.addActionListener(j::cmbSubFamiliaActionPerformed);
        j.cmbProduto.addActionListener(j::cmbProdutoActionPerformed);
        j.cmbArmazem.addActionListener(j::cmbArmazemActionPerformed);
        j.txtQuatindade.addActionListener(j::txtQuatindadeActionPerformed);
        j.btn_adicionar.addActionListener(ocupado(j::btn_adicionarActionPerformed));
        j.btn_remover.addActionListener(j::btn_removerActionPerformed);

        // Tabela
        j.table.addPropertyChangeListener(j::tablePropertyChange);
        j.jScrollPane1.addPropertyChangeListener(j::jScrollPane1PropertyChange);

        // Totais
        j.txtTotalPagar.addActionListener(j::txtTotalPagarActionPerformed);
        j.txtTotalPagarGeral.addActionListener(j::txtTotalPagarGeralActionPerformed);
        j.sp_desconto_financeiro.addChangeListener(j::sp_desconto_financeiroStateChanged);
        j.sp_desconto_financeiro.addInputMethodListener(new InputMethodListener() {
            @Override
            public void caretPositionChanged(InputMethodEvent evt) {
            }

            @Override
            public void inputMethodTextChanged(InputMethodEvent evt) {
                j.sp_desconto_financeiroInputMethodTextChanged(evt);
            }
        });
        j.sp_desconto_financeiro.addPropertyChangeListener(j::sp_desconto_financeiroPropertyChange);
        j.sp_desconto_financeiro.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent evt) {
                j.sp_desconto_financeiroKeyPressed(evt);
            }

            @Override
            public void keyTyped(KeyEvent evt) {
                j.sp_desconto_financeiroKeyTyped(evt);
            }
        });

        // Acções
        j.btnFormaPagamento.addActionListener(j::btnFormaPagamentoActionPerformed);
        j.btnProcessar.addActionListener(ocupado(j::btnProcessarActionPerformed));
        j.btnSemFormaPagamento.addActionListener(j::btnSemFormaPagamentoActionPerformed);
        j.jButton1.addActionListener(j::jButton1ActionPerformed);
        j.jButton2.addActionListener(j::jButton2ActionPerformed);
        j.jButton6.addActionListener(j::jButton6ActionPerformed);
        j.btn_abertura_dia_venda.addActionListener(j::btn_abertura_dia_vendaActionPerformed);
        j.btn_fecho_dia_venda.addActionListener(j::btn_fecho_dia_vendaActionPerformed);
        j.btnCancelar.addActionListener(j::btnCancelarActionPerformed);

        // Ocultos (mantidos para a lógica)
        j.rbTranstorno.addActionListener(j::rbTranstornoActionPerformed);
        j.rbMostrar.addActionListener(j::rbMostrarActionPerformed);
        j.rbArmazem1.addActionListener(j::rbArmazem1ActionPerformed);
        j.rbArmazem.addActionListener(j::rbArmazemActionPerformed);
        j.cmbMoeda.addActionListener(j::cmbMoedaActionPerformed);
    }

    /**
     * Estado de carregamento: cursor de espera enquanto a acção corre (gravar
     * a venda, procurar o produto na base de dados...).
     */
    private ActionListener ocupado(final ActionListener accao) {
        return (ActionEvent e) -> {
            j.getRootPane().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            try {
                accao.actionPerformed(e);
            } finally {
                j.getRootPane().setCursor(Cursor.getDefaultCursor());
            }
        };
    }
}
