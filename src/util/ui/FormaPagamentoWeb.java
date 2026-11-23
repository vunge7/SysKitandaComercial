package util.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Window;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RootPaneContainer;
import javax.swing.SwingConstants;
import javax.swing.table.TableColumnModel;
import kitanda.util.CfMethods;

/**
 * Aspecto da Forma de Pagamento do SysKitandaHotel (azul-noite, dourado e
 * marfim): cabeçalho, resumo com total / recebido / troco, tabela das formas
 * e barra de acções. Monta-se sobre os componentes do initComponents, que
 * continuam a ser os mesmos (com os seus eventos): os valores do total e do
 * troco não mudam de componente, porque o cálculo do pagamento os volta a ler.
 * Serve para janelas (JFrame) e diálogos (JDialog).
 */
public final class FormaPagamentoWeb {

    private static final int[] LARGURAS = {50, 250, 120, 140};
    private static final Color VERDE_ESCURO = new Color(21, 128, 61);

    private FormaPagamentoWeb() {
    }

    /**
     * Formas de pagamento numa tabela (Cód. / Designação / Ref. / Valor).
     *
     * @param lbSaldo saldo do cliente no cabeçalho; pode ser null
     */
    public static <J extends Window & RootPaneContainer> void montar(J janela, JLabel lbSaldo, JLabel lbTotal,
            JLabel lbTroco, JTable tabela, JScrollPane rolagem, JButton finalizar, JButton sair) {
        JPanel corpo = ComponentesWeb.transparente(new BorderLayout(0, 10));
        corpo.setBorder(BorderFactory.createEmptyBorder(12, 14, 10, 14));
        corpo.add(resumo(lbTotal, lbTroco), BorderLayout.NORTH);
        corpo.add(tabela(tabela, rolagem), BorderLayout.CENTER);
        montarJanela(janela, cabecalho(lbSaldo, "Indique o valor recebido em cada forma"), corpo, finalizar, sair);
    }

    /**
     * Formas de pagamento em campos (ex.: Dinheiro, Cartão, Transferência).
     *
     * @param pares rótulo (JLabel) e campo (JTextField), alternados
     */
    public static <J extends Window & RootPaneContainer> void montarComCampos(J janela, JLabel lbTotal,
            JLabel lbTroco, JButton finalizar, JButton sair, Component... pares) {
        JPanel campos = ComponentesWeb.transparente(new GridLayout(0, 1, 0, 8));
        for (int i = 0; i + 1 < pares.length; i += 2) {
            JLabel rotulo = (JLabel) pares[i];
            String t = rotulo.getText() == null ? "" : rotulo.getText().trim();
            if (t.endsWith(":")) {
                t = t.substring(0, t.length() - 1);
            }
            rotulo.setText(t.isEmpty() ? t : t.substring(0, 1).toUpperCase() + t.substring(1).toLowerCase());
            rotulo.setHorizontalAlignment(SwingConstants.LEFT);
            Component campo = pares[i + 1];
            if (campo instanceof JTextField) {
                JTextField f = (JTextField) campo;
                FormularioWeb.campoTexto(f);
                f.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(16f));
                f.setHorizontalAlignment(SwingConstants.RIGHT);
                f.setPreferredSize(new Dimension(200, 40));
            }
            campos.add(ComponentesWeb.campo(rotulo, campo));
        }
        JPanel corpo = ComponentesWeb.transparente(new BorderLayout(0, 10));
        corpo.setBorder(BorderFactory.createEmptyBorder(12, 14, 10, 14));
        corpo.add(resumo(lbTotal, lbTroco), BorderLayout.NORTH);
        corpo.add(FormularioWeb.cartao("Valor recebido em cada forma", MenuWeb.DOURADO, campos), BorderLayout.CENTER);
        montarJanela(janela, cabecalho(null, "Indique o valor recebido em cada forma"), corpo, finalizar, sair);
    }

    private static <J extends Window & RootPaneContainer> void montarJanela(J janela, JComponent cabecalho,
            JComponent corpo, JButton finalizar, JButton sair) {
        Dimension habitual = janela.getSize();
        JPanel raiz = new JPanel(new BorderLayout());
        raiz.setBackground(MenuWeb.MARFIM);
        raiz.setBorder(BorderFactory.createLineBorder(MenuWeb.AZUL_NOITE, 1));
        raiz.add(cabecalho, BorderLayout.NORTH);
        raiz.add(corpo, BorderLayout.CENTER);
        raiz.add(accoes(finalizar, sair), BorderLayout.SOUTH);

        janela.setContentPane(raiz);
        janela.setIconImages(MenuWeb.iconesAplicacao());
        Dimension minimo = raiz.getPreferredSize();
        janela.setSize(Math.max(habitual.width, Math.min(minimo.width, 760)),
                Math.max(habitual.height, Math.min(minimo.height + 40, 620)));
        janela.setLocationRelativeTo(null);
    }

    private static JPanel cabecalho(JLabel lbSaldo, String subtitulo) {
        JPanel c = new ComponentesWeb.PainelGradiente(new BorderLayout(12, 0), MenuWeb.AZUL_NOITE, MenuWeb.AZUL_MARINHO);
        c.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 0, MenuWeb.DOURADO),
                BorderFactory.createEmptyBorder(10, 16, 10, 16)));

        JLabel titulo = new JLabel("Forma de Pagamento");
        titulo.setFont(new Font("Georgia", Font.BOLD, 20));
        titulo.setForeground(Color.WHITE);
        JLabel sub = new JLabel(subtitulo);
        sub.setFont(EstiloUI.FONTE.deriveFont(12.5f));
        sub.setForeground(MenuWeb.DOURADO_CLARO);
        JPanel textos = ComponentesWeb.transparente(new GridLayout(2, 1));
        textos.add(titulo);
        textos.add(sub);
        c.add(textos, BorderLayout.CENTER);

        if (lbSaldo != null) {
            JLabel legenda = new JLabel("Saldo do cliente", SwingConstants.RIGHT);
            legenda.setFont(EstiloUI.FONTE.deriveFont(12f));
            legenda.setForeground(new Color(190, 205, 225));
            lbSaldo.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(16f));
            lbSaldo.setForeground(Color.WHITE);
            lbSaldo.setHorizontalAlignment(SwingConstants.RIGHT);
            JPanel saldo = ComponentesWeb.transparente(new GridLayout(2, 1));
            saldo.add(legenda);
            saldo.add(lbSaldo);
            c.add(saldo, BorderLayout.EAST);
        } else {
            c.add(new MenuWeb.Logotipo(38), BorderLayout.EAST);
        }
        return c;
    }

    private static JPanel resumo(JLabel lbTotal, JLabel lbTroco) {
        JLabel lbRecebido = new JLabel();
        JLabel legendaTroco = new JLabel();
        JPanel linha = ComponentesWeb.transparente(new GridLayout(1, 3, 8, 0));
        linha.add(mosaico(new JLabel("Total a pagar"), lbTotal, true));
        linha.add(mosaico(new JLabel("Valor recebido"), lbRecebido, false));
        linha.add(mosaico(legendaTroco, lbTroco, false));

        Runnable actualizar = () -> actualizarResumo(lbTotal, lbTroco, lbRecebido, legendaTroco);
        lbTroco.addPropertyChangeListener("text", e -> actualizar.run());
        lbTotal.addPropertyChangeListener("text", e -> actualizar.run());
        actualizar.run();
        return linha;
    }

    private static void actualizarResumo(JLabel lbTotal, JLabel lbTroco, JLabel lbRecebido, JLabel legendaTroco) {
        double troco;
        double total;
        try {
            troco = CfMethods.parseMoedaFormatada(lbTroco.getText());
            total = CfMethods.parseMoedaFormatada(lbTotal.getText());
        } catch (RuntimeException e) {
            legendaTroco.setText("Troco");
            return;
        }
        lbRecebido.setText(CfMethods.formatarComoMoeda(total + troco));
        boolean emFalta = troco < 0;
        legendaTroco.setText(emFalta ? "Em falta" : "Troco");
        lbTroco.setForeground(emFalta ? ComponentesWeb.VERMELHO : VERDE_ESCURO);
    }

    private static JComponent mosaico(JLabel legenda, JLabel valor, boolean destaque) {
        JPanel m = new ComponentesWeb.CaixaSuave(new GridLayout(2, 1, 0, 0),
                destaque ? MenuWeb.AZUL_NOITE : Color.WHITE,
                destaque ? MenuWeb.DOURADO : MenuWeb.LINHA_QUENTE);
        m.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
        legenda.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(11.5f));
        legenda.setForeground(destaque ? MenuWeb.DOURADO_CLARO : MenuWeb.TEXTO_SUAVE);
        valor.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(17f));
        valor.setHorizontalAlignment(SwingConstants.LEFT);
        valor.setForeground(destaque ? Color.WHITE : MenuWeb.AZUL_NOITE);
        m.add(legenda);
        m.add(valor);
        return m;
    }

    private static JComponent tabela(JTable tabela, JScrollPane rolagem) {
        EstiloUI.estilizarTabela(tabela, true);
        tabela.setFont(EstiloUI.FONTE.deriveFont(14f));
        tabela.setRowHeight(32);
        // valor escrito e clique em Finalizar sem premir Enter: o valor conta na mesma
        tabela.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);

        TableColumnModel colunas = tabela.getColumnModel();
        for (int i = 0; i < LARGURAS.length && i < colunas.getColumnCount(); i++) {
            colunas.getColumn(i).setPreferredWidth(LARGURAS[i]);
        }
        if (colunas.getColumnCount() >= 4) {
            colunas.getColumn(3).setCellRenderer(new EstiloUI.ZebraRenderer(SwingConstants.RIGHT));
            colunas.getColumn(3).setHeaderValue("Valor recebido");
            colunas.getColumn(2).setHeaderValue("Referência");
            colunas.getColumn(0).setHeaderValue("Cód.");
        }

        rolagem.setBorder(BorderFactory.createLineBorder(MenuWeb.LINHA_QUENTE));
        rolagem.getViewport().setBackground(Color.WHITE);
        rolagem.setPreferredSize(new Dimension(300, 120));
        return rolagem;
    }

    private static JPanel accoes(JButton finalizar, JButton sair) {
        ComponentesWeb.botao(finalizar, ComponentesWeb.Variante.SUCESSO, "/imagens/confirmacao.png",
                "Registar o pagamento e emitir o documento");
        ComponentesWeb.botao(sair, ComponentesWeb.Variante.PERIGO, null, "Fechar sem emitir o documento");
        sair.setIcon(new ComponentesWeb.IconeSair(16, Color.WHITE));

        JPanel barra = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        barra.setBackground(Color.WHITE);
        barra.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, MenuWeb.LINHA_QUENTE));
        barra.add(finalizar);
        barra.add(sair);
        return barra;
    }
}
