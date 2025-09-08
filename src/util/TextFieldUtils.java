/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

/**
 *
 * @author Domingos Dala Vunge
 */
import java.awt.Component;
import java.math.BigDecimal;
import java.math.RoundingMode;
import javax.swing.*;
import javax.swing.table.TableColumn;
import javax.swing.text.*;

public class TextFieldUtils
{

    public static void configurarCampoDecimal( JTextField textField, int casasDecimais )
    {
        ( (AbstractDocument) textField.getDocument() ).setDocumentFilter( new DocumentFilter()
        {
            @Override
            public void insertString( FilterBypass fb, int offset, String string, AttributeSet attr )
                    throws BadLocationException
            {
                String atual = fb.getDocument().getText( 0, fb.getDocument().getLength() );
                String futuro = new StringBuilder( atual ).insert( offset, string ).toString();

                if ( string != null && isValid( futuro, casasDecimais ) )
                {
                    super.insertString( fb, offset, string, attr );
                }
            }

            @Override
            public void replace( FilterBypass fb, int offset, int length, String text, AttributeSet attrs )
                    throws BadLocationException
            {
                String atual = fb.getDocument().getText( 0, fb.getDocument().getLength() );
                String futuro = new StringBuilder( atual ).replace( offset, offset + length, text ).toString();

                if ( text != null && isValid( futuro, casasDecimais ) )
                {
                    super.replace( fb, offset, length, text, attrs );
                }
            }

            private boolean isValid( String futuro, int casasDecimais )
            {
                // Não permitir vírgula
                if ( futuro.contains( "," ) )
                {
                    return false;
                }

                // Só números e ponto
                if ( !futuro.matches( "[0-9.]*" ) )
                {
                    return false;
                }

                // Não permitir começar com ponto
                if ( futuro.startsWith( "." ) )
                {
                    return false;
                }

                // Só permitir um ponto decimal
                if ( futuro.chars().filter( ch -> ch == '.' ).count() > 1 )
                {
                    return false;
                }

                // Limite de casas decimais
                if ( casasDecimais > 0 && futuro.contains( "." ) )
                {
                    String[] partes = futuro.split( "\\." );
                    if ( partes.length > 1 && partes[ 1 ].length() > casasDecimais )
                    {
                        return false;
                    }
                }

                return true;
            }
        } );
    }

    public static void configurarColunaDecimal( JTable tabela, int colIndex, int casasDecimais )
    {
        TableColumn coluna = tabela.getColumnModel().getColumn( colIndex );

        DefaultCellEditor editor = new DefaultCellEditor( new JTextField() )
        {
            {
                JTextField textField = (JTextField) getComponent();
                TextFieldUtils.configurarCampoDecimal( textField, casasDecimais );
            }

            @Override
            public Component getTableCellEditorComponent( JTable table, Object value,
                    boolean isSelected, int row, int column )
            {
                JTextField textField = (JTextField) getComponent();

                // 🔹 sempre inicializar limpo
                textField.setText( "" );

                // 🔹 carregar o valor atual da célula
                if ( value != null )
                {
                    textField.setText( value.toString().trim() );
                }

                // 🔹 se continuar vazio → mostrar "0"
                if ( textField.getText().isEmpty() )
                {
                    textField.setText( "0" );
                }

                return textField;
            }

            @Override
            public Object getCellEditorValue()
            {
                String text = ( (JTextField) getComponent() ).getText();

                if ( text == null || text.trim().isEmpty() )
                {
                    return BigDecimal.ZERO.setScale( casasDecimais, RoundingMode.HALF_UP );
                }

                try
                {
                    text = text.replace( ",", "." );
                    BigDecimal valor = new BigDecimal( text );
                    return valor.setScale( casasDecimais, RoundingMode.HALF_UP );
                }
                catch ( NumberFormatException e )
                {
                    return BigDecimal.ZERO.setScale( casasDecimais, RoundingMode.HALF_UP );
                }
            }
        };

        coluna.setCellEditor( editor );
    }

}
