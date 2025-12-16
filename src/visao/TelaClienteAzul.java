package visao;

import java.awt.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.*;

import kitanda.util.CfMethods;

public class TelaClienteAzul extends JFrame
{

    // ======================================================
    // MODELOS
    // ======================================================
    private final DefaultTableModel modeloPrincipal;
    private final DefaultTableModel modeloCliente;

    // ======================================================
    // COMPONENTES
    // ======================================================
    private JTable tabela;

    private JLabel lblIliquido;
    private JLabel lblIva;
    private JLabel lblDesconto;
    private JLabel lblTotalGeral;

    // ======================================================
    // CONTROLE VISUAL
    // ======================================================
    private int ultimaLinhaPiscar = -1;

    // ======================================================
    // CORES (DEGRADÊ AZUL)
    // ======================================================
    private static final Color AZUL_CLARO = new Color( 0x02, 0xEE, 0xF9 );
    private static final Color AZUL_ESCURO = new Color( 0x00, 0x0A, 0x0B );
    private static final Color AZUL_TOTAL = new Color( 0x00, 0xC8, 0xFF );
    private static final Color COR_BLINK = new Color( 255, 255, 170 );

    private static final Font FONTE_TEXTO = new Font( "Segoe UI", Font.PLAIN, 17 );
    private static final Font FONTE_NUMERO = new Font( "Consolas", Font.PLAIN, 17 );

    // ======================================================
    // CONSTRUTOR
    // ======================================================
    public TelaClienteAzul( DefaultTableModel modeloPrincipal )
    {
        super( "Tela Cliente" );

        this.modeloPrincipal = modeloPrincipal;
        this.modeloCliente = criarModeloCliente();

        configurarJanela();
        montarLayout();
        sincronizarModelo();
        atualizarTotais();

        modeloPrincipal.addTableModelListener( this::onModeloMudou );

        setVisible( true );
    }

    // ======================================================
    // EVENTO DO MODELO PRINCIPAL
    // ======================================================
    private void onModeloMudou( TableModelEvent e )
    {
        int antes = modeloCliente.getRowCount();

        sincronizarModelo();
        atualizarTotais();

        if ( modeloCliente.getRowCount() > antes )
        {
            ultimaLinhaPiscar = modeloCliente.getRowCount() - 1;
            Toolkit.getDefaultToolkit().beep();
            scrollParaUltimaLinha();
            iniciarBlink();
        }
    }

    // ======================================================
    // JANELA
    // ======================================================
    private void configurarJanela()
    {
        setDefaultCloseOperation( DISPOSE_ON_CLOSE );
        setSize( 1200, 700 );
        setLocationRelativeTo( null );
        setLayout( new BorderLayout() );
    }

    // ======================================================
    // LAYOUT
    // ======================================================
    private void montarLayout()
    {
        tabela = criarTabela();

        JScrollPane scroll = new JScrollPane( tabela );
        scroll.setBorder( BorderFactory.createEmptyBorder() );

        add( criarPainelPublicidade(), BorderLayout.WEST );
        add( scroll, BorderLayout.CENTER );
        add( criarRodapeFinanceiro(), BorderLayout.SOUTH );
    }

    // ======================================================
    // MODELO CLIENTE
    // ======================================================
    private DefaultTableModel criarModeloCliente()
    {
        return new DefaultTableModel(
                new Object[]
                {
                    "Designação", "Preço", "Qtd", "Taxa", "Total"
                }, 0 )
        {
            @Override
            public boolean isCellEditable( int r, int c )
            {
                return false;
            }
        };
    }

    private void sincronizarModelo()
    {
        modeloCliente.setRowCount( 0 );

        int d = idx( "Designacao" );
        int p = idx( "Preco" );
        int q = idx( "Qtd" );
        int t = idx( "Taxa" );
        int tot = idx( "Total" );

        for ( int i = 0; i < modeloPrincipal.getRowCount(); i++ )
        {
            modeloCliente.addRow( new Object[]
            {
                modeloPrincipal.getValueAt( i, d ),
                modeloPrincipal.getValueAt( i, p ),
                modeloPrincipal.getValueAt( i, q ),
                modeloPrincipal.getValueAt( i, t ),
                modeloPrincipal.getValueAt( i, tot )
            } );
        }
    }

    private int idx( String nome )
    {
        for ( int i = 0; i < modeloPrincipal.getColumnCount(); i++ )
        {
            if ( modeloPrincipal.getColumnName( i ).equalsIgnoreCase( nome ) )
            {
                return i;
            }
        }

        throw new RuntimeException( "Coluna não encontrada: " + nome );
    }

    // ======================================================
    // TABELA
    // ======================================================
    private JTable criarTabela()
    {
        JTable t = new JTable( modeloCliente );
        t.setRowHeight( 28 );
        t.setFont( FONTE_TEXTO );
        t.setShowGrid( false );
        t.setIntercellSpacing( new Dimension( 0, 0 ) );

        JTableHeader h = t.getTableHeader();
        h.setFont( new Font( "Segoe UI", Font.BOLD, 16 ) );

        DefaultTableCellRenderer r = new LinhaRenderer();
        for ( int i = 0; i < t.getColumnCount(); i++ )
        {
            t.getColumnModel().getColumn( i ).setCellRenderer( r );
        }

        ajustarColunas( t );
        return t;
    }

    private class LinhaRenderer extends DefaultTableCellRenderer
    {

        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value, boolean sel,
                boolean focus, int row, int col )
        {
            super.getTableCellRendererComponent( table, value, sel, focus, row, col );

            setOpaque( true );
            setFont( col == 0 ? FONTE_TEXTO : FONTE_NUMERO );
            setHorizontalAlignment( col == 0 ? LEFT : RIGHT );

            if ( row == ultimaLinhaPiscar )
            {
                setBackground( COR_BLINK );
            }
            else
            {
                setBackground( row % 2 == 0 ? AZUL_CLARO : AZUL_ESCURO );
            }

            setForeground( row % 2 == 0 ? Color.BLACK : Color.WHITE );
            setText( formatarValor( col, value ) );

            return this;
        }
    }

    // ======================================================
    // FORMATAÇÃO
    // ======================================================
    private String formatarValor( int col, Object v )
    {
        if ( v == null )
        {
            return "";
        }

        try
        {
            BigDecimal bd = new BigDecimal( v.toString().replace( ",", "." ) );
            return ( col == 1 ) ? fmt( bd, 3 ) : fmt( bd, 2 );
        }
        catch ( Exception e )
        {
            return v.toString();
        }
    }

    private String fmt( BigDecimal v, int casas )
    {
        DecimalFormatSymbols s = new DecimalFormatSymbols();
        s.setDecimalSeparator( ',' );
        s.setGroupingSeparator( ' ' );

        StringBuilder p = new StringBuilder( "#,##0" );
        if ( casas > 0 )
        {
            p.append( "." );
            for ( int i = 0; i < casas; i++ )
            {
                p.append( "0" );
            }
        }

        return new DecimalFormat( p.toString(), s ).format( v );
    }

    private BigDecimal bd( Object o )
    {
        if ( o == null )
        {
            return BigDecimal.ZERO;
        }
        try
        {
            return new BigDecimal( o.toString().replace( ",", "." ) );
        }
        catch ( Exception e )
        {
            return BigDecimal.ZERO;
        }
    }

    // ======================================================
    // PUBLICIDADE (ESQUERDA)
    // ======================================================
    private JPanel criarPainelPublicidade()
    {
        JPanel p = new JPanel( new BorderLayout() );
        p.setPreferredSize( new Dimension( 350, 0 ) );
        p.setBackground( Color.BLACK );

        JLabel msg = new JLabel(
                "<html><center>Carne fresca todos os dias<br>Qualidade garantida</center></html>",
                SwingConstants.CENTER
        );
        msg.setForeground( Color.WHITE );
        msg.setFont( new Font( "Segoe UI", Font.BOLD, 22 ) );

        p.add( msg, BorderLayout.CENTER );
        return p;
    }

    // ======================================================
    // RODAPÉ FINANCEIRO (TODOS À DIREITA)
    // ======================================================
    private JPanel criarRodapeFinanceiro()
    {
        JPanel rodape = new JPanel( new BorderLayout() );
        rodape.setBackground( Color.BLACK );
        rodape.setBorder( BorderFactory.createEmptyBorder( 10, 20, 10, 20 ) );

        JPanel painelDireito = new JPanel();
        painelDireito.setOpaque( false );
        painelDireito.setLayout( new BoxLayout( painelDireito, BoxLayout.Y_AXIS ) );

        Font fSub = new Font( "Consolas", Font.BOLD, 16 );

        lblIliquido = criarLabelResumo( "Total Ilíquido:", fSub );
        lblIva = criarLabelResumo( "IVA:", fSub );
        lblDesconto = criarLabelResumo( "Desconto:", fSub );

        lblTotalGeral = new JLabel( "TOTAL: 0,00 AOA" );
        lblTotalGeral.setFont( new Font( "Segoe UI", Font.BOLD, 26 ) );
        lblTotalGeral.setForeground( AZUL_TOTAL );
        lblTotalGeral.setBorder( BorderFactory.createEmptyBorder( 10, 0, 0, 0 ) );

        painelDireito.add( lblIliquido );
        painelDireito.add( lblIva );
        painelDireito.add( lblDesconto );
        painelDireito.add( new JSeparator() );
        painelDireito.add( lblTotalGeral );

        rodape.add( painelDireito, BorderLayout.EAST );
        return rodape;
    }

    private JLabel criarLabelResumo( String titulo, Font f )
    {
        JLabel l = new JLabel( titulo + " 0,00 AOA" );
        l.setFont( f );
        l.setForeground( Color.WHITE );
        return l;
    }

    // ======================================================
    // CÁLCULOS
    // ======================================================
    private void atualizarTotais()
    {
        BigDecimal iliquido = BigDecimal.ZERO;
        BigDecimal iva = BigDecimal.ZERO;
        BigDecimal desconto = BigDecimal.ZERO;

        for ( int i = 0; i < modeloCliente.getRowCount(); i++ )
        {
            BigDecimal preco = bd( modeloCliente.getValueAt( i, 1 ) );
            BigDecimal qtd = bd( modeloCliente.getValueAt( i, 2 ) );
            BigDecimal taxa = bd( modeloCliente.getValueAt( i, 3 ) );

            BigDecimal base = preco.multiply( qtd );
            iliquido = iliquido.add( base );
            iva = iva.add( base.multiply( taxa ).divide( BigDecimal.valueOf( 100 ) ) );
        }

        BigDecimal totalGeral = iliquido.add( iva ).subtract( desconto );

        lblIliquido.setText( "Total Ilíquido: " + fmt( iliquido, 2 ) + " AOA" );
        lblIva.setText( "IVA: " + fmt( iva, 2 ) + " AOA" );
        lblDesconto.setText( "Desconto: " + fmt( desconto, 2 ) + " AOA" );

        lblTotalGeral.setText(
                "TOTAL: " + CfMethods.formatarComoMoeda( totalGeral.doubleValue() )
        );
    }

    // ======================================================
    // EFEITOS
    // ======================================================
    private void iniciarBlink()
    {
        Timer t = new Timer( 180, e ->
        {
            ultimaLinhaPiscar = -1;
            tabela.repaint();
        } );
        t.setRepeats( false );
        t.start();
    }

    private void scrollParaUltimaLinha()
    {
        SwingUtilities.invokeLater( () ->
        {
            int r = tabela.getRowCount() - 1;
            tabela.scrollRectToVisible( tabela.getCellRect( r, 0, true ) );
        } );
    }

    // ======================================================
    // COLUNAS
    // ======================================================
    private void ajustarColunas( JTable t )
    {
        t.getColumnModel().getColumn( 0 ).setPreferredWidth( 320 );
        t.getColumnModel().getColumn( 1 ).setPreferredWidth( 120 );
        t.getColumnModel().getColumn( 2 ).setPreferredWidth( 80 );
        t.getColumnModel().getColumn( 3 ).setPreferredWidth( 70 );
        t.getColumnModel().getColumn( 4 ).setPreferredWidth( 130 );
    }
}
