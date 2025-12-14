package visao;

import java.awt.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.*;

import kitanda.util.CfMethods;

public class TelaCliente extends JFrame
{

    private JTable tabela;
    private JLabel lblTotal;

    private final DefaultTableModel modeloPrincipal;
    private final DefaultTableModel modeloCliente;

    // ==========================================================
    // CONSTRUTOR
    // ==========================================================
    public TelaCliente( DefaultTableModel modeloPrincipal )
    {
        super( "Tela Cliente" );

        this.modeloPrincipal = modeloPrincipal;
        this.modeloCliente = criarModeloCliente();

        configurarJanela();
        montarLayout();
        sincronizarModelo();
        atualizarTotal();

        // Espelhamento em tempo real
        modeloPrincipal.addTableModelListener( ( TableModelEvent e ) ->
        {
            sincronizarModelo();
            atualizarTotal();
        } );

        setVisible( true );
    }

    // ==========================================================
    // CONFIGURAÇÃO DA JANELA
    // ==========================================================
    private void configurarJanela()
    {
        setDefaultCloseOperation( DISPOSE_ON_CLOSE );
        setSize( 1200, 700 );
        setLayout( new BorderLayout() );
        setLocationRelativeTo( null );
    }

    // ==========================================================
    // LAYOUT
    // ==========================================================
    private void montarLayout()
    {
        tabela = criarTabela();

        JScrollPane scroll = new JScrollPane( tabela );
        scroll.setBorder( BorderFactory.createEmptyBorder() );
        scroll.getViewport().setBackground( Color.WHITE );

        add( criarPainelPublicidade(), BorderLayout.WEST );
        add( scroll, BorderLayout.CENTER );
        add( criarRodapeTotal(), BorderLayout.SOUTH );
    }

    // ==========================================================
    // MODELO DO CLIENTE (COLUNAS VISÍVEIS)
    // ==========================================================
    private DefaultTableModel criarModeloCliente()
    {
        DefaultTableModel m = new DefaultTableModel()
        {
            @Override
            public boolean isCellEditable( int row, int column )
            {
                return false;
            }
        };

        m.addColumn( "Designação" );
        m.addColumn( "Preço" );
        m.addColumn( "Qtd" );
        m.addColumn( "Taxa" );
        m.addColumn( "Total" );

        return m;
    }

    // ==========================================================
    // SINCRONIZAÇÃO (ESPELHO PURO)
    // ==========================================================
    private void sincronizarModelo()
    {
        modeloCliente.setRowCount( 0 );

        int cDesc = idx( "Designacao" );
        int cPreco = idx( "Preco" );
        int cQtd = idx( "Qtd" );
        int cTaxa = idx( "Taxa" );
        int cTotal = idx( "Total" );

        for ( int r = 0; r < modeloPrincipal.getRowCount(); r++ )
        {
            modeloCliente.addRow( new Object[]
            {
                modeloPrincipal.getValueAt( r, cDesc ),
                modeloPrincipal.getValueAt( r, cPreco ),
                modeloPrincipal.getValueAt( r, cQtd ),
                modeloPrincipal.getValueAt( r, cTaxa ),
                modeloPrincipal.getValueAt( r, cTotal )
            } );
        }
    }

    // ==========================================================
    // LOCALIZA COLUNA PELO NOME
    // ==========================================================
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

    // ==========================================================
    // TABELA
    // ==========================================================
    private JTable criarTabela()
    {
        JTable t = new JTable( modeloCliente );

        t.setRowHeight( 28 );
        t.setFont( new Font( "Segoe UI", Font.PLAIN, 17 ) );
        t.setShowGrid( false );
        t.setIntercellSpacing( new Dimension( 0, 0 ) );
        t.setFillsViewportHeight( true );

        JTableHeader h = t.getTableHeader();
        h.setFont( new Font( "Segoe UI", Font.BOLD, 16 ) );
        h.setReorderingAllowed( false );

        aplicarRenderizadores( t );
        ajustarColunas( t );

        return t;
    }

    // ==========================================================
    // RENDERIZADORES
    // ==========================================================
    private void aplicarRenderizadores( JTable t )
    {
        TableColumnModel cm = t.getColumnModel();

        cm.getColumn( 1 ).setCellRenderer( new Money3Renderer() ); // Preço
        cm.getColumn( 2 ).setCellRenderer( new QtdRenderer() );    // Qtd
        cm.getColumn( 3 ).setCellRenderer( new Money2Renderer() );// Taxa
        cm.getColumn( 4 ).setCellRenderer( new Money2Renderer() );// Total
    }

    // ==========================================================
    // RENDER MONETÁRIO – 3 CASAS (PREÇO)
    // ==========================================================
    private static class Money3Renderer extends DefaultTableCellRenderer
    {

        private static final DecimalFormat DF;

        static
        {
            DecimalFormatSymbols s = new DecimalFormatSymbols();
            s.setDecimalSeparator( ',' );
            s.setGroupingSeparator( ' ' );

            DF = new DecimalFormat( "#,##0.000", s );
            DF.setGroupingUsed( true );
            DF.setMinimumFractionDigits( 3 );
            DF.setMaximumFractionDigits( 3 );
        }

        public Money3Renderer()
        {
            setHorizontalAlignment( RIGHT );
        }

        @Override
        protected void setValue( Object value )
        {
            if ( value == null )
            {
                setText( "" );
                return;
            }

            try
            {
                BigDecimal v = new BigDecimal(
                        value.toString().replace( ",", "." ) );
                setText( DF.format( v ) );
            }
            catch ( Exception e )
            {
                setText( value.toString() );
            }
        }
    }

    // ==========================================================
    // RENDER MONETÁRIO – 2 CASAS (TAXA / TOTAL)
    // ==========================================================
    private static class Money2Renderer extends DefaultTableCellRenderer
    {

        private static final DecimalFormat DF;

        static
        {
            DecimalFormatSymbols s = new DecimalFormatSymbols();
            s.setDecimalSeparator( ',' );
            s.setGroupingSeparator( ' ' );

            DF = new DecimalFormat( "#,##0.00", s );
            DF.setGroupingUsed( true );
            DF.setMinimumFractionDigits( 2 );
            DF.setMaximumFractionDigits( 2 );
        }

        public Money2Renderer()
        {
            setHorizontalAlignment( RIGHT );
        }

        @Override
        protected void setValue( Object value )
        {
            if ( value == null )
            {
                setText( "" );
                return;
            }

            try
            {
                BigDecimal v = new BigDecimal(
                        value.toString().replace( ",", "." ) );
                setText( DF.format( v ) );
            }
            catch ( Exception e )
            {
                setText( value.toString() );
            }
        }
    }

    // ==========================================================
    // RENDER QTD – ATÉ 3 CASAS (SEM ARREDONDAR)
    // ==========================================================
    private static class QtdRenderer extends DefaultTableCellRenderer
    {

        public QtdRenderer()
        {
            setHorizontalAlignment( RIGHT );
        }

        @Override
        protected void setValue( Object value )
        {
            if ( value == null )
            {
                setText( "" );
                return;
            }

            String txt = value.toString().replace( ',', '.' );

            if ( txt.contains( "." ) )
            {
                int p = txt.indexOf( "." );
                int max = Math.min( p + 4, txt.length() );
                txt = txt.substring( 0, max );
            }

            setText( txt );
        }
    }

    // ==========================================================
    // AJUSTE DE COLUNAS
    // ==========================================================
    private void ajustarColunas( JTable t )
    {
        t.getColumnModel().getColumn( 0 ).setPreferredWidth( 320 );
        t.getColumnModel().getColumn( 1 ).setPreferredWidth( 120 );
        t.getColumnModel().getColumn( 2 ).setPreferredWidth( 80 );
        t.getColumnModel().getColumn( 3 ).setPreferredWidth( 70 );
        t.getColumnModel().getColumn( 4 ).setPreferredWidth( 130 );
    }

    // ==========================================================
    // PAINEL PUBLICIDADE
    // ==========================================================
    private JPanel criarPainelPublicidade()
    {
        JPanel p = new JPanel( new BorderLayout() );
        p.setPreferredSize( new Dimension( 350, 0 ) );
        p.setBackground( Color.BLACK );

        JLabel topo = new JLabel( "PUBLICIDADE", SwingConstants.CENTER );
        topo.setFont( new Font( "Segoe UI", Font.BOLD, 26 ) );
        topo.setForeground( Color.WHITE );
        topo.setBorder( BorderFactory.createEmptyBorder( 30, 10, 20, 10 ) );

        JLabel msg = new JLabel(
                "<html><center>"
                + "<span style='font-size:22px;'>Carne fresca todos os dias</span><br>"
                + "<span style='font-size:18px;'>Qualidade garantida</span>"
                + "</center></html>",
                SwingConstants.CENTER
        );
        msg.setForeground( Color.LIGHT_GRAY );

        p.add( topo, BorderLayout.NORTH );
        p.add( msg, BorderLayout.CENTER );

        return p;
    }

    // ==========================================================
    // RODAPÉ TOTAL
    // ==========================================================
    private JLabel criarRodapeTotal()
    {
        lblTotal = new JLabel( "TOTAL: 0,00 AOA", SwingConstants.RIGHT );
        lblTotal.setFont( new Font( "Segoe UI", Font.BOLD, 40 ) );
        lblTotal.setForeground( new Color( 0, 255, 128 ) );
        lblTotal.setBackground( Color.BLACK );
        lblTotal.setOpaque( true );
        lblTotal.setBorder( BorderFactory.createEmptyBorder( 10, 20, 10, 20 ) );
        return lblTotal;
    }

    private void atualizarTotal()
    {
        double total = 0;

        for ( int i = 0; i < modeloCliente.getRowCount(); i++ )
        {
            Object v = modeloCliente.getValueAt( i, 4 );
            if ( v != null )
            {
                try
                {
                    total += Double.parseDouble(
                            v.toString().replace( ",", "." ) );
                }
                catch ( Exception e )
                {
                }
            }
        }

        lblTotal.setText( "TOTAL: " + CfMethods.formatarComoMoeda( total ) );
    }
}
