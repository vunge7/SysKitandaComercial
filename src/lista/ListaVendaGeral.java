/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lista;


import java.sql.Connection;
import dao.VendaDao;
import entity.TbVenda;
import java.io.File;
import java.sql.SQLException;
import java.util.HashMap;
import javax.persistence.EntityManagerFactory;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import util.BDConexao;
import util.DVML.Abreviacao;
import static util.DVML.CAMINHO_REPORT;
import static util.DVML.VERSION_SOFTWARE;
import static util.DVML.NAME_SOFTWARE;
import util.JPAEntityMannagerFactoryUtil;

/**
 *
 * @author Domingos Dala Vunge
 */
public class ListaVendaGeral
{

    private EntityManagerFactory emf = JPAEntityMannagerFactoryUtil.em;
    private VendaDao vendaDao = new VendaDao( emf );
    private BDConexao conexao;
    private int codigo;
    private double valor_entregue, troco;
    private boolean performance, factura_simplificada;
    private Abreviacao doc_breviacao;
    private String status_documento;
    private String motivo_isencao = "";

    public ListaVendaGeral( int codigo, Abreviacao doc_abrevicao, boolean performance, boolean factura_simplificada, String status_documento )
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

    public ListaVendaGeral( int codigo, Abreviacao doc_abrevicao, boolean performance, boolean factura_simplificada, String status_documento, String motivo_isencao )
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

    public void mostrarVenda() throws SQLException
    {

        Connection connection = ( Connection ) BDConexao.getConexao();
        HashMap hashMap = new HashMap();
        this.motivo_isencao = "IVA-Regime de não sujeição";
        hashMap.put( "CODIGO_VENDA", this.codigo );
        hashMap.put( "DOCUMENTO", vendaDao.findTbVenda( codigo ).getFkDocumento().getDesignacao() );
        hashMap.put( "SOFTWARE_VERSION", VERSION_SOFTWARE );
        hashMap.put( "SOFTWARE_NAME", NAME_SOFTWARE );
        hashMap.put( "REF_COD_FACT", getRefCodFact( vendaDao.findTbVenda( codigo ).getRefCodFact() ) );
        hashMap.put( "STATUS_DOCUMENTO", this.status_documento );
        hashMap.put( "MOTIVO_ISENCAO", this.motivo_isencao );
        hashMap.put( "NIF_CLIENTE_CONSOMIDOR_FINAL", setConsumidorFinal( vendaDao.findTbVenda( codigo ) ) );

        String relatorio = getCaminho();

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
                //Imprime directamente
//                if ( !performance )
//                {
//                    JasperPrintManager.printReport( jasperPrint, false );
//                }

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
            case FR_A6:
                
                return CAMINHO_REPORT + "facturaA6.jasper";
//                return CAMINHO_REPORT + "factura_A5_8.jasper";
                
//            case FR_S:
//                
//                return CAMINHO_REPORT + "factura_A5_1.jasper";
                
            case FA:
                return CAMINHO_REPORT + "facturaA4_normal.jasper";
            case PP:
                return CAMINHO_REPORT + "factura_proforma.jasper";
            case RC:
                return CAMINHO_REPORT + "recibo.jasper";
            case GT:
                return CAMINHO_REPORT + "guia_transporteA4.jasper";
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
        Abreviacao abreviacao = Abreviacao.FR_A4;
        new ListaVendaGeral( 16, abreviacao, false, false, "Original" );
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
