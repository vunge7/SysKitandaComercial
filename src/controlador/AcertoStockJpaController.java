/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;


import java.sql.Connection;
import controlador.exceptions.NonexistentEntityException;
import entity.AcertoStock;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author lenovo
 */
public class AcertoStockJpaController implements Serializable
{

    public AcertoStockJpaController( EntityManagerFactory emf )
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create( AcertoStock acertoStock )
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist( acertoStock );
            em.getTransaction().commit();
        }
        finally
        {
            if ( em != null )
            {
                em.close();
            }
        }
    }

    public void edit( AcertoStock acertoStock ) throws NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            acertoStock = em.merge( acertoStock );
            em.getTransaction().commit();
        }
        catch ( Exception ex )
        {
            String msg = ex.getLocalizedMessage();
            if ( msg == null || msg.length() == 0 )
            {
                Long id = acertoStock.getPkAcertoStock();
                if ( findAcertoStock( id ) == null )
                {
                    throw new NonexistentEntityException( "The acertoStock with id " + id + " no longer exists." );
                }
            }
            throw ex;
        }
        finally
        {
            if ( em != null )
            {
                em.close();
            }
        }
    }

    public void destroy( Long id ) throws NonexistentEntityException
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            AcertoStock acertoStock;
            try
            {
                acertoStock = em.getReference( AcertoStock.class, id );
                acertoStock.getPkAcertoStock();
            }
            catch ( EntityNotFoundException enfe )
            {
                throw new NonexistentEntityException( "The acertoStock with id " + id + " no longer exists.", enfe );
            }
            em.remove( acertoStock );
            em.getTransaction().commit();
        }
        finally
        {
            if ( em != null )
            {
                em.close();
            }
        }
    }

    public List<AcertoStock> findAcertoStockEntities()
    {
        return findAcertoStockEntities( true, -1, -1 );
    }

    public List<AcertoStock> findAcertoStockEntities( int maxResults, int firstResult )
    {
        return findAcertoStockEntities( false, maxResults, firstResult );
    }

    private List<AcertoStock> findAcertoStockEntities( boolean all, int maxResults, int firstResult )
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select( cq.from( AcertoStock.class ) );
            Query q = em.createQuery( cq );
            if ( !all )
            {
                q.setMaxResults( maxResults );
                q.setFirstResult( firstResult );
            }
            return q.getResultList();
        }
        finally
        {
            em.close();
        }
    }

    public AcertoStock findAcertoStock( Long id )
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find( AcertoStock.class, id );
        }
        finally
        {
            em.close();
        }
    }

    public int getAcertoStockCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<AcertoStock> rt = cq.from( AcertoStock.class );
            cq.select( em.getCriteriaBuilder().count( rt ) );
            Query q = em.createQuery( cq );
            return ( ( Long ) q.getSingleResult() ).intValue();
        }
        finally
        {
            em.close();
        }
    }
    
}
