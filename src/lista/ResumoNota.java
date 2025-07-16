/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lista;

import dao.ArmazemDao;
import dao.DocumentoDao;
import dao.ItemNotaDao;
import dao.ItemVendaDao;
import entity.Notas;
import entity.TbVenda;
import java.io.File;
import java.sql.Connection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
public class ResumoNota
{

    private EntityManagerFactory emf = JPAEntityMannagerFactoryUtil.em;
    private ItemNotaDao itemNotaDao = new ItemNotaDao( emf );
    private ArmazemDao armazemDao = new ArmazemDao( emf );
    private DocumentoDao documentoDao = new DocumentoDao( emf );
    private Date data_incio, data_fim;
    private int id_armazem = 0;
    private List<Notas> list = null;
    private int id_documento;

    public ResumoNota( Date data_incio, Date data_fim, int id_armazem, List<Notas> list )
    {

        this.data_incio = data_incio;
        this.data_fim = data_fim;
        this.id_armazem = id_armazem;
        this.list = list;
        this.id_documento = id_documento;
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
        hashMap.put( "ARMAZEM", this.armazemDao.findTbArmazem( id_armazem ).getDesignacao() );
//        hashMap.put( "DESIGNACAO_DOCUMENTO", this.documentoDao.findDocumento( id_documento ).getDesignacao() );
        hashMap.put( "CODIGO_ARMAZEM", id_armazem );
//        hashMap.put( "PARM_DOCUMENTO", id_documento );
//        hashMap.put( "PARM_LUCRO", getLucro() );
//        System.out.println( "LUCRO" + getLucro() );
        hashMap.put( "SUBREPORT_DIR", "relatorios/" );

        //obter o path do relatorio
        String relatorio = "relatorios/princ_relatorio_diario_nota.jasper";

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

//    private double getLucro()
//    {
//        return itemNotaDao.lucro( data_incio, data_fim, id_armazem, id_documento );
//    }

    public static void main( String[] args )
    {

        EntityManagerFactory emf = JPAEntityMannagerFactoryUtil.em;
        ItemVendaDao itemVendaDao = new ItemVendaDao( emf );

//        System.out.println( "LUCRO: " + itemVendaDao.lucro( new Date(), new Date() ) );

    }

}
