/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lista;

import controller.ProdutoController;
import controller.StockController;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import modelo.ExtratoProdutoModelo;
import modelo.ProdutoModelo;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import util.BDConexao;

/**
 *
 * @author Domingos Dala Vunge
 */
public class GerarRelatorioExtratoProduto {
    
    public  JasperDesign jasperDesign;
    public  JasperPrint jasperPrint;
    public  JasperReport jasperReport;
    public  String reportTemplateUrl = "relatorios/extratoProduto.jrxml";
    public int quantidade = 0;
    public double valor_total = 0;
    
    
    //return the report filename
    public  String getReportFile(){
            return reportTemplateUrl;
    }
    
        //get a parameter to pass into jrxml
    public  Map getReportParameter(Integer codigo_produto,  
                             String data) throws SQLException {
       
    Map parameters = new HashMap();

    
    
   Vector<ExtratoProdutoModelo>   vector =  new BDConexao().getExtratosProduto(codigo_produto, data);
    
    for (int i = 0; i < vector.size(); i++) {
                 quantidade += vector.get(i).getQuant_total() ;
                valor_total += vector.get(i).getQuant_valor();
    }
    

    ProdutoModelo  produtoModelo = getProdutoByCodigo(codigo_produto);   
    
    
        parameters.put("PRODUTO",produtoModelo.getDesignacao() );
        
        if(produto_sotcavel(produtoModelo.getStocavel()))
              parameters.put("PRECO", new StockController(new BDConexao()).getStockProduto(codigo_produto, 1).getPreco_venda());
        else  parameters.put("PRECO", produtoModelo.getPreco());
       
        parameters.put("QUANT_EXISTENTE",  new StockController(new BDConexao()).getStockProduto(codigo_produto, 1).getQuantidade_existente());
        parameters.put("DATA", data);
        parameters.put("TOTAL_QUANTIDADE", quantidade);
        parameters.put("TOTAL_VALOR", valor_total);
        
        return parameters;
    }
    
    
   public boolean produto_sotcavel(String stocavel){

            return stocavel.equals("true");
   
   }
    
    public ProdutoModelo getProdutoByCodigo(Integer codigo) throws SQLException{
                return new ProdutoController( new BDConexao()).getProduto(codigo);
    }
    
    
    
 
    
    //get the data to pass into jrxml
public static List<Object> findReportData(int codigoProduto, String data_proucurada) throws SQLException{
    
    //declare a list of object
    List<Object> reports = new LinkedList<Object>();
 
   
    Vector<ExtratoProdutoModelo>   vector =  new BDConexao().getExtratosProduto(codigoProduto, data_proucurada);
    
    for (int i = 0; i < vector.size(); i++) {
                  reports.add((Object)   vector.get(i)  );
                
    }
    
 
    return  reports;
    
}
    

public  void chamar_relatorio(int codigo_produto, String data_proucurada) throws SQLException{

    
    
        try {
            //get report file and then load into jasperDesign
            jasperDesign = JRXmlLoader.load(getReportFile());
            //compile the jasperDesign
            jasperReport = JasperCompileManager.compileReport(jasperDesign);
            //fill the ready report with data and parameter
            jasperPrint = JasperFillManager.fillReport(jasperReport, getReportParameter(codigo_produto, data_proucurada), new JRBeanCollectionDataSource(
          
                    findReportData(codigo_produto, data_proucurada)));

             JasperViewer jasperViewer = new JasperViewer(jasperPrint, false);
             jasperViewer.setVisible(true);
            
        } catch (JRException e) {
            e.printStackTrace();
        }

    }



    public static void main(String[] args) throws SQLException {
        
        
                    new GerarRelatorioExtratoProduto().chamar_relatorio(4, "2014-01-17");
//                    new GerarRelatorioListaDesvedores().chamar_relatorio();
        
     
        
    }
    
}
