/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package visao;


import java.sql.Connection;
import dao.FornecedorDao;
import dao.RegimeDao;
import entity.Compras;
import entity.ExtratoContaFornecedor;
import entity.ItemCompras;
import entity.Regime;
import entity.TbFornecedor;
import comercial.controller.ExtratoContaFornecedorController;
import comercial.controller.ComprasController;
import comercial.controller.FornecedoresController;
import comercial.controller.ItemComprasController;
import comercial.controller.RegimesController;
import java.awt.Color;
import java.awt.Frame;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.EntityManagerFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import kitanda.util.CfMethods;
import tesouraria.novo.visao.SaidaVisao;
import util.BDConexao;
import util.DVML;
import util.JPAEntityMannagerFactoryUtil;
import util.MetodosUtil;

/**
 *
 * @author Domingos Dala Vunge
 */
public class FornecedorOutroVisao extends javax.swing.JFrame
{
    private static EntityManagerFactory emf = JPAEntityMannagerFactoryUtil.em;
    private static FornecedorDao fornecedorDao = new FornecedorDao(emf );
    private static FornecedoresController fornecedoresController;
    private static ExtratoContaFornecedorController ecfController;
    private static ComprasController comprasController;
    private static ItemComprasController itemComprasController;
    private static RegimesController regimesController;
    private static Regime regime;
    private static TbFornecedor fornecedor;
    private Frame parent;
    private static int codigo = 0;
    private BDConexao conexao;
    private int idUser;

    public FornecedorOutroVisao( java.awt.Frame parent, int idUser, boolean modal, BDConexao conexao )
    {
        //this.parent =  parent1;

//        super( parent, modal );
        this.parent = parent;
        this.conexao = conexao;
        this.idUser = idUser;
        comprasController = new ComprasController( conexao );
        fornecedoresController = new FornecedoresController( conexao );
        regimesController = new RegimesController( conexao );
        itemComprasController = new ItemComprasController( conexao );
        ecfController = new ExtratoContaFornecedorController( conexao );
        initComponents();
        confLabel();
        setLocationRelativeTo( null );
        dcDataInicial.setDate( new Date() );
        dcDataFinal.setDate( new Date() );
        set_combos();
        txtInicias.addKeyListener( new TratarEvento() );
        adicionar();
        // gestão de fornecedores (design do SysKitandaHotel), sobre o initComponents
        util.ui.LayoutSeguro.aplicar( this, "Gestão de fornecedores", this::montarLayoutWeb );

    }


    // =====================================================================
    // GESTÃO DE FORNECEDORES (design do SysKitandaHotel, responsivo)
    // Ficha, compras, dívidas em aberto com antiguidade, maiores credores e
    // pagamentos. O pagamento usa a Saída de tesouraria que já existia
    // (SaidaVisao): sai da conta, debita o extrato e actualiza a compra.
    // As leituras correm em segundo plano para o ecrã não prender.
    // =====================================================================
    private comercial.controller.GestaoFornecedoresController gestao;
    private final util.ui.ComponentesWeb.CartaoKpi kpiFornecedores = new util.ui.ComponentesWeb.CartaoKpi(
            util.ui.ComponentesWeb.CartaoKpi.PESSOA, "Fornecedores", util.ui.ComponentesWeb.AZUL_MEDIO );
    private final util.ui.ComponentesWeb.CartaoKpi kpiDivida = new util.ui.ComponentesWeb.CartaoKpi(
            "Kz", "Total em dívida", util.ui.ComponentesWeb.LARANJA );
    private final util.ui.ComponentesWeb.CartaoKpi kpiVencido = new util.ui.ComponentesWeb.CartaoKpi(
            "!", "Vencido", util.ui.ComponentesWeb.VERMELHO );
    private final util.ui.ComponentesWeb.CartaoKpi kpiPago = new util.ui.ComponentesWeb.CartaoKpi(
            util.ui.ComponentesWeb.CartaoKpi.VISTO, "Pago (30 dias)", util.ui.ComponentesWeb.VERDE );
    private final javax.swing.JTable tabelaDividas = new javax.swing.JTable();
    private final javax.swing.JTable tabelaCredores = new javax.swing.JTable();
    private final java.util.List<comercial.controller.GestaoFornecedoresController.Divida> dividasMostradas = new java.util.ArrayList<>();
    private final java.util.List<comercial.controller.GestaoFornecedoresController.DividaFornecedor> credoresMostrados = new java.util.ArrayList<>();
    private final BarrasAntiguidade antiguidade = new BarrasAntiguidade();
    private final javax.swing.JLabel lbFornecedorEscolhido = new javax.swing.JLabel( "Escolha um fornecedor na lista" );
    private final javax.swing.JLabel lbActividade = util.ui.FormularioWeb.dica( " " );
    private final javax.swing.JLabel lbTotalDividas = util.ui.FormularioWeb.dica( " " );
    private util.ui.ComponentesWeb.Cartao cartaoDividas;
    private boolean dividasGlobais = true;

    private void montarLayoutWeb()
    {
        gestao = new comercial.controller.GestaoFornecedoresController( conexao );

        // ---------------------------------------------------------- cabeçalho
        jButton3.setText( "Nova compra" );
        util.ui.ComponentesWeb.botao( jButton3, util.ui.ComponentesWeb.Variante.SUCESSO, null,
                "Emitir uma compra (entrada de stock) a um fornecedor" );
        pilulaWeb( jButton3 );
        javax.swing.JButton btActualizar = new javax.swing.JButton( "Actualizar" );
        util.ui.ComponentesWeb.botao( btActualizar, util.ui.ComponentesWeb.Variante.SECUNDARIO, null,
                "Ler de novo dívidas, compras e pagamentos (F5)" );
        pilulaWeb( btActualizar );
        btActualizar.addActionListener( e -> actualizarTudo() );
        javax.swing.JPanel direita = util.ui.ComponentesWeb.transparente( new util.ui.EstiloUI.WrapLayout( java.awt.FlowLayout.RIGHT, 8, 4 ) );
        direita.add( jButton3 );
        direita.add( btActualizar );

        // ---------------------------------------------------------- indicadores
        javax.swing.JPanel kpis = util.ui.FormularioWeb.grelha( 200, 4, kpiFornecedores, kpiDivida, kpiVencido, kpiPago );

        // ---------------------------------------------------------- fornecedores
        txtInicias.setToolTipText( "Escreva as iniciais do nome: a lista filtra-se sozinha" );
        util.ui.FormularioWeb.campoTexto( txtInicias );
        estiloTabela( tabela_fornecedor );
        jScrollPane1.setPreferredSize( new java.awt.Dimension( 300, 260 ) );
        tabela_fornecedor.addMouseListener( new java.awt.event.MouseAdapter()
        {
            @Override
            public void mouseClicked( java.awt.event.MouseEvent e )
            {
                // depois do evento antigo (que preenche a ficha e as tabelas)
                javax.swing.SwingUtilities.invokeLater( () -> fornecedorEscolhido() );
            }
        } );
        util.ui.ComponentesWeb.Cartao cartaoLista = util.ui.FormularioWeb.cartao( "Fornecedores", util.ui.ComponentesWeb.AZUL_VIVO,
                util.ui.FormularioWeb.pilha( 8, util.ui.ComponentesWeb.campo( "Pesquisar pelas iniciais", txtInicias ), jScrollPane1 ) );

        // maiores credores (todos os fornecedores com dívida)
        tabelaCredores.setModel( new javax.swing.table.DefaultTableModel(
                new Object[]{ "Fornecedor", "Docs", "Em dívida", "Vencido" }, 0 )
        {
            @Override
            public boolean isCellEditable( int r, int c )
            {
                return false;
            }
        } );
        estiloTabela( tabelaCredores );
        alinharDireita( tabelaCredores, 1, 2, 3 );
        tabelaCredores.getColumnModel().getColumn( 0 ).setPreferredWidth( 180 );
        tabelaCredores.addMouseListener( new java.awt.event.MouseAdapter()
        {
            @Override
            public void mouseClicked( java.awt.event.MouseEvent e )
            {
                int r = tabelaCredores.getSelectedRow();
                if ( r >= 0 && r < credoresMostrados.size() )
                {
                    escolherFornecedor( credoresMostrados.get( r ).fornecedorId );
                }
            }
        } );
        javax.swing.JScrollPane spCredores = rolagemTabela( tabelaCredores, 170 );
        util.ui.ComponentesWeb.Cartao cartaoCredores = util.ui.FormularioWeb.cartao( "Maiores credores", util.ui.ComponentesWeb.LARANJA, spCredores );
        cartaoCredores.adicionarAccao( new util.ui.ComponentesWeb.Etiqueta( "clique para abrir", util.ui.ComponentesWeb.LARANJA ) );

        javax.swing.JPanel esquerda = util.ui.FormularioWeb.pilha( 10, cartaoLista, cartaoCredores );

        // ---------------------------------------------------------- ficha
        lbTipoProduto.setText( "Nome *" );
        jLabel1.setText( "Endereço" );
        jLabel2.setText( "Telefone" );
        jLabel3.setText( "Email" );
        lbTipoProduto2.setText( "NIF *" );
        jLabel4.setText( "Regime" );
        for ( javax.swing.JLabel l : new javax.swing.JLabel[]
        {
            lbTipoProduto, jLabel1, jLabel2, jLabel3, lbTipoProduto2, jLabel4
        } )
        {
            l.setHorizontalAlignment( javax.swing.SwingConstants.LEFT );
        }
        util.ui.FormularioWeb.campoTexto( txtNome, txtMorada, txtTelefone, txtEmail, txtNif );
        util.ui.FormularioWeb.combo( cmbRegime );
        lbFornecedorEscolhido.setFont( util.ui.EstiloUI.FONTE_NEGRITO.deriveFont( 15f ) );
        lbFornecedorEscolhido.setForeground( util.ui.MenuWeb.AZUL_NOITE );
        jLabel5.setText( "Saldo no extrato" );
        lbSaldoFornecedor.setFont( util.ui.MesasWeb.fonteVisor( 20 ) );
        lbSaldoFornecedor.setForeground( new java.awt.Color( 134, 239, 172 ) );
        lbSaldoFornecedor.setHorizontalAlignment( javax.swing.SwingConstants.RIGHT );
        jLabel5.setFont( util.ui.EstiloUI.FONTE_NEGRITO.deriveFont( 11f ) );
        jLabel5.setForeground( new java.awt.Color( 148, 163, 184 ) );
        util.ui.ComponentesWeb.CaixaSuave visorSaldo = new util.ui.ComponentesWeb.CaixaSuave( new java.awt.BorderLayout( 10, 0 ),
                new java.awt.Color( 15, 23, 42 ), null );
        visorSaldo.setBorder( javax.swing.BorderFactory.createEmptyBorder( 8, 12, 8, 12 ) );
        visorSaldo.add( jLabel5, java.awt.BorderLayout.WEST );
        visorSaldo.add( lbSaldoFornecedor, java.awt.BorderLayout.CENTER );
        visorSaldo.setToolTipText( "Saldo da conta corrente do fornecedor (o que se lhe deve, segundo o extrato)" );

        javax.swing.JButton btNovo = new javax.swing.JButton( "Novo" );
        util.ui.ComponentesWeb.botao( btNovo, util.ui.ComponentesWeb.Variante.SECUNDARIO, null, "Limpar a ficha para cadastrar um fornecedor novo" );
        btNovo.addActionListener( e ->
        {
            limpar();
            fornecedor = null;
            codigo = 0;
            fornecedorEscolhido();
            txtNome.requestFocusInWindow();
        } );
        util.ui.FormularioWeb.botaoSalvar( btnSalvar, "Cadastrar um fornecedor novo com estes dados" );
        util.ui.FormularioWeb.botaoAlterar( btnAlterar, "Gravar as alterações do fornecedor escolhido" );
        util.ui.FormularioWeb.botaoEliminar( btnEliminar, "Eliminar o fornecedor escolhido" );
        javax.swing.JPanel botoesFicha = util.ui.ComponentesWeb.transparente( new util.ui.EstiloUI.WrapLayout( java.awt.FlowLayout.RIGHT, 6, 0 ) );
        for ( javax.swing.JButton b : new javax.swing.JButton[]
        {
            btNovo, btnSalvar, btnAlterar, btnEliminar
        } )
        {
            botoesFicha.add( b );
        }

        javax.swing.JPanel topoFicha = util.ui.ComponentesWeb.transparente( new java.awt.BorderLayout( 12, 0 ) );
        javax.swing.JPanel nomeEActividade = util.ui.ComponentesWeb.transparente( new java.awt.GridLayout( 2, 1 ) );
        nomeEActividade.add( lbFornecedorEscolhido );
        nomeEActividade.add( lbActividade );
        topoFicha.add( nomeEActividade, java.awt.BorderLayout.CENTER );
        topoFicha.add( visorSaldo, java.awt.BorderLayout.EAST );

        util.ui.ComponentesWeb.Cartao cartaoFicha = util.ui.FormularioWeb.cartao( "Ficha do fornecedor", util.ui.ComponentesWeb.VERDE,
                util.ui.FormularioWeb.pilha( 10,
                        topoFicha,
                        util.ui.FormularioWeb.grelha( 220, 3,
                                util.ui.ComponentesWeb.campo( lbTipoProduto, txtNome ),
                                util.ui.ComponentesWeb.campo( lbTipoProduto2, txtNif ),
                                util.ui.ComponentesWeb.campo( jLabel4, cmbRegime ),
                                util.ui.ComponentesWeb.campo( jLabel2, txtTelefone ),
                                util.ui.ComponentesWeb.campo( jLabel3, txtEmail ),
                                util.ui.ComponentesWeb.campo( jLabel1, txtMorada ) ),
                        botoesFicha ) );

        // ---------------------------------------------------------- dívidas em aberto
        tabelaDividas.setModel( new javax.swing.table.DefaultTableModel(
                new Object[]{ "Fornecedor", "Documento", "Data", "Vencimento", "Situação", "Total", "Pago", "Em falta" }, 0 )
        {
            @Override
            public boolean isCellEditable( int r, int c )
            {
                return false;
            }
        } );
        estiloTabela( tabelaDividas );
        alinharDireita( tabelaDividas, 5, 6, 7 );
        tabelaDividas.getColumnModel().getColumn( 4 ).setCellRenderer( new RenderSituacao() );
        int[] largurasDiv = { 160, 150, 80, 85, 120, 100, 100, 110 };
        for ( int i = 0; i < largurasDiv.length; i++ )
        {
            tabelaDividas.getColumnModel().getColumn( i ).setPreferredWidth( largurasDiv[ i ] );
        }
        tabelaDividas.addMouseListener( new java.awt.event.MouseAdapter()
        {
            @Override
            public void mouseClicked( java.awt.event.MouseEvent e )
            {
                if ( e.getClickCount() >= 2 )
                {
                    pagarDividaSeleccionada();
                }
            }
        } );
        javax.swing.JButton btPagar = new javax.swing.JButton( "Pagar a seleccionada" );
        util.ui.ComponentesWeb.botao( btPagar, util.ui.ComponentesWeb.Variante.SUCESSO, null,
                "Registar o pagamento desta compra (saída da tesouraria e débito no extrato)" );
        btPagar.addActionListener( e -> pagarDividaSeleccionada() );
        javax.swing.JToggleButton btTodos = new javax.swing.JToggleButton( "Todos os fornecedores", true );
        util.ui.ComponentesWeb.pilulaCompacta( btTodos );
        btTodos.setToolTipText( "Ver as dívidas de todos os fornecedores ou só do escolhido" );
        btTodos.addActionListener( e ->
        {
            dividasGlobais = btTodos.isSelected();
            carregarDividas();
        } );
        javax.swing.JPanel barraDividas = util.ui.ComponentesWeb.transparente( new java.awt.BorderLayout( 10, 0 ) );
        barraDividas.add( lbTotalDividas, java.awt.BorderLayout.CENTER );
        javax.swing.JPanel botoesDiv = util.ui.ComponentesWeb.transparente( new java.awt.FlowLayout( java.awt.FlowLayout.RIGHT, 6, 0 ) );
        botoesDiv.add( btTodos );
        botoesDiv.add( btPagar );
        barraDividas.add( botoesDiv, java.awt.BorderLayout.EAST );
        javax.swing.JPanel painelDividas = util.ui.ComponentesWeb.transparente( new java.awt.BorderLayout( 0, 8 ) );
        painelDividas.setBorder( javax.swing.BorderFactory.createEmptyBorder( 8, 8, 8, 8 ) );
        painelDividas.add( antiguidade, java.awt.BorderLayout.NORTH );
        painelDividas.add( rolagemTabela( tabelaDividas, 220 ), java.awt.BorderLayout.CENTER );
        painelDividas.add( barraDividas, java.awt.BorderLayout.SOUTH );

        // ---------------------------------------------------------- compras / conta corrente / encomendas
        estiloTabela( tabela_compras );
        alinharDireita( tabela_compras, 4, 5, 6 );
        tabela_compras.setToolTipText( "Duplo clique numa compra com valor pendente para a pagar" );
        jPanel5.removeAll();
        jPanel5.setOpaque( false );
        jPanel5.setLayout( new java.awt.BorderLayout( 0, 8 ) );
        jPanel5.setBorder( javax.swing.BorderFactory.createEmptyBorder( 8, 8, 8, 8 ) );
        jPanel5.add( jScrollPane4, java.awt.BorderLayout.CENTER );
        jPanel5.add( util.ui.FormularioWeb.dica( "Duplo clique numa compra com valor pendente para registar o pagamento" ),
                java.awt.BorderLayout.SOUTH );

        estiloTabela( tabela_conta_corrente );
        alinharDireita( tabela_conta_corrente, 2, 3, 4 );
        util.ui.FormularioWeb.data( dcDataInicial );
        util.ui.FormularioWeb.data( dcDataFinal );
        jButton1.setText( "Analisar" );
        util.ui.ComponentesWeb.botao( jButton1, util.ui.ComponentesWeb.Variante.PRIMARIO, null, "Mostrar o extrato entre as datas" );
        javax.swing.JPanel filtroConta = util.ui.ComponentesWeb.transparente( new util.ui.EstiloUI.WrapLayout( java.awt.FlowLayout.LEFT, 8, 0 ) );
        dcDataInicial.setPreferredSize( new java.awt.Dimension( 140, util.ui.FormularioWeb.ALTURA_CAMPO ) );
        dcDataFinal.setPreferredSize( new java.awt.Dimension( 140, util.ui.FormularioWeb.ALTURA_CAMPO ) );
        filtroConta.add( util.ui.ComponentesWeb.campo( "Data inicial", dcDataInicial ) );
        filtroConta.add( util.ui.ComponentesWeb.campo( "Data final", dcDataFinal ) );
        javax.swing.JPanel rodapeBotao = util.ui.ComponentesWeb.transparente( new java.awt.BorderLayout() );
        rodapeBotao.add( jButton1, java.awt.BorderLayout.SOUTH );
        filtroConta.add( rodapeBotao );
        jPanel6.removeAll();
        jPanel6.setOpaque( false );
        jPanel6.setLayout( new java.awt.BorderLayout( 0, 8 ) );
        jPanel6.setBorder( javax.swing.BorderFactory.createEmptyBorder( 8, 8, 8, 8 ) );
        jPanel6.add( filtroConta, java.awt.BorderLayout.NORTH );
        jPanel6.add( jScrollPane3, java.awt.BorderLayout.CENTER );

        estiloTabela( tabela_encomenda );
        jButton2.setText( "Nova encomenda" );
        util.ui.ComponentesWeb.botao( jButton2, util.ui.ComponentesWeb.Variante.SECUNDARIO, null,
                "Encomendar ao fornecedor escolhido" );
        javax.swing.JPanel barraEnc = util.ui.ComponentesWeb.transparente( new java.awt.FlowLayout( java.awt.FlowLayout.RIGHT, 0, 0 ) );
        barraEnc.add( jButton2 );
        jPanel7.removeAll();
        jPanel7.setOpaque( false );
        jPanel7.setLayout( new java.awt.BorderLayout( 0, 8 ) );
        jPanel7.setBorder( javax.swing.BorderFactory.createEmptyBorder( 8, 8, 8, 8 ) );
        jPanel7.add( jScrollPane2, java.awt.BorderLayout.CENTER );
        jPanel7.add( barraEnc, java.awt.BorderLayout.SOUTH );

        jTabbedPane1.insertTab( "Dívidas em aberto", null, painelDividas, "Compras a crédito por pagar", 0 );
        jTabbedPane1.setSelectedIndex( 0 );
        jTabbedPane1.setFont( util.ui.EstiloUI.FONTE_NEGRITO.deriveFont( 13f ) );
        jTabbedPane1.setForeground( util.ui.ComponentesWeb.AZUL );
        jTabbedPane1.setPreferredSize( new java.awt.Dimension( 600, 380 ) );
        cartaoDividas = util.ui.FormularioWeb.cartao( "Dívidas, compras e conta corrente", util.ui.ComponentesWeb.ROXO, jTabbedPane1 );

        javax.swing.JPanel direitaCol = util.ui.FormularioWeb.pilha( 10, cartaoFicha, cartaoDividas );

        javax.swing.JPanel colunas = util.ui.ComponentesWeb.transparente( new util.ui.ComponentesWeb.ColunasResponsivas(
                new double[]{ 0.36, 0.64 }, new int[]{ 320, 480 }, 10 ) );
        colunas.add( esquerda );
        colunas.add( direitaCol );

        javax.swing.JPanel conteudo = util.ui.ComponentesWeb.transparente( new java.awt.BorderLayout( 0, 10 ) );
        conteudo.add( kpis, java.awt.BorderLayout.NORTH );
        conteudo.add( colunas, java.awt.BorderLayout.CENTER );

        util.ui.FormularioWeb.botaoSair( btnCancelar, "Fechar a gestão de fornecedores" );
        util.ui.FormularioWeb.montar( this,
                util.ui.FormularioWeb.cabecalho( "Gestão de Fornecedores", "Compras, dívidas e pagamentos aos fornecedores", direita ),
                conteudo,
                util.ui.FormularioWeb.barraAccoes( util.ui.FormularioWeb.dica( "Duplo clique numa dívida para a pagar  ·  F5 actualiza" ),
                        btnCancelar ),
                "Kitanda  ·  Gestão de Fornecedores" );
        getRootPane().registerKeyboardAction( e -> actualizarTudo(),
                javax.swing.KeyStroke.getKeyStroke( java.awt.event.KeyEvent.VK_F5, 0 ), javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW );
        // ao voltar de uma compra ou de um pagamento, os números ficam certos
        addWindowListener( new java.awt.event.WindowAdapter()
        {
            @Override
            public void windowActivated( java.awt.event.WindowEvent e )
            {
                actualizarTudo();
            }
        } );
        util.ui.FormularioWeb.ajustar( this, 1280, 820, 860, 580 );
        fornecedorEscolhido();
        actualizarTudo();
    }

    // ---------------------------------------------------------------- leituras (em segundo plano)
    private boolean aActualizar = false;

    private void actualizarTudo()
    {
        if ( aActualizar || gestao == null )
        {
            return;
        }
        aActualizar = true;
        new javax.swing.SwingWorker<Object[], Void>()
        {
            @Override
            protected Object[] doInBackground() throws Exception
            {
                return new Object[]{ gestao.resumo(), gestao.rankingDividas() };
            }

            @Override
            @SuppressWarnings( "unchecked" )
            protected void done()
            {
                aActualizar = false;
                try
                {
                    Object[] r = get();
                    comercial.controller.GestaoFornecedoresController.Resumo s = ( comercial.controller.GestaoFornecedoresController.Resumo ) r[ 0 ];
                    kpiFornecedores.setValor( String.valueOf( s.fornecedores ), s.fornecedoresComDivida + " com dívida" );
                    kpiDivida.setValor( kitanda.util.CfMethods.formatarComoMoeda( s.emDivida ),
                            s.documentosAbertos + ( s.documentosAbertos == 1 ? " documento" : " documentos" ) + " por pagar" );
                    kpiVencido.setValor( kitanda.util.CfMethods.formatarComoMoeda( s.vencido ),
                            s.emDivida <= 0 ? "nada vencido" : Math.round( s.vencido * 100 / s.emDivida ) + "% da dívida" );
                    kpiPago.setValor( kitanda.util.CfMethods.formatarComoMoeda( s.pago30Dias ), "pagamentos registados" );
                    credoresMostrados.clear();
                    credoresMostrados.addAll( ( java.util.List<comercial.controller.GestaoFornecedoresController.DividaFornecedor> ) r[ 1 ] );
                    javax.swing.table.DefaultTableModel m = ( javax.swing.table.DefaultTableModel ) tabelaCredores.getModel();
                    m.setRowCount( 0 );
                    for ( comercial.controller.GestaoFornecedoresController.DividaFornecedor f : credoresMostrados )
                    {
                        m.addRow( new Object[]{ f.fornecedor, f.documentos, kitanda.util.CfMethods.formatarComoMoeda( f.emDivida ),
                            f.vencido > 0 ? kitanda.util.CfMethods.formatarComoMoeda( f.vencido ) : "—" } );
                    }
                }
                catch ( Exception e )
                {
                    e.printStackTrace();
                    kpiDivida.setValor( "—", "não foi possível ler as dívidas" );
                }
                carregarDividas();
            }
        }.execute();
    }

    private void carregarDividas()
    {
        final int id = dividasGlobais || fornecedor == null ? 0 : fornecedor.getCodigo();
        new javax.swing.SwingWorker<java.util.List<comercial.controller.GestaoFornecedoresController.Divida>, Void>()
        {
            @Override
            protected java.util.List<comercial.controller.GestaoFornecedoresController.Divida> doInBackground() throws Exception
            {
                return gestao.dividas( id );
            }

            @Override
            protected void done()
            {
                try
                {
                    mostrarDividas( get(), id );
                }
                catch ( Exception e )
                {
                    e.printStackTrace();
                    lbTotalDividas.setText( "Não foi possível ler as dívidas" );
                }
            }
        }.execute();
    }

    private void mostrarDividas( java.util.List<comercial.controller.GestaoFornecedoresController.Divida> lista, int id )
    {
        dividasMostradas.clear();
        dividasMostradas.addAll( lista );
        javax.swing.table.DefaultTableModel m = ( javax.swing.table.DefaultTableModel ) tabelaDividas.getModel();
        m.setRowCount( 0 );
        java.text.SimpleDateFormat f = new java.text.SimpleDateFormat( "dd/MM/yyyy" );
        double total = 0;
        for ( comercial.controller.GestaoFornecedoresController.Divida d : lista )
        {
            total += d.restante;
            long dias = d.diasAtraso();
            String situacao = d.vencimento == null ? ( dias <= 0 ? "Sem vencimento" : "Há " + dias + " dias" )
                    : dias > 0 ? "Vencida há " + dias + ( dias == 1 ? " dia" : " dias" )
                    : dias == 0 ? "Vence hoje" : "Vence em " + ( -dias ) + ( dias == -1 ? " dia" : " dias" );
            m.addRow( new Object[]{ d.fornecedor, d.documento.isEmpty() ? d.codFact : d.documento,
                d.data == null ? "" : f.format( d.data ), d.vencimento == null ? "—" : f.format( d.vencimento ), situacao,
                kitanda.util.CfMethods.formatarComoMoeda( d.total ), kitanda.util.CfMethods.formatarComoMoeda( d.pago ),
                kitanda.util.CfMethods.formatarComoMoeda( d.restante ) } );
        }
        antiguidade.setValores( comercial.controller.GestaoFornecedoresController.antiguidade( lista ) );
        String quem = id == 0 ? "todos os fornecedores" : fornecedor == null ? "" : fornecedor.getNome();
        lbTotalDividas.setText( "<html>" + lista.size() + ( lista.size() == 1 ? " documento" : " documentos" ) + " por pagar · <b>"
                + kitanda.util.CfMethods.formatarComoMoeda( total ) + "</b> · " + quem + "</html>" );
    }

    /** Depois do evento antigo da lista: nome, actividade e dívidas do fornecedor escolhido. */
    private void fornecedorEscolhido()
    {
        if ( fornecedor == null )
        {
            lbFornecedorEscolhido.setText( "Novo fornecedor" );
            lbActividade.setText( "Preencha a ficha e carregue em Salvar" );
            if ( !dividasGlobais )
            {
                carregarDividas();
            }
            return;
        }
        lbFornecedorEscolhido.setText( fornecedor.getNome() );
        final int id = fornecedor.getCodigo();
        new javax.swing.SwingWorker<Object[], Void>()
        {
            @Override
            protected Object[] doInBackground() throws Exception
            {
                return gestao.actividade( id );
            }

            @Override
            protected void done()
            {
                try
                {
                    Object[] a = get();
                    int n = ( Integer ) a[ 1 ];
                    java.util.Date ultima = ( java.util.Date ) a[ 0 ];
                    lbActividade.setText( n + ( n == 1 ? " compra" : " compras" )
                            + ( ultima == null ? "" : " · última em " + new java.text.SimpleDateFormat( "dd/MM/yyyy" ).format( ultima ) ) );
                }
                catch ( Exception e )
                {
                    lbActividade.setText( " " );
                }
            }
        }.execute();
        if ( !dividasGlobais )
        {
            carregarDividas();
        }
    }

    /** Escolhe o fornecedor na lista (a partir dos credores) como se fosse clicado. */
    private void escolherFornecedor( int idFornecedor )
    {
        javax.swing.table.DefaultTableModel m = ( javax.swing.table.DefaultTableModel ) tabela_fornecedor.getModel();
        for ( int vez = 0; vez < 2; vez++ )
        {
            for ( int r = 0; r < m.getRowCount(); r++ )
            {
                if ( String.valueOf( idFornecedor ).equals( String.valueOf( m.getValueAt( r, 0 ) ) ) )
                {
                    int v = tabela_fornecedor.convertRowIndexToView( r );
                    tabela_fornecedor.setRowSelectionInterval( v, v );
                    tabela_fornecedor.scrollRectToVisible( tabela_fornecedor.getCellRect( v, 0, true ) );
                    mostar_dados();
                    adicionar_encomendas();
                    mostrarSaldoFornecedor();
                    mostrarContaCorrenteFornecedor();
                    adicionar_compras();
                    fornecedorEscolhido();
                    return;
                }
            }
            txtInicias.setText( "" );
            adicionar(); // a pesquisa escondia-o: volta a lista toda
        }
    }

    /** Paga a dívida seleccionada pela Saída de tesouraria (o caminho que já existia). */
    private void pagarDividaSeleccionada()
    {
        int r = tabelaDividas.getSelectedRow();
        if ( r < 0 || r >= dividasMostradas.size() )
        {
            JOptionPane.showMessageDialog( this, "Seleccione na tabela a compra que quer pagar.", "Pagamento a fornecedor",
                    JOptionPane.INFORMATION_MESSAGE );
            return;
        }
        comercial.controller.GestaoFornecedoresController.Divida d = dividasMostradas.get( tabelaDividas.convertRowIndexToModel( r ) );
        try
        {
            gestao.prepararParaPagamento( d.pkCompra );
            Compras compraLocal = comprasController.findByCodFact( d.codFact );
            if ( compraLocal == null )
            {
                JOptionPane.showMessageDialog( this, "Não foi possível abrir a compra " + d.codFact + ".", "Pagamento a fornecedor",
                        JOptionPane.WARNING_MESSAGE );
                return;
            }
            SaidaVisao saida = new SaidaVisao( compraLocal, d.fornecedorId, d.documento, d.restante, BDConexao.getInstancia() );
            saida.addWindowListener( new java.awt.event.WindowAdapter()
            {
                @Override
                public void windowClosed( java.awt.event.WindowEvent e )
                {
                    actualizarTudo();
                }
            } );
            saida.setVisible( true );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            JOptionPane.showMessageDialog( this, "Não foi possível preparar o pagamento:\n" + e.getMessage(),
                    "Pagamento a fornecedor", JOptionPane.ERROR_MESSAGE );
        }
    }

    // ---------------------------------------------------------------- aspecto
    private static void pilulaWeb( javax.swing.JButton b )
    {
        b.putClientProperty( "web.pilula", Boolean.TRUE );
        b.setBorder( javax.swing.BorderFactory.createEmptyBorder( 6, 14, 6, 16 ) );
        b.setFont( util.ui.EstiloUI.FONTE_NEGRITO.deriveFont( 12.5f ) );
    }

    private static void estiloTabela( javax.swing.JTable t )
    {
        util.ui.EstiloUI.estilizarTabela( t, true );
        t.setRowHeight( 28 );
    }

    private static void alinharDireita( javax.swing.JTable t, int... colunas )
    {
        util.ui.EstiloUI.ZebraRenderer direita = new util.ui.EstiloUI.ZebraRenderer( javax.swing.SwingConstants.RIGHT );
        for ( int c : colunas )
        {
            if ( c < t.getColumnCount() )
            {
                t.getColumnModel().getColumn( c ).setCellRenderer( direita );
            }
        }
    }

    private static javax.swing.JScrollPane rolagemTabela( javax.swing.JTable t, int altura )
    {
        javax.swing.JScrollPane sp = new javax.swing.JScrollPane( t );
        sp.setBorder( new javax.swing.border.LineBorder( util.ui.ComponentesWeb.LINHA, 1, true ) );
        sp.getViewport().setBackground( java.awt.Color.WHITE );
        sp.setPreferredSize( new java.awt.Dimension( 300, altura ) );
        return sp;
    }

    /** "Vencida" a vermelho, "Vence hoje/em" a laranja, o resto neutro. */
    private static final class RenderSituacao extends util.ui.EstiloUI.ZebraRenderer
    {

        RenderSituacao()
        {
            super( javax.swing.SwingConstants.LEFT );
        }

        @Override
        public java.awt.Component getTableCellRendererComponent( javax.swing.JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column )
        {
            java.awt.Component c = super.getTableCellRendererComponent( table, value, isSelected, hasFocus, row, column );
            String s = String.valueOf( value );
            if ( !isSelected )
            {
                c.setForeground( s.startsWith( "Vencida" ) ? util.ui.ComponentesWeb.VERMELHO
                        : s.startsWith( "Vence" ) ? util.ui.ComponentesWeb.LARANJA.darker() : util.ui.ComponentesWeb.TEXTO );
            }
            c.setFont( s.startsWith( "Vencida" ) ? util.ui.EstiloUI.FONTE_NEGRITO : util.ui.EstiloUI.FONTE );
            return c;
        }
    }

    /** Antiguidade da dívida: barras a vencer / 0-30 / 31-60 / 61-90 / +90 dias. */
    private static final class BarrasAntiguidade extends javax.swing.JComponent
    {

        private static final String[] NOMES = { "A vencer", "1-30 dias", "31-60 dias", "61-90 dias", "+90 dias" };
        private static final java.awt.Color[] CORES = { util.ui.ComponentesWeb.VERDE, util.ui.ComponentesWeb.LARANJA,
            new java.awt.Color( 234, 88, 12 ), util.ui.ComponentesWeb.VERMELHO, new java.awt.Color( 127, 29, 29 ) };
        private double[] v = new double[ 5 ];

        BarrasAntiguidade()
        {
            setPreferredSize( new java.awt.Dimension( 300, 74 ) );
            setToolTipText( "Antiguidade da dívida pelos dias desde o vencimento" );
        }

        void setValores( double[] valores )
        {
            v = valores;
            repaint();
        }

        @Override
        protected void paintComponent( java.awt.Graphics g )
        {
            java.awt.Graphics2D g2 = util.ui.ComponentesWeb.suave( g );
            int w = getWidth(), h = getHeight();
            double total = 0;
            for ( double x : v )
            {
                total += x;
            }
            int cols = NOMES.length, gap = 8;
            int cw = ( w - gap * ( cols - 1 ) ) / cols;
            for ( int i = 0; i < cols; i++ )
            {
                int x = i * ( cw + gap );
                g2.setColor( new java.awt.Color( 248, 250, 252 ) );
                g2.fillRoundRect( x, 0, cw, h - 1, 12, 12 );
                g2.setColor( util.ui.ComponentesWeb.LINHA );
                g2.drawRoundRect( x, 0, cw, h - 1, 12, 12 );
                double p = total <= 0 ? 0 : v[ i ] / total;
                g2.setColor( CORES[ i ] );
                g2.fillRoundRect( x + 8, h - 14, Math.max( 0, ( int ) ( ( cw - 16 ) * p ) ), 6, 6, 6 );
                g2.setFont( util.ui.EstiloUI.FONTE_NEGRITO.deriveFont( 11f ) );
                g2.setColor( util.ui.ComponentesWeb.TEXTO_SUAVE );
                g2.drawString( NOMES[ i ], x + 8, 16 );
                g2.setFont( util.ui.EstiloUI.FONTE_NEGRITO.deriveFont( 14f ) );
                g2.setColor( v[ i ] > 0 ? CORES[ i ] : util.ui.ComponentesWeb.CINZA );
                String s = kitanda.util.CfMethods.formatarComoMoeda( v[ i ] );
                java.awt.FontMetrics fm = g2.getFontMetrics();
                if ( fm.stringWidth( s ) > cw - 12 )
                {
                    g2.setFont( util.ui.EstiloUI.FONTE_NEGRITO.deriveFont( 11.5f ) );
                }
                g2.drawString( s, x + 8, 40 );
            }
            g2.dispose();
        }
    }

    private void set_combos()
    {

        cmbRegime.setModel( new DefaultComboBoxModel( regimesController.getVector() ) );

    }

    public void confLabel()
    {

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

        jPanel2 = new javax.swing.JPanel();
        btnSalvar = new javax.swing.JButton();
        btnAlterar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        lbTipoProduto = new javax.swing.JLabel();
        txtNome = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        txtMorada = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtTelefone = new javax.swing.JTextField();
        txtEmail = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtNif = new javax.swing.JTextField();
        lbTipoProduto2 = new javax.swing.JLabel();
        cmbRegime = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabela_fornecedor = new javax.swing.JTable();
        txtInicias = new javax.swing.JTextField();
        lbTipoProduto1 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tabela_compras = new javax.swing.JTable();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tabela_conta_corrente = new javax.swing.JTable();
        jLabel6 = new javax.swing.JLabel();
        dcDataInicial = new com.toedter.calendar.JDateChooser();
        dcDataFinal = new com.toedter.calendar.JDateChooser();
        jLabel7 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tabela_encomenda = new javax.swing.JTable();
        jButton2 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        lbSaldoFornecedor = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("...::::: DVML-FORNECEDORES::::...");

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        btnSalvar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/salvar_16x16.png"))); // NOI18N
        btnSalvar.setText("Salvar");
        btnSalvar.setAlignmentX(0.5F);
        btnSalvar.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnSalvarActionPerformed(evt);
            }
        });

        btnAlterar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/alterar_16x16.png"))); // NOI18N
        btnAlterar.setText("Alterar");
        btnAlterar.setAlignmentX(0.5F);
        btnAlterar.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnAlterarActionPerformed(evt);
            }
        });

        btnCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/Logout 32x32.png"))); // NOI18N
        btnCancelar.setText("Sair");
        btnCancelar.setAlignmentX(0.5F);
        btnCancelar.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnCancelarActionPerformed(evt);
            }
        });

        btnEliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/eliminar_16x16.png"))); // NOI18N
        btnEliminar.setText("Eliminar");
        btnEliminar.setAlignmentX(0.5F);
        btnEliminar.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnEliminarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnCancelar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnEliminar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 174, Short.MAX_VALUE)
                    .addComponent(btnSalvar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnAlterar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnSalvar, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnAlterar, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnCancelar)
                .addContainerGap(43, Short.MAX_VALUE))
        );

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Cadastro de Fornecedor", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 12), new java.awt.Color(0, 51, 102))); // NOI18N
        jPanel1.setFont(new java.awt.Font("Showcard Gothic", 0, 24)); // NOI18N

        lbTipoProduto.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 16)); // NOI18N
        lbTipoProduto.setText("Nome:");

        txtNome.setBackground(new java.awt.Color(0, 51, 102));
        txtNome.setForeground(new java.awt.Color(255, 255, 255));
        txtNome.setCaretColor(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 16)); // NOI18N
        jLabel1.setText("Endereço:");

        txtMorada.setBackground(new java.awt.Color(0, 51, 102));
        txtMorada.setForeground(new java.awt.Color(255, 255, 255));
        txtMorada.setCaretColor(new java.awt.Color(255, 255, 255));

        jLabel2.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 16)); // NOI18N
        jLabel2.setText("Telefone:");

        txtTelefone.setBackground(new java.awt.Color(0, 51, 102));
        txtTelefone.setForeground(new java.awt.Color(255, 255, 255));
        txtTelefone.setCaretColor(new java.awt.Color(255, 255, 255));

        txtEmail.setBackground(new java.awt.Color(0, 51, 102));
        txtEmail.setForeground(new java.awt.Color(255, 255, 255));
        txtEmail.setCaretColor(new java.awt.Color(255, 255, 255));

        jLabel3.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 16)); // NOI18N
        jLabel3.setText("Email:");

        txtNif.setBackground(new java.awt.Color(0, 51, 102));
        txtNif.setForeground(new java.awt.Color(255, 255, 255));
        txtNif.setCaretColor(new java.awt.Color(255, 255, 255));

        lbTipoProduto2.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 16)); // NOI18N
        lbTipoProduto2.setText("NIF:");

        jLabel4.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 16)); // NOI18N
        jLabel4.setText("Regime:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(txtTelefone, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cmbRegime, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(112, 112, 112))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lbTipoProduto, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtNome, javax.swing.GroupLayout.PREFERRED_SIZE, 438, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtNif)
                                    .addComponent(lbTipoProduto2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel1)
                                .addComponent(txtMorada, javax.swing.GroupLayout.PREFERRED_SIZE, 628, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(98, 98, 98)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(lbTipoProduto)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(lbTipoProduto2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtNif, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtMorada, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTelefone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbRegime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(18, Short.MAX_VALUE))))
        );

        tabela_fornecedor.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "Cod", "Nome", "Morada", "Telefone"
            }
        )
        {
            boolean[] canEdit = new boolean []
            {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex)
            {
                return canEdit [columnIndex];
            }
        });
        tabela_fornecedor.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                tabela_fornecedorMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tabela_fornecedor);
        if (tabela_fornecedor.getColumnModel().getColumnCount() > 0)
        {
            tabela_fornecedor.getColumnModel().getColumn(0).setMaxWidth(50);
        }

        txtInicias.setBackground(new java.awt.Color(0, 51, 102));
        txtInicias.setForeground(new java.awt.Color(255, 255, 255));
        txtInicias.setCaretColor(new java.awt.Color(255, 255, 255));

        lbTipoProduto1.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 16)); // NOI18N
        lbTipoProduto1.setText("Iniciais nome");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(lbTipoProduto1, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtInicias, javax.swing.GroupLayout.PREFERRED_SIZE, 273, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 812, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbTipoProduto1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtInicias))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 421, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tabela_compras.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String []
            {
                "Cod.", "V/Documento", "Data", "Data Limite", "Total", "Pago", "Pendente"
            }
        )
        {
            boolean[] canEdit = new boolean []
            {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex)
            {
                return canEdit [columnIndex];
            }
        });
        tabela_compras.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                tabela_comprasMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(tabela_compras);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 771, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 606, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Compras", jPanel5);

        tabela_conta_corrente.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String []
            {
                "Data", "Documento", "Crédito", "Débito", "Saldo"
            }
        )
        {
            Class[] types = new Class []
            {
                java.lang.Object.class, java.lang.Object.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean []
            {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex)
            {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex)
            {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(tabela_conta_corrente);

        jLabel6.setText("Data Inicial");

        jLabel7.setText("Data Final");

        jButton1.setText("executar análise");
        jButton1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(dcDataInicial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(29, 29, 29)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(dcDataFinal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 77, Short.MAX_VALUE)
                        .addComponent(jButton1)))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(dcDataFinal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(dcDataInicial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 547, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Conta Corrente", jPanel6);

        tabela_encomenda.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String []
            {
                "Documento", "Data", "Accão"
            }
        )
        {
            boolean[] canEdit = new boolean []
            {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex)
            {
                return canEdit [columnIndex];
            }
        });
        tabela_encomenda.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                tabela_encomendaMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tabela_encomenda);

        jButton2.setText("+");
        jButton2.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 771, Short.MAX_VALUE)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 553, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Encomendas", jPanel7);

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel5.setText("saldo:");

        lbSaldoFornecedor.setFont(new java.awt.Font("Segoe UI", 1, 30)); // NOI18N
        lbSaldoFornecedor.setText("0.0");

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/encomenda_16x16.png"))); // NOI18N
        jButton3.setText("Compras Informais");
        jButton3.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jTabbedPane1))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(lbSaldoFornecedor, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbSaldoFornecedor, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 648, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tabela_fornecedorMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabela_fornecedorMouseClicked
        // TODO add your handling code here:
        if ( evt.getClickCount() >= 1 )
        {
            mostar_dados();
            adicionar_encomendas();
            mostrarSaldoFornecedor();
            mostrarContaCorrenteFornecedor();
            adicionar_compras();
        }

    }//GEN-LAST:event_tabela_fornecedorMouseClicked

    private void btnSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalvarActionPerformed

        procedimento_salvar();

    }//GEN-LAST:event_btnSalvarActionPerformed

    private void btnAlterarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAlterarActionPerformed
        procedimento_alterar();

    }//GEN-LAST:event_btnAlterarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        // TODO add your handling code here:
        procedimento_eliminar();

    }//GEN-LAST:event_btnEliminarActionPerformed

    private void tabela_encomendaMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_tabela_encomendaMouseClicked
    {//GEN-HEADEREND:event_tabela_encomendaMouseClicked
        // TODO add your handling code here:
        if ( evt.getClickCount() > 1 )
        {

            DefaultTableModel modelo = ( DefaultTableModel ) tabela_encomenda.getModel();
            String codFact = modelo.getValueAt( tabela_encomenda.getSelectedRow(), 0 ).toString();
            List<ItemCompras> listaItens = itemComprasController.getAllItensByCodFact( codFact );

            new ConverterEncomendasCompraVisao( idUser, conexao, codFact, listaItens ).setVisible( true );

        }
    }//GEN-LAST:event_tabela_encomendaMouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton1ActionPerformed
    {//GEN-HEADEREND:event_jButton1ActionPerformed
        // TODO add your handling code here:
        mostrarContaCorrenteFornecedor();
        mostrarSaldoFornecedor();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void tabela_comprasMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_tabela_comprasMouseClicked
    {//GEN-HEADEREND:event_tabela_comprasMouseClicked
        // TODO add your handling code here:

        if ( evt.getClickCount() >= 2 )
        {
            DefaultTableModel modelo = ( DefaultTableModel ) tabela_compras.getModel();
            String valorPorPagar = modelo.getValueAt( tabela_compras.getSelectedRow(), 6 ).toString();
            String documento = modelo.getValueAt( tabela_compras.getSelectedRow(), 1 ).toString();
            String cod = modelo.getValueAt( tabela_compras.getSelectedRow(), 0 ).toString();

            Compras compraLocal = comprasController.findByCodFact( cod );
            int idFornecedor = compraLocal.getFkFornecedor().getCodigo();

            new SaidaVisao( compraLocal, idFornecedor, documento, Double.parseDouble( valorPorPagar ), BDConexao.getInstancia()).setVisible(true);

        }


    }//GEN-LAST:event_tabela_comprasMouseClicked

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton2ActionPerformed
    {//GEN-HEADEREND:event_jButton2ActionPerformed
        // TODO add your handling code here:

        new NovaEncomendaVisao( fornecedor, idUser, BDConexao.getInstancia()).setVisible(true);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton3ActionPerformed
    {//GEN-HEADEREND:event_jButton3ActionPerformed
        try
        {

            new CompraInformalVisao(idUser, BDConexao.getInstancia()).setVisible(true);

        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButton3ActionPerformed

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
            java.util.logging.Logger.getLogger( FornecedorOutroVisao.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        catch ( InstantiationException ex )
        {
            java.util.logging.Logger.getLogger( FornecedorOutroVisao.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        catch ( IllegalAccessException ex )
        {
            java.util.logging.Logger.getLogger( FornecedorOutroVisao.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        catch ( javax.swing.UnsupportedLookAndFeelException ex )
        {
            java.util.logging.Logger.getLogger( FornecedorOutroVisao.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }


        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater( new Runnable()
        {
            public void run()
            {
                FornecedorOutroVisao dialog = new FornecedorOutroVisao( new javax.swing.JFrame(), 15, true, BDConexao.getInstancia() );
                dialog.addWindowListener( new java.awt.event.WindowAdapter()
                {
                    @Override
                    public void windowClosing( java.awt.event.WindowEvent e )
                    {
                        System.exit( 0 );
                    }
                } );
                dialog.setVisible( true );
            }
        } );
    }

    /* CRIACAO DO GETS  */
    public String getTipoFornecedor()
    {
        return txtNome.getText();
    }

    public String getMorada()
    {
        return txtMorada.getText();
    }

    public String getTelefone()
    {
        return txtTelefone.getText();
    }

    /*
     *   lbNome.setHorizontalAlignment(JLabel.RIGHT);
        lbSobreNome.setHorizontalAlignment(JLabel.RIGHT);
        lbUserName.setHorizontalAlignment(JLabel.RIGHT);
        lbSenha.setHorizontalAlignment(JLabel.RIGHT);
        lbConfirSenha.setHorizontalAlignment(JLabel.RIGHT);
        lbDataNascimento.setHorizontalAlignment(JLabel.RIGHT);
        lbTelefone.setHorizontalAlignment(JLabel.RIGHT);
        lbEmail.setHorizontalAlignment(JLabel.RIGHT);
        lbEndereco.setHorizontalAlignment(JLabel.RIGHT);
     
     
     */
 /*VALIDACOES */
    public void limpar()
    {

        txtNome.setText( "" );
        txtMorada.setText( "" );
        txtTelefone.setText( "" );
        txtInicias.setText( "" );
        txtEmail.setText( "" );
        txtNif.setText( "" );

    }

    public boolean campos_invalidos()
    {
        Color backGround = Color.WHITE;
        Color foreGround = Color.BLACK;
        Color caretColor = Color.BLACK;

        if ( getTipoFornecedor().equals( "" ) )
        {
            txtNome.setBackground( backGround );
            txtNome.setForeground( foreGround );
            txtNome.setCaretColor( caretColor );
            JOptionPane.showMessageDialog( null, "Pf. Preêncha o campo Nome: " );
            return true;
        }
        else if ( getMorada().equals( "" ) )
        {

            txtMorada.setBackground( backGround );
            txtMorada.setForeground( foreGround );
            txtMorada.setCaretColor( caretColor );
            JOptionPane.showMessageDialog( null, "Pf. Preêncha o campo Morada: " );
            return true;

        }
        else if ( getTelefone().equals( "" ) )
        {

            txtTelefone.setBackground( backGround );
            txtTelefone.setForeground( foreGround );
            txtTelefone.setCaretColor( caretColor );
            JOptionPane.showMessageDialog( null, "Pf. Preêncha o campo Telefone: " );
            return true;

        }

        return false;
    }

    //CLASSE EVENTO TECLADO 

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAlterar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnSalvar;
    public static javax.swing.JComboBox<String> cmbRegime;
    private static com.toedter.calendar.JDateChooser dcDataFinal;
    private static com.toedter.calendar.JDateChooser dcDataInicial;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private static javax.swing.JLabel lbSaldoFornecedor;
    private javax.swing.JLabel lbTipoProduto;
    private javax.swing.JLabel lbTipoProduto1;
    private javax.swing.JLabel lbTipoProduto2;
    private static javax.swing.JTable tabela_compras;
    private static javax.swing.JTable tabela_conta_corrente;
    private static javax.swing.JTable tabela_encomenda;
    private static javax.swing.JTable tabela_fornecedor;
    public static javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtInicias;
    public static javax.swing.JTextField txtMorada;
    public static javax.swing.JTextField txtNif;
    public static javax.swing.JTextField txtNome;
    public static javax.swing.JTextField txtTelefone;
    // End of variables declaration//GEN-END:variables

    private void procedimento_salvar()
    {

        if ( valido() )
        {

            if ( !fornecedoresController.existe_fornecedor( txtNome.getText() ) )
            {

                this.fornecedor = new TbFornecedor();
                setDados();
                try
                {
                    fornecedoresController.salvar( fornecedor );
//                    fornecedorDao.create( fornecedor );
                    limpar();
                    adicionar();
                    JOptionPane.showMessageDialog( null, "Fornecedor salvo com sucesso!...", DVML.DVML_COMERCIAL, JOptionPane.INFORMATION_MESSAGE );
                    VendaUsuarioVisao.cmbCliente.setModel( new DefaultComboBoxModel( fornecedoresController.getVector() ) );
                    VendaUsuarioVisao.cmbCliente.setSelectedItem( fornecedor.getNome() );
                    dispose();
                }
                catch ( Exception e )
                {
//                      JOptionPane.showMessageDialog(null, "Erro ao salvar o fornecedor", DVML.DVML_COMERCIAL, JOptionPane.ERROR_MESSAGE);
                }

            }
            else
            {
                JOptionPane.showMessageDialog( null, "Este fornecedor já existe.", DVML.DVML_COMERCIAL, JOptionPane.WARNING_MESSAGE );
            }

        }

    }

    private boolean valido()
    {

        if ( cmbRegime.getSelectedIndex() < 0 )
        {
            cmbRegime.requestFocus();
            JOptionPane.showMessageDialog( null, "Pf. selecione o regime" );
            return false;
        }

        if ( txtNome.equals( "" ) )
        {
            txtNome.requestFocus();
            JOptionPane.showMessageDialog( null, "Pf. digite a categoria", DVML.DVML_COMERCIAL, JOptionPane.WARNING_MESSAGE );
            return false;
        }

        return true;

    }

    private void setDados()
    {

        this.fornecedor.setNome( txtNome.getText() );
        this.fornecedor.setTelefone( txtTelefone.getText() );
        this.fornecedor.setEndereco( txtMorada.getText() );
        this.fornecedor.setSite( "" );
        this.fornecedor.setEmail( txtEmail.getText() );
        this.fornecedor.setNif( txtNif.getText() );
        this.fornecedor.setFkRegime( new Regime(getIdRegime()) );

    }

    public static void mostar_dados()
    {
        DefaultTableModel modelo = ( DefaultTableModel ) tabela_fornecedor.getModel();
        codigo = Integer.parseInt( modelo.getValueAt( tabela_fornecedor.getSelectedRow(), 0 ).toString() );
        fornecedor = ( TbFornecedor ) fornecedoresController.findByCod( codigo );
        regime = ( Regime ) regimesController.findById( fornecedor.getFkRegime().getPkRegime() );
        System.out.println( "O Regime :" + regime );
        txtNome.setText( fornecedor.getNome() );
        txtTelefone.setText( fornecedor.getTelefone() );
        txtMorada.setText( fornecedor.getEndereco() );
        txtEmail.setText( fornecedor.getEmail() );
        txtNif.setText( fornecedor.getNif() );
        cmbRegime.setSelectedItem( regime.getDesignacao() ); 
    }

    public static void adicionar()
    {

        DefaultTableModel modelo = ( DefaultTableModel ) tabela_fornecedor.getModel();
//        List<TbFornecedor> list = fornecedoresController.getAllFornecedorOrdenado();
//        List<TbFornecedor> list = fornecedoresController.getAllFornecedorOrdenado2();
//        List<TbFornecedor> list = fornecedoresController.getAllFornecedorOrdenado3();
        List<TbFornecedor> list = fornecedorDao.getAllFornecedorOrdenado();
//        List<TbFornecedor> list = fornecedoresController.listarTodos();
        tabela_fornecedor.setRowHeight( 25 );
        modelo.setRowCount( 0 );
        for ( int i = 0; i < list.size(); i++ )
        {

            TbFornecedor fornecedor = list.get( i );
            modelo.addRow(
                    new Object[]
                    {
                        fornecedor.getCodigo(),
                        fornecedor.getNome(),
                        fornecedor.getEndereco(),
                        fornecedor.getTelefone(),
                        fornecedor.getEmail(),
                        fornecedor.getNif(),
                    }
            );
        }

    }

    private void adicionar( List<TbFornecedor> list )
    {

        DefaultTableModel modelo = ( DefaultTableModel ) tabela_fornecedor.getModel();

        modelo.setRowCount( 0 );
        for ( int i = 0; i < list.size(); i++ )
        {

            TbFornecedor fornecedor = list.get( i );
            modelo.addRow(
                    new Object[]
                    {
                        fornecedor.getCodigo(),
                        fornecedor.getNome(),
                        fornecedor.getEndereco(),
                        fornecedor.getTelefone(),
                        fornecedor.getEmail(),
                        fornecedor.getNif()
                    }
            );
        }

    }

    private void procedimento_alterar()
    {

        if ( valido() )
        {

            this.fornecedor = (TbFornecedor)fornecedoresController.findById(this.codigo );
            setDados();
            try
            {
                fornecedoresController.actualizar(fornecedor );
                limpar();
                adicionar();
                JOptionPane.showMessageDialog( null, "Dados alterados com sucesso!...", DVML.DVML_COMERCIAL, JOptionPane.INFORMATION_MESSAGE );
            }
            catch ( Exception e )
            {
                JOptionPane.showMessageDialog( null, "Erro ao alteraer os dados", DVML.DVML_COMERCIAL, JOptionPane.ERROR_MESSAGE );
            }

        }

    }

    private void procedimento_eliminar()
    {

        int opcao = JOptionPane.showConfirmDialog( null, "Tens a plena certeza que pretendes eliminar essa categria?" );
        if ( opcao == JOptionPane.YES_OPTION )
        {

            try
            {
                fornecedoresController.eliminar(this.codigo );
                adicionar();
                limpar();
                JOptionPane.showMessageDialog( null, "Fornecedor eliminado com sucesso!...", DVML.DVML_COMERCIAL, JOptionPane.INFORMATION_MESSAGE );

            }
            catch ( Exception e )
            {

                e.printStackTrace();
                JOptionPane.showMessageDialog( null, "Erro ao eliminar este Fornecedor.\nPossivelmente já esta realcionado com algumas vendas.", DVML.DVML_COMERCIAL, JOptionPane.ERROR_MESSAGE );
            }

        }

    }

    public static void adicionar_encomendas()
    {
        DefaultTableModel modelo = ( DefaultTableModel ) tabela_encomenda.getModel();
        modelo.setRowCount( 0 );
        tabela_encomenda.setRowHeight( 25 );
        if ( Objects.nonNull( fornecedor ) )
        {
            List<Compras> listasEncomendas = comprasController.getAllEncomendasByIdFornecedor( fornecedor.getCodigo() );

            if ( Objects.nonNull( listasEncomendas ) )
            {
                for ( int i = 0; i < listasEncomendas.size(); i++ )
                {
                    Compras get = listasEncomendas.get( i );
                    modelo.addRow( new Object[]
                    {
                        get.getCodFact(),
                        get.getDataCompra()
                    } );
                }
            }

        }
        else
        {
            System.out.println( "Fornecedor NULO." );
        }
    }

    public static void adicionar_compras()
    {
        DefaultTableModel modelo = ( DefaultTableModel ) tabela_compras.getModel();
        modelo.setRowCount( 0 );
        tabela_compras.setRowHeight( 25 );
        if ( Objects.nonNull( fornecedor ) )
        {
            List<Compras> listasCompras = comprasController.getAllComrpasByIdFornecedor( fornecedor.getCodigo() );

            if ( Objects.nonNull( listasCompras ) )
            {
                for ( int i = 0; i < listasCompras.size(); i++ )
                {
                    Compras compraLocal = listasCompras.get( i );
                    modelo.addRow( new Object[]
                    {
                        compraLocal.getCodFact(),
                        compraLocal.getDocVosso() + " " + compraLocal.getDocVossoNumero(),
                        MetodosUtil.getDataBanco( compraLocal.getDocVossoData() ),
                        MetodosUtil.getDataBanco( compraLocal.getDocVossoDataVencimento() ),
                        compraLocal.getTotalCompra(),
                        compraLocal.getValorPago(),
                        compraLocal.getValorPorPagar(),
                    } );
                }
            }

        }
    }

    private int getIdRegime()
    {
//        Regime regime = regimeDao.getRegimeByDescricao( String.valueOf( cmbRegime.getSelectedItem() ) );
        Regime regime = regimesController.getRegimeByDesignacao(String.valueOf( cmbRegime.getSelectedItem() ) );
        return Objects.isNull( regime ) ? 1 : regime.getPkRegime();
    }

    class TratarEvento implements KeyListener
    {

        String prefixo = "";

        public void keyPressed( KeyEvent evt )
        {

            if ( evt.getKeyCode() != KeyEvent.VK_BACK_SPACE && evt.getKeyCode() != KeyEvent.VK_ENTER )
            {
                char key = evt.getKeyChar();
                prefixo = txtInicias.getText().trim() + key;
                adicionar( fornecedoresController.IniciaisFornecedores( prefixo ) );

            }
            else if ( evt.getKeyCode() == KeyEvent.VK_BACK_SPACE )
            {
                try
                {

                    prefixo = prefixo.toString().trim().substring( 0, prefixo.length() - 1 );
                    adicionar( fornecedoresController.IniciaisFornecedores( prefixo ) );

                }
                catch ( Exception e )
                {

                }

            }
        }

        public void keyReleased( KeyEvent evt )
        {
        }

        public void keyTyped( KeyEvent evt )
        {
        }

    }

    public static void mostrarSaldoFornecedor()
    {
        try
        {
            BigDecimal saldo = ecfController.getSaldoActualByIdFornecedor( fornecedor.getCodigo() );
            lbSaldoFornecedor.setText( CfMethods.formatarComoMoeda( saldo ) );
        }
        catch ( Exception e )
        {
            lbSaldoFornecedor.setText( CfMethods.formatarComoMoeda( 0 ) );
        }

    }

    public static void mostrarContaCorrenteFornecedor()
    {
        if ( Objects.nonNull( fornecedor ) )
        {
            List<ExtratoContaFornecedor> lista = ecfController.listarTodosByData1AndData2(
                    fornecedor.getCodigo(),
                    dcDataInicial.getDate(),
                    dcDataFinal.getDate() );
            tabela_conta_corrente.setRowHeight( 25 );

            DefaultTableModel modelo = ( DefaultTableModel ) tabela_conta_corrente.getModel();
            modelo.setRowCount( 0 );
            if ( Objects.nonNull( lista ) )
            {
                for ( int i = 0; i < lista.size(); i++ )
                {
                    ExtratoContaFornecedor get = lista.get( i );
                    modelo.addRow( new Object[]
                    {
                        get.getDataHora(),
                        get.getDocumento() + " " + get.getReferencia(),
                        get.getCredito(),
                        get.getDebito(),
                        get.getSaldoActual(),

                    } );

                }

            }

        }

    }

}
