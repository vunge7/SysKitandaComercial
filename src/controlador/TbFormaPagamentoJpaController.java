/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;


import java.sql.Connection;
import controlador.exceptions.NonexistentEntityException;
import entity.TbFormaPagamento;
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
public class TbFormaPagamentoJpaController implements Serializable
{

    public TbFormaPagamentoJpaController( EntityManagerFactory emf )
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create( TbFormaPagamento tbFormaPagamento )
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist( tbFormaPagamento );
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

    public void edit( TbFormaPagamento tbFormaPagamento ) throws NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            tbFormaPagamento = em.merge( tbFormaPagamento );
            em.getTransaction().commit();
        }
        catch ( Exception ex )
        {
            String msg = ex.getLocalizedMessage();
            if ( msg == null || msg.length() == 0 )
            {
                Integer id = tbFormaPagamento.getPkFormaPagamento();
                if ( findTbFormaPagamento( id ) == null )
                {
                    throw new NonexistentEntityException( "The tbFormaPagamento with id " + id + " no longer exists." );
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
            TbFormaPagamento tbFormaPagamento;
            try
            {
                tbFormaPagamento = em.getReference( TbFormaPagamento.class, id );
                tbFormaPagamento.getPkFormaPagamento();
            }
            catch ( EntityNotFoundException enfe )
            {
                throw new NonexistentEntityException( "The tbFormaPagamento with id " + id + " no longer exists.", enfe );
            }
            em.remove( tbFormaPagamento );
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

    public List<TbFormaPagamento> findTbFormaPagamentoEntities()
    {
        return findTbFormaPagamentoEntities( true, -1, -1 );
    }

    public List<TbFormaPagamento> findTbFormaPagamentoEntities( int maxResults, int firstResult )
    {
        return findTbFormaPagamentoEntities( false, maxResults, firstResult );
    }

    private List<TbFormaPagamento> findTbFormaPagamentoEntities( boolean all, int maxResults, int firstResult )
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select( cq.from( TbFormaPagamento.class ) );
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

    public TbFormaPagamento findTbFormaPagamento( Integer id )
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find( TbFormaPagamento.class, id );
        }
        finally
        {
            em.close();
        }
    }

    public int getTbFormaPagamentoCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TbFormaPagamento> rt = cq.from( TbFormaPagamento.class );
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
