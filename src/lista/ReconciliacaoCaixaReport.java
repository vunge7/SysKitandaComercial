/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lista;

import com.mysql.jdbc.Connection;
import comercial.controller.CaixasController;
import dao.DadosInstituicaoDao;
import dao.VendaDao;
import java.io.File;
import java.sql.SQLException;
import java.util.HashMap;
import javax.persistence.EntityManagerFactory;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.view.JasperViewer;
import util.BDConexao;
import util.DVML;
import static util.DVML.CAMINHO_REPORT;
import util.JPAEntityMannagerFactoryUtil;
import util.MetodosUtil;

/**
 *
 * @author Domingos Dala Vunge
 */
public class ReconciliacaoCaixaReport
{

    private EntityManagerFactory emf = JPAEntityMannagerFactoryUtil.em;
    private VendaDao vendaDao = new VendaDao( emf );
    private DadosInstituicaoDao dadosInstituicaoDao = new DadosInstituicaoDao( emf );
    private int idCaixa;
    private CaixasController caixasController;

    public ReconciliacaoCaixaReport( int pk_caixa, BDConexao conexao )
    {

        this.idCaixa = pk_caixa;
        caixasController = new CaixasController( conexao );

        try
        {
            mostrarReconciliacaoCaixa();
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

    }

    public void mostrarReconciliacaoCaixa() throws SQLException
    {

        Connection connection = ( Connection ) BDConexao.getConexao();

        HashMap hashMap = new HashMap();
        //this.motivo_isencao = "Regime Transitório";
        hashMap.put( "ID_CAIXA", this.idCaixa );
        hashMap.put( "DATA_1", caixasController.buscar_data_abertura_by_id( idCaixa ) );
        hashMap.put( "DATA_2", caixasController.buscar_data_feicho_by_id( idCaixa ) );
        hashMap.put( "ID_USUARIO", caixasController.getIdUsusarioByIdCaixa( idCaixa ) );
        hashMap.put( "ID_DOCUMENTO", DVML.DOC_FACTURA_RECIBO_FR );

        String relatorio = getCaminho();

        System.out.println( "relatorio: " + relatorio );
        System.out.println( "DATA DE ABERTURA: " + MetodosUtil.getDataBancoFull( caixasController.buscar_data_abertura_by_id( idCaixa ) ) );
        System.out.println( "DATA DO FECHO " + MetodosUtil.getDataBancoFull( caixasController.buscar_data_feicho_by_id( idCaixa ) ) );
        System.out.println( "ID_USUARIO: " + caixasController.getIdUsusarioByIdCaixa( idCaixa ) );

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
                //exporta o ficheiro como pdf no directorio 'anexos'
                JasperExportManager.exportReportToPdfFile( jasperPrint, "anexos/reconciliacao_caixa_lavandaria.pdf" );
//                JasperPrintManager.printReport( jasperPrint, false );              
                //Imprime directamente
                JasperPrintManager.printReport( jasperPrint, false );
            }
            else
            {
                JOptionPane.showMessageDialog( null, "O Documento não tem página." );
            }

        }
        catch ( JRException jex )
        {
            jex.printStackTrace();
            //System.out.println("aqui");
            JOptionPane.showMessageDialog( null, "FALHA AO TENTAR MOSTRAR A FACTURA!..." );
        }
        catch ( Exception ex )
        {
            ex.printStackTrace();
            JOptionPane.showMessageDialog( null, "ERRO AO EFECTUAR A FACTURA!..." );
        }
    }

    public String getCaminho()
    {

        String FILE ;
        String tipoFechoCaixa = dadosInstituicaoDao.findTbDadosInstituicao( 1 ).getTipoFechoCaixa();

        switch ( tipoFechoCaixa )
        {

            case "Simplificado":
                FILE = "reconciliacao_caixa_lavandaria.jasper";
                break;
            default:
                FILE = "reconciliacao_caixa.jasper";
        }

        return CAMINHO_REPORT + FILE;

    }

    public static void main( String[] args ) throws JRException, SQLException
    {
        new ReconciliacaoCaixaReport( 4, new BDConexao() );
    }

}
