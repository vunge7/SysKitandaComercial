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
import entity.Compras;
import entity.ItemCompras;
import entity.TbProduto;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author lenovo
 */
public class ItemComprasJpaController implements Serializable
{

    public ItemComprasJpaController( EntityManagerFactory emf )
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create( ItemCompras itemCompras )
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            Compras fkCompra = itemCompras.getFkCompra();
            if ( fkCompra != null )
            {
                fkCompra = em.getReference( fkCompra.getClass(), fkCompra.getPkCompra() );
                itemCompras.setFkCompra( fkCompra );
            }
            TbProduto fkProduto = itemCompras.getFkProduto();
            if ( fkProduto != null )
            {
                fkProduto = em.getReference( fkProduto.getClass(), fkProduto.getCodigo() );
                itemCompras.setFkProduto( fkProduto );
            }
            em.persist( itemCompras );
            if ( fkCompra != null )
            {
                fkCompra.getItemComprasList().add( itemCompras );
                fkCompra = em.merge( fkCompra );
            }
            if ( fkProduto != null )
            {
                fkProduto.getItemComprasList().add( itemCompras );
                fkProduto = em.merge( fkProduto );
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

    public void edit( ItemCompras itemCompras ) throws NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            ItemCompras persistentItemCompras = em.find( ItemCompras.class, itemCompras.getPkItmCompras() );
            Compras fkCompraOld = persistentItemCompras.getFkCompra();
            Compras fkCompraNew = itemCompras.getFkCompra();
            TbProduto fkProdutoOld = persistentItemCompras.getFkProduto();
            TbProduto fkProdutoNew = itemCompras.getFkProduto();
            if ( fkCompraNew != null )
            {
                fkCompraNew = em.getReference( fkCompraNew.getClass(), fkCompraNew.getPkCompra() );
                itemCompras.setFkCompra( fkCompraNew );
            }
            if ( fkProdutoNew != null )
            {
                fkProdutoNew = em.getReference( fkProdutoNew.getClass(), fkProdutoNew.getCodigo() );
                itemCompras.setFkProduto( fkProdutoNew );
            }
            itemCompras = em.merge( itemCompras );
            if ( fkCompraOld != null && !fkCompraOld.equals( fkCompraNew ) )
            {
                fkCompraOld.getItemComprasList().remove( itemCompras );
                fkCompraOld = em.merge( fkCompraOld );
            }
            if ( fkCompraNew != null && !fkCompraNew.equals( fkCompraOld ) )
            {
                fkCompraNew.getItemComprasList().add( itemCompras );
                fkCompraNew = em.merge( fkCompraNew );
            }
            if ( fkProdutoOld != null && !fkProdutoOld.equals( fkProdutoNew ) )
            {
                fkProdutoOld.getItemComprasList().remove( itemCompras );
                fkProdutoOld = em.merge( fkProdutoOld );
            }
            if ( fkProdutoNew != null && !fkProdutoNew.equals( fkProdutoOld ) )
            {
                fkProdutoNew.getItemComprasList().add( itemCompras );
                fkProdutoNew = em.merge( fkProdutoNew );
            }
            em.getTransaction().commit();
        }
        catch ( Exception ex )
        {
            String msg = ex.getLocalizedMessage();
            if ( msg == null || msg.length() == 0 )
            {
                Integer id = itemCompras.getPkItmCompras();
                if ( findItemCompras( id ) == null )
                {
                    throw new NonexistentEntityException( "The itemCompras with id " + id + " no longer exists." );
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
            ItemCompras itemCompras;
            try
            {
                itemCompras = em.getReference( ItemCompras.class, id );
                itemCompras.getPkItmCompras();
            }
            catch ( EntityNotFoundException enfe )
            {
                throw new NonexistentEntityException( "The itemCompras with id " + id + " no longer exists.", enfe );
            }
            Compras fkCompra = itemCompras.getFkCompra();
            if ( fkCompra != null )
            {
                fkCompra.getItemComprasList().remove( itemCompras );
                fkCompra = em.merge( fkCompra );
            }
            TbProduto fkProduto = itemCompras.getFkProduto();
            if ( fkProduto != null )
            {
                fkProduto.getItemComprasList().remove( itemCompras );
                fkProduto = em.merge( fkProduto );
            }
            em.remove( itemCompras );
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

    public List<ItemCompras> findItemComprasEntities()
    {
        return findItemComprasEntities( true, -1, -1 );
    }

    public List<ItemCompras> findItemComprasEntities( int maxResults, int firstResult )
    {
        return findItemComprasEntities( false, maxResults, firstResult );
    }

    private List<ItemCompras> findItemComprasEntities( boolean all, int maxResults, int firstResult )
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select( cq.from( ItemCompras.class ) );
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

    public ItemCompras findItemCompras( Integer id )
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find( ItemCompras.class, id );
        }
        finally
        {
            em.close();
        }
    }

    public int getItemComprasCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ItemCompras> rt = cq.from( ItemCompras.class );
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
