/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;


import java.sql.Connection;
import controlador.exceptions.NonexistentEntityException;
import entity.TbItemPedidos;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entity.TbLugares;
import entity.TbProduto;
import entity.TbPedido;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author lenovo
 */
public class TbItemPedidosJpaController implements Serializable
{

    public TbItemPedidosJpaController( EntityManagerFactory emf )
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create( TbItemPedidos tbItemPedidos )
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            TbLugares fkLugares = tbItemPedidos.getFkLugares();
            if ( fkLugares != null )
            {
                fkLugares = em.getReference( fkLugares.getClass(), fkLugares.getPkLugares() );
                tbItemPedidos.setFkLugares( fkLugares );
            }
            TbProduto fkProdutos = tbItemPedidos.getFkProdutos();
            if ( fkProdutos != null )
            {
                fkProdutos = em.getReference( fkProdutos.getClass(), fkProdutos.getCodigo() );
                tbItemPedidos.setFkProdutos( fkProdutos );
            }
            TbPedido fkPedidos = tbItemPedidos.getFkPedidos();
            if ( fkPedidos != null )
            {
                fkPedidos = em.getReference( fkPedidos.getClass(), fkPedidos.getPkPedido() );
                tbItemPedidos.setFkPedidos( fkPedidos );
            }
            em.persist( tbItemPedidos );
            if ( fkLugares != null )
            {
                fkLugares.getTbItemPedidosList().add( tbItemPedidos );
                fkLugares = em.merge( fkLugares );
            }
            if ( fkProdutos != null )
            {
                fkProdutos.getTbItemPedidosList().add( tbItemPedidos );
                fkProdutos = em.merge( fkProdutos );
            }
            if ( fkPedidos != null )
            {
                fkPedidos.getTbItemPedidosList().add( tbItemPedidos );
                fkPedidos = em.merge( fkPedidos );
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

    public void edit( TbItemPedidos tbItemPedidos ) throws NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            TbItemPedidos persistentTbItemPedidos = em.find( TbItemPedidos.class, tbItemPedidos.getPkItemPedidos() );
            TbLugares fkLugaresOld = persistentTbItemPedidos.getFkLugares();
            TbLugares fkLugaresNew = tbItemPedidos.getFkLugares();
            TbProduto fkProdutosOld = persistentTbItemPedidos.getFkProdutos();
            TbProduto fkProdutosNew = tbItemPedidos.getFkProdutos();
            TbPedido fkPedidosOld = persistentTbItemPedidos.getFkPedidos();
            TbPedido fkPedidosNew = tbItemPedidos.getFkPedidos();
            if ( fkLugaresNew != null )
            {
                fkLugaresNew = em.getReference( fkLugaresNew.getClass(), fkLugaresNew.getPkLugares() );
                tbItemPedidos.setFkLugares( fkLugaresNew );
            }
            if ( fkProdutosNew != null )
            {
                fkProdutosNew = em.getReference( fkProdutosNew.getClass(), fkProdutosNew.getCodigo() );
                tbItemPedidos.setFkProdutos( fkProdutosNew );
            }
            if ( fkPedidosNew != null )
            {
                fkPedidosNew = em.getReference( fkPedidosNew.getClass(), fkPedidosNew.getPkPedido() );
                tbItemPedidos.setFkPedidos( fkPedidosNew );
            }
            tbItemPedidos = em.merge( tbItemPedidos );
            if ( fkLugaresOld != null && !fkLugaresOld.equals( fkLugaresNew ) )
            {
                fkLugaresOld.getTbItemPedidosList().remove( tbItemPedidos );
                fkLugaresOld = em.merge( fkLugaresOld );
            }
            if ( fkLugaresNew != null && !fkLugaresNew.equals( fkLugaresOld ) )
            {
                fkLugaresNew.getTbItemPedidosList().add( tbItemPedidos );
                fkLugaresNew = em.merge( fkLugaresNew );
            }
            if ( fkProdutosOld != null && !fkProdutosOld.equals( fkProdutosNew ) )
            {
                fkProdutosOld.getTbItemPedidosList().remove( tbItemPedidos );
                fkProdutosOld = em.merge( fkProdutosOld );
            }
            if ( fkProdutosNew != null && !fkProdutosNew.equals( fkProdutosOld ) )
            {
                fkProdutosNew.getTbItemPedidosList().add( tbItemPedidos );
                fkProdutosNew = em.merge( fkProdutosNew );
            }
            if ( fkPedidosOld != null && !fkPedidosOld.equals( fkPedidosNew ) )
            {
                fkPedidosOld.getTbItemPedidosList().remove( tbItemPedidos );
                fkPedidosOld = em.merge( fkPedidosOld );
            }
            if ( fkPedidosNew != null && !fkPedidosNew.equals( fkPedidosOld ) )
            {
                fkPedidosNew.getTbItemPedidosList().add( tbItemPedidos );
                fkPedidosNew = em.merge( fkPedidosNew );
            }
            em.getTransaction().commit();
        }
        catch ( Exception ex )
        {
            String msg = ex.getLocalizedMessage();
            if ( msg == null || msg.length() == 0 )
            {
                Integer id = tbItemPedidos.getPkItemPedidos();
                if ( findTbItemPedidos( id ) == null )
                {
                    throw new NonexistentEntityException( "The tbItemPedidos with id " + id + " no longer exists." );
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
            TbItemPedidos tbItemPedidos;
            try
            {
                tbItemPedidos = em.getReference( TbItemPedidos.class, id );
                tbItemPedidos.getPkItemPedidos();
            }
            catch ( EntityNotFoundException enfe )
            {
                throw new NonexistentEntityException( "The tbItemPedidos with id " + id + " no longer exists.", enfe );
            }
            TbLugares fkLugares = tbItemPedidos.getFkLugares();
            if ( fkLugares != null )
            {
                fkLugares.getTbItemPedidosList().remove( tbItemPedidos );
                fkLugares = em.merge( fkLugares );
            }
            TbProduto fkProdutos = tbItemPedidos.getFkProdutos();
            if ( fkProdutos != null )
            {
                fkProdutos.getTbItemPedidosList().remove( tbItemPedidos );
                fkProdutos = em.merge( fkProdutos );
            }
            TbPedido fkPedidos = tbItemPedidos.getFkPedidos();
            if ( fkPedidos != null )
            {
                fkPedidos.getTbItemPedidosList().remove( tbItemPedidos );
                fkPedidos = em.merge( fkPedidos );
            }
            em.remove( tbItemPedidos );
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

    public List<TbItemPedidos> findTbItemPedidosEntities()
    {
        return findTbItemPedidosEntities( true, -1, -1 );
    }

    public List<TbItemPedidos> findTbItemPedidosEntities( int maxResults, int firstResult )
    {
        return findTbItemPedidosEntities( false, maxResults, firstResult );
    }

    private List<TbItemPedidos> findTbItemPedidosEntities( boolean all, int maxResults, int firstResult )
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select( cq.from( TbItemPedidos.class ) );
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

    public TbItemPedidos findTbItemPedidos( Integer id )
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find( TbItemPedidos.class, id );
        }
        finally
        {
            em.close();
        }
    }

    public int getTbItemPedidosCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TbItemPedidos> rt = cq.from( TbItemPedidos.class );
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
