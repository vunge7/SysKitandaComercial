/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.tabela_manual.render;

import java.awt.Color;
import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import util.DVML;

/**
 *
 * @author Domingos Dala Vunge
 */
public class Render extends DefaultTableCellRenderer
{

    @Override
    public Component getTableCellRendererComponent( JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column )
    {

        if ( value instanceof JButton )
        {
            JButton btn = ( JButton ) value;
            if ( isSelected )
            {
                // btn.setForeground( table.getSelectionForeground() );
                btn.setForeground( Color.BLUE );
                btn.setBackground( table.getSelectionBackground() );

            }
            else
            {
                btn.setForeground( table.getForeground() );
                btn.setBackground( UIManager.getColor( "Button.background" ) );
            }

            if ( ( ( JButton ) value ).getText().equals( DVML.JUSTIFICAR ) )
            {
                btn.setIcon( new javax.swing.ImageIcon( getClass().getResource( "/imagens/confirmacao.png" ) ) );
            }
            else if ( ( ( JButton ) value ).getText().equals( DVML.REMOVER ) )
            {
                btn.setIcon( new javax.swing.ImageIcon( getClass().getResource( "/imagens/eliminar_16x16.png" ) ) );
            }

            return btn;
        }

        if ( value instanceof JCheckBox )
        {
            JCheckBox ch = ( JCheckBox ) value;
//            if ( isSelected )
//            {
//                ch.setSelected( true );
//            }
//            else
//            {
//                ch.setSelected( false );
//            }

            return ch;
        }
        if ( value instanceof JComboBox )
        {
            JComboBox ch = ( JComboBox ) value;
//            if ( isSelected )
//            {
//                ch.setSelected( true );
//            }
//            else
//            {
//                ch.setSelected( false );
//            }

            return ch;
        }

        return super.getTableCellRendererComponent( table, value, isSelected, hasFocus, row, column );
    }

}
