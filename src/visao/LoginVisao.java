/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package visao;

import java.sql.Connection;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

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
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.net.UnknownHostException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import javax.persistence.EntityManagerFactory;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
//import javax.swing.UIManager;
import util.BDConexao;
import util.BackupUsb;
import util.DVML;
import util.AuditoriaJanelas;
import util.JPAEntityMannagerFactoryUtil;
import util.LogActividadeUtil;
import util.SessaoUtil;
//import util.LicensaUtil;
import util.MetodosUtil;
import static util.MetodosUtil.startBackGroundProcesses;
import util.SingleInstanceLock;
import util.SingleInstanceManager;
import util.TemaNegocioUtil;
import util.cronjob.QuartzApp;
import util.cronjob.StockAutoCheckService;
import util.cronjob.ValidadorJob;

/**
 *
 * @author Domingos Dala Vunge
 */
public class LoginVisao extends javax.swing.JFrame {

    // instância (não estático): carregar a classe para correr o main() já não cria o JPA,
    // por isso o ecrã de arranque aparece logo e o EclipseLink prepara-se em paralelo
    private EntityManagerFactory emf = JPAEntityMannagerFactoryUtil.em;
    private static DadosInstituicaoController dadosInstituicaoController;
    private UsuarioDao usuarioDao = new UsuarioDao(emf);
    private EmpresaDao empresaDao = new EmpresaDao(emf);

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

    public LoginVisao(BDConexao conexao) {

        initComponents();
        setLocationRelativeTo(null);
        setResizable(false);
        // aspecto web do SysKitandaHotel (marca Kitanda), sobre o initComponents
        util.ui.LayoutSeguro.aplicar(this, "Início de sessão", this::montarLayoutWeb);
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

// Produtos a expirar: só farmácias e minimercados com bens alimentares
        if (mostrarPainelExpirar(d)) {
            criarPainelExpirar();
        }

//        boolean ehFarmacia = d != null
//                && TemaNegocioUtil.ehFarmacia(d.getNegocio());
//
//        TemaNegocioUtil.aplicarCorCampos(ehFarmacia,
//                txtUserName, pswSenha);

        LoginVisao.setDefaultLookAndFeelDecorated(true);
//        verificar_data_sistema();
        instalarFocoInicial();
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

    /** A versão tem um só sítio: util.DVML.VERSION_SOFTWARE. */
    private String versaoSoftwareWeb() {
        return util.DVML.VERSION_SOFTWARE;
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
        configurarAvisos();
        // fechar a janela segue o mesmo caminho do botão Sair (espera pela cópia de segurança)
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                sairDoSistema();
            }
        });
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
        // produtos a expirar (farmácias e minimercados com bens alimentares): por baixo da empresa
        JPanel meio = util.ui.ComponentesWeb.transparente(new BorderLayout(0, 16));
        meio.add(centro, BorderLayout.CENTER);
        meio.add(zonaExpirar, BorderLayout.SOUTH);
        painel.add(meio, BorderLayout.CENTER);
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
            // branco como no SysKitandaHotel (o ciano do tema geral fica nos outros formulários)
            t.setBackground(Color.WHITE);
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
                lbMensagem,
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


    // =====================================================================
    // AVISOS NO CARTÃO (como no SysKitandaHotel): Caps Lock ligado e erro de
    // acesso por baixo da senha, sem caixas de diálogo; o botão Entrar mostra
    // que está a trabalhar enquanto o menu abre.
    // =====================================================================
    private final JLabel lbMensagem = new JLabel(" ");
    private boolean erroVisivel = false;
    /** Onde fica o painel dos produtos a expirar no layout web. */
    private final JPanel zonaExpirar = util.ui.ComponentesWeb.transparente(new BorderLayout());

    private void configurarAvisos() {
        lbMensagem.setFont(util.ui.EstiloUI.FONTE_NEGRITO.deriveFont(12.5f));
        lbMensagem.setPreferredSize(new java.awt.Dimension(10, 20));
        DocumentListener aoEscrever = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                limparErro();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                limparErro();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        };
        txtUserName.getDocument().addDocumentListener(aoEscrever);
        pswSenha.getDocument().addDocumentListener(aoEscrever);
        pswSenha.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                actualizarAvisoMaiusculas();
            }
        });
        pswSenha.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                actualizarAvisoMaiusculas();
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (!erroVisivel) {
                    lbMensagem.setText(" ");
                }
            }
        });
    }

    private void limparErro() {
        if (erroVisivel) {
            erroVisivel = false;
            lbMensagem.setText(" ");
            actualizarAvisoMaiusculas();
        }
    }

    private void actualizarAvisoMaiusculas() {
        if (erroVisivel) {
            return;
        }
        boolean ligado = false;
        try {
            ligado = pswSenha.isFocusOwner()
                    && Toolkit.getDefaultToolkit().getLockingKeyState(KeyEvent.VK_CAPS_LOCK);
        } catch (UnsupportedOperationException e) {
            // sistema sem esta informação: fica sem aviso
        }
        lbMensagem.setForeground(util.ui.ComponentesWeb.LARANJA.darker());
        lbMensagem.setText(ligado ? "As maiúsculas (Caps Lock) estão ligadas" : " ");
    }

    /** Erro de acesso no próprio cartão (antes era uma caixa de diálogo). */
    private void mostrarErroAcesso(String texto) {
        erroVisivel = false; // os próximos setText limpam com o aviso certo
        pswSenha.setText("");
        lbMensagem.setForeground(util.ui.ComponentesWeb.VERMELHO);
        lbMensagem.setText(texto);
        erroVisivel = true;
        pswSenha.requestFocusInWindow();
    }

    /**
     * O cursor começa sempre a piscar no campo do utilizador. Pedir o foco no
     * construtor não chega: o ecrã de arranque (sempre por cima) ainda está à
     * frente e, quando fecha, o Windows não devolve a activação ao login. Por
     * isso o foco volta a ser pedido quando a janela abre e sempre que fica
     * activa sem nenhum campo escolhido.
     */
    private void instalarFocoInicial() {
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowOpened(java.awt.event.WindowEvent e) {
                focarUtilizador();
            }

            @Override
            public void windowActivated(java.awt.event.WindowEvent e) {
                java.awt.Component dono = getFocusOwner();
                if (dono != txtUserName && dono != pswSenha) {
                    focarCampoCerto();
                }
            }
        });
        focarCampoCerto();
    }

    /** Traz o login para a frente e põe o cursor no utilizador (ou na senha, se o nome já estiver escrito). */
    public void focarUtilizador() {
        javax.swing.SwingUtilities.invokeLater(() -> {
            toFront();
            requestFocus();
            focarCampoCerto();
            // depois de maximizar e arrumar o layout o foco pode ter-se perdido: confirma-se uma vez
            javax.swing.Timer confirmar = new javax.swing.Timer(250, e -> {
                if (isShowing() && getFocusOwner() == null) {
                    toFront();
                    focarCampoCerto();
                }
            });
            confirmar.setRepeats(false);
            confirmar.start();
        });
    }

    private void focarCampoCerto() {
        javax.swing.JComponent alvo = txtUserName.getText().trim().isEmpty() ? txtUserName : pswSenha;
        if (!alvo.requestFocusInWindow()) {
            alvo.requestFocus();
        }
    }

    /** O botão mostra logo que está a trabalhar: abrir o menu ocupa a interface. */
    private void indicarAEntrar(boolean aEntrar) {
        jButton1.setText(aEntrar ? "A entrar..." : "Entrar");
        jButton1.setEnabled(!aEntrar);
        BTnSair.setEnabled(!aEntrar);
        setCursor(java.awt.Cursor.getPredefinedCursor(aEntrar ? java.awt.Cursor.WAIT_CURSOR : java.awt.Cursor.DEFAULT_CURSOR));
        if (aEntrar) {
            jButton1.paintImmediately(jButton1.getVisibleRect());
            BTnSair.paintImmediately(BTnSair.getVisibleRect());
        }
    }

    /** Valida o ano económico e entra, com o botão a indicar que está a trabalhar. */
    private void tentarEntrar() {
        indicarAEntrar(true);
        try {
            if (MetodosUtil.acesso_sistema()) {
                entrar_sistema();
            } else {
                JOptionPane.showMessageDialog(null, "Impossível aceder ao sistema.\nDeves abrir um novo ano económico.\nContacte o seu fornecedor.", "AVISO", JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (isDisplayable()) {
                indicarAEntrar(false);
            }
        }
    }

    /**
     * Sair: a cópia de segurança corre em segundo plano e a aplicação espera que
     * termine (com aviso), em vez de a interface ficar parada no mysqldump.
     */
    private void sairDoSistema() {
        setVisible(false);
        BackupUsb.iniciarEmSegundoPlano();
        BackupUsb.sairDaAplicacao();
    }

    /**
     * Produtos a expirar: só nas farmácias e nos minimercados que vendem bens
     * alimentares. Um minimercado está registado como "Comercial": o painel só
     * aparece se houver lotes com validade e stock (produtos perecíveis).
     */
    private static boolean mostrarPainelExpirar(TbDadosInstituicao dados) {
        if (dados == null || dados.getNegocio() == null) {
            return false;
        }
        String negocio = dados.getNegocio().trim();
        if (TemaNegocioUtil.ehFarmacia(negocio)) {
            return true;
        }
        String n = java.text.Normalizer.normalize(negocio.toLowerCase(), java.text.Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "").replaceAll("[^a-z]", "");
        if (!n.equals("comercial") && !n.contains("mercado")) {
            return false;
        }
        try (PreparedStatement ps = BDConexao.getInstancia().getConnectionAtiva().prepareStatement(
                "SELECT 1 FROM lote WHERE qtd > 0 AND data_validade IS NOT NULL AND estado IN ('ACTIVO','EXPIRADO') LIMIT 1");
                ResultSet rs = ps.executeQuery()) {
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public LoginVisao(int idUser) {

        initComponents();
        setLocationRelativeTo(null);
        setResizable(false);
        // aspecto web do SysKitandaHotel (marca Kitanda), sobre o initComponents
        util.ui.LayoutSeguro.aplicar(this, "Início de sessão", this::montarLayoutWeb);
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

// Produtos a expirar: só farmácias e minimercados com bens alimentares
        if (mostrarPainelExpirar(d)) {
            criarPainelExpirar();
        }

        LoginVisao.setDefaultLookAndFeelDecorated(true);
//        prazo();

//        criarPainelExpirar();   // <-- nova linha
//        LoginVisao.setDefaultLookAndFeelDecorated( true );
        try {
            System.out.println("Data Ultimo: " + documentoDao.getMAxDataDoc().toString());
        } catch (Exception e) {
        }
//        verificar_data_sistema();
        instalarFocoInicial();
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
        Soft.setText("KITANDA 1.1.   Sistema Certificado pela AGT. Nº 258");

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
                            .addComponent(Soft, javax.swing.GroupLayout.PREFERRED_SIZE, 407, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(517, 517, 517)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(Soft1, javax.swing.GroupLayout.PREFERRED_SIZE, 365, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(201, 201, 201)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(160, Short.MAX_VALUE))
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
                .addGap(165, 165, 165)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 836, Short.MAX_VALUE)
                    .addContainerGap()))
        );

        Soft.getAccessibleContext().setAccessibleName("KITANDA 1.1.   Sistema Certificado pela AGT. Nº 258");
        Soft1.getAccessibleContext().setAccessibleName("KITANDA 1.1");

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
        tentarEntrar();
    }//GEN-LAST:event_pswSenhaActionPerformed

    private void cmbEmpresaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbEmpresaActionPerformed
//        System.out.println("ID EMPRESA:"+getIdEmpresa());
    }//GEN-LAST:event_cmbEmpresaActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        tentarEntrar();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void BTnSairActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_BTnSairActionPerformed
    {//GEN-HEADEREND:event_BTnSairActionPerformed
        sairDoSistema();
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

        TbUsuario usuario = usuarioDao.getUsuariowithEncriptedPass(getUserName(), getSenha());
        TbDadosInstituicao dadosInstituicao = (TbDadosInstituicao) dadosInstituicaoController.findById(1);

        if (usuario == null) {
            LogActividadeUtil.registar(null, getUserName(), LogActividadeUtil.MODULO_SISTEMA, LogActividadeUtil.LOGIN_FALHADO,
                    "tb_usuario", null, "Tentativa de login falhada com o user name: " + getUserName(), null, null);
            mostrarErroAcesso("Utilizador ou senha incorrectos.");
        } else {
            if (usuario.getDataUltimoAcesso() == null) {
                alterarSenha();
            } else {

                id_user = usuario.getCodigo();
                id_empresa = getIdEmpresa();
                if (SessaoUtil.getCodUsuario() != null) {
                    // troca de utilizador sem fechar a aplicação
                    LogActividadeUtil.registar(LogActividadeUtil.MODULO_SISTEMA, LogActividadeUtil.LOGOUT,
                            "tb_usuario", SessaoUtil.getCodUsuario(), "Terminou a sessão (troca de utilizador)");
                }
                SessaoUtil.iniciar(usuario);
                LogActividadeUtil.registar(LogActividadeUtil.MODULO_SISTEMA, LogActividadeUtil.LOGIN,
                        "tb_usuario", usuario.getCodigo(), "Entrou no sistema");
                AuditoriaJanelas.instalar();
                try {
//                    BackupUtil.fazerBackupAgora();
                } catch (Exception e) {
                    e.printStackTrace();
                }

//                dispose();
                switch (usuario.getIdTipoUsuario().getIdTipoUsuario()) {

                    //1 - Administrador
                    case 1:
//                        dispose();

                        System.err.println("getCodico_Utilizador (): " + getCodico_Utilizador());
                        System.err.println("getCodico_Empresa (): " + getIdEmpresa());
//                        MetodosUtil.abrir_caixa_automatica( getCodico_Utilizador(), new UsuariosController( conexao ), new CaixasController( conexao ) );
                        new RootVisao(getCodico_Utilizador(), getIdEmpresa(), true, BDConexao.getInstancia()).setVisible(true);
                        limpar();
                        break;
//                    Sub Administrador
                    case 2:
//                        dispose();

                        System.err.println("getCodico_Utilizador (): " + getCodico_Utilizador());
                        System.err.println("getCodico_Empresa (): " + getIdEmpresa());
//                        MetodosUtil.abrir_caixa_automatica( getCodico_Utilizador(), new UsuariosController( conexao ), new CaixasController( conexao ) );
                        if (dadosInstituicao.getNegocio().equals("Lavandaria")) {

                            new JanelaFrontOfficeLavandariaVisao(getCodico_Utilizador(), BDConexao.getInstancia()).setVisible(true);

                        } else {

                            new RootVisao(getCodico_Utilizador(), getIdEmpresa(), true, BDConexao.getInstancia()).setVisible(true);
                        }
                        limpar();
                        break;
                    //2 - Caixa
                    case 3:
//                        dispose();
//                        MetodosUtil.abrir_caixa_automatica( getCodico_Utilizador(), new UsuariosController( conexao ), new CaixasController( conexao ) );
                        if (comercial.mensalidade.Mensalidades.activo()) {
                            // ginásio / centro infantil / prestação de serviço: a janela de venda configurada,
                            // a única que cobra mensalidades e multas (meses, multa automática, registo)
                            util.TelaVendaUtil.abrir(getCodico_Utilizador());
                        } else {
                            new VendaUsuarioVisao(getCodico_Utilizador(), BDConexao.getInstancia()).setVisible(true);
                        }
                        limpar();
                        break;
                    //3 - VendaPO
                    case 4:
//                        dispose();
//                        new VendaPOSVisao( BDConexao.getInstancia(), DVML.ARMAZEM_LOJA, getCodico_Utilizador() ).setVisible( true );
//                        MetodosUtil.abrir_caixa_automatica( getCodico_Utilizador(), new UsuariosController( conexao ), new CaixasController( conexao ) );
                        new FrontOfficeVisao(getCodico_Utilizador(), BDConexao.getInstancia()).setVisible(true);
                        limpar();
                        break;

                    default:
                        JOptionPane.showMessageDialog(null, "Erro Senha errado ou  User Name");
                        break;
                }
            }
        }
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

        // o JPA e a base de dados preparam-se já, numa thread própria
        final Thread arranque = prepararArranqueEmSegundoPlano();

        // marca Kitanda em todas as janelas, botões Sair iguais e uma só entrada na
        // barra de tarefas (os formulários ficam "filhos" do menu), como no SysKitandaHotel
        util.ui.MenuWeb.instalarIconeAplicacao();
        util.ui.BotoesSair.instalar();
        util.ui.JanelasFilhas.instalar();
        util.ui.RelatoriosWeb.instalar();


        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                // marca Kitanda logo ao abrir (antes ficavam 5 a 6 segundos sem nada no ecrã)
                util.ui.EcraArranque.mostrar(null); // sem o DVML: carregá-lo prepara o JPA e o ecrã ficaria à espera
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

                // a base de dados, o tema e os ícones já vêm da thread do arranque
                abrirLoginQuandoPronto(arranque);
            }
        });
    }

    /**
     * Prepara o que o início de sessão precisa, fora da interface: o JPA
     * (EclipseLink: 3 a 4 segundos na primeira vez), a ligação JDBC, as cores do
     * negócio e os ícones. Corre em paralelo com o look and feel e o ecrã de arranque.
     */
    private static Thread prepararArranqueEmSegundoPlano() {
        Thread t = new Thread(() -> {
            long inicio = System.currentTimeMillis();
            try {
                util.ui.EcraArranque.estado("A ligar à base de dados…");
                BDConexao.getInstancia().getConnectionAtiva();
                // cores e símbolo do negócio (leitura JDBC, leve)
                util.ui.Tema.aplicarDaInstituicao();
                util.ui.EcraArranque.estado("A preparar o sistema…");
                javax.persistence.EntityManager em = JPAEntityMannagerFactoryUtil.createEntityManager();
                if (em != null) {
                    try {
                        em.find(TbDadosInstituicao.class, 1); // o EclipseLink prepara as entidades aqui
                    } finally {
                        em.close();
                    }
                }
                util.ui.MenuWeb.iconesAplicacao(); // emblema Kitanda já reduzido para todos os tamanhos
                // a imagem de fundo do Login (2,2 MB) descodificada já: o Toolkit guarda-a e o Login abre sem esperar
                new javax.swing.ImageIcon(LoginVisao.class.getResource("/imagens/img30.png")).getImage();
            } catch (Throwable e) {
                e.printStackTrace(); // o Login trata a falha da base de dados como antes
            }
            System.out.println("[Arranque] base de dados e JPA prontos em " + (System.currentTimeMillis() - inicio) + " ms");
        }, "kitanda-arranque");
        t.setDaemon(true);
        t.start();
        return t;
    }

    /** Espera pela preparação fora da interface e abre o início de sessão. */
    private static void abrirLoginQuandoPronto(final Thread arranque) {
        new javax.swing.SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                arranque.join();
                return null;
            }

            @Override
            protected void done() {
                try {
                    if (true) { // <- aqui você pode colocar sua regra de licença
                        login = new LoginVisao(BDConexao.getInstancia());
                        login.setVisible(true);
                    } else {
                        JOptionPane.showMessageDialog(null,
                                "A sua licença expirou.\nPor favor contacte a DVML-COMERCIAL, Lda\nContactos: 923409284 / 940537124",
                                "AVISO", JOptionPane.WARNING_MESSAGE);
                    }
                } finally {
                    util.ui.EcraArranque.fechar();
                    // o ecrã de arranque estava por cima: agora o login fica activo, com o cursor no utilizador
                    if (login != null) {
                        login.focarUtilizador();
                    }
                }
            }

            private LoginVisao login;
        }.execute();
    }

    // =====================================================================
    // SUBSTITUI, na classe LoginVisao, TODO o bloco do painel "Produtos a expirar"
    // (desde "private static final int EXP_X..." até ao fim da classe RendererExpirar).
    // Os imports e as chamadas criarPainelExpirar() nos construtores ficam como estão.
    //
    // Mostra apenas os lotes do SEGUNDO armazém (Farmácia). Se a empresa só tiver
    // um armazém, usa o primeiro. O nome do armazém aparece no título do painel.
    // =====================================================================
    // ------------------------------------------------------------------
    // Painel: produtos a expirar (visível antes do login)
    // ------------------------------------------------------------------
    // Posição e tamanho do painel (ajusta aqui se quiseres mexer na estética)
    // ------------------------------------------------------------------
// Relatório iReport - Produtos a expirar no Login
// ------------------------------------------------------------------
    private static final String RELATORIO_LOGIN_JASPER
            = "produtos_por_expirar_login.jasper";

    private static final String RELATORIO_LOGIN_JRXML
            = "produtos_por_expirar_login.jrxml";

    private JasperReport relatorioLoginCarregado;

    private final SimpleDateFormat fmtDataHoraRelatorio
            = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    private static final int EXP_X = 33, EXP_Y = 250, EXP_W = 540, EXP_H = 360;
    private static final int DIAS_INICIAIS = 60;

    // Índice do armazém mostrado, na lista ordenada por código: 1 = SEGUNDO (Farmácia)
    private static final int ARMAZEM_EXPIRAR_INDICE = 1;

    private static final Color COR_EXPIRADO = new Color(255, 170, 170); // vermelho claro
    private static final Color COR_URGENTE = new Color(255, 204, 153);  // laranja claro (<= 30 dias)
    private static final Color COR_URGENTE_FORTE = new Color(255, 120, 0); // laranja forte (fase "acesa" do piscar)

    // Alarme: toca ALARME_TOQUES vezes quando passam a existir linhas a piscar
    private static final int ALARME_TOQUES = 3;
    private static final int ALARME_FREQUENCIA_HZ = 880; // nota Lá
    private static final int ALARME_DURACAO_MS = 250;    // duração de cada toque
    private static final int ALARME_PAUSA_MS = 150;      // pausa entre toques
    private static final Color COR_AVISO = new Color(255, 245, 170);    // amarelo claro (> 30 dias)

    // ASSUMIDO: tb_armazem(codigo, designacao) e lote.fk_armazem
    private static final String SQL_ARMAZENS_EXPIRAR
            = "SELECT codigo, designacao FROM tb_armazem ORDER BY codigo ASC";

    private static final String SQL_A_EXPIRAR
            = "SELECT p.codigo, p.designacao, l.numero_lote, l.data_validade, "
            + "       DATEDIFF( l.data_validade, CURDATE() ) AS dias_restantes "
            + "FROM lote l "
            + "INNER JOIN tb_produto p ON p.codigo = l.fk_produto "
            + "WHERE l.qtd > 0 AND l.estado IN ('ACTIVO','EXPIRADO') "
            + "  AND l.fk_armazem = ? "
            + "  AND l.data_validade <= DATE_ADD( CURDATE(), INTERVAL ? DAY ) "
            + "  AND ( ? = 1 OR l.data_validade >= CURDATE() ) "
            + "ORDER BY l.data_validade ASC, p.designacao ASC, l.id ASC";

    private int idArmazemExpirar = -1;
    private String nomeArmazemExpirar = "";

    private final JSpinner spDiasExpirar = new JSpinner(new SpinnerNumberModel(DIAS_INICIAIS, 1, 3650, 1));
    private final JCheckBox chkExpirados = new JCheckBox("Incluir já expirados com stock", true);
    private final DefaultTableModel modExpirar = new DefaultTableModel(
            new String[]{
                "Código", "Designação Produto", "Lote", "Data Validade"
            }, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable tblExpirar = new JTable(modExpirar);
    private final javax.swing.JLabel lblTotalExpirar = new javax.swing.JLabel(" ");
    private final List<Integer> diasExpirar = new ArrayList<>(); // dias restantes de cada linha (para a cor)
    private final SimpleDateFormat fmtDataExpirar = new SimpleDateFormat("dd/MM/yyyy");
    private javax.swing.Timer refreshExpirarTimer;

    // Piscar das linhas laranja (a expirar em 0 a 30 dias)
    private javax.swing.Timer piscarExpirarTimer;
    private boolean piscarExpirarLigado = false;
    private boolean temLinhasUrgentes = false;
    private volatile boolean alarmeATocar = false;

    /**
     * Descobre o armazém a mostrar: o 2.º da lista (ou o 1.º, se só houver um).
     */
    private void resolverArmazemExpirar() {
        try {
            Connection cn = BDConexao.getInstancia().getConnectionAtiva();
            try (PreparedStatement ps = cn.prepareStatement(SQL_ARMAZENS_EXPIRAR); ResultSet rs = ps.executeQuery()) {
                int i = 0;
                while (rs.next()) {
                    if (i == 0 || i == ARMAZEM_EXPIRAR_INDICE) {
                        idArmazemExpirar = rs.getInt("codigo");
                        nomeArmazemExpirar = rs.getString("designacao");
                    }
                    if (i == ARMAZEM_EXPIRAR_INDICE) {
                        break;
                    }
                    i++;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            idArmazemExpirar = -1;
        }
    }

    /**
     * Cria o painel e coloca-o por cima do fundo, sem tocar no layout gerado.
     */
    private void criarPainelExpirar() {
        resolverArmazemExpirar();

        Font negrito = new Font("Tahoma", Font.BOLD, 12);

        javax.swing.JLabel lbl1 = new javax.swing.JLabel("A expirar nos próximos");
        javax.swing.JLabel lbl2 = new javax.swing.JLabel("dias");
        for (javax.swing.JLabel l : new javax.swing.JLabel[]{
            lbl1, lbl2
        }) {
            l.setFont(negrito);
            l.setForeground(Color.WHITE);
        }
        spDiasExpirar.setPreferredSize(new Dimension(75, 26));
        chkExpirados.setOpaque(false);
        chkExpirados.setForeground(Color.WHITE);
        chkExpirados.setFont(negrito);

        JPanel topo = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 2));
        topo.setOpaque(false);
        topo.add(lbl1);
        topo.add(spDiasExpirar);
        topo.add(lbl2);
        topo.add(chkExpirados);

        // Garantir 5 colunas no modelo:
// 0 = Código
// 1 = Designação Produto
// 2 = Lote
// 3 = Data Validade
// 4 = Dias restantes (coluna auxiliar para o relatório)
        javax.swing.table.DefaultTableModel modeloExpirar
                = (javax.swing.table.DefaultTableModel) tblExpirar.getModel();

        if (modeloExpirar.getColumnCount() == 4) {
            modeloExpirar.addColumn("Dias restantes");
        }

        // Tabela
        tblExpirar.setRowHeight(24);
        tblExpirar.getTableHeader().setReorderingAllowed(false);
        tblExpirar.getTableHeader().setFont(negrito);
        tblExpirar.setFillsViewportHeight(true);
        tblExpirar.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        int[] alinhamentos
                = {
                    SwingConstants.CENTER, SwingConstants.LEFT, SwingConstants.LEFT, SwingConstants.CENTER
                };
        int[] larguras
                = {
                    70, 260, 100, 100
                };
        for (int c = 0; c < alinhamentos.length; c++) {
            tblExpirar.getColumnModel().getColumn(c).setCellRenderer(new RendererExpirar(alinhamentos[c]));
            tblExpirar.getColumnModel().getColumn(c).setPreferredWidth(larguras[c]);
        }
        // Ocultar a coluna auxiliar, mantendo-a no modelo de dados.
        if (tblExpirar.getColumnModel().getColumnCount() > 4) {
            tblExpirar.removeColumn(
                    tblExpirar.getColumnModel().getColumn(4)
            );
        }
        JScrollPane scroll = new JScrollPane(tblExpirar);
        scroll.getViewport().setBackground(new Color(20, 25, 50));

        lblTotalExpirar.setForeground(Color.WHITE);
        lblTotalExpirar.setFont(new Font("Tahoma", Font.ITALIC, 11));

        // Painel com fundo escuro semi-transparente (deixa ver a imagem de fundo)
        JPanel painel = new JPanel(new BorderLayout(0, 6)) {
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(getBackground());
                g.fillRect(0, 0, getWidth(), getHeight());
                super.paintComponent(g);
            }
        };
        painel.setOpaque(false);
        painel.setBackground(new Color(10, 15, 40, 190));
        String titulo = nomeArmazemExpirar.isEmpty()
                ? "Produtos a expirar"
                : "Produtos a expirar - " + nomeArmazemExpirar;
        painel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.WHITE),
                        titulo, javax.swing.border.TitledBorder.LEFT,
                        javax.swing.border.TitledBorder.TOP, new Font("Tahoma", Font.BOLD, 14), Color.WHITE),
                BorderFactory.createEmptyBorder(4, 6, 6, 6)));
//        painel.add( topo, BorderLayout.NORTH );
//        painel.add( scroll, BorderLayout.CENTER );
//        painel.add( lblTotalExpirar, BorderLayout.SOUTH );
//        painel.setBounds( EXP_X, EXP_Y, EXP_W, EXP_H );
// Rodapé com total de lotes e botão de impressão
        JPanel rodapeExpirar = new JPanel(new BorderLayout(5, 0));
        rodapeExpirar.setOpaque(false);

        JButton btnImprimirExpirar = new JButton("Imprimir");
        btnImprimirExpirar.setToolTipText(
                "Imprimir os produtos a expirar apresentados na tabela"
        );

        btnImprimirExpirar.addActionListener(e -> imprimirProdutosAExpirar());

        rodapeExpirar.add(lblTotalExpirar, BorderLayout.CENTER);
        rodapeExpirar.add(btnImprimirExpirar, BorderLayout.EAST);

        painel.add(topo, BorderLayout.NORTH);
        painel.add(scroll, BorderLayout.CENTER);
        painel.add(rodapeExpirar, BorderLayout.SOUTH);

        painel.setBounds(EXP_X, EXP_Y, EXP_W, EXP_H);

        if (zonaExpirar.getParent() != null) {
            // layout web: por baixo dos dados da empresa, sem tapar nada
            painel.setPreferredSize(new Dimension(EXP_W, EXP_H));
            zonaExpirar.add(painel, BorderLayout.WEST);
            zonaExpirar.revalidate();
        } else {
            getLayeredPane().add(painel, JLayeredPane.PALETTE_LAYER);
        }

        // Actualização dinâmica: setas do spinner, escrita manual e checkbox
        spDiasExpirar.addChangeListener(e -> agendarRefreshExpirar());
        JFormattedTextField tf = ((JSpinner.DefaultEditor) spDiasExpirar.getEditor()).getTextField();
        tf.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                agendarRefreshExpirar();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                agendarRefreshExpirar();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                agendarRefreshExpirar();
            }
        });
        chkExpirados.addActionListener(e -> carregarProdutosAExpirar());

        carregarProdutosAExpirar();
        iniciarPiscarExpirar();
    }

    /**
     * Faz piscar as linhas laranja (500 ms), para chamar a atenção do
     * utilizador.
     */
    private void iniciarPiscarExpirar() {
        piscarExpirarTimer = new javax.swing.Timer(500, e
                -> {
            if (!isDisplayable()) {
                ((javax.swing.Timer) e.getSource()).stop(); // janela fechada: pára o timer
                return;
            }
            piscarExpirarLigado = !piscarExpirarLigado;
            if (temLinhasUrgentes) {
                tblExpirar.repaint();
            }
        });
        piscarExpirarTimer.start();
    }

    /**
     * Espera 400 ms depois da última tecla antes de consultar a BD.
     */
    private void agendarRefreshExpirar() {
        if (refreshExpirarTimer == null) {
            refreshExpirarTimer = new javax.swing.Timer(400, e -> carregarProdutosAExpirar());
            refreshExpirarTimer.setRepeats(false);
        }
        refreshExpirarTimer.restart();
    }

    /**
     * Lê o nº de dias directamente do texto do spinner (mesmo antes de carregar
     * Enter).
     */
    private int obterDiasExpirar() {
        String txt = ((JSpinner.DefaultEditor) spDiasExpirar.getEditor()).getTextField()
                .getText().replaceAll("[^0-9]", "");
        if (txt.isEmpty()) {
            return -1;
        }
        try {
            int d = Integer.parseInt(txt);
            return (d >= 1 && d <= 3650) ? d : -1;
        } catch (NumberFormatException ex) {
            return -1;
        }
    }

    private void carregarProdutosAExpirar() {
        int dias = obterDiasExpirar();
        if (dias < 0) {
            return; // campo vazio ou valor inválido: mantém a lista anterior
        }

        if (idArmazemExpirar < 0) {
            resolverArmazemExpirar(); // tenta outra vez (ex.: BD indisponível no arranque)
        }

        modExpirar.setRowCount(0);
        boolean tinhaUrgentes = temLinhasUrgentes;
        diasExpirar.clear();
        temLinhasUrgentes = false;

        if (idArmazemExpirar < 0) {
            lblTotalExpirar.setText(" Não foi possível identificar o armazém.");
            return;
        }

        try {
            Connection cn = BDConexao.getInstancia().getConnectionAtiva();
            try (PreparedStatement ps = cn.prepareStatement(SQL_A_EXPIRAR)) {
                ps.setInt(1, idArmazemExpirar);
                ps.setInt(2, dias);
                ps.setInt(3, chkExpirados.isSelected() ? 1 : 0);
                try (ResultSet rs = ps.executeQuery()) {

                    while (rs.next()) {
                        int diasRestantes = rs.getInt("dias_restantes");

                        diasExpirar.add(diasRestantes);

                        if (diasRestantes >= 0 && diasRestantes <= 30) {
                            temLinhasUrgentes = true;
                        }

                        modExpirar.addRow(new Object[]{
                            rs.getInt("codigo"),
                            rs.getString("designacao"),
                            rs.getString("numero_lote"),
                            rs.getDate("data_validade") == null
                            ? ""
                            : fmtDataExpirar.format(rs.getDate("data_validade")),
                            diasRestantes // Quinta coluna: utilizada pelo iReport
                        });
                    }

                }
            }
            // Alarme: só quando as linhas a piscar APARECEM (não repete a cada actualização)
            if (temLinhasUrgentes && !tinhaUrgentes) {
                tocarAlarmeExpirar();
            }
            lblTotalExpirar.setText(" " + modExpirar.getRowCount() + " lote(s)   |   Vermelho: expirado   "
                    + "Laranja: até 30 dias   Amarelo: mais de 30 dias");
        } catch (Exception ex) {
            // Nunca bloquear o login por causa desta lista
            ex.printStackTrace();
            lblTotalExpirar.setText(" Não foi possível carregar a lista: " + ex.getMessage());
        }
    }

    /**
     * Toca ALARME_TOQUES toques, numa thread própria (não bloqueia o ecrã de
     * login).
     */
    private void tocarAlarmeExpirar() {
        if (alarmeATocar) {
            return; // já está a tocar
        }
        alarmeATocar = true;

        Thread t = new Thread(()
                -> {
            try {
                for (int i = 0; i < ALARME_TOQUES; i++) {
                    tocarTomExpirar(ALARME_FREQUENCIA_HZ, ALARME_DURACAO_MS);
                    Thread.sleep(ALARME_PAUSA_MS);
                }
            } catch (Exception ex) {
                // sem placa de som / linha de áudio indisponível: usa o beep do sistema
                try {
                    for (int i = 0; i < ALARME_TOQUES; i++) {
                        Toolkit.getDefaultToolkit().beep();
                        Thread.sleep(ALARME_DURACAO_MS + ALARME_PAUSA_MS);
                    }
                } catch (Exception ignorar) {
                }
            } finally {
                alarmeATocar = false;
            }
        }, "alarme-expirar");
        t.setDaemon(true);
        t.start();
    }

    /**
     * Gera e toca um tom (onda sinusoidal) com fade curto para não estalar.
     */
    private static void tocarTomExpirar(int frequenciaHz, int duracaoMs) throws Exception {
        float taxa = 44100f;
        javax.sound.sampled.AudioFormat formato = new javax.sound.sampled.AudioFormat(taxa, 16, 1, true, false);
        try (javax.sound.sampled.SourceDataLine linha = javax.sound.sampled.AudioSystem.getSourceDataLine(formato)) {
            linha.open(formato);
            linha.start();

            int amostras = (int) (taxa * duracaoMs / 1000);
            byte[] buf = new byte[amostras * 2];
            for (int i = 0; i < amostras; i++) {
                double fade = Math.min(1.0, Math.min(i, amostras - i) / (taxa * 0.01)); // 10 ms
                short v = (short) (Math.sin(2 * Math.PI * frequenciaHz * i / taxa) * 0.6 * Short.MAX_VALUE * fade);
                buf[2 * i] = (byte) (v & 0xff);
                buf[2 * i + 1] = (byte) ((v >> 8) & 0xff);
            }
            linha.write(buf, 0, buf.length);
            linha.drain();
        }
    }

    /**
     * Pinta a linha conforme os dias restantes.
     */
    private class RendererExpirar extends DefaultTableCellRenderer {

        RendererExpirar(int alinhamento) {
            setHorizontalAlignment(alinhamento);
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (!isSelected) {
                int dias = row < diasExpirar.size() ? diasExpirar.get(row) : 0;
                Color fundo;
                if (dias < 0) {
                    fundo = COR_EXPIRADO;
                } else if (dias <= 30) {
                    fundo = piscarExpirarLigado ? COR_URGENTE_FORTE : COR_URGENTE; // pisca
                } else {
                    fundo = COR_AVISO;
                }
                c.setBackground(fundo);
                c.setForeground(Color.BLACK);
            }
            return c;
        }
    }

    /**
     * Imprime os dados actualmente apresentados no painel de produtos a
     * expirar.
     */
    private void imprimirProdutosAExpirar() {

        // Garante que a consulta corresponde aos filtros actuais.
        if (refreshExpirarTimer != null && refreshExpirarTimer.isRunning()) {
            refreshExpirarTimer.stop();
            carregarProdutosAExpirar();
        }

        if (modExpirar.getRowCount() == 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "Não há produtos a expirar para imprimir com os filtros actuais.",
                    "Imprimir produtos a expirar",
                    JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }

        // Copia os dados para o relatório, incluindo a coluna auxiliar
        // "Dias restantes", necessária para o campo COLUMN_4 do iReport.
        DefaultTableModel copia = new DefaultTableModel(
                new String[]{
                    "Código",
                    "Designação Produto",
                    "Lote",
                    "Data Validade",
                    "Dias restantes"
                }, 0
        );

        for (int i = 0; i < modExpirar.getRowCount(); i++) {
            Object[] linha = new Object[5];

            for (int c = 0; c < 5; c++) {
                linha[c] = modExpirar.getValueAt(i, c);
            }

            copia.addRow(linha);
        }

        // Parâmetros enviados ao iReport.
        Map<String, Object> parametros = new HashMap<>();

        parametros.put(
                "DATA_EMISSAO",
                fmtDataHoraRelatorio.format(new Date())
        );

        parametros.put(
                "DIAS_ALERTA",
                Integer.valueOf(30)
        );

        parametros.put(
                "DIAS_CONSULTA",
                Integer.valueOf(obterDiasExpirar())
        );

        parametros.put(
                "ARMAZEM",
                nomeArmazemExpirar == null || nomeArmazemExpirar.trim().isEmpty()
                ? "Não identificado"
                : nomeArmazemExpirar
        );

        parametros.put(
                "INCLUIR_EXPIRADOS",
                chkExpirados.isSelected() ? "Sim" : "Não"
        );

        try {
            JasperPrint impressao = JasperFillManager.fillReport(
                    carregarRelatorioLogin(),
                    parametros,
                    new JRTableModelDataSource(copia)
            );

            JasperViewer visualizador = new JasperViewer(impressao, false);

            visualizador.setTitle("RELATÓRIO DE PRODUTOS A EXPIRAR - LOGIN");
            visualizador.setLocationRelativeTo(this);
            visualizador.setVisible(true);

        } catch (Exception ex) {
            ex.printStackTrace();

            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao gerar o relatório:\n" + ex.getMessage(),
                    "Erro de impressão",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    /**
     * Carrega o relatório produtos_por_expirar_login. Utiliza o .jasper
     * existente ou compila o .jrxml.
     */
    private JasperReport carregarRelatorioLogin() throws JRException {

        if (relatorioLoginCarregado != null) {
            return relatorioLoginCarregado;
        }

        File jasper = new File(
                DVML.CAMINHO_REPORT + RELATORIO_LOGIN_JASPER
        ).getAbsoluteFile();

        if (jasper.exists()) {
            relatorioLoginCarregado
                    = (JasperReport) JRLoader.loadObject(jasper);

            return relatorioLoginCarregado;
        }

        File jrxml = new File(
                DVML.CAMINHO_REPORT + RELATORIO_LOGIN_JRXML
        ).getAbsoluteFile();

        if (jrxml.exists()) {
            relatorioLoginCarregado
                    = JasperCompileManager.compileReport(
                            jrxml.getAbsolutePath()
                    );

            return relatorioLoginCarregado;
        }

        throw new JRException(
                "Relatório não encontrado.\n"
                + "Coloque o ficheiro "
                + RELATORIO_LOGIN_JRXML
                + " ou "
                + RELATORIO_LOGIN_JASPER
                + " na pasta:\n"
                + jasper.getParent()
        );
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

    private void verificar_data_sistema() {
        Date data_maxima_ultimo_doc = documentoDao.getMAxDataDoc();
        Date data_actual = new Date();

        if (data_maxima_ultimo_doc != null) {
            if (MetodosUtil.menor_data_1_data_2(data_actual, data_maxima_ultimo_doc)) {
                JOptionPane.showMessageDialog(null, "Caro usário verifique a data do sistema", "AVISO", JOptionPane.WARNING_MESSAGE);
            }

        }

    }

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
    
    private void aplicarTemaLogin() {
    try {
        BDConexao con = BDConexao.getInstancia();
        con.getConnectionAtiva();
        DadosInstituicaoController ctrl = new DadosInstituicaoController(con);
        TbDadosInstituicao dados = (TbDadosInstituicao) ctrl.findById(1);

        boolean ehFarmacia = dados != null
                && TemaNegocioUtil.ehFarmacia(dados.getNegocio());

        TemaNegocioUtil.aplicarCorCampos(ehFarmacia, txtUserName, pswSenha);
    } catch (Exception ex) {
        ex.printStackTrace();
    }
}

}
