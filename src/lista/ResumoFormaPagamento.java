/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lista;

import dao.ArmazemDao;
import dao.DocumentoDao;
import dao.FormaPagamentoDao;
import dao.ItemVendaDao;
import entity.TbVenda;
import java.io.File;
import java.sql.Connection;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
public class ResumoFormaPagamento
{
    
    private EntityManagerFactory emf = JPAEntityMannagerFactoryUtil.em;
    private ArmazemDao armazemDao = new ArmazemDao( emf );
    private FormaPagamentoDao formaPagamentoDao = new FormaPagamentoDao( emf );
    private Date data_incio, data_fim;
    private int id_armazem = 0;
    private int id_forma_pagamento = 0;
    private List<TbVenda> list = null;


    public ResumoFormaPagamento( Date data_incio, Date data_fim, int id_armazem )
    {

        this.data_incio = data_incio;
        this.data_fim = data_fim;
        this.id_armazem = id_armazem;
    
        try
        {
            mostrar();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }

    }
    
    public ResumoFormaPagamento( Date data_incio, Date data_fim, int id_armazem, int id_forma_pagamento )
    {

        this.data_incio = data_incio;
        this.data_fim = data_fim;
        this.id_armazem = id_armazem;
        this.id_forma_pagamento = id_forma_pagamento;
        try
        {
            mostrar_forma();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }

    }

    public void mostrar()
    {

        //PARM_LUCRO
        HashMap hashMap = new HashMap();

        hashMap.put( "PARM_DATA_1", this.data_incio );
        hashMap.put( "PARM_DATA_2", this.data_fim );
        hashMap.put( "SUBREPORT_DIR", "relatorios/" );
        hashMap.put( "ARMAZEM", this.armazemDao.findTbArmazem( id_armazem ).getDesignacao() );
        hashMap.put( "PARM_ARMAZEM", id_armazem );

        String relatorio = "relatorios/relatorio_por_forma_de_pagamentos.jasper";

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

    public void mostrar_forma()
    {

        //PARM_LUCRO
        HashMap hashMap = new HashMap();

        hashMap.put( "PARM_DATA_1", this.data_incio );
        hashMap.put( "PARM_DATA_2", this.data_fim );
        hashMap.put( "ARMAZEM", this.armazemDao.findTbArmazem( id_armazem ).getDesignacao() );
        hashMap.put( "PARM_ARMAZEM", id_armazem); 
        hashMap.put( "FORMA", this.formaPagamentoDao.findFormaPagamento(id_forma_pagamento).getDesignacao() );       
        hashMap.put( "PARM_FORMA", id_forma_pagamento);

//        String relatorio = "relatorios/relatorio_por_forma_de_pagamento_forma.jasper";
        String relatorio = "relatorios/forma.jasper";

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
                JOptionPane.showMessageDialog( null, "Não existe registro de forma pagamentos para esse intervalo de data!...", "ERROR", JOptionPane.ERROR_MESSAGE );
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
