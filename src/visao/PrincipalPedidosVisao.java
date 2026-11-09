/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package visao;

import comercial.controller.PedidosController;
import comercial.controller.ItemPedidosController;
import java.sql.Connection;
import dao.AccessoArmazemDao;
import dao.ArmazemDao;
import dao.ItemPedidosDao;
import dao.PedidoDao;
import dao.TurnoDao;
import dao.UsuarioDao;
import entity.TbPedido;
import dao.CaixaDao;
import dao.DadosInstituicaoDao;
import entity.TbArmazem;
import entity.Turno;
import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import javax.persistence.EntityManagerFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import static kitanda.util.CfConstantes.YYYYMMDD_HHMMSS;
import util.BDConexao;
import util.DVML;
import util.JPAEntityMannagerFactoryUtil;
import static util.MetodosUtil.rodarComandoWindows;

/**
 *
 * @author MartinhoLuis e Domingos Dala Vunge
 */
public class PrincipalPedidosVisao extends javax.swing.JFrame implements Runnable
{

    private TbPedido pedidos = new TbPedido();
    private static EntityManagerFactory emf = JPAEntityMannagerFactoryUtil.em;
    private static AccessoArmazemDao accessoArmazemDao;
    private static ArmazemDao armazemDao;
    private static PedidoDao pedidoDao = new PedidoDao( emf );
    private static ItemPedidosDao itemPedidosDao = new ItemPedidosDao( emf );
    private UsuarioDao usuarioDao = new UsuarioDao( emf );
    private DadosInstituicaoDao dadosInstituicaoDao = new DadosInstituicaoDao( emf );
    private TurnoDao turnoDao = new TurnoDao( emf );
    private Turno turno;
//    private static CaixaDao caixaDao;
    private int id_turno = 0;
    private static BDConexao conexao;
    private Thread t;
    private int idUser = 0;
    private int id_armazem;
    private String lugar = "";

    public PrincipalPedidosVisao( int idUser, String lugar, int id_armazem, BDConexao conexao )
    {
        initComponents();
        //this.setExtendedState(   this.getExtendedState()|PrincipalPedidosVisao.MAXIMIZED_BOTH  );
        setLocationRelativeTo( null );
//        t = new Thread(this);
//        t.start();

        this.id_armazem = id_armazem;
        this.lugar = lugar;

        this.conexao = conexao;
        this.idUser = idUser;
        armazemDao = new ArmazemDao( emf );
        accessoArmazemDao = new AccessoArmazemDao( emf );
//        cmbArmazem.setModel(new DefaultComboBoxModel(accessoArmazemDao.getAllArmazemByIdUSuario(idUser)));
        procedimento_mesas_livre();
        controlo_turno();
//        procedimento_abrir_mesas();
//        caixaDao = new CaixaDao( emf );
//        verificarCaixa();
//        mostrar_armazem();
        try
        {
            configurar_armazens();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
        setArmazem( dadosInstituicaoDao.findTbDadosInstituicao( 1 ).getConfigArmazens() );
        // aspecto da Gestão de Mesas do SysKitandaHotel, sobre o initComponents
        util.ui.LayoutSeguro.aplicar( this, "Gestão de mesas", this::montarLayoutWeb );
    }


    // =====================================================================
    // LAYOUT WEB + MESAS DINÂMICAS (como a Gestão de Mesas do SysKitandaHotel)
    // As mesas vêm do cadastro (tb_mesas): criam-se e eliminam-se em
    // "Configurar mesas". O estado de todas lê-se numa só consulta, fora do
    // ecrã (SwingWorker), para a janela não travar. Abrir uma mesa faz o mesmo
    // que os botões antigos: NovaGestaoPedidosVisao com o número da mesa.
    // Os 40 botões/etiquetas do initComponents ficam fora de vista (as outras
    // janelas ainda os referem).
    // =====================================================================
    /** De quanto em quanto tempo o plano se actualiza sozinho (outros postos também lançam pedidos). */
    private static final int ACTUALIZAR_MS = 20000;

    /** Janela aberta (para as actualizações pedidas pelo ecrã de pedidos). */
    private static PrincipalPedidosVisao activa;

    private comercial.controller.MesasController mesasControllerWeb;
    private final java.util.Map<String, util.ui.MesasWeb.CartaoMesa> cartoes = new java.util.LinkedHashMap<>();
    private final util.ui.MesasWeb.PainelGrelha grelha = new util.ui.MesasWeb.PainelGrelha( 140, 12 );
    private final javax.swing.JTextField txtNumero = new javax.swing.JTextField();
    private final javax.swing.JRadioButton rbTodas = new javax.swing.JRadioButton( "Todas", true );
    private final javax.swing.JRadioButton rbLivres = new javax.swing.JRadioButton( "Livres" );
    private final javax.swing.JRadioButton rbOcupadas = new javax.swing.JRadioButton( "Ocupadas" );
    private final util.ui.ComponentesWeb.CartaoKpi kpiLivres = new util.ui.ComponentesWeb.CartaoKpi(
            util.ui.ComponentesWeb.CartaoKpi.VISTO, "Mesas livres", util.ui.MesasWeb.LIVRE );
    private final util.ui.ComponentesWeb.CartaoKpi kpiOcupadas = new util.ui.ComponentesWeb.CartaoKpi(
            util.ui.ComponentesWeb.CartaoKpi.PESSOA, "Mesas ocupadas", util.ui.MesasWeb.OCUPADA );
    private final util.ui.ComponentesWeb.CartaoKpi kpiValor = new util.ui.ComponentesWeb.CartaoKpi(
            "Kz", "Valor em aberto", util.ui.ComponentesWeb.AZUL_MEDIO );
    private final util.ui.ComponentesWeb.CartaoKpi kpiAntiga = new util.ui.ComponentesWeb.CartaoKpi(
            "!", "Conta aberta há mais tempo", util.ui.MesasWeb.DEMORADA );
    private final javax.swing.JPanel listaOcupadas = new javax.swing.JPanel();
    private final javax.swing.JLabel lbSemResultados = new javax.swing.JLabel( "Nenhuma mesa corresponde ao filtro.",
            javax.swing.SwingConstants.CENTER );
    private final javax.swing.JLabel lbActualizado = util.ui.FormularioWeb.dica( " " );
    private util.ui.ComponentesWeb.Cartao cartaoPlano;
    private javax.swing.Timer temporizador;
    private long ultimaActualizacao;
    /** Uma leitura de cada vez; um pedido durante a leitura fica para logo a seguir. */
    private boolean aLer = false;
    private boolean lerDeNovo = false;
    /** Junta vários pedidos seguidos (ex.: 8 chamadas a pintar_mesas) numa só leitura. */
    private static javax.swing.Timer agrupador;

    private void montarLayoutWeb()
    {
        mesasControllerWeb = new comercial.controller.MesasController( conexao );

        // ------------------------------------------------ cabeçalho
        javax.swing.JButton btConfigurar = new javax.swing.JButton( "Configurar mesas" );
        util.ui.ComponentesWeb.botao( btConfigurar, util.ui.ComponentesWeb.Variante.SECUNDARIO, "/imagens/Button-Add-icon.png",
                "Criar ou eliminar mesas da sala" );
        pilula( btConfigurar );
        btConfigurar.addActionListener( e -> configurarMesas() );
        javax.swing.JButton btActualizar = new javax.swing.JButton( "Actualizar" );
        util.ui.ComponentesWeb.botao( btActualizar, util.ui.ComponentesWeb.Variante.SECUNDARIO, null,
                "Ler de novo o estado das mesas (F5)" );
        pilula( btActualizar );
        btActualizar.addActionListener( e -> actualizarEstados() );
        jButton17.setText( "Sair" );
        jButton17.setIcon( null );
        util.ui.FormularioWeb.botaoSair( jButton17, "Fechar a gestão de mesas (Esc)" );
        pilula( jButton17 );

        util.ui.FormularioWeb.combo( cmbArmazem );
        cmbArmazem.setPreferredSize( new java.awt.Dimension( 200, 30 ) );
        javax.swing.JPanel direita = util.ui.ComponentesWeb.transparente(
                new util.ui.EstiloUI.WrapLayout( java.awt.FlowLayout.RIGHT, 10, 4 ) );
        direita.add( util.ui.FormularioWeb.campoCabecalho( new javax.swing.JLabel( "Armazém" ), cmbArmazem ) );
        direita.add( btConfigurar );
        direita.add( btActualizar );
        direita.add( jButton17 );
        javax.swing.JPanel cabecalho = util.ui.FormularioWeb.cabecalho( "Gestão de Mesas",
                "Toque numa mesa para abrir ou continuar o pedido", direita );

        // ------------------------------------------------ indicadores
        javax.swing.JPanel kpis = util.ui.FormularioWeb.grelha( 200, 4, kpiLivres, kpiOcupadas, kpiValor, kpiAntiga );

        // ------------------------------------------------ plano da sala
        javax.swing.JPanel filtros = util.ui.FormularioWeb.pilulas( rbTodas, rbLivres, rbOcupadas );
        javax.swing.ButtonGroup g = new javax.swing.ButtonGroup();
        g.add( rbTodas );
        g.add( rbLivres );
        g.add( rbOcupadas );
        for ( javax.swing.JRadioButton rb : new javax.swing.JRadioButton[]
        {
            rbTodas, rbLivres, rbOcupadas
        } )
        {
            rb.addActionListener( e -> aplicarFiltro() );
        }
        cartaoPlano = new util.ui.ComponentesWeb.Cartao( "Plano da sala", util.ui.ComponentesWeb.AZUL_VIVO,
                new java.awt.BorderLayout( 0, 6 ) );
        cartaoPlano.adicionarAccao( filtros );
        javax.swing.JScrollPane rolagem = new javax.swing.JScrollPane( grelha, javax.swing.JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER );
        rolagem.setBorder( null );
        rolagem.setOpaque( false );
        rolagem.getViewport().setOpaque( false );
        rolagem.getVerticalScrollBar().setUnitIncrement( 24 );
        rolagem.setMinimumSize( new java.awt.Dimension( 200, 180 ) );
        javax.swing.JPanel legenda = util.ui.ComponentesWeb.transparente( new java.awt.FlowLayout( java.awt.FlowLayout.LEFT, 14, 0 ) );
        legenda.add( util.ui.MesasWeb.legenda( "Livre", util.ui.MesasWeb.LIVRE ) );
        legenda.add( util.ui.MesasWeb.legenda( "Ocupada", util.ui.MesasWeb.OCUPADA ) );
        legenda.add( util.ui.MesasWeb.legenda( "Aberta há mais de " + ( util.ui.MesasWeb.MINUTOS_DEMORA / 60 ) + " h",
                util.ui.MesasWeb.DEMORADA ) );

        // mensagens de estado do código (o .form traz textos de teste)
        if ( "sahdbvhsdva".equals( status_mensagem_primaria.getText() ) )
        {
            status_mensagem_primaria.setText( " " );
        }
        if ( "...".equals( status_mensagem_secundaria.getText() ) )
        {
            status_mensagem_secundaria.setText( " " );
        }
        status_mensagem_primaria.setFont( util.ui.EstiloUI.FONTE_NEGRITO.deriveFont( 12.5f ) );
        status_mensagem_primaria.setForeground( util.ui.ComponentesWeb.VERMELHO );
        status_mensagem_secundaria.setFont( util.ui.EstiloUI.FONTE.deriveFont( 12.5f ) );
        status_mensagem_secundaria.setForeground( util.ui.ComponentesWeb.TEXTO_SUAVE );
        javax.swing.JPanel mensagens = util.ui.ComponentesWeb.transparente( new java.awt.GridLayout( 0, 1 ) );
        mensagens.add( status_mensagem_primaria );
        mensagens.add( status_mensagem_secundaria );
        mensagens.add( lbActualizado );

        javax.swing.JPanel rodapePlano = util.ui.ComponentesWeb.transparente( new java.awt.BorderLayout() );
        rodapePlano.add( legenda, java.awt.BorderLayout.WEST );
        rodapePlano.add( mensagens, java.awt.BorderLayout.EAST );
        lbSemResultados.setFont( util.ui.EstiloUI.FONTE.deriveFont( 13f ) );
        lbSemResultados.setForeground( util.ui.ComponentesWeb.TEXTO_SUAVE );
        lbSemResultados.setVisible( false );
        javax.swing.JPanel meio = util.ui.ComponentesWeb.transparente( new java.awt.BorderLayout() );
        meio.add( lbSemResultados, java.awt.BorderLayout.NORTH );
        meio.add( rolagem, java.awt.BorderLayout.CENTER );
        cartaoPlano.getCorpo().add( meio, java.awt.BorderLayout.CENTER );
        cartaoPlano.getCorpo().add( rodapePlano, java.awt.BorderLayout.SOUTH );

        // ------------------------------------------------ acesso rápido
        javax.swing.JPanel lateral = util.ui.ComponentesWeb.transparente( new java.awt.BorderLayout( 0, 10 ) );
        lateral.add( cartaoTeclado(), java.awt.BorderLayout.NORTH );
        lateral.add( cartaoOcupadas(), java.awt.BorderLayout.CENTER );

        // à esquerda o acesso rápido (abrir pelo número e contas em aberto), à direita o plano
        javax.swing.JPanel colunas = util.ui.ComponentesWeb.transparente( new util.ui.ComponentesWeb.ColunasResponsivas(
                new double[]
                {
                    0.0, 1.0
                }, new int[]
                {
                    290, 440
                }, 10 ) );
        colunas.add( util.ui.ComponentesWeb.rolagemVertical( lateral ) );
        colunas.add( cartaoPlano );

        javax.swing.JPanel conteudo = util.ui.ComponentesWeb.transparente( new java.awt.BorderLayout( 0, 8 ) );
        conteudo.add( kpis, java.awt.BorderLayout.NORTH );
        conteudo.add( colunas, java.awt.BorderLayout.CENTER );

        javax.swing.JPanel accoes = util.ui.ComponentesWeb.transparente( null ); // tudo está no cabeçalho e no teclado
        accoes.setPreferredSize( new java.awt.Dimension( 0, 0 ) );
        util.ui.FormularioWeb.montar( this, cabecalho, conteudo, accoes,
                "Plano de mesas  ·  actualiza sozinho a cada " + ( ACTUALIZAR_MS / 1000 ) + " s  ·  F5 actualiza já" );

        getRootPane().registerKeyboardAction( e -> actualizarEstados(),
                javax.swing.KeyStroke.getKeyStroke( java.awt.event.KeyEvent.VK_F5, 0 ), javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW );
        getRootPane().registerKeyboardAction( e -> jButton17.doClick(),
                javax.swing.KeyStroke.getKeyStroke( java.awt.event.KeyEvent.VK_ESCAPE, 0 ), javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW );

        // ------------------------------------------------ ciclo de vida
        activa = this;
        actualizarEstados();
        temporizador = new javax.swing.Timer( ACTUALIZAR_MS, e ->
        {
            if ( isShowing() )
            {
                actualizarEstados();
            }
        } );
        temporizador.start();
        addWindowListener( new java.awt.event.WindowAdapter()
        {
            @Override
            public void windowActivated( java.awt.event.WindowEvent e )
            {
                activa = PrincipalPedidosVisao.this;
                // ao voltar do ecrã de pedidos (sem repetir se acabou de actualizar)
                if ( System.currentTimeMillis() - ultimaActualizacao > 3000 )
                {
                    actualizarEstados();
                }
                txtNumero.requestFocusInWindow();
            }

            @Override
            public void windowClosed( java.awt.event.WindowEvent e )
            {
                temporizador.stop();
                if ( activa == PrincipalPedidosVisao.this )
                {
                    activa = null;
                }
            }
        } );
        util.ui.EstiloUI.ajustarAoEcra( this, 980, 640 );
    }

    private static void pilula( javax.swing.JButton b )
    {
        b.putClientProperty( "web.pilula", Boolean.TRUE );
        b.setBorder( javax.swing.BorderFactory.createEmptyBorder( 6, 14, 6, 16 ) );
        b.setFont( util.ui.EstiloUI.FONTE_NEGRITO.deriveFont( 12.5f ) );
    }

    /** Visor + teclado numérico: escreve o número e abre (como num POS táctil). */
    private javax.swing.JComponent cartaoTeclado()
    {
        txtNumero.setFont( util.ui.MesasWeb.fonteVisor( 30 ) );
        txtNumero.setHorizontalAlignment( javax.swing.SwingConstants.CENTER );
        txtNumero.setForeground( new java.awt.Color( 134, 239, 172 ) );
        txtNumero.setBackground( new java.awt.Color( 15, 23, 42 ) );
        txtNumero.setCaretColor( java.awt.Color.WHITE );
        txtNumero.setBorder( javax.swing.BorderFactory.createEmptyBorder( 8, 10, 8, 10 ) );
        txtNumero.setToolTipText( "Escreva o número da mesa e carregue Enter; enquanto escreve, o plano mostra só as mesas que começam por esse número" );
        txtNumero.getDocument().addDocumentListener( new javax.swing.event.DocumentListener()
        {
            @Override
            public void insertUpdate( javax.swing.event.DocumentEvent e )
            {
                aplicarFiltro();
            }

            @Override
            public void removeUpdate( javax.swing.event.DocumentEvent e )
            {
                aplicarFiltro();
            }

            @Override
            public void changedUpdate( javax.swing.event.DocumentEvent e )
            {
                aplicarFiltro();
            }
        } );
        txtNumero.addActionListener( e -> abrirPeloVisor() );
        util.ui.ComponentesWeb.CaixaSuave visor = new util.ui.ComponentesWeb.CaixaSuave( new java.awt.BorderLayout(),
                new java.awt.Color( 15, 23, 42 ), null );
        visor.setBorder( javax.swing.BorderFactory.createEmptyBorder( 4, 4, 4, 4 ) );
        visor.add( txtNumero, java.awt.BorderLayout.CENTER );

        javax.swing.JPanel teclas = util.ui.ComponentesWeb.transparente( new java.awt.GridLayout( 4, 3, 8, 8 ) );
        for ( String t : new String[]
        {
            "1", "2", "3", "4", "5", "6", "7", "8", "9"
        } )
        {
            teclas.add( teclaNumero( t ) );
        }
        javax.swing.JButton limpar = util.ui.MesasWeb.tecla( "C", util.ui.ComponentesWeb.Variante.AVISO );
        limpar.setToolTipText( "Limpar" );
        limpar.addActionListener( e ->
        {
            txtNumero.setText( "" );
            txtNumero.requestFocusInWindow();
        } );
        teclas.add( limpar );
        teclas.add( teclaNumero( "0" ) );
        javax.swing.JButton apagar = util.ui.MesasWeb.tecla( "<", util.ui.ComponentesWeb.Variante.SECUNDARIO );
        apagar.setToolTipText( "Apagar o último algarismo" );
        apagar.addActionListener( e ->
        {
            String t = txtNumero.getText();
            if ( !t.isEmpty() )
            {
                txtNumero.setText( t.substring( 0, t.length() - 1 ) );
            }
            txtNumero.requestFocusInWindow();
        } );
        teclas.add( apagar );

        javax.swing.JButton abrir = new javax.swing.JButton( "Abrir mesa" );
        util.ui.ComponentesWeb.botao( abrir, util.ui.ComponentesWeb.Variante.SUCESSO, null, "Abrir a mesa do visor (Enter)" );
        abrir.setFont( util.ui.EstiloUI.FONTE_NEGRITO.deriveFont( 15f ) );
        abrir.setBorder( javax.swing.BorderFactory.createEmptyBorder( 11, 16, 11, 16 ) );
        abrir.setFocusable( false );
        abrir.addActionListener( e -> abrirPeloVisor() );

        javax.swing.JPanel corpo = util.ui.FormularioWeb.pilha( 10, visor, teclas, abrir );
        return util.ui.FormularioWeb.cartao( "Abrir mesa pelo número", util.ui.ComponentesWeb.VERDE, corpo );
    }

    private javax.swing.JButton teclaNumero( String t )
    {
        javax.swing.JButton b = util.ui.MesasWeb.tecla( t, util.ui.ComponentesWeb.Variante.SECUNDARIO );
        b.addActionListener( e ->
        {
            if ( txtNumero.getText().length() < 6 )
            {
                txtNumero.setText( txtNumero.getText() + t );
            }
            txtNumero.requestFocusInWindow();
        } );
        return b;
    }

    /** Mesas ocupadas, da mais antiga para a mais recente (atalho para fechar contas). */
    private javax.swing.JComponent cartaoOcupadas()
    {
        listaOcupadas.setLayout( new javax.swing.BoxLayout( listaOcupadas, javax.swing.BoxLayout.Y_AXIS ) );
        listaOcupadas.setOpaque( false );
        javax.swing.JPanel topo = util.ui.ComponentesWeb.transparente( new java.awt.BorderLayout() );
        topo.add( listaOcupadas, java.awt.BorderLayout.NORTH );
        javax.swing.JScrollPane sp = new javax.swing.JScrollPane( topo, javax.swing.JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER );
        sp.setBorder( null );
        sp.setOpaque( false );
        sp.getViewport().setOpaque( false );
        sp.getVerticalScrollBar().setUnitIncrement( 16 );
        sp.setPreferredSize( new java.awt.Dimension( 260, 160 ) );
        sp.setMinimumSize( new java.awt.Dimension( 200, 80 ) );
        util.ui.ComponentesWeb.Cartao c = new util.ui.ComponentesWeb.Cartao( "Contas em aberto", util.ui.MesasWeb.OCUPADA,
                new java.awt.BorderLayout() );
        c.getCorpo().add( sp, java.awt.BorderLayout.CENTER );
        return c;
    }

    // =====================================================================
    // ESTADO DAS MESAS (uma consulta, fora do ecrã)
    // =====================================================================
    private void actualizarEstados()
    {
        if ( mesasControllerWeb == null )
        {
            return;
        }
        if ( aLer )
        {
            lerDeNovo = true;
            return;
        }
        aLer = true;
        ultimaActualizacao = System.currentTimeMillis();
        new javax.swing.SwingWorker<java.util.List<comercial.controller.MesasController.EstadoMesa>, Void>()
        {
            @Override
            protected java.util.List<comercial.controller.MesasController.EstadoMesa> doInBackground() throws Exception
            {
                return mesasControllerWeb.estadoMesas();
            }

            @Override
            protected void done()
            {
                aLer = false;
                try
                {
                    aplicarEstados( get() );
                }
                catch ( Exception e )
                {
                    e.printStackTrace();
                    lbActualizado.setText( "Não foi possível ler o estado das mesas" );
                    lbActualizado.setForeground( util.ui.ComponentesWeb.VERMELHO );
                }
                if ( lerDeNovo )
                {
                    lerDeNovo = false;
                    actualizarEstados();
                }
            }
        }.execute();
    }

    private void aplicarEstados( java.util.List<comercial.controller.MesasController.EstadoMesa> lista )
    {
        boolean mesmas = lista.size() == cartoes.size();
        if ( mesmas )
        {
            int i = 0;
            for ( String chave : cartoes.keySet() )
            {
                if ( !chave.equalsIgnoreCase( lista.get( i++ ).designacao ) )
                {
                    mesmas = false;
                    break;
                }
            }
        }
        if ( mesmas )
        {
            for ( comercial.controller.MesasController.EstadoMesa e : lista )
            {
                cartoes.get( e.designacao.toUpperCase() ).setEstado( e );
            }
        }
        else
        {
            // mesas criadas ou eliminadas: refaz o plano
            cartoes.clear();
            grelha.removeAll();
            for ( comercial.controller.MesasController.EstadoMesa e : lista )
            {
                final util.ui.MesasWeb.CartaoMesa c = new util.ui.MesasWeb.CartaoMesa( e );
                c.addAccao( () -> abrirMesa( c.getNumero() ) );
                cartoes.put( e.designacao.toUpperCase(), c );
                grelha.add( c );
            }
        }
        actualizarResumo( lista );
        aplicarFiltro();
        lbActualizado.setForeground( util.ui.ComponentesWeb.TEXTO_SUAVE );
        lbActualizado.setText( "Actualizado às " + new java.text.SimpleDateFormat( "HH:mm:ss" ).format( new java.util.Date() ) );
    }

    private void actualizarResumo( java.util.List<comercial.controller.MesasController.EstadoMesa> lista )
    {
        int ocupadas = 0;
        double valor = 0;
        comercial.controller.MesasController.EstadoMesa antiga = null;
        java.util.List<comercial.controller.MesasController.EstadoMesa> abertas = new java.util.ArrayList<>();
        for ( comercial.controller.MesasController.EstadoMesa e : lista )
        {
            if ( e.itens > 0 )
            {
                ocupadas++;
                valor += e.total;
                abertas.add( e );
                if ( e.desde != null && ( antiga == null || e.desde.before( antiga.desde ) ) )
                {
                    antiga = e;
                }
            }
        }
        int total = lista.size();
        int livres = total - ocupadas;
        kpiLivres.setValor( String.valueOf( livres ), "de " + total + " mesas" );
        kpiOcupadas.setValor( String.valueOf( ocupadas ),
                total == 0 ? " " : Math.round( ocupadas * 100.0 / total ) + "% da sala" );
        kpiValor.setValor( kitanda.util.CfMethods.formatarComoMoeda( valor ),
                ocupadas == 0 ? "nenhuma conta aberta" : "em " + ocupadas + ( ocupadas == 1 ? " mesa" : " mesas" ) );
        if ( antiga != null )
        {
            long min = ( System.currentTimeMillis() - antiga.desde.getTime() ) / 60000L;
            kpiAntiga.setValor( antiga.designacao, util.ui.MesasWeb.tempo( min, antiga.desde ) );
        }
        else
        {
            kpiAntiga.setValor( "—", "nenhuma conta aberta" );
        }
        cartaoPlano.getTitulo().setText( "Plano da sala · " + total + ( total == 1 ? " mesa" : " mesas" ) );

        // contas em aberto, da mais antiga para a mais recente
        abertas.sort( ( a, b ) ->
        {
            if ( a.desde == null )
            {
                return b.desde == null ? 0 : 1;
            }
            return b.desde == null ? -1 : a.desde.compareTo( b.desde );
        } );
        listaOcupadas.removeAll();
        if ( abertas.isEmpty() )
        {
            javax.swing.JLabel vazio = util.ui.FormularioWeb.dica( total == 0
                    ? "Ainda não há mesas: crie-as em \"Configurar mesas\"." : "Todas as mesas estão livres." );
            vazio.setBorder( javax.swing.BorderFactory.createEmptyBorder( 6, 4, 6, 4 ) );
            listaOcupadas.add( vazio );
        }
        for ( comercial.controller.MesasController.EstadoMesa e : abertas )
        {
            long min = e.desde == null ? -1 : ( System.currentTimeMillis() - e.desde.getTime() ) / 60000L;
            javax.swing.JButton b = new javax.swing.JButton( "<html><b>" + e.designacao + "</b> &nbsp; "
                    + kitanda.util.CfMethods.formatarComoMoeda( e.total )
                    + "<br><span style='color:#64748b'>" + e.itens + ( e.itens == 1 ? " item" : " itens" )
                    + ( min >= 0 ? " · " + util.ui.MesasWeb.tempo( min, e.desde ) : "" ) + "</span></html>" );
            util.ui.ComponentesWeb.botao( b, util.ui.ComponentesWeb.Variante.SECUNDARIO, null, "Abrir a " + e.designacao );
            b.setIcon( new util.ui.ComponentesWeb.IconeCor( min >= util.ui.MesasWeb.MINUTOS_DEMORA
                    ? util.ui.MesasWeb.DEMORADA : util.ui.MesasWeb.OCUPADA, 10 ) );
            b.setHorizontalAlignment( javax.swing.SwingConstants.LEFT );
            b.setFont( util.ui.EstiloUI.FONTE.deriveFont( 12.5f ) );
            b.setForeground( util.ui.ComponentesWeb.TEXTO );
            b.setBorder( javax.swing.BorderFactory.createEmptyBorder( 6, 10, 6, 10 ) );
            b.setAlignmentX( 0f );
            b.setMaximumSize( new java.awt.Dimension( Integer.MAX_VALUE, b.getPreferredSize().height ) );
            String numero = e.getNumero();
            b.addActionListener( ev -> abrirMesa( numero ) );
            listaOcupadas.add( b );
            listaOcupadas.add( javax.swing.Box.createVerticalStrut( 6 ) );
        }
        listaOcupadas.revalidate();
        listaOcupadas.repaint();
    }

    /** Filtro das pílulas (todas/livres/ocupadas) e do número escrito no visor. */
    private void aplicarFiltro()
    {
        String prefixo = txtNumero.getText().trim().toUpperCase();
        int visiveis = 0;
        for ( util.ui.MesasWeb.CartaoMesa c : cartoes.values() )
        {
            boolean ok = ( rbTodas.isSelected()
                    || ( rbLivres.isSelected() && !c.isOcupada() )
                    || ( rbOcupadas.isSelected() && c.isOcupada() ) )
                    && ( prefixo.isEmpty() || c.getNumero().toUpperCase().startsWith( prefixo ) );
            c.setVisible( ok );
            if ( ok )
            {
                visiveis++;
            }
        }
        lbSemResultados.setVisible( visiveis == 0 && !cartoes.isEmpty() );
        grelha.revalidate();
        grelha.repaint();
    }

    // =====================================================================
    // ACÇÕES
    // =====================================================================
    private void abrirPeloVisor()
    {
        String numero = txtNumero.getText().trim();
        if ( numero.isEmpty() )
        {
            return;
        }
        if ( !cartoes.containsKey( ( "MESA " + numero ).toUpperCase() ) )
        {
            javax.swing.JOptionPane.showMessageDialog( this, "A MESA " + numero + " não existe.\n"
                    + "Pode criá-la em \"Configurar mesas\".", "Gestão de Mesas", javax.swing.JOptionPane.WARNING_MESSAGE );
            txtNumero.selectAll();
            return;
        }
        txtNumero.setText( "" );
        abrirMesa( numero );
    }

    /** O ecrã de pedidos recebe o número (sem "MESA "), como os botões antigos. */
    private void abrirMesa( String numero )
    {
        new NovaGestaoPedidosVisao( numero, lugar, this.idUser, id_armazem, BDConexao.getInstancia() ).setVisible( true );
    }

    private void configurarMesas()
    {
        new MesasCadastroVisao( this, conexao, null, this::actualizarEstados ).setVisible( true );
        actualizarEstados();
    }

    /** Pede uma leitura à janela aberta; vários pedidos seguidos dão uma só consulta. */
    private static void pedirActualizacao()
    {
        if ( activa == null || !activa.isDisplayable() )
        {
            return;
        }
        Runnable agendar = () ->
        {
            if ( agrupador == null )
            {
                agrupador = new javax.swing.Timer( 300, e ->
                {
                    if ( activa != null && activa.isDisplayable() )
                    {
                        activa.actualizarEstados();
                    }
                } );
                agrupador.setRepeats( false );
            }
            agrupador.restart();
        };
        if ( javax.swing.SwingUtilities.isEventDispatchThread() )
        {
            agendar.run();
        }
        else
        {
            javax.swing.SwingUtilities.invokeLater( agendar );
        }
    }

    private void setArmazem( String armazem )
    {
        if ( armazem.equalsIgnoreCase( "Multi_armazem" ) )
        {
            rbArmazem.setSelected( true );

        }
        else
        {
            rbArmazem1.setSelected( true );

        }
    }

    public void configurar_armazens()
    {
        setArmazem( dadosInstituicaoDao.findTbDadosInstituicao( 1 ).getConfigArmazens() );
        try
        {
            if ( !rbArmazem.isSelected() )
            {
                //                Caso for MultiArmazens
                cmbArmazem.setModel( new DefaultComboBoxModel( armazemDao.buscaTodos2() ) );
//                cmbArmazem.setModel( new DefaultComboBoxModel( accessoArmazemDao.getAllArmazemByIdUSuario( cod_usuario ) ) );

            }
            else if ( rbArmazem.isSelected() )
            {
                //                Caso for apenas Um Armazem
                cmbArmazem.setModel( new DefaultComboBoxModel( accessoArmazemDao.getAllArmazemExceptoEconomatoByIdUSuario( idUser ) ) );

            }
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings( "unchecked" )
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        status_mensagem_primaria = new javax.swing.JLabel();
        status_mensagem_secundaria = new javax.swing.JLabel();
        cmbArmazem = new javax.swing.JComboBox<>();
        rbArmazem1 = new javax.swing.JRadioButton();
        rbArmazem = new javax.swing.JRadioButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel2 = new javax.swing.JPanel();
        btn_mesa_1 = new javax.swing.JButton();
        btn_mesa_2 = new javax.swing.JButton();
        btn_mesa_3 = new javax.swing.JButton();
        btn_mesa_4 = new javax.swing.JButton();
        btn_mesa_5 = new javax.swing.JButton();
        btn_mesa_6 = new javax.swing.JButton();
        btn_mesa_7 = new javax.swing.JButton();
        btn_mesa_9 = new javax.swing.JButton();
        btn_mesa_8 = new javax.swing.JButton();
        btn_mesa_10 = new javax.swing.JButton();
        btn_mesa_11 = new javax.swing.JButton();
        btn_mesa_12 = new javax.swing.JButton();
        btn_mesa_13 = new javax.swing.JButton();
        btn_mesa_14 = new javax.swing.JButton();
        btn_mesa_16 = new javax.swing.JButton();
        lb_mesa_05 = new javax.swing.JLabel();
        lb_mesa_02 = new javax.swing.JLabel();
        lb_mesa_04 = new javax.swing.JLabel();
        lb_mesa_03 = new javax.swing.JLabel();
        lb_mesa_01 = new javax.swing.JLabel();
        lb_mesa_06 = new javax.swing.JLabel();
        lb_mesa_07 = new javax.swing.JLabel();
        lb_mesa_08 = new javax.swing.JLabel();
        lb_mesa_09 = new javax.swing.JLabel();
        lb_mesa_10 = new javax.swing.JLabel();
        lb_mesa_11 = new javax.swing.JLabel();
        lb_mesa_12 = new javax.swing.JLabel();
        lb_mesa_13 = new javax.swing.JLabel();
        lb_mesa_14 = new javax.swing.JLabel();
        lb_mesa_15 = new javax.swing.JLabel();
        lb_mesa_16 = new javax.swing.JLabel();
        btn_mesa_15 = new javax.swing.JButton();
        btn_mesa_32 = new javax.swing.JButton();
        lb_mesa_32 = new javax.swing.JLabel();
        btn_mesa_24 = new javax.swing.JButton();
        lb_mesa_24 = new javax.swing.JLabel();
        lb_mesa_23 = new javax.swing.JLabel();
        btn_mesa_23 = new javax.swing.JButton();
        btn_mesa_31 = new javax.swing.JButton();
        lb_mesa_31 = new javax.swing.JLabel();
        lb_mesa_30 = new javax.swing.JLabel();
        btn_mesa_30 = new javax.swing.JButton();
        btn_mesa_22 = new javax.swing.JButton();
        lb_mesa_22 = new javax.swing.JLabel();
        lb_mesa_21 = new javax.swing.JLabel();
        btn_mesa_21 = new javax.swing.JButton();
        btn_mesa_29 = new javax.swing.JButton();
        lb_mesa_29 = new javax.swing.JLabel();
        lb_mesa_28 = new javax.swing.JLabel();
        btn_mesa_28 = new javax.swing.JButton();
        btn_mesa_20 = new javax.swing.JButton();
        lb_mesa_20 = new javax.swing.JLabel();
        btn_mesa_19 = new javax.swing.JButton();
        lb_mesa_19 = new javax.swing.JLabel();
        btn_mesa_27 = new javax.swing.JButton();
        lb_mesa_27 = new javax.swing.JLabel();
        lb_mesa_26 = new javax.swing.JLabel();
        btn_mesa_26 = new javax.swing.JButton();
        btn_mesa_18 = new javax.swing.JButton();
        lb_mesa_18 = new javax.swing.JLabel();
        btn_mesa_17 = new javax.swing.JButton();
        btn_mesa_25 = new javax.swing.JButton();
        lb_mesa_17 = new javax.swing.JLabel();
        lb_mesa_25 = new javax.swing.JLabel();
        btn_mesa_33 = new javax.swing.JButton();
        btn_mesa_34 = new javax.swing.JButton();
        lb_mesa_33 = new javax.swing.JLabel();
        lb_mesa_34 = new javax.swing.JLabel();
        btn_mesa_35 = new javax.swing.JButton();
        lb_mesa_35 = new javax.swing.JLabel();
        btn_mesa_36 = new javax.swing.JButton();
        lb_mesa_36 = new javax.swing.JLabel();
        btn_mesa_37 = new javax.swing.JButton();
        lb_mesa_37 = new javax.swing.JLabel();
        btn_mesa_38 = new javax.swing.JButton();
        lb_mesa_38 = new javax.swing.JLabel();
        btn_mesa_39 = new javax.swing.JButton();
        lb_mesa_39 = new javax.swing.JLabel();
        btn_mesa_40 = new javax.swing.JButton();
        lb_mesa_40 = new javax.swing.JLabel();
        jButton17 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 204, 51));

        jLabel1.setBackground(new java.awt.Color(102, 102, 102));
        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 30)); // NOI18N
        jLabel1.setText("GESTÃO  DE  MESAS");

        status_mensagem_primaria.setFont(new java.awt.Font("Lucida Grande", 1, 12)); // NOI18N
        status_mensagem_primaria.setForeground(new java.awt.Color(255, 0, 51));
        status_mensagem_primaria.setText("sahdbvhsdva");

        status_mensagem_secundaria.setFont(new java.awt.Font("Lucida Grande", 1, 12)); // NOI18N
        status_mensagem_secundaria.setText("...");

        cmbArmazem.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        cmbArmazem.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbArmazem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbArmazemActionPerformed(evt);
            }
        });

        buttonGroup1.add(rbArmazem1);
        rbArmazem1.setEnabled(false);
        rbArmazem1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                rbArmazem1ActionPerformed(evt);
            }
        });

        buttonGroup1.add(rbArmazem);
        rbArmazem.setSelected(true);
        rbArmazem.setEnabled(false);
        rbArmazem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                rbArmazemActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 328, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(335, 335, 335)
                        .addComponent(rbArmazem1)
                        .addGap(18, 18, 18)
                        .addComponent(rbArmazem)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmbArmazem, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(status_mensagem_secundaria, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(status_mensagem_primaria, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 725, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(status_mensagem_primaria)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(status_mensagem_secundaria)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(rbArmazem, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(rbArmazem1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(cmbArmazem, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(49, 49, 49))))
        );

        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btn_mesa_1.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btn_mesa_1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagem/mesa_teste.jpg"))); // NOI18N
        btn_mesa_1.setText("1");
        btn_mesa_1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btn_mesa_1ActionPerformed(evt);
            }
        });
        jPanel2.add(btn_mesa_1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 70, 80));

        btn_mesa_2.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btn_mesa_2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens_mesas/mesa_teste.jpg"))); // NOI18N
        btn_mesa_2.setText("2");
        btn_mesa_2.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btn_mesa_2ActionPerformed(evt);
            }
        });
        jPanel2.add(btn_mesa_2, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 0, 60, 80));

        btn_mesa_3.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btn_mesa_3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens_mesas/mesa_teste.jpg"))); // NOI18N
        btn_mesa_3.setText("3");
        btn_mesa_3.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btn_mesa_3ActionPerformed(evt);
            }
        });
        jPanel2.add(btn_mesa_3, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 0, 60, 80));

        btn_mesa_4.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btn_mesa_4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens_mesas/mesa_teste.jpg"))); // NOI18N
        btn_mesa_4.setText("4");
        btn_mesa_4.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btn_mesa_4ActionPerformed(evt);
            }
        });
        jPanel2.add(btn_mesa_4, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 0, 70, 80));

        btn_mesa_5.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btn_mesa_5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens_mesas/mesa_teste.jpg"))); // NOI18N
        btn_mesa_5.setText("5");
        btn_mesa_5.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btn_mesa_5ActionPerformed(evt);
            }
        });
        jPanel2.add(btn_mesa_5, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 0, 70, 80));

        btn_mesa_6.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btn_mesa_6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens_mesas/mesa_teste.jpg"))); // NOI18N
        btn_mesa_6.setText("6");
        btn_mesa_6.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btn_mesa_6ActionPerformed(evt);
            }
        });
        jPanel2.add(btn_mesa_6, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 0, 70, 80));

        btn_mesa_7.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btn_mesa_7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens_mesas/mesa_teste.jpg"))); // NOI18N
        btn_mesa_7.setText("7");
        btn_mesa_7.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btn_mesa_7ActionPerformed(evt);
            }
        });
        jPanel2.add(btn_mesa_7, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 0, 70, 80));

        btn_mesa_9.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btn_mesa_9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens_mesas/mesa_teste.jpg"))); // NOI18N
        btn_mesa_9.setText("9");
        btn_mesa_9.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btn_mesa_9ActionPerformed(evt);
            }
        });
        jPanel2.add(btn_mesa_9, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, 70, 80));

        btn_mesa_8.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btn_mesa_8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens_mesas/mesa_teste.jpg"))); // NOI18N
        btn_mesa_8.setText("8");
        btn_mesa_8.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btn_mesa_8ActionPerformed(evt);
            }
        });
        jPanel2.add(btn_mesa_8, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 0, 70, 80));

        btn_mesa_10.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btn_mesa_10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens_mesas/mesa_teste.jpg"))); // NOI18N
        btn_mesa_10.setText("10");
        btn_mesa_10.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btn_mesa_10ActionPerformed(evt);
            }
        });
        jPanel2.add(btn_mesa_10, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 100, 60, 80));

        btn_mesa_11.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btn_mesa_11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens_mesas/mesa_teste.jpg"))); // NOI18N
        btn_mesa_11.setText("11");
        btn_mesa_11.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btn_mesa_11ActionPerformed(evt);
            }
        });
        jPanel2.add(btn_mesa_11, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 100, 70, 80));

        btn_mesa_12.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btn_mesa_12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens_mesas/mesa_teste.jpg"))); // NOI18N
        btn_mesa_12.setText("12");
        btn_mesa_12.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btn_mesa_12ActionPerformed(evt);
            }
        });
        jPanel2.add(btn_mesa_12, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 100, 70, 80));

        btn_mesa_13.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btn_mesa_13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens_mesas/mesa_teste.jpg"))); // NOI18N
        btn_mesa_13.setText("13");
        btn_mesa_13.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btn_mesa_13ActionPerformed(evt);
            }
        });
        jPanel2.add(btn_mesa_13, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 100, 70, 80));

        btn_mesa_14.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btn_mesa_14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens_mesas/mesa_teste.jpg"))); // NOI18N
        btn_mesa_14.setText("14");
        btn_mesa_14.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btn_mesa_14ActionPerformed(evt);
            }
        });
        jPanel2.add(btn_mesa_14, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 100, 70, 80));

        btn_mesa_16.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btn_mesa_16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens_mesas/mesa_teste.jpg"))); // NOI18N
        btn_mesa_16.setText("16");
        btn_mesa_16.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btn_mesa_16ActionPerformed(evt);
            }
        });
        jPanel2.add(btn_mesa_16, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 100, 70, 80));

        lb_mesa_05.setFont(new java.awt.Font("Lucida Grande", 1, 24)); // NOI18N
        lb_mesa_05.setText("05");
        jPanel2.add(lb_mesa_05, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 0, -1, 80));

        lb_mesa_02.setFont(new java.awt.Font("Lucida Grande", 1, 24)); // NOI18N
        lb_mesa_02.setText("02");
        jPanel2.add(lb_mesa_02, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 0, 40, 80));

        lb_mesa_04.setFont(new java.awt.Font("Lucida Grande", 1, 24)); // NOI18N
        lb_mesa_04.setText("04");
        jPanel2.add(lb_mesa_04, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 0, -1, 80));

        lb_mesa_03.setFont(new java.awt.Font("Lucida Grande", 1, 24)); // NOI18N
        lb_mesa_03.setText("03");
        jPanel2.add(lb_mesa_03, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 0, -1, 80));

        lb_mesa_01.setBackground(new java.awt.Color(51, 153, 0));
        lb_mesa_01.setFont(new java.awt.Font("Lucida Grande", 1, 24)); // NOI18N
        lb_mesa_01.setText("01");
        lb_mesa_01.setOpaque(true);
        jPanel2.add(lb_mesa_01, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 0, -1, 80));

        lb_mesa_06.setFont(new java.awt.Font("Lucida Grande", 1, 24)); // NOI18N
        lb_mesa_06.setText("06");
        jPanel2.add(lb_mesa_06, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 0, 40, 80));

        lb_mesa_07.setFont(new java.awt.Font("Lucida Grande", 1, 24)); // NOI18N
        lb_mesa_07.setText("07");
        jPanel2.add(lb_mesa_07, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 0, -1, 80));

        lb_mesa_08.setBackground(new java.awt.Color(255, 153, 51));
        lb_mesa_08.setFont(new java.awt.Font("Lucida Grande", 1, 24)); // NOI18N
        lb_mesa_08.setText("08");
        jPanel2.add(lb_mesa_08, new org.netbeans.lib.awtextra.AbsoluteConstraints(870, 0, 40, 80));

        lb_mesa_09.setFont(new java.awt.Font("Lucida Grande", 1, 24)); // NOI18N
        lb_mesa_09.setText("09");
        jPanel2.add(lb_mesa_09, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 100, -1, 80));

        lb_mesa_10.setFont(new java.awt.Font("Lucida Grande", 1, 24)); // NOI18N
        lb_mesa_10.setText("10");
        jPanel2.add(lb_mesa_10, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 100, 40, 80));

        lb_mesa_11.setFont(new java.awt.Font("Lucida Grande", 1, 24)); // NOI18N
        lb_mesa_11.setText("11");
        jPanel2.add(lb_mesa_11, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 100, -1, 80));

        lb_mesa_12.setFont(new java.awt.Font("Lucida Grande", 1, 24)); // NOI18N
        lb_mesa_12.setText("12");
        jPanel2.add(lb_mesa_12, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 100, -1, 80));

        lb_mesa_13.setFont(new java.awt.Font("Lucida Grande", 1, 24)); // NOI18N
        lb_mesa_13.setText("13");
        jPanel2.add(lb_mesa_13, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 100, -1, 80));

        lb_mesa_14.setFont(new java.awt.Font("Lucida Grande", 1, 24)); // NOI18N
        lb_mesa_14.setText("14");
        jPanel2.add(lb_mesa_14, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 100, 40, 80));

        lb_mesa_15.setFont(new java.awt.Font("Lucida Grande", 1, 24)); // NOI18N
        lb_mesa_15.setText("15");
        jPanel2.add(lb_mesa_15, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 100, -1, 80));

        lb_mesa_16.setFont(new java.awt.Font("Lucida Grande", 1, 24)); // NOI18N
        lb_mesa_16.setText("16");
        jPanel2.add(lb_mesa_16, new org.netbeans.lib.awtextra.AbsoluteConstraints(870, 100, -1, 80));

        btn_mesa_15.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btn_mesa_15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens_mesas/mesa_teste.jpg"))); // NOI18N
        btn_mesa_15.setText("15");
        btn_mesa_15.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btn_mesa_15ActionPerformed(evt);
            }
        });
        jPanel2.add(btn_mesa_15, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 100, 70, 80));

        btn_mesa_32.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btn_mesa_32.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens_mesas/mesa_teste.jpg"))); // NOI18N
        btn_mesa_32.setText("32");
        btn_mesa_32.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btn_mesa_32ActionPerformed(evt);
            }
        });
        jPanel2.add(btn_mesa_32, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 300, 70, 80));

        lb_mesa_32.setFont(new java.awt.Font("Lucida Grande", 1, 24)); // NOI18N
        lb_mesa_32.setText("32");
        jPanel2.add(lb_mesa_32, new org.netbeans.lib.awtextra.AbsoluteConstraints(870, 300, -1, 80));

        btn_mesa_24.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btn_mesa_24.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens_mesas/mesa_teste.jpg"))); // NOI18N
        btn_mesa_24.setText("24");
        btn_mesa_24.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btn_mesa_24ActionPerformed(evt);
            }
        });
        jPanel2.add(btn_mesa_24, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 200, 70, 80));

        lb_mesa_24.setBackground(new java.awt.Color(255, 153, 51));
        lb_mesa_24.setFont(new java.awt.Font("Lucida Grande", 1, 24)); // NOI18N
        lb_mesa_24.setText("24");
        jPanel2.add(lb_mesa_24, new org.netbeans.lib.awtextra.AbsoluteConstraints(870, 200, 40, 80));

        lb_mesa_23.setFont(new java.awt.Font("Lucida Grande", 1, 24)); // NOI18N
        lb_mesa_23.setText("23");
        jPanel2.add(lb_mesa_23, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 200, -1, 80));

        btn_mesa_23.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btn_mesa_23.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens_mesas/mesa_teste.jpg"))); // NOI18N
        btn_mesa_23.setText("23");
        btn_mesa_23.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btn_mesa_23ActionPerformed(evt);
            }
        });
        jPanel2.add(btn_mesa_23, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 200, 70, 80));

        btn_mesa_31.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btn_mesa_31.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens_mesas/mesa_teste.jpg"))); // NOI18N
        btn_mesa_31.setText("31");
        btn_mesa_31.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btn_mesa_31ActionPerformed(evt);
            }
        });
        jPanel2.add(btn_mesa_31, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 300, 70, 80));

        lb_mesa_31.setFont(new java.awt.Font("Lucida Grande", 1, 24)); // NOI18N
        lb_mesa_31.setText("31");
        jPanel2.add(lb_mesa_31, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 300, -1, 80));

        lb_mesa_30.setFont(new java.awt.Font("Lucida Grande", 1, 24)); // NOI18N
        lb_mesa_30.setText("30");
        jPanel2.add(lb_mesa_30, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 300, 40, 80));

        btn_mesa_30.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btn_mesa_30.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens_mesas/mesa_teste.jpg"))); // NOI18N
        btn_mesa_30.setText("30");
        btn_mesa_30.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btn_mesa_30ActionPerformed(evt);
            }
        });
        jPanel2.add(btn_mesa_30, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 300, 70, 80));

        btn_mesa_22.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btn_mesa_22.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens_mesas/mesa_teste.jpg"))); // NOI18N
        btn_mesa_22.setText("22");
        btn_mesa_22.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btn_mesa_22ActionPerformed(evt);
            }
        });
        jPanel2.add(btn_mesa_22, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 200, 70, 80));

        lb_mesa_22.setFont(new java.awt.Font("Lucida Grande", 1, 24)); // NOI18N
        lb_mesa_22.setText("22");
        jPanel2.add(lb_mesa_22, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 200, 40, 80));

        lb_mesa_21.setFont(new java.awt.Font("Lucida Grande", 1, 24)); // NOI18N
        lb_mesa_21.setText("21");
        jPanel2.add(lb_mesa_21, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 200, -1, 80));

        btn_mesa_21.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btn_mesa_21.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens_mesas/mesa_teste.jpg"))); // NOI18N
        btn_mesa_21.setText("21");
        btn_mesa_21.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btn_mesa_21ActionPerformed(evt);
            }
        });
        jPanel2.add(btn_mesa_21, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 200, 70, 80));

        btn_mesa_29.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btn_mesa_29.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens_mesas/mesa_teste.jpg"))); // NOI18N
        btn_mesa_29.setText("29");
        btn_mesa_29.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btn_mesa_29ActionPerformed(evt);
            }
        });
        jPanel2.add(btn_mesa_29, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 300, 70, 80));

        lb_mesa_29.setFont(new java.awt.Font("Lucida Grande", 1, 24)); // NOI18N
        lb_mesa_29.setText("29");
        jPanel2.add(lb_mesa_29, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 300, -1, 80));

        lb_mesa_28.setFont(new java.awt.Font("Lucida Grande", 1, 24)); // NOI18N
        lb_mesa_28.setText("28");
        jPanel2.add(lb_mesa_28, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 300, -1, 80));

        btn_mesa_28.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btn_mesa_28.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens_mesas/mesa_teste.jpg"))); // NOI18N
        btn_mesa_28.setText("28");
        btn_mesa_28.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btn_mesa_28ActionPerformed(evt);
            }
        });
        jPanel2.add(btn_mesa_28, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 300, 70, 80));

        btn_mesa_20.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btn_mesa_20.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens_mesas/mesa_teste.jpg"))); // NOI18N
        btn_mesa_20.setText("20");
        btn_mesa_20.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btn_mesa_20ActionPerformed(evt);
            }
        });
        jPanel2.add(btn_mesa_20, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 200, 70, 80));

        lb_mesa_20.setFont(new java.awt.Font("Lucida Grande", 1, 24)); // NOI18N
        lb_mesa_20.setText("20");
        jPanel2.add(lb_mesa_20, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 200, -1, 80));

        btn_mesa_19.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btn_mesa_19.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens_mesas/mesa_teste.jpg"))); // NOI18N
        btn_mesa_19.setText("19");
        btn_mesa_19.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btn_mesa_19ActionPerformed(evt);
            }
        });
        jPanel2.add(btn_mesa_19, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 200, 70, 80));

        lb_mesa_19.setFont(new java.awt.Font("Lucida Grande", 1, 24)); // NOI18N
        lb_mesa_19.setText("19");
        jPanel2.add(lb_mesa_19, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 200, -1, 80));

        btn_mesa_27.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btn_mesa_27.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens_mesas/mesa_teste.jpg"))); // NOI18N
        btn_mesa_27.setText("27");
        btn_mesa_27.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btn_mesa_27ActionPerformed(evt);
            }
        });
        jPanel2.add(btn_mesa_27, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 300, 70, 80));

        lb_mesa_27.setFont(new java.awt.Font("Lucida Grande", 1, 24)); // NOI18N
        lb_mesa_27.setText("27");
        jPanel2.add(lb_mesa_27, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 300, -1, 80));

        lb_mesa_26.setFont(new java.awt.Font("Lucida Grande", 1, 24)); // NOI18N
        lb_mesa_26.setText("26");
        jPanel2.add(lb_mesa_26, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 300, 40, 80));

        btn_mesa_26.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btn_mesa_26.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens_mesas/mesa_teste.jpg"))); // NOI18N
        btn_mesa_26.setText("26");
        btn_mesa_26.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btn_mesa_26ActionPerformed(evt);
            }
        });
        jPanel2.add(btn_mesa_26, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 300, 70, 80));

        btn_mesa_18.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btn_mesa_18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens_mesas/mesa_teste.jpg"))); // NOI18N
        btn_mesa_18.setText("18");
        btn_mesa_18.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btn_mesa_18ActionPerformed(evt);
            }
        });
        jPanel2.add(btn_mesa_18, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 200, 70, 80));

        lb_mesa_18.setFont(new java.awt.Font("Lucida Grande", 1, 24)); // NOI18N
        lb_mesa_18.setText("18");
        jPanel2.add(lb_mesa_18, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 200, 40, 80));

        btn_mesa_17.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btn_mesa_17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagem/mesa_teste.jpg"))); // NOI18N
        btn_mesa_17.setText("17");
        btn_mesa_17.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btn_mesa_17ActionPerformed(evt);
            }
        });
        jPanel2.add(btn_mesa_17, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 200, 70, 80));

        btn_mesa_25.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btn_mesa_25.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens_mesas/mesa_teste.jpg"))); // NOI18N
        btn_mesa_25.setText("25");
        btn_mesa_25.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btn_mesa_25ActionPerformed(evt);
            }
        });
        jPanel2.add(btn_mesa_25, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 300, 70, 80));

        lb_mesa_17.setFont(new java.awt.Font("Lucida Grande", 1, 24)); // NOI18N
        lb_mesa_17.setText("17");
        jPanel2.add(lb_mesa_17, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 200, -1, 80));

        lb_mesa_25.setFont(new java.awt.Font("Lucida Grande", 1, 24)); // NOI18N
        lb_mesa_25.setText("25");
        jPanel2.add(lb_mesa_25, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 300, -1, 80));

        btn_mesa_33.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btn_mesa_33.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens_mesas/mesa_teste.jpg"))); // NOI18N
        btn_mesa_33.setText("33");
        btn_mesa_33.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btn_mesa_33ActionPerformed(evt);
            }
        });
        jPanel2.add(btn_mesa_33, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 400, 70, 80));

        btn_mesa_34.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btn_mesa_34.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens_mesas/mesa_teste.jpg"))); // NOI18N
        btn_mesa_34.setText("34");
        btn_mesa_34.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btn_mesa_34ActionPerformed(evt);
            }
        });
        jPanel2.add(btn_mesa_34, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 400, 70, 80));

        lb_mesa_33.setFont(new java.awt.Font("Lucida Grande", 1, 24)); // NOI18N
        lb_mesa_33.setText("33");
        jPanel2.add(lb_mesa_33, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 400, -1, 80));

        lb_mesa_34.setFont(new java.awt.Font("Lucida Grande", 1, 24)); // NOI18N
        lb_mesa_34.setText("34");
        jPanel2.add(lb_mesa_34, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 400, 40, 80));

        btn_mesa_35.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btn_mesa_35.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens_mesas/mesa_teste.jpg"))); // NOI18N
        btn_mesa_35.setText("35");
        btn_mesa_35.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btn_mesa_35ActionPerformed(evt);
            }
        });
        jPanel2.add(btn_mesa_35, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 400, 70, 80));

        lb_mesa_35.setFont(new java.awt.Font("Lucida Grande", 1, 24)); // NOI18N
        lb_mesa_35.setText("35");
        jPanel2.add(lb_mesa_35, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 400, -1, 80));

        btn_mesa_36.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btn_mesa_36.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens_mesas/mesa_teste.jpg"))); // NOI18N
        btn_mesa_36.setText("36");
        btn_mesa_36.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btn_mesa_36ActionPerformed(evt);
            }
        });
        jPanel2.add(btn_mesa_36, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 400, 70, 80));

        lb_mesa_36.setFont(new java.awt.Font("Lucida Grande", 1, 24)); // NOI18N
        lb_mesa_36.setText("36");
        jPanel2.add(lb_mesa_36, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 400, -1, 80));

        btn_mesa_37.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btn_mesa_37.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens_mesas/mesa_teste.jpg"))); // NOI18N
        btn_mesa_37.setText("37");
        btn_mesa_37.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btn_mesa_37ActionPerformed(evt);
            }
        });
        jPanel2.add(btn_mesa_37, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 400, 70, 80));

        lb_mesa_37.setFont(new java.awt.Font("Lucida Grande", 1, 24)); // NOI18N
        lb_mesa_37.setText("37");
        jPanel2.add(lb_mesa_37, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 400, -1, 80));

        btn_mesa_38.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btn_mesa_38.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens_mesas/mesa_teste.jpg"))); // NOI18N
        btn_mesa_38.setText("38");
        btn_mesa_38.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btn_mesa_38ActionPerformed(evt);
            }
        });
        jPanel2.add(btn_mesa_38, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 400, 70, 80));

        lb_mesa_38.setFont(new java.awt.Font("Lucida Grande", 1, 24)); // NOI18N
        lb_mesa_38.setText("38");
        jPanel2.add(lb_mesa_38, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 400, 40, 80));

        btn_mesa_39.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btn_mesa_39.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens_mesas/mesa_teste.jpg"))); // NOI18N
        btn_mesa_39.setText("39");
        btn_mesa_39.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btn_mesa_39ActionPerformed(evt);
            }
        });
        jPanel2.add(btn_mesa_39, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 400, 70, 80));

        lb_mesa_39.setFont(new java.awt.Font("Lucida Grande", 1, 24)); // NOI18N
        lb_mesa_39.setText("39");
        jPanel2.add(lb_mesa_39, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 400, -1, 80));

        btn_mesa_40.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btn_mesa_40.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens_mesas/mesa_teste.jpg"))); // NOI18N
        btn_mesa_40.setText("40");
        btn_mesa_40.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btn_mesa_40ActionPerformed(evt);
            }
        });
        jPanel2.add(btn_mesa_40, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 400, 70, 80));

        lb_mesa_40.setFont(new java.awt.Font("Lucida Grande", 1, 24)); // NOI18N
        lb_mesa_40.setText("40");
        jPanel2.add(lb_mesa_40, new org.netbeans.lib.awtextra.AbsoluteConstraints(870, 400, -1, 80));

        jScrollPane1.setViewportView(jPanel2);

        jButton17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/LOGOUT - VERMELHO/Logout 32x32.png"))); // NOI18N
        jButton17.setText("SAIR");
        jButton17.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButton17ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 3, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton17, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 494, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton17, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton17ActionPerformed
        dispose();

    }//GEN-LAST:event_jButton17ActionPerformed

    private void btn_mesa_1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_mesa_1ActionPerformed

        new NovaGestaoPedidosVisao( btn_mesa_1.getText(), lugar, this.idUser, id_armazem, BDConexao.getInstancia() ).setVisible( true );
    }//GEN-LAST:event_btn_mesa_1ActionPerformed

    private void btn_mesa_2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_mesa_2ActionPerformed
        // TODO add your handling code here:
        new NovaGestaoPedidosVisao( btn_mesa_2.getText(), lugar, this.idUser, id_armazem, BDConexao.getInstancia() ).setVisible( true );
    }//GEN-LAST:event_btn_mesa_2ActionPerformed

    private void btn_mesa_3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_mesa_3ActionPerformed
        // TODO add your handling code here:
        new NovaGestaoPedidosVisao( btn_mesa_3.getText(), lugar, this.idUser, id_armazem, BDConexao.getInstancia() ).setVisible( true );
    }//GEN-LAST:event_btn_mesa_3ActionPerformed

    private void btn_mesa_4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_mesa_4ActionPerformed
        new NovaGestaoPedidosVisao( btn_mesa_4.getText(), lugar, this.idUser, id_armazem, BDConexao.getInstancia() ).setVisible( true );
    }//GEN-LAST:event_btn_mesa_4ActionPerformed

    private void btn_mesa_10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_mesa_10ActionPerformed
        new NovaGestaoPedidosVisao( btn_mesa_10.getText(), "", this.idUser, id_armazem, BDConexao.getInstancia() ).setVisible( true );
    }//GEN-LAST:event_btn_mesa_10ActionPerformed

    private void btn_mesa_5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_mesa_5ActionPerformed
        new NovaGestaoPedidosVisao( btn_mesa_5.getText(), "", this.idUser, id_armazem, BDConexao.getInstancia() ).setVisible( true );
    }//GEN-LAST:event_btn_mesa_5ActionPerformed

    private void btn_mesa_6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_mesa_6ActionPerformed
        new NovaGestaoPedidosVisao( btn_mesa_6.getText(), "", this.idUser, id_armazem, BDConexao.getInstancia() ).setVisible( true );
    }//GEN-LAST:event_btn_mesa_6ActionPerformed

    private void btn_mesa_7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_mesa_7ActionPerformed
        new NovaGestaoPedidosVisao( btn_mesa_7.getText(), "", this.idUser, id_armazem, BDConexao.getInstancia() ).setVisible( true );
    }//GEN-LAST:event_btn_mesa_7ActionPerformed

    private void btn_mesa_9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_mesa_9ActionPerformed
        new NovaGestaoPedidosVisao( btn_mesa_9.getText(), "", this.idUser, id_armazem, BDConexao.getInstancia() ).setVisible( true );
    }//GEN-LAST:event_btn_mesa_9ActionPerformed

    private void btn_mesa_8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_mesa_8ActionPerformed
        new NovaGestaoPedidosVisao( btn_mesa_8.getText(), "", this.idUser, id_armazem, BDConexao.getInstancia() ).setVisible( true );
    }//GEN-LAST:event_btn_mesa_8ActionPerformed

    private void btn_mesa_11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_mesa_11ActionPerformed
        new NovaGestaoPedidosVisao( btn_mesa_11.getText(), "", this.idUser, id_armazem, BDConexao.getInstancia() ).setVisible( true );
    }//GEN-LAST:event_btn_mesa_11ActionPerformed

    private void btn_mesa_12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_mesa_12ActionPerformed
        new NovaGestaoPedidosVisao( btn_mesa_12.getText(), "", this.idUser, id_armazem, BDConexao.getInstancia() ).setVisible( true );
    }//GEN-LAST:event_btn_mesa_12ActionPerformed

    private void btn_mesa_13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_mesa_13ActionPerformed
        new NovaGestaoPedidosVisao( btn_mesa_13.getText(), "", this.idUser, id_armazem, BDConexao.getInstancia() ).setVisible( true );
    }//GEN-LAST:event_btn_mesa_13ActionPerformed

    private void btn_mesa_14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_mesa_14ActionPerformed
        new NovaGestaoPedidosVisao( btn_mesa_14.getText(), "", this.idUser, id_armazem, BDConexao.getInstancia() ).setVisible( true );
    }//GEN-LAST:event_btn_mesa_14ActionPerformed

    private void btn_mesa_16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_mesa_16ActionPerformed
        new NovaGestaoPedidosVisao( btn_mesa_16.getText(), "", this.idUser, id_armazem, BDConexao.getInstancia() ).setVisible( true );
    }//GEN-LAST:event_btn_mesa_16ActionPerformed

    private void btn_mesa_15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_mesa_15ActionPerformed
        // TODO add your handling code here:
        new NovaGestaoPedidosVisao( btn_mesa_15.getText(), "", this.idUser, id_armazem, BDConexao.getInstancia() ).setVisible( true );
    }//GEN-LAST:event_btn_mesa_15ActionPerformed

    private void btn_mesa_32ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btn_mesa_32ActionPerformed
    {//GEN-HEADEREND:event_btn_mesa_32ActionPerformed
        new NovaGestaoPedidosVisao( btn_mesa_32.getText(), "", this.idUser, id_armazem, BDConexao.getInstancia() ).setVisible( true );
    }//GEN-LAST:event_btn_mesa_32ActionPerformed

    private void btn_mesa_24ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btn_mesa_24ActionPerformed
    {//GEN-HEADEREND:event_btn_mesa_24ActionPerformed
        new NovaGestaoPedidosVisao( btn_mesa_24.getText(), "", this.idUser, id_armazem, BDConexao.getInstancia() ).setVisible( true );
    }//GEN-LAST:event_btn_mesa_24ActionPerformed

    private void btn_mesa_23ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btn_mesa_23ActionPerformed
    {//GEN-HEADEREND:event_btn_mesa_23ActionPerformed
        new NovaGestaoPedidosVisao( btn_mesa_23.getText(), "", this.idUser, id_armazem, BDConexao.getInstancia() ).setVisible( true );
    }//GEN-LAST:event_btn_mesa_23ActionPerformed

    private void btn_mesa_31ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btn_mesa_31ActionPerformed
    {//GEN-HEADEREND:event_btn_mesa_31ActionPerformed
        new NovaGestaoPedidosVisao( btn_mesa_31.getText(), "", this.idUser, id_armazem, BDConexao.getInstancia() ).setVisible( true );
    }//GEN-LAST:event_btn_mesa_31ActionPerformed

    private void btn_mesa_30ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btn_mesa_30ActionPerformed
    {//GEN-HEADEREND:event_btn_mesa_30ActionPerformed
        new NovaGestaoPedidosVisao( btn_mesa_30.getText(), "", this.idUser, id_armazem, BDConexao.getInstancia() ).setVisible( true );
    }//GEN-LAST:event_btn_mesa_30ActionPerformed

    private void btn_mesa_22ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btn_mesa_22ActionPerformed
    {//GEN-HEADEREND:event_btn_mesa_22ActionPerformed
        new NovaGestaoPedidosVisao( btn_mesa_22.getText(), "", this.idUser, id_armazem, BDConexao.getInstancia() ).setVisible( true );
    }//GEN-LAST:event_btn_mesa_22ActionPerformed

    private void btn_mesa_21ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btn_mesa_21ActionPerformed
    {//GEN-HEADEREND:event_btn_mesa_21ActionPerformed
        new NovaGestaoPedidosVisao( btn_mesa_21.getText(), "", this.idUser, id_armazem, BDConexao.getInstancia() ).setVisible( true );
    }//GEN-LAST:event_btn_mesa_21ActionPerformed

    private void btn_mesa_29ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btn_mesa_29ActionPerformed
    {//GEN-HEADEREND:event_btn_mesa_29ActionPerformed
        new NovaGestaoPedidosVisao( btn_mesa_29.getText(), "", this.idUser, id_armazem, BDConexao.getInstancia() ).setVisible( true );
    }//GEN-LAST:event_btn_mesa_29ActionPerformed

    private void btn_mesa_28ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btn_mesa_28ActionPerformed
    {//GEN-HEADEREND:event_btn_mesa_28ActionPerformed
        new NovaGestaoPedidosVisao( btn_mesa_28.getText(), "", this.idUser, id_armazem, BDConexao.getInstancia() ).setVisible( true );
    }//GEN-LAST:event_btn_mesa_28ActionPerformed

    private void btn_mesa_20ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btn_mesa_20ActionPerformed
    {//GEN-HEADEREND:event_btn_mesa_20ActionPerformed
        new NovaGestaoPedidosVisao( btn_mesa_20.getText(), "", this.idUser, id_armazem, BDConexao.getInstancia() ).setVisible( true );
    }//GEN-LAST:event_btn_mesa_20ActionPerformed

    private void btn_mesa_19ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btn_mesa_19ActionPerformed
    {//GEN-HEADEREND:event_btn_mesa_19ActionPerformed
        new NovaGestaoPedidosVisao( btn_mesa_19.getText(), "", this.idUser, id_armazem, BDConexao.getInstancia() ).setVisible( true );
    }//GEN-LAST:event_btn_mesa_19ActionPerformed

    private void btn_mesa_27ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btn_mesa_27ActionPerformed
    {//GEN-HEADEREND:event_btn_mesa_27ActionPerformed
        new NovaGestaoPedidosVisao( btn_mesa_27.getText(), "", this.idUser, id_armazem, BDConexao.getInstancia() ).setVisible( true );
    }//GEN-LAST:event_btn_mesa_27ActionPerformed

    private void btn_mesa_26ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btn_mesa_26ActionPerformed
    {//GEN-HEADEREND:event_btn_mesa_26ActionPerformed
        new NovaGestaoPedidosVisao( btn_mesa_26.getText(), "", this.idUser, id_armazem, BDConexao.getInstancia() ).setVisible( true );
    }//GEN-LAST:event_btn_mesa_26ActionPerformed

    private void btn_mesa_18ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btn_mesa_18ActionPerformed
    {//GEN-HEADEREND:event_btn_mesa_18ActionPerformed
        new NovaGestaoPedidosVisao( btn_mesa_18.getText(), "", this.idUser, id_armazem, BDConexao.getInstancia() ).setVisible( true );
    }//GEN-LAST:event_btn_mesa_18ActionPerformed

    private void btn_mesa_17ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btn_mesa_17ActionPerformed
    {//GEN-HEADEREND:event_btn_mesa_17ActionPerformed
        new NovaGestaoPedidosVisao( btn_mesa_17.getText(), "", this.idUser, id_armazem, BDConexao.getInstancia() ).setVisible( true );
    }//GEN-LAST:event_btn_mesa_17ActionPerformed

    private void btn_mesa_25ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btn_mesa_25ActionPerformed
    {//GEN-HEADEREND:event_btn_mesa_25ActionPerformed
        new NovaGestaoPedidosVisao( btn_mesa_25.getText(), "", this.idUser, id_armazem, BDConexao.getInstancia() ).setVisible( true );
    }//GEN-LAST:event_btn_mesa_25ActionPerformed

    private void btn_mesa_33ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btn_mesa_33ActionPerformed
    {//GEN-HEADEREND:event_btn_mesa_33ActionPerformed
        new NovaGestaoPedidosVisao( btn_mesa_33.getText(), "", this.idUser, id_armazem, BDConexao.getInstancia() ).setVisible( true );
    }//GEN-LAST:event_btn_mesa_33ActionPerformed

    private void btn_mesa_34ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btn_mesa_34ActionPerformed
    {//GEN-HEADEREND:event_btn_mesa_34ActionPerformed
        new NovaGestaoPedidosVisao( btn_mesa_34.getText(), "", this.idUser, id_armazem, BDConexao.getInstancia() ).setVisible( true );
    }//GEN-LAST:event_btn_mesa_34ActionPerformed

    private void btn_mesa_35ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btn_mesa_35ActionPerformed
    {//GEN-HEADEREND:event_btn_mesa_35ActionPerformed
        new NovaGestaoPedidosVisao( btn_mesa_35.getText(), "", this.idUser, id_armazem, BDConexao.getInstancia() ).setVisible( true );
    }//GEN-LAST:event_btn_mesa_35ActionPerformed

    private void btn_mesa_36ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btn_mesa_36ActionPerformed
    {//GEN-HEADEREND:event_btn_mesa_36ActionPerformed
        new NovaGestaoPedidosVisao( btn_mesa_36.getText(), "", this.idUser, id_armazem, BDConexao.getInstancia() ).setVisible( true );
    }//GEN-LAST:event_btn_mesa_36ActionPerformed

    private void btn_mesa_37ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btn_mesa_37ActionPerformed
    {//GEN-HEADEREND:event_btn_mesa_37ActionPerformed
        new NovaGestaoPedidosVisao( btn_mesa_37.getText(), "", this.idUser, id_armazem, BDConexao.getInstancia() ).setVisible( true );
    }//GEN-LAST:event_btn_mesa_37ActionPerformed

    private void btn_mesa_38ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btn_mesa_38ActionPerformed
    {//GEN-HEADEREND:event_btn_mesa_38ActionPerformed
        new NovaGestaoPedidosVisao( btn_mesa_38.getText(), "", this.idUser, id_armazem, BDConexao.getInstancia() ).setVisible( true );
    }//GEN-LAST:event_btn_mesa_38ActionPerformed

    private void btn_mesa_39ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btn_mesa_39ActionPerformed
    {//GEN-HEADEREND:event_btn_mesa_39ActionPerformed
        new NovaGestaoPedidosVisao( btn_mesa_39.getText(), "", this.idUser, id_armazem, BDConexao.getInstancia() ).setVisible( true );
    }//GEN-LAST:event_btn_mesa_39ActionPerformed

    private void btn_mesa_40ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btn_mesa_40ActionPerformed
    {//GEN-HEADEREND:event_btn_mesa_40ActionPerformed
        new NovaGestaoPedidosVisao( btn_mesa_40.getText(), "", this.idUser, id_armazem, BDConexao.getInstancia() ).setVisible( true );
    }//GEN-LAST:event_btn_mesa_40ActionPerformed

    private void cmbArmazemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbArmazemActionPerformed
    {//GEN-HEADEREND:event_cmbArmazemActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbArmazemActionPerformed

    private void rbArmazem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbArmazem1ActionPerformed
        try
        {
            configurar_armazens();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }//GEN-LAST:event_rbArmazem1ActionPerformed

    private void rbArmazemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbArmazemActionPerformed
        try
        {
            configurar_armazens();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }//GEN-LAST:event_rbArmazemActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main( String args[] )
    {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try
        {
            for ( javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels() )
            {
                if ( "Windows".equals( info.getName() ) )
                {
                    javax.swing.UIManager.setLookAndFeel( info.getClassName() );
                    break;
                }
            }
        }
        catch ( ClassNotFoundException ex )
        {
            java.util.logging.Logger.getLogger( PrincipalPedidosVisao.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        catch ( InstantiationException ex )
        {
            java.util.logging.Logger.getLogger( PrincipalPedidosVisao.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        catch ( IllegalAccessException ex )
        {
            java.util.logging.Logger.getLogger( PrincipalPedidosVisao.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        catch ( javax.swing.UnsupportedLookAndFeelException ex )
        {
            java.util.logging.Logger.getLogger( PrincipalPedidosVisao.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater( new Runnable()
        {
            public void run()
            {
                new PrincipalPedidosVisao( 15, "", DVML.ARMAZEM_DEFAUTL, BDConexao.getInstancia() ).setVisible( true );
            }
        } );
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_mesa_1;
    private javax.swing.JButton btn_mesa_10;
    private javax.swing.JButton btn_mesa_11;
    private javax.swing.JButton btn_mesa_12;
    private javax.swing.JButton btn_mesa_13;
    private javax.swing.JButton btn_mesa_14;
    private javax.swing.JButton btn_mesa_15;
    private javax.swing.JButton btn_mesa_16;
    private javax.swing.JButton btn_mesa_17;
    private javax.swing.JButton btn_mesa_18;
    private javax.swing.JButton btn_mesa_19;
    private javax.swing.JButton btn_mesa_2;
    private javax.swing.JButton btn_mesa_20;
    private javax.swing.JButton btn_mesa_21;
    private javax.swing.JButton btn_mesa_22;
    private javax.swing.JButton btn_mesa_23;
    private javax.swing.JButton btn_mesa_24;
    private javax.swing.JButton btn_mesa_25;
    private javax.swing.JButton btn_mesa_26;
    private javax.swing.JButton btn_mesa_27;
    private javax.swing.JButton btn_mesa_28;
    private javax.swing.JButton btn_mesa_29;
    private javax.swing.JButton btn_mesa_3;
    private javax.swing.JButton btn_mesa_30;
    private javax.swing.JButton btn_mesa_31;
    private javax.swing.JButton btn_mesa_32;
    private javax.swing.JButton btn_mesa_33;
    private javax.swing.JButton btn_mesa_34;
    private javax.swing.JButton btn_mesa_35;
    private javax.swing.JButton btn_mesa_36;
    private javax.swing.JButton btn_mesa_37;
    private javax.swing.JButton btn_mesa_38;
    private javax.swing.JButton btn_mesa_39;
    private javax.swing.JButton btn_mesa_4;
    private javax.swing.JButton btn_mesa_40;
    private javax.swing.JButton btn_mesa_5;
    private javax.swing.JButton btn_mesa_6;
    private javax.swing.JButton btn_mesa_7;
    private javax.swing.JButton btn_mesa_8;
    private javax.swing.JButton btn_mesa_9;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox<String> cmbArmazem;
    private javax.swing.JButton jButton17;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    public static javax.swing.JLabel lb_mesa_01;
    public static javax.swing.JLabel lb_mesa_02;
    public static javax.swing.JLabel lb_mesa_03;
    public static javax.swing.JLabel lb_mesa_04;
    public static javax.swing.JLabel lb_mesa_05;
    public static javax.swing.JLabel lb_mesa_06;
    public static javax.swing.JLabel lb_mesa_07;
    public static javax.swing.JLabel lb_mesa_08;
    public static javax.swing.JLabel lb_mesa_09;
    public static javax.swing.JLabel lb_mesa_10;
    public static javax.swing.JLabel lb_mesa_11;
    public static javax.swing.JLabel lb_mesa_12;
    public static javax.swing.JLabel lb_mesa_13;
    public static javax.swing.JLabel lb_mesa_14;
    public static javax.swing.JLabel lb_mesa_15;
    public static javax.swing.JLabel lb_mesa_16;
    public static javax.swing.JLabel lb_mesa_17;
    public static javax.swing.JLabel lb_mesa_18;
    public static javax.swing.JLabel lb_mesa_19;
    public static javax.swing.JLabel lb_mesa_20;
    public static javax.swing.JLabel lb_mesa_21;
    public static javax.swing.JLabel lb_mesa_22;
    public static javax.swing.JLabel lb_mesa_23;
    public static javax.swing.JLabel lb_mesa_24;
    public static javax.swing.JLabel lb_mesa_25;
    public static javax.swing.JLabel lb_mesa_26;
    public static javax.swing.JLabel lb_mesa_27;
    public static javax.swing.JLabel lb_mesa_28;
    public static javax.swing.JLabel lb_mesa_29;
    public static javax.swing.JLabel lb_mesa_30;
    public static javax.swing.JLabel lb_mesa_31;
    public static javax.swing.JLabel lb_mesa_32;
    public static javax.swing.JLabel lb_mesa_33;
    public static javax.swing.JLabel lb_mesa_34;
    public static javax.swing.JLabel lb_mesa_35;
    public static javax.swing.JLabel lb_mesa_36;
    public static javax.swing.JLabel lb_mesa_37;
    public static javax.swing.JLabel lb_mesa_38;
    public static javax.swing.JLabel lb_mesa_39;
    public static javax.swing.JLabel lb_mesa_40;
    private static javax.swing.JRadioButton rbArmazem;
    private static javax.swing.JRadioButton rbArmazem1;
    public static javax.swing.JLabel status_mensagem_primaria;
    public static javax.swing.JLabel status_mensagem_secundaria;
    // End of variables declaration//GEN-END:variables

//    public static void pintar_mesas(JLabel label_mesa, String mesa) {
//
//        //0 - oucupado - amarelo, 1 - livre - verde 
//        try {
//            System.out.println("QTD lugares oucupados: " + itemPedidosDao.getQtdItensByIdPedido(pedidoDao.getLastPedidoByMesa(mesa)));
//
//            long numero_lugares_oucupado = itemPedidosDao.getQtdItensByIdPedido(pedidoDao.getLastPedidoByMesa(mesa));
//
//            //Mesa Oucupada
//            if (numero_lugares_oucupado > 0) {
//                label_mesa.setBackground(new Color(255, 153, 51));
//                label_mesa.setOpaque(true);
//            }
//        } catch (Exception e) {
//        }
//
//    }
    /**
     * Antes pintava um rótulo por mesa (duas consultas por mesa); agora o
     * plano lê todas as mesas de uma vez, por isso basta pedir a actualização.
     */
    public static void pintar_mesas( JLabel label_mesa, String mesa )
    {
        pedirActualizacao();
    }

    public static void mesas_livres( JLabel label_mesa )
    {
        // mantido para o código antigo: o estado vem de pintar_mesas / procedimento_mesas_livre
    }

    /** Volta a ler o estado de todas as mesas, se o plano estiver aberto. */
    public static void procedimento_mesas_livre()
    {
        pedirActualizacao();
    }

    private void abertura_primeiro_turno()
    {

        this.turno = new Turno();
        this.turno.setData( new Date() );
        this.turno.setHoraAbertura( new Date() );
        this.turno.setHoraFecho( null );
        this.turno.setTurno( DVML.PRIMEIRO_TURNO );
        this.turno.setFkUsuario( usuarioDao.findTbUsuario( this.idUser ) );

        try
        {
            turnoDao.create( this.turno );
            controlo_turno();
            procedimento_abrir_mesas();
            JOptionPane.showMessageDialog( null, "Abertura efectuada com sucesso!..." );
        }
        catch ( Exception e )
        {

            JOptionPane.showMessageDialog( null, "Falha na abertura do 1º turno!...", "ERRO", JOptionPane.ERROR_MESSAGE );
        }

    }

    private void abertura_segundo_turno()
    {

        if ( turnoDao.existe_fecho_by_data( new Date(), DVML.PRIMEIRO_TURNO ) )
        {

            this.turno = new Turno();
            this.turno.setData( new Date() );
            this.turno.setHoraAbertura( new Date() );
            this.turno.setHoraFecho( null );
            this.turno.setTurno( DVML.SEGUNDO_TURNO );
            this.turno.setFkUsuario( usuarioDao.findTbUsuario( this.idUser ) );

            try
            {
                turnoDao.create( this.turno );
                controlo_turno();
                procedimento_abrir_mesas();
                JOptionPane.showMessageDialog( null, "Abertura efectuada com sucesso!..." );
            }
            catch ( Exception e )
            {
                JOptionPane.showMessageDialog( null, "Falha na abertura do 2º turno!...", "ERRO", JOptionPane.ERROR_MESSAGE );
            }

        }
        else
        {
            JOptionPane.showMessageDialog( null, "Impossivel abrir o 2º turno sem fechar o 1º turno!...", "ERRO", JOptionPane.ERROR_MESSAGE );
        }

    }

    private void controlo_turno()
    {

    }

    private void enable_mesas( boolean status )
    {

        btn_mesa_1.setEnabled( status );
        btn_mesa_2.setEnabled( status );
        btn_mesa_3.setEnabled( status );
        btn_mesa_4.setEnabled( status );
        btn_mesa_5.setEnabled( status );
        btn_mesa_6.setEnabled( status );
        btn_mesa_7.setEnabled( status );
        btn_mesa_8.setEnabled( status );
        btn_mesa_9.setEnabled( status );
        btn_mesa_10.setEnabled( status );
        btn_mesa_11.setEnabled( status );
        btn_mesa_12.setEnabled( status );
        btn_mesa_13.setEnabled( status );
        btn_mesa_14.setEnabled( status );
        btn_mesa_15.setEnabled( status );
        btn_mesa_16.setEnabled( status );
        btn_mesa_17.setEnabled( status );
        btn_mesa_18.setEnabled( status );
        btn_mesa_19.setEnabled( status );
        btn_mesa_20.setEnabled( status );
        btn_mesa_21.setEnabled( status );
        btn_mesa_22.setEnabled( status );
        btn_mesa_23.setEnabled( status );
        btn_mesa_24.setEnabled( status );
        btn_mesa_25.setEnabled( status );
        btn_mesa_26.setEnabled( status );
        btn_mesa_27.setEnabled( status );
        btn_mesa_28.setEnabled( status );
        btn_mesa_29.setEnabled( status );
        btn_mesa_30.setEnabled( status );
        btn_mesa_31.setEnabled( status );
        btn_mesa_32.setEnabled( status );
        btn_mesa_33.setEnabled( status );
        btn_mesa_34.setEnabled( status );
        btn_mesa_35.setEnabled( status );
        btn_mesa_36.setEnabled( status );
        btn_mesa_37.setEnabled( status );
        btn_mesa_38.setEnabled( status );
        btn_mesa_39.setEnabled( status );
        btn_mesa_40.setEnabled( status );
//        btn_vendas_pontuais.setEnabled(status);

    }

    private void procedimento_abrir_mesas()
    {

        if ( turnoDao.existe_abertura_by_data( new Date(), DVML.PRIMEIRO_TURNO ) && !turnoDao.existe_fecho_by_data( new Date(), DVML.PRIMEIRO_TURNO ) )
        {
            enable_mesas( true );
        }
        else if ( turnoDao.existe_fecho_by_data( new Date(), DVML.PRIMEIRO_TURNO )
                && turnoDao.existe_abertura_by_data( new Date(), DVML.SEGUNDO_TURNO )
                && !turnoDao.existe_fecho_by_data( new Date(), DVML.SEGUNDO_TURNO ) )
        {
            enable_mesas( true );
        }
        else
        {
            enable_mesas( false );
        }

    }

    @Override
    public void run()
    {

        while ( true )
        {

//            try {
//                Thread.sleep(6000);
//                procedimento_mesas_livre();
//                System.out.println("cheguei aqui dallas");
//            } catch (InterruptedException ex) {
//                Logger.getLogger(PrincipalPedidosVisao.class.getName()).log(Level.SEVERE, null, ex);
//            }
        }

    }

    private void mostrar_armazem()
    {
        TbArmazem armazem = armazemDao.findTbArmazem( id_armazem );

        if ( !Objects.isNull( armazem ) )
        {
            cmbArmazem.setSelectedItem( armazem.getDesignacao() );
        }
    }

    public static void fazerBackupAgora()
    {
        String data = new SimpleDateFormat( YYYYMMDD_HHMMSS ).format( new Date() );
//        String rodar_camando = "cmd /c mysqldump -uroot -pDoV90x?# --dump-date --triggers --tables --routines --skip-quote-names --compact --skip-opt --skip-set-charset --hex-blob kitanda_db > \"..\\BD_BACKUP\\_database_backup_" + data + ".sql\"";
        String rodar_camando = "cmd /c mysqldump --single-transaction -uroot -pDoV90x?# --dump-date --triggers --add-drop-database --routines --skip-quote-names --skip-set-charset --add-locks --disable-keys --databases kitanda_db > \"..\\BD_BACKUP\\_database_backup_" + data + ".sql\"";
//String rodar_camando = "cmd /c mysqldump --single-transaction=TRUE -uroot -pDoV90x?# --dump-date --triggers --add-drop-database  --routines --skip-quote-names --compact --skip-opt --skip-set-charset --hex-blob --add-locks --disable-keys --lock-tables  --databases kitanda_db > \"..\\BD_BACKUP\\_database_backup_" + data + ".sql\"";
        Process rodarComandoWindows = rodarComandoWindows( rodar_camando, true );

//        JOptionPane.showMessageDialog ( null, "Backup realizado com sucesso! ", "Notificação", JOptionPane.INFORMATION_MESSAGE );
        System.err.println( "Backup realizado com sucesso! " );

    }

}
