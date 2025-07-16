/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import controlador.exceptions.NonexistentEntityException;
import entity.TbFalta;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entity.TbFuncionario;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author lenovo
 */
public class TbFaltaJpaController implements Serializable
{

    public TbFaltaJpaController( EntityManagerFactory emf )
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create( TbFalta tbFalta )
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            TbFuncionario idFuncionarioFK = tbFalta.getIdFuncionarioFK();
            if ( idFuncionarioFK != null )
            {
                idFuncionarioFK = em.getReference( idFuncionarioFK.getClass(), idFuncionarioFK.getIdFuncionario() );
                tbFalta.setIdFuncionarioFK( idFuncionarioFK );
            }
            em.persist( tbFalta );
            if ( idFuncionarioFK != null )
            {
                idFuncionarioFK.getTbFaltaList().add( tbFalta );
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

    public void edit( TbFalta tbFalta ) throws NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            TbFalta persistentTbFalta = em.find( TbFalta.class, tbFalta.getIdFaltaPK() );
            TbFuncionario idFuncionarioFKOld = persistentTbFalta.getIdFuncionarioFK();
            TbFuncionario idFuncionarioFKNew = tbFalta.getIdFuncionarioFK();
            if ( idFuncionarioFKNew != null )
            {
                idFuncionarioFKNew = em.getReference( idFuncionarioFKNew.getClass(), idFuncionarioFKNew.getIdFuncionario() );
                tbFalta.setIdFuncionarioFK( idFuncionarioFKNew );
            }
            tbFalta = em.merge( tbFalta );
            if ( idFuncionarioFKOld != null && !idFuncionarioFKOld.equals( idFuncionarioFKNew ) )
            {
                idFuncionarioFKOld.getTbFaltaList().remove( tbFalta );
                idFuncionarioFKOld = em.merge( idFuncionarioFKOld );
            }
            if ( idFuncionarioFKNew != null && !idFuncionarioFKNew.equals( idFuncionarioFKOld ) )
            {
                idFuncionarioFKNew.getTbFaltaList().add( tbFalta );
                idFuncionarioFKNew = em.merge( idFuncionarioFKNew );
            }
            em.getTransaction().commit();
        }
        catch ( Exception ex )
        {
            String msg = ex.getLocalizedMessage();
            if ( msg == null || msg.length() == 0 )
            {
                Integer id = tbFalta.getIdFaltaPK();
                if ( findTbFalta( id ) == null )
                {
                    throw new NonexistentEntityException( "The tbFalta with id " + id + " no longer exists." );
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
            TbFalta tbFalta;
            try
            {
                tbFalta = em.getReference( TbFalta.class, id );
                tbFalta.getIdFaltaPK();
            }
            catch ( EntityNotFoundException enfe )
            {
                throw new NonexistentEntityException( "The tbFalta with id " + id + " no longer exists.", enfe );
            }
            TbFuncionario idFuncionarioFK = tbFalta.getIdFuncionarioFK();
            if ( idFuncionarioFK != null )
            {
                idFuncionarioFK.getTbFaltaList().remove( tbFalta );
                idFuncionarioFK = em.merge( idFuncionarioFK );
            }
            em.remove( tbFalta );
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

    public List<TbFalta> findTbFaltaEntities()
    {
        return findTbFaltaEntities( true, -1, -1 );
    }

    public List<TbFalta> findTbFaltaEntities( int maxResults, int firstResult )
    {
        return findTbFaltaEntities( false, maxResults, firstResult );
    }

    private List<TbFalta> findTbFaltaEntities( boolean all, int maxResults, int firstResult )
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select( cq.from( TbFalta.class ) );
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

    public TbFalta findTbFalta( Integer id )
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find( TbFalta.class, id );
        }
        finally
        {
            em.close();
        }
    }

    public int getTbFaltaCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TbFalta> rt = cq.from( TbFalta.class );
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
