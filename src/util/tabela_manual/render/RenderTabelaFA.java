/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.tabela_manual.render;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author Domingos Dala Vunge
 */
public class RenderTabelaFA extends DefaultTableCellRenderer
{

    @Override
    public Component getTableCellRendererComponent( JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column )
    {

        Color background;
        Color foreground;

        //verificar o prazo
        int prazo = Integer.parseInt( table.getValueAt( row, 9 ).toString() );
        double divida = Double.parseDouble( table.getValueAt( row, 7 ).toString() );
        if ( prazo < 0 && divida < 0 )
        {
            background = new Color( 255, 0, 0, 100 );
            foreground = new Color( 0, 0, 0 );
        }

        else if ( divida >= 0 )
        {
            background = new Color( 50, 205, 50, 100);
            foreground = new Color( 0, 0, 0 );
        }
        else
        {
            background = new Color( 255, 255, 255 );
            foreground = new Color( 0, 0, 0 );
        }

        setBackground( background );
        setForeground( foreground );

        return super.getTableCellRendererComponent( table, value, isSelected, hasFocus, row, column );
    }

}
