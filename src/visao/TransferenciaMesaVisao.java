package visao;

import comercial.controller.MesasController;
import comercial.controller.MesasController.EstadoMesa;
import comercial.controller.MesasController.ItemAberto;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import kitanda.util.CfMethods;
import util.BDConexao;
import util.ui.ComponentesWeb;
import util.ui.EstiloUI;
import util.ui.FormularioWeb;
import util.ui.MesasWeb;

/**
 * Transferência de pedidos entre mesas, num só ecrã:
 * <ul>
 * <li>Mesa inteira: todos os itens por facturar passam para outra mesa;</li>
 * <li>Itens escolhidos: só as linhas marcadas;</li>
 * <li>Quantidade: parte de um produto (ex.: 2 das 5 cervejas) — a linha é dividida;</li>
 * <li>Mudar de lugar: os itens ficam na mesma mesa, noutro lugar.</li>
 * </ul>
 * Na mesa de destino os itens juntam-se ao pedido aberto (criado se não
 * houver). Tudo numa transacção. Feito à mão, sem .form.
 */
public class TransferenciaMesaVisao extends JDialog {

    private static final int COL_MARCA = 0, COL_PRODUTO = 1, COL_LUGAR = 2, COL_QTD = 3, COL_PASSAR = 4, COL_TOTAL = 5;

    private final MesasController mesasController;
    private final String mesaOrigem;
    private final Runnable aoTransferir;
    private int pedidoOrigem;
    private List<ItemAberto> itens = new ArrayList<>();

    private final JRadioButton rbTudo = new JRadioButton("Mesa inteira", true);
    private final JRadioButton rbItens = new JRadioButton("Itens escolhidos");
    private final JRadioButton rbLugar = new JRadioButton("Mudar de lugar");
    private final DefaultTableModel modelo;
    private final JTable tabela;
    private final JComboBox<String> cmbLugar = new JComboBox<>();
    private final List<Integer> codigosLugar = new ArrayList<>();
    private final MesasWeb.PainelGrelha grelhaDestino = new MesasWeb.PainelGrelha(112, 8);
    private final JPanel blocoDestino;
    private final JLabel lbResumo = new JLabel(" ");
    private final JButton btTransferir = new JButton("Transferir");
    private final JButton btMarcarTudo = new JButton("Marcar tudo");
    private final JButton btDesmarcar = new JButton("Nenhum");
    private MesasWeb.CartaoMesa destino;

    /**
     * @param mesaOrigem designação ("MESA 5")
     * @param aoTransferir chamado depois de uma transferência feita (pode ser null)
     */
    public TransferenciaMesaVisao(Frame pai, BDConexao conexao, String mesaOrigem, Runnable aoTransferir) {
        super(pai, "Transferir pedidos · " + mesaOrigem, true);
        this.mesasController = new MesasController(conexao);
        this.mesaOrigem = mesaOrigem;
        this.aoTransferir = aoTransferir;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // ------------------------------------------------ itens da mesa de origem
        modelo = new DefaultTableModel(new String[]{"", "Produto", "Lugar", "Qtd", "Passar", "Total"}, 0) {
            @Override
            public Class<?> getColumnClass(int c) {
                return c == COL_MARCA ? Boolean.class : c == COL_QTD || c == COL_PASSAR ? Integer.class : String.class;
            }

            @Override
            public boolean isCellEditable(int r, int c) {
                return !rbTudo.isSelected() && (c == COL_MARCA || c == COL_PASSAR);
            }

            @Override
            public void setValueAt(Object v, int r, int c) {
                if (c == COL_PASSAR) {
                    int max = (Integer) getValueAt(r, COL_QTD);
                    int n = v instanceof Integer ? (Integer) v : 0;
                    n = Math.max(0, Math.min(max, n));
                    super.setValueAt(n, r, c);
                    super.setValueAt(n > 0, r, COL_MARCA);
                } else if (c == COL_MARCA) {
                    boolean m = Boolean.TRUE.equals(v);
                    super.setValueAt(m, r, c);
                    int actual = (Integer) getValueAt(r, COL_PASSAR);
                    if (m && actual == 0) {
                        super.setValueAt(getValueAt(r, COL_QTD), r, COL_PASSAR);
                    } else if (!m) {
                        super.setValueAt(0, r, COL_PASSAR);
                    }
                } else {
                    super.setValueAt(v, r, c);
                }
                actualizarResumo();
            }
        };
        tabela = new JTable(modelo);
        EstiloUI.estilizarTabela(tabela, true);
        tabela.setRowHeight(32);
        int[] larguras = {34, 220, 80, 50, 70, 110};
        for (int i = 0; i < larguras.length; i++) {
            tabela.getColumnModel().getColumn(i).setPreferredWidth(larguras[i]);
        }
        tabela.getColumnModel().getColumn(COL_MARCA).setMaxWidth(40);
        tabela.getColumnModel().getColumn(COL_TOTAL).setCellRenderer(new EstiloUI.ZebraRenderer(SwingConstants.RIGHT));
        tabela.setToolTipText("Marque as linhas a passar; em \"Passar\" pode indicar só uma parte da quantidade");
        JScrollPane spItens = new JScrollPane(tabela);
        spItens.setBorder(new javax.swing.border.LineBorder(ComponentesWeb.LINHA, 1, true));
        spItens.getViewport().setBackground(Color.WHITE);
        spItens.setPreferredSize(new Dimension(420, 260));

        ComponentesWeb.botao(btMarcarTudo, ComponentesWeb.Variante.SECUNDARIO, null, "Marcar todas as linhas com a quantidade toda");
        ComponentesWeb.botao(btDesmarcar, ComponentesWeb.Variante.SECUNDARIO, null, "Desmarcar todas as linhas");
        for (JButton b : new JButton[]{btMarcarTudo, btDesmarcar}) {
            b.putClientProperty("web.pilula", Boolean.TRUE);
            b.setBorder(BorderFactory.createEmptyBorder(4, 12, 4, 12));
        }
        btMarcarTudo.addActionListener(e -> marcarTudo(true));
        btDesmarcar.addActionListener(e -> marcarTudo(false));

        ButtonGroup g = new ButtonGroup();
        g.add(rbTudo);
        g.add(rbItens);
        g.add(rbLugar);
        rbTudo.setToolTipText("Passar todos os itens por facturar para outra mesa (juntar mesas)");
        rbItens.setToolTipText("Passar só as linhas marcadas, ou parte da quantidade de uma linha");
        rbLugar.setToolTipText("Os itens marcados ficam nesta mesa, mas passam para outro lugar");
        JPanel modos = FormularioWeb.pilulas(rbTudo, rbItens, rbLugar);
        for (JRadioButton rb : new JRadioButton[]{rbTudo, rbItens, rbLugar}) {
            rb.addActionListener(e -> actualizarModo());
        }
        JLabel lbModo = new JLabel("O que transferir");
        ComponentesWeb.rotulo(lbModo);
        JPanel topoItens = ComponentesWeb.transparente(new BorderLayout(0, 4));
        topoItens.add(lbModo, BorderLayout.NORTH);
        topoItens.add(modos, BorderLayout.CENTER);
        JPanel corpoItens = ComponentesWeb.transparente(new BorderLayout(0, 8));
        corpoItens.add(topoItens, BorderLayout.NORTH);
        corpoItens.add(spItens, BorderLayout.CENTER);
        ComponentesWeb.Cartao cartaoItens = FormularioWeb.cartao("Pedido da " + mesaOrigem, MesasWeb.OCUPADA, corpoItens);
        cartaoItens.adicionarAccao(btMarcarTudo);
        cartaoItens.adicionarAccao(btDesmarcar);

        // ------------------------------------------------ destino
        cmbLugar.addItem("Manter o lugar de cada item");
        codigosLugar.add(0);
        for (Object[] l : mesasController.lugares()) {
            codigosLugar.add((Integer) l[0]);
            cmbLugar.addItem(l[1].toString());
        }
        FormularioWeb.combo(cmbLugar);
        cmbLugar.addActionListener(e -> actualizarResumo());
        JScrollPane spDestino = new JScrollPane(grelhaDestino, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        spDestino.setBorder(null);
        spDestino.setOpaque(false);
        spDestino.getViewport().setOpaque(false);
        spDestino.getVerticalScrollBar().setUnitIncrement(24);
        JLabel lbMesas = new JLabel("Mesa de destino (clique para escolher)");
        ComponentesWeb.rotulo(lbMesas);
        blocoDestino = ComponentesWeb.transparente(new BorderLayout(0, 4));
        blocoDestino.add(lbMesas, BorderLayout.NORTH);
        blocoDestino.add(spDestino, BorderLayout.CENTER);
        JPanel corpoDestino = ComponentesWeb.transparente(new BorderLayout(0, 10));
        corpoDestino.add(ComponentesWeb.campo("Lugar no destino", cmbLugar), BorderLayout.NORTH);
        corpoDestino.add(blocoDestino, BorderLayout.CENTER);
        ComponentesWeb.Cartao cartaoDestino = FormularioWeb.cartao("Para onde", ComponentesWeb.VERDE, corpoDestino);

        JPanel colunas = ComponentesWeb.transparente(new ComponentesWeb.ColunasResponsivas(
                new double[]{0.5, 0.5}, new int[]{420, 360}, 10));
        colunas.add(cartaoItens);
        colunas.add(cartaoDestino);

        // ------------------------------------------------ janela
        lbResumo.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(13f));
        ComponentesWeb.botao(btTransferir, ComponentesWeb.Variante.SUCESSO, null, "Fazer a transferência");
        btTransferir.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(15f));
        btTransferir.setBorder(BorderFactory.createEmptyBorder(10, 24, 10, 24));
        btTransferir.addActionListener(e -> transferir());
        JButton btFechar = new JButton("Fechar");
        FormularioWeb.botaoSair(btFechar, "Fechar sem transferir (Esc)");
        btFechar.addActionListener(e -> dispose());
        JPanel cabecalho = FormularioWeb.cabecalho("Transferir pedidos da " + mesaOrigem,
                "Mesa inteira, itens escolhidos, parte de um produto ou mudança de lugar", null);
        FormularioWeb.montar(this, cabecalho, colunas, FormularioWeb.barraAccoes(lbResumo, btFechar, btTransferir),
                "Transferências de mesas");
        getRootPane().registerKeyboardAction(e -> dispose(),
                javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0),
                javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW);

        carregar();
        actualizarModo();
        FormularioWeb.ajustar(this, 1180, 720, 820, 540);
    }

    // =====================================================================
    // DADOS
    // =====================================================================
    private void carregar() {
        try {
            pedidoOrigem = mesasController.pedidoAberto(mesaOrigem);
            itens = pedidoOrigem == 0 ? new ArrayList<>() : mesasController.itensAbertos(pedidoOrigem);
        } catch (Exception e) {
            e.printStackTrace();
            itens = new ArrayList<>();
        }
        modelo.setRowCount(0);
        for (ItemAberto it : itens) {
            modelo.addRow(new Object[]{true, it.produto, it.lugar == null ? "" : it.lugar, it.quantidade, it.quantidade,
                CfMethods.formatarComoMoeda(it.total)});
        }
        grelhaDestino.removeAll();
        destino = null;
        try {
            for (EstadoMesa e : mesasController.estadoMesas()) {
                if (e.designacao.equalsIgnoreCase(mesaOrigem)) {
                    continue;
                }
                MesasWeb.CartaoMesa c = new MesasWeb.CartaoMesa(e);
                c.setPreferredSize(new Dimension(112, 136));
                c.setTextoLivre("Escolher");
                c.addAccao(() -> escolherDestino(c));
                grelhaDestino.add(c);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        grelhaDestino.revalidate();
        grelhaDestino.repaint();
    }

    // =====================================================================
    // INTERFACE
    // =====================================================================
    private void actualizarModo() {
        boolean tudo = rbTudo.isSelected();
        if (tudo) {
            marcarTudo(true);
        }
        tabela.setEnabled(!tudo);
        btMarcarTudo.setEnabled(!tudo);
        btDesmarcar.setEnabled(!tudo);
        blocoDestino.setVisible(!rbLugar.isSelected());
        if (rbLugar.isSelected() && cmbLugar.getSelectedIndex() == 0 && cmbLugar.getItemCount() > 1) {
            cmbLugar.setSelectedIndex(1);
        }
        actualizarResumo();
        revalidate();
        repaint();
    }

    private void marcarTudo(boolean marcar) {
        if (tabela.isEditing()) {
            tabela.getCellEditor().stopCellEditing();
        }
        for (int r = 0; r < modelo.getRowCount(); r++) {
            modelo.setValueAt(marcar, r, COL_MARCA);
            modelo.setValueAt(marcar ? modelo.getValueAt(r, COL_QTD) : 0, r, COL_PASSAR);
        }
        actualizarResumo();
    }

    private void escolherDestino(MesasWeb.CartaoMesa c) {
        if (destino != null) {
            destino.setSeleccionado(false);
        }
        destino = c;
        c.setSeleccionado(true);
        actualizarResumo();
    }

    /** Linha do item -> quantidade a passar (só as marcadas). */
    private Map<Integer, Integer> escolhidos() {
        Map<Integer, Integer> m = new LinkedHashMap<>();
        for (int r = 0; r < modelo.getRowCount() && r < itens.size(); r++) {
            int n = (Integer) modelo.getValueAt(r, COL_PASSAR);
            if (Boolean.TRUE.equals(modelo.getValueAt(r, COL_MARCA)) && n > 0) {
                m.put(itens.get(r).codigo, n);
            }
        }
        return m;
    }

    private String nomeDestino() {
        return rbLugar.isSelected() ? mesaOrigem : destino == null ? null : destino.getEstado().designacao;
    }

    private void actualizarResumo() {
        if (lbResumo == null || btTransferir == null) {
            return; // ainda na construção
        }
        Map<Integer, Integer> m = escolhidos();
        int unidades = 0;
        for (int n : m.values()) {
            unidades += n;
        }
        String msg;
        boolean pronto = false;
        Color cor = ComponentesWeb.TEXTO_SUAVE;
        if (itens.isEmpty()) {
            msg = "A " + mesaOrigem + " não tem itens por facturar.";
        } else if (m.isEmpty()) {
            msg = "Marque pelo menos um item.";
        } else if (rbLugar.isSelected()) {
            pronto = cmbLugar.getSelectedIndex() > 0;
            msg = pronto ? unidades + (unidades == 1 ? " unidade passa" : " unidades passam") + " para o " + cmbLugar.getSelectedItem().toString().toLowerCase()
                    + " da " + mesaOrigem + "." : "Escolha o lugar.";
        } else if (destino == null) {
            msg = "Escolha a mesa de destino.";
        } else {
            pronto = true;
            msg = (rbTudo.isSelected() ? "Todo o pedido" : unidades + (unidades == 1 ? " unidade" : " unidades"))
                    + " da " + mesaOrigem + " → " + destino.getEstado().designacao
                    + (destino.isOcupada() ? " (junta-se ao pedido aberto)" : "") + ".";
        }
        if (pronto) {
            cor = ComponentesWeb.VERDE.darker();
        }
        lbResumo.setText(msg);
        lbResumo.setForeground(cor);
        btTransferir.setEnabled(pronto);
    }

    // =====================================================================
    // TRANSFERÊNCIA
    // =====================================================================
    private void transferir() {
        if (tabela.isEditing()) {
            tabela.getCellEditor().stopCellEditing();
        }
        Map<Integer, Integer> m = escolhidos();
        String para = nomeDestino();
        if (m.isEmpty() || para == null) {
            return;
        }
        int lugar = codigosLugar.get(Math.max(0, cmbLugar.getSelectedIndex()));
        int r = JOptionPane.showConfirmDialog(this, lbResumo.getText() + "\n\nConfirma a transferência?",
                "Transferir pedidos", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (r != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            mesasController.transferir(pedidoOrigem, para, m, lugar);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "A transferência não foi feita (nada foi alterado):\n" + e.getMessage(),
                    "Transferir pedidos", JOptionPane.ERROR_MESSAGE);
            carregar();
            actualizarModo();
            return;
        }
        if (aoTransferir != null) {
            aoTransferir.run();
        }
        PrincipalPedidosVisao.procedimento_mesas_livre();
        util.ui.ConfirmacaoPositiva.mostrar("Transferência", "Transferência feita",
                rbLugar.isSelected() ? "Os itens mudaram de lugar." : "Os itens estão agora na " + para + ".");
        dispose();
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> new TransferenciaMesaVisao(null, BDConexao.getInstancia(), "MESA 1", null).setVisible(true));
    }
}
