package comercial.mensalidade;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.text.Normalizer;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import kitanda.util.CfMethods;
import util.BDConexao;
import util.ui.ComponentesWeb;
import util.ui.EstiloUI;
import util.ui.FormularioWeb;

/**
 * Inscrições dos clientes das mensalidades: quem está activo, desistente ou
 * desactivado. Em cada linha muda-se o estado (caixa "Estado") ou usa-se o
 * botão da linha: "Desistir…" num activo, "Reactivar…" num inactivo. Quem
 * volta não precisa de nova inscrição (já existe na base de dados): reactiva-se
 * aqui, com a taxa de reactivação, a dívida antiga e o 1.º mês na venda.
 */
public class InscricoesVisao extends JDialog {

    private static final DateTimeFormatter DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final String[] MOTIVOS = {"Deixou de treinar", "Mudou de residência", "Dificuldades financeiras",
        "Lesão / motivos de saúde", "Viagem / ausência prolongada", "Falta de pagamento", "Outro"};

    private static final int C_COD = 0, C_CLIENTE = 1, C_TELEFONE = 2, C_SERVICOS = 3, C_MENSALIDADE = 4, C_DIVIDA = 5,
            C_ESTADO = 6, C_DESDE = 7, C_ACCAO = 8;

    private final BDConexao conexao;
    private final List<Cliente> todos = new ArrayList<>();
    private final List<Cliente> visiveis = new ArrayList<>();
    private boolean aPreencher;

    private final JTextField txtPesquisa = new JTextField();
    private final JToggleButton fActivos = new JToggleButton("Activos");
    private final JToggleButton fDesistentes = new JToggleButton("Desistentes");
    private final JToggleButton fDesactivados = new JToggleButton("Desactivados");
    private final JToggleButton fTodos = new JToggleButton("Todos");
    private final JLabel lbEstado = FormularioWeb.dica(" ");
    private final DefaultTableModel modeloHistorico = new DefaultTableModel(new Object[]{"Data", "Estado", "Motivo", "Valor"}, 0) {
        @Override
        public boolean isCellEditable(int r, int c) {
            return false;
        }
    };
    private final JLabel lbHistorico = new JLabel("Histórico");

    private final DefaultTableModel modelo = new DefaultTableModel(new Object[]{
        "Cód", "Cliente", "Telefone", "Serviços", "Mensalidade", "Dívida", "Estado", "Desde", ""}, 0) {
        @Override
        public boolean isCellEditable(int r, int c) {
            return c == C_ESTADO || c == C_ACCAO;
        }

        @Override
        public void setValueAt(Object v, int r, int c) {
            if (c == C_ESTADO && !aPreencher) {
                // a mudança só fica se for confirmada (motivo / reactivação)
                Cliente cli = visiveis.get(r);
                if (v instanceof EstadoCliente.Estado && v != cli.registo.estado) {
                    javax.swing.SwingUtilities.invokeLater(() -> mudarEstado(cli, (EstadoCliente.Estado) v));
                }
                return;
            }
            super.setValueAt(v, r, c);
        }
    };
    private final JTable tabela = new JTable(modelo);

    /** Um cliente e os seus contratos. */
    static final class Cliente {

        int id;
        String nome, telefone = "";
        EstadoCliente.Registo registo = EstadoCliente.ACTIVO_SEM_REGISTO;
        final List<Devedores.Linha> contratos = new ArrayList<>();
        BigDecimal divida = BigDecimal.ZERO;
        int mesesAtraso;

        String servicos() {
            StringBuilder s = new StringBuilder();
            for (Devedores.Linha l : contratos) {
                s.append(s.length() > 0 ? ", " : "").append(l.servico);
            }
            return s.toString();
        }

        String mensalidade() {
            if (mesesAtraso > 0) {
                return "Em atraso · " + mesesAtraso + (mesesAtraso == 1 ? " mês" : " meses");
            }
            if (registo.inactivo()) {
                return "Sem dívida";
            }
            for (Devedores.Linha l : contratos) {
                if (l.situacao == Devedores.Situacao.VENCE_HOJE) {
                    return "Vence hoje";
                }
            }
            return "Em dia";
        }
    }

    // =====================================================================
    // ABRIR
    // =====================================================================
    public static void abrir(Window dono, BDConexao conexao) {
        if (!Mensalidades.activo()) {
            JOptionPane.showMessageDialog(dono, "As inscrições só existem nos negócios Ginásio, Centro Infantil e Prestação de Serviço.");
            return;
        }
        new InscricoesVisao(dono, conexao, null).setVisible(true);
    }

    /** Abre já com a pesquisa num cliente (ex.: tentaram inscrever de novo um desistente). */
    public static void abrir(Window dono, BDConexao conexao, String nomeCliente) {
        new InscricoesVisao(dono, conexao, nomeCliente).setVisible(true);
    }

    /**
     * Antes de cobrar mensalidades na venda: se o cliente desistiu ou foi
     * desactivado, não deixa cobrar como se nada fosse — oferece a reactivação.
     * Devolve true se pode seguir (cliente activo).
     */
    public static boolean podeCobrar(Window dono, int clienteId, BDConexao conexao) {
        if (!Mensalidades.activo()) {
            return true;
        }
        EstadoCliente.Registo r = EstadoCliente.de(clienteId);
        if (!r.inactivo()) {
            return true;
        }
        String nome = "";
        try {
            entity.TbCliente c = new comercial.controller.ClientesController(conexao).findByCodigo(clienteId);
            nome = c == null ? "" : c.getNome();
        } catch (Exception ignorar) {
        }
        int op = JOptionPane.showConfirmDialog(dono,
                "O cliente " + nome + " está " + r.estado.texto().toLowerCase()
                + (r.data == null ? "" : " desde " + r.data.toLocalDate().format(DATA))
                + (r.motivo.isEmpty() ? "" : " (" + r.motivo + ")") + ".\n\nPretende reactivá-lo agora?",
                "Cliente " + r.estado.texto().toLowerCase(), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (op == JOptionPane.YES_OPTION) {
            ReactivarClienteDialog.abrir(dono, conexao, clienteId);
        }
        return false;
    }

    /**
     * Na ficha do cliente, ao gravar um nome que já existe: se esse cliente
     * desistiu ou foi desactivado, oferece a reactivação em vez de uma nova
     * inscrição. Devolve true se tratou o caso (não mostrar "já existe").
     */
    public static boolean ofereceReactivar(Window dono, BDConexao conexao, String nome) {
        if (!Mensalidades.activo() || nome == null || nome.trim().isEmpty()) {
            return false;
        }
        entity.TbCliente c;
        try {
            c = new comercial.controller.ClientesController(conexao).findByNome(nome.trim());
        } catch (Exception e) {
            return false;
        }
        if (c == null || c.getCodigo() == null) {
            return false;
        }
        EstadoCliente.Registo r = EstadoCliente.de(c.getCodigo());
        if (!r.inactivo()) {
            return false;
        }
        int op = JOptionPane.showConfirmDialog(dono, c.getNome() + " já está registado (Cód. " + c.getCodigo() + ") e está "
                + r.estado.texto().toLowerCase() + (r.data == null ? "" : " desde " + r.data.toLocalDate().format(DATA))
                + ".\nNão é preciso inscrever de novo.\n\nPretende reactivá-lo?", "Cliente já registado",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (op == JOptionPane.YES_OPTION) {
            ReactivarClienteDialog.abrir(dono, conexao, c.getCodigo());
        }
        return true;
    }

    private InscricoesVisao(Window dono, BDConexao conexao, String pesquisa) {
        this(dono, conexao, pesquisa, true);
    }

    /** Só para pré-visualizações: sem BD. */
    static InscricoesVisao previa(List<Devedores.Linha> linhas) {
        InscricoesVisao v = new InscricoesVisao(null, null, null, false);
        v.todos.addAll(agrupar(linhas));
        v.fTodos.setSelected(true);
        v.mostrar();
        return v;
    }

    private InscricoesVisao(Window dono, BDConexao conexao, String pesquisa, boolean ler) {
        super(dono, ModalityType.APPLICATION_MODAL);
        this.conexao = conexao;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setTitle("Inscrições dos clientes");
        montar();
        if (pesquisa != null) {
            txtPesquisa.setText(pesquisa);
            fTodos.setSelected(true);
        } else {
            fActivos.setSelected(true);
        }
        FormularioWeb.ajustar(this, 1240, 760, 760, 480);
        if (ler) {
            carregar();
        }
    }

    // =====================================================================
    // ECRÃ
    // =====================================================================
    private void montar() {
        FormularioWeb.campoTexto(txtPesquisa);
        txtPesquisa.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                mostrar();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                mostrar();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });
        JPanel campo = FormularioWeb.campoCabecalho(new JLabel("Procurar cliente"), txtPesquisa);
        campo.setPreferredSize(new Dimension(260, 56));
        JPanel direita = ComponentesWeb.transparente(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        direita.add(campo);
        JPanel cabecalho = FormularioWeb.cabecalho("Inscrições dos clientes",
                "Activos, desistentes e desactivados  ·  quem volta reactiva-se aqui, sem nova inscrição", direita);

        ButtonGroup g = new ButtonGroup();
        for (JToggleButton b : new JToggleButton[]{fActivos, fDesistentes, fDesactivados, fTodos}) {
            g.add(b);
            b.addActionListener(e -> mostrar());
        }
        JPanel filtros = FormularioWeb.pilulas(fActivos, fDesistentes, fDesactivados, fTodos);

        // ---- tabela
        EstiloUI.estilizarTabela(tabela, true);
        tabela.setRowHeight(34);
        tabela.getColumnModel().getColumn(C_COD).setMaxWidth(60);
        int[] larguras = {50, 200, 100, 170, 150, 100, 130, 90, 120};
        for (int i = 0; i < larguras.length; i++) {
            tabela.getColumnModel().getColumn(i).setPreferredWidth(larguras[i]);
        }
        JComboBox<EstadoCliente.Estado> cmbEstado = new JComboBox<>(EstadoCliente.Estado.values());
        cmbEstado.setFont(EstiloUI.FONTE_NEGRITO);
        tabela.getColumnModel().getColumn(C_ESTADO).setCellEditor(new DefaultCellEditor(cmbEstado));
        tabela.getColumnModel().getColumn(C_ESTADO).setCellRenderer(new RendererEstado());
        tabela.getColumnModel().getColumn(C_MENSALIDADE).setCellRenderer(new RendererMensalidade());
        tabela.getColumnModel().getColumn(C_DIVIDA).setCellRenderer(new RendererMoeda());
        BotaoLinha botao = new BotaoLinha();
        tabela.getColumnModel().getColumn(C_ACCAO).setCellRenderer(botao);
        tabela.getColumnModel().getColumn(C_ACCAO).setCellEditor(botao);
        tabela.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                mostrarHistorico();
            }
        });
        JScrollPane rol = new JScrollPane(tabela);
        rol.setBorder(BorderFactory.createLineBorder(ComponentesWeb.LINHA));
        rol.setPreferredSize(new Dimension(900, 360));

        JPanel corpo = ComponentesWeb.transparente(new BorderLayout(0, 10));
        corpo.add(filtros, BorderLayout.NORTH);
        corpo.add(rol, BorderLayout.CENTER);
        corpo.add(FormularioWeb.dica("Mude a caixa \"Estado\" da linha ou use o botão da linha. Desistentes e desactivados "
                + "deixam de gerar dívida a partir do dia em que saem e não aparecem nos alertas."), BorderLayout.SOUTH);
        ComponentesWeb.Cartao cartaoLista = FormularioWeb.cartao("Clientes com mensalidades", ComponentesWeb.AZUL_VIVO, corpo);

        // ---- histórico do seleccionado
        JTable hist = new JTable(modeloHistorico);
        EstiloUI.estilizarTabela(hist, true);
        hist.getColumnModel().getColumn(0).setPreferredWidth(120);
        hist.getColumnModel().getColumn(2).setPreferredWidth(360);
        JScrollPane rolHist = new JScrollPane(hist);
        rolHist.setBorder(BorderFactory.createLineBorder(ComponentesWeb.LINHA));
        rolHist.setPreferredSize(new Dimension(600, 130));
        lbHistorico.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(13f));
        JPanel corpoHist = ComponentesWeb.transparente(new BorderLayout(0, 6));
        corpoHist.add(lbHistorico, BorderLayout.NORTH);
        corpoHist.add(rolHist, BorderLayout.CENTER);
        ComponentesWeb.Cartao cartaoHist = FormularioWeb.cartao("Histórico de entradas e saídas", ComponentesWeb.AZUL_VIVO, corpoHist);

        JPanel conteudo = ComponentesWeb.transparente(new BorderLayout(0, 10));
        conteudo.add(cartaoLista, BorderLayout.CENTER);
        conteudo.add(cartaoHist, BorderLayout.SOUTH);

        JButton btActualizar = new JButton("Actualizar");
        JButton btFechar = new JButton("Fechar");
        ComponentesWeb.botao(btActualizar, ComponentesWeb.Variante.SECUNDARIO, null, "Ler de novo (F5)");
        FormularioWeb.botaoSair(btFechar, "Fechar (Esc)");
        btActualizar.addActionListener(e -> carregar());
        btFechar.addActionListener(e -> dispose());
        FormularioWeb.montar(this, cabecalho, conteudo, FormularioWeb.barraAccoes(lbEstado, btActualizar, btFechar),
                "Inscrições dos clientes");
        JComponent raiz = getRootPane();
        raiz.registerKeyboardAction(e -> dispose(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
        raiz.registerKeyboardAction(e -> carregar(), KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
    }

    // =====================================================================
    // DADOS
    // =====================================================================
    private void carregar() {
        lbEstado.setText("A carregar…");
        new SwingWorker<List<Cliente>, Void>() {
            @Override
            protected List<Cliente> doInBackground() throws Exception {
                return agrupar(Devedores.carregar(LocalDate.now(), ConfigMensalidade.ler().diasAlerta));
            }

            @Override
            protected void done() {
                try {
                    todos.clear();
                    todos.addAll(get());
                    mostrar();
                } catch (Exception e) {
                    lbEstado.setText("Erro: " + e.getMessage());
                }
            }
        }.execute();
    }

    static List<Cliente> agrupar(List<Devedores.Linha> linhas) {
        Map<Integer, Cliente> m = new LinkedHashMap<>();
        for (Devedores.Linha l : linhas) {
            Cliente c = m.computeIfAbsent(l.clienteId, k -> {
                Cliente n = new Cliente();
                n.id = l.clienteId;
                n.nome = l.cliente;
                n.telefone = l.telefone;
                n.registo = l.estadoCliente;
                return n;
            });
            c.contratos.add(l);
            c.divida = c.divida.add(l.divida).add(l.multa);
            c.mesesAtraso += l.emAtraso.size();
        }
        List<Cliente> r = new ArrayList<>(m.values());
        r.sort((a, b) -> a.nome.compareToIgnoreCase(b.nome));
        return r;
    }

    private static String chave(String s) {
        return Normalizer.normalize(s == null ? "" : s, Normalizer.Form.NFD).replaceAll("\\p{M}", "").toLowerCase();
    }

    private void mostrar() {
        int a = 0, d = 0, x = 0;
        for (Cliente c : todos) {
            switch (c.registo.estado) {
                case DESISTENTE:
                    d++;
                    break;
                case DESACTIVADO:
                    x++;
                    break;
                default:
                    a++;
            }
        }
        fActivos.setText("Activos (" + a + ")");
        fDesistentes.setText("Desistentes (" + d + ")");
        fDesactivados.setText("Desactivados (" + x + ")");
        fTodos.setText("Todos (" + todos.size() + ")");

        String q = chave(txtPesquisa.getText().trim());
        visiveis.clear();
        for (Cliente c : todos) {
            if (!q.isEmpty() && !chave(c.nome).contains(q) && !c.telefone.contains(q)) {
                continue;
            }
            EstadoCliente.Estado e = c.registo.estado;
            if ((fActivos.isSelected() && e != EstadoCliente.Estado.ACTIVO)
                    || (fDesistentes.isSelected() && e != EstadoCliente.Estado.DESISTENTE)
                    || (fDesactivados.isSelected() && e != EstadoCliente.Estado.DESACTIVADO)) {
                continue;
            }
            visiveis.add(c);
        }
        if (tabela.isEditing()) {
            tabela.getCellEditor().cancelCellEditing();
        }
        aPreencher = true;
        modelo.setRowCount(0);
        for (Cliente c : visiveis) {
            modelo.addRow(new Object[]{c.id, c.nome, c.telefone, c.servicos(), c.mensalidade(), c.divida, c.registo.estado,
                c.registo.inactivo() && c.registo.data != null ? c.registo.data.toLocalDate().format(DATA) : "",
                c.registo.inactivo() ? "Reactivar…" : "Desistir…"});
        }
        aPreencher = false;
        lbEstado.setText(visiveis.size() + " cliente(s)");
        mostrarHistorico();
    }

    private void mostrarHistorico() {
        modeloHistorico.setRowCount(0);
        int r = tabela.getSelectedRow();
        if (r < 0 || r >= visiveis.size()) {
            lbHistorico.setText("Seleccione um cliente para ver quando saiu e quando voltou.");
            return;
        }
        Cliente c = visiveis.get(tabela.convertRowIndexToModel(r));
        lbHistorico.setText(c.nome);
        for (EstadoCliente.Movimento m : EstadoCliente.historico(c.id)) {
            modeloHistorico.addRow(new Object[]{m.data == null ? "" : m.data.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
                m.estado.texto(), m.motivo, m.valor.signum() == 0 ? "" : CfMethods.formatarComoMoeda(m.valor.doubleValue())});
        }
        if (modeloHistorico.getRowCount() == 0) {
            modeloHistorico.addRow(new Object[]{"", "Activo", "Sempre activo (sem saídas registadas)", ""});
        }
    }

    // =====================================================================
    // MUDANÇAS DE ESTADO
    // =====================================================================
    private void accaoDaLinha(Cliente c) {
        if (c.registo.inactivo()) {
            reactivar(c);
        } else {
            mudarEstado(c, EstadoCliente.Estado.DESISTENTE);
        }
    }

    private void mudarEstado(Cliente c, EstadoCliente.Estado novo) {
        if (novo == EstadoCliente.Estado.ACTIVO) {
            reactivar(c);
            return;
        }
        JComboBox<EstadoCliente.Estado> cmbNovo = new JComboBox<>(new EstadoCliente.Estado[]{
            EstadoCliente.Estado.DESISTENTE, EstadoCliente.Estado.DESACTIVADO});
        cmbNovo.setSelectedItem(novo);
        JComboBox<String> cmbMotivo = new JComboBox<>(MOTIVOS);
        cmbMotivo.setEditable(true);
        FormularioWeb.combo(cmbNovo, cmbMotivo);
        String aviso = c.divida.signum() > 0
                ? "Fica com " + CfMethods.formatarComoMoeda(c.divida.doubleValue()) + " em dívida (" + c.mesesAtraso
                + " mês(es)) até hoje.\nEssa dívida fica registada e pode ser cobrada quando voltar."
                : "Não tem dívida.";
        JPanel p = FormularioWeb.pilha(10,
                FormularioWeb.dica("A partir de hoje deixa de gerar mensalidades e de aparecer nos alertas."),
                FormularioWeb.grelha(200, 2, ComponentesWeb.campo("Novo estado", cmbNovo),
                        ComponentesWeb.campo("Motivo (escolha ou escreva)", cmbMotivo)),
                new JLabel("<html>" + aviso.replace("\n", "<br>") + "</html>"));
        p.setPreferredSize(new Dimension(520, 150));
        if (JOptionPane.showConfirmDialog(this, p, "Saída de " + c.nome, JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE) != JOptionPane.OK_OPTION) {
            mostrar(); // repõe a caixa
            return;
        }
        EstadoCliente.Estado escolhido = (EstadoCliente.Estado) cmbNovo.getSelectedItem();
        String motivo = String.valueOf(cmbMotivo.getSelectedItem());
        if (EstadoCliente.alterar(c.id, escolhido, motivo, BigDecimal.ZERO)) {
            lbEstado.setText(c.nome + " passou a " + escolhido.texto().toLowerCase() + ".");
            carregar();
        } else {
            JOptionPane.showMessageDialog(this, "Não foi possível gravar.");
            mostrar();
        }
    }

    private void reactivar(Cliente c) {
        if (!c.registo.inactivo()) {
            JOptionPane.showMessageDialog(this, c.nome + " já está activo.");
            mostrar();
            return;
        }
        ReactivarClienteDialog.abrir(this, conexao, c.id);
        carregar();
    }

    // =====================================================================
    // RENDERERS / EDITOR DO BOTÃO
    // =====================================================================
    private final class BotaoLinha extends AbstractCellEditor implements TableCellRenderer, TableCellEditor {

        private final JButton render = new JButton();
        private final JButton editor = new JButton();
        private Cliente actual;

        BotaoLinha() {
            for (JButton b : new JButton[]{render, editor}) {
                b.setFocusable(false);
            }
            editor.addActionListener(e -> {
                Cliente c = actual;
                fireEditingStopped();
                if (c != null) {
                    javax.swing.SwingUtilities.invokeLater(() -> accaoDaLinha(c));
                }
            });
        }

        private void estilo(JButton b, Object valor) {
            boolean reactivar = "Reactivar…".equals(valor);
            ComponentesWeb.botao(b, reactivar ? ComponentesWeb.Variante.SUCESSO : ComponentesWeb.Variante.SECUNDARIO, null,
                    reactivar ? "Reactivar o cliente (com taxa, dívida antiga e 1.º mês)" : "Marcar como desistente ou desactivado");
            b.setText(String.valueOf(valor));
            b.setBorder(BorderFactory.createEmptyBorder(4, 10, 4, 10));
        }

        @Override
        public Component getTableCellRendererComponent(JTable t, Object v, boolean sel, boolean foco, int r, int c) {
            estilo(render, v);
            return render;
        }

        @Override
        public Component getTableCellEditorComponent(JTable t, Object v, boolean sel, int r, int c) {
            actual = visiveis.get(t.convertRowIndexToModel(r));
            estilo(editor, v);
            return editor;
        }

        @Override
        public Object getCellEditorValue() {
            return editor.getText();
        }
    }

    private static final class RendererEstado extends EstiloUI.ZebraRenderer {

        RendererEstado() {
            super(SwingConstants.LEFT);
        }

        @Override
        public Component getTableCellRendererComponent(JTable t, Object v, boolean sel, boolean foco, int r, int c) {
            EstadoCliente.Estado e = v instanceof EstadoCliente.Estado ? (EstadoCliente.Estado) v : EstadoCliente.Estado.ACTIVO;
            Component comp = super.getTableCellRendererComponent(t, "●  " + e.texto(), sel, foco, r, c);
            if (!sel) {
                comp.setForeground(e == EstadoCliente.Estado.ACTIVO ? ComponentesWeb.VERDE.darker()
                        : e == EstadoCliente.Estado.DESISTENTE ? ComponentesWeb.VERMELHO : ComponentesWeb.LARANJA.darker());
            }
            comp.setFont(EstiloUI.FONTE_NEGRITO);
            return comp;
        }

        /** Seta à direita: a célula é uma caixa de escolha. */
        @Override
        protected void paintComponent(java.awt.Graphics g) {
            super.paintComponent(g);
            java.awt.Graphics2D g2 = ComponentesWeb.suave(g);
            int w = getWidth(), h = getHeight();
            g2.setColor(ComponentesWeb.TEXTO_SUAVE);
            int x = w - 16, y = h / 2 - 2;
            g2.fillPolygon(new int[]{x, x + 8, x + 4}, new int[]{y, y, y + 5}, 3);
            g2.dispose();
        }
    }

    private static final class RendererMensalidade extends EstiloUI.ZebraRenderer {

        RendererMensalidade() {
            super(SwingConstants.LEFT);
        }

        @Override
        public Component getTableCellRendererComponent(JTable t, Object v, boolean sel, boolean foco, int r, int c) {
            Component comp = super.getTableCellRendererComponent(t, v, sel, foco, r, c);
            if (!sel) {
                String s = String.valueOf(v);
                comp.setForeground(s.startsWith("Em atraso") || s.startsWith("Vence") ? ComponentesWeb.VERMELHO
                        : s.startsWith("Sem") ? ComponentesWeb.TEXTO_SUAVE : ComponentesWeb.VERDE.darker());
            }
            return comp;
        }
    }

    private static final class RendererMoeda extends EstiloUI.ZebraRenderer {

        RendererMoeda() {
            super(SwingConstants.RIGHT);
        }

        @Override
        public Component getTableCellRendererComponent(JTable t, Object v, boolean sel, boolean foco, int r, int c) {
            Object texto = v instanceof BigDecimal
                    ? (((BigDecimal) v).signum() == 0 ? "—" : CfMethods.formatarComoMoeda(((BigDecimal) v).doubleValue())) : v;
            return super.getTableCellRendererComponent(t, texto, sel, foco, r, c);
        }
    }
}
