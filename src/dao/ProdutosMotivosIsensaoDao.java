/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;


import java.sql.Connection;
import controlador.ProdutosMotivosIsensaoJpaController;
import entity.ProdutosMotivosIsensao;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

/**
 *
 * @author Toshiba
 */
public class ProdutosMotivosIsensaoDao extends ProdutosMotivosIsensaoJpaController
{

    public ProdutosMotivosIsensaoDao ( EntityManagerFactory emf )
    {
        super ( emf );
    }

    public List<ProdutosMotivosIsensao> findAll ()
    {
        EntityManager em = getEntityManager ();
        Query query = em.createQuery ( "SELECT  e  FROM ProdutosMotivosIsensao e " );

        List<ProdutosMotivosIsensao> result = query.getResultList ();
        em.close ();

        if (  ! result.isEmpty () )
        {
            return result;
        }

        return null;

    }

    public ProdutosMotivosIsensao findByRegime ( String designacao )
    {
        EntityManager em = getEntityManager ();
        Query query = em.createQuery ( "SELECT  e  FROM ProdutosMotivosIsensao e WHERE e.regime = :REGIME" );

        query.setParameter ( "REGIME", designacao );

        List<ProdutosMotivosIsensao> result = query.getResultList ();
        em.close ();

        if (  ! result.isEmpty () )
        {
            return result.get ( 0);
        }

        return null;

    }

}
