/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lista;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import javax.persistence.EntityManagerFactory;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import util.BDConexao;

/**
 *
 * @author Domingos Dala Vunge
 */

public  class RelGeralSaldos {


    public RelGeralSaldos() 
    {

        try {
            mostrar();
        } catch (Exception e) {
        }
        
    }

    public void mostrar() throws SQLException{

        Connection connection =  BDConexao.getConnection();
        HashMap hashMap = new HashMap();   
          
        //obter o path do relatorio
        String relatorio = "relatorios/PRINC_RELATORIO_SALDOS.jasper";

        File file = new File(relatorio).getAbsoluteFile();
        String obterCaminho = file.getAbsolutePath();

        try {
          
            JasperFillManager.fillReport(obterCaminho, hashMap, connection);
            JasperPrint jasperPrint = JasperFillManager.fillReport(obterCaminho, hashMap, connection);

            if (jasperPrint.getPages().size() >= 1) {
                JasperViewer jasperViewer = new JasperViewer(jasperPrint, false);
                jasperViewer.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(null, "Não existe registro de Saldos!...", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (JRException jex) {
            jex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Falha ao mostrar o relatorio", "DVML-COMERCIAL, LDA" , JOptionPane.ERROR);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao tentar mostrar o relatorio", "DVML-COMERCIAL, LDA" , JOptionPane.ERROR_MESSAGE);
        }

        }

    public static void main(String[] args){
        
        new RelGeralSaldos();
        
    }

    
}
