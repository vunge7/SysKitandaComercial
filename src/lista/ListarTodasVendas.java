/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lista;

import com.mysql.jdbc.Connection;
import dao.DadosInstituicaoDao;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;
import util.BDConexao;
import util.JPAEntityMannagerFactoryUtil;
import util.VendaUsuarioUtil;

/**
 *
 * @author Domingos Dala Vunge
 */
public class ListarTodasVendas {
    
    
      
    
    private EntityManagerFactory emf = JPAEntityMannagerFactoryUtil.em;
    private DadosInstituicaoDao dadosInstituicaoDao = new DadosInstituicaoDao(emf);
    private  List<VendaUsuarioUtil> lista  = new ArrayList();   
  
    private String periodo = "";
    
    public ListarTodasVendas(List<VendaUsuarioUtil> venda_suario_util, String periodo)
    {        
        lista = venda_suario_util; 
        this.periodo = periodo;
        mostrar();        
    }
    
    public void mostrar()
    {    
        //Meus Parametros
        HashMap hashMap = new HashMap();  
     
        hashMap.put("NOME_INSTITUICAO", dadosInstituicaoDao.findTbDadosInstituicao(1).getNome().toUpperCase() );
        hashMap.put("PERIODO", this.periodo );       
        String caminho = getCaminho();
        //Minha Coleção de Dados 
        try {
 
            JasperReport report = JasperCompileManager.compileReport(caminho);
        
            JasperPrint print = JasperFillManager.fillReport(report, hashMap, new JRBeanCollectionDataSource(lista));

            JasperViewer jasperViewer = new JasperViewer(print, false);
            jasperViewer.setVisible(true);

        } catch (JRException ex) {
            ex.printStackTrace();
        }

    }
  
  
    
    private  String getCaminho()
    {
             
        return "relatorios/listar_todas_venda_ordem_usuario.jrxml";
           
    
    }
  
    
    public static void main(String[] args) {
        
          List<VendaUsuarioUtil> lista = new ArrayList<>();
          
          VendaUsuarioUtil vendaUsuarioUtil = new VendaUsuarioUtil();
          
          vendaUsuarioUtil.setNumero_fact("1");
          vendaUsuarioUtil.setCliente_nome("Teste");
          vendaUsuarioUtil.setData_venda("01/02/2016");
          vendaUsuarioUtil.setTotal_venda("5000");
          
          lista.add(vendaUsuarioUtil);
          
          vendaUsuarioUtil = new VendaUsuarioUtil();
          vendaUsuarioUtil.setNumero_fact("2");
          vendaUsuarioUtil.setCliente_nome("Teste");
          vendaUsuarioUtil.setData_venda("01/02/2016");
          vendaUsuarioUtil.setTotal_venda("5000");
          
          lista.add(vendaUsuarioUtil);
          
          new ListarTodasVendas(lista, "2016");
          
        
    }
    
    
    
    
    
   
    
}
