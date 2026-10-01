/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lista;

import java.util.logging.Logger;
import comercial.controller.DocumentosController;
import comercial.controller.VendasController;
import com.mysql.jdbc.Connection;
import dao.VendaDao;
import entity.Documento;
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
import util.BDConexao;
import util.DVML.Abreviacao;
import static util.DVML.Abreviacao.FA;
import static util.DVML.Abreviacao.FR_A4;
import static util.DVML.Abreviacao.FR_A4_Duplicado;
import static util.DVML.Abreviacao.FR_A6;
import static util.DVML.Abreviacao.FR_SA7;
import static util.DVML.Abreviacao.FT_A4_Duplicado;
import static util.DVML.Abreviacao.GT;
import static util.DVML.Abreviacao.PP;
import static util.DVML.Abreviacao.RC;
import static util.DVML.CAMINHO_REPORT;
import static util.DVML.VERSION_SOFTWARE;
import static util.DVML.NAME_SOFTWARE;
import util.JPAEntityMannagerFactoryUtil;

/**
 *
 * @author Domingos Dala Vunge
 */
public class ListaVenda5
{

    private EntityManagerFactory emf = JPAEntityMannagerFactoryUtil.em;
    private VendaDao vendaDao = new VendaDao( emf );
    private BDConexao conexao;
    private VendasController vendasController;
    private DocumentosController documentosController;
    private int codigo;
    private double valor_entregue, troco;
    private boolean performance, factura_simplificada;
    private Abreviacao doc_breviacao;
    private String status_documento;
    private String motivo_isencao = "";
    private String area;
    private static final Logger logger = Logger.getLogger(ListaVenda5.class.getName() );

    public ListaVenda5( int codigo, Abreviacao doc_abrevicao, boolean performance, boolean factura_simplificada, String status_documento )
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

    public ListaVenda5( int codigo, Abreviacao doc_abrevicao, boolean performance, boolean factura_simplificada, String status_documento, String motivo_isencao )
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

    public ListaVenda5( int codigo, Abreviacao doc_abrevicao, boolean performance, boolean factura_simplificada, String status_documento, String motivo_isencao, String nada, String mais )
    {

        this.codigo = codigo;
        this.performance = performance;
        this.factura_simplificada = factura_simplificada;
        this.doc_breviacao = doc_abrevicao;
        this.status_documento = status_documento;
        this.motivo_isencao = motivo_isencao;
        try
        {
            logger.info( "Chamada primeiro" );
            mostrarVendaFtParceiros();
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

    }

    public ListaVenda5( int codigo, Abreviacao doc_abrevicao, boolean performance, boolean factura_simplificada, String status_documento, String motivo_isencao, String nada )
    {

        this.codigo = codigo;
        this.performance = performance;
        this.factura_simplificada = factura_simplificada;
        this.doc_breviacao = doc_abrevicao;
        this.status_documento = status_documento;
        this.motivo_isencao = motivo_isencao;
        try
        {
            mostrarVendaFt();
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

    }

    public void mostrarVendaFtParceiros() throws SQLException
    {

        this.motivo_isencao = "Regime Geral";
        vendasController = new VendasController( BDConexao.getBDConetion() );
        documentosController = new DocumentosController( BDConexao.getBDConetion() );
        logger.info( "Instancia criadas do vendasController e documentosController" );
        String relatorio = getCaminho_ft_parceiros();

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
                    case FR_S_A6:
                    {
                        jasperViewer.setVisible( true );

                    }
                    break;
                    case FR_SA7:
                    {
                        jasperViewer.setVisible( true );

                    }
                    break;
//                    case FR_S:
                    case FR_A4:
                    case FR_A4_Duplicado:
                    case FA:
                    case FT_A4_Duplicado:
                    case PP:
                    case RC:
                    case GT:
                    case CM:
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

    public void mostrarVendaFt() throws SQLException
    {

        this.motivo_isencao = "Regime Geral";
        //this.motivo_isencao = "Regime Transitório";
        vendasController = new VendasController( BDConexao.getBDConetion() );
        documentosController = new DocumentosController( BDConexao.getBDConetion() );
//        TbVenda venda = ( TbVenda ) v.findById( codigo );
//        area = venda.getAreaVenda();
        String relatorio = getCaminho_ft();

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
                    case FR_S_A6:
                    {
                        jasperViewer.setVisible( true );

                    }
                    break;
                    case FR_SA7:
                    {
                        jasperViewer.setVisible( true );
                    }
                    break;
//                    case FR_S:
                    case FR_A4:
                    case FR_A4_Duplicado:
                    case FA:
                    case FT_A4_Duplicado:
                    case PP:
                    case RC:
                    case GT:
                    case CM:
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

    private Connection getConexao()
    {

        try
        {
            return (Connection) BDConexao.getConexao();
        }
        catch ( SQLException e )
        {
        }

        return null;

    }

    private HashMap getParamentros()
    {
        System.err.println( "CODIGO_ID_VENDA: " + this.codigo );
        HashMap hashMap = new HashMap();
        hashMap.put( "CODIGO_VENDA", this.codigo );

        TbVenda vendaLocal = (TbVenda) vendasController.findById( codigo );
        System.err.println( "CODIGO_ID_VENDA: " + vendaLocal.getCodigo() );
        System.err.println( "ID DOCUMENTO: " + vendaLocal.getFkDocumento().getPkDocumento() );
        Documento documento = (Documento) documentosController.findById( vendaLocal.getFkDocumento().getPkDocumento() );

        hashMap.put( "DOCUMENTO", documento.getDesignacao() );
        hashMap.put( "SOFTWARE_VERSION", VERSION_SOFTWARE );
        hashMap.put( "SOFTWARE_NAME", NAME_SOFTWARE );
        hashMap.put( "REF_COD_FACT", getRefCodFact( vendaDao.findTbVenda( codigo ).getRefCodFact() ) );
        hashMap.put( "STATUS_DOCUMENTO", this.status_documento );
        hashMap.put( "MOTIVO_ISENCAO", this.motivo_isencao );
        hashMap.put( "NIF_CLIENTE_CONSOMIDOR_FINAL", setConsumidorFinal( vendaDao.findTbVenda( codigo ) ) );
        return hashMap;

    }

    public String getCaminho_ft_parceiros()
    {
        logger.info( "Ficheiro invocado facturaA4_normal_parceiros" );
        return CAMINHO_REPORT + "facturaA4_normal_parceiros.jasper";
    }

    public String getCaminho_ft()
    {
        return CAMINHO_REPORT + "facturaA4_normal_qualidade.jasper";
    }

    public void mostrarVenda() throws SQLException
    {

        this.motivo_isencao = "Regime Geral";
        vendasController = new VendasController( BDConexao.getBDConetion() );
        documentosController = new DocumentosController( BDConexao.getBDConetion() );
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
//                        jasperViewer.setVisible( false );
                        //Imprime directamente
                        if ( !performance )
                        {
                            JasperPrintManager.printReport( jasperPrint, true );
                        }
                    }
                    break;
                    case FR_S_A6:
                    {
                        jasperViewer.setVisible( true );
                    }
                    break;
                    case FR_SA7:
                    {
                        jasperViewer.setVisible( true );
                    }
                    break;
//                    case FR_S:
                    case FR_A4:
                    case FR_A4_Duplicado:
                    case FA:
                    case FT_A4_Duplicado:
                    case PP:
                    case RC:
                    case GT:
                    case CM:
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

    public String getCaminho()
    {

        switch ( this.doc_breviacao )
        {

            case FR_A4:
                return CAMINHO_REPORT + "facturaA4.jasper";

            case FR_A4_Duplicado:
                System.out.println( "DUPLICADO" );
                return CAMINHO_REPORT + "facturaA4_duplicado.jasper";

            case FR_A6:
                return CAMINHO_REPORT + "facturaA6.jasper";
            case FR_S_A6:
                return CAMINHO_REPORT + "facturaA6.jasper";

            case FR_SA7:
                return CAMINHO_REPORT + "facturaA7.jasper";

            case FA:
                return CAMINHO_REPORT + "facturaA4_normal_part.jasper";

            case FT_A4_Duplicado:
                return CAMINHO_REPORT + "facturaA4_normal.jasper";

            case PP:
                return CAMINHO_REPORT + "factura_proforma.jasper";

            case RC:
                return CAMINHO_REPORT + "recibos.jasper";

            case GT:
                return CAMINHO_REPORT + "guia_transporteA4.jasper";

            case CM:
                return CAMINHO_REPORT + "factura_proforma.jasper";
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
