/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;


import java.sql.Connection;
import controlador.exceptions.NonexistentEntityException;
import entity.ItensNota;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entity.Notas;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author lenovo
 */
public class ItensNotaJpaController implements Serializable
{

    public ItensNotaJpaController( EntityManagerFactory emf )
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create( ItensNota itensNota )
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            Notas fkNota = itensNota.getFkNota();
            if ( fkNota != null )
            {
                fkNota = em.getReference( fkNota.getClass(), fkNota.getPkNota() );
                itensNota.setFkNota( fkNota );
            }
            em.persist( itensNota );
            if ( fkNota != null )
            {
                fkNota.getItensNotaList().add( itensNota );
                fkNota = em.merge( fkNota );
            }
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

    public void edit( ItensNota itensNota ) throws NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            ItensNota persistentItensNota = em.find( ItensNota.class, itensNota.getPkItensNota() );
            Notas fkNotaOld = persistentItensNota.getFkNota();
            Notas fkNotaNew = itensNota.getFkNota();
            if ( fkNotaNew != null )
            {
                fkNotaNew = em.getReference( fkNotaNew.getClass(), fkNotaNew.getPkNota() );
                itensNota.setFkNota( fkNotaNew );
            }
            itensNota = em.merge( itensNota );
            if ( fkNotaOld != null && !fkNotaOld.equals( fkNotaNew ) )
            {
                fkNotaOld.getItensNotaList().remove( itensNota );
                fkNotaOld = em.merge( fkNotaOld );
            }
            if ( fkNotaNew != null && !fkNotaNew.equals( fkNotaOld ) )
            {
                fkNotaNew.getItensNotaList().add( itensNota );
                fkNotaNew = em.merge( fkNotaNew );
            }
            em.getTransaction().commit();
        }
        catch ( Exception ex )
        {
            String msg = ex.getLocalizedMessage();
            if ( msg == null || msg.length() == 0 )
            {
                Integer id = itensNota.getPkItensNota();
                if ( findItensNota( id ) == null )
                {
                    throw new NonexistentEntityException( "The itensNota with id " + id + " no longer exists." );
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
            ItensNota itensNota;
            try
            {
                itensNota = em.getReference( ItensNota.class, id );
                itensNota.getPkItensNota();
            }
            catch ( EntityNotFoundException enfe )
            {
                throw new NonexistentEntityException( "The itensNota with id " + id + " no longer exists.", enfe );
            }
            Notas fkNota = itensNota.getFkNota();
            if ( fkNota != null )
            {
                fkNota.getItensNotaList().remove( itensNota );
                fkNota = em.merge( fkNota );
            }
            em.remove( itensNota );
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

    public List<ItensNota> findItensNotaEntities()
    {
        return findItensNotaEntities( true, -1, -1 );
    }

    public List<ItensNota> findItensNotaEntities( int maxResults, int firstResult )
    {
        return findItensNotaEntities( false, maxResults, firstResult );
    }

    private List<ItensNota> findItensNotaEntities( boolean all, int maxResults, int firstResult )
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select( cq.from( ItensNota.class ) );
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

    public ItensNota findItensNota( Integer id )
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find( ItensNota.class, id );
        }
        finally
        {
            em.close();
        }
    }

    public int getItensNotaCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ItensNota> rt = cq.from( ItensNota.class );
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
