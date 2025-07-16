/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import controlador.exceptions.NonexistentEntityException;
import entity.ItemReciboRh;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entity.ReciboRh;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author lenovo
 */
public class ItemReciboRhJpaController implements Serializable
{

    public ItemReciboRhJpaController( EntityManagerFactory emf )
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create( ItemReciboRh itemReciboRh )
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            ReciboRh fkReciboRh = itemReciboRh.getFkReciboRh();
            if ( fkReciboRh != null )
            {
                fkReciboRh = em.getReference( fkReciboRh.getClass(), fkReciboRh.getPkReciboRh() );
                itemReciboRh.setFkReciboRh( fkReciboRh );
            }
            em.persist( itemReciboRh );
            if ( fkReciboRh != null )
            {
                fkReciboRh.getItemReciboRhList().add( itemReciboRh );
                fkReciboRh = em.merge( fkReciboRh );
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

    public void edit( ItemReciboRh itemReciboRh ) throws NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            ItemReciboRh persistentItemReciboRh = em.find( ItemReciboRh.class, itemReciboRh.getPkItemReciboRh() );
            ReciboRh fkReciboRhOld = persistentItemReciboRh.getFkReciboRh();
            ReciboRh fkReciboRhNew = itemReciboRh.getFkReciboRh();
            if ( fkReciboRhNew != null )
            {
                fkReciboRhNew = em.getReference( fkReciboRhNew.getClass(), fkReciboRhNew.getPkReciboRh() );
                itemReciboRh.setFkReciboRh( fkReciboRhNew );
            }
            itemReciboRh = em.merge( itemReciboRh );
            if ( fkReciboRhOld != null && !fkReciboRhOld.equals( fkReciboRhNew ) )
            {
                fkReciboRhOld.getItemReciboRhList().remove( itemReciboRh );
                fkReciboRhOld = em.merge( fkReciboRhOld );
            }
            if ( fkReciboRhNew != null && !fkReciboRhNew.equals( fkReciboRhOld ) )
            {
                fkReciboRhNew.getItemReciboRhList().add( itemReciboRh );
                fkReciboRhNew = em.merge( fkReciboRhNew );
            }
            em.getTransaction().commit();
        }
        catch ( Exception ex )
        {
            String msg = ex.getLocalizedMessage();
            if ( msg == null || msg.length() == 0 )
            {
                Integer id = itemReciboRh.getPkItemReciboRh();
                if ( findItemReciboRh( id ) == null )
                {
                    throw new NonexistentEntityException( "The itemReciboRh with id " + id + " no longer exists." );
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
            ItemReciboRh itemReciboRh;
            try
            {
                itemReciboRh = em.getReference( ItemReciboRh.class, id );
                itemReciboRh.getPkItemReciboRh();
            }
            catch ( EntityNotFoundException enfe )
            {
                throw new NonexistentEntityException( "The itemReciboRh with id " + id + " no longer exists.", enfe );
            }
            ReciboRh fkReciboRh = itemReciboRh.getFkReciboRh();
            if ( fkReciboRh != null )
            {
                fkReciboRh.getItemReciboRhList().remove( itemReciboRh );
                fkReciboRh = em.merge( fkReciboRh );
            }
            em.remove( itemReciboRh );
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

    public List<ItemReciboRh> findItemReciboRhEntities()
    {
        return findItemReciboRhEntities( true, -1, -1 );
    }

    public List<ItemReciboRh> findItemReciboRhEntities( int maxResults, int firstResult )
    {
        return findItemReciboRhEntities( false, maxResults, firstResult );
    }

    private List<ItemReciboRh> findItemReciboRhEntities( boolean all, int maxResults, int firstResult )
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select( cq.from( ItemReciboRh.class ) );
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

    public ItemReciboRh findItemReciboRh( Integer id )
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find( ItemReciboRh.class, id );
        }
        finally
        {
            em.close();
        }
    }

    public int getItemReciboRhCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ItemReciboRh> rt = cq.from( ItemReciboRh.class );
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
