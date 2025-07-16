/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import controlador.exceptions.NonexistentEntityException;
import entity.LinhaTransferencia;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entity.TransferenciaArmazem;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author lenovo
 */
public class LinhaTransferenciaJpaController implements Serializable
{

    public LinhaTransferenciaJpaController( EntityManagerFactory emf )
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create( LinhaTransferencia linhaTransferencia )
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            TransferenciaArmazem fkTransferenciaArmazem = linhaTransferencia.getFkTransferenciaArmazem();
            if ( fkTransferenciaArmazem != null )
            {
                fkTransferenciaArmazem = em.getReference( fkTransferenciaArmazem.getClass(), fkTransferenciaArmazem.getPkTransferenciaArmazem() );
                linhaTransferencia.setFkTransferenciaArmazem( fkTransferenciaArmazem );
            }
            em.persist( linhaTransferencia );
            if ( fkTransferenciaArmazem != null )
            {
                fkTransferenciaArmazem.getLinhaTransferenciaList().add( linhaTransferencia );
                fkTransferenciaArmazem = em.merge( fkTransferenciaArmazem );
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

    public void edit( LinhaTransferencia linhaTransferencia ) throws NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            LinhaTransferencia persistentLinhaTransferencia = em.find( LinhaTransferencia.class, linhaTransferencia.getPkLinhaTransferencia() );
            TransferenciaArmazem fkTransferenciaArmazemOld = persistentLinhaTransferencia.getFkTransferenciaArmazem();
            TransferenciaArmazem fkTransferenciaArmazemNew = linhaTransferencia.getFkTransferenciaArmazem();
            if ( fkTransferenciaArmazemNew != null )
            {
                fkTransferenciaArmazemNew = em.getReference( fkTransferenciaArmazemNew.getClass(), fkTransferenciaArmazemNew.getPkTransferenciaArmazem() );
                linhaTransferencia.setFkTransferenciaArmazem( fkTransferenciaArmazemNew );
            }
            linhaTransferencia = em.merge( linhaTransferencia );
            if ( fkTransferenciaArmazemOld != null && !fkTransferenciaArmazemOld.equals( fkTransferenciaArmazemNew ) )
            {
                fkTransferenciaArmazemOld.getLinhaTransferenciaList().remove( linhaTransferencia );
                fkTransferenciaArmazemOld = em.merge( fkTransferenciaArmazemOld );
            }
            if ( fkTransferenciaArmazemNew != null && !fkTransferenciaArmazemNew.equals( fkTransferenciaArmazemOld ) )
            {
                fkTransferenciaArmazemNew.getLinhaTransferenciaList().add( linhaTransferencia );
                fkTransferenciaArmazemNew = em.merge( fkTransferenciaArmazemNew );
            }
            em.getTransaction().commit();
        }
        catch ( Exception ex )
        {
            String msg = ex.getLocalizedMessage();
            if ( msg == null || msg.length() == 0 )
            {
                Integer id = linhaTransferencia.getPkLinhaTransferencia();
                if ( findLinhaTransferencia( id ) == null )
                {
                    throw new NonexistentEntityException( "The linhaTransferencia with id " + id + " no longer exists." );
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
            LinhaTransferencia linhaTransferencia;
            try
            {
                linhaTransferencia = em.getReference( LinhaTransferencia.class, id );
                linhaTransferencia.getPkLinhaTransferencia();
            }
            catch ( EntityNotFoundException enfe )
            {
                throw new NonexistentEntityException( "The linhaTransferencia with id " + id + " no longer exists.", enfe );
            }
            TransferenciaArmazem fkTransferenciaArmazem = linhaTransferencia.getFkTransferenciaArmazem();
            if ( fkTransferenciaArmazem != null )
            {
                fkTransferenciaArmazem.getLinhaTransferenciaList().remove( linhaTransferencia );
                fkTransferenciaArmazem = em.merge( fkTransferenciaArmazem );
            }
            em.remove( linhaTransferencia );
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

    public List<LinhaTransferencia> findLinhaTransferenciaEntities()
    {
        return findLinhaTransferenciaEntities( true, -1, -1 );
    }

    public List<LinhaTransferencia> findLinhaTransferenciaEntities( int maxResults, int firstResult )
    {
        return findLinhaTransferenciaEntities( false, maxResults, firstResult );
    }

    private List<LinhaTransferencia> findLinhaTransferenciaEntities( boolean all, int maxResults, int firstResult )
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select( cq.from( LinhaTransferencia.class ) );
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

    public LinhaTransferencia findLinhaTransferencia( Integer id )
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find( LinhaTransferencia.class, id );
        }
        finally
        {
            em.close();
        }
    }

    public int getLinhaTransferenciaCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<LinhaTransferencia> rt = cq.from( LinhaTransferencia.class );
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
