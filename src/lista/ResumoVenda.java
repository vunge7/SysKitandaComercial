/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lista;

import dao.ArmazemDao;
import dao.DocumentoDao;
import dao.ItemVendaDao;
import entity.TbVenda;
import java.io.File;
import java.sql.Connection;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import javax.persistence.EntityManagerFactory;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import util.BDConexao;
import util.DVML;
import util.JPAEntityMannagerFactoryUtil;

/**
 *
 * @author Domingos Dala Vunge
 */
public class ResumoVenda
{

    private EntityManagerFactory emf = JPAEntityMannagerFactoryUtil.em;
    private ItemVendaDao itemVendaDao = new ItemVendaDao( emf );
    private ArmazemDao armazemDao = new ArmazemDao( emf );
    private DocumentoDao documentoDao = new DocumentoDao( emf );
    private Date data_incio, data_fim;
    private int id_armazem = 0;
    private List<TbVenda> list = null;
    private int id_documento;
    private boolean mapa_iva = false;
    private boolean por_ordem = false;

    public ResumoVenda( Date data_incio, Date data_fim, int id_armazem, List<TbVenda> list, int id_documento, boolean mapa_iva )
    {

        this.data_incio = data_incio;
        this.data_fim = data_fim;
        this.id_armazem = id_armazem;
        this.list = list;
        this.id_documento = id_documento;
        this.mapa_iva = mapa_iva;
        try
        {
            mostrar();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }

    }

    public ResumoVenda( Date data_incio, Date data_fim, int id_armazem, List<TbVenda> list, int id_documento, boolean mapa_iva, boolean por_ordem )
    {

        this.data_incio = data_incio;
        this.data_fim = data_fim;
        this.id_armazem = id_armazem;
        this.list = list;
        this.id_documento = id_documento;
        this.mapa_iva = mapa_iva;
        try
        {
            mostrar_por_ordem();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }

    }
    
    public ResumoVenda( Date data_incio, Date data_fim, int id_armazem, List<TbVenda> list, int id_documento )
    {

        this.data_incio = data_incio;
        this.data_fim = data_fim;
        this.id_armazem = id_armazem;
        this.list = list;
        this.id_documento = id_documento;
        this.mapa_iva = mapa_iva;
        this.por_ordem = por_ordem;
        try
        {
            mostrar_clientes();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }

    }
//    public ResumoVenda( Date data_incio, Date data_fim, int id_armazem, List<TbVenda> list, int id_documento )
//    {
//
//        this.data_incio = data_incio;
//        this.data_fim = data_fim;
//        this.id_armazem = id_armazem;
//        this.list = list;
//        this.id_documento = id_documento;
//        this.mapa_iva = mapa_iva;
//        this.por_ordem = por_ordem;
//        try
//        {
//            mostrar_por_ordem();
//        }
//        catch ( Exception e )
//        {
//            e.printStackTrace();
//        }
//
//    }

    public void mostrar()
    {

        //PARM_LUCRO
        HashMap hashMap = new HashMap();

        hashMap.put( "PARM_DATA_1", this.data_incio );
        hashMap.put( "PARM_DATA_2", this.data_fim );
        hashMap.put( "PARM_DOCUMENTO", id_documento );
        System.out.println( "LUCRO" + getLucro() );
        if ( !mapa_iva )
        {
            hashMap.put( "PARM_LUCRO", getLucro() );
            hashMap.put( "SUBREPORT_DIR", "relatorios/" );
            hashMap.put( "ARMAZEM", this.armazemDao.findTbArmazem( id_armazem ).getDesignacao() );
            System.out.println( "IDDOC: " + id_documento );
            if ( id_documento != 0 )
            {
                hashMap.put( "DESIGNACAO_DOCUMENTO", this.documentoDao.findDocumento( id_documento ).getDesignacao() );
                hashMap.put( "CODIGO_ARMAZEM", id_armazem );
            }

        }

        String relatorio = "";

        if ( id_documento == DVML.DOC_FACTURA_RECIBO_FR )
        {
            relatorio = !mapa_iva ? "relatorios/princ_relatorio_diario.jasper" : "relatorios/mapa_iva.jasper";
        }
        else if ( id_documento == DVML.DOC_FACTURA_FT )
        {
            relatorio = !mapa_iva ? "relatorios/princ_relatorio_diario_factura.jasper" : "relatorios/mapa_iva.jasper";
        }
        else if ( id_documento == DVML.DOC_NOTA_CREDITO_NC )
        {
            relatorio = "relatorios/princ_relatorio_diario_notas.jasper";
        }
        else if ( id_documento == DVML.DOC_RECIBO_RC )
        {
            relatorio = "relatorios/princ_relatorio_diario.jasper";
        }
        else if ( id_documento == DVML.DOC_FACTURA_PROFORMA_PP )
        {
            relatorio = "relatorios/princ_relatorio_diario_proformas.jasper";
        }
        else
        {
            relatorio = !mapa_iva ? "relatorios/princ_relatorio_diario_geral.jasper" : "relatorios/mapa_iva.jasper";
        }
//        if ( id_documento != DVML.DOC_FACTURA_FT )
//        {
//            relatorio = !mapa_iva ? "relatorios/princ_relatorio_diario.jasper" : "relatorios/mapa_iva.jasper";
//        }
//        else if ( id_documento == DVML.DOC_FACTURA_FT )
//        {
//            relatorio = !mapa_iva ? "relatorios/princ_relatorio_diario_factura.jasper" : "relatorios/mapa_iva.jasper";
//        }
//        else
//        {
//            relatorio = !mapa_iva ? "relatorios/princ_relatorio_diario_factura.jasper" : "relatorios/mapa_iva.jasper";
//        }
        //obter o path do relatorio

        System.out.println( "Localizar Ficheiro Jasper :" + relatorio );

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

    public void mostrar_por_ordem()
    {

        //PARM_LUCRO
        HashMap hashMap = new HashMap();

        hashMap.put( "PARM_DATA_1", this.data_incio );
        hashMap.put( "PARM_DATA_2", this.data_fim );
        hashMap.put( "PARM_DOCUMENTO", id_documento );
        System.out.println( "LUCRO" + getLucro() );
        if ( !mapa_iva )
        {
            hashMap.put( "PARM_LUCRO", getLucro() );
            hashMap.put( "SUBREPORT_DIR", "relatorios/" );
            hashMap.put( "ARMAZEM", this.armazemDao.findTbArmazem( id_armazem ).getDesignacao() );
            hashMap.put( "DESIGNACAO_DOCUMENTO", this.documentoDao.findDocumento( id_documento ).getDesignacao() );
            hashMap.put( "CODIGO_ARMAZEM", id_armazem );

        }

        String relatorio = "";

//        if ( id_documento != DVML.DOC_FACTURA_FT )
        if ( id_documento != DVML.DOC_FACTURA_FT )
        {
            relatorio = por_ordem ? "relatorios/princ_relatorio_diario_ordem.jasper" : "relatorios/princ_relatorio_diario_ordem.jasper";
        }
        else
        {
            relatorio = por_ordem ? "relatorios/princ_relatorio_diario_ordem_ft.jasper" : "relatorios/princ_relatorio_diario_ordem_ft.jasper";

        }

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
    
    public void mostrar_clientes()
    {

        //PARM_LUCRO
        HashMap hashMap = new HashMap();

        hashMap.put( "PARM_DATA_1", this.data_incio );
        hashMap.put( "PARM_DATA_2", this.data_fim );
        hashMap.put( "PARM_DOCUMENTO", id_documento );
        System.out.println( "LUCRO" + getLucro() );
        if ( !mapa_iva )
        {
            hashMap.put( "PARM_LUCRO", getLucro() );
            hashMap.put( "SUBREPORT_DIR", "relatorios/" );
            hashMap.put( "ARMAZEM", this.armazemDao.findTbArmazem( id_armazem ).getDesignacao() );
            System.out.println( "IDDOC: " + id_documento );
            if ( id_documento != 0 )
            {
                hashMap.put( "DESIGNACAO_DOCUMENTO", this.documentoDao.findDocumento( id_documento ).getDesignacao() );
                hashMap.put( "CODIGO_ARMAZEM", id_armazem );
            }

        }

        String relatorio = "";

//        if ( id_documento == DVML.DOC_FACTURA_RECIBO_FR )
//        {
            relatorio = "relatorios/princ_relatorio_diario_clientes.jasper";
//            relatorio = !mapa_iva ? "relatorios/princ_relatorio_diario_clientes.jasper" : "relatorios/mapa_iva.jasper";
//        }
//        else if ( id_documento == DVML.DOC_FACTURA_FT )
//        {
//            relatorio = !mapa_iva ? "relatorios/princ_relatorio_diario_factura.jasper" : "relatorios/mapa_iva.jasper";
//        }
//        else if ( id_documento == DVML.DOC_NOTA_CREDITO_NC )
//        {
//            relatorio = "relatorios/princ_relatorio_diario_notas.jasper";
//        }
//        else if ( id_documento == DVML.DOC_RECIBO_RC )
//        {
//            relatorio = "relatorios/princ_relatorio_diario.jasper";
//        }
//        else
//        {
//            relatorio = !mapa_iva ? "relatorios/princ_relatorio_diario_geral.jasper" : "relatorios/mapa_iva.jasper";
//        }

        System.out.println( "Localizar Ficheiro Jasper :" + relatorio );

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

        EntityManagerFactory emf = JPAEntityMannagerFactoryUtil.em;
        ItemVendaDao itemVendaDao = new ItemVendaDao( emf );
        Calendar calendar = Calendar.getInstance();
        calendar.setTime( new Date() );
        calendar.add( Calendar.DATE, 1 );

        System.out.println( "LUCRO: " + itemVendaDao.lucro( new Date(), new Date(), 1, DVML.DOC_FACTURA_RECIBO_FR ) );

    }

}
