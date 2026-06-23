package util.tabela_manual.render;

import java.awt.Color;
import java.awt.Component;
import java.util.Objects;
import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import util.BDConexao;
import static util.Definicoes.*;

public class RenderBusca extends DefaultTableCellRenderer
{

    private BDConexao conexao;
    private int idArmazem;

    // Cor da linha selecionada
    private final Color selectionBackground = new Color( 51, 153, 255 );
    private final Color selectionForeground = Color.WHITE;

    public RenderBusca( int idArmazem, BDConexao conexao )
    {
        this.idArmazem = idArmazem;
        this.conexao = conexao;
    }

    @Override
    public Component getTableCellRendererComponent( JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column )
    {

        Color background;
        Color foreground;

        if ( isSelected )
        {
            // Linha selecionada
            background = selectionBackground;
            foreground = selectionForeground;
        }
        else
        {
            // Linha não selecionada: cor conforme estado do produto
            if ( !Objects.isNull( value ) && !String.valueOf( value ).isEmpty() )
            {

                String estadoCritico = table.getValueAt( row, 5 ).toString();
                String qtd = table.getValueAt( row, 3 ).toString();

                if ( estadoCritico.equals( "true" ) )
                {
                    background = new Color( PRODUTO_CRITICO[ 0 ], PRODUTO_CRITICO[ 1 ], PRODUTO_CRITICO[ 2 ] );
                    foreground = new Color( COR_PRETA[ 0 ], COR_PRETA[ 1 ], COR_PRETA[ 2 ] );
                }
                else
                {
                    switch ( qtd )
                    {
                        case "0":
                            background = new Color( STOCK_VAZIO[ 0 ], STOCK_VAZIO[ 1 ], STOCK_VAZIO[ 2 ] );
                            foreground = new Color( COR_PRETA[ 0 ], COR_PRETA[ 1 ], COR_PRETA[ 2 ] );
                            break;
                        case "-":
                            background = new Color( SERVICOS[ 0 ], SERVICOS[ 1 ], SERVICOS[ 2 ] );
                            foreground = new Color( COR_PRETA[ 0 ], COR_PRETA[ 1 ], COR_PRETA[ 2 ] );
                            break;
                        default:
                            background = new Color( PRODUTO[ 0 ], PRODUTO[ 1 ], PRODUTO[ 2 ] );
                            foreground = new Color( COR_PRETA[ 0 ], COR_PRETA[ 1 ], COR_PRETA[ 2 ] );
                            break;
                    }
                }

            }
            else
            {
                // Valor nulo ou vazio
                background = new Color( PRODUTO[ 0 ], PRODUTO[ 1 ], PRODUTO[ 2 ] );
                foreground = new Color( COR_PRETA[ 0 ], COR_PRETA[ 1 ], COR_PRETA[ 2 ] );
            }
        }

        // Aplicar cores e borda
        setBackground( background );
        setForeground( foreground );
        setBorder( getCustomBorder() );

        return super.getTableCellRendererComponent( table, value, isSelected, hasFocus, row, column );
    }

    private Border getCustomBorder()
    {
        Border insideMargin = BorderFactory.createEmptyBorder( 10, 10, 10, 10 );
        Border lineBorder = BorderFactory.createLineBorder( Color.BLACK, 3 );
        return BorderFactory.createCompoundBorder( lineBorder, insideMargin );
    }
}
