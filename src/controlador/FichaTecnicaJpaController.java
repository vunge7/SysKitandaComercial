/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;


import java.sql.Connection;
import controlador.exceptions.NonexistentEntityException;
import entity.FichaTecnica;
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
 * @author marti
 */
public class FichaTecnicaJpaController implements Serializable
{

    public FichaTecnicaJpaController( EntityManagerFactory emf )
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create( FichaTecnica fichaTecnica )
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist( fichaTecnica );
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

    public void edit( FichaTecnica fichaTecnica ) throws NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            fichaTecnica = em.merge( fichaTecnica );
            em.getTransaction().commit();
        }
        catch ( Exception ex )
        {
            String msg = ex.getLocalizedMessage();
            if ( msg == null || msg.length() == 0 )
            {
                Integer id = fichaTecnica.getId();
                if ( findFichaTecnica( id ) == null )
                {
                    throw new NonexistentEntityException( "The fichaTecnica with id " + id + " no longer exists." );
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

    public void destroy( Integer id ) throws NonexistentEntityException
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            FichaTecnica fichaTecnica;
            try
            {
                fichaTecnica = em.getReference( FichaTecnica.class, id );
                fichaTecnica.getId();
            }
            catch ( EntityNotFoundException enfe )
            {
                throw new NonexistentEntityException( "The fichaTecnica with id " + id + " no longer exists.", enfe );
            }
            em.remove( fichaTecnica );
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

    public List<FichaTecnica> findFichaTecnicaEntities()
    {
        return findFichaTecnicaEntities( true, -1, -1 );
    }

    public List<FichaTecnica> findFichaTecnicaEntities( int maxResults, int firstResult )
    {
        return findFichaTecnicaEntities( false, maxResults, firstResult );
    }

    private List<FichaTecnica> findFichaTecnicaEntities( boolean all, int maxResults, int firstResult )
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select( cq.from( FichaTecnica.class ) );
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

    public FichaTecnica findFichaTecnica( Integer id )
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find( FichaTecnica.class, id );
        }
        finally
        {
            em.close();
        }
    }

    public int getFichaTecnicaCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<FichaTecnica> rt = cq.from( FichaTecnica.class );
            cq.select( em.getCriteriaBuilder().count( rt ) );
            Query q = em.createQuery( cq );
            return ( (Long) q.getSingleResult() ).intValue();
        }
        finally
        {
            em.close();
        }
    }
    
}
