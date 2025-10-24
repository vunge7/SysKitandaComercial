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
import entity.TbEstorno;
import entity.TbItemEstorno;
import entity.TbProduto;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author lenovo
 */
public class TbItemEstornoJpaController implements Serializable
{

    public TbItemEstornoJpaController( EntityManagerFactory emf )
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create( TbItemEstorno tbItemEstorno )
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            TbEstorno fkEstorno = tbItemEstorno.getFkEstorno();
            if ( fkEstorno != null )
            {
                fkEstorno = em.getReference( fkEstorno.getClass(), fkEstorno.getPkEstorno() );
                tbItemEstorno.setFkEstorno( fkEstorno );
            }
            TbProduto fkProdutos = tbItemEstorno.getFkProdutos();
            if ( fkProdutos != null )
            {
                fkProdutos = em.getReference( fkProdutos.getClass(), fkProdutos.getCodigo() );
                tbItemEstorno.setFkProdutos( fkProdutos );
            }
            em.persist( tbItemEstorno );
            if ( fkEstorno != null )
            {
                fkEstorno.getTbItemEstornoList().add( tbItemEstorno );
                fkEstorno = em.merge( fkEstorno );
            }
            if ( fkProdutos != null )
            {
                fkProdutos.getTbItemEstornoList().add( tbItemEstorno );
                fkProdutos = em.merge( fkProdutos );
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

    public void edit( TbItemEstorno tbItemEstorno ) throws NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            TbItemEstorno persistentTbItemEstorno = em.find( TbItemEstorno.class, tbItemEstorno.getCodigo() );
            TbEstorno fkEstornoOld = persistentTbItemEstorno.getFkEstorno();
            TbEstorno fkEstornoNew = tbItemEstorno.getFkEstorno();
            TbProduto fkProdutosOld = persistentTbItemEstorno.getFkProdutos();
            TbProduto fkProdutosNew = tbItemEstorno.getFkProdutos();
            if ( fkEstornoNew != null )
            {
                fkEstornoNew = em.getReference( fkEstornoNew.getClass(), fkEstornoNew.getPkEstorno() );
                tbItemEstorno.setFkEstorno( fkEstornoNew );
            }
            if ( fkProdutosNew != null )
            {
                fkProdutosNew = em.getReference( fkProdutosNew.getClass(), fkProdutosNew.getCodigo() );
                tbItemEstorno.setFkProdutos( fkProdutosNew );
            }
            tbItemEstorno = em.merge( tbItemEstorno );
            if ( fkEstornoOld != null && !fkEstornoOld.equals( fkEstornoNew ) )
            {
                fkEstornoOld.getTbItemEstornoList().remove( tbItemEstorno );
                fkEstornoOld = em.merge( fkEstornoOld );
            }
            if ( fkEstornoNew != null && !fkEstornoNew.equals( fkEstornoOld ) )
            {
                fkEstornoNew.getTbItemEstornoList().add( tbItemEstorno );
                fkEstornoNew = em.merge( fkEstornoNew );
            }
            if ( fkProdutosOld != null && !fkProdutosOld.equals( fkProdutosNew ) )
            {
                fkProdutosOld.getTbItemEstornoList().remove( tbItemEstorno );
                fkProdutosOld = em.merge( fkProdutosOld );
            }
            if ( fkProdutosNew != null && !fkProdutosNew.equals( fkProdutosOld ) )
            {
                fkProdutosNew.getTbItemEstornoList().add( tbItemEstorno );
                fkProdutosNew = em.merge( fkProdutosNew );
            }
            em.getTransaction().commit();
        }
        catch ( Exception ex )
        {
            String msg = ex.getLocalizedMessage();
            if ( msg == null || msg.length() == 0 )
            {
                Integer id = tbItemEstorno.getCodigo();
                if ( findTbItemEstorno( id ) == null )
                {
                    throw new NonexistentEntityException( "The tbItemEstorno with id " + id + " no longer exists." );
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
            TbItemEstorno tbItemEstorno;
            try
            {
                tbItemEstorno = em.getReference( TbItemEstorno.class, id );
                tbItemEstorno.getCodigo();
            }
            catch ( EntityNotFoundException enfe )
            {
                throw new NonexistentEntityException( "The tbItemEstorno with id " + id + " no longer exists.", enfe );
            }
            TbEstorno fkEstorno = tbItemEstorno.getFkEstorno();
            if ( fkEstorno != null )
            {
                fkEstorno.getTbItemEstornoList().remove( tbItemEstorno );
                fkEstorno = em.merge( fkEstorno );
            }
            TbProduto fkProdutos = tbItemEstorno.getFkProdutos();
            if ( fkProdutos != null )
            {
                fkProdutos.getTbItemEstornoList().remove( tbItemEstorno );
                fkProdutos = em.merge( fkProdutos );
            }
            em.remove( tbItemEstorno );
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

    public List<TbItemEstorno> findTbItemEstornoEntities()
    {
        return findTbItemEstornoEntities( true, -1, -1 );
    }

    public List<TbItemEstorno> findTbItemEstornoEntities( int maxResults, int firstResult )
    {
        return findTbItemEstornoEntities( false, maxResults, firstResult );
    }

    private List<TbItemEstorno> findTbItemEstornoEntities( boolean all, int maxResults, int firstResult )
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select( cq.from( TbItemEstorno.class ) );
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

    public TbItemEstorno findTbItemEstorno( Integer id )
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find( TbItemEstorno.class, id );
        }
        finally
        {
            em.close();
        }
    }

    public int getTbItemEstornoCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TbItemEstorno> rt = cq.from( TbItemEstorno.class );
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
