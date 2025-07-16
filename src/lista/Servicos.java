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
import util.ServicosUtil;

/**
 *
 * @author MartinhoLuis
 */
public class Servicos {
  
    private int pk_marcacao_consultas;
    private EntityManagerFactory emf = JPAEntityMannagerFactoryUtil.em;
    private DadosInstituicaoDao dadosInstituicaoDao = new DadosInstituicaoDao(emf);
    private List<ServicosUtil> lista;
  
 
    public Servicos(List<ServicosUtil> lista){
       
        this.lista = lista;
        mostrar();
        
    }
  
    public void mostrar(){

       
        HashMap hashMap = new HashMap();
        
  
        hashMap.put("DADOS_INSTITUICAO",  dadosInstituicaoDao.findTbDadosInstituicao(1).getNome()  );
       
        //obter o path do relatorio
        String relatorio = "relatorios/listagen_servicos.jrxml";

//   
        
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
          EntityManagerFactory emf = JPAEntityMannagerFactoryUtil.em;
    
          
    }

}