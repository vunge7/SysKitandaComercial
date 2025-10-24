/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lista;


import java.sql.Connection;
import java.io.File;
import java.util.HashMap;
import java.util.logging.Level;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.view.JasperViewer;
import util.BDConexao;

/**
 *
 * @author Domingos Dala Vunge
 */
public class ImprimirPrecosNovo
{

    private BDConexao conexao = BDConexao.getInstancia();
    private double preco_retalho = 0d, preco_grosso = 0d;
    private String designacao;
    private int cod_produto;
    private double qtd;
    
    private String cod_barra = "";
    private String dados_instituicao;

    public ImprimirPrecosNovo( String cod_barra, double preco_retalho, double qtd, int cod_produto, String designacao, String dados_instituicao )
    {

        this.preco_retalho = preco_retalho;
        this.preco_grosso = preco_grosso;
        this.cod_barra = cod_barra;
        this.designacao = designacao;
        this.dados_instituicao = dados_instituicao;
        this.qtd = qtd;
        this.cod_produto = cod_produto;

        mostrar_preco();

    }

    public ImprimirPrecosNovo()
    {
        mostrar_preco();
    }

    public void mostrar_preco()
    {

        java.sql.Connection connection = conexao.getConnectionAtiva();
        HashMap hashMap = new HashMap();
        hashMap.put( "DADOS_INSTITUICAO", "KOGUI" );
        hashMap.put( "DADOS_INSTITUICAO", dados_instituicao );
        hashMap.put( "BARRA", Long.parseLong( cod_barra ) );
        hashMap.put( "PRECO_RETALHO", preco_retalho );
        hashMap.put( "PRECO_GROSSO", preco_grosso );
        hashMap.put( "DESIGNACAO", designacao );
        hashMap.put( "QTD", qtd );
        hashMap.put( "COD_PRODUTO", cod_produto );

        String relatorio = "relatorios/preco_produto.jasper";

        File file = new File( relatorio ).getAbsoluteFile();
        String obterCaminho = file.getAbsolutePath();

        try
        {
            JasperFillManager.fillReport( obterCaminho, hashMap, connection );

            JasperPrint jasperPrint = JasperFillManager.fillReport( obterCaminho, hashMap, connection );

            
            if ( jasperPrint.getPages().size() >= 1 )
            {
                JasperViewer jasperViewer = new JasperViewer( jasperPrint, true );
                jasperViewer.setVisible( true );
//                JasperPrintManager.printReport( jasperPrint, false );

            }
            else
            {
                JOptionPane.showMessageDialog( null, "Nao Existe produto com este codigo!..." );
            }
        }
        catch ( JRException jex )
        {
            jex.printStackTrace();
            //System.out.println("aqui");
            JOptionPane.showMessageDialog( null, "FALHA AO TENTAR MOSTRAR O PREÇO!..." );
        }
        catch ( Exception ex )
        {
            ex.printStackTrace();
            JOptionPane.showMessageDialog( null, "ERRO AO TENTAR MOSTRAR OS PREÇOS!..." );
        }
    }


    //factura - perfoma.jasper
    public static void main( String[] args )
    {
        new ImprimirPrecosNovo( "6181100231199", 12, 4, 1, "Produtos A", "" );
    }

}
