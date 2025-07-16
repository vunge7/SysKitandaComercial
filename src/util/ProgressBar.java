/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
/**
 *
 * @author Domingos Dala Vunge
 */


public class ProgressBar {
    
    private static JProgressBar pro;
    private static JFrame frame;
    private static int x = 0;

    public ProgressBar() {
        
        frame = new JFrame();
        frame.setUndecorated(true);
        JPanel panel = new JPanel();
      
        pro = new JProgressBar(0, 100);
        pro.setStringPainted(true);
      
        frame.add(panel);
        panel.add(pro);
        frame.setVisible(true);
        frame.setSize(100,100);
        //frame.setAlwaysOnTop(true);
        frame.setLocationRelativeTo(null);
  
        frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
        
    }
    
    public static void iterate(){
        
            while(x <= 100){
                pro.setValue(x);
                x++;
                
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    Logger.getLogger(ProgressBar.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            //frame.setAlwaysOnTop(false);
            frame.dispose();
           
            JOptionPane.showMessageDialog(null, "Done!...");
    }
    
    public static void main(String[] args) {
        new ProgressBar();
        iterate();
    }
    
    
}
