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
import entity.TbEncomenda;
import entity.TbItemEncomenda;
import entity.TbProduto;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author lenovo
 */
public class TbItemEncomendaJpaController implements Serializable
{

    public TbItemEncomendaJpaController( EntityManagerFactory emf )
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create( TbItemEncomenda tbItemEncomenda )
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            TbEncomenda idEncomenda = tbItemEncomenda.getIdEncomenda();
            if ( idEncomenda != null )
            {
                idEncomenda = em.getReference( idEncomenda.getClass(), idEncomenda.getIdEncomenda() );
                tbItemEncomenda.setIdEncomenda( idEncomenda );
            }
            TbProduto idProduto = tbItemEncomenda.getIdProduto();
            if ( idProduto != null )
            {
                idProduto = em.getReference( idProduto.getClass(), idProduto.getCodigo() );
                tbItemEncomenda.setIdProduto( idProduto );
            }
            em.persist( tbItemEncomenda );
            if ( idEncomenda != null )
            {
                idEncomenda.getTbItemEncomendaList().add( tbItemEncomenda );
                idEncomenda = em.merge( idEncomenda );
            }
            if ( idProduto != null )
            {
                idProduto.getTbItemEncomendaList().add( tbItemEncomenda );
                idProduto = em.merge( idProduto );
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

    public void edit( TbItemEncomenda tbItemEncomenda ) throws NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            TbItemEncomenda persistentTbItemEncomenda = em.find( TbItemEncomenda.class, tbItemEncomenda.getCodigo() );
            TbEncomenda idEncomendaOld = persistentTbItemEncomenda.getIdEncomenda();
            TbEncomenda idEncomendaNew = tbItemEncomenda.getIdEncomenda();
            TbProduto idProdutoOld = persistentTbItemEncomenda.getIdProduto();
            TbProduto idProdutoNew = tbItemEncomenda.getIdProduto();
            if ( idEncomendaNew != null )
            {
                idEncomendaNew = em.getReference( idEncomendaNew.getClass(), idEncomendaNew.getIdEncomenda() );
                tbItemEncomenda.setIdEncomenda( idEncomendaNew );
            }
            if ( idProdutoNew != null )
            {
                idProdutoNew = em.getReference( idProdutoNew.getClass(), idProdutoNew.getCodigo() );
                tbItemEncomenda.setIdProduto( idProdutoNew );
            }
            tbItemEncomenda = em.merge( tbItemEncomenda );
            if ( idEncomendaOld != null && !idEncomendaOld.equals( idEncomendaNew ) )
            {
                idEncomendaOld.getTbItemEncomendaList().remove( tbItemEncomenda );
                idEncomendaOld = em.merge( idEncomendaOld );
            }
            if ( idEncomendaNew != null && !idEncomendaNew.equals( idEncomendaOld ) )
            {
                idEncomendaNew.getTbItemEncomendaList().add( tbItemEncomenda );
                idEncomendaNew = em.merge( idEncomendaNew );
            }
            if ( idProdutoOld != null && !idProdutoOld.equals( idProdutoNew ) )
            {
                idProdutoOld.getTbItemEncomendaList().remove( tbItemEncomenda );
                idProdutoOld = em.merge( idProdutoOld );
            }
            if ( idProdutoNew != null && !idProdutoNew.equals( idProdutoOld ) )
            {
                idProdutoNew.getTbItemEncomendaList().add( tbItemEncomenda );
                idProdutoNew = em.merge( idProdutoNew );
            }
            em.getTransaction().commit();
        }
        catch ( Exception ex )
        {
            String msg = ex.getLocalizedMessage();
            if ( msg == null || msg.length() == 0 )
            {
                Integer id = tbItemEncomenda.getCodigo();
                if ( findTbItemEncomenda( id ) == null )
                {
                    throw new NonexistentEntityException( "The tbItemEncomenda with id " + id + " no longer exists." );
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
            TbItemEncomenda tbItemEncomenda;
            try
            {
                tbItemEncomenda = em.getReference( TbItemEncomenda.class, id );
                tbItemEncomenda.getCodigo();
            }
            catch ( EntityNotFoundException enfe )
            {
                throw new NonexistentEntityException( "The tbItemEncomenda with id " + id + " no longer exists.", enfe );
            }
            TbEncomenda idEncomenda = tbItemEncomenda.getIdEncomenda();
            if ( idEncomenda != null )
            {
                idEncomenda.getTbItemEncomendaList().remove( tbItemEncomenda );
                idEncomenda = em.merge( idEncomenda );
            }
            TbProduto idProduto = tbItemEncomenda.getIdProduto();
            if ( idProduto != null )
            {
                idProduto.getTbItemEncomendaList().remove( tbItemEncomenda );
                idProduto = em.merge( idProduto );
            }
            em.remove( tbItemEncomenda );
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

    public List<TbItemEncomenda> findTbItemEncomendaEntities()
    {
        return findTbItemEncomendaEntities( true, -1, -1 );
    }

    public List<TbItemEncomenda> findTbItemEncomendaEntities( int maxResults, int firstResult )
    {
        return findTbItemEncomendaEntities( false, maxResults, firstResult );
    }

    private List<TbItemEncomenda> findTbItemEncomendaEntities( boolean all, int maxResults, int firstResult )
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select( cq.from( TbItemEncomenda.class ) );
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

    public TbItemEncomenda findTbItemEncomenda( Integer id )
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find( TbItemEncomenda.class, id );
        }
        finally
        {
            em.close();
        }
    }

    public int getTbItemEncomendaCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TbItemEncomenda> rt = cq.from( TbItemEncomenda.class );
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
