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
import entity.TbArmazem;
import entity.TbProduto;
import entity.TbStock;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author lenovo
 */
public class TbStockJpaController implements Serializable
{

    public TbStockJpaController( EntityManagerFactory emf )
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create( TbStock tbStock )
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            TbArmazem codArmazem = tbStock.getCodArmazem();
            if ( codArmazem != null )
            {
                codArmazem = em.getReference( codArmazem.getClass(), codArmazem.getCodigo() );
                tbStock.setCodArmazem( codArmazem );
            }
            TbProduto codProdutoCodigo = tbStock.getCodProdutoCodigo();
            if ( codProdutoCodigo != null )
            {
                codProdutoCodigo = em.getReference( codProdutoCodigo.getClass(), codProdutoCodigo.getCodigo() );
                tbStock.setCodProdutoCodigo( codProdutoCodigo );
            }
            em.persist( tbStock );
            if ( codArmazem != null )
            {
                codArmazem.getTbStockList().add( tbStock );
                codArmazem = em.merge( codArmazem );
            }
            if ( codProdutoCodigo != null )
            {
                codProdutoCodigo.getTbStockList().add( tbStock );
                codProdutoCodigo = em.merge( codProdutoCodigo );
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

    public void edit( TbStock tbStock ) throws NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            TbStock persistentTbStock = em.find( TbStock.class, tbStock.getCodigo() );
            TbArmazem codArmazemOld = persistentTbStock.getCodArmazem();
            TbArmazem codArmazemNew = tbStock.getCodArmazem();
            TbProduto codProdutoCodigoOld = persistentTbStock.getCodProdutoCodigo();
            TbProduto codProdutoCodigoNew = tbStock.getCodProdutoCodigo();
            if ( codArmazemNew != null )
            {
                codArmazemNew = em.getReference( codArmazemNew.getClass(), codArmazemNew.getCodigo() );
                tbStock.setCodArmazem( codArmazemNew );
            }
            if ( codProdutoCodigoNew != null )
            {
                codProdutoCodigoNew = em.getReference( codProdutoCodigoNew.getClass(), codProdutoCodigoNew.getCodigo() );
                tbStock.setCodProdutoCodigo( codProdutoCodigoNew );
            }
            tbStock = em.merge( tbStock );
            if ( codArmazemOld != null && !codArmazemOld.equals( codArmazemNew ) )
            {
                codArmazemOld.getTbStockList().remove( tbStock );
                codArmazemOld = em.merge( codArmazemOld );
            }
            if ( codArmazemNew != null && !codArmazemNew.equals( codArmazemOld ) )
            {
                codArmazemNew.getTbStockList().add( tbStock );
                codArmazemNew = em.merge( codArmazemNew );
            }
            if ( codProdutoCodigoOld != null && !codProdutoCodigoOld.equals( codProdutoCodigoNew ) )
            {
                codProdutoCodigoOld.getTbStockList().remove( tbStock );
                codProdutoCodigoOld = em.merge( codProdutoCodigoOld );
            }
            if ( codProdutoCodigoNew != null && !codProdutoCodigoNew.equals( codProdutoCodigoOld ) )
            {
                codProdutoCodigoNew.getTbStockList().add( tbStock );
                codProdutoCodigoNew = em.merge( codProdutoCodigoNew );
            }
            em.getTransaction().commit();
        }
        catch ( Exception ex )
        {
            String msg = ex.getLocalizedMessage();
            if ( msg == null || msg.length() == 0 )
            {
                Integer id = tbStock.getCodigo();
                if ( findTbStock( id ) == null )
                {
                    throw new NonexistentEntityException( "The tbStock with id " + id + " no longer exists." );
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
            TbStock tbStock;
            try
            {
                tbStock = em.getReference( TbStock.class, id );
                tbStock.getCodigo();
            }
            catch ( EntityNotFoundException enfe )
            {
                throw new NonexistentEntityException( "The tbStock with id " + id + " no longer exists.", enfe );
            }
            TbArmazem codArmazem = tbStock.getCodArmazem();
            if ( codArmazem != null )
            {
                codArmazem.getTbStockList().remove( tbStock );
                codArmazem = em.merge( codArmazem );
            }
            TbProduto codProdutoCodigo = tbStock.getCodProdutoCodigo();
            if ( codProdutoCodigo != null )
            {
                codProdutoCodigo.getTbStockList().remove( tbStock );
                codProdutoCodigo = em.merge( codProdutoCodigo );
            }
            em.remove( tbStock );
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

    public List<TbStock> findTbStockEntities()
    {
        return findTbStockEntities( true, -1, -1 );
    }

    public List<TbStock> findTbStockEntities( int maxResults, int firstResult )
    {
        return findTbStockEntities( false, maxResults, firstResult );
    }

    private List<TbStock> findTbStockEntities( boolean all, int maxResults, int firstResult )
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select( cq.from( TbStock.class ) );
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

    public TbStock findTbStock( Integer id )
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find( TbStock.class, id );
        }
        finally
        {
            em.close();
        }
    }

    public int getTbStockCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TbStock> rt = cq.from( TbStock.class );
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
