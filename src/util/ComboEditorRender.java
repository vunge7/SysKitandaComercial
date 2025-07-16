/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;

/**
 *
 * @author Domingos Dala Vunge
 */
public class ComboEditorRender extends DefaultCellEditor
{

    public ComboEditorRender( String[] items )
    {
        super( new JComboBox( items ) );
    }

}
