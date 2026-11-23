package comercial.ginasio;

import comercial.ginasio.PedidosGinasio.Categoria;
import comercial.ginasio.PedidosGinasio.Cliente;
import comercial.ginasio.PedidosGinasio.Pedido;
import comercial.ginasio.PedidosGinasio.Produto;
import comercial.ginasio.PedidosGinasio.ResumoCliente;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import util.ui.ComponentesWeb;
import util.ui.EstiloUI;
import util.ui.MenuWeb;
import util.ui.MesasWeb;
import util.ui.Tema;
import visao.design.CampoPesquisa;

/**
 * Pedidos dos clientes do ginásio, como uma gestão de mesas em que as mesas
 * são os clientes: em cima os clientes com pedidos em aberto (cartões numa
 * lista horizontal), à esquerda o catálogo (categorias em cartões de cor,
 * produtos com fotografia, pesquisa por código interno, código de barras ou
 * nome) e à direita os pedidos do cliente escolhido, com o total e o botão
 * "Adicionar à facturação", que os passa para a tabela da janela de vendas.
 * 2026-11-21.
 */
public final class PedidosGinasioDialog extends JDialog {

    /** Quem recebe os pedidos na venda; devolve os ids que entraram de facto. */
    public interface Receptor {

        List<Integer> receber(int idCliente, String nomeCliente, List<Pedido> pedidos);
    }

    private static final String KZ = " Kz";

    private final int idUsuario;
    private final String operador;
    private final int armazem;
    private final Set<Integer> naVenda;
    private final Receptor receptor;
    private final Frame donoFrame;

    // clientes
    private final DefaultListModel<ResumoCliente> modeloClientes = new DefaultListModel<>();
    private final JList<ResumoCliente> listaClientes = new JList<>(modeloClientes);
    private final CardLayout cartasClientes = new CardLayout();
    private final JPanel painelClientes = new JPanel(cartasClientes);
    private ResumoCliente clienteActual;
    private boolean aSeleccionar;

    // pedidos do cliente
    private final DefaultTableModel modeloPedidos = new DefaultTableModel(
            new Object[]{"Hora", "Produto", "Qtd", "Preço", "Total"}, 0) {
        @Override
        public boolean isCellEditable(int r, int c) {
            return false;
        }
    };
    private final JTable tabela = new JTable(modeloPedidos);
    private List<Pedido> pedidosActuais = new ArrayList<>();
    private final Set<Integer> criadosNestaSessao = new HashSet<>();
    private final Avatar avatarCliente = new Avatar(46);
    private final JLabel lbClienteNome = new JLabel(" ");
    private final JLabel lbClienteInfo = new JLabel(" ");
    private final JLabel lbTotal = new JLabel("0,00" + KZ);
    private final JLabel lbItens = new JLabel(" ");
    private final CardLayout cartasPedido = new CardLayout();
    private final JPanel painelPedido = new JPanel(cartasPedido);
    private final ComponentesWeb.Etiqueta aviso = new ComponentesWeb.Etiqueta(" ", ComponentesWeb.VERDE);
    private final Timer esconderAviso;

    // catálogo
    private final MesasWeb.PainelGrelha grelha = new MesasWeb.PainelGrelha(150, 10);
    private final JScrollPane rolagemGrelha = new JScrollPane(grelha);
    private final JButton btVoltar = new JButton("Categorias");
    private final JLabel lbCaminho = new JLabel("Categorias");
    private final CampoPesquisa pesquisa = new CampoPesquisa("Código interno, código de barras ou nome do produto…");
    private final Timer temporizadorPesquisa;
    private Categoria categoriaActual;
    private int geracaoCatalogo;

    // cabeçalho
    private final ComponentesWeb.Etiqueta kpiClientes = new ComponentesWeb.Etiqueta(" ", Color.WHITE);
    private final ComponentesWeb.Etiqueta kpiTotal = new ComponentesWeb.Etiqueta(" ", Color.WHITE);

    // acções
    private final JButton btNovoCliente = new JButton("Cliente");
    private final JButton btFacturar = new JButton("Adicionar à facturação");
    private final JButton btFechar = new JButton("Fechar");
    private final JButton btMais = new JButton("+");
    private final JButton btMenos = new JButton("−");
    private final JButton btAnular = new JButton("Anular");

    /**
     * @param dono             janela de vendas
     * @param idClienteInicial cliente escolhido na venda (0 = nenhum / Consumidor Final)
     * @param naVenda          ids dos pedidos que já estão na tabela da venda (não aparecem aqui)
     */
    public PedidosGinasioDialog(Window dono, int idUsuario, String operador, int armazem, int idClienteInicial,
            String nomeClienteInicial, Set<Integer> naVenda, Receptor receptor) {
        super(dono, "Pedidos dos clientes", ModalityType.APPLICATION_MODAL);
        this.idUsuario = idUsuario;
        this.operador = operador;
        this.armazem = armazem;
        this.naVenda = naVenda == null ? Collections.<Integer>emptySet() : naVenda;
        this.receptor = receptor;
        this.donoFrame = dono instanceof Frame ? (Frame) dono : null;
        visao.design.Tema.definirCorNegocio(Tema.corCampo());
        setIconImages(MenuWeb.iconesAplicacao());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        esconderAviso = new Timer(2600, e -> aviso.setVisible(false));
        esconderAviso.setRepeats(false);
        temporizadorPesquisa = new Timer(280, e -> pesquisar());
        temporizadorPesquisa.setRepeats(false);

        montar();
        atalhos();
        tamanho(dono);

        recarregarClientes();
        if (idClienteInicial > 0) {
            seleccionarCliente(idClienteInicial, nomeClienteInicial);
        } else if (!modeloClientes.isEmpty()) {
            listaClientes.setSelectedIndex(0);
        } else {
            mostrarCliente(null);
        }
        mostrarCategorias();
    }

    // =====================================================================
    // MONTAGEM
    // =====================================================================
    private void montar() {
        JPanel raiz = new JPanel(new BorderLayout());
        raiz.setBackground(ComponentesWeb.FUNDO_PAGINA);
        raiz.add(cabecalho(), BorderLayout.NORTH);

        JPanel centro = new JPanel(new BorderLayout(0, 10));
        centro.setOpaque(false);
        centro.setBorder(new EmptyBorder(10, 12, 6, 12));
        centro.add(faixaClientes(), BorderLayout.NORTH);

        JSplitPane divisao = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, catalogo(), painelDoCliente());
        divisao.setResizeWeight(0.56);
        divisao.setBorder(null);
        divisao.setOpaque(false);
        divisao.setDividerSize(10);
        divisao.setContinuousLayout(true);
        centro.add(divisao, BorderLayout.CENTER);
        raiz.add(centro, BorderLayout.CENTER);

        JPanel rodape = new JPanel(new BorderLayout());
        rodape.setBackground(Color.WHITE);
        rodape.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, ComponentesWeb.LINHA), new EmptyBorder(6, 14, 6, 14)));
        JLabel dica = new JLabel("F2 procurar produto  ·  F4 lupa  ·  Enter lança pelo código  ·  F3 escolher cliente  ·  "
                + "Ctrl+Enter adicionar à facturação  ·  Esc fechar");
        dica.setFont(EstiloUI.FONTE.deriveFont(11.5f));
        dica.setForeground(ComponentesWeb.TEXTO_SUAVE);
        rodape.add(dica, BorderLayout.WEST);
        raiz.add(rodape, BorderLayout.SOUTH);
        setContentPane(raiz);
    }

    private JComponent cabecalho() {
        JPanel cab = new ComponentesWeb.PainelGradiente(new BorderLayout(14, 0), EstiloUI.AZUL_ESCURO, Tema.gradienteFim());
        cab.setBorder(new EmptyBorder(12, 18, 12, 18));
        JComponent emblema = new JComponent() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = ComponentesWeb.suave(g);
                Color d = Tema.amostra(Tema.actual())[2];
                g2.setPaint(new GradientPaint(0, 0, ComponentesWeb.mistura(d, Color.WHITE, 0.35), 44, 44, d));
                g2.fillRoundRect(0, 0, 44, 44, 14, 14);
                MenuWeb.desenharSimbolo(g2, MenuWeb.Simbolo.PRANCHETA, 9, 9, 26, EstiloUI.AZUL_ESCURO);
                g2.dispose();
            }
        };
        emblema.setPreferredSize(new Dimension(44, 44));
        JLabel titulo = new JLabel("Pedidos dos clientes");
        titulo.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(21f));
        titulo.setForeground(Color.WHITE);
        JLabel sub = new JLabel(util.ui.Tema.actual() == util.ui.Tema.Negocio.RESTAURANTE
                ? "O que o cliente vai pedindo fica aqui, por cliente, e paga-se tudo de uma vez no fim."
                : "O que o sócio pede durante o treino fica aqui, por cliente, e paga-se tudo de uma vez no fim.");
        sub.setFont(EstiloUI.FONTE.deriveFont(12.5f));
        sub.setForeground(Tema.claro());
        JPanel textos = ComponentesWeb.transparente(new GridLayout(2, 1));
        textos.add(titulo);
        textos.add(sub);
        JPanel marca = ComponentesWeb.transparente(new BorderLayout(12, 0));
        marca.add(emblema, BorderLayout.WEST);
        marca.add(textos, BorderLayout.CENTER);
        cab.add(marca, BorderLayout.CENTER);

        JPanel kpis = ComponentesWeb.transparente(new FlowLayout(FlowLayout.RIGHT, 8, 10));
        for (ComponentesWeb.Etiqueta e : new ComponentesWeb.Etiqueta[]{kpiClientes, kpiTotal}) {
            e.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(12.5f));
            e.setCor(ComponentesWeb.mistura(Tema.amostra(Tema.actual())[2], Color.WHITE, 0.4));
            kpis.add(e);
        }
        cab.add(kpis, BorderLayout.EAST);
        return cab;
    }

    private JComponent faixaClientes() {
        listaClientes.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        listaClientes.setVisibleRowCount(1);
        listaClientes.setFixedCellWidth(232);
        listaClientes.setFixedCellHeight(70);
        listaClientes.setOpaque(false);
        listaClientes.setBackground(ComponentesWeb.FUNDO_PAGINA);
        listaClientes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaClientes.setCellRenderer(new RendererCliente());
        listaClientes.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && !aSeleccionar) {
                mostrarCliente(listaClientes.getSelectedValue());
            }
        });
        JScrollPane rolagem = new JScrollPane(listaClientes, ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        rolagem.setBorder(null);
        rolagem.setOpaque(false);
        rolagem.getViewport().setOpaque(false);
        rolagem.getHorizontalScrollBar().setUnitIncrement(40);
        rolagem.setPreferredSize(new Dimension(400, 84));

        JLabel vazio = new JLabel("<html><b>Ainda não há pedidos em aberto.</b> Escolha o cliente em "
                + "<b>+ Cliente</b> (ou na venda) e toque nos produtos que ele pedir.</html>");
        vazio.setFont(EstiloUI.FONTE.deriveFont(13f));
        vazio.setForeground(ComponentesWeb.TEXTO_SUAVE);
        vazio.setBorder(new EmptyBorder(0, 14, 0, 0));
        painelClientes.setOpaque(false);
        painelClientes.add(rolagem, "lista");
        painelClientes.add(vazio, "vazio");

        ComponentesWeb.botao(btNovoCliente, ComponentesWeb.Variante.PRIMARIO, null,
                "Escolher outro cliente ou cadastrar um novo (F3)");
        btNovoCliente.setIcon(new IconeMais(14, Color.WHITE));
        btNovoCliente.addActionListener(e -> escolherCliente());
        JPanel botao = ComponentesWeb.transparente(new BorderLayout());
        botao.add(btNovoCliente, BorderLayout.CENTER);
        botao.setBorder(new EmptyBorder(18, 0, 18, 6));

        ComponentesWeb.Cartao cartao = new ComponentesWeb.Cartao("Clientes com pedidos em aberto",
                Tema.amostra(Tema.actual())[1], new BorderLayout(8, 0));
        cartao.getCorpo().add(botao, BorderLayout.WEST);
        cartao.getCorpo().add(painelClientes, BorderLayout.CENTER);
        cartao.getCorpo().setBorder(new EmptyBorder(0, 10, 6, 10));
        return cartao;
    }

    private JComponent catalogo() {
        ComponentesWeb.botao(btVoltar, ComponentesWeb.Variante.SECUNDARIO, null, "Voltar às categorias");
        btVoltar.setText("‹  Categorias");
        btVoltar.addActionListener(e -> {
            pesquisa.setText("");
            mostrarCategorias();
        });
        lbCaminho.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(15f));
        lbCaminho.setForeground(ComponentesWeb.TEXTO);

        pesquisa.setPreferredSize(new Dimension(260, 38));
        pesquisa.setToolTipText("<html>Enter: lança o produto do <b>código interno</b>, <b>código de barras</b> ou código manual."
                + "<br>Escreva 2 letras ou mais para procurar pelo <b>nome</b> em todas as categorias.</html>");
        pesquisa.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                temporizadorPesquisa.restart();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                temporizadorPesquisa.restart();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                temporizadorPesquisa.restart();
            }
        });
        pesquisa.addActionListener(e -> lancarPorCodigo());

        JPanel caminho = ComponentesWeb.transparente(new FlowLayout(FlowLayout.LEFT, 8, 0));
        caminho.add(btVoltar);
        caminho.add(lbCaminho);
        aviso.setVisible(false);
        JPanel linhaCaminho = ComponentesWeb.transparente(new BorderLayout(8, 0));
        linhaCaminho.add(caminho, BorderLayout.CENTER);
        linhaCaminho.add(aviso, BorderLayout.EAST);
        // lupa: a pesquisa completa de produtos (a mesma da venda e da gestão de mesas)
        JButton btLupa = new JButton("Pesquisar");
        ComponentesWeb.botao(btLupa, ComponentesWeb.Variante.SECUNDARIO, "/imagens/proucura.png",
                "Pesquisar um produto na lista completa (F4)");
        btLupa.putClientProperty("web.pilula", Boolean.TRUE);
        btLupa.addActionListener(e -> abrirLupa());
        JPanel linhaPesquisa = ComponentesWeb.transparente(new BorderLayout(8, 0));
        linhaPesquisa.add(pesquisa, BorderLayout.CENTER);
        linhaPesquisa.add(btLupa, BorderLayout.EAST);

        JPanel topo = ComponentesWeb.transparente(new BorderLayout(0, 8));
        topo.add(linhaPesquisa, BorderLayout.NORTH);
        topo.add(linhaCaminho, BorderLayout.CENTER);

        rolagemGrelha.setBorder(null);
        rolagemGrelha.setOpaque(false);
        rolagemGrelha.getViewport().setOpaque(false);
        rolagemGrelha.getVerticalScrollBar().setUnitIncrement(24);
        rolagemGrelha.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        ComponentesWeb.Cartao cartao = new ComponentesWeb.Cartao("Catálogo", ComponentesWeb.AZUL_VIVO, new BorderLayout(0, 10));
        cartao.getCorpo().add(topo, BorderLayout.NORTH);
        cartao.getCorpo().add(rolagemGrelha, BorderLayout.CENTER);
        cartao.setMinimumSize(new Dimension(340, 300));
        return cartao;
    }

    private JComponent painelDoCliente() {
        // cabeçalho do cliente
        lbClienteNome.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(18f));
        lbClienteNome.setForeground(ComponentesWeb.TEXTO);
        lbClienteInfo.setFont(EstiloUI.FONTE.deriveFont(12.5f));
        lbClienteInfo.setForeground(ComponentesWeb.TEXTO_SUAVE);
        JPanel nomes = ComponentesWeb.transparente(new GridLayout(2, 1));
        nomes.add(lbClienteNome);
        nomes.add(lbClienteInfo);
        JPanel cab = ComponentesWeb.transparente(new BorderLayout(12, 0));
        cab.add(avatarCliente, BorderLayout.WEST);
        cab.add(nomes, BorderLayout.CENTER);

        // tabela
        EstiloUI.estilizarTabela(tabela, true);
        tabela.setRowHeight(32);
        tabela.setShowVerticalLines(false);
        EstiloUI.ZebraRenderer direita = new EstiloUI.ZebraRenderer(SwingConstants.RIGHT);
        EstiloUI.ZebraRenderer centro = new EstiloUI.ZebraRenderer(SwingConstants.CENTER);
        tabela.getColumnModel().getColumn(0).setCellRenderer(centro);
        for (int c = 2; c <= 4; c++) {
            tabela.getColumnModel().getColumn(c).setCellRenderer(c == 2 ? centro : direita);
        }
        int[] larguras = {70, 260, 60, 110, 120};
        for (int c = 0; c < larguras.length; c++) {
            tabela.getColumnModel().getColumn(c).setPreferredWidth(larguras[c]);
        }
        tabela.getSelectionModel().addListSelectionListener(e -> actualizarBotoesLinha());
        tabela.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    mudarQuantidade(+1);
                }
            }
        });
        JScrollPane rolTabela = new JScrollPane(tabela);
        rolTabela.setBorder(BorderFactory.createLineBorder(ComponentesWeb.LINHA));
        rolTabela.getViewport().setBackground(Color.WHITE);

        // botões da linha
        ComponentesWeb.botao(btMenos, ComponentesWeb.Variante.SECUNDARIO, null, "Tirar uma unidade à linha seleccionada");
        ComponentesWeb.botao(btMais, ComponentesWeb.Variante.SECUNDARIO, null, "Mais uma unidade na linha seleccionada (ou duplo clique)");
        ComponentesWeb.botao(btAnular, ComponentesWeb.Variante.AVISO, null, "Anular o pedido seleccionado (fica registado como anulado)");
        for (JButton b : new JButton[]{btMenos, btMais}) {
            b.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(20f));
            b.setBorder(new EmptyBorder(4, 16, 6, 16));
        }
        btMenos.addActionListener(e -> mudarQuantidade(-1));
        btMais.addActionListener(e -> mudarQuantidade(+1));
        btAnular.addActionListener(e -> anularLinha());
        JPanel linha = ComponentesWeb.transparente(new FlowLayout(FlowLayout.LEFT, 6, 0));
        linha.add(btMenos);
        linha.add(btMais);
        linha.add(btAnular);

        // total e acções
        lbItens.setFont(EstiloUI.FONTE.deriveFont(12.5f));
        lbItens.setForeground(ComponentesWeb.TEXTO_SUAVE);
        lbTotal.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(26f));
        lbTotal.setForeground(EstiloUI.AZUL_ESCURO);
        lbTotal.setHorizontalAlignment(SwingConstants.RIGHT);
        lbItens.setHorizontalAlignment(SwingConstants.RIGHT);
        JPanel totais = ComponentesWeb.transparente(new BorderLayout());
        JLabel lbT = new JLabel("TOTAL A PAGAR");
        lbT.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(11f));
        lbT.setForeground(ComponentesWeb.TEXTO_SUAVE);
        lbT.setHorizontalAlignment(SwingConstants.RIGHT);
        totais.add(lbT, BorderLayout.NORTH);
        totais.add(lbTotal, BorderLayout.CENTER);
        totais.add(lbItens, BorderLayout.SOUTH);
        JPanel linhaP = ComponentesWeb.transparente(new BorderLayout());
        linha.setLayout(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        linhaP.add(linha, BorderLayout.CENTER);
        linhaP.setBorder(new EmptyBorder(8, 0, 0, 0));
        cab.add(linhaP, BorderLayout.EAST);

        ComponentesWeb.botao(btFacturar, ComponentesWeb.Variante.SUCESSO, null,
                "Passa todos os pedidos deste cliente para a tabela da venda (Ctrl+Enter)");
        btFacturar.setIcon(new IconeFactura(18, Color.WHITE));
        btFacturar.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(15f));
        btFacturar.setBorder(new EmptyBorder(12, 22, 12, 22));
        btFacturar.addActionListener(e -> facturar());
        ComponentesWeb.botao(btFechar, ComponentesWeb.Variante.SECUNDARIO, null, "Voltar à venda (Esc)");
        btFechar.setBorder(new EmptyBorder(12, 18, 12, 18));
        btFechar.addActionListener(e -> dispose());
        JPanel botoesFim = ComponentesWeb.transparente(new FlowLayout(FlowLayout.LEFT, 8, 0));
        botoesFim.add(btFechar);
        botoesFim.add(btFacturar);
        // lado a lado quando cabe; em janelas estreitas o total passa para a linha de baixo
        JPanel accoes = ComponentesWeb.transparente(new EstiloUI.WrapLayout(FlowLayout.RIGHT, 14, 2));
        accoes.add(botoesFim);
        accoes.add(totais);

        JPanel sul = ComponentesWeb.transparente(new BorderLayout());
        sul.add(accoes, BorderLayout.CENTER);

        JPanel comCliente = ComponentesWeb.transparente(new BorderLayout(0, 10));
        comCliente.add(cab, BorderLayout.NORTH);
        comCliente.add(rolTabela, BorderLayout.CENTER);
        comCliente.add(sul, BorderLayout.SOUTH);

        // estado vazio
        JPanel semCliente = ComponentesWeb.transparente(new BorderLayout());
        JComponent ilustracao = new JComponent() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = ComponentesWeb.suave(g);
                int cx = getWidth() / 2, cy = getHeight() / 2 - 40;
                Color v = Tema.amostra(Tema.actual())[1];
                g2.setColor(ComponentesWeb.mistura(v, Color.WHITE, 0.88));
                g2.fillOval(cx - 60, cy - 60, 120, 120);
                MenuWeb.desenharSimbolo(g2, MenuWeb.Simbolo.PESSOAS, cx - 32, cy - 32, 64, v);
                g2.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(16f));
                centrar(g2, "Escolha um cliente", cx, cy + 92, ComponentesWeb.TEXTO);
                g2.setFont(EstiloUI.FONTE.deriveFont(13f));
                centrar(g2, "nos cartões em cima ou em “+ Cliente”,", cx, cy + 116, ComponentesWeb.TEXTO_SUAVE);
                centrar(g2, "e toque nos produtos que ele pedir.", cx, cy + 135, ComponentesWeb.TEXTO_SUAVE);
                g2.dispose();
            }
        };
        semCliente.add(ilustracao, BorderLayout.CENTER);

        painelPedido.setOpaque(false);
        painelPedido.add(comCliente, "cliente");
        painelPedido.add(semCliente, "vazio");

        ComponentesWeb.Cartao cartao = new ComponentesWeb.Cartao("Pedido do cliente", ComponentesWeb.VERDE, new BorderLayout());
        cartao.getCorpo().add(painelPedido, BorderLayout.CENTER);
        cartao.setMinimumSize(new Dimension(380, 300));
        return cartao;
    }

    private void atalhos() {
        JComponent r = getRootPane();
        teclas(r, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "fechar", this::dispose);
        teclas(r, KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0), "procurar", () -> {
            pesquisa.requestFocusInWindow();
            pesquisa.selectAll();
        });
        teclas(r, KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0), "cliente", this::escolherCliente);
        teclas(r, KeyStroke.getKeyStroke(KeyEvent.VK_F4, 0), "lupa", this::abrirLupa);
        teclas(r, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, java.awt.event.InputEvent.CTRL_DOWN_MASK), "facturar", this::facturar);
    }

    private static void teclas(JComponent c, KeyStroke k, String nome, Runnable accao) {
        c.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(k, nome);
        c.getActionMap().put(nome, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                accao.run();
            }
        });
    }

    /** Mais pequeno que a janela de vendas, ao centro dela. */
    private void tamanho(Window dono) {
        Rectangle util = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        Rectangle base = dono != null && dono.isShowing() ? dono.getBounds() : util;
        int w = Math.max(Math.min(960, util.width), (int) (base.width * 0.86));
        int h = Math.max(Math.min(640, util.height), (int) (base.height * 0.86));
        setMinimumSize(new Dimension(Math.min(900, util.width), Math.min(600, util.height)));
        setSize(Math.min(w, util.width), Math.min(h, util.height));
        setLocationRelativeTo(dono);
    }

    // =====================================================================
    // CLIENTES
    // =====================================================================
    private void recarregarClientes() {
        int seleccionado = clienteActual == null ? -1 : clienteActual.idCliente;
        List<ResumoCliente> l;
        try {
            l = PedidosGinasio.clientesEmAberto(naVenda);
        } catch (Exception e) {
            e.printStackTrace();
            erro("Não foi possível ler os pedidos em aberto", e);
            l = new ArrayList<>();
        }
        aSeleccionar = true;
        try {
            modeloClientes.clear();
            int indice = -1;
            for (ResumoCliente r : l) {
                if (r.idCliente == seleccionado) {
                    indice = modeloClientes.size();
                }
                modeloClientes.addElement(r);
            }
            // cliente novo (ainda sem pedidos): fica à frente, para se ver quem está escolhido
            if (indice < 0 && clienteActual != null) {
                modeloClientes.add(0, new ResumoCliente(clienteActual.idCliente, clienteActual.nome, 0, 0, null));
                indice = 0;
            }
            if (indice >= 0) {
                listaClientes.setSelectedIndex(indice);
                listaClientes.ensureIndexIsVisible(indice);
            }
        } finally {
            aSeleccionar = false;
        }
        cartasClientes.show(painelClientes, modeloClientes.isEmpty() ? "vazio" : "lista");

        double total = 0;
        int clientes = 0;
        for (ResumoCliente r : l) {
            total += r.total;
            clientes++;
        }
        kpiClientes.setText(clientes + (clientes == 1 ? " cliente com pedidos" : " clientes com pedidos"));
        kpiTotal.setText("Por receber: " + moeda(total));
    }

    /** Escolhe um cliente (com ou sem pedidos) e mostra os pedidos dele. */
    private void seleccionarCliente(int idCliente, String nome) {
        if (!clienteAceite(idCliente)) {
            return;
        }
        clienteActual = new ResumoCliente(idCliente, nome, 0, 0, null);
        recarregarClientes();
        mostrarCliente(listaClientes.getSelectedValue() != null ? listaClientes.getSelectedValue() : clienteActual);
    }

    private void mostrarCliente(ResumoCliente r) {
        clienteActual = r;
        if (r == null) {
            pedidosActuais = new ArrayList<>();
            cartasPedido.show(painelPedido, "vazio");
            return;
        }
        try {
            pedidosActuais = PedidosGinasio.pedidosDoCliente(r.idCliente, naVenda);
        } catch (Exception e) {
            erro("Não foi possível ler os pedidos de " + r.nome, e);
            pedidosActuais = new ArrayList<>();
        }
        avatarCliente.setNome(r.nome);
        lbClienteNome.setText(r.nome);
        if (pedidosActuais.isEmpty()) {
            lbClienteInfo.setText("Sem pedidos em aberto: toque nos produtos para registar o primeiro.");
        } else {
            lbClienteInfo.setText("Pedidos desde as " + PedidosGinasio.hora(pedidosActuais.get(0).data)
                    + "  ·  há " + MesasWeb.tempo(minutosDesde(pedidosActuais.get(0).data), pedidosActuais.get(0).data));
        }
        modeloPedidos.setRowCount(0);
        double total = 0, itens = 0;
        for (Pedido p : pedidosActuais) {
            modeloPedidos.addRow(new Object[]{PedidosGinasio.hora(p.data), p.designacao, quantidade(p.quantidade),
                moeda(p.precoComIva()), moeda(p.total())});
            total += p.total();
            itens += p.quantidade;
        }
        lbTotal.setText(moeda(total));
        lbItens.setText(pedidosActuais.isEmpty() ? " " : quantidade(itens) + (itens == 1 ? " artigo" : " artigos")
                + " em " + pedidosActuais.size() + (pedidosActuais.size() == 1 ? " pedido" : " pedidos"));
        btFacturar.setEnabled(!pedidosActuais.isEmpty());
        actualizarBotoesLinha();
        cartasPedido.show(painelPedido, "cliente");
    }

    private void actualizarBotoesLinha() {
        boolean linha = tabela.getSelectedRow() >= 0;
        btMais.setEnabled(linha);
        btMenos.setEnabled(linha);
        btAnular.setEnabled(linha);
    }

    /** Só clientes cadastrados (nunca o Consumidor Final); senão avisa e devolve false. */
    private boolean clienteAceite(int idCliente) {
        String invalido;
        try {
            invalido = PedidosGinasio.validarCliente(idCliente);
        } catch (Exception e) {
            erro("Não foi possível confirmar o cliente", e);
            return false;
        }
        if (invalido != null) {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(this, invalido, "Cliente do pedido", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    /** Pesquisa de clientes (nome, telefone, NIF) com a opção de cadastrar um novo. */
    private void escolherCliente() {
        EscolhaCliente d = new EscolhaCliente();
        d.setVisible(true);
        if (d.escolhido != null) {
            seleccionarCliente(d.escolhido.codigo, d.escolhido.nome);
            pesquisa.requestFocusInWindow();
        }
    }

    // =====================================================================
    // CATÁLOGO
    // =====================================================================
    private void mostrarCategorias() {
        categoriaActual = null;
        btVoltar.setVisible(false);
        lbCaminho.setText("Categorias");
        int g = ++geracaoCatalogo;
        new SwingWorker<List<Categoria>, Void>() {
            @Override
            protected List<Categoria> doInBackground() throws Exception {
                return PedidosGinasio.categorias(armazem);
            }

            @Override
            protected void done() {
                if (g != geracaoCatalogo) {
                    return;
                }
                grelha.removeAll();
                grelha.setLarguraCelula(150, 10);
                try {
                    for (Categoria c : get()) {
                        grelha.add(new CartaoCategoria(c, () -> abrirCategoria(c)));
                    }
                } catch (Exception e) {
                    erro("Não foi possível ler as categorias", e);
                }
                if (grelha.getComponentCount() == 0) {
                    grelha.add(mensagemGrelha("Não há produtos vendáveis neste armazém."));
                }
                refrescarGrelha();
            }
        }.execute();
    }

    private void abrirCategoria(Categoria c) {
        categoriaActual = c;
        btVoltar.setVisible(true);
        lbCaminho.setText(c.designacao);
        carregarProdutos(() -> PedidosGinasio.produtosDaCategoria(c.codigo, armazem), "Esta categoria não tem produtos.");
    }

    private interface Consulta {

        List<Produto> ler() throws Exception;
    }

    private void carregarProdutos(Consulta consulta, String seVazio) {
        int g = ++geracaoCatalogo;
        new SwingWorker<List<Object[]>, Void>() {
            @Override
            protected List<Object[]> doInBackground() throws Exception {
                List<Object[]> l = new ArrayList<>();
                for (Produto p : consulta.ler()) {
                    l.add(new Object[]{p, foto(p.foto)});
                }
                return l;
            }

            @Override
            protected void done() {
                if (g != geracaoCatalogo) {
                    return;
                }
                grelha.removeAll();
                grelha.setLarguraCelula(150, 10);
                try {
                    for (Object[] o : get()) {
                        grelha.add(cartaoProduto((Produto) o[0], (Image) o[1]));
                    }
                } catch (Exception e) {
                    erro("Não foi possível ler os produtos", e);
                }
                if (grelha.getComponentCount() == 0) {
                    grelha.add(mensagemGrelha(seVazio));
                }
                refrescarGrelha();
            }
        }.execute();
    }

    private void refrescarGrelha() {
        grelha.revalidate();
        grelha.repaint();
        rolagemGrelha.getVerticalScrollBar().setValue(0);
    }

    private JComponent cartaoProduto(Produto p, Image foto) {
        String stock = null;
        if (p.stocavel && !p.servico) {
            stock = quantidade(p.stock);
        }
        return new MesasWeb.CartaoProduto(p.designacao, moeda(p.precoComIva()), stock, p.esgotado(), foto,
                MesasWeb.corCategoria(p.categoria), () -> adicionarProduto(p));
    }

    private static Image foto(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        try {
            Image i = new ImageIcon(bytes).getImage();
            return i.getWidth(null) > 0 ? i : null;
        } catch (Exception e) {
            return null;
        }
    }

    private static JComponent mensagemGrelha(String texto) {
        JLabel l = new JLabel(texto);
        l.setFont(EstiloUI.FONTE.deriveFont(13f));
        l.setForeground(ComponentesWeb.TEXTO_SUAVE);
        l.setBorder(new EmptyBorder(20, 6, 20, 6));
        return l;
    }

    /** Enquanto se escreve: 2 letras ou mais procuram pelo nome; vazio volta ao que estava. */
    private void pesquisar() {
        String t = pesquisa.getText().trim();
        if (t.isEmpty()) {
            if (categoriaActual != null) {
                abrirCategoria(categoriaActual);
            } else {
                mostrarCategorias();
            }
            return;
        }
        if (t.length() < 2 || t.matches("\\d+")) {
            return; // códigos: só com Enter
        }
        btVoltar.setVisible(true);
        lbCaminho.setText("Resultados para “" + t + "”");
        carregarProdutos(() -> PedidosGinasio.procurarPorNome(t, armazem), "Nenhum produto com “" + t + "” no nome.");
    }

    /** Lupa (F4): a pesquisa completa de produtos; o escolhido fica pedido para o cliente. */
    private void abrirLupa() {
        try {
            new visao.BuscaProdutoVisao(this, armazem, util.BDConexao.getInstancia(), codigo -> {
                try {
                    Produto p = PedidosGinasio.procurarPorCodigo(String.valueOf(codigo), armazem);
                    if (p == null) {
                        Toolkit.getDefaultToolkit().beep();
                        mostrarAviso("Produto indisponível neste armazém", ComponentesWeb.VERMELHO);
                    } else {
                        adicionarProduto(p);
                    }
                } catch (Exception e) {
                    erro("Erro ao lançar o produto", e);
                }
            }).setVisible(true);
        } catch (Exception e) {
            erro("Erro ao abrir a pesquisa de produtos", e);
        }
    }

    /** Enter na pesquisa: código interno, de barras ou manual; senão, o único resultado pelo nome. */
    private void lancarPorCodigo() {
        String t = pesquisa.getText().trim();
        if (t.isEmpty()) {
            return;
        }
        try {
            Produto p = PedidosGinasio.procurarPorCodigo(t, armazem);
            if (p == null && t.length() >= 2) {
                List<Produto> l = PedidosGinasio.procurarPorNome(t, armazem);
                if (l.size() == 1) {
                    p = l.get(0);
                }
            }
            if (p == null) {
                Toolkit.getDefaultToolkit().beep();
                mostrarAviso("Nenhum produto com o código “" + t + "”", ComponentesWeb.VERMELHO);
                pesquisa.selectAll();
                return;
            }
            if (adicionarProduto(p)) {
                pesquisa.setText("");
            }
        } catch (Exception e) {
            erro("Erro ao procurar o produto", e);
        }
    }

    // =====================================================================
    // PEDIDOS
    // =====================================================================
    /** Regista uma unidade do produto para o cliente escolhido. */
    private boolean adicionarProduto(Produto p) {
        if (clienteActual != null && PedidosGinasio.ehConsumidorFinal(clienteActual.nome)) {
            clienteActual = null; // o Consumidor Final nunca fica com pedidos
        }
        if (clienteActual == null) {
            Toolkit.getDefaultToolkit().beep();
            mostrarAviso("Escolha primeiro o cliente", ComponentesWeb.LARANJA);
            escolherCliente();
            if (clienteActual == null) {
                return false;
            }
        }
        if (p.esgotado() && JOptionPane.showConfirmDialog(this, p.designacao + " não tem stock no armazém.\n"
                + "Registar o pedido na mesma? (na facturação o stock volta a ser verificado)",
                "Sem stock", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) != JOptionPane.YES_OPTION) {
            return false;
        }
        try {
            // o mesmo produto tocado outra vez seguida: soma à última linha (desta sessão) em vez de criar outra
            Pedido ultimo = pedidosActuais.isEmpty() ? null : pedidosActuais.get(pedidosActuais.size() - 1);
            if (ultimo != null && ultimo.idProduto == p.codigo && criadosNestaSessao.contains(ultimo.id)) {
                PedidosGinasio.alterarQuantidade(ultimo.id, ultimo.quantidade + 1);
            } else {
                int id = PedidosGinasio.registar(clienteActual.idCliente, clienteActual.nome, p, 1, idUsuario, operador);
                criadosNestaSessao.add(id);
            }
            String nome = MesasWeb.primeiroEUltimo(clienteActual.nome);
            recarregarClientes();
            mostrarCliente(listaClientes.getSelectedValue() != null ? listaClientes.getSelectedValue() : clienteActual);
            int ultimaLinha = modeloPedidos.getRowCount() - 1;
            if (ultimaLinha >= 0) {
                tabela.setRowSelectionInterval(ultimaLinha, ultimaLinha);
                tabela.scrollRectToVisible(tabela.getCellRect(ultimaLinha, 0, true));
            }
            mostrarAviso("✓  " + p.designacao + "  →  " + nome, ComponentesWeb.VERDE);
            return true;
        } catch (Exception e) {
            erro("Não foi possível registar o pedido", e);
            return false;
        }
    }

    private Pedido linhaSeleccionada() {
        int r = tabela.getSelectedRow();
        return r < 0 || r >= pedidosActuais.size() ? null : pedidosActuais.get(tabela.convertRowIndexToModel(r));
    }

    private void mudarQuantidade(int delta) {
        Pedido p = linhaSeleccionada();
        if (p == null) {
            return;
        }
        double nova = p.quantidade + delta;
        if (nova <= 0) {
            anularLinha();
            return;
        }
        int linha = tabela.getSelectedRow();
        try {
            PedidosGinasio.alterarQuantidade(p.id, nova);
            recarregarClientes();
            mostrarCliente(clienteActual);
            if (linha < modeloPedidos.getRowCount()) {
                tabela.setRowSelectionInterval(linha, linha);
            }
        } catch (Exception e) {
            erro("Não foi possível alterar a quantidade", e);
        }
    }

    private void anularLinha() {
        Pedido p = linhaSeleccionada();
        if (p == null) {
            return;
        }
        if (JOptionPane.showConfirmDialog(this, "Anular o pedido de " + quantidade(p.quantidade) + " × " + p.designacao
                + "\n(" + PedidosGinasio.hora(p.data) + ", " + clienteActual.nome + ")?",
                "Anular pedido", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            PedidosGinasio.anular(p.id);
            recarregarClientes();
            mostrarCliente(clienteActual);
            mostrarAviso("Pedido anulado", ComponentesWeb.LARANJA);
        } catch (Exception e) {
            erro("Não foi possível anular o pedido", e);
        }
    }

    /** Passa os pedidos do cliente para a venda; fecha quando entram todos. */
    private void facturar() {
        if (clienteActual == null || pedidosActuais.isEmpty() || receptor == null) {
            Toolkit.getDefaultToolkit().beep();
            return;
        }
        List<Pedido> todos = new ArrayList<>(pedidosActuais);
        List<Integer> aceites = receptor.receber(clienteActual.idCliente, clienteActual.nome, todos);
        if (aceites == null || aceites.isEmpty()) {
            mostrarCliente(clienteActual);
            return;
        }
        if (aceites.size() >= todos.size()) {
            dispose();
            return;
        }
        recarregarClientes();
        mostrarCliente(clienteActual);
    }

    // =====================================================================
    // AJUDAS
    // =====================================================================
    private void mostrarAviso(String texto, Color cor) {
        aviso.setText(texto);
        aviso.setCor(cor);
        aviso.setVisible(true);
        esconderAviso.restart();
    }

    private void erro(String texto, Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, texto + ":\n" + e.getMessage(), "Pedidos", JOptionPane.ERROR_MESSAGE);
    }

    private static final java.text.DecimalFormat FORMATO_KZ;

    static {
        java.text.DecimalFormatSymbols s = new java.text.DecimalFormatSymbols(new java.util.Locale("pt", "PT"));
        s.setGroupingSeparator('.');
        s.setDecimalSeparator(',');
        FORMATO_KZ = new java.text.DecimalFormat("#,##0.00", s);
    }

    /** 5949.9885 -> "5.949,99 Kz". */
    static String moeda(double v) {
        synchronized (FORMATO_KZ) {
            return FORMATO_KZ.format(v) + KZ;
        }
    }

    static String quantidade(double q) {
        return q == Math.rint(q) ? String.valueOf((long) q) : String.valueOf(q);
    }

    private static long minutosDesde(java.util.Date d) {
        return d == null ? 0 : Math.max(0, (System.currentTimeMillis() - d.getTime()) / 60000L);
    }

    private static void centrar(Graphics2D g2, String t, int cx, int y, Color cor) {
        FontMetrics fm = g2.getFontMetrics();
        g2.setColor(cor);
        g2.drawString(t, cx - fm.stringWidth(t) / 2, y);
    }

    private static String cortar(String t, FontMetrics fm, int largura) {
        if (t == null) {
            return "";
        }
        if (fm.stringWidth(t) <= largura) {
            return t;
        }
        while (t.length() > 1 && fm.stringWidth(t + "…") > largura) {
            t = t.substring(0, t.length() - 1);
        }
        return t + "…";
    }

    /** Cor estável de um cliente (avatar). */
    static Color corDoNome(String nome) {
        Color[] p = {new Color(37, 99, 235), new Color(22, 163, 74), new Color(234, 88, 12), new Color(124, 58, 237),
            new Color(8, 145, 178), new Color(219, 39, 119), new Color(101, 163, 13), new Color(202, 138, 4),
            new Color(79, 70, 229), new Color(13, 148, 136)};
        return p[Math.floorMod(nome == null ? 0 : nome.trim().toUpperCase().hashCode(), p.length)];
    }

    static String iniciais(String nome) {
        String n = MesasWeb.primeiroEUltimo(nome == null ? "" : nome);
        StringBuilder sb = new StringBuilder();
        for (String s : n.trim().split("\\s+")) {
            if (!s.isEmpty() && Character.isLetterOrDigit(s.charAt(0))) {
                sb.append(Character.toUpperCase(s.charAt(0)));
            }
        }
        return sb.length() == 0 ? "?" : sb.toString();
    }

    // =====================================================================
    // COMPONENTES
    // =====================================================================
    /** Círculo com as iniciais do cliente. */
    private static final class Avatar extends JComponent {

        private final int d;
        private String nome = "";

        Avatar(int d) {
            this.d = d;
            setPreferredSize(new Dimension(d, d));
        }

        void setNome(String n) {
            nome = n == null ? "" : n;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = ComponentesWeb.suave(g);
            pintarAvatar(g2, 0, (getHeight() - d) / 2, d, nome);
            g2.dispose();
        }
    }

    static void pintarAvatar(Graphics2D g2, int x, int y, int d, String nome) {
        Color c = corDoNome(nome);
        g2.setPaint(new GradientPaint(x, y, ComponentesWeb.mistura(c, Color.WHITE, 0.25), x + d, y + d, c.darker()));
        g2.fillOval(x, y, d, d);
        g2.setColor(Color.WHITE);
        g2.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(d * 0.36f));
        FontMetrics fm = g2.getFontMetrics();
        String ini = iniciais(nome);
        g2.drawString(ini, x + (d - fm.stringWidth(ini)) / 2, y + (d + fm.getAscent() - fm.getDescent()) / 2);
    }

    /** Cartão de um cliente na faixa de cima. */
    private final class RendererCliente extends JComponent implements ListCellRenderer<ResumoCliente> {

        private ResumoCliente r;
        private boolean seleccionado;

        @Override
        public Component getListCellRendererComponent(JList<? extends ResumoCliente> list, ResumoCliente value,
                int index, boolean isSelected, boolean cellHasFocus) {
            r = value;
            seleccionado = isSelected;
            setToolTipText(value == null ? null : "<html><b>" + value.nome + "</b><br>"
                    + (value.linhas == 0 ? "Sem pedidos ainda" : value.linhas + " pedidos · " + moeda(value.total)) + "</html>");
            return this;
        }

        @Override
        protected void paintComponent(Graphics g) {
            if (r == null) {
                return;
            }
            Graphics2D g2 = ComponentesWeb.suave(g);
            Color vivo = Tema.amostra(Tema.actual())[1];
            int x = 4, y = 4, w = getWidth() - 10, h = getHeight() - 10;
            g2.setColor(new Color(15, 23, 42, seleccionado ? 34 : 16));
            g2.fillRoundRect(x, y + 2, w, h, 16, 16);
            g2.setColor(seleccionado ? ComponentesWeb.mistura(vivo, Color.WHITE, 0.90) : Color.WHITE);
            g2.fillRoundRect(x, y, w, h, 16, 16);
            g2.setStroke(new BasicStroke(seleccionado ? 2.2f : 1f));
            g2.setColor(seleccionado ? vivo : ComponentesWeb.LINHA);
            g2.drawRoundRect(x, y, w - 1, h - 1, 16, 16);

            int d = 40;
            pintarAvatar(g2, x + 10, y + (h - d) / 2, d, r.nome);
            int tx = x + 10 + d + 10, larg = x + w - tx - 10;
            g2.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(13f));
            g2.setColor(ComponentesWeb.TEXTO);
            g2.drawString(cortar(MesasWeb.primeiroEUltimo(r.nome), g2.getFontMetrics(), larg), tx, y + 24);
            g2.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(12.5f));
            g2.setColor(r.linhas == 0 ? ComponentesWeb.TEXTO_SUAVE : ComponentesWeb.AZUL_MEDIO);
            g2.drawString(r.linhas == 0 ? "novo pedido" : moeda(r.total), tx, y + 42);
            if (r.desde != null) {
                long min = minutosDesde(r.desde);
                String t = r.linhas + (r.linhas == 1 ? " pedido" : " pedidos") + " · há " + MesasWeb.tempo(min, r.desde);
                g2.setFont(EstiloUI.FONTE.deriveFont(11f));
                // pedidos de outro dia ficam a laranja: alguém saiu sem pagar
                g2.setColor(min >= 12 * 60 ? ComponentesWeb.LARANJA.darker() : ComponentesWeb.TEXTO_SUAVE);
                g2.drawString(cortar(t, g2.getFontMetrics(), larg), tx, y + 58);
            }
            g2.dispose();
        }
    }

    /** Cartão de uma categoria: cor própria, desenho a condizer com o nome e o número de produtos. */
    private static final class CartaoCategoria extends JComponent {

        private final Categoria c;
        private final Color cor;
        private final MenuWeb.Simbolo simbolo;
        private boolean sobre;

        CartaoCategoria(Categoria c, Runnable accao) {
            this.c = c;
            this.cor = MesasWeb.corCategoria(c.designacao);
            this.simbolo = simboloDaCategoria(c.designacao);
            setPreferredSize(new Dimension(150, 96));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setToolTipText("Ver os produtos de " + c.designacao);
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    sobre = true;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    sobre = false;
                    repaint();
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    if (contains(e.getPoint())) {
                        accao.run();
                    }
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = ComponentesWeb.suave(g);
            int sobe = sobre ? 2 : 0;
            int x = 4, y = 4 - sobe, w = getWidth() - 8, h = getHeight() - 10;
            g2.setColor(new Color(15, 23, 42, sobre ? 40 : 22));
            g2.fillRoundRect(x, y + 3 + sobe, w, h, 16, 16);
            Color base = sobre ? ComponentesWeb.mistura(cor, Color.WHITE, 0.1) : cor;
            g2.setPaint(new GradientPaint(x, y, ComponentesWeb.mistura(base, Color.WHITE, 0.14), x + w, y + h, base.darker()));
            g2.fillRoundRect(x, y, w, h, 16, 16);
            java.awt.Shape clip = g2.getClip();
            g2.clip(new java.awt.geom.RoundRectangle2D.Double(x, y, w, h, 16, 16));
            g2.setColor(new Color(255, 255, 255, 30));
            g2.fillOval(x + w - 52, y - 24, 80, 80);
            g2.setClip(clip);
            MenuWeb.desenharSimbolo(g2, simbolo, x + w - 40, y + 10, 28, new Color(255, 255, 255, 215));
            g2.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(13.5f));
            g2.setColor(Color.WHITE);
            g2.drawString(cortar(c.designacao.toUpperCase(), g2.getFontMetrics(), w - 24), x + 12, y + h - 30);
            g2.setFont(EstiloUI.FONTE.deriveFont(11.5f));
            g2.setColor(new Color(255, 255, 255, 210));
            g2.drawString(c.produtos + (c.produtos == 1 ? " produto" : " produtos"), x + 12, y + h - 12);
            g2.dispose();
        }
    }

    /** Desenho pela palavra-chave da categoria (bebidas, toalhas, suplementos, aulas...). */
    static MenuWeb.Simbolo simboloDaCategoria(String nome) {
        String n = java.text.Normalizer.normalize(nome == null ? "" : nome.toLowerCase(), java.text.Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
        if (n.matches(".*(agua|sumo|refri|bebida|energ|cerve|vinho|isoton|cafe|cha).*")) {
            return MenuWeb.Simbolo.CHAVENA;
        }
        if (n.matches(".*(toalha|roupa|vestu|camis|luva|acess).*")) {
            return MenuWeb.Simbolo.CAMISA;
        }
        if (n.matches(".*(suplem|protei|whey|vitamin|barra|nutri).*")) {
            return MenuWeb.Simbolo.PULSACAO;
        }
        if (n.matches(".*(aula|mensal|propina|plano|inscri|servic|treino|personal).*")) {
            return MenuWeb.Simbolo.CRONOMETRO;
        }
        if (n.matches(".*(iogurt|lanche|comida|snack|prato|sandes|fruta).*")) {
            return MenuWeb.Simbolo.TALHERES;
        }
        return MenuWeb.Simbolo.HALTERE;
    }

    /** "+" desenhado (botão Cliente). */
    private static final class IconeMais implements javax.swing.Icon {

        private final int t;
        private final Color cor;

        IconeMais(int t, Color cor) {
            this.t = t;
            this.cor = cor;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = ComponentesWeb.suave(g);
            g2.setColor(cor);
            g2.setStroke(new BasicStroke(2.4f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.drawLine(x + 2, y + t / 2, x + t - 2, y + t / 2);
            g2.drawLine(x + t / 2, y + 2, x + t / 2, y + t - 2);
            g2.dispose();
        }

        @Override
        public int getIconWidth() {
            return t;
        }

        @Override
        public int getIconHeight() {
            return t;
        }
    }

    /** Talão com seta (botão "Adicionar à facturação"). */
    private static final class IconeFactura implements javax.swing.Icon {

        private final int t;
        private final Color cor;

        IconeFactura(int t, Color cor) {
            this.t = t;
            this.cor = cor;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = ComponentesWeb.suave(g);
            MenuWeb.desenharSimbolo(g2, MenuWeb.Simbolo.RECIBO, x, y, t, cor);
            g2.dispose();
        }

        @Override
        public int getIconWidth() {
            return t;
        }

        @Override
        public int getIconHeight() {
            return t;
        }
    }

    // =====================================================================
    // ESCOLHA DO CLIENTE
    // =====================================================================
    /** Pesquisa rápida de clientes, com "Cadastrar novo cliente". */
    private final class EscolhaCliente extends JDialog {

        Cliente escolhido;
        private final DefaultListModel<Cliente> modelo = new DefaultListModel<>();
        private final JList<Cliente> lista = new JList<>(modelo);
        private final CampoPesquisa campo = new CampoPesquisa("Nome, telefone ou NIF do cliente…");
        private final Timer t;

        EscolhaCliente() {
            super(PedidosGinasioDialog.this, "Escolher cliente", ModalityType.APPLICATION_MODAL);
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            t = new Timer(220, e -> procurar());
            t.setRepeats(false);

            JPanel raiz = new JPanel(new BorderLayout(0, 10));
            raiz.setBackground(Color.WHITE);
            raiz.setBorder(new EmptyBorder(14, 14, 14, 14));
            campo.setPreferredSize(new Dimension(380, 40));
            campo.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    t.restart();
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    t.restart();
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    t.restart();
                }
            });
            campo.addActionListener(e -> {
                if (lista.getSelectedValue() == null && !modelo.isEmpty()) {
                    lista.setSelectedIndex(0);
                }
                confirmar();
            });
            campo.addKeyListener(new java.awt.event.KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    int i = lista.getSelectedIndex();
                    if (e.getKeyCode() == KeyEvent.VK_DOWN && i < modelo.size() - 1) {
                        lista.setSelectedIndex(i + 1);
                        lista.ensureIndexIsVisible(i + 1);
                    } else if (e.getKeyCode() == KeyEvent.VK_UP && i > 0) {
                        lista.setSelectedIndex(i - 1);
                        lista.ensureIndexIsVisible(i - 1);
                    }
                }
            });
            lista.setFixedCellHeight(46);
            lista.setCellRenderer(new ListCellRenderer<Cliente>() {
                private final JPanel p = new JPanel(new BorderLayout(10, 0));
                private final Avatar a = new Avatar(32);
                private final JLabel n = new JLabel();
                private final JLabel s = new JLabel();

                {
                    JPanel tx = ComponentesWeb.transparente(new GridLayout(2, 1));
                    n.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(13f));
                    s.setFont(EstiloUI.FONTE.deriveFont(11.5f));
                    s.setForeground(ComponentesWeb.TEXTO_SUAVE);
                    tx.add(n);
                    tx.add(s);
                    p.add(a, BorderLayout.WEST);
                    p.add(tx, BorderLayout.CENTER);
                    p.setBorder(new EmptyBorder(4, 8, 4, 8));
                }

                @Override
                public Component getListCellRendererComponent(JList<? extends Cliente> l, Cliente c, int i,
                        boolean sel, boolean foco) {
                    a.setNome(c.nome);
                    n.setText(c.nome);
                    String extra = (c.telefone == null || c.telefone.trim().isEmpty() ? "" : "Tel. " + c.telefone)
                            + (c.nif == null || c.nif.trim().isEmpty() ? "" : "   NIF " + c.nif);
                    s.setText(extra.isEmpty() ? "Cliente n.º " + c.codigo : extra);
                    p.setBackground(sel ? ComponentesWeb.mistura(Tema.amostra(Tema.actual())[1], Color.WHITE, 0.85)
                            : Color.WHITE);
                    n.setForeground(ComponentesWeb.TEXTO);
                    return p;
                }
            });
            lista.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2) {
                        confirmar();
                    }
                }
            });
            JScrollPane rol = new JScrollPane(lista);
            rol.setBorder(BorderFactory.createLineBorder(ComponentesWeb.LINHA));
            rol.setPreferredSize(new Dimension(380, 300));

            JButton novo = new JButton("Cadastrar novo cliente");
            ComponentesWeb.botao(novo, ComponentesWeb.Variante.SECUNDARIO, null, "Abre o cadastro de clientes");
            novo.setIcon(new IconeMais(12, ComponentesWeb.AZUL));
            novo.addActionListener(e -> cadastrar());
            JButton ok = new JButton("Escolher");
            ComponentesWeb.botao(ok, ComponentesWeb.Variante.PRIMARIO, null, "Escolher o cliente seleccionado (Enter)");
            ok.addActionListener(e -> confirmar());
            JPanel botoes = ComponentesWeb.transparente(new BorderLayout(8, 0));
            botoes.add(novo, BorderLayout.WEST);
            botoes.add(ok, BorderLayout.EAST);

            raiz.add(campo, BorderLayout.NORTH);
            raiz.add(rol, BorderLayout.CENTER);
            raiz.add(botoes, BorderLayout.SOUTH);
            setContentPane(raiz);
            teclas(getRootPane(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "fechar", this::dispose);
            pack();
            setLocationRelativeTo(btNovoCliente.isShowing() ? btNovoCliente : PedidosGinasioDialog.this);
            procurar();
        }

        private void procurar() {
            try {
                modelo.clear();
                for (Cliente c : PedidosGinasio.procurarClientes(campo.getText())) {
                    modelo.addElement(c);
                }
                if (!modelo.isEmpty()) {
                    lista.setSelectedIndex(0);
                }
            } catch (Exception e) {
                erro("Não foi possível procurar os clientes", e);
            }
        }

        private void confirmar() {
            escolhido = lista.getSelectedValue();
            if (escolhido != null) {
                dispose();
            }
        }

        /** Abre o cadastro; o cliente novo (o de código mais alto criado agora) fica escolhido. */
        private void cadastrar() {
            int antes = maiorCodigo();
            try {
                new visao.ClienteInternoVisao(donoFrame, true, util.BDConexao.getInstancia()).setVisible(true);
            } catch (Exception e) {
                erro("Não foi possível abrir o cadastro de clientes", e);
                return;
            }
            int depois = maiorCodigo();
            if (depois > antes) {
                try {
                    String nome = PedidosGinasio.nomeCliente(depois);
                    if (nome != null) {
                        escolhido = new Cliente(depois, nome, null, null);
                        dispose();
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            procurar();
        }

        private int maiorCodigo() {
            try (java.sql.Connection cn = util.BDConexao.novaLigacao();
                    java.sql.Statement st = cn.createStatement();
                    java.sql.ResultSet rs = st.executeQuery("SELECT COALESCE(MAX(codigo), 0) FROM tb_cliente")) {
                return rs.next() ? rs.getInt(1) : 0;
            } catch (Exception e) {
                return 0;
            }
        }
    }
}
