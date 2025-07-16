/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import controlador.exceptions.NonexistentEntityException;
import entity.TbDadosInstituicao;
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
public class TbDadosInstituicaoJpaController implements Serializable
{

    public TbDadosInstituicaoJpaController( EntityManagerFactory emf )
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create( TbDadosInstituicao tbDadosInstituicao )
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist( tbDadosInstituicao );
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

    public void edit( TbDadosInstituicao tbDadosInstituicao ) throws NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            tbDadosInstituicao = em.merge( tbDadosInstituicao );
            em.getTransaction().commit();
        }
        catch ( Exception ex )
        {
            String msg = ex.getLocalizedMessage();
            if ( msg == null || msg.length() == 0 )
            {
                Integer id = tbDadosInstituicao.getIdDadosInsitiuicao();
                if ( findTbDadosInstituicao( id ) == null )
                {
                    throw new NonexistentEntityException( "The tbDadosInstituicao with id " + id + " no longer exists." );
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
            TbDadosInstituicao tbDadosInstituicao;
            try
            {
                tbDadosInstituicao = em.getReference( TbDadosInstituicao.class, id );
                tbDadosInstituicao.getIdDadosInsitiuicao();
            }
            catch ( EntityNotFoundException enfe )
            {
                throw new NonexistentEntityException( "The tbDadosInstituicao with id " + id + " no longer exists.", enfe );
            }
            em.remove( tbDadosInstituicao );
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

    public List<TbDadosInstituicao> findTbDadosInstituicaoEntities()
    {
        return findTbDadosInstituicaoEntities( true, -1, -1 );
    }

    public List<TbDadosInstituicao> findTbDadosInstituicaoEntities( int maxResults, int firstResult )
    {
        return findTbDadosInstituicaoEntities( false, maxResults, firstResult );
    }

    private List<TbDadosInstituicao> findTbDadosInstituicaoEntities( boolean all, int maxResults, int firstResult )
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select( cq.from( TbDadosInstituicao.class ) );
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

    public TbDadosInstituicao findTbDadosInstituicao( Integer id )
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find( TbDadosInstituicao.class, id );
        }
        finally
        {
            em.close();
        }
    }

    public int getTbDadosInstituicaoCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TbDadosInstituicao> rt = cq.from( TbDadosInstituicao.class );
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
