/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lista;

import com.mysql.jdbc.Connection;
import dao.DadosInstituicaoDao;
import dao.VendaDao;
import entity.TbDadosInstituicao;
import entity.TbVenda;
import java.io.File;
import java.sql.SQLException;
import java.util.HashMap;
import javax.persistence.EntityManagerFactory;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.view.JasperViewer;
import static org.eclipse.persistence.config.ParameterDelimiterType.Hash;
import util.BDConexao;
import util.DVML.Abreviacao;
import static util.DVML.CAMINHO_REPORT;
import static util.DVML.VERSION_SOFTWARE;
import static util.DVML.NAME_SOFTWARE;
import util.JPAEntityMannagerFactoryUtil;
import util.MetodosUtil;
import util.email.EnvioEmailUtil;
import static visao.NotaLevantamentoVisao.btnProcessar;

/**
 *
 * @author Domingos Dala Vunge
 */
public class ListaVenda1
{

    private EntityManagerFactory emf = JPAEntityMannagerFactoryUtil.em;
    private VendaDao vendaDao = new VendaDao( emf );
    private DadosInstituicaoDao dadosInstituicaoDao = new DadosInstituicaoDao( emf );
    private TbDadosInstituicao dadosInstituicao;

    private BDConexao conexao;
    private int codigo;
    private double valor_entregue, troco;
    private boolean performance, factura_simplificada;
    private Abreviacao doc_breviacao;
    private String status_documento;
    private String motivo_isencao = "";

    public ListaVenda1( int codigo, Abreviacao doc_abrevicao, boolean performance, boolean factura_simplificada, String status_documento )
    {

        this.codigo = codigo;
        this.performance = performance;
        this.factura_simplificada = factura_simplificada;
        this.doc_breviacao = doc_abrevicao;
        this.status_documento = status_documento;

        try
        {
            mostrarVenda();
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

    }

    public ListaVenda1( int codigo, Abreviacao doc_abrevicao, boolean performance, boolean factura_simplificada, String status_documento, String motivo_isencao, String not )
    {

        this.codigo = codigo;
        this.performance = performance;
        this.factura_simplificada = factura_simplificada;
        this.doc_breviacao = doc_abrevicao;
        this.status_documento = status_documento;
        this.motivo_isencao = motivo_isencao;
        try
        {
            mostrarVendaPOS();
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

    }
    
    public ListaVenda1( int codigo, Abreviacao doc_abrevicao, boolean performance, boolean factura_simplificada, String status_documento, String motivo_isencao )
    {

        this.codigo = codigo;
        this.performance = performance;
        this.factura_simplificada = factura_simplificada;
        this.doc_breviacao = doc_abrevicao;
        this.status_documento = status_documento;
        this.motivo_isencao = motivo_isencao;
        try
        {
            mostrarVenda();
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

    }

    private Connection getConexao()
    {

        try
        {
            return ( Connection ) BDConexao.getConexao();
        }
        catch ( SQLException e )
        {
        }

        return null;

    }

    private HashMap getParamentros()
    {
        HashMap hashMap = new HashMap();
        hashMap.put( "CODIGO_VENDA", this.codigo );
        hashMap.put( "DOCUMENTO", vendaDao.findTbVenda( codigo ).getFkDocumento().getDesignacao() );
        hashMap.put( "SOFTWARE_VERSION", VERSION_SOFTWARE );
        hashMap.put( "SOFTWARE_NAME", NAME_SOFTWARE );
        hashMap.put( "REF_COD_FACT", getRefCodFact( vendaDao.findTbVenda( codigo ).getRefCodFact() ) );
        hashMap.put( "STATUS_DOCUMENTO", this.status_documento );
        hashMap.put( "MOTIVO_ISENCAO", this.motivo_isencao );
        hashMap.put( "NIF_CLIENTE_CONSOMIDOR_FINAL", setConsumidorFinal( vendaDao.findTbVenda( codigo ) ) );
//        hashMap.put( "REGIME", dadosInstituicao.getRegime() );
        return hashMap;

    }

    public void mostrarVenda() throws SQLException
    {

//        this.motivo_isencao = "Motivo de Exclusão";
        //this.motivo_isencao = "Regime Transitório";

        String relatorio = getCaminho();

        File file = new File( relatorio ).getAbsoluteFile();
        String obterCaminho = file.getAbsolutePath();

        try
        {
            JasperFillManager.fillReport( obterCaminho, getParamentros(), getConexao() );
            JasperPrint jasperPrint = JasperFillManager.fillReport( obterCaminho, getParamentros(), getConexao() );
            if ( jasperPrint.getPages().size() >= 1 )
            {
                JasperViewer jasperViewer = new JasperViewer( jasperPrint, false );
                switch ( this.doc_breviacao )
                {

                    case FR_A6:
                    {
                        jasperViewer.setVisible( false );
                        //Imprime directamente
                        if ( !performance )
                        {
                            JasperPrintManager.printReport( jasperPrint, false );
                        }
                    }
                    break;
                    case FR_A6_O:
                    {
                        jasperViewer.setVisible( false );
                        //Imprime directamente
                        if ( !performance )
                        {
                            JasperPrintManager.printReport( jasperPrint, false );
                        }
                    }
                    break;
                    case FR_A6_Com_Virgula:
                    {
                        jasperViewer.setVisible( false );
                        //Imprime directamente
                        if ( !performance )
                        {
                            JasperPrintManager.printReport( jasperPrint, false );
                        }
                    }
                    break;
                    case FR_SA7:
                    {
                        jasperViewer.setVisible( false );
                        //Imprime directamente
                        if ( !performance )
                        {
                            JasperPrintManager.printReport( jasperPrint, false );
                        }
                    }
                    break;
                    case FR_S_A6:
                    case FR_S_A6_O:
                    case FR_A4:
                    case FR_A4_Duplicado:
                    case FA:
                    case FT_A4_Duplicado:
                    case PP:
                    case RC:
                    case GT:
                        jasperViewer.setVisible( true );
                        break;
                    case NL:
//                        EnvioEmailUtil.inicio("nota_levantamento_A4.pdf");
                        btnProcessar.setEnabled( true );
                        jasperViewer.setVisible( true );

                        break;
                }

            }
            else
            {
                JOptionPane.showMessageDialog( null, "Nao Existem Operações!..." );
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
            JOptionPane.showMessageDialog( null, "DOCUMENTO NÃO FINALIZADO\nPOR FAVOR, REPITA A OPERAÇÃO!..." );
        }
    }
    public void mostrarVendaPOS() throws SQLException
    {

//        this.motivo_isencao = "Motivo de Exclusão";
        //this.motivo_isencao = "Regime Transitório";

        String relatorio = getCaminho();

        File file = new File( relatorio ).getAbsoluteFile();
        String obterCaminho = file.getAbsolutePath();

        try
        {
            JasperFillManager.fillReport( obterCaminho, getParamentros(), getConexao() );
            JasperPrint jasperPrint = JasperFillManager.fillReport( obterCaminho, getParamentros(), getConexao() );
            if ( jasperPrint.getPages().size() >= 1 )
            {
                JasperViewer jasperViewer = new JasperViewer( jasperPrint, false );
                switch ( this.doc_breviacao )
                {

                    case FR_A6:
                    {
                        jasperViewer.setVisible( true );
                        //Imprime directamente
//                        if ( !performance )
//                        {
//                            JasperPrintManager.printReport( jasperPrint, false );
//                        }
                    }
                    break;
                    case FR_A6_O:
                    {
                        jasperViewer.setVisible( false );
                        //Imprime directamente
                        if ( !performance )
                        {
                            JasperPrintManager.printReport( jasperPrint, false );
                        }
                    }
                    break;
                    case FR_A6_Com_Virgula:
                    {
                        jasperViewer.setVisible( false );
                        //Imprime directamente
                        if ( !performance )
                        {
                            JasperPrintManager.printReport( jasperPrint, false );
                        }
                    }
                    break;
                    case FR_SA7:
                    {
                        jasperViewer.setVisible( false );
                        //Imprime directamente
                        if ( !performance )
                        {
                            JasperPrintManager.printReport( jasperPrint, false );
                        }
                    }
                    break;
                    case FR_S_A6:
                    case FR_S_A6_O:
                    case FR_A4:
                    case FR_A4_Duplicado:
                    case FA:
                    case FT_A4_Duplicado:
                    case PP:
                    case RC:
                    case GT:
                        jasperViewer.setVisible( true );
                        break;
                    case NL:
//                        EnvioEmailUtil.inicio("nota_levantamento_A4.pdf");
                        btnProcessar.setEnabled( true );
                        jasperViewer.setVisible( true );

                        break;
                }

            }
            else
            {
                JOptionPane.showMessageDialog( null, "Nao Existem Operações!..." );
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

    private String getRefCodFact( String ref_cod )
    {

        if ( ref_cod == null )
        {
            return null;
        }
        return "Ref. à Doc." + ref_cod;

    }

    //factura - perfoma.jasper
    // caso caixa
    public String getCaminho()
    {

        //System.err.println(Abreviacao.FR_A4);
        switch ( this.doc_breviacao )
        {

            case FR_A4:
                return CAMINHO_REPORT + "facturaA4.jasper";

            case FR_A4_Duplicado:
                return CAMINHO_REPORT + "facturaA4_duplicado.jasper";

            case FR_A6:
                return CAMINHO_REPORT + "facturaA6.jasper";
                
            case FR_A6_O:
                return CAMINHO_REPORT + "facturaA6_O.jasper";
                
            case FR_S_A6_O:
                return CAMINHO_REPORT + "facturaA6_O.jasper";


            case FR_S_A6:
                return CAMINHO_REPORT + "facturaA6.jasper";
                
            case EL:
                return CAMINHO_REPORT + "facturaA6.jasper";

            case FR_A6_Com_Virgula:
                return CAMINHO_REPORT + "facturaA6V.jasper";

            case FR_SA7:
                return CAMINHO_REPORT + "facturaA7.jasper";

            case FA:
//                return CAMINHO_REPORT + "facturaA4_normal_transitario.jasper";
                return CAMINHO_REPORT + "facturaA4_normal.jasper";

            case FT_A4_Duplicado:
                return CAMINHO_REPORT + "facturaA4_normal_duplicado.jasper";

            case PP:
//                return CAMINHO_REPORT + "vistoria.jasper";
                return CAMINHO_REPORT + "factura_proforma.jasper";

            case RC:
                return CAMINHO_REPORT + "recibos.jasper";

            case GT:
                return CAMINHO_REPORT + "guia_transportes_A4.jasper";
            case NL:
                try
                {
//                    MetodosUtil.exportReportToPdf("nota_levantamento_A4.jasper", "nota_levantamento_A4.pdf", getParamentros(), getConexao());                    
                }
                catch ( Exception e )
                {
                }
                return CAMINHO_REPORT + "nota_levantamento_A4.jasper";
            default:
                return "";

        }

    }

    private String isCredito()
    {
        TbVenda venda = vendaDao.findTbVenda( codigo );
        if ( venda.getCredito().equals( "false" ) )
        {
            return "";
        }
        else
        {
            return "Crédito";
        }
    }

    public static void main( String[] args ) throws JRException, SQLException
    {
        Abreviacao abreviacao = Abreviacao.NL;
        new ListaVenda1( 28, abreviacao, false, false, "Original" );
    }

    private String setConsumidorFinal( TbVenda venda )
    {
        if ( venda.getCodigoCliente().getCodigo() == 1 )
        {
            return "Consumidor Final";
        }
        return venda.getClienteNif();
    }

}
