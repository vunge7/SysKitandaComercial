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
import entity.Compras;
import entity.NotasCompras;
import entity.NotasItemCompras;
import entity.TbProduto;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author lenovo
 */
public class NotasItemComprasJpaController implements Serializable
{

    public NotasItemComprasJpaController( EntityManagerFactory emf )
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create( NotasItemCompras notasItemCompras )
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            Compras fkCompras = notasItemCompras.getFkCompras();
            if ( fkCompras != null )
            {
                fkCompras = em.getReference( fkCompras.getClass(), fkCompras.getPkCompra() );
                notasItemCompras.setFkCompras( fkCompras );
            }
            NotasCompras fkNotaCompras = notasItemCompras.getFkNotaCompras();
            if ( fkNotaCompras != null )
            {
                fkNotaCompras = em.getReference( fkNotaCompras.getClass(), fkNotaCompras.getPkNotaCompras() );
                notasItemCompras.setFkNotaCompras( fkNotaCompras );
            }
            TbProduto fkProduto = notasItemCompras.getFkProduto();
            if ( fkProduto != null )
            {
                fkProduto = em.getReference( fkProduto.getClass(), fkProduto.getCodigo() );
                notasItemCompras.setFkProduto( fkProduto );
            }
            em.persist( notasItemCompras );
            if ( fkCompras != null )
            {
                fkCompras.getNotasItemComprasList().add( notasItemCompras );
                fkCompras = em.merge( fkCompras );
            }
            if ( fkNotaCompras != null )
            {
                fkNotaCompras.getNotasItemComprasList().add( notasItemCompras );
                fkNotaCompras = em.merge( fkNotaCompras );
            }
            if ( fkProduto != null )
            {
                fkProduto.getNotasItemComprasList().add( notasItemCompras );
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

    public void edit( NotasItemCompras notasItemCompras ) throws NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            NotasItemCompras persistentNotasItemCompras = em.find( NotasItemCompras.class, notasItemCompras.getPkNotasItem() );
            Compras fkComprasOld = persistentNotasItemCompras.getFkCompras();
            Compras fkComprasNew = notasItemCompras.getFkCompras();
            NotasCompras fkNotaComprasOld = persistentNotasItemCompras.getFkNotaCompras();
            NotasCompras fkNotaComprasNew = notasItemCompras.getFkNotaCompras();
            TbProduto fkProdutoOld = persistentNotasItemCompras.getFkProduto();
            TbProduto fkProdutoNew = notasItemCompras.getFkProduto();
            if ( fkComprasNew != null )
            {
                fkComprasNew = em.getReference( fkComprasNew.getClass(), fkComprasNew.getPkCompra() );
                notasItemCompras.setFkCompras( fkComprasNew );
            }
            if ( fkNotaComprasNew != null )
            {
                fkNotaComprasNew = em.getReference( fkNotaComprasNew.getClass(), fkNotaComprasNew.getPkNotaCompras() );
                notasItemCompras.setFkNotaCompras( fkNotaComprasNew );
            }
            if ( fkProdutoNew != null )
            {
                fkProdutoNew = em.getReference( fkProdutoNew.getClass(), fkProdutoNew.getCodigo() );
                notasItemCompras.setFkProduto( fkProdutoNew );
            }
            notasItemCompras = em.merge( notasItemCompras );
            if ( fkComprasOld != null && !fkComprasOld.equals( fkComprasNew ) )
            {
                fkComprasOld.getNotasItemComprasList().remove( notasItemCompras );
                fkComprasOld = em.merge( fkComprasOld );
            }
            if ( fkComprasNew != null && !fkComprasNew.equals( fkComprasOld ) )
            {
                fkComprasNew.getNotasItemComprasList().add( notasItemCompras );
                fkComprasNew = em.merge( fkComprasNew );
            }
            if ( fkNotaComprasOld != null && !fkNotaComprasOld.equals( fkNotaComprasNew ) )
            {
                fkNotaComprasOld.getNotasItemComprasList().remove( notasItemCompras );
                fkNotaComprasOld = em.merge( fkNotaComprasOld );
            }
            if ( fkNotaComprasNew != null && !fkNotaComprasNew.equals( fkNotaComprasOld ) )
            {
                fkNotaComprasNew.getNotasItemComprasList().add( notasItemCompras );
                fkNotaComprasNew = em.merge( fkNotaComprasNew );
            }
            if ( fkProdutoOld != null && !fkProdutoOld.equals( fkProdutoNew ) )
            {
                fkProdutoOld.getNotasItemComprasList().remove( notasItemCompras );
                fkProdutoOld = em.merge( fkProdutoOld );
            }
            if ( fkProdutoNew != null && !fkProdutoNew.equals( fkProdutoOld ) )
            {
                fkProdutoNew.getNotasItemComprasList().add( notasItemCompras );
                fkProdutoNew = em.merge( fkProdutoNew );
            }
            em.getTransaction().commit();
        }
        catch ( Exception ex )
        {
            String msg = ex.getLocalizedMessage();
            if ( msg == null || msg.length() == 0 )
            {
                Integer id = notasItemCompras.getPkNotasItem();
                if ( findNotasItemCompras( id ) == null )
                {
                    throw new NonexistentEntityException( "The notasItemCompras with id " + id + " no longer exists." );
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
            NotasItemCompras notasItemCompras;
            try
            {
                notasItemCompras = em.getReference( NotasItemCompras.class, id );
                notasItemCompras.getPkNotasItem();
            }
            catch ( EntityNotFoundException enfe )
            {
                throw new NonexistentEntityException( "The notasItemCompras with id " + id + " no longer exists.", enfe );
            }
            Compras fkCompras = notasItemCompras.getFkCompras();
            if ( fkCompras != null )
            {
                fkCompras.getNotasItemComprasList().remove( notasItemCompras );
                fkCompras = em.merge( fkCompras );
            }
            NotasCompras fkNotaCompras = notasItemCompras.getFkNotaCompras();
            if ( fkNotaCompras != null )
            {
                fkNotaCompras.getNotasItemComprasList().remove( notasItemCompras );
                fkNotaCompras = em.merge( fkNotaCompras );
            }
            TbProduto fkProduto = notasItemCompras.getFkProduto();
            if ( fkProduto != null )
            {
                fkProduto.getNotasItemComprasList().remove( notasItemCompras );
                fkProduto = em.merge( fkProduto );
            }
            em.remove( notasItemCompras );
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

    public List<NotasItemCompras> findNotasItemComprasEntities()
    {
        return findNotasItemComprasEntities( true, -1, -1 );
    }

    public List<NotasItemCompras> findNotasItemComprasEntities( int maxResults, int firstResult )
    {
        return findNotasItemComprasEntities( false, maxResults, firstResult );
    }

    private List<NotasItemCompras> findNotasItemComprasEntities( boolean all, int maxResults, int firstResult )
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select( cq.from( NotasItemCompras.class ) );
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

    public NotasItemCompras findNotasItemCompras( Integer id )
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find( NotasItemCompras.class, id );
        }
        finally
        {
            em.close();
        }
    }

    public int getNotasItemComprasCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<NotasItemCompras> rt = cq.from( NotasItemCompras.class );
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
