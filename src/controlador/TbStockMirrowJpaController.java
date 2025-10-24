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
import entity.TbStockMirrow;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author lenovo
 */
public class TbStockMirrowJpaController implements Serializable
{

    public TbStockMirrowJpaController( EntityManagerFactory emf )
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create( TbStockMirrow tbStockMirrow )
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            TbArmazem codArmazem = tbStockMirrow.getCodArmazem();
            if ( codArmazem != null )
            {
                codArmazem = em.getReference( codArmazem.getClass(), codArmazem.getCodigo() );
                tbStockMirrow.setCodArmazem( codArmazem );
            }
            TbProduto codProdutoCodigo = tbStockMirrow.getCodProdutoCodigo();
            if ( codProdutoCodigo != null )
            {
                codProdutoCodigo = em.getReference( codProdutoCodigo.getClass(), codProdutoCodigo.getCodigo() );
                tbStockMirrow.setCodProdutoCodigo( codProdutoCodigo );
            }
            em.persist( tbStockMirrow );
            if ( codArmazem != null )
            {
                codArmazem.getTbStockMirrowList().add( tbStockMirrow );
                codArmazem = em.merge( codArmazem );
            }
            if ( codProdutoCodigo != null )
            {
                codProdutoCodigo.getTbStockMirrowList().add( tbStockMirrow );
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

    public void edit( TbStockMirrow tbStockMirrow ) throws NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            TbStockMirrow persistentTbStockMirrow = em.find( TbStockMirrow.class, tbStockMirrow.getCodigo() );
            TbArmazem codArmazemOld = persistentTbStockMirrow.getCodArmazem();
            TbArmazem codArmazemNew = tbStockMirrow.getCodArmazem();
            TbProduto codProdutoCodigoOld = persistentTbStockMirrow.getCodProdutoCodigo();
            TbProduto codProdutoCodigoNew = tbStockMirrow.getCodProdutoCodigo();
            if ( codArmazemNew != null )
            {
                codArmazemNew = em.getReference( codArmazemNew.getClass(), codArmazemNew.getCodigo() );
                tbStockMirrow.setCodArmazem( codArmazemNew );
            }
            if ( codProdutoCodigoNew != null )
            {
                codProdutoCodigoNew = em.getReference( codProdutoCodigoNew.getClass(), codProdutoCodigoNew.getCodigo() );
                tbStockMirrow.setCodProdutoCodigo( codProdutoCodigoNew );
            }
            tbStockMirrow = em.merge( tbStockMirrow );
            if ( codArmazemOld != null && !codArmazemOld.equals( codArmazemNew ) )
            {
                codArmazemOld.getTbStockMirrowList().remove( tbStockMirrow );
                codArmazemOld = em.merge( codArmazemOld );
            }
            if ( codArmazemNew != null && !codArmazemNew.equals( codArmazemOld ) )
            {
                codArmazemNew.getTbStockMirrowList().add( tbStockMirrow );
                codArmazemNew = em.merge( codArmazemNew );
            }
            if ( codProdutoCodigoOld != null && !codProdutoCodigoOld.equals( codProdutoCodigoNew ) )
            {
                codProdutoCodigoOld.getTbStockMirrowList().remove( tbStockMirrow );
                codProdutoCodigoOld = em.merge( codProdutoCodigoOld );
            }
            if ( codProdutoCodigoNew != null && !codProdutoCodigoNew.equals( codProdutoCodigoOld ) )
            {
                codProdutoCodigoNew.getTbStockMirrowList().add( tbStockMirrow );
                codProdutoCodigoNew = em.merge( codProdutoCodigoNew );
            }
            em.getTransaction().commit();
        }
        catch ( Exception ex )
        {
            String msg = ex.getLocalizedMessage();
            if ( msg == null || msg.length() == 0 )
            {
                Integer id = tbStockMirrow.getCodigo();
                if ( findTbStockMirrow( id ) == null )
                {
                    throw new NonexistentEntityException( "The tbStockMirrow with id " + id + " no longer exists." );
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
            TbStockMirrow tbStockMirrow;
            try
            {
                tbStockMirrow = em.getReference( TbStockMirrow.class, id );
                tbStockMirrow.getCodigo();
            }
            catch ( EntityNotFoundException enfe )
            {
                throw new NonexistentEntityException( "The tbStockMirrow with id " + id + " no longer exists.", enfe );
            }
            TbArmazem codArmazem = tbStockMirrow.getCodArmazem();
            if ( codArmazem != null )
            {
                codArmazem.getTbStockMirrowList().remove( tbStockMirrow );
                codArmazem = em.merge( codArmazem );
            }
            TbProduto codProdutoCodigo = tbStockMirrow.getCodProdutoCodigo();
            if ( codProdutoCodigo != null )
            {
                codProdutoCodigo.getTbStockMirrowList().remove( tbStockMirrow );
                codProdutoCodigo = em.merge( codProdutoCodigo );
            }
            em.remove( tbStockMirrow );
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

    public List<TbStockMirrow> findTbStockMirrowEntities()
    {
        return findTbStockMirrowEntities( true, -1, -1 );
    }

    public List<TbStockMirrow> findTbStockMirrowEntities( int maxResults, int firstResult )
    {
        return findTbStockMirrowEntities( false, maxResults, firstResult );
    }

    private List<TbStockMirrow> findTbStockMirrowEntities( boolean all, int maxResults, int firstResult )
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select( cq.from( TbStockMirrow.class ) );
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

    public TbStockMirrow findTbStockMirrow( Integer id )
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find( TbStockMirrow.class, id );
        }
        finally
        {
            em.close();
        }
    }

    public int getTbStockMirrowCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TbStockMirrow> rt = cq.from( TbStockMirrow.class );
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
