/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lista;


import java.sql.Connection;
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
import static util.DVML.CAMINHO_REPORT;
import util.JPAEntityMannagerFactoryUtil;
import util.MetodosUtil;
import util.email.EnvioEmailUtil;

/**
 *
 * @author Domingos Dala Vunge
 */
public final class ExporToPdfForSandEmailReport
{

    //relatorio_dirario_produto_top
    //relatorio_dirario_resumo_caixa
    //relatorio_dirario_mes_caixa
    private EntityManagerFactory emf = JPAEntityMannagerFactoryUtil.em;
    private DadosInstituicaoDao dadosInstituicaoDao = new DadosInstituicaoDao( emf );

    
    
    public ExporToPdfForSandEmailReport()
    {
        try
        {
            exportReportToPdf( "relatorio_diario_produto_top.jasper", "relatorio_diario_produto_top.pdf" );
            exportReportToPdf( "relatorio_diario_resumo_caixa.jasper", "relatorio_diario_resumo_caixa.pdf" );
            exportReportToPdf( "relatorio_diario_mes_caixa.jasper", "relatorio_diario_mes_caixa.pdf" );
            EnvioEmailUtil.inicio();
        }
        catch ( SQLException e )
        {
            JOptionPane.showMessageDialog( null, "Falha a visualizar o report.\nEroor: " + e.getLocalizedMessage() );
        }
    }

    public HashMap getParametros()
    {
        HashMap hashMap = new HashMap();
        //this.motivo_isencao = "Regime Transitório";
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

        Connection connection = (Connection) BDConexao.getConexao();
        String caminho_and_file = CAMINHO_REPORT + name_file_retport;
        File file = new File( caminho_and_file ).getAbsoluteFile();
        String caminhoAbsoluto = file.getAbsolutePath();

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
            JOptionPane.showMessageDialog( null, "Falha ao converter o report em pdf.\nErro: " + jex.getLocalizedMessage(), "Erro", JOptionPane.ERROR_MESSAGE );
        }
        catch ( Exception ex )
        {
            JOptionPane.showMessageDialog( null, "Falha ao converter o report em pdf.\nErro: " + ex.getLocalizedMessage(), "Erro", JOptionPane.ERROR_MESSAGE );
        }
    }

    public static void main( String[] args ) throws JRException, SQLException
    {
        ExporToPdfForSandEmailReport r = new ExporToPdfForSandEmailReport();
    }

}
