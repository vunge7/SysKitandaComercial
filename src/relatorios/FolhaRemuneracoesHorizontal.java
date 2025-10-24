/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package relatorios;


import java.sql.Connection;
import dao.DadosInstituicaoDao;
import entity.TbFuncionario;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import util.RemuneracaoUtil;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;
import util.JPAEntityMannagerFactoryUtil;

/**
 *
 * @author Domingos Dala Vunge
 */
public class FolhaRemuneracoesHorizontal
{

    private EntityManagerFactory emf = JPAEntityMannagerFactoryUtil.em;
    private DadosInstituicaoDao dadoIntituicaoDao = new DadosInstituicaoDao( emf );
    private List<RemuneracaoUtil> lista = new ArrayList();
    private TbFuncionario funcionario;
    private int opcao;
    private String periodo = "", ano = "";

    public FolhaRemuneracoesHorizontal( List<RemuneracaoUtil> folhaRemuneracoes, String periodo, String ano, int opcao )
    {

        lista = folhaRemuneracoes;
        this.opcao = opcao;
        this.periodo = periodo;
        this.ano = ano;
        mostrar();

    }

    public void mostrar()
    {

        //Meus Parametros
        HashMap hashMap = new HashMap();
        hashMap.put( "PERIODO", this.periodo );
        hashMap.put( "ANO", this.ano );
        hashMap.put( "NOME_INSTITUICAO", this.dadoIntituicaoDao.findTbDadosInstituicao( 1 ).getNome() );
        hashMap.put( "ENDERECO", this.dadoIntituicaoDao.findTbDadosInstituicao( 1 ).getEnderecos() );
        hashMap.put( "TELEFONE", this.dadoIntituicaoDao.findTbDadosInstituicao( 1 ).getTelefone() + "/" + this.dadoIntituicaoDao.findTbDadosInstituicao( 1 ).getTelefone() );
        hashMap.put( "NIF", this.dadoIntituicaoDao.findTbDadosInstituicao( 1 ).getNif() );
        hashMap.put( "NIF_SOCIAL", this.dadoIntituicaoDao.findTbDadosInstituicao( 1 ).getNif() );
        String caminho = getCaminho();
        //Minha Coleção de Dados 
        try
        {

            JasperReport report = JasperCompileManager.compileReport( caminho );

            JasperPrint print = JasperFillManager.fillReport( report, hashMap, new JRBeanCollectionDataSource( lista ) );

            JasperViewer jasperViewer = new JasperViewer( print, false );
            jasperViewer.setVisible( true );

        }
        catch ( JRException ex )
        {
            ex.printStackTrace();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }

    }

    private String getCaminho()
    {
        if ( opcao == 1)
        {
            return "relatorios/folha_remuneracoes.jrxml";
            
        }
        else 
        {
            return "relatorios/folha_irt.jrxml";
        }

    }

    public static void main( String[] args )
    {
        RemuneracaoUtil remuneracaoUtil = new RemuneracaoUtil();
        remuneracaoUtil.setNomeFuncionario( "Domingos Dal Vunge" );
        remuneracaoUtil.setCategoria( "Director de Projectos" );
        remuneracaoUtil.setNinss( "1234567" );
        remuneracaoUtil.setBase( 100000 );
        remuneracaoUtil.setSalararioIliquido( 100000 );
        remuneracaoUtil.setSalararioIliquido( 30 );
        remuneracaoUtil.setAdicional( 0 );
        remuneracaoUtil.setSalararioIliquido( 100000 );
        remuneracaoUtil.setInss3( 0 );
        remuneracaoUtil.setInss8( 8000 );
        remuneracaoUtil.setIrt( 1250 );
        remuneracaoUtil.setIrt( 98000 );

        List<RemuneracaoUtil> list = new ArrayList<>();
        list.add( remuneracaoUtil );
        list.add( remuneracaoUtil );
        list.add( remuneracaoUtil );
        list.add( remuneracaoUtil );
        list.add( remuneracaoUtil );
        list.add( remuneracaoUtil );
        list.add( remuneracaoUtil );

        FolhaRemuneracoesHorizontal folhaRemuneracoesHorizontal = new FolhaRemuneracoesHorizontal( list, "Julho", "2020", 2 );

    }

}
