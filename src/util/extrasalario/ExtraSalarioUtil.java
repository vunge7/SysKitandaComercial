/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.extrasalario;


import java.sql.Connection;
import javax.swing.JComboBox;
import javax.swing.JSpinner;
import javax.swing.JTable;

/**
 *
 * @author Domingos Dala Vunge
 */

public class ExtraSalarioUtil extends AbstractSalarioExtra
{

    public ExtraSalarioUtil( JTable tabela, int extra, JComboBox combo, JSpinner spinner )
    {
        super( tabela, extra, combo, spinner );
    }

   
}
