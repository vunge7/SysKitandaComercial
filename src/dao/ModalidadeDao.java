/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;


import java.sql.Connection;
import controlador.ModalidadeJpaController;
import entity.Modalidade;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import static util.DVML.SELECCIONE;

/**
 *
 * @author Dallas
 */
public class ModalidadeDao extends ModalidadeJpaController
{

    public ModalidadeDao( EntityManagerFactory emf )
    {
        super( emf );
    }

    public List<Modalidade> getAllModalidade()
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT c.designacao FROM Modalidade c" );

        List<Modalidade> result = query.getResultList();
        em.close();

        if ( result != null )
        {
            return result;
        }
        return null;
    }

    public List<String> buscaTodosSeleccione()
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT s.designacao FROM Modalidade s" );
        List<String> resutl = query.getResultList();
        resutl.add( 0, SELECCIONE );
        return resutl;
    }

    public List<String> getTodasModalidades()
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT m.designacao FROM Modalidade m" );

        List<String> lista = query.getResultList();

        lista.add( 0, "--Selecione--" );

        return lista;

    }

    public Modalidade getModalidadeByDescricao( String designacao )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT c FROM Modalidade  c WHERE c.designacao = :designacao" )
                .setParameter( "designacao", designacao );

        List<Modalidade> result = query.getResultList();
        em.close();
//               if (!result.isEmpty()) {
//            return result.get(0);
//        }
//        return null;
        if ( result != null )
        {
            return result.get( 0 );
        }
        return null;
    }

    public boolean comparar( String x )
    {
        EntityManager em = getEntityManager();

        Query query = em.createQuery( "SELECT s FROM Modalidade s WHERE s.designacao = :x" )
                .setParameter( "x", x );

        List<Modalidade> result = query.getResultList();

        return !result.isEmpty();

    }
    
    public List<Modalidade> buscaTodasModalidades()
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT p FROM Modalidade p  ORDER BY p.pkModalidade" );
        return query.getResultList();
    }
    
    public boolean exist_modalidade( String designacao )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT s FROM Modalidade s WHERE s.designacao = :designacao" )
                .setParameter( "designacao", designacao );

        List result = query.getResultList();

        if ( !result.isEmpty() )
        {
            return true;
        }
        return false;
    }

}
