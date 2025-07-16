/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package relatorios;

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
//import entity.Producao;

/**
 *
 * @author MartinhoLuis
 */


public class ReciboDesactivos {
  
//    private int pkAno;
 
    public ReciboDesactivos() throws SQLException {
    
   
//        this.pkAno = pkAno;
        try {
             mostrar();
        } catch (Exception e) {
        }
       
        
    }
  
    public void mostrar() throws SQLException{

        Connection connection =  BDConexao.getConexao();
        HashMap hashMap = new HashMap();

//        hashMap.put("ID_ANO",  this.pkAno );
//        hashMap.put("SUBREPORT_DIR", "relatorios/");
       
        
        //obter o path do relatorio
        String relatorio = "relatorios/listagemDesactivos.jasper";

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
                JOptionPane.showMessageDialog(null, "ATENÇÃO\nNão existem Funcionários desistentes!...", "Informação", JOptionPane.WARNING_MESSAGE);
            }
            
        } catch (JRException jex) {
            jex.printStackTrace();
            JOptionPane.showMessageDialog(null, "FALHA AO TENTAR MOSTRAR A LISTA");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "ERRO AO TENTAR MOSTRAR A LISTA");
        }

        }
    
//    private void retornar_feicho_sem_feicho(){
//        
//                
//            if(!producao.getFeicho()){
//               String relatorio = "relatorios/teste_princ_ordem_producao_folha.jasper"; 
//                
//            }else
//                String relatorio = "relatorios/teste_princ_ordem_producao_feicho.jasper";
//    
//    
//    }

    public static void main(String[] args) throws SQLException {
        ReciboDesactivos recibo = new ReciboDesactivos(); 
    }

}
