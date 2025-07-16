/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lista;

import dao.ArmazemDao;
import dao.DocumentoDao;
import dao.ItemVendaDao;
import entity.TbVenda;
import java.io.File;
import java.sql.Connection;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import javax.persistence.EntityManagerFactory;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import util.BDConexao;
import util.DVML;
import util.JPAEntityMannagerFactoryUtil;

/**
 *
 * @author Domingos Dala Vunge
 */
public class ResumoPedidosCancelados
{

    private EntityManagerFactory emf = JPAEntityMannagerFactoryUtil.em;
    private ItemVendaDao itemVendaDao = new ItemVendaDao( emf );
    private ArmazemDao armazemDao = new ArmazemDao( emf );
    private DocumentoDao documentoDao = new DocumentoDao( emf );
    private Date data_incio, data_fim;
    private int id_armazem = 0;
    private List<TbVenda> list = null;
    private int id_documento;
    private boolean mapa_iva = false;
    private boolean por_ordem = false;

    public ResumoPedidosCancelados( Date data_incio, Date data_fim )
    {

        this.data_incio = data_incio;
        this.data_fim = data_fim;
        this.id_armazem = id_armazem;
        this.list = list;
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

        hashMap.put( "PARM_DATA_1", this.data_incio );
        hashMap.put( "PARM_DATA_2", this.data_fim );

        String relatorio = "";

            relatorio = "relatorios/princ_produtos_cancelados.jasper";


        System.out.println( "Localizar Ficheiro Jasper :" + relatorio );

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

        EntityManagerFactory emf = JPAEntityMannagerFactoryUtil.em;
        ItemVendaDao itemVendaDao = new ItemVendaDao( emf );
        Calendar calendar = Calendar.getInstance();
        calendar.setTime( new Date() );
        calendar.add( Calendar.DATE, 1 );

        System.out.println( "LUCRO: " + itemVendaDao.lucro( new Date(), new Date(), 1, DVML.DOC_FACTURA_RECIBO_FR ) );

    }

}
