/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lista;

import com.mysql.jdbc.Connection;
import dao.VendaDao;
import entity.Notas;
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
import util.DVML;
import util.DVML.Abreviacao;
import static util.DVML.CAMINHO_REPORT;
import static util.DVML.VERSION_SOFTWARE;
import static util.DVML.NAME_SOFTWARE;
import util.JPAEntityMannagerFactoryUtil;

/**
 *
 * @author Domingos Dala Vunge
 */
public class ListaNotaDebito
{

    private EntityManagerFactory emf = JPAEntityMannagerFactoryUtil.em;
    private VendaDao vendaDao = new VendaDao( emf );
    private BDConexao conexao;
    private int codigo;
    private double valor_entregue, troco;
    private boolean performance, factura_simplificada;
    private Abreviacao doc_breviacao;
    private Abreviacao abreviacao;
    private String motivo_isencao = "";

    public ListaNotaDebito( int codigo, Abreviacao doc_abrevicao, boolean performance, boolean factura_simplificada, HashMap hashMap, String motivo_isencao )
    {

        this.codigo = codigo;
        this.performance = performance;
        this.factura_simplificada = factura_simplificada;
        this.doc_breviacao = doc_abrevicao;
        this.motivo_isencao = motivo_isencao;

        try
        {
            mostrarNota( hashMap );
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

    }

//    public ListaNotaDebito(int lastNota, Abreviacao abreviacaoDocumento, boolean b, boolean b0, HashMap hashMap) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
    public void mostrarNota( HashMap hashMap ) throws SQLException
    {

        Connection connection = (Connection) BDConexao.getConexao();

        hashMap.put( "CODIGO_NOTA", this.codigo );
        hashMap.put( "DOCUMENTO", vendaDao.findTbVenda(codigo ).getFkDocumento().getDesignacao() );
        hashMap.put( "SOFTWARE_VERSION", VERSION_SOFTWARE );
        hashMap.put( "SOFTWARE_NAME", NAME_SOFTWARE );
        hashMap.put( "MOTIVO_ISENCAO", this.motivo_isencao );

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
//                if (!performance) {
//                    JasperPrintManager.printReport(jasperPrint, false);
//                }

            }
            else
            {
                JOptionPane.showMessageDialog( null, "Nao Existem Notas de credito!..." );
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

    //factura - perfoma.jasper
    // caso caixa
    public String getCaminho()
    {
        switch ( this.doc_breviacao )
        {
            case NC:
            {
                return CAMINHO_REPORT + "notaCreditoDebito.jasper";
            }
            case ND:
            {
                return CAMINHO_REPORT + "notaDebito.jasper";
            }

            default:
                return "";
        }
    }

    public static void main( String[] args ) throws JRException, SQLException
    {
        Abreviacao abreviacao = Abreviacao.ND;
        HashMap hashMap = new HashMap();

        hashMap.put( "_CLIENTE_EMAIL", DVML._NAO_INCLUIR );
        hashMap.put( "_CLIENTE_MORADA", DVML._NAO_INCLUIR );
        hashMap.put( "_CLIENTE_NIF", DVML._NAO_INCLUIR );
        hashMap.put( "_CLIENTE_NOME", DVML._NAO_INCLUIR );
        hashMap.put( "_CLIENTE_TELEFONE", DVML._NAO_INCLUIR );

//        new ListaNotaDebito(0, abreviacao, true, true, hashMap, CAMINHO_REPORT );
        new ListaNotaDebito( 40, abreviacao, false, true, hashMap, "" );
    }

}
