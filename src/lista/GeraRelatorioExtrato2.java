/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lista;


import java.sql.Connection;
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
import util.Extrato;
import util.JPAEntityMannagerFactoryUtil;

/**
 *
 * @author macpro
 */
public class GeraRelatorioExtrato2 {
    
    private EntityManagerFactory emf = JPAEntityMannagerFactoryUtil.em;
    private  List<Extrato> lista_geral  = new ArrayList();
    
    
    public GeraRelatorioExtrato2(List<Extrato> lista_geral){
        
        this.lista_geral = lista_geral;
        
        mostrar();
    }
    
    
    public void mostrar(){
    
        //Meus Parametros
        HashMap hashMap = new HashMap();  
       
//        hashMap.put("NOME_INSTITUICAO",  dadoIntituicaoDao.findDadoIntituicao(1).getNomeEscola() );
//        hashMap.put("TURMA", turma.getDescrisao());
//        hashMap.put("SALA", turma.getIdSala().getDescrisao());
//        hashMap.put("TURNO", turma.getIdTurno().getDescrisao());
//        hashMap.put("CLASSE", turma.getIdClasse().getDescrisao());
//        hashMap.put("ANO", turma.getIdAno().getDescrisao());
//        hashMap.put("CURSO", turma.getIdCurso().getDescrisao());
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

