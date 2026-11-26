/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package visao;

//import comercial.ProdutoItemVisao;
import controller.TipoClienteController;
import comercial.controller.VendasController;
import dao.CaixaDao;
import dao.ItemPedidosDao;
import dao.MesasDao;
import dao.MoedaDao;
import dao.PedidoDao;
import dao.PrecoDao;
import dao.ProductoDao;
import dao.ProductosDao;
import dao.ProdutoDao;
import dao.StockDao;
import entity.*;
import comercial.controller.AnoEconomicoController;
import comercial.controller.ArmazensAccessoController;
import comercial.controller.ArmazensController;
import comercial.controller.CaixasController;
import comercial.controller.CambiosController;
import comercial.controller.ClientesController;
import comercial.controller.DadosInstituicaoController;
import comercial.controller.DescontosController;
import comercial.controller.DocumentosController;
import comercial.controller.FamiliasController;
import comercial.controller.FormaPagamentoController;
import comercial.controller.FormaPagamentoItemController;
import comercial.controller.GruposController;
import comercial.controller.ItemVendasController;
import comercial.controller.LocalController;
import comercial.controller.LugaresController;
import comercial.controller.MesasController;
import comercial.controller.MoedasController;
import comercial.controller.PrecosController;
import comercial.controller.ProdutosController;
import comercial.controller.ProdutosImpostoController;
import comercial.controller.ProdutosIsentoController;
import comercial.controller.StoksController;
import comercial.controller.TipoProdutosController;
import comercial.controller.UnidadesController;
import comercial.controller.UsuariosController;
import dao.DocumentoDao;
import dao.VendaDao;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import static kitanda.util.CfConstantes.YYYYMMDD_HHMMSS;
import kitanda.util.CfMethods;
import lista.ListaPedidos;
import lista.ListaVenda1;
import lista.ListaVendaRecolhas;
import lista.ListaVendaRecolhasReimpressao;
import lista.ListaVendasMesas;
import modelo.ProdutoModelo;
import org.netbeans.lib.awtextra.AbsoluteLayout;
import tesouraria.novo.controller.ContaController;
import tesouraria.novo.controller.ContaMovimentosController;
import tesouraria.novo.util.AnyReport;
import tesouraria.novo.util.MetodosUtilTS;
import util.BDConexao;
import util.DVML;
import util.JPAEntityMannagerFactoryUtil;
import util.MetodosUtil;
import static util.DVML.*;
import util.FinanceUtils;
import static util.MetodosUtil.rodarComandoWindows;
import static visao.PrincipalPedidosVisao.procedimento_mesas_livre;

/**
 *
 * @author Domingos Dala Vunge & Martinho Luís
 */
public class RecolhaPedidosVisao extends javax.swing.JFrame {

    private static EntityManagerFactory emf = JPAEntityMannagerFactoryUtil.em;
    private static ClientesController clientesController;
    private static FamiliasController familiaController;
    private static PrecosController precosController;
    private static LugaresController lugaresController;
    private static ProdutosController produtosController;
    private static DocumentosController documentosController;
    private static MoedasController moedasController;
    private static DescontosController descontosController;
    private static CambiosController cambiosController;
    private static UsuariosController usuariosController;
    private static DadosInstituicaoController dadosInstituicaoController;
    private static ProdutosImpostoController produtosImpostoController;
    private static ProdutosIsentoController produtosIsentoController;
    private static CaixasController caixasController;
    private static FormaPagamentoItemController formaPagamentoItemController;
    private static ArmazensAccessoController armazensAccessoController;
    private static TbDadosInstituicao dadosInstituicao;
    private static MesasController mesasController;
    private static ArmazensController armazensController;
    private static LocalController localController;
    private static UnidadesController unidadesController;
    private static AnoEconomicoController anoEconomicoController;
    private static GruposController gruposController;
    private static TipoClienteController tipoClienteController;
    private static VendasController vendasController;
    private static ItemVendasController itemVendasController;
    private static StoksController stocksController;
    private static TipoProdutosController tipoProdutosController;
    private static TbStock stock_local;

    private TbItemPedidos itemPedidos;
    private static MesasDao mesasDao = new MesasDao(emf);
    private static PrecoDao precoDao = new PrecoDao(emf);
    private static CaixaDao caixaDao;
    private static PedidoDao pedidoDao = new PedidoDao(emf);
    private static ProdutoDao produtoDao = new ProdutoDao(emf);
    private static ItemPedidosDao itemPedidosDao = new ItemPedidosDao(emf);
    private static StockDao stockDao = new StockDao(emf);
    private static ProductoDao productoDao;
    private static VendaDao vendaDao = new VendaDao(emf);
    private List<TbVenda> lista = null;
    private static String prox_doc;
    private static Documento documento;
    private DefaultListModel lista_lugares = new DefaultListModel();
    private static TbPedido pedido;
    private static TbProduto produtos;
    private static ProductosDao productosDao;
    private static int linha_acutal = 0;
    private static BDConexao conexao;
    private ProdutoModelo produtoModelo;
    private static String mesa = "";
    private static String lugar = "";
    private static int lugares = 0;
    private int pk_mesa = 0;
    private int idpedido = 0;
    private static int linha_actual = -1;
    private ArrayList<JButton> botoes_object = new ArrayList<>();
    private ArrayList<JButton> botoes_categorias = new ArrayList<>();
    private int TAMANHO_ESQUERDO = 10;
    private int TAMANHO_CATEGORIA = 100;
    private final int idCategoria = 0;
    // grelha adaptável: as colunas acompanham a largura da janela (ignora o GridLayout fixo que o código pede)
    private JPanel painel_central = new util.ui.GrelhaAdaptavel(150, 92, 8);
    private GridLayout gl;
    private ImageIcon imageIcon;
    private int codigo = 0;
    private static int idUser = 0;
    private static int doc_prox_cod;
    public static Abreviacao abreviacao;
    private boolean aviso_continuar_documento = false;
    private static int id_armzem;
    public static double gorjeta = 0;
    private static int GRUPO_AREA;
    private static FormaPagamentoController formaPagamentoController;
    private static ContaController contaController;
    private static ContaMovimentosController cmc;
    private static AnoEconomico anoEconomico;
    private static BDConexao conexaoTransaction;
    private static String categoria;
    private static int cod_categoria;

    /**
     * Creates new form GestaoItemPedidosVisao
     */
    public RecolhaPedidosVisao(String mesa, String lugar, int idUser, int id_armazem, BDConexao conexao) {
        initComponents();
        setLocationRelativeTo(null);
        cmbMoeda.setVisible(false);
        cmb_area_venda_restaurante.setVisible(false);
        lbQuantidadeExistente.setVisible(false);
        txtQuatidadeExistente.setVisible(false);
        cmbArmazem.setVisible(false);
        cmbAnoEconomico.setVisible(false);
        //this.setExtendedState(   this.getExtendedState()|GestaoPedidosVisao.MAXIMIZED_BOTH  );   
        this.conexao = conexao;
//        this.GRUPO_AREA = GRUPO_AREA;
        this.idUser = idUser;
        this.id_armzem = id_armazem;
        this.mesa = mesa.trim();
        this.lugar = lugar.trim();
        dcDataInicio.setDate(new Date());
        dcDataFim.setDate(new Date());
        txtIniciaisCliente.addKeyListener(new TratarEventoTeclado());
        vendasController = new VendasController(RecolhaPedidosVisao.conexao);
        itemVendasController = new ItemVendasController(RecolhaPedidosVisao.conexao);
        mesasController = new MesasController(RecolhaPedidosVisao.conexao);
        lugaresController = new LugaresController(RecolhaPedidosVisao.conexao);
        produtosController = new ProdutosController(RecolhaPedidosVisao.conexao);
        stocksController = new StoksController(RecolhaPedidosVisao.conexao);
        precosController = new PrecosController(RecolhaPedidosVisao.conexao);
        tipoProdutosController = new TipoProdutosController(RecolhaPedidosVisao.conexao);
        familiaController = new FamiliasController(RecolhaPedidosVisao.conexao);
        armazensController = new ArmazensController(RecolhaPedidosVisao.conexao);
        localController = new LocalController(RecolhaPedidosVisao.conexao);
        unidadesController = new UnidadesController(RecolhaPedidosVisao.conexao);
        anoEconomicoController = new AnoEconomicoController(RecolhaPedidosVisao.conexao);
        clientesController = new ClientesController(RecolhaPedidosVisao.conexao);
        documentosController = new DocumentosController(RecolhaPedidosVisao.conexao);
        produtosImpostoController = new ProdutosImpostoController(RecolhaPedidosVisao.conexao);
        cambiosController = new CambiosController(RecolhaPedidosVisao.conexao);
        dadosInstituicaoController = new DadosInstituicaoController(RecolhaPedidosVisao.conexao);
        formaPagamentoItemController = new FormaPagamentoItemController(RecolhaPedidosVisao.conexao);
        gruposController = new GruposController(RecolhaPedidosVisao.conexao);
        armazensAccessoController = new ArmazensAccessoController(RecolhaPedidosVisao.conexao);
        usuariosController = new UsuariosController(RecolhaPedidosVisao.conexao);
        moedasController = new MoedasController(RecolhaPedidosVisao.conexao);
        formaPagamentoController = new FormaPagamentoController(RecolhaPedidosVisao.conexao);
        contaController = new ContaController(RecolhaPedidosVisao.conexao);
        cmc = new ContaMovimentosController(conexao);
        dadosInstituicao = (TbDadosInstituicao) dadosInstituicaoController.findById(1);
        caixasController = new CaixasController(conexao);

        rbArmazem1.setVisible(false);
        rbArmazem.setVisible(false);
        caixaDao = new CaixaDao(emf);
//        txtMesa.setText( this.mesa );
        lbValorPorExtenco.setText("");

//        dc_data_entrega.setDate( new Date() );
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.DATE, +2);
//        dc_data_entrega.setMinSelectableDate( instance.getTime() );
        dc_data_entrega.setDate(instance.getTime());

        cmbTipoDocumento.setModel(new DefaultComboBoxModel(documentosController.getVectorRecolha()));

        cmbAnoEconomico.setModel(new DefaultComboBoxModel(anoEconomicoController.getVector()));
        cmbMoeda.setModel(new DefaultComboBoxModel(moedasController.getVector()));
        cmbCliente.setModel(new DefaultComboBoxModel(clientesController.getVectorExcetoConsumidorFinal()));
//        cmbCliente.setSelectedItem( DVML._CLIENTE_CONSUMIDOR_FINAL );

        try {
            setDocpadrao(dadosInstituicao.getDocpadrao());
            setFolhaImpressora(dadosInstituicao.getImpressora());
            setDesactivarvias(dadosInstituicao.getDesactivarvias());
            int numero_copia = dadosInstituicao.getNumeroVias();
        } catch (Exception e) {
            e.printStackTrace();
        }

        jPanel1.setLayout(new AbsoluteLayout());
        adicionar_catgorias();
        setSalvarPedidos();
        actualizar();
        adicionar_lugares();
        dc_data_documento.setDate(new Date());
//        jPanel6.setVisible( true );
        jPanel4.setVisible(true);
//        mostrar_proximo_codigo_documento();

        setWindowsListener();

        try {
            configurar_armazens();
        } catch (Exception e) {
            e.printStackTrace();
        }
        setArmazem(dadosInstituicao.getConfigArmazens());

        txtObs.addKeyListener(new TratarEvento());

        txtLargura.setEnabled(true);
        txtAltura.setEnabled(false);
        txtQtdItens.setEnabled(false);

        try {

            empresa();
        } catch (Exception e) {
        }
        try {

            boas_vinda();
        } catch (Exception e) {
        }
        // aspecto Kitanda e responsivo; se falhar, o ecrã abre com o desenho antigo
        util.ui.LayoutSeguro.aplicar(this, "Recolha de Roupa", this::montarLayoutWeb);
    }

    // =====================================================================
    // LAYOUT WEB (Kitanda): os mesmos componentes e eventos do initComponents,
    // arrumados como o pedido das mesas (NovaGestaoPedidosVisao): catálogo à
    // esquerda, peças da recolha (tabela, visor do total e cobrança) ao centro,
    // cliente e peça, compactos, à direita (em janelas estreitas passam para baixo).
    // =====================================================================
    private void montarLayoutWeb() {
        final util.ui.ComponentesWeb.Variante SEC = util.ui.ComponentesWeb.Variante.SECUNDARIO;
        final java.awt.Color claroCabecalho = util.ui.Tema.claro();

        // ---- campos
        util.ui.FormularioWeb.campoTexto(txtIniciaisCliente, txtTelefoneCliente, txtLargura, txtAltura, txtQtdItens,
                txtTotalQTD, txtTotalGeral, txtDesconto);
        util.ui.FormularioWeb.combo(cmbCliente, cmbTipoDocumento);
        // o .form pinta o texto destes combos de branco (desenho antigo, fundo escuro): no fundo branco não se via
        cmbCliente.setForeground(util.ui.ComponentesWeb.TEXTO);
        cmbTipoDocumento.setForeground(util.ui.ComponentesWeb.TEXTO);
        util.ui.FormularioWeb.data(dc_data_entrega);
        util.ui.FormularioWeb.data(dc_data_documento);
        txtIniciaisCliente.setToolTipText("Escreva parte do nome e carregue Enter");
        txtTelefoneCliente.setToolTipText("Telefone do cliente");
        for (javax.swing.JTextField t : new javax.swing.JTextField[]{txtTotalQTD, txtTotalGeral, txtDesconto}) {
            t.setEditable(false);
            t.setFocusable(false);
            t.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
            t.setFont(util.ui.EstiloUI.FONTE_NEGRITO.deriveFont(13.5f));
        }
        txtObs.setLineWrap(true);
        txtObs.setWrapStyleWord(true);
        txtObs.setRows(2);
        jScrollPane1.setBorder(javax.swing.BorderFactory.createLineBorder(util.ui.ComponentesWeb.LINHA));
        jScrollPane1.setPreferredSize(new java.awt.Dimension(200, 34));
        for (javax.swing.JLabel l : new javax.swing.JLabel[]{lbTelefoneCliente, lbEmailCliente, lbDesconto}) {
            l.setFont(util.ui.EstiloUI.FONTE.deriveFont(12f));
            l.setForeground(util.ui.ComponentesWeb.TEXTO_SUAVE);
        }
        lbServico.setFont(util.ui.EstiloUI.FONTE_NEGRITO.deriveFont(14f));
        lbServico.setForeground(util.ui.ComponentesWeb.AZUL);
        lbServico.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lbSomaLarguraAltura.setFont(util.ui.EstiloUI.FONTE_NEGRITO.deriveFont(13f));
        lbTotalQtd.setFont(util.ui.EstiloUI.FONTE_NEGRITO.deriveFont(13f));
        lbSomaLarguraAltura.setForeground(util.ui.ComponentesWeb.TEXTO);
        lbTotalQtd.setForeground(util.ui.ComponentesWeb.TEXTO);
        // textos de exemplo do desenho antigo: o código preenche-os quando há dados
        if ("serviço".equals(lbServico.getText())) {
            lbServico.setText("Escolha a peça no catálogo");
        }
        if (lbSomaLarguraAltura.getText().trim().equals("X")) {
            lbSomaLarguraAltura.setText("—");
        }
        if (lbTotalQtd.getText().contains("XXX")) {
            lbTotalQtd.setText("—");
        }
        if (lbTelefoneCliente.getText().equals("Telefone")) {
            lbTelefoneCliente.setText("");
            lbEmailCliente.setText("");
        }
        if (status_mensagem_primaria.getText().equals("sahdbvhsdva")) {
            status_mensagem_primaria.setText(" ");
        }

        // ================================================ cabeçalho
        javax.swing.JComponent emblema = new javax.swing.JComponent() {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                java.awt.Graphics2D g2 = util.ui.ComponentesWeb.suave(g);
                int w = getWidth(), h = getHeight();
                g2.setColor(new java.awt.Color(255, 255, 255, 40));
                g2.fillRoundRect(0, 2, w, h - 2, 16, 16);
                g2.setColor(java.awt.Color.WHITE);
                g2.fillRoundRect(0, 0, w - 1, h - 3, 16, 16);
                util.ui.MenuWeb.desenharSimbolo(g2, util.ui.MenuWeb.Simbolo.CABIDE, (w - 34) / 2.0, (h - 37) / 2.0, 34,
                        util.ui.ComponentesWeb.AZUL);
                g2.dispose();
            }
        };
        emblema.setPreferredSize(new java.awt.Dimension(58, 56));
        javax.swing.JLabel titulo = new javax.swing.JLabel("Recolha de Roupa");
        titulo.setFont(util.ui.EstiloUI.FONTE_NEGRITO.deriveFont(23f));
        titulo.setForeground(java.awt.Color.WHITE);
        jlUsuario.setForeground(claroCabecalho);
        jlUsuario.setFont(util.ui.EstiloUI.FONTE.deriveFont(13f));
        jlUsuario.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        final javax.swing.JLabel subtitulo = new javax.swing.JLabel();
        subtitulo.setFont(util.ui.EstiloUI.FONTE.deriveFont(13f));
        subtitulo.setForeground(claroCabecalho);
        final Runnable textoSubtitulo = () -> subtitulo.setText(
                new java.text.SimpleDateFormat("dd/MM/yyyy").format(new Date())
                + (jlUsuario.getText() == null || jlUsuario.getText().trim().isEmpty() ? "" : "   ·   " + jlUsuario.getText().trim()));
        jlUsuario.addPropertyChangeListener("text", e -> textoSubtitulo.run());
        textoSubtitulo.run();
        javax.swing.JPanel titulos = util.ui.ComponentesWeb.transparente(new java.awt.GridLayout(2, 1));
        titulos.add(titulo);
        titulos.add(subtitulo);
        javax.swing.JPanel marca = util.ui.ComponentesWeb.transparente(new java.awt.BorderLayout(12, 0));
        marca.add(emblema, java.awt.BorderLayout.WEST);
        marca.add(titulos, java.awt.BorderLayout.CENTER);
        // fotografias de roupa à direita do título (as peças do catálogo; sem fotos, as imagens da lavandaria)
        FaixaRoupa faixa = new FaixaRoupa(fotosDeRoupa(6));
        javax.swing.JPanel esquerdaCab = util.ui.ComponentesWeb.transparente(new java.awt.BorderLayout(26, 0));
        esquerdaCab.add(marca, java.awt.BorderLayout.WEST);
        esquerdaCab.add(faixa, java.awt.BorderLayout.CENTER);

        lb_proximo_documento.setForeground(java.awt.Color.WHITE);
        lb_proximo_documento.setFont(util.ui.EstiloUI.FONTE_NEGRITO.deriveFont(13f));
        cmbTipoDocumento.setPreferredSize(new java.awt.Dimension(170, 30));
        dc_data_documento.setPreferredSize(new java.awt.Dimension(130, 30));
        dc_data_documento.setToolTipText("Data do documento");
        jButton3.setText("Reimprimir");
        pilulaCabecalho(jButton3, SEC, "Reimprimir um talão de recolha");
        jButton2.setText("Sair");
        util.ui.FormularioWeb.botaoSair(jButton2, "Fechar a recolha");
        pilulaCabecalho(jButton2, null, null);
        javax.swing.JPanel botoesCab = util.ui.ComponentesWeb.transparente(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 8, 0));
        botoesCab.add(jButton3);
        javax.swing.JPanel documento = util.ui.ComponentesWeb.transparente(new util.ui.EstiloUI.WrapLayout(java.awt.FlowLayout.RIGHT, 10, 0));
        documento.add(lb_proximo_documento);
        documento.add(util.ui.FormularioWeb.campoCabecalho(new javax.swing.JLabel("Documento"), cmbTipoDocumento));
        documento.add(util.ui.FormularioWeb.campoCabecalho(new javax.swing.JLabel("Data"), dc_data_documento));
        javax.swing.JPanel botoesBaixo = util.ui.ComponentesWeb.transparente(new java.awt.BorderLayout());
        botoesBaixo.add(botoesCab, java.awt.BorderLayout.SOUTH);
        documento.add(botoesBaixo);

        javax.swing.JPanel cabecalho = new util.ui.ComponentesWeb.PainelGradiente(new java.awt.BorderLayout(16, 10),
                util.ui.EstiloUI.AZUL_ESCURO, util.ui.Tema.gradienteFim());
        cabecalho.setBorder(javax.swing.BorderFactory.createEmptyBorder(12, 20, 12, 20));
        cabecalho.add(esquerdaCab, java.awt.BorderLayout.CENTER);
        cabecalho.add(documento, java.awt.BorderLayout.EAST);
        // janela estreita: o documento, a data e os botões descem para uma 2.ª linha (o título não é cortado)
        final javax.swing.JPanel cab = cabecalho;
        cabecalho.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                java.awt.Insets in = cab.getInsets();
                boolean estreito = cab.getWidth() - in.left - in.right < marca.getPreferredSize().width + 16
                        + documento.getPreferredSize().width + 12;
                Object onde = ((java.awt.BorderLayout) cab.getLayout()).getConstraints(documento);
                Object quer = estreito ? java.awt.BorderLayout.SOUTH : java.awt.BorderLayout.EAST;
                if (!quer.equals(onde)) {
                    cab.remove(documento);
                    cab.add(documento, quer);
                    ((java.awt.FlowLayout) documento.getLayout()).setAlignment(estreito ? java.awt.FlowLayout.LEFT : java.awt.FlowLayout.RIGHT);
                    cab.revalidate();
                    cab.repaint();
                }
            }
        });

        // ================================================ catálogo (categorias e peças)
        // Lavagem / Engomagem: a lógica desactiva o botão do serviço escolhido; aqui esse fica "seleccionado"
        ServicoUI.aplicar(btnLavar, "Lavagem", util.ui.MenuWeb.Simbolo.LAVANDARIA, "Serviços de lavagem");
        ServicoUI.aplicar(btnEngomar, "Engomagem", util.ui.MenuWeb.Simbolo.FERRO, "Serviços de engomagem");
        jButton4.setText("Pesquisar");
        util.ui.ComponentesWeb.botao(jButton4, SEC, "/imagens/proucura.png", "Pesquisar um serviço / artigo");
        jButton4.putClientProperty("web.pilula", Boolean.TRUE);
        jButton4.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 12, 5, 14));
        btn_voltar.setText("<<");
        util.ui.ComponentesWeb.botao(btn_voltar, SEC, null, "Voltar às categorias");
        btn_voltar.putClientProperty("web.pilula", Boolean.TRUE);
        btn_voltar.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 12, 5, 14));
        designacao_categoria.setFont(util.ui.EstiloUI.FONTE_NEGRITO.deriveFont(14f));
        designacao_categoria.setForeground(util.ui.ComponentesWeb.TEXTO);
        designacao_categoria.setBorder(null); // o .form tinha uma moldura (TitledBorder vazio)
        final javax.swing.JLabel lbCategorias = new javax.swing.JLabel("Categorias");
        lbCategorias.setFont(util.ui.EstiloUI.FONTE_NEGRITO.deriveFont(14f));
        lbCategorias.setForeground(util.ui.ComponentesWeb.TEXTO);
        lbCategorias.setVisible(!designacao_categoria.isVisible());
        designacao_categoria.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentShown(java.awt.event.ComponentEvent e) {
                lbCategorias.setVisible(false);
            }

            @Override
            public void componentHidden(java.awt.event.ComponentEvent e) {
                lbCategorias.setVisible(true);
            }
        });
        javax.swing.JPanel caminho = util.ui.ComponentesWeb.transparente(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 8, 0));
        caminho.add(btn_voltar);
        caminho.add(lbCategorias);
        caminho.add(designacao_categoria);
        javax.swing.JPanel servicos = util.ui.ComponentesWeb.transparente(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 6, 0));
        servicos.add(btnLavar);
        servicos.add(btnEngomar);
        javax.swing.JPanel linhaServicos = util.ui.ComponentesWeb.transparente(new java.awt.BorderLayout(8, 0));
        linhaServicos.add(servicos, java.awt.BorderLayout.CENTER);
        linhaServicos.add(jButton4, java.awt.BorderLayout.EAST);
        javax.swing.JPanel barraCatalogo = util.ui.ComponentesWeb.transparente(new java.awt.BorderLayout(0, 8));
        barraCatalogo.add(linhaServicos, java.awt.BorderLayout.NORTH);
        barraCatalogo.add(caminho, java.awt.BorderLayout.CENTER);

        // categorias coloridas, como no pedido das mesas (a lógica continua a pôr JButton simples)
        painel_central.setOpaque(false);
        painel_central.addContainerListener(new java.awt.event.ContainerAdapter() {
            @Override
            public void componentAdded(java.awt.event.ContainerEvent e) {
                if (e.getChild() instanceof javax.swing.JButton) {
                    botaoCategoriaRoupa((javax.swing.JButton) e.getChild());
                }
            }
        });
        for (java.awt.Component c : painel_central.getComponents()) {
            if (c instanceof javax.swing.JButton) {
                botaoCategoriaRoupa((javax.swing.JButton) c);
            }
        }
        jScrollPane3.setBorder(null);
        jScrollPane3.setOpaque(false);
        jScrollPane3.getViewport().setOpaque(false);
        jScrollPane3.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane3.getVerticalScrollBar().setUnitIncrement(24);
        jScrollPane3.setPreferredSize(new java.awt.Dimension(380, 280));
        jScrollPane3.setMinimumSize(new java.awt.Dimension(200, 160));
        util.ui.ComponentesWeb.Cartao cartaoCatalogo = new util.ui.ComponentesWeb.Cartao("Catálogo",
                util.ui.ComponentesWeb.CIANO, new java.awt.BorderLayout(0, 10));
        cartaoCatalogo.getCorpo().add(barraCatalogo, java.awt.BorderLayout.NORTH);
        cartaoCatalogo.getCorpo().add(jScrollPane3, java.awt.BorderLayout.CENTER);

        // ================================================ peças da recolha (tabela grande, visor e cobrança)
        comercial.caixa.UiCaixa.tabela(table, new int[0], util.ui.ComponentesWeb.AZUL);
        // o .form limita as colunas (ID a 10 px, data e total a 100): cortava os valores
        int[][] colunasTabela = {{36, 48}, {104, 120}, {220, Integer.MAX_VALUE}, {48, 60}, {112, 140}};
        for (int i = 0; i < colunasTabela.length && i < table.getColumnCount(); i++) {
            javax.swing.table.TableColumn col = table.getColumnModel().getColumn(i);
            col.setMinWidth(i == 2 ? 120 : colunasTabela[i][0] - 10);
            col.setMaxWidth(colunasTabela[i][1]);
            col.setPreferredWidth(colunasTabela[i][0]);
        }
        jScrollPane2.setBorder(new javax.swing.border.LineBorder(util.ui.ComponentesWeb.LINHA, 1, true));
        jScrollPane2.getViewport().setBackground(java.awt.Color.WHITE);
        jScrollPane2.setPreferredSize(new java.awt.Dimension(400, 200));
        jScrollPane2.setMinimumSize(new java.awt.Dimension(260, 160));

        javax.swing.JPanel totais = util.ui.FormularioWeb.grelha(90, 3,
                util.ui.ComponentesWeb.campo("Peças", txtTotalQTD),
                util.ui.ComponentesWeb.campo("Total geral", txtTotalGeral),
                util.ui.ComponentesWeb.campo("Desconto", txtDesconto));

        // visor do total (escuro, números grandes, como na conta da mesa)
        javax.swing.JLabel lbVisor = new javax.swing.JLabel("TOTAL A PAGAR");
        lbVisor.setFont(util.ui.EstiloUI.FONTE_NEGRITO.deriveFont(11.5f));
        lbVisor.setForeground(new java.awt.Color(71, 85, 105));
        txtTotalApagar.setEditable(false);
        txtTotalApagar.setFont(util.ui.MesasWeb.fonteVisor(32));
        txtTotalApagar.setForeground(new java.awt.Color(21, 128, 61)); // verde escuro: bem visível no visor claro
        txtTotalApagar.setOpaque(false);
        txtTotalApagar.setBorder(null);
        txtTotalApagar.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        txtTotalApagar.setFocusable(false);
        final javax.swing.JLabel lbPecasVisor = new javax.swing.JLabel(" ");
        lbPecasVisor.setFont(util.ui.EstiloUI.FONTE_NEGRITO.deriveFont(11.5f));
        lbPecasVisor.setForeground(new java.awt.Color(71, 85, 105));
        final util.ui.ComponentesWeb.TextoQuebrado extenso = new util.ui.ComponentesWeb.TextoQuebrado();
        extenso.setFont(util.ui.EstiloUI.FONTE.deriveFont(java.awt.Font.ITALIC, 11.5f));
        extenso.setForeground(new java.awt.Color(220, 38, 38)); // vermelho: bem visível
        extenso.setFont(util.ui.EstiloUI.FONTE_NEGRITO.deriveFont(java.awt.Font.BOLD | java.awt.Font.ITALIC, 13f));
        extenso.setText(lbValorPorExtenco.getText());
        lbValorPorExtenco.addPropertyChangeListener("text", e -> extenso.setText(lbValorPorExtenco.getText()));
        txtTotalApagar.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            private void ver() {
                javax.swing.SwingUtilities.invokeLater(() -> {
                    double v = 0;
                    try {
                        v = CfMethods.parseMoedaFormatada(txtTotalApagar.getText());
                    } catch (Exception ex) {
                        // vazio
                    }
                    if (v <= 0.004) {
                        extenso.setText("");
                    } else {
                        extenso.setText(MetodosUtil.valorPorExtenso(v, "Kwanza"));
                    }
                });
            }

            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                ver();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                ver();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
            }
        });
        javax.swing.JPanel topoVisor = util.ui.ComponentesWeb.transparente(new java.awt.BorderLayout());
        topoVisor.add(lbVisor, java.awt.BorderLayout.WEST);
        topoVisor.add(lbPecasVisor, java.awt.BorderLayout.EAST);
        util.ui.ComponentesWeb.CaixaSuave visor = new util.ui.ComponentesWeb.CaixaSuave(new java.awt.BorderLayout(0, 2),
                new java.awt.Color(240, 253, 244), new java.awt.Color(134, 239, 172)); // visor claro
        visor.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 14, 10, 14));
        visor.add(topoVisor, java.awt.BorderLayout.NORTH);
        visor.add(txtTotalApagar, java.awt.BorderLayout.CENTER);
        visor.add(extenso, java.awt.BorderLayout.SOUTH);

        final util.ui.ComponentesWeb.Etiqueta etiquetaPecas = new util.ui.ComponentesWeb.Etiqueta("0 peças", util.ui.ComponentesWeb.VERDE);
        final Runnable contarPecas = () -> {
            int n = table.getRowCount();
            String t = n + (n == 1 ? " peça" : " peças");
            etiquetaPecas.setText(t);
            lbPecasVisor.setText(t.toUpperCase());
        };
        final javax.swing.event.TableModelListener ouvintePecas = e -> contarPecas.run();
        table.getModel().addTableModelListener(ouvintePecas);
        table.addPropertyChangeListener("model", e -> {
            if (e.getOldValue() instanceof javax.swing.table.TableModel) {
                ((javax.swing.table.TableModel) e.getOldValue()).removeTableModelListener(ouvintePecas);
            }
            if (e.getNewValue() instanceof javax.swing.table.TableModel) {
                ((javax.swing.table.TableModel) e.getNewValue()).addTableModelListener(ouvintePecas);
            }
            contarPecas.run();
        });
        contarPecas.run();

        // um botão de cada vez, conforme o documento (FR = Cobrar, FT = a crédito, PP = Proforma)
        BT_Conversao.setText("Cobrar");
        util.ui.ComponentesWeb.botao(BT_Conversao, util.ui.ComponentesWeb.Variante.SUCESSO, null, "Emitir a factura-recibo e receber agora");
        btFT.setText("Processar (a crédito)");
        util.ui.ComponentesWeb.botao(btFT, util.ui.ComponentesWeb.Variante.PRIMARIO, null, "Emitir factura para pagar na entrega");
        BtnProforma.setText("Emitir Proforma");
        util.ui.ComponentesWeb.botao(BtnProforma, util.ui.ComponentesWeb.Variante.PRIMARIO, null, "Factura proforma (orçamento)");
        javax.swing.JPanel cobrar = util.ui.ComponentesWeb.transparente(new util.ui.ComponentesWeb.GrelhaResponsiva(160, 6, 3));
        for (javax.swing.JButton b : new javax.swing.JButton[]{BtnProforma, btFT, BT_Conversao}) {
            b.setIcon(null);
            b.setFont(util.ui.EstiloUI.FONTE_NEGRITO.deriveFont(16f));
            b.setBorder(javax.swing.BorderFactory.createEmptyBorder(12, 12, 12, 12));
            b.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            cobrar.add(b);
        }
        // avisos (ex.: "Abra o caixa para poder cobrar") por cima dos botões de cobrança
        status_mensagem_primaria.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        javax.swing.JPanel rodapePecas = util.ui.FormularioWeb.pilha(8, totais, visor, status_mensagem_primaria, cobrar);

        util.ui.ComponentesWeb.Cartao cartaoPecas = new util.ui.ComponentesWeb.Cartao("Peças da recolha",
                util.ui.ComponentesWeb.VERDE, new java.awt.BorderLayout(0, 10));
        cartaoPecas.adicionarAccao(etiquetaPecas);
        cartaoPecas.getCorpo().add(jScrollPane2, java.awt.BorderLayout.CENTER);
        cartaoPecas.getCorpo().add(rodapePecas, java.awt.BorderLayout.SOUTH);

        // ================================================ cliente (compacto)
        btCliente.setText("");
        util.ui.ComponentesWeb.botao(btCliente, SEC, null, "Cadastrar um cliente novo");
        // telefone e desconto do cliente na linha do título do cartão (não gastam altura:
        // a 1366 x 768 a linha a mais empurrava o "Fecho Caixa" para fora); o e-mail fica na dica
        javax.swing.JPanel infoCliente = util.ui.ComponentesWeb.transparente(
                new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 10, 0));
        infoCliente.add(lbTelefoneCliente);
        infoCliente.add(lbDesconto);
        lbEmailCliente.addPropertyChangeListener("text", e -> lbTelefoneCliente.setToolTipText(lbEmailCliente.getText()));
        // o desconto da ficha do cliente vê-se (a laranja) e o campo "Desconto" diz de onde vem
        final Runnable destacarDesconto = () -> {
            double pct = 0;
            try {
                pct = getDescontoClientePercentagem();
            } catch (Exception e) {
                // sem cliente
            }
            boolean tem = pct > 0;
            lbDesconto.setForeground(tem ? new java.awt.Color(194, 65, 12) : util.ui.ComponentesWeb.TEXTO_SUAVE);
            lbDesconto.setFont(tem ? util.ui.EstiloUI.FONTE_NEGRITO.deriveFont(12.5f) : util.ui.EstiloUI.FONTE.deriveFont(12f));
            txtDesconto.setForeground(tem ? new java.awt.Color(194, 65, 12) : util.ui.ComponentesWeb.TEXTO);
            txtDesconto.setToolTipText(tem ? "Desconto de " + new java.text.DecimalFormat("#,##0.##").format(pct)
                    + "% da ficha do cliente" : "Sem desconto");
            if (pct >= 100) {
                status_mensagem_primaria.setText("Atenção: cliente com 100% de desconto na ficha");
            } else if (status_mensagem_primaria.getText().startsWith("Atenção: cliente")) {
                status_mensagem_primaria.setText(" ");
            }
        };
        lbDesconto.addPropertyChangeListener("text", e -> destacarDesconto.run());
        javax.swing.JPanel cliente = util.ui.FormularioWeb.pilha(6,
                util.ui.FormularioWeb.grelha(110, 2,
                        util.ui.ComponentesWeb.campo("Pesquisar (nome)", txtIniciaisCliente),
                        util.ui.ComponentesWeb.campo("Telefone", txtTelefoneCliente)),
                util.ui.ComponentesWeb.campo("Cliente", util.ui.FormularioWeb.comBotao(cmbCliente, btCliente, "Cliente novo")));
        util.ui.ComponentesWeb.Cartao cartaoCliente = new util.ui.ComponentesWeb.Cartao("Cliente", util.ui.ComponentesWeb.AZUL_VIVO,
                new java.awt.BorderLayout());
        cartaoCliente.adicionarAccao(infoCliente);
        cartaoCliente.getCorpo().add(cliente, java.awt.BorderLayout.CENTER);

        // ================================================ peça (compacto)
        util.ui.ComponentesWeb.botao(jButton1, SEC, null, "Aplicar esta data de entrega às peças da lista");
        for (javax.swing.JRadioButton rb : new javax.swing.JRadioButton[]{jRadioButton1, rbTaxaUrgente50, rbTaxaExpresso100}) {
            util.ui.ComponentesWeb.pilulaCompacta(rb);
        }
        jRadioButton1.setText("Normal");
        rbTaxaUrgente50.setText("Urgente +50%");
        rbTaxaExpresso100.setText("Expresso +100%");
        javax.swing.JPanel resumoMedidas = util.ui.ComponentesWeb.transparente(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 6, 0));
        javax.swing.JLabel lbArea = new javax.swing.JLabel("Área");
        javax.swing.JLabel lbTotalMedidas = new javax.swing.JLabel("   Total");
        for (javax.swing.JLabel l : new javax.swing.JLabel[]{lbArea, lbTotalMedidas}) {
            l.setFont(util.ui.EstiloUI.FONTE.deriveFont(12f));
            l.setForeground(util.ui.ComponentesWeb.TEXTO_SUAVE);
        }
        resumoMedidas.add(lbArea);
        resumoMedidas.add(lbSomaLarguraAltura);
        resumoMedidas.add(lbTotalMedidas);
        resumoMedidas.add(lbTotalQtd);
        javax.swing.JPanel taxas = util.ui.ComponentesWeb.transparente(new util.ui.EstiloUI.WrapLayout(java.awt.FlowLayout.LEFT, 4, 2));
        taxas.add(jRadioButton1);
        taxas.add(rbTaxaUrgente50);
        taxas.add(rbTaxaExpresso100);
        javax.swing.JPanel linhaServico = util.ui.ComponentesWeb.transparente(new java.awt.BorderLayout(8, 0));
        linhaServico.add(lbServico, java.awt.BorderLayout.CENTER);
        linhaServico.add(resumoMedidas, java.awt.BorderLayout.EAST);
        javax.swing.JPanel peca = util.ui.FormularioWeb.pilha(5,
                linhaServico,
                util.ui.FormularioWeb.grelha(70, 3,
                        util.ui.ComponentesWeb.campo("Largura", txtLargura),
                        util.ui.ComponentesWeb.campo("Altura", txtAltura),
                        util.ui.ComponentesWeb.campo("Qtd.", txtQtdItens)),
                util.ui.ComponentesWeb.campo("Observação (cor, nódoas, defeitos...)", jScrollPane1),
                util.ui.ComponentesWeb.campo("Data de entrega", util.ui.FormularioWeb.comBotao(dc_data_entrega, jButton1, null)),
                util.ui.ComponentesWeb.campo("Taxa", taxas));
        javax.swing.JPanel cartaoPeca = util.ui.FormularioWeb.cartao("Peça", util.ui.ComponentesWeb.ROXO, peca);

        status_mensagem_primaria.setFont(util.ui.EstiloUI.FONTE_NEGRITO.deriveFont(12.5f));
        status_mensagem_primaria.setForeground(util.ui.ComponentesWeb.VERMELHO);
        // ================================================ balcão (canto inferior direito): painel do dia, entregas e caixa
        String nomeUtilizador = "";
        try {
            nomeUtilizador = usuariosController.getUsuarioByCodigo(idUser).getNome();
        } catch (Exception e) {
            // sem nome: o balcão mostra só o Sair
        }
        final comercial.lavandaria.BalcaoLavandaria balcao = new comercial.lavandaria.BalcaoLavandaria(this, idUser, nomeUtilizador,
                () -> new EntregaLavandariaVisao(idUser, BDConexao.getInstancia()).setVisible(true),
                jButton2::doClick, // o mesmo Sair de sempre (fecha a recolha)
                aberto -> {
                    // como na venda: sem caixa aberto não se cobra (a FT a crédito e a proforma continuam)
                    BT_Conversao.setEnabled(aberto);
                    String aviso = "Abra o caixa para poder cobrar";
                    if (!aberto) {
                        status_mensagem_primaria.setText(aviso);
                    } else if (aviso.equals(status_mensagem_primaria.getText())) {
                        status_mensagem_primaria.setText(" ");
                    }
                });
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowActivated(java.awt.event.WindowEvent e) {
                balcao.verificarCaixa();
                balcao.actualizar(false);
            }
        });
        // depois de cobrar / processar, os números do dia acompanham
        for (javax.swing.JButton b : new javax.swing.JButton[]{BT_Conversao, btFT}) {
            b.addActionListener(e -> javax.swing.SwingUtilities.invokeLater(() -> balcao.actualizar(true)));
        }
        balcao.verificarCaixa();
        balcao.actualizar(true);

        // cliente e peça em cima, o balcão encostado ao fundo; se a janela for baixa, a coluna desliza
        javax.swing.JPanel clientePeca = util.ui.FormularioWeb.pilha(10, cartaoCliente, cartaoPeca);
        ColunaLateral lateralConteudo = new ColunaLateral(clientePeca, balcao);
        javax.swing.JScrollPane lateral = new javax.swing.JScrollPane(lateralConteudo,
                javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        lateral.setBorder(null);
        lateral.setOpaque(false);
        lateral.getViewport().setOpaque(false);
        lateral.getVerticalScrollBar().setUnitIncrement(24);
        lateral.setMinimumSize(new java.awt.Dimension(260, 200));

        // ================================================ disposição responsiva
        // catálogo | peças da recolha | cliente e peça (em janelas estreitas a 3.ª coluna passa para baixo)
        javax.swing.JPanel colunas = util.ui.ComponentesWeb.transparente(
                new util.ui.ComponentesWeb.ColunasResponsivas(new double[]{0.35, 0.38, 0.27}, new int[]{330, 370, 320}, 10));
        colunas.add(cartaoCatalogo);
        colunas.add(cartaoPecas);
        colunas.add(lateral);

        javax.swing.JPanel accoes = util.ui.ComponentesWeb.transparente(null); // os botões estão nos cartões e no cabeçalho
        accoes.setPreferredSize(new java.awt.Dimension(0, 0));
        util.ui.FormularioWeb.montar(this, cabecalho, colunas, accoes, "Lavandaria  ·  Recolha");
        util.ui.EstiloUI.ajustarAoEcra(this, 1000, 650);
    }

    /**
     * Coluna direita dentro de um JScrollPane: acompanha a largura e, quando há
     * altura de sobra, estica (o balcão fica no fundo); quando falta, desliza.
     */
    private static final class ColunaLateral extends javax.swing.JPanel implements javax.swing.Scrollable {

        private final java.awt.Component norte;
        private final comercial.lavandaria.BalcaoLavandaria balcao;

        ColunaLateral(java.awt.Component norte, comercial.lavandaria.BalcaoLavandaria balcao) {
            // sem vgap: o BorderLayout contava-o duas vezes (norte e sul) e a coluna deslizava a 1366 x 768
            super(new java.awt.BorderLayout());
            this.norte = norte;
            this.balcao = balcao;
            setOpaque(false);
            balcao.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 0, 0, 0));
            add(norte, java.awt.BorderLayout.NORTH);
            add(balcao, java.awt.BorderLayout.SOUTH);
        }

        /** Balcão completo se couber por baixo do cliente e da peça (ou se a coluna estiver empilhada, larga); senão, a faixa. */
        @Override
        public void doLayout() {
            if (getParent() instanceof javax.swing.JViewport) {
                int disponivel = getParent().getHeight();
                int precisa = norte.getPreferredSize().height + 10 + balcao.alturaCompleta(getWidth());
                boolean compacto = disponivel < precisa && getWidth() < 600;
                if (compacto != balcao.isCompacto()) {
                    balcao.setCompacto(compacto);
                    javax.swing.SwingUtilities.invokeLater(this::revalidate);
                }
            }
            super.doLayout();
        }

        @Override
        public java.awt.Dimension getPreferredScrollableViewportSize() {
            return getPreferredSize();
        }

        @Override
        public int getScrollableUnitIncrement(java.awt.Rectangle r, int o, int d) {
            return 24;
        }

        @Override
        public int getScrollableBlockIncrement(java.awt.Rectangle r, int o, int d) {
            return Math.max(24, r.height - 40);
        }

        @Override
        public boolean getScrollableTracksViewportWidth() {
            return true;
        }

        @Override
        public boolean getScrollableTracksViewportHeight() {
            return getParent() instanceof javax.swing.JViewport && getParent().getHeight() > getPreferredSize().height;
        }
    }

    /**
     * Botão de serviço (Lavagem / Engomagem) em pílula: a lógica antiga desactiva
     * o serviço escolhido, por isso "desactivado" desenha-se como seleccionado
     * (azul, com visto) e continua a não se poder carregar outra vez.
     */
    private static final class ServicoUI extends javax.swing.plaf.basic.BasicButtonUI {

        private final String texto;
        private final util.ui.MenuWeb.Simbolo simbolo;

        private ServicoUI(String texto, util.ui.MenuWeb.Simbolo simbolo) {
            this.texto = texto;
            this.simbolo = simbolo;
        }

        static void aplicar(javax.swing.JButton b, String texto, util.ui.MenuWeb.Simbolo simbolo, String dica) {
            b.setUI(new ServicoUI(texto, simbolo));
            b.setText(texto);
            b.setIcon(null);
            b.setOpaque(false);
            b.setContentAreaFilled(false);
            b.setBorderPainted(false);
            b.setFocusPainted(false);
            b.setRolloverEnabled(true);
            b.setFont(util.ui.EstiloUI.FONTE_NEGRITO.deriveFont(13f));
            b.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));
            b.setToolTipText(dica);
        }

        @Override
        public java.awt.Dimension getPreferredSize(javax.swing.JComponent c) {
            java.awt.FontMetrics f = c.getFontMetrics(c.getFont());
            return new java.awt.Dimension(f.stringWidth(texto) + 64, 34);
        }

        @Override
        public void paint(java.awt.Graphics g, javax.swing.JComponent c) {
            javax.swing.AbstractButton b = (javax.swing.AbstractButton) c;
            boolean escolhido = !b.isEnabled();
            boolean rato = b.getModel().isRollover() && !escolhido;
            java.awt.Graphics2D g2 = util.ui.ComponentesWeb.suave(g);
            int w = c.getWidth(), h = c.getHeight();
            java.awt.Color azul = util.ui.ComponentesWeb.AZUL;
            g2.setColor(escolhido ? azul : rato ? new java.awt.Color(239, 246, 255) : java.awt.Color.WHITE);
            g2.fillRoundRect(0, 0, w - 1, h - 1, h, h);
            g2.setColor(escolhido ? azul.darker() : util.ui.ComponentesWeb.LINHA);
            g2.drawRoundRect(0, 0, w - 1, h - 1, h, h);
            java.awt.Color fg = escolhido ? java.awt.Color.WHITE : azul;
            util.ui.MenuWeb.desenharSimbolo(g2, simbolo, 12, (h - 18) / 2.0, 18, fg);
            g2.setFont(c.getFont());
            java.awt.FontMetrics f = g2.getFontMetrics();
            g2.setColor(fg);
            g2.drawString(texto, 36, (h + f.getAscent() - f.getDescent()) / 2);
            if (escolhido) { // visto
                int x = w - 20, y = h / 2;
                g2.setStroke(new java.awt.BasicStroke(2.2f, java.awt.BasicStroke.CAP_ROUND, java.awt.BasicStroke.JOIN_ROUND));
                g2.drawLine(x, y, x + 3, y + 3);
                g2.drawLine(x + 3, y + 3, x + 9, y - 4);
            }
            g2.dispose();
        }
    }

    private static void botaoCategoriaRoupa(javax.swing.JButton b) {
        b.putClientProperty(util.ui.MesasWeb.SIMBOLO_CATEGORIA, util.ui.MenuWeb.Simbolo.CABIDE);
        util.ui.MesasWeb.botaoCategoria(b);
    }

    private static void pilulaCabecalho(javax.swing.JButton b, util.ui.ComponentesWeb.Variante v, String dica) {
        if (v != null) {
            util.ui.ComponentesWeb.botao(b, v, null, dica);
        }
        b.setIcon(null);
        b.putClientProperty("web.pilula", Boolean.TRUE);
        b.setBorder(javax.swing.BorderFactory.createEmptyBorder(6, 14, 6, 16));
        b.setFont(util.ui.EstiloUI.FONTE_NEGRITO.deriveFont(12.5f));
    }

    /**
     * Fotografias de peças do catálogo (ao acaso) para o cabeçalho; se a base de
     * dados tiver poucas, completa com as imagens da lavandaria do projecto.
     */
    private static java.util.List<java.awt.Image> fotosDeRoupa(int quantas) {
        java.util.List<java.awt.Image> fotos = new java.util.ArrayList<>();
        try {
            java.util.List<Integer> codigos = new java.util.ArrayList<>();
            java.sql.ResultSet rs = conexao.executeQuery("SELECT codigo FROM tb_produto WHERE photo IS NOT NULL AND LENGTH(photo) > 0"
                    + " ORDER BY RAND() LIMIT " + quantas);
            while (rs != null && rs.next()) {
                codigos.add(rs.getInt(1));
            }
            if (!codigos.isEmpty()) {
                StringBuilder in = new StringBuilder();
                for (Integer c : codigos) {
                    in.append(in.length() == 0 ? "" : ",").append(c);
                }
                rs = conexao.executeQuery("SELECT photo FROM tb_produto WHERE codigo IN (" + in + ")");
                while (rs != null && rs.next()) {
                    java.awt.Image i = foto(rs.getBytes(1));
                    if (i != null) {
                        fotos.add(i);
                    }
                }
            }
        } catch (Exception e) {
            // sem fotografias: ficam as imagens do projecto
        }
        for (String r : new String[]{"/imagem/lavandaria/roupa_01_01.png", "/imagem/maquina-de-lavar.png",
            "/imagem/ferro.png", "/imagem/lavandaria/recolha_01_01.png"}) {
            if (fotos.size() >= Math.min(quantas, 4)) {
                break;
            }
            java.net.URL url = RecolhaPedidosVisao.class.getResource(r);
            if (url != null) {
                fotos.add(new ImageIcon(url).getImage());
            }
        }
        return fotos;
    }

    /** Fotografias redondas, lado a lado, para o cabeçalho escuro (mostra as que couberem). */
    private static final class FaixaRoupa extends javax.swing.JComponent {

        private static final int D = 52, PASSO = 60;
        private final java.util.List<java.awt.Image> fotos = new java.util.ArrayList<>();

        FaixaRoupa(java.util.List<java.awt.Image> origem) {
            for (java.awt.Image i : origem) {
                fotos.add(redonda(i));
            }
            setPreferredSize(new java.awt.Dimension(PASSO * fotos.size(), D + 6));
            setMinimumSize(new java.awt.Dimension(0, D + 6));
            setToolTipText("Lavandaria");
        }

        private static java.awt.image.BufferedImage redonda(java.awt.Image img) {
            int s = D * 2; // o dobro, para ficar nítida em ecrãs com escala
            java.awt.image.BufferedImage b = new java.awt.image.BufferedImage(s, s, java.awt.image.BufferedImage.TYPE_INT_ARGB);
            java.awt.Graphics2D g = b.createGraphics();
            g.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
            g.setRenderingHint(java.awt.RenderingHints.KEY_INTERPOLATION, java.awt.RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g.setRenderingHint(java.awt.RenderingHints.KEY_RENDERING, java.awt.RenderingHints.VALUE_RENDER_QUALITY);
            g.setColor(java.awt.Color.WHITE);
            g.fillOval(0, 0, s, s);
            g.setClip(new java.awt.geom.Ellipse2D.Double(0, 0, s, s));
            int w = Math.max(1, img.getWidth(null)), h = Math.max(1, img.getHeight(null));
            double esc = Math.min((double) s * 0.86 / w, (double) s * 0.86 / h); // a peça inteira, com margem
            int fw = (int) (w * esc), fh = (int) (h * esc);
            g.drawImage(img, (s - fw) / 2, (s - fh) / 2, fw, fh, null);
            g.dispose();
            return b;
        }

        @Override
        protected void paintComponent(java.awt.Graphics g) {
            java.awt.Graphics2D g2 = util.ui.ComponentesWeb.suave(g);
            g2.setRenderingHint(java.awt.RenderingHints.KEY_INTERPOLATION, java.awt.RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            int y = (getHeight() - D) / 2;
            int cabem = Math.min(fotos.size(), Math.max(0, (getWidth() - (D - PASSO)) / PASSO));
            for (int i = 0; i < cabem; i++) {
                int x = i * PASSO;
                g2.setColor(new java.awt.Color(255, 255, 255, 60));
                g2.fillOval(x - 3, y - 3, D + 6, D + 6);
                g2.drawImage(fotos.get(i), x, y, D, D, null);
            }
            g2.dispose();
        }
    }

//        @Override
    public void run() {
        while (true) {

            verificarCaixa();
        }
    }

    private void setArmazem(String armazem) {
        if (armazem.equalsIgnoreCase("Multi_armazem")) {
            rbArmazem.setSelected(true);

        } else {
            rbArmazem1.setSelected(true);

        }
    }

    private static String nomeUtilizador = "";

    /** Nome do utilizador com sessão nesta recolha (o pagamento mostra-o no cabeçalho). */
    public static String getNomeUtilizador() {
        return nomeUtilizador == null ? "" : nomeUtilizador;
    }

    private void boas_vinda() {
        TbUsuario usuario = usuariosController.getUsuarioByCodigo(this.idUser);

        System.err.println(" codigo local do Usuario: " + this.idUser);
        jlUsuario.setText("Usuario(a): " + usuario.getNome());
        nomeUtilizador = usuario.getNome() == null ? "" : usuario.getNome().trim();
        System.err.println("nome usuario1: " + usuario.getNome());

//        if (usuario.getCodigoSexo().getCodigo() == 1) {
//        } else {
//            jlUsuario.setText("Seja bem vinda! Sr.ª  " + usuario.getNome());
//            System.err.println("nome usuario2: " + usuario.getNome());
//        }
    }

    private void empresa() {
        TbDadosInstituicao dados = (TbDadosInstituicao) dadosInstituicaoController.findById(1);

        jlEmpresa.setText(dados.getNome());

    }

    public static int getIdMoeda() {
        try {
            Moeda moedaLocal = moedasController.getMoedaByDesignacao(cmbMoeda.getSelectedItem().toString());
            return moedaLocal.getPkMoeda();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }

    }

    public void configurar_armazens() {
        setArmazem(dadosInstituicao.getConfigArmazens());
        try {
            if (!rbArmazem.isSelected()) {
                //                Caso for MultiArmazens
                cmbArmazem.setModel(new DefaultComboBoxModel(armazensController.getVector2()));
            } else if (rbArmazem.isSelected()) {
                //                Caso for apenas Um Armazem
                cmbArmazem.setModel(new DefaultComboBoxModel(armazensAccessoController.getAllArmazemExceptoEconomatoByIdUSuario(idUser)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class TratarEventoTeclado implements KeyListener {

        String prefixo = "";

        public void keyPressed(KeyEvent evt) {

            if (evt.getKeyCode() != KeyEvent.VK_BACK_SPACE && evt.getKeyCode() != KeyEvent.VK_ENTER) {
                char key = evt.getKeyChar();

                try {

                    prefixo = txtIniciaisCliente.getText().trim() + key;
                    cmbCliente.setModel(new DefaultComboBoxModel(clientesController.getVectorByIinciais(prefixo)));
                    mostra_consumidor_final();
                } catch (Exception e) {
                    cmbCliente.setSelectedIndex(0);
                    mostra_consumidor_final();
                }

            } else if (evt.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                try {

                    prefixo = prefixo.toString().trim().substring(0, prefixo.length() - 1);
                    System.out.println("NOME VOLTAR " + prefixo);
                    cmbCliente.setModel(new DefaultComboBoxModel(clientesController.getVectorByIinciais(prefixo)));
                    mostra_consumidor_final();

                } catch (Exception e) {
                    cmbCliente.setSelectedIndex(0);
                    mostra_consumidor_final();
                }

            }
        }

        public void keyReleased(KeyEvent evt) {
        }

        public void keyTyped(KeyEvent evt) {
        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        buttonGroup3 = new javax.swing.ButtonGroup();
        buttonGroup4 = new javax.swing.ButtonGroup();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        btn_voltar = new javax.swing.JButton();
        designacao_categoria = new javax.swing.JLabel();
        jlUsuario = new javax.swing.JLabel();
        jlEmpresa = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtTotalApagar = new javax.swing.JTextField();
        txtTotalQTD = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        BT_Conversao = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        btFT = new javax.swing.JButton();
        BtnProforma = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtObs = new javax.swing.JTextArea();
        lbServico = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        dc_data_entrega = new com.toedter.calendar.JDateChooser();
        jButton1 = new javax.swing.JButton();
        rbTaxaExpresso100 = new javax.swing.JRadioButton();
        rbTaxaUrgente50 = new javax.swing.JRadioButton();
        jLabel5 = new javax.swing.JLabel();
        txtLargura = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtAltura = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtQtdItens = new javax.swing.JTextField();
        lbSomaLarguraAltura = new javax.swing.JLabel();
        lbTotalQtd = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jRadioButton1 = new javax.swing.JRadioButton();
        status_mensagem_secundaria = new javax.swing.JLabel();
        status_mensagem_primaria = new javax.swing.JLabel();
        lbValorPorExtenco = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        cmbArmazem = new javax.swing.JComboBox<>();
        cmbAnoEconomico = new javax.swing.JComboBox<>();
        cmbMoeda = new javax.swing.JComboBox();
        cmb_area_venda_restaurante = new javax.swing.JComboBox<>();
        cmbTipoDocumento = new javax.swing.JComboBox();
        dc_data_documento = new com.toedter.calendar.JDateChooser();
        rbArmazem = new javax.swing.JRadioButton();
        rbArmazem1 = new javax.swing.JRadioButton();
        lb_proximo_documento = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        txtQuatidadeExistente = new javax.swing.JTextField();
        lbQuantidadeExistente = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        btnLavar = new javax.swing.JButton();
        btnEngomar = new javax.swing.JButton();
        dcDataInicio = new com.toedter.calendar.JDateChooser();
        dcDataFim = new com.toedter.calendar.JDateChooser();
        jScrollPane4 = new javax.swing.JScrollPane();
        jtable_reimpressao = new javax.swing.JTable();
        jButton6 = new javax.swing.JButton();
        lbPreco4 = new javax.swing.JLabel();
        cmbSeries = new javax.swing.JComboBox();
        jPanel7 = new javax.swing.JPanel();
        lbClienteConsumidorFinal1 = new javax.swing.JLabel();
        txtIniciaisCliente = new javax.swing.JTextField();
        btCliente = new javax.swing.JButton();
        cmbCliente = new javax.swing.JComboBox<>();
        lbTelefoneCliente = new javax.swing.JLabel();
        lbEmailCliente = new javax.swing.JLabel();
        txtTelefoneCliente = new javax.swing.JTextField();
        lbClienteConsumidorFinal2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("RECOLHA DE PEÇAS");

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel4.setEnabled(false);
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jScrollPane3.setViewportView(jPanel1);

        jPanel4.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 670, 500));

        btn_voltar.setText("<<");
        btn_voltar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_voltarActionPerformed(evt);
            }
        });
        jPanel4.add(btn_voltar, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 80, 30));

        designacao_categoria.setBackground(new java.awt.Color(255, 255, 255));
        designacao_categoria.setFont(new java.awt.Font("Lucida Grande", 3, 22)); // NOI18N
        designacao_categoria.setForeground(new java.awt.Color(204, 0, 0));
        designacao_categoria.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        designacao_categoria.setText("designacao_categoria");
        designacao_categoria.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        designacao_categoria.setOpaque(true);
        jPanel4.add(designacao_categoria, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 10, 580, -1));

        jlUsuario.setFont(new java.awt.Font("Lucida Grande", 1, 18)); // NOI18N
        jlUsuario.setForeground(new java.awt.Color(0, 102, 102));
        jlUsuario.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jlUsuario.setText("Usuario");
        jPanel4.add(jlUsuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 570, 650, 20));

        jlEmpresa.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        jlEmpresa.setForeground(new java.awt.Color(0, 0, 102));
        jlEmpresa.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jlEmpresa.setText("Empresa");
        jPanel4.add(jlEmpresa, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 550, 650, 20));

        jLabel1.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        jLabel1.setText("Valor Pagar");

        txtTotalApagar.setEditable(false);
        txtTotalApagar.setFont(new java.awt.Font("Arial", 1, 30)); // NOI18N
        txtTotalApagar.setForeground(new java.awt.Color(255, 51, 51));
        txtTotalApagar.setText("0");

        txtTotalQTD.setEditable(false);
        txtTotalQTD.setFont(new java.awt.Font("Arial", 1, 30)); // NOI18N
        txtTotalQTD.setForeground(new java.awt.Color(0, 0, 102));
        txtTotalQTD.setText("0");

        jLabel9.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        jLabel9.setText("Total de Peças");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtTotalApagar, javax.swing.GroupLayout.PREFERRED_SIZE, 262, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtTotalQTD, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(37, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtTotalQTD, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addComponent(jLabel1)
                    .addComponent(txtTotalApagar, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21))
        );

        BT_Conversao.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        BT_Conversao.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/1323444801_currency_dollar red.png"))); // NOI18N
        BT_Conversao.setText("Cobrar");
        BT_Conversao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BT_ConversaoActionPerformed(evt);
            }
        });

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/LOGOUT - VERMELHO/Logout 32x32.png"))); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        btFT.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        btFT.setText("Processar");
        btFT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btFTActionPerformed(evt);
            }
        });

        BtnProforma.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        BtnProforma.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/1588_32x32.png"))); // NOI18N
        BtnProforma.setText("Proforma");
        BtnProforma.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnProformaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(BT_Conversao, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btFT, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(BtnProforma)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 80, Short.MAX_VALUE)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(17, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(btFT, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(BtnProforma))
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 49, Short.MAX_VALUE)
                    .addComponent(BT_Conversao, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel5Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {BT_Conversao, BtnProforma, btFT, jButton2});

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "D. Entrega", "Serviço", "QTD", "Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, true, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                tableMouseEntered(evt);
            }
        });
        table.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                tablePropertyChange(evt);
            }
        });
        jScrollPane2.setViewportView(table);
        if (table.getColumnModel().getColumnCount() > 0) {
            table.getColumnModel().getColumn(0).setMaxWidth(10);
            table.getColumnModel().getColumn(1).setMaxWidth(100);
            table.getColumnModel().getColumn(3).setMaxWidth(50);
            table.getColumnModel().getColumn(4).setMaxWidth(100);
        }

        jLabel2.setText("Obs:");

        txtObs.setColumns(20);
        txtObs.setRows(5);
        jScrollPane1.setViewportView(txtObs);

        lbServico.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lbServico.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbServico.setText("serviço");

        jLabel3.setText("Data Entrega");

        dc_data_entrega.setDateFormatString("yyyy-MM-dd");
        dc_data_entrega.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                dc_data_entregaPropertyChange(evt);
            }
        });

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/actualizar_1_32x32_1.png"))); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        buttonGroup4.add(rbTaxaExpresso100);
        rbTaxaExpresso100.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        rbTaxaExpresso100.setText("Taxa Expresso 100%");
        rbTaxaExpresso100.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbTaxaExpresso100ActionPerformed(evt);
            }
        });

        buttonGroup4.add(rbTaxaUrgente50);
        rbTaxaUrgente50.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        rbTaxaUrgente50.setText("Taxa de Urgência 50%");
        rbTaxaUrgente50.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbTaxaUrgente50ActionPerformed(evt);
            }
        });

        jLabel5.setText("Largura:");

        txtLargura.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtLarguraActionPerformed(evt);
            }
        });

        jLabel6.setText("Altura");

        txtAltura.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAlturaActionPerformed(evt);
            }
        });

        jLabel7.setText("x Qtd Item");

        txtQtdItens.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtQtdItensActionPerformed(evt);
            }
        });

        lbSomaLarguraAltura.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lbSomaLarguraAltura.setText(" X");

        lbTotalQtd.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lbTotalQtd.setText("= XXX");

        jLabel8.setText("=");

        buttonGroup4.add(jRadioButton1);
        jRadioButton1.setSelected(true);
        jRadioButton1.setText("Sem Taxa");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbServico, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 311, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel2))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(dc_data_entrega, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(rbTaxaExpresso100, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(rbTaxaUrgente50, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 261, Short.MAX_VALUE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jRadioButton1))))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtLargura, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtAltura, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel8)
                                .addGap(18, 18, 18)
                                .addComponent(lbSomaLarguraAltura, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtQtdItens, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lbTotalQtd, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane2))
                        .addGap(0, 17, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbServico, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtLargura, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(txtAltura, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(txtQtdItens, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbTotalQtd, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(lbSomaLarguraAltura, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButton1)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(dc_data_entrega, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(rbTaxaExpresso100)
                            .addComponent(jRadioButton1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(rbTaxaUrgente50)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        status_mensagem_secundaria.setFont(new java.awt.Font("Lucida Grande", 1, 12)); // NOI18N
        status_mensagem_secundaria.setText("...");

        status_mensagem_primaria.setFont(new java.awt.Font("Lucida Grande", 1, 12)); // NOI18N
        status_mensagem_primaria.setForeground(new java.awt.Color(204, 153, 0));
        status_mensagem_primaria.setText("sahdbvhsdva");

        lbValorPorExtenco.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbValorPorExtenco.setForeground(new java.awt.Color(204, 0, 0));
        lbValorPorExtenco.setText("VALOR POR EXTENSO");

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        cmbArmazem.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbArmazem.setEnabled(false);
        cmbArmazem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbArmazemActionPerformed(evt);
            }
        });

        cmbAnoEconomico.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbAnoEconomico.setEnabled(false);
        cmbAnoEconomico.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbAnoEconomicoActionPerformed(evt);
            }
        });

        cmbMoeda.setBackground(new java.awt.Color(0, 51, 102));
        cmbMoeda.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 14)); // NOI18N
        cmbMoeda.setEnabled(false);
        cmbMoeda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbMoedaActionPerformed(evt);
            }
        });

        cmb_area_venda_restaurante.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cmb_area_venda_restaurante.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Restaurante", "Alojamento" }));
        cmb_area_venda_restaurante.setEnabled(false);

        cmbTipoDocumento.setBackground(new java.awt.Color(0, 51, 102));
        cmbTipoDocumento.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        cmbTipoDocumento.setForeground(new java.awt.Color(255, 255, 255));
        cmbTipoDocumento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbTipoDocumentoActionPerformed(evt);
            }
        });

        dc_data_documento.setFont(new java.awt.Font("Lucida Grande", 1, 12)); // NOI18N

        buttonGroup2.add(rbArmazem);
        rbArmazem.setSelected(true);
        rbArmazem.setEnabled(false);
        rbArmazem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbArmazemActionPerformed(evt);
            }
        });

        buttonGroup2.add(rbArmazem1);
        rbArmazem1.setEnabled(false);
        rbArmazem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbArmazem1ActionPerformed(evt);
            }
        });

        lb_proximo_documento.setFont(new java.awt.Font("Lucida Grande", 1, 11)); // NOI18N
        lb_proximo_documento.setText("PRÓX. DOC. : XX PP/A1");

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/proucura.png"))); // NOI18N
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        txtQuatidadeExistente.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        txtQuatidadeExistente.setEnabled(false);
        txtQuatidadeExistente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtQuatidadeExistenteActionPerformed(evt);
            }
        });

        lbQuantidadeExistente.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lbQuantidadeExistente.setText("QTD Stock :");

        jLabel4.setText("Tabela Preços");

        jButton3.setFont(new java.awt.Font("Arial", 1, 10)); // NOI18N
        jButton3.setText("Reimprimir Documento");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        btnLavar.setFont(new java.awt.Font("Mongolian Baiti", 1, 17)); // NOI18N
        btnLavar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagem/maquina-de-lavar.png"))); // NOI18N
        btnLavar.setText("Lavagem");
        btnLavar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLavarActionPerformed(evt);
            }
        });

        btnEngomar.setFont(new java.awt.Font("Mongolian Baiti", 1, 17)); // NOI18N
        btnEngomar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagem/ferro.png"))); // NOI18N
        btnEngomar.setText("Engomagem");
        btnEngomar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEngomarActionPerformed(evt);
            }
        });

        dcDataInicio.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N

        dcDataFim.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N

        jtable_reimpressao.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Doc"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jtable_reimpressao.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtable_reimpressaoMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(jtable_reimpressao);

        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/actualizar_1.png"))); // NOI18N
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        lbPreco4.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbPreco4.setText("Séries");

        cmbSeries.setBackground(new java.awt.Color(0, 51, 102));
        cmbSeries.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 14)); // NOI18N

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(dcDataInicio, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(dcDataFim, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(cmbTipoDocumento, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(6, 6, 6)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel6Layout.createSequentialGroup()
                                        .addComponent(dc_data_documento, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(lbQuantidadeExistente, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtQuatidadeExistente, javax.swing.GroupLayout.PREFERRED_SIZE, 9, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel6Layout.createSequentialGroup()
                                        .addComponent(jButton6)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(lb_proximo_documento, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(cmbArmazem, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cmbAnoEconomico, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(lbPreco4, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cmbSeries, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnLavar, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 48, Short.MAX_VALUE)
                        .addComponent(btnEngomar, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rbArmazem)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rbArmazem1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbMoeda, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmb_area_venda_restaurante, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel6Layout.createSequentialGroup()
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(cmb_area_venda_restaurante, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cmbMoeda))
                                .addGap(5, 5, 5))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel6Layout.createSequentialGroup()
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(cmbArmazem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(cmbAnoEconomico, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel6Layout.createSequentialGroup()
                                            .addGap(6, 6, 6)
                                            .addComponent(rbArmazem1, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(rbArmazem, javax.swing.GroupLayout.Alignment.TRAILING)))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lb_proximo_documento, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(dcDataInicio, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(dcDataFim, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jButton6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(lbQuantidadeExistente, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(22, 22, 22))
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(cmbSeries, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(lbPreco4))
                                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(txtQuatidadeExistente, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(cmbTipoDocumento, javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(dc_data_documento, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addGap(17, 17, 17)))))
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnEngomar, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnLavar)))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(3, 3, 3))
        );

        jPanel6Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {cmbAnoEconomico, cmbArmazem});

        jPanel6Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btnEngomar, btnLavar});

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        lbClienteConsumidorFinal1.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbClienteConsumidorFinal1.setText("Pesquisar Clientes:");

        txtIniciaisCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIniciaisClienteActionPerformed(evt);
            }
        });
        txtIniciaisCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtIniciaisClienteKeyPressed(evt);
            }
        });

        btCliente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/usuario.png"))); // NOI18N
        btCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btClienteActionPerformed(evt);
            }
        });

        cmbCliente.setFont(new java.awt.Font("Lucida Grande", 1, 18)); // NOI18N
        cmbCliente.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbClienteActionPerformed(evt);
            }
        });

        lbTelefoneCliente.setText("Telefone");

        lbEmailCliente.setText("Email");

        txtTelefoneCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTelefoneClienteActionPerformed(evt);
            }
        });
        txtTelefoneCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtTelefoneClienteKeyPressed(evt);
            }
        });

        lbClienteConsumidorFinal2.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbClienteConsumidorFinal2.setText("Telefone:");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(lbTelefoneCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 552, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(lbEmailCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 552, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(cmbCliente, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel7Layout.createSequentialGroup()
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel7Layout.createSequentialGroup()
                                        .addComponent(lbClienteConsumidorFinal1, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addComponent(txtIniciaisCliente))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lbClienteConsumidorFinal2, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtTelefoneCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(73, 73, 73))))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbClienteConsumidorFinal1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbClienteConsumidorFinal2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtIniciaisCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtTelefoneCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btCliente, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbTelefoneCliente)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lbEmailCliente)
                .addGap(17, 17, 17))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, 698, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jPanel7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lbValorPorExtenco, javax.swing.GroupLayout.PREFERRED_SIZE, 575, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(status_mensagem_primaria, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(status_mensagem_secundaria, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(79, 79, 79))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(status_mensagem_primaria)
                        .addComponent(status_mensagem_secundaria))
                    .addComponent(lbValorPorExtenco))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public static int getCodigoProduto() throws SQLException {
        return 0;
    }

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        dispose();
    }//GEN-LAST:event_jButton2ActionPerformed


    private void BT_ConversaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BT_ConversaoActionPerformed

        if (true) {
            if (MetodosUtil.licencaValidada(conexao)) {

                if (cmbTipoDocumento.getSelectedIndex() == 0) {
                    JOptionPane.showMessageDialog(null, "Atenção\nSelecione o tipo de documento!");
                } else {
                    if (validar_cliente()) {
                        procedimento_processar_facturacao();
                    }

                }

            }

        }

//       

    }//GEN-LAST:event_BT_ConversaoActionPerformed

    public boolean validar_cliente() {
        if (cmbCliente.getSelectedItem().equals("--Seleccione o Cliente--")) {

            JOptionPane.showMessageDialog(null, "Por favor, Seleccione ou Digite o nome do Cliente!");
            txtIniciaisCliente.requestFocus();
            return false;

//        } else if ( dc_data_vencimento.getSelectedItem().equals( "--Seleccione--" ) )
        }

        return true;
    }

    private void procedimento_processar_facturacao() {

        // desconto da ficha do cliente (ex.: 100%) que deixa a factura a zero: confirmar antes de cobrar
        setTotalPagar();
        double percentagem = getDescontoClientePercentagem();
        if (table.getRowCount() > 0 && percentagem > 0
                && CfMethods.parseMoedaFormatada(txtTotalApagar.getText()) <= 0.004) {
            int r = JOptionPane.showConfirmDialog(this, "O cliente " + cmbCliente.getSelectedItem() + " tem "
                    + new java.text.DecimalFormat("#,##0.##").format(percentagem) + "% de desconto na ficha:\n"
                    + "o total a pagar fica 0,00.\n\nContinuar mesmo assim?\n"
                    + "(Para cobrar o valor, corrija o desconto na ficha do cliente.)",
                    "Desconto do cliente", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (r != JOptionPane.YES_OPTION) {
                return;
            }
        }

        if (verifica_ano_documento_igual_economico()) {
            if (data_documento_superior_ou_igual_ao_ultimo_doc()) {
                if (true) {
//                    new FormaPagamentoVisao( this, rootPaneCheckingEnabled, emf, DVML.VENDA_LAVANDARIA, BDConexao.getInstancia()).setVisible(true);
                    new FormaPagamentoGoldVisao(this, rootPaneCheckingEnabled, BDConexao.getInstancia()).setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, "O documento não pode ser processado porque possui uma data inferior ao úlimo documento efectuado", "AVISO", JOptionPane.WARNING_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "A data do documento a ser emitido deve estar no intervalo do ano economico", "Aviso", JOptionPane.WARNING_MESSAGE);
        }

    }

    public static void procedimento_converter_factura() {

        if (true) {
            conexaoTransaction = BDConexao.getInstancia();
            DocumentosController.start(conexaoTransaction);
            try {
                TbVenda salvar_venda = salvar_venda();

                if (salvar_venda != null) {
                    System.err.println("VENDA AQUI " + salvar_venda.getCodigo());

                    salvarItemvenda(salvar_venda);
                    remover_dados_tabela();

                } else {
                    System.err.println("  erro ao salvar venda!!!");
                }
            } catch (RuntimeException e) {
                // nada fica a meio: sem isto a venda incompleta era confirmada no commit seguinte
                e.printStackTrace();
                DocumentosController.rollback(conexaoTransaction);
                JOptionPane.showMessageDialog(null, "O documento não foi emitido:\n" + e, "Recolha", JOptionPane.ERROR_MESSAGE);
            }
        }

    }

    static void procedimento_processar_factura_proforma() throws SQLException {

        if (true) {
            conexaoTransaction = BDConexao.getInstancia();
            DocumentosController.start(conexaoTransaction);
            try {
                TbVenda salvar_venda = salvar_venda();

                if (salvar_venda != null) {
    //                vendasController.actualizar_hash_and_assinatura( salvar_venda.getCodigo(), getGrossTotal() );
                    System.err.println("VENDA AQUI " + salvar_venda.getCodigo());
                    salvarItemvenda(salvar_venda);
                    remover_dados_tabela();

                } else {
                    System.err.println("  erro ao salvar venda!!!");
                }
            } catch (RuntimeException e) {
                // nada fica a meio: sem isto a venda incompleta era confirmada no commit seguinte
                e.printStackTrace();
                DocumentosController.rollback(conexaoTransaction);
                JOptionPane.showMessageDialog(null, "O documento não foi emitido:\n" + e, "Recolha", JOptionPane.ERROR_MESSAGE);
            }
        }

    }


    private void tableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableMouseClicked
        // TODO add your handling code here:

        if (evt.getClickCount() == 1) {
            setDados();
        }

        if (evt.getClickCount() >= 2) {
            procedimento_eliminar_item_pedido();
        }

    }//GEN-LAST:event_tableMouseClicked

    private void cmbClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbClienteActionPerformed
        mostra_consumidor_final();

        setTotalPagar();
    }//GEN-LAST:event_cmbClienteActionPerformed

    private void tableMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_tableMouseEntered

    private void cmbAnoEconomicoActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbAnoEconomicoActionPerformed
    {//GEN-HEADEREND:event_cmbAnoEconomicoActionPerformed
        // TODO add your handling code here:
        mostrar_proximo_codigo_documento();
        actualizar_abreviacao();
    }//GEN-LAST:event_cmbAnoEconomicoActionPerformed

    private void cmbArmazemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbArmazemActionPerformed
    {//GEN-HEADEREND:event_cmbArmazemActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbArmazemActionPerformed

    private void rbArmazem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbArmazem1ActionPerformed
        try {
            configurar_armazens();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_rbArmazem1ActionPerformed

    private void rbArmazemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbArmazemActionPerformed
        try {
            configurar_armazens();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_rbArmazemActionPerformed

    private void btn_voltarActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btn_voltarActionPerformed
    {//GEN-HEADEREND:event_btn_voltarActionPerformed
        // TODO add your handling code here:
        adicionar_catgorias();
    }//GEN-LAST:event_btn_voltarActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton4ActionPerformed
    {//GEN-HEADEREND:event_jButton4ActionPerformed
        try {
//            if ( activo_um_lugar() )
//            {
            new BuscaProdutoVisao(this, rootPaneCheckingEnabled, getCodigoArmazem(), DVML.JANELA_RECOLHA, BDConexao.getInstancia()).setVisible(true);
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void tablePropertyChange(java.beans.PropertyChangeEvent evt)//GEN-FIRST:event_tablePropertyChange
    {//GEN-HEADEREND:event_tablePropertyChange
        // TODO add your handling code here:

        if (table.getSelectedColumn() == 2) {
            System.out.println("change");
            setDados();
        }

        if (table.getSelectedColumn() == 3) {
            System.out.println("change");
            actualizarQtdTable();
        }

    }//GEN-LAST:event_tablePropertyChange

    private void cmbMoedaActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbMoedaActionPerformed
    {//GEN-HEADEREND:event_cmbMoedaActionPerformed
        // TODO add your handling code here:
        actualizar_moeda();
    }//GEN-LAST:event_cmbMoedaActionPerformed

    private void cmbTipoDocumentoActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbTipoDocumentoActionPerformed
    {//GEN-HEADEREND:event_cmbTipoDocumentoActionPerformed
        // TODO add your handling code here:
        mostrar_proximo_codigo_documento();
        actualizar_abreviacao();
        selecionar_documento();

    }//GEN-LAST:event_cmbTipoDocumentoActionPerformed

    private void btClienteActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btClienteActionPerformed
    {//GEN-HEADEREND:event_btClienteActionPerformed
        new ClienteInternoVisao(this, rootPaneCheckingEnabled, BDConexao.getInstancia()).setVisible(true);
    }//GEN-LAST:event_btClienteActionPerformed

    private void txtIniciaisClienteActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtIniciaisClienteActionPerformed
    {//GEN-HEADEREND:event_txtIniciaisClienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIniciaisClienteActionPerformed

    private void txtIniciaisClienteKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtIniciaisClienteKeyPressed
    {//GEN-HEADEREND:event_txtIniciaisClienteKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIniciaisClienteKeyPressed

    private void txtQuatidadeExistenteActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtQuatidadeExistenteActionPerformed
    {//GEN-HEADEREND:event_txtQuatidadeExistenteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtQuatidadeExistenteActionPerformed

    private void txtTelefoneClienteActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtTelefoneClienteActionPerformed
    {//GEN-HEADEREND:event_txtTelefoneClienteActionPerformed
        // TODO add your handling code here:
        pesquisa_cliente_by_telefone();
    }//GEN-LAST:event_txtTelefoneClienteActionPerformed

    private void txtTelefoneClienteKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtTelefoneClienteKeyPressed
    {//GEN-HEADEREND:event_txtTelefoneClienteKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTelefoneClienteKeyPressed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton3ActionPerformed
    {//GEN-HEADEREND:event_jButton3ActionPerformed
        new ReemprimirFacturasRecolhaVisao(this, rootPaneCheckingEnabled, this.conexao).setVisible(true);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void dc_data_entregaPropertyChange(java.beans.PropertyChangeEvent evt)//GEN-FIRST:event_dc_data_entregaPropertyChange
    {//GEN-HEADEREND:event_dc_data_entregaPropertyChange
        // TODO add your handling code here:

        try {
            procedimetoAdicionarUrgencia();
        } catch (Exception e) {
        }
    }//GEN-LAST:event_dc_data_entregaPropertyChange

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton1ActionPerformed
    {//GEN-HEADEREND:event_jButton1ActionPerformed
        // TODO add your handling code here:
        adicionarDateEntrega();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void rbTaxaExpresso100ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_rbTaxaExpresso100ActionPerformed
    {//GEN-HEADEREND:event_rbTaxaExpresso100ActionPerformed
        // TODO add your handling code here:
        procedimetoAdicionarUrgencia();
    }//GEN-LAST:event_rbTaxaExpresso100ActionPerformed

    private void rbTaxaUrgente50ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_rbTaxaUrgente50ActionPerformed
    {//GEN-HEADEREND:event_rbTaxaUrgente50ActionPerformed
        // TODO add your handling code here:
        procedimetoAdicionarUrgencia();
    }//GEN-LAST:event_rbTaxaUrgente50ActionPerformed

    private void txtQtdItensActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtQtdItensActionPerformed
    {//GEN-HEADEREND:event_txtQtdItensActionPerformed
        // TODO add your handling code here:

        if (table.getSelectedRow() > -1) {
            adicionarLaguraAltura();
            try {
                BigDecimal decimal = new BigDecimal(lbTotalQtd.getText()).setScale(2, BigDecimal.ROUND_UP);
                double qtdTotal = decimal.doubleValue();
                actualizarQtdTable(qtdTotal);

//                txtLargura.setEnabled( true );
//                txtAltura.setEnabled( false );
//                txtQtdItens.setEnabled( false );
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            JOptionPane.showMessageDialog(null, "Seleccione um serviço a aplicar!", "Aviso", JOptionPane.WARNING_MESSAGE);
        }


    }//GEN-LAST:event_txtQtdItensActionPerformed

    private void txtLarguraActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtLarguraActionPerformed
    {//GEN-HEADEREND:event_txtLarguraActionPerformed
        // TODO add your handling code here:
        adicionarLaguraAltura();
        txtLargura.setEnabled(true);
        txtAltura.setEnabled(true);
        txtQtdItens.setEnabled(true);
        txtAltura.requestFocus();

    }//GEN-LAST:event_txtLarguraActionPerformed

    private void txtAlturaActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtAlturaActionPerformed
    {//GEN-HEADEREND:event_txtAlturaActionPerformed
        // TODO add your handling code here:
        adicionarLaguraAltura();
        txtQtdItens.setEnabled(true);
        txtQtdItens.requestFocus();
    }//GEN-LAST:event_txtAlturaActionPerformed

    private void btnLavarActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnLavarActionPerformed
    {//GEN-HEADEREND:event_btnLavarActionPerformed
        // TODO add your handling code here:
        btnLavar.setEnabled(false);
        btnEngomar.setEnabled(true);
        adicionar_centro_botao(cod_categoria);

    }//GEN-LAST:event_btnLavarActionPerformed

    private void btnEngomarActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnEngomarActionPerformed
    {//GEN-HEADEREND:event_btnEngomarActionPerformed
        // TODO add your handling code here:
        btnEngomar.setEnabled(false);
        btnLavar.setEnabled(true);
        adicionar_centro_botao(cod_categoria);

    }//GEN-LAST:event_btnEngomarActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton6ActionPerformed
    {//GEN-HEADEREND:event_jButton6ActionPerformed
        adicionar_tabela();
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jtable_reimpressaoMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_jtable_reimpressaoMouseClicked
    {//GEN-HEADEREND:event_jtable_reimpressaoMouseClicked
        if (evt.getClickCount() > 1) {

            reimprimir_FR();

        }
    }//GEN-LAST:event_jtable_reimpressaoMouseClicked

    private void btFTActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btFTActionPerformed
    {//GEN-HEADEREND:event_btFTActionPerformed

        if (validar()) {
            if (MetodosUtil.licencaValidada(conexao)) {
                procedimento_converter_factura_FT();
                dispose();
                //                procedimento_salvar();
            }

        }
    }//GEN-LAST:event_btFTActionPerformed

    public boolean validar() {
        if (cmbCliente.getSelectedItem().equals("--Seleccione o Cliente--")) {

            JOptionPane.showMessageDialog(null, "Por favor, Seleccione ou Digite o nome do Cliente!");
            txtIniciaisCliente.requestFocus();
            txtIniciaisCliente.setBackground(Color.YELLOW);
            return false;

        }

        txtIniciaisCliente.setBackground(Color.WHITE);
        return true;
    }

    public static void procedimento_converter_factura_FT() {

        if (true) {
            conexaoTransaction = BDConexao.getInstancia();
            DocumentosController.start(conexaoTransaction);
            try {
                TbVenda salvar_venda = salvar_venda();

                if (salvar_venda != null) {
                    System.err.println("VENDA AQUI " + salvar_venda.getCodigo());
    //                registrar_forma_pagamento( salvar_venda.getCodigo() );
                    salvarItemvenda(salvar_venda);
                    remover_dados_tabela();

                } else {
                    System.err.println("  erro ao salvar venda!!!");
                }
            } catch (RuntimeException e) {
                // nada fica a meio: sem isto a venda incompleta era confirmada no commit seguinte
                e.printStackTrace();
                DocumentosController.rollback(conexaoTransaction);
                JOptionPane.showMessageDialog(null, "O documento não foi emitido:\n" + e, "Recolha", JOptionPane.ERROR_MESSAGE);
            }
        }

    }

    private void BtnProformaActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_BtnProformaActionPerformed
    {//GEN-HEADEREND:event_BtnProformaActionPerformed

        try {

            if (MetodosUtil.licencaValidada(conexao)) {

                procedimento_processar_factura_proforma();
                dispose();

            }

        } catch (Exception e) {
        }
    }//GEN-LAST:event_BtnProformaActionPerformed

    private void actualizar_moeda() {
        CfMethods.MOEDA = getMoeda().getAbreviacao();
    }

    private static void actualizar_moeda(String moeda) {
        CfMethods.MOEDA = moeda;
    }

    private void setDocpadrao(String documentos) {
        System.out.println("DOCUMENTO PADRAO: " + documentos);
        if (documentos.equalsIgnoreCase("Factura/Recibo")) {
            cmbTipoDocumento.setSelectedIndex(1);

        } else if (documentos.equalsIgnoreCase("Factura")) {
            cmbTipoDocumento.setSelectedIndex(2);

        } else if (documentos.equalsIgnoreCase("Factura-Proforma")) {
            cmbTipoDocumento.setSelectedIndex(3);

        }
    }

    private void actualizar_abreviacao() {

        switch (getIdDocumento()) {
            case DVML.DOC_FACTURA_RECIBO_FR:

//                if ( ck_simplificada.isSelected() )
//                {
//                    this.abreviacao = Abreviacao.FR_A6;
//                }
//                else if ( ck_S_A6.isSelected() )
//                {
//                    this.abreviacao = Abreviacao.FR_S_A6;
//                }
//                else if ( ck_ComVirgula.isSelected() )
//                {
//                    this.abreviacao = Abreviacao.FR_A6_Com_Virgula;
//                }
                break;
            case DVML.DOC_FACTURA_FT:
                this.abreviacao = Abreviacao.FA;
                break;
            case DVML.DOC_FACTURA_CONSULTA_MESA:
                this.abreviacao = Abreviacao.CM;
                break;
            case DVML.DOC_FACTURA_PROFORMA_PP:
                this.abreviacao = Abreviacao.PP;
                break;
            default:
                break;
        }

    }

    public int getCodigoTipoProduto() throws SQLException {
        return 0;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Skin".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(RecolhaPedidosVisao.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(RecolhaPedidosVisao.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(RecolhaPedidosVisao.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(RecolhaPedidosVisao.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(()
                -> {
            //                procedimentoImprimirTicket( 780, BDConexao.getInstancia() );
            new RecolhaPedidosVisao("MESA 16", "LUGAR 1", 15, 1, BDConexao.getInstancia()).setVisible(true);
        });
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private static javax.swing.JButton BT_Conversao;
    private javax.swing.JButton BtnProforma;
    private javax.swing.JButton btCliente;
    private javax.swing.JButton btFT;
    private static javax.swing.JButton btnEngomar;
    private static javax.swing.JButton btnLavar;
    private javax.swing.JButton btn_voltar;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.ButtonGroup buttonGroup3;
    private javax.swing.ButtonGroup buttonGroup4;
    private static javax.swing.JComboBox<String> cmbAnoEconomico;
    private static javax.swing.JComboBox<String> cmbArmazem;
    public static javax.swing.JComboBox<String> cmbCliente;
    public static javax.swing.JComboBox cmbMoeda;
    public static javax.swing.JComboBox cmbSeries;
    public static javax.swing.JComboBox cmbTipoDocumento;
    public static javax.swing.JComboBox<String> cmb_area_venda_restaurante;
    private com.toedter.calendar.JDateChooser dcDataFim;
    private com.toedter.calendar.JDateChooser dcDataInicio;
    private static com.toedter.calendar.JDateChooser dc_data_documento;
    private static com.toedter.calendar.JDateChooser dc_data_entrega;
    private javax.swing.JLabel designacao_categoria;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JLabel jlEmpresa;
    private javax.swing.JLabel jlUsuario;
    private javax.swing.JTable jtable_reimpressao;
    private javax.swing.JLabel lbClienteConsumidorFinal1;
    private javax.swing.JLabel lbClienteConsumidorFinal2;
    private static javax.swing.JLabel lbEmailCliente;
    private javax.swing.JLabel lbPreco4;
    private static javax.swing.JLabel lbQuantidadeExistente;
    private javax.swing.JLabel lbServico;
    private static javax.swing.JLabel lbSomaLarguraAltura;
    private static javax.swing.JLabel lbTelefoneCliente;
    private static javax.swing.JLabel lbTotalQtd;
    public static javax.swing.JLabel lbValorPorExtenco;
    private static javax.swing.JLabel lb_proximo_documento;
    private static javax.swing.JRadioButton rbArmazem;
    private static javax.swing.JRadioButton rbArmazem1;
    private javax.swing.JRadioButton rbTaxaExpresso100;
    private javax.swing.JRadioButton rbTaxaUrgente50;
    public static javax.swing.JLabel status_mensagem_primaria;
    public static javax.swing.JLabel status_mensagem_secundaria;
    private static javax.swing.JTable table;
    private static javax.swing.JTextField txtAltura;
    private static javax.swing.JTextField txtIniciaisCliente;
    private static javax.swing.JTextField txtLargura;
    private static javax.swing.JTextArea txtObs;
    private static javax.swing.JTextField txtQtdItens;
    private static javax.swing.JTextField txtQuatidadeExistente;
    private static javax.swing.JTextField txtTelefoneCliente;
    public static javax.swing.JTextField txtTotalApagar;
    public static javax.swing.JTextField txtTotalQTD;
    // End of variables declaration//GEN-END:variables

    // totais e desconto do cliente (mostrados no painel de totais do layout web)
    public static final javax.swing.JTextField txtTotalGeral = new javax.swing.JTextField( "0,00" );
    public static final javax.swing.JTextField txtDesconto = new javax.swing.JTextField( "0,00" );
    private final javax.swing.JLabel lbDesconto = new javax.swing.JLabel();

    private void adicionar_lugares() {

        try {
            List<TbLugares> itens = lugaresController.listarTodos();
            lista_lugares.clear();
            for (int i = 0; i < itens.size(); i++) {
                lista_lugares.addElement(itens.get(i).getDesignacao());
            }
        } catch (Exception e) {
        }
    }

//    public static void procedimento_salvar_pedidos_iten_pedidos( String designacao_produto )
//    {
//        BDConexao conexaoTransactionLocal = BDConexao.getInstancia();
//        DocumentosController.start( conexaoTransactionLocal );
//        try
//        {
//
//            int codigoProduto = produtoDao.getIdByDescricao( designacao_produto );
//            TbProduto produto = produtoDao.findTbProduto( codigoProduto );
//
//            // =============================
//            // STOCK VISUAL (UI)
//            // =============================
//            if ( "true".equals( produto.getStocavel() )
//                    && produto.getCodTipoProduto().getFkFamilia().getPkFamilia() == DVML.COD_PRODUTO )
//            {
//                lbQuantidadeExistente.setVisible( true );
//                txtQuatidadeExistente.setVisible( true );
//                txtQuatidadeExistente.setText(
//                        String.valueOf( conexao.getQtdExistenteStock( codigoProduto, getCodigoArmazem() ) )
//                );
//            }
//            else
//            {
//                lbQuantidadeExistente.setVisible( false );
//                txtQuatidadeExistente.setVisible( false );
//                txtQuatidadeExistente.setText( "" );
//            }
//
//            // =============================
//            // BUSCAR PEDIDO ATIVO
//            // =============================
//            int codPedido = pedidoDao.getLastPedidoByDefignacaoMesaFALSE( mesa, conexao );
//
    ////            if ( codPedido == 0 )
////            {
////                throw new IllegalStateException( "Nenhum pedido aberto para esta mesa" );
////            }
//
//            TbPedido pedido = pedidoDao.findTbPedido( codPedido );
//
//            // =============================
//            // CRIAR ITEM
//            // =============================
//            TbItemPedidos item = new TbItemPedidos();
//            item.setFkPedidos( pedido );
//            item.setFkProdutos( produto );
//            item.setFkLugares( (TbLugares) lugaresController.findByLugar( lugar ) );
//            item.setQtd( 1 );
//            item.setObs( "" );
//            item.setDataEntrega( new Date() );
//            item.setStatusConvertido( false );
//            item.setStatusEnviado( true );
//            item.setStatusEfectuado( false );
//
//            BigDecimal precoVenda = precoDao
//                    .findTbPreco( precoDao.getUltimoIdPrecoByIdProduto( produto.getCodigo() ) )
//                    .getPrecoVenda();
//
//            item.setPreco( precoVenda.doubleValue() );
//            item.setTotalItem( precoVenda.multiply( BigDecimal.valueOf( item.getQtd() ) ).doubleValue() );
//
//            // =============================
//            // VALIDAÇÕES DE STOCK
//            // =============================
//            if ( !possivel_quantidade( produto.getCodigo() ) )
//            {
//                throw new IllegalStateException(
//                        "Quantidade insuficiente para o produto: " + designacao_produto
//                );
//            }
//
////            if ( produto.getCodTipoProduto().getFkFamilia().getPkFamilia() == DVML.COD_PRODUTO )
////            {
//////                if ( estado_critico( produto.getCodigo() ) )
//////                {
//////                    JOptionPane.showMessageDialog(
//////                            null,
//////                            "O produto: " + designacao_produto + " está em estado crítico de stock",
//////                            "AVISO",
//////                            JOptionPane.WARNING_MESSAGE
//////                    );
//////                }
////            }
//
//            // =============================
//            // GRAVAR ITEM (DAO)
//            // =============================
//            Integer idItem = itemPedidosDao.criarComProcedimentoLav( item, conexao );
//
//            if ( idItem == null )
//            {
//                throw new SQLException( "Falha ao inserir ItemPedido" );
//            }
//
//            // =============================
//            // COMMIT
//            // =============================
//            DocumentosController.commit( conexaoTransactionLocal );
//
//            actualizar(); // UI
//
//        }
//        catch ( Exception e )
//        {
//            try
//            {
//                DocumentosController.rollback( conexaoTransactionLocal );
//            }
//            catch ( Exception ex )
//            {
//                ex.printStackTrace();
//            }
//
//            JOptionPane.showMessageDialog(
//                    null,
//                    e.getMessage(),
//                    "Erro ao salvar item do pedido",
//                    JOptionPane.ERROR_MESSAGE
//            );
//        }
//    }
    public static void procedimento_salvar_pedidos_iten_pedidos(String designacao_produto) {
        BDConexao conexaoTransactionLocal = BDConexao.getInstancia();
        DocumentosController.start(conexaoTransactionLocal);

        try {

            /* MOSTRA A QUANTIDADE NO STOCK */
            int codigo_produto = produtoDao.getIdByDescricao(designacao_produto);

            TbProduto produto_local = produtoDao.findTbProduto(codigo_produto);
            if (produto_local.getStocavel().equals("true") && produto_local.getCodTipoProduto().getFkFamilia().getPkFamilia() == DVML.COD_PRODUTO) {
                lbQuantidadeExistente.setVisible(true);
                txtQuatidadeExistente.setVisible(true);

//            txtQuatidadeExistente.setText( String.valueOf( stockDao.get_stock_by_id_produto_and_id_armazem( getCodigoProduto(), getCodigoArmazem() ).getQuantidadeExistente() ) );
                txtQuatidadeExistente.setText(String.valueOf(conexao.getQtdExistenteStock(codigo_produto, getCodigoArmazem())));
            } else {
                lbQuantidadeExistente.setVisible(false);
                txtQuatidadeExistente.setVisible(false);
                txtQuatidadeExistente.setText("");
            }

            TbItemPedidos itemPedidosLocal = new TbItemPedidos();
            int cod_pedido = pedidoDao.getLastPedidoByDefignacaoMesaFALSE(mesa, conexaoTransactionLocal
            );

//            itemPedidosLocal.setFkLugares(
//                    (TbLugares) lugaresController.findByLugar( lugar )
//            );
            System.err.println("$$$$CODIGO PEDIDO ACTUAL: " + cod_pedido);
            pedido = pedidoDao.findTbPedido(cod_pedido);
            itemPedidosLocal.setFkLugares((TbLugares) lugaresController.findByLugar(lugar));
            itemPedidosLocal.setFkProdutos(produtoDao.findTbProduto(produtoDao.getIdByDescricao(designacao_produto)));
            itemPedidosLocal.setQtd(1);
            itemPedidosLocal.setObs("");
            itemPedidosLocal.setDataEntrega(dataEntregaEscolhida()); // a data do campo "Data de entrega" (por omissão, daqui a 2 dias)
            itemPedidosLocal.setStatusConvertido(false);
            /*Envia para a área da cozinha*/
            itemPedidosLocal.setStatusEnviado(true);
            //para saber se o prato ja foi feito 
            itemPedidosLocal.setStatusEfectuado(false);

            double preco = precoDao.findTbPreco(precoDao.getUltimoIdPrecoByIdProduto(itemPedidosLocal.getFkProdutos().getCodigo())).getPrecoVenda().doubleValue();
            itemPedidosLocal.setPreco(preco);
            double total = itemPedidosLocal.getQtd() * precoDao.findTbPreco(precoDao.getUltimoIdPrecoByIdProduto(itemPedidosLocal.getFkProdutos().getCodigo())).getPrecoVenda().doubleValue();
            itemPedidosLocal.setTotalItem(total);
            itemPedidosLocal.setFkPedidos(pedido);

            try {
                if (possivel_quantidade(itemPedidosLocal.getFkProdutos().getCodigo())) {

                    // if( (conexao.getQtdSolicitados(  itemPedidos.getFkProdutos().getCodigo() ) + 1 ) <=  conexao.getQtdExistenteStock(codigo_produto, 1)  ){
                    if (produto_local.getCodTipoProduto().getFkFamilia().getPkFamilia() == DVML.COD_PRODUTO) {
                        if (estado_critico(itemPedidosLocal.getFkProdutos().getCodigo())) {
                            JOptionPane.showMessageDialog(null, "O produto: " + designacao_produto + " precisa de ser actualizado no stock", "AVISO", JOptionPane.WARNING_MESSAGE);
                        }

                    }

//#ped2z
                    Integer idLastItemPedido = itemPedidosDao.criarComProcedimentoLav(itemPedidosLocal, conexaoTransactionLocal);

                    if (idLastItemPedido != null) {
                        //##PINTAR
                        actualizar();

                    } else {
                        System.err.println("ERRO AO INSERIR O ITEM ...");
                    }

                } else {
                    JOptionPane.showMessageDialog(null, "O Produto " + designacao_produto + " não pode ser vendido para esta quantidade.", "AVISO", JOptionPane.WARNING_MESSAGE);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            // fecha a transacção aberta no início (antes ficava aberta e a ligação partilhada em modo manual)
            DocumentosController.commit(conexaoTransactionLocal);

        } catch (Exception e) {
            e.printStackTrace();
            DocumentosController.rollback(conexaoTransactionLocal);
        }
    }

    public static boolean possivel_quantidade(int codigo_produto) {

        TbProduto produtoLocal = (TbProduto) produtosController.findById(codigo_produto);
        if (true) //            ERRORR
        //        if ( produto.getCodTipoProduto().getFkFamilia().getPkFamilia() == DVML.COD_SERVICO )
        {
            return true;
        }

        System.err.println(conexao.getQuantidade_Existente_Publico(codigo_produto, id_armzem));
        System.err.println("ID ARMAZEM : " + id_armzem);
        System.err.println("ID ARMAZEM : " + getCodigoArmazem());
        double quant_possivel = 0d;
        try {
            quant_possivel = (conexao.getQuantidade_Existente_Publico(codigo_produto, id_armzem) - conexao.getQuantidade_minima_publico(codigo_produto, id_armzem));
        } catch (Exception e) {
            e.printStackTrace();
        }

        // int quant_possivel = stock.getQuantidadeExistente() -  stock.getQuantBaixa();
        System.out.println("Quantidade Possivel: " + quant_possivel);
        System.out.println("Quantidade Itens Pedidos: " + MetodosUtil.getQtdInItemPedidos(conexao, codigo_produto));
        return quant_possivel >= (1 + MetodosUtil.getQtdInItemPedidos(conexao, codigo_produto));

    }

    public static boolean possivel_quantidade(int codigo_produto, int qtd) {
        TbProduto produtoLocal = (TbProduto) produtosController.findById(codigo_produto);
        TbTipoProduto tipo = tipoProdutosController.getTipoProdutoByCodigo(produtoLocal.getCodTipoProduto().getCodigo());
//ESTE
        if (tipo.getFkFamilia().getPkFamilia() == DVML.COD_SERVICO) //        if ( true )
        {
            return true;
        }

        System.err.println(conexao.getQuantidade_Existente_Publico(codigo_produto, id_armzem));
        System.err.println("ID ARMAZEM : " + id_armzem);
        System.err.println("ID ARMAZEM : " + getCodigoArmazem());
        double quant_possivel = 0d;
        try {
            quant_possivel = (conexao.getQuantidade_Existente_Publico(codigo_produto, id_armzem) - conexao.getQuantidade_minima_publico(codigo_produto, id_armzem));
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Quantidade Possivel: " + quant_possivel);
        System.out.println("Quantidade Itens Pedidos: " + MetodosUtil.getQtdInItemPedidos(conexao, codigo_produto));
        return quant_possivel >= (qtd + MetodosUtil.getQtdInItemPedidos(conexao, codigo_produto));

    }

    public static void setSalvarPedidos() {

        System.out.println("$$$$$$ ID PEDIDO : " + pedidoDao.getLastPedidoByDefignacaoMesaSemStatus(mesa));
        TbPedido pedido_local;
        //busca o último pedido de uma determinada mesa, senão existe instancia um pedido como 
        if (pedidoDao.getLastPedidoByDefignacaoMesaSemStatus(mesa) == 0) {

            pedido_local = new TbPedido();
            pedido_local.setStatusPedido(true);

        } else {
            pedido_local = pedidoDao.findTbPedido(pedidoDao.getLastPedidoByDefignacaoMesaSemStatus(mesa));
        }

        if (pedido_local.getStatusPedido()) {
            try {

                TbPedido pedido_2 = new TbPedido();
                pedido_2.setDataPedido(new Date());
                pedido_2.setHoraPedido(new Date());
                pedido_2.setStatusPedido(false);
                pedido_2.setFkMesas(mesasDao.findTbMesas(mesasDao.getIdByDescricao(mesa)));
//#ped1                
                Integer idLastPedido = pedidoDao.criarComProcedimentos(pedido_2, conexao);
                System.out.println("$$$$$$ SALVAR NOVO PEDIDO >  " + idLastPedido);

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            //JOptionPane.showMessageDialog(null, "Não Houve Feicho");
        }

    }

    public static void setSalvarPedidoPosVenda(BDConexao conexaoParm) {

//        System.out.println( "ID PEDIDO : " + pedidoDao.getLastPedidoByDefignacaoMesaSemStatus( mesa ) );
//        TbPedido pedido_local;
//        //busca o último pedido de uma determinada mesa, senão existe instancia um pedido como 
//        if ( pedidoDao.getLastPedidoByDefignacaoMesaSemStatus( mesa ) == 0 )
//        {
//
//            pedido_local = new TbPedido();
//            pedido_local.setStatusPedido( true );
//
//        }
//        else
//        {
//            pedido_local = pedidoDao.findTbPedido( pedidoDao.getLastPedidoByDefignacaoMesaSemStatus( mesa ) );
//        }
        if (true) {
            try {

                TbPedido pedido_2 = new TbPedido();
                pedido_2.setDataPedido(new Date());
                pedido_2.setHoraPedido(new Date());
                pedido_2.setStatusPedido(false);
                pedido_2.setFkMesas(mesasDao.findTbMesas(mesasDao.getIdByDescricao(mesa)));

                Integer idLastPedido = pedidoDao.criarComProcedimentos(pedido_2, conexaoParm);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            //JOptionPane.showMessageDialog(null, "Não Houve Feicho");
        }

    }

    public void setSalvarItemPedidos() {
        try {
            TbItemPedidos itemPedidos = new TbItemPedidos();
            int cod_pedido = pedidoDao.getLastPedidoByDefignacaoMesaFALSE(mesa);

            this.pedido = pedidoDao.findTbPedido(cod_pedido);
            itemPedidos.setFkLugares((TbLugares) lugaresController.findByLugar(getDescricaoLugar()));

            itemPedidos.setFkProdutos((TbProduto) produtosController.findByDesignacao(getDescricaoProduto()));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static String getDescricaoLugar() {
        return "";
    }

    private String getDescricaoProduto() {
        //return String.valueOf( cmbProduto.getSelectedItem());    
        return "";
    }

    public static void actualizar() {
        try {
            adicionar_tabela(itemPedidosDao.buscaTodosItemPedidosRecolha(pedidoDao.getLastPedidoByDefignacaoMesaFALSE(mesa)));
            valor_por_extenco();
            scrolltable();
        } catch (Exception e) {
//            System.err.println( e.getMessage() );
//            e.printStackTrace();
        }

    }

    public static void actualizar_lugar(int lugar) {
        try {

            adicionar_tabela_lugar(itemPedidosDao.buscaTodosItemPedidos2(pedidoDao.getLastPedidoByDefignacaoMesaLugarFALSE(lugar)));
            scrolltable();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void adicionar_tabela(List<TbItemPedidos> itemPedidos_list) {
        DefaultTableModel modelo = (DefaultTableModel) table.getModel();
        Double preco = 0d, desconto = 0d, taxa = 0d;
        double qtd = 0;

//        if ( validar_zero() )
//        {
        //limpa dados na jTabel1
//            stock_local = stockDao.get_stock_by_id_produto_and_id_armazem( this.itemPedidos.getFkProdutos().getCodigo(), getCodigoArmazem() );
        //so retirar caso existir mesmo no armazém em questão.
//        if ( stock_local.getCodigo() != 0 && itemPedidos.getFkProdutos().getStocavel().equals( "true" ) )
        modelo.setRowCount(0);

        if (itemPedidos_list != null) {

            //define a altura das linhas
            table.setRowHeight(50);
            try {
                for (int i = 0; i < itemPedidos_list.size(); i++) {
                    qtd = itemPedidos_list.get(i).getQtd();
                    preco = precoDao.findTbPreco(precoDao.getUltimoIdPrecoByIdProduto(itemPedidos_list.get(i).getFkProdutos().getCodigo(), qtd)).getPrecoVenda().doubleValue();
//                    preco = precoDao.findTbPreco( precoDao.getUltimoIdPrecoByIdProduto( itemPedidos_list.get( i ).getFkProdutos().getCodigo(), qtd ) ).getPrecoVenda().doubleValue();
                    System.err.println("QTD ADD: " + qtd);
                    System.err.println("PU  ADD: " + preco);

                    taxa = MetodosUtil.getTaxaPercantagem(itemPedidos_list.get(i).getFkProdutos().getCodigo());
                    TbItemPedidos get = itemPedidos_list.get(i);
                    String obs = get.getObs();
                    String dataEntrega = (!Objects.isNull(get.getDataEntrega())) ? MetodosUtil.getDataBanco(get.getDataEntrega()) : "";
                    System.out.println("DATE ENTREGA: " + MetodosUtil.getDataBanco(get.getDataEntrega()));
                    modelo.addRow(new Object[]{
                        get.getPkItemPedidos(),
                        dataEntrega,
                        get.getFkProdutos().getDesignacao() + "-" + obs,
                        qtd,
                        //                        CfMethods.formatarComoMoeda(MetodosUtil.getValorComIVA( qtd, taxa, preco, desconto ))
                        //                        MetodosUtil.getValorComIVA( qtd, taxa, preco, desconto )
                        CfMethods.formatarComoMoeda(FinanceUtils.getValorComIVA(qtd, taxa, preco, desconto)) //itemPedidos.get( i ).getQtd() * precoDao.findTbPreco( precoDao.getUltimoIdPrecoByIdProduto( itemPedidos.get( i ).getFkProdutos().getCodigo() ) ).getPrecoVenda()
                    }
                    );
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            setTotalPagar();
            setTotalQTD();
            valor_por_extenco();
            txtObs.setText("");
            txtObs.requestFocus();

        }
//        }
//        else
//        {
//            JOptionPane.showMessageDialog( null, "Atenção\nA quantidade a sair não pode ser igual a zero!" );
//        }

    }

    /** Percentagem de desconto da ficha do cliente seleccionado (0 se não tiver). */
    public static double getPercentagemDescontoCliente() {
        try {
            return getDescontoClientePercentagem();
        } catch (Exception e) {
            return 0;
        }
    }

    private static double getDescontoClientePercentagem() {
        TbCliente clientes = clientesController.findByCodigo(getIdCliente());

        if (Objects.nonNull(clientes)
                && clientes.getPercentagemDesconto() > 0) {
            return clientes.getPercentagemDesconto();
        }

        return 0.0;
    }

    public static void adicionar_tabela_lugar(List<TbItemPedidos> itemPedidos_list) {
        DefaultTableModel modelo = (DefaultTableModel) table.getModel();
        Double preco = 0d, desconto = 0d, taxa = 0d;
        double qtd = 0;

//        if ( validar_zero() )
//        {
        //limpa dados na jTabel1
//            stock_local = stockDao.get_stock_by_id_produto_and_id_armazem( this.itemPedidos.getFkProdutos().getCodigo(), getCodigoArmazem() );
        //so retirar caso existir mesmo no armazém em questão.
//        if ( stock_local.getCodigo() != 0 && itemPedidos.getFkProdutos().getStocavel().equals( "true" ) )
        modelo.setRowCount(0);

        if (itemPedidos_list != null) {

            //define a altura das linhas
            table.setRowHeight(50);
            try {
                for (int i = 0; i < itemPedidos_list.size(); i++) {

                    qtd = itemPedidos_list.get(i).getQtd();

                    preco = precoDao.findTbPreco(precoDao.getUltimoIdPrecoByIdProduto(itemPedidos_list.get(i).getFkProdutos().getCodigo(), qtd)).getPrecoVenda().doubleValue();
                    System.err.println("QTD ADD: " + qtd);
                    System.err.println("PU  ADD: " + preco);

                    taxa = MetodosUtil.getTaxaPercantagem(itemPedidos_list.get(i).getFkProdutos().getCodigo());
                    modelo.addRow(new Object[]{
                        itemPedidos_list.get(i).getPkItemPedidos(),
                        itemPedidos_list.get(i).getFkLugares().getDesignacao(),
                        itemPedidos_list.get(i).getFkProdutos().getDesignacao(),
                        qtd,
                        //                        CfMethods.formatarComoMoeda(MetodosUtil.getValorComIVA( qtd, taxa, preco, desconto ))
                        //                        MetodosUtil.getValorComIVA( qtd, taxa, preco, desconto )
                        CfMethods.formatarComoMoeda(FinanceUtils.getValorComIVA(qtd, taxa, preco, desconto))
                    //itemPedidos.get( i ).getQtd() * precoDao.findTbPreco( precoDao.getUltimoIdPrecoByIdProduto( itemPedidos.get( i ).getFkProdutos().getCodigo() ) ).getPrecoVenda()
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            setTotalPagar();
            valor_por_extenco();
        }
//        }
//        else
//        {
//            JOptionPane.showMessageDialog( null, "Atenção\nA quantidade a sair não pode ser igual a zero!" );
//        }

    }

    public boolean validar_zero() {
        if (Double.parseDouble(txtQuatidadeExistente.getText()) == 0.0) {
            JOptionPane.showMessageDialog(null, "Atenção\nA quantidade a sair não pode ser igual a zero!");

            return false;
        }
        return true;
    }

    private Date getDataPedido() {
        return new Date(Integer.parseInt(String.valueOf(new Date().getYear())) - 1900, new Date().getMonth(), new Date().getDay());
    }

    public int getLastCodigo(String tabela) {

        String sql = "SELECT max(pk_pedido) FROM " + tabela;
        ResultSet rs = conexao.executeQuery(sql);
        try {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;

    }

    public void imprimirPedidos() {

        try {
            TbPedido pedido = pedidoDao.findTbPedido(pedidoDao.getLastPedidoByDefignacaoMesaFALSE(mesa));
            //pedido.setStatusPedido(true);
            //pedidoDao.edit(pedido);

            ListaPedidos listaPedidos = new ListaPedidos(pedido.getPkPedido());
            //eliminar_toda_tabela(jTable1);
            //eliminar_toda_tabela(jTable1);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void imprimirPedidosPorLugar(int opcao) {

        try {
            TbPedido pedido = pedidoDao.findTbPedido(pedidoDao.getLastPedidoByDefignacaoMesaFALSE(mesa));
            //TbPedido pedido = pedidoDao.findTbPedido(   pedidoDao.getLastPedidoByDefignacaoMesaFALSE(mesa)     );
            //pedido.setStatusPedido(true);
            pedidoDao.edit(pedido);
            ListaPedidos listaPedidos = new ListaPedidos(pedido.getPkPedido(), opcao);
            //eliminar_toda_tabela(jTable1);
            //eliminar_toda_tabela(jTable1);

        } catch (Exception e) {
        }
    }

//    //----------- evento do teclado ---------------------------------------
//    public class TratarTroco implements KeyListener
//    {
//
//        String prefixo = "";
//
//        public void keyPressed( KeyEvent evt )
//        {
//
//            String prefixo = "";
//
//            if ( evt.getKeyCode() != KeyEvent.VK_BACK_SPACE )
//            {
//                char key = evt.getKeyChar();
//                prefixo = txtValorEntregue.getText().trim() + key;
//                double troco = 0;
//                double valor_entregue = Double.parseDouble( prefixo );
//                double total_pagar = Double.parseDouble( txtTotalApagar.getText().trim() );
//                troco = valor_entregue - total_pagar;
//                txtTroco.setText( String.valueOf( troco ) );
//
//            }
//            else if ( evt.getKeyCode() == KeyEvent.VK_BACK_SPACE )
//            {
//                char key = evt.getKeyChar();
//
//                prefixo = txtValorEntregue.getText() + key;
//
//                double valor_entregue = 0;
//                double troco = 0;
//                double total_pagar = Double.parseDouble( txtTotalApagar.getText().trim() );
//
//                try
//                {
//                    valor_entregue = Double.parseDouble( prefixo.trim().substring( 0, prefixo.length() - 2 ) );
//                    total_pagar = Double.parseDouble( txtTotalApagar.getText().trim() );
//                    troco = valor_entregue - total_pagar;
//                }
//                catch ( Exception e )
//                {
//                    valor_entregue = 0;
//                }
//
//                troco = valor_entregue - total_pagar;
//                txtTroco.setText( String.valueOf( troco ) );
//
//            }
//
//        }
//
//        public void keyReleased( KeyEvent evt )
//        {
//        }
//
//        public void keyTyped( KeyEvent evt )
//        {
//        }
//
//    }
    private void eliminar() {

        int opcao = JOptionPane.showConfirmDialog(null, "Tens a certeza que queres mesmo eliminar esse pedido\n Uma vez eliminado todas as tabelas relacionadas com o mesmo deixaram de existir", "AVISO", JOptionPane.WARNING_MESSAGE);

        System.out.println("OPCAO " + opcao);
        if (opcao == 0) {

            try {

                itemPedidos = itemPedidosDao.findTbItemPedidos(idpedido);
                this.itemPedidos.getFkPedidos().setStatusPedido(false);
                itemPedidosDao.destroy(idpedido);
                // limpar();
                adicionar_tabela(itemPedidosDao.getAllPedidosNovos());
                JOptionPane.showMessageDialog(null, "Item eliminado com sucesso!...");

            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Erro ao eliminar o Item", "ERRO", JOptionPane.ERROR_MESSAGE);
            }

        } else {
            JOptionPane.showMessageDialog(null, "O Item não foi eliminado");
        }

    }

    public static void eliminar_item(JTable tabela) {

        DefaultTableModel modelo = (DefaultTableModel) tabela.getModel();
        int id_item_pedido = Integer.parseInt(modelo.getValueAt(tabela.getSelectedRow(), 0).toString());
        TbItemPedidos itemPedidosLocal = itemPedidosDao.findTbItemPedidos(id_item_pedido);

        int idPedido = itemPedidosLocal.getPkItemPedidos();

        TbLugares lugarEntity = (TbLugares) lugaresController.findById(itemPedidosLocal.getFkLugares().getPkLugares());
        String lugarLocal = lugarEntity.getDesignacao();
        TbUsuario usuarioEntity = (TbUsuario) usuariosController.findById(idUser);
        String usuario = usuarioEntity.getNome();

        try {

//            MetodosUtil.imprimir_cozinha( itemPedidosLocal.getFkProdutos(),
//                    idPedido,
//                    mesa,
//                    lugarLocal,
//                    usuario,
//                    "Cancelado",
//                    itemPedidosLocal.getQtd(),
//                    dadosInstituicaoController );
            itemPedidosDao.destroy(id_item_pedido);
            actualizar();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro:\n Não foi eliminado o Item");
        }

    }

    public static void salvar_item_cancelado(JTable tabela) {

        DefaultTableModel modelo = (DefaultTableModel) tabela.getModel();
        int id_item_pedido = Integer.parseInt(modelo.getValueAt(tabela.getSelectedRow(), 0).toString());

        try {

            itemPedidosDao.destroy(id_item_pedido);
            actualizar();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro:\n Não foi eliminado o Item");
        }

    }

    private void actualizar_campo(int qtd) {

        DefaultTableModel modelo = (DefaultTableModel) table.getModel();
        TbItemPedidos itemPedidos = itemPedidosDao.findTbItemPedidos(Integer.parseInt(table.getValueAt(this.linha_actual, 0).toString()));
        itemPedidos.setQtd(qtd);

        try {
            itemPedidosDao.edit(itemPedidos);
            actualizar();
        } catch (Exception e) {
        }

    }

    public static void gravar_item_eliminado(JTable tabela) {
//        linha_acutal = jTable1.getSelectedRow();
        DefaultTableModel modelo = (DefaultTableModel) tabela.getModel();
        TbItemPedidos itemPedidos = itemPedidosDao.findTbItemPedidos(Integer.parseInt(table.getValueAt(linha_actual, 0).toString()));
//        int id_item_pedido = Integer.parseInt( modelo.getValueAt( tabela.getSelectedRow(), 0 ).toString() );

        try {
            itemPedidosDao.create(itemPedidos);
//            actualizar();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro:\n Não foi salvo o Item");
        }

    }

    private void eliminar_toda_tabela(JTable tabela) {

        try {
            DefaultTableModel modelo = (DefaultTableModel) tabela.getModel();
            int filas = tabela.getRowCount();

            for (int i = 0; filas > i; i++) {

                modelo.removeRow(0);
                actualizar();
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro:\n Não foi eliminado o Item");
        }

    }

    public int getCodigoCliente() {
        return 1;
    }

    public static TbVenda salvar_venda() {

        Date data_documento = new Date();
//        Date data_documento = dc_data_documento.getDate();
        TbVenda venda_local = new TbVenda();
        venda_local.setDataVenda(data_documento);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(data_documento);
        //adicionar 15 dias na data do documento.
        calendar.add(Calendar.DATE, 15);
//        venda_local.setDataVencimento( calendar.getTime() );
        venda_local.setDataVencimento(dc_data_entrega.getDate());
        venda_local.setHora(data_documento);

        venda_local.setNomeCliente(getNomeCliente());
        venda_local.setNomeConsumidorFinal(cmbCliente.getSelectedItem().toString());

        venda_local.setClienteNif(getClienteNif());
        venda_local.setCodigoCliente(clientesController.findByCodigo(getIdCliente()));

        //Total Ilíquido
        venda_local.setTotalGeral(new BigDecimal(getTotalIliquido()));
        //desconto por linha
        venda_local.setDescontoComercial(new BigDecimal(getDescontoComercial()));
        //imposto
        //calculaTotalIVA();
        venda_local.setTotalIva(new BigDecimal(getTotalImposto()));
        //desconto global
        venda_local.setDescontoFinanceiro(new BigDecimal(getDescontoFinanceiro()));
        //Total(AOA) <=> Total Líquido
        venda_local.setTotalVenda(new BigDecimal(getTotalAOALiquido()));
        venda_local.setValorEntregue(new BigDecimal(getValor_entregue()));
        venda_local.setTroco(new BigDecimal(getTroco()));
        venda_local.setTotalIncidencia(new BigDecimal(getTotalIncidencia()));
        venda_local.setTotalIncidenciaIsento(new BigDecimal(getTotalIncidenciaIsento()));
//        venda_local.setTotalRetencao( getTotalRetencao() );
        /*outros campos*/
        venda_local.setDescontoTotal(new BigDecimal(getDescontoComercial() + getDescontoFinanceiro()));
//        venda_local.setIdBanco( bancoDao.findTbBanco( getIdBanco() ) );
//        venda_local.setIdArmazemFK( armazemDao.findTbArmazem( id_armzem ) );
//        venda_local.setCodigoUsuario( usuarioDao.findTbUsuario( idUser ) );
        venda_local.setIdArmazemFK(new TbArmazem(id_armzem));
        venda_local.setCodigoUsuario(new TbUsuario(idUser));
//        venda_local.setTroco( getTroco() );
//        venda_local.setFkAnoEconomico( anoEconomicoDao.getLastObject() );
        venda_local.setFkAnoEconomico(anoEconomico);
        venda_local.setFkDocumento(documento);
//        venda_local.setCodFact( prox_doc );
        venda_local.setCodFact(getCodDocActualizador());
        venda_local.setRefDataFact(data_documento);
//        venda_local.setCodFact ( getCodDocActualizador() );
        //venda_local.setHashCod( MetodosUtil.criptografia_hash( venda_local, getGrossTotal(), conexao ) );
//        venda_local.setHashCod( MetodosUtil.criptografia_hash( prox_doc ) );
        venda_local.setTotalPorExtenso(iniciais_extenso() + extensoTotalPagar());
//        venda_local.setTotalPorExtenso( MetodosUtil.iniciais_extenso( DOC_FACTURA_RECIBO_FR, documentoDao ) + MetodosUtil.valorPorExtenso( venda_local.getTotalVenda().doubleValue(), "Kwanza" ) );
        System.out.println("STATUS:hash cod processado.");
        venda_local.setHashCod(MetodosUtil.criptografia_hash(venda_local, getGrossTotal(), conexao));
        venda_local.setAssinatura(MetodosUtil.assinatura_doc(venda_local.getHashCod()));
//        venda_local.setAreaVenda( String.valueOf( cmb_area_venda_restaurante.getSelectedItem() ) );
//        venda_local.setQuarto( "" );
//        venda_local.setRefDataFact( CfMethods.fullDateToText( venda_local.getDataVenda() ) );
        //System.out.println( "STATUS:documento assinado com sucesso." );
        venda_local.setFkCambio(cambiosController.findByCodigo(ID_CAMBIO_NACIONAL));
//        venda_local.setFkCambio( cambioDao.findCambio( ID_CAMBIO_NACIONAL ) );

        /*status documento*/
        venda_local.setStatusEliminado("false");
        venda_local.setPerformance("false");
        venda_local.setCredito("false");
        venda_local.setGorjeta(new BigDecimal(gorjeta));

        Integer last_venda = 0;

        try {

            if (vendasController.salvar(venda_local)) {
                last_venda = vendasController.getLastVenda().getCodigo();

                if (Objects.isNull(last_venda) || last_venda == 0) {
                    DocumentosController.rollback(conexaoTransaction);
                    conexaoTransaction.close();
                    return venda_local;
                }
                System.err.println("last_venda: " + last_venda);
                System.out.println("STATUS:factura criada com sucesso.");

                if (last_venda != null) {
                    if (getIdDocumento() == DOC_FACTURA_RECIBO_FR) {
//                        MetodosUtil.adicionar_saldo_banco( venda_local.getTotalVenda().doubleValue(), venda_local.get.getIdBanco(), conexao );
                    }
//                    salvar_item_venda_comercial( last_venda );
                }
            } else {
                System.out.println("ERROR: Já existe venda relacionada.");
            }

        } catch (Exception e) {
            System.err.println("STATUS: falha ao actualizar a factura");
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Falha ao Processar a Factura", "FALHA", JOptionPane.ERROR_MESSAGE);
            DocumentosController.rollback(conexao);
//            conexaoTransaction.close();
        }
//        return vendaDao.findTbVenda( last_venda );
        return new TbVenda(last_venda);

    }

    public static int getIdCliente() {
        try {
            if (cmbCliente.getSelectedItem() == null) {
                return 0;
            }

            String nomeCliente = cmbCliente.getSelectedItem().toString().trim();

            if (nomeCliente.isEmpty()) {
                return 0;
            }

            TbCliente cliente = clientesController.getClienteByNome(nomeCliente);

            if (cliente == null) {
                return 0;
            }

            return cliente.getCodigo();
        } catch (Exception e) {
            // logger.error("Erro ao obter ID do cliente", e);
            return 0;
        }
    }

    private static double getTotalRetencao() {
//        DefaultTableModel modelo = ( DefaultTableModel ) jTable1.getModel();
        double imposto = 0d;
        return imposto;
    }

    public static TbVenda salvar_venda(int lugar) {
//        DocumentoDao.startTransaction( conexao );

//        Date data_documento = new Date ();
        Date data_documento = dc_data_documento.getDate();
        TbVenda venda_local = new TbVenda();
        venda_local.setDataVenda(data_documento);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(data_documento);
        //adicionar 15 dias na data do documento.
        calendar.add(Calendar.DATE, 15);
        venda_local.setDataVencimento(calendar.getTime());
        venda_local.setHora(data_documento);
//        venda_local.setNomeCliente ( _CLIENTE_CONSUMIDOR_FINAL );
        venda_local.setNomeCliente(getNomeCliente());
        venda_local.setClienteNif(getClienteNif());
        venda_local.setNomeConsumidorFinal(getNomeCliente());
//        venda_local.setNomeConsumidorFinal ( txtNomeConsumidorFinal.getText () );

//        TbCliente clienteSelecionado = clienteDao.getClienteByNome( ( String ) cmbCliente.getSelectedItem() );
        TbCliente clienteSelecionado = clientesController.getClienteByNome((String) cmbCliente.getSelectedItem());

        if (!Objects.isNull(clienteSelecionado)) {
            venda_local.setNomeCliente(clienteSelecionado.getNome());
            venda_local.setClienteNif(clienteSelecionado.getNif());
            venda_local.setCodigoCliente(clienteSelecionado);
        } else {
//            venda_local.setNomeCliente ( _CLIENTE_CONSUMIDOR_FINAL );
            venda_local.setNomeCliente(_CLIENTE_CONSUMIDOR_FINAL);
            venda_local.setNomeConsumidorFinal(cmbCliente.getSelectedItem().toString());
            venda_local.setCodigoCliente(clientesController.findByCodigo(getIdCliente()));
//            venda_local.setCodigoCliente( clienteDao.findTbCliente( getIdCliente() ) );
            venda_local.setClienteNif(NUMBER_NIF_GENERICO);
        }

        //Total Ilíquido
        venda_local.setTotalGeral(new BigDecimal(getTotalIliquido(lugar)));
        //desconto por linha
        venda_local.setDescontoComercial(new BigDecimal(getDescontoComercial(lugar)));
        //imposto
        //calculaTotalIVA();
        venda_local.setTotalIva(new BigDecimal(getTotalImposto(lugar)));
//        venda_local.setTotalRetencao( getTotalRetencao() );
        //desconto global
        venda_local.setDescontoFinanceiro(new BigDecimal(getDescontoFinanceiro(lugar)));
        //Total(AOA) <=> Total Líquido
        venda_local.setTotalVenda(new BigDecimal(getTotalAOALiquido(lugar)));
        venda_local.setValorEntregue(new BigDecimal(getValor_entregue()));
        venda_local.setTroco(new BigDecimal(getTrocoLugar()));
        venda_local.setTotalIncidencia(new BigDecimal(getTotalIncidencia(lugar)));
        venda_local.setTotalIncidenciaIsento(new BigDecimal(getTotalIncidenciaIsento(lugar)));
        venda_local.setReferencia("");
        venda_local.setRefDataFact(data_documento);
        /*outros campos*/
        venda_local.setDescontoTotal(new BigDecimal(getDescontoComercial(lugar) + getDescontoFinanceiro(lugar)));
//        venda_local.setIdBanco( bancoDao.findTbBanco( getIdBanco() ) );
//        venda_local.setIdArmazemFK( new TbArmazem( getCodigoArmazem() ) );
        venda_local.setIdArmazemFK(new TbArmazem(id_armzem));
//        venda_local.setIdArmazemFK( armazemDao.findTbArmazem( id_armzem ) );
        venda_local.setCodigoUsuario(new TbUsuario(idUser));
//        venda_local.setCodigoUsuario( usuarioDao.findTbUsuario( idUser ) );
//        venda_local.setCodigoCliente( clienteDao.getClienteByNome( _CLIENTE_CONSUMIDOR_FINAL ) );
        venda_local.setCodigoCliente(clientesController.getClienteByNome(_CLIENTE_CONSUMIDOR_FINAL));
        venda_local.setFkAnoEconomico(anoEconomico);
//        venda_local.setFkAnoEconomico( anoEconomicoDao.getLastObject() );
        venda_local.setFkDocumento(documento);
        venda_local.setCodFact(getCodDocActualizador());
//        venda_local.setCodFact( prox_doc );
//        venda_local.setHashCod( MetodosUtil.criptografia_hash( venda_local, getGrossTotal( lugar ), conexao ) );
        venda_local.setHashCod(MetodosUtil.criptografia_hash(venda_local, getGrossTotal(lugar), conexao));

//        venda_local.setHashCod( MetodosUtil.criptografia_hash( venda_local, getGrossTotal( lugar ), conexaoTransaction ) );
        //venda_local.setHashCod(MetodosUtil.criptografia_hash(prox_doc));
        venda_local.setTotalPorExtenso(iniciais_extenso() + extensoTotalPagar());
//        venda_local.setTotalPorExtenso( MetodosUtil.iniciais_extenso( DOC_FACTURA_RECIBO_FR, documentoDao ) + MetodosUtil.valorPorExtenso( venda_local.getTotalVenda().doubleValue(), "Kwanza" ) );
        System.out.println("STATUS:hash cod processado.");
        venda_local.setAssinatura(MetodosUtil.assinatura_doc(venda_local.getHashCod()));
//        venda_local.setAreaVenda( String.valueOf( cmb_area_venda_restaurante.getSelectedItem() ) );
//        venda_local.setQuarto( "" );

        //System.out.println( "STATUS:documento assinado com sucesso." );
        venda_local.setFkCambio(cambiosController.findByCodigo(ID_CAMBIO_NACIONAL));


        /*status documento*/
        venda_local.setStatusEliminado("false");
        venda_local.setPerformance("false");
        venda_local.setCredito("false");
        venda_local.setGorjeta(new BigDecimal(gorjeta));

        Integer last_venda = 0;
        try {

            if (vendasController.salvar(venda_local)) {
                last_venda = vendasController.getLastVenda().getCodigo();

                if (Objects.isNull(last_venda) || last_venda == 0) {
                    DocumentosController.rollback(conexaoTransaction);
                    conexaoTransaction.close();
                    return venda_local;
                }
                System.err.println("last_venda: " + last_venda);
                System.out.println("STATUS:factura criada com sucesso.");

                if (last_venda != null) {
                    if (getIdDocumento() == DOC_FACTURA_RECIBO_FR) {
//                        MetodosUtil.adicionar_saldo_banco( venda_local.getTotalVenda().doubleValue(), venda_local.get.getIdBanco(), conexao );
                    }
//                    salvar_item_venda_comercial( last_venda );
                }
            } else {
                System.out.println("ERROR: Já existe venda relacionada.");
            }

        } catch (Exception e) {
            System.err.println("STATUS: falha ao actualizar a factura");
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Falha ao Processar a Factura", "FALHA", JOptionPane.ERROR_MESSAGE);
            DocumentosController.rollback(conexao);
//            conexaoTransaction.close();
        }
//        return vendaDao.findTbVenda( last_venda );
        return new TbVenda(last_venda);

    }

    private static String getNomeCliente() {
        return cmbCliente.getSelectedItem().toString();
    }

//        private static String iniciais_extenso()
//    {
//        Documento documento_local = ( Documento ) documentosController.findById( getIdDocumento() );
//        String abreviacao_local = documento_local.getAbreviacao();
//
//        switch ( abreviacao_local )
//        {
//            case "FT":
//                return "Facturamos o valor de: ";
//            case "FR":
//                return "Recebemos a quantia de: ";
//            default:
//                return "São: ";
//        }
//    }
    private static String iniciais_extenso() {
        Documento documento_local = (Documento) documentosController.findById(getIdDocumento());
        String abreviacao_local = documento_local.getAbreviacao();

        switch (abreviacao_local) {
            case "FT":
                return "Facturamos o valor de: ";
            case "FR":
                return "Recebemos a quantia de: ";
            default:
                return "São: ";
        }
    }

    private static String getCodDocActualizador() {
        try {
            documento = (Documento) documentosController.findById(getIdDocumento());
            anoEconomico = (AnoEconomico) anoEconomicoController.findById(getIdAnoEconomico());
            // this.doc_prox_cod = documento.getCodUltimoDoc() + 1;
            doc_prox_cod = vendasController.getUltimaContagemByIdDocumentoAndAnoEconomico(
                    getIdDocumento(), getIdAnoEconomico()) + 1;
            prox_doc = documento.getAbreviacao();
            //FA Série / codigo
            prox_doc += " " + anoEconomico.getSerie() + "/" + doc_prox_cod;
            return prox_doc;
        } catch (Exception e) {
            return "";
        }
    }

    private static Moeda getMoeda() {
        String moedaSelecionada = (String) cmbMoeda.getSelectedItem();

        if (moedaSelecionada == null) {
            return null;
        }

        return new MoedaDao(emf).getByDescricao(moedaSelecionada);
    }

    private static void valor_por_extenco() {
//        System.out.println( "Valor XXXXXXX: " + CfMethods.parseMoedaFormatada( txtTotalApagar.getText() ) );

        // antes só mudava com total diferente de zero e ficava o valor antigo (ex.: sem o desconto do cliente)
        lbValorPorExtenco.setText(extensoTotalPagar());
    }

    /** Total a pagar (depois do desconto) por extenso; vazio se for zero. */
    public static String extensoTotalPagar() {
        double v = 0;
        try {
            v = CfMethods.parseMoedaFormatada(txtTotalApagar.getText());
        } catch (Exception e) {
            // vazio
        }
        return v > 0.004 ? MetodosUtil.valorPorExtenso(v, "Kwanza") : "";
    }

//    private static void valor_por_extenco( Moeda moeda )
//    {
//        System.out.println( "Valor XXXXXXX: " + CfMethods.parseMoedaFormatada( txtTotalApagar.getText() ) );
//        lbValorPorExtenco.setText( MetodosUtil.valorPorExtenso( CfMethods.parseMoedaFormatada( txtTotalApagar.getText() ), moeda.getDesignacao() ) );
//    }
    private static String getClienteNif() {
        try {
            TbCliente cliente = (TbCliente) clientesController.findById(getIdCliente());

            if (cliente == null) {
                return DVML.NUMBER_NIF_GENERICO;
            }

            String nif = cliente.getNif();

            if (nif == null || nif.trim().isEmpty()) {
                return DVML.NUMBER_NIF_GENERICO;
            }

            return nif.trim();
        } catch (Exception e) {
            // logger.error("Erro ao obter NIF do cliente", e);
            return DVML.NUMBER_NIF_GENERICO;
        }
    }

//    private static String getClienteNif()
//    {
//        try
//        {
//            TbCliente cliente = (TbCliente) clientesController.findById( getIdCliente() );
//
//            String nif = cliente.getNif();
//            System.out.println( "NIF CLIENTE: " + nif );
//            if ( nif.equals( "" ) )
//            {
//                return DVML.NUMBER_NIF_GENERICO;
//            }
//            return nif;
//        }
//        catch ( Exception e )
//        {
//            e.printStackTrace();
//            return "";
//        }
//    }
    private static List<TbProduto> getProdutosIsentos() {
        DefaultTableModel modelo = (DefaultTableModel) table.getModel();
//        double taxa = 0.0;
        int codigo_produto = 0;
        List<TbProduto> lista_produtos_isentos = new ArrayList<>();
        for (int i = 0; i < modelo.getRowCount(); i++) {
            codigo_produto = Integer.parseInt(modelo.getValueAt(i, 0).toString());
//            taxa = Double.parseDouble( modelo.getValueAt( i, 6 ).toString() );
//            if ( taxa == 0.0 )
//            {
            lista_produtos_isentos.add((TbProduto) produtosController.findById(codigo_produto));
//            }
        }

        return lista_produtos_isentos;

    }

//    
//    private static String getClienteNif() {
//        try {
//            TbCliente cliente = (TbCliente) clientesController.findById(getIdCliente());
//            
//            return cliente.getNif();
//            
//            
//            
//        } catch (Exception e) {
//            return "";
//        }
//    }
    public static void salvarItemvenda(TbVenda venda) {

        DefaultTableModel modelo = (DefaultTableModel) table.getModel();
        boolean efectuada = true;
        int idProduto = 0;
        double qtd = 0d;
        String lugar = "1";
        TbItemVenda itemVenda = null;
        TbProduto produto;
//        TbStock stock_local;
        double sub_total_iliquido = 0, preco_unitario = 0d, taxa = 0d, desconto = 0d, valor_iva = 0d;

        for (int i = 0; i < table.getRowCount(); i++) {
            try {

                String data_entrega = modelo.getValueAt(i, 1).toString();
                int ano = Integer.parseInt(data_entrega.split("-")[0]);
                int mes = Integer.parseInt(data_entrega.split("-")[1]);
                int dia = Integer.parseInt(data_entrega.split("-")[2]);
                System.out.println("ANO: " + ano);
                Date dataEntrega = new Date();
                dataEntrega.setYear(ano - 1900);
                dataEntrega.setMonth(mes - 1);
                dataEntrega.setDate(dia);

                itemVenda = new TbItemVenda();
                // a coluna tem "serviço-observação": o serviço pode ter hífenes no nome
                // ("Lavagem - Fato"), por isso procura-se do último hífen para trás
                String linhaServico = modelo.getValueAt(i, 2).toString();
                String servico = linhaServico, obs = "";
                TbProduto produto_local = null;
                for (int corte = linhaServico.lastIndexOf('-'); corte >= 0 && produto_local == null; corte = linhaServico.lastIndexOf('-', corte - 1)) {
                    produto_local = (TbProduto) produtosController.findByDesignacao(linhaServico.substring(0, corte));
                    if (produto_local != null) {
                        servico = linhaServico.substring(0, corte);
                        obs = linhaServico.substring(corte + 1);
                    }
                }
                if (produto_local == null) {
                    produto_local = (TbProduto) produtosController.findByDesignacao(servico);
                }
//                lugar = modelo.getValueAt( i, 1 ).toString();

                idProduto = produto_local.getCodigo();
//                idProduto = produtoDao.getProdutoByDescricao( modelo.getValueAt( i, 2 ).toString() ).getCodigo();
                qtd = Double.parseDouble(modelo.getValueAt(i, 3).toString());
                preco_unitario = precosController.getLastIdPrecoByIdProduto(idProduto, qtd).getPrecoVenda().doubleValue();
//                preco_unitario = precoDao.findTbPreco( precoDao.getUltimoIdPrecoByIdProduto( idProduto, qtd ) ).getPrecoVenda().doubleValue();
                // a taxa do próprio produto primeiro (antes ficava a do item anterior: o 1º ia sem IVA)
                taxa = MetodosUtil.getTaxaPercantagem(idProduto);
                sub_total_iliquido = FinanceUtils.getValorComIVA(qtd, taxa, preco_unitario, desconto);

                itemVenda.setCodigoProduto(produtosController.findByCod(idProduto));
                itemVenda.setCodigoVenda(venda);
                itemVenda.setQuantidade(qtd);
                itemVenda.setDesconto(desconto);
                itemVenda.setValorIva(new BigDecimal(taxa).doubleValue());
                itemVenda.setMotivoIsensao(MetodosUtil.getMotivoIsensao(idProduto));
                itemVenda.setCodigoIsensao(MetodosUtil.getCodigoRegime(idProduto));
                itemVenda.setTotal(new BigDecimal(sub_total_iliquido));
                itemVenda.setPosicao((i + 1));
                itemVenda.setFkPreco(precosController.getLastIdPrecoByIdProduto(itemVenda.getCodigoProduto().getCodigo(), itemVenda.getQuantidade()));
//                itemVenda.setFkPreco( precoDao.findTbPreco( precoDao.getUltimoIdPrecoByIdProduto( itemVenda.getCodigoProduto().getCodigo(), itemVenda.getQuantidade() ) ) );
//                itemVenda.setValorRetencao( 0d );
                itemVenda.setDataServico(new Date());
                itemVenda.setObs(obs);
                itemVenda.setDataEntrega(dataEntrega);
                /*setando a mesa e lugar para cunprir a formalidade só aplica-se somente para resstauração*/
//                itemVenda.setFkLugares( ( TbLugares ) lugaresController.findById( DVML.LUGAR_BALCAO ) );
                itemVenda.setFkLugares((TbLugares) lugaresController.findById(DVML.LUGAR_BALCAO));
//                itemVenda.setFkLugares( lugarDao.findTbLugares( lugarDao.getIdByDescricao( lugar ) ) );
                itemVenda.setFkMesas((TbMesas) mesasController.findByDesignacao(mesa));

                //cria o item venda
//                itemVendaDao.create ( itemVenda );
//                int last_venda = itemVendaDao.criarComProcedimentos( itemVenda, conexao );
                if (!itemVendasController.salvarLavandaria(itemVenda)) {
                    DocumentosController.rollback(conexaoTransaction);
                    conexaoTransaction.close();
                    return;
                }

                boolean isStocavel = produto_local.getStocavel().equals("true");

                if (isStocavel) {
                    stock_local = stocksController.getStockByIdProdutoAndIdArmazem(itemVenda.getCodigoProduto().getCodigo(), getCodigoArmazem());
                }
//                stock_local = stockDao.get_stock_by_id_produto_and_id_armazem( idProduto, id_armazem );
//                stock_local = stockDao.get_stock_by_id_produto_and_id_armazem( itemVenda.getCodigoProduto().getCodigo(), id_armzem );
                if (!Objects.isNull(stock_local)) {
                    if (getIdDocumento() == DOC_FACTURA_RECIBO_FR || getIdDocumento() == DOC_FACTURA_FT) {
                        System.out.println("passei quando é FR ou FT");
                        //so retirar caso existir mesmo no armazém em questão.
                        if (stock_local.getCodigo() != 0 && itemVenda.getCodigoProduto().getStocavel().equals("true")) {
                            actualizar_quantidade(itemVenda.getCodigoProduto().getCodigo(), itemVenda.getQuantidade());
                        }
                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
                efectuada = false;
                JOptionPane.showMessageDialog(null, "Falha ao registrar o produto: " + itemVenda.getCodigoProduto().getCodigo() + " na Factura");
                break;
            }
        }

        if (efectuada) {
            cmbCliente.setModel(new DefaultComboBoxModel(clientesController.getVectorExcetoConsumidorFinal()));
//            cmbCliente.setSelectedIndex( 0 );
            List<TbProduto> lista_produto_isentos = getProdutosIsentos();
            String motivos_isentos = MetodosUtil.getMotivoIsensaoProdutos(lista_produto_isentos);

            if ((getIdDocumento() == DOC_FACTURA_RECIBO_FR)) {
                registrar_forma_pagamento(venda.getCodigo());
            }
            mensagemFacturaEfectuada(venda);
            limpar();
//            setSalvarPedidos();
            txtObs.setText("");
            limparTotais();
            TbPedido pedido = pedidoDao.findTbPedido(pedidoDao.getLastPedidoByDefignacaoMesaFALSE(mesa));
            System.out.println(" VARIAVEL PEDIDO: " + pedido);
            PedidoDao.eliminarPedido(pedido, conexaoTransaction); // Elimina o pedido
            setSalvarPedidoPosVenda(conexaoTransaction);

            // commit que devolve o autocommit (o "COMMIT" em SQL deixava a ligação partilhada em modo manual
            // e o que os outros ecrãs gravassem a seguir ficava por confirmar)
            DocumentosController.commit(conexaoTransaction);
            gorjeta = 0;

            if ((getIdDocumento() == DOC_FACTURA_RECIBO_FR)) {
                ListaVendaRecolhas listaVendaCliente = new ListaVendaRecolhas(venda.getCodigo(), abreviacao, false, true, "Original");

            } else {

                ListaVenda1 listaVenda1 = new ListaVenda1(venda.getCodigo(), abreviacao, false, true, "", motivos_isentos);
            }

            if (getIdDocumento() == DOC_FACTURA_RECIBO_FR || getIdDocumento() == DOC_FACTURA_FT) {

                procedimentoImprimirTicket();

            }
        }

    }

    public static void actualizar_quantidade(int cod, double quantidade) {

        System.err.println("Entrei no actualizar quantidade");
        String sql = "UPDATE tb_stock SET quantidade_existente =  " + (getQuantidadeProduto(cod) - quantidade) + " WHERE cod_produto_codigo = " + cod + " AND  cod_armazem = " + id_armzem;
        System.out.println("Quantidade   : " + quantidade);
        conexao.executeUpdate(sql);

    }

    /**
     * "Factura efectuada com sucesso!" com a imagem de OK (como no SysKitandaHotel):
     * nº do documento, total, recebido e troco (na FR) e o total por extenso.
     */
    private static void mensagemFacturaEfectuada(TbVenda venda) {
        try {
            double total = CfMethods.parseMoedaFormatada(txtTotalApagar.getText());
            java.util.List<String[]> linhas = new java.util.ArrayList<>();
            linhas.add(new String[]{"Total", CfMethods.formatarComoMoeda(total)});
            if (getIdDocumento() == DOC_FACTURA_RECIBO_FR) {
                linhas.add(new String[]{"Recebido", CfMethods.formatarComoMoeda(getValor_entregue())});
                linhas.add(new String[]{"Troco", CfMethods.formatarComoMoeda(getTroco())});
            }
            String extenso = total > 0.004 ? MetodosUtil.valorPorExtenso(total, "Kwanza") : null;
            util.ui.MensagemSucesso.mostrar(table, "Factura efectuada com sucesso!",
                    venda == null ? null : venda.getCodFact(), linhas.toArray(new String[0][]), extenso);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Factura efectuada com sucesso!");
        }
    }

    /** Depois da venda: os totais e o valor por extenso voltam a zero (a tabela fica vazia). */
    public static void limparTotais() {
        String zero = CfMethods.formatarComoMoeda(0);
        txtTotalQTD.setText("0");
        txtTotalGeral.setText(zero);
        txtDesconto.setText(zero);
        txtTotalApagar.setText(zero);
        lbValorPorExtenco.setText("");
    }

    public static void limpar() {

        txtIniciaisCliente.setText("");
        txtTelefoneCliente.setText("");
        cmbCliente.setSelectedIndex(0);
        lbTelefoneCliente.setText("");
        lbEmailCliente.setText("");

    }

//    public static double getValor_entregue()
//    {
//        return FormaPagamentoVisao.get_total_valor().doubleValue();
//    }
//
    ////    public static double getTroco()
////    {
////        return 0d;
////    }
//    public static double getTroco()
//    {
//        return CfMethods.parseMoedaFormatada( FormaPagamentoVisao.lb_troco.getText() );
//    }
    /** O que o cliente entregou no diálogo de pagamento da recolha (dinheiro + cartão + transferência). */
    public static double getValor_entregue() {
        try {
            double v = 0;
            for (javax.swing.JTextField t : new javax.swing.JTextField[]{FormaPagamentoGoldVisao.txtDinheiro,
                FormaPagamentoGoldVisao.txtCartao, FormaPagamentoGoldVisao.txtTransferencia}) {
                v += t == null ? 0 : CfMethods.parseMoedaFormatadaNovo(t.getText());
            }
            return v;
        } catch (Exception e) {
            return 0d;
        }
    }

//    public static double getTroco()
//    {
//        return 0d;
//    }
    /** A data escolhida no campo "Data de entrega" (hoje, se estiver vazio). */
    static Date dataEntregaEscolhida() {
        return dc_data_entrega != null && dc_data_entrega.getDate() != null ? dc_data_entrega.getDate() : new Date();
    }

    public static double getTroco() {

        try {
            // a recolha cobra com o FormaPagamentoGoldVisao (o FormaPagamentoVisao não é aberto aqui)
            return Math.max(0, CfMethods.parseMoedaFormatada(FormaPagamentoGoldVisao.lb_troco.getText()));
        } catch (Exception e) {
        }

        return 0d;

    }

    public static double getTrocoLugar() {

        try {
            return CfMethods.parseMoedaFormatada(FormaPagamentoLugarVisao.lb_troco.getText());
        } catch (Exception e) {
        }

        return 0d;

    }

    public static void setTotalGeral() {
        DefaultTableModel modelo = (DefaultTableModel) table.getModel();
        double total_geral = 0;
        for (int i = 0; i <= modelo.getRowCount() - 1; i++) {
            total_geral += Double.parseDouble(modelo.getValueAt(i, 4).toString());
        }
        txtTotalApagar.setText(CfMethods.formatarComoMoeda(total_geral));
    }

    public static void setTotalPecas() {
        DefaultTableModel modelo = (DefaultTableModel) table.getModel();
        double total_geral = 0;
        for (int i = 0; i <= modelo.getRowCount() - 1; i++) {
            total_geral += Double.parseDouble(modelo.getValueAt(i, 3).toString());
        }
        txtTotalQTD.setText(String.valueOf(total_geral));
    }

    public boolean campos_invalido_imprimir() {

        if (getValor_entregue() < Double.parseDouble(txtTotalApagar.getText())) {
            JOptionPane.showMessageDialog(null, "O valor entregue tem quer ser maior ou igual ao Total a Pagar", "AVISO", JOptionPane.WARNING_MESSAGE);
//            txtValorEntregue.requestFocus();
            return true;
        }
        return false;
    }

//    public static boolean registrar_forma_pagamento( int id_venda )
//    {
//
//        DefaultTableModel modelo = ( DefaultTableModel ) FormaPagamentoVisao.tabela_forma_pagamento.getModel();
//
//        FormaPagamentoItem formaPagamentoItem;
//        for ( int i = 0; i < modelo.getRowCount(); i++ )
//        {
//            formaPagamentoItem = new FormaPagamentoItem();
//            Integer id_banco = Integer.parseInt( modelo.getValueAt( i, 0 ).toString() );
//            String referencia = ( modelo.getValueAt( i, 2 ) != null ) ? modelo.getValueAt( i, 2 ).toString() : "n/a";
//            String valor = ( modelo.getValueAt( i, 3 ) != null ) ? modelo.getValueAt( i, 3 ).toString() : "0";
//
//            formaPagamentoItem.setValor( new BigDecimal( valor ) );
//            formaPagamentoItem.setReferencia( referencia );
//            formaPagamentoItem.setFkFormaPagamento( new FormaPagamento( id_banco ) );
//            formaPagamentoItem.setFkVenda( new TbVenda( id_venda ) );
//
//            try
//            {
//                if ( !valor.equals( "0" ) )
//                {
//                    FormaPagamentoItemDao.insert( formaPagamentoItem, conexao );
//                }
//            }
//            catch ( Exception e )
//            {
//                return false;
//            }
//
//        }
//
//        return true;
//
//    }
//    public static boolean registrar_forma_pagamento( int id_venda )
//    {
//
//        DefaultTableModel modelo = ( DefaultTableModel ) FormaPagamentoVisao.tabela_forma_pagamento.getModel();
//
//        FormaPagamentoItem formaPagamentoItem;
//
//        for ( int i = 0; i < modelo.getRowCount(); i++ )
//        {
//
//            formaPagamentoItem = new FormaPagamentoItem();
//            Integer id_forma_pagamento = Integer.parseInt( modelo.getValueAt( i, 0 ).toString() );
//            String referencia = ( modelo.getValueAt( i, 2 ) != null ) ? modelo.getValueAt( i, 2 ).toString() : "n/a";
    ////            String valor = ( Objects.nonNull( modelo.getValueAt( i, 3 ) ) ) ? modelo.getValueAt( i, 3 ).toString() : "0";
//            String valor = ( !modelo.getValueAt( i, 3 ).equals( "" ) ) ? modelo.getValueAt( i, 3 ).toString() : "0";
//
//            formaPagamentoItem.setValor( new BigDecimal( valor ) );
//            formaPagamentoItem.setReferencia( referencia );
//            formaPagamentoItem.setFkVenda( new TbVenda( id_venda ) );
//            formaPagamentoItem.setFkFormaPagamento( new FormaPagamento( id_forma_pagamento ) );
//
//            try
//            {
//                if ( !valor.equals( "0" ) )
//                {
//                    formaPagamentoItemController.salvar( formaPagamentoItem );
//                }
//            }
//            catch ( Exception e )
//            {
//                return false;
//            }
//
//        }
//
//        return true;
//    }
    private static String getValor(JTextField field) {
        String valor = "0";

//        field.setEnabled( true );
//        if ( !field.isEnabled() )
//        {
//            System.out.println( "FIELD DESACTIVO" );
//            return "0";
//        }
        try {
            valor = field.getText();
        } catch (Exception e) {
            e.printStackTrace();
            valor = "0";
        }
//        
        return valor;
    }

    public static void registrar_forma_pagamento(int id_venda) {

        String valorDinheiro = getValor(FormaPagamentoGoldVisao.txtDinheiro);
        String valorCartao = getValor(FormaPagamentoGoldVisao.txtCartao);
        String valorTransferencia = getValor(FormaPagamentoGoldVisao.txtTransferencia);

        /*
        registrar_forma_pagamento_and_banco( id_venda, 1, "0" );
        registrar_forma_pagamento_and_banco( id_venda, 2, "0" );
        registrar_forma_pagamento_and_banco( id_venda, 4, "0" );
         */
        if (!valorDinheiro.equals("")) {
            registrar_forma_pagamento_and_banco(id_venda, 1, valorDinheiro);
            System.out.println("SALVOU NO DINHEIRO");
        }

        if (!valorCartao.equals("")) {
            registrar_forma_pagamento_and_banco(id_venda, 2, valorCartao);
            System.out.println("SALVOU NO CARTÃO");
        }

        if (!valorTransferencia.equals("")) {
            registrar_forma_pagamento_and_banco(id_venda, 4, valorTransferencia);
            System.out.println("SALVOU NA TRANSFERÊNCIA");
        }

    }

    public static void registrar_forma_pagamento_and_banco(int id_venda, int id_banco, String valor) {

//        DocumentoDao.startTransaction( conexao );
        FormaPagamentoItem formaPagamentoItem;
        Contas contas;
        double troco = CfMethods.parseMoedaFormatada(FormaPagamentoGoldVisao.lb_troco.getText());

        formaPagamentoItem = new FormaPagamentoItem();
        FormaPagamento formaPagamento = formaPagamentoController.findByCodigo(id_banco);
        contas = (Contas) contaController.findById(formaPagamento.getFkContaAssociada());

        String referencia = "n/a";
        // o valor vem como o operador o escreveu (30400, 30.400, 30.400,00...): new BigDecimal(texto) rebentava e perdia a venda
        BigDecimal valorPago = BigDecimal.valueOf(CfMethods.parseMoedaFormatadaNovo(valor));

        formaPagamentoItem.setValor(valorPago);
        formaPagamentoItem.setReferencia(referencia);
        formaPagamentoItem.setTroco(new BigDecimal(troco));
        formaPagamentoItem.setValor_real(
                formaPagamentoItem.getValor().subtract(formaPagamentoItem.getTroco()));
        formaPagamentoItem.setFkVenda(new TbVenda(id_venda));
        formaPagamentoItem.setFkFormaPagamento(new FormaPagamento(id_banco));

        try {
//            if ( !valor.equals( "0" ) )
            if (true) {
                formaPagamentoItemController.salvar(formaPagamentoItem);

                if (Objects.nonNull(contas)) {
                    MetodosUtilTS.entradaTesouraria(contas,
                            lb_proximo_documento.getText(),
                            formaPagamento,
                            referencia,
                            valorPago,
                            idUser,
                            usuariosController,
                            cmc,
                            conexaoTransaction
                    );
                }
                troco = 0;
//                DocumentoDao.commitTransaction( conexao );

            }
        } catch (Exception e) {
            e.printStackTrace();
//            DocumentoDao.rollBackTransaction( conexao );
        }

    }

    public static boolean registrar_forma_pagamento_lugar(int id_venda, int lugar) {

        DefaultTableModel modelo = (DefaultTableModel) FormaPagamentoLugarVisao.tabela_forma_pagamento.getModel();

        FormaPagamentoItem formaPagamentoItem;

        for (int i = 0; i < modelo.getRowCount(); i++) {

            formaPagamentoItem = new FormaPagamentoItem();
            Integer id_forma_pagamento = Integer.parseInt(modelo.getValueAt(i, 0).toString());
            String referencia = (modelo.getValueAt(i, 2) != null) ? modelo.getValueAt(i, 2).toString() : "n/a";
            String valor = (!modelo.getValueAt(i, 3).equals("")) ? modelo.getValueAt(i, 3).toString() : "0";

            formaPagamentoItem.setValor(new BigDecimal(valor));
            formaPagamentoItem.setReferencia(referencia);
            formaPagamentoItem.setFkVenda(new TbVenda(id_venda));
            formaPagamentoItem.setFkFormaPagamento(new FormaPagamento(id_forma_pagamento));

            try {
                if (!valor.equals("0")) {
                    formaPagamentoItemController.salvar(formaPagamentoItem);
                }
            } catch (Exception e) {
                return false;
            }
        }

        return true;
    }

    //#D
    public static void setTotalPagar() {

        DefaultTableModel modelo = (DefaultTableModel) table.getModel();
        double total_pagar = 0, totalGeral = 0;
        for (int i = 0; i < modelo.getRowCount(); i++) {
            totalGeral += CfMethods.parseMoedaFormatada(modelo.getValueAt(i, 4).toString());
        }

        double desconto = getDescontoFinanceiro();

        total_pagar = totalGeral - desconto;

        txtTotalGeral.setText(CfMethods.formatarComoMoeda(totalGeral));
        txtDesconto.setText(CfMethods.formatarComoMoeda(desconto));
        txtTotalApagar.setText(CfMethods.formatarComoMoeda(total_pagar));
        // o extenso acompanha sempre o total a pagar (também quando o desconto do cliente muda)
        valor_por_extenco();

    }

    public static void setTotalQTD() {

        DefaultTableModel modelo = (DefaultTableModel) table.getModel();
        int total_qtd = 0;
        for (int i = 0; i < modelo.getRowCount(); i++) {
            total_qtd += Double.parseDouble(modelo.getValueAt(i, 3).toString());
        }
//        txtTotalQTD.setText( String.valueOf( total_qtd ) );
        txtTotalQTD.setText(String.valueOf(modelo.getRowCount()));

    }

    public static void setTotalPagarByLugar(int lugar) {

        DefaultTableModel modelo = (DefaultTableModel) table.getModel();

        double total_pagar = 0;

        for (int i = 0; i < modelo.getRowCount(); i++) {

            if (lugaresController.getIdByDescricao(table.getModel().getValueAt(i, 1).toString()) == lugar) {
//                total_pagar += Double.parseDouble( String.valueOf( modelo.getValueAt( i, 4 ) ) );
                total_pagar += CfMethods.parseMoedaFormatada(modelo.getValueAt(i, 4).toString());
            }

        }
        txtTotalApagar.setText(CfMethods.formatarComoMoeda(total_pagar));
//        txtTotalApagar.setText( String.valueOf( total_pagar ) );
//        txtValorEntregue.setText( txtTotalApagar.getText() );
//        valor_por_extenco();

    }

//    last_venda
//    private void procedimento_converter_factura()
//    {
//
//        if ( !campos_invalido_imprimir() )
//        {
//            TbVenda salvar_venda = salvar_venda();
//
//            if ( salvar_venda != null )
//            {
//                salvarItemvenda( salvar_venda );
//                remover_dados_tabela();
//            }
//            else
//            {
//                System.err.println( "  erro ao salvar venda!!!" );
//            }
//        }
//
//    }
//    public void setTotalPagar()
//    {
//
//        DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
//        double total_pagar = 0;
//        for ( int i = 0; i < modelo.getRowCount(); i++ )
//        {
    ////            total_pagar += Double.parseDouble( String.valueOf( modelo.getValueAt( i, 4 ) ) );
//            total_pagar += CfMethods.parseMoedaFormatada( modelo.getValueAt( i, 4 ).toString() );
//        }
////        txtTotalApagar.setText( String.valueOf( total_pagar ) );
//            txtTotalApagar.setText( CfMethods.formatarComoMoeda( total_pagar ) );
//        try
//        {
//            calcularTroco();
//
//        }
//        catch ( Exception e )
//        {
//        }
//
//    }
//z
//    public void setTotalPagar()
//    {
//
//        DefaultTableModel modelo = ( DefaultTableModel ) jTable1.getModel();
//        double total_pagar = 0;
//        for ( int i = 0; i < modelo.getRowCount(); i++ )
//        {
//            total_pagar += Double.parseDouble( String.valueOf( modelo.getValueAt( i, 4 ) ) );
//        }
//        txtTotalApagar.setText( String.valueOf( total_pagar ) );
//        try
//        {
//            getTroco();
//
//        }
//        catch ( Exception e )
//        {
//        }
//
//    }
    //----------- evento do teclado ---------------------------------------
    class TratarEventoValorEntregue implements KeyListener {

        String prefixo = "";
        int codigo = 0, codigo_categoria = 0, quatidade_produto = 0;

        public void keyPressed(KeyEvent evt) {

            if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

                try {

                } catch (Exception ex) {
                    Logger.getLogger(VendaUsuarioVisao.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }

        public void keyReleased(KeyEvent evt) {
        }

        public void keyTyped(KeyEvent evt) {
        }
    }

//    private static void valor_por_extenco()
//    {
//        //lbValorPorExtenco.setText(    "São: "   +  MetodosUtil .valorPorExtenso(     Double.parseDouble(   txtTotalApagar.getText() )   ) );
//    }
    private static void remover_dados_tabela() {
        DefaultTableModel modelo = (DefaultTableModel) table.getModel();
        modelo.setRowCount(0);
    }

    public static boolean isStocavel(String status) {
        try {
            if (status.equals("true")) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return true;
        }

    }

    private void procedimento_processar_pedidos() {

        int opcao = JOptionPane.showConfirmDialog(null, "Por Favor\nDeseja ver a Conta da Mesa Completa?");
        //Mostra Pedidos Gerais ou seja Pedidos de todos lugares de uma Mesa
        if (opcao == JOptionPane.YES_OPTION) {
            imprimirPedidos();
        } else if (opcao == JOptionPane.NO_OPTION) {

            try {

                opcao = Integer.parseInt(JOptionPane.showInputDialog(null, "Por Favor\nQual lugar Deseja ver a Conta?"));
                if (exite_pedido_lugar(opcao)) {
                    //Mostra Pedidos Por TbLugares ou seja TbPedido de um lugar na Mesa
                    imprimirPedidosPorLugar(opcao);
                } else {
                    JOptionPane.showMessageDialog(null, "Não existe pedido neste lugar!", "AVISO", JOptionPane.WARNING_MESSAGE);
                }

            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Erro: tem que ser um número.");
            }

        } else {
            JOptionPane.showMessageDialog(null, "Operação cancelada");
        }

    }

    //Lugar
    public static void salvarItemvendaLugar(int lugar) {
        DefaultTableModel modelo = (DefaultTableModel) table.getModel();

        Integer cod_venda = vendasController.getLastVenda().getCodigo();
//        int cod_venda = vendaDao.getLastVenda();
        int cod_mesa = mesasDao.getIdByDescricao(mesa);
        boolean efectuada = true;
        TbVenda venda_local = vendasController.getVendas(cod_venda);
//ESTE
//        TbVenda venda_local = vendaDao.findTbVenda( cod_venda );
        TbItemVenda itemVenda = null;
        TbProduto produto;
//        TbStock stock_local;
        double total = 0;
        int valor_lugar_tabela = 0;

        for (int i = 0; i < table.getRowCount(); i++) {
            try {
                valor_lugar_tabela = getLugarSelecionado(i);
                if (valor_lugar_tabela == lugar) {

                    itemVenda = new TbItemVenda();
                    TbProduto produto_local = (TbProduto) produtosController.findByDesignacao(modelo.getValueAt(i, 2).toString());

                    int idProduto = produtosController.findByDesignacao(modelo.getValueAt(i, 2).toString()).getCodigo();
                    double qtd = Double.parseDouble(modelo.getValueAt(i, 3).toString());
                    double preco_unitario = precoDao.findTbPreco(precoDao.getUltimoIdPrecoByIdProduto(idProduto, qtd)).getPrecoVenda().doubleValue();
                    double taxa = MetodosUtil.getTaxaPercantagem(idProduto);
                    double desconto = 0d;
                    double sub_total_iliquido = FinanceUtils.getValorComIVA(qtd, taxa, preco_unitario, desconto);

                    itemVenda.setCodigoProduto(produtosController.findByCod(idProduto));
                    itemVenda.setCodigoVenda(venda_local);
                    itemVenda.setQuantidade(qtd);
                    itemVenda.setDesconto(desconto);
                    itemVenda.setValorIva(new BigDecimal(taxa).doubleValue());
//                    itemVenda.setValorRetencao( 0d );
                    itemVenda.setDataServico(new Date());
                    itemVenda.setMotivoIsensao(MetodosUtil.getMotivoIsensao(idProduto));
                    itemVenda.setCodigoIsensao(MetodosUtil.getCodigoRegime(idProduto));
                    itemVenda.setTotal(new BigDecimal(sub_total_iliquido));
                    itemVenda.setFkPreco(precoDao.findTbPreco(precoDao.getUltimoIdPrecoByIdProduto(itemVenda.getCodigoProduto().getCodigo(), itemVenda.getQuantidade())));

                    /*setando a mesa e lugar para cunprir a formalidade só aplica-se somente para resstauração*/
                    itemVenda.setFkLugares((TbLugares) lugaresController.findById(lugar));
//                itemVenda.setFkLugares( lugarDao.findTbLugares( lugarDao.getIdByDescricao( lugar ) ) );
                    itemVenda.setFkMesas((TbMesas) mesasController.findByDesignacao(mesa));

                    //cria o item venda
//                    itemVendaDao.create ( itemVenda );
                    if (!itemVendasController.salvar(itemVenda)) {
                        DocumentosController.rollback(conexaoTransaction);
                        conexaoTransaction.close();
                        return;
                    }

                    boolean isStocavel = produto_local.getStocavel().equals("true");

                    if (isStocavel) {
                        stock_local = stocksController.getStockByIdProdutoAndIdArmazem(itemVenda.getCodigoProduto().getCodigo(), getCodigoArmazem());
                    }
//                if ( stock_local.getCodigo() != 0 && itemVenda.getCodigoProduto().getStocavel().equals( "true" ) )
//                {
//                    System.out.println( "chamei o actualizar quantidade" );
//                    MetodosUtil.subtrai_quantidade( idProduto, qtd, id_armazem, conexao );
//                }
                    if (getIdDocumento() == DOC_FACTURA_RECIBO_FR || getIdDocumento() == DOC_FACTURA_FT) {
                        System.out.println("passei quando é FR ou FT");
                        if (!Objects.isNull(stock_local) && isStocavel) {
                            System.out.println("chamei o actualizar quantidade");
                            actualizar_quantidade(itemVenda.getCodigoProduto().getCodigo(), itemVenda.getQuantidade());
                        }

                    }

//                    if ( stock_local.getCodigo() != 0 && itemVenda.getCodigoProduto().getStocavel().equals( "true" ) )
//                    {
//                        MetodosUtil.subtrai_quantidade( idProduto, qtd, id_armzem, conexao );
//                    }
                
             ///
//                    if ( stock_local.getCodigo() != 0 && itemVenda.getCodigoProduto().getStocavel().equals( "true" ) )
//                    {
//                        actualizar_quantidade( itemVenda.getCodigoProduto().getCodigo(), itemVenda.getQuantidade() );
//                    }
                    //
                }

            }
            catch (Exception e) {
                e.printStackTrace();
                efectuada = false;
                JOptionPane.showMessageDialog(null, "Falha ao registrar o produto: " + itemVenda.getCodigoProduto().getCodigo() + " na Factura");
                DocumentosController.rollback(conexaoTransaction);
                conexaoTransaction.close();
                return;
            }
        }

        if (efectuada) {
//            DocumentoDao.commitTransaction( conexao );
            gorjeta = 0;
//          act actualizar_cod_doc ( venda_local.getDataVenda () );
            alterar_status_pedido_lugar(lugar);
            util.ui.MensagemSucesso.mostrar(table, "Factura convertida com sucesso!");
            actualizar_lugar(lugar);
            procedimento_mesas_livre();

//            ListaVenda1 original = new ListaVenda1( last_cod, this.abreviacao, false, ck_simplificada.isSelected(), "Original", motivos_isentos )
            ListaVendasMesas listaVenda1 = new ListaVendasMesas(cod_venda, abreviacao, cod_mesa, lugar, false, true, "Duplicado");
            ListaVendasMesas listaVenda2 = new ListaVendasMesas(cod_venda, abreviacao, cod_mesa, lugar, false, true, "Original");
//            ListaVenda listaVenda1 = new ListaVenda( cod_venda, abreviacao, false, true, "Duplicado" );
//            ListaVenda listaVenda2 = new ListaVenda( cod_venda, abreviacao, false, true, "Original" );

//            ListaVenda listaVenda = new ListaVenda(cod_venda, cod_mesa, lugar, false, true, "Original");
//            ListaVenda listaVenda1 = new ListaVenda(cod_venda, cod_mesa, lugar, false, true, "Duplicado");
            //ListaVenda listaVenda1 = new ListaVenda(cod_venda, cod_mesa, lugar, false, true);
        }

    }

    public static double getTotal_por_lugar(int lugar) {

        DefaultTableModel modelo = (DefaultTableModel) table.getModel();
        double total_local = 0d;
        for (int i = 0; i < table.getRowCount(); i++) {

            int lugar_local = Integer.parseInt(modelo.getValueAt(i, 1).toString());
            if (lugar_local == lugar) {

                total_local += Double.parseDouble(modelo.getValueAt(i, 4).toString());
            }

        }

        return total_local;
    }

    public static double getTotal_QTD_lugar(int lugar) {

        DefaultTableModel modelo = (DefaultTableModel) table.getModel();
        double total_local = 0d;
        for (int i = 0; i < table.getRowCount(); i++) {

            int lugar_local = Integer.parseInt(modelo.getValueAt(i, 1).toString());
            if (lugar_local == lugar) {

                total_local += Double.parseDouble(modelo.getValueAt(i, 3).toString());
            }

        }

        return total_local;
    }

//        public static void dispose() {
//        doDispose();
//    }
    public static void alterar_status_pedido_lugar(int lugar) {

        int cod_pedido = pedidoDao.getLastPedidoByDefignacaoMesaFALSE(mesa);
        List<TbItemPedidos> list = itemPedidosDao.buscaTodosItemPedidos(cod_pedido, lugar);

        for (int i = 0; i < list.size(); i++) {

            TbItemPedidos itemPedidos = list.get(i);
            itemPedidos.setStatusConvertido(true);

            try {
                itemPedidosDao.edit(itemPedidos);
                System.out.println("1:linha do pedido convertido");
            } catch (Exception e) {
                System.out.println("0:falha ao actualizar a linha do pedido");
                e.printStackTrace();
            }

        }

    }

    private static void alterar_status_pedido_all_lugar() {

        int cod_pedido = pedidoDao.getLastPedidoByDefignacaoMesaFALSE(mesa);
        List<TbItemPedidos> list = itemPedidosDao.buscaTodosItemPedidos(cod_pedido);
        for (int i = 0; i < list.size(); i++) {

            TbItemPedidos itemPedidos = list.get(i);
            itemPedidos.setStatusConvertido(true);

            try {
                itemPedidosDao.edit(itemPedidos);
            } catch (Exception e) {
            }
        }
    }

    public static boolean exite_pedido_lugar(int lugar) {

        boolean efectuada = false;
        for (int i = 0; i < table.getRowCount(); i++) {
            if (lugaresController.getIdByDescricao(table.getModel().getValueAt(i, 1).toString()) == lugar) {
                efectuada = true;
                break;
            }
        }
        return efectuada;
    }

//    public void setTotalPagarByLugar( int lugar )
//    {
//
//        DefaultTableModel modelo = ( DefaultTableModel ) jTable1.getModel();
//
//        double total_pagar = 0;
//
//        for ( int i = 0; i < modelo.getRowCount(); i++ )
//        {
//
//            if ( lugarDao.getIdByDescricao( jTable1.getModel().getValueAt( i, 1 ).toString() ) == lugar )
//            {
//                total_pagar += Double.parseDouble( String.valueOf( modelo.getValueAt( i, 4 ) ) );
//            }
//
//        }
//
//        txtTotalApagar.setText( String.valueOf( total_pagar ) );
    ////        txtValorEntregue.setText( txtTotalApagar.getText() );
//        valor_por_extenco();
//
//        try
//        {
//
//            getTroco();
//
//        }
//        catch ( Exception e )
//        {
//            //e.printStackTrace();
//        }
//
//    }
//    private void actualizar_campo( int qtd )
//    {
//
//        DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
//        TbItemPedidos itemPedidos = itemPedidosDao.findTbItemPedidos( Integer.parseInt( jTable1.getValueAt( this.linha_actual, 0 ).toString() ) );
//        itemPedidos.setQtd( qtd );
//
//        try
//        {
//            itemPedidosDao.edit( itemPedidos );
//            actualizar();
//        }
//        catch ( Exception e )
//        {
//        }
//
//    }
    private void adicionar_butons() {

    }

    private void adicionar_catgorias() {

        List<TbTipoProduto> lista_categoria = tipoProdutosController.buscaTodasCategoriasByAreaExcept(DVML.COD_CATEGORIA_URGENCIA);
//        List<TbTipoProduto> lista_categoria = tipoProdutoDao.getAll_filtro();
        this.TAMANHO_CATEGORIA = lista_categoria.size();

//        this.gl = new GridLayout( TAMANHO_CENTRO, 1 );
        int raiz_quadrada = (int) Math.sqrt(TAMANHO_CATEGORIA);

        int linhas = raiz_quadrada;
        int colunas = raiz_quadrada;

        painel_central.setLayout(new java.awt.GridLayout(linhas, colunas));
        painel_central.removeAll();
        celulas(150, 92); // categorias: mosaicos de texto
        jScrollPane3.setViewportView(painel_central);

        botoes_object.clear();
        btn_voltar.setVisible(false);
        designacao_categoria.setVisible(false);

        for (int i = 0; i < TAMANHO_CATEGORIA; i++) {

            JButton jButton = new JButton(lista_categoria.get(i).getDesignacao());
            jButton.addActionListener(new ButtonHandler1());
            botoes_object.add(jButton);
            painel_central.add(jButton);

        }

    }

    private void celulas(int largura, int altura) {
        if (painel_central instanceof util.ui.GrelhaAdaptavel) {
            ((util.ui.GrelhaAdaptavel) painel_central).setCelula(largura, altura);
        }
    }

    /**
     * Cartão do artigo com a fotografia da peça (ou as iniciais, se não tiver),
     * o nome e o preço; o clique lança a peça na recolha. O ProdutoItemVisao
     * antigo (desenho fixo de 230x225) ficava cortado na grelha adaptável.
     */
    private javax.swing.JComponent cartaoProduto(TbProduto produto, TbPreco preco) {
        celulas(150, 176); // produtos: cartões altos, com a fotografia
        final String designacao = produto.getDesignacao();
        String textoPreco = (preco != null && preco.getPrecoVenda() != null)
                ? CfMethods.formatarComoMoeda(preco.getPrecoVenda()) : "";
        return new util.ui.MesasWeb.CartaoProduto(designacao, textoPreco, null, false, foto(produto.getPhoto()),
                util.ui.MesasWeb.corCategoria(designacao_categoria.getText()),
                () -> RecolhaPedidosVisao.adicionarImportar(designacao));
    }

    private static java.awt.Image foto(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        try {
            java.awt.Image i = new ImageIcon(bytes).getImage();
            return i.getWidth(null) > 0 ? i : null;
        } catch (Exception e) {
            return null;
        }
    }

    private void adicionar_centro_botao(int idTipoProduto) {
        try {
            System.out.println("ESTOU AQUI DALLAS");

            // 1️⃣ Buscar tipo de produto
            TbTipoProduto tipoProduto = tipoProdutosController.getTipoProdutoByCodigo(idTipoProduto);
            if (tipoProduto == null) {
                System.err.println("Tipo de produto não encontrado para id: " + idTipoProduto);
                return;
            }

            // 2️⃣ Buscar família completa do banco
            Familia familia = tipoProduto.getFkFamilia();
            if (familia == null) {
                System.err.println("Familia não encontrada para o tipo de produto: " + tipoProduto.getDesignacao());
                return;
            }

            btn_voltar.setVisible(true);
            designacao_categoria.setVisible(true);
            designacao_categoria.setText(tipoProduto.getDesignacao());
            System.err.println("Cod Tipo de Produtos: " + tipoProduto.getDesignacao());

            System.out.println("ID ARMAZEM: " + id_armzem);
            System.out.println("FAMILIA: " + familia.getPkFamilia());
            System.out.println("ID GRUPO: " + getIdGrupo());

            // 3️⃣ Buscar lista de produtos
            List<TbProduto> lista_produtos;
            if (familia.getPkFamilia() != DVML.COD_SERVICO) {
                lista_produtos = stocksController.get_all_produtos_by_id_tipo_produto_and_id_armazem_and_grupo(
                        idTipoProduto, id_armzem, getIdGrupo());
            } else {
                lista_produtos = produtosController.getProdutosByTipoProdutoAndIdGrupo(idTipoProduto, getIdGrupo());
            }

            if (lista_produtos == null || lista_produtos.isEmpty()) {
                System.out.println("LISTA DE PRODUTO VAZIA.");
                painel_central.removeAll();
                return;
            }

            // 4️⃣ Limpar painel e preparar layout
            painel_central.removeAll();
            jScrollPane3.setViewportView(painel_central);

            TbUsuario usuarioLocal = (TbUsuario) usuariosController.findById(idUser);
            if (usuarioLocal == null || usuarioLocal.getIdTipoUsuario() == null) {
                System.err.println("Usuário ou tipo de usuário não encontrado para id: " + idUser);
                return;
            }

            botoes_object.clear();
            this.TAMANHO_CATEGORIA = lista_produtos.size();
            System.out.println("TAMANHO CENTRO: " + TAMANHO_CATEGORIA);

            int raiz_quadrada = (int) Math.sqrt(TAMANHO_CATEGORIA);
            int linhas = raiz_quadrada > 0 ? raiz_quadrada : 1;
            int colunas = raiz_quadrada > 0 ? raiz_quadrada : 1;
            painel_central.setLayout(new java.awt.GridLayout(linhas, colunas));
            jScrollPane3.setViewportView(painel_central);

            // 5️⃣ Adicionar produtos ao painel
            for (TbProduto produto : lista_produtos) {
                TbPreco precoObject = precosController.getLastIdPrecoByIdProdutos(produto.getCodigo());
                double quantidadeProduto = stocksController.getQuantidadeProduto(produto.getCodigo(), id_armzem);

                painel_central.add(cartaoProduto(produto, precoObject));
            }

            painel_central.revalidate();
            painel_central.repaint();

        } catch (Exception e) {
            e.printStackTrace();
            painel_central.removeAll();
            botoes_object.clear();
        }
    }

    private void adicionar_centro_botao2(int idTipoPorduto) {

        try {

            System.out.println("ESTOU AQUI DALLAS");
            TbTipoProduto tipoProduto = tipoProdutosController.getTipoProdutoByCodigo(idTipoPorduto);
            btn_voltar.setVisible(true);
            designacao_categoria.setVisible(true);
            designacao_categoria.setText(tipoProduto.getDesignacao());
            System.err.println("Cod Tipo de Produtos: " + tipoProduto.getDesignacao());

//            Familia familia = familiaController.findById( 0 )
            System.out.println(" ID ARMAZEM: " + id_armzem);
            System.out.println(" FAMILIA: " + tipoProduto.getFkFamilia().getPkFamilia());
            System.out.println(" ID GRUPO : " + getIdGrupo());
            List<TbProduto> lista_prdutos = (tipoProduto.getFkFamilia().getPkFamilia() != DVML.COD_SERVICO)
                    ? stocksController.get_all_produtos_by_id_tipo_produto_and_id_armazem_and_grupo(idTipoPorduto, id_armzem, getIdGrupo())
                    : produtosController.getProdutosByTipoProdutoAndIdGrupo(idTipoPorduto, getIdGrupo());
            painel_central.removeAll();
            jScrollPane3.setViewportView(painel_central);
            TbUsuario usuarioLocal = (TbUsuario) usuariosController.findById(idUser);
            botoes_object.clear();
            this.TAMANHO_CATEGORIA = lista_prdutos.size();
            if (!Objects.isNull(lista_prdutos) && TAMANHO_CATEGORIA > 0) {

                boolean adcionar_imagem = true;

                System.out.println("TAMANHO CENTRO: " + TAMANHO_CATEGORIA);

                int raiz_quadrada = (int) Math.sqrt(TAMANHO_CATEGORIA);
                int linhas = raiz_quadrada;
                int colunas = raiz_quadrada;

                painel_central.setLayout(new java.awt.GridLayout(linhas, colunas));
                jScrollPane3.setViewportView(painel_central);

                double preco = 0;
                TbPreco preco_object;
                for (int i = 0; i < this.TAMANHO_CATEGORIA; i++) {
                    TbProduto get = lista_prdutos.get(i);

                    preco_object = precosController.getLastIdPrecoByIdProdutos(lista_prdutos.get(i).getCodigo());
                    double quantidadeProduto = stocksController.getQuantidadeProduto(get.getCodigo(), id_armzem);
                    painel_central.add(cartaoProduto(get, preco_object));

                }

            } else {
                System.out.println("LISTA DE PRODUTO VAZIA.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            painel_central.removeAll();
            botoes_object.clear();

        }

    }

    public class ButtonHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent evento) {

            for (int i = 0; i < TAMANHO_CATEGORIA; i++) {

                if (evento.getSource() == botoes_object.get(i)) {

                    String designacao = botoes_object.get(i).getText();

                    String[] parts = designacao.split("-");
                    designacao = parts[0];

//                    if ( activo_um_lugar() )
//                    {
                    if (qtd_possivel(designacao)) {
                        try {

                            procedimento_salvar_pedidos_iten_pedidos(designacao);
                            status_button_lugar(true);
                        } catch (Exception e) {
                            JOptionPane.showMessageDialog(null, "Fallha ao adicionar o pedido", DVML.DVML_COMERCIAL, JOptionPane.ERROR_MESSAGE);
                            e.printStackTrace();
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Nao existe quantidade no stock.", "Aviso", JOptionPane.WARNING_MESSAGE);
                    }

//                    }
                }

            }

        }

    }

    public class ButtonHandler1 implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent evento) {

            for (int i = 0; i < TAMANHO_CATEGORIA; i++) {

                if (evento.getSource() == botoes_object.get(i)) {

                    categoria = botoes_object.get(i).getText();
                    System.out.println("BOTOES :" + categoria);
//                    System.out.println( "ID : " + tipoProdutoDao.getCategoriaByDescricao( categoria ).getCodigo() );
//                    int cod_categoria = tipoProdutoDao.getCategoriaByDescricao( categoria ).getCodigo();
                    cod_categoria = tipoProdutosController.getTipoFamiliaByDesignacao(categoria).getCodigo();
                    adicionar_centro_botao(cod_categoria);
                    break;

                }

            }

        }

    }

    private void construir_painel_central() {
        painel_central.setLayout(new java.awt.GridLayout(10, 1));
        jScrollPane3.setViewportView(painel_central);
    }

    private void activar_button_lugar(boolean bt1, boolean bt2, boolean bt3, boolean bt4, boolean bt5, boolean bt6, boolean bt7, boolean bt8, boolean bt9, boolean bt10) {

        status_button_lugar(true);

    }

    private static void status_button_lugar(boolean status) {

    }

//    private void procedimento_eliminar_item_pedido()
//    {
//
//        if ( validar1() )
//        {
//            eliminar_item( jTable1 );
//            PrincipalPedidosVisao.mesas_livres( getLabelMesaByMesa() );
//            PrincipalPedidosVisao.pintar_mesas( getLabelMesaByMesa(), this.mesa );
//        }
//
//    }
    public void procedimento_eliminar_item_pedido() {
        linha_acutal = table.getSelectedRow();
        DefaultTableModel modelo = (DefaultTableModel) table.getModel();
//        int id_item_consumo_alojamento = Integer.parseInt( modelo.getValueAt( linha_acutal, 0 ).toString() );

        try {
            new SenhaMestreVisao(this, rootPaneCheckingEnabled, DVML.FORMULARIO_RECOLHA_LAVANDARIA).setVisible(true);
        } catch (Exception e) {
        }

    }

    public void procedimento_salver_item_pedido() {
        linha_acutal = table.getSelectedRow();
        DefaultTableModel modelo = (DefaultTableModel) table.getModel();
//        int id_item_consumo_alojamento = Integer.parseInt( modelo.getValueAt( linha_acutal, 0 ).toString() );
        try {

//            new SenhaMestreVisao( this, rootPaneCheckingEnabled ).setVisible( true );
//                if ( validar1() )
//                {
//                    eliminar_item( jTable1 );
//                    PrincipalPedidosVisao.mesas_livres( getLabelMesaByMesa() );
//                    PrincipalPedidosVisao.pintar_mesas( getLabelMesaByMesa(), mesa );
//                }
//                ItemAlojamentoConsumo item = itemAlojamentoDao.findItemAlojamentoConsumo( id_item_consumo_alojamento );
//                //chamar o
//                itemAlojamentoDao.destroy( id_item_consumo_alojamento );
//                if ( item.getFkProduto().getStocavel().equals( "true" ) )
//                {
//                    MetodosUtil.adiciona_quantidade( item.getFkProduto().getCodigo(), item.getQtd(), DVML.ARMAZEM_DEFAUTL, conexao );
//                }
//                adicionar_consumos_tabela();
//                JOptionPane.showMessageDialog( null, "Produto removido com sucesso!...", DVML.DVML_COMERCIAL, JOptionPane.INFORMATION_MESSAGE );
        } catch (Exception e) {
        }

    }

    public static void procedimento_eliminar_item_pedido(String chave_mestre) {
        linha_actual = table.getSelectedRow();
        DefaultTableModel modelo = (DefaultTableModel) table.getModel();
        String lugar = modelo.getValueAt(linha_actual, 1).toString();
        String consumo = modelo.getValueAt(linha_actual, 2).toString().split("-")[0];
        int idProduto = produtosController.getIdProduto(consumo);
        double qtd = Double.parseDouble(modelo.getValueAt(linha_actual, 3).toString());
//ESTE
//        double preco_unitario = precoDao.findTbPreco( precoDao.getUltimoIdPrecoByIdProduto( idProduto, qtd ) ).getPrecoVenda().doubleValue();
        double preco_unitario = precosController.getLastIdPrecoByIdProduto(idProduto, qtd).getPrecoVenda().doubleValue();
//        String usuario = usuarioDao.findTbUsuario( idUser ).getNome();
        String usuario = usuariosController.getUsuarioByCodigo(idUser).getNome();

        String chave_mestre_local = dadosInstituicaoController.findByCodigo(1).getChaveMestre();
//        String chave_mestre_local = dadosInstituicaoController.findByCodigo( 1 ).getChaveMestre();
        System.out.println("Chave Apresentação1: " + chave_mestre);
        System.out.println("Chave Apresentação2 sem chave: " + chave_mestre_local);
        if (chave_mestre.equals(chave_mestre_local)) {
            System.out.println("Chave Apresentação2: " + chave_mestre);
            System.out.println("Chave Real: " + chave_mestre);
            try {
                if (validar1()) {
                    eliminar_item(table);
//                    PrincipalPedidosVisao.mesas_livres( getLabelMesaByMesa() );
//                    PrincipalPedidosVisao.pintar_mesas( getLabelMesaByMesa(), mesa );

                    Productos p = new Productos();
                    p.setConsumo(consumo);
                    p.setLugar(lugar);
                    p.setMesa(mesa);
                    p.setQtd(qtd);
                    p.setPreco(preco_unitario);
                    p.setUsuario(usuario);
                    p.setDataHora(new Date());

                    if (ProductoDao.insert(p, conexao)) {
                        System.err.println("dados eliminado registrado com successo.");
                    } else {
                        System.err.println("Falha ao registrar os dados eliminados");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(null, "Accesso negado.");
        }

    }

//    public static void procedimento_eliminar_item_pedido( String chave_mestre )
//    {
//        linha_acutal = jTable1.getSelectedRow();
//        DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
//        String chave_mestre_local;
//        chave_mestre_local = dadosInstituicaoDao.findTbDadosInstituicao( 1 ).getChaveMestre();
//
    ////        int id_item_consumo_alojamento = Integer.parseInt( modelo.getValueAt( linha_acutal, 0 ).toString() );
//        TbDadosInstituicao dadosInstituicao = new TbDadosInstituicao();
//        if ( chave_mestre.equals( chave_mestre_local ) )
//        {
//            try
//            {
//                if ( validar1() )
//                {
//                    eliminar_item( jTable1 );
//                    PrincipalPedidosVisao.mesas_livres( getLabelMesaByMesa() );
//                    PrincipalPedidosVisao.pintar_mesas( getLabelMesaByMesa(), mesa );
//                }
////                ItemAlojamentoConsumo item = itemAlojamentoDao.findItemAlojamentoConsumo( id_item_consumo_alojamento );
////                //chamar o
////                itemAlojamentoDao.destroy( id_item_consumo_alojamento );
//
////                if ( item.getFkProduto().getStocavel().equals( "true" ) )
////                {
////                    MetodosUtil.adiciona_quantidade( item.getFkProduto().getCodigo(), item.getQtd(), DVML.ARMAZEM_DEFAUTL, conexao );
////                }
////                adicionar_consumos_tabela();
////                JOptionPane.showMessageDialog( null, "Produto removido com sucesso!...", DVML.DVML_COMERCIAL, JOptionPane.INFORMATION_MESSAGE );
//            }
//            catch ( Exception e )
//            {
//            }
//        }
//        else
//        {
//            JOptionPane.showMessageDialog( null, "Accesso negado." );
//        }
//
//    }
//    public static void procedimento_gravar_item_pedido_eliminado()
//    {
//        linha_acutal = jTable1.getSelectedRow();
//        DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
//
////        int id_item_consumo_alojamento = Integer.parseInt( modelo.getValueAt( linha_acutal, 0 ).toString() );
//        try
//        {
//            if ( validar1() )
//            {
//                gravar_item_eliminado( jTable1 );
//
//            }
////                ItemAlojamentoConsumo item = itemAlojamentoDao.findItemAlojamentoConsumo( id_item_consumo_alojamento );
////                //chamar o
////                itemAlojamentoDao.destroy( id_item_consumo_alojamento );
//
////                if ( item.getFkProduto().getStocavel().equals( "true" ) )
////                {
////                    MetodosUtil.adiciona_quantidade( item.getFkProduto().getCodigo(), item.getQtd(), DVML.ARMAZEM_DEFAUTL, conexao );
////                }
////                adicionar_consumos_tabela();
////                JOptionPane.showMessageDialog( null, "Produto removido com sucesso!...", DVML.DVML_COMERCIAL, JOptionPane.INFORMATION_MESSAGE );
//        }
//        catch ( Exception e )
//        {
//        }
//
//    }
//    private static boolean activo_um_lugar()
//    {
//
//        JOptionPane.showMessageDialog( null, "Caro usuário seleccione pelo menos um lugar da " + mesa, DVML.DVML_COMERCIAL, JOptionPane.WARNING_MESSAGE );
//
//        return false;
//
//    }
    public static int getQuantidadeProduto(int cod_produto) {

        String sql = "SELECT quantidade_existente FROM  tb_stock WHERE  cod_produto_codigo = " + cod_produto + " AND cod_armazem = " + id_armzem;

        ResultSet rs = conexao.executeQuery(sql);

        try {
            if (rs.next()) {
                return rs.getInt("quantidade_existente");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return 0;
        }

        return 0;
    }
//    public int getQuantidadeProduto( int cod_produto )
//    {
//

    ////        String sql = "SELECT quantidade_existente FROM  tb_stock WHERE  cod_produto_codigo = "  +cod_produto +" AND cod_armazem = " +getCodigoArmazem(); 
//        String sql = "SELECT quantidade_existente FROM  tb_stock WHERE  cod_produto_codigo = " + cod_produto;
//
//        ResultSet rs = conexao.executeQuery( sql );
//
//        try
//        {
//            if ( rs.next() )
//            {
//                return rs.getInt( "quantidade_existente" );
//            }
//        }
//        catch ( SQLException ex )
//        {
//            ex.printStackTrace();
//            return 0;
//        }
//
//        return 0;
//    }

    public void actualizar_quantidade(int cod, int quantidade, int idArmazem) {
//        public  void actualizar_quantidade(int cod, int quantidade) {

        String sql = "UPDATE tb_stock SET quantidade_existente =  " + (getQuantidadeProduto(cod) - quantidade) + " WHERE cod_produto_codigo = " + cod + " AND cod_armazem = " + idArmazem;
//        String sql = "UPDATE tb_stock SET quantidade_existente =  "  + ( getQuantidadeProduto(cod) - quantidade)     +" WHERE cod_produto_codigo = "   +cod;

        conexao.executeUpdate(sql);

    }

//    public static boolean possivel_quantidade( int codigo_produto )
//    {
//
//        System.err.println( conexao.getQuantidade_Existente_Publico( codigo_produto, 1 ) );
//        // TbStock stock =  stockDao.getStockByDescricao(codigo_produto, 1 );
//        System.err.println( "ID ARMAZEM : " + id_armzem );
//        double quant_possivel = 0d;
//        try
//        {
//            quant_possivel = ( conexao.getQuantidade_Existente_Publico( codigo_produto, getCodigoArmazem() ) - conexao.getQuantidade_minima_publico( codigo_produto, getCodigoArmazem() ) );
//        }
//        catch ( Exception e )
//        {
//            e.printStackTrace();
//        }
//
//        // int quant_possivel = stock.getQuantidadeExistente() -  stock.getQuantBaixa();
//        System.out.println( "Quantidade Possivel: " + quant_possivel );
//        System.out.println( "Quantidade Itens Pedidos: " + MetodosUtil.getQtdInItemPedidos( conexao, codigo_produto ) );
//        return quant_possivel >= ( 1 + MetodosUtil.getQtdInItemPedidos( conexao, codigo_produto ) );
//
//    }
    public static boolean estado_critico(int codigo_produto) throws SQLException {
        TbStock stock = stocksController.getStockByIdProdutoAndIdArmazem(codigo_produto, id_armzem);
        double qtd_minima = stock.getQuantBaixa(),
                qtd_existente = stock.getQuantidadeExistente(),
                qtd_critica = stock.getQuantCritica();

        return qtd_minima < qtd_existente
                && qtd_existente <= qtd_critica;

    }

    //AQUI
//    private boolean verificar_disponiblidade ( int lugar )
//    {
//
//        int codigo_produto = 0, opcao = 0, diferenca = 0;
//
//        for ( int i = 0; i < jTable1.getRowCount (); i ++ )
//        {
//
//            if ( lugarDao.getIdByDescricao ( jTable1.getModel ().getValueAt ( i, 1 ).toString () ) == lugar )
//            {
//                codigo_produto = Integer.parseInt ( String.valueOf ( jTable1.getModel ().getValueAt ( i, 0 ) ) );
//
//                if (  ! possivel_quantidade ( getQtdPedidosTabela ( codigo_produto ) ) )
//                {
//
//                    diferenca = ( MetodosUtil.getQtdInItemPedidos ( conexao, codigo_produto ) - qtd_diferenca_existente_baixa ( codigo_produto ) );
//                    remover_item_all_pedidos_amais_tabela ( codigo_produto, diferenca );
//                    opcao = JOptionPane.showConfirmDialog ( null, "De momento encontra-se apenas disponível a quantidade "
//                            + qtd_possivel_quantidade ( codigo_produto ) + " do produto " + produtoDao.findTbProduto ( codigo_produto ).getDesignacao () );
//                    return false;
//                }
//            }
//
//        }
//        return true;
//
//    }
    public int getQtdPedidosTabela(int cod_produto) {

        DefaultTableModel modelo = (DefaultTableModel) table.getModel();
        int soma = 0;
        int cod_produto_local = 0;

        for (int i = 0; i < modelo.getRowCount(); i++) {
            cod_produto_local = Integer.parseInt(modelo.getValueAt(i, 0).toString());
            if (cod_produto_local == cod_produto) {
                soma += Integer.parseInt(modelo.getValueAt(i, 3).toString());
            }
        }

        return soma;

    }

    public void remover_item_all_pedidos_tabela(int cod_produto) {

        DefaultTableModel modelo = (DefaultTableModel) table.getModel();
        int soma = 0;
        int cod_produto_local = 0;

        for (int i = 0; i < modelo.getRowCount(); i++) {
            cod_produto_local = Integer.parseInt(modelo.getValueAt(i, 0).toString());
            if (cod_produto_local == cod_produto) {
                modelo.removeRow(i);
            }
        }

    }

    public double qtd_possivel_quantidade(int codigo_produto) {

        TbStock stock = stocksController.getStockByIdProdutoAndIdArmazem(codigo_produto, 1);
        double quant_possivel = stock.getQuantidadeExistente() - stock.getQuantBaixa();

        return quant_possivel + (1 + MetodosUtil.getQtdInItemPedidos(conexao, codigo_produto));

    }

    public void remover_item_all_pedidos_amais_tabela(int cod_produto, int qtd) {

        DefaultTableModel modelo = (DefaultTableModel) table.getModel();
        int cont = 1;
        int cod_produto_local = 0;

        for (int i = 0; i < modelo.getRowCount(); i++) {
            cod_produto_local = Integer.parseInt(modelo.getValueAt(i, 0).toString());
            if (cod_produto_local == cod_produto && cont <= qtd) {
                modelo.removeRow(i);
                cont++;
            } else {
                break;
            }
        }

    }

//    private double qtd_diferenca_existente_baixa( int codigo_produto )
//    {
//        TbStock stock = stockDao.getStockByDescricao( codigo_produto, 1 );
//        return ( stock.getQuantidadeExistente() - stock.getQuantBaixa() );
//
//    }
    private static JLabel getLabelMesaByMesa() {

        if (mesa.equals("MESA 1")) {
            return PrincipalPedidosVisao.lb_mesa_01;
        } else if (mesa.equals("MESA 2")) {
            return PrincipalPedidosVisao.lb_mesa_02;
        } else if (mesa.equals("MESA 3")) {
            return PrincipalPedidosVisao.lb_mesa_03;
        } else if (mesa.equals("MESA 4")) {
            return PrincipalPedidosVisao.lb_mesa_04;
        } else if (mesa.equals("MESA 5")) {
            return PrincipalPedidosVisao.lb_mesa_05;
        } else if (mesa.equals("MESA 6")) {
            return PrincipalPedidosVisao.lb_mesa_06;
        } else if (mesa.equals("MESA 7")) {
            return PrincipalPedidosVisao.lb_mesa_07;
        } else if (mesa.equals("MESA 8")) {
            return PrincipalPedidosVisao.lb_mesa_08;
        } else if (mesa.equals("MESA 9")) {
            return PrincipalPedidosVisao.lb_mesa_09;
        } else if (mesa.equals("MESA 10")) {
            return PrincipalPedidosVisao.lb_mesa_10;
        } else if (mesa.equals("MESA 11")) {
            return PrincipalPedidosVisao.lb_mesa_11;
        } else if (mesa.equals("MESA 12")) {
            return PrincipalPedidosVisao.lb_mesa_12;
        } else if (mesa.equals("MESA 13")) {
            return PrincipalPedidosVisao.lb_mesa_13;
        } else if (mesa.equals("MESA 14")) {
            return PrincipalPedidosVisao.lb_mesa_14;
        } else if (mesa.equals("MESA 15")) {
            return PrincipalPedidosVisao.lb_mesa_15;
        } else if (mesa.equals("MESA 16")) {
            return PrincipalPedidosVisao.lb_mesa_16;
        } else if (mesa.equals("MESA 17")) {
            return PrincipalPedidosVisao.lb_mesa_17;
        } else if (mesa.equals("MESA 18")) {
            return PrincipalPedidosVisao.lb_mesa_18;
        } else if (mesa.equals("MESA 19")) {
            return PrincipalPedidosVisao.lb_mesa_19;
        } else if (mesa.equals("MESA 20")) {
            return PrincipalPedidosVisao.lb_mesa_20;
        } else if (mesa.equals("MESA 21")) {
            return PrincipalPedidosVisao.lb_mesa_21;
        } else if (mesa.equals("MESA 22")) {
            return PrincipalPedidosVisao.lb_mesa_22;
        } else if (mesa.equals("MESA 23")) {
            return PrincipalPedidosVisao.lb_mesa_23;
        } else if (mesa.equals("MESA 24")) {
            return PrincipalPedidosVisao.lb_mesa_24;
        } else if (mesa.equals("MESA 25")) {
            return PrincipalPedidosVisao.lb_mesa_25;
        } else if (mesa.equals("MESA 26")) {
            return PrincipalPedidosVisao.lb_mesa_26;
        } else if (mesa.equals("MESA 27")) {
            return PrincipalPedidosVisao.lb_mesa_27;
        } else if (mesa.equals("MESA 28")) {
            return PrincipalPedidosVisao.lb_mesa_28;
        } else if (mesa.equals("MESA 29")) {
            return PrincipalPedidosVisao.lb_mesa_29;
        } else if (mesa.equals("MESA 30")) {
            return PrincipalPedidosVisao.lb_mesa_30;
        } else if (mesa.equals("MESA 31")) {
            return PrincipalPedidosVisao.lb_mesa_31;
        } else if (mesa.equals("MESA 32")) {
            return PrincipalPedidosVisao.lb_mesa_32;
        } else if (mesa.equals("MESA 33")) {
            return PrincipalPedidosVisao.lb_mesa_33;
        } else if (mesa.equals("MESA 34")) {
            return PrincipalPedidosVisao.lb_mesa_34;
        } else if (mesa.equals("MESA 35")) {
            return PrincipalPedidosVisao.lb_mesa_35;
        } else if (mesa.equals("MESA 36")) {
            return PrincipalPedidosVisao.lb_mesa_36;
        } else if (mesa.equals("MESA 37")) {
            return PrincipalPedidosVisao.lb_mesa_37;
        } else if (mesa.equals("MESA 38")) {
            return PrincipalPedidosVisao.lb_mesa_38;
        } else if (mesa.equals("MESA 39")) {
            return PrincipalPedidosVisao.lb_mesa_39;
        } else {
            System.out.println("CHEGUEI AQUI NA MESA 40.");
            return PrincipalPedidosVisao.lb_mesa_40;
        }

    }

    public static boolean validar1() {

        if (table.getModel().getRowCount() < 0) {
            JOptionPane.showMessageDialog(null, "Atenção\nNão existe itens na tabela!");
            return false;
        }
        return true;
    }

//    private boolean validar_cliente()
//    {
//        if ( cmbCliente.getSelectedIndex() == 0 )
//        {
//            JOptionPane.showMessageDialog( null, "Atenção\nSeleccione a Entidade ou o Cliente!..." );
//            return true;
//        }
//        else
//        {
//            return true;
//        }
//
//    }
//    private boolean validar_forma_pagamento()
//    {
//        if ( cmbFormaPagamento.getSelectedIndex() == 0 )
//        {
//            JOptionPane.showMessageDialog( null, "Atenção\nSeleccione a Forma de Pagamento!..." );
//            return true;
//        }
//        else
//        {
//            return true;
//        }
//
//    }
//    private Integer getIdBanco()
//    {
//        return bancoDao.getIdByDescricao( String.valueOf( cmbFormaPagamento.getSelectedItem() ) );
//    }
//    private void mostrar_proximo_codigo_documento ()
//    {
//        try
//        {
//            this.anoEconomico = anoEconomicoDao.getLastObject ();
//            this.documento = documentoDao.findDocumento ( DOC_FACTURA_RECIBO_FR );
//            this.doc_prox_cod = documento.getCodUltimoDoc () + 1;
//            prox_doc = documento.getAbreviacao ();
//            //FA Série / codigo
//            prox_doc += " " + this.anoEconomico.getSerie () + "/" + this.doc_prox_cod;
//            //lb_proximo_documento.setText( "PRÓXIMO DOC. :" + prox_doc );
//        }
//        catch ( Exception e )
//        {
//            e.printStackTrace ();
//            this.documento = null;
//            //lb_proximo_documento.setText( "" );
//        }
//    }
//    private void mostrar_proximo_codigo_documento()
//    {
//        try
//        {
//            this.documento = documentoDao.findDocumento( getIdDocumento() );
//            this.anoEconomico = anoEconomicoDao.findAnoEconomico( getIdAnoEconomico() );
//            // this.doc_prox_cod = documento.getCodUltimoDoc() + 1;
//            this.doc_prox_cod = vendaDao.getUltimaContagemByIdDocumentoAndAnoEconomico( getIdDocumento(), getIdAnoEconomico(), conexao ) + 1;
//            prox_doc = documento.getAbreviacao();
//            //FA Série / codigo
//            prox_doc += " " + this.anoEconomico.getSerie() + "/" + this.doc_prox_cod;
//            lb_proximo_documento.setText( "PRÓX.DOC. :" + prox_doc );
//        }
//        catch ( Exception e )
//        {
//            this.documento = null;
//            lb_proximo_documento.setText( "" );
//        }
//    }
    private void mostrar_proximo_codigo_documento() {
        try {
            this.documento = (Documento) documentosController.findById(getIdDocumento());
            this.anoEconomico = (AnoEconomico) anoEconomicoController.findById(getIdAnoEconomico());
            this.doc_prox_cod = vendasController.getUltimaContagemByIdDocumentoAndAnoEconomico(
                    getIdDocumento(), getIdAnoEconomico()) + 1;
            prox_doc = documento.getAbreviacao();
            //FA Série / codigo
            prox_doc += " " + this.anoEconomico.getSerie() + "/" + this.doc_prox_cod;
            lb_proximo_documento.setText("PRÓX.DOC. :" + prox_doc);
        } catch (Exception e) {
            this.documento = null;
            lb_proximo_documento.setText("");
        }
    }

//    public static int getIdDocumento()
//    {
//        try
//        {
//            return DVML.DOC_FACTURA_RECIBO_FR;
//        }
//        catch ( Exception e )
//        {
//            return 0;
//        }
//    }
    public static int getIdDocumento() {
        try {
            Documento documentoLocal = documentosController.getDocumentoByDesignacao(cmbTipoDocumento.getSelectedItem().toString());
            return documentoLocal.getPkDocumento();
        } catch (Exception e) {
            return 0;
        }
    }

    public static int getIdAnoEconomico() {
        try {
            AnoEconomico anoEconomicoLocal = anoEconomicoController
                    .getAnoEconomicoByDesignacao(cmbAnoEconomico.getSelectedItem().toString());
            return anoEconomicoLocal.getPkAnoEconomico();
        } catch (Exception e) {
            return 0;
        }
    }

    private static double getTotalIliquido() {
        DefaultTableModel modelo = (DefaultTableModel) table.getModel();
        double qtd = 0;
        int idProduto = 0;
        double total_iliquido = 0, preco_unitario = 0;

        for (int i = 0; i < modelo.getRowCount(); i++) {
            String servico = modelo.getValueAt(i, 2).toString().split("-")[0];
            idProduto = produtosController.findByDesignacao(servico).getCodigo();
//            idProduto = produtoDao.getProdutoByDescricao( modelo.getValueAt( i, 2 ).toString() ).getCodigo();
            qtd = Double.parseDouble(modelo.getValueAt(i, 3).toString());
//ESTE
//            preco_unitario = precoDao.findTbPreco( precoDao.getUltimoIdPrecoByIdProduto( idProduto, qtd ) ).getPrecoVenda().doubleValue();
            preco_unitario = precosController.getLastIdPrecoByIdProduto(idProduto, qtd).getPrecoVenda().doubleValue();
            System.err.println("PU: " + preco_unitario);
            System.err.println("QTD: " + qtd);
            total_iliquido += (preco_unitario * qtd);

        }

        System.out.println("TOTAL_GERAL (ILÍQUIDO):" + total_iliquido);
        return total_iliquido;
    }

    private static double getTotalIliquido(int lugar) {
        DefaultTableModel modelo = (DefaultTableModel) table.getModel();
        int qtd = 0, idProduto = 0;
        double total_iliquido = 0, preco_unitario = 0;

        for (int i = 0; i < modelo.getRowCount(); i++) {

            int valor_lugar_tabela = getLugarSelecionado(i);
            if (valor_lugar_tabela == lugar) {

                String servico = modelo.getValueAt(i, 2).toString().split("-")[0];
                idProduto = produtosController.findByDesignacao(servico).getCodigo();
                qtd = Integer.parseInt(modelo.getValueAt(i, 3).toString());
//                ESTE
//                preco_unitario = precoDao.findTbPreco( precoDao.getUltimoIdPrecoByIdProduto( idProduto, qtd ) ).getPrecoVenda().doubleValue();
                preco_unitario = precosController.getLastIdPrecoByIdProduto(idProduto, qtd).getPrecoVenda().doubleValue();
                System.err.println("PU: " + preco_unitario);
                System.err.println("QTD: " + qtd);
                total_iliquido += (preco_unitario * qtd);
            }
        }

        return total_iliquido;
    }

    private static double getDescontoComercial() {
        return 0d;
    }

    private static double getDescontoComercial(int lugar) {
        return 0d;
    }

    private static double getDescontoFinanceiro() {

        DefaultTableModel modelo = (DefaultTableModel) table.getModel();
        double valorPercentual = (getDescontoClientePercentagem() / 100);
        double desconto = (getTotalIliquido() * valorPercentual);
        return desconto;
    }

    private static double getDescontoFinanceiro(int lugar) {
        return 0d;
    }

    private static double getTotalImposto() {
        DefaultTableModel modelo = (DefaultTableModel) table.getModel();
        double qtd = 0;
        int idProduto = 0;
        double imposto = 0d, preco_unitario = 0d, desconto_valor_linha = 0;

        for (int i = 0; i < modelo.getRowCount(); i++) {
            String servico = modelo.getValueAt(i, 2).toString().split("-")[0];
            idProduto = produtosController.findByDesignacao(servico).getCodigo();
            qtd = Double.parseDouble(modelo.getValueAt(i, 3).toString());
            preco_unitario = precosController.getLastIdPrecoByIdProduto(idProduto, qtd).getPrecoVenda().doubleValue();
            double valor_percentagem = 0d;
            double taxa = MetodosUtil.getTaxaPercantagem(idProduto);
            // a incidência só é aplicável ao produtos sujeitos a iva 
            if (taxa != 0) {
                double valor_unitario = (preco_unitario * qtd);
                desconto_valor_linha = valor_unitario * ((valor_percentagem) / 100);
                imposto += ((valor_unitario - desconto_valor_linha) * (taxa / 100));

            }

        }

        return imposto;
    }

    private static double getTotalImposto(int lugar) {
        DefaultTableModel modelo = (DefaultTableModel) table.getModel();
        double qtd = 0;
        int idProduto = 0;
        double imposto = 0d, preco_unitario = 0d, desconto_valor_linha = 0;

        for (int i = 0; i < modelo.getRowCount(); i++) {
            int valor_lugar_tabela = getLugarSelecionado(i);
            if (valor_lugar_tabela == lugar) {

                String servico = modelo.getValueAt(i, 2).toString().split("-")[0];
                idProduto = produtosController.findByDesignacao(servico).getCodigo();
                qtd = Double.parseDouble(modelo.getValueAt(i, 3).toString());
                preco_unitario = precosController.getLastIdPrecoByIdProduto(idProduto, qtd).getPrecoVenda().doubleValue();
                double valor_percentagem = 0d;
                double taxa = MetodosUtil.getTaxaPercantagem(idProduto);
                // a incidência só é aplicável ao produtos sujeitos a iva 
                if (taxa != 0) {
                    double valor_unitario = (preco_unitario * qtd);
                    desconto_valor_linha = valor_unitario * ((valor_percentagem) / 100);
                    imposto += ((valor_unitario - desconto_valor_linha) * (taxa / 100));

                }
            }

        }

        return imposto;
    }

    private static double getTotalAOALiquido() {
        double valores = (getTotalIliquido() + getTotalImposto());
        double descontos = (getDescontoComercial() + getDescontoFinanceiro());
        System.out.println("TotalIliquido: " + getTotalIliquido());
        System.out.println("TotalImposto: " + getTotalImposto());
        System.out.println("TotalDescontoComercial: " + getDescontoComercial());
        System.out.println("TotalDescontoFinanceiro: " + getDescontoFinanceiro());
        System.out.println("Total Liquido: " + (valores - descontos));
        return (valores - descontos);
    }

    private static double getTotalAOALiquido(int lugar) {
        double valores = (getTotalIliquido(lugar) + getTotalImposto(lugar));
        double descontos = (getDescontoComercial(lugar) + getDescontoFinanceiro(lugar));
        System.out.println("TotalIliquido: " + getTotalIliquido(lugar));
        System.out.println("TotalImposto: " + getTotalImposto(lugar));
        System.out.println("TotalDescontoComercial: " + getDescontoComercial(lugar));
        System.out.println("TotalDescontoFinanceiro: " + getDescontoFinanceiro(lugar));
        System.out.println("Total Liquido: " + (valores - descontos));
        return (valores - descontos);
    }

    private static double getTotalIncidencia() {
        DefaultTableModel modelo = (DefaultTableModel) table.getModel();
        double qtd = 0;
        int idProduto = 0;
        double incidencia = 0d, preco_unitario = 0d, desconto_valor_linha = 0, taxa = 0d;

        for (int i = 0; i < modelo.getRowCount(); i++) {

            String servico = modelo.getValueAt(i, 2).toString().split("-")[0];
            idProduto = produtosController.findByDesignacao(servico).getCodigo();
            qtd = Double.parseDouble(modelo.getValueAt(i, 3).toString());
            preco_unitario = precosController.getLastIdPrecoByIdProduto(idProduto, qtd).getPrecoVenda().doubleValue();

            double valor_percentagem = 0d;
            taxa = MetodosUtil.getTaxaPercantagem(idProduto);
            // a incidência só é aplicável ao produtos sujeitos a iva 
            if (taxa != 0) {
                desconto_valor_linha = ((valor_percentagem) / 100);
                double valor_unitario = (preco_unitario * qtd);
                incidencia += ((valor_unitario) - (valor_unitario * desconto_valor_linha));

            }

        }

        return incidencia;
    }

    private static double getTotalIncidencia(int lugar) {
        DefaultTableModel modelo = (DefaultTableModel) table.getModel();
        double qtd = 0;
        int idProduto = 0;
        double incidencia = 0d, preco_unitario = 0d, desconto_valor_linha = 0, taxa = 0d;

        for (int i = 0; i < modelo.getRowCount(); i++) {

            int valor_lugar_tabela = getLugarSelecionado(i);
            if (valor_lugar_tabela == lugar) {
                String servico = modelo.getValueAt(i, 2).toString().split("-")[0];
                idProduto = produtosController.findByDesignacao(servico).getCodigo();
                qtd = Double.parseDouble(modelo.getValueAt(i, 3).toString());
                preco_unitario = precosController.getLastIdPrecoByIdProduto(idProduto, qtd).getPrecoVenda().doubleValue();

                double valor_percentagem = 0d;
                taxa = MetodosUtil.getTaxaPercantagem(idProduto);
                // a incidência só é aplicável ao produtos sujeitos a iva 
                if (taxa != 0) {
                    desconto_valor_linha = ((valor_percentagem) / 100);
                    double valor_unitario = (preco_unitario * qtd);
                    incidencia += ((valor_unitario) - (valor_unitario * desconto_valor_linha));

                }
            }

        }

        return incidencia;
    }

    private static double getTotalIncidenciaIsento() {
        DefaultTableModel modelo = (DefaultTableModel) table.getModel();
        double qtd = 0;
        int idProduto = 0;
        double incidencia_isento = 0d, preco_unitario = 0d, desconto_valor_linha = 0, taxa = 0;

        for (int i = 0; i < modelo.getRowCount(); i++) {
            String servico = modelo.getValueAt(i, 2).toString().split("-")[0];
            idProduto = produtosController.findByDesignacao(servico).getCodigo();
            qtd = Double.parseDouble(modelo.getValueAt(i, 3).toString());
            preco_unitario = precosController.getLastIdPrecoByIdProduto(idProduto, qtd).getPrecoVenda().doubleValue();

            double valor_percentagem = 0d;
            taxa = MetodosUtil.getTaxaPercantagem(idProduto);
            // a incidência também é aplicável à produtos isentos do iva 
            if (taxa == 0) {
                desconto_valor_linha = ((valor_percentagem) / 100);
                double valor_unitario = (preco_unitario * qtd);
                incidencia_isento += ((valor_unitario) - (valor_unitario * desconto_valor_linha));

            }

        }

        return incidencia_isento;
    }

    private static double getTotalIncidenciaIsento(int lugar) {
        DefaultTableModel modelo = (DefaultTableModel) table.getModel();
        double qtd = 0;
        int idProduto = 0;
        double incidencia_isento = 0d, preco_unitario = 0d, desconto_valor_linha = 0, taxa = 0;

        for (int i = 0; i < modelo.getRowCount(); i++) {
            int valor_lugar_tabela = getLugarSelecionado(i);
            if (valor_lugar_tabela == lugar) {
                String servico = modelo.getValueAt(i, 2).toString().split("-")[0];
                idProduto = produtosController.findByDesignacao(servico).getCodigo();
                qtd = Double.parseDouble(modelo.getValueAt(i, 3).toString());
                preco_unitario = precosController.getLastIdPrecoByIdProduto(idProduto, qtd).getPrecoVenda().doubleValue();

                double valor_percentagem = 0d;
                taxa = MetodosUtil.getTaxaPercantagem(idProduto);
                // a incidência também é aplicável à produtos isentos do iva 
                if (taxa == 0) {
                    desconto_valor_linha = ((valor_percentagem) / 100);
                    double valor_unitario = (preco_unitario * qtd);
                    incidencia_isento += ((valor_unitario) - (valor_unitario * desconto_valor_linha));

                }
            }

        }

        return incidencia_isento;
    }

    private static double getGrossTotal() {
        System.out.println("TOTALILIQUIDO: " + getTotalVendaIVASemIncluirDesconto());
        System.out.println("TOTALVENDAIVASEMDESCONTO: " + getTotalVendaIVASemIncluirDesconto());
        return getTotalIliquido() + getTotalVendaIVASemIncluirDesconto();
    }

    private static double getGrossTotal(int lugar) {
        System.out.println("TOTALILIQUIDO: " + getTotalVendaIVASemIncluirDesconto(lugar));
        System.out.println("TOTALVENDAIVASEMDESCONTO: " + getTotalVendaIVASemIncluirDesconto(lugar));
        return getTotalIliquido(lugar) + getTotalVendaIVASemIncluirDesconto(lugar);
    }

    private static double getTotalVendaIVASemIncluirDesconto() {
        double taxa = 0, total_iva_local = 0, preco_unitario = 0, sub_total_iliquido = 0;
        double qtd = 0;
        int idProduto = 0;

        DefaultTableModel modelo = (DefaultTableModel) table.getModel();
        for (int i = 0; i < modelo.getRowCount(); i++) {
            String servico = modelo.getValueAt(i, 2).toString().split("-")[0];
            System.out.println("SEVICO: " + servico);
            idProduto = produtosController.findByDesignacao(servico).getCodigo();
            qtd = Double.parseDouble(modelo.getValueAt(i, 3).toString());
            preco_unitario = precosController.getLastIdPrecoByIdProduto(idProduto, qtd).getPrecoVenda().doubleValue();
            sub_total_iliquido = (preco_unitario * qtd);
            taxa = MetodosUtil.getTaxaPercantagem(idProduto);
            total_iva_local += (((sub_total_iliquido) * (taxa / 100)));
        }

        return total_iva_local;
    }

    private static double getTotalVendaIVASemIncluirDesconto(int lugar) {
        double taxa = 0, total_iva_local = 0, preco_unitario = 0, sub_total_iliquido = 0;
        double qtd = 0;
        int idProduto = 0;

        DefaultTableModel modelo = (DefaultTableModel) table.getModel();
        for (int i = 0; i < modelo.getRowCount(); i++) {
            int valor_lugar_tabela = getLugarSelecionado(i);
            if (valor_lugar_tabela == lugar) {
                String servico = modelo.getValueAt(i, 2).toString().split("-")[0];
                idProduto = produtosController.findByDesignacao(servico).getCodigo();
                qtd = Double.parseDouble(modelo.getValueAt(i, 3).toString());
                preco_unitario = precosController.getLastIdPrecoByIdProduto(idProduto, qtd).getPrecoVenda().doubleValue();
                sub_total_iliquido = (preco_unitario * qtd);
                taxa = MetodosUtil.getTaxaPercantagem(idProduto);
                total_iva_local += (((sub_total_iliquido) * (taxa / 100)));
            }
        }

        return total_iva_local;
    }

//    private void actualizar_cod_doc( Date data_documento )
//    {
//        this.documento.setCodUltimoDoc( this.doc_prox_cod );
//        this.documento.setDescricaoUltimoDoc( this.prox_doc );
//        this.documento.setUltimaData( data_documento );
//        try
//        {
//            documentoDao.edit( documento );
//        }
//        catch ( Exception e )
//        {
//            System.err.println( "Falha ao actualizar o documento" );
//        }
//    }
    public static int getLugarSelecionado(int linha) {
        DefaultTableModel modelo = (DefaultTableModel) table.getModel();
        String lugar_valor_tabela = modelo.getValueAt(linha, 1).toString();
        return lugaresController.getIdByDescricao(lugar_valor_tabela);
//        return lugarDao.getIdByDescricao( lugar_valor_tabela );

    }

    public static void verificarCaixa() {

        if (!caixasController.existeCaixas()) {
            BT_Conversao.setEnabled(false);
            status_mensagem_primaria.setText("");
        } else if (caixasController.existe_abertura() && caixasController.existe_fecho()) {
            BT_Conversao.setEnabled(false);
            BT_Conversao.setEnabled(false);
            status_mensagem_primaria.setText("Deves abrir o caixa");

        } else {
            BT_Conversao.setEnabled(true);
            BT_Conversao.setEnabled(true);
            status_mensagem_primaria.setText("");
        }
    }

    private void setWindowsListener() {

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowActivated(WindowEvent e) {
                mostrar_proximo_codigo_documento();
            }

        });

    }

    private void mostra_consumidor_final() {

        TbCliente cliente = (TbCliente) clientesController.findById(getIdCliente());

        if (Objects.nonNull(cliente)) {
            lbTelefoneCliente.setText("Tel.  " + cliente.getTelefone());
            lbEmailCliente.setText("Email.  " + cliente.getEmail());
            lbDesconto.setText("Desconto.  " + cliente.getPercentagemDesconto() + " %");
        } else {
            lbTelefoneCliente.setText("");
            lbEmailCliente.setText("");
            lbDesconto.setText("");

        }

    }

    private static boolean data_documento_superior_ou_igual_ao_ultimo_doc() {
        //buscando o id do documento.
        int pk_documento = getIdDocumento();
        //buscando o id do ano ecoonomico.
        int pk_ano_economico = getIdAnoEconomico();

        //busca o último documento da série em questão.
        // Integer cod_ultima_venda = vendaDao.getLastVenda( pk_documento );
        Integer cod_ultima_venda = vendasController.getLastCodigoVenda(pk_documento, pk_ano_economico);
        if (cod_ultima_venda != 0) {

            //busca o objecto para retirar apenas a data do seu procesamento
            TbVenda venda_local = (TbVenda) vendasController.findById(cod_ultima_venda);
            //retirando a data do documebto
            Date data_ultimo_documento = venda_local.getDataVenda();
            //pegando a data do documento (data actual do sistema)
            Date data_actual = dc_data_documento.getDate();
            return MetodosUtil.maior_data_1_data_2(data_actual, data_ultimo_documento)
                    || MetodosUtil.igual_data_1_data_2(data_actual, data_ultimo_documento);

        } else {
            return true;
        }

    }

//    private void data_documento_superior_data_actual()
//    {
//
//        //retirando a data do documento
//        Date data_documento = dc_data_documento.getDate();
//        //pegando a data actual do sistema 
//        Date data_sistema = new Date();
//        //comparar as datas
//        if ( MetodosUtil.maior_data_1_data_2( data_documento, data_sistema ) )
//        {
//            JOptionPane.showMessageDialog( null, "Após essa emissão, não poderá ser emitido um novo documento\n "
//                    + "com a data actual ou anterior, dentro da mesma série.",
//                    "AVISO", JOptionPane.WARNING_MESSAGE );
//
//            this.aviso_continuar_documento = JOptionPane.showConfirmDialog( null, "Ainda assim deseja continuar com a operação ?" )
//                    == JOptionPane.YES_OPTION;
//
//        }
//        else
//        {
//            this.aviso_continuar_documento = true;
//        }
//
//    }
//    private boolean verifica_ano_documento_igual_economico()
//    {
//        int ano_economico = Integer.parseInt( anoEconomicoDao.findAnoEconomico( getIdAnoEconomico() ).getDesignacao() );
//        int ano_documento = dc_data_documento.getDate().getYear() + 1900;
//        return ano_documento == ano_economico;
//
//    }
    private static boolean verifica_ano_documento_igual_economico() {
        AnoEconomico anoEconomicoLocal = (AnoEconomico) anoEconomicoController.findById(getIdAnoEconomico());
        int ano_economico = Integer.parseInt(anoEconomicoLocal.getDesignacao());
        int ano_documento = dc_data_documento.getDate().getYear() + 1900;
        return ano_documento == ano_economico;

    }

    private void mostrar_armazem() {
        TbArmazem armazem = armazensController.findByCodigo(id_armzem);

        if (!Objects.isNull(armazem)) {
            cmbArmazem.setSelectedItem(armazem.getDesignacao());
        }
    }

//    public static int getCodigoArmazem()
//    {
//        //return conexao.getCodigoPublico("tb_armazem", String.valueOf(  cmbArmazem.getSelectedItem() ) );   
//        return armazemDao.getArmazemByDescricao( cmbArmazem.getSelectedItem().toString() ).getCodigo();
//    }
    public static int getCodigoArmazem() {
        return armazensController.getArmazemByDesignacao(cmbArmazem.getSelectedItem().toString()).getCodigo();
    }

    private void setDesactivarvias(String desactivarvias) {
        if (desactivarvias.equalsIgnoreCase("Sim")) {
//            spnCopia.setVisible( true );
//            lbVias.setVisible( true );

        } else {
//            spnCopia.setVisible( false );
//            lbVias.setVisible( false );

        }
    }

//    private void setArmazem(String armazem) {
//        if (armazem.equalsIgnoreCase("Multi_armazem")) {
//            rbArmazem.setSelected(true);
//
//        } else {
//            rbArmazem1.setSelected(true);
//
//        }
//    }
//
//    public void configurar_armazens() {
//        setArmazem(dadosInstituicaoDao.findTbDadosInstituicao(1).getConfigArmazens());
//        try {
//            if (!rbArmazem.isSelected()) {
//                //                Caso for MultiArmazens
//                cmbArmazem.setModel(new DefaultComboBoxModel(armazemDao.buscaTodos2()));
    ////                cmbArmazem.setModel( new DefaultComboBoxModel( accessoArmazemDao.getAllArmazemByIdUSuario( cod_usuario ) ) );
//
//            } else if (rbArmazem.isSelected()) {
//                //                Caso for apenas Um Armazem
//                cmbArmazem.setModel(new DefaultComboBoxModel(accessoArmazemDao.getAllArmazemExceptoEconomatoByIdUSuario(idUser)));
//
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//    public static void setTotalGeral()
//    {
//        DefaultTableModel modelo = ( DefaultTableModel ) jTable1.getModel();
//        double total_geral = 0;
//        for ( int i = 0; i <= modelo.getRowCount() - 1; i++ )
//        {
//            total_geral += Double.parseDouble( modelo.getValueAt( i, 4 ).toString() );
//        }
//        lb_total_geral.setText( CfMethods.formatarComoMoeda( total_geral ) );
//    }
//    private String getCodDocActualizador()
//    {
//        try
//        {
//            this.documento = documentoDao.findDocumento( getIdDocumento() );
//            this.anoEconomico = anoEconomicoDao.findAnoEconomico( getIdAnoEconomico() );
//            // this.doc_prox_cod = documento.getCodUltimoDoc() + 1;
//            this.doc_prox_cod = vendaDao.getUltimaContagemByIdDocumentoAndAnoEconomico( getIdDocumento(), getIdAnoEconomico(), conexao ) + 1;
//            prox_doc = documento.getAbreviacao();
//            //FA Série / codigo
//            prox_doc += " " + this.anoEconomico.getSerie() + "/" + this.doc_prox_cod;
//            return prox_doc;
//        }
//        catch ( Exception e )
//        {
//            return "";
//        }
//    }
    public static void fazerBackupAgora() {
        String data = new SimpleDateFormat(YYYYMMDD_HHMMSS).format(new Date());
//        String rodar_camando = "cmd /c mysqldump -uroot -pDoV90x?# --dump-date --triggers --tables --routines --skip-quote-names --compact --skip-opt --skip-set-charset --hex-blob kitanda_db > \"..\\BD_BACKUP\\_database_backup_" + data + ".sql\"";
        String rodar_camando = "cmd /c mysqldump --single-transaction -uroot -pDoV90x?# --dump-date --triggers --add-drop-database --routines --skip-quote-names --skip-set-charset --add-locks --disable-keys --databases kitanda_db > \"..\\BD_BACKUP\\_database_backup_" + data + ".sql\"";
//String rodar_camando = "cmd /c mysqldump --single-transaction=TRUE -uroot -pDoV90x?# --dump-date --triggers --add-drop-database  --routines --skip-quote-names --compact --skip-opt --skip-set-charset --hex-blob --add-locks --disable-keys --lock-tables  --databases kitanda_db > \"..\\BD_BACKUP\\_database_backup_" + data + ".sql\"";
        Process rodarComandoWindows = rodarComandoWindows(rodar_camando, true);

//        JOptionPane.showMessageDialog ( null, "Backup realizado com sucesso! ", "Notificação", JOptionPane.INFORMATION_MESSAGE );
        System.err.println("Backup realizado com sucesso! ");

    }

    private void setFolhaImpressora(String folha) {
//        if ( folha.equalsIgnoreCase( "A6" ) )
//        {

    

    ////            ck_simplificada.setSelected( true );
////            ck_S_A6.setSelected( false );
////            ck_ComVirgula.setSelected( false );
////            ck_A7.setSelected( false );
//            this.abreviacao = Abreviacao.FR_A6;
//        }
//        else if ( folha.equalsIgnoreCase( "S_A6" ) )
//        {
//            ck_S_A6.setSelected( true );
//            ck_simplificada.setSelected( false );
//            ck_ComVirgula.setSelected( false );
//            this.abreviacao = Abreviacao.FR_S_A6;
//            ck_A7.setSelected( false );
//        }
//        else if ( folha.equalsIgnoreCase( "A7" ) )
//        {
//            ck_A7.setSelected( true );
//            ck_S_A6.setSelected( false );
//            ck_simplificada.setSelected( false );
//            ck_ComVirgula.setSelected( false );
//            this.abreviacao = Abreviacao.FR_SA7;
//        }
//        else
//        {
//            ck_ComVirgula.setSelected( true );
//            ck_simplificada.setSelected( false );
//            ck_S_A6.setSelected( false );
//            ck_A7.setSelected( false );
//            this.abreviacao = Abreviacao.FR_A6_Com_Virgula;
//        }

    }

    public static void accao_codigo_interno_enter_busca_exterior(int codigo) {

        try {
//            int codigo = produtoDao.getIdByDescricao( designacao );

            String designacao1 = produtosController.getDescricaoById(codigo);
            System.out.println("Codigo do Produto no Armazem 2" + codigo);
            System.out.println("Designacao do Produto no Armazem 2" + designacao1);
            //int   codigo = Integer.parseInt(txtCodigoProduto.getText() );
            TbProduto produto = produtosController.findByCod(codigo);
//            cmbFamilia.setSelectedItem( produto.getCodTipoProduto().getFkFamilia().getDesignacao() );
//            cmbSubFamilia.setSelectedItem( produto.getCodTipoProduto().getDesignacao() );
//            cmbProduto.setSelectedItem( produto.getDesignacao() );

            adicionar_preco_quantidade_anitgo();

            procedimento_adicionar(designacao1);
//            }
//            txtCodigoProduto.setText( "" );
//            txtQuatindade.setText( "1" );
//            txtQuatindade.requestFocus();

        } catch (Exception ex) {
            Logger.getLogger(RecolhaPedidosVisao.class
                    .getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Este produto não existe no armazém " + cmbArmazem.getSelectedItem(), DVML.DVML_COMERCIAL, JOptionPane.ERROR_MESSAGE);
        }

    }

    public static void adicionar_preco_quantidade_anitgo() {

        try {
//            if ( txtQuatindade.getText().isEmpty() )
//            {
//                JOptionPane.showMessageDialog( null, "Não informou a quantidade, por favor informe a quantidade!" );
//            }
//            else
//            {
            // TODO add your handling code here
//Mano
            //if(BDConexao.getCodigoByCodigo("tb_stock", "quantidade_existente", "cod_produto_codigo", getCodigoProduto())<=5)
            //if (conexao.getQtdExistenteStock(getCodigoProduto(), getCodigoArmazem()) <= conexao.getQtdCriticaStock(getCodigoProduto(), getCodigoArmazem())) {
            TbStock stock = stocksController.getStockByIdProdutoAndIdArmazem(getCodigoProduto(), getCodigoArmazem());

            if (stock != (null)) {

                txtQuatidadeExistente.setText(String.valueOf(conexao.getQtdExistenteStock(getCodigoProduto(), getCodigoArmazem())));

                System.err.println("Codigo Produto:  " + getCodigoProduto());
//                    System.err.println( "Qtd:  " + txtQuatindade.getText() );
                //txtPreco.setText(String.valueOf(MetodosUtil.retirar_dizimas(precoDao.findTbPreco(precoDao.getUltimoIdPrecoByIdProduto(getCodigoProduto(), Integer.parseInt(txtQuatindade.getText()))).getPrecoVenda())));
//                    txtPreco.setText( String.valueOf( MetodosUtil.retirar_dizimas( precoDao.findTbPreco( precoDao.getUltimoIdPrecoByIdProduto( getCodigoProduto(), Double.parseDouble( txtQuatindade.getText() ) ) ).getPrecoVenda().doubleValue() ) ) );
            }

//            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger
                    .getLogger(RecolhaPedidosVisao.class
                            .getName()).log(Level.SEVERE, null, ex);
        }

    }

//        private void accao_codigo_manual_enter()
//    {
//        try
//        {
//            String codigo_manual = txtCodigoManual.getText();
//            TbProduto produtoLocal = produtosController.findByCodManual( codigo_manual );
//            procedimentoAdicionarTabela( produtoLocal );
//        }
//        catch ( Exception ex )
//        {
//            ex.printStackTrace();
//            Logger.getLogger( VendaUsuarioVisao.class.getName() ).log( Level.SEVERE, null, ex );
//            JOptionPane.showMessageDialog( null, "Não existe produto com este código de barra.", DVML.DVML_COMERCIAL, JOptionPane.ERROR_MESSAGE );
//        }
//
//    }
    public static void procedimento_adicionar(String designacao) {

        int codigo_produto = produtosController.getIdProduto(designacao);
        System.out.println("Aqui vai o codigo" + codigo_produto);
        String designacao1 = produtosController.getDescricaoById(codigo_produto);
        System.out.println("Aqui vai a designacao" + designacao1);
        TbProduto produto_local = produtosController.findByCod(codigo_produto);
//        if ( activo_um_lugar() )
//        {

        if (qtd_possivel(designacao1)) {
            try {

                procedimento_salvar_pedidos_iten_pedidos(designacao1);
                status_button_lugar(true);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Fallha ao adicionar o pedido", DVML.DVML_COMERCIAL, JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(null, "Atenção!!!\nNão existe quantidade no stock.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }

//        }
//                    }
//                }
//                else
//                {
//                    JOptionPane.showMessageDialog( null, "Impossivel adicionar o produto porque já foi expirado.", "Aviso", JOptionPane.WARNING_MESSAGE );
//                }
//        }
//        else
//        {
//            JOptionPane.showMessageDialog( null, "Por Favor Digite a Quantidade" );
//        }
    }

//    public static boolean campos_invalidos()
//    {
//        return txtQuatindade.getText().equals( "" );
//    }
    private static boolean isProdutoExpirado(int codigoProduto) {
        // return produtoDao.produtoExpirado( codigoProduto );
        return false;
    }

    public static boolean estado_critico() throws SQLException {
        TbStock stock = stocksController.getStockByIdProdutoAndIdArmazem(getCodigoProduto(), getCodigoArmazem());
        double qtd_minima = stock.getQuantBaixa(),
                qtd_existente = stock.getQuantidadeExistente(),
                qtd_critica = stock.getQuantCritica();

        return conexao.getQuantidade_minima_publico(getCodigoProduto(), getCodigoArmazem())
                < conexao.getQuantidade_Existente_Publico(getCodigoProduto(), getCodigoArmazem())
                && conexao.getQuantidade_Existente_Publico(getCodigoProduto(), getCodigoArmazem())
                <= conexao.getQuantidade_critica_public(getCodigoProduto(), getCodigoArmazem());
//   
//        return qtd_minima < qtd_existente
//                && qtd_existente <= qtd_critica;

    }

    private static boolean qtd_possivel(String designacao) {
//            TbDadosInstituicao dadosInstituicao = dadosInstituicaoDao.findTbDadosInstituicao( 1 );
//        int idProduto = produtoDao.getIdByDescricao( designacao );
        int idProduto = produtosController.getIdProduto(designacao);

        TbProduto produto = produtoDao.findTbProduto(idProduto);

        if (produto.getCodTipoProduto().getFkFamilia().getPkFamilia() == DVML.COD_SERVICO) {
            return true;
        }

        boolean isStocavel = produto.getStocavel().equals("true");

        if (isStocavel) {
            double qtdExistentesNoutrosPedidos = ItemPedidosDao.getQtdItensByIdPedidoTradicional(idProduto, conexao);
            qtdExistentesNoutrosPedidos++;// Mais Um porque causa do novo pedido actual
            double qtdExistente = conexao.getQuantidade_Existente_Publico(idProduto, getCodigoArmazem());

            return qtdExistente >= qtdExistentesNoutrosPedidos;

        }

        return true;

    }

    public static void scrolltable() {
        table.scrollRectToVisible(table.getCellRect(table.getRowCount() - 1, table.getColumnCount(), true));
    }

    private void actualizarQtdTable() {

        int linhaSelecionada = table.getSelectedRow();

        if (linhaSelecionada > -1) {
            int idItemPedidos = Integer.parseInt(table.getValueAt(linhaSelecionada, 0).toString());
            TbItemPedidos itemPedidosLocal = itemPedidosDao.findTbItemPedidos(idItemPedidos);

            try {
                itemPedidosLocal.setQtd(1);
                itemPedidosLocal.setTotalItem(itemPedidosLocal.getTotalItem() * 1);
                itemPedidosDao.edit(itemPedidosLocal);
            } catch (Exception e) {
            }

            int qtd;

            try {
                qtd = Integer.parseInt(table.getValueAt(linhaSelecionada, 3).toString());
            } catch (NumberFormatException e) {
                resetQtd("Erro de formatação da quantidade.\nAtenção: Tem que ser número.");
                return;
            }

            if (qtd <= 0) {
                resetQtd("Quantidade não pode ser zero(0) ou número négativo");
                qtd = 1;
            }

            if (!possivel_quantidade(itemPedidosLocal.getFkProdutos().getCodigo())) //verifica se é possivel adicionar
            {
                resetQtd("Impossivel para esta quantidade no stock");
            } else if (possivel_quantidade(itemPedidosLocal.getFkProdutos().getCodigo(), qtd)) //verifica se  há disponiblidade ao  adicionar depois da  'qtd'  ser lançada.
            {
                itemPedidosLocal.setQtd(qtd);
                itemPedidosLocal.setTotalItem(itemPedidosLocal.getTotalItem() * qtd);
                try {
                    itemPedidosDao.edit(itemPedidosLocal);
                    actualizar();
                } catch (Exception e) {
                }
            } else {
                resetQtd("Impossivel para esta quantidade no stock");
            }

        }

    }

    private static void actualizarQtdTable(double qtd) {

        int linhaSelecionada = table.getSelectedRow();

        if (linhaSelecionada > -1) {
            int idItemPedidos = Integer.parseInt(table.getValueAt(linhaSelecionada, 0).toString());
            TbItemPedidos itemPedidosLocal = itemPedidosDao.findTbItemPedidos(idItemPedidos);

            try {
                itemPedidosLocal.setQtd(1);
                itemPedidosLocal.setTotalItem(itemPedidosLocal.getTotalItem() * 1);
                itemPedidosDao.edit(itemPedidosLocal);
            } catch (Exception e) {
            }

            if (qtd <= 0) {
                resetQtd("Quantidade não pode ser zero(0) ou número négativo");
                qtd = 1;
            }

            if (!possivel_quantidade(itemPedidosLocal.getFkProdutos().getCodigo())) //verifica se é possivel adicionar
            {
                resetQtd("Impossivel para esta quantidade no stock");
            } else if (possivel_quantidade(itemPedidosLocal.getFkProdutos().getCodigo(), (int) qtd)) //verifica se  há disponiblidade ao  adicionar depois da  'qtd'  ser lançada.
            {
                itemPedidosLocal.setQtd(qtd);
                itemPedidosLocal.setTotalItem(itemPedidosLocal.getTotalItem() * qtd);
                try {
                    itemPedidosDao.edit(itemPedidosLocal);
                    actualizar();
                } catch (Exception e) {
                }
            } else {
                resetQtd("Impossivel para esta quantidade no stock");
            }

        }

    }

    private void selecionar_documento() {
//        if ( cmbTipoDocumento.getSelectedItem().equals( "Factura/Recibo" ) )
//        {
//            BT_Conversao.setVisible( true );
//        }
//        else if ( cmbTipoDocumento.getSelectedItem().equals( "Factura Consulta" ) )
//        {
//            BT_Conversao.setVisible( false );
//        }
//        
        if (cmbTipoDocumento.getSelectedItem().equals("Factura/Recibo")) {
            BT_Conversao.setVisible(true);
            BtnProforma.setVisible(false);
            btFT.setVisible(false);
        } else if (cmbTipoDocumento.getSelectedItem().equals("Factura")) {
            btFT.setVisible(true);
            BtnProforma.setVisible(false);
            BT_Conversao.setVisible(false);
        } else if (cmbTipoDocumento.getSelectedItem().equals("Factura-Proforma")) {
            BtnProforma.setVisible(true);
            BT_Conversao.setVisible(false);
            btFT.setVisible(false);
        }
    }

    private static void resetQtd(String msg) {
        table.setValueAt(1, table.getSelectedRow(), 3);
        JOptionPane.showMessageDialog(null, msg);
        table.clearSelection();
    }

    public static void adicionarImportar(String designacao) {

        if (true) {

            if (qtd_possivel(designacao)) {
                try {

                    procedimento_salvar_pedidos_iten_pedidos(designacao);
                    status_button_lugar(true);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Fallha ao adicionar o pedido", DVML.DVML_COMERCIAL, JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                }
            } else {
                JOptionPane.showMessageDialog(null, "Nao existe quantidade no stock.", "Aviso", JOptionPane.WARNING_MESSAGE);

            }

        }

    }

    class TratarEvento implements KeyListener {

        String prefixo = "";

        public void keyPressed(KeyEvent evt) {
            prefixo = txtObs.getText();
            if (evt.getKeyCode() != KeyEvent.VK_BACK_SPACE && evt.getKeyCode() != KeyEvent.VK_ENTER) {
                char key = evt.getKeyChar();
                prefixo = txtObs.getText().trim() + key;
                //adicionar( stockDao.getStockLIKE_Nome( prefixo ) );
                actualizarLinhaObs(prefixo);
            } else if (evt.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                try {
                    prefixo = prefixo.toString().trim().substring(0, prefixo.length() - 1);
//                    prefixo = txtObs.toString().trim().substring( 0, prefixo.length() - 1 );
                    // adicionar( stockDao.getStockLIKE_Nome( prefixo ) );
                    actualizarLinhaObs(prefixo);

                } catch (Exception e) {
                }
            }
        }

        public void keyReleased(KeyEvent evt) {
        }

        public void keyTyped(KeyEvent evt) {
        }

        private void actualizarLinhaObs(String prefixo) {
            DefaultTableModel modelo = (DefaultTableModel) table.getModel();
            int linhaSeleccionada = table.getSelectedRow();

            if (linhaSeleccionada == -1) {
                linhaSeleccionada = modelo.getRowCount() - 1;
            }

            int id = Integer.parseInt(modelo.getValueAt(linhaSeleccionada, 0).toString());
            String servico = modelo.getValueAt(linhaSeleccionada, 2).toString();
            String obs = "";

            try {
                servico = servico.split("-")[0];
            } catch (Exception e) {
            }
            modelo.setValueAt(servico, linhaSeleccionada, 2);

            if (prefixo.length() < 500) {
                obs = prefixo.toUpperCase();
                servico += "-" + obs;
            }

            try {
                ItemPedidosDao.alterar_item_pedidios_obs(id, obs, conexao);
            } catch (Exception e) {
            }
            modelo.setValueAt(servico, linhaSeleccionada, 2);
        }

    }

    private void setDados() {
        try {
            DefaultTableModel modelo = (DefaultTableModel) table.getModel();
            int linhaSeleccionada = table.getSelectedRow();
            String data_entrega = modelo.getValueAt(linhaSeleccionada, 1).toString();
            int ano = Integer.parseInt(data_entrega.split("-")[0]);
            int mes = Integer.parseInt(data_entrega.split("-")[1]);
            int dia = Integer.parseInt(data_entrega.split("-")[2]);
            System.out.println("ANO: " + ano);
            Date dataEntrega = new Date();
            dataEntrega.setYear(ano - 1900);
            dataEntrega.setMonth(mes - 1);
            dataEntrega.setDate(dia);

            System.out.println("SET DATA ENTREGA: " + dataEntrega);
//        dc_data_entrega.setDateFormatString( data_entrega );
//        dc_data_entrega.setDate( dataEntrega );

            String linha = modelo.getValueAt(linhaSeleccionada, 2).toString();
            String servico;
            String obs;
            try {
                servico = linha.split("-")[0];
                obs = linha.split("-")[1];
            } catch (Exception e) {
                obs = "";
                servico = "";
            }

            lbServico.setText(servico);
            txtObs.setText(obs);
        } catch (Exception e) {
        }

    }

    private void adicionarDateEntrega() {
        DefaultTableModel modelo = (DefaultTableModel) table.getModel();
        int linhaSeleccionada = table.getSelectedRow();

        if (modelo.getRowCount() > 0) {
            if (linhaSeleccionada == -1) {
                linhaSeleccionada = modelo.getRowCount() - 1;
            }

            try {
                int id = Integer.parseInt(modelo.getValueAt(linhaSeleccionada, 0).toString());
                Date dataEntrega = dc_data_entrega.getDate();
                ItemPedidosDao.alterar_item_pedidios_data_entrega(id, dataEntrega, conexao);
                modelo.setValueAt(MetodosUtil.getDataBanco(dataEntrega), linhaSeleccionada, 1);
            } catch (Exception e) {
            }

        }

    }

    private static int getTotalPecas(List<TbItemVenda> lista) {
        int total = 0;
        for (int i = 0; i < lista.size(); i++) {
            TbItemVenda item = lista.get(i);
            Vector<TbProduto> allProdutosAssociados = produtosController.getAllProdutosAssociados(
                    item.getCodigoProduto().getCodigo());

            if (allProdutosAssociados.size() > 0) {
                total += allProdutosAssociados.size();
            } else {
                total += 1;
            }

        }

        return total;

    }

    private static void procedimentoImprimirTicket() {

        TbVenda vendaLocal = vendasController.getLastVenda();
        corpoImprimirTicket(vendaLocal.getCodigo());

    }

    private static void corpoImprimirTicket(int codigo) {
        String file = "ticket_recolha_unico.jasper";

        String impressora = dadosInstituicaoController.findByCodigo(1).getImpressoraCozinha();
        HashMap hashMap = new HashMap();
        hashMap.put("CodVenda", codigo);
        hashMap.put("TotalPecas", 0);

        AnyReport anyReport = new AnyReport(hashMap, file, impressora);
//        AnyReport anyReport = new AnyReport( hashMap, file );
    }

    private static void procedimentoImprimirTicket(int codigo, BDConexao conexao) {
        vendasController = new VendasController(conexao);
        dadosInstituicaoController = new DadosInstituicaoController(conexao);
        corpoImprimirTicket(codigo);

    }

    private static void procedimentoReimprimirTickets(String ref_doc) {
//        String ref_doc = txtFactura.getText();

        TbVenda venda = vendaDao.findByCodFactReemprensaoGeral(ref_doc);
        List<TbItemVenda> listaItem = itemVendasController.listarTodosByCodigoVenda(venda.getCodigo());
//        List<TbItemVenda> listaItem = itemVendasController.listarTodosByCodigoVenda( lastVenda.getCodigo() );
//        String impressora = "";
        String file = "ticket_recolha_unico.jasper";
        HashMap hashMap = new HashMap();

        hashMap.put("CodVenda", venda.getCodigo());
        int totalPecas = listaItem.size();
        hashMap.put("TotalPecas", totalPecas);

        AnyReport anyReport = new AnyReport(hashMap, file);

    }

//    private static void procedimentoReimprimirTicket()
//    {
//        TbVenda vendaLocal = vendasController.getLastVenda();
//
//        List<TbItemVenda> listaItem = itemVendasController.listarTodosByCodigoVenda( vendaLocal.getCodigo() );
//        String impressora = "";
    ////        String file = "recolha_2.jasper";
//        String file = "ticket_recolha_personalizado.jasper";
//        int totalPecas = listaItem.size();
//        for ( int i = 0; i < totalPecas; i++ )
//        {
//            TbItemVenda item = listaItem.get( i );
//
//            Vector<TbProduto> allProdutosAssociados = produtosController.getAllProdutosAssociados( item.getCodigoProduto().getCodigo() );
//
//            System.out.println( "Associado:  " + Objects.nonNull( allProdutosAssociados ) );
//            /**
//             * IMPRIMIR TODAS AS PEÇAS ASSOCIADAS
//             */
//            if ( Objects.nonNull( allProdutosAssociados ) )
//            {
//                System.out.println( "Tem associados" );
//                for ( int j = 0; j < allProdutosAssociados.size(); j++ )
//                {
//                    TbProduto associado = allProdutosAssociados.get( j );
//
//                    HashMap hashMap = new HashMap();
//                    hashMap.put( "CodItem", associado.getCodigo() );
//                    hashMap.put( "TotalPecas", totalPecas );
//                    hashMap.put( "data_entrega", MetodosUtil.getDataBanco( vendaLocal.getDataVencimento() ) );
//                    hashMap.put( "Designacao", associado.getDesignacao() );
//                    hashMap.put( "CodFact", vendaLocal.getCodFact() );
//                    hashMap.put( "Posicao", item.getPosicao() );
//                    hashMap.put( "Obs", item.getObs() );
//                    TbCliente clienteLocal = ( TbCliente ) clientesController.findByCodigo( vendaLocal.getCodigoCliente().getCodigo() );
//                    TbUsuario usuariolocal = ( TbUsuario ) usuariosController.findById( vendaLocal.getCodigoUsuario().getCodigo() );
//                    hashMap.put( "NomeCliente", clienteLocal.getNome() );
//                    hashMap.put( "TelefoneCliente", clienteLocal.getTelefone() );
//                    hashMap.put( "NomeUsuario", usuariolocal.getNome() );
//
//                    AnyReport anyReport = new AnyReport( hashMap, file, "" );
//
//                }
//            }
//            else
//            {
//                System.out.println( "Não Tem associação." );
//                HashMap hashMap = new HashMap();
//                hashMap.put( "CodItem", item.getCodigo() );
//                hashMap.put( "TotalPecas", totalPecas );
//                hashMap.put( "DataVencimento", MetodosUtil.getDataBanco( vendaLocal.getDataVencimento() ) );
//                TbProduto produtoLocal = ( TbProduto ) produtosController.findByCod( item.getCodigoProduto().getCodigo() );
//                hashMap.put( "Designacao", produtoLocal.getDesignacao() );
//                hashMap.put( "CodFact", vendaLocal.getCodFact() );
//                hashMap.put( "Posicao", item.getPosicao() );
//                hashMap.put( "Obs", item.getObs() );
//                TbCliente clienteLocal = ( TbCliente ) clientesController.findByCodigo( vendaLocal.getCodigoCliente().getCodigo() );
//                TbUsuario usuariolocal = ( TbUsuario ) usuariosController.findById( vendaLocal.getCodigoUsuario().getCodigo() );
//                hashMap.put( "NomeCliente", clienteLocal.getNome() );
//                hashMap.put( "TelefoneCliente", clienteLocal.getTelefone() );
//                AnyReport anyReport = new AnyReport( hashMap, file, "" );
//
//            }
//
//        }
//    }
    private static void procedimentoImprimirTicketCada() {
        String impressora = dadosInstituicaoController.findByCodigo(1).getImpressoraCozinha();
//        JOptionPane.showMessageDialog( null, "Cheguei aqui...." );
        TbVenda vendaLocal = vendasController.getLastVenda();
        List<TbItemVenda> listaItem = itemVendasController.listarTodosByCodigoVenda(vendaLocal.getCodigo());
        String file = "ticket_recolha_singular.jasper";
        int totalPecas = listaItem.size();

        System.err.println("TOTAL DE PECAS $$$$: " + totalPecas);
        for (int i = 0; i < totalPecas; i++) {
            TbItemVenda item = listaItem.get(i);
            TbProduto pai = produtosController.findByCod(item.getCodigoProduto().getCodigo());
            Vector<TbProduto> allProdutosAssociados = produtosController.getAllProdutosAssociados(item.getCodigoProduto().getCodigo());
            System.err.println("ENTREI NO CORPO DAS PECAS");
            /**
             * IMPRIMIR TODAS AS PEÇAS ASSOCIADAS
             */

            System.err.println("SIZE ASSOCIADO: " + allProdutosAssociados.size());
            if (allProdutosAssociados.size() > 0) {
                for (int j = 0; j < allProdutosAssociados.size(); j++) {
                    TbProduto associado = allProdutosAssociados.get(j);

                    HashMap hashMap = new HashMap();
                    hashMap.put("designacao", associado.getDesignacao());
                    hashMap.put("designacao_pai", pai.getDesignacao());
                    hashMap.put("data_entrega", item.getDataEntrega());
                    hashMap.put("cod_fact", vendaLocal.getCodFact());
                    hashMap.put("posicao", item.getPosicao());
                    hashMap.put("obs", item.getObs());
                    TbCliente clienteLocal = (TbCliente) clientesController.findByCodigo(vendaLocal.getCodigoCliente().getCodigo());
                    TbUsuario usuariolocal = (TbUsuario) usuariosController.findById(vendaLocal.getCodigoUsuario().getCodigo());
                    hashMap.put("nome_cliente", clienteLocal.getNome());
                    hashMap.put("telefone_cliente", clienteLocal.getTelefone());
                    hashMap.put("usuario", usuariolocal.getNome());
                    System.err.println("Chamei o REPORT ASSOCIADO");
                    AnyReport anyReport = new AnyReport(hashMap, file, impressora);

                }
            } else {
                System.err.println("Chamei o REPORT SEM ASSOCIADO");
                HashMap hashMap = new HashMap();
                hashMap.put("designacao", pai.getDesignacao());
                hashMap.put("designacao_pai", "");
                hashMap.put("data_entrega", item.getDataEntrega());
                hashMap.put("cod_fact", vendaLocal.getCodFact());
                hashMap.put("posicao", item.getPosicao());
                hashMap.put("obs", item.getObs());
                TbCliente clienteLocal = (TbCliente) clientesController.findByCodigo(vendaLocal.getCodigoCliente().getCodigo());
                TbUsuario usuariolocal = (TbUsuario) usuariosController.findById(vendaLocal.getCodigoUsuario().getCodigo());
                hashMap.put("nome_cliente", clienteLocal.getNome());
                hashMap.put("telefone_cliente", clienteLocal.getTelefone());
                hashMap.put("usuario", usuariolocal.getNome());

                AnyReport anyReport = new AnyReport(hashMap, file, impressora);

            }

        }

    }

    public static void procedimento_salvar_pedidos_iten_pedidos_urgencia(String designacao_produto) {

        try {
            /* MOSTRA A QUANTIDADE NO STOCK */
            int codigo_produto = produtoDao.getIdByDescricao(designacao_produto);

            TbProduto produto_local = produtoDao.findTbProduto(codigo_produto);

            lbQuantidadeExistente.setVisible(false);
            txtQuatidadeExistente.setVisible(false);
            txtQuatidadeExistente.setText("");

            TbItemPedidos itemPedidosLocal = new TbItemPedidos();

            itemPedidosLocal.setFkLugares((TbLugares) lugaresController.findByLugar(lugar));
//            itemPedidosLocal.setFkLugares( lugarDao.findTbLugares( lugarDao.getIdByDescricao( getDescricaoLugar() ) ) );
            itemPedidosLocal.setFkProdutos(produtoDao.findTbProduto(produtoDao.getIdByDescricao(designacao_produto)));
            itemPedidosLocal.setQtd(1);
            itemPedidosLocal.setObs("");
            itemPedidosLocal.setDataEntrega(dataEntregaEscolhida()); // a data do campo "Data de entrega" (por omissão, daqui a 2 dias)
            itemPedidosLocal.setStatusConvertido(false);
            /*Envia para a área da cozinha*/
            //para enviar  da cozinha
            itemPedidosLocal.setStatusEnviado(true);
            //para saber se o prato ja foi feito 
            itemPedidosLocal.setStatusEfectuado(false);

            double total = itemPedidosLocal.getQtd() * precoDao.findTbPreco(precoDao.getUltimoIdPrecoByIdProduto(itemPedidosLocal.getFkProdutos().getCodigo())).getPrecoVenda().doubleValue();
            itemPedidosLocal.setTotalItem(total);
            itemPedidosLocal.setFkPedidos(pedido);

            try {

                Integer idLastItemPedido = itemPedidosDao.criarComProcedimento(itemPedidosLocal, conexao);
                if (idLastItemPedido != null) {
                    //##PINTAR
                    actualizar();
                } else {
                    System.err.println("ERRO AO INSERIR O ITEM ...");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void adicionar_tabela() {

        DefaultTableModel modelo = null;

        try {
            modelo = (DefaultTableModel) jtable_reimpressao.getModel();
            modelo.setRowCount(0);
            lista = vendaDao.getAllFRVendaByBetweenDataAndArmazemAndDocumentoRecolha(dcDataInicio.getDate(), dcDataFim.getDate(), getCodigoArmazem(), DVML.DOC_FACTURA_RECIBO_FR);
            if (lista != null) {
                for (TbVenda object : lista) {

                    modelo.addRow(new Object[]{
                        object.getCodFact(),});

                }

            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Não há registro para esse armazém", DVML.DVML_COMERCIAL, JOptionPane.WARNING_MESSAGE);
        }

    }

    private void reimprimir_FR() {
        DefaultTableModel modelo = (DefaultTableModel) jtable_reimpressao.getModel();
        int selectedRow = jtable_reimpressao.getSelectedRow();
        String codRef = modelo.getValueAt(selectedRow, 0).toString();

        procedimentoReimprimirTickets(codRef);
        procedimento_reimprimir_FR(codRef);
    }

    private void procedimento_reimprimir_FR(String ref_doc) {

        HashMap hashMap = new HashMap();
        TbVenda venda = vendaDao.findByCodFactReemprensao(ref_doc);

        if (venda != null) {

//            Abreviacao abreviacao = DVML.getAbreviacao( venda.getFkDocumento().getPkDocumento() );
//            abreviacao = DVML.Abreviacao.FR_A4;
            List<TbProduto> lista_produto_isentos = new ArrayList<>();
            lista_produto_isentos = MetodosUtil.getProdutosIsentos(venda.getTbItemVendaList());
            String motivos_isentos = MetodosUtil.getMotivoIsensaoProdutos(lista_produto_isentos);
//            ListaVenda1 original = new ListaVenda1( cod_venda, abreviacao, false, ck_simplificada.isSelected(), "Original", motivos_isentos );
            ListaVendaRecolhasReimpressao listaVenda1 = new ListaVendaRecolhasReimpressao(venda.getCodigo(), abreviacao, false, true, SEGUNDA_VIA_CONFORMIDADE_COM_ORIGINAL);
//            ListaVenda2 listaVenda2 = new ListaVenda2( venda.getCodigo(), abreviacao, false, false, DVML.SEGUNDA_VIA_CONFORMIDADE_COM_ORIGINAL, motivos_isentos );

        } else {
            JOptionPane.showMessageDialog(null, "Atenção\nO Documento não existe na base de dados. \nObs: Verifique a referência. ");
        }

    }

    private void pesquisa_cliente_by_telefone() {

        String nif = txtTelefoneCliente.getText();
        try {
            String nome_cliente = clientesController.getClienteByNifOrberByTelefone(nif).getNome();
            cmbCliente.setSelectedItem(nome_cliente.trim());
            mostra_consumidor_final();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Não existe cliente com código");
            cmbCliente.setSelectedItem("Consumidor Final");
        }

    }

    private void procedimetoAdicionarUrgencia() {
        TbProduto produto50 = null, produto100 = null, produto = null;
        Double totalFactura;
        totalFactura = 0d;

        if (rbTaxaUrgente50.isSelected()) {
            produto100 = produtosController.findByDesignacao(TAXA_EXPRESSO_100);

            if (ItemPedidosDao.existeProdutoIemPedidos(produto100.getCodigo(), conexao)) {
                removerUrgente(produto100);
            }

            totalFactura = CfMethods.parseMoedaFormatada(txtTotalApagar.getText());
            produto = produtosController.findByDesignacao(TAXA_URGENCIA_50);

            if (Objects.isNull(produto)) {
                JOptionPane.showMessageDialog(null, "Caro usuário deve pedir ao admin cadastrar o serviço 'Taxa de Urgência 50'");
                return;
            }
            totalFactura = (totalFactura / 2);
        } else if (rbTaxaExpresso100.isSelected()) {

            produto50 = produtosController.findByDesignacao(TAXA_URGENCIA_50);

            if (ItemPedidosDao.existeProdutoIemPedidos(produto50.getCodigo(), conexao)) {
                removerUrgente(produto50);
            }

            totalFactura = CfMethods.parseMoedaFormatada(txtTotalApagar.getText());

            produto = produtosController.findByDesignacao(TAXA_EXPRESSO_100);
            if (Objects.isNull(produto)) {
                JOptionPane.showMessageDialog(null, "Caro usuário deve pedir ao admin cadastrar o serviço 'Taxa Expresso 100'");
                return;
            }
        }

        removerUrgente(produto);
//        totalFactura = CfMethods.parseMoedaFormatada( txtTotalApagar.getText() );
        MetodosUtil.actualizarPrecoVendaManual(produto.getCodigo(), totalFactura, precosController, conexao);
        procedimento_salvar_pedidos_iten_pedidos_urgencia(produto.getDesignacao());

    }

    private static void removerUrgente(TbProduto produto) {
        int cod_pedido = (pedidoDao.getLastPedidoByDefignacaoMesaFALSE(mesa));
        pedido = pedidoDao.findTbPedido(cod_pedido);
        TbLugares lugarPedido = (TbLugares) lugaresController.findByLugar(lugar);
        ItemPedidosDao.deletar_item_perdido(produto.getCodigo(), pedido.getPkPedido(), lugarPedido.getPkLugares(), conexao);
        actualizar();
    }

    private static void adicionarLaguraAltura() {
        double largura;
        double altura;
        double qtdItens;
        double qtdTotal;

        try {
            BigDecimal decimal = new BigDecimal(txtLargura.getText()).setScale(2, BigDecimal.ROUND_UP);
//            largura = Double.parseDouble( txtLargura.getText() );
            largura = decimal.doubleValue();

        } catch (Exception e) {
            largura = 0;
        }

        try {
            altura = Double.parseDouble(txtAltura.getText());
        } catch (Exception e) {
            altura = 0;
        }

        double soma = (largura + altura);

        try {
            lbSomaLarguraAltura.setText(String.valueOf(soma));
        } catch (Exception e) {
            lbSomaLarguraAltura.setText("");
        }

        try {
            qtdItens = Double.parseDouble(txtQtdItens.getText());
        } catch (Exception e) {
            qtdItens = 0;
        }

        qtdTotal = soma * qtdItens;

        try {

            BigDecimal decimal = new BigDecimal(qtdTotal).setScale(2, BigDecimal.ROUND_UP);
            lbTotalQtd.setText(String.valueOf(decimal.doubleValue()));
        } catch (Exception e) {
            lbTotalQtd.setText("");
        }

    }

    private static int getIdGrupo() {
        if (!btnLavar.isEnabled()) {
            return DVML.ID_GRUPO_LAVAR;
        }

        if (!btnEngomar.isEnabled()) {
            return DVML.ID_GRUPO_ENGOMAR;
        }

        return 0;
    }

}
