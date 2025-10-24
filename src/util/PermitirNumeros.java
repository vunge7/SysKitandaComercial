/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;



import java.sql.Connection;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
/**
 *
 * @author Domingos Dala Vunge
 */
public class PermitirNumeros extends PlainDocument{
    @Override
    public void insertString(int offset, String str, javax.swing.text.AttributeSet attr)
    throws BadLocationException{
        super.insertString(offset, str.replaceAll("[^0-9]", ""), attr);
    
    }
    
      public void replace(int offset, String str, javax.swing.text.AttributeSet attr)
    throws BadLocationException{
        super.insertString(offset, str.replaceAll("[^0-9]", ""), attr);
    
    }
    
    
    
}
