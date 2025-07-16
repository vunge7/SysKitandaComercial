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
import entity.TbUsuario;
import entity.Turno;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author lenovo
 */
public class TurnoJpaController implements Serializable
{

    public TurnoJpaController( EntityManagerFactory emf )
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create( Turno turno )
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            TbUsuario fkUsuario = turno.getFkUsuario();
            if ( fkUsuario != null )
            {
                fkUsuario = em.getReference( fkUsuario.getClass(), fkUsuario.getCodigo() );
                turno.setFkUsuario( fkUsuario );
            }
            em.persist( turno );
            if ( fkUsuario != null )
            {
                fkUsuario.getTurnoList().add( turno );
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

    public void edit( Turno turno ) throws NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            Turno persistentTurno = em.find( Turno.class, turno.getPkTurno() );
            TbUsuario fkUsuarioOld = persistentTurno.getFkUsuario();
            TbUsuario fkUsuarioNew = turno.getFkUsuario();
            if ( fkUsuarioNew != null )
            {
                fkUsuarioNew = em.getReference( fkUsuarioNew.getClass(), fkUsuarioNew.getCodigo() );
                turno.setFkUsuario( fkUsuarioNew );
            }
            turno = em.merge( turno );
            if ( fkUsuarioOld != null && !fkUsuarioOld.equals( fkUsuarioNew ) )
            {
                fkUsuarioOld.getTurnoList().remove( turno );
                fkUsuarioOld = em.merge( fkUsuarioOld );
            }
            if ( fkUsuarioNew != null && !fkUsuarioNew.equals( fkUsuarioOld ) )
            {
                fkUsuarioNew.getTurnoList().add( turno );
                fkUsuarioNew = em.merge( fkUsuarioNew );
            }
            em.getTransaction().commit();
        }
        catch ( Exception ex )
        {
            String msg = ex.getLocalizedMessage();
            if ( msg == null || msg.length() == 0 )
            {
                Integer id = turno.getPkTurno();
                if ( findTurno( id ) == null )
                {
                    throw new NonexistentEntityException( "The turno with id " + id + " no longer exists." );
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
            Turno turno;
            try
            {
                turno = em.getReference( Turno.class, id );
                turno.getPkTurno();
            }
            catch ( EntityNotFoundException enfe )
            {
                throw new NonexistentEntityException( "The turno with id " + id + " no longer exists.", enfe );
            }
            TbUsuario fkUsuario = turno.getFkUsuario();
            if ( fkUsuario != null )
            {
                fkUsuario.getTurnoList().remove( turno );
                fkUsuario = em.merge( fkUsuario );
            }
            em.remove( turno );
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

    public List<Turno> findTurnoEntities()
    {
        return findTurnoEntities( true, -1, -1 );
    }

    public List<Turno> findTurnoEntities( int maxResults, int firstResult )
    {
        return findTurnoEntities( false, maxResults, firstResult );
    }

    private List<Turno> findTurnoEntities( boolean all, int maxResults, int firstResult )
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select( cq.from( Turno.class ) );
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

    public Turno findTurno( Integer id )
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find( Turno.class, id );
        }
        finally
        {
            em.close();
        }
    }

    public int getTurnoCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Turno> rt = cq.from( Turno.class );
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
