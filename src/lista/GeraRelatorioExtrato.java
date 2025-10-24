/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lista;


import java.sql.Connection;
import dao.BancoDao;
import dao.DadosInstituicaoDao;
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
import util.Extrato;
import util.JPAEntityMannagerFactoryUtil;

/**
 *
 * @author macpro
 */
public class GeraRelatorioExtrato {
    
    private EntityManagerFactory emf = JPAEntityMannagerFactoryUtil.em;
    private Date data_inicio,data_fim;
    private String id_banco;
//    private BancoDao bancoDao = new BancoDao(emf);
    private DadosInstituicaoDao dadoIntituicaoDao = new DadosInstituicaoDao(emf);
    private  List<Extrato> lista_geral  = new ArrayList();
    
    
    
    public GeraRelatorioExtrato(List<Extrato> lista_geral,String id_banco, Date data_inicio, Date data_fim){
        this.id_banco = id_banco;
        this.data_inicio = data_inicio;
        this.data_fim = data_fim;
        this.lista_geral = lista_geral;
        
        mostrar();
    }
    
    
    public void mostrar(){
    
        //Meus Parametros
        HashMap hashMap = new HashMap();
        hashMap.put("ID_BANCO", this.id_banco); 
        hashMap.put("PARM_DATA_INICIO", this.data_inicio);      
        hashMap.put("PARM_DATA_FIM", this.data_fim); 
        hashMap.put("NOME_INSTITUICAO", this.dadoIntituicaoDao.findTbDadosInstituicao(1).getNome() );
        String caminho = getCaminho();
        //Minha Coleção de Dados 
        try {
 
            JasperReport report = JasperCompileManager.compileReport(caminho);
        
            JasperPrint print = JasperFillManager.fillReport(report, hashMap, new JRBeanCollectionDataSource(lista_geral));

            JasperViewer jasperViewer = new JasperViewer(print, false);
            jasperViewer.setVisible(true);

        } catch (JRException ex) {
            ex.printStackTrace();
        }
        

    }
  
  
    
    private  String getCaminho()
    {
             
        return "relatorios/extrato_bancario.jrxml";

    
    }
  
    
    public static void main(String[] args) {
        
        
          
        
    }
       
  
}

