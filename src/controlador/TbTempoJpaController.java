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
import entity.TbFuncionario;
import entity.TbTempo;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author lenovo
 */
public class TbTempoJpaController implements Serializable
{

    public TbTempoJpaController( EntityManagerFactory emf )
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create( TbTempo tbTempo )
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            TbFuncionario idFuncionarioFK = tbTempo.getIdFuncionarioFK();
            if ( idFuncionarioFK != null )
            {
                idFuncionarioFK = em.getReference( idFuncionarioFK.getClass(), idFuncionarioFK.getIdFuncionario() );
                tbTempo.setIdFuncionarioFK( idFuncionarioFK );
            }
            em.persist( tbTempo );
            if ( idFuncionarioFK != null )
            {
                idFuncionarioFK.getTbTempoList().add( tbTempo );
                idFuncionarioFK = em.merge( idFuncionarioFK );
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

    public void edit( TbTempo tbTempo ) throws NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            TbTempo persistentTbTempo = em.find( TbTempo.class, tbTempo.getIdTempoPK() );
            TbFuncionario idFuncionarioFKOld = persistentTbTempo.getIdFuncionarioFK();
            TbFuncionario idFuncionarioFKNew = tbTempo.getIdFuncionarioFK();
            if ( idFuncionarioFKNew != null )
            {
                idFuncionarioFKNew = em.getReference( idFuncionarioFKNew.getClass(), idFuncionarioFKNew.getIdFuncionario() );
                tbTempo.setIdFuncionarioFK( idFuncionarioFKNew );
            }
            tbTempo = em.merge( tbTempo );
            if ( idFuncionarioFKOld != null && !idFuncionarioFKOld.equals( idFuncionarioFKNew ) )
            {
                idFuncionarioFKOld.getTbTempoList().remove( tbTempo );
                idFuncionarioFKOld = em.merge( idFuncionarioFKOld );
            }
            if ( idFuncionarioFKNew != null && !idFuncionarioFKNew.equals( idFuncionarioFKOld ) )
            {
                idFuncionarioFKNew.getTbTempoList().add( tbTempo );
                idFuncionarioFKNew = em.merge( idFuncionarioFKNew );
            }
            em.getTransaction().commit();
        }
        catch ( Exception ex )
        {
            String msg = ex.getLocalizedMessage();
            if ( msg == null || msg.length() == 0 )
            {
                Integer id = tbTempo.getIdTempoPK();
                if ( findTbTempo( id ) == null )
                {
                    throw new NonexistentEntityException( "The tbTempo with id " + id + " no longer exists." );
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
            TbTempo tbTempo;
            try
            {
                tbTempo = em.getReference( TbTempo.class, id );
                tbTempo.getIdTempoPK();
            }
            catch ( EntityNotFoundException enfe )
            {
                throw new NonexistentEntityException( "The tbTempo with id " + id + " no longer exists.", enfe );
            }
            TbFuncionario idFuncionarioFK = tbTempo.getIdFuncionarioFK();
            if ( idFuncionarioFK != null )
            {
                idFuncionarioFK.getTbTempoList().remove( tbTempo );
                idFuncionarioFK = em.merge( idFuncionarioFK );
            }
            em.remove( tbTempo );
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

    public List<TbTempo> findTbTempoEntities()
    {
        return findTbTempoEntities( true, -1, -1 );
    }

    public List<TbTempo> findTbTempoEntities( int maxResults, int firstResult )
    {
        return findTbTempoEntities( false, maxResults, firstResult );
    }

    private List<TbTempo> findTbTempoEntities( boolean all, int maxResults, int firstResult )
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select( cq.from( TbTempo.class ) );
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

    public TbTempo findTbTempo( Integer id )
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find( TbTempo.class, id );
        }
        finally
        {
            em.close();
        }
    }

    public int getTbTempoCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TbTempo> rt = cq.from( TbTempo.class );
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
