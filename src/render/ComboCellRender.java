/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package render;


import java.sql.Connection;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author Domingos Dala Vunge
 */
public class ComboCellRender extends JComboBox implements TableCellRenderer
{

    public ComboCellRender( String[] items )
    {
        super( items );
    }

    @Override
    public Component getTableCellRendererComponent( JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column )
    {

        if ( isSelected )
        {
            setForeground( Color.BLACK );
            setBackground( table.getSelectionBackground() );
        }
        else
        {
            setForeground( table.getForeground() );
            setBackground( table.getBackground() );
        }

        setSelectedItem( value );
        return this;
    }

}
