/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import controlador.exceptions.NonexistentEntityException;
import entity.DbBackupSchedule;
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
public class DbBackupScheduleJpaController implements Serializable
{

    public DbBackupScheduleJpaController( EntityManagerFactory emf )
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create( DbBackupSchedule dbBackupSchedule )
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist( dbBackupSchedule );
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

    public void edit( DbBackupSchedule dbBackupSchedule ) throws NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            dbBackupSchedule = em.merge( dbBackupSchedule );
            em.getTransaction().commit();
        }
        catch ( Exception ex )
        {
            String msg = ex.getLocalizedMessage();
            if ( msg == null || msg.length() == 0 )
            {
                Integer id = dbBackupSchedule.getPkDbBackupSchedule();
                if ( findDbBackupSchedule( id ) == null )
                {
                    throw new NonexistentEntityException( "The dbBackupSchedule with id " + id + " no longer exists." );
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
            DbBackupSchedule dbBackupSchedule;
            try
            {
                dbBackupSchedule = em.getReference( DbBackupSchedule.class, id );
                dbBackupSchedule.getPkDbBackupSchedule();
            }
            catch ( EntityNotFoundException enfe )
            {
                throw new NonexistentEntityException( "The dbBackupSchedule with id " + id + " no longer exists.", enfe );
            }
            em.remove( dbBackupSchedule );
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

    public List<DbBackupSchedule> findDbBackupScheduleEntities()
    {
        return findDbBackupScheduleEntities( true, -1, -1 );
    }

    public List<DbBackupSchedule> findDbBackupScheduleEntities( int maxResults, int firstResult )
    {
        return findDbBackupScheduleEntities( false, maxResults, firstResult );
    }

    private List<DbBackupSchedule> findDbBackupScheduleEntities( boolean all, int maxResults, int firstResult )
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select( cq.from( DbBackupSchedule.class ) );
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

    public DbBackupSchedule findDbBackupSchedule( Integer id )
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find( DbBackupSchedule.class, id );
        }
        finally
        {
            em.close();
        }
    }

    public int getDbBackupScheduleCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<DbBackupSchedule> rt = cq.from( DbBackupSchedule.class );
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
