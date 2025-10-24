/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;


import java.sql.Connection;
import controlador.exceptions.NonexistentEntityException;
import entity.AccessoArmazem;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entity.TbArmazem;
import entity.TbUsuario;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author lenovo
 */
public class AccessoArmazemJpaController implements Serializable
{

    public AccessoArmazemJpaController( EntityManagerFactory emf )
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create( AccessoArmazem accessoArmazem )
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            TbArmazem fkArmazem = accessoArmazem.getFkArmazem();
            if ( fkArmazem != null )
            {
                fkArmazem = em.getReference( fkArmazem.getClass(), fkArmazem.getCodigo() );
                accessoArmazem.setFkArmazem( fkArmazem );
            }
            TbUsuario fkUsuario = accessoArmazem.getFkUsuario();
            if ( fkUsuario != null )
            {
                fkUsuario = em.getReference( fkUsuario.getClass(), fkUsuario.getCodigo() );
                accessoArmazem.setFkUsuario( fkUsuario );
            }
            em.persist( accessoArmazem );
            if ( fkArmazem != null )
            {
                fkArmazem.getAccessoArmazemList().add( accessoArmazem );
                fkArmazem = em.merge( fkArmazem );
            }
            if ( fkUsuario != null )
            {
                fkUsuario.getAccessoArmazemList().add( accessoArmazem );
                fkUsuario = em.merge( fkUsuario );
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

    public void edit( AccessoArmazem accessoArmazem ) throws NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            AccessoArmazem persistentAccessoArmazem = em.find( AccessoArmazem.class, accessoArmazem.getPkAccessoArmazem() );
            TbArmazem fkArmazemOld = persistentAccessoArmazem.getFkArmazem();
            TbArmazem fkArmazemNew = accessoArmazem.getFkArmazem();
            TbUsuario fkUsuarioOld = persistentAccessoArmazem.getFkUsuario();
            TbUsuario fkUsuarioNew = accessoArmazem.getFkUsuario();
            if ( fkArmazemNew != null )
            {
                fkArmazemNew = em.getReference( fkArmazemNew.getClass(), fkArmazemNew.getCodigo() );
                accessoArmazem.setFkArmazem( fkArmazemNew );
            }
            if ( fkUsuarioNew != null )
            {
                fkUsuarioNew = em.getReference( fkUsuarioNew.getClass(), fkUsuarioNew.getCodigo() );
                accessoArmazem.setFkUsuario( fkUsuarioNew );
            }
            accessoArmazem = em.merge( accessoArmazem );
            if ( fkArmazemOld != null && !fkArmazemOld.equals( fkArmazemNew ) )
            {
                fkArmazemOld.getAccessoArmazemList().remove( accessoArmazem );
                fkArmazemOld = em.merge( fkArmazemOld );
            }
            if ( fkArmazemNew != null && !fkArmazemNew.equals( fkArmazemOld ) )
            {
                fkArmazemNew.getAccessoArmazemList().add( accessoArmazem );
                fkArmazemNew = em.merge( fkArmazemNew );
            }
            if ( fkUsuarioOld != null && !fkUsuarioOld.equals( fkUsuarioNew ) )
            {
                fkUsuarioOld.getAccessoArmazemList().remove( accessoArmazem );
                fkUsuarioOld = em.merge( fkUsuarioOld );
            }
            if ( fkUsuarioNew != null && !fkUsuarioNew.equals( fkUsuarioOld ) )
            {
                fkUsuarioNew.getAccessoArmazemList().add( accessoArmazem );
                fkUsuarioNew = em.merge( fkUsuarioNew );
            }
            em.getTransaction().commit();
        }
        catch ( Exception ex )
        {
            String msg = ex.getLocalizedMessage();
            if ( msg == null || msg.length() == 0 )
            {
                Integer id = accessoArmazem.getPkAccessoArmazem();
                if ( findAccessoArmazem( id ) == null )
                {
                    throw new NonexistentEntityException( "The accessoArmazem with id " + id + " no longer exists." );
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
            AccessoArmazem accessoArmazem;
            try
            {
                accessoArmazem = em.getReference( AccessoArmazem.class, id );
                accessoArmazem.getPkAccessoArmazem();
            }
            catch ( EntityNotFoundException enfe )
            {
                throw new NonexistentEntityException( "The accessoArmazem with id " + id + " no longer exists.", enfe );
            }
            TbArmazem fkArmazem = accessoArmazem.getFkArmazem();
            if ( fkArmazem != null )
            {
                fkArmazem.getAccessoArmazemList().remove( accessoArmazem );
                fkArmazem = em.merge( fkArmazem );
            }
            TbUsuario fkUsuario = accessoArmazem.getFkUsuario();
            if ( fkUsuario != null )
            {
                fkUsuario.getAccessoArmazemList().remove( accessoArmazem );
                fkUsuario = em.merge( fkUsuario );
            }
            em.remove( accessoArmazem );
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

    public List<AccessoArmazem> findAccessoArmazemEntities()
    {
        return findAccessoArmazemEntities( true, -1, -1 );
    }

    public List<AccessoArmazem> findAccessoArmazemEntities( int maxResults, int firstResult )
    {
        return findAccessoArmazemEntities( false, maxResults, firstResult );
    }

    private List<AccessoArmazem> findAccessoArmazemEntities( boolean all, int maxResults, int firstResult )
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select( cq.from( AccessoArmazem.class ) );
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

    public AccessoArmazem findAccessoArmazem( Integer id )
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find( AccessoArmazem.class, id );
        }
        finally
        {
            em.close();
        }
    }

    public int getAccessoArmazemCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<AccessoArmazem> rt = cq.from( AccessoArmazem.class );
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
