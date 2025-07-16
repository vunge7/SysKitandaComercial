/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lista;

import dao.ArmazemDao;
import dao.DadosInstituicaoDao;
import dao.FornecedorDao;
import dao.ItemVendaDao;
import dao.UsuarioDao;
import dao.VendaDao;
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
import util.JPAEntityMannagerFactoryUtil;


/**
 *
 * @author Domingos Dala Vunge
 */
public  class ResumoVendasQTD_A6 {

    private EntityManagerFactory emf = JPAEntityMannagerFactoryUtil.em;      
    private ItemVendaDao itemVendaDao = new ItemVendaDao(emf);
    private ArmazemDao armazemDao = new ArmazemDao(emf);
    private FornecedorDao fornecedorDao = new FornecedorDao(emf);
    private DadosInstituicaoDao dadosInstituicaoDao = new DadosInstituicaoDao(emf);
    private Date data_incio, data_fim;
    private int  pk_armazem, pk_fornecedor;

    public ResumoVendasQTD_A6(Date data_incio,Date data_fim, int pk_armazem) 
    {
    
        this.data_incio = data_incio;
        this.data_fim = data_fim;
        this.pk_armazem = pk_armazem;
        
        try {
            mostrar();
        } catch (Exception e) {
        }
        
    }
    
    public ResumoVendasQTD_A6(Date data_incio,Date data_fim, int pk_armazem, int pk_fornecedor) 
    {
    
        this.data_incio = data_incio;
        this.data_fim = data_fim;
        this.pk_armazem = pk_armazem;
        this.pk_fornecedor = pk_fornecedor;
        try {
            mostrarFornecedor();
        } catch (Exception e) {
        }
        
    }

    public void mostrar(){

       //PARM_LUCRO
     
        HashMap hashMap = new HashMap();
     
        hashMap.put("PARM_DATA_1", this.data_incio);      
        hashMap.put("PARM_DATA_2", this.data_fim);      
        hashMap.put("PARM_ID_ARMAZEM", this.pk_armazem);      
        hashMap.put("DESIGNACAO_ARMAZEM", armazemDao.findTbArmazem(pk_armazem).getDesignacao() );  
        hashMap.put("NOME_INSTITUICAO", dadosInstituicaoDao.findTbDadosInstituicao(1).getNome().toUpperCase() );
          
        //obter o path do relatorio
        String relatorio = "relatorios/relatorio_diario_produtos_A6.jasper";

        File file = new File(relatorio).getAbsoluteFile();
        String obterCaminho = file.getAbsolutePath();

        try {
             Connection connection =  BDConexao.getConnection();
            JasperFillManager.fillReport(obterCaminho, hashMap, connection);
            JasperPrint jasperPrint = JasperFillManager.fillReport(obterCaminho, hashMap, connection);

            if (jasperPrint.getPages().size() >= 1) {
                JasperViewer jasperViewer = new JasperViewer(jasperPrint, false);
                jasperViewer.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(null, "Não existe registro para esse intervalo de data!...", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (JRException jex) {
            jex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Falha ao mostrar o relatorio", "DVML-COMERCIAL, LDA" , JOptionPane.ERROR);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao tentar mostrar o relatorio", "DVML-COMERCIAL, LDA" , JOptionPane.ERROR_MESSAGE);
        }

        }

    
    
     public void mostrarFornecedor(){

       //PARM_LUCRO
     
        HashMap hashMap = new HashMap();
     
        hashMap.put("PARM_DATA_1", this.data_incio);      
        hashMap.put("PARM_DATA_2", this.data_fim);      
        hashMap.put("PARM_ID_ARMAZEM", this.pk_armazem);      
        hashMap.put("PARM_ID_FORNECEDOR", this.pk_fornecedor);      
        hashMap.put("DESIGNACAO_ARMAZEM", armazemDao.findTbArmazem(pk_armazem).getDesignacao() );  
        hashMap.put("DESIGNACAO_FORNECEDOR", fornecedorDao.findTbFornecedor(pk_fornecedor).getNome() );  
        hashMap.put("NOME_INSTITUICAO", dadosInstituicaoDao.findTbDadosInstituicao(1).getNome().toUpperCase() );
        hashMap.put("ENDERECO", dadosInstituicaoDao.findTbDadosInstituicao(1).getEnderecos());
          
        //obter o path do relatorio
        String relatorio = "relatorios/relatorio_diario_produtos_fornecedor.jasper";

        File file = new File(relatorio).getAbsoluteFile();
        String obterCaminho = file.getAbsolutePath();

        try {
             Connection connection =  BDConexao.getConnection();
            JasperFillManager.fillReport(obterCaminho, hashMap, connection);
            JasperPrint jasperPrint = JasperFillManager.fillReport(obterCaminho, hashMap, connection);

            if (jasperPrint.getPages().size() >= 1) {
                JasperViewer jasperViewer = new JasperViewer(jasperPrint, false);
                jasperViewer.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(null, "Não existe registro para esse intervalo de data!...", "ERROR", JOptionPane.ERROR_MESSAGE);
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
        
        //new ResumoVenda(1);
        
    }

    
}
