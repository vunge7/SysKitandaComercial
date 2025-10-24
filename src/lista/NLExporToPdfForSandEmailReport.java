/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lista;


import java.sql.Connection;
import comercial.controller.CaixasController;
import dao.DadosInstituicaoDao;
import java.io.File;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import javax.persistence.EntityManagerFactory;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import util.BDConexao;
import util.DVML;
import static util.DVML.CAMINHO_REPORT;
import util.JPAEntityMannagerFactoryUtil;
import util.MetodosUtil;
import util.email.EnvioEmailUtil;

/**
 *
 * @author Domingos Dala Vunge
 */
public final class NLExporToPdfForSandEmailReport
{

    //relatorio_dirario_produto_top
    //relatorio_dirario_resumo_caixa
    //relatorio_dirario_mes_caixa
    private EntityManagerFactory emf = JPAEntityMannagerFactoryUtil.em;
    private DadosInstituicaoDao dadosInstituicaoDao = new DadosInstituicaoDao( emf );
    private CaixasController caixasController;

    public NLExporToPdfForSandEmailReport( BDConexao conexao )
    {
        try
        {
            caixasController = new CaixasController( conexao );
            exportReportToPdf( "relatorio_diario_resumo_caixa_lavandaria.jasper", "relatorio_diario_resumo_caixa_lavandaria.pdf" );
            exportReportToPdf( "relatorio_diario_mes_caixa.jasper", "relatorio_diario_mes_caixa.pdf" );

        }
        catch ( SQLException e )
        {
            JOptionPane.showMessageDialog( null, "Falha a visualizar o report.\nEroor: " + e.getLocalizedMessage() );
        }
    }

    public HashMap getParametros()
    {

        int firstId = caixasController.getFirstIdCaixaByDataAbertura( new Date() );
        int lastId = caixasController.getLastCaixaByDataAbertura( new Date() );
        HashMap hashMap = new HashMap();
        //this.motivo_isencao = "Regime Transitório";
        hashMap.put( "ID_DOCUMENTO", DVML.DOC_FACTURA_RECIBO_FR );
        hashMap.put( "DATA_1", caixasController.buscar_data_abertura_by_id( firstId ) );
        hashMap.put( "DATA_2", caixasController.buscar_data_feicho_by_id( lastId ) );

        hashMap.put( "NOME_EMPRESA", dadosInstituicaoDao.findTbDadosInstituicao( 1 ).getNome() );
        hashMap.put( "NIF_EMPRESA", dadosInstituicaoDao.findTbDadosInstituicao( 1 ).getNif() );
        hashMap.put( "ENDERECO_EMPRESA", dadosInstituicaoDao.findTbDadosInstituicao( 1 ).getEnderecos() );
        hashMap.put( "TOP", 20 );
        hashMap.put( "ANO", ( new Date().getYear() + 1900 ) );
        hashMap.put( "MES", MetodosUtil.getMesPagarU( new Date().getMonth() + 1 ) );
        hashMap.put( "ID_MES", ( new Date().getMonth() + 1 ) );
        return hashMap;
    }

    public void exportReportToPdf( String name_file_retport, String name_file_pdf ) throws SQLException
    {

        Connection connection = ( Connection ) BDConexao.getConexao();
        String caminho_and_file = CAMINHO_REPORT + name_file_retport;
        File file = new File( caminho_and_file ).getAbsoluteFile();
        String caminhoAbsoluto = file.getAbsolutePath();
        System.out.println( "FILE: " + name_file_retport );
        try
        {
            JasperFillManager.fillReport( caminhoAbsoluto, getParametros(), connection );
            JasperPrint jasperPrint = JasperFillManager.fillReport( caminhoAbsoluto, getParametros(), connection );
            if ( jasperPrint.getPages().size() >= 1 )
            {
                //exporta o ficheiro como pdf no directorio 'anexos'
                JasperExportManager.exportReportToPdfFile( jasperPrint, "anexos/" + name_file_pdf );
            }

        }
        catch ( JRException jex )
        {
            jex.printStackTrace();
            JOptionPane.showMessageDialog( null, "Falha ao converter o report em pdf.\nErro: " + jex.getLocalizedMessage(), "Erro", JOptionPane.ERROR_MESSAGE );
        }
        catch ( Exception ex )
        {
            ex.printStackTrace();
            JOptionPane.showMessageDialog( null, "Falha ao converter o report em pdf.\nErro: " + ex.getLocalizedMessage(), "Erro", JOptionPane.ERROR_MESSAGE );
        }
    }

    public static void main( String[] args ) throws JRException, SQLException
    {
        NLExporToPdfForSandEmailReport r = new NLExporToPdfForSandEmailReport( BDConexao.getInstancia() );
    }

}
