package visao;

import comercial.controller.ClientesController;
import comercial.controller.ConfiguracaoMesComecoController;
import comercial.controller.DadosInstituicaoController;
import comercial.controller.DocumentosController;
import comercial.controller.FamiliasController;
import comercial.controller.MesRhController;
import comercial.controller.ProdutosController;
import comercial.controller.TipoProdutosController;
import entity.ConfiguracaoMesComeco;
import entity.TbCliente;
import entity.TbDadosInstituicao;
import entity.TbProduto;
import entity.TbTipoProduto;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.SwingWorker;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import util.BDConexao;
import util.DVML;
import util.MetodosUtil;
import util.ValidadorNifAGT;
import visao.cliente.ConfirmacaoNif;
import util.ValidadorNifAGT.Resultado;
import visao.cliente.PainelEstadoNif;
import visao.design.BotaoModerno;
import visao.design.Cabecalho;
import visao.design.CampoModerno;
import visao.design.CampoPesquisa;
import visao.design.Cartao;
import visao.design.ComboModerno;
import visao.design.FundoJanela;
import visao.design.IconeLinha;
import visao.design.PainelResponsivo;
import visao.design.PainelRolavel;
import visao.design.PainelTabela;
import visao.design.RolagemModerna;
import visao.design.Rodape;
import visao.design.Selo;
import visao.design.TabelaModerna;
import visao.design.Tema;
import static visao.design.Formularios.adicionarCampo;
import static visao.design.Formularios.atalho;
import static visao.design.Formularios.coluna;
import static visao.design.Formularios.dimensionar;
import static visao.design.Formularios.fecharGrade;
import static visao.design.Formularios.gbc;
import static visao.design.Formularios.marcarErro;
import static visao.design.Formularios.nota;
import static visao.design.Formularios.rotulo;
import static visao.design.Formularios.rotuloObrigatorio;
import static visao.design.Formularios.transparente;

/**
 * Cadastro de clientes.
 * <p>
 * O NIF é validado antes de salvar ou alterar: primeiro o formato exigido
 * pela AGT e depois o registo do contribuinte no serviço público da AGT
 * (ver {@link ValidadorNifAGT}). Um NIF com formato inválido ou que não esteja
 * registado na AGT impede a gravação.
 * <p>
 * Janela construída em código com o kit {@code visao.design} (sem .form).
 * A disposição adapta-se à largura: duas colunas (ficha + lista) em ecrãs
 * largos, uma coluna em ecrãs estreitos.
 */
public class ClienteVisao extends javax.swing.JDialog {

    private static final String PAIS_ANGOLA = "Angola - AO";
    private static final String[] PAISES = {
        "Afeganistão - AF",
        "África do Sul - ZA",
        "Albânia - AL",
        "Alemanha - DE",
        "Andorra - AD",
        "Angola - AO",
        "Antígua e Barbuda - AG",
        "Arábia Saudita - SA",
        "Argélia - DZ",
        "Argentina - AR",
        "Arménia - AM",
        "Austrália - AU",
        "Áustria - AT",
        "Azerbaijão - AZ",
        "Bahamas - BS",
        "Bangladesh - BD",
        "Barbados - BB",
        "Bélgica - BE",
        "Belize - BZ",
        "Benim - BJ",
        "Bielorrússia - BY",
        "Bolívia - BO",
        "Bósnia e Herzegovina - BA",
        "Botswana - BW",
        "Brasil - BR",
        "Brunei - BN",
        "Bulgária - BG",
        "Burkina Faso - BF",
        "Burundi - BI",
        "Butão - BT",
        "Cabo Verde - CV",
        "Camarões - CM",
        "Camboja - KH",
        "Canadá - CA",
        "Catar - QA",
        "Cazaquistão - KZ",
        "Chade - TD",
        "Chile - CL",
        "China - CN",
        "Chipre - CY",
        "Colômbia - CO",
        "Comores - KM",
        "Congo (Brazzaville) - CG",
        "Congo (Kinshasa) - CD",
        "Coreia do Norte - KP",
        "Coreia do Sul - KR",
        "Costa do Marfim - CI",
        "Costa Rica - CR",
        "Croácia - HR",
        "Cuba - CU",
        "Dinamarca - DK",
        "Djibuti - DJ",
        "Dominica - DM",
        "Egito - EG",
        "El Salvador - SV",
        "Emirados Árabes Unidos - AE",
        "Equador - EC",
        "Eritreia - ER",
        "Eslováquia - SK",
        "Eslovénia - SI",
        "Espanha - ES",
        "Estados Unidos - US",
        "Estónia - EE",
        "Etiópia - ET",
        "Fiji - FJ",
        "Filipinas - PH",
        "Finlândia - FI",
        "França - FR",
        "Gabão - GA",
        "Gâmbia - GM",
        "Gana - GH",
        "Geórgia - GE",
        "Grécia - GR",
        "Granada - GD",
        "Guatemala - GT",
        "Guiné - GN",
        "Guiné-Bissau - GW",
        "Guiné Equatorial - GQ",
        "Guiana - GY",
        "Haiti - HT",
        "Honduras - HN",
        "Hungria - HU",
        "Iémen - YE",
        "Ilhas Marshall - MH",
        "Índia - IN",
        "Indonésia - ID",
        "Irão - IR",
        "Iraque - IQ",
        "Irlanda - IE",
        "Islândia - IS",
        "Israel - IL",
        "Itália - IT",
        "Jamaica - JM",
        "Japão - JP",
        "Jordânia - JO",
        "Kuwait - KW",
        "Laos - LA",
        "Lesoto - LS",
        "Letónia - LV",
        "Líbano - LB",
        "Libéria - LR",
        "Líbia - LY",
        "Liechtenstein - LI",
        "Lituânia - LT",
        "Luxemburgo - LU",
        "Macedónia do Norte - MK",
        "Madagáscar - MG",
        "Malásia - MY",
        "Malawi - MW",
        "Maldivas - MV",
        "Mali - ML",
        "Malta - MT",
        "Marrocos - MA",
        "Maurícia - MU",
        "Mauritânia - MR",
        "México - MX",
        "Micronésia - FM",
        "Moçambique - MZ",
        "Moldávia - MD",
        "Mónaco - MC",
        "Mongólia - MN",
        "Montenegro - ME",
        "Myanmar - MM",
        "Namíbia - NA",
        "Nauru - NR",
        "Nepal - NP",
        "Nicarágua - NI",
        "Níger - NE",
        "Nigéria - NG",
        "Noruega - NO",
        "Nova Zelândia - NZ",
        "Omã - OM",
        "Países Baixos - NL",
        "Paquistão - PK",
        "Palau - PW",
        "Panamá - PA",
        "Papua-Nova Guiné - PG",
        "Paraguai - PY",
        "Peru - PE",
        "Polónia - PL",
        "Portugal - PT",
        "Quénia - KE",
        "Quirguistão - KG",
        "Reino Unido - GB",
        "República Centro-Africana - CF",
        "República Checa - CZ",
        "República Dominicana - DO",
        "Roménia - RO",
        "Ruanda - RW",
        "Rússia - RU",
        "Salvador - SV",
        "Samoa - WS",
        "Santa Lúcia - LC",
        "São Tomé e Príncipe - ST",
        "Senegal - SN",
        "Serra Leoa - SL",
        "Sérvia - RS",
        "Singapura - SG",
        "Síria - SY",
        "Somália - SO",
        "Sri Lanka - LK",
        "Suazilândia (Eswatini) - SZ",
        "Sudão - SD",
        "Sudão do Sul - SS",
        "Suécia - SE",
        "Suíça - CH",
        "Suriname - SR",
        "Tailândia - TH",
        "Taiwan - TW",
        "Tanzânia - TZ",
        "Timor-Leste - TL",
        "Togo - TG",
        "Tonga - TO",
        "Trindade e Tobago - TT",
        "Tunísia - TN",
        "Turquia - TR",
        "Ucrânia - UA",
        "Uganda - UG",
        "Uruguai - UY",
        "Uzbequistão - UZ",
        "Vanuatu - VU",
        "Vaticano - VA",
        "Venezuela - VE",
        "Vietname - VN",
        "Zâmbia - ZM",
        "Zimbabué - ZW"
    };

    /** Código do cliente "Consumidor Final" criado com a base de dados. */
    private static final int CODIGO_CONSUMIDOR_FINAL = 1;

    private static final int QUEBRA_COLUNAS = 1150;
    private static final int QUEBRA_RODAPE = 760;

    private static final String[] DURACOES = {"--seleccione--", "01 Mês", "02 Meses", "03 Meses", "04 Meses",
        "05 Meses", "06 Meses", "07 Meses", "8 Meses", "9 Meses", "10 Meses", "11 Meses", "12 Meses"};

    /** Janela aberta: a lupa (BuscaClienteVisao) devolve-lhe o cliente escolhido. */
    private static ClienteVisao instanciaAberta;

    // ------------------------------------------------------------------
    // Dependências
    // ------------------------------------------------------------------
    private final BDConexao conexao;
    private final Frame parent;
    private final ClientesController clientesController;
    private final ProdutosController produtosController;
    private final TipoProdutosController tipoProdutoController;
    private final FamiliasController familiaController;
    private final MesRhController mesRhController;
    private final ConfiguracaoMesComecoController configuracaoMesComecoController;

    // ------------------------------------------------------------------
    // Estado
    // ------------------------------------------------------------------
    /** Cliente em edição; null quando se está a cadastrar um cliente novo. */
    private TbCliente clienteActual;
    /** Última validação do NIF e a chave (nif|país) a que se refere. */
    private Resultado resultadoNif;
    private String chaveValidada;
    private boolean aValidar;

    // ------------------------------------------------------------------
    // Componentes
    // ------------------------------------------------------------------
    private final Selo seloEstado = new Selo("Novo cliente");

    private final ComboModerno<String> cmbPais = new ComboModerno<>(new DefaultComboBoxModel<>(PAISES));
    private final CampoModerno txtNif = campo();
    private final BotaoModerno btnValidarNif = new BotaoModerno("Validar na AGT", Tema.PRIMARIA, false);
    private final PainelEstadoNif painelNif = new PainelEstadoNif();

    private final CampoModerno txtNome = campo();
    private final CampoModerno txtEndereco = campo();
    private final CampoModerno txtTelefone = campo();
    private final CampoModerno txtEmail = campo();
    private final CampoModerno txtDesconto = campo();

    private final CampoPesquisa txtPesquisa = new CampoPesquisa("Pesquisar por nome, NIF ou telefone…");
    private final CampoPesquisa txtCodigo = new CampoPesquisa("Código");
    private final BotaoModerno btnLupa = new BotaoModerno("", "/imagens/proucura.png", Tema.PRIMARIA, false);
    private final DefaultTableModel modeloClientes = modeloSomenteLeitura(
            "N.º", "Cliente", "NIF", "Telefone", "Endereço", "Email", "Desc. %");
    private final TabelaModerna tabelaClientes = new TabelaModerna(modeloClientes);
    private final TableRowSorter<DefaultTableModel> ordenador = new TableRowSorter<>(modeloClientes);
    private final JLabel lbTotal = nota("");

    @SuppressWarnings("rawtypes")
    private final ComboModerno cmbFamilia = new ComboModerno();
    @SuppressWarnings("rawtypes")
    private final ComboModerno cmbSubFamilia = new ComboModerno();
    @SuppressWarnings("rawtypes")
    private final ComboModerno cmbProduto = new ComboModerno();
    @SuppressWarnings("rawtypes")
    private final ComboModerno cmbMesComeco = new ComboModerno();
    private final ComboModerno<String> cmbDuracao = new ComboModerno<>(new DefaultComboBoxModel<>(DURACOES));
    private final BotaoModerno btnAdicionarServico = new BotaoModerno("Adicionar serviço", Tema.PRIMARIA, false);
    private final BotaoModerno btnRemoverServico = new BotaoModerno("Remover", Tema.PERIGO, true);
    private final DefaultTableModel modeloServicos = modeloSomenteLeitura("Cód.", "Serviço", "Mês de começo", "Duração");
    private final TabelaModerna tabelaServicos = new TabelaModerna(modeloServicos);
    private final JLabel lbServicosAviso = nota("Seleccione ou salve um cliente para gerir as mensalidades.");

    private final BotaoModerno btnNovo = new BotaoModerno("Novo", Tema.PRIMARIA, true);
    private final BotaoModerno btnEliminar = new BotaoModerno("Eliminar", "/imagens/eliminar_16x16.png", Tema.PERIGO, true);
    private final BotaoModerno btnGuardar = new BotaoModerno("Salvar cliente", "/imagens/salvar_16x16.png", Tema.SUCESSO, false);
    private final BotaoModerno btnSair = new BotaoModerno("Sair", Tema.NEUTRA, false);

    public ClienteVisao(java.awt.Frame parent, boolean modal, BDConexao conexao) {
        super(parent, modal);
        this.parent = parent;
        this.conexao = conexao;
        clientesController = new ClientesController(conexao);
        produtosController = new ProdutosController(conexao);
        tipoProdutoController = new TipoProdutosController(conexao);
        familiaController = new FamiliasController(conexao);
        mesRhController = new MesRhController(conexao.getConnectionAtiva());
        configuracaoMesComecoController = new ConfiguracaoMesComecoController(conexao.getConnectionAtiva());

        aplicarCorDoNegocio();
        setTitle("DVML · Clientes");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        configurarComponentes();
        montarLayout();
        ligarEventos();
        carregarCombosMensalidade();

        instanciaAberta = this;
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                if (instanciaAberta == ClienteVisao.this) {
                    instanciaAberta = null;
                }
            }

            @Override
            public void windowOpened(WindowEvent e) {
                txtNif.requestFocusInWindow();
            }
        });

        actualizarLista();
        novoCliente();
        setLocationRelativeTo(null);
    }

    /**
     * Chamado pela lupa de pesquisa (BuscaClienteVisao) com o cliente
     * escolhido.
     */
    public static void importarDados(TbCliente cliente) {
        if (instanciaAberta != null && cliente != null) {
            instanciaAberta.carregarCliente(cliente.getCodigo());
        }
    }

    // ==================================================================
    // Construção
    // ==================================================================
    private static CampoModerno campo() {
        CampoModerno c = new CampoModerno();
        c.setFont(Tema.CAMPO);
        dimensionar(c, 80, Tema.ALTURA_CAMPO);
        return c;
    }

    private static DefaultTableModel modeloSomenteLeitura(String... colunas) {
        return new DefaultTableModel(new Object[0][], colunas) {
            @Override
            public boolean isCellEditable(int linha, int coluna) {
                return false;
            }
        };
    }

    private void aplicarCorDoNegocio() {
        try {
            TbDadosInstituicao dados = new DadosInstituicaoController(conexao).findByCodigo(1);
            boolean farmacia = dados != null && "Farmacia".equalsIgnoreCase(dados.getNegocio());
            Tema.definirCorNegocio(farmacia ? new Color(140, 198, 63) : new Color(0, 255, 255));
        } catch (Exception e) {
            // mantém a cor por omissão do tema
        }
    }

    private void configurarComponentes() {
        cmbPais.setSelectedItem(PAIS_ANGOLA);
        cmbPais.setMaximumRowCount(16);
        dimensionar(cmbPais, 160, Tema.ALTURA_CAMPO);

        txtNif.setToolTipText("<html>Formatos aceites pela AGT:<br>"
                + "• Empresa / NIF da AGT: 10 dígitos (ex.: 5417033855)<br>"
                + "• Pessoa singular: nº do BI (ex.: 000067259ME013)<br>"
                + "• Consumidor final: 999999999</html>");
        btnValidarNif.setIcon(new IconeLinha(IconeLinha.Tipo.PESQUISAR));
        btnValidarNif.setToolTipText("Confirmar o NIF no serviço da AGT (F5)");
        dimensionar(btnValidarNif, btnValidarNif.getPreferredSize().width, Tema.ALTURA_CAMPO);

        txtDesconto.setHorizontalAlignment(JTextField.RIGHT);
        txtTelefone.setToolTipText("Telefone ou telemóvel do cliente");

        dimensionar(txtCodigo, 120, Tema.ALTURA_CAMPO);
        txtCodigo.setToolTipText("Código do cliente (Enter para abrir)");
        btnLupa.setToolTipText("Pesquisa avançada de clientes");
        dimensionar(btnLupa, 44, Tema.ALTURA_CAMPO);
        dimensionar(txtPesquisa, 200, Tema.ALTURA_CAMPO);

        tabelaClientes.setRowSorter(ordenador);
        tabelaClientes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        larguras(tabelaClientes, 55, 250, 135, 105);
        // Endereço, email e desconto ficam no modelo, mas fora da grelha: a
        // lista mostra só o essencial e lê-se sem cortes (a ficha mostra o resto).
        for (int i = tabelaClientes.getColumnCount() - 1; i >= 4; i--) {
            tabelaClientes.removeColumn(tabelaClientes.getColumnModel().getColumn(i));
        }

        for (JComponent combo : new JComponent[]{cmbFamilia, cmbSubFamilia, cmbProduto, cmbMesComeco, cmbDuracao}) {
            dimensionar(combo, 120, Tema.ALTURA_CAMPO);
        }

        tabelaServicos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        larguras(tabelaServicos, 60, 240, 140, 110);
        tabelaServicos.setPreferredScrollableViewportSize(new Dimension(300, 4 * TabelaModerna.ALTURA_LINHA));

        btnNovo.setIcon(new IconeLinha(IconeLinha.Tipo.CLIENTE_NOVO));
        btnSair.setIcon(new IconeLinha(IconeLinha.Tipo.SAIR));
        btnAdicionarServico.setIcon(new IconeLinha(IconeLinha.Tipo.ADICIONAR));
        btnRemoverServico.setIcon(new IconeLinha(IconeLinha.Tipo.REMOVER));
        btnNovo.setToolTipText("Novo cliente (F2)");
        btnGuardar.setToolTipText("Validar o NIF na AGT e guardar (Ctrl+S)");
        btnSair.setToolTipText("Fechar (Esc)");
    }

    private static void larguras(TabelaModerna tabela, int... larguras) {
        for (int i = 0; i < larguras.length && i < tabela.getColumnCount(); i++) {
            tabela.getColumnModel().getColumn(i).setPreferredWidth(larguras[i]);
        }
    }

    private void montarLayout() {
        FundoJanela raiz = new FundoJanela();
        Cabecalho cabecalho = new Cabecalho("C", "Cadastro de Clientes",
                "NIF validado na AGT · contactos · condições comerciais · mensalidades");
        cabecalho.adicionarDireita(seloEstado);
        cabecalho.permitirArrastar(this);
        raiz.add(cabecalho, BorderLayout.NORTH);
        raiz.add(criarConteudo(), BorderLayout.CENTER);
        raiz.add(criarRodape(), BorderLayout.SOUTH);
        setContentPane(raiz);

        Rectangle ecra = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        setSize(Math.min(1340, ecra.width - 40), Math.min(880, ecra.height - 40));
        setMinimumSize(new Dimension(Math.min(700, ecra.width), Math.min(560, ecra.height)));
    }

    private JComponent criarConteudo() {
        final JPanel fiscal = criarCartaoFiscal();
        final JPanel dados = criarCartaoDados();
        final JPanel lista = criarCartaoLista();
        final JPanel mensalidades = criarCartaoMensalidades();

        PainelResponsivo corpo = new PainelResponsivo(new int[]{QUEBRA_COLUNAS}, (p, modo) -> {
            if (modo == 0) {
                p.add(coluna(fiscal, dados, lista, mensalidades),
                        gbc(0, 0, 1, 1, 1, 1, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0)));
            } else {
                p.add(coluna(fiscal, dados, mensalidades),
                        gbc(0, 0, 1, 1, 0.55, 1, GridBagConstraints.BOTH, new Insets(0, 0, 0, Tema.ESPACO)));
                p.add(lista, gbc(1, 0, 1, 1, 0.45, 1, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0)));
            }
        });

        PainelRolavel conteudo = new PainelRolavel(new GridBagLayout(), 620);
        conteudo.setBorder(BorderFactory.createEmptyBorder(16, 20, 8, 20));
        conteudo.add(corpo, gbc(0, 0, 1, 1, 1, 1, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0)));
        return new RolagemModerna(conteudo);
    }

    private JPanel criarCartaoFiscal() {
        Cartao cartao = new Cartao("Identificação Fiscal", "o NIF é confirmado na AGT antes de guardar", Tema.PRIMARIA);
        cartao.setLayout(new GridBagLayout());

        JPanel nif = transparente(new BorderLayout(8, 0));
        nif.add(txtNif, BorderLayout.CENTER);
        nif.add(btnValidarNif, BorderLayout.EAST);

        cartao.add(rotulo("País"), gbc(0, 0, 1, 1, 0, 0, GridBagConstraints.NONE, new Insets(0, 0, 5, 0)));
        cartao.add(cmbPais, gbc(0, 1, 1, 1, 0.3, 0, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 12, 0)));
        cartao.add(rotuloObrigatorio("NIF"), gbc(1, 0, 1, 1, 0, 0, GridBagConstraints.NONE, new Insets(0, 12, 5, 0)));
        cartao.add(nif, gbc(1, 1, 1, 1, 0.7, 0, GridBagConstraints.HORIZONTAL, new Insets(0, 12, 12, 0)));
        cartao.add(painelNif, gbc(0, 2, 2, 1, 1, 0, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0)));
        fecharGrade(cartao, 3, 2);
        return cartao;
    }

    private JPanel criarCartaoDados() {
        Cartao cartao = new Cartao("Dados do Cliente", "identificação, contactos e condições comerciais", Tema.PRIMARIA);
        cartao.setLayout(new GridBagLayout());
        adicionarCampo(cartao, 0, 0, 2, rotuloObrigatorio("Nome / Designação social"), txtNome);
        adicionarCampo(cartao, 0, 1, 2, rotuloObrigatorio("Endereço"), txtEndereco);
        adicionarCampo(cartao, 0, 2, 1, rotulo("Telefone"), txtTelefone);
        adicionarCampo(cartao, 1, 2, 1, rotulo("Email"), txtEmail);
        adicionarCampo(cartao, 0, 3, 1, rotulo("Desconto (%)"), txtDesconto);
        fecharGrade(cartao, 8, 2);
        return cartao;
    }

    private JPanel criarCartaoLista() {
        Cartao cartao = new Cartao("Clientes Registados", "clique num cliente para o abrir", Tema.INFO);
        cartao.setLayout(new GridBagLayout());

        JPanel pesquisa = transparente(new BorderLayout(8, 0));
        pesquisa.add(txtPesquisa, BorderLayout.CENTER);
        JPanel porCodigo = transparente(new BorderLayout(6, 0));
        porCodigo.add(txtCodigo, BorderLayout.CENTER);
        porCodigo.add(btnLupa, BorderLayout.EAST);
        pesquisa.add(porCodigo, BorderLayout.EAST);

        RolagemModerna rolagem = new RolagemModerna(tabelaClientes);
        PainelTabela painelTabela = new PainelTabela(rolagem);
        painelTabela.setPreferredSize(new Dimension(420, 380));
        painelTabela.setMinimumSize(new Dimension(200, 220));

        cartao.add(pesquisa, gbc(0, 0, 1, 1, 1, 0, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 12, 0)));
        cartao.add(painelTabela, gbc(0, 1, 1, 1, 1, 1, GridBagConstraints.BOTH, new Insets(0, 0, 8, 0)));
        cartao.add(lbTotal, gbc(0, 2, 1, 1, 1, 0, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0)));
        return cartao;
    }

    private JPanel criarCartaoMensalidades() {
        Cartao cartao = new Cartao("Serviços de Mensalidade", "inscrição do cliente em serviços mensais", Tema.INFO);
        cartao.setLayout(new GridBagLayout());
        adicionarCampo(cartao, 0, 0, 1, rotulo("Família"), cmbFamilia);
        adicionarCampo(cartao, 1, 0, 1, rotulo("Sub-família"), cmbSubFamilia);
        adicionarCampo(cartao, 0, 1, 2, rotulo("Serviço"), cmbProduto);
        adicionarCampo(cartao, 0, 2, 1, rotulo("Mês de começo"), cmbMesComeco);
        adicionarCampo(cartao, 1, 2, 1, rotulo("Duração"), cmbDuracao);

        JPanel accoes = transparente(new FlowLayout(FlowLayout.LEFT, 0, 0));
        accoes.add(btnAdicionarServico);
        accoes.add(Box.createHorizontalStrut(10));
        accoes.add(btnRemoverServico);
        accoes.add(Box.createHorizontalStrut(14));
        accoes.add(lbServicosAviso);
        cartao.add(accoes, gbc(0, 6, 2, 1, 1, 0, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 12, 0)));

        PainelTabela painelTabela = new PainelTabela(new RolagemModerna(tabelaServicos));
        cartao.add(painelTabela, gbc(0, 7, 2, 1, 1, 1, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0)));
        return cartao;
    }

    private JPanel criarRodape() {
        JPanel esquerda = transparente(new FlowLayout(FlowLayout.LEFT, 10, 0));
        esquerda.add(btnNovo);
        esquerda.add(btnEliminar);
        esquerda.add(Box.createHorizontalStrut(6));
        esquerda.add(nota("* obrigatório  ·  F2 novo  ·  F5 validar NIF  ·  Ctrl+S guardar  ·  Esc sair"));

        JPanel direita = transparente(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        direita.add(btnGuardar);
        direita.add(btnSair);
        return new Rodape(esquerda, direita, QUEBRA_RODAPE);
    }

    // ==================================================================
    // Eventos
    // ==================================================================
    private void ligarEventos() {
        txtNif.getDocument().addDocumentListener(aoMudar(this::aoAlterarNif));
        cmbPais.addActionListener(e -> aoAlterarNif());
        txtNif.addActionListener(e -> validarNif(null));
        txtNif.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                if (!e.isTemporary() && resultadoNif == null && !aValidar
                        && !ValidadorNifAGT.normalizar(txtNif.getText()).isEmpty()) {
                    validarNif(null);
                }
            }
        });
        btnValidarNif.addActionListener(e -> validarNif(null));
        painelNif.getBotaoUsarDados().addActionListener(e -> usarDadosAGT(true));

        txtNome.addActionListener(e -> txtEndereco.requestFocusInWindow());
        txtEndereco.addActionListener(e -> txtTelefone.requestFocusInWindow());
        txtTelefone.addActionListener(e -> txtEmail.requestFocusInWindow());
        txtEmail.addActionListener(e -> txtDesconto.requestFocusInWindow());
        txtDesconto.addActionListener(e -> guardar());
        for (CampoModerno c : new CampoModerno[]{txtNome, txtEndereco, txtEmail, txtDesconto}) {
            c.getDocument().addDocumentListener(aoMudar(() -> marcarErro(c, false)));
        }

        txtPesquisa.getDocument().addDocumentListener(aoMudar(this::filtrarLista));
        txtCodigo.addActionListener(e -> abrirPorCodigo());
        btnLupa.addActionListener(e -> abrirLupa());
        tabelaClientes.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                int linha = tabelaClientes.getSelectedRow();
                if (linha >= 0) {
                    Object codigo = tabelaClientes.getValueAt(linha, 0);
                    carregarCliente(Integer.parseInt(String.valueOf(codigo)));
                }
            }
        });

        cmbFamilia.addActionListener(e -> recarregarSubFamilias());
        cmbSubFamilia.addActionListener(e -> recarregarServicos());
        btnAdicionarServico.addActionListener(e -> adicionarServico());
        btnRemoverServico.addActionListener(e -> removerServico());
        tabelaServicos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                mostrarServicoSeleccionado();
            }
        });

        btnNovo.addActionListener(e -> novoCliente());
        btnEliminar.addActionListener(e -> eliminar());
        btnGuardar.addActionListener(e -> guardar());
        btnSair.addActionListener(e -> dispose());

        JComponent raiz = getRootPane();
        atalho(raiz, "F2", this::novoCliente);
        atalho(raiz, "F5", () -> validarNif(null));
        atalho(raiz, "ctrl S", this::guardar);
        atalho(raiz, "ESCAPE", this::dispose);
    }

    private static DocumentListener aoMudar(final Runnable accao) {
        return new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                accao.run();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                accao.run();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        };
    }

    // ==================================================================
    // Validação do NIF
    // ==================================================================
    private String paisSeleccionado() {
        Object item = cmbPais.getSelectedItem();
        return item == null ? PAIS_ANGOLA : item.toString();
    }

    private String paisNome() {
        String p = paisSeleccionado();
        int i = p.lastIndexOf(" - ");
        return i < 0 ? p.trim() : p.substring(0, i).trim();
    }

    private String paisIso() {
        String p = paisSeleccionado();
        int i = p.lastIndexOf(" - ");
        return i < 0 ? "" : p.substring(i + 3).trim();
    }

    private String chaveActual() {
        return ValidadorNifAGT.normalizar(txtNif.getText()) + "|" + paisIso();
    }

    /** O NIF ou o país mudaram: a validação anterior deixa de valer. */
    private void aoAlterarNif() {
        if (resultadoNif != null && chaveActual().equals(chaveValidada)) {
            return;
        }
        resultadoNif = null;
        chaveValidada = null;
        marcarErro(txtNif, false);
        if (!aValidar) {
            painelNif.porValidar();
        }
    }

    /**
     * Valida o NIF (formato + AGT) sem bloquear a janela e, no fim, executa
     * {@code depois} (pode ser null). Se o NIF já foi validado, executa
     * {@code depois} logo.
     */
    private void validarNif(final Runnable depois) {
        if (aValidar) {
            return;
        }
        final String nif = ValidadorNifAGT.normalizar(txtNif.getText());
        final String iso = paisIso();
        final String chave = nif + "|" + iso;

        if (resultadoNif != null && chave.equals(chaveValidada)) {
            if (depois != null) {
                depois.run();
            }
            return;
        }

        // Formato inválido: resposta imediata, sem consultar a AGT.
        if (ValidadorNifAGT.validarFormato(nif, iso) != null) {
            aplicarResultado(ValidadorNifAGT.validar(nif, iso));
            if (depois != null) {
                depois.run();
            }
            return;
        }

        aValidar = true;
        ocupado(true);
        painelNif.aConsultar();
        new SwingWorker<Resultado, Void>() {
            @Override
            protected Resultado doInBackground() {
                return ValidadorNifAGT.validar(nif, iso);
            }

            @Override
            protected void done() {
                aValidar = false;
                ocupado(false);
                if (!chave.equals(chaveActual())) {
                    // o utilizador alterou o NIF entretanto
                    painelNif.porValidar();
                    return;
                }
                try {
                    aplicarResultado(get());
                } catch (InterruptedException | ExecutionException e) {
                    painelNif.porValidar();
                    return;
                }
                if (depois != null) {
                    depois.run();
                }
            }
        }.execute();
    }

    private void aplicarResultado(Resultado r) {
        if (!r.getNif().isEmpty() && !txtNif.getText().equals(r.getNif())) {
            txtNif.setText(r.getNif()); // mostra o NIF normalizado
        }
        resultadoNif = r;
        chaveValidada = chaveActual();
        painelNif.mostrar(r);
        marcarErro(txtNif, r.isBloqueante());
        if (r.isConfirmado() && r.getTipo() != ValidadorNifAGT.Tipo.CONSUMIDOR_FINAL) {
            usarDadosAGT(false);
        }
    }

    /**
     * Copia para o formulário os dados do contribuinte registados na AGT.
     *
     * @param substituir false = só preenche os campos vazios
     */
    private void usarDadosAGT(boolean substituir) {
        Resultado r = resultadoNif;
        if (r == null || !r.isConfirmado()) {
            return;
        }
        preencher(txtNome, r.getNome() == null ? null : MetodosUtil.normalizarCampo(r.getNome()), substituir);
        preencher(txtEndereco, r.getMorada(), substituir);
        preencher(txtTelefone, r.getTelefone(), substituir);
        preencher(txtEmail, r.getEmail(), substituir);
    }

    private static void preencher(JTextField campo, String valor, boolean substituir) {
        if (valor != null && !valor.trim().isEmpty() && (substituir || campo.getText().trim().isEmpty())) {
            campo.setText(valor.trim());
            campo.setCaretPosition(0);
        }
    }

    /** Decide, com base na validação, se o cliente pode ser guardado. */
    private boolean nifPermiteGuardar() {
        Resultado r = resultadoNif;
        if (!ConfirmacaoNif.permiteGuardar(this, r)) {
            if (r != null && r.isBloqueante()) {
                txtNif.requestFocusInWindow();
                txtNif.selectAll();
            }
            return false;
        }
        if (r.isConfirmado() && r.getTipo() != ValidadorNifAGT.Tipo.CONSUMIDOR_FINAL && r.getNome() != null
                && !mesmoNome(r.getNome(), txtNome.getText())) {
            int opcao = JOptionPane.showOptionDialog(this,
                    "O nome do cliente não corresponde ao contribuinte registado na AGT com este NIF:\n\n"
                    + "   No formulário:  " + txtNome.getText().trim() + "\n"
                    + "   Na AGT:            " + r.getNome() + "\n\n"
                    + "Confirme se o NIF pertence a este cliente.",
                    "Nome diferente do registado na AGT", JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.WARNING_MESSAGE, null,
                    new Object[]{"Usar o nome da AGT", "Manter o nome", "Cancelar"}, "Usar o nome da AGT");
            if (opcao == 0) {
                txtNome.setText(MetodosUtil.normalizarCampo(r.getNome()));
            } else if (opcao != 1) {
                return false;
            }
        }
        return true;
    }

    /**
     * Compara nomes ignorando maiúsculas, acentos, pontuação e espaços; um
     * nome que contém o outro (p.ex. com ", SA" no fim) conta como igual.
     */
    private static boolean mesmoNome(String a, String b) {
        String x = simplificar(a);
        String y = simplificar(b);
        return !x.isEmpty() && !y.isEmpty() && (x.contains(y) || y.contains(x));
    }

    private static String simplificar(String s) {
        String semAcentos = java.text.Normalizer.normalize(s == null ? "" : s, java.text.Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
        return semAcentos.toUpperCase().replaceAll("[^A-Z0-9]", "");
    }

    private boolean confirmar(String mensagem, String titulo) {
        return JOptionPane.showConfirmDialog(this, mensagem, titulo,
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION;
    }

    private void ocupado(boolean ocupado) {
        setCursor(ocupado ? Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR) : null);
        btnValidarNif.setEnabled(!ocupado);
        btnGuardar.setEnabled(!ocupado);
    }

    // ==================================================================
    // Salvar / alterar / eliminar
    // ==================================================================
    private void guardar() {
        if (aValidar || !camposValidos()) {
            return;
        }
        // Primeiro valida o NIF; só depois salva ou altera.
        validarNif(() -> {
            if (!nifPermiteGuardar()) {
                return;
            }
            if (clienteActual == null) {
                salvar();
            } else {
                alterar();
            }
        });
    }

    private boolean camposValidos() {
        if (txtNome.getText().trim().isEmpty()) {
            return invalido(txtNome, "Introduza o nome do cliente.");
        }
        if (txtEndereco.getText().trim().isEmpty()) {
            return invalido(txtEndereco, "Introduza o endereço do cliente.");
        }
        if (ValidadorNifAGT.normalizar(txtNif.getText()).isEmpty()) {
            return invalido(txtNif, "Introduza o NIF do cliente.\nSe o cliente não tiver NIF, use "
                    + ValidadorNifAGT.NIF_CONSUMIDOR_FINAL + " (Consumidor final).");
        }
        String email = txtEmail.getText().trim();
        if (!email.isEmpty() && !email.matches("[^@\\s]+@[^@\\s]+\\.[^@\\s]+")) {
            return invalido(txtEmail, "O email \"" + email + "\" não é válido.");
        }
        Double desconto = lerDesconto();
        if (desconto == null || desconto < 0 || desconto > 100) {
            return invalido(txtDesconto, "O desconto deve ser um número entre 0 e 100.");
        }
        return true;
    }

    private boolean invalido(JComponent campo, String mensagem) {
        marcarErro(campo, true);
        JOptionPane.showMessageDialog(this, mensagem, DVML.DVML_COMERCIAL, JOptionPane.WARNING_MESSAGE);
        campo.requestFocusInWindow();
        return false;
    }

    private Double lerDesconto() {
        String t = txtDesconto.getText().trim().replace(',', '.');
        if (t.isEmpty()) {
            return 0d;
        }
        try {
            return Double.parseDouble(t);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private void preencherEntidade(TbCliente c) {
        c.setNome(MetodosUtil.normalizarCampo(txtNome.getText()));
        c.setMorada(MetodosUtil.normalizarEndereco(txtEndereco.getText()));
        c.setNif(ValidadorNifAGT.normalizar(txtNif.getText()));
        c.setPais(paisNome());
        c.setPaisISO(paisIso());
        c.setTelefone(txtTelefone.getText().trim());
        c.setEmail(txtEmail.getText().trim());
        c.setPercentagemDesconto(lerDesconto());
    }

    private static boolean nifGenerico(String nif) {
        return ValidadorNifAGT.NIF_CONSUMIDOR_FINAL.equals(nif);
    }

    private void salvar() {
        TbCliente novo = new TbCliente();
        preencherEntidade(novo);
        try {
            if (clientesController.existeClienteNome(novo.getNome(), BDConexao.getConexao())) {
                aviso("Já existe um cliente com o nome \"" + novo.getNome() + "\".");
                return;
            }
            if (!nifGenerico(novo.getNif())
                    && clientesController.existeClienteNIF(novo.getNif(), BDConexao.getConexao())) {
                aviso("Já existe um cliente com o NIF " + novo.getNif() + ".");
                return;
            }
        } catch (java.sql.SQLException e) {
            erro("Erro ao verificar duplicados: " + e.getMessage());
            return;
        }

        BDConexao transaccao = BDConexao.getInstancia();
        DocumentosController.start(transaccao);
        boolean ok = false;
        try {
            ok = new ClientesController(transaccao).salvar(novo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!ok) {
            DocumentosController.rollback(transaccao);
            erro("Erro ao salvar o cliente.");
            return;
        }
        DocumentosController.commit(transaccao);
        actualizarLista();
        JOptionPane.showMessageDialog(this, "Cliente \"" + novo.getNome() + "\" salvo com sucesso.",
                DVML.DVML_COMERCIAL, JOptionPane.INFORMATION_MESSAGE);
        novoCliente();
    }

    private void alterar() {
        TbCliente cliente = clientesController.findByCodigo(clienteActual.getCodigo());
        if (cliente == null) {
            erro("Cliente não encontrado na base de dados.");
            return;
        }
        preencherEntidade(cliente);
        if (clientesController.existeClienteNomeParaOutroCliente(cliente.getNome(), cliente.getCodigo(),
                BDConexao.getConnection())) {
            aviso("Já existe outro cliente com o nome \"" + cliente.getNome() + "\".");
            return;
        }
        if (!nifGenerico(cliente.getNif()) && clientesController.existeClienteNIFParaOutroCliente(
                cliente.getNif(), cliente.getCodigo(), BDConexao.getConnection())) {
            aviso("Já existe outro cliente com o NIF " + cliente.getNif() + ".");
            return;
        }
        try {
            if (clientesController.actualizar(cliente)) {
                actualizarLista();
                JOptionPane.showMessageDialog(this, "Dados do cliente alterados com sucesso.",
                        DVML.DVML_COMERCIAL, JOptionPane.INFORMATION_MESSAGE);
                carregarCliente(cliente.getCodigo());
            } else {
                erro("Não foi possível alterar os dados do cliente.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            erro("Erro ao alterar os dados: " + e.getMessage());
        }
    }

    private void eliminar() {
        if (clienteActual == null) {
            aviso("Seleccione primeiro o cliente a eliminar.");
            return;
        }
        if (clienteActual.getCodigo() == CODIGO_CONSUMIDOR_FINAL) {
            aviso("O cliente \"Consumidor Final\" é usado nas vendas sem cliente e não pode ser eliminado.");
            return;
        }
        if (!confirmar("Tem a certeza de que pretende eliminar o cliente \"" + clienteActual.getNome() + "\"?",
                "Eliminar cliente")) {
            return;
        }
        boolean ok;
        try {
            ok = clientesController.eliminar(clienteActual.getCodigo());
        } catch (Exception e) {
            e.printStackTrace();
            ok = false;
        }
        if (ok) {
            actualizarLista();
            novoCliente();
            JOptionPane.showMessageDialog(this, "Cliente eliminado com sucesso.",
                    DVML.DVML_COMERCIAL, JOptionPane.INFORMATION_MESSAGE);
        } else {
            erro("Não foi possível eliminar este cliente.\nPossivelmente já está relacionado com vendas.");
        }
    }

    private void aviso(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem, DVML.DVML_COMERCIAL, JOptionPane.WARNING_MESSAGE);
    }

    private void erro(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem, DVML.DVML_COMERCIAL, JOptionPane.ERROR_MESSAGE);
    }

    // ==================================================================
    // Ficha do cliente
    // ==================================================================
    private void novoCliente() {
        clienteActual = null;
        for (JTextField c : new JTextField[]{txtNif, txtNome, txtEndereco, txtTelefone, txtEmail}) {
            c.setText("");
            marcarErro(c, false);
        }
        txtDesconto.setText("0");
        marcarErro(txtDesconto, false);
        cmbPais.setSelectedItem(PAIS_ANGOLA);
        resultadoNif = null;
        chaveValidada = null;
        painelNif.porValidar();

        seloEstado.setText("Novo cliente");
        btnGuardar.setText("Salvar cliente");
        btnEliminar.setEnabled(false);
        tabelaClientes.clearSelection();
        modeloServicos.setRowCount(0);
        activarMensalidades(false);
        txtNif.requestFocusInWindow();
    }

    private void carregarCliente(int codigo) {
        TbCliente c = clientesController.findByCodigo(codigo);
        if (c == null) {
            aviso("Não existe nenhum cliente com o código " + codigo + ".");
            return;
        }
        clienteActual = c;
        txtNome.setText(texto(c.getNome()));
        txtEndereco.setText(texto(c.getMorada()));
        txtTelefone.setText(texto(c.getTelefone()));
        txtEmail.setText(texto(c.getEmail()));
        txtDesconto.setText(formatarDesconto(c.getPercentagemDesconto()));
        seleccionarPais(c.getPaisISO());
        txtNif.setText(texto(c.getNif()));
        for (JTextField t : new JTextField[]{txtNif, txtNome, txtEndereco, txtTelefone, txtEmail, txtDesconto}) {
            marcarErro(t, false);
            t.setCaretPosition(0);
        }

        seloEstado.setText("Cliente nº " + c.getCodigo());
        btnGuardar.setText("Guardar alterações");
        btnEliminar.setEnabled(c.getCodigo() != CODIGO_CONSUMIDOR_FINAL);
        activarMensalidades(true);
        listarServicos();
        seleccionarNaLista(c.getCodigo());

        // Mostra logo a situação do NIF na AGT (em segundo plano).
        aoAlterarNif();
        if (!ValidadorNifAGT.normalizar(txtNif.getText()).isEmpty()) {
            validarNif(null);
        }
    }

    private void seleccionarPais(String iso) {
        if (iso != null && !iso.trim().isEmpty()) {
            for (String p : PAISES) {
                if (p.endsWith(" - " + iso.trim().toUpperCase())) {
                    cmbPais.setSelectedItem(p);
                    return;
                }
            }
        }
        cmbPais.setSelectedItem(PAIS_ANGOLA);
    }

    private static String formatarDesconto(Double d) {
        if (d == null) {
            return "0";
        }
        return d == Math.rint(d) ? String.valueOf(d.longValue()) : String.valueOf(d);
    }

    private static String texto(String s) {
        return s == null || "null".equalsIgnoreCase(s.trim()) ? "" : s.trim();
    }

    // ==================================================================
    // Lista de clientes
    // ==================================================================
    private void actualizarLista() {
        modeloClientes.setRowCount(0);
        List<TbCliente> clientes = clientesController.listarTodos();
        for (TbCliente c : clientes) {
            modeloClientes.addRow(new Object[]{
                c.getCodigo(), texto(c.getNome()), texto(c.getNif()), texto(c.getTelefone()),
                texto(c.getMorada()), texto(c.getEmail()), c.getPercentagemDesconto()
            });
        }
        filtrarLista();
    }

    private void filtrarLista() {
        String t = txtPesquisa.getText().trim();
        ordenador.setRowFilter(t.isEmpty() ? null
                : RowFilter.<DefaultTableModel, Integer>regexFilter("(?i)" + Pattern.quote(t), 0, 1, 2, 3));
        int visiveis = tabelaClientes.getRowCount();
        int total = modeloClientes.getRowCount();
        lbTotal.setText(visiveis == total
                ? total + (total == 1 ? " cliente registado" : " clientes registados")
                : visiveis + " de " + total + " clientes");
    }

    private void seleccionarNaLista(int codigo) {
        for (int i = 0; i < tabelaClientes.getRowCount(); i++) {
            if (String.valueOf(codigo).equals(String.valueOf(tabelaClientes.getValueAt(i, 0)))) {
                tabelaClientes.setRowSelectionInterval(i, i);
                tabelaClientes.scrollRectToVisible(tabelaClientes.getCellRect(i, 0, true));
                return;
            }
        }
        tabelaClientes.clearSelection();
    }

    private void abrirPorCodigo() {
        try {
            carregarCliente(Integer.parseInt(txtCodigo.getText().trim()));
        } catch (NumberFormatException e) {
            aviso("Introduza um código de cliente válido.");
        }
    }

    private void abrirLupa() {
        try {
            new BuscaClienteVisao(parent, true, clientesController, conexao).setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ==================================================================
    // Serviços de mensalidade
    // ==================================================================
    @SuppressWarnings("unchecked")
    private void carregarCombosMensalidade() {
        try {
            cmbFamilia.setModel(modelo(familiaController.getVector()));
            if (cmbFamilia.getItemCount() > 1) {
                cmbFamilia.setSelectedIndex(1);
            }
            cmbMesComeco.setModel(modelo(mesRhController.getVector()));
            recarregarSubFamilias();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("rawtypes")
    private static DefaultComboBoxModel modelo(Collection<?> itens) {
        return new DefaultComboBoxModel<Object>(new Vector<Object>(itens));
    }

    @SuppressWarnings("unchecked")
    private void recarregarSubFamilias() {
        try {
            cmbSubFamilia.setModel(modelo(tipoProdutoController.getVectorByIdFamilia(idFamilia())));
        } catch (Exception e) {
            cmbSubFamilia.setModel(new DefaultComboBoxModel<>());
        }
        recarregarServicos();
    }

    @SuppressWarnings("unchecked")
    private void recarregarServicos() {
        try {
            cmbProduto.setModel(modelo(produtosController.getVectorByIdTipoProduto(idTipoProduto())));
        } catch (Exception e) {
            cmbProduto.setModel(new DefaultComboBoxModel<>());
        }
    }

    private int idFamilia() {
        try {
            return familiaController.getFamiliaByDesignacao(String.valueOf(cmbFamilia.getSelectedItem())).getPkFamilia();
        } catch (Exception e) {
            return 0;
        }
    }

    private int idTipoProduto() {
        try {
            return tipoProdutoController.getTipoFamiliaByDesignacao(String.valueOf(cmbSubFamilia.getSelectedItem())).getCodigo();
        } catch (Exception e) {
            return 0;
        }
    }

    private void activarMensalidades(boolean activo) {
        cmbFamilia.setEnabled(false); // a família é fixa (serviços)
        cmbSubFamilia.setEnabled(activo);
        cmbProduto.setEnabled(activo);
        cmbMesComeco.setEnabled(activo);
        cmbDuracao.setEnabled(activo);
        btnAdicionarServico.setEnabled(activo);
        btnRemoverServico.setEnabled(activo);
        lbServicosAviso.setVisible(!activo);
    }

    private void listarServicos() {
        modeloServicos.setRowCount(0);
        if (clienteActual == null) {
            return;
        }
        List<ConfiguracaoMesComeco> itens = configuracaoMesComecoController.listarTodos(clienteActual.getCodigo());
        for (ConfiguracaoMesComeco item : itens) {
            TbProduto produto = produtosController.findByCod(item.getProdutoId());
            modeloServicos.addRow(new Object[]{
                item.getId(),
                produto == null ? "—" : produto.getDesignacao(),
                mesRhController.getDescricaoByIdMes(item.getMesId()),
                MetodosUtil.getNumeroFormatado(item.getDuracao()) + " " + (item.getDuracao() > 1 ? "Meses" : "Mês")
            });
        }
    }

    private void adicionarServico() {
        if (clienteActual == null) {
            aviso("Seleccione ou salve primeiro o cliente.");
            return;
        }
        Object servico = cmbProduto.getSelectedItem();
        if (servico == null) {
            aviso("Seleccione o serviço.");
            return;
        }
        if (cmbDuracao.getSelectedIndex() <= 0) {
            aviso("Seleccione a duração.");
            return;
        }
        String designacao = servico.toString().trim();
        for (int i = 0; i < modeloServicos.getRowCount(); i++) {
            if (String.valueOf(modeloServicos.getValueAt(i, 1)).trim().equalsIgnoreCase(designacao)) {
                aviso("Este serviço já foi adicionado.");
                return;
            }
        }
        TbProduto produto = produtosController.findByDesignacao(designacao);
        if (produto == null) {
            aviso("Serviço não encontrado.");
            return;
        }
        ConfiguracaoMesComeco cmc = new ConfiguracaoMesComeco();
        cmc.setDataCadastro(new Date());
        cmc.setMesId(cmbMesComeco.getSelectedIndex() + 1);
        cmc.setProdutoId(produto.getCodigo());
        cmc.setUsuarioId(0);
        cmc.setDuracao(cmbDuracao.getSelectedIndex());
        cmc.setClienteId(clienteActual.getCodigo());
        if (configuracaoMesComecoController.salvar(cmc)) {
            listarServicos();
        } else {
            erro("Erro ao configurar o serviço.");
        }
    }

    private void mostrarServicoSeleccionado() {
        int linha = tabelaServicos.getSelectedRow();
        if (linha < 0) {
            return;
        }
        try {
            int id = Integer.parseInt(String.valueOf(modeloServicos.getValueAt(linha, 0)));
            ConfiguracaoMesComeco item = configuracaoMesComecoController.buscarPorId(id);
            TbProduto produto = produtosController.findByCod(item.getProdutoId());
            TbTipoProduto tipo = (TbTipoProduto) tipoProdutoController.findById(produto.getCodTipoProduto().getCodigo());
            cmbSubFamilia.setSelectedItem(tipo.getDesignacao());
            cmbProduto.setSelectedItem(produto.getDesignacao());
            cmbDuracao.setSelectedIndex(Math.max(0, Math.min(item.getDuracao(), DURACOES.length - 1)));
            cmbMesComeco.setSelectedItem(modeloServicos.getValueAt(linha, 2));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void removerServico() {
        int linha = tabelaServicos.getSelectedRow();
        if (linha < 0) {
            aviso("Seleccione o serviço a remover.");
            return;
        }
        if (!confirmar("Remover o serviço \"" + modeloServicos.getValueAt(linha, 1) + "\" deste cliente?",
                "Remover serviço")) {
            return;
        }
        try {
            int id = Integer.parseInt(String.valueOf(modeloServicos.getValueAt(linha, 0)));
            if (configuracaoMesComecoController.deletar(id)) {
                listarServicos();
            } else {
                erro("Falha ao remover o serviço.");
            }
        } catch (Exception e) {
            erro("Falha ao remover o serviço.");
        }
    }

    // ==================================================================
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            ClienteVisao dialog = new ClienteVisao(new javax.swing.JFrame(), true, BDConexao.getInstancia());
            dialog.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    System.exit(0);
                }
            });
            dialog.setVisible(true);
        });
    }
}
