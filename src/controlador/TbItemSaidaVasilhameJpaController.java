/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import controlador.exceptions.NonexistentEntityException;
import entity.TbItemSaidaVasilhame;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author lenovo
 */
public class TbItemSaidaVasilhameJpaController implements Serializable
{

    public TbItemSaidaVasilhameJpaController( EntityManagerFactory emf )
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create( TbItemSaidaVasilhame tbItemSaidaVasilhame )
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist( tbItemSaidaVasilhame );
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

    public void edit( TbItemSaidaVasilhame tbItemSaidaVasilhame ) throws NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            tbItemSaidaVasilhame = em.merge( tbItemSaidaVasilhame );
            em.getTransaction().commit();
        }
        catch ( Exception ex )
        {
            String msg = ex.getLocalizedMessage();
            if ( msg == null || msg.length() == 0 )
            {
                Integer id = tbItemSaidaVasilhame.getPkItemSaidaVasilhame();
                if ( findTbItemSaidaVasilhame( id ) == null )
                {
                    throw new NonexistentEntityException( "The tbItemSaidaVasilhame with id " + id + " no longer exists." );
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
            TbItemSaidaVasilhame tbItemSaidaVasilhame;
            try
            {
                tbItemSaidaVasilhame = em.getReference( TbItemSaidaVasilhame.class, id );
                tbItemSaidaVasilhame.getPkItemSaidaVasilhame();
            }
            catch ( EntityNotFoundException enfe )
            {
                throw new NonexistentEntityException( "The tbItemSaidaVasilhame with id " + id + " no longer exists.", enfe );
            }
            em.remove( tbItemSaidaVasilhame );
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

    public List<TbItemSaidaVasilhame> findTbItemSaidaVasilhameEntities()
    {
        return findTbItemSaidaVasilhameEntities( true, -1, -1 );
    }

    public List<TbItemSaidaVasilhame> findTbItemSaidaVasilhameEntities( int maxResults, int firstResult )
    {
        return findTbItemSaidaVasilhameEntities( false, maxResults, firstResult );
    }

    private List<TbItemSaidaVasilhame> findTbItemSaidaVasilhameEntities( boolean all, int maxResults, int firstResult )
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select( cq.from( TbItemSaidaVasilhame.class ) );
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

    public TbItemSaidaVasilhame findTbItemSaidaVasilhame( Integer id )
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find( TbItemSaidaVasilhame.class, id );
        }
        finally
        {
            em.close();
        }
    }

    public int getTbItemSaidaVasilhameCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TbItemSaidaVasilhame> rt = cq.from( TbItemSaidaVasilhame.class );
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
