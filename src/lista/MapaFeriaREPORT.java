/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lista;

import com.mysql.jdbc.Connection;
import dao.DadosInstituicaoDao;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import util.BDConexao;
import util.JPAEntityMannagerFactoryUtil;

/**
 *
 * @author Domingos Dala Vunge
 */
public class MapaFeriaREPORT
{

    private EntityManagerFactory emf = JPAEntityMannagerFactoryUtil.em;
    private Date data_inicio, data_fim;
    private DadosInstituicaoDao dadoIntituicaoDao = new DadosInstituicaoDao( emf );
    private int idEmpresa = 0;

    public MapaFeriaREPORT( int idEmpresa, Date data_inicio, Date data_fim )
    {
        this.idEmpresa = idEmpresa;
        this.data_inicio = data_inicio;
        this.data_fim = data_fim;

        mostrar();
    }

    public void mostrar()
    {

        Connection connection = null;
        try
        {
            connection = ( Connection ) BDConexao.getConexao();
        }
        catch ( SQLException ex )
        {
            Logger.getLogger( MapaFeriaREPORT.class.getName() ).log( Level.SEVERE, null, ex );
        }

        //Meus Parametros
        HashMap hashMap = new HashMap();
        hashMap.put( "PARM_ID_EMPRESA", this.idEmpresa );
        hashMap.put( "PARM_DATA_INICIO", this.data_inicio );
        hashMap.put( "PARM_DATA_FIM", this.data_fim );
        hashMap.put( "NOME_INSTITUICAO", this.dadoIntituicaoDao.findTbDadosInstituicao( 1 ).getNome() );
        String caminho = getCaminho();
        //Minha Coleção de Dados 
        try
        {

            JasperFillManager.fillReport( caminho, hashMap, connection );
            JasperPrint jasperPrint = JasperFillManager.fillReport( caminho, hashMap, connection );
            JasperViewer jasperViewer = new JasperViewer( jasperPrint, false );
            jasperViewer.setVisible( true );

        }
        catch ( JRException ex )
        {
            ex.printStackTrace();
        }

    }

    private String getCaminho()
    {

        return "relatorios/mapa_ferias_fucnionario.jasper";

    }

    public static void main( String[] args )
    {
        new MapaFeriaREPORT( 1, new Date(), new Date() );
    }

}
