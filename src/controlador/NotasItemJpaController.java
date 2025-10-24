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
import entity.Notas;
import entity.NotasItem;
import entity.TbPreco;
import entity.TbProduto;
import entity.TbVenda;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author lenovo
 */
public class NotasItemJpaController implements Serializable
{

    public NotasItemJpaController( EntityManagerFactory emf )
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create( NotasItem notasItem )
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            Notas fkNota = notasItem.getFkNota();
            if ( fkNota != null )
            {
                fkNota = em.getReference( fkNota.getClass(), fkNota.getPkNota() );
                notasItem.setFkNota( fkNota );
            }
            TbPreco fkPreco = notasItem.getFkPreco();
            if ( fkPreco != null )
            {
                fkPreco = em.getReference( fkPreco.getClass(), fkPreco.getPkPreco() );
                notasItem.setFkPreco( fkPreco );
            }
            TbProduto fkProduto = notasItem.getFkProduto();
            if ( fkProduto != null )
            {
                fkProduto = em.getReference( fkProduto.getClass(), fkProduto.getCodigo() );
                notasItem.setFkProduto( fkProduto );
            }
            TbVenda fkVenda = notasItem.getFkVenda();
            if ( fkVenda != null )
            {
                fkVenda = em.getReference( fkVenda.getClass(), fkVenda.getCodigo() );
                notasItem.setFkVenda( fkVenda );
            }
            em.persist( notasItem );
            if ( fkNota != null )
            {
                fkNota.getNotasItemList().add( notasItem );
                fkNota = em.merge( fkNota );
            }
            if ( fkPreco != null )
            {
                fkPreco.getNotasItemList().add( notasItem );
                fkPreco = em.merge( fkPreco );
            }
            if ( fkProduto != null )
            {
                fkProduto.getNotasItemList().add( notasItem );
                fkProduto = em.merge( fkProduto );
            }
            if ( fkVenda != null )
            {
                fkVenda.getNotasItemList().add( notasItem );
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

    public void edit( NotasItem notasItem ) throws NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            NotasItem persistentNotasItem = em.find( NotasItem.class, notasItem.getPkNotasItem() );
            Notas fkNotaOld = persistentNotasItem.getFkNota();
            Notas fkNotaNew = notasItem.getFkNota();
            TbPreco fkPrecoOld = persistentNotasItem.getFkPreco();
            TbPreco fkPrecoNew = notasItem.getFkPreco();
            TbProduto fkProdutoOld = persistentNotasItem.getFkProduto();
            TbProduto fkProdutoNew = notasItem.getFkProduto();
            TbVenda fkVendaOld = persistentNotasItem.getFkVenda();
            TbVenda fkVendaNew = notasItem.getFkVenda();
            if ( fkNotaNew != null )
            {
                fkNotaNew = em.getReference( fkNotaNew.getClass(), fkNotaNew.getPkNota() );
                notasItem.setFkNota( fkNotaNew );
            }
            if ( fkPrecoNew != null )
            {
                fkPrecoNew = em.getReference( fkPrecoNew.getClass(), fkPrecoNew.getPkPreco() );
                notasItem.setFkPreco( fkPrecoNew );
            }
            if ( fkProdutoNew != null )
            {
                fkProdutoNew = em.getReference( fkProdutoNew.getClass(), fkProdutoNew.getCodigo() );
                notasItem.setFkProduto( fkProdutoNew );
            }
            if ( fkVendaNew != null )
            {
                fkVendaNew = em.getReference( fkVendaNew.getClass(), fkVendaNew.getCodigo() );
                notasItem.setFkVenda( fkVendaNew );
            }
            notasItem = em.merge( notasItem );
            if ( fkNotaOld != null && !fkNotaOld.equals( fkNotaNew ) )
            {
                fkNotaOld.getNotasItemList().remove( notasItem );
                fkNotaOld = em.merge( fkNotaOld );
            }
            if ( fkNotaNew != null && !fkNotaNew.equals( fkNotaOld ) )
            {
                fkNotaNew.getNotasItemList().add( notasItem );
                fkNotaNew = em.merge( fkNotaNew );
            }
            if ( fkPrecoOld != null && !fkPrecoOld.equals( fkPrecoNew ) )
            {
                fkPrecoOld.getNotasItemList().remove( notasItem );
                fkPrecoOld = em.merge( fkPrecoOld );
            }
            if ( fkPrecoNew != null && !fkPrecoNew.equals( fkPrecoOld ) )
            {
                fkPrecoNew.getNotasItemList().add( notasItem );
                fkPrecoNew = em.merge( fkPrecoNew );
            }
            if ( fkProdutoOld != null && !fkProdutoOld.equals( fkProdutoNew ) )
            {
                fkProdutoOld.getNotasItemList().remove( notasItem );
                fkProdutoOld = em.merge( fkProdutoOld );
            }
            if ( fkProdutoNew != null && !fkProdutoNew.equals( fkProdutoOld ) )
            {
                fkProdutoNew.getNotasItemList().add( notasItem );
                fkProdutoNew = em.merge( fkProdutoNew );
            }
            if ( fkVendaOld != null && !fkVendaOld.equals( fkVendaNew ) )
            {
                fkVendaOld.getNotasItemList().remove( notasItem );
                fkVendaOld = em.merge( fkVendaOld );
            }
            if ( fkVendaNew != null && !fkVendaNew.equals( fkVendaOld ) )
            {
                fkVendaNew.getNotasItemList().add( notasItem );
                fkVendaNew = em.merge( fkVendaNew );
            }
            em.getTransaction().commit();
        }
        catch ( Exception ex )
        {
            String msg = ex.getLocalizedMessage();
            if ( msg == null || msg.length() == 0 )
            {
                Integer id = notasItem.getPkNotasItem();
                if ( findNotasItem( id ) == null )
                {
                    throw new NonexistentEntityException( "The notasItem with id " + id + " no longer exists." );
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
            NotasItem notasItem;
            try
            {
                notasItem = em.getReference( NotasItem.class, id );
                notasItem.getPkNotasItem();
            }
            catch ( EntityNotFoundException enfe )
            {
                throw new NonexistentEntityException( "The notasItem with id " + id + " no longer exists.", enfe );
            }
            Notas fkNota = notasItem.getFkNota();
            if ( fkNota != null )
            {
                fkNota.getNotasItemList().remove( notasItem );
                fkNota = em.merge( fkNota );
            }
            TbPreco fkPreco = notasItem.getFkPreco();
            if ( fkPreco != null )
            {
                fkPreco.getNotasItemList().remove( notasItem );
                fkPreco = em.merge( fkPreco );
            }
            TbProduto fkProduto = notasItem.getFkProduto();
            if ( fkProduto != null )
            {
                fkProduto.getNotasItemList().remove( notasItem );
                fkProduto = em.merge( fkProduto );
            }
            TbVenda fkVenda = notasItem.getFkVenda();
            if ( fkVenda != null )
            {
                fkVenda.getNotasItemList().remove( notasItem );
                fkVenda = em.merge( fkVenda );
            }
            em.remove( notasItem );
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

    public List<NotasItem> findNotasItemEntities()
    {
        return findNotasItemEntities( true, -1, -1 );
    }

    public List<NotasItem> findNotasItemEntities( int maxResults, int firstResult )
    {
        return findNotasItemEntities( false, maxResults, firstResult );
    }

    private List<NotasItem> findNotasItemEntities( boolean all, int maxResults, int firstResult )
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select( cq.from( NotasItem.class ) );
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

    public NotasItem findNotasItem( Integer id )
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find( NotasItem.class, id );
        }
        finally
        {
            em.close();
        }
    }

    public int getNotasItemCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<NotasItem> rt = cq.from( NotasItem.class );
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
