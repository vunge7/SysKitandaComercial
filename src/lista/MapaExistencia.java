/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package lista;

import dao.DadosInstituicaoDao;
import dao.StockDao;
import dao.TipoProdutoDao;
import entity.TbStock;
import entity.TbTipoProduto;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;
import util.BDConexao;
import util.JPAEntityMannagerFactoryUtil;
import util.MapaExistenciaUtil;
import util.ServicosUtil;

/**
 *
 * @author MartinhoLuis
 */
public class MapaExistencia {
  
   
    private EntityManagerFactory emf = JPAEntityMannagerFactoryUtil.em;
    private DadosInstituicaoDao dadosInstituicaoDao = new DadosInstituicaoDao(emf);
    private List<MapaExistenciaUtil> lista;
    private String armazem;
    private Date data;
    
  
 
    public MapaExistencia(List<MapaExistenciaUtil> lista, String armazem, Date data){
       
        this.lista = lista;
        this.armazem = armazem;
        this.data = data;
        mostrar();
        
    }
  
    public void mostrar(){

       
        HashMap hashMap = new HashMap();

        hashMap.put("NOME",  dadosInstituicaoDao.findTbDadosInstituicao(1).getNome()  );
        hashMap.put("ENDERECO",  dadosInstituicaoDao.findTbDadosInstituicao(1).getEnderecos()  );
        hashMap.put("ARMAZEM",  this.armazem );
        hashMap.put("DATA",  this.data  );
       
        //obter o path do relatorio
        String relatorio = "relatorios/mapa_existencia.jrxml";
        
        try {
 
            JasperReport report = JasperCompileManager.compileReport(relatorio);        
            JasperPrint print = JasperFillManager.fillReport(report, hashMap, new JRBeanCollectionDataSource(lista));

            JasperViewer jasperViewer = new JasperViewer(print, false);
            jasperViewer.setVisible(true);

        } catch (JRException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) throws SQLException {
        
        
         List<MapaExistenciaUtil> lista =  new ArrayList<>();
         MapaExistenciaUtil object = new MapaExistenciaUtil();
         
         object.setCod_produto(1);
         object.setDesignacao("Produto Teste");
         object.setExistencia_anterior(12);
         object.setQtd_entrada(4);
         object.setQtd_vendida(12);
         object.setQtd_actual(4);
         object.setPreco_venda(450.0);
         object.setTotal( object.getQtd_vendida()*object.getPreco_venda() );

         lista.add(object);
         
         object = new MapaExistenciaUtil();
         
         object.setCod_produto(1);
         object.setDesignacao("Produto Teste");
         object.setExistencia_anterior(12);
         object.setQtd_entrada(4);
         object.setQtd_vendida(12);
         object.setQtd_actual(4);
         object.setPreco_venda(450.0);
         object.setTotal( object.getQtd_vendida()*object.getPreco_venda() );
        
         
         lista.add(object);
         
         new MapaExistencia(lista, "Armazeém 1",new Date());
        
        
        
    
          
    }

}