/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;


import java.sql.Connection;
import controlador.exceptions.NonexistentEntityException;
import entity.TbPagamentoCredito;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entity.TbVenda;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author lenovo
 */
public class TbPagamentoCreditoJpaController implements Serializable
{

    public TbPagamentoCreditoJpaController( EntityManagerFactory emf )
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create( TbPagamentoCredito tbPagamentoCredito )
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            TbVenda codigoVenda = tbPagamentoCredito.getCodigoVenda();
            if ( codigoVenda != null )
            {
                codigoVenda = em.getReference( codigoVenda.getClass(), codigoVenda.getCodigo() );
                tbPagamentoCredito.setCodigoVenda( codigoVenda );
            }
            em.persist( tbPagamentoCredito );
            if ( codigoVenda != null )
            {
                codigoVenda.getTbPagamentoCreditoList().add( tbPagamentoCredito );
                codigoVenda = em.merge( codigoVenda );
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

    public void edit( TbPagamentoCredito tbPagamentoCredito ) throws NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            TbPagamentoCredito persistentTbPagamentoCredito = em.find( TbPagamentoCredito.class, tbPagamentoCredito.getCodigo() );
            TbVenda codigoVendaOld = persistentTbPagamentoCredito.getCodigoVenda();
            TbVenda codigoVendaNew = tbPagamentoCredito.getCodigoVenda();
            if ( codigoVendaNew != null )
            {
                codigoVendaNew = em.getReference( codigoVendaNew.getClass(), codigoVendaNew.getCodigo() );
                tbPagamentoCredito.setCodigoVenda( codigoVendaNew );
            }
            tbPagamentoCredito = em.merge( tbPagamentoCredito );
            if ( codigoVendaOld != null && !codigoVendaOld.equals( codigoVendaNew ) )
            {
                codigoVendaOld.getTbPagamentoCreditoList().remove( tbPagamentoCredito );
                codigoVendaOld = em.merge( codigoVendaOld );
            }
            if ( codigoVendaNew != null && !codigoVendaNew.equals( codigoVendaOld ) )
            {
                codigoVendaNew.getTbPagamentoCreditoList().add( tbPagamentoCredito );
                codigoVendaNew = em.merge( codigoVendaNew );
            }
            em.getTransaction().commit();
        }
        catch ( Exception ex )
        {
            String msg = ex.getLocalizedMessage();
            if ( msg == null || msg.length() == 0 )
            {
                Integer id = tbPagamentoCredito.getCodigo();
                if ( findTbPagamentoCredito( id ) == null )
                {
                    throw new NonexistentEntityException( "The tbPagamentoCredito with id " + id + " no longer exists." );
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
            TbPagamentoCredito tbPagamentoCredito;
            try
            {
                tbPagamentoCredito = em.getReference( TbPagamentoCredito.class, id );
                tbPagamentoCredito.getCodigo();
            }
            catch ( EntityNotFoundException enfe )
            {
                throw new NonexistentEntityException( "The tbPagamentoCredito with id " + id + " no longer exists.", enfe );
            }
            TbVenda codigoVenda = tbPagamentoCredito.getCodigoVenda();
            if ( codigoVenda != null )
            {
                codigoVenda.getTbPagamentoCreditoList().remove( tbPagamentoCredito );
                codigoVenda = em.merge( codigoVenda );
            }
            em.remove( tbPagamentoCredito );
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

    public List<TbPagamentoCredito> findTbPagamentoCreditoEntities()
    {
        return findTbPagamentoCreditoEntities( true, -1, -1 );
    }

    public List<TbPagamentoCredito> findTbPagamentoCreditoEntities( int maxResults, int firstResult )
    {
        return findTbPagamentoCreditoEntities( false, maxResults, firstResult );
    }

    private List<TbPagamentoCredito> findTbPagamentoCreditoEntities( boolean all, int maxResults, int firstResult )
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select( cq.from( TbPagamentoCredito.class ) );
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

    public TbPagamentoCredito findTbPagamentoCredito( Integer id )
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find( TbPagamentoCredito.class, id );
        }
        finally
        {
            em.close();
        }
    }

    public int getTbPagamentoCreditoCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TbPagamentoCredito> rt = cq.from( TbPagamentoCredito.class );
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
