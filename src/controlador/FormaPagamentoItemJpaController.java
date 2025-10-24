/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;


import java.sql.Connection;
import controlador.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entity.FormaPagamento;
import entity.FormaPagamentoItem;
import entity.TbVenda;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author lenovo
 */
public class FormaPagamentoItemJpaController implements Serializable
{

    public FormaPagamentoItemJpaController( EntityManagerFactory emf )
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create( FormaPagamentoItem formaPagamentoItem )
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            FormaPagamento fkFormaPagamento = formaPagamentoItem.getFkFormaPagamento();
            if ( fkFormaPagamento != null )
            {
                fkFormaPagamento = em.getReference( fkFormaPagamento.getClass(), fkFormaPagamento.getPkFormaPagamento() );
                formaPagamentoItem.setFkFormaPagamento( fkFormaPagamento );
            }
            TbVenda fkVenda = formaPagamentoItem.getFkVenda();
            if ( fkVenda != null )
            {
                fkVenda = em.getReference( fkVenda.getClass(), fkVenda.getCodigo() );
                formaPagamentoItem.setFkVenda( fkVenda );
            }
            em.persist( formaPagamentoItem );
            if ( fkFormaPagamento != null )
            {
                fkFormaPagamento.getFormaPagamentoItemList().add( formaPagamentoItem );
                fkFormaPagamento = em.merge( fkFormaPagamento );
            }
            if ( fkVenda != null )
            {
                fkVenda.getFormaPagamentoItemList().add( formaPagamentoItem );
                fkVenda = em.merge( fkVenda );
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

    public void edit( FormaPagamentoItem formaPagamentoItem ) throws NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            FormaPagamentoItem persistentFormaPagamentoItem = em.find( FormaPagamentoItem.class, formaPagamentoItem.getPkFormaPagamentoItem() );
            FormaPagamento fkFormaPagamentoOld = persistentFormaPagamentoItem.getFkFormaPagamento();
            FormaPagamento fkFormaPagamentoNew = formaPagamentoItem.getFkFormaPagamento();
            TbVenda fkVendaOld = persistentFormaPagamentoItem.getFkVenda();
            TbVenda fkVendaNew = formaPagamentoItem.getFkVenda();
            if ( fkFormaPagamentoNew != null )
            {
                fkFormaPagamentoNew = em.getReference( fkFormaPagamentoNew.getClass(), fkFormaPagamentoNew.getPkFormaPagamento() );
                formaPagamentoItem.setFkFormaPagamento( fkFormaPagamentoNew );
            }
            if ( fkVendaNew != null )
            {
                fkVendaNew = em.getReference( fkVendaNew.getClass(), fkVendaNew.getCodigo() );
                formaPagamentoItem.setFkVenda( fkVendaNew );
            }
            formaPagamentoItem = em.merge( formaPagamentoItem );
            if ( fkFormaPagamentoOld != null && !fkFormaPagamentoOld.equals( fkFormaPagamentoNew ) )
            {
                fkFormaPagamentoOld.getFormaPagamentoItemList().remove( formaPagamentoItem );
                fkFormaPagamentoOld = em.merge( fkFormaPagamentoOld );
            }
            if ( fkFormaPagamentoNew != null && !fkFormaPagamentoNew.equals( fkFormaPagamentoOld ) )
            {
                fkFormaPagamentoNew.getFormaPagamentoItemList().add( formaPagamentoItem );
                fkFormaPagamentoNew = em.merge( fkFormaPagamentoNew );
            }
            if ( fkVendaOld != null && !fkVendaOld.equals( fkVendaNew ) )
            {
                fkVendaOld.getFormaPagamentoItemList().remove( formaPagamentoItem );
                fkVendaOld = em.merge( fkVendaOld );
            }
            if ( fkVendaNew != null && !fkVendaNew.equals( fkVendaOld ) )
            {
                fkVendaNew.getFormaPagamentoItemList().add( formaPagamentoItem );
                fkVendaNew = em.merge( fkVendaNew );
            }
            em.getTransaction().commit();
        }
        catch ( Exception ex )
        {
            String msg = ex.getLocalizedMessage();
            if ( msg == null || msg.length() == 0 )
            {
                Integer id = formaPagamentoItem.getPkFormaPagamentoItem();
                if ( findFormaPagamentoItem( id ) == null )
                {
                    throw new NonexistentEntityException( "The formaPagamentoItem with id " + id + " no longer exists." );
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
            FormaPagamentoItem formaPagamentoItem;
            try
            {
                formaPagamentoItem = em.getReference( FormaPagamentoItem.class, id );
                formaPagamentoItem.getPkFormaPagamentoItem();
            }
            catch ( EntityNotFoundException enfe )
            {
                throw new NonexistentEntityException( "The formaPagamentoItem with id " + id + " no longer exists.", enfe );
            }
            FormaPagamento fkFormaPagamento = formaPagamentoItem.getFkFormaPagamento();
            if ( fkFormaPagamento != null )
            {
                fkFormaPagamento.getFormaPagamentoItemList().remove( formaPagamentoItem );
                fkFormaPagamento = em.merge( fkFormaPagamento );
            }
            TbVenda fkVenda = formaPagamentoItem.getFkVenda();
            if ( fkVenda != null )
            {
                fkVenda.getFormaPagamentoItemList().remove( formaPagamentoItem );
                fkVenda = em.merge( fkVenda );
            }
            em.remove( formaPagamentoItem );
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

    public List<FormaPagamentoItem> findFormaPagamentoItemEntities()
    {
        return findFormaPagamentoItemEntities( true, -1, -1 );
    }

    public List<FormaPagamentoItem> findFormaPagamentoItemEntities( int maxResults, int firstResult )
    {
        return findFormaPagamentoItemEntities( false, maxResults, firstResult );
    }

    private List<FormaPagamentoItem> findFormaPagamentoItemEntities( boolean all, int maxResults, int firstResult )
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select( cq.from( FormaPagamentoItem.class ) );
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

    public FormaPagamentoItem findFormaPagamentoItem( Integer id )
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find( FormaPagamentoItem.class, id );
        }
        finally
        {
            em.close();
        }
    }

    public int getFormaPagamentoItemCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<FormaPagamentoItem> rt = cq.from( FormaPagamentoItem.class );
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
