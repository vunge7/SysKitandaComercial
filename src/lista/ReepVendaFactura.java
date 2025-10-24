/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lista;


import java.sql.Connection;
import dao.VendaDao;
import entity.TbVenda;
import java.io.File;
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
public class ReepVendaFactura {
    
    
     private EntityManagerFactory emf = JPAEntityMannagerFactoryUtil.em;
     private VendaDao vendaDao = new VendaDao(emf);
     private BDConexao conexao  = BDConexao.getInstancia();
     private int codigo;
     private double valor_entregue, troco;
     private boolean performance;
     private boolean a4;

    public ReepVendaFactura(int codigo, double valor_entregue, double troco, boolean performance, boolean a4)
    {
    
        this.codigo = codigo;
        this.valor_entregue = valor_entregue;
        this.troco = troco;
        this.performance = performance;
        this.a4 = a4;
        
        mostrarVenda();
    }

    public ReepVendaFactura() {
        mostrarVenda();
    }
    

    public void mostrarVenda(){
        
        java.sql.Connection connection = conexao.getConnectionAtiva();
        HashMap hashMap = new HashMap();
        
        
        hashMap.put("CODIGO_VENDA", codigo);
        hashMap.put("CREDITO", isCredito());
     
        
        String relatorio = getCaminho();
       
        File file = new File(relatorio).getAbsoluteFile();
        String obterCaminho = file.getAbsolutePath();

        try {
            JasperFillManager.fillReport(obterCaminho, hashMap, connection);

            JasperPrint jasperPrint = JasperFillManager.fillReport(obterCaminho, hashMap, connection);

            if (jasperPrint.getPages().size() >= 1) {
                JasperViewer jasperViewer = new JasperViewer(jasperPrint, false);
                
                
                jasperViewer.setVisible(true);
                
            } else {
                JOptionPane.showMessageDialog(null, "Não Exite factura com este código!...");
            }
        } catch (JRException jex) {
            jex.printStackTrace();
            //System.out.println("aqui");
            JOptionPane.showMessageDialog(null, "FALHA AO TENTAR MOSTRAR A FACTURA!...");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "ERRO AO EFECTUAR A FACTURA!...");
        }
    }
    
    //factura - perfoma.jasper
    
    
    // caso caixa
    public String getCaminho()
    {
        
        if(!performance)
        {
            if(!a4)
                  return "relatorios/factura_A5_1.jasper";
            return "relatorios/facturaA4.jasper";
        }
        else return "relatorios/proforma.jasper";
        
    }
    
    
     private String isCredito() {
        
         TbVenda venda =    vendaDao.findTbVenda(codigo);
         if (venda.getCredito().equals("false") ) {
              return "Recibo de Pagamento";
        }else return "Crédito";
    
    }
    
    
    public static void main(String[] args) {
        new ReepVendaFactura(26908, 2, 2, false, true);
    }
    
}
