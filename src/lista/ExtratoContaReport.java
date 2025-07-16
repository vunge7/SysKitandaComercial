/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lista;

import java.io.File;
import java.sql.Connection;
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
public class ExtratoContaReport
{

    private EntityManagerFactory emf = JPAEntityMannagerFactoryUtil.em;
    private Date data_inicio, data_fim;
    private int cod_cliente = 0;

    public ExtratoContaReport( int cod_cliente, Date data_inicio, Date data_fim )
    {

        this.data_inicio = data_inicio;
        this.data_fim = data_fim;
        this.cod_cliente = cod_cliente;

        try
        {
            mostrar();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }

    }

    public void mostrar()
    {

        //PARM_LUCRO
        HashMap hashMap = new HashMap();

        hashMap.put( "PARM_DATA_1", this.data_inicio );
        hashMap.put( "PARM_DATA_2", this.data_fim );
        hashMap.put( "COD_CLIENTE", this.cod_cliente );

        hashMap.put( "SUBREPORT_DIR", "relatorios/" );

        String relatorio = "";

        relatorio = "relatorios/extrato_conta_cliente.jasper";

        //obter o path do relatorio
        File file = new File( relatorio ).getAbsoluteFile();
        String obterCaminho = file.getAbsolutePath();

        try
        {
            Connection connection = BDConexao.getConnection();
            JasperFillManager.fillReport( obterCaminho, hashMap, connection );
            JasperPrint jasperPrint = JasperFillManager.fillReport( obterCaminho, hashMap, connection );

            if ( jasperPrint.getPages().size() >= 1 )
            {
                JasperViewer jasperViewer = new JasperViewer( jasperPrint, false );
                jasperViewer.setVisible( true );
            }
            else
            {
                JOptionPane.showMessageDialog( null, "Não existe registro para esse intervalo de data!...", "ERROR", JOptionPane.ERROR_MESSAGE );
            }

        }
        catch ( JRException jex )
        {
            jex.printStackTrace();
            JOptionPane.showMessageDialog( null, "Falha ao mostrar o relatorio", "DVML-COMERCIAL, LDA", JOptionPane.ERROR );
        }
        catch ( Exception ex )
        {
            ex.printStackTrace();
            JOptionPane.showMessageDialog( null, "Erro ao tentar mostrar o relatorio", "DVML-COMERCIAL, LDA", JOptionPane.ERROR_MESSAGE );
        }

    }

   

    public static void main( String[] args )
    {
        
    }

}
