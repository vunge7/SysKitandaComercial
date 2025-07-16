/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import controlador.FormaPagamentoJpaController;
//import controller.AnoJpaController;
import entity.FormaPagamento;
import java.util.List;
import java.util.Vector;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

/**
 *
 * @author Domingos Dala Vunge
 */
public class FormaPagamentoDao extends FormaPagamentoJpaController
{

    public FormaPagamentoDao( EntityManagerFactory emf )
    {
        super( emf );
    }

    public List<FormaPagamento> buscaTodos()
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT s.designacao FROM FormaPagamento s" );
        return query.getResultList();
    }
    
        public Vector<String> getAllFormasPagto()
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT a.designacao FROM FormaPagamento a" );
             

        List<String> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return ( Vector ) result;
        }
        return null;
    }

    public List<FormaPagamento> buscaTodosObjects()
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT s FROM FormaPagamento s" );
        return query.getResultList();
    }
    
        public FormaPagamento getFormaPagamentoByDescricao(String designacao) {

        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT a FROM FormaPagamento  a WHERE a.designacao = :designacao")
                .setParameter("designacao", designacao);

        List<FormaPagamento> result = query.getResultList();
        em.close();

        if (result != null) {
            return result.get(0);
        }
        return null;
    }


    public int getIdByDescricao( String designacao )
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT s.pkFormaPagamento FROM FormaPagamento s WHERE s.designacao = :designacao" )
                .setParameter( "designacao", designacao );

        List result = query.getResultList();

        if ( result != null )
        {
            return Integer.parseInt( String.valueOf( result.get( 0 ) ) );
        }
        return 0;

    }
}
