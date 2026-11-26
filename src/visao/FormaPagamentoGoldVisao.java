/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package visao;


import java.sql.Connection;
//import com.sun.org.apache.bcel.internal.generic.DCMPG;
import comercial.controller.CaixasController;
import comercial.controller.FormaPagamentoController;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import kitanda.util.CfMethods;
import util.BDConexao;

/**
 *
 * @author Domingos Dala Vunge
 */

public class FormaPagamentoGoldVisao extends javax.swing.JDialog
{

    private static FormaPagamentoController formaPagamentoController;
    private static CaixasController caixasController;

    public FormaPagamentoGoldVisao( java.awt.Frame parent, boolean modal, BDConexao conexao )
    {
        super( parent, modal );
        initComponents();
        setLocationRelativeTo( null );
        formaPagamentoController = new FormaPagamentoController( conexao );
        caixasController = new CaixasController( conexao );
        lb_total_geral.setText( totalDaRecolha() );
        // aspecto Kitanda; se falhar, abre o desenho antigo
        util.ui.LayoutSeguro.aplicar( this, "Pagamento da recolha", this::montarLayoutWeb );
    }

    /**
     * Total a pagar da recolha. Recalcula-o das peças da tabela (setTotalPagar):
     * depois de cada factura a recolha limpa o txtTotalApagar e, na venda
     * seguinte, o pagamento podia abrir com o Total vazio.
     */
    private static String totalDaRecolha()
    {
        try
        {
            RecolhaPedidosVisao.setTotalPagar();
        }
        catch ( Exception e )
        {
            System.err.println( "[Pagamento] total: " + e.getMessage() );
        }
        String t = RecolhaPedidosVisao.txtTotalApagar.getText();
        return t == null || t.trim().isEmpty() ? CfMethods.formatarComoMoeda( 0 ) : t.trim();
    }

    // =====================================================================
    // LAYOUT WEB: visor escuro com o total, o recebido e o que falta / troco
    // (actualiza enquanto se escreve); as três formas lado a lado, cada uma
    // com "Pagar tudo aqui"; dinheiro rápido (valor exacto e notas); F1/F2/F3
    // e Esc. Os campos e o lb_troco são os mesmos que a recolha lê ao gravar.
    // =====================================================================
    private void montarLayoutWeb()
    {
        util.ui.FormularioWeb.campoTexto( txtDinheiro, txtCartao, txtTransferencia );
        for ( javax.swing.JTextField t : new javax.swing.JTextField[]
        {
            txtDinheiro, txtCartao, txtTransferencia
        } )
        {
            t.setHorizontalAlignment( javax.swing.JTextField.RIGHT );
            t.setFont( util.ui.EstiloUI.FONTE_NEGRITO.deriveFont( 20f ) );
            t.setPreferredSize( new java.awt.Dimension( 140, 42 ) );
            t.setToolTipText( "Ex.: 15000, 15.000 ou 15.000,00 — Enter finaliza" );
            // os ouvintes de rato do desenho antigo (passar o rato por cima já "validava" e desligava
            // as outras formas): com o valor automático nos bancos bloqueavam o dinheiro. Fica o Enter.
            for ( java.awt.event.MouseListener ml : t.getMouseListeners() )
            {
                if ( ml.getClass().getName().startsWith( FormaPagamentoGoldVisao.class.getName() + "$" ) )
                {
                    t.removeMouseListener( ml );
                }
            }
            final javax.swing.JTextField campo = t;
            t.getDocument().addDocumentListener( new javax.swing.event.DocumentListener()
            {
                public void insertUpdate( javax.swing.event.DocumentEvent e )
                {
                    mudou( campo );
                }

                public void removeUpdate( javax.swing.event.DocumentEvent e )
                {
                    mudou( campo );
                }

                public void changedUpdate( javax.swing.event.DocumentEvent e )
                {
                }
            } );
        }
        for ( final javax.swing.JTextField banco : new javax.swing.JTextField[]
        {
            txtDinheiro, txtCartao, txtTransferencia
        } )
        {
            banco.addFocusListener( new java.awt.event.FocusAdapter()
            {
                @Override
                public void focusGained( java.awt.event.FocusEvent e )
                {
                    preencherBanco( banco );
                    banco.selectAll();
                }
            } );
        }

        // ---- visor: total, recebido e em falta / troco (actualiza enquanto se escreve)
        javax.swing.JLabel lbTotalTitulo = rotuloVisor( "TOTAL A PAGAR" );
        lb_total_geral.setFont( util.ui.MesasWeb.fonteVisor( 34 ) );
        lb_total_geral.setForeground( new java.awt.Color( 21, 128, 61 ) ); // verde escuro: bem visível no visor claro
        lb_total_geral.setHorizontalAlignment( javax.swing.SwingConstants.RIGHT );
        final javax.swing.JLabel lbRecebido = valorVisor();
        final javax.swing.JLabel lbSaldoTitulo = rotuloVisor( "EM FALTA" );
        final javax.swing.JLabel lbSaldo = valorVisor();
        lbSaldoTitulo.setHorizontalAlignment( javax.swing.SwingConstants.RIGHT );
        lbSaldo.setHorizontalAlignment( javax.swing.SwingConstants.RIGHT );
        javax.swing.JPanel recebido = util.ui.ComponentesWeb.transparente( new java.awt.GridLayout( 2, 1 ) );
        recebido.add( rotuloVisor( "RECEBIDO" ) );
        recebido.add( lbRecebido );
        javax.swing.JPanel saldo = util.ui.ComponentesWeb.transparente( new java.awt.GridLayout( 2, 1 ) );
        saldo.add( lbSaldoTitulo );
        saldo.add( lbSaldo );
        javax.swing.JPanel baseVisor = util.ui.ComponentesWeb.transparente( new java.awt.GridLayout( 1, 2, 12, 0 ) );
        baseVisor.add( recebido );
        baseVisor.add( saldo );
        javax.swing.JPanel linhaVisor = new javax.swing.JPanel();
        linhaVisor.setBackground( new java.awt.Color( 187, 247, 208 ) );
        linhaVisor.setPreferredSize( new java.awt.Dimension( 10, 1 ) );
        javax.swing.JPanel baixoVisor = util.ui.ComponentesWeb.transparente( new java.awt.BorderLayout( 0, 8 ) );
        baixoVisor.add( linhaVisor, java.awt.BorderLayout.NORTH );
        baixoVisor.add( baseVisor, java.awt.BorderLayout.CENTER );
        util.ui.ComponentesWeb.CaixaSuave visor = new util.ui.ComponentesWeb.CaixaSuave( new java.awt.BorderLayout( 0, 4 ),
                new java.awt.Color( 240, 253, 244 ), new java.awt.Color( 134, 239, 172 ) ); // visor claro
        visor.setBorder( javax.swing.BorderFactory.createEmptyBorder( 12, 16, 12, 16 ) );
        // de onde vem o total (desconto da ficha do cliente) e o total por extenso
        javax.swing.JLabel lbOrigem = new javax.swing.JLabel( " " );
        lbOrigem.setFont( util.ui.EstiloUI.FONTE_NEGRITO.deriveFont( 11.5f ) );
        lbOrigem.setForeground( new java.awt.Color( 194, 65, 12 ) );
        lbOrigem.setHorizontalAlignment( javax.swing.SwingConstants.RIGHT );
        double descontoRecolha = CfMethods.parseMoedaFormatada( RecolhaPedidosVisao.txtDesconto.getText() );
        if ( descontoRecolha > 0.004 )
        {
            lbOrigem.setText( "Total geral " + RecolhaPedidosVisao.txtTotalGeral.getText().trim() + "   ·   Desconto "
                    + new java.text.DecimalFormat( "#,##0.##" ).format( RecolhaPedidosVisao.getPercentagemDescontoCliente() )
                    + "%  −" + RecolhaPedidosVisao.txtDesconto.getText().trim() );
        }
        javax.swing.JPanel topoVisor = util.ui.ComponentesWeb.transparente( new java.awt.BorderLayout( 10, 0 ) );
        topoVisor.add( lbTotalTitulo, java.awt.BorderLayout.WEST );
        topoVisor.add( lbOrigem, java.awt.BorderLayout.CENTER );
        util.ui.ComponentesWeb.TextoQuebrado lbExtenso = new util.ui.ComponentesWeb.TextoQuebrado();
        lbExtenso.setFont( util.ui.EstiloUI.FONTE.deriveFont( java.awt.Font.ITALIC, 12.5f ) );
        lbExtenso.setForeground( new java.awt.Color( 220, 38, 38 ) ); // vermelho: bem visível
        lbExtenso.setFont( util.ui.EstiloUI.FONTE_NEGRITO.deriveFont( java.awt.Font.BOLD | java.awt.Font.ITALIC, 13f ) );
        String extenso = RecolhaPedidosVisao.extensoTotalPagar();
        lbExtenso.setText( extenso.isEmpty() ? " " : "São: " + extenso );
        javax.swing.JPanel centroVisor = util.ui.ComponentesWeb.transparente( new java.awt.BorderLayout( 0, 2 ) );
        centroVisor.add( lb_total_geral, java.awt.BorderLayout.CENTER );
        centroVisor.add( lbExtenso, java.awt.BorderLayout.SOUTH );
        visor.add( topoVisor, java.awt.BorderLayout.NORTH );
        visor.add( centroVisor, java.awt.BorderLayout.CENTER );
        visor.add( baixoVisor, java.awt.BorderLayout.SOUTH );

        // o lb_troco (lido pela recolha ao gravar) continua a ser o que conta; o visor só o mostra
        final Runnable actualizarVisor = () ->
        {
            double total = CfMethods.parseMoedaFormatada( lb_total_geral.getText() );
            double entregue = getValor( txtDinheiro ) + getValor( txtCartao ) + getValor( txtTransferencia );
            double diferenca = entregue - total;
            lbRecebido.setText( CfMethods.formatarComoMoeda( entregue ) );
            boolean falta = diferenca < -0.004;
            lbSaldoTitulo.setText( falta ? "EM FALTA" : "TROCO" );
            lbSaldo.setText( CfMethods.formatarComoMoeda( Math.abs( diferenca ) ) );
            lbSaldo.setForeground( falta ? new java.awt.Color( 194, 65, 12 ) : new java.awt.Color( 21, 128, 61 ) );
            jButton3.setEnabled( !falta );
        };
        lb_troco.addPropertyChangeListener( "text", e -> actualizarVisor.run() );

        // ---- formas de pagamento, lado a lado; "Tudo" põe nessa forma o que falta pagar
        javax.swing.JPanel formas = util.ui.ComponentesWeb.transparente( new util.ui.ComponentesWeb.GrelhaResponsiva( 170, 10, 3 ) );
        formas.add( formaPagamento( "Dinheiro", "F1", IconePagamento.DINHEIRO, util.ui.ComponentesWeb.VERDE, txtDinheiro, this::accaoDinheiro ) );
        formas.add( formaPagamento( "Cartão (TPA)", "F2", IconePagamento.CARTAO, util.ui.ComponentesWeb.AZUL_VIVO, txtCartao, this::accaoCartao ) );
        formas.add( formaPagamento( "Transferência", "F3", IconePagamento.TRANSFERENCIA, util.ui.ComponentesWeb.ROXO, txtTransferencia,
                this::accaoTransferencia ) );

        // ---- dinheiro rápido: valor exacto e notas (somam ao dinheiro)
        javax.swing.JPanel notas = util.ui.ComponentesWeb.transparente( new util.ui.EstiloUI.WrapLayout( java.awt.FlowLayout.LEFT, 6, 4 ) );
        javax.swing.JButton exacto = botaoNota( "Valor exacto", "O cliente entrega em dinheiro o valor que falta" );
        exacto.addActionListener( e ->
        {
            txtDinheiro.putClientProperty( AUTO, true );
            preencherBanco( txtDinheiro );
            accaoDinheiro();
            txtDinheiro.requestFocusInWindow();
        } );
        notas.add( exacto );
        for ( final int nota : new int[]
        {
            500, 1000, 2000, 5000, 10000
        } )
        {
            javax.swing.JButton b = botaoNota( "+ " + formatarInteiro( nota ), "Somar uma nota de " + formatarInteiro( nota ) + " ao dinheiro" );
            b.addActionListener( e ->
            {
                double base = auto( txtDinheiro ) ? 0 : getValor( txtDinheiro );
                txtDinheiro.setText( formatar( base + nota ) );
                accaoDinheiro();
                txtDinheiro.requestFocusInWindow();
            } );
            notas.add( b );
        }
        javax.swing.JButton limpar = botaoNota( "Limpar", "Apagar os valores e começar de novo" );
        limpar.setForeground( util.ui.ComponentesWeb.VERMELHO );
        limpar.addActionListener( e ->
        {
            for ( javax.swing.JTextField t : new javax.swing.JTextField[]
            {
                txtDinheiro, txtCartao, txtTransferencia
            } )
            {
                t.setText( "" );
                t.setEnabled( true );
            }
            setTroco();
            txtDinheiro.requestFocusInWindow();
        } );
        notas.add( limpar );
        javax.swing.JLabel lbNotas = new javax.swing.JLabel( "Dinheiro rápido" );
        util.ui.ComponentesWeb.rotulo( lbNotas );
        javax.swing.JPanel blocoNotas = util.ui.ComponentesWeb.transparente( new java.awt.BorderLayout( 0, 2 ) );
        blocoNotas.add( lbNotas, java.awt.BorderLayout.NORTH );
        blocoNotas.add( notas, java.awt.BorderLayout.CENTER );

        javax.swing.JPanel recebimento = util.ui.FormularioWeb.pilha( 10, formas, blocoNotas );
        javax.swing.JPanel cartaoFormas = util.ui.FormularioWeb.cartao( "Como paga o cliente?", util.ui.ComponentesWeb.AZUL_VIVO, recebimento );
        javax.swing.JPanel conteudo = util.ui.FormularioWeb.pilha( 10, visor, cartaoFormas );

        jButton3.setText( "Finalizar e imprimir" );
        jButton3.setIcon( null );
        util.ui.ComponentesWeb.botao( jButton3, util.ui.ComponentesWeb.Variante.SUCESSO, null, "Emitir a factura-recibo (Enter)" );
        jButton3.setFont( util.ui.EstiloUI.FONTE_NEGRITO.deriveFont( 15f ) );
        jButton3.setBorder( javax.swing.BorderFactory.createEmptyBorder( 11, 26, 11, 26 ) );
        jButton6.setText( "Cancelar" );
        util.ui.FormularioWeb.botaoSair( jButton6, "Voltar à recolha sem cobrar (Esc)" );
        getRootPane().setDefaultButton( jButton3 );

        // teclas: F1/F2/F3 vão a cada forma de pagamento; Esc cancela
        javax.swing.JComponent raiz = getRootPane();
        javax.swing.InputMap im = raiz.getInputMap( javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW );
        javax.swing.ActionMap am = raiz.getActionMap();
        Object[][] teclas =
        {
            {
                "F1", txtDinheiro
            },
            {
                "F2", txtCartao
            },
            {
                "F3", txtTransferencia
            }
        };
        for ( final Object[] t : teclas )
        {
            im.put( javax.swing.KeyStroke.getKeyStroke( ( String ) t[ 0 ] ), "pagamento." + t[ 0 ] );
            am.put( "pagamento." + t[ 0 ], new javax.swing.AbstractAction()
            {
                @Override
                public void actionPerformed( java.awt.event.ActionEvent e )
                {
                    javax.swing.JTextField campo = ( javax.swing.JTextField ) t[ 1 ];
                    if ( campo.isEnabled() )
                    {
                        campo.requestFocusInWindow();
                        campo.selectAll();
                    }
                }
            } );
        }
        im.put( javax.swing.KeyStroke.getKeyStroke( java.awt.event.KeyEvent.VK_ESCAPE, 0 ), "pagamento.cancelar" );
        am.put( "pagamento.cancelar", new javax.swing.AbstractAction()
        {
            @Override
            public void actionPerformed( java.awt.event.ActionEvent e )
            {
                dispose();
            }
        } );

        util.ui.FormularioWeb.montar( this,
                util.ui.FormularioWeb.cabecalho( "Cobrar", "Indique quanto o cliente entrega; pode dividir por várias formas", lbUtilizador() ),
                conteudo,
                util.ui.FormularioWeb.barraAccoes( util.ui.FormularioWeb.dica( "F1 Dinheiro  ·  F2 Cartão  ·  F3 Transferência  ·  Esc Cancelar" ),
                        jButton6, jButton3 ),
                "Lavandaria  ·  Pagamento" );
        escrever( txtDinheiro, CfMethods.parseMoedaFormatada( lb_total_geral.getText() ) );
        setTroco();
        actualizarVisor.run();
        util.ui.FormularioWeb.ajustar( this, 760, 700, 520, 520 );
        javax.swing.SwingUtilities.invokeLater( txtDinheiro::requestFocusInWindow );
    }

    // ---- valor automático nos bancos (como na forma de pagamento das mesas)

    /** true enquanto o próprio ecrã escreve num campo (não é o operador). */
    private boolean aPreencher;
    private static final String AUTO = "pagamento.auto";

    private boolean auto( JTextField t )
    {
        return Boolean.TRUE.equals( t.getClientProperty( AUTO ) );
    }

    private JTextField[] campos()
    {
        return new JTextField[]
        {
            txtDinheiro, txtCartao, txtTransferencia
        };
    }

    /** Escreve um valor calculado pelo ecrã (fica "automático": acompanha o que falta). */
    private void escrever( JTextField t, double v )
    {
        aPreencher = true;
        try
        {
            String novo = v > 0.004 ? formatar( v ) : "";
            if ( !novo.equals( t.getText() ) )
            {
                t.setText( novo );
            }
            t.putClientProperty( AUTO, true );
        }
        finally
        {
            aPreencher = false;
        }
    }

    /**
     * Como na forma de pagamento da Venda: os campos automáticos (os que o
     * operador não escreveu) ficam com o que falta para o total, descontando
     * os valores escritos. origem = o campo que acabou de mudar (não mexe nele).
     */
    private void redistribuir( JTextField origem )
    {
        double total = CfMethods.parseMoedaFormatada( lb_total_geral.getText() );
        double usado = 0;
        for ( JTextField t : campos() )
        {
            if ( !auto( t ) || t == origem )
            {
                usado += getValor( t );
            }
        }
        for ( JTextField t : campos() )
        {
            if ( t != origem && auto( t ) && t.isEnabled() )
            {
                double v = Math.max( 0, Math.round( ( total - usado ) * 100 ) / 100.0 );
                escrever( t, v );
                usado += v;
            }
        }
        setTroco();
    }

    /**
     * Ao entrar num campo vazio (ou automático): fica com tudo o que falta,
     * descontando só o que o operador escreveu; os outros automáticos ficam a zero.
     */
    private void preencherBanco( JTextField campo )
    {
        if ( !campo.isEnabled() || getValor( campo ) > 0 && !auto( campo ) )
        {
            return;
        }
        double total = CfMethods.parseMoedaFormatada( lb_total_geral.getText() );
        double escritos = 0;
        for ( JTextField t : campos() )
        {
            if ( t != campo && !auto( t ) )
            {
                escritos += getValor( t );
            }
        }
        escrever( campo, Math.max( 0, Math.round( ( total - escritos ) * 100 ) / 100.0 ) );
        redistribuir( campo );
    }

    /** O operador escreveu num campo: deixa de ser automático e os automáticos acompanham. */
    private void mudou( final JTextField campo )
    {
        setTroco();
        if ( aPreencher )
        {
            return;
        }
        campo.putClientProperty( AUTO, false );
        javax.swing.SwingUtilities.invokeLater( () -> redistribuir( campo ) );
    }

    // ---- peças do layout do pagamento

    /** Utilizador com sessão, à direita no cabeçalho. */
    private static javax.swing.JLabel lbUtilizador()
    {
        String nome = RecolhaPedidosVisao.getNomeUtilizador();
        javax.swing.JLabel l = new javax.swing.JLabel( nome.isEmpty() ? "" : "Utilizador:  " + nome );
        l.setFont( util.ui.EstiloUI.FONTE_NEGRITO.deriveFont( 13f ) );
        l.setForeground( java.awt.Color.WHITE );
        return l;
    }

    private static javax.swing.JLabel rotuloVisor( String texto )
    {
        javax.swing.JLabel l = new javax.swing.JLabel( texto );
        l.setFont( util.ui.EstiloUI.FONTE_NEGRITO.deriveFont( 11.5f ) );
        l.setForeground( new java.awt.Color( 71, 85, 105 ) );
        return l;
    }

    private static javax.swing.JLabel valorVisor()
    {
        javax.swing.JLabel l = new javax.swing.JLabel( " " );
        l.setFont( util.ui.MesasWeb.fonteVisor( 20 ) );
        l.setForeground( new java.awt.Color( 15, 23, 42 ) );
        return l;
    }

    /** O que falta pagar sem contar com o campo indicado (o que esse campo deve ter para fechar a conta). */
    private double emFalta( JTextField campo )
    {
        double total = CfMethods.parseMoedaFormatada( lb_total_geral.getText() );
        double outros = 0;
        for ( JTextField t : new JTextField[]
        {
            txtDinheiro, txtCartao, txtTransferencia
        } )
        {
            if ( t != campo )
            {
                outros += getValor( t );
            }
        }
        return Math.round( ( total - outros ) * 100 ) / 100.0;
    }

    private javax.swing.JComponent formaPagamento( String titulo, String tecla, IconePagamento icone, java.awt.Color cor,
            final JTextField campo, final Runnable accao )
    {
        javax.swing.JLabel lbTitulo = new javax.swing.JLabel( titulo, new IconeForma( icone, cor ), javax.swing.SwingConstants.LEFT );
        lbTitulo.setIconTextGap( 8 );
        lbTitulo.setFont( util.ui.EstiloUI.FONTE_NEGRITO.deriveFont( 14f ) );
        lbTitulo.setForeground( util.ui.ComponentesWeb.TEXTO );
        util.ui.ComponentesWeb.Etiqueta lbTecla = new util.ui.ComponentesWeb.Etiqueta( tecla, cor );
        lbTecla.setFont( util.ui.EstiloUI.FONTE_NEGRITO.deriveFont( 10.5f ) );
        lbTecla.setBorder( javax.swing.BorderFactory.createEmptyBorder( 2, 7, 2, 7 ) );
        javax.swing.JPanel topo = util.ui.ComponentesWeb.transparente( new java.awt.BorderLayout( 6, 0 ) );
        topo.add( lbTitulo, java.awt.BorderLayout.CENTER );
        topo.add( lbTecla, java.awt.BorderLayout.EAST );

        final javax.swing.JButton tudo = new javax.swing.JButton( "Pagar tudo aqui" );
        util.ui.ComponentesWeb.botao( tudo, util.ui.ComponentesWeb.Variante.SECUNDARIO, null, "Põe nesta forma o valor que falta pagar" );
        tudo.putClientProperty( "web.pilula", Boolean.TRUE );
        tudo.setFont( util.ui.EstiloUI.FONTE_NEGRITO.deriveFont( 12f ) );
        tudo.setBorder( javax.swing.BorderFactory.createEmptyBorder( 5, 10, 5, 10 ) );
        tudo.addActionListener( e ->
        {
            campo.putClientProperty( AUTO, true );
            preencherBanco( campo );
            accao.run();
            campo.requestFocusInWindow();
        } );
        // a lógica antiga desliga as outras formas quando uma já paga tudo: o botão segue o campo
        campo.addPropertyChangeListener( "enabled", e -> tudo.setEnabled( campo.isEnabled() ) );
        tudo.setEnabled( campo.isEnabled() );

        util.ui.ComponentesWeb.CaixaSuave caixa = new util.ui.ComponentesWeb.CaixaSuave( new java.awt.BorderLayout( 0, 8 ),
                util.ui.ComponentesWeb.mistura( cor, java.awt.Color.WHITE, 0.93 ), util.ui.ComponentesWeb.mistura( cor, java.awt.Color.WHITE, 0.70 ) );
        caixa.setBorder( javax.swing.BorderFactory.createEmptyBorder( 10, 12, 12, 12 ) );
        caixa.add( topo, java.awt.BorderLayout.NORTH );
        caixa.add( campo, java.awt.BorderLayout.CENTER );
        caixa.add( tudo, java.awt.BorderLayout.SOUTH );
        return caixa;
    }

    private static javax.swing.JButton botaoNota( String texto, String dica )
    {
        javax.swing.JButton b = new javax.swing.JButton( texto );
        util.ui.ComponentesWeb.botao( b, util.ui.ComponentesWeb.Variante.SECUNDARIO, null, dica );
        b.putClientProperty( "web.pilula", Boolean.TRUE );
        b.setFont( util.ui.EstiloUI.FONTE_NEGRITO.deriveFont( 12.5f ) );
        b.setBorder( javax.swing.BorderFactory.createEmptyBorder( 6, 12, 6, 12 ) );
        b.setFocusable( false );
        return b;
    }

    /** 1500.5 -> "1.500,50" (o formato que os operadores escrevem e que a recolha sabe ler). */
    private static String formatar( double v )
    {
        java.text.DecimalFormatSymbols s = new java.text.DecimalFormatSymbols();
        s.setDecimalSeparator( ',' );
        s.setGroupingSeparator( '.' );
        return new java.text.DecimalFormat( "#,##0.00", s ).format( v );
    }

    private static String formatarInteiro( int v )
    {
        String t = formatar( v );
        return t.substring( 0, t.length() - 3 );
    }

    private enum IconePagamento
    {
        DINHEIRO, CARTAO, TRANSFERENCIA
    }

    /** Ícone de traço da forma de pagamento, num círculo suave da cor da forma. */
    private static final class IconeForma implements javax.swing.Icon
    {

        private final IconePagamento tipo;
        private final java.awt.Color cor;

        IconeForma( IconePagamento tipo, java.awt.Color cor )
        {
            this.tipo = tipo;
            this.cor = cor;
        }

        @Override
        public int getIconWidth()
        {
            return 28;
        }

        @Override
        public int getIconHeight()
        {
            return 28;
        }

        @Override
        public void paintIcon( java.awt.Component c, java.awt.Graphics g, int x, int y )
        {
            java.awt.Graphics2D g2 = util.ui.ComponentesWeb.suave( g );
            g2.translate( x, y );
            g2.setColor( util.ui.ComponentesWeb.mistura( cor, java.awt.Color.WHITE, 0.78 ) );
            g2.fillOval( 0, 0, 28, 28 );
            g2.setColor( cor.darker() );
            g2.setStroke( new java.awt.BasicStroke( 1.8f, java.awt.BasicStroke.CAP_ROUND, java.awt.BasicStroke.JOIN_ROUND ) );
            switch ( tipo )
            {
                case DINHEIRO:
                    g2.drawRoundRect( 6, 9, 16, 10, 3, 3 );
                    g2.drawOval( 11, 11, 6, 6 );
                    break;
                case CARTAO:
                    g2.drawRoundRect( 6, 8, 16, 12, 3, 3 );
                    g2.drawLine( 6, 12, 22, 12 );
                    g2.drawLine( 9, 17, 13, 17 );
                    break;
                default: // transferência: duas setas
                    g2.drawLine( 7, 11, 20, 11 );
                    g2.drawLine( 17, 8, 20, 11 );
                    g2.drawLine( 17, 14, 20, 11 );
                    g2.drawLine( 21, 18, 8, 18 );
                    g2.drawLine( 11, 15, 8, 18 );
                    g2.drawLine( 11, 21, 8, 18 );
                    break;
            }
            g2.dispose();
        }
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
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        lb_total_geral = new javax.swing.JLabel();
        lb_total_geral1 = new javax.swing.JLabel();
        txtDinheiro = new javax.swing.JTextField();
        txtCartao = new javax.swing.JTextField();
        lb_total_geral2 = new javax.swing.JLabel();
        lb_total_geral3 = new javax.swing.JLabel();
        txtTransferencia = new javax.swing.JTextField();
        lb_troco = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jButton3 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 0, 51));

        jLabel1.setFont(new java.awt.Font("Lucida Grande", 1, 48)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Forma de Pagamento");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        lb_total_geral.setFont(new java.awt.Font("Lucida Grande", 1, 36)); // NOI18N
        lb_total_geral.setText("0.0 AOA");

        lb_total_geral1.setFont(new java.awt.Font("Lucida Grande", 0, 30)); // NOI18N
        lb_total_geral1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lb_total_geral1.setText("DINHEIRO:");

        txtDinheiro.setFont(new java.awt.Font("Segoe UI", 0, 36)); // NOI18N
        txtDinheiro.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txtDinheiro.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseEntered(java.awt.event.MouseEvent evt)
            {
                txtDinheiroMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt)
            {
                txtDinheiroMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt)
            {
                txtDinheiroMousePressed(evt);
            }
        });
        txtDinheiro.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtDinheiroActionPerformed(evt);
            }
        });

        txtCartao.setFont(new java.awt.Font("Segoe UI", 0, 36)); // NOI18N
        txtCartao.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txtCartao.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseEntered(java.awt.event.MouseEvent evt)
            {
                txtCartaoMouseEntered(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt)
            {
                txtCartaoMousePressed(evt);
            }
        });
        txtCartao.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtCartaoActionPerformed(evt);
            }
        });

        lb_total_geral2.setFont(new java.awt.Font("Lucida Grande", 0, 30)); // NOI18N
        lb_total_geral2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lb_total_geral2.setText("CARTÃO:");

        lb_total_geral3.setFont(new java.awt.Font("Lucida Grande", 0, 30)); // NOI18N
        lb_total_geral3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lb_total_geral3.setText("TRANSFERÊNCIA:");

        txtTransferencia.setFont(new java.awt.Font("Segoe UI", 0, 36)); // NOI18N
        txtTransferencia.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txtTransferencia.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseEntered(java.awt.event.MouseEvent evt)
            {
                txtTransferenciaMouseEntered(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt)
            {
                txtTransferenciaMousePressed(evt);
            }
        });
        txtTransferencia.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtTransferenciaActionPerformed(evt);
            }
        });

        lb_troco.setFont(new java.awt.Font("Lucida Grande", 1, 36)); // NOI18N
        lb_troco.setText("0");

        jLabel2.setBackground(new java.awt.Color(0, 0, 51));
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Total da Factura");
        jLabel2.setOpaque(true);

        jLabel3.setBackground(new java.awt.Color(0, 0, 51));
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Troco");
        jLabel3.setOpaque(true);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lb_total_geral, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lb_troco, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(lb_total_geral1, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtDinheiro, javax.swing.GroupLayout.PREFERRED_SIZE, 332, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(txtTransferencia, javax.swing.GroupLayout.PREFERRED_SIZE, 332, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtCartao, javax.swing.GroupLayout.PREFERRED_SIZE, 332, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(37, 37, 37))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(lb_total_geral3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lb_total_geral2, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lb_total_geral, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lb_total_geral1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtDinheiro, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lb_total_geral2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtCartao, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lb_total_geral3, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTransferencia, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                .addComponent(lb_troco, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jButton3.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagem/salvar/save_32x32.png"))); // NOI18N
        jButton3.setText("Finalizar Processo");
        jButton3.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButton3ActionPerformed(evt);
            }
        });

        jButton6.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagem/out/out_32x32.png"))); // NOI18N
        jButton6.setText("Sair");
        jButton6.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButton6ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(335, Short.MAX_VALUE)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(12, 12, 12))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(15, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtDinheiroActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtDinheiroActionPerformed
    {//GEN-HEADEREND:event_txtDinheiroActionPerformed
        // TODO add your handling code here:
        accaoDinheiro();
    }//GEN-LAST:event_txtDinheiroActionPerformed

    private void txtCartaoActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtCartaoActionPerformed
    {//GEN-HEADEREND:event_txtCartaoActionPerformed
        // TODO add your handling code here:
        accaoCartao();
    }//GEN-LAST:event_txtCartaoActionPerformed

    private void txtTransferenciaActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtTransferenciaActionPerformed
    {//GEN-HEADEREND:event_txtTransferenciaActionPerformed
        // TODO add your handling code here:
        accaoTransferencia();
    }//GEN-LAST:event_txtTransferenciaActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton3ActionPerformed
    {//GEN-HEADEREND:event_jButton3ActionPerformed

        procedimento_finalizar();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton6ActionPerformed
    {//GEN-HEADEREND:event_jButton6ActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_jButton6ActionPerformed

    private void txtDinheiroMousePressed(java.awt.event.MouseEvent evt)//GEN-FIRST:event_txtDinheiroMousePressed
    {//GEN-HEADEREND:event_txtDinheiroMousePressed
        // TODO add your handling code here:
//        accaoDinheiro();
    }//GEN-LAST:event_txtDinheiroMousePressed

    private void txtCartaoMousePressed(java.awt.event.MouseEvent evt)//GEN-FIRST:event_txtCartaoMousePressed
    {//GEN-HEADEREND:event_txtCartaoMousePressed
        // TODO add your handling code here:
        accaoCartao();
    }//GEN-LAST:event_txtCartaoMousePressed

    private void txtTransferenciaMousePressed(java.awt.event.MouseEvent evt)//GEN-FIRST:event_txtTransferenciaMousePressed
    {//GEN-HEADEREND:event_txtTransferenciaMousePressed
        // TODO add your handling code here:
        accaoTransferencia();
    }//GEN-LAST:event_txtTransferenciaMousePressed

    private void txtDinheiroMouseEntered(java.awt.event.MouseEvent evt)//GEN-FIRST:event_txtDinheiroMouseEntered
    {//GEN-HEADEREND:event_txtDinheiroMouseEntered
        // TODO add your handling code here:
        accaoDinheiro();
    }//GEN-LAST:event_txtDinheiroMouseEntered

    private void txtCartaoMouseEntered(java.awt.event.MouseEvent evt)//GEN-FIRST:event_txtCartaoMouseEntered
    {//GEN-HEADEREND:event_txtCartaoMouseEntered
        // TODO add your handling code here:
        accaoCartao();
    }//GEN-LAST:event_txtCartaoMouseEntered

    private void txtTransferenciaMouseEntered(java.awt.event.MouseEvent evt)//GEN-FIRST:event_txtTransferenciaMouseEntered
    {//GEN-HEADEREND:event_txtTransferenciaMouseEntered
        // TODO add your handling code here:
        accaoTransferencia();
    }//GEN-LAST:event_txtTransferenciaMouseEntered

    private void txtDinheiroMouseExited(java.awt.event.MouseEvent evt)//GEN-FIRST:event_txtDinheiroMouseExited
    {//GEN-HEADEREND:event_txtDinheiroMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDinheiroMouseExited

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
                if ( "Nimbus".equals( info.getName() ) )
                {
                    javax.swing.UIManager.setLookAndFeel( info.getClassName() );
                    break;
                }
            }
        }
        catch ( ClassNotFoundException ex )
        {
            java.util.logging.Logger.getLogger( FormaPagamentoGoldVisao.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        catch ( InstantiationException ex )
        {
            java.util.logging.Logger.getLogger( FormaPagamentoGoldVisao.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        catch ( IllegalAccessException ex )
        {
            java.util.logging.Logger.getLogger( FormaPagamentoGoldVisao.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        catch ( javax.swing.UnsupportedLookAndFeelException ex )
        {
            java.util.logging.Logger.getLogger( FormaPagamentoGoldVisao.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater( new Runnable()
        {
            public void run()
            {
                FormaPagamentoGoldVisao dialog = new FormaPagamentoGoldVisao( new javax.swing.JFrame(), true, BDConexao.getInstancia() );
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

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    public static javax.swing.JLabel lb_total_geral;
    public static javax.swing.JLabel lb_total_geral1;
    public static javax.swing.JLabel lb_total_geral2;
    public static javax.swing.JLabel lb_total_geral3;
    public static javax.swing.JLabel lb_troco;
    public static javax.swing.JTextField txtCartao;
    public static javax.swing.JTextField txtDinheiro;
    public static javax.swing.JTextField txtTransferencia;
    // End of variables declaration//GEN-END:variables

    private double getValor( JTextField field )
    {

        try
        {
            // aceita 30400, 30.400, 30.400,00 e 30400,50 (como os operadores escrevem)
            return kitanda.util.CfMethods.parseMoedaFormatadaNovo( field.getText() );
        }
        catch ( Exception e )
        {
            return 0;
        }

    }

    private void accaoDinheiro()
    {

        double valor_factura = CfMethods.parseMoedaFormatada( lb_total_geral.getText() );
        double valorDinehiro = getValor( txtDinheiro );

        if ( valorDinehiro >= valor_factura )
        {

            txtCartao.setText( "" );
            txtTransferencia.setText( "" );
            txtCartao.setEnabled( false );
            txtTransferencia.setEnabled( false );

        }
        else
        {
            txtCartao.setEnabled( true );
            txtTransferencia.setEnabled( true );

        }
        setTroco();
    }

    private void accaoCartao()
    {
        double valor_factura = CfMethods.parseMoedaFormatada( lb_total_geral.getText() );
        double valorDinehiro = getValor( txtDinheiro );
        double valorTransferencia = getValor( txtTransferencia );

        double valorEmFalta = valor_factura - valorDinehiro - valorTransferencia;

        double valorCartao = getValor( txtCartao );

        if ( valorCartao == valorEmFalta )
        {
            txtDinheiro.setEnabled( false );
            txtTransferencia.setEnabled( false );
        }
        else if ( valorCartao <= valorEmFalta )
        {
            txtDinheiro.setEnabled( true );
            txtTransferencia.setEnabled( true );

        }
        else
        {

            txtCartao.setText( "" );
            JOptionPane.showMessageDialog( null, "Valor excedido para o cartão.", "Aviso", JOptionPane.WARNING_MESSAGE );
        }

        System.out.println( "valor em falta: " + valorEmFalta );

        setTroco();

    }

    private void accaoTransferencia()
    {
        double valor_factura = CfMethods.parseMoedaFormatada( lb_total_geral.getText() );

        double valorDinehiro = getValor( txtDinheiro );
        double valorCartao = getValor( txtCartao );

        double valorEmFalta = valor_factura - valorDinehiro - valorCartao;

        double valorTransferencia = getValor( txtTransferencia );

        if ( valorTransferencia == valorEmFalta )
        {
            txtDinheiro.setEnabled( false );
            txtCartao.setEnabled( false );
        }
        else if ( valorTransferencia <= valorEmFalta )
        {
            txtDinheiro.setEnabled( true );
            txtCartao.setEnabled( true );
        }
        else
        {
            txtTransferencia.setText( "" );
            JOptionPane.showMessageDialog( null, "Valor excedido para a transferência.", "Aviso", JOptionPane.WARNING_MESSAGE );
        }

        setTroco();

    }

    private void setTroco()
    {
        double valor_factura = CfMethods.parseMoedaFormatada( lb_total_geral.getText() );
        double valor = getValor( txtDinheiro ) + getValor( txtCartao ) + getValor( txtTransferencia ) - valor_factura;
        lb_troco.setText( CfMethods.formatarComoMoeda( valor ) );

    }

    private void procedimento_finalizar()
    {
        double troco = CfMethods.parseMoedaFormatada( lb_troco.getText() );
        if ( troco >= 0 )
        {

            dispose();
            RecolhaPedidosVisao.procedimento_converter_factura();
        }
        else
        {
            JOptionPane.showMessageDialog( null, "Valor entregue inferior ao total geral.", "Aviso", JOptionPane.WARNING_MESSAGE );
        }

    }

}
