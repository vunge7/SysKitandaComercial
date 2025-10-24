/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;


import java.sql.Connection;
import controlador.TbSalarioJpaController;
import entity.TbSalario;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

/**
 *
 * @author Domingos Dala Vunge
 */
public class SalarioDao extends TbSalarioJpaController
{

    public SalarioDao( EntityManagerFactory emf )
    {
        super( emf );
    }

    public List<TbSalario> getAllSalario()
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT a FROM  TbSalario a  ORDER BY a.idSalarioFK DESC" );

        List<TbSalario> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result;
        }
        return null;

    }

    public List<TbSalario> getAllSalarioByIdEmpresa( int pkEmpresa )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT a FROM  TbSalario a WHERE a.idFuncionarioFK.fkEmpresa.pkEmpresa =:pkEmpresa  ORDER BY a.idSalarioFK DESC" )
                .setParameter( "pkEmpresa", pkEmpresa );
        List<TbSalario> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result;
        }
        return null;

    }

    public boolean exist_preofessor( long idFuncionario )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT s FROM TbSalario s WHERE   s.idFuncionarioFK.idFuncionario = :idFuncionario" )
                .setParameter( "idFuncionario", idFuncionario );
        return !query.getResultList().isEmpty();

    }

    public double getSalarioByIdFuncionario( long idFuncionario )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT t FROM TbSalario t WHERE  t.idFuncionarioFK.idFuncionario = :idFuncionario" )
                .setParameter( "idFuncionario", idFuncionario );
        List<TbSalario> result = query.getResultList();

        if ( !result.isEmpty() )
        {
            return result.get( 0 ).getValorTempo();
        }
        else
        {
            return 0;
        }

    }

    public List<TbSalario> getSalarioLIKENome( String nome )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT a FROM TbSalario a WHERE  a.idFuncionarioFK.nome LIKE :nome ORDER BY a.idFuncionarioFK.nome" )
                .setParameter( "nome", nome + "%" );
        List<TbSalario> result = query.getResultList();
        em.close();

        if ( result != null )
        {
            return result;
        }
        return null;
    }

    public TbSalario getLastSalario( int idFuncionario )
    {
        return findTbSalario( getIdLastID( idFuncionario ) );
    }

    private int getIdLastID( int idFuncionario )
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT MAX(t.idSalarioFK) FROM TbSalario t WHERE  t.idFuncionarioFK.idFuncionario = :idFuncionario" )
                .setParameter( "idFuncionario", idFuncionario );
        List<Integer> result = query.getResultList();
        try
        {
            return result.get( 0 );
        }
        catch ( Exception e )
        {
            return 0;
        }
    }

    public TbSalario getSalarioObjectByIdFuncionario( long idFuncionario )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT t FROM TbSalario t WHERE  t.idFuncionarioFK.idFuncionario = :idFuncionario" )
                .setParameter( "idFuncionario", idFuncionario );
        List<TbSalario> result = query.getResultList();
        try
        {

            return result.get( 0 );
        }
        catch ( Exception e )
        {
            return null;
        }

    }

}
