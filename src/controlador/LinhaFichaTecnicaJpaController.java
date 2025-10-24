/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;


import java.sql.Connection;
import controlador.exceptions.NonexistentEntityException;
import entity.LinhaFichaTecnica;
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
public class LinhaFichaTecnicaJpaController implements Serializable
{

    public LinhaFichaTecnicaJpaController( EntityManagerFactory emf )
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create( LinhaFichaTecnica linhaFichaTecnica )
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist( linhaFichaTecnica );
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

    public void edit( LinhaFichaTecnica linhaFichaTecnica ) throws NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            linhaFichaTecnica = em.merge( linhaFichaTecnica );
            em.getTransaction().commit();
        }
        catch ( Exception ex )
        {
            String msg = ex.getLocalizedMessage();
            if ( msg == null || msg.length() == 0 )
            {
                Integer id = linhaFichaTecnica.getId();
                if ( findLinhaFichaTecnica( id ) == null )
                {
                    throw new NonexistentEntityException( "The linhaFichaTecnica with id " + id + " no longer exists." );
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
            LinhaFichaTecnica linhaFichaTecnica;
            try
            {
                linhaFichaTecnica = em.getReference( LinhaFichaTecnica.class, id );
                linhaFichaTecnica.getId();
            }
            catch ( EntityNotFoundException enfe )
            {
                throw new NonexistentEntityException( "The linhaFichaTecnica with id " + id + " no longer exists.", enfe );
            }
            em.remove( linhaFichaTecnica );
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

    public List<LinhaFichaTecnica> findLinhaFichaTecnicaEntities()
    {
        return findLinhaFichaTecnicaEntities( true, -1, -1 );
    }

    public List<LinhaFichaTecnica> findLinhaFichaTecnicaEntities( int maxResults, int firstResult )
    {
        return findLinhaFichaTecnicaEntities( false, maxResults, firstResult );
    }

    private List<LinhaFichaTecnica> findLinhaFichaTecnicaEntities( boolean all, int maxResults, int firstResult )
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select( cq.from( LinhaFichaTecnica.class ) );
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

    public LinhaFichaTecnica findLinhaFichaTecnica( Integer id )
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find( LinhaFichaTecnica.class, id );
        }
        finally
        {
            em.close();
        }
    }

    public int getLinhaFichaTecnicaCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<LinhaFichaTecnica> rt = cq.from( LinhaFichaTecnica.class );
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
