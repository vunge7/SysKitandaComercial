package visao;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import kitanda.util.CfMethods;

public class TelaCliente extends JFrame
{

    private JTable tabela;
    private JLabel lblTotal;

    public TelaCliente( DefaultTableModel modeloRecebido )
    {

        super( "Tela Cliente" );
        setDefaultCloseOperation( DISPOSE_ON_CLOSE );
        setLayout( new BorderLayout() );
        setSize( 1200, 700 );
        setLocationRelativeTo( null );

        // ======================================================
        // 1) REMOVER LINHAS VAZIAS DO MODELO
        // ======================================================
        for ( int i = modeloRecebido.getRowCount() - 1; i >= 0; i-- )
        {
            boolean vazia = true;

            for ( int c = 0; c < modeloRecebido.getColumnCount(); c++ )
            {
                Object val = modeloRecebido.getValueAt( i, c );
                if ( val != null && !val.toString().trim().isEmpty() )
                {
                    vazia = false;
                    break;
                }
            }
            if ( vazia )
            {
                modeloRecebido.removeRow( i );
            }
        }

        // ======================================================
        // 2) TABELA COMPLETA (SEM OCULTAR COLUNAS)
        // ======================================================
        tabela = new JTable( modeloRecebido );
        tabela.setRowHeight( 32 );
        tabela.setFont( new Font( "Segoe UI", Font.PLAIN, 20 ) );
        tabela.setShowHorizontalLines( false );
        tabela.setShowVerticalLines( false );
        tabela.setIntercellSpacing( new Dimension( 0, 0 ) );

        // Render moderno
        tabela.setDefaultRenderer( Object.class, new LinhaRenderer() );

        JScrollPane scroll = new JScrollPane( tabela );
        scroll.setBorder( null );
        scroll.getViewport().setOpaque( false );

        add( scroll, BorderLayout.CENTER );

        // ======================================================
        // 3) CABEÇALHO PROFISSIONAL
        // ======================================================
        tabela.getTableHeader().setPreferredSize( new Dimension( 0, 40 ) );
        tabela.getTableHeader().setFont( new Font( "Segoe UI", Font.BOLD, 22 ) );
        tabela.getTableHeader().setDefaultRenderer( new HeaderRenderer() );
        tabela.getTableHeader().setReorderingAllowed( false );

        // ======================================================
        // 4) AJUSTAR LARGURA DAS COLUNAS IMPORTANTES
        // ======================================================
        ajustarColunas();

        // ======================================================
        // 5) TOTAL DESTACADO
        // ======================================================
        lblTotal = new JLabel( "TOTAL: 0,00 AOA", SwingConstants.RIGHT );
        lblTotal.setFont( new Font( "Segoe UI", Font.BOLD, 40 ) );
        lblTotal.setForeground( new Color( 0, 255, 128 ) );
        lblTotal.setBackground( Color.BLACK );
        lblTotal.setOpaque( true );
        lblTotal.setBorder( BorderFactory.createEmptyBorder( 10, 20, 10, 20 ) );

        add( lblTotal, BorderLayout.SOUTH );

        modeloRecebido.addTableModelListener( e -> atualizarTotal() );
        atualizarTotal();

        setVisible( true );
    }

    // ============================================================
    // RENDER DAS LINHAS COM DEGRADÊ E FORMATAÇÃO PROFISSIONAL
    // ============================================================
    private class LinhaRenderer extends DefaultTableCellRenderer
    {

        @Override
        public Component getTableCellRendererComponent( JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column )
        {

            super.getTableCellRendererComponent( table, value, isSelected, hasFocus, row, column );

            setOpaque( false );

            // Formatação automática de números
            if ( value instanceof Number )
            {
                setHorizontalAlignment( RIGHT );
                double n = ( ( Number ) value ).doubleValue();
                setText( String.format( "%,.2f", n ) );
            }
            else
            {
                setHorizontalAlignment( LEFT );
            }

            setFont( new Font( "Segoe UI", Font.PLAIN, 20 ) );
            setForeground( Color.BLACK );

            return this;
        }

        @Override
        protected void paintComponent( Graphics g )
        {
            Graphics2D g2 = ( Graphics2D ) g;
            g2.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );

            int realRow = tabela.rowAtPoint( new Point( 0, getY() ) );

            if ( realRow == -1 )
            {
                super.paintComponent( g );
                return;
            }

            Color c1, c2;

            if ( tabela.isRowSelected( realRow ) )
            {
                c1 = new Color( 0, 255, 255 );
                c2 = new Color( 0, 150, 255 );
            }
            else if ( realRow % 2 == 0 )
            {
                c1 = new Color( 210, 240, 255 );
                c2 = new Color( 180, 220, 255 );
            }
            else
            {
                c1 = new Color( 230, 250, 255 );
                c2 = new Color( 200, 230, 255 );
            }

            g2.setPaint( new GradientPaint( 0, 0, c1, getWidth(), getHeight(), c2 ) );
            g2.fillRect( 0, 0, getWidth(), getHeight() );

            super.paintComponent( g );
        }
    }

    // ============================================================
    // CABEÇALHO MODERNO
    // ============================================================
    private class HeaderRenderer extends DefaultTableCellRenderer
    {

        public HeaderRenderer()
        {
            setHorizontalAlignment( CENTER );
            setForeground( Color.WHITE );
        }

        @Override
        protected void paintComponent( Graphics g )
        {
            Graphics2D g2 = ( Graphics2D ) g;

            g2.setPaint( new GradientPaint(
                    0, 0, new Color( 100, 100, 100 ),
                    0, getHeight(), new Color( 60, 60, 60 ) ) );

            g2.fillRect( 0, 0, getWidth(), getHeight() );
            setFont( new Font( "Segoe UI", Font.BOLD, 22 ) );

            super.paintComponent( g );
        }
    }

    // ============================================================
    // AJUSTE DE LARGURAS
    // ============================================================
    private void ajustarColunas()
    {
        for ( int i = 0; i < tabela.getColumnCount(); i++ )
        {
            tabela.getColumnModel().getColumn( i ).setPreferredWidth( 150 );
        }

        // Ajuste especial
        if ( tabela.getColumnCount() > 4 )
        {
            tabela.getColumnModel().getColumn( 4 ).setPreferredWidth( 90 );
        }
    }

    // ============================================================
    // TOTAL
    // ============================================================
    private void atualizarTotal()
    {
        double total = 0;

        int colTotal = tabela.getColumnCount() - 1;

        for ( int i = 0; i < tabela.getRowCount(); i++ )
        {
            Object valor = tabela.getValueAt( i, colTotal );
            if ( valor != null )
            {
                try
                {
                    total += Double.parseDouble( valor.toString().replace( ",", "." ) );
                }
                catch ( Exception ignored )
                {
                }
            }
        }

        lblTotal.setText( "TOTAL: " + CfMethods.formatarComoMoeda( total ) + " AOA" );
    }
}
