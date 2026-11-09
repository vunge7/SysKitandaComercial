/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package visao;

import java.sql.Connection;
import com.formdev.flatlaf.intellijthemes.FlatCyanLightIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatDarkFlatIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatNordIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatOneDarkIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatVuesionIJTheme;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatMTMaterialLighterIJTheme;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatMTMaterialOceanicIJTheme;

import comercial.controller.DadosInstituicaoController;
import dao.CaixaDao;
import dao.DadosInstituicaoDao;
import dao.DocumentoDao;
import dao.EmpresaDao;
import dao.UsuarioDao;
import de.javasoft.plaf.synthetica.SyntheticaBlackStarLookAndFeel;
//import de.javasoft.plaf.synthetica.SyntheticaBlackStarLookAndFeel;
import entity.TbDadosInstituicao;
import entity.TbUsuario;
import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import javax.persistence.EntityManagerFactory;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
//import javax.swing.UIManager;
import util.BDConexao;
import util.BackupUsb;
import util.JPAEntityMannagerFactoryUtil;
//import util.LicensaUtil;
import util.MetodosUtil;
import static util.MetodosUtil.startBackGroundProcesses;
import util.SingleInstanceLock;
import util.SingleInstanceManager;
import util.cronjob.QuartzApp;
import util.cronjob.StockAutoCheckService;
import util.cronjob.ValidadorJob;
import util.fe.FEConfig;

/**
 *
 * @author Domingos Dala Vunge
 */
public class LoginVisao extends javax.swing.JFrame {

    private static EntityManagerFactory emf = JPAEntityMannagerFactoryUtil.em;
    private static DadosInstituicaoController dadosInstituicaoController;
    private UsuarioDao usuarioDao = new UsuarioDao(emf);
    private EmpresaDao empresaDao = new EmpresaDao(emf);
    private static CaixaDao caixaDao = new CaixaDao(emf);

    private DocumentoDao documentoDao = new DocumentoDao(emf);
    private DadosInstituicaoDao dadosInstituicaoDao = new DadosInstituicaoDao(emf);
    public Date horaTerminoVenda;
//    private LicensaUtil licensaUtil;
    private static BDConexao conexao;
    private TbDadosInstituicao d;
//    private int idUser;
    private boolean contagem = true;
    private Timer logoutTimer;
    private final int TIMEOUT = 60 * 60 * 1000; // 60 minutos em milissegundos
    private int id_user, id_empresa;
    private javax.swing.Timer piscarTimer;
    private boolean loginEmExecucao = false;

    public LoginVisao(BDConexao conexao) {

        initComponents();
        carregarAmbienteFE();        // <-- NOVA: lê o ambiente da BD
        adicionarFaixaAmbiente();    // <-- a faixa que já tinhas
        setLocationRelativeTo(null);
        setResizable(false);
        // aspecto web (como no SysKitandaHotel), sobre o initComponents
        util.ui.LayoutSeguro.aplicar( this, "Início de sessão", this::montarLayoutWeb );
        setVisible(true);

// Obtém a instância única e garante que a conexão está ativa
        conexao = BDConexao.getInstancia();
        conexao.getConnectionAtiva(); // força a ativação da conexão, se ainda não estiver

// Cria o controller com a instância de BDConexao já inicializada
        dadosInstituicaoController = new DadosInstituicaoController(conexao);

// Busca os dados da instituição (id = 1)
        d = (TbDadosInstituicao) dadosInstituicaoController.findById(1);

        mostrar_empresas();

        prazo();

        LoginVisao.setDefaultLookAndFeelDecorated(true);
        try {
            System.out.println("Data Ultimo: " + documentoDao.getMAxDataDoc().toString());
        } catch (Exception e) {
        }
//        verificar_data_sistema();
        txtUserName.requestFocus();
        //Envio de Email
        try {
            verificarEnvioEmail();
        } catch (Exception e) {
        }
        iniciarMonitoramento();
        iniciarPiscarLabelJLDataFecho();

    }


    // =====================================================================
    // LAYOUT WEB (mesmo design do início de sessão do SysKitandaHotel): os
    // componentes vêm do initComponents, com os seus eventos. Fundo azul com
    // a imagem, empresa à esquerda e cartão de acesso à direita; em janelas
    // estreitas fica só o cartão, ao centro. A faixa do ambiente (FE) mantém-se.
    // =====================================================================
    private static final int LARGURA_COM_PAINEL = 900;
    private char caracterSenhaWeb;

    /** Olho desenhado (a letra do Windows não tem este símbolo); riscado quando a senha está à vista. */
    private static final class IconeOlhoWeb implements javax.swing.Icon {

        private final boolean riscado;

        IconeOlhoWeb(boolean riscado) {
            this.riscado = riscado;
        }

        @Override
        public void paintIcon(java.awt.Component c, java.awt.Graphics g, int x, int y) {
            java.awt.Graphics2D g2 = util.ui.ComponentesWeb.suave(g);
            g2.translate(x, y);
            g2.setColor(util.ui.MenuWeb.AZUL_NOITE);
            g2.setStroke(new java.awt.BasicStroke(1.6f));
            g2.drawArc(1, 4, 18, 16, 25, 130);
            g2.drawArc(1, 0, 18, 16, 205, 130);
            g2.fillOval(7, 7, 6, 6);
            if (riscado) {
                g2.drawLine(3, 17, 17, 3);
            }
            g2.dispose();
        }

        @Override
        public int getIconWidth() {
            return 20;
        }

        @Override
        public int getIconHeight() {
            return 20;
        }
    }

    /** Cartão de acesso com largura fixa (nunca comprimido). */
    private static final class CartaoAcessoWeb extends util.ui.ComponentesWeb.Cartao {

        CartaoAcessoWeb() {
            super(null, null, new BorderLayout());
        }

        @Override
        public java.awt.Dimension getPreferredSize() {
            java.awt.Dimension d = super.getPreferredSize();
            d.width = 420;
            return d;
        }

        @Override
        public java.awt.Dimension getMinimumSize() {
            return getPreferredSize();
        }
    }

    /** Fundo: a imagem do formulário (img30) coberta por um gradiente azul. */
    private static final class PainelFundoWeb extends JPanel {

        private final java.awt.Image imagem = new javax.swing.ImageIcon(
                LoginVisao.class.getResource("/imagens/img30.png")).getImage();
        private java.awt.image.BufferedImage cache;

        @Override
        protected void paintComponent(java.awt.Graphics g) {
            int w = getWidth(), h = getHeight();
            if (cache == null || cache.getWidth() != w || cache.getHeight() != h) {
                cache = pintar(w, h);
            }
            g.drawImage(cache, 0, 0, null);
        }

        private java.awt.image.BufferedImage pintar(int w, int h) {
            java.awt.image.BufferedImage b = new java.awt.image.BufferedImage(Math.max(1, w), Math.max(1, h),
                    java.awt.image.BufferedImage.TYPE_INT_RGB);
            java.awt.Graphics2D g2 = b.createGraphics();
            g2.setRenderingHint(java.awt.RenderingHints.KEY_INTERPOLATION,
                    java.awt.RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            if (imagem.getWidth(null) > 0 && imagem.getHeight(null) > 0) {
                double escala = Math.max(w / (double) imagem.getWidth(null), h / (double) imagem.getHeight(null));
                int iw = (int) (imagem.getWidth(null) * escala), ih = (int) (imagem.getHeight(null) * escala);
                g2.drawImage(imagem, (w - iw) / 2, (h - ih) / 2, iw, ih, null);
            }
            // cores do tema do negócio (azul no comercial, verde na farmácia...)
            g2.setPaint(new java.awt.GradientPaint(0, 0, util.ui.ComponentesWeb.alfa(util.ui.MenuWeb.AZUL_NOITE, 235), w, 0,
                    util.ui.ComponentesWeb.alfa(util.ui.EstiloUI.AZUL_ESCURO, 150)));
            g2.fillRect(0, 0, w, h);
            g2.dispose();
            return b;
        }
    }

    /** A versão continua a ter um só sítio: o texto "KITANDA 1.x" do formulário. */
    private String versaoSoftwareWeb() {
        String[] partes = Soft1.getText().trim().split("\\s+");
        return partes.length > 1 ? partes[partes.length - 1] : "";
    }

    private void montarLayoutWeb() {
        JPanel fundo = new PainelFundoWeb();
        java.awt.GridBagLayout grelha = new java.awt.GridBagLayout();
        fundo.setLayout(grelha);

        JPanel painelEmpresa = criarPainelEmpresaWeb();
        java.awt.GridBagConstraints g = new java.awt.GridBagConstraints();
        g.gridx = 0;
        g.weightx = 1;
        g.weighty = 1;
        g.fill = java.awt.GridBagConstraints.BOTH;
        fundo.add(painelEmpresa, g);

        final CartaoAcessoWeb cartaoAcesso = criarCartaoAcessoWeb();
        final java.awt.GridBagConstraints gc = new java.awt.GridBagConstraints();
        gc.gridx = 1;
        gc.insets = new java.awt.Insets(20, 20, 20, 72);
        fundo.add(cartaoAcesso, gc);

        fundo.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                boolean largo = fundo.getWidth() >= LARGURA_COM_PAINEL;
                if (painelEmpresa.isVisible() != largo) {
                    painelEmpresa.setVisible(largo);
                    gc.insets = new java.awt.Insets(20, 20, 20, largo ? 72 : 20);
                    grelha.setConstraints(cartaoAcesso, gc);
                    fundo.revalidate();
                }
            }
        });

        // a faixa do ambiente (adicionarFaixaAmbiente) fica no topo: só se troca o centro
        java.awt.Container raiz = getContentPane();
        java.awt.Component centro = raiz.getLayout() instanceof BorderLayout
                ? ((BorderLayout) raiz.getLayout()).getLayoutComponent(BorderLayout.CENTER) : null;
        if (centro != null) {
            raiz.remove(centro);
            raiz.add(fundo, BorderLayout.CENTER);
        } else {
            raiz.removeAll();
            raiz.setLayout(new BorderLayout());
            raiz.add(fundo, BorderLayout.CENTER);
        }
        setIconImages(util.ui.MenuWeb.iconesAplicacao());
        setMinimumSize(new java.awt.Dimension(560, 640));
        setResizable(true);
        setExtendedState(getExtendedState() | MAXIMIZED_BOTH);
    }

    private JPanel criarPainelEmpresaWeb() {
        JLabel lbArea = new JLabel(util.ui.Tema.area().toUpperCase(new java.util.Locale("pt", "PT")));
        lbArea.setFont(util.ui.MenuWeb.rotuloGrupo("x", Color.WHITE).getFont().deriveFont(13f));
        lbArea.setForeground(util.ui.MenuWeb.DOURADO_CLARO);

        JEmpresa.setFont(new java.awt.Font("Georgia", java.awt.Font.BOLD, 28));
        JEmpresa.setForeground(Color.WHITE);

        JLNif.setFont(util.ui.EstiloUI.FONTE.deriveFont(15f));
        JLNif.setForeground(new Color(214, 226, 243));

        // contrato: verde fixo dentro do prazo; a 30 dias ou menos do fim, vermelho a piscar
        JLDataFecho.setFont(util.ui.EstiloUI.FONTE_NEGRITO.deriveFont(13f));
        JLDataFecho.setForeground(Color.WHITE);
        pilulaContratoWeb.add(JLDataFecho, BorderLayout.CENTER);
        JPanel linhaContrato = util.ui.ComponentesWeb.transparente(new BorderLayout());
        linhaContrato.add(pilulaContratoWeb, BorderLayout.WEST);
        // depois do construtor (os dados da instituição já foram lidos): verde ou a piscar
        javax.swing.SwingUtilities.invokeLater(this::iniciarPiscarLabelJLDataFecho);

        Soft.setFont(util.ui.EstiloUI.FONTE.deriveFont(13f));
        Soft.setForeground(new Color(214, 226, 243));

        JPanel linhaMarca = util.ui.ComponentesWeb.transparente(new BorderLayout());
        linhaMarca.add(new util.ui.MenuWeb.MarcaSoftware("KITANDA", versaoSoftwareWeb(), 64f,
                new Color(250, 232, 190), util.ui.MenuWeb.DOURADO, util.ui.MenuWeb.DOURADO_CLARO, true), BorderLayout.CENTER);

        JPanel bloco = util.ui.FormularioWeb.pilha(10, linhaMarca, lbArea, JEmpresa, JLNif, linhaContrato, Soft);

        // ao centro, na altura, para ficar alinhado com o cartão
        JPanel centro = util.ui.ComponentesWeb.transparente(new java.awt.GridBagLayout());
        java.awt.GridBagConstraints g = new java.awt.GridBagConstraints();
        g.weightx = 1;
        g.weighty = 1;
        g.fill = java.awt.GridBagConstraints.HORIZONTAL;
        g.anchor = java.awt.GridBagConstraints.WEST;
        centro.add(bloco, g);

        for (JLabel l : new JLabel[]{jLabel4, jLabel5}) {
            l.setFont(util.ui.EstiloUI.FONTE.deriveFont(12f));
            l.setForeground(new Color(190, 205, 225));
        }
        JPanel rodape = util.ui.ComponentesWeb.transparente(new java.awt.GridLayout(2, 1, 0, 2));
        rodape.add(jLabel4);
        rodape.add(jLabel5);

        JPanel painel = util.ui.ComponentesWeb.transparente(new BorderLayout());
        painel.setBorder(BorderFactory.createEmptyBorder(28, 64, 28, 48));
        painel.add(centro, BorderLayout.CENTER);
        painel.add(rodape, BorderLayout.SOUTH);
        // a largura é a que sobra do cartão: um texto comprido não pode empurrá-lo
        painel.setPreferredSize(new java.awt.Dimension(10, 10));
        painel.setMinimumSize(new java.awt.Dimension(0, 0));
        return painel;
    }

    private CartaoAcessoWeb criarCartaoAcessoWeb() {
        JLabel titulo = new JLabel("Iniciar sessão");
        titulo.setFont(new java.awt.Font("Georgia", java.awt.Font.BOLD, 25));
        titulo.setForeground(util.ui.MenuWeb.AZUL_NOITE);
        JLabel subtitulo = new JLabel("Introduza as suas credenciais");
        subtitulo.setFont(util.ui.EstiloUI.FONTE.deriveFont(13f));
        subtitulo.setForeground(util.ui.MenuWeb.TEXTO_SUAVE);

        jLabel6.setText("Empresa");
        jLabel6.setToolTipText(null);
        jLabel2.setText("Utilizador");
        jLabel2.setToolTipText(null);
        jLabel3.setText("Senha");
        for (JLabel l : new JLabel[]{jLabel6, jLabel2, jLabel3}) {
            l.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        }
        util.ui.FormularioWeb.combo(cmbEmpresa);
        cmbEmpresa.setPreferredSize(new java.awt.Dimension(120, 40));
        util.ui.FormularioWeb.campoTexto(txtUserName, pswSenha);
        for (javax.swing.JTextField t : new javax.swing.JTextField[]{txtUserName, pswSenha}) {
            t.setFont(util.ui.EstiloUI.FONTE.deriveFont(15f));
            t.setPreferredSize(new java.awt.Dimension(120, 42));
            t.setMinimumSize(new java.awt.Dimension(60, 42));
        }

        // olho: mostrar / esconder a senha (como no SysKitandaHotel)
        caracterSenhaWeb = pswSenha.getEchoChar();
        final javax.swing.JButton btVerSenha = new javax.swing.JButton();
        btVerSenha.addActionListener(e -> {
            boolean mostrar = pswSenha.getEchoChar() != 0;
            pswSenha.setEchoChar(mostrar ? (char) 0 : caracterSenhaWeb);
            btVerSenha.setIcon(new IconeOlhoWeb(mostrar));
            btVerSenha.setToolTipText(mostrar ? "Esconder a senha" : "Mostrar a senha");
            pswSenha.requestFocusInWindow();
        });
        JPanel campoSenha = util.ui.FormularioWeb.comBotao(pswSenha, btVerSenha, "Mostrar ou esconder a senha");
        btVerSenha.setPreferredSize(new java.awt.Dimension(42, 42));
        btVerSenha.setIcon(new IconeOlhoWeb(false));
        btVerSenha.setFocusable(false);

        util.ui.ComponentesWeb.botao(jButton1, util.ui.ComponentesWeb.Variante.PRIMARIO, null, "Entrar no sistema");
        jButton1.setIcon(null);
        jButton1.setText("Entrar");
        jButton1.setFont(util.ui.EstiloUI.FONTE_NEGRITO.deriveFont(14.5f));
        jButton1.setPreferredSize(new java.awt.Dimension(120, 44));
        util.ui.ComponentesWeb.botao(BTnSair, util.ui.ComponentesWeb.Variante.SECUNDARIO, null, "Fechar o sistema");
        BTnSair.setIcon(new util.ui.ComponentesWeb.IconeSair(16, util.ui.MenuWeb.AZUL_NOITE));
        BTnSair.setText("Sair do sistema");
        BTnSair.setPreferredSize(new java.awt.Dimension(120, 40));

        // em janelas estreitas o painel da empresa some: a marca continua aqui
        JPanel marcaCartao = util.ui.ComponentesWeb.transparente(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 0, 0));
        marcaCartao.add(new util.ui.MenuWeb.MarcaSoftware("KITANDA", versaoSoftwareWeb(), 20f,
                util.ui.MenuWeb.AZUL_MARINHO, util.ui.MenuWeb.AZUL_NOITE, util.ui.MenuWeb.DOURADO.darker(), false));

        JPanel cabecalho = util.ui.ComponentesWeb.transparente(new BorderLayout(14, 0));
        cabecalho.add(new util.ui.MenuWeb.Logotipo(50), BorderLayout.WEST);
        JPanel textos = util.ui.ComponentesWeb.transparente(new java.awt.GridLayout(2, 1));
        textos.add(titulo);
        textos.add(subtitulo);
        cabecalho.add(textos, BorderLayout.CENTER);

        JPanel conteudo = util.ui.FormularioWeb.pilha(10,
                cabecalho,
                javax.swing.Box.createVerticalStrut(6),
                util.ui.ComponentesWeb.campo(jLabel6, cmbEmpresa),
                util.ui.ComponentesWeb.campo(jLabel2, txtUserName),
                util.ui.ComponentesWeb.campo(jLabel3, campoSenha),
                javax.swing.Box.createVerticalStrut(4),
                jButton1,
                BTnSair,
                marcaCartao);
        conteudo.setBorder(BorderFactory.createEmptyBorder(16, 20, 12, 20));

        CartaoAcessoWeb cartao = new CartaoAcessoWeb();
        cartao.getCorpo().add(conteudo, BorderLayout.CENTER);
        return cartao;
    }

    /** Dias antes do fim do contrato em que o aviso começa a piscar. */
    private static final int DIAS_ALERTA_CONTRATO = 30;
    private final PilulaContratoWeb pilulaContratoWeb = new PilulaContratoWeb();
    private String textoContratoBase;

    /**
     * Contrato: com mais de 30 dias até ao fim fica verde, sem piscar; a 30 dias
     * ou menos (ou já terminado) pisca a vermelho. Pode ser chamado mais de uma
     * vez: não arranca dois temporizadores.
     */
    public void iniciarPiscarLabelJLDataFecho() {
        pararPiscarLabelJLDataFecho();
        java.util.Date fecho = null;
        try {
            TbDadosInstituicao dados = d != null ? d : (TbDadosInstituicao) dadosInstituicaoController.findById(1);
            fecho = dados == null ? null : dados.getDataFecho();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (textoContratoBase == null) {
            textoContratoBase = JLDataFecho.getText();
        }
        JLDataFecho.setForeground(Color.WHITE);
        if (fecho == null) {
            pilulaContratoWeb.setEstado(PilulaContratoWeb.NEUTRO);
            return;
        }
        long dias = diasAteWeb(fecho);
        String situacao = dias < 0 ? "contrato terminado"
                : dias == 0 ? "termina hoje"
                : dias == 1 ? "falta 1 dia"
                : "faltam " + dias + " dias";
        JLDataFecho.setText(textoContratoBase + "  ·  " + situacao);
        if (dias > DIAS_ALERTA_CONTRATO) {
            // dentro do prazo: verde, sem piscar
            pilulaContratoWeb.setEstado(PilulaContratoWeb.VERDE);
            JLDataFecho.setToolTipText("Licença dentro do prazo.");
            return;
        }
        JLDataFecho.setToolTipText("Contacte a DVML-COMERCIAL para renovar a licença.");
        pilulaContratoWeb.setEstado(PilulaContratoWeb.VERMELHO);
        piscarTimer = new javax.swing.Timer(500, new ActionListener() {
            private boolean ligado = true;

            @Override
            public void actionPerformed(ActionEvent e) {
                ligado = !ligado;
                pilulaContratoWeb.setEstado(ligado ? PilulaContratoWeb.VERMELHO : PilulaContratoWeb.NEUTRO);
            }
        });
        piscarTimer.start();
    }

    public void pararPiscarLabelJLDataFecho() {
        if (piscarTimer != null && piscarTimer.isRunning()) {
            piscarTimer.stop();
        }
    }

    /** Dias de calendário até à data de fecho: 0 = termina hoje, negativo = já terminou. */
    private static long diasAteWeb(java.util.Date dataFecho) {
        java.time.ZoneId zona = java.time.ZoneId.systemDefault();
        // new Date(...): a BD pode devolver java.sql.Date, que não converte para Instant
        java.time.LocalDate fecho = new java.util.Date(dataFecho.getTime()).toInstant().atZone(zona).toLocalDate();
        return java.time.temporal.ChronoUnit.DAYS.between(java.time.LocalDate.now(zona), fecho);
    }

    /** Pílula do contrato: verde (dentro do prazo), vermelho (alerta) ou neutra. */
    private static final class PilulaContratoWeb extends JPanel {

        static final int NEUTRO = 0, VERDE = 1, VERMELHO = 2;
        private int estado = NEUTRO;

        PilulaContratoWeb() {
            super(new BorderLayout());
            setOpaque(false);
            setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        }

        void setEstado(int e) {
            estado = e;
            repaint();
        }

        @Override
        protected void paintComponent(java.awt.Graphics g) {
            java.awt.Graphics2D g2 = util.ui.ComponentesWeb.suave(g);
            g2.setColor(estado == VERMELHO ? util.ui.ComponentesWeb.VERMELHO
                    : estado == VERDE ? new Color(21, 128, 61) : new Color(255, 255, 255, 30));
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 14, 14);
            g2.setColor(estado == VERMELHO ? new Color(254, 202, 202)
                    : estado == VERDE ? new Color(187, 247, 208) : new Color(255, 255, 255, 70));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 14, 14);
            g2.dispose();
        }
    }

    public LoginVisao(int idUser) {

        initComponents();
        carregarAmbienteFE();
        adicionarFaixaAmbiente();
        setLocationRelativeTo(null);
        setResizable(false);
        // o logout volta a este ecrã: o mesmo aspecto web do início de sessão
        util.ui.LayoutSeguro.aplicar( this, "Início de sessão", this::montarLayoutWeb );
        setVisible(true);

        conexao = BDConexao.getInstancia();
//        setTitle( "BEM VINDO AO " + DVML.NAME_SOFTWARE + " " + DVML.VERSION_SOFTWARE );

//        if (Objects.nonNull(conexao)) {
//            conexao.close();
//        }
        dadosInstituicaoController = new DadosInstituicaoController(conexao);
        d = (TbDadosInstituicao) dadosInstituicaoController.findById(1);
        horaTerminoVenda = d.getHoraTerminoVenda();
//        this.idUser = idUser;
//        licensaUtil = new LicensaUtil( conexao );
//        registerLog( conexao );
        mostrar_empresas();
        prazo();

        LoginVisao.setDefaultLookAndFeelDecorated(true);
        try {
            System.out.println("Data Ultimo: " + documentoDao.getMAxDataDoc().toString());
        } catch (Exception e) {
        }
//        verificar_data_sistema();
        txtUserName.requestFocus();
        //Envio de Email
        try {
            verificarEnvioEmail();
        } catch (Exception e) {
        }

        iniciarMonitoramento();

    }

    private void verificarEnvioEmail() {

        if (d.getEnviarEmail().equals("Sim")) {
            BTnSair.setVisible(false);
            QuartzApp.executarCronjob(ValidadorJob.class, (TbDadosInstituicao) dadosInstituicaoController.findById(1));
        } else {
            BTnSair.setVisible(true);
        }

    }

    public void mostrar_empresas() {
        cmbEmpresa.setModel(new DefaultComboBoxModel((Vector) empresaDao.getAllEmpresa()));
    }

    private void prazo() {

        TbDadosInstituicao dadosInstituicao = dadosInstituicaoDao.findTbDadosInstituicao(1);
        JEmpresa.setText("Empresa " + dadosInstituicao.getNome());
        JLNif.setText("NIF " + dadosInstituicao.getNif());
        JLDataFecho.setText("Contrato válido até " + (dadosInstituicao.getDataFecho().getDate()) + " / " + (dadosInstituicao.getDataFecho().getMonth() + 1) + " / " + (1900 + dadosInstituicao.getDataFecho().getYear()));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtUserName = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        pswSenha = new javax.swing.JPasswordField();
        cmbEmpresa = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        BTnSair = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        JLDataFecho = new javax.swing.JLabel();
        JLNif = new javax.swing.JLabel();
        JEmpresa = new javax.swing.JLabel();
        Soft = new javax.swing.JLabel();
        Soft1 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Login", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Adobe Garamond Pro Bold", 0, 24), new java.awt.Color(51, 51, 51))); // NOI18N
        jPanel1.setForeground(new java.awt.Color(51, 51, 51));

        jLabel2.setFont(new java.awt.Font("Trajan Pro", 1, 12)); // NOI18N
        jLabel2.setText("         User Name:");
        jLabel2.setToolTipText("User Name");

        txtUserName.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        txtUserName.setForeground(new java.awt.Color(51, 153, 0));
        txtUserName.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtUserNameActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Trajan Pro", 1, 12)); // NOI18N
        jLabel3.setText("               Senha:");

        pswSenha.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        pswSenha.setForeground(new java.awt.Color(51, 153, 0));
        pswSenha.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                pswSenhaActionPerformed(evt);
            }
        });

        cmbEmpresa.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbEmpresaActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Trajan Pro", 1, 11)); // NOI18N
        jLabel6.setText("         Empresa:");
        jLabel6.setToolTipText("User Name");

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/confirmacao.png"))); // NOI18N
        jButton1.setText("Confirmar");
        jButton1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButton1ActionPerformed(evt);
            }
        });

        jButton3.setFont(new java.awt.Font("Noteworthy", 1, 11)); // NOI18N
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens100x100/funcionarioAtivado100x100.png"))); // NOI18N
        jButton3.setText("BEM VINDO");

        BTnSair.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/sair32x32.png"))); // NOI18N
        BTnSair.setText("Sair");
        BTnSair.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                BTnSairActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cmbEmpresa, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtUserName)
                            .addComponent(pswSenha, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(BTnSair, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(22, 22, 22))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jButton3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbEmpresa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtUserName, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(8, 8, 8)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(pswSenha, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(BTnSair)
                .addGap(23, 23, 23))
        );

        jLabel4.setFont(new java.awt.Font("Tahoma", 2, 9)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("desenvolvido pela DVML-COMERCIAL , Lda / Contactos: 940537124 /923409284");

        jLabel5.setFont(new java.awt.Font("Tahoma", 2, 9)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("email: dvml.comerciall@gmail.com");

        JLDataFecho.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        JLDataFecho.setForeground(new java.awt.Color(255, 255, 255));
        JLDataFecho.setText("jLabel7");

        JLNif.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        JLNif.setForeground(new java.awt.Color(255, 255, 255));
        JLNif.setText("jLabel7");

        JEmpresa.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        JEmpresa.setForeground(new java.awt.Color(255, 255, 255));
        JEmpresa.setText("jLabel7");

        Soft.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        Soft.setForeground(new java.awt.Color(255, 255, 255));
        Soft.setText("KITANDA 1.1.   Sistema Certificado pela AGT. Nº 258 - Facturação Electónica Nº FE/110/AGT/2025");

        Soft1.setFont(new java.awt.Font("American Typewriter", 1, 48)); // NOI18N
        Soft1.setForeground(new java.awt.Color(255, 255, 255));
        Soft1.setText("KITANDA 1.1");

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/img30.png"))); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(JLNif, javax.swing.GroupLayout.PREFERRED_SIZE, 447, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(JEmpresa, javax.swing.GroupLayout.PREFERRED_SIZE, 482, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(JLDataFecho, javax.swing.GroupLayout.PREFERRED_SIZE, 538, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(29, 29, 29)
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(Soft, javax.swing.GroupLayout.PREFERRED_SIZE, 1073, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(517, 517, 517)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(Soft1, javax.swing.GroupLayout.PREFERRED_SIZE, 365, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(201, 201, 201)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(38, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 1138, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 6, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(74, 74, 74)
                .addComponent(JEmpresa)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JLNif)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Soft)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(48, 48, 48)
                        .addComponent(JLDataFecho)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 408, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addGap(221, 221, 221))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(Soft1, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(137, 137, 137))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 836, Short.MAX_VALUE)
                    .addContainerGap()))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtUserNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtUserNameActionPerformed
        // TODO add your handling code here:

        pswSenha.requestFocus();
//        try
//        {
//            Runtime.getRuntime().exec( "cmd /c osk" );
//            txtUserName.grabFocus();
//        }
//        catch ( IOException e )
//        {
//        }


    }//GEN-LAST:event_txtUserNameActionPerformed

    private void pswSenhaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pswSenhaActionPerformed
        // TODO add your handling code here:
        try {
            if (MetodosUtil.acesso_sistema()) {
//                if ( licensaUtil.licenca_valida( new Date() ) )
//                {
                entrar_sistema();
//                fazerBackupAgora();
//                }

            } else {
                JOptionPane.showMessageDialog(null, "Impossível aceder ao sistema.\nDeves abrir um novo ano económico.\nContacte o seu fornecedor.", "AVISO", JOptionPane.WARNING_MESSAGE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }//GEN-LAST:event_pswSenhaActionPerformed

    private void cmbEmpresaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbEmpresaActionPerformed
//        System.out.println("ID EMPRESA:"+getIdEmpresa());
    }//GEN-LAST:event_cmbEmpresaActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            if (MetodosUtil.acesso_sistema()) {
//                if ( licensaUtil.licenca_valida( new Date() ) )
//                {

                entrar_sistema();
//                fazerBackupAgora();
//                }

            } else {
                JOptionPane.showMessageDialog(null, "Impossível aceder ao sistema.\nDeves abrir um novo ano económico.\nContacte o seu fornecedor.", "AVISO", JOptionPane.WARNING_MESSAGE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void BTnSairActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_BTnSairActionPerformed
    {//GEN-HEADEREND:event_BTnSairActionPerformed
        try {
            dispose();
            // se já houver um backup em segundo plano a correr, espera por ele
            BackupUsb.aguardarBackupEmCurso(120000L);
            BackupUsb.realizarBackup();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.exit(0);
    }//GEN-LAST:event_BTnSairActionPerformed

//    public static void alterar_status_botao() {
//
//        if (!caixaDao.existeCaixas()) {
    ////            btn_abertura_dia.setEnabled(true);
//            jButton1.setEnabled(false);
//        } else if (caixaDao.existe_abertura() && caixaDao.existe_fecho()) {
////            btn_abertura_dia.setEnabled(true);
//            jButton1.setEnabled(false);
//        } else {
////            btn_abertura_dia.setEnabled(false);
//            jButton1.setEnabled(true);
//        }
//    }
    
   
public void entrar_sistema() throws SQLException, UnknownHostException {

    // Impede múltiplas execuções simultâneas do login
    if (loginEmExecucao) {
        return;
    }

    loginEmExecucao = true;

    try {

        TbUsuario usuario = usuarioDao.getUsuariowithEncriptedPass(
                getUserName(),
                getSenha()
        );

        if (usuario == null) {
            JOptionPane.showMessageDialog(
                    null,
                    "Erro: senha errada ou User Name inválido."
            );
            return;
        }

        // Verifica se é o primeiro acesso do utilizador
        if (usuario.getDataUltimoAcesso() == null) {
            alterarSenha();
            return;
        }

        TbDadosInstituicao dadosInstituicao =
                (TbDadosInstituicao) dadosInstituicaoController.findById(1);

        id_user = usuario.getCodigo();
        id_empresa = getIdEmpresa();
        // o negócio pode ter mudado nos dados da instituição: o tema acompanha
        if (dadosInstituicao != null) {
            util.ui.Tema.aplicar(dadosInstituicao.getNegocio());
        }

        try {
            // BackupUtil.fazerBackupAgora();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // FE modo contingência: comunica à AGT, em segundo plano, as facturas
        // emitidas sem ligação (estado 'P' sem requestID) - de 5 em 5 minutos
        try {
            util.fe.ComunicacaoPendentesFE.iniciarAgendamento(LoginVisao::notificarComunicacaoFE);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Encaminha o utilizador de acordo com o seu tipo
        switch (usuario.getIdTipoUsuario().getIdTipoUsuario()) {

            // 1 - Administrador
            case 1:

                System.err.println(
                        "getCodico_Utilizador(): " + getCodico_Utilizador()
                );

                System.err.println(
                        "getCodico_Empresa(): " + getIdEmpresa()
                );

                new RootVisao(
                        getCodico_Utilizador(),
                        getIdEmpresa(),
                        true,
                        BDConexao.getInstancia()
                ).setVisible(true);

                break;

            // 2 - Subadministrador
            case 2:

                System.err.println(
                        "getCodico_Utilizador(): " + getCodico_Utilizador()
                );

                System.err.println(
                        "getCodico_Empresa(): " + getIdEmpresa()
                );

                if (dadosInstituicao != null
                        && "Lavandaria".equals(dadosInstituicao.getNegocio())) {

                    new JanelaFrontOfficeLavandariaVisao(
                            getCodico_Utilizador(),
                            BDConexao.getInstancia()
                    ).setVisible(true);

                } else {

                    new RootVisao(
                            getCodico_Utilizador(),
                            getIdEmpresa(),
                            true,
                            BDConexao.getInstancia()
                    ).setVisible(true);
                }

                break;

            // 3 - Caixa
            case 3:

                new VendaUsuarioVisao(
                        getCodico_Utilizador(),
                        BDConexao.getInstancia()
                ).setVisible(true);

                break;

            // 4 - Venda POS
            case 4:

                new FrontOfficeVisao(
                        getCodico_Utilizador(),
                        BDConexao.getInstancia()
                ).setVisible(true);

                break;

            default:

                JOptionPane.showMessageDialog(
                        null,
                        "Tipo de utilizador inválido."
                );

                return;
        }

        limpar();
        dispose();

    } finally {

        // Liberta o login mesmo que ocorra um erro ou um return
        loginEmExecucao = false;
    }

    // Executa o backup depois do login, em segundo plano (não prende o ecrã)
    BackupUsb.realizarBackupEmSegundoPlano();
}


    public int getCodico_Utilizador() {
        return BDConexao.getCodigoUSuario(getUserName(), getSenha());
    }

    public int getIdEmpresa() {
        return empresaDao.getIdByDescricao(cmbEmpresa.getSelectedItem().toString());
    }

    public String getUserName() {
        return txtUserName.getText().toLowerCase();
    }

    public String getSenha() {
        return pswSenha.getText();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        try {

            Locale.setDefault(new Locale("pt", "AO"));
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(LoginVisao.class.getName())
                    .log(java.util.logging.Level.SEVERE, null, ex);
        }

        // 🔒 Verifica instância única ANTES de abrir interface
//        if ( SingleInstanceLock.isAlreadyRunning() )
//        {
//            JOptionPane.showMessageDialog( null, "Atenção\nO sistema já está aberto!" );
//            System.exit( 0 );
//        }
        if (SingleInstanceManager.isInstanceRunning()) {
            // A instância antiga será fechada automaticamente.
            // Depois disto a nova abertura funciona normalmente.
            System.exit(0);
        }


        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {

                    UIManager.setLookAndFeel(new SyntheticaBlackStarLookAndFeel());
//                    UIManager.setLookAndFeel("com.jtattoo.plaf.acryl.AcrylLookAndFeel");
//                    FlatMTMaterialOceanicIJTheme.setup();
                    // JTextField
                    UIManager.put("TextField.background", new Color(0, 255, 255)); // fundo
                    UIManager.put("TextField.foreground", new Color(0, 51, 102));  // texto
                    UIManager.put("TextField.caretForeground", Color.RED);         // cursor
                    UIManager.put("TextField.selectionBackground", Color.YELLOW);  // seleção (amarelo só no JTextField)
                    UIManager.put("TextField.selectionForeground", Color.BLUE);    // texto selecionado

                    //JComboBox
                    UIManager.put("ComboBox.background", new Color(0, 255, 255));
                    UIManager.put("ComboBox.foreground", new Color(0, 51, 102));
                    UIManager.put("ComboBox.selectionBackground", new Color(173, 216, 230)); // azul claro
                    UIManager.put("ComboBox.selectionForeground", Color.BLACK);

                    //JTable
                    UIManager.put("Table.background", new Color(0, 255, 255));
                    UIManager.put("Table.foreground", new Color(0, 51, 102));
                    UIManager.put("Table.selectionBackground", new Color(0, 120, 215)); // azul estilo Windows
                    UIManager.put("Table.selectionForeground", Color.WHITE);      // texto branco na seleção
                    UIManager.put("Table.gridColor", Color.GRAY);
                    UIManager.put("TableHeader.background", new Color(0, 200, 200));
                    UIManager.put("TableHeader.foreground", Color.BLACK);

                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Erro no Look and Feel: " + e.getMessage(),
                            "Erro", JOptionPane.ERROR_MESSAGE);
                }

                // INICIAR PROCESSOS EM BACKGROUND
                if (startBackGroundProcesses()) {
                    // a ligação partilhada primeiro: o monitor de ligação fica nela (e não na do stock)
                    BDConexao.getInstancia();
                    // cores e símbolos do negócio (tb_dados_instituicao.negocio) antes de abrir qualquer janela
                    util.ui.Tema.aplicarDaInstituicao();
                    // todos os relatórios (JasperViewer): maximizados e com o zoom ajustado à largura
                    util.ui.RelatoriosWeb.instalar();
                    // stock diário (registo do dia) em segundo plano, com ligação própria:
                    // o início de sessão aparece logo, sem esperar por ele
                    Thread stockDiario = new Thread(() -> {
                        BDConexao conexaoStock = null;
                        long inicio = System.currentTimeMillis();
                        try {
                            conexaoStock = new BDConexao();
                            new StockAutoCheckService(conexaoStock.getConnectionAtiva()).verificarOuSalvarStockDiario();
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            if (conexaoStock != null) {
                                conexaoStock.close();
                            }
                            System.out.println("Stock diário verificado em segundo plano ("
                                    + (System.currentTimeMillis() - inicio) + " ms)");
                        }
                    }, "kitanda-stock-diario");
                    stockDiario.setPriority(Thread.MIN_PRIORITY);
                    stockDiario.start();

                    if (true) { // <- aqui você pode colocar sua regra de licença
                        new LoginVisao(BDConexao.getInstancia()).setVisible(true);

                    } else {
                        JOptionPane.showMessageDialog(null,
                                "A sua licença expirou.\nPor favor contacte a DVML-COMERCIAL, Lda\nContactos: 923409284 / 940537124",
                                "AVISO", JOptionPane.WARNING_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "NÃO FOI POSSÍVEL ACEDER A BASE DE DADOS",
                            "ERRO NA BASE DE DADOS", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BTnSair;
    private javax.swing.JLabel JEmpresa;
    private javax.swing.JLabel JLDataFecho;
    private javax.swing.JLabel JLNif;
    private javax.swing.JLabel Soft;
    private javax.swing.JLabel Soft1;
    private javax.swing.JComboBox<String> cmbEmpresa;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPasswordField pswSenha;
    private javax.swing.JTextField txtUserName;
    // End of variables declaration//GEN-END:variables

    private void limpar() {
        txtUserName.setText("");
        pswSenha.setText("");

    }

    private void alterarSenha() {
        JOptionPane.showMessageDialog(null, "A senha que está a usar Atualmente não é segura, deve mudar a senha!");
        dispose();
        new UsuarioAlterarSenhaVissao(usuarioDao.getUsuariowithEncriptedPass(txtUserName.getText(), new String(pswSenha.getPassword())));
    }

//    public static void fazerBackupAgora() {
//        String data = new SimpleDateFormat(YYYYMMDD_HHMMSS).format(new Date());
    ////        String rodar_camando = "cmd /c mysqldump -uroot -pDoV90x?# --dump-date --triggers --tables --routines --skip-quote-names --compact --skip-opt --skip-set-charset --hex-blob kitanda_db > \"..\\BD_BACKUP\\_database_backup_" + data + ".sql\"";
//        String rodar_camando = "cmd /c mysqldump --single-transaction -uroot -pDoV90x?# --dump-date --triggers --add-drop-database --routines --skip-quote-names --skip-set-charset --add-locks --disable-keys --databases kitanda_db > \"..\\BD_BACKUP\\_database_backup_" + data + ".sql\"";
////String rodar_camando = "cmd /c mysqldump --single-transaction=TRUE -uroot -pDoV90x?# --dump-date --triggers --add-drop-database  --routines --skip-quote-names --compact --skip-opt --skip-set-charset --hex-blob --add-locks --disable-keys --lock-tables  --databases kitanda_db > \"..\\BD_BACKUP\\_database_backup_" + data + ".sql\"";
//        Process rodarComandoWindows = rodarComandoWindows(rodar_camando, true);
//
////        JOptionPane.showMessageDialog ( null, "Backup realizado com sucesso! ", "Notificação", JOptionPane.INFORMATION_MESSAGE );
//        System.err.println("Backup realizado com sucesso! ");
//
//    }
    private void verificar_data_sistema() {
        Date data_maxima_ultimo_doc = documentoDao.getMAxDataDoc();
        Date data_actual = new Date();

        if (data_maxima_ultimo_doc != null) {
            if (MetodosUtil.menor_data_1_data_2(data_actual, data_maxima_ultimo_doc)) {
                JOptionPane.showMessageDialog(null, "Caro usário verifique a data do sistema", "AVISO", JOptionPane.WARNING_MESSAGE);
            }

        }

    }

//    public static void fazerBackupAgora()
//    {
//        String data = new SimpleDateFormat( "yyyyMMdd_HHmmss" ).format( new Date() );
//        String caminhoBackup = System.getProperty( "user.dir" ) + "\\BK\\_database_backup_" + data + ".sql";
//        String rodar_comando = "cmd /c mysqldump --single-transaction -uroot -pDoV90x?# "
//                + "--dump-date --triggers --add-drop-database --routines "
//                + "--skip-quote-names --skip-set-charset --add-locks --disable-keys "
//                + "--databases kitanda_db > \"" + caminhoBackup + "\"";
//        Process rodarComandoWindows = rodarComandoWindows( caminhoBackup, true );
//        System.err.println( "Backup realizado com sucesso! " );
//    }
//    private void registerLog( BDConexao conexao )
//    {
//        Log log = new Log();
//        String ipMaquina = MetodosUtil.getIpMaquina();
//        String nomeMaquina = MetodosUtil.getNomeMaquina();
//
//        log.setIpMaquina( ipMaquina );
//        log.setNomeMaquina( nomeMaquina );
//        log.setEstado( "ON" );
//
//        LogController logController = new LogController( conexao );
//
//        Log logByIpMaquina = logController.getLogByNomeMaquina( nomeMaquina );
//
//        if ( Objects.nonNull( logByIpMaquina ) )
//        {
//            System.out.println( "MAQUINA JA CADASTRADA" );
//            if ( "ON".equals( logByIpMaquina.getEstado() ) )
//            {
//                int opcao = JOptionPane.showConfirmDialog( null, "Provavelmente o sistema foi encerrado de forma inesperada. Clica em Sim para corrigir." );
//
//                if ( opcao == JOptionPane.YES_NO_OPTION )
//                {
//                    MetodosUtil.actualizarEstadoLog( "OFF" );
//                    LoginVisao.conexao.close();
//                    MetodosUtil.rodarComandoWindows( "taskkill /im javaw.exe -f", false );
//                    System.exit( 0 );
//                }
//                else
//                {
//                    MetodosUtil.rodarComandoWindows( "taskkill /im javaw.exe -f", false );
//                }
//
//            }
//            else
//            {
//                System.out.println( "ENTREI para actualizar o ON" );
//                MetodosUtil.actualizarEstadoLog( "ON" );
//            }
//        }
//        else
//        {
//            if ( !logController.existe( nomeMaquina ) )
//            {
//                logController.salvar( log );
//            }
//
//        }
//
//    }
    private void iniciarMonitoramento() {
        resetTimer();

        // Adiciona um listener global para detectar atividades do usuário
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher((KeyEvent e)
                -> {
            resetTimer();
            return false;
        });

        Toolkit.getDefaultToolkit().addAWTEventListener((AWTEvent event)
                -> {
            if (event.getID() == MouseEvent.MOUSE_MOVED
                    || event.getID() == MouseEvent.MOUSE_CLICKED
                    || event.getID() == MouseEvent.MOUSE_PRESSED) {
                resetTimer();
            }
        }, AWTEvent.MOUSE_MOTION_EVENT_MASK | AWTEvent.MOUSE_EVENT_MASK);
    }

    private void resetTimer() {
        if (logoutTimer != null) {
            logoutTimer.cancel();
        }

        logoutTimer = new Timer();
        logoutTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(()
                        -> {
                    //JOptionPane.showMessageDialog( null, "Sessão expirada! Voltando para o login." );
                    conexao.close();
                    logo_out();
                });
            }
        }, TIMEOUT);
    }

    private void carregarAmbienteFE() {
        String ambiente = null;
        try {
            TbDadosInstituicao dados = dadosInstituicaoDao.findTbDadosInstituicao(1);
            if (dados != null) {
                ambiente = dados.getAmbiente();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        FEConfig.carregarAmbiente(ambiente);
    }

    /**
     * Mostra no topo do login em que ambiente da AGT o sistema arrancou
     * (Homologação ou Produção), antes de o utilizador entrar.
     */
    private void adicionarFaixaAmbiente() {
        boolean hml = FEConfig.isHomologacao();

        JLabel lblAmbiente = new JLabel(
                hml
                        ? "AMBIENTE: HOMOLOGAÇÃO (testes)"
                        : "AMBIENTE: PRODUÇÃO",
                JLabel.CENTER);
        lblAmbiente.setOpaque(true);
        lblAmbiente.setForeground(Color.WHITE);
        lblAmbiente.setBackground(hml ? new Color(190, 0, 0) : new Color(0, 130, 0));
        lblAmbiente.setFont(lblAmbiente.getFont().deriveFont(java.awt.Font.BOLD, 14f));
        lblAmbiente.setBorder(BorderFactory.createEmptyBorder(6, 4, 6, 4));

        // Guarda o conteúdo desenhado no Design e coloca-o por baixo da faixa
        java.awt.Container conteudoOriginal = getContentPane();
        JPanel novoConteudo = new JPanel(new BorderLayout());
        novoConteudo.add(lblAmbiente, BorderLayout.NORTH);
        novoConteudo.add(conteudoOriginal, BorderLayout.CENTER);
        setContentPane(novoConteudo);
    }

    public static void logo_out() {
//        EntityManagerFactory emf = JPAEntityMannagerFactoryUtil.em;
//        UsuarioDao usuarioDao = new UsuarioDao( emf );
//        EmpresaDao empresaDao = new EmpresaDao( emf );
//        TbUsuario usuario = usuarioDao.findTbUsuario( id_user );
//        Empresa empresa = empresaDao.findEmpresa( id_empresa );
        MetodosUtil.fechar_todas_janelas();
        MetodosUtil.actualizarEstadoLog("OFF");
        conexao.close();
        new LoginVisao(BDConexao.getInstancia()).setVisible(true);

//        new LoginVisao(BDConexao.getInstancia().getConnectionAtiva()).setVisible(true);
    }

    /**
     * Aviso discreto (não modal, fecha sozinho) quando o envio automático
     * comunicou facturas emitidas offline. Não interrompe a caixa.
     */
    private static void notificarComunicacaoFE(util.fe.ComunicacaoPendentesFE.Resumo r) {
        StringBuilder sb = new StringBuilder("<html><b>Facturação Electrónica (AGT)</b><br>");
        if (r.enviadas > 0) {
            sb.append(r.enviadas).append(" factura(s) emitida(s) offline comunicada(s).<br>");
        }
        if (r.validadas > 0) {
            sb.append(r.validadas).append(" validada(s).<br>");
        }
        if (r.invalidadas > 0) {
            sb.append("<font color='#b00000'>").append(r.invalidadas)
                    .append(" rejeitada(s) - ver Consulta de Estado FE.</font><br>");
        }
        sb.append("</html>");

        final javax.swing.JWindow janela = new javax.swing.JWindow();
        JLabel lbl = new JLabel(sb.toString());
        lbl.setOpaque(true);
        lbl.setBackground(new java.awt.Color(255, 250, 225));
        lbl.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createLineBorder(new java.awt.Color(200, 160, 60)),
                javax.swing.BorderFactory.createEmptyBorder(10, 14, 10, 14)));
        lbl.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 13));
        janela.getContentPane().add(lbl);
        janela.pack();
        java.awt.Rectangle ecra = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        janela.setLocation(ecra.x + ecra.width - janela.getWidth() - 16,
                ecra.y + ecra.height - janela.getHeight() - 16);
        janela.setFocusableWindowState(false); // não rouba o foco à caixa
        janela.setAlwaysOnTop(true);
        janela.setVisible(true);
        javax.swing.Timer t = new javax.swing.Timer(8000, e -> janela.dispose());
        t.setRepeats(false);
        t.start();
    }

}
