/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lista;

import dao.ArmazemDao;
import dao.DocumentoDao;
import dao.ItemVendaDao;
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
import static util.DVML.CAMINHO_REPORT;
import util.JPAEntityMannagerFactoryUtil;

/**
 *
 * @author Domingos Dala Vunge
 */
public class ResumoCompra
{

    private EntityManagerFactory emf = JPAEntityMannagerFactoryUtil.em;
    private ItemVendaDao itemVendaDao = new ItemVendaDao( emf );
    private ArmazemDao armazemDao = new ArmazemDao( emf );
    private DocumentoDao documentoDao = new DocumentoDao( emf );
    private Date data_incio, data_fim;
    private int id_armazem = 0;
    private int id_documento, id_fornecedor;
    private String relatorio = "";

    public ResumoCompra( Date data_incio, Date data_fim, int id_documento )
    {

        this.data_incio = data_incio;
        this.data_fim = data_fim;
        this.id_documento = id_documento;

        try
        {
            relatorio = CAMINHO_REPORT + "relatorio_compra.jasper";
            mostrar();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }

    }

    public ResumoCompra( Date data_incio, Date data_fim, int id_documento, int id_fornecedor )
    {

        this.data_incio = data_incio;
        this.data_fim = data_fim;
        this.id_documento = id_documento;
        this.id_fornecedor = id_fornecedor;
        try
        {
            relatorio = CAMINHO_REPORT + "relatorio_compra_by_fornecedor.jasper";
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
        hashMap.put( "DESIGNACAO_DOCUMENTO", this.documentoDao.findDocumento( id_documento ).getDesignacao() );
        hashMap.put( "CODIGO_ARMAZEM", id_armazem );
        hashMap.put( "PARM_DOCUMENTO", id_documento );
        hashMap.put( "PARM_ID_FORNECEDOR", id_fornecedor );

        System.out.println( id_fornecedor );
        System.out.println( relatorio );
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

    private double getLucro()
    {
        return itemVendaDao.lucro( data_incio, data_fim, id_armazem, id_documento );
    }

    public static void main( String[] args )
    {

    }

}
