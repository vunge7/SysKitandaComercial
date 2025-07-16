/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.tabela_manual.render;

import java.awt.Color;
import java.awt.Component;
import java.util.Objects;
import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import util.BDConexao;
import util.DVML;
import static util.Definicoes.*;
import util.MetodosUtil;

/**
 *
 * @author Domingos Dala Vunge
 */
public class RenderBusca extends DefaultTableCellRenderer
{

    private BDConexao conexao;
    private int idArmazem;

    public RenderBusca( int idArmazem, BDConexao conexao )
    {
        this.idArmazem = idArmazem;
        this.conexao = conexao;
    }

    @Override
    public Component getTableCellRendererComponent( JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column )
    {

        Color background = null;
        Color foreground = null;

        if ( !Objects.isNull( value ) && !String.valueOf( value ).equals( "" ) )
        {

            String estado = table.getValueAt( row, 5 ).toString(); // quantidade

            if ( estado.equals( "true" ) )
            {
                background = new Color( PRODUTO_CRITICO[ 0 ], PRODUTO_CRITICO[ 1 ], PRODUTO_CRITICO[ 2 ] );
                foreground = new Color( COR_PRETA[ 0 ], COR_PRETA[ 1 ], COR_PRETA[ 2 ] );
            }
            else
            {
                String qtd = table.getValueAt( row, 3 ).toString(); // quantidade
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
            background = new Color( PRODUTO[ 0 ], PRODUTO[ 1 ], PRODUTO[ 2 ] );
            foreground = new Color( PRODUTO[ 0 ], PRODUTO[ 1 ], PRODUTO[ 2 ] );
        }

        setForeground( foreground );
        setBackground( background );
        setBorder( getCustomBorder() );
        table.setSelectionBackground( background );
        table.setSelectionForeground( foreground );

        return super.getTableCellRendererComponent( table, value, isSelected, hasFocus, row, column );
    }

    private Border getCustomBorder()
    {
        Border insideMargin = BorderFactory.createEmptyBorder( 10, 10, 10, 10 );
        Border trickMargin = BorderFactory.createLineBorder( Color.BLACK, 3 );

        Border borderWithMargin = BorderFactory.createCompoundBorder( trickMargin, insideMargin );

        return borderWithMargin;

    }

}
