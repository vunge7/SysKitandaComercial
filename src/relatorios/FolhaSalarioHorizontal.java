/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package relatorios;



import java.sql.Connection;
import dao.DadosInstituicaoDao;
import entity.TbFuncionario;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import util.FolhaSalarioUtil;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;
import org.eclipse.persistence.internal.jpa.parsing.SomeNode;
import util.JPAEntityMannagerFactoryUtil;

/**
 *
 * @author DALLAS
 */


public class FolhaSalarioHorizontal {
    
    private EntityManagerFactory emf = JPAEntityMannagerFactoryUtil.em;
    private DadosInstituicaoDao dadoIntituicaoDao = new DadosInstituicaoDao(emf);
    private List<FolhaSalarioUtil> lista  = new ArrayList();   
    private TbFuncionario funcionario;
    private int opcao;
    private String periodo = "", ano = "";
    
    
    public FolhaSalarioHorizontal(List<FolhaSalarioUtil> salario_util, String periodo, String ano, int opcao){
        
        lista = salario_util; 
        this.opcao = opcao;
        this.periodo = periodo;
        this.ano = ano;
        this.funcionario = funcionario;
        mostrar();
        
    }
    
    
   
    
    public void mostrar(){
    
        //Meus Parametros
        HashMap hashMap = new HashMap();         
        hashMap.put("PERIODO", this.periodo );
        hashMap.put("ANO", this.ano );
        hashMap.put("NOME_INSTITUICAO", this.dadoIntituicaoDao.findTbDadosInstituicao(1).getNome() );
        hashMap.put("ENDERECO", this.dadoIntituicaoDao.findTbDadosInstituicao(1).getEnderecos() );
        hashMap.put("TELEFONE", this.dadoIntituicaoDao.findTbDadosInstituicao(1).getTelefone() +"/" +this.dadoIntituicaoDao.findTbDadosInstituicao(1).getTelefone()  );
        hashMap.put("NIF", this.dadoIntituicaoDao.findTbDadosInstituicao(1).getNif() );
        String caminho = getCaminho();
        //Minha Coleção de Dados 
        try {
 
            JasperReport report = JasperCompileManager.compileReport(caminho);
        
            JasperPrint print = JasperFillManager.fillReport(report, hashMap, new JRBeanCollectionDataSource(lista));

            JasperViewer jasperViewer = new JasperViewer(print, false);
            jasperViewer.setVisible(true);

        } catch (JRException ex) {
            ex.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }

    }
  
    private  String getCaminho(){            
        if (opcao == 1) {
             return "relatorios/folha_salario_horizontal.jrxml";
        }        
        else if (opcao == 2){
                return "relatorios/folha_salario_horizontal_resumida.jrxml";  
        }
         else if (opcao == 3){
                return "relatorios/folha_salario_horizontal_seguranca_social.jrxml";  
        }
        else if (opcao == 4){
                return "relatorios/folha_salario_horizontal_irt.jrxml";  
        }
        
        return "";
    }
  
    public static void main(String[] args) {
        
        
          
        
    }
    
    
    
    
    
   
    
    
    
    
    
  
}
