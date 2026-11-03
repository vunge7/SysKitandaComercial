package visao;

import java.awt.Cursor;
import java.util.regex.Pattern;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import visao.design.Formularios;
import visao.design.OrdemTab;

/**
 * Liga os eventos dos componentes aos métodos de tratamento da janela de
 * compras (os mesmos do antigo .form) e acrescenta apenas comportamento de
 * interface: validação visual, cursor de espera e ordem do Tab.
 */
final class CompraInformalEventos {

    /** Número com separadores de milhares/decimais (ex.: 10, 2,5, 1.200,00, 1 200.50). */
    private static final Pattern NUMERO = Pattern.compile("^\\s*\\d[\\d .,]*\\s*$");

    private final CompraInformalVisao j;

    CompraInformalEventos(CompraInformalVisao janela) {
        this.j = janela;
    }

    void ligar() {
        // Documento e fornecedor
        j.cmbArmazem.addActionListener(j::cmbArmazemActionPerformed);
        j.cmbAnoEconomico.addActionListener(j::cmbAnoEconomicoActionPerformed);
        j.cmbFornecedor.addActionListener(j::cmbFornecedorActionPerformed);

        // Produto
        j.txtCodigoInterno.addActionListener(j::txtCodigoInternoActionPerformed);
        j.jButtonLupa.addActionListener(j::jButtonLupaActionPerformed);
        j.txtCodBarra.addActionListener(j::txtCodBarraActionPerformed);
        j.txtFactorConversao.addActionListener(j::txtFactorConversaoActionPerformed);
        j.txtQtdEntrar.addActionListener(j::txtQtdEntrarActionPerformed);

        // Itens
        j.jButton_inserir.addActionListener(j::jButton_inserirActionPerformed);
        j.jButton_eliminar.addActionListener(j::jButton_eliminarActionPerformed);
        j.tableCompra.addPropertyChangeListener(j::tableCompraPropertyChange);

        // Totais (só leitura; mantidos como no .form)
        j.txtTotal_AOA_Iliquido.addActionListener(j::txtTotal_AOA_IliquidoActionPerformed);
        j.txtTotal_AOA_liquido.addActionListener(j::txtTotal_AOA_liquidoActionPerformed);
        j.txtTotal_AOA_IVA.addActionListener(j::txtTotal_AOA_IVAActionPerformed);
        j.txtTotal_AOA_Desconto.addActionListener(j::txtTotal_AOA_DescontoActionPerformed);

        // Rodapé
        j.btTransferenciaArmazem.addActionListener(j::btTransferenciaArmazemActionPerformed);
        j.btnCancelar.addActionListener(j::btnCancelarActionPerformed);
        j.jButton4.addActionListener(e -> {
            // feedback de carregamento enquanto a compra é gravada e impressa
            j.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            j.jButton4.setEnabled(false);
            try {
                j.jButton4ActionPerformed(e);
            } finally {
                j.jButton4.setEnabled(true);
                j.setCursor(Cursor.getDefaultCursor());
            }
        });

        // Diálogo de pesquisa antigo
        j.tabela_busca.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                j.tabela_buscaMouseClicked(evt);
            }
        });
        j.jButton6.addActionListener(j::jButton6ActionPerformed);

        validarNumeros(j.txtQtdEntrar, j.txtQtdCritica, j.txtQtdBaixa, j.txtPercentagemGanho, j.txtPrecoVenda);
        definirOrdemTab();
    }

    /**
     * Borda vermelha enquanto o texto não for um número (o campo vazio não é
     * erro: a lógica da compra trata esse caso com a sua própria mensagem).
     */
    private static void validarNumeros(JTextField... campos) {
        for (final JTextField campo : campos) {
            final String nome = campo.getAccessibleContext().getAccessibleName();
            campo.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    verificar();
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    verificar();
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    verificar();
                }

                private void verificar() {
                    String texto = campo.getText();
                    boolean erro = !texto.trim().isEmpty() && !NUMERO.matcher(texto).matches();
                    Formularios.marcarErro(campo, erro);
                    campo.getAccessibleContext().setAccessibleDescription(erro ? "Valor inválido: use apenas números" : nome);
                }
            });
        }
    }

    /** Tab na ordem lógica de preenchimento, seja qual for a disposição no ecrã. */
    private void definirOrdemTab() {
        j.setFocusTraversalPolicy(new OrdemTab(
                j.cmbArmazem, j.cmbFornecedor,
                j.txtCodigoInterno, j.jButtonLupa, j.txtCodBarra,
                j.txtNumeroLote, j.jcDataFabrico, j.jcDataValidade,
                j.spPrecoCompra, j.txtPercentagemGanho, j.txtPrecoVenda,
                j.txtQtdEntrar, j.txtQtdCritica, j.txtQtdBaixa,
                j.jButton_inserir, j.tableCompra, j.jButton_eliminar,
                j.btTransferenciaArmazem, j.jButton4, j.btnCancelar));
    }
}
