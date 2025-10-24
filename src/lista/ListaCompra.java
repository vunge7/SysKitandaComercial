/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lista;


import java.sql.Connection;
import dao.ComprasDao;
import dao.VendaDao;
import entity.Compras;
import entity.TbVenda;
import java.io.File;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import visao.CompraVisao;
import static visao.NotaLevantamentoVisao.btnProcessar;

/**
 *
 * @author Domingos Dala Vunge
 */
public class ListaCompra
{

    private EntityManagerFactory emf = JPAEntityMannagerFactoryUtil.em;
    private ComprasDao comprasDao = new ComprasDao( emf );
   
    private BDConexao conexao;
    private int codigo;
    private double valor_entregue, troco;
    private boolean performance, factura_simplificada;
    private Abreviacao doc_breviacao;
    private String status_documento;
    private String motivo_isencao = "";

    public ListaCompra( int codigo)
    {

        this.codigo = codigo;

        try
        {
            mostrarCompra();
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

    }

    
    public void mostrarCompra() throws SQLException
    {
        try
        {

            java.sql.Connection connection = ( java.sql.Connection ) BDConexao.getConexao();
            HashMap hashMap = new HashMap();

            hashMap.put( "PK_COMPRA", this.codigo );
            hashMap.put( "REF_COD_FACT", getRefCodFact( comprasDao.findCompras(codigo ).getRefCodFact() ) );
            String relatorio = CAMINHO_REPORT + "compra_copia.jasper";;

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
        catch ( SQLException ex )
        {
            Logger.getLogger( CompraVisao.class.getName() ).log( Level.SEVERE, null, ex );
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


    public static void main( String[] args ) throws JRException, SQLException
    {

        new ListaCompra( 11);
    }


}
