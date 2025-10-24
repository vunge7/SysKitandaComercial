/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lista;


import java.sql.Connection;
import java.io.File;
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
 * @author Domingos Dala Vunge
 */
public class ListaPedidos {
    
    
    
   private int pk_pedido;
   private int pk_lugar;
   private double totalPedidos;
  
 
    public ListaPedidos(int pk_pedido) throws SQLException {
    
   
        this.pk_pedido = pk_pedido;

//        mostrar();
        mostrar_ticket_geral();
        
    }
    public ListaPedidos(int pk_pedido, double totalPedidos) throws SQLException {
    
   
        this.pk_pedido = pk_pedido;
        this.totalPedidos = totalPedidos;

        mostrar();
        
    }
    
    public ListaPedidos(int pk_pedido, int pk_lugar) throws SQLException {
    
   
        this.pk_pedido = pk_pedido;
        this.pk_lugar = pk_lugar;

        mostrar_separado();
        
    }
   //Mostra Pedidos Gerais ou seja Pedidos de todos lugares de uma Mesa
    public void mostrar() throws SQLException{

        java.sql.Connection connection =  BDConexao.getConexao();
        HashMap hashMap = new HashMap();
  
       
        hashMap.put("ID_PEDIDO",  this.pk_pedido);
        hashMap.put("TotalPedido",  this.totalPedidos);
       
        
        //obter o path do relatorio
        String relatorio = "relatorios/pedido_A5_1.jasper";

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
                JOptionPane.showMessageDialog(null, "ERRO AO TENTAR MOSTRAR O RECIBO!...", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (JRException jex) {
            jex.printStackTrace();
            JOptionPane.showMessageDialog(null, "FALHA AO TENTAR MOSTRAR O RCIBO");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "ERRO AO TENTAR MOSTRAR O RECIBO");
        }

        }
    
    
    public void mostrar_ticket_geral() throws SQLException{

        java.sql.Connection connection =  BDConexao.getConexao();
        HashMap hashMap = new HashMap();
  
       
        hashMap.put("ID_PEDIDO",  this.pk_pedido);
//        hashMap.put("TotalPedido",  this.totalPedidos);
       
        
        //obter o path do relatorio
        String relatorio = "relatorios/ticket_geral.jasper";

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
                JOptionPane.showMessageDialog(null, "ERRO AO TENTAR MOSTRAR O RECIBO!...", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (JRException jex) {
            jex.printStackTrace();
            JOptionPane.showMessageDialog(null, "FALHA AO TENTAR MOSTRAR O RCIBO");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "ERRO AO TENTAR MOSTRAR O RECIBO");
        }

        }
    
    //Mostra Pedidos Por Lugares ou seja Pedido de um lugar na Mesa
     public void mostrar_separado() throws SQLException{

        java.sql.Connection connection =  BDConexao.getConexao();
        HashMap hashMap = new HashMap();   
        hashMap.put("ID_PEDIDO",  this.pk_pedido);
        hashMap.put("ID_LUGAR",  this.pk_lugar);        
        //obter o path do relatorio
        String relatorio = "relatorios/pedido_A5_separado.jasper";

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
                JOptionPane.showMessageDialog(null, "ERRO AO TENTAR MOSTRAR O RECIBO!...", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (JRException jex) {
            jex.printStackTrace();
            JOptionPane.showMessageDialog(null, "FALHA AO TENTAR MOSTRAR O RCIBO");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "ERRO AO TENTAR MOSTRAR O RECIBO");
        }

        }

    public static void main(String[] args) throws SQLException {
        ListaPedidos listaPedidos = new ListaPedidos(40); 
    }


}
    
    
    
    
    
   

