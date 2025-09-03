package util;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class CalculadoraPrecoHelper
{

    private final JTextField txtPrecoCompra;
    private final JTextField txtPrecoVenda;
    private final JTextField txtPercentagem;
    private final int casadecimais;
    private boolean updating;

    public CalculadoraPrecoHelper( JTextField txtPrecoCompra,
            JTextField txtPrecoVenda,
            JTextField txtPercentagem,
            int casadecimais )
    {
        this.txtPrecoCompra = txtPrecoCompra;
        this.txtPrecoVenda = txtPrecoVenda;
        this.txtPercentagem = txtPercentagem;
        this.casadecimais = casadecimais;
    }

    public void inicializar()
    {
        // Filtro de entrada (só números e um ".")
        setNumericFilter( txtPrecoCompra );
        setNumericFilter( txtPrecoVenda );
        setNumericFilter( txtPercentagem );

        DocumentListener listener = new DocumentListener()
        {
            @Override
            public void insertUpdate( DocumentEvent e )
            {
                calcular( e.getDocument() );
            }

            @Override
            public void removeUpdate( DocumentEvent e )
            {
                calcular( e.getDocument() );
            }

            @Override
            public void changedUpdate( DocumentEvent e )
            {
                calcular( e.getDocument() );
            }
        };

        txtPrecoCompra.getDocument().addDocumentListener( listener );
        txtPrecoVenda.getDocument().addDocumentListener( listener );
        txtPercentagem.getDocument().addDocumentListener( listener );

        // Formata para 2 casas ao sair do campo
        FocusAdapter format2 = new FocusAdapter()
        {
            @Override
            public void focusLost( FocusEvent e )
            {
                JTextField f = (JTextField) e.getSource();
                BigDecimal v = parse( f.getText() );
                if ( v != null )
                {
                    f.setText( scale2( v ).toPlainString() );
                }
            }
        };
        txtPrecoCompra.addFocusListener( format2 );
        txtPrecoVenda.addFocusListener( format2 );
        txtPercentagem.addFocusListener( format2 );
    }

    private void calcular( Document srcDoc )
    {
        if ( updating )
        {
            return;
        }
        updating = true;
        try
        {
            BigDecimal compra = parse( txtPrecoCompra.getText() );
            BigDecimal venda = parse( txtPrecoVenda.getText() );
            BigDecimal perc = parse( txtPercentagem.getText() );

            Document compraDoc = txtPrecoCompra.getDocument();
            Document vendaDoc = txtPrecoVenda.getDocument();
            Document percDoc = txtPercentagem.getDocument();

            // 1) Compra + Venda → calcula Percentagem
            if ( isPositive( compra ) && ( srcDoc == compraDoc || srcDoc == vendaDoc ) )
            {
                if ( isPositive( venda ) )
                {
                    BigDecimal diff = venda.subtract( compra );
                    BigDecimal pct = diff.divide( compra, casadecimais, RoundingMode.HALF_UP )
                            .multiply( BigDecimal.valueOf( 100 ) );
                    setIfChanged( txtPercentagem, scale3( pct ).toPlainString() );
                }
                else
                {
                    setIfChanged( txtPercentagem, "" );
                }
            }
            // 2) Compra + Percentagem → calcula Venda
            else if ( isPositive( compra ) && ( srcDoc == compraDoc || srcDoc == percDoc ) )
            {
                if ( perc != null )
                {
                    BigDecimal vendaCalc = compra.multiply(
                            BigDecimal.ONE.add( perc.divide( BigDecimal.valueOf( 100 ), casadecimais, RoundingMode.HALF_UP ) )
                    );
                    setIfChanged( txtPrecoVenda, scale2( vendaCalc ).toPlainString() );
                }
                else
                {
                    setIfChanged( txtPrecoVenda, "" );
                }
            }
            // 3) Venda + Percentagem → calcula Compra
            else if ( ( srcDoc == vendaDoc || srcDoc == percDoc ) )
            {
                if ( isPositive( venda ) && perc != null )
                {
                    BigDecimal divisor = BigDecimal.ONE.add( perc.divide( BigDecimal.valueOf( 100 ), casadecimais, RoundingMode.HALF_UP ) );
                    if ( divisor.compareTo( BigDecimal.ZERO ) != 0 )
                    {
                        BigDecimal compraCalc = venda.divide( divisor, casadecimais, RoundingMode.HALF_UP );
                        setIfChanged( txtPrecoCompra, scale2( compraCalc ).toPlainString() );
                    }
                }
                else
                {
                    setIfChanged( txtPrecoCompra, "" );
                }
            }
        }
        finally
        {
            updating = false;
        }
    }

    private static void setIfChanged( JTextField field, String newVal )
    {
        if ( !newVal.equals( field.getText() ) )
        {
            field.setText( newVal );
        }
    }

    private static boolean isPositive( BigDecimal x )
    {
        return x != null && x.compareTo( BigDecimal.ZERO ) > 0;
    }

    private BigDecimal scale2( BigDecimal x )
    {
        return x.setScale( casadecimais, RoundingMode.HALF_UP );
    }
    private BigDecimal scale3( BigDecimal x )
    {
        return x.setScale( 2, RoundingMode.HALF_UP );
    }

    private static BigDecimal parse( String s )
    {
        if ( s == null )
        {
            return null;
        }
        s = s.trim();
        if ( s.isEmpty() )
        {
            return null;
        }
        if ( s.equals( "." ) || s.endsWith( "." ) )
        {
            return null;
        }
        if ( s.indexOf( ',' ) >= 0 )
        {
            return null; // vírgula não é permitida
        }
        try
        {
            return new BigDecimal( s );
        }
        catch ( NumberFormatException e )
        {
            return null;
        }
    }

    private static void setNumericFilter( JTextField field )
    {
        ( (AbstractDocument) field.getDocument() ).setDocumentFilter( new NumericDocumentFilter() );
    }

    // Só dígitos e um único ponto; sem vírgula; não começa com ponto
    static class NumericDocumentFilter extends DocumentFilter
    {

        @Override
        public void insertString( FilterBypass fb, int offset, String string, AttributeSet attr )
                throws BadLocationException
        {
            if ( string == null )
            {
                return;
            }
            String cur = fb.getDocument().getText( 0, fb.getDocument().getLength() );
            String newText = new StringBuilder( cur ).insert( offset, string ).toString();
            if ( isValid( newText ) )
            {
                super.insertString( fb, offset, string, attr );
            }
        }

        @Override
        public void replace( FilterBypass fb, int offset, int length, String text, AttributeSet attrs )
                throws BadLocationException
        {
            String cur = fb.getDocument().getText( 0, fb.getDocument().getLength() );
            StringBuilder sb = new StringBuilder( cur );
            sb.replace( offset, offset + length, text == null ? "" : text );
            String newText = sb.toString();
            if ( isValid( newText ) )
            {
                super.replace( fb, offset, length, text, attrs );
            }
        }

        @Override
        public void remove( FilterBypass fb, int offset, int length ) throws BadLocationException
        {
            super.remove( fb, offset, length );
        }

        private boolean isValid( String s )
        {
            if ( s.isEmpty() )
            {
                return true;
            }
            if ( s.startsWith( "." ) )
            {
                return false;
            }
            if ( s.indexOf( ',' ) >= 0 )
            {
                return false;
            }

            int dots = 0;
            for ( int i = 0; i < s.length(); i++ )
            {
                char ch = s.charAt( i );
                if ( Character.isDigit( ch ) )
                {
                    continue;
                }
                if ( ch == '.' )
                {
                    dots++;
                    if ( dots > 1 )
                    {
                        return false;
                    }
                }
                else
                {
                    return false;
                }
            }
            return true;
        }
    }
}
