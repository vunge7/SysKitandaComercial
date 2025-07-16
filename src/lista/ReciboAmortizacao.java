/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lista;

import com.mysql.jdbc.Connection;
import dao.VendaDao;
import entity.TbVenda;
import java.io.File;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import javax.persistence.EntityManagerFactory;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.view.JasperViewer;
import util.BDConexao;
import util.JPAEntityMannagerFactoryUtil;

/**
 *
 * @author Domingos Dala Vunge
 */
public class ReciboAmortizacao
{

    private EntityManagerFactory emf = JPAEntityMannagerFactoryUtil.em;
    private VendaDao vendaDao = new VendaDao( emf );
    private BDConexao conexao = new BDConexao();
    private String cod_factura;
    private String usuario = "";
    private double valor_por_pagar, total_amortizacoes, valor_entregue, diferenca;
    private Date data_emissao;

    public ReciboAmortizacao( String cod_factura, double valor_por_pagar, double total_amortizacoes, double valor_entregue, double diferenca, Date data_emissao, String usuario )
    {
        this.cod_factura = cod_factura;
        this.valor_por_pagar = valor_por_pagar;
        this.total_amortizacoes = total_amortizacoes;
        this.valor_entregue = valor_entregue;
        this.diferenca = diferenca;
        this.data_emissao = data_emissao;
        this.usuario = usuario;
        mostrarVenda();
    }

    public void mostrarVenda()
    {

        Connection connection = (Connection) conexao.conectar();
        HashMap hashMap = new HashMap();

        System.out.println( "COD_VENDA: "  + this.cod_factura);
        hashMap.put( "DATA_EMISSAO", this.data_emissao );
        hashMap.put( "TOTAL_AMORTIZACOES", this.total_amortizacoes );
        hashMap.put( "VALOR_POR_PAGAR", this.valor_por_pagar );
        hashMap.put( "VALOR_ENTREGUE", this.valor_entregue );
        hashMap.put( "DIFERENCA", this.diferenca );
        hashMap.put( "COD_VENDA", this.cod_factura );
        hashMap.put( "USUARIO", this.usuario );

        String relatorio = getCaminho();

        File file = new File( relatorio ).getAbsoluteFile();
        String obterCaminho = file.getAbsolutePath();

        try
        {
            JasperFillManager.fillReport( obterCaminho, hashMap, connection );

            JasperPrint jasperPrint = JasperFillManager.fillReport( obterCaminho, hashMap, connection );

            if ( jasperPrint.getPages().size() >= 1 )
            {

                JasperViewer jasperViewer = new JasperViewer( jasperPrint, false );
                jasperViewer.setVisible( true );

//                //Imprime directamente
//                if ( !performance )
//                {
//                    JasperPrintManager.printReport( jasperPrint, false );
//                }
            }
            else
            {
                JOptionPane.showMessageDialog( null, "O relatório não tem páginas" );
            }
        }
        catch ( JRException jex )
        {
            jex.printStackTrace();
            //System.out.println("aqui");
            JOptionPane.showMessageDialog( null, "FALHA AO TENTAR MOSTRAR O RECIBO!..." );
        }
        catch ( Exception ex )
        {
            ex.printStackTrace();
            JOptionPane.showMessageDialog( null, "ERRO AO EFECTUAR O RECIBO!..." );
        }
    }

    //factura - perfoma.jasper
    // caso caixa
    public String getCaminho()
    {

        return "relatorios/recibo_amortizacao.jasper";

    }

    public static void main( String[] args ) throws JRException, SQLException
    {
        new ReciboAmortizacao( "", 0, 0, 0, 0, new Date(), "Usuario" );
    }

}
