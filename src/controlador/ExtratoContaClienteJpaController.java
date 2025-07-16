/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import controlador.exceptions.NonexistentEntityException;
import entity.ExtratoContaCliente;
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
public class ExtratoContaClienteJpaController implements Serializable
{

    public ExtratoContaClienteJpaController( EntityManagerFactory emf )
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create( ExtratoContaCliente extratoContaCliente )
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist( extratoContaCliente );
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

    public void edit( ExtratoContaCliente extratoContaCliente ) throws NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            extratoContaCliente = em.merge( extratoContaCliente );
            em.getTransaction().commit();
        }
        catch ( Exception ex )
        {
            String msg = ex.getLocalizedMessage();
            if ( msg == null || msg.length() == 0 )
            {
                Long id = extratoContaCliente.getPkExtratoContaCliente();
                if ( findExtratoContaCliente( id ) == null )
                {
                    throw new NonexistentEntityException( "The extratoContaCliente with id " + id + " no longer exists." );
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
            ExtratoContaCliente extratoContaCliente;
            try
            {
                extratoContaCliente = em.getReference( ExtratoContaCliente.class, id );
                extratoContaCliente.getPkExtratoContaCliente();
            }
            catch ( EntityNotFoundException enfe )
            {
                throw new NonexistentEntityException( "The extratoContaCliente with id " + id + " no longer exists.", enfe );
            }
            em.remove( extratoContaCliente );
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

    public List<ExtratoContaCliente> findExtratoContaClienteEntities()
    {
        return findExtratoContaClienteEntities( true, -1, -1 );
    }

    public List<ExtratoContaCliente> findExtratoContaClienteEntities( int maxResults, int firstResult )
    {
        return findExtratoContaClienteEntities( false, maxResults, firstResult );
    }

    private List<ExtratoContaCliente> findExtratoContaClienteEntities( boolean all, int maxResults, int firstResult )
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select( cq.from( ExtratoContaCliente.class ) );
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

    public ExtratoContaCliente findExtratoContaCliente( Long id )
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find( ExtratoContaCliente.class, id );
        }
        finally
        {
            em.close();
        }
    }

    public int getExtratoContaClienteCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ExtratoContaCliente> rt = cq.from( ExtratoContaCliente.class );
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
