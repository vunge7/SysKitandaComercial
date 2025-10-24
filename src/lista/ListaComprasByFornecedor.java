/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lista;


import java.sql.Connection;
import dao.ComprasDao;
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
import static util.DVML.*;
import util.JPAEntityMannagerFactoryUtil;

/**
 *
 * @author Domingos Dala Vunge
 */
public class ListaComprasByFornecedor
{

    private EntityManagerFactory emf = JPAEntityMannagerFactoryUtil.em;
    private ComprasDao comprasDao = new ComprasDao( emf );
    private BDConexao conexao;
    private int pk_compra;
    private double valor_entregue, troco;
    private boolean performance, factura_simplificada;
    private Abreviacao doc_breviacao;
    private String status_documento;
    private String motivo_isencao = "";

    public ListaComprasByFornecedor( int codigo, Abreviacao doc_abrevicao, boolean performance, boolean factura_simplificada, String status_documento, String motivo_isencao )
    {

        this.pk_compra = codigo;
        this.performance = performance;
        this.factura_simplificada = factura_simplificada;
        this.doc_breviacao = doc_abrevicao;
        this.status_documento = status_documento;
        this.motivo_isencao = motivo_isencao;
        try
        {
            mostrarSolicitacaoCompras();
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

    }

    public void mostrarSolicitacaoCompras() throws SQLException
    {

        Connection connection = ( Connection ) BDConexao.getConexao();
        HashMap hashMap = new HashMap();
        //this.motivo_isencao = "IVA-Regime de não sujeição";
//        this.motivo_isencao = "Regime Transitório";
        hashMap.put( "CODIGO_COMPRA", this.pk_compra );
        hashMap.put( "DOCUMENTO", comprasDao.findCompras(pk_compra ).getFkDocumento().getDesignacao() );
        hashMap.put( "SOFTWARE_VERSION", VERSION_SOFTWARE );
        hashMap.put( "SOFTWARE_NAME", NAME_SOFTWARE );
        hashMap.put( "REF_COD_FACT", getRefCodFact( comprasDao.findCompras( pk_compra ).getRefCodFact() ) );
        hashMap.put( "STATUS_DOCUMENTO", this.status_documento );
        hashMap.put( "MOTIVO_ISENCAO", this.motivo_isencao );

        String relatorio = getCaminho();

        File file = new File( relatorio ).getAbsoluteFile();
        String obterCaminho = file.getAbsolutePath();

                try {
            JasperFillManager.fillReport(obterCaminho, hashMap, connection);
            JasperPrint jasperPrint = JasperFillManager.fillReport(obterCaminho, hashMap, connection);

            if ( jasperPrint.getPages().size() >= 1 ) {
                
                JasperViewer jasperViewer = new JasperViewer(jasperPrint, false);
                jasperViewer.setVisible(true);              
                //Imprime directamente
//                if(!performance)
//                    JasperPrintManager.printReport(jasperPrint, true);       
                
            } else {
                JOptionPane.showMessageDialog(null, "documento vazio");
            }            
        } catch (JRException jex) {
            jex.printStackTrace();
            //System.out.println("aqui");
            JOptionPane.showMessageDialog( null, "FALHA AO TENTAR MOSTRAR O DOCUMENTO!..." );
        }
        catch ( Exception ex )
        {
            ex.printStackTrace();
            JOptionPane.showMessageDialog( null, "ERRO AO EFECTUAR O DOCUMENTO!..." );
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

        switch ( this.doc_breviacao )
        {
            case SO:
                return CAMINHO_REPORT + "requisicaoCompras.jasper";
            case NE:
                return CAMINHO_REPORT + "doc_encomenda.jasper";
            default:
                return "";

        }

    }

    public static void main( String[] args ) throws JRException, SQLException
    {
        Abreviacao abreviacao = Abreviacao.NE;
        new ListaComprasByFornecedor( 14, abreviacao, false, false, "Original", "" );
    }

}
