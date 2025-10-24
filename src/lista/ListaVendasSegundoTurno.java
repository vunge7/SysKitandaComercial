/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lista;


import java.sql.Connection;
import dao.TurnoDao;
import entity.Turno;
import java.io.File;
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
import util.DVML;
import util.JPAEntityMannagerFactoryUtil;

/**
 *
 * @author Domingos Dala Vunge
 */
public class ListaVendasSegundoTurno
{

    BDConexao conexao = BDConexao.getInstancia();
    private String kid = "";
    private Turno turno;

    public ListaVendasSegundoTurno( Turno turno )
    {

        this.turno = turno;
        mostrarVendas();

    }

    public void mostrarVendas()
    {

        java.sql.Connection connection = conexao.getConnectionAtiva();
        HashMap hashMap = new HashMap();
        String relatorio = "";
        hashMap.put( "DATA_ACTUAL", this.turno.getData() );
        hashMap.put( "HORA_ABERTURA", getHora( this.turno.getHoraAbertura() ) );
        hashMap.put( "HORA_FECHO", getHora( this.turno.getHoraFecho() ) );

//        hashMap.put("TOTAL_VENDAS_GEARAIS",  conexao.getValor_Vendas_Total_Geral(
//                getData(this.turno.getData()), 
//                getHora(this.turno.getHoraAbertura()), 
//                getHora( this.turno.getHoraFecho() ) ) 
//        );
        hashMap.put( "SUBREPORT_DIR", "relatorios/" );
//        relatorio = "relatorios/VendasPrimeiroTurno.jasper";
        relatorio = "relatorios/VendasSegundoTurno_A6.jasper";

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

                JasperPrintManager.printReport( jasperPrint, false );
            }
            else
            {
                JOptionPane.showMessageDialog( null, "Nao Existe Listagem!..." );
            }
        }
        catch ( JRException jex )
        {
            jex.printStackTrace();
            //System.out.println("aqui");
            JOptionPane.showMessageDialog( null, "FALHA AO TENTAR MOSTRAR O RELATORIO!..." );
        }
        catch ( Exception ex )
        {
            ex.printStackTrace();
            JOptionPane.showMessageDialog( null, "ERRO AO TENTAR O RELATORIO!..." );
        }

    }

    private String getHora( Date hora )
    {
        return hora.getHours() + ":" + hora.getMinutes() + ":" + hora.getSeconds();
    }

    private String getData( Date data )
    {
        return ( data.getYear() + 1900 ) + "-" + ( data.getMonth() + 1 ) + "-" + data.getDate();
    }

    public static void main( String[] args )
    {

        EntityManagerFactory emf = JPAEntityMannagerFactoryUtil.em;
        TurnoDao turnoDao = new TurnoDao( emf );

        new ListaVendasSegundoTurno( turnoDao.findTurno( turnoDao.last_turno_by_designacao_primeiro_segundo_turno( DVML.PRIMEIRO_TURNO ) ) );
    }

}
