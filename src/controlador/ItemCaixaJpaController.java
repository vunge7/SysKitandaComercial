/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import controlador.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entity.Caixa;
import entity.FormaPagamento;
import entity.ItemCaixa;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author lenovo
 */
public class ItemCaixaJpaController implements Serializable
{

    public ItemCaixaJpaController( EntityManagerFactory emf )
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create( ItemCaixa itemCaixa )
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            Caixa fkCaixa = itemCaixa.getFkCaixa();
            if ( fkCaixa != null )
            {
                fkCaixa = em.getReference( fkCaixa.getClass(), fkCaixa.getPkCaixa() );
                itemCaixa.setFkCaixa( fkCaixa );
            }
            FormaPagamento fkFormaPagamento = itemCaixa.getFkFormaPagamento();
            if ( fkFormaPagamento != null )
            {
                fkFormaPagamento = em.getReference( fkFormaPagamento.getClass(), fkFormaPagamento.getPkFormaPagamento() );
                itemCaixa.setFkFormaPagamento( fkFormaPagamento );
            }
            em.persist( itemCaixa );
            if ( fkCaixa != null )
            {
                fkCaixa.getItemCaixaList().add( itemCaixa );
                fkCaixa = em.merge( fkCaixa );
            }
            if ( fkFormaPagamento != null )
            {
                fkFormaPagamento.getItemCaixaList().add( itemCaixa );
                fkFormaPagamento = em.merge( fkFormaPagamento );
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

    public void edit( ItemCaixa itemCaixa ) throws NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            ItemCaixa persistentItemCaixa = em.find( ItemCaixa.class, itemCaixa.getPkItemCaixa() );
            Caixa fkCaixaOld = persistentItemCaixa.getFkCaixa();
            Caixa fkCaixaNew = itemCaixa.getFkCaixa();
            FormaPagamento fkFormaPagamentoOld = persistentItemCaixa.getFkFormaPagamento();
            FormaPagamento fkFormaPagamentoNew = itemCaixa.getFkFormaPagamento();
            if ( fkCaixaNew != null )
            {
                fkCaixaNew = em.getReference( fkCaixaNew.getClass(), fkCaixaNew.getPkCaixa() );
                itemCaixa.setFkCaixa( fkCaixaNew );
            }
            if ( fkFormaPagamentoNew != null )
            {
                fkFormaPagamentoNew = em.getReference( fkFormaPagamentoNew.getClass(), fkFormaPagamentoNew.getPkFormaPagamento() );
                itemCaixa.setFkFormaPagamento( fkFormaPagamentoNew );
            }
            itemCaixa = em.merge( itemCaixa );
            if ( fkCaixaOld != null && !fkCaixaOld.equals( fkCaixaNew ) )
            {
                fkCaixaOld.getItemCaixaList().remove( itemCaixa );
                fkCaixaOld = em.merge( fkCaixaOld );
            }
            if ( fkCaixaNew != null && !fkCaixaNew.equals( fkCaixaOld ) )
            {
                fkCaixaNew.getItemCaixaList().add( itemCaixa );
                fkCaixaNew = em.merge( fkCaixaNew );
            }
            if ( fkFormaPagamentoOld != null && !fkFormaPagamentoOld.equals( fkFormaPagamentoNew ) )
            {
                fkFormaPagamentoOld.getItemCaixaList().remove( itemCaixa );
                fkFormaPagamentoOld = em.merge( fkFormaPagamentoOld );
            }
            if ( fkFormaPagamentoNew != null && !fkFormaPagamentoNew.equals( fkFormaPagamentoOld ) )
            {
                fkFormaPagamentoNew.getItemCaixaList().add( itemCaixa );
                fkFormaPagamentoNew = em.merge( fkFormaPagamentoNew );
            }
            em.getTransaction().commit();
        }
        catch ( Exception ex )
        {
            String msg = ex.getLocalizedMessage();
            if ( msg == null || msg.length() == 0 )
            {
                Integer id = itemCaixa.getPkItemCaixa();
                if ( findItemCaixa( id ) == null )
                {
                    throw new NonexistentEntityException( "The itemCaixa with id " + id + " no longer exists." );
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
            ItemCaixa itemCaixa;
            try
            {
                itemCaixa = em.getReference( ItemCaixa.class, id );
                itemCaixa.getPkItemCaixa();
            }
            catch ( EntityNotFoundException enfe )
            {
                throw new NonexistentEntityException( "The itemCaixa with id " + id + " no longer exists.", enfe );
            }
            Caixa fkCaixa = itemCaixa.getFkCaixa();
            if ( fkCaixa != null )
            {
                fkCaixa.getItemCaixaList().remove( itemCaixa );
                fkCaixa = em.merge( fkCaixa );
            }
            FormaPagamento fkFormaPagamento = itemCaixa.getFkFormaPagamento();
            if ( fkFormaPagamento != null )
            {
                fkFormaPagamento.getItemCaixaList().remove( itemCaixa );
                fkFormaPagamento = em.merge( fkFormaPagamento );
            }
            em.remove( itemCaixa );
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

    public List<ItemCaixa> findItemCaixaEntities()
    {
        return findItemCaixaEntities( true, -1, -1 );
    }

    public List<ItemCaixa> findItemCaixaEntities( int maxResults, int firstResult )
    {
        return findItemCaixaEntities( false, maxResults, firstResult );
    }

    private List<ItemCaixa> findItemCaixaEntities( boolean all, int maxResults, int firstResult )
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select( cq.from( ItemCaixa.class ) );
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

    public ItemCaixa findItemCaixa( Integer id )
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find( ItemCaixa.class, id );
        }
        finally
        {
            em.close();
        }
    }

    public int getItemCaixaCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ItemCaixa> rt = cq.from( ItemCaixa.class );
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
