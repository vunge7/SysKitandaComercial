/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import controlador.TbFuncionarioJpaController;
import entity.TbConta;
import entity.TbFuncionario;
import entity.TbItemSubsidiosFuncionario;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import util.BDConexao;
import util.DVML;
import util.JPAEntityMannagerFactoryUtil;
import util.MetodosUtil;
import static util.MetodosUtil.conversaoHoraEmDia;
import util.ResumoTrabalhoUtil;

/**
 *
 * @author Domingos Dala Vunge
 */
public class FuncionarioDao extends TbFuncionarioJpaController
{

    public FuncionarioDao( EntityManagerFactory emf )
    {
        super( emf );
    }

    public int getUltimoCodFuncionario()
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT MAX(p.idFuncionario) FROM TbFuncionario p" );

        List<Integer> result = query.getResultList();
        em.close();
        if ( !result.isEmpty() )
        {
            return result.get( 0 );
        }
        return 0;

    }

    public List<TbFuncionario> buscaTodos()
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT s FROM TbFuncionario s ORDER BY s.nome" );
        return query.getResultList();
    }

    public List<TbFuncionario> buscaTodosNome()
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT s.nome FROM TbFuncionario s" );
        return query.getResultList();
    }

    public List<TbFuncionario> buscaFuncoes()
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT s.fkFuncao.designacao FROM TbFuncionario s" );
        return query.getResultList();
    }

    public List<TbFuncionario> buscaModalidades()
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT s.fkModalidade.designacao FROM TbFuncionario s" );
        return query.getResultList();
    }

    public List<TbFuncionario> buscaTodosNomeByIdEmpresa( int pkEmpresa )
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT s FROM TbFuncionario s WHERE s.fkEmpresa.pkEmpresa =:pkEmpresa " )
                .setParameter( "pkEmpresa", pkEmpresa );
        return query.getResultList();
    }

    public List<String> buscaTodosNomeByIdEmpresaCombo( int pkEmpresa )
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT s.nome FROM TbFuncionario s WHERE s.fkEmpresa.pkEmpresa =:pkEmpresa " )
                .setParameter( "pkEmpresa", pkEmpresa );
        return query.getResultList();
    }

    public List<TbFuncionario> buscaTodosNomeByNomeEmpresa( String nome )
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT s.nome FROM TbFuncionario s WHERE s.fkEmpresa.nome =:nome " )
                .setParameter( "nome", nome );
        return query.getResultList();
    }

    public List<TbFuncionario> buscaTodosProfessores()
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT s.nome FROM TbFuncionario s WHERE s.fkFuncao.pkFuncao = 4 ORDER BY s.nome" );
        return query.getResultList();
    }

    public String getNomeByIdFuncionario( long idFuncionario )
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT s.nome FROM TbFuncionario s WHERE s.idFuncionario = :idFuncionario" )
                .setParameter( "idFuncionario", idFuncionario );

        List list = query.getResultList();

        if ( list != null )
        {
            return String.valueOf( list.get( 0 ) );
        }
        return "";
    }

    public int getIdFuncionarioByNome( String nome )
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT s.idFuncionario FROM TbFuncionario s WHERE s.nome = :nome" )
                .setParameter( "nome", nome );

        List result = query.getResultList();

        if ( result != null )
        {
            return Integer.parseInt( String.valueOf( result.get( 0 ) ) );
        }
        return 0;

    }

    public boolean exist_professor( String nome )
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT s FROM TbFuncionario s WHERE s.nome = :nome" )
                .setParameter( "nome", nome );

        List result = query.getResultList();

        if ( !result.isEmpty() )
        {
            return true;
        }
        return false;
    }

    public boolean exist_professor( int cod_funcionario )
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT s FROM TbFuncionario s WHERE s.idFuncionario = :cod_funcionario" )
                .setParameter( "cod_funcionario", cod_funcionario );

        List result = query.getResultList();

        if ( !result.isEmpty() )
        {
            return true;
        }
        return false;
    }

    public int getDiasUltilEmpresa( int cod_funcionario )
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT s.diasInstituicao FROM TbFuncionario s WHERE s.idFuncionario = :cod_funcionario" )
                .setParameter( "cod_funcionario", cod_funcionario );

        List<String> result = query.getResultList();

        if ( !result.isEmpty() )
        {
            return Integer.parseInt( result.get( 0 ) );
        }
        return 0;
    }

    public List<TbFuncionario> getFuncionarioLIKENome( String nome )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT p.nome FROM TbFuncionario p WHERE p.nome LIKE :nome" )
                .setParameter( "nome", nome + "%" );

        List<TbFuncionario> result = query.getResultList();
        em.close();

        if ( result != null )
        {
            return result;
        }
        return null;
    }

    public List<TbFuncionario> getProcessamentoSalarioFuncionarioLIKENome( String nome )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT p.nome FROM TbFuncionario p WHERE p.nome LIKE :nome AND p.idStatusFK.idStatus = 1" )
                .setParameter( "nome", nome + "%" );

        List<TbFuncionario> result = query.getResultList();
        em.close();

        if ( result != null )
        {
            return result;
        }
        return null;
    }

//    public List<TbFuncionario> getProcessamentoSalarioFuncionarioLIKENomeByIdEmpresa( String nome, int pkEmpresa )
//    {
//
//        EntityManager em = getEntityManager();
//        Query query = em.createQuery( "SELECT p.nome FROM TbFuncionario p WHERE p.nome LIKE :nome AND p.idStatusFK.idStatus = 1 AND p.fkEmpresa.pkEmpresa =:pkEmpresa" )
//                .setParameter( "nome", nome + "%" );
//
//        List<TbFuncionario> result = query.getResultList();
//        em.close();
//
//        if ( result != null )
//        {
//            return result;
//        }
//        return null;
//    }
    public List<TbFuncionario> getProcessamentoFolhaFuncionarioLIKENome()
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT p FROM TbFuncionario p WHERE p.idStatusFK.idStatus = 1" );
//                .setParameter("nome",nome +"%");

        List<TbFuncionario> result = query.getResultList();
        em.close();

        if ( result != null )
        {
            return result;
        }
        return null;
    }

    public List<TbFuncionario> getProcessamentoFolhaFuncionarioByIdEmpresa( int idEmpresa )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT p FROM TbFuncionario p WHERE    P.fkEmpresa.pkEmpresa = :idEmpresa  AND   p.idStatusFK.idStatus = 1" )
                .setParameter( "idEmpresa", idEmpresa );
//                .setParameter("nome",nome +"%");

        List<TbFuncionario> result = query.getResultList();
        em.close();

        if ( result != null )
        {
            return result;
        }
        return null;
    }

    public List<TbFuncionario> getFuncionarioProfessoresLIKENome( String nome )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT p.nome FROM TbFuncionario p WHERE    p.fkFuncao.pkFuncao = 4 AND  p.nome LIKE :nome " )
                .setParameter( "nome", nome + "%" );

        List<TbFuncionario> result = query.getResultList();
        em.close();

        if ( result != null )
        {
            return result;
        }
        return null;
    }

    public List<TbFuncionario> getFuncionarioLIKE_Nome( String nome )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT p FROM TbFuncionario p WHERE p.nome LIKE :nome ORDER BY p.nome" )
                .setParameter( "nome", "%" + nome + "%" );

        List<TbFuncionario> result = query.getResultList();
        em.close();

        if ( result != null )
        {
            return result;
        }
        return null;
    }

    public boolean exist_username_password( String username, String password )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT a FROM TbFuncionario a WHERE a.userName = :username AND a.password = :password" )
                .setParameter( "username", username ).setParameter( "password", password );
        return !query.getResultList().isEmpty();

    }

    public TbFuncionario get_professor_nome_sobrenome( String username, String password )
    {

        System.out.println( "USER NAME :" + username );
        System.out.println( "SENHA :" + password );

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT a FROM TbFuncionario a WHERE a.userName = :username AND a.password = :password" )
                .setParameter( "username", username ).setParameter( "password", password );

        List<TbFuncionario> list = query.getResultList();

        if ( !list.isEmpty() )
        {
            return list.get( 0 );
        }
        return new TbFuncionario( 0 );

    }

    public TbFuncionario getFuncionarioByNome( String nome )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT a FROM TbFuncionario a WHERE a.nome = :nome" )
                .setParameter( "nome", nome );

        List<TbFuncionario> list = query.getResultList();

        if ( !list.isEmpty() )
        {
            return list.get( 0 );
        }
        return null;

    }

    public TbFuncionario getFuncionarioByNumeroFuncionario( String numeroFuncionario )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT a FROM TbFuncionario a WHERE a.numeroFuncionario = :numeroFuncionario" )
                .setParameter( "numeroFuncionario", numeroFuncionario );

        List<TbFuncionario> list = query.getResultList();

        if ( !list.isEmpty() )
        {
            return list.get( 0 );
        }
        return null;

    }

    public List<TbFuncionario> getFuncionarioLIKENomeByIdEmpresa( String nome, int pkEmpresa )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT p.nome FROM TbFuncionario p WHERE p.nome LIKE :nome AND p.fkEmpresa.pkEmpresa =:pkEmpresa" )
                .setParameter( "nome", nome + "%" )
                .setParameter( "pkEmpresa", pkEmpresa );

        List<TbFuncionario> result = query.getResultList();
        em.close();

        if ( result != null )
        {
            return result;
        }
        return null;
    }

    public List<TbFuncionario> getProcessamentoSalarioFuncionarioLIKENomeByIdEmpresa( String nome, int pkEmpresa )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT p.nome FROM TbFuncionario p WHERE p.fkEmpresa.pkEmpresa =:pkEmpresa AND p.nome LIKE :nome AND p.idStatusFK.idStatus = 1" )
                .setParameter( "nome", nome + "%" )
                .setParameter( "pkEmpresa", pkEmpresa );

        List<TbFuncionario> result = query.getResultList();
        em.close();

        if ( result != null )
        {
            return result;
        }
        return null;
    }

    public boolean exist_funcionario_numero_func( String numero_funcionario )
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT s FROM TbFuncionario s WHERE s.numeroFuncionario = :cod_funcionario" )
                .setParameter( "cod_funcionario", numero_funcionario );

        List result = query.getResultList();

        return !result.isEmpty();
    }

    public boolean exist_funcionario_nome( String nome )
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT s FROM TbFuncionario s WHERE s.nome = :nome" )
                .setParameter( "nome", nome );

        List result = query.getResultList();

        return !result.isEmpty();
    }

    public int getLastFuncionario()
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT  MAX(v.idFuncionario)  FROM TbFuncionario  v" );

        List<Integer> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result.get( 0 );
        }

        return 0;

    }

    //Métodos de apoio para o preenchimento da jtable pra processamento de recibos de funcionarios colectivos
    public static double calcular_remuneracao_funcionario( TbFuncionario funcionario, SalarioDao salarioDao, FaltaDao faltaDao,
            AdiantamentoDao adiantamentoDao, Date data_inicio, Date data_fim, BDConexao conexao )
    {
        //salário iliquido
        double remuneracao = getSalarioIliquido( funcionario, salarioDao, adiantamentoDao, faltaDao, data_inicio, data_fim, conexao );
        //os subisidios
        List<TbItemSubsidiosFuncionario> subisidios = funcionario.getTbItemSubsidiosFuncionarioList();

        int dias_uteis_trabalho = funcionario.getFkModalidade().getDiasUteisTrabalho();
        // double horas_de_faltas = faltaDao.getNHoraIntervaloDatasFalta( funcionario, data_inicio, data_fim, conexao , JPAEntityMannagerFactoryUtil.em);
        double horas_de_faltas = 0;

        if ( subisidios != null )
        {
            for ( TbItemSubsidiosFuncionario object : subisidios )
            {
                if ( object.getIdSubsidioFK().getIdSubsidios() == DVML.ID_SUBSIDIO_TRANSPORTE
                        || object.getIdSubsidioFK().getIdSubsidios() == DVML.ID_SUBSIDIO_ALIMENTACAO )
                {
                    remuneracao += remuneracao - faltaDao.getDescontoSubsidio( object.getValorSubsidio(), dias_uteis_trabalho, horas_de_faltas );
                }
                else
                {
                    remuneracao += object.getValorSubsidio();
                }

            }
        }

        // acrescentar as remunuerações - dallas - $$$$$
        return remuneracao;

    }

    public static double calcular_desconto_funcionario( TbFuncionario funcionario, SalarioDao salarioDao, FaltaDao faltaDao,
            AdiantamentoDao adiantamentoDao, Date data_inicio, Date data_fim, BDConexao conexao )
    {

        double desconto = 0d;

        desconto += getDescontoIRT( funcionario, salarioDao, adiantamentoDao, faltaDao, data_inicio, data_fim, conexao );
        desconto += getDescontoINSS3( funcionario, salarioDao );
        //menos outros desconto - dallas - $$$$$

        return desconto;

    }

    public static double getSalarioBase( int idFuncionario, SalarioDao salarioDao )
    {
        return salarioDao.getLastSalario( idFuncionario ).getValorTempo();
    }

    public static double getDiasTrabalho( TbFuncionario funcionario, FaltaDao faltaDao, Date data_inicio, Date data_fim, BDConexao conexao )
    {
        return ResumoTrabalhoUtil.getDiasUteisTrabalho( faltaDao, funcionario, data_inicio, data_fim, conexao );

    }

    public static double getSalarioDiario( TbFuncionario funcionario, SalarioDao salarioDao )
    {
        Double salarioBase = getSalarioBase( funcionario.getIdFuncionario(), salarioDao );
        return ( salarioBase / funcionario.getFkModalidade().getDiasUteisTrabalho() );
    }

    public static double getDiasFalta( TbFuncionario funcionario, FaltaDao faltaDao, Date data_inicio, Date data_fim, BDConexao conexao )
    {
        double numerosHorasFaltadas = faltaDao.getNumeroHorasFaltasByIdFuncionario( funcionario.getIdFuncionario(), data_inicio, data_fim, false, conexao );
        return conversaoHoraEmDia( numerosHorasFaltadas );

    }

    public static double getDescontoDiario( TbFuncionario funcionario, SalarioDao salarioDao, FaltaDao faltaDao, Date data_inicio, Date data_fim, BDConexao conexao )
    {
        return getDiasFalta( funcionario, faltaDao, data_inicio, data_fim, conexao ) * getSalarioDiario( funcionario, salarioDao );
    }

    public static double getDescontoINSS3( TbFuncionario funcionario, SalarioDao salarioDao )
    {
        if ( funcionario.getDescontoSegurancaSocial().equals( "Sim" ) )
        {
            return getSalarioBase( funcionario.getIdFuncionario(), salarioDao ) * 0.03;
        }
        return 0d;
    }

    public static double getDescontoINSS8( TbFuncionario funcionario, SalarioDao salarioDao )
    {
        return getSalarioBase( funcionario.getIdFuncionario(), salarioDao ) * 0.08;
    }

    public static double getAdiantamentos( TbFuncionario funcionario, AdiantamentoDao adiantamentoDao, Date data_inicio, Date data_fim )
    {
        return adiantamentoDao.getNumeroAdiantamentosByIdFuncionario( funcionario.getIdFuncionario(), data_inicio, data_fim );
    }

    public static double getSalarioIliquido( TbFuncionario funcionario,
            SalarioDao salarioDao,
            AdiantamentoDao adiantamentoDao,
            FaltaDao faltaDao, Date data_inicio, Date data_fim, BDConexao conexao )
    {
        return getSalarioBase( funcionario.getIdFuncionario(), salarioDao )
                - getDescontoDiario( funcionario, salarioDao, faltaDao, data_inicio, data_fim, conexao )
                - getAdiantamentos( funcionario, adiantamentoDao, data_inicio, data_fim );
    }

    public double getSalarioLiquido( TbFuncionario funcionario, SalarioDao salarioDao, AdiantamentoDao adiantamentoDao, FaltaDao faltaDao, Date data_inicio, Date data_fim, BDConexao conexao )
    {

        return getSalarioIliquido( funcionario, salarioDao, adiantamentoDao, faltaDao, data_inicio, data_fim, conexao )
                - getDescontoINSS3( funcionario, salarioDao )
                - getDescontoINSS8( funcionario, salarioDao )
                - getDescontoIRT( funcionario, salarioDao, adiantamentoDao, faltaDao, data_inicio, data_fim, conexao );
    }

    public static double getDescontoIRT( TbFuncionario funcionario, SalarioDao salarioDao, AdiantamentoDao adiantamentoDao, FaltaDao faltaDao, Date data_inicio, Date data_fim, BDConexao conexao )
    {
        return new MetodosUtil().calculo_irt( getSalarioIliquido( funcionario, salarioDao, adiantamentoDao, faltaDao, data_inicio, data_fim, conexao ), null, 0 );
    }

    public TbFuncionario getLastObject()
    {
        return findTbFuncionario( getLastFuncionario() );

    }

    public int getFuncionarioByNomes( String nome )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT a FROM TbFuncionario a WHERE a.nome = :nome" )
                .setParameter( "nome", nome );

        List<TbFuncionario> result = query.getResultList();
        em.close();
        if ( !result.isEmpty() )
        {
            return result.get( 0 ).getIdFuncionario();
        }
        return 0;

    }

    public boolean exist_funcionario( int idFuncionario )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT a FROM TbFuncionario a WHERE a.idStatusFK.idStatus = 1 AND  a.idFuncionario = :idFuncionario" )
                .setParameter( "idFuncionario", idFuncionario );
        return !query.getResultList().isEmpty();

    }

    public TbFuncionario getFuncionarioByCodigo( int idFuncionario )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT a FROM TbFuncionario a WHERE a.idFuncionario = :idFuncionario" )
                .setParameter( "idFuncionario", idFuncionario );

        List<TbFuncionario> result = query.getResultList();
        em.close();
        if ( !result.isEmpty() )
        {
            return result.get( 0 );
        }
        return null;

    }

    public Vector<Vector> getAllFuncionario( String campos, String[] vector, BDConexao conexao )
    {
        Vector<Vector> matrix = new Vector<>();
        try
        {

            String query = "SELECT " + campos + " FROM funcionario_view ORDER BY nome ASC";

            ResultSet result = conexao.executeQuery( query );

            while ( result.next() )
            {
                //criar a linha
                Vector linha = new Vector();
                /**
                 * Adiciona os campos sobre a linha
                 */
                for ( int i = 0; i < vector.length; i++ )
                {
                    Object object = result.getObject( vector[i] );
                    linha.add( object );
                }
                //adiciona a linha na matrix
                matrix.add( linha );
            }

        }
        catch ( SQLException ex )
        {
            Logger.getLogger( FuncionarioDao.class.getName() ).log( Level.SEVERE, null, ex );
        }

        return matrix;
    }

    public List<TbFuncionario> getALLFuncionarioByNome20( String nome )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT a.nome FROM TbFuncionario a WHERE a.nome LIKE :nome ORDER BY a.nome" )
                //        Query query = em.createQuery("SELECT a.nome FROM Aluno a  WHERE a.nome LIKE :nome ORDER BY a.nome AND a.idStatusFK.idStatus = 1 OR a.idStatusFK.idStatus = 4")
                .setParameter( "nome", nome + "%" );

        List<TbFuncionario> result = query.getResultList();
        em.close();

        if ( result != null )
        {
            return result;
        }

        return null;
    }

    public boolean exist_funcionario( String nome )
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT s FROM TbFuncionario s WHERE s.nome = :nome" )
                .setParameter( "nome", nome );

        List result = query.getResultList();

        return !result.isEmpty();
    }

}
