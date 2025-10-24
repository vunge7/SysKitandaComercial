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
import entity.TbFuncionario;
import entity.TbSalario;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author lenovo
 */
public class TbSalarioJpaController implements Serializable
{

    public TbSalarioJpaController( EntityManagerFactory emf )
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create( TbSalario tbSalario )
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            TbFuncionario idFuncionarioFK = tbSalario.getIdFuncionarioFK();
            if ( idFuncionarioFK != null )
            {
                idFuncionarioFK = em.getReference( idFuncionarioFK.getClass(), idFuncionarioFK.getIdFuncionario() );
                tbSalario.setIdFuncionarioFK( idFuncionarioFK );
            }
            em.persist( tbSalario );
            if ( idFuncionarioFK != null )
            {
                idFuncionarioFK.getTbSalarioList().add( tbSalario );
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

    public void edit( TbSalario tbSalario ) throws NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            TbSalario persistentTbSalario = em.find( TbSalario.class, tbSalario.getIdSalarioFK() );
            TbFuncionario idFuncionarioFKOld = persistentTbSalario.getIdFuncionarioFK();
            TbFuncionario idFuncionarioFKNew = tbSalario.getIdFuncionarioFK();
            if ( idFuncionarioFKNew != null )
            {
                idFuncionarioFKNew = em.getReference( idFuncionarioFKNew.getClass(), idFuncionarioFKNew.getIdFuncionario() );
                tbSalario.setIdFuncionarioFK( idFuncionarioFKNew );
            }
            tbSalario = em.merge( tbSalario );
            if ( idFuncionarioFKOld != null && !idFuncionarioFKOld.equals( idFuncionarioFKNew ) )
            {
                idFuncionarioFKOld.getTbSalarioList().remove( tbSalario );
                idFuncionarioFKOld = em.merge( idFuncionarioFKOld );
            }
            if ( idFuncionarioFKNew != null && !idFuncionarioFKNew.equals( idFuncionarioFKOld ) )
            {
                idFuncionarioFKNew.getTbSalarioList().add( tbSalario );
                idFuncionarioFKNew = em.merge( idFuncionarioFKNew );
            }
            em.getTransaction().commit();
        }
        catch ( Exception ex )
        {
            String msg = ex.getLocalizedMessage();
            if ( msg == null || msg.length() == 0 )
            {
                Integer id = tbSalario.getIdSalarioFK();
                if ( findTbSalario( id ) == null )
                {
                    throw new NonexistentEntityException( "The tbSalario with id " + id + " no longer exists." );
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
            TbSalario tbSalario;
            try
            {
                tbSalario = em.getReference( TbSalario.class, id );
                tbSalario.getIdSalarioFK();
            }
            catch ( EntityNotFoundException enfe )
            {
                throw new NonexistentEntityException( "The tbSalario with id " + id + " no longer exists.", enfe );
            }
            TbFuncionario idFuncionarioFK = tbSalario.getIdFuncionarioFK();
            if ( idFuncionarioFK != null )
            {
                idFuncionarioFK.getTbSalarioList().remove( tbSalario );
                idFuncionarioFK = em.merge( idFuncionarioFK );
            }
            em.remove( tbSalario );
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

    public List<TbSalario> findTbSalarioEntities()
    {
        return findTbSalarioEntities( true, -1, -1 );
    }

    public List<TbSalario> findTbSalarioEntities( int maxResults, int firstResult )
    {
        return findTbSalarioEntities( false, maxResults, firstResult );
    }

    private List<TbSalario> findTbSalarioEntities( boolean all, int maxResults, int firstResult )
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select( cq.from( TbSalario.class ) );
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

    public TbSalario findTbSalario( Integer id )
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find( TbSalario.class, id );
        }
        finally
        {
            em.close();
        }
    }

    public int getTbSalarioCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TbSalario> rt = cq.from( TbSalario.class );
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
