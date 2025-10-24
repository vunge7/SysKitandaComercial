/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;


import java.sql.Connection;
import controlador.AnoEconomicoJpaController;
//import controller.AnoEconomicoJpaController;
import entity.AnoEconomico;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import static util.JPAEntityMannagerFactoryUtil.em;

/**
 *
 * @author Domingos Dala Vunge
 */
public class AnoEconomicoDao extends AnoEconomicoJpaController
{

    public AnoEconomicoDao()
    {
        super( em );
    }

    public AnoEconomicoDao( EntityManagerFactory emf )
    {
        super( emf );
    }

    public List<AnoEconomico> buscaTodos()
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT s.designacao FROM AnoEconomico s ORDER BY s.designacao DESC" );
        return query.getResultList();
    }

    public List<AnoEconomico> buscaTodosObject()
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT s FROM AnoEconomico s ORDER BY s.designacao DESC" );
        return query.getResultList();
    }

    public AnoEconomico getLastObject()
    {
        return findAnoEconomico( getLastAnoEconomico() );
    }

    public int getLastAnoEconomico()
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT  MAX(a.pkAnoEconomico)  FROM AnoEconomico  a" );

        List<Integer> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result.get( 0 );
        }

        return 0;

    }

    public int getIdByDescricao( String designacao )
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT s.pkAnoEconomico FROM AnoEconomico s WHERE s.designacao = :designacao" )
                .setParameter( "designacao", designacao );
        List result = query.getResultList();

        if ( result != null )
        {
            return Integer.parseInt( String.valueOf( result.get( 0 ) ) );
        }
        return 0;

    }

    public boolean existe_ano_economico( String ano )
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT s FROM AnoEconomico s WHERE s.designacao = :designacao" )
                .setParameter( "designacao", ano );
        List result = query.getResultList();

        return ( result != null );

    }

}
