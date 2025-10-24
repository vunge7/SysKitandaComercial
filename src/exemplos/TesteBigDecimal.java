/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package exemplos;


import java.sql.Connection;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 *
 * @author Domingos Dala Vunge
 */
public class TesteBigDecimal {
    
    
    public static void main(String[] args) {
        
        BigDecimal bigDecimal1 = new BigDecimal( 0.511);
        BigDecimal bigDecimal2 = new BigDecimal( 0.5);
        
        System.err.println("A DIVISAO E " +bigDecimal1.divide(bigDecimal2, 3, RoundingMode.UP));
    }
    
}
