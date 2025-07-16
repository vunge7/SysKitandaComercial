/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import controlador.exceptions.NonexistentEntityException;
import entity.TbItemSaidas;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entity.TbProduto;
import entity.TbSaidasProdutos;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author lenovo
 */
public class TbItemSaidasJpaController implements Serializable
{

    public TbItemSaidasJpaController( EntityManagerFactory emf )
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create( TbItemSaidas tbItemSaidas )
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            TbProduto fkProdutos = tbItemSaidas.getFkProdutos();
            if ( fkProdutos != null )
            {
                fkProdutos = em.getReference( fkProdutos.getClass(), fkProdutos.getCodigo() );
                tbItemSaidas.setFkProdutos( fkProdutos );
            }
            TbSaidasProdutos fkSaidasProdutos = tbItemSaidas.getFkSaidasProdutos();
            if ( fkSaidasProdutos != null )
            {
                fkSaidasProdutos = em.getReference( fkSaidasProdutos.getClass(), fkSaidasProdutos.getPkSaidasProdutos() );
                tbItemSaidas.setFkSaidasProdutos( fkSaidasProdutos );
            }
            em.persist( tbItemSaidas );
            if ( fkProdutos != null )
            {
                fkProdutos.getTbItemSaidasList().add( tbItemSaidas );
                fkProdutos = em.merge( fkProdutos );
            }
            if ( fkSaidasProdutos != null )
            {
                fkSaidasProdutos.getTbItemSaidasList().add( tbItemSaidas );
                fkSaidasProdutos = em.merge( fkSaidasProdutos );
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

    public void edit( TbItemSaidas tbItemSaidas ) throws NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            TbItemSaidas persistentTbItemSaidas = em.find( TbItemSaidas.class, tbItemSaidas.getCodigo() );
            TbProduto fkProdutosOld = persistentTbItemSaidas.getFkProdutos();
            TbProduto fkProdutosNew = tbItemSaidas.getFkProdutos();
            TbSaidasProdutos fkSaidasProdutosOld = persistentTbItemSaidas.getFkSaidasProdutos();
            TbSaidasProdutos fkSaidasProdutosNew = tbItemSaidas.getFkSaidasProdutos();
            if ( fkProdutosNew != null )
            {
                fkProdutosNew = em.getReference( fkProdutosNew.getClass(), fkProdutosNew.getCodigo() );
                tbItemSaidas.setFkProdutos( fkProdutosNew );
            }
            if ( fkSaidasProdutosNew != null )
            {
                fkSaidasProdutosNew = em.getReference( fkSaidasProdutosNew.getClass(), fkSaidasProdutosNew.getPkSaidasProdutos() );
                tbItemSaidas.setFkSaidasProdutos( fkSaidasProdutosNew );
            }
            tbItemSaidas = em.merge( tbItemSaidas );
            if ( fkProdutosOld != null && !fkProdutosOld.equals( fkProdutosNew ) )
            {
                fkProdutosOld.getTbItemSaidasList().remove( tbItemSaidas );
                fkProdutosOld = em.merge( fkProdutosOld );
            }
            if ( fkProdutosNew != null && !fkProdutosNew.equals( fkProdutosOld ) )
            {
                fkProdutosNew.getTbItemSaidasList().add( tbItemSaidas );
                fkProdutosNew = em.merge( fkProdutosNew );
            }
            if ( fkSaidasProdutosOld != null && !fkSaidasProdutosOld.equals( fkSaidasProdutosNew ) )
            {
                fkSaidasProdutosOld.getTbItemSaidasList().remove( tbItemSaidas );
                fkSaidasProdutosOld = em.merge( fkSaidasProdutosOld );
            }
            if ( fkSaidasProdutosNew != null && !fkSaidasProdutosNew.equals( fkSaidasProdutosOld ) )
            {
                fkSaidasProdutosNew.getTbItemSaidasList().add( tbItemSaidas );
                fkSaidasProdutosNew = em.merge( fkSaidasProdutosNew );
            }
            em.getTransaction().commit();
        }
        catch ( Exception ex )
        {
            String msg = ex.getLocalizedMessage();
            if ( msg == null || msg.length() == 0 )
            {
                Integer id = tbItemSaidas.getCodigo();
                if ( findTbItemSaidas( id ) == null )
                {
                    throw new NonexistentEntityException( "The tbItemSaidas with id " + id + " no longer exists." );
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
            TbItemSaidas tbItemSaidas;
            try
            {
                tbItemSaidas = em.getReference( TbItemSaidas.class, id );
                tbItemSaidas.getCodigo();
            }
            catch ( EntityNotFoundException enfe )
            {
                throw new NonexistentEntityException( "The tbItemSaidas with id " + id + " no longer exists.", enfe );
            }
            TbProduto fkProdutos = tbItemSaidas.getFkProdutos();
            if ( fkProdutos != null )
            {
                fkProdutos.getTbItemSaidasList().remove( tbItemSaidas );
                fkProdutos = em.merge( fkProdutos );
            }
            TbSaidasProdutos fkSaidasProdutos = tbItemSaidas.getFkSaidasProdutos();
            if ( fkSaidasProdutos != null )
            {
                fkSaidasProdutos.getTbItemSaidasList().remove( tbItemSaidas );
                fkSaidasProdutos = em.merge( fkSaidasProdutos );
            }
            em.remove( tbItemSaidas );
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

    public List<TbItemSaidas> findTbItemSaidasEntities()
    {
        return findTbItemSaidasEntities( true, -1, -1 );
    }

    public List<TbItemSaidas> findTbItemSaidasEntities( int maxResults, int firstResult )
    {
        return findTbItemSaidasEntities( false, maxResults, firstResult );
    }

    private List<TbItemSaidas> findTbItemSaidasEntities( boolean all, int maxResults, int firstResult )
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select( cq.from( TbItemSaidas.class ) );
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

    public TbItemSaidas findTbItemSaidas( Integer id )
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find( TbItemSaidas.class, id );
        }
        finally
        {
            em.close();
        }
    }

    public int getTbItemSaidasCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TbItemSaidas> rt = cq.from( TbItemSaidas.class );
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
