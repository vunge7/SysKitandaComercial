/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import javax.swing.*;
import java.awt.*;

public class ModernTextField extends JTextField
{

    private String placeholder;

    public ModernTextField( String placeholder )
    {
        this.placeholder = placeholder;
        setFont( new Font( "Segoe UI", Font.PLAIN, 14 ) );
        setPreferredSize( new Dimension( 260, 34 ) );
        setBorder( BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder( new Color( 180, 180, 180 ), 1 ),
                BorderFactory.createEmptyBorder( 6, 8, 6, 8 )
        ) );
    }

    @Override
    protected void paintComponent( Graphics g )
    {
        super.paintComponent( g );
        if ( getText().isEmpty() && !isFocusOwner() && placeholder != null )
        {
            Graphics2D g2 = ( Graphics2D ) g.create();
            g2.setColor( new Color( 140, 140, 140 ) );
            g2.setFont( getFont().deriveFont( Font.ITALIC ) );
            FontMetrics fm = g2.getFontMetrics();
            int y = ( getHeight() - fm.getHeight() ) / 2 + fm.getAscent();
            g2.drawString( placeholder, 8, y );
            g2.dispose();
        }
    }
}
