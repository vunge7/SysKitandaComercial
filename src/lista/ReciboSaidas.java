/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package lista;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import util.BDConexao;

/**
 *
 * @author MartinhoLuis
 */


public class ReciboSaidas {
  
    private int comprovativo;
  
 
    public ReciboSaidas(int comprovativo) throws SQLException {
    
   
        this.comprovativo = comprovativo;

        mostrar();
        
    }
  
    public void mostrar() throws SQLException{

        Connection connection =  BDConexao.getConexao();
        HashMap hashMap = new HashMap();
  
       
        hashMap.put("ID_RECIBO",  this.comprovativo);
       
        
        //obter o path do relatorio
        String relatorio = "relatorios/saidaCaixaSegundaVia.jasper";

        File file = new File(relatorio).getAbsoluteFile();
        String obterCaminho = file.getAbsolutePath();
            
        try {
            
            //JasperFillManager.fillReport(obterCaminho, hashMap, connection);
            JasperFillManager.fillReport(obterCaminho, hashMap, connection);
            JasperPrint jasperPrint = JasperFillManager.fillReport(obterCaminho, hashMap, connection);

            if (jasperPrint.getPages().size() >= 1) {
                JasperViewer jasperViewer = new JasperViewer(jasperPrint, false);
                jasperViewer.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(null, "Atenção\nNão existe recibo relacionado com este numero!");
            }
            
        } catch (JRException jex) {
            jex.printStackTrace();
            JOptionPane.showMessageDialog(null, "FALHA AO TENTAR MOSTRAR O RELATORIO!");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "ERRO AO TENTAR MOSTRAR O RELATORIO!");
        }

        }

    public static void main(String[] args) throws SQLException {
        ReciboSaidas reciboEntradas = new ReciboSaidas(1); 
    }

}
